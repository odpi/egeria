<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OpenLineageIntegrationConnectorErrorCode

The OpenLineageIntegrationConnectorErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the Kafka monitor integration connector. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 2 |
| **Message identifiers begin** | `OPEN-LINEAGE-INTEGRATION-CONNECTOR-500-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.integration.openlineage.ffdc.OpenLineageIntegrationConnectorErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/integration-connectors/openlineage-integration-connectors](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/openlineage-integration-connectors) |
| **Source** | [OpenLineageIntegrationConnectorErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/openlineage-integration-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/integration/openlineage/ffdc/OpenLineageIntegrationConnectorErrorCode.java) |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-open-lineage/overview/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [OPEN-LINEAGE-INTEGRATION-CONNECTOR-500-001](#open-lineage-integration-connector-500-001) | 500 | The {0} integration connector received an unexpected {1} exception in method {2} when working with open lineage events; the error message was: {3} |
| [OPEN-LINEAGE-INTEGRATION-CONNECTOR-500-002](#open-lineage-integration-connector-500-002) | 500 | The {0} integration connector received a null raw open lineage event in method {1} when working with open lineage events |

----

### OPEN-LINEAGE-INTEGRATION-CONNECTOR-500-001

> The {0} integration connector received an unexpected {1} exception in method {2} when working with open lineage events; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `OpenLineageIntegrationConnectorErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot process one or more lineage events.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### OPEN-LINEAGE-INTEGRATION-CONNECTOR-500-002

> The {0} integration connector received a null raw open lineage event in method {1} when working with open lineage events

|  |  |
|---|---|
| **Java constant** | `OpenLineageIntegrationConnectorErrorCode.NO_RAW_EVENT` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector cannot process an open lineage events because it has no content.

**User action**

Validate the set up and source of the events and retry the request once it is resolved.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
