package com.github.yuruki.camel.scr;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class AbstractCamelRunnerTest {

    Logger log = LoggerFactory.getLogger(getClass());

    @Rule
    public TestName testName = new TestName();

    @Before
    public void setUp() throws Exception {
        log.info("*******************************************************************");
        log.info("Test: " + testName.getMethodName());
        log.info("*******************************************************************");
    }

    @Test
    public void testActivateDeactivate() {
        ConcreteCamelRunner integration = new ConcreteCamelRunner();
        try {
            integration.activate(null, integration.getDefaultProperties());
            Thread.sleep(ConcreteCamelRunner.START_DELAY + 1000);
            integration.deactivate();
            assertTrue("Camel context has not started.", integration.camelContextStarted == 1);
            assertTrue("Camel context has not stopped.", integration.camelContextStopped == 1);
            assertTrue("Not enough routes added.", integration.routeAdded == 2);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testPrepareRunStop() {
        ConcreteCamelRunner integration = new ConcreteCamelRunner();
        try {
            integration.prepare(null, integration.getDefaultProperties());
            integration.run();
            do {
                Thread.sleep(500);
            } while (integration.getContext().isStartingRoutes());
            integration.stop();
            assertTrue("Camel context has not started.", integration.camelContextStarted == 1);
            assertTrue("Camel context has not stopped.", integration.camelContextStopped == 1);
            assertTrue("Not enough routes added.", integration.routeAdded == 2);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testDelayedStart() {
        ConcreteCamelRunner integration = new ConcreteCamelRunner();
        try {
            integration.activate(null, integration.getDefaultProperties());
            Thread.sleep(2000);
            integration.gotCamelComponent(null);
            Thread.sleep(ConcreteCamelRunner.START_DELAY - 1000);
            assertTrue("Camel context has started too early", integration.camelContextStarted == 0);
            Thread.sleep(2000);
            assertTrue("Camel context has not started.", integration.camelContextStarted == 1);
            integration.deactivate();
            assertTrue("Camel context has not stopped.", integration.camelContextStopped == 1);
            assertTrue("Not enough routes added.", integration.routeAdded == 2);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testDelayedStartCancel() {
        ConcreteCamelRunner integration = new ConcreteCamelRunner();

        Map<String, String> properties = integration.getDefaultProperties();
        properties.put("from", "notfound:something");
        properties.put("camelroute.id", "test/notfound-mock");

        try {
            integration.activate(null, properties);
            Thread.sleep(ConcreteCamelRunner.START_DELAY - 1000);
            integration.deactivate();
            assertTrue("Routes have been added.", integration.routeAdded == 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}