/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationPropagationRule;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EnumDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EnumElementDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipEndCardinality;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipEndDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSLogicErrorException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OpenMetadataTypesArchive builds an open metadata archive containing all the standard open metadata types.
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
public class OpenMetadataTypesArchive4_0
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  archiveVersion     = "4.0";
    private static final String                  originatorName     = "Egeria";
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
    public OpenMetadataTypesArchive4_0()
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
    public OpenMetadataTypesArchive4_0(OMRSArchiveBuilder archiveBuilder)
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
     * Returns the open metadata type archive containing all the standard open metadata types.
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
        OpenMetadataTypesArchive3_15 previousTypes = new OpenMetadataTypesArchive3_15(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * Add the type updates
         */
        update0021Collections();
        update0137ToDos();
        add0220DataFileCollectionDataSet();
        add0224TableDataSet();
        add0239DeployedReportType();
        update0385ControlledGlossaries();
        update0462GovernanceActionProcesses();
        create0464DynamicIntegrationGroups();
        update0470IncidentClassifierSet();
        update0484AgreementActor();
        update0545ReferenceData();
        update0720InformationSupplyChains();
        addFormulaTypeAttribute();
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0385ControlledGlossaries()
    {
        this.archiveBuilder.addTypeDefPatch(updateControlledGlossaryTermEntity());
        this.archiveBuilder.addRelationshipDef(addGlossaryTermEvolutionRelationship());
    }


    private TypeDefPatch updateControlledGlossaryTermEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "ControlledGlossaryTerm";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "userDefinedStatus";
        final String attribute1Description     = "Extend or replace the valid instance statuses with additional statuses controlled through valid metadata values.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        /*
         * Update the valid instance statuses
         */
        ArrayList<InstanceStatus> validInstanceStatusList = new ArrayList<>();

        validInstanceStatusList.add(InstanceStatus.DRAFT);
        validInstanceStatusList.add(InstanceStatus.PREPARED);
        validInstanceStatusList.add(InstanceStatus.PROPOSED);
        validInstanceStatusList.add(InstanceStatus.APPROVED);
        validInstanceStatusList.add(InstanceStatus.REJECTED);
        validInstanceStatusList.add(InstanceStatus.ACTIVE);
        validInstanceStatusList.add(InstanceStatus.DEPRECATED);
        validInstanceStatusList.add(InstanceStatus.OTHER);
        validInstanceStatusList.add(InstanceStatus.DELETED);

        typeDefPatch.setValidInstanceStatusList(validInstanceStatusList);

        return typeDefPatch;
    }


    private RelationshipDef addGlossaryTermEvolutionRelationship()
    {
        final String guid            = "b323c9cf-f254-49c7-a391-11222e9da70f";
        final String name            = "GlossaryTermEvolution";
        final String description     = "Links a live glossary term with a future version of the term.";
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
        final String                     end1EntityType               = "ControlledGlossaryTerm";
        final String                     end1AttributeName            = "glossaryTermUpdates";
        final String                     end1AttributeDescription     = "A glossary term that contains proposed updates to the live glossary term.";
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
        final String                     end2EntityType               = "GlossaryTerm";
        final String                     end2AttributeName            = "liveGlossaryTerm";
        final String                     end2AttributeDescription     = "The approved term that is in use.";
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

        final String attribute1Name            = "description";
        final String attribute1Description     = "Short description of the update.";
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

    private void update0021Collections()
    {
        this.archiveBuilder.addEnumDef(getMembershipStatusEnum());
        this.archiveBuilder.addTypeDefPatch(updateCollectionMembershipRelationship());
    }

    private EnumDef getMembershipStatusEnum()
    {
        final String guid            = "a3bdb2ac-c28e-4e5a-8ab7-76aa01038832";
        final String name            = "MembershipStatus";
        final String description     = "Defines the provenance and confidence that a member belongs in a collection.";
        final String descriptionGUID = null;

        EnumDef enumDef = archiveHelper.getEmptyEnumDef(guid, name, description, descriptionGUID);

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        final int    element0Ordinal         = 0;
        final String element0Value           = "Unknown";
        final String element0Description     = "The membership origin is unknown.";
        final String element0DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element0Ordinal,
                                                     element0Value,
                                                     element0Description,
                                                     element0DescriptionGUID);
        elementDefs.add(elementDef);
        enumDef.setDefaultValue(elementDef);

        final int    element1Ordinal         = 1;
        final String element1Value           = "Discovered";
        final String element1Description     = "The membership was discovered by an automated process.";
        final String element1DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element1Ordinal,
                                                     element1Value,
                                                     element1Description,
                                                     element1DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element2Ordinal         = 2;
        final String element2Value           = "Assigned";
        final String element2Description     = "The membership was proposed by an expert curator.";
        final String element2DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element2Ordinal,
                                                     element2Value,
                                                     element2Description,
                                                     element2DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element3Ordinal         = 3;
        final String element3Value           = "Imported";
        final String element3Description     = "The membership was imported from another metadata system.";
        final String element3DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element3Ordinal,
                                                     element3Value,
                                                     element3Description,
                                                     element3DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element4Ordinal         = 4;
        final String element4Value           = "Validated";
        final String element4Description     = "The membership created by an automated process has been validated and approved by an expert curator.";
        final String element4DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element4Ordinal,
                                                     element4Value,
                                                     element4Description,
                                                     element4DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element5Ordinal         = 5;
        final String element5Value           = "Deprecated";
        final String element5Description     = "The membership should no longer be used.";
        final String element5DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element5Ordinal,
                                                     element5Value,
                                                     element5Description,
                                                     element5DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element6Ordinal         = 6;
        final String element6Value           = "Obsolete";
        final String element6Description     = "The membership must no longer be used.";
        final String element6DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element6Ordinal,
                                                     element6Value,
                                                     element6Description,
                                                     element6DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element99Ordinal         = 99;
        final String element99Value           = "Other";
        final String element99Description     = "Another membership status.";
        final String element99DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element99Ordinal,
                                                     element99Value,
                                                     element99Description,
                                                     element99DescriptionGUID);
        elementDefs.add(elementDef);

        enumDef.setElementDefs(elementDefs);
        return enumDef;
    }

    private TypeDefPatch updateCollectionMembershipRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "CollectionMembership";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "membershipRationale";
        final String attribute1Description     = "Description of how the member is used, or why it is useful in this collection.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "expression";
        final String attribute2Description     = "Expression describing the membership relationship.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "status";
        final String attribute3Description     = "The status of the membership relationship.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "confidence";
        final String attribute4Description     = "Level of confidence in the correctness of the membership relationship.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "steward";
        final String attribute5Description     = "Person responsible for the membership relationship.";
        final String attribute5DescriptionGUID = null;
        final String attribute6Name            = "source";
        final String attribute6Description     = "Person, organization or automated process that created the membership relationship.";
        final String attribute6DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute("MembershipStatus",
                                                         attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute4Name,
                                                        attribute4Description,
                                                        attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute5Name,
                                                           attribute5Description,
                                                           attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute6Name,
                                                           attribute6Description,
                                                           attribute6DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0137ToDos()
    {
        this.archiveBuilder.addTypeDefPatch(updateActionTargetRelationship());
    }

    private TypeDefPatch updateActionTargetRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "ActionTarget";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.STATUS.name;
        final String attribute1Description     = OpenMetadataProperty.STATUS.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.STATUS.descriptionGUID;
        final String attribute2Name            = OpenMetadataProperty.START_DATE.name;
        final String attribute2Description     = OpenMetadataProperty.START_DATE.description;
        final String attribute2DescriptionGUID = OpenMetadataProperty.START_DATE.descriptionGUID;
        final String attribute3Name            = OpenMetadataProperty.COMPLETION_DATE.name;
        final String attribute3Description     = OpenMetadataProperty.COMPLETION_DATE.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.COMPLETION_DATE.descriptionGUID;
        final String attribute4Name            = OpenMetadataProperty.ACTION_TARGET_NAME.name;
        final String attribute4Description     = OpenMetadataProperty.ACTION_TARGET_NAME.description;
        final String attribute4DescriptionGUID = OpenMetadataProperty.ACTION_TARGET_NAME.descriptionGUID;
        final String attribute5Name            = OpenMetadataProperty.COMPLETION_MESSAGE.name;
        final String attribute5Description     = OpenMetadataProperty.COMPLETION_MESSAGE.description;
        final String attribute5DescriptionGUID = OpenMetadataProperty.COMPLETION_MESSAGE.descriptionGUID;

        property = archiveHelper.getEnumTypeDefAttribute("ToDoStatus",
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
        property = archiveHelper.getStringTypeDefAttribute(attribute5Name,
                                                           attribute5Description,
                                                           attribute5DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0220DataFileCollectionDataSet()
    {
        this.archiveBuilder.addEntityDef(getDataFileCollectionDataSetEntity());

    }

    private EntityDef getDataFileCollectionDataSetEntity()
    {
        final String guid            = "962de053-ab51-40eb-b843-85b98013f5ca";
        final String name            = "DataFileCollection";
        final String description     = "A data set that consists of a collection files (do not need to be co-located).";
        final String descriptionGUID = null;

        final String superTypeName = OpenMetadataType.DATA_SET.typeName;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0224TableDataSet()
    {
        this.archiveBuilder.addEntityDef(getTableDataSetEntity());

    }

    private EntityDef getTableDataSetEntity()
    {
        final String guid            = "20c45531-5d2e-4eb6-9a47-035cb1067b82";
        final String name            = "TableDataSet";
        final String description     = "A tabular data source (typically a database table) that is an asset in its own right.";
        final String descriptionGUID = null;

        final String superTypeName = OpenMetadataType.DATA_SET.typeName;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0239DeployedReportType()
    {
        this.archiveBuilder.addEntityDef(getDeployedReportTypeEntity());

    }

    private EntityDef getDeployedReportTypeEntity()
    {
        final String guid            = "ed53a480-e6d4-44f1-aac7-3fac60bbb00e";
        final String name            = "DeployedReportType";
        final String description     = "A template for generating report.";
        final String descriptionGUID = null;

        final String superTypeName = OpenMetadataType.ASSET.typeName;

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

        final String attribute1Name            = "id";
        final String attribute1Description     = "Id of report.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "author";
        final String attribute2Description     = "Author of the report.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "url";
        final String attribute3Description     = "url of the report.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "createdTime";
        final String attribute4Description     = "Report create time.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "lastModifiedTime";
        final String attribute5Description     = "Report last modified time.";
        final String attribute5DescriptionGUID = null;
        final String attribute6Name            = "lastModifier";
        final String attribute6Description     = "Report last modifier.";
        final String attribute6DescriptionGUID = null;

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
        property = archiveHelper.getDateTypeDefAttribute(attribute4Name,
                                                         attribute4Description,
                                                         attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute5Name,
                                                         attribute5Description,
                                                         attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute6Name,
                                                           attribute6Description,
                                                           attribute6DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void update0462GovernanceActionProcesses()
    {
        this.archiveBuilder.addTypeDefPatch(update0462GovernanceActionExecutorRelationship());
    }

    private TypeDefPatch update0462GovernanceActionExecutorRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_TYPE_NAME;

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.REQUEST_PARAMETER_FILTER.name;
        final String attribute1Description     = OpenMetadataProperty.REQUEST_PARAMETER_FILTER.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.REQUEST_PARAMETER_FILTER.descriptionGUID;
        final String attribute2Name            = OpenMetadataProperty.REQUEST_PARAMETER_MAP.name;
        final String attribute2Description     = OpenMetadataProperty.REQUEST_PARAMETER_MAP.description;
        final String attribute2DescriptionGUID = OpenMetadataProperty.REQUEST_PARAMETER_MAP.descriptionGUID;
        final String attribute3Name            = OpenMetadataProperty.ACTION_TARGET_FILTER.name;
        final String attribute3Description     = OpenMetadataProperty.ACTION_TARGET_FILTER.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.ACTION_TARGET_FILTER.descriptionGUID;
        final String attribute4Name            = OpenMetadataProperty.ACTION_TARGET_MAP.name;
        final String attribute4Description     = OpenMetadataProperty.ACTION_TARGET_MAP.description;
        final String attribute4DescriptionGUID = OpenMetadataProperty.ACTION_TARGET_MAP.descriptionGUID;

        property = archiveHelper.getArrayStringTypeDefAttribute(attribute1Name,
                                                                attribute1Description,
                                                                attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute2Name,
                                                                    attribute2Description,
                                                                    attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute3Name,
                                                                    attribute3Description,
                                                                    attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute4Name,
                                                                    attribute4Description,
                                                                    attribute4DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void create0464DynamicIntegrationGroups()
    {
        this.archiveBuilder.addEntityDef(addIntegrationGroupEntity());
        this.archiveBuilder.addEntityDef(addIntegrationConnectorEntity());
        this.archiveBuilder.addEntityDef(addIntegrationReportEntity());
        this.archiveBuilder.addRelationshipDef(addRegisteredIntegrationConnectorRelationship());
        this.archiveBuilder.addRelationshipDef(addCatalogTargetRelationship());
        this.archiveBuilder.addRelationshipDef(addRelatedIntegrationReportRelationship());
    }


    private EntityDef addIntegrationGroupEntity()
    {
        final String guid            = "4d7c43ec-983b-40e4-af78-6fb66c4f5136";
        final String name            = "IntegrationGroup";
        final String description     = "A collection of integration connectors to run together.";
        final String descriptionGUID = null;

        final String superTypeName = "SoftwareServerCapability";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }

    private EntityDef addIntegrationConnectorEntity()
    {
        final String guid            = "759da11b-ebb6-4382-bdc9-72adc7c922db";
        final String name            = "IntegrationConnector";
        final String description     = "A definition to control the execution of an integration connector.";
        final String descriptionGUID = null;

        final String superTypeName = "DeployedConnector";

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

        final String attribute1Name            = "usesBlockingCalls";
        final String attribute1Description     = "The integration connector needs to use blocking calls to a third party technology and so needs to" +
                " run in its own thread.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getBooleanTypeDefAttribute(attribute1Name,
                                                            attribute1Description,
                                                            attribute1DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private EntityDef addIntegrationReportEntity()
    {
        final String guid            = "b8703d3f-8668-4e6a-bf26-27db1607220d";
        final String name            = "IntegrationReport";
        final String description     = "Details of the metadata changes made by the execution of the refresh() method by an integration connector.";
        final String descriptionGUID = null;

        final String superTypeName = OpenMetadataType.OPEN_METADATA_ROOT.typeName;

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

        final String attribute1Name            = "connectorName";
        final String attribute1Description     = "Name of the integration connector for logging purposes.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "connectorId";
        final String attribute2Description     = "Unique identifier of the integration connector deployment.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "serverName";
        final String attribute3Description     = "Name of the integration daemon where the integration connector is/was running.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "refreshStartDate";
        final String attribute4Description     = "Date/time when the refresh() call was made.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "refreshCompletionDate";
        final String attribute5Description     = "Date/time when the integration connector returned from the refresh() call.";
        final String attribute5DescriptionGUID = null;
        final String attribute6Name            = "createdElements";
        final String attribute6Description     = "List of elements that were created.";
        final String attribute6DescriptionGUID = null;
        final String attribute7Name            = "updatedElements";
        final String attribute7Description     = "List of elements that were updated.";
        final String attribute7DescriptionGUID = null;
        final String attribute8Name            = "deletedElements";
        final String attribute8Description     = "List of elements that were deleted.";
        final String attribute8DescriptionGUID = null;
        final String attribute9Name            = OpenMetadataProperty.ADDITIONAL_PROPERTIES.name;
        final String attribute9Description     = OpenMetadataProperty.ADDITIONAL_PROPERTIES.description;
        final String attribute9DescriptionGUID = OpenMetadataProperty.ADDITIONAL_PROPERTIES.descriptionGUID;


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
        property = archiveHelper.getDateTypeDefAttribute(attribute4Name,
                                                         attribute4Description,
                                                         attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute5Name,
                                                         attribute5Description,
                                                         attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute6Name,
                                                                attribute6Description,
                                                                attribute6DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute7Name,
                                                                attribute7Description,
                                                                attribute7DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute8Name,
                                                                attribute8Description,
                                                                attribute8DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute9Name,
                                                                    attribute9Description,
                                                                    attribute9DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private RelationshipDef addRegisteredIntegrationConnectorRelationship()
    {
        final String guid            = "7528bcd4-ae4c-47d0-a33f-4aeebbaa92c2";
        final String name            = "RegisteredIntegrationConnector";
        final String description     = "A link between an integration group and an integration connector that is part of the group.";
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
        final String                     end1EntityType               = "IntegrationGroup";
        final String                     end1AttributeName            = "includedInIntegrationGroups";
        final String                     end1AttributeDescription     = "An integration group that this integration connector is a member of.";
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
        final String                     end2EntityType               = "IntegrationConnector";
        final String                     end2AttributeName            = "registeredIntegrationConnectors";
        final String                     end2AttributeDescription     = "An integration connector that should run as part of the integration group.";
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

        final String attribute1Name            = "connectorName";
        final String attribute1Description     = "Name of the connector for logging purposes.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "connectorUserId";
        final String attribute2Description     = "UserId for the integration connector to use when working with open metadata.  The default userId " +
                "comes from the hosting server if this value is blank.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "metadataSourceQualifiedName";
        final String attribute3Description     = "Qualified name of a software server capability that is the owner/home of the metadata catalogued " +
                "by the integration connector.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "startDate";
        final String attribute4Description     = "Earliest time that the connector can run.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "stopTime";
        final String attribute5Description     = "Latest time that the connector can run.";
        final String attribute5DescriptionGUID = null;
        final String attribute6Name            = "refreshTimeInterval";
        final String attribute6Description     = "Describes how frequently the integration connector should run - in minutes.";
        final String attribute6DescriptionGUID = null;
        final String attribute7Name            = "permittedSynchronization";
        final String attribute7Description     = "Defines the permitted directions of flow of metadata updates between open metadata and a third party technology.";
        final String attribute7DescriptionGUID = null;
        final String attribute8Name            = "generateIntegrationReport";
        final String attribute8Description     = "Should the integration daemon create integration reports based on the integration connector's " +
                "activity? (Default is true.)";
        final String attribute8DescriptionGUID = null;

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
        property = archiveHelper.getDateTypeDefAttribute(attribute4Name,
                                                         attribute4Description,
                                                         attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute5Name,
                                                         attribute5Description,
                                                         attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getLongTypeDefAttribute(attribute6Name,
                                                         attribute6Description,
                                                         attribute6DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute("PermittedSynchronization",
                                                         attribute7Name,
                                                         attribute7Description,
                                                         attribute7DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getBooleanTypeDefAttribute(attribute8Name,
                                                            attribute8Description,
                                                            attribute8DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    private RelationshipDef addCatalogTargetRelationship()
    {
        final String guid            = "bc5a5eb1-881b-4055-aa2c-78f314282ac2";
        final String name            = "CatalogTarget";
        final String description     = "Identifies an element that an integration connector is to work with.";
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
        final String                     end1EntityType               = "IntegrationConnector";
        final String                     end1AttributeName            = "cataloguedByConnectors";
        final String                     end1AttributeDescription     = "An integration connector managing metadata synchronization.";
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
        final String                     end2EntityType               = OpenMetadataType.OPEN_METADATA_ROOT.typeName;
        final String                     end2AttributeName            = "catalogTargets";
        final String                     end2AttributeDescription     = "An open metadata element that the integration connector is working on.";
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

        final String attribute1Name            = "catalogTargetName";
        final String attribute1Description     = "Symbolic name of the catalog target to help the integration connector to choose when to use it.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef addRelatedIntegrationReportRelationship()
    {
        final String guid            = "83d12156-f8f3-4b4b-b31b-18c140df9aa3";
        final String name            = "RelatedIntegrationReport";
        final String description     = "Links an integration report to the anchor entity it describes.";
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
        final String                     end1EntityType               = OpenMetadataType.OPEN_METADATA_ROOT.typeName;
        final String                     end1AttributeName            = "anchorSubject";
        final String                     end1AttributeDescription     = "The anchor entity that the integration report describes.";
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
        final String                     end2EntityType               = "IntegrationReport";
        final String                     end2AttributeName            = "integrationReports";
        final String                     end2AttributeDescription     = "A description of the changes made to the anchor entity by an integration " +
                "report.";
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

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0545ReferenceData()
    {
        this.archiveBuilder.addTypeDefPatch(updateReferenceValueAssignment());
    }


    private TypeDefPatch updateReferenceValueAssignment()
    {
        /*
         * Create the Patch
         */
        final String typeName = "ReferenceValueAssignment";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "attributeName";
        final String attribute1Description     = "The name of the attribute that the reference data assignment represents.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void addFormulaTypeAttribute()
    {
        this.archiveBuilder.addTypeDefPatch(updateDataSet());
        this.archiveBuilder.addTypeDefPatch(updateProcess());
        this.archiveBuilder.addTypeDefPatch(updateCalculatedValue());
        this.archiveBuilder.addTypeDefPatch(updateProcessCall());
        this.archiveBuilder.addTypeDefPatch(updateDataFlow());
    }


    private TypeDefPatch updateDataSet()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.DATA_SET.typeName;

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.FORMULA_TYPE.name;
        final String attribute1Description     = OpenMetadataProperty.FORMULA_TYPE.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.FORMULA_TYPE.descriptionGUID;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    private TypeDefPatch updateProcess()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.PROCESS.typeName;

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.FORMULA_TYPE.name;
        final String attribute1Description     = OpenMetadataProperty.FORMULA_TYPE.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.FORMULA_TYPE.descriptionGUID;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    private TypeDefPatch updateCalculatedValue()
    {
        /*
         * Create the Patch
         */
        final String typeName = "CalculatedValue";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.FORMULA_TYPE.name;
        final String attribute1Description     = OpenMetadataProperty.FORMULA_TYPE.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.FORMULA_TYPE.descriptionGUID;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    private TypeDefPatch updateProcessCall()
    {
        /*
         * Create the Patch
         */
        final String typeName = "ProcessCall";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.FORMULA_TYPE.name;
        final String attribute1Description     = OpenMetadataProperty.FORMULA_TYPE.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.FORMULA_TYPE.descriptionGUID;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    private TypeDefPatch updateDataFlow()
    {
        /*
         * Create the Patch
         */
        final String typeName = "DataFlow";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.FORMULA_TYPE.name;
        final String attribute1Description     = OpenMetadataProperty.FORMULA_TYPE.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.FORMULA_TYPE.descriptionGUID;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0470IncidentClassifierSet()
    {
        this.archiveBuilder.addTypeDefPatch(updateIncidentClassifierSetClassification());
    }

    private TypeDefPatch updateIncidentClassifierSetClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "IncidentClassifierSet";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "incidentClassifierCategory";
        final String attribute1Description     = "The category of classifiers used to set the incidentClassifiers in IncidentReport.";
        final String attribute1DescriptionGUID = null;


        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0484AgreementActor()
    {
        this.archiveBuilder.addTypeDefPatch(updateAgreementActorRelationship());
    }

    private TypeDefPatch updateAgreementActorRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "AgreementActor";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setUpdateMultiLink(true);
        typeDefPatch.setMultiLink(true);

        return typeDefPatch;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0720InformationSupplyChains()
    {
        this.archiveBuilder.addRelationshipDef(getInformationSupplyChainLinkRelationship());
    }

    private RelationshipDef getInformationSupplyChainLinkRelationship()
    {
        final String guid            = "207e5130-ab7c-4048-9249-a63a43c13d60";
        final String name            = "InformationSupplyChainLink";
        final String description     = "A link between two related information supply chain segments -or to their source or destination.";
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
        final String                     end1AttributeName            = "supplyFrom";
        final String                     end1AttributeDescription     = "Logical source of the information supply chain.";
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
        final String                     end2AttributeName            = "supplyTo";
        final String                     end2AttributeDescription     = "Logical destination of an information supply chain.";
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
        final String attribute1Description     = "Description of the relationship.";
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

}

