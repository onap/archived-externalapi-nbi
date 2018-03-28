.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. http://creativecommons.org/licenses/by/4.0
.. Copyright 2018 ORANGE


============
NBI - Developer Guide
============
***************
Introduction
***************

NBI is a Java 8 web application built over Spring Framework. Using Spring Boot 1.5.10 dependencies, it runs as a standalone application with an embedded Tomcat server.

***************
Dependencies
***************
This project use various framework which are managed with Maven dependency management tool (see *pom.xml* file at root level) :

- Swagger annotations
- `Spring Framework <https://github.com/spring-projects/spring-boot>`_
- `JOLT <https://github.com/bazaarvoice/jolt>`_ to perform JsonToJson transformation
- `FasterXML/Jackson <https://github.com/FasterXML/jackson>`_ to perform JSON parsing
- `Wiremock <https://github.com/tomakehurst/wiremock>`_ to perform testing over HTTP mocked response


***************
Configuration
***************
A configuration file, *src/main/resources/application.properties* list all the component interface that can be configured depending on the environment were the app is deployed. 
By default, the application runs with an embedded MySQL H2 Database and a MongoDB local instance.
This file also list configurations of all the REST interface maid from NBI to other ONAP component such as SDC, AA&I and SO.

***************
Source tree
***************
This application provides ServiceOrder, ServiceCatalag and ServiceInventory as main functional resources and HealthCheck. Each resource is implemented independently in a package corresponding to its name.

*commons , configuration, and exceptions* are shared technical classes that provided for all the application.


***************************************
Running and testing the application
***************************************

**Locally**

Ensure that you have a MongoDB instance running and configured in *application.properties* file.
Run *Application.java* class in your favorite IDE

Or through a terminal, ensure that your maven installation is works and run *mvn spring-boot:run* command to start the appication.


**Docker**

in progress ...


**Testing**

You can run a test by using `VisualStudio RestClient plugin <https://github.com/Huachao/vscode-restclient>`_
See the *restclient* package at root level to find *.vscode/settings.json* configuration file and */json/* package with samples requests that can be run.