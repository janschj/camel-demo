package dk.tv2.camel.demo.routes.sol;

import org.apache.camel.builder.RouteBuilder;

public class SolutionHandRouteBuilder extends RouteBuilder {

  @Override
  public void configure() throws Exception {
	  from("direct:start")
	  .log("start")
	  .split(xpath("//navn"), new MyStrategy())
	  	.choice()
      		.when(xpath("//navn = 'gadget'"))
      		.to("direct:step2")
      		.otherwise()
      		.to("direct:step3")
      		.end()
      .end();
//	  .log("efter split ${body}");
	  
	  from("direct:step2")
	  .log("step2 - gadget")
	  .setBody(constant("<navn>foo</navn>"));
	  
	  from("direct:step3")
	  .log("step3");
	  
  }

}
