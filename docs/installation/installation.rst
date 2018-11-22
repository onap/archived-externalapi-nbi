.. SPDX-License-Identifier: CC-BY-4.0
.. Copyright 2018 ORANGE


Installation
============

This document describes local build and installation for development purpose

Build
-----

Requirements

* Java 8
* Maven
* port 8080 should be free, used by tests

Build
::

    mvn clean package

Run
---

**Maven**

Requirements

* Java 8
* Maven
* MongoDB
* MariaDB

Review and edit *src/main/resources/application.properties*

Defaults

    Mongo, host=localhost, port=27017, database=ServiceOrderDB

    Mariadb, url=jdbc:mariadb://localhost:3306/nbi, username=root,
    password=secret

Run
::

    mvn spring-boot:run

**Docker**

Requirements

* Docker
* Docker-compose

Edit *docker-compose.yml* to select previous generated local build, replace::

    image: ${NEXUS_DOCKER_REPO}/onap/externalapi/nbi:latest

by::

    build: .

Run::

    docker-compose up -d mongo mariadb

    docker-compose up --build -d nbi

Logs::

    docker-compose logs -f nbi


Test
----

**Healthcheck**

http://localhost:8080/nbi/api/v3/status

You should get::

    {
        "name": "nbi",
        "status": "ok",
        "version": "v3"
    }

**Play with RESTclient**

You can also test NBI with `VisualStudio RestClient plugin <https://github.com/Huachao/vscode-restclient>`_

See the *restclient* package at root level to find *.vscode/settings.json*
configuration file and */json/* package with samples requests that can be run.

**Play with Postman**

A collection is available here *docs/offeredapis/postman*
