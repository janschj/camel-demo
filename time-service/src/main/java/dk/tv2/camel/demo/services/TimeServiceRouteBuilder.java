package dk.tv2.camel.demo.services;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;

public class TimeServiceRouteBuilder extends RouteBuilder {

  @Override
  public void configure() throws Exception {
    from("direct:getTime").setExchangePattern(ExchangePattern.InOut).bean(new TimeService(), "getTime");
    // .to("direct:time");
    
    from("timer://myTimer?period=2000")
       .log("Timer")
       .to("direct:getTime")
    	.log("${body}");
    
    // add HTTP interface
    from("jetty:http://0.0.0.0:{{port}}/time").setExchangePattern(ExchangePattern.InOut)
    .to("direct:getTime")
    .log("Done");
  }

}
