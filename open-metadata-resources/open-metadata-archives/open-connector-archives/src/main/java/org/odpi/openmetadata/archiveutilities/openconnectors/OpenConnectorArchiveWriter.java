/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.openconnectors;

import org.odpi.openmetadata.adapters.connectors.apacheatlas.control.AtlasPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ApacheAtlasIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.ApacheAtlasRESTProvider;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.survey.SurveyApacheAtlasProvider;
import org.odpi.openmetadata.adapters.connectors.apachekafka.control.KafkaPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.apachekafka.integration.KafkaTopicIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.apachekafka.resource.ApacheKafkaAdminProvider;
import org.odpi.openmetadata.adapters.connectors.apachekafka.survey.SurveyApacheKafkaServerProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStoreProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFolderProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVFileStoreProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.datafolder.DataFolderProvider;
import org.odpi.openmetadata.adapters.connectors.governanceactions.provisioning.MoveCopyFileGovernanceActionProvider;
import org.odpi.openmetadata.adapters.connectors.governanceactions.remediation.OriginSeekerGovernanceActionProvider;
import org.odpi.openmetadata.adapters.connectors.governanceactions.remediation.QualifiedNamePeerDuplicateGovernanceActionProvider;
import org.odpi.openmetadata.adapters.connectors.governanceactions.remediation.ZonePublisherGovernanceActionProvider;
import org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship.*;
import org.odpi.openmetadata.adapters.connectors.governanceactions.verification.VerifyAssetGovernanceActionProvider;
import org.odpi.openmetadata.adapters.connectors.governanceactions.watchdog.GenericFolderWatchdogGovernanceActionProvider;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.DataFilesMonitorIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.DataFolderMonitorIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.controls.FilesPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.integration.csvlineageimporter.CSVLineageImporterProvider;
import org.odpi.openmetadata.adapters.connectors.integration.egeria.EgeriaCataloguerIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.JDBCIntegrationConnectorProvider;
import org.odpi.openmetadata.adapters.connectors.integration.kafka.KafkaMonitorIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.kafkaaudit.DistributeAuditEventsFromKafkaProvider;
import org.odpi.openmetadata.adapters.connectors.integration.openapis.OpenAPIMonitorIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.openlineage.*;
import org.odpi.openmetadata.adapters.connectors.postgres.catalog.PostgresServerIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.postgres.survey.PostgresDatabaseSurveyActionProvider;
import org.odpi.openmetadata.adapters.connectors.postgres.survey.PostgresServerSurveyActionProvider;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnectorProvider;
import org.odpi.openmetadata.adapters.connectors.secretsstore.envar.EnvVarSecretsStoreProvider;
import org.odpi.openmetadata.adapters.connectors.surveyaction.surveycsv.CSVSurveyServiceProvider;
import org.odpi.openmetadata.adapters.connectors.surveyaction.surveyfile.FileSurveyServiceProvider;
import org.odpi.openmetadata.adapters.connectors.surveyaction.surveyfolder.FolderSurveyServiceProvider;
import org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider;
import org.odpi.openmetadata.adminservices.configuration.registration.*;
import org.odpi.openmetadata.frameworks.governanceaction.controls.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.*;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceServiceProviderBase;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyActionServiceProvider;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnalysisStepType;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnnotationTypeType;
import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchive;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveWriter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.samples.archiveutilities.GovernanceArchiveHelper;

import java.util.*;

import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueCategory;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * OpenConnectorArchiveWriter creates an open metadata archive that includes the connector type
 * information for all open connectors supplied by the egeria project.
 */
public class OpenConnectorArchiveWriter extends OMRSArchiveWriter
{
    private static final String archiveFileName = "CoreContentPack.omarchive";

    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "09450b83-20ff-4a8b-a8fb-f9b527bbcba6";
    private static final String                  archiveName        = "CoreContentPack";
    private static final String                  archiveLicense     = "Apache-2.0";
    private static final String                  archiveDescription = "Connector Types and Categories for connectors from the Egeria project along with metadata valid values for the types of technology supported by these connectors.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  originatorName     = "Egeria Project";
    private static final Date                    creationDate       = new Date();

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
     * Specific values for initializing elements.  The version number is based off of the build time to ensure the
     * latest version of the archive elements is loaded.
     */
    private static final long   versionNumber = creationDate.getTime();
    private static final String versionName   = "1.0";

    private final OMRSArchiveBuilder      archiveBuilder;
    private final GovernanceArchiveHelper archiveHelper;

    private final Map<String, String> parentValidValueQNameToGUIDMap = new HashMap<>();
    private final Map<String, String> deployedImplementationTypeGUIDs = new HashMap<>();
    private final Map<String, String> openMetadataTypeGUIDs = new HashMap<>();


    /**
     * Default constructor initializes the archive.
     */
    public OpenConnectorArchiveWriter()
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
    public OpenMetadataArchive getOpenMetadataArchive()
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
        String resourceUseParentSetGUID = this.getParentSet(null,
                                                            OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                                            OpenMetadataProperty.RESOURCE_USE.name,
                                                            null);

        for (ResourceUse resourceUse : ResourceUse.values())
        {
            Map<String, String> additionalProperties = null;

            if (resourceUse.getResourceUseProperties() != null)
            {
                additionalProperties = new HashMap<>();

                for (ResourceUseProperties resourceUseProperties : resourceUse.getResourceUseProperties())
                {
                    additionalProperties.put(resourceUseProperties.getName(), resourceUseProperties.getDescription());
                }
            }

            this.archiveHelper.addValidValue(null,
                                             resourceUseParentSetGUID,
                                             resourceUseParentSetGUID,
                                             OpenMetadataType.VALID_VALUE_SET.typeName,
                                             OpenMetadataType.VALID_VALUE_SET.typeName,
                                             resourceUse.getQualifiedName(),
                                             resourceUse.getResourceUse(),
                                             resourceUse.getDescription(),
                                             resourceUse.getCategory(),
                                             OpenMetadataValidValues.VALID_METADATA_VALUES_USAGE,
                                             "string",
                                             OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE,
                                             resourceUse.getResourceUse(),
                                             false,
                                             false,
                                             additionalProperties);
        }

        /*
         * The resource use properties provide the mapNames for resource use properties.
         * There are no values for these map names.
         */
        for (ResourceUseProperties resourceUseProperties : ResourceUseProperties.values())
        {
            this.getParentSet(null,
                              OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                              OpenMetadataProperty.RESOURCE_USE_PROPERTIES.name,
                              resourceUseProperties.getName());
        }

        /*
         * Add the valid metadata values used in the resourceUse property of the ResourceList relationship.
         */
        String projectPhaseParentSetGUID = this.getParentSet(null,
                                                             OpenMetadataType.PROJECT.typeName,
                                                             OpenMetadataProperty.PROJECT_PHASE.name,
                                                             null);

        for (ProjectPhase projectStatus : ProjectPhase.values())
        {
            this.archiveHelper.addValidValue(null,
                                             projectPhaseParentSetGUID,
                                             projectPhaseParentSetGUID,
                                             OpenMetadataType.VALID_VALUE_SET.typeName,
                                             OpenMetadataType.VALID_VALUE_SET.typeName,
                                             projectStatus.getQualifiedName(),
                                             projectStatus.getName(),
                                             projectStatus.getDescription(),
                                             projectStatus.getCategory(),
                                             OpenMetadataValidValues.VALID_METADATA_VALUES_USAGE,
                                             "string",
                                             OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE,
                                             projectStatus.getName(),
                                             false,
                                             false,
                                             null);
        }


        /*
         * Add the valid metadata values used in the resourceUse property of the ResourceList relationship.
         */
        String projectHealthParentSetGUID = this.getParentSet(null,
                                                              OpenMetadataType.PROJECT.typeName,
                                                              OpenMetadataProperty.PROJECT_HEALTH.name,
                                                              null);

        for (ProjectHealth projectHealth : ProjectHealth.values())
        {
            Map<String, String> additionalProperties = new HashMap<>();

            additionalProperties.put("colour", projectHealth.getColour());

            this.archiveHelper.addValidValue(null,
                                             projectHealthParentSetGUID,
                                             projectHealthParentSetGUID,
                                             OpenMetadataType.VALID_VALUE_SET.typeName,
                                             OpenMetadataType.VALID_VALUE_SET.typeName,
                                             projectHealth.getQualifiedName(),
                                             projectHealth.getName(),
                                             projectHealth.getDescription(),
                                             projectHealth.getCategory(),
                                             OpenMetadataValidValues.VALID_METADATA_VALUES_USAGE,
                                             "string",
                                             OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE,
                                             projectHealth.getName(),
                                             false,
                                             false,
                                             additionalProperties);
        }


        /*
         * Add the valid metadata values used in the resourceUse property of the ResourceList relationship.
         */
        String projectStatusParentSetGUID = this.getParentSet(null,
                                                              OpenMetadataType.PROJECT.typeName,
                                                              OpenMetadataType.PROJECT_STATUS_PROPERTY_NAME,
                                                              null);

        for (ProjectStatus projectStatus : ProjectStatus.values())
        {
            this.archiveHelper.addValidValue(null,
                                             projectStatusParentSetGUID,
                                             projectStatusParentSetGUID,
                                             OpenMetadataType.VALID_VALUE_SET.typeName,
                                             OpenMetadataType.VALID_VALUE_SET.typeName,
                                             projectStatus.getQualifiedName(),
                                             projectStatus.getName(),
                                             projectStatus.getDescription(),
                                             projectStatus.getCategory(),
                                             OpenMetadataValidValues.VALID_METADATA_VALUES_USAGE,
                                             "string",
                                             OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE,
                                             projectStatus.getName(),
                                             false,
                                             false,
                                             null);
        }


        /*
         * Add the valid metadata values used in the resourceUse property of the ResourceList relationship.
         */
        String collectionTypeParentSetGUID = this.getParentSet(null,
                                                               OpenMetadataType.COLLECTION.typeName,
                                                               OpenMetadataProperty.COLLECTION_TYPE.name,
                                                               null);

        for (CollectionType collectionType : CollectionType.values())
        {
            this.archiveHelper.addValidValue(null,
                                             collectionTypeParentSetGUID,
                                             collectionTypeParentSetGUID,
                                             OpenMetadataType.VALID_VALUE_SET.typeName,
                                             OpenMetadataType.VALID_VALUE_SET.typeName,
                                             collectionType.getQualifiedName(),
                                             collectionType.getName(),
                                             collectionType.getDescription(),
                                             collectionType.getCategory(),
                                             OpenMetadataValidValues.VALID_METADATA_VALUES_USAGE,
                                             "string",
                                             OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE,
                                             collectionType.getName(),
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

        /*===========================================
         * Add the open metadata type enums
         */
        addOpenMetadataEnumValidNames(OpenMetadataType.OPERATING_PLATFORM.typeName,
                                      OpenMetadataProperty.BYTE_ORDERING.name,
                                      ByteOrdering.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(ByteOrdering.values())));

        addOpenMetadataEnumValidNames(OpenMetadataType.EXTERNAL_ID.typeName,
                                      OpenMetadataProperty.KEY_PATTERN.name,
                                      KeyPattern.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(KeyPattern.values())));

        addOpenMetadataEnumValidNames(OpenMetadataType.PRIMARY_KEY_CLASSIFICATION_TYPE_NAME,
                                      OpenMetadataProperty.KEY_PATTERN.name,
                                      KeyPattern.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(KeyPattern.values())));

        addOpenMetadataEnumValidNames(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                      OpenMetadataProperty.MEMBERSHIP_STATUS.name,
                                      CollectionMemberStatus.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(CollectionMemberStatus.values())));

        addOpenMetadataEnumValidNames(OpenMetadataType.COMMENT.typeName,
                                      OpenMetadataProperty.COMMENT_TYPE.name,
                                      CommentType.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(CommentType.values())));

        addOpenMetadataEnumValidIdentifiers(OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeName,
                                            OpenMetadataProperty.CONFIDENCE_LEVEL_IDENTIFIER.name,
                                            new ArrayList<>(Arrays.asList(ConfidenceLevel.values())));

        addOpenMetadataEnumValidIdentifiers(OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName,
                                            OpenMetadataProperty.CONFIDENTIALITY_LEVEL_IDENTIFIER.name,
                                            new ArrayList<>(Arrays.asList(ConfidentialityLevel.values())));

        addOpenMetadataEnumValidNames(OpenMetadataType.CONTACT_DETAILS.typeName,
                                      OpenMetadataProperty.CONTACT_METHOD_TYPE.name,
                                      ContactMethodType.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(ContactMethodType.values())));

        addOpenMetadataEnumValidIdentifiers(OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName,
                                            OpenMetadataProperty.CRITICALITY_LEVEL_IDENTIFIER.name,
                                            new ArrayList<>(Arrays.asList(CriticalityLevel.values())));

        addOpenMetadataEnumValidNames(OpenMetadataType.DATA_CLASS_ASSIGNMENT.typeName,
                                      OpenMetadataProperty.DATA_CLASS_ASSIGNMENT_STATUS.name,
                                      DataClassAssignmentStatus.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(DataClassAssignmentStatus.values())));

        addOpenMetadataEnumValidNames(OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                      OpenMetadataProperty.SORT_ORDER.name,
                                      DataItemSortOrder.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(DataItemSortOrder.values())));

        addOpenMetadataEnumValidNames(OpenMetadataType.DATA_FIELD.typeName,
                                      OpenMetadataProperty.DATA_FIELD_SORT_ORDER.name,
                                      DataItemSortOrder.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(DataItemSortOrder.values())));

        addOpenMetadataEnumValidNames(OpenMetadataType.ENGINE_ACTION.typeName,
                                      OpenMetadataProperty.ACTION_STATUS.name,
                                      EngineActionStatus.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(EngineActionStatus.values())));

        addOpenMetadataEnumValidNames(OpenMetadataType.TARGET_FOR_ACTION.typeName,
                                      OpenMetadataProperty.ACTION_STATUS.name,
                                      EngineActionStatus.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(EngineActionStatus.values())));

        addOpenMetadataEnumValidNames(OpenMetadataType.ACTIVITY_DESCRIPTION_CLASSIFICATION.typeName,
                                      OpenMetadataProperty.ACTIVITY_TYPE.name,
                                      GlossaryTermActivityType.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(GlossaryTermActivityType.values())));

        addOpenMetadataEnumValidNames(OpenMetadataType.SEMANTIC_ASSIGNMENT.typeName,
                                      OpenMetadataProperty.TERM_ASSIGNMENT_STATUS.name,
                                      GlossaryTermAssignmentStatus.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(GlossaryTermAssignmentStatus.values())));

        addOpenMetadataEnumValidNames(OpenMetadataType.TERM_CATEGORIZATION.typeName,
                                      OpenMetadataProperty.TERM_RELATIONSHIP_STATUS.name,
                                      GlossaryTermRelationshipStatus.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(GlossaryTermRelationshipStatus.values())));

        addOpenMetadataEnumValidNames(OpenMetadataType.RELATED_TERM_RELATIONSHIP_NAME,
                                      OpenMetadataProperty.TERM_RELATIONSHIP_STATUS.name,
                                      GlossaryTermRelationshipStatus.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(GlossaryTermRelationshipStatus.values())));

        addOpenMetadataEnumValidNames(OpenMetadataType.USED_IN_CONTEXT.typeName,
                                      OpenMetadataProperty.TERM_RELATIONSHIP_STATUS.name,
                                      GlossaryTermRelationshipStatus.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(GlossaryTermRelationshipStatus.values())));

        addOpenMetadataEnumValidIdentifiers(null,
                                            OpenMetadataProperty.STATUS_IDENTIFIER.name,
                                            new ArrayList<>(Arrays.asList(GovernanceClassificationStatus.values())));

        addOpenMetadataEnumValidIdentifiers(null,
                                            OpenMetadataProperty.SEVERITY_IDENTIFIER.name,
                                            new ArrayList<>(Arrays.asList(ImpactSeverity.values())));

        addOpenMetadataEnumValidNames(OpenMetadataType.INCIDENT_REPORT.typeName,
                                      OpenMetadataProperty.INCIDENT_STATUS.name,
                                      IncidentReportStatus.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(IncidentReportStatus.values())));


        addOpenMetadataEnumValidIdentifiers(OpenMetadataType.RETENTION_CLASSIFICATION.typeName,
                                            OpenMetadataProperty.RETENTION_BASIS_IDENTIFIER.name,
                                            new ArrayList<>(Arrays.asList(RetentionBasis.values())));

        addOpenMetadataEnumValidNames(OpenMetadataType.TO_DO.typeName,
                                      OpenMetadataProperty.TO_DO_STATUS.name,
                                      ToDoStatus.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(ToDoStatus.values())));

        addOpenMetadataEnumValidNames(OpenMetadataType.RATING_TYPE_NAME,
                                      OpenMetadataType.STARS_PROPERTY_NAME,
                                      StarRating.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(StarRating.values())));


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
                                                             OpenMetadataType.SOFTWARE_SERVER.typeName,
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
                                                            ResourceUse.HOSTED_SERVICE.getResourceUse(),
                                                            ResourceUse.HOSTED_SERVICE.getDescription());

            archiveHelper.addResourceListRelationshipByGUID(serverTypeGUID2,
                                                            guid,
                                                            ResourceUse.HOSTED_SERVICE.getResourceUse(),
                                                            ResourceUse.HOSTED_SERVICE.getDescription());
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
                                                            ResourceUse.HOSTED_SERVICE.getResourceUse(),
                                                            ResourceUse.HOSTED_SERVICE.getDescription());

            archiveHelper.addResourceListRelationshipByGUID(guid,
                                                            serviceGUIDs.get(viewServiceDescription.getViewServicePartnerService()),
                                                            ResourceUse.CALLED_SERVICE.getResourceUse(),
                                                            ResourceUse.CALLED_SERVICE.getDescription());
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
                                                            ResourceUse.HOSTED_SERVICE.getResourceUse(),
                                                            ResourceUse.HOSTED_SERVICE.getDescription());

            archiveHelper.addResourceListRelationshipByGUID(guid,
                                                            serviceGUIDs.get(engineServiceDescription.getEngineServicePartnerService()),
                                                            ResourceUse.CALLED_SERVICE.getResourceUse(),
                                                            ResourceUse.CALLED_SERVICE.getDescription());

            String governanceEngineGUID = deployedImplementationTypeGUIDs.get(engineServiceDescription.getHostedGovernanceEngineDeployedImplementationType());
            String governanceServiceGUID = deployedImplementationTypeGUIDs.get(engineServiceDescription.getHostedGovernanceServiceDeployedImplementationType());

            if (governanceEngineGUID != null)
            {
                archiveHelper.addResourceListRelationshipByGUID(guid,
                                                                governanceEngineGUID,
                                                                ResourceUse.HOSTED_GOVERNANCE_ENGINE.getResourceUse(),
                                                                ResourceUse.HOSTED_GOVERNANCE_ENGINE.getDescription());

                if (governanceServiceGUID != null)
                {
                    archiveHelper.addResourceListRelationshipByGUID(governanceEngineGUID,
                                                                    governanceServiceGUID,
                                                                    ResourceUse.HOSTED_CONNECTOR.getResourceUse(),
                                                                    ResourceUse.HOSTED_CONNECTOR.getDescription());
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
                                                            ResourceUse.HOSTED_SERVICE.getResourceUse(),
                                                            ResourceUse.HOSTED_SERVICE.getDescription());

            archiveHelper.addResourceListRelationshipByGUID(guid,
                                                            serviceGUIDs.get(integrationServiceDescription.getIntegrationServicePartnerOMAS().getAccessServiceFullName()),
                                                            ResourceUse.CALLED_SERVICE.getResourceUse(),
                                                            ResourceUse.CALLED_SERVICE.getDescription());

            String connectorTypeGUID = deployedImplementationTypeGUIDs.get(integrationServiceDescription.getConnectorDeployedImplementationType());

            if (connectorTypeGUID != null)
            {
                archiveHelper.addResourceListRelationshipByGUID(guid,
                                                                connectorTypeGUID,
                                                                ResourceUse.HOSTED_CONNECTOR.getResourceUse(),
                                                                ResourceUse.HOSTED_CONNECTOR.getDescription());
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
                             fileName.getFileType().getDeployedImplementationType());
        }

        /*
         * Add the list of recognized file extensions.
         */
        for (FileExtension fileExtension : FileExtension.values())
        {
            this.addFileExtension(fileExtension.getFileExtension(), fileExtension.getFileTypes());
        }

        /*
         * Add the list of property types used in specifications.
         */
        for (SpecificationPropertyType specificationPropertyType : SpecificationPropertyType.values())
        {
            this.addAttributeName(specificationPropertyType.getPropertyType(),
                                  specificationPropertyType.getDescription());
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
        archiveHelper.addConnectorType(null, new PostgresServerIntegrationProvider());
        archiveHelper.addConnectorType(null, new ApacheKafkaAdminProvider());
        archiveHelper.addConnectorType(null, new KafkaTopicIntegrationProvider());

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
                                                        "FilesCataloguer",
                                                        "filecatnpa",
                                                        null,
                                                        60,
                                                        filesIntegrationConnectorGUID);

        String deployedImplementationTypeGUID = archiveHelper.getGUID(DeployedImplementationType.FILE_FOLDER.getQualifiedName());
        archiveHelper.addResourceListRelationshipByGUID(deployedImplementationTypeGUID,
                                                        filesIntegrationConnectorGUID,
                                                        ResourceUse.CATALOG_RESOURCE.getResourceUse(),
                                                        ResourceUse.CATALOG_RESOURCE.getDescription());


        String databaseIntegrationConnectorGUID = archiveHelper.addIntegrationConnector(JDBCIntegrationConnectorProvider.class.getName(),
                                                                                        null,
                                                                                        OpenMetadataValidValues.DEFAULT_INTEGRATION_GROUP_QUALIFIED_NAME + ":JDBCIntegrationConnector",
                                                                                        "JDBCIntegrationConnector",
                                                                                        "Catalogs JDBC database schemas, tables and columns.",
                                                                                        null,
                                                                                        null);

        deployedImplementationTypeGUID = archiveHelper.getGUID(DeployedImplementationType.JDBC_RELATIONAL_DATABASE.getQualifiedName());
        archiveHelper.addResourceListRelationshipByGUID(deployedImplementationTypeGUID,
                                                        databaseIntegrationConnectorGUID,
                                                        ResourceUse.CATALOG_RESOURCE.getResourceUse(),
                                                        ResourceUse.CATALOG_RESOURCE.getDescription());
        deployedImplementationTypeGUID = archiveHelper.getGUID(DeployedImplementationType.POSTGRESQL_DATABASE.getQualifiedName());
        archiveHelper.addResourceListRelationshipByGUID(deployedImplementationTypeGUID,
                                                        databaseIntegrationConnectorGUID,
                                                        ResourceUse.CATALOG_RESOURCE.getResourceUse(),
                                                        ResourceUse.CATALOG_RESOURCE.getDescription());

        archiveHelper.addRegisteredIntegrationConnector(integrationGroupGUID,
                                                        "JDBCDatabaseCataloguer",
                                                        "dbcatnpa",
                                                        null,
                                                        60,
                                                        databaseIntegrationConnectorGUID);

        String postgresIntegrationConnectorGUID = archiveHelper.addIntegrationConnector(PostgresServerIntegrationProvider.class.getName(),
                                                                                        null,
                                                                                        OpenMetadataValidValues.DEFAULT_INTEGRATION_GROUP_QUALIFIED_NAME + ":PostgreSQLServerIntegrationConnector",
                                                                                        "PostgreSQLServerIntegrationConnector",
                                                                                        "Catalogs PostgreSQL Databases in a PostgreSQL Server.",
                                                                                        null,
                                                                                        null);
        deployedImplementationTypeGUID = archiveHelper.getGUID(DeployedImplementationType.POSTGRESQL_SERVER.getQualifiedName());
        archiveHelper.addResourceListRelationshipByGUID(deployedImplementationTypeGUID,
                                                        postgresIntegrationConnectorGUID,
                                                        ResourceUse.CATALOG_RESOURCE.getResourceUse(),
                                                        ResourceUse.CATALOG_RESOURCE.getDescription());
        archiveHelper.addRegisteredIntegrationConnector(integrationGroupGUID,
                                                        "PostgreSQLServerCataloguer",
                                                        "postgrescatnpa",
                                                        null,
                                                        60,
                                                        postgresIntegrationConnectorGUID);

        String kafkaIntegrationConnectorGUID = archiveHelper.addIntegrationConnector(KafkaTopicIntegrationProvider.class.getName(),
                                                                                     null,
                                                                                     OpenMetadataValidValues.DEFAULT_INTEGRATION_GROUP_QUALIFIED_NAME + ":KafkaTopicIntegrationConnector",
                                                                                     "KafkaTopicIntegrationConnector",
                                                                                     "Catalogs Apache Kafka Topics.",
                                                                                     null,
                                                                                     null);
        deployedImplementationTypeGUID = archiveHelper.getGUID(DeployedImplementationType.APACHE_KAFKA_SERVER.getQualifiedName());
        archiveHelper.addResourceListRelationshipByGUID(deployedImplementationTypeGUID,
                                                        kafkaIntegrationConnectorGUID,
                                                        ResourceUse.CATALOG_RESOURCE.getResourceUse(),
                                                        ResourceUse.CATALOG_RESOURCE.getDescription());
        archiveHelper.addRegisteredIntegrationConnector(integrationGroupGUID,
                                                        "ApacheKafkaCataloguer",
                                                        "kafkacatnpa",
                                                        null,
                                                        60,
                                                        kafkaIntegrationConnectorGUID);

        String apiIntegrationConnectorGUID = archiveHelper.addIntegrationConnector(OpenAPIMonitorIntegrationProvider.class.getName(),
                                                                                   null,
                                                                                   OpenMetadataValidValues.DEFAULT_INTEGRATION_GROUP_QUALIFIED_NAME + ":OpenAPIIntegrationConnector",
                                                                                     "OpenAPIIntegrationConnector",
                                                                                     "Catalogs REST APIs through the Open API Specification.",
                                                                                     null,
                                                                                     null);

        archiveHelper.addRegisteredIntegrationConnector(integrationGroupGUID,
                                                        "OpenAPICataloguer",
                                                        "apicatnpa",
                                                        null,
                                                        60,
                                                        apiIntegrationConnectorGUID);

        /*
         * Register the governance services that are going to be in the default governance engines.
         */
        GovernanceActionDescription fileProvisionerDescription        = this.getFileProvisioningGovernanceActionService();
        GovernanceActionDescription watchDogServiceDescription        = this.getWatchdogGovernanceActionService();
        GovernanceActionDescription originSeekerDescription           = this.getOriginSeekerGovernanceActionService();
        GovernanceActionDescription qualifiedNameDeDupDescription     = this.getQualifiedNameDeDupGovernanceActionService();
        GovernanceActionDescription zonePublisherDescription          = this.getZonePublisherGovernanceActionService();
        GovernanceActionDescription evaluateAnnotationsDescription    = this.getEvaluateAnnotationsGovernanceActionService();
        GovernanceActionDescription writeAuditLogDescription          = this.getWriteAuditLogGovernanceActionService();
        GovernanceActionDescription dayOfWeekDescription              = this.getDayOfWeekGovernanceActionService();
        GovernanceActionDescription verifyAssetDescription            = this.getVerifyAssetGovernanceActionService();
        GovernanceActionDescription csvSurveyDescription              = this.getCSVFileSurveyService();
        GovernanceActionDescription fileSurveyDescription             = this.getDataFileSurveyService();
        GovernanceActionDescription folderSurveyDescription           = this.getFolderSurveyService();
        GovernanceActionDescription atlasSurveyDescription            = this.getAtlasSurveyService();
        GovernanceActionDescription postgresServerSurveyDescription   = this.getPostgresServerSurveyService();
        GovernanceActionDescription postgresDatabaseSurveyDescription = this.getPostgresDatabaseSurveyService();
        GovernanceActionDescription kafkaServerSurveyDescription      = this.getKafkaServerSurveyService();

        /*
         * Define the FileProvisioning engine.
         */
        String fileProvisioningEngineName = "FileProvisioning";
        String fileProvisioningEngineGUID = this.getFileProvisioningEngine(fileProvisioningEngineName);

        this.addCopyFileRequestType(fileProvisioningEngineGUID, fileProvisioningEngineName, fileProvisionerDescription);
        this.addMoveFileRequestType(fileProvisioningEngineGUID, fileProvisioningEngineName, fileProvisionerDescription);
        this.addDeleteFileRequestType(fileProvisioningEngineGUID, fileProvisioningEngineName, fileProvisionerDescription);

        /*
         * Define the AssetOnboarding engine
         */
        String assetOnboardingEngineName = "AssetOnboarding";
        String assetOnboardingEngineGUID = this.getAssetOnboardingEngine(assetOnboardingEngineName);

        this.addWatchNestedInFolderRequestType(assetOnboardingEngineGUID, assetOnboardingEngineName, watchDogServiceDescription);
        this.addSeekOriginRequestType(assetOnboardingEngineGUID, assetOnboardingEngineName, originSeekerDescription);
        this.addSetZoneMembershipRequestType(assetOnboardingEngineGUID, assetOnboardingEngineName, zonePublisherDescription);
        this.addVerifyAssetRequestType(assetOnboardingEngineGUID, assetOnboardingEngineName, verifyAssetDescription);

        /*
         * Define the Stewardship engine
         */
        String stewardshipEngineName = "Stewardship";
        String stewardshipEngineGUID = this.getStewardshipEngine(stewardshipEngineName);

        this.addEvaluateAnnotationsRequestType(stewardshipEngineGUID, stewardshipEngineName, evaluateAnnotationsDescription);
        this.addWriteAuditLogRequestType(stewardshipEngineGUID, stewardshipEngineName, writeAuditLogDescription);
        this.addDayOfWeekRequestType(stewardshipEngineGUID, stewardshipEngineName, dayOfWeekDescription);
        this.addQualifiedNameDeDupRequestType(stewardshipEngineGUID, stewardshipEngineName, qualifiedNameDeDupDescription);

        this.createDailyGovernanceActionProcess(stewardshipEngineGUID);

        /*
         * Define the AssetSurvey engine
         */
        String assetSurveyEngineName = "AssetSurvey";
        String assetSurveyEngineGUID = this.getAssetSurveyEngine(assetSurveyEngineName);

        this.addCSVFileRequestType(assetSurveyEngineGUID, assetSurveyEngineName, csvSurveyDescription);
        this.addDataFileRequestType(assetSurveyEngineGUID, assetSurveyEngineName, fileSurveyDescription);
        this.addFolderRequestType(assetSurveyEngineGUID, assetSurveyEngineName, folderSurveyDescription);
        this.addAtlasRequestType(assetSurveyEngineGUID, assetSurveyEngineName, atlasSurveyDescription);
        this.addPostgresServerRequestType(assetSurveyEngineGUID, assetSurveyEngineName, postgresServerSurveyDescription);
        this.addPostgresDatabaseRequestType(assetSurveyEngineGUID, assetSurveyEngineName, postgresDatabaseSurveyDescription);
        this.addKafkaServerRequestType(assetSurveyEngineGUID, assetSurveyEngineName, kafkaServerSurveyDescription);

        /*
         * Add catalog templates
         */
        this.addFileTemplates();
        this.addPostgresServerCatalogTemplate();
        this.addPostgresDatabaseCatalogTemplate();
        this.addPostgresDatabaseSchemaCatalogTemplate();
        this.addAtlasServerCatalogTemplate();
        this.addKafkaServerCatalogTemplate();
        this.addKafkaTopicCatalogTemplate();

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
     * Set up a valid value list for an enum based on its names.
     *
     * @param enumConsumingTypeName entity type name
     * @param enumConsumingProperty attribute name
     * @param enumTypeName type name for enum
     * @param openMetadataEnums list of valid values
     */
    private void addOpenMetadataEnumValidNames(String                 enumConsumingTypeName,
                                               String                 enumConsumingProperty,
                                               String                 enumTypeName,
                                               List<OpenMetadataEnum> openMetadataEnums)
    {
        String parentSetGUID = this.getParentSet(null, enumConsumingTypeName, enumConsumingProperty, null);

        for (OpenMetadataEnum enumValue : openMetadataEnums)
        {
            String enumPreferredValue = enumValue.getName().toUpperCase();
            this.archiveHelper.addValidValue(enumValue.getDescriptionGUID(),
                                             parentSetGUID,
                                             parentSetGUID,
                                             OpenMetadataType.VALID_VALUE_SET.typeName,
                                             OpenMetadataType.VALID_VALUE_SET.typeName,
                                             constructValidValueQualifiedName(enumConsumingTypeName,
                                                                              enumConsumingProperty,
                                                                              null,
                                                                              enumPreferredValue),
                                             enumValue.getName(),
                                             enumValue.getDescription(),
                                             constructValidValueCategory(enumConsumingTypeName,
                                                                         enumConsumingProperty,
                                                                         null),
                                             OpenMetadataValidValues.VALID_METADATA_VALUES_USAGE,
                                             enumTypeName,
                                             OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE,
                                             enumPreferredValue,
                                             false,
                                             false,
                                             null);
        }
    }


    /**
     * Set up a valid value list for an enum based on its ordinals.
     *
     * @param enumConsumingTypeName entity type name
     * @param enumConsumingProperty attribute name
     * @param openMetadataEnums list of valid values
     */
    private void addOpenMetadataEnumValidIdentifiers(String                 enumConsumingTypeName,
                                                     String                 enumConsumingProperty,
                                                     List<OpenMetadataEnum> openMetadataEnums)
    {
        String parentSetGUID = this.getParentSet(null, enumConsumingTypeName, enumConsumingProperty, null);

        for (OpenMetadataEnum enumValue : openMetadataEnums)
        {
            String enumPreferredValue = Integer.toString(enumValue.getOrdinal());
            this.archiveHelper.addValidValue(enumValue.getDescriptionGUID(),
                                             parentSetGUID,
                                             parentSetGUID,
                                             OpenMetadataType.VALID_VALUE_SET.typeName,
                                             OpenMetadataType.VALID_VALUE_SET.typeName,
                                             constructValidValueQualifiedName(enumConsumingTypeName,
                                                                              enumConsumingProperty,
                                                                              null,
                                                                              enumPreferredValue),
                                             enumValue.getName(),
                                             enumValue.getDescription(),
                                             constructValidValueCategory(enumConsumingTypeName,
                                                                         enumConsumingProperty,
                                                                         null),
                                             OpenMetadataValidValues.VALID_METADATA_VALUES_USAGE,
                                             "int",
                                             OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE,
                                             enumPreferredValue,
                                             false,
                                             false,
                                             null);
        }
    }


    /**
     * Add templates to the open metadata types for files.
     */
    private void addFileTemplates()
    {
        String basicFileConnectorTypeGUID = new BasicFileStoreProvider().getConnectorType().getGUID();

        createFolderCatalogTemplate(DeployedImplementationType.FILE_FOLDER, new BasicFolderProvider().getConnectorType().getGUID());
        createFolderCatalogTemplate(DeployedImplementationType.DATA_FOLDER, new DataFolderProvider().getConnectorType().getGUID());

        createDataFileCatalogTemplate(DeployedImplementationType.FILE, basicFileConnectorTypeGUID, null);
        createDataFileCatalogTemplate(DeployedImplementationType.DATA_FILE, basicFileConnectorTypeGUID, null);
        createDataFileCatalogTemplate(DeployedImplementationType.CSV_FILE, new CSVFileStoreProvider().getConnectorType().getGUID(), null);
        createDataFileCatalogTemplate(DeployedImplementationType.AVRO_FILE, basicFileConnectorTypeGUID,  null);
        createDataFileCatalogTemplate(DeployedImplementationType.JSON_FILE, basicFileConnectorTypeGUID,  null);
        createDataFileCatalogTemplate(DeployedImplementationType.PARQUET_FILE, basicFileConnectorTypeGUID, null);
        createDataFileCatalogTemplate(DeployedImplementationType.SPREADSHEET_FILE, basicFileConnectorTypeGUID,  null);
        createDataFileCatalogTemplate(DeployedImplementationType.XML_FILE, basicFileConnectorTypeGUID, null);
        createDataFileCatalogTemplate(DeployedImplementationType.DOCUMENT, basicFileConnectorTypeGUID, null);
        createDataFileCatalogTemplate(DeployedImplementationType.AUDIO_DATA_FILE, basicFileConnectorTypeGUID, null);
        createDataFileCatalogTemplate(DeployedImplementationType.VIDEO_DATA_FILE, basicFileConnectorTypeGUID,  null);
        createDataFileCatalogTemplate(DeployedImplementationType.THREE_D_IMAGE_DATA_FILE, basicFileConnectorTypeGUID, null);
        createDataFileCatalogTemplate(DeployedImplementationType.RASTER_DATA_FILE, basicFileConnectorTypeGUID, null);
        createDataFileCatalogTemplate(DeployedImplementationType.VECTOR_DATA_FILE, basicFileConnectorTypeGUID, null);
        createDataFileCatalogTemplate(DeployedImplementationType.ARCHIVE_FILE, basicFileConnectorTypeGUID, null);
        createDataFileCatalogTemplate(DeployedImplementationType.KEYSTORE_FILE, basicFileConnectorTypeGUID, null);

        createSoftwareFileCatalogTemplate(DeployedImplementationType.PROGRAM_FILE, basicFileConnectorTypeGUID);
        createSoftwareFileCatalogTemplate(DeployedImplementationType.SOURCE_CODE_FILE, basicFileConnectorTypeGUID);
        createSoftwareFileCatalogTemplate(DeployedImplementationType.BUILD_FILE, basicFileConnectorTypeGUID);
        createSoftwareFileCatalogTemplate(DeployedImplementationType.EXECUTABLE_FILE, basicFileConnectorTypeGUID);
        createSoftwareFileCatalogTemplate(DeployedImplementationType.SCRIPT_FILE, basicFileConnectorTypeGUID);
        createSoftwareFileCatalogTemplate(DeployedImplementationType.PROPERTIES_FILE, basicFileConnectorTypeGUID);
        createSoftwareFileCatalogTemplate(DeployedImplementationType.YAML_FILE, basicFileConnectorTypeGUID);
    }


    /**
     * Create a template for a data file and link it to the associated open metadata type.
     * The template consists of a DataFile asset plus an optional connection, linked
     * to the supplied connector type and an endpoint,
     *
     * @param connectorTypeGUID connector type to link to the connection
     * @param deployedImplementationType deployed implementation type to link the template to
     * @param configurationProperties configuration properties
     */
    private   void createDataFileCatalogTemplate(DeployedImplementationType     deployedImplementationType,
                                                 String                         connectorTypeGUID,
                                                 Map<String, Object>            configurationProperties)
    {
        final String methodName    = "createDataFileCatalogTemplate";

        String               qualifiedName = deployedImplementationType.getDeployedImplementationType() + ":" + FilesPlaceholderProperty.PATH_NAME.getPlaceholder();
        String               versionIdentifier = "V1.0";
        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, FilesPlaceholderProperty.DEPLOYED_IMPLEMENTATION_TYPE.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.PATH_NAME.name, FilesPlaceholderProperty.PATH_NAME.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FILE_TYPE.name, FilesPlaceholderProperty.FILE_TYPE.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FILE_EXTENSION.name, FilesPlaceholderProperty.FILE_EXTENSION.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FILE_NAME.name, FilesPlaceholderProperty.FILE_NAME.getPlaceholder());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    "Create an asset of type " + deployedImplementationType.getAssociatedTypeName() + " with an associated Connection.",
                                                                    "V1.0",
                                                                    null, methodName));

        classifications.add(archiveHelper.getDataStoreEncodingClassification(FilesPlaceholderProperty.FILE_ENCODING.getPlaceholder(), null, null, null));

        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  FilesPlaceholderProperty.FILE_NAME.getPlaceholder(),
                                                  versionIdentifier,
                                                  null,
                                                  null,
                                                  extendedProperties,
                                                  classifications);

        String endpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                        deployedImplementationType.getAssociatedTypeName(),
                                                        qualifiedName + ":Endpoint",
                                                        FilesPlaceholderProperty.PATH_NAME.getPlaceholder() + " endpoint",
                                                        null,
                                                        FilesPlaceholderProperty.PATH_NAME.getPlaceholder(),
                                                        null,
                                                        null);

        String connectionGUID = archiveHelper.addConnection(qualifiedName + ":Connection",
                                                            FilesPlaceholderProperty.PATH_NAME.getPlaceholder() + " connection",
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            configurationProperties,
                                                            null,
                                                            connectorTypeGUID,
                                                            endpointGUID,
                                                            assetGUID,
                                                            deployedImplementationType.getAssociatedTypeName());

        archiveHelper.addConnectionForAsset(assetGUID, null, connectionGUID);

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               FilesPlaceholderProperty.getDataFilesPlaceholderPropertyTypes());
    }


    /**
     * Create a template for a file directory and link it to the associated open metadata type.
     * The template consists of a DataFile asset plus an optional connection, linked
     * to the supplied connector type and an endpoint,
     *
     * @param deployedImplementationType info for the template
     * @param connectorTypeGUID connector type to link to the connection
     */
    private   void createFolderCatalogTemplate(DeployedImplementationType     deployedImplementationType,
                                               String                         connectorTypeGUID)
    {
        final String methodName    = "createFolderCatalogTemplate";

        String               qualifiedName = deployedImplementationType.getDeployedImplementationType() + ":" + FilesPlaceholderProperty.PATH_NAME.getPlaceholder();
        String               versionIdentifier = "V1.0";
        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, FilesPlaceholderProperty.DEPLOYED_IMPLEMENTATION_TYPE.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.PATH_NAME.name, FilesPlaceholderProperty.PATH_NAME.getPlaceholder());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    "Create an asset of type " + deployedImplementationType.getAssociatedTypeName() + " with an associated Connection.",
                                                                    "V1.0",
                                                                    null, methodName));

        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  FilesPlaceholderProperty.FOLDER_NAME.getPlaceholder(),
                                                  versionIdentifier,
                                                  null,
                                                  null,
                                                  extendedProperties,
                                                  classifications);

        if (connectorTypeGUID != null)
        {
            String endpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                            deployedImplementationType.getAssociatedTypeName(),
                                                            qualifiedName + ":Endpoint",
                                                            FilesPlaceholderProperty.PATH_NAME + " endpoint",
                                                            null,
                                                            FilesPlaceholderProperty.PATH_NAME.getPlaceholder(),
                                                            null,
                                                            null);

            String connectionGUID = archiveHelper.addConnection(qualifiedName + ":Connection",
                                                                FilesPlaceholderProperty.PATH_NAME.getPlaceholder() + " connection",
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                connectorTypeGUID,
                                                                endpointGUID,
                                                                assetGUID,
                                                                deployedImplementationType.getAssociatedTypeName());

            archiveHelper.addConnectionForAsset(assetGUID, null, connectionGUID);
        }

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               FilesPlaceholderProperty.getFolderPlaceholderPropertyTypes());
    }


    /**
     * Create a template for a file directory and link it to the associated open metadata type.
     * The template consists of a DataFile asset plus an optional connection, linked
     * to the supplied connector type and an endpoint,
     *
     * @param deployedImplementationType values for the template
     * @param connectorTypeGUID connector type to link to the connection
     */
    private   void createDataSetCatalogTemplate(DeployedImplementationType     deployedImplementationType,
                                                String                         connectorTypeGUID)
    {
        final String methodName    = "createDataSetCatalogTemplate";

        String               qualifiedName = deployedImplementationType.getDeployedImplementationType() + ":" + FilesPlaceholderProperty.DATA_SET_NAME.getPlaceholder();
        String               versionIdentifier = "V1.0";
        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, FilesPlaceholderProperty.DEPLOYED_IMPLEMENTATION_TYPE.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FORMULA.name, FilesPlaceholderProperty.FORMULA.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FORMULA_TYPE.name, FilesPlaceholderProperty.FORMULA_TYPE.getPlaceholder());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    "Create an asset of type " + deployedImplementationType.getAssociatedTypeName() + " with an associated Connection.",
                                                                    "V1.0",
                                                                    null, methodName));

        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  FilesPlaceholderProperty.DATA_SET_NAME.getPlaceholder(),
                                                  versionIdentifier,
                                                  null,
                                                  null,
                                                  extendedProperties,
                                                  classifications);

        if (connectorTypeGUID != null)
        {
            String connectionGUID = archiveHelper.addConnection(qualifiedName + ":Connection",
                                                                FilesPlaceholderProperty.DATA_SET_NAME.getPlaceholder() + " connection",
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                connectorTypeGUID,
                                                                null,
                                                                assetGUID,
                                                                deployedImplementationType.getAssociatedTypeName());

            archiveHelper.addConnectionForAsset(assetGUID, null, connectionGUID);
        }

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               FilesPlaceholderProperty.getDataSetPlaceholderPropertyTypes());
    }


    /**
     * Create a template for a software file and link it to the associated open metadata type.
     * The template consists of a DataFile asset plus an optional connection, linked
     * to the supplied connector type and an endpoint,
     *
     * @param deployedImplementationType description for the template
     * @param connectorTypeGUID connector type to link to the connection
     */
    private   void createSoftwareFileCatalogTemplate(DeployedImplementationType deployedImplementationType,
                                                     String                     connectorTypeGUID)
    {
        final String methodName    = "createSoftwareFileCatalogTemplate";

        String               qualifiedName = deployedImplementationType.getDeployedImplementationType() + ":" + FilesPlaceholderProperty.PATH_NAME.getPlaceholder();
        String               versionIdentifier = "V1.0";
        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, FilesPlaceholderProperty.DEPLOYED_IMPLEMENTATION_TYPE.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.PATH_NAME.name, FilesPlaceholderProperty.PATH_NAME.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FILE_TYPE.name, FilesPlaceholderProperty.FILE_TYPE.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FILE_EXTENSION.name, FilesPlaceholderProperty.FILE_EXTENSION.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FILE_NAME.name, FilesPlaceholderProperty.FILE_NAME.getPlaceholder());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    "Create an asset of type " + deployedImplementationType.getAssociatedTypeName() + " with an associated Connection.",
                                                                    "V1.0",
                                                                    null, methodName));

        classifications.add(archiveHelper.getDataStoreEncodingClassification(FilesPlaceholderProperty.FILE_ENCODING.getPlaceholder(),
                                                                             FilesPlaceholderProperty.PROGRAMMING_LANGUAGE.getPlaceholder(),
                                                                             null,
                                                                             null));

        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  FilesPlaceholderProperty.FILE_NAME.getPlaceholder(),
                                                  versionIdentifier,
                                                  FilesPlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                                  null,
                                                  extendedProperties,
                                                  classifications);

        if (connectorTypeGUID != null)
        {
            String endpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                            deployedImplementationType.getAssociatedTypeName(),
                                                            qualifiedName + ":Endpoint",
                                                            FilesPlaceholderProperty.PATH_NAME.getPlaceholder() + " endpoint",
                                                            null,
                                                            FilesPlaceholderProperty.PATH_NAME.getPlaceholder(),
                                                            null,
                                                            null);

            String connectionGUID = archiveHelper.addConnection(qualifiedName + ":Connection",
                                                                FilesPlaceholderProperty.PATH_NAME.getPlaceholder() + " connection",
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                connectorTypeGUID,
                                                                endpointGUID,
                                                                assetGUID,
                                                                deployedImplementationType.getAssociatedTypeName());

            archiveHelper.addConnectionForAsset(assetGUID, null, connectionGUID);
        }

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               FilesPlaceholderProperty.getSoftwareFilesPlaceholderPropertyTypes());
    }


    /**
     * Create a template for a software server and link it to the associated deployed implementation type.
     * The template consists of a SoftwareServer asset linked to a software capability, plus a connection, linked
     * to the supplied connector type and an endpoint,
     *
     * @param deployedImplementationType deployed implementation type for the technology
     * @param softwareCapabilityType type of the associated capability
     * @param softwareCapabilityName name for the associated capability
     * @param serverName name for the server
     * @param userId userId for the connection
     * @param password password for the connection
     * @param connectorTypeGUID connector type to link to the connection
     * @param networkAddress network address for the endpoint
     * @param configurationProperties  additional properties for the connection
     * @param replacementAttributeTypes attributes that should have a replacement value to successfully use the template
     * @param placeholderPropertyTypes placeholder variables used in the supplied parameters
     */
    private   void createSoftwareServerCatalogTemplate(DeployedImplementationType     deployedImplementationType,
                                                       DeployedImplementationType     softwareCapabilityType,
                                                       String                         softwareCapabilityName,
                                                       String                         serverName,
                                                       String                         userId,
                                                       String                         password,
                                                       String                         connectorTypeGUID,
                                                       String                         networkAddress,
                                                       Map<String, Object>            configurationProperties,
                                                       List<ReplacementAttributeType> replacementAttributeTypes,
                                                       List<PlaceholderPropertyType>  placeholderPropertyTypes)
    {
        final String methodName = "createSoftwareServerCatalogTemplate";

        String               qualifiedName = deployedImplementationType.getDeployedImplementationType() + ":" + serverName;
        String               versionIdentifier = "V1.0";
        String               description = deployedImplementationType.getDescription();
        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                               deployedImplementationType.getDeployedImplementationType());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    "Create a SoftwareServer with an associated SoftwareCapability and Connection.",
                                                                    "V1.0",
                                                                    null, methodName));

        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  serverName,
                                                  versionIdentifier,
                                                  description,
                                                  null,
                                                  extendedProperties,
                                                  classifications);

        archiveHelper.addSoftwareCapability(softwareCapabilityType.getAssociatedTypeName(),
                                            qualifiedName + ":" + softwareCapabilityName,
                                            softwareCapabilityName,
                                            null,
                                            softwareCapabilityType.getDeployedImplementationType(),
                                            null,
                                            null,
                                            null,
                                            null,
                                            null,
                                            assetGUID,
                                            deployedImplementationType.getAssociatedTypeName());

        archiveHelper.addSupportedSoftwareCapabilityRelationship(qualifiedName + ":" + softwareCapabilityName,
                                                                 qualifiedName,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 1);

        String endpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                        deployedImplementationType.getAssociatedTypeName(),
                                                        qualifiedName + ":Endpoint",
                                                        serverName + " endpoint",
                                                        null,
                                                        networkAddress,
                                                        null,
                                                        null);

        String connectionGUID = archiveHelper.addConnection(qualifiedName + ":Connection",
                                                            serverName + " connection",
                                                            null,
                                                            userId,
                                                            password,
                                                            null,
                                                            null,
                                                            configurationProperties,
                                                            null,
                                                            connectorTypeGUID,
                                                            endpointGUID,
                                                            assetGUID,
                                                            deployedImplementationType.getAssociatedTypeName());

        archiveHelper.addConnectionForAsset(assetGUID, null, connectionGUID);

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addReplacementAttributes(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               replacementAttributeTypes);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               placeholderPropertyTypes);
    }


    /**
     * Create a catalog template for a PostgreSQL database server
     */
    private void addPostgresServerCatalogTemplate()
    {
        JDBCResourceConnectorProvider provider = new JDBCResourceConnectorProvider();

        List<PlaceholderPropertyType>  placeholderPropertyTypes = PostgresPlaceholderProperty.getPostgresServerPlaceholderPropertyTypes();

        this.createSoftwareServerCatalogTemplate(DeployedImplementationType.POSTGRESQL_SERVER,
                                                 DeployedImplementationType.POSTGRESQL_DATABASE_MANAGER,
                                                 "DBMS",
                                                 PostgresPlaceholderProperty.SERVER_NAME.getPlaceholder(),
                                                 PostgresPlaceholderProperty.DATABASE_USER_ID.getPlaceholder(),
                                                 PostgresPlaceholderProperty.DATABASE_PASSWORD.getPlaceholder(),
                                                 provider.getConnectorType().getGUID(),
                                                 "jdbc:postgresql://" +
                                                         PostgresPlaceholderProperty.HOST_IDENTIFIER.getPlaceholder() + ":" +
                                                         PostgresPlaceholderProperty.PORT_NUMBER.getPlaceholder() + "/postgres",
                                                 null,
                                                 null,
                                                 placeholderPropertyTypes);
    }


    /**
     * Create a catalog template for Apache Atlas
     */
    private void addAtlasServerCatalogTemplate()
    {
        ApacheAtlasRESTProvider provider = new ApacheAtlasRESTProvider();

        List<PlaceholderPropertyType>  placeholderPropertyTypes = AtlasPlaceholderProperty.getPlaceholderPropertyTypes();

        this.createSoftwareServerCatalogTemplate(DeployedImplementationType.APACHE_ATLAS_SERVER,
                                                 DeployedImplementationType.ASSET_CATALOG,
                                                 "MetadataCatalog",
                                                 AtlasPlaceholderProperty.SERVER_NAME.getPlaceholder(),
                                                 AtlasPlaceholderProperty.CONNECTION_USER_ID.getPlaceholder(),
                                                 AtlasPlaceholderProperty.CONNECTION_PASSWORD.getPlaceholder(),
                                                 provider.getConnectorType().getGUID(),
                                                 "http://" +
                                                         AtlasPlaceholderProperty.HOST_IDENTIFIER.getPlaceholder() + ":" +
                                                         AtlasPlaceholderProperty.PORT_NUMBER.getPlaceholder(),
                                                 null,
                                                 null,
                                                 placeholderPropertyTypes);
    }


    /**
     * Create a catalog template for Apache Kafka Server
     */
    private void addKafkaServerCatalogTemplate()
    {
        ApacheKafkaAdminProvider provider = new ApacheKafkaAdminProvider();

        List<PlaceholderPropertyType>  placeholderPropertyTypes = KafkaPlaceholderProperty.getKafkaServerPlaceholderPropertyTypes();

        this.createSoftwareServerCatalogTemplate(DeployedImplementationType.APACHE_KAFKA_SERVER,
                                                 DeployedImplementationType.APACHE_KAFKA_EVENT_BROKER,
                                                 "EventBroker",
                                                 KafkaPlaceholderProperty.SERVER_NAME.getPlaceholder(),
                                                 null,
                                                 null,
                                                 provider.getConnectorType().getGUID(),
                                                 "http://" +
                                                         KafkaPlaceholderProperty.HOST_IDENTIFIER.getPlaceholder() + ":" +
                                                         KafkaPlaceholderProperty.PORT_NUMBER.getPlaceholder(),
                                                 null,
                                                 null,
                                                 placeholderPropertyTypes);
    }


    /**
     * Create a template got a type of asset and link it to the associated deployed implementation type.
     * The template consists of a asset linked to a connection, that is in turn linked
     * to the supplied connector type and an endpoint,
     *
     * @param deployedImplementationType deployed implementation type for the technology
     * @param assetName name for the asset
     * @param serverName optional server name
     * @param userId userId for the connection
     * @param password password for the connection
     * @param connectorTypeGUID connector type to link to the connection
     * @param networkAddress network address for the endpoint
     * @param configurationProperties  additional properties for the connection
     * @param replacementAttributeTypes attributes that should have a replacement value to successfully use the template
     * @param placeholderPropertyTypes placeholder variables used in the supplied parameters
     */
    private   void createServerAssetCatalogTemplate(DeployedImplementationType     deployedImplementationType,
                                                    String                         assetName,
                                                    String                         serverName,
                                                    String                         userId,
                                                    String                         password,
                                                    String                         connectorTypeGUID,
                                                    String                         networkAddress,
                                                    Map<String, Object>            configurationProperties,
                                                    List<ReplacementAttributeType> replacementAttributeTypes,
                                                    List<PlaceholderPropertyType>  placeholderPropertyTypes)
    {
        final String methodName = "createServerAssetCatalogTemplate";

        String               qualifiedName;

        if (serverName == null)
        {
            qualifiedName = deployedImplementationType.getDeployedImplementationType() + ":" + assetName;
        }
        else
        {
            qualifiedName = deployedImplementationType.getDeployedImplementationType() + ":" + serverName + ":" + assetName;
        }

        String               versionIdentifier = "V1.0";
        String               description = deployedImplementationType.getDescription();
        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                               deployedImplementationType.getDeployedImplementationType());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    null, "V1.0", null, methodName));

        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  assetName,
                                                  versionIdentifier,
                                                  description,
                                                  null,
                                                  extendedProperties,
                                                  classifications);


        String endpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                        deployedImplementationType.getAssociatedTypeName(),
                                                        qualifiedName + ":Endpoint",
                                                        assetName + " endpoint",
                                                        null,
                                                        networkAddress,
                                                        null,
                                                        null);

        String connectionGUID = archiveHelper.addConnection(qualifiedName + ":Connection",
                                                            assetName + " connection",
                                                            null,
                                                            userId,
                                                            password,
                                                            null,
                                                            null,
                                                            configurationProperties,
                                                            null,
                                                            connectorTypeGUID,
                                                            endpointGUID,
                                                            assetGUID,
                                                            deployedImplementationType.getAssociatedTypeName());

        archiveHelper.addConnectionForAsset(assetGUID, null, connectionGUID);

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addReplacementAttributes(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               replacementAttributeTypes);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               placeholderPropertyTypes);
    }


    /**
     * Create a catalog template for a PostgreSQL database
     */
    private void addPostgresDatabaseCatalogTemplate()
    {
        JDBCResourceConnectorProvider provider = new JDBCResourceConnectorProvider();

        List<PlaceholderPropertyType>  placeholderPropertyTypes = PostgresPlaceholderProperty.getPostgresDatabasePlaceholderPropertyTypes();

        this.createServerAssetCatalogTemplate(DeployedImplementationType.POSTGRESQL_DATABASE,
                                              PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholder(),
                                              PostgresPlaceholderProperty.SERVER_NAME.getPlaceholder(),
                                              PostgresPlaceholderProperty.DATABASE_USER_ID.getPlaceholder(),
                                              PostgresPlaceholderProperty.DATABASE_PASSWORD.getPlaceholder(),
                                              provider.getConnectorType().getGUID(),
                                              "jdbc:postgresql://" +
                                                         PostgresPlaceholderProperty.HOST_IDENTIFIER.getPlaceholder() + ":" +
                                                         PostgresPlaceholderProperty.PORT_NUMBER.getPlaceholder() + "/" + PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholder(),
                                              null,
                                              null,
                                              placeholderPropertyTypes);
    }


    /**
     * Create a catalog template for a PostgreSQL database schema
     */
    private void addPostgresDatabaseSchemaCatalogTemplate()
    {
        JDBCResourceConnectorProvider provider = new JDBCResourceConnectorProvider();

        List<PlaceholderPropertyType>  placeholderPropertyTypes = PostgresPlaceholderProperty.getPostgresSchemaPlaceholderPropertyTypes();

        this.createServerAssetCatalogTemplate(DeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA,
                                              PostgresPlaceholderProperty.SCHEMA_NAME.getPlaceholder(),
                                              PostgresPlaceholderProperty.SERVER_NAME.getPlaceholder() + "." + PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholder(),
                                              PostgresPlaceholderProperty.DATABASE_USER_ID.getPlaceholder(),
                                              PostgresPlaceholderProperty.DATABASE_PASSWORD.getPlaceholder(),
                                              provider.getConnectorType().getGUID(),
                                              "jdbc:postgresql://" +
                                                      PostgresPlaceholderProperty.HOST_IDENTIFIER.getPlaceholder() + ":" +
                                                      PostgresPlaceholderProperty.PORT_NUMBER.getPlaceholder() + "/" +
                                                      PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholder() + "?currentSchema=" +
                                                      PostgresPlaceholderProperty.SCHEMA_NAME.getPlaceholder(),
                                              null,
                                              null,
                                              placeholderPropertyTypes);
    }


    /**
     * Create a catalog template for a Kafka topic
     */
    private void addKafkaTopicCatalogTemplate()
    {
        KafkaOpenMetadataTopicProvider provider = new KafkaOpenMetadataTopicProvider();

        List<PlaceholderPropertyType>  placeholderPropertyTypes = KafkaPlaceholderProperty.getKafkaTopicPlaceholderPropertyTypes();

        Map<String, Object> configurationProperties = new HashMap<>();
        Map<String, String> bootstrapServersProperties = new HashMap<>();

        bootstrapServersProperties.put("bootstrap.servers",
                                       KafkaPlaceholderProperty.HOST_IDENTIFIER.getPlaceholder() + ":" +
                                               KafkaPlaceholderProperty.PORT_NUMBER.getPlaceholder());

        configurationProperties.put(KafkaPlaceholderProperty.EVENT_DIRECTION.getName(), "inOut");
        configurationProperties.put("producer", bootstrapServersProperties);
        configurationProperties.put("consumer", bootstrapServersProperties);

        this.createServerAssetCatalogTemplate(DeployedImplementationType.APACHE_KAFKA_TOPIC,
                                              DeployedImplementationType.APACHE_KAFKA_TOPIC.getAssociatedTypeName(),
                                              PostgresPlaceholderProperty.SERVER_NAME.getPlaceholder() + "." + KafkaPlaceholderProperty.TOPIC_NAME.getPlaceholder() + ":inOut",
                                              null,
                                              null,
                                              provider.getConnectorType().getGUID(),
                                              KafkaPlaceholderProperty.TOPIC_NAME.getPlaceholder(),
                                              configurationProperties,
                                              null,
                                              placeholderPropertyTypes);
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
                                                 null,
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
                                                                 OpenMetadataType.VALID_VALUE_SET.typeName,
                                                                 OpenMetadataType.VALID_VALUE_SET.typeName,
                                                                 qualifiedName,
                                                                 openMetadataType.typeName,
                                                                 openMetadataType.description,
                                                                 category,
                                                                 OpenMetadataValidValues.VALID_METADATA_VALUES_USAGE,
                                                                 "string",
                                                                 OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE,
                                                                 openMetadataType.typeName,
                                                                 false,
                                                                 false,
                                                                 null);

        if (openMetadataType.wikiURL != null)
        {
            String externalReferenceGUID = this.archiveHelper.addExternalReference(null,
                                                                                   validValueGUID,
                                                                                   OpenMetadataType.VALID_VALUE_SET.typeName,
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
                                                                                   openMetadataType.wikiURL,
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
        String parentSetGUID = this.getParentSet(null,
                                                 associatedTypeName,
                                                 OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                 null);

        String typeQualifiedName = constructValidValueQualifiedName(null,
                                                                    OpenMetadataProperty.TYPE_NAME.name,
                                                                    null,
                                                                    associatedTypeName);

        Map<String, String> additionalProperties = new HashMap<>();

        additionalProperties.put(OpenMetadataProperty.TYPE_NAME.name, associatedTypeName);

        String validValueGUID = this.archiveHelper.addValidValue(null,
                                                                 parentSetGUID,
                                                                 parentSetGUID,
                                                                 OpenMetadataType.VALID_VALUE_SET.typeName,
                                                                 OpenMetadataType.VALID_VALUE_SET.typeName,
                                                                 qualifiedName,
                                                                 deployedImplementationType,
                                                                 description,
                                                                 category,
                                                                 OpenMetadataValidValues.VALID_METADATA_VALUES_USAGE,
                                                                 "string",
                                                                 OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE,
                                                                 deployedImplementationType,
                                                                 false,
                                                                 false,
                                                                 additionalProperties);

        if (wikiLink != null)
        {
            String externalReferenceGUID = this.archiveHelper.addExternalReference(null,
                                                                                   validValueGUID,
                                                                                   OpenMetadataType.VALID_VALUE_SET.typeName,
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
                                                                                   wikiLink,
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

        String parentSetGUID = this.getParentSet(null,
                                                 OpenMetadataType.DATA_FILE.typeName,
                                                 OpenMetadataProperty.FILE_TYPE.name,
                                                 null);

        this.archiveHelper.addValidValue(null,
                                         parentSetGUID,
                                         parentSetGUID,
                                         OpenMetadataType.VALID_VALUE_SET.typeName,
                                         OpenMetadataType.VALID_VALUE_SET.typeName,
                                         qualifiedName,
                                         fileTypeName,
                                         description,
                                         category,
                                         OpenMetadataValidValues.VALID_METADATA_VALUES_USAGE,
                                         "string",
                                         OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE,
                                         fileTypeName,
                                         false,
                                         false,
                                         additionalProperties);

        if (deployedImplementationType != null)
        {
            String deployedImplementationTypeQName = deployedImplementationType.getQualifiedName();
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

        String parentSetGUID = this.getParentSet(null,
                                                 OpenMetadataType.DATA_FILE.typeName,
                                                 OpenMetadataProperty.FILE_NAME.name,
                                                 null);

        this.archiveHelper.addValidValue(null,
                                         parentSetGUID,
                                         parentSetGUID,
                                         OpenMetadataType.VALID_VALUE_SET.typeName,
                                         OpenMetadataType.VALID_VALUE_SET.typeName,
                                         qualifiedName,
                                         fileName,
                                         null,
                                         category,
                                         OpenMetadataValidValues.VALID_METADATA_VALUES_USAGE,
                                         "string",
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

        String parentSetGUID = this.getParentSet(null,
                                                 OpenMetadataType.DATA_FILE.typeName,
                                                 OpenMetadataProperty.FILE_EXTENSION.name,
                                                 null);

        this.archiveHelper.addValidValue(null, 
                                         parentSetGUID,
                                         parentSetGUID,
                                         OpenMetadataType.VALID_VALUE_SET.typeName,
                                         OpenMetadataType.VALID_VALUE_SET.typeName,
                                         qualifiedName,
                                         fileExtension,
                                         null,
                                         category,
                                         OpenMetadataValidValues.VALID_METADATA_VALUES_USAGE,
                                         "string",
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
     * Add reference data for an attribute name.
     *
     * @param attributeName   name of the attribute
     * @param attributeDescription  description of the attribute
     */
    private void addAttributeName(String attributeName,
                                  String attributeDescription)
    {
        String qualifiedName = constructValidValueQualifiedName(OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                OpenMetadataProperty.PROPERTY_TYPE.name,
                                                                null,
                                                                attributeName);

        String category = constructValidValueCategory(OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName,
                                                      OpenMetadataProperty.PROPERTY_TYPE.name,
                                                      null);

        String parentSetGUID = this.getParentSet(null,
                                                 OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName,
                                                 OpenMetadataProperty.PROPERTY_TYPE.name,
                                                 null);

        this.archiveHelper.addValidValue(null,
                                         parentSetGUID,
                                         parentSetGUID,
                                         OpenMetadataType.VALID_VALUE_SET.typeGUID,
                                         OpenMetadataType.VALID_VALUE_SET.typeName,
                                         qualifiedName,
                                         attributeName,
                                         attributeDescription,
                                         category,
                                         OpenMetadataValidValues.VALID_METADATA_VALUES_USAGE,
                                         "string",
                                         OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE,
                                         attributeName,
                                         false,
                                         false,
                                         null);
    }


    /**
     * Find or create the parent set for a valid value.
     *
     * @param requestedGUID optional guid for the valid value
     * @param typeName name of the type (can be null)
     * @param propertyName name of the property (can be null)
     * @param mapName name of the mapName (can be null)
     * @return unique identifier (guid) of the parent set
     */
    private String getParentSet(String requestedGUID,
                                String typeName,
                                String propertyName,
                                String mapName)
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
                grandParentSetGUID = getParentSet(null, typeName, propertyName, null);
            }
            else if (propertyName != null)
            {
                grandParentSetGUID = getParentSet(null, typeName, null, null);
            }
            else if (typeName != null)
            {
                grandParentSetGUID = getParentSet(null, null, null, null);
            }

            parentSetGUID =  archiveHelper.addValidValue(requestedGUID,
                                                         grandParentSetGUID,
                                                         grandParentSetGUID,
                                                         OpenMetadataType.VALID_VALUE_SET.typeName,
                                                         OpenMetadataType.VALID_VALUE_SET.typeName,
                                                         parentQualifiedName,
                                                         parentDisplayName,
                                                         parentDescription,
                                                         constructValidValueCategory(typeName, propertyName, mapName),
                                                         OpenMetadataValidValues.VALID_METADATA_VALUES_USAGE,
                                                         null,
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
     * @param governanceEngineName name
     * @return unique identifier for the governance engine
     */
    private String getFileProvisioningEngine(String governanceEngineName)
    {
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
     * @param assetOnboardingEngineName name
     * @return unique identifier for the governance engine
     */
    private String getAssetOnboardingEngine(String assetOnboardingEngineName)
    {
        final String assetOnboardingEngineDisplayName = "Asset Onboarding Engine";
        final String assetOnboardingEngineDescription = "Monitors, validates and enriches metadata relating to assets as they are catalogued.";

        return archiveHelper.addGovernanceEngine(OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                                                 assetOnboardingEngineName,
                                                 assetOnboardingEngineDisplayName,
                                                 assetOnboardingEngineDescription,
                                                 null,
                                                 null,
                                                 null,
                                                 null,
                                                 null,
                                                 null);
    }



    /**
     * Create an entity for the Stewardship governance engine.
     *
     * @param stewardshipEngineName name
     * @return unique identifier for the governance engine
     */
    private String getStewardshipEngine(String stewardshipEngineName)
    {
        final String stewardshipEngineDisplayName = "Stewardship Engine";
        final String stewardshipEngineDescription = "Liaises with stewards to make corrections to open metadata.";

        return archiveHelper.addGovernanceEngine(OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                                                 stewardshipEngineName,
                                                 stewardshipEngineDisplayName,
                                                 stewardshipEngineDescription,
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
     * @param assetSurveyEngineName name
     * @return unique identifier for the governance engine
     */
    private String getAssetSurveyEngine(String assetSurveyEngineName)
    {
        final String assetSurveyEngineDisplayName = "Asset Survey Engine";
        final String assetSurveyEngineDescription = "Extracts information about a digital resource and attaches it to its asset description.";

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
     * Create an entity for the File Provisioning governance action service.
     *
     * @return descriptive information on the governance service
     */
    private GovernanceActionDescription getFileProvisioningGovernanceActionService()
    {
        final String governanceServiceName        = "file-provisioning-governance-action-service";
        final String governanceServiceDisplayName = "File {move, copy, delete} Governance Action Service";
        final String governanceServiceDescription = "Works with files.  The request type defines which action is taken.  " +
                                                            "The request parameters define the source file and destination folder, along with lineage options";
        final String ftpGovernanceServiceProviderClassName = MoveCopyFileGovernanceActionProvider.class.getName();

        MoveCopyFileGovernanceActionProvider provider = new MoveCopyFileGovernanceActionProvider();

        GovernanceActionDescription governanceActionDescription = getGovernanceActionDescription(ResourceUse.PROVISION_RESOURCE,
                                                                                                 provider,
                                                                                                 governanceServiceDescription);

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
        final String governanceServiceName              = "new-files-watchdog-governance-action-service";
        final String governanceServiceDisplayName       = "New Files Watchdog Governance Action Service";
        final String governanceServiceDescription       = "Initiates a governance action process when a new file arrives.";
        final String governanceServiceProviderClassName = GenericFolderWatchdogGovernanceActionProvider.class.getName();

        GenericFolderWatchdogGovernanceActionProvider provider = new GenericFolderWatchdogGovernanceActionProvider();

        GovernanceActionDescription governanceActionDescription = getGovernanceActionDescription(ResourceUse.WATCH_DOG,
                                                                                                 provider,
                                                                                                 governanceServiceDescription);

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
     * Create a governance action description from the governance service's provider.
     *
     * @param resourceUse how is this
     * @param provider connector provider
     * @return governance action description
     */
    private GovernanceActionDescription getGovernanceActionDescription(ResourceUse                   resourceUse,
                                                                       GovernanceServiceProviderBase provider,
                                                                       String                        governanceServiceDescription)
    {
        GovernanceActionDescription governanceActionDescription = new GovernanceActionDescription();

        governanceActionDescription.resourceUse                  = resourceUse;
        governanceActionDescription.supportedRequestTypes        = provider.getSupportedRequestTypes();
        governanceActionDescription.supportedRequestParameters   = provider.getSupportedRequestParameters();
        governanceActionDescription.supportedActionTargets       = provider.getSupportedActionTargetTypes();
        governanceActionDescription.producedRequestParameters    = provider.getProducedRequestParameters();
        governanceActionDescription.producedActionTargets        = provider.getProducedActionTargetTypes();
        governanceActionDescription.producedGuards               = provider.getProducedGuards();

        if (provider instanceof SurveyActionServiceProvider surveyActionServiceProvider)
        {
            governanceActionDescription.supportedAnalysisSteps = surveyActionServiceProvider.getSupportedAnalysisSteps();
            governanceActionDescription.supportedAnnotationTypes = surveyActionServiceProvider.getProducedAnnotationTypes();
        }

        governanceActionDescription.governanceServiceDescription = governanceServiceDescription;

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
        final String governanceServiceProviderClassName = ZonePublisherGovernanceActionProvider.class.getName();

        ZonePublisherGovernanceActionProvider provider = new ZonePublisherGovernanceActionProvider();

        final String governanceServiceDescription = provider.getConnectorType().getDescription();

        GovernanceActionDescription governanceActionDescription = getGovernanceActionDescription(ResourceUse.IMPROVE_METADATA,
                                                                                                 provider,
                                                                                                 governanceServiceDescription);

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
     * Add a governance service that links elements with the same qualified name.
     *
     * @return descriptive information on the governance service
     */
    private GovernanceActionDescription getQualifiedNameDeDupGovernanceActionService()
    {
        final String governanceServiceName = "qualified-name-deduplication-governance-action-service";
        final String governanceServiceDisplayName = "Qualified Name De-duplicator Governance Action Service";
        final String governanceServiceDescription = "Detect elements with the same qualified names.";
        final String governanceServiceProviderClassName = QualifiedNamePeerDuplicateGovernanceActionProvider.class.getName();

        QualifiedNamePeerDuplicateGovernanceActionProvider provider = new QualifiedNamePeerDuplicateGovernanceActionProvider();

        GovernanceActionDescription governanceActionDescription = getGovernanceActionDescription(ResourceUse.IMPROVE_METADATA,
                                                                                                 provider,
                                                                                                 governanceServiceDescription);

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
     * Add a governance service that checks for request for action annotations in a survey report.
     *
     * @return descriptive information on the governance service
     */
    private GovernanceActionDescription getEvaluateAnnotationsGovernanceActionService()
    {
        final String governanceServiceName = "evaluate-annotations-governance-action-service";
        final String governanceServiceDisplayName = "Verify annotations in a Survey Report";
        final String governanceServiceProviderClassName = EvaluateAnnotationsGovernanceActionProvider.class.getName();

        EvaluateAnnotationsGovernanceActionProvider provider = new EvaluateAnnotationsGovernanceActionProvider();
        final String governanceServiceDescription = provider.getConnectorType().getDescription();

        GovernanceActionDescription governanceActionDescription = getGovernanceActionDescription(ResourceUse.IMPROVE_METADATA,
                                                                                                 provider,
                                                                                                 governanceServiceDescription);

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
     * Add a governance service that writes an audit log message to the audit log destinations.
     *
     * @return descriptive information on the governance service
     */
    private GovernanceActionDescription getWriteAuditLogGovernanceActionService()
    {
        final String governanceServiceName = "write-audit-log-governance-action-service";
        final String governanceServiceDisplayName = "Write an Audit Log Message";
        final String governanceServiceProviderClassName = WriteAuditLogMessageGovernanceActionProvider.class.getName();

        WriteAuditLogMessageGovernanceActionProvider provider = new WriteAuditLogMessageGovernanceActionProvider();
        final String governanceServiceDescription = provider.getConnectorType().getDescription();

        GovernanceActionDescription governanceActionDescription = getGovernanceActionDescription(ResourceUse.INFORM_STEWARD,
                                                                                                 provider,
                                                                                                 governanceServiceDescription);

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
     * Add a governance service that writes an audit log message to the audit log destinations.
     *
     * @return descriptive information on the governance service
     */
    private GovernanceActionDescription getDayOfWeekGovernanceActionService()
    {
        final String governanceServiceName = "get-day-of-week-governance-action-service";
        final String governanceServiceDisplayName = "Detect the day of the week";
        final String governanceServiceProviderClassName = DaysOfWeekGovernanceActionProvider.class.getName();

        DaysOfWeekGovernanceActionProvider provider = new DaysOfWeekGovernanceActionProvider();
        final String governanceServiceDescription = provider.getConnectorType().getDescription();

        GovernanceActionDescription governanceActionDescription = getGovernanceActionDescription(ResourceUse.CHOOSE_PATH,
                                                                                                 provider,
                                                                                                 governanceServiceDescription);

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
     * Add a governance service that verifies and asset.
     *
     * @return descriptive information on the governance service
     */
    private GovernanceActionDescription getVerifyAssetGovernanceActionService()
    {
        final String governanceServiceName = "verify-asset-governance-action-service";
        final String governanceServiceDisplayName = "Verify an Asset Governance Action Service";
        final String governanceServiceProviderClassName = VerifyAssetGovernanceActionProvider.class.getName();

        VerifyAssetGovernanceActionProvider provider = new VerifyAssetGovernanceActionProvider();
        final String governanceServiceDescription = provider.getConnectorType().getDescription();

        GovernanceActionDescription governanceActionDescription = getGovernanceActionDescription(ResourceUse.IMPROVE_METADATA,
                                                                                                 provider,
                                                                                                 governanceServiceDescription);

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

        GovernanceActionDescription governanceActionDescription = getGovernanceActionDescription(ResourceUse.IMPROVE_METADATA,
                                                                                                 provider,
                                                                                                 governanceServiceDescription);

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
        String                     governanceServiceGUID        = null;
        String                     governanceServiceDescription = null;
        List<RequestTypeType>      supportedRequestTypes        = null;
        List<RequestParameterType> supportedRequestParameters   = null;
        List<ActionTargetType>     supportedActionTargets       = null;
        List<AnalysisStepType>     supportedAnalysisSteps       = null;
        List<AnnotationTypeType>   supportedAnnotationTypes     = null;
        List<RequestParameterType> producedRequestParameters    = null;
        List<ActionTargetType>     producedActionTargets        = null;
        List<GuardType>            producedGuards               = null;
        ResourceUse                resourceUse                  = null;
    }


    /**
     * Add details of a request type to the engine.
     *
     * @param governanceEngineGUID unique identifier of the engine
     * @param governanceEngineName name of the governance engine
     * @param governanceEngineTypeName type of engine
     * @param governanceRequestType name of request type
     * @param serviceRequestType internal name of the request type
     * @param requestParameters any request parameters
     * @param governanceActionDescription description of the governance action if and
     */
    private void addRequestType(String                      governanceEngineGUID,
                                String                      governanceEngineName,
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

        if (governanceActionDescription.supportedActionTargets != null)
        {
            String governanceActionTypeGUID = archiveHelper.addGovernanceActionType(null,
                                                                                    governanceEngineGUID,
                                                                                    governanceEngineTypeName,
                                                                                    "Egeria:GovernanceActionType:" + governanceEngineName + ":" + governanceRequestType,
                                                                                    governanceRequestType + " (" + governanceEngineName + ")",
                                                                                    governanceActionDescription.governanceServiceDescription,
                                                                                    0,
                                                                                    governanceActionDescription.supportedRequestParameters,
                                                                                    governanceActionDescription.supportedActionTargets,
                                                                                    governanceActionDescription.supportedAnalysisSteps,
                                                                                    governanceActionDescription.supportedAnnotationTypes,
                                                                                    governanceActionDescription.producedRequestParameters,
                                                                                    governanceActionDescription.producedActionTargets,
                                                                                    governanceActionDescription.producedGuards,
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

                for (ActionTargetType actionTargetType : governanceActionDescription.supportedActionTargets)
                {
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


    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @param governanceEngineGUID unique identifier of the governance engine
     * @param governanceEngineName unique name of the governance engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addWatchNestedInFolderRequestType(String                      governanceEngineGUID,
                                                   String                      governanceEngineName,
                                                   GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "watch-for-new-files-in-folder";
        final String serviceRequestType = "watch-nested-in-folder";

        this.addRequestType(governanceEngineGUID,
                            governanceEngineName,
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
     * @param governanceEngineName unique name of the governance engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addCopyFileRequestType(String                      governanceEngineGUID,
                                        String                      governanceEngineName,
                                        GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "copy-file";

        this.addRequestType(governanceEngineGUID,
                            governanceEngineName,
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
     * @param governanceEngineName unique name of the governance engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addMoveFileRequestType(String                      governanceEngineGUID,
                                        String                      governanceEngineName,
                                        GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "move-file";

        this.addRequestType(governanceEngineGUID,
                            governanceEngineName,
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
     * @param governanceEngineName unique name of the governance engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addDeleteFileRequestType(String                      governanceEngineGUID,
                                          String                      governanceEngineName,
                                          GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "delete-file";

        this.addRequestType(governanceEngineGUID,
                            governanceEngineName,
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
     * @param governanceEngineName unique name of the governance engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addSeekOriginRequestType(String                      governanceEngineGUID,
                                          String                      governanceEngineName,
                                          GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "seek-origin-of-asset";

        this.addRequestType(governanceEngineGUID,
                            governanceEngineName,
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
     * @param governanceEngineName unique name of the governance engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addSetZoneMembershipRequestType(String                      governanceEngineGUID,
                                                 String                      governanceEngineName,
                                                 GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "set-zone-membership-for-asset";

        this.addRequestType(governanceEngineGUID,
                            governanceEngineName,
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
     * @param governanceEngineName unique name of the governance engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addVerifyAssetRequestType(String                      governanceEngineGUID,
                                           String                      governanceEngineName,
                                           GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "verify-asset";

        this.addRequestType(governanceEngineGUID,
                            governanceEngineName,
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
     * @param governanceEngineName unique name of the governance engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addEvaluateAnnotationsRequestType(String                      governanceEngineGUID,
                                                   String                      governanceEngineName,
                                                   GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "evaluate-annotations";

        this.addRequestType(governanceEngineGUID,
                            governanceEngineName,
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
     * @param governanceEngineName unique name of the governance engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addWriteAuditLogRequestType(String                      governanceEngineGUID,
                                             String                      governanceEngineName,
                                             GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "write-to-audit-log";

        this.addRequestType(governanceEngineGUID,
                            governanceEngineName,
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
     * @param governanceEngineName unique name of the governance engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addDayOfWeekRequestType(String                      governanceEngineGUID,
                                         String                      governanceEngineName,
                                         GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "get-day-of-week";

        this.addRequestType(governanceEngineGUID,
                            governanceEngineName,
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
     * @param governanceEngineName unique name of the governance engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addQualifiedNameDeDupRequestType(String                      governanceEngineGUID,
                                                  String                      governanceEngineName,
                                                  GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "qualified-name-de-dup";

        this.addRequestType(governanceEngineGUID,
                            governanceEngineName,
                            OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                            governanceRequestType,
                            null,
                            null,
                            governanceActionDescription);
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

        GovernanceActionDescription governanceActionDescription = getGovernanceActionDescription(ResourceUse.SURVEY_RESOURCE,
                                                                                                 provider,
                                                                                                 surveyServiceDescription);

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

        GovernanceActionDescription governanceActionDescription = getGovernanceActionDescription(ResourceUse.SURVEY_RESOURCE,
                                                                                                 provider,
                                                                                                 surveyServiceDescription);

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

        GovernanceActionDescription governanceActionDescription = getGovernanceActionDescription(ResourceUse.SURVEY_RESOURCE,
                                                                                                 provider,
                                                                                                 surveyServiceDescription);

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
     * Create an entity for the Apache Atlas Survey governance service.
     *
     * @return unique identifier for the governance service
     */
    private GovernanceActionDescription getAtlasSurveyService()
    {
        final String surveyServiceName = "apache-atlas-survey-service";
        final String surveyServiceDisplayName = "Apache Atlas Survey Service";
        final String surveyServiceProviderClassName = SurveyApacheAtlasProvider.class.getName();

        SurveyApacheAtlasProvider provider = new SurveyApacheAtlasProvider();

        final String surveyServiceDescription = provider.getConnectorType().getDescription();

        GovernanceActionDescription governanceActionDescription = getGovernanceActionDescription(ResourceUse.SURVEY_RESOURCE,
                                                                                                 provider,
                                                                                                 surveyServiceDescription);

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
     * Create an entity for the Postgres Server Survey governance service.
     *
     * @return unique identifier for the governance service
     */
    private GovernanceActionDescription getPostgresServerSurveyService()
    {
        final String surveyServiceName = "postgres-server-survey-service";
        final String surveyServiceDisplayName = "PostgreSQL Server Survey Service";
        final String surveyServiceProviderClassName = PostgresServerSurveyActionProvider.class.getName();

        PostgresServerSurveyActionProvider provider = new PostgresServerSurveyActionProvider();

        final String surveyServiceDescription = provider.getConnectorType().getDescription();

        GovernanceActionDescription governanceActionDescription = getGovernanceActionDescription(ResourceUse.SURVEY_RESOURCE,
                                                                                                 provider,
                                                                                                 surveyServiceDescription);

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
     * Create an entity for the Postgres Database Survey governance service.
     *
     * @return unique identifier for the governance service
     */
    private GovernanceActionDescription getPostgresDatabaseSurveyService()
    {
        final String surveyServiceName = "postgres-database-survey-service";
        final String surveyServiceDisplayName = "PostgreSQL Database Survey Service";
        final String surveyServiceProviderClassName = PostgresDatabaseSurveyActionProvider.class.getName();

        PostgresDatabaseSurveyActionProvider provider = new PostgresDatabaseSurveyActionProvider();

        final String surveyServiceDescription = provider.getConnectorType().getDescription();

        GovernanceActionDescription governanceActionDescription = getGovernanceActionDescription(ResourceUse.SURVEY_RESOURCE,
                                                                                                 provider,
                                                                                                 surveyServiceDescription);

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
     * Create an entity for the Apache Kafka Server Survey governance service.
     *
     * @return unique identifier for the governance service
     */
    private GovernanceActionDescription getKafkaServerSurveyService()
    {
        final String surveyServiceName = "kafka-server-survey-service";
        final String surveyServiceDisplayName = "Apache Kafka Server Survey Service";
        final String surveyServiceProviderClassName = SurveyApacheKafkaServerProvider.class.getName();

        SurveyApacheKafkaServerProvider provider = new SurveyApacheKafkaServerProvider();

        final String surveyServiceDescription = provider.getConnectorType().getDescription();

        GovernanceActionDescription governanceActionDescription = getGovernanceActionDescription(ResourceUse.SURVEY_RESOURCE,
                                                                                                 provider,
                                                                                                 surveyServiceDescription);

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
     * @param governanceEngineName unique name of the governance engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addCSVFileRequestType(String                      governanceEngineGUID,
                                       String                      governanceEngineName,
                                       GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "survey-csv-file";

        this.addRequestType(governanceEngineGUID,
                            governanceEngineName,
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
     * @param governanceEngineName unique name of the governance engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addDataFileRequestType(String                      governanceEngineGUID,
                                        String                      governanceEngineName,
                                        GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "survey-data-file";

        this.addRequestType(governanceEngineGUID,
                            governanceEngineName,
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
     * @param governanceEngineName unique name of the governance engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addFolderRequestType(String                      governanceEngineGUID,
                                      String                      governanceEngineName,
                                      GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "survey-folder";

        this.addRequestType(governanceEngineGUID,
                            governanceEngineName,
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
     * @param governanceEngineName unique name of the governance engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addAtlasRequestType(String                      governanceEngineGUID,
                                     String                      governanceEngineName,
                                     GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "survey-apache-atlas-server";

        this.addRequestType(governanceEngineGUID,
                            governanceEngineName,
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
     * @param governanceEngineName unique name of the governance engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addPostgresServerRequestType(String                      governanceEngineGUID,
                                              String                      governanceEngineName,
                                              GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "survey-postgres-server";

        this.addRequestType(governanceEngineGUID,
                            governanceEngineName,
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
     * @param governanceEngineName unique name of the governance engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addPostgresDatabaseRequestType(String                      governanceEngineGUID,
                                                String                      governanceEngineName,
                                                GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "survey-postgres-database";

        this.addRequestType(governanceEngineGUID,
                            governanceEngineName,
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
     * @param governanceEngineName unique name of the governance engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addKafkaServerRequestType(String                      governanceEngineGUID,
                                           String                      governanceEngineName,
                                           GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "survey-kafka-server";

        this.addRequestType(governanceEngineGUID,
                            governanceEngineName,
                            OpenMetadataType.SURVEY_ACTION_ENGINE.typeName,
                            governanceRequestType,
                            null,
                            null,
                            governanceActionDescription);
    }



    private void createDailyGovernanceActionProcess(String governanceEngineGUID)
    {
        String processGUID = archiveHelper.addGovernanceActionProcess(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                      "Egeria:DailyGovernanceActionProcess",
                                                                      "DailyGovernanceActionProcess",
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      0,
                                                                      null,
                                                                      null,
                                                                      null);

        String step1GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                        "Egeria:DailyGovernanceActionProcess:GetDayOfWeek",
                                                                        "Get the day of the Week",
                                                                        null,
                                                                        0,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        0,
                                                                        true,
                                                                        null,
                                                                        null,
                                                                        null);

        if (step1GUID != null)
        {
            archiveHelper.addGovernanceActionExecutor(step1GUID,
                                                      "get-day-of-week",
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      governanceEngineGUID);

            archiveHelper.addGovernanceActionProcessFlow(processGUID,
                                                         null,
                                                         step1GUID);
        }

        String step2GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                        "Egeria:DailyGovernanceActionProcess:MondayTask",
                                                                        "Output Monday's task",
                                                                        null,
                                                                        0,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        0,
                                                                        true,
                                                                        null,
                                                                        null,
                                                                        null);

        if (step2GUID != null)
        {
            Map<String, String> requestParameters = new HashMap<>();

            requestParameters.put(WriteAuditLogRequestParameter.MESSAGE_TEXT.getName(), "Action For Monday is: Wash");
            archiveHelper.addGovernanceActionExecutor(step2GUID,
                                                      "write-to-audit-log",
                                                      requestParameters,
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      governanceEngineGUID);

            archiveHelper.addNextGovernanceActionProcessStep(step1GUID, "monday", false, step2GUID);
        }

        String step3GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                        "Egeria:DailyGovernanceActionProcess:TuesdayTask",
                                                                        "Output Tuesday's task",
                                                                        null,
                                                                        0,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        0,
                                                                        true,
                                                                        null,
                                                                        null,
                                                                        null);

        if (step3GUID != null)
        {
            Map<String, String> requestParameters = new HashMap<>();

            requestParameters.put(WriteAuditLogRequestParameter.MESSAGE_TEXT.getName(), "Action For Tuesday is: Iron");
            archiveHelper.addGovernanceActionExecutor(step3GUID,
                                                      "write-to-audit-log",
                                                      requestParameters,
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      governanceEngineGUID);

            archiveHelper.addNextGovernanceActionProcessStep(step1GUID, "tuesday", false, step3GUID);
        }

        String step4GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                        "Egeria:DailyGovernanceActionProcess:WednesdayTask",
                                                                        "Output Wednesday's task",
                                                                        null,
                                                                        0,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        0,
                                                                        true,
                                                                        null,
                                                                        null,
                                                                        null);

        if (step4GUID != null)
        {
            Map<String, String> requestParameters = new HashMap<>();

            requestParameters.put(WriteAuditLogRequestParameter.MESSAGE_TEXT.getName(), "Action For Wednesday is: Mend");
            archiveHelper.addGovernanceActionExecutor(step4GUID,
                                                      "write-to-audit-log",
                                                      requestParameters,
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      governanceEngineGUID);

            archiveHelper.addNextGovernanceActionProcessStep(step1GUID, "wednesday", false, step4GUID);
        }

        String step5GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                        "Egeria:DailyGovernanceActionProcess:ThursdayTask",
                                                                        "Output Thursday's task",
                                                                        null,
                                                                        0,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        0,
                                                                        true,
                                                                        null,
                                                                        null,
                                                                        null);

        if (step5GUID != null)
        {
            Map<String, String> requestParameters = new HashMap<>();

            requestParameters.put(WriteAuditLogRequestParameter.MESSAGE_TEXT.getName(), "Action For Thursday is: Market");
            archiveHelper.addGovernanceActionExecutor(step5GUID,
                                                      "write-to-audit-log",
                                                      requestParameters,
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      governanceEngineGUID);

            archiveHelper.addNextGovernanceActionProcessStep(step1GUID, "thursday", false, step5GUID);
        }

        String step6GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                        "Egeria:DailyGovernanceActionProcess:FridayTask",
                                                                        "Output Friday's task",
                                                                        null,
                                                                        0,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        0,
                                                                        true,
                                                                        null,
                                                                        null,
                                                                        null);

        if (step6GUID != null)
        {
            Map<String, String> requestParameters = new HashMap<>();

            requestParameters.put(WriteAuditLogRequestParameter.MESSAGE_TEXT.getName(), "Action For Friday is: Clean");
            archiveHelper.addGovernanceActionExecutor(step6GUID,
                                                      "write-to-audit-log",
                                                      requestParameters,
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      governanceEngineGUID);

            archiveHelper.addNextGovernanceActionProcessStep(step1GUID, "friday", false, step6GUID);
        }

        String step7GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                        "Egeria:DailyGovernanceActionProcess:SaturdayTask",
                                                                        "Output Saturday's task",
                                                                        null,
                                                                        0,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        0,
                                                                        true,
                                                                        null,
                                                                        null,
                                                                        null);

        if (step7GUID != null)
        {
            Map<String, String> requestParameters = new HashMap<>();

            requestParameters.put(WriteAuditLogRequestParameter.MESSAGE_TEXT.getName(), "Action For Saturday is: Bake");
            archiveHelper.addGovernanceActionExecutor(step7GUID,
                                                      "write-to-audit-log",
                                                      requestParameters,
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      governanceEngineGUID);

            archiveHelper.addNextGovernanceActionProcessStep(step1GUID, "saturday", false, step7GUID);
        }


        String step8GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                        "Egeria:DailyGovernanceActionProcess:SundayTask",
                                                                        "Output Sunday's task",
                                                                        null,
                                                                        0,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        0,
                                                                        true,
                                                                        null,
                                                                        null,
                                                                        null);

        if (step8GUID != null)
        {
            Map<String, String> requestParameters = new HashMap<>();

            requestParameters.put(WriteAuditLogRequestParameter.MESSAGE_TEXT.getName(), "Action For Sunday is: Rest");
            archiveHelper.addGovernanceActionExecutor(step8GUID,
                                                      "write-to-audit-log",
                                                      requestParameters,
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      governanceEngineGUID);

            archiveHelper.addNextGovernanceActionProcessStep(step1GUID, "sunday", false, step8GUID);
        }
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