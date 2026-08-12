<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Server Operations API

The server operations API module provides the property and REST response structures used to query and
control an [OMAG Server](https://egeria-project.org/concepts/omag-server).

* `ServerStatus`, `ServerActiveStatus` and `ServerInstanceStatus` describe the start and end, and the active
  state, of a server (or a service within it) on a single platform.  `OMAGServerInstanceHistory` documents the
  start and end of a server instance.
* `OMAGServerServiceStatus` and `ServerServicesStatus` document the status of the services running within a
  server, useful for determining which service is causing a server to be stuck starting or stopping.
* `ServerStatusResponse`, `OMAGServerStatusResponse`, `ServerServicesListResponse` and
  `SuccessMessageResponse` are the REST response structures returned by the server operations API.
* `ServerOpsAuditCode` and `ServerOpsErrorCode` define the audit log messages and first failure data capture
  (FFDC) exceptions used by server operations.

----
Return to [Server Operations](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
