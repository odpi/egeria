/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.contentpacks.core;

import org.odpi.openmetadata.adapters.connectors.apacheatlas.controls.AtlasDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ApacheAtlasIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.apachekafka.integration.KafkaTopicIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStorePurpose;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.EgeriaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.catalog.OMAGServerPlatformCatalogProvider;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.DataFilesMonitorIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.DataFolderMonitorIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.OMArchiveFilesMonitorIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.JDBCIntegrationConnectorProvider;
import org.odpi.openmetadata.adapters.connectors.integration.kafkaaudit.DistributeAuditEventsFromKafkaProvider;
import org.odpi.openmetadata.adapters.connectors.integration.openapis.OpenAPIMonitorIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.openlineage.*;
import org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestopenmetadata.HarvestOpenMetadataProvider;
import org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestsurveys.HarvestSurveysProvider;
import org.odpi.openmetadata.adapters.connectors.postgres.catalog.PostgresServerIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.productmanager.OpenMetadataProductsHarvesterProvider;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.sync.OSSUnityCatalogInsideCatalogSyncProvider;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.sync.OSSUnityCatalogServerSyncProvider;
import org.odpi.openmetadata.adapters.connectors.apachekafka.control.KafkaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
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
                           "loading-bay/sample-data",
                           getAllFileCataloguerConfigProperties(),
                           1440,
                           new String[]{DeployedImplementationType.FILE_FOLDER.getQualifiedName()},
                           "acebfa6f-f3a6-4fe3-a467-963a4e5bf0d6",
                           "Sample Data Files Monitor",
                           "Catalogs any files added to the 'loading-bay/sample-data' directory.  This is used to demonstrate how different types of files are catalog in open metadata.  Just drop the files that you want to experiment with into the directory anf they will be catalogued.  Use the Asset Maker API to query the results.",
                           null,
                           null,
                           null,
                           null,
                           ContentPackDefinition.FILES_CONTENT_PACK),

    CONTENT_PACK_CATALOGUER("6bb2181e-7724-4515-ba3c-877cded55980",
                            "ContentPacksMonitorIntegrationConnector",
                            "Catalogs Open Metadata Archive files found under the content-packs directory (folder) and any other folder added as a catalog target.",
                            OMArchiveFilesMonitorIntegrationProvider.class.getName(),
                            "ContentPacksCataloguer",
                            "contentpackcatnpa",
                            null,
                            "content-packs",
                            getFileCataloguerConfigProperties(),
                            60,
                            new String[]{DeployedImplementationType.FILE_FOLDER.getQualifiedName()},
                            "c67172bb-bd66-4d22-9ed7-ee6dc9c99b38",
                            "Content Packs Monitor",
                            "Catalogs open metadata archive files located in the 'content-packs' directory.  This includes cataloguing the header information from the archive file.  This includes a description of its content.  The resulting open metadata elements are used to list the content packs that are available to load into the Open Metadata Ecosystem.",
                            null,
                            null,
                            null,
                            null,
                            ContentPackDefinition.FILES_CONTENT_PACK),

    GENERAL_FOLDER_CATALOGUER("1b98cdac-dd0a-4621-93db-99ef5a1098bc",
                              "GeneralFilesMonitorIntegrationConnector",
                              "Catalogs files found under the directories (folders) listed in the catalog targets.",
                              DataFilesMonitorIntegrationProvider.class.getName(),
                              "FilesCataloguer",
                              "filescatnpa",
                              null,
                              null,
                              getFileCataloguerConfigProperties(),
                              60,
                              new String[]{DeployedImplementationType.FILE_FOLDER.getQualifiedName()},
                              "aa8914ac-b557-4595-b248-e1f252b77cf2",
                              "Catalog Files in Directory (Folder)",
                              "Create an open metadata representation of the files in the directories linked to this connector through the CatalogTarget relationship.",
                              null,
                              null,
                              null,
                              null,
                              ContentPackDefinition.FILES_CONTENT_PACK),

    MAINTAIN_LAST_UPDATE_CATALOGUER("fd26f07c-ae44-4bc5-b457-37b43112224f",
                                    "MaintainDataFolderLastUpdateDateIntegrationConnector",
                                    "Maintains the last update date in a data folder asset based on the file activity in the resource's directory.",
                                    DataFolderMonitorIntegrationProvider.class.getName(),
                                    "MaintainLastUpdateDate",
                                    "datafoldercatnpa",
                                    null,
                                    null,
                                    getAllFileCataloguerConfigProperties(),
                                    60,
                                    new String[]{DeployedImplementationType.DATA_FOLDER.getQualifiedName()},
                                    "871aa27d-a74b-48f5-9c29-ddbcd09c94c7",
                                    "Maintain DataFolder's storeUpdateTime",
                                    "Monitors changes to the files in the directory associated with a DataFolder and maintains the storeUpdateTime attribute in the DataFolder with the data/time that ome of the files was created/changed.",
                                    null,
                                    null,
                                    null,
                                    null,
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
                    60,
                    new String[]{DeployedImplementationType.JDBC_RELATIONAL_DATABASE.getQualifiedName()},
                    "855a6c95-ee48-4f79-a7ce-c0b660012d30",
                    "JDBC Database Cataloguer",
                    "Maintains the open metadata elements that represent the schemas, tables and columns in a JDBC relational database.",
                    null,
                    null,
                    null,
                    null,
                    ContentPackDefinition.CORE_CONTENT_PACK),

    POSTGRES_DB_CATALOGUER("ef301220-7dfe-4c6c-bb9d-8f92d9f63823",
                           "PostgreSQLDatabaseIntegrationConnector",
                           "Catalogs JDBC database schemas, tables and columns attached as catalog targets.",
                           JDBCIntegrationConnectorProvider.class.getName(),
                           "PostgreSQLDatabaseCataloguer",
                           "postgresdbcatnpa",
                           null,
                           null,
                           null,
                           60,
                           new String[]{PostgresDeployedImplementationType.POSTGRESQL_DATABASE.getQualifiedName()},
                           "b33fcf90-575f-4384-9ccd-07780688dc28",
                           "PostgreSQL Database Cataloguer",
                           "Maintains the open metadata elements that represent the schemas, tables and columns in a PostgreSQL relational database.",
                           null,
                           null,
                           null,
                           null,
                           ContentPackDefinition.POSTGRES_CONTENT_PACK),

    POSTGRES_SERVER_CATALOGUER("36f69fd0-54ba-4f59-8a44-11ccf2687a34",
                               "PostgreSQLServerIntegrationConnector",
                               "Catalogs the databases found in PostgreSQL Servers attached as catalog targets.",
                               PostgresServerIntegrationProvider.class.getName(),
                               "PostgreSQLServerCataloguer",
                               "postgrescatnpa",
                               null,
                               null,
                               getPostgreSQLServerConfigProperties(),
                               60,
                               new String[]{PostgresDeployedImplementationType.POSTGRESQL_SERVER.getQualifiedName()},
                               "871d318b-5556-4d41-9552-6f0cbc7f411e",
                               "Catalog PostgreSQL Server",
                               "Maintains the open metadata elements that represent the databases in a PostgreSQL server.  Hands off the cataloguing of the schemas, tables and columns of each database to the 'PostgreSQL Database Cataloguer'.",
                               null,
                               null,
                               null,
                               null,
                               ContentPackDefinition.POSTGRES_CONTENT_PACK),


    APACHE_ATLAS_EXCHANGE("5721627a-2dd4-4f95-a274-6cfb128edb97",
                               "ApacheAtlasServerIntegrationConnector",
                               "Exchanges metadata with Apache Atlas metadata catalogs.",
                               ApacheAtlasIntegrationProvider.class.getName(),
                               "ApacheAtlasExchange",
                               "atlascatnpa",
                               null,
                               null,
                               null,
                               60,
                               new String[]{AtlasDeployedImplementationType.APACHE_ATLAS_SERVER.getQualifiedName()},
                          "174a5f66-eb58-4a67-b886-288988b8e330",
                          "Apache Atlas Exchange",
                          "Proves a two-way synchronization of metadata between the open metadata ecosystem and Apache Atlas servers.",
                          null,
                          null,
                          null,
                          null,
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
                            60,
                            new String[]{KafkaDeployedImplementationType.APACHE_KAFKA_SERVER.getQualifiedName()},
                            "ac589ef1-c3b9-443c-856a-7b5be788e2ae",
                            "Apache Kafka Cataloguer",
                            "Catalogs the topics found in an Apache Kafka broker.",
                            null,
                            null,
                            null,
                            null,
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
                   60,
                   new String[]{EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getQualifiedName()},
                   "22bcb015-84ab-4503-9619-231896c33828",
                   "Open API Cataloguer",
                   "Maintains open metadata descriptions of Open API specification extracted from a running application/server/platform.",
                   null,
                   null,
                   null,
                   null,
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
                          60,
                          new String[]{UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getQualifiedName()},
                          "0102f808-2ee3-42d4-a769-95c891517876",
                          "Unity Catalog Asset Exchange",
                          "Exchanges the metadata found in a Unity Catalog Catalog with the open metadata ecosystem.  The open metadata ecosystem can provide enrichment of the description of Unity Catalog assets and search across Unity Catalog server instances.  It can also be used to provision new assets to Unity Catalog.",
                          null,
                          null,
                          null,
                          null,
                          ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK),

    UC_SERVER_CATALOGUER("06d068d9-9e08-4e67-8c59-073bbf1013af",
                         "UnityCatalogServerIntegrationConnector",
                         "Synchronizes metadata about the catalogs found in the OSS Unity Catalog 'catalog of catalogs' with the open metadata ecosystem.",
                         OSSUnityCatalogServerSyncProvider.class.getName(),
                         "UnityCatalogServerSynchronizer",
                         "ucservernpa",
                         null,
                         null,
                         getUCServerConfigProperties(),
                         60,
                         new String[]{UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getQualifiedName()},
                         "a8d557c8-c914-4d70-a04e-45a48119a670",
                         "Unity Catalog Server Exchange",
                         "Maintains the open metadata description of the catalogs found in the Unity Catalog servers linked through CatalogTarget relationships.  It hands off the cataloguing of the assets within each catalog to the 'Unity Catalog Asset Exchange'.  New catalogs linked to the metadata element for a Unity Catalog server are automatically provisioned to the corresponding Unity Catalog server instance.",
                         null,
                         null,
                         null,
                         null,
                         ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK),

    OMAG_SERVER_PLATFORM_CATALOGUER("dee84e6e-7a96-4975-86c1-152fb3ab759b",
                                    "OMAGServerPlatformIntegrationConnector",
                                    "Catalogs the OMAG Servers known to the OMAG Server Platforms catalogued in the open metadata ecosystem.",
                                    OMAGServerPlatformCatalogProvider.class.getName(),
                                    "OMAGServerPlatformCataloguer",
                                    "omagspcatnpa",
                                    null,
                                    null,
                                    getOMAGCataloguerConfigProperties(),
                                    60,
                                    new String[]{EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getQualifiedName()},
                                    "2eed7ad8-9189-4971-ba67-2de94efc7db3",
                                    "OMAG Server Platform Cataloguer",
                                    "Monitors the running OMAG Server Platform instances that are catalogued in the open metadata ecosystem and maintains the metadata that describes the servers and their configuration.",
                                    null,
                                    null,
                                    null,
                                    null,
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
                               60,
                               new String[]{DeployedImplementationType.MARQUEZ_SERVER.getQualifiedName()},
                               "f993be00-e07f-452c-96d0-22813f5f9db6",
                               "Open Lineage API Publisher",
                               "Publishes open lineage events to APIs linked through CatalogTarget relationships.  These open lineage events may have been received from third party processes or were generated from running Governance Action Processes in the open metadata ecosystem.",
                               null,
                               null,
                               null,
                               null,
                               ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK),

    OPEN_LINEAGE_FILE_PUBLISHER("6271b678-7d22-4cdf-87b1-45b366beaf4e",
                                "OpenLineageFilePublisherIntegrationConnector",
                                "Publishes open lineage events as JSON files to each of the file directories attached as catalog targets.",
                                FileBasedOpenLineageLogStoreProvider.class.getName(),
                                "OpenLineageFilePublisher",
                                "olfilepubnpa",
                                null,
                                "logs/openlineage",
                                null,
                                60,
                                new String[]{DeployedImplementationType.FILE_FOLDER.getQualifiedName(), DeployedImplementationType.DATA_FOLDER.getQualifiedName()},
                                "ed596c5f-0908-4266-b378-55963f0afc09",
                                "Open Lineage File Publisher",
                                "Publishes open lineage events as JSON files to each of the file directories attached as catalog targets.",
                                null,
                                null,
                                null,
                                null,
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
                              60,
                              null,
                              "df6a2737-c574-4383-85e7-9e0708ea9f62",
                              "Open Lineage Governance Action Publisher",
                              "Publishes open lineage events whenever governance actions run in the open metadata ecosystem.",
                              null,
                              null,
                              null,
                              null,
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
                            60,
                            null,
                            "dc0c7870-4da4-41bc-ad65-98d38d563a4d",
                            "Open Lineage Cataloguer",
                            "Catalogs the resources detailed in the open lineage events received from third party systems.",
                            null,
                            null,
                            null,
                            null,
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
                                60,
                                new String[]{DeployedImplementationType.APACHE_KAFKA_TOPIC.getQualifiedName()},
                                "638322ff-3c26-4259-8c46-70f969fbe5cd",
                                "Open Lineage Kafka Listener",
                                "Receives open lineage events from the open lineage proxy.  It listens for events from Apache Kafka topics attached as catalog targets.",
                                null,
                                null,
                                null,
                                null,
                                ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK),

    HARVEST_OPEN_METADATA("f8bf326b-d613-4ece-a12e-a1423bc272d7",
                          "HarvestOpenMetadataIntegrationConnector",
                          "Monitors metadata changes in the open metadata ecosystem and populates a PostgreSQL database schema.",
                          HarvestOpenMetadataProvider.class.getName(),
                          "HarvestOpenMetadata",
                          "nannyomnpa",
                          null,
                          null,
                          null,
                          60,
                          new String[]{PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getQualifiedName()},
                          "11b3d589-2391-4104-9f6d-f5dfb3b1adf5",
                          "Harvest Open Metadata",
                          "Monitors metadata changes in the open metadata ecosystem and populates a PostgreSQL database schema.",
                          null,
                          null,
                          null,
                          null,
                          ContentPackDefinition.NANNY_CONTENT_PACK),

    HARVEST_SURVEYS("fae162c3-2bfd-467f-9c47-2e3b63a655de",
                    "HarvestSurveysIntegrationConnector",
                    "Scans for new survey reports in the open metadata ecosystem and populates a PostgreSQL database schema.",
                    HarvestSurveysProvider.class.getName(),
                    "HarvestSurveys",
                    "nannysrnpa",
                    null,
                    null,
                    null,
                    60,
                    new String[]{PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getQualifiedName()},
                    "f74c05ea-c1ad-48e7-9d79-5449d4e727bf",
                    "Harvest Surveys",
                    "Scans for new survey reports in the open metadata ecosystem and populates a PostgreSQL database schema.",
                    null,
                    null,
                    null,
                    null,
                    ContentPackDefinition.NANNY_CONTENT_PACK),

    HARVEST_ACTIVITY("856501d9-ec29-4e67-9cd7-120f53710ffa",
                    "HarvestActivityIntegrationConnector",
                    "Listens for audit log records published through specific Apache Kafka Topics and populates a PostgreSQL database schema or folder.",
                    DistributeAuditEventsFromKafkaProvider.class.getName(),
                    "HarvestActivity",
                    "nannyalnpa",
                    null,
                    null,
                    null,
                    60,
                    new String[]{PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getQualifiedName(),
                            DeployedImplementationType.FILE_FOLDER.getQualifiedName()},
                     "c09464a4-95df-4e8b-bfeb-43197da9db00",
                     "Harvest Activity",
                     "Listens for audit log records published through specific Apache Kafka Topics and populates a PostgreSQL database schema or folder.",
                     null,
                     null,
                     null,
                     null,
                     ContentPackDefinition.NANNY_CONTENT_PACK),

    PRODUCT_HARVESTER("8a3d91da-05a0-49ca-83e0-77f5c48bdf0c",
                      "JacquardHarvesterIntegrationConnector",
                      "Defines and maintains digital products based on the content of the open metadata repositories.",
                      OpenMetadataProductsHarvesterProvider.class.getName(),
                      "JacquardHarvester",
                      "jacquardnpa",
                      null,
                      null,
                      null,
                      60,
                      null,
                      "cff44017-c366-47cc-b3ac-2e5af75f2e3e",
                      "Harvest Open Metadata Products (Jacquard)",
                      "Defines and maintains digital products based on the content of the open metadata repositories.",
                      "JacquardHarvester",
                      SecretsStorePurpose.REST_BEARER_TOKEN.getName(),
                      new YAMLSecretsStoreProvider().getConnectorType().getGUID(),
                      "loading-bay/secrets/jacquard.omsecrets",
                      ContentPackDefinition.PRODUCTS_CONTENT_PACK),
    ;


    /**
     * Get the configuration properties for the Unity Catalog Server Cataloguer.
     *
     * @return map
     */
    private static Map<String, Object> getUCServerConfigProperties()
    {
        Map<String, Object> configurationProperties = new HashMap<>();
        configurationProperties.put(UnityCatalogConfigurationProperty.FRIENDSHIP_GUID.getName(), UC_CATALOG_CATALOGUER.getGUID());

        return configurationProperties;
    }

    /**
     * Get the configuration properties for the OMAGServerPlatformCataloguer.
     *
     * @return map
     */
    private static Map<String, Object> getOMAGCataloguerConfigProperties()
    {
        Map<String, Object> configurationProperties = new HashMap<>();

        configurationProperties.put(PlaceholderProperty.SECRETS_STORE.getName(), "loading-bay/secrets/default.omsecrets");

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


    private final String                guid;
    private final String                displayName;
    private final String                description;
    private final String                connectorProviderClassName;
    private final String                connectorName;
    private final String                connectorUserId;
    private final String                metadataSourceQualifiedName;
    private final String                endpointAddress;
    private final Map<String, Object>   configurationProperties;
    private final long                  refreshTimeInterval;
    private final String[]              deployedImplementationTypes;
    private final String                solutionComponentGUID;
    private final String                solutionComponentName;
    private final String                solutionComponentDescription;
    private final String                secretsCollectionName;
    private final String                secretsStorePurpose;
    private final String                secretsStoreConnectorTypeGUID;
    private final String                secretsStoreFileName;
    private final ContentPackDefinition contentPackDefinition;


    /**
     *
     * @param guid unique identifier for the integration connector
     * @param displayName display name
     * @param description description of the connector's behaviour
     * @param connectorProviderClassName provider class name for the implementation
     * @param connectorName name of the connector when it is running (stored in relationship between group and connector)
     * @param connectorUserId runtime userId (stored in relationship between group and connector)
     * @param metadataSourceQualifiedName metadata collection for newly created elements from this connector
     * @param endpointAddress optional endpoint address for the connector's connection
     * @param configurationProperties configuration properties for the integration connector's connection
     * @param refreshTimeInterval how often should it refresh (initial value)
     * @param deployedImplementationTypes what deployed implementation types does this connector support?
     * @param solutionComponentGUID unique identifier of the solution component for the governance action type
     * @param solutionComponentName display name of the solution component for the governance action type
     * @param solutionComponentDescription description of the solution component for the governance action type
     * @param secretsCollectionName            name of collection of secrets to use in the secrets store
     * @param secretsStorePurpose              type of authentication information provided by the secrets store
     * @param secretsStoreConnectorTypeGUID    optional connector type for secrets store
     * @param secretsStoreFileName             location of the secrets store
     * @param contentPackDefinition which content pack?
     */
    IntegrationConnectorDefinition(String                       guid,
                                   String                       displayName,
                                   String                       description,
                                   String                       connectorProviderClassName,
                                   String                       connectorName,
                                   String                       connectorUserId,
                                   String                       metadataSourceQualifiedName,
                                   String                       endpointAddress,
                                   Map<String, Object>          configurationProperties,
                                   long                         refreshTimeInterval,
                                   String[]                     deployedImplementationTypes,
                                   String                       solutionComponentGUID,
                                   String                       solutionComponentName,
                                   String                       solutionComponentDescription,
                                   String                       secretsCollectionName,
                                   String                       secretsStorePurpose,
                                   String                       secretsStoreConnectorTypeGUID,
                                   String                       secretsStoreFileName,
                                   ContentPackDefinition        contentPackDefinition)
    {
        this.guid                          = guid;
        this.displayName                   = displayName;
        this.description                   = description;
        this.connectorProviderClassName    = connectorProviderClassName;
        this.connectorName                 = connectorName;
        this.connectorUserId               = connectorUserId;
        this.metadataSourceQualifiedName   = metadataSourceQualifiedName;
        this.endpointAddress               = endpointAddress;
        this.configurationProperties       = configurationProperties;
        this.refreshTimeInterval           = refreshTimeInterval;
        this.deployedImplementationTypes   = deployedImplementationTypes;
        this.solutionComponentGUID         = solutionComponentGUID;
        this.solutionComponentName         = solutionComponentName;
        this.solutionComponentDescription  = solutionComponentDescription;
        this.secretsCollectionName         = secretsCollectionName;
        this.secretsStorePurpose           = secretsStorePurpose;
        this.secretsStoreConnectorTypeGUID = secretsStoreConnectorTypeGUID;
        this.secretsStoreFileName          = secretsStoreFileName;
        this.contentPackDefinition         = contentPackDefinition;
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
     * @param integrationGroupQualifiedName qualified name of the associated integration group
     * @return string
     */
    public String getQualifiedName(String integrationGroupQualifiedName)
    {
        return integrationGroupQualifiedName + "::" + displayName;
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
    public List<String> getDeployedImplementationTypes()
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
     * @return path name of file
     */
    public String getSecretsStoreFileName()
    {
        return secretsStoreFileName;
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
