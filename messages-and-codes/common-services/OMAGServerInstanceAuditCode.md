<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OMAGServerInstanceAuditCode

The OMAGServerInstanceAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 2 |
| **Message identifiers begin** | `OMAG-MULTI-TENANT-` |
| **Java class** | `org.odpi.openmetadata.commonservices.multitenant.ffdc.OMAGServerInstanceAuditCode` |
| **Module** | [open-metadata-implementation/common-services/multi-tenant](../../open-metadata-implementation/common-services/multi-tenant) |
| **Source** | [OMAGServerInstanceAuditCode.java](../../open-metadata-implementation/common-services/multi-tenant/src/main/java/org/odpi/openmetadata/commonservices/multitenant/ffdc/OMAGServerInstanceAuditCode.java) |
| **Further reading** | <https://egeria-project.org/services/multi-tenant/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OMAG-MULTI-TENANT-0001](#omag-multi-tenant-0001) | EXCEPTION | Method {0} called on behalf of the {1} service detected a {2} exception when creating an open metadata topic connection because the connector provider is incorrect.  The error message was {3} |
| [OMAG-MULTI-TENANT-0002](#omag-multi-tenant-0002) | ERROR | Method {0} called on behalf of the {1} service cannot create a client-side open metadata topic connection because the topic name is not configured in the configuration for this service |

----

### OMAG-MULTI-TENANT-0001

> Method {0} called on behalf of the {1} service detected a {2} exception when creating an open metadata topic connection because the connector provider is incorrect.  The error message was {3}

|  |  |
|---|---|
| **Java constant** | `OMAGServerInstanceAuditCode.BAD_TOPIC_CONNECTOR_PROVIDER` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

This is an internal error.  The access service is not using a valid connector provider.

**User action**

Raise an issue on Egeria's GitHub and work with the Egeria community to resolve.


----

### OMAG-MULTI-TENANT-0002

> Method {0} called on behalf of the {1} service cannot create a client-side open metadata topic connection because the topic name is not configured in the configuration for this service

|  |  |
|---|---|
| **Java constant** | `OMAGServerInstanceAuditCode.NO_TOPIC_INFORMATION` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

This is a configuration error and an exception is sent to the requester.

**User action**

Correct the configuration of the access service to include the name of the topic.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
