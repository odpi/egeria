<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Egeria's Connector Catalog

Egeria has a growing collection of [connectors](../developer-guide/what-is-a-connector.md) to third party technologies.
These connectors help to accelerate the rollout of your open metadata ecosystem.


## Connectors supported by the Egeria OMAG Servers

### Integration Connectors

The integration connectors support the exchange of metadata with third party technologies.





### Event bus connectors

The event bus connectors are used by Egeria to connect to an event bus to support event exchange
in topics.

* The [Kafka Open Metadata Topic Connector](../../../open-metadata-implementation/adapters/open-connectors/event-bus-connectors/open-metadata-topic-connectors/kafka-open-metadata-topic-connector) implements 
an [Apache Kafka](https://kafka.apache.org/) connector for a topic that exchanges
Java Objects as JSON payloads.

### Repository and event mapper Connectors

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
  
* The [Apache Atlas OMRS Repository Connector]  
  
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