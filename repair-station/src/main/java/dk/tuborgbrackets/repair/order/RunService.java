package dk.tuborgbrackets.repair.order;

import org.apache.camel.spring.Main;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RunService {

  private Main main;

  public static void main(String[] args) throws Exception {
    RunService app = new RunService();
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


