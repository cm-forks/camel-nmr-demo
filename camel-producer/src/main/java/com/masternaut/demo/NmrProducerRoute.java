package com.masternaut.demo;

import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.language.SimpleExpression;

import java.util.Date;

public class NmrProducerRoute extends RouteBuilder {

 @EndpointInject(ref = "nmrProducer")
 private Endpoint nmrProducerUri;

 @Override
 public void configure() throws Exception {

   from(
       "timer://foo?fixedRate=true&period=1000")
       .setBody(new SimpleExpression(new Date().toString()))
       .to(nmrProducerUri);

 }
}
