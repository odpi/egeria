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
 * <a href="https://egeria-project.org/types/">The Open Metadata Type System</a>
 * </p>
 * <p>
 * There are 8 areas, each covering a different topic area of metadata.  The module breaks down the process of creating
 * the models into the areas and then the individual models to simplify the maintenance of this class
 * </p>
 */
public class OpenMetadataTypesArchive2_7
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  archiveVersion     = "2.7";
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
    public OpenMetadataTypesArchive2_7()
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
    public OpenMetadataTypesArchive2_7(OMRSArchiveBuilder archiveBuilder)
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
        OpenMetadataTypesArchive2_6  previousTypes = new OpenMetadataTypesArchive2_6(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * Calls for new and changed types go here
         */
        update0045ServersAndAssets();
        update0210DataStores();
        update0462GovernanceActionFlow();
        update0463GovernanceAction();
        update0512CalculatedValue();
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0045 Replace AssetServerUse relationship with SeverAssetUse
     */
    private void update0045ServersAndAssets()
    {
        this.archiveBuilder.addTypeDefPatch(deprecateAssetServerUse());
        this.archiveBuilder.addRelationshipDef(getServerAssetUseRelationship());
    }

    private TypeDefPatch deprecateAssetServerUse()
    {
        final String typeName = "AssetServerUse";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }


    private RelationshipDef getServerAssetUseRelationship()
    {
        final String guid            = "56315447-88a6-4235-ba91-fead86524ebf";
        final String name            = "ServerAssetUse";
        final String description     = "Defines that a server capability is using an asset.";
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
        final String                     end1EntityType               = "SoftwareServerCapability";
        final String                     end1AttributeName            = "consumedIn";
        final String                     end1AttributeDescription     = "Capability consuming this asset.";
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
        final String                     end2EntityType               = "Asset";
        final String                     end2AttributeName            = "consumesAsset";
        final String                     end2AttributeDescription     = "Asset that this software server capability is dependent on.";
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

        final String attribute1Name            = "useType";
        final String attribute1Description     = "Describes how the software server capability uses the asset.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "description";
        final String attribute2Description     = "Additional information on how the asset is used by the software server capability.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getEnumTypeDefAttribute("ServerAssetUseType",
                                                         attribute1Name,
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

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0210 Add pathname to a data store.
     */
    private void update0210DataStores()
    {
        this.archiveBuilder.addTypeDefPatch(updateDataStore());
    }

    private TypeDefPatch updateDataStore()
    {
        final String typeName = "DataStore";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "pathName";
        final String attribute1Description     = "The fully qualified physical location of the data store.";
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

    /**
     * 0462 Update the GovernanceAction entity with the mandatoryGuards property
     */
    private void update0463GovernanceAction()
    {
        this.archiveBuilder.addTypeDefPatch(updateGovernanceActionEntity());
    }

    private TypeDefPatch updateGovernanceActionEntity()
    {
        final String typeName = "GovernanceAction";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "mandatoryGuards";
        final String attribute1Description     = "The list of guards that must be received before this governance action can progress.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getArrayStringTypeDefAttribute(attribute1Name,
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
     * 0462 Update the GovernanceActionFlow relationship with guard property
     */
    private void update0462GovernanceActionFlow()
    {
        this.archiveBuilder.addTypeDefPatch(updateGovernanceActionFlowRelationship());
    }

    private TypeDefPatch updateGovernanceActionFlowRelationship()
    {
        final String typeName = "GovernanceActionFlow";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "guard";
        final String attribute1Description     = "The guard passed to the first governance service to run in this process.";
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

    /**
     * 0512 Update the CalculatedValue classification to link to SchemaElement
     */
    private void update0512CalculatedValue()
    {
        this.archiveBuilder.addTypeDefPatch(updateCalculatedValueClassification());
    }

    private TypeDefPatch updateCalculatedValueClassification()
    {
        final String typeName = "CalculatedValue";
        final String linkedToEntity = "SchemaElement";

        final List<TypeDefLink> linkedToEntities = new ArrayList<>();

        linkedToEntities.add(this.archiveBuilder.getEntityDef(linkedToEntity));

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setValidEntityDefs(linkedToEntities);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


}

