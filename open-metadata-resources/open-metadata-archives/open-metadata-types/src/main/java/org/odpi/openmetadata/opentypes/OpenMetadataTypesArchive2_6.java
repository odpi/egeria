/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


import org.odpi.openmetadata.frameworks.openmetadata.enums.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSLogicErrorException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OpenMetadataTypesArchive builds an open metadata archive containing all of the standard open metadata types.
 * These types have hardcoded dates and guids so that however many times this archive is rebuilt, it will
 * produce the same content.
 * <p>
 * Details of the open metadata types are documented on the wiki:
 * <a href="https://egeria-project.org/types/">The Open Metadata Type System</a>
 * </p>
 * <p>
 * There are 8 areas, each covering a different topic area of metadata.  The module breaks down the process of creating
 * the models into the areas and then the individual models to simplify the maintenance of this class
 * </p>
 */
public class OpenMetadataTypesArchive2_6
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  archiveVersion     = "2.6";
    private static final String                  originatorName     = "ODPi Egeria";
    private static final String                  originatorLicense  = "Apache-2.0";
    private static final Date                    creationDate       = new Date(1588261366992L);

    /*
     * Specific values for initializing TypeDefs
     */
    private static final long   versionNumber = 1L;
    private static final String versionName   = "1.0";


    private final OMRSArchiveBuilder archiveBuilder;
    private final OMRSArchiveHelper  archiveHelper;

    /**
     * Default constructor sets up the archive builder.  This in turn sets up the header for the archive.
     */
    public OpenMetadataTypesArchive2_6()
    {
        this.archiveBuilder = new OMRSArchiveBuilder(archiveGUID,
                                                     archiveName,
                                                     archiveDescription,
                                                     archiveType,
                                                     archiveVersion,
                                                     originatorName,
                                                     originatorLicense,
                                                     creationDate,
                                                     null);

        this.archiveHelper = new OMRSArchiveHelper(archiveBuilder,
                                                   archiveGUID,
                                                   originatorName,
                                                   creationDate,
                                                   versionNumber,
                                                   versionName);
    }


    /**
     * Chained constructor sets up the archive builder.  This in turn sets up the header for the archive.
     *
     * @param archiveBuilder accumulator for types
     */
    public OpenMetadataTypesArchive2_6(OMRSArchiveBuilder archiveBuilder)
    {
        this.archiveBuilder = archiveBuilder;

        this.archiveHelper = new OMRSArchiveHelper(archiveBuilder,
                                                   archiveGUID,
                                                   originatorName,
                                                   creationDate,
                                                   versionNumber,
                                                   versionName);
    }


    /**
     * Return the unique identifier for this archive.
     *
     * @return String guid
     */
    public String getArchiveGUID()
    {
        return archiveGUID;
    }


    /**
     * Returns the open metadata type archive containing all of the standard open metadata types.
     *
     * @return populated open metadata archive object
     */
    public OpenMetadataArchive getOpenMetadataArchive()
    {
        final String methodName = "getOpenMetadataArchive";

        if (this.archiveBuilder != null)
        {
            /*
             * Build the type archive.
             */
            this.getOriginalTypes();

            /*
             * The completed archive is ready to be packaged up and returned
             */
            return this.archiveBuilder.getOpenMetadataArchive();
        }
        else
        {
            /*
             * This is a logic error since it means the creation of the archive builder threw an exception
             * in the constructor and so this object should not be used.
             */
            throw new OMRSLogicErrorException(OMRSErrorCode.ARCHIVE_UNAVAILABLE.getMessageDefinition(),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Add the types from this archive to the archive builder supplied in the
     * constructor.
     */
    public void getOriginalTypes()
    {
        OpenMetadataTypesArchive2_5  previousTypes = new OpenMetadataTypesArchive2_5(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * Calls for new and changed types go here
         */
        update0010ManagingMemento();
        update0215MoreProcessTypes();
        update0422GovernanceActionClassifications();
        update0445GovernanceRoles();
        update0460GovernanceExecutionPoints();
        add0461GovernanceActionEngines();
        add0462GovernanceActionProcesses();
        add0463EngineActions();
        add0465DuplicateProcessing();
        add0470IncidentReports();
        add0550InstanceMetadata();
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0010 Add the Memento classification
     */
    private void update0010ManagingMemento()
    {
        this.archiveBuilder.addClassificationDef(addMementoClassification());
    }

    private ClassificationDef addMementoClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.MEMENTO_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.OPEN_METADATA_ROOT.typeName),
                                                                                 true);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.ARCHIVE_DATE.name;
        final String attribute1Description     = OpenMetadataProperty.ARCHIVE_DATE.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.ARCHIVE_DATE.descriptionGUID;
        final String attribute2Name            = OpenMetadataProperty.ARCHIVE_USER.name;
        final String attribute2Description     = OpenMetadataProperty.ARCHIVE_USER.description;
        final String attribute2DescriptionGUID = OpenMetadataProperty.ARCHIVE_USER.descriptionGUID;
        final String attribute3Name            = OpenMetadataProperty.ARCHIVE_PROCESS.name;
        final String attribute3Description     = OpenMetadataProperty.ARCHIVE_PROCESS.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.ARCHIVE_PROCESS.descriptionGUID;
        final String attribute4Name            = OpenMetadataProperty.ARCHIVE_SERVICE.name;
        final String attribute4Description     = OpenMetadataProperty.ARCHIVE_SERVICE.description;
        final String attribute4DescriptionGUID = OpenMetadataProperty.ARCHIVE_SERVICE.descriptionGUID;
        final String attribute5Name            = OpenMetadataProperty.ARCHIVE_METHOD.name;
        final String attribute5Description     = OpenMetadataProperty.ARCHIVE_METHOD.description;
        final String attribute5DescriptionGUID = OpenMetadataProperty.ARCHIVE_METHOD.descriptionGUID;
        final String attribute6Name            = OpenMetadataProperty.ARCHIVE_PROPERTIES.name;
        final String attribute6Description     = OpenMetadataProperty.ARCHIVE_PROPERTIES.description;
        final String attribute6DescriptionGUID = OpenMetadataProperty.ARCHIVE_PROPERTIES.descriptionGUID;

        property = archiveHelper.getDateTypeDefAttribute(attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute3Name,
                                                           attribute3Description,
                                                           attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
                                                           attribute4Description,
                                                           attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute5Name,
                                                           attribute5Description,
                                                           attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute6Name,
                                                                    attribute6Description,
                                                                    attribute6DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0010 Add the EmbeddedProcess and TransientEmbeddedProcess entities
     */
    private void update0215MoreProcessTypes()
    {
        this.archiveBuilder.addEntityDef(addEmbeddedProcessEntity());
        this.archiveBuilder.addEntityDef(addTransientEmbeddedProcessEntity());
    }


    private EntityDef addEmbeddedProcessEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.EMBEDDED_PROCESS,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.PROCESS.typeName));
        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PROCESS_START_TIME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PROCESS_END_TIME));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private EntityDef addTransientEmbeddedProcessEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.TRANSIENT_EMBEDDED_PROCESS,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.EMBEDDED_PROCESS.typeName));
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0422 Add the Impact classification
     */
    private void update0422GovernanceActionClassifications()
    {
        this.archiveBuilder.addEnumDef(getImpactSeverityEnum());
        this.archiveBuilder.addClassificationDef(addImpactClassification());
    }

    private EnumDef getImpactSeverityEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(ImpactSeverity.getOpenTypeGUID(),
                                                        ImpactSeverity.getOpenTypeName(),
                                                        ImpactSeverity.getOpenTypeDescription(),
                                                        ImpactSeverity.getOpenTypeDescriptionGUID(),
                                                        ImpactSeverity.getOpenTypeDescriptionWiki());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (ImpactSeverity enumValues : ImpactSeverity.values())
        {
            elementDef = archiveHelper.getEnumElementDef(enumValues.getOrdinal(),
                                                         enumValues.getName(),
                                                         enumValues.getDescription(),
                                                         enumValues.getDescriptionGUID());

            elementDefs.add(elementDef);

            if (enumValues.isDefault())
            {
                enumDef.setDefaultValue(elementDef);
            }
        }

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }

    private ClassificationDef addImpactClassification()
    {
        final String guid            = "3a6c4ba7-3cc5-48cd-8952-a50a92da016d";
        final String name            = "Impact";
        final String description     = "Defines the severity of a situation on the attach entity.";
        final String descriptionGUID = null;

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                                 true);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "status";
        final String attribute1Description     = "Status of this classification.";
        final String attribute1DescriptionGUID = null;
        final String attribute6Name            = "level";
        final String attribute6Description     = "Level of severity associated with this classification";
        final String attribute6DescriptionGUID = null;
        final String attribute7Name            = "levelIdentifier";
        final String attribute7Description     = "Defined level for this classification.";
        final String attribute7DescriptionGUID = null;

        property = archiveHelper.getEnumTypeDefAttribute(GovernanceClassificationStatus.getOpenTypeName(),
                                                         attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(OpenMetadataProperty.CONFIDENCE.name,
                                                        OpenMetadataProperty.CONFIDENCE.description,
                                                        OpenMetadataProperty.CONFIDENCE.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.STEWARD.name,
                                                           OpenMetadataProperty.STEWARD.description,
                                                           OpenMetadataProperty.STEWARD.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.SOURCE.name,
                                                           OpenMetadataProperty.SOURCE.description,
                                                           OpenMetadataProperty.SOURCE.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.NOTES.name,
                                                           OpenMetadataProperty.NOTES.description,
                                                           OpenMetadataProperty.NOTES.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute(ImpactSeverity.getOpenTypeName(),
                                                         attribute6Name,
                                                         attribute6Description,
                                                         attribute6DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute7Name,
                                                        attribute7Description,
                                                        attribute7DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0445 Add the AssetOwnerType classification
     */
    private void update0445GovernanceRoles()
    {
        this.archiveBuilder.addEnumDef(getOwnerTypeEnum());
    }

    private EnumDef getOwnerTypeEnum()
    {
        final String guid            = "5ce92a70-b86a-4e0d-a9d7-fc961121de97";
        final String name            = "OwnerType";
        final String description     = "Defines the type of identifier for a governance owner.";
        final String descriptionGUID = null;

        EnumDef enumDef = archiveHelper.getEmptyEnumDef(guid, name, description, descriptionGUID);

        List<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef       elementDef;

        final int    element1Ordinal         = 0;
        final String element1Value           = "UserId";
        final String element1Description     = "The owner's userId is specified (default).";
        final String element1DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element1Ordinal,
                                                     element1Value,
                                                     element1Description,
                                                     element1DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element2Ordinal         = 1;
        final String element2Value           = "ProfileId";
        final String element2Description     = "The unique identifier (guid) of the profile of the owner.";
        final String element2DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element2Ordinal,
                                                     element2Value,
                                                     element2Description,
                                                     element2DescriptionGUID);
        elementDefs.add(elementDef);


        final int    element99Ordinal         = 99;
        final String element99Value           = "Other";
        final String element99Description     = "Another type of owner identifier, probably not supported by open metadata.";
        final String element99DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element99Ordinal,
                                                     element99Value,
                                                     element99Description,
                                                     element99DescriptionGUID);
        elementDefs.add(elementDef);

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */


    public void update0460GovernanceExecutionPoints()
    {
        this.archiveBuilder.addEntityDef(addExecutionPointDefinitionEntity());
        this.archiveBuilder.addEntityDef(addControlPointDefinitionEntity());
        this.archiveBuilder.addEntityDef(addVerificationPointDefinitionEntity());
        this.archiveBuilder.addEntityDef(addEnforcementPointDefinitionEntity());

        this.archiveBuilder.addRelationshipDef(addExecutionPointUseRelationship());

        this.archiveBuilder.addTypeDefPatch(updateControlPointClassification());
        this.archiveBuilder.addTypeDefPatch(updateVerificationPointClassification());
        this.archiveBuilder.addTypeDefPatch(updateEnforcementPointClassification());

    }

    private EntityDef addExecutionPointDefinitionEntity()
    {
        /*
         * Build the Entity
         */
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.EXECUTION_POINT_DEFINITION,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DISPLAY_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private EntityDef addControlPointDefinitionEntity()
    {
        /*
         * Build the Entity
         */
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.CONTROL_POINT_DEFINITION,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.EXECUTION_POINT_DEFINITION.typeName));
    }


    private EntityDef addVerificationPointDefinitionEntity()
    {
        /*
         * Build the Entity
         */
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.VERIFICATION_POINT_DEFINITION,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.EXECUTION_POINT_DEFINITION.typeName));
    }

    private EntityDef addEnforcementPointDefinitionEntity()
    {
        /*
         * Build the Entity
         */
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.ENFORCEMENT_POINT_DEFINITION,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.EXECUTION_POINT_DEFINITION.typeName));
    }

    private RelationshipDef addExecutionPointUseRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.EXECUTION_POINT_USE_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "supportsGovernanceDefinitions";
        final String                     end1AttributeDescription     = "Governance definition that is implemented by this execution point.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_DEFINITION.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "executedThrough";
        final String                     end2AttributeDescription     = "Description of the execution points that support the implementation of this governance definition.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.EXECUTION_POINT_DEFINITION.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private TypeDefPatch updateControlPointClassification()
    {
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.CONTROL_POINT_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.QUALIFIED_NAME));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    private TypeDefPatch updateVerificationPointClassification()
    {
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.VERIFICATION_POINT_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.QUALIFIED_NAME));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    private TypeDefPatch updateEnforcementPointClassification()
    {
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.ENFORCEMENT_POINT_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.QUALIFIED_NAME));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0461 Describe Governance Action Engines and Services
     */
    private void add0461GovernanceActionEngines()
    {
        this.archiveBuilder.addEntityDef(addGovernanceEngineEntity());
        this.archiveBuilder.addEntityDef(addGovernanceServiceEntity());
        this.archiveBuilder.addEntityDef(addGovernanceActionEngineEntity());
        this.archiveBuilder.addEntityDef(addGovernanceActionServiceEntity());

        this.archiveBuilder.addRelationshipDef(addSupportedGovernanceServiceRelationship());
    }

    private EntityDef addGovernanceEngineEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.GOVERNANCE_ENGINE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName));
    }

    private EntityDef addGovernanceServiceEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.GOVERNANCE_SERVICE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DEPLOYED_CONNECTOR.typeName));
    }

    private EntityDef addGovernanceActionEngineEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.GOVERNANCE_ACTION_ENGINE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ENGINE.typeName));
    }

    private EntityDef addGovernanceActionServiceEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.GOVERNANCE_ACTION_SERVICE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_SERVICE.typeName));
    }

    private RelationshipDef addSupportedGovernanceServiceRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "calledFromGovernanceEngines";
        final String                     end1AttributeDescription     = "Governance Engine making use of the governance service.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ENGINE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "supportedGovernanceServices";
        final String                     end2AttributeDescription     = "Governance service that is part of the governance engine.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_SERVICE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REQUEST_TYPE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REQUEST_PARAMETERS));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0462 Describe Governance Action Process Steps
     */
    private void add0462GovernanceActionProcesses()
    {
        this.archiveBuilder.addEntityDef(addGovernanceActionProcessEntity());
        this.archiveBuilder.addEntityDef(addGovernanceActionTypeEntity());
        this.archiveBuilder.addEntityDef(addGovernanceActionProcessStepEntity());

        this.archiveBuilder.addRelationshipDef(addGovernanceActionProcessFlowRelationship());
        this.archiveBuilder.addRelationshipDef(addNextGovernanceActionProcessStepRelationship());
        this.archiveBuilder.addRelationshipDef(addGovernanceActionExecutorRelationship());
    }

    private EntityDef addGovernanceActionProcessEntity()
    {
        /*
         * Build the Entity
         */
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.GOVERNANCE_ACTION_PROCESS,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.PROCESS.typeName));
    }


    private EntityDef addGovernanceActionTypeEntity()
    {
        /*
         * Build the Entity
         */
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.GOVERNANCE_ACTION_TYPE,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DOMAIN_IDENTIFIER));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DISPLAY_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.WAIT_TIME));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private EntityDef addGovernanceActionProcessStepEntity()
    {
        /*
         * Build the Entity
         */
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ACTION_TYPE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.IGNORE_MULTIPLE_TRIGGERS));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private RelationshipDef addGovernanceActionProcessFlowRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_FLOW_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "triggeredFrom";
        final String                     end1AttributeDescription     = "Governance process that describes the set of process steps.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "firstStep";
        final String                     end2AttributeDescription     = "First step to execute in a governance action process.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private RelationshipDef addNextGovernanceActionProcessStepRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.NEXT_GOVERNANCE_ACTION_PROCESS_STEP_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "dependedOnProcessSteps";
        final String                     end1AttributeDescription     = "Governance Action Process Step caller.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "followOnProcessSteps";
        final String                     end2AttributeDescription     = "Governance Action Process Step called.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.GUARD));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MANDATORY_GUARD));


        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    private RelationshipDef addGovernanceActionExecutorRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "supportsGovernanceActions";
        final String                     end1AttributeDescription     = "Governance action that drives calls to a governance engine.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ACTION_TYPE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "governanceActionExecutor";
        final String                     end2AttributeDescription     = "Governance engine that will run the requested action.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ENGINE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REQUEST_TYPE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REQUEST_PARAMETERS));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0463EngineActions()
    {
        this.archiveBuilder.addEnumDef(getEngineActionStatusEnum());

        this.archiveBuilder.addEntityDef(addEngineActionEntity());

        this.archiveBuilder.addRelationshipDef(addEngineActionRequestSourceRelationship());
        this.archiveBuilder.addRelationshipDef(addTargetForActionRelationship());
        this.archiveBuilder.addRelationshipDef(addNextEngineActionRelationship());
    }

    private EnumDef getEngineActionStatusEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(EngineActionStatus.getOpenTypeGUID(),
                                                        EngineActionStatus.getOpenTypeName(),
                                                        EngineActionStatus.getOpenTypeDescription(),
                                                        EngineActionStatus.getOpenTypeDescriptionGUID(),
                                                        EngineActionStatus.getOpenTypeDescriptionWiki());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (EngineActionStatus enumValues : EngineActionStatus.values())
        {
            elementDef = archiveHelper.getEnumElementDef(enumValues.getOrdinal(),
                                                         enumValues.getName(),
                                                         enumValues.getDescription(),
                                                         enumValues.getDescriptionGUID());

            elementDefs.add(elementDef);

            if (enumValues.isDefault())
            {
                enumDef.setDefaultValue(elementDef);
            }
        }

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }

    private EntityDef addEngineActionEntity()
    {
        /*
         * Build the Entity
         */
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.ENGINE_ACTION,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DOMAIN_IDENTIFIER));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DISPLAY_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PROCESSING_ENGINE_USER_ID));
        properties.add(archiveHelper.getEnumTypeDefAttribute(OpenMetadataProperty.ACTION_STATUS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.START_DATE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.COMPLETION_DATE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.COMPLETION_GUARDS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.RECEIVED_GUARDS));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private RelationshipDef addEngineActionRequestSourceRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.ENGINE_ACTION_REQUEST_SOURCE,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "sourceActivity";
        final String                     end1AttributeDescription     = "Element(s) that caused this engine action to be created.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.OPEN_METADATA_ROOT.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "identifiedActions";
        final String                     end2AttributeDescription     = "Engine actions that were initiated for the linked element.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ENGINE_ACTION.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ORIGIN_GOVERNANCE_SERVICE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ORIGIN_GOVERNANCE_ENGINE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REQUEST_SOURCE_NAME));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    private RelationshipDef addTargetForActionRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.TARGET_FOR_ACTION,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "identifiedEngineActions";
        final String                     end1AttributeDescription     = "Engine action that is acting on this element.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ENGINE_ACTION.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "actionTarget";
        final String                     end2AttributeDescription     = "Element(s) to work on.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getEnumTypeDefAttribute(OpenMetadataProperty.ACTION_STATUS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.START_DATE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.COMPLETION_DATE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ACTION_TARGET_NAME));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    private RelationshipDef addNextEngineActionRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.NEXT_ENGINE_ACTION,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "previousActions";
        final String                     end1AttributeDescription     = "Engine action that triggered this engine action.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ENGINE_ACTION.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "followOnActions";
        final String                     end2AttributeDescription     = "Engine action(s) that should run next.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ENGINE_ACTION.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.GUARD));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MANDATORY_GUARD));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * Add types for duplicate processing
     */
    public void add0465DuplicateProcessing()
    {
        this.archiveBuilder.addEnumDef(addDuplicateTypeEnum());
        this.archiveBuilder.addClassificationDef(addKnowDuplicateClassification());
    }


    /**
     * Define DuplicateTypeEnum.
     *
     * @return enum def
     */
    private EnumDef addDuplicateTypeEnum()
    {
        final String guid            = "2f6a3dc1-aa98-4b92-add4-68de53b7369c";
        final String name            = "DuplicateType";
        final String description     = "Defines if the duplicates are peers or one is a consolidated duplicate.";
        final String descriptionGUID = null;

        EnumDef enumDef = archiveHelper.getEmptyEnumDef(guid, name, description, descriptionGUID);

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        final int    element1Ordinal         = 0;
        final String element1Value           = "Peer";
        final String element1Description     = "The duplicates are peers.";
        final String element1DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element1Ordinal,
                                                     element1Value,
                                                     element1Description,
                                                     element1DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element2Ordinal         = 1;
        final String element2Value           = "Consolidated";
        final String element2Description     = "One duplicate has been constructed from the other (ands its peers).";
        final String element2DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element2Ordinal,
                                                     element2Value,
                                                     element2Description,
                                                     element2DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element99Ordinal         = 99;
        final String element99Value           = "Other";
        final String element99Description     = "Another duplicate type.";
        final String element99DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element99Ordinal,
                                                     element99Value,
                                                     element99Description,
                                                     element99DescriptionGUID);
        elementDefs.add(elementDef);

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }

    private ClassificationDef addKnowDuplicateClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION,
                                                  null,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                  true);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * Add incident reports
     */
    public void add0470IncidentReports()
    {
        this.archiveBuilder.addEnumDef(getIncidentReportStatusEnum());

        this.archiveBuilder.addEntityDef(addIncidentClassifierEntity());
        this.archiveBuilder.addEntityDef(addIncidentReportEntity());

        this.archiveBuilder.addRelationshipDef(addIncidentOriginatorRelationship());
        this.archiveBuilder.addRelationshipDef(addImpactedResourceRelationship());
        this.archiveBuilder.addRelationshipDef(addIncidentDependencyRelationship());
    }


    private EnumDef getIncidentReportStatusEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(IncidentReportStatus.getOpenTypeGUID(),
                                                        IncidentReportStatus.getOpenTypeName(),
                                                        IncidentReportStatus.getOpenTypeDescription(),
                                                        IncidentReportStatus.getOpenTypeDescriptionGUID(),
                                                        IncidentReportStatus.getOpenTypeDescriptionWiki());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (CriticalityLevel enumValues : CriticalityLevel.values())
        {
            elementDef = archiveHelper.getEnumElementDef(enumValues.getOrdinal(),
                                                         enumValues.getName(),
                                                         enumValues.getDescription(),
                                                         enumValues.getDescriptionGUID());

            elementDefs.add(elementDef);

            if (enumValues.isDefault())
            {
                enumDef.setDefaultValue(elementDef);
            }
        }

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }

    private EntityDef addIncidentClassifierEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = "1fad7fe4-5115-412b-ae31-a418e93888fe";
        final String name            = "IncidentClassifier";
        final String description     = "A definition of a classifier used to label incident reports.";
        final String descriptionGUID = null;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                description,
                                                                descriptionGUID);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "classifierLabel";
        final String attribute1Description     = "Label to add to the incident.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "classifierIdentifier";
        final String attribute2Description     = "Unique identifier for the classifier associated with the classifier label.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "classifierName";
        final String attribute3Description     = "Display name for the classifier identifier.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "classifierDescription";
        final String attribute4Description     = "Description of the meaning of the classifier identifier.";
        final String attribute4DescriptionGUID = null;


        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute2Name,
                                                        attribute2Description,
                                                        attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute3Name,
                                                           attribute3Description,
                                                           attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
                                                           attribute4Description,
                                                           attribute4DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private EntityDef addIncidentReportEntity()
    {
        /*
         * Build the Entity
         */
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.INCIDENT_REPORT,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;


        final String attribute2Name            = "background";
        final String attribute2Description     = "Description of the background cause or activity.";
        final String attribute2DescriptionGUID = null;
        final String attribute4Name            = "owner";
        final String attribute4Description     = "Person, team or engine responsible for this incident.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "ownerType";
        final String attribute5Description     = "Type of element representing the owner.";
        final String attribute5DescriptionGUID = null;
        final String attribute6Name            = "incidentStatus";
        final String attribute6Description     = "Current lifecycle state of the incident report.";
        final String attribute6DescriptionGUID = null;
        final String attribute7Name            = OpenMetadataProperty.START_DATE.name;
        final String attribute7Description     = OpenMetadataProperty.START_DATE.description;
        final String attribute7DescriptionGUID = OpenMetadataProperty.START_DATE.descriptionGUID;
        final String attribute8Name            = OpenMetadataProperty.COMPLETION_DATE.name;
        final String attribute8Description     = OpenMetadataProperty.COMPLETION_DATE.description;
        final String attribute8DescriptionGUID = OpenMetadataProperty.COMPLETION_DATE.descriptionGUID;
        final String attribute9Name            = "incidentClassifiers";
        final String attribute9Description     = "Map of label to level indicator to provide customizable grouping of incidents.";
        final String attribute9DescriptionGUID = null;

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DOMAIN_IDENTIFIER));
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
                                                           attribute4Description,
                                                           attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute("AssetOwnerType",
                                                         attribute5Name,
                                                         attribute5Description,
                                                         attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute("IncidentReportStatus",
                                                         attribute6Name,
                                                         attribute6Description,
                                                         attribute6DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute7Name,
                                                         attribute7Description,
                                                         attribute7DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute8Name,
                                                         attribute8Description,
                                                         attribute8DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringIntTypeDefAttribute(attribute9Name,
                                                                 attribute9Description,
                                                                 attribute9DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private RelationshipDef addIncidentOriginatorRelationship()
    {
        final String guid            = "e490772e-c2c5-445a-aea6-1aab3499a76c";
        final String name            = "IncidentOriginator";
        final String description     = "Link between an incident report and its originator (person, process, engine, ...).";
        final String descriptionGUID = null;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "originators";
        final String                     end1AttributeDescription     = "Source(s) of the incident report.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "IncidentReport";
        final String                     end2AttributeName            = "resultingIncidentReports";
        final String                     end2AttributeDescription     = "Descriptions of detected incidents.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private RelationshipDef addImpactedResourceRelationship()
    {
        final String guid            = "0908e153-e0fd-499c-8a30-5ea8b81395cd";
        final String name            = "ImpactedResource";
        final String description     = "Link between an impacted referenceable and an incident report.";
        final String descriptionGUID = null;


        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "impactedResources";
        final String                     end1AttributeDescription     = "Resources impacted by the incident.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "IncidentReport";
        final String                     end2AttributeName            = "incidentReports";
        final String                     end2AttributeDescription     = "Descriptions of incidents affecting this resource and the action taken.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "severityLevelIdentifier";
        final String attribute1Description     = "How severe is the impact on the resource?";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getIntTypeDefAttribute(attribute1Name,
                                                        attribute1Description,
                                                        attribute1DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    private RelationshipDef addIncidentDependencyRelationship()
    {
        final String guid            = "017be6a8-0037-49d8-af5d-c45c41f25e0b";
        final String name            = "IncidentDependency";
        final String description     = "Link between an incident report and its predecessors.";
        final String descriptionGUID = null;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "IncidentReport";
        final String                     end1AttributeName            = "priorReportedIncidents";
        final String                     end1AttributeDescription     = "Previous reports on the same or related incident.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "IncidentReport";
        final String                     end2AttributeName            = "followOnReportedIncidents";
        final String                     end2AttributeDescription     = "Subsequent reports on the same or related incident.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * Add instance metadata classification
     */
    public void add0550InstanceMetadata()
    {
        this.archiveBuilder.addClassificationDef(addInstanceMetadataClassification());
    }

    private ClassificationDef addInstanceMetadataClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.INSTANCE_METADATA_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ELEMENT.typeName),
                                                                                 true);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.INSTANCE_METADATA_TYPE_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ADDITIONAL_PROPERTIES));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */


}

