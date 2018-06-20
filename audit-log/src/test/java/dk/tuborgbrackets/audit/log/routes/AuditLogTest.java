package dk.tuborgbrackets.audit.log.routes;

import java.util.Properties;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class AuditLogTest extends CamelTestSupport {
	@EndpointInject(uri = "mock:result")
	protected MockEndpoint mockEndpoint;

	@Produce(uri = "direct:start")
	protected ProducerTemplate template;

    ActiveMQComponent queueComponent = ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=false"); 

	@BeforeClass
	  public static void setJettyPort() {
	  }	
	@Before
	public void xxx() {
	}
	
	@Override
	    public String isMockEndpointsAndSkip() {
	        return "auditLog:queue:Audit";
	    }
	
    @Override
    protected Properties useOverridePropertiesWithPropertiesComponent() {
        Properties properties = new Properties();
        properties.put("ipHome", "8080");
        properties.put("port", "8080");
        properties.put("AuditLogQueue", "vm://localhost?broker.persistent=false");
        return properties;
    }
	
	@Override
	protected RouteBuilder[] createRouteBuilders() throws Exception {
		return new RouteBuilder[] { new AuditLog(), new RouteBuilder() {
			public void configure() throws Exception {
			  String auditLogQueue = context.resolvePropertyPlaceholders("{{AuditLogQueue}}");
			  this.getContext()
              .addComponent("auditLog", queueComponent);
 				from("direct:start")
			       .log("Uploading file {{AuditLogQueue}}")
				.to("direct:auditLog").to("mock:result");
			}
		} };
	}

	@Test
	public void testAuditRoute() throws Exception {
		String order = "<shiporder><xxx>bbb</xxx><vz>hh</vz></shiporder>";
		mockEndpoint.setExpectedCount(1);
        getMockEndpoint("mock:auditLog:queue:Audit").expectedMessageCount(1);
		template.sendBody(order);
		assertMockEndpointsSatisfied();
		mockEndpoint.assertIsSatisfied();
	}
}
