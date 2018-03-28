package org.onap.nbi.apis.serviceinventory;

import java.util.LinkedHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class NbiClient extends BaseClient {

    @Value("${nbi.url}")
    private String nbiUrl;

    public LinkedHashMap getServiceSpecification(String id) {
        StringBuilder callURL = new StringBuilder().append(nbiUrl).append("/serviceSpecification/").append(id);
        ResponseEntity<Object> response = callApiGet(callURL.toString(), new HttpHeaders());
        return (LinkedHashMap) response.getBody();
    }
}
