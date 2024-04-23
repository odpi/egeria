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
        update06xxDiscoveryEnginesAndServices();
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
        final String guid            = OpenMetadataType.MEMENTO_CLASSIFICATION.typeGUID;
        final String name            = OpenMetadataType.MEMENTO_CLASSIFICATION.typeName;
        final String description     = OpenMetadataType.MEMENTO_CLASSIFICATION.description;
        final String descriptionGUID = OpenMetadataType.MEMENTO_CLASSIFICATION.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.MEMENTO_CLASSIFICATION.wikiURL;

        final String linkedToEntity = OpenMetadataType.OPEN_METADATA_ROOT.typeName;

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 descriptionWiki,
                                                                                 this.archiveBuilder.getEntityDef(linkedToEntity),
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
        /*
         * Build the Entity
         */
        final String guid            = "8145967e-bb83-44b2-bc8c-68112c6a5a06";
        final String name            = "EmbeddedProcess";
        final String description     = "A child process.";
        final String descriptionGUID = null;
        final String superTypeName   = OpenMetadataType.PROCESS.typeName;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef addTransientEmbeddedProcessEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = "9bd9d37a-b2ae-48ec-9776-080f667e91c5";
        final String name            = "TransientEmbeddedProcess";
        final String description     = "A child process that runs for a short period of time.";
        final String descriptionGUID = null;
        final String superTypeName   = "EmbeddedProcess";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
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

        final String linkedToEntity = OpenMetadataType.REFERENCEABLE.typeName;

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(linkedToEntity),
                                                                                 true);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "status";
        final String attribute1Description     = "Status of this classification.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "confidence";
        final String attribute2Description     = "Level of confidence in the classification (0=none -> 100=excellent).";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "steward";
        final String attribute3Description     = "Person responsible for maintaining this classification.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "source";
        final String attribute4Description     = "Source of the classification.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "notes";
        final String attribute5Description     = "Information relating to the classification.";
        final String attribute5DescriptionGUID = null;
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
        property = archiveHelper.getStringTypeDefAttribute(attribute5Name,
                                                           attribute5Description,
                                                           attribute5DescriptionGUID);
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
        final String guid            = "d7f8d1d2-8cec-4fd2-b9fd-c8307cad750d";
        final String name            = "ExecutionPointDefinition";
        final String description     = "A description of an activity that supports the implementation of a governance requirement.";
        final String descriptionGUID = null;
        final String superTypeName   = OpenMetadataType.REFERENCEABLE.typeName;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(superTypeName),
                                                                description,
                                                                descriptionGUID);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "displayName";
        final String attribute1Description     = "Short name for display and reports.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "description";
        final String attribute2Description     = "Description of the execution point.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private EntityDef addControlPointDefinitionEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = "a376a993-5f1c-4926-b74e-a15a38e1d55a";
        final String name            = "ControlPointDefinition";
        final String description     = "A decision needs to be made on how to proceed.";
        final String descriptionGUID = null;
        final String superTypeName   = "ExecutionPointDefinition";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }

    private EntityDef addVerificationPointDefinitionEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = "27db26a1-ff66-4042-9932-ddc728b977b9";
        final String name            = "VerificationPointDefinition";
        final String description     = "A test is made to ensure the current situation is valid.";
        final String descriptionGUID = null;
        final String superTypeName   = "ExecutionPointDefinition";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }

    private EntityDef addEnforcementPointDefinitionEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = "e87ff806-bb9c-4c5d-8106-f38f2dd21037";
        final String name            = "EnforcementPointDefinition";
        final String description     = "A change is made to enforce a governance requirement.";
        final String descriptionGUID = null;
        final String superTypeName   = "ExecutionPointDefinition";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }

    private RelationshipDef addExecutionPointUseRelationship()
    {
        final String guid            = "3eb268f4-9419-4281-a487-d25ccd88eba3";
        final String name            = "ExecutionPointUse";
        final String description     = "Link between a governance execution point definition and the governance definition it supports.";
        final String descriptionGUID = null;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "GovernanceDefinition";
        final String                     end1AttributeName            = "supportsGovernanceDefinitions";
        final String                     end1AttributeDescription     = "Governance definition that is implemented by this execution point.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "ExecutionPointDefinition";
        final String                     end2AttributeName            = "executedThrough";
        final String                     end2AttributeDescription     = "Description of the execution points that support the implementation of this governance definition.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private TypeDefPatch updateControlPointClassification()
    {
        final String typeName = "ControlPoint";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.QUALIFIED_NAME.name;
        final String attribute1Description     = "Qualified name of the enforcement point definition.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getIntTypeDefAttribute(attribute1Name,
                                                        attribute1Description,
                                                        attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    private TypeDefPatch updateVerificationPointClassification()
    {
        final String typeName = "VerificationPoint";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.QUALIFIED_NAME.name;
        final String attribute1Description     = "Qualified name of the enforcement point definition.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getIntTypeDefAttribute(attribute1Name,
                                                        attribute1Description,
                                                        attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    private TypeDefPatch updateEnforcementPointClassification()
    {
        final String typeName = "EnforcementPoint";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.QUALIFIED_NAME.name;
        final String attribute1Description     = "Qualified name of the enforcement point definition.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getIntTypeDefAttribute(attribute1Name,
                                                        attribute1Description,
                                                        attribute1DescriptionGUID);
        properties.add(property);

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
        /*
         * Build the Entity
         */
        final String guid            = OpenMetadataType.GOVERNANCE_ENGINE.typeGUID;
        final String name            = OpenMetadataType.GOVERNANCE_ENGINE.typeName;
        final String description     = OpenMetadataType.GOVERNANCE_ENGINE.description;
        final String descriptionGUID = OpenMetadataType.GOVERNANCE_ENGINE.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.GOVERNANCE_ENGINE.wikiURL;

        final String superTypeName   = OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID,
                                                 descriptionWiki);
    }

    private EntityDef addGovernanceServiceEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = OpenMetadataType.GOVERNANCE_SERVICE.typeGUID;
        final String name            = OpenMetadataType.GOVERNANCE_SERVICE.typeName;
        final String description     = OpenMetadataType.GOVERNANCE_SERVICE.description;
        final String descriptionGUID = OpenMetadataType.GOVERNANCE_SERVICE.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.GOVERNANCE_SERVICE.wikiURL;

        final String superTypeName   = OpenMetadataType.DEPLOYED_CONNECTOR_TYPE_NAME;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID,
                                                 descriptionWiki);
    }

    private EntityDef addGovernanceActionEngineEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeGUID;
        final String name            = OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName;
        final String description     = OpenMetadataType.GOVERNANCE_ACTION_ENGINE.description;
        final String descriptionGUID = OpenMetadataType.GOVERNANCE_ACTION_ENGINE.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.GOVERNANCE_ACTION_ENGINE.wikiURL;

        final String superTypeName   = OpenMetadataType.GOVERNANCE_ENGINE.typeName;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID,
                                                 descriptionWiki);
    }

    private EntityDef addGovernanceActionServiceEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = OpenMetadataType.GOVERNANCE_ACTION_SERVICE.typeGUID;
        final String name            = OpenMetadataType.GOVERNANCE_ACTION_SERVICE.typeName;
        final String description     = OpenMetadataType.GOVERNANCE_ACTION_SERVICE.description;
        final String descriptionGUID = OpenMetadataType.GOVERNANCE_ACTION_SERVICE.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.GOVERNANCE_ACTION_SERVICE.wikiURL;

        final String superTypeName   = OpenMetadataType.GOVERNANCE_SERVICE.typeName;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID,
                                                 descriptionWiki);
    }

    private RelationshipDef addSupportedGovernanceServiceRelationship()
    {
        final String guid            = OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeGUID;
        final String name            = OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeName;
        final String description     = OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.description;
        final String descriptionGUID = OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.wikiURL;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                descriptionWiki,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = OpenMetadataType.GOVERNANCE_ENGINE.typeName;
        final String                     end1AttributeName            = "calledFromGovernanceEngines";
        final String                     end1AttributeDescription     = "Governance Engine making use of the governance service.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = OpenMetadataType.GOVERNANCE_SERVICE.typeName;
        final String                     end2AttributeName            = "supportedGovernanceServices";
        final String                     end2AttributeDescription     = "Governance service that is part of the governance engine.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.REQUEST_TYPE.name;
        final String attribute1Description     = OpenMetadataProperty.REQUEST_TYPE.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.REQUEST_TYPE.descriptionGUID;
        final String attribute2Name            = OpenMetadataProperty.REQUEST_PARAMETERS.name;
        final String attribute2Description     = OpenMetadataProperty.REQUEST_PARAMETERS.description;
        final String attribute2DescriptionGUID = OpenMetadataProperty.REQUEST_PARAMETERS.descriptionGUID;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute2Name,
                                                                    attribute2Description,
                                                                    attribute2DescriptionGUID);
        properties.add(property);

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
        final String guid            = "4d3a2b8d-9e2e-4832-b338-21c74e45b238";
        final String name            = "GovernanceActionProcess";
        final String description     = "A process implemented by chained engine actions that call governance services.";
        final String descriptionGUID = null;
        final String superTypeName   = OpenMetadataType.PROCESS.typeName;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef addGovernanceActionTypeEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = OpenMetadataType.GOVERNANCE_ACTION_TYPE_TYPE_GUID;
        final String name            = OpenMetadataType.GOVERNANCE_ACTION_TYPE_TYPE_NAME;
        final String description     = "A description of a call to a governance engine that acts as a template when creating the appropriate engine action instance.";
        final String descriptionGUID = null;
        final String superTypeName   = OpenMetadataType.REFERENCEABLE.typeName;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(superTypeName),
                                                                description,
                                                                descriptionGUID);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "domainIdentifier";
        final String attribute1Description     = "Identifier used to show which governance domain this action type belongs to.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = OpenMetadataProperty.DISPLAY_NAME.name;
        final String attribute2Description     = OpenMetadataProperty.DISPLAY_NAME.description;
        final String attribute2DescriptionGUID = OpenMetadataProperty.DISPLAY_NAME.descriptionGUID;
        final String attribute3Name            = OpenMetadataProperty.DESCRIPTION.name;
        final String attribute3Description     = OpenMetadataProperty.DESCRIPTION.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.DESCRIPTION.descriptionGUID;
        final String attribute4Name            = "waitTime";
        final String attribute4Description     = "The minimum number of minutes that the governance engine should wait before calling the governance service.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "supportedGuards";
        final String attribute5Description     = "List of guards that this action type supports.";
        final String attribute5DescriptionGUID = null;

        property = archiveHelper.getIntTypeDefAttribute(attribute1Name,
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
        property = archiveHelper.getIntTypeDefAttribute(attribute4Name,
                                                        attribute4Description,
                                                        attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute5Name,
                                                                attribute5Description,
                                                                attribute5DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private EntityDef addGovernanceActionProcessStepEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_GUID;
        final String name            = OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME;
        final String description     = "A description of a call to a step in a governance action process that acts as a template when creating the appropriate engine action instance.";
        final String descriptionGUID = null;
        final String superTypeName   = OpenMetadataType.GOVERNANCE_ACTION_TYPE_TYPE_NAME;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(superTypeName),
                                                                description,
                                                                descriptionGUID);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "ignoreMultipleTriggers";
        final String attribute1Description     = "Trigger one or many engine action instances?";
        final String attribute1DescriptionGUID = null;


        property = archiveHelper.getBooleanTypeDefAttribute(attribute1Name,
                                                            attribute1Description,
                                                            attribute1DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private RelationshipDef addGovernanceActionProcessFlowRelationship()
    {
        final String guid            = "5f6ddee5-31ea-4d4f-9c3f-00ad2fcb2aa0";
        final String name            = "GovernanceActionProcessFlow";
        final String description     = "A link between a governance action process and its first process step.";
        final String descriptionGUID = null;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "GovernanceActionProcess";
        final String                     end1AttributeName            = "triggeredFrom";
        final String                     end1AttributeDescription     = "Governance process that describes the set of process steps.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "GovernanceActionProcessStep";
        final String                     end2AttributeName            = "firstStep";
        final String                     end2AttributeDescription     = "First step to execute in a governance action process.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.AT_MOST_ONE;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private RelationshipDef addNextGovernanceActionProcessStepRelationship()
    {
        final String guid            = "d9567840-9904-43a5-990b-4585c0446e00";
        final String name            = "NextGovernanceActionProcessStep";
        final String description     = "Link between a process steps in a governance action process flow.";
        final String descriptionGUID = null;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "GovernanceActionProcessStep";
        final String                     end1AttributeName            = "dependedOnProcessSteps";
        final String                     end1AttributeDescription     = "Governance Action Process Step caller.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "GovernanceActionProcessStep";
        final String                     end2AttributeName            = "followOnProcessSteps";
        final String                     end2AttributeDescription     = "Governance Action Process Step called.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "guard";
        final String attribute1Description     = "The guard that is returned by the previous step that triggers this next step to run.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "mandatoryGuard";
        final String attribute2Description     = "Is this guard mandatory for the next step to run.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getBooleanTypeDefAttribute(attribute2Name,
                                                            attribute2Description,
                                                            attribute2DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    private RelationshipDef addGovernanceActionExecutorRelationship()
    {
        final String guid            = OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_TYPE_GUID;
        final String name            = OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_TYPE_NAME;
        final String description     = "Link between a governance action type, or process step, and the governance engine that will execute it.";
        final String descriptionGUID = null;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = OpenMetadataType.GOVERNANCE_ACTION_TYPE_TYPE_NAME;
        final String                     end1AttributeName            = "supportsGovernanceActions";
        final String                     end1AttributeDescription     = "Governance action that drives calls to a governance engine.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = OpenMetadataType.GOVERNANCE_ENGINE.typeName;
        final String                     end2AttributeName            = "governanceActionExecutor";
        final String                     end2AttributeDescription     = "Governance engine that will run the requested action.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.AT_MOST_ONE;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "requestType";
        final String attribute1Description     = "The request type used to call the service.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "requestParameters";
        final String attribute2Description     = "Properties that configure the governance service for this type of request.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute2Name,
                                                                    attribute2Description,
                                                                    attribute2DescriptionGUID);
        properties.add(property);

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
        this.archiveBuilder.addRelationshipDef(addGovernanceActionProcessStepUseRelationship());
        this.archiveBuilder.addRelationshipDef(addTargetForActionRelationship());
        this.archiveBuilder.addRelationshipDef(addNextEngineActionRelationship());
        this.archiveBuilder.addRelationshipDef(addEngineActionExecutorRelationship());
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
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.ENGINE_ACTION.typeGUID,
                                                                OpenMetadataType.ENGINE_ACTION.typeName,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                OpenMetadataType.ENGINE_ACTION.description,
                                                                OpenMetadataType.ENGINE_ACTION.descriptionGUID,
                                                                OpenMetadataType.ENGINE_ACTION.wikiURL);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "domainIdentifier";
        final String attribute1Description     = "Identifier used to show which governance domain this action belongs to.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "displayName";
        final String attribute2Description     = "Name of the engine action.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "description";
        final String attribute3Description     = "Description of the engine action.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "processingEngineUserId";
        final String attribute4Description     = "Governance engine responsible for this engine action.";
        final String attribute4DescriptionGUID = null;
        final String attribute6Name            = "actionStatus";
        final String attribute6Description     = "Current lifecycle state of the engine action.";
        final String attribute6DescriptionGUID = null;
        final String attribute7Name            = "startDate";
        final String attribute7Description     = "Date and time when the governance service started running.";
        final String attribute7DescriptionGUID = null;
        final String attribute8Name            = "completionDate";
        final String attribute8Description     = "Date and time when the governance service completed.";
        final String attribute8DescriptionGUID = null;
        final String attribute9Name            = "completionGuards";
        final String attribute9Description     = "List of guards returned by the governance service.";
        final String attribute9DescriptionGUID = null;
        final String attribute10Name            = "receivedGuards";
        final String attribute10Description     = "List of guards received from the previous governance service(s).";
        final String attribute10DescriptionGUID = null;

        property = archiveHelper.getIntTypeDefAttribute(attribute1Name,
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
        property = archiveHelper.getEnumTypeDefAttribute(EngineActionStatus.getOpenTypeName(),
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
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute9Name,
                                                                attribute9Description,
                                                                attribute9DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute10Name,
                                                                attribute10Description,
                                                                attribute10DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private RelationshipDef addEngineActionRequestSourceRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.ENGINE_ACTION_REQUEST_SOURCE.typeGUID,
                                                                                OpenMetadataType.ENGINE_ACTION_REQUEST_SOURCE.typeName,
                                                                                null,
                                                                                OpenMetadataType.ENGINE_ACTION_REQUEST_SOURCE.description,
                                                                                OpenMetadataType.ENGINE_ACTION_REQUEST_SOURCE.descriptionGUID,
                                                                                OpenMetadataType.ENGINE_ACTION_REQUEST_SOURCE.wikiURL,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = OpenMetadataType.OPEN_METADATA_ROOT.typeName;
        final String                     end1AttributeName            = "sourceActivity";
        final String                     end1AttributeDescription     = "Element(s) that caused this engine action to be created.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = OpenMetadataType.ENGINE_ACTION.typeName;
        final String                     end2AttributeName            = "identifiedActions";
        final String                     end2AttributeDescription     = "Engine actions that were initiated for the linked element.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "originGovernanceService";
        final String attribute1Description     = "The qualifiedName of the governance service that caused the engine action to be created.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "originGovernanceEngine";
        final String attribute2Description     = "The qualifiedName of the governance engine that caused the engine action to be created.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "requestSourceName";
        final String attribute3Description     = "The name to identify the request source to the governance service that processes it.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
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

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    private RelationshipDef addGovernanceActionProcessStepUseRelationship()
    {
        final String guid            = "31e734ec-5baf-4e96-9f0d-e8a85081cb14";
        final String name            = "GovernanceActionProcessStepUse";
        final String description     = "Link between a governance action process step and a resulting engine action.";
        final String descriptionGUID = null;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "GovernanceActionProcessStep";
        final String                     end1AttributeName            = "fromProcessStepTemplate";
        final String                     end1AttributeDescription     = "Governance action process step that was the template for this engine action.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.AT_MOST_ONE;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "EngineAction";
        final String                     end2AttributeName            = "spawnedActions";
        final String                     end2AttributeDescription     = "Engine actions that were created from this process step template.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "originGovernanceService";
        final String attribute1Description     = "The qualifiedName of the governance service that caused the engine action to be created.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "originGovernanceEngine";
        final String attribute2Description     = "The qualifiedName of the governance engine that caused the engine action to be created.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    private RelationshipDef addTargetForActionRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.TARGET_FOR_ACTION.typeGUID,
                                                                                OpenMetadataType.TARGET_FOR_ACTION.typeName,
                                                                                null,
                                                                                OpenMetadataType.TARGET_FOR_ACTION.description,
                                                                                OpenMetadataType.TARGET_FOR_ACTION.descriptionGUID,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = OpenMetadataType.ENGINE_ACTION.typeName;
        final String                     end1AttributeName            = "identifiedEngineActions";
        final String                     end1AttributeDescription     = "Engine action that is acting on this element.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = OpenMetadataType.REFERENCEABLE.typeName;
        final String                     end2AttributeName            = "actionTarget";
        final String                     end2AttributeDescription     = "Element(s) to work on.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "status";
        final String attribute1Description     = "The status of the work on this element.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "startDate";
        final String attribute2Description     = "Date/time that work started on this element for the linked governance action.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "completionDate";
        final String attribute3Description     = "Date/time that work stopped on this element for the linked governance action.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "actionTargetName";
        final String attribute4Description     = "The name to identify the action target to the governance service that processes it.";
        final String attribute4DescriptionGUID = null;

        property = archiveHelper.getEnumTypeDefAttribute(EngineActionStatus.getOpenTypeName(),
                                                         attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute2Name,
                                                         attribute2Description,
                                                         attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
                                                           attribute4Description,
                                                           attribute4DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    private RelationshipDef addNextEngineActionRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.NEXT_ENGINE_ACTION.typeGUID,
                                                                                OpenMetadataType.NEXT_ENGINE_ACTION.typeName,
                                                                                null,
                                                                                OpenMetadataType.NEXT_ENGINE_ACTION.description,
                                                                                OpenMetadataType.NEXT_ENGINE_ACTION.descriptionGUID,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = OpenMetadataType.ENGINE_ACTION.typeName;
        final String                     end1AttributeName            = "previousActions";
        final String                     end1AttributeDescription     = "Engine action that triggered this engine action.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = OpenMetadataType.ENGINE_ACTION.typeName;
        final String                     end2AttributeName            = "followOnActions";
        final String                     end2AttributeDescription     = "Engine action(s) that should run next.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "guard";
        final String attribute1Description     = "The guard that is returned by the previous action that means this next action will run.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "mandatoryGuard";
        final String attribute2Description     = "Is this guard mandatory for the next action to run.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getBooleanTypeDefAttribute(attribute2Name,
                                                            attribute2Description,
                                                            attribute2DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    private RelationshipDef addEngineActionExecutorRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.ENGINE_ACTION_EXECUTOR.typeGUID,
                                                                                OpenMetadataType.ENGINE_ACTION_EXECUTOR.typeName,
                                                                                null,
                                                                                OpenMetadataType.ENGINE_ACTION_EXECUTOR.description,
                                                                                OpenMetadataType.ENGINE_ACTION_EXECUTOR.descriptionGUID,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = OpenMetadataType.ENGINE_ACTION.typeName;
        final String                     end1AttributeName            = "supportsEngineAction";
        final String                     end1AttributeDescription     = "Engine action that drives a governance engine.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = OpenMetadataType.GOVERNANCE_ENGINE.typeName;
        final String                     end2AttributeName            = "governanceActionExecutor";
        final String                     end2AttributeDescription     = "Governance engine that will run the governance action.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.AT_MOST_ONE;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "requestType";
        final String attribute1Description     = "The request type used to call the service.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "requestParameters";
        final String attribute2Description     = "Properties that configure the governance service for this type of request.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute2Name,
                                                                    attribute2Description,
                                                                    attribute2DescriptionGUID);
        properties.add(property);

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
        this.archiveBuilder.addRelationshipDef(addKnownDuplicateLinkRelationship());

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
        final String guid            = "e55062b2-907f-44bd-9831-255642285731";
        final String name            = "KnownDuplicate";
        final String description     = "Defines that duplicate resolution processing is required.";
        final String descriptionGUID = null;

        final String linkedToEntity = OpenMetadataType.REFERENCEABLE.typeName;

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(linkedToEntity),
                                                  true);
    }

    private RelationshipDef addKnownDuplicateLinkRelationship()
    {
        final String guid            = "7540d9fb-1848-472e-baef-97a44b9f0c45";
        final String name            = "KnownDuplicateLink";
        final String description     = "Link between duplicate entities.";
        final String descriptionGUID = null;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = OpenMetadataType.REFERENCEABLE.typeName;
        final String                     end1AttributeName            = "knownDuplicateOrigin";
        final String                     end1AttributeDescription     = "Oldest element.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = OpenMetadataType.REFERENCEABLE.typeName;
        final String                     end2AttributeName            = "knownDuplicatePartner";
        final String                     end2AttributeDescription     = "Newest element.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "duplicateType";
        final String attribute1Description     = "Type of duplicate.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "status";
        final String attribute2Description     = "Status of the duplicate processing.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "steward";
        final String attribute3Description     = "Person responsible for maintaining this classification.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "source";
        final String attribute4Description     = "Source of the classification.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "notes";
        final String attribute5Description     = "Information relating to the classification.";
        final String attribute5DescriptionGUID = null;

        property = archiveHelper.getEnumTypeDefAttribute("DuplicateType",
                                                         attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute("GovernanceClassificationStatus",
                                                         attribute2Name,
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

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
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
        final String superTypeName   = OpenMetadataType.REFERENCEABLE.typeName;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(superTypeName),
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
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.INCIDENT_REPORT.typeGUID,
                                                                OpenMetadataType.INCIDENT_REPORT.typeName,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                OpenMetadataType.INCIDENT_REPORT.description,
                                                                OpenMetadataType.INCIDENT_REPORT.descriptionGUID,
                                                                OpenMetadataType.INCIDENT_REPORT.wikiURL);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "domainIdentifier";
        final String attribute1Description     = "Identifier used to show which governance domain this incident belongs to.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "background";
        final String attribute2Description     = "Description of the background cause or activity.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = OpenMetadataProperty.DESCRIPTION.name;
        final String attribute3Description     = OpenMetadataProperty.DESCRIPTION.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.DESCRIPTION.descriptionGUID;
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


        property = archiveHelper.getIntTypeDefAttribute(attribute1Name,
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

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = OpenMetadataType.REFERENCEABLE.typeName;
        final String                     end1AttributeName            = "originators";
        final String                     end1AttributeDescription     = "Source(s) of the incident report.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "IncidentReport";
        final String                     end2AttributeName            = "resultingIncidentReports";
        final String                     end2AttributeDescription     = "Descriptions of detected incidents.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private RelationshipDef addImpactedResourceRelationship()
    {
        final String guid            = "0908e153-e0fd-499c-8a30-5ea8b81395cd";
        final String name            = "ImpactedResource";
        final String description     = "Link between an impacted referenceable and an incident report.";
        final String descriptionGUID = null;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = OpenMetadataType.REFERENCEABLE.typeName;
        final String                     end1AttributeName            = "impactedResources";
        final String                     end1AttributeDescription     = "Resources impacted by the incident.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "IncidentReport";
        final String                     end2AttributeName            = "incidentReports";
        final String                     end2AttributeDescription     = "Descriptions of incidents affecting this resource and the action taken.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
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

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "IncidentReport";
        final String                     end1AttributeName            = "priorReportedIncidents";
        final String                     end1AttributeDescription     = "Previous reports on the same or related incident.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "IncidentReport";
        final String                     end2AttributeName            = "followOnReportedIncidents";
        final String                     end2AttributeDescription     = "Subsequent reports on the same or related incident.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "description";
        final String attribute1Description     = "Description of the dependency.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
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
        final String guid            = OpenMetadataType.INSTANCE_METADATA_CLASSIFICATION.typeGUID;
        final String name            = OpenMetadataType.INSTANCE_METADATA_CLASSIFICATION.typeName;
        final String description     = OpenMetadataType.INSTANCE_METADATA_CLASSIFICATION.description;
        final String descriptionGUID = OpenMetadataType.INSTANCE_METADATA_CLASSIFICATION.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.INSTANCE_METADATA_CLASSIFICATION.wikiURL;

        final String linkedToEntity = OpenMetadataType.SCHEMA_ELEMENT_TYPE_NAME;

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 descriptionWiki,
                                                                                 this.archiveBuilder.getEntityDef(linkedToEntity),
                                                                                 true);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.TYPE_NAME.name;
        final String attribute1Description     = OpenMetadataProperty.TYPE_NAME.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.TYPE_NAME.descriptionGUID;
        final String attribute2Name            = OpenMetadataProperty.DESCRIPTION.name;
        final String attribute2Description     = OpenMetadataProperty.DESCRIPTION.description;
        final String attribute2DescriptionGUID = OpenMetadataProperty.DESCRIPTION.descriptionGUID;
        final String attribute3Name            = OpenMetadataProperty.ADDITIONAL_PROPERTIES.name;
        final String attribute3Description     = OpenMetadataProperty.ADDITIONAL_PROPERTIES.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.ADDITIONAL_PROPERTIES.descriptionGUID;


        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute3Name,
                                                                    attribute3Description,
                                                                    attribute3DescriptionGUID);

        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * Update the discovery engines.
     */
    private void update06xxDiscoveryEnginesAndServices()
    {
        this.archiveBuilder.addTypeDefPatch(updateOpenDiscoveryEngine());
        this.archiveBuilder.addTypeDefPatch(updateOpenDiscoveryService());

    }

    /**
     * Update the engine
     *
     * @return type def patch
     */
    private TypeDefPatch updateOpenDiscoveryEngine()
    {
        final String typeName = OpenMetadataType.OPEN_DISCOVERY_ENGINE.typeName;

        final String superTypeName = OpenMetadataType.GOVERNANCE_ENGINE.typeName;

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(superTypeName));

        return typeDefPatch;
    }


    /**
     * Update the service.
     *
     * @return typedef patch
     */
    private TypeDefPatch updateOpenDiscoveryService()
    {
        final String typeName = OpenMetadataType.OPEN_DISCOVERY_SERVICE.typeName;

        final String superTypeName = OpenMetadataType.GOVERNANCE_SERVICE.typeName;

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(superTypeName));

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


}

