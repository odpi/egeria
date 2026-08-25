<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# SmartCollectionsErrorCode

The SmartCollectionsErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the Smart Collections integration connector. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 2 |
| **Message identifiers begin** | `SMART-COLLECTIONS-INTEGRATION-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.integration.smartcollections.ffdc.SmartCollectionsErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/integration-connectors/smart-collections-integration-connector](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/smart-collections-integration-connector) |
| **Source** | [SmartCollectionsErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/integration-connectors/smart-collections-integration-connector/src/main/java/org/odpi/openmetadata/adapters/connectors/integration/smartcollections/ffdc/SmartCollectionsErrorCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/integration-connector/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [SMART-COLLECTIONS-INTEGRATION-CONNECTOR-400-001](#smart-collections-integration-connector-400-001) | 400 | Connector {0} has not been supplied with a secret that can be used to authenticate the REST calls it makes to run saved queries |
| [SMART-COLLECTIONS-INTEGRATION-CONNECTOR-500-001](#smart-collections-integration-connector-500-001) | 500 | The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3} |

----

### SMART-COLLECTIONS-INTEGRATION-CONNECTOR-400-001

> Connector {0} has not been supplied with a secret that can be used to authenticate the REST calls it makes to run saved queries

|  |  |
|---|---|
| **Java constant** | `SmartCollectionsErrorCode.NO_SECRETS` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The connector is not able to issue the REST calls needed to refresh the membership of its results sets.

**User action**

Add an embedded connection to a secrets store connector to the connector's connection so that it can retrieve the userId and password it needs to call the metadata access server.


----

### SMART-COLLECTIONS-INTEGRATION-CONNECTOR-500-001

> The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `SmartCollectionsErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot refresh the membership of one or more results sets.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
