<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Egeria's Connector Catalog

Egeria has a growing collection of [connectors](../developer-guide/what-is-a-connector.md) to third party technologies.
These connectors help to accelerate the rollout of your open metadata ecosystem.

## Connectors supported by the Egeria OMAG Servers

Many subsystems in Egeria's 
[OMAG Server Platform and Servers](../../../open-metadata-implementation/admin-services/docs/concepts/omag-server-platform.md) 
use connectors to plug in additional function through connector. 
These subsystems define a specialized interface for the type of connector they support.

* One or more connector implementations supporting that interface are then written either by the Egeria community or
other organizations.

* When an Egeria [OMAG Server](../../../open-metadata-implementation/admin-services/docs/concepts/omag-server.md) is configured, 
details of which connector implementation to use is specified in the
server's 
[configuration document](../../../open-metadata-implementation/admin-services/docs/concepts/configuration-document.md).  

* At start up, the OMAG server passes the connector configuration to the OCF to
instantiate the required connector instance.  Connectors enable Egeria to operate in many environments
and with many types of third party technologies, just by managing the configuration of the OMAG servers.

The connectors listed below are available for use ...

### Integration Connectors

The integration connectors support the exchange of metadata with third party technologies.  This exchange
may be inbound, outbound, synchronous, polling or event-driven.  Details of how integration connector work is
described in [this article](../../../open-metadata-implementation/governance-servers/integration-daemon-services/docs/integration-connector.md).

#### Files

The files integration connectors run in the 
[Files Integrator Open Metadata Integration Service (OMIS)](../../../open-metadata-implementation/integration-services/files-integrator)
hosted in the [Integration Daemon](../../../open-metadata-implementation/admin-services/docs/concepts/integration-daemon.md).

* The [Data Files Monitor Integration Connector](data-files-monitor-integration-connector.md)
  maintains a DataFile asset for each file in the directory (or any subdirectory).
  When a new file is created, a new DataFile asset is created.  If a file is modified, the lastModified property
  of the corresponding DataFile asset is updated.  When a file is deleted, its corresponding DataFile asset is also deleted 
  (or archived if it is still needed for lineage).

* The [DataFolderMonitorIntegrationConnector](data-folder-monitor-integration-connector.md)
  maintains a DataFolder asset for the directory.  The files and directories
  underneath it are assumed to be elements/records in the DataFolder asset and so each time there is a change to the
  files and directories under the monitored directory, it results in an update to the lastModified property
  of the corresponding DataFolder asset.

#### Databases

The database integration connectors run in the 
[Database Integrator Open Metadata Integration Service (OMIS)](../../../open-metadata-implementation/integration-services/database-integrator)
hosted in the [Integration Daemon](../../../open-metadata-implementation/admin-services/docs/concepts/integration-daemon.md).

* The [PostgreSQL database integration connector](https://github.com/odpi/egeria-database-connectors/tree/main/postgres-connector)
  automatically maintains the open metadata instances for the databases hosted on a [PostgreSQL server](https://www.postgresql.org).
  This includes the database schemas, tables, columns, primary keys and foreign keys.

#### Security Enforcement Engines

The security integration connectors run in the 
[Security Integrator Open Metadata Integration Service (OMIS)](../../../open-metadata-implementation/integration-services/security-integrator)
hosted in the [Integration Daemon](../../../open-metadata-implementation/admin-services/docs/concepts/integration-daemon.md).

* The [Apache Ranger Integration Connector](../../../open-metadata-implementation/adapters/open-connectors/governance-daemon-connectors/security-sync-connectors/ranger-connector)
  pushes assets and schemas marked up with tags and labels from the SecurityTag classification to Apache Ranger
  to use in security enforcement policies.

### Open Discovery Services

[Open Discovery Services](../../../open-metadata-implementation/frameworks/open-discovery-framework/docs/discovery-service.md) 
are connectors that analyze the content of resources in the digital landscape and create annotations
that are attached to the resource's Asset metadata element in the open metadata repositories.

The interfaces used by a discovery service are defined in
the [Open Discovery Framework (ODF)](../../../open-metadata-implementation/frameworks/open-discovery-framework)
along with a guide on how to write a discovery service.

* [CSV Discovery Service](../../../open-metadata-implementation/adapters/open-connectors/discovery-service-connectors)
  extracts the column names from the first line of the file, counts up the number of records in the file
  and extracts its last modified time.
  
* [Sequential Discovery Pipeline](../../../open-metadata-implementation/adapters/open-connectors/discovery-service-connectors)
  runs nested discovery services in a sequence. 
  [More information on discovery pipelines](../../../open-metadata-implementation/frameworks/open-discovery-framework/docs/discovery-pipeline.md).
      

### Governance Action Services

[Governance Action Services](../../../open-metadata-implementation/frameworks/governance-action-framework/docs/governance-action-service.md) 
are connectors that perform monitoring of metadata changes, validation of metadata, triage of issues, 
assessment and/or remediation activities on request.
They run in the
[Governance Action Open Metadata Engine Service (OMES)](../../../open-metadata-implementation/engine-services/governance-action)
hosted by the
[Engine Host OMAG Server](../../../open-metadata-implementation/admin-services/docs/concepts/engine-host.md).

* [Generic Element Watchdog Governance Action Service](generic-element-watchdog-governance-action-service.md)
  listens for changing metadata elements and initiates governance action processes when certain events occur.
  
* [Generic Folder Watchdog Governance Action Service](generic-folder-watchdog-governance-action-service.md)
  listens for changing assets linked to a DataFolder element.  This may be for DataFiles directly linked to the folder or
  in sub-folders.  It initiates governance action processes when specific events occur.
  
* [Move/Copy File Provisioning Governance Action Service](move-copy-file-provisioning-governance-action-service.md)
  moves or copied files from one location to another and maintains the lineage of the action.
  
* [Origin Seeker Remediation Governance Action Service](origin-seeker-remediation-governance-action-service.md) walks
  backwards through the lineage mappings to 

### Event Bus Connectors

The event bus connectors are used by Egeria to connect to an event bus to support event exchange
in topics.

* The [Kafka Open Metadata Topic Connector](../../../open-metadata-implementation/adapters/open-connectors/event-bus-connectors/open-metadata-topic-connectors/kafka-open-metadata-topic-connector) implements 
an [Apache Kafka](https://kafka.apache.org/) connector for a topic that exchanges
Java Objects as JSON payloads.

### Repository and Event Mapper Connectors

The repository connectors provide the ability to integrate a third party metadata repository
into an [open metadata repository cohort](../../../open-metadata-implementation/repository-services/docs/open-metadata-repository-cohort.md).

* The [JanusGraph OMRS Repository Connector](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/graph-repository-connector)
  provides a native repository for a [Metadata Server](../../../open-metadata-implementation/admin-services/docs/concepts/metadata-server.md).
  
* The [In-memory OMRS Repository Connector](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/inmemory-repository-connector)
  provides a simple native repository implementation that "stores" metadata in hash maps within the JVM. 
  It is used for testing, or for environments where metadata maintained in other repositories
  needs to be cached locally for performance/scalability reasons.

* The [Read-only OMRS Repository Connector](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/inmemory-repository-connector)
  provides a native repository implementation 
  that does not support the interfaces for create, update, delete.  However it does support the search interfaces
  and is able to cache metadata.  This means it can be loaded with open metadata archives to provide
  standard metadata definitions.
  
* The [Apache Atlas OMRS Repository Connector](https://github.com/odpi/egeria-connector-hadoop-ecosystem) 
  implements read-only connectivity to the Apache Atlas metadata repository.
  
* The [IBM Information Governance Catalog (IGC) OMRS Repository Connector](https://github.com/odpi/egeria-connector-ibm-information-server)
  implements read-only connectivity to the metadata repository within the 
  [IBM InfoSphere Information Server](https://www.ibm.com/analytics/information-server) suite.

### Audit Log Connectors

The audit log store connectors support different destinations for audit log record.
Multiple of these connectors can be [active in an OMAG Server](../../../open-metadata-implementation/admin-services/docs/user/configuring-the-audit-log.md)
at any one time and they can each be configured to only process particular types of audit log records.

* [Console Audit Log Connector](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/audit-log-connectors/audit-log-console-connector)
  writes selected parts of each audit log record to stdout.

* [slf4j Audit Log Connector](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/audit-log-connectors/audit-log-slf4j-connector)
  writes full log records to the slf4j ecosystem.

* [File Audit Log Connector](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/audit-log-connectors/audit-log-file-connector)
  creates log records as JSON files in a shared directory.

* [Event Topic Audit Log Connector](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/audit-log-connectors/audit-log-event-topic-connector)
  sends each log record as an event on the supplied event topic.

There is more information on the use of audit logging in the [Diagnostic Guide](../diagnostic-guide).

### Cohort Registry Stores

The cohort registry store connectors are connectors that store the
[open metadata repository cohort](../../../open-metadata-implementation/repository-services/docs/open-metadata-repository-cohort.md)
membership details used and maintained by the [cohort registry](../../../open-metadata-implementation/repository-services/docs/component-descriptions/cohort-registry.md).
The cohort protocols are peer-to-peer and hence there is a cohort registry
(with a [cohort registry store](../../../open-metadata-implementation/repository-services/docs/component-descriptions/connectors/cohort-registry-store-connector.md))
for each [member of a cohort](../../../open-metadata-implementation/admin-services/docs/concepts/cohort-member.md).

Egeria provides a single implementation of a
cohort registry store connector:

* [cohort-registry-file-store-connector](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/cohort-registry-store-connectors/cohort-registry-file-store-connector)
  provides the means to store the cohort registry membership details as a JSON file.
  
### Open Metadata Archive Stores

The open metadata archive connectors support connectors that can
read and write [open metadata archives](../../../open-metadata-resources/open-metadata-archives).

Egeria provides a single implementation of
this type of connector:

* [open-metadata-archive-file-connector](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-archive-connectors/open-metadata-archive-file-connector)
 stores an open metadata archive as a JSON file.
  
  
## Connectors for use in Applications and Tools

These connectors are for use by external applications and tools to connect with resources 
and services in the digital landscape along with stored metadata from Egeria.

### Files

* The [Avro file connector](../../../open-metadata-implementation/adapters/open-connectors/data-store-connectors/file-connectors/avro-file-connector) 
  provides access to an Avro file that has been catalogued using open metadata.
  
* The [basic file connector](../../../open-metadata-implementation/adapters/open-connectors/data-store-connectors/file-connectors/basic-file-connector) 
  provides support to read and write to a file using the Java File object.  

* The [CSV file connector](../../../open-metadata-implementation/adapters/open-connectors/data-store-connectors/file-connectors/csv-file-connector) 
  is able to retrieve data from a Comma Separated Values (CSV) file where the contents are stored in logical columns
  with a special character delimiter between the columns.
  
* The [data folder connector](../../../open-metadata-implementation/adapters/open-connectors/data-store-connectors/file-connectors/data-folder-connector)
  is for accessing data that is
  stored as a number of files within a folder (directory).
  
### Databases

 
* The [Gaian connector](../../../open-metadata-implementation/adapters/open-connectors/data-store-connectors/gaian-connector)
  provides a JDBC style connector to the [Gaian](https://github.com/gaiandb/gaiandb) virtualization engine.


----
* Learn how to [write your own connector](../developer-guide/what-is-a-connector.md)
* Return to [Home Page](../../../index.md)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.