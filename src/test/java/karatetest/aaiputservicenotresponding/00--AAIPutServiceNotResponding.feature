# new feature
# Tags: optional
    
Feature: AAI With Put Service Not responding

Background:
* url demoBaseUrl
* def data = read('../data/serviceOrder.json')

    
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