package com.masternaut.demo;

import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;

public class FileJmsTxRoute extends RouteBuilder {

    @EndpointInject(ref = "queueInput")
    private Endpoint queueInputUri;

    @EndpointInject(ref = "queueOutput")
    private Endpoint queueOutputUri;

    @EndpointInject(ref = "fileOutput")
    private Endpoint fileUri;

    @Override
    public void configure() throws Exception {

        onException(CamelException.class)
           .handled(true)
              .log("ERROR Exception !!!");

        from("activemq:queue:queueInput")
           .transacted("required")
           .log("Received : ${in.body} ...")
           .to("file://output")
           .process(new Processor() {

               @Override
               public void process(Exchange exchange) throws Exception {
                   if (((String) exchange.getIn().getBody()).equals("ERROR")) {
                       throw new RuntimeException();
                   }
               }
           })
          .to("activemq:queue:queueOutput");


    }

}
