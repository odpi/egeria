<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OpenAPIIntegrationConnectorAuditCode

The OpenAPIIntegrationConnectorAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 14 |
| **Message identifiers begin** | `OPEN-API-INTEGRATION-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.integration.openapis.ffdc.OpenAPIIntegrationConnectorAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/integration-connectors/openapi-integration-connector](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/openapi-integration-connector) |
| **Source** | [OpenAPIIntegrationConnectorAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/openapi-integration-connector/src/main/java/org/odpi/openmetadata/adapters/connectors/integration/openapis/ffdc/OpenAPIIntegrationConnectorAuditCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/integration-connector/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OPEN-API-INTEGRATION-CONNECTOR-0001](#open-api-integration-connector-0001) | INFO | The {0} integration connector has been initialized to monitor URL {1} with templateQualifiedName={2} |
| [OPEN-API-INTEGRATION-CONNECTOR-0002](#open-api-integration-connector-0002) | INFO | The {0} integration connector has been initialized to monitor all Http(s) Endpoints with templateQualifiedName={1} |
| [OPEN-API-INTEGRATION-CONNECTOR-0003](#open-api-integration-connector-0003) | INFO | The {0} integration connector has been refreshed to monitor URL {1} |
| [OPEN-API-INTEGRATION-CONNECTOR-0004](#open-api-integration-connector-0004) | INFO | The {0} integration connector has been refreshed to monitor all Http(s) Endpoints.  Currently {1} are known. |
| [OPEN-API-INTEGRATION-CONNECTOR-0005](#open-api-integration-connector-0005) | INFO | The {0} integration connector has retrieved a new endpoint {1} at URL {2}. |
| [OPEN-API-INTEGRATION-CONNECTOR-0006](#open-api-integration-connector-0006) | INFO | The {0} integration connector retrieved the Open API Specification from URL {1}.  The API retrieved was {2} |
| [OPEN-API-INTEGRATION-CONNECTOR-0007](#open-api-integration-connector-0007) | INFO | The {0} integration connector retrieved the Open API Specification from URL {1} ({2} ({3})) and catalogued {4} APIs with a total of {5} operations. |
| [OPEN-API-INTEGRATION-CONNECTOR-0008](#open-api-integration-connector-0008) | EXCEPTION | An unexpected {0} exception was returned to the {1} integration connector {2} method when trying to retrieve the Open API Spec for URL {3}.  The error message was {4} |
| [OPEN-API-INTEGRATION-CONNECTOR-0009](#open-api-integration-connector-0009) | INFO | The {0} integration connector has stopped its monitoring and is shutting down |
| [OPEN-API-INTEGRATION-CONNECTOR-0010](#open-api-integration-connector-0010) | INFO | The {0} integration connector created a new DeployedAPI asset {1} ({2}) for the Open API Specification retrieved from URL {3} |
| [OPEN-API-INTEGRATION-CONNECTOR-0011](#open-api-integration-connector-0011) | INFO | The {0} integration connector created a new APIOperation {1} ({2}) for path {3} and command {4} under DeployedAPI {5} |
| [OPEN-API-INTEGRATION-CONNECTOR-0016](#open-api-integration-connector-0016) | ERROR | The {0} integration connector retrieved an invalid {1} element in method {2}.  Element guid is: {3} |
| [OPEN-API-INTEGRATION-CONNECTOR-0029](#open-api-integration-connector-0029) | EXCEPTION | The {0} integration connector received an unexpected exception {1} in method {2}; the error message was: {3} |
| [OPEN-API-INTEGRATION-CONNECTOR-0030](#open-api-integration-connector-0030) | EXCEPTION | A client-side exception was received from API call {0} to OMAG Server {1} at {2}.  The error message was {3} |

----

### OPEN-API-INTEGRATION-CONNECTOR-0001

> The {0} integration connector has been initialized to monitor URL {1} with templateQualifiedName={2}

|  |  |
|---|---|
| **Java constant** | `OpenAPIIntegrationConnectorAuditCode.CONNECTOR_CONFIGURATION_WITH_ENDPOINT` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector is designed to monitor changes to the Open API Specification located at the URL.  If the templateQualifiedName is set, it identifies a template entity to use when cataloging APIs.

**User action**

No specific action is required.  This message is to confirm the configuration for the integration connector.


----

### OPEN-API-INTEGRATION-CONNECTOR-0002

> The {0} integration connector has been initialized to monitor all Http(s) Endpoints with templateQualifiedName={1}

|  |  |
|---|---|
| **Java constant** | `OpenAPIIntegrationConnectorAuditCode.CONNECTOR_CONFIGURATION_NO_ENDPOINT` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector is designed to monitor changes to the Open API Specification located at specific URLs.  These URLs will be retrieved by querying Endpoints with a protocol of 'Http(s)' from the open metadata repositories.If the templateQualifiedName is set, it identifies a template entity to use when cataloging APIs

**User action**

No specific action is required.  This message is to confirm that the missing endpoint in the configuration is correct for the integration connector.


----

### OPEN-API-INTEGRATION-CONNECTOR-0003

> The {0} integration connector has been refreshed to monitor URL {1}

|  |  |
|---|---|
| **Java constant** | `OpenAPIIntegrationConnectorAuditCode.CONNECTOR_REFRESH_WITH_ENDPOINT` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector will retrieve the API Specification for the URL

**User action**

Look to see if the spec is retrieved.


----

### OPEN-API-INTEGRATION-CONNECTOR-0004

> The {0} integration connector has been refreshed to monitor all Http(s) Endpoints.  Currently {1} are known.

|  |  |
|---|---|
| **Java constant** | `OpenAPIIntegrationConnectorAuditCode.CONNECTOR_REFRESH_ALL_ENDPOINTS` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector will attempt to retrieve the specifications for each of the endpoints.

**User action**

Look to see if the right specs are retrieved.


----

### OPEN-API-INTEGRATION-CONNECTOR-0005

> The {0} integration connector has retrieved a new endpoint {1} at URL {2}.

|  |  |
|---|---|
| **Java constant** | `OpenAPIIntegrationConnectorAuditCode.NEW_ENDPOINT` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector will attempt to retrieve the specification for this endpoint if supported.

**User action**

Look to see if an Open API Specification is retrieved where it is expected.


----

### OPEN-API-INTEGRATION-CONNECTOR-0006

> The {0} integration connector retrieved the Open API Specification from URL {1}.  The API retrieved was {2}

|  |  |
|---|---|
| **Java constant** | `OpenAPIIntegrationConnectorAuditCode.RETRIEVED_OPEN_API_SPEC` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The exception is passed back to the integration daemon that is hosting this connector to enable it to perform error handling.  More messages are likely to follow describing the error handling that was performed.  These can help to determine how to recover from this error

**User action**

This message contains the exception that was the original cause of the problem. Use the information from the exception stack trace to determine why the connector is not able to access the directory and resolve that issue.  Use the messages that where subsequently logged during the error handling to discover how to restart the connector in the integration daemon once the original cause of the error has been corrected.


----

### OPEN-API-INTEGRATION-CONNECTOR-0007

> The {0} integration connector retrieved the Open API Specification from URL {1} ({2} ({3})) and catalogued {4} APIs with a total of {5} operations.

|  |  |
|---|---|
| **Java constant** | `OpenAPIIntegrationConnectorAuditCode.CATALOGUED_OPEN_API_SPEC` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The connector extracted the content of the Open API, retrieved/created the endpoint for it and created a DeployedAPI asset for each 'tag' linked to an APIOperation for each 'path/operation' pair

**User action**

Validate that the connector is extracting all the required information for your use case.


----

### OPEN-API-INTEGRATION-CONNECTOR-0008

> An unexpected {0} exception was returned to the {1} integration connector {2} method when trying to retrieve the Open API Spec for URL {3}.  The error message was {4}

|  |  |
|---|---|
| **Java constant** | `OpenAPIIntegrationConnectorAuditCode.UNABLE_TO_RETRIEVE_OPEN_API_SPEC` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The exception is returned to the integration daemon that is hosting this connector to enable it to perform error handling.

**User action**

Use the message in the nested exception to determine the root cause of the error. Once this is resolved, follow the instructions in the messages produced by the integration daemon to restart this connector.


----

### OPEN-API-INTEGRATION-CONNECTOR-0009

> The {0} integration connector has stopped its monitoring and is shutting down

|  |  |
|---|---|
| **Java constant** | `OpenAPIIntegrationConnectorAuditCode.CONNECTOR_STOPPING` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |

**System action**

The connector is disconnecting.

**User action**

No action is required unless there are errors that follow indicating that there were problems shutting down.


----

### OPEN-API-INTEGRATION-CONNECTOR-0010

> The {0} integration connector created a new DeployedAPI asset {1} ({2}) for the Open API Specification retrieved from URL {3}

|  |  |
|---|---|
| **Java constant** | `OpenAPIIntegrationConnectorAuditCode.NEW_DEPLOYED_API` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector has catalogued a new DeployedAPI asset to represent the API described by this specification.

**User action**

No specific action is required.  This message confirms that a new API has been catalogued.


----

### OPEN-API-INTEGRATION-CONNECTOR-0011

> The {0} integration connector created a new APIOperation {1} ({2}) for path {3} and command {4} under DeployedAPI {5}

|  |  |
|---|---|
| **Java constant** | `OpenAPIIntegrationConnectorAuditCode.NEW_API_OPERATION` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The connector has catalogued a new APIOperation schema type to represent this path/command combination from the Open API Specification.

**User action**

No specific action is required.  This message confirms that a new API operation has been catalogued.


----

### OPEN-API-INTEGRATION-CONNECTOR-0016

> The {0} integration connector retrieved an invalid {1} element in method {2}.  Element guid is: {3}

|  |  |
|---|---|
| **Java constant** | `OpenAPIIntegrationConnectorAuditCode.BAD_ENDPOINT` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The metadata element is ignored.

**User action**

Investigate why this element is incomplete.


----

### OPEN-API-INTEGRATION-CONNECTOR-0029

> The {0} integration connector received an unexpected exception {1} in method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `OpenAPIIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot catalog one or more APIs.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### OPEN-API-INTEGRATION-CONNECTOR-0030

> A client-side exception was received from API call {0} to OMAG Server {1} at {2}.  The error message was {3}

|  |  |
|---|---|
| **Java constant** | `OpenAPIIntegrationConnectorAuditCode.CLIENT_SIDE_REST_API_ERROR` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The server has issued a call to the open metadata access service REST API in a remote server and has received an exception from the local client libraries.

**User action**

Look for errors in the local server's console to understand and correct the source of the error.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
