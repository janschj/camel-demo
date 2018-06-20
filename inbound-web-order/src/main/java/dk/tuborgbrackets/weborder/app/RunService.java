package dk.tuborgbrackets.weborder.app;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.camel.component.ActiveMQConfiguration;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.main.Main;

import dk.tuborgbrackets.weborder.routes.WebOrderInAdapter;

public class RunService {

  private Main main;

  private void addAuditLogQueue(CamelContext context) {
    ActiveMQComponent answer = ActiveMQComponent.activeMQComponent();
    ActiveMQConnectionFactory jmsConnectionFactory = new ActiveMQConnectionFactory(
            "vm://localhost?broker.persistent=false");
    PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory(jmsConnectionFactory);
    ((ActiveMQConfiguration) answer.getConfiguration()).setConnectionFactory(pooledConnectionFactory);
    context.addComponent("auditLog", answer);   
  }
  
  public static void main(String[] args) throws Exception {
    RunService app = new RunService();
    final String port = (args.length == 1 ? args[0] : "8080");
    app.boot(port);
  }

  public void boot(String port) throws Exception {
    System.setProperty("port", port);

    // create a Main instance
    main = new Main();
    // enable hangup support so you can press ctrl + c to terminate the JVM
    CamelContext camelContext = main.getOrCreateCamelContext();
    //
    PropertiesComponent pc = new PropertiesComponent();
    pc.setLocation("classpath:application.properties");
    camelContext.addComponent("properties", pc);    //
    addAuditLogQueue(camelContext);
    //
    main.enableHangupSupport();
    // add routes
    main.addRouteBuilder(createRouteBuilder());

    // run until you terminate the JVM
    System.out.println(String.format("Starting Camel, using port %s. Use ctrl + c to terminate the JVM.", port));
    main.run();
  }
  
  static RouteBuilder createRouteBuilder() {
      return new WebOrderInAdapter();
  }
}
