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

  * **[access-services](open-metadata-implementation/access-services)** - client support for the frameworks
    * **[ocf-metadata-management](open-metadata-implementation/access-services/ocf-metadata-management)** - provides metadata management for the [Open Connector Framework (OCF)](open-metadata-implementation/frameworks/open-connector-framework) properties and APIs.
    * **[omf-metadata-management](open-metadata-implementation/access-services/omf-metadata-management)** - provides metadata management for the [Open Metadata Framework (OMF)](open-metadata-implementation/frameworks/open-metadata-framework) properties and APIs.
    * **[gaf-metadata-management](open-metadata-implementation/access-services/gaf-metadata-management)** - provides metadata management for the [Open Governance Framework (OGF)](open-metadata-implementation/frameworks/open-governance-framework) properties and APIs.

  * **[adapters](open-metadata-implementation/adapters)** - pluggable component implementations.
    * **[authentication-plugins](open-metadata-implementation/adapters/authentication-plugins)** support extensions to technology such as LDAP that are used to verify the identity of an individual or service requesting access to data/metadata.
    * **[open-connectors](open-metadata-implementation/adapters/open-connectors)** are connectors that support the Open Connector Framework (OCF).
      * **[configuration-store-connectors](open-metadata-implementation/adapters/open-connectors/configuration-store-connectors)** contains the connectors that manage the open metadata configuration.
      * **[connector-configuration-factory](open-metadata-implementation/adapters/open-connectors/connector-configuration-factory)** creates **Connection** objects to configure the open connectors.
      * **[data-store-connectors](open-metadata-implementation/adapters/open-connectors/data-store-connectors)** contain OCF connectors to data stores on different data platforms.
      * **[discovery-service-connectors](open-metadata-implementation/adapters/open-connectors/discovery-service-connectors)** contain ODF discovery service implementations.
      * **[dynamic-archiver-connectors](open-metadata-implementation/adapters/open-connectors/dynamic-archiver-connectors)** contains dynamic archiver services implementations.
      * **[event-bus-connectors](open-metadata-implementation/adapters/open-connectors/event-bus-connectors)** supports different event/messaging infrastructures.  They can be plugged into the topic connectors from the access-service-connectors and repository-service-connectors.
      * **[governance-action-connectors](open-metadata-implementation/adapters/open-connectors/governance-action-connectors)** contains governance action implementations.
      * **[integration-connectors](open-metadata-implementation/adapters/open-connectors/integration-connectors)** contains connectors synchronously exchanging between different third party technologies.
      * **[repository-services-connectors](open-metadata-implementation/adapters/open-connectors/repository-services-connectors)** contains connector implementations for each type of connector supported by the Open Metadata Repository Services (OMRS).
        * **[audit-log-connectors](open-metadata-implementation/adapters/open-connectors/repository-services-connectors/audit-log-connectors)** supports different destinations for audit log messages.
        * **[cohort-registry-store-connectors](open-metadata-implementation/adapters/open-connectors/repository-services-connectors/cohort-registry-store-connectors)** contains connectors that store the cohort membership details used and maintained by the cohort registry.
        * **[open-metadata-archive-connectors](open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-archive-connectors)** contains connectors that can read and write open metadata archives.
        * **[open-metadata-collection-store-connectors](open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors)** contains connectors that support mappings to different vendors' metadata repositories.
          * **[graph-repository-connector](open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/graph-repository-connector)** - provides a local repository that uses a graph store as its persistence store.
          * **[inmemory-repository-connector](open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/inmemory-repository-connector)** - provides a local repository that is entirely in memory.  It is useful for testing/developing OMASs and demos.
          * **[postgres-repository-connector](open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/postgres-repository-connector)** - provides a local repository that is soring its metadata in a postgres database.  Each server/repository has its own database schema.
          * **[xtdb-repository-connector](open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/xtdb-repository-connector)** - provides a local repository built on the XTDB repository.  This allows different types of storage to be used.
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
    * **[governance-action](open-metadata-implementation/engine-services/governance-action)** - services that host [Open Governance Framework (OGF)](open-metadata-implementation/frameworks/open-governance-framework) governance action services.
    * **[survey-action](open-metadata-implementation/engine-services/survey-action)** - services that host [Open Survey Framework (OSF)](open-metadata-implementation/frameworks/open-survey-framework) services.
    * **[watchdog-action](open-metadata-implementation/engine-services/watchdog-action)** - services that host [Open Watchdog Framework (OWF)](open-metadata-implementation/frameworks/open-watchdog-framework) services.
    * **[repository-governance](open-metadata-implementation/engine-services/repository-governance)** - services that host dynamic governance services for open metadata repositories.

  * **[frameworks](open-metadata-implementation/frameworks)** - frameworks that support pluggable components.
    * **[audit-log-framework](open-metadata-implementation/frameworks/audit-log-framework)** provides the interfaces and base implementations for components (called connectors) that access data-related assets. OCF connectors also provide detailed metadata about the assets they access.
    * **[open-connector-framework](open-metadata-implementation/frameworks/open-connector-framework)** provides the interfaces for diagnostics and exceptions.
    * **[open-governance-framework](open-metadata-implementation/frameworks/open-governance-framework)** provides the interfaces and base implementations for components (called governance actions) that take action to correct a situation that is harmful the data, or the organization in some way.
    * **[open-survey-framework](open-metadata-implementation/frameworks/open-survey-framework)** provides the interfaces and base implementations for components (called survey action services) that access data-related assets and extract characteristics about the data that can be stored in an open metadata repository.
    * **[open-watchdog-framework](open-metadata-implementation/frameworks/open-watchdog-framework)** provides the interfaces and base implementations for components (called watchdog action services) that monitor for events and issue notification/actions to subscribers.
 
* **[governance-servers](open-metadata-implementation/governance-servers)** - servers and daemons to run open metadata and governance function.
    * **[engine-host-services](open-metadata-implementation/governance-servers/engine-host-services)** - supports the core function of the [Engine Host](https://egeria-project.org/concepts/engine-host) OMAG Server.
    * **[integration-daemon-services](open-metadata-implementation/governance-servers/integration-daemon-services)** - supports the core function of the [Integration Daemon](https://egeria-project.org/concepts/integration-daemon) OMAG Server.

  * **[platform-services](open-metadata-implementation/platform-services)** - the platform services support REST APIs for the OMAG Server Platform.

  * **[repository-services](open-metadata-implementation/repository-services)** - metadata exchange and federation - aka the Open Metadata Repository Services (OMRS).

  * **[platform-chassis](open-metadata-implementation/platform-chassis)** - the platform chassis provides the runtime framework for the OMAG Server Platform.
  * **[platform-services](open-metadata-implementation/platform-services)** - the services for querying the status of the OMAG Server Platform that can run multiple OMAG Servers simultaneously.
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
