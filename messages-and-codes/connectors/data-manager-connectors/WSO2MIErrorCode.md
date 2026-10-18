<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# WSO2MIErrorCode

The WSO2MIErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the WSO2 Micro Integrator connectors. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 3 |
| **Message identifiers begin** | `WSO2MI-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.wso2mi.ffdc.WSO2MIErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/data-manager-connectors/wso2mi-connectors](../../../open-metadata-implementation/adapters/open-connectors/data-manager-connectors/wso2mi-connectors) |
| **Source** | [WSO2MIErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/data-manager-connectors/wso2mi-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/wso2mi/ffdc/WSO2MIErrorCode.java) |
| **Further reading** | <https://egeria-project.org/connectors/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [WSO2MI-CONNECTOR-400-001](#wso2mi-connector-400-001) | 400 | Connection {0} has been configured without a network address for the WSO2 Micro Integrator Management API |
| [WSO2MI-CONNECTOR-401-001](#wso2mi-connector-401-001) | 401 | The {0} connector could not authenticate with the WSO2 Micro Integrator Management API at {1}; the login call returned no access token |
| [WSO2MI-CONNECTOR-500-001](#wso2mi-connector-500-001) | 500 | The {0} WSO2 Micro Integrator connector received an unexpected exception {1} during method {2}; the error message was: {3} |

----

### WSO2MI-CONNECTOR-400-001

> Connection {0} has been configured without a network address for the WSO2 Micro Integrator Management API

|  |  |
|---|---|
| **Java constant** | `WSO2MIErrorCode.NULL_URL` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The connector cannot start because it does not know where to send Management API requests.

**User action**

Update the connection's endpoint with the base URL of the WSO2 Micro Integrator Management API, for example https://localhost:9164/management.


----

### WSO2MI-CONNECTOR-401-001

> The {0} connector could not authenticate with the WSO2 Micro Integrator Management API at {1}; the login call returned no access token

|  |  |
|---|---|
| **Java constant** | `WSO2MIErrorCode.NO_ACCESS_TOKEN` |
| **HTTP error code** | 401 - Unauthorized - the caller is not authenticated |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector cannot make Management API calls because it has no bearer token.

**User action**

Check that the configured user name and password are valid for the Management API and that the /management/login resource is enabled.


----

### WSO2MI-CONNECTOR-500-001

> The {0} WSO2 Micro Integrator connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `WSO2MIErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot process the current request.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
