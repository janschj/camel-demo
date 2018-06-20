package dk.tv2.camel.demo.routes;

import org.apache.camel.builder.RouteBuilder;

public class HandRouteBuilder extends RouteBuilder {

  @Override
  public void configure() throws Exception {
	  from("direct:start")
	  .log("start");  
  }

}
