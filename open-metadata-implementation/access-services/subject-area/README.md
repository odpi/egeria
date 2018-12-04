<!-- SPDX-License-Identifier: Apache-2.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Subject Area Open Metadata Access Service (OMAS)

The Subject Area OMAS supports subject matter experts who are documenting
their knowledge about a particular subject.  This includes:

* glossary terms
* reference data
* validation rules

The Subject Area API enables subject matter experts to author glossary content. The operations include Find, Create, Read, Update and 
Delete (CRUD) operations on Glossary, Term and Category objects.

These structures are defined as POJO property objects (aka beans).

The module structure for the Subject Area OMAS is as follows:

* [subject-area-client](subject-area-client) supports the client library.
* [subject-area-api](subject-area-api) supports the common Java classes that are used both by the client and the server.
* [subject-area-server](subject-area-server) supports an implementation of the access service and its related event management.
* [subject-area-spring](subject-area-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.
* [subject-area-tools](subject-area-tools) supports code generation of POJO property objects from archive types.

The implementation is not complete. The following has been implemented : 

* Java and REST API for create, get and update for Glossary, Category, Term , SubjectAreaDefinition.
* Java and REST API for the Term to Term relationships HASA, RelatedTerm, Synonym, Antonym, Translations, used in context,
  preferred terms, valid values, replacement terms, typed by, is a, is a type of.

## Example JAVA calls: 
See the [Subject Area samples](../../../open-metadata-resources/open-metadata-samples/access-services-samples/subject-area-client-samples/README.md) and [Subject Area FVT](../../../open-metadata-test/open-metadata-fvt/access-services-fvt/README.md) projects for examples around how to use the Java API.    

## Example REST calls: 
### Create Glossary instance

POST url: `localhost:8080/servers/{serverName}/open-metadata/access-services/subject-area/users/{user}/glossaries`

JSON body:
```JSON
{
  "name": "Test glossary 1",
  "description": "This is a Glossary for testing.",
  "usage": "for test" 
}
```

### Get Glossary instance
 Get Glossary instance (where {user} is the guid in the Glossary create response and {user} is the userid )


GET url: `localhost:8080/servers/{serverName}/open-metadata/access-services/subject-area/users/{user}/glossaries/{guid}`

(where `{guid}` is the GUID in the Glossary create response)

### Delete Glossary instance

Delete Glossary instance (where {user} is the guid in the Glossary create response and {user} is the userid )
DELETE url : localhost:8080/open-metadata/access-services/subject-area/users/{user}/glossaries/{guid}
