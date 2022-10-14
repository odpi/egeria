/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.events;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;

/**
 * OMRSInstanceEventProcessor is an interface implemented by a component that is able to process incoming
 * metadata instance events for an Open Metadata Repository.  Instance events are used to replicate metadata
 * instances across an Open Metadata Repository Cohort.
 */
public abstract class OMRSInstanceEventProcessor implements OMRSInstanceEventProcessorInterface,
                                                            OMRSInstanceEventProcessorClassificationExtension
{
    private String  eventProcessorName;

    /**
     * Return the name of the event processor.
     *
     * @return event processor name
     */
    public String getEventProcessorName()
    {
        return eventProcessorName;
    }


    /**
     * Constructor to initialize the name of the processor.
     *
     * @param eventProcessorName string name
     */
    protected OMRSInstanceEventProcessor(String eventProcessorName)
    {
        this.eventProcessorName = eventProcessorName;
    }


    /**
     * Process the instance event directly.
     *
     * @param sourceName source of the event
     * @param instanceEvent  properties of the event to send
     * */
    public abstract void sendInstanceEvent(String            sourceName,
                                           OMRSInstanceEvent instanceEvent);


    /*
     * ================================================
     * Processor methods requesting specific events.
     */

    /**
     * A new entity has been created.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  details of the new entity
     */
    public abstract void processNewEntityEvent(String       sourceName,
                                               String       originatorMetadataCollectionId,
                                               String       originatorServerName,
                                               String       originatorServerType,
                                               String       originatorOrganizationName,
                                               EntityDetail entity);


    /**
     * An existing entity has been updated.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param oldEntity  original entity before update.
     * @param newEntity  details of the new version of the entity.
     */
    public abstract void processUpdatedEntityEvent(String       sourceName,
                                                   String       originatorMetadataCollectionId,
                                                   String       originatorServerName,
                                                   String       originatorServerType,
                                                   String       originatorOrganizationName,
                                                   EntityDetail oldEntity,
                                                   EntityDetail newEntity);


    /**
     * An update to an entity has been undone.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  details of the version of the entity that has been restored.
     */
    public abstract void processUndoneEntityEvent(String       sourceName,
                                                  String       originatorMetadataCollectionId,
                                                  String       originatorServerName,
                                                  String       originatorServerType,
                                                  String       originatorOrganizationName,
                                                  EntityDetail entity);


    /**
     * A new classification has been added to an entity.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  details of the entity with the new classification added. No guarantee this is all the classifications.
     */
    @Deprecated
    @SuppressWarnings(value = "unused")
    public  void processClassifiedEntityEvent(String         sourceName,
                                              String         originatorMetadataCollectionId,
                                              String         originatorServerName,
                                              String         originatorServerType,
                                              String         originatorOrganizationName,
                                              EntityDetail   entity)
    {
    }


    /**
     * A new classification has been added to an entity.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  details of the entity with the new classification added. No guarantee this is all the classifications.
     * @param classification new classification
     */
    @SuppressWarnings(value = {"unused", "deprecation"})
    public  void processClassifiedEntityEvent(String         sourceName,
                                              String         originatorMetadataCollectionId,
                                              String         originatorServerName,
                                              String         originatorServerType,
                                              String         originatorOrganizationName,
                                              EntityDetail   entity,
                                              Classification classification)
    {
        processClassifiedEntityEvent(sourceName,
                                     originatorMetadataCollectionId,
                                     originatorServerName,
                                     originatorServerType,
                                     originatorOrganizationName,
                                     entity);
    }


    /**
     * A new classification has been added to an entity.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  proxy of the entity with the new classification added. No guarantee this is all the classifications.
     * @param classification new classification
     */
    @SuppressWarnings(value = "unused")
    public void processClassifiedEntityEvent(String         sourceName,
                                             String         originatorMetadataCollectionId,
                                             String         originatorServerName,
                                             String         originatorServerType,
                                             String         originatorOrganizationName,
                                             EntityProxy    entity,
                                             Classification classification)
    {
    }


    /**
     * A classification has been removed from an entity.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  details of the entity after the classification has been removed. No guarantee this is all the classifications.
     */
    @Deprecated
    @SuppressWarnings(value = "unused")
    public   void processDeclassifiedEntityEvent(String         sourceName,
                                                 String         originatorMetadataCollectionId,
                                                 String         originatorServerName,
                                                 String         originatorServerType,
                                                 String         originatorOrganizationName,
                                                 EntityDetail   entity)
    {
    }


    /**
     * A classification has been removed from an entity.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  details of the entity after the classification has been removed. No guarantee this is all the classifications.
     * @param originalClassification classification that was removed
     */
    @SuppressWarnings(value = {"unused", "deprecation"})
    public  void processDeclassifiedEntityEvent(String         sourceName,
                                                String         originatorMetadataCollectionId,
                                                String         originatorServerName,
                                                String         originatorServerType,
                                                String         originatorOrganizationName,
                                                EntityDetail   entity,
                                                Classification originalClassification)
    {
        processDeclassifiedEntityEvent(sourceName,
                                       originatorMetadataCollectionId,
                                       originatorServerName,
                                       originatorServerType,
                                       originatorOrganizationName,
                                       entity);
    }


    /**
     * A classification has been removed from an entity.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  proxy of the entity after the classification has been removed. No guarantee this is all the classifications.
     * @param originalClassification classification that was removed
     */
    @SuppressWarnings(value = "unused")
    public void processDeclassifiedEntityEvent(String         sourceName,
                                               String         originatorMetadataCollectionId,
                                               String         originatorServerName,
                                               String         originatorServerType,
                                               String         originatorOrganizationName,
                                               EntityProxy    entity,
                                               Classification originalClassification)
    {
    }


    /**
     * An existing classification has been changed on an entity.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  details of the entity after the classification has been changed. No guarantee this is all the classifications.
     */
    @Deprecated
    @SuppressWarnings(value = "unused")
    public  void processReclassifiedEntityEvent(String         sourceName,
                                                String         originatorMetadataCollectionId,
                                                String         originatorServerName,
                                                String         originatorServerType,
                                                String         originatorOrganizationName,
                                                EntityDetail   entity)
    {
    }



    /**
     * An existing classification has been changed on an entity.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  details of the entity after the classification has been changed. No guarantee this is all the classifications.
     * @param originalClassification classification that was removed
     * @param classification new classification
     */
    @SuppressWarnings(value = {"unused", "deprecation"})
    public  void processReclassifiedEntityEvent(String         sourceName,
                                                String         originatorMetadataCollectionId,
                                                String         originatorServerName,
                                                String         originatorServerType,
                                                String         originatorOrganizationName,
                                                EntityDetail   entity,
                                                Classification originalClassification,
                                                Classification classification)
    {
        processReclassifiedEntityEvent(sourceName,
                                       originatorMetadataCollectionId,
                                       originatorServerName,
                                       originatorServerType,
                                       originatorOrganizationName,
                                       entity);
    }


    /**
     * An existing classification has been changed on an entity.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  proxy of the entity after the classification has been changed. No guarantee this is all the classifications.
     * @param originalClassification classification that was removed
     * @param classification new classification
     */
    @SuppressWarnings(value = "unused")
    public void processReclassifiedEntityEvent(String         sourceName,
                                               String         originatorMetadataCollectionId,
                                               String         originatorServerName,
                                               String         originatorServerType,
                                               String         originatorOrganizationName,
                                               EntityProxy    entity,
                                               Classification originalClassification,
                                               Classification classification)
    {
    }


    /**
     * An existing entity has been deleted.  This is a soft delete. This means it is still in the repository
     * but it is no longer returned on queries.
     *
     * All relationships to the entity are also soft-deleted and will no longer be usable.  These deleted relationships
     * will be notified through separate events.
     *
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  deleted entity
     */
    public abstract void processDeletedEntityEvent(String       sourceName,
                                                   String       originatorMetadataCollectionId,
                                                   String       originatorServerName,
                                                   String       originatorServerType,
                                                   String       originatorOrganizationName,
                                                   EntityDetail entity);


    /**
     * A deleted entity has been restored to the state it was before it was deleted.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  details of the version of the entity that has been restored.
     */
    public abstract void processRestoredEntityEvent(String       sourceName,
                                                    String       originatorMetadataCollectionId,
                                                    String       originatorServerName,
                                                    String       originatorServerType,
                                                    String       originatorOrganizationName,
                                                    EntityDetail entity);


    /**
     * An entity has been permanently removed from the repository.  This request can not be undone.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  details of the version of the entity that has been purged.
     */
    public  void processPurgedEntityEvent(String       sourceName,
                                          String       originatorMetadataCollectionId,
                                          String       originatorServerName,
                                          String       originatorServerType,
                                          String       originatorOrganizationName,
                                          EntityDetail entity)
    {
        /*
         * This is a new method - if this method is not overridden in the implementing repository connector,
         * it delegates to the original version of purgeRelationshipReferenceCopy.
         */
        if (entity != null)
        {
            String  typeDefGUID = null;
            String  typeDefName = null;

            InstanceType type = entity.getType();

            if (type != null)
            {
                typeDefGUID = type.getTypeDefGUID();
                typeDefName = type.getTypeDefName();
            }

            this.processPurgedEntityEvent(sourceName,
                                          originatorMetadataCollectionId,
                                          originatorServerName,
                                          originatorServerType,
                                          originatorOrganizationName,
                                          typeDefGUID,
                                          typeDefName,
                                          entity.getGUID());
        }
    }


    /**
     * A deleted entity has been permanently removed from the repository.  This request can not be undone.
     *
     * Details of the TypeDef are included with the entity's unique id (guid) to ensure the right entity is purged in
     * the remote repositories.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param typeDefGUID  unique identifier for this entity's TypeDef
     * @param typeDefName  name of this entity's TypeDef
     * @param instanceGUID  unique identifier for the entity
     */
    public abstract void processPurgedEntityEvent(String       sourceName,
                                                  String       originatorMetadataCollectionId,
                                                  String       originatorServerName,
                                                  String       originatorServerType,
                                                  String       originatorOrganizationName,
                                                  String       typeDefGUID,
                                                  String       typeDefName,
                                                  String       instanceGUID);


    /**
     * An existing entity has been deleted and purged in a single action.
     *
     * All relationships to the entity are also deleted and purged and will no longer be usable.  These deleted relationships
     * will be notified through separate events.
     *
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  deleted entity
     */
    public void processDeletePurgedEntityEvent(String       sourceName,
                                               String       originatorMetadataCollectionId,
                                               String       originatorServerName,
                                               String       originatorServerType,
                                               String       originatorOrganizationName,
                                               EntityDetail entity)
    {
        this.processDeletedEntityEvent(sourceName,
                                       originatorMetadataCollectionId,
                                       originatorServerName,
                                       originatorServerType,
                                       originatorOrganizationName,
                                       entity);

        if (entity != null)
        {
            InstanceType type = entity.getType();

            if (type != null)
            {
                this.processPurgedEntityEvent(sourceName,
                                              originatorMetadataCollectionId,
                                              originatorServerName,
                                              originatorServerType,
                                              originatorOrganizationName,
                                              entity);
            }
        }
    }


    /**
     * An existing entity has had its type changed.  Typically this action is taken to move an entity's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param originalTypeDef  description of this entity's original TypeDef.
     * @param entity  new values for this entity, including the new type information.
     */
    public abstract void processReTypedEntityEvent(String         sourceName,
                                                   String         originatorMetadataCollectionId,
                                                   String         originatorServerName,
                                                   String         originatorServerType,
                                                   String         originatorOrganizationName,
                                                   TypeDefSummary originalTypeDef,
                                                   EntityDetail   entity);


    /**
     * An existing entity has changed home repository.  This action is taken for example, if a repository
     * becomes permanently unavailable, or if the user community updating this entity move to working
     * from a different repository in the open metadata repository cluster.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param originalHomeMetadataCollectionId  unique identifier for the original home metadata collection/repository.
     * @param entity  new values for this entity, including the new home information.
     */
    public abstract void processReHomedEntityEvent(String       sourceName,
                                                   String       originatorMetadataCollectionId,
                                                   String       originatorServerName,
                                                   String       originatorServerType,
                                                   String       originatorOrganizationName,
                                                   String       originalHomeMetadataCollectionId,
                                                   EntityDetail entity);

    /**
     * The guid of an existing entity has been changed to a new value.  This is used if two different
     * entities are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param originalEntityGUID  the existing identifier for the entity.
     * @param entity  new values for this entity, including the new guid.
     */
    public abstract void processReIdentifiedEntityEvent(String       sourceName,
                                                        String       originatorMetadataCollectionId,
                                                        String       originatorServerName,
                                                        String       originatorServerType,
                                                        String       originatorOrganizationName,
                                                        String       originalEntityGUID,
                                                        EntityDetail entity);


    /**
     * The local repository is requesting that an entity from another repository's metadata collection is
     * refreshed so the local repository can create a reference copy.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param typeDefGUID  unique identifier for this entity's TypeDef
     * @param typeDefName  name of this entity's TypeDef
     * @param instanceGUID  unique identifier for the entity
     * @param homeMetadataCollectionId  metadata collection id for the home of this instance.
     */
    public abstract void processRefreshEntityRequested(String       sourceName,
                                                       String       originatorMetadataCollectionId,
                                                       String       originatorServerName,
                                                       String       originatorServerType,
                                                       String       originatorOrganizationName,
                                                       String       typeDefGUID,
                                                       String       typeDefName,
                                                       String       instanceGUID,
                                                       String       homeMetadataCollectionId);


    /**
     * A remote repository in the cohort has sent entity details in response to a refresh request.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  details of the requested entity
     */
    public abstract void processRefreshEntityEvent(String       sourceName,
                                                   String       originatorMetadataCollectionId,
                                                   String       originatorServerName,
                                                   String       originatorServerType,
                                                   String       originatorOrganizationName,
                                                   EntityDetail entity);


    /**
     * A new relationship has been created.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param relationship  details of the new relationship
     */
    public abstract void processNewRelationshipEvent(String       sourceName,
                                                     String       originatorMetadataCollectionId,
                                                     String       originatorServerName,
                                                     String       originatorServerType,
                                                     String       originatorOrganizationName,
                                                     Relationship relationship);


    /**
     * An existing relationship has been updated.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param oldRelationship  original details of the relationship.
     * @param newRelationship  details of the new version of the relationship.
     */
    public abstract void processUpdatedRelationshipEvent(String       sourceName,
                                                         String       originatorMetadataCollectionId,
                                                         String       originatorServerName,
                                                         String       originatorServerType,
                                                         String       originatorOrganizationName,
                                                         Relationship oldRelationship,
                                                         Relationship newRelationship);


    /**
     * An update to a relationship has been undone.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param relationship details of the version of the relationship that has been restored.
     */
    public abstract void processUndoneRelationshipEvent(String       sourceName,
                                                        String       originatorMetadataCollectionId,
                                                        String       originatorServerName,
                                                        String       originatorServerType,
                                                        String       originatorOrganizationName,
                                                        Relationship relationship);


    /**
     * An active relationship has been deleted.  This is a soft delete. This means it is still in the repository
     * but it is no longer returned on queries.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param relationship  deleted relationship
     */
    public abstract void processDeletedRelationshipEvent(String       sourceName,
                                                         String       originatorMetadataCollectionId,
                                                         String       originatorServerName,
                                                         String       originatorServerType,
                                                         String       originatorOrganizationName,
                                                         Relationship relationship);


    /**
     * A deleted relationship has been restored to the state it was before it was deleted.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param relationship  details of the version of the relationship that has been restored.
     */
    public abstract void processRestoredRelationshipEvent(String       sourceName,
                                                          String       originatorMetadataCollectionId,
                                                          String       originatorServerName,
                                                          String       originatorServerType,
                                                          String       originatorOrganizationName,
                                                          Relationship relationship);



    /**
     * A relationship has been permanently removed from the repository.  This request can not be undone.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param relationship  details of the  relationship that has been purged.
     */
    public void processPurgedRelationshipEvent(String       sourceName,
                                               String       originatorMetadataCollectionId,
                                               String       originatorServerName,
                                               String       originatorServerType,
                                               String       originatorOrganizationName,
                                               Relationship relationship)
    {
        /*
         * This is a new method - if this method is not overridden in the implementing repository connector,
         * it delegates to the original version of purgeRelationshipReferenceCopy.
         */
        if (relationship != null)
        {
            String  typeDefGUID = null;
            String  typeDefName = null;

            InstanceType type = relationship.getType();

            if (type != null)
            {
                typeDefGUID = type.getTypeDefGUID();
                typeDefName = type.getTypeDefName();
            }

            this.processPurgedEntityEvent(sourceName,
                                          originatorMetadataCollectionId,
                                          originatorServerName,
                                          originatorServerType,
                                          originatorOrganizationName,
                                          typeDefGUID,
                                          typeDefName,
                                          relationship.getGUID());
        }
    }


    /**
     * A deleted relationship has been permanently removed from the repository.  This request can not be undone.
     *
     * Details of the TypeDef are included with the relationship's unique id (guid) to ensure the right
     * relationship is purged in the remote repositories.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param typeDefGUID  unique identifier for this relationship's TypeDef.
     * @param typeDefName name of this relationship's TypeDef.
     * @param instanceGUID  unique identifier for the relationship.
     */
    public abstract void processPurgedRelationshipEvent(String sourceName,
                                                        String originatorMetadataCollectionId,
                                                        String originatorServerName,
                                                        String originatorServerType,
                                                        String originatorOrganizationName,
                                                        String typeDefGUID,
                                                        String typeDefName,
                                                        String instanceGUID);


    /**
     * An active relationship has been deleted and purged from the repository.  This request can not be undone.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param relationship  deleted relationship
     */
    public void processDeletePurgedRelationshipEvent(String       sourceName,
                                                     String       originatorMetadataCollectionId,
                                                     String       originatorServerName,
                                                     String       originatorServerType,
                                                     String       originatorOrganizationName,
                                                     Relationship relationship)
    {
        this.processDeletedRelationshipEvent(sourceName,
                                             originatorMetadataCollectionId,
                                             originatorServerName,
                                             originatorServerType,
                                             originatorOrganizationName,
                                             relationship);

        if (relationship != null)
        {
            InstanceType type = relationship.getType();

            if (type != null)
            {
                this.processPurgedRelationshipEvent(sourceName,
                                                    originatorMetadataCollectionId,
                                                    originatorServerName,
                                                    originatorServerType,
                                                    originatorOrganizationName,
                                                    relationship);
            }
        }
    }



    /**
     * An existing relationship has had its type changed.  Typically this action is taken to move a relationship's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param originalTypeDef  description of this relationship's original TypeDef.
     * @param relationship  new values for this relationship, including the new type information.
     */
    public abstract void processReTypedRelationshipEvent(String         sourceName,
                                                         String         originatorMetadataCollectionId,
                                                         String         originatorServerName,
                                                         String         originatorServerType,
                                                         String         originatorOrganizationName,
                                                         TypeDefSummary originalTypeDef,
                                                         Relationship   relationship);


    /**
     * An existing relationship has changed home repository.  This action is taken for example, if a repository
     * becomes permanently unavailable, or if the user community updating this relationship move to working
     * from a different repository in the open metadata repository cluster.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param originalHomeMetadataCollectionId  unique identifier for the original home repository.
     * @param relationship  new values for this relationship, including the new home information.
     */
    public abstract void processReHomedRelationshipEvent(String       sourceName,
                                                         String       originatorMetadataCollectionId,
                                                         String       originatorServerName,
                                                         String       originatorServerType,
                                                         String       originatorOrganizationName,
                                                         String       originalHomeMetadataCollectionId,
                                                         Relationship relationship);


    /**
     * The guid of an existing relationship has changed.  This is used if two different
     * relationships are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param originalRelationshipGUID  the existing identifier for the relationship.
     * @param relationship  new values for this relationship, including the new guid.
     */
    public abstract void processReIdentifiedRelationshipEvent(String       sourceName,
                                                              String       originatorMetadataCollectionId,
                                                              String       originatorServerName,
                                                              String       originatorServerType,
                                                              String       originatorOrganizationName,
                                                              String       originalRelationshipGUID,
                                                              Relationship relationship);



    /**
     * An open metadata repository has requested the home repository of a relationship send details of the relationship so
     * its local metadata collection can create a reference copy of the instance.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param typeDefGUID unique identifier for this instance's TypeDef
     * @param typeDefName name of this relationship's TypeDef
     * @param instanceGUID unique identifier for the instance
     * @param homeMetadataCollectionId metadata collection id for the home of this instance.
     */
    public abstract void processRefreshRelationshipRequest(String       sourceName,
                                                           String       originatorMetadataCollectionId,
                                                           String       originatorServerName,
                                                           String       originatorServerType,
                                                           String       originatorOrganizationName,
                                                           String       typeDefGUID,
                                                           String       typeDefName,
                                                           String       instanceGUID,
                                                           String       homeMetadataCollectionId);


    /**
     * An open metadata repository is refreshing the information about a relationship for the other
     * repositories in the cohort.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param relationship relationship details
     */
    public abstract void processRefreshRelationshipEvent(String       sourceName,
                                                         String       originatorMetadataCollectionId,
                                                         String       originatorServerName,
                                                         String       originatorServerType,
                                                         String       originatorOrganizationName,
                                                         Relationship relationship);


    /**
     * An open metadata repository is passing information about a collection of entities and relationships
     * with the other repositories in the cohort.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param instances multiple entities and relationships for sharing.
     */
    public abstract void processInstanceBatchEvent(String         sourceName,
                                                   String         originatorMetadataCollectionId,
                                                   String         originatorServerName,
                                                   String         originatorServerType,
                                                   String         originatorOrganizationName,
                                                   InstanceGraph  instances);


    /**
     * An open metadata repository has detected two metadata instances with the same identifier (guid).
     * This is a serious error because it could lead to corruption of the metadata collections within the cohort.
     * When this occurs, all repositories in the cohort delete their reference copies of the metadata instances and
     * at least one of the instances has its GUID changed in its respective home repository.  The updated instance(s)
     * are redistributed around the cohort.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId metadata collection id of the repository reporting the conflicting instance
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param targetMetadataCollectionId metadata collection id of other repository with the conflicting instance
     * @param targetTypeDef details of the target instance's TypeDef
     * @param targetInstanceGUID unique identifier for the source instance
     * @param otherOrigin origin of the other (older) metadata instance
     * @param otherMetadataCollectionId metadata collection of the other (older) metadata instance
     * @param otherTypeDef details of the other (older) instance's TypeDef
     * @param otherInstanceGUID unique identifier for the other (older) instance
     * @param errorMessage description of the error.
     */
    public abstract void processConflictingInstancesEvent(String                 sourceName,
                                                          String                 originatorMetadataCollectionId,
                                                          String                 originatorServerName,
                                                          String                 originatorServerType,
                                                          String                 originatorOrganizationName,
                                                          String                 targetMetadataCollectionId,
                                                          TypeDefSummary         targetTypeDef,
                                                          String                 targetInstanceGUID,
                                                          String                 otherMetadataCollectionId,
                                                          InstanceProvenanceType otherOrigin,
                                                          TypeDefSummary         otherTypeDef,
                                                          String                 otherInstanceGUID,
                                                          String                 errorMessage);


    /**
     * An open metadata repository has detected an inconsistency in the version number of the type used in an updated metadata
     * instance compared to its stored version.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId metadata collection id of the repository reporting the conflicting instance
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param targetMetadataCollectionId metadata collection id of other repository with the conflicting instance
     * @param targetTypeDef description of the target instance's TypeDef
     * @param targetInstanceGUID unique identifier for the source instance
     * @param otherTypeDef details of the other (older) instance's TypeDef
     * @param errorMessage description of the error.
     */
    public abstract void processConflictingTypeEvent(String                 sourceName,
                                                     String                 originatorMetadataCollectionId,
                                                     String                 originatorServerName,
                                                     String                 originatorServerType,
                                                     String                 originatorOrganizationName,
                                                     String                 targetMetadataCollectionId,
                                                     TypeDefSummary         targetTypeDef,
                                                     String                 targetInstanceGUID,
                                                     TypeDefSummary         otherTypeDef,
                                                     String                 errorMessage);
}

