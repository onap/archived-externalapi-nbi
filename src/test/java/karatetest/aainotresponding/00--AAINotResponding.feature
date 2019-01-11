# new feature
# Tags: optional
    
Feature: AAI Not Responding

Background:
* url demoBaseUrl
* def data = read('../data/serviceOrder.json')

    
Scenario: testCheckServiceOrderWithCustomerAAINotResponding
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



