<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![TechPreview](../../../images/egeria-content-status-tech-preview.png#pagewidth)

# Subject Area Open Metadata Access Service (OMAS)

The Subject Area OMAS supports subject-matter experts who are documenting
their knowledge about a particular in a glossary.

* [Documentation](https://egeria-project.org/services/omas/security-manager/overview)

## Design Information

The module structure for the Subject Area OMAS is as follows:

* [subject-area-client](subject-area-client) supports the client library.
* [subject-area-api](subject-area-api) supports the common Java classes that are used both by the client and the server.
* [subject-area-server](subject-area-server) supports an implementation of the access service and its related event management.
* [subject-area-spring](subject-area-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.

The implementation is not complete. The following has been implemented : 

* Java and REST API for create, get and update for Glossary, Category, Term , SubjectAreaDefinition.
* Java and REST API for the Term to Term relationships HAS-A, RelatedTerm, Synonym, Antonym, Translations, used in context,
  preferred terms, valid values, replacement terms, typed by, is a, is a type of.
* Java and REST API for the Term to Category relationships TermCategorization.  
* TermAnchor and CategoryAnchor relationships can be created , deleted, purged and restored. As there are no properties, there are no update or replace operations.
* getTermRelationships, get GlossaryRelationships and getCategoryRelationships
* findTerm, findCategory and findGlossary  

This has been verified by running the [Subject Area samples](../../../open-metadata-resources/open-metadata-samples/access-services-samples/subject-area-client-samples) and
 [Subject Area FVT](../../../open-metadata-test/open-metadata-fvt/access-services-fvt/README.md) against an in-memory repository


## Example REST calls: 
### Create Glossary instance

POST url: `localhost:9443/servers/{serverName}/open-metadata/access-services/subject-area/users/{user}/glossaries`

JSON body:

```json
{
  "name": "Test glossary 1",
  "description": "This is a Glossary for testing.",
  "usage": "for test" 
}
```

### Get Glossary instance
 Get Glossary instance (where {serverName} is the name of the server, {guid} is the guid in the Glossary create response and {user} is the userid )


GET url: `localhost:9443/servers/{serverName}/open-metadata/access-services/subject-area/users/{user}/glossaries/{guid}`

(where `{guid}` is the GUID in the Glossary create response)

### Delete Glossary instance

Delete Glossary instance (where {guid} is the guid in the Glossary create response and {user} is the userid )
DELETE url : localhost:9443/open-metadata/access-services/subject-area/users/{user}/glossaries/{guid}

## The Subject Area OMAS philosophy

The Subject area OMAS is the access service that subject area experts should use. The intent is that the APIs that are exposed are natural for the 
tasks that a subject area expert is performing. At this time the Subject area OMAS exposes APIs around the task of Glossary authoring,
focusing on Glossary, Category and Term objects.
 
## The Subject Area OMAS architecture
The Subject Area main objects are the Glossary, Category and Terms. There map onto the OMRS types Glossary, GlossaryCategory and GlossaryTerm. The mapping is
not one to one, because the OMAS API is looking to emphasise certain content and hide some of the OMRS details that the subject area expert is not concerned with.
 
Subject Area OMAS mapping to OMRS entities considerations:
* Glossary, Category and Term objects have associated icons, these are embedded objects rather than relationships. In this way icon content is shown as important to 
the subject area expert as they are like to be working with the glossary content visually
* The icon embedded object is an IconSummary object. This is an example of other object in the OMAS API whose names end with "Summary". These objects represent
 a summary of the entity at the end of a certain type of OMRS relationships. Note the icon function has not been implemented in the OMAS yet.
* Categories and Terms have a GlossarySummary , this is there associated Glossary. Good practice is to have Terms and Categories within a Glossary, so the Subject
area API Term and Category create and update APIs expect a glossary to be supplied. See effective date considerations.   
* OMRS relationships can be managed via the Subject Area relationships API. Some of these relationships may appear as summary objects.    
    

## The Subject Area OMAS API overview.

There are a number of types of APIs associated with the Subject Area OMAS.  
* Create, update, replace, get, delete (hard and soft) and restore for Glossary, Category, Term and relationships.
* get relationships associated with a Term - implemented
* Find APIs allow content to be found - findTerm, findCategory and findGlossary implemented
* Collaboration APIs allow comments and TODO and the like to be associate with glossary content
* A report API, allows glossary content to be analysed, the API response highlights areas that the subject Area Expert might want to amend. - not implemented yet
* Node orientated APIs


## How the Subject Area OMAS deals with effective dates
 
 The OMRS entities, relationships and classifications have optional effective From and To dates. These dates are exposed in the Term, Category and Glossary 
objects as attributes.
* create, update and replace calls to the subject Area for Term, Category, glossary and relationships omas can specify an effective date range in the request, allowing the subject area 
OMAS to manage effective dates. The null value or when it is not specified To date means there is no limit in the future for the objects effectivity.
 A null or unspecified from date means that this no starting restriction for effectivity. The date must not be in the past. The From date should be prior to the To Date.
* create, update, replace, restore, soft delete responses may return Summary objects that are not in the effective date range of the associated Term,
Category or Glossary object. This is to allow glossaries content to be 'messy' and allow the subject area expert to fix it up.           
* A get of a Term, Glossary or Category that has potentially associated Summary objects. Because the Subject Area OMAS is an authoring interface, the user needs to 
see all content irrespective of the effectivity date. So associated summary objects are exposed even if they are not effective. The summary objects contain the effectivity dates of the relationship
and the connected object. The Subject Area user can see these dates and maker a decision as to whether they want to amend them.
* create, update, delete restore and replace operations are exposed for relationships that appear as summary objects - so that their effectivity ranges can be managed
by the subject area expert.

## How the Subject Area OMAS deals with finds

 The find APIs in the Subject Area do not accept input from the user that will be interpreted as a regex. Instead 2 flags are supplied,
 with the searchCriteria: exactValue and mixedCase. The search criteria from API is a literal and is then extended appropriately
 to form a regex expression - implementing the requested exactValue and mixedCase.  
 
----
Return to [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
  
  








   
 
 






