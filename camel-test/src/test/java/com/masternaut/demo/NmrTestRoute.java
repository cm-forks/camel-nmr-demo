package com.masternaut.demo;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.servicemix.camel.nmr.AbstractComponentTest;
import org.junit.Test;

/**
 * A Camel NMR Test Router
 */
public class NmrTestRoute extends AbstractComponentTest {

    private static final String REQUEST_MESSAGE = "Simple message body";
    private static final String RESPONSE_MESSAGE = "Simple message reply";

    @Test
    public void testSimpleInOnly() throws InterruptedException {
        MockEndpoint mock = getMockEndpoint("mock:simple");
        mock.expectedBodiesReceived(REQUEST_MESSAGE);

        template.sendBody("direct:simple", REQUEST_MESSAGE);

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testSimpleInOnlyWithMultipleHops() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:hops");
        mock.expectedBodiesReceived(REQUEST_MESSAGE);

        template.sendBody("direct:hops", REQUEST_MESSAGE);

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testSimpleInOut() throws InterruptedException {
        MockEndpoint mock = getMockEndpoint("mock:simple");
        mock.expectedBodiesReceived(REQUEST_MESSAGE);

        final String response = template.requestBody("direct:simple", REQUEST_MESSAGE, String.class);

        assertMockEndpointsSatisfied();
        assertEquals("Receiving back the reply set by the second route",
                     RESPONSE_MESSAGE, response);
    }

    @Test
    public void testSimpleInOutWithMultipleHops() throws InterruptedException {
        MockEndpoint mock = getMockEndpoint("mock:hops");
        mock.expectedBodiesReceived(REQUEST_MESSAGE);

        final String response = template.requestBody("direct:hops", REQUEST_MESSAGE, String.class);

        assertMockEndpointsSatisfied();
        assertEquals("Receiving back the reply set by the second route",
                     RESPONSE_MESSAGE, response);
    }


    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("direct:simple").to("nmr:simple");
                from("nmr:simple").to("mock:simple").setBody(constant(RESPONSE_MESSAGE));

                from("direct:hops").to("nmr:hop1");
                from("nmr:hop1").to("nmr:hop2");
                from("nmr:hop2").to("mock:hops").setBody(constant(RESPONSE_MESSAGE));
            }
        };
    }
}
