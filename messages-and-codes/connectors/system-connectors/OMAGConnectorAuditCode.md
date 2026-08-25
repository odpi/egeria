<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OMAGConnectorAuditCode

The OMAGConnectorAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 6 |
| **Message identifiers begin** | `OMAG-CONNECTORS-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.ffdc.OMAGConnectorAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/system-connectors/egeria-system-connectors](../../../open-metadata-implementation/adapters/open-connectors/system-connectors/egeria-system-connectors) |
| **Source** | [OMAGConnectorAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/system-connectors/egeria-system-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/egeriainfrastructure/ffdc/OMAGConnectorAuditCode.java) |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-egeria/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OMAG-CONNECTORS-0001](#omag-connectors-0001) | ERROR | The {0} Egeria Connector received an unexpected exception {1} during method {2}; the error message was: {3} |
| [OMAG-CONNECTORS-0002](#omag-connectors-0002) | INFO | The {0} Egeria Connector has been started.  The monitored platforms are: {1} |
| [OMAG-CONNECTORS-0005](#omag-connectors-0005) | INFO | The {0} integration connector has created a new {1} server element {2} for server {3} on platform {4} |
| [OMAG-CONNECTORS-0006](#omag-connectors-0006) | INFO | The {0} integration connector has detected that the {1} server of type {2} has no metadata collection id |
| [OMAG-CONNECTORS-0010](#omag-connectors-0010) | INFO | The monitoring of OMAG Server Platform {0} has started |
| [OMAG-CONNECTORS-0011](#omag-connectors-0011) | INFO | Connector {0} is synchronizing metadata for OMAG Server Platform: {1} |

----

### OMAG-CONNECTORS-0001

> The {0} Egeria Connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `OMAGConnectorAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot connector the the OMAG Infrastructure.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### OMAG-CONNECTORS-0002

> The {0} Egeria Connector has been started.  The monitored platforms are: {1}

|  |  |
|---|---|
| **Java constant** | `OMAGConnectorAuditCode.EGERIA_CONNECTOR_START` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector is designed to catalog details of Software Server Platforms that have the deployedImplementationType property set to 'OMAG Server Platform'.

**User action**

No specific action is required.  This message is to confirm the start of the integration connector.


----

### OMAG-CONNECTORS-0005

> The {0} integration connector has created a new {1} server element {2} for server {3} on platform {4}

|  |  |
|---|---|
| **Java constant** | `OMAGConnectorAuditCode.NEW_SERVER` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The connector is has catalogued a new server.

**User action**

No action is required unless there are errors that follow indicating that there were problems with the new definition.


----

### OMAG-CONNECTORS-0006

> The {0} integration connector has detected that the {1} server of type {2} has no metadata collection id

|  |  |
|---|---|
| **Java constant** | `OMAGConnectorAuditCode.NULL_METADATA_COLLECTION_ID` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

No metadata collection asset nor inventory catalog software capability is connected to the server.

**User action**

This is only ok if the server is a metadata access point.


----

### OMAG-CONNECTORS-0010

> The monitoring of OMAG Server Platform {0} has started

|  |  |
|---|---|
| **Java constant** | `OMAGConnectorAuditCode.EGERIA_TARGET_START` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |

**System action**

The connector will synchronize the configuration of the platform and its servers with its open metadata description.

**User action**

No specific action is required.  This message is to confirm the start of the target processor.


----

### OMAG-CONNECTORS-0011

> Connector {0} is synchronizing metadata for OMAG Server Platform: {1}

|  |  |
|---|---|
| **Java constant** | `OMAGConnectorAuditCode.EGERIA_TARGET_REFRESH` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector is synchronizing the configuration of the platform and its servers with its open metadata description.

**User action**

No specific action is required.  This message is to confirm the refreshing of the target processor.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
