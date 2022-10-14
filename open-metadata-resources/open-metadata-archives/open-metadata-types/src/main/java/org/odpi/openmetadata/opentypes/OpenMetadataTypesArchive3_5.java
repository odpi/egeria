/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


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
        update0461GovernanceEngines();
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
        final String guid            = "54055c38-b9ad-4a66-a75b-14dc643d4c69";
        final String name            = "SoftwareCapability";
        final String description     = "A software capability such as an software service or engine.";
        final String descriptionGUID = null;

        final String superTypeName = "Referenceable";

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

        final String attribute1Name            = "name";
        final String attribute1Description     = "Name of the software capability.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "description";
        final String attribute2Description     = "Description of the software capability.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "capabilityType";
        final String attribute3Description     = "Type of the software capability.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "capabilityVersion";
        final String attribute4Description     = "Version number of the software capability.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "patchLevel";
        final String attribute5Description     = "Patch level of the software server capability.";
        final String attribute5DescriptionGUID = null;
        final String attribute6Name            = "source";
        final String attribute6Description     = "Supplier of the software server capability.";
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
        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
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

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private RelationshipDef getSupportedSoftwareCapabilityRelationship()
    {
        final String guid            = "2480aa71-44c5-414d-8b32-9c4340786d77";
        final String name            = "SupportedSoftwareCapability";
        final String description     = "Identifies a software capability that is deployed to an instance of IT infrastructure.";
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
        final String                     end1EntityType               = "ITInfrastructure";
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
        final String                     end2EntityType               = "SoftwareCapability";
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

        final String attribute1Name            = "deploymentTime";
        final String attribute1Description     = "Time that the software capability was deployed to the IT Infrastructure.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "deployer";
        final String attribute2Description     = "Person, organization or engine that deployed the software capability.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "deployerTypeName";
        final String attribute3Description     = "Type name of deployer.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "deployerPropertyName";
        final String attribute4Description     = "Identifying property name of deployer.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "capabilityStatus";
        final String attribute5Description     = "The operational status of the software capability on this IT Infrastructure.";
        final String attribute5DescriptionGUID = null;

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
        property = archiveHelper.getEnumTypeDefAttribute("OperationalStatus",
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
        final String superTypeName = "SupportedSoftwareCapability";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getRelationshipDef(superTypeName));
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }


    private TypeDefPatch updateSoftwareServerCapabilityEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "SoftwareServerCapability";
        final String superTypeName = "SoftwareCapability";

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
        final String attachToEntity = "Referenceable";

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
        final String attachToEntity = "Referenceable";

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
        final String attachToEntity = "Referenceable";

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
        final String attachToEntity = "Referenceable";

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

        final String linkedToEntity = "Referenceable";

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

        final String linkedToEntity = "Referenceable";

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

        final String linkedToEntity = "Referenceable";

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
        final String typeName = "CloudService";
        final String attachToEntity = "SoftwareCapability";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        List<TypeDefLink> validEntityDefs = new ArrayList<>();
        validEntityDefs.add(new TypeDefLink(archiveBuilder.getTypeDefByName(attachToEntity)));

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
        final String                     end2EntityType               = "Asset";
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
        final String guid = "78f68757-600f-4e8e-843b-00e77cdee37c";

        final String name            = "ServerPurpose";
        final String description     = "Adds more detail about the purpose of a deployed instance of IT infrastructure.";
        final String descriptionGUID = null;

        final List<TypeDefLink> linkedToEntities = new ArrayList<>();

        linkedToEntities.add(this.archiveBuilder.getEntityDef("ITInfrastructure"));

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 linkedToEntities,
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "deployedImplementationType";
        final String attribute1Description     = "Type of software deployed - such as product name.";
        final String attribute1DescriptionGUID = null;


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
        final String typeName = "ApplicationServer";
        final String superTypeName = "ServerPurpose";
        final String attachToEntity = "ITInfrastructure";

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
        final String typeName = "Webserver";
        final String superTypeName = "ServerPurpose";
        final String attachToEntity = "ITInfrastructure";

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
        final String typeName = "DatabaseServer";
        final String superTypeName = "ServerPurpose";
        final String attachToEntity = "ITInfrastructure";

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
        final String typeName = "MetadataServer";
        final String superTypeName = "ServerPurpose";
        final String attachToEntity = "ITInfrastructure";

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
        final String typeName = "RepositoryProxy";
        final String superTypeName = "ServerPurpose";
        final String attachToEntity = "ITInfrastructure";

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
        final String typeName = "GovernanceDaemon";
        final String superTypeName = "ServerPurpose";
        final String attachToEntity = "ITInfrastructure";

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
        final String typeName = "StewardshipServer";
        final String superTypeName = "ServerPurpose";
        final String attachToEntity = "ITInfrastructure";

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
        final String guid = "c165b760-d9ab-47ac-a2ee-7854ec74605a";

        final String name            = "IntegrationServer";
        final String superTypeName   = "ServerPurpose";
        final String description     = "Identifies a server that exchanges data between between other servers.";
        final String descriptionGUID = null;

        final List<TypeDefLink> linkedToEntities = new ArrayList<>();

        linkedToEntities.add(this.archiveBuilder.getEntityDef("ITInfrastructure"));

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  archiveBuilder.getClassificationDef(superTypeName),
                                                  description,
                                                  descriptionGUID,
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
        final String typeName = "Person";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "title";
        final String attribute1Description     = "The courtesy title for the person.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "givenNames";
        final String attribute2Description     = "The name strings that are the part of a person's name that is not their surname.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "surname";
        final String attribute3Description     = "The family name of the person.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "employeeNumber";
        final String attribute4Description     = "The unique identifier of the person used by their employer.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "employeeType";
        final String attribute5Description     = "Code used by employer typically to describe the type of employment contract.";
        final String attribute5DescriptionGUID = null;
        final String attribute6Name            = "preferredLanguage";
        final String attribute6Description     = "Spoken or written language preferred by the person.";
        final String attribute6DescriptionGUID = null;
        final String attribute7Name            = "initials";
        final String attribute7Description     = "First letter of each of the person's given names.";
        final String attribute7DescriptionGUID = null;



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
        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
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
        property = archiveHelper.getStringTypeDefAttribute(attribute7Name,
                                                           attribute7Description,
                                                           attribute7DescriptionGUID);
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


    /**
     * 0461 Add Archive Engines and Services
     */
    private void update0461GovernanceEngines()
    {
        this.archiveBuilder.addEntityDef(addArchiveEngineEntity());
        this.archiveBuilder.addEntityDef(addArchiveServiceEntity());
    }


    private EntityDef addArchiveEngineEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = "773298be-68ab-4b99-99ab-19eaa886261e";
        final String name            = "ArchiveEngine";
        final String description     = "A collection of related archive services.";
        final String descriptionGUID = null;
        final String superTypeName   = "GovernanceEngine";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }

    private EntityDef addArchiveServiceEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = "e6c049e2-56aa-4512-a634-20cd7085e534";
        final String name            = "ArchiveService";
        final String description     = "A governance service that maintains open metadata archives.";
        final String descriptionGUID = null;
        final String superTypeName   = "GovernanceService";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }
}

