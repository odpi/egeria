<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# DcatIntegrationConnectorAuditCode

The DcatIntegrationConnectorAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 16 |
| **Message identifiers begin** | `DCAT-INTEGRATION-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.integration.dcat.ffdc.DcatIntegrationConnectorAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/integration-connectors/dcat-integration-connectors](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/dcat-integration-connectors) |
| **Source** | [DcatIntegrationConnectorAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/dcat-integration-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/integration/dcat/ffdc/DcatIntegrationConnectorAuditCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/asset/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [DCAT-INTEGRATION-CONNECTOR-0001](#dcat-integration-connector-0001) | INFO | The {0} integration connector is monitoring topic {1} with connection: {2} for DCAT documents |
| [DCAT-INTEGRATION-CONNECTOR-0002](#dcat-integration-connector-0002) | INFO | The {0} integration connector is monitoring directory {1} (from {2}) for DCAT documents |
| [DCAT-INTEGRATION-CONNECTOR-0003](#dcat-integration-connector-0003) | INFO | The {0} integration connector published the DCAT document from file {1} |
| [DCAT-INTEGRATION-CONNECTOR-0004](#dcat-integration-connector-0004) | ERROR | The {0} integration connector is unable to use directory {1} (from {2}): {3} |
| [DCAT-INTEGRATION-CONNECTOR-0005](#dcat-integration-connector-0005) | INFO | The {0} integration connector {1} element {2} ({3}) for DCAT {4} {5} |
| [DCAT-INTEGRATION-CONNECTOR-0006](#dcat-integration-connector-0006) | ERROR | The {0} integration connector was not able to catalog everything in a DCAT document: {1} |
| [DCAT-INTEGRATION-CONNECTOR-0007](#dcat-integration-connector-0007) | INFO | The {0} integration connector has no collections attached as catalog targets, so there are no DCAT catalogs to publish |
| [DCAT-INTEGRATION-CONNECTOR-0008](#dcat-integration-connector-0008) | ERROR | The {0} integration connector has no directory to publish DCAT descriptions to |
| [DCAT-INTEGRATION-CONNECTOR-0009](#dcat-integration-connector-0009) | INFO | The {0} integration connector published the DCAT description of {1} {2} ({3}) to file {4} |
| [DCAT-INTEGRATION-CONNECTOR-0010](#dcat-integration-connector-0010) | INFO | The {0} integration connector published the DCAT catalog for collection {1} to file {2}; it describes {3} dataset(s) and {4} data service(s) |
| [DCAT-INTEGRATION-CONNECTOR-0011](#dcat-integration-connector-0011) | INFO | The {0} integration connector removed file {1} because the asset it describes is no longer in the collection being published |
| [DCAT-INTEGRATION-CONNECTOR-0012](#dcat-integration-connector-0012) | ERROR | The {0} integration connector was not able to generate the DCAT description of element {1}; the {2} exception had message: {3} |
| [DCAT-INTEGRATION-CONNECTOR-0013](#dcat-integration-connector-0013) | EXCEPTION | The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3} |
| [DCAT-INTEGRATION-CONNECTOR-0014](#dcat-integration-connector-0014) | ERROR | The {0} integration connector is ignoring catalog target {1} {2} because it is not a collection |
| [DCAT-INTEGRATION-CONNECTOR-0015](#dcat-integration-connector-0015) | EXCEPTION | The {0} integration connector was not able to publish collection {1} as a DCAT catalog; the {2} exception had message: {3} |
| [DCAT-INTEGRATION-CONNECTOR-0016](#dcat-integration-connector-0016) | INFO | The {0} integration connector removed directory {1} because the collection it describes is no longer a catalog target |

----

### DCAT-INTEGRATION-CONNECTOR-0001

> The {0} integration connector is monitoring topic {1} with connection: {2} for DCAT documents

|  |  |
|---|---|
| **Java constant** | `DcatIntegrationConnectorAuditCode.TOPIC_RECEIVER_CONFIGURATION` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector publishes each event received from the topic as a DCAT document to the listeners registered in the same integration daemon.

**User action**

No specific action is required.  This message is to confirm the configuration for the DCAT event receiver integration connector.  It is output for each topic catalog target.


----

### DCAT-INTEGRATION-CONNECTOR-0002

> The {0} integration connector is monitoring directory {1} (from {2}) for DCAT documents

|  |  |
|---|---|
| **Java constant** | `DcatIntegrationConnectorAuditCode.DIRECTORY_MONITORED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

On each refresh the connector scans the directory tree for JSON-LD files that describe DCAT catalogs, datasets, dataset series or data services and publishes the new and changed documents to the listeners registered in the same integration daemon.

**User action**

No specific action is required.  This message is to confirm the configuration for the DCAT files receiver integration connector.  It is output for the connection's endpoint and for each file folder catalog target.


----

### DCAT-INTEGRATION-CONNECTOR-0003

> The {0} integration connector published the DCAT document from file {1}

|  |  |
|---|---|
| **Java constant** | `DcatIntegrationConnectorAuditCode.DOCUMENT_PUBLISHED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The file is new, or has changed since it was last published, so its content has been passed to the DCAT listeners registered in the same integration daemon.

**User action**

No specific action is required.  This message records which files have been processed.


----

### DCAT-INTEGRATION-CONNECTOR-0004

> The {0} integration connector is unable to use directory {1} (from {2}): {3}

|  |  |
|---|---|
| **Java constant** | `DcatIntegrationConnectorAuditCode.DIRECTORY_NOT_ACCESSIBLE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector skips the directory on this refresh and tries again on the next refresh.

**User action**

Check that the directory exists and that the integration daemon's process has the access it needs.  Correct the catalog target or connection endpoint if the path is wrong.


----

### DCAT-INTEGRATION-CONNECTOR-0005

> The {0} integration connector {1} element {2} ({3}) for DCAT {4} {5}

|  |  |
|---|---|
| **Java constant** | `DcatIntegrationConnectorAuditCode.RESOURCE_CATALOGUED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

A resource described in a DCAT document has been catalogued in open metadata.  A dataset is catalogued as a data set asset with an asset for each distribution, a data service as a deployed API and a catalog as a collection folder.

**User action**

No specific action is required.  Use the element's unique identifier to retrieve it from open metadata.  Add it to the DCAT Publishing Catalog collection if it is to be published by this deployment.


----

### DCAT-INTEGRATION-CONNECTOR-0006

> The {0} integration connector was not able to catalog everything in a DCAT document: {1}

|  |  |
|---|---|
| **Java constant** | `DcatIntegrationConnectorAuditCode.DOCUMENT_MAPPING_WARNING` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector has catalogued the rest of the document.

**User action**

Use the message to locate the part of the document that needs to be corrected, then send the document again.  The elements that were catalogued are updated rather than duplicated.


----

### DCAT-INTEGRATION-CONNECTOR-0007

> The {0} integration connector has no collections attached as catalog targets, so there are no DCAT catalogs to publish

|  |  |
|---|---|
| **Java constant** | `DcatIntegrationConnectorAuditCode.NO_PUBLISHING_CATALOGS` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |

**System action**

No DCAT descriptions are published.  The connector looks for its catalog targets again on its next refresh.

**User action**

Attach each collection that lists the assets to publish to the connector as a catalog target.  The DCAT Content Pack attaches the DCAT Publishing Catalog collection.


----

### DCAT-INTEGRATION-CONNECTOR-0008

> The {0} integration connector has no directory to publish DCAT descriptions to

|  |  |
|---|---|
| **Java constant** | `DcatIntegrationConnectorAuditCode.NO_DESTINATION` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}` |

**System action**

No DCAT descriptions are published.

**User action**

Set the network address of the endpoint in the connector's connection to the name of the destination directory, and restart the connector.


----

### DCAT-INTEGRATION-CONNECTOR-0009

> The {0} integration connector published the DCAT description of {1} {2} ({3}) to file {4}

|  |  |
|---|---|
| **Java constant** | `DcatIntegrationConnectorAuditCode.RESOURCE_STORED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The asset is a member of a collection that is published as a DCAT catalog and its DCAT description is new, or has changed since it was last published, so the file has been written.

**User action**

No specific action is required.  This message records what has been published and where.


----

### DCAT-INTEGRATION-CONNECTOR-0010

> The {0} integration connector published the DCAT catalog for collection {1} to file {2}; it describes {3} dataset(s) and {4} data service(s)

|  |  |
|---|---|
| **Java constant** | `DcatIntegrationConnectorAuditCode.CATALOG_STORED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The catalog file describes the collection as a DCAT catalog, along with every asset that is a member of the collection or of the collection folders nested in it.  It has been written because its content has changed.

**User action**

No specific action is required.  The catalog file is the one to give to a portal that harvests DCAT catalogs.


----

### DCAT-INTEGRATION-CONNECTOR-0011

> The {0} integration connector removed file {1} because the asset it describes is no longer in the collection being published

|  |  |
|---|---|
| **Java constant** | `DcatIntegrationConnectorAuditCode.RESOURCE_WITHDRAWN` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The file was written by an earlier refresh for an asset that has since been removed from the collection, or deleted, or renamed.

**User action**

No specific action is required.  Add the asset to the collection again if it was removed by mistake.


----

### DCAT-INTEGRATION-CONNECTOR-0012

> The {0} integration connector was not able to generate the DCAT description of element {1}; the {2} exception had message: {3}

|  |  |
|---|---|
| **Java constant** | `DcatIntegrationConnectorAuditCode.RESOURCE_NOT_PUBLISHED` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The element is left out of the published catalog.  The connector carries on with the other members of the collection and tries this one again on its next refresh.

**User action**

Use the message from the exception to determine what is wrong with the element.  A common cause is that the connector's userId is not permitted to read the element or something attached to it.


----

### DCAT-INTEGRATION-CONNECTOR-0013

> The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `DcatIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector is unable to complete the request it was processing.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### DCAT-INTEGRATION-CONNECTOR-0014

> The {0} integration connector is ignoring catalog target {1} {2} because it is not a collection

|  |  |
|---|---|
| **Java constant** | `DcatIntegrationConnectorAuditCode.CATALOG_TARGET_NOT_COLLECTION` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

Only collections can be published as DCAT catalogs.  The connector publishes its other catalog targets.

**User action**

Remove the catalog target relationship and attach the collection that lists the assets to publish instead.  The directory to publish to is set in the network address of the endpoint in the connector's connection.


----

### DCAT-INTEGRATION-CONNECTOR-0015

> The {0} integration connector was not able to publish collection {1} as a DCAT catalog; the {2} exception had message: {3}

|  |  |
|---|---|
| **Java constant** | `DcatIntegrationConnectorAuditCode.CATALOG_NOT_PUBLISHED` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector carries on with its other catalog targets and tries this collection again on its next refresh.  No published catalogs are withdrawn on this refresh.

**User action**

Use the message from the exception to determine what is wrong with the collection.  A common cause is that the connector's userId is not permitted to read it.


----

### DCAT-INTEGRATION-CONNECTOR-0016

> The {0} integration connector removed directory {1} because the collection it describes is no longer a catalog target

|  |  |
|---|---|
| **Java constant** | `DcatIntegrationConnectorAuditCode.CATALOG_WITHDRAWN` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The directory was written by an earlier refresh for a collection that has since been detached from the connector, or deleted, or renamed.

**User action**

No specific action is required.  Attach the collection to the connector as a catalog target again if it was removed by mistake.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
