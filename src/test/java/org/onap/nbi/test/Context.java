/**
 * Copyright (c) 2018 Orange
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.onap.nbi.test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import java.util.List;
import org.onap.nbi.Application;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class Context {


  public static ConfigurableApplicationContext nbiServer;

  public static WireMockServer wireMockServer;

  public static void startServers() throws Exception {
    startWiremock();

    // NBI
    if (nbiServer == null) {
      String[] args = new String[1];
      args[0] = "--spring.profiles.active=test";
      nbiServer = Application.run(args);
      MockHttpServletRequest request = new MockHttpServletRequest();
      RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

  }

  public static void startWiremock() {
    // Wiremock
    // If wireMockServer was previously created
    if (wireMockServer != null) {
      wireMockServer.stop();
      wireMockServer.resetToDefaultMappings();
    } else {
      wireMockServer =
          new WireMockServer(
              WireMockConfiguration.wireMockConfig().port(8091).jettyStopTimeout(1000L));
    }

    wireMockServer.start();
  }

  public static void removeWireMockMapping(String mappingToBeRemoved) {

    if (mappingToBeRemoved == null) {
      return;
    }

    // Get current mappings
    List<StubMapping> mappings = wireMockServer.listAllStubMappings().getMappings();

    StubMapping mappingToDelete = null;
    for (StubMapping mapping : mappings) {
      if (mapping.getRequest().getUrl().equals(mappingToBeRemoved)) {
        mappingToDelete = mapping;
      }
    }

    if (mappingToDelete != null) {
      wireMockServer.removeStubMapping(mappingToDelete);
    }
  }

  public static void stopServers() throws Exception {

    // NBI
    if (nbiServer != null) { // keep spring boot side alive for all tests including package
      // 'mock'
      nbiServer.close();
    }

    // Wiremock
    if (wireMockServer != null) {
      wireMockServer.stop();
    }

  }
  public static void stopWiremock() throws Exception {

    // Wiremock
    if (wireMockServer != null) {
      wireMockServer.stop();
    }

  }
}
