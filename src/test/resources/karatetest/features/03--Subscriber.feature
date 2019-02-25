# new feature
# Tags: optional
    
Feature: Subscriber

Background:
* url nbiBaseUrl
* def Context = Java.type('org.onap.nbi.test.Context');
* call Context.startServers();
* def data = read('../data/subscriber.json')
* def serviceOrderData = read('../data/serviceOrder.json')
* configure retry = { count: 10, interval: 500 }
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
And def location = responseHeaders['Location'][0]
Given path 'hub'
When method get
And match $ == '#[1]'
Given url location
When method delete
Then status 204

Scenario: testCreation2SameSubscribers
Given path 'hub'
And request data[0]
When method post
Then status 201
And def location = responseHeaders['Location'][0]
Given path 'hub'
And request data[0]
When method post
Then status 400
And match $ contains { message : 'Bad Request'}
Given path 'hub'
When method get
And match $ == '#[1]'
Given url location
When method delete
Then status 204

Scenario: testGetByIdSubscriber
Given path 'hub'
And request data[0]
When method post
Then status 201
Given path 'hub'
When method get
And def Id = $[0].id
Given path 'hub',Id
When method get
And match $ contains { callback : 'http://localhost:8080/test' , query : 'eventType=ServiceOrderCreationNotification' }
Given path 'hub',Id
When method delete
Then status 204

Scenario: testFindSubscriber
Given path 'hub'
And request data[1]
When method post
Then status 201
And def location1 = responseHeaders['Location'][0]
Given path 'hub'
And request data[2]
When method post
Then status 201
And def location2 = responseHeaders['Location'][0]
Given path 'hub'
When method get
Then status 200
And match $ == '#notnull'
Given url location1
When method delete
Then status 204
Given url location2
When method delete
Then status 204

Scenario: testFindWithFilteringSubscriber
Given path 'hub'
And request data[0]
When method post
Then status 201
And def location = responseHeaders['Location'][0]
Given path 'hub'
And params { query.eventType : 'ServiceOrderCreationNotification' }
When method get
Then status 200
And match $ == '#[1]'
Given url location
When method delete
Then status 204

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
And request serviceOrderData[17]
When method post
Then status 201
And def serviceOrderId = $.id
Given path 'test/listener'
And params {serviceOrderId : '#(serviceOrderId)'}
And retry until responseStatus == 200
When method get
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
Given path 'serviceOrder',serviceOrderId
When method delete
Then status 204


Scenario: testSubscriberWithTestListenerForServiceInventorCreationEvents
* def listenerUrl = nbiBaseUrl + "/test/listener"
Given path 'test/listener'
When method delete
Then status 204
Given path 'hub'
And request { id : 'id', callback : '#(listenerUrl)' , query : 'eventType = ServiceCreationNotification' }
When method post
Then status 201
Given path 'hub'
When method get
And def hubId = $[0].id
Given path 'hub/testaaievents'
Then status 200
When method get
Given path 'test/listener'
And params {serviceInstanceId : 'new-test5'}
And retry until responseStatus == 200
When method get
And assert response.length == 1
And match $[0] contains { eventId : '#notnull' , eventType : 'ServiceCreationNotification' , eventDate : '#notnull' , event :'#notnull'}
And def eventId = $[0].eventId
And def eventDate = $[0].eventDate
And call checkDateFormat(eventDate)
Given path 'hub',hubId
When method delete
Then status 204
Given path 'test/listener',eventId
When method delete
Then status 204

Scenario: testSubscriberWithTestListenerForServiceInventoryUpdateEvents
* def listenerUrl = nbiBaseUrl + "/test/listener"
Given path 'test/listener'
When method delete
Then status 204
Given path 'hub'
And request { id : 'id', callback : '#(listenerUrl)' , query : 'eventType = ServiceAttributeValueChangeNotification' }
When method post
Then status 201
Given path 'hub'
When method get
And def hubId = $[0].id
Given path 'hub/testaaievents'
Then status 200
When method get
Given path 'test/listener'
And params {serviceInstanceId : 'new-test5'}
And retry until responseStatus == 200
When method get
And assert response.length == 1
And match $[0] contains { eventId : '#notnull' , eventType : 'ServiceAttributeValueChangeNotification' , eventDate : '#notnull' , event :'#notnull'}
And def eventId = $[0].eventId
And def eventDate = $[0].eventDate
And call checkDateFormat(eventDate)
Given path 'hub',hubId
When method delete
Then status 204
Given path 'test/listener',eventId
When method delete
Then status 204

Scenario: testSubscriberWithTestListenerForServiceInventoryRemoveEvents
* def listenerUrl = nbiBaseUrl + "/test/listener"
Given path 'test/listener'
When method delete
Then status 204
Given path 'hub'
And request { id : 'id', callback : '#(listenerUrl)' , query : 'eventType = ServiceRemoveNotification' }
When method post
Then status 201
Given path 'hub'
When method get
And def hubId = $[0].id
Given path 'hub/testaaievents'
Then status 200
When method get
Given path 'test/listener'
And params {serviceInstanceId : 'new-test5'}
And retry until responseStatus == 200
When method get
And assert response.length == 1
And match $[0] contains { eventId : '#notnull' , eventType : 'ServiceRemoveNotification' , eventDate : '#notnull' , event :'#notnull'}
And def eventId = $[0].eventId
And def eventDate = $[0].eventDate
And call checkDateFormat(eventDate)
Given path 'hub',hubId
When method delete
Then status 204
Given path 'test/listener',eventId
When method delete
Then status 204