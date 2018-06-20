package dk.tuborgbrackets.order.processing.routes;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.camel.component.ActiveMQConfiguration;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;

public class OrderProcessing extends RouteBuilder {

	@Override
	public void configure() throws Exception {

        from("newOrder:queue:NewOrder")
        .routeId("OrderProcessing")
        .log("newOrder:queue:NewOrder received  ${body}")
		.split(xpath("/shiporder/item"))
		  .log("Order item ${body}")
		  .to("newOrderItem:queue:NewOrderItem")
		.end()
        .log("Order after split  ${body}")
        .routeId("OrderProcessingStatus")
        .to("inventoryStatus:queue:InventoryStatus");
	}

}
