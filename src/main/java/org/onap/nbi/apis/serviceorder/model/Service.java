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

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Service attributes description (these are as per the Service ODE model as used in the Service
 * Inventory specification)
 */
@ApiModel(
        description = "Service attributes description (these are as per the Service ODE model as used in the Service Inventory specification)")
@javax.annotation.Generated(
        value = "io.swagger.codegen.languages.JavaJerseyServerCodegen",
        date = "2018-02-19T14:00:30.767Z")
public class Service {
    @JsonProperty("id")
    private String id = null;
    
    @JsonProperty("serviceType")
    private String servicetype = null;

    @JsonProperty("href")
    private String href = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("serviceState")
    private String serviceState = null;

    @JsonProperty("@type")
    private String type = null;

    @JsonProperty("@schemaLocation")
    private String schemaLocation = null;

    @JsonProperty("serviceCharacteristic")
    private List<ServiceCharacteristic> serviceCharacteristic = null;

    @JsonProperty("serviceRelationship")
    private List<ServiceRelationship> serviceRelationship = null;

    @JsonProperty("relatedParty")
    private List<RelatedParty> relatedParty = null;

    @JsonProperty("serviceSpecification")
    private ServiceSpecificationRef serviceSpecification = null;

    public Service id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Identifier of a service instance
     *
     * @return id
     **/
    @JsonProperty("id")
    @ApiModelProperty(value = "Identifier of a service instance")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    @JsonProperty("serviceType")
    @ApiModelProperty(value = "Business type of the service") 
    public String getServicetype() {
		return servicetype;
	}

	public void setServicetype(String servicetype) {
		this.servicetype = servicetype;
	}

    public Service href(String href) {
        this.href = href;
        return this;
    }

    /**
     * Reference to the owned Service (useful for delete or modify command)
     *
     * @return href
     **/
    @JsonProperty("href")
    @ApiModelProperty(value = "Reference to the owned Service (useful for delete or modify command)")
    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Service name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Name of the service
     *
     * @return name
     **/
    @JsonProperty("name")
    @ApiModelProperty(value = "Name of the service")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Service serviceState(String serviceState) {
        this.serviceState = serviceState;
        return this;
    }

    /**
     * The lifecycle state of the service
     *
     * @return serviceState
     **/
    @JsonProperty("serviceState")
    @ApiModelProperty(value = "The lifecycle state of the service")
    public String getServiceState() {
        return serviceState;
    }

    public void setServiceState(String serviceState) {
        this.serviceState = serviceState;
    }

    public Service type(String type) {
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

    public Service schemaLocation(String schemaLocation) {
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

    public Service serviceCharacteristic(List<ServiceCharacteristic> serviceCharacteristic) {
        this.serviceCharacteristic = serviceCharacteristic;
        return this;
    }

    public Service addServiceCharacteristicItem(ServiceCharacteristic serviceCharacteristicItem) {
        if (this.serviceCharacteristic == null) {
            this.serviceCharacteristic = new ArrayList<ServiceCharacteristic>();
        }
        this.serviceCharacteristic.add(serviceCharacteristicItem);
        return this;
    }

    /**
     * A list of service characteristics .A name/value pair list used to store instance specific
     * values of attributes. The behavior is equivalent to a MAP data structure where only one entry
     * for any given value of \&quot;name\&quot; can exist
     *
     * @return serviceCharacteristic
     **/
    @JsonProperty("serviceCharacteristic")
    @ApiModelProperty(
            value = "A list of service characteristics .A name/value pair list used to store instance specific values of attributes. The behavior is equivalent to a MAP data structure where only one entry for any given value of \"name\" can exist")
    @Valid
    public List<ServiceCharacteristic> getServiceCharacteristic() {
        return serviceCharacteristic;
    }

    public void setServiceCharacteristic(List<ServiceCharacteristic> serviceCharacteristic) {
        this.serviceCharacteristic = serviceCharacteristic;
    }

    public Service serviceRelationship(List<ServiceRelationship> serviceRelationship) {
        this.serviceRelationship = serviceRelationship;
        return this;
    }

    public Service addServiceRelationshipItem(ServiceRelationship serviceRelationshipItem) {
        if (this.serviceRelationship == null) {
            this.serviceRelationship = new ArrayList<ServiceRelationship>();
        }
        this.serviceRelationship.add(serviceRelationshipItem);
        return this;
    }

    /**
     * A list or service relationships (ServiceRelationship[*]). Linked Services to the one
     * instantiate, it can be : “reliesOn” if the Service needs another already owned Service to
     * rely on (e.g. an option on an already owned mobile access Service) or “targets” or
     * “isTargeted” (depending on the way of expressing the link) for any other kind of links that
     * may be useful
     *
     * @return serviceRelationship
     **/
    @JsonProperty("serviceRelationship")
    @ApiModelProperty(
            value = "A list or service relationships (ServiceRelationship[*]). Linked Services to the one instantiate, it can be : “reliesOn” if the Service needs another already owned Service to rely on (e.g. an option on an already owned mobile access Service) or “targets” or “isTargeted” (depending on the way of expressing the link) for any other kind of links that may be useful")
    @Valid
    public List<ServiceRelationship> getServiceRelationship() {
        return serviceRelationship;
    }

    public void setServiceRelationship(List<ServiceRelationship> serviceRelationship) {
        this.serviceRelationship = serviceRelationship;
    }

    public Service relatedParty(List<RelatedParty> relatedParty) {
        this.relatedParty = relatedParty;
        return this;
    }

    public Service addRelatedPartyItem(RelatedParty relatedPartyItem) {
        if (this.relatedParty == null) {
            this.relatedParty = new ArrayList<RelatedParty>();
        }
        this.relatedParty.add(relatedPartyItem);
        return this;
    }

    /**
     * A list of related party parties linked at the Service level (it may be a User for example)
     *
     * @return relatedParty
     **/
    @JsonProperty("relatedParty")
    @ApiModelProperty(
            value = "A list of related party parties linked at the Service level (it may be a User for example)")
    @Valid
    public List<RelatedParty> getRelatedParty() {
        return relatedParty;
    }

    public void setRelatedParty(List<RelatedParty> relatedParty) {
        this.relatedParty = relatedParty;
    }

    public Service serviceSpecification(ServiceSpecificationRef serviceSpecification) {
        this.serviceSpecification = serviceSpecification;
        return this;
    }

    /**
     * @return serviceSpecification
     **/
    @JsonProperty("serviceSpecification")
    @ApiModelProperty(value = "")
    @NotNull(message = "ServiceSpecification cannot be null")
    @Valid
    public ServiceSpecificationRef getServiceSpecification() {
        return serviceSpecification;
    }

    public void setServiceSpecification(ServiceSpecificationRef serviceSpecification) {
        this.serviceSpecification = serviceSpecification;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Service service = (Service) o;
        return Objects.equals(this.id, service.id) && Objects.equals(this.href, service.href)
                && Objects.equals(this.name, service.name) && Objects.equals(this.serviceState, service.serviceState)
                && Objects.equals(this.type, service.type)
                && Objects.equals(this.schemaLocation, service.schemaLocation)
                && Objects.equals(this.serviceCharacteristic, service.serviceCharacteristic)
                && Objects.equals(this.serviceRelationship, service.serviceRelationship)
                && Objects.equals(this.relatedParty, service.relatedParty)
                && Objects.equals(this.serviceSpecification, service.serviceSpecification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, href, name, serviceState, type, schemaLocation, serviceCharacteristic,
                serviceRelationship, relatedParty, serviceSpecification);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Service {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    href: ").append(toIndentedString(href)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    serviceState: ").append(toIndentedString(serviceState)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    schemaLocation: ").append(toIndentedString(schemaLocation)).append("\n");
        sb.append("    serviceCharacteristic: ").append(toIndentedString(serviceCharacteristic)).append("\n");
        sb.append("    serviceRelationship: ").append(toIndentedString(serviceRelationship)).append("\n");
        sb.append("    relatedParty: ").append(toIndentedString(relatedParty)).append("\n");
        sb.append("    serviceSpecification: ").append(toIndentedString(serviceSpecification)).append("\n");
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
