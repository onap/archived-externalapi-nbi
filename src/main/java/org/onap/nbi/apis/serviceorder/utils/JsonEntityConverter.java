package org.onap.nbi.apis.serviceorder.utils;

import java.io.IOException;
import org.onap.nbi.apis.serviceorder.model.orchestrator.ServiceOrderInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonEntityConverter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static String convertServiceOrderInfoToJson(ServiceOrderInfo serviceOrderInfo) {
        return MAPPER.valueToTree(serviceOrderInfo).toString();
    }

    public static ServiceOrderInfo convertJsonToServiceOrderInfo(String serviceOrderInfoJson) throws IOException {
        return MAPPER.treeToValue(MAPPER.readTree(serviceOrderInfoJson), ServiceOrderInfo.class);
    }
}
