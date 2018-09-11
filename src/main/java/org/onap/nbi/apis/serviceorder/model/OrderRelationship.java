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
import javax.validation.constraints.Pattern;

/**
 * Linked order to the one containing this attribute
 */
@ApiModel(description = "Linked order to the one containing this attribute")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen",
        date = "2018-02-19T14:00:30.767Z")
public class OrderRelationship {
    @JsonProperty("type")
    private String type = null;

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("href")
    private String href = null;

    @JsonProperty("@referredType")
    private String referredType = null;

    public OrderRelationship type(String type) {
        this.type = type;
        return this;
    }

    /**
     * The type of related order, can be : “dependency” if the order needs to be “not started” until
     * another order item is complete (a service order in this case) or “cross-ref” to keep track of
     * the source order (a productOrder)
     *
     * @return type
     **/
    @JsonProperty("type")
    @ApiModelProperty(
            value = "The type of related order, can be : “dependency” if the order needs to be “not started” until another order item is complete (a service order in this case) or “cross-ref” to keep track of the source order (a productOrder)")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public OrderRelationship id(String id) {
        this.id = id;
        return this;
    }

    /**
     * The id of the related order
     *
     * @return id
     **/
    @JsonProperty("id")
    @ApiModelProperty(required = true, value = "The id of the related order")
    @NotNull(message = "OrderRelationship id cannot be null")
    @Pattern(regexp="^(?!\\s*$).+", message="OrderRelationship id cannot be empty")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrderRelationship href(String href) {
        this.href = href;
        return this;
    }

    /**
     * A hyperlink to the related order
     *
     * @return href
     **/
    @JsonProperty("href")
    @ApiModelProperty(value = "A hyperlink to the related order")
    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public OrderRelationship referredType(String referredType) {
        this.referredType = referredType;
        return this;
    }

    /**
     * @return referredType
     **/
    @JsonProperty("@referredType")
    @ApiModelProperty(value = "")
    public String getReferredType() {
        return referredType;
    }

    public void setReferredType(String referredType) {
        this.referredType = referredType;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderRelationship orderRelationship = (OrderRelationship) o;
        return Objects.equals(this.type, orderRelationship.type) && Objects.equals(this.id, orderRelationship.id)
                && Objects.equals(this.href, orderRelationship.href)
                && Objects.equals(this.referredType, orderRelationship.referredType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id, href, referredType);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OrderRelationship {\n");

        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    href: ").append(toIndentedString(href)).append("\n");
        sb.append("    referredType: ").append(toIndentedString(referredType)).append("\n");
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

