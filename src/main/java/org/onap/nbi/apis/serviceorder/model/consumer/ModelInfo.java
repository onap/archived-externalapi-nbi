package org.onap.nbi.apis.serviceorder.model.consumer;

public class ModelInfo {

    private String modelType;

    private String modelInvariantId;

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

    @Override
    public String toString() {
        return "ModelInfo{" + "modelType='" + modelType + '\'' + ", modelInvariantId='" + modelInvariantId + '\''
                + ", modelNameVersionId='" + modelNameVersionId + '\'' + ", modelName='" + modelName + '\''
                + ", modelVersion='" + modelVersion + '\'' + ", modelCustomizationName='" + modelCustomizationName
                + '\'' + ", modelCustomizationId='" + modelCustomizationId + '\'' + '}';
    }
}
