/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors;

import org.odpi.openmetadata.adapters.connectors.controls.PostgresDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;


/**
 * Enumeration of the different types of connectors provided by the Egeria community.  This module also
 * provides the connector provider base class that performs the setup for a connector provider.
 */
public enum EgeriaOpenConnectorDefinition implements OpenConnectorDefinition
{
    FILE_BASED_CONFIG_STORE(60,
                            "39276d19-be00-4fdc-84cb-a21438fa4ad0",
                            "Egeria:RuntimeConnector:ConfigStore:File",
                            "File Based Server Config Store Connector",
                            "Connector supports storing of OMAG Server configuration document in a file.",
                            "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/adapters/open-connectors/configuration-store-connectors/configuration-file-store-connector",
                            "org.odpi.openmetadata.adapters.adminservices.configurationstore.file.FileBasedServerConfigStoreProvider",
                            ComponentDevelopmentStatus.STABLE,
                            OpenMetadataType.JSON_FILE.typeName,
                            null),

    IN_MEMORY_TOPIC_CONNECTOR(89,
                             "ed8e682b-2fec-4403-b551-02f8c46322ef",
                             "Egeria:RuntimeConnector:OpenMetadataTopicConnector:InMemory",
                             "In Memory Open Metadata Topic Connector",
                             "Connector supports publishing and subscribing to string based events using an in-memory topic.",
                             "https://egeria-project.org/connectors/resource/in-mem-open-metadata-topic-connector/",
                             "org.odpi.openmetadata.adapters.eventbus.inmemory.InMemoryOpenMetadataTopicProvider",
                              ComponentDevelopmentStatus.STABLE,
                              DeployedImplementationType.TOPIC.getAssociatedTypeName(),
                              DeployedImplementationType.TOPIC.getDeployedImplementationType()),

    KAFKA_TOPIC_CONNECTOR(90,
                          "3851e8d0-e343-400c-82cb-3918fed81da6",
                          "Egeria:OpenMetadataTopicConnector:Kafka",
                          "Apache Kafka Open Metadata Topic Connector",
                          "Apache Kafka Open Metadata Topic Connector supports string based events over an Apache Kafka event bus.",
                          "https://egeria-project.org/connectors/resource/kafka-open-metadata-topic-connector/",
                          "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider",
                          ComponentDevelopmentStatus.STABLE,
                          DeployedImplementationType.APACHE_KAFKA_TOPIC.getAssociatedTypeName(),
                          DeployedImplementationType.APACHE_KAFKA_TOPIC.getDeployedImplementationType()),

    JDBC_RESOURCE_CONNECTOR(93,
                            "64463b01-92f6-4d7b-9737-f1d20b2654f4",
                            "Egeria:ResourceConnector:RelationalDatabase:JDBC",
                            "Relational Database JDBC Connector",
                            "Connector supports access to relational databases using exclusively the JDBC API.  This includes both data and metadata.",
                            "https://egeria-project.org/connectors/resource/jdbc-resource-connector/",
                            "org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnectorProvider",
                            ComponentDevelopmentStatus.STABLE,
                            DeployedImplementationType.JDBC_RELATIONAL_DATABASE.getAssociatedTypeName(),
                            DeployedImplementationType.JDBC_RELATIONAL_DATABASE.getDeployedImplementationType()),

    BASIC_FILE_STORE_CONNECTOR(94,
                               "ba213761-f5f5-4cf5-a95f-6150aef09e0b",
                               "Egeria:ResourceConnector:DataFile",
                               "Basic File Store Connector",
                               "Connector supports reading of Files.",
                               "https://egeria-project.org/connectors/resource/basic-file-resource-connector/",
                               "org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStoreConnector",
                               ComponentDevelopmentStatus.STABLE,
                               DeployedImplementationType.FILE.getAssociatedTypeName(),
                               DeployedImplementationType.FILE.getDeployedImplementationType()),

    BASIC_FOLDER_CONNECTOR(95,
                           "a9fc9231-f04a-40c4-99b1-4a1058063f5e",
                           "Egeria:ResourceConnector:FileFolder",
                           "Basic Folder Connector",
                           "Connector supports reading of files in a directory (folder).",
                           "https://egeria-project.org/connectors/resource/basic-folder-resource-connector/",
                           "org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFolderConnector",
                           ComponentDevelopmentStatus.STABLE,
                           DeployedImplementationType.FILE_FOLDER.getAssociatedTypeName(),
                           DeployedImplementationType.FILE_FOLDER.getDeployedImplementationType()),

    CSV_FILE_STORE_CONNECTOR(96,
                             "108b85fe-d7b8-45c3-9fb8-742ac4e4fb14",
                             "Egeria:ResourceConnector:DataFile:CSV",
                             "CSV File Store Connector",
                             "Connector supports reading of CSV files.",
                             "https://egeria-project.org/connectors/resource/csv-file-resource-connector/",
                             "org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVFileStoreProvider",
                             ComponentDevelopmentStatus.STABLE,
                             DeployedImplementationType.CSV_FILE.getAssociatedTypeName(),
                             DeployedImplementationType.CSV_FILE.getDeployedImplementationType()),

    DATA_FOLDER_CONNECTOR(97,
                          "1ef9cbe2-9119-4ac0-b9ac-d838f0ed9caf",
                          "Egeria:ResourceConnector:FileFolder:DataFolder",
                          "Data Folder Connector",
                          "Connector supports reading of files in a directory (folder) that is used to store data.",
                          "https://egeria-project.org/connectors/resource/data-folder-resource-connector/",
                          "org.odpi.openmetadata.adapters.connectors.datastore.datafolder.DataFolderProvider",
                          ComponentDevelopmentStatus.STABLE,
                          DeployedImplementationType.DATA_FOLDER.getAssociatedTypeName(),
                          DeployedImplementationType.DATA_FOLDER.getDeployedImplementationType()),

    GLOSSARY_DYNAMIC_ARCHIVER_CONNECTOR(150,
                                        "02cfb290-43cb-497c-928e-267bd3d69324",
                                        "Egeria:ArchiveService:Glossary",
                                        "Glossary Dynamic Archiver Connector",
                                        "Connector supports dynamically archiving a glossary into an open metadata archive.",
                                        "https://egeria-project.org/connectors/resource/glossary-dynamic-archiver-connector/",
                                        "org.odpi.openmetadata.adapters.connectors.dynamicarchivers.glossary.GlossaryDynamicArchiverProvider",
                                        ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                        DeployedImplementationType.REPOSITORY_GOVERNANCE_SERVICE_CONNECTOR.getAssociatedTypeName(),
                                        DeployedImplementationType.REPOSITORY_GOVERNANCE_SERVICE_CONNECTOR.getDeployedImplementationType()),

    JDBC_INTEGRATION_CONNECTOR(661,
                               "49cd6772-1efd-40bb-a1d9-cc9460962ff6",
                               "Egeria:IntegrationConnector:RelationalDatabase:JDBC",
                               "JDBC Relational Database Integration Connector",
                               "This connector retrieves schema information about a relational database's tables and columns and catalogs them in the open metadata ecosystem.",
                               "https://egeria-project.org/connectors/integration/jdbc-integration-connector",
                               "org.odpi.openmetadata.adapters.connectors.integration.jdbc.JDBCIntegrationConnectorProvider",
                               ComponentDevelopmentStatus.STABLE,
                               DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                               DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType()),

    POSTGRES_SERVER_INTEGRATION_CONNECTOR(672,
                                          "71b84d4f-aaa7-4a01-892c-2c60e66d31a4",
                                          "Egeria:IntegrationConnector:DataManagerCatalog:PostgreSQLServer",
                                          "Catalog databases in PostgreSQL database server",
                                          "Catalogs the databases found in a PostgreSQL database server.  This includes the RelationalDatabase asset element plus a connection with the JDBC Resource connector type.",
                                          "https://egeria-project.org/connectors/databases/postgres-database-server-integration-connector/",
                                          "org.odpi.openmetadata.adapters.connectors.postgres.catalog.PostgresServerIntegrationProvider",
                                          ComponentDevelopmentStatus.STABLE,
                                          DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                          DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType()),

    POSTGRES_SERVER_SURVEY_ACTION_SERVICE(673,
                                          "3e47db62-5407-4cbd-ba54-1ce6612af6f9",
                                          "Egeria:SurveyActionService:DataManagerCatalog:PostgreSQLServer",
                                          "Survey for a PostgreSQL Database Server",
                                          "Surveys the databases, their tables and columns, found in a PostgreSQL database server",
                                          "https://egeria-project.org/connectors/databases/postgres-database-server-survey-action-service/",
                                          "org.odpi.openmetadata.adapters.connectors.postgres.survey.PostgresServerSurveyActionProvider",
                                          ComponentDevelopmentStatus.TECHNICAL_PREVIEW,
                                          DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName(),
                                          DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType()),

    POSTGRES_DATABASE_SURVEY_ACTION_SERVICE(674,
                                            "225f53a4-be5e-4008-b3a9-5dcf25f92514",
                                            "Egeria:SurveyActionService:DataManagerCatalog:PostgreSQLDatabase",
                                            "Survey for a PostgreSQL Database",
                                            "Surveys the tables and columns found in a PostgreSQL database server",
                                            "https://egeria-project.org/connectors/databases/postgres-database-survey-action-service/",
                                            "org.odpi.openmetadata.adapters.connectors.postgres.survey.PostgresDatabaseSurveyActionProvider",
                                            ComponentDevelopmentStatus.STABLE,
                                            DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName(),
                                            DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType()),

    YAML_SECRETS_STORE_CONNECTOR(662,
                                 "e507dfdc-2f03-4746-a274-d3027a8e9153",
                                 "Egeria:SecretsStoreConnector:YAMLFile",
                                 "YAML File Secrets Store Connector",
                                 "Connector retrieves secrets from environment variables.",
                                 "https://egeria-project.org/connectors/secrets/yaml-file-secrets-store-connector/",
                                 "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider",
                                 ComponentDevelopmentStatus.STABLE,
                                 OpenMetadataType.KEY_STORE_FILE.typeName,
                                 null),

    OSS_UNITY_CATALOG_RESOURCE_CONNECTOR(690,
                                         "0df7ec59-aa05-46fd-a090-3d879f869eff",
                                         "Egeria:ResourceConnector:DataManagerCatalog:UnityCatalog",
                                         "OSS Unity Catalog REST Connector",
                                         "Connector that provides access to the Unity Catalog REST API",
                                         "https://egeria-project.org/connectors/unity-catalog/resource-connector/",
                                         "org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceProvider",
                                         ComponentDevelopmentStatus.STABLE,
                                         UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getAssociatedTypeName(),
                                         UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getDeployedImplementationType()),

    OSS_UNITY_CATALOG_SERVER_SURVEY_ACTION_SERVICE(691,
                                                   "0b110ed0-df5a-4537-abcb-dbdadae26168",
                                                   "Egeria:SurveyActionService:DataManagerCatalog:UnityCatalog:Server",
                                                   "OSS Unity Catalog (UC) Server Survey Service",
                                                   "Surveys the catalogs found in a Unity Catalog Server.",
                                                   "https://egeria-project.org/connectors/unity-catalog/server-survey-service/",
                                                   "org.odpi.openmetadata.adapters.connectors.unitycatalog.survey.OSSUnityCatalogServerSurveyProvider",
                                                   ComponentDevelopmentStatus.STABLE,
                                                   DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName(),
                                                   DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType()),

    OSS_UNITY_CATALOG_INSIDE_CATALOG_SURVEY_ACTION_SERVICE(692,
                                                           "6cc7c135-eada-440f-97d8-704c8d0a508f",
                                                           "Egeria:SurveyActionService:DataManagerCatalog:UnityCatalog:InsideCatalog",
                                                           "OSS Unity Catalog (UC) Inside Catalog Survey Action Service",
                                                           "Surveys the contents of a catalog found in a Unity Catalog Server.",
                                                           "https://egeria-project.org/connectors/unity-catalog/catalog-survey-service/",
                                                           "org.odpi.openmetadata.adapters.connectors.unitycatalog.survey.OSSUnityCatalogInsideCatalogSurveyProvider",
                                                           ComponentDevelopmentStatus.STABLE,
                                                           DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName(),
                                                           DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType()),

    OSS_UNITY_CATALOG_SERVER_SYNC_INTEGRATION_CONNECTOR(693,
                                                        "211ab462-e08e-41d4-9e18-052f8a37d669",
                                                        "Egeria:IntegrationConnector:SoftwareServer:UnityCatalog",
                                                        "OSS Unity Catalog (UC) Server Synchronizing Connector",
                                                        "Connector that synchronizes the catalogs between the OSS Unity Catalog 'catalog of catalogs' and the open metadata ecosystem.",
                                                        "https://egeria-project.org/connectors/unity-catalog/sync-server-connector/",
                                                        "org.odpi.openmetadata.adapters.connectors.unitycatalog.sync.OSSUnityCatalogServerSyncProvider",
                                                        ComponentDevelopmentStatus.STABLE,
                                                        DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName(),
                                                        DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType()),

    OSS_UNITY_CATALOG_INSIDE_CATALOG_SYNC_INTEGRATION_CONNECTOR(694,
                                                                "7767df9a-9d2f-49e1-bf61-8b3f88b11fd0",
                                                                "Egeria:IntegrationConnector:DataManagerCatalog:UnityCatalog:InsideCatalogSync",
                                                                "OSS Unity Catalog (UC) Inside a Catalog Synchronizing Connector",
                                                                "Connector supports the synchronization of a catalog from a Unity Catalog Server.",
                                                                "https://egeria-project.org/connectors/sync-action/unity-catalog/sync-catalog-connector/",
                                                                "org.odpi.openmetadata.adapters.connectors.unitycatalog.sync.OSSUnityCatalogInsideCatalogSyncProvider",
                                                                ComponentDevelopmentStatus.STABLE,
                                                                DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                                                DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType()),

    OSS_UNITY_CATALOG_INSIDE_SCHEMA_SURVEY_ACTION_SERVICE(695,
                                                          "cde2cd15-500c-4f30-b54b-99b29e7e9cb6",
                                                          "Egeria:SurveyActionService:DataManagerCatalog:UnityCatalog:InsideSchema",
                                                          "OSS Unity Catalog (UC) Inside Schema Survey Action Service",
                                                          "Surveys the contents of a schema found in a Unity Catalog Server.",
                                                          "https://egeria-project.org/connectors/unity-catalog/schema-survey-service/",
                                                          "org.odpi.openmetadata.adapters.connectors.unitycatalog.survey.OSSUnityCatalogInsideSchemaSurveyProvider",
                                                          ComponentDevelopmentStatus.STABLE,
                                                          DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName(),
                                                          DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType()),

    OSS_UNITY_CATALOG_INSIDE_VOLUME_SURVEY_ACTION_SERVICE(696,
                                                          "5a9f3813-2cc7-46ac-a1a8-b2b508d07100",
                                                          "Egeria:SurveyActionService:DataManagerCatalog:UnityCatalog:InsideVolume",
                                                          "OSS Unity Catalog (UC) Inside a Volume Survey Service",
                                                          "Connector supports the surveying of files in a Unity Catalog Volume's directory (folder) and the directories beneath it.",
                                                          "https://egeria-project.org/connectors/survey-action/unity-catalog/volume-survey-service/",
                                                          "org.odpi.openmetadata.adapters.connectors.unitycatalog.survey.OSSUnityCatalogInsideVolumeSurveyProvider",
                                                          ComponentDevelopmentStatus.STABLE,
                                                          DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName(),
                                                          DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType()),

    PROVISION_UNITY_CATALOG_GOVERNANCE_ACTION_SERVICE(698,
                                                      "58681cd6-ded2-488b-be8d-031e42cb345c",
                                                      "Egeria:GovernanceActionService:DataManagerCatalog:UnityCatalog:Provision",
                                                      "Provision Unity Catalog Governance Action Service",
                                                      "Governance Action Service that provisions resources to an OSS Unity Catalog server.",
                                                      "https://egeria-project.org/connectors/unity-catalog/provision-governance-action-service/",
                                                      "org.odpi.openmetadata.adapters.connectors.unitycatalog.provision.ProvisionUnityCatalogGovernanceActionProvider",
                                                      ComponentDevelopmentStatus.STABLE,
                                                      DeployedImplementationType.GOVERNANCE_SERVICE.getAssociatedTypeName(),
                                                      DeployedImplementationType.GOVERNANCE_SERVICE.getDeployedImplementationType()),

    POSTGRES_TABULAR_DATA_SET_CONNECTOR(703,
                                        "92bdcf05-96d3-4660-936d-2a8ae15c2c14",
                                        "Egeria:ResourceConnector:TabularDataSet:PostgreSQL",
                                        "PostgreSQL Tabular Data Set Connector",
                                        "Connector supports reading/writing of data to a PostgreSQL table.",
                                        "https://egeria-project.org/connectors/resource/postgres-tabular-data-set/",
                                        "org.odpi.openmetadata.adapters.connectors.postgres.tabulardatasets.PostgresTabularDataSetProvider",
                                        ComponentDevelopmentStatus.STABLE,
                                        PostgresDeployedImplementationType.POSTGRESQL_TABULAR_DATA_SET.getAssociatedTypeName(),
                                        PostgresDeployedImplementationType.POSTGRESQL_TABULAR_DATA_SET.getDeployedImplementationType()),

    POSTGRES_TABULAR_DATA_SET_COLLECTION_CONNECTOR(711,
                                                   "25a44f44-fee6-4334-acab-282a09bbc924",
                                                   "Egeria:ResourceConnector:TabularDataSetCollection:PostgreSQL",
                                                   "PostgreSQL Tabular Data Set Collection Connector",
                                                   "Connector manages a collection of tabular data sets from a PostgreSQL database server.",
                                                   "https://egeria-project.org/connectors/resource/tabular-data-set-collection/postgresql/",
                                                   "org.odpi.openmetadata.adapters.connectors.postgres.tabulardatasets.PostgresTabularDataSetCollectionProvider",
                                                   ComponentDevelopmentStatus.STABLE,
                                                   PostgresDeployedImplementationType.POSTGRESQL_TABULAR_DATA_SET_COLLECTION.getAssociatedTypeName(),
                                                   PostgresDeployedImplementationType.POSTGRESQL_TABULAR_DATA_SET_COLLECTION.getDeployedImplementationType()),

    CSV_TABULAR_DATA_SET_COLLECTION_CONNECTOR(712,
                                              "32c25bc2-e0bf-4d78-87ab-ed3c5aead169",
                                              "Egeria:ResourceConnector:TabularDataSetCollection:CSVFile",
                                              "CSV Tabular Data Set Collection Connector",
                                              "Connector manages a collection of tabular data sets stored in CSV files.",
                                              "https://egeria-project.org/connectors/resource/tabular-data-set-collection/csv/",
                                              "org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVTabularDataSetCollectionProvider",
                                              ComponentDevelopmentStatus.STABLE,
                                              DeployedImplementationType.CSV_TABULAR_DATA_SET_COLLECTION.getAssociatedTypeName(),
                                              DeployedImplementationType.CSV_TABULAR_DATA_SET_COLLECTION.getDeployedImplementationType()),

    CSV_TABULAR_DATA_SET_CONNECTOR(713,
                                   "fabaf243-e0ed-4f30-9df0-1cba38d90df4",
                                   "Egeria:ResourceConnector:TabularDataSet:CSV",
                                   "CSV Tabular Data Set Connector",
                                   "Connector supports reading/writing of data to a CSV table.",
                                   "https://egeria-project.org/connectors/resource/csv-tabular-data-set/",
                                   "org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVTabularDataSetProvider",
                                   ComponentDevelopmentStatus.STABLE,
                                   DeployedImplementationType.CSV_TABULAR_DATA_SET.getAssociatedTypeName(),
                                   DeployedImplementationType.CSV_TABULAR_DATA_SET.getDeployedImplementationType()),

    OPEN_METADATA_TYPES_LIST_TABULAR_DATA_SET(717,
                                              "c67aed34-284e-4a4f-8293-0cfb65cb0321",
                                              "Egeria:ResourceConnector:TabularDataSet:OpenMetadataTypesList",
                                              "Open Metadata Types List Tabular Data Set Connector",
                                              "Connector manages the list of open metadata types as if it was a tabular data set.",
                                              "https://egeria-project.org/connectors/resource/tabular-data-set/open-metadata-types-list/",
                                              "org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.openmetadatatypes.OpenMetadataTypesListProvider",
                                              ComponentDevelopmentStatus.STABLE,
                                              OpenMetadataType.REFERENCE_DATA_SET.typeName,
                                              null),

    VALID_METADATA_VALUE_SET_LIST_TABULAR_DATA_SET(715,
                                                   "bb25f6a4-bb02-4dcc-bf8b-9e1e4f0fe879",
                                                   "Egeria:ResourceConnector:TabularDataSet:ValidMetadataValueSetList",
                                                   "Valid Metadata Value Set List Tabular Data Set Connector",
                                                   "Connector manages the list of valid metadata value sets as if it was a tabular data set.",
                                                   "https://egeria-project.org/connectors/resource/tabular-data-set/valid-metadata-value-set-list/",
                                                   "org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.validmetadatavalues.ValidMetadataValueSetListProvider",
                                                   ComponentDevelopmentStatus.STABLE,
                                                   OpenMetadataType.REFERENCE_DATA_SET.typeName,
                                                   null),

    VALID_METADATA_VALUE_TABULAR_DATA_SET(716,
                                          "381d1d1a-d498-4e47-a555-7004984a63c7",
                                          "Egeria:ResourceConnector:TabularDataSet:ValidMetadataValues",
                                          "Valid Metadata Values Tabular Data Set Connector",
                                          "Connector manages an open metadata valid value set for a particular property as if it was a tabular data set.",
                                          "https://egeria-project.org/connectors/resource/tabular-data-set/valid-metadata-values/",
                                          "org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.validmetadatavalues.ValidMetadataValueDataSetProvider",
                                          ComponentDevelopmentStatus.STABLE,
                                          OpenMetadataType.REFERENCE_DATA_SET.typeName,
                                          null),

    REFERENCE_DATA_LIST_TABULAR_DATA_SET(705,
                                         "e2870eda-cba3-406e-b44a-42b3364d2316",
                                         "Egeria:ResourceConnector:TabularDataSet:ReferenceDataSetList",
                                         "Reference Data Set List Tabular Data Set Connector",
                                         "Connector manages the list of reference data sets as if it was a tabular data set.",
                                         "https://egeria-project.org/connectors/resource/tabular-data-set/reference-data-set-list/",
                                         "org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.referencedata.ReferenceDataSetListProvider",
                                         ComponentDevelopmentStatus.STABLE,
                                         OpenMetadataType.REFERENCE_DATA_SET.typeName,
                                         null),

    REFERENCE_DATA_TABULAR_DATA_SET(707,
                                    "2755cb53-91c3-472f-9f3a-2f6f8269fb7e",
                                    "Egeria:ResourceConnector:TabularDataSet:ReferenceDataSet",
                                    "Reference Data Set Tabular Data Set Connector",
                                    "Connector manages an open metadata reference data set as if it was a tabular data set.",
                                    "https://egeria-project.org/connectors/resource/tabular-data-set/reference-data-set/",
                                    "org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.referencedata.ReferenceDataSetProvider",
                                    ComponentDevelopmentStatus.STABLE,
                                    OpenMetadataType.REFERENCE_DATA_SET.typeName,
                                    null),
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
    public int getConnectorComponentId()
    {
        return connectorComponentId;
    }


    /**
     * Return the unique identifier for the connector type.
     *
     * @return string
     */
    public String getConnectorTypeGUID()
    {
        return connectorTypeGUID;
    }


    /**
     * Return the qualified name of the connector type.
     *
     * @return string
     */
    public String getConnectorQualifiedName()
    {
        return connectorQualifiedName;
    }


    /**
     * Return the display name of the connector type.
     *
     * @return string
     */
    public String getConnectorDisplayName()
    {
        return connectorDisplayName;
    }


    /**
     * Return the description of the connector type.
     *
     * @return string
     */
    public String getConnectorDescription()
    {
        return connectorDescription;
    }


    /**
     * Return the link to the wiki page for this connector.
     *
     * @return string
     */
    public String getConnectorWikiPage()
    {
        return connectorWikiPage;
    }


    /**
     * Return the class name of the connector provider that implements this connector.
     *
     * @return string
     */
    public String getConnectorProviderClassName()
    {
        return connectorProviderClassName;
    }


    /**
     * Return the development status of the connector.
     *
     * @return ComponentDevelopmentStatus enum
     */
    public ComponentDevelopmentStatus getConnectorDevelopmentStatus()
    {
        return connectorDevelopmentStatus;
    }


    /**
     * Return the name of the asset type that this connector supports.
     *
     * @return string
     */
    public String getSupportedAssetTypeName()
    {
        return supportedAssetTypeName;
    }


    /**
     * Return the name of the deployed implementation that this connector supports.
     *
     * @return string
     */
    public String getSupportedDeployedImplementationType()
    {
        return supportedDeployedImplementationType;
    }
}
