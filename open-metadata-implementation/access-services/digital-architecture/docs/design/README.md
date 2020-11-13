<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Digital Architecture OMAS Design

The module structure for the Digital Architecture OMAS follows the standard pattern as follows:

* [digital-architecture-client](../../digital-architecture-client) supports the client library.
* [digital-architecture-api](../../digital-architecture-api) supports the common Java classes that are used both by the client and the server.
* [digital-architecture-server](../../digital-architecture-server) supports in implementation of the access service and its related event management.
* [digital-architecture-spring](../../digital-architecture-spring) supports
the REST API using the [Spring](../../../../../developer-resources/Spring.md) libraries.

It makes use of the [ocf-metadata-management](../../../../common-services/ocf-metadata-management)
for its server side interaction with the metadata repository and so the
primary function of the Digital Architecture OMAS is to manage the
APIs for the architects and translate between them and
the [Open Connector Framework (OCF)](../../../../frameworks/open-connector-framework) oriented interfaces
of ocf-metadata-management.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.