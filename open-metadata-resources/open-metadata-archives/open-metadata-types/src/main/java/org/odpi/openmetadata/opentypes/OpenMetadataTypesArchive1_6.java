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
public class OpenMetadataTypesArchive1_6
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  archiveVersion     = "1.6";
    private static final String                  originatorName     = "ODPi Egeria";
    private static final String                  originatorLicense  = "Apache-2.0";
    private static final Date                    creationDate       = new Date(1516313040008L);

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
    public OpenMetadataTypesArchive1_6()
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
    public OpenMetadataTypesArchive1_6(OMRSArchiveBuilder archiveBuilder)
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
             * Build the archive
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
        OpenMetadataTypesArchive1_5  previousTypes = new OpenMetadataTypesArchive1_5(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * Calls for new types go here
         */
        update0501SchemaElements();
        update0505SchemaAttributes();
        update0512DerivedSchemaAttributes();
    }


    private void update0501SchemaElements()
    {
        this.archiveBuilder.addTypeDefPatch(updateSchemaElementEntity());
    }


    /**
     * 0501 - SchemaElement entity is changed to allow a schema element to be marked as deprecated.
     */
    private TypeDefPatch updateSchemaElementEntity()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.SCHEMA_ELEMENT.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);


        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "isDeprecated";
        final String attribute1Description     = "This element may still be used but is flagged that it will be removed at some point in the future.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getBooleanTypeDefAttribute(attribute1Name,
                                                            attribute1Description,
                                                            attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    private void update0505SchemaAttributes()
    {
        this.archiveBuilder.addTypeDefPatch(updateSchemaAttributeEntity());
        this.archiveBuilder.addTypeDefPatch(updateTypeEmbeddedAttributeClassification());
    }


    /**
     * 0505 - SchemaAttribute entity is changed to show deprecated properties
     */
    private TypeDefPatch updateSchemaAttributeEntity()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.SCHEMA_ATTRIBUTE.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);


        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "minimumLength";
        final String attribute1Description     = "Minimum length of the data value (zero means unlimited).";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "length";
        final String attribute2Description     = "Length of the data field (zero means unlimited).";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "significantDigits";
        final String attribute3Description     = "Number of significant digits before the decimal point (zero means it is an integer).";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "isNullable";
        final String attribute4Description     = "Accepts null values or not.";
        final String attribute4DescriptionGUID = null;
        final String attribute6Name            = "cardinality";
        final String attribute6Description     = "Number of occurrences of this attribute allowed (deprecated).";
        final String attribute6DescriptionGUID = null;
        final String attribute6ReplacedBy      = "maxCardinality";
        final String attribute7Name            = "name";
        final String attribute7Description     = "Name of schema attribute (deprecated).";
        final String attribute7DescriptionGUID = null;
        final String attribute7ReplacedBy      = OpenMetadataProperty.DISPLAY_NAME.name;

        property = archiveHelper.getIntTypeDefAttribute(attribute1Name,
                                                        attribute1Description,
                                                        attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute2Name,
                                                        attribute2Description,
                                                        attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute3Name,
                                                        attribute3Description,
                                                        attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getBooleanTypeDefAttribute(attribute4Name,
                                                            attribute4Description,
                                                            attribute4DescriptionGUID);
        properties.add(property);

        property = archiveHelper.getStringTypeDefAttribute(attribute6Name,
                                                           attribute6Description,
                                                           attribute6DescriptionGUID);
        property.setReplacedByAttribute(attribute6ReplacedBy);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        properties.add(property);

        property = archiveHelper.getStringTypeDefAttribute(attribute7Name,
                                                           attribute7Description,
                                                           attribute7DescriptionGUID);
        property.setReplacedByAttribute(attribute7ReplacedBy);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /**
     * 0505 - TypeEmbeddedAttribute classification is changed to include more values of schema type
     */
    private TypeDefPatch updateTypeEmbeddedAttributeClassification()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name             = "schemaTypeName";
        final String attribute1Description      = "Type name for the schema type.";
        final String attribute1DescriptionGUID  = null;
        final String attribute2Name             = OpenMetadataProperty.QUALIFIED_NAME.name;
        final String attribute2Description      = OpenMetadataProperty.QUALIFIED_NAME.description;
        final String attribute2DescriptionGUID  = OpenMetadataProperty.QUALIFIED_NAME.descriptionGUID;
        final String attribute3Name             = OpenMetadataProperty.DISPLAY_NAME.name;
        final String attribute3Description      = OpenMetadataProperty.DISPLAY_NAME.description;
        final String attribute3DescriptionGUID  = OpenMetadataProperty.DISPLAY_NAME.descriptionGUID;
        final String attribute4Name             = OpenMetadataProperty.DESCRIPTION.name;
        final String attribute4Description      = OpenMetadataProperty.DESCRIPTION.description;
        final String attribute4DescriptionGUID  = OpenMetadataProperty.DESCRIPTION.descriptionGUID;
        final String attribute5Name             = "versionNumber";
        final String attribute5Description      = "Version of the schema type.";
        final String attribute5DescriptionGUID  = null;
        final String attribute6Name             = "author";
        final String attribute6Description      = "User name of the person or process that created the schema type.";
        final String attribute6DescriptionGUID  = null;
        final String attribute7Name             = OpenMetadataProperty.USAGE.name;
        final String attribute7Description      = OpenMetadataProperty.USAGE.description;
        final String attribute7DescriptionGUID  = OpenMetadataProperty.USAGE.descriptionGUID;
        final String attribute8Name             = "defaultValue";
        final String attribute8Description      = "Initial value for data stored in this schema type (primitive and enum types).";
        final String attribute8DescriptionGUID  = null;
        final String attribute9Name             = "fixedValue";
        final String attribute9Description      = "Fixed value for data stored in this schema type (literal schema type).";
        final String attribute9DescriptionGUID  = null;
        final String attribute10Name            = OpenMetadataProperty.ADDITIONAL_PROPERTIES.name;
        final String attribute10Description     = OpenMetadataProperty.ADDITIONAL_PROPERTIES.description;
        final String attribute10DescriptionGUID = OpenMetadataProperty.ADDITIONAL_PROPERTIES.descriptionGUID;
        final String attribute11Name            = "isDeprecated";
        final String attribute11Description     = "This element may still be used but is flagged that it will be removed at some point in the " +
                "future.";
        final String attribute11DescriptionGUID = null;

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
        property = archiveHelper.getStringTypeDefAttribute(attribute8Name,
                                                           attribute8Description,
                                                           attribute8DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute9Name,
                                                           attribute9Description,
                                                           attribute9DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute10Name,
                                                                    attribute10Description,
                                                                    attribute10DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getBooleanTypeDefAttribute(attribute11Name,
                                                            attribute11Description,
                                                            attribute11DescriptionGUID);
        properties.add(property);
        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    /**
     * 0512 - DerivedSchemaAttributes
     */
    private void update0512DerivedSchemaAttributes()
    {
        this.archiveBuilder.addTypeDefPatch(updateDerivedSchemaAttributeEntity());
    }

    /**
     * 0512 - SchemaAttribute entity is changed to show deprecated properties
     */
    private TypeDefPatch updateDerivedSchemaAttributeEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "DerivedSchemaAttribute";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);


        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "comment";
        final String attribute1Description     = "Comment from source system (deprecated).";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "id";
        final String attribute2Description     = "Id of derived schema attribute (deprecated).";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "aggregatingFunction";
        final String attribute3Description     = "Aggregating function of derived schema attribute (deprecated).";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        properties.add(property);

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute3Name,
                                                           attribute3Description,
                                                           attribute3DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }
}

