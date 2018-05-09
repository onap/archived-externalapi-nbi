.. This work is licensed under
.. a Creative Commons Attribution 4.0 International License.
.. http://creativecommons.org/licenses/by/4.0
.. Copyright 2018 ORANGE


Installation
============



Environment
-----------

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


Steps
-----

**Testing**
When the application is running, you can access the API at
:samp:`http://yourhostname:8080/nbi/api/v1/` and fill the URL with the name
of the resources you asking for (/serviceSpecification, /service,
/serviceOrder or /status)
You can run a test by using `VisualStudio RestClient
plugin <https://github.com/Huachao/vscode-restclient>`_
See the *restclient* package at root level to find *.vscode/settings.json*
configuration file and */json/* package with samples requests that can be run.
You can also trigger these endpoints with any RESTful client or
automation framework.

