/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.openconnectors;

import org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ApacheAtlasIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.ApacheAtlasRESTProvider;
import org.odpi.openmetadata.adapters.connectors.apachekafka.control.KafkaPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.apachekafka.integration.KafkaTopicIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.apachekafka.resource.ApacheKafkaAdminProvider;
import org.odpi.openmetadata.adapters.connectors.apachekafka.survey.SurveyApacheKafkaServerProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStoreProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFolderProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVFileStoreProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.datafolder.DataFolderProvider;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.OMAGServerPlatformProvider;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.catalog.OMAGServerPlatformCatalogProvider;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.*;
import org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship.CreateServerGuard;
import org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship.DaysOfWeekGuard;
import org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship.WriteAuditLogRequestParameter;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.DataFilesMonitorIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.DataFolderMonitorIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.csvlineageimporter.CSVLineageImporterProvider;
import org.odpi.openmetadata.adapters.connectors.integration.egeria.EgeriaCataloguerIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.JDBCIntegrationConnectorProvider;
import org.odpi.openmetadata.adapters.connectors.integration.kafka.KafkaMonitorIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.kafkaaudit.DistributeAuditEventsFromKafkaProvider;
import org.odpi.openmetadata.adapters.connectors.integration.openapis.OpenAPIMonitorIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.openlineage.*;
import org.odpi.openmetadata.adapters.connectors.postgres.catalog.PostgresServerIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnectorProvider;
import org.odpi.openmetadata.adapters.connectors.secretsstore.envar.EnvVarSecretsStoreProvider;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogTemplateType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceProvider;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.sync.OSSUnityCatalogInsideCatalogSyncProvider;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.sync.OSSUnityCatalogServerSyncProvider;
import org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider;
import org.odpi.openmetadata.adminservices.configuration.registration.*;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.ReplacementAttributeType;
import org.odpi.openmetadata.frameworks.openmetadata.enums.*;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.PropertyFacetValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchive;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveWriter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.samples.archiveutilities.GovernanceActionDescription;
import org.odpi.openmetadata.samples.archiveutilities.GovernanceArchiveHelper;

import java.util.*;

import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueCategory;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * XCoreContentArchiveWriter creates an open metadata archive that includes the connector type
 * information for all open connectors supplied by the egeria project.
 */
public class XCoreContentArchiveWriter extends OMRSArchiveWriter
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

    private final Map<String, String> parentValidValueQNameToGUIDMap  = new HashMap<>();
    private final Map<String, String> deployedImplementationTypeGUIDs = new HashMap<>();
    private final Map<String, String> openMetadataTypeGUIDs           = new HashMap<>();


    /**
     * Default constructor initializes the archive.
     */
    public XCoreContentArchiveWriter()
    {
        List<OpenMetadataArchive> dependentOpenMetadataArchives = new ArrayList<>();

        /*
         * This value allows the elements from the archive to be based on the existing open metadata types
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
                                             OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
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
                                             OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
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
                                             OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
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
                                             OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
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
                                             OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
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

        addOpenMetadataEnumValidNames(OpenMetadataType.CATALOG_TARGET_RELATIONSHIP_TYPE_NAME,
                                      OpenMetadataProperty.DELETE_METHOD.name,
                                      DeleteMethod.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(DeleteMethod.values())));

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

        addOpenMetadataEnumValidNames(OpenMetadataType.RATING_TYPE_NAME,
                                      OpenMetadataType.STARS_PROPERTY_NAME,
                                      StarRating.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(StarRating.values())));

        addOpenMetadataEnumValidNames(OpenMetadataType.EXTERNAL_ID_SCOPE_RELATIONSHIP.typeName,
                                      OpenMetadataProperty.PERMITTED_SYNCHRONIZATION.name,
                                      PermittedSynchronization.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(PermittedSynchronization.values())));

        addOpenMetadataEnumValidNames(OpenMetadataType.REGISTERED_INTEGRATION_CONNECTOR_TYPE_NAME,
                                      OpenMetadataProperty.PERMITTED_SYNCHRONIZATION.name,
                                      PermittedSynchronization.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(PermittedSynchronization.values())));

        addOpenMetadataEnumValidNames(OpenMetadataType.CATALOG_TARGET_RELATIONSHIP_TYPE_NAME,
                                      OpenMetadataProperty.PERMITTED_SYNCHRONIZATION.name,
                                      PermittedSynchronization.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(PermittedSynchronization.values())));

        addOpenMetadataEnumValidNames(OpenMetadataType.TO_DO.typeName,
                                      OpenMetadataProperty.TO_DO_STATUS.name,
                                      ToDoStatus.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(ToDoStatus.values())));

        addOpenMetadataEnumValidNames(OpenMetadataType.PROCESS_HIERARCHY_TYPE_NAME,
                                      OpenMetadataType.CONTAINMENT_TYPE_PROPERTY_NAME,
                                      ProcessContainmentType.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(ProcessContainmentType.values())));

        addOpenMetadataEnumValidNames(OpenMetadataType.RELATED_MEDIA.typeName,
                                      OpenMetadataProperty.MEDIA_TYPE.name,
                                      MediaType.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(MediaType.values())));

        addOpenMetadataEnumValidNames(OpenMetadataType.RELATED_MEDIA.typeName,
                                      OpenMetadataProperty.DEFAULT_MEDIA_USAGE.name,
                                      MediaUsage.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(MediaUsage.values())));

        addOpenMetadataEnumValidNames(OpenMetadataType.MEDIA_REFERENCE_RELATIONSHIP.typeName,
                                      OpenMetadataProperty.MEDIA_USAGE.name,
                                      MediaUsage.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(MediaUsage.values())));

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
         */
        Map<String, String> serviceGUIDs    = new HashMap<>();

        /*
         * Next are the common services of Egeria.
         */
        for (CommonServicesDescription commonServicesDescription : CommonServicesDescription.values())
        {
            if (commonServicesDescription.getServiceDevelopmentStatus() != ComponentDevelopmentStatus.DEPRECATED)
            {
                String guid = this.addDeployedImplementationType(commonServicesDescription.getServiceName(),
                                                                 OpenMetadataType.SOFTWARE_SERVICE.typeName,
                                                                 commonServicesDescription.getServiceDescription(),
                                                                 commonServicesDescription.getServiceWiki());

                serviceGUIDs.put(commonServicesDescription.getServiceName(), guid);
            }
        }

        /*
         * These services support the governance servers.
         */
        for (GovernanceServicesDescription governanceServicesDescription : GovernanceServicesDescription.values())
        {
            if (governanceServicesDescription.getServiceDevelopmentStatus() != ComponentDevelopmentStatus.DEPRECATED)
            {
                String guid = this.addDeployedImplementationType(governanceServicesDescription.getServiceName(),
                                                                 OpenMetadataType.SOFTWARE_SERVICE.typeName,
                                                                 governanceServicesDescription.getServiceDescription(),
                                                                 governanceServicesDescription.getServiceWiki());

                serviceGUIDs.put(governanceServicesDescription.getServiceName(), guid);
            }
        }

        /*
         * The access services are found in the Metadata Access Server and Metadata Access Point OMAG Servers.
         */
        String serverTypeGUID  = deployedImplementationTypeGUIDs.get(DeployedImplementationType.METADATA_ACCESS_SERVER.getDeployedImplementationType());

        for (AccessServiceDescription accessServiceDescription : AccessServiceDescription.values())
        {
            if (accessServiceDescription.getAccessServiceDevelopmentStatus() != ComponentDevelopmentStatus.DEPRECATED)
            {
                String guid = this.addDeployedImplementationType(accessServiceDescription.getAccessServiceFullName(),
                                                                 OpenMetadataType.SOFTWARE_SERVICE.typeName,
                                                                 accessServiceDescription.getAccessServiceDescription(),
                                                                 accessServiceDescription.getAccessServiceWiki());

                serviceGUIDs.put(accessServiceDescription.getAccessServiceFullName(), guid);

                archiveHelper.addResourceListRelationshipByGUID(serverTypeGUID,
                                                                guid,
                                                                ResourceUse.HOSTED_SERVICE.getResourceUse(),
                                                                ResourceUse.HOSTED_SERVICE.getDescription());
            }
        }

        /*
         * View services are found in the View Server.  They call an access service.
         */
        serverTypeGUID = deployedImplementationTypeGUIDs.get(DeployedImplementationType.VIEW_SERVER.getDeployedImplementationType());

        for (ViewServiceDescription viewServiceDescription : ViewServiceDescription.values())
        {
            if (viewServiceDescription.getViewServiceDevelopmentStatus() != ComponentDevelopmentStatus.DEPRECATED)
            {
                String guid = this.addDeployedImplementationType(viewServiceDescription.getViewServiceFullName(),
                                                                 OpenMetadataType.SOFTWARE_SERVICE.typeName,
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
        }

        /*
         * Engine services are found in the Engine Host.   They call an access service.  They also
         * support a particular type of governance engine and governance service.
         */
        serverTypeGUID = deployedImplementationTypeGUIDs.get(DeployedImplementationType.ENGINE_HOST.getDeployedImplementationType());

        for (EngineServiceDescription engineServiceDescription : EngineServiceDescription.values())
        {
            if (engineServiceDescription.getEngineServiceDevelopmentStatus() != ComponentDevelopmentStatus.DEPRECATED)
            {
                String guid = this.addDeployedImplementationType(engineServiceDescription.getEngineServiceFullName(),
                                                                 OpenMetadataType.SOFTWARE_SERVICE.typeName,
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

                String governanceEngineGUID  = deployedImplementationTypeGUIDs.get(engineServiceDescription.getHostedGovernanceEngineDeployedImplementationType());
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
        }

        /*
         * Integration services are found in the integration daemon.  They each call a particular access service and
         * host a particular type of connector.
         */
        for (IntegrationServiceDescription integrationServiceDescription : IntegrationServiceDescription.values())
        {
            if (integrationServiceDescription.getIntegrationServiceDevelopmentStatus() != ComponentDevelopmentStatus.DEPRECATED)
            {
                String guid = this.addDeployedImplementationType(integrationServiceDescription.getIntegrationServiceFullName(),
                                                                 OpenMetadataType.SOFTWARE_SERVICE.typeName,
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
        archiveHelper.addConnectorType(null, new SurveyApacheKafkaServerProvider());
        archiveHelper.addConnectorType(null, new OMAGServerPlatformCatalogProvider());
        archiveHelper.addConnectorType(null, new OMAGServerPlatformProvider());
        archiveHelper.addConnectorType(null, new OMAGServerProvider());
        archiveHelper.addConnectorType(null, new EngineHostProvider());
        archiveHelper.addConnectorType(null, new IntegrationDaemonProvider());
        archiveHelper.addConnectorType(null, new MetadataAccessServerProvider());
        archiveHelper.addConnectorType(null, new ViewServerProvider());
        archiveHelper.addConnectorType(null, new EgeriaCataloguerIntegrationProvider());
        archiveHelper.addConnectorType(null, new OSSUnityCatalogResourceProvider());
        archiveHelper.addConnectorType(null, new OSSUnityCatalogServerSyncProvider());
        archiveHelper.addConnectorType(null, new OSSUnityCatalogInsideCatalogSyncProvider());

        /*
         * Add catalog templates
         */
        this.addFileTemplates();
        this.addEndpointCatalogTemplates();
        this.addSoftwareServerCatalogTemplates();
        this.addPostgresDatabaseCatalogTemplate();
        this.addPostgresDatabaseSchemaCatalogTemplate();
        this.addUCCatalogCatalogTemplate();
        this.addUCSchemaCatalogTemplate();
        this.addUCVolumeCatalogTemplate();
        this.addUCTableCatalogTemplate();
        this.addUCFunctionCatalogTemplate();
        this.addKafkaTopicCatalogTemplate();
        this.addMacBookProCatalogTemplate();
        this.addFileSystemTemplate();
        this.addUNIXFileSystemTemplate();

        this.addDefaultOMAGServerPlatform();


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

        /*
         * Add the integration connectors to the default integration group
         */
        for (IntegrationConnectorDefinition integrationConnectorDefinition : IntegrationConnectorDefinition.values())
        {
            archiveHelper.setGUID(integrationConnectorDefinition.getQualifiedName(), integrationConnectorDefinition.getGUID());
            String guid = archiveHelper.addIntegrationConnector(integrationConnectorDefinition.getConnectorProviderClassName(),
                                                                integrationConnectorDefinition.getConfigurationProperties(),
                                                                integrationConnectorDefinition.getQualifiedName(),
                                                                integrationConnectorDefinition.getDisplayName(),
                                                                integrationConnectorDefinition.getDescription(),
                                                                integrationConnectorDefinition.getEndpointAddress(),
                                                                null);
            assert(integrationConnectorDefinition.getGUID().equals(guid));

            archiveHelper.addRegisteredIntegrationConnector(integrationGroupGUID,
                                                            integrationConnectorDefinition.getConnectorName(),
                                                            integrationConnectorDefinition.getConnectorUserId(),
                                                            integrationConnectorDefinition.getMetadataSourceQualifiedName(),
                                                            integrationConnectorDefinition.getRefreshTimeInterval(),
                                                            integrationConnectorDefinition.getGUID());

            if (integrationConnectorDefinition.getDeployedImplementationTypes() != null)
            {
                for (DeployedImplementationType deployedImplementationType : integrationConnectorDefinition.getDeployedImplementationTypes())
                {
                    String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

                    archiveHelper.addResourceListRelationshipByGUID(deployedImplementationTypeGUID,
                                                                    integrationConnectorDefinition.getGUID(),
                                                                    integrationConnectorDefinition.getResourceUse().getResourceUse(),
                                                                    integrationConnectorDefinition.getResourceUse().getDescription());
                }
            }
        }


        /*
         * Create the default governance engines
         */
        for (GovernanceEngineDefinition governanceEngineDefinition : GovernanceEngineDefinition.values())
        {
            this.createGovernanceEngine(governanceEngineDefinition);
        }

        /*
         * Register the governance services that are going to be in the default governance engines.
         */
        for (GovernanceServiceDefinition governanceServiceDefinition : GovernanceServiceDefinition.values())
        {
            this.addGovernanceServiceDefinition(governanceServiceDefinition);
        }

        /*
         * Connect the governance engines to the governance services using the request types.
         */
        for (RequestTypeDefinition requestTypeDefinition : RequestTypeDefinition.values())
        {
            this.addRequestType(requestTypeDefinition.getGovernanceEngine().getGUID(),
                                requestTypeDefinition.getGovernanceEngine().getName(),
                                requestTypeDefinition.getGovernanceEngine().getType(),
                                requestTypeDefinition.getGovernanceRequestType(),
                                requestTypeDefinition.getServiceRequestType(),
                                requestTypeDefinition.getRequestParameters(),
                                requestTypeDefinition.getActionTargets(),
                                requestTypeDefinition.getGovernanceService().getGovernanceActionDescription(),
                                requestTypeDefinition.getGovernanceActionTypeGUID(),
                                requestTypeDefinition.getSupportedElementQualifiedName());
        }

        /*
         * Create a sample process
         */
        this.createDailyGovernanceActionProcess();
        this.createAndSurveyUCGovernanceActionProcess();
        this.createAndCatalogUCGovernanceActionProcess();

        /*
         * Saving the GUIDs means tha the guids in the archive are stable between runs of the archive writer.
         */
        archiveHelper.saveGUIDs();
        archiveHelper.saveUsedGUIDs();

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
     * @param enumTypeName          type name for enum
     * @param openMetadataEnums     list of valid values
     */
    private void addOpenMetadataEnumValidNames(String enumConsumingTypeName,
                                               String enumConsumingProperty,
                                               String enumTypeName,
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
                                             OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
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
     * @param openMetadataEnums     list of valid values
     */
    private void addOpenMetadataEnumValidIdentifiers(String enumConsumingTypeName,
                                                     String enumConsumingProperty,
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
                                             OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
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
        createDataFileCatalogTemplate(DeployedImplementationType.AVRO_FILE, basicFileConnectorTypeGUID, null);
        createDataFileCatalogTemplate(DeployedImplementationType.JSON_FILE, basicFileConnectorTypeGUID, null);
        createDataFileCatalogTemplate(DeployedImplementationType.PARQUET_FILE, basicFileConnectorTypeGUID, null);
        createDataFileCatalogTemplate(DeployedImplementationType.SPREADSHEET_FILE, basicFileConnectorTypeGUID, null);
        createDataFileCatalogTemplate(DeployedImplementationType.XML_FILE, basicFileConnectorTypeGUID, null);
        createDataFileCatalogTemplate(DeployedImplementationType.DOCUMENT, basicFileConnectorTypeGUID, null);
        createDataFileCatalogTemplate(DeployedImplementationType.AUDIO_DATA_FILE, basicFileConnectorTypeGUID, null);
        createDataFileCatalogTemplate(DeployedImplementationType.VIDEO_DATA_FILE, basicFileConnectorTypeGUID, null);
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
     * @param connectorTypeGUID          connector type to link to the connection
     * @param deployedImplementationType deployed implementation type to link the template to
     * @param configurationProperties    configuration properties
     */
    private void createDataFileCatalogTemplate(DeployedImplementationType deployedImplementationType,
                                               String                     connectorTypeGUID,
                                               Map<String, Object>        configurationProperties)
    {
        final String methodName = "createDataFileCatalogTemplate";

        String               qualifiedName      = deployedImplementationType.getDeployedImplementationType()  + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + ":" + PlaceholderProperty.FILE_PATH_NAME.getPlaceholder();
        String               versionIdentifier  = "V1.0";
        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications    = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, deployedImplementationType.getDeployedImplementationType());
        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name, PlaceholderProperty.FILE_PATH_NAME.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.PATH_NAME.name, PlaceholderProperty.FILE_PATH_NAME.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FILE_TYPE.name, PlaceholderProperty.FILE_TYPE.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FILE_EXTENSION.name, PlaceholderProperty.FILE_EXTENSION.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FILE_NAME.name, PlaceholderProperty.FILE_NAME.getPlaceholder());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    "Create an asset of type " + deployedImplementationType.getAssociatedTypeName() + " with an associated Connection.",
                                                                    "V1.0",
                                                                    null, methodName));

        classifications.add(archiveHelper.getDataAssetEncodingClassification(PlaceholderProperty.FILE_ENCODING.getPlaceholder(), null, null, null));

        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  PlaceholderProperty.FILE_NAME.getPlaceholder(),
                                                  versionIdentifier,
                                                  null,
                                                  null,
                                                  extendedProperties,
                                                  classifications);

        String endpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                        deployedImplementationType.getAssociatedTypeName(),
                                                        OpenMetadataType.ASSET.typeName,
                                                        qualifiedName + ":Endpoint",
                                                        PlaceholderProperty.FILE_PATH_NAME.getPlaceholder() + " endpoint",
                                                        null,
                                                        PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                                                        null,
                                                        null);

        String connectionGUID = archiveHelper.addConnection(qualifiedName + ":Connection",
                                                            PlaceholderProperty.FILE_PATH_NAME.getPlaceholder() + " connection",
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
                                                            deployedImplementationType.getAssociatedTypeName(),
                                                            OpenMetadataType.ASSET.typeName);

        archiveHelper.addConnectionForAsset(assetGUID, null, connectionGUID);

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               PlaceholderProperty.getDataFilesPlaceholderPropertyTypes());
    }


    /**
     * Create a template for a file directory and link it to the associated open metadata type.
     * The template consists of a DataFile asset plus an optional connection, linked
     * to the supplied connector type and an endpoint,
     *
     * @param deployedImplementationType info for the template
     * @param connectorTypeGUID          connector type to link to the connection
     */
    private void createFolderCatalogTemplate(DeployedImplementationType deployedImplementationType,
                                             String                     connectorTypeGUID)
    {
        final String methodName = "createFolderCatalogTemplate";

        String               qualifiedName      = deployedImplementationType.getDeployedImplementationType() + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + ":" + PlaceholderProperty.DIRECTORY_PATH_NAME.getPlaceholder();
        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications    = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, deployedImplementationType.getDeployedImplementationType());
        extendedProperties.put(OpenMetadataProperty.PATH_NAME.name, PlaceholderProperty.DIRECTORY_PATH_NAME.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name, PlaceholderProperty.DIRECTORY_PATH_NAME.getPlaceholder());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    "Create an asset of type " + deployedImplementationType.getAssociatedTypeName() + " with an associated Connection.",
                                                                    "V1.0",
                                                                    null, methodName));

        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  PlaceholderProperty.DIRECTORY_NAME.getPlaceholder(),
                                                  PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(), 
                                                  PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                                  null,
                                                  extendedProperties,
                                                  classifications);

        if (connectorTypeGUID != null)
        {
            String endpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                            deployedImplementationType.getAssociatedTypeName(),
                                                            OpenMetadataType.ASSET.typeName,
                                                            qualifiedName + ":Endpoint",
                                                            PlaceholderProperty.DIRECTORY_PATH_NAME + " endpoint",
                                                            null,
                                                            PlaceholderProperty.DIRECTORY_PATH_NAME.getPlaceholder(),
                                                            null,
                                                            null);

            String connectionGUID = archiveHelper.addConnection(qualifiedName + ":Connection",
                                                                PlaceholderProperty.DIRECTORY_PATH_NAME.getPlaceholder() + " connection",
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
                                                                deployedImplementationType.getAssociatedTypeName(),
                                                                OpenMetadataType.ASSET.typeName);

            archiveHelper.addConnectionForAsset(assetGUID, null, connectionGUID);
        }

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               PlaceholderProperty.getFolderPlaceholderPropertyTypes());
    }


    /**
     * Create a template for a dataset and link it to the associated open metadata type.
     * The template consists of a DataFile asset plus an optional connection, linked
     * to the supplied connector type and an endpoint,
     *
     * @param deployedImplementationType values for the template
     * @param connectorTypeGUID          connector type to link to the connection
     */
    private void createDataSetCatalogTemplate(DeployedImplementationType deployedImplementationType,
                                              String                     qualifiedName,
                                              String                     connectorTypeGUID)
    {
        final String methodName = "createDataSetCatalogTemplate";

        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications    = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, deployedImplementationType.getDeployedImplementationType());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    "Create an asset of type " + deployedImplementationType.getAssociatedTypeName() + " with an associated Connection.",
                                                                    "V1.0",
                                                                    null, methodName));

        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  PlaceholderProperty.DISPLAY_NAME.getPlaceholder(),
                                                  PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                                  PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                                  null,
                                                  extendedProperties,
                                                  classifications);

        if (connectorTypeGUID != null)
        {
            String connectionGUID = archiveHelper.addConnection(qualifiedName + ":Connection",
                                                                PlaceholderProperty.DISPLAY_NAME.getPlaceholder() + " connection",
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
                                                                deployedImplementationType.getAssociatedTypeName(),
                                                                OpenMetadataType.ASSET.typeName);

            archiveHelper.addConnectionForAsset(assetGUID, null, connectionGUID);
        }

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               PlaceholderProperty.getDataSetPlaceholderPropertyTypes());
    }


    /**
     * Create a template for a software file and link it to the associated open metadata type.
     * The template consists of a DataFile asset plus an optional connection, linked
     * to the supplied connector type and an endpoint,
     *
     * @param deployedImplementationType description for the template
     * @param connectorTypeGUID          connector type to link to the connection
     */
    private void createSoftwareFileCatalogTemplate(DeployedImplementationType deployedImplementationType,
                                                   String                     connectorTypeGUID)
    {
        final String methodName = "createSoftwareFileCatalogTemplate";

        String               qualifiedName      = deployedImplementationType.getDeployedImplementationType() + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + ":" + PlaceholderProperty.FILE_PATH_NAME.getPlaceholder();
        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications    = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, deployedImplementationType.getDeployedImplementationType());
        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name, PlaceholderProperty.FILE_PATH_NAME.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.PATH_NAME.name, PlaceholderProperty.FILE_PATH_NAME.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FILE_TYPE.name, PlaceholderProperty.FILE_TYPE.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FILE_EXTENSION.name, PlaceholderProperty.FILE_EXTENSION.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FILE_NAME.name, PlaceholderProperty.FILE_NAME.getPlaceholder());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    "Create an asset of type " + deployedImplementationType.getAssociatedTypeName() + " with an associated Connection.",
                                                                    "V1.0",
                                                                    null, methodName));

        classifications.add(archiveHelper.getDataAssetEncodingClassification(PlaceholderProperty.FILE_ENCODING.getPlaceholder(),
                                                                             PlaceholderProperty.PROGRAMMING_LANGUAGE.getPlaceholder(),
                                                                             null,
                                                                             null));

        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  PlaceholderProperty.FILE_NAME.getPlaceholder(),
                                                  PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(), 
                                                  PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                                  null,
                                                  extendedProperties,
                                                  classifications);

        if (connectorTypeGUID != null)
        {
            String endpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                            deployedImplementationType.getAssociatedTypeName(),
                                                            OpenMetadataType.ASSET.typeName,
                                                            qualifiedName + ":Endpoint",
                                                            PlaceholderProperty.FILE_PATH_NAME.getPlaceholder() + " endpoint",
                                                            null,
                                                            PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                                                            null,
                                                            null);

            String connectionGUID = archiveHelper.addConnection(qualifiedName + ":Connection",
                                                                PlaceholderProperty.FILE_PATH_NAME.getPlaceholder() + " connection",
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
                                                                deployedImplementationType.getAssociatedTypeName(),
                                                                OpenMetadataType.ASSET.typeName);

            archiveHelper.addConnectionForAsset(assetGUID, null, connectionGUID);
        }

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               PlaceholderProperty.getSoftwareFilesPlaceholderPropertyTypes());
    }


    /**
     * Loop through the server template definitions creating the specified templates.
     */
    private void addSoftwareServerCatalogTemplates()
    {
        for (SoftwareServerTemplateDefinition templateDefinition : SoftwareServerTemplateDefinition.values())
        {
            createSoftwareServerCatalogTemplate(templateDefinition.getTemplateGUID(),
                                                templateDefinition.getTemplateName(),
                                                templateDefinition.getTemplateDescription(),
                                                templateDefinition.getTemplateVersionIdentifier(),
                                                templateDefinition.getDeployedImplementationType(),
                                                templateDefinition.getSoftwareCapabilityType(),
                                                templateDefinition.getSoftwareCapabilityName(),
                                                templateDefinition.getServerName(),
                                                templateDefinition.getServerDescription(),
                                                templateDefinition.getUserId(),
                                                templateDefinition.getPassword(),
                                                templateDefinition.getConnectorTypeGUID(),
                                                templateDefinition.getNetworkAddress(),
                                                templateDefinition.getConfigurationProperties(),
                                                templateDefinition.getReplacementAttributes(),
                                                templateDefinition.getPlaceholders());
        }
    }


    /**
     * Create a template for a software server and link it to the associated deployed implementation type.
     * The template consists of a SoftwareServer asset linked to a software capability, plus a connection, linked
     * to the supplied connector type and an endpoint,
     *
     * @param guid                             fixed unique identifier
     * @param templateName                     name of template in Template classification
     * @param templateDescription              description of the template in the Template classification
     * @param templateVersion                  version of the template in the Template classification
     * @param deployedImplementationType       deployed implementation type for the technology
     * @param softwareCapabilityType           type of the associated capability
     * @param softwareCapabilityName           name for the associated capability
     * @param serverName                       name for the server
     * @param description                      description for the server
     * @param userId                           userId for the connection
     * @param password                         password for the connection
     * @param connectorTypeGUID                connector type to link to the connection
     * @param networkAddress                   network address for the endpoint
     * @param configurationProperties          additional properties for the connection
     * @param replacementAttributeTypes        attributes that should have a replacement value to successfully use the template
     * @param placeholderPropertyTypes         placeholder variables used in the supplied parameters
     */
    private void createSoftwareServerCatalogTemplate(String                         guid,
                                                     String                         templateName,
                                                     String                         templateDescription,
                                                     String                         templateVersion,
                                                     DeployedImplementationType     deployedImplementationType,
                                                     DeployedImplementationType     softwareCapabilityType,
                                                     String                         softwareCapabilityName,
                                                     String                         serverName,
                                                     String                         description,
                                                     String                         userId,
                                                     String                         password,
                                                     String                         connectorTypeGUID,
                                                     String                         networkAddress,
                                                     Map<String, Object>            configurationProperties,
                                                     List<ReplacementAttributeType> replacementAttributeTypes,
                                                     List<PlaceholderPropertyType>  placeholderPropertyTypes)
    {
        final String methodName = "createSoftwareServerCatalogTemplate";

        String               qualifiedName      = deployedImplementationType.getDeployedImplementationType() + ":" + serverName;
        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications    = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                               deployedImplementationType.getDeployedImplementationType());
        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name,
                               serverName);

        if (deployedImplementationType.getAssociatedClassification() != null)
        {
            classifications.add(archiveHelper.getServerPurposeClassification(deployedImplementationType.getAssociatedClassification(), null));
        }

        classifications.add(archiveHelper.getTemplateClassification(templateName,
                                                                    templateDescription,
                                                                    templateVersion,
                                                                    null,
                                                                    methodName));

        archiveHelper.setGUID(qualifiedName, guid);
        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  serverName,
                                                  PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(), 
                                                  description,
                                                  null,
                                                  extendedProperties,
                                                  classifications);
        assert(guid.equals(assetGUID));

        if (softwareCapabilityType != null)
        {
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
                                                (Classification) null,
                                                assetGUID,
                                                deployedImplementationType.getAssociatedTypeName(),
                                                OpenMetadataType.ASSET.typeName);

            archiveHelper.addSupportedSoftwareCapabilityRelationship(qualifiedName + ":" + softwareCapabilityName,
                                                                     qualifiedName,
                                                                     null,
                                                                     null,
                                                                     null,
                                                                     null,
                                                                     1);
        }

        String endpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                        deployedImplementationType.getAssociatedTypeName(),
                                                        OpenMetadataType.ASSET.typeName,
                                                        qualifiedName + ":Endpoint",
                                                        serverName + " endpoint",
                                                        null,
                                                        networkAddress,
                                                        null,
                                                        null);

        archiveHelper.addServerEndpointRelationship(assetGUID, endpointGUID);

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
                                                            deployedImplementationType.getAssociatedTypeName(),
                                                            OpenMetadataType.ASSET.typeName);

        archiveHelper.addConnectionForAsset(assetGUID, null, connectionGUID);

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addReplacementAttributes(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               replacementAttributeTypes);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               placeholderPropertyTypes);
    }



    /**
     * Loop through the server template definitions creating the specified templates.
     */
    private void addEndpointCatalogTemplates()
    {
        for (EndpointTemplateDefinition templateDefinition : EndpointTemplateDefinition.values())
        {
            createEndpointCatalogTemplate(templateDefinition.getTemplateGUID(),
                                                templateDefinition.getTemplateName(),
                                                templateDefinition.getTemplateDescription(),
                                                templateDefinition.getTemplateVersionIdentifier(),
                                                templateDefinition.getDeployedImplementationType(),
                                                templateDefinition.getServerName(),
                                                templateDefinition.getEndpointDescription(),
                                                templateDefinition.getNetworkAddress(),
                                                templateDefinition.getProtocol(),
                                                templateDefinition.getPlaceholders());
        }
    }




    /**
     * Create a template for a software server and link it to the associated deployed implementation type.
     * The template consists of a SoftwareServer asset linked to a software capability, plus a connection, linked
     * to the supplied connector type and an endpoint,
     *
     * @param guid                             fixed unique identifier
     * @param templateName                     name of template in Template classification
     * @param templateDescription              description of the template in the Template classification
     * @param templateVersion                  version of the template in the Template classification
     * @param serverName                       name for the server
     * @param description                      description for the server
     * @param networkAddress                   network address for the endpoint
     * @param protocol                         communication protocol for the endpoint
     * @param placeholderPropertyTypes         placeholder variables used in the supplied parameters
     */
    private void createEndpointCatalogTemplate(String                         guid,
                                               String                         templateName,
                                               String                         templateDescription,
                                               String                         templateVersion,
                                               DeployedImplementationType     deployedImplementationType,
                                               String                         serverName,
                                               String                         description,
                                               String                         networkAddress,
                                               String                         protocol,
                                               List<PlaceholderPropertyType>  placeholderPropertyTypes)
    {
        final String methodName = "createEndpointCatalogTemplate";

        String         qualifiedName            = deployedImplementationType.getDeployedImplementationType() + ":" + serverName;
        Classification templateClassification   = archiveHelper.getTemplateClassification(templateName,
                                                                                          templateDescription,
                                                                                          templateVersion,
                                                                                          null,
                                                                                          methodName);

        archiveHelper.setGUID(qualifiedName, guid);

        String endpointGUID = archiveHelper.addEndpoint(null,
                                                        OpenMetadataType.ENDPOINT.typeName,
                                                        OpenMetadataType.ENDPOINT.typeName,
                                                        qualifiedName,
                                                        serverName + " endpoint",
                                                        description,
                                                        networkAddress,
                                                        protocol,
                                                        null,
                                                        templateClassification);

        assert(guid.equals(endpointGUID));

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, endpointGUID);

        archiveHelper.addPlaceholderProperties(endpointGUID,
                                               OpenMetadataType.ENDPOINT.typeName,
                                               OpenMetadataType.ENDPOINT.typeName,
                                               placeholderPropertyTypes);
    }



    /**
     * This entry is used by Runtime Manager to display the platform report for 9443
     */
    private void addDefaultOMAGServerPlatform()
    {
        final String guid = "44bf319f-1e41-4da1-b771-2753b92b631a";
        OMAGServerPlatformProvider provider = new OMAGServerPlatformProvider();

        DeployedImplementationType deployedImplementationType = DeployedImplementationType.OMAG_SERVER_PLATFORM;
        DeployedImplementationType softwareCapabilityType = DeployedImplementationType.USER_AUTHENTICATION_MANAGER;
        String softwareCapabilityName = "User Token Manager";
        String serverName = "Default Local OMAG Server Platform";
        String userId = "garygeeke";
        String connectorTypeGUID = provider.getConnectorType().getGUID();
        String networkAddress = "https://localhost:9443";

        String               qualifiedName      = deployedImplementationType.getDeployedImplementationType() + ":" + serverName;
        String               versionIdentifier  = "V1.0";
        String               description        = "Default OMAG Server Platform running on local host and port 9443.";
        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications    = null;

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                               deployedImplementationType.getDeployedImplementationType());

        if (deployedImplementationType.getAssociatedClassification() != null)
        {
            classifications    = new ArrayList<>();
            classifications.add(archiveHelper.getServerPurposeClassification(deployedImplementationType.getAssociatedClassification(), null));
        }

        archiveHelper.setGUID(qualifiedName, guid);
        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  serverName,
                                                  versionIdentifier,
                                                  description,
                                                  null,
                                                  extendedProperties,
                                                  classifications);
        assert(guid.equals(assetGUID));

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
                                            (Classification)null,
                                            assetGUID,
                                            deployedImplementationType.getAssociatedTypeName(),
                                            OpenMetadataType.ASSET.typeName);

        archiveHelper.addSupportedSoftwareCapabilityRelationship(qualifiedName + ":" + softwareCapabilityName,
                                                                 qualifiedName,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 1);

        String endpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                        deployedImplementationType.getAssociatedTypeName(),
                                                        OpenMetadataType.ASSET.typeName,
                                                        qualifiedName + ":Endpoint",
                                                        serverName + " endpoint",
                                                        null,
                                                        networkAddress,
                                                        null,
                                                        null);

        archiveHelper.addServerEndpointRelationship(assetGUID, endpointGUID);

        String connectionGUID = archiveHelper.addConnection(qualifiedName + ":Connection",
                                                            serverName + " connection",
                                                            null,
                                                            userId,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            connectorTypeGUID,
                                                            endpointGUID,
                                                            assetGUID,
                                                            deployedImplementationType.getAssociatedTypeName(),
                                                            OpenMetadataType.ASSET.typeName);

        archiveHelper.addConnectionForAsset(assetGUID, null, connectionGUID);
    }


    /**
     * Create a template for a host and link it to the associated deployed implementation type.
     * The template consists of a SoftwareServer asset linked to a software capability, plus a connection, linked
     * to the supplied connector type and an endpoint,
     *
     * @param guid                             fixed unique identifier
     * @param deployedImplementationType       deployed implementation type for the technology
     * @param softwareCapabilityType           type of the associated capability
     * @param softwareCapabilityName           name for the associated capability
     * @param softwareCapabilityClassification classification for the software capability (or null)
     */
    private void createHostCatalogTemplate(String                         guid,
                                           DeployedImplementationType     deployedImplementationType,
                                           DeployedImplementationType     softwareCapabilityType,
                                           String                         softwareCapabilityName,
                                           Classification                 softwareCapabilityClassification)
    {
        final String methodName = "createHostCatalogTemplate";

        String               qualifiedName      = deployedImplementationType.getDeployedImplementationType() + ":" + PlaceholderProperty.HOST_URL.getPlaceholder();
        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications    = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                               deployedImplementationType.getDeployedImplementationType());
        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name,
                               PlaceholderProperty.HOST_URL.getPlaceholder());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    "Create a " + deployedImplementationType.getDeployedImplementationType() + " Host with an associated SoftwareCapability.",
                                                                    "V1.0",
                                                                    null, methodName));

        archiveHelper.setGUID(qualifiedName, guid);
        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  PlaceholderProperty.DISPLAY_NAME.getPlaceholder(),
                                                  PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                                  PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                                  null,
                                                  extendedProperties,
                                                  classifications);
        assert(guid.equals(assetGUID));

        if (softwareCapabilityType != null)
        {
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
                                                softwareCapabilityClassification,
                                                assetGUID,
                                                deployedImplementationType.getAssociatedTypeName(),
                                                OpenMetadataType.ASSET.typeName);

            archiveHelper.addSupportedSoftwareCapabilityRelationship(qualifiedName + ":" + softwareCapabilityName,
                                                                     qualifiedName,
                                                                     null,
                                                                     null,
                                                                     null,
                                                                     null,
                                                                     1);
        }

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               PlaceholderProperty.getHostPlaceholderPropertyTypes());
    }


    private void addMacBookProCatalogTemplate()
    {
        final String methodName = "addMacBookProCatalogTemplate";
        final String guid       = "32a9fd56-85c9-47fe-a211-9da3871bf4da";

        Classification classification = archiveHelper.getFileSystemClassification("APFS", "Enabled", methodName);

        createHostCatalogTemplate(guid,
                                  DeployedImplementationType.MACBOOK_PRO,
                                  DeployedImplementationType.UNIX_FILE_SYSTEM,
                                  "Local File System",
                                  classification);
    }


    private void addUCCatalogCatalogTemplate()
    {
        final String methodName = "addUCCatalogCatalogTemplate";
        final String guid       = UnityCatalogTemplateType.UC_CATALOG_TEMPLATE.getDefaultTemplateGUID();

        DeployedImplementationType deployedImplementationType = DeployedImplementationType.OSS_UC_CATALOG;

        String               qualifiedName      = deployedImplementationType.getDeployedImplementationType() + ":" + PlaceholderProperty.SERVER_NETWORK_ADDRESS.getPlaceholder() + ":" + UnityCatalogPlaceholderProperty.CATALOG_NAME.getPlaceholder();
        List<Classification> classifications    = new ArrayList<>();

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    UnityCatalogTemplateType.UC_CATALOG_TEMPLATE.getTemplateDescription(),
                                                                    "V1.0",
                                                                    null, methodName));

        archiveHelper.setGUID(qualifiedName, guid);
        String capabilityGUID = archiveHelper.addSoftwareCapability(deployedImplementationType.getAssociatedTypeName(),
                                                                    qualifiedName,
                                                                    UnityCatalogPlaceholderProperty.CATALOG_NAME.getPlaceholder(),
                                                                    PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                                                    deployedImplementationType.getDeployedImplementationType(),
                                                                    PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    classifications,
                                                                    null,
                                                                    deployedImplementationType.getAssociatedTypeName(),
                                                                    OpenMetadataType.SOFTWARE_CAPABILITY.typeName);
        assert(guid.equals(capabilityGUID));

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, capabilityGUID);

        archiveHelper.addPlaceholderProperties(capabilityGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                               UnityCatalogPlaceholderProperty.getCatalogPlaceholderPropertyTypes());
    }


    private void addUCSchemaCatalogTemplate()
    {
        final String methodName = "addUCSchemaCatalogTemplate";
        final String guid       = UnityCatalogTemplateType.UC_SCHEMA_TEMPLATE.getDefaultTemplateGUID();

        DeployedImplementationType deployedImplementationType = DeployedImplementationType.OSS_UC_SCHEMA;
        String                     fullName                   = UnityCatalogPlaceholderProperty.CATALOG_NAME.getPlaceholder() + "."
                                                              + UnityCatalogPlaceholderProperty.SCHEMA_NAME.getPlaceholder();
        String                     qualifiedName              = deployedImplementationType.getDeployedImplementationType() + ":"
                                                              + PlaceholderProperty.SERVER_NETWORK_ADDRESS.getPlaceholder() + ":"
                                                              + fullName;

        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications    = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, deployedImplementationType.getDeployedImplementationType());
        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name, fullName);

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    UnityCatalogTemplateType.UC_SCHEMA_TEMPLATE.getTemplateDescription(),
                                                                    "V1.0",
                                                                    null,
                                                                    methodName));

        archiveHelper.setGUID(qualifiedName, guid);
        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  UnityCatalogPlaceholderProperty.SCHEMA_NAME.getPlaceholder(),
                                                  PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                                  PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                                  null,
                                                  extendedProperties,
                                                  classifications);
        assert(guid.equals(assetGUID));

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               UnityCatalogPlaceholderProperty.getSchemaPlaceholderPropertyTypes());
    }


    private void addUCVolumeCatalogTemplate()
    {
        final String methodName = "addUCVolumeCatalogTemplate";
        final String guid       = UnityCatalogTemplateType.UC_VOLUME_TEMPLATE.getDefaultTemplateGUID();

        DeployedImplementationType deployedImplementationType = DeployedImplementationType.OSS_UC_VOLUME;
        String                     fullName                   = UnityCatalogPlaceholderProperty.CATALOG_NAME.getPlaceholder() + "."
                                                              + UnityCatalogPlaceholderProperty.SCHEMA_NAME.getPlaceholder() + "."
                                                              + UnityCatalogPlaceholderProperty.VOLUME_NAME.getPlaceholder();
        String                     qualifiedName              = deployedImplementationType.getDeployedImplementationType() + ":"
                                                              + PlaceholderProperty.SERVER_NETWORK_ADDRESS.getPlaceholder() + ":"
                                                              + fullName;
        String connectorTypeGUID = new DataFolderProvider().getConnectorType().getGUID();

        Map<String, Object>  extendedProperties   = new HashMap<>();
        Map<String, String>  facetProperties      = new HashMap<>();
        List<Classification> classifications      = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, deployedImplementationType.getDeployedImplementationType());
        extendedProperties.put(OpenMetadataProperty.PATH_NAME.name, UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name, fullName);

        facetProperties.put(UnityCatalogPlaceholderProperty.VOLUME_TYPE.getName(), UnityCatalogPlaceholderProperty.VOLUME_TYPE.getPlaceholder());
        facetProperties.put(UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getName(), UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getPlaceholder());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    UnityCatalogTemplateType.UC_VOLUME_TEMPLATE.getTemplateDescription(),
                                                                    "V1.0",
                                                                    null,
                                                                    methodName));

        archiveHelper.setGUID(qualifiedName, guid);
        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  UnityCatalogPlaceholderProperty.VOLUME_NAME.getPlaceholder(),
                                                  PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                                  PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                                  null,
                                                  extendedProperties,
                                                  classifications);
        assert(guid.equals(assetGUID));

        archiveHelper.addPropertyFacet(assetGUID,
                                       deployedImplementationType.getAssociatedTypeName(),
                                       OpenMetadataType.ASSET.typeName,
                                       qualifiedName,
                                       PropertyFacetValidValues.UNITY_CATALOG_SOURCE_VALUE,
                                       PropertyFacetValidValues.UNITY_CATALOG_SCHEMA_VERSION_VALUE,
                                       PropertyFacetValidValues.VENDOR_PROPERTIES_DESCRIPTION_VALUE,
                                       facetProperties);

        String endpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                        deployedImplementationType.getAssociatedTypeName(),
                                                        OpenMetadataType.ASSET.typeName,
                                                        qualifiedName + ":Endpoint",
                                                        fullName + " endpoint",
                                                        null,
                                                        UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getPlaceholder(),
                                                        null,
                                                        null);

        String connectionGUID = archiveHelper.addConnection(qualifiedName + ":Connection",
                                                            fullName + " connection",
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
                                                            deployedImplementationType.getAssociatedTypeName(),
                                                            OpenMetadataType.ASSET.typeName);

        archiveHelper.addConnectionForAsset(assetGUID, null, connectionGUID);

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               UnityCatalogPlaceholderProperty.getVolumePlaceholderPropertyTypes());
    }


    private void addUCTableCatalogTemplate()
    {
        final String methodName = "addUCTableCatalogTemplate";
        final String guid       = UnityCatalogTemplateType.UC_TABLE_TEMPLATE.getDefaultTemplateGUID();

        DeployedImplementationType deployedImplementationType = DeployedImplementationType.OSS_UC_TABLE;
        String                     fullName                   = UnityCatalogPlaceholderProperty.CATALOG_NAME.getPlaceholder() + "."
                                                              + UnityCatalogPlaceholderProperty.SCHEMA_NAME.getPlaceholder() + "."
                                                              + UnityCatalogPlaceholderProperty.TABLE_NAME.getPlaceholder();
        String                     qualifiedName              = deployedImplementationType.getDeployedImplementationType() + ":"
                                                              + PlaceholderProperty.SERVER_NETWORK_ADDRESS.getPlaceholder() + ":"
                                                              + fullName;

        Map<String, Object>  extendedProperties   = new HashMap<>();
        Map<String, Object>  folderProperties     = new HashMap<>();
        Map<String, String>  facetProperties      = new HashMap<>();
        List<Classification> classifications      = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, deployedImplementationType.getDeployedImplementationType());
        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name, fullName);

        folderProperties.put(OpenMetadataProperty.PATH_NAME.name, UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getPlaceholder());

        facetProperties.put(UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getName(), UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getPlaceholder());
        facetProperties.put(UnityCatalogPlaceholderProperty.TABLE_TYPE.getName(), UnityCatalogPlaceholderProperty.TABLE_TYPE.getPlaceholder());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    UnityCatalogTemplateType.UC_TABLE_TEMPLATE.getTemplateDescription(),
                                                                    "V1.0",
                                                                    null,
                                                                    methodName));

        classifications.add(archiveHelper.getDataAssetEncodingClassification(UnityCatalogPlaceholderProperty.DATA_SOURCE_FORMAT.getPlaceholder(),
                                                                             null,
                                                                             null,
                                                                             null));

        archiveHelper.setGUID(qualifiedName, guid);
        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  UnityCatalogPlaceholderProperty.TABLE_NAME.getPlaceholder(),
                                                  PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                                  PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                                  null,
                                                  extendedProperties,
                                                  classifications);
        assert(guid.equals(assetGUID));

        extendedProperties   = new HashMap<>();
        extendedProperties.put("file:///" + OpenMetadataProperty.RESOURCE_NAME.name, UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getPlaceholder());
        String folderGUID = archiveHelper.addAnchoredAsset(OpenMetadataType.DATA_FOLDER.typeName,
                                                           assetGUID,
                                                           deployedImplementationType.getAssociatedTypeName(),
                                                           OpenMetadataType.ASSET.typeName,
                                                           qualifiedName + "_storageLocation",
                                                           fullName + "_storageLocation",
                                                           PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                                           "Location of files for table " + fullName,
                                                           null,
                                                           folderProperties,
                                                           classifications);

        archiveHelper.addDataContentForDataSet(folderGUID, assetGUID);

        archiveHelper.addPropertyFacet(assetGUID,
                                       deployedImplementationType.getAssociatedTypeName(),
                                       OpenMetadataType.ASSET.typeName,
                                       qualifiedName,
                                       PropertyFacetValidValues.UNITY_CATALOG_SOURCE_VALUE,
                                       PropertyFacetValidValues.UNITY_CATALOG_SCHEMA_VERSION_VALUE,
                                       PropertyFacetValidValues.VENDOR_PROPERTIES_DESCRIPTION_VALUE,
                                       facetProperties);

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               UnityCatalogPlaceholderProperty.getTablePlaceholderPropertyTypes());
    }


    private void addUCFunctionCatalogTemplate()
    {
        final String methodName = "addUCFunctionCatalogTemplate";
        final String guid       = UnityCatalogTemplateType.UC_FUNCTION_TEMPLATE.getDefaultTemplateGUID();

        DeployedImplementationType deployedImplementationType = DeployedImplementationType.OSS_UC_FUNCTION;
        String                     fullName                   = UnityCatalogPlaceholderProperty.CATALOG_NAME.getPlaceholder() + "."
                                                              + UnityCatalogPlaceholderProperty.SCHEMA_NAME.getPlaceholder() + "."
                                                              + UnityCatalogPlaceholderProperty.FUNCTION_NAME.getPlaceholder();
        String                     qualifiedName              = deployedImplementationType.getDeployedImplementationType() + ":"
                                                              + PlaceholderProperty.SERVER_NETWORK_ADDRESS.getPlaceholder() + ":"
                                                              + fullName;

        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications    = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, deployedImplementationType.getDeployedImplementationType());
        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name, fullName);

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    UnityCatalogTemplateType.UC_FUNCTION_TEMPLATE.getTemplateDescription(),
                                                                    "V1.0",
                                                                    null,
                                                                    methodName));

        archiveHelper.setGUID(qualifiedName, guid);
        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  UnityCatalogPlaceholderProperty.FUNCTION_NAME.getPlaceholder(),
                                                  PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                                  PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                                  null,
                                                  extendedProperties,
                                                  classifications);
        assert(guid.equals(assetGUID));

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               UnityCatalogPlaceholderProperty.getFunctionPlaceholderPropertyTypes());
    }


    /**
     * Create a template for a software server and link it to the associated deployed implementation type.
     * The template consists of a SoftwareServer asset linked to a software capability, plus a connection, linked
     * to the supplied connector type and an endpoint,
     *
     * @param guid fixed guid for this template
     * @param deployedImplementationType deployed implementation type for the technology
     * @param serverQualifiedName qualified name of the owning server
     * @param softwareCapabilityName name for the associated capability
     * @param softwareCapabilityDescription description for the software capability
     * @param softwareCapabilityClassification optional classification for the associated capability
     * @param replacementAttributeTypes attributes that should have a replacement value to successfully use the template
     * @param placeholderPropertyTypes placeholder variables used in the supplied parameters
     */
    private   void createSoftwareCapabilityCatalogTemplate(String                         guid,
                                                           DeployedImplementationType     deployedImplementationType,
                                                           String                         serverQualifiedName,
                                                           String                         softwareCapabilityName,
                                                           String                         softwareCapabilityDescription,
                                                           Classification                 softwareCapabilityClassification,
                                                           List<ReplacementAttributeType> replacementAttributeTypes,
                                                           List<PlaceholderPropertyType>  placeholderPropertyTypes)
    {
        final String methodName = "createSoftwareCapabilityCatalogTemplate";

        String               qualifiedName      = deployedImplementationType.getAssociatedTypeName() + ":" +deployedImplementationType.getDeployedImplementationType() + ":" + serverQualifiedName + ":" + softwareCapabilityName;
        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications    = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                               deployedImplementationType.getDeployedImplementationType());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    "Create a " + deployedImplementationType.getDeployedImplementationType() + " SoftwareCapability.",
                                                                    "V1.0",
                                                                    null, methodName));

        if (softwareCapabilityClassification != null)
        {
            classifications.add(softwareCapabilityClassification);
        }

        archiveHelper.setGUID(qualifiedName, guid);
        String capabilityGUID = archiveHelper.addSoftwareCapability(deployedImplementationType.getAssociatedTypeName(),
                                                                    qualifiedName,
                                                                    softwareCapabilityName,
                                                                    softwareCapabilityDescription,
                                                                    deployedImplementationType.getDeployedImplementationType(),
                                                                    PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(), 
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    extendedProperties,
                                                                    classifications,
                                                                    null,
                                                                    deployedImplementationType.getAssociatedTypeName(),
                                                                    OpenMetadataType.SOFTWARE_CAPABILITY.typeName);
        assert(guid.equals(capabilityGUID));

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, capabilityGUID);

        archiveHelper.addReplacementAttributes(capabilityGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                               replacementAttributeTypes);

        archiveHelper.addPlaceholderProperties(capabilityGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                               placeholderPropertyTypes);
    }

    private void addFileSystemTemplate()
    {
        final String methodName = "addFileSystemTemplate";
        final String guid       = "522f228c-097c-4f90-9efc-26c1f2696f87";

        Classification fileSystemClassification = archiveHelper.getFileSystemClassification(PlaceholderProperty.FORMAT.getPlaceholder(),
                                                                                            PlaceholderProperty.ENCRYPTION.getPlaceholder(),
                                                                                            methodName);

        createSoftwareCapabilityCatalogTemplate(guid,
                                                DeployedImplementationType.FILE_SYSTEM,
                                                null,
                                                PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder(),
                                                PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                                fileSystemClassification,
                                                null,
                                                PlaceholderProperty.getFileSystemPlaceholderPropertyTypes());
    }


    private void addUNIXFileSystemTemplate()
    {
        final String methodName = "addUNIXFileSystemTemplate";
        final String guid = "27117270-8667-41d0-a99a-9118f9b60199";

        Classification fileSystemClassification = archiveHelper.getFileSystemClassification(PlaceholderProperty.FORMAT.getPlaceholder(),
                                                                                            PlaceholderProperty.ENCRYPTION.getPlaceholder(),
                                                                                            methodName);

        createSoftwareCapabilityCatalogTemplate(guid,
                                                DeployedImplementationType.UNIX_FILE_SYSTEM,
                                                null,
                                                PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder(),
                                                PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                                fileSystemClassification,
                                                null,
                                                PlaceholderProperty.getFileSystemPlaceholderPropertyTypes());
    }


    /**
     * Create a template for a type of asset and link it to the associated deployed implementation type.
     * The template consists of an asset linked to a connection, that is in turn linked
     * to the supplied connector type and an endpoint,
     *
     * @param guid fixed unique identifier
     * @param deployedImplementationType deployed implementation type for the technology
     * @param assetName name for the asset
     * @param assetDescription description
     * @param serverName optional server name
     * @param userId userId for the connection
     * @param password password for the connection
     * @param connectorTypeGUID connector type to link to the connection
     * @param networkAddress network address for the endpoint
     * @param configurationProperties  additional properties for the connection
     * @param replacementAttributeTypes attributes that should have a replacement value to successfully use the template
     * @param placeholderPropertyTypes placeholder variables used in the supplied parameters
     */
    private   void createDataAssetCatalogTemplate(String                         guid,
                                                  DeployedImplementationType     deployedImplementationType,
                                                  String                         assetName,
                                                  String                         assetDescription,
                                                  String                         serverName,
                                                  String                         userId,
                                                  String                         password,
                                                  String                         connectorTypeGUID,
                                                  String                         networkAddress,
                                                  Map<String, Object>            configurationProperties,
                                                  List<ReplacementAttributeType> replacementAttributeTypes,
                                                  List<PlaceholderPropertyType>  placeholderPropertyTypes)
    {
        final String methodName = "createDataAssetCatalogTemplate";

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
        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                               deployedImplementationType.getDeployedImplementationType());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    null, "V1.0", null, methodName));

        archiveHelper.setGUID(qualifiedName, guid);
        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  assetName,
                                                  versionIdentifier,
                                                  assetDescription,
                                                  null,
                                                  extendedProperties,
                                                  classifications);
        assert(guid.equals(assetGUID));

        String endpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                        deployedImplementationType.getAssociatedTypeName(),
                                                        OpenMetadataType.ASSET.typeName,
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
                                                            deployedImplementationType.getAssociatedTypeName(),
                                                            OpenMetadataType.ASSET.typeName);

        archiveHelper.addConnectionForAsset(assetGUID, null, connectionGUID);

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addReplacementAttributes(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               replacementAttributeTypes);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               placeholderPropertyTypes);
    }


    /**
     * Create a catalog template for a PostgreSQL database
     */
    private void addPostgresDatabaseCatalogTemplate()
    {
        final String guid = "3d398b3f-7ae6-4713-952a-409f3dea8520";
        JDBCResourceConnectorProvider provider = new JDBCResourceConnectorProvider();

        List<PlaceholderPropertyType>  placeholderPropertyTypes = PostgresPlaceholderProperty.getPostgresDatabasePlaceholderPropertyTypes();

        this.createDataAssetCatalogTemplate(guid,
                                            DeployedImplementationType.POSTGRESQL_DATABASE,
                                            PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholder(),
                                            PostgresPlaceholderProperty.DATABASE_DESCRIPTION.getPlaceholder(),
                                            PlaceholderProperty.SERVER_NAME.getPlaceholder(),
                                            PostgresPlaceholderProperty.DATABASE_USER_ID.getPlaceholder(),
                                            PostgresPlaceholderProperty.DATABASE_PASSWORD.getPlaceholder(),
                                            provider.getConnectorType().getGUID(),
                                            "jdbc:postgresql://" +
                                                         PlaceholderProperty.HOST_IDENTIFIER.getPlaceholder() + ":" +
                                                         PlaceholderProperty.PORT_NUMBER.getPlaceholder() + "/" + PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholder(),
                                            null,
                                            null,
                                            placeholderPropertyTypes);
    }


    /**
     * Create a catalog template for a PostgreSQL database schema
     */
    private void addPostgresDatabaseSchemaCatalogTemplate()
    {
        final String guid = "82a5417c-d882-4271-8444-4c6a996a8bfc";

        JDBCResourceConnectorProvider provider = new JDBCResourceConnectorProvider();

        List<PlaceholderPropertyType>  placeholderPropertyTypes = PostgresPlaceholderProperty.getPostgresSchemaPlaceholderPropertyTypes();

        this.createDataAssetCatalogTemplate(guid,
                                            DeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA,
                                            PostgresPlaceholderProperty.SCHEMA_NAME.getPlaceholder(),
                                            PostgresPlaceholderProperty.SCHEMA_DESCRIPTION.getPlaceholder(),
                                            PlaceholderProperty.SERVER_NAME.getPlaceholder() + "." + PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholder(),
                                            PostgresPlaceholderProperty.DATABASE_USER_ID.getPlaceholder(),
                                            PostgresPlaceholderProperty.DATABASE_PASSWORD.getPlaceholder(),
                                            provider.getConnectorType().getGUID(),
                                            "jdbc:postgresql://" +
                                                      PlaceholderProperty.HOST_IDENTIFIER.getPlaceholder() + ":" +
                                                      PlaceholderProperty.PORT_NUMBER.getPlaceholder() + "/" +
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
        final String guid = "ea8f81c9-c59c-47de-9525-7cc59d1251e5";

        KafkaOpenMetadataTopicProvider provider = new KafkaOpenMetadataTopicProvider();

        List<PlaceholderPropertyType>  placeholderPropertyTypes = KafkaPlaceholderProperty.getKafkaTopicPlaceholderPropertyTypes();

        Map<String, Object> configurationProperties = new HashMap<>();
        Map<String, String> bootstrapServersProperties = new HashMap<>();

        bootstrapServersProperties.put("bootstrap.servers",
                                       PlaceholderProperty.HOST_IDENTIFIER.getPlaceholder() + ":" +
                                               PlaceholderProperty.PORT_NUMBER.getPlaceholder());

        configurationProperties.put(KafkaPlaceholderProperty.EVENT_DIRECTION.getName(), "inOut");
        configurationProperties.put("producer", bootstrapServersProperties);
        configurationProperties.put("consumer", bootstrapServersProperties);

        this.createDataAssetCatalogTemplate(guid,
                                            DeployedImplementationType.APACHE_KAFKA_TOPIC,
                                            KafkaPlaceholderProperty.SHORT_TOPIC_NAME.getPlaceholder(),
                                            PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                            PlaceholderProperty.SERVER_NAME.getPlaceholder() + "." + KafkaPlaceholderProperty.FULL_TOPIC_NAME.getPlaceholder() + ":inOut",
                                            null,
                                            null,
                                            provider.getConnectorType().getGUID(),
                                            KafkaPlaceholderProperty.FULL_TOPIC_NAME.getPlaceholder(),
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
                                                                 OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
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
                                                                                   OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
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
                                                                 OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
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
                                                                                   OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
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
                                         OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
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
                                         OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
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
                                         OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
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
                                         OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
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
                                                         OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
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
     * Create an entity that represents a governance engine.
     *
     * @param governanceEngineDefinition details of the governance engine
     */
    private void createGovernanceEngine(GovernanceEngineDefinition governanceEngineDefinition)
    {
        archiveHelper.setGUID(governanceEngineDefinition.getName(),
                              governanceEngineDefinition.getGUID());

        archiveHelper.addGovernanceEngine(governanceEngineDefinition.getType(),
                                          governanceEngineDefinition.getName(),
                                          governanceEngineDefinition.getDisplayName(),
                                          governanceEngineDefinition.getDescription(),
                                          null,
                                          null,
                                          null,
                                          null,
                                          null,
                                          null);
    }


    /**
     * Add entities for each governance action service.
     *
     * @param governanceServiceDefinition details of governance service
     */
    private void addGovernanceServiceDefinition(GovernanceServiceDefinition governanceServiceDefinition)
    {
        archiveHelper.setGUID(governanceServiceDefinition.getName(), governanceServiceDefinition.getGUID());

        archiveHelper.addGovernanceService(governanceServiceDefinition.getDeployedImplementationType(),
                                           governanceServiceDefinition.getConnectorProviderClassName(),
                                           null,
                                           governanceServiceDefinition.getName(),
                                           governanceServiceDefinition.getDisplayName(),
                                           governanceServiceDefinition.getDescription(),
                                           null);
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
     * @param actionTargets action targets
     * @param governanceActionDescription description of the governance action if and
     * @param governanceActionTypeGUID unique identifier of the associated governance action type
     * @param supportedElementQualifiedName element to link the governance action type to
     */
    private void addRequestType(String                      governanceEngineGUID,
                                String                      governanceEngineName,
                                String                      governanceEngineTypeName,
                                String                      governanceRequestType,
                                String                      serviceRequestType,
                                Map<String, String>         requestParameters,
                                List<NewActionTarget>       actionTargets,
                                GovernanceActionDescription governanceActionDescription,
                                String                      governanceActionTypeGUID,
                                String                      supportedElementQualifiedName)
    {
        archiveHelper.addSupportedGovernanceService(governanceEngineGUID,
                                                    governanceRequestType,
                                                    serviceRequestType,
                                                    requestParameters,
                                                    governanceActionDescription.governanceServiceGUID);

        this.addGovernanceActionType(governanceEngineGUID,
                                     governanceEngineName,
                                     governanceEngineTypeName,
                                     governanceRequestType,
                                     requestParameters,
                                     actionTargets,
                                     governanceActionDescription,
                                     governanceActionTypeGUID,
                                     supportedElementQualifiedName);
    }



    /**
     * Add details of a request type to the engine.
     *
     * @param governanceEngineGUID unique identifier of the engine
     * @param governanceEngineName name of the governance engine
     * @param governanceEngineTypeName type of engine
     * @param governanceRequestType name of request type
     * @param requestParameters any request parameters
     * @param actionTargets action targets
     * @param governanceActionDescription description of the governance action if and
     * @param governanceActionTypeGUID unique identifier of the associated governance action type
     * @param supportedElementQualifiedName element to link the governance action type to
     */
    private void addGovernanceActionType(String                      governanceEngineGUID,
                                         String                      governanceEngineName,
                                         String                      governanceEngineTypeName,
                                         String                      governanceRequestType,
                                         Map<String, String>         requestParameters,
                                         List<NewActionTarget>       actionTargets,
                                         GovernanceActionDescription governanceActionDescription,
                                         String                      governanceActionTypeGUID,
                                         String                      supportedElementQualifiedName)
    {
        String governanceActionTypeQualifiedName = governanceEngineName + ":" + governanceRequestType;

        archiveHelper.setGUID(governanceActionTypeQualifiedName, governanceActionTypeGUID);

        String guid = archiveHelper.addGovernanceActionType(null,
                                                            governanceEngineGUID,
                                                            governanceEngineTypeName,
                                                            OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                            governanceEngineName + ":" + governanceRequestType,
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

        assert(governanceActionTypeGUID.equals(guid));

        archiveHelper.addGovernanceActionExecutor(governanceActionTypeGUID,
                                                  governanceRequestType,
                                                  requestParameters,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  governanceEngineGUID);

        if (actionTargets != null)
        {
            for (NewActionTarget actionTarget : actionTargets)
            {
                if (actionTarget != null)
                {
                    archiveHelper.addTargetForActionType(governanceActionTypeGUID, actionTarget);
                }
            }
        }

        if (governanceActionDescription.supportedTechnologies != null)
        {
            for (SupportedTechnologyType supportedTechnology : governanceActionDescription.supportedTechnologies)
            {
                if (supportedTechnology != null)
                {
                    if (supportedTechnology.getDataType() != null)
                    {
                        String openMetadataTypeGUID = openMetadataTypeGUIDs.get(supportedTechnology.getDataType());

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

                    if (supportedTechnology.getName() != null)
                    {
                        String deployedImplementationTypeGUID = deployedImplementationTypeGUIDs.get(supportedTechnology.getName());

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

        if (supportedElementQualifiedName != null)
        {
            String supportedElementGUID = archiveHelper.queryGUID(supportedElementQualifiedName);
            archiveHelper.addResourceListRelationshipByGUID(supportedElementGUID,
                                                            governanceActionTypeGUID,
                                                            governanceActionDescription.resourceUse.getResourceUse(),
                                                            governanceActionDescription.governanceServiceDescription,
                                                            requestParameters,
                                                            false);
        }
    }


    /**
     * Create a sample governance action process.
     */
    private void createDailyGovernanceActionProcess()
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
                                                                        OpenMetadataType.ASSET.typeName,
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
                                                      RequestTypeDefinition.GET_DAY_OF_WEEK.getGovernanceRequestType(),
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      GovernanceEngineDefinition.STEWARDSHIP_ENGINE.getGUID());

            archiveHelper.addGovernanceActionProcessFlow(processGUID, null, null, step1GUID);
        }

        String step2GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                        OpenMetadataType.ASSET.typeName,
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
                                                      RequestTypeDefinition.WRITE_AUDIT_LOG.getGovernanceRequestType(),
                                                      requestParameters,
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      GovernanceEngineDefinition.STEWARDSHIP_ENGINE.getGUID());

            archiveHelper.addNextGovernanceActionProcessStep(step1GUID, DaysOfWeekGuard.MONDAY.getName(), false, step2GUID);
        }

        String step3GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                        OpenMetadataType.ASSET.typeName,
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
                                                      RequestTypeDefinition.WRITE_AUDIT_LOG.getGovernanceRequestType(),
                                                      requestParameters,
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      GovernanceEngineDefinition.STEWARDSHIP_ENGINE.getGUID());

            archiveHelper.addNextGovernanceActionProcessStep(step1GUID, DaysOfWeekGuard.TUESDAY.getName(), false, step3GUID);
        }

        String step4GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                        OpenMetadataType.ASSET.typeName,
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
                                                      RequestTypeDefinition.WRITE_AUDIT_LOG.getGovernanceRequestType(),
                                                      requestParameters,
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      GovernanceEngineDefinition.STEWARDSHIP_ENGINE.getGUID());

            archiveHelper.addNextGovernanceActionProcessStep(step1GUID, DaysOfWeekGuard.WEDNESDAY.getName(), false, step4GUID);
        }

        String step5GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                        OpenMetadataType.ASSET.typeName,
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
                                                      RequestTypeDefinition.WRITE_AUDIT_LOG.getGovernanceRequestType(),
                                                      requestParameters,
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      GovernanceEngineDefinition.STEWARDSHIP_ENGINE.getGUID());

            archiveHelper.addNextGovernanceActionProcessStep(step1GUID, DaysOfWeekGuard.THURSDAY.getName(), false, step5GUID);
        }

        String step6GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                        OpenMetadataType.ASSET.typeName,
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
                                                      RequestTypeDefinition.WRITE_AUDIT_LOG.getGovernanceRequestType(),
                                                      requestParameters,
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      GovernanceEngineDefinition.STEWARDSHIP_ENGINE.getGUID());

            archiveHelper.addNextGovernanceActionProcessStep(step1GUID, DaysOfWeekGuard.FRIDAY.getName(), false, step6GUID);
        }

        String step7GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                        OpenMetadataType.ASSET.typeName,
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
                                                      RequestTypeDefinition.WRITE_AUDIT_LOG.getGovernanceRequestType(),
                                                      requestParameters,
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      GovernanceEngineDefinition.STEWARDSHIP_ENGINE.getGUID());

            archiveHelper.addNextGovernanceActionProcessStep(step1GUID, DaysOfWeekGuard.SATURDAY.getName(), false, step7GUID);
        }


        String step8GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                        OpenMetadataType.ASSET.typeName,
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
                                                      RequestTypeDefinition.WRITE_AUDIT_LOG.getGovernanceRequestType(),
                                                      requestParameters,
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      GovernanceEngineDefinition.STEWARDSHIP_ENGINE.getGUID());

            archiveHelper.addNextGovernanceActionProcessStep(step1GUID, DaysOfWeekGuard.SUNDAY.getName(), false, step8GUID);
        }
    }

    
    /**
     * Create a sample governance action process.
     */
    private void createAndSurveyUCGovernanceActionProcess()
    {
        String processGUID = archiveHelper.addGovernanceActionProcess(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                      "UnityCatalogServer:CreateAndSurveyGovernanceActionProcess",
                                                                      "UnityCatalogServer:CreateAndSurvey",
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
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        "UnityCatalogServer:CreateAndSurvey:Step1",
                                                                        "Create the software server entity",
                                                                        "Create the description of the Unity Catalog Server",
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
                                                      RequestTypeDefinition.CREATE_UC_SERVER.getGovernanceRequestType(),
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      GovernanceEngineDefinition.UNITY_CATALOG_GOVERNANCE_ENGINE.getGUID());

            archiveHelper.addGovernanceActionProcessFlow(processGUID, null, null, step1GUID);
        }

        String step2GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        "UnityCatalogServer:CreateAndSurvey:Step2",
                                                                        "Run the survey.",
                                                                        "Create a survey report detailing the contents of the Unity Catalog Server.",
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
            archiveHelper.addGovernanceActionExecutor(step2GUID,
                                                      RequestTypeDefinition.SURVEY_UC_SERVER.getGovernanceRequestType(),
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      GovernanceEngineDefinition.UNITY_CATALOG_SURVEY_ENGINE.getGUID());

            archiveHelper.addNextGovernanceActionProcessStep(step1GUID, CreateServerGuard.SET_UP_COMPLETE.getName(), false, step2GUID);
        }
    }


    /**
     * Create a sample governance action process.
     */
    private void createAndCatalogUCGovernanceActionProcess()
    {
        String processGUID = archiveHelper.addGovernanceActionProcess(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                      "UnityCatalogServer:CreateAndCatalogGovernanceActionProcess",
                                                                      "UnityCatalogServer:CreateAndCatalog",
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
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        "UnityCatalogServer:CreateAndCatalog:Step1",
                                                                        "Create the software server entity",
                                                                        "Create the description of the Unity Catalog Server",
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
                                                      RequestTypeDefinition.CREATE_UC_SERVER.getGovernanceRequestType(),
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      GovernanceEngineDefinition.UNITY_CATALOG_GOVERNANCE_ENGINE.getGUID());

            archiveHelper.addGovernanceActionProcessFlow(processGUID, null, null, step1GUID);
        }

        String step2GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        "UnityCatalogServer:CreateAndCatalog:Step2",
                                                                        "Run the survey.",
                                                                        "Connect the server entity for the Unity Catalog Server to the appropriate integration connector.",
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
            archiveHelper.addGovernanceActionExecutor(step2GUID,
                                                      RequestTypeDefinition.CATALOG_UC_SERVER.getGovernanceRequestType(),
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      GovernanceEngineDefinition.UNITY_CATALOG_GOVERNANCE_ENGINE.getGUID());

            for (NewActionTarget actionTarget : RequestTypeDefinition.CATALOG_UC_SERVER.getActionTargets())
            {
                if (actionTarget != null)
                {
                    archiveHelper.addTargetForActionType(step2GUID, actionTarget);
                }
            }


            archiveHelper.addNextGovernanceActionProcessStep(step1GUID, CreateServerGuard.SET_UP_COMPLETE.getName(), false, step2GUID);
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
            XCoreContentArchiveWriter archiveWriter = new XCoreContentArchiveWriter();

            archiveWriter.writeOpenMetadataArchive();
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error);
            System.exit(-1);
        }
    }
}