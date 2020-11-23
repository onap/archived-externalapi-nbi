.. SPDX-License-Identifier: CC-BY-4.0
.. Copyright 2019 ORANGE
.. _release_notes:

Release Notes
=============

Version: 7.0.2
--------------

:Release Date: 2020-11-16

Guilin Release tag - 7.0.2

**Fix**

- `EXTAPI-510 <https://jira.onap.org/browse/EXTAPI-510>`_ - 1 NBI pod has no limit 
- `EXTAPI-509 <https://jira.onap.org/browse/EXTAPI-509>`_ - NBI has root pods 
- `EXTAPI-197 <https://jira.onap.org/browse/EXTAPI-197>`_ - MSB registration 

Details of NBI features for Guilin are described in the readTheDoc documentation.

https://docs.onap.org/projects/onap-externalapi-nbi/en/latest/index.html#master-index

**Known Issues**

No new issues

**Security Notes**

Updates may to External API pods security are per TSC must have list

Quick Links:

- `External API project page <https://wiki.onap.org/display/DW/External+API+Framework+Project>`_

**Upgrade Notes**

No major API changes. The API Major version is still 4.


Version: 6.0.3
--------------

:Release Date: 2020-06-04

Frankfurt Release tag - 6.0.3

**Fix**

- `OJSI-136 <https://jira.onap.org/browse/OJSI-136>`_ - In default deployment EXTAPI (nbi) exposes HTTP port 30274 outside of cluster.
- `EXTAPI-347 <https://jira.onap.org/browse/EXTAPI-347>`_ - Move to mariadb galera instead of mariadb 
- `EXTAPI-222 <https://jira.onap.org/browse/EXTAPI-222>`_ - Add support for HTTPS with AAF artefacts 
- `EXTAPI-294 <https://jira.onap.org/browse/EXTAPI-294>`_ - Add support for Service Orders using new "Object" type 
- `EXTAPI-304 <https://jira.onap.org/browse/EXTAPI-304>`_ - Update SO request to use GR_API instead of VNF_API 
- `EXTAPI-342 <https://jira.onap.org/browse/EXTAPI-342>`_ - NBI to SO: new URL and new Header params 
- `EXTAPI-343 <https://jira.onap.org/browse/EXTAPI-343>`_ - NBI to SO: cloudowner value to be taken from application.properties 
- `EXTAPI-258 <https://jira.onap.org/browse/EXTAPI-258>`_ - Identify whether the Service is of A-la-carte or macro type 
- `EXTAPI-370 <https://jira.onap.org/browse/EXTAPI-370>`_ - Java 11 & oparent 3.0.0-SNAPSHOT 
- `EXTAPI-378 <https://jira.onap.org/browse/EXTAPI-378>`_ - Update swagger based on spectral 
- `EXTAPI-384 <https://jira.onap.org/browse/EXTAPI-384>`_ - SECCOM Java 11 migration from v8 [REQ-219] in NBI 
- `EXTAPI-397 <https://jira.onap.org/browse/EXTAPI-397>`_ - Update Service Order Swagger to align to ONAP Style Guidelines
- `EXTAPI-399 <https://jira.onap.org/browse/EXTAPI-399>`_ - Fix Docker File Image to point to correct base and use 3.0.0 in pom
- `EXTAPI-400 <https://jira.onap.org/browse/EXTAPI-400>`_ - Migrate and Fix sonarcloud code coverage issue
- `EXTAPI-401 <https://jira.onap.org/browse/EXTAPI-401>`_ - remove sonar.jacoco.reportMissing.force.zero 
- `EXTAPI-415 <https://jira.onap.org/browse/EXTAPI-415>`_ - Configure NBI with http xor https support, using basic spring capabilities 
- `EXTAPI-417 <https://jira.onap.org/browse/EXTAPI-417>`_ - Support http local docker and https OOM via Env Variable 
- `EXTAPI-423 <https://jira.onap.org/browse/EXTAPI-423>`_ - Check for CST template is case sensitive 
- `EXTAPI-424 <https://jira.onap.org/browse/EXTAPI-424>`_ - Public HTTP port open 
- `EXTAPI-427 <https://jira.onap.org/browse/EXTAPI-427>`_ - DMaap https port enable 

Detail of NBI features are described in the readTheDoc documentation.

https://onap.readthedocs.io/en/latest/submodules/externalapi/nbi.git/docs/index.html


**Known Issues**

No new issues

**Security Notes**

In the Frankfurt release, External API has been updated to expose a https interface via OOM installations, in response to OJSI-136. 
NBI has also upgraded to Java 11, using the base registry.gitlab.com/onap-integration/docker/onap-java image. 

Quick Links:

- `External API project page <https://wiki.onap.org/display/DW/External+API+Framework+Project>`_

**Upgrade Notes**

No major API changes. The API Major version is still 4.

/nbi/api/v4

Frankfurt API version is 4.1.0 i.e. Minor API changes only,  as most changes are related to security updates. No new APIs.
Swagger changes are mainly in the use of additional markdown for API understanding and conformance to ONAP API Swagger Style Guidelines 
https://wiki.onap.org/pages/viewpage.action?pageId=71834147

Version: 5.0.1
--------------

:Release Date: 2019-09-06

El Alto Release tag - 5.0.1

**Fix**

- `EXTAPI-248 <https://jira.onap.org/browse/EXTAPI-248>`_ - ExtAPI should not be polling SDC-DISTR-NOTIF-TOPIC-AUTO without authenticating
- `EXTAPI-249 <https://jira.onap.org/browse/EXTAPI-249>`_ - Change to oom dockers causing permissions failing when tosca parsing
- `EXTAPI-287 <https://jira.onap.org/browse/EXTAPI-287>`_ - NBI to SDC connectivity health checks fail 
- `EXTAPI-305 <https://jira.onap.org/browse/EXTAPI-305>`_ - No Need for "ReadWriteMany" access on storage when deploying on Kubernetes 

Detail of features described in the readTheDoc documentation.

https://onap.readthedocs.io/en/latest/submodules/externalapi/nbi.git/docs/index.html


**Known Issues**

No new issues

**Security Notes**

- Same as Dublin 4.0.0

Quick Links:

- `External API project page <https://wiki.onap.org/display/DW/External+API+Framework+Project>`_

**Upgrade Notes**

No major API change. The API Major version is still 4.

/nbi/api/v4

El Alto API version is 4.0.1 i.e. Patch only

Version: 4.0.0
--------------

:Release Date: 2019-05-30

New major version v4 for the API, see Upgrade Notes

Dedicated Postman collection can be found in the integration project see `test/postman <https://git.onap.org/integration/tree/test/postman?h=dublin>`_

All tests suites have been re written in Karate, see `src/test/resources/karatetest <https://git.onap.org/externalapi/nbi/tree/src/test/resources/karatetest?h=dublin>`_ for inspiration.

**New Features**

Main new features are supports of

- `BroadBand Service Use Case ( BBS ) <https://wiki.onap.org/pages/viewpage.action?pageId=45297636>`_
- `Cross Domain and Cross Layer VPN ( CCVPN ) <https://wiki.onap.org/display/DW/CCVPN%28Cross+Domain+and+Cross+Layer+VPN%29+USE+CASE>`_

Main functional changes for BBS:

- `EXTAPI-98 <https://jira.onap.org/browse/EXTAPI-98>`_ - Service inventory notification`
- `EXTAPI-161 <https://jira.onap.org/browse/EXTAPI-161>`_ - New service specificationInputSchemas operation`

Main functional change for CCVPN

- `EXTAPI-182 <https://jira.onap.org/browse/EXTAPI-182>`_ - Create SO -> ExtAPI interface`

Many other changes and improvement are listed in JIRA:

- `All Dublin issues <https://jira.onap.org/issues/?filter=11786>`_

**Known Issues**

- `EXTAPI-197 <https://jira.onap.org/browse/EXTAPI-197>`_ - Bad hostname while registering on MSB
- `EXTAPI-222 <https://jira.onap.org/browse/EXTAPI-222>`_ - Add support for HTTPS
- `EXTAPI-249 <https://jira.onap.org/browse/EXTAPI-249>`_ - Change to oom dockers causing permissions failing when tosca parsing

EXTAPI-249 has limited impact on BBS use case:
GET /serviceSpecification{id}
returns empty serviceSpecCharacteristic.

**Security Notes**

*Fixed Security Issues*

NBI has been improved to reduce signs of vulnerabilities,
especially by migrating from Springboot 1.x to Springboot 2 and using ONAP Parent pom.xml

*Known Security Issues*

- `OJSI-136 <https://jira.onap.org/browse/OJSI-136>`_ - In default deployment EXTAPI (nbi) exposes HTTP port 30274 outside of cluster.
   NBI exposes non TLS API endpoint on port 30274, meaning full plain text exchange with NBI API.
   TLS configuration, with ONAP Root CA signed certificate will be proposed in El Alto.

   As a workaround it is quite easy to add HTTPS support to NBI by configuring SSL and activating strict https.
   Presuming you have a valid JKS keystore, with private key and a signed certificate:

   ::

       src/main/resources/application.properties

   ::

       # tls/ssl
       server.ssl.key-store-type=JKS
       server.ssl.key-store=classpath:certificate/yourkeystore.jks
       server.ssl.key-store-password=password
       server.ssl.key-alias=youralias

       # disable http and activate https
       security.require-ssl=true

*Known Vulnerabilities in Used Modules*

- `Dublin Vulnerability Report <https://wiki.onap.org/pages/viewpage.action?pageId=51282484>`_

Quick Links:

- `External API project page <https://wiki.onap.org/display/DW/External+API+Framework+Project>`_

**Upgrade Notes**

API is a new MAJOR v4 version due to the deletion of the 'hasStarted' attribute from getServiceById response
GET /service/{id}

So don't forget to use this new path:

/nbi/api/v4

before:

/nbi/api/v3

**Deprecation Notes**

API v3 is deprecated

**Other**

===========

Version: 3.0.2
--------------

:Release Date: 2019-01-31

Part of Casablanca Maintenance Release tag - 3.0.1 January 31st, 2019

**Fix**

- `EXTAPI-164 <https://jira.onap.org/browse/EXTAPI-164>`_ - Start up failed without msb
- `EXTAPI-172 <https://jira.onap.org/browse/EXTAPI-172>`_ - Multiple service orders in a single request

Detail of features described in the readTheDoc documentation.

**Known Issues**

No new issues

**Security Notes**

- `Casablanca Vulnerability Report <https://wiki.onap.org/pages/viewpage.action?pageId=45310585>`_

Quick Links:

- `External API project page <https://wiki.onap.org/display/DW/External+API+Framework+Project>`_


https://wiki.onap.org/pages/viewpage.action?pageId=51282484

===========

Version: 3.0.1
--------------

:Release Date: 2018-11-30

**New Features**

Main features are:

- `EXTAPI-96 <https://jira.onap.org/browse/EXTAPI-96>`_ - Add notification for serviceOrder API
- `EXTAPI-97 <https://jira.onap.org/browse/EXTAPI-97>`_ - Upgrade ServiceOrder API to manage modification UC
- `EXTAPI-100 <https://jira.onap.org/browse/EXTAPI-100>`_ - Improve ServiceInventory API
- `EXTAPI-101 <https://jira.onap.org/browse/EXTAPI-101>`_ - Integrate ExtAPI/NBI to MSB
- `EXTAPI-102 <https://jira.onap.org/browse/EXTAPI-102>`_ - Integrate ExtAPI/NBI to an E2E ONAP UC
- `EXTAPI-116 <https://jira.onap.org/browse/EXTAPI-116>`_ - Help NBI user to get information when Service order fails
- `EXTAPI-125 <https://jira.onap.org/browse/EXTAPI-125>`_ - Add support for progress percentage on ServiceOrder tracking

Detail of features described in the readTheDoc documentation.

**Known Issues**

No new issue (see Beijing ones)

**Security Notes**

- `Vulnerability Report <https://wiki.onap.org/pages/viewpage.action?pageId=45301150>`_

Quick Links:

- `External API project page <https://wiki.onap.org/display/DW/External+API+Framework+Project>`_

**Upgrade Notes**

No upgrade available from Beijing

**Deprecation Notes**

NA

**Other**

===========

Version: 1.0.0
--------------

:Release Date: 2018-06-07

**New Features**

Main features are:

- `EXTAPI-39 <https://jira.onap.org/browse/EXTAPI-39>`_ - Retrieve SDC information (catalog information) for service level artifacts based on TMF633 open APIs - operation GET
- `EXTAPI-41 <https://jira.onap.org/browse/EXTAPI-41>`_ - Retrieve AAI information (inventory information) for service instance level artifacts based on TMF638 open APIs - operation GET
- `EXTAPI-42 <https://jira.onap.org/browse/EXTAPI-42>`_ - Create and retrieve SO service request for service level based on TMF641 open APIS - Operations POST & GET

Detail of features described in the readTheDoc documentation.

**Bug Fixes**

Not applicable - This is an initial release

**Known Issues**

For service catalog:

- Find criteria are limited

For service inventory:

- Customer information must be passed to get complete service representation.
- Find criteria are limited.

For service order:

- ServiceOrder will manage only 'add' and 'delete' operation (no change).
- Only service level request is performed.
- No request for VNF/VF and no call to SDNC.
- `EXTAPI-70 <https://jira.onap.org/browse/EXTAPI-70>`_ : links between customer/service instance and cloud/tenant not done (trigger VID issue).
- Only active service state is considered to add a service.

Detail of limitations described in the readTheDoc documentation.

**Security Notes**

External API code has been formally scanned during build time using NexusIQ and all Critical vulnerabilities have been addressed, items that remain open have been assessed for risk and determined to be false positive. The External API open Critical security vulnerabilities and their risk assessment have been documented as part of the `project <https://wiki.onap.org/pages/viewpage.action?pageId=28382906>`_.
Authentication management and Data Access rights have not been implemented.

Quick Links:

- `External API project page <https://wiki.onap.org/display/DW/External+API+Framework+Project>`_
- `Passing Badge information for External API <https://bestpractices.coreinfrastructure.org/en/projects/1771>`_
- `Project Vulnerability Review Table for External API <https://wiki.onap.org/pages/viewpage.action?pageId=28382906>`_

**Upgrade Notes**

Not applicable - This is an initial release

**Deprecation Notes**

Not applicable - This is an initial release

**Other**

===========

End of Release Notes
