package org.onap.nbi.apis.serviceorder.model.consumer;

public class CreateServiceInstanceResponse {

    public RequestReference getRequestReference() {
        return requestReference;
    }

    public void setRequestReference(RequestReference requestReference) {
        this.requestReference = requestReference;
    }

    private RequestReference requestReference;
}
