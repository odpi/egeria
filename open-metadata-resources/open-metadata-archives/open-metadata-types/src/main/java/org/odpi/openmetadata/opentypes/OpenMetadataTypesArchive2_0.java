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
        update0220FilesAndFolders();
        update0221DocumentStores();
        update0224Databases();
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
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.TEMPLATE_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ADDITIONAL_PROPERTIES));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
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
        return archiveHelper.getClassificationDef(OpenMetadataType.FILE_MANAGER_CLASSIFICATION,
                                                  null,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName),
                                                  false);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void update0221DocumentStores()
    {
        this.archiveBuilder.addClassificationDef(getContentCollectionManagerClassification());
    }


    private ClassificationDef getContentCollectionManagerClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.CONTENT_COLLECTION_MANAGER,
                                                  null,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName),
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
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.DATABASE_MANAGER,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName));

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

