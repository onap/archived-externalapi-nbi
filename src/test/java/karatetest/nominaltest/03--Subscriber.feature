# new feature
# Tags: optional
    
Feature: Subscriber

Background:
* url demoBaseUrl
* def data = read('../data/subscriber.json')
    
Scenario: testFindWhenNoSubscriber
Given path 'hub'
When method get
Then status 200
And match $ == '#[0]'

Scenario: testCreationAndFindSubscriber
Given path 'hub'
And request data[0]
When method post
Then status 201
Given url 'http://localhost:8080/nbi/api/v3/hub/'
When method get
And match $ == '#[1]'

Scenario: testGetByIdSubscriber
Given path 'hub'
When method get
And def Id = $[0].id
Given path 'hub',Id
When method get
And match $ contains { callback : 'http://localhost:8080/test' , query : 'eventType=ServiceOrderCreationNotification' }

Scenario: testFindSubscriber
Given path 'hub'
And request data[1]
When method post
Then status 201
Given path 'hub'
And request data[2]
When method post
Then status 201
Given path 'hub'
When method get
Then status 200
And match $ == '#notnull'

Scenario: testFindWithFilteringSubscriber
Given path 'hub'
And params { query.eventType : 'ServiceOrderCreationNotification' }
When method get
Then status 200
And match $ == '#[1]'

Scenario: testSubscriberDeletion
Given path 'hub'
And request { id : 'id', callback : 'http://localhost:8080/test' , query : 'eventType = ServiceOrderCreationNotification' }
When method post
Then status 201
Given path 'hub'
When method get
And def Id = $[0].id
Given path 'hub',Id
When method delete
Then status 204
