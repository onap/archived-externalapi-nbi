package org.onap.nbi.apis.assertions;


import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import static org.assertj.core.api.Assertions.assertThat;


public class ServiceCatalogAssertions {


    public static void assertGetServiceCatalog(ResponseEntity<Object> resource) {
        assertThat(resource.getStatusCode()).isEqualTo(HttpStatus.OK);
        LinkedHashMap service = (LinkedHashMap) resource.getBody();
        assertThat(service.get("id")).isEqualTo("1e3feeb0-8e36-46c6-862c-236d9c626439");
        assertThat(service.get("name")).isEqualTo("vFW");
        assertThat(service.get("invariantUUID")).isEqualTo("b58a118e-eeb9-4f6e-bdca-e292f84d17df");
        assertThat(service.get("toscaModelURL"))
                .isEqualTo("/sdc/v1/catalog/services/1e3feeb0-8e36-46c6-862c-236d9c626439/toscaModel");
        assertThat(service.get("distributionStatus")).isEqualTo("DISTRIBUTED");
        assertThat(service.get("version")).isEqualTo("2.0");
        assertThat(service.get("lifecycleStatus")).isEqualTo("CERTIFIED");
        assertThat(service.get("@type")).isEqualTo("ONAPservice");
        assertThat(((ArrayList) service.get("attachment")).size()).isEqualTo(5);
        LinkedHashMap relatedParty = (LinkedHashMap) service.get("relatedParty");
        assertThat(relatedParty.get("name")).isEqualTo("Joni Mitchell");
        assertThat(relatedParty.get("role")).isEqualTo("lastUpdater");


        assertThat(((ArrayList) service.get("resourceSpecification")).size()).isEqualTo(2);
        LinkedHashMap resource1 = (LinkedHashMap) ((ArrayList) service.get("resourceSpecification")).get(0);
        assertThat(resource1.get("name")).isEqualTo("vFW-vSINK");
        assertThat(resource1.get("instanceName")).isEqualTo("vFW-vSINK 0");
        assertThat(resource1.get("resourceInvariantUUID")).isEqualTo("18b90934-aa82-456f-938e-e74a07a426f3");
        assertThat(resource1.get("@type")).isEqualTo("ONAPresource");
        assertThat(resource1.get("modelCustomizationId")).isEqualTo("f7ae574e-fd5f-41e7-9b21-75e001561c96");
        assertThat(resource1.get("modelCustomizationName")).isEqualTo("vFW-vSINK");

        assertThat(((ArrayList) service.get("serviceSpecCharacteristic")).size()).isEqualTo(4);
        ArrayList serviceSPecCharacteristics = (ArrayList) service.get("serviceSpecCharacteristic");
        for (Object serviceSPecCharacteristic : serviceSPecCharacteristics) {
            LinkedHashMap serviceSPecCharacteristicMap = (LinkedHashMap) serviceSPecCharacteristic;
            if (serviceSPecCharacteristicMap.get("name").toString().equals("cpus")) {
                assertThat(serviceSPecCharacteristicMap.get("valueType")).isEqualTo("integer");
                assertThat(serviceSPecCharacteristicMap.get("@type")).isEqualTo("ONAPserviceCharacteristic");
                ArrayList serviceSpecCharacteristicValues =
                        (ArrayList) serviceSPecCharacteristicMap.get("serviceSpecCharacteristicValue");
                for (Object serviceSpecCharacteristicValue : serviceSpecCharacteristicValues) {
                    LinkedHashMap serviceSpecCharacteristicValueMap = (LinkedHashMap) serviceSpecCharacteristicValue;
                    if (serviceSpecCharacteristicValueMap.get("value").toString().equals("2")) {
                        assertThat(serviceSpecCharacteristicValueMap.get("isDefault")).isEqualTo(true);
                        assertThat(serviceSpecCharacteristicValueMap.get("valueType")).isEqualTo("integer");
                    } else {
                        assertThat(serviceSpecCharacteristicValueMap.get("isDefault")).isEqualTo(false);
                        assertThat(serviceSpecCharacteristicValueMap.get("valueType")).isEqualTo("integer");
                    }
                }
            }
        }
    }



    public static void asserGetServiceCatalogWithoutTosca(ResponseEntity<Object> resource) {
        assertThat(resource.getStatusCode()).isEqualTo(HttpStatus.PARTIAL_CONTENT);
        LinkedHashMap service = (LinkedHashMap) resource.getBody();
        assertThat(service.get("id")).isEqualTo("1e3feeb0-8e36-46c6-862c-236d9c626439");
        assertThat(service.get("name")).isEqualTo("vFW");
        assertThat(service.get("invariantUUID")).isEqualTo("b58a118e-eeb9-4f6e-bdca-e292f84d17df");
        assertThat(service.get("toscaModelURL"))
                .isEqualTo("/sdc/v1/catalog/services/1e3feeb0-8e36-46c6-862c-236d9c626439toto/toscaModel");
        assertThat(service.get("distributionStatus")).isEqualTo("DISTRIBUTED");
        assertThat(service.get("version")).isEqualTo("2.0");
        assertThat(service.get("lifecycleStatus")).isEqualTo("CERTIFIED");
        assertThat(service.get("@type")).isEqualTo("ONAPservice");
        assertThat(((ArrayList) service.get("attachment")).size()).isEqualTo(5);
        LinkedHashMap relatedParty = (LinkedHashMap) service.get("relatedParty");
        assertThat(relatedParty.get("name")).isEqualTo("Joni Mitchell");
        assertThat(relatedParty.get("role")).isEqualTo("lastUpdater");


        assertThat(((ArrayList) service.get("resourceSpecification")).size()).isEqualTo(2);
        LinkedHashMap resource1 = (LinkedHashMap) ((ArrayList) service.get("resourceSpecification")).get(0);
        assertThat(resource1.get("name")).isEqualTo("vFW-vSINK");
        assertThat(resource1.get("instanceName")).isEqualTo("vFW-vSINK 0");
        assertThat(resource1.get("resourceInvariantUUID")).isEqualTo("18b90934-aa82-456f-938e-e74a07a426f3");
        assertThat(resource1.get("@type")).isEqualTo("ONAPresource");
        assertThat(resource1.get("modelCustomizationId")).isNull();
        assertThat(resource1.get("modelCustomizationName")).isNull();

        assertThat(service.get("serviceSpecCharacteristic")).isNull();
    }



    public static void assertFindServiceCatalog(ResponseEntity<Object> resource) {
        assertThat(resource.getStatusCode()).isEqualTo(HttpStatus.OK);
        ArrayList body = (ArrayList) resource.getBody();
        assertThat(body.size()).isEqualTo(21);
        LinkedHashMap service1 = (LinkedHashMap) body.get(0);
        assertThat(service1.get("id")).isEqualTo("446afaf6-79b5-420e-aff8-7551b00bb510");
        assertThat(service1.get("name")).isEqualTo("FreeRadius-service");
        assertThat(service1.get("invariantUUID")).isEqualTo("7e4781e8-6c6e-41c5-b889-6a321d5f2490");
        assertThat(service1.get("category")).isEqualTo("Network L4+");
        assertThat(service1.get("distributionStatus")).isEqualTo("DISTRIBUTED");
        assertThat(service1.get("version")).isEqualTo("1.0");
        assertThat(service1.get("lifecycleStatus")).isEqualTo("CERTIFIED");
        LinkedHashMap relatedParty = (LinkedHashMap) service1.get("relatedParty");
        assertThat(relatedParty.get("role")).isEqualTo("lastUpdater");
    }



    public static void assertFindServiceCatalogWIthFilter(ResponseEntity<Object> resource) {
        assertThat(resource.getStatusCode()).isEqualTo(HttpStatus.OK);
        ArrayList body = (ArrayList) resource.getBody();
        assertThat(body.size()).isEqualTo(21);


        ObjectNode service1 = (ObjectNode) body.get(0);
        assertThat(service1.get("id")).isNull();
        assertThat(service1.get("name").asText()).isEqualTo("FreeRadius-service");
        assertThat(service1.get("invariantUUID")).isNull();
        assertThat(service1.get("category")).isNull();
        assertThat(service1.get("distributionStatus")).isNull();
        assertThat(service1.get("version")).isNull();
        assertThat(service1.get("lifecycleStatus")).isNull();
        assertThat(service1.get("relatedParty")).isNull();
    }

}

