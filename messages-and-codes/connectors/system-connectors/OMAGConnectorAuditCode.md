<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OMAGConnectorAuditCode

The OMAGConnectorAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 15 |
| **Message identifiers begin** | `OMAG-CONNECTORS-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.ffdc.OMAGConnectorAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/system-connectors/egeria-system-connectors](../../../open-metadata-implementation/adapters/open-connectors/system-connectors/egeria-system-connectors) |
| **Source** | [OMAGConnectorAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/system-connectors/egeria-system-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/egeriainfrastructure/ffdc/OMAGConnectorAuditCode.java) |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OMAG-CONNECTORS-0001](#omag-connectors-0001) | ERROR | The {0} Egeria Connector received an unexpected exception {1} during method {2}; the error message was: {3} |
| [OMAG-CONNECTORS-0002](#omag-connectors-0002) | INFO | The {0} Egeria Connector has been started.  The monitored platforms are: {1} |
| [OMAG-CONNECTORS-0005](#omag-connectors-0005) | INFO | The {0} integration connector has created a new {1} server element {2} for server {3} on platform {4} |
| [OMAG-CONNECTORS-0006](#omag-connectors-0006) | INFO | The {0} integration connector has detected that the {1} server of type {2} has no metadata collection id |
| [OMAG-CONNECTORS-0010](#omag-connectors-0010) | INFO | Connector {0} has started monitoring OMAG Server Platform: {1} |
| [OMAG-CONNECTORS-0011](#omag-connectors-0011) | INFO | Connector {0} is synchronizing metadata for OMAG Server Platform: {1} |
| [OMAG-CONNECTORS-0012](#omag-connectors-0012) | EXCEPTION | The {0} integration connector was not able to catalog server {1} on platform {2}; the {3} exception returned message {4} |
| [OMAG-CONNECTORS-0013](#omag-connectors-0013) | INFO | The {0} integration connector has renamed platform element {1} from {2} to {3} so that it can be recognised again |
| [OMAG-CONNECTORS-0014](#omag-connectors-0014) | INFO | The {0} integration connector is now monitoring the OMAG Server Platform at {1}, catalogued as {2} |
| [OMAG-CONNECTORS-0015](#omag-connectors-0015) | EXCEPTION | The {0} integration connector could not register the OMAG Server Platform at {1} yet; the {2} exception returned message {3} |
| [OMAG-CONNECTORS-0016](#omag-connectors-0016) | INFO | The {0} integration connector has added audit log topic {1} ({2}) to the catalog targets of integration connector {3} |
| [OMAG-CONNECTORS-0017](#omag-connectors-0017) | INFO | The {0} integration connector has not catalogued the OMAG Server Platform at {1} because it is the same platform as the one at {2} |
| [OMAG-CONNECTORS-0018](#omag-connectors-0018) | INFO | The {0} integration connector has removed the audit log lineage from server {1} to topic {2} ({3}) because the server's configuration now names audit log topic {4} |
| [OMAG-CONNECTORS-0019](#omag-connectors-0019) | INFO | The {0} integration connector has removed audit log topic {1} ({2}) from the catalog targets of integration connector {3} because no server writes its audit log to it |
| [OMAG-CONNECTORS-0020](#omag-connectors-0020) | INFO | The {0} integration connector has updated the connection for audit log topic {1} ({2}): {3} was {4} and is now {5} |

----

### OMAG-CONNECTORS-0001

> The {0} Egeria Connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `OMAGConnectorAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-egeria/overview/> |

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
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-egeria/overview/> |

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
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-egeria/overview/> |

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
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-egeria/overview/> |

**System action**

No metadata collection asset nor inventory catalog software capability is connected to the server.

**User action**

This is only ok if the server is a metadata access point.


----

### OMAG-CONNECTORS-0010

> Connector {0} has started monitoring OMAG Server Platform: {1}

|  |  |
|---|---|
| **Java constant** | `OMAGConnectorAuditCode.EGERIA_TARGET_START` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-egeria/overview/> |

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
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-egeria/overview/> |

**System action**

The connector is synchronizing the configuration of the platform and its servers with its open metadata description.

**User action**

No specific action is required.  This message is to confirm the refreshing of the target processor.


----

### OMAG-CONNECTORS-0012

> The {0} integration connector was not able to catalog server {1} on platform {2}; the {3} exception returned message {4}

|  |  |
|---|---|
| **Java constant** | `OMAGConnectorAuditCode.SERVER_CATALOG_FAILED` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-egeria/overview/> |

**System action**

The connector moves on to the platform's other servers.  This server's open metadata description is left as it was, which means it no longer reflects the running server.

**User action**

Use the message to work out why this one server could not be catalogued.  A duplicate qualified name usually means the server, or something belonging to it, has already been catalogued under a name that this connector no longer computes - most often because the server has been renamed, moved to another platform, or catalogued by an earlier release.


----

### OMAG-CONNECTORS-0013

> The {0} integration connector has renamed platform element {1} from {2} to {3} so that it can be recognised again

|  |  |
|---|---|
| **Java constant** | `OMAGConnectorAuditCode.PLATFORM_NAME_MIGRATED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-egeria/overview/> |

**System action**

The connector carries on using the element it renamed, so the platform's history, its servers and anything else attached to it are kept.

**User action**

No action is required.  Earlier releases named a platform element after the platform's own name and organization, which is not unique to one running platform and is not the name this connector looks the element up by.  This message records the one-off correction; it should not appear again for the same platform.


----

### OMAG-CONNECTORS-0014

> The {0} integration connector is now monitoring the OMAG Server Platform at {1}, catalogued as {2}

|  |  |
|---|---|
| **Java constant** | `OMAGConnectorAuditCode.PLATFORM_CATALOG_TARGET_ADDED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-egeria/overview/> |

**System action**

The platform has been registered as one of this connector's catalog targets, so its configuration and servers are synchronized with open metadata on every refresh.

**User action**

No specific action is required.  This message records a platform being taken under management, which happens the first time the connector runs against it.


----

### OMAG-CONNECTORS-0015

> The {0} integration connector could not register the OMAG Server Platform at {1} yet; the {2} exception returned message {3}

|  |  |
|---|---|
| **Java constant** | `OMAGConnectorAuditCode.PLATFORM_REGISTRATION_DEFERRED` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-egeria/overview/> |

**System action**

The connector carries on with the catalog targets it already has, and tries this platform again on its next refresh.  The platform is not monitored in the meantime.

**User action**

This is usually temporary - most often the metadata access store was restarting, or had not finished loading its content packs, when the connector started.  If the message keeps appearing, use the exception to work out why the connector cannot write to the metadata access store.


----

### OMAG-CONNECTORS-0016

> The {0} integration connector has added audit log topic {1} ({2}) to the catalog targets of integration connector {3}

|  |  |
|---|---|
| **Java constant** | `OMAGConnectorAuditCode.AUDIT_LOG_TOPIC_REGISTERED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/types/7/0795-Usage-Activity/> |

**System action**

The topic that the OMAG Servers write their audit logs to is now one of the catalog targets of the integration connector that observes the activity recorded there.  This is normally the Pepys Activity Observer, which maintains the usage activity counts of the assets reported in the audit log records.

**User action**

No specific action is required.  This message records an audit log topic being handed to the activity observer, which happens the first time the topic is catalogued.


----

### OMAG-CONNECTORS-0017

> The {0} integration connector has not catalogued the OMAG Server Platform at {1} because it is the same platform as the one at {2}

|  |  |
|---|---|
| **Java constant** | `OMAGConnectorAuditCode.PLATFORM_ALIAS_IGNORED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-egeria/overview/> |

**System action**

The platforms at both addresses reported the same start time and origin, so they are one running platform reached by two different addresses.  The connector catalogues and monitors it only under the address it reached first, and does not register the other address as a catalog target.

**User action**

No action is required.  This is expected where a server's localServerURL is an external address of the platform (for example https://host.docker.internal:9443) and the metadata access server is reached through an internal one (for example https://localhost:9443).  Elements catalogued under the other address by an earlier release of this connector are no longer maintained and can be deleted.


----

### OMAG-CONNECTORS-0018

> The {0} integration connector has removed the audit log lineage from server {1} to topic {2} ({3}) because the server's configuration now names audit log topic {4}

|  |  |
|---|---|
| **Java constant** | `OMAGConnectorAuditCode.AUDIT_LOG_TOPIC_UNLINKED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |
| **Further reading** | <https://egeria-project.org/types/7/0795-Usage-Activity/> |

**System action**

The server's audit log destinations have changed since the lineage was catalogued, so the server no longer writes its audit log to this topic.  The lineage relationship is removed, and lineage to the topic named in the configuration is added in its place.

**User action**

No action is required.  If the topic is no longer used by any server, it is also removed from the catalog targets of the activity observer.  The topic element itself is not deleted, since the topic may still exist in the event broker.


----

### OMAG-CONNECTORS-0019

> The {0} integration connector has removed audit log topic {1} ({2}) from the catalog targets of integration connector {3} because no server writes its audit log to it

|  |  |
|---|---|
| **Java constant** | `OMAGConnectorAuditCode.AUDIT_LOG_TOPIC_UNREGISTERED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/types/7/0795-Usage-Activity/> |

**System action**

The last server that wrote its audit log to this topic has been reconfigured to use a different destination.  The activity observer stops listening to the topic.

**User action**

No action is required.  The topic element remains in open metadata and can be deleted if the topic has also been removed from the event broker.


----

### OMAG-CONNECTORS-0020

> The {0} integration connector has updated the connection for audit log topic {1} ({2}): {3} was {4} and is now {5}

|  |  |
|---|---|
| **Java constant** | `OMAGConnectorAuditCode.AUDIT_LOG_TOPIC_CONNECTION_UPDATED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |
| **Further reading** | <https://egeria-project.org/types/7/0795-Usage-Activity/> |

**System action**

The connection catalogued for the audit log topic no longer matched the configuration of the servers that write to it, so it has been updated.  Connectors that read the topic through this connection, such as the activity observer, use the new value when they next connect.

**User action**

No action is required.  If the activity observer is already connected to the topic, it may need to be restarted, or its catalog target refreshed, to pick up the change.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
