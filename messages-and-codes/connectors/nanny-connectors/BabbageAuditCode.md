<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# BabbageAuditCode

The BabbageAuditCode is used to define the message content for the Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 4 |
| **Message identifiers begin** | `BABBAGE-ANALYTICAL-ENGINE-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.babbage.ffdc.BabbageAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/nanny-connectors](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors) |
| **Source** | [BabbageAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/babbage/ffdc/BabbageAuditCode.java) |
| **Further reading** | <https://egeria-project.org/egeria-solutions/organization-insight/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [BABBAGE-ANALYTICAL-ENGINE-0001](#babbage-analytical-engine-0001) | EXCEPTION | The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3} |
| [BABBAGE-ANALYTICAL-ENGINE-0009](#babbage-analytical-engine-0009) | INFO | The {0} integration connector has stopped its monitoring of engine actions from server {1} on platform {2} and is shutting down |
| [BABBAGE-ANALYTICAL-ENGINE-0011](#babbage-analytical-engine-0011) | INFO | The {0} integration connector is starting its monitoring for analytical work from server {1} on platform {2} |
| [BABBAGE-ANALYTICAL-ENGINE-0012](#babbage-analytical-engine-0012) | INFO | The {0} integration connector has created a new engine action {1} for Governance Action Type {2} ({3}) |

----

### BABBAGE-ANALYTICAL-ENGINE-0001

> The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `BabbageAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot catalog one or more metadata elements in the metadata repository.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### BABBAGE-ANALYTICAL-ENGINE-0009

> The {0} integration connector has stopped its monitoring of engine actions from server {1} on platform {2} and is shutting down

|  |  |
|---|---|
| **Java constant** | `BabbageAuditCode.CONNECTOR_STOPPING` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector is disconnecting.

**User action**

No action is required unless there are errors that follow indicating that there were problems shutting down.


----

### BABBAGE-ANALYTICAL-ENGINE-0011

> The {0} integration connector is starting its monitoring for analytical work from server {1} on platform {2}

|  |  |
|---|---|
| **Java constant** | `BabbageAuditCode.STARTING_CONNECTOR` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector is initializing engine actions from the Governance Action Types connected as Catalog Targets.

**User action**

Monitor the creation of the engine actions and ensure they are executing successfully.


----

### BABBAGE-ANALYTICAL-ENGINE-0012

> The {0} integration connector has created a new engine action {1} for Governance Action Type {2} ({3})

|  |  |
|---|---|
| **Java constant** | `BabbageAuditCode.NEW_ENGINE_ACTION` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector is initiating analytical work.

**User action**

No action is required.  This message is for monitoring the set up of the engine actions.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
