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

package org.onap.nbi.apis.hub.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.onap.nbi.apis.hub.model.Event;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.List;

public class CriteriaBuilderServiceOrder implements CriteriaBuilder {
    @Override
    public Criteria adjust(Criteria base, Event event) {
        switch (event.getEventType()) {
            case "ServiceOrderCreationNotification":
                return base;

            case "ServiceOrderStateChangeNotification":
                JsonNode stateNode = event.getEvent().path("state");
                if (stateNode.isValueNode())
                    return base.orOperator(Criteria.where("query.serviceOrder__state").exists(false),
                            Criteria.where("query.serviceOrder__state").in(stateNode.textValue()));
                break;

            case "ServiceOrderItemStateChangeNotification":
                Object[] states = getStates(event);
                if (states.length > 0)
                    return base.orOperator(Criteria.where("query.serviceOrder__serviceOrderItem__state").exists(false),
                            Criteria.where("query.serviceOrder__serviceOrderItem__state").in(states));
                break;
        }

        return base;
    }

    private String[] getStates(Event event) {
        List<String> states = new ArrayList<>();

        JsonNode orderItems = event.getEvent().path("orderItem");
        if (orderItems.isArray()) {
            for (JsonNode node : orderItems) {
                JsonNode stateNode = node.path("state");
                if (stateNode.isValueNode()) {
                    states.add(stateNode.textValue());
                }
            }
        }

        return states.toArray(new String[0]);
    }
}
