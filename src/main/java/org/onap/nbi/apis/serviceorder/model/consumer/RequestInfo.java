package org.onap.nbi.apis.serviceorder.model.consumer;

public class RequestInfo {

    private String instanceName;

    private String productFamilyId;

    private String source;

    private boolean suppressRollback;

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getProductFamilyId() {
        return productFamilyId;
    }

    public void setProductFamilyId(String productFamilyId) {
        this.productFamilyId = productFamilyId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isSuppressRollback() {
        return suppressRollback;
    }

    public void setSuppressRollback(boolean suppressRollback) {
        this.suppressRollback = suppressRollback;
    }

    @Override
    public String toString() {
        return "RequestInfo{" +
                "instanceName='" + instanceName + '\'' +
                ", productFamilyId='" + productFamilyId + '\'' +
                ", source='" + source + '\'' +
                ", suppressRollback=" + suppressRollback +
                '}';
    }
}
