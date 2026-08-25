<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# Repository Services Connectors Messages

These connectors provide the pluggable implementations used by the repository services - the metadata repositories, the audit log destinations, the cohort registry stores and the open metadata archive stores.

This directory documents 30 messages in 8 message sets.  Return to the [messages and codes index](../../README.md).


## Message sets

| Message set | Type | Message identifiers | Messages | Further reading |
|---|---|---|---|---|
| [DirectoryBasedOpenMetadataArchiveStoreConnectorAuditCode](DirectoryBasedOpenMetadataArchiveStoreConnectorAuditCode.md) | Audit log messages | `OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-` | 1 | <https://egeria-project.org/concepts/open-metadata-archive-store-connector/> |
| [DirectoryBasedOpenMetadataArchiveStoreConnectorErrorCode](DirectoryBasedOpenMetadataArchiveStoreConnectorErrorCode.md) | Exception messages | `OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-400-` | 2 | <https://egeria-project.org/concepts/open-metadata-archive-store-connector/> |
| [FileBasedOpenMetadataArchiveStoreConnectorAuditCode](FileBasedOpenMetadataArchiveStoreConnectorAuditCode.md) | Audit log messages | `FILE-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-` | 2 | <https://egeria-project.org/concepts/open-metadata-archive-store-connector/> |
| [FileBasedOpenMetadataArchiveStoreConnectorErrorCode](FileBasedOpenMetadataArchiveStoreConnectorErrorCode.md) | Exception messages | `FILE-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-400-` | 1 | <https://egeria-project.org/concepts/open-metadata-archive-store-connector/> |
| [FileBasedRegistryStoreConnectorAuditCode](FileBasedRegistryStoreConnectorAuditCode.md) | Audit log messages | `OCF-FILE-REGISTRY-STORE-CONNECTOR-` | 10 | <https://egeria-project.org/concepts/cohort-registry-store-connector/> |
| [PostgreSQLAuditLogErrorCode](PostgreSQLAuditLogErrorCode.md) | Exception messages | `JDBC-AUDIT-LOG-500-` | 1 | <https://egeria-project.org/concepts/audit-log-destination-connector/> |
| [PostgresAuditCode](PostgresAuditCode.md) | Audit log messages | `POSTGRES-REPOSITORY-CONNECTOR-` | 5 | <https://egeria-project.org/concepts/repository-connector/> |
| [PostgresErrorCode](PostgresErrorCode.md) | Exception messages | `POSTGRES-REPOSITORY-CONNECTOR-` | 8 | <https://egeria-project.org/concepts/repository-connector/> |


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
