/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.contentpacks.core.core;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.EgeriaRoleDefinition;
import org.odpi.openmetadata.adapters.connectors.ExceptionTypeDefinition;
import org.odpi.openmetadata.adapters.connectors.controls.EgeriaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship.DaysOfWeekGuard;
import org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship.WriteAuditLogRequestParameter;
import org.odpi.openmetadata.contentpacks.core.*;
import org.odpi.openmetadata.contentpacks.core.base.ContentPackBaseArchiveWriter;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProvider;
import org.odpi.openmetadata.frameworks.connectors.properties.users.AccessOperation;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccountStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccountType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.enums.*;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyDatabaseAnnotationType;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyResourceManagerAnnotationType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
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
     * The default constructor initializes the archive.
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
         * Add the root digital product catalog.
         */
        addDigitalProductCatalogDefinition(ContentPackDefinition.CORE_CONTENT_PACK);

        /*
         * Write exception types used by Egeria's connectors.
         */
        writeExceptionTypes();

        /*
         * Add valid metadata values for the Survey Action Framework standard controls.
         */
        for (AnalysisStep analysisStep : AnalysisStep.values())
        {
            this.addAnalysisStep(analysisStep.getName(), analysisStep.getDescription());
        }

        for (SurveyResourceManagerAnnotationType annotationType : SurveyResourceManagerAnnotationType.values())
        {
            this.addAnnotationType(annotationType);
        }

        for (SurveyDatabaseAnnotationType annotationType : SurveyDatabaseAnnotationType.values())
        {
            this.addAnnotationType(annotationType);
        }

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
                                       DataType.STRING.getDisplayName(),
                                       null,
                                       null,
                                       resourceUse.getResourceUse(),
                                       resourceUse.ordinal(),
                                       false,
                                       true,
                                       additionalProperties);
        }


        /*
         * Add the valid metadata values used in the standard zone name.
         */
        for (GovernanceZoneName governanceZoneName : GovernanceZoneName.values())
        {
            this.addValidMetadataValue(governanceZoneName.getDisplayName(),
                                       governanceZoneName.getDescription(),
                                       OpenMetadataProperty.ZONE_MEMBERSHIP.name,
                                       null,
                                       null,
                                       governanceZoneName.getZoneName());
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
                                       OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                       OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
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
         * Add the valid metadata values used in the projectPhase property of the Project entity.
         */
        for (ActionType actionType : ActionType.values())
        {
            this.addValidMetadataValue(actionType.getDisplayName(),
                                       actionType.getDescription(),
                                       OpenMetadataProperty.CATEGORY.name,
                                       OpenMetadataType.ACTION.typeName,
                                       null,
                                       actionType.getDisplayName());
        }

        for (ConfidenceLevel confidenceLevel : ConfidenceLevel.values())
        {
            this.addValidMetadataValue(confidenceLevel.getDisplayName(),
                                       confidenceLevel.getDescription(),
                                       OpenMetadataProperty.CONFIDENCE_LEVEL.name,
                                       DataType.INT.getDisplayName(),
                                       OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeName,
                                       null,
                                       Integer.toString(confidenceLevel.getOrdinal()),
                                       confidenceLevel.getOrdinal());
        }

        for (ConfidentialityLevel confidentialityLevel : ConfidentialityLevel.values())
        {
            this.addValidMetadataValue(confidentialityLevel.getDisplayName(),
                                       confidentialityLevel.getDescription(),
                                       OpenMetadataProperty.CONFIDENTIALITY_LEVEL.name,
                                       DataType.INT.getDisplayName(),
                                       OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName,
                                       null,
                                       Integer.toString(confidentialityLevel.getOrdinal()),
                                       confidentialityLevel.getOrdinal());
        }

        for (CriticalityLevel criticalityLevel : CriticalityLevel.values())
        {
            this.addValidMetadataValue(criticalityLevel.getDisplayName(),
                                       criticalityLevel.getDescription(),
                                       OpenMetadataProperty.CRITICALITY_LEVEL.name,
                                       DataType.INT.getDisplayName(),
                                       OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName,
                                       null,
                                       Integer.toString(criticalityLevel.getOrdinal()),
                                       criticalityLevel.getOrdinal());
        }

        for (GovernanceDomain governanceDomain : GovernanceDomain.values())
        {
            this.addValidMetadataValue(governanceDomain.getDisplayName(),
                                       governanceDomain.getDescription(),
                                       OpenMetadataProperty.DOMAIN_IDENTIFIER.name,
                                       DataType.INT.getDisplayName(),
                                       null,
                                       null,
                                       Integer.toString(governanceDomain.getOrdinal()),
                                       governanceDomain.getOrdinal());
        }

        for (RetentionBasis retentionBasis : RetentionBasis.values())
        {
            this.addValidMetadataValue(retentionBasis.getDisplayName(),
                                       retentionBasis.getDescription(),
                                       OpenMetadataProperty.RETENTION_BASIS.name,
                                       DataType.INT.getDisplayName(),
                                       OpenMetadataType.RETENTION_CLASSIFICATION.typeName,
                                       null,
                                       Integer.toString(retentionBasis.getOrdinal()),
                                       retentionBasis.getOrdinal());
        }

        for (StatusIdentifier statusIdentifier : StatusIdentifier.values())
        {
            this.addValidMetadataValue(statusIdentifier.getDisplayName(),
                                       statusIdentifier.getDescription(),
                                       OpenMetadataProperty.STATUS_IDENTIFIER.name,
                                       DataType.INT.getDisplayName(),
                                       null,
                                       null,
                                       Integer.toString(statusIdentifier.ordinal()),
                                       statusIdentifier.ordinal());
        }

        for (SeverityLevel severityLevel : SeverityLevel.values())
        {
            this.addValidMetadataValue(severityLevel.getDisplayName(),
                                       severityLevel.getDescription(),
                                       OpenMetadataProperty.SEVERITY_LEVEL.name,
                                       DataType.INT.getDisplayName(),
                                       null,
                                       null,
                                       Integer.toString(severityLevel.ordinal()),
                                       severityLevel.ordinal());
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
                                       DataType.STRING.getDisplayName(),
                                       null,
                                       null,
                                       projectHealth.getName(),
                                       projectHealth.ordinal(),
                                       false,
                                       true,
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
         * Add the valid metadata values used in the actorRoleGroups list in  the PersonRole entity.
         */
        for (ActorRoleGroup actorRoleGroup : ActorRoleGroup.values())
        {
            this.addValidMetadataValue(actorRoleGroup.getGUID(),
                                       actorRoleGroup.getName(),
                                       actorRoleGroup.getDescription(),
                                       DataType.STRING.getDisplayName(),
                                       OpenMetadataProperty.ACTOR_ROLE_GROUPS.name,
                                       null,
                                       null,
                                       actorRoleGroup.getName(),
                                       0,
                                       false,
                                       true,
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

        for (OpenMetadataProperty openMetadataProperty : OpenMetadataProperty.values())
        {
            this.addOpenMetadataProperty(openMetadataProperty);
        }

        /*===========================================
         * Add the open metadata enums for element headers
         */
        for (ElementStatus elementStatus : ElementStatus.values())
        {
            this.addValidMetadataValue(elementStatus.getDisplayName(),
                                       elementStatus.getDescription(),
                                       OpenMetadataProperty.STATUS.name,
                                       DataType.STRING.getDisplayName(),
                                       null,
                                       null,
                                       elementStatus.name(),
                                       elementStatus.ordinal());
        }

        for (ElementOriginCategory originCategory : ElementOriginCategory.values())
        {
            this.addValidMetadataValue(originCategory.getDisplayName(),
                                       originCategory.getDescription(),
                                       OpenMetadataProperty.ORIGIN_CATEGORY.name,
                                       DataType.STRING.getDisplayName(),
                                       null,
                                       null,
                                       originCategory.name(),
                                       originCategory.ordinal());
        }

        /*===========================================
         * Add the open metadata API enums
         */

        for (SequencingOrder sequencingOrder : SequencingOrder.values())
        {
            this.addValidMetadataValue(sequencingOrder.getDisplayName(),
                                       sequencingOrder.getDescription(),
                                       OpenMetadataProperty.SEQUENCING_ORDER.name,
                                       DataType.STRING.getDisplayName(),
                                       null,
                                       null,
                                       sequencingOrder.name(),
                                       sequencingOrder.ordinal());
        }

        for (PropertyComparisonOperator operator : PropertyComparisonOperator.values())
        {
            this.addValidMetadataValue(operator.getDisplayName(),
                                       operator.getDescription(),
                                       OpenMetadataProperty.PROPERTY_COMPARISON_OPERATOR.name,
                                       DataType.STRING.getDisplayName(),
                                       null,
                                       null,
                                       operator.name(),
                                       operator.ordinal());
        }

        for (MatchCriteria matchCriteria : MatchCriteria.values())
        {
            this.addValidMetadataValue(matchCriteria.getDisplayName(),
                                       matchCriteria.getDescription(),
                                       OpenMetadataProperty.MATCH_CRITERIA.name,
                                       DataType.STRING.getDisplayName(),
                                       null,
                                       null,
                                       matchCriteria.name(),
                                       matchCriteria.ordinal());
        }

        for (EndMatchCriteria endMatchCriteria : EndMatchCriteria.values())
        {
            this.addValidMetadataValue(endMatchCriteria.getDisplayName(),
                                       endMatchCriteria.getDescription(),
                                       OpenMetadataProperty.END_MATCH_CRITERIA.name,
                                       DataType.STRING.getDisplayName(),
                                       null,
                                       null,
                                       endMatchCriteria.name(),
                                       endMatchCriteria.ordinal());
        }


        for (PrimitiveTypeCategory primitiveTypeCategory : PrimitiveTypeCategory.values())
        {
            this.addValidMetadataValue(primitiveTypeCategory.getDisplayName(),
                                       primitiveTypeCategory.getDescription(),
                                       OpenMetadataProperty.PRIMITIVE_TYPE_CATEGORY.name,
                                       DataType.STRING.getDisplayName(),
                                       null,
                                       null,
                                       primitiveTypeCategory.name(),
                                       primitiveTypeCategory.ordinal());
        }

        /*===========================================
         * Add the open connector framework user account/secret store enums
         */

        for (AccessOperation accessOperation : AccessOperation.values())
        {
            this.addValidMetadataValue(accessOperation.getName(),
                                       accessOperation.getDescription(),
                                       OpenMetadataProperty.OPERATION_NAME.name,
                                       DataType.STRING.getDisplayName(),
                                       null,
                                       null,
                                       accessOperation.name(),
                                       accessOperation.ordinal());
        }

        for (UserAccountStatus userAccountStatus : UserAccountStatus.values())
        {
            this.addValidMetadataValue(userAccountStatus.getName(),
                                       userAccountStatus.getDescription(),
                                       "userAccountStatus",
                                       DataType.STRING.getDisplayName(),
                                       null,
                                       null,
                                       userAccountStatus.name(),
                                       userAccountStatus.ordinal());
        }

        for (UserAccountType userAccountType : UserAccountType.values())
        {
            this.addValidMetadataValue(userAccountType.getName(),
                                       userAccountType.getDescription(),
                                       "userAccountType",
                                       DataType.STRING.getDisplayName(),
                                       null,
                                       null,
                                       userAccountType.name(),
                                       userAccountType.ordinal());
        }

        /*===========================================
         * Add the report spec enums
         */

        /*===========================================
         * Add the open metadata type enums
         */

        addOpenMetadataEnumValidNames(OpenMetadataProperty.ACTIVITY_STATUS.name,
                                      ActivityStatus.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(ActivityStatus.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.ACTIVITY_TYPE.name,
                                      ActivityType.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(ActivityType.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.ANNOTATION_STATUS.name,
                                      AnnotationStatus.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(AnnotationStatus.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.BUSINESS_CAPABILITY_TYPE.name,
                                      BusinessCapabilityType.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(BusinessCapabilityType.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.BYTE_ORDERING.name,
                                      ByteOrdering.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(ByteOrdering.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.USE_TYPE.name,
                                      CapabilityAssetUseType.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(CapabilityAssetUseType.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.MEMBERSHIP_STATUS.name,
                                      CollectionMemberStatus.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(CollectionMemberStatus.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.COMMENT_TYPE.name,
                                      CommentType.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(CommentType.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.CONTACT_METHOD_TYPE.name,
                                      ContactMethodType.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(ContactMethodType.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.CONTENT_STATUS.name,
                                      ContentStatus.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(ContentStatus.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.COVERAGE_CATEGORY.name,
                                      CoverageCategory.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(CoverageCategory.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.ROLE_TYPE.name,
                                      CrowdSourcingRole.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(CrowdSourcingRole.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.ASSIGNMENT_STATUS.name,
                                      DataValueAssignmentStatus.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(DataValueAssignmentStatus.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.SORT_ORDER.name,
                                      DataItemSortOrder.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(DataItemSortOrder.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.DELETE_METHOD.name,
                                      DeleteMethod.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(DeleteMethod.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.DEPLOYMENT_STATUS.name,
                                      DeploymentStatus.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(DeploymentStatus.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.INCIDENT_STATUS.name,
                                      IncidentReportStatus.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(IncidentReportStatus.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.KEY_PATTERN.name,
                                      KeyPattern.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(KeyPattern.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.MEDIA_TYPE.name,
                                      MediaType.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(MediaType.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.DEFAULT_MEDIA_USAGE.name,
                                      MediaUsage.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(MediaUsage.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.MEDIA_USAGE.name,
                                      MediaUsage.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(MediaUsage.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.PERMITTED_SYNCHRONIZATION.name,
                                      PermittedSynchronization.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(PermittedSynchronization.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.PORT_TYPE.name,
                                      PortType.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(PortType.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.CONTAINMENT_TYPE.name,
                                      ProcessContainmentType.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(ProcessContainmentType.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.DECORATION.name,
                                      RelationshipDecoration.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(RelationshipDecoration.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.DIRECTION.name,
                                      SolutionPortDirection.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(SolutionPortDirection.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.STARS.name,
                                      StarRating.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(StarRating.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.TERM_ASSIGNMENT_STATUS.name,
                                      TermAssignmentStatus.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(TermAssignmentStatus.values())));

        addOpenMetadataEnumValidNames(OpenMetadataProperty.TERM_RELATIONSHIP_STATUS.name,
                                      TermRelationshipStatus.getOpenTypeName(),
                                      new ArrayList<>(Arrays.asList(TermRelationshipStatus.values())));


        /*
         * Add valid metadata values for deployedImplementationType.  The GUIDs are saved in a look-up map
         * to make it easy to link other elements to these valid values later.
         */
        for (DeployedImplementationType deployedImplementationType : DeployedImplementationType.values())
        {
            this.addDeployedImplementationType(deployedImplementationType);
        }


        /*
         * Add the valid values for the assignmentType property.
         */
        for (AssignmentType assignmentType : AssignmentType.values())
        {
            this.addAssignmentType(assignmentType.getDisplayName(),
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
         * Add the connector type definitions for the connectors supplied by Egeria.
         */
        for (EgeriaOpenConnectorDefinition egeriaOpenConnectorDefinition : EgeriaOpenConnectorDefinition.values())
        {
            if (egeriaOpenConnectorDefinition.getConnectorProviderClassName() != null)
            {
                try
                {
                    Class<?> connectorProviderClass = Class.forName(egeriaOpenConnectorDefinition.getConnectorProviderClassName());
                    Object   potentialConnectorProvider = connectorProviderClass.getDeclaredConstructor().newInstance();

                    ConnectorProvider connectorProvider = (ConnectorProvider)potentialConnectorProvider;
                    archiveHelper.addConnectorType(connectorProvider);
                }
                catch (Exception error)
                {
                    System.err.println("Connector Provider Exception: " + error);
                    System.exit(-1);
                }
            }
        }

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

        /*
         * Add valid metadata values for deployedImplementationType.  The GUIDs are saved in a look-up map
         * to make it easy to link other elements to these valid values later.
         */
        for (EgeriaDeployedImplementationType deployedImplementationType : EgeriaDeployedImplementationType.values())
        {
            this.addDeployedImplementationType(deployedImplementationType);
        }

        /*
         * Add Egeria's common solution definitions
         */
        archiveHelper.addSolutionRoles(List.of(EgeriaRoleDefinition.values()));
        archiveHelper.addSolutionComponents(List.of(org.odpi.openmetadata.adapters.connectors.EgeriaSolutionComponent.values()));
        archiveHelper.addSolutionComponentActors(List.of(org.odpi.openmetadata.adapters.connectors.EgeriaSolutionComponentActor.values()));
        archiveHelper.addSolutionComponentWires(List.of(org.odpi.openmetadata.adapters.connectors.EgeriaSolutionComponentWire.values()));
        archiveHelper.addSolutionBlueprints(List.of(org.odpi.openmetadata.adapters.connectors.EgeriaSolutionBlueprint.values()));


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

        /*
         * Add information supply chains.
         */
        super.addInformationSupplyChains();

        /*
         * Create a helper process for Apache Kafka.
         */
        List<String> additionalSolutionComponents = new ArrayList<>();

        String solutionComponentGUID = this.deleteAsCatalogTargetGovernanceActionProcess("ApacheKafkaTopic",
                                                                                         DeployedImplementationType.APACHE_KAFKA_TOPIC,
                                                                                         "https://egeria-project.org/egeria-solutions/leveraging-apache-kafka/overview/",
                                                                                         RequestTypeDefinition.DELETE_KAFKA_TOPIC);

        additionalSolutionComponents.add(solutionComponentGUID);

        /*
         * Create a sample process
         */
        solutionComponentGUID = this.createDailyGovernanceActionProcess();
        additionalSolutionComponents.add(solutionComponentGUID);

        /*
         * Define the solution components for this solution.
         */
        this.addSolutionBlueprints(ContentPackDefinition.CORE_CONTENT_PACK, additionalSolutionComponents);
        this.addSolutionLinkingWires(ContentPackDefinition.CORE_CONTENT_PACK);


        /*
         * Saving the GUIDs means that the guids in the archive are stable between runs of the archive writer.
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
            Map<String, String> additionalProperties = new HashMap<>();

            additionalProperties.put(OpenMetadataProperty.IS_DEFAULT_VALUE.name, Boolean.toString(enumValue.isDefault()));
            additionalProperties.put(OpenMetadataProperty.OPEN_METADATA_TYPE_NAME.name, enumTypeName);

            super.addValidMetadataValue(null,
                                        enumValue.getDisplayName(),
                                        enumValue.getDescription(),
                                        enumConsumingProperty,
                                        enumTypeName,
                                        null,
                                        null,
                                        enumValue.name(),
                                        enumValue.getOrdinal(),
                                        enumValue.isDefault(),
                                        true,
                                        additionalProperties);
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
     * Add a new valid values record for an open metadata type.
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

        String parentSetGUID = this.getParentSet(OpenMetadataProperty.OPEN_METADATA_TYPE_NAME.name);

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
                                                                 DataType.STRING.getDisplayName(),
                                                                 OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE,
                                                                 openMetadataType.typeName,
                                                                 openMetadataType.wikiURL,
                                                                 true,
                                                                 openMetadataType.ordinal(),
                                                                 false,
                                                                 additionalProperties);

        assert(openMetadataType.descriptionGUID.equals(validValueGUID));
    }



    /**
     * Add a new valid values record for an open metadata property.
     *
     * @param openMetadataProperty preferred values
     */
    private void addOpenMetadataProperty(OpenMetadataProperty openMetadataProperty)
    {
        String parentSetGUID = this.getParentSet(OpenMetadataProperty.PROPERTY_NAME.name);

        String qualifiedName = constructValidValueQualifiedName(null,
                                                                OpenMetadataProperty.PROPERTY_NAME.name,
                                                                null,
                                                                openMetadataProperty.name);

        String validValueGUID = this.archiveHelper.addValidValue(openMetadataProperty.descriptionGUID,
                                                                 parentSetGUID,
                                                                 parentSetGUID,
                                                                 OpenMetadataType.VALID_METADATA_VALUE.typeName,
                                                                 OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                 null,
                                                                 OpenMetadataType.VALID_METADATA_VALUE.typeName,
                                                                 qualifiedName,
                                                                 Category.OPEN_METADATA_TYPES.getName(),
                                                                 OpenMetadataProperty.PROPERTY_NAME.name,
                                                                 openMetadataProperty.name,
                                                                 openMetadataProperty.description,
                                                                 null,
                                                                 openMetadataProperty.example,
                                                                 openMetadataProperty.type,
                                                                 OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE,
                                                                 openMetadataProperty.name,
                                                                 null,
                                                                 true,
                                                                 openMetadataProperty.ordinal(),
                                                                 false,
                                                                 null);

        assert(openMetadataProperty.descriptionGUID.equals(validValueGUID));
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
    private String createDailyGovernanceActionProcess()
    {
        final String qualifiedName = "Egeria::DailyGovernanceActionProcess";
        final String displayName = "Daily Governance Action Process";
        final String description = "Determines which day of the week it is today, and puts out a message on the audit log matching the assigned task for the day of the week.";
        final String url = "https://egeria-project.org/concepts/guard/";

        String processGUID = archiveHelper.addGovernanceActionProcess(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                      qualifiedName,
                                                                      displayName,
                                                                      versionName,
                                                                      description,
                                                                      url,
                                                                      null,
                                                                      0,
                                                                      null,
                                                                      null,
                                                                      null);

        String processComponentGUID = archiveHelper.addSolutionComponent(OpenMetadataType.SOLUTION_COMPONENT.typeName,
                                                                         OpenMetadataType.SOLUTION_COMPONENT.typeName + "::" + qualifiedName,
                                                                         "DAILY-PROCESS",
                                                                         displayName,
                                                                         description,
                                                                         versionName,
                                                                         SolutionComponentType.MULTI_STEP_PROCESS.getSolutionComponentType(),
                                                                         DeployedImplementationType.GOVERNANCE_ACTION_PROCESS.getDeployedImplementationType(),
                                                                         url,
                                                                         null,
                                                                         null);

        archiveHelper.addSolutionComponentActorRelationship(EgeriaRoleDefinition.OPEN_METADATA_USER.getGUID(),
                                                            processComponentGUID,
                                                            "requests daily process",
                                                            "A user wishing to run the daily process can request its execution.");

        String step1GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        null,
                                                                        qualifiedName + "::GetDayOfWeek",
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

        archiveHelper.addSolutionLinkingWireRelationship(processComponentGUID,
                                                         RequestTypeDefinition.GET_DAY_OF_WEEK.getSolutionComponentGUID(),
                                                         "step 1", "Get the data of the week.", null);


        archiveHelper.addSolutionLinkingWireRelationship(processComponentGUID,
                                                         RequestTypeDefinition.WRITE_AUDIT_LOG.getSolutionComponentGUID(),
                                                         "step 2", "Output the daily task.", null);


        String step2GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        null,
                                                                        qualifiedName + "::MondayTask",
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
                                                                        qualifiedName + "::TuesdayTask",
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
                                                                        qualifiedName + "::WednesdayTask",
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
                                                                        qualifiedName + "::ThursdayTask",
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
                                                                        qualifiedName + "::FridayTask",
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
                                                                        qualifiedName + "::SaturdayTask",
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
                                                                        qualifiedName + "::SundayTask",
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

        return processComponentGUID;
    }


    /**
     * Creates ExceptionType definitions.
     */
    private void writeExceptionTypes()
    {
        final String methodName = "writeExceptionTypes";

        for (ExceptionTypeDefinition exceptionTypeDefinition : ExceptionTypeDefinition.values())
        {
            archiveHelper.setGUID(exceptionTypeDefinition.getQualifiedName(), exceptionTypeDefinition.getGUID());

            String guid = archiveHelper.addGovernanceDefinition(OpenMetadataType.EXCEPTION_TYPE.typeName,
                                                                exceptionTypeDefinition.getQualifiedName(),
                                                                exceptionTypeDefinition.getIdentifier(),
                                                                exceptionTypeDefinition.getDisplayName(),
                                                                exceptionTypeDefinition.getSummary(),
                                                                exceptionTypeDefinition.getDescription(),
                                                                exceptionTypeDefinition.getScope().getPreferredValue(),
                                                                exceptionTypeDefinition.getDetails(),
                                                                0,
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                null);

            assert (exceptionTypeDefinition.getGUID().equals(guid));

            if (exceptionTypeDefinition.isTemplate())
            {
                archiveHelper.addTemplateClassification(exceptionTypeDefinition.getGUID(),
                                                        exceptionTypeDefinition.getTemplateName(),
                                                        exceptionTypeDefinition.getTemplateDescription(),
                                                        "V1.0",
                                                        null,
                                                        methodName);
            }
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