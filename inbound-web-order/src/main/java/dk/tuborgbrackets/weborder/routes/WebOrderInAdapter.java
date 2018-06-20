package dk.tuborgbrackets.weborder.routes;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.camel.component.ActiveMQConfiguration;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JaxbDataFormat;
import dk.tuborgbrackets.weborder.dto.PostOrderRequest;

public class WebOrderInAdapter extends RouteBuilder {
  final static String generateIdBean = "bean:dk.tuborgbrackets.addorderid.services.OrderUtil?method=generateId";

  private static class RouteSpecificException extends Exception { }

  @Override
  public void configure() throws Exception {
    
    onException(org.apache.camel.processor.validation.SchemaValidationException.class)
    .handled(true)
    .log("GenericException Handled")
    .to("repairStation:queue:WebNewOrder");
 
    JaxbDataFormat jaxb = new JaxbDataFormat();
    jaxb.setContextPath("dk.tuborgbrackets.weborder.dto");

    from("direct:WebOrderValidate")
      .routeId("WebOrderValidate")
      .marshal(jaxb)
      .wireTap("direct:auditLog")
      .to("validator:file:src/main/resources/order.xsd")
      .to("webNewOrder:queue:WebNewOrder");
 
    from("webNewOrder:queue:WebNewOrder")
    .routeId("WebOrderAddOrderId")

    .log("before enrich :: ${body}")
    .setHeader("orderId", simple(generateIdBean))
    .setProperty("orderId", simple("${header.orderId}"))
    .to("xslt:weborder-to-order.xsl")
    .log("after tran NNNNNNNNNNNNNNN :: ${body} ${header.orderId}")
        .to("newOrder:queue:NewOrder")
        .log("Order added orderId : ${header.orderId} to NEW_ORDER");



    /**
     * Configure the REST API (POST, GET, etc.)
     */
    rest().path("/api")
    .consumes("application/json")
    .post().type(PostOrderRequest.class)
        .to("direct:WebOrderValidate");
    // translator
    // add order id
    // to newOrderQue

  }
}
