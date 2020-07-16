#    Copyright (c) 2018 Orange
#
#    Licensed under the Apache License, Version 2.0 (the "License");
#    you may not use this file except in compliance with the License.
#    You may obtain a copy of the License at
#
#        http://www.apache.org/licenses/LICENSE-2.0
#
#    Unless required by applicable law or agreed to in writing, software
#    distributed under the License is distributed on an "AS IS" BASIS,
#    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#    See the License for the specific language governing permissions and
#    limitations under the License.swagger: "2.0"

# new feature
# Tags: optional

Feature: Service Catalog

Background:
* url nbiBaseUrl
* def Context = Java.type('org.onap.nbi.test.Context');
* call Context.startServers();
    
Scenario: testServiceCatalogGetResourceWithoutTosca
Given path 'serviceSpecification','1e3feeb0-8e36-46c6-862c-236d9c626439_withoutTosca'
When method get
Then status 206
And match $ contains { id : '1e3feeb0-8e36-46c6-862c-236d9c626439' , name : 'vFW' , invariantUUID : 'b58a118e-eeb9-4f6e-bdca-e292f84d17df' , toscaModelURL : '/sdc/v1/catalog/services/1e3feeb0-8e36-46c6-862c-236d9c626439toto/toscaModel' , distributionStatus : 'DISTRIBUTED' , version : '2.0' , lifecycleStatus : 'CERTIFIED' , @type : 'ONAPservice' , attachment : '#array' , relatedParty : '#notnull' , resourceSpecification : '#array' }
And match $.relatedParty contains { name : 'Joni Mitchell', role : 'lastUpdater' }
And match $.resourceSpecification[0] contains { name : 'vFW-vSINK', resourceInstanceName : 'vFW-vSINK 0', resourceType : 'VF' , resourceInvariantUUID : '18b90934-aa82-456f-938e-e74a07a426f3' , @type : 'ONAPresource', modelCustomizationName : 'vFW-vSINK 0' }
And match $.resourceSpecification == '#[2]'
And match $.attachment == '#[5]'

Scenario: testServiceCatalogGetServiceWithToscaInput
Given path 'serviceSpecification','462f84e5-f0e5-44c5-ab95-38fb4bf77064'
When method get
Then status 200
And match $ contains { id : '462f84e5-f0e5-44c5-ab95-38fb4bf77064' , name : 'vFW' , invariantUUID : 'b58a118e-eeb9-4f6e-bdca-e292f84d17df' , toscaModelURL : '/sdc/v1/catalog/services/462f84e5-f0e5-44c5-ab95-38fb4bf77064/toscaModel' , distributionStatus : 'DISTRIBUTED' , version : '2.0' , lifecycleStatus : 'CERTIFIED' , @type : 'ONAPservice' , attachment : '#array' , relatedParty : '#notnull' , resourceSpecification : '#array' }
And match $.serviceSpecCharacteristic contains
"""
{
    name : 'vFW_ServiceCharacteristics',
    description : 'This object describes all the inputs needed from the client to interact with the vFW Service Topology',
    valueType : 'object',
    @type : 'ONAPServiceCharacteristic',
    @schemaLocation : 'null',
    serviceSpecCharacteristicValue :
    {
       valueType : 'object',
       @schemaLocation : '/serviceSpecification/462f84e5-f0e5-44c5-ab95-38fb4bf77064/specificationInputSchema',
       @type : 'vFW_ServiceCharacteristic',
    }
}
"""


Scenario: testServiceCatalogInputSchema
Given path 'serviceSpecification','462f84e5-f0e5-44c5-ab95-38fb4bf77064'
When method get
Then status 200
Given path 'serviceSpecification','462f84e5-f0e5-44c5-ab95-38fb4bf77064','specificationInputSchema'
When method get
Then status 200
And match $ contains
"""
{
  "ServiceCharacteristics" : {
    "required" : [ "sdwanconnectivity0_name", "sdwanconnectivity0_topology" ],
    "properties" : {
      "sdwanconnectivity0_topology" : {
        "type" : "string",
        "description" : "full mesh, hub-spoke"
      },
      "sdwanconnectivity0_name" : {
        "type" : "string",
        "description" : "the name of this VPM object"
      }
    }
  }
}
"""

Scenario: testServiceCatalogGetServiceWithoutInstanceParamsAndVFModules
Given path 'serviceSpecification','82c9fbb4-656c-4973-8c7f-172b22b5fa8f'
When method get
Then status 200
And match $.resourceSpecification contains
"""
[{
	"id": "35d7887d-3c35-4fb4-aed1-d15b4d9f4ccc",
	"version": "1.0",
	"name": "vLB_VSP",
	"resourceInstanceName": "vLB_VSP 0",
	"modelCustomizationName": "vLB_VSP 0",
	"resourceInvariantUUID": "368371e6-1d2f-4f4a-b992-a9053c7c2f1e",
	"resourceType": "VF",
	"@type": "ONAPresource",
	"modelCustomizationId": "86dcf0b6-5f76-4444-8392-23ca325fd1d9"
}]
"""


Scenario: findServiceCatalog
Given path 'serviceSpecification'
When method get
Then status 200
And assert response.length == 21
And match $[0] contains { id : '446afaf6-79b5-420e-aff8-7551b00bb510' , name : 'FreeRadius-service' , invariantUUID : '7e4781e8-6c6e-41c5-b889-6a321d5f2490' , category : 'Network L4+' , distributionStatus : 'DISTRIBUTED' , version : '1.0' , lifecycleStatus : 'CERTIFIED'  }
And match $[0].relatedParty contains { role : 'lastUpdater' }



Scenario: findServiceCatalogWithFilter
Given path 'serviceSpecification'
And params {fields:'name'}
When method get
Then status 200
And assert response.length == 21
And match $[0] contains { name : 'FreeRadius-service' }

Scenario: findServiceCatalogWithoutWiremock
* call Context.stopWiremock();
Given path 'serviceSpecification','1e3feeb0-8e36-46c6-862c-236d9c626439'
When method get
Then status 500
* call Context.startServers();


