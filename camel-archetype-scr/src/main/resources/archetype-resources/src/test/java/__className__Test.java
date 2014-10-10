#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
// This file was generated from ${archetypeGroupId}/${archetypeArtifactId}/${archetypeVersion}
package ${groupId};

import com.github.yuruki.camel.scr.ScrHelper;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockComponent;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

@RunWith(JUnit4.class)
public class ${className}Test {

    Logger log = LoggerFactory.getLogger(getClass());

    @Rule
    public TestName testName = new TestName();

    ${className} integration;
    ModelCamelContext context;

    @Before
    public void setUp() throws Exception {
        log.info("*******************************************************************");
        log.info("Test: " + testName.getMethodName());
        log.info("*******************************************************************");

        // Set property prefix for unit testing
        System.setProperty(${className}.PROPERTY_PREFIX, "unit");

        // Prepare the integration
        integration = new ${className}();
        integration.prepare(null, ScrHelper.getScrProperties(integration.getClass().getName()));
        context = integration.getContext();

        // Fake a component for test
        // context.addComponent("amq", new MockComponent());
    }

    @After
    public void tearDown() throws Exception {
        integration.stop();
    }

	@Test
	public void testRoutes() throws Exception {
        // Adjust routes
        List<RouteDefinition> routes = context.getRouteDefinitions();

        routes.get(0).adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                // Replace "from" endpoint with direct:start
                replaceFromWith("direct:start");
                // Mock and skip result endpoint
                mockEndpointsAndSkip("log:*");
            }
        });

        MockEndpoint resultEndpoint = context.getEndpoint("mock:log:foo", MockEndpoint.class);
        // resultEndpoint.expectedMessageCount(1); // If you want to just check the number of messages
        resultEndpoint.expectedBodiesReceived("hello"); // If you want to check the contents

        // You can also take the expected result from an external file
        // String result = IOUtils.toString(context.getClassResolver().loadResourceAsStream("testdata/out/result.txt"));
        // resultEndpoint.expectedBodiesReceived(result.replaceAll("\r?\n", "\n"));

        // Start the integration
        integration.run();

        // Send the test message
        context.createProducerTemplate().sendBody("direct:start", "hello");

        // You can also send an external file
        // template.sendBody(context.getClassResolver().loadResourceAsStream("testdata/in/input.xml"));

        // REST/HTTP services can be easily tested with RestAssured:
        // get(context.resolvePropertyPlaceholders("{{restUrl}}")).then().statusCode(204).body(isEmptyOrNullString());
        // given().param("status").get(context.resolvePropertyPlaceholders("{{restUrl}}")).then().statusCode(200).body(equalTo("active"));
        // given().auth().basic("testuser", "testpass").body("hello").when().post(context.resolvePropertyPlaceholders("{{restUrl}}")).then().statusCode(200).body(equalTo("response"));

        resultEndpoint.assertIsSatisfied();
	}
}
