package dk.tuborgbrackets.weborder.routes;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.camel.component.ActiveMQConfiguration;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Expression;
import org.apache.camel.builder.ExpressionBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.jsonpath.JsonPathExpression;
import org.apache.camel.model.dataformat.JaxbDataFormat;
import dk.tuborgbrackets.weborder.app.MyBean;
import dk.tuborgbrackets.weborder.dto.PostOrderRequest;

public class FileOrderInAdapter extends RouteBuilder {

  @Override
  public void configure() throws Exception {

    ActiveMQComponent answer = ActiveMQComponent.activeMQComponent();
    ActiveMQConnectionFactory jmsConnectionFactory =
        new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
    PooledConnectionFactory pooledConnectionFactory =
        new PooledConnectionFactory(jmsConnectionFactory);
    ((ActiveMQConfiguration) answer.getConfiguration())
        .setConnectionFactory(pooledConnectionFactory);
    // camelContext.addComponent("activemq", answer);
    this.getContext().addComponent("WebNewOrder", answer);
    this.getContext().addComponent("NewOrder", answer);

    Expression yy = ExpressionBuilder.languageExpression("jsonpath","$.shiporder");
    
    from("direct:validate")
        .log("Messsage sent to WEBnnnn_NEW_ORDER :: ${body}")
        .split(yy)
           .to("direct:split")
        .end();

    from("direct:split")
    .log("Messsage split :: ${body}")
    .bean(MyBean.class, "method")
 ;

    from("file:target/file/custom?noop=true")
    .to("direct:validate");
    // translator
    // add order id
    // to newOrderQue

  }
}
