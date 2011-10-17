package com.masternaut.demo;

import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import java.util.Date;

public class NmrProducerRoute extends RouteBuilder {

 @EndpointInject(ref = "nmrProducer")
 private Endpoint nmrProducerUri;

 @Override
 public void configure() throws Exception {

from(
       "timer://foo?fixedRate=true&period=1000")
       .process(new Processor() {

           @Override
           public void process(Exchange exchange) throws Exception {
               exchange.getIn().setBody(new Date().toString());
           }
       })
       .to(nmrProducerUri);

 }
}
