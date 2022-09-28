/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
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
    private static final String                  archiveVersion     = "3.12";
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
        OpenMetadataTypesArchive3_11 previousTypes = new OpenMetadataTypesArchive3_11(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * Calls for new and changed types go here
         */
        add0053XRootSchemaType();
        add0042ProcessingStateClassification();


    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0042  Add the Processing State Classification
     */
    private void add0042ProcessingStateClassification()
    {
        this.archiveBuilder.addClassificationDef(addProcessingStateClassification());
    }

    private ClassificationDef addProcessingStateClassification() {
        final String guid = "261fb0aa-b884-4ee8-87ea-a60510e9751d";
        final String name = "ProcessingState";
        final String description = "Stores processing state information used by various SoftwareCapabilities.";
        final String descriptionGUID = null;

        final String linkedToEntity = "SoftwareCapability";

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
        TypeDefAttribute property;

        final String attribute1Name = "syncDatesByKey";
        final String attribute1Description = "Collection of synchronization dates identified by a key";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getMapStringLongTypeDefAttribute(attribute1Name,
                attribute1Description,
                attribute1DescriptionGUID);

        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    private void add0053XRootSchemaType()
    {
        this.archiveBuilder.addEntityDef(getRootSchemaTypeEntity());

        this.archiveBuilder.addTypeDefPatch(updateTabularSchemaType());
        this.archiveBuilder.addTypeDefPatch(updateDocumentSchemaType());
        this.archiveBuilder.addTypeDefPatch(updateObjectSchemaType());
        this.archiveBuilder.addTypeDefPatch(updateEventType());
        this.archiveBuilder.addTypeDefPatch(updateRelationalDBSchemaType());
        this.archiveBuilder.addTypeDefPatch(updateAPISchemaType());
        this.archiveBuilder.addTypeDefPatch(updateDisplayDataSchemaType());
        this.archiveBuilder.addTypeDefPatch(updateQuerySchemaType());
    }


    private EntityDef getRootSchemaTypeEntity()
    {
        final String guid            = "126962bf-dd26-4fcf-97d8-d0ad1fdd2d50";
        final String name            = "RootSchemaType";
        final String description     = "The root of a complex schema - normally attaches to an asset or port.";
        final String descriptionGUID = null;

        final String superTypeName = "ComplexSchemaType";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private TypeDefPatch updateTabularSchemaType()
    {
        /*
         * Create the Patch
         */
        final String typeName = "TabularSchemaType";

        final String superTypeName = "RootSchemaType";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(superTypeName));

        return typeDefPatch;
    }


    private TypeDefPatch updateDocumentSchemaType()
    {
        /*
         * Create the Patch
         */
        final String typeName = "DocumentSchemaType";

        final String superTypeName = "RootSchemaType";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(superTypeName));

        return typeDefPatch;
    }


    private TypeDefPatch updateObjectSchemaType()
    {
        /*
         * Create the Patch
         */
        final String typeName = "ObjectSchemaType";

        final String superTypeName = "RootSchemaType";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(superTypeName));

        return typeDefPatch;
    }



    private TypeDefPatch updateEventType()
    {
        /*
         * Create the Patch
         */
        final String typeName = "EventType";

        final String superTypeName = "RootSchemaType";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(superTypeName));

        return typeDefPatch;
    }


    private TypeDefPatch updateRelationalDBSchemaType()
    {
        /*
         * Create the Patch
         */
        final String typeName = "RelationalDBSchemaType";

        final String superTypeName = "RootSchemaType";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(superTypeName));

        return typeDefPatch;
    }


    private TypeDefPatch updateAPISchemaType()
    {
        /*
         * Create the Patch
         */
        final String typeName = "APISchemaType";

        final String superTypeName = "RootSchemaType";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(superTypeName));

        return typeDefPatch;
    }


    private TypeDefPatch updateDisplayDataSchemaType()
    {
        /*
         * Create the Patch
         */
        final String typeName = "DisplayDataSchemaType";

        final String superTypeName = "RootSchemaType";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(superTypeName));

        return typeDefPatch;
    }


    private TypeDefPatch updateQuerySchemaType()
    {
        /*
         * Create the Patch
         */
        final String typeName = "QuerySchemaType";

        final String superTypeName = "RootSchemaType";

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

