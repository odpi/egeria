<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# GovernanceActionConnectorsAuditCode

The GovernanceActionConnectorsAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 43 |
| **Message identifiers begin** | `GOVERNANCE-ACTION-CONNECTORS-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/governance-action-connectors](../../../open-metadata-implementation/adapters/open-connectors/governance-action-connectors) |
| **Source** | [GovernanceActionConnectorsAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/governance-action-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/governanceactions/ffdc/GovernanceActionConnectorsAuditCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/governance-action-service/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [GOVERNANCE-ACTION-CONNECTORS-0001](#governance-action-connectors-0001) | INFO | The {0} governance action service is copying source file {1} to destination file {2} |
| [GOVERNANCE-ACTION-CONNECTORS-0002](#governance-action-connectors-0002) | INFO | The {0} governance action service is moving source file {1} to destination file {2} |
| [GOVERNANCE-ACTION-CONNECTORS-0004](#governance-action-connectors-0004) | INFO | The {0} governance action service has created lineage from source {1} to process {2} to destination {3} |
| [GOVERNANCE-ACTION-CONNECTORS-0005](#governance-action-connectors-0005) | ERROR | The {0} governance action service has been called without a source file name to work with |
| [GOVERNANCE-ACTION-CONNECTORS-0006](#governance-action-connectors-0006) | ERROR | The {0} governance action service cannot provision file {1} to {2} destination folder using {3} file pattern |
| [GOVERNANCE-ACTION-CONNECTORS-0007](#governance-action-connectors-0007) | EXCEPTION | The {0} governance action service encountered an {1} exception when provisioning file {2} to {3} destination folder using the {4} file pattern.  The exception message included was {5} |
| [GOVERNANCE-ACTION-CONNECTORS-0008](#governance-action-connectors-0008) | EXCEPTION | The {0} governance action service encountered an {1} exception when attempting to retrieve the file path name from the attached endpoint.  The exception message included was {5} |
| [GOVERNANCE-ACTION-CONNECTORS-0009](#governance-action-connectors-0009) | INFO | The {0} governance action service is using the qualified name from the Folder asset as the path name: {1} |
| [GOVERNANCE-ACTION-CONNECTORS-0010](#governance-action-connectors-0010) | INFO | The {0} governance action service detected that asset {1} has no linked connection |
| [GOVERNANCE-ACTION-CONNECTORS-0011](#governance-action-connectors-0011) | INFO | The {0} governance action service detected that asset {1} has {2} linked connections for asset {3} and is not sure which one to use since they have inconsistent networkAddress properties in their endpoint |
| [GOVERNANCE-ACTION-CONNECTORS-0012](#governance-action-connectors-0012) | ERROR | The context for {0} governance action service returned a RelatedMetadataElement with a null related element: {1} |
| [GOVERNANCE-ACTION-CONNECTORS-0013](#governance-action-connectors-0013) | INFO | The {0} governance action service detected that asset {1} has no endpoint linked to connection {2} |
| [GOVERNANCE-ACTION-CONNECTORS-0014](#governance-action-connectors-0014) | ERROR | The {0} governance action service detected that asset {1} has a linked connection {2} with {3} linked endpoints which is not valid: {4} |
| [GOVERNANCE-ACTION-CONNECTORS-0015](#governance-action-connectors-0015) | INFO | The {0} governance action service detected that the endpoint {1} linked to connection {2} for asset {3} has no networkAddressProperty |
| [GOVERNANCE-ACTION-CONNECTORS-0016](#governance-action-connectors-0016) | INFO | The {0} governance action service received a {1} exception when it registered its completion status.  The exception message is: {2} |
| [GOVERNANCE-ACTION-CONNECTORS-0017](#governance-action-connectors-0017) | INFO | The {0} governance action service received a {1} exception when it registered a listener with the governance context.  The exception message is: {2} |
| [GOVERNANCE-ACTION-CONNECTORS-0018](#governance-action-connectors-0018) | ERROR | The {0} governance action service has no targets to operate on |
| [GOVERNANCE-ACTION-CONNECTORS-0019](#governance-action-connectors-0019) | INFO | The {0} governance action service has publishZones set to null |
| [GOVERNANCE-ACTION-CONNECTORS-0020](#governance-action-connectors-0020) | INFO | The {0} governance action service is publishing asset {1} to the following zones: {2} |
| [GOVERNANCE-ACTION-CONNECTORS-0021](#governance-action-connectors-0021) | INFO | The {0} governance action service is initiating governance action process {1} with request parameters {2} for action targets {3} |
| [GOVERNANCE-ACTION-CONNECTORS-0022](#governance-action-connectors-0022) | EXCEPTION | The {0} governance action service encountered an {1} exception initiating governance action process {2} with request parameters {3} for action targets {4}.  The exception message included was {5} |
| [GOVERNANCE-ACTION-CONNECTORS-0023](#governance-action-connectors-0023) | ERROR | The {0} governance action service cannot retrieve the template {1} configured in property {2}. |
| [GOVERNANCE-ACTION-CONNECTORS-0024](#governance-action-connectors-0024) | INFO | {0} |
| [GOVERNANCE-ACTION-CONNECTORS-0025](#governance-action-connectors-0025) | INFO | The {0} governance action service is attaching the retention classification to asset {1} with an archive time of {2} and a delete time of {3} |
| [GOVERNANCE-ACTION-CONNECTORS-0026](#governance-action-connectors-0026) | ERROR | The {0} governance action service has not been passed a steward as an action target |
| [GOVERNANCE-ACTION-CONNECTORS-0027](#governance-action-connectors-0027) | ERROR | The {0} governance action service has not been passed a survey report as an action target |
| [GOVERNANCE-ACTION-CONNECTORS-0028](#governance-action-connectors-0028) | INFO | No request for action annotations detected in survey report {0} |
| [GOVERNANCE-ACTION-CONNECTORS-0029](#governance-action-connectors-0029) | INFO | {0} request for action annotations detected in survey report {1} |
| [GOVERNANCE-ACTION-CONNECTORS-0030](#governance-action-connectors-0030) | ERROR | The {0} governance action service has not been passed the unique identifier of the server's template as a request parameter |
| [GOVERNANCE-ACTION-CONNECTORS-0031](#governance-action-connectors-0031) | ERROR | The {0} governance action service has not been passed the unique identifier of an integration connector as an action target |
| [GOVERNANCE-ACTION-CONNECTORS-0032](#governance-action-connectors-0032) | INFO | Integration connector {0} is now cataloging {1} server {2} |
| [GOVERNANCE-ACTION-CONNECTORS-0033](#governance-action-connectors-0033) | ERROR | The {0} governance action service has not been passed a {1} action target |
| [GOVERNANCE-ACTION-CONNECTORS-0034](#governance-action-connectors-0034) | INFO | The {0} governance action service has created a new {1} asset called {2} ({3}) |
| [GOVERNANCE-ACTION-CONNECTORS-0035](#governance-action-connectors-0035) | INFO | The {0} governance action service has deleted the {1} asset called {2} ({3}) |
| [GOVERNANCE-ACTION-CONNECTORS-0036](#governance-action-connectors-0036) | INFO | The {0} governance action service has determined that today is {1} |
| [GOVERNANCE-ACTION-CONNECTORS-0037](#governance-action-connectors-0037) | EXCEPTION | The governance service {0} received an unexpected {1} exception during method {2}; the error message was: {3} |
| [GOVERNANCE-ACTION-CONNECTORS-0038](#governance-action-connectors-0038) | INFO | The governance service {0} has completed successfully |
| [GOVERNANCE-ACTION-CONNECTORS-0039](#governance-action-connectors-0039) | ERROR | The {0} governance action service has not been passed a {1} request parameter |
| [GOVERNANCE-ACTION-CONNECTORS-0040](#governance-action-connectors-0040) | INFO | The {0} governance action service has created a new {1} digital subscription {2} for {3} {4} ({5}) requested by {6} {7} ({8}) |
| [GOVERNANCE-ACTION-CONNECTORS-0041](#governance-action-connectors-0041) | INFO | The {0} governance action service has delivered {1} record(s) of table {2} from {3} to {4} |
| [GOVERNANCE-ACTION-CONNECTORS-0042](#governance-action-connectors-0042) | EXCEPTION | The {0} governance action service was unable to deliver table {1} from {2} to {3}: {4} exception with message {5} |
| [GOVERNANCE-ACTION-CONNECTORS-0043](#governance-action-connectors-0043) | INFO | The {0} governance action service has delivered all {1} table(s) of collection {2} to {3} |
| [GOVERNANCE-ACTION-CONNECTORS-0044](#governance-action-connectors-0044) | ERROR | The {0} governance action service delivered {1} of the {2} table(s) of collection {3} to {4}; the table(s) it could not deliver are: {5} |

----

### GOVERNANCE-ACTION-CONNECTORS-0001

> The {0} governance action service is copying source file {1} to destination file {2}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.COPY_FILE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The provisioning governance action service connector is designed to deploy files on request.  This message confirms that a file has been copied.

**User action**

No specific action is required.  This message is to log that a copy provisioning action has taken place.


----

### GOVERNANCE-ACTION-CONNECTORS-0002

> The {0} governance action service is moving source file {1} to destination file {2}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.MOVE_FILE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The provisioning governance action service connector is designed to deploy files on request.  This message confirms that a file has been moved.

**User action**

No specific action is required.  This message is to log that a move provisioning action has taken place.


----

### GOVERNANCE-ACTION-CONNECTORS-0004

> The {0} governance action service has created lineage from source {1} to process {2} to destination {3}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.CREATED_LINEAGE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The provisioning governance action service connector has created lineage to cover the data movement it has just performed.

**User action**

Validate that the lineage is being created between the correct metadata elements.


----

### GOVERNANCE-ACTION-CONNECTORS-0005

> The {0} governance action service has been called without a source file name to work with

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.NO_SOURCE_FILE_NAME` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}` |

**System action**

The provisioning governance action service connector is designed to manage files on request.  It cannot operate without the name of the source file and so it terminates with a FAILED completion status.

**User action**

The source file is passed to the governance action service through the request parameters or via the TargetForAction relationship.  Correct the information passed to the governance service and rerun the request.


----

### GOVERNANCE-ACTION-CONNECTORS-0006

> The {0} governance action service cannot provision file {1} to {2} destination folder using {3} file pattern

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.FILE_PATTERN_FULL` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

This message is logged and the governance action is marked as failed

**User action**

Since no exception occurred it means that there are currently files already occupying all the possible file names allowed by the file pattern.  Files in the destination folder need to be deleted or this connector needs to be reconfigured with a new destination folder or file pattern.


----

### GOVERNANCE-ACTION-CONNECTORS-0007

> The {0} governance action service encountered an {1} exception when provisioning file {2} to {3} destination folder using the {4} file pattern.  The exception message included was {5}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.FILE_PROVISIONING_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The exception is logged.  More messages may follow if follow on attempts are made to provision the file.  These can help to determine how to recover from this error.

**User action**

This message contains the exception that was the original cause of the problem. Use the information from the exception stack trace to determine why the connector is not able to access the directory and resolve that issue.  Use the messages that were subsequently logged during the error handling to discover how to re-run the file provisioning service once the original cause of the error has been corrected.


----

### GOVERNANCE-ACTION-CONNECTORS-0008

> The {0} governance action service encountered an {1} exception when attempting to retrieve the file path name from the attached endpoint.  The exception message included was {5}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.ENDPOINT_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{5}` |

**System action**

The governance action connector will use the qualified name of the asset as the path name to work with

**User action**

This message contains the exception that was the original cause of the problem. If using the qualified name is not working, use the information from the exception stack trace to determine why the connector is not able to access the endpoint and resolve that issue.  Use the messages that were subsequently logged during the error handling to discover how to re-run the governance action service once the file path name on the attached endpoint has been corrected.


----

### GOVERNANCE-ACTION-CONNECTORS-0009

> The {0} governance action service is using the qualified name from the Folder asset as the path name: {1}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.QUALIFIED_NAME_PATH_NAME` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The governance action connector will use the qualified name of the asset as the path name to work with.

**User action**

Validate that the qualified name is a good choice for the path name.  If it is not, add a connection with an endpoint that has the desired path in its networkAddress property.


----

### GOVERNANCE-ACTION-CONNECTORS-0010

> The {0} governance action service detected that asset {1} has no linked connection

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.NO_LINKED_CONNECTION` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

Since the asset has no connection, the governance action connector will use the qualified name of the asset as the path name to work with.

**User action**

The governance action service will next produce the GOVERNANCE-ACTION-CONNECTORS-0006 message with the qualified name.  Follow the instructions for this message.


----

### GOVERNANCE-ACTION-CONNECTORS-0011

> The {0} governance action service detected that asset {1} has {2} linked connections for asset {3} and is not sure which one to use since they have inconsistent networkAddress properties in their endpoint

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.TOO_MANY_CONNECTIONS` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

Since the governance action service cannot choose an appropriate endpoint, it will use the qualified name of the asset as the path name to work with.

**User action**

The governance action service will next produce the GOVERNANCE-ACTION-CONNECTORS-0006 message with the qualified name embedded in it.  Follow the instructions for this message.


----

### GOVERNANCE-ACTION-CONNECTORS-0012

> The context for {0} governance action service returned a RelatedMetadataElement with a null related element: {1}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.NO_RELATED_ASSET` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The governance action service stops attempting extract the path name from the connection and will use the qualified name of the asset as the path name to work with.

**User action**

The governance action service will write the GOVERNANCE-ACTION-CONNECTORS-0006 message with the qualified name embedded in it.  Follow the instructions for this message.  Also investigate why the related element returned a null related element.  This is a logic error in the context or one of its underlying services.


----

### GOVERNANCE-ACTION-CONNECTORS-0013

> The {0} governance action service detected that asset {1} has no endpoint linked to connection {2}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.NO_LINKED_ENDPOINT` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

Since the asset's connection has no endpoint, the governance action connector will use the qualified name of the asset as the path name to work with.

**User action**

The governance action service will next write out the GOVERNANCE-ACTION-CONNECTORS-0006 message with the qualified name.  Follow the instructions for this message.


----

### GOVERNANCE-ACTION-CONNECTORS-0014

> The {0} governance action service detected that asset {1} has a linked connection {2} with {3} linked endpoints which is not valid: {4}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.TOO_MANY_ENDPOINTS` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The governance action service ignores this connection.

**User action**

The governance action service will search for additional connections.


----

### GOVERNANCE-ACTION-CONNECTORS-0015

> The {0} governance action service detected that the endpoint {1} linked to connection {2} for asset {3} has no networkAddressProperty

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.NO_NETWORK_ADDRESS` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

Since the asset's connection has no networkAddress in its endpoint, the governance action connector will use the qualified name of the asset as the path name to work with.

**User action**

The governance action service will log the GOVERNANCE-ACTION-CONNECTORS-0006 message with the qualified name.  Follow the instructions for this message.


----

### GOVERNANCE-ACTION-CONNECTORS-0016

> The {0} governance action service received a {1} exception when it registered its completion status.  The exception message is: {2}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.UNABLE_TO_SET_COMPLETION_STATUS` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The governance action throws a GovernanceServiceException in the hope that the hosting server is able to clean up.

**User action**

Review the exception messages that are logged about the same time as one of them will point to the root cause of the error.


----

### GOVERNANCE-ACTION-CONNECTORS-0017

> The {0} governance action service received a {1} exception when it registered a listener with the governance context.  The exception message is: {2}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.UNABLE_TO_REGISTER_LISTENER` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The governance action service throws a GovernanceServiceException.

**User action**

This is likely to be a configuration error.  Review the description of the exception's message to understand what is not set up correctly and and follow its instructions.


----

### GOVERNANCE-ACTION-CONNECTORS-0018

> The {0} governance action service has no targets to operate on

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.NO_TARGETS` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}` |

**System action**

The governance action service returns an INVALID completion status because it has nothing to work on.

**User action**

This is an error in the way that the governance action service has been called.Identify the way it was called which could be a direct invocation through the initiateGovernanceAction() method,or as part of a governance action process.  Then correct this approach so that an action target is set up.


----

### GOVERNANCE-ACTION-CONNECTORS-0019

> The {0} governance action service has publishZones set to null

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.NO_ZONES` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |

**System action**

The governance action service will remove the AssetZoneMembership from

**User action**

Verify that this is the intended behaviour.  If zones are needed, the zone names are passed with as a configuration property or as a request parameter.  Either method can provide a valid list of zone names expressed as a comma separated list(for example: zone1,zone2) that will control the visibility of the asset.


----

### GOVERNANCE-ACTION-CONNECTORS-0020

> The {0} governance action service is publishing asset {1} to the following zones: {2}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.SETTING_ZONES` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

This governance action service completes normally.

**User action**

Validate that these are the intended zones.


----

### GOVERNANCE-ACTION-CONNECTORS-0021

> The {0} governance action service is initiating governance action process {1} with request parameters {2} for action targets {3}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.INITIATE_PROCESS` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The request is sent to the partner metadata server and executed.  This results in governance services running on one or more engine host servers.

**User action**

Validate that the call to the process has the expected parameters and executes successfully.


----

### GOVERNANCE-ACTION-CONNECTORS-0022

> The {0} governance action service encountered an {1} exception initiating governance action process {2} with request parameters {3} for action targets {4}.  The exception message included was {5}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.INITIATE_PROCESS_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The exception is logged.  More messages may follow if follow on attempts are made to initiate the process.  These can help to determine how to recover from this error.

**User action**

This message contains the exception that was the original cause of the problem. Use the information from the exception stack trace to determine why the connector is not able to initiate the process and resolve that issue.  Use the messages that were subsequently logged during the error handling to discover how to re-run the governance action process once the original cause of the error has been corrected.


----

### GOVERNANCE-ACTION-CONNECTORS-0023

> The {0} governance action service cannot retrieve the template {1} configured in property {2}.

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.MISSING_TEMPLATE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The asset is created with the supplied parameters.

**User action**

Determine whether the template name is specified incorrectly, or if the name is correct, why it is not accessible to governance service.  Once the situation has been corrected, future assets will be created with the right template.  However this asset may need some remediation to add the values that would have been added by the template.


----

### GOVERNANCE-ACTION-CONNECTORS-0024

> {0}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.BLANK_INFO_LOG_MESSAGE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |

**System action**

The message is supplied by the caller.

**User action**

Look at the message text to understand any actions.


----

### GOVERNANCE-ACTION-CONNECTORS-0025

> The {0} governance action service is attaching the retention classification to asset {1} with an archive time of {2} and a delete time of {3}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.SETTING_RETENTION` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

This governance action service completes normally once the retention classification is attached.

**User action**

Validate that these are the intended retention dates.


----

### GOVERNANCE-ACTION-CONNECTORS-0026

> The {0} governance action service has not been passed a steward as an action target

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.NO_STEWARD` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}` |

**System action**

The governance action service will stop with a completion status of INVALID.

**User action**

Rerun the request, but this time add a steward action target.


----

### GOVERNANCE-ACTION-CONNECTORS-0027

> The {0} governance action service has not been passed a survey report as an action target

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.NO_SURVEY_REPORT` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}` |

**System action**

The governance action service will end with a completion status of INVALID.

**User action**

Rerun the request, but this time add a survey report action target.


----

### GOVERNANCE-ACTION-CONNECTORS-0028

> No request for action annotations detected in survey report {0}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.NO_RFAS` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |

**System action**

The governance action service will stop with a completion status of ACTIONED.

**User action**

No action is required since the survey completed successfully.


----

### GOVERNANCE-ACTION-CONNECTORS-0029

> {0} request for action annotations detected in survey report {1}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.RFAS_DETECTED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The governance action service will stop with a completion status of ACTIONED. A ToDo has been created for each Request for Action annotation to notify the appropriate steward.

**User action**

Instructions for the action to take are in the Todos and the attached requests for action.


----

### GOVERNANCE-ACTION-CONNECTORS-0030

> The {0} governance action service has not been passed the unique identifier of the server's template as a request parameter

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.NO_TEMPLATE_GUID` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}` |

**System action**

The governance action service will terminate with a completion status of INVALID.

**User action**

Rerun the request, but this time add a templateGUID request parameter.


----

### GOVERNANCE-ACTION-CONNECTORS-0031

> The {0} governance action service has not been passed the unique identifier of an integration connector as an action target

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.NO_CONNECTOR` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}` |

**System action**

The governance action service will immediately end with a completion status of INVALID.

**User action**

Rerun the request, but this time add an integrationConnector action target.


----

### GOVERNANCE-ACTION-CONNECTORS-0032

> Integration connector {0} is now cataloging {1} server {2}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.CONNECTOR_CONFIGURED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The governance action service has completed the setup of the integration connector.  It will exit with a completion status of ACTIONED.

**User action**

Check that the integration connector is able to contact the server and the cataloguing is operating as expected.


----

### GOVERNANCE-ACTION-CONNECTORS-0033

> The {0} governance action service has not been passed a {1} action target

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.MISSING_ACTION_TARGET` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The governance action service returns an INVALID completion status because a required action target is missing.

**User action**

This is an error in the way that the governance action service has been called since a vital piece of information is missing.Identify the way it was called which could be a direct invocation through the initiateGovernanceAction() method,or as part of a governance action process.  Then correct this approach so that this action target is set up.


----

### GOVERNANCE-ACTION-CONNECTORS-0034

> The {0} governance action service has created a new {1} asset called {2} ({3})

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.NEW_ASSET_CREATED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The governance action service returns an ACTIONED completion status once the new asset is catalogued.

**User action**

Ensure follow-on uses of the asset are successful.


----

### GOVERNANCE-ACTION-CONNECTORS-0035

> The {0} governance action service has deleted the {1} asset called {2} ({3})

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.NEW_ASSET_DELETED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The governance action service returns an ACTIONED completion status once the asset has been removed from the catalog.

**User action**

Ensure follow-on uses of the asset GUID are successful.


----

### GOVERNANCE-ACTION-CONNECTORS-0036

> The {0} governance action service has determined that today is {1}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.DAY_OF_THE_WEEK` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The governance action service has completed successfully.

**User action**

Ensure follow-on uses of the day of the week are successful.


----

### GOVERNANCE-ACTION-CONNECTORS-0037

> The governance service {0} received an unexpected {1} exception during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The service cannot process the current request.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### GOVERNANCE-ACTION-CONNECTORS-0038

> The governance service {0} has completed successfully

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.SERVICE_COMPLETED_SUCCESSFULLY` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |

**System action**

The service is shutting down.

**User action**

No action is required except to validate that the shutdown is occurring at an appropriate time.


----

### GOVERNANCE-ACTION-CONNECTORS-0039

> The {0} governance action service has not been passed a {1} request parameter

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.MISSING_REQUEST_PARAMETER` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The governance action service returns an INVALID completion status with this message.

**User action**

This is an error in the way that the governance action service has been called since a vital piece of information is missing.Identify the way it was called which could be a direct invocation through the initiateGovernanceAction() method,or as part of a governance action process.  Then correct this approach so that this request parameter is set up.


----

### GOVERNANCE-ACTION-CONNECTORS-0040

> The {0} governance action service has created a new {1} digital subscription {2} for {3} {4} ({5}) requested by {6} {7} ({8})

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.NEW_SUBSCRIPTION_CREATED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}`, `{7}`, `{8}` |

**System action**

The governance action service returns an ACTIONED completion status with this subscription as a new action target.

**User action**

Ensure follow-on uses of the subscription are successful.


----

### GOVERNANCE-ACTION-CONNECTORS-0041

> The {0} governance action service has delivered {1} record(s) of table {2} from {3} to {4}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.TABLE_PROVISIONED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The table's records have been written to the destination, replacing any earlier delivery of the same records.

**User action**

No action is required.  This message records what was delivered.


----

### GOVERNANCE-ACTION-CONNECTORS-0042

> The {0} governance action service was unable to deliver table {1} from {2} to {3}: {4} exception with message {5}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.TABLE_PROVISIONING_FAILED` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The table is left as it was.  Where the source is a collection of tables, the service carries on with the remaining tables and reports the failure in its completion status.

**User action**

Use the details from the error message to determine why the table could not be delivered and correct the problem.  The next delivery will try again.


----

### GOVERNANCE-ACTION-CONNECTORS-0043

> The {0} governance action service has delivered all {1} table(s) of collection {2} to {3}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.COLLECTION_PROVISIONED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

Every table the source collection offers has been delivered.

**User action**

No action is required.  This message records what was delivered.


----

### GOVERNANCE-ACTION-CONNECTORS-0044

> The {0} governance action service delivered {1} of the {2} table(s) of collection {3} to {4}; the table(s) it could not deliver are: {5}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionConnectorsAuditCode.COLLECTION_PARTIALLY_PROVISIONED` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The tables that could be delivered have been.  The service completes with a failed status so that the shortfall is visible.

**User action**

Each table that could not be delivered has its own exception message in the audit log.  Correct the problems and the next delivery will try again.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
