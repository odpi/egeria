<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Platform report for: https://localhost:9443

## Platform deployment
* **Egeria version**: Egeria OMAG Server Platform (version 5.1-SNAPSHOT)
### Configuration document store connector
* **Implementation**: org.odpi.openmetadata.adapters.adminservices.configurationstore.file.FileBasedServerConfigStoreProvider
* **Location**: data/servers/{0}/config/{0}.config
### Platform security connector
* **Implementation**: org.odpi.openmetadata.metadatasecurity.samples.CocoPharmaPlatformSecurityProvider
* **Location**: *null*
## Registered services
* **Asset Owner OMAS**: Manage an asset
* **Data Manager OMAS**: Capture changes to the data stores and data set managed by a data manager such as a database server, content manager or file system.
* **Stewardship Action OMAS**: Manage exceptions and actions from open governance.
* **Governance Program OMAS**: Manage the governance program.
* **Digital Service OMAS**: Manage a digital service through its lifecycle.
* **Asset Lineage OMAS**: Publish asset lineage
* **Design Model OMAS**: Exchange design model content with tools and standard packages.
* **Asset Manager OMAS**: Manage metadata from a third party asset manager
* **Asset Consumer OMAS**: Access assets through connectors.
* **IT Infrastructure OMAS**: Manage information about the deployed IT infrastructure.
* **Data Science OMAS**: Create and manage data science definitions and models.
* **Community Profile OMAS**: Define personal profile and collaborate.
* **Software Developer OMAS**: Interact with software development tools.
* **Project Management OMAS**: Manage governance related projects.
* **Governance Engine OMAS**: Provide metadata services and watch dog notification to the governance action services.
* **Digital Architecture OMAS**: Design of the digital services for an organization
* **Governance Server OMAS**: Supply the governance engine definitions to the engine hosts and the and integration group definitions to the integration daemons.
* **Security Manager OMAS**: Manages exchange of metadata with a security service.
* **Governance Action OMES**: Executes requested governance action services to monitor, assess and maintain metadata and its real-world counterparts.
* **Repository Governance OMES**: Dynamically govern open metadata repositories in the connected cohorts.
* **Survey Action OMES**: Analyses the content of an asset's real world counterpart (resource), generates annotations in a survey report that is attached to the asset in the open metadata repositories.
* **Infrastructure Integrator OMIS**: Exchange information relating to IT infrastructure such as hosts, platforms, servers, server capabilities and services.
* **Files Integrator OMIS**: Extract metadata about files stored in a file system or file manager.
* **Topic Integrator OMIS**: Exchange metadata with third party event-based brokers.
* **Security Integrator OMIS**: Distribute security properties to security enforcement points.
* **API Integrator OMIS**: Exchange metadata with third party API Gateways.
* **Lineage Integrator OMIS**: Manage exchange of lineage with a third party tool.
* **Database Integrator OMIS**: Extract metadata such as schema, tables and columns from database managers.
* **Catalog Integrator OMIS**: Exchange metadata with third party data catalogs.
* **Display Integrator OMIS**: Exchange metadata with applications that display data to users.
* **Analytics Integrator OMIS**: Exchange metadata with third party analytics tools.
* **Organization Integrator OMIS**: Load information about the teams and people in an organization and return collaboration activity.
* **Classification Manager OMVS**: Maintain classifications and relationships used to organize open metadata elements.
* **Collection Manager OMVS**: Build collections of asset and other metadata.
* **Data Discovery OMVS**: Define and search for new data resources.
* **Feedback Manager OMVS**: Work with note logs, comments, informal tags, ratings/reviews and likes.
* **Reference Data OMVS**: Work with code tables and associated reference data.
* **My Profile OMVS**: Manage information about the logged on user as well as their preferences.
* **Action Author OMVS**: Maintain definitions of governance actions such as governance action processes and governance action types.
* **Glossary Browser OMVS**: View glossary terms and categories within a glossary.
* **Asset Catalog OMVS**: Search and understand your assets.
* **Runtime Manager OMVS**: Retrieve configuration and status from platforms and servers.
* **Template Manager OMVS**: Retrieve and refine the templates for use during cataloguing.
* **Automated Curation OMVS**: Manage Egeria's automation services.
* **Project Manager OMVS**: Set up and manage projects.
* **Glossary Manager OMVS**: Create glossary terms and organize them into categories as part of a controlled workflow process. It supports the editing glossary and multiple states.
* **Valid Metadata OMVS**: Maintain and query valid values for metadata.
## Platform servers
### Server: simple-metadata-store
* **Type**: Metadata Access Store
* **Description**: A metadata store that supports Open Metadata Access Services (OMASs) without event notifications.
* **UserId**: simplenpa
#### Local Repository
* **Local Repository Mode**: Open Metadata Native
##### Local Repository Connector
* **Implementation**: org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector.InMemoryOMRSRepositoryConnectorProvider
* **Location**: *null*
##### Local Repository Remote Connector
* **Implementation**: org.odpi.openmetadata.adapters.repositoryservices.rest.repositoryconnector.OMRSRESTRepositoryConnectorProvider
* **Location**: {{egeriaEndpoint}}/servers/simple-metadata-store
#### Runtime Status
* **Last Start Time**: Wed Jun 19 12:08:02 BST 2024
* **Server Active Status**: Running
#### Services
##### Service: Digital Service OMAS
* **Service Status**: Running
##### Service: Governance Action Framework Services
* **Service Status**: Running
##### Service: Open Metadata Repository Services (OMRS)
* **Service Status**: Running
##### Service: Community Profile OMAS
* **Service Status**: Running
##### Service: Stewardship Action OMAS
* **Service Status**: Running
##### Service: Asset Consumer OMAS
* **Service Status**: Running
##### Service: Software Developer OMAS
* **Service Status**: Running
##### Service: Asset Lineage OMAS
* **Service Status**: Running
##### Service: Data Science OMAS
* **Service Status**: Running
##### Service: IT Infrastructure OMAS
* **Service Status**: Running
##### Service: Asset Owner OMAS
* **Service Status**: Running
##### Service: Security Manager OMAS
* **Service Status**: Running
##### Service: Design Model OMAS
* **Service Status**: Running
##### Service: Asset Manager OMAS
* **Service Status**: Running
##### Service: Connected Asset Services
* **Service Status**: Running
##### Service: Data Manager OMAS
* **Service Status**: Running
##### Service: Digital Architecture OMAS
* **Service Status**: Running
##### Service: Governance Program OMAS
* **Service Status**: Running
##### Service: Project Management OMAS
* **Service Status**: Running
##### Service: Governance Engine OMAS
* **Service Status**: Running
##### Service: Open Integration Service
* **Service Status**: Running
##### Service: Governance Server OMAS
* **Service Status**: Running
### Server: active-metadata-store
* **Type**: Metadata Access Store
* **Description**: A metadata store that supports Open Metadata Access Services (OMASs) with event notifications.  It provides metadata to view-server, engine-host and integration-daemon.
* **UserId**: activenpa
#### Local Repository
* **Local Repository Mode**: Open Metadata Native
##### Local Repository Connector
* **Implementation**: org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnectorProvider
* **Location**: *null*
* **Configuration Properties**: {xtdbConfig={xtdb.lucene/lucene-store={db-dir=data/servers/active-metadata-store/repository/xtdb-kv/lucene}, xtdb/tx-log={kv-store={db-dir=data/servers/active-metadata-store/repository/xtdb-kv/rdb-tx, xtdb/module=xtdb.rocksdb/->kv-store}}, xtdb/index-store={kv-store={db-dir=data/servers/active-metadata-store/repository/xtdb-kv/rdb-index, xtdb/module=xtdb.rocksdb/->kv-store}}, xtdb/document-store={kv-store={db-dir=data/servers/active-metadata-store/repository/xtdb-kv/rdb-docs, xtdb/module=xtdb.rocksdb/->kv-store}}}}
##### Local Repository Remote Connector
* **Implementation**: org.odpi.openmetadata.adapters.repositoryservices.rest.repositoryconnector.OMRSRESTRepositoryConnectorProvider
* **Location**: {{egeriaEndpoint}}/servers/active-metadata-store
#### Runtime Status
* **Last Start Time**: Wed Jun 19 12:08:00 BST 2024
* **Server Active Status**: Running
#### Services
##### Service: Digital Service OMAS
* **Service Status**: Running
##### Service: Governance Action Framework Services
* **Service Status**: Running
##### Service: Open Metadata Repository Services (OMRS)
* **Service Status**: Running
##### Service: Community Profile OMAS
* **Service Status**: Running
##### Service: Stewardship Action OMAS
* **Service Status**: Running
##### Service: Asset Consumer OMAS
* **Service Status**: Running
##### Service: Software Developer OMAS
* **Service Status**: Running
##### Service: Asset Lineage OMAS
* **Service Status**: Running
##### Service: Data Science OMAS
* **Service Status**: Running
##### Service: IT Infrastructure OMAS
* **Service Status**: Running
##### Service: Asset Owner OMAS
* **Service Status**: Running
##### Service: Security Manager OMAS
* **Service Status**: Running
##### Service: Design Model OMAS
* **Service Status**: Running
##### Service: Asset Manager OMAS
* **Service Status**: Running
##### Service: Connected Asset Services
* **Service Status**: Running
##### Service: Data Manager OMAS
* **Service Status**: Running
##### Service: Digital Architecture OMAS
* **Service Status**: Running
##### Service: Governance Program OMAS
* **Service Status**: Running
##### Service: Project Management OMAS
* **Service Status**: Running
##### Service: Governance Engine OMAS
* **Service Status**: Running
##### Service: Open Integration Service
* **Service Status**: Running
##### Service: Governance Server OMAS
* **Service Status**: Running
### Server: view-server
* **Type**: View Server
* **Description**: A server that supplies REST API endpoints for User Interfaces and non-Java environments such as Python.
* **UserId**: viewnpa
#### Runtime Status
* **Last Start Time**: Wed Jun 19 12:08:01 BST 2024
* **Server Active Status**: Running
#### Services
##### Service: Template Manager OMVS
* **Service Status**: Running
###### Partner Service:
* **Partner Server**: active-metadata-store
* **Partner URL root**: {{egeriaEndpoint}}
* **Calling Service Name**: *null*
##### Service: Open Metadata Repository Services (OMRS)
* **Service Status**: Running
##### Service: Feedback Manager OMVS
* **Service Status**: Running
###### Partner Service:
* **Partner Server**: active-metadata-store
* **Partner URL root**: {{egeriaEndpoint}}
* **Calling Service Name**: *null*
##### Service: Runtime Manager OMVS
* **Service Status**: Running
###### Partner Service:
* **Partner Server**: active-metadata-store
* **Partner URL root**: {{egeriaEndpoint}}
* **Calling Service Name**: *null*
##### Service: Collection Manager OMVS
* **Service Status**: Running
###### Partner Service:
* **Partner Server**: active-metadata-store
* **Partner URL root**: {{egeriaEndpoint}}
* **Calling Service Name**: *null*
##### Service: Reference Data OMVS
* **Service Status**: Running
###### Partner Service:
* **Partner Server**: active-metadata-store
* **Partner URL root**: {{egeriaEndpoint}}
* **Calling Service Name**: *null*
##### Service: Action Author OMVS
* **Service Status**: Running
###### Partner Service:
* **Partner Server**: active-metadata-store
* **Partner URL root**: {{egeriaEndpoint}}
* **Calling Service Name**: *null*
##### Service: Glossary Browser OMVS
* **Service Status**: Running
###### Partner Service:
* **Partner Server**: active-metadata-store
* **Partner URL root**: {{egeriaEndpoint}}
* **Calling Service Name**: *null*
##### Service: My Profile OMVS
* **Service Status**: Running
###### Partner Service:
* **Partner Server**: active-metadata-store
* **Partner URL root**: {{egeriaEndpoint}}
* **Calling Service Name**: *null*
##### Service: Project Manager OMVS
* **Service Status**: Running
###### Partner Service:
* **Partner Server**: active-metadata-store
* **Partner URL root**: {{egeriaEndpoint}}
* **Calling Service Name**: *null*
##### Service: Asset Catalog OMVS
* **Service Status**: Running
###### Partner Service:
* **Partner Server**: active-metadata-store
* **Partner URL root**: {{egeriaEndpoint}}
* **Calling Service Name**: *null*
##### Service: Data Discovery OMVS
* **Service Status**: Running
###### Partner Service:
* **Partner Server**: active-metadata-store
* **Partner URL root**: {{egeriaEndpoint}}
* **Calling Service Name**: *null*
##### Service: Automated Curation OMVS
* **Service Status**: Running
###### Partner Service:
* **Partner Server**: active-metadata-store
* **Partner URL root**: {{egeriaEndpoint}}
* **Calling Service Name**: *null*
##### Service: Valid Metadata OMVS
* **Service Status**: Running
###### Partner Service:
* **Partner Server**: active-metadata-store
* **Partner URL root**: {{egeriaEndpoint}}
* **Calling Service Name**: *null*
##### Service: Glossary Manager OMVS
* **Service Status**: Running
###### Partner Service:
* **Partner Server**: active-metadata-store
* **Partner URL root**: {{egeriaEndpoint}}
* **Calling Service Name**: *null*
##### Service: Classification Manager OMVS
* **Service Status**: Running
###### Partner Service:
* **Partner Server**: active-metadata-store
* **Partner URL root**: {{egeriaEndpoint}}
* **Calling Service Name**: *null*
### Server: engine-host
* **Type**: Engine Host Server
* **Description**: A server that runs governance service requests, triggered by engine actions created in active-metadata-store.
* **UserId**: enginenpa
#### Runtime Status
* **Last Start Time**: Wed Jun 19 12:08:01 BST 2024
* **Server Active Status**: Running
#### Services
##### Service: Survey Action OMES
* **Service Status**: Running
##### Service: Open Metadata Repository Services (OMRS)
* **Service Status**: Running
##### Service: Engine Host Services
* **Service Status**: Running
##### Service: Governance Action OMES
* **Service Status**: Running
##### Service: Repository Governance OMES
* **Service Status**: Running
### Server: integration-daemon
* **Type**: Integration Daemon
* **Description**: A server that runs integration connectors that synchronize and exchange metadata with different types of technologies and tools.
* **UserId**: daemonnpa
#### Runtime Status
* **Last Start Time**: Wed Jun 19 12:08:01 BST 2024
* **Server Active Status**: Running
#### Services
##### Service: Open Metadata Repository Services (OMRS)
* **Service Status**: Running
##### Service: Integration Daemon Services
* **Service Status**: Running

--8<-- "snippets/abbr.md"