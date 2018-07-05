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
package org.onap.nbi.apis.serviceorder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.onap.nbi.apis.serviceorder.model.StateType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

@Service
public class MultiCriteriaRequestBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceOrderResource.class);


    public Query buildRequest(MultiValueMap<String, String> params) {
        Query query = new Query();

        List<String> externalIds = params.get("externalId");
        if (!CollectionUtils.isEmpty(externalIds)) {
            String externalId = externalIds.get(0);
            LOGGER.debug("add criterion externalId {}", externalId);
            query.addCriteria(Criteria.where("externalId").is(externalId));

        }
        List<String> states = params.get("state");
        if (!CollectionUtils.isEmpty(states)) {
            String state = states.get(0);
            LOGGER.debug("add criterion state {}", state);
            query.addCriteria(Criteria.where("state").is(StateType.fromValue(state)));

        }
        List<String> descriptions = params.get("description");
        if (!CollectionUtils.isEmpty(descriptions)) {
            String description = descriptions.get(0);
            LOGGER.debug("add criterion description {}", description);
            query.addCriteria(Criteria.where("description").is(description));

        }

        handleDate(params, query);

        handleOffsetAndLimit(params, query);

        return query;
    }

    private void handleDate(MultiValueMap<String, String> params, Query query) {
        List<String> orderDateLts = params.get("orderDate.lt");
        List<String> orderDateGts = params.get("orderDate.gt");
        if (!CollectionUtils.isEmpty(orderDateLts) || !CollectionUtils.isEmpty(orderDateGts)) {
            Criteria orderDateCriteria = Criteria.where("orderDate");

            if (!CollectionUtils.isEmpty(orderDateLts)) {
                String orderDateLt = orderDateLts.get(0);
                LOGGER.debug("add criterion orderDate.lt {}", orderDateLt);
                orderDateCriteria.lt(convertDate(orderDateLt));
            }
            if (!CollectionUtils.isEmpty(orderDateGts)) {
                String orderDateGt = orderDateGts.get(0);
                LOGGER.debug("add criterion orderDate.gt {}", orderDateGt);
                orderDateCriteria.gt(convertDate(orderDateGt));
            }
            query.addCriteria(orderDateCriteria);
        }
    }

    private void handleOffsetAndLimit(MultiValueMap<String, String> params, Query query) {
        List<String> offsets = params.get("offset");
        List<String> limits = params.get("limit");
        if (!CollectionUtils.isEmpty(offsets) && !CollectionUtils.isEmpty(limits)) {
            String offsetString = offsets.get(0);
            String limitString = limits.get(0);
            int offset = Integer.parseInt(offsetString);
            int limit = Integer.parseInt(limitString);
            final Pageable pageableRequest = new PageRequest(offset, limit);
            query.with(pageableRequest);
        } else if (!CollectionUtils.isEmpty(limits)) {
            String limitString = limits.get(0);
            int limit = Integer.parseInt(limitString);
            final Pageable pageableRequest = new PageRequest(0, limit);
            query.with(pageableRequest);
        }
    }

    private Date convertDate(String dateString) {
        String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            LOGGER.error("unable to convert date " + dateString + ", the pattern is " + dateFormat + " ; " + e);
        }
        return null;

    }

}
