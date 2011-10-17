package com.masternaut.demo;

import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;

public class NmrConsumerRoute extends RouteBuilder{

 @EndpointInject(ref="nmrConsumer")
 private Endpoint demo1ConsumerUri ;

 @Override
 public void configure() throws Exception {
   from(demo1ConsumerUri).log("${in.body}");
 }

}
