package dk.tuborgbrackets.widget.orderitem.routes;

import org.apache.camel.builder.RouteBuilder;

public class WidgetInventoryCheck extends RouteBuilder {

	@Override
	public void configure() throws Exception {

        from("widgetOrderItem:queue:NewOrderItem")
        .routeId("WidgetInventoryCheck")
        .log("Order arriving from widgetOrderItem ${body}")
        .setHeader("itemStatus", simple("OK"))
        .setProperty("itemStatus", simple("OK"))
        .to("xslt:inv-status-to-item.xsl")
        .log("after tran NNNNNNNNNNNNNNN :: ${body} ${header.itemStatus}")
        .log("Order leving check ${body}");

	}
}
