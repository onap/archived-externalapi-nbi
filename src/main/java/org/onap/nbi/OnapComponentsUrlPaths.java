/**
 *     Copyright (c) 2018 Orange
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package org.onap.nbi;

/**
 * Contains ONAP SDC and AAI urlPaths
 *
 */
public final class OnapComponentsUrlPaths {

    private OnapComponentsUrlPaths() {}

    // SDC
    public static final String SDC_ROOT_URL = "/sdc/v1/catalog/services";
    public static final String SDC_GET_PATH = "/metadata";
    public static final String SDC_TOSCA_PATH = "/toscaModel";

    // AAI
    public static final String AAI_GET_TENANTS_PATH =
            "/aai/v11/cloud-infrastructure/cloud-regions/cloud-region/$onap.cloudOwner/$onap.lcpCloudRegionId/tenants";
    public static final String AAI_GET_CUSTOMER_PATH = "/aai/v11/business/customers/customer/";
    public static final String AAI_GET_SERVICES_FOR_CUSTOMER_PATH =
            "/aai/v11/business/customers/customer/$customerId/service-subscriptions";
    public static final String AAI_PUT_SERVICE_FOR_CUSTOMER_PATH =
            "/aai/v11/business/customers/customer/$customerId/service-subscriptions/service-subscription/";
    public static final String AAI_GET_SERVICE_FOR_CUSTOMER_PATH =
            "/aai/v11/business/customers/customer/$customerId/service-subscriptions/service-subscription/$serviceSpecName/service-instances/service-instance/$serviceId";
    public static final String AAI_GET_SERVICE_INSTANCES_PATH =
            "/aai/v11/business/customers/customer/$customerId/service-subscriptions/service-subscription/$serviceSpecName/service-instances/";

    // MSO
    public static final String MSO_CREATE_SERVICE_INSTANCE_PATH = "/ecomp/mso/infra/serviceInstances/v4";
    public static final String MSO_GET_REQUEST_STATUS_PATH = "/ecomp/mso/infra/orchestrationRequests/v4/";
    public static final String MSO_DELETE_REQUEST_STATUS_PATH = "/ecomp/mso/infra/serviceInstances/v4/";
}
