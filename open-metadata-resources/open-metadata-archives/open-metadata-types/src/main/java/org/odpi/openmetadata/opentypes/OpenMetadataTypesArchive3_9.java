/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


import org.odpi.openmetadata.frameworks.openmetadata.enums.MediaUsage;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttributeStatus;
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
public class OpenMetadataTypesArchive3_9
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  archiveVersion     = "3.9";
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
    public OpenMetadataTypesArchive3_9()
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
    public OpenMetadataTypesArchive3_9(OMRSArchiveBuilder archiveBuilder)
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
        OpenMetadataTypesArchive3_8 previousTypes = new OpenMetadataTypesArchive3_8(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * Calls for new and changed types go here
         */
        update0015LinkedMediaTypes();
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * Add multi-link flags and extend properties to be able to record proper attributions.
     */
    private void update0015LinkedMediaTypes()
    {
        this.archiveBuilder.addTypeDefPatch(updateExternalReferenceLinkRelationship());
        this.archiveBuilder.addTypeDefPatch(updateMediaReferenceRelationship());
        this.archiveBuilder.addTypeDefPatch(updateRelatedMediaEntity());
        this.archiveBuilder.addTypeDefPatch(updateExternalReferenceEntity());
    }

    private TypeDefPatch updateExternalReferenceLinkRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName;

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.PAGES.name,
                                                           OpenMetadataProperty.PAGES.description,
                                                           OpenMetadataProperty.PAGES.descriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    private TypeDefPatch updateMediaReferenceRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.MEDIA_REFERENCE_RELATIONSHIP.typeName;

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        property = archiveHelper.getEnumTypeDefAttribute(MediaUsage.getOpenTypeName(),
                                                         OpenMetadataProperty.MEDIA_USAGE.name,
                                                         OpenMetadataProperty.MEDIA_USAGE.description,
                                                         OpenMetadataProperty.MEDIA_USAGE.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.MEDIA_USAGE_OTHER_ID.name,
                                                           OpenMetadataProperty.MEDIA_USAGE_OTHER_ID.description,
                                                           OpenMetadataProperty.MEDIA_USAGE_OTHER_ID.descriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    private TypeDefPatch updateRelatedMediaEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.RELATED_MEDIA.typeName;

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        property = archiveHelper.getEnumTypeDefAttribute(MediaUsage.getOpenTypeName(),
                                                         OpenMetadataProperty.DEFAULT_MEDIA_USAGE.name,
                                                         OpenMetadataProperty.DEFAULT_MEDIA_USAGE.description,
                                                         OpenMetadataProperty.DEFAULT_MEDIA_USAGE.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DEFAULT_MEDIA_USAGE_OTHER_ID.name,
                                                           OpenMetadataProperty.DEFAULT_MEDIA_USAGE_OTHER_ID.description,
                                                           OpenMetadataProperty.DEFAULT_MEDIA_USAGE_OTHER_ID.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayIntTypeDefAttribute("mediaUsages",
                                                             "Types of recommended media usage.",
                                                             null);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(OpenMetadataProperty.DEFAULT_MEDIA_USAGE.name);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.MEDIA_TYPE_OTHER_ID.name,
                                                           OpenMetadataProperty.MEDIA_TYPE_OTHER_ID.description,
                                                           OpenMetadataProperty.MEDIA_TYPE_OTHER_ID.descriptionGUID);
        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    private TypeDefPatch updateExternalReferenceEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.EXTERNAL_REFERENCE.typeName;

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DISPLAY_NAME.name,
                                                           OpenMetadataProperty.DISPLAY_NAME.description,
                                                           OpenMetadataProperty.DISPLAY_NAME.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.REFERENCE_TITLE.name,
                                                           OpenMetadataProperty.REFERENCE_TITLE.description,
                                                           OpenMetadataProperty.REFERENCE_TITLE.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.REFERENCE_ABSTRACT.name,
                                                           OpenMetadataProperty.REFERENCE_ABSTRACT.description,
                                                           OpenMetadataProperty.REFERENCE_ABSTRACT.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(OpenMetadataProperty.AUTHORS.name,
                                                                OpenMetadataProperty.AUTHORS.description,
                                                                OpenMetadataProperty.AUTHORS.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(OpenMetadataProperty.NUMBER_OF_PAGES.name,
                                                        OpenMetadataProperty.NUMBER_OF_PAGES.description,
                                                        OpenMetadataProperty.NUMBER_OF_PAGES.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.PAGE_RANGE.name,
                                                           OpenMetadataProperty.PAGE_RANGE.description,
                                                           OpenMetadataProperty.PAGE_RANGE.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.ORGANIZATION.name,
                                                           OpenMetadataProperty.ORGANIZATION.description,
                                                           OpenMetadataProperty.ORGANIZATION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.PUBLICATION_SERIES.name,
                                                           OpenMetadataProperty.PUBLICATION_SERIES.description,
                                                           OpenMetadataProperty.PUBLICATION_SERIES.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.PUBLICATION_SERIES_VOLUME.name,
                                                           OpenMetadataProperty.PUBLICATION_SERIES_VOLUME.description,
                                                           OpenMetadataProperty.PUBLICATION_SERIES_VOLUME.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.EDITION.name,
                                                           OpenMetadataProperty.EDITION.description,
                                                           OpenMetadataProperty.EDITION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.REFERENCE_VERSION.name,
                                                           OpenMetadataProperty.REFERENCE_VERSION.description,
                                                           OpenMetadataProperty.REFERENCE_VERSION.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.URL.name,
                                                           OpenMetadataProperty.URL.description,
                                                           OpenMetadataProperty.URL.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.PUBLISHER.name,
                                                           OpenMetadataProperty.PUBLISHER.description,
                                                           OpenMetadataProperty.PUBLISHER.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(OpenMetadataProperty.FIRST_PUB_DATE.name,
                                                         OpenMetadataProperty.FIRST_PUB_DATE.description,
                                                         OpenMetadataProperty.FIRST_PUB_DATE.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(OpenMetadataProperty.PUBLICATION_DATE.name,
                                                         OpenMetadataProperty.PUBLICATION_DATE.description,
                                                         OpenMetadataProperty.PUBLICATION_DATE.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.PUBLICATION_CITY.name,
                                                           OpenMetadataProperty.PUBLICATION_CITY.description,
                                                           OpenMetadataProperty.PUBLICATION_CITY.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.PUBLICATION_YEAR.name,
                                                           OpenMetadataProperty.PUBLICATION_YEAR.description,
                                                           OpenMetadataProperty.PUBLICATION_YEAR.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(OpenMetadataProperty.PUBLICATION_NUMBERS.name,
                                                                OpenMetadataProperty.PUBLICATION_NUMBERS.description,
                                                                OpenMetadataProperty.PUBLICATION_NUMBERS.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.LICENSE.name,
                                                           OpenMetadataProperty.LICENSE.description,
                                                           OpenMetadataProperty.LICENSE.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.COPYRIGHT.name,
                                                           OpenMetadataProperty.COPYRIGHT.description,
                                                           OpenMetadataProperty.COPYRIGHT.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.ATTRIBUTION.name,
                                                           OpenMetadataProperty.ATTRIBUTION.description,
                                                           OpenMetadataProperty.ATTRIBUTION.descriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */
}

