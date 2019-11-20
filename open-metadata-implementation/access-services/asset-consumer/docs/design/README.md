<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Asset Consumer OMAS Design

The module structure for the Asset Consumer OMAS is as follows:

* [asset-consumer-client](../../asset-consumer-client) supports the client library.
* [asset-consumer-api](../../asset-consumer-api) supports the common Java classes that are used both by the client and the server.
* [asset-consumer-server](../../asset-consumer-server) supports in implementation of the access service and its related event management.
* [asset-consumer-spring](../../asset-consumer-spring) supports the REST API using the [Spring](../../../../../developer-resources/Spring.md) libraries.

The Asset Consumer OMAS interfaces are heavily influenced by the
[Open Connector Framework (OCF)](../../../../frameworks/open-connector-framework).
It also uses the client and server side support provided by the
[ocf-metadata-management](../../../../common-services/ocf-metadata-management) common services.
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.