/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
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
public class OpenMetadataTypesArchive4_3
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  archiveVersion     = "4.3";
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
    public OpenMetadataTypesArchive4_3()
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
    public OpenMetadataTypesArchive4_3(OMRSArchiveBuilder archiveBuilder)
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
        OpenMetadataTypesArchive4_2 previousTypes = new OpenMetadataTypesArchive4_2(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * Add the type updates
         */
        update0010Base();
        update0017ExternalIdentifiers();
        update0035Hosts();
        update0112People();
        update0210DataStores();
        update0212APIs();
        update0224Databases();
        update0215SoftwareComponents();
        update0223Events();
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0010Base()
    {
        this.archiveBuilder.addTypeDefPatch(updateDataSet());
        this.archiveBuilder.addTypeDefPatch(updateInfrastructure());
    }


    private TypeDefPatch updateDataSet()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.DATA_SET.typeName;

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

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

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    private TypeDefPatch updateInfrastructure()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.INFRASTRUCTURE.typeName;

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

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

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0017ExternalIdentifiers()
    {
        this.archiveBuilder.addTypeDefPatch(updateExternalId());
    }


    private TypeDefPatch updateExternalId()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.EXTERNAL_ID.typeName;

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.EXT_INSTANCE_CREATED_BY.name;
        final String attribute1Description     = OpenMetadataProperty.EXT_INSTANCE_CREATED_BY.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.EXT_INSTANCE_CREATED_BY.descriptionGUID;
        final String attribute2Name            = OpenMetadataProperty.EXT_INSTANCE_CREATION_TIME.name;
        final String attribute2Description     = OpenMetadataProperty.EXT_INSTANCE_CREATION_TIME.description;
        final String attribute2DescriptionGUID = OpenMetadataProperty.EXT_INSTANCE_CREATION_TIME.descriptionGUID;
        final String attribute3Name            = OpenMetadataProperty.EXT_INSTANCE_LAST_UPDATED_BY.name;
        final String attribute3Description     = OpenMetadataProperty.EXT_INSTANCE_LAST_UPDATED_BY.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.EXT_INSTANCE_LAST_UPDATED_BY.descriptionGUID;
        final String attribute4Name            = OpenMetadataProperty.EXT_INSTANCE_LAST_UPDATE_TIME.name;
        final String attribute4Description     = OpenMetadataProperty.EXT_INSTANCE_LAST_UPDATE_TIME.description;
        final String attribute4DescriptionGUID = OpenMetadataProperty.EXT_INSTANCE_LAST_UPDATE_TIME.descriptionGUID;
        final String attribute5Name            = OpenMetadataProperty.EXT_INSTANCE_VERSION.name;
        final String attribute5Description     = OpenMetadataProperty.EXT_INSTANCE_VERSION.description;
        final String attribute5DescriptionGUID = OpenMetadataProperty.EXT_INSTANCE_VERSION.descriptionGUID;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute2Name,
                                                         attribute2Description,
                                                         attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute3Name,
                                                           attribute3Description,
                                                           attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute4Name,
                                                         attribute4Description,
                                                         attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getLongTypeDefAttribute(attribute5Name,
                                                         attribute5Description,
                                                         attribute5DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0035Hosts()
    {
        this.archiveBuilder.addTypeDefPatch(updateHostClusterMemberRelationship());
    }


    private TypeDefPatch updateHostClusterMemberRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "HostClusterMember";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "memberRole";
        final String attribute1Description     = "The role of the member in the host cluster.  This value is typically defined by the technology of the host cluster.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = OpenMetadataProperty.ADDITIONAL_PROPERTIES.name;
        final String attribute2Description     = "Additional properties that define the configuration and properties of the member.";
        final String attribute2DescriptionGUID = null;


        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute2Name,
                                                                    attribute2Description,
                                                                    attribute2DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0112People()
    {
        this.archiveBuilder.addTypeDefPatch(updatePerson());
    }


    private TypeDefPatch updatePerson()
    {
        /*
         * Create the Patch
         */
        final String typeName = "Person";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "residentCountry";
        final String attribute1Description     = "Country that is the person's primary residence.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "timeZone";
        final String attribute2Description     = "Principle time zone where this person is located.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
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



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0210DataStores()
    {
        this.archiveBuilder.addTypeDefPatch(updateDataStore());
    }


    private TypeDefPatch updateDataStore()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.DATA_STORE.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                           OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.description,
                                                           OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.descriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0212APIs()
    {
        this.archiveBuilder.addTypeDefPatch(updateDeployedAPI());
    }


    private TypeDefPatch updateDeployedAPI()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.DEPLOYED_API.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                           OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.description,
                                                           OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.descriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0215SoftwareComponents()
    {
        this.archiveBuilder.addTypeDefPatch(updateDeployedSoftwareComponent());
    }


    private TypeDefPatch updateDeployedSoftwareComponent()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                           OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.description,
                                                           OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.descriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0223Events()
    {
        this.archiveBuilder.addTypeDefPatch(updateDataFeed());
    }


    private TypeDefPatch updateDataFeed()
    {
        /*
         * Create the Patch
         */
        final String typeName = "DataFeed";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                           OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.description,
                                                           OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.descriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0224Databases()
    {
        this.archiveBuilder.addEntityDef(addRelationalDatabaseEntity());
    }


    private EntityDef addRelationalDatabaseEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.RELATIONAL_DATABASE.typeGUID,
                                                 OpenMetadataType.RELATIONAL_DATABASE.typeName,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATABASE.typeName),
                                                 OpenMetadataType.RELATIONAL_DATABASE.description,
                                                 OpenMetadataType.RELATIONAL_DATABASE.descriptionGUID,
                                                 OpenMetadataType.RELATIONAL_DATABASE.wikiURL);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */
}

