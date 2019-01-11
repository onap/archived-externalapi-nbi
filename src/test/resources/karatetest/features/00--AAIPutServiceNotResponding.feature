# new feature
# Tags: optional
    
Feature: AAI With Put Service Not responding

Background:
* url nbiBaseUrl
* def data = read('../data/serviceOrder.json')
* def Context = Java.type('org.onap.nbi.test.Context');
* call Context.startServers();
* call Context.removeWireMockMapping("/aai/v11/business/customers/customer/new/service-subscriptions/service-subscription/vFW");
    
Scenario: Test Put Service

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