/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
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
    private static final String                  archiveVersion     = "4.4";
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
        OpenMetadataTypesArchive4_3 previousTypes = new OpenMetadataTypesArchive4_3(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * Add the type updates
         */
        update0010BaseModel();
        update0011UpdateTemplates();
        update0021Collections();
        update0050ApplicationsAndProcesses();
        update0137Actions();
        update0130Projects();
        update0201Connections();
        update0210DataStores();
        update0220DataFiles();
        update0221MediaFiles();
        add0226ArchiveFiles();
        update0280SoftwareArtifacts();
        update0380TermInheritance();
        update0461GovernanceEngines();
        update0464IntegrationGroups();
        update0545ReferenceData();
        update0462GovernanceActionTypes();
        add00475ContextEvents();
        add0603SurveyReports();
        update0615SchemaExtraction();
        update0690RequestForAction();
        add0755UltimateSourcesDestinations();
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0010BaseModel()
    {
        this.archiveBuilder.addTypeDefPatch(updateAnchors());
    }

    private TypeDefPatch updateAnchors()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.ANCHORS_CLASSIFICATION.typeName;

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.ANCHOR_TYPE_NAME.name;
        final String attribute1Description     = OpenMetadataProperty.ANCHOR_TYPE_NAME.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.ANCHOR_TYPE_NAME.descriptionGUID;

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

    private void update0021Collections()
    {
        this.archiveBuilder.addTypeDefPatch(updateCollection());
        this.archiveBuilder.addClassificationDef(getRootCollectionClassification());
        this.archiveBuilder.addClassificationDef(getDataSpecClassification());
        this.archiveBuilder.addClassificationDef(getHomeCollectionClassification());
        this.archiveBuilder.addClassificationDef(getRecentAccessClassification());
        this.archiveBuilder.addClassificationDef(getWorkItemListClassification());
    }

    private TypeDefPatch updateCollection()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.COLLECTION.typeName;

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.COLLECTION_TYPE.name;
        final String attribute1Description     = OpenMetadataProperty.COLLECTION_TYPE.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.COLLECTION_TYPE.descriptionGUID;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    private ClassificationDef getRootCollectionClassification()
    {
        final String guid            = OpenMetadataType.ROOT_COLLECTION.typeGUID;
        final String name            = OpenMetadataType.ROOT_COLLECTION.typeName;
        final String description     = OpenMetadataType.ROOT_COLLECTION.description;
        final String descriptionGUID = OpenMetadataType.ROOT_COLLECTION.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.ROOT_COLLECTION.wikiURL;

        final String linkedToEntity = OpenMetadataType.COLLECTION.typeName;

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  descriptionWiki,
                                                  this.archiveBuilder.getEntityDef(linkedToEntity),
                                                  false);
    }


    private ClassificationDef getDataSpecClassification()
    {
        final String guid            = OpenMetadataType.DATA_SPEC_COLLECTION.typeGUID;
        final String name            = OpenMetadataType.DATA_SPEC_COLLECTION.typeName;
        final String description     = OpenMetadataType.DATA_SPEC_COLLECTION.description;
        final String descriptionGUID = OpenMetadataType.DATA_SPEC_COLLECTION.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.DATA_SPEC_COLLECTION.wikiURL;

        final String linkedToEntity = OpenMetadataType.COLLECTION.typeName;

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  descriptionWiki,
                                                  this.archiveBuilder.getEntityDef(linkedToEntity),
                                                  false);
    }

    private ClassificationDef getHomeCollectionClassification()
    {
        final String guid            = OpenMetadataType.HOME_COLLECTION.typeGUID;
        final String name            = OpenMetadataType.HOME_COLLECTION.typeName;
        final String description     = OpenMetadataType.HOME_COLLECTION.description;
        final String descriptionGUID = OpenMetadataType.HOME_COLLECTION.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.HOME_COLLECTION.wikiURL;

        final String linkedToEntity = OpenMetadataType.COLLECTION.typeName;

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  descriptionWiki,
                                                  this.archiveBuilder.getEntityDef(linkedToEntity),
                                                  false);
    }


    private ClassificationDef getRecentAccessClassification()
    {
        final String guid            = OpenMetadataType.RECENT_ACCESS.typeGUID;
        final String name            = OpenMetadataType.RECENT_ACCESS.typeName;
        final String description     = OpenMetadataType.RECENT_ACCESS.description;
        final String descriptionGUID = OpenMetadataType.RECENT_ACCESS.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.RECENT_ACCESS.wikiURL;

        final String linkedToEntity = OpenMetadataType.COLLECTION.typeName;

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  descriptionWiki,
                                                  this.archiveBuilder.getEntityDef(linkedToEntity),
                                                  false);
    }


    private ClassificationDef getWorkItemListClassification()
    {
        final String guid            = OpenMetadataType.WORK_ITEM_LIST.typeGUID;
        final String name            = OpenMetadataType.WORK_ITEM_LIST.typeName;
        final String description     = OpenMetadataType.WORK_ITEM_LIST.description;
        final String descriptionGUID = OpenMetadataType.WORK_ITEM_LIST.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.WORK_ITEM_LIST.wikiURL;

        final String linkedToEntity = OpenMetadataType.COLLECTION.typeName;

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  descriptionWiki,
                                                  this.archiveBuilder.getEntityDef(linkedToEntity),
                                                  false);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * Add more software server capabilities
     */
    private void update0050ApplicationsAndProcesses()
    {
        this.archiveBuilder.addEntityDef(addEventManagerEntity());
        this.archiveBuilder.addEntityDef(addAuthorizationManagerEntity());
        this.archiveBuilder.addEntityDef(addUserAuthenticationManagerEntity());
    }


    /**
     * This new subtype of software server capability for event managers.
     *
     * @return entity definition
     */
    private EntityDef addEventManagerEntity()
    {
        final String guid            = OpenMetadataType.EVENT_MANAGER.typeGUID;
        final String name            = OpenMetadataType.EVENT_MANAGER.typeName;
        final String description     = OpenMetadataType.EVENT_MANAGER.description;
        final String descriptionGUID = OpenMetadataType.EVENT_MANAGER.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.EVENT_MANAGER.wikiURL;

        final String superTypeName = OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID,
                                                 descriptionWiki);
    }


    /**
     * This new subtype of software server capability for event managers.
     *
     * @return entity definition
     */
    private EntityDef addAuthorizationManagerEntity()
    {
        final String guid            = OpenMetadataType.AUTHORIZATION_MANAGER.typeGUID;
        final String name            = OpenMetadataType.AUTHORIZATION_MANAGER.typeName;
        final String description     = OpenMetadataType.AUTHORIZATION_MANAGER.description;
        final String descriptionGUID = OpenMetadataType.AUTHORIZATION_MANAGER.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.AUTHORIZATION_MANAGER.wikiURL;

        final String superTypeName = OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID,
                                                 descriptionWiki);
    }


    /**
     * This new subtype of software server capability for event managers.
     *
     * @return entity definition
     */
    private EntityDef addUserAuthenticationManagerEntity()
    {
        final String guid            = OpenMetadataType.USER_AUTHENTICATION_MANAGER.typeGUID;
        final String name            = OpenMetadataType.USER_AUTHENTICATION_MANAGER.typeName;
        final String description     = OpenMetadataType.USER_AUTHENTICATION_MANAGER.description;
        final String descriptionGUID = OpenMetadataType.USER_AUTHENTICATION_MANAGER.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.USER_AUTHENTICATION_MANAGER.wikiURL;

        final String superTypeName = OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID,
                                                 descriptionWiki);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * update0137Actions
     */
    private void update0137Actions()
    {
        this.archiveBuilder.addEntityDef(addActionEntity());
        this.archiveBuilder.addTypeDefPatch(updateToDoEntity());
        this.archiveBuilder.addTypeDefPatch(updateEngineActionEntity());
        this.archiveBuilder.addTypeDefPatch(updateActionsRelationship());
    }


    /**
     * Add Action as a new superclass for EngineAction and To Do entities
     *
     * @return entity def
     */
    private EntityDef addActionEntity()
    {
        final String guid            = OpenMetadataType.ACTION.typeGUID;
        final String name            = OpenMetadataType.ACTION.typeName;
        final String description     = OpenMetadataType.ACTION.description;
        final String descriptionGUID = OpenMetadataType.ACTION.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.ACTION.wikiURL;

        final String superTypeName = OpenMetadataType.REFERENCEABLE.typeName;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID,
                                                 descriptionWiki);
    }


    /**
     * Update To Do's superclass to Action
     *
     * @return type def patch
     */
    private TypeDefPatch updateToDoEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.TO_DO_TYPE_NAME;
        final String superTypeName = OpenMetadataType.ACTION.typeName;

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(superTypeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataType.LAST_REVIEW_TIME_PROPERTY_NAME;
        final String attribute1Description     = "The Date/time that this action was reviewed.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /**
     * Update EngineAction's superclass to Action
     *
     * @return type def patch
     */
    private TypeDefPatch updateEngineActionEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.ENGINE_ACTION_TYPE_NAME;
        final String superTypeName = OpenMetadataType.ACTION.typeName;

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(superTypeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.REQUESTER_USER_ID.name;
        final String attribute1Description     = OpenMetadataProperty.REQUESTER_USER_ID.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.REQUESTER_USER_ID.descriptionGUID;
        final String attribute2Name            = "requestedStartDate";
        final String attribute2Description     = "Time that the ending action should start.  This is the request time plus any requested wait time.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        property = archiveHelper.getDateTypeDefAttribute(attribute2Name,
                                                         attribute2Description,
                                                         attribute2DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /**
     * Update the Actions relationship to point to Action at end 2
     *
     * @return type def patch
     */
    private TypeDefPatch updateActionsRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.ACTION_SPONSOR_RELATIONSHIP_TYPE_NAME;

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = OpenMetadataType.REFERENCEABLE.typeName;
        final String                     end1AttributeName            = "actionCause";
        final String                     end1AttributeDescription     = "Rule or meeting that is driving the need for the to do.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        RelationshipEndDef relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                                    end1AttributeName,
                                                                                    end1AttributeDescription,
                                                                                    end1AttributeDescriptionGUID,
                                                                                    end1Cardinality);
        typeDefPatch.setEndDef1(relationshipEndDef);

        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = OpenMetadataType.ACTION.typeName;
        final String                     end2AttributeName            = "relatedActions";
        final String                     end2AttributeDescription     = "Potentially impacting requests for change.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);

        typeDefPatch.setEndDef2(relationshipEndDef);

        return typeDefPatch;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * update0130Projects
     */
    private void update0130Projects()
    {
        this.archiveBuilder.addTypeDefPatch(updateProject());
        this.archiveBuilder.addClassificationDef(getPersonalProjectClassification());
        this.archiveBuilder.addClassificationDef(getStudyProjectClassification());
    }


    private TypeDefPatch updateProject()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.PROJECT.typeName;

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.PROJECT_HEALTH.name;
        final String attribute1Description     = OpenMetadataProperty.PROJECT_HEALTH.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.PROJECT_HEALTH.descriptionGUID;
        final String attribute3Name            = OpenMetadataProperty.PROJECT_PHASE.name;
        final String attribute3Description     = OpenMetadataProperty.PROJECT_PHASE.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.PROJECT_PHASE.descriptionGUID;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute3Name,
                                                           attribute3Description,
                                                           attribute3DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /**
     * Add PersonalProject classification to allow people to use projects to organize their work.
     *
     * @return classification def
     */
    private ClassificationDef getPersonalProjectClassification()
    {
        final String guid            = OpenMetadataType.PERSONAL_PROJECT_CLASSIFICATION.typeGUID;
        final String name            = OpenMetadataType.PERSONAL_PROJECT_CLASSIFICATION.typeName;
        final String description     = OpenMetadataType.PERSONAL_PROJECT_CLASSIFICATION.description;
        final String descriptionGUID = OpenMetadataType.PERSONAL_PROJECT_CLASSIFICATION.descriptionGUID;

        final String linkedToEntity = OpenMetadataType.PROJECT.typeName;

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(linkedToEntity),
                                                  false);
    }



    /**
     * Add StudyProject classification to allow specific types of investigations to be characterized.
     *
     * @return classification def
     */
    private ClassificationDef getStudyProjectClassification()
    {
        final String guid            = OpenMetadataType.STUDY_PROJECT_CLASSIFICATION.typeGUID;
        final String name            = OpenMetadataType.STUDY_PROJECT_CLASSIFICATION.typeName;
        final String description     = OpenMetadataType.STUDY_PROJECT_CLASSIFICATION.description;
        final String descriptionGUID = OpenMetadataType.STUDY_PROJECT_CLASSIFICATION.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.STUDY_PROJECT_CLASSIFICATION.wikiURL;

        final String linkedToEntity = OpenMetadataType.PROJECT.typeName;

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  descriptionWiki,
                                                  this.archiveBuilder.getEntityDef(linkedToEntity),
                                                  false);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * update0011UpdateTemplates
     */
    private void update0011UpdateTemplates()
    {
        this.archiveBuilder.addRelationshipDef(addCatalogTemplateRelationship());
        this.archiveBuilder.addTypeDefPatch(updateTemplateClassification());
    }


    /**
     * Add CatalogTemplate
     *
     * @return relationship def
     */
    private RelationshipDef addCatalogTemplateRelationship()
    {
        final String guid            = OpenMetadataType.CATALOG_TEMPLATE_RELATIONSHIP.typeGUID;
        final String name            = OpenMetadataType.CATALOG_TEMPLATE_RELATIONSHIP.typeName;
        final String description     = OpenMetadataType.CATALOG_TEMPLATE_RELATIONSHIP.description;
        final String descriptionGUID = OpenMetadataType.CATALOG_TEMPLATE_RELATIONSHIP.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.CATALOG_TEMPLATE_RELATIONSHIP.wikiURL;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                descriptionWiki,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = OpenMetadataType.OPEN_METADATA_ROOT.typeName;
        final String                     end1AttributeName            = "implementationTypes";
        final String                     end1AttributeDescription     = "Description of a type of technology.";
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
        final String                     end2EntityType               = OpenMetadataType.OPEN_METADATA_ROOT.typeName;
        final String                     end2AttributeName            = "templatesForCataloguing";
        final String                     end2AttributeDescription     = "Template element for a new catalog entry.";
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


    /**
     * Add new properties to TemplateClassification to describe the replacement properties and the
     * placeholder properties.
     *
     * @return  patch
     */
    private TypeDefPatch updateTemplateClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName;

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.VERSION_IDENTIFIER.name;
        final String attribute1Description     = OpenMetadataProperty.VERSION_IDENTIFIER.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.VERSION_IDENTIFIER.descriptionGUID;

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

    private void update0201Connections()
    {
        this.archiveBuilder.addTypeDefPatch(updateConnectorType());
    }


    private TypeDefPatch updateConnectorType()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.CONNECTOR_TYPE_TYPE_NAME;

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name;
        final String attribute1Description     = "The type of technology that this connector works with.  It is used to match connectors to assets.";
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
     * Update 0210 DataStores model with new DataScope classification.
     */
    private void update0210DataStores()
    {
        this.archiveBuilder.addClassificationDef(getDataScopeClassification());
    }


    /**
     * Add the DataScope classification to allow details of the scope of data represented by the attached element.
     *
     * @return classification def
     */
    private ClassificationDef getDataScopeClassification()
    {
        final String guid            = "22f996d0-c4b7-433a-af0b-6a3e9478e488";
        final String name            = "DataScope";
        final String description     = "Defines the scope of the data held in the associated data resource.";
        final String descriptionGUID = null;

        final String linkedToEntity = OpenMetadataType.REFERENCEABLE.typeName;

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

        final String attribute1Name            = OpenMetadataType.MIN_LONGITUDE_PROPERTY_NAME;
        final String attribute1Description     = "If the data is bound by an area, this is the longitude for bottom-left corner of the bounding box (BBOX) for the area covered by the data.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = OpenMetadataType.MIN_LATITUDE_PROPERTY_NAME;
        final String attribute2Description     = "If the data is bound by an area, this is the latitude for the bottom-left corner of the bounding box (BBOX) for the area covered by the data.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = OpenMetadataType.MAX_LONGITUDE_PROPERTY_NAME;
        final String attribute3Description     = "If the data is bound by an area, this is the longitude for top-right corner of the bounding box (BBOX) for the area covered by the data..";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = OpenMetadataType.MAX_LATITUDE_PROPERTY_NAME;
        final String attribute4Description     = "If the data is bound by an area, this is the latitude for top-right corner of the bounding box (BBOX) for the area covered by the data.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = OpenMetadataType.MIN_HEIGHT_PROPERTY_NAME;
        final String attribute5Description     = "If the height above ground is relevant, this is the lowest height that the data covers.";
        final String attribute5DescriptionGUID = null;
        final String attribute6Name            = OpenMetadataType.MAX_HEIGHT_PROPERTY_NAME;
        final String attribute6Description     = "If the height above ground is relevant, this is the highest height that the data covers.";
        final String attribute6DescriptionGUID = null;
        final String attribute7Name            = OpenMetadataType.START_TIME_PROPERTY_NAME;
        final String attribute7Description     = "If the data is bound by time, this is the start time.";
        final String attribute7DescriptionGUID = null;
        final String attribute8Name            = OpenMetadataType.END_TIME_PROPERTY_NAME;
        final String attribute8Description     = "If the data is bound by time, this is the end time.";
        final String attribute8DescriptionGUID = null;
        final String attribute9Name            = OpenMetadataProperty.ADDITIONAL_PROPERTIES.name;
        final String attribute9Description     = "Supports other properties that identify the scope of the data represented by this element.";
        final String attribute9DescriptionGUID = null;

        property = archiveHelper.getFloatTypeDefAttribute(attribute1Name,
                                                          attribute1Description,
                                                          attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getFloatTypeDefAttribute(attribute2Name,
                                                          attribute2Description,
                                                          attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getFloatTypeDefAttribute(attribute3Name,
                                                          attribute3Description,
                                                          attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getFloatTypeDefAttribute(attribute4Name,
                                                          attribute4Description,
                                                          attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getFloatTypeDefAttribute(attribute5Name,
                                                          attribute5Description,
                                                          attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getFloatTypeDefAttribute(attribute6Name,
                                                          attribute6Description,
                                                          attribute6DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute7Name,
                                                         attribute7Description,
                                                         attribute7DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute8Name,
                                                         attribute8Description,
                                                         attribute8DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute9Name,
                                                                    attribute9Description,
                                                                    attribute9DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0220DataFiles()
    {
        this.archiveBuilder.addTypeDefPatch(updateDataFile());
        this.archiveBuilder.addEntityDef(addXMLFileEntity());
        this.archiveBuilder.addEntityDef(addSpreadsheetFileEntity());
    }


    /**
     * Update DataFile with an explicit property for fileExtension.  Prior to this change the file extension
     * was stored in the fileType property.  The fileType property is now a logical fileType.
     *
     * @return type def patch
     */
    private TypeDefPatch updateDataFile()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.DATA_FILE.typeName;

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.FILE_EXTENSION.name;
        final String attribute1Description     = OpenMetadataProperty.FILE_EXTENSION.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.FILE_EXTENSION.descriptionGUID;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /**
     * Add SpreadsheetFile
     *
     * @return entity def
     */
    private EntityDef addSpreadsheetFileEntity()
    {
        final String guid = OpenMetadataType.SPREADSHEET_FILE_TYPE_GUID;

        final String name            = OpenMetadataType.SPREADSHEET_FILE_TYPE_NAME;
        final String description     = "A file containing tabular data with formula.";
        final String descriptionGUID = null;

        final String superTypeName = OpenMetadataType.DATA_FILE.typeName;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    /**
     * Add XMLFile
     *
     * @return entity def
     */
    private EntityDef addXMLFileEntity()
    {
        final String guid = OpenMetadataType.XML_FILE_TYPE_GUID;

        final String name            = OpenMetadataType.XML_FILE_TYPE_NAME;
        final String description     = "A file containing an XML structure.";
        final String descriptionGUID = null;

        final String superTypeName = OpenMetadataType.DATA_FILE.typeName;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * Update 0221 Media Files models with new types of files
     */
    private void update0221MediaFiles()
    {
        this.archiveBuilder.addEntityDef(addAudioFileEntity());
        this.archiveBuilder.addEntityDef(addVideoFileEntity());
        this.archiveBuilder.addEntityDef(add3DImageFileEntity());
        this.archiveBuilder.addEntityDef(addRasterFileEntity());
        this.archiveBuilder.addEntityDef(addVectorFileEntity());
        this.archiveBuilder.addTypeDefPatch(deprecateGroupedMedia());
    }


    /**
     * Add AudioFile.
     *
     * @return entity def
     */
    private EntityDef addAudioFileEntity()
    {
        final String guid            = OpenMetadataType.AUDIO_FILE_TYPE_GUID;
        final String name            = OpenMetadataType.AUDIO_FILE_TYPE_NAME;
        final String description     = "A file containing an audio recording.";
        final String descriptionGUID = null;

        final String superTypeName = OpenMetadataType.MEDIA_FILE_TYPE_NAME;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    /**
     * Add VideoFile.
     *
     * @return entity def
     */
    private EntityDef addVideoFileEntity()
    {
        final String guid            = OpenMetadataType.VIDEO_FILE_TYPE_GUID;
        final String name            = OpenMetadataType.VIDEO_FILE_TYPE_NAME;
        final String description     = "A file containing a video recording.";
        final String descriptionGUID = null;

        final String superTypeName = OpenMetadataType.MEDIA_FILE_TYPE_NAME;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    /**
     * Add 3DImageFile.
     *
     * @return entity def
     */
    private EntityDef add3DImageFileEntity()
    {
        final String guid            = OpenMetadataType.THREE_D_IMAGE_FILE_TYPE_GUID;
        final String name            = OpenMetadataType.THREE_D_IMAGE_FILE_TYPE_NAME;
        final String description     = "A file containing a three dimensional image.";
        final String descriptionGUID = null;

        final String superTypeName = OpenMetadataType.MEDIA_FILE_TYPE_NAME;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    /**
     * Add RasterFile.
     *
     * @return entity def
     */
    private EntityDef addRasterFileEntity()
    {
        final String guid            = OpenMetadataType.RASTER_FILE_TYPE_GUID;
        final String name            = OpenMetadataType.RASTER_FILE_TYPE_NAME;
        final String description     = "A file containing an image as a matrix of pixels.";
        final String descriptionGUID = null;

        final String superTypeName = OpenMetadataType.MEDIA_FILE_TYPE_NAME;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    /**
     * Add VectorFile
     *
     * @return entity def
     */
    private EntityDef addVectorFileEntity()
    {
        final String guid            = OpenMetadataType.VECTOR_FILE_TYPE_GUID;
        final String name            = OpenMetadataType.VECTOR_FILE_TYPE_NAME;
        final String description     = "A file containing an image described using mathematical formulas.";
        final String descriptionGUID = null;

        final String superTypeName = OpenMetadataType.MEDIA_FILE_TYPE_NAME;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    /**
     * Deprecate the GroupedMedia - use DataContentForDataSet
     *
     * @return patch
     */
    private TypeDefPatch deprecateGroupedMedia()
    {
        final String typeName = "GroupedMedia";

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
     * Add new model 0226 ArchiveFiles
     */
    private void add0226ArchiveFiles()
    {
        this.archiveBuilder.addEntityDef(addArchiveFileEntity());
        this.archiveBuilder.addRelationshipDef(addArchiveContentsRelationship());
    }


    /**
     * Add ArchiveFile
     *
     * @return entity def
     */
    private EntityDef addArchiveFileEntity()
    {
        final String guid            = OpenMetadataType.ARCHIVE_FILE_TYPE_GUID;
        final String name            = OpenMetadataType.ARCHIVE_FILE_TYPE_NAME;
        final String description     = "A file containing compressed files.  These files may be organized into a directory (folder) structure.";
        final String descriptionGUID = null;

        final String superTypeName = OpenMetadataType.DATA_FILE.typeName;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    /**
     * Add ArchiveContents
     *
     * @return relationship def
     */
    private RelationshipDef addArchiveContentsRelationship()
    {
        final String guid            = OpenMetadataType.ARCHIVE_CONTENTS_TYPE_GUID;
        final String name            = OpenMetadataType.ARCHIVE_CONTENTS_TYPE_NAME;
        final String description     = "Links an archive to a collection that has a description of the archive's contents as its members.";
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
        final String                     end1EntityType               = OpenMetadataType.ARCHIVE_FILE_TYPE_NAME;
        final String                     end1AttributeName            = "packagedInArchiveFiles";
        final String                     end1AttributeDescription     = "Associated archive file.";
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
        final String                     end2EntityType               = OpenMetadataType.COLLECTION.typeName;
        final String                     end2AttributeName            = "archiveFileContents";
        final String                     end2AttributeDescription     = "Collection describing the archive's contents.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.AT_MOST_ONE;

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

    private void update0280SoftwareArtifacts()
    {
        this.archiveBuilder.addEntityDef(addSourceCodeFileEntity());
        this.archiveBuilder.addEntityDef(addBuildInstructionFileEntity());
        this.archiveBuilder.addEntityDef(addExecutableFileEntity());
        this.archiveBuilder.addEntityDef(addScriptFileEntity());
        this.archiveBuilder.addEntityDef(addPropertiesFileEntity());
        this.archiveBuilder.addEntityDef(addYAMLFileEntity());
    }


    /**
     * Add SourceCodeFile
     *
     * @return entity def
     */
    private EntityDef addSourceCodeFileEntity()
    {
        final String guid            = OpenMetadataType.SOURCE_CODE_FILE_TYPE_GUID;
        final String name            = OpenMetadataType.SOURCE_CODE_FILE_TYPE_NAME;
        final String description     = "A file containing an audio recording.";
        final String descriptionGUID = null;

        final String superTypeName = OpenMetadataType.DATA_FILE.typeName;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(superTypeName),
                                                                description,
                                                                descriptionGUID);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataType.PROGRAMMING_LANGUAGE_PROPERTY_NAME;
        final String attribute1Description     = "The programming language used in the code.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /**
     * Add BuildInstructionFile
     *
     * @return entity def
     */
    private EntityDef addBuildInstructionFileEntity()
    {
        final String guid            = OpenMetadataType.BUILD_INSTRUCTION_FILE_TYPE_GUID;
        final String name            = OpenMetadataType.BUILD_INSTRUCTION_FILE_TYPE_NAME;
        final String description     = "A file containing instructions to run a build of a software artifact or system.";
        final String descriptionGUID = null;

        final String superTypeName = OpenMetadataType.DATA_FILE.typeName;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(superTypeName),
                                                                description,
                                                                descriptionGUID);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataType.PROGRAMMING_LANGUAGE_PROPERTY_NAME;
        final String attribute1Description     = "The programming language used in the code.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /**
     * Add ExecutableFile
     *
     * @return entity def
     */
    private EntityDef addExecutableFileEntity()
    {
        final String guid            = OpenMetadataType.EXECUTABLE_FILE_TYPE_GUID;
        final String name            = OpenMetadataType.EXECUTABLE_FILE_TYPE_NAME;
        final String description     = "A file containing compiled code that can be executed.";
        final String descriptionGUID = null;

        final String superTypeName = OpenMetadataType.DATA_FILE.typeName;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(superTypeName),
                                                                description,
                                                                descriptionGUID);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataType.PROGRAMMING_LANGUAGE_PROPERTY_NAME;
        final String attribute1Description     = "The programming language used in the code.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /**
     * Add ScriptFile
     *
     * @return entity def
     */
    private EntityDef addScriptFileEntity()
    {
        final String guid            = OpenMetadataType.SCRIPT_FILE_TYPE_GUID;
        final String name            = OpenMetadataType.SCRIPT_FILE_TYPE_NAME;
        final String description     = "A file containing code that is interpreted when it is run.";
        final String descriptionGUID = null;

        final String superTypeName = OpenMetadataType.DATA_FILE.typeName;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(superTypeName),
                                                                description,
                                                                descriptionGUID);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataType.PROGRAMMING_LANGUAGE_PROPERTY_NAME;
        final String attribute1Description     = "The programming language used in the code.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /**
     * Add PropertiesFile
     *
     * @return entity def
     */
    private EntityDef addPropertiesFileEntity()
    {
        final String guid            = OpenMetadataType.PROPERTIES_FILE_TYPE_GUID;
        final String name            = OpenMetadataType.PROPERTIES_FILE_TYPE_NAME;
        final String description     = "A file containing a list of properties, typically used for configuration.";
        final String descriptionGUID = null;

        final String superTypeName = OpenMetadataType.DATA_FILE.typeName;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    /**
     * Add YAMLFile
     *
     * @return entity def
     */
    private EntityDef addYAMLFileEntity()
    {
        final String guid            = OpenMetadataType.YAML_FILE_TYPE_GUID;
        final String name            = OpenMetadataType.YAML_FILE_TYPE_NAME;
        final String description     = "A file containing properties in YAML format.  This it typically used for configuration";
        final String descriptionGUID = null;

        final String superTypeName = OpenMetadataType.PROPERTIES_FILE_TYPE_NAME;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0380TermInheritance()
    {
        this.archiveBuilder.addTypeDefPatch(deprecateIsATypeOfRelationship());
        this.archiveBuilder.addRelationshipDef(addTermISATYPEOFRelationship());
    }

    /**
     * Deprecate the IsATypeOfRelationship - use TermISATYPEOFRelationship
     *
     * @return patch
     */
    private TypeDefPatch deprecateIsATypeOfRelationship()
    {
        final String typeName = "IsATypeOfRelationship";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }


    /**
     * Defines an inheritance relationship between two spine objects. It provides a type for a Spine Object.
     * @return RelationshipDef
     */
    private RelationshipDef addTermISATYPEOFRelationship()
    {
        final String guid            = "71f83296-2007-46a5-a4c7-919a7c4a12f5";
        final String name            = "TermISATYPEOFRelationship";
        final String description     = "Defines an inheritance relationship between two spine objects.";
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
        final String                     end1EntityType               = "GlossaryTerm";
        final String                     end1AttributeName            = "inherited";
        final String                     end1AttributeDescription     = "Inherited (Subtypes) for this object.";
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
        final String                     end2EntityType               = "GlossaryTerm";
        final String                     end2AttributeName            = "inheritedFrom";
        final String                     end2AttributeDescription     = "Inherited from type (Supertypes) for this object.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);


        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "description";
        final String attribute1Description     = "Description of the relationship.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "status";
        final String attribute2Description     = "The status of or confidence in the relationship.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "steward";
        final String attribute3Description     = "Person responsible for the relationship.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "source";
        final String attribute4Description     = "Person, organization or automated process that created the relationship.";
        final String attribute4DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute("TermRelationshipStatus",
                                                         attribute2Name,
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

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;

    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0461GovernanceEngines()
    {
        this.archiveBuilder.addEntityDef(getContextEventEngineEntity());
        this.archiveBuilder.addEntityDef(getContextEventServiceEntity());
        this.archiveBuilder.addEntityDef(getSurveyActionEngineEntity());
        this.archiveBuilder.addEntityDef(getSurveyActionServiceEntity());
    }

    private EntityDef getContextEventEngineEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = OpenMetadataType.CONTEXT_EVENT_ENGINE.typeGUID;
        final String name            = OpenMetadataType.CONTEXT_EVENT_ENGINE.typeName;
        final String description     = OpenMetadataType.CONTEXT_EVENT_ENGINE.description;
        final String descriptionGUID = OpenMetadataType.CONTEXT_EVENT_ENGINE.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.CONTEXT_EVENT_ENGINE.wikiURL;

        final String superTypeName   = OpenMetadataType.GOVERNANCE_ENGINE.typeName;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID,
                                                 descriptionWiki);

    }

    private EntityDef getContextEventServiceEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = OpenMetadataType.CONTEXT_EVENT_SERVICE.typeGUID;
        final String name            = OpenMetadataType.CONTEXT_EVENT_SERVICE.typeName;
        final String description     = OpenMetadataType.CONTEXT_EVENT_SERVICE.description;
        final String descriptionGUID = OpenMetadataType.CONTEXT_EVENT_SERVICE.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.CONTEXT_EVENT_SERVICE.wikiURL;

        final String superTypeName   = OpenMetadataType.GOVERNANCE_SERVICE.typeName;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID,
                                                 descriptionWiki);

    }

    private EntityDef getSurveyActionEngineEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = OpenMetadataType.SURVEY_ACTION_ENGINE.typeGUID;
        final String name            = OpenMetadataType.SURVEY_ACTION_ENGINE.typeName;
        final String description     = OpenMetadataType.SURVEY_ACTION_ENGINE.description;
        final String descriptionGUID = OpenMetadataType.SURVEY_ACTION_ENGINE.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.SURVEY_ACTION_ENGINE.wikiURL;

        final String superTypeName   = OpenMetadataType.GOVERNANCE_ENGINE.typeName;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID,
                                                 descriptionWiki);
    }

    private EntityDef getSurveyActionServiceEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = OpenMetadataType.SURVEY_ACTION_SERVICE.typeGUID;
        final String name            = OpenMetadataType.SURVEY_ACTION_SERVICE.typeName;
        final String description     = OpenMetadataType.SURVEY_ACTION_SERVICE.description;
        final String descriptionGUID = OpenMetadataType.SURVEY_ACTION_SERVICE.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.SURVEY_ACTION_SERVICE.wikiURL;

        final String superTypeName   = OpenMetadataType.GOVERNANCE_SERVICE.typeName;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID,
                                                 descriptionWiki);
    }




    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0464IntegrationGroups()
    {
        this.archiveBuilder.addTypeDefPatch(updateCatalogTarget());
    }

    /**
     * Add configurationProperties
     *
     * @return patch
     */
    private TypeDefPatch updateCatalogTarget()
    {
        final String typeName = OpenMetadataType.CATALOG_TARGET_RELATIONSHIP_TYPE_NAME;

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "templates";
        final String attribute1Description     = "Map of template name to qualified name of parent element in associated catalog template.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "connectionName";
        final String attribute2Description     = "Name of connector to use to access the associated resource.  If this is null, the connection for the asset associated with the catalog target is used.  The asset may be the catalog target itself or the catalog target's anchor.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "metadataSourceQualifiedName";
        final String attribute3Description     = "Qualified name of a software server capability that is the owner/home of the metadata catalogued by the integration connector.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "configurationProperties";
        final String attribute4Description     = "Specific configuration properties used to configure the behaviour of the connector.";
        final String attribute4DescriptionGUID = null;

        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute1Name,
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
        property = archiveHelper.getMapStringObjectTypeDefAttribute(attribute4Name,
                                                                    attribute4Description,
                                                                    attribute4DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add00475ContextEvents()
    {
        this.archiveBuilder.addEntityDef(addContextEventEntity());
        this.archiveBuilder.addRelationshipDef(addContextEventEvidenceRelationship());
        this.archiveBuilder.addRelationshipDef(addContextEventForTimelineEffectsRelationship());
        this.archiveBuilder.addRelationshipDef(addContextEventImpactRelationship());
        this.archiveBuilder.addRelationshipDef(addDependentContextEventRelationship());
        this.archiveBuilder.addRelationshipDef(addRelatedContextEventRelationship());
        this.archiveBuilder.addEntityDef(addContextEventTimelineEntryEntity());
        this.archiveBuilder.addRelationshipDef(addContextEventTimelineRelationship());
        this.archiveBuilder.addClassificationDef(getContextEventCollectionClassification());
    }

    private EntityDef addContextEventEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = "63d2e056-2f39-40ad-b13b-fe5d8a82d6c6";
        final String name            = "ContextEvent";
        final String description     = "A description of an event that impacts users, data, services, etcetera.  It is used to describe what was happening during a named time period.";
        final String descriptionGUID = null;
        final String superTypeName   = OpenMetadataType.REFERENCEABLE.typeName;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(superTypeName),
                                                                description,
                                                                descriptionGUID);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "name";
        final String attribute1Description     = "Descriptive name for the event.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "eventEffect";
        final String attribute2Description     = "Describes the expected effects of the event on the organization.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "description";
        final String attribute3Description     = "A more detailed description of the event.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "contextEventType";
        final String attribute4Description     = "Describes the type/category of event.  Valid values for this attribute can be managed in a valid metadata value set";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "plannedStartDate";
        final String attribute5Description     = "Provides a planned date/time when the event should start.";
        final String attribute5DescriptionGUID = null;
        final String attribute6Name            = "actualStartDate";
        final String attribute6Description     = "Provides a definitive date/time when the event did start.";
        final String attribute6DescriptionGUID = null;
        final String attribute7Name            = "plannedDuration";
        final String attribute7Description     = "Defines, in milliseconds, the length of time that the event is expected to last.";
        final String attribute7DescriptionGUID = null;
        final String attribute8Name            = "actualDuration";
        final String attribute8Description     = "Defines, in milliseconds, the length of time that the event did last.";
        final String attribute8DescriptionGUID = null;
        final String attribute9Name            = "repeatInterval";
        final String attribute9Description     = "Defines, in milliseconds, how frequently the event is expected to repeat.";
        final String attribute9DescriptionGUID = null;
        final String attribute10Name            = "plannedCompletionDate";
        final String attribute10Description     = "Provides an expected date/time when the event is complete.";
        final String attribute10DescriptionGUID = null;
        final String attribute11Name            = "actualCompletionDate";
        final String attribute11Description     = "Provides a date/time when the event did complete.";
        final String attribute11DescriptionGUID = null;
        final String attribute12Name            = "referenceEffectiveFrom";
        final String attribute12Description     = "Provides a value to use in the starting effective dates for entities, relationships and classifications whose effectivity is triggered by this context event.";
        final String attribute12DescriptionGUID = null;
        final String attribute13Name            = "referenceEffectiveTo";
        final String attribute13Description     = "Provides a value to use in the ending effective dates for entities, relationships and classifications whose effectivity is ended by this context event.";
        final String attribute13DescriptionGUID = null;


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
        property = archiveHelper.getDateTypeDefAttribute(attribute5Name,
                                                         attribute5Description,
                                                         attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute6Name,
                                                         attribute6Description,
                                                         attribute6DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getLongTypeDefAttribute(attribute7Name,
                                                         attribute7Description,
                                                         attribute7DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getLongTypeDefAttribute(attribute8Name,
                                                         attribute8Description,
                                                         attribute8DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getLongTypeDefAttribute(attribute9Name,
                                                         attribute9Description,
                                                         attribute9DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute10Name,
                                                         attribute10Description,
                                                         attribute10DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute11Name,
                                                         attribute11Description,
                                                         attribute11DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute12Name,
                                                         attribute12Description,
                                                         attribute12DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute13Name,
                                                         attribute13Description,
                                                         attribute13DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private RelationshipDef addContextEventEvidenceRelationship()
    {
        final String guid            = "410ab118-a880-4b6a-950d-dada0363c50e";
        final String name            = "ContextEventEvidence";
        final String description     = "Link to evidence that the context event has occurred, started or is expected can appear as alerts/notification or incidents that people have raised.";
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
        final String                     end1EntityType               = "ContextEvent";
        final String                     end1AttributeName            = "relatedToContextEvents";
        final String                     end1AttributeDescription     = "Description of a related event.";
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
        final String                     end2EntityType               = OpenMetadataType.REFERENCEABLE.typeName;
        final String                     end2AttributeName            = "contextEventEvidence";
        final String                     end2AttributeDescription     = "Descriptions of notifications, incidents and other indications that the context event is underway.";
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

    private RelationshipDef addContextEventForTimelineEffectsRelationship()
    {
        final String guid            = "f1f407cc-9047-487d-9ce3-aa892cf39711";
        final String name            = "ContextEventForTimelineEffects";
        final String description     = "Associates a ContextEvent to a Referenceable (typically and Asset or DataProduct) whose data is affected by the event.";
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
        final String                     end1EntityType               = OpenMetadataType.REFERENCEABLE.typeName;
        final String                     end1AttributeName            = "eventEffectedResources";
        final String                     end1AttributeDescription     = "Entities whose data is impacted by the context event.";
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
        final String                     end2EntityType               = "ContextEvent";
        final String                     end2AttributeName            = "associatedEventForTimelines";
        final String                     end2AttributeDescription     = "Descriptions of events that have impacted this resource.";
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

    private RelationshipDef addContextEventImpactRelationship()
    {
        final String guid            = "335f421f-357a-41dd-a365-1c0aa1226ed9";
        final String name            = "ContextEventImpact";
        final String description     = "Links a ContextEvent entity to Referenceable entities that describe resources that are impacted by the event.";
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
        final String                     end1EntityType               = OpenMetadataType.REFERENCEABLE.typeName;
        final String                     end1AttributeName            = "eventImpacts";
        final String                     end1AttributeDescription     = "Resources impacted by the context event.";
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
        final String                     end2EntityType               = "ContextEvent";
        final String                     end2AttributeName            = "impactedByContextEvents";
        final String                     end2AttributeDescription     = "Descriptions of context events affecting this resource and the action taken.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "severityLevelIdentifier";
        final String attribute1Description     = "How severe is the impact on the resource?";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "description";
        final String attribute2Description     = "Description of hte impact";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getIntTypeDefAttribute(attribute1Name,
                                                        attribute1Description,
                                                        attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    private ClassificationDef getContextEventCollectionClassification()
    {
        final String guid            = "a4f037c6-abad-4957-bd3d-1adb3279f274";
        final String name            = "ContextEventCollection";
        final String description     = "Defines the scope of the data held in the associated data resource.";
        final String descriptionGUID = null;

        final String linkedToEntity = "Collection";

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(linkedToEntity),
                                                  false);
    }

    private RelationshipDef addDependentContextEventRelationship()
    {
        final String guid            = "3ad9d182-f0d5-4216-abe3-8dd641d0e37b";
        final String name            = "DependentContextEvent";
        final String description     = "Link between a parent context event and its children.";
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
        final String                     end1EntityType               = "ContextEvent";
        final String                     end1AttributeName            = "parentContextEvents";
        final String                     end1AttributeDescription     = "Context event that is driving the child context events.";
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
        final String                     end2EntityType               = "ContextEvent";
        final String                     end2AttributeName            = "childContextEvents";
        final String                     end2AttributeDescription     = "Subsequent context events created to help manage the parent context events.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "description";
        final String attribute1Description     = "Description of the dependency.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    private RelationshipDef addRelatedContextEventRelationship()
    {
        final String guid            = "a94db527-7e1c-4f45-914e-a49dc009a305";
        final String name            = "RelatedContextEvent";
        final String description     = "Link between context events that are impacting one another in some way.";
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
        final String                     end1EntityType               = "ContextEvent";
        final String                     end1AttributeName            = "relatedContextEvents";
        final String                     end1AttributeDescription     = "Context event that is affecting the other context event.";
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
        final String                     end2EntityType               = "ContextEvent";
        final String                     end2AttributeName            = "relatedContextEvents";
        final String                     end2AttributeDescription     = "Context event that is affecting the other context event.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "statusIdentifier";
        final String attribute1Description     = "Status of this association.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "confidence";
        final String attribute2Description     = "Level of confidence in the association (0=none -> 100=excellent).";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "steward";
        final String attribute3Description     = "Person responsible for maintaining this association.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "stewardTypeName";
        final String attribute4Description     = "Type of element used to identify the steward.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "stewardPropertyName";
        final String attribute5Description     = "Name of property used to identify the steward.";
        final String attribute5DescriptionGUID = null;
        final String attribute6Name            = "source";
        final String attribute6Description     = "Source of the association.";
        final String attribute6DescriptionGUID = null;
        final String attribute7Name            = "notes";
        final String attribute7Description     = "Information relating to the association.";
        final String attribute7DescriptionGUID = null;

        property = archiveHelper.getIntTypeDefAttribute(attribute1Name,
                                                        attribute1Description,
                                                        attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute2Name,
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
        property = archiveHelper.getStringTypeDefAttribute(attribute7Name, attribute7Description,
                                                           attribute7DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    private RelationshipDef addContextEventTimelineRelationship()
    {
        final String guid            = "2b96d76d-bc92-4d1e-8470-5a68db1d6f2f";
        final String name            = "ContextEventTimeline";
        final String description     = "Links a ContextEventTimelineEntry entity to either a ContextEvent entity or a collection of ContextEvent entities.";
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
        final String                     end1EntityType               = OpenMetadataType.REFERENCEABLE.typeName;
        final String                     end1AttributeName            = "associatedContextEvents";
        final String                     end1AttributeDescription     = "Description of the events described in the timeline.";
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
        final String                     end2EntityType               = "ContextEventTimelineEntry";
        final String                     end2AttributeName            = "contextEventTimelineEntry";
        final String                     end2AttributeDescription     = "Descriptions of notifications or significant activity.";
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

    private EntityDef addContextEventTimelineEntryEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = "6b8162ba-08fc-4494-83f5-0b32589c7ba3";
        final String name            = "ContextEventTimelineEntry";
        final String description     = "Describes a notification, significant activity or other occurrence during the context event.";
        final String descriptionGUID = null;
        final String superTypeName   = OpenMetadataType.REFERENCEABLE.typeName;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(superTypeName),
                                                                description,
                                                                descriptionGUID);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "timelineEntryDate";
        final String attribute1Description     = "When does the entry refer to.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "timelineEntryDescription";
        final String attribute2Description     = "Describes the entry in the timeline.";
        final String attribute2DescriptionGUID = null;



        property = archiveHelper.getDateTypeDefAttribute(attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0545ReferenceData()
    {
        this.archiveBuilder.addTypeDefPatch(updateValidValueDefinition());
        this.archiveBuilder.addTypeDefPatch(updateValidValuesImplementation());
        this.archiveBuilder.addRelationshipDef(getConsistentValidValuesRelationship());
        this.archiveBuilder.addRelationshipDef(getValidValueAssociationRelationship());
        this.archiveBuilder.addRelationshipDef(getSpecificationPropertyAssignmentRelationship());
    }

    private TypeDefPatch updateValidValueDefinition()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.VALID_VALUE_DEFINITION.typeName;

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataType.CATEGORY_PROPERTY_NAME;
        final String attribute1Description     = "Descriptive name of the concept that this valid value describes a possible value for.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = OpenMetadataType.IS_CASE_SENSITIVE_PROPERTY_NAME;
        final String attribute2Description     = "Is this valid value case-sensitive, or should the values match irrespective of case?";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = OpenMetadataType.DATA_TYPE_PROPERTY_NAME;
        final String attribute3Description     = "The type of the value identifies it format and content.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getBooleanTypeDefAttribute(attribute2Name,
                                                            attribute2Description,
                                                            attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute3Name,
                                                           attribute3Description,
                                                           attribute3DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    private TypeDefPatch updateValidValuesImplementation()
    {
        final String typeName = OpenMetadataType.VALID_VALUES_IMPL_RELATIONSHIP.typeName;

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        RelationshipEndDef relationshipEndDef;

        /*
         * Update end 2.
         */
        final String                     end1EntityType               = OpenMetadataType.REFERENCEABLE.typeName;
        final String                     end1AttributeName            = "validValueImplementations";
        final String                     end1AttributeDescription     = "The location where mapped value is stored.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        typeDefPatch.setEndDef1(relationshipEndDef);

        return typeDefPatch;
    }

    private RelationshipDef getConsistentValidValuesRelationship()
    {
        final String guid            = OpenMetadataType.CONSISTENT_VALID_VALUES_RELATIONSHIP.typeGUID;
        final String name            = OpenMetadataType.CONSISTENT_VALID_VALUES_RELATIONSHIP.typeName;
        final String description     = OpenMetadataType.CONSISTENT_VALID_VALUES_RELATIONSHIP.description;
        final String descriptionGUID = OpenMetadataType.CONSISTENT_VALID_VALUES_RELATIONSHIP.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.CONSISTENT_VALID_VALUES_RELATIONSHIP.wikiURL;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                descriptionWiki,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = OpenMetadataType.VALID_VALUE_DEFINITION.typeName;
        final String                     end1AttributeName            = "consistentValue";
        final String                     end1AttributeDescription     = "Valid value for another valid value set (property) that is consistent with this value.";
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
        final String                     end2EntityType               = OpenMetadataType.VALID_VALUE_DEFINITION.typeName;
        final String                     end2AttributeName            = "consistentValue";
        final String                     end2AttributeDescription     = "Valid value for another valid value set (property) that is consistent with this value.";
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


    private RelationshipDef getValidValueAssociationRelationship()
    {
        final String guid            = OpenMetadataType.VALID_VALUE_ASSOCIATION_RELATIONSHIP.typeGUID;
        final String name            = OpenMetadataType.VALID_VALUE_ASSOCIATION_RELATIONSHIP.typeName;
        final String description     = OpenMetadataType.VALID_VALUE_ASSOCIATION_RELATIONSHIP.description;
        final String descriptionGUID = OpenMetadataType.VALID_VALUE_ASSOCIATION_RELATIONSHIP.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.VALID_VALUE_ASSOCIATION_RELATIONSHIP.wikiURL;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                descriptionWiki,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = OpenMetadataType.VALID_VALUE_DEFINITION.typeName;
        final String                     end1AttributeName            = "associatedValues1";
        final String                     end1AttributeDescription     = "Valid value at end one of this association.";
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
        final String                     end2EntityType               = OpenMetadataType.VALID_VALUE_DEFINITION.typeName;
        final String                     end2AttributeName            = "associatedValues2";
        final String                     end2AttributeDescription     = "Valid value at end two of this association.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataType.ASSOCIATION_NAME_PROPERTY_NAME;
        final String attribute1Description     = "Descriptive name of the association.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = OpenMetadataType.ASSOCIATION_TYPE_PROPERTY_NAME;
        final String attribute2Description     = "Type of the association, such as 'containment', 'aggregation' or 'inheritance.'";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = OpenMetadataProperty.ADDITIONAL_PROPERTIES.name;
        final String attribute3Description     = "Other properties for this association.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute3Name,
                                                                    attribute3Description,
                                                                    attribute3DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getSpecificationPropertyAssignmentRelationship()
    {
        final String guid            = OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeGUID;
        final String name            = OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName;
        final String description     = OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.description;
        final String descriptionGUID = OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.wikiURL;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                descriptionWiki,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = OpenMetadataType.REFERENCEABLE.typeName;
        final String                     end1AttributeName            = "implementingItem";
        final String                     end1AttributeDescription     = "Connector or template, or similar implementation.";
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
        final String                     end2EntityType               = OpenMetadataType.VALID_VALUE_DEFINITION.typeName;
        final String                     end2AttributeName            = "specificationProperty";
        final String                     end2AttributeDescription     = "Valid value representing a property type that controls its behaviour.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.PROPERTY_TYPE.name;
        final String attribute1Description     = OpenMetadataProperty.PROPERTY_TYPE.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.PROPERTY_TYPE.descriptionGUID;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0462GovernanceActionTypes()
    {
        this.archiveBuilder.addTypeDefPatch(updateGovernanceActionType());
    }

    private TypeDefPatch updateGovernanceActionType()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.GOVERNANCE_ACTION_TYPE_TYPE_NAME;

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataType.SUPPORTED_GUARDS_PROPERTY_NAME;
        final String attribute1Description     = "Deprecated list of produced guards.";
        final String attribute1DescriptionGUID = null;


        property = archiveHelper.getArrayStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(OpenMetadataType.PRODUCED_GUARDS_PROPERTY_NAME);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0603SurveyReports()
    {
        this.archiveBuilder.addEntityDef(addSurveyReportEntity());
        this.archiveBuilder.addRelationshipDef(addAssetSurveyReportRelationship());
        this.archiveBuilder.addRelationshipDef(addEngineActionSurveyReportRelationship());
        this.archiveBuilder.addRelationshipDef(getReportedAnnotationRelationship());
        this.archiveBuilder.addRelationshipDef(getAssociatedAnnotationRelationship());
    }

    private EntityDef addSurveyReportEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = OpenMetadataType.SURVEY_REPORT.typeGUID;
        final String name            = OpenMetadataType.SURVEY_REPORT.typeName;
        final String description     = OpenMetadataType.SURVEY_REPORT.description;
        final String descriptionGUID = OpenMetadataType.SURVEY_REPORT.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.SURVEY_REPORT.wikiURL;

        final String superTypeName   = OpenMetadataType.REFERENCEABLE.typeName;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(superTypeName),
                                                                description,
                                                                descriptionGUID,
                                                                descriptionWiki);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.DISPLAY_NAME.name;
        final String attribute1Description     = OpenMetadataProperty.DISPLAY_NAME.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.DISPLAY_NAME.descriptionGUID;
        final String attribute2Name            = OpenMetadataProperty.DESCRIPTION.name;
        final String attribute2Description     = OpenMetadataProperty.DESCRIPTION.description;
        final String attribute2DescriptionGUID = OpenMetadataProperty.DESCRIPTION.descriptionGUID;
        final String attribute3Name            = OpenMetadataProperty.PURPOSE.name;
        final String attribute3Description     = OpenMetadataProperty.PURPOSE.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.PURPOSE.descriptionGUID;
        final String attribute4Name            = OpenMetadataProperty.ANALYSIS_PARAMETERS.name;
        final String attribute4Description     = OpenMetadataProperty.ANALYSIS_PARAMETERS.description;
        final String attribute4DescriptionGUID = OpenMetadataProperty.ANALYSIS_PARAMETERS.descriptionGUID;
        final String attribute5Name            = OpenMetadataProperty.ANALYSIS_STEP.name;
        final String attribute5Description     = OpenMetadataProperty.ANALYSIS_STEP.description;
        final String attribute5DescriptionGUID = OpenMetadataProperty.ANALYSIS_STEP.descriptionGUID;
        final String attribute6Name            = OpenMetadataProperty.START_DATE.name;
        final String attribute6Description     = OpenMetadataProperty.START_DATE.description;
        final String attribute6DescriptionGUID = OpenMetadataProperty.START_DATE.descriptionGUID;
        final String attribute7Name            = OpenMetadataProperty.COMPLETION_DATE.name;
        final String attribute7Description     = OpenMetadataProperty.COMPLETION_DATE.description;
        final String attribute7DescriptionGUID = OpenMetadataProperty.COMPLETION_DATE.descriptionGUID;
        final String attribute8Name            = OpenMetadataProperty.COMPLETION_MESSAGE.name;
        final String attribute8Description     = OpenMetadataProperty.COMPLETION_MESSAGE.description;
        final String attribute8DescriptionGUID = OpenMetadataProperty.COMPLETION_MESSAGE.descriptionGUID;
        final String attribute9Name            = OpenMetadataProperty.USER.name;
        final String attribute9Description     = OpenMetadataProperty.USER.description;
        final String attribute9DescriptionGUID = OpenMetadataProperty.USER.descriptionGUID;


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
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute4Name,
                                                                    attribute4Description,
                                                                    attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute5Name,
                                                           attribute5Description,
                                                           attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute6Name,
                                                         attribute6Description,
                                                         attribute6DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getDateTypeDefAttribute(attribute7Name,
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

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private RelationshipDef addAssetSurveyReportRelationship()
    {
        final String guid            = OpenMetadataType.ASSET_SURVEY_REPORT_RELATIONSHIP.typeGUID;
        final String name            = OpenMetadataType.ASSET_SURVEY_REPORT_RELATIONSHIP.typeName;
        final String description     = OpenMetadataType.ASSET_SURVEY_REPORT_RELATIONSHIP.description;
        final String descriptionGUID = OpenMetadataType.ASSET_SURVEY_REPORT_RELATIONSHIP.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.ASSET_SURVEY_REPORT_RELATIONSHIP.wikiURL;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                descriptionWiki,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = OpenMetadataType.ASSET.typeName;
        final String                     end1AttributeName            = "surveyReportTarget";
        final String                     end1AttributeDescription     = "The asset describing the resource that was surveyed.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.AT_MOST_ONE;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = OpenMetadataType.SURVEY_REPORT.typeName;
        final String                     end2AttributeName            = "surveyReports";
        final String                     end2AttributeDescription     = "Survey reports for the asset.";
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

    private RelationshipDef addEngineActionSurveyReportRelationship()
    {
        final String guid            = OpenMetadataType.ENGINE_ACTION_SURVEY_REPORT_RELATIONSHIP.typeGUID;
        final String name            = OpenMetadataType.ENGINE_ACTION_SURVEY_REPORT_RELATIONSHIP.typeName;
        final String description     = OpenMetadataType.ENGINE_ACTION_SURVEY_REPORT_RELATIONSHIP.description;
        final String descriptionGUID = OpenMetadataType.ENGINE_ACTION_SURVEY_REPORT_RELATIONSHIP.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.ENGINE_ACTION_SURVEY_REPORT_RELATIONSHIP.wikiURL;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                descriptionWiki,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = OpenMetadataType.ENGINE_ACTION_TYPE_NAME;
        final String                     end1AttributeName            = "engineAction";
        final String                     end1AttributeDescription     = "The engine action controlling the survey action service invocation.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.AT_MOST_ONE;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = OpenMetadataType.SURVEY_REPORT.typeName;
        final String                     end2AttributeName            = "surveyReport";
        final String                     end2AttributeDescription     = "Survey report generated form the execution of the survey action service.  Typically only one unless failures occurred.";
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

    private RelationshipDef getReportedAnnotationRelationship()
    {
        final String guid            = OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeGUID;
        final String name            = OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName;
        final String description     = OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.description;
        final String descriptionGUID = OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.wikiURL;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                descriptionWiki,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = OpenMetadataType.SURVEY_REPORT.typeName;
        final String                     end1AttributeName            = "fromSurveyReport";
        final String                     end1AttributeDescription     = "The report that the annotations belong to.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.AT_MOST_ONE;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);

        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = OpenMetadataType.ANNOTATION.typeName;
        final String                     end2AttributeName            = "reportedAnnotations";
        final String                     end2AttributeDescription     = "The annotations providing the contents for the report.";
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

    private RelationshipDef getAssociatedAnnotationRelationship()
    {
        final String guid            = OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeGUID;
        final String name            = OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName;
        final String description     = OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.description;
        final String descriptionGUID = OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.wikiURL;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                descriptionWiki,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = OpenMetadataType.OPEN_METADATA_ROOT.typeName;
        final String                     end1AttributeName            = "annotationSubject";
        final String                     end1AttributeDescription     = "The element described in the annotation.";
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
        final String                     end2EntityType               = OpenMetadataType.ANNOTATION.typeName;
        final String                     end2AttributeName            = "associatedAnnotations";
        final String                     end2AttributeDescription     = "The annotations describing the element or its real-world counterpart.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.AT_MOST_ONE;

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


    private void update0615SchemaExtraction()
    {
        this.archiveBuilder.addTypeDefPatch(updateDataFieldEntity());
        this.archiveBuilder.addRelationshipDef(getLinkedDataFieldRelationship());
    }

    private TypeDefPatch updateDataFieldEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.DATA_FIELD_TYPE_NAME;

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

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
        final String attribute6Name            = "minCardinality";
        final String attribute6Description     = "Minimum number of occurrences of this attribute allowed.";
        final String attribute6DescriptionGUID = null;
        final String attribute7Name            = "maxCardinality";
        final String attribute7Description     = "Maximum number of occurrences of this attribute allowed.";
        final String attribute7DescriptionGUID = null;
        final String attribute8Name            = "precision";
        final String attribute8Description     = "Number of digits after the decimal point.";
        final String attribute8DescriptionGUID = null;
        final String attribute9Name            = "isDeprecated";
        final String attribute9Description     = "Is this field deprecated?";
        final String attribute9DescriptionGUID = null;
        final String attribute10Name            = "version";
        final String attribute10Description     = "Incrementing version number.";
        final String attribute10DescriptionGUID = null;
        final String attribute11Name            = OpenMetadataProperty.VERSION_IDENTIFIER.name;
        final String attribute11Description     = OpenMetadataProperty.VERSION_IDENTIFIER.description;
        final String attribute11DescriptionGUID = OpenMetadataProperty.VERSION_IDENTIFIER.descriptionGUID;

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

        property = archiveHelper.getIntTypeDefAttribute(attribute6Name,
                                                        attribute6Description,
                                                        attribute6DescriptionGUID);
        properties.add(property);

        property = archiveHelper.getIntTypeDefAttribute(attribute7Name,
                                                        attribute7Description,
                                                        attribute7DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute8Name,
                                                        attribute8Description,
                                                        attribute8DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getBooleanTypeDefAttribute(attribute9Name,
                                                            attribute9Description,
                                                            attribute9DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getLongTypeDefAttribute(attribute10Name,
                                                         attribute10Description,
                                                         attribute10DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute11Name,
                                                           attribute11Description,
                                                           attribute11DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    private RelationshipDef getLinkedDataFieldRelationship()
    {
        final String guid            = "cca4b116-4490-44c4-84e1-535231ae46a1";
        final String name            = "LinkedDataField";
        final String description     = "Represents an association between two data fields in a schema.  This may describe a full relationship in the schema (for example, in a relational schema) or a relationship end (for example, in a graph schema).";
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
        final String                     end1EntityType               = "DataField";
        final String                     end1AttributeName            = "linkFromDataFields";
        final String                     end1AttributeDescription     = "Data field that is linked from.";
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
        final String                     end2EntityType               = "DataField";
        final String                     end2AttributeName            = "linkToDataFields";
        final String                     end2AttributeDescription     = "Data field that is linked to.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "relationshipTypeName";
        final String attribute1Description     = "Unique name of the relationship type to use in the associated schema.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "relationshipEnd";
        final String attribute2Description     = "If the end of a relationship is significant set to 1 or 2 to indicated the end; otherwise use 0.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "name";
        final String attribute3Description     = "Display name for the relationship (or relationship end).";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "description";
        final String attribute4Description     = "Description of the relationship (or relationship end).";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "minCardinality";
        final String attribute5Description     = "Minimum number of occurrences of this relationship (or relationship end) allowed.";
        final String attribute5DescriptionGUID = null;
        final String attribute6Name            = "maxCardinality";
        final String attribute6Description     = "Maximum number of occurrences of this relationship (or relationship end) allowed.";
        final String attribute6DescriptionGUID = null;
        final String attribute7Name            = OpenMetadataProperty.ADDITIONAL_PROPERTIES.name;
        final String attribute7Description     = "Any additional properties for the relationship (or relationship end).";
        final String attribute7DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute2Name,
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
        property = archiveHelper.getIntTypeDefAttribute(attribute5Name,
                                                        attribute5Description,
                                                        attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute6Name,
                                                        attribute6Description,
                                                        attribute6DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute7Name,
                                                                    attribute7Description,
                                                                    attribute7DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0690RequestForAction()
    {
        this.archiveBuilder.addRelationshipDef(getRequestForActionTargetRelationship());
    }

    private RelationshipDef getRequestForActionTargetRelationship()
    {
        final String guid            = OpenMetadataType.REQUEST_FOR_ACTION_TARGET.typeGUID;
        final String name            = OpenMetadataType.REQUEST_FOR_ACTION_TARGET.typeName;
        final String description     = OpenMetadataType.REQUEST_FOR_ACTION_TARGET.description;
        final String descriptionGUID = OpenMetadataType.REQUEST_FOR_ACTION_TARGET.descriptionGUID;
        final String wikiURL         = OpenMetadataType.REQUEST_FOR_ACTION_TARGET.wikiURL;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                wikiURL,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = OpenMetadataType.REQUEST_FOR_ACTION_ANNOTATION.typeName;
        final String                     end1AttributeName            = "identifiedInRequestForActions";
        final String                     end1AttributeDescription     = "Request for action that originated the action.";
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
        final String                     end2EntityType               = OpenMetadataType.REFERENCEABLE.typeName;
        final String                     end2AttributeName            = "requestForActionTargets";
        final String                     end2AttributeDescription     = "Elements that originated the data.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.ACTION_TARGET_NAME.name;
        final String attribute1Description     = "Unique name of the action target.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0755UltimateSourcesDestinations()
    {
        this.archiveBuilder.addRelationshipDef(getUltimateSourceRelationship());
        this.archiveBuilder.addRelationshipDef(getUltimateDestinationRelationship());
    }

    private RelationshipDef getUltimateSourceRelationship()
    {
        final String guid            = "e5649e7a-4d97-4a41-a91d-20f521f961aa";
        final String name            = "UltimateSource";
        final String description     = "Links a node in the lineage graph to its ultimate source - ie the node at the start of the lineage data flow.";
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
        final String                     end1EntityType               = OpenMetadataType.REFERENCEABLE.typeName;
        final String                     end1AttributeName            = "downstreamFromLineageSource";
        final String                     end1AttributeDescription     = "Elements that receives data from the ultimate source (via other processing).";
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
        final String                     end2EntityType               = OpenMetadataType.REFERENCEABLE.typeName;
        final String                     end2AttributeName            = "ultimateSources";
        final String                     end2AttributeDescription     = "Elements that originated the data.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.QUALIFIED_NAME.name;
        final String attribute1Description     = "Unique name of the associated information supply chain.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "distance";
        final String attribute2Description     = "The number of hops along the lineage graph to the ultimate source organized by type of element.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringIntTypeDefAttribute(attribute2Name,
                                                                 attribute2Description,
                                                                 attribute2DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getUltimateDestinationRelationship()
    {
        final String guid            = "27d48f4a-a5bd-4320-a4ba-55f03adbb27b";
        final String name            = "UltimateDestination";
        final String description     = "Links a node in the lineage graph to its ultimate destination - ie the node at the end of the lineage data flow.";
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
        final String                     end1EntityType               = OpenMetadataType.REFERENCEABLE.typeName;
        final String                     end1AttributeName            = "upstreamFromLineageDestination";
        final String                     end1AttributeDescription     = "Elements that sends data from the ultimate destination (via other processing).";
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
        final String                     end2EntityType               = OpenMetadataType.REFERENCEABLE.typeName;
        final String                     end2AttributeName            = "ultimateDestinations";
        final String                     end2AttributeDescription     = "Elements that ultimately receive the data.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.QUALIFIED_NAME.name;
        final String attribute1Description     = "Unique name of the associated information supply chain.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "distance";
        final String attribute2Description     = "The number of hops along the lineage graph to the ultimate destination organized by type of element.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringIntTypeDefAttribute(attribute2Name,
                                                                 attribute2Description,
                                                                 attribute2DescriptionGUID);
        properties.add(property);

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }
}

