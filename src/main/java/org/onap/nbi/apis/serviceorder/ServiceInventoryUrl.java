package org.onap.nbi.apis.serviceorder;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ServiceInventoryUrl {

    @Value("${nbi.url}")
    private String nbiUrl;


    public String getServiceInventoryUrl() {

        return new StringBuilder().append(nbiUrl).append("/service/").toString();

    }


}
