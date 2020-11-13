<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Data Platform Server

The **Data Platform Server** is an [Integration Daemon](integration-daemon.md)
that can capture metadata about data set and data stores managed by a data
platform, such as a database server, Apache Cassandra or Apache Hive.
This information results in new
[DataStore](../../../../open-metadata-publication/website/open-metadata-types/0210-Data-Stores.md) and
[DataSet](../../../../open-metadata-publication/website/open-metadata-types/0010-Base-Model.md) assets
being defined in open metadata optionally linked to Connection objects for
access them and details of their internal schema.

The data platform server is paired with the [Data Platform OMAS](../../../access-services/data-platform).
Its connector interfaces are defined
in the [data-platform-services-connector](../../../governance-servers/data-platform-services/data-platform-services-connector) module.

----
* Return to [Integration Daemon](integration-daemon.md).
* Return to [Admin Guide](../user).
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.