package dk.tv2.camel.demo.app;

import org.apache.camel.spring.Main;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RunLoginService {

  private Main main;

  public static void main(String[] args) throws Exception {
    RunLoginService app = new RunLoginService();
    final String port = (args.length == 1 ? args[0] : "8765");
    app.boot(port);
  }

  public void boot(String port) throws Exception {
    System.setProperty("port", port);
    
    // create a Main instance
    main = new Main();
    // enable hangup support so you can press ctrl + c to terminate the JVM
    main.enableHangupSupport();
    //
    AbstractXmlApplicationContext springCtx = new  ClassPathXmlApplicationContext("camel-context.xml");
    
    //
    main.setApplicationContext(springCtx);
//    main.setApplicationContextUri("camel-context.xml");
    //
    // run until you terminate the JVM
    System.out.println(String.format("Starting Camel, using port %s. Use ctrl + c to terminate the JVM.", port));
    main.run();
  }

}


