package dk.tv2.camel.demo.routes;

import java.time.LocalDate;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.BeforeClass;
import org.junit.Test;

import dk.tv2.camel.demo.routes.HandRouteBuilder;

public class HandRouteBuilderTest extends CamelTestSupport {
  @Produce(uri = "direct:start")
  private ProducerTemplate start;

  @EndpointInject(uri = "mock:result")
  protected MockEndpoint resultEndpoint;

 
  @Test
  public void canSplitAndAggregate() throws InterruptedException {
	String txt = "<navne><navn>gadget</navn><navn>widget</navn></navne>";
    Exchange exchange = createExchangeWithBody(txt);
    start.send(exchange);

    String result =  exchange.getIn().getBody(String.class);
    assertEquals("<navn>foo</navn><navn>widget</navn>", result);
  }

  @Override
  protected RouteBuilder[] createRouteBuilders() throws Exception {
    return new RouteBuilder[] { new HandRouteBuilder() };
  }

}
