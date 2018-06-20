package dk.tuborgbrackets.weborder.routes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Properties;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.BeforeClass;
import org.junit.Test;
import dk.tuborgbrackets.addorderid.routes.AddOrderId;
import dk.tuborgbrackets.weborder.dto.Item;
import dk.tuborgbrackets.weborder.dto.ObjectFactory;
import dk.tuborgbrackets.weborder.dto.Shiporder;
import dk.tuborgbrackets.weborder.dto.Shipto;


public class WebOrderInAdapterTest extends CamelTestSupport {
	@EndpointInject(uri = "mock:result")
	protected MockEndpoint mockEndpoint;

	@Produce(uri = "direct:start")
	protected ProducerTemplate template;

    ActiveMQComponent queueComponent = ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=false"); 

	@Override
	protected Properties useOverridePropertiesWithPropertiesComponent() {
	    Properties properties = new Properties();
	    properties.put("port", "8080");
	    properties.put("AuditLogQueue", "vm://localhost?broker.persistent=false");
	    return properties;
	}

    @Override
    public String isMockEndpointsAndSkip() {
        return "direct:auditLog|repairStation:queue:WebNewOrder|webNewOrder:queue:WebNewOrder";
    }
    

	@BeforeClass
	public static void setUpClass() throws Exception {
		BrokerService brokerSvc = new BrokerService();
		brokerSvc.setBrokerName("TestBroker");
		brokerSvc.addConnector("tcp://localhost:61616");
		brokerSvc.setPersistent(false);
		brokerSvc.start();
	}
	
	@Override
	protected RouteBuilder[] createRouteBuilders() throws Exception {
      String auditLogQueue = context.resolvePropertyPlaceholders("{{AuditLogQueue}}");
		return new RouteBuilder[] { new AddOrderId(), new WebOrderInAdapter(), new RouteBuilder() {
			public void configure() {
              this.getContext()
              .addComponent("auditLog", queueComponent);
              this.getContext()
              .addComponent("webNewOrder", queueComponent);
              this.getContext()
              .addComponent("newOrder", queueComponent);
              this.getContext()
              .addComponent("repairStation", queueComponent);
			  
				from("direct:start").to("direct:WebOrderValidate").to("mock:result");
			}
		} };
	}


	
	@Test
	public void testWebOrder() throws Exception {
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
//		mockEndpoint.expectedBodiesReceived(req);
		template.sendBody(req);
		mockEndpoint.assertIsSatisfied();
		Thread.sleep(3000);
	}
	

    @Test
    public void testWebOrderWithoutId() throws Exception {
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
        req.setShipto(shipto);
        req.getItem().add(item);
        
        getMockEndpoint("mock:repairStation:queue:WebNewOrder").expectedMessageCount(1);
        getMockEndpoint("mock:webNewOrder:queue:WebNewOrder").expectedMessageCount(0);
        getMockEndpoint("mock:direct:auditLog").expectedMessageCount(1);

//      mockEndpoint.expectedBodiesReceived(req);
        template.sendBody(req);
        assertMockEndpointsSatisfied();
        Thread.sleep(3000);
    }
}
