camel-archetype-scr
===================

Maven archetype for creating Camel SCR (OSGi Declarative Services) bundles

This archetype can be used to create ready-to-run Camel SCR bundle projects. As usual, dependency versions have to be matched to the framework where you want to run the bundle. The defaults work in Red Hat JBoss Fuse 6.1.

### Generating a project

First you have to install camel-scr artifact (https://github.com/yuruki/camel-scr):

    camel-scr
    $ mvn install
  
Then we install camel-archetype-scr:

    camel-archetype-scr
    $ mvn install
  
Let's generate a project:

    your_git_repositories
    $ mvn archetype:generate -Dfilter=com.github.yuruki.camel.scr:
  
    [INFO] Scanning for projects...
    [INFO]
    [INFO] Using the builder org.apache.maven.lifecycle.internal.builder.singlethreaded.SingleThreadedBuilder with a thread count of 1
    [INFO]
    [INFO] ------------------------------------------------------------------------
    [INFO] Building Maven Stub Project (No POM) 1
    [INFO] ------------------------------------------------------------------------
    [INFO]
    [INFO] >>> maven-archetype-plugin:2.2:generate (default-cli) @ standalone-pom >>>
    [INFO]
    [INFO] <<< maven-archetype-plugin:2.2:generate (default-cli) @ standalone-pom <<<
    [INFO]
    [INFO] --- maven-archetype-plugin:2.2:generate (default-cli) @ standalone-pom ---
    [INFO] Generating project in Interactive mode
    [INFO] No archetype defined. Using maven-archetype-quickstart (org.apache.maven.archetypes:maven-archetype-quickstart:1.0)
    Choose archetype:
    1: local -> com.github.yuruki.camel.scr:camel-archetype-scr (camel-archetype-scr)
    Choose a number or apply filter (format: [groupId:]artifactId, case sensitive contains): : 1
    Define value for property 'groupId': : example
    [INFO] Using property: groupId = example
    Define value for property 'artifactId': : camel-scr-example
    Define value for property 'version':  1.0-SNAPSHOT: :
    Define value for property 'package':  example: :
    [INFO] Using property: archetypeArtifactId = camel-archetype-scr
    [INFO] Using property: archetypeGroupId = com.github.yuruki.camel.scr
    Define value for property 'archetypeVersion': : 1.0-SNAPSHOT
    Define value for property 'className': : CamelScrExample
    Confirm properties configuration:
    groupId: example
    groupId: example
    artifactId: camel-scr-example
    version: 1.0-SNAPSHOT
    package: example
    archetypeArtifactId: camel-archetype-scr
    archetypeGroupId: com.github.yuruki.camel.scr
    archetypeVersion: 1.0-SNAPSHOT
    className: CamelScrExample
     Y: :
    [INFO] ----------------------------------------------------------------------------
    [INFO] Using following parameters for creating project from Archetype: camel-archetype-scr:1.0-SNAPSHOT
    [INFO] ----------------------------------------------------------------------------
    [INFO] Parameter: groupId, Value: example
    [INFO] Parameter: artifactId, Value: camel-scr-example
    [INFO] Parameter: version, Value: 1.0-SNAPSHOT
    [INFO] Parameter: package, Value: example
    [INFO] Parameter: packageInPathFormat, Value: example
    [INFO] Parameter: package, Value: example
    [INFO] Parameter: version, Value: 1.0-SNAPSHOT
    [INFO] Parameter: archetypeArtifactId, Value: camel-archetype-scr
    [INFO] Parameter: groupId, Value: example
    [INFO] Parameter: className, Value: CamelScrExample
    [INFO] Parameter: archetypeVersion, Value: 1.0-SNAPSHOT
    [INFO] Parameter: archetypeGroupId, Value: com.github.yuruki.camel.scr
    [INFO] Parameter: artifactId, Value: camel-scr-example
    [INFO] project created from Archetype in dir: c:\Repositories\Git\camel-scr-example
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 03:52 min
    [INFO] Finished at: 2014-10-09T12:25:26+02:00
    [INFO] Final Memory: 14M/240M
    [INFO] ------------------------------------------------------------------------
    
Build and install the bundle:

    your_git_repositories
    $ cd camel-scr-example
    
    camel-scr-example
    $ mvn install
    
    [INFO] Scanning for projects...
    [INFO]
    [INFO] Using the builder org.apache.maven.lifecycle.internal.builder.singlethreaded.SingleThreadedBuilder with a thread count of 1
    [INFO]
    [INFO] ------------------------------------------------------------------------
    [INFO] Building camel-scr-example 1.0-SNAPSHOT
    [INFO] ------------------------------------------------------------------------
    [INFO]
    [INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ camel-scr-example ---
    [INFO] Using 'UTF-8' encoding to copy filtered resources.
    [INFO] Copying 2 resources
    [INFO]
    [INFO] --- maven-compiler-plugin:3.1:compile (default-compile) @ camel-scr-example ---
    [INFO] Changes detected - recompiling the module!
    [INFO] Compiling 2 source files to c:\Repositories\Git\camel-scr-example\target\classes
    [INFO]
    [INFO] --- maven-scr-plugin:1.19.0:scr (generate-scr-scrdescriptor) @ camel-scr-example ---
    [INFO] Generating 1 MetaType Descriptors in c:\Repositories\Git\camel-scr-example\target\classes\OSGI-INF\metatype\example.CamelScrExample.xml
    [INFO] Writing 1 Service Component Descriptors to c:\Repositories\Git\camel-scr-example\target\classes\OSGI-INF\example.CamelScrExample.xml
    [INFO]
    [INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ camel-scr-example ---
    [INFO] Using 'UTF-8' encoding to copy filtered resources.
    [INFO] Copying 1 resource
    [INFO]
    [INFO] --- maven-compiler-plugin:3.1:testCompile (default-testCompile) @ camel-scr-example ---
    [INFO] Changes detected - recompiling the module!
    [INFO] Compiling 1 source file to c:\Repositories\Git\camel-scr-example\target\test-classes
    [INFO]
    [INFO] --- maven-surefire-plugin:2.17:test (default-test) @ camel-scr-example ---
    [INFO] Surefire report directory: c:\Repositories\Git\camel-scr-example\target\surefire-reports
    
    -------------------------------------------------------
     T E S T S
    -------------------------------------------------------
    Running example.CamelScrExampleTest
    [                          main] CamelScrExample                DEBUG Added 12 override properties
    [                          main] CamelScrExample                DEBUG Configuring example.CamelScrExample
    [                          main] CamelScrExample                DEBUG Field COMPONENT_LABEL skipped: Property with key [unit.COMPONENT_LABEL] (and original key [COMPONENT_LABEL]) not found in properties from text: {{COMPONENT_LABEL}}
    [                          main] CamelScrExample                DEBUG Field COMPONENT_DESCRIPTION skipped: Property with key [unit.COMPONENT_DESCRIPTION] (and original key [COMPONENT_DESCRIPTION]) not found in properties from text: {{COMPONENT_DESCRIPTION}}
    [                          main] CamelScrExample                DEBUG Field COMPONENT_LABEL skipped: Property with key [unit.COMPONENT_LABEL] (and original key [COMPONENT_LABEL]) not found in properties from text: {{COMPONENT_LABEL}}
    [                          main] CamelScrExample                DEBUG Field COMPONENT_DESCRIPTION skipped: Property with key [unit.COMPONENT_DESCRIPTION] (and original key [COMPONENT_DESCRIPTION]) not found in properties from text: {{COMPONENT_DESCRIPTION}}
    [                          main] CamelScrExample                DEBUG Field START_DELAY skipped: Property with key [unit.START_DELAY] (and original key [START_DELAY]) not found in properties from text: {{START_DELAY}}
    [                          main] CamelScrExample                DEBUG Field PROPERTY_PREFIX skipped: Property with key [unit.PROPERTY_PREFIX] (and original key [PROPERTY_PREFIX]) not found in properties from text: {{PROPERTY_PREFIX}}
    [                          main] CamelScrExample                DEBUG Set field camelContextId with value camel-scr-example
    [                          main] CamelScrExample                DEBUG Set field active with value true
    [                          main] CamelScrExample                DEBUG Configuring example.internal.CamelScrExampleRoute
    [                          main] CamelScrExample                DEBUG Field registry skipped: Property with key [unit.registry] (and original key [registry]) not found in properties from text: {{registry}}
    [                          main] CamelScrExample                DEBUG Set field maximumRedeliveries with value 0
    [                          main] CamelScrExample                DEBUG Set field redeliveryDelay with value 5000
    [                          main] CamelScrExample                DEBUG Set field backOffMultiplier with value 2
    [                          main] CamelScrExample                DEBUG Set field maximumRedeliveryDelay with value 60000
    [                          main] CamelScrExample                DEBUG Set field camelRouteId with value foo/timer-log
    [                          main] AdviceWithTasks                INFO  AdviceWith replace input from [{{from}}] --> [direct:start]
    [                          main] RouteDefinition                INFO  AdviceWith route after: Route(foo/timer-log)[[From[direct:start]] -> [onCompletion[[To[direct:processCompletion]]], RemoveHeaders[*], To[{{to}}]]]
    [                          main] DefaultCamelContext            INFO  Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-scr-example) is starting
    [                          main] DefaultCamelContext            INFO  MDC logging is enabled on CamelContext: camel-scr-example
    [                          main] ManagedManagementStrategy      INFO  JMX is enabled
    [                          main] DefaultTypeConverter           INFO  Loaded 175 type converters
    [                          main] ceptSendToMockEndpointStrategy INFO  Adviced endpoint [log://foo?showHeaders=true] with mock endpoint [mock:log:foo]
    [                          main] DefaultCamelContext            INFO  AllowUseOriginalMessage is enabled. If access to the original message is not needed, then its recommended to turn this option off as it may improve performance.
    [                          main] DefaultCamelContext            INFO  StreamCaching is not in use. If using streams then its recommended to enable stream caching. See more details at http://camel.apache.org/stream-caching.html
    [                          main] DefaultCamelContext            INFO  Route: foo/timer-log started and consuming from: Endpoint[direct://start]
    [                          main] DefaultCamelContext            INFO  Route: foo/timer-log.completion started and consuming from: Endpoint[direct://processCompletion]
    [                          main] DefaultCamelContext            INFO  Total 2 routes, of which 2 is started.
    [                          main] DefaultCamelContext            INFO  Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-scr-example) started in 0.374 seconds
    [                          main] MockEndpoint                   INFO  Asserting: Endpoint[mock://log:foo] is satisfied
    [                          main] DefaultCamelContext            INFO  Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-scr-example) is shutting down
    [                          main] DefaultShutdownStrategy        INFO  Starting to graceful shutdown 2 routes (timeout 300 seconds)
    [mple) thread #2 - ShutdownTask] DefaultShutdownStrategy        INFO  Waiting as there are still 1 inflight and pending exchanges to complete, timeout in 300 seconds.
    [mple) thread #1 - OnCompletion] completion                     INFO  Success: timer:foo?period=5000 -> log:foo?showHeaders=true
    [mple) thread #2 - ShutdownTask] DefaultShutdownStrategy        INFO  Route: foo/timer-log.completion shutdown complete, was consuming from: Endpoint[direct://processCompletion]
    [mple) thread #2 - ShutdownTask] DefaultShutdownStrategy        INFO  Route: foo/timer-log shutdown complete, was consuming from: Endpoint[direct://start]
    [                          main] DefaultShutdownStrategy        INFO  Graceful shutdown of 2 routes completed in 1 seconds
    [                          main] DefaultCamelContext            INFO  Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-scr-example) uptime 1.423 seconds
    [                          main] DefaultCamelContext            INFO  Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-scr-example) is shutdown in 1.019 seconds
    Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.926 sec - in example.CamelScrExampleTest
    
    Results :
    
    Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
    
    [INFO]
    [INFO] --- maven-bundle-plugin:2.4.0:bundle (default-bundle) @ camel-scr-example ---
    [INFO]
    [INFO] --- maven-install-plugin:2.5.2:install (default-install) @ camel-scr-example ---
    [INFO] Installing c:\Repositories\Git\camel-scr-example\target\camel-scr-example-1.0-SNAPSHOT.jar to C:\Users\jnir\.m2\repository\example\camel-scr-example\1.0-SNAPSHOT\camel-scr-example-1.0-SNAPSHOT.jar
    [INFO] Installing c:\Repositories\Git\camel-scr-example\pom.xml to C:\Users\jnir\.m2\repository\example\camel-scr-example\1.0-SNAPSHOT\camel-scr-example-1.0-SNAPSHOT.pom
    [INFO]
    [INFO] --- maven-bundle-plugin:2.4.0:install (default-install) @ camel-scr-example ---
    [INFO] Installing example/camel-scr-example/1.0-SNAPSHOT/camel-scr-example-1.0-SNAPSHOT.jar
    [INFO] Writing OBR metadata
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 5.913 s
    [INFO] Finished at: 2014-10-09T12:30:29+02:00
    [INFO] Final Memory: 24M/431M
    [INFO] ------------------------------------------------------------------------

That's it. The example can now be deployed in JBoss Fuse.
