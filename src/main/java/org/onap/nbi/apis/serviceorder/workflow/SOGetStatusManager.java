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
import org.onap.nbi.apis.serviceorder.model.ServiceStateType;
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

    public void pollRequestStatus(ServiceOrder serviceOrder, ServiceOrderItem serviceOrderItem, boolean e2eService) {
        if (e2eService) {
            pollE2ESoRequestStatus(serviceOrder, serviceOrderItem);
        } else {
            pollSoRequestStatus(serviceOrder, serviceOrderItem);

        }
    }

    /**
     * * @param orderItem
     */
    private void pollSoRequestStatus(ServiceOrder serviceOrder, ServiceOrderItem orderItem) {
        String requestId = orderItem.getRequestId();
        GetRequestStatusResponse response = null;

        response = soClient.callGetRequestStatus(requestId);
        if (response != null) {
            orderItem.setPercentProgress(String.valueOf(response.getRequest().getRequestStatus().getPercentProgress()));
            if (response.getRequest().getRequestStatus().getPercentProgress() != 100) {
                LOGGER.debug("orderitem id {} still in progress from so", orderItem.getId());
            } else if (RequestState.COMPLETE != response.getRequest().getRequestStatus().getRequestState()) {
                serviceOrderService.updateOrderItemState(serviceOrder, orderItem, StateType.FAILED);
                LOGGER.debug("orderitem id {} failed, response from request status {}", orderItem.getId(),
                        response.getRequest().getRequestStatus().getRequestState());
            } else {
		boolean e2eService = false;
                updateOrderItemIfStatusCompleted(serviceOrder, orderItem, e2eService);
                LOGGER.debug("orderitem id {} completed");
            }
        } else {
            LOGGER.debug("orderitem id {} still in progress from so", orderItem.getId());
        }

    }

	private void updateOrderItemIfStatusCompleted(ServiceOrder serviceOrder, ServiceOrderItem orderItem,boolean e2eService) {
    	boolean serviceActivationReq = orderItem.getService().getServiceState() == ServiceStateType.ACTIVE || 
        		orderItem.getService().getServiceState() == ServiceStateType.INACTIVE;
        if (orderItem.getAction() != ActionType.MODIFY) {
            serviceOrderService.updateOrderItemState(serviceOrder, orderItem, StateType.COMPLETED);
        }else if(orderItem.getAction() == ActionType.MODIFY && serviceActivationReq && e2eService) {
        	serviceOrderService.updateOrderItemState(serviceOrder, orderItem, StateType.COMPLETED);
        }else {
            if (StateType.INPROGRESS_MODIFY_REQUEST_CREATE_SEND == orderItem.getState()) {
                serviceOrderService.updateOrderItemState(serviceOrder, orderItem, StateType.COMPLETED);
            } else {
                serviceOrderService.updateOrderItemState(serviceOrder, orderItem,
                        StateType.INPROGRESS_MODIFY_ITEM_TO_CREATE);
            }
        }
    }

    private void pollE2ESoRequestStatus(ServiceOrder serviceOrder, ServiceOrderItem orderItem) {
        String operationId = orderItem.getRequestId();
        String serviceId = orderItem.getService().getId();
        GetE2ERequestStatusResponse response = null;
        final String ERROR = "error";
        final String FINISHED = "finished";
        final String PROCESSING = "processing";
        String result = null;
        response = soClient.callE2EGetRequestStatus(operationId, serviceId);
        if (response != null) {
            orderItem.setPercentProgress(response.getOperation().getProgress());
            result = response.getOperation().getResult();
            if (PROCESSING.equals(result)) {
                LOGGER.debug("orderitem id {} still in progress from so", orderItem.getId());
            } else if (ERROR.equals(result)) {
                serviceOrderService.updateOrderItemState(serviceOrder, orderItem, StateType.FAILED);
                LOGGER.debug("orderitem id {} failed, response from request status {}", orderItem.getId(),
                        response.getOperation().getResult());
            } else if (FINISHED.equals(result)) {
                boolean e2eService = true;
		updateOrderItemIfStatusCompleted(serviceOrder, orderItem,e2eService);
                LOGGER.debug("orderitem id {} completed");
            }
        } else {
            LOGGER.debug("orderitem id {} still in progress from so", orderItem.getId());
        }
    }

}
