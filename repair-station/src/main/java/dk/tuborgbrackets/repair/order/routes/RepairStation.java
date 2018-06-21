package dk.tuborgbrackets.repair.order.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

public class RepairStation extends RouteBuilder {

	@Override
	public void configure() throws Exception {
	  
	    from("direct:get")
          .to("log:com.mycompany.order?showAll=true&multiline=true") 
        .enrich("browse:repairStation:queue:WebNewOrder")
        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));
       ;

	    from("direct:delete")
	       .log("Order arriving from widgetOrderItem ${body}")
	       .to("repairStation:queue:WebNewOrder?selector=orderId='${header.orderId}'")
	      .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));
        
        rest("/api/orders")
        .get().to("direct:get")
        .post().to("direct:post")
        .delete("{orderId}")
        .to("direct:delete");

	}
}
