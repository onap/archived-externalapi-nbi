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

public class ServiceModel {
	
	private String name;
	
	private String description;
	
	private String serviceInvariantUuid;
	
	private String serviceUuid;
	
	private String globalSubscriberId;
	
	private String serviceType;
	
	private ParametersModel parameters;
	
	
	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getServiceInvariantUuid() {
		return serviceInvariantUuid;
	}
	
	public void setServiceInvariantUuid(String serviceInvariantUuid) {
		this.serviceInvariantUuid = serviceInvariantUuid;
	}
	
	public String getServiceUuid() {
		return serviceUuid;
	}
	
	public void setServiceUuid(String serviceUuid) {
		this.serviceUuid = serviceUuid;
	}
	
	public String getGlobalSubscriberId() {
		return globalSubscriberId;
	}
	
	public void setGlobalSubscriberId(String globalSubscriberId) {
		this.globalSubscriberId = globalSubscriberId;
	}
	
	public String getServiceType() {
		return serviceType;
	}
	
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
	public ParametersModel getParameters() {
		return parameters;
	}
	
	public void setParameters(ParametersModel parameters) {
		this.parameters = parameters;
	}

	@Override
	public String toString() {
	        return "ServiceModel{" +
	            "name='" + name + '\'' +
	            ", description='" + description + '\'' +
	            ", serviceInvariantUuid='" + serviceInvariantUuid + '\'' +
	            ", serviceUuid='" + serviceUuid + '\'' +
	            ", globalSubscriberId='" + globalSubscriberId + '\'' +
	            ", serviceType='" + serviceType + '\'' +
	            ", parameters='" + parameters + '}';
	 }
}
