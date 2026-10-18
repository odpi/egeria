<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# WSO2MIAuditCode

The WSO2MIAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 4 |
| **Message identifiers begin** | `WSO2MI-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.wso2mi.ffdc.WSO2MIAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/data-manager-connectors/wso2mi-connectors](../../../open-metadata-implementation/adapters/open-connectors/data-manager-connectors/wso2mi-connectors) |
| **Source** | [WSO2MIAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/data-manager-connectors/wso2mi-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/wso2mi/ffdc/WSO2MIAuditCode.java) |
| **Further reading** | <https://egeria-project.org/connectors/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [WSO2MI-CONNECTOR-0001](#wso2mi-connector-0001) | EXCEPTION | The WSO2 Micro Integrator connector {0} received an unexpected {1} exception during method {2}; the error message was: {3} |
| [WSO2MI-CONNECTOR-0002](#wso2mi-connector-0002) | INFO | The {0} integration connector has catalogued WSO2 Micro Integrator API {1} ({2}) |
| [WSO2MI-CONNECTOR-0003](#wso2mi-connector-0003) | INFO | The {0} integration connector is skipping WSO2 Micro Integrator API {1} ({2}) because it is already catalogued |
| [WSO2MI-CONNECTOR-0004](#wso2mi-connector-0004) | INFO | The {0} connector has authenticated with the WSO2 Micro Integrator Management API at {1} |

----

### WSO2MI-CONNECTOR-0001

> The WSO2 Micro Integrator connector {0} received an unexpected {1} exception during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `WSO2MIAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot process the current request.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### WSO2MI-CONNECTOR-0002

> The {0} integration connector has catalogued WSO2 Micro Integrator API {1} ({2})

|  |  |
|---|---|
| **Java constant** | `WSO2MIAuditCode.CATALOGED_API` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The integration connector looks for another deployed API.

**User action**

This is an information message showing that the integration connector has found a new deployed API.


----

### WSO2MI-CONNECTOR-0003

> The {0} integration connector is skipping WSO2 Micro Integrator API {1} ({2}) because it is already catalogued

|  |  |
|---|---|
| **Java constant** | `WSO2MIAuditCode.SKIPPING_API` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The integration connector continues, looking for another deployed API.

**User action**

This is an information message showing that the integration connector is working, but does not need to do any processing on this API.


----

### WSO2MI-CONNECTOR-0004

> The {0} connector has authenticated with the WSO2 Micro Integrator Management API at {1}

|  |  |
|---|---|
| **Java constant** | `WSO2MIAuditCode.AUTHENTICATED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector exchanges the supplied user name and password for a short-lived bearer token and uses it for subsequent calls.

**User action**

No action is required.  This message confirms that the Management API endpoint is reachable and the credentials are valid.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
