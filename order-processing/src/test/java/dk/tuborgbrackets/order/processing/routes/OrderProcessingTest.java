package dk.tuborgbrackets.order.processing.routes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Properties;
import javax.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.camel.component.ActiveMQConfiguration;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.seda.SedaComponent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.BeforeClass;
import org.junit.Test;
import dk.tuborgbrackets.model.order.Item;
import dk.tuborgbrackets.model.order.ObjectFactory;
import dk.tuborgbrackets.model.order.Shiporder;
import dk.tuborgbrackets.model.order.Shipto;


public class OrderProcessingTest extends CamelTestSupport {
	@EndpointInject(uri = "mock:result")
	protected MockEndpoint mockEndpoint;

	@Produce(uri = "direct:start")
	protected ProducerTemplate template;

	   @Override
	    public String isMockEndpointsAndSkip() {
	        return "direct:auditMonitor|inventoryStatus:queue:InventoryStatus|newOrderItem:queue:NewOrderItem";
	    }
	@Override
	protected Properties useOverridePropertiesWithPropertiesComponent() {
	    Properties properties = new Properties();
	    properties.put("port", "8080");
	    return properties;
	}

    ActiveMQComponent queueComponent = ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=false"); 
	
	
	@Override
	protected RouteBuilder[] createRouteBuilders() throws Exception {
		return new RouteBuilder[] { new OrderProcessing(), new RouteBuilder() {
			public void configure() {
		        this.getContext().addComponent("auditLog", queueComponent);
                this.getContext().addComponent("newOrder", queueComponent);
                this.getContext().addComponent("newOrderItem", queueComponent);
                this.getContext().addComponent("inventoryStatus", queueComponent);
			  			  
				from("direct:start")
				.setHeader("JMSCorrelationID",constant("foo-123"))
				.to("newOrder:queue:NewOrder?useMessageIDAsCorrelationID=true").to("mock:result");
			}
		} };
	}



	@Test
	public void testOrderProcessing() throws Exception {
		Shiporder req = new ObjectFactory().createShiporder();
		Shipto shipto = new ObjectFactory().createShipto();
		
		Item item1 = new ObjectFactory().createItem();
		item1.setNote("note");
		item1.setPrice(BigDecimal.ONE);
		item1.setQuantity(BigInteger.ONE);
		item1.setTitle("widget");
        Item item2 = new ObjectFactory().createItem();
        item2.setNote("note");
        item2.setPrice(BigDecimal.ONE);
        item2.setQuantity(BigInteger.ONE);
        item2.setTitle("gadget");
		//
		shipto.setAddress("adr");
		shipto.setCity("city");
		shipto.setCountry("dk");
		shipto.setName("nm");
		//
		req.setOrderperson("Hans");
		req.setOrderid("d");
		req.setShipto(shipto);
		req.getItem().add(item1);
        req.getItem().add(item2);
		mockEndpoint.setExpectedCount(1);
		mockEndpoint.expectedBodiesReceived(XmlUtil.toString(req));
        getMockEndpoint("mock:inventoryStatus:queue:InventoryStatus").expectedMessageCount(1);
        getMockEndpoint("mock:newOrderItem:queue:NewOrderItem").expectedMessageCount(2);
		template.sendBody(XmlUtil.toString(req));
        assertMockEndpointsSatisfied();
		Thread.sleep(3000);
	}
}
