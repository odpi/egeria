<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Asset Owner OMAS design

The module structure for the Asset Owner OMAS follows the standard pattern as follows:

* [asset-owner-client](../../asset-owner-client) supports the client library.
* [asset-owner-api](../../asset-owner-api) supports the common Java classes that are used both by the client and the server.
* [asset-owner-server](../../asset-owner-server) supports in implementation of the access service and its related event management.
* [asset-owner-spring](../../asset-owner-spring) supports
the REST API using the [Spring](../../../../../developer-resources/Spring.md) libraries.

It makes use of the [ocf-metadata-management](../../../../common-services/ocf-metadata-management)
for its server side interaction with the metadata repository and so the
primary function of the Asset Owner OMAS is to manage the
APIs for the Asset Owner and translate between them and
the [Open Connector Framework (OCF)](../../../../frameworks/open-connector-framework) oriented interfaces
of ocf-metadata-management.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.