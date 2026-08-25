<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# SurveyServiceAuditCode

The SurveyServiceAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 5 |
| **Message identifiers begin** | `SURVEY-ACTION-SERVICE-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.surveyaction.ffdc.SurveyServiceAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/file-survey-connectors](../../../open-metadata-implementation/adapters/open-connectors/file-survey-connectors) |
| **Source** | [SurveyServiceAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/file-survey-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/surveyaction/ffdc/SurveyServiceAuditCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/survey-action-service/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [SURVEY-ACTION-SERVICE-0002](#survey-action-service-0002) | INFO | The survey action service {0} is creating log file {1} which is catalogued as CSVFile asset {2} |
| [SURVEY-ACTION-SERVICE-0003](#survey-action-service-0003) | INFO | The survey action service {0} is overriding log file {1} |
| [SURVEY-ACTION-SERVICE-0004](#survey-action-service-0004) | INFO | The survey action service {0} is surveying the {1} folder (directory) with an analysis level of {2} |
| [SURVEY-ACTION-SERVICE-0005](#survey-action-service-0005) | INFO | The survey action service {0} is has surveyed {1} files and folders (directories) |
| [SURVEY-ACTION-SERVICE-0006](#survey-action-service-0006) | ERROR | The survey action service {0} received an unexpected IO exception {1} when it attempted to access the attributes of file {2}; the error message was: {3} |

----

### SURVEY-ACTION-SERVICE-0002

> The survey action service {0} is creating log file {1} which is catalogued as CSVFile asset {2}

|  |  |
|---|---|
| **Java constant** | `SurveyServiceAuditCode.CREATING_LOG_FILE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

This message tells the survey team that a particular survey log file is being created.

**User action**

No specific action is required.  The results are added to the log file and the asset for this log file is catalogued as a CSV file.


----

### SURVEY-ACTION-SERVICE-0003

> The survey action service {0} is overriding log file {1}

|  |  |
|---|---|
| **Java constant** | `SurveyServiceAuditCode.REUSING_LOG_FILE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

This message warns the survey team that a particular survey log file is being reused.

**User action**

No specific action is required.  The new results are appended to the existing results.


----

### SURVEY-ACTION-SERVICE-0004

> The survey action service {0} is surveying the {1} folder (directory) with an analysis level of {2}

|  |  |
|---|---|
| **Java constant** | `SurveyServiceAuditCode.SURVEYING_FOLDER` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

This message shows that the starting folder to survey.

**User action**

No specific action is required.  This message is marking the start of the survey process.


----

### SURVEY-ACTION-SERVICE-0005

> The survey action service {0} is has surveyed {1} files and folders (directories)

|  |  |
|---|---|
| **Java constant** | `SurveyServiceAuditCode.PROGRESS_REPORT` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

This message shows that the progress of the survey.

**User action**

No specific action is required.  This message is marking the progress of the survey process.


----

### SURVEY-ACTION-SERVICE-0006

> The survey action service {0} received an unexpected IO exception {1} when it attempted to access the attributes of file {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `SurveyServiceAuditCode.FILE_IO_ERROR` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The file is skipped and will not appear in the totals for this folder.  However a separate request for action annotation with a log file of all of the inaccessible files is created.

**User action**

If this file is of interest and you want it to be included in the survey report, use the details from the error message to determine the cause of the access error; retry the survey once it is resolved.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
