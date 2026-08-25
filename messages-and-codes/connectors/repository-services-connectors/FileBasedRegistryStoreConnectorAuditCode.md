<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# FileBasedRegistryStoreConnectorAuditCode

The FileBasedRegistryStoreConnectorAuditCode is used to define the message content for the Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 10 |
| **Message identifiers begin** | `OCF-FILE-REGISTRY-STORE-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.repositoryservices.cohortregistrystore.file.ffdc.FileBasedRegistryStoreConnectorAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/repository-services-connectors/cohort-registry-store-connectors/cohort-registry-file-store-connector](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/cohort-registry-store-connectors/cohort-registry-file-store-connector) |
| **Source** | [FileBasedRegistryStoreConnectorAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/cohort-registry-store-connectors/cohort-registry-file-store-connector/src/main/java/org/odpi/openmetadata/adapters/repositoryservices/cohortregistrystore/file/ffdc/FileBasedRegistryStoreConnectorAuditCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/cohort-registry-store-connector/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OCF-FILE-REGISTRY-STORE-CONNECTOR-0115](#ocf-file-registry-store-connector-0115) | COHORT | Creating new cohort registry store {0} |
| [OCF-FILE-REGISTRY-STORE-CONNECTOR-0116](#ocf-file-registry-store-connector-0116) | EXCEPTION | Unable to write to cohort registry store {0} |
| [OCF-FILE-REGISTRY-STORE-CONNECTOR-0117](#ocf-file-registry-store-connector-0117) | ERROR | Unable to read or write to cohort registry store {0} because registration information is null |
| [OCF-FILE-REGISTRY-STORE-CONNECTOR-0118](#ocf-file-registry-store-connector-0118) | ERROR | Unable to process the {0} request for cohort {1} from cohort member {2} because there is no cohort registry store |
| [OCF-FILE-REGISTRY-STORE-CONNECTOR-0119](#ocf-file-registry-store-connector-0119) | ACTION | Metadata collection id {0} is being used by server {1} and server {2} |
| [OCF-FILE-REGISTRY-STORE-CONNECTOR-0120](#ocf-file-registry-store-connector-0120) | ACTION | Server {0} has registered with a null metadata collection id |
| [OCF-FILE-REGISTRY-STORE-CONNECTOR-0121](#ocf-file-registry-store-connector-0121) | ACTION | Server name {0} is being used by metadata collection {1} and metadata collection {2} |
| [OCF-FILE-REGISTRY-STORE-CONNECTOR-0122](#ocf-file-registry-store-connector-0122) | ACTION | The server using metadata collection id {0} has registered with a null server name |
| [OCF-FILE-REGISTRY-STORE-CONNECTOR-0123](#ocf-file-registry-store-connector-0123) | ACTION | Server name {0} with metadata collection id {1} is using the same server address of {2} as server name {3} with metadata collection id {4} |
| [OCF-FILE-REGISTRY-STORE-CONNECTOR-0125](#ocf-file-registry-store-connector-0125) | ACTION | The server name {0} using metadata collection id {1} has registered with a null server connection |

----

### OCF-FILE-REGISTRY-STORE-CONNECTOR-0115

> Creating new cohort registry store {0}

|  |  |
|---|---|
| **Java constant** | `FileBasedRegistryStoreConnectorAuditCode.CREATE_REGISTRY_FILE` |
| **Severity** | COHORT - The server is exchanging registration information about an open metadata repository cohort that it is connecting to. |
| **Message inserts** | `{0}` |

**System action**

The local server is creating a new cohort registry store. The local server should continue to operate correctly.

**User action**

Verify that the local server is connecting to the open metadata repository cohort forthe first time.


----

### OCF-FILE-REGISTRY-STORE-CONNECTOR-0116

> Unable to write to cohort registry store {0}

|  |  |
|---|---|
| **Java constant** | `FileBasedRegistryStoreConnectorAuditCode.UNUSABLE_REGISTRY_FILE` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}` |

**System action**

The local server can not write to the cohort registry store. This is a serious issue because the local server is not able to record its interaction with other servers in the cohort.

**User action**

Shutdown the local server and resolve the issue with the repository store.


----

### OCF-FILE-REGISTRY-STORE-CONNECTOR-0117

> Unable to read or write to cohort registry store {0} because registration information is null

|  |  |
|---|---|
| **Java constant** | `FileBasedRegistryStoreConnectorAuditCode.NULL_MEMBER_REGISTRATION` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}` |

**System action**

The local server can not manage a member registration in the cohort registry store because the registration information is null. This is a serious issue because the local server is not able to record its interaction with other servers in the cohort.

**User action**

Shutdown the local server and resolve the issue with the cohort registry.


----

### OCF-FILE-REGISTRY-STORE-CONNECTOR-0118

> Unable to process the {0} request for cohort {1} from cohort member {2} because there is no cohort registry store

|  |  |
|---|---|
| **Java constant** | `FileBasedRegistryStoreConnectorAuditCode.MISSING_MEMBER_REGISTRATION` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The local server can not process a member registration event because the registration information cal not be stored in the cohort registry store. This may simply be a timing issue. However, it may be the result of an earlier issue with the local cohort registry store.

**User action**

Verify that there are no issues with writing to the cohort registry store.


----

### OCF-FILE-REGISTRY-STORE-CONNECTOR-0119

> Metadata collection id {0} is being used by server {1} and server {2}

|  |  |
|---|---|
| **Java constant** | `FileBasedRegistryStoreConnectorAuditCode.DUPLICATE_REGISTERED_MC_ID` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The local server has detected a duplicate record in its cohort registry store.

**User action**

Verify that this is caused by the rename of a server.


----

### OCF-FILE-REGISTRY-STORE-CONNECTOR-0120

> Server {0} has registered with a null metadata collection id

|  |  |
|---|---|
| **Java constant** | `FileBasedRegistryStoreConnectorAuditCode.NULL_REGISTERED_MC_ID` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}` |

**System action**

The local server has detected an invalid record in its cohort registry store.

**User action**

Correct the configuration of the named server so that it has a valid metadata collection id.


----

### OCF-FILE-REGISTRY-STORE-CONNECTOR-0121

> Server name {0} is being used by metadata collection {1} and metadata collection {2}

|  |  |
|---|---|
| **Java constant** | `FileBasedRegistryStoreConnectorAuditCode.DUPLICATE_REGISTERED_SERVER_NAME` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The local server has identified a duplicate record in its cohort registry store.

**User action**

This suggests that a server has been restarted with a different metadata collection id.


----

### OCF-FILE-REGISTRY-STORE-CONNECTOR-0122

> The server using metadata collection id {0} has registered with a null server name

|  |  |
|---|---|
| **Java constant** | `FileBasedRegistryStoreConnectorAuditCode.NULL_REGISTERED_SERVER_NAME` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}` |

**System action**

The local server has detected an suspicious record in its cohort registry store.

**User action**

Correct the configuration of the named server so that it has a valid server name.


----

### OCF-FILE-REGISTRY-STORE-CONNECTOR-0123

> Server name {0} with metadata collection id {1} is using the same server address of {2} as server name {3} with metadata collection id {4}

|  |  |
|---|---|
| **Java constant** | `FileBasedRegistryStoreConnectorAuditCode.DUPLICATE_REGISTERED_SERVER_ADDR` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The local server has found a duplicate record in its cohort registry store.

**User action**

This indicates that a server has been restarted with a different metadata collection id.


----

### OCF-FILE-REGISTRY-STORE-CONNECTOR-0125

> The server name {0} using metadata collection id {1} has registered with a null server connection

|  |  |
|---|---|
| **Java constant** | `FileBasedRegistryStoreConnectorAuditCode.NULL_REGISTERED_SERVER_CONNECTION` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The local server has found an suspicious record in its cohort registry store.

**User action**

Correct the configuration of one of the named servers so that it has a unique server address.  Otherwise one of the server will not be called during federated queries issued by the enterprise repository services.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
