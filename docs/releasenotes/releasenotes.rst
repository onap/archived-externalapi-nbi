.. This work is licensed under a
.. Creative Commons Attribution 4.0 International License.
.. http://creativecommons.org/licenses/by/4.0
.. Copyright 2018 ORANGE

Release Notes
=============

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

- ServiceOrder will manage only ‘add’ and ‘delete’ operation (no change).
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
