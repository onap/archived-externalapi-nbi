package org.onap.nbi.apis.serviceorder;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ServiceCatalogUrl {


    @Value("${nbi.url}")
    private String nbiUrl;

    public String getServiceCatalogUrl() {

        return new StringBuilder().append(nbiUrl).append("/serviceSpecification/").toString();

    }


}
