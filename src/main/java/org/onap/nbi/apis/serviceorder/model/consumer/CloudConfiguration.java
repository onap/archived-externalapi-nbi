package org.onap.nbi.apis.serviceorder.model.consumer;

public class CloudConfiguration {

    private String lcpCloudRegionId;

    private String tenantId;

    public CloudConfiguration(String lcpCloudRegionId, String tenantId) {
        this.lcpCloudRegionId = lcpCloudRegionId;
        this.tenantId = tenantId;
    }

    public String getLcpCloudRegionId() {
        return lcpCloudRegionId;
    }

    public void setLcpCloudRegionId(String lcpCloudRegionId) {
        this.lcpCloudRegionId = lcpCloudRegionId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "CloudConfiguration{" + "lcpCloudRegionId='" + lcpCloudRegionId + '\'' + ", tenantId='" + tenantId + '\''
                + '}';
    }
}
