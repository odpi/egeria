<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# SurveyActionAuditCode

The SurveyActionAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 9 |
| **Message identifiers begin** | `OMES-SURVEY-ACTION-` |
| **Java class** | `org.odpi.openmetadata.engineservices.surveyaction.ffdc.SurveyActionAuditCode` |
| **Module** | [open-metadata-implementation/engine-services/survey-action/survey-action-api](../../open-metadata-implementation/engine-services/survey-action/survey-action-api) |
| **Source** | [SurveyActionAuditCode.java](../../open-metadata-implementation/engine-services/survey-action/survey-action-api/src/main/java/org/odpi/openmetadata/engineservices/surveyaction/ffdc/SurveyActionAuditCode.java) |
| **Further reading** | <https://egeria-project.org/services/omes/survey-action/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OMES-SURVEY-ACTION-0001](#omes-survey-action-0001) | STARTUP | The Survey Action engine services are initializing in server {0} |
| [OMES-SURVEY-ACTION-0012](#omes-survey-action-0012) | ERROR | The Survey Action OMES cannot initialize a new instance of itself in server {0}; error message is {1} |
| [OMES-SURVEY-ACTION-0014](#omes-survey-action-0014) | SHUTDOWN | The Survey Action OMES in server {0} is shutting down |
| [OMES-SURVEY-ACTION-0016](#omes-survey-action-0016) | SHUTDOWN | The Survey Action OMES in server {0} has completed shutdown |
| [OMES-SURVEY-ACTION-0017](#omes-survey-action-0017) | STARTUP | The survey action service {0} is starting to analyze asset {1} with request type {2} in survey action engine {3} (guid={4}); the results will be stored in survey report {5} |
| [OMES-SURVEY-ACTION-0018](#omes-survey-action-0018) | EXCEPTION | The survey action service {0} threw a {1} exception during the generation of survey report {2} for asset {3} during request type {4} in survey action engine {5} (guid={6}). The error message was {7} |
| [OMES-SURVEY-ACTION-0019](#omes-survey-action-0019) | SHUTDOWN | The survey action service {0} has completed the analysis of asset {1} with request type {2} in {3} milliseconds; the results are stored in survey report {4} |
| [OMES-SURVEY-ACTION-0029](#omes-survey-action-0029) | EXCEPTION | The survey action service {0} linked to request type {1} can not be started.  The {2} exception was returned with message {3} |
| [OMES-SURVEY-ACTION-0030](#omes-survey-action-0030) | INFO | The survey action service {0} linked to request type {1} for engine action {2} is processing asset {3} and ignoring the following asset action targets: {4} |

----

### OMES-SURVEY-ACTION-0001

> The Survey Action engine services are initializing in server {0}

|  |  |
|---|---|
| **Java constant** | `SurveyActionAuditCode.ENGINE_SERVICE_INITIALIZING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}` |

**System action**

A new OMAG server has been started that is configured to run the Survey Action OMES.  Within this engine service are one or more survey action engines that analyze the content of assets on demand and create annotation metadata. The configuration for the survey action engines is retrieved from the metadata server and the survey action engines are initialized.

**User action**

Verify that the start up sequence goes on to initialize the configured survey action engines.


----

### OMES-SURVEY-ACTION-0012

> The Survey Action OMES cannot initialize a new instance of itself in server {0}; error message is {1}

|  |  |
|---|---|
| **Java constant** | `SurveyActionAuditCode.SERVICE_INSTANCE_FAILURE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The engine services detected an error during the start up of a specific engine host server instance.  Its survey action services are not available for the server.

**User action**

Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server.


----

### OMES-SURVEY-ACTION-0014

> The Survey Action OMES in server {0} is shutting down

|  |  |
|---|---|
| **Java constant** | `SurveyActionAuditCode.SERVER_SHUTTING_DOWN` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The local administrator has requested shut down of this engine service.

**User action**

Verify that this server is no longer needed and the shutdown is expected.


----

### OMES-SURVEY-ACTION-0016

> The Survey Action OMES in server {0} has completed shutdown

|  |  |
|---|---|
| **Java constant** | `SurveyActionAuditCode.SERVER_SHUTDOWN` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The local administrator has requested shut down of this engine service and the operation has completed.

**User action**

Verify that all configured survey action engines shut down successfully.


----

### OMES-SURVEY-ACTION-0017

> The survey action service {0} is starting to analyze asset {1} with request type {2} in survey action engine {3} (guid={4}); the results will be stored in survey report {5}

|  |  |
|---|---|
| **Java constant** | `SurveyActionAuditCode.SURVEY_ACTION_SERVICE_STARTING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

A new survey request is being processed.

**User action**

Verify that the survey action service ran to completion.


----

### OMES-SURVEY-ACTION-0018

> The survey action service {0} threw a {1} exception during the generation of survey report {2} for asset {3} during request type {4} in survey action engine {5} (guid={6}). The error message was {7}

|  |  |
|---|---|
| **Java constant** | `SurveyActionAuditCode.SURVEY_ACTION_SERVICE_FAILED` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}`, `{7}` |

**System action**

A survey action service failed to complete the analysis of an asset.

**User action**

Review the exception to determine the cause of the error.


----

### OMES-SURVEY-ACTION-0019

> The survey action service {0} has completed the analysis of asset {1} with request type {2} in {3} milliseconds; the results are stored in survey report {4}

|  |  |
|---|---|
| **Java constant** | `SurveyActionAuditCode.SURVEY_ACTION_SERVICE_COMPLETE` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

A survey request has completed.

**User action**

It is possible to query the result of the survey request through Egeria's Open Metadata REST APIs.


----

### OMES-SURVEY-ACTION-0029

> The survey action service {0} linked to request type {1} can not be started.  The {2} exception was returned with message {3}

|  |  |
|---|---|
| **Java constant** | `SurveyActionAuditCode.INVALID_SURVEY_ACTION_SERVICE` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The survey request is not run and an error is returned to the caller.

**User action**

This may be an error in the survey action service's logic or the survey action service may not be properly deployed or there is a configuration error related to the survey action engine.  The configuration that defines the request type in the survey action engine and links it to the survey action service is maintained in the metadata server by the Governance Engine OMAS's configuration API.Verify that this configuration is correct.  If it is then validate that the jar file containing the survey action service's implementation has been deployed so the Survey Action OMES can load it.  If all this is true this it is likely to be a code error in the survey action service in which case, raise an issue with the author of the survey action service to get it fixed.  Once the cause is resolved, retry the survey request.


----

### OMES-SURVEY-ACTION-0030

> The survey action service {0} linked to request type {1} for engine action {2} is processing asset {3} and ignoring the following asset action targets: {4}

|  |  |
|---|---|
| **Java constant** | `SurveyActionAuditCode.IGNORING_ASSETS` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

There are multiple assets in the action targets.  The survey action service can only process one of them.  The other assets are ignored.

**User action**

Create a new engine action for each of the ignored assets so that they each run in their own survey action service.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
