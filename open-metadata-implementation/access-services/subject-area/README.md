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

## The Subject Area OMAS philosophy

The Subject area OMAS is the access service that subject area experts should use. The intent is that the APIs that are exposed are natural for the 
tasks that a subject area expert is performing. At this time the subjet area OMAS exposes APIs around the task of Glossary authoring,
focussing on Glossary, Category and Term objects.
 
## The Subject Area OMAS architecture
The Subject Area main objects are the Glossary, Category and Terms. There map onto the OMRS types Glossary, GlossaryCategory and GlossaryTerm. The mapping is
not one to one, because the OMAS API is looking to emphasis certain content and hide some of the OMRS details that the  subject area expert is not concerned with.
 
Subject Area OMAS mapping to OMRS entities considerations:
* Glossary, Category and Term objects have associated icons, these are embedded objects rather than relationships. In this way icon content is shown as important to 
the subject area expert as they are like to be working with the glossary content visually
* The icon embedded object is a n IconSummary object. This is an example of other object in the OMAS API whose names end with "Summary". These objects represent
 a summary of the entity at the end of a certain type of OMRS relationship
* Categories and Terms have a GlossarySummary , this is there associated Glossary. Good practice is to have Terms and Categories within a Glossary, so the Subject
area API Term and Category create and update APIs expect a glossary to be supplied. See effective date considerations.   
* OMRS relationships can be managed via the Subject Area relationship API. Some of these relationships may appear as summary objects.    
    

## The Subject Area OMAS API overview.

There are a number of types of APIs associated with the Subject Area OMAS.  
* Create, update, replace, get, delete (hard and soft) and restore for Glossary, Category, Term and relationships.
* Find APIs allow content to be found - not implemented yet
* Collaboration APIs allow comments and TODO and the like to be associate with glossary content
* A report API, allows glossary content to be analysed, the API response highlights areas that the subject Area Expert might want to amend. - not implemented yet
* More sophisticated get API for example get relationships associated with a Term - not implemented yet
* find API - not implemented yet


## How the Subject Area OMAS deals with effective dates
 
 The OMRS entities, relationships and classifications have optional effective From and To dates. These dates are exposed in the Term, Category and Glossary 
objects as attributes.
* create, update and replace calls to the subject Area for Term, Category, glossary and relationships omas can specify an effective date range in the request, allowing the subject area 
OMAS to manage effective dates. The null value or when it is not specified To date means there is not limit in the future for the objects effectivity.
 A null or unspecified from  date means that this no starting restriction for effectivity. The date must not be in the past. The From date should be prior to the To Date.
* create, update, replace, restore, soft delete responses may return Summary objects that are not in the effective date range of the associated Term,
Category or Glossary object. This is to allow glossaries content to be 'messy' and allow the subject area expert to fix it up.           
* A get of a Term, Glossary or Category that has potentially associated Summary objects, will only return the summary objects within the effective date
range of the get operation at the time the get was processed.
* create, update, delete restore and replace operations are exposed for relationships that appear as summary objects - so that their effectivity ranges can be managed
by the subject area expert.
  
  








   
 
 






