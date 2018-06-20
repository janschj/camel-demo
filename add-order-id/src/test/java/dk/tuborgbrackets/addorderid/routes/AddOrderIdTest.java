package dk.tuborgbrackets.addorderid.routes;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Assert;
import org.junit.Test;


public class AddOrderIdTest extends CamelTestSupport {
	@EndpointInject(uri = "mock:result")
	protected MockEndpoint mockEndpoint;

	@Produce(uri = "direct:start")
	protected ProducerTemplate template;
	
	
	@Override
	protected RouteBuilder[] createRouteBuilders() throws Exception {
		return new RouteBuilder[] { new AddOrderId(), new RouteBuilder() {
			public void configure() {
				from("direct:start").to("direct:addOrderId").to("mock:result");
			}
		} };
	}

	@Test
	public void testAddOrderIdToXML() throws Exception {
		String order = "<shiporder><xxx>bbb</xxx><vz>hh</vz></shiporder>";
		mockEndpoint.setExpectedCount(1);
		template.sendBody(order);
		mockEndpoint.assertIsSatisfied();
        Exchange exchange = mockEndpoint.getExchanges().get(0);
        String str = exchange.getIn().getBody(String.class);
        Assert.assertTrue(str.contains("orderId="));    
        Assert.assertNotNull(exchange.getIn().getHeader("orderId"));
	}
}
