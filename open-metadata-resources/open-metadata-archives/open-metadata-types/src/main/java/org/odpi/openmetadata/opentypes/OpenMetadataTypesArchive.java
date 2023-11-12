/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationPropagationRule;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipEndCardinality;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipEndDef;
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
        update0021Collections();
        update0137Actions();
        update0130Projects();
        update0210DataStores();
        update0461GovernanceEngines();
        add00475ContextEvents();
        add00755UltimateSourcesDestinations();
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0021Collections()
    {
        this.archiveBuilder.addTypeDefPatch(updateCollection());
        this.archiveBuilder.addClassificationDef(getRootCollectionClassification());
    }

    private TypeDefPatch updateCollection()
    {
        /*
         * Create the Patch
         */
        final String typeName = "Collection";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "collectionType";
        final String attribute1Description     = "Descriptive name of the concept that this collection represents.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    private ClassificationDef getRootCollectionClassification()
    {
        final String guid            = "9fdb6d71-fd69-4c40-81f3-5eab1c44d1f4";
        final String name            = "RootCollection";
        final String description     = "This collection is the root collection in a collection hierarchy.";
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


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0137Actions()
    {
        this.archiveBuilder.addEntityDef(addActionEntity());
        this.archiveBuilder.addTypeDefPatch(updateToDoEntity());
        this.archiveBuilder.addTypeDefPatch(updateGovernanceActionEntity());
        this.archiveBuilder.addTypeDefPatch(updateActionsRelationship());
    }



    private EntityDef addActionEntity()
    {
        final String guid = "95261f26-8fe0-4723-b953-4ae5789ec639";

        final String name            = "Action";
        final String description     = "An action that has been identified to support the development, improvement, or remedy of an object or situation.";
        final String descriptionGUID = null;

        final String superTypeName = "Referenceable";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private TypeDefPatch updateToDoEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "ToDo";
        final String superTypeName = "Action";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(superTypeName));

        return typeDefPatch;
    }


    private TypeDefPatch updateGovernanceActionEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "GovernanceAction";
        final String superTypeName = "Action";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(superTypeName));

        return typeDefPatch;
    }


    private TypeDefPatch updateActionsRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "Actions";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "Referenceable";
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
        final String                     end2EntityType               = "Action";
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

    private void update0130Projects()
    {
        this.archiveBuilder.addClassificationDef(getPersonalProjectClassification());
    }


    private ClassificationDef getPersonalProjectClassification()
    {
        final String guid            = "3d7b8500-cebd-4f18-b85c-a459bec3e3ef";
        final String name            = "PersonalProject";
        final String description     = "This is an informal project that has been created by an individual to help them organize their work.";
        final String descriptionGUID = null;

        final String linkedToEntity = "Project";

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

    private void update0210DataStores()
    {
        this.archiveBuilder.addClassificationDef(getDataScopeClassification());
    }


    private ClassificationDef getDataScopeClassification()
    {
        final String guid            = "22f996d0-c4b7-433a-af0b-6a3e9478e488";
        final String name            = "DataScope";
        final String description     = "Defines the scope of the data held in the associated data resource.";
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

        final String attribute1Name            = "minimumLongitude";
        final String attribute1Description     = "If the data is bound by an area, this is the longitude for bottom-left corner of the bounding box (BBOX) for the area covered by the data.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "minimumLatitude";
        final String attribute2Description     = "If the data is bound by an area, this is the latitude for the bottom-left corner of the bounding box (BBOX) for the area covered by the data.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "maxLongitude";
        final String attribute3Description     = "If the data is bound by an area, this is the longitude for top-right corner of the bounding box (BBOX) for the area covered by the data..";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "maxLatitude";
        final String attribute4Description     = "If the data is bound by an area, this is the latitude for top-right corner of the bounding box (BBOX) for the area covered by the data.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "minHeight";
        final String attribute5Description     = "If the height above ground is relevant, this is the lowest height that the data covers.";
        final String attribute5DescriptionGUID = null;
        final String attribute6Name            = "maxHeight";
        final String attribute6Description     = "If the height above ground is relevant, this is the highest height that the data covers.";
        final String attribute6DescriptionGUID = null;
        final String attribute7Name            = "startTime";
        final String attribute7Description     = "If the data is bound by time, this is the start time.";
        final String attribute7DescriptionGUID = null;
        final String attribute8Name            = "endTime";
        final String attribute8Description     = "If the data is bound by time, this is the end time.";
        final String attribute8DescriptionGUID = null;

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

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0461GovernanceEngines()
    {
        this.archiveBuilder.addEntityDef(getEventActionEngineEntity());
        this.archiveBuilder.addEntityDef(getEventActionServiceEntity());
    }

    private EntityDef getEventActionEngineEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = "796f6493-3c3e-4091-8b21-46ea4e54d011";
        final String name            = "EventActionEngine";
        final String description     = "A governance engine for managing context events and associated actions.";
        final String descriptionGUID = null;
        final String superTypeName   = "GovernanceEngine";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);

    }


    private EntityDef getEventActionServiceEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = "464bb4d8-f865-4b9d-a06e-7ed19518ff13";
        final String name            = "EventActionService";
        final String description     = "A governance service for managing context events and associated actions.";
        final String descriptionGUID = null;
        final String superTypeName   = "GovernanceService";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);

    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add00475ContextEvents()
    {
        this.archiveBuilder.addEntityDef(addContextEventEntity());
        this.archiveBuilder.addRelationshipDef(addContextEventEvidenceRelationship());
        this.archiveBuilder.addRelationshipDef(addContextEventForTimelineRelationship());
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
        final String superTypeName   = "Referenceable";

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
        final String                     end2EntityType               = "Referenceable";
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

    private RelationshipDef addContextEventForTimelineRelationship()
    {
        final String guid            = "f1f407cc-9047-487d-9ce3-aa892cf39711";
        final String name            = "ContextEventForTimeline";
        final String description     = "Link between a ContextEventTimelineEntry entity and either a ContextEvent entity or a collection of ContextEvent entities.";
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
        final String                     end1EntityType               = "Referenceable";
        final String                     end1AttributeName            = "eventEffectedResources";
        final String                     end1AttributeDescription     = "Entities whose data/performance is impacted by the context event.";
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
        final String                     end1EntityType               = "Referenceable";
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
        final String                     end2AttributeName            = "effectedByContextEvents";
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
        final String                     end1EntityType               = "Referenceable";
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
        final String superTypeName   = "Referenceable";

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

    private void add00755UltimateSourcesDestinations()
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
        final String                     end1EntityType               = "Referenceable";
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
        final String                     end2EntityType               = "Referenceable";
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

        final String attribute1Name            = "qualifiedName";
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
        final String                     end1EntityType               = "Referenceable";
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
        final String                     end2EntityType               = "Referenceable";
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

        final String attribute1Name            = "qualifiedName";
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

