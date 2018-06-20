package dk.tuborgbrackets.audit.log.routes;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.camel.component.ActiveMQConfiguration;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.builder.RouteBuilder;

public class AuditLog extends RouteBuilder {

	@Override
	public void configure() throws Exception {
	  /*
		ActiveMQComponent answer = ActiveMQComponent.activeMQComponent();
		ActiveMQConnectionFactory jmsConnectionFactory = new ActiveMQConnectionFactory(
				"vm://localhost?broker.persistent=false");
		PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory(jmsConnectionFactory);
		((ActiveMQConfiguration) answer.getConfiguration()).setConnectionFactory(pooledConnectionFactory);
		// camelContext.addComponent("activemq", answer);
		this.getContext().addComponent("auditLog", answer);
*/
		from("direct:auditLog")
		.routeId("auditLog")
		.to("auditLog:queue:Audit");
	}

}
