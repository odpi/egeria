<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# REST Client Connector API

This module defines the contract that every [REST Client Connector](..) implementation must satisfy - it
has no dependency on any particular HTTP library, so that Egeria clients can code against it without
caring which implementation is actually running underneath.

* **`RESTClientCalls`** - the interface implemented by every REST client connector. It declares one
  method per HTTP verb Egeria needs: `callGetRESTCall`/`callGetRESTCallNoParams`,
  `callPostRESTCall`/`callPostRESTCallNoParams`, `callPutRESTCall`/`callPutRESTCallNoParams`,
  `callDeleteRESTCall`/`callDeleteRESTCallNoParams` and `callPatchRESTCall`. Each takes a URL template
  with positional `{0}`, `{1}`, ... placeholders, the parameters to substitute into them, and (where
  relevant) a request body and the class to deserialize the response into.
* **`RESTClientConnector`** - the abstract [OCF connector](https://egeria-project.org/concepts/connector)
  base class that implementations extend. It stays abstract deliberately - see the
  [jdk-rest-client-connector](../jdk-rest-client-connector) and
  [spring-rest-client-connector](../spring-rest-client-connector) READMEs for why there are two sibling
  implementations rather than one.
* **`RESTClientConnectorErrorCode`** and **`RESTServerException`** - the FFDC error code set and checked
  exception that every implementation uses to report a failed REST call in a consistent, Egeria-standard
  way, regardless of which HTTP library actually issued the request.

Callers do not normally construct a connector directly - they ask the
[REST Client Factory](../rest-client-factory) for one.

Return to [rest-client-connectors](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
