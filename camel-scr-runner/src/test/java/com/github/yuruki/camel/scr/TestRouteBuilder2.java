package com.github.yuruki.camel.scr;

import org.apache.camel.builder.RouteBuilder;

public class TestRouteBuilder2 extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct://start2")
                .routeId("gugguu")
                .log("{{messageOk}}")
                .to("{{to}}");
    }
}
