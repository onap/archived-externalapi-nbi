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

import java.util.List;
import java.util.Map;

public class ParametersModel {

    private List<LocationConstraintsModel> locationConstraints;

    private List<ResourceModel> resources;

    private Map<String, String> requestInputs;

    public List<LocationConstraintsModel> getLocationConstraints() {
        return locationConstraints;
    }

    public void setLocationConstraints(List<LocationConstraintsModel> locationConstraints) {
        this.locationConstraints = locationConstraints;
    }

    public List<ResourceModel> getResources() {
        return resources;
    }

    public void setResources(List<ResourceModel> resources) {
        this.resources = resources;
    }

    public Map<String, String> getRequestInputs() {
        return requestInputs;
    }

    public void setRequestInputs(Map<String, String> requestInputs) {
        this.requestInputs = requestInputs;
    }

    @Override
    public String toString() {
        return "ParametersModel{" + "locationConstraints=" + locationConstraints + ", resources=" + resources
                + ", requestInputs=" + requestInputs + '}';
    }
}
