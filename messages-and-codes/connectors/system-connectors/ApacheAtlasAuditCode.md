<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# ApacheAtlasAuditCode

The ApacheAtlasAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 2 |
| **Message identifiers begin** | `APACHE-ATLAS-REST-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.ffdc.ApacheAtlasAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/system-connectors/apache-atlas-connectors](../../../open-metadata-implementation/adapters/open-connectors/system-connectors/apache-atlas-connectors) |
| **Source** | [ApacheAtlasAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/system-connectors/apache-atlas-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/apacheatlas/resource/ffdc/ApacheAtlasAuditCode.java) |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-apache-atlas/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [APACHE-ATLAS-REST-CONNECTOR-0005](#apache-atlas-rest-connector-0005) | EXCEPTION | The {0} Apache Atlas REST Connector encountered an {1} exception when connecting to {2} during the {3} method.  The exception message included was {4} |
| [APACHE-ATLAS-REST-CONNECTOR-0031](#apache-atlas-rest-connector-0031) | EXCEPTION | A client-side exception was received from API call {0} to server {1} at {2}.  The error message was {3} |

----

### APACHE-ATLAS-REST-CONNECTOR-0005

> The {0} Apache Atlas REST Connector encountered an {1} exception when connecting to {2} during the {3} method.  The exception message included was {4}

|  |  |
|---|---|
| **Java constant** | `ApacheAtlasAuditCode.BAD_CONFIGURATION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The exception is passed back to the integration daemon that is hosting this connector to enable it to perform error handling.  More messages are likely to follow describing the error handling that was performed.  These can help to determine how to recover from this error

**User action**

This message contains the exception that was the original cause of the problem. Use the information from the exception stack trace to determine why the connector is not able to access the event broker and resolve that issue.  Use the messages that where subsequently logged during the error handling to discover how to restart the connector in the integration daemon once the original cause of the error has been corrected.


----

### APACHE-ATLAS-REST-CONNECTOR-0031

> A client-side exception was received from API call {0} to server {1} at {2}.  The error message was {3}

|  |  |
|---|---|
| **Java constant** | `ApacheAtlasAuditCode.CLIENT_SIDE_REST_API_ERROR` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The server has issued a call to the open metadata access service REST API in a remote server and has received an exception from the local client libraries.

**User action**

Look for errors in the local server's console to understand and correct the source of the error.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
