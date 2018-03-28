package org.onap.nbi.apis.serviceorder.model.consumer;

public class RequestReference {

    private String instanceId;

    private String requestId;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "RequestReference{" +
                "instanceId='" + instanceId + '\'' +
                ", requestId='" + requestId + '\'' +
                '}';
    }
}
