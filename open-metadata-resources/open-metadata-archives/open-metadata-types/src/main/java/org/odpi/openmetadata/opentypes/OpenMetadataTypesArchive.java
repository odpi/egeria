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
    private static final String                  archiveVersion     = "1.8";
    private static final String                  originatorName     = "ODPi Egeria";
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
        OpenMetadataTypesArchive1_7  previousTypes = new OpenMetadataTypesArchive1_7(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();


        /*
         * Calls for new types go here
         */
        update0010BaseModel();
        add0057IntegrationCapabilities();
        update0220FilesAndFolders();
        update0221DocumentStores();
        update0224Databases();
        update0512DerivedSchemaAttributes();
        update0130Projects();
        updateClashingControlProperties();
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0010 Base Model - add Template Classification.
     */
    private void update0010BaseModel()
    {
        this.archiveBuilder.addClassificationDef(addTemplateClassification());
    }


    private ClassificationDef addTemplateClassification()
    {
        final String guid = "25fad4a2-c2d6-440d-a5b1-e537881f84ee";

        final String name            = "Template";
        final String description     = "Marks the referenceable as a template for creating new objects.";
        final String descriptionGUID = null;

        final String linkedToEntity = "Referenceable";

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(linkedToEntity),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;


        final String attribute1Name            = "name";
        final String attribute1Description     = "Unique name of the template.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "description";
        final String attribute2Description     = "Description of the template and how/where it is used.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "additionalProperties";
        final String attribute3Description     = "Additional information that is useful to the consumer of the template.";
        final String attribute3DescriptionGUID = null;


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
     * A number of types have attributes whose names clash with header (control) attributes. It is not possible to
     * patch an attribute to change its name for compatibility reasons. These patches deprecate the old (clashing)
     * property names, and introduce new (non-clashing) properties to replace them.
     */
    private void updateClashingControlProperties()
    {
        this.archiveBuilder.addTypeDefPatch(updateSoftwareServerPlatformEntity());
        this.archiveBuilder.addTypeDefPatch(updateSoftwareServerEntity());
        this.archiveBuilder.addTypeDefPatch(updateSoftwareServerCapabilityEntity());
        this.archiveBuilder.addTypeDefPatch(updateCloudPlatformClassification());
        this.archiveBuilder.addTypeDefPatch(updateCloudTenantClassification());
        this.archiveBuilder.addTypeDefPatch(updateCloudServiceClassification());
        this.archiveBuilder.addTypeDefPatch(updateCommentEntity());
        this.archiveBuilder.addTypeDefPatch(updateGraphStoreEntity());
        this.archiveBuilder.addTypeDefPatch(updateLogFileEntity());
        this.archiveBuilder.addTypeDefPatch(updateDatabaseEntity());
        this.archiveBuilder.addTypeDefPatch(updateDatabaseServerClassification());
        this.archiveBuilder.addTypeDefPatch(updateMetadataRepositoryEntity());
        this.archiveBuilder.addTypeDefPatch(updateMetadataServerClassification());
        this.archiveBuilder.addTypeDefPatch(updateRepositoryProxyClassification());
        this.archiveBuilder.addTypeDefPatch(updateBusinessCapabilityEntity());
        this.archiveBuilder.addTypeDefPatch(updateDataStoreEntity());
        this.archiveBuilder.addTypeDefPatch(updateDataSourcePhysicalStatusAnnotationEntity());
        this.archiveBuilder.addTypeDefPatch(updateEnterpriseAccessLayerEntity());
        this.archiveBuilder.addTypeDefPatch(updateMetadataCollectionEntity());
        this.archiveBuilder.addTypeDefPatch(updateExternalReferenceEntity());
        this.archiveBuilder.addTypeDefPatch(updatePropertyFacetEntity());
        this.archiveBuilder.addTypeDefPatch(updateCohortMemberEntity());
        this.archiveBuilder.addTypeDefPatch(updateImplementationSnippetEntity());
        this.archiveBuilder.addTypeDefPatch(updatePolicyAdministrationPointClassification());
        this.archiveBuilder.addTypeDefPatch(updatePolicyDecisionPointClassification());
        this.archiveBuilder.addTypeDefPatch(updatePolicyEnforcementPointClassification());
        this.archiveBuilder.addTypeDefPatch(updatePolicyInformationPointClassification());
        this.archiveBuilder.addTypeDefPatch(updatePolicyRetrievalPointClassification());
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     * @see #updateSoftwareServerPlatformEntity()
     */
    private TypeDefPatch updateSoftwareServerPlatformEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "SoftwareServerPlatform";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the platformType attribute to describe the type of software server platform.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "platformType";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                attribute1Description,
                attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "platformType";
        final String attribute2Description     = "Type of implemented or deployed software server platform.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                attribute2Description,
                attribute2DescriptionGUID);

        properties.add(property);

        final String attribute3Name            = "version";
        final String attribute3Description     = "Deprecated attribute. Use the platformVersion attribute to define the version number of software server platform.";
        final String attribute3DescriptionGUID = null;
        final String attribute3ReplacedBy      = "platformVersion";

        property = archiveHelper.getStringTypeDefAttribute(attribute3Name,
                attribute3Description,
                attribute3DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute3ReplacedBy);
        properties.add(property);

        final String attribute4Name            = "platformVersion";
        final String attribute4Description     = "Version number of the software server platform.";
        final String attribute4DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
                attribute4Description,
                attribute4DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     * @see #updateSoftwareServerPlatformEntity()
     */
    private TypeDefPatch updateSoftwareServerEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "SoftwareServer";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the serverType attribute to describe the type of software server.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "serverType";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                attribute1Description,
                attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "serverType";
        final String attribute2Description     = "Type of implemented or deployed software server.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                attribute2Description,
                attribute2DescriptionGUID);

        properties.add(property);

        final String attribute3Name            = "version";
        final String attribute3Description     = "Deprecated attribute. Use the serverVersion attribute to define the version number of software server.";
        final String attribute3DescriptionGUID = null;
        final String attribute3ReplacedBy      = "serverVersion";

        property = archiveHelper.getStringTypeDefAttribute(attribute3Name,
                attribute3Description,
                attribute3DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute3ReplacedBy);
        properties.add(property);

        final String attribute4Name            = "serverVersion";
        final String attribute4Description     = "Version number of the software server.";
        final String attribute4DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
                attribute4Description,
                attribute4DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     * @see #updateSoftwareServerPlatformEntity()
     */
    private TypeDefPatch updateSoftwareServerCapabilityEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "SoftwareServerCapability";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the capabilityType attribute to describe the type of software server capability.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "capabilityType";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                attribute1Description,
                attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "capabilityType";
        final String attribute2Description     = "Type of implemented or deployed software server capability.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                attribute2Description,
                attribute2DescriptionGUID);

        properties.add(property);

        final String attribute3Name            = "version";
        final String attribute3Description     = "Deprecated attribute. Use the capabilityVersion attribute to define the version number of software server capability.";
        final String attribute3DescriptionGUID = null;
        final String attribute3ReplacedBy      = "capabilityVersion";

        property = archiveHelper.getStringTypeDefAttribute(attribute3Name,
                attribute3Description,
                attribute3DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute3ReplacedBy);
        properties.add(property);

        final String attribute4Name            = "capabilityVersion";
        final String attribute4Description     = "Version number of the software server capability.";
        final String attribute4DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
                attribute4Description,
                attribute4DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     * @see #updateSoftwareServerPlatformEntity()
     */
    private TypeDefPatch updateCloudPlatformClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "CloudPlatform";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the platformType attribute to describe the type of cloud platform.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "platformType";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                attribute1Description,
                attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "platformType";
        final String attribute2Description     = "Type of implemented or deployed cloud platform.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                attribute2Description,
                attribute2DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     * @see #updateSoftwareServerPlatformEntity()
     */
    private TypeDefPatch updateCloudTenantClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "CloudTenant";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the tenantType attribute to describe the type of cloud tenant.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "tenantType";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                attribute1Description,
                attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "tenantType";
        final String attribute2Description     = "Description of the type of tenant.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                attribute2Description,
                attribute2DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     * @see #updateSoftwareServerPlatformEntity()
     */
    private TypeDefPatch updateCloudServiceClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "CloudService";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the serviceType attribute to describe the type of cloud service.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "serviceType";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                attribute1Description,
                attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "serviceType";
        final String attribute2Description     = "Description of the type of the service.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                attribute2Description,
                attribute2DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     * @see #updateSoftwareServerPlatformEntity()
     */
    private TypeDefPatch updateCommentEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "Comment";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the commentType attribute to describe the type of comment.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "commentType";

        property = archiveHelper.getEnumTypeDefAttribute("CommentType",
                attribute1Name,
                attribute1Description,
                attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "commentType";
        final String attribute2Description     = "Type of comment.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getEnumTypeDefAttribute("CommentType",
                attribute2Name,
                attribute2Description,
                attribute2DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     * @see #updateSoftwareServerPlatformEntity()
     */
    private TypeDefPatch updateGraphStoreEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "GraphStore";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the storeType attribute to describe the type of graph store.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "storeType";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                attribute1Description,
                attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "storeType";
        final String attribute2Description     = "Type of implemented or deployed graph store.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                attribute2Description,
                attribute2DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     * @see #updateSoftwareServerPlatformEntity()
     */
    private TypeDefPatch updateLogFileEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "LogFile";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the fileType attribute to describe the type of log file.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "fileType";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                attribute1Description,
                attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "fileType";
        final String attribute2Description     = "Type of implemented or deployed log file.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                attribute2Description,
                attribute2DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     * @see #updateSoftwareServerPlatformEntity()
     */
    private TypeDefPatch updateDatabaseEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "Database";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the databaseType attribute to describe the type of database.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "databaseType";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                attribute1Description,
                attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "databaseType";
        final String attribute2Description     = "Type of implemented or deployed database.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                attribute2Description,
                attribute2DescriptionGUID);

        properties.add(property);

        final String attribute3Name            = "version";
        final String attribute3Description     = "Deprecated attribute. Use the databaseVersion attribute to define the version number of database.";
        final String attribute3DescriptionGUID = null;
        final String attribute3ReplacedBy      = "databaseVersion";

        property = archiveHelper.getStringTypeDefAttribute(attribute3Name,
                attribute3Description,
                attribute3DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute3ReplacedBy);
        properties.add(property);

        final String attribute4Name            = "databaseVersion";
        final String attribute4Description     = "Version of the database.";
        final String attribute4DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
                attribute4Description,
                attribute4DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     * @see #updateSoftwareServerPlatformEntity()
     */
    private TypeDefPatch updateDatabaseServerClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "DatabaseServer";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the serverType attribute to describe the type of database server.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "serverType";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                attribute1Description,
                attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "serverType";
        final String attribute2Description     = "Type of implemented or deployed database server.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                attribute2Description,
                attribute2DescriptionGUID);

        properties.add(property);

        final String attribute3Name            = "version";
        final String attribute3Description     = "Deprecated attribute. Use the softwareVersion attribute to define the version number of database server software.";
        final String attribute3DescriptionGUID = null;
        final String attribute3ReplacedBy      = "softwareVersion";

        property = archiveHelper.getStringTypeDefAttribute(attribute3Name,
                attribute3Description,
                attribute3DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute3ReplacedBy);
        properties.add(property);

        final String attribute4Name            = "softwareVersion";
        final String attribute4Description     = "Version of the database server software.";
        final String attribute4DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
                attribute4Description,
                attribute4DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     * @see #updateSoftwareServerPlatformEntity()
     */
    private TypeDefPatch updateMetadataRepositoryEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "MetadataRepository";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the repositoryType attribute to describe the type of metadata repository.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "repositoryType";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                attribute1Description,
                attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "repositoryType";
        final String attribute2Description     = "Type of implemented or deployed metadata repository.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                attribute2Description,
                attribute2DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     * @see #updateSoftwareServerPlatformEntity()
     */
    private TypeDefPatch updateMetadataServerClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "MetadataServer";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the serverType attribute to describe the type of metadata server.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "serverType";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                attribute1Description,
                attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "serverType";
        final String attribute2Description     = "Type of implemented or deployed metadata server.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                attribute2Description,
                attribute2DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     * @see #updateSoftwareServerPlatformEntity()
     */
    private TypeDefPatch updateRepositoryProxyClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "RepositoryProxy";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the proxyType attribute to describe the type of repository proxy.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "proxyType";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                attribute1Description,
                attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "proxyType";
        final String attribute2Description     = "Type of implemented or deployed repository proxy.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                attribute2Description,
                attribute2DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     * @see #updateSoftwareServerPlatformEntity()
     */
    private TypeDefPatch updateBusinessCapabilityEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "BusinessCapability";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the capabilityType attribute to describe the type of business capability.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "capabilityType";

        property = archiveHelper.getEnumTypeDefAttribute("BusinessCapabilityType",
                attribute1Name,
                attribute1Description,
                attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "capabilityType";
        final String attribute2Description     = "Type of implemented or deployed business capability.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getEnumTypeDefAttribute("BusinessCapabilityType",
                attribute2Name,
                attribute2Description,
                attribute2DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     * @see #updateSoftwareServerPlatformEntity()
     */
    private TypeDefPatch updateDataStoreEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "DataStore";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "createTime";
        final String attribute1Description     = "Deprecated attribute. Use the storeCreateTime attribute to describe the creation time of the data store.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "storeCreateTime";

        property = archiveHelper.getDateTypeDefAttribute(attribute1Name,
                attribute1Description,
                attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "storeCreateTime";
        final String attribute2Description     = "Creation time of the data store.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getDateTypeDefAttribute(attribute2Name,
                attribute2Description,
                attribute2DescriptionGUID);

        properties.add(property);

        final String attribute3Name            = "modifiedTime";
        final String attribute3Description     = "Deprecated attribute. Use the storeUpdateTime attribute to define the last known modification time of the data store.";
        final String attribute3DescriptionGUID = null;
        final String attribute3ReplacedBy      = "storeUpdateTime";

        property = archiveHelper.getDateTypeDefAttribute(attribute3Name,
                attribute3Description,
                attribute3DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute3ReplacedBy);
        properties.add(property);

        final String attribute4Name            = "storeUpdateTime";
        final String attribute4Description     = "Last known modification time of the data store.";
        final String attribute4DescriptionGUID = null;

        property = archiveHelper.getDateTypeDefAttribute(attribute4Name,
                attribute4Description,
                attribute4DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     * @see #updateSoftwareServerPlatformEntity()
     */
    private TypeDefPatch updateDataSourcePhysicalStatusAnnotationEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "DataSourcePhysicalStatusAnnotation";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "createTime";
        final String attribute1Description     = "Deprecated attribute. Use the sourceCreateTime attribute to describe when the data source was created.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "sourceCreateTime";

        property = archiveHelper.getDateTypeDefAttribute(attribute1Name,
                attribute1Description,
                attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "sourceCreateTime";
        final String attribute2Description     = "When the data source was created.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getDateTypeDefAttribute(attribute2Name,
                attribute2Description,
                attribute2DescriptionGUID);

        properties.add(property);

        final String attribute3Name            = "modifiedTime";
        final String attribute3Description     = "Deprecated attribute. Use the sourceUpdateTime attribute to describe when the data source was last modified.";
        final String attribute3DescriptionGUID = null;
        final String attribute3ReplacedBy      = "sourceUpdateTime";

        property = archiveHelper.getDateTypeDefAttribute(attribute3Name,
                attribute3Description,
                attribute3DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute3ReplacedBy);
        properties.add(property);

        final String attribute4Name            = "sourceUpdateTime";
        final String attribute4Description     = "When the data source was last modified.";
        final String attribute4DescriptionGUID = null;

        property = archiveHelper.getDateTypeDefAttribute(attribute4Name,
                attribute4Description,
                attribute4DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     * @see #updateSoftwareServerPlatformEntity()
     */
    private TypeDefPatch updateEnterpriseAccessLayerEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "EnterpriseAccessLayer";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "metadataCollectionId";
        final String attribute1Description     = "Deprecated attribute. Use the accessedMetadataCollectionId attribute to define the unique identifier for the metadata collection accessed through this enterprise access layer.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "accessedMetadataCollectionId";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                attribute1Description,
                attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "accessedMetadataCollectionId";
        final String attribute2Description     = "Unique identifier for the metadata collection accessed through this enterprise access layer.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                attribute2Description,
                attribute2DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     * @see #updateSoftwareServerPlatformEntity()
     */
    private TypeDefPatch updateMetadataCollectionEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "MetadataCollection";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "metadataCollectionId";
        final String attribute1Description     = "Deprecated attribute. Use the managedMetadataCollectionId attribute to define the unique identifier for the metadata collection managed in the local repository.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "managedMetadataCollectionId";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                attribute1Description,
                attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "managedMetadataCollectionId";
        final String attribute2Description     = "Unique identifier for the metadata collection managed in the local repository.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                attribute2Description,
                attribute2DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     * @see #updateSoftwareServerPlatformEntity()
     */
    private TypeDefPatch updateExternalReferenceEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "ExternalReference";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "version";
        final String attribute1Description     = "Deprecated attribute. Use the referenceVersion attribute to define the version number of the external reference.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "referenceVersion";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                attribute1Description,
                attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "referenceVersion";
        final String attribute2Description     = "Version number of the external reference.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                attribute2Description,
                attribute2DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     * @see #updateSoftwareServerPlatformEntity()
     */
    private TypeDefPatch updatePropertyFacetEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "PropertyFacet";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "version";
        final String attribute1Description     = "Deprecated attribute. Use the schemaVersion attribute to define the version number of the property facet schema.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "schemaVersion";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                attribute1Description,
                attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "schemaVersion";
        final String attribute2Description     = "Version of the property facet schema.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                attribute2Description,
                attribute2DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     * @see #updateSoftwareServerPlatformEntity()
     */
    private TypeDefPatch updateCohortMemberEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "CohortMember";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "version";
        final String attribute1Description     = "Deprecated attribute. Use the protocolVersion attribute to define the version number of the protocol supported by the cohort registry.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "protocolVersion";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                attribute1Description,
                attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "protocolVersion";
        final String attribute2Description     = "Version number of the protocol supported by the cohort registry.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                attribute2Description,
                attribute2DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     * @see #updateSoftwareServerPlatformEntity()
     */
    private TypeDefPatch updateImplementationSnippetEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "ImplementationSnippet";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "version";
        final String attribute1Description     = "Deprecated attribute. Use the snippetVersion attribute to define the version number of the snippet.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "snippetVersion";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                attribute1Description,
                attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "snippetVersion";
        final String attribute2Description     = "Version number of the snippet.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                attribute2Description,
                attribute2DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     * @see #updateSoftwareServerPlatformEntity()
     */
    private TypeDefPatch updatePolicyAdministrationPointClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "PolicyAdministrationPoint";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the pointType attribute to describe type information about the policy administration point.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "pointType";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                attribute1Description,
                attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "pointType";
        final String attribute2Description     = "Descriptive type information about the policy administration point.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                attribute2Description,
                attribute2DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     * @see #updateSoftwareServerPlatformEntity()
     */
    private TypeDefPatch updatePolicyDecisionPointClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "PolicyDecisionPoint";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the pointType attribute to describe type information about the policy decision point.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "pointType";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                attribute1Description,
                attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "pointType";
        final String attribute2Description     = "Descriptive type information about the policy decision point.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                attribute2Description,
                attribute2DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     * @see #updateSoftwareServerPlatformEntity()
     */
    private TypeDefPatch updatePolicyEnforcementPointClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "PolicyEnforcementPoint";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the pointType attribute to describe type information about the policy enforcement point.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "pointType";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                attribute1Description,
                attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "pointType";
        final String attribute2Description     = "Descriptive type information about the policy enforcement point.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                attribute2Description,
                attribute2DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     * @see #updateSoftwareServerPlatformEntity()
     */
    private TypeDefPatch updatePolicyInformationPointClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "PolicyInformationPoint";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the pointType attribute to describe type information about the policy information point.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "pointType";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                attribute1Description,
                attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "pointType";
        final String attribute2Description     = "Descriptive type information about the policy information point.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                attribute2Description,
                attribute2DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     * @see #updateSoftwareServerPlatformEntity()
     */
    private TypeDefPatch updatePolicyRetrievalPointClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "PolicyRetrievalPoint";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the pointType attribute to describe type information about the policy retrieval point.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "pointType";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                attribute1Description,
                attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "pointType";
        final String attribute2Description     = "Descriptive type information about the policy retrieval point.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                attribute2Description,
                attribute2DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0057 - Integration capabilities describe the different types of integration capabilities.
     * Initially these are Egeria's integration daemons.
     */
    private void add0057IntegrationCapabilities()
    {
        this.archiveBuilder.addEntityDef(addMetadataIntegrationCapabilityEntity());

        this.archiveBuilder.addClassificationDef(addDataManagerIntegrationClassification());
        this.archiveBuilder.addClassificationDef(addDataEngineIntegrationClassification());
    }


    private EntityDef addMetadataIntegrationCapabilityEntity()
    {
        final String guid            = "cc6d2d77-626c-4d0d-aa22-0a491b5fee94";
        final String name            = "MetadataIntegrationCapability";
        final String description     = "Defines a capability that exchanges metadata between servers.";
        final String descriptionGUID = null;

        final String superTypeName = "SoftwareServerCapability";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);

    }


    private ClassificationDef addDataManagerIntegrationClassification()
    {
        final String guid = "2356af59-dda5-45ad-927f-540bed6b281d";

        final String name            = "DataManagerIntegration";
        final String description     = "Integrating metadata about data managers such as database servers, content managers and file systems.";
        final String descriptionGUID = null;

        final String linkedToEntity = "MetadataIntegrationCapability";

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(linkedToEntity),
                                                  false);
    }


    private ClassificationDef addDataEngineIntegrationClassification()
    {
        final String guid = "b675a6d1-7dd7-4b20-b91b-43b358dfe0cf";

        final String name            = "DataEngineIntegration";
        final String description     = "Integrating metadata from data engines.";
        final String descriptionGUID = null;

        final String linkedToEntity = "MetadataIntegrationCapability";

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(linkedToEntity),
                                                  false);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0130 - update ProjectScope description
     */
    private void update0130Projects()
    {
        this.archiveBuilder.addTypeDefPatch(updateProjectScopeRelationship());
    }

    /**
     * The ProjectScope has an attribute with the incorrect type of Date. It is not possible to patch an attribute to change its type for
     * compatibility reasons. This patch deprecates the old scopeDescription (with Date type) and introduces a new description (with
     * String type).
     *
     * @return the type def patch
     */
    private TypeDefPatch updateProjectScopeRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "ProjectScope";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "scopeDescription";
        final String attribute1Description     = "Deprecated attribute. Use the description attribute to describe the scope.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getDateTypeDefAttribute(attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);

        properties.add(property);

        final String attribute2Name            = "description";
        final String attribute2Description     = "Description of how each item is related to the project.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void update0220FilesAndFolders()
    {
        this.archiveBuilder.addClassificationDef(getFileManagerClassification());
    }


    private ClassificationDef getFileManagerClassification()
    {
        final String guid            = "eadec807-02f0-4d6f-911c-261eddd0c2f5";
        final String name            = "FileManager";
        final String description     = "Identifies a software server capability as a manager of a collection of files and folders.";
        final String descriptionGUID = null;

        final String linkedToEntity = "SoftwareServerCapability";

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(linkedToEntity),
                                                  false);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void update0221DocumentStores()
    {
        this.archiveBuilder.addTypeDefPatch(deprecateConnectManagerClassification());
        this.archiveBuilder.addClassificationDef(getContentCollectionManagerClassification());
    }


    private TypeDefPatch deprecateConnectManagerClassification()
    {
        final String typeName = "ContentManager";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }


    private ClassificationDef getContentCollectionManagerClassification()
    {
        final String guid            = "dbde6a5b-fc89-4b04-969a-9dc09a60ebd7";
        final String name            = "ContentCollectionManager";
        final String description     = "Identifies a software server capability as a manager of controlled documents and related media.";
        final String descriptionGUID = null;

        final String linkedToEntity = "SoftwareServerCapability";

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(linkedToEntity),
                                                  false);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0224 - Add the databaseManager to cover the software server capability for the database server.
     */
    private void update0224Databases()
    {
        this.archiveBuilder.addEntityDef(addDatabaseManagerEntity());
    }


    private EntityDef addDatabaseManagerEntity()
    {
        final String guid            = "68b35c1e-6c28-4ac3-94f9-2c3dbcbb79e9";
        final String name            = "DatabaseManager";
        final String description     = "Defines a capability that manages data organized as relational schemas.";
        final String descriptionGUID = null;

        final String superTypeName = "SoftwareServerCapability";

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
     * 0512 - Add the queryId to identify how the query is used in a complex formula.
     */
    private void update0512DerivedSchemaAttributes()
    {
        this.archiveBuilder.addTypeDefPatch(updateSchemaQueryImplementationRelationship());
    }


    private TypeDefPatch updateSchemaQueryImplementationRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "SchemaQueryImplementation";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "queryId";
        final String attribute1Description     = "Identifier for placeholder in derived schema attribute's formula.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;

    }


    /*
     * ========================================================================
     * Below are place holders for types to be introduced in future releases.
     * ========================================================================
     */


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0217 Automated Processes defines a Process is automated (as opposed to manual procedure).
     */
    private void add0217AutomatedProcesses()
    {
        /* placeholder */
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0260Transformations()
    {
        /* placeholder */
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void add0265AnalyticsAssets()
    {
        /* placeholder */
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void add0270IoTAssets()
    {
        /* placeholder */
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0280ModelAssets()
    {
        /* placeholder */
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0435 Governance Rules define details of a governance rule implementation.
     */
    private void add0435GovernanceRules()
    {
        /* placeholder */
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0447GovernanceProcesses()
    {
        /* placeholder */
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0452GovernanceDaemons()
    {
        /* placeholder */
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0480RightsManagement()
    {
        /* placeholder */
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0550LogicSpecificationModel()
    {
        /* placeholder */
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0560MappingModel()
    {
        /* placeholder */
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


}

