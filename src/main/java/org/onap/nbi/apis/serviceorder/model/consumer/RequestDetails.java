package org.onap.nbi.apis.serviceorder.model.consumer;

public class RequestDetails {

    private ModelInfo modelInfo;

    private SubscriberInfo subscriberInfo;

    private RequestInfo requestInfo;

    private RequestParameters requestParameters;

    private CloudConfiguration cloudConfiguration;


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

    public RequestParameters getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(RequestParameters requestParameters) {
        this.requestParameters = requestParameters;
    }

    @Override
    public String toString() {
        return "RequestDetails{" + "modelInfo=" + modelInfo + ", subscriberInfo=" + subscriberInfo + ", requestInfo="
                + requestInfo + ", requestParameters=" + requestParameters + ", cloudConfiguration="
                + cloudConfiguration + '}';
    }
}
