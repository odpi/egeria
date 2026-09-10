<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# BitolIntegrationConnectorAuditCode

The BitolIntegrationConnectorAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 12 |
| **Message identifiers begin** | `BITOL-INTEGRATION-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.integration.bitol.ffdc.BitolIntegrationConnectorAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/integration-connectors/bitol-integration-connectors](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/bitol-integration-connectors) |
| **Source** | [BitolIntegrationConnectorAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/bitol-integration-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/integration/bitol/ffdc/BitolIntegrationConnectorAuditCode.java) |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [BITOL-INTEGRATION-CONNECTOR-0001](#bitol-integration-connector-0001) | INFO | The {0} integration connector is monitoring topic {1} with connection: {2} for Bitol documents |
| [BITOL-INTEGRATION-CONNECTOR-0002](#bitol-integration-connector-0002) | INFO | The {0} integration connector is monitoring directory {1} (from {2}) for Bitol documents |
| [BITOL-INTEGRATION-CONNECTOR-0003](#bitol-integration-connector-0003) | INFO | The {0} integration connector published the {1} document from file {2} |
| [BITOL-INTEGRATION-CONNECTOR-0004](#bitol-integration-connector-0004) | ERROR | The {0} integration connector is unable to read directory {1} (from {2}): {3} |
| [BITOL-INTEGRATION-CONNECTOR-0005](#bitol-integration-connector-0005) | INFO | The {0} integration connector stored a {1} document as {2} |
| [BITOL-INTEGRATION-CONNECTOR-0006](#bitol-integration-connector-0006) | INFO | The {0} integration connector {1} element {2} for {3} document {4} version {5} |
| [BITOL-INTEGRATION-CONNECTOR-0007](#bitol-integration-connector-0007) | INFO | The {0} integration connector could not fully represent {1} document {2} version {3}: {4} |
| [BITOL-INTEGRATION-CONNECTOR-0008](#bitol-integration-connector-0008) | ERROR | The {0} integration connector skipped a {1} document because it has no identifier; the document begins: {2} |
| [BITOL-INTEGRATION-CONNECTOR-0009](#bitol-integration-connector-0009) | INFO | The {0} integration connector generated and published the {1} document {2} version {3} from element {4} |
| [BITOL-INTEGRATION-CONNECTOR-0011](#bitol-integration-connector-0011) | INFO | The {0} integration connector catalogued the {1} document file {2} as asset {3} with deployed implementation type {4} |
| [BITOL-INTEGRATION-CONNECTOR-0012](#bitol-integration-connector-0012) | INFO | The {0} integration connector could not catalog the {1} document file {2}: {3} |
| [BITOL-INTEGRATION-CONNECTOR-0010](#bitol-integration-connector-0010) | EXCEPTION | The {0} integration connector received an unexpected {1} exception in method {2} when working with Bitol documents; the error message was: {3} |

----

### BITOL-INTEGRATION-CONNECTOR-0001

> The {0} integration connector is monitoring topic {1} with connection: {2} for Bitol documents

|  |  |
|---|---|
| **Java constant** | `BitolIntegrationConnectorAuditCode.TOPIC_RECEIVER_CONFIGURATION` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |
| **Further reading** | <https://egeria-project.org/concepts/digital-product/> |

**System action**

The connector publishes each event received from the topic as a Bitol document to the listeners registered in the same integration daemon.

**User action**

No specific action is required.  This message is to confirm the configuration for the Bitol event receiver integration connector.  It is output for each topic catalog target.


----

### BITOL-INTEGRATION-CONNECTOR-0002

> The {0} integration connector is monitoring directory {1} (from {2}) for Bitol documents

|  |  |
|---|---|
| **Java constant** | `BitolIntegrationConnectorAuditCode.DIRECTORY_MONITORED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |
| **Further reading** | <https://egeria-project.org/concepts/digital-product/> |

**System action**

On each refresh the connector scans the directory tree for YAML and JSON files whose kind is DataContract or DataProduct and publishes the new and changed documents to the listeners registered in the same integration daemon.

**User action**

No specific action is required.  This message is to confirm the configuration for the Bitol files receiver integration connector.  It is output for the connection's endpoint and for each file folder catalog target.


----

### BITOL-INTEGRATION-CONNECTOR-0003

> The {0} integration connector published the {1} document from file {2}

|  |  |
|---|---|
| **Java constant** | `BitolIntegrationConnectorAuditCode.DOCUMENT_PUBLISHED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |
| **Further reading** | <https://egeria-project.org/concepts/digital-product/> |

**System action**

The file is new, or has changed since it was last published, so its content has been passed to the Bitol listeners registered in the same integration daemon.

**User action**

No specific action is required.  This message records which files have been processed.


----

### BITOL-INTEGRATION-CONNECTOR-0004

> The {0} integration connector is unable to read directory {1} (from {2}): {3}

|  |  |
|---|---|
| **Java constant** | `BitolIntegrationConnectorAuditCode.DIRECTORY_NOT_ACCESSIBLE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/concepts/digital-product/> |

**System action**

The connector skips the directory on this refresh and tries again on the next refresh.

**User action**

Check that the directory exists and is readable by the integration daemon's process.  Correct the catalog target or connection endpoint if the path is wrong.


----

### BITOL-INTEGRATION-CONNECTOR-0005

> The {0} integration connector stored a {1} document as {2}

|  |  |
|---|---|
| **Java constant** | `BitolIntegrationConnectorAuditCode.DOCUMENT_STORED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |
| **Further reading** | <https://egeria-project.org/concepts/digital-product/> |

**System action**

A Bitol document published in the integration daemon has been written to the store.

**User action**

No specific action is required.  This message records where documents have been stored.


----

### BITOL-INTEGRATION-CONNECTOR-0006

> The {0} integration connector {1} element {2} for {3} document {4} version {5}

|  |  |
|---|---|
| **Java constant** | `BitolIntegrationConnectorAuditCode.DOCUMENT_CATALOGUED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |
| **Further reading** | <https://egeria-project.org/concepts/digital-product/> |

**System action**

A Bitol document published in the integration daemon has been catalogued in open metadata.  Each version of a document is a separate element, identified by a qualified name that includes the version.

**User action**

No specific action is required.  This message records which documents have been catalogued.


----

### BITOL-INTEGRATION-CONNECTOR-0007

> The {0} integration connector could not fully represent {1} document {2} version {3}: {4}

|  |  |
|---|---|
| **Java constant** | `BitolIntegrationConnectorAuditCode.DOCUMENT_MAPPING_WARNING` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |
| **Further reading** | <https://egeria-project.org/concepts/digital-product/> |

**System action**

Part of the document refers to something that is not yet known to open metadata, for example a team member without a user identity or a data contract that has not been catalogued.  The rest of the document has been catalogued and the missing detail is recorded in the element's additional properties.

**User action**

Catalogue the missing element (for example publish the referenced data contract, or create the user identity and profile) and republish the document to complete the links.


----

### BITOL-INTEGRATION-CONNECTOR-0008

> The {0} integration connector skipped a {1} document because it has no identifier; the document begins: {2}

|  |  |
|---|---|
| **Java constant** | `BitolIntegrationConnectorAuditCode.DOCUMENT_WITHOUT_ID` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |
| **Further reading** | <https://egeria-project.org/concepts/digital-product/> |

**System action**

The Bitol standards require every document to carry an id.  Without it the connector can not build stable names for the elements and so the document is not catalogued.

**User action**

Add an id (typically a UUID) to the document and publish it again.


----

### BITOL-INTEGRATION-CONNECTOR-0009

> The {0} integration connector generated and published the {1} document {2} version {3} from element {4}

|  |  |
|---|---|
| **Java constant** | `BitolIntegrationConnectorAuditCode.DOCUMENT_GENERATED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |
| **Further reading** | <https://egeria-project.org/concepts/digital-product/> |

**System action**

A digital product or data sharing agreement in open metadata has been expressed as a Bitol document and passed to the Bitol listeners registered in the same integration daemon, for example to be stored as a file.

**User action**

No specific action is required.  This message records which documents have been generated.


----

### BITOL-INTEGRATION-CONNECTOR-0011

> The {0} integration connector catalogued the {1} document file {2} as asset {3} with deployed implementation type {4}

|  |  |
|---|---|
| **Java constant** | `BitolIntegrationConnectorAuditCode.FILE_CATALOGUED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |
| **Further reading** | <https://egeria-project.org/features/digital-product-management/overview/> |

**System action**

The file holding a Bitol document has been catalogued from the file template for its format, and linked as a resource of the agreement or digital product catalogued from the document when that element exists.

**User action**

No specific action is required.  This message records which document files have been catalogued.


----

### BITOL-INTEGRATION-CONNECTOR-0012

> The {0} integration connector could not catalog the {1} document file {2}: {3}

|  |  |
|---|---|
| **Java constant** | `BitolIntegrationConnectorAuditCode.FILE_NOT_CATALOGUED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/content-packs/files-content-pack/overview/> |

**System action**

The document was published to the Bitol listeners as normal, but the file itself has not been catalogued as an asset.  The usual cause is that the file templates from the Files Content Pack are not loaded in the metadata access store.

**User action**

Load the Files Content Pack (or another content pack that supplies the YAML and JSON file templates) if the document files should appear in the catalog.  Otherwise no action is required.


----

### BITOL-INTEGRATION-CONNECTOR-0010

> The {0} integration connector received an unexpected {1} exception in method {2} when working with Bitol documents; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `BitolIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/concepts/digital-product/> |

**System action**

The connector cannot process one or more Bitol documents.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
