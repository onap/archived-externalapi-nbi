# API ServiceInventory


<a name="overview"></a>
## Overview

### Api URL

[Swagger UI](https://api-designer.sso.infra.ftgroup/swagger-ui/?url=https://api-designer.sso.infra.ftgroup/api/1.0/apis/5ymwb6l1dR/swagger.json)


[plant UML UI](https://plantuml.rd.francetelecom.fr/proxy?fmt=svg&src=https://api-designer.sso.infra.ftgroup/api/1.0/apis/5ymwb6l1dR/plantuml&noCache=137264.0)

serviceInventory API designed for ONAP Beijing Release.
This API is build from TMF open API18.0 (applying TMF Guideline 3.0)
only operation GET (by id & byList) for resource serviceSpecification is available


### Version information
*Version* : 3.0.0_inProgress


### URI scheme
*Host* : serverRoot  
*BasePath* : /nbi/api/v3
*Schemes* : HTTPS


### Tags

* Service


### Produces

* `application/json;charset=utf-8`


<a name="paths"></a>
## Resources

<a name="service_resource"></a>
### Service

<a name="servicefind"></a>
#### List services
```
GET /service
```


##### Description
This operation list service entities. 
Attribute selection is restricted. 
fields attribute may be used to filter retrieved attribute(s) for each service

Specific business errors for current operation will be encapsulated in

HTTP Response 422 Unprocessable entity


##### Parameters

|Type|Name|Schema|
|---|---|---|
|**Query**|**fields**  <br>*optional*|string|
|**Query**|**id**  <br>*optional*|string|
|**Query**|**relatedParty.id**  <br>*optional*|string|
|**Query**|**serviceSpecification.id**  <br>*optional*|string|
|**Query**|**serviceSpecification.name**  <br>*optional*|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Success|< [ListService](#listservice) > array|
|**400**|Bad Request<br><br>List of supported error codes:<br>- 20: Invalid URL parameter value<br>- 21: Missing body<br>- 22: Invalid body<br>- 23: Missing body field<br>- 24: Invalid body field<br>- 25: Missing header<br>- 26: Invalid header value<br>- 27: Missing query-string parameter<br>- 28: Invalid query-string parameter value|[ErrorRepresentation](#errorrepresentation)|
|**401**|Unauthorized<br><br>List of supported error codes:<br>- 40: Missing credentials<br>- 41: Invalid credentials<br>- 42: Expired credentials|[ErrorRepresentation](#errorrepresentation)|
|**403**|Forbidden<br><br>List of supported error codes:<br>- 50: Access denied<br>- 51: Forbidden requester<br>- 52: Forbidden user<br>- 53: Too many requests|[ErrorRepresentation](#errorrepresentation)|
|**404**|Not Found<br><br>List of supported error codes:<br>- 60: Resource not found|[ErrorRepresentation](#errorrepresentation)|
|**422**|Unprocessable entity<br><br>Functional error|[ErrorRepresentation](#errorrepresentation)|
|**500**|Internal Server Error<br><br>List of supported error codes:<br>- 1: Internal error|[ErrorRepresentation](#errorrepresentation)|
|**503**|Service Unavailable<br><br>List of supported error codes:<br>- 5: The service is temporarily unavailable<br>- 6: Orange API is over capacity, retry later !|[ErrorRepresentation](#errorrepresentation)|


##### Produces

* `application/json;charset=utf-8`


<a name="serviceget"></a>
#### Retrieve a service
```
GET /service/{id}
```


##### Description
This operation retrieves a service entity. 
Attribute selection is enabled for all first level attributes.

Specific business errors for current operation will be encapsulated in

HTTP Response 422 Unprocessable entity


##### Parameters

|Type|Name|Schema|
|---|---|---|
|**Path**|**id**  <br>*required*|string|
|**Query**|**relatedParty.id**  <br>*optional*|string|
|**Query**|**serviceSpecification.id**  <br>*optional*|string|
|**Query**|**serviceSpecification.name**  <br>*optional*|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Success|[Service](#service)|
|**400**|Bad Request<br><br>List of supported error codes:<br>- 20: Invalid URL parameter value<br>- 21: Missing body<br>- 22: Invalid body<br>- 23: Missing body field<br>- 24: Invalid body field<br>- 25: Missing header<br>- 26: Invalid header value<br>- 27: Missing query-string parameter<br>- 28: Invalid query-string parameter value|[ErrorRepresentation](#errorrepresentation)|
|**401**|Unauthorized<br><br>List of supported error codes:<br>- 40: Missing credentials<br>- 41: Invalid credentials<br>- 42: Expired credentials|[ErrorRepresentation](#errorrepresentation)|
|**403**|Forbidden<br><br>List of supported error codes:<br>- 50: Access denied<br>- 51: Forbidden requester<br>- 52: Forbidden user<br>- 53: Too many requests|[ErrorRepresentation](#errorrepresentation)|
|**404**|Not Found<br><br>List of supported error codes:<br>- 60: Resource not found|[ErrorRepresentation](#errorrepresentation)|
|**422**|Unprocessable entity<br><br>Functional error|[ErrorRepresentation](#errorrepresentation)|
|**500**|Internal Server Error<br><br>List of supported error codes:<br>- 1: Internal error|[ErrorRepresentation](#errorrepresentation)|
|**503**|Service Unavailable<br><br>List of supported error codes:<br>- 5: The service is temporarily unavailable<br>- 6: Orange API is over capacity, retry later !|[ErrorRepresentation](#errorrepresentation)|


##### Produces

* `application/json;charset=utf-8`


<a name="definitions"></a>
## Definitions

<a name="errorrepresentation"></a>
### ErrorRepresentation
This class is used to describe error.
for nbi Beijing release we do not manage additional error for serviceCatalog


|Name|Description|Schema|
|---|---|---|
|**@schemaLocation**  <br>*optional*|it provides a link to the schema describing a REST resource.|string|
|**@type**  <br>*optional*|The class type of a REST resource.|string|
|**code**  <br>*required*|Application related code (as defined in the API or from a common list)|integer (int32)|
|**message**  <br>*optional*|Text that provide more details and corrective actions related to the error. This can be shown to a client user.|string|
|**reason**  <br>*required*|Text that explains the reason for error. This can be shown to a client user.|string|
|**referenceError**  <br>*optional*|url pointing to documentation describing the error|string|
|**status**  <br>*optional*|http error code extension like 400-2|string|


<a name="listrelatedpartyref"></a>
### ListRelatedPartyRef
This class is used to structure list of service(s) retrieved


|Name|Description|Schema|
|---|---|---|
|**id**  <br>*optional*|Unique identifier of a related party|string|
|**role**  <br>*optional*|Role played by the related party - only role “ONAPcustomer” is managed in Beijing release.|string|


<a name="listservice"></a>
### ListService
This class is used to structure list of service(s) retrieved


|Name|Description|Schema|
|---|---|---|
|**id**  <br>*optional*|Unique identifier of the service|string|
|**name**  <br>*optional*|Name of the service|string|
|**relatedParty**  <br>*optional*||[ListRelatedPartyRef](#listrelatedpartyref)|
|**serviceSpecification**  <br>*optional*||[ListServiceSpecificationRef](#listservicespecificationref)|


<a name="listservicespecificationref"></a>
### ListServiceSpecificationRef
This class is used to structure list of service(s) retrieved


|Name|Description|Schema|
|---|---|---|
|**id**  <br>*optional*|Unique identifier of the service specification|string|
|**name**  <br>*optional*|Name of the required service specification|string|


<a name="relatedpartyref"></a>
### RelatedPartyRef
RelatedParty reference. A related party defines party or party role linked to a specific entity.
Only ONAP Customer is managed in Beijing release.


|Name|Description|Schema|
|---|---|---|
|**@referredType**  <br>*optional*|Not managed in the Beijing release.|string|
|**href**  <br>*optional*|Reference of a related party.<br>Not filled in Beijing release.|string|
|**id**  <br>*optional*|Unique identifier of a related party|string|
|**role**  <br>*optional*|Role played by the related party.<br>Filled with 'ONAPcustomer'|string|


<a name="service"></a>
### Service
Instantiated service (service_instance) in AAI


|Name|Description|Schema|
|---|---|---|
|**@baseType**  <br>*optional*|Not managed in Beijing release|string|
|**@schemaLocation**  <br>*optional*|Not managed in Beijing release|string|
|**@type**  <br>*optional*|This attribute allows to dynamically extends TMF class. Not used in Beijing release.|string|
|**characteristic**  <br>*optional*||< [ServiceCharacteristic](#servicecharacteristic) > array|
|**hasStarted**  <br>*optional*|This is a Boolean attribute that, if TRUE, signifies that this Service has already been started. If the value of this attribute is FALSE, then this signifies that this Service has NOT been Started<br>Not managed in Beijing release|boolean|
|**href**  <br>*optional*|Reference of the service<br>Not managed in Beijing release|string|
|**id**  <br>*optional*|Unique identifier of the service - Valued with service-instance-id|string|
|**name**  <br>*optional*|Name of the service - Valued with service-instance-name|string|
|**relatedParty**  <br>*optional*||< [RelatedPartyRef](#relatedpartyref) > array|
|**serviceSpecification**  <br>*optional*||[ServiceSpecificationRef](#servicespecificationref)|
|**state**  <br>*optional*|State of the service. Not managed in Beijing release|string|
|**supportingResource**  <br>*optional*||< [SupportingResource](#supportingresource) > array|
|**type**  <br>*optional*|Service type - valued with 'service-instance'|string|


<a name="servicecharacteristic"></a>
### ServiceCharacteristic
A list of name value pairs that define the service characteristics
Not managed in Beijing release.


|Name|Description|Schema|
|---|---|---|
|**name**  <br>*required*|Name of the characteristic<br>Not managed in Beijing release.|string|
|**value**  <br>*optional*||[Value](#value)|
|**valueType**  <br>*optional*|Type of value for this characteristic.<br>Not managed in Beijing release.|string|


<a name="servicespecificationref"></a>
### ServiceSpecificationRef
Service specification reference: ServiceSpecification of this service (catalog information)


|Name|Description|Schema|
|---|---|---|
|**@referredType**  <br>*optional*|This attribute allows to dynamically extends TMF class. Valued with 'ONAPservice'. We used this features to add following attribute: invariantUUID|string|
|**@schemaLocation**  <br>*optional*|Not managed in Beijing release|string|
|**href**  <br>*optional*|Reference of the service specification.<br>not managed in Beijing release.|string|
|**id**  <br>*optional*|Unique identifier of the service specification. valued to model-version-id|string|
|**invariantUUID**  <br>*optional*|Additional attribute (not in the TMF API) - extended through @referredType - model-invariant-id|string|
|**name**  <br>*optional*|Name of the required service specification|string|
|**version**  <br>*optional*|Service specification version.<br>Not managed in Beijing release|string|


<a name="supportingresource"></a>
### SupportingResource
Supporting resource - A supportingResource will be retrieved for each relationship of the relationship-list where related-link describe a vnf


|Name|Description|Schema|
|---|---|---|
|**@referredType**  <br>*optional*|This attribute allows to dynamically extends TMF class. Valued with 'ONAP resource'. We used this features to add following attributes:<br>    status	<br>    modelInvariantId<br>   modelVersionId<br>   modelCustomisationId|string|
|**@schemaLocation**  <br>*optional*|Not managed in Beijing release.|string|
|**href**  <br>*optional*|Reference of the supporting resource|string|
|**id**  <br>*optional*|Unique identifier of the supporting resource - Valued to vnf-id|string|
|**modelCustomisationId**  <br>*optional*|Additional attribute (not in the TMF API) - extended through @referredType - valued with model-customisation-id|string|
|**modelInvariantId**  <br>*optional*|Additional attribute (not in the TMF API) - extended through @referredType - valued with model-invariant-id|string|
|**modelVersionId**  <br>*optional*|Additional attribute (not in the TMF API) - extended through @referredType - valued with model-verson-id|string|
|**name**  <br>*optional*|Name of the supporting resource - Valued with vnf_-name|string|
|**role**  <br>*optional*|Not managed in Beijing release.|string|
|**status**  <br>*optional*|Additional attribute (not in the TMF API) - extended through @referredType - valued with prov-status|string|


<a name="value"></a>
### Value
Structure used to describe characteristic value.
Not managed in Beijing release.


|Name|Description|Schema|
|---|---|---|
|**@schemaLocation**  <br>*optional*|Not managed in Beijing release.|string|
|**@type**  <br>*optional*|Not managed in Beijing release.|string|
|**serviceCharacteristicValue**  <br>*optional*|Not managed in Beijing release.|string|

