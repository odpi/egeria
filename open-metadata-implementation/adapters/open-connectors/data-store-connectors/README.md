<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Data Store Connectors

The Data Store Connectors module contains
a small collection of connectors for applications.
The aim is to eventually cover most common types of data sources.
connectors to different types of data stores.  These connectors implement the

[Open Connector Framework (OCF)](../../../frameworks/open-connector-framework) **Connector** interface.

* **[file-connectors](file-connectors)** provides connector to read files.

* **[gaian-connector](gaian-connector)** provides a JDBC-style connector to the [Gaian](https://github.com/gaiandb/gaiandb) virtualization
engine.

* **[cassandra-data-store-connector](cassandra-data-store-connector)** provides connector to read data from
[Apache Cassandra](http://cassandra.apache.org/).


There is a code sample that shows how to work with the file connector
in the [asset-management-samples](../../../../open-metadata-resources/open-metadata-samples/access-services-samples/asset-management-samples).

----
* Return to [open-connectors](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
