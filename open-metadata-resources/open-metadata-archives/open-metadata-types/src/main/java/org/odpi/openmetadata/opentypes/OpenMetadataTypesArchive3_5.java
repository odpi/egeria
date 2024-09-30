/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationPropagationRule;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipEndCardinality;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipEndDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefStatus;
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
public class OpenMetadataTypesArchive3_5
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  archiveVersion     = "3.5";
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
    public OpenMetadataTypesArchive3_5()
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
    public OpenMetadataTypesArchive3_5(OMRSArchiveBuilder archiveBuilder)
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
        OpenMetadataTypesArchive3_4 previousTypes = new OpenMetadataTypesArchive3_4(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * Calls for new and changed types go here
         */
        update0041ServerPurpose();
        update00420045SoftwareCapabilities();
        extend0112Person();
        update0130Projects();
        update0140Communities();
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * Allow software capabilities to be supported by any type of IT Infrastructure - not just SoftwareServers
     */
    private void update00420045SoftwareCapabilities()
    {
        this.archiveBuilder.addEntityDef(getSoftwareCapabilityEntity());
        this.archiveBuilder.addRelationshipDef(getSupportedSoftwareCapabilityRelationship());
        this.archiveBuilder.addTypeDefPatch(deprecateSoftwareServerSupportedCapabilityRelationship());
        this.archiveBuilder.addTypeDefPatch(updateSoftwareServerCapabilityEntity());
        this.archiveBuilder.addTypeDefPatch(updateFileSystemClassification());
        this.archiveBuilder.addTypeDefPatch(updateFileManagerClassification());
        this.archiveBuilder.addTypeDefPatch(updateNotificationManagerClassification());
        this.archiveBuilder.addClassificationDef(addSourceControlLibraryClassification());
        this.archiveBuilder.addClassificationDef(addChangeManagementLibraryClassification());
        this.archiveBuilder.addClassificationDef(addSoftwareLibraryClassification());
        this.archiveBuilder.addTypeDefPatch(updateDatabaseManagerClassification());
        this.archiveBuilder.addTypeDefPatch(updateContentCollectionManagerClassification());
        this.archiveBuilder.addTypeDefPatch(updateCloudServiceClassification());
        this.archiveBuilder.addTypeDefPatch(updateServerAssetUseRelationship());
        this.archiveBuilder.addTypeDefPatch(updateSoftwareServerDeploymentRelationship());
    }


    private EntityDef getSoftwareCapabilityEntity()
    {
        final String guid            = OpenMetadataType.SOFTWARE_CAPABILITY.typeGUID;
        final String name            = OpenMetadataType.SOFTWARE_CAPABILITY.typeName;
        final String description     = OpenMetadataType.SOFTWARE_CAPABILITY.description;
        final String descriptionGUID = OpenMetadataType.SOFTWARE_CAPABILITY.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.SOFTWARE_CAPABILITY.wikiURL;

        final String superTypeName = OpenMetadataType.REFERENCEABLE.typeName;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(superTypeName),
                                                                description,
                                                                descriptionGUID,
                                                                descriptionWiki);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute3Name            = OpenMetadataProperty.CAPABILITY_TYPE.name;
        final String attribute3Description     = OpenMetadataProperty.CAPABILITY_TYPE.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.CAPABILITY_TYPE.descriptionGUID;
        final String attribute4Name            = OpenMetadataProperty.CAPABILITY_VERSION.name;
        final String attribute4Description     = OpenMetadataProperty.CAPABILITY_VERSION.description;
        final String attribute4DescriptionGUID = OpenMetadataProperty.CAPABILITY_VERSION.descriptionGUID;
        final String attribute5Name            = OpenMetadataProperty.PATCH_LEVEL.name;
        final String attribute5Description     = OpenMetadataProperty.PATCH_LEVEL.description;
        final String attribute5DescriptionGUID = OpenMetadataProperty.PATCH_LEVEL.descriptionGUID;
        final String attribute7Name            = OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name;
        final String attribute7Description     = OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.description;
        final String attribute7DescriptionGUID = OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.descriptionGUID;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.NAME.name,
                                                           OpenMetadataProperty.NAME.description,
                                                           OpenMetadataProperty.NAME.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
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
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.SOURCE.name,
                                                           OpenMetadataProperty.SOURCE.description,
                                                           OpenMetadataProperty.SOURCE.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute7Name,
                                                           attribute7Description,
                                                           attribute7DescriptionGUID);

        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private RelationshipDef getSupportedSoftwareCapabilityRelationship()
    {
        final String guid            = OpenMetadataType.SUPPORTED_CAPABILITY_RELATIONSHIP.typeGUID;
        final String name            = OpenMetadataType.SUPPORTED_CAPABILITY_RELATIONSHIP.typeName;
        final String description     = OpenMetadataType.SUPPORTED_CAPABILITY_RELATIONSHIP.description;
        final String descriptionGUID = OpenMetadataType.SUPPORTED_CAPABILITY_RELATIONSHIP.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.SUPPORTED_CAPABILITY_RELATIONSHIP.wikiURL;

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
        final String                     end1EntityType               = OpenMetadataType.IT_INFRASTRUCTURE.typeName;
        final String                     end1AttributeName            = "hostedByDeployedITInfrastructure";
        final String                     end1AttributeDescription     = "IT infrastructure hosting this capability.";
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
        final String                     end2EntityType               = OpenMetadataType.SOFTWARE_CAPABILITY.typeName;
        final String                     end2AttributeName            = "capabilities";
        final String                     end2AttributeDescription     = "Capabilities deployed on this IT infrastructure.";
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

        final String attribute1Name            = OpenMetadataProperty.DEPLOYMENT_TIME.name;
        final String attribute1Description     = OpenMetadataProperty.DEPLOYMENT_TIME.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.DEPLOYMENT_TIME.descriptionGUID;
        final String attribute2Name            = OpenMetadataProperty.DEPLOYER.name;
        final String attribute2Description     = OpenMetadataProperty.DEPLOYER.description;
        final String attribute2DescriptionGUID = OpenMetadataProperty.DEPLOYER.descriptionGUID;
        final String attribute3Name            = OpenMetadataProperty.DEPLOYER_TYPE_NAME.name;
        final String attribute3Description     = OpenMetadataProperty.DEPLOYER_TYPE_NAME.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.DEPLOYER_TYPE_NAME.descriptionGUID;
        final String attribute4Name            = OpenMetadataProperty.DEPLOYER_PROPERTY_NAME.name;
        final String attribute4Description     = OpenMetadataProperty.DEPLOYER_PROPERTY_NAME.description;
        final String attribute4DescriptionGUID = OpenMetadataProperty.DEPLOYER_PROPERTY_NAME.descriptionGUID;
        final String attribute5Name            = OpenMetadataProperty.OPERATIONAL_STATUS.name;
        final String attribute5Description     = OpenMetadataProperty.OPERATIONAL_STATUS.description;
        final String attribute5DescriptionGUID = OpenMetadataProperty.OPERATIONAL_STATUS.descriptionGUID;

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
        property = archiveHelper.getEnumTypeDefAttribute(OpenMetadataType.OPERATIONAL_STATUS_ENUM_TYPE_NAME,
                                                         attribute5Name,
                                                         attribute5Description,
                                                         attribute5DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private TypeDefPatch deprecateSoftwareServerSupportedCapabilityRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "SoftwareServerSupportedCapability";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }


    private TypeDefPatch updateSoftwareServerCapabilityEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName;
        final String superTypeName = OpenMetadataType.SOFTWARE_CAPABILITY.typeName;

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(superTypeName));

        return typeDefPatch;
    }


    private TypeDefPatch updateFileSystemClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "FileSystem";
        final String attachToEntity = OpenMetadataType.REFERENCEABLE.typeName;

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        List<TypeDefLink> validEntityDefs = new ArrayList<>();
        validEntityDefs.add(new TypeDefLink(archiveBuilder.getTypeDefByName(attachToEntity)));

        typeDefPatch.setValidEntityDefs(validEntityDefs);

        return typeDefPatch;
    }


    private TypeDefPatch updateFileManagerClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "FileManager";
        final String attachToEntity = OpenMetadataType.REFERENCEABLE.typeName;

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        List<TypeDefLink> validEntityDefs = new ArrayList<>();
        validEntityDefs.add(new TypeDefLink(archiveBuilder.getTypeDefByName(attachToEntity)));

        typeDefPatch.setValidEntityDefs(validEntityDefs);
        return typeDefPatch;
    }


    private TypeDefPatch updateNotificationManagerClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "NotificationManager";
        final String attachToEntity = OpenMetadataType.REFERENCEABLE.typeName;

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        List<TypeDefLink> validEntityDefs = new ArrayList<>();
        validEntityDefs.add(new TypeDefLink(archiveBuilder.getTypeDefByName(attachToEntity)));

        typeDefPatch.setValidEntityDefs(validEntityDefs);
        return typeDefPatch;
    }


    private TypeDefPatch updateContentCollectionManagerClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "ContentCollectionManager";
        final String attachToEntity = OpenMetadataType.REFERENCEABLE.typeName;

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        List<TypeDefLink> validEntityDefs = new ArrayList<>();
        validEntityDefs.add(new TypeDefLink(archiveBuilder.getTypeDefByName(attachToEntity)));

        typeDefPatch.setValidEntityDefs(validEntityDefs);
        return typeDefPatch;
    }


    private ClassificationDef addSourceControlLibraryClassification()
    {
        final String guid            = "0ef3c90d-20d7-4259-8d66-9c8bb109f2ae";
        final String name            = "SourceControlLibrary";
        final String description     = "Defines a software source code library that provides version control.";
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

        final String attribute1Name            = "libraryType";
        final String attribute1Description     = "The type of library - may be a product name or open source project name.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    private ClassificationDef addChangeManagementLibraryClassification()
    {
        final String guid            = "4e236548-b802-4a1d-a329-4abdeaae5323";
        final String name            = "ChangeManagementLibrary";
        final String description     = "Defines a managed collection of requirements, defects and proposed changes to a project.";
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

        final String attribute1Name            = "libraryType";
        final String attribute1Description     = "The type of library - may be a product name or open source project name.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }



    private ClassificationDef addSoftwareLibraryClassification()
    {
        final String guid            = "5708fa1a-2b64-4706-8e14-a020e4567db3";
        final String name            = "SoftwareLibrary";
        final String description     = "Defines a collection of software modules.  Also known as the definitive software library.";
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

        final String attribute1Name            = "libraryType";
        final String attribute1Description     = "The type of library - may be a product name or open source project name.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    private TypeDefPatch updateCloudServiceClassification()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.CLOUD_SERVICE_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        List<TypeDefLink> validEntityDefs = new ArrayList<>();
        validEntityDefs.add(new TypeDefLink(archiveBuilder.getTypeDefByName(OpenMetadataType.SOFTWARE_CAPABILITY.typeName)));

        typeDefPatch.setValidEntityDefs(validEntityDefs);
        return typeDefPatch;
    }


    private TypeDefPatch updateDatabaseManagerClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "DatabaseManager";
        final String attachToEntity = "DataManager";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        List<TypeDefLink> validEntityDefs = new ArrayList<>();
        validEntityDefs.add(new TypeDefLink(archiveBuilder.getTypeDefByName(attachToEntity)));

        typeDefPatch.setValidEntityDefs(validEntityDefs);
        return typeDefPatch;
    }


    private TypeDefPatch updateServerAssetUseRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "ServerAssetUse";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "SoftwareCapability";
        final String                     end1AttributeName            = "consumedBy";
        final String                     end1AttributeDescription     = "Capability consuming this asset.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        typeDefPatch.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = OpenMetadataType.ASSET.typeName;
        final String                     end2AttributeName            = "consumedAsset";
        final String                     end2AttributeDescription     = "Asset that this software capability is dependent on.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        typeDefPatch.setEndDef2(relationshipEndDef);

        return typeDefPatch;
    }


    private TypeDefPatch updateSoftwareServerDeploymentRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "SoftwareServerDeployment";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "SoftwareServerPlatform";
        final String                     end1AttributeName            = "hostingPlatforms";
        final String                     end1AttributeDescription     = "Supporting platforms for the software server.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        typeDefPatch.setEndDef1(relationshipEndDef);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * Allow IT Infrastructure to be classified with its purpose in the IT Landscape
     */
    private void update0041ServerPurpose()
    {
        this.archiveBuilder.addClassificationDef(addServerPurposeClassification());
        this.archiveBuilder.addTypeDefPatch(updateApplicationServerClassification());
        this.archiveBuilder.addTypeDefPatch(updateWebserverClassification());
        this.archiveBuilder.addTypeDefPatch(updateDatabaseServerClassification());
        this.archiveBuilder.addTypeDefPatch(updateMetadataServerClassification());
        this.archiveBuilder.addTypeDefPatch(updateRepositoryProxyClassification());
        this.archiveBuilder.addTypeDefPatch(updateGovernanceDaemonClassification());
        this.archiveBuilder.addTypeDefPatch(updateStewardshipServerClassification());
        this.archiveBuilder.addClassificationDef(addIntegrationServerClassification());
    }


    private ClassificationDef addServerPurposeClassification()
    {
        final List<TypeDefLink> linkedToEntities = new ArrayList<>();

        linkedToEntities.add(this.archiveBuilder.getEntityDef(OpenMetadataType.IT_INFRASTRUCTURE.typeName));

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.SERVER_PURPOSE_CLASSIFICATION.typeGUID,
                                                                                 OpenMetadataType.SERVER_PURPOSE_CLASSIFICATION.typeName,
                                                                                 null,
                                                                                 OpenMetadataType.SERVER_PURPOSE_CLASSIFICATION.description,
                                                                                 OpenMetadataType.SERVER_PURPOSE_CLASSIFICATION.descriptionGUID,
                                                                                 OpenMetadataType.SERVER_PURPOSE_CLASSIFICATION.wikiURL,
                                                                                 linkedToEntities,
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name;
        final String attribute1Description     = OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.descriptionGUID;


        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    private TypeDefPatch updateApplicationServerClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.APPLICATION_SERVER_CLASSIFICATION.typeName;
        final String superTypeName = OpenMetadataType.SERVER_PURPOSE_CLASSIFICATION.typeName;
        final String attachToEntity = OpenMetadataType.IT_INFRASTRUCTURE.typeName;

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        typeDefPatch.setSuperType(archiveBuilder.getTypeDefByName(superTypeName));

        List<TypeDefLink> validEntityDefs = new ArrayList<>();
        validEntityDefs.add(new TypeDefLink(archiveBuilder.getTypeDefByName(attachToEntity)));

        typeDefPatch.setValidEntityDefs(validEntityDefs);

        return typeDefPatch;
    }


    private TypeDefPatch updateWebserverClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.WEBSERVER_CLASSIFICATION.typeName;
        final String superTypeName = OpenMetadataType.SERVER_PURPOSE_CLASSIFICATION.typeName;
        final String attachToEntity = OpenMetadataType.IT_INFRASTRUCTURE.typeName;

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        typeDefPatch.setSuperType(archiveBuilder.getTypeDefByName(superTypeName));

        List<TypeDefLink> validEntityDefs = new ArrayList<>();
        validEntityDefs.add(new TypeDefLink(archiveBuilder.getTypeDefByName(attachToEntity)));

        typeDefPatch.setValidEntityDefs(validEntityDefs);

        return typeDefPatch;
    }


    private TypeDefPatch updateDatabaseServerClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.DATABASE_SERVER_CLASSIFICATION.typeName;
        final String superTypeName = OpenMetadataType.SERVER_PURPOSE_CLASSIFICATION.typeName;
        final String attachToEntity = OpenMetadataType.IT_INFRASTRUCTURE.typeName;

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        typeDefPatch.setSuperType(archiveBuilder.getTypeDefByName(superTypeName));

        List<TypeDefLink> validEntityDefs = new ArrayList<>();
        validEntityDefs.add(new TypeDefLink(archiveBuilder.getTypeDefByName(attachToEntity)));

        typeDefPatch.setValidEntityDefs(validEntityDefs);
        return typeDefPatch;
    }


    private TypeDefPatch updateMetadataServerClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.METADATA_SERVER_CLASSIFICATION.typeName;
        final String superTypeName = OpenMetadataType.SERVER_PURPOSE_CLASSIFICATION.typeName;
        final String attachToEntity = OpenMetadataType.IT_INFRASTRUCTURE.typeName;

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        typeDefPatch.setSuperType(archiveBuilder.getTypeDefByName(superTypeName));

        List<TypeDefLink> validEntityDefs = new ArrayList<>();
        validEntityDefs.add(new TypeDefLink(archiveBuilder.getTypeDefByName(attachToEntity)));

        typeDefPatch.setValidEntityDefs(validEntityDefs);
        return typeDefPatch;
    }


    private TypeDefPatch updateRepositoryProxyClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.REPOSITORY_PROXY_CLASSIFICATION.typeName;
        final String superTypeName = OpenMetadataType.SERVER_PURPOSE_CLASSIFICATION.typeName;
        final String attachToEntity = OpenMetadataType.IT_INFRASTRUCTURE.typeName;

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        typeDefPatch.setSuperType(archiveBuilder.getTypeDefByName(superTypeName));

        List<TypeDefLink> validEntityDefs = new ArrayList<>();
        validEntityDefs.add(new TypeDefLink(archiveBuilder.getTypeDefByName(attachToEntity)));

        typeDefPatch.setValidEntityDefs(validEntityDefs);
        return typeDefPatch;
    }


    private TypeDefPatch updateGovernanceDaemonClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.GOVERNANCE_DAEMON_CLASSIFICATION.typeName;
        final String superTypeName = OpenMetadataType.SERVER_PURPOSE_CLASSIFICATION.typeName;
        final String attachToEntity = OpenMetadataType.IT_INFRASTRUCTURE.typeName;

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        typeDefPatch.setSuperType(archiveBuilder.getTypeDefByName(superTypeName));

        List<TypeDefLink> validEntityDefs = new ArrayList<>();
        validEntityDefs.add(new TypeDefLink(archiveBuilder.getTypeDefByName(attachToEntity)));

        typeDefPatch.setValidEntityDefs(validEntityDefs);

        return typeDefPatch;
    }


    private TypeDefPatch updateStewardshipServerClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.STEWARDSHIP_SERVER_CLASSIFICATION.typeName;
        final String superTypeName = OpenMetadataType.SERVER_PURPOSE_CLASSIFICATION.typeName;
        final String attachToEntity = OpenMetadataType.IT_INFRASTRUCTURE.typeName;

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        typeDefPatch.setSuperType(archiveBuilder.getTypeDefByName(superTypeName));

        List<TypeDefLink> validEntityDefs = new ArrayList<>();
        validEntityDefs.add(new TypeDefLink(archiveBuilder.getTypeDefByName(attachToEntity)));

        typeDefPatch.setValidEntityDefs(validEntityDefs);

        return typeDefPatch;
    }


    private ClassificationDef addIntegrationServerClassification()
    {
        final List<TypeDefLink> linkedToEntities = new ArrayList<>();

        linkedToEntities.add(this.archiveBuilder.getEntityDef(OpenMetadataType.IT_INFRASTRUCTURE.typeName));

        return archiveHelper.getClassificationDef(OpenMetadataType.INTEGRATION_SERVER_CLASSIFICATION.typeGUID,
                                                  OpenMetadataType.INTEGRATION_SERVER_CLASSIFICATION.typeName,
                                                  archiveBuilder.getClassificationDef(OpenMetadataType.SERVER_PURPOSE_CLASSIFICATION.typeName),
                                                  OpenMetadataType.INTEGRATION_SERVER_CLASSIFICATION.description,
                                                  OpenMetadataType.INTEGRATION_SERVER_CLASSIFICATION.descriptionGUID,
                                                  OpenMetadataType.INTEGRATION_SERVER_CLASSIFICATION.wikiURL,
                                                  linkedToEntities,
                                                  false);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * Extend Person using the field definitions from LDAP
     */
    private void extend0112Person()
    {
        this.archiveBuilder.addTypeDefPatch(updatePersonEntity());
    }

    private TypeDefPatch updatePersonEntity()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.PERSON.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.COURTESY_TITLE.name,
                                                           OpenMetadataProperty.COURTESY_TITLE.description,
                                                           OpenMetadataProperty.COURTESY_TITLE.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.GIVEN_NAMES.name,
                                                           OpenMetadataProperty.GIVEN_NAMES.description,
                                                           OpenMetadataProperty.GIVEN_NAMES.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.SURNAME.name,
                                                           OpenMetadataProperty.SURNAME.description,
                                                           OpenMetadataProperty.SURNAME.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.EMPLOYEE_NUMBER.name,
                                                           OpenMetadataProperty.EMPLOYEE_NUMBER.description,
                                                           OpenMetadataProperty.EMPLOYEE_NUMBER.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.EMPLOYEE_TYPE.name,
                                                           OpenMetadataProperty.EMPLOYEE_TYPE.description,
                                                           OpenMetadataProperty.EMPLOYEE_TYPE.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.PREFERRED_LANGUAGE.name,
                                                           OpenMetadataProperty.PREFERRED_LANGUAGE.description,
                                                           OpenMetadataProperty.PREFERRED_LANGUAGE.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.INITIALS.name,
                                                           OpenMetadataProperty.INITIALS.description,
                                                           OpenMetadataProperty.INITIALS.descriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /**
     * Changing end 2 from ProjectManager to PersonRole to allow any role to manage projects.
     */
    private void update0130Projects()
    {
        this.archiveBuilder.addTypeDefPatch(updateProjectManagementRelationship());
    }


    private TypeDefPatch updateProjectManagementRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "ProjectManagement";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "PersonRole";
        final String                     end2AttributeName            = "projectManagers";
        final String                     end2AttributeDescription     = "The roles for managing this project.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        RelationshipEndDef relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                                    end2AttributeName,
                                                                                    end2AttributeDescription,
                                                                                    end2AttributeDescriptionGUID,
                                                                                    end2Cardinality);


        typeDefPatch.setEndDef2(relationshipEndDef);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * Changing end 2 from CommunityMember to PersonRole to allow any role to be a member of a community.
     */
    private void update0140Communities()
    {
        this.archiveBuilder.addTypeDefPatch(updateCommunityMembershipRelationship());
    }


    private TypeDefPatch updateCommunityMembershipRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "CommunityMembership";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "PersonRole";
        final String                     end2AttributeName            = "communityMembers";
        final String                     end2AttributeDescription     = "Members of the community.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        RelationshipEndDef relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                                    end2AttributeName,
                                                                                    end2AttributeDescription,
                                                                                    end2AttributeDescriptionGUID,
                                                                                    end2Cardinality);


        typeDefPatch.setEndDef2(relationshipEndDef);

        return typeDefPatch;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

}

