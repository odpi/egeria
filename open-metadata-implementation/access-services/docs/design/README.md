<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Designing and Implementing an OMAS

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

* ***omas-name*-api** - a module containing the objects passed on the API, in the event payloads and the exceptions.
* ***omas-name*-client** - a module containing the client interface definition and implementation.
There may be client interfaces and implementations for different programming languages.
Typically Java is supported and this is assumed in the descriptions that follow.
* ***omas-name*-server** - a module containing the server-side implementation of the OMAS.
This module registers with the server administration, supports the implementation or the API and the eventing interface
and interfaces with the [Open Metadata Repository Services (OMRS)](../../../repository-services).
* ***omas-name*-spring** - a module containing the server-side API decorated with [Spring](../../../../developer-resources/Spring.md) 
annotations.  This module is designed to be replaceable with a module using other REST annotation libraries so its
API should be 1-1 with the *omas-name*-server API.

Each OMAS should also have an FVT test suite called ***omas-name*-fvt** implemented
under the [access-services-fvt](../../../../open-metadata-test/open-metadata-fvt/access-services-fvt).  This is
in addition to the unit tests for the module's components that are implemented within each module.
The FVT test suite should test all of the API calls through to the repository to show that the OMAS
can find, create, update and delete all of the necessary metadata on behalf of its consumer.

It is also expected that each OMAS will feature in multiple demos, samples and scenarios found under
[open-metadata-resources](../../../../open-metadata-resources).  However, these are not the responsibility of
the OMAS component status.

### Java package names

The java package name root to use for all of the Java implementation of the OMAS is:
 
 > `org.odpi.openmetadata.accessservices.`*omasname*.

### REST URL names

The REST URL naming convention for OMAS URLs is:
 
> *serverURL*`/servers/{serverName}/open-metadata/access-services/community-profile/users/{userId}/...`

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
     more detailed description as in introduction to the user documentation in ***omas-name*/docs/user/README.md**.
  
   * Document each of the core concepts as their own markdown file in ***omas-name*/docs/concepts/*concept-name*.md**.
     Add links to these files wherever the concept is first used in other markdown files.
     Create a README.md in the same directory (ie ***omas-name*/docs/concepts/README.md**) that lists and links to each of the key concepts.
  
   * Add a link to each of the core concepts descriptions in the [open metadata glossary](../../../../open-metadata-publication/website/open-metadata-glossary.md) markdown file.
  
* Think about the typical situations where the consumer would use the OMAS and defined the typical scenarios they would follow.  This
  process often causes you to add more core concepts to the list defined above.

   * Document a summary of these scenarios in the README.md for the OMAS's user documentation (***omas-name*/docs/user/README.md**)
     with a link to the full list of scenarios in ***omas-name*/docs/scenarios/README.md** (see next bullet).
   
   * Document the detailed list of scenarios in the README.md for the OMAS's scenario documentation (***omas-name*/docs/scenarios/README.md**)
     with links to a separate markdown description of each scenario stored in the same directory.
     
   * Add any additional concepts to ***omas-name*/docs/user/concepts**.
   
   * Review the top-level description (***omas-name*/README.md**) to ensure it is still accurate.
  
* Create the directories for the *omas-name*-api and *omas-name*-client module.

* Design the client API and the beans that are passed.

   * Document the list of API operations in the top-level README.md for the OMAS's client module (***omas-name*/*omas-name*-client/README.md**)
     These operations should map to the scenarios defined above.
  
   * Document the beans in the top-level README.md for the OMAS's client module (***omas-name*/*omas-name*-client/README.md**).
  
  Document the API operations used to achieve each of the scenarios described in the OMAS's top-level README.md.
  This description should go in the user directory of the 
  OMAS's client module (***omas-name*/*omas-name*-client/docs/user**).
  There should be a top level README.md that lists each scenario with a link to a separate markdown file for each
  scenario.  The descriptions of the scenarios should be linked to from the OMAS's user documentation README (***omas-name*/docs/user/README.md**))
  
  Create the programming language (typically Java) interface for the API
  (located in *omas-ame*/*omas-name*-client/src/main/org/odpi/openmetadata/accessservices/*omasname*)
  
  Create the bean implementations that are needed for the client API in the OMAS's api module
  (located in *omas-name*/*omas-name*-client/src/main/org/odpi/openmetadata/accessservices/*omasname*/properties)
  
  Create the exception implementations that are needed for the client API in the OMAS's api module
  (located in *omas-name*/*omas-name*-client/src/main/org/odpi/openmetadata/accessservices/*omasname*/ffdc/exceptions)
  
  Create the initial error code list that are needed to distinguish different situations where each exception is thrown
  by the API.  This is only an initial list but it is a helpful exercise to start to think through the different cases.
  (located in *omas-name*/*omas-name*-client/src/main/org/odpi/openmetadata/accessservices/*omasname*/ffdc/*OmasName*ErrorCode.java)

* Design the FVT tests for the API.
 
* Design the event payloads that are sent and received through the OMAS's 
  [OutTopic](../concepts/out-topic.md) and [InTopic](../concepts/in-topic.md) respectively.

* Design the FVT test for the Events.

* Create the directories for the *omas-name*-spring and *omas-name*-server module.

* Design and implement the REST API - both client and server-side.

  Document the REST API operations in the top-level README.md for the OMAS's server module (***omas-name*/*omas-name*-server/README.md**).

* Implement the server-side framework.

  Document the server-side components in one or more markdown files in the design directory of the 
  OMAS's server module (***omas-name*/*omas-name*-server/docs/design**).

* Design and implement converters.

* Design and implement the handlers.

* Design and implement the event handling.

* Implement the FVT and run it.






----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.