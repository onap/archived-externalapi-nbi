/**
 * Copyright (c) 2018 Orange
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.onap.nbi.apis.serviceorder.workflow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.onap.nbi.apis.serviceorder.SoClient;
import org.onap.nbi.apis.serviceorder.model.ServiceCharacteristic;
import org.onap.nbi.apis.serviceorder.model.ServiceOrder;
import org.onap.nbi.apis.serviceorder.model.ServiceOrderItem;
import org.onap.nbi.apis.serviceorder.model.StateType;
import org.onap.nbi.apis.serviceorder.model.consumer.CloudConfiguration;
import org.onap.nbi.apis.serviceorder.model.consumer.CreateE2EServiceInstanceResponse;
import org.onap.nbi.apis.serviceorder.model.consumer.CreateServiceInstanceResponse;
import org.onap.nbi.apis.serviceorder.model.consumer.MSOE2EPayload;
import org.onap.nbi.apis.serviceorder.model.consumer.MSOPayload;
import org.onap.nbi.apis.serviceorder.model.consumer.ModelInfo;
import org.onap.nbi.apis.serviceorder.model.consumer.OwningEntity;
import org.onap.nbi.apis.serviceorder.model.consumer.ParametersModel;
import org.onap.nbi.apis.serviceorder.model.consumer.Project;
import org.onap.nbi.apis.serviceorder.model.consumer.RequestDetails;
import org.onap.nbi.apis.serviceorder.model.consumer.RequestInfo;
import org.onap.nbi.apis.serviceorder.model.consumer.RequestParameters;
import org.onap.nbi.apis.serviceorder.model.consumer.ResourceModel;
import org.onap.nbi.apis.serviceorder.model.consumer.ServiceModel;
import org.onap.nbi.apis.serviceorder.model.consumer.SubscriberInfo;
import org.onap.nbi.apis.serviceorder.model.consumer.UserParams;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ServiceOrderInfo;
import org.onap.nbi.apis.serviceorder.service.ServiceOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class PostSoProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostSoProcessor.class);

    @Value("${onap.lcpCloudRegionId}")
    private String lcpCloudRegionId;

    @Value("${onap.tenantId}")
    private String tenantId;

    @Value("${so.owning.entity.id}")
    private String soOwningEntityId;

    @Value("${so.owning.entity.name}")
    private String soOwningEntityName;

    @Value("${so.project.name}")
    private String soProjectName;

    @Value("${onap.cloudOwner}")
    private String cloudOwner;

    @Autowired
    private ServiceOrderService serviceOrderService;

    @Autowired
    private SoClient soClient;

    public ResponseEntity<CreateServiceInstanceResponse> postServiceOrderItem(ServiceOrderInfo serviceOrderInfo,
            ServiceOrderItem serviceOrderItem) {
        ResponseEntity<CreateServiceInstanceResponse> response = null;
        try {
            response = postSORequest(serviceOrderItem, serviceOrderInfo);
        } catch (NullPointerException e) {
            LOGGER.error("Unable to create service instance for serviceOrderItem.id=" + serviceOrderItem.getId(), e);
            response = null;
        }
        return response;
    }

    public ResponseEntity<CreateE2EServiceInstanceResponse> postE2EServiceOrderItem(ServiceOrderInfo serviceOrderInfo,
            ServiceOrderItem serviceOrderItem, ServiceOrder serviceOrder) {
        ResponseEntity<CreateE2EServiceInstanceResponse> response;
        try {
            response = postE2ESORequest(serviceOrderItem, serviceOrderInfo, serviceOrder);
        } catch (NullPointerException e) {
            LOGGER.error("Unable to create service instance for serviceOrderItem.id=" + serviceOrderItem.getId(), e);
            response = null;
        }
        return response;
    }

    private ResponseEntity<CreateServiceInstanceResponse> postSORequest(ServiceOrderItem serviceOrderItem,
            ServiceOrderInfo serviceOrderInfo) {
        RequestDetails requestDetails = buildSoRequest(serviceOrderItem, serviceOrderInfo);
        MSOPayload msoPayload = new MSOPayload(requestDetails);
        ResponseEntity<CreateServiceInstanceResponse> response = null;

        switch (serviceOrderItem.getAction()) {
            case ADD:
                response = soClient.callCreateServiceInstance(msoPayload);
                break;
            case DELETE:
                response = soClient.callDeleteServiceInstance(msoPayload, serviceOrderItem.getService().getId());
                break;
            case MODIFY:
                if (StateType.INPROGRESS_MODIFY_ITEM_TO_CREATE == serviceOrderItem.getState()) {
                    response = soClient.callCreateServiceInstance(msoPayload);
                }
                if (StateType.ACKNOWLEDGED == serviceOrderItem.getState()) {
                    response = soClient.callDeleteServiceInstance(msoPayload, serviceOrderItem.getService().getId());
                }
                break;
            default:
                break;
        }
        return response;
    }

    private ResponseEntity<CreateE2EServiceInstanceResponse> postE2ESORequest(ServiceOrderItem serviceOrderItem,
            ServiceOrderInfo serviceOrderInfo, ServiceOrder serviceOrder) {
        ServiceModel service = buildE2ESoRequest(serviceOrderItem,
                serviceOrderInfo.getServiceOrderItemInfos().get(serviceOrderItem.getId()).getCatalogResponse(),
                serviceOrderInfo.getSubscriberInfo(), serviceOrder);
        MSOE2EPayload msoE2EPayload = new MSOE2EPayload(service);
        ResponseEntity<CreateE2EServiceInstanceResponse> response = null;
        switch (serviceOrderItem.getAction()) {
            case ADD:
                response = soClient.callE2ECreateServiceInstance(msoE2EPayload);
                break;
            case DELETE:
                response = soClient.callE2EDeleteServiceInstance(service.getGlobalSubscriberId(),
                        service.getServiceType(), serviceOrderItem.getService().getId());
                break;
            case MODIFY:
                if (StateType.INPROGRESS_MODIFY_ITEM_TO_CREATE == serviceOrderItem.getState()) {
                    response = soClient.callE2ECreateServiceInstance(msoE2EPayload);
                }
                if (StateType.ACKNOWLEDGED == serviceOrderItem.getState()) {
                    response = soClient.callE2EDeleteServiceInstance(service.getGlobalSubscriberId(),
                            service.getServiceType(), serviceOrderItem.getService().getId());
                }
                break;
            default:
                break;
        }
        return response;
    }

    /**
     * Build SO CREATE request from the ServiceOrder and catalog informations from SDC
     *
     * @param orderItem
     * @param serviceOrderInfo
     * @return
     */
    private RequestDetails buildSoRequest(ServiceOrderItem orderItem, ServiceOrderInfo serviceOrderInfo) {
        RequestDetails requestDetails = new RequestDetails();

        requestDetails.setSubscriberInfo(serviceOrderInfo.getSubscriberInfo());
        Map<String, Object> sdcInfos =
                serviceOrderInfo.getServiceOrderItemInfos().get(orderItem.getId()).getCatalogResponse();
        ModelInfo modelInfo = new ModelInfo();
        modelInfo.setModelType("service");
        modelInfo.setModelInvariantId((String) sdcInfos.get("invariantUUID"));
        modelInfo.setModelNameVersionId(orderItem.getService().getServiceSpecification().getId());
        modelInfo.setModelVersionId(orderItem.getService().getServiceSpecification().getId());
        modelInfo.setModelName((String) sdcInfos.get("name"));
        modelInfo.setModelVersion((String) sdcInfos.get("version"));
        requestDetails.setModelInfo(modelInfo);

        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setInstanceName(orderItem.getService().getName());
        requestInfo.setSource("VID");
        requestInfo.setSuppressRollback(false);
        requestInfo.setRequestorId("NBI");
        requestDetails.setRequestInfo(requestInfo);

        RequestParameters requestParameters = new RequestParameters();
        requestParameters.setSubscriptionServiceType((String) sdcInfos.get("name"));
        requestParameters.setUserParams(
                retrieveUserParamsFromServiceCharacteristics(orderItem.getService().getServiceCharacteristic()));
        requestParameters.setaLaCarte(true);
        requestParameters.setTestApi("GR_API");
        requestDetails.setRequestParameters(requestParameters);

        CloudConfiguration cloudConfiguration = new CloudConfiguration(lcpCloudRegionId, tenantId, cloudOwner);
        requestDetails.setCloudConfiguration(cloudConfiguration);

        OwningEntity owningEntity = new OwningEntity();
        owningEntity.setOwningEntityId(serviceOrderInfo.getOwningEntityId());
        owningEntity.setOwningEntityName(soOwningEntityName);
        requestDetails.setOwningEntity(owningEntity);

        Project project = new Project();
        project.setProjectName(soProjectName);

        requestDetails.setProject(project);

        return requestDetails;
    }

    /**
     * Build E2E SO CREATE request from the ServiceOrder and catalog informations from SDC
     *
     * @param serviceOrderItem
     * @param serviceOrder
     * @param sdcInfos
     * @return
     */
    // ServiceOrderItem serviceOrderItem --> orderItem?
    private ServiceModel buildE2ESoRequest(ServiceOrderItem serviceOrderItem, Map<String, Object> sdcInfos,
            SubscriberInfo subscriberInfo, ServiceOrder serviceOrder) {

        subscriberInfo.getGlobalSubscriberId();
        ServiceModel service = new ServiceModel();
        service.setName(serviceOrderItem.getService().getName());
        service.setDescription(serviceOrder.getDescription());
        service.setServiceUuid(serviceOrderItem.getService().getServiceSpecification().getId());
        service.setServiceInvariantUuid((String) sdcInfos.get("invariantUUID"));
        service.setGlobalSubscriberId(subscriberInfo.getGlobalSubscriberId());
        service.setServiceType((String) sdcInfos.get("name"));

        ParametersModel parameters = new ParametersModel();
        ArrayList<ResourceModel> resources = new ArrayList();

        ArrayList<Object> resourceObjects = (ArrayList<Object>) sdcInfos.get("resourceSpecification");

        for (int i = 0; i < resourceObjects.size(); i++) {

            ResourceModel resourceModel = new ResourceModel((Map<String, Object>) resourceObjects.get(i));
            ParametersModel resourceParameters = new ParametersModel();
            resourceModel.setParameters(resourceParameters);
            resources.add(resourceModel);

        }
        parameters.setResources(resources);
        List<UserParams> userParams =
                retrieveUserParamsFromServiceCharacteristics(serviceOrderItem.getService().getServiceCharacteristic());

        // If there are ServiceCharacteristics add them to requestInputs
        if (!userParams.isEmpty()) {
            Map<String, String> requestInputs = new HashMap<String, String>();
            for (int i = 0; i < userParams.size(); i++) {
                requestInputs.put(userParams.get(i).getName(), userParams.get(i).getValue());
            }

            parameters.setRequestInputs(requestInputs);
        }
        service.setParameters(parameters);

        return service;
    }

    /**
     * Build a list of UserParams for the SO request by browsing a list of ServiceCharacteristics from
     * SDC
     */
    private List<UserParams> retrieveUserParamsFromServiceCharacteristics(List<ServiceCharacteristic> characteristics) {
        List<UserParams> userParams = new ArrayList<>();
        UserParams userParam;

        if (!CollectionUtils.isEmpty(characteristics)) {
            for (ServiceCharacteristic characteristic : characteristics) {
                // Check is the characteristic is of type object, if proceed as before to allow for
                // backwards compatibility.
                if (characteristic.getValueType() != null && !characteristic.getValueType().isEmpty()
                        && characteristic.getValueType().equals("object")) {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode jsonNode = null;
                    try {
                        jsonNode = mapper.readTree(characteristic.getValue().getServiceCharacteristicValue());
                    } catch (IOException e) {
                        LOGGER.error("Failed to read object json {} , exception is ",
                                characteristic.getValue().getServiceCharacteristicValue(), e.getMessage());
                    }
                    ObjectNode objectNode = (ObjectNode) jsonNode;
                    Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields();
                    while (iter.hasNext()) {
                        Map.Entry<String, JsonNode> entry = iter.next();
                        if (!entry.getValue().isArray()) {
                            userParam = new UserParams(entry.getKey(), entry.getValue().asText());
                        } else {
                            ArrayNode arrayNode = (ArrayNode) entry.getValue();
                            String arrayNodeValueString = arrayNode.toString();
                            userParam = new UserParams(entry.getKey(), arrayNodeValueString);
                        }
                        userParams.add(userParam);
                    }
                }
                // as UserParams for all other types, boolean, string, integer etc
                else {
                    userParam = new UserParams(characteristic.getName(),
                            characteristic.getValue().getServiceCharacteristicValue());
                    userParams.add(userParam);
                }
            }
        }

        return userParams;
    }

}
