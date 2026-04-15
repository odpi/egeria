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
    private static final String                  archiveVersion     = "6.1-SNAPSHOT";
    private static final String                  originatorName     = "Egeria";
    private static final String                  originatorLicense  = "Apache-2.0";
    private static final Date                    creationDate       = new Date(1769277597779L);

    /*
     * Specific values for initializing TypeDefs
     */
    private static final long   versionNumber = 1L;
    private static final String versionName   = "6.1-SNAPSHOT";


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
        OpenMetadataTypesArchive6_0 previousTypes = new OpenMetadataTypesArchive6_0(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * New types for this release
         */
        update0112Person();
        update01135ActionsForPeople();
        update0424GovernanceZones();
        update0455ExceptionManagement();
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0112Person()
    {
        this.archiveBuilder.addTypeDefPatch(getPeerRelationshipPatch());
    }

    private TypeDefPatch getPeerRelationshipPatch()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.PEER_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LABEL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;

    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update01135ActionsForPeople()
    {
        this.archiveBuilder.addEntityDef(getActivityEntryEntity());
        this.archiveBuilder.addEntityDef(getBlogEntryEntity());
        this.archiveBuilder.addEntityDef(getJournalEntryEntity());
    }


    private EntityDef getActivityEntryEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.ACTIVITY_ENTRY,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.NOTIFICATION.typeName));
    }

    private EntityDef getBlogEntryEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.BLOG_ENTRY,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.NOTIFICATION.typeName));
    }

    private EntityDef getJournalEntryEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.JOURNAL_ENTRY,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.NOTIFICATION.typeName));
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0424GovernanceZones()
    {
        this.archiveBuilder.addTypeDefPatch(updateZoneMembershipProfileClassification());
    }

    private TypeDefPatch updateZoneMembershipProfileClassification()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.ZONE_MEMBERSHIP_PROFILE_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.TOTAL_MEMBERSHIP));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.TYPE_MEMBERSHIP));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ANALYSIS_TIME));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0455ExceptionManagement()
    {
        this.archiveBuilder.addClassificationDef(getSecurityLogClassification());
        this.archiveBuilder.addEntityDef(getExceptionTypeEntity());
        this.archiveBuilder.addRelationshipDef(getExceptionRelationship());
    }

    private ClassificationDef getSecurityLogClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.SECURITY_LOG_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.ASSET.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PROCESS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.NOTES));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SOURCE));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }

    private EntityDef getExceptionTypeEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.EXCEPTION_TYPE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_CONTROL.typeName));
    }


    private RelationshipDef getExceptionRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.EXCEPTION_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        relationshipDef.setMultiLink(true);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "exceptions";
        final String                     end1AttributeDescription     = "Types of exception assigned to this element.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "excludedFromRequirements";
        final String                     end2AttributeDescription     = "The elements that are non-compliant with the associated policy.  The exception type defines the nature of the non-compliance.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.EXCEPTION_TYPE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LABEL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LAST_REVIEW_TIME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REVIEW_DATE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CONDITIONS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STEWARD));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STEWARD_TYPE_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STEWARD_PROPERTY_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.NOTES));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

}

