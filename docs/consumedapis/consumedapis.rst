.. SPDX-License-Identifier: CC-BY-4.0
.. Copyright 2018 ORANGE


Consumed APIs
=============


NBI application is interacting with 4 ONAP APIs

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

    AAI_GET_TENANTS_PATH = "/aai/v14/cloud-infrastructure/cloud-regions/cloud-region/$onap.cloudOwner/$onap.lcpCloudRegionId/tenants"

    AAI_GET_CUSTOMER_PATH = "/aai/v14/business/customers/customer/"

    AAI_GET_SERVICES_FOR_CUSTOMER_PATH = "/aai/v14/business/customers/customer/$customerId/service-subscriptions"

    AAI_PUT_SERVICE_FOR_CUSTOMER_PATH = "/aai/v14/business/customers/customer/$customerId/service-subscriptions/service-subscription/

    AAI_GET_SERVICE_FOR_CUSTOMER_PATH = "/aai/v14/business/customers/customer/$customerId/service-subscriptions/service-subscription/$serviceSpecName/service-instances/service-instance/$serviceId"

    AAI_GET_SERVICE_INSTANCES_PATH = "/aai/v14/business/customers/customer/$customerId/service-subscriptions/service-subscription/$serviceSpecName/service-instances/"

    AAI_HEALTH_CHECK = "aai/util/echo?action=long"

    AAI_GET_SERVICE = "/aai/v14/nodes/service-instances/service-instance/$serviceId"

    AAI_GET_SERVICE_CUSTOMER = "/aai/v14/nodes/service-instances/service-instance/$serviceId?format=resource_and_url"

    SDC_HEALTH_CHECK = "/sdc2/rest/healthCheck"

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

    MSO_GET_REQUEST_STATUS_PATH = "/ecomp/mso/infra/orchestrationRequests/v7/"

    MSO_DELETE_REQUEST_STATUS_PATH = "/ecomp/mso/infra/serviceInstances/v7/"


else following API are used:

::

    MSO_CREATE_SERVICE_INSTANCE_PATH = "/ecomp/mso/infra/serviceInstance/v6"

    MSO_GET_REQUEST_STATUS_PATH = "/ecomp/mso/infra/orchestrationRequests/v7/"

    MSO_DELETE_REQUEST_STATUS_PATH = "/ecomp/mso/infra/serviceInstances/v7/"

    MSO_HEALTH_CHECK = "/globalhealthcheck"

*********
DMAAP API
*********

This API is used to retrieve Dmaap notifications from SDC and AAI.

::

    DMAAP_CONSUME_EVENTS = "/events/$topic/$consumergroup/$consumerid?timeout=$timeout"