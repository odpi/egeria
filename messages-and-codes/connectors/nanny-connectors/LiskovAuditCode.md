<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# LiskovAuditCode

The LiskovAuditCode is used to define the message content for the Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 16 |
| **Message identifiers begin** | `LISKOV-DATA-HUB-MANAGER-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.liskov.ffdc.LiskovAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/nanny-connectors](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors) |
| **Source** | [LiskovAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/liskov/ffdc/LiskovAuditCode.java) |


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
| [LISKOV-DATA-HUB-MANAGER-0019](#liskov-data-hub-manager-0019) | INFO | The {0} integration connector is refreshing data fields from {1} {2} ({3}) for data sharing hub {4} ({5}) |
| [LISKOV-DATA-HUB-MANAGER-0020](#liskov-data-hub-manager-0020) | INFO | The {0} integration connector has started engine action {1} to enable the cataloguing of {2} {3} ({4}) using governance action type {5} |
| [LISKOV-DATA-HUB-MANAGER-0021](#liskov-data-hub-manager-0021) | INFO | The {0} integration connector has started engine action {1} to survey {2} {3} ({4}) using governance action type {5} because {6} |
| [LISKOV-DATA-HUB-MANAGER-0022](#liskov-data-hub-manager-0022) | INFO | The {0} integration connector is unable to locate a technology type called {1} for {2} {3} ({4}) |
| [LISKOV-DATA-HUB-MANAGER-0023](#liskov-data-hub-manager-0023) | INFO | The {0} integration connector has changed the path name of {1} {2} ({3}) from {4} to its canonical path name {5} |
| [LISKOV-DATA-HUB-MANAGER-0024](#liskov-data-hub-manager-0024) | INFO | The {0} integration connector has linked {1} elements {2} and {3} as potential duplicates because they have the same canonical path name {4} |
| [LISKOV-DATA-HUB-MANAGER-0025](#liskov-data-hub-manager-0025) | ACTION | The {0} integration connector is ignoring the file systems for file folder {1} because the nearest ones are {2} file systems with mount points: {3} |

----

### LISKOV-DATA-HUB-MANAGER-0001

> The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `LiskovAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/concepts/data-sharing-hub/> |

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
| **Further reading** | <https://egeria-project.org/concepts/data-sharing-hub/> |

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
| **Further reading** | <https://egeria-project.org/concepts/data-sharing-hub/> |

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
| **Further reading** | <https://egeria-project.org/concepts/data-sharing-hub/> |

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
| **Further reading** | <https://egeria-project.org/concepts/data-sharing-hub/> |

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
| **Further reading** | <https://egeria-project.org/concepts/data-sharing-hub/> |

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
| **Further reading** | <https://egeria-project.org/concepts/data-sharing-hub/> |

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
| **Further reading** | <https://egeria-project.org/concepts/data-sharing-hub/> |

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
| **Further reading** | <https://egeria-project.org/concepts/data-sharing-hub/> |

**System action**

The connector has created a data structure for a data sharing hub's data dictionary.

**User action**

No action is required.  This message is for monitoring the set up of the data sharing hub's data structures.


----

### LISKOV-DATA-HUB-MANAGER-0019

> The {0} integration connector is refreshing data fields from {1} {2} ({3}) for data sharing hub {4} ({5})

|  |  |
|---|---|
| **Java constant** | `LiskovAuditCode.REFRESHING_TABULAR_FILE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |
| **Further reading** | <https://egeria-project.org/concepts/data-sharing-hub/> |

**System action**

The connector is reading the data fields from the columns of a file with a tabular schema, such as a CSV file or an Apache Parquet file, so that it can refresh a data sharing hub.

**User action**

No action is required.  This message identifies the type of file, and the file, that the data fields are being read from.


----

### LISKOV-DATA-HUB-MANAGER-0020

> The {0} integration connector has started engine action {1} to enable the cataloguing of {2} {3} ({4}) using governance action type {5}

|  |  |
|---|---|
| **Java constant** | `LiskovAuditCode.ENABLING_CATALOGUING` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |
| **Further reading** | <https://egeria-project.org/concepts/data-sharing-hub/> |

**System action**

The connector has detected that the contents of a member of a data sharing hub are not being catalogued and has requested that the appropriate cataloguing integration connector begins to monitor it.  The contents of the member will appear in open metadata once the cataloguing integration connector has run.

**User action**

No action is required.  This message is for monitoring the set up of the cataloguing for the members of a data sharing hub.


----

### LISKOV-DATA-HUB-MANAGER-0021

> The {0} integration connector has started engine action {1} to survey {2} {3} ({4}) using governance action type {5} because {6}

|  |  |
|---|---|
| **Java constant** | `LiskovAuditCode.STARTING_SURVEY` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}` |
| **Further reading** | <https://egeria-project.org/concepts/data-sharing-hub/> |

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
| **Further reading** | <https://egeria-project.org/concepts/data-sharing-hub/> |

**System action**

The connector is not able to determine which cataloguing and survey governance action types are appropriate for this element and so it skips them.  The rest of the refresh continues.

**User action**

If this element should be catalogued and surveyed, check that its deployedImplementationType property is set to a technology type that is defined in the content packs loaded into this metadata store.


----

### LISKOV-DATA-HUB-MANAGER-0023

> The {0} integration connector has changed the path name of {1} {2} ({3}) from {4} to its canonical path name {5}

|  |  |
|---|---|
| **Java constant** | `LiskovAuditCode.CANONICAL_PATH_NAME_SET` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |
| **Further reading** | <https://egeria-project.org/concepts/data-sharing-hub/> |

**System action**

The folder or file is a member of a data sharing hub, or is catalogued beneath a folder that is, and it was catalogued with a path name that is local to one machine.  The connector has replaced the local mount point in its path name with the canonical mount point, so that it lines up with the folders and files catalogued by other runtimes.  Its qualified name and its connection are not changed.

**User action**

No action is required unless the canonical path name is wrong.  In that case, correct the mount points and restore the path name.  The mount points come from the file system linked to the folder or one of the folders above it; then from the catalog target relationship that makes the folder a target of the folder cataloguer - its configuration properties, or the file system named as its metadata source - and otherwise from the localMountPoint and canonicalMountPoint configuration properties of this connector.


----

### LISKOV-DATA-HUB-MANAGER-0024

> The {0} integration connector has linked {1} elements {2} and {3} as potential duplicates because they have the same canonical path name {4}

|  |  |
|---|---|
| **Java constant** | `LiskovAuditCode.POTENTIAL_DUPLICATES_LINKED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |
| **Further reading** | <https://egeria-project.org/features/duplicate-management/overview/> |

**System action**

The two elements are in a folder that is a member of a data sharing hub and, now that they have their canonical path names, they turn out to describe the same folder or file.  The connector has linked them with a PeerDuplicateLink relationship whose status is DISCOVERED.  They are not combined on retrieval until the link is validated.

**User action**

No action is required if the Mendel Automated Duplicate Manager is running, since it validates links between files, or folders, that have the same path name.  Otherwise a steward needs to decide whether the elements are duplicates.


----

### LISKOV-DATA-HUB-MANAGER-0025

> The {0} integration connector is ignoring the file systems for file folder {1} because the nearest ones are {2} file systems with mount points: {3}

|  |  |
|---|---|
| **Java constant** | `LiskovAuditCode.AMBIGUOUS_FILE_SYSTEMS` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/concepts/data-sharing-hub/> |

**System action**

The file folder is a member of a data sharing hub.  The connector takes the mount points for the folder from the FileSystem that the folder, or one of the folders above it, is linked to with a CapabilityAssetUse relationship.  The nearest file systems are used - the ones linked to the folder itself or, failing that, to the closest level of folders above it that has any.  More than one was found at that level, even after setting aside the ones whose useType is USES, so there is no telling which one's mount points apply.  The connector carries on as if the folder had no file system, taking the mount points from the folder's catalog target, its own configuration properties or the defaults.

**User action**

Review the CapabilityAssetUse relationships that link the listed file systems to the folder, or to the nearest folders above it.  Remove the ones that are wrong, or set their useType to USES, so that a single file system is left.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
