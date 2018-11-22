.. SPDX-License-Identifier: CC-BY-4.0
.. Copyright 2018 ORANGE


Consumed APIs
=============


NBI application is interacting with 3 ONAP APIs

*******
SDC API
*******

This API is used to provide Service Catalog information
Information are retrieved in SDC (and in TOSCA "service template" file)
- Only GET operation is provided - this API DID NOT UPDATE SDC

::

    SDC_ROOT_URL = "/sdc/v1/catalog/services/"

    SDC_GET_PATH = "/metadata"

    SDC_TOSCA_PATH = "/toscaModel"


*******
AAI API
*******

This API is used to provide Service Inventory information
This API retrieves service(s) in the AAI inventory. Only following attributes
will be retrieve in service inventory: id, name and type
(no state or startDate available )

::

    AAI_GET_TENANTS_PATH = "/aai/v11/cloud-infrastructure/cloud-regions/cloud-region/$onap.cloudOwner/$onap.lcpCloudRegionId/tenants"

    AAI_GET_CUSTOMER_PATH = "/aai/v11/business/customers/customer/"

    AAI_GET_SERVICES_FOR_CUSTOMER_PATH = "/aai/v11/business/customers/customer/$customerId/service-subscriptions"

    AAI_PUT_SERVICE_FOR_CUSTOMER_PATH = "/aai/v11/business/customers/customer/$customerId/service-subscriptions/service-subscription/

    AAI_GET_SERVICE_FOR_CUSTOMER_PATH = "/aai/v11/business/customers/customer/$customerId/service-subscriptions/service-subscription/$serviceSpecName/service-instances/service-instance/$serviceId"

    AAI_GET_SERVICE_INSTANCES_PATH = "/aai/v11/business/customers/customer/$customerId/service-subscriptions/service-subscription/$serviceSpecName/service-instances/"



******
SO API
******

This API is used to perform Service Order and thus instantiate a service.
Distinct SO APIs are used for serviceInstance creation request depending on the
serviceSpecification category (set in SDC). If service could be delivered
end-to-end from one request category is set to 'E2E Service'.
In this case NBI uses

::

    MSO_CREATE_E2ESERVICE_INSTANCE_PATH = "/ecomp/mso/infra/e2eServiceInstances/v3"

    MSO_GET_REQUEST_STATUS_PATH = "/ecomp/mso/infra/orchestrationRequests/v6/"

    MSO_DELETE_REQUEST_STATUS_PATH = "/ecomp/mso/infra/serviceInstances/v6/"


else following API are used:

::

    MSO_CREATE_SERVICE_INSTANCE_PATH = "/ecomp/mso/infra/serviceInstance/v6"

    MSO_GET_REQUEST_STATUS_PATH = "/ecomp/mso/infra/orchestrationRequests/v6/"

    MSO_DELETE_REQUEST_STATUS_PATH = "/ecomp/mso/infra/serviceInstances/v6/"
