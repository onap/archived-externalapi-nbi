package karatetest;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.ListStubMappingsResult;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import org.onap.nbi.Application;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

public abstract class KarateTestConf {

    static private WireMockServer wireMockServer = new WireMockServer(8091);

    public static void setUp(String mapToRemove) throws Exception {
        wireMockServer.start();
        String[] args= new String[1];
        args[0] = "--server.port=8080";
        Application.main(args);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        removeWireMockMapping(mapToRemove);
    }

    public static void tearsDown() throws Exception {
        wireMockServer.stop();
        wireMockServer.resetToDefaultMappings();
    }

    private static void removeWireMockMapping(String s) {
        if(s == null) return;
        ListStubMappingsResult listStubMappingsResult = wireMockServer.listAllStubMappings();
        StubMapping mappingToDelete = null;
        List<StubMapping> mappings = listStubMappingsResult.getMappings();
        for (StubMapping mapping : mappings) {
            if (mapping.getRequest().getUrl().equals(s)) {
                mappingToDelete = mapping;
            }
        }
        wireMockServer.removeStubMapping(mappingToDelete);
    }
}
