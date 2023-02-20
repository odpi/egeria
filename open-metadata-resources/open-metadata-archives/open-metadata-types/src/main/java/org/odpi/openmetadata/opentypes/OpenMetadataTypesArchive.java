/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
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
public class OpenMetadataTypesArchive
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
    private static final String                  originatorLicense  = "Apache 2.0";
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
    public OpenMetadataTypesArchive()
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
    public OpenMetadataTypesArchive(OMRSArchiveBuilder archiveBuilder)
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
        update0462GovernanceActionType();
        update0470IncidentClassifierSet();
        update0484AgreementActor();
        update0720InformationSupplyChains();
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

        final String attribute1Name            = "status";
        final String attribute1Description     = "The status of the work on this element.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "startDate";
        final String attribute2Description     = "Date/time that work started on this element.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "completionDate";
        final String attribute3Description     = "Date/time that work stopped on this element.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "actionTargetName";
        final String attribute4Description     = "The name to identify the action target to the actor that processes it.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "completionMessage";
        final String attribute5Description     = "Message to provide additional information on the results of acting on the target by the actor or the reasons for any failures.";
        final String attribute5DescriptionGUID = null;

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

        final String superTypeName = "DataSet";

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

        final String superTypeName = "DataSet";

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

        final String superTypeName = "Asset";

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


    private void update0462GovernanceActionType()
    {
        this.archiveBuilder.addTypeDefPatch(update0462GovernanceActionTypeExecutorRelationship());
    }

    private TypeDefPatch update0462GovernanceActionTypeExecutorRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "GovernanceActionTypeExecutor";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "requestParameterFilter";
        final String attribute1Description     = "Which requestParameters to remove before calling governance action.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "requestParameterMap";
        final String attribute2Description     = "The request parameters to rename before calling the governance action. Map is from old name to new name.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "actionTargetFilter";
        final String attribute3Description     = "Which actionTargets to remove before calling governance action.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "actionTargetMap";
        final String attribute4Description     = "The action target to rename before calling the governance action. Map is from old name to new name.";
        final String attribute4DescriptionGUID = null;

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
        final String                     end1EntityType               = "Referenceable";
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
        final String                     end2EntityType               = "Referenceable";
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

