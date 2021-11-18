package io.github.lasyard.camel.file;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

public class SimpleMockTest extends CamelTestSupport {
    @Override
    protected RoutesBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("direct:start").to("mock:result");
            }
        };
    }

    @Test
    public void testMock() throws Exception {
        getMockEndpoint("mock:result").expectedBodiesReceived("Hello World");
        template().sendBody("direct:start", "Hello World");
        assertMockEndpointsSatisfied();
    }
}
