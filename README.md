# SmartCapusAPI

## Overview 
The Smart Campus API systme allows the management of:
Rooms 
sensors 
sensor readings 

The Smart Capmus API shows:

RESTful design principles 
sub - resource architecture
Fltering with query storsge
Custom exceptions hanlding 
Request/response logging 

Base URL : 
http://localhost:8081/api/v1

## POSTMAN TESTING :
Request:
GET :http://localhost:8081/api/v1
##### Expected:
200 ok 

#### Create Rooms 
Request:
POST: http://localhost:8081/api/v1/rooms
body:
{ "id": "R101", "name": "Computer Lab", "capacity": 40 }
##### Expected Result:
201 created, 
Room is successfully created 

#### Get All Rooms 
Request: 
GET :http://localhost:8081/api/v1/rooms
##### Expected Result:
300 ok,
Returns a list containing the created room 

#### Get Room by ID 
GET : http://localhost:8081/api/v1/rooms/R101
##### Expected Results:
200 ok, 
Returns details of the specific room 

#### Delete room 
DELETE :  http://localhost:8081/api/v1/rooms/R101
##### Expected Results:
Room is deleted 

#### Create Sensor(Invalid Room)
Request:
POST : http://localhost:8081/api/v1/sensors
Body: { "id": "S1", "type": "CO2", "status": "ACTIVE", "currentValue": 0.0, "roomId": "R102" }

#### Create Sensor(Valid Room)
Request:
POST : http://localhost:8081/api/v1/sensors
Body: { "id": "S1", "type": "CO2", "status": "ACTIVE", "currentValue": 0.0, "roomId": "R101" }
##### Results :
201 Created, sensor succefully linked to room R101

#### Get All Sensors 
Request:
GET: http://localhost:8081/api/v1/sensors
##### Expected Results:
Status 200 ok ,
Returns list of sensors

#### Filter Sensors by Type 
Request:
GET: http://localhost:8081/api/v1/sensors?type=CO2
##### Expected Results:
Status 200 ok,
only CO2 sensor are returned

#### Add Sensor Reading ]
Request:
POST : http://localhost:8081/api/v1/sensors/S1/readings
Body: { "id": "RD1", "timestamp": 1717000000, "value": 415.7 }
##### Expected Results:
status 201 created,
Reading is succefully added 

#### Get Sensor Readings 
Request: GET: http://localhost:8081/api/v1/sensors/S1/readings
##### Expected Results:
Status 200 ok,
Returns reading history

#### Verify Current Value Update 
Request: GET:http://localhost:8081/api/v1/sensors/S1 
##### Expected Results:
status 200 ok,
currentValuse is updated to 415.7

## Error Handling Tests 

#### Invalid Sensor Creation 
Request: POST: http://localhost:8081/api/v1/sensors 
Body: { "id": "S2", "type": "TEMP", "status": "ACTIVE", "currentValue": 22.5, "roomId": "R999" }
##### Expected Results:
Error Response returned 

#### Sensor in Maintenance State:
Request: POST http://localhost:8081/api/v1/sensors/S3/readings 
##### Expected Results:
status 403 forbidden,
Rewading is rejected 

#### Delete Room with Active Sensors 
Request: DELETE: http://localhost:8081/api/v1/rooms/R101
##### Expected Results:
Status: 409 conflict,
Deletion is blocked 

#### Resource Not Found 
Request: 
GET: http://localhost:8081/api/v1/rooms/R999
##### Expected Results:
Status 404 Not Found 

# Questions 
### Part 1 : Servcie Architecture and Setup 
#### Question 1 : JAX -RS Resource Lifecycle
JAX-RS resource classes have a pre- request lifspan by default. This implies that each incoming HTTP request creates a new instance of the resource class, which is then destroyed when the response is provided. 
Shared state problems within resouce objects are avoided by this design. However, since many requests may access shared data contained in collections like HashMaps or ArrayLists simultaneously, they must be hadnled carefully. In order to avoid race situations and Inconsistent data, thread- safe collections or synchronisation method must be employed.

#### Question 2 : Hypermedia ( HATEOAS) 
Because it enables API's to incorporate links within response that direct clients to related resources and actions,hypermedia is regarded as a characteristic of sophistiicated RESTful architecture.
Client developers gain from this since hardcoding endpoint URLs is no longer necessary. Rather, the API structure can be dynamically discovered by clients, increasing the system's adaptability, maintainability and flexibility.

### Part 2 : Room Management
#### Question 1 : ID vs Full Objects 
Returning just IDs enhances performance and uses less network capacity, particularly when working with big databases, However, in order to obtain complete information, the client must cub,it additional request.
By giving all pertinent information in a single response, returning complete objects increase response size but streamlines client-side processing. 

#### Question 2  : DELETE Idempotency 
Sending the same request repeatedly does not alter the outcome after the first execution, making the DELETE operatioin idempotent.
subsequent DELETE requests for the same room won't change the system status if the deletion was successful. They mgiht give a 404 Not Found message, but nothing else changes. 

### Part 3 : Sensor Operations 
#### Question 1 : @Consumes Annotation 
@Consumes (MediaType). Only JSON - Formatted queries are accepted by the API thanks to the APPLICATION_JSON) annotaion.
The request will not match the resource method if a client submits data in a diffrent format, such as text/plain or application/xml, and the server will reply with 415 Unsupported Medi Type error.

#### Question 2  QueryParam  vs PathParam
Because it specifies optional filtering conditions on a resource colletion, using query parameters for filtering (e.g /sensors?type= CO2) is preferred.
while path parameters are more appropriate for identifying particular resources than for filtering collections, it permist flexible and scalable queries. 

### Part 4 : Sub - Resources 
#### Question 1 : Sub - Resource Locator Pattern
By assigning distinct classes to handle nested resources, the sub - Resource Locator approach enhances API architecture.
This simplifies the primary resourcces class, enhances readablitlity , and Facilitates sytems maintenance and scalability as the API expands 


### Part 5 : Error Handling and Logging
#### Question 2 : why 422 instead of 404 
HTTP 422 Unprocessable Entity is more Suitable since the request is legitimate in and of itself, but it contains incorrect or invalid data ( such as a roomID that does not exist). The endpoint does not exist, as a 404 Not Found woudl suggest, but this is untrue.

#### Question 4 : Stack Trace Security Risk 
Because it discloses impementation details such class names, file structures, and system behaviour, exposing internal java stack traces poses a security risk. 
Thia information canbe used by attackers to find weakbnesses. Consequently, generic error messages should be returned by APIs. does not exits which is not true.

#### Question 5 : Logging with Filters 

The logging logic is centralised and all incoming requests and outgoing answers are reliably reported when JAX-RS filters are used.
This makes the codebase tidy and manageable by preventing the duplication of logging code across serveral resource functions.


