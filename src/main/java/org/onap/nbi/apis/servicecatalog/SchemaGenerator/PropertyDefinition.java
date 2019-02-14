/**
 * Copyright (c) 2018 Huawei
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

package org.onap.nbi.apis.servicecatalog.SchemaGenerator;

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.annotations.ApiModelProperty;

public class PropertyDefinition {
	
    @JsonIgnore
    String id;
    String description;
    @Nullable
    Boolean required;
    String type;
    @JsonProperty("default")
    JsonNode defaultValue;
    String status;
    List<ConstraintClause> constraints;
    @JsonProperty("entry_schema")
    EntrySchema entrySchema;
    @ApiModelProperty(notes = "Property Value, It may be raw JSON or primitive data type values")
    JsonNode value;
    
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getRequired() {
		return required;
	}
	public void setRequired(Boolean required) {
		this.required = required;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public JsonNode getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(JsonNode defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<ConstraintClause> getConstraints() {
		return constraints;
	}
	public void setConstraints(List<ConstraintClause> constraints) {
		this.constraints = constraints;
	}
	public EntrySchema getEntrySchema() {
		return entrySchema;
	}
	public void setEntrySchema(EntrySchema entrySchema) {
		this.entrySchema = entrySchema;
	}
	public JsonNode getValue() {
		return value;
	}
	public void setValue(JsonNode value) {
		this.value = value;
	}
	
}