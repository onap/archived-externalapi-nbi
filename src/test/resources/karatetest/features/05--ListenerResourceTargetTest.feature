# new feature
# Tags: optional

Feature: Listener

Background:
* url nbiBaseUrl
* def Context = Java.type('org.onap.nbi.test.Context');
* call Context.startServers();
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

Scenario: testPostListenerResource
* def listenerUrl = nbiBaseUrl + "/test/listener"
Given path 'test/listener'
When method delete
Then status 204
Given path 'hub'
And header Target = 'http://127.0.0.1:8080'
And request { callback : '#(listenerUrl)' , query : 'eventType = ServiceOrderCreationNotification' }
When method post
Then status 201
And def location = responseHeaders['Location'][0]
And def hubId = $.id
Given url nbiBaseUrl
Given path 'hub',hubId
And header Target = 'http://localhost:8080'
When method get
Then status 200
And match hubId == $.id
Given path 'serviceOrder'
And request serviceOrderData[17]
And header Target = 'http://localhost:8080'
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
And header Target = 'http://localhost:8080'
When method delete
Then status 204
Given path 'test/listener',eventId
When method delete
Then status 204
Given path 'serviceOrder',serviceOrderId
And header Target = 'http://localhost:8080'
When method delete
Then status 204
