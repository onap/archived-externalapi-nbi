.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. http://creativecommons.org/licenses/by/4.0
.. Copyright 2018 ORANGE


==================================================
nbi - northbound interface - External API for ONAP
==================================================
************
Introduction
************

NBI stands for NorthBound Interface. It brings to ONAP a set of API that can be used by external systems as BSS for example. These API are based on **TMF API**.

*******************************************
Global NBI architecture for Beijing release
*******************************************

Following illustration provides a global view about nbi architecture,integration with other ONAP components and API resource/operation provided.

.. image:: images/ONAP_External_ID_Beijing.jpg
   :width: 800px

***********
API Version
***********

APIs are described with a  state version with “v” following the API Name, e.g.:  nbi/api/v1/productOrder.
The schema associated with a REST API must have its version number aligned with that of the REST API.

The version number has major, minor and revision numbers. E.g. v1.0.0
The version number (without the revision number) is held in the URI.

The major version number is incremented for an incompatible change.
The minor version number is incremented for a compatible change.
For minor modifications of the API, version numbering must not be updated, provided the following  backward compatibility rules are respected:

- New elements in a data type must be optional (minOccurs=0)
- Changes in the cardinality of an attribute in a data type must be from mandatory to optional or from lower to greater
- New attributes defined in an element must be optional (absence of use=”required”).
- If new enumerated values are included, the former ones and its meaning must be kept.
- If new operations are added, the existing operations must be kept
- New parameters added to existing operations must be optional and existing parameters

must be kept

For major modifications of the API, not backward compatible and forcing client implementations to be changed, the version number must be updated.

*********
API Table
*********

.. |pdf-icon| image:: images/pdf.png
              :width: 40px

.. |swagger-icon| image:: images/swagger.png
                  :width: 40px


.. |swaggerUI-icon| image:: images/swaggerUI.png
                    :width: 40px

.. |html-icon| image:: images/html.png
               :width: 40px

.. |plantuml-icon| image:: images/uml.jpg
                  :width: 40px

.. |postman-icon| image:: images/postman.png
                  :width: 40px

.. csv-table::
   :header: "API", "|swagger-icon|", "|html-icon|", "|plantuml-icon|", "|swagger-icon|", "|postman-icon|", "|pdf-icon|"
   :widths: 10,5,5,5,5,5,5

   " ", "json file", "html doc", "plantUML doc", "Swagger Editor", "Postman Collection", "pdf doc"
   "serviceCatalog", ":download:`link <swaggers/serviceCatalog_1_0_0.json>`", ":download:`link <serviceCatalog/documentation.html>`", ":download:`link <serviceCatalog/apiServiceCatalog.plantuml>`", "coming", "coming", "coming"
   "serviceInventory", ":download:`link <swaggers/serviceInventory_1_0_0.json>`", ":download:`link <serviceInventory/documentation.html>`", ":download:`link <serviceInventory/apiServiceInventory.plantuml>`", "coming", "coming", "coming"
   "serviceOrder", ":download:`link <swaggers/serviceOrder_1_0_0.json>`", ":download:`link <serviceOrder/documentation.html>`", ":download:`link <serviceOrder/apiServiceOrder.plantuml>`", "coming", ":download:`link <postman/ONAPBeijingServiceOrderDoc.postman_collection.json>`", "coming"


***************
API Description
***************

**serviceCatalog:**

From TMF633 serviceCatalog

API at a glance:
Only high level information are provided - swagger is documented.

Only serviceSpecification resource is provided.
Information are retrieved in SDC (and in Tosca file) - Only GET operation is provided - this API DID NOT UPDATE SDC

Only characteristics at service level will be retrieved in ONAP Tosca file. For example if an ONAP service is composed of VNF and the VF module, the serviceSpecification resource will only feature characteristic describe in the ONAP service tosca model and not attributes in the tosca files for VNF or VF module.

Only ‘basic’ service characteristics will be managed in this release. By ‘basic’ we mean string, boolean, integer parameter type and we do not manage ‘map’ or ‘list parameter type


GET serviceSpecification(list)

(example: GET /nbi/api/v1/serviceSpecification/?category=NetworkService&distributionStatus =DISTRIBUTED)

It is possible to retrieve a list of serviceSpecification (get by list).

Only attributes category and distributionStatus are available for serviceSpecification filtering. It is possible to select retrieved attributes using fields attribute.

if no serviceSpecification matches, an empty list is send back.

GET tservice Specification (id)

(example: GET /nbi/api/v1/serviceSpecification/{uuid})

It is use to retrieve one serviceSpecification - all available information are retieved (see swagger for description)


**serviceInventory:**

From TMF638 serviceInventory

API at a glance:
Only high level information are provided - swagger is documented.

This API retrieves service(s) in the AAI inventory. Only following attributes will be retrieve in service inventory: id, name and type (no state or startDate available )

GET Service Inventory (list):

(example: GET /nbi/api/v1/service/?relatedParty.id=Pontus
)

GET (by list) allows to request with following criteria (all optional) :

*   id (id of the service instance) - id of the service instance (inventory)
*   serviceSpecification.id - id of the service specification (catalog)
*   serviceSpecification.name - name of the service specification (catalog)
*   relatedParty.id - id of the (aai) customer - if not filled we use ‘generic’ customer

if no service matches, an empty list is send back.

1.	If a request is send without any parameter, we’ll retrieve the list of service-instance for the ‘generic’ customer
2.	If only customer parameter is filled (relatedParty.id + role= relatedParty’ONAPcustomer’) we’ll retrieve the list of service-instance for this customer
3.	If serviceSpecification.id or name is filled we’ll retrieve the list of Service instance (from this service specification) – We’ll use the customer id if provided (with Role=’ONAPcustomer) or generic if no customer id provided


GET Service Inventory (id):

(example: GET /nbi/api/v1/service/{uuid} but customerId & serviceSpecification.id must passed in requested parameters)


Because of AAI capability, additionally to the service id, customer id and [serviceSpecification.id or serviceSpecification.name] must be supplied. If the customer id is not supplied, External API will use ‘generic’ customer

**serviceOrder:**


From TMF641 serviceOrder

API at a glance:
Only high level information are provided - swagger is documented.

It is possible to use POST operation to create new serviceOrder in nbi and triggers service provisioning. GET operation is also available to retrieve one service order by providing id or a list of service order. For this release, only a subset of criteria is available:

•	externalId
•	state
•	description
•	orderDate.gt (orderDate must be greater – after -than)
•	orderDate.lt (orderDate must be lower-before - than)
•	fields – attribute used to filter retrieved attributes (if needed) and also for sorted SO
•	offset and limit are used for pagination purpose



ServiceOrder will manage only ‘add’ and ‘delete’ operation (no change).

prerequisites & assumptions :

•	Cloud & tenant information MUST BE defined in the external API property file
•	Management of ONAP customer for add service action

With the current version of APIs used from SO and AAI we need to manage a ‘customer’. This customer concept is confusing with Customer BSS concept. We took the following rules to manage the ‘customer’ information:

•	It could be provided through a serviceOrder in the service Order a relatedParty with role ‘ONAPcustomer’ should be provided in the serviceOrder header (we will not consider in this release the party at item level); External API component will check if this customer exists and create it in AAI if not.
•	If no relatedParty are provided the service will be affected to ‘generic’ customer (dummy customer) – we assume this ‘generic’ customer always exists.
•	Additionally nbi will create in AAI the service-type if it did not exists for the customer
•	Integration is done at service-level: nbi will trigger only SO request at serviceInstance level -->  ONAP prerequisite: SO must be able to find a BPMN to process service fulfillment (integrate vnf, vnf activation in SDNC, VF module
•	State management: States are only managed by ServiceOrder component and could not be updated from north side via API. Accordingly to service order item fulfillment progress, order item state are updated. Order state is automatically updated based on item state.


***************
Developer Guide
***************

Technical information about NBI (dependancies, configuration, running & testing) could be found here: :doc:`NBI_R1_Developer_Guide <../architecture/NBI_R1_Developer_Guide>`

API Flow illustration (with example messages) is described in this document: :download:`nbicallflow.pdf <pdf/nbicallflow.pdf>`

