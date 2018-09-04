/**
 *     Copyright (c) 2018 Vodafone Group
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
package org.onap.nbi.apis.serviceorder.model.consumer;

public class CreateE2EServiceInstanceResponse {

	private ServiceResponse service;

	public ServiceResponse getService() {
		return service;
	}

	public void setService(ServiceResponse service) {
		this.service = service;
	}

	@Override
	public String toString() {
		return "CreateE2EServiceInstanceResponse{" +
			"service=" + service +
			'}';
	}
}