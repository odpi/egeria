<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

## ODPi Egeria Site organization

The Egeria content is organized across a number of repositories:

### Egeria Core repository

The Egeria core repository contains the core Egeria functionality, and is organized into the following modules:

* **[developer-resources](developer-resources)** - contains useful files and documentation for an Egeria developer.

* **[open-metadata-conformance-suite](open-metadata-conformance-suite)** - implementation of the tests that determine if a vendor or open source technology is compliant with the open metadata and governance standards.

* **[open-metadata-distribution](open-metadata-distribution)** - contains scripts to extract the completed artifacts from the other modules and stores them together to make it easy to find them.

* **[open-metadata-implementation](open-metadata-implementation)** - implementation of standards, frameworks and connectors.
  
  * **[access-services](open-metadata-implementation/access-services)** - domain specific APIs known as the Open Metadata Access Services (OMAS).
    * **[asset-consumer](open-metadata-implementation/access-services/asset-consumer)** - create connectors to access assets.
    * **[asset-manager](open-metadata-implementation/access-services/asset-manager)** - provide a synchronization interface for asset managers and catalogs.
    * **[asset-owner](open-metadata-implementation/access-services/asset-owner)** - manage metadata and feedback for owned assets.
    * **[community-profile](open-metadata-implementation/access-services/community-profile)** - manage personal profiles and communities.
    * **[data-manager](open-metadata-implementation/access-services/data-manager)** - exchange metadata with a technology that manages collections of data.
    * **[data-science](open-metadata-implementation/access-services/data-science)** - manage metadata for analytics.
    * **[design-model](open-metadata-implementation/access-services/design-model)** - manage content from design models.
    * **[digital-architecture](open-metadata-implementation/access-services/digital-architecture)** - support the definition of data standards and models.
    * **[digital-service](open-metadata-implementation/access-services/digital-service)** - manage metadata for a Digital Service.
    * **[governance-engine](open-metadata-implementation/access-services/governance-engine)** - manage metadata for an operational governance engine.
    * **[governance-program](open-metadata-implementation/access-services/governance-program)** - set up and manage a governance program.
    * **[it-infrastructure](open-metadata-implementation/access-services/it-infrastructure)** - manage metadata about deployed infrastructure.
    * **[project-management](open-metadata-implementation/access-services/project-management)** - manage definitions of projects for metadata management and governance.
    * **[security-manager](open-metadata-implementation/access-services/security-manager)** - synchronization of metadata with security services.
    * **[software-developer](open-metadata-implementation/access-services/software-developer)** - deliver useful metadata to software developers.
    * **[stewardship-action](open-metadata-implementation/access-services/stewardship-action)** - manage metadata as part of a data steward's work to improve the data landscape.
 
  * **[adapters](open-metadata-implementation/adapters)** - pluggable component implementations.
    * **[authentication-plugins](open-metadata-implementation/adapters/authentication-plugins)** support extensions to technology such as LDAP that are used to verify the identity of an individual or service requesting access to data/metadata.
    * **[open-connectors](open-metadata-implementation/adapters/open-connectors)** are connectors that support the Open Connector Framework (OCF).
      * **[configuration-store-connectors](open-metadata-implementation/adapters/open-connectors/configuration-store-connectors)** contains the connectors that manage the open metadata configuration.
      * **[connector-configuration-factory](open-metadata-implementation/adapters/open-connectors/connector-configuration-factory)** creates **Connection** objects to configure the open connectors.
      * **[data-store-connectors](open-metadata-implementation/adapters/open-connectors/data-store-connectors)** contain OCF connectors to data stores on different data platforms.
      * **[discovery-service-connectors](open-metadata-implementation/adapters/open-connectors/discovery-service-connectors)** contain ODF discovery service implementations.
      * **[dynamic-archiver-connectors](open-metadata-implementation/adapters/open-connectors/dynamic-archiver-connectors)** contains dynamic archiver services implementations.
      * **[event-bus-connectors](open-metadata-implementation/adapters/open-connectors/event-bus-connectors)** supports different event/messaging infrastructures.  They can be plugged into the topic connectors from the access-service-connectors and repository-service-connectors.
      * **[governance-action-connectors](open-metadata-implementation/adapters/open-connectors/governance-action-connectors)** contains GAF governance action implementations.
      * **[integration-connectors](open-metadata-implementation/adapters/open-connectors/integration-connectors)** contains connectors synchronously exchanging between different third party technologies.
      * **[repository-services-connectors](open-metadata-implementation/adapters/open-connectors/repository-services-connectors)** contains connector implementations for each type of connector supported by the Open Metadata Repository Services (OMRS).
        * **[audit-log-connectors](open-metadata-implementation/adapters/open-connectors/repository-services-connectors/audit-log-connectors)** supports different destinations for audit log messages.
        * **[cohort-registry-store-connectors](open-metadata-implementation/adapters/open-connectors/repository-services-connectors/cohort-registry-store-connectors)** contains connectors that store the cohort membership details used and maintained by the cohort registry.
        * **[open-metadata-archive-connectors](open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-archive-connectors)** contains connectors that can read and write open metadata archives.
        * **[open-metadata-collection-store-connectors](open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors)** contains connectors that support mappings to different vendors' metadata repositories.
          * **[graph-repository-connector](open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/graph-repository-connector)** - provides a local repository that uses a graph store as its persistence store.
          * **[inmemory-repository-connector](open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/inmemory-repository-connector)** - provides a local repository that is entirely in memory.  It is useful for testing/developing OMASs and demos.
          * **[omrs-rest-repository-connector](open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/omrs-rest-repository-connector)** - enables IBM Information Governance Catalog to support open metadata.
      * **[rest-client-connectors](open-metadata-implementation/adapters/open-connectors/rest-client-connectors)** contains connector implementations for issuing REST calls.
      * **[secrets-store-connectors](open-metadata-implementation/adapters/open-connectors/rest-client-connectors)** contains connector implementations for retrieving secrets from external sources.
      * **[file-survey-connectors](open-metadata-implementation/adapters/open-connectors/file-survey-connectors)** contains connector implementations for survey action services.
      * **[system-connectors](open-metadata-implementation/adapters/open-connectors/sustem-connectors)** contains connector implementations for issuing REST calls to different types of systems.
  
  * **[admin-services](open-metadata-implementation/admin-services)** - supports the configuration of the OMAG Server Platform.  This configuration determines which of the open metadata and governance services are active.
  
  * **[common-services](open-metadata-implementation/common-services)** - support modules that are reused by other services.
    * **[ffdc-services](open-metadata-implementation/common-services/ffdc-services)** - provides base classes and validation for First Failure Data Capture (FFDC).
    * **[generic-handlers](open-metadata-implementation/common-services/generic-handlers)** - provides support for metadata management for open metadata types including visibility and authorization checking, anchor management and provenance recording.
    * **[metadata-security](open-metadata-implementation/common-services/metadata-security)** - provides integration points for fine-grained security for metadata.
    * **[multi-tenant](open-metadata-implementation/common-services/multi-tenant)** - provides management of server instances within the OMAG Server Platform.
    * **[repository-handler](open-metadata-implementation/common-services/repository-handler)** - provides an enhanced set of services for accessing metadata from the [repository services](open-metadata-implementation/repository-services).
  
  * **[engine-services](open-metadata-implementation/engine-services)** - services that host data management and governance engines.  These run in the [Engine Host](https://egeria-project.org/concepts/engine-host) OMAG Server.
    * **[survey-action](open-metadata-implementation/engine-services/survey-action)** - services that host [Survey Action Framework (SAF)](open-metadata-implementation/frameworks/survey-action-framework) services.
    * **[repository-governance](open-metadata-implementation/engine-services/repository-governance)** - services that host dynamic governance services for open metadata repositories.
    * **[governance-action](open-metadata-implementation/engine-services/governance-action)** - services that host [Governance Action Framework (GAF)](open-metadata-implementation/frameworks/governance-action-framework) governance action services.

  * **[frameworks](open-metadata-implementation/frameworks)** - frameworks that support pluggable components.
    * **[audit-log-framework](open-metadata-implementation/frameworks/audit-log-framework)** provides the interfaces and base implementations for components (called connectors) that access data-related assets. OCF connectors also provide detailed metadata about the assets they access.
    * **[open-connector-framework](open-metadata-implementation/frameworks/open-connector-framework)** provides the interfaces for diagnostics and exceptions.
    * **[open-discovery-framework](open-metadata-implementation/frameworks/open-discovery-framework)** provides the interfaces and base implementations for components (called discovery services) that access data-related assets and extract characteristics about the data that can be stored in an open metadata repository.
    * **[governance-action-framework](open-metadata-implementation/frameworks/governance-action-framework)** provides the interfaces and base implementations for components (called governance actions) that take action to correct a situation that is harmful the data, or the organization in some way.
 
  * **[framework-services](open-metadata-implementation/framework-services)** - client support for the frameworks
    * **[ocf-metadata-management](open-metadata-implementation/framework-services/ocf-metadata-management)** - provides metadata management for the [Open Connector Framework (OCF)](open-metadata-implementation/frameworks/open-connector-framework) properties and APIs.
    * **[oif-metadata-management](open-metadata-implementation/framework-services/oif-metadata-management)** - provides metadata management for the [Open Integration Framework (OCF)](open-metadata-implementation/frameworks/open-integration-framework) properties and APIs.
    * **[gaf-metadata-management](open-metadata-implementation/framework-services/gaf-metadata-management)** - provides metadata management for the [Governance Action Framework (GAF)](open-metadata-implementation/frameworks/governance-action-framework) properties and APIs.

* **[governance-servers](open-metadata-implementation/governance-servers)** - servers and daemons to run open metadata and governance function.
    * **[engine-host-services](open-metadata-implementation/governance-servers/engine-host-services)** - supports the core function of the [Engine Host](https://egeria-project.org/concepts/engine-host) OMAG Server.
    * **[integration-daemon-services](open-metadata-implementation/governance-servers/integration-daemon-services)** - supports the core function of the [Integration Daemon](https://egeria-project.org/concepts/integration-daemon) OMAG Server.

  * **[integration-services](open-metadata-implementation/integration-services)** - services that host integration connectors.  These run in the [Integration Daemon](https://egeria-project.org/concepts/integration-daemon) OMAG Server.
    * **[analytics-integrator](open-metadata-implementation/integration-services/analytics-integrator)** - services to catalog analytics services.
    * **[api-integrator](open-metadata-implementation/integration-services/api-integrator)** - services to catalog APIs supported by API Managers.
    * **[catalog-integrator](open-metadata-implementation/integration-services/catalog-integrator)** - services to exchange metadata with asset managers and catalogs.
    * **[database-integrator](open-metadata-implementation/integration-services/database-integrator)** - services to capture metadata from file systems and file management applications.
    * **[files-integrator](open-metadata-implementation/integration-services/files-integrator)** - services to exchange metadata with relational databases.
    * **[infrastructure-integrator](open-metadata-implementation/integration-services/infrastructure-integrator)** - services to describe deployed IT infrastructure as metadata.
    * **[lineage-integrator](open-metadata-implementation/integration-services/lineage-integrator)** - services to load lineage metadata.
    * **[organization-integrator](open-metadata-implementation/integration-services/organization-integrator)** - services to load metadata about the organization, including user identifiers, profiles, team structures and department hierarchy.
    * **[search-integrator](open-metadata-implementation/integration-services/search-integrator)** - services to maintain search indexes about open metadata.
    * **[security-integrator](open-metadata-implementation/integration-services/security-integrator)** - services to push security tags to an external security enforcement point.
    * **[topic-integrator](open-metadata-implementation/integration-services/topic-integrator)** - services to catalog topics and their event types from an Event Broker.

  * **[platform-services](open-metadata-implementation/platform-services)** - the platform services support REST APIs for the OMAG Server Platform.

  * **[repository-services](open-metadata-implementation/repository-services)** - metadata exchange and federation - aka the Open Metadata Repository Services (OMRS).

  * **[platform-chassis](open-metadata-implementation/platform-chassis)** - the platform chassis provides the runtime framework for the OMAG Server Platform.
  * **[platform-services](open-metadata-implementation/platform-services)** - the services for querying the status of the OMAG Server Platform that can run multiple OMAG Servers simultaneously.
  * **[server-chassis](open-metadata-implementation/server-chassis)** - the service chassis provides the runtime framework for a single OMAG Server.  This is call the OMAG Server Runtime.
  * **[server-operations](open-metadata-implementation/server-operations)** - the services to start and stop OMAG Server's in either the OMAG Server Runtime or OMAG Server Platform.

  * **[user-interfaces](open-metadata-implementation/user-interfaces)** - browser based user interfaces.
  * **[user-security](open-metadata-implementation/user-security)** - support for authenticating users calling through the UIs.
 
  * **[view-services](open-metadata-implementation/view-services)** - services that provide services for user interfaces.  These run in the [View Server](https://egeria-project.org/concepts/view-server) OMAG Server.

* **[open-metadata-resources](open-metadata-resources)** - contains the open metadata demos and samples.
  * **[open-metadata-archives](open-metadata-resources/open-metadata-archives)** - contains pre-canned collections of metadata for loading into an open metadata server.
  * **[open-metadata-deployment](open-metadata-resources/open-metadata-deployment)** - contains resources for deploying Egeria and related technology.
  * **[open-metadata-samples](open-metadata-resources/open-metadata-samples)** - contains coding samples for Egeria APIs.

* **[open-metadata-test](open-metadata-test)** - supports additional test cases beyond unit test.

### Egeria connectors repositories

Egeria connectors repositories contain the code that allow non-Egeria technologies to connect into Egeria:

- [Hadoop Ecosystem connectors](https://github.com/odpi/egeria-connector-hadoop-ecosystem) houses the connectors for
    various Hadoop ecosystem components, like Apache Atlas and Apache Ranger.
- [IBM Information Server connectors](https://github.com/odpi/egeria-connector-ibm-information-server) houses the
    connectors for IBM Information Server components, like Information Governance Catalog, DataStage and Information
    Analyzer.
- [Database connectors](https://github.com/odpi/egeria-database-connectors)    

### Egeria samples repository

The samples repository contains samples of using Egeria in various ways: Ansible roles, sample data and metadata,
documented demonstrations, and so on at https://github.com/odpi/egeria-samples

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
