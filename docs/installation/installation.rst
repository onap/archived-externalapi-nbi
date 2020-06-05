.. SPDX-License-Identifier: CC-BY-4.0
.. Copyright 2018 ORANGE


Installation
============

This document describes local installation for development purpose
It also describes some keypoints for understanding NBI in OOM context

Build
-----

Requirements

* Java 11
* Maven
* free port 8080, used by tests

Build::

    mvn clean package

Alternative 1 run SpringBoot application
----------------------------------------

Requirements

* Java 11
* Maven
* MongoDB
* MariaDB

Review and edit *src/main/resources/application.properties*

Defaults

    Mongo, host=localhost, port=27017, database=ServiceOrderDB

    Mariadb, url=jdbc:mariadb://localhost:3306/nbi, username=root,
    password=secret

Run::

    mvn spring-boot:run

Alternative 2 run docker
------------------------

Requirements

* Docker
* Docker-compose
* Free ports 8080

Edit *docker-compose.yml* to select previous generated local build, replace::

    image: ${NEXUS_DOCKER_REPO}/onap/externalapi/nbi:latest

by::

    build: .

Run::

    docker-compose up -d mongo mariadb

    docker-compose up --build -d nbi

Logs::

    docker-compose logs -f nbi


Local tests
-----------

Status is available at http://localhost:8080/nbi/api/v4/status

Running a standalone test::

    curl "http://localhost:8080/nbi/api/v4/status"

You should get::

    {
        "name": "nbi",
        "status": "ok",
        "version": "v4"
    }

OOM Context
-----------

**Healthcheck**

Running a standalone test::

    curl "https://nbi.api.simpledemo.onap.org:30274/nbi/api/v4/status"

    {
        "name": "nbi",
        "status": "ok",
        "version": "v4"
    }

Running an integration test with SO, SDC, DMAAP, AAI::

    curl "https://nbi.api.simpledemo.onap.org:30274/nbi/api/v4/status?fullStatus=true"

    {
        "name": "nbi",
        "status": "ok",
        "version": "v4",
        "components": [
            {
                "name": "so connectivity",
                "status": "ok"
            },
            {
                "name": "sdc connectivity",
                "status": "ok"
            },
            {
                "name": "dmaap connectivity",
                "status": "ok"
            },
            {
                "name": "aai connectivity",
                "status": "ok"
            }
        ]
    }

**Understanding OOM deployment**

NBI uses AAF init container to generate valid server certificate, signed by
ONAP Root CA. This server certificate is used for TLS over HTTP.

Passing specific JAVA_OPTS to NBI SpringBoot java app will enable HTTPS.

Here are some OOM related files which could help to understand how HTTPS is
set up.

Search for JAVA_OPTS in
https://github.com/onap/oom/blob/master/kubernetes/nbi/templates/deployment.yaml

AAF values
https://github.com/onap/oom/blob/master/kubernetes/nbi/values.yaml

AAF init container
https://github.com/onap/oom/blob/master/kubernetes/nbi/templates/configmap-aaf-add-config.yaml
