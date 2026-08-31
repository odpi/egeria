<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# Egeria Messages and Codes

Egeria practises *first failure data capture* (FFDC).  When something notable happens - whether it is a failure, a decision, or a step in a long-running process - the component involved raises a message that carries enough information to understand and act on the situation without having to reproduce it.

These pages document every message that Egeria can produce.  They are generated from the message set definitions in the Egeria source, so they always describe the messages of the release they are shipped with.


## The anatomy of a message

Every Egeria message is defined once, as a constant in a *message set*.  A message set is a java enum that implements one of the interfaces in the audit log framework's `org.odpi.openmetadata.frameworks.auditlog.messagesets` package.  Each message definition supplies the following fields.

| Field | Description |
|---|---|
| Message identifier | Uniquely identifies the message.  It is the value to search for in these pages when a message appears in a log or an error response.  The identifier never changes, even when the wording of the message is improved. |
| Message text | Describes what happened.  The `{0}`, `{1}` ... markers in the text show where the *message inserts* are placed.  The inserts carry the values - server names, element identifiers, exception messages - that are only known at the moment the message is raised. |
| System action | Describes what Egeria did as a result of the situation.  This is how to tell whether the request was abandoned, retried, or carried on regardless. |
| User action | Describes what the reader should do next.  For an informational message this is often "nothing"; for a failure it explains how to investigate and correct the problem. |
| Further reading | An optional link to the page on this site, or in the Egeria repository, that describes the component or concept the message is about.  Not every message has one.  Where a message does have a link, it travels with the message: it is written to the audit log record, and it is carried on the exception and in the error response of a REST API call. |
| Severity or HTTP error code | An audit log message carries a *severity* that says what kind of activity is being reported.  An exception message carries an *HTTP error code* so that the exception survives a REST API call. |


## Types of message

| Type | Message sets | Messages | Description |
|---|---|---|---|
| Exception messages | 66 | 615 | These messages are used to fill out the exceptions thrown by Egeria.  Each message carries an HTTP error code so that the exception can be faithfully passed across a REST API call and rebuilt by the client. |
| Audit log messages | 104 | 930 | These messages are written to the audit log destinations configured for the OMAG Server Platform.  Each message carries a severity that describes the type of activity being reported and is used to route the message to the appropriate audit log destinations. |
| Notification messages | 1 | 5 | These messages are the general purpose message sets.  They are used for message content that is neither an exception nor an audit log record - such as the notifications sent to a subscriber. |


## Audit log severities

The severity of an audit log message says what kind of activity is being reported.  It is used to decide which audit log destinations a message is written to, and it is the quickest way to pick the significant records out of a busy log.

| Severity | Shown in the log as | Description |
|---|---|---|
| `UNKNOWN` | Unknown | Uninitialized Severity. |
| `INFO` | Information | The server is providing information about its normal operation. |
| `EVENT` | Event | An event was sent to or received from another participant in the server's ecosystem. |
| `DECISION` | Decision | A decision has been made related to the operation of the system. |
| `ACTION` | Action | Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| `ERROR` | Error | An error occurred. This may restrict some of the server's operations. |
| `EXCEPTION` | Exception | An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| `SECURITY` | Security | Unauthorized access to a service or metadata instance has been attempted. |
| `STARTUP` | Startup | A new component is starting up. |
| `SHUTDOWN` | Shutdown | An existing component is shutting down. |
| `ASSET` | Asset | An auditable action relating to an asset has been taken. |
| `TYPES` | Types | Activity is occurring that relates to the open metadata types in use by this server. |
| `COHORT` | Cohort | The server is exchanging registration information about an open metadata repository cohort that it is connecting to. |
| `TRACE` | Trace | This is additional information on the operation of the server that may be of assistance in debugging a problem. It is not normally logged to any destination, but can be added when needed. |
| `PERFMON` | PerfMon | This log record contains performance monitoring timing information for specific types of processing. It is not normally logged to any destination but can be added when needed. |
| `ACTIVITY` | Activity | This log record contains user activity information such as the requests being made and the metadata being accessed. |


## HTTP error codes

An exception message carries an HTTP error code so that an exception raised deep inside a server can be returned over a REST API call and rebuilt as the equivalent exception in the client.  The code also indicates who is best placed to fix the problem: the 4xx codes point at the caller's request, the 5xx codes at the server.

| Code | Meaning |
|---|---|
| 400 | Bad Request - the caller has supplied invalid parameters |
| 401 | Unauthorized - the caller is not authenticated |
| 403 | Forbidden - the caller is not authorized to perform this request |
| 404 | Not Found - the requested element does not exist |
| 405 | Method Not Allowed - this operation is not supported for this element |
| 409 | Conflict - the request clashes with the current state of the metadata |
| 410 | Gone - the requested element has been deleted |
| 422 | Unprocessable Content - the request is understood but cannot be carried out |
| 500 | Internal Server Error - an unexpected error occurred inside Egeria |
| 501 | Not Implemented - this function is not implemented by the called component |
| 503 | Service Unavailable - the service needed to process the request is not running |


## Where the messages come from

The message sets are grouped to match the part of Egeria that defines them.

| Area | Message sets | Messages | Description |
|---|---|---|---|
| [Frameworks](frameworks) | 11 | 118 | The frameworks define the interfaces and base classes that connectors, governance services and clients are built on.  Their message sets are inherited by every component that builds on them, so these messages appear widely. |
| [Common Services](common-services) | 11 | 180 | The common services provide the shared function - such as parameter validation, metadata security and the generic metadata handlers - that the rest of the Egeria services call.  Their messages surface through whichever service is running at the time. |
| [Access Services](access-services) | 6 | 38 | The access services provide the domain-specific APIs and events that run in a metadata access server. |
| [Generic View Services](view-server-generic-services) | 15 | 75 | The generic view services provide the REST APIs used by user interfaces to work with any type of open metadata element. |
| [View Services](view-services) | 25 | 122 | The view services provide the REST APIs used by user interfaces such as Egeria UI.  Each view service supports a particular type of user or task. |
| [Engine Services](engine-services) | 8 | 59 | The engine services run the governance services of a particular governance service type in an Engine Host server. |
| [Governance Server Services](governance-server-services) | 4 | 85 | The governance server services host the connectors and governance services that run outside of a metadata access server - such as the integration daemon and the engine host. |
| [Repository Services](repository-services) | 2 | 315 | The Open Metadata Repository Services (OMRS) manage the exchange of metadata between the repositories of an open metadata repository cohort.  This is the oldest and largest set of messages in Egeria. |
| [Administration Services](admin-services) | 2 | 53 | The administration services configure and control the servers running on the OMAG Server Platform. |
| [Server Operations](server-operations) | 2 | 31 | The server operations services report on the servers that are running on an OMAG Server Platform. |
| [User Security](user-security) | 1 | 1 | The user security services authenticate the callers of the OMAG Server Platform's REST APIs. |
| [Data Manager Connectors](connectors/data-manager-connectors) | 12 | 72 | These connectors catalog and survey the contents of database servers and other data managers. |
| [Data Store Connectors](connectors/data-store-connectors) | 5 | 27 | These connectors provide access to the contents of files, folders and databases. |
| [Integration Connectors](connectors/integration-connectors) | 12 | 66 | Integration connectors run in an integration daemon.  They keep the open metadata ecosystem synchronized with the third party technologies that they monitor. |
| [System Connectors](connectors/system-connectors) | 13 | 73 | These connectors call the APIs of third party systems such as Apache Atlas, Apache Kafka and the Egeria runtime itself. |
| [Repository Services Connectors](connectors/repository-services-connectors) | 8 | 32 | These connectors provide the pluggable implementations used by the repository services - the metadata repositories, the audit log destinations, the cohort registry stores and the open metadata archive stores. |
| [Event Bus Connectors](connectors/event-bus-connectors) | 2 | 22 | These connectors send and receive events over the event bus - typically Apache Kafka. |
| [Governance Action Connectors](connectors/governance-action-connectors) | 2 | 43 | These governance services run in an engine host to make changes to the open metadata ecosystem and the resources it describes. |
| [File Survey Connectors](connectors/file-survey-connectors) | 1 | 5 | These survey action services analyse the content of files and folders and record what they find in a survey report. |
| [Nanny Connectors](connectors/nanny-connectors) | 16 | 78 | The nanny connectors harvest observability data from the open metadata ecosystem into a database so that the operation of Egeria itself can be analysed. |
| [Lovelace Insights](connectors/lovelace-insights) | 2 | 7 | These connectors analyse the harvested observability data and turn it into insight reports. |
| [Report Generating Connectors](connectors/report-generating-connectors) | 1 | 2 | These connectors turn the contents of the open metadata ecosystem into human-readable documents. |
| [Secrets Store Connectors](connectors/secrets-store-connectors) | 2 | 5 | These connectors supply the credentials that other connectors need when they call a third party technology. |
| [Metadata Security Connectors](connectors/metadata-security-connectors) | 1 | 1 | These connectors implement an organization's authorization rules for the OMAG Server Platform and its servers. |
| [Configuration Store Connectors](connectors/configuration-store-connectors) | 1 | 2 | These connectors store and retrieve the configuration documents of the servers running on an OMAG Server Platform. |
| [REST Client Connectors](connectors/rest-client-connectors) | 1 | 2 | These connectors issue the REST API calls that Egeria's clients make to a remote OMAG Server Platform. |
| [Other Connectors](connectors/other-connectors) | 1 | 2 | The remaining connectors shipped with Egeria. |
| [Conformance Suite](conformance-suite) | 2 | 25 | The conformance suite tests whether a technology conforms to the open metadata specifications. |
| [Samples](samples) | 2 | 9 | The sample connectors and governance services that are shipped with Egeria to illustrate how the interfaces are used. |


## Finding a message identifier

Every message identifier begins with a prefix that names the component that raised it.  Find the prefix in the table below to reach the page that documents the message.

| Message identifiers | Type | Messages | Message set |
|---|---|---|---|
| `APACHE-ATLAS-INTEGRATION-CONNECTOR-` | Audit log messages | 36 | [AtlasIntegrationAuditCode](connectors/system-connectors/AtlasIntegrationAuditCode.md) |
| `APACHE-ATLAS-INTEGRATION-CONNECTOR-` | Exception messages | 5 | [AtlasIntegrationErrorCode](connectors/system-connectors/AtlasIntegrationErrorCode.md) |
| `APACHE-ATLAS-REST-CONNECTOR-` | Audit log messages | 2 | [ApacheAtlasAuditCode](connectors/system-connectors/ApacheAtlasAuditCode.md) |
| `APACHE-ATLAS-REST-CONNECTOR-` | Exception messages | 6 | [ApacheAtlasErrorCode](connectors/system-connectors/ApacheAtlasErrorCode.md) |
| `APACHE-ATLAS-SURVEY-ACTION-CONNECTOR-` | Audit log messages | 2 | [AtlasSurveyAuditCode](connectors/system-connectors/AtlasSurveyAuditCode.md) |
| `APACHE-ATLAS-SURVEY-ACTION-CONNECTOR-` | Exception messages | 3 | [AtlasSurveyErrorCode](connectors/system-connectors/AtlasSurveyErrorCode.md) |
| `APACHE-KAFKA-INTEGRATION-CONNECTOR-` | Audit log messages | 5 | [KafkaIntegrationConnectorAuditCode](connectors/system-connectors/KafkaIntegrationConnectorAuditCode.md) |
| `APACHE-KAFKA-INTEGRATION-CONNECTOR-400-` | Exception messages | 1 | [KafkaIntegrationConnectorErrorCode](connectors/system-connectors/KafkaIntegrationConnectorErrorCode.md) |
| `APACHE-KAFKA-REST-CONNECTOR-` | Audit log messages | 1 | [ApacheKafkaAuditCode](connectors/system-connectors/ApacheKafkaAuditCode.md) |
| `APACHE-KAFKA-REST-CONNECTOR-` | Exception messages | 2 | [ApacheKafkaErrorCode](connectors/system-connectors/ApacheKafkaErrorCode.md) |
| `APACHE-KAFKA-SURVEY-ACTION-CONNECTOR-500-` | Exception messages | 1 | [KafkaSurveyErrorCode](connectors/system-connectors/KafkaSurveyErrorCode.md) |
| `BABBAGE-ANALYTICAL-ENGINE-` | Audit log messages | 4 | [BabbageAuditCode](connectors/nanny-connectors/BabbageAuditCode.md) |
| `BABBAGE-ANALYTICAL-ENGINE-500-` | Exception messages | 1 | [BabbageErrorCode](connectors/nanny-connectors/BabbageErrorCode.md) |
| `BASIC-FILE-CONNECTOR-` | Exception messages | 10 | [BasicFileConnectorErrorCode](connectors/data-store-connectors/BasicFileConnectorErrorCode.md) |
| `BASIC-FILES-INTEGRATION-CONNECTORS-` | Audit log messages | 19 | [BasicFilesIntegrationConnectorsAuditCode](connectors/integration-connectors/BasicFilesIntegrationConnectorsAuditCode.md) |
| `BASIC-FILES-INTEGRATION-CONNECTORS-` | Exception messages | 7 | [BasicFilesIntegrationConnectorsErrorCode](connectors/integration-connectors/BasicFilesIntegrationConnectorsErrorCode.md) |
| `BAUDOT-SUBSCRIPTION-MANAGEMENT-` | Notification messages | 5 | [BaudotNotificationMessageSet](connectors/nanny-connectors/BaudotNotificationMessageSet.md) |
| `BAUDOT-SUBSCRIPTION-MANAGER-` | Audit log messages | 6 | [BaudotAuditCode](connectors/nanny-connectors/BaudotAuditCode.md) |
| `CLIENT-SIDE-REST-API-CONNECTOR-503-` | Exception messages | 2 | [RESTClientConnectorErrorCode](connectors/rest-client-connectors/RESTClientConnectorErrorCode.md) |
| `CONFORMANCE-SUITE-` | Audit log messages | 15 | [ConformanceSuiteAuditCode](conformance-suite/ConformanceSuiteAuditCode.md) |
| `CONFORMANCE-SUITE-` | Exception messages | 10 | [ConformanceSuiteErrorCode](conformance-suite/ConformanceSuiteErrorCode.md) |
| `CONNECTED-ASSET-SERVICES-` | Audit log messages | 4 | [OCFServicesAuditCode](access-services/OCFServicesAuditCode.md) |
| `CONNECTED-ASSET-SERVICES-` | Exception messages | 2 | [OCFServicesErrorCode](access-services/OCFServicesErrorCode.md) |
| `CONNECTOR-CONFIGURATION-FACTORY-400-` | Exception messages | 2 | [ConnectorConfigurationFactoryErrorCode](connectors/other-connectors/ConnectorConfigurationFactoryErrorCode.md) |
| `CSV-FILE-CONNECTOR-` | Audit log messages | 1 | [CSVFileConnectorAuditCode](connectors/data-store-connectors/CSVFileConnectorAuditCode.md) |
| `CSV-FILE-CONNECTOR-` | Exception messages | 5 | [CSVFileConnectorErrorCode](connectors/data-store-connectors/CSVFileConnectorErrorCode.md) |
| `CSV-LINEAGE-IMPORTER-` | Audit log messages | 1 | [CSVLineageImporterAuditCode](connectors/integration-connectors/CSVLineageImporterAuditCode.md) |
| `CSV-LINEAGE-IMPORTER-400-` | Exception messages | 1 | [CSVLineageImporterErrorCode](connectors/integration-connectors/CSVLineageImporterErrorCode.md) |
| `DB2LUW-CONNECTOR-` | Audit log messages | 6 | [DB2LUWAuditCode](connectors/data-manager-connectors/DB2LUWAuditCode.md) |
| `DB2LUW-CONNECTOR-` | Exception messages | 2 | [DB2LUWErrorCode](connectors/data-manager-connectors/DB2LUWErrorCode.md) |
| `DISTRIBUTE-KAFKA-AUDIT-LOG-` | Audit log messages | 1 | [DistributeKafkaAuditCode](connectors/integration-connectors/DistributeKafkaAuditCode.md) |
| `DUCKDB-CONNECTOR-` | Audit log messages | 14 | [DuckDBAuditCode](connectors/data-manager-connectors/DuckDBAuditCode.md) |
| `DUCKDB-CONNECTOR-500-` | Exception messages | 1 | [DuckDBErrorCode](connectors/data-manager-connectors/DuckDBErrorCode.md) |
| `ENGINE-HOST-SERVICES-` | Audit log messages | 28 | [EngineHostServicesAuditCode](governance-server-services/EngineHostServicesAuditCode.md) |
| `ENGINE-HOST-SERVICES-400-` | Exception messages | 15 | [EngineHostServicesErrorCode](governance-server-services/EngineHostServicesErrorCode.md) |
| `FILE-DOC-STORE-400-` | Exception messages | 2 | [DocStoreErrorCode](connectors/configuration-store-connectors/DocStoreErrorCode.md) |
| `FILE-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-` | Audit log messages | 2 | [FileBasedOpenMetadataArchiveStoreConnectorAuditCode](connectors/repository-services-connectors/FileBasedOpenMetadataArchiveStoreConnectorAuditCode.md) |
| `FILE-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-400-` | Exception messages | 1 | [FileBasedOpenMetadataArchiveStoreConnectorErrorCode](connectors/repository-services-connectors/FileBasedOpenMetadataArchiveStoreConnectorErrorCode.md) |
| `GOVERNANCE-ACTION-CONNECTORS-` | Audit log messages | 39 | [GovernanceActionConnectorsAuditCode](connectors/governance-action-connectors/GovernanceActionConnectorsAuditCode.md) |
| `GOVERNANCE-ACTION-CONNECTORS-` | Exception messages | 4 | [GovernanceActionConnectorsErrorCode](connectors/governance-action-connectors/GovernanceActionConnectorsErrorCode.md) |
| `GOVERNANCE-ACTION-SAMPLES-` | Audit log messages | 5 | [GovernanceActionSamplesAuditCode](samples/GovernanceActionSamplesAuditCode.md) |
| `GOVERNANCE-ACTION-SAMPLES-` | Exception messages | 4 | [GovernanceActionSamplesErrorCode](samples/GovernanceActionSamplesErrorCode.md) |
| `HARVEST-OPEN-METADATA-` | Audit log messages | 1 | [HarvestOpenMetadataAuditCode](connectors/nanny-connectors/HarvestOpenMetadataAuditCode.md) |
| `HARVEST-OPEN-METADATA-500-` | Exception messages | 1 | [HarvestOpenMetadataErrorCode](connectors/nanny-connectors/HarvestOpenMetadataErrorCode.md) |
| `HARVEST-SURVEYS-` | Audit log messages | 1 | [HarvestSurveysAuditCode](connectors/nanny-connectors/HarvestSurveysAuditCode.md) |
| `HARVEST-SURVEYS-500-` | Exception messages | 1 | [HarvestSurveysErrorCode](connectors/nanny-connectors/HarvestSurveysErrorCode.md) |
| `INTEGRATION-DAEMON-SERVICES-` | Audit log messages | 34 | [IntegrationDaemonServicesAuditCode](governance-server-services/IntegrationDaemonServicesAuditCode.md) |
| `INTEGRATION-DAEMON-SERVICES-400-` | Exception messages | 8 | [IntegrationDaemonServicesErrorCode](governance-server-services/IntegrationDaemonServicesErrorCode.md) |
| `JACQUARD-HARVESTER-` | Audit log messages | 18 | [JacquardAuditCode](connectors/nanny-connectors/JacquardAuditCode.md) |
| `JACQUARD-HARVESTER-` | Exception messages | 2 | [JacquardErrorCode](connectors/nanny-connectors/JacquardErrorCode.md) |
| `JDBC-AUDIT-LOG-500-` | Exception messages | 1 | [PostgreSQLAuditLogErrorCode](connectors/repository-services-connectors/PostgreSQLAuditLogErrorCode.md) |
| `JDBC-INTEGRATION-CONNECTOR-` | Audit log messages | 11 | [JDBCIntegrationConnectorAuditCode](connectors/integration-connectors/JDBCIntegrationConnectorAuditCode.md) |
| `JDBC-RESOURCE-CONNECTOR-` | Audit log messages | 4 | [JDBCAuditCode](connectors/data-store-connectors/JDBCAuditCode.md) |
| `JDBC-RESOURCE-CONNECTOR-` | Exception messages | 7 | [JDBCErrorCode](connectors/data-store-connectors/JDBCErrorCode.md) |
| `LISKOV-DATA-HUB-MANAGER-` | Audit log messages | 10 | [LiskovAuditCode](connectors/nanny-connectors/LiskovAuditCode.md) |
| `LISKOV-DATA-HUB-MANAGER-500-` | Exception messages | 1 | [LiskovErrorCode](connectors/nanny-connectors/LiskovErrorCode.md) |
| `LOVELACE-INSIGHTS-` | Audit log messages | 5 | [LovelaceInsightAuditCode](connectors/lovelace-insights/LovelaceInsightAuditCode.md) |
| `LOVELACE-INSIGHTS-500-` | Exception messages | 2 | [LovelaceInsightErrorCode](connectors/lovelace-insights/LovelaceInsightErrorCode.md) |
| `MENDEL-DUPLICATE-MANAGER-` | Audit log messages | 18 | [MendelAuditCode](connectors/nanny-connectors/MendelAuditCode.md) |
| `MENDEL-DUPLICATE-MANAGER-500-` | Exception messages | 2 | [MendelErrorCode](connectors/nanny-connectors/MendelErrorCode.md) |
| `METADATA-OBSERVABILITY-` | Audit log messages | 10 | [OpenMetadataObservabilityAuditCode](common-services/OpenMetadataObservabilityAuditCode.md) |
| `MSSQL-CONNECTOR-` | Audit log messages | 6 | [MSSQLAuditCode](connectors/data-manager-connectors/MSSQLAuditCode.md) |
| `MSSQL-CONNECTOR-` | Exception messages | 2 | [MSSQLErrorCode](connectors/data-manager-connectors/MSSQLErrorCode.md) |
| `O-` | Audit log messages | 9 | [OpenGovernanceAuditCode](access-services/OpenGovernanceAuditCode.md) |
| `O-` | Exception messages | 20 | [OpenMetadataSecurityErrorCode](common-services/OpenMetadataSecurityErrorCode.md) |
| `OCF-` | Exception messages | 26 | [OCFErrorCode](frameworks/OCFErrorCode.md) |
| `OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-` | Audit log messages | 1 | [DirectoryBasedOpenMetadataArchiveStoreConnectorAuditCode](connectors/repository-services-connectors/DirectoryBasedOpenMetadataArchiveStoreConnectorAuditCode.md) |
| `OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-400-` | Exception messages | 2 | [DirectoryBasedOpenMetadataArchiveStoreConnectorErrorCode](connectors/repository-services-connectors/DirectoryBasedOpenMetadataArchiveStoreConnectorErrorCode.md) |
| `OCF-FILE-REGISTRY-STORE-CONNECTOR-` | Audit log messages | 10 | [FileBasedRegistryStoreConnectorAuditCode](connectors/repository-services-connectors/FileBasedRegistryStoreConnectorAuditCode.md) |
| `OCF-KAFKA-TOPIC-CONNECTOR-` | Audit log messages | 19 | [KafkaOpenMetadataTopicConnectorAuditCode](connectors/event-bus-connectors/KafkaOpenMetadataTopicConnectorAuditCode.md) |
| `OCF-KAFKA-TOPIC-CONNECTOR-400-` | Exception messages | 3 | [KafkaOpenMetadataTopicConnectorErrorCode](connectors/event-bus-connectors/KafkaOpenMetadataTopicConnectorErrorCode.md) |
| `OIF-CONNECTOR-` | Audit log messages | 16 | [OIFAuditCode](frameworks/OIFAuditCode.md) |
| `OIF-CONNECTOR-` | Exception messages | 7 | [OIFErrorCode](frameworks/OIFErrorCode.md) |
| `OMAG-ADMIN-` | Audit log messages | 8 | [OMAGAdminAuditCode](admin-services/OMAGAdminAuditCode.md) |
| `OMAG-ADMIN-` | Exception messages | 45 | [OMAGAdminErrorCode](admin-services/OMAGAdminErrorCode.md) |
| `OMAG-COMMON-` | Audit log messages | 2 | [OMAGCommonAuditCode](common-services/OMAGCommonAuditCode.md) |
| `OMAG-COMMON-` | Exception messages | 32 | [OMAGCommonErrorCode](common-services/OMAGCommonErrorCode.md) |
| `OMAG-CONNECTORS-` | Audit log messages | 6 | [OMAGConnectorAuditCode](connectors/system-connectors/OMAGConnectorAuditCode.md) |
| `OMAG-CONNECTORS-` | Exception messages | 3 | [OMAGConnectorErrorCode](connectors/system-connectors/OMAGConnectorErrorCode.md) |
| `OMAG-GENERIC-HANDLERS-` | Audit log messages | 15 | [GenericHandlersAuditCode](common-services/GenericHandlersAuditCode.md) |
| `OMAG-GENERIC-HANDLERS-` | Exception messages | 25 | [GenericHandlersErrorCode](common-services/GenericHandlersErrorCode.md) |
| `OMAG-MULTI-TENANT-` | Audit log messages | 2 | [OMAGServerInstanceAuditCode](common-services/OMAGServerInstanceAuditCode.md) |
| `OMAG-MULTI-TENANT-` | Exception messages | 12 | [OMAGServerInstanceErrorCode](common-services/OMAGServerInstanceErrorCode.md) |
| `OMAG-REPOSITORY-HANDLER-` | Audit log messages | 10 | [RepositoryHandlerAuditCode](common-services/RepositoryHandlerAuditCode.md) |
| `OMAG-REPOSITORY-HANDLER-` | Exception messages | 26 | [RepositoryHandlerErrorCode](common-services/RepositoryHandlerErrorCode.md) |
| `OMES-GOVERNANCE-ACTION-` | Audit log messages | 14 | [GovernanceActionAuditCode](engine-services/GovernanceActionAuditCode.md) |
| `OMES-GOVERNANCE-ACTION-400-` | Exception messages | 4 | [GovernanceActionErrorCode](engine-services/GovernanceActionErrorCode.md) |
| `OMES-REPOSITORY-GOVERNANCE-` | Audit log messages | 10 | [RepositoryGovernanceAuditCode](engine-services/RepositoryGovernanceAuditCode.md) |
| `OMES-REPOSITORY-GOVERNANCE-` | Exception messages | 4 | [RepositoryGovernanceErrorCode](engine-services/RepositoryGovernanceErrorCode.md) |
| `OMES-SURVEY-ACTION-` | Audit log messages | 9 | [SurveyActionAuditCode](engine-services/SurveyActionAuditCode.md) |
| `OMES-SURVEY-ACTION-400-` | Exception messages | 4 | [SurveyActionErrorCode](engine-services/SurveyActionErrorCode.md) |
| `OMES-WATCHDOG-ACTION-` | Audit log messages | 11 | [WatchdogActionAuditCode](engine-services/WatchdogActionAuditCode.md) |
| `OMES-WATCHDOG-ACTION-400-` | Exception messages | 3 | [WatchdogActionErrorCode](engine-services/WatchdogActionErrorCode.md) |
| `OMF-SERVICES-` | Audit log messages | 11 | [OMFServicesAuditCode](access-services/OMFServicesAuditCode.md) |
| `OMF-SERVICES-` | Exception messages | 7 | [OMFServicesErrorCode](access-services/OMFServicesErrorCode.md) |
| `OMRS-` | Exception messages | 188 | [OMRSErrorCode](repository-services/OMRSErrorCode.md) |
| `OMRS-AUDIT-` | Audit log messages | 127 | [OMRSAuditCode](repository-services/OMRSAuditCode.md) |
| `OMVS-ACTION-AUTHOR-` | Audit log messages | 5 | [ActionAuthorAuditCode](view-services/ActionAuthorAuditCode.md) |
| `OMVS-ACTOR-MANAGER-` | Audit log messages | 5 | [ActorManagerAuditCode](view-server-generic-services/ActorManagerAuditCode.md) |
| `OMVS-ASSET-CATALOG-` | Audit log messages | 5 | [AssetCatalogAuditCode](view-services/AssetCatalogAuditCode.md) |
| `OMVS-ASSET-MAKER-` | Audit log messages | 5 | [AssetMakerAuditCode](view-server-generic-services/AssetMakerAuditCode.md) |
| `OMVS-AUTOMATED-CURATION-` | Audit log messages | 5 | [AutomatedCurationAuditCode](view-server-generic-services/AutomatedCurationAuditCode.md) |
| `OMVS-CLASSIFICATION-EXPLORER-` | Audit log messages | 5 | [ClassificationExplorerAuditCode](view-server-generic-services/ClassificationExplorerAuditCode.md) |
| `OMVS-COLLECTION-MANAGER-` | Audit log messages | 5 | [CollectionManagerAuditCode](view-server-generic-services/CollectionManagerAuditCode.md) |
| `OMVS-COMMUNITY-MATTERS-` | Audit log messages | 5 | [CommunityMattersAuditCode](view-services/CommunityMattersAuditCode.md) |
| `OMVS-CONNECTION-MAKER-` | Audit log messages | 5 | [ConnectionMakerAuditCode](view-server-generic-services/ConnectionMakerAuditCode.md) |
| `OMVS-DATA-DESIGNER-` | Audit log messages | 5 | [DataDesignerAuditCode](view-services/DataDesignerAuditCode.md) |
| `OMVS-DATA-DISCOVERY-` | Audit log messages | 5 | [DataDiscoveryAuditCode](view-services/DataDiscoveryAuditCode.md) |
| `OMVS-DATA-ENGINEER-` | Audit log messages | 5 | [DataEngineerAuditCode](view-services/DataEngineerAuditCode.md) |
| `OMVS-DATA-OFFICER-` | Audit log messages | 5 | [DataOfficerAuditCode](view-services/DataOfficerAuditCode.md) |
| `OMVS-DEVOPS-PIPELINE-` | Audit log messages | 5 | [DevopsPipelineAuditCode](view-services/DevopsPipelineAuditCode.md) |
| `OMVS-DIGITAL-BUSINESS-` | Audit log messages | 5 | [DigitalBusinessAuditCode](view-services/DigitalBusinessAuditCode.md) |
| `OMVS-EXTERNAL-LINKS-` | Audit log messages | 5 | [ExternalLinksAuditCode](view-server-generic-services/ExternalLinksAuditCode.md) |
| `OMVS-FEEDBACK-MANAGER-` | Audit log messages | 5 | [FeedbackManagerAuditCode](view-server-generic-services/FeedbackManagerAuditCode.md) |
| `OMVS-GLOSSARY-MANAGER-` | Audit log messages | 5 | [GlossaryManagerAuditCode](view-services/GlossaryManagerAuditCode.md) |
| `OMVS-GOVERNANCE-OFFICER-` | Audit log messages | 5 | [GovernanceOfficerAuditCode](view-server-generic-services/GovernanceOfficerAuditCode.md) |
| `OMVS-LINEAGE-LINKER-` | Audit log messages | 5 | [LineageLinkerAuditCode](view-server-generic-services/LineageLinkerAuditCode.md) |
| `OMVS-LOCATION-ARENA-` | Audit log messages | 5 | [LocationArenaAuditCode](view-services/LocationArenaAuditCode.md) |
| `OMVS-METADATA-EXPERT-` | Audit log messages | 5 | [MetadataExpertAuditCode](view-server-generic-services/MetadataExpertAuditCode.md) |
| `OMVS-MULTI-LANGUAGE-` | Audit log messages | 5 | [MultiLanguageAuditCode](view-server-generic-services/MultiLanguageAuditCode.md) |
| `OMVS-MY-PROFILE-` | Audit log messages | 5 | [MyProfileAuditCode](view-services/MyProfileAuditCode.md) |
| `OMVS-MY-PROFILE-400-` | Exception messages | 2 | [MyProfileErrorCode](view-services/MyProfileErrorCode.md) |
| `OMVS-NOTIFICATION-MANAGER-` | Audit log messages | 5 | [NotificationManagerAuditCode](view-services/NotificationManagerAuditCode.md) |
| `OMVS-PEOPLE-ORGANIZER-` | Audit log messages | 5 | [PeopleOrganizerAuditCode](view-services/PeopleOrganizerAuditCode.md) |
| `OMVS-PRIVACY-OFFICER-` | Audit log messages | 5 | [PrivacyOfficerAuditCode](view-services/PrivacyOfficerAuditCode.md) |
| `OMVS-PRODUCT-CATALOG-` | Audit log messages | 5 | [ProductCatalogAuditCode](view-services/ProductCatalogAuditCode.md) |
| `OMVS-PRODUCT-MANAGER-` | Audit log messages | 5 | [ProductManagerAuditCode](view-services/ProductManagerAuditCode.md) |
| `OMVS-PROJECT-MANAGER-` | Audit log messages | 5 | [ProjectManagerAuditCode](view-services/ProjectManagerAuditCode.md) |
| `OMVS-REFERENCE-DATA-` | Audit log messages | 5 | [ReferenceDataAuditCode](view-services/ReferenceDataAuditCode.md) |
| `OMVS-RUNTIME-MANAGER-` | Audit log messages | 5 | [RuntimeManagerAuditCode](view-services/RuntimeManagerAuditCode.md) |
| `OMVS-SCHEMA-MAKER-` | Audit log messages | 5 | [SchemaMakerAuditCode](view-server-generic-services/SchemaMakerAuditCode.md) |
| `OMVS-SECURITY-OFFICER-` | Audit log messages | 5 | [SecurityOfficerAuditCode](view-services/SecurityOfficerAuditCode.md) |
| `OMVS-SOLUTION-ARCHITECT-` | Audit log messages | 5 | [SolutionArchitectAuditCode](view-services/SolutionArchitectAuditCode.md) |
| `OMVS-SUBJECT-AREA-` | Audit log messages | 5 | [SubjectAreaAuditCode](view-services/SubjectAreaAuditCode.md) |
| `OMVS-TEMPLATE-MANAGER-` | Audit log messages | 5 | [TemplateManagerAuditCode](view-services/TemplateManagerAuditCode.md) |
| `OMVS-TIME-KEEPER-` | Audit log messages | 5 | [TimeKeeperAuditCode](view-server-generic-services/TimeKeeperAuditCode.md) |
| `OMVS-VALID-METADATA-` | Audit log messages | 5 | [ValidMetadataAuditCode](view-server-generic-services/ValidMetadataAuditCode.md) |
| `OPEN-API-INTEGRATION-CONNECTOR-` | Audit log messages | 14 | [OpenAPIIntegrationConnectorAuditCode](connectors/integration-connectors/OpenAPIIntegrationConnectorAuditCode.md) |
| `OPEN-API-INTEGRATION-CONNECTOR-` | Exception messages | 2 | [OpenAPIIntegrationConnectorErrorCode](connectors/integration-connectors/OpenAPIIntegrationConnectorErrorCode.md) |
| `OPEN-GOVERNANCE-` | Exception messages | 5 | [OpenGovernanceErrorCode](access-services/OpenGovernanceErrorCode.md) |
| `OPEN-GOVERNANCE-ACTION-` | Audit log messages | 3 | [OGFAuditCode](frameworks/OGFAuditCode.md) |
| `OPEN-GOVERNANCE-ACTION-` | Exception messages | 2 | [OGFErrorCode](frameworks/OGFErrorCode.md) |
| `OPEN-LINEAGE-INTEGRATION-CONNECTOR-` | Audit log messages | 2 | [OpenLineageIntegrationConnectorAuditCode](connectors/integration-connectors/OpenLineageIntegrationConnectorAuditCode.md) |
| `OPEN-LINEAGE-INTEGRATION-CONNECTOR-500-` | Exception messages | 2 | [OpenLineageIntegrationConnectorErrorCode](connectors/integration-connectors/OpenLineageIntegrationConnectorErrorCode.md) |
| `OPEN-METADATA-` | Audit log messages | 12 | [OMFAuditCode](frameworks/OMFAuditCode.md) |
| `OPEN-METADATA-` | Exception messages | 31 | [OMFErrorCode](frameworks/OMFErrorCode.md) |
| `OPEN-METADATA-ACCESS-SECURITY-` | Audit log messages | 1 | [MetadataSecurityAuditCode](connectors/metadata-security-connectors/MetadataSecurityAuditCode.md) |
| `OPEN-METADATA-SECURITY-` | Audit log messages | 26 | [OpenMetadataSecurityAuditCode](common-services/OpenMetadataSecurityAuditCode.md) |
| `OPEN-SURVEY-` | Audit log messages | 8 | [OSFAuditCode](frameworks/OSFAuditCode.md) |
| `OPEN-SURVEY-` | Exception messages | 8 | [OSFErrorCode](frameworks/OSFErrorCode.md) |
| `OPEN-WATCHDOG-` | Exception messages | 2 | [OWFErrorCode](frameworks/OWFErrorCode.md) |
| `OPEN-WATCHDOG-ACTION-` | Audit log messages | 3 | [OWFAuditCode](frameworks/OWFAuditCode.md) |
| `ORACLE-CONNECTOR-` | Audit log messages | 6 | [OracleAuditCode](connectors/data-manager-connectors/OracleAuditCode.md) |
| `ORACLE-CONNECTOR-` | Exception messages | 2 | [OracleErrorCode](connectors/data-manager-connectors/OracleErrorCode.md) |
| `POSTGRES-CONNECTOR-` | Audit log messages | 6 | [PostgresAuditCode](connectors/data-manager-connectors/PostgresAuditCode.md) |
| `POSTGRES-CONNECTOR-` | Exception messages | 2 | [PostgresErrorCode](connectors/data-manager-connectors/PostgresErrorCode.md) |
| `POSTGRES-REPOSITORY-CONNECTOR-` | Audit log messages | 7 | [PostgresAuditCode](connectors/repository-services-connectors/PostgresAuditCode.md) |
| `POSTGRES-REPOSITORY-CONNECTOR-` | Exception messages | 8 | [PostgresErrorCode](connectors/repository-services-connectors/PostgresErrorCode.md) |
| `REFERENCE-DATA-CONNECTORS-` | Exception messages | 6 | [TabularDataErrorCode](connectors/nanny-connectors/TabularDataErrorCode.md) |
| `REPORT-GENERATORS-` | Audit log messages | 2 | [ReportsAuditCode](connectors/report-generating-connectors/ReportsAuditCode.md) |
| `SERVER-OPS-` | Audit log messages | 24 | [ServerOpsAuditCode](server-operations/ServerOpsAuditCode.md) |
| `SERVER-OPS-` | Exception messages | 7 | [ServerOpsErrorCode](server-operations/ServerOpsErrorCode.md) |
| `SMART-COLLECTIONS-INTEGRATION-CONNECTOR-` | Audit log messages | 4 | [SmartCollectionsAuditCode](connectors/integration-connectors/SmartCollectionsAuditCode.md) |
| `SMART-COLLECTIONS-INTEGRATION-CONNECTOR-` | Exception messages | 2 | [SmartCollectionsErrorCode](connectors/integration-connectors/SmartCollectionsErrorCode.md) |
| `SURVEY-ACTION-SERVICE-` | Audit log messages | 5 | [SurveyServiceAuditCode](connectors/file-survey-connectors/SurveyServiceAuditCode.md) |
| `TABULAR-METADATA-CONNECTORS-` | Audit log messages | 1 | [TabularDataAuditCode](connectors/nanny-connectors/TabularDataAuditCode.md) |
| `TOKEN-CONTROLLER-401-` | Exception messages | 1 | [TokenControllerErrorCode](user-security/TokenControllerErrorCode.md) |
| `UNITY-CATALOG-CONNECTOR-` | Audit log messages | 18 | [UCAuditCode](connectors/data-manager-connectors/UCAuditCode.md) |
| `UNITY-CATALOG-CONNECTOR-` | Exception messages | 7 | [UCErrorCode](connectors/data-manager-connectors/UCErrorCode.md) |
| `YAML-SECRETS-STORE-CONNECTOR-` | Audit log messages | 4 | [YAMLAuditCode](connectors/secrets-store-connectors/YAMLAuditCode.md) |
| `YAML-SECRETS-STORE-CONNECTOR-500-` | Exception messages | 1 | [YAMLErrorCode](connectors/secrets-store-connectors/YAMLErrorCode.md) |


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
