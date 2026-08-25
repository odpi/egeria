<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OpenAPIIntegrationConnectorErrorCode

The OpenAPIIntegrationConnectorErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the Basic File Connector. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 2 |
| **Message identifiers begin** | `OPEN-API-INTEGRATION-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.integration.openapis.ffdc.OpenAPIIntegrationConnectorErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/integration-connectors/openapi-integration-connector](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/openapi-integration-connector) |
| **Source** | [OpenAPIIntegrationConnectorErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/openapi-integration-connector/src/main/java/org/odpi/openmetadata/adapters/connectors/integration/openapis/ffdc/OpenAPIIntegrationConnectorErrorCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/integration-connector/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [OPEN-API-INTEGRATION-CONNECTOR-400-014](#open-api-integration-connector-400-014) | 400 | OMAG server has been called with a null local server name |
| [OPEN-API-INTEGRATION-CONNECTOR-503-001](#open-api-integration-connector-503-001) | 503 | A client-side exception of {0} was received from API call {1} to URL {2}.  The error message was {3} |

----

### OPEN-API-INTEGRATION-CONNECTOR-400-014

> OMAG server has been called with a null local server name

|  |  |
|---|---|
| **Java constant** | `OpenAPIIntegrationConnectorErrorCode.NULL_URL` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | none |

**System action**

The system cannot configure the local server without knowing what it is called.

**User action**

The local server name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can operate correctly.


----

### OPEN-API-INTEGRATION-CONNECTOR-503-001

> A client-side exception of {0} was received from API call {1} to URL {2}.  The error message was {3}

|  |  |
|---|---|
| **Java constant** | `OpenAPIIntegrationConnectorErrorCode.CLIENT_SIDE_REST_API_ERROR` |
| **HTTP error code** | 503 - Service Unavailable - the service needed to process the request is not running |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The integration has issued a call to the open metadata access service REST API in a remote server and has received an exception from the local client libraries.

**User action**

Look for errors in the local server's console to understand and correct the source of the error.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
