/**
 * Copyright (c) 2018 Huawei
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

package org.onap.nbi.apis.serviceorder.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResourceSpecification {
	
	@JsonProperty("id")
    private String id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("version")
    private String version = null;

    @JsonProperty("instanceName")
    private String instanceName = null;
    
    @JsonProperty("resourceInvariantUUID")
    private String resourceInvariantUUID = null;
    
	@JsonProperty("resourceType")
    private String resourceType = null;
    
    @JsonProperty("@type")
    private String type = null;
    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getResourceInvariantUUID() {
		return resourceInvariantUUID;
	}

	public void setResourceInvariantUUID(String resourceInvariantUUID) {
		this.resourceInvariantUUID = resourceInvariantUUID;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}



	

}
