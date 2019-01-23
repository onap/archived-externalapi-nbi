.. SPDX-License-Identifier: CC-BY-4.0
.. Copyright 2018 ORANGE

Release Notes
=============

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

- `Vulnerability Report <https://wiki.onap.org/pages/viewpage.action?pageId=45310585>`_

Quick Links:

- `External API project page <https://wiki.onap.org/display/DW/External+API+Framework+Project>`_



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
