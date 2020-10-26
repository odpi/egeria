<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Design Guidelines for an OMAS

Each OMAS is designed from its intended consumer's perspective.  The end user may be a person performing
a specific role or a specific type of technology such as a tool, engine or platform.

The consumer of an OMAS may be served through a client API, or asynchronous events (in and out) or both.
The choice depends on which is most useful to the consumer.

With this in mind, the descriptions below aim to guide you through the process of building an OMAS
so that the structure is consistent with the other OMASs, whilst ensuring it is designed for its intended users.

## Naming Conventions

In the description, the name of OMAS being built is embedded in the path names and file names of the implementation.
Different technologies have different naming rules so the name of the OMAS is coded as follows:

* **omas-name** - lower case name with a dash separator, eg community-profile.  This is used in module names.
* **omasname** - lower case name with no separator, eg communityprofile.  This is used in Java package names.
* **OmasName** - title case name with no separator, eg CommunityProfile.  This is used in Java class names.

### Implementation modules

The implementation of the OMAS is typically divided into 4 modules:

* ***omas-name*-api** - a module containing the APIs, objects passed on the API, in the event payloads and the exceptions.
* ***omas-name*-client** - a module containing the client implementation(s).
There may be client interfaces and implementations for different programming languages.
Typically Java is supported and this is assumed in the descriptions that follow.
* ***omas-name*-server** - a module containing the server-side implementation of the OMAS.
This module registers with the server administration, supports the implementation or the API and the eventing interface
and interfaces with the [Open Metadata Repository Services (OMRS)](../../../repository-services).
* ***omas-name*-spring** - a module containing the server-side API decorated with [Spring](../../../../developer-resources/Spring.md) 
annotations.  This module is designed to be replaceable with a module using other REST annotation libraries so its
API should be 1-1 with the *omas-name*-server API.

Each OMAS should also have an FVT test suite called ***omas-name*-fvt** implemented
under the [access-services-fvt](../../../../open-metadata-test/open-metadata-fvt/access-services-fvt) module.
This is in addition to the unit tests for the module's components that are implemented within each module.
The FVT test suite should test all of the API calls through to the repository to show that the OMAS
can find, create, update and delete all of the necessary metadata on behalf of its consumer.

It is also expected that each OMAS will feature in multiple demos, samples and scenarios found under
[open-metadata-resources](../../../../open-metadata-resources).  However, these are not the responsibility of
the OMAS component owner.

### Java package names

The java package name root to use for all of the Java implementation of the OMAS is:
 
 > `org.odpi.openmetadata.accessservices.`*omasname*.

### REST URL names

The REST URL naming convention for OMAS URLs is:
 
> *serverRootURL*`/servers/{serverName}/open-metadata/access-services/*omas-name*/users/{userId}/...`

where:

* **serverName** is the name of the server instance.
* **userId** is the identifier for the calling user.

Both of these parameters are mandatory on each operation. The `...` is where the specific operations are defined.

## Implementation process

The following steps define the recommended approach to writing a new OMAS.  The list can also be used when
extending an OMAS as well, skipping the steps that do not need changing/extending.  For example, if you are
implementing the OMAS one scenario at a time.

* Define who the intended consumer(s) of the OMAS is/are, the types of information they need from the metadata
  repository and the tasks they need to perform on that metadata.   Ideally pick one or two appropriate persona
  from [Coco Pharmaceuticals](https://odpi.github.io/data-governance/coco-pharmaceuticals/personas/) to help
  clarify who the consumer is if it is a person.
  
  Use the vocabulary that is familiar to the consumer community in your description, rather than the vocabulary used in the
  [open metadata types](../../../../open-metadata-publication/website/open-metadata-types) (mapping to the open metadata types comes later).
 
   * Document this as a summary for the top-level README.md for the OMAS (***omas-name*/README.md**) and a
     more detailed description as an introduction to the user documentation in ***omas-name*/docs/user/README.md**.
  
   * Document each of the core concepts as their own markdown file in ***omas-name*/docs/concepts/*concept-name*.md**.
     Add links to these files wherever the concept is first used in other markdown files.
     Create a README.md in the same directory (ie ***omas-name*/docs/concepts/README.md**) that lists and links to each of the key concepts.
  
   * Add a link to each of the core concepts descriptions in the [open metadata glossary](../../../../open-metadata-publication/website/open-metadata-glossary.md) markdown file.
  
* Think about the typical situations where the consumer would use the OMAS and define the typical scenarios they would follow.
  This process often causes you to add more core concepts to the list defined above.

   * Document a summary of these scenarios in the README.md for the OMAS's user documentation (***omas-name*/docs/user/README.md**)
     with a link to the full list of scenarios in ***omas-name*/docs/scenarios/README.md** (see next bullet).
   
   * Document the detailed list of scenarios in the README.md for the OMAS's scenario documentation (***omas-name*/docs/scenarios/README.md**)
     with links to a separate markdown description of each scenario stored in the same directory.
     
   * Add any additional concepts to ***omas-name*/docs/user/concepts**.
   
   * Review the top-level description (***omas-name*/README.md**) to ensure it is still accurate.
  
* Create the directories for the *omas-name*-api and *omas-name*-client module.

* Design the client interface and the beans that are passed.

   * Divide the scenarios into logical groups, which become the client interfaces.
   
   * Create the programming language (typically Java) interface for the API
     (located in *omas-name*/*omas-name*-client/src/main/org/odpi/openmetadata/accessservices/*omasname*)
     Document each interface and its purpose in the *omas-name*-api/docs/interface folder.
     
   * Create the bean implementations that are needed for the client API in the OMAS's api module
     (located in *omas-name*/*omas-name*-client/src/main/org/odpi/openmetadata/accessservices/*omasname*/properties)
       
   * Create the exception implementations that are needed for the client API in the OMAS's api module
     (located in *omas-name*/*omas-name*-client/src/main/org/odpi/openmetadata/accessservices/*omasname*/ffdc/exceptions)
       
   * Create the initial error code list that are needed to distinguish different situations where each exception is thrown
     by the API.  This is only an initial list but it is a helpful exercise to start to think through the different cases.
     (located in *omas-name*/*omas-name*-client/src/main/org/odpi/openmetadata/accessservices/*omasname*/ffdc/*OmasName*ErrorCode.java)

   * Design and document each of the operations in the *omas-name*-client/docs/user folder and link them
     to the interface description.
     
   * Document the list of clients in the top-level README.md for the OMAS's client module (***omas-name*/*omas-name*-client/README.md**)
     These operations should map to the scenarios defined above.
  
   * Document the beans in the top-level README.md for the OMAS's client module (***omas-name*/*omas-name*-client/README.md**).
  
   * Document the interface operations used to achieve each of the scenarios described in the OMAS's top-level README.md.
     This description should go in the user directory of the 
     OMAS's client module (***omas-name*/*omas-name*-client/docs/user**).
     There should be a top level README.md that lists each scenario with a link to a separate markdown file for each
     scenario.  The descriptions of the scenarios should be linked to from the OMAS's user documentation README (***omas-name*/docs/user/README.md**))
   
* Design the FVT tests for the interface.

  FVT stands for functional verification test.  FVTs test the whole OMAS working with the repository services.
  They should exercise each part of the API including passing bad parameters and testing the exceptions
  format the messages properly.  They should also verify that the audit log messages format properly too.
  
  The FVTs should run with little manual intervention and include instructions so that anyone can run them.
  Ultimately, they will run as part of the centralized build.
 
* Design the event payloads that are sent and received through the OMAS's 
  [OutTopic](../concepts/client-server/out-topic.md) and [InTopic](../concepts/client-server/in-topic.md) respectively.

* Design the FVT test for the Events.

* Create the directories for the *omas-name*-server module.

* Implement the server-side framework.

  Each OMAS needs to implement the AccessServiceAdmin interface and register the presence of an OMAS in the
  server.  This registration provides the class name of the OMAS's admin implementation that is used
  by the admin services to initialize and terminate the OMAS in the server.
  
  Implementing the admin interface requires you to know if the OMAS supports (1) receiving events from
  the repository services, (2) receiving events from external application through the OMAS's InTopic and/or (3)
  sending events on the OMAS's OutTopic. 
  
  Create the skeleton classes for the listeners and publisher as necessary.
  
  Document the server-side components in one or more markdown files in the design directory of the 
  OMAS's server module (***omas-name*/*omas-name*-server/docs/design**).

* Design and implement converters and mappers.  

  The mappers are classes that define the statics that map from the bean properties to the open metadata
  types.  The converters build the beans from repository services entities, classifications and relationships.
  They are used to build the values returned on methods.
  
  This step allows you to confirm that the design for the beans and the open metadata types are consistent
  and sufficient for the needs of the OMAS.

* Design and implement the builders.

  Builder classes create repository services entities, classifications and relationships from properties
  passed as parameters on the methods, typically for create, update and find methods.  The results of the
  builder classes are passed to the repository services by the handlers.
  
* Design and implement the handlers.

  The handlers implement the client APIs but run on the server side and implement the calls to the
  repository services.
  
* Create the directories for the *omas-name*-spring module.

* Design and implement the REST API - both client and server-side.

  The clients are java classes that implement the OMAS interfaces.
  With the exception of OMASs that support APIs for the open connector framework (OCF), the REST API is
  one-to-one with the client interfaces.  This means there is one REST operation for each Java method on each
  API.
  
  Where an OMAS supports the OCF, the client-side has additional methods to manage connectors through the
  connector broker.
  
  The clients make calls to the REST APIs.
  
  Document the REST API operations in ***omas-name*/*omas-name*-server/docs/user**
  
  Link to the the top-level README.md for the OMAS's server module (***omas-name*/*omas-name*-server/README.md**).
  
  Implement the ***omas-name*/*omas-name*-server-spring** module for the OMAS.  This contains the sever-side
  resource classes that support the REST API.
  
  Add the ***omas-name*/*omas-name*-server-spring** module to the `pom.xml` file for the
  [OMAG Server Chassis](../../../server-chassis/server-chassis-spring).

* Design and implement the event handling.

  Log each event that is picked up (inbound) to be processed by the OMAS into its audit log using the `EVENT` severity.
  Also log each event that is produced (outbound) by the OMAS into its audit log using the `EVENT` severity. This
  ensures a record is kept of the asynchronous processing done by the OMAS.

  Log the first error of a given kind via audit log as well, using the appropriate severity. For example, if an event is
  picked up that triggers some logic in the OMAS that requires the OMAS to communicate with an external system for
  additional information, and that system is unreachable, log that failure in communication once per external system.
  Since there could be many other events that also trigger a need to communicate with that same system, it is not
  necessary to log this communication failure with that system every time, as this would result in many duplicate error
  messages and could needlessly flood the audit log. However, it is important to log the error at least once so that
  appropriate problem determination of the asynchronous processing can be done from the audit log.

* Implement the FVT and run it.



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.