<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# JacquardErrorCode

The JacquardErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the Kafka monitor integration connector. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 2 |
| **Message identifiers begin** | `JACQUARD-HARVESTER-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.jacquard.ffdc.JacquardErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/nanny-connectors](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors) |
| **Source** | [JacquardErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/jacquard/ffdc/JacquardErrorCode.java) |
| **Further reading** | <https://egeria-project.org/patterns/harvest-and-publish/overview/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [JACQUARD-HARVESTER-400-002](#jacquard-harvester-400-002) | 400 | Integration connector {0} has been configured without a secrets connector |
| [JACQUARD-HARVESTER-500-001](#jacquard-harvester-500-001) | 500 | The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3} |

----

### JACQUARD-HARVESTER-400-002

> Integration connector {0} has been configured without a secrets connector

|  |  |
|---|---|
| **Java constant** | `JacquardErrorCode.NO_SECRETS` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The connector is moved to FAILED status and will no longer be called to build open metadata products.

**User action**

Update the connection information for the connector to include an embedded SecretsConnector configured to point to the secret store/collection for use by the digital products when they are harvesting open metadata.  The connection information is stored in the open metadata ecosystem. By default this is seeded from JacquardHarvesterContentPack.omarchive.


----

### JACQUARD-HARVESTER-500-001

> The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `JacquardErrorCode.UNEXPECTED_EXCEPTION` |
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
