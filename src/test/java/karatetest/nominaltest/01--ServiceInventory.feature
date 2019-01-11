# new feature
# Tags: optional
    
Feature: Service Inventory

Background:
* url demoBaseUrl

    
Scenario: testServiceResourceGetInventory
Given path 'service','e4688e5f-61a0-4f8b-ae02-a2fbde623bcb'
And params {serviceSpecification.name : 'vFW' , relatedParty.id : '6490'}
When method get
Then status 200
And match $ contains { id : 'e4688e5f-61a0-4f8b-ae02-a2fbde623bcb' , name : 'NewFreeRadius-service-instance-01', hasStarted : 'yes', type : 'service-instance', @type : 'serviceONAP' }
And match $.relatedParty contains { role : 'ONAPcustomer' , id : '6490' }
And match $.serviceSpecification contains { id : '98d95267-5e0f-4531-abf8-f14b90031dc5' , invariantUUID : '709d157b-52fb-4250-976e-7133dff5c347' , @type : 'ONAPservice' }
And match $.supportingResource[0] contains { id : 'cb80fbb6-9aa7-4ac5-9541-e14f45de533e' , name : 'NewFreeRadius-VNF-instance-01' , status :  'PREPROV' , modelInvariantId : 'f5993703-977f-4346-a1c9-c1884f8cfd8d' , modelVersionId : '902438f7-1e4c-492d-b7cc-8650e13b8aeb' , @referredType : 'ONAP resource' }
And match $.supportingResource == '#[2]'

Scenario: testServiceResourceGetInventoryWithStatus
Given url 'http://localhost:8080/nbi/api/v3/service/405c8c00-44b9-4303-9f27-6797d22ca096?serviceSpecification.name=AnsibleService&relatedParty.id=6490'
When method get
Then status 200
And match $.state == 'Active'

Scenario: testServiceResourceGetInventoryWithoutRelationShipList
Given path 'service','e4688e5f-61a0-4f8b-ae02-a2fbde623bcbWithoutList'
And params {serviceSpecification.name:'vFW',relatedParty.id:'6490'}
When method get
Then status 200
And match $ contains { id : 'e4688e5f-61a0-4f8b-ae02-a2fbde623bcb' , name : 'NewFreeRadius-service-instance-01' , hasStarted : 'yes' , type : 'service-instance' , @type : 'serviceONAP' }
And match $.relatedParty contains { role : 'ONAPcustomer' , id : '6490' }
And match $.serviceSpecification contains { id : '98d95267-5e0f-4531-abf8-f14b90031dc5' , invariantUUID : '709d157b-52fb-4250-976e-7133dff5c347' , @type : 'ONAPservice' }
And match $.supportingResource == '#[0]'

Scenario: testServiceResourceGetInventoryWithServiceSpecId
Given path 'service','e4688e5f-61a0-4f8b-ae02-a2fbde623bcb'
And params {serviceSpecification.id:'1e3feeb0-8e36-46c6-862c-236d9c626439', relatedParty.id:'6490'}
When method get
Then status 200
And match $ contains { id : 'e4688e5f-61a0-4f8b-ae02-a2fbde623bcb' , name : 'NewFreeRadius-service-instance-01', hasStarted : 'yes', type : 'service-instance', @type : 'serviceONAP' }
And match $.relatedParty contains { role : 'ONAPcustomer' , id : '6490' }
And match $.serviceSpecification contains { id : '98d95267-5e0f-4531-abf8-f14b90031dc5' , invariantUUID : '709d157b-52fb-4250-976e-7133dff5c347' , @type : 'ONAPservice' }
And match $.supportingResource[0] contains { id : 'cb80fbb6-9aa7-4ac5-9541-e14f45de533e' , name : 'NewFreeRadius-VNF-instance-01' , status :  'PREPROV' , modelInvariantId : 'f5993703-977f-4346-a1c9-c1884f8cfd8d' , modelVersionId : '902438f7-1e4c-492d-b7cc-8650e13b8aeb' , @referredType : 'ONAP resource' }
And match $.supportingResource == '#[2]'


Scenario: testServiceInventoryFind
Given path 'service'
And params {serviceSpecification.name : 'vFW' , relatedParty.id : '6490' }
When method get
Then status 200
And match $ == '#[1]'
And match $[0] contains { id : 'e4688e5f-61a0-4f8b-ae02-a2fbde623bcb' , name : 'NewFreeRadius-service-instance-01' }
And match $[0].relatedParty  contains { role : 'ONAPcustomer' , id : '6490' }
And match $[0].serviceSpecification contains { name : 'vFW' , id : '98d95267-5e0f-4531-abf8-f14b90031dc5' }

Scenario: testServiceInventoryFindWithServiceSpecId
Given path 'service'
And params {serviceSpecification.id : '1e3feeb0-8e36-46c6-862c-236d9c626439' , relatedParty.id : '6490'}
When method get
Then status 200
And match $ == '#[1]'
And match $[0] contains { id : 'e4688e5f-61a0-4f8b-ae02-a2fbde623bcb' , name : 'NewFreeRadius-service-instance-01' }
And match $[0].relatedParty  contains { role : 'ONAPcustomer' , id : '6490' }
And match $[0].serviceSpecification contains { name : 'vFW' , id : '98d95267-5e0f-4531-abf8-f14b90031dc5' }

Scenario: testServiceInventoryFindWithoutParameter
Given path 'service'
And params {relatedParty.id:'6490'}
When method get
Then status 200
And match $ == '#[2]'
And match $[0] contains { id : 'vfw-service-id' , name : 'vfw-service-name' }
And match $[0].relatedParty  contains { role : 'ONAPcustomer' , id : '6490' }
And match $[0].serviceSpecification contains { name : 'vFW-service-2VF-based' , id : '9vfw-service-modek-version-id' }
And match $[1] contains { id : 'e4688e5f-61a0-4f8b-ae02-a2fbde623bcb' , name : 'NewFreeRadius-service-instance-01' }
And match $[1].relatedParty  contains { role : 'ONAPcustomer' , id : '6490' }
And match $[1].serviceSpecification contains { name : 'vFW' , id : '98d95267-5e0f-4531-abf8-f14b90031dc5' }








