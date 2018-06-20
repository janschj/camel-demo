package dk.tuborgbrackets.weborder.routes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Properties;
import javax.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.camel.component.ActiveMQConfiguration;
import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import dk.tuborgbrackets.addorderid.routes.AddOrderId;
import dk.tuborgbrackets.audit.log.routes.AuditLog;
import dk.tuborgbrackets.audit.monitor.routes.AuditMonitor;
import dk.tuborgbrackets.order.item.routes.OrderItemProcessing;
import dk.tuborgbrackets.order.processing.routes.OrderProcessing;
import dk.tuborgbrackets.weborder.dto.Item;
import dk.tuborgbrackets.weborder.dto.ObjectFactory;
import dk.tuborgbrackets.weborder.dto.Shiporder;
import dk.tuborgbrackets.weborder.dto.Shipto;
import dk.tuborgbrackets.weborder.routes.WebOrderInAdapter;


public class WebOrderTest extends CamelTestSupport {
	@EndpointInject(uri = "mock:result")
	protected MockEndpoint mockEndpoint;

	@Produce(uri = "direct:start")
	protected ProducerTemplate template;
	
	@Override
	protected Properties useOverridePropertiesWithPropertiesComponent() {
	    Properties properties = new Properties();
        properties.put("ipHome", "u:/ipconfig");
        properties.put("port", "8080");
        properties.put("AuditLogQueue", "vm://localhost?broker.persistent=false");
	    return properties;
	}

    ActiveMQComponent queueComponent = ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=false"); 
	
    @Override
    public String isMockEndpointsAndSkip() {
        return "direct:auditMonitor|inventoryStatus:queue:InventoryStatus";
    }
    
	//@BeforeClass
	public static void setUpClass() throws Exception {
		BrokerService brokerSvc = new BrokerService();
		brokerSvc.setBrokerName("TestBroker");
		brokerSvc.addConnector("tcp://localhost:61616");
		brokerSvc.setPersistent(false);
		brokerSvc.start();
	}
	
	@Override
	protected RouteBuilder[] createRouteBuilders() throws Exception {
		return new RouteBuilder[] { 
		    new AddOrderId()
		    , new WebOrderInAdapter()
            , new OrderProcessing()
            , new OrderItemProcessing()
            , new AuditLog()
            , new AuditMonitor()
		    , new RouteBuilder() {
			public void configure() throws Exception {
              String auditLogQueue = context.resolvePropertyPlaceholders("{{AuditLogQueue}}");
			  this.getContext()
			  .addComponent("auditLog", queueComponent);
              this.getContext()
              .addComponent("webNewOrder", queueComponent);
              this.getContext()
              .addComponent("newOrder", queueComponent);
              this.getContext()
              .addComponent("newOrderItem", queueComponent);
              this.getContext()
              .addComponent("inventoryStatus", queueComponent);
              this.getContext()
              .addComponent("widgetOrderItem", queueComponent);
              this.getContext()
              .addComponent("gadgetOrderItem", queueComponent);
			  
			  from("direct:start")
				.to("direct:WebOrderValidate")
				.to("mock:result");
			}
		} };
	}



	@Test
	public void testWebOrder() throws Exception {
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
		String expectedBody = "<matched/>";
		mockEndpoint.setExpectedCount(1);
	    getMockEndpoint("mock:direct:auditMonitor").expectedMessageCount(1);
        getMockEndpoint("mock:inventoryStatus:queue:InventoryStatus").expectedMessageCount(1);
	       
//		mockEndpoint.expectedBodiesReceived(req);
		template.sendBody(req);
        Thread.sleep(3000);
        assertMockEndpointsSatisfied();
//        mock2.assertIsSatisfied();
		Thread.sleep(3000);
	}
}
