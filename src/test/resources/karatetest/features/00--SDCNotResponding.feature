# new feature
# Tags: optional
    
Feature: SDC Not Responding

Background:

* url nbiBaseUrl
* def Context = Java.type('org.onap.nbi.test.Context');
* call Context.startServers();
* call Context.removeWireMockMapping("/sdc/v1/catalog/services/1e3feeb0-8e36-46c6-862c-236d9c626439/metadata");
* def data = read('../data/serviceOrder.json')

    
Scenario: testCheckServiceOrderWithSDCNotResponding

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