package com.masternaut.demo;

import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;

public class NmrConsumerRoute extends RouteBuilder{

 @EndpointInject(uri="nmr:demo1")
 private Endpoint demo1ConsumerUri ;

 @Override
 public void configure() throws Exception {
   from("nmr:demo1").log("${in.body}");
 }

}
