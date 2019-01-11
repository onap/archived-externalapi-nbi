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