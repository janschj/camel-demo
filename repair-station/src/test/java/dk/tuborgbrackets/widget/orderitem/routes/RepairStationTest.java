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
import dk.tuborgbrackets.repair.order.routes.RepairStation;


public class RepairStationTest extends CamelTestSupport {
	@EndpointInject(uri = "mock:result")
	protected MockEndpoint mockEndpoint;

	@Produce(uri = "direct:start")
	protected ProducerTemplate template;

    @Override
    public String isMockEndpointsAndSkip() {
        return "direct:auditLog|webNewOrder:queue:WebNewOrder";
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
		return new RouteBuilder[] { new RepairStation(), new RouteBuilder() {
			public void configure() {
		        this.getContext().addComponent("widgetOrderItem", queueComponent);
	              this.getContext()
	              .addComponent("repairStation", queueComponent);
 			  			  
				from("direct:start")
				.to("repairStation:queue:WebNewOrder")
				.to("direct:get")
				.to("mock:result");
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
      req.setShipto(shipto);
      req.getItem().add(item);
		//

		mockEndpoint.setExpectedCount(1);
		mockEndpoint.expectedBodiesReceived(XmlUtil.toString(req));
 //       getMockEndpoint("mock:repairStation:queue:WebNewOrder").expectedMessageCount(1);
		template.sendBody(XmlUtil.toString(req));
		mockEndpoint.assertIsSatisfied();
		Thread.sleep(1000);
	}
}
