<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# ApacheAtlasErrorCode

The ApacheAtlasErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the Apache Atlas REST connector. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 6 |
| **Message identifiers begin** | `APACHE-ATLAS-REST-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.ffdc.ApacheAtlasErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/system-connectors/apache-atlas-connectors](../../../open-metadata-implementation/adapters/open-connectors/system-connectors/apache-atlas-connectors) |
| **Source** | [ApacheAtlasErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/system-connectors/apache-atlas-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/apacheatlas/resource/ffdc/ApacheAtlasErrorCode.java) |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-apache-atlas/overview/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [APACHE-ATLAS-REST-CONNECTOR-400-001](#apache-atlas-rest-connector-400-001) | 400 | Apache Atlas REST connector {0} has been configured without the URL to Apache Atlas |
| [APACHE-ATLAS-REST-CONNECTOR-400-002](#apache-atlas-rest-connector-400-002) | 400 | Apache Atlas REST connector {0} has been configured with either a null userId or password for connecting to Apache Atlas |
| [APACHE-ATLAS-REST-CONNECTOR-400-004](#apache-atlas-rest-connector-400-004) | 400 | Glossary category {0} already exists in Apache Atlas |
| [APACHE-ATLAS-REST-CONNECTOR-400-005](#apache-atlas-rest-connector-400-005) | 400 | Glossary term {0} already exists in Apache Atlas |
| [APACHE-ATLAS-REST-CONNECTOR-500-001](#apache-atlas-rest-connector-500-001) | 500 | The {0} Apache Atlas REST connector received an unexpected exception {1} during method {2}; the error message was: {3} |
| [APACHE-ATLAS-REST-CONNECTOR-503-001](#apache-atlas-rest-connector-503-001) | 503 | A client-side exception of {0} was received from API call {1} to URL {2}.  The error message was {3} |

----

### APACHE-ATLAS-REST-CONNECTOR-400-001

> Apache Atlas REST connector {0} has been configured without the URL to Apache Atlas

|  |  |
|---|---|
| **Java constant** | `ApacheAtlasErrorCode.NULL_URL` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The connector is move to FAILED status and will not be called by the integration daemon until the configuration error has been corrected.

**User action**

The Apache Atlas URL is configured in the Apache Atlas REST connector's connection endpoint in the address property.  Typically it is the host name and port where Apache Atlas is listening.  The connection is either found in the Integration Daemon's configuration, or, if the Integration Daemon is configured with integration groups, in the open metadata definition of the appropriate integration group.


----

### APACHE-ATLAS-REST-CONNECTOR-400-002

> Apache Atlas REST connector {0} has been configured with either a null userId or password for connecting to Apache Atlas

|  |  |
|---|---|
| **Java constant** | `ApacheAtlasErrorCode.NULL_USER` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The connector is not able to call Apache Atlas without error.

**User action**

Update the connection information for the connector.  This may have been supplied through the Integration Daemon's configuration, or if the Integration Daemon is using integration groups, the connection information is stored in the open metadata ecosystem.  It is possible to supply the userId and password directly in the connection object or via an embedded SecretsConnector.


----

### APACHE-ATLAS-REST-CONNECTOR-400-004

> Glossary category {0} already exists in Apache Atlas

|  |  |
|---|---|
| **Java constant** | `ApacheAtlasErrorCode.CATEGORY_ALREADY_EXISTS` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The connector attempts to add a numerical post-fix to the category name to ensure it has a unique name.

**User action**

No action is required. The connector will validate whether it has already created the category on another thread, or it will try the request with a new name.


----

### APACHE-ATLAS-REST-CONNECTOR-400-005

> Glossary term {0} already exists in Apache Atlas

|  |  |
|---|---|
| **Java constant** | `ApacheAtlasErrorCode.TERM_ALREADY_EXISTS` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The connector attempts to add a numerical post-fix to the term name to ensure it has a unique name.

**User action**

No action is required. The connector will validate whether it has already created the term on another thread, or it will try the request with a new name.


----

### APACHE-ATLAS-REST-CONNECTOR-500-001

> The {0} Apache Atlas REST connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `ApacheAtlasErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot catalog one or more metadata elements.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### APACHE-ATLAS-REST-CONNECTOR-503-001

> A client-side exception of {0} was received from API call {1} to URL {2}.  The error message was {3}

|  |  |
|---|---|
| **Java constant** | `ApacheAtlasErrorCode.CLIENT_SIDE_REST_API_ERROR` |
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
