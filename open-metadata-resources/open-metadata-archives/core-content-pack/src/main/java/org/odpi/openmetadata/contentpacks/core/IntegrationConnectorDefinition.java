/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.contentpacks.core;

import org.odpi.openmetadata.adapters.connectors.controls.AtlasDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ApacheAtlasIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.apachekafka.integration.KafkaTopicIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.babbage.BabbageAnalyticalEngineProvider;
import org.odpi.openmetadata.adapters.connectors.controls.PostgresDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.OMSecretsFilesMonitorIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.jacquard.solutionblueprint.ProductSolutionComponent;
import org.odpi.openmetadata.adapters.connectors.controls.MSSQLDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.liskov.DataSharingHubManagerProvider;
import org.odpi.openmetadata.adapters.connectors.mendel.MendelAutomatedDuplicateManagerProvider;
import org.odpi.openmetadata.adapters.connectors.mendel.controls.MendelConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.controls.DB2LUWDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.db2luw.catalog.DB2LUWServerIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.db2luw.controls.DB2LUWConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.controls.DuckDBDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.duckdb.catalog.DuckDBDatabaseIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.duckdb.controls.DuckDBConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.mssql.catalog.MSSQLServerIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.mssql.controls.MSSQLConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.controls.OracleDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.oracle.catalog.OracleServerIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.oracle.controls.OracleConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStorePurpose;
import org.odpi.openmetadata.adapters.connectors.controls.EgeriaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.catalog.OMAGServerPlatformCatalogProvider;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.DataFilesMonitorIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.DataFolderMonitorIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.OMArchiveFilesMonitorIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.JDBCIntegrationConnectorProvider;
import org.odpi.openmetadata.adapters.connectors.integration.openapis.OpenAPIMonitorIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.openlineage.*;
import org.odpi.openmetadata.adapters.connectors.integration.smartcollections.SmartCollectionsIntegrationConnectorProvider;
import org.odpi.openmetadata.adapters.connectors.postgres.catalog.PostgresServerIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.jacquard.JacquardIntegrationConnectorProvider;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.sync.OSSUnityCatalogInsideCatalogSyncProvider;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.sync.OSSUnityCatalogServerSyncProvider;
import org.odpi.openmetadata.adapters.connectors.controls.KafkaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ResourceUse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Describes the standard integration connector configuration shipped with Egeria.  They are all defined under the default integration group
 */
public enum IntegrationConnectorDefinition
{
    /**
     * Catalogs files found under the sample-data directory (folder).
     */
    SAMPLE_DATA_CATALOGUER("cd6479e1-2fe7-4426-b358-8a0cf70be117",
                           "SampleDataFilesMonitorIntegrationConnector",
                           "Catalogs files found under the sample-data directory (folder).",
                           DataFilesMonitorIntegrationProvider.class.getName(),
                           "SampleDataCataloguer",
                           "sampledatacatnpa",
                           null,
                           null,
                           "loading-bay/sample-data",
                           getAllFileCataloguerConfigProperties(),
                           5*24*7, // Every 5 days
                           new DeployedImplementationTypeDefinition[]{DeployedImplementationType.FILE_SYSTEM_DIRECTORY},
                           "acebfa6f-f3a6-4fe3-a467-963a4e5bf0d6",
                           "Sample Data Files Monitor",
                           "Catalogs any files added to the 'loading-bay/sample-data' directory.  This is used to demonstrate how different types of files are catalog in open metadata.  Just drop the files that you want to experiment with into the directory anf they will be catalogued.  Use the Asset Maker API to query the results.",
                           true,
                           null,
                           null,
                           null,
                           null,
                           IntegrationGroupDefinition.FILES,
                           ContentPackDefinition.FILES_CONTENT_PACK),

    CONTENT_PACK_CATALOGUER("6bb2181e-7724-4515-ba3c-877cded55980",
                            "ContentPacksMonitorIntegrationConnector",
                            "Catalogs Open Metadata Archive files found under the content-packs directory (folder) and any other folder added as a catalog target.",
                            OMArchiveFilesMonitorIntegrationProvider.class.getName(),
                            "ContentPacksCataloguer",
                            "contentpackcatnpa",
                            null,
                            null,
                            "content-packs",
                            getFileCataloguerConfigProperties(),
                            60,
                            new DeployedImplementationTypeDefinition[]{DeployedImplementationType.FILE_SYSTEM_DIRECTORY},
                            "c67172bb-bd66-4d22-9ed7-ee6dc9c99b38",
                            "Content Packs Monitor",
                            "Catalogs open metadata archive files located in the 'content-packs' directory.  This includes cataloguing the header information from the archive file.  This includes a description of its content.  The resulting open metadata elements are used to list the content packs that are available to load into the Open Metadata Ecosystem.",
                            true,
                            null,
                            null,
                            null,
                            null,
                            IntegrationGroupDefinition.FILES,
                            ContentPackDefinition.FILES_CONTENT_PACK),

    SECRETS_STORE_CATALOGUER("75e55161-4633-49f4-9358-77b6810aa2d1",
                            "SecretsStoreMonitorIntegrationConnector",
                            "Catalogs Secrets Stores found under the secrets directory (folder) and any other folder added as a catalog target.",
                            OMSecretsFilesMonitorIntegrationProvider.class.getName(),
                            "SecretsStoreCataloguer",
                            "secretsstorecatnpa",
                            null,
                             null,
                            "secrets",
                            getFileCataloguerConfigProperties(),
                            60,
                            new DeployedImplementationTypeDefinition[]{DeployedImplementationType.FILE_SYSTEM_DIRECTORY},
                            "469ccdaf-17b8-4c6d-8713-5f66004f1cff",
                            "Secrets Store Monitor",
                            "Catalogs secrets store files located in the 'secrets' directory.  This includes cataloguing the secrets collections and security access controls.  User accounts are linked to UserIdentity elements if defined in open metadata.",
                            true,
                            null,
                            null,
                            null,
                            null,
                             IntegrationGroupDefinition.FILES,
                            ContentPackDefinition.FILES_CONTENT_PACK),

    GENERAL_FOLDER_CATALOGUER("1b98cdac-dd0a-4621-93db-99ef5a1098bc",
                              "GeneralFilesMonitorIntegrationConnector",
                              "Catalogs files found under the directories (folders) listed in the catalog targets.",
                              DataFilesMonitorIntegrationProvider.class.getName(),
                              "FilesCataloguer",
                              "filescatnpa",
                              null,
                              null,
                              null,
                              getFileCataloguerConfigProperties(),
                              60,
                              new DeployedImplementationTypeDefinition[]{DeployedImplementationType.FILE_SYSTEM_DIRECTORY},
                              "aa8914ac-b557-4595-b248-e1f252b77cf2",
                              "Catalog Files in Directory (Folder)",
                              "Create an open metadata representation of the files in the directories linked to this connector through the CatalogTarget relationship.",
                              true,
                              null,
                              null,
                              null,
                              null,
                              IntegrationGroupDefinition.FILES,
                              ContentPackDefinition.FILES_CONTENT_PACK),

    MAINTAIN_LAST_UPDATE_CATALOGUER("fd26f07c-ae44-4bc5-b457-37b43112224f",
                                    "MaintainDataFolderLastUpdateDateIntegrationConnector",
                                    "Maintains the last update date in a data folder asset based on the file activity in the resource's directory.",
                                    DataFolderMonitorIntegrationProvider.class.getName(),
                                    "MaintainLastUpdateDate",
                                    "datafoldercatnpa",
                                    null,
                                    null,
                                    null,
                                    getAllFileCataloguerConfigProperties(),
                                    60,
                                    new DeployedImplementationTypeDefinition[]{DeployedImplementationType.DATA_FOLDER},
                                    "871aa27d-a74b-48f5-9c29-ddbcd09c94c7",
                                    "Maintain DataFolder's storeUpdateTime",
                                    "Monitors changes to the files in the directory associated with a DataFolder and maintains the storeUpdateTime attribute in the DataFolder with the data/time that ome of the files was created/changed.",
                                    true,
                                    null,
                                    null,
                                    null,
                                    null,
                                    IntegrationGroupDefinition.FILES,
                                    ContentPackDefinition.FILES_CONTENT_PACK),

    JDBC_CATALOGUER("70dcd0b7-9f06-48ad-ad44-ae4d7a7762aa",
                    "JDBCIntegrationConnector",
                    "Catalogs JDBC database schemas, tables and columns attached as catalog targets.",
                    JDBCIntegrationConnectorProvider.class.getName(),
                    "JDBCDatabaseCataloguer",
                    "dbcatnpa",
                    null,
                    null,
                    null,
                    null,
                    60,
                    new DeployedImplementationTypeDefinition[]{DeployedImplementationType.JDBC_RELATIONAL_DATABASE},
                    "855a6c95-ee48-4f79-a7ce-c0b660012d30",
                    "JDBC Database Cataloguer",
                    "Maintains the open metadata elements that represent the schemas, tables and columns in a JDBC relational database.",
                    true,
                    null,
                    null,
                    null,
                    null,
                    IntegrationGroupDefinition.DATABASE,
                    ContentPackDefinition.CORE_CONTENT_PACK),


    POSTGRES_SERVER_CATALOGUER("36f69fd0-54ba-4f59-8a44-11ccf2687a34",
                               "PostgreSQLServerIntegrationConnector",
                               "Catalogs the databases found in PostgreSQL Servers attached as catalog targets.",
                               PostgresServerIntegrationProvider.class.getName(),
                               "PostgreSQLServerCataloguer",
                               "postgrescatnpa",
                               null,
                               null,
                               null,
                               getPostgreSQLServerConfigProperties(),
                               60,
                               new DeployedImplementationTypeDefinition[]{PostgresDeployedImplementationType.POSTGRESQL_SERVER},
                               "871d318b-5556-4d41-9552-6f0cbc7f411e",
                               "PostgreSQL Server Cataloguer",
                               "Maintains the open metadata elements that represent the databases in a PostgreSQL server.  Hands off the cataloguing of the schemas, tables and columns of each database to the 'PostgreSQL Database Cataloguer'.",
                               true,
                               null,
                               null,
                               null,
                               null,
                               IntegrationGroupDefinition.POSTGRES,
                               ContentPackDefinition.POSTGRES_CONTENT_PACK),


    MSSQL_SERVER_CATALOGUER("d34a538f-36ff-4ded-8203-1c3e8f440967",
                            "MSSQLServerIntegrationConnector",
                            "Catalogs the databases found in Microsoft SQL Servers attached as catalog targets.",
                            MSSQLServerIntegrationProvider.class.getName(),
                            "MSSQLServerCataloguer",
                            "mssqlcatnpa",
                            null,
                            null,
                            null,
                            getMSSQLServerConfigProperties(),
                            60,
                            new DeployedImplementationTypeDefinition[]{MSSQLDeployedImplementationType.MSSQL_SERVER},
                            "7054dd4f-129f-41e1-b0fd-3e0537ae6b7e",
                            "Microsoft SQL Server Cataloguer",
                            "Maintains the open metadata elements that represent the databases in a Microsoft SQL Server.  Hands off the cataloguing of the schemas, tables and columns of each database to the generic JDBC Database Cataloguer.",
                            true,
                            null,
                            null,
                            null,
                            null,
                            IntegrationGroupDefinition.MSSQL,
                            ContentPackDefinition.MSSQL_CONTENT_PACK),


    ORACLE_SERVER_CATALOGUER("64cc2ff9-beab-4624-b90a-b7299eba7231",
                             "OracleServerIntegrationConnector",
                             "Catalogs the pluggable databases found in Oracle Database Servers attached as catalog targets.",
                             OracleServerIntegrationProvider.class.getName(),
                             "OracleServerCataloguer",
                             "oraclecatnpa",
                             null,
                             null,
                             null,
                             getOracleServerConfigProperties(),
                             60,
                             new DeployedImplementationTypeDefinition[]{OracleDeployedImplementationType.ORACLE_SERVER},
                             "9483efdb-ab28-436e-959b-04e4442f940a",
                             "Oracle Database Server Cataloguer",
                             "Maintains the open metadata elements that represent the pluggable databases in an Oracle Database Server.  Hands off the cataloguing of the schemas, tables and columns of each pluggable database to the generic JDBC Database Cataloguer.",
                             true,
                             null,
                             null,
                             null,
                             null,
                             IntegrationGroupDefinition.ORACLE,
                             ContentPackDefinition.ORACLE_CONTENT_PACK),


    DB2LUW_SERVER_CATALOGUER("5e573a72-eed3-47a9-bf1f-8d31a1c493bd",
                             "DB2LUWServerIntegrationConnector",
                             "Catalogs the databases found in Db2 for Linux, UNIX and Windows Servers attached as catalog targets.",
                             DB2LUWServerIntegrationProvider.class.getName(),
                             "DB2LUWServerCataloguer",
                             "db2luwcatnpa",
                             null,
                             null,
                             null,
                             getDB2LUWServerConfigProperties(),
                             60,
                             new DeployedImplementationTypeDefinition[]{DB2LUWDeployedImplementationType.DB2LUW_SERVER},
                             "9be0d341-3764-4f0e-9d6c-e6875c71118e",
                             "Db2 for Linux, UNIX and Windows Server Cataloguer",
                             "Maintains the open metadata elements that represent the databases in a Db2 for Linux, UNIX and Windows Server.  Hands off the cataloguing of the schemas, tables and columns of each database to the generic JDBC Database Cataloguer.",
                             true,
                             null,
                             null,
                             null,
                             null,
                             IntegrationGroupDefinition.DB2LUW,
                             ContentPackDefinition.DB2LUW_CONTENT_PACK),


    DUCKDB_DATABASE_CATALOGUER("ecbd4f92-6376-49ee-abf5-244cd76dd211",
                               "DuckDBDatabaseIntegrationConnector",
                               "Catalogs the schema, tables and columns of a DuckDB database attached as a catalog target.",
                               DuckDBDatabaseIntegrationProvider.class.getName(),
                               "DuckDBDatabaseCataloguer",
                               "duckdbcatnpa",
                               null,
                               null,
                               null,
                               getDuckDBDatabaseConfigProperties(),
                               60,
                               new DeployedImplementationTypeDefinition[]{DuckDBDeployedImplementationType.DUCKDB_DATABASE},
                               "7d4f5272-30c6-46c5-b7af-7c7f7fb882c6",
                               "DuckDB Database Cataloguer",
                               "Maintains the open metadata elements that represent a DuckDB database.  Hands off the cataloguing of the schemas, tables and columns of the database to the generic JDBC Database Cataloguer.",
                               true,
                               null,
                               null,
                               null,
                               null,
                               IntegrationGroupDefinition.DUCKDB,
                               ContentPackDefinition.DUCKDB_CONTENT_PACK),


    APACHE_ATLAS_EXCHANGE("5721627a-2dd4-4f95-a274-6cfb128edb97",
                          "ApacheAtlasServerIntegrationConnector",
                          "Exchanges metadata with Apache Atlas metadata catalogs.",
                          ApacheAtlasIntegrationProvider.class.getName(),
                          "ApacheAtlasExchange",
                          "atlascatnpa",
                          null,
                          null,
                          null,
                          null,
                          60,
                          new DeployedImplementationTypeDefinition[]{AtlasDeployedImplementationType.APACHE_ATLAS_SERVER},
                          "174a5f66-eb58-4a67-b886-288988b8e330",
                          "Apache Atlas Exchange",
                          "Proves a two-way synchronization of metadata between the open metadata ecosystem and Apache Atlas servers.",
                          true,
                          null,
                          null,
                          null,
                          null,
                          IntegrationGroupDefinition.APACHE_ATLAS,
                          ContentPackDefinition.APACHE_ATLAS_CONTENT_PACK),

    KAFKA_SERVER_CATALOGUER("fa1f711c-0b34-4b57-8e6e-16162b132b0c",
                            "KafkaTopicIntegrationConnector",
                            "Catalogs the topics found in Apache Kafka Servers attached as catalog targets.",
                            KafkaTopicIntegrationProvider.class.getName(),
                            "ApacheKafkaCataloguer",
                            "kafkacatnpa",
                            null,
                            null,
                            null,
                            null,
                            60,
                            new DeployedImplementationTypeDefinition[]{KafkaDeployedImplementationType.APACHE_KAFKA_SERVER},
                            "ac589ef1-c3b9-443c-856a-7b5be788e2ae",
                            "Apache Kafka Cataloguer",
                            "Catalogs the topics found in an Apache Kafka broker.",
                            true,
                            null,
                            null,
                            null,
                            null,
                            IntegrationGroupDefinition.APACHE_KAFKA,
                            ContentPackDefinition.APACHE_KAFKA_CONTENT_PACK),

    API_CATALOGUER("b89d9a5a-2ea6-49bc-a4fc-e7df9f3ca93e",
                   "OpenAPIIntegrationConnector",
                   "Catalogs the REST APIs found in servers attached as catalog targets that support the swagger API.",
                   OpenAPIMonitorIntegrationProvider.class.getName(),
                   "OpenAPICataloguer",
                   "apicatnpa",
                   null,
                   null,
                   null,
                   null,
                   7*24*60, // Weekly
                   new DeployedImplementationTypeDefinition[]{EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM},
                   "22bcb015-84ab-4503-9619-231896c33828",
                   "Open API Cataloguer",
                   "Maintains open metadata descriptions of Open API specification extracted from a running application/server/platform.",
                   true,
                   null,
                   null,
                   null,
                   null,
                   IntegrationGroupDefinition.APIS,
                   ContentPackDefinition.APIS_CONTENT_PACK),

    UC_CATALOG_CATALOGUER("74dde22f-2249-4ea3-af2b-b39e73f79b81",
                          "UnityCatalogAssetExchange",
                          "Synchronizes metadata information about the contents of catalogs found in the OSS Unity Catalog 'catalog of catalogs' with the open metadata ecosystem.",
                          OSSUnityCatalogInsideCatalogSyncProvider.class.getName(),
                          "UnityCatalogInsideCatalogSynchronizer",
                          "uccatcatnpa",
                          null,
                          null,
                          null,
                          null,
                          60,
                          new DeployedImplementationTypeDefinition[]{UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER},
                          "0102f808-2ee3-42d4-a769-95c891517876",
                          "Unity Catalog Asset Exchange",
                          "Exchanges the metadata found in a Unity Catalog Catalog with the open metadata ecosystem.  The open metadata ecosystem can provide enrichment of the description of Unity Catalog assets and search across Unity Catalog server instances.  It can also be used to provision new assets to Unity Catalog.",
                          true,
                          null,
                          null,
                          null,
                          null,
                          IntegrationGroupDefinition.UNITY_CATALOG,
                          ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK),

    UC_SERVER_CATALOGUER("06d068d9-9e08-4e67-8c59-073bbf1013af",
                         "UnityCatalogServerIntegrationConnector",
                         "Synchronizes metadata about the catalogs found in the OSS Unity Catalog 'catalog of catalogs' with the open metadata ecosystem.",
                         OSSUnityCatalogServerSyncProvider.class.getName(),
                         "UnityCatalogServerSynchronizer",
                         "ucservernpa",
                         null,
                         null,
                         null,
                         getUCServerConfigProperties(),
                         60,
                         new DeployedImplementationTypeDefinition[]{UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER},
                         "a8d557c8-c914-4d70-a04e-45a48119a670",
                         "Unity Catalog Server Exchange",
                         "Maintains the open metadata description of the catalogs found in the Unity Catalog servers linked through CatalogTarget relationships.  It hands off the cataloguing of the assets within each catalog to the 'Unity Catalog Asset Exchange'.  New catalogs linked to the metadata element for a Unity Catalog server are automatically provisioned to the corresponding Unity Catalog server instance.",
                         true,
                         null,
                         null,
                         null,
                         null,
                         IntegrationGroupDefinition.UNITY_CATALOG,
                         ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK),

    OMAG_SERVER_PLATFORM_CATALOGUER("dee84e6e-7a96-4975-86c1-152fb3ab759b",
                                    "OMAGServerPlatformIntegrationConnector",
                                    "Catalogs the OMAG Servers known to the OMAG Server Platforms catalogued in the open metadata ecosystem.",
                                    OMAGServerPlatformCatalogProvider.class.getName(),
                                    "OMAGServerPlatformCataloguer",
                                    "omagspcatnpa",
                                    null,
                                    null,
                                    null,
                                    null,
                                    60,
                                    new DeployedImplementationTypeDefinition[]{EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM},
                                    "2eed7ad8-9189-4971-ba67-2de94efc7db3",
                                    "OMAG Server Platform Cataloguer",
                                    "Monitors the running OMAG Server Platform instances that are catalogued in the open metadata ecosystem and maintains the metadata that describes the servers and their configuration.",
                                    true,
                                    "OMAGServerPlatformCataloguer",
                                    SecretsStorePurpose.REST_BEARER_TOKEN.getName(),
                                    new YAMLSecretsStoreProvider().getConnectorType().getGUID(),
                                    "secrets/egeria-servers.omsecrets",
                                    IntegrationGroupDefinition.EGERIA,
                                    ContentPackDefinition.EGERIA_CONTENT_PACK),

    OPEN_LINEAGE_API_PUBLISHER("2156bc98-973a-4859-908d-4ccc96f53cc5",
                               "OpenLineageAPIPublisherIntegrationConnector",
                               "Publishes open lineage events to the APIs attached as catalog targets.",
                               APIBasedOpenLineageLogStoreProvider.class.getName(),
                               "OpenLineageAPIPublisher",
                               "olapipubnpa",
                               null,
                               null,
                               null,
                               null,
                               60,
                               new DeployedImplementationTypeDefinition[]{
                                       DeployedImplementationType.MARQUEZ_SERVER
                               },
                               "f993be00-e07f-452c-96d0-22813f5f9db6",
                               "Open Lineage API Publisher",
                               "Publishes open lineage events to APIs linked through CatalogTarget relationships.  These open lineage events may have been received from third party processes or were generated from running Governance Action Processes in the open metadata ecosystem.",
                               false,
                               null,
                               null,
                               null,
                               null,
                               IntegrationGroupDefinition.OPEN_LINEAGE,
                               ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK),

    OPEN_LINEAGE_FILE_PUBLISHER("6271b678-7d22-4cdf-87b1-45b366beaf4e",
                                "OpenLineageFilePublisherIntegrationConnector",
                                "Publishes open lineage events as JSON files to each of the file directories attached as catalog targets.",
                                FileBasedOpenLineageLogStoreProvider.class.getName(),
                                "OpenLineageFilePublisher",
                                "olfilepubnpa",
                                null,
                                null,
                                "logs/openlineage",
                                null,
                                60,
                                new DeployedImplementationTypeDefinition[]{
                                        DeployedImplementationType.FILE_SYSTEM_DIRECTORY},
                                "ed596c5f-0908-4266-b378-55963f0afc09",
                                "Open Lineage File Publisher",
                                "Publishes open lineage events as JSON files to each of the file directories attached as catalog targets.",
                                false,
                                null,
                                null,
                                null,
                                null,
                                IntegrationGroupDefinition.OPEN_LINEAGE,
                                ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK),

    OPEN_LINEAGE_GA_PUBLISHER("206f8faf-04da-4b6f-8280-eeee3943afeb",
                              "OpenLineageGovernanceActionPublisherIntegrationConnector",
                              "Publishes open lineage events whenever governance actions run in the open metadata ecosystem.",
                              GovernanceActionOpenLineageIntegrationProvider.class.getName(),
                              "OpenLineageGovernanceActionPublisher",
                              "olgapubnpa",
                              null,
                              null,
                              null,
                              null,
                              60,
                              null,
                              "df6a2737-c574-4383-85e7-9e0708ea9f62",
                              "Open Lineage Governance Action Publisher",
                              "Publishes open lineage events whenever governance actions run in the open metadata ecosystem.",
                              true,
                              null,
                              null,
                              null,
                              null,
                              IntegrationGroupDefinition.OPEN_LINEAGE,
                              ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK),

    OPEN_LINEAGE_CATALOGUER("3347ac71-8dd2-403a-bc16-75a71be64bd7",
                            "OpenLineageCataloguerIntegrationConnector",
                            "Catalogs the resources detailed in the open lineage events received by the integration daemon.",
                            OpenLineageCataloguerIntegrationProvider.class.getName(),
                            "OpenLineageCataloguer",
                            "olcatnpa",
                            null,
                            null,
                            null,
                            null,
                            60,
                            null,
                            "dc0c7870-4da4-41bc-ad65-98d38d563a4d",
                            "Open Lineage Cataloguer",
                            "Catalogs the resources detailed in the open lineage events received from third party systems.",
                            true,
                            null,
                            null,
                            null,
                            null,
                            IntegrationGroupDefinition.OPEN_LINEAGE,
                            ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK),

    OPEN_LINEAGE_KAFKA_LISTENER("980b989c-de78-4e6a-a58d-51049d7381bf",
                                "OpenLineageKafkaListenerIntegrationConnector",
                                "Receives the open lineage events published to the Apache Kafka topics attached as catalog targets.",
                                OpenLineageEventReceiverIntegrationProvider.class.getName(),
                                "OpenLineageKafkaListener",
                                "olkafkainnpa",
                                null,
                                null,
                                null,
                                null,
                                60,
                                null,
                                "638322ff-3c26-4259-8c46-70f969fbe5cd",
                                "Open Lineage Kafka Listener",
                                "Receives open lineage events from the open lineage proxy.  It listens for events from Apache Kafka topics attached as catalog targets.",
                                false,
                                null,
                                null,
                                null,
                                null,
                                IntegrationGroupDefinition.OPEN_LINEAGE,
                                ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK),

    PRODUCT_HARVESTER("8a3d91da-05a0-49ca-83e0-77f5c48bdf0c",
                      "JacquardDigitalProductLoomIntegrationConnector",
                      "Defines and maintains digital products based on the content of the open metadata repositories.",
                      JacquardIntegrationConnectorProvider.class.getName(),
                      "JacquardDigitalProductLoom",
                      "jacquardnpa",
                      null,
                      null,
                      null,
                      null,
                      60,
                      null,
                      ProductSolutionComponent.JACQUARD_HARVESTER.getGUID(),
                      ProductSolutionComponent.JACQUARD_HARVESTER.getDisplayName(),
                      ProductSolutionComponent.JACQUARD_HARVESTER.getDescription(),
                      true,
                      "JacquardDigitalProductLoom",
                      SecretsStorePurpose.REST_BEARER_TOKEN.getName(),
                      new YAMLSecretsStoreProvider().getConnectorType().getGUID(),
                      "secrets/egeria-servers.omsecrets",
                      IntegrationGroupDefinition.JACQUARD,
                      ContentPackDefinition.PRODUCTS_CONTENT_PACK),

    BABBAGE_ANALYTICAL_ENGINE("a2c281e0-3c8d-4621-bb4d-8f6dff756d1a",
                              "BabbageAnalyticalEngineIntegrationConnector",
                              "Initiates analytical processing using the Governance Action Types that are attached as catalog targets.",
                              BabbageAnalyticalEngineProvider.class.getName(),
                              "BabbageAnalyticalEngine",
                              "babbagenpa",
                              null,
                              null,
                              null,
                              null,
                              60*24, // 24 hours
                              null,
                              "2e7ec1b4-a8ba-4be6-b345-2c1735a94c7a",
                              "Babbage Analytical Engine",
                              "Initiates analytical processing that generates statistics on the content and operation of the open metadata ecosystem.  The analytical processors are called lovelace analytical services.",
                              true,
                              null,
                              null,
                              null,
                              null,
                              IntegrationGroupDefinition.BABBAGE,
                              ContentPackDefinition.CORE_CONTENT_PACK),

    MENDEL_AUTOMATED_DUPLICATE_MANAGER("f5f0a1ba-1f24-4a9d-b96e-cb00fa89c1f5",
                                       "MendelAutomatedDuplicateManagerIntegrationConnector",
                                       "Manages the duplicate links and classifications for the elements that have been detected as duplicates.",
                                       MendelAutomatedDuplicateManagerProvider.class.getName(),
                                       "MendelAutomatedDuplicateManager",
                                       "mendelnpa",
                                       null,
                                       null,
                                       null,
                                       getMendelConfigProperties(),
                                       60*24, // 24 hours
                                       null,
                                       "2d499775-4a9c-45db-b07e-630aec9d6166",
                                       "Automated Duplicate Manager",
                                       "Reviews the duplicate links that have been detected in the open metadata ecosystem.  It confirms the close matches, asks a steward to decide on the rest, retires the classifications of the duplicates that a steward has separated, and consolidates the clusters of confirmed duplicates.",
                                       true,
                                       null,
                                       null,
                                       null,
                                       null,
                                       IntegrationGroupDefinition.MENDEL,
                                       ContentPackDefinition.CORE_CONTENT_PACK),

    LISKOV_DATA_SHARING_HUB_MANAGER("a38e7f2b-a672-419e-95b4-d650d9bb5c92",
                                    "LiskovDataSharingHubManagerIntegrationConnector",
                                    "Manages the content of the data dictionary for a data sharing hub.",
                                    DataSharingHubManagerProvider.class.getName(),
                                    "DataSharingHubManager",
                                    "liskovnpa",
                                    null,
                                    null,
                                    null,
                                    null,
                                    60*24, // 24 hours
                                    null,
                                    "805026d3-d873-4f11-ab44-5477862aadcf",
                                    "Data Sharing Hub Manager",
                                    "Builds a data dictionary for a data sharing hub by scavenging information from the data sharing hub stores.  This includes schema information and the survey reports.",
                                    true,
                                    null,
                                    null,
                                    null,
                                    null,
                                    IntegrationGroupDefinition.LISKOV,
                                    ContentPackDefinition.CORE_CONTENT_PACK),

    SMART_COLLECTIONS_MEMBERSHIP_MANAGER("217bd925-b50d-4596-9ade-1d7a940e8874",
                              "SmartCollectionsIntegrationConnector",
                              "Maintains the membership of Results Set collections by running their attached saved query and linking/unlinking members to match the results.",
                              SmartCollectionsIntegrationConnectorProvider.class.getName(),
                              "SmartCollectionsMembershipManager",
                              "smartcnpa",
                              null,
                              null,
                              null,
                              null,
                              30,
                              null,
                              "204af0dc-1e8c-4050-aad9-c565f5bdcf72",
                              "Smart Collections Membership Manager",
                              "Maintains the membership of Results Set collections linked as catalog targets by navigating to their Saved Query, issuing its query and linking/unlinking members to match the elements returned.",
                              true,
                              "EgeriaServers",
                              SecretsStorePurpose.REST_BEARER_TOKEN.getName(),
                              new YAMLSecretsStoreProvider().getConnectorType().getGUID(),
                              "secrets/egeria-servers.omsecrets",
                              IntegrationGroupDefinition.SMART_COLLECTIONS,
                              ContentPackDefinition.CORE_CONTENT_PACK),
    ;


    /**
     * Get the configuration properties for the Unity Catalog Server Cataloguer.
     *
     * @return map
     */
    /**
     * Return the configuration properties for the Mendel Automated Duplicate Manager.
     *
     * @return map
     */
    private static Map<String, Object> getMendelConfigProperties()
    {
        Map<String, Object> configurationProperties = new HashMap<>();

        configurationProperties.put(MendelConfigurationProperty.DUPLICATE_CLUSTER_SIZE.getName(),
                                    MendelConfigurationProperty.DEFAULT_DUPLICATE_CLUSTER_SIZE);

        return configurationProperties;
    }


    private static Map<String, Object> getUCServerConfigProperties()
    {
        Map<String, Object> configurationProperties = new HashMap<>();
        configurationProperties.put(UnityCatalogConfigurationProperty.FRIENDSHIP_GUID.getName(), UC_CATALOG_CATALOGUER.getGUID());

        return configurationProperties;
    }


    /**
     * Get the configuration properties for the PostgreSQL Server Cataloguer.
     *
     * @return map
     */
    private static Map<String, Object> getPostgreSQLServerConfigProperties()
    {
        Map<String, Object> configurationProperties = new HashMap<>();
        configurationProperties.put(PostgresConfigurationProperty.FRIENDSHIP_GUID.getName(), JDBC_CATALOGUER.getGUID());

        return configurationProperties;
    }


    /**
     * Get the configuration properties for the Microsoft SQL Server Cataloguer.
     *
     * @return map
     */
    private static Map<String, Object> getMSSQLServerConfigProperties()
    {
        Map<String, Object> configurationProperties = new HashMap<>();
        configurationProperties.put(MSSQLConfigurationProperty.FRIENDSHIP_GUID.getName(), JDBC_CATALOGUER.getGUID());

        return configurationProperties;
    }


    /**
     * Get the configuration properties for the Oracle Database Server Cataloguer.
     *
     * @return map
     */
    private static Map<String, Object> getOracleServerConfigProperties()
    {
        Map<String, Object> configurationProperties = new HashMap<>();
        configurationProperties.put(OracleConfigurationProperty.FRIENDSHIP_GUID.getName(), JDBC_CATALOGUER.getGUID());

        return configurationProperties;
    }


    /**
     * Get the configuration properties for the Db2 for Linux, UNIX and Windows Server Cataloguer.
     *
     * @return map
     */
    private static Map<String, Object> getDB2LUWServerConfigProperties()
    {
        Map<String, Object> configurationProperties = new HashMap<>();
        configurationProperties.put(DB2LUWConfigurationProperty.FRIENDSHIP_GUID.getName(), JDBC_CATALOGUER.getGUID());

        return configurationProperties;
    }


    /**
     * Get the configuration properties for the DuckDB Database Cataloguer.
     *
     * @return map
     */
    private static Map<String, Object> getDuckDBDatabaseConfigProperties()
    {
        Map<String, Object> configurationProperties = new HashMap<>();
        configurationProperties.put(DuckDBConfigurationProperty.FRIENDSHIP_GUID.getName(), JDBC_CATALOGUER.getGUID());

        return configurationProperties;
    }


    /**
     * Get the configuration properties for the file cataloguers.
     *
     * @return map
     */
    private static Map<String, Object> getFileCataloguerConfigProperties()
    {
        Map<String, Object> configurationProperties = new HashMap<>();
        configurationProperties.put("waitForDirectory", "true");

        return configurationProperties;
    }


    /**
     * Get the configuration properties for the file cataloguers that catalog all files whether
     * they are classified or not.
     *
     * @return map
     */
    private static Map<String, Object> getAllFileCataloguerConfigProperties()
    {
        Map<String, Object> configurationProperties = getFileCataloguerConfigProperties();
        configurationProperties.put("catalogAllFiles", "true");

        return configurationProperties;
    }


    private final String                                 guid;
    private final String                                 displayName;
    private final String                                 description;
    private final String                                 connectorProviderClassName;
    private final String                                 connectorName;
    private final String                                 connectorUserId;
    private final String                                 metadataSourceQualifiedName;
    private final String                                 metadataCollectionQualifiedName;
    private final String                                 endpointAddress;
    private final Map<String, Object>                    configurationProperties;
    private final long                                   refreshTimeInterval;
    private final DeployedImplementationTypeDefinition[] deployedImplementationTypes;
    private final String                                 solutionComponentGUID;
    private final String                                 solutionComponentName;
    private final String                                 solutionComponentDescription;
    private final boolean                                linkToMetadataServerSolutionComponent;
    private final String                                 secretsCollectionName;
    private final String                                 secretsStorePurpose;
    private final String                                 secretsStoreConnectorTypeGUID;
    private final String                                 secretsStoreFileName;
    private final IntegrationGroupDefinition             integrationGroupDefinition;
    private final ContentPackDefinition                  contentPackDefinition;


    /**
     *
     * @param guid unique identifier for the integration connector
     * @param displayName display name
     * @param description description of the connector's behaviour
     * @param connectorProviderClassName provider class name for the implementation
     * @param connectorName name of the connector when it is running (stored in relationship between group and connector)
     * @param connectorUserId runtime userId (stored in relationship between group and connector)
     * @param metadataSourceQualifiedName metadata source for newly created elements from this connector
     * @param metadataCollectionQualifiedName metadata collection for newly created elements from this connector
     * @param endpointAddress optional endpoint address for the connector's connection
     * @param configurationProperties configuration properties for the integration connector's connection
     * @param refreshTimeInterval how often should it refresh (initial value)
     * @param deployedImplementationTypes what deployed implementation types does this connector support?
     * @param solutionComponentGUID unique identifier of the solution component for the governance action type
     * @param solutionComponentName the display name of the solution component for the governance action type
     * @param solutionComponentDescription description of the solution component for the governance action type
     * @param linkToMetadataServerSolutionComponent should this component link to the open metadata server solution component
     * @param secretsCollectionName            name of the collection of secrets to use in the secrets store
     * @param secretsStorePurpose              type of authentication information provided by the secrets store
     * @param secretsStoreConnectorTypeGUID    optional connector type for secrets store
     * @param secretsStoreFileName             location of the secrets store
     * @param contentPackDefinition which content pack?
     */
    IntegrationConnectorDefinition(String                                 guid,
                                   String                                 displayName,
                                   String                                 description,
                                   String                                 connectorProviderClassName,
                                   String                                 connectorName,
                                   String                                 connectorUserId,
                                   String                                 metadataSourceQualifiedName,
                                   String                                 metadataCollectionQualifiedName,
                                   String                                 endpointAddress,
                                   Map<String, Object>                    configurationProperties,
                                   long                                   refreshTimeInterval,
                                   DeployedImplementationTypeDefinition[] deployedImplementationTypes,
                                   String                                 solutionComponentGUID,
                                   String                                 solutionComponentName,
                                   String                                 solutionComponentDescription,
                                   boolean                                linkToMetadataServerSolutionComponent,
                                   String                                 secretsCollectionName,
                                   String                                 secretsStorePurpose,
                                   String                                 secretsStoreConnectorTypeGUID,
                                   String                                 secretsStoreFileName,
                                   IntegrationGroupDefinition             integrationGroupDefinition,
                                   ContentPackDefinition                  contentPackDefinition)
    {
        this.guid                                  = guid;
        this.displayName                           = displayName;
        this.description                           = description;
        this.connectorProviderClassName            = connectorProviderClassName;
        this.connectorName                         = connectorName;
        this.connectorUserId                       = connectorUserId;
        this.metadataSourceQualifiedName           = metadataSourceQualifiedName;
        this.metadataCollectionQualifiedName       = metadataCollectionQualifiedName;
        this.endpointAddress                       = endpointAddress;
        this.configurationProperties               = configurationProperties;
        this.refreshTimeInterval                   = refreshTimeInterval;
        this.deployedImplementationTypes           = deployedImplementationTypes;
        this.solutionComponentGUID                 = solutionComponentGUID;
        this.solutionComponentName                 = solutionComponentName;
        this.solutionComponentDescription          = solutionComponentDescription;
        this.linkToMetadataServerSolutionComponent = linkToMetadataServerSolutionComponent;
        this.secretsCollectionName                 = secretsCollectionName;
        this.secretsStorePurpose                   = secretsStorePurpose;
        this.secretsStoreConnectorTypeGUID         = secretsStoreConnectorTypeGUID;
        this.secretsStoreFileName                  = secretsStoreFileName;
        this.integrationGroupDefinition            = integrationGroupDefinition;
        this.contentPackDefinition                 = contentPackDefinition;
    }


    /**
     * Return the unique identifier of the integration connector.
     *
     * @return string
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Return the unique name of the integration connector.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return integrationGroupDefinition.getQualifiedName() + "::" + displayName;
    }


    /**
     * Return the display name of the integration connector.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the description of the integration connector.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the class name of the connector provider.
     *
     * @return string
     */
    public String getConnectorProviderClassName()
    {
        return connectorProviderClassName;
    }


    /**
     * Return the name of the connector in the integration group
     *
     * @return string
     */
    public String getConnectorName()
    {
        return connectorName;
    }


    /**
     * Return the connector's userId.
     *
     * @return string
     */
    public String getConnectorUserId()
    {
        return connectorUserId;
    }


    /**
     * Return the optional metadata source qualified name for the connector.
     *
     * @return string
     */
    public String getMetadataSourceQualifiedName()
    {
        return metadataSourceQualifiedName;
    }


    /**
     * Return the optional metadata collection qualified name for the connector.
     *
     * @return string
     */
    public String getMetadataCollectionQualifiedName()
    {
        return metadataCollectionQualifiedName;
    }


    /**
     * Return the optional endpoint address for the first catalog target.
     *
     * @return string
     */
    public String getEndpointAddress()
    {
        return endpointAddress;
    }


    /**
     * Return the configuration properties.
     *
     * @return map
     */
    public Map<String, Object> getConfigurationProperties()
    {
        return configurationProperties;
    }


    /**
     * Return the refresh time interval (minutes)
     *
     * @return long
     */
    public long getRefreshTimeInterval()
    {
        return refreshTimeInterval;
    }


    /**
     * Return the qualified name for the deployed implementation type that this connector supports
     *
     * @return enum
     */
    public List<DeployedImplementationTypeDefinition> getDeployedImplementationTypes()
    {
        if (deployedImplementationTypes == null)
        {
            return null;
        }

        return Arrays.asList(deployedImplementationTypes);
    }


    /**
     * Return the deployed implementation type that this connector supports
     *
     * @return enum
     */
    public ResourceUse getResourceUse()
    {
        return ResourceUse.CATALOG_RESOURCE;
    }


    /**
     * Retrieves the GUID of the solution component for the governance action type.
     *
     * @return string
     */
    public String getSolutionComponentGUID()
    {
        return solutionComponentGUID;
    }


    /**
     * Retrieves the name of the solution component for the governance action type.
     *
     * @return string
     */
    public String getSolutionComponentName()
    {
        return solutionComponentName;
    }


    /**
     * Retrieves the description of the solution component for the governance action type.
     *
     * @return string
     */
    public String getSolutionComponentDescription()
    {
        return solutionComponentDescription;
    }


    /**
     * Retrieves whether this component should link to the open metadata server solution component
     *
     * @return boolean
     */
    public boolean linkToMetadataServerSolutionComponent()
    {
        return linkToMetadataServerSolutionComponent;
    }


    /**
     * Return the name of the secrets collection to use to locate this asset's secrets.
     *
     * @return name
     */
    public String getSecretsCollectionName()
    {
        return secretsCollectionName;
    }


    /**
     * Return the purpose of the secrets store.
     *
     * @return name
     */
    public String getSecretsStorePurpose()
    {
        return secretsStorePurpose;
    }

    /**
     * Return the optional secrets store connector provider for the server.
     *
     * @return connector provider
     */
    public String getSecretsStoreConnectorTypeGUID()
    {
        return secretsStoreConnectorTypeGUID;
    }


    /**
     * Return the location of the secrets store.
     *
     * @return path name of the file
     */
    public String getSecretsStoreFileName()
    {
        return secretsStoreFileName;
    }


    /**
     * Return the integration group definition.
     *
     * @return integration group definition
     */
    public IntegrationGroupDefinition getIntegrationGroupDefinition()
    {
        return integrationGroupDefinition;
    }


    /**
     * Get identifier of content pack where this template should be located.
     *
     * @return content pack definition
     */
    public ContentPackDefinition getContentPackDefinition()
    {
        return contentPackDefinition;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "IntegrationConnectorDefinition{" + "name='" + connectorName + '\'' + "}";
    }
}
