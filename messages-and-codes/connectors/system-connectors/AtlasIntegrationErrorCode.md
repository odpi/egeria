<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# AtlasIntegrationErrorCode

The AtlasIntegrationErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the Apache Atlas integration connector. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 5 |
| **Message identifiers begin** | `APACHE-ATLAS-INTEGRATION-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ffdc.AtlasIntegrationErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/system-connectors/apache-atlas-connectors](../../../open-metadata-implementation/adapters/open-connectors/system-connectors/apache-atlas-connectors) |
| **Source** | [AtlasIntegrationErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/system-connectors/apache-atlas-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/apacheatlas/integration/ffdc/AtlasIntegrationErrorCode.java) |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-apache-atlas/overview/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-400-003](#apache-atlas-integration-connector-400-003) | 400 | Integration connector {0} has been configured without a metadataSourceQualifiedName value |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-400-004](#apache-atlas-integration-connector-400-004) | 400 | Integration connector {0} cannot create an Apache Atlas REST Connector |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-404-001](#apache-atlas-integration-connector-404-001) | 404 | The {0} integration connector can not retrieve the correlation information for {1} open metadata entity {2} linked in Apache Atlas {3} to {4} entity {5} |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-404-002](#apache-atlas-integration-connector-404-002) | 404 | The {0} integration connector can not retrieve the atlas GUID for {1} open metadata entity {2} and metadata collection {3} |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-500-001](#apache-atlas-integration-connector-500-001) | 500 | The {0} connector received an unexpected exception {1} during method {2}; the error message was: {3} |

----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-400-003

> Integration connector {0} has been configured without a metadataSourceQualifiedName value

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationErrorCode.NULL_ASSET_MANAGER` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The connector uses the metadataSourceQualifiedName to identify the metadata that originated in Apache Atlas so that any updates/deletes to this metadata are reflected into the open metadata ecosystem.  Otherwise, any changes will be overridden by the values in the open metadata ecosystem. In order to ensure metadata integrity, the connector is moved to FAILED status and will no longer be called to synchronize metadata until the metadata source name has been supplied.

**User action**

Update the metadata source qualified name for the connector.  This may have been supplied through the Integration Daemon's configuration, or, if the Integration Daemon is using integration groups, the connection information is stored in the connector's RegisteredIntegrationConnector relationship in the open metadata ecosystem.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-400-004

> Integration connector {0} cannot create an Apache Atlas REST Connector

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationErrorCode.NULL_ATLAS_CLIENT` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The connector uses the Apache Atlas REST Connector to make REST calls to Apache Atlas. The connector is moved to FAILED status and will no longer be called to synchronize metadata until the problem creating the Apache Atlas REST Connector is resolved.

**User action**

Ensure that the jar file for the Apache Atlas REST Connector is in the class path of the platform.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-404-001

> The {0} integration connector can not retrieve the correlation information for {1} open metadata entity {2} linked in Apache Atlas {3} to {4} entity {5}

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationErrorCode.MISSING_CORRELATION` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The correlation information that should be associated with the open metadata entity is missing and the integration connector is not able to confidently synchronize it with the Apache Atlas entity.

**User action**

Review the audit log to determine if there were errors detected when the open metadata entity was created.  The simplest resolution is to delete the open metadata entity.  However, if this entity has been enhanced with many attachments and classifications then it is also possible to add the correlation information to the open metadata entity to allow the synchronization to continue.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-404-002

> The {0} integration connector can not retrieve the atlas GUID for {1} open metadata entity {2} and metadata collection {3}

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationErrorCode.MISSING_ATLAS_GUID` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

There is no Apache Atlas correlation information for this element.

**User action**

Review the follow on messages.  If there are none, it is just a timing issue.  If there are subsequent error messages then follow their instructions.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-500-001

> The {0} connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot work with one or more metadata elements.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
