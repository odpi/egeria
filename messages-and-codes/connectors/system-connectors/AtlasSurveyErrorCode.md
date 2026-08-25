<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# AtlasSurveyErrorCode

The AtlasSurveyErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the Apache Atlas REST connector. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 3 |
| **Message identifiers begin** | `APACHE-ATLAS-SURVEY-ACTION-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.apacheatlas.survey.ffdc.AtlasSurveyErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/system-connectors/apache-atlas-connectors](../../../open-metadata-implementation/adapters/open-connectors/system-connectors/apache-atlas-connectors) |
| **Source** | [AtlasSurveyErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/system-connectors/apache-atlas-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/apacheatlas/survey/ffdc/AtlasSurveyErrorCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/survey-action-service/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [APACHE-ATLAS-SURVEY-ACTION-CONNECTOR-400-002](#apache-atlas-survey-action-connector-400-002) | 400 | The root schema type for Apache Atlas Software Server {0} is of type {1} rather than {2}. Apache Atlas Survey Connector {3} is not able to continue with its schema analysis.  The existing schema type properties are {4} |
| [APACHE-ATLAS-SURVEY-ACTION-CONNECTOR-400-003](#apache-atlas-survey-action-connector-400-003) | 400 | The asset universe for Apache Atlas Software Server is null. Apache Atlas Survey Connector {0} is not able to continue with its schema analysis |
| [APACHE-ATLAS-SURVEY-ACTION-CONNECTOR-500-001](#apache-atlas-survey-action-connector-500-001) | 500 | The {0} Apache Atlas Survey Action connector received an unexpected {1} exception during method {2}; the error message was: {3} |

----

### APACHE-ATLAS-SURVEY-ACTION-CONNECTOR-400-002

> The root schema type for Apache Atlas Software Server {0} is of type {1} rather than {2}. Apache Atlas Survey Connector {3} is not able to continue with its schema analysis.  The existing schema type properties are {4}

|  |  |
|---|---|
| **Java constant** | `AtlasSurveyErrorCode.WRONG_ROOT_SCHEMA_TYPE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The connector cannot continue to define the schema for the Apache Atlas Server based on its defined types because it cannot understand the existing root schema type.

**User action**

Use the details from the error message to determine the origin and reason for the existing schema type.  If it is correct then disable the schema analysis of this survey action service.  It the existing root schema type should not be present, then delete it, and re-run the failed survey action service.


----

### APACHE-ATLAS-SURVEY-ACTION-CONNECTOR-400-003

> The asset universe for Apache Atlas Software Server is null. Apache Atlas Survey Connector {0} is not able to continue with its schema analysis

|  |  |
|---|---|
| **Java constant** | `AtlasSurveyErrorCode.MISSING_ASSET_UNIVERSE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The connector cannot continue to define the schema for the Apache Atlas Server based on its defined types because it cannot access the existing root schema type from the asset universe because it is null.

**User action**

Use the details from the error message to determine the asset universe being null.  Correct the error and re-run the failed survey action service.


----

### APACHE-ATLAS-SURVEY-ACTION-CONNECTOR-500-001

> The {0} Apache Atlas Survey Action connector received an unexpected {1} exception during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `AtlasSurveyErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot continue to survey the Apache Atlas Server.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
