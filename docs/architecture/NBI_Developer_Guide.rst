.. This work is licensed under
.. a Creative Commons Attribution 4.0 International License.
.. http://creativecommons.org/licenses/by/4.0
.. Copyright 2018 ORANGE


=====================
NBI - Developer Guide
=====================
************
Introduction
************

NBI is a Java 11 web application built over Spring Framework.
Using Spring Boot 2.2.2 dependencies, it runs as a standalone
application with an embedded Tomcat server.

************
Dependencies
************
This project use various framework which are managed with Maven
dependency management tool (see *pom.xml* file at root level) :

- Swagger annotations
- `Spring Framework <https://github.com/spring-projects/spring-boot>`_
- `JOLT <https://github.com/bazaarvoice/jolt>`_ to perform JsonToJson transformation
- `FasterXML/Jackson <https://github.com/FasterXML/jackson>`_ to perform JSON parsing
- `Wiremock <https://github.com/tomakehurst/wiremock>`_ to perform testing over HTTP mocked response


*************
Configuration
*************
A configuration file, *src/main/resources/application-localhost.properties*
list all the component interface that can be configured depending on
the environment were the application is deployed.
By default, the application runs with an embedded both MongoDB and MariaDB
local instance.
This file also list configurations of all the REST interface maid from NBI
to other ONAP component such as SDC, AA&I and SO.

***********
Source tree
***********
This application provides ServiceOrder, ServiceCatalag and ServiceInventory
as main functional resources and HealthCheck. Each resource is implemented
independently in a package corresponding to its name.

*commons , configuration, and exceptions* are shared technical classes that
provided for all the application.


***********************************
Running and testing the application
***********************************

**Locally**

Ensure that you have a MongoDB and MariaDB instance running and properly
configured in *application.properties* file.
Run *Application.java* class in your favorite IDE

Or through a terminal, ensure that your maven installation is works and
run *mvn spring-boot:run* command to start the application.


**Docker**

Requirements: `Docker engine <https://docs.docker.com/engine/>`_ and
`docker-compose <https://docs.docker.com/compose/>`_.

To start the application:

    1. Generate the application .jar file: `$ mvn clean package`
    2. Configure the **.env** file
    3. Start the *MariaDB* and *MongoDB* services:
       `$ docker-compose up -d mongo mariadb`
    4. Build and start the *NBI* service: `$ docker-compose up --build -d nbi`

You can view the log output of the application with the following command:

`$ docker-compose logs -f nbi`

**Testing**
When the application is running, you can access the API at
:samp:`http://yourhostname:8080/nbi/api/v4` and fill the URL with the name
of the resources you asking for (/serviceSpecification, /service,
/serviceOrder or /status)
You can run a test by using `VisualStudio RestClient plugin <https://github.com/Huachao/vscode-restclient>`_
See the *restclient* package at root level to find *.vscode/settings.json*
configuration file and */json/* package with samples requests that can be run.
You can also trigger these endpoints with any RESTful client or automation
framework.
