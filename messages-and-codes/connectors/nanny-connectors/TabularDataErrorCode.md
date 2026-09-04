<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# TabularDataErrorCode

The TabularDataErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the Reference Data Connectors. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 10 |
| **Message identifiers begin** | `REFERENCE-DATA-CONNECTORS-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.ffdc.TabularDataErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/nanny-connectors](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors) |
| **Source** | [TabularDataErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/jacquard/tabulardatasets/ffdc/TabularDataErrorCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/tabular-data-set-connector/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [REFERENCE-DATA-CONNECTORS-400-001](#reference-data-connectors-400-001) | 400 | Reference Data Connector {0} has been configured without the URL to the OMAG Server Platform |
| [REFERENCE-DATA-CONNECTORS-400-002](#reference-data-connectors-400-002) | 400 | Reference Data Connector {0} has been configured without the name of the OMAG Server to call |
| [REFERENCE-DATA-CONNECTORS-400-004](#reference-data-connectors-400-004) | 400 | Reference Data Connector {0} has no data at record {1}.  The data set size is {2} |
| [REFERENCE-DATA-CONNECTORS-500-001](#reference-data-connectors-500-001) | 500 | The {0} Reference Data Connector received an unexpected exception {1} during method {2}; the error message was: {3} |
| [REFERENCE-DATA-CONNECTORS-500-003](#reference-data-connectors-500-003) | 500 | The {0} Reference Data Connector cannot map column {1} |
| [REFERENCE-DATA-CONNECTORS-500-004](#reference-data-connectors-500-004) | 500 | Product definition is null for connector {0} in method {1} |
| [REFERENCE-DATA-CONNECTORS-400-005](#reference-data-connectors-400-005) | 400 | Connector {0} has been configured without the unique identifier of the digital product family it reads; it is supplied in the {1} configuration property |
| [REFERENCE-DATA-CONNECTORS-400-006](#reference-data-connectors-400-006) | 400 | Connector {0} has been configured to read element {1}, which is a {2} rather than a digital product family |
| [REFERENCE-DATA-CONNECTORS-400-007](#reference-data-connectors-400-007) | 400 | Connector {0} has no tabular data set called {1} in digital product family {2}; the data sets it has are: {3} |
| [REFERENCE-DATA-CONNECTORS-400-008](#reference-data-connectors-400-008) | 400 | Connector {0} has been asked to read digital product family {1} without being told which of its tabular data sets to read |

----

### REFERENCE-DATA-CONNECTORS-400-001

> Reference Data Connector {0} has been configured without the URL to the OMAG Server Platform

|  |  |
|---|---|
| **Java constant** | `TabularDataErrorCode.NULL_URL` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The connector cannot contact the OMAG Infrastructure.

**User action**

The Platform URL Root is configured in the connector's connection endpoint in the address property.  Typically it is the host name and port where the OMAG Server Platform is running.


----

### REFERENCE-DATA-CONNECTORS-400-002

> Reference Data Connector {0} has been configured without the name of the OMAG Server to call

|  |  |
|---|---|
| **Java constant** | `TabularDataErrorCode.NULL_SERVER_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The connector cannot contact the OMAG Server.

**User action**

The server's name is configured in the connector's connection additionalProperties in the serverName property.


----

### REFERENCE-DATA-CONNECTORS-400-004

> Reference Data Connector {0} has no data at record {1}.  The data set size is {2}

|  |  |
|---|---|
| **Java constant** | `TabularDataErrorCode.NULL_RECORD` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector is not able to return data at the requested record number.

**User action**

Request data from the connector with record numbers from '0' to the 'data set size minus 1'.


----

### REFERENCE-DATA-CONNECTORS-500-001

> The {0} Reference Data Connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `TabularDataErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot continue with the request.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### REFERENCE-DATA-CONNECTORS-500-003

> The {0} Reference Data Connector cannot map column {1}

|  |  |
|---|---|
| **Java constant** | `TabularDataErrorCode.UNMAPPED_COLUMN` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector cannot continue to work with this element.

**User action**

Use the details from the element and any other related error messages to determine the cause of the error and retry the request once this is resolved.


----

### REFERENCE-DATA-CONNECTORS-500-004

> Product definition is null for connector {0} in method {1}

|  |  |
|---|---|
| **Java constant** | `TabularDataErrorCode.NULL_PRODUCT_DEFINITION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector cannot continue to work without the product definition.

**User action**

This is a timing issue.  The connector should set up a valid product definition either in its constructor or start() method.  The calls to retrieve the table name/description should occur after start().


----

### REFERENCE-DATA-CONNECTORS-400-005

> Connector {0} has been configured without the unique identifier of the digital product family it reads; it is supplied in the {1} configuration property

|  |  |
|---|---|
| **Java constant** | `TabularDataErrorCode.NULL_STARTING_ELEMENT` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector cannot find the family whose products it is to present, so it cannot start.

**User action**

Add the unique identifier of the digital product family to the connection's configuration properties.  Jacquard sets this property when it builds the family's asset.


----

### REFERENCE-DATA-CONNECTORS-400-006

> Connector {0} has been configured to read element {1}, which is a {2} rather than a digital product family

|  |  |
|---|---|
| **Java constant** | `TabularDataErrorCode.NOT_A_PRODUCT_FAMILY` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector only knows how to walk the members of a digital product family, so it cannot start.

**User action**

Correct the unique identifier in the connection's configuration properties so that it names a digital product family.


----

### REFERENCE-DATA-CONNECTORS-400-007

> Connector {0} has no tabular data set called {1} in digital product family {2}; the data sets it has are: {3}

|  |  |
|---|---|
| **Java constant** | `TabularDataErrorCode.UNKNOWN_TABLE_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot bring the requested data set into focus.

**User action**

Ask the connector for its table names and use one of those.  If the product was added to the family after the connector started, refresh the connector's cache.


----

### REFERENCE-DATA-CONNECTORS-400-008

> Connector {0} has been asked to read digital product family {1} without being told which of its tabular data sets to read

|  |  |
|---|---|
| **Java constant** | `TabularDataErrorCode.NO_TABLE_IN_FOCUS` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

A collection of data sets is read one data set at a time, and none has been chosen, so there is nothing to read.

**User action**

Call setTableName() with one of the names returned by getTableNames() before describing or reading records.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
