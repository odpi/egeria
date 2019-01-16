<!-- SPDX-License-Identifier: Apache-2.0 -->

# Asset Lineage Open Metadata Access Service (OMAS)

The Asset Catalog OMAS provides services to query the lineage of business terms and data assets.

The module structure for the Asset Catalog OMAS is as follows:

* [asset-lineage-client](asset-lineage-client) supports the client library.
* [asset-lineage-api](asset-lineage-api) supports the common Java classes that are used both by the client and the server.
* [asset-lineage-server](asset-lineage-server) supports in implementation of the access service and its related event management.
* [asset-lineage-spring](asset-lineage-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.
