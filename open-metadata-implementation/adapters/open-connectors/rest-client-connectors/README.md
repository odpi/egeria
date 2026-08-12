<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

![Released](../../../../images/egeria-content-status-released.png#pagewidth)

# REST Client Connectors

Egeria clients call out to OMAG Server REST APIs through a small, stable interface -
[`RESTClientCalls`](rest-client-connectors-api) - rather than coding directly against any particular
HTTP library. The actual HTTP work is done by an implementation of the abstract
[`RESTClientConnector`](rest-client-connectors-api), obtained at runtime from the
[`RESTClientFactory`](rest-client-factory).

This design exists so that the HTTP transport used across the whole of Egeria can be swapped out - to
pick up performance improvements, drop a dependency, or change error handling behaviour - **without
touching any of the calling code**. Only the connector implementation needs to change; every OMAS,
OMVS, OMES and integration connector that calls a REST API automatically picks up the new behaviour the
next time `RESTClientFactory` hands out a connector.

## The modules

* **[rest-client-connectors-api](rest-client-connectors-api)** - defines the `RESTClientCalls` interface and
  the abstract `RESTClientConnector` base class that all implementations extend. This module has no
  dependency on any particular HTTP library.
* **[rest-client-factory](rest-client-factory)** - `RESTClientFactory` builds the `Connection` object used to
  instantiate the REST client connector, and is where the choice of which implementation to use is made.
* **[jdk-rest-client-connector](jdk-rest-client-connector)** - **the implementation used everywhere by
  default.** Built on the JDK's own `java.net.http.HttpClient`, so it adds no third-party HTTP dependency,
  supports HTTP/2 and connection reuse, and supports every HTTP method Egeria needs - including PATCH,
  which some target APIs (such as Unity Catalog) require but which `RestTemplate`'s default configuration
  cannot issue.
* **[spring-rest-client-connector](spring-rest-client-connector)** - the original implementation, built on
  Spring's `RestTemplate`. It is no longer the default, but it is kept in the codebase, fully working, for
  anyone who would rather use Spring - for example because their own code already depends on it, or they
  want Spring's request/response interceptor ecosystem. Point `RESTClientFactory` at
  `SpringRESTClientConnectorProvider` to use it instead.

Return to [open-connectors](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
