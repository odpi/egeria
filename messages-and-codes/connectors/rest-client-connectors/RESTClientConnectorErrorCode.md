<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# RESTClientConnectorErrorCode

The RESTClientConnectorErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the REST Client. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 2 |
| **Message identifiers begin** | `CLIENT-SIDE-REST-API-CONNECTOR-503-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.restclients.ffdc.RESTClientConnectorErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/rest-client-connectors/rest-client-connectors-api](../../../open-metadata-implementation/adapters/open-connectors/rest-client-connectors/rest-client-connectors-api) |
| **Source** | [RESTClientConnectorErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/rest-client-connectors/rest-client-connectors-api/src/main/java/org/odpi/openmetadata/adapters/connectors/restclients/ffdc/RESTClientConnectorErrorCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/rest-client-connector/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [CLIENT-SIDE-REST-API-CONNECTOR-503-002 ](#client-side-rest-api-connector-503-002-) | 503 | A client-side exception {0} was received by method {1} from API call {2} to server {3} on platform {4}.  The error message was {5} |
| [CLIENT-SIDE-REST-API-CONNECTOR-503-004 ](#client-side-rest-api-connector-503-004-) | 503 | REST API call {0} to server {1} on platform {2} returned an unsuccessful HTTP status {3}.  The response body was: {4} |

----

### CLIENT-SIDE-REST-API-CONNECTOR-503-002 

> A client-side exception {0} was received by method {1} from API call {2} to server {3} on platform {4}.  The error message was {5}

|  |  |
|---|---|
| **Java constant** | `RESTClientConnectorErrorCode.CLIENT_SIDE_REST_API_ERROR` |
| **HTTP error code** | 503 - Service Unavailable - the service needed to process the request is not running |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The client has issued a call to the open metadata access service REST API in a remote server and has received an exception from the local client libraries.

**User action**

Review the error message to determine the cause of the error.  Check that the server is running and the URL is correct. Also check that the request body has legal values in it.  Look for errors in the local server's audit log to understand and correct the cause of the error. Then rerun the request


----

### CLIENT-SIDE-REST-API-CONNECTOR-503-004 

> REST API call {0} to server {1} on platform {2} returned an unsuccessful HTTP status {3}.  The response body was: {4}

|  |  |
|---|---|
| **Java constant** | `RESTClientConnectorErrorCode.UNSUCCESSFUL_HTTP_RESPONSE` |
| **HTTP error code** | 503 - Service Unavailable - the service needed to process the request is not running |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The remote server rejected the request, or was not able to process it, before returning a body of the expected type.

**User action**

Review the HTTP status and response body to determine the cause of the error, correct the request and retry.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
