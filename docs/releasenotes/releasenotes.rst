.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. http://creativecommons.org/licenses/by/4.0
.. Copyright 2018 ORANGE


Release Notes
=============


Version: 1.0.0
--------------


:Release Date:
May 25th, 2018 - Beijing release.

**New Features**
Main features are:
- EXTAPI39 - Retrieve SDC information (catalog information) for service level artefacts based on TMF633 open APIs - operation GET
- EXTAPI41 - Retrieve AAI information (inventory information) for service instance level artefacts based on TMF638 open APIs - operation GET
- EXTAPI42 - Create and retrieve SO service request for service level based on TMF 641 open APIS - Operations POST & GET
Detail of features described in the readTheDoc documentation.

**Bug Fixes**
Not applicable - This is an initial release

**Known Issues**
For service catalog:
- Find criterias are limited

For service inventory:
- Customer information must be passed to get complete service representation.
- Find criterias are limited

For service order:
- ServiceOrder will manage only ‘add’ and ‘delete’ operation (no change).
- Only service level request is performed - no request for VNF/VF and no call to SDNC
- (EXTAPI70) Links between customer/service instance and cloud/tenant not done (trigger VID issue)
- Only active service state is considered to add a service.

Detail of limitations described in the readTheDoc documentation.

**Security Issues**
Security has not be addressed in this release: Authentification management and Data Access rights have not been implemented.

**Upgrade Notes**
Not applicable - This is an initial release

**Deprecation Notes**
Not applicable - This is an initial release

**Other**

===========

End of Release Notes
