<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# WSO2MISurveyErrorCode

The WSO2MISurveyErrorCode is used to define first failure data capture (FFDC) for errors that occur when running the WSO2 Micro Integrator survey action service. It is used in conjunction with both Checked and Runtime (unchecked) exceptions. The 5 fields in the enum are:

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 1 |
| **Message identifiers begin** | `WSO2MI-SURVEY-ACTION-CONNECTOR-500-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.wso2mi.survey.ffdc.WSO2MISurveyErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/data-manager-connectors/wso2mi-connectors](../../../open-metadata-implementation/adapters/open-connectors/data-manager-connectors/wso2mi-connectors) |
| **Source** | [WSO2MISurveyErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/data-manager-connectors/wso2mi-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/wso2mi/survey/ffdc/WSO2MISurveyErrorCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/survey-action-service/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [WSO2MI-SURVEY-ACTION-CONNECTOR-500-001](#wso2mi-survey-action-connector-500-001) | 500 | The {0} WSO2 Micro Integrator Survey Action connector received an unexpected {1} exception during method {2}; the error message was: {3} |

----

### WSO2MI-SURVEY-ACTION-CONNECTOR-500-001

> The {0} WSO2 Micro Integrator Survey Action connector received an unexpected {1} exception during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `WSO2MISurveyErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot continue to survey the WSO2 Micro Integrator.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
