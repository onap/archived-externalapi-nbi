package org.onap.nbi.apis.serviceorder.model.orchestrator;


import java.util.LinkedHashMap;

public class ServiceOrderItemInfo {

    private String id;

    private LinkedHashMap catalogResponse;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LinkedHashMap getCatalogResponse() {
        return catalogResponse;
    }

    public void setCatalogResponse(LinkedHashMap catalogResponse) {
        this.catalogResponse = catalogResponse;
    }
}
