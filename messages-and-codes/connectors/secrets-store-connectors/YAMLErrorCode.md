<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# YAMLErrorCode

The YAMLErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the Basic File Connector. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 1 |
| **Message identifiers begin** | `YAML-SECRETS-STORE-CONNECTOR-500-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.ffdc.YAMLErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/secrets-store-connectors/yaml-secrets-store-connector](../../../open-metadata-implementation/adapters/open-connectors/secrets-store-connectors/yaml-secrets-store-connector) |
| **Source** | [YAMLErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/secrets-store-connectors/yaml-secrets-store-connector/src/main/java/org/odpi/openmetadata/adapters/connectors/secretsstore/yaml/ffdc/YAMLErrorCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/secrets-store-connector/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [YAML-SECRETS-STORE-CONNECTOR-500-002](#yaml-secrets-store-connector-500-002) | 500 | The connector received an unexpected IO exception when reading the file named {0}; the error message was: {1} |

----

### YAML-SECRETS-STORE-CONNECTOR-500-002

> The connector received an unexpected IO exception when reading the file named {0}; the error message was: {1}

|  |  |
|---|---|
| **Java constant** | `YAMLErrorCode.UNEXPECTED_IO_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector cannot save secrets to the file.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
