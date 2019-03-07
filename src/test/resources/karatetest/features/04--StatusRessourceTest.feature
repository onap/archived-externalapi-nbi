# new feature
# Tags: optional
    
Feature: Status Resource

Background:
* url nbiBaseUrl
* def Context = Java.type('org.onap.nbi.test.Context');
* call Context.startServers();
    
Scenario: testHealthCheck
Given path 'status'
When method get
Then status 200
And match response.status == 'ok'
And match response.name == 'nbi'
And match each $.components contains {status :'ok'}

Scenario: testHealthChecWithoutWiremock
* call Context.stopWiremock();
Given path 'status'
When method get
Then status 200
And match response.status == 'ok'
And match response.name == 'nbi'
And match each response.components contains {  status: 'ko'}
* call Context.startServers();

Scenario: testHealthCheckWithSDCNotResponding
* call Context.removeWireMockMapping("/sdc/v1/artifactTypes");
Given path 'status'
When method get
Then status 200
And match response.status == 'ok'
And match response.name == 'nbi'
And match response.components contains [{name:'sdc connectivity', status: 'ko'},{name:'so connectivity', status: 'ok'},{name:'aai connectivity', status: 'ok'},{name:'dmaap connectivity', status: 'ok'},]
* call Context.startServers();
