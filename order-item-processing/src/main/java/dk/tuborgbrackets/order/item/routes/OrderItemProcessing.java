package dk.tuborgbrackets.order.item.routes;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.camel.component.ActiveMQConfiguration;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;

public class OrderItemProcessing extends RouteBuilder {

	@Override
	public void configure() throws Exception {

        from("newOrderItem:queue:NewOrderItem")
        .routeId("OrderItemProcessing")
        .log("Order arriving from newOrderItem ${body}")
       .choice()
        .when(xpath("//title[starts-with(.,'widget')]/text()"))
            .to("widgetOrderItem:queue:NewOrderItem")
        .otherwise()
          .to("gadgetOrderItem:queue:NewOrderItem")
        .end(); 
	}
}
