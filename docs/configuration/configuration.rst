.. This work is licensed under
.. a Creative Commons Attribution 4.0 International License.
.. http://creativecommons.org/licenses/by/4.0
.. Copyright 2018 ORANGE


Configuration
=============

A configuration file, *src/main/resources/application-localhost.properties*
list all the component interface that can be configured depending on the
environment were the application is deployed.
By default, the application runs with an embedded both MongoDB and MariaDB
local instance.
This file also list configurations of all the REST interface maid from NBI
to other ONAP component such as SDC, AA&I and SO.


**************
Default values
**************

::

    # SERVER
    server.servlet.context-path          = /nbi/api/${nbi.version}
    server.port                          = 8080
    server.public.ip                     = localhost

    # LOGGING
    logging.level.                       = WARN
    logging.level.org.springframework    = OFF
    logging.level.org.onap               = INFO
    logging.level.root                   = WARN
    spring.main.banner-mode              = off

    # ONAP
    onap.lcpCloudRegionId                = RegionOne
    onap.tenantId                        = 6e97a2bd51d74f6db5671d8dc1517d82
    onap.cloudOwner                      = CloudOwner

    # NBI
    nbi.url                              = http://localhost:${server.port}${server.servlet.context-path}
    nbi.callForVNF                       = false
    nbi.public.url                       = http://${server.public.ip}:${server.port}${server.servlet.context-path}

    # SCHEDULER
    scheduler.pollingDurationInMins      = 360
    serviceOrder.schedule                = 5000
    serviceOrder.initial                 = 1
    executionTask.schedule               = 2000
    executionTask.initial                = 1
    dmaapCheck.schedule                  = 10000
    dmaapCheck.initial                   = 1

    # SDC
    sdc.host                             = http://10.0.3.1:8080
    sdc.header.ecompInstanceId           = demo
    sdc.header.authorization             = Basic YWFpOktwOGJKNFNYc3pNMFdYbGhhazNlSGxjc2UyZ0F3ODR2YW9HR21KdlV5MlU=
    # AAI

    aai.host                             = https://10.0.1.1:8443
    aai.header.authorization             = Basic QUFJOkFBSQ==
    aai.api.id                           = NBI
    aai.header.transaction.id            = 808b54e3-e563-4144-a1b9-e24e2ed93d4f

    # SO
    so.host                              = http://10.0.5.1:8080
    so.header.authorization              = Basic SW5mcmFQb3J0YWxDbGllbnQ6cGFzc3dvcmQxJA==
    so.api.id                            = SO
    so.owning.entity.id                  = 6b5b6b70-4e9a-4f6f-8b7b-cbd7cf990c6e
    so.owning.entity.name                = OE-generic
    so.project.name                      = Project-generic

    # DMAAP
    dmaap.host                           = http://10.0.6.1:3904
    dmaap.aai.topic                      = AAI-EVENT
    dmaap.sdc.topic                      = SDC-DISTR-NOTIF-TOPIC-AUTO
    dmaap.consumergroup                  = NBICG1
    dmaap.consumerid                     = NBIC1
    dmaap.timeout                        = 2000

    # MSB
    msb.enabled                          = true
    msb.discovery.host                   = msb_discovery
    msb.discovery.port                   = 10081
    msb.discovery.retry                  = 1
    msb.discovery.retry_interval         = 5000
    msb.service.host                     =
    msb.service.name                     = nbi
    msb.service.custom_path              =
    msb.service.protocol                 = REST
    msb.service.visual_range             = 1
    msb.service.enable_ssl               = false

    # MONGO
    spring.data.mongodb.host             = localhost
    spring.data.mongodb.port             = 27017
    spring.data.mongodb.database         = ServiceOrderDB

    # MYSQL
    spring.datasource.url                = jdbc:mariadb://localhost:3306/nbi
    spring.datasource.username           = root
    spring.datasource.password           = secret
    spring.datasource.testWhileIdle      = true
    spring.datasource.validationQuery    = SELECT 1
    spring.datasource.driver-class-name  = org.mariadb.jdbc.Driver
    spring.jpa.show-sql                  = false
    spring.jpa.hibernate.ddl-auto        = update
    spring.jpa.hibernate.naming-strategy = org.hibernate.cfg.ImprovedNamingStrategy

***************
Changing values
***************

To adapt application parameters to your context, you need to set up some
environment attributes. For example :

::

      SPRING_DATASOURCE_PASSWORD: your own value here
      SPRING_DATASOURCE_USERNAME: your own value here
      SDC_HOST: http://${SDC_IP}:8080
      AAI_HOST: https://${AAI_IP}:8443
      SO_HOST: http://${SO_IP}:8080
      DMAAP_HOST: http://${DMAAP_IP}:3904

