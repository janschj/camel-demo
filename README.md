

Apache Camel demo project
=====================

Example to build microservices based on [Java 8][0], [Apache Maven][1] and [Apache Camel][2]. 
Example also include  run in [Docker][5] containers.

Prerequisites
_____________

In order to run this example on your machine you need Maven and docker-compose installed.

**Linux**

    apt-get install maven
    apt-get insatll docker-compose

**Mac**

    brew install maven
    brew insatll docker-compose

Or install Docker Toolbox found here:

    https://docs.docker.com/toolbox/toolbox_install_mac/

**Windows**

Instructions for how to install Maven are described here: [https://maven.apache.org/install.html](https://maven.apache.org/install.html)
For docker instructions, look here: [https://docs.docker.com/toolbox/toolbox_install_windows/](https://docs.docker.com/toolbox/toolbox_install_windows/)

How do I run this example?
--------------------------

*Run with Maven and Java*

You can run this example applications like any other Maven project. First, build the whole project by executing the following command in the top directory:

    mvn clean package

After that you can start the 4 services with pure Java:

    java -jar time-service/target/time-service-*.jar 5678
    java -jar date-service/target/date-service-*.jar 5679
    java -jar blue-service/target/blue-service-*.jar 5681
    

When everything runs fine and there are no *ERROR*s in your console output, open [http://localhost:5678/time][10] or [http://localhost:5679/date][11] to see the output of the services. 
Time service includes a timer - so it will run in background

*Run with Docker*

You have to build the project with Maven first. Then you can use *docker-compose* to start the services. Run the following comand in the top directory:

    docker-compose up

After that you can open [http://localhost:8081/time][14] or [http://localhost:8080/date][15] to see the output of the services. If those ports are not free at your machine, you have to edit the docker-compose.yml and change the left side of the "ports" attribute to some available ports.

[0]: http://www.oracle.com/technetwork/java/javase/overview/java8-2100321.html
[1]: https://maven.apache.org/
[2]: https://camel.apache.org/
[3]: 
[4]: 
[5]: https://www.docker.com/
[10]: http://localhost:5678/time
[11]: http://localhost:5679/date
[14]: http://localhost:8081/time
[15]: http://localhost:8080/date
