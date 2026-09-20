<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# DarwinAuditCode

The DarwinAuditCode is used to define the message content for the Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 15 |
| **Message identifiers begin** | `DARWIN-PRODUCT-DEPENDENCY-MANAGER-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.darwin.ffdc.DarwinAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/nanny-connectors](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors) |
| **Source** | [DarwinAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/darwin/ffdc/DarwinAuditCode.java) |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [DARWIN-PRODUCT-DEPENDENCY-MANAGER-0001](#darwin-product-dependency-manager-0001) | EXCEPTION | The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3} |
| [DARWIN-PRODUCT-DEPENDENCY-MANAGER-0002](#darwin-product-dependency-manager-0002) | INFO | The {0} integration connector is starting to maintain the digital product dependencies in server {1} on platform {2}; lineage paths are followed for up to {3} steps |
| [DARWIN-PRODUCT-DEPENDENCY-MANAGER-0003](#darwin-product-dependency-manager-0003) | INFO | The {0} integration connector has created the {1} exception type ({2}) to record the digital products whose externally asserted dependencies are not proven by lineage |
| [DARWIN-PRODUCT-DEPENDENCY-MANAGER-0004](#darwin-product-dependency-manager-0004) | INFO | The {0} integration connector has recorded that digital product {1} depends on digital product {2} through information supply chain {3} (relationship {4}) |
| [DARWIN-PRODUCT-DEPENDENCY-MANAGER-0005](#darwin-product-dependency-manager-0005) | INFO | The {0} integration connector has removed the dependency ({1}) of digital product {2} on digital product {3} through information supply chain {4} because the lineage no longer supports it |
| [DARWIN-PRODUCT-DEPENDENCY-MANAGER-0006](#darwin-product-dependency-manager-0006) | INFO | The {0} integration connector has set the information supply chain of the dependency ({1}) of digital product {2} on digital product {3}, asserted by {4}, to {5} |
| [DARWIN-PRODUCT-DEPENDENCY-MANAGER-0007](#darwin-product-dependency-manager-0007) | ACTION | The {0} integration connector has recorded an exception ({1}) against digital product {2} because {3} of its dependencies asserted by external users are not proven by lineage: {4} |
| [DARWIN-PRODUCT-DEPENDENCY-MANAGER-0008](#darwin-product-dependency-manager-0008) | INFO | The {0} integration connector has cleared the exception ({1}) against digital product {2} because all of its dependencies asserted by external users are now proven by lineage |
| [DARWIN-PRODUCT-DEPENDENCY-MANAGER-0009](#darwin-product-dependency-manager-0009) | INFO | The {0} integration connector stopped following {1} lineage paths because they exceeded the limit of {2} steps |
| [DARWIN-PRODUCT-DEPENDENCY-MANAGER-0010](#darwin-product-dependency-manager-0010) | INFO | The {0} integration connector reviewed {1} digital products and derived {2} dependencies from their lineage; it created {3} relationships, removed {4}, set the information supply chain on {5} and found {6} unproven; it raised {7} exceptions, updated {8} and cleared {9} |
| [DARWIN-PRODUCT-DEPENDENCY-MANAGER-0011](#darwin-product-dependency-manager-0011) | INFO | The {0} integration connector has stopped maintaining the digital product dependencies in server {1} on platform {2} |
| [DARWIN-PRODUCT-DEPENDENCY-MANAGER-0012](#darwin-product-dependency-manager-0012) | INFO | The {0} integration connector has recorded that data flows from {1} to {2} through information supply chain {3} (relationship {4}) because {5} |
| [DARWIN-PRODUCT-DEPENDENCY-MANAGER-0013](#darwin-product-dependency-manager-0013) | INFO | The {0} integration connector has removed the data flow ({1}) from {2} to {3} through information supply chain {4} because the finer-grained lineage no longer supports it |
| [DARWIN-PRODUCT-DEPENDENCY-MANAGER-0014](#darwin-product-dependency-manager-0014) | INFO | The {0} integration connector has set the information supply chain of the data flow ({1}) from {2} to {3}, asserted by {4}, to {5} |
| [DARWIN-PRODUCT-DEPENDENCY-MANAGER-0015](#darwin-product-dependency-manager-0015) | INFO | The {0} integration connector derived {1} data flows between data assets from the data mappings between their schema elements, and {2} data flows between software servers from the lineage between the data assets their capabilities own; it created {3} DataFlow relationships, removed {4} and set the information supply chain on {5} |

----

### DARWIN-PRODUCT-DEPENDENCY-MANAGER-0001

> The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `DarwinAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/concepts/digital-product/> |

**System action**

The connector is unable to complete the maintenance of the digital product dependencies.  The dependencies are left as they were at the point of failure and are reconciled again on the next refresh.

**User action**

Use the details from the error message to determine the cause of the error and correct it.


----

### DARWIN-PRODUCT-DEPENDENCY-MANAGER-0002

> The {0} integration connector is starting to maintain the digital product dependencies in server {1} on platform {2}; lineage paths are followed for up to {3} steps

|  |  |
|---|---|
| **Java constant** | `DarwinAuditCode.STARTING_CONNECTOR` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/concepts/digital-product/> |

**System action**

On each refresh, the connector works upwards from the finest-grained lineage: it derives data flows between data assets from the data mappings between their schema elements, data flows between software servers from the lineage between the data assets their capabilities own, and dependencies between digital products from the data lineage between the assets that are members of the products.  At each level it creates the relationships that are missing, removes the ones it created earlier that the finer-grained lineage no longer supports, and fills in the information supply chain on relationships asserted by external users.  It also records an exception against each product whose externally asserted dependencies are not proven by lineage.

**User action**

No action is required.  This message is for monitoring the start up of the product dependency manager.


----

### DARWIN-PRODUCT-DEPENDENCY-MANAGER-0003

> The {0} integration connector has created the {1} exception type ({2}) to record the digital products whose externally asserted dependencies are not proven by lineage

|  |  |
|---|---|
| **Java constant** | `DarwinAuditCode.NEW_EXCEPTION_TYPE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |
| **Further reading** | <https://egeria-project.org/types/4/0455-Exception-Management/> |

**System action**

The exception type is created because there was no existing exception type with this name.  All of the exceptions raised by this connector are linked to it.

**User action**

No action is required.  Link the exception type to the appropriate governance policy if the organization wants the exceptions it records to be reviewed as part of the governance program.


----

### DARWIN-PRODUCT-DEPENDENCY-MANAGER-0004

> The {0} integration connector has recorded that digital product {1} depends on digital product {2} through information supply chain {3} (relationship {4})

|  |  |
|---|---|
| **Java constant** | `DarwinAuditCode.DEPENDENCY_CREATED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |
| **Further reading** | <https://egeria-project.org/concepts/digital-product/> |

**System action**

The data lineage shows that data flows from an asset that is a member of the second product to an asset that is a member of the first, and there was no DigitalProductDependency relationship recording this for the information supply chain, so the connector has created one.

**User action**

No action is required.  The relationship is removed automatically if the lineage stops supporting it.


----

### DARWIN-PRODUCT-DEPENDENCY-MANAGER-0005

> The {0} integration connector has removed the dependency ({1}) of digital product {2} on digital product {3} through information supply chain {4} because the lineage no longer supports it

|  |  |
|---|---|
| **Java constant** | `DarwinAuditCode.DEPENDENCY_REMOVED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |
| **Further reading** | <https://egeria-project.org/concepts/digital-product/> |

**System action**

The connector created this relationship on an earlier refresh from the data lineage between the products' assets.  That lineage is no longer there - or it is now recorded by a relationship asserted by an external user - so the connector's own relationship is removed.  Relationships asserted by external users are never removed.

**User action**

No action is required.  If the dependency is real but the lineage is missing, correct the lineage and the relationship is recreated on the next refresh.


----

### DARWIN-PRODUCT-DEPENDENCY-MANAGER-0006

> The {0} integration connector has set the information supply chain of the dependency ({1}) of digital product {2} on digital product {3}, asserted by {4}, to {5}

|  |  |
|---|---|
| **Java constant** | `DarwinAuditCode.SUPPLY_CHAIN_SET` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |
| **Further reading** | <https://egeria-project.org/concepts/information-supply-chain/> |

**System action**

A DigitalProductDependency relationship asserted by an external user did not name an information supply chain.  The data lineage between the products' assets proves the dependency, so the connector has filled in the information supply chain from the first lineage path it found.

**User action**

No action is required.  If the dependency belongs to a different information supply chain, correct the relationship; the connector does not change an information supply chain once it is set.


----

### DARWIN-PRODUCT-DEPENDENCY-MANAGER-0007

> The {0} integration connector has recorded an exception ({1}) against digital product {2} because {3} of its dependencies asserted by external users are not proven by lineage: {4}

|  |  |
|---|---|
| **Java constant** | `DarwinAuditCode.UNPROVEN_DEPENDENCIES` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |
| **Further reading** | <https://egeria-project.org/types/4/0455-Exception-Management/> |

**System action**

The listed DigitalProductDependency relationships were asserted by external users, but there is no data lineage path between the assets of the two products for the information supply chain named in the relationship.  The connector does not remove relationships it did not create, so it records the discrepancy as an Exception relationship linking the dependent product to its exception type.  The affectedRelationships property of the exception lists the unproven relationships.

**User action**

Review the listed relationships.  Either add the missing lineage, correct the information supply chain named on the relationship, or remove the relationship if the dependency does not exist.  The exception is cleared automatically once none of the product's externally asserted dependencies are unproven.


----

### DARWIN-PRODUCT-DEPENDENCY-MANAGER-0008

> The {0} integration connector has cleared the exception ({1}) against digital product {2} because all of its dependencies asserted by external users are now proven by lineage

|  |  |
|---|---|
| **Java constant** | `DarwinAuditCode.EXCEPTION_CLEARED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |
| **Further reading** | <https://egeria-project.org/types/4/0455-Exception-Management/> |

**System action**

The unproven dependencies that the exception recorded have either been removed or are now supported by the data lineage between the products' assets, so the Exception relationship is deleted.

**User action**

No action is required.  This message is for monitoring the resolution of exceptions.


----

### DARWIN-PRODUCT-DEPENDENCY-MANAGER-0009

> The {0} integration connector stopped following {1} lineage paths because they exceeded the limit of {2} steps

|  |  |
|---|---|
| **Java constant** | `DarwinAuditCode.LINEAGE_DEPTH_EXCEEDED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |
| **Further reading** | <https://egeria-project.org/concepts/digital-product/> |

**System action**

The connector follows the data lineage downstream from each asset that is a member of a digital product, or is owned by a software server's capability, until it reaches an asset that belongs to another product or server.  A path that has not reached one within the configured number of steps is abandoned, so a relationship at the end of a very long path is not recorded.

**User action**

If the lineage graph legitimately contains long paths between products or servers, increase the maxLineageDepth configuration property of the connector.


----

### DARWIN-PRODUCT-DEPENDENCY-MANAGER-0010

> The {0} integration connector reviewed {1} digital products and derived {2} dependencies from their lineage; it created {3} relationships, removed {4}, set the information supply chain on {5} and found {6} unproven; it raised {7} exceptions, updated {8} and cleared {9}

|  |  |
|---|---|
| **Java constant** | `DarwinAuditCode.REFRESH_COMPLETE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}`, `{7}`, `{8}`, `{9}` |
| **Further reading** | <https://egeria-project.org/concepts/digital-product/> |

**System action**

The refresh has completed and the DigitalProductDependency relationships are consistent with the data lineage between the products' assets.

**User action**

No action is required.  This message is for monitoring the operation of the product dependency manager.


----

### DARWIN-PRODUCT-DEPENDENCY-MANAGER-0011

> The {0} integration connector has stopped maintaining the digital product dependencies in server {1} on platform {2}

|  |  |
|---|---|
| **Java constant** | `DarwinAuditCode.CONNECTOR_STOPPING` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |
| **Further reading** | <https://egeria-project.org/concepts/digital-product/> |

**System action**

The connector is shutting down.  The dependencies are left as they are.

**User action**

No action is required.  This message is for monitoring the shutdown of the product dependency manager.


----

### DARWIN-PRODUCT-DEPENDENCY-MANAGER-0012

> The {0} integration connector has recorded that data flows from {1} to {2} through information supply chain {3} (relationship {4}) because {5}

|  |  |
|---|---|
| **Java constant** | `DarwinAuditCode.LINEAGE_CREATED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |
| **Further reading** | <https://egeria-project.org/features/lineage-management/overview/> |

**System action**

Finer-grained lineage shows that data flows between the two elements, and there was no DataFlow relationship recording this for the information supply chain, so the connector has created one.  This is how lineage bubbles up from the detailed level to the coarse-grained: data mappings between schema elements imply a data flow between their assets, and lineage between the assets owned by software servers' capabilities implies a data flow between the servers.

**User action**

No action is required.  The relationship is removed automatically if the finer-grained lineage stops supporting it.


----

### DARWIN-PRODUCT-DEPENDENCY-MANAGER-0013

> The {0} integration connector has removed the data flow ({1}) from {2} to {3} through information supply chain {4} because the finer-grained lineage no longer supports it

|  |  |
|---|---|
| **Java constant** | `DarwinAuditCode.LINEAGE_REMOVED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |
| **Further reading** | <https://egeria-project.org/features/lineage-management/overview/> |

**System action**

The connector created this DataFlow relationship on an earlier refresh from the finer-grained lineage between the two elements.  That lineage is no longer there - or the data flow is now recorded by a relationship asserted by an external user - so the connector's own relationship is removed.  Relationships asserted by external users are never removed.

**User action**

No action is required.  If the data flow is real but the finer-grained lineage is missing, correct the lineage and the relationship is recreated on the next refresh.


----

### DARWIN-PRODUCT-DEPENDENCY-MANAGER-0014

> The {0} integration connector has set the information supply chain of the data flow ({1}) from {2} to {3}, asserted by {4}, to {5}

|  |  |
|---|---|
| **Java constant** | `DarwinAuditCode.LINEAGE_SUPPLY_CHAIN_SET` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |
| **Further reading** | <https://egeria-project.org/concepts/information-supply-chain/> |

**System action**

A DataFlow relationship asserted by an external user did not name an information supply chain.  The finer-grained lineage between the two elements proves the data flow, so the connector has filled in the information supply chain from the first lineage that does.

**User action**

No action is required.  If the data flow belongs to a different information supply chain, correct the relationship; the connector does not change an information supply chain once it is set.


----

### DARWIN-PRODUCT-DEPENDENCY-MANAGER-0015

> The {0} integration connector derived {1} data flows between data assets from the data mappings between their schema elements, and {2} data flows between software servers from the lineage between the data assets their capabilities own; it created {3} DataFlow relationships, removed {4} and set the information supply chain on {5}

|  |  |
|---|---|
| **Java constant** | `DarwinAuditCode.LINEAGE_REFRESH_COMPLETE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |
| **Further reading** | <https://egeria-project.org/features/lineage-management/overview/> |

**System action**

The coarse-grained lineage is consistent with the finer-grained lineage beneath it.  The digital product dependencies are derived next, from the asset-level lineage that this step has just completed.

**User action**

No action is required.  This message is for monitoring the derivation of the coarse-grained lineage.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
