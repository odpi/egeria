<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# HarvestSurveysErrorCode

The HarvestSurveysErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the integration connector. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 1 |
| **Message identifiers begin** | `HARVEST-SURVEYS-500-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.externalharvesters.harvestsurveys.ffdc.HarvestSurveysErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/nanny-connectors](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors) |
| **Source** | [HarvestSurveysErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/externalharvesters/harvestsurveys/ffdc/HarvestSurveysErrorCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/survey-report/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [HARVEST-SURVEYS-500-001](#harvest-surveys-500-001) | 500 | The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3} |

----

### HARVEST-SURVEYS-500-001

> The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `HarvestSurveysErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot catalog one or more metadata elements.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
