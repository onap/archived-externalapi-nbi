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
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * The service specification (default values, etc. are fetched from the catalogue).
 */
@ApiModel(description = "The service specification (default values, etc. are fetched from the catalogue).")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen",
        date = "2018-02-19T14:00:30.767Z")
public class ServiceSpecificationRef {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("href")
    private String href = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("version")
    private String version = null;

    @JsonProperty("targetServiceSchema")
    private TargetServiceSchema targetServiceSchema = null;

    @JsonProperty("@type")
    private String type = null;

    @JsonProperty("@schemaLocation")
    private String schemaLocation = null;

    @JsonProperty("@baseType")
    private String baseType = null;

    public ServiceSpecificationRef id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique identifier of the service specification
     *
     * @return id
     **/
    @JsonProperty("id")
    @ApiModelProperty(required = true, value = "Unique identifier of the service specification")
    @NotNull(message = "ServiceSpecification id cannot be null")
    @Pattern(regexp="^(?!\\s*$).+", message="ServiceSpecification id cannot be empty")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ServiceSpecificationRef href(String href) {
        this.href = href;
        return this;
    }

    /**
     * Reference of the service specification
     *
     * @return href
     **/
    @JsonProperty("href")
    @ApiModelProperty(value = "Reference of the service specification")
    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public ServiceSpecificationRef name(String name) {
        this.name = name;
        return this;
    }

    /**
     * @return name
     **/
    @JsonProperty("name")
    @ApiModelProperty(value = "")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ServiceSpecificationRef version(String version) {
        this.version = version;
        return this;
    }

    /**
     * @return version
     **/
    @JsonProperty("version")
    @ApiModelProperty(value = "")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ServiceSpecificationRef targetServiceSchema(TargetServiceSchema targetServiceSchema) {
        this.targetServiceSchema = targetServiceSchema;
        return this;
    }

    /**
     * @return targetServiceSchema
     **/
    @JsonProperty("targetServiceSchema")
    @ApiModelProperty(value = "")
    @Valid
    public TargetServiceSchema getTargetServiceSchema() {
        return targetServiceSchema;
    }

    public void setTargetServiceSchema(TargetServiceSchema targetServiceSchema) {
        this.targetServiceSchema = targetServiceSchema;
    }

    public ServiceSpecificationRef type(String type) {
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

    public ServiceSpecificationRef schemaLocation(String schemaLocation) {
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

    public ServiceSpecificationRef baseType(String baseType) {
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


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServiceSpecificationRef serviceSpecificationRef = (ServiceSpecificationRef) o;
        return Objects.equals(this.id, serviceSpecificationRef.id)
                && Objects.equals(this.href, serviceSpecificationRef.href)
                && Objects.equals(this.name, serviceSpecificationRef.name)
                && Objects.equals(this.version, serviceSpecificationRef.version)
                && Objects.equals(this.targetServiceSchema, serviceSpecificationRef.targetServiceSchema)
                && Objects.equals(this.type, serviceSpecificationRef.type)
                && Objects.equals(this.schemaLocation, serviceSpecificationRef.schemaLocation)
                && Objects.equals(this.baseType, serviceSpecificationRef.baseType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, href, name, version, targetServiceSchema, type, schemaLocation, baseType);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ServiceSpecificationRef {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    href: ").append(toIndentedString(href)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    version: ").append(toIndentedString(version)).append("\n");
        sb.append("    targetServiceSchema: ").append(toIndentedString(targetServiceSchema)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    schemaLocation: ").append(toIndentedString(schemaLocation)).append("\n");
        sb.append("    baseType: ").append(toIndentedString(baseType)).append("\n");
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

