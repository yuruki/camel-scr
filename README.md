camel-scr
=========

Camel runner for SCR (OSGi)

Running Camel in an SCR bundle is a great alternative for the more common methods (Spring DM and Blueprint). Using Camel runner for SCR your bundle can remain completely in Java world; there is no need to create or modify any XML or properties files. This offers us full control over everything and also means that your IDE knows exactly what is going on in your project.

AbstractCamelRunner ties CamelContext's lifecycle to SCR and handles configuration with Camel's PropertiesComponent. All you have to do is to extends your Service Component class from AbstractCamelRunner and set the following references on class level:

```
@References({
  @Reference(...coming soon)
})
```

and provide the default configuration on class level as well:

```
@Properties({
  @Property(name = "...", value = "..."),
  @Property(...),
  ...
})
```

AbstractCamelRunner will make these properties available to your RouteBuilders through Camel's PropertiesComponent AND it will also inject these values into your Service Component class' and RouteBuilder's fields when their names match. The fields can be declared with any visibility level, and many types are supported (String, int, boolean, URL, ...).

### Lifecycle

AbstractCamelRunner lifecycle in SCR:

1. When component's configuration policy and mandatory references are satisfied SCR calls activate(). This creates and sets up a CamelContext through the following call chain: activate() -> prepare() -> createCamelContext() -> setupPropertiesComponent() -> configure() -> setupCamelContext(). Finally, the context is scheduled to start after START_DELAY with runWithDelay().
2. When Camel components (actually ComponentResolvers) are registered SCR calls gotCamelComponent() which reschedules the CamelContext start to happen after START_DELAY. This causes the CamelContext to wait until all Camel components are loaded or there is a sufficient gap between them. The same logic will reschedule a failed-to-start CamelContext whenever we add more (hopefully the missing ones) Camel components.
3. When Camel components are unregistered SCR calls lostCamelComponent(). This is a no-op.
4. When one of the requirements that caused the activate() is lost SCR will call deactivate(). This will shutdown the CamelContext.

In unit tests we don't generally use activate() -> deactivate(), but prepare() -> run() -> stop() for a more fine-grained control. Also, this allows us to avoid possible SCR specific operations in tests.

Usage and camel-archetype-scr coming soon...
