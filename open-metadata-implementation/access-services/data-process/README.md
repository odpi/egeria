<!-- SPDX-License-Identifier: Apache-2.0 -->

# Data Process Open Metadata Access Service (OMAS)

The Data Process OMAS provides APIs and events for a data movement/processing
engine to record the changes it is making the the data landscape. 
This information forms a key part of the lineage for the data assets that
are affected.

The module structure for the Data Process OMAS is as follows:

* [data-process-client](data-process-client) supports the client library.
* [data-process-api](data-process-api) supports the common Java classes that are used both by the client and the server.
* [data-process-server](data-process-server) supports in implementation of the access service and its related event management.
* [data-process-spring](data-process-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.
