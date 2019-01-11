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
Given path 'serviceOrder','test'
When method delete
Then status 204

Scenario: testCheckServiceOrder
Given path 'serviceOrder'
And request data[0]
When method post
Then status 201
Given path 'serviceOrder','test','test'
And request $
When method put
Then status 201
And match $.id == 'test'
And match $.state == 'acknowledged'
Given path 'serviceOrder','test'
When method get
Then status 200
Given path 'serviceOrder','test'
When method delete
Then status 204


Scenario: testCheckServiceOrderWithUnknownSverviceSpecId
Given path 'serviceOrder'
And request data[1]
When method post
Then status 201
Given path 'serviceOrder','test','test1'
And request $
When method put
Then status 201
And match $.state == 'rejected'
And match $.orderItem[0].orderItemMessage ==  '#[1]'
And match $.orderItem[0].orderItemMessage[0] contains { code : '102'  , field : 'serviceSpecification.id' }
Given path 'serviceOrder','test1'
When method get
Then status 200
Given path 'serviceOrder','test1'
When method delete
Then status 204


Scenario: testCheckServiceOrderWithGenericCustomer
Given path 'serviceOrder'
And request data[2]
When method post
Then status 201
Given path 'serviceOrder','test','test2'
And request $
When method put
Then status 201
And match $.state == 'acknowledged'
Given path 'serviceOrder','test2'
When method get
Then status 200
Given path 'serviceOrder','test2'
When method delete
Then status 204

Scenario: testCheckServiceOrderWithoutRelatedParty
Given path 'serviceOrder'
And request data[3]
When method post
Then status 201
Given path 'serviceOrder','test','test3'
And request $
When method put
Then status 201
And match $.state == 'acknowledged'
Given path 'serviceOrder','test3'
When method get
Then status 200
Given path 'serviceOrder','test3'
When method delete
Then status 204


Scenario: testCheckServiceOrderWithUnKnownCustomer
Given path 'serviceOrder'
And request data[4]
When method post
Then status 201
Given path 'serviceOrder','test','test4'
And request $
When method put
Then status 201
And match $.state == 'acknowledged'
Given path 'serviceOrder','test4'
When method get
Then status 200
Given path 'serviceOrder','test4'
When method delete
Then status 204

Scenario: testCheckServiceOrderInDeleteWithNoServiceId
Given path 'serviceOrder'
And request data[5]
When method post
Then status 201
Given path 'serviceOrder','test','test5'
And request $
When method put
Then status 201
And match $.state == 'rejected'
And match $.orderItem[0].orderItemMessage ==  '#[1]'
And match $.orderItem[0].orderItemMessage[0] contains { code : '106'  , field : 'service.id' }
Given path 'serviceOrder','test5'
When method get
Then status 200
Given url 'http://localhost:8080/nbi/api/v3/serviceOrder/test5'
When method delete
Then status 204


Scenario: testCheckServiceOrderInModifyWithNoServiceId
Given path 'serviceOrder'
And request data[6]
When method post
Then status 201
Given path 'serviceOrder','test','test6'
And request $
When method put
Then status 201
And match $.state == 'rejected'
And match $.orderItem[0].orderItemMessage ==  '#[1]'
And match $.orderItem[0].orderItemMessage[0] contains { code : '106'  , field : 'service.id' }
Given path 'serviceOrder','test6'
When method get
Then status 200
Given path 'serviceOrder','test6'
When method delete
Then status 204


Scenario: testCheckServiceOrderInAddWithServiceId
Given path 'serviceOrder'
And request data[7]
When method post
Then status 201
Given path 'serviceOrder','test','test7'
And request $
When method put
Then status 201
And match $.state == 'rejected'
And match $.orderItem[0].orderItemMessage ==  '#[1]'
And match $.orderItem[0].orderItemMessage[0] contains { code : '103'  , field : 'service.id' }
Given path 'serviceOrder','test7'
When method get
Then status 200
Given path 'serviceOrder','test7'
When method delete
Then status 204

Scenario: testCheckServiceOrderWithUnKnownCustomerInChange
Given path 'serviceOrder'
And request data[8]
When method post
Then status 201
Given path 'serviceOrder','test','test8'
And request $
When method put
Then status 201
And match $.state == 'rejected'
And match $.orderMessage ==  '#[1]'
And match $.orderMessage[0] contains { code : '104'  , field : 'relatedParty.id' }
Given path 'serviceOrder','test8'
When method get
Then status 200
Given path 'serviceOrder','test8'
When method delete
Then status 204


Scenario: testCheckServiceOrderDelete
Given path 'serviceOrder'
And request data[9]
When method post
Then status 201
Given path 'serviceOrder','test','test9'
And request $
When method put
Then status 201
And match $.state == 'acknowledged'
Given path 'serviceOrder','test9'
When method get
Then status 200
Given path 'serviceOrder','test9'
When method delete
Then status 204

Scenario: testCheckServiceOrderDeleteRejected
Given path 'serviceOrder'
And request data[10]
When method post
Then status 201
Given path 'serviceOrder','test','test10'
And request $
When method put
Then status 201
And match $.state == 'rejected'
And match $.orderItem[0].orderItemMessage ==  '#[1]'
And match $.orderItem[0].orderItemMessage[0] contains { code : '106'  , field : 'service.id' }
Given path 'serviceOrder','test10'
When method get
Then status 200
Given path 'serviceOrder','test10'
When method delete
Then status 204

Scenario: testCheckServiceOrderNoChange
Given path 'serviceOrder'
And request data[11]
When method post
Then status 201
Given path 'serviceOrder','test','test11'
And request $
When method put
Then status 201
And match $.state == 'completed'
And match $.orderItem[0].state == 'completed'
And match $.orderItem[1].state == 'completed'
Given path 'serviceOrder','test11'
When method get
Then status 200
Given path 'serviceOrder','test11'
When method delete
Then status 204

Scenario: testCheckServiceOrderNoChangeAndDelete
Given path 'serviceOrder'
And request data[12]
When method post
Then status 201
Given path 'serviceOrder','test','test12'
And request $
When method put
Then status 201
And match $.state == 'acknowledged'
And match $.orderItem[0].state == 'completed'
Given path 'serviceOrder','test12'
When method get
Then status 200
Given path 'serviceOrder','test12'
When method delete
Then status 204

Scenario: testCheckServiceOrderDeleteWithKoServiceSpecId
Given path 'serviceOrder'
And request data[13]
When method post
Then status 201
Given path 'serviceOrder','test','test13'
And request $
When method put
Then status 201
And match $.state == 'rejected'
And match $.orderItem[0].state == 'rejected'
Given path 'serviceOrder','test13'
When method get
Then status 200
Given path 'serviceOrder','test13'
When method delete
Then status 204

Scenario: testCheckServiceOrderRejected
Given path 'serviceOrder'
And request data[14]
When method post
Then status 201
Given path 'serviceOrder','test','test14'
And request $
When method put
Then status 201
And match $.state == 'rejected'
And match $.orderItem[0].orderItemMessage ==  '#[1]'
And match $.orderItem[0].orderItemMessage[0] contains { code : '102'  , field : 'serviceSpecification.id' }
Given path 'serviceOrder','test14'
When method get
Then status 200
Given path 'serviceOrder','test14'
When method delete
Then status 204


Scenario: testFindAndGetServiceOrder
Given path 'serviceOrder'
And request data[15]
When method post
Then status 201
Given path 'serviceOrder'
And request data[16]
When method post
Then status 201
Given path 'serviceOrder','test','test15'
And request $
When method put
Then status 201
Given path 'serviceOrder','test','test16'
And request $
When method put
Then status 201
Given path 'serviceOrder'
And params {fields : 'id'}
When method get
Then status 200
And match $ == '#[2]'
Given path 'serviceOrder'
And params {externalId : 'extid1' , state : 'acknowledged'}
When method get
Then status 200
And match $ == '#[1]'
Given path 'serviceOrder','test15'
When method get
Then status 200
And match $ contains '#notnull'
Given path 'serviceOrder','test15'
When method delete
Then status 204
Given path 'serviceOrder','test16'
When method delete
Then status 204


Scenario: testCheckServiceOrderWithCustomerAAINotResponding
* call Context.removeWireMockMapping("/aai/v11/business/customers/customer/new");
Given path 'serviceOrder'
And request data[4]
When method post
Then status 201
Given path 'serviceOrder','test','test4'
And request $
When method put
Then status 201
And match $.id == 'test4'
And match $.state == 'rejected'
And match $.orderItem == '#[2]'
And match $.orderMessage[0] contains { code : '501'  , messageInformation : 'Problem with AAI API' }
Given path 'serviceOrder','test4'
When method get
Then status 200
* call Context.startServers();


Scenario: testAAIPutServiceNotResponding
* call Context.removeWireMockMapping("/aai/v11/business/customers/customer/new/service-subscriptions/service-subscription/vFW");
Given path 'serviceOrder'
And request data[4]
When method post
Then status 201
Given path 'serviceOrder','test','test4'
And request $
When method put
Then status 201
And match $.id == 'test4'
And match $.state == 'rejected'
Given path 'serviceOrder','test4'
When method get
Then status 200
* call Context.startServers();

Scenario: testCheckServiceOrderWithSDCNotResponding
* call Context.removeWireMockMapping("/sdc/v1/catalog/services/1e3feeb0-8e36-46c6-862c-236d9c626439/metadata");
Given path 'serviceOrder'
And request data[4]
When method post
Then status 201
Given path 'serviceOrder','test','test4'
And request $
When method put
Then status 201
And match $.id == 'test4'
And match $.state == 'rejected'
And match $.orderItem[0].orderMessage[0] contains { code : '102'  , field : 'serviceSpecification.id' }
Given path 'serviceOrder','test4'
When method get
Then status 200
* call Context.startServers();