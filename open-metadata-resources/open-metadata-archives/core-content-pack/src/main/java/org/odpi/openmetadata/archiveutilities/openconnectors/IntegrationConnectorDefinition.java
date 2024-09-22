/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.archiveutilities.openconnectors;

import org.odpi.openmetadata.adapters.connectors.apacheatlas.controls.AtlasDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ApacheAtlasIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.apachekafka.integration.KafkaTopicIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control.EgeriaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.catalog.OMAGServerPlatformCatalogProvider;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.DataFilesMonitorIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.DataFolderMonitorIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.JDBCIntegrationConnectorProvider;
import org.odpi.openmetadata.adapters.connectors.integration.openapis.OpenAPIMonitorIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.openlineage.*;
import org.odpi.openmetadata.adapters.connectors.postgres.catalog.PostgresServerIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.sync.OSSUnityCatalogInsideCatalogSyncProvider;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.sync.OSSUnityCatalogServerSyncProvider;
import org.odpi.openmetadata.adapters.connectors.apachekafka.control.KafkaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues;
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
    SAMPLE_DATA_CATALOGUER("cd6479e1-2fe7-4426-b358-8a0cf70be117",
                           "SampleDataFilesMonitorIntegrationConnector",
                           "Catalogs files found under the sample-data directory (folder).",
                           DataFilesMonitorIntegrationProvider.class.getName(),
                           "SampleDataCataloguer",
                           "sampledatacatnpa",
                           null,
                           "sample-data",
                           getAllFileCataloguerConfigProperties(),
                           1440,
                           new String[]{DeployedImplementationType.FILE_FOLDER.getQualifiedName()},
                           ContentPackDefinition.CORE_CONTENT_PACK),

    CONTENT_PACK_CATALOGUER("6bb2181e-7724-4515-ba3c-877cded55980",
                            "ContentPacksMonitorIntegrationConnector",
                            "Catalogs files found under the content-packs directory (folder).",
                            DataFilesMonitorIntegrationProvider.class.getName(),
                            "ContentPacksCataloguer",
                            "contentpackcatnpa",
                            null,
                            "content-packs",
                            getFileCataloguerConfigProperties(),
                            14400,
                            new String[]{DeployedImplementationType.FILE_FOLDER.getQualifiedName()},
                            ContentPackDefinition.CORE_CONTENT_PACK),

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
                              ContentPackDefinition.CORE_CONTENT_PACK),

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
                                    ContentPackDefinition.CORE_CONTENT_PACK),

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
                           ContentPackDefinition.POSTGRES_CONTENT_PACK),

    POSTGRES_SERVER_CATALOGUER("36f69fd0-54ba-4f59-8a44-11ccf2687a34",
                               "PostgreSQLServerIntegrationConnector",
                               "Catalogs the databases found in PostgreSQL Servers attached as catalog targets.",
                               PostgresServerIntegrationProvider.class.getName(),
                               "PostgreSQLServerCataloguer",
                               "postgrescatnpa",
                               null,
                               null,
                               null,
                               60,
                               new String[]{PostgresDeployedImplementationType.POSTGRESQL_SERVER.getQualifiedName()},
                               ContentPackDefinition.POSTGRES_CONTENT_PACK),


    APACHE_ATLAS_EXCHANGE("5721627a-2dd4-4f95-a274-6cfb128edb97",
                               "ApacheAtlasServerIntegrationConnector",
                               "Catalogs the databases found in PostgreSQL Servers attached as catalog targets.",
                               ApacheAtlasIntegrationProvider.class.getName(),
                               "ApacheAtlasExchange",
                               "atlascatnpa",
                               null,
                               null,
                               null,
                               60,
                               new String[]{AtlasDeployedImplementationType.APACHE_ATLAS_SERVER.getQualifiedName()},
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
                   ContentPackDefinition.APIS_CONTENT_PACK),

    UC_CATALOG_CATALOGUER("74dde22f-2249-4ea3-af2b-b39e73f79b81",
                          "UnityCatalogInsideCatalogIntegrationConnector",
                          "Synchronizes metadata information about the contents of catalogs found in the OSS Unity Catalog 'catalog of catalogs' with the open metadata ecosystem.",
                          OSSUnityCatalogInsideCatalogSyncProvider.class.getName(),
                          "UnityCatalogInsideCatalogSynchronizer",
                          "ossuccatcatnpa",
                          null,
                          null,
                          null,
                          60,
                          new String[]{UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getQualifiedName()},
                          ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK),

    UC_SERVER_CATALOGUER("06d068d9-9e08-4e67-8c59-073bbf1013af",
                         "UnityCatalogServerIntegrationConnector",
                         "Synchronizes metadata about the catalogs found in the OSS Unity Catalog 'catalog of catalogs' with the open metadata ecosystem.",
                         OSSUnityCatalogServerSyncProvider.class.getName(),
                         "UnityCatalogServerSynchronizer",
                         "ossuccatnpa",
                         null,
                         null,
                         getUCServerConfigProperties(),
                         60,
                         new String[]{UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getQualifiedName()},
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
                                    60,
                                    new String[]{EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getQualifiedName()},
                                    ContentPackDefinition.CORE_CONTENT_PACK),

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
                               ContentPackDefinition.CORE_CONTENT_PACK),

    OPEN_LINEAGE_FILE_PUBLISHER("6271b678-7d22-4cdf-87b1-45b366beaf4e",
                                "OpenLineageFilePublisherIntegrationConnector",
                                "Publishes open lineage events as JSON files to each of the folders attached as catalog targets.",
                                FileBasedOpenLineageLogStoreProvider.class.getName(),
                                "OpenLineageFilePublisher",
                                "olfilepubnpa",
                                null,
                                "logs/openlineage",
                                null,
                                60,
                                new String[]{DeployedImplementationType.FILE_FOLDER.getQualifiedName(), DeployedImplementationType.DATA_FOLDER.getQualifiedName()},
                                ContentPackDefinition.CORE_CONTENT_PACK),

    OPEN_LINEAGE_GA_PUBLISHER("206f8faf-04da-4b6f-8280-eeee3943afeb",
                              "OpenLineageGovernanceActionPublisherIntegrationConnector",
                              "Publishes open lineage events as governance actions run in the open metadata ecosystem.",
                              GovernanceActionOpenLineageIntegrationProvider.class.getName(),
                              "OpenLineageGovernanceActionPublisher",
                              "olgapubnpa",
                              null,
                              null,
                              null,
                              60,
                              null,
                              ContentPackDefinition.CORE_CONTENT_PACK),

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
                            ContentPackDefinition.CORE_CONTENT_PACK),

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
                                ContentPackDefinition.CORE_CONTENT_PACK),


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
    private final ContentPackDefinition contentPackDefinition;


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
                                   ContentPackDefinition        contentPackDefinition)
    {
        this.guid                        = guid;
        this.displayName                 = displayName;
        this.description                 = description;
        this.connectorProviderClassName  = connectorProviderClassName;
        this.connectorName               = connectorName;
        this.connectorUserId             = connectorUserId;
        this.metadataSourceQualifiedName = metadataSourceQualifiedName;
        this.endpointAddress             = endpointAddress;
        this.configurationProperties     = configurationProperties;
        this.refreshTimeInterval         = refreshTimeInterval;
        this.deployedImplementationTypes = deployedImplementationTypes;
        this.contentPackDefinition       = contentPackDefinition;
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
        return OpenMetadataValidValues.DEFAULT_INTEGRATION_GROUP_QUALIFIED_NAME + ":" + displayName;
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
