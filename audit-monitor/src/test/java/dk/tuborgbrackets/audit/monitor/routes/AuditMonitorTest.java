package dk.tuborgbrackets.audit.monitor.routes;

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

import dk.tuborgbrackets.addorderid.routes.AddOrderId;
import dk.tuborgbrackets.audit.monitor.routes.AuditMonitor;
import dk.tuborgbrackets.weborder.dto.Item;
import dk.tuborgbrackets.weborder.dto.ObjectFactory;
import dk.tuborgbrackets.weborder.dto.Shiporder;
import dk.tuborgbrackets.weborder.dto.Shipto;


public class AuditMonitorTest extends CamelTestSupport {
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
		return new RouteBuilder[] { new AuditMonitor(), new RouteBuilder() {
			public void configure() {
		        this.getContext().addComponent("auditLog", queueComponent);
			  			  
				from("direct:start")
				.setHeader("JMSCorrelationID",constant("foo-123"))
				.to("auditLog:queue:Audit?useMessageIDAsCorrelationID=true").to("mock:result");
			}
		} };
	}



	@Test
	public void testConfigure1() throws Exception {
		Shiporder req = new ObjectFactory().createShiporder();
		Shipto shipto = new ObjectFactory().createShipto();
		Item item = new ObjectFactory().createItem();
		item.setNote("note");
		item.setPrice(BigDecimal.ONE);
		item.setQuantity(BigInteger.ONE);
		item.setTitle("title");
		//
		shipto.setAddress("adr");
		shipto.setCity("city");
		shipto.setCountry("dk");
		shipto.setName("nm");
		//
		req.setOrderperson("Hans");
		req.setOrderid("d");
		req.setShipto(shipto);
		req.getItem().add(item);
		String expectedBody = "<matched/>";
		mockEndpoint.setExpectedCount(1);
		mockEndpoint.expectedBodiesReceived(req);
		template.sendBody(req);
		mockEndpoint.assertIsSatisfied();
		Thread.sleep(3000);
	}
}
