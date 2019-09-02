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
    
Feature: Service Inventory

Background:
* url nbiBaseUrl
* def Context = Java.type('org.onap.nbi.test.Context');
* call Context.startServers();
    
Scenario: testServiceResourceGetInventory
Given path 'service','e4688e5f-61a0-4f8b-ae02-a2fbde623bcb'
When method get
Then status 200
And match $ contains { id : 'e4688e5f-61a0-4f8b-ae02-a2fbde623bcb' , href : 'service/e4688e5f-61a0-4f8b-ae02-a2fbde623bcb' , name : 'NewFreeRadius-service-instance-01', type : 'service-instance', @type : 'serviceONAP' }
And match $.serviceSpecification contains { id : '98d95267-5e0f-4531-abf8-f14b90031dc5' , invariantUUID : '709d157b-52fb-4250-976e-7133dff5c347' , @type : 'ONAPservice', name :'servicename2' }
And match $.relatedParty  contains { role : 'ONAPcustomer' , id : 'DemoTest2' }
And match $.supportingResource[0] contains { id : 'cb80fbb6-9aa7-4ac5-9541-e14f45de533e' , name : 'NewFreeRadius-VNF-instance-01' , status :  'PREPROV' , modelInvariantId : 'f5993703-977f-4346-a1c9-c1884f8cfd8d' , modelVersionId : '902438f7-1e4c-492d-b7cc-8650e13b8aeb' , @referredType : 'ONAP resource' }
And match $.supportingResource == '#[2]'


Scenario: testServiceResourceGetInventoryWithStatus
Given path 'service','405c8c00-44b9-4303-9f27-6797d22ca096'
When method get
Then status 200
And match $.state == 'Active'
And match $.relatedParty  contains { role : 'ONAPcustomer' , id : 'DemoTest1' }
And match $.serviceSpecification contains { id : '0bf5f56a-4506-4e98-ab50-336d73ca4b07' , invariantUUID : 'f3ec9092-5c98-41f1-9fea-96be80abd064' , @type : 'ONAPservice', name :'servicename1' }


Scenario: testServiceResourceGetInventoryWithoutRelationShipList
Given path 'service','e4688e5f-61a0-4f8b-ae02-a2fbde623bcbWithoutList'
When method get
Then status 200
And match $ contains { id : 'e4688e5f-61a0-4f8b-ae02-a2fbde623bcbWithoutList' , href : 'service/e4688e5f-61a0-4f8b-ae02-a2fbde623bcbWithoutList' , name : 'NewFreeRadius-service-instance-01' , type : 'service-instance' , @type : 'serviceONAP' }
And match $.serviceSpecification contains { id : '98d95267-5e0f-4531-abf8-f14b90031dc5' , invariantUUID : '709d157b-52fb-4250-976e-7133dff5c347' , @type : 'ONAPservice', name : 'servicename3' }
And match $.relatedParty  contains { role : 'ONAPcustomer' , id : 'DemoTest3' }
And match $.supportingResource == '#[0]'


Scenario: testServiceInventoryFind
Given path 'service'
And params {serviceSpecification.name : 'vFW' , relatedParty.id : '6490' }
When method get
Then status 200
And match $ == '#[1]'
And match $[0] contains { id : 'e4688e5f-61a0-4f8b-ae02-a2fbde623bcb' , href : 'service/e4688e5f-61a0-4f8b-ae02-a2fbde623bcb' ,  name : 'NewFreeRadius-service-instance-01' }
And match $[0].relatedParty  contains { role : 'ONAPcustomer' , id : '6490' }
And match $[0].serviceSpecification contains { name : 'vFW' , id : '98d95267-5e0f-4531-abf8-f14b90031dc5' }

Scenario: testServiceInventoryFindWithServiceSpecId
Given path 'service'
And params {serviceSpecification.id : '1e3feeb0-8e36-46c6-862c-236d9c626439' , relatedParty.id : '6490'}
When method get
Then status 200
And match $ == '#[1]'
And match $[0] contains { id : 'e4688e5f-61a0-4f8b-ae02-a2fbde623bcb' ,  href : 'service/e4688e5f-61a0-4f8b-ae02-a2fbde623bcb' , name : 'NewFreeRadius-service-instance-01' }
And match $[0].relatedParty  contains { role : 'ONAPcustomer' , id : '6490' }
And match $[0].serviceSpecification contains { name : 'vFW' , id : '98d95267-5e0f-4531-abf8-f14b90031dc5' }

Scenario: testServiceInventoryFindWithoutParameter
Given path 'service'
And params {relatedParty.id:'6490'}
When method get
Then status 200
And match $ == '#[2]'
And match $[0] contains { id : 'vfw-service-id' , href : 'service/vfw-service-id' , name : 'vfw-service-name' }
And match $[0].relatedParty  contains { role : 'ONAPcustomer' , id : '6490' }
And match $[0].serviceSpecification contains { name : 'vFW-service-2VF-based' , id : '9vfw-service-modek-version-id' }
And match $[1] contains { id : 'e4688e5f-61a0-4f8b-ae02-a2fbde623bcb' , name : 'NewFreeRadius-service-instance-01' }
And match $[1].relatedParty  contains { role : 'ONAPcustomer' , id : '6490' }
And match $[1].serviceSpecification contains { name : 'vFW' , id : '98d95267-5e0f-4531-abf8-f14b90031dc5' }


Scenario: testServiceResourceGetInventoryWithoutWiremock
* call Context.stopWiremock();
Given path 'service','e4688e5f-61a0-4f8b-ae02-a2fbde623bcb'
And params {serviceSpecification.name : 'vFW' , relatedParty.id : '6490'}
When method get
Then status 500
* call Context.startServers();







