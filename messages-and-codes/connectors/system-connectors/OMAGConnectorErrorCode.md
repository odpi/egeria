<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OMAGConnectorErrorCode

The OMAGConnectorErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the Egeria connectors. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 3 |
| **Message identifiers begin** | `OMAG-CONNECTORS-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.ffdc.OMAGConnectorErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/system-connectors/egeria-system-connectors](../../../open-metadata-implementation/adapters/open-connectors/system-connectors/egeria-system-connectors) |
| **Source** | [OMAGConnectorErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/system-connectors/egeria-system-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/egeriainfrastructure/ffdc/OMAGConnectorErrorCode.java) |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-egeria/overview/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [OMAG-CONNECTORS-400-001](#omag-connectors-400-001) | 400 | Egeria connector {0} has been configured without the URL to the OMAG Server Platform |
| [OMAG-CONNECTORS-400-002](#omag-connectors-400-002) | 400 | Egeria connector {0} has been configured without the name of the OMAG Server to call |
| [OMAG-CONNECTORS-500-001](#omag-connectors-500-001) | 500 | The {0} Egeria connector received an unexpected exception {1} during method {2}; the error message was: {3} |

----

### OMAG-CONNECTORS-400-001

> Egeria connector {0} has been configured without the URL to the OMAG Server Platform

|  |  |
|---|---|
| **Java constant** | `OMAGConnectorErrorCode.NULL_URL` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The connector cannot contact the OMAG Infrastructure.

**User action**

The Platform URL Root is configured in the connector's connection endpoint in the address property.  Typically it is the host name and port where the OMAG Server Platform is running.


----

### OMAG-CONNECTORS-400-002

> Egeria connector {0} has been configured without the name of the OMAG Server to call

|  |  |
|---|---|
| **Java constant** | `OMAGConnectorErrorCode.NULL_SERVER_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The connector cannot contact the OMAG Server.

**User action**

The server's name is configured in the connector's connection additionalProperties in the serverName property.


----

### OMAG-CONNECTORS-500-001

> The {0} Egeria connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `OMAGConnectorErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector is unable to complete the requested operation and returns this exception to its caller.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
