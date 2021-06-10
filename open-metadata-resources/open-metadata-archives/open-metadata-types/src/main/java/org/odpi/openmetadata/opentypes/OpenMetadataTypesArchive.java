/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


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
 * <a href="https://egeria.odpi.org/open-metadata-publication/website/open-metadata-types/">The Open Metadata Type System</a>
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
    private static final String                  archiveVersion     = "2.11";
    private static final String                  originatorName     = "Egeria";
    private static final String                  originatorLicense  = "Apache 2.0";
    private static final Date                    creationDate       = new Date(1588261366992L);

    /*
     * Specific values for initializing TypeDefs
     */
    private static final long   versionNumber = 1L;
    private static final String versionName   = "1.0";


    private OMRSArchiveBuilder archiveBuilder;
    private OMRSArchiveHelper  archiveHelper;

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
        OpenMetadataTypesArchive2_10 previousTypes = new OpenMetadataTypesArchive2_10(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * Calls for new and changed types go here
         */
        update0010BaseModel();
        update04xxGovernanceDefinitions();
        update03Subject();
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * Mark deprecated field in Asset
     */
    private void update0010BaseModel()
    {
        this.archiveBuilder.addTypeDefPatch(updateAsset());
    }

    /**
     * Deprecate the ownership properties - use Ownership classification instead
     *
     * @return patch
     */
    private TypeDefPatch updateAsset()
    {
        /*
         * Create the Patch
         */
        final String typeName = "Asset";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "owner";
        final String attribute1Description     = "Deprecated Attribute. Person, team or engine responsible for this type of action. Use Ownership classification";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "ownerType";
        final String attribute2Description     = "Deprecated Attribute. Type of element representing the owner. Use Ownership classification";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "zoneMembership";
        final String attribute3Description     = "Deprecated Attribute. The list of zones that this asset belongs to. Use AssetZoneMembership classification";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "latestChange";
        final String attribute4Description     = "Deprecated Attribute. Description of the last change to the asset's metadata. Use LatestChange classification";
        final String attribute4DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute("AssetOwnerType",
                                                         attribute2Name,
                                                         attribute2Description,
                                                         attribute2DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute3Name,
                                                                attribute3Description,
                                                                attribute3DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
                                                           attribute4Description,
                                                           attribute4DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * A variety of changes to improve consistency and flexibility of the governance definitions
     */
    private void update04xxGovernanceDefinitions()
    {

        this.archiveBuilder.addEntityDef(addThreatEntity());

        this.archiveBuilder.addRelationshipDef(addGovernanceDefinitionScopeRelationship());

    }


    /**
     * This relationships allows the scope of a governance definition to be set up.
     *
     * @return  relationship definition
     */
    private RelationshipDef addGovernanceDefinitionScopeRelationship()
    {
        final String guid            = "3845b5cc-8c85-462f-b7e6-47472a568793";
        final String name            = "GovernanceDefinitionScope";
        final String description     = "Link between a scope - such as a digital service, infrastructure element or organization - and a governance definition.";
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
        final String                     end1AttributeName            = "definitionAppliesTo";
        final String                     end1AttributeDescription     = "Elements defining the scope that the governance definition applies to.";
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
        final String                     end2EntityType               = "GovernanceDefinition";
        final String                     end2AttributeName            = "associatedGovernanceDefinitions";
        final String                     end2AttributeDescription     = "Governance definitions for this scope.";
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



    /**
     * This new subtype of governance driver is to document a specific threat.
     *
     * @return entity definition
     */
    private EntityDef addThreatEntity()
    {
        final String guid            = "4ca51fdf-9b70-46b1-bdf6-8860429e78d8";
        final String name            = "Threat";
        final String description     = "A description of a specific threat.";
        final String descriptionGUID = null;

        final String superTypeName = "GovernanceDriver";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);

    }
    private void update03Subject()
    {
        this.archiveBuilder.addRelationshipDef(addObjectInheritance());
        this.archiveBuilder.addTypeDefPatch(updateIsaTypeOf());
    }

    /**
     * Defines an inheritance relationship between two spine objects. It provides a type for a Spine Object.
     * @return RelationshipDef
     */
    private RelationshipDef addObjectInheritance()
        {
            final String guid            = "9b6a91b5-a339-4245-b208-040805f95a75";
            final String name            = "ObjectInheritance";
            final String description     = "Defines an inheritance relationship between two spine objects.";
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
            final String                     end1EntityType               = "GlossaryTerm";
            final String                     end1AttributeName            = "Inherited";
            final String                     end1AttributeDescription     = "Inherited (Subtypes) for this object.";
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
            final String                     end2AttributeName            = "InheritedFrom";
            final String                     end2AttributeDescription     = "Inherited from type (Supertypes) for this object.";
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
            final String attribute2Name            = "status";
            final String attribute2Description     = "The status of or confidence in the relationship.";
            final String attribute2DescriptionGUID = null;
            final String attribute3Name            = "steward";
            final String attribute3Description     = "Person responsible for the relationship.";
            final String attribute3DescriptionGUID = null;
            final String attribute4Name            = "source";
            final String attribute4Description     = "Person, organization or automated process that created the relationship.";
            final String attribute4DescriptionGUID = null;

            property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                               attribute1Description,
                                                               attribute1DescriptionGUID);
            properties.add(property);
            property = archiveHelper.getEnumTypeDefAttribute("TermRelationshipStatus",
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

            relationshipDef.setPropertiesDefinition(properties);

            return relationshipDef;

    }
    /**
     * Deprecate the TermISATypeOFRelationship - use TermTypeOFRelationship
     *
     * @return patch
     */
    private TypeDefPatch updateIsaTypeOf()
    {
        final String typeName = "TermISATypeOFRelationship";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }
}

