package org.onap.nbi.apis.serviceinventory.jolt;

import java.util.List;
import org.onap.nbi.exceptions.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import com.bazaarvoice.jolt.exception.JoltException;

@Service
public class GetServiceInventoryJsonTransformer {

    private Chainr chainr;

    private static final Logger LOGGER = LoggerFactory.getLogger(GetServiceInventoryJsonTransformer.class);


    public GetServiceInventoryJsonTransformer() {
        List<Object> specs = JsonUtils.classpathToList("/jolt/getServiceInventory.json");
        this.chainr = Chainr.fromSpec(specs);
    }

    public Object transform(Object serviceSpec) {
        Object output = null;
        try {
            output = chainr.transform(serviceSpec);
        } catch (JoltException joE) {
            LOGGER.error("Unable to transform SDC response with JOLT Transformer : " + joE.getMessage());
            throw new TechnicalException("Error while parsing ONAP response");
        }
        return output;
    }

}
