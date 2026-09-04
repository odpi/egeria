<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# TabularDataAuditCode

The TabularDataAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 6 |
| **Message identifiers begin** | `TABULAR-METADATA-CONNECTORS-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.ffdc.TabularDataAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/nanny-connectors](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors) |
| **Source** | [TabularDataAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/jacquard/tabulardatasets/ffdc/TabularDataAuditCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/tabular-data-set-connector/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [TABULAR-METADATA-CONNECTORS-0001](#tabular-metadata-connectors-0001) | ERROR | The {0} Tabular Metadata Connector received an unexpected exception {1} during method {2}; the error message was: {3} |
| [TABULAR-METADATA-CONNECTORS-0002](#tabular-metadata-connectors-0002) | INFO | The {0} connector found {1} tabular data set(s) in digital product family {2} ({3}) |
| [TABULAR-METADATA-CONNECTORS-0003](#tabular-metadata-connectors-0003) | INFO | The {0} connector is not presenting asset {1} of product {2} in digital product family {3} because its connector {4} is not a readable tabular data source |
| [TABULAR-METADATA-CONNECTORS-0004](#tabular-metadata-connectors-0004) | ERROR | The {0} connector found that asset {1} of product {2} in digital product family {3} is a copy of asset {4} - both carry qualified name {5}; the copies have been linked as peer duplicates for the duplicate manager and only the first is presented |
| [TABULAR-METADATA-CONNECTORS-0006](#tabular-metadata-connectors-0006) | ERROR | The {0} connector is not presenting asset {1} of product {2} in digital product family {3} because its table name {4} is already used by asset {5} of a different product |
| [TABULAR-METADATA-CONNECTORS-0005](#tabular-metadata-connectors-0005) | EXCEPTION | The {0} connector is not presenting asset {1} of product {2} in digital product family {3} because its connector could not be built: {4} exception with message {5} |

----

### TABULAR-METADATA-CONNECTORS-0001

> The {0} Tabular Metadata Connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `TabularDataAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot connector the the OMAG Infrastructure.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### TABULAR-METADATA-CONNECTORS-0002

> The {0} connector found {1} tabular data set(s) in digital product family {2} ({3})

|  |  |
|---|---|
| **Java constant** | `TabularDataAuditCode.FAMILY_MEMBERS_LOADED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector walked the family's members and found this many products with a readable tabular data set.  These are the tables it presents.

**User action**

No action is required.  If a product in the family is missing from the count, check that it has an asset with a connection to a readable tabular data source.


----

### TABULAR-METADATA-CONNECTORS-0003

> The {0} connector is not presenting asset {1} of product {2} in digital product family {3} because its connector {4} is not a readable tabular data source

|  |  |
|---|---|
| **Java constant** | `TabularDataAuditCode.FAMILY_MEMBER_NOT_TABULAR` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The product is left out of the collection.  The other products in the family are still presented.

**User action**

No action is required unless the product's data should be delivered with the family, in which case give its asset a connection to a connector that implements ReadableTabularDataSource.


----

### TABULAR-METADATA-CONNECTORS-0004

> The {0} connector found that asset {1} of product {2} in digital product family {3} is a copy of asset {4} - both carry qualified name {5}; the copies have been linked as peer duplicates for the duplicate manager and only the first is presented

|  |  |
|---|---|
| **Java constant** | `TabularDataAuditCode.FAMILY_MEMBER_DUPLICATE_TABLE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

Two writers created the same product at the same time.  The connector links the two assets with a PeerDuplicateLink in DISCOVERED status, which is what the Mendel Automated Duplicate Manager works from to confirm and consolidate duplicates, and presents the first copy so that the family's data is still delivered.

**User action**

No action is required if the duplicate manager is deployed.  If it is not, a steward should review the peer duplicate link and remove or consolidate the copies.


----

### TABULAR-METADATA-CONNECTORS-0006

> The {0} connector is not presenting asset {1} of product {2} in digital product family {3} because its table name {4} is already used by asset {5} of a different product

|  |  |
|---|---|
| **Java constant** | `TabularDataAuditCode.FAMILY_MEMBER_TABLE_NAME_CLASH` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

Two distinct products in the family give their data sets the same table name, and two tables of the same name cannot both be delivered into one destination.  The product found second is left out.

**User action**

Give the products in the family distinct table names in their data specifications.


----

### TABULAR-METADATA-CONNECTORS-0005

> The {0} connector is not presenting asset {1} of product {2} in digital product family {3} because its connector could not be built: {4} exception with message {5}

|  |  |
|---|---|
| **Java constant** | `TabularDataAuditCode.FAMILY_MEMBER_UNREADABLE` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The product is left out of the collection.  The other products in the family are still presented.

**User action**

Use the details from the error message to correct the product asset's connection, then refresh the connector.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
