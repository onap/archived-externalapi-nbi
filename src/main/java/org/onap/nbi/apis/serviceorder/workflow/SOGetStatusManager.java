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
package org.onap.nbi.apis.serviceorder.workflow;

import org.onap.nbi.apis.serviceorder.SoClient;
import org.onap.nbi.apis.serviceorder.model.ActionType;
import org.onap.nbi.apis.serviceorder.model.ServiceOrder;
import org.onap.nbi.apis.serviceorder.model.ServiceOrderItem;
import org.onap.nbi.apis.serviceorder.model.StateType;
import org.onap.nbi.apis.serviceorder.model.consumer.GetE2ERequestStatusResponse;
import org.onap.nbi.apis.serviceorder.model.consumer.GetRequestStatusResponse;
import org.onap.nbi.apis.serviceorder.model.consumer.RequestState;
import org.onap.nbi.apis.serviceorder.service.ServiceOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SOGetStatusManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(SOGetStatusManager.class);

    @Autowired
    private ServiceOrderService serviceOrderService;

    @Autowired
    private SoClient soClient;



    public void pollRequestStatus(ServiceOrder serviceOrder, ServiceOrderItem serviceOrderItem, boolean e2eService)
        throws InterruptedException {
        if (e2eService) {
            pollE2ESoRequestStatus(serviceOrder, serviceOrderItem);
        } else {
            pollSoRequestStatus(serviceOrder, serviceOrderItem);

        }
    }

    /**
     * * @param orderItem
     */
    private void pollSoRequestStatus(ServiceOrder serviceOrder,
        ServiceOrderItem orderItem) throws InterruptedException {
        boolean stopPolling = false;
        String requestId = orderItem.getRequestId();
        GetRequestStatusResponse response = null;
        int nbRetries = 0;

        while (!stopPolling) {
            response = soClient.callGetRequestStatus(requestId);
            if (response != null) {
                if (response.getRequest().getRequestStatus().getPercentProgress() != 100) {
                    nbRetries++;
                    Thread.sleep(1000);
                    LOGGER.debug("orderitem id {} still in progress from so", orderItem.getId());
                } else if (RequestState.COMPLETE != response.getRequest().getRequestStatus().getRequestState()) {
                    serviceOrderService.updateOrderItemState(serviceOrder, orderItem, StateType.FAILED);
                    stopPolling = true;
                    LOGGER.debug("orderitem id {} failed, response from request status {}", orderItem.getId(),
                        response.getRequest().getRequestStatus().getRequestState());
                } else {
                    updateOrderItemIfStatusCompleted(serviceOrder, orderItem);
                    stopPolling = true;
                    LOGGER.debug("orderitem id {} completed");
                }
            } else {
                stopPolling = true;
                LOGGER.debug("orderitem id {} still in progress from so", orderItem.getId());
            }
            if (nbRetries == 3) {
                stopPolling = true;
                LOGGER.debug("orderitem id {} stop polling from getrequeststatus, 3 retries done", orderItem.getId());

            }
        }
    }

    private void updateOrderItemIfStatusCompleted(ServiceOrder serviceOrder, ServiceOrderItem orderItem) {
        if(orderItem.getAction()!= ActionType.MODIFY){
            serviceOrderService.updateOrderItemState(serviceOrder, orderItem, StateType.COMPLETED);
        } else {
            if(StateType.INPROGRESS_MODIFY_REQUEST_CREATE_SEND ==orderItem.getState()){
                serviceOrderService.updateOrderItemState(serviceOrder, orderItem, StateType.COMPLETED);
            } else {
                serviceOrderService.updateOrderItemState(serviceOrder,orderItem,StateType.INPROGRESS_MODIFY_ITEM_TO_CREATE);
            }
        }
    }

    private void pollE2ESoRequestStatus(ServiceOrder serviceOrder, ServiceOrderItem orderItem)
        throws InterruptedException {
        boolean stopPolling = false;
        String operationId = orderItem.getRequestId();
        String serviceId = orderItem.getService().getId();
        int nbRetries = 0;
        GetE2ERequestStatusResponse response = null;
        final String ERROR = "error";
        final String FINISHED = "finished";
        final String PROCESSING = "processing";
        String result = null;
        while (!stopPolling) {
            response = soClient.callE2EGetRequestStatus(operationId, serviceId);
            if (response != null) {
                result = response.getOperation().getResult();
                if (PROCESSING.equals(result)) {
                    nbRetries++;
                    Thread.sleep(1000);
                    LOGGER.debug("orderitem id {} still in progress from so", orderItem.getId());
                } else if (ERROR.equals(result)) {
                    serviceOrderService.updateOrderItemState(serviceOrder, orderItem, StateType.FAILED);
                    stopPolling = true;
                    LOGGER.debug("orderitem id {} failed, response from request status {}", orderItem.getId(),
                        response.getOperation().getResult());
                } else if (FINISHED.equals(result)) {
                    updateOrderItemIfStatusCompleted(serviceOrder, orderItem);
                    stopPolling = true;
                    LOGGER.debug("orderitem id {} completed");
                }
            } else {
                stopPolling = true;
                LOGGER.debug("orderitem id {} still in progress from so", orderItem.getId());
            }
            if (nbRetries == 3) {
                stopPolling = true;
                LOGGER.debug("orderitem id {} stop polling from getrequeststatus, 3 retries done", orderItem.getId());

            }
        }
    }



}
