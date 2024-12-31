/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


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
        update0137ToDos();
        update0450GovernanceRollout();
        update0217Ports();
        update0534RelationalSchema();
        add0335PrimaryCategoryClassification();
    }




    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * Add UserProfileManager, UserAccessDirectory and MasterDataManager classification for Referenceables.
     */
    private void update0056AssetManagers()
    {
        this.archiveBuilder.addClassificationDef(getUserProfileManagerClassification());
        this.archiveBuilder.addClassificationDef(getUserAccessDirectoryClassification());
        this.archiveBuilder.addClassificationDef(getMasterDataManagerClassification());
    }

    private ClassificationDef getUserProfileManagerClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.USER_PROFILE_MANAGER.typeGUID,
                                                  OpenMetadataType.USER_PROFILE_MANAGER.typeName,
                                                  null,
                                                  OpenMetadataType.USER_PROFILE_MANAGER.description,
                                                  OpenMetadataType.USER_PROFILE_MANAGER.descriptionGUID,
                                                  OpenMetadataType.USER_PROFILE_MANAGER.wikiURL,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                  false);
    }

    private ClassificationDef getUserAccessDirectoryClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.USER_ACCESS_DIRECTORY.typeGUID,
                                                  OpenMetadataType.USER_ACCESS_DIRECTORY.typeName,
                                                  null,
                                                  OpenMetadataType.USER_ACCESS_DIRECTORY.description,
                                                  OpenMetadataType.USER_ACCESS_DIRECTORY.descriptionGUID,
                                                  OpenMetadataType.USER_ACCESS_DIRECTORY.wikiURL,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                  false);
    }

    private ClassificationDef getMasterDataManagerClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.MASTER_DATA_MANAGER,
                                                  null,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                  false);
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * Add ActionTarget relationship for ToDos.
     */
    private void update0137ToDos()
    {
        this.archiveBuilder.addRelationshipDef(getActionTargetRelationship());
    }


    private RelationshipDef getActionTargetRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.ACTION_TARGET_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "identifiedToDoActions";
        final String                     end1AttributeDescription     = "Actions that have been identified for this element.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.TO_DO.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "elementsToWorkOn";
        final String                     end2AttributeDescription     = "Elements that will be updated or used to complete the action.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
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
        final String guid            = "9d99d962-0214-49ba-83f7-c9b1f9f5bed4";
        final String name            = "GovernanceMeasurements";
        final String description     = "A set of measurements on the performance and use of the connected resource.";
        final String descriptionGUID = null;

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "measurementCounts";
        final String attribute1Description     = "A set of metric name to current count value pairs.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "measurementValues";
        final String attribute2Description     = "A set of metric name to current value pairs.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "measurementFlags";
        final String attribute3Description     = "A set of metric name to current boolean value pairs.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getMapStringIntTypeDefAttribute(attribute1Name,
                                                                 attribute1Description,
                                                                 attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute2Name,
                                                                    attribute2Description,
                                                                    attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringBooleanTypeDefAttribute(attribute3Name,
                                                                     attribute3Description,
                                                                     attribute3DescriptionGUID);
        properties.add(property);

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
        final String typeName = "Port";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "filterExpression";
        final String attribute1Description     = "Expression used to filter data values passing through port.";
        final String attribute1DescriptionGUID = null;

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


    /**
     * The RelationalColumnType only allows for a column to be primitive. - could be a literal, enum or external.
     */
    private void update0534RelationalSchema()
    {
        this.archiveBuilder.addTypeDefPatch(deprecateRelationalColumnType());
    }

    private TypeDefPatch deprecateRelationalColumnType()
    {
        final String typeName = "RelationalColumnType";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0335  Add the Primary Category Classification
     */
    private void add0335PrimaryCategoryClassification()
    {
        this.archiveBuilder.addClassificationDef(addPrimaryCategoryClassification());
    }

    private ClassificationDef addPrimaryCategoryClassification()
    {
        final String guid            = "3a6c4ba7-3cc5-48cd-8952-bwra92da016d";
        final String name            = "PrimaryCategory";
        final String description     = "Defines a category as being the base category of a glossary term";
        final String descriptionGUID = null;

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.GLOSSARY_TERM.typeName),
                                                                                 true);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "categoryQualifiedName";
        final String attribute1Description     = "The qualified name of the primary category of a GlossaryTerm.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }
}

