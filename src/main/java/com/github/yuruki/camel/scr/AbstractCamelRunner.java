package com.github.yuruki.camel.scr;

import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.core.osgi.OsgiDefaultCamelContext;
import org.apache.camel.core.osgi.utils.BundleDelegatingClassLoader;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.ExplicitCamelContextNameStrategy;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.spi.ComponentResolver;
import org.apache.camel.util.ReflectionHelper;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class AbstractCamelRunner implements Runnable {

    protected Logger log = LoggerFactory.getLogger(getClass());
    protected CamelContext context;
    protected SimpleRegistry registry = new SimpleRegistry();

    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture starter;
    private boolean activated = false;
    private boolean started = false;

    public static final int START_DELAY = 5000;
    public static final String PROPERTY_PREFIX = "camel.properties.prefix";

    // Configured fields
    public String camelContextId = "camel-runner-default";
    public boolean active = false;

    public synchronized void activate(final BundleContext bundleContext, final Map<String, String> props) throws Exception {
        if (activated) return;
        log.debug("activated!");

        activated = true;

        doActivate(bundleContext, props);

        runWithDelay(this);
    }

    protected void doActivate(final BundleContext bundleContext, final Map<String, String> props) throws Exception {
        createCamelContext(bundleContext, props);

        // Configure fields from properties
        configure(context, this, log);

        setupCamelContext(this.camelContextId);
    }

    protected void createCamelContext(final BundleContext bundleContext, final Map<String, String> props) {
        if (null != bundleContext) {
            context = new OsgiDefaultCamelContext(bundleContext, registry);
            // From https://issues.jboss.org/browse/MR-911
            // Setup the application context classloader with the bundle classloader
            context.setApplicationContextClassLoader(new BundleDelegatingClassLoader(bundleContext.getBundle()));
            // and make sure the TCCL is our classloader
            Thread.currentThread().setContextClassLoader(context.getApplicationContextClassLoader());
        } else {
            context = new DefaultCamelContext(registry);
        }
        setupPropertiesComponent(context, props, log);
    }

    protected void setupCamelContext(final String camelContextId) throws Exception {
        // Setup
        context.setNameStrategy(new ExplicitCamelContextNameStrategy(camelContextId));
        context.setUseMDCLogging(true);
        context.setUseBreadcrumb(true);

        // Add routes
        for (RoutesBuilder route : getRouteBuilders()) {
            context.addRoutes(configure(context, route, log));
        }
    }

    public static void setupPropertiesComponent(final CamelContext context, final Map<String, String> props, Logger log) {
        // Set up PropertiesComponent
        PropertiesComponent pc = new PropertiesComponent();
        if (context.getComponentNames().contains("properties")) {
            pc = context.getComponent("properties", PropertiesComponent.class);
        } else {
            context.addComponent("properties", pc);
        }

        // Set property prefix
        if (null != System.getProperty(PROPERTY_PREFIX)) {
            pc.setPropertyPrefix(System.getProperty(PROPERTY_PREFIX) + ".");
        }

        if (null != props) {
            Properties overrideProps = new Properties();
            overrideProps.putAll(props);
            log.debug(String.format("Added %d override properties", props.size()));
            pc.setOverrideProperties(overrideProps);
        }

        // Add default properties to component
        pc.setLocation("default.properties");
    }

    protected abstract List<RoutesBuilder> getRouteBuilders();

    // Run after a delay unless the method is called again
    private synchronized void runWithDelay(final Runnable runnable) {
        if (activated && !started) {
            cancelDelayedRun();
            // Run after a delay
            starter = executor.schedule(runnable, START_DELAY, TimeUnit.MILLISECONDS);
        }
    }

    private void cancelDelayedRun() {
        if (null != starter) {
            // Cancel but don't interrupt
            starter.cancel(false);
        }
    }

    public synchronized void run() {
        startCamelContext();
    }

    public synchronized void deactivate() {
        if (!activated) return;
        log.debug("deactivated!");

        activated = false;

        cancelDelayedRun();

        doDeactivate();

        stopCamelContext();
    }

    protected void doDeactivate() {
        // nop
    }

    protected synchronized void startCamelContext() {
        if (started) return;
        try {
            if (active) {
                context.start();
            } else {
                log.info(camelContextId + " not started (active property is not true)");
            }
            started = true;
        } catch (Exception e) {
            log.error("Failed to start Camel context. Will try again when more Camel components have been registered.", e);
        }
    }

    protected synchronized void stopCamelContext() {
        if (!started) return;
        try {
            context.stop();
        } catch (Exception e) {
            log.error("Failed to stop Camel context.", e);
        } finally {
            // Even if stopping failed we consider Camel context stopped
            started = false;
        }
    }

    @SuppressWarnings("unused")
    protected void gotCamelComponent(final ComponentResolver componentResolver) {
        log.trace("Got a new Camel Component.");
        runWithDelay(this);
    }

    @SuppressWarnings("unused")
    protected void lostCamelComponent(final ComponentResolver componentResolver) {
        log.trace("Lost a Camel Component.");
    }

    public static <T> T configure(final CamelContext context, final T target, final Logger log) {
        Class clazz = target.getClass();
        log.debug("Configuring " + clazz.getName());
        Collection<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        fields.addAll(Arrays.asList(clazz.getFields()));
        for (Field field : fields) {
            try {
                String propertyValue = context.resolvePropertyPlaceholders("{{" + field.getName() + "}}");
                if (!propertyValue.isEmpty()) {
                    // Try to convert the value and set the field
                    Object convertedValue = convertValue(propertyValue, field.getGenericType());
                    ReflectionHelper.setField(field, target, convertedValue);
                    log.debug("Set field " + field.getName() + " with value " + propertyValue);
                }
            } catch (Exception e) {
                log.debug("Field " + field.getName() + " skipped", e);
            }
        }
        return target;
    }

    public static Object convertValue(final String value, final Type type) throws Exception {
        Class<?> clazz = null;
        if (type instanceof ParameterizedType) {
            clazz = (Class<?>) ((ParameterizedType) type).getRawType();
        } else if (type instanceof Class) {
            clazz = (Class) type;
        }
        if (null != value) {
            if (clazz.isInstance(value)) {
                return value;
            } else if (clazz == String.class) {
                return value;
            } else if (clazz == Boolean.class || clazz == boolean.class) {
                return Boolean.parseBoolean(value);
            } else if (clazz == Integer.class || clazz == int.class) {
                return Integer.parseInt(value);
            } else if (clazz == Long.class || clazz == long.class) {
                return Long.parseLong(value);
            } else if (clazz == Double.class || clazz == double.class) {
                return Double.parseDouble(value);
            } else if (clazz == File.class) {
                return new File(value);
            } else if (clazz == URI.class) {
                return new URI(value);
            } else if (clazz == URL.class) {
                return new URL(value);
            } else {
                throw new IllegalArgumentException("Unknown type: "+ (clazz != null ? clazz.getName() : null));
            }
        } else {
            return null;
        }
    }
}
