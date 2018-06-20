package dk.tuborgbrackets.order.item.routes;

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


public class OrderItemProcessingTest extends CamelTestSupport {
	@EndpointInject(uri = "mock:result")
	protected MockEndpoint mockEndpoint;

	@Produce(uri = "direct:start")
	protected ProducerTemplate template;
	
	@Override
	protected Properties useOverridePropertiesWithPropertiesComponent() {
	    Properties properties = new Properties();
	    properties.put("port", "8080");
	    return properties;
	}
	
	@Override
	    public String isMockEndpointsAndSkip() {
	        return "widgetOrderItem:queue:NewOrderItem|gadgetOrderItem:queue:NewOrderItem";
	}
	
    ActiveMQComponent queueComponent = ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=false"); 
	
	
	@Override
	protected RouteBuilder[] createRouteBuilders() throws Exception {
		return new RouteBuilder[] { new OrderItemProcessing(), new RouteBuilder() {
			public void configure() {
                this.getContext().addComponent("newOrderItem", queueComponent);
                this.getContext().addComponent("widgetOrderItem", queueComponent);
                this.getContext().addComponent("gadgetOrderItem", queueComponent);
 			  			  
				from("direct:start")
				.setHeader("JMSCorrelationID",constant("foo-123"))
				.to("newOrderItem:queue:NewOrderItem?useMessageIDAsCorrelationID=true").to("mock:result");
			}
		} };
	}



	@Test
	public void testWidget() throws Exception {
        Item item1 = new ObjectFactory().createItem();
        item1.setNote("note");
        item1.setPrice(BigDecimal.ONE);
        item1.setQuantity(BigInteger.ONE);
        item1.setTitle("widget");
 
		mockEndpoint.setExpectedCount(1);
		mockEndpoint.expectedBodiesReceived(XmlUtil.toString(item1));
        getMockEndpoint("mock:widgetOrderItem:queue:NewOrderItem").expectedMessageCount(1);
        getMockEndpoint("mock:gadgetOrderItem:queue:NewOrderItem").expectedMessageCount(0);
		template.sendBody(XmlUtil.toString(item1));
	    assertMockEndpointsSatisfied();
		Thread.sleep(3000);
	}
	
	   @Test
	    public void testGadget() throws Exception {
	        Item item2 = new ObjectFactory().createItem();
	        item2.setNote("note");
	        item2.setPrice(BigDecimal.ONE);
	        item2.setQuantity(BigInteger.ONE);
	        item2.setTitle("gadget");       //
	        mockEndpoint.setExpectedCount(1);
	        mockEndpoint.expectedBodiesReceived(XmlUtil.toString(item2));
	        getMockEndpoint("mock:widgetOrderItem:queue:NewOrderItem").expectedMessageCount(0);
	        getMockEndpoint("mock:gadgetOrderItem:queue:NewOrderItem").expectedMessageCount(1);
	        template.sendBody(XmlUtil.toString(item2));
	        assertMockEndpointsSatisfied();
	        Thread.sleep(3000);
	    }
}
