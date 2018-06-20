package dk.tuborgbrackets.weborder.routes;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.camel.component.ActiveMQConfiguration;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.CamelContext;

public class QueueUtil {

  public static ActiveMQComponent getQueueComponent(final String queName) {
    ActiveMQComponent comp = ActiveMQComponent.activeMQComponent();
    ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
            queName);
    PooledConnectionFactory pooledFactory = new PooledConnectionFactory(connectionFactory);
    ((ActiveMQConfiguration) comp.getConfiguration()).setConnectionFactory(pooledFactory);
    return comp;
  } 
  
}
