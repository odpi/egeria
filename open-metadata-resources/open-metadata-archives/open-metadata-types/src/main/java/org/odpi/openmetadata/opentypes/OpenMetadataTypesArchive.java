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
    private static final String                  archiveVersion     = "6.3-SNAPSHOT";
    private static final String                  originatorName     = "Egeria";
    private static final String                  originatorLicense  = "Apache-2.0";
    private static final Date                    creationDate       = new Date(1769277597779L);

    /*
     * Specific values for initializing TypeDefs
     */
    private static final long   versionNumber = 1L;
    private static final String versionName   = "6.3-SNAPSHOT";


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
        OpenMetadataTypesArchive6_2 previousTypes = new OpenMetadataTypesArchive6_2(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * New types for this release
         */
        update0795UsageActivity();
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0795UsageActivity()
    {
        this.archiveBuilder.addClassificationDef(getUsageActivityClassification());
        this.archiveBuilder.addClassificationDef(getMemberPopularityProfileClassification());
    }


    /**
     * Add the UsageActivity classification to count the activity around an anchor element and its anchored
     * elements.  It is typically attached to an Asset, DigitalProduct or GlossaryTerm, and so it is valid on any Referenceable.
     *
     * @return classification def
     */
    private ClassificationDef getUsageActivityClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.USAGE_ACTIVITY_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.EXPLICIT_GET_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FIND_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.UPDATE_PROPERTY_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.UPDATE_ATTACHMENT_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.UPDATE_FEEDBACK_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DELETE_ATTACHMENT_COUNT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DELETE_FEEDBACK_COUNT));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /**
     * Add the MemberPopularityProfile classification to summarize the usage activity around the members of an
     * element.  It is valid on any Referenceable.
     *
     * @return classification def
     */
    private ClassificationDef getMemberPopularityProfileClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.MEMBER_POPULARITY_PROFILE_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.EXPLICIT_GET_COUNT_TOTAL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.EXPLICIT_GET_COUNT_PROFILE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FIND_COUNT_TOTAL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FIND_COUNT_PROFILE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.UPDATE_PROPERTY_COUNT_TOTAL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.UPDATE_PROPERTY_COUNT_PROFILE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.UPDATE_ATTACHMENT_COUNT_TOTAL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.UPDATE_ATTACHMENT_COUNT_PROFILE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.UPDATE_FEEDBACK_COUNT_TOTAL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.UPDATE_FEEDBACK_COUNT_PROFILE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DELETE_ATTACHMENT_COUNT_TOTAL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DELETE_ATTACHMENT_COUNT_PROFILE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DELETE_FEEDBACK_COUNT_TOTAL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DELETE_FEEDBACK_COUNT_PROFILE));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


}
