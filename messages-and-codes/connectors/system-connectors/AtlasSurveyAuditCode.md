<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# AtlasSurveyAuditCode

The AtlasSurveyAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 2 |
| **Message identifiers begin** | `APACHE-ATLAS-SURVEY-ACTION-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.apacheatlas.survey.ffdc.AtlasSurveyAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/system-connectors/apache-atlas-connectors](../../../open-metadata-implementation/adapters/open-connectors/system-connectors/apache-atlas-connectors) |
| **Source** | [AtlasSurveyAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/system-connectors/apache-atlas-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/apacheatlas/survey/ffdc/AtlasSurveyAuditCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/survey-action-service/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [APACHE-ATLAS-SURVEY-ACTION-CONNECTOR-0003](#apache-atlas-survey-action-connector-0003) | ERROR | The root schema type for Apache Atlas Software Server {0} is of type {1} rather than {2}. Apache Atlas Survey Connector {3} is not able to continue with its schema analysis.  The existing schema type properties are {4} |
| [APACHE-ATLAS-SURVEY-ACTION-CONNECTOR-0004](#apache-atlas-survey-action-connector-0004) | ERROR | The asset universe for Apache Atlas Software Server is null. Apache Atlas Survey Connector {0} is not able to continue with its schema analysis |

----

### APACHE-ATLAS-SURVEY-ACTION-CONNECTOR-0003

> The root schema type for Apache Atlas Software Server {0} is of type {1} rather than {2}. Apache Atlas Survey Connector {3} is not able to continue with its schema analysis.  The existing schema type properties are {4}

|  |  |
|---|---|
| **Java constant** | `AtlasSurveyAuditCode.WRONG_ROOT_SCHEMA_TYPE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The connector cannot continue to define the schema for the Apache Atlas Server based on its defined types because it cannot understand the existing root schema type.

**User action**

Use the details from the error message to determine the origin and reason for the existing schema type.  If it is correct then disable the schema analysis of this survey action service.  It the existing root schema type should not be present, then delete it, and re-run the failed survey action service.


----

### APACHE-ATLAS-SURVEY-ACTION-CONNECTOR-0004

> The asset universe for Apache Atlas Software Server is null. Apache Atlas Survey Connector {0} is not able to continue with its schema analysis

|  |  |
|---|---|
| **Java constant** | `AtlasSurveyAuditCode.MISSING_ASSET_UNIVERSE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}` |

**System action**

The connector cannot continue to define the schema for the Apache Atlas Server based on its defined types because it cannot access the existing root schema type from the asset universe because it is null.

**User action**

Use the details from the error message to determine the asset universe being null.  Correct the error and re-run the failed survey action service.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
