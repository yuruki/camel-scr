#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
// This file was generated from ${archetypeGroupId}/${archetypeArtifactId}/${archetypeVersion}
package ${groupId}.internal;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.commons.lang.Validate;

public class ${className}Route extends RouteBuilder {

    SimpleRegistry registry;

    // Configured fields
    @SuppressWarnings("unused")
    private String camelRouteId;
    @SuppressWarnings("unused")
    private Integer maximumRedeliveries;
    @SuppressWarnings("unused")
    private Long redeliveryDelay;
    @SuppressWarnings("unused")
    private Double backOffMultiplier;
    @SuppressWarnings("unused")
    private Long maximumRedeliveryDelay;

    public ${className}Route(final SimpleRegistry registry) {
        this.registry = registry;
    }

    @Override
	public void configure() throws Exception {
        checkProperties();

        // Add a bean to Camel context registry
        // registry.put("test", "bean");

        errorHandler(defaultErrorHandler()
            .maximumRedeliveries(maximumRedeliveries)
            .redeliveryDelay(redeliveryDelay)
            .backOffMultiplier(backOffMultiplier)
            .maximumRedeliveryDelay(maximumRedeliveryDelay));

        from("{{from}}")
            .routeId(camelRouteId)
            .onCompletion()
                .to("direct:processCompletion")
            .end()
            .removeHeaders("*", "breadcrumbId")
            .to("{{to}}");

        from("direct:processCompletion")
            .routeId(camelRouteId + ".completion")
            .choice()
                .when(simple("${exception} == null"))
                    .log("{{messageOk}}")
                .otherwise()
                    .log(LoggingLevel.ERROR, "{{messageError}}")
            .endChoice();
	}

    public void checkProperties() {
        Validate.notNull(camelRouteId, "camelRouteId property is not set");
        Validate.notNull(maximumRedeliveries, "maximumRedeliveries property is not set");
        Validate.notNull(redeliveryDelay, "redeliveryDelay property is not set");
        Validate.notNull(backOffMultiplier, "backOffMultiplier property is not set");
        Validate.notNull(maximumRedeliveryDelay, "maximumRedeliveryDelay property is not set");
    }
}