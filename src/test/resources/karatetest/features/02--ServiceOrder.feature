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
    
Feature: Service order

Background:
* url nbiBaseUrl
* def Context = Java.type('org.onap.nbi.test.Context');
* call Context.startServers();
* def data = read('../data/serviceOrder.json')
* def sleep = function(){java.lang.Thread.sleep(5000)}

    
Scenario: testCreateServiceOrderResource
Given path 'serviceOrder'
And request data[0]
When method post
Then status 201
And match $.id contains '#notnull'
And match $.state == 'acknowledged'
And def serviceOrderId = $.id
Given path 'serviceOrder',serviceOrderId
When method delete
Then status 204

Scenario: testCheckServiceOrder
Given path 'serviceOrder'
And request data[0]
When method post
Then status 201
And def serviceOrderId = $.id
Given path 'serviceOrder','test',serviceOrderId
And request $
When method put
Then status 201
And match $.id == serviceOrderId
And match $.state == 'inProgress'
Given path 'serviceOrder',serviceOrderId
When method get
Then status 200
Given path 'serviceOrder',serviceOrderId
When method delete
Then status 204


Scenario: testCheckServiceOrderWithUnknownSverviceSpecId
Given path 'serviceOrder'
And request data[1]
When method post
Then status 201
And def serviceOrderId = $.id
Given path 'serviceOrder','test',serviceOrderId
And request $
When method put
Then status 201
And match $.state == 'rejected'
And match $.orderItem[0].orderItemMessage ==  '#[1]'
And match $.orderItem[0].orderItemMessage[0] contains { code : '102'  , field : 'serviceSpecification.id' }
Given path 'serviceOrder',serviceOrderId
When method get
Then status 200
Given path 'serviceOrder',serviceOrderId
When method delete
Then status 204


Scenario: testCheckServiceOrderWithGenericCustomer
Given path 'serviceOrder'
And request data[2]
When method post
Then status 201
And def serviceOrderId = $.id
Given path 'serviceOrder','test',serviceOrderId
And request $
When method put
Then status 201
And match $.state == 'inProgress'
Given path 'serviceOrder',serviceOrderId
When method get
Then status 200
Given path 'serviceOrder',serviceOrderId
When method delete
Then status 204

Scenario: testCheckServiceOrderWithoutRelatedParty
Given path 'serviceOrder'
And request data[3]
When method post
Then status 201
And def serviceOrderId = $.id
Given path 'serviceOrder','test',serviceOrderId
And request $
When method put
Then status 201
And match $.state == 'inProgress'
Given path 'serviceOrder',serviceOrderId
When method get
Then status 200
Given path 'serviceOrder',serviceOrderId
When method delete
Then status 204


Scenario: testCheckServiceOrderWithUnKnownCustomer
Given path 'serviceOrder'
And request data[4]
When method post
Then status 201
And def serviceOrderId = $.id
Given path 'serviceOrder','test',serviceOrderId
And request $
When method put
Then status 201
And match $.state == 'inProgress'
Given path 'serviceOrder',serviceOrderId
When method get
Then status 200
Given path 'serviceOrder',serviceOrderId
When method delete
Then status 204

Scenario: testCheckServiceOrderInDeleteWithNoServiceId
Given path 'serviceOrder'
And request data[5]
When method post
Then status 201
And def serviceOrderId = $.id
Given path 'serviceOrder','test',serviceOrderId
And request $
When method put
Then status 201
And match $.state == 'rejected'
And match $.orderItem[0].orderItemMessage ==  '#[1]'
And match $.orderItem[0].orderItemMessage[0] contains { code : '106'  , field : 'service.id' }
Given path 'serviceOrder',serviceOrderId
When method get
Then status 200
Given path 'serviceOrder',serviceOrderId
When method delete
Then status 204


Scenario: testCheckServiceOrderInModifyWithNoServiceId
Given path 'serviceOrder'
And request data[6]
When method post
Then status 201
And def serviceOrderId = $.id
Given path 'serviceOrder','test',serviceOrderId
And request $
When method put
Then status 201
And match $.state == 'rejected'
And match $.orderItem[0].orderItemMessage ==  '#[1]'
And match $.orderItem[0].orderItemMessage[0] contains { code : '106'  , field : 'service.id' }
Given path 'serviceOrder',serviceOrderId
When method get
Then status 200
Given path 'serviceOrder',serviceOrderId
When method delete
Then status 204


Scenario: testCheckServiceOrderInAddWithServiceId
Given path 'serviceOrder'
And request data[7]
When method post
Then status 201
And def serviceOrderId = $.id
Given path 'serviceOrder','test',serviceOrderId
And request $
When method put
Then status 201
And match $.state == 'rejected'
And match $.orderItem[0].orderItemMessage ==  '#[1]'
And match $.orderItem[0].orderItemMessage[0] contains { code : '103'  , field : 'service.id' }
Given path 'serviceOrder',serviceOrderId
When method get
Then status 200
Given path 'serviceOrder',serviceOrderId
When method delete
Then status 204

Scenario: testCheckServiceOrderWithUnKnownCustomerInChange
Given path 'serviceOrder'
And request data[8]
When method post
Then status 201
And def serviceOrderId = $.id
Given path 'serviceOrder','test',serviceOrderId
And request $
When method put
Then status 201
And match $.state == 'rejected'
And match $.orderMessage ==  '#[1]'
And match $.orderMessage[0] contains { code : '104'  , field : 'relatedParty.id' }
Given path 'serviceOrder',serviceOrderId
When method get
Then status 200
Given path 'serviceOrder',serviceOrderId
When method delete
Then status 204


Scenario: testCheckServiceOrderDelete
Given path 'serviceOrder'
And request data[9]
When method post
Then status 201
And def serviceOrderId = $.id
Given path 'serviceOrder','test',serviceOrderId
And request $
When method put
Then status 201
And match $.state == 'inProgress'
Given path 'serviceOrder',serviceOrderId
When method get
Then status 200
Given path 'serviceOrder',serviceOrderId
When method delete
Then status 204

Scenario: testCheckServiceOrderDeleteActionForMacro
Given path 'serviceOrder'
And request data[18]
When method post
Then status 201
And def serviceOrderId = $.id
Given path 'serviceOrder',serviceOrderId
When method get
Then status 200
Given path 'serviceOrder',serviceOrderId
When method delete
Then status 204

Scenario: testCheckServiceOrderDeleteRejected
Given path 'serviceOrder'
And request data[10]
When method post
Then status 201
And def serviceOrderId = $.id
Given path 'serviceOrder','test',serviceOrderId
And request $
When method put
Then status 201
And match $.state == 'rejected'
And match $.orderItem[0].orderItemMessage ==  '#[1]'
And match $.orderItem[0].orderItemMessage[0] contains { code : '106'  , field : 'service.id' }
Given path 'serviceOrder',serviceOrderId
When method get
Then status 200
Given path 'serviceOrder',serviceOrderId
When method delete
Then status 204

Scenario: testCheckServiceOrderNoChange
Given path 'serviceOrder'
And request data[11]
When method post
Then status 201
And def serviceOrderId = $.id
Given path 'serviceOrder','test',serviceOrderId
And request $
When method put
Then status 201
And match $.state == 'completed'
And match $.orderItem[0].state == 'completed'
And match $.orderItem[1].state == 'completed'
Given path 'serviceOrder',serviceOrderId
When method get
Then status 200
Given path 'serviceOrder',serviceOrderId
When method delete
Then status 204

Scenario: testCheckServiceOrderNoChangeAndDelete
Given path 'serviceOrder'
And request data[12]
When method post
Then status 201
And def serviceOrderId = $.id
Given path 'serviceOrder','test',serviceOrderId
And request $
When method put
Then status 201
And match $.state == 'inProgress'
And match $.orderItem[0].state == 'completed'
Given path 'serviceOrder',serviceOrderId
When method get
Then status 200
Given path 'serviceOrder',serviceOrderId
When method delete
Then status 204

Scenario: testCheckServiceOrderDeleteWithKoServiceSpecId
Given path 'serviceOrder'
And request data[13]
When method post
Then status 201
And def serviceOrderId = $.id
Given path 'serviceOrder','test',serviceOrderId
And request $
When method put
Then status 201
And match $.state == 'rejected'
And match $.orderItem[0].state == 'rejected'
Given path 'serviceOrder',serviceOrderId
When method get
Then status 200
Given path 'serviceOrder',serviceOrderId
When method delete
Then status 204

Scenario: testCheckServiceOrderRejected
Given path 'serviceOrder'
And request data[14]
When method post
Then status 201
And def serviceOrderId = $.id
Given path 'serviceOrder','test',serviceOrderId
And request $
When method put
Then status 201
And match $.state == 'rejected'
And match $.orderItem[0].orderItemMessage ==  '#[1]'
And match $.orderItem[0].orderItemMessage[0] contains { code : '102'  , field : 'serviceSpecification.id' }
Given path 'serviceOrder',serviceOrderId
When method get
Then status 200
Given path 'serviceOrder',serviceOrderId
When method delete
Then status 204


Scenario: testFindAndGetServiceOrder
Given path 'serviceOrder'
And request data[15]
When method post
Then status 201
And def serviceOrderId15 = $.id
Given path 'serviceOrder'
And request data[16]
When method post
Then status 201
And def serviceOrderId16 = $.id
Given path 'serviceOrder','test',serviceOrderId15
And request $
When method put
Then status 201
Given path 'serviceOrder','test',serviceOrderId16
And request $
When method put
Then status 201
Given path 'serviceOrder'
And params {fields : 'id'}
When method get
Then status 200
And match $ == '#[2]'
Given path 'serviceOrder'
And params {externalId : 'extid1' , state : 'inProgress'}
When method get
Then status 200
And print response
And match $ == '#[1]'
Given path 'serviceOrder',serviceOrderId15
When method get
Then status 200
And match $ contains '#notnull'
Given path 'serviceOrder',serviceOrderId15
When method delete
Then status 204
Given path 'serviceOrder',serviceOrderId16
When method delete
Then status 204

Scenario: testCheckServiceOrderWithTargetHeader
Given path 'serviceOrder'
And header Target = targetHeader
And request data[0]
When method post
Then status 201
And match $.id contains '#notnull'
And match $.state == 'acknowledged'
And def serviceOrderId = $.id
Given path 'serviceOrder',serviceOrderId
And header Target = targetHeader
When method get
Then status 200

Scenario: testCheckServiceOrderWithCustomerAAINotResponding
* call Context.removeWireMockMapping("/aai/v14/business/customers/customer/new");
Given path 'serviceOrder'
And request data[4]
When method post
Then status 201
And def serviceOrderId = $.id
Given path 'serviceOrder','test',serviceOrderId
And request $
When method put
Then status 201
And match $.id == serviceOrderId
And match $.state == 'rejected'
And match $.orderItem == '#[2]'
And match $.orderMessage[0] contains { code : '501'  , messageInformation : 'Problem with AAI API' }
Given path 'serviceOrder',serviceOrderId
When method get
Then status 200
* call Context.startServers();


Scenario: testAAIPutServiceNotResponding
* call Context.removeWireMockMapping("/aai/v14/business/customers/customer/new/service-subscriptions/service-subscription/vFW");
Given path 'serviceOrder'
And request data[4]
When method post
Then status 201
And def serviceOrderId = $.id
Given path 'serviceOrder','test',serviceOrderId
And request $
When method put
Then status 201
And match $.id == serviceOrderId
And match $.state == 'rejected'
Given path 'serviceOrder',serviceOrderId
When method get
Then status 200
* call Context.startServers();



Scenario: testCheckServiceOrderWithSDCNotResponding
* call Context.removeWireMockMapping("/sdc/v1/catalog/services/1e3feeb0-8e36-46c6-862c-236d9c626439/metadata");
Given path 'serviceOrder'
And request data[4]
When method post
Then status 201
And def serviceOrderId = $.id
Given path 'serviceOrder','test',serviceOrderId
And request $
When method put
Then status 201
And match $.id == serviceOrderId
And match $.state == 'rejected'
And match $.orderItem[0].orderMessage[0] contains { code : '102'  , field : 'serviceSpecification.id' }
Given path 'serviceOrder',serviceOrderId
When method get
Then status 200
* call Context.startServers();

Scenario: testCheckServiceOrderWithSDCNotRespondingWithoutWiremock
* call Context.stopWiremock();
Given path 'serviceOrder'
And request data[4]
When method post
Then status 201
And def serviceOrderId = $.id
Given path 'serviceOrder','test',serviceOrderId
And request $
When method put
Then status 201
And match $.id == serviceOrderId
And match $.state == 'rejected'
And match $.orderMessage[0] contains { code : '500'  , messageInformation : 'Problem with SDC API' }
Given path 'serviceOrder',serviceOrderId
When method get
Then status 200
* call Context.startServers();

Scenario: testCheckServiceOrderNoOwningEntities
* call Context.removeWireMockMapping("/aai/v14/business/owning-entities");
Given path 'serviceOrder'
And request data[9]
When method post
Then status 201
And def serviceOrderId = $.id
Given path 'serviceOrder','test',serviceOrderId
And request $
When method put
Then status 201
And match $.state == 'inProgress'
Given path 'serviceOrder',serviceOrderId
When method get
Then status 200
Given path 'serviceOrder',serviceOrderId
When method delete
Then status 204
* call Context.startServers();
