<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# YAMLAuditCode

The YAMLAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 4 |
| **Message identifiers begin** | `YAML-SECRETS-STORE-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.ffdc.YAMLAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/secrets-store-connectors/yaml-secrets-store-connector](../../../open-metadata-implementation/adapters/open-connectors/secrets-store-connectors/yaml-secrets-store-connector) |
| **Source** | [YAMLAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/secrets-store-connectors/yaml-secrets-store-connector/src/main/java/org/odpi/openmetadata/adapters/connectors/secretsstore/yaml/ffdc/YAMLAuditCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/secrets-store-connector/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [YAML-SECRETS-STORE-CONNECTOR-0001](#yaml-secrets-store-connector-0001) | EXCEPTION | The YAML secrets store connector received an unexpected {0} exception during method {1}; the error message was: {2} |
| [YAML-SECRETS-STORE-CONNECTOR-0002](#yaml-secrets-store-connector-0002) | INFO | The YAML secrets store connector is creating a new secrets store at {0} |
| [YAML-SECRETS-STORE-CONNECTOR-0003](#yaml-secrets-store-connector-0003) | SECURITY | Adding client-side secret {0} to secrets store {1} |
| [YAML-SECRETS-STORE-CONNECTOR-0004](#yaml-secrets-store-connector-0004) | SECURITY | Removing client-side secret {0} from from secrets store {1} |

----

### YAML-SECRETS-STORE-CONNECTOR-0001

> The YAML secrets store connector received an unexpected {0} exception during method {1}; the error message was: {2}

|  |  |
|---|---|
| **Java constant** | `YAMLAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector cannot process the current request.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### YAML-SECRETS-STORE-CONNECTOR-0002

> The YAML secrets store connector is creating a new secrets store at {0}

|  |  |
|---|---|
| **Java constant** | `YAMLAuditCode.NEW_SECRETS_STORE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |

**System action**

The connector needs to create a new secrets store.

**User action**

Validate that this new secrets store should be created.


----

### YAML-SECRETS-STORE-CONNECTOR-0003

> Adding client-side secret {0} to secrets store {1}

|  |  |
|---|---|
| **Java constant** | `YAMLAuditCode.ADDING_CLIENT_SIDE_SECRET` |
| **Severity** | SECURITY - Unauthorized access to a service or metadata instance has been attempted. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

A secrets collection for a client-side secret is from the named secrets store.

**User action**

Make sure this client-side secret is valid and has the correct permissions.


----

### YAML-SECRETS-STORE-CONNECTOR-0004

> Removing client-side secret {0} from from secrets store {1}

|  |  |
|---|---|
| **Java constant** | `YAMLAuditCode.REMOVING_CLIENT_SIDE_SECRET` |
| **Severity** | SECURITY - Unauthorized access to a service or metadata instance has been attempted. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

A secrets collection for a client-side secret is removed from the named secrets store.

**User action**

Make sure this client-side secret is no longer needed.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
