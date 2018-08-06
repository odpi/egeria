<!-- SPDX-License-Identifier: Apache-2.0 -->
  
# Egeria - Open Metadata and Governance
  
Egeria provides the Apache 2.0 licensed [open metadata and governance](open-metadata-publication/website/README.md)
type system, frameworks, APIs, event payloads and interchange protocols to enable tools,
engines and platforms to exchange metadata in order to get the best
value from data whilst ensuring it is properly governed.

## Useful links

* **[What is Egeria and what does it do?](open-metadata-publication/website/README.md)**
* **[Why is this open metadata and governance capability supported by Egeria important?](https://zenodo.org/record/556504)**
* **[Where is the design documentation?](open-metadata-publication/website/README.md#technical-components)**
* **[Where are the open metadata specifications?](open-metadata-publication/website/open-metadata-specifications/README.md)**
* **[How do I run Egeria?](open-metadata-implementation/governance-servers/server-chassis/README.md)**
* **[How do I contribute to Egeria?](Community-Guide.md)**
* **[How do I raise a bug or feature request?](Community-Guide.md#getting-a-jira-account-and-creating-an-issue)**
* **[How do I enhance my product to support open metadata?](open-metadata-publication/website/open-metadata-integration-patterns/README.md)**
* **[How do I test that a product is compliant with the open metadata standards?](open-metadata-compliance-suite/README.md)**

## Egeria content organization
  
The Egeria content is organized into the following modules:

* **[developer-resources](developer-resources)** - contains useful files and documentation for an Egeria developer.
* **[open-metadata-implementation](open-metadata-implementation)** - implementation of standards, frameworks and connectors.
  * **[access-services](open-metadata-implementation/access-services)** - domain specific APIs known as the Open Metadata Access Services (OMAS).
    * **[asset-catalog](open-metadata-implementation/access-services/asset-catalog)** - search for assets.
    * **[asset-consumer](open-metadata-implementation/access-services/asset-consumer)** - create connectors to access assets.
    * **[asset-owner](open-metadata-implementation/access-services/asset-owner)** - manage metadata and feedback for owned assets.
    * **[community-profile](open-metadata-implementation/access-services/community-profile)** - manage personal profiles and communities.
    * **[connected-asset](open-metadata-implementation/access-services/connected-asset)** - provide metadata about assets for the connectors.
    * **[data-architecture](open-metadata-implementation/access-services/data-architecture)** - support the definition of data standards and models.
    * **[data-infrastructure](open-metadata-implementation/access-services/data-infrastructure)** - manage metadata about deployed infrastructure.
    * **[data-platform](open-metadata-implementation/access-services/data-platform)** - exchange metadata with a data platform.
    * **[data-privacy](open-metadata-implementation/access-services/data-privacy)** - support a data privacy officer.
    * **[data-process](open-metadata-implementation/access-services/data-process)** - exchange metadata with a data processing engine.
    * **[data-protection](open-metadata-implementation/access-services/data-protection)** - set up rules to protect data.
    * **[data-science](open-metadata-implementation/access-services/data-science)** - manage metadata for analytics.
    * **[dev-ops](open-metadata-implementation/access-services/dev-ops)** - manage metadata for a devOps pipeline.
    * **[discovery-engine](open-metadata-implementation/access-services/discovery-engine)** - manage metadata for metadata discovery services.
    * **[governance-engine](open-metadata-implementation/access-services/governance-engine)** - manage metadata for an operational governance engine.
    * **[governance-program](open-metadata-implementation/access-services/governance-program)** - set up and manage a governance program.
    * **[information-view](open-metadata-implementation/access-services/information-view)** - configure and manage metadata for data tools that create virtual views over data - such as business intelligence tools and data virtualization platforms.
    * **[project-management](open-metadata-implementation/access-services/project-management)** - manage definitions of projects for metadata management and governance.
    * **[software-developer](open-metadata-implementation/access-services/software-developer)** - deliver useful metadata to software developers.
    * **[stewardship-action](open-metadata-implementation/access-services/stewardship-action)** - manage metadata as part of a data steward's work to improve the data landscape.
    * **[subject-area](open-metadata-implementation/access-services/subject-area)** - develop a definition of a subject area including glossary terms, reference data and rules.
  * **[adapters](open-metadata-implementation/adapters)** - pluggable component implementations.
    * **[open-connectors](open-metadata-implementation/adapters/open-connectors)** are connectors that support the Open Connector Framework (OCF).
      * **[connector-configuration-factory](open-metadata-implementation/adapters/open-connectors/connector-configuration-factory)** creates **Connection** objects to configure the open connectors.
      * **[access-services-connectors](open-metadata-implementation/adapters/open-connectors/access-services-connectors)** contains the topic connector implementations for each of the access services that support inbound events through an InTopic.
      * **[repository-services-connectors](open-metadata-implementation/adapters/open-connectors/repository-services-connectors)** contains connector implementations for each type of connector supported by the Open Metadata Repository Services (OMRS).
        * **[audit-log-connectors](open-metadata-implementation/adapters/open-connectors/repository-services-connectors/audit-log-connectors)** supports different destinations for audit log messages.
        * **[cohort-registry-store-connectors](open-metadata-implementation/adapters/open-connectors/repository-services-connectors/cohort-registry-store-connectors)** contains connectors that store the cohort membership details used and maintained by the cohort registry.
        * **[open-metadata-archive-connectors](open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-archive-connectors)** contains connectors that can read and write open metadata archives.
        * **[open-metadata-collection-store-connectors](open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors)** contains connectors that support mappings to different vendors' metadata repositories.
          * **[graph-repository-connector](open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/graph-repository-connector)** - provides a local repository that uses a graph store as its persistence store.
          * **[inmemory-repository-connector](open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/inmemory-repository-connector)** - provides a local repository that is entirely in memory.  It is useful for testing/developing OMASs and demos.
          * **[omrs-rest-repository-connector](open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/omrs-rest-repository-connector)** - uses the OMRS REST API to call an open metadata compliant repository.
      * **[configuration-store-connectors](open-metadata-implementation/adapters/open-connectors/configuration-store-connectors)** contains the connectors that manage the open metadata configuration.
      * **[event-bus-connectors](open-metadata-implementation/adapters/open-connectors/event-bus-connectors)** supports different event/messaging infrastructures.  They can be plugged into the topic connectors from the access-service-connectors and repository-service-connectors.
      * **[governance-daemon-connectors](open-metadata-implementation/adapters/open-connectors/governance-daemon-connectors)** contains connectors for the governance daemon servers that monitor activity or synchronize metadata and configuration asynchronously between different tools.
      * **[data-store-connectors](open-metadata-implementation/adapters/open-connectors/data-store-connectors)** contain OCF connectors to data stores on different data platforms.
    * **[authentication-plugins](open-metadata-implementation/adapters/authentication-plugins)** support extensions to technology such as LDAP that are used to verify the identity of an individual or service requesting access to data/metadata.
    * **[governance-engines-plugins](open-metadata-implementation/adapters/governance-engines-plugins)** support plugins to governance engines to enable them to use open metadata settings in their validation and enforcement decisions, and the resulting actions they take.
  * **[frameworks](open-metadata-implementation/frameworks)** - frameworks that support pluggable components.
    * **[Open Connector Framework (OCF)](open-metadata-implementation/frameworks/open-connector-framework)** provides the interfaces and base implementations for components (called connectors) that access data-related assets. OCF connectors also provide detailed metadata about the assets they access.
    * **[Open Discovery Framework (ODF)](open-metadata-implementation/frameworks/open-discovery-framework)** provides the interfaces and base implementations for components (called discovery services) that access data-related assets and extract characteristics about the data that can be stored in an open metadata repository.
    * **[Governance Action Framework (GAF)](open-metadata-implementation/frameworks/governance-action-framework)** provides the interfaces and base implementations for components (called governance actions) that take action to correct a situation that is harmful the data, or the organization in some way.
  * **[governance-servers](open-metadata-implementation/governance-servers)** - servers and daemons to run open metadata and governance function.
    * **[server-chassis](open-metadata-implementation/governance-servers/server-chassis)** - the server chassis provides an "empty" server to host the open metadata services.
    * **[admin-services](open-metadata-implementation/governance-servers/admin-services)** - the admin services support the configuration of the open metadata server chassis.  This configuration determines which of the open metadata services are active.
  * **[repository-services](open-metadata-implementation/repository-services)** - metadata exchange and federation - aka the Open Metadata Repository Services (OMRS).
  * **[user-interfaces](open-metadata-implementation/user-interfaces)** - browser based user interfaces.
    * **[access-services-user-interface](open-metadata-implementation/user-interfaces/access-services-user-interface)** - provides a user interface that supports the user-facing Open Metadata Access Services (OMASs).
    * **[server-configuration-user-interface](open-metadata-implementation/user-interfaces/server-configuration-user-interface)** - provides the administration user interface for an open metadata server.
* **[open-metadata-compliance-suite](open-metadata-compliance-suite)** - implementation of the tests that determine if a vendor or open source technology is compliant with the open metadata and governance standards.
* **[open-metadata-distribution](open-metadata-distribution)** - contains scripts to extract the completed artifacts from the other modules and stores them together to make it easy to find them.
* **[open-metadata-publication](open-metadata-publication)** - contains scripts that send artifacts collected together by the open-metadata-distribution module to external parties.
* **[open-metadata-resources](open-metadata-resources)** - contains the open metadata demos and samples.

This content is maintained by the Egeria community.
To understand how to join and contribute see the 
[Community Guide](./Community-Guide.md).

## Egeria governance

This project aims to operate in a transparent, accessible way for the benefit
of the Egeria community.
All participation in this project is open and not
bound to any corporate affiliation.
Participants are bound the ODPi's [Code of Conduct](https://github.com/odpi/specs/wiki/ODPi-Code-of-Conduct).
The governance of the project is described in more detail in the
[Egeria Operations](./Egeria-Operations.md).