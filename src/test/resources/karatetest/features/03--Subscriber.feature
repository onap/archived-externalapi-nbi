# new feature
# Tags: optional
    
Feature: Subscriber

Background:
* url nbiBaseUrl
* def Context = Java.type('org.onap.nbi.test.Context');
* call Context.startServers();
* def data = read('../data/subscriber.json')
* def serviceOrderData = read('../data/serviceOrder.json')
* def checkDateFormat =
"""
function(s) {
  var SimpleDateFormat = Java.type("java.text.SimpleDateFormat");
  var sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
  return sdf.parse(s);
}
"""

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

Scenario: testSubscriberWithTestListener
* def listenerUrl = nbiBaseUrl + "/test/listener"
Given path 'test/listener'
When method delete
Then status 204
Given path 'hub'
And request { id : 'id', callback : '#(listenerUrl)' , query : 'eventType = ServiceOrderCreationNotification' }
When method post
Then status 201
Given path 'hub'
When method get
And def hubId = $[0].id
Given path 'serviceOrder'
And request serviceOrderData[0]
When method post
Then status 201
Given path 'test/listener'
When method get
Then status 200
And assert response.length == 1
And match $[0] contains { eventId : '#notnull' , eventType : 'ServiceOrderCreationNotification' , eventDate : '#notnull' , event :'#notnull'}
And def eventId = $[0].eventId
And def eventDate = $[0].eventDate
And call checkDateFormat(eventDate)
Given path 'hub',hubId
When method delete
Then status 204
Given path 'test/listener',eventId
When method delete
Then status 204