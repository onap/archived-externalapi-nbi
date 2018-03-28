package org.onap.nbi.apis.serviceorder.model.orchestrator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class ServiceOrderInfoJson {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long internalId;

    private String serviceOrderId;

    @Lob
    private String serviceOrderInfoJson;

    public ServiceOrderInfoJson() {}

    public ServiceOrderInfoJson(String serviceOrderId, String serviceOrderInfoJson) {
        this.serviceOrderId = serviceOrderId;
        this.serviceOrderInfoJson = serviceOrderInfoJson;
    }

    public String getServiceOrderId() {
        return serviceOrderId;
    }

    public void setServiceOrderId(String serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
    }

    public String getServiceOrderInfoJson() {
        return serviceOrderInfoJson;
    }

    public void setServiceOrderInfoJson(String serviceOrderInfoJson) {
        this.serviceOrderInfoJson = serviceOrderInfoJson;
    }
}
