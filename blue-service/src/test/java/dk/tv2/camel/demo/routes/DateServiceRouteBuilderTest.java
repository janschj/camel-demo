package dk.tv2.camel.demo.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DateServiceRouteBuilderTest extends CamelSpringTestSupport {
 
    private boolean debugBeforeMethodCalled;
    private boolean debugAfterMethodCalled;
 


@Override
protected AbstractApplicationContext createApplicationContext() {
    return new ClassPathXmlApplicationContext("camel-context.xml");
    }    
  @Produce(uri = "direct:getLogin")
  private ProducerTemplate start;

  @BeforeClass
  public static void setJettyPort() {
    System.setProperty("port", "2345");
  }

 

@Test
  public void testRoute() throws InterruptedException {
	

    Exchange exchange = createExchangeWithBody("not relevant");

    start.sendBodyAndHeader(exchange, "name", "jimmitest9876@mailinator.com");

 //   Object date = exchange.getOut().getBody();
 //   assertEquals(LocalDate.class, date.getClass());
  }

  
  @Override
  public boolean isUseDebugger() {
      // must enable debugger
      return true;
  }

  @Override
  protected void debugBefore(Exchange exchange, org.apache.camel.Processor processor, ProcessorDefinition<?> definition, String id, String label) {
      log.info("Before " + definition + " with body " + exchange.getIn().getBody());
      debugBeforeMethodCalled = true;
  }

  @Override
  protected void debugAfter(Exchange exchange, org.apache.camel.Processor processor, ProcessorDefinition<?> definition, String id, String label, long timeTaken) {
      log.info("After " + definition + " with body " + exchange.getIn().getBody());
      debugAfterMethodCalled = true;
  }

}
