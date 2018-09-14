# API ServiceCatalog


<a name="overview"></a>
## Overview

### Api URL

[Swagger UI](https://api-designer.sso.infra.ftgroup/swagger-ui/?url=https://api-designer.sso.infra.ftgroup/api/1.0/apis/XOmvoxNn9d/swagger.json)


[plant UML UI](https://plantuml.rd.francetelecom.fr/proxy?fmt=svg&src=https://api-designer.sso.infra.ftgroup/api/1.0/apis/XOmvoxNn9d/plantuml&noCache=995303.0)

serviceCatalog API designed for ONAP Beijing Release.
This API is build from TMF open API17.5. 
Only operation GET (by id & byList) for resource serviceSpecification is available


### Version information
*Version* : 3.0.0_inProgress


### URI scheme
*Host* : serverRoot  
*BasePath* : /nbi/api/v3
*Schemes* : HTTPS


### Tags

* ServiceSpecification


### Produces

* `application/json;charset=utf-8`


<a name="paths"></a>
## Resources

<a name="servicespecification_resource"></a>
### ServiceSpecification

<a name="servicespecificationfind"></a>
#### List service specifications
```
GET /serviceSpecification
```


##### Description
This operation returns service specifications from a catalog.
Only a predefined set of attribute is proposed : Based on SDC limitations, only attributes category and distributionStatus are available for serviceSpecification filtering
Fields attribute could be used to filter attributes retrieved

Specific business errors for current operation will be encapsulated in

HTTP Response 422 Unprocessable entity


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Query**|**category**  <br>*optional*|Service Category (filter)|string|
|**Query**|**distributionStatus**  <br>*optional*|Service distribution status (filter)|string|
|**Query**|**fields**  <br>*optional*|Field selection - used to filtering the attributes to be retreived|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Success|< [ServiceSpecification](#servicespecification) > array|
|**400**|Bad Request<br><br>List of supported error codes:<br>- 20: Invalid URL parameter value<br>- 21: Missing body<br>- 22: Invalid body<br>- 23: Missing body field<br>- 24: Invalid body field<br>- 25: Missing header<br>- 26: Invalid header value<br>- 27: Missing query-string parameter<br>- 28: Invalid query-string parameter value|[ErrorRepresentation](#errorrepresentation)|
|**401**|Unauthorized<br><br>List of supported error codes:<br>- 40: Missing credentials<br>- 41: Invalid credentials<br>- 42: Expired credentials|[ErrorRepresentation](#errorrepresentation)|
|**403**|Forbidden<br><br>List of supported error codes:<br>- 50: Access denied<br>- 51: Forbidden requester<br>- 52: Forbidden user<br>- 53: Too many requests|[ErrorRepresentation](#errorrepresentation)|
|**404**|Not Found<br><br>List of supported error codes:<br>- 60: Resource not found|[ErrorRepresentation](#errorrepresentation)|
|**422**|Unprocessable entity<br><br>Functional error|[ErrorRepresentation](#errorrepresentation)|
|**500**|Internal Server Error<br><br>List of supported error codes:<br>- 1: Internal error|[ErrorRepresentation](#errorrepresentation)|
|**503**|Service Unavailable<br><br>List of supported error codes:<br>- 5: The service is temporarily unavailable<br>- 6: Orange API is over capacity, retry later !|[ErrorRepresentation](#errorrepresentation)|


##### Produces

* `application/json;charset=utf-8`


<a name="servicespecificationget"></a>
#### Retrieve a service specification
```
GET /serviceSpecification/{id}
```


##### Description
This operation returns a service specification by its id from a catalog. Attribute selection is enabled using the fields attribute.

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
|**200**|Success|[ServiceSpecification](#servicespecification)|
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

<a name="attachment"></a>
### Attachment
An attachment is a file uses to describe the service.
In nbi we use attachment to retrieve ONAP artifacts.


|Name|Description|Schema|
|---|---|---|
|**@type**  <br>*optional*|This attribute allows to dynamically extends TMF class. Valued with 'ONAPartifact'. We used this features to add following attributes: <br>artifactLabel<br>artifactGroupType<br>artifactTimeout<br>artifactChecksum<br>artifactVersion<br>generatedFromUUID  <br>**Default** : `"ONAPartifact"`|string|
|**artifactChecksum**  <br>*optional*|Additional attribute (not in the TMF API) - extended through @type - artifactChecksum|string|
|**artifactGroupType**  <br>*optional*|Additional attribute (not in the TMF API) - extended through @type - artifactGroupType|string|
|**artifactLabel**  <br>*optional*|Additional attribute (not in the TMF API) - extended through @type - artifactLabel|string|
|**artifactTimeout**  <br>*optional*|Additional attribute (not in the TMF API) - extended through @type - artifactTimeout|string|
|**artifactVersion**  <br>*optional*|Additional attribute (not in the TMF API) - extended through @type - artifactVersion|string|
|**description**  <br>*optional*|Description of the attachment - filled with artifactDescription|string|
|**generatedFromUUID**  <br>*optional*|Additional attribute (not in the TMF API) - extended through @type - generatedFromUUID|string|
|**id**  <br>*optional*|Unique identifier of the attachment - filled with artifactUUID.|string|
|**mimeType**  <br>*optional*|Filled with artifactType|string|
|**name**  <br>*optional*|Name of the attachment - filled with artifactName|string|
|**url**  <br>*optional*|Uniform Resource Locator, is a web page address - filled with artifactURL|string|


<a name="distributionstatus"></a>
### DistributionStatus
Service distribution status from ONAP.

*Type* : enum (DISTRIBUTION_NOT_APPROVED, DISTRIBUTION_APPROVED, DISTRIBUTED, DISTRIBUTION_REJECTED)


<a name="errorrepresentation"></a>
### ErrorRepresentation
This class is used to describe error.
for nbi Beijing release we do not manage additional error for serviceCatalog


|Name|Description|Schema|
|---|---|---|
|**@schemaLocation**  <br>*optional*|it provides a link to the schema describing a REST resource.|string|
|**@type**  <br>*optional*|The class type of a REST resource.|string|
|**code**  <br>*required*|Application related code (as defined in the API or from a common list)|integer (int32)|
|**message**  <br>*optional*|Text that provide more details and corrective actions related to the error. This can be shown to a client user|string|
|**reason**  <br>*required*|Text that explains the reason for error. This can be shown to a client user.|string|
|**referenceErrror**  <br>*optional*|url pointing to documentation describing the error|string|
|**status**  <br>*optional*|http error code extension like 400-2|string|


<a name="lifecyclestatusvalues"></a>
### LifecycleStatusValues
Service lifecycle value from ONAP SDC

*Type* : enum (NOT_CERTIFIED_CHECKOUT, NOT_CERTIFIED_CHECKIN, READY_FOR_CERTIFICATION, CERTIFICATION_IN_PROGRESS, CERTIFIED)


<a name="relatedpartyref"></a>
### RelatedPartyRef
Party linked to the service catalog.
in nbi we retrieve information about last updater of the service in SDC


|Name|Description|Schema|
|---|---|---|
|**id**  <br>*optional*|Unique identifier of the related party. Filled with lastUpdaterUserId|string|
|**name**  <br>*optional*|Name of the related party - Filled with lastUpdatedFullName|string|
|**role**  <br>*optional*|Role payed by the related party<br>Only role 'lastUpdater' is retrieved in Beijing release|string|


<a name="resourcespecificationref"></a>
### ResourceSpecificationRef
A list of resourceSpec identified to deliver the service.
for nbi we retrieve resource information available in service description (through SDC api) bu as well information retrieved in the TOSCA file.


|Name|Description|Schema|
|---|---|---|
|**@type**  <br>*optional*|This attribute allows to dynamically extends TMF class. Valued with: 'ONAPresource'. We used this features to add following attributes:<br>resourceInstanceName<br>resourceInvariantUUID<br>resourceType<br>modelCustomizationName<br>modelCustomizationId  <br>**Default** : `"ONAPresource"`|string|
|**id**  <br>*optional*|Unique identifier of the resource specification - filled with resourceUUID|string|
|**modelCustomizationId**  <br>*optional*|Additional attribute (not in the TMF API) - extended through @type - Retrieved in the TOSCA file : attribute customizationUUID in topology_template/node_template for the resource|string|
|**modelCustomizationName**  <br>*optional*|Additional attribute (not in the TMF API) - extended through @type - Retrieved in the TOSCA file : attribute name in topology_template/node_template for the resource|string|
|**name**  <br>*optional*|Name of the resource specification - filled with resourceName|string|
|**resourceInstanceName**  <br>*optional*|Additional attribute (not in the TMF API) - extended through @type - resourceInstanceName|string|
|**resourceInvariantUUID**  <br>*optional*|Additional attribute (not in the TMF API) - extended through @type - resourceInvariantUUID|string|
|**resourceType**  <br>*optional*|Additional attribute (not in the TMF API) - extended through @type - resoucreType|string|
|**version**  <br>*optional*|Version for this resource specification - filled with resourceVersion|string|


<a name="servicespeccharacteristic"></a>
### ServiceSpecCharacteristic
A characteristic quality or distinctive feature of a ServiceSpecification. 
ServiceSpecCharacteristic are retrieved in the serviceTosca file in the topology_template section in the inputs section.


|Name|Description|Schema|
|---|---|---|
|**@schemaLocation**  <br>*optional*|An url pointing to type description - we do not use it in nbi Beijing release|string|
|**@type**  <br>*optional*|This attribute allows to dynamically extends TMF class. Valued with: 'ONAPserviceCharacteristic'. We do not used this features in nbi Beijing release.|string|
|**description**  <br>*optional*|A narrative that explains in detail what the characteristic is - Filled with parameter_description|string|
|**name**  <br>*optional*|Name of the characteristic - Filled with parameter_name|string|
|**required**  <br>*optional*|A parameter to define if the characteristic is mandatory - Filled from parameter_required – if not fielded by default ‘true’  <br>**Default** : `true`|boolean|
|**serviceSpecCharacteristicValue**  <br>*optional*||< [ServiceSpecCharacteristicValue](#servicespeccharacteristicvalue) > array|
|**status**  <br>*optional*|Status of the characteristic - filled with status_value|string|
|**valueType**  <br>*optional*|A kind of value that the characteristic can take on, such as numeric, text and so forth - Filled with parameter_type|string|


<a name="servicespeccharacteristicvalue"></a>
### ServiceSpecCharacteristicValue
A number or text that can be assigned to a service specification characteristic.
ServiceSpecCharacteristicValue are retrieved in the service Tosca file


|Name|Description|Schema|
|---|---|---|
|**isDefault**  <br>*optional*|Information calculated from parameter default in the Tosca file|boolean|
|**value**  <br>*optional*|A discrete value that the characteristic can take on|string|
|**valueType**  <br>*optional*|A kind of value that the characteristic can take on, such as numeric, text, and so forth<br>Retrieved in the Tosca in the topology_template section in the inputs section - parameter_type. <br>We do not manage parameter_type= list or map for Beijing release|string|


<a name="servicespecification"></a>
### ServiceSpecification
ServiceSpecification is a class that offers characteristics to describe a type of service. Functionally, it acts as a template by which Services may be instantiated. By sharing the same specification, these services would therefore share the same set of characteristics.
the service information are retrieved in SDC


|Name|Description|Schema|
|---|---|---|
|**@baseType**  <br>*optional*|Not used for Beijing release|string|
|**@schemaLocation**  <br>*optional*|Not used for Beijing release|string|
|**@type**  <br>*optional*|This attribute allows to dynamically extends TMF class. Valued with 'ONAPservice'. We used this features to add following attributes:<br>invariantUUID<br>toscaModelURL<br>toscaResourceName<br>category (1)<br>subcategory (1)<br>distributionStatus  <br>**Default** : `"ONAPservice"`|string|
|**attachment**  <br>*optional*||< [Attachment](#attachment) > array|
|**category**  <br>*optional*|Additional attribute - extended through @type - category<br>Please note that this attribute is managed in TMF - in future release we'll introduce category resource|string|
|**description**  <br>*optional*|A narrative that explains in detail what the service specification is - Filled with SDC Service description|string|
|**distributionStatus**  <br>*optional*||[DistributionStatus](#distributionstatus)|
|**href**  <br>*optional*|Reference of the service specification- not mapped in Beijing|string|
|**id**  <br>*optional*|Unique identifier of the service specification. Filled with SDC Service uuid|string|
|**invariantUUID**  <br>*required*|Additional attribute (not in the TMF API) - extended through @type - invariantUUID|string|
|**lifecycleStatus**  <br>*optional*||[LifecycleStatusValues](#lifecyclestatusvalues)|
|**name**  <br>*optional*|Name of the service specification- Filled with SDC Service name|string|
|**relatedParty**  <br>*optional*||< [RelatedPartyRef](#relatedpartyref) > array|
|**resourceSpecification**  <br>*optional*||< [ResourceSpecificationRef](#resourcespecificationref) > array|
|**serviceSpecCharacteristic**  <br>*optional*||< [ServiceSpecCharacteristic](#servicespeccharacteristic) > array|
|**subcategory**  <br>*optional*|Additional attribute - extended through @type - category<br>Please note that this attribute is managed in TMF - in future release we'll introduce category resourc|string|
|**targetServiceSchema**  <br>*optional*||[TargetServiceSchemaRef](#targetserviceschemaref)|
|**toscaModelURL**  <br>*optional*|Additional attribute (not in the TMF API) - extended through @type - toscaModelURL|string|
|**toscaResourceName**  <br>*optional*|Additional attribute (not in the TMF API) - extended through @type - toscaResourceName|string|
|**version**  <br>*optional*|Service specification version - Filled with SDC Service version|string|


<a name="targetserviceschemaref"></a>
### TargetServiceSchemaRef

|Name|Schema|
|---|---|
|**@schemaLocation**  <br>*required*|string|
|**@type**  <br>*required*|string|


<a name="timeperiod"></a>
### TimePeriod
A time period


|Name|Description|Schema|
|---|---|---|
|**endDateTime**  <br>*optional*|End date and time of the period|string (date-time)|
|**startDateTime**  <br>*optional*|Start date and time of the period|string (date-time)|

