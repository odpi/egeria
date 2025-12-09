/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.contentpacks.core.core;

import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStoreProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFolderProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVFileStoreProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVTabularDataSetCollectionProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVTabularDataSetProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.datafolder.DataFolderProvider;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control.EgeriaSoftwareServerTemplateDefinition;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.OMAGServerPlatformProvider;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.catalog.OMAGServerPlatformCatalogProvider;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.*;
import org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship.DaysOfWeekGuard;
import org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship.WriteAuditLogRequestParameter;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.DataFilesMonitorIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.DataFolderMonitorIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.csvlineageimporter.CSVLineageImporterProvider;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.JDBCIntegrationConnectorProvider;
import org.odpi.openmetadata.adapters.connectors.integration.kafkaaudit.DistributeAuditEventsFromKafkaProvider;
import org.odpi.openmetadata.adapters.connectors.integration.openlineage.*;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnectorProvider;
import org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider;
import org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider;
import org.odpi.openmetadata.contentpacks.core.*;
import org.odpi.openmetadata.contentpacks.core.base.ContentPackBaseArchiveWriter;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.*;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;

import java.util.*;

import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * CorePackArchiveWriter creates an open metadata archive that includes the connector type
 * information for the default open connectors supplied by the egeria project.
 */
public class CorePackArchiveWriter extends ContentPackBaseArchiveWriter
{
    /**
     * Default constructor initializes the archive.
     */
    public CorePackArchiveWriter()
    {
        super(ContentPackDefinition.CORE_CONTENT_PACK.getArchiveGUID(),
              ContentPackDefinition.CORE_CONTENT_PACK.getArchiveName(),
              ContentPackDefinition.CORE_CONTENT_PACK.getArchiveDescription(),
              ContentPackDefinition.CORE_CONTENT_PACK.getArchiveFileName(),
              null);
    }


    /**
     * Implemented by subclass to add the content.
     */
    @Override
    public void getArchiveContent()
    {
        /*
         * Add Egeria's common solution definitions
         */
        archiveHelper.addSolutionRoles(List.of(EgeriaRoleDefinition.values()));
        archiveHelper.addSolutionComponents(List.of(EgeriaSolutionComponent.values()));
        archiveHelper.addSolutionComponentActors(List.of(EgeriaSolutionComponentActor.values()));
        archiveHelper.addSolutionComponentWires(List.of(EgeriaSolutionComponentWire.values()));
        archiveHelper.addSolutionBlueprints(List.of(EgeriaSolutionBlueprint.values()));

        /*
         * Add the root digital product catalog.
         */
        addDigitalProductCatalogDefinition(ContentPackDefinition.CORE_CONTENT_PACK);

        /*
         * Add the valid metadata values used in the resourceUse property of the ResourceList relationship.
         */
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

            this.addValidMetadataValue(null,
                                       resourceUse.getResourceUse(),
                                       resourceUse.getDescription(),
                                       OpenMetadataProperty.RESOURCE_USE.name,
                                       DataType.STRING.getName(),
                                       null,
                                       null,
                                       resourceUse.getResourceUse(),
                                       additionalProperties);
        }


        /*
         * Add the valid metadata values used in the scope property found in many elements.
         */
        for (ScopeDefinition scopeDefinition : ScopeDefinition.values())
        {
            this.addValidMetadataValue(scopeDefinition.getPreferredValue(),
                                       scopeDefinition.getDescription(),
                                       OpenMetadataProperty.SCOPE.name,
                                       null,
                                       null,
                                       scopeDefinition.getPreferredValue());
        }


        /*
         * Add the valid metadata values used in the solutionComponentType property of the SolutionComponent entity.
         */
        for (SolutionComponentType solutionComponentType : SolutionComponentType.values())
        {
            this.addValidMetadataValue(solutionComponentType.getSolutionComponentType(),
                                       solutionComponentType.getDescription(),
                                       OpenMetadataProperty.SOLUTION_COMPONENT_TYPE.name,
                                       null,
                                       null,
                                       solutionComponentType.getSolutionComponentType());
        }


        /*
         * The resource use properties provide the mapNames for resource use properties.
         * There are no values for these map names.
         */
        for (ResourceUseProperties resourceUseProperties : ResourceUseProperties.values())
        {
            this.addValidMetadataValue(resourceUseProperties.getName(),
                                       resourceUseProperties.getDescription(),
                                       OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                       OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                       null,
                                       resourceUseProperties.getName());
        }

        /*
         * Add the valid metadata values used in the projectPhase property of the Project entity.
         */
        for (ProjectPhase projectStatus : ProjectPhase.values())
        {
            this.addValidMetadataValue(projectStatus.getName(),
                                       projectStatus.getDescription(),
                                       OpenMetadataProperty.PROJECT_PHASE.name,
                                       null,
                                       null,
                                       projectStatus.getName());
        }


        /*
         * Add the valid metadata values used in the projectHealth property of the Project entity.
         */
        for (ProjectHealth projectHealth : ProjectHealth.values())
        {
            Map<String, String> additionalProperties = new HashMap<>();

            additionalProperties.put("colour", projectHealth.getColour());

            this.addValidMetadataValue(null,
                                       projectHealth.getName(),
                                       projectHealth.getDescription(),
                                       OpenMetadataProperty.PROJECT_HEALTH.name,
                                       DataType.STRING.getName(),
                                       null,
                                       null,
                                       projectHealth.getName(),
                                       additionalProperties);
        }


        /*
         * Add the valid metadata values used in the projectStatus property of the Project entity.
         */
        for (ProjectStatus projectStatus : ProjectStatus.values())
        {
            this.addValidMetadataValue(projectStatus.getName(),
                                       projectStatus.getDescription(),
                                       OpenMetadataProperty.PROJECT_STATUS.name,
                                       null,
                                       null,
                                       projectStatus.getName());
        }


        /*
         * Add the valid metadata values used in the category property of the Referenceable entity.
         */
        for (Category category : Category.values())
        {
            this.addValidMetadataValue(category.getName(),
                                       category.getDescription(),
                                       OpenMetadataProperty.CATEGORY.name,
                                       null,
                                       null,
                                       category.getName());
        }


        /*
         * Add the valid metadata values used in the portType property of the Port entity.
         */
        for (PortType portType : PortType.values())
        {
            this.addValidMetadataValue(portType.getName(),
                                       portType.getDescription(),
                                       OpenMetadataProperty.PORT_TYPE.name,
                                       null,
                                       null,
                                       portType.getName());
        }



        /*
         * Add the valid metadata values used in the collectionType property of the Collection entity.
         */
        for (SolutionPortDirection solutionPortDirection : SolutionPortDirection.values())
        {
            super.addValidMetadataValue(solutionPortDirection.getDescriptionGUID(),
                                        solutionPortDirection.getName(),
                                        solutionPortDirection.getDescription(),
                                        OpenMetadataProperty.DIRECTION.name,
                                        DataType.STRING.getName(),
                                        null,
                                        null,
                                        solutionPortDirection.getName(),
                                        null);
        }


        /*
         * Add valid metadata values for open metadata types that have been reformatted.
         * The GUIDs are saved in a look-up map
         * to make it easy to link other elements to these valid values later.
         */
        for (OpenMetadataType openMetadataType : OpenMetadataType.values())
        {
            this.addOpenMetadataType(openMetadataType);
        }

        /*===========================================
         * Add the open metadata type enums
         */

        addOpenMetadataEnumValidIdentifiers(OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeName,
                                            OpenMetadataProperty.CONFIDENCE_LEVEL_IDENTIFIER.name,
                                            new ArrayList<>(Arrays.asList(ConfidenceLevel.values())));

        addOpenMetadataEnumValidIdentifiers(OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName,
                                            OpenMetadataProperty.CONFIDENTIALITY_LEVEL_IDENTIFIER.name,
                                            new ArrayList<>(Arrays.asList(ConfidentialityLevel.values())));

        addOpenMetadataEnumValidIdentifiers(OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName,
                                            OpenMetadataProperty.CRITICALITY_LEVEL_IDENTIFIER.name,
                                            new ArrayList<>(Arrays.asList(CriticalityLevel.values())));

        addOpenMetadataEnumValidIdentifiers(OpenMetadataType.RETENTION_CLASSIFICATION.typeName,
                                            OpenMetadataProperty.RETENTION_BASIS_IDENTIFIER.name,
                                            new ArrayList<>(Arrays.asList(RetentionBasis.values())));

        addOpenMetadataEnumValidIdentifiers(null,
                                            OpenMetadataProperty.STATUS_IDENTIFIER.name,
                                            new ArrayList<>(Arrays.asList(GovernanceClassificationStatus.values())));

        addOpenMetadataEnumValidIdentifiers(null,
                                            OpenMetadataProperty.SEVERITY_IDENTIFIER.name,
                                            new ArrayList<>(Arrays.asList(ImpactSeverity.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.BYTE_ORDERING.name,
                                      ByteOrdering.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(ByteOrdering.values())));

        addOpenMetadataEnumValidNames( OpenMetadataProperty.OPERATIONAL_STATUS.name,
                                      OperationalStatus.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(OperationalStatus.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.DELETE_METHOD.name,
                                      DeleteMethod.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(DeleteMethod.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.COVERAGE_CATEGORY.name,
                                      CoverageCategory.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(CoverageCategory.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.PERMITTED_SYNCHRONIZATION.name,
                                      PermittedSynchronization.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(PermittedSynchronization.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.BUSINESS_CAPABILITY_TYPE.name,
                                      BusinessCapabilityType.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(BusinessCapabilityType.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.KEY_PATTERN.name,
                                      KeyPattern.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(KeyPattern.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.MEMBERSHIP_STATUS.name,
                                      CollectionMemberStatus.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(CollectionMemberStatus.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.COMMENT_TYPE.name,
                                      CommentType.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(CommentType.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.CONTACT_METHOD_TYPE.name,
                                      ContactMethodType.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(ContactMethodType.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.DATA_CLASS_ASSIGNMENT_STATUS.name,
                                      DataClassAssignmentStatus.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(DataClassAssignmentStatus.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.SORT_ORDER.name,
                                      DataItemSortOrder.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(DataItemSortOrder.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.ACTIVITY_STATUS.name,
                                      ActivityStatus.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(ActivityStatus.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.ACTIVITY_TYPE.name,
                                      ActivityType.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(ActivityType.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.TERM_ASSIGNMENT_STATUS.name,
                                      TermAssignmentStatus.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(TermAssignmentStatus.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.TERM_RELATIONSHIP_STATUS.name,
                                      TermRelationshipStatus.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(TermRelationshipStatus.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.USE_TYPE.name,
                                      CapabilityAssetUseType.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(CapabilityAssetUseType.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.INCIDENT_STATUS.name,
                                      IncidentReportStatus.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(IncidentReportStatus.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.STARS.name,
                                      StarRating.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(StarRating.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.CONTAINMENT_TYPE.name,
                                      ProcessContainmentType.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(ProcessContainmentType.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.MEDIA_TYPE.name,
                                      MediaType.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(MediaType.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.DEFAULT_MEDIA_USAGE.name,
                                      MediaUsage.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(MediaUsage.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.MEDIA_USAGE.name,
                                      MediaUsage.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(MediaUsage.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.DECORATION.name,
                                      RelationshipDecoration.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(RelationshipDecoration.values())));

        /*
         * Add valid metadata values for deployedImplementationType.  The GUIDs are saved in a look-up map
         * to make it easy to link other elements to these valid values later.
         */
        for (DeployedImplementationType deployedImplementationType : DeployedImplementationType.values())
        {
            this.addDeployedImplementationType(deployedImplementationType.getGUID(),
                                               deployedImplementationType.getDeployedImplementationType(),
                                               deployedImplementationType.getAssociatedTypeName(),
                                               deployedImplementationType.getQualifiedName(),
                                               deployedImplementationType.getDescription(),
                                               deployedImplementationType.getWikiLink(),
                                               deployedImplementationType.getIsATypeOf());
        }


        /*
         * Add the valid values for the assignmentType property.
         */
        for (AssignmentType assignmentType : AssignmentType.values())
        {
            this.addAssignmentType(assignmentType.getName(),
                                   assignmentType.getDescription(),
                                   assignmentType.getDescriptionGUID());
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
        archiveHelper.addConnectorType(new CSVFileStoreProvider());
        archiveHelper.addConnectorType(new CSVTabularDataSetProvider());
        archiveHelper.addConnectorType(new CSVTabularDataSetCollectionProvider());
        archiveHelper.addConnectorType(new DataFolderProvider());
        archiveHelper.addConnectorType(new BasicFileStoreProvider());
        archiveHelper.addConnectorType(new BasicFolderProvider());
        archiveHelper.addConnectorType(new JDBCResourceConnectorProvider());
        archiveHelper.addConnectorType(new KafkaOpenMetadataTopicProvider());

        archiveHelper.addConnectorType(new YAMLSecretsStoreProvider());

        archiveHelper.addConnectorType(new CSVLineageImporterProvider());
        archiveHelper.addConnectorType(new DataFilesMonitorIntegrationProvider());
        archiveHelper.addConnectorType(new DataFolderMonitorIntegrationProvider());
        archiveHelper.addConnectorType(new JDBCIntegrationConnectorProvider());
        archiveHelper.addConnectorType(new DistributeAuditEventsFromKafkaProvider());

        archiveHelper.addConnectorType(new APIBasedOpenLineageLogStoreProvider());
        archiveHelper.addConnectorType(new FileBasedOpenLineageLogStoreProvider());
        archiveHelper.addConnectorType(new GovernanceActionOpenLineageIntegrationProvider());
        archiveHelper.addConnectorType(new OpenLineageCataloguerIntegrationProvider());
        archiveHelper.addConnectorType(new OpenLineageEventReceiverIntegrationProvider());

        archiveHelper.addConnectorType(new OMAGServerPlatformCatalogProvider());
        archiveHelper.addConnectorType(new OMAGServerPlatformProvider());
        archiveHelper.addConnectorType(new OMAGServerProvider());
        archiveHelper.addConnectorType(new EngineHostProvider());
        archiveHelper.addConnectorType(new IntegrationDaemonProvider());
        archiveHelper.addConnectorType(new MetadataAccessServerProvider());
        archiveHelper.addConnectorType(new ViewServerProvider());

        /*
         * Add catalog templates
         */
        this.addEndpointCatalogTemplates();
        this.addSoftwareServerCatalogTemplates(ContentPackDefinition.CORE_CONTENT_PACK);
        this.addDataAssetCatalogTemplates(ContentPackDefinition.CORE_CONTENT_PACK);
        this.addTabularDataSetCatalogTemplates(ContentPackDefinition.CORE_CONTENT_PACK);



        this.addMacBookProCatalogTemplate();
        this.addFileSystemTemplate();
        this.addUNIXFileSystemTemplate();
        // this.addDefaultOMAGServerPlatform();


        /*
         * Create the default integration group.
         */
        super.addIntegrationGroups(ContentPackDefinition.CORE_CONTENT_PACK);

        /*
         * Add the integration connectors to the default integration group
         */
        super.addIntegrationConnectors(ContentPackDefinition.CORE_CONTENT_PACK, IntegrationGroupDefinition.DEFAULT);

        /*
         * Create the default governance engines
         */
        super.createGovernanceEngines(ContentPackDefinition.CORE_CONTENT_PACK);

        /*
         * Register the governance services that are going to be in the default governance engines.
         */
        super.createGovernanceServices(ContentPackDefinition.CORE_CONTENT_PACK);

        /*
         * Connect the governance engines to the governance services using the request types.
         */
        super.createRequestTypes(ContentPackDefinition.CORE_CONTENT_PACK);



        this.deleteAsCatalogTargetGovernanceActionProcess("ApacheKafkaTopic",
                                                          DeployedImplementationType.APACHE_KAFKA_TOPIC.getAssociatedTypeName(),
                                                          DeployedImplementationType.APACHE_KAFKA_TOPIC.getDeployedImplementationType(),
                                                          RequestTypeDefinition.DELETE_KAFKA_TOPIC,
                                                          DeployedImplementationType.APACHE_KAFKA_TOPIC.getQualifiedName());



        /*
         * Create a sample process
         */
        this.createDailyGovernanceActionProcess();

        /*
         * Define the solution components for this solution.
         */
        this.addSolutionBlueprints(ContentPackDefinition.CORE_CONTENT_PACK);
        this.addSolutionLinkingWires(ContentPackDefinition.CORE_CONTENT_PACK);


        /*
         * Saving the GUIDs means tha the guids in the archive are stable between runs of the archive writer.
         */
        archiveHelper.saveGUIDs();
        archiveHelper.saveUsedGUIDs();
    }


    /**
     * Set up a valid value list for an enum based on its names.
     *
     * @param enumConsumingProperty attribute name
     * @param enumTypeName          type name for enum
     * @param openMetadataEnums     list of valid values
     */
    protected void addOpenMetadataEnumValidNames(String                 enumConsumingProperty,
                                                 String                 enumTypeName,
                                                 List<OpenMetadataEnum> openMetadataEnums)
    {
        for (OpenMetadataEnum enumValue : openMetadataEnums)
        {
            String enumPreferredValue = enumValue.getName().toUpperCase();
            enumPreferredValue = enumPreferredValue.replace(' ', '_');

            Map<String, String> additionalProperties = new HashMap<>();

            additionalProperties.put("ordinal", Integer.toString(enumValue.getOrdinal()));
            additionalProperties.put(OpenMetadataProperty.IS_DEFAULT_VALUE.name, Boolean.toString(enumValue.isDefault()));
            additionalProperties.put(OpenMetadataProperty.OPEN_METADATA_TYPE_NAME.name, enumTypeName);

            super.addValidMetadataValue(enumValue.getDescriptionGUID(),
                                        enumValue.getName(),
                                        enumValue.getDescription(),
                                        enumConsumingProperty,
                                        DataType.STRING.getName(),
                                        null,
                                        null,
                                        enumPreferredValue,
                                        additionalProperties);
        }
    }


    /**
     * Set up a valid value list for an enum based on its ordinals.
     *
     * @param enumConsumingTypeName entity type name
     * @param enumConsumingProperty attribute name
     * @param openMetadataEnums     list of valid values
     */
    protected void addOpenMetadataEnumValidIdentifiers(String                 enumConsumingTypeName,
                                                       String                 enumConsumingProperty,
                                                       List<OpenMetadataEnum> openMetadataEnums)
    {
        for (OpenMetadataEnum enumValue : openMetadataEnums)
        {
            String enumPreferredValue = enumValue.getName().toUpperCase();
            enumPreferredValue = enumPreferredValue.replace(' ', '_');

            super.addValidMetadataValue(enumValue.getName(),
                                        enumValue.getDescription(),
                                        enumConsumingProperty,
                                        enumConsumingTypeName,
                                        null,
                                        enumPreferredValue);
        }
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
     * This entry is used by Runtime Manager to display the platform report for 9443
     */
    private void addDefaultOMAGServerPlatform()
    {
        final String guid = "44bf319f-1e41-4da1-b771-2753b92b631a";
        OMAGServerPlatformProvider provider = new OMAGServerPlatformProvider();

        DeployedImplementationTypeDefinition deployedImplementationType = EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM;
        DeployedImplementationTypeDefinition softwareCapabilityType = DeployedImplementationType.USER_AUTHENTICATION_MANAGER;
        String softwareCapabilityName = "User Token Manager";
        String serverName = "Default Local OMAG Server Platform";
        String userId = "defaultplatformnpa";
        String connectorTypeGUID = provider.getConnectorType().getGUID();
        String networkAddress = "https://localhost:9443";

        String               qualifiedName      = deployedImplementationType.getDeployedImplementationType() + "::" + serverName;
        String               versionIdentifier  = "6.0-SNAPSHOT";
        String               description        = "Default OMAG Server Platform running on local host and port 9443.";
        List<Classification> classifications    = null;


        if (deployedImplementationType.getAssociatedClassification() != null)
        {
            classifications    = new ArrayList<>();
            classifications.add(archiveHelper.getServerPurposeClassification(deployedImplementationType.getAssociatedClassification(), null));
        }

        archiveHelper.setGUID(qualifiedName, guid);
        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  serverName,
                                                  deployedImplementationType.getDeployedImplementationType(),
                                                  versionIdentifier,
                                                  description,
                                                  null,
                                                  null,
                                                  classifications);
        assert(guid.equals(assetGUID));

        archiveHelper.addSoftwareCapability(softwareCapabilityType.getAssociatedTypeName(),
                                            qualifiedName + "::" + softwareCapabilityName,
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
                                            OpenMetadataType.ASSET.typeName,
                                            null);

        archiveHelper.addSupportedSoftwareCapabilityRelationship(qualifiedName + "::" + softwareCapabilityName,
                                                                 qualifiedName,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 1);

        String endpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                        deployedImplementationType.getAssociatedTypeName(),
                                                        OpenMetadataType.ASSET.typeName,
                                                        null,
                                                        qualifiedName + ":Endpoint",
                                                        serverName + " endpoint",
                                                        null,
                                                        networkAddress,
                                                        null,
                                                        null);

        archiveHelper.addServerEndpointRelationship(assetGUID, endpointGUID);

        Map<String, Object> configurationProperties = new HashMap<>();

        configurationProperties.put(PlaceholderProperty.SECRETS_STORE.getName(), "loading-bay/secrets/default.omsecrets");

        String connectionGUID = archiveHelper.addConnection(qualifiedName + ":Connection",
                                                            serverName + " connection",
                                                            null,
                                                            userId,
                                                            null,
                                                            null,
                                                            null,
                                                            configurationProperties,
                                                            null,
                                                            connectorTypeGUID,
                                                            endpointGUID,
                                                            assetGUID,
                                                            deployedImplementationType.getAssociatedTypeName(),
                                                            OpenMetadataType.ASSET.typeName,
                                                            null);

        archiveHelper.addConnectionForAsset(assetGUID, connectionGUID);
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
     * Add a new valid values record for a deployed implementation type.
     *
     * @param openMetadataType preferred value
     */
    private void addOpenMetadataType(OpenMetadataType openMetadataType)
    {
        Map<String, String> additionalProperties = new HashMap<>();

        additionalProperties.put("typeGUID", openMetadataType.typeGUID);

        if (openMetadataType.beanClass != null)
        {
            additionalProperties.put("beanClass", openMetadataType.beanClass.getName());
        }

        String parentSetGUID = this.getParentSet(null,
                                                 null,
                                                 OpenMetadataProperty.OPEN_METADATA_TYPE_NAME.name,
                                                 null);

        String qualifiedName = constructValidValueQualifiedName(null,
                                                                OpenMetadataProperty.OPEN_METADATA_TYPE_NAME.name,
                                                                null,
                                                                openMetadataType.typeName);

        String validValueGUID = this.archiveHelper.addValidValue(openMetadataType.descriptionGUID,
                                                                 parentSetGUID,
                                                                 parentSetGUID,
                                                                 OpenMetadataType.VALID_METADATA_VALUE.typeName,
                                                                 OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                 null,
                                                                 OpenMetadataType.VALID_METADATA_VALUE.typeName,
                                                                 qualifiedName,
                                                                 Category.OPEN_METADATA_TYPES.getName(),
                                                                 OpenMetadataProperty.OPEN_METADATA_TYPE_NAME.name,
                                                                 openMetadataType.typeName,
                                                                 openMetadataType.description,
                                                                 null,
                                                                 null,
                                                                 DataType.STRING.getName(),
                                                                 OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE,
                                                                 openMetadataType.typeName,
                                                                 null,
                                                                 openMetadataType.wikiURL,
                                                                 false,
                                                                 additionalProperties);

        assert(openMetadataType.descriptionGUID.equals(validValueGUID));
    }


    /**
     * Add reference data that describes a specific assignment type.
     *
     * @param name               the name of the assignment type
     * @param description                description of the type
     * @param descriptionGUID           guid of the type
     */
    private void addAssignmentType(String name,
                                   String description,
                                   String descriptionGUID)
    {

        String qualifiedName = constructValidValueQualifiedName(null,
                                                                OpenMetadataProperty.ASSIGNMENT_TYPE.name,
                                                                null,
                                                                name);

        this.archiveHelper.setGUID(qualifiedName, descriptionGUID);

        this.addValidMetadataValue(name,
                                   description,
                                   OpenMetadataProperty.ASSIGNMENT_TYPE.name,
                                   null,
                                   null,
                                   name);
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
        this.addValidMetadataValue(attributeName,
                                   attributeDescription,
                                   OpenMetadataProperty.PROPERTY_NAME.name,
                                   null,
                                   null,
                                   attributeName);
    }


    /**
     * Create a sample governance action process.
     */
    private void createDailyGovernanceActionProcess()
    {
        String processGUID = archiveHelper.addGovernanceActionProcess(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                      "Egeria:DailyGovernanceActionProcess",
                                                                      "DailyGovernanceActionProcess",
                                                                      null,
                                                                      "Determines which day of the week it is today, and puts out a message on the audit log matching the assigned task for the day of the week.",
                                                                      null,
                                                                      0,
                                                                      null,
                                                                      null,
                                                                      null);

        String step1GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        null,
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

        String step2GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        null,
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

        String step3GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        null,
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

        String step4GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        null,
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

        String step5GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        null,
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

        String step6GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        null,
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

        String step7GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        null,
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


        String step8GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        null,
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
     * Main program to initiate the archive writer for the connector types for data store connectors supported by
     * the Egeria project.
     *
     * @param args ignored
     */
    public static void main(String[] args)
    {
        try
        {
            CorePackArchiveWriter archiveWriter = new CorePackArchiveWriter();

            archiveWriter.writeOpenMetadataArchive();
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error);
            System.exit(-1);
        }
    }
}