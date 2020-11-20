<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Standards for OMAS implementations

Draft - put together by Nigel on 15 Aug – I hope this can be a starting point to get some agreement and consistency over how we develop the OMAS interfaces.

Notes from 15/08 call 

 * not a chatty interface

 * move this comment into a sub JIRA of OMAS JIRA & add comments as pdf/doc or ascii text so they can go through JIRA review -> or even apache review board tool with line by line annotation..... (I'll do this tomorrow 16/08)


Scope


* Over time general external applications should find the functionality they need through this level of interface (OMRS is more geared to direct metadata exchange) & other Atlas APIs will be superceeded
* OMAS interfaces are targeted at a particular type of consumer with the objective of making it easy for that consumer - transforming, mapping data from Atlas/OMRS as required
* Given the application focus most consuming apps may interact with only 1 or perhaps 2 OMASs. Having to interact with more indicates incomplete OMAS definitions. Some duplication is to be expected.
* OMAS interfaces will use OMRS to manage the underlying metadata (other than in any transitionary phase)
* The structures used on the REST API should be similar to the messaging api
* Where the same representation of an underlying entity etc is similar between OMASs, consideration should be given to using the same structure. However the second point is the more critical
* A standard list of scoping parameters has been documented (see connector OMAS), these should be implemented IF RELEVANT but ommitted if not.
General


* The performance framework will be used at a minimum to record response times for all API calls
REST API

 

* GET for query, nothing should change in the entity. No side effects (except last access time/audit logs)
* PUT is used for create
* POST is used for update or actions with side effects
* DELETE will delete (usually soft)
* PATCH is not used
* Scoping parameters should feature in GETs as query parameters
* Pagination should be supported (Page Number & Page Size)
* Only json is supported
* A non successful response will send back a sensible http status code, a more detailed enumeration specifying the type of error, a specific, unique error code, and a string containing other helpful information or parameters. A "user response" is also returned. (Example I used for GAF is here - follow swagger link)
* The endpoint will be /v2/<omasname>
* The /v2/<omasname>/>object> will be a plural noun
Kafka Messaging (pub/sub)


* Each OMAS will listen to ONE incoming omas kafka topic (per omas) for requests to process
* Each OMAS will listen to OMRS kafka topic so as to respond to changes in the metadata repository
* Deployment-time properties will configure the topic names to be used
* Out of order messages need to be handled "sensibly" [requires further discussion] & clustered environments catered for
* Messaging infrastructure should be pluggable, but only kafka will be implemented
Language/Bindings


 * REST/Kafka - language neutral
 * Java client (remote) - improves ease of use 
 * Python - not initially but expressed as a language of choice for data scientists
Documentation


* A skeleton Java implementation will initially be committed to generate Swagger API
* When the community is broadly happy the implementation will be filled out

    omasstandardsapikafka 

2 Comments

    User icon: david_radley
    david radley

    Hi Nigel ,

    some comments in <<David >> sections:

    General

     

    * The performance framework will be used at a minimum to record response times for all API calls <<David I am not sure I understand - is this an OMAS perfomance framework - I assume we do not want to use Atlas code here. We need to think through audit logs and debugging LOGs in the implementation for OMAS. >>

    REST API

    * GET for query, nothing should change in the entity. No side effects (except last access time/audit logs)

    * PUT is used for create  <<David PUT for update / replace>>

    * POST is used for update or actions with side effects <<David POST for create - Atlas does tolerate updates if a guid is supplied >>

    * DELETE will delete (usually soft) <<David I am not sure the OMAS needs to know about soft>>

    * PATCH is not used <<David Head might be useful to check whether the service is up.  >>

    * Scoping parameters should feature in GETs as query parameters <<David I was thinking that the Get for a resource will not have these query parameters, but the get with the search endpoint could have>>

    * Pagination should be supported (Page Number & Page Size)  <<David and sort direction and criteria >>

    * Only json is supported

    * A non successful response will send back a sensible http status code, a more detailed enumeration specifying the type of error, a specific, unique error code, and a string containing other helpful information or parameters. A "user response" is also returned. (Example I used for GAF is here - follow swagger link) <<David I thought we were not using GAF for the OMAS - and are moving to Governance Action - If we are not thinking of non-Goverance Action OMAS, I wonder if we can just call it the Action OMAS.  >>

    * The endpoint will be /v2/<omasname> <<David I suggest OMAS/<<David omasname> as per last call>>

    * The /v2/<omasname>/>object> will be a plural noun <<David I was thinking singular - what is your thinking?>>

    Kafka Messaging (pub/sub)

     

    * Each OMAS will listen to ONE incoming omas kafka topic (per omas) for requests to process

    * Each OMAS will listen to OMRS kafka topic so as to respond to changes in the metadata repostitory <<David I assume it creates a n OMAS message containing the change>>

    * Deployment-time properties will configure the topic names to be used <<David why is this important?>>

    * Out of order messages need to be handled "sensibly" [requires further discussion] & clustered environments catered for

    * Messaging infrastructure should be pluggable, but only kafka will be implemented

    Language/Bindings

     

     * REST/Kafka - language neutral

     * Java client (remote) - improves ease of use

     * Python - not initially but expressed as a language of choice for data scientists

    Documentation

     

    * A skeleton Java implementation will initially be committed to generate Swagger API

    * When the community is broadly happy the implementation will be filled out
        Aug 16, 2017
        Delete comments

        User icon: jonesn
        Nigel Jones

        Thanks for the comments.
        Following our call yesterday I'm going to
         a) create a new subtask of the toplevel JIRA to capture this list of best practices 
         b) copy the content into a test file 

         c) remove the content from this wiki and leave a placeholder until refined and agreed

         d) submit through the review board tool so that we can collect and review comments

        I'll take into the account the useful comments you made above - can you give me a day or so and then we continue on RB?
            Aug 16, 2017
            Delete comments
            
            
 
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
