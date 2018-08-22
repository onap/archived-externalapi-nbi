/**
 * Copyright (c) 2018 Orange
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.onap.nbi.apis.serviceinventory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.onap.nbi.apis.serviceinventory.jolt.FindServiceInventoryJsonTransformer;
import org.onap.nbi.apis.serviceinventory.jolt.GetServiceInventoryJsonTransformer;
import org.onap.nbi.exceptions.BackendFunctionalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

@Service
public class ServiceInventoryService {

    @Autowired
    NbiClient nbiClient;

    @Autowired
    AaiClient aaiClient;

    @Autowired
    GetServiceInventoryJsonTransformer getServiceInventoryJsonTransformer;

    @Autowired
    FindServiceInventoryJsonTransformer findServiceInventoryJsonTransformer;

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceInventoryService.class);


    public Map get(String serviceId, MultiValueMap<String, String> params) {

        String clientId = params.getFirst("relatedParty.id");
        String serviceSpecId = params.getFirst("serviceSpecification.id");
        String serviceSpecName = params.getFirst("serviceSpecification.name");

        if (StringUtils.isEmpty(serviceSpecId) && StringUtils.isEmpty(serviceSpecName)) {
            throw new BackendFunctionalException(HttpStatus.NOT_FOUND,
                "serviceSpecName or serviceSpecId must be provided","serviceSpecName or serviceSpecId must be provided");
        }

        String customerId = getCustomerId(clientId);
        String serviceName = getServiceName(serviceSpecName, serviceSpecId);
        Map serviceResponse = aaiClient.getCatalogService(customerId, serviceName, serviceId);

        if (serviceResponse != null) {
            addVnfsToResponse(serviceResponse);
            LinkedHashMap serviceInventoryResponse =
                (LinkedHashMap) getServiceInventoryJsonTransformer.transform(serviceResponse);
            addRelatedPartyId(customerId, serviceInventoryResponse);
            return serviceInventoryResponse;
        } else {
            throw new BackendFunctionalException(HttpStatus.NOT_FOUND, "no catalog service found","no catalog service found");
        }

    }


    private String getCustomerId(String clientId) {

        if (StringUtils.isEmpty(clientId)) {
            return "generic";
        } else {
            return clientId;
        }

    }

    private String getServiceName(String serviceSpecificationName, String serviceSpecificationId) {

        if (StringUtils.isEmpty(serviceSpecificationName)) {
            Map serviceSpecification = nbiClient.getServiceSpecification(serviceSpecificationId);
            return (String) serviceSpecification.get("name");
        } else {
            return serviceSpecificationName;
        }

    }

    private void addRelatedPartyId(String customerId, LinkedHashMap serviceInventoryResponse) {

        LinkedHashMap relatedParty = (LinkedHashMap) serviceInventoryResponse.get("relatedParty");
        relatedParty.put("id", customerId);

    }

    private void addVnfsToResponse(Map serviceResponse) {

        List<Map> vnfs = new ArrayList<>();
        LinkedHashMap relationShip = (LinkedHashMap) serviceResponse.get("relationship-list");
        if (relationShip != null) {
            List<LinkedHashMap> relationsList = (List<LinkedHashMap>) relationShip.get("relationship");
            if (relationsList != null) {
                for (LinkedHashMap relation : relationsList) {
                    String relatedLink = (String) relation.get("related-link");
                    Map vnf = aaiClient.getVNF(relatedLink);
                    if (vnf != null) {
                        vnfs.add(vnf);
                    }
                }
                serviceResponse.put("vnfs", vnfs);
            }
        }
    }


    public List<LinkedHashMap> find(MultiValueMap<String, String> params) {

        String clientId = params.getFirst("relatedParty.id");
        String serviceSpecId = params.getFirst("serviceSpecification.id");
        String serviceSpecName = params.getFirst("serviceSpecification.name");
        String customerId = getCustomerId(clientId);
        String serviceName;
        List<LinkedHashMap> serviceInstances = new ArrayList<>();
        if (StringUtils.isEmpty(serviceSpecId) && StringUtils.isEmpty(serviceSpecName)) {
            handleFindWithNoServiceParam(customerId, serviceInstances);
        } else {
            serviceName = getServiceName(serviceSpecName, serviceSpecId);
            buildServiceInstances(serviceInstances, customerId, serviceName);
        }
        List<LinkedHashMap> serviceInventoryResponse = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(serviceInstances)){
            serviceInventoryResponse =
                findServiceInventoryJsonTransformer.transform(serviceInstances);
            for (LinkedHashMap serviceInventory : serviceInventoryResponse) {
                LinkedHashMap party = (LinkedHashMap) serviceInventory.get("relatedParty");
                party.put("id", customerId);
            }
        }else {
            LOGGER.warn("no service instance found for customer {} ",customerId);
        }
        return serviceInventoryResponse;


    }

    private void handleFindWithNoServiceParam(String customerId, List<LinkedHashMap> serviceInstances) {
        Map servicesInAaiForCustomer = aaiClient.getServicesInAaiForCustomer(customerId);
        if(servicesInAaiForCustomer!=null){
            List<LinkedHashMap> servicesInAAI =
                (List<LinkedHashMap>) servicesInAaiForCustomer.get("service-subscription");
            for (LinkedHashMap service : servicesInAAI) {
                String serviceType = (String) service.get("service-type");
                buildServiceInstances(serviceInstances, customerId, serviceType);
            }
        }else {
            LOGGER.warn("no service instance found for customer {} ",customerId);
        }
    }

    private void buildServiceInstances(List<LinkedHashMap> serviceInstances, String customerId, String serviceType) {

        Map serviceInstancesInAaiForCustomer =
            aaiClient.getServiceInstancesInAaiForCustomer(customerId, serviceType);
        if (serviceInstancesInAaiForCustomer != null) {
            List<LinkedHashMap> serviceInstancesForServiceType =
                (List<LinkedHashMap>) serviceInstancesInAaiForCustomer.get("service-instance");

            if (!CollectionUtils.isEmpty(serviceInstancesForServiceType)) {
                // add service type for jolt
                for (LinkedHashMap serviceInstanceForServiceType : serviceInstancesForServiceType) {
                    serviceInstanceForServiceType.put("service-type", serviceType);
                }
                serviceInstances.addAll(serviceInstancesForServiceType);
            } else {
                LOGGER.warn("no service instance found for customer {} and service type {}",customerId,serviceType);
            }
        } else {
            LOGGER.warn("no service instance found for customer {} and service type {}",customerId,serviceType);
        }


    }

}
