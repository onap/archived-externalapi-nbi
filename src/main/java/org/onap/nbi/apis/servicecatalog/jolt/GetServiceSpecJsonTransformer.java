package org.onap.nbi.apis.servicecatalog.jolt;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import com.bazaarvoice.jolt.exception.JoltException;
import org.onap.nbi.exceptions.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetServiceSpecJsonTransformer {

    private Chainr chainr;

    private static final Logger LOGGER = LoggerFactory.getLogger(GetServiceSpecJsonTransformer.class);


    public GetServiceSpecJsonTransformer() {
        List<Object> specs = JsonUtils.classpathToList("/jolt/getServiceCatalog.json");
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
