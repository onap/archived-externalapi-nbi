# API Listener


<a name="overview"></a>
## Overview

### Api URL

[Swagger UI](https://api-designer.sso.infra.ftgroup/swagger-ui/?url=https://api-designer.sso.infra.ftgroup/api/1.0/apis/aoG0EJ01Pv/swagger.json)


[plant UML UI](https://plantuml.rd.francetelecom.fr/proxy?fmt=svg&src=https://api-designer.sso.infra.ftgroup/api/1.0/apis/aoG0EJ01Pv/plantuml&noCache=7322.0)

Listener API has to be implemented on the client side in order to receive notification.
Notification are received if HUB has been posted on server side.


### Version information
*Version* : 0.1.0_inProgress


### URI scheme
*Host* : serverRoot  
*BasePath* : /externalapi/listener/v1  
*Schemes* : HTTPS


### Tags

* Listener


### Produces

* `application/json;charset=utf-8`


<a name="paths"></a>
## Resources

<a name="listener_resource"></a>
### Listener

<a name="listenercreate"></a>
#### createEvent
```
POST /listener
```


##### Description
The create event is used by the seller to trigger (POST) a notification to the buyer. The buyer has previously subscribed to receive notification

Specific business errors for current operation will be encapsulated in

HTTP Response 422 Unprocessable entity


##### Parameters

|Type|Name|Schema|
|---|---|---|
|**Body**|**event**  <br>*required*|[Listener](#listener)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**201**|Success|[Listener](#listener)|
|**400**|Bad Request<br><br>List of supported error codes:<br>- 20: Invalid URL parameter value<br>- 21: Missing body<br>- 22: Invalid body<br>- 23: Missing body field<br>- 24: Invalid body field<br>- 25: Missing header<br>- 26: Invalid header value<br>- 27: Missing query-string parameter<br>- 28: Invalid query-string parameter value|[ErrorRepresentation](#errorrepresentation)|
|**401**|Unauthorized<br><br>List of supported error codes:<br>- 40: Missing credentials<br>- 41: Invalid credentials<br>- 42: Expired credentials|[ErrorRepresentation](#errorrepresentation)|
|**403**|Forbidden<br><br>List of supported error codes:<br>- 50: Access denied<br>- 51: Forbidden requester<br>- 52: Forbidden user<br>- 53: Too many requests|[ErrorRepresentation](#errorrepresentation)|
|**422**|Unprocessable entity<br><br>Functional error|[ErrorRepresentation](#errorrepresentation)|
|**500**|Internal Server Error<br><br>List of supported error codes:<br>- 1: Internal error|[ErrorRepresentation](#errorrepresentation)|
|**503**|Service Unavailable<br><br>List of supported error codes:<br>- 5: The service is temporarily unavailable<br>- 6: Orange API is over capacity, retry later !|[ErrorRepresentation](#errorrepresentation)|


##### Consumes

* `application/json;charset=utf-8`


##### Produces

* `application/json;charset=utf-8`


<a name="definitions"></a>
## Definitions

<a name="errorrepresentation"></a>
### ErrorRepresentation

|Name|Schema|
|---|---|
|**@schemaLocation**  <br>*optional*|string|
|**@type**  <br>*optional*|string|
|**code**  <br>*required*|integer (int32)|
|**message**  <br>*optional*|string|
|**reason**  <br>*optional*|string|
|**referenceError**  <br>*optional*|string|
|**status**  <br>*optional*|integer (int32)|


<a name="eventtype"></a>
### EventType
*Type* : enum (ServiceOrderCreationNotification, ServiceOrderStateChangeNotification, ServiceOrderItemStateChangeNotification)


<a name="listener"></a>
### Listener
An event will be triggered for each time a notification is send to a listener.


|Name|Description|Schema|
|---|---|---|
|**event**  <br>*required*|An event representation is the payload of information send with the notification; it will feature event attributes + summary view of the resource.|object|
|**eventDate**  <br>*required*||string (date-time)|
|**eventId**  <br>*required*|id of the event|string|
|**eventType**  <br>*required*||[EventType](#eventtype)|

