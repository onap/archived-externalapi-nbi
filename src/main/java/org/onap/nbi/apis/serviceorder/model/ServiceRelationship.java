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
import java.util.Objects;

/**
 * Linked Services to the one instantiate
 */
@ApiModel(description = "Linked Services to the one instantiate")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen",
        date = "2018-02-19T14:00:30.767Z")
public class ServiceRelationship {
    @JsonProperty("type")
    private RelationshipType type = null;

    @JsonProperty("service")
    private Service service = null;

    public ServiceRelationship type(RelationshipType type) {
        this.type = type;
        return this;
    }

    /**
     * Relationship type. It can be : “reliesOn” if the Service needs another already owned Service
     * to rely on (e.g. an option on an already owned mobile access Service) or “targets” or
     * “isTargeted” (depending on the way of expressing the link) for any other kind of links that
     * may be useful
     *
     * @return type
     **/
    @JsonProperty("type")
    @ApiModelProperty(required = true,
            value = "Relationship type. It can be : “reliesOn” if the Service needs another already owned Service to rely on (e.g. an option on an already owned mobile access Service) or “targets” or “isTargeted” (depending on the way of expressing the link) for any other kind of links that may be useful")
    @NotNull(message = "Relationship type cannot be null")
    @Valid
    public RelationshipType getType() {
        return type;
    }

    public void setType(RelationshipType type) {
        this.type = type;
    }

    public ServiceRelationship service(Service service) {
        this.service = service;
        return this;
    }

    /**
     * Service reference
     *
     * @return service
     **/
    @JsonProperty("service")
    @ApiModelProperty(required = true, value = "Service reference")
    @NotNull(message = "Relationship service cannot be null")
    @Valid
    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServiceRelationship serviceRelationship = (ServiceRelationship) o;
        return Objects.equals(this.type, serviceRelationship.type)
                && Objects.equals(this.service, serviceRelationship.service);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, service);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ServiceRelationship {\n");

        sb.append("    type: ").append(toIndentedString(type)).append("\n");
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
