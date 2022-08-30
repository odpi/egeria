/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


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
        final String typeName = "ExternalReferenceLink";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "pages";
        final String attribute1Description     = "Range of pages in the external reference that this link refers.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    private TypeDefPatch updateMediaReferenceRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "MediaReference";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "mediaUsage";
        final String attribute1Description     = "Specific media usage by the consumer that overrides the media usage document in the related media.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "mediaUsageOtherId";
        final String attribute2Description     = "Unique identifier of the code (typically a valid value definition) that defines the media use.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getEnumTypeDefAttribute("MediaUsage",
                                                         attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    private TypeDefPatch updateRelatedMediaEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "RelatedMedia";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "defaultMediaUsage";
        final String attribute1Description     = "Default media usage by a consumer.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "defaultMediaUsageOtherId";
        final String attribute2Description     = "Unique identifier of the code (typically a valid value definition) that defines the media use.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "mediaUsage";
        final String attribute3Description     = "Type of recommended media usage.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "mediaTypeOtherId";
        final String attribute4Description     = "Unique identifier of the code (typically a valid value definition) that defines the media type.";
        final String attribute4DescriptionGUID = null;


        property = archiveHelper.getEnumTypeDefAttribute("MediaUsage",
                                                         attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayIntTypeDefAttribute(attribute3Name,
                                                             attribute3Description,
                                                             attribute3DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1Name);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
                                                           attribute4Description,
                                                           attribute4DescriptionGUID);
        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    private TypeDefPatch updateExternalReferenceEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "ExternalReference";
        final String description = "A link to an external reference source such as a web page, article or book.";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setDescription(description);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "displayName";
        final String attribute1Description     = "Name to use when displaying reference in a list.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "referenceTitle";
        final String attribute2Description     = "Full publication title of the external source.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "referenceAbstract";
        final String attribute3Description     = "Summary of the key messages in the external source.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "description";
        final String attribute4Description     = "Description of the external source.  For example, its significance and use.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "authors";
        final String attribute5Description     = "List of authors for the external source.";
        final String attribute5DescriptionGUID = null;
        final String attribute6Name            = "numberOfPages";
        final String attribute6Description     = "Number of pages that this external source has.";
        final String attribute6DescriptionGUID = null;
        final String attribute7Name            = "pageRange";
        final String attribute7Description     = "Range of pages that this reference covers. For example, if it is a journal article, this could be the range of pages for the article in the journal.";
        final String attribute7DescriptionGUID = null;
        final String attribute8Name            = "organization";
        final String attribute8Description     = "Name of the organization that this external source is from.";
        final String attribute8DescriptionGUID = null;
        final String attribute9Name            = "publicationSeries";
        final String attribute9Description     = "Name of the journal or series of publications that this external source is from.";
        final String attribute9DescriptionGUID = null;
        final String attribute10Name            = "publicationSeriesVolume";
        final String attribute10Description     = "Name of the volume in the publication series that this external source is from.";
        final String attribute10DescriptionGUID = null;
        final String attribute11Name            = "edition";
        final String attribute11Description     = "Name of the edition for this external source.";
        final String attribute11DescriptionGUID = null;
        final String attribute12Name            = "referenceVersion";
        final String attribute12Description     = "Name of the revision or version of the external source.";
        final String attribute12DescriptionGUID = null;
        final String attribute13Name            = "url";
        final String attribute13Description     = "Network address where this external source can be accessed from.";
        final String attribute13DescriptionGUID = null;
        final String attribute14Name            = "publisher";
        final String attribute14Description     = "Name of the publisher responsible for producing this external source.";
        final String attribute14DescriptionGUID = null;
        final String attribute15Name            = "firstPublicationDate";
        final String attribute15Description     = "Date of the first published version/edition of this external source.";
        final String attribute15DescriptionGUID = null;
        final String attribute16Name            = "publicationDate";
        final String attribute16Description     = "Date when this version/edition of this external source was published.";
        final String attribute16DescriptionGUID = null;
        final String attribute17Name            = "publicationCity";
        final String attribute17Description     = "City where the publishers are based.";
        final String attribute17DescriptionGUID = null;
        final String attribute18Name            = "publicationYear";
        final String attribute18Description     = "Year when the publication of this version/edition of the external source was published.";
        final String attribute18DescriptionGUID = null;
        final String attribute19Name            = "publicationNumbers";
        final String attribute19Description     = "List of unique numbers allocated by the publisher for this external source.  For example ISBN, ASIN, UNSPSC code.";
        final String attribute19DescriptionGUID = null;
        final String attribute20Name            = "license";
        final String attribute20Description     = "Name of license associated with this external source.";
        final String attribute20DescriptionGUID = null;
        final String attribute21Name            = "copyright";
        final String attribute21Description     = "Copyright statement associated with this external source.";
        final String attribute21DescriptionGUID = null;
        final String attribute22Name            = "attribution";
        final String attribute22Description     = "Attribution statement to use when consuming this external resource.";
        final String attribute22DescriptionGUID = null;

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
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute5Name,
                                                                attribute5Description,
                                                                attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute6Name,
                                                        attribute6Description,
                                                        attribute6DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute7Name,
                                                           attribute7Description,
                                                           attribute7DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute8Name,
                                                           attribute8Description,
                                                           attribute8DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute9Name,
                                                           attribute9Description,
                                                           attribute9DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute10Name,
                                                           attribute10Description,
                                                           attribute10DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute11Name,
                                                           attribute11Description,
                                                           attribute11DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute12Name,
                                                           attribute12Description,
                                                           attribute12DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute13Name,
                                                           attribute13Description,
                                                           attribute13DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute14Name,
                                                           attribute14Description,
                                                           attribute14DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute15Name,
                                                         attribute15Description,
                                                         attribute15DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute16Name,
                                                         attribute16Description,
                                                         attribute16DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute17Name,
                                                           attribute17Description,
                                                           attribute17DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute18Name,
                                                           attribute18Description,
                                                           attribute18DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute19Name,
                                                                attribute19Description,
                                                                attribute19DescriptionGUID);
        properties.add(property);

        property = archiveHelper.getStringTypeDefAttribute(attribute20Name,
                                                           attribute20Description,
                                                           attribute20DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute21Name,
                                                           attribute21Description,
                                                           attribute21DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute22Name,
                                                           attribute22Description,
                                                           attribute22DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */
}

