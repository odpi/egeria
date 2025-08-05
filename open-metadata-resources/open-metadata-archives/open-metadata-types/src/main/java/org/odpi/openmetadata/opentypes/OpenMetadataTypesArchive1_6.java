/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
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


    private final OMRSArchiveBuilder archiveBuilder;
    private final OMRSArchiveHelper  archiveHelper;

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
        update0505SchemaAttributes();
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

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MINIMUM_LENGTH));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LENGTH));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.IS_NULLABLE));

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

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SCHEMA_TYPE_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.QUALIFIED_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DISPLAY_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.USER_DEFINED_STATUS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.AUTHOR));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DEFAULT_VALUE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FIXED_VALUE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.USAGE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ADDITIONAL_PROPERTIES));

        typeDefPatch.setPropertyDefinitions(properties);

        ArrayList<InstanceStatus> validInstanceStatusList = new ArrayList<>();
        validInstanceStatusList.add(InstanceStatus.DRAFT);
        validInstanceStatusList.add(InstanceStatus.PREPARED);
        validInstanceStatusList.add(InstanceStatus.PROPOSED);
        validInstanceStatusList.add(InstanceStatus.APPROVED);
        validInstanceStatusList.add(InstanceStatus.REJECTED);
        validInstanceStatusList.add(InstanceStatus.ACTIVE);
        validInstanceStatusList.add(InstanceStatus.DEPRECATED);
        validInstanceStatusList.add(InstanceStatus.DELETED);

        typeDefPatch.setValidInstanceStatusList(validInstanceStatusList);
        typeDefPatch.setInitialStatus(InstanceStatus.ACTIVE);

        return typeDefPatch;
    }
}

