<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2019. -->

# Working with connectors

An **open connector** is a Java client to an [Asset](../../../docs/concepts/assets) that implements the **Connector** interface
defined in the [Open Connector Framework (OCF)](../../../../frameworks/open-connector-framework).
It has 2 parts to its interface:

* The specialized interface to work with the specific contents of the asset.  For example, if the connector
  was for data stored in a relational database, this interface would probably follow the 
  [Java Database Connectivity (JDBC)](https://en.wikipedia.org/wiki/Java_Database_Connectivity) specification.
  The documentation for this interface is found with the specific connector.  These are the connectors
  supported by ODPi Egeria with links to the documentation:
   
    * **[basic-file-connector](../../../../adapters/open-connectors/data-store-connectors/file-connectors/basic-file-connector)** provides connector to read a file.
      It has no special knowledge of the format.

    * **[avro-file-connector](../../../../adapters/open-connectors/data-store-connectors/file-connectors/avro-file-connector)** provides connector to read files
      that have an [Apache Avro](https://avro.apache.org/) format.

    * **[csv-file-connector](../../../../adapters/open-connectors/data-store-connectors/file-connectors/csv-file-connector)** provides connector to read files
      that have a CSV tabular format.

    * **[data-folder-connector](../../../../adapters/open-connectors/data-store-connectors/file-connectors/data-folder-connector)** provides connector to read a data set that is made up of many files
      stored within a data folder.

* A generalized interface to extract all of the open metadata known about the asset.  This is referred to
  as the **connected asset properties**.  This interface is documented [here](retrieving-asset-properties.md).
  
An application creates a connector using the [Asset Consumer OMAS client](creating-a-connector.md).
When an [Asset is cataloged](../../../../../open-metadata-publication/website/cataloging-assets) in the open metadata repository,
there is a [Connection](../../../../frameworks/open-connector-framework/docs/concepts/connection.md) object
linked to it.  This defines all of the properties required to create the connector.

See [Creating a connector](creating-a-connector.md) for step by step instructions on creating connectors.
Asset Consumer OMAS looks up the Connection object and calls the [Connector Broker](../../../../frameworks/open-connector-framework/docs/concepts/connector-broker.md)
to create the connector.

Once the connector is created, an application may use it to retrieve the content of the asset and the connected
asset properties.

When the application has finished with the connector, it should call `disconnect()` to release any resources
that the connector may be holding.
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.