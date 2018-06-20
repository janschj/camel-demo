package dk.tuborgbrackets.addorderid.routes;

import org.apache.camel.builder.RouteBuilder;

import dk.tuborgbrackets.addorderid.processor.AddOrderIdAttribute;

public class AddOrderId extends RouteBuilder {

	final static String generateIdBean = "bean:dk.tuborgbrackets.addorderid.services.OrderUtil?method=generateId";

	@Override
	public void configure() throws Exception {
		from("direct:addOrderId")
		.setHeader("orderId", simple(generateIdBean))
		.process(new AddOrderIdAttribute());;
	}
}
