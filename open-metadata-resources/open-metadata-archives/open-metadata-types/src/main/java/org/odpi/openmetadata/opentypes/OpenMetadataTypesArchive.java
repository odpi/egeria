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
    private static final String                  archiveVersion     = "4.4";
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
        OpenMetadataTypesArchive4_3 previousTypes = new OpenMetadataTypesArchive4_3(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * Add the type updates
         */
        update0021Collections();
        update0130Projects();
        update0210DataStores();
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0021Collections()
    {
        this.archiveBuilder.addTypeDefPatch(updateCollection());
        this.archiveBuilder.addClassificationDef(getRootCollectionClassification());
    }

    private TypeDefPatch updateCollection()
    {
        /*
         * Create the Patch
         */
        final String typeName = "Collection";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "collectionType";
        final String attribute1Description     = "Descriptive name of the concept that this collection represents.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    private ClassificationDef getRootCollectionClassification()
    {
        final String guid            = "9fdb6d71-fd69-4c40-81f3-5eab1c44d1f4";
        final String name            = "RootCollection";
        final String description     = "This collection is the root collection in a collection hierarchy.";
        final String descriptionGUID = null;

        final String linkedToEntity = "Collection";

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

    private void update0130Projects()
    {
        this.archiveBuilder.addClassificationDef(getPersonalProjectClassification());
    }


    private ClassificationDef getPersonalProjectClassification()
    {
        final String guid            = "3d7b8500-cebd-4f18-b85c-a459bec3e3ef";
        final String name            = "PersonalProject";
        final String description     = "This is an informal project that has been created by an individual to help them organize their work.";
        final String descriptionGUID = null;

        final String linkedToEntity = "Project";

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

    private void update0210DataStores()
    {
        this.archiveBuilder.addClassificationDef(getDataScopeClassification());
    }


    private ClassificationDef getDataScopeClassification()
    {
        final String guid            = "22f996d0-c4b7-433a-af0b-6a3e9478e488";
        final String name            = "DataScope";
        final String description     = "Defines the scope of the data held in the associated data resource.";
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

        final String attribute1Name            = "minimumLongitude";
        final String attribute1Description     = "If the data is bound by an area, this is the longitude for bottom-left corner of the bounding box (BBOX) for the area covered by the data.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "minimumLatitude";
        final String attribute2Description     = "If the data is bound by an area, this is the latitude for the bottom-left corner of the bounding box (BBOX) for the area covered by the data.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "maxLongitude";
        final String attribute3Description     = "If the data is bound by an area, this is the longitude for top-right corner of the bounding box (BBOX) for the area covered by the data..";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "maxLatitude";
        final String attribute4Description     = "If the data is bound by an area, this is the latitude for top-right corner of the bounding box (BBOX) for the area covered by the data.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "minHeight";
        final String attribute5Description     = "If the height above ground is relevant, this is the lowest height that the data covers.";
        final String attribute5DescriptionGUID = null;
        final String attribute6Name            = "maxHeight";
        final String attribute6Description     = "If the height above ground is relevant, this is the highest height that the data covers.";
        final String attribute6DescriptionGUID = null;
        final String attribute7Name            = "startTime";
        final String attribute7Description     = "If the data is bound by time, this is the start time.";
        final String attribute7DescriptionGUID = null;
        final String attribute8Name            = "endTime";
        final String attribute8Description     = "If the data is bound by time, this is the end time.";
        final String attribute8DescriptionGUID = null;

        property = archiveHelper.getFloatTypeDefAttribute(attribute1Name,
                                                          attribute1Description,
                                                          attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getFloatTypeDefAttribute(attribute2Name,
                                                          attribute2Description,
                                                          attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getFloatTypeDefAttribute(attribute3Name,
                                                          attribute3Description,
                                                          attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getFloatTypeDefAttribute(attribute4Name,
                                                          attribute4Description,
                                                          attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getFloatTypeDefAttribute(attribute5Name,
                                                          attribute5Description,
                                                          attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getFloatTypeDefAttribute(attribute6Name,
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

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */
}

