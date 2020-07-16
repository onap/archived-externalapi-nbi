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

package org.onap.nbi.apis.serviceorder.model.consumer;

public class RequestDetails {

    private ModelInfo modelInfo;

    private SubscriberInfo subscriberInfo;

    private RequestInfo requestInfo;

    private Object requestParameters;

    private CloudConfiguration cloudConfiguration;

    private OwningEntity owningEntity;

    private Project project;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public OwningEntity getOwningEntity() {
        return owningEntity;
    }

    public void setOwningEntity(OwningEntity owningEntity) {
        this.owningEntity = owningEntity;
    }

    public CloudConfiguration getCloudConfiguration() {
        return cloudConfiguration;
    }

    public void setCloudConfiguration(CloudConfiguration cloudConfiguration) {
        this.cloudConfiguration = cloudConfiguration;
    }

    public ModelInfo getModelInfo() {
        return modelInfo;
    }

    public void setModelInfo(ModelInfo modelInfo) {
        this.modelInfo = modelInfo;
    }

    public SubscriberInfo getSubscriberInfo() {
        return subscriberInfo;
    }

    public void setSubscriberInfo(SubscriberInfo subscriberInfo) {
        this.subscriberInfo = subscriberInfo;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public Object getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(Object requestParameters) {
        this.requestParameters = requestParameters;
    }

    @Override
    public String toString() {
        return "RequestDetails{" + "modelInfo=" + modelInfo + ", subscriberInfo=" + subscriberInfo + ", requestInfo="
                + requestInfo + ", requestParameters=" + requestParameters + ", cloudConfiguration="
                + cloudConfiguration + ", owningEntity=" + owningEntity + ", project=" + project + '}';
    }
}
