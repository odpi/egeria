/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.openconnectors;

import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStoreProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVFileStoreProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.datafolder.DataFolderProvider;
import org.odpi.openmetadata.adapters.connectors.discoveryservices.discoveratlas.DiscoverApacheAtlasProvider;
import org.odpi.openmetadata.adapters.connectors.discoveryservices.discovercsv.CSVDiscoveryServiceProvider;
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
import org.odpi.openmetadata.adapters.connectors.integration.openlineage.APIBasedOpenLineageLogStoreProvider;
import org.odpi.openmetadata.adapters.connectors.integration.openlineage.FileBasedOpenLineageLogStoreProvider;
import org.odpi.openmetadata.adapters.connectors.integration.openlineage.GovernanceActionOpenLineageIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.openlineage.OpenLineageCataloguerIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.openlineage.OpenLineageEventReceiverIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.ApacheAtlasRESTProvider;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnectorProvider;
import org.odpi.openmetadata.adapters.connectors.secretsstore.envar.EnvVarSecretsStoreProvider;
import org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.GovernanceServicesDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.IntegrationServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.ServerTypeClassification;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataTypesMapper;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataValidValues;
import org.odpi.openmetadata.frameworks.governanceaction.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.governanceaction.refdata.FileExtension;
import org.odpi.openmetadata.frameworks.governanceaction.refdata.FileName;
import org.odpi.openmetadata.frameworks.governanceaction.refdata.FileType;
import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchive;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveWriter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.samples.archiveutilities.GovernanceArchiveHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final Map<String, String>     parentValidValueQNameToGUIDMap = new HashMap<>();

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


        Map<String, String> deployedImplementationTypeGUIDs = new HashMap<>();

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

        Map<String, String> serverTypeGUIDs = new HashMap<>();
        Map<String, String> serviceGUIDs    = new HashMap<>();

        for (ServerTypeClassification serverTypeClassification : ServerTypeClassification.values())
        {
            String guid = this.addDeployedImplementationType(serverTypeClassification.getServerTypeName(),
                                                             OpenMetadataTypesMapper.SOFTWARE_SERVER_TYPE_NAME,
                                                             serverTypeClassification.getServerTypeDescription(),
                                                             serverTypeClassification.getServerTypeWiki());

            serverTypeGUIDs.put(serverTypeClassification.getServerTypeName(), guid);
        }

        String serverTypeGUID = serverTypeGUIDs.get(ServerTypeClassification.METADATA_ACCESS_POINT.getServerTypeName());
        String serverTypeGUID2 = serverTypeGUIDs.get(ServerTypeClassification.METADATA_SERVER.getServerTypeName());

        for (CommonServicesDescription commonServicesDescription : CommonServicesDescription.values())
        {
            String guid = this.addDeployedImplementationType(commonServicesDescription.getServiceName(),
                                                             OpenMetadataTypesMapper.SOFTWARE_SERVICE_TYPE_NAME,
                                                             commonServicesDescription.getServiceDescription(),
                                                             commonServicesDescription.getServiceWiki());

            serviceGUIDs.put(commonServicesDescription.getServiceName(), guid);
        }

        for (GovernanceServicesDescription governanceServicesDescription : GovernanceServicesDescription.values())
        {
            String guid = this.addDeployedImplementationType(governanceServicesDescription.getServiceName(),
                                                             OpenMetadataTypesMapper.SOFTWARE_SERVICE_TYPE_NAME,
                                                             governanceServicesDescription.getServiceDescription(),
                                                             governanceServicesDescription.getServiceWiki());

            serviceGUIDs.put(governanceServicesDescription.getServiceName(), guid);
        }

        for (AccessServiceDescription accessServiceDescription : AccessServiceDescription.values())
        {
            String guid = this.addDeployedImplementationType(accessServiceDescription.getAccessServiceFullName(),
                                                             OpenMetadataTypesMapper.SOFTWARE_SERVICE_TYPE_NAME,
                                                             accessServiceDescription.getAccessServiceDescription(),
                                                             accessServiceDescription.getAccessServiceWiki());

            serviceGUIDs.put(accessServiceDescription.getAccessServiceFullName(), guid);

            archiveHelper.addResourceListRelationshipByGUID(serverTypeGUID,
                                                            guid,
                                                            OpenMetadataValidValues.HOSTED_SERVICE_RESOURCE_USE);

            archiveHelper.addResourceListRelationshipByGUID(serverTypeGUID2,
                                                            guid,
                                                            OpenMetadataValidValues.HOSTED_SERVICE_RESOURCE_USE);
        }

        serverTypeGUID = serverTypeGUIDs.get(ServerTypeClassification.VIEW_SERVER.getServerTypeName());

        for (ViewServiceDescription viewServiceDescription : ViewServiceDescription.values())
        {
            String guid = this.addDeployedImplementationType(viewServiceDescription.getViewServiceFullName(),
                                                             OpenMetadataTypesMapper.SOFTWARE_SERVICE_TYPE_NAME,
                                                             viewServiceDescription.getViewServiceDescription(),
                                                             viewServiceDescription.getViewServiceWiki());

            archiveHelper.addResourceListRelationshipByGUID(serverTypeGUID,
                                                            guid,
                                                            OpenMetadataValidValues.HOSTED_SERVICE_RESOURCE_USE);

            archiveHelper.addResourceListRelationshipByGUID(guid,
                                                            serviceGUIDs.get(viewServiceDescription.getViewServicePartnerService()),
                                                            OpenMetadataValidValues.CALLED_SERVICE_RESOURCE_USE);
        }

        serverTypeGUID = serverTypeGUIDs.get(ServerTypeClassification.ENGINE_HOST.getServerTypeName());

        for (EngineServiceDescription engineServiceDescription : EngineServiceDescription.values())
        {
            String guid = this.addDeployedImplementationType(engineServiceDescription.getEngineServiceFullName(),
                                                             OpenMetadataTypesMapper.SOFTWARE_SERVICE_TYPE_NAME,
                                                             engineServiceDescription.getEngineServiceDescription(),
                                                             engineServiceDescription.getEngineServiceWiki());

            archiveHelper.addResourceListRelationshipByGUID(serverTypeGUID,
                                                            guid,
                                                            OpenMetadataValidValues.HOSTED_SERVICE_RESOURCE_USE);

            archiveHelper.addResourceListRelationshipByGUID(guid,
                                                            serviceGUIDs.get(engineServiceDescription.getEngineServicePartnerService()),
                                                            OpenMetadataValidValues.CALLED_SERVICE_RESOURCE_USE);

            String governanceEngineGUID = deployedImplementationTypeGUIDs.get(engineServiceDescription.getHostedGovernanceEngineType());
            String governanceServiceGUID = deployedImplementationTypeGUIDs.get(engineServiceDescription.getHostedGovernanceServiceType());

            if (governanceEngineGUID != null)
            {
                archiveHelper.addResourceListRelationshipByGUID(guid,
                                                                governanceEngineGUID,
                                                                OpenMetadataValidValues.HOSTED_GOVERNANCE_ENGINE_RESOURCE_USE);

                if (governanceServiceGUID != null)
                {
                    archiveHelper.addResourceListRelationshipByGUID(governanceEngineGUID,
                                                                    governanceServiceGUID,
                                                                    OpenMetadataValidValues.HOSTED_CONNECTOR_RESOURCE_USE);
                }
            }
        }

        for (IntegrationServiceDescription integrationServiceDescription : IntegrationServiceDescription.values())
        {
            String guid = this.addDeployedImplementationType(integrationServiceDescription.getIntegrationServiceFullName(),
                                                             OpenMetadataTypesMapper.SOFTWARE_SERVICE_TYPE_NAME,
                                                             integrationServiceDescription.getIntegrationServiceDescription(),
                                                             integrationServiceDescription.getIntegrationServiceWiki());

            archiveHelper.addResourceListRelationshipByGUID(serverTypeGUID,
                                                            guid,
                                                            OpenMetadataValidValues.HOSTED_SERVICE_RESOURCE_USE);

            archiveHelper.addResourceListRelationshipByGUID(guid,
                                                            serviceGUIDs.get(integrationServiceDescription.getIntegrationServicePartnerOMAS().getAccessServiceFullName()),
                                                            OpenMetadataValidValues.CALLED_SERVICE_RESOURCE_USE);

            String connectorTypeGUID = deployedImplementationTypeGUIDs.get(integrationServiceDescription.getConnectorDeployedImplementationType());

            if (connectorTypeGUID != null)
            {
                archiveHelper.addResourceListRelationshipByGUID(guid,
                                                                connectorTypeGUID,
                                                                OpenMetadataValidValues.HOSTED_CONNECTOR_RESOURCE_USE);
            }
        }

        for (FileType fileType : FileType.values())
        {
            this.addFileType(fileType.getFileTypeName(),
                             fileType.getEncoding(),
                             fileType.getAssetSubTypeName(),
                             fileType.getDeployedImplementationType(),
                             fileType.getDescription());
        }

        for (FileName fileName : FileName.values())
        {
            this.addFileName(fileName.getFileName(),
                             fileName.getFileType(),
                             fileName.getDeployedImplementationType());
        }

        for (FileExtension fileExtension : FileExtension.values())
        {
            this.addFileExtension(fileExtension.getFileExtension(), fileExtension.getFileTypes());
        }


        /*
         * Integration Connector Types may need to link to deployedImplementationType valid value element
         */
        archiveHelper.addConnectorType(fileConnectorCategoryGUID, new CSVFileStoreProvider());
        archiveHelper.addConnectorType(fileConnectorCategoryGUID, new DataFolderProvider());
        archiveHelper.addConnectorType(fileConnectorCategoryGUID, new BasicFileStoreProvider());
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

        String fileProvisionerGUID = this.getFileProvisioningGovernanceActionService();
        String watchDogServiceGUID = this.getWatchdogGovernanceActionService();
        String originSeekerGUID = this.getOriginSeekerGovernanceActionService();
        String zonePublisherGUID = this.getZonePublisherGovernanceActionService();
        String csvDiscoveryGUID = this.getCSVAssetDiscoveryService();
        String atlasDiscoveryGUID = this.getApacheAtlasDiscoveryService();

        String fileProvisioningEngineGUID = this.getFileProvisioningEngine();

        this.addCopyFileRequestType(fileProvisioningEngineGUID, fileProvisionerGUID);
        this.addMoveFileRequestType(fileProvisioningEngineGUID, fileProvisionerGUID);
        this.addDeleteFileRequestType(fileProvisioningEngineGUID, fileProvisionerGUID);

        String assetGovernanceEngineGUID = this.getAssetGovernanceEngine();

        this.addFTPFileRequestType(assetGovernanceEngineGUID, fileProvisionerGUID);
        this.addWatchNestedInFolderRequestType(assetGovernanceEngineGUID, watchDogServiceGUID);
        this.addSeekOriginRequestType(assetGovernanceEngineGUID, originSeekerGUID);
        this.addSetZoneMembershipRequestType(assetGovernanceEngineGUID, zonePublisherGUID);
        this.addMoveFileRequestType(assetGovernanceEngineGUID, fileProvisionerGUID);
        this.addDeleteFileRequestType(assetGovernanceEngineGUID, fileProvisionerGUID);

        String assetDiscoveryEngineGUID = this.getAssetDiscoveryEngine();

        this.addSmallCSVRequestType(assetDiscoveryEngineGUID, csvDiscoveryGUID);
        this.addApacheAtlasRequestType(assetDiscoveryEngineGUID, atlasDiscoveryGUID);

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
                                                                OpenMetadataTypesMapper.DEPLOYED_IMPLEMENTATION_TYPE_PROPERTY_NAME,
                                                                null,
                                                                deployedImplementationType);

        String category = constructValidValueCategory(associatedTypeName,
                                                      OpenMetadataTypesMapper.DEPLOYED_IMPLEMENTATION_TYPE_PROPERTY_NAME,
                                                      null);

        return this.addDeployedImplementationType(deployedImplementationType, associatedTypeName, qualifiedName, category, description, wikiLink);
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
                                                 OpenMetadataTypesMapper.DEPLOYED_IMPLEMENTATION_TYPE_PROPERTY_NAME,
                                                 null);

        String validValueGUID = this.archiveHelper.addValidValue(parentSetGUID,
                                                                 parentSetGUID,
                                                                 OpenMetadataTypesMapper.VALID_VALUE_SET_TYPE_NAME,
                                                                 OpenMetadataTypesMapper.VALID_VALUE_SET_TYPE_NAME,
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
        String qualifiedName = constructValidValueQualifiedName(OpenMetadataTypesMapper.DATA_FILE_TYPE_NAME,
                                                                OpenMetadataTypesMapper.FILE_TYPE_PROPERTY_NAME,
                                                                null,
                                                                fileTypeName);

        String category = constructValidValueCategory(OpenMetadataTypesMapper.DATA_FILE_TYPE_NAME,
                                                      OpenMetadataTypesMapper.FILE_TYPE_PROPERTY_NAME,
                                                      null);

        Map<String, String> additionalProperties = new HashMap<>();

        if (encoding != null)
        {
            additionalProperties.put(OpenMetadataTypesMapper.ENCODING_PROPERTY_NAME, encoding);
        }


        if (assetSubTypeName != null)
        {
            additionalProperties.put("assetSubTypeName", assetSubTypeName);
        }

        if (additionalProperties.isEmpty())
        {
            additionalProperties = null;
        }

        String parentSetGUID = this.getParentSet(OpenMetadataTypesMapper.DATA_FILE_TYPE_NAME,
                                                 OpenMetadataTypesMapper.FILE_TYPE_PROPERTY_NAME,
                                                 null);

        this.archiveHelper.addValidValue(parentSetGUID,
                                         parentSetGUID,
                                         OpenMetadataTypesMapper.VALID_VALUE_SET_TYPE_NAME,
                                         OpenMetadataTypesMapper.VALID_VALUE_SET_TYPE_NAME,
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
                                                                                      OpenMetadataTypesMapper.DEPLOYED_IMPLEMENTATION_TYPE_PROPERTY_NAME,
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
        String qualifiedName = constructValidValueQualifiedName(OpenMetadataTypesMapper.DATA_FILE_TYPE_NAME,
                                                                OpenMetadataTypesMapper.FILE_NAME_PROPERTY_NAME,
                                                                null,
                                                                fileName);

        String category = constructValidValueCategory(OpenMetadataTypesMapper.DATA_FILE_TYPE_NAME,
                                                      OpenMetadataTypesMapper.FILE_NAME_PROPERTY_NAME,
                                                      null);

        String parentSetGUID = this.getParentSet(OpenMetadataTypesMapper.DATA_FILE_TYPE_NAME,
                                                 OpenMetadataTypesMapper.FILE_NAME_PROPERTY_NAME,
                                                 null);

        this.archiveHelper.addValidValue(parentSetGUID,
                                         parentSetGUID,
                                         OpenMetadataTypesMapper.VALID_VALUE_SET_TYPE_NAME,
                                         OpenMetadataTypesMapper.VALID_VALUE_SET_TYPE_NAME,
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
                                                                                      OpenMetadataTypesMapper.DEPLOYED_IMPLEMENTATION_TYPE_PROPERTY_NAME,
                                                                                      null,
                                                                                      deployedImplementationType.getDeployedImplementationType());
            this.archiveHelper.addConsistentValidValueRelationship(qualifiedName, deployedImplementationTypeQName);
        }

        if (fileType != null)
        {
            String fileTypeQName = constructValidValueQualifiedName(OpenMetadataTypesMapper.DATA_FILE_TYPE_NAME,
                                                                    OpenMetadataTypesMapper.FILE_TYPE_PROPERTY_NAME,
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
        String qualifiedName = constructValidValueQualifiedName(OpenMetadataTypesMapper.DATA_FILE_TYPE_NAME,
                                                                OpenMetadataTypesMapper.FILE_EXTENSION_PROPERTY_NAME,
                                                                null,
                                                                fileExtension);

        String category = constructValidValueCategory(OpenMetadataTypesMapper.DATA_FILE_TYPE_NAME,
                                                      OpenMetadataTypesMapper.FILE_EXTENSION_PROPERTY_NAME,
                                                      null);

        String parentSetGUID = this.getParentSet(OpenMetadataTypesMapper.DATA_FILE_TYPE_NAME,
                                                 OpenMetadataTypesMapper.FILE_EXTENSION_PROPERTY_NAME,
                                                 null);

        this.archiveHelper.addValidValue(parentSetGUID,
                                         parentSetGUID,
                                         OpenMetadataTypesMapper.VALID_VALUE_SET_TYPE_NAME,
                                         OpenMetadataTypesMapper.VALID_VALUE_SET_TYPE_NAME,
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
                String fileTypeQName = constructValidValueQualifiedName(OpenMetadataTypesMapper.DATA_FILE_TYPE_NAME,
                                                                        OpenMetadataTypesMapper.FILE_TYPE_PROPERTY_NAME,
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

            parentSetGUID =  archiveHelper.addValidValue(grandParentSetGUID,
                                                         grandParentSetGUID,
                                                         OpenMetadataTypesMapper.VALID_VALUE_SET_TYPE_NAME,
                                                         OpenMetadataTypesMapper.VALID_VALUE_SET_TYPE_NAME,
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
        final String governanceEngineDisplayName = "File Provisioning Governance Action Engine";
        final String governanceEngineDescription = "Copies, moves or deletes a file on request.";

        return archiveHelper.addGovernanceEngine(OpenMetadataTypesMapper.GOVERNANCE_ACTION_ENGINE_TYPE_NAME,
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
     * Create an entity for the AssetGovernance governance engine.
     *
     * @return unique identifier for the governance engine
     */
    private String getAssetGovernanceEngine()
    {
        final String assetGovernanceEngineName        = "AssetGovernance";
        final String assetGovernanceEngineDisplayName = "AssetGovernance Governance Action Engine";
        final String assetGovernanceEngineDescription = "Monitors, validates and enriches metadata relating to assets.";

        return archiveHelper.addGovernanceEngine(OpenMetadataTypesMapper.GOVERNANCE_ACTION_ENGINE_TYPE_NAME,
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

        return archiveHelper.addGovernanceEngine(OpenMetadataTypesMapper.OPEN_DISCOVERY_ENGINE_TYPE_NAME,
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
     * Create an entity for the FileProvisioning governance action service.
     *
     * @return unique identifier for the governance engine
     */
    private String getFileProvisioningGovernanceActionService()
    {
        final String governanceServiceName        = "file-provisioning-governance-action-service";
        final String governanceServiceDisplayName = "File {move, copy, delete} Governance Action Service";
        final String governanceServiceDescription = "Works with files.  The request type defines which action is taken.  " +
                                                            "The request parameters define the source file and destination, along with lineage options";
        final String ftpGovernanceServiceProviderClassName = MoveCopyFileGovernanceActionProvider.class.getName();

        return archiveHelper.addGovernanceService(OpenMetadataTypesMapper.GOVERNANCE_ACTION_SERVICE_TYPE_NAME,
                                                  ftpGovernanceServiceProviderClassName,
                                                  null,
                                                  governanceServiceName,
                                                  governanceServiceDisplayName,
                                                  governanceServiceDescription,
                                                  null,
                                                  null);
    }


    /**
     * Create an entity for the generic watchdog governance action service.
     *
     * @return unique identifier for the governance engine
     */
    private String getWatchdogGovernanceActionService()
    {
        final String governanceServiceName = "new-measurements-watchdog-governance-action-service";
        final String governanceServiceDisplayName = "New Measurements Watchdog Governance Action Service";
        final String governanceServiceDescription = "Initiates a governance action process when a new weekly measurements file arrives.";
        final String governanceServiceProviderClassName = GenericFolderWatchdogGovernanceActionProvider.class.getName();

        return archiveHelper.addGovernanceService(OpenMetadataTypesMapper.GOVERNANCE_ACTION_SERVICE_TYPE_NAME,
                                                  governanceServiceProviderClassName,
                                                  null,
                                                  governanceServiceName,
                                                  governanceServiceDisplayName,
                                                  governanceServiceDescription,
                                                  null,
                                                  null);
    }


    /**
     * Add a governance service that walks backwards through an asset's lineage to find an origin classification.  If found, the same origin is added
     * to the asset.
     *
     * @return unique identifier fo the governance service
     */
    private String getZonePublisherGovernanceActionService()
    {
        final String governanceServiceName = "zone-publisher-governance-action-service";
        final String governanceServiceDisplayName = "Update Asset's Zone Membership Governance Action Service";
        final String governanceServiceDescription = "Set up the zone membership for one or more assets supplied as action targets.";
        final String governanceServiceProviderClassName = ZonePublisherGovernanceActionProvider.class.getName();

        return archiveHelper.addGovernanceService(OpenMetadataTypesMapper.GOVERNANCE_ACTION_SERVICE_TYPE_NAME,
                                                  governanceServiceProviderClassName,
                                                  null,
                                                  governanceServiceName,
                                                  governanceServiceDisplayName,
                                                  governanceServiceDescription,
                                                  null,
                                                  null);
    }


    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @return unique identifier fo the governance service
     */
    private String getOriginSeekerGovernanceActionService()
    {
        final String governanceServiceName = "origin-seeker-governance-action-service";
        final String governanceServiceDisplayName = "Locate and Set Origin Governance Action Service";
        final String governanceServiceDescription = "Navigates back through the lineage relationships to locate the origin classification(s) from the source(s) and sets it on the requested asset if the origin is unique.";
        final String governanceServiceProviderClassName = OriginSeekerGovernanceActionProvider.class.getName();

        return archiveHelper.addGovernanceService(OpenMetadataTypesMapper.GOVERNANCE_ACTION_SERVICE_TYPE_NAME,
                                                  governanceServiceProviderClassName,
                                                  null,
                                                  governanceServiceName,
                                                  governanceServiceDisplayName,
                                                  governanceServiceDescription,
                                                  null,
                                                  null);
    }


    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @param governanceEngineGUID unique identifier of the governance engine
     * @param governanceServiceGUID unique identifier of the governance service
     */
    private void addFTPFileRequestType(String governanceEngineGUID,
                                       String governanceServiceGUID)
    {
        final String governanceRequestType = "simulate-ftp";
        final String serviceRequestType = "copy-file";
        final String noLineagePropertyName = "noLineage";

        Map<String, String> requestParameters = new HashMap<>();

        requestParameters.put(noLineagePropertyName, "");

        archiveHelper.addSupportedGovernanceService(governanceEngineGUID, governanceRequestType, serviceRequestType, requestParameters, governanceServiceGUID);
    }


    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @param governanceEngineGUID unique identifier of the governance engine
     * @param governanceServiceGUID unique identifier of the governance service
     */
    private void addWatchNestedInFolderRequestType(String governanceEngineGUID,
                                                   String governanceServiceGUID)
    {
        final String governanceRequestType = "watch-for-new-files";
        final String serviceRequestType = "watch-nested-in-folder";

        archiveHelper.addSupportedGovernanceService(governanceEngineGUID, governanceRequestType, serviceRequestType, null, governanceServiceGUID);
    }


    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @param governanceEngineGUID unique identifier of the governance engine
     * @param governanceServiceGUID unique identifier of the governance service
     */
    private void addCopyFileRequestType(String governanceEngineGUID,
                                        String governanceServiceGUID)
    {
        final String governanceRequestType = "copy-file";

        archiveHelper.addSupportedGovernanceService(governanceEngineGUID, governanceRequestType, null, null, governanceServiceGUID);
    }



    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @param governanceEngineGUID unique identifier of the governance engine
     * @param governanceServiceGUID unique identifier of the governance service
     */
    private void addMoveFileRequestType(String governanceEngineGUID,
                                        String governanceServiceGUID)
    {
        final String governanceRequestType = "move-file";

        archiveHelper.addSupportedGovernanceService(governanceEngineGUID, governanceRequestType, null, null, governanceServiceGUID);
    }



    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @param governanceEngineGUID unique identifier of the governance engine
     * @param governanceServiceGUID unique identifier of the governance service
     */
    private void addDeleteFileRequestType(String governanceEngineGUID,
                                          String governanceServiceGUID)
    {
        final String governanceRequestType = "delete-file";

        archiveHelper.addSupportedGovernanceService(governanceEngineGUID, governanceRequestType, null, null, governanceServiceGUID);
    }


    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @param governanceEngineGUID unique identifier of the governance engine
     * @param governanceServiceGUID unique identifier of the governance service
     */
    private void addSeekOriginRequestType(String governanceEngineGUID,
                                          String governanceServiceGUID)
    {
        final String governanceServiceRequestType = "seek-origin";

        archiveHelper.addSupportedGovernanceService(governanceEngineGUID, governanceServiceRequestType, null, null, governanceServiceGUID);
    }



    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @param governanceEngineGUID unique identifier of the governance engine
     * @param governanceServiceGUID unique identifier of the governance service
     */
    private void addSetZoneMembershipRequestType(String governanceEngineGUID,
                                                 String governanceServiceGUID)
    {
        final String governanceServiceRequestType = "set-zone-membership";

        archiveHelper.addSupportedGovernanceService(governanceEngineGUID, governanceServiceRequestType, null, null, governanceServiceGUID);
    }


    /**
     * Create an entity for the CSV Asset Discovery governance service.
     *
     * @return unique identifier for the governance engine
     */
    private String getCSVAssetDiscoveryService()
    {
        final String discoveryServiceName = "csv-asset-discovery-service";
        final String discoveryServiceDisplayName = "CSV Asset Discovery Service";
        final String discoveryServiceDescription = "Discovers columns for CSV Files.";
        final String discoveryServiceProviderClassName = CSVDiscoveryServiceProvider.class.getName();

        return archiveHelper.addGovernanceService(OpenMetadataTypesMapper.OPEN_DISCOVERY_SERVICE_TYPE_NAME,
                                                  discoveryServiceProviderClassName,
                                                  null,
                                                  discoveryServiceName,
                                                  discoveryServiceDisplayName,
                                                  discoveryServiceDescription,
                                                  null,
                                                  null);
    }


    /**
     * Create an entity for the Apache Atlas Discovery governance service.
     *
     * @return unique identifier for the governance engine
     */
    private String getApacheAtlasDiscoveryService()
    {
        final String discoveryServiceName = "apache-atlas-discovery-service";
        final String discoveryServiceDisplayName = "Apache Atlas Discovery Service";
        final String discoveryServiceDescription = "Discovers the types and instances found in an Apache Atlas server.";
        final String discoveryServiceProviderClassName = DiscoverApacheAtlasProvider.class.getName();

        return archiveHelper.addGovernanceService(OpenMetadataTypesMapper.OPEN_DISCOVERY_SERVICE_TYPE_NAME,
                                                  discoveryServiceProviderClassName,
                                                  null,
                                                  discoveryServiceName,
                                                  discoveryServiceDisplayName,
                                                  discoveryServiceDescription,
                                                  null,
                                                  null);
    }


    private void addSmallCSVRequestType(String governanceEngineGUID,
                                        String governanceServiceGUID)
    {
        final String discoveryServiceRequestType = "small-csv";

        archiveHelper.addSupportedGovernanceService(governanceEngineGUID, discoveryServiceRequestType, null, null, governanceServiceGUID);
    }


    private void addApacheAtlasRequestType(String governanceEngineGUID,
                                           String governanceServiceGUID)
    {
        final String discoveryServiceRequestType = "apache-atlas";

        archiveHelper.addSupportedGovernanceService(governanceEngineGUID, discoveryServiceRequestType, null, null, governanceServiceGUID);
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
