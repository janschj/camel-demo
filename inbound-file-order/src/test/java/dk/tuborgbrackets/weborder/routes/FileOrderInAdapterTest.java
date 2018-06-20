package dk.tuborgbrackets.weborder.routes;

import java.util.Properties;
import org.apache.activemq.broker.BrokerService;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.BeforeClass;
import org.junit.Test;
import dk.tuborgbrackets.addorderid.routes.AddOrderId;


public class FileOrderInAdapterTest extends CamelTestSupport {
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
		return new RouteBuilder[] { new AddOrderId(), new FileOrderInAdapter(), new RouteBuilder() {
			public void configure() {
				from("direct:start").to("direct:validate").to("mock:result");
			}
		} };
	}

	 private final static String  jString = ""
	     + "{"
	     + "\"shiporder\": {"
	     + "   \"orderperson\": \"hans\","
	     + "   \"item\": ["
	     + "          {"
	     + "          \"title\": \"reference\","
	     + "          \"note\": \"Nigel Rees\","
	     + "          \"quantity\": 10 ,"
	     + "          \"price\": 8.95 "
	     + "       },"
	     + "       {"
	     + "          \"title\": \"fiction\","
	     + "          \"note\": \"J. R. R. Tolkien\","
	     + "          \"quantity\": 11 ,"
	     + "          \"price\": 22.99"
	     + "        }"
	     + "   ]"
	     + " }}";	     
	     

	@Test
	public void testConfigure1() throws Exception {
	
		mockEndpoint.setExpectedCount(0);
	    template.sendBodyAndHeader("file:target/file/custom", jString, Exchange.FILE_NAME, "hello.txt");
		mockEndpoint.assertIsSatisfied();
		Thread.sleep(1000);
	}
}
