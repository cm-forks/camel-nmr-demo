package com.masternaut.demo;

import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class FileJmsTxRoute extends RouteBuilder{

@EndpointInject(ref = "queueInput")
 private Endpoint queueInputUri;

 @EndpointInject(ref = "queueOutput")
 private Endpoint queueOutputUri;

 @EndpointInject(ref = "fileOutput")
 private Endpoint fileUri;

 @Override
 public void configure() throws Exception {

     from("activemq:queue:queueInput").log("Received : ${in.body} ...").process(new Processor() {

     @Override
     public void process(Exchange exchange) throws Exception {
       if(((String)exchange.getIn().getBody()).equals("ERROR")) {
         throw new RuntimeException();
       }
     }
   }).to("file://output").to("activemq:queue:queueOutput");


 }

}
