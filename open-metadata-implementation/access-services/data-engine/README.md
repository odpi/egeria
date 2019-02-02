<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Data Engine Open Metadata Access Service (OMAS)

The Data Engine OMAS provides APIs and events for a data movement/processing
engine to record the changes it is making the the data landscape. 
This information forms a key part of the lineage for the data assets that
are affected.

The module structure for the Data Process OMAS is as follows:

* [data-engine-client](data-engine-client) supports the client library.
* [data-engine-api](data-engine-api) supports the common Java classes that are used both by the client and the server.
* [data-engine-server](data-engine-server) supports in implementation of the access service and its related event management.
* [data-engine-spring](data-engine-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.