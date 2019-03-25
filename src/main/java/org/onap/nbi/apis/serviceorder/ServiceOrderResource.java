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
package org.onap.nbi.apis.serviceorder;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.onap.nbi.apis.serviceorder.model.ServiceOrder;
import org.onap.nbi.apis.serviceorder.model.StateType;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ServiceOrderInfo;
import org.onap.nbi.apis.serviceorder.service.ServiceOrderService;
import org.onap.nbi.apis.serviceorder.workflow.CheckOrderConsistenceManager;
import org.onap.nbi.apis.serviceorder.workflow.CreateAAICustomerManager;
import org.onap.nbi.apis.serviceorder.workflow.CreateAAIServiceTypeManager;
import org.onap.nbi.apis.serviceorder.workflow.SOTaskManager;
import org.onap.nbi.commons.JsonRepresentation;
import org.onap.nbi.commons.MultiCriteriaRequestBuilder;
import org.onap.nbi.commons.ResourceManagement;
import org.onap.nbi.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/serviceOrder")
public class ServiceOrderResource extends ResourceManagement {

    @Autowired
    ServiceOrderService serviceOrderService;

    @Autowired
    CheckOrderConsistenceManager checkOrderConsistenceManager;

    @Autowired
    CreateAAICustomerManager createAAICustomer;

    @Autowired
    CreateAAIServiceTypeManager createAAIServiceType;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    SOTaskManager serviceOrchestratorManager;

    @Autowired
    MultiCriteriaRequestBuilder multiCriteriaRequestBuilder;

    @Autowired
    ExtApiClient extApiClient;


    @GetMapping(value = "/{serviceOrderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getServiceOrder(@PathVariable String serviceOrderId,
                                                  @RequestParam MultiValueMap<String, String> params, @RequestHeader(required = false) String targetURL) {

        if(targetURL != null) {
            return extApiClient.getServiceOrder(serviceOrderId, targetURL);
        }else {
            Optional<ServiceOrder> optionalServiceOrder = serviceOrderService.findServiceOrderById(serviceOrderId);
            if (!optionalServiceOrder.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            JsonRepresentation filter = new JsonRepresentation(params);
            return this.getResponse(optionalServiceOrder.get(), filter);
        }

    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> findServiceOrder(@RequestParam MultiValueMap<String, String> params) {

        Query query = multiCriteriaRequestBuilder.buildRequest(params);
        List<ServiceOrder> serviceOrders = mongoTemplate.find(query, ServiceOrder.class);
        JsonRepresentation filter = new JsonRepresentation(params);
        long totalCount = serviceOrderService.countServiceOrder();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(totalCount));
        headers.add("X-Result-Count", String.valueOf(serviceOrders.size()));

        return this.findResponse(serviceOrders, filter, headers);

    }

    @DeleteMapping(value = "/{serviceOrderId}")
    public ResponseEntity<Object> deleteServiceOrder(@PathVariable String serviceOrderId) {

        serviceOrderService.deleteServiceOrder(serviceOrderId);

        return this.deleteResponse();

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createServiceOrder(@Valid @RequestBody ServiceOrder serviceOrder, Errors errors,
                                                     @RequestParam MultiValueMap<String, String> params, @RequestHeader(required = false) String targetURL) {

        if (errors != null && errors.hasErrors()) {
            throw new ValidationException(errors.getAllErrors());
        }
        if(targetURL != null) {
            return extApiClient.postServiceOrder(serviceOrder, targetURL);

        }else {
            ServiceOrder serviceOrderSaved = serviceOrderService.createServiceOrder(serviceOrder);
            JsonRepresentation filter = new JsonRepresentation(params);
            return this.createResponse(serviceOrderSaved, filter);
        }

    }


    @PutMapping(value = "/test/{serviceOrderId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> checkServiceOrderRessource(@PathVariable String serviceOrderId,
                                                             @RequestParam MultiValueMap<String, String> params) {
        Optional<ServiceOrder> optionalServiceOrder = serviceOrderService.findServiceOrderById(serviceOrderId);
        if (!optionalServiceOrder.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        ServiceOrder serviceOrder = checkServiceOrder(optionalServiceOrder.get());
        JsonRepresentation filter = new JsonRepresentation(params);
        return this.createResponse(serviceOrder, filter);
    }


    public ServiceOrder checkServiceOrder(ServiceOrder serviceOrder) {
        ServiceOrderInfo serviceOrderInfo = checkOrderConsistenceManager.checkServiceOrder(serviceOrder);
        if (serviceOrderInfo.isServiceOrderRejected()) {
            serviceOrderService.updateOrderState(serviceOrder, StateType.REJECTED);
        } else if (serviceOrderInfo.isAllItemsCompleted()) {
            serviceOrderService.updateOrderState(serviceOrder, StateType.COMPLETED);
        } else {
            createAAICustomer.createAAICustomer(serviceOrder, serviceOrderInfo);
            if (StateType.ACKNOWLEDGED == serviceOrder.getState()) {
                createAAIServiceType.createAAIServiceType(serviceOrder, serviceOrderInfo);
                if (StateType.ACKNOWLEDGED == serviceOrder.getState()) {
                    serviceOrchestratorManager.registerServiceOrder(serviceOrder, serviceOrderInfo);
                }
            }

        }
        return serviceOrder;
    }

}
