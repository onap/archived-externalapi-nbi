# API ServiceOrder


<a name="overview"></a>
## Overview

### Api URL

[Swagger UI](https://api-designer.sso.infra.ftgroup/swagger-ui/?url=https://api-designer.sso.infra.ftgroup/api/1.0/apis/kl1kgvz1zR/swagger.json)


[plant UML UI](https://plantuml.rd.francetelecom.fr/proxy?fmt=svg&src=https://api-designer.sso.infra.ftgroup/api/1.0/apis/kl1kgvz1zR/plantuml&noCache=934804.0)

serviceOrder API designed for ONAP Beijing Release.
This API is build from TMF open API18.0 (applying TMF Guideline 3.0);
Only operations GET (by id and list) and POST are available.


### Version information
*Version* : 1.0.0_inProgress


### URI scheme
*Host* : serverRoot  
*BasePath* : /nbi/api/v1  
*Schemes* : HTTPS


### Tags

* ServiceOrder : A Service Order is a type of order which can be used to describe a group of operations on service – one service order item per service. An action at the level of the service order item describe the operation to be done on a service (add, terminate for example). The service order is triggered from the BSS system in charge of the product order management to ONAP that will manage the service fulfillment.


### Consumes

* `application/json;charset=utf-8`


### Produces

* `application/json;charset=utf-8`


<a name="paths"></a>
## Resources

<a name="serviceorder_resource"></a>
### ServiceOrder
A Service Order is a type of order which can be used to describe a group of operations on service – one service order item per service. An action at the level of the service order item describe the operation to be done on a service (add, terminate for example). The service order is triggered from the BSS system in charge of the product order management to ONAP that will manage the service fulfillment.


<a name="serviceordercreate"></a>
#### Create a service order
```
POST /serviceOrder
```


##### Description
This operation creates a service order entity.
The TMF Open API specification document provides the list of mandatory and non mandatory attributes when creating a ServiceOrder, including any possible rule conditions and applicable default values.
POST should be used without specifying the id and the href, the Service Order Management system is in charge of generating the id + href for the ServiceOrder.

Specific business errors for current operation will be encapsulated in

HTTP Response 422 Unprocessable entity

 - 100: OrderItem with 'add' action but serviceSpecification id missing
   
 - 101: OrderItem with 'change'/'noChange'/'remove' but service id missing
   
 - 102: OrderItem with 'add' action - serviceSpecification id provided but not existing
   
 - 103: OrderItem with 'add' action but service id already existing in the inventory
   
 - 104: A customer for existing service(s) is provided but he did not exist
   
 - 105: OrderItem with 'change'/'noChange'/'remove' - Service id provided but it is not existing in the inventory
   
 - 106: [Not managed for current Relese] Issue with lcpCloudRegionId  and tenantId provided


##### Parameters

|Type|Name|Schema|
|---|---|---|
|**Body**|**serviceOrder**  <br>*required*|[CreateServiceOrder](#createserviceorder)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**201**|Success|[CreateServiceOrder](#createserviceorder)|
|**400**|Bad Request<br><br>List of supported error codes:<br>- 20: Invalid URL parameter value<br>- 21: Missing body<br>- 22: Invalid body<br>- 23: Missing body field<br>- 24: Invalid body field<br>- 25: Missing header<br>- 26: Invalid header value<br>- 27: Missing query-string parameter<br>- 28: Invalid query-string parameter value|[ErrorRepresentation](#errorrepresentation)|
|**401**|Unauthorized<br><br>List of supported error codes:<br>- 40: Missing credentials<br>- 41: Invalid credentials<br>- 42: Expired credentials|[ErrorRepresentation](#errorrepresentation)|
|**403**|Forbidden<br><br>List of supported error codes:<br>- 50: Access denied<br>- 51: Forbidden requester<br>- 52: Forbidden user<br>- 53: Too many requests|[ErrorRepresentation](#errorrepresentation)|
|**404**|Not Found<br><br>List of supported error codes:<br>- 60: Resource not found|[ErrorRepresentation](#errorrepresentation)|
|**422**|Unprocessable entity<br><br>Functional error<br><br>Specific encapsulated business errors for current operation<br><br> - 100: OrderItem with 'add' action but serviceSpecification id missing<br>   <br> - 101: OrderItem with 'change'/'noChange'/'remove' but service id missing<br>   <br> - 102: OrderItem with 'add' action - serviceSpecification id provided but not existing<br>   <br> - 103: OrderItem with 'add' action but service id already existing in the inventory<br>   <br> - 104: A customer for existing service(s) is provided but he did not exist<br>   <br> - 105: OrderItem with 'change'/'noChange'/'remove' - Service id provided but it is not existing in the inventory<br>   <br> - 106: [Not managed for current Relese] Issue with lcpCloudRegionId  and tenantId provided|[ErrorRepresentation](#errorrepresentation)|
|**500**|Internal Server Error<br><br>List of supported error codes:<br>- 1: Internal error|[ErrorRepresentation](#errorrepresentation)|
|**503**|Service Unavailable<br><br>List of supported error codes:<br>- 5: The service is temporarily unavailable<br>- 6: Orange API is over capacity, retry later !|[ErrorRepresentation](#errorrepresentation)|


<a name="serviceorderfind"></a>
#### List service orders
```
GET /serviceOrder
```


##### Description
Retrieve and list service order entities according to given criteria.
Only a predefined set of attribute is proposed.
Attribute selection could be described in the fields attribute.

Specific business errors for current operation will be encapsulated in

HTTP Response 422 Unprocessable entity


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Query**|**description**  <br>*optional*||string|
|**Query**|**externalId**  <br>*optional*||string|
|**Query**|**fields**  <br>*optional*|this attribute could be used to filter retrieved attribute(s) and/or sort SO.|string|
|**Query**|**limit**  <br>*optional*|The maximum number of elements to retrieve (it can be greater than the actual available number of items).|integer (int32)|
|**Query**|**offset**  <br>*optional*|The index of the first element to retrieve. Zero is the first element of the collection.|integer (int32)|
|**Query**|**orderDate.gt**  <br>*optional*|order date greather than|string|
|**Query**|**orderDate.lt**  <br>*optional*|order date lower than|string|
|**Query**|**state**  <br>*optional*|state of the order(s) to be retrieved|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Success  <br>**Headers** :   <br>`X-Total-Count` (integer (int32))  <br>`X-Result-Count` (integer (int32))|< [ServiceOrder](#serviceorder) > array|
|**400**|Bad Request<br><br>List of supported error codes:<br>- 20: Invalid URL parameter value<br>- 21: Missing body<br>- 22: Invalid body<br>- 23: Missing body field<br>- 24: Invalid body field<br>- 25: Missing header<br>- 26: Invalid header value<br>- 27: Missing query-string parameter<br>- 28: Invalid query-string parameter value|[ErrorRepresentation](#errorrepresentation)|
|**401**|Unauthorized<br><br>List of supported error codes:<br>- 40: Missing credentials<br>- 41: Invalid credentials<br>- 42: Expired credentials|[ErrorRepresentation](#errorrepresentation)|
|**403**|Forbidden<br><br>List of supported error codes:<br>- 50: Access denied<br>- 51: Forbidden requester<br>- 52: Forbidden user<br>- 53: Too many requests|[ErrorRepresentation](#errorrepresentation)|
|**404**|Not Found<br><br>List of supported error codes:<br>- 60: Resource not found|[ErrorRepresentation](#errorrepresentation)|
|**422**|Unprocessable entity<br><br>Functional error|[ErrorRepresentation](#errorrepresentation)|
|**500**|Internal Server Error<br><br>List of supported error codes:<br>- 1: Internal error|[ErrorRepresentation](#errorrepresentation)|
|**503**|Service Unavailable<br><br>List of supported error codes:<br>- 5: The service is temporarily unavailable<br>- 6: Orange API is over capacity, retry later !|[ErrorRepresentation](#errorrepresentation)|


<a name="serviceorderget"></a>
#### Retrieve a service order
```
GET /serviceOrder/{id}
```


##### Description
This operation retrieves a service order entity. 
Attribute selection is enabled for all first level attributes.

Specific business errors for current operation will be encapsulated in

HTTP Response 422 Unprocessable entity


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**id**  <br>*required*||string|
|**Query**|**fields**  <br>*optional*|Attribute selection|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Success|[ServiceOrder](#serviceorder)|
|**400**|Bad Request<br><br>List of supported error codes:<br>- 20: Invalid URL parameter value<br>- 21: Missing body<br>- 22: Invalid body<br>- 23: Missing body field<br>- 24: Invalid body field<br>- 25: Missing header<br>- 26: Invalid header value<br>- 27: Missing query-string parameter<br>- 28: Invalid query-string parameter value|[ErrorRepresentation](#errorrepresentation)|
|**401**|Unauthorized<br><br>List of supported error codes:<br>- 40: Missing credentials<br>- 41: Invalid credentials<br>- 42: Expired credentials|[ErrorRepresentation](#errorrepresentation)|
|**403**|Forbidden<br><br>List of supported error codes:<br>- 50: Access denied<br>- 51: Forbidden requester<br>- 52: Forbidden user<br>- 53: Too many requests|[ErrorRepresentation](#errorrepresentation)|
|**404**|Not Found<br><br>List of supported error codes:<br>- 60: Resource not found|[ErrorRepresentation](#errorrepresentation)|
|**422**|Unprocessable entity<br><br>Functional error|[ErrorRepresentation](#errorrepresentation)|
|**500**|Internal Server Error<br><br>List of supported error codes:<br>- 1: Internal error|[ErrorRepresentation](#errorrepresentation)|
|**503**|Service Unavailable<br><br>List of supported error codes:<br>- 5: The service is temporarily unavailable<br>- 6: Orange API is over capacity, retry later !|[ErrorRepresentation](#errorrepresentation)|


<a name="definitions"></a>
## Definitions

<a name="actiontype"></a>
### ActionType
Action type to be describer on the order item.
modify is not managed in Beijing release

*Type* : enum (add, modify, delete, noChange)


<a name="createserviceorder"></a>
### CreateServiceOrder
This structure is used in the operation POST for a serviceOrder request.
Attribute description is not accurate and should be find in the serviceOrder class.


|Name|Description|Schema|
|---|---|---|
|**@baseType**  <br>*optional*||string|
|**@schemaLocation**  <br>*optional*||string|
|**@type**  <br>*optional*||string|
|**category**  <br>*optional*|Used to categorize the order that can be useful for the OM system (e.g. “broadband”, “TVOption”, ...)|string|
|**description**  <br>*optional*|A free-text description of the service order|string|
|**externalId**  <br>*optional*|ID given by the consumer and only understandable by him (to facilitate his searches)|string|
|**orderItem**  <br>*optional*||< [CreateServiceOrderItem](#createserviceorderitem) > array|
|**orderRelationship**  <br>*optional*||< [OrderRelationship](#orderrelationship) > array|
|**priority**  <br>*optional*|A way that can be used by consumers to prioritize orders in Service Order Management system (from 0 to 4 : 0 is the highest priority, and 4 the lowest)|string|
|**relatedParty**  <br>*optional*||< [RelatedParty](#relatedparty) > array|
|**requestedCompletionDate**  <br>*optional*|Requested delivery date from the requestor perspective|string (date-time)|
|**requestedStartDate**  <br>*optional*|Order start date wished by the requestor|string (date-time)|


<a name="createserviceorderitem"></a>
### CreateServiceOrderItem
This structure is used in the operation POST for a serviceOrder request to describe an item.
Attribute description is not accurate and should be find in the serviceOrderItem class.


|Name|Description|Schema|
|---|---|---|
|**@baseType**  <br>*optional*|Indicates the base type of the resource.|string|
|**@schemaLocation**  <br>*optional*|A link to the schema describing this REST resource|string|
|**@type**  <br>*optional*|Indicates the type of resource.|string|
|**action**  <br>*optional*||[ActionType](#actiontype)|
|**id**  <br>*required*|Identifier of the line item (generally it is a sequence number 01, 02, 03, …)|string|
|**orderItemRelationship**  <br>*optional*||< [OrderItemRelationship](#orderitemrelationship) > array|
|**service**  <br>*required*||[Service](#service)|


<a name="errorrepresentation"></a>
### ErrorRepresentation
Representation of an error.


|Name|Description|Schema|
|---|---|---|
|**@schemaLocation**  <br>*optional*|it provides a link to the schema describing a REST resource|string|
|**@type**  <br>*optional*|The class type of a REST resource|string|
|**code**  <br>*required*|Application related code (as defined in the API or from a common list)|integer (int32)|
|**message**  <br>*optional*|Text that provide more details and corrective actions related to the error. This can be shown to a client user|string|
|**reason**  <br>*required*|Text that explains the reason for error. This can be shown to a client user.|string|
|**referenceError**  <br>*optional*|url pointing to documentation describing the error|string|
|**status**  <br>*optional*|http error code extension like 400-2|string|


<a name="hub"></a>
### Hub
An HUB resource is used by client side to subscribe to notification.
Not managed in the Beijing release.


|Name|Schema|
|---|---|
|**callback**  <br>*required*|string|
|**id**  <br>*optional*|string|
|**query**  <br>*optional*|string|


<a name="orderitemrelationship"></a>
### OrderItemRelationship
Linked order item to the one containing this attribute.
nbi component used this relationship to sort request to ONAP.


|Name|Description|Schema|
|---|---|---|
|**id**  <br>*required*|Unique identifier of an order item|string|
|**type**  <br>*required*||[RelationshipType](#relationshiptype)|


<a name="orderrelationship"></a>
### OrderRelationship
Linked order to the one containing this attribute.
This relationship is not used to sort ONAP request.


|Name|Description|Schema|
|---|---|---|
|**@referredType**  <br>*optional*|Type of the referred order.|string|
|**href**  <br>*optional*|A hyperlink to the related order|string|
|**id**  <br>*required*|The id of the related order|string|
|**type**  <br>*optional*|The type of related order, can be : “dependency” if the order needs to be “not started” until another order item is complete (a service order in this case) or “cross-ref” to keep track of the source order (a productOrder)|string|


<a name="relatedparty"></a>
### RelatedParty
A related party defines party which are involved in this order and the role they are playing.
for Beijing release:
With the current version of APIs used from SO and AAI we need to manage a ‘customer’. This customer concept is confusing with Customer BSS concept. We took the following rules to manage the ‘customer’ information:
o	It could be provided through a serviceOrder in the service Order a relatedParty with role ‘ONAPcustomer’ should be provided in the serviceOrder header (we will not consider in this release the party at item level); External API component will check if this customer exists and create it in AAI if not.
o	If no relatedParty are provided the service will be affected to ‘generic’ customer (dummy customer) – we assume this ‘generic’ customer always exists.


|Name|Description|Schema|
|---|---|---|
|**@referredType**  <br>*optional*||string|
|**href**  <br>*optional*|An hyperlink to the party - not used in Beijnig release|string|
|**id**  <br>*required*|Unique identifier of a related party|string|
|**name**  <br>*optional*|Name of the related party|string|
|**role**  <br>*required*|The role of the related party (e.g. Owner, requester, fullfiller etc).<br>ONLY 'ONAPcustomer' is considered|string|


<a name="relationshiptype"></a>
### RelationshipType
Relationship type;
Only reliesOn is managed in Beijing release.

*Type* : enum (reliesOn)


<a name="service"></a>
### Service
Service (to be added, modified, deleted) description


|Name|Description|Schema|
|---|---|---|
|**@schemaLocation**  <br>*optional*|The URL to get the resource schema.<br>Not managed in Beijing Release|string|
|**@type**  <br>*optional*|To define the service type<br>Not managed in Beijing Release|string|
|**href**  <br>*optional*|Reference to the Service (useful for delete or modify command).<br>Not managed in Beijing release.|string|
|**id**  <br>*required*|Identifier of a service instance.<br>It must be valued if orderItem action is 'delete' and corresponds to a AAI service.id|string|
|**name**  <br>*optional*|Name of the service - When orderItem action is 'add' this name will be used in ONAP/SO request as InstaceName.|string|
|**relatedParty**  <br>*optional*||< [RelatedParty](#relatedparty) > array|
|**serviceCharacteristic**  <br>*optional*||< [ServiceCharacteristic](#servicecharacteristic) > array|
|**serviceRelationship**  <br>*optional*||< [ServiceRelationship](#servicerelationship) > array|
|**serviceSpecification**  <br>*optional*||[ServiceSpecificationRef](#servicespecificationref)|
|**serviceState**  <br>*optional*|The lifecycle state of the service requested;<br>Not managed in Beijing release.|string|


<a name="servicecharacteristic"></a>
### ServiceCharacteristic
ServiceCharacteristic


|Name|Description|Schema|
|---|---|---|
|**name**  <br>*required*|Name of characteristic|string|
|**value**  <br>*optional*||[Value](#value)|
|**valueType**  <br>*optional*||string|


<a name="serviceorder"></a>
### ServiceOrder
A Service Order is a type of order which can be used to place an order between a customer and a service provider or between a service provider and a partner and vice versa


|Name|Description|Schema|
|---|---|---|
|**@baseType**  <br>*optional*||string|
|**@schemaLocation**  <br>*optional*||string|
|**@type**  <br>*optional*||string|
|**category**  <br>*optional*|Used to categorize the order that can be useful for the OM system (e.g. “broadband”, “TVOption”, ...)|string|
|**completionDateTime**  <br>*optional*|Date when the order was completed|string (date-time)|
|**description**  <br>*optional*|A free-text description of the service order|string|
|**expectedCompletionDate**  <br>*optional*||string (date-time)|
|**externalId**  <br>*optional*|ID given by the consumer and only understandable by him (to facilitate his searches)|string|
|**href**  <br>*optional*|Hyperlink to access the order|string|
|**id**  <br>*required*|ID created on repository side|string|
|**orderDate**  <br>*optional*||string (date-time)|
|**orderItem**  <br>*optional*||< [ServiceOrderItem](#serviceorderitem) > array|
|**orderRelationship**  <br>*optional*||< [OrderRelationship](#orderrelationship) > array|
|**priority**  <br>*optional*|A way that can be used by consumers to prioritize orders in Service Order Management system (from 0 to 4 : 0 is the highest priority, and 4 the lowest)|string|
|**relatedParty**  <br>*optional*||< [RelatedParty](#relatedparty) > array|
|**requestedCompletionDate**  <br>*optional*|Requested delivery date from the requestor perspective|string (date-time)|
|**requestedStartDate**  <br>*optional*|Order start date wished by the requestor|string (date-time)|
|**startDate**  <br>*optional*|Date when the order was started for processing|string (date-time)|
|**state**  <br>*optional*||[StateType](#statetype)|


<a name="serviceorderitem"></a>
### ServiceOrderItem
An identified part of the order. A service order is decomposed into one or more order items.


|Name|Description|Schema|
|---|---|---|
|**@baseType**  <br>*optional*|not used in Beijing relase|string|
|**@schemaLocation**  <br>*optional*|not used in Beijing relase|string|
|**@type**  <br>*optional*|Used to extend the order item.<br>not used in Beijing relase|string|
|**action**  <br>*optional*||[ActionType](#actiontype)|
|**id**  <br>*required*|Identifier of the line item (generally it is a sequence number 01, 02, 03, …)|string|
|**orderItemRelationship**  <br>*optional*||< [OrderItemRelationship](#orderitemrelationship) > array|
|**service**  <br>*required*||[Service](#service)|
|**state**  <br>*optional*||[StateType](#statetype)|


<a name="serviceref"></a>
### ServiceRef
Service references


|Name|Description|Schema|
|---|---|---|
|**href**  <br>*optional*|Reference of the service|string|
|**id**  <br>*required*|Unique identifier of the service|string|


<a name="servicerelationship"></a>
### ServiceRelationship
Linked Services to the one instantiate
nbi component used this relationship to sort request to ONAP.


|Name|Schema|
|---|---|
|**service**  <br>*required*|[Service](#service)|
|**type**  <br>*required*|[RelationshipType](#relationshiptype)|


<a name="servicespecificationref"></a>
### ServiceSpecificationRef
The service specification (these attributes are fetched from the catalogue).


|Name|Description|Schema|
|---|---|---|
|**@baseType**  <br>*optional*|Not used in Beijing release|string|
|**@schemaLocation**  <br>*optional*|Not used in Beijing release|string|
|**@type**  <br>*optional*|Not used in Beijing release|string|
|**href**  <br>*optional*|Reference of the service specification<br>Not used in Beijing release.|string|
|**id**  <br>*required*|Unique identifier of the service specification<br>This information will be used to retrieve SDC information + mapped to SO ModelNameVersionIdin the request.|string|
|**name**  <br>*optional*|Name of the service specification<br>Not used in Beijing release|string|
|**targetServiceSchema**  <br>*optional*||[TargetServiceSchema](#targetserviceschema)|
|**version**  <br>*optional*|Version of the service Specification<br>Not used in Beijing release|string|


<a name="statetype"></a>
### StateType
List of possible state for the order and the orderItem.

*Type* : enum (acknowledged, rejected, pending, held, inProgress, cancelled, completed, failed, partial)


<a name="targetserviceschema"></a>
### TargetServiceSchema
Target to the schema describing the service spec resource


|Name|Description|Schema|
|---|---|---|
|**@schemaLocation**  <br>*required*|This field provided a link to the schema describing this REST resource.|string|
|**@type**  <br>*required*|Indicates the (class) type of resource.|string|


<a name="value"></a>
### Value
Value is a descriptive structure for service characteristic;
For Beijing we only manage 'basic' attribute - the serviceCharacteristicValue must be used.


|Name|Description|Schema|
|---|---|---|
|**@schemaLocation**  <br>*optional*|This field provided a link to the schema describing this REST resource.<br>Not used in Beijing Release|string|
|**@type**  <br>*optional*|Indicates the (class) type of resource.<br>Not used in Beijing Release|string|
|**serviceCharacteristicValue**  <br>*optional*|Value of the characteristic.<br>This attribute must be used in Beijing Release to provide characteristic value.|string|

