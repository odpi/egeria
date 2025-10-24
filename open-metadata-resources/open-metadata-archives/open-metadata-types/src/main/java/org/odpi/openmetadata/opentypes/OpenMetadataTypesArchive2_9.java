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
public class OpenMetadataTypesArchive2_9
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  archiveVersion     = "2.9";
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
    public OpenMetadataTypesArchive2_9()
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
    public OpenMetadataTypesArchive2_9(OMRSArchiveBuilder archiveBuilder)
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
        OpenMetadataTypesArchive2_8 previousTypes = new OpenMetadataTypesArchive2_8(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * Calls for new and changed types go here
         */
        update0056AssetManagers();
        update0450GovernanceRollout();
        update0217Ports();
    }




    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * Add UserProfileManager, UserAccessDirectory and MasterDataManager classification for Referenceables.
     */
    private void update0056AssetManagers()
    {
        this.archiveBuilder.addEntityDef(getUserProfileManager());
        this.archiveBuilder.addEntityDef(getUserAccessDirectory());
        this.archiveBuilder.addEntityDef(getMasterDataManager());
    }

    private EntityDef getUserProfileManager()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.USER_PROFILE_MANAGER,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.RESOURCE_MANAGER.typeName));
    }

    private EntityDef getUserAccessDirectory()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.USER_ACCESS_DIRECTORY,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.RESOURCE_MANAGER.typeName));
    }

    private EntityDef getMasterDataManager()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.MASTER_DATA_MANAGER,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.RESOURCE_MANAGER.typeName));
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * Add GovernanceMeasurements classification for Referenceables.
     */
    private void update0450GovernanceRollout()
    {
        this.archiveBuilder.addClassificationDef(getGovernanceMeasurementClassification());
    }

    private ClassificationDef getGovernanceMeasurementClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.GOVERNANCE_MEASUREMENTS_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DATA_COLLECTION_START_TIME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.COUNTS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.VALUES));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FLAGS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DATES));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DATA_COLLECTION_END_TIME));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * Add filterExpression attribute to Port.
     */
    private void update0217Ports()
    {
        this.archiveBuilder.addTypeDefPatch(updatePortEntity());
    }


    /**
     * Add filterExpression attribute to Port.
     * @return the type def patch
     */
    private TypeDefPatch updatePortEntity()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.PORT.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FILTER_EXPRESSION));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }
}

