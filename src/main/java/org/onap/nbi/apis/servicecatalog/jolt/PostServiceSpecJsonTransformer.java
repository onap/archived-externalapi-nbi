/*
 * ============LICENSE_START=============================================================================================================
 * Copyright (c) 2020 NikhilMohan
 * ===================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 * ============LICENSE_END===============================================================================================================
 * 
 */
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
public class PostServiceSpecJsonTransformer {
    private Chainr chainr;

    private static final Logger LOGGER = LoggerFactory.getLogger(PostServiceSpecJsonTransformer.class);

    public PostServiceSpecJsonTransformer() {
        List<Object> specs = JsonUtils.classpathToList("/jolt/postServiceCatalog.json");
        this.chainr = Chainr.fromSpec(specs);
    }

    public Object transform(Object serviceSpec) {
        Object output = null;
        try {
            output = chainr.transform(serviceSpec);
        } catch (JoltException joE) {
            LOGGER.error("Unable to transform SDC response with JOLT Transformer", joE);
            throw new TechnicalException("Error while parsing ONAP response");
        }
        return output;
    }

}
