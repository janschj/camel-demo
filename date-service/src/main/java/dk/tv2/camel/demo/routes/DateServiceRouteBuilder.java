package dk.tv2.camel.demo.routes;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;

import dk.tv2.camel.demo.services.DateService;

public class DateServiceRouteBuilder extends RouteBuilder {

  @Override
  public void configure() throws Exception {
    from("direct:getDate").setExchangePattern(ExchangePattern.InOut).bean(new DateService(), "getDate");
    // .to("direct:date");
    // add HTTP interface
    from("restlet:http://0.0.0.0:{{port}}/date").setExchangePattern(ExchangePattern.InOut).to("direct:getDate");
  }

}
