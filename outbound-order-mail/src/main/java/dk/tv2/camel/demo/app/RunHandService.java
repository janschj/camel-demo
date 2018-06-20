package dk.tv2.camel.demo.app;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

import dk.tv2.camel.demo.routes.HandRouteBuilder;

public class RunHandService {

  private Main main;

  public static void main(String[] args) throws Exception {
    RunHandService app = new RunHandService();
    final String port = (args.length == 1 ? args[0] : "8765");
    app.boot(port);
  }

  public void boot(String port) throws Exception {
    System.setProperty("port", port);

    // create a Main instance
    main = new Main();
    // enable hangup support so you can press ctrl + c to terminate the JVM
    main.enableHangupSupport();
    // add routes
    main.addRouteBuilder(createRouteBuilder());

    // run until you terminate the JVM
    System.out.println(String.format("Starting Camel, using port %s. Use ctrl + c to terminate the JVM.", port));
    main.run();
  }
  static RouteBuilder createRouteBuilder() {
      return new HandRouteBuilder();
  }
}
