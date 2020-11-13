<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Spring and Spring Boot

**Spring** is a framework and set of annotations for building REST APIs.  **Spring Boot** provides the server chassis
(or `main()` method) for hosting RESTful services in a server.

Spring is used in our client libraries to call REST APIs.  Specifically it provides the
`org.springframework.web.client.RestTemplate` class for formatting rest calls and parsing the responses.

See the [OMRS REST Repository Connector](../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/omrs-rest-repository-connector/README.md)
for an example of the use of RestTemplate to implement calls to the Open Metadata Repository Services (OMRS)
REST API.

On the server-side, Spring provides the annotations that define how a Java method is exposed as a REST API.
This includes the URL of the call, and how the parameters and responses are mapped.
A REST API is typically implemented as a single Java class where each method is a different operation on the
REST API.

At the top of the Java class is a declaration of the URI that is common for all methods in the class.
For example, this is the declaration used for the OMRS REST APIs

```java
@RestController
@RequestMapping("/open-metadata/repository-services")
```

This URI follows the root URL of the server.  So if the server was using `https://localhost:9443`, the methods are called
using

```
https://localhost:9443/open-metadata/repository-services ...
```

Then for each method/operation, the rest of the URL is defined and mapped.

```
@GetMapping(path = "/metadata-collection-id")

public MetadataCollectionIdResponse getMetadataCollectionId()
{
   /*
    * ... implementation here
    */
}
```

See the [OMRS Server-side REST implementation](../open-metadata-implementation/repository-services/repository-services-spring/README.md)
for the server-side annotations that process the REST calls from the OMRS REST Connector.

Spring Boot is used in the 
[OMAG Server Chassis](../open-metadata-implementation/server-chassis/server-chassis-spring/README.md).
It searches for all of the REST API definitions and starts them in a server.

For more information on Spring, see [https://spring.io/](https://spring.io/).


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.