#
#     Copyright (c) 2018 Orange
#
#     Licensed under the Apache License, Version 2.0 (the "License");
#     you may not use this file except in compliance with the License.
#     You may obtain a copy of the License at
#
#         http://www.apache.org/licenses/LICENSE-2.0
#
#     Unless required by applicable law or agreed to in writing, software
#     distributed under the License is distributed on an "AS IS" BASIS,
#     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#     See the License for the specific language governing permissions and
#     limitations under the License.
#

# new feature
# Tags: optional
    
Feature: Status Resource

Background:
* url nbiBaseUrl
* def Context = Java.type('org.onap.nbi.test.Context');
* configure readTimeout = 10000
* call Context.startServers();
    
Scenario: testHealthCheck
Given path 'status'
When method get
Then status 200
And match response.status == 'ok'
And match response.name == 'nbi'

Scenario: testFullHealthCheck
Given path 'status'
And params {fullStatus :true}
When method get
Then status 200
And match response.status == 'ok'
And match response.name == 'nbi'
And assert response.components.length == 4
And match each $.components contains {status :'ok'}


Scenario: testFullHealthChecWithoutWiremock
* call Context.stopWiremock();
Given path 'status'
And params {fullStatus :true}
When method get
Then status 200
And match response.status == 'ok'
And match response.name == 'nbi'
And assert response.components.length == 4
And match each response.components contains {  status: 'ko'}

Scenario: testFullHealthCheckWithSDCNotResponding
* call Context.removeWireMockMapping("/sdc2/rest/healthCheck");
Given path 'status'
And params {fullStatus :true}
When method get
Then status 200
And match response.status == 'ok'
And match response.name == 'nbi'
And assert response.components.length == 4
And match response.components contains [{name:'sdc connectivity', status: 'ko'},{name:'so connectivity', status: 'ok'},{name:'aai connectivity', status: 'ok'},{name:'dmaap connectivity', status: 'ok'},]
