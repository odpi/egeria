/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


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


    private OMRSArchiveBuilder archiveBuilder;
    private OMRSArchiveHelper  archiveHelper;

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
        final String guid            = "53ef4062-9e0a-4892-9824-8d51d4ad59d3";
        final String name            = "UserProfileManager";
        final String description     = "A system that sores descriptions of individuals and their roles/interests in an organization.";
        final String descriptionGUID = null;

        final String linkedToEntity = "Referenceable";

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(linkedToEntity),
                                                  false);
    }

    private ClassificationDef getUserAccessDirectoryClassification()
    {
        final String guid            = "29c98cf7-32b3-47d2-a411-48c1c9967e6d";
        final String name            = "UserAccessDirectory";
        final String description     = "A system that stores the access rights and groups for users (people and automated processes).";
        final String descriptionGUID = null;

        final String linkedToEntity = "Referenceable";

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(linkedToEntity),
                                                  false);
    }

    private ClassificationDef getMasterDataManagerClassification()
    {
        final String guid            = "5bdad12e-57e7-4ff9-b7be-5d869e77d30b";
        final String name            = "MasterDataManager";
        final String description     = "A system that manages the consolidation and reconciliation of master data - typically people, organizations, products and accounts.";
        final String descriptionGUID = null;

        final String linkedToEntity = "Referenceable";

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(linkedToEntity),
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
        final String guid            = "207e2594-e3e4-4be8-a12c-4c401656e241";
        final String name            = "ActionTarget";
        final String description     = "Associates a To Do with one or more elements to work on.";
        final String descriptionGUID = null;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "ToDo";
        final String                     end1AttributeName            = "identifiedToDoActions";
        final String                     end1AttributeDescription     = "Actions that have been identified for this element.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "Referenceable";
        final String                     end2AttributeName            = "elementsToWorkOn";
        final String                     end2AttributeDescription     = "Elements that will be updated or used to complete the action.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
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

        final String linkedToEntity = "Referenceable";

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(linkedToEntity),
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

        final String linkedToEntity = "GlossaryTerm";

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(linkedToEntity),
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

