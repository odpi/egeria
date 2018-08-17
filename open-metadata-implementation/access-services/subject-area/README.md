<!-- SPDX-License-Identifier: Apache-2.0 -->

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

The implementation is not complete. 
The following has been implemented : 

* Java and REST API for create, get, update and delete for Glossary.
* [GlossarySample](../../../open-metadata-resources/open-metadata-samples/open-metadata-subjectarea-client-samples/src/main/java/org/odpi/openmetadata/accessservices/subjectarea/samples/GlossarySample.java) is a sample that can be used to test the glossary java APIs. 

## Example REST calls: 

The implementation is not complete. The following has been implemented : 

* REST API for create, get and update for Glossary.

## Example REST calls: 

### Create Glossary instance

POST url: `localhost:8080/open-metadata/access-services/subject-area/users/{user}/glossaries`


JSON body 
{
  "name": "Test glossary 1",
  "description": "This is a Glossary for testing.",
  "usage": "for test",
  "governanceActions": {
    "confidentiality": {
      "level": "Confidential",
      "confidence": "10",
      "steward": "Stuart",
      "source": "source value",
      "notes": "An interesting note",
      "status": "Proposed"
    },
    "criticality": {
      "level": "Important",
      "confidence": "9",
      "steward": "Stuart2",
      "source": "source value2",
      "notes": "An interesting note ish",
      "status": "Imported"
    },
    "confidence": {
      "level": "AdHoc",
      "confidence": "9",
      "steward": "Stuart2",
      "source": "source value2",
      "notes": "An interesting note ish",
      "status": "Imported"
    },
    "retention":{
      "confidence": 6,
      "notes": "some notes",
      "steward": "Fred",
      "source": "a source",
      "basis": "RegulatedLifetime"
    }
  }
}

### Get Glossary instance
 Get Glossary instance (where {user} is the guid in the Glossary create response and {user} is the userid )


GET url: `localhost:8080/open-metadata/access-services/subject-area/users/{user}/glossaries/{guid}`

(where `{guid}` is the GUID in the Glossary create response)

### Delete Glossary instance

Delete Glossary instance (where {user} is the guid in the Glossary create response and {user} is the userid )
DELETE url : localhost:8080/open-metadata/access-services/subject-area/users/{user}/glossaries/{guid}


