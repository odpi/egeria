<!-- SPDX-License-Identifier: Apache-2.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Data Store Connectors

The Data Store Connectors module contains connectors to different types of data stores.  These connectors implement the
[Open Connector Framework (OCF)](../../../frameworks/open-connector-framework) **Connector** interface.

* **[avro-file-connector](avro-file-connector)** provides connector to read files
that have an [Apache Avro](https://avro.apache.org/https://avro.apache.org/) format.

* **[gaian-connector](gaian-connector)** provides a JDBC-style connector to the [Gaian](https://github.com/gaiandb/gaiandb) virtualization
engine.

* **[structured-file-connector](structured-file-connector)** provides connector to read files
that have a CSV tabular format.