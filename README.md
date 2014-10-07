camel-scr
=========

Camel runner for SCR (OSGi)

Running Camel in an SCR bundle is a great alternative for the more common methods (Spring DM and Blueprint). Using Camel runner for SCR your bundle can remain completely in Java world; there is no need to create or modify any XML or properties files. This offers us full control over everything and also means that your IDE knows exactly what is going on in your project.

AbstractCamelRunner ties CamelContext's lifecycle to SCR and handles configuration with Camel's PropertiesComponent.

AbstractCamelRunner lifecycle in SCR:

1. When component's configuration policy and mandatory references are satisfied SCR calls activate(). This creates and sets up a CamelContext through the following call chain: activate() -> prepare() -> createCamelContext() -> setupPropertiesComponent() -> configure() -> setupCamelContext(). Finally the context is scheduled to start after START_DELAY.
2. When Camel components (actually ComponentResolvers) are registered SCR calls gotCamelComponent() which reschedules the CamelContext start to happen after START_DELAY. This causes the CamelContext to wait until all Camel components are loaded or there is a sufficient gap between them. The same logic will restart a failed to start CamelContext whenever we add more (hopefully the missing ones) Camel components.
3. When Camel components are unregistered SCR calls lostCamelComponent(). This is a no-op.
4. When one of the requirements that caused the activate() is lost SCR will call deactivate(). This will shutdown the CamelContext.

Usage and camel-archetype-scr coming soon...
