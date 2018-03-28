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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An identified part of the order. A service order is decomposed into one or more order items.
 */
@ApiModel(description = "An identified part of the order. A service order is decomposed into one or more order items.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen",
        date = "2018-02-19T14:00:30.767Z")
public class ServiceOrderItem {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("action")
    private ActionType action = null;

    @JsonProperty("state")
    private StateType state = null;

    @JsonProperty("@type")
    private String type = null;

    @JsonProperty("@schemaLocation")
    private String schemaLocation = null;

    @JsonProperty("@baseType")
    private String baseType = null;

    @JsonIgnore
    private String requestId;

    @JsonProperty("orderItemRelationship")
    private List<OrderItemRelationship> orderItemRelationship = new ArrayList<>();

    @JsonProperty("service")
    private Service service = null;

    public ServiceOrderItem id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Identifier of the line item (generally it is a sequence number 01, 02, 03, …)
     * 
     * @return id
     **/
    @JsonProperty("id")
    @ApiModelProperty(required = true,
            value = "Identifier of the line item (generally it is a sequence number 01, 02, 03, …)")
    @NotNull
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ServiceOrderItem action(ActionType action) {
        this.action = action;
        return this;
    }

    /**
     * The action to be carried out on the Service. Can be add, modify, delete, noChange
     * 
     * @return action
     **/
    @JsonProperty("action")
    @ApiModelProperty(value = "The action to be carried out on the Service. Can be add, modify, delete, noChange")
    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    public ServiceOrderItem state(StateType state) {
        this.state = state;
        return this;
    }

    /**
     * State of the order item (described in the state machine diagram)
     * 
     * @return state
     **/
    @JsonProperty("state")
    @ApiModelProperty(value = "State of the order item (described in the state machine diagram)")
    public StateType getState() {
        return state;
    }

    public void setState(StateType state) {
        this.state = state;
    }

    public ServiceOrderItem type(String type) {
        this.type = type;
        return this;
    }

    /**
     * 
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

    public ServiceOrderItem schemaLocation(String schemaLocation) {
        this.schemaLocation = schemaLocation;
        return this;
    }

    /**
     * 
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

    public ServiceOrderItem baseType(String baseType) {
        this.baseType = baseType;
        return this;
    }

    /**
     * 
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

    public ServiceOrderItem orderItemRelationship(List<OrderItemRelationship> orderItemRelationship) {
        this.orderItemRelationship = orderItemRelationship;
        return this;
    }

    public ServiceOrderItem addOrderItemRelationshipItem(OrderItemRelationship orderItemRelationshipItem) {
        if (this.orderItemRelationship == null) {
            this.orderItemRelationship = new ArrayList<OrderItemRelationship>();
        }
        this.orderItemRelationship.add(orderItemRelationshipItem);
        return this;
    }

    /**
     * Linked order item to the one containing this attribute
     * 
     * @return orderItemRelationship
     **/
    @JsonProperty("orderItemRelationship")
    @ApiModelProperty(value = "Linked order item to the one containing this attribute")
    public List<OrderItemRelationship> getOrderItemRelationship() {
        return orderItemRelationship;
    }

    public void setOrderItemRelationship(List<OrderItemRelationship> orderItemRelationship) {
        this.orderItemRelationship = orderItemRelationship;
    }

    public ServiceOrderItem service(Service service) {
        this.service = service;
        return this;
    }

    /**
     * The Service to be acted on by the order item
     * 
     * @return service
     **/
    @JsonProperty("service")
    @ApiModelProperty(required = true, value = "The Service to be acted on by the order item")
    @NotNull
    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServiceOrderItem serviceOrderItem = (ServiceOrderItem) o;
        return Objects.equals(this.id, serviceOrderItem.id) && Objects.equals(this.action, serviceOrderItem.action)
                && Objects.equals(this.state, serviceOrderItem.state)
                && Objects.equals(this.type, serviceOrderItem.type)
                && Objects.equals(this.schemaLocation, serviceOrderItem.schemaLocation)
                && Objects.equals(this.baseType, serviceOrderItem.baseType)
                && Objects.equals(this.orderItemRelationship, serviceOrderItem.orderItemRelationship)
                && Objects.equals(this.service, serviceOrderItem.service)
                && Objects.equals(this.requestId, serviceOrderItem.requestId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, action, state, type, schemaLocation, baseType, orderItemRelationship, service,
                requestId);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ServiceOrderItem {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    action: ").append(toIndentedString(action)).append("\n");
        sb.append("    state: ").append(toIndentedString(state)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    schemaLocation: ").append(toIndentedString(schemaLocation)).append("\n");
        sb.append("    baseType: ").append(toIndentedString(baseType)).append("\n");
        sb.append("    orderItemRelationship: ").append(toIndentedString(orderItemRelationship)).append("\n");
        sb.append("    requestId: ").append(toIndentedString(requestId)).append("\n");
        sb.append("    service: ").append(toIndentedString(service)).append("\n");
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

