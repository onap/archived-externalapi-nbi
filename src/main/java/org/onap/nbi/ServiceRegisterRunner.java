/**
 * Copyright (c) 2018 Orange
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onap.nbi;

import com.google.common.base.Strings;
import org.onap.msb.sdk.discovery.entity.MicroServiceInfo;
import org.onap.msb.sdk.discovery.entity.Node;
import org.onap.msb.sdk.httpclient.msb.MSBServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

/**
 * Register this NBI instance with the MSB when the app is fully started
 */
@Component
public class ServiceRegisterRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(ServiceRegisterRunner.class);

    @Value("${msb.discovery.host}")
    private String DISCOVERY_HOST;

    @Value("${msb.discovery.port}")
    private int DISCOVERY_PORT;

    @Value("${msb.service.host}")
    private String SERVICE_HOST;

    @Value("${msb.service.port}")
    private String SERVICE_PORT;

    @Value("${msb.service.name}")
    private String SERVICE_NAME;

    @Value("${msb.service.version}")
    private String SERVICE_VERSION;

    @Value("${msb.service.url}")
    private String SERVICE_URL;

    @Value("${msb.service.custom_path}")
    private String SERVICE_CUSTOM_PATH;

    @Value("${msb.service.protocol}")
    private String SERVICE_PROTOCOL;

    @Value("${msb.service.visual_range}")
    private String SERVICE_VISUAL_RANGE;

    @Value("${msb.service.enable_ssl}")
    private boolean SERVICE_ENABLE_SSL;

    @Override
    public void run(String... strings) throws Exception {
        logDebug();

        MicroServiceInfo msinfo = new MicroServiceInfo();
        msinfo.setServiceName(SERVICE_NAME);
        msinfo.setVersion(SERVICE_VERSION);
        msinfo.setUrl(SERVICE_URL);
        msinfo.setProtocol(SERVICE_PROTOCOL);
        msinfo.setVisualRange(SERVICE_VISUAL_RANGE);
        msinfo.setEnable_ssl(SERVICE_ENABLE_SSL);

        if (!Strings.isNullOrEmpty(SERVICE_CUSTOM_PATH)) {
            msinfo.setPath(SERVICE_CUSTOM_PATH);
        }

        Set<Node> nodes = new HashSet<>();

        Node thisNode = new Node();
        thisNode.setIp(Strings.isNullOrEmpty(SERVICE_HOST) ? InetAddress.getLocalHost().getHostAddress() : SERVICE_HOST);
        thisNode.setPort(SERVICE_PORT);
        nodes.add(thisNode);

        msinfo.setNodes(nodes);

        MSBServiceClient msbClient = new MSBServiceClient(DISCOVERY_HOST, DISCOVERY_PORT);
        msbClient.registerMicroServiceInfo(msinfo);
    }

    private void logDebug() {
        logger.debug(
                "Register this service with msb discovery (" + DISCOVERY_HOST + ":" + DISCOVERY_PORT + "):\n"
                        + " - host: [" + SERVICE_HOST + "]\n"
                        + " - port: [" + SERVICE_PORT + "]\n"
                        + " - name: [" + SERVICE_NAME + "]\n"
                        + " - version: [" + SERVICE_VERSION + "]\n"
                        + " - url: [" + SERVICE_URL + "]\n"
                        + " - path: [" + SERVICE_CUSTOM_PATH + "]\n"
                        + " - protocol: [" + SERVICE_PROTOCOL + "]\n"
                        + " - visualRange: [" + SERVICE_VISUAL_RANGE + "]\n"
                        + " - enableSSL: [" + SERVICE_ENABLE_SSL + "]\n"
        );
    }
}
