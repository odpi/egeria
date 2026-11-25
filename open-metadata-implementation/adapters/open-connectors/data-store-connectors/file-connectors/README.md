<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# File Connectors

The File Connectors module contains connectors to different types of data stores.  These connectors implement the
[Open Connector Framework (OCF)](../../../../frameworks/open-connector-framework) **Connector** interface.

* **[basic-file-connector](basic-file-connector)** provides connector to read a file.
It has no special knowledge of the format.

* **[csv-file-connector](csv-file-connector)** provides connector to read files
that have a CSV tabular format.

* **[data-folder-connector](data-folder-connector)** provides connector to read a data set that is made up of many files
stored within a data folder.

* **[parquet-file-connector](parquet-file-connector)** provides connector to extract the schema,
row counts, storage characteristics and column statistics from the footer of a file in the Apache Parquet format.
It supports all versions of the Parquet format and has no dependencies on other libraries.  It also reads the rows of data
when the DuckDB JDBC driver is available at runtime.


----
Return to the [data-store-connectors](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
