package dk.tuborgbrackets.widget.orderitem.routes;

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


public class WidgetInventoryCheckTest extends CamelTestSupport {
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

    ActiveMQComponent queueComponent = ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=false"); 
	
	
	@Override
	protected RouteBuilder[] createRouteBuilders() throws Exception {
		return new RouteBuilder[] { new WidgetInventoryCheck(), new RouteBuilder() {
			public void configure() {
		        this.getContext().addComponent("widgetOrderItem", queueComponent);
 			  			  
				from("direct:start")
				.setHeader("JMSCorrelationID",constant("foo-123"))
				.to("widgetOrderItem:queue:NewOrderItem?useMessageIDAsCorrelationID=true").to("mock:result");
			}
		} };
	}



	@Test
	public void testConfigure1() throws Exception {
		Item item = new ObjectFactory().createItem();
		item.setNote("note");
		item.setPrice(BigDecimal.ONE);
		item.setQuantity(BigInteger.ONE);
		item.setTitle("widget");
		//

		mockEndpoint.setExpectedCount(1);
		mockEndpoint.expectedBodiesReceived(XmlUtil.toString(item));
		template.sendBody(XmlUtil.toString(item));
		mockEndpoint.assertIsSatisfied();
		Thread.sleep(3000);
	}
}
