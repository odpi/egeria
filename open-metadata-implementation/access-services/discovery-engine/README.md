<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Discovery Engine Open Metadata Access Service (OMAS)

The Discovery Engine OMAS provides APIs and events for metadata discovery tools
that are surveying the data landscape and recording information in the
open metadata repositories.

The module structure for the Discovery Engine OMAS is as follows:

* [discovery-engine-client](discovery-engine-client) supports the client library.
* [discovery-engine-api](discovery-engine-api) supports the common Java classes that are used both by the client and the server.
* [discovery-engine-server](discovery-engine-server) supports in implementation of the access service and its related event management.
* [discovery-engine-spring](discovery-engine-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.
