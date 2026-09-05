<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OIFAuditCode

The OIFAuditCode is used to define the message content for the Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 16 |
| **Message identifiers begin** | `OIF-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.frameworks.integration.ffdc.OIFAuditCode` |
| **Module** | [open-metadata-implementation/frameworks/open-integration-framework](../../open-metadata-implementation/frameworks/open-integration-framework) |
| **Source** | [OIFAuditCode.java](../../open-metadata-implementation/frameworks/open-integration-framework/src/main/java/org/odpi/openmetadata/frameworks/integration/ffdc/OIFAuditCode.java) |
| **Further reading** | <https://egeria-project.org/frameworks/oif/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OIF-CONNECTOR-0001](#oif-connector-0001) | STARTUP | The integration connector context manager is being initialized for calls to server {0} on platform {1} |
| [OIF-CONNECTOR-0005](#oif-connector-0005) | ERROR | A {0} exception with message {1} occurred when parsing open lineage event: {2} |
| [OIF-CONNECTOR-0006](#oif-connector-0006) | EXCEPTION | A {0} exception with message {1} occurred when a listening integration connector tried to push an OpenLineage event |
| [OIF-CONNECTOR-0007](#oif-connector-0007) | INFO | No catalog targets are defined for the {0} integration connector |
| [OIF-CONNECTOR-0008](#oif-connector-0008) | INFO | The {0} integration connector is refreshing action target {1} |
| [OIF-CONNECTOR-0009](#oif-connector-0009) | INFO | The {0} integration connector has refreshed {1} action target(s) |
| [OIF-CONNECTOR-0010](#oif-connector-0010) | ACTION | The {0} connector has detected an unsynchronized {1} element ({2}) in metadata collection {3} ({4}) but the permitted synchronization to catalog target {5} is {6} |
| [OIF-CONNECTOR-0011](#oif-connector-0011) | ACTION | The {0} connector is unsure which action to take for an element. This is a logic error.  The member element information is {1} |
| [OIF-CONNECTOR-0012](#oif-connector-0012) | EXCEPTION | The {0} integration connector received an unexpected exception {1} while trying to disconnect connector {2}; the error message was: {3} |
| [OIF-CONNECTOR-0013](#oif-connector-0013) | EXCEPTION | The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3} |
| [OIF-CONNECTOR-0014](#oif-connector-0014) | INFO | The {0} integration connector has stopped its monitoring and is shutting down |
| [OIF-CONNECTOR-0015](#oif-connector-0015) | ERROR | An unexpected {0} exception was returned to the {1} integration connector while retrieving the catalog targets.  The error message was {2} |
| [OIF-CONNECTOR-0016](#oif-connector-0016) | EXCEPTION | The {0} integration connector encountered an {1} exception when registering a listener to the open metadata ecosystem.  The exception message included was {2} |
| [OIF-CONNECTOR-0017](#oif-connector-0017) | ERROR | The {0} connector has detected a missing or invalid {1} property in method {2} - element is: {3} |
| [OIF-CONNECTOR-0018](#oif-connector-0018) | ERROR | The {0} connector has detected that element {1} which should be of type {2} has bean properties of {3} rather than {4} in method {5} - element is {6} |
| [OIF-CONNECTOR-0019](#oif-connector-0019) | ACTION | The {0} connector is recommending the {1} action to take for element {2} |

----

### OIF-CONNECTOR-0001

> The integration connector context manager is being initialized for calls to server {0} on platform {1}

|  |  |
|---|---|
| **Java constant** | `OIFAuditCode.CONTEXT_INITIALIZING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The integration daemon is initializing its context manager.

**User action**

Verify that the start up sequence goes on to initialize the context for each connector configured for this service.


----

### OIF-CONNECTOR-0005

> A {0} exception with message {1} occurred when parsing open lineage event: {2}

|  |  |
|---|---|
| **Java constant** | `OIFAuditCode.OPEN_LINEAGE_FORMAT_ERROR` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The integration daemon cannot parse an incoming open lineage event into Egeria's OpenLineageRunEvent bean.  This may be due to either (1) an invalid open lineage event, or (2) Egeria's OpenLineageRunEvent not supporting an advancement in the open lineage standard.  The raw event is passed to the listening connectors with a null OpenLineageRunEvent bean.  The connector can use the open lineage standard server to process the event facet by facet.

**User action**

Verify the format of the open lineage event.  If incorrect, seek the source of the event.  If correct, look to enhance Egeria's OpenLineageRunEvent.


----

### OIF-CONNECTOR-0006

> A {0} exception with message {1} occurred when a listening integration connector tried to push an OpenLineage event

|  |  |
|---|---|
| **Java constant** | `OIFAuditCode.OPEN_LINEAGE_PUBLISH_ERROR` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The integration daemon has caught the exception and will continue to pass the event to the remaining listening integration connectors.

**User action**

Look at the resulting stack trace to understand what went wrong in the called integration connector.


----

### OIF-CONNECTOR-0007

> No catalog targets are defined for the {0} integration connector

|  |  |
|---|---|
| **Java constant** | `OIFAuditCode.NO_CATALOG_TARGETS` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |

**System action**

The integration connector waits for the next refresh.

**User action**

Add one or more action targets to the integration connector to provide it with work to do.


----

### OIF-CONNECTOR-0008

> The {0} integration connector is refreshing action target {1}

|  |  |
|---|---|
| **Java constant** | `OIFAuditCode.REFRESHING_CATALOG_TARGET` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The integration connector performs the requested metadata synchronization.

**User action**

Check for reported errors.   Otherwise, the connector is working as configured.


----

### OIF-CONNECTOR-0009

> The {0} integration connector has refreshed {1} action target(s)

|  |  |
|---|---|
| **Java constant** | `OIFAuditCode.REFRESHED_CATALOG_TARGETS` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The integration connector has completed refresh processing of the catalog targets.

**User action**

Check that the correct action targets have been processes, and adjust them if necessary before the next refresh.


----

### OIF-CONNECTOR-0010

> The {0} connector has detected an unsynchronized {1} element ({2}) in metadata collection {3} ({4}) but the permitted synchronization to catalog target {5} is {6}

|  |  |
|---|---|
| **Java constant** | `OIFAuditCode.IGNORED_EGERIA_ELEMENT` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}` |

**System action**

The element is ignored.

**User action**

Determine why this element is in the metadata collection and determine if it should be synchronized with the catalog target.  If it should, then set up the permitted synchronization direction to allow it.


----

### OIF-CONNECTOR-0011

> The {0} connector is unsure which action to take for an element. This is a logic error.  The member element information is {1}

|  |  |
|---|---|
| **Java constant** | `OIFAuditCode.UNKNOWN_ACTION` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector stops processing.

**User action**

Using information from the element, the set up of the connector, and the connector's logic to determine why this 'should not occur' case has happened.


----

### OIF-CONNECTOR-0012

> The {0} integration connector received an unexpected exception {1} while trying to disconnect connector {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `OIFAuditCode.DISCONNECT_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector is cannot disconnect a connector to a catalog target.  Although it continues to run, it may have leaked a resource in the remote target.

**User action**

Use the details from the error message to determine the cause of the error.  Check the remote target for errors and correct as needed.


----

### OIF-CONNECTOR-0013

> The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `OIFAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector records the error anf tries to continue; subsequent errors may occur as a result of this initial failure

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### OIF-CONNECTOR-0014

> The {0} integration connector has stopped its monitoring and is shutting down

|  |  |
|---|---|
| **Java constant** | `OIFAuditCode.CONNECTOR_STOPPING` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |

**System action**

The connector is disconnecting.

**User action**

No action is required unless there are errors that follow indicating that there were problems shutting down.


----

### OIF-CONNECTOR-0015

> An unexpected {0} exception was returned to the {1} integration connector while retrieving the catalog targets.  The error message was {2}

|  |  |
|---|---|
| **Java constant** | `OIFAuditCode.GET_CATALOG_TARGET_EXCEPTION` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The exception is logged and the integration connector waits for the next refresh.

**User action**

Use the message in the unexpected exception to determine the root cause of the error. Once this is resolved, follow the instructions to prepare the integration connector for the next refresh.


----

### OIF-CONNECTOR-0016

> The {0} integration connector encountered an {1} exception when registering a listener to the open metadata ecosystem.  The exception message included was {2}

|  |  |
|---|---|
| **Java constant** | `OIFAuditCode.UNABLE_TO_REGISTER_LISTENER` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector continues to scan and synchronize metadata as configured.  Without the listener, updates to open metadata elements with only be synchronized to the third party during a refresh scan.

**User action**

The likely cause of this error is that the OMF in the metadata access server used by the integration daemon is not configured to support topics.  This can be changed by reconfiguring the metadata access server to support topics.  A less likely cause is that the metadata access server has stopped running


----

### OIF-CONNECTOR-0017

> The {0} connector has detected a missing or invalid {1} property in method {2} - element is: {3}

|  |  |
|---|---|
| **Java constant** | `OIFAuditCode.BAD_OM_VALUE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector throws an exception to indicate that it should not continue.

**User action**

Check the templates that where used to create the element to be sure they include the correct value.  If the template is correct then check the element's history to discover which processing cleared the value, since this is where the problem was introduced.  Now decide if the test in the connector is incorrect, or the .


----

### OIF-CONNECTOR-0018

> The {0} connector has detected that element {1} which should be of type {2} has bean properties of {3} rather than {4} in method {5} - element is {6}

|  |  |
|---|---|
| **Java constant** | `OIFAuditCode.BAD_OM_PROPERTY_TYPE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}` |

**System action**

The connector throws an exception to indicate that it should not continue since something has gone very wrong with the connector or Open Metadata Framework since the connector is expecting an element of a certain type, with bean properties that match that type, but the OMF has returned something different.

**User action**

Check that the type of element is that which was expected.  If not, this may be a connector logic problem, or another process may have created additional metadata that has confused the connector. Compare the element retrieved through the OMF with its stored value.  If the stored value is correct, then the problem is in the OMF converters.  If the stored value is not correct then look at the element's history to understand which processing caused the problem.


----

### OIF-CONNECTOR-0019

> The {0} connector is recommending the {1} action to take for element {2}

|  |  |
|---|---|
| **Java constant** | `OIFAuditCode.MEMBER_ACTION` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector logs the action it has selected for the element and carries on processing.

**User action**

No action is required.  This message traces the decision that the connector made about each element it processed.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
