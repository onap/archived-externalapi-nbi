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
/*
 * API ServiceOrder serviceOrder API designed for ONAP Beijing Release. This API is build from TMF
 * open API16.5 + applied TMF guideline 3.0
 *
 * OpenAPI spec version: 0.1.1_inProgress
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git Do not edit the class manually.
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

package org.onap.nbi.apis.serviceorder.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.onap.nbi.apis.serviceorder.serviceordervalidator.ValidServiceOrder;
import org.onap.nbi.commons.Resource;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * A Service Order is a type of order which can be used to place an order between a customer and a
 * service provider or between a service provider and a partner and vice versa
 */
@ApiModel(
        description = "A Service Order is a type of order which can be used to place an order between a customer and a service provider or between a service provider and a partner and vice versa")
@javax.annotation.Generated(
        value = "io.swagger.codegen.languages.JavaJerseyServerCodegen",
        date = "2018-02-19T14:00:30.767Z")
@Document
@ValidServiceOrder
public class ServiceOrder implements Resource {

    @Id
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("href")
    private String href = null;

    @JsonProperty("externalId")
    private String externalId = null;

    @JsonProperty("priority")
    private String priority = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("category")
    private String category = null;

    @JsonProperty("state")
    private StateType state = null;

    @JsonProperty("orderDate")
    private Date orderDate = null;

    @JsonProperty("completionDateTime")
    private Date completionDateTime = null;

    @JsonProperty("expectedCompletionDate")
    private Date expectedCompletionDate = null;

    @JsonProperty("requestedStartDate")
    private Date requestedStartDate = null;

    @JsonProperty("requestedCompletionDate")
    private Date requestedCompletionDate = null;

    @JsonProperty("startDate")
    private Date startDate = null;

    @JsonProperty("@baseType")
    private String baseType = null;

    @JsonProperty("@type")
    private String type = null;

    @JsonProperty("@schemaLocation")
    private String schemaLocation = null;

    @JsonProperty("relatedParty")
    private List<RelatedParty> relatedParty = null;

    @JsonProperty("orderRelationship")
    private List<OrderRelationship> orderRelationship = null;

    @JsonProperty("orderItem")
    private List<ServiceOrderItem> orderItem = null;

    @JsonProperty("orderMessage")
    private List<OrderMessage> orderMessage = null;

    public ServiceOrder id(String id) {
        this.id = id;
        return this;
    }

    /**
     * ID created on repository side
     *
     * @return id
     **/
    @Override
    @JsonProperty("id")
    @ApiModelProperty(required = true, value = "ID created on repository side")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ServiceOrder href(String href) {
        this.href = href;
        return this;
    }

    /**
     * Hyperlink to access the order
     *
     * @return href
     **/
    @JsonProperty("href")
    @ApiModelProperty(value = "Hyperlink to access the order")
    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public ServiceOrder externalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    /**
     * ID given by the consumer and only understandable by him (to facilitate his searches)
     *
     * @return externalId
     **/
    @JsonProperty("externalId")
    @ApiModelProperty(value = "ID given by the consumer and only understandable by him (to facilitate his searches)")
    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public ServiceOrder priority(String priority) {
        this.priority = priority;
        return this;
    }

    /**
     * A way that can be used by consumers to prioritize orders in Service Order Management system
     * (from 0 to 4 : 0 is the highest priority, and 4 the lowest)
     *
     * @return priority
     **/
    @JsonProperty("priority")
    @ApiModelProperty(
            value = "A way that can be used by consumers to prioritize orders in Service Order Management system (from 0 to 4 : 0 is the highest priority, and 4 the lowest)")
    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public ServiceOrder description(String description) {
        this.description = description;
        return this;
    }

    /**
     * A free-text description of the service order
     *
     * @return description
     **/
    @JsonProperty("description")
    @ApiModelProperty(value = "A free-text description of the service order")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ServiceOrder category(String category) {
        this.category = category;
        return this;
    }

    /**
     * Used to categorize the order that can be useful for the OM system (e.g. “broadband”,
     * “TVOption”, ...)
     *
     * @return category
     **/
    @JsonProperty("category")
    @ApiModelProperty(
            value = "Used to categorize the order that can be useful for the OM system (e.g. “broadband”, “TVOption”, ...)")
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ServiceOrder state(StateType state) {
        this.state = state;
        return this;
    }

    /**
     * State of the order : described in the state-machine diagram
     *
     * @return state
     **/
    @JsonProperty("state")
    @ApiModelProperty(value = "State of the order : described in the state-machine diagram")
    public StateType getState() {
        return state;
    }

    public void setState(StateType state) {
        this.state = state;
    }

    public ServiceOrder orderDate(Date orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    /**
     * @return orderDate
     **/
    @JsonProperty("orderDate")
    @ApiModelProperty(value = "")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public ServiceOrder completionDateTime(Date completionDateTime) {
        this.completionDateTime = completionDateTime;
        return this;
    }

    /**
     * Date when the order was completed
     *
     * @return completionDateTime
     **/
    @JsonProperty("completionDateTime")
    @ApiModelProperty(value = "Date when the order was completed")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    public Date getCompletionDateTime() {
        return completionDateTime;
    }

    public void setCompletionDateTime(Date completionDateTime) {
        this.completionDateTime = completionDateTime;
    }

    /**
     * @return expectedCompletionDate
     **/
    @JsonProperty("expectedCompletionDate")
    @ApiModelProperty(value = "")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    public Date getExpectedCompletionDate() {
        return expectedCompletionDate;
    }

    public void setExpectedCompletionDate(Date expectedCompletionDate) {
        this.expectedCompletionDate = expectedCompletionDate;
    }

    public ServiceOrder requestedStartDate(Date requestedStartDate) {
        this.requestedStartDate = requestedStartDate;
        return this;
    }

    /**
     * Order start date wished by the requestor
     *
     * @return requestedStartDate
     **/
    @JsonProperty("requestedStartDate")
    @ApiModelProperty(value = "Order start date wished by the requestor")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    public Date getRequestedStartDate() {
        return requestedStartDate;
    }

    public void setRequestedStartDate(Date requestedStartDate) {
        this.requestedStartDate = requestedStartDate;
    }

    public ServiceOrder requestedCompletionDate(Date requestedCompletionDate) {
        this.requestedCompletionDate = requestedCompletionDate;
        return this;
    }

    /**
     * Requested delivery date from the requestor perspective
     *
     * @return requestedCompletionDate
     **/
    @JsonProperty("requestedCompletionDate")
    @ApiModelProperty(value = "Requested delivery date from the requestor perspective")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    public Date getRequestedCompletionDate() {
        return requestedCompletionDate;
    }

    public void setRequestedCompletionDate(Date requestedCompletionDate) {
        this.requestedCompletionDate = requestedCompletionDate;
    }

    public ServiceOrder startDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    /**
     * Date when the order was started for processing
     *
     * @return startDate
     **/
    @JsonProperty("startDate")
    @ApiModelProperty(value = "Date when the order was started for processing")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public ServiceOrder baseType(String baseType) {
        this.baseType = baseType;
        return this;
    }

    /**
     * @return baseType
     **/
    @JsonProperty("@baseType")
    @ApiModelProperty(value = "")
    public String getBaseType() {
        return baseType;
    }

    public void setBaseType(String baseType) {
        this.baseType = baseType;
    }

    public ServiceOrder type(String type) {
        this.type = type;
        return this;
    }

    /**
     * @return type
     **/
    @JsonProperty("@type")
    @ApiModelProperty(value = "")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ServiceOrder schemaLocation(String schemaLocation) {
        this.schemaLocation = schemaLocation;
        return this;
    }

    /**
     * @return schemaLocation
     **/
    @JsonProperty("@schemaLocation")
    @ApiModelProperty(value = "")
    public String getSchemaLocation() {
        return schemaLocation;
    }

    public void setSchemaLocation(String schemaLocation) {
        this.schemaLocation = schemaLocation;
    }

    public ServiceOrder relatedParty(List<RelatedParty> relatedParty) {
        this.relatedParty = relatedParty;
        return this;
    }

    public ServiceOrder addRelatedPartyItem(RelatedParty relatedPartyItem) {
        if (this.relatedParty == null) {
            this.relatedParty = new ArrayList<RelatedParty>();
        }
        this.relatedParty.add(relatedPartyItem);
        return this;
    }

    /**
     * A list of related parties which are involved in this order and the role they are playing.
     *
     * @return relatedParty
     **/
    @JsonProperty("relatedParty")
    @ApiModelProperty(
            value = "A list of related parties which are involved in this order and the role they are playing.")
    @Valid
    public List<RelatedParty> getRelatedParty() {
        return relatedParty;
    }

    public void setRelatedParty(List<RelatedParty> relatedParty) {
        this.relatedParty = relatedParty;
    }

    public ServiceOrder orderRelationship(List<OrderRelationship> orderRelationship) {
        this.orderRelationship = orderRelationship;
        return this;
    }

    public ServiceOrder addOrderRelationshipItem(OrderRelationship orderRelationshipItem) {
        if (this.orderRelationship == null) {
            this.orderRelationship = new ArrayList<OrderRelationship>();
        }
        this.orderRelationship.add(orderRelationshipItem);
        return this;
    }

    /**
     * A list of related order references .Linked order to the one containing this attribute
     *
     * @return orderRelationship
     **/
    @JsonProperty("orderRelationship")
    @ApiModelProperty(value = "A list of related order references .Linked order to the one containing this attribute")
    @Valid
    public List<OrderRelationship> getOrderRelationship() {
        return orderRelationship;
    }

    public void setOrderRelationship(List<OrderRelationship> orderRelationship) {
        this.orderRelationship = orderRelationship;
    }

    public ServiceOrder orderItem(List<ServiceOrderItem> orderItem) {
        this.orderItem = orderItem;
        return this;
    }

    public ServiceOrder addOrderItemItem(ServiceOrderItem orderItemItem) {
        if (this.orderItem == null) {
            this.orderItem = new ArrayList<ServiceOrderItem>();
        }
        this.orderItem.add(orderItemItem);
        return this;
    }

    /**
     * A list of order items that have to be processed.
     *
     * @return orderItem
     **/
    @JsonProperty("orderItem")
    @ApiModelProperty(value = "A list of order items that have to be processed.")
    @NotEmpty
    @Valid
    public List<ServiceOrderItem> getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(List<ServiceOrderItem> orderItem) {
        this.orderItem = orderItem;
    }

    public ServiceOrder orderMessage(List<OrderMessage> orderMessage) {
        this.orderMessage = orderMessage;
        return this;
    }

    public ServiceOrder addOrderMessageItem(OrderMessage orderMessageItem) {
        if (this.orderMessage == null) {
            this.orderMessage = new ArrayList<OrderMessage>();
        }
        boolean mesageAlreadyExist = false;
        for (OrderMessage message : this.orderMessage) {
            if (message.getCode().equals(orderMessageItem.getCode())) {
                mesageAlreadyExist = true;

            }
        }
        if (!mesageAlreadyExist) {
            this.orderMessage.add(orderMessageItem);
        }
        return this;
    }

    /**
     *
     * @return orderMessage
     **/
    @JsonProperty("orderMessage")
    @ApiModelProperty(value = "")
    @Valid
    public List<OrderMessage> getOrderMessage() {
        return orderMessage;
    }

    public void setOrderMessage(List<OrderMessage> orderMessage) {
        this.orderMessage = orderMessage;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServiceOrder serviceOrder = (ServiceOrder) o;
        return Objects.equals(this.id, serviceOrder.id) && Objects.equals(this.href, serviceOrder.href)
                && Objects.equals(this.externalId, serviceOrder.externalId)
                && Objects.equals(this.priority, serviceOrder.priority)
                && Objects.equals(this.description, serviceOrder.description)
                && Objects.equals(this.category, serviceOrder.category)
                && Objects.equals(this.state, serviceOrder.state)
                && Objects.equals(this.orderDate, serviceOrder.orderDate)
                && Objects.equals(this.completionDateTime, serviceOrder.completionDateTime)
                && Objects.equals(this.expectedCompletionDate, serviceOrder.expectedCompletionDate)
                && Objects.equals(this.requestedStartDate, serviceOrder.requestedStartDate)
                && Objects.equals(this.requestedCompletionDate, serviceOrder.requestedCompletionDate)
                && Objects.equals(this.startDate, serviceOrder.startDate)
                && Objects.equals(this.baseType, serviceOrder.baseType) && Objects.equals(this.type, serviceOrder.type)
                && Objects.equals(this.schemaLocation, serviceOrder.schemaLocation)
                && Objects.equals(this.relatedParty, serviceOrder.relatedParty)
                && Objects.equals(this.orderRelationship, serviceOrder.orderRelationship)
                && Objects.equals(this.orderItem, serviceOrder.orderItem)
                && Objects.equals(this.orderMessage, serviceOrder.orderMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, href, externalId, priority, description, category, state, orderDate, completionDateTime,
                expectedCompletionDate, requestedStartDate, requestedCompletionDate, startDate, baseType, type,
                schemaLocation, relatedParty, orderRelationship, orderItem, orderMessage);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ServiceOrder {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    href: ").append(toIndentedString(href)).append("\n");
        sb.append("    externalId: ").append(toIndentedString(externalId)).append("\n");
        sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    category: ").append(toIndentedString(category)).append("\n");
        sb.append("    state: ").append(toIndentedString(state)).append("\n");
        sb.append("    orderDate: ").append(toIndentedString(orderDate)).append("\n");
        sb.append("    completionDateTime: ").append(toIndentedString(completionDateTime)).append("\n");
        sb.append("    expectedCompletionDate: ").append(toIndentedString(expectedCompletionDate)).append("\n");
        sb.append("    requestedStartDate: ").append(toIndentedString(requestedStartDate)).append("\n");
        sb.append("    requestedCompletionDate: ").append(toIndentedString(requestedCompletionDate)).append("\n");
        sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
        sb.append("    baseType: ").append(toIndentedString(baseType)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    schemaLocation: ").append(toIndentedString(schemaLocation)).append("\n");
        sb.append("    relatedParty: ").append(toIndentedString(relatedParty)).append("\n");
        sb.append("    orderRelationship: ").append(toIndentedString(orderRelationship)).append("\n");
        sb.append("    orderItem: ").append(toIndentedString(orderItem)).append("\n");
        sb.append("    orderMessage: ").append(toIndentedString(orderMessage)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces (except the first
     * line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
