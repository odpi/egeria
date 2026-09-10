<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# BitolIntegrationConnectorErrorCode

The BitolIntegrationConnectorErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the Bitol integration connectors. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 1 |
| **Message identifiers begin** | `BITOL-INTEGRATION-CONNECTOR-500-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.integration.bitol.ffdc.BitolIntegrationConnectorErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/integration-connectors/bitol-integration-connectors](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/bitol-integration-connectors) |
| **Source** | [BitolIntegrationConnectorErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/bitol-integration-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/integration/bitol/ffdc/BitolIntegrationConnectorErrorCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/digital-product/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [BITOL-INTEGRATION-CONNECTOR-500-001](#bitol-integration-connector-500-001) | 500 | The {0} integration connector received an unexpected {1} exception in method {2} when working with Bitol documents; the error message was: {3} |

----

### BITOL-INTEGRATION-CONNECTOR-500-001

> The {0} integration connector received an unexpected {1} exception in method {2} when working with Bitol documents; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `BitolIntegrationConnectorErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot process one or more Bitol documents.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
