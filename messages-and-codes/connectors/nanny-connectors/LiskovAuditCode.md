<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# LiskovAuditCode

The LiskovAuditCode is used to define the message content for the Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 13 |
| **Message identifiers begin** | `LISKOV-DATA-HUB-MANAGER-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.liskov.ffdc.LiskovAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/nanny-connectors](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors) |
| **Source** | [LiskovAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/liskov/ffdc/LiskovAuditCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/data-sharing-hub/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [LISKOV-DATA-HUB-MANAGER-0001](#liskov-data-hub-manager-0001) | EXCEPTION | The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3} |
| [LISKOV-DATA-HUB-MANAGER-0009](#liskov-data-hub-manager-0009) | INFO | The {0} integration connector has stopped its monitoring of data sharing hubs from server {1} on platform {2} and is shutting down |
| [LISKOV-DATA-HUB-MANAGER-0011](#liskov-data-hub-manager-0011) | INFO | The {0} integration connector is starting its monitoring of data sharing hubs from server {1} on platform {2} |
| [LISKOV-DATA-HUB-MANAGER-0012](#liskov-data-hub-manager-0012) | INFO | The {0} integration connector has created a new catalog target for data sharing hub {1} ({2}) |
| [LISKOV-DATA-HUB-MANAGER-0013](#liskov-data-hub-manager-0013) | INFO | The {0} integration connector has created a new data dictionary for data sharing hub {1} ({2}) |
| [LISKOV-DATA-HUB-MANAGER-0014](#liskov-data-hub-manager-0014) | INFO | The {0} integration connector has created a new data field {1} ({2}) for data sharing hub {3} ({4}) |
| [LISKOV-DATA-HUB-MANAGER-0016](#liskov-data-hub-manager-0016) | INFO | The {0} integration connector is refreshing data fields from {1} data store {2} ({3}) for data sharing hub {4} ({5}) |
| [LISKOV-DATA-HUB-MANAGER-0017](#liskov-data-hub-manager-0017) | INFO | The {0} integration connector is retrieving known data dictionary definitions for data sharing hub {1} ({2}) |
| [LISKOV-DATA-HUB-MANAGER-0018](#liskov-data-hub-manager-0018) | INFO | The {0} integration connector has created a new data structure {1} ({2}) for data sharing hub {3} ({4}) |
| [LISKOV-DATA-HUB-MANAGER-0019](#liskov-data-hub-manager-0019) | INFO | The {0} integration connector is refreshing data fields from CSV File {2} ({3}) for data sharing hub {4} ({5}) |
| [LISKOV-DATA-HUB-MANAGER-0020](#liskov-data-hub-manager-0020) | INFO | The {0} integration connector has started engine action {1} to enable the cataloguing of {2} {3} ({4}) using governance action type {5} |
| [LISKOV-DATA-HUB-MANAGER-0021](#liskov-data-hub-manager-0021) | INFO | The {0} integration connector has started engine action {1} to survey {2} {3} ({4}) using governance action type {5} |
| [LISKOV-DATA-HUB-MANAGER-0022](#liskov-data-hub-manager-0022) | INFO | The {0} integration connector is unable to locate a technology type called {1} for {2} {3} ({4}) |

----

### LISKOV-DATA-HUB-MANAGER-0001

> The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `LiskovAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot catalog one or more metadata elements in the metadata repository.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### LISKOV-DATA-HUB-MANAGER-0009

> The {0} integration connector has stopped its monitoring of data sharing hubs from server {1} on platform {2} and is shutting down

|  |  |
|---|---|
| **Java constant** | `LiskovAuditCode.CONNECTOR_STOPPING` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector is disconnecting.

**User action**

No action is required unless there are errors that follow indicating that there were problems shutting down.


----

### LISKOV-DATA-HUB-MANAGER-0011

> The {0} integration connector is starting its monitoring of data sharing hubs from server {1} on platform {2}

|  |  |
|---|---|
| **Java constant** | `LiskovAuditCode.STARTING_CONNECTOR` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector is initializing its monitoring of the data sharing hubs connected as Catalog Targets.

**User action**

Monitor the data dictionaries for these data sharing hubs are being maintained successfully.


----

### LISKOV-DATA-HUB-MANAGER-0012

> The {0} integration connector has created a new catalog target for data sharing hub {1} ({2})

|  |  |
|---|---|
| **Java constant** | `LiskovAuditCode.NEW_DATA_HUB` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector is initiating its management of a new data sharing hub .

**User action**

No action is required.  This message is for monitoring the set up of the data sharing hub management.


----

### LISKOV-DATA-HUB-MANAGER-0013

> The {0} integration connector has created a new data dictionary for data sharing hub {1} ({2})

|  |  |
|---|---|
| **Java constant** | `LiskovAuditCode.NEW_DATA_DICTIONARY` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector has created a data dictionary for a new data sharing hub.

**User action**

No action is required.  This message is for monitoring the set up of the data sharing hub data dictionary.


----

### LISKOV-DATA-HUB-MANAGER-0014

> The {0} integration connector has created a new data field {1} ({2}) for data sharing hub {3} ({4})

|  |  |
|---|---|
| **Java constant** | `LiskovAuditCode.NEW_DATA_FIELD` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The connector has created a data field for a data sharing hub's data dictionary.

**User action**

No action is required.  This message is for monitoring the set up of the data sharing hub's data fields.


----

### LISKOV-DATA-HUB-MANAGER-0016

> The {0} integration connector is refreshing data fields from {1} data store {2} ({3}) for data sharing hub {4} ({5})

|  |  |
|---|---|
| **Java constant** | `LiskovAuditCode.REFRESHING_DATA_HUB_STORE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The connector is initiating its refreshing of a data sharing hub.

**User action**

No action is required.  This message is for monitoring the activity of the data sharing hub management.


----

### LISKOV-DATA-HUB-MANAGER-0017

> The {0} integration connector is retrieving known data dictionary definitions for data sharing hub {1} ({2})

|  |  |
|---|---|
| **Java constant** | `LiskovAuditCode.RETRIEVING_DATA_FIELDS` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector is initiating its retrieving the contents of the data dictionary for a data sharing hub.

**User action**

No action is required.  This message is for monitoring the progress of the data sharing hub management refresh.


----

### LISKOV-DATA-HUB-MANAGER-0018

> The {0} integration connector has created a new data structure {1} ({2}) for data sharing hub {3} ({4})

|  |  |
|---|---|
| **Java constant** | `LiskovAuditCode.NEW_DATA_STRUCTURE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The connector has created a data structure for a data sharing hub's data dictionary.

**User action**

No action is required.  This message is for monitoring the set up of the data sharing hub's data structures.


----

### LISKOV-DATA-HUB-MANAGER-0019

> The {0} integration connector is refreshing data fields from CSV File {2} ({3}) for data sharing hub {4} ({5})

|  |  |
|---|---|
| **Java constant** | `LiskovAuditCode.REFRESHING_CSV_FILE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The connector is reading the data fields from a CSV file so that it can refresh a data sharing hub.

**User action**

No action is required.  This message identifies the CSV file that the data fields are being read from.


----

### LISKOV-DATA-HUB-MANAGER-0020

> The {0} integration connector has started engine action {1} to enable the cataloguing of {2} {3} ({4}) using governance action type {5}

|  |  |
|---|---|
| **Java constant** | `LiskovAuditCode.ENABLING_CATALOGUING` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The connector has detected that the contents of a member of a data sharing hub are not being catalogued and has requested that the appropriate cataloguing integration connector begins to monitor it.  The contents of the member will appear in open metadata once the cataloguing integration connector has run.

**User action**

No action is required.  This message is for monitoring the set up of the cataloguing for the members of a data sharing hub.


----

### LISKOV-DATA-HUB-MANAGER-0021

> The {0} integration connector has started engine action {1} to survey {2} {3} ({4}) using governance action type {5}

|  |  |
|---|---|
| **Java constant** | `LiskovAuditCode.STARTING_SURVEY` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The connector has requested a new survey of a member of a data sharing hub so that the latest characteristics of its contents are available to the data sharing hub's owner.

**User action**

No action is required.  This message is for monitoring the surveying of the members of a data sharing hub.


----

### LISKOV-DATA-HUB-MANAGER-0022

> The {0} integration connector is unable to locate a technology type called {1} for {2} {3} ({4})

|  |  |
|---|---|
| **Java constant** | `LiskovAuditCode.NO_TECHNOLOGY_TYPE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The connector is not able to determine which cataloguing and survey governance action types are appropriate for this element and so it skips them.  The rest of the refresh continues.

**User action**

If this element should be catalogued and surveyed, check that its deployedImplementationType property is set to a technology type that is defined in the content packs loaded into this metadata store.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
