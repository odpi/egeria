<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# PostgreSQLAuditLogErrorCode

The PostgreSQLAuditLogErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the JDBC audit log destination connector. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 1 |
| **Message identifiers begin** | `JDBC-AUDIT-LOG-500-` |
| **Java class** | `org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.postgres.ffdc.PostgreSQLAuditLogErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/repository-services-connectors/audit-log-connectors/audit-log-postgres-connector](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/audit-log-connectors/audit-log-postgres-connector) |
| **Source** | [PostgreSQLAuditLogErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/audit-log-connectors/audit-log-postgres-connector/src/main/java/org/odpi/openmetadata/adapters/repositoryservices/auditlogstore/postgres/ffdc/PostgreSQLAuditLogErrorCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/audit-log-destination-connector/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [JDBC-AUDIT-LOG-500-001](#jdbc-audit-log-500-001) | 500 | The {0} audit log destination connector received an unexpected exception {1} during method {2}; the error message was: {3} |

----

### JDBC-AUDIT-LOG-500-001

> The {0} audit log destination connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `PostgreSQLAuditLogErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot store audit log records.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
