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

package org.onap.nbi.apis.serviceorder.model.consumer;

import java.util.*;

public class ResourceModel {

	private String resourceName;
	
	private String resourceInvariantUuid;
	
	private String resourceUuid;
	
	//private String resourceCustomizationUuid;
	
	private ParametersModel parameters;


	public ResourceModel (Map<String, Object> info) {
		setResourceName((String)info.get("name"));
		setResourceInvariantUuid((String)info.get("resourceInvariantUUID"));
		setResourceUuid((String)info.get("id"));
		//setResourceCustomizationUuid((String)info.get("modelCustomizationId"));
	}

	public String getResourceName() {
		return resourceName;
	}
	
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	
	public String getResourceInvariantUuid() {
		return resourceInvariantUuid;
	}
	
	public void setResourceInvariantUuid(String resourceInvariantUuid) {
		this.resourceInvariantUuid = resourceInvariantUuid;
	}
	
	public String getResourceUuid() {
		return resourceUuid;
	}
	
	public void setResourceUuid(String resourceUuid) {
		this.resourceUuid = resourceUuid;
	}
	
	/*
	public String getResourceCustomizationUuid() {
		return resourceCustomizationUuid;
	}
	
	public void setResourceCustomizationUuid(String resourceCustomizationUuid) {
		this.resourceCustomizationUuid = resourceCustomizationUuid;
	}
	*/
	public ParametersModel getParameters() {
		return parameters;
	}
	
	public void setParameters(ParametersModel parameters) {
		this.parameters = parameters;
	}

	@Override
	public String toString() {
	        return "ResourceModel{" +
	            "resourceName='" + resourceName + '\'' +
	            ", resourceInvariantUuid='" + resourceInvariantUuid + '\'' +
	            ", resourceUuid='" + resourceUuid + '\'' +
	            ", parameters='" + parameters + '}';
	 }
}
