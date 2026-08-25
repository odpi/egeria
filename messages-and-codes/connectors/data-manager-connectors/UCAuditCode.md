<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# UCAuditCode

The UCAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 18 |
| **Message identifiers begin** | `UNITY-CATALOG-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/data-manager-connectors/unity-catalog-connectors](../../../open-metadata-implementation/adapters/open-connectors/data-manager-connectors/unity-catalog-connectors) |
| **Source** | [UCAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/data-manager-connectors/unity-catalog-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/unitycatalog/ffdc/UCAuditCode.java) |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-unity-catalog/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [UNITY-CATALOG-CONNECTOR-0001](#unity-catalog-connector-0001) | EXCEPTION | The Unity Catalog connector {0} received an unexpected {1} exception during method {2}; the error message was: {3} |
| [UNITY-CATALOG-CONNECTOR-0005](#unity-catalog-connector-0005) | EXCEPTION | A client-side exception was received from API call {0} to server {1} at {2}.  The error message was {3} |
| [UNITY-CATALOG-CONNECTOR-0007](#unity-catalog-connector-0007) | INFO | The {0} Unity Catalog Connector has been supplied with a friendship connector with GUID {1} |
| [UNITY-CATALOG-CONNECTOR-0008](#unity-catalog-connector-0008) | INFO | The {0} OSS Unity Inside Catalog Synchronizer Connector only works with catalog targets |
| [UNITY-CATALOG-CONNECTOR-0009](#unity-catalog-connector-0009) | INFO | The {0} Connector has added a catalog target relationship {1} from friendship connector {2} to Unity Catalog Server Asset {3} for Unity Catalog (UC) {4} |
| [UNITY-CATALOG-CONNECTOR-0010](#unity-catalog-connector-0010) | ACTION | The {0} Connector has detected a change in the identity of element {1}.  The original id was from {2} and now it is {3} in Unity Catalog (UC) {4} |
| [UNITY-CATALOG-CONNECTOR-0011](#unity-catalog-connector-0011) | ACTION | The {0} Connector has detected a change in the open metadata that controls the definition of catalog {1} but cannot update the catalog in Unity Catalog (UC) at {2} |
| [UNITY-CATALOG-CONNECTOR-0012](#unity-catalog-connector-0012) | ACTION | The {0} Connector has detected a change in the open metadata that controls the definition of schema {1} but cannot update the schema in Unity Catalog (UC) at {2} |
| [UNITY-CATALOG-CONNECTOR-0013](#unity-catalog-connector-0013) | ACTION | The {0} Connector has detected a change in the open metadata that controls the definition of table {1} but cannot update the table in Unity Catalog (UC) at {2} |
| [UNITY-CATALOG-CONNECTOR-0014](#unity-catalog-connector-0014) | ACTION | The {0} Connector has detected a change in the open metadata element {1} that controls the definition of volume {2} but cannot update the volume in Unity Catalog (UC) at {3} |
| [UNITY-CATALOG-CONNECTOR-0015](#unity-catalog-connector-0015) | ACTION | The {0} Connector has detected a change in the open metadata element {1} that controls the definition of function {2} but cannot update the function in Unity Catalog (UC) at {3} |
| [UNITY-CATALOG-CONNECTOR-0016](#unity-catalog-connector-0016) | INFO | The {0} Connector has detected that the open metadata element that controls the definition of element {1} in Unity Catalog (UC) at {2} has been deleted |
| [UNITY-CATALOG-CONNECTOR-0019](#unity-catalog-connector-0019) | INFO | The {0} governance action service has created a new {1} element called {2} ({3}) |
| [UNITY-CATALOG-CONNECTOR-0020](#unity-catalog-connector-0020) | ERROR | The {0} governance action service has no technology type to work with |
| [UNITY-CATALOG-CONNECTOR-0021](#unity-catalog-connector-0021) | ERROR | The {0} governance action service has been passed a technology type of {1} which is not supported |
| [UNITY-CATALOG-CONNECTOR-0022](#unity-catalog-connector-0022) | ERROR | The {0} governance action service has not been passed all the placeholder variables needed to create an element for technology type {1}; the missing placeholder variables are: {2} |
| [UNITY-CATALOG-CONNECTOR-0023](#unity-catalog-connector-0023) | ERROR | The {0} connector has detected a missing or invalid {1} property in method {2} - element is: {3} |
| [UNITY-CATALOG-CONNECTOR-0024](#unity-catalog-connector-0024) | ERROR | The {0} connector has detected that element {1} which should be of type {2} has bean properties of {3} rather than {4} in method {5} - element is {6} |

----

### UNITY-CATALOG-CONNECTOR-0001

> The Unity Catalog connector {0} received an unexpected {1} exception during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `UCAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot process the current request.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### UNITY-CATALOG-CONNECTOR-0005

> A client-side exception was received from API call {0} to server {1} at {2}.  The error message was {3}

|  |  |
|---|---|
| **Java constant** | `UCAuditCode.CLIENT_SIDE_REST_API_ERROR` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The server has issued a call to the open metadata access service REST API in a remote server and has received an exception from the local client libraries.

**User action**

Look for errors in the local server's console to understand and correct the source of the error.


----

### UNITY-CATALOG-CONNECTOR-0007

> The {0} Unity Catalog Connector has been supplied with a friendship connector with GUID {1}

|  |  |
|---|---|
| **Java constant** | `UCAuditCode.FRIENDSHIP_GUID` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The friendship connector is an integration connector that is able to synchronize the contents inside a Unity Catalog (UC) connector.  Therefore, they will cooperate to synchronize the contents of the Unity Catalog with the open metadata ecosystem.

**User action**

No action is required, this message is just to acknowledge that that the two integration connectors are going to collaborate to catalog the entire contents of Unity Catalog.


----

### UNITY-CATALOG-CONNECTOR-0008

> The {0} OSS Unity Inside Catalog Synchronizer Connector only works with catalog targets

|  |  |
|---|---|
| **Java constant** | `UCAuditCode.IGNORING_ENDPOINT` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |

**System action**

The connector is ignoring the Unity Catalog (UC) server instance that are configured directly through its connection.

**User action**

Update the integration connector's configuration to use catalog targets.


----

### UNITY-CATALOG-CONNECTOR-0009

> The {0} Connector has added a catalog target relationship {1} from friendship connector {2} to Unity Catalog Server Asset {3} for Unity Catalog (UC) {4}

|  |  |
|---|---|
| **Java constant** | `UCAuditCode.NEW_CATALOG_TARGET` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The connector has requested that its friendship connector starts to catalog a new Unity Catalog (UC) catalog.

**User action**

Verify that the cataloguing starts the next time that the friendship connector refreshes.


----

### UNITY-CATALOG-CONNECTOR-0010

> The {0} Connector has detected a change in the identity of element {1}.  The original id was from {2} and now it is {3} in Unity Catalog (UC) {4}

|  |  |
|---|---|
| **Java constant** | `UCAuditCode.IDENTITY_MISMATCH` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The connector will continue to synchronize metadata with Unity Catalog (UC).  This element will not be updated in with system and will remain out of sync.

**User action**

Verify that the element is being correctly synchronized.  It is possible that there are two different elements with the same name. Also investigate why this element changed in Unity Catalog when it is owned by the open metadata ecosystem.  It may be that the structure of the element was changed and the UC element was replaced to reflect the new data structure.  If this is a planned change then all is ok, if it is unexpected then take steps to repair the data source and prevent it happening again.


----

### UNITY-CATALOG-CONNECTOR-0011

> The {0} Connector has detected a change in the open metadata that controls the definition of catalog {1} but cannot update the catalog in Unity Catalog (UC) at {2}

|  |  |
|---|---|
| **Java constant** | `UCAuditCode.CATALOG_UPDATE` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector will continue to synchronize metadata with Unity Catalog (UC).  However, this catalog's properties will remain unchanged and out of sync with the open metadata ecosystem because Unity Catalog uses the PATCH request which is not supported by Java.

**User action**

Validate that the change in the open metadata ecosystem is intended.  If it is, manually update the table in Unity Catalog using the CLI.  There may also need to be some data migration in the underlying data source.


----

### UNITY-CATALOG-CONNECTOR-0012

> The {0} Connector has detected a change in the open metadata that controls the definition of schema {1} but cannot update the schema in Unity Catalog (UC) at {2}

|  |  |
|---|---|
| **Java constant** | `UCAuditCode.SCHEMA_UPDATE` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector will continue to synchronize metadata with Unity Catalog (UC).  However, this schema's properties will remain unchanged and out of sync with the open metadata ecosystem because Unity Catalog uses the PATCH request which is not supported by Java.

**User action**

Validate that the change in the open metadata ecosystem is intended.  If it is, manually update the table in Unity Catalog using the CLI.  There may also need to be some data migration in the underlying data source.


----

### UNITY-CATALOG-CONNECTOR-0013

> The {0} Connector has detected a change in the open metadata that controls the definition of table {1} but cannot update the table in Unity Catalog (UC) at {2}

|  |  |
|---|---|
| **Java constant** | `UCAuditCode.TABLE_UPDATE` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector will continue to synchronize metadata with Unity Catalog (UC) catalog.  However, this table's properties will remain unchanged and out of sync with the open metadata ecosystem because Unity Catalog does not support an update request.

**User action**

Validate that the change in the open metadata ecosystem is intended.  If it is, manually update the table in Unity Catalog using the CLI.  There may also need to be some data migration in the underlying data source.


----

### UNITY-CATALOG-CONNECTOR-0014

> The {0} Connector has detected a change in the open metadata element {1} that controls the definition of volume {2} but cannot update the volume in Unity Catalog (UC) at {3}

|  |  |
|---|---|
| **Java constant** | `UCAuditCode.VOLUME_UPDATE` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector will continue to synchronize metadata with Unity Catalog (UC) catalog.  However, this volume's properties will remain unchanged and out of sync with the open metadata ecosystem because Unity Catalog does not support a full update request.

**User action**

Validate that the change in the open metadata ecosystem is intended.  If it is, manually update the volume in Unity Catalog using the CLI.  There may also need to be some data migration in the underlying data source.


----

### UNITY-CATALOG-CONNECTOR-0015

> The {0} Connector has detected a change in the open metadata element {1} that controls the definition of function {2} but cannot update the function in Unity Catalog (UC) at {3}

|  |  |
|---|---|
| **Java constant** | `UCAuditCode.FUNCTION_UPDATE` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector will continue to synchronize metadata with Unity Catalog (UC) catalog.  However, this function's properties will remain unchanged and out of sync with the open metadata ecosystem because Unity Catalog does not support a full update request.

**User action**

Validate that the change in the open metadata ecosystem is intended.  If it is, manually update the volume in Unity Catalog using the CLI.  There may also need to be some data migration in the underlying called data sources.


----

### UNITY-CATALOG-CONNECTOR-0016

> The {0} Connector has detected that the open metadata element that controls the definition of element {1} in Unity Catalog (UC) at {2} has been deleted

|  |  |
|---|---|
| **Java constant** | `UCAuditCode.UC_ELEMENT_DELETE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector will delete the element in Unity Catalog (UC) catalog.

**User action**

Validate that the change in the open metadata ecosystem is intended.  If it is, no additional action is required.  If the element is still required, investigate what happened to the element in the open metadata ecosystem.   It may have moved zones or metadata collections or its security controls changed, making it invisible to the connector.  Or it may have been soft-deleted which means it can be restored.


----

### UNITY-CATALOG-CONNECTOR-0019

> The {0} governance action service has created a new {1} element called {2} ({3})

|  |  |
|---|---|
| **Java constant** | `UCAuditCode.NEW_ELEMENT_CREATED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The governance action service returns an ACTIONED completion status.

**User action**

Ensure follow-on uses of the asset are successful.


----

### UNITY-CATALOG-CONNECTOR-0020

> The {0} governance action service has no technology type to work with

|  |  |
|---|---|
| **Java constant** | `UCAuditCode.NO_TECHNOLOGY_TYPE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}` |

**System action**

The governance action service returns an INVALID completion status.

**User action**

This is an error in the way that the governance action service has been called.Identify the way it was called which could be a direct invocation through the initiateGovernanceAction() method,or as part of a governance action process.  Then correct this approach so that a technology type is set up in the request parameters.


----

### UNITY-CATALOG-CONNECTOR-0021

> The {0} governance action service has been passed a technology type of {1} which is not supported

|  |  |
|---|---|
| **Java constant** | `UCAuditCode.INVALID_TECHNOLOGY_TYPE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The governance action service ends with an INVALID completion status.

**User action**

This is an error in the way that the governance action service has been called.Identify the way it was called which could be a direct invocation through the initiateGovernanceAction() method,or as part of a governance action process.  Then correct this approach so that a supported technology type is set up in the request parameters.


----

### UNITY-CATALOG-CONNECTOR-0022

> The {0} governance action service has not been passed all the placeholder variables needed to create an element for technology type {1}; the missing placeholder variables are: {2}

|  |  |
|---|---|
| **Java constant** | `UCAuditCode.MISSING_PLACEHOLDER_VALUES` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The governance action service ends with an INVALID completion status and this message.

**User action**

This is an error in the way that the governance action service has been called.Identify the way it was called which could be a direct invocation through the initiateGovernanceAction() method,or as part of a governance action process.  Then correct this approach so that the required placeholder properties are set up in the request parameters.


----

### UNITY-CATALOG-CONNECTOR-0023

> The {0} connector has detected a missing or invalid {1} property in method {2} - element is: {3}

|  |  |
|---|---|
| **Java constant** | `UCAuditCode.BAD_OM_VALUE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector throws an exception to indicate that it should not continue.

**User action**

Check the templates that where used to create the element to be sure they include the correct value.  If the template is correct then check the element's history to discover which processing cleared the value, since this is where the problem was introduced.  Now decide if the test in the connector is incorrect, or the .


----

### UNITY-CATALOG-CONNECTOR-0024

> The {0} connector has detected that element {1} which should be of type {2} has bean properties of {3} rather than {4} in method {5} - element is {6}

|  |  |
|---|---|
| **Java constant** | `UCAuditCode.BAD_OM_PROPERTY_TYPE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}` |

**System action**

The connector throws an exception to indicate that it should not continue since something has gone very wrong with the connector or Open Metadata Framework since the connector is expecting an element of a certain type, with bean properties that match that type, but the OMF has returned something different.

**User action**

Check that the type of element is that which was expected.  If not, this may be a connector logic problem, or another process may have created additional metadata that has confused the connector. Compare the element retrieved through the OMF with its stored value.  If the stored value is correct, then the problem is in the OMF converters.  If the stored value is not correct then look at the element's history to understand which processing caused the problem.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
