<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# UCErrorCode

The UCErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the Kafka monitor integration connector. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 7 |
| **Message identifiers begin** | `UNITY-CATALOG-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/data-manager-connectors/unity-catalog-connectors](../../../open-metadata-implementation/adapters/open-connectors/data-manager-connectors/unity-catalog-connectors) |
| **Source** | [UCErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/data-manager-connectors/unity-catalog-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/unitycatalog/ffdc/UCErrorCode.java) |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-unity-catalog/overview/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [UNITY-CATALOG-CONNECTOR-400-001](#unity-catalog-connector-400-001) | 400 | Connection {0} has been configured without the URL to the Unity Catalog (UC) |
| [UNITY-CATALOG-CONNECTOR-400-002](#unity-catalog-connector-400-002) | 400 | The {0} Unity Catalog Connector has not been supplied with a {1} property value |
| [UNITY-CATALOG-CONNECTOR-500-001](#unity-catalog-connector-500-001) | 500 | The {0} Unity Catalog (UC) connector received an unexpected exception {1} during method {2}; the error message was: {3} |
| [UNITY-CATALOG-CONNECTOR-500-002](#unity-catalog-connector-500-002) | 500 | The {0} Unity Catalog (UC) governance service has detected an invalid technology type in code that runs after the technology type has been validated |
| [UNITY-CATALOG-CONNECTOR-500-003](#unity-catalog-connector-500-003) | 500 | The {0} connector has detected a missing or invalid {1} property in method {2} - element is: {3} |
| [UNITY-CATALOG-CONNECTOR-500-004](#unity-catalog-connector-500-004) | 500 | The {0} connector has detected that element {1} which should be of type {2} has bean properties of {3} rather than {4} in method {5} - element is {6} |
| [UNITY-CATALOG-CONNECTOR-503-001](#unity-catalog-connector-503-001) | 503 | A client-side exception of {0} was received from API call {1} to URL {2}.  The error message was {3} |

----

### UNITY-CATALOG-CONNECTOR-400-001

> Connection {0} has been configured without the URL to the Unity Catalog (UC)

|  |  |
|---|---|
| **Java constant** | `UCErrorCode.NULL_URL` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The connector cannot start because the endpoint of its connection has a null address property.

**User action**

Update the connection's endpoint to include the connection string needed to connect to the desired database.


----

### UNITY-CATALOG-CONNECTOR-400-002

> The {0} Unity Catalog Connector has not been supplied with a {1} property value

|  |  |
|---|---|
| **Java constant** | `UCErrorCode.MISSING_PROPERTY_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector cannot continue because it is not sure which elements to work on.

**User action**

Add this property to either the connector's configuration properties (or if it is a governance service, to the request parameters) and retry the request.


----

### UNITY-CATALOG-CONNECTOR-500-001

> The {0} Unity Catalog (UC) connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `UCErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot process the current request.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### UNITY-CATALOG-CONNECTOR-500-002

> The {0} Unity Catalog (UC) governance service has detected an invalid technology type in code that runs after the technology type has been validated

|  |  |
|---|---|
| **Java constant** | `UCErrorCode.LOGIC_ERROR` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}` |

**System action**

The connector ends with an exception.

**User action**

The code in the service needs to be fixed to ensure the list of valid unity catalog resources is consistent throughout.


----

### UNITY-CATALOG-CONNECTOR-500-003

> The {0} connector has detected a missing or invalid {1} property in method {2} - element is: {3}

|  |  |
|---|---|
| **Java constant** | `UCErrorCode.BAD_OM_VALUE` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector throws an exception to indicate that it should not continue.

**User action**

Check the templates that where used to create the element to be sure they include the correct value.  If the template is correct then check the element's history to discover which processing cleared the value, since this is where the problem was introduced.  Now decide if the test in the connector is incorrect, or the .


----

### UNITY-CATALOG-CONNECTOR-500-004

> The {0} connector has detected that element {1} which should be of type {2} has bean properties of {3} rather than {4} in method {5} - element is {6}

|  |  |
|---|---|
| **Java constant** | `UCErrorCode.BAD_OM_PROPERTY_TYPE` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}` |

**System action**

The connector throws an exception to indicate that it should not continue since something has gone very wrong with the connector or Open Metadata Framework since the connector is expecting an element of a certain type, with bean properties that match that type, but the OMF has returned something different.

**User action**

Check that the type of element is that which was expected.  If not, this may be a connector logic problem, or another process may have created additional metadata that has confused the connector. Compare the element retrieved through the OMF with its stored value.  If the stored value is correct, then the problem is in the OMF converters.  If the stored value is not correct then look at the element's history to understand which processing caused the problem.


----

### UNITY-CATALOG-CONNECTOR-503-001

> A client-side exception of {0} was received from API call {1} to URL {2}.  The error message was {3}

|  |  |
|---|---|
| **Java constant** | `UCErrorCode.CLIENT_SIDE_REST_API_ERROR` |
| **HTTP error code** | 503 - Service Unavailable - the service needed to process the request is not running |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector has issued a call to the open metadata access service REST API in a remote server and has received an exception from the local client libraries.

**User action**

Look for errors in the local server's console to understand and correct the source of the error.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
