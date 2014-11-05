#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
// This file was generated from ${archetypeGroupId}/${archetypeArtifactId}/${archetypeVersion}
package ${groupId};

import com.github.yuruki.camel.scr.AbstractCamelRunner;
import ${groupId}.internal.${className}Route;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.spi.ComponentResolver;
import org.apache.felix.scr.annotations.*;

import java.util.ArrayList;
import java.util.List;

@Component(label = ${className}.COMPONENT_LABEL, description = ${className}.COMPONENT_DESCRIPTION, immediate = true, metatype = true)
@Properties({
        @Property(name = "camelContextId", value = "${artifactId}"),
        @Property(name = "camelRouteId", value = "foo/timer-log"),
        @Property(name = "active", value = "true"),
        @Property(name = "from", value = "timer:foo?period=5000"),
        @Property(name = "to", value = "log:foo?showHeaders=true"),
        @Property(name = "summaryLogging", value = "false"),
        @Property(name = "messageOk", value = "Success: {{from}} -> {{to}}"),
        @Property(name = "messageError", value = "Failure: {{from}} -> {{to}}"),
        @Property(name = "maximumRedeliveries", value = "0"),
        @Property(name = "redeliveryDelay", value = "5000"),
        @Property(name = "backOffMultiplier", value = "2"),
        @Property(name = "maximumRedeliveryDelay", value = "60000")
})
@References({
        @Reference(name = "camelComponent",referenceInterface = ComponentResolver.class,
                cardinality = ReferenceCardinality.MANDATORY_MULTIPLE, policy = ReferencePolicy.DYNAMIC,
                policyOption = ReferencePolicyOption.GREEDY, bind = "gotCamelComponent", unbind = "lostCamelComponent")
})
public class ${className} extends AbstractCamelRunner {

    public static final String COMPONENT_LABEL = "${groupId}.${className}";
    public static final String COMPONENT_DESCRIPTION = "This is the current configuration for ${artifactId}.";

    @Override
    protected List<RoutesBuilder>getRouteBuilders() {
        List<RoutesBuilder>routesBuilders = new ArrayList<>();
        routesBuilders.add(new ${className}Route(registry));
        return routesBuilders;
    }
}
