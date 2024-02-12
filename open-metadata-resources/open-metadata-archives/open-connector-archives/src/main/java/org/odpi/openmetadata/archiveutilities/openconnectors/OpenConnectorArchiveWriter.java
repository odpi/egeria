/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.openconnectors;

import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStoreProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFolderProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVFileStoreProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.datafolder.DataFolderProvider;
import org.odpi.openmetadata.adapters.connectors.discoveryservices.discoveratlas.DiscoverApacheAtlasProvider;
import org.odpi.openmetadata.adapters.connectors.governanceactions.provisioning.MoveCopyFileGovernanceActionProvider;
import org.odpi.openmetadata.adapters.connectors.governanceactions.remediation.OriginSeekerGovernanceActionProvider;
import org.odpi.openmetadata.adapters.connectors.governanceactions.remediation.ZonePublisherGovernanceActionProvider;
import org.odpi.openmetadata.adapters.connectors.governanceactions.watchdog.GenericFolderWatchdogGovernanceActionProvider;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.ApacheAtlasIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.DataFilesMonitorIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.DataFolderMonitorIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.csvlineageimporter.CSVLineageImporterProvider;
import org.odpi.openmetadata.adapters.connectors.integration.egeria.EgeriaCataloguerIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.JDBCIntegrationConnectorProvider;
import org.odpi.openmetadata.adapters.connectors.integration.kafka.KafkaMonitorIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.kafkaaudit.DistributeAuditEventsFromKafkaProvider;
import org.odpi.openmetadata.adapters.connectors.integration.openapis.OpenAPIMonitorIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.openlineage.*;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.ApacheAtlasRESTProvider;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnectorProvider;
import org.odpi.openmetadata.adapters.connectors.secretsstore.envar.EnvVarSecretsStoreProvider;
import org.odpi.openmetadata.adapters.connectors.surveyaction.surveycsv.CSVSurveyServiceProvider;
import org.odpi.openmetadata.adapters.connectors.surveyaction.surveyfile.FileSurveyServiceProvider;
import org.odpi.openmetadata.adapters.connectors.surveyaction.surveyfolder.FolderSurveyServiceProvider;
import org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider;
import org.odpi.openmetadata.adminservices.configuration.registration.*;
import org.odpi.openmetadata.frameworks.governanceaction.actiontargettype.ActionTargetType;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataValidValues;
import org.odpi.openmetadata.frameworks.governanceaction.refdata.*;
import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchive;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveWriter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.samples.archiveutilities.GovernanceArchiveHelper;

import java.util.*;

import static org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataValidValues.constructValidValueCategory;
import static org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * OpenConnectorArchiveWriter creates an open metadata archive that includes the connector type
 * information for all open connectors supplied by the egeria project.
 */
public class OpenConnectorArchiveWriter extends OMRSArchiveWriter
{
    private static final String archiveFileName = "OpenConnectorsArchive.omarchive";

    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "09450b83-20ff-4a8b-a8fb-f9b527bbcba6";
    private static final String                  archiveName        = "OpenConnectorsArchive";
    private static final String                  archiveLicense     = "Apache-2.0";
    private static final String                  archiveDescription = "Connector Types and Categories for connectors from the Egeria project.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  originatorName     = "Egeria Project";
    private static final Date                    creationDate       = new Date(1649686978059L);

    /*
     * Names for standard definitions
     */
    private static final String connectorTypeDirectoryQualifiedName = "OpenMetadataConnectorTypeDirectory_09450b83-20ff-4a8b-a8fb-f9b527bbcba6";
    private static final String connectorTypeDirectoryDisplayName   = "Open Metadata Connector Type Directory";
    private static final String connectorTypeDirectoryDescription   = "Open Metadata standard connector categories and connector types.";
    private static final String fileConnectorCategoryQualifiedName  = "OpenMetadataFileConnectorCategory_09450b83-20ff-4a8b-a8fb-f9b527bbcba6";
    private static final String fileConnectorCategoryDisplayName    = "Open Metadata File Connector Category";
    private static final String fileConnectorCategoryDescription    = "Open Metadata connector category for connectors that work with files.";
    private static final String kafkaConnectorCategoryQualifiedName = "OpenMetadataKafkaConnectorCategory_09450b83-20ff-4a8b-a8fb-f9b527bbcba6";
    private static final String kafkaConnectorCategoryDisplayName   = "Open Metadata Apache Kafka Connector Category";
    private static final String kafkaConnectorCategoryDescription   = "Open Metadata connector category for connectors to Apache Kafka.";
    private static final String kafkaConnectorCategoryTargetSource  = "Apache Software Foundation (ASF)";
    private static final String kafkaConnectorCategoryTargetName    = "Apache Kafka";

    private static final String relationalConnectorCategoryQualifiedName = "OpenMetadataJDBCConnectorCategory_09450b83-20ff-4a8b-a8fb-f9b527bbcba6";
    private static final String relationalConnectorCategoryDisplayName   = "Open Metadata JDBC Connector Category";
    private static final String relationalConnectorCategoryDescription   = "Open Metadata connector category for connectors to relational databases.";
    private static final String relationalConnectorCategoryTargetSource  = "Java Database Connector (JDBC)";
    private static final String relationalConnectorCategoryTargetName    = "Relational Database";


    /*
     * Specific values for initializing TypeDefs
     */
    private static final long   versionNumber = 1L;
    private static final String versionName   = "1.0";

    private final OMRSArchiveBuilder      archiveBuilder;
    private final GovernanceArchiveHelper archiveHelper;

    private final Map<String, String> parentValidValueQNameToGUIDMap = new HashMap<>();
    private final Map<String, String> deployedImplementationTypeGUIDs = new HashMap<>();
    private final Map<String, String> openMetadataTypeGUIDs = new HashMap<>();


    /**
     * Default constructor initializes the archive.
     */
    private OpenConnectorArchiveWriter()
    {
        List<OpenMetadataArchive> dependentOpenMetadataArchives = new ArrayList<>();

        /*
         * This value allows the Coco Types to be based on the existing open metadata types
         */
        dependentOpenMetadataArchives.add(new OpenMetadataTypesArchive().getOpenMetadataArchive());

        this.archiveBuilder = new OMRSArchiveBuilder(archiveGUID,
                                                     archiveName,
                                                     archiveDescription,
                                                     archiveType,
                                                     originatorName,
                                                     archiveLicense,
                                                     creationDate,
                                                     dependentOpenMetadataArchives);

        this.archiveHelper = new GovernanceArchiveHelper(archiveBuilder,
                                                         archiveGUID,
                                                         archiveName,
                                                         archiveName,
                                                         originatorName,
                                                         creationDate,
                                                         versionNumber,
                                                         versionName);
    }


    /**
     * Returns the open metadata archive containing new metadata entities.
     *
     * @return populated open metadata archive object
     */
    private OpenMetadataArchive getOpenMetadataArchive()
    {
        String connectorDirectoryTypeGUID = archiveHelper.addConnectorTypeDirectory(connectorTypeDirectoryQualifiedName,
                                                                                    connectorTypeDirectoryDisplayName,
                                                                                    connectorTypeDirectoryDescription,
                                                                                    null);

        String fileConnectorCategoryGUID = archiveHelper.addConnectorCategory(connectorDirectoryTypeGUID,
                                                                              fileConnectorCategoryQualifiedName,
                                                                              fileConnectorCategoryDisplayName,
                                                                              fileConnectorCategoryDescription,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              null);

        String kafkaConnectorCategoryGUID = archiveHelper.addConnectorCategory(connectorDirectoryTypeGUID,
                                                                               kafkaConnectorCategoryQualifiedName,
                                                                               kafkaConnectorCategoryDisplayName,
                                                                               kafkaConnectorCategoryDescription,
                                                                               kafkaConnectorCategoryTargetSource,
                                                                               kafkaConnectorCategoryTargetName,
                                                                               null,
                                                                               null,
                                                                               null,
                                                                               null);

        String relationalConnectorCategoryGUID = archiveHelper.addConnectorCategory(connectorDirectoryTypeGUID,
                                                                                    relationalConnectorCategoryQualifiedName,
                                                                                    relationalConnectorCategoryDisplayName,
                                                                                    relationalConnectorCategoryDescription,
                                                                                    relationalConnectorCategoryTargetSource,
                                                                                    relationalConnectorCategoryTargetName,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null);


        /*
         * Add the valid metadata values used in the resourceUse property of the ResourceList relationship.
         */
        String resourceUseParentSetGUID = this.getParentSet(OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                                             OpenMetadataProperty.RESOURCE_USE.name,
                                                             null);

        for (ResourceUse resourceUse : ResourceUse.values())
        {
            this.archiveHelper.addValidValue(null,
                                             resourceUseParentSetGUID,
                                             resourceUseParentSetGUID,
                                             OpenMetadataType.VALID_VALUE_SET_TYPE_NAME,
                                             OpenMetadataType.VALID_VALUE_SET_TYPE_NAME,
                                             resourceUse.getQualifiedName(),
                                             resourceUse.getResourceUse(),
                                             resourceUse.getDescription(),
                                             resourceUse.getCategory(),
                                             OpenMetadataValidValues.VALID_METADATA_VALUES_USAGE,
                                             OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE,
                                             resourceUse.getResourceUse(),
                                             false,
                                             false,
                                             null);
        }

        /*
         * Add valid metadata values for open metadata types that have been reformatted.
         * The GUIDs are saved in a look-up map
         * to make it easy to link other elements to these valid values later.
         */
        for (OpenMetadataType openMetadataType : OpenMetadataType.values())
        {
            String guid = this.addOpenMetadataType(openMetadataType);

            if (guid != null)
            {
                openMetadataTypeGUIDs.put(openMetadataType.typeName, guid);
            }
        }

        /*
         * Add valid metadata values for deployedImplementationType.  The GUIDs are saved in a look-up map
         * to make it easy to link other elements to these valid values later.
         */
        for (DeployedImplementationType deployedImplementationType : DeployedImplementationType.values())
        {
            String guid = this.addDeployedImplementationType(deployedImplementationType.getDeployedImplementationType(),
                                                             deployedImplementationType.getAssociatedTypeName(),
                                                             deployedImplementationType.getQualifiedName(),
                                                             deployedImplementationType.getCategory(),
                                                             deployedImplementationType.getDescription(),
                                                             deployedImplementationType.getWikiLink());

            deployedImplementationTypeGUIDs.put(deployedImplementationType.getDeployedImplementationType(), guid);
        }

        /*
         * Egeria also has valid values for its implementation.  These are useful when cataloguing Egeria.
         * This first list are the different types of OMAG Servers
         */
        Map<String, String> serverTypeGUIDs = new HashMap<>();
        Map<String, String> serviceGUIDs    = new HashMap<>();

        for (ServerTypeClassification serverTypeClassification : ServerTypeClassification.values())
        {
            String guid = this.addDeployedImplementationType(serverTypeClassification.getServerTypeName(),
                                                             OpenMetadataType.SOFTWARE_SERVER_TYPE_NAME,
                                                             serverTypeClassification.getServerTypeDescription(),
                                                             serverTypeClassification.getServerTypeWiki());

            serverTypeGUIDs.put(serverTypeClassification.getServerTypeName(), guid);
        }

        /*
         * Next are the common services of Egeria.
         */
        for (CommonServicesDescription commonServicesDescription : CommonServicesDescription.values())
        {
            String guid = this.addDeployedImplementationType(commonServicesDescription.getServiceName(),
                                                             OpenMetadataType.SOFTWARE_SERVICE_TYPE_NAME,
                                                             commonServicesDescription.getServiceDescription(),
                                                             commonServicesDescription.getServiceWiki());

            serviceGUIDs.put(commonServicesDescription.getServiceName(), guid);
        }

        /*
         * These services support the governance servers.
         */
        for (GovernanceServicesDescription governanceServicesDescription : GovernanceServicesDescription.values())
        {
            String guid = this.addDeployedImplementationType(governanceServicesDescription.getServiceName(),
                                                             OpenMetadataType.SOFTWARE_SERVICE_TYPE_NAME,
                                                             governanceServicesDescription.getServiceDescription(),
                                                             governanceServicesDescription.getServiceWiki());

            serviceGUIDs.put(governanceServicesDescription.getServiceName(), guid);
        }

        /*
         * The access services are found in the Metadata Access Server and Metadata Access Point OMAG Servers.
         */
        String serverTypeGUID = serverTypeGUIDs.get(ServerTypeClassification.METADATA_ACCESS_SERVER.getServerTypeName());
        String serverTypeGUID2 = serverTypeGUIDs.get(ServerTypeClassification.METADATA_ACCESS_STORE.getServerTypeName());

        for (AccessServiceDescription accessServiceDescription : AccessServiceDescription.values())
        {
            String guid = this.addDeployedImplementationType(accessServiceDescription.getAccessServiceFullName(),
                                                             OpenMetadataType.SOFTWARE_SERVICE_TYPE_NAME,
                                                             accessServiceDescription.getAccessServiceDescription(),
                                                             accessServiceDescription.getAccessServiceWiki());

            serviceGUIDs.put(accessServiceDescription.getAccessServiceFullName(), guid);

            archiveHelper.addResourceListRelationshipByGUID(serverTypeGUID,
                                                            guid,
                                                            ResourceUse.HOSTED_SERVICE.getResourceUse());

            archiveHelper.addResourceListRelationshipByGUID(serverTypeGUID2,
                                                            guid,
                                                            ResourceUse.HOSTED_SERVICE.getResourceUse());
        }

        /*
         * View services are found in the View Server.  They call an access service.
         */
        serverTypeGUID = serverTypeGUIDs.get(ServerTypeClassification.VIEW_SERVER.getServerTypeName());

        for (ViewServiceDescription viewServiceDescription : ViewServiceDescription.values())
        {
            String guid = this.addDeployedImplementationType(viewServiceDescription.getViewServiceFullName(),
                                                             OpenMetadataType.SOFTWARE_SERVICE_TYPE_NAME,
                                                             viewServiceDescription.getViewServiceDescription(),
                                                             viewServiceDescription.getViewServiceWiki());

            archiveHelper.addResourceListRelationshipByGUID(serverTypeGUID,
                                                            guid,
                                                            ResourceUse.HOSTED_SERVICE.getResourceUse());

            archiveHelper.addResourceListRelationshipByGUID(guid,
                                                            serviceGUIDs.get(viewServiceDescription.getViewServicePartnerService()),
                                                            ResourceUse.CALLED_SERVICE.getResourceUse());
        }

        /*
         * Engine services are found in the Engine Host.   They call an access service.  They also
         * support a particular type of governance engine and governance service.
         */
        serverTypeGUID = serverTypeGUIDs.get(ServerTypeClassification.ENGINE_HOST.getServerTypeName());

        for (EngineServiceDescription engineServiceDescription : EngineServiceDescription.values())
        {
            String guid = this.addDeployedImplementationType(engineServiceDescription.getEngineServiceFullName(),
                                                             OpenMetadataType.SOFTWARE_SERVICE_TYPE_NAME,
                                                             engineServiceDescription.getEngineServiceDescription(),
                                                             engineServiceDescription.getEngineServiceWiki());

            archiveHelper.addResourceListRelationshipByGUID(serverTypeGUID,
                                                            guid,
                                                            ResourceUse.HOSTED_SERVICE.getResourceUse());

            archiveHelper.addResourceListRelationshipByGUID(guid,
                                                            serviceGUIDs.get(engineServiceDescription.getEngineServicePartnerService()),
                                                            ResourceUse.CALLED_SERVICE.getResourceUse());

            String governanceEngineGUID = deployedImplementationTypeGUIDs.get(engineServiceDescription.getHostedGovernanceEngineDeployedImplementationType());
            String governanceServiceGUID = deployedImplementationTypeGUIDs.get(engineServiceDescription.getHostedGovernanceServiceDeployedImplementationType());

            if (governanceEngineGUID != null)
            {
                archiveHelper.addResourceListRelationshipByGUID(guid,
                                                                governanceEngineGUID,
                                                                ResourceUse.HOSTED_GOVERNANCE_ENGINE.getResourceUse());

                if (governanceServiceGUID != null)
                {
                    archiveHelper.addResourceListRelationshipByGUID(governanceEngineGUID,
                                                                    governanceServiceGUID,
                                                                    ResourceUse.HOSTED_CONNECTOR.getResourceUse());
                }
            }
        }

        /*
         * Integration services are found in the integration daemon.  They each call a particular access service and
         * host a particular type of connector.
         */
        for (IntegrationServiceDescription integrationServiceDescription : IntegrationServiceDescription.values())
        {
            String guid = this.addDeployedImplementationType(integrationServiceDescription.getIntegrationServiceFullName(),
                                                             OpenMetadataType.SOFTWARE_SERVICE_TYPE_NAME,
                                                             integrationServiceDescription.getIntegrationServiceDescription(),
                                                             integrationServiceDescription.getIntegrationServiceWiki());

            archiveHelper.addResourceListRelationshipByGUID(serverTypeGUID,
                                                            guid,
                                                            ResourceUse.HOSTED_SERVICE.getResourceUse());

            archiveHelper.addResourceListRelationshipByGUID(guid,
                                                            serviceGUIDs.get(integrationServiceDescription.getIntegrationServicePartnerOMAS().getAccessServiceFullName()),
                                                            ResourceUse.CALLED_SERVICE.getResourceUse());

            String connectorTypeGUID = deployedImplementationTypeGUIDs.get(integrationServiceDescription.getConnectorDeployedImplementationType());

            if (connectorTypeGUID != null)
            {
                archiveHelper.addResourceListRelationshipByGUID(guid,
                                                                connectorTypeGUID,
                                                                ResourceUse.HOSTED_CONNECTOR.getResourceUse());
            }
        }

        /*
         * Add the valid values for the fileType property.
         */
        for (FileType fileType : FileType.values())
        {
            this.addFileType(fileType.getFileTypeName(),
                             fileType.getEncoding(),
                             fileType.getAssetSubTypeName(),
                             fileType.getDeployedImplementationType(),
                             fileType.getDescription());
        }

        /*
         * Add the list of special file names.
         */
        for (FileName fileName : FileName.values())
        {
            this.addFileName(fileName.getFileName(),
                             fileName.getFileType(),
                             fileName.getDeployedImplementationType());
        }

        /*
         * Add the list of recognized file extensions.
         */
        for (FileExtension fileExtension : FileExtension.values())
        {
            this.addFileExtension(fileExtension.getFileExtension(), fileExtension.getFileTypes());
        }

        /*
         * Integration Connector Types may need to link to deployedImplementationType valid value element.
         * This information is in the connector provider.
         */
        archiveHelper.addConnectorType(fileConnectorCategoryGUID, new CSVFileStoreProvider());
        archiveHelper.addConnectorType(fileConnectorCategoryGUID, new DataFolderProvider());
        archiveHelper.addConnectorType(fileConnectorCategoryGUID, new BasicFileStoreProvider());
        archiveHelper.addConnectorType(fileConnectorCategoryGUID, new BasicFolderProvider());
        archiveHelper.addConnectorType(relationalConnectorCategoryGUID, new JDBCResourceConnectorProvider());
        archiveHelper.addConnectorType(kafkaConnectorCategoryGUID, new KafkaOpenMetadataTopicProvider());
        archiveHelper.addConnectorType(null, new ApacheAtlasRESTProvider());
        archiveHelper.addConnectorType(null, new ApacheAtlasIntegrationProvider());
        archiveHelper.addConnectorType(null, new EgeriaCataloguerIntegrationProvider());
        archiveHelper.addConnectorType(null, new CSVLineageImporterProvider());
        archiveHelper.addConnectorType(null, new DataFilesMonitorIntegrationProvider());
        archiveHelper.addConnectorType(null, new DataFolderMonitorIntegrationProvider());
        archiveHelper.addConnectorType(null, new JDBCIntegrationConnectorProvider());
        archiveHelper.addConnectorType(null, new DistributeAuditEventsFromKafkaProvider());
        archiveHelper.addConnectorType(null, new KafkaMonitorIntegrationProvider());
        archiveHelper.addConnectorType(null, new OpenAPIMonitorIntegrationProvider());
        archiveHelper.addConnectorType(null, new APIBasedOpenLineageLogStoreProvider());
        archiveHelper.addConnectorType(null, new FileBasedOpenLineageLogStoreProvider());
        archiveHelper.addConnectorType(null, new GovernanceActionOpenLineageIntegrationProvider());
        archiveHelper.addConnectorType(null, new OpenLineageCataloguerIntegrationProvider());
        archiveHelper.addConnectorType(null, new OpenLineageEventReceiverIntegrationProvider());
        archiveHelper.addConnectorType(null, new EnvVarSecretsStoreProvider());

        /*
         * Create the default integration group.
         */
        String integrationGroupGUID = archiveHelper.addIntegrationGroup(OpenMetadataValidValues.DEFAULT_INTEGRATION_GROUP_QUALIFIED_NAME,
                                                                        OpenMetadataValidValues.DEFAULT_INTEGRATION_GROUP_NAME,
                                                                        "Dynamic integration group to use with an Integration Daemon configuration.",
                                                                        null,
                                                                        null,
                                                                        archiveFileName,
                                                                        null,
                                                                        null);

        Map<String, Object> configurationProperties = new HashMap<>();
        configurationProperties.put("waitForDirectory", "true");

        String filesIntegrationConnectorGUID = archiveHelper.addIntegrationConnector(DataFilesMonitorIntegrationProvider.class.getName(),
                                                                                     configurationProperties,
                                                                                     OpenMetadataValidValues.DEFAULT_INTEGRATION_GROUP_QUALIFIED_NAME + ":DataFilesMonitorIntegrationConnector",
                                                                                     "DataFilesMonitorIntegrationConnector",
                                                                                     "Catalogs files found under the starting directory (folder).",
                                                                                     "sample-data",
                                                                                     null);

        archiveHelper.addRegisteredIntegrationConnector(integrationGroupGUID,
                                                        "FilesMonitor",
                                                        "generalnpa",
                                                        "cocoDataLake",
                                                        60,
                                                        filesIntegrationConnectorGUID);

        /*
         * Register the governance services that are going to be in the default governance engines.
         */
        GovernanceActionDescription fileProvisionerGUID = this.getFileProvisioningGovernanceActionService();
        GovernanceActionDescription watchDogServiceGUID = this.getWatchdogGovernanceActionService();
        GovernanceActionDescription originSeekerGUID = this.getOriginSeekerGovernanceActionService();
        GovernanceActionDescription zonePublisherGUID = this.getZonePublisherGovernanceActionService();
        GovernanceActionDescription csvSurveyGUID = this.getCSVFileSurveyService();
        GovernanceActionDescription fileSurveyGUID = this.getDataFileSurveyService();
        GovernanceActionDescription folderSurveyGUID = this.getFolderSurveyService();

        /*
         * Define the file provisioning engine.
         */
        String fileProvisioningEngineGUID = this.getFileProvisioningEngine();

        this.addCopyFileRequestType(fileProvisioningEngineGUID, fileProvisionerGUID);
        this.addMoveFileRequestType(fileProvisioningEngineGUID, fileProvisionerGUID);
        this.addDeleteFileRequestType(fileProvisioningEngineGUID, fileProvisionerGUID);

        /*
         * Define the AssetOnboarding engine
         */
        String assetGovernanceEngineGUID = this.getAssetOnboardingEngine();

        this.addWatchNestedInFolderRequestType(assetGovernanceEngineGUID, watchDogServiceGUID);
        this.addSeekOriginRequestType(assetGovernanceEngineGUID, originSeekerGUID);
        this.addSetZoneMembershipRequestType(assetGovernanceEngineGUID, zonePublisherGUID);

        /*
         * Define the asset survey engine
         */
        String assetSurveyEngineGUID = this.getAssetSurveyEngine();

        this.addCSVFileRequestType(assetSurveyEngineGUID, csvSurveyGUID);
        this.addDataFileRequestType(assetSurveyEngineGUID, fileSurveyGUID);
        this.addFolderRequestType(assetSurveyEngineGUID, folderSurveyGUID);

        /*
         * Saving the GUIDs means tha the guids in the archive are stable between runs of the archive writer.
         */
        archiveHelper.saveGUIDs();

        /*
         * The completed archive is ready to be packaged up and returned
         */
        return this.archiveBuilder.getOpenMetadataArchive();
    }


    /**
     * Add a new valid values record for a deployed implementation type.
     *
     * @param deployedImplementationType preferred value
     * @param associatedTypeName         specific type name to tie it to (maybe null)
     * @param description                description of this value
     * @param wikiLink                   optional URL link to more information
     * @return unique identifier of the deployedImplementationType
     */
    private String addDeployedImplementationType(String deployedImplementationType,
                                                 String associatedTypeName,
                                                 String description,
                                                 String wikiLink)
    {
        String qualifiedName = constructValidValueQualifiedName(associatedTypeName,
                                                                OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                                null,
                                                                deployedImplementationType);

        String category = constructValidValueCategory(associatedTypeName,
                                                      OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                      null);

        return this.addDeployedImplementationType(deployedImplementationType, associatedTypeName, qualifiedName, category, description, wikiLink);
    }


    /**
     * Add a new valid values record for a deployed implementation type.
     *
     * @param openMetadataType preferred value
     */
    private String addOpenMetadataType(OpenMetadataType openMetadataType)
    {
        String parentSetGUID = this.getParentSet(null,
                                                 OpenMetadataProperty.TYPE_NAME.name,
                                                 null);

        String qualifiedName = constructValidValueQualifiedName(null,
                                                                OpenMetadataProperty.TYPE_NAME.name,
                                                                null,
                                                                openMetadataType.typeName);

        String category = constructValidValueCategory(null,
                                                      OpenMetadataProperty.TYPE_NAME.name,
                                                      null);

        String validValueGUID = this.archiveHelper.addValidValue(openMetadataType.descriptionGUID,
                                                                 parentSetGUID,
                                                                 parentSetGUID,
                                                                 OpenMetadataType.VALID_VALUE_SET_TYPE_NAME,
                                                                 OpenMetadataType.VALID_VALUE_SET_TYPE_NAME,
                                                                 qualifiedName,
                                                                 openMetadataType.typeName,
                                                                 openMetadataType.description,
                                                                 category,
                                                                 OpenMetadataValidValues.VALID_METADATA_VALUES_USAGE,
                                                                 OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE,
                                                                 openMetadataType.typeName,
                                                                 false,
                                                                 false,
                                                                 null);

        if (openMetadataType.wikiURL != null)
        {
            String externalReferenceGUID = this.archiveHelper.addExternalReference(null,
                                                                                   validValueGUID,
                                                                                   OpenMetadataType.VALID_VALUE_SET_TYPE_NAME,
                                                                                   qualifiedName + "_wikiLink",
                                                                                   "More information about open metadata type: " + openMetadataType.typeName,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   0,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null);

            this.archiveHelper.addExternalReferenceLink(validValueGUID, externalReferenceGUID, null, null, null);
        }

        return null;
    }


    /**
     * Add a new valid values record for a deployed implementation type.
     *
     * @param deployedImplementationType preferred value
     * @param associatedTypeName         specific type name to tie it to (maybe null)
     * @param qualifiedName              qualifiedName for this value
     * @param category                   category for this value
     * @param description                description of this value
     * @param wikiLink                   optional URL link to more information
     * @return unique identifier of the deployedImplementationType
     */
    private String addDeployedImplementationType(String deployedImplementationType,
                                                 String associatedTypeName,
                                                 String qualifiedName,
                                                 String category,
                                                 String description,
                                                 String wikiLink)
    {
        String parentSetGUID = this.getParentSet(associatedTypeName,
                                                 OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                 null);


        String validValueGUID = this.archiveHelper.addValidValue(null,
                                                                 parentSetGUID,
                                                                 parentSetGUID,
                                                                 OpenMetadataType.VALID_VALUE_SET_TYPE_NAME,
                                                                 OpenMetadataType.VALID_VALUE_SET_TYPE_NAME,
                                                                 qualifiedName,
                                                                 deployedImplementationType,
                                                                 description,
                                                                 category,
                                                                 OpenMetadataValidValues.VALID_METADATA_VALUES_USAGE,
                                                                 OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE,
                                                                 deployedImplementationType,
                                                                 false,
                                                                 false,
                                                                 null);

        if (wikiLink != null)
        {
            String externalReferenceGUID = this.archiveHelper.addExternalReference(null,
                                                                                   validValueGUID,
                                                                                   OpenMetadataType.VALID_VALUE_SET_TYPE_NAME,
                                                                                   qualifiedName + "_wikiLink",
                                                                                   "More information about deployedImplementationType: " + deployedImplementationType,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   0,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null);

            this.archiveHelper.addExternalReferenceLink(validValueGUID, externalReferenceGUID, null, null, null);
        }

        return validValueGUID;
    }


    /**
     * Add reference data that describes a specific file type.
     *
     * @param fileTypeName               the name of the file type
     * @param encoding                   the optional name of the encoding method used in the file
     * @param deployedImplementationType value for deployedImplementationType
     * @param assetSubTypeName           the open metadata type where this value is used
     * @param description                description of the type
     */
    private void addFileType(String                     fileTypeName,
                             String                     encoding,
                             String                     assetSubTypeName,
                             DeployedImplementationType deployedImplementationType,
                             String                     description)
    {
        String qualifiedName = constructValidValueQualifiedName(OpenMetadataType.DATA_FILE.typeName,
                                                                OpenMetadataProperty.FILE_TYPE.name,
                                                                null,
                                                                fileTypeName);

        String category = constructValidValueCategory(OpenMetadataType.DATA_FILE.typeName,
                                                      OpenMetadataProperty.FILE_TYPE.name,
                                                      null);

        Map<String, String> additionalProperties = new HashMap<>();

        if (encoding != null)
        {
            additionalProperties.put(OpenMetadataProperty.ENCODING.name, encoding);
        }


        if (assetSubTypeName != null)
        {
            additionalProperties.put(OpenMetadataValidValues.ASSET_SUB_TYPE_NAME, assetSubTypeName);
        }

        if (additionalProperties.isEmpty())
        {
            additionalProperties = null;
        }

        String parentSetGUID = this.getParentSet(OpenMetadataType.DATA_FILE.typeName,
                                                 OpenMetadataProperty.FILE_TYPE.name,
                                                 null);

        this.archiveHelper.addValidValue(null,
                                         parentSetGUID,
                                         parentSetGUID,
                                         OpenMetadataType.VALID_VALUE_SET_TYPE_NAME,
                                         OpenMetadataType.VALID_VALUE_SET_TYPE_NAME,
                                         qualifiedName,
                                         fileTypeName,
                                         description,
                                         category,
                                         OpenMetadataValidValues.VALID_METADATA_VALUES_USAGE,
                                         OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE,
                                         fileTypeName,
                                         false,
                                         false,
                                         additionalProperties);

        if (deployedImplementationType != null)
        {
            String deployedImplementationTypeQName = constructValidValueQualifiedName(deployedImplementationType.getAssociatedTypeName(),
                                                                                      OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                                                      null,
                                                                                      deployedImplementationType.getDeployedImplementationType());
            this.archiveHelper.addConsistentValidValueRelationship(qualifiedName, deployedImplementationTypeQName);
        }
    }


    /**
     * Add reference data for a file name.
     *
     * @param fileName                   name of the file
     * @param fileType                   the file type
     * @param deployedImplementationType value for deployedImplementationType
     */
    private void addFileName(String                     fileName,
                             FileType                   fileType,
                             DeployedImplementationType deployedImplementationType)
    {
        String qualifiedName = constructValidValueQualifiedName(OpenMetadataType.DATA_FILE.typeName,
                                                                OpenMetadataProperty.FILE_NAME.name,
                                                                null,
                                                                fileName);

        String category = constructValidValueCategory(OpenMetadataType.DATA_FILE.typeName,
                                                      OpenMetadataProperty.FILE_NAME.name,
                                                      null);

        String parentSetGUID = this.getParentSet(OpenMetadataType.DATA_FILE.typeName,
                                                 OpenMetadataProperty.FILE_NAME.name,
                                                 null);

        this.archiveHelper.addValidValue(null,
                                         parentSetGUID,
                                         parentSetGUID,
                                         OpenMetadataType.VALID_VALUE_SET_TYPE_NAME,
                                         OpenMetadataType.VALID_VALUE_SET_TYPE_NAME,
                                         qualifiedName,
                                         fileName,
                                         null,
                                         category,
                                         OpenMetadataValidValues.VALID_METADATA_VALUES_USAGE,
                                         OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE,
                                         fileName,
                                         false,
                                         false,
                                         null);

        if (deployedImplementationType != null)
        {
            String deployedImplementationTypeQName = constructValidValueQualifiedName(deployedImplementationType.getAssociatedTypeName(),
                                                                                      OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                                                      null,
                                                                                      deployedImplementationType.getDeployedImplementationType());
            this.archiveHelper.addConsistentValidValueRelationship(qualifiedName, deployedImplementationTypeQName);
        }

        if (fileType != null)
        {
            String fileTypeQName = constructValidValueQualifiedName(OpenMetadataType.DATA_FILE.typeName,
                                                                    OpenMetadataProperty.FILE_TYPE.name,
                                                                    null,
                                                                    fileType.getFileTypeName());
            this.archiveHelper.addConsistentValidValueRelationship(qualifiedName, fileTypeQName);
        }
    }


    /**
     * Add reference data for a file extension.
     *
     * @param fileExtension   name of the file
     * @param fileTypes      list of matching file types
     */
    private void addFileExtension(String                     fileExtension,
                                  List<FileType>             fileTypes)
    {
        String qualifiedName = constructValidValueQualifiedName(OpenMetadataType.DATA_FILE.typeName,
                                                                OpenMetadataProperty.FILE_EXTENSION.name,
                                                                null,
                                                                fileExtension);

        String category = constructValidValueCategory(OpenMetadataType.DATA_FILE.typeName,
                                                      OpenMetadataProperty.FILE_EXTENSION.name,
                                                      null);

        String parentSetGUID = this.getParentSet(OpenMetadataType.DATA_FILE.typeName,
                                                 OpenMetadataProperty.FILE_EXTENSION.name,
                                                 null);

        this.archiveHelper.addValidValue(null, 
                                         parentSetGUID,
                                         parentSetGUID,
                                         OpenMetadataType.VALID_VALUE_SET_TYPE_NAME,
                                         OpenMetadataType.VALID_VALUE_SET_TYPE_NAME,
                                         qualifiedName,
                                         fileExtension,
                                         null,
                                         category,
                                         OpenMetadataValidValues.VALID_METADATA_VALUES_USAGE,
                                         OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE,
                                         fileExtension,
                                         false,
                                         false,
                                         null);

        if (fileTypes != null)
        {
            for (FileType fileType : fileTypes)
            {
                String fileTypeQName = constructValidValueQualifiedName(OpenMetadataType.DATA_FILE.typeName,
                                                                        OpenMetadataProperty.FILE_TYPE.name,
                                                                        null,
                                                                        fileType.getFileTypeName());
                this.archiveHelper.addConsistentValidValueRelationship(qualifiedName, fileTypeQName);
            }
        }
    }


    /**
     * Find or create the parent set for a valid value.
     *
     * @param typeName name of the type (can be null)
     * @param propertyName name of the property (can be null)
     * @param mapName name of the mapName (can be null)
     * @return unique identifier (guid) of the parent set
     */
    private String getParentSet(String                                 typeName,
                                String                                 propertyName,
                                String                                 mapName)
    {
        final String parentDescription = "Organizing set for valid metadata values";

        String parentQualifiedName = constructValidValueQualifiedName(typeName, propertyName, mapName, null);
        String parentSetGUID = parentValidValueQNameToGUIDMap.get(parentQualifiedName);

        if (parentSetGUID == null)
        {
            String grandParentSetGUID = null;
            String parentDisplayName = parentQualifiedName.substring(26);

            if (mapName != null)
            {
                grandParentSetGUID = getParentSet(typeName, propertyName, null);
            }
            else if (propertyName != null)
            {
                grandParentSetGUID = getParentSet(typeName, null, null);
            }
            else if (typeName != null)
            {
                grandParentSetGUID = getParentSet(null, null, null);
            }

            parentSetGUID =  archiveHelper.addValidValue(null,
                                                         grandParentSetGUID,
                                                         grandParentSetGUID,
                                                         OpenMetadataType.VALID_VALUE_SET_TYPE_NAME,
                                                         OpenMetadataType.VALID_VALUE_SET_TYPE_NAME,
                                                         parentQualifiedName,
                                                         parentDisplayName,
                                                         parentDescription,
                                                         constructValidValueCategory(typeName, propertyName, mapName),
                                                         OpenMetadataValidValues.VALID_METADATA_VALUES_USAGE,
                                                         OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE,
                                                         null,
                                                         false,
                                                         false,
                                                         null);

            parentValidValueQNameToGUIDMap.put(parentQualifiedName, parentSetGUID);

            return parentSetGUID;
        }
        else
        {
            return parentSetGUID;
        }
    }


    /**
     * Create an entity for the FileProvisioner governance engine.
     *
     * @return unique identifier for the governance engine
     */
    private String getFileProvisioningEngine()
    {
        final String governanceEngineName        = "FileProvisioning";
        final String governanceEngineDisplayName = "File Provisioning Engine";
        final String governanceEngineDescription = "Copies, moves or deletes a file on request.";

        return archiveHelper.addGovernanceEngine(OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                                                 governanceEngineName,
                                                 governanceEngineDisplayName,
                                                 governanceEngineDescription,
                                                 null,
                                                 null,
                                                 null,
                                                 null,
                                                 null,
                                                 null);
    }


    /**
     * Create an entity for the AssetOnboarding governance engine.
     *
     * @return unique identifier for the governance engine
     */
    private String getAssetOnboardingEngine()
    {
        final String assetGovernanceEngineName        = "AssetOnboarding";
        final String assetGovernanceEngineDisplayName = "Asset Onboarding Engine";
        final String assetGovernanceEngineDescription = "Monitors, validates and enriches metadata relating to assets as they are catalogued.";

        return archiveHelper.addGovernanceEngine(OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                                                 assetGovernanceEngineName,
                                                 assetGovernanceEngineDisplayName,
                                                 assetGovernanceEngineDescription,
                                                 null,
                                                 null,
                                                 null,
                                                 null,
                                                 null,
                                                 null);
    }


    /**
     * Create an entity for the AssetDiscovery governance engine.
     *
     * @return unique identifier for the governance engine
     */
    private String getAssetDiscoveryEngine()
    {
        final String assetDiscoveryEngineName        = "AssetDiscovery";
        final String assetDiscoveryEngineDisplayName = "AssetDiscovery Open Discovery Engine";
        final String assetDiscoveryEngineDescription = "Extracts metadata about a digital resource and attach it to its asset description.";

        return archiveHelper.addGovernanceEngine(OpenMetadataType.OPEN_DISCOVERY_ENGINE.typeName,
                                                 assetDiscoveryEngineName,
                                                 assetDiscoveryEngineDisplayName,
                                                 assetDiscoveryEngineDescription,
                                                 null,
                                                 null,
                                                 null,
                                                 null,
                                                 null,
                                                 null);
    }



    /**
     * Create an entity for the AssetSurvey governance engine.
     *
     * @return unique identifier for the governance engine
     */
    private String getAssetSurveyEngine()
    {
        final String assetSurveyEngineName        = "AssetSurvey";
        final String assetSurveyEngineDisplayName = "Asset Survey Engine";
        final String assetSurveyEngineDescription = "Extracts metadata about a digital resource and attach it to its asset description.";

        return archiveHelper.addGovernanceEngine(OpenMetadataType.SURVEY_ACTION_ENGINE.typeName,
                                                 assetSurveyEngineName,
                                                 assetSurveyEngineDisplayName,
                                                 assetSurveyEngineDescription,
                                                 null,
                                                 null,
                                                 null,
                                                 null,
                                                 null,
                                                 null);
    }



    /**
     * Create an entity for the FileProvisioning governance action service.
     *
     * @return descriptive information on the governance service
     */
    private GovernanceActionDescription getFileProvisioningGovernanceActionService()
    {
        final String governanceServiceName        = "file-provisioning-governance-action-service";
        final String governanceServiceDisplayName = "File {move, copy, delete} Governance Action Service";
        final String governanceServiceDescription = "Works with files.  The request type defines which action is taken.  " +
                                                            "The request parameters define the source file and destination, along with lineage options";
        final String ftpGovernanceServiceProviderClassName = MoveCopyFileGovernanceActionProvider.class.getName();

        MoveCopyFileGovernanceActionProvider provider = new MoveCopyFileGovernanceActionProvider();

        GovernanceActionDescription governanceActionDescription = new GovernanceActionDescription();

        governanceActionDescription.resourceUse = ResourceUse.PROVISION_RESOURCE;
        governanceActionDescription.supportedGuards = provider.getSupportedGuards();
        governanceActionDescription.actionTargetTypes = provider.getActionTargetTypes();
        governanceActionDescription.governanceServiceDescription = governanceServiceDescription;
        governanceActionDescription.governanceServiceGUID = archiveHelper.addGovernanceService(DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR,
                                                                                               ftpGovernanceServiceProviderClassName,
                                                                                               null,
                                                                                               governanceServiceName,
                                                                                               governanceServiceDisplayName,
                                                                                               governanceServiceDescription,
                                                                                               null);

        return governanceActionDescription;
    }


    /**
     * Create an entity for the generic watchdog governance action service.
     *
     * @return descriptive information on the governance service
     */
    private GovernanceActionDescription getWatchdogGovernanceActionService()
    {
        final String governanceServiceName = "new-measurements-watchdog-governance-action-service";
        final String governanceServiceDisplayName = "New Measurements Watchdog Governance Action Service";
        final String governanceServiceDescription = "Initiates a governance action process when a new weekly measurements file arrives.";
        final String governanceServiceProviderClassName = GenericFolderWatchdogGovernanceActionProvider.class.getName();

        GenericFolderWatchdogGovernanceActionProvider provider = new GenericFolderWatchdogGovernanceActionProvider();

        GovernanceActionDescription governanceActionDescription = new GovernanceActionDescription();

        governanceActionDescription.resourceUse = ResourceUse.WATCH_DOG;
        governanceActionDescription.supportedGuards = provider.getSupportedGuards();
        governanceActionDescription.actionTargetTypes = provider.getActionTargetTypes();
        governanceActionDescription.governanceServiceDescription = governanceServiceDescription;
        governanceActionDescription.governanceServiceGUID = archiveHelper.addGovernanceService(DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR,
                                                                                               governanceServiceProviderClassName,
                                                                                               null,
                                                                                               governanceServiceName,
                                                                                               governanceServiceDisplayName,
                                                                                               governanceServiceDescription,
                                                                                               null);
        return governanceActionDescription;
    }


    /**
     * Add a governance service that walks backwards through an asset's lineage to find an origin classification.
     * If found, the same origin is added to the asset.
     *
     * @return descriptive information on the governance service
     */
    private GovernanceActionDescription getZonePublisherGovernanceActionService()
    {
        final String governanceServiceName = "zone-publisher-governance-action-service";
        final String governanceServiceDisplayName = "Update Asset's Zone Membership Governance Action Service";
        final String governanceServiceDescription = "Set up the zone membership for one or more assets supplied as action targets.";
        final String governanceServiceProviderClassName = ZonePublisherGovernanceActionProvider.class.getName();

        ZonePublisherGovernanceActionProvider provider = new ZonePublisherGovernanceActionProvider();

        GovernanceActionDescription governanceActionDescription = new GovernanceActionDescription();

        governanceActionDescription.resourceUse = ResourceUse.IMPROVE_METADATA;
        governanceActionDescription.supportedGuards = provider.getSupportedGuards();
        governanceActionDescription.actionTargetTypes = provider.getActionTargetTypes();
        governanceActionDescription.governanceServiceDescription = governanceServiceDescription;
        governanceActionDescription.governanceServiceGUID = archiveHelper.addGovernanceService(DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR,
                                                                                               governanceServiceProviderClassName,
                                                                                               null,
                                                                                               governanceServiceName,
                                                                                               governanceServiceDisplayName,
                                                                                               governanceServiceDescription,
                                                                                               null);
        return governanceActionDescription;
    }


    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @return descriptive information on the governance service
     */
    private GovernanceActionDescription getOriginSeekerGovernanceActionService()
    {
        final String governanceServiceName = "origin-seeker-governance-action-service";
        final String governanceServiceDisplayName = "Locate and Set Origin Governance Action Service";
        final String governanceServiceDescription = "Navigates back through the lineage relationships to locate the origin classification(s) from the source(s) and sets it on the requested asset if the origin is unique.";
        final String governanceServiceProviderClassName = OriginSeekerGovernanceActionProvider.class.getName();

        OriginSeekerGovernanceActionProvider provider = new OriginSeekerGovernanceActionProvider();

        GovernanceActionDescription governanceActionDescription = new GovernanceActionDescription();

        governanceActionDescription.resourceUse = ResourceUse.IMPROVE_METADATA;
        governanceActionDescription.supportedGuards = provider.getSupportedGuards();
        governanceActionDescription.actionTargetTypes = provider.getActionTargetTypes();
        governanceActionDescription.governanceServiceDescription = governanceServiceDescription;
        governanceActionDescription.governanceServiceGUID = archiveHelper.addGovernanceService(DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR,
                                                                                               governanceServiceProviderClassName,
                                                                                               null,
                                                                                               governanceServiceName,
                                                                                               governanceServiceDisplayName,
                                                                                               governanceServiceDescription,
                                                                                               null);
        return governanceActionDescription;
    }


    /**
     * GovernanceActionDescription provides details for calling a governance service.
     */
    static class GovernanceActionDescription
    {
        String                        governanceServiceGUID = null;
        String                        governanceServiceDescription = null;
        Map<String, ActionTargetType> actionTargetTypes     = null;
        List<String>                  supportedGuards       = null;
        ResourceUse                   resourceUse           = null;
    }


    /**
     * Add details of a request type to the engine.
     *
     * @param governanceEngineGUID unique identifier of the engine
     * @param governanceEngineTypeName type of engine
     * @param governanceRequestType name of request type
     * @param serviceRequestType internal name of the request type
     * @param requestParameters any request parameters
     * @param governanceActionDescription description of the governance action if and
     */
    private void addRequestType(String                      governanceEngineGUID,
                                String                      governanceEngineTypeName,
                                String                      governanceRequestType,
                                String                      serviceRequestType,
                                Map<String, String>         requestParameters,
                                GovernanceActionDescription governanceActionDescription)
    {
        archiveHelper.addSupportedGovernanceService(governanceEngineGUID,
                                                    governanceRequestType,
                                                    serviceRequestType,
                                                    requestParameters,
                                                    governanceActionDescription.governanceServiceGUID);

        if (governanceActionDescription.actionTargetTypes != null)
        {
            String governanceActionTypeGUID = archiveHelper.addGovernanceActionType(null,
                                                                                    governanceEngineGUID,
                                                                                    governanceEngineTypeName,
                                                                                    "Egeria:GovernanceActionType:" + governanceEngineGUID + governanceRequestType,
                                                                                    governanceRequestType,
                                                                                    governanceActionDescription.governanceServiceDescription,
                                                                                    0,
                                                                                    governanceActionDescription.supportedGuards,
                                                                                    0,
                                                                                    null,
                                                                                    null,
                                                                                    null);

            if (governanceActionTypeGUID != null)
            {
                archiveHelper.addGovernanceActionExecutor(governanceActionTypeGUID,
                                                          governanceRequestType,
                                                          requestParameters,
                                                          null,
                                                          null,
                                                          null,
                                                          null,
                                                          governanceEngineGUID);

                for (String actionTargetTypeName : governanceActionDescription.actionTargetTypes.keySet())
                {
                    if (actionTargetTypeName != null)
                    {
                        ActionTargetType actionTargetType = governanceActionDescription.actionTargetTypes.get(actionTargetTypeName);

                        if (actionTargetType != null)
                        {
                            if (actionTargetType.getTypeName() != null)
                            {
                                String openMetadataTypeGUID = openMetadataTypeGUIDs.get(actionTargetType.getTypeName());

                                if (openMetadataTypeGUID != null)
                                {
                                    archiveHelper.addResourceListRelationshipByGUID(openMetadataTypeGUID,
                                                                                    governanceActionTypeGUID,
                                                                                    governanceActionDescription.resourceUse.getResourceUse(),
                                                                                    governanceActionDescription.governanceServiceDescription,
                                                                                    requestParameters,
                                                                                    false);
                                }
                            }

                            if (actionTargetType.getDeployedImplementationType() != null)
                            {
                                String deployedImplementationTypeGUID = deployedImplementationTypeGUIDs.get(actionTargetType.getDeployedImplementationType());

                                if (deployedImplementationTypeGUID != null)
                                {
                                    archiveHelper.addResourceListRelationshipByGUID(deployedImplementationTypeGUID,
                                                                                    governanceActionTypeGUID,
                                                                                    governanceActionDescription.resourceUse.getResourceUse(),
                                                                                    governanceActionDescription.governanceServiceDescription,
                                                                                    requestParameters,
                                                                                    false);
                                }
                            }
                        }
                    }
                }
            }
        }
    }



    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @param governanceEngineGUID unique identifier of the governance engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addWatchNestedInFolderRequestType(String                      governanceEngineGUID,
                                                   GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "watch-for-new-files-in-folder";
        final String serviceRequestType = "watch-nested-in-folder";

        this.addRequestType(governanceEngineGUID,
                            OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                            governanceRequestType,
                            serviceRequestType,
                            null,
                            governanceActionDescription);
    }


    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @param governanceEngineGUID unique identifier of the governance engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addCopyFileRequestType(String                      governanceEngineGUID,
                                        GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "copy-file";

        this.addRequestType(governanceEngineGUID,
                            OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                            governanceRequestType,
                            null,
                            null,
                            governanceActionDescription);
    }



    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @param governanceEngineGUID unique identifier of the governance engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addMoveFileRequestType(String                      governanceEngineGUID,
                                        GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "move-file";

        this.addRequestType(governanceEngineGUID,
                            OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                            governanceRequestType,
                            null,
                            null,
                            governanceActionDescription);
    }


    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @param governanceEngineGUID unique identifier of the governance engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addDeleteFileRequestType(String                      governanceEngineGUID,
                                          GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "delete-file";

        this.addRequestType(governanceEngineGUID,
                            OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                            governanceRequestType,
                            null,
                            null,
                            governanceActionDescription);
    }


    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @param governanceEngineGUID unique identifier of the governance engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addSeekOriginRequestType(String                      governanceEngineGUID,
                                          GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "seek-origin-of-asset";

        this.addRequestType(governanceEngineGUID,
                            OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                            governanceRequestType,
                            null,
                            null,
                            governanceActionDescription);
    }


    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @param governanceEngineGUID unique identifier of the governance engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addSetZoneMembershipRequestType(String                      governanceEngineGUID,
                                                 GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "set-zone-membership-for-asset";

        this.addRequestType(governanceEngineGUID,
                            OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                            governanceRequestType,
                            null,
                            null,
                            governanceActionDescription);
    }


    /**
     * Create an entity for the Apache Atlas Survey governance service.
     *
     * @return unique identifier for the governance service
     */
    private GovernanceActionDescription getApacheAtlasDiscoveryService()
    {
        final String discoveryServiceName = "apache-atlas-discovery-service";
        final String discoveryServiceDisplayName = "Apache Atlas Discovery Service";
        final String discoveryServiceDescription = "Discovers the types and instances found in an Apache Atlas server.";
        final String discoveryServiceProviderClassName = DiscoverApacheAtlasProvider.class.getName();

        GovernanceActionDescription governanceActionDescription = new GovernanceActionDescription();

        governanceActionDescription.governanceServiceGUID = archiveHelper.addGovernanceService(DeployedImplementationType.OPEN_DISCOVERY_SERVICE_CONNECTOR,
                                                                                               discoveryServiceProviderClassName,
                                                                                               null,
                                                                                               discoveryServiceName,
                                                                                               discoveryServiceDisplayName,
                                                                                               discoveryServiceDescription,
                                                                                               null);

        return governanceActionDescription;
    }


    /**
     * Create an entity for the CSV File Survey governance service.
     *
     * @return unique identifier for the governance service
     */
    private GovernanceActionDescription getCSVFileSurveyService()
    {
        final String surveyServiceName = "csv-file-survey-service";
        final String surveyServiceDisplayName = "CSV File Survey Service";
        final String surveyServiceDescription = "Discovers columns within a CSV File.";
        final String surveyServiceProviderClassName = CSVSurveyServiceProvider.class.getName();

        CSVSurveyServiceProvider provider = new CSVSurveyServiceProvider();

        GovernanceActionDescription governanceActionDescription = new GovernanceActionDescription();

        governanceActionDescription.resourceUse = ResourceUse.SURVEY_RESOURCE;
        governanceActionDescription.supportedGuards = provider.getSupportedGuards();
        governanceActionDescription.actionTargetTypes = provider.getActionTargetTypes();
        governanceActionDescription.governanceServiceDescription = surveyServiceDescription;
        governanceActionDescription.governanceServiceGUID = archiveHelper.addGovernanceService(DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR,
                                                                                               surveyServiceProviderClassName,
                                                                                               null,
                                                                                               surveyServiceName,
                                                                                               surveyServiceDisplayName,
                                                                                               surveyServiceDescription,
                                                                                               null);

        return governanceActionDescription;
    }


    /**
     * Create an entity for the File Survey governance service.
     *
     * @return unique identifier for the governance service
     */
    private GovernanceActionDescription getDataFileSurveyService()
    {
        final String surveyServiceName = "data-file-survey-service";
        final String surveyServiceDisplayName = "Data File Survey Service";
        final String surveyServiceDescription = "Discovers the name, extension, file type and other characteristics of a file.";
        final String surveyServiceProviderClassName = FileSurveyServiceProvider.class.getName();

        FileSurveyServiceProvider provider = new FileSurveyServiceProvider();

        GovernanceActionDescription governanceActionDescription = new GovernanceActionDescription();

        governanceActionDescription.resourceUse = ResourceUse.SURVEY_RESOURCE;
        governanceActionDescription.supportedGuards = provider.getSupportedGuards();
        governanceActionDescription.actionTargetTypes = provider.getActionTargetTypes();
        governanceActionDescription.governanceServiceDescription = surveyServiceDescription;
        governanceActionDescription.governanceServiceGUID = archiveHelper.addGovernanceService(DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR,
                                                                          surveyServiceProviderClassName,
                                                                          null,
                                                                          surveyServiceName,
                                                                          surveyServiceDisplayName,
                                                                          surveyServiceDescription,
                                                                          null);

        return governanceActionDescription;
    }


    /**
     * Create an entity for the Folder Survey governance service.
     *
     * @return unique identifier for the governance service
     */
    private GovernanceActionDescription getFolderSurveyService()
    {
        final String surveyServiceName = "folder-survey-service";
        final String surveyServiceDisplayName = "Folder (directory) Survey Service";
        final String surveyServiceDescription = "Discovers the types of files located within a file system directory (and its sub-directories).";
        final String surveyServiceProviderClassName = FolderSurveyServiceProvider.class.getName();

        FolderSurveyServiceProvider provider = new FolderSurveyServiceProvider();

        GovernanceActionDescription governanceActionDescription = new GovernanceActionDescription();

        governanceActionDescription.resourceUse = ResourceUse.SURVEY_RESOURCE;
        governanceActionDescription.supportedGuards = provider.getSupportedGuards();
        governanceActionDescription.actionTargetTypes = provider.getActionTargetTypes();
        governanceActionDescription.governanceServiceDescription = surveyServiceDescription;
        governanceActionDescription.governanceServiceGUID = archiveHelper.addGovernanceService(DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR,
                                                                          surveyServiceProviderClassName,
                                                                          null,
                                                                          surveyServiceName,
                                                                          surveyServiceDisplayName,
                                                                          surveyServiceDescription,
                                                                          null);

        return governanceActionDescription;
    }


    /**
     * Create the relationship between a governance engine and a governance service that defines the request type.
     *
     * @param governanceEngineGUID unique identifier of the engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addCSVFileRequestType(String                      governanceEngineGUID,
                                       GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "survey-csv-file";

        this.addRequestType(governanceEngineGUID,
                            OpenMetadataType.SURVEY_ACTION_ENGINE.typeName,
                            governanceRequestType,
                            null,
                            null,
                            governanceActionDescription);
    }


    /**
     * Create the relationship between a governance engine and a governance service that defines the request type.
     *
     * @param governanceEngineGUID unique identifier of the engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addDataFileRequestType(String                      governanceEngineGUID,
                                        GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "survey-data-file";

        this.addRequestType(governanceEngineGUID,
                            OpenMetadataType.SURVEY_ACTION_ENGINE.typeName,
                            governanceRequestType,
                            null,
                            null,
                            governanceActionDescription);
    }


    /**
     * Create the relationship between a governance engine and a governance service that defines the request type.
     *
     * @param governanceEngineGUID unique identifier of the engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addFolderRequestType(String                      governanceEngineGUID,
                                      GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "survey-folder";

        this.addRequestType(governanceEngineGUID,
                            OpenMetadataType.SURVEY_ACTION_ENGINE.typeName,
                            governanceRequestType,
                            null,
                            null,
                            governanceActionDescription);
    }


    /**
     * Generates and writes out an open metadata archive containing all the connector types
     * describing the Egeria project open connectors.
     */
    private void writeOpenMetadataArchive()
    {
        try
        {
            System.out.println("Writing to file: " + archiveFileName);

            super.writeOpenMetadataArchive(archiveFileName, this.getOpenMetadataArchive());
        }
        catch (Exception error)
        {
            System.out.println("error is " + error);
        }
    }


    /**
     * Main program to initiate the archive writer for the connector types for data store connectors supported by
     * the Egeria project.
     *
     * @param args ignored
     */
    public static void main(String[] args)
    {
        try
        {
            OpenConnectorArchiveWriter archiveWriter = new OpenConnectorArchiveWriter();

            archiveWriter.writeOpenMetadataArchive();
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error);
            System.exit(-1);
        }
    }
}