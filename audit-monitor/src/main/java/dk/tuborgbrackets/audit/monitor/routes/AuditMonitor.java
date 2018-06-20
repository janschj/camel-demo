package dk.tuborgbrackets.audit.monitor.routes;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.camel.component.ActiveMQConfiguration;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class AuditMonitor extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("auditLog:queue:Audit")
		.routeId("AuditMonitor")
		.log("after event mapping: id=${exchangeId}")
        .to("direct:auditMonitor");	
		
		from("direct:auditMonitor")
		   .to("log:com.mycompany.order?showAll=true&multiline=true");
		/*
        .log("after breadcrumbId: id=${in.header.breadcrumbId}")
        .log("after JMSCorrelationID: id=${in.header.JMSCorrelationID}")
		  .to("log:com.mycompany.order?showAll=true&multiline=true")
		  		.to("log:auditLog ${exchangeId}")
		.to("direct:auditMonitor");
		
		from("direct:auditMonitor")
        .to("log:com.mycompany.order?showAll=true&multiline=true")
        .to("log:auditLog ${exchangeId}")
.routeId("auditLog2");
*/
	}
	/*
	 * breadcrumbId
exchangeId
messageId
correlationId
routeId
camelContextId
transactionKey
	 */

}
