/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors;

import org.odpi.openmetadata.adapters.connectors.controls.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;


/**
 * Enumeration of the different types of connectors provided by the Egeria community.  This module also
 * provides the connector provider base class that performs the setup for a connector provider.
 */
public enum EgeriaOpenConnectorDefinition implements OpenConnectorDefinition
{
    FILE_BASED_CONFIG_STORE(30,
                            "39276d19-be00-4fdc-84cb-a21438fa4ad0",
                            "Egeria::RuntimeConnector::ConfigStore::File",
                            "File Based Server Config Store Connector",
                            "Connector supports storing of OMAG Server configuration document in a file.",
                            "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/configuration-store-connectors/configuration-file-store-connector",
                            "org.odpi.openmetadata.adapters.adminservices.configurationstore.file.FileBasedServerConfigStoreProvider",
                            ComponentDevelopmentStatus.STABLE,
                            DeployedImplementationType.CONFIG_DOCUMENT_STORE_CONNECTOR.getAssociatedTypeName(),
                            DeployedImplementationType.CONFIG_DOCUMENT_STORE_CONNECTOR.getDeployedImplementationType()),

    IN_MEMORY_TOPIC_CONNECTOR(31,
                              "ed8e682b-2fec-4403-b551-02f8c46322ef",
                              "Egeria::RuntimeConnector::OpenMetadataTopicConnector::InMemory",
                              "In Memory Open Metadata Topic Connector",
                              "Connector supports publishing and subscribing to string based events using an in-memory topic.",
                              "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/event-bus-connectors/open-metadata-topic-connectors/inmemory-open-metadata-topic-connector",
                              "org.odpi.openmetadata.adapters.eventbus.topic.inmemory.InMemoryOpenMetadataTopicProvider",
                              ComponentDevelopmentStatus.STABLE,
                              DeployedImplementationType.OPEN_METADATA_TOPIC_CONNECTOR.getAssociatedTypeName(),
                              DeployedImplementationType.OPEN_METADATA_TOPIC_CONNECTOR.getDeployedImplementationType()),

    SPRING_REST_API_CONNECTOR(32,
                              "434cd526-ecea-4e14-b8f5-97c2c1d44fa",
                              "Egeria::ResourceConnector::SpringRESTAPI",
                              "Spring REST API Client Connector",
                              "Connector that calls the REST API of a remote server using Spring.",
                              "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/rest-client-connectors/spring-rest-client-connector",
                              "org.odpi.openmetadata.adapters.connectors.restclients.spring.SpringRESTClientConnectorProvider",
                              ComponentDevelopmentStatus.STABLE,
                              DeployedImplementationType.REST_API.getAssociatedTypeName(),
                              DeployedImplementationType.REST_API.getDeployedImplementationType()),

    JSON_FILE_COHORT_REGISTRY_STORE(33,
                                    "108b85fe-d7a8-45c3-9f88-742ac4e4fd14",
                                    "Egeria::CohortRegistryConnector::File",
                                    "File-based Cohort Registry Store Connector",
                                    "Connector supports storing of the open metadata cohort registry in a file using JSON format.",
                                    "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/repository-services-connectors/cohort-registry-store-connectors/cohort-registry-file-store-connector",
                                    "org.odpi.openmetadata.adapters.repositoryservices.cohortregistrystore.file.FileBasedRegistryStoreProvider",
                                    ComponentDevelopmentStatus.STABLE,
                                    DeployedImplementationType.COHORT_REGISTRY_STORE_CONNECTOR.getAssociatedTypeName(),
                                    DeployedImplementationType.COHORT_REGISTRY_STORE_CONNECTOR.getDeployedImplementationType()),

    JSON_FILE_OPEN_METADATA_ARCHIVE_CONNECTOR(34,
                                              "f4b49aa8-4f8f-4e0d-a725-fef8fa6ae722",
                                              "Egeria::OpenMetadataArchiveConnector::JSONFile",
                                              "JSON File Open Metadata Archive Connector",
                                              "Connector supports storing of an open metadata archive as a single file stored using JSON format.",
                                              "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-archive-connectors/open-metadata-archive-file-connector",
                                              "org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.file.FileBasedOpenMetadataArchiveStoreProvider",
                                              ComponentDevelopmentStatus.STABLE,
                                              DeployedImplementationType.OPEN_METADATA_ARCHIVE_CONNECTOR.getAssociatedTypeName(),
                                              DeployedImplementationType.OPEN_METADATA_ARCHIVE_CONNECTOR.getDeployedImplementationType()),

    DIRECTORY_OPEN_METADATA_ARCHIVE_CONNECTOR(35,
                                              "66a7ebe3-cf15-44a0-84c9-f777bdfe4ba6",
                                              "Egeria::OpenMetadataArchiveStoreConnector::Directory",
                                              "Directory-based Open Metadata Archive Store Connector",
                                              "Connector supports reading of JSON files that each contain a type definition or an instance organized in to a file directory that collectively make up an open metadata archive.",
                                              "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-archive-connectors/open-metadata-archive-directory-connector",
                                              "org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.directory.DirectoryBasedOpenMetadataArchiveStoreProvider",
                                              ComponentDevelopmentStatus.STABLE,
                                              DeployedImplementationType.OPEN_METADATA_ARCHIVE_CONNECTOR.getAssociatedTypeName(),
                                              DeployedImplementationType.OPEN_METADATA_ARCHIVE_CONNECTOR.getDeployedImplementationType()),

    GAF_TOPIC_CLIENT_CONNECTOR(36,
                               "09f48b14-cce9-4339-a170-63b3be3422f3",
                               "Egeria::OpenMetadataTopicConnector::Kafka::GAFTopic::Client",
                               "GAF Out Topic Client Connector",
                               "Connector supports the receipt of events from the GAF Services.",
                               "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/access-services/gaf-metadata-management/gaf-topic-connectors",
                               "org.odpi.openmetadata.frameworkservices.gaf.connectors.outtopic.GAFOutTopicClientProvider",
                               ComponentDevelopmentStatus.STABLE,
                               DeployedImplementationType.OPEN_METADATA_TOPIC_CONNECTOR.getAssociatedTypeName(),
                               DeployedImplementationType.OPEN_METADATA_TOPIC_CONNECTOR.getDeployedImplementationType()),

    GAF_TOPIC_SERVER_CONNECTOR(37,
                               "59eebd66-aa3b-464e-8ed3-37b41b637fc0",
                               "Egeria::OpenMetadataTopicConnector::Kafka::GAFTopic::Server",
                               "GAF Out Topic Server Connector",
                               "Connector supports the sending of events from the GAF Services.",
                               "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/access-services/gaf-metadata-management/gaf-topic-connectors",
                               "org.odpi.openmetadata.frameworkservices.gaf.connectors.outtopic.GAFOutTopicServerProvider",
                               ComponentDevelopmentStatus.STABLE,
                               DeployedImplementationType.OPEN_METADATA_TOPIC_CONNECTOR.getAssociatedTypeName(),
                               DeployedImplementationType.OPEN_METADATA_TOPIC_CONNECTOR.getDeployedImplementationType()),

    OMF_TOPIC_CLIENT_CONNECTOR(38,
                               "3fc78a69-c272-47ef-bc36-c411b1420b9c",
                               "Egeria::OpenMetadataTopicConnector::Kafka::OMFTopic::Client",
                               "OMF Out Topic Client Connector",
                               "Connector supports the receipt of events from the OMF Services.",
                               "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/access-services/omf-metadata-management/omf-topic-connectors",
                               "org.odpi.openmetadata.frameworkservices.omf.connectors.outtopic.OMFOutTopicClientProvider",
                               ComponentDevelopmentStatus.STABLE,
                               DeployedImplementationType.OPEN_METADATA_TOPIC_CONNECTOR.getAssociatedTypeName(),
                               DeployedImplementationType.OPEN_METADATA_TOPIC_CONNECTOR.getDeployedImplementationType()),

    OMF_TOPIC_SERVER_CONNECTOR(39,
                               "10cdb777-ac35-4c9b-bae2-0b53349bc9d6",
                               "Egeria::OpenMetadataTopicConnector::Kafka::OMFTopic::Server",
                               "OMF Out Topic Server Connector",
                               "Connector supports the sending of events from the OMF Services.",
                               "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/access-services/omf-metadata-management/omf-topic-connectors",
                               "org.odpi.openmetadata.frameworkservices.omf.connectors.outtopic.OMFOutTopicServerProvider",
                               ComponentDevelopmentStatus.STABLE,
                               DeployedImplementationType.OPEN_METADATA_TOPIC_CONNECTOR.getAssociatedTypeName(),
                               DeployedImplementationType.OPEN_METADATA_TOPIC_CONNECTOR.getDeployedImplementationType()),


    CONSOLE_AUDIT_LOG_DESTINATION_CONNECTOR(40,
                                            "4afac741-3dcc-4c60-a4ca-a6dede994e3f",
                                            "Egeria::AuditLogDestinationConnector::Console",
                                            "Console Audit Log Destination Connector",
                                            "Connector supports writing audit log messages to the console.",
                                            "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/repository-services-connectors/audit-log-connectors/audit-log-console-connector",
                                            "org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.console.ConsoleAuditLogStoreProvider",
                                            ComponentDevelopmentStatus.STABLE,
                                            DeployedImplementationType.AUDIT_LOG_DESTINATION_CONNECTOR.getAssociatedTypeName(),
                                            DeployedImplementationType.AUDIT_LOG_DESTINATION_CONNECTOR.getDeployedImplementationType()),

    CONSOLE_EVENT_DISPLAY_AUDIT_LOG_DESTINATION_CONNECTOR(41,
                                                          "f986afd0-15d5-4e99-a5fd-35e4a3013da4",
                                                          "Egeria::AuditLogDestinationConnector::ConsoleEventDisplay",
                                                          "Console Event Display Audit Log Destination Connector",
                                                          "Connector supports writing event audit log messages to the console.",
                                                          "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/repository-services-connectors/audit-log-connectors/audit-log-console-event-display-connector",
                                                          "org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.console.eventdisplay.EventDisplayAuditLogStoreProvider",
                                                          ComponentDevelopmentStatus.STABLE,
                                                          DeployedImplementationType.AUDIT_LOG_DESTINATION_CONNECTOR.getAssociatedTypeName(),
                                                          DeployedImplementationType.AUDIT_LOG_DESTINATION_CONNECTOR.getDeployedImplementationType()),

    TOPIC_AUDIT_LOG_DESTINATION_CONNECTOR(42,
                                          "e92e8bc3-c3ef-404d-933f-9819083c0386",
                                          "Egeria::AuditLogDestinationConnector::EventTopic",
                                          "Event Topic Audit Log Destination Connector",
                                          "Connector supports writing event audit log messages to an Apache Kafka Topic.",
                                          "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/repository-services-connectors/audit-log-connectors/audit-log-event-topic-connector",
                                          "org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.eventtopic.EventTopicAuditLogStoreProvider",
                                          ComponentDevelopmentStatus.STABLE,
                                          DeployedImplementationType.AUDIT_LOG_DESTINATION_CONNECTOR.getAssociatedTypeName(),
                                          DeployedImplementationType.AUDIT_LOG_DESTINATION_CONNECTOR.getDeployedImplementationType()),

    FILE_AUDIT_LOG_DESTINATION_CONNECTOR(43,
                                         "3afcc741-5dcc-4c60-a4ca-a6dede994e3f",
                                         "Egeria::AuditLogDestinationConnector::Files",
                                         "File-based Audit Log Destination Connector",
                                         "Connector supports the distribution of audit log record to a directory where each file is a JSON formatted log record.",
                                         "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/repository-services-connectors/audit-log-connectors/audit-log-file-connector",
                                         "org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.file.FileBasedAuditLogStoreProvider",
                                         ComponentDevelopmentStatus.STABLE,
                                         DeployedImplementationType.AUDIT_LOG_DESTINATION_CONNECTOR.getAssociatedTypeName(),
                                         DeployedImplementationType.AUDIT_LOG_DESTINATION_CONNECTOR.getDeployedImplementationType()),

    POSTGRES_AUDIT_LOG_DESTINATION_CONNECTOR(44,
                                             "757ab89a-dc11-4bb7-a09f-6436b3a8eee2",
                                             "Egeria::AuditLogDestinationConnector::PostgreSQL",
                                             "PostgreSQL Audit Log Destination Connector",
                                             "Connector supports the distribution of audit log record to a PostgreSQL database schema.",
                                             "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/repository-services-connectors/audit-log-connectors/audit-log-postgres-connector",
                                             "org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.postgres.PostgreSQLAuditLogDestinationProvider",
                                             ComponentDevelopmentStatus.STABLE,
                                             DeployedImplementationType.AUDIT_LOG_DESTINATION_CONNECTOR.getAssociatedTypeName(),
                                             DeployedImplementationType.AUDIT_LOG_DESTINATION_CONNECTOR.getDeployedImplementationType()),

    SLF4J_AUDIT_LOG_DESTINATION_CONNECTOR(45,
                                          "e8303911-ba1c-4640-974e-c4d57ee1b310",
                                          "Egeria::AuditLogDestinationConnector::SLF4J",
                                          "SLF4J Audit Log Destination Connector",
                                          "Connector supports logging of audit log messages to the slf4j logger ecosystem.",
                                          "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/repository-services-connectors/audit-log-connectors/audit-log-slf4j-connector",
                                          "org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.slf4j.SLF4JAuditLogStoreProvider",
                                          ComponentDevelopmentStatus.STABLE,
                                          DeployedImplementationType.AUDIT_LOG_DESTINATION_CONNECTOR.getAssociatedTypeName(),
                                          DeployedImplementationType.AUDIT_LOG_DESTINATION_CONNECTOR.getDeployedImplementationType()),

    IN_MEMORY_REPOSITORY_CONNECTOR(50,
                                   "65cc9091-757f-4bcd-b937-426160be8bc2",
                                   "Egeria::OpenMetadataRepositoryConnector::InMemory",
                                   "In Memory Open Metadata Repository Connector",
                                   "Native open metadata repository connector that maps open metadata calls to a set of in memory hash maps - demo use only.",
                                   "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/inmemory-repository-connector",
                                   "org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector.InMemoryOMRSRepositoryConnectorProvider",
                                   ComponentDevelopmentStatus.STABLE,
                                   DeployedImplementationType.REPOSITORY_CONNECTOR.getAssociatedTypeName(),
                                   DeployedImplementationType.REPOSITORY_CONNECTOR.getDeployedImplementationType()),

    READ_ONLY_REPOSITORY_CONNECTOR(51,
                                   "72aa9091-757f-4bcd-b937-426160be8bc2",
                                   "Egeria::OpenMetadataRepositoryConnector::ReadOnly",
                                   "Read Only In Memory Open Metadata Repository Connector",
                                   "Native open metadata repository connector that uses an archive for content and stores nothing else.",
                                   "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/inmemory-repository-connector",
                                   "org.odpi.openmetadata.adapters.repositoryservices.readonly.repositoryconnector.ReadOnlyOMRSRepositoryConnectorProvider",
                                   ComponentDevelopmentStatus.STABLE,
                                   DeployedImplementationType.REPOSITORY_CONNECTOR.getAssociatedTypeName(),
                                   DeployedImplementationType.REPOSITORY_CONNECTOR.getDeployedImplementationType()),

    OMRS_REST_REPOSITORY_CONNECTOR(52,
                                   "75ea56d1-656c-43fb-bc0c-9d35c5553b9e",
                                   "Egeria::OpenMetadataRepositoryConnector::CohortMemberClient::REST",
                                   "REST Cohort Member Client Connector",
                                   "Cohort member client connector that provides access to open metadata located in a remote repository via REST calls.",
                                   "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/omrs-rest-repository-connector",
                                   "org.odpi.openmetadata.adapters.repositoryservices.rest.repositoryconnector.OMRSRESTRepositoryConnectorProvider",
                                   ComponentDevelopmentStatus.STABLE,
                                   DeployedImplementationType.REPOSITORY_CONNECTOR.getAssociatedTypeName(),
                                   DeployedImplementationType.REPOSITORY_CONNECTOR.getDeployedImplementationType()),

    POSTGRES_REPOSITORY_CONNECTOR(53,
                                  "3d64dcb4-9341-4509-86fa-9c49b63db749",
                                  "Egeria::OpenMetadataRepositoryConnector::PostgreSQL",
                                  "PostgreSQL Open Metadata Repository Connector",
                                  "Native open metadata repository connector that maps open metadata calls to a set of tables stored in a PostgreSQL Database Schema.",
                                  "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/postgres-repository-connector",
                                  "org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.PostgresOMRSRepositoryConnectorProvider",
                                  ComponentDevelopmentStatus.STABLE,
                                  DeployedImplementationType.REPOSITORY_CONNECTOR.getAssociatedTypeName(),
                                  DeployedImplementationType.REPOSITORY_CONNECTOR.getDeployedImplementationType()),

    YAML_SECRETS_STORE_CONNECTOR(90,
                                 "e507dfdc-2f03-4746-a274-d3027a8e9153",
                                 "Egeria::SecretsStoreConnector::YAMLFile",
                                 "YAML File Secrets Store Connector",
                                 "Connector retrieves secrets from a YAML file.",
                                 "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/secrets-store-connectors/yaml-secrets-store-connector",
                                 "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider",
                                 ComponentDevelopmentStatus.STABLE,
                                 DeployedImplementationType.SECRETS_STORE_CONNECTOR.getAssociatedTypeName(),
                                 DeployedImplementationType.SECRETS_STORE_CONNECTOR.getDeployedImplementationType()),

    YAML_SECRETS_FILE_CONNECTOR(91,
                                 "178aace3-6286-42dc-9dce-505f6c8b0e76",
                                 "Egeria::FileConnector::YAMLSecretsStoreFile",
                                 "File Connector to YAML Secrets Store",
                                 "Connector enables access to a YAML File that is formatted like a YAML Secrets store. It does not require prior knowledge of the secrets collection to use it.",
                                 "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/secrets-store-connectors/yaml-secrets-store-connector",
                                 "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsFileProvider",
                                 ComponentDevelopmentStatus.STABLE,
                                 DeployedImplementationType.SECRETS_STORE_CONNECTOR.getAssociatedTypeName(),
                                 DeployedImplementationType.SECRETS_STORE_CONNECTOR.getDeployedImplementationType()),

    KAFKA_TOPIC_CONNECTOR(302,
                          "3851e8d0-e343-400c-82cb-3918fed81da6",
                          "Egeria::OpenMetadataTopicConnector::Kafka",
                          "Apache Kafka Open Metadata Topic Connector",
                          "Apache Kafka Open Metadata Topic Connector supports string based events over an Apache Kafka event bus.",
                          "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/event-bus-connectors/open-metadata-topic-connectors/kafka-open-metadata-topic-connector",
                          "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider",
                          ComponentDevelopmentStatus.STABLE,
                          DeployedImplementationType.APACHE_KAFKA_TOPIC.getAssociatedTypeName(),
                          DeployedImplementationType.APACHE_KAFKA_TOPIC.getDeployedImplementationType()),

    JDBC_RESOURCE_CONNECTOR(303,
                            "64463b01-92f6-4d7b-9737-f1d20b2654f4",
                            "Egeria::ResourceConnector::RelationalDatabase::JDBC",
                            "Relational Database JDBC Connector",
                            "Connector supports access to relational databases using exclusively the JDBC API.  This includes both data and metadata.",
                            "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/data-store-connectors/jdbc-resource-connector",
                            "org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnectorProvider",
                            ComponentDevelopmentStatus.STABLE,
                            DeployedImplementationType.JDBC_RELATIONAL_DATABASE.getAssociatedTypeName(),
                            DeployedImplementationType.JDBC_RELATIONAL_DATABASE.getDeployedImplementationType()),

    BASIC_FILE_STORE_CONNECTOR(304,
                               "ba213761-f5f5-4cf5-a95f-6150aef09e0b",
                               "Egeria::ResourceConnector::DataFile",
                               "Basic File Store Connector",
                               "Connector supports reading of Files.",
                               "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/data-store-connectors/file-connectors/basic-file-connector",
                               "org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStoreProvider",
                               ComponentDevelopmentStatus.STABLE,
                               DeployedImplementationType.FILE.getAssociatedTypeName(),
                               DeployedImplementationType.FILE.getDeployedImplementationType()),

    BASIC_FOLDER_CONNECTOR(305,
                           "a9fc9231-f04a-40c4-99b1-4a1058063f5e",
                           "Egeria::ResourceConnector::FileFolder",
                           "Basic Folder Connector",
                           "Connector supports reading of files in a directory (folder).",
                           "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/data-store-connectors/file-connectors/basic-file-connector",
                           "org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFolderProvider",
                           ComponentDevelopmentStatus.STABLE,
                           DeployedImplementationType.FILE_SYSTEM_DIRECTORY.getAssociatedTypeName(),
                           DeployedImplementationType.FILE_SYSTEM_DIRECTORY.getDeployedImplementationType()),

    CSV_FILE_STORE_CONNECTOR(306,
                             "108b85fe-d7b8-45c3-9fb8-742ac4e4fb14",
                             "Egeria::ResourceConnector::DataFile::CSV",
                             "CSV File Store Connector",
                             "Connector supports reading of CSV files.",
                             "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/data-store-connectors/file-connectors/csv-file-connector",
                             "org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVFileStoreProvider",
                             ComponentDevelopmentStatus.STABLE,
                             DeployedImplementationType.CSV_FILE.getAssociatedTypeName(),
                             DeployedImplementationType.CSV_FILE.getDeployedImplementationType()),

    DATA_FOLDER_CONNECTOR(307,
                          "1ef9cbe2-9119-4ac0-b9ac-d838f0ed9caf",
                          "Egeria::ResourceConnector::FileFolder::DataFolder",
                          "Data Folder Connector",
                          "Connector supports reading of files in a directory (folder) that is used to store data.",
                          "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/data-store-connectors/file-connectors/data-folder-connector",
                          "org.odpi.openmetadata.adapters.connectors.datastore.datafolder.DataFolderProvider",
                          ComponentDevelopmentStatus.STABLE,
                          DeployedImplementationType.DATA_FOLDER.getAssociatedTypeName(),
                          DeployedImplementationType.DATA_FOLDER.getDeployedImplementationType()),

    GLOSSARY_DYNAMIC_ARCHIVER_CONNECTOR(320,
                                        "02cfb290-43cb-497c-928e-267bd3d69324",
                                        "Egeria::ArchiveService::Glossary",
                                        "Glossary Dynamic Archiver Connector",
                                        "Connector supports dynamically archiving a glossary into an open metadata archive.",
                                        "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/dynamic-archiver-connectors",
                                        "org.odpi.openmetadata.adapters.connectors.dynamicarchivers.glossary.GlossaryDynamicArchiverProvider",
                                        ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                        DeployedImplementationType.REPOSITORY_GOVERNANCE_SERVICE_CONNECTOR.getAssociatedTypeName(),
                                        DeployedImplementationType.REPOSITORY_GOVERNANCE_SERVICE_CONNECTOR.getDeployedImplementationType()),

    DATA_HUB_MANAGER_INTEGRATION_CONNECTOR(325,
                                           "0ced349c-9a49-45db-a751-4973db83ebb0",
                                           "Egeria::IntegrationConnector::DataHubManager",
                                           "Data Hub Manager Integration Connector",
                                           "This connector builds out a data dictionary for the data stores that are members of a Data Hub.",
                                           "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/nanny-connectors",
                                           "org.odpi.openmetadata.adapters.connectors.liskov.DataHubManagerProvider",
                                           ComponentDevelopmentStatus.STABLE,
                                           DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                           DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType()),

    JDBC_INTEGRATION_CONNECTOR(330,
                               "49cd6772-1efd-40bb-a1d9-cc9460962ff6",
                               "Egeria::IntegrationConnector::RelationalDatabase::JDBC",
                               "JDBC Relational Database Integration Connector",
                               "This connector retrieves schema information about a relational database's tables and columns and catalogs them in the open metadata ecosystem.",
                               "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/integration-connectors/jdbc-integration-connector",
                               "org.odpi.openmetadata.adapters.connectors.integration.jdbc.JDBCIntegrationConnectorProvider",
                               ComponentDevelopmentStatus.STABLE,
                               DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                               DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType()),

    POSTGRES_SERVER_INTEGRATION_CONNECTOR(331,
                                          "71b84d4f-aaa7-4a01-892c-2c60e66d31a4",
                                          "Egeria::IntegrationConnector::DataManagerCatalog::PostgreSQLServer",
                                          "Catalog databases in PostgreSQL database server",
                                          "Catalogs the databases found in a PostgreSQL database server.  This includes the RelationalDatabase asset element plus a connection with the JDBC Resource connector type.",
                                          "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/data-manager-connectors/postgres-server-connectors",
                                          "org.odpi.openmetadata.adapters.connectors.postgres.catalog.PostgresServerIntegrationProvider",
                                          ComponentDevelopmentStatus.STABLE,
                                          DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                          DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType()),

    POSTGRES_SERVER_SURVEY_ACTION_SERVICE(332,
                                          "3e47db62-5407-4cbd-ba54-1ce6612af6f9",
                                          "Egeria::SurveyActionService::DataManagerCatalog::PostgreSQLServer",
                                          "Survey for a PostgreSQL Database Server",
                                          "Surveys the databases, their tables and columns, found in a PostgreSQL database server",
                                          "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/data-manager-connectors/postgres-server-connectors",
                                          "org.odpi.openmetadata.adapters.connectors.postgres.survey.PostgresServerSurveyActionProvider",
                                          ComponentDevelopmentStatus.STABLE,
                                          DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName(),
                                          DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType()),

    POSTGRES_DATABASE_SURVEY_ACTION_SERVICE(333,
                                            "225f53a4-be5e-4008-b3a9-5dcf25f92514",
                                            "Egeria::SurveyActionService::DataManagerCatalog::PostgreSQLDatabase",
                                            "Survey for a PostgreSQL Database",
                                            "Surveys the tables and columns found in a PostgreSQL database server",
                                            "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/data-manager-connectors/postgres-server-connectors",
                                            "org.odpi.openmetadata.adapters.connectors.postgres.survey.PostgresDatabaseSurveyActionProvider",
                                            ComponentDevelopmentStatus.STABLE,
                                            DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName(),
                                            DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType()),


    OSS_UNITY_CATALOG_RESOURCE_CONNECTOR(350,
                                         "0df7ec59-aa05-46fd-a090-3d879f869eff",
                                         "Egeria::ResourceConnector::DataManagerCatalog::UnityCatalog",
                                         "OSS Unity Catalog REST Connector",
                                         "Connector that provides access to the Unity Catalog REST API",
                                         "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/data-manager-connectors/unity-catalog-connectors",
                                         "org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceProvider",
                                         ComponentDevelopmentStatus.STABLE,
                                         UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getAssociatedTypeName(),
                                         UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getDeployedImplementationType()),

    OSS_UNITY_CATALOG_SERVER_SURVEY_ACTION_SERVICE(351,
                                                   "0b110ed0-df5a-4537-abcb-dbdadae26168",
                                                   "Egeria::SurveyActionService::DataManagerCatalog::UnityCatalog::Server",
                                                   "OSS Unity Catalog (UC) Server Survey Service",
                                                   "Surveys the catalogs found in a Unity Catalog Server.",
                                                   "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/data-manager-connectors/unity-catalog-connectors",
                                                   "org.odpi.openmetadata.adapters.connectors.unitycatalog.survey.OSSUnityCatalogServerSurveyProvider",
                                                   ComponentDevelopmentStatus.STABLE,
                                                   DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName(),
                                                   DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType()),

    OSS_UNITY_CATALOG_INSIDE_CATALOG_SURVEY_ACTION_SERVICE(352,
                                                           "6cc7c135-eada-440f-97d8-704c8d0a508f",
                                                           "Egeria::SurveyActionService::DataManagerCatalog::UnityCatalog::InsideCatalog",
                                                           "OSS Unity Catalog (UC) Inside Catalog Survey Action Service",
                                                           "Surveys the contents of a catalog found in a Unity Catalog Server.",
                                                           "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/data-manager-connectors/unity-catalog-connectors",
                                                           "org.odpi.openmetadata.adapters.connectors.unitycatalog.survey.OSSUnityCatalogInsideCatalogSurveyProvider",
                                                           ComponentDevelopmentStatus.STABLE,
                                                           DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName(),
                                                           DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType()),

    OSS_UNITY_CATALOG_SERVER_SYNC_INTEGRATION_CONNECTOR(353,
                                                        "211ab462-e08e-41d4-9e18-052f8a37d669",
                                                        "Egeria::IntegrationConnector::SoftwareServer::UnityCatalog",
                                                        "OSS Unity Catalog (UC) Server Synchronizing Connector",
                                                        "Connector that synchronizes the catalogs between the OSS Unity Catalog 'catalog of catalogs' and the open metadata ecosystem.",
                                                        "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/data-manager-connectors/unity-catalog-connectors",
                                                        "org.odpi.openmetadata.adapters.connectors.unitycatalog.sync.OSSUnityCatalogServerSyncProvider",
                                                        ComponentDevelopmentStatus.STABLE,
                                                        DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName(),
                                                        DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType()),

    OSS_UNITY_CATALOG_INSIDE_CATALOG_SYNC_INTEGRATION_CONNECTOR(354,
                                                                "7767df9a-9d2f-49e1-bf61-8b3f88b11fd0",
                                                                "Egeria::IntegrationConnector::DataManagerCatalog::UnityCatalog::InsideCatalogSync",
                                                                "OSS Unity Catalog (UC) Inside a Catalog Synchronizing Connector",
                                                                "Connector supports the synchronization of a catalog from a Unity Catalog Server.",
                                                                "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/data-manager-connectors/unity-catalog-connectors",
                                                                "org.odpi.openmetadata.adapters.connectors.unitycatalog.sync.OSSUnityCatalogInsideCatalogSyncProvider",
                                                                ComponentDevelopmentStatus.STABLE,
                                                                DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                                                DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType()),

    OSS_UNITY_CATALOG_INSIDE_SCHEMA_SURVEY_ACTION_SERVICE(355,
                                                          "cde2cd15-500c-4f30-b54b-99b29e7e9cb6",
                                                          "Egeria::SurveyActionService::DataManagerCatalog::UnityCatalog::InsideSchema",
                                                          "OSS Unity Catalog (UC) Inside Schema Survey Action Service",
                                                          "Surveys the contents of a schema found in a Unity Catalog Server.",
                                                          "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/data-manager-connectors/unity-catalog-connectors",
                                                          "org.odpi.openmetadata.adapters.connectors.unitycatalog.survey.OSSUnityCatalogInsideSchemaSurveyProvider",
                                                          ComponentDevelopmentStatus.STABLE,
                                                          DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName(),
                                                          DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType()),

    OSS_UNITY_CATALOG_INSIDE_VOLUME_SURVEY_ACTION_SERVICE(356,
                                                          "5a9f3813-2cc7-46ac-a1a8-b2b508d07100",
                                                          "Egeria::SurveyActionService::DataManagerCatalog::UnityCatalog::InsideVolume",
                                                          "OSS Unity Catalog (UC) Inside a Volume Survey Service",
                                                          "Connector supports the surveying of files in a Unity Catalog Volume's directory (folder) and the directories beneath it.",
                                                          "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/data-manager-connectors/unity-catalog-connectors",
                                                          "org.odpi.openmetadata.adapters.connectors.unitycatalog.survey.OSSUnityCatalogInsideVolumeSurveyProvider",
                                                          ComponentDevelopmentStatus.STABLE,
                                                          DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName(),
                                                          DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType()),

    PROVISION_UNITY_CATALOG_GOVERNANCE_ACTION_SERVICE(358,
                                                      "58681cd6-ded2-488b-be8d-031e42cb345c",
                                                      "Egeria::GovernanceActionService::DataManagerCatalog::UnityCatalog::Provision",
                                                      "Provision Unity Catalog Governance Action Service",
                                                      "Governance Action Service that provisions resources to an OSS Unity Catalog server.",
                                                      "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/data-manager-connectors/unity-catalog-connectors",
                                                      "org.odpi.openmetadata.adapters.connectors.unitycatalog.provision.ProvisionUnityCatalogGovernanceActionProvider",
                                                      ComponentDevelopmentStatus.STABLE,
                                                      DeployedImplementationType.GOVERNANCE_SERVICE.getAssociatedTypeName(),
                                                      DeployedImplementationType.GOVERNANCE_SERVICE.getDeployedImplementationType()),

    POSTGRES_TABULAR_DATA_SET_CONNECTOR(360,
                                        "92bdcf05-96d3-4660-936d-2a8ae15c2c14",
                                        "Egeria::ResourceConnector::TabularDataSet::PostgreSQL",
                                        "PostgreSQL Tabular Data Set Connector",
                                        "Connector supports reading/writing of data to a PostgreSQL table.",
                                        "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/data-manager-connectors/postgres-server-connectors",
                                        "org.odpi.openmetadata.adapters.connectors.postgres.tabulardatasource.PostgresTabularDataSetProvider",
                                        ComponentDevelopmentStatus.STABLE,
                                        PostgresDeployedImplementationType.POSTGRESQL_TABULAR_DATA_SET.getAssociatedTypeName(),
                                        PostgresDeployedImplementationType.POSTGRESQL_TABULAR_DATA_SET.getDeployedImplementationType()),

    POSTGRES_TABULAR_DATA_SET_COLLECTION_CONNECTOR(361,
                                                   "25a44f44-fee6-4334-acab-282a09bbc924",
                                                   "Egeria::ResourceConnector::TabularDataSetCollection::PostgreSQL",
                                                   "PostgreSQL Tabular Data Set Collection Connector",
                                                   "Connector manages a collection of tabular data sets from a PostgreSQL database server.",
                                                   "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/data-manager-connectors/postgres-server-connectors",
                                                   "org.odpi.openmetadata.adapters.connectors.postgres.tabulardatasource.PostgresTabularDataSetCollectionProvider",
                                                   ComponentDevelopmentStatus.STABLE,
                                                   PostgresDeployedImplementationType.POSTGRESQL_TABULAR_DATA_SET_COLLECTION.getAssociatedTypeName(),
                                                   PostgresDeployedImplementationType.POSTGRESQL_TABULAR_DATA_SET_COLLECTION.getDeployedImplementationType()),

    CSV_TABULAR_DATA_SET_COLLECTION_CONNECTOR(362,
                                              "32c25bc2-e0bf-4d78-87ab-ed3c5aead169",
                                              "Egeria::ResourceConnector::TabularDataSetCollection::CSVFile",
                                              "CSV Tabular Data Set Collection Connector",
                                              "Connector manages a collection of tabular data sets stored in CSV files.",
                                              "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/data-store-connectors/file-connectors/csv-file-connector",
                                              "org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVTabularDataSetCollectionProvider",
                                              ComponentDevelopmentStatus.STABLE,
                                              DeployedImplementationType.CSV_TABULAR_DATA_SET_COLLECTION.getAssociatedTypeName(),
                                              DeployedImplementationType.CSV_TABULAR_DATA_SET_COLLECTION.getDeployedImplementationType()),

    CSV_TABULAR_DATA_SET_CONNECTOR(363,
                                   "fabaf243-e0ed-4f30-9df0-1cba38d90df4",
                                   "Egeria::ResourceConnector::TabularDataSet::CSV",
                                   "CSV Tabular Data Set Connector",
                                   "Connector supports reading/writing of data to a CSV table.",
                                   "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/data-store-connectors/file-connectors/csv-file-connector",
                                   "org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVTabularDataSetProvider",
                                   ComponentDevelopmentStatus.STABLE,
                                   DeployedImplementationType.CSV_TABULAR_DATA_SET.getAssociatedTypeName(),
                                   DeployedImplementationType.CSV_TABULAR_DATA_SET.getDeployedImplementationType()),

    REFERENCE_DATA_LIST_TABULAR_DATA_SET(365,
                                         "e2870eda-cba3-406e-b44a-42b3364d2316",
                                         "Egeria::ResourceConnector::TabularDataSet::ReferenceDataSetList",
                                         "Reference Data Set List Tabular Data Set Connector",
                                         "Connector manages the list of reference data sets as if it was a tabular data set.",
                                         "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/nanny-connectors",
                                         "org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.referencedata.ReferenceDataSetListProvider",
                                         ComponentDevelopmentStatus.STABLE,
                                         DeployedImplementationType.TABULAR_DATA_SET.getAssociatedTypeName(),
                                         DeployedImplementationType.TABULAR_DATA_SET.getDeployedImplementationType()),

    REFERENCE_DATA_TABULAR_DATA_SET(366,
                                    "2755cb53-91c3-472f-9f3a-2f6f8269fb7e",
                                    "Egeria::ResourceConnector::TabularDataSet::ReferenceDataSet",
                                    "Reference Data Set Tabular Data Set Connector",
                                    "Connector manages an open metadata reference data set as if it was a tabular data set.",
                                    "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/nanny-connectors",
                                    "org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.referencedata.ReferenceDataSetProvider",
                                    ComponentDevelopmentStatus.STABLE,
                                    DeployedImplementationType.TABULAR_DATA_SET.getAssociatedTypeName(),
                                    DeployedImplementationType.TABULAR_DATA_SET.getDeployedImplementationType()),


    VALID_METADATA_VALUE_SET_LIST_TABULAR_DATA_SET(367,
                                                   "bb25f6a4-bb02-4dcc-bf8b-9e1e4f0fe879",
                                                   "Egeria::ResourceConnector::TabularDataSet::ValidMetadataValueSetList",
                                                   "Valid Metadata Value Set List Tabular Data Set Connector",
                                                   "Connector manages the list of valid metadata value sets as if it was a tabular data set.",
                                                   "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/nanny-connectors",
                                                   "org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.validmetadatavalues.ValidMetadataValueSetListProvider",
                                                   ComponentDevelopmentStatus.STABLE,
                                                   DeployedImplementationType.TABULAR_DATA_SET.getAssociatedTypeName(),
                                                   DeployedImplementationType.TABULAR_DATA_SET.getDeployedImplementationType()),

    VALID_METADATA_VALUE_TABULAR_DATA_SET(368,
                                          "381d1d1a-d498-4e47-a555-7004984a63c7",
                                          "Egeria::ResourceConnector::TabularDataSet::ValidMetadataValues",
                                          "Valid Metadata Values Tabular Data Set Connector",
                                          "Connector manages an open metadata valid value set for a particular property as if it was a tabular data set.",
                                          "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/nanny-connectors",
                                          "org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.validmetadatavalues.ValidMetadataValueDataSetProvider",
                                          ComponentDevelopmentStatus.STABLE,
                                          DeployedImplementationType.TABULAR_DATA_SET.getAssociatedTypeName(),
                                          DeployedImplementationType.TABULAR_DATA_SET.getDeployedImplementationType()),

    OPEN_METADATA_TYPES_LIST_TABULAR_DATA_SET(369,
                                              "c67aed34-284e-4a4f-8293-0cfb65cb0321",
                                              "Egeria::ResourceConnector::TabularDataSet::OpenMetadataTypesList",
                                              "Open Metadata Types List Tabular Data Set Connector",
                                              "Connector manages the list of open metadata types as if it was a tabular data set.",
                                              "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/nanny-connectors",
                                              "org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.openmetadatatypes.OpenMetadataTypesDataSetProvider",
                                              ComponentDevelopmentStatus.STABLE,
                                              DeployedImplementationType.TABULAR_DATA_SET.getAssociatedTypeName(),
                                              DeployedImplementationType.TABULAR_DATA_SET.getDeployedImplementationType()),

    DATA_TYPES_LIST_TABULAR_DATA_SET(370,
                                     "29888327-163d-4ddb-910f-1dc9ebb97fc7",
                                     "Egeria::ResourceConnector::TabularDataSet::OpenMetadataDataTypesList",
                                     "Open Metadata Data Types List Tabular Data Set Connector",
                                     "Connector manages the list of open metadata data types that can be assigned to a property as if it was a tabular data set.",
                                     "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/nanny-connectors",
                                     "org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.openmetadatatypes.OpenMetadataDataTypesDataSetProvider",
                                     ComponentDevelopmentStatus.STABLE,
                                     DeployedImplementationType.TABULAR_DATA_SET.getAssociatedTypeName(),
                                     DeployedImplementationType.TABULAR_DATA_SET.getDeployedImplementationType()),

    PROPERTIES_LIST_TABULAR_DATA_SET(371,
                                     "d4f84de1-2f61-44cd-8d36-68a4cc7ea274",
                                     "Egeria::ResourceConnector::TabularDataSet::OpenMetadataPropertiesList",
                                     "Open Metadata Properties List Tabular Data Set Connector",
                                     "Connector manages the list of open metadata properties as if it was a tabular data set.",
                                     "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/nanny-connectors",
                                     "org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.openmetadatatypes.OpenMetadataPropertiesDataSetProvider",
                                     ComponentDevelopmentStatus.STABLE,
                                     DeployedImplementationType.TABULAR_DATA_SET.getAssociatedTypeName(),
                                     DeployedImplementationType.TABULAR_DATA_SET.getDeployedImplementationType()),

    ATTRIBUTES_FOR_TYPES_LIST_TABULAR_DATA_SET(372,
                                               "ceed55a4-70ff-44bf-9172-2df27e4f63ec",
                                               "Egeria::ResourceConnector::TabularDataSet::OpenMetadataAttributesForTypesList",
                                               "Open Metadata Attributes for Types List Tabular Data Set Connector",
                                               "Connector manages the list of open metadata attributes for each type as if it was a tabular data set.",
                                               "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/nanny-connectors",
                                               "org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.openmetadatatypes.OpenMetadataAttributesForTypesDataSetProvider",
                                               ComponentDevelopmentStatus.STABLE,
                                               DeployedImplementationType.TABULAR_DATA_SET.getAssociatedTypeName(),
                                               DeployedImplementationType.TABULAR_DATA_SET.getDeployedImplementationType()),



    JACQUARD_PRODUCT_LOOM(380,
                          "ec0dab36-d3c5-44fe-94c4-67695fe465a5",
                          "Egeria::IntegrationConnector::Jacquard::OpenMetadataDigitalProductLoom",
                          "Jacquard Open Metadata Digital Product Loom",
                          "Connector extracts open metadata collections into useful digital products.",
                          "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/nanny-connectors",
                          "org.odpi.openmetadata.adapters.connectors.jacquard.JacquardIntegrationConnectorProvider",
                          ComponentDevelopmentStatus.STABLE,
                          DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                          DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType()),

    BAUDOT_SUBSCRIPTION_MANAGER(381,
                                "a99bbd51-71ed-4c1d-9a2a-cd04e6b95837",
                                "Egeria::GovernanceService::Watchdog::Baudot::DigitalProductSubscriptionManager",
                                "Baudot Open Metadata Digital Product Subscription Manager",
                                "Manages the notifications used to fulfill digital product subscriptions obligations.",
                                "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/nanny-connectors",
                                "org.odpi.openmetadata.adapters.connectors.baudot.BaudotSubscriptionManagementProvider",
                                ComponentDevelopmentStatus.STABLE,
                                DeployedImplementationType.WATCHDOG_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName(),
                                DeployedImplementationType.WATCHDOG_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType()),

    CANCEL_SUBSCRIPTION(382,
                        "9c45474b-a685-40df-8b40-5b7e7988546a",
                        "Egeria::GovernanceActionService::DigitalSubscription::Cancel",
                        "Cancel Digital Subscription Governance Action Service",
                        "Governance Action Service that cancels a digital subscription.",
                        "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/nanny-connectors",
                        "org.odpi.openmetadata.adapters.connectors.subscriptions.CancelSubscriptionGovernanceActionProvider",
                        ComponentDevelopmentStatus.STABLE,
                        DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName(),
                        DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType()),

    CREATE_SUBSCRIPTION(383,
                        "488136c1-fded-4449-a820-60762b4f7fac",
                        "Egeria::GovernanceActionService::DigitalSubscription::Create",
                        "Create Digital Subscription Governance Action Service",
                        "Governance Action Service that creates a digital subscription.",
                        "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/nanny-connectors",
                        "org.odpi.openmetadata.adapters.connectors.subscriptions.CreateSubscriptionGovernanceActionProvider",
                        ComponentDevelopmentStatus.STABLE,
                        DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName(),
                        DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType()),

    PROVISION_SUBSCRIPTION(384,
                           "b35dc137-7014-4ebb-ae95-001e736a5443",
                           "Egeria::GovernanceActionService::DigitalSubscription::Provision",
                           "Wedgwood Digital Subscription Governance Action Service",
                           "Governance Action Service that provisions the data identified in a digital subscription to the requested destination.",
                           "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/nanny-connectors",
                           "org.odpi.openmetadata.adapters.connectors.wedgwood.WedgwoodProvisionSubscriptionGovernanceActionProvider",
                           ComponentDevelopmentStatus.STABLE,
                           DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName(),
                           DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType()),

    BABBAGE_ANALYTICAL_ENGINE(390,
                              "e553f0b6-06b9-427e-921e-3b7993c5b0ef",
                              "Egeria::IntegrationConnector::Babbage::AnalyticalEngine",
                              "Babbage Analytical Engine Integration Connector",
                              "Connector coordinates the gathering of analytical insights from open metadata by calling the Lovelace Governance Services.",
                              "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/nanny-connectors",
                              "org.odpi.openmetadata.adapters.connectors.babbage.BabbageAnalyticalEngineProvider",
                              ComponentDevelopmentStatus.STABLE,
                              DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                              DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType()),

    KARMA_POINTS_AWARDS_LOVELACE_SERVICE(391,
                                         "b435b4e6-9a98-4ff2-b216-8b067eab0cee",
                                         "Egeria::GovernanceService::Watchdog::Lovelace::KarmaPointAwardsService",
                                         "Karma Point Awards Lovelace Service",
                                         "A Watchdog Action Service that detects changes to elements, identifies the user who is performing the update and awards them a karma point for their contribution.",
                                         "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/lovelace-insights",
                                         "org.odpi.openmetadata.adapters.connectors.organizationinsight.karmapoints.LovelaceKarmaPointAwardsServiceProvider",
                                         ComponentDevelopmentStatus.STABLE,
                                         DeployedImplementationType.WATCHDOG_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName(),
                                         DeployedImplementationType.WATCHDOG_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType()),

    ZONE_MEMBERSHIP_PROFILER_LOVELACE_SERVICE(392,
                                           "d77f6c4c-abb7-4f1c-b839-fb41819ec9e1",
                                           "Egeria::GovernanceService::Verification::Lovelace::ZoneMembershipProfilerService",
                                           "Zone Membership Profiler Lovelace Service",
                                           "A Governance Action Service that populates the ZoneMembershipProfile classification for each catalogued governance zone.",
                                           "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/lovelace-insights",
                                           "org.odpi.openmetadata.adapters.connectors.securityinsight.zoneprofile.LovelaceZoneMembershipProfilerServiceProvider",
                                              ComponentDevelopmentStatus.STABLE,
                                              DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName(),
                                              DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType()),

    ORGANIZATIONS_TABULAR_DATA_SET(400,
                                   "5d88c0ad-8915-4e42-bc23-dc69bf00eb6c",
                                   "Egeria::ResourceConnector::TabularDataSet::Organizations",
                                   "Organizations Tabular Data Set Connector",
                                   "Connector manages the list of organizations known to the open metadata ecosystem.",
                                   "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/nanny-connectors",
                                   "org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.masterdata.OrganizationsTabularDataSetProvider",
                                   ComponentDevelopmentStatus.STABLE,
                                   DeployedImplementationType.TABULAR_DATA_SET.getAssociatedTypeName(),
                                   DeployedImplementationType.TABULAR_DATA_SET.getDeployedImplementationType()),

    PEOPLE_TABULAR_DATA_SET(401,
                                   "28086621-7a3f-4f82-80f8-b0e39bc7e81d",
                                   "Egeria::ResourceConnector::TabularDataSet::People",
                                   "People Tabular Data Set Connector",
                                   "Connector manages the list of people known to the open metadata ecosystem.",
                                   "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/nanny-connectors",
                                   "org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.masterdata.PeopleTabularDataSetProvider",
                                   ComponentDevelopmentStatus.STABLE,
                                   DeployedImplementationType.TABULAR_DATA_SET.getAssociatedTypeName(),
                                   DeployedImplementationType.TABULAR_DATA_SET.getDeployedImplementationType()),

    DIGITAL_PRODUCTS_TABULAR_DATA_SET(402,
                            "00b26c63-0f6b-4675-b3cd-acc32585b190",
                            "Egeria::ResourceConnector::TabularDataSet::DigitalProducts",
                            "Digital Products Tabular Data Set Connector",
                            "Connector manages the list of digital products known to the open metadata ecosystem.",
                            "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/nanny-connectors",
                            "org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.masterdata.DigitalProductsTabularDataSetProvider",
                            ComponentDevelopmentStatus.STABLE,
                            DeployedImplementationType.TABULAR_DATA_SET.getAssociatedTypeName(),
                            DeployedImplementationType.TABULAR_DATA_SET.getDeployedImplementationType()),

    LOCATIONS_TABULAR_DATA_SET(403,
                                      "a0695f81-cd1a-4f35-a62c-5886c8aeb4f2",
                                      "Egeria::ResourceConnector::TabularDataSet::Locations",
                                      "Locations Tabular Data Set Connector",
                                      "Connector manages the list of locations known to the open metadata ecosystem.",
                                      "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/nanny-connectors",
                                      "org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.masterdata.LocationsTabularDataSetProvider",
                                      ComponentDevelopmentStatus.STABLE,
                                      DeployedImplementationType.TABULAR_DATA_SET.getAssociatedTypeName(),
                                      DeployedImplementationType.TABULAR_DATA_SET.getDeployedImplementationType()),

    GOVERNANCE_CONTROLS_TABULAR_DATA_SET(404,
                               "0e5bc8ec-36c0-4369-8a57-3a7b6fc8d044",
                               "Egeria::ResourceConnector::TabularDataSet::GovernanceControls",
                               "Governance Controls Tabular Data Set Connector",
                               "Connector manages the list of governance controls known to the open metadata ecosystem.",
                               "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/nanny-connectors",
                               "org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.governance.GovernanceControlsTabularDataSetProvider",
                               ComponentDevelopmentStatus.STABLE,
                               DeployedImplementationType.TABULAR_DATA_SET.getAssociatedTypeName(),
                               DeployedImplementationType.TABULAR_DATA_SET.getDeployedImplementationType()),


    SECRETS_STORES_TABULAR_DATA_SET(405,
                                  "5cd776c4-f241-4710-882e-e10d32ce0249",
                                  "Egeria::ResourceConnector::TabularDataSet::SecretsStores",
                                  "Secrets Stores Tabular Data Set Connector",
                                  "Connector manages the list of secrets stores known to the open metadata ecosystem.",
                                  "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/nanny-connectors",
                                  "org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.security.SecretsStoresTabularDataSetProvider",
                                  ComponentDevelopmentStatus.STABLE,
                                  DeployedImplementationType.TABULAR_DATA_SET.getAssociatedTypeName(),
                                  DeployedImplementationType.TABULAR_DATA_SET.getDeployedImplementationType()),

    CERTIFICATIONS_TABULAR_DATA_SET(406,
                                         "96d2c1a4-d980-41bd-afc2-a531e98c8937",
                                         "Egeria::ResourceConnector::TabularDataSet::Certifications",
                                         "Certifications Tabular Data Set Connector",
                                         "Connector manages the list of certifications known to the open metadata ecosystem.",
                                         "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/nanny-connectors",
                                         "org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.governance.CertificationsTabularDataSetProvider",
                                         ComponentDevelopmentStatus.STABLE,
                                         DeployedImplementationType.TABULAR_DATA_SET.getAssociatedTypeName(),
                                         DeployedImplementationType.TABULAR_DATA_SET.getDeployedImplementationType()),

    EXCEPTIONS_TABULAR_DATA_SET(407,
                                         "5e7b59ae-57f0-4723-967c-57b4eb7d18f0",
                                         "Egeria::ResourceConnector::TabularDataSet::Exceptions",
                                         "Exceptions Tabular Data Set Connector",
                                         "Connector manages the list of exceptions known to the open metadata ecosystem.",
                                         "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/nanny-connectors",
                                         "org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.governance.ExceptionsTabularDataSetProvider",
                                         ComponentDevelopmentStatus.STABLE,
                                         DeployedImplementationType.TABULAR_DATA_SET.getAssociatedTypeName(),
                                         DeployedImplementationType.TABULAR_DATA_SET.getDeployedImplementationType()),

    LICENSES_TABULAR_DATA_SET(408,
                                         "d75ed7af-d7b3-4554-b708-9fc12f3e5c86",
                                         "Egeria::ResourceConnector::TabularDataSet::Licenses",
                                         "Licences Tabular Data Set Connector",
                                         "Connector manages the list of licenses known to the open metadata ecosystem.",
                                         "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/nanny-connectors",
                                         "org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.governance.LicensesTabularDataSetProvider",
                                         ComponentDevelopmentStatus.STABLE,
                                         DeployedImplementationType.TABULAR_DATA_SET.getAssociatedTypeName(),
                                         DeployedImplementationType.TABULAR_DATA_SET.getDeployedImplementationType()),

    APACHE_ATLAS_RESOURCE_CONNECTOR(500,
                                    "aea66ea9-5763-4f93-ba89-244b60ae0da7",
                                    "Egeria::ResourceConnector::System::ApacheAtlas",
                                    "Apache Atlas REST Connector",
                                    "Connector that provides a Java API to Apache Atlas.",
                                    "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/system-connectors/apache-atlas-connectors",
                                    "org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.ApacheAtlasRESTProvider",
                                    ComponentDevelopmentStatus.STABLE,
                                    AtlasDeployedImplementationType.APACHE_ATLAS_SERVER.getAssociatedTypeName(),
                                    AtlasDeployedImplementationType.APACHE_ATLAS_SERVER.getDeployedImplementationType()),

    APACHE_ATLAS_INTEGRATION_CONNECTOR(501,
                                       "aeca7da2-80c1-4e2a-baa5-8c30472be766",
                                       "Egeria::IntegrationConnector::Catalog::ApacheAtlas",
                                       "Apache Atlas Integration Connector",
                                       "Connector extracts data assets and glossary terms from Apache Atlas and, optionally copies governance metadata to Apache Atlas and attaches it to appropriate entities.",
                                       "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/system-connectors/apache-atlas-connectors",
                                       "org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ApacheAtlasIntegrationProvider",
                                       ComponentDevelopmentStatus.STABLE,
                                       DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                       DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType()),

    APACHE_ATLAS_SURVEY_SERVICE(502,
                                "095de44e-fe18-4849-bf08-4d91d9ea3e35",
                                "Egeria::SurveyActionService::SurveyApacheAtlas",
                                "Apache Atlas Survey Action Service",
                                "Discovers the types and number of instances within an Apache Atlas server.",
                                "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/system-connectors/apache-atlas-connectors",
                                "org.odpi.openmetadata.adapters.connectors.apacheatlas.survey.SurveyApacheAtlasProvider",
                                ComponentDevelopmentStatus.STABLE,
                                DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName(),
                                DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType()),

    APACHE_KAFKA_ADMIN_RESOURCE_CONNECTOR(503,
                                          "200afa34-c4e6-4416-afb7-4ffa0f8f2e5e",
                                          "Egeria::ResourceConnector::System::ApacheKafka",
                                          "Apache Kafka Admin Connector",
                                          "Connector that provides access to the Admin Interface for an Apache Kafka Server.",
                                          "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/system-connectors/apache-kafka-connectors",
                                          "org.odpi.openmetadata.adapters.connectors.apachekafka.resource.ApacheKafkaAdminProvider",
                                          ComponentDevelopmentStatus.STABLE,
                                          KafkaDeployedImplementationType.APACHE_KAFKA_SERVER.getAssociatedTypeName(),
                                          KafkaDeployedImplementationType.APACHE_KAFKA_SERVER.getDeployedImplementationType()),

    APACHE_KAFKA_TOPIC_INTEGRATION_CONNECTOR(504,
                                             "6a9bc1a2-e960-48c4-92c8-396637859f41",
                                             "Egeria::IntegrationConnector::Topics::ApacheKafka",
                                             "Apache Kafka Topic Integration Connector",
                                             "Connector maintains a list of KafkaTopic assets associated with an Apache Kafka server.",
                                             "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/system-connectors/apache-kafka-connectors",
                                             "org.odpi.openmetadata.adapters.connectors.apachekafka.integration.KafkaTopicIntegrationProvider",
                                             ComponentDevelopmentStatus.STABLE,
                                             DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                             DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType()),

    APACHE_KAFKA_TOPIC_SURVEY_SERVICE(505,
                                      "a6f9e92f-a16a-494c-bac6-d3618de12a6a",
                                      "Egeria::SurveyActionService::SurveyApacheKafkaServer",
                                      "Apache Kafka Server Survey Action Service",
                                      "Discovers the topics supported by the Apache Kafka Server.",
                                      "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/system-connectors/apache-kafka-connectors",
                                      "org.odpi.openmetadata.adapters.connectors.apachekafka.survey.SurveyApacheKafkaServerProvider",
                                      ComponentDevelopmentStatus.STABLE,
                                      DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName(),
                                      DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType()),

    DISTRIBUTE_AUDIT_LOG_EVENTS_FROM_KAFKA_TOPIC(506,
                                                 "b237dfab-12bc-42b2-95d9-b459f17b0af5",
                                                 "Egeria::IntegrationConnector::Catalog::DistributeAuditEventsFromKafka",
                                                 "Distribute Audit Log Events From Kafka Topic Integration Connector",
                                                 "Connector distributes audit log events from a Kafka topic to the catalog targets.",
                                                 "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/integration-connectors/kafka-audit-integration-connector",
                                                 "org.odpi.openmetadata.adapters.connectors.integration.kafkaaudit.DistributeAuditEventsFromKafkaProvider",
                                                 ComponentDevelopmentStatus.STABLE,
                                                 DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                                 DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType()),


    OPEN_API_MONITORING_INTEGRATION_CONNECTOR(507,
                                              "4cf65dbf-0808-4968-819b-6a49a9fe537a",
                                              "Egeria::IntegrationConnector::APIs::OpenAPISpecificationMonitor",
                                              "Open API Specification Monitor Integration Connector",
                                              "Connector maintains the definitions associated with an API that supports the OpenAPI specification.",
                                              "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/integration-connectors/openapi-integration-connector",
                                              "org.odpi.openmetadata.adapters.connectors.integration.openapis.OpenAPIMonitorIntegrationProvider",
                                              ComponentDevelopmentStatus.STABLE,
                                              DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                              DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType()),


    OMAG_SERVER_PLATFORM_CATALOG_INTEGRATION_CONNECTOR(510,
                                                       "adf0626d-0c64-428c-9a52-b680b51701bd",
                                                       "Egeria::IntegrationConnector::System::OMAGServerPlatform",
                                                       "OMAG Server Platform Catalog Connector",
                                                       "Connector that catalogues the servers running on an instance of Egeria's OMAG Server Platform.",
                                                       "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/system-connectors/egeria-system-connectors",
                                                       "org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.catalog.OMAGServerPlatformCatalogProvider",
                                                       ComponentDevelopmentStatus.STABLE,
                                                       DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                                       DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType()),

    OMAG_SERVER_PLATFORM_RESOURCE_CONNECTOR(511,
                                            "da054904-e6d9-441e-8f30-4d08c6784d16",
                                            "Egeria::ResourceConnector::System::OMAGServerPlatform",
                                            "OMAG Server Platform Connector",
                                            "Connector that provides access to the operational services of Egeria's OMAG Server Platform.",
                                            "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/system-connectors/egeria-system-connectors",
                                            "org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.OMAGServerPlatformProvider",
                                            ComponentDevelopmentStatus.STABLE,
                                            EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getAssociatedTypeName(),
                                            EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getDeployedImplementationType()),

    OMAG_SERVER_RESOURCE_CONNECTOR(512,
                                   "fabbd17e-8f67-465c-a47a-34d5c8c8f482",
                                   "Egeria::ResourceConnector::System::OMAGServer",
                                   "OMAG Server Connector",
                                   "Connector that provides access to the operational services of any type of Egeria's OMAG Server.",
                                   "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/system-connectors/egeria-system-connectors",
                                   "org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.OMAGServerProvider",
                                   ComponentDevelopmentStatus.STABLE,
                                   EgeriaDeployedImplementationType.OMAG_SERVER.getAssociatedTypeName(),
                                   EgeriaDeployedImplementationType.OMAG_SERVER.getDeployedImplementationType()),

    ENGINE_HOST_RESOURCE_CONNECTOR(513,
                                   "17e3636d-f1fd-4ebc-8604-d789c0a2021b",
                                   "Egeria::ResourceConnector::System::EngineHost",
                                   "Engine Host Connector",
                                   "Connector that provides access to the operational services of Egeria's Engine Host.",
                                   "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/system-connectors/egeria-system-connectors",
                                   "org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.EngineHostProvider",
                                   ComponentDevelopmentStatus.STABLE,
                                   EgeriaDeployedImplementationType.ENGINE_HOST.getAssociatedTypeName(),
                                   EgeriaDeployedImplementationType.ENGINE_HOST.getDeployedImplementationType()),

    INTEGRATION_DAEMON_RESOURCE_CONNECTOR(514,
                                          "1b37e67b-c600-4c91-8730-a03a28dfc190",
                                          "Egeria::ResourceConnector::System::IntegrationDaemon",
                                          "Integration Daemon Connector",
                                          "Connector that provides access to the operational services of Egeria's Integration Daemon.",
                                          "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/system-connectors/egeria-system-connectors",
                                          "org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.IntegrationDaemonProvider",
                                          ComponentDevelopmentStatus.STABLE,
                                          EgeriaDeployedImplementationType.INTEGRATION_DAEMON.getAssociatedTypeName(),
                                          EgeriaDeployedImplementationType.INTEGRATION_DAEMON.getDeployedImplementationType()),

    METADATA_ACCESS_SERVER_RESOURCE_CONNECTOR(515,
                                              "4f670bba-a0b5-4748-83b5-f7490d091f89",
                                              "Egeria::ResourceConnector::System::MetadataAccessServer",
                                              "Metadata Access Server Connector",
                                              "Connector that provides access to the operational services of Egeria's Metadata Access Server.",
                                              "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/system-connectors/egeria-system-connectors",
                                              "org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.MetadataAccessServerProvider",
                                              ComponentDevelopmentStatus.STABLE,
                                              EgeriaDeployedImplementationType.METADATA_ACCESS_SERVER.getAssociatedTypeName(),
                                              EgeriaDeployedImplementationType.METADATA_ACCESS_SERVER.getDeployedImplementationType()),

    VIEW_SERVER_RESOURCE_CONNECTOR(516,
                                   "33dca742-ef9c-42dd-bb4a-f20704055d19",
                                   "Egeria::ResourceConnector::System::ViewServer",
                                   "View Server Connector",
                                   "Connector that provides access to the operational services of Egeria's View Server.",
                                   "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/system-connectors/egeria-system-connectors",
                                   "org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.ViewServerProvider",
                                   ComponentDevelopmentStatus.STABLE,
                                   EgeriaDeployedImplementationType.VIEW_SERVER.getAssociatedTypeName(),
                                   EgeriaDeployedImplementationType.VIEW_SERVER.getDeployedImplementationType()),

    SURVEY_REPORT_WRITER(517,
                                   "f1071e6c-da8b-4a01-899a-14b9d24b1165",
                                   "Egeria::GovernanceActionService::SurveyReportWriter",
                                   "Survey Report Governance Action Service",
                                   "Outputs a survey report for an asset as a file.",
                                   "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/report-generating-connectors",
                                   "org.odpi.openmetadata.adapters.connectors.reports.surveyreport.SurveyReportProvider",
                                   ComponentDevelopmentStatus.STABLE,
                                   DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName(),
                                   DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType()),

    API_BASED_OPEN_LINEAGE_LOG_STORE(520,
                                     "88fc3777-19a3-4b17-b8fc-09c29e04f7d1",
                                     "Egeria::IntegrationConnector::Lineage::APIBasedOpenLineageLogStore",
                                     "API-based Open Lineage Log Store Integration Connector",
                                     "Connector that calls an API to store open lineage events.",
                                     "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/integration-connectors/openlineage-integration-connectors",
                                     "org.odpi.openmetadata.adapters.connectors.integration.openlineage.APIBasedOpenLineageLogStoreProvider",
                                     ComponentDevelopmentStatus.STABLE,
                                     DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                     DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType()),

    FILE_BASED_OPEN_LINEAGE_LOG_STORE(521,
                                      "2cb5763e-9c67-4e81-be2b-883dd20ae93e",
                                      "Egeria::IntegrationConnector::Lineage::FileBasedOpenLineageLogStore",
                                      "File-based Open Lineage Log Store Integration Connector",
                                      "Connector that stores open lineage events to the file system.",
                                      "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/integration-connectors/openlineage-integration-connectors",
                                      "org.odpi.openmetadata.adapters.connectors.integration.openlineage.FileBasedOpenLineageLogStoreProvider",
                                      ComponentDevelopmentStatus.STABLE,
                                      DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                      DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType()),

    GOVERNANCE_ACTION_OPEN_LINEAGE_INTEGRATION_CONNECTOR(522,
                                                         "de7320e7-3928-4266-8552-06a860533b99",
                                                         "Egeria::IntegrationConnector::Lineage::GovernanceActionOpenLineage",
                                                         "Governance Action to Open Lineage Integration Connector",
                                                         "Connector to listen for governance actions executing in the open metadata ecosystem, " +
                                                                 "generate open lineage events for them and publish them to any integration " +
                                                                 "connectors running in the same instance of the integration daemon.",
                                                         "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/integration-connectors/openlineage-integration-connectors",
                                                         "org.odpi.openmetadata.adapters.connectors.integration.openlineage.GovernanceActionOpenLineageIntegrationProvider",
                                                         ComponentDevelopmentStatus.STABLE,
                                                         DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                                         DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType()),

    OPEN_LINEAGE_CATALOGUER(523,
                            "60c80f78-552d-42e3-b0a4-00131869996a",
                            "Egeria::IntegrationConnector::Lineage::OpenLineageCataloguer",
                            "Open Lineage Cataloguer Integration Connector",
                            "Connector to register an OpenLineage listener with the integration daemon and " +
                                    "to catalog any processes that are not already known to the open metadata ecosystem.",
                            "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/integration-connectors/openlineage-integration-connectors",
                            "org.odpi.openmetadata.adapters.connectors.integration.openlineage.OpenLineageCataloguerIntegrationProvider",
                            ComponentDevelopmentStatus.STABLE,
                            DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                            DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType()),

    OPEN_LINEAGE_EVENT_RECEIVER_INTEGRATION_CONNECTOR(524,
                                                      "20a7cfe0-e2c1-4ce6-9c06-2d7005553d23",
                                                      "Egeria::IntegrationConnector::Lineage::OpenLineageEventReceiver",
                                                      "Open Lineage Event Receiver Integration Connector",
                                                      "Connector to receive and publish open lineage events from an event broker topic and publish" +
                                                              "them to lineage integration connectors with listeners registered in the same " +
                                                              "instance of the integration daemon.",
                                                      "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/integration-connectors/openlineage-integration-connectors",
                                                      "org.odpi.openmetadata.adapters.connectors.integration.openlineage.OpenLineageEventReceiverIntegrationProvider",
                                                      ComponentDevelopmentStatus.STABLE,
                                                      DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                                      DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType()),

    CSV_LINEAGE_IMPORTER_INTEGRATION_CONNECTOR(526,
                                               "b6e5fda2-f5ef-4fc9-861f-2e724e74e6ac",
                                               "Egeria::IntegrationConnector::Files::CSVLineageImporter",
                                               "CSV Lineage Importer Integration Connector",
                                               "Connector reads the content of a CSV file that contains lineage information and catalogues the content.",
                                               "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/integration-connectors/csv-lineage-import-integration-connector",
                                               "org.odpi.openmetadata.adapters.connectors.integration.csvlineageimporter.CSVLineageImporterProvider",
                                               ComponentDevelopmentStatus.STABLE,
                                               DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                               DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType()),

    DATA_FILES_MONITOR_INTEGRATION_CONNECTOR(527,
                                             "bbbd2213-dee1-4a21-8951-68f0f6d35eb7",
                                             "Egeria::IntegrationConnector::Files::DataFilesMonitor",
                                             "Data Files Monitor Integration Connector",
                                             "Connector supports cataloguing of files under a specific directory (folder) in the file system.",
                                             "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/integration-connectors/files-integration-connectors",
                                             "org.odpi.openmetadata.adapters.connectors.integration.basicfiles.DataFilesMonitorIntegrationProvider",
                                             ComponentDevelopmentStatus.STABLE,
                                             DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                             DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType()),

    DATA_FOLDER_MONITOR_INTEGRATION_CONNECTOR(528,
                                              "6718d248-5e0c-4e32-9d38-187318caea70",
                                              "Egeria::IntegrationConnector::Files::DataFolderMonitor",
                                              "Data Folder Monitor Integration Connector",
                                              "Connector maintains a DataFolder asset by monitoring the file directory where it is located.",
                                              "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/integration-connectors/files-integration-connectors",
                                              "org.odpi.openmetadata.adapters.connectors.integration.basicfiles.DataFolderMonitorIntegrationProvider",
                                              ComponentDevelopmentStatus.STABLE,
                                              DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                              DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType()),

    OM_ARCHIVE_FILES_MONITOR_INTEGRATION_CONNECTOR(529,
                                                   "67ed3803-9e14-4a5a-93f3-8d358f3d1ac4",
                                                   "Egeria::IntegrationConnector::Files::OpenMetadataArchiveFilesMonitor",
                                                   "Open Metadata Archive Files Monitor Integration Connector",
                                                   "Connector supports cataloguing of Open Metadata Archive files under a specific directory (folder) in the file system.",
                                                   "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/integration-connectors/files-integration-connectors",
                                                   "org.odpi.openmetadata.adapters.connectors.integration.basicfiles.OMArchiveFilesMonitorIntegrationProvider",
                                                   ComponentDevelopmentStatus.STABLE,
                                                   DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                                   DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType()),

    CSV_FILE_SURVEY_SERVICE(530,
                            "2a844ac9-bb86-4765-9f3c-04df148c05a5",
                            "Egeria::SurveyActionService::CSVFileSurveyService",
                            "CSV File Survey Action Service Connector",
                            "Connector supports the schema extraction and profiling of data in a CSV file.",
                            "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/file-survey-connectors",
                            "org.odpi.openmetadata.adapters.connectors.surveyaction.surveycsv.CSVSurveyServiceProvider",
                            ComponentDevelopmentStatus.STABLE,
                            DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName(),
                            DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType()),

    FILE_SURVEY_SERVICE(531,
                        "0c06ebb3-0a8f-476f-b8f8-602c01643523",
                        "Egeria::SurveyActionService::FileSurveyService",
                        "File Survey Action Service Connector",
                        "Connector supports the extraction of basic file properties.",
                        "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/file-survey-connectors",
                        "org.odpi.openmetadata.adapters.connectors.surveyaction.surveyfile.FileSurveyServiceProvider",
                        ComponentDevelopmentStatus.STABLE,
                        DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName(),
                        DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType()),

    FOLDER_SURVEY_SERVICE(532,
                          "297ede10-a004-4aa6-9af3-55e400551531",
                          "Egeria::SurveyActionService::FolderSurveyService",
                          "Folder Survey Action Service Connector",
                          "Connector supports the surveying of files in a directory (folder) and the directories beneath it.",
                          "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/file-survey-connectors",
                          "org.odpi.openmetadata.adapters.connectors.surveyaction.surveyfolder.FolderSurveyServiceProvider",
                          ComponentDevelopmentStatus.STABLE,
                          DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName(),
                          DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType()),

    OM_SECRETS_FILES_MONITOR_INTEGRATION_CONNECTOR(533,
                                                   "7c58a1ae-32ee-46f6-a651-a569579151d7",
                                                   "Egeria::IntegrationConnector::Files::SecretsKeyStoreFilesMonitor",
                                                   "Open Metadata Secrets Key Store Files Monitor Integration Connector",
                                                   "Connector supports cataloguing of Secrets Store files under a specific directory (folder) in the file system.",
                                                   "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/integration-connectors/files-integration-connectors",
                                                   "org.odpi.openmetadata.adapters.connectors.integration.basicfiles.OMSecretsFilesMonitorIntegrationProvider",
                                                   ComponentDevelopmentStatus.STABLE,
                                                   DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                                   DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType()),

    ;


    private final int                        connectorComponentId;
    private final String                     connectorTypeGUID;
    private final String                     connectorQualifiedName;
    private final String                     connectorDisplayName;
    private final String                     connectorDescription;
    private final String                     connectorWikiPage;
    private final String                     connectorProviderClassName;
    private final ComponentDevelopmentStatus connectorDevelopmentStatus;
    private final String                     supportedAssetTypeName;
    private final String                     supportedDeployedImplementationType;


    /**
     * Constructor for an open connector definition.
     *
     * @param connectorComponentId                connector component identifier
     * @param connectorTypeGUID                   connector type unique identifier
     * @param connectorQualifiedName              connector type qualified name
     * @param connectorDisplayName                connector type display name
     * @param connectorDescription                connector type description
     * @param connectorWikiPage                   connector wiki page
     * @param connectorProviderClassName          connector provider class name
     * @param connectorDevelopmentStatus          connector development status
     * @param supportedAssetTypeName              connector supported asset type name
     * @param supportedDeployedImplementationType connector supported deployed implementation type
     */
    EgeriaOpenConnectorDefinition(int connectorComponentId,
                                  String connectorTypeGUID,
                                  String connectorQualifiedName,
                                  String connectorDisplayName,
                                  String connectorDescription,
                                  String connectorWikiPage,
                                  String connectorProviderClassName,
                                  ComponentDevelopmentStatus connectorDevelopmentStatus,
                                  String supportedAssetTypeName,
                                  String supportedDeployedImplementationType)
    {
        this.connectorComponentId                = connectorComponentId;
        this.connectorTypeGUID                   = connectorTypeGUID;
        this.connectorQualifiedName              = connectorQualifiedName;
        this.connectorDisplayName                = connectorDisplayName;
        this.connectorDescription                = connectorDescription;
        this.connectorWikiPage                   = connectorWikiPage;
        this.connectorProviderClassName          = connectorProviderClassName;
        this.connectorDevelopmentStatus          = connectorDevelopmentStatus;
        this.supportedAssetTypeName              = supportedAssetTypeName;
        this.supportedDeployedImplementationType = supportedDeployedImplementationType;
    }


    /**
     * Return the component identifier for the connector.
     *
     * @return int
     */
    @Override
    public int getConnectorComponentId()
    {
        return connectorComponentId;
    }


    /**
     * Return the unique identifier for the connector type.
     *
     * @return string
     */
    @Override
    public String getConnectorTypeGUID()
    {
        return connectorTypeGUID;
    }


    /**
     * Return the qualified name of the connector type.
     *
     * @return string
     */
    @Override
    public String getConnectorQualifiedName()
    {
        return connectorQualifiedName;
    }


    /**
     * Return the display name of the connector type.
     *
     * @return string
     */
    @Override
    public String getConnectorDisplayName()
    {
        return connectorDisplayName;
    }


    /**
     * Return the description of the connector type.
     *
     * @return string
     */
    @Override
    public String getConnectorDescription()
    {
        return connectorDescription;
    }


    /**
     * Return the link to the wiki page for this connector.
     *
     * @return string
     */
    @Override
    public String getConnectorWikiPage()
    {
        return connectorWikiPage;
    }


    /**
     * Return the class name of the connector provider that implements this connector.
     *
     * @return string
     */
    @Override
    public String getConnectorProviderClassName()
    {
        return connectorProviderClassName;
    }


    /**
     * Return the development status of the connector.
     *
     * @return ComponentDevelopmentStatus enum
     */
    @Override
    public ComponentDevelopmentStatus getConnectorDevelopmentStatus()
    {
        return connectorDevelopmentStatus;
    }


    /**
     * Return the name of the asset type that this connector supports.
     *
     * @return string
     */
    @Override
    public String getSupportedAssetTypeName()
    {
        return supportedAssetTypeName;
    }


    /**
     * Return the name of the deployed implementation that this connector supports.
     *
     * @return string
     */
    @Override
    public String getSupportedDeployedImplementationType()
    {
        return supportedDeployedImplementationType;
    }


    /**
     * Return the component description for this connector.
     * This is used to initialize the audit log for the connector.
     *
     * @return AuditLogReportingComponent
     */
    @Override
    public AuditLogReportingComponent getComponentDescription()
    {
        return new AuditLogReportingComponent(connectorComponentId,
                                              connectorDevelopmentStatus,
                                              connectorDisplayName,
                                              connectorDescription,
                                              connectorWikiPage);
    }
}
