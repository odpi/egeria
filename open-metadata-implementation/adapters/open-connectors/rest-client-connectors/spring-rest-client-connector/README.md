<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Spring REST Client Connector

`SpringRESTClientConnector` is a [REST client connector](../rest-client-connectors-api) implementation
built on Spring's `RestTemplate`. It was originally the only implementation, and every Egeria client -
OMAS, OMVS, OMES and integration connector alike - used it, since all of them call REST APIs only through
the `RESTClientCalls` interface and never depend on this class directly.

## Current status

[`RESTClientFactory`](../rest-client-factory) now defaults to the
[JDK-based connector](../jdk-rest-client-connector) instead, primarily because Spring's default HTTP
request factory cannot issue an HTTP PATCH request, and because it makes Jackson deserialization errors
harder to see (see the [jdk-rest-client-connector](../jdk-rest-client-connector) README for the full
reasoning).

This connector is kept in the codebase, complete and fully working, for anyone who would rather use
Spring - for example because their own code already has a dependency on it, or they want to use Spring's
request/response interceptor ecosystem. To use it, point `RESTClientFactory` at
`SpringRESTClientConnectorProvider` instead of the JDK provider; no other code needs to change.

Return to [rest-client-connectors](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
