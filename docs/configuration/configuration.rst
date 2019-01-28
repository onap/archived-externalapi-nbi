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
    server.servlet.context-path=/nbi/api/v3
    server.port = 8080

    # LOGGING
    logging.level.=INFO

    # ONAP
    onap.lcpCloudRegionId=RegionOne
    onap.tenantId=6e97a2bd51d74f6db5671d8dc1517d82
    onap.cloudOwner=CloudOwner

    # NBI
    nbi.url=http://localhost:8080/nbi/api/v3
    nbi.callForVNF=false

    # SDC
    sdc.host=http://10.0.3.1:8080
    sdc.header.ecompInstanceId=demo
    sdc.header.authorization=Basic YWFpOktwOGJKNFNYc3pNMFdYbGhhazNlSGxjc2UyZ0F3ODR2YW9HR21KdlV5MlU=

    # AAI
    aai.host=https://10.0.1.1:8443
    aai.header.authorization=Basic QUFJOkFBSQ==
    aai.api.id=AAI

    # SO
    so.host=http://10.0.5.1:8080
    so.header.authorization=Basic SW5mcmFQb3J0YWxDbGllbnQ6cGFzc3dvcmQxJA==
    so.api.id=SO

    # MONGO
    spring.data.mongodb.host=localhost
    spring.data.mongodb.port=27017
    spring.data.mongodb.database=ServiceOrderDB

    # MYSQL
    spring.datasource.url=jdbc:mariadb://localhost:3306/nbi
    spring.datasource.username=root
    spring.datasource.password=secret
    spring.datasource.testWhileIdle=true
    spring.datasource.validationQuery=SELECT 1
    spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
    spring.jpa.show-sql=false
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.hibernate.naming-strategy=org.hibernate.cfg.ImprovedNamingStrategy

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
