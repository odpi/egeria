<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# SurveyActionErrorCode

The SurveyActionErrorCode error code is used to define first failure data capture (FFDC) for errors that occur when working with the Discovery Engine Services. It is used in conjunction with all exceptions, both Checked and Runtime (unchecked).

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 4 |
| **Message identifiers begin** | `OMES-SURVEY-ACTION-400-` |
| **Java class** | `org.odpi.openmetadata.engineservices.surveyaction.ffdc.SurveyActionErrorCode` |
| **Module** | [open-metadata-implementation/engine-services/survey-action/survey-action-api](../../open-metadata-implementation/engine-services/survey-action/survey-action-api) |
| **Source** | [SurveyActionErrorCode.java](../../open-metadata-implementation/engine-services/survey-action/survey-action-api/src/main/java/org/odpi/openmetadata/engineservices/surveyaction/ffdc/SurveyActionErrorCode.java) |
| **Further reading** | <https://egeria-project.org/services/omes/survey-action/overview/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [OMES-SURVEY-ACTION-400-001](#omes-survey-action-400-001) | 400 | The Survey Action OMES are unable to initialize a new instance in server {0}; error message is {1} |
| [OMES-SURVEY-ACTION-400-002](#omes-survey-action-400-002) | 400 | The survey action service {0} linked to request type {1} can not be started.  The {2} exception was returned with message {3} |
| [OMES-SURVEY-ACTION-400-003](#omes-survey-action-400-003) | 400 | The survey action service {0} linked to request type {1} and engine action {2} can not be started because there is no asset action target supplied |
| [OMES-SURVEY-ACTION-400-004](#omes-survey-action-400-004) | 400 | The survey engine action {0} can not be started because there is no governance service context |

----

### OMES-SURVEY-ACTION-400-001

> The Survey Action OMES are unable to initialize a new instance in server {0}; error message is {1}

|  |  |
|---|---|
| **Java constant** | `SurveyActionErrorCode.SERVICE_INSTANCE_FAILURE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The Survey Action OMES detected an error during the start up of a specific server instance.  No survey action services are available in the server.

**User action**

Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server.


----

### OMES-SURVEY-ACTION-400-002

> The survey action service {0} linked to request type {1} can not be started.  The {2} exception was returned with message {3}

|  |  |
|---|---|
| **Java constant** | `SurveyActionErrorCode.INVALID_SURVEY_SERVICE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The survey request is not run and an error is returned to the caller.

**User action**

This may be an error in the survey action service's logic or the survey action service may not be properly deployed or there is a configuration error related to the survey action engine.  The configuration that defines the request type in the survey action engine and links it to the survey action service is maintained in the Governance Configuration API.Verify that this configuration is correct.  If it is then validate that the jar file containing the survey action service's implementation has been deployed so the Survey Action OMES can load it.  If all this is true this it is likely to be a code error in the survey action service in which case, raise an issue with the author of the survey action service to get it fixed.  Once the cause is resolved, retry the survey request.


----

### OMES-SURVEY-ACTION-400-003

> The survey action service {0} linked to request type {1} and engine action {2} can not be started because there is no asset action target supplied

|  |  |
|---|---|
| **Java constant** | `SurveyActionErrorCode.NO_TARGET_ASSET` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The survey request is not run and an error is recorded in the engine action.

**User action**

Retry the survey request and ensuring that an action target is included in the request.


----

### OMES-SURVEY-ACTION-400-004

> The survey engine action {0} can not be started because there is no governance service context

|  |  |
|---|---|
| **Java constant** | `SurveyActionErrorCode.NULL_REQUEST` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The survey request is not run and an error is recorded in the engine action because the governance service is not set up property.

**User action**

This is an unexpected error, you may need to trace through the code to find out what has happened.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
