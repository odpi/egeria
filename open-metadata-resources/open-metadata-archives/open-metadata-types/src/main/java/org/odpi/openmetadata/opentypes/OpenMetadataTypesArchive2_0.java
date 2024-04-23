/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


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
public class OpenMetadataTypesArchive2_0
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  archiveVersion     = "2.0";
    private static final String                  originatorName     = "ODPi Egeria";
    private static final String                  originatorLicense  = "Apache-2.0";
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
    public OpenMetadataTypesArchive2_0()
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
    public OpenMetadataTypesArchive2_0(OMRSArchiveBuilder archiveBuilder)
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
        OpenMetadataTypesArchive1_7  previousTypes = new OpenMetadataTypesArchive1_7(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();


        /*
         * Calls for new and changed types go here
         */
        add0011ManagingReferenceables();
        update0130Projects();
        update0220FilesAndFolders();
        update0221DocumentStores();
        update0224Databases();
        update0512DerivedSchemaAttributes();
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0011 Managing Referenceables - add Template Classification.
     */
    private void add0011ManagingReferenceables()
    {
        this.archiveBuilder.addClassificationDef(addTemplateClassification());
    }


    private ClassificationDef addTemplateClassification()
    {
        final String guid            = OpenMetadataType.TEMPLATE_CLASSIFICATION.typeGUID;
        final String name            = OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName;
        final String description     = OpenMetadataType.TEMPLATE_CLASSIFICATION.description;
        final String descriptionGUID = OpenMetadataType.TEMPLATE_CLASSIFICATION.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.TEMPLATE_CLASSIFICATION.wikiURL;

        final String linkedToEntity = OpenMetadataType.REFERENCEABLE.typeName;

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 descriptionWiki,
                                                                                 this.archiveBuilder.getEntityDef(linkedToEntity),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;


        final String attribute1Name            = OpenMetadataProperty.NAME.name;
        final String attribute1Description     = OpenMetadataProperty.NAME.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.NAME.descriptionGUID;
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
        final String attribute2Name            = "description";
        final String attribute2Description     = "Description of how each item is related to the project.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getDateTypeDefAttribute(attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute2Name);

        properties.add(property);



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

        final String linkedToEntity = OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName;

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

        final String linkedToEntity = OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName;

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
        final String guid            = OpenMetadataType.DATABASE_MANAGER.typeGUID;
        final String name            = OpenMetadataType.DATABASE_MANAGER.typeName;
        final String description     = OpenMetadataType.DATABASE_MANAGER.description;
        final String descriptionGUID = OpenMetadataType.DATABASE_MANAGER.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.DATABASE_MANAGER.wikiURL;

        final String superTypeName = OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID,
                                                 descriptionWiki);

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
     * -------------------------------------------------------------------------------------------------------
     */


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

