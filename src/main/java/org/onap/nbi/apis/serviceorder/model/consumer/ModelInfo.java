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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ModelInfo {

    private String modelType;

    private String modelInvariantId;

    private String modelVersionId;

    private String modelNameVersionId;

    private String modelName;

    private String modelVersion;

    private String modelCustomizationName;

    private String modelCustomizationId;

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public String getModelInvariantId() {
        return modelInvariantId;
    }

    public void setModelInvariantId(String modelInvariantId) {
        this.modelInvariantId = modelInvariantId;
    }

    public String getModelNameVersionId() {
        return modelNameVersionId;
    }

    public void setModelNameVersionId(String modelNameVersionId) {
        this.modelNameVersionId = modelNameVersionId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getModelCustomizationName() {
        return modelCustomizationName;
    }

    public void setModelCustomizationName(String modelCustomizationName) {
        this.modelCustomizationName = modelCustomizationName;
    }

    public String getModelCustomizationId() {
        return modelCustomizationId;
    }

    public void setModelCustomizationId(String modelCustomizationId) {
        this.modelCustomizationId = modelCustomizationId;
    }

    public String getModelVersionId() {
        return modelVersionId;
    }

    public void setModelVersionId(String modelVersionId) {
        this.modelVersionId = modelVersionId;
    }

    @Override
    public String toString() {
        return "ModelInfo{" + "modelType='" + modelType + '\'' + ", modelInvariantId='" + modelInvariantId + '\''
                + ", modelVersionId='" + modelVersionId + '\'' + ", modelNameVersionId='" + modelNameVersionId + '\''
                + ", modelName='" + modelName + '\'' + ", modelVersion='" + modelVersion + '\''
                + ", modelCustomizationName='" + modelCustomizationName + '\'' + ", modelCustomizationId='"
                + modelCustomizationId + '\'' + '}';
    }
}
