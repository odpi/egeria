/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.omf.listener;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.omf.handlers.MetadataElementHandler;
import org.odpi.openmetadata.frameworkservices.omf.outtopic.OpenMetadataOutTopicPublisher;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListenerBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * OpenMetadataOMRSTopicListener received details of each OMRS event from the cohorts that the local server
 * is connected to.  It passes NEW_ENTITY_EVENTs to the publisher.
 */
public class OpenMetadataOMRSTopicListener extends OMRSTopicListenerBase
{
    private static final Logger log = LoggerFactory.getLogger(OpenMetadataOMRSTopicListener.class);

    private final OpenMetadataOutTopicPublisher               eventPublisher;
    private final MetadataElementHandler<OpenMetadataElement> metadataElementHandler;
    private final String                                      localServerUserId;


    /**
     * Initialize the topic listener.
     *
     * @param serviceName this is the full name of the service - used for error logging in base class
     * @param localServerUserId userId used by this server for metadata governance
     * @param eventPublisher this is the out topic publisher
     * @param metadataElementHandler handler for retrieving asset information
     * @param auditLog logging destination
     */
    public OpenMetadataOMRSTopicListener(String                                      serviceName,
                                         String                                      localServerUserId,
                                         OpenMetadataOutTopicPublisher               eventPublisher,
                                         MetadataElementHandler<OpenMetadataElement> metadataElementHandler,
                                         AuditLog                                    auditLog)
    {
        super(serviceName, auditLog);

        this.metadataElementHandler = metadataElementHandler;
        this.localServerUserId      = localServerUserId;
        this.eventPublisher = eventPublisher;
    }

    /**
     * A new entity has been created.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param entity                         details of the new entity
     */
    @Override
    public void processNewEntityEvent(String       sourceName,
                                      String       originatorMetadataCollectionId,
                                      String       originatorServerName,
                                      String       originatorServerType,
                                      String       originatorOrganizationName,
                                      EntityDetail entity)
    {
        log.debug("Received new Entity event from: " + sourceName);

        EntityDetail eventEntity = this.entityOfInterest(localServerUserId, entity, null);

        if (eventEntity != null)
        {
            log.debug("Publishing new Entity event from: " + sourceName);

            eventPublisher.publishEntityEvent(eventEntity, OpenMetadataEventType.NEW_ELEMENT_CREATED);
        }
    }


    /**
     * An existing entity has been updated.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param oldEntity                      original values for the entity.
     * @param newEntity                      details of the new version of the entity.
     */
    @Override
    public void processUpdatedEntityEvent(String       sourceName,
                                          String       originatorMetadataCollectionId,
                                          String       originatorServerName,
                                          String       originatorServerType,
                                          String       originatorOrganizationName,
                                          EntityDetail oldEntity,
                                          EntityDetail newEntity)
    {
        log.debug("Received updated Entity event from: " + sourceName);

        EntityDetail eventEntity = this.entityOfInterest(localServerUserId, newEntity, null);

        if (eventEntity != null)
        {
            log.debug("Publishing updated Entity event from: " + sourceName);

            eventPublisher.publishEntityEvent(OpenMetadataEventType.ELEMENT_UPDATED,
                                              eventEntity,
                                              oldEntity,
                                              null,
                                              null);
        }
    }


    /**
     * A new classification has been added to an entityProxy.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entityProxy  details of the entityProxy with the new classification added. No guarantee this is all the classifications.
     * @param classification new classification
     */
    @Override
    public void processClassifiedEntityEvent(String         sourceName,
                                             String         originatorMetadataCollectionId,
                                             String         originatorServerName,
                                             String         originatorServerType,
                                             String         originatorOrganizationName,
                                             EntityProxy    entityProxy,
                                             Classification classification)
    {
        log.debug("Processing classified EntityProxy event from: " + sourceName);

        EntityDetail eventEntity = this.entityOfInterest(localServerUserId, null, entityProxy);

        if (eventEntity != null)
        {
            log.debug("Publishing declassified Entity event from: " + sourceName);

            eventPublisher.publishEntityEvent(OpenMetadataEventType.ELEMENT_DECLASSIFIED,
                                              eventEntity,
                                              null,
                                              null,
                                              classification);
        }
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
    @Override
    public void processClassifiedEntityEvent(String         sourceName,
                                             String         originatorMetadataCollectionId,
                                             String         originatorServerName,
                                             String         originatorServerType,
                                             String         originatorOrganizationName,
                                             EntityDetail   entity,
                                             Classification classification)
    {
        log.debug("Received classified Entity event from: " + sourceName);

        EntityDetail eventEntity = this.entityOfInterest(localServerUserId, entity, null);

        if (eventEntity != null)
        {
            log.debug("Publishing classified Entity event from: " + sourceName);

            eventPublisher.publishEntityEvent(OpenMetadataEventType.ELEMENT_CLASSIFIED,
                                              eventEntity,
                                              null,
                                              classification,
                                              null);
        }
    }


    /**
     * A new classification has been added to an entity.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param entity                         details of the entity with the new classification added.
     */
    @SuppressWarnings(value = "deprecation")
    @Override
    public void processClassifiedEntityEvent(String       sourceName,
                                             String       originatorMetadataCollectionId,
                                             String       originatorServerName,
                                             String       originatorServerType,
                                             String       originatorOrganizationName,
                                             EntityDetail entity)
    {
        log.debug("Received classified Entity event from: " + sourceName);

        EntityDetail eventEntity = this.entityOfInterest(localServerUserId, entity, null);

        if (eventEntity != null)
        {
            log.debug("Publishing classified Entity event from: " + sourceName);

            eventPublisher.publishEntityEvent(OpenMetadataEventType.ELEMENT_CLASSIFIED,
                                              eventEntity,
                                              null,
                                              null,
                                              null);
        }
    }


    /**
     * A classification has been removed from an entityProxy.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entityProxy  details of the entityProxy after the classification has been removed. No guarantee this is all the classifications.
     * @param originalClassification classification that was removed
     */
    @Override
    public void processDeclassifiedEntityEvent(String         sourceName,
                                               String         originatorMetadataCollectionId,
                                               String         originatorServerName,
                                               String         originatorServerType,
                                               String         originatorOrganizationName,
                                               EntityProxy    entityProxy,
                                               Classification originalClassification)
    {
        log.debug("Receiving declassified Entity event from: " + sourceName);

        EntityDetail eventEntity = this.entityOfInterest(localServerUserId, null, entityProxy);

        if (eventEntity != null)
        {
            log.debug("Publishing declassified Entity event from: " + sourceName);

            eventPublisher.publishEntityEvent(OpenMetadataEventType.ELEMENT_DECLASSIFIED,
                                              eventEntity,
                                              null,
                                              null,
                                              originalClassification);
        }
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
    @Override
    public void processDeclassifiedEntityEvent(String         sourceName,
                                               String         originatorMetadataCollectionId,
                                               String         originatorServerName,
                                               String         originatorServerType,
                                               String         originatorOrganizationName,
                                               EntityDetail   entity,
                                               Classification originalClassification)
    {
        log.debug("Receiving declassified Entity event from: " + sourceName);

        EntityDetail eventEntity = this.entityOfInterest(localServerUserId, entity, null);

        if (eventEntity != null)
        {
            log.debug("Publishing declassified Entity event from: " + sourceName);

            eventPublisher.publishEntityEvent(OpenMetadataEventType.ELEMENT_DECLASSIFIED,
                                              eventEntity,
                                              null,
                                              null,
                                              originalClassification);
        }
    }


    /**
     * A classification has been removed from an entity.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param entity                         details of the entity after the classification has been removed.
     */
    @SuppressWarnings(value = "deprecation")
    @Override
    public void processDeclassifiedEntityEvent(String       sourceName,
                                               String       originatorMetadataCollectionId,
                                               String       originatorServerName,
                                               String       originatorServerType,
                                               String       originatorOrganizationName,
                                               EntityDetail entity)
    {
        log.debug("Receiving declassified Entity event from: " + sourceName);

        EntityDetail eventEntity = this.entityOfInterest(localServerUserId, entity, null);

        if (eventEntity != null)
        {
            log.debug("Publishing declassified Entity event from: " + sourceName);

            eventPublisher.publishEntityEvent(OpenMetadataEventType.ELEMENT_DECLASSIFIED,
                                              eventEntity,
                                              null,
                                              null,
                                              null);
        }
    }


    /**
     * An existing classification has been changed on an entityProxy.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entityProxy  details of the entityProxy after the classification has been changed. No guarantee this is all the classifications.
     * @param originalClassification classification that was removed
     * @param classification new classification
     */
    @Override
    public void processReclassifiedEntityEvent(String         sourceName,
                                               String         originatorMetadataCollectionId,
                                               String         originatorServerName,
                                               String         originatorServerType,
                                               String         originatorOrganizationName,
                                               EntityProxy    entityProxy,
                                               Classification originalClassification,
                                               Classification classification)
    {
        log.debug("Processing reclassified EntityProxy event from: " + sourceName);

        EntityDetail eventEntity = this.entityOfInterest(localServerUserId, null, entityProxy);

        if (eventEntity != null)
        {
            log.debug("Publishing reclassified Entity event from: " + sourceName);

            eventPublisher.publishEntityEvent(OpenMetadataEventType.ELEMENT_RECLASSIFIED,
                                              eventEntity,
                                              null,
                                              classification,
                                              originalClassification);
        }
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
    @Override
    public void processReclassifiedEntityEvent(String         sourceName,
                                               String         originatorMetadataCollectionId,
                                               String         originatorServerName,
                                               String         originatorServerType,
                                               String         originatorOrganizationName,
                                               EntityDetail   entity,
                                               Classification originalClassification,
                                               Classification classification)
    {
        log.debug("Receiving reclassified Entity event from: " + sourceName);

        EntityDetail eventEntity = this.entityOfInterest(localServerUserId, entity, null);

        if (eventEntity != null)
        {
            log.debug("Publishing reclassified Entity event from: " + sourceName);

            eventPublisher.publishEntityEvent(OpenMetadataEventType.ELEMENT_RECLASSIFIED,
                                              eventEntity,
                                              null,
                                              classification,
                                              originalClassification);
        }
    }


    /**
     * An existing classification has been changed on an entity. Only implement one of the processReclassifiedEntityEvent methods
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param entity                         details of the entity after the classification has been changed.
     */
    @SuppressWarnings(value = "deprecation")
    @Override
    public void processReclassifiedEntityEvent(String       sourceName,
                                               String       originatorMetadataCollectionId,
                                               String       originatorServerName,
                                               String       originatorServerType,
                                               String       originatorOrganizationName,
                                               EntityDetail entity)
    {
        log.debug("Receiving reclassified Entity event from: " + sourceName);

        EntityDetail eventEntity = this.entityOfInterest(localServerUserId, entity, null);

        if (eventEntity != null)
        {
            log.debug("Publishing reclassified Entity event from: " + sourceName);

            eventPublisher.publishEntityEvent(OpenMetadataEventType.ELEMENT_RECLASSIFIED,
                                              eventEntity,
                                              null,
                                              null,
                                              null);
        }
    }


    /**
     * An existing entity has been deleted.  This is a soft delete. This means it is still in the repository,
     * but it is no longer returned on queries.
     * <p>
     * All relationships to the entity are also soft-deleted and will no longer be usable.  These deleted relationships
     * will be notified through separate events.
     * </p>
     * <p>
     * Details of the TypeDef are included with the entity's unique id (guid) to ensure the right entity is deleted in
     * the remote repositories.
     * </p>
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param entity                         deleted entity
     */
    @Override
    public void processDeletedEntityEvent(String       sourceName,
                                          String       originatorMetadataCollectionId,
                                          String       originatorServerName,
                                          String       originatorServerType,
                                          String       originatorOrganizationName,
                                          EntityDetail entity)
    {
        log.debug("Receiving deleted Entity event from: " + sourceName);

        EntityDetail eventEntity = this.entityOfInterest(localServerUserId, entity, null);

        if (eventEntity != null)
        {
            log.debug("Publishing deleted Entity event from: " + sourceName);

            eventPublisher.publishEntityEvent(eventEntity, OpenMetadataEventType.ELEMENT_DELETED);
        }
    }



    /**
     * An existing entity has been deleted and purged in a single action.
     * All relationships to the entity are also deleted and purged and will no longer be usable.  These deleted relationships
     * will be notified through separate events.
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
    @Override
    public void processDeletePurgedEntityEvent(String       sourceName,
                                               String       originatorMetadataCollectionId,
                                               String       originatorServerName,
                                               String       originatorServerType,
                                               String       originatorOrganizationName,
                                               EntityDetail entity)
    {
        log.debug("Processing delete-purge entity event from: " + sourceName);

        EntityDetail eventEntity = this.entityOfInterest(localServerUserId, entity, null);

        if (eventEntity != null)
        {
            log.debug("Publishing delete-purge entity event from: " + sourceName);

            eventPublisher.publishEntityEvent(eventEntity, OpenMetadataEventType.ELEMENT_DELETED);
        }
    }


    /**
     * A deleted entity has been restored to the state it was before it was deleted.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param entity                         details of the version of the entity that has been restored.
     */
    @Override
    public void processRestoredEntityEvent(String       sourceName,
                                           String       originatorMetadataCollectionId,
                                           String       originatorServerName,
                                           String       originatorServerType,
                                           String       originatorOrganizationName,
                                           EntityDetail entity)
    {
        log.debug("Receiving restored Entity event from: " + sourceName);

        EntityDetail eventEntity = this.entityOfInterest(localServerUserId, entity, null);

        if (eventEntity != null)
        {
            log.debug("Publishing restored Entity event from: " + sourceName);

            eventPublisher.publishEntityEvent(eventEntity, OpenMetadataEventType.ELEMENT_RESTORED);
        }
    }


    /**
     * The guid of an existing entity has been changed to a new value.  This is used if two different
     * entities are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param originalEntityGUID             the existing identifier for the entity.
     * @param entity                         new values for this entity, including the new guid.
     */
    @Override
    public void processReIdentifiedEntityEvent(String       sourceName,
                                               String       originatorMetadataCollectionId,
                                               String       originatorServerName,
                                               String       originatorServerType,
                                               String       originatorOrganizationName,
                                               String       originalEntityGUID,
                                               EntityDetail entity)
    {
        log.debug("Processing re-identified Entity event from: " + sourceName);

        EntityDetail eventEntity = this.entityOfInterest(localServerUserId, entity, null);

        if (eventEntity != null)
        {
            log.debug("Publishing re-identified Entity event from: " + sourceName);

            eventPublisher.publishEntityEvent(eventEntity, OpenMetadataEventType.ELEMENT_GUID_CHANGED);
        }
    }


    /**
     * An existing entity has had its type changed.  Typically, this action is taken to move an entity's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param originalTypeDefSummary         original details of this entity's TypeDef.
     * @param entity                         new values for this entity, including the new type information.
     */
    @Override
    public void processReTypedEntityEvent(String         sourceName,
                                          String         originatorMetadataCollectionId,
                                          String         originatorServerName,
                                          String         originatorServerType,
                                          String         originatorOrganizationName,
                                          TypeDefSummary originalTypeDefSummary,
                                          EntityDetail   entity)
    {
        log.debug("Processing re-typed Entity event from: " + sourceName);

        EntityDetail eventEntity = this.entityOfInterest(localServerUserId, entity, null);

        if (eventEntity != null)
        {
            log.debug("Publishing re-typed Entity event from: " + sourceName);

            eventPublisher.publishEntityEvent(eventEntity, OpenMetadataEventType.ELEMENT_TYPE_CHANGED);
        }
    }


    /**
     * An existing entity has changed home repository.  This action is taken for example, if a repository
     * becomes permanently unavailable, or if the user community updating this entity move to working
     * from a different repository in the open metadata repository cohort.
     *
     * @param sourceName                       name of the source of the event.  It may be the cohort name for incoming events or the
     *                                         local repository, or event mapper name.
     * @param originatorMetadataCollectionId   unique identifier for the metadata collection hosted by the server that
     *                                         sent the event.
     * @param originatorServerName             name of the server that the event came from.
     * @param originatorServerType             type of server that the event came from.
     * @param originatorOrganizationName       name of the organization that owns the server that sent the event.
     * @param originalHomeMetadataCollectionId unique identifier for the original home repository.
     * @param entity                           new values for this entity, including the new home information.
     */
    @Override
    public void processReHomedEntityEvent(String       sourceName,
                                          String       originatorMetadataCollectionId,
                                          String       originatorServerName,
                                          String       originatorServerType,
                                          String       originatorOrganizationName,
                                          String       originalHomeMetadataCollectionId,
                                          EntityDetail entity)
    {
        log.debug("Processing re-homed Entity event from: " + sourceName);

        EntityDetail eventEntity = this.entityOfInterest(localServerUserId, entity, null);

        if (eventEntity != null)
        {
            log.debug("Publishing re-homed Entity event from: " + sourceName);

            eventPublisher.publishEntityEvent(eventEntity, OpenMetadataEventType.ELEMENT_HOME_CHANGED);
        }
    }


    /**
     * A remote repository in the cohort has sent entity details in response to a refresh request.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param entity                         details of the requested entity
     */
    @SuppressWarnings(value = "unused")
    @Override
    public void processRefreshEntityEvent(String       sourceName,
                                          String       originatorMetadataCollectionId,
                                          String       originatorServerName,
                                          String       originatorServerType,
                                          String       originatorOrganizationName,
                                          EntityDetail entity)
    {
        log.debug("Receiving refresh Entity event from: " + sourceName);

        EntityDetail eventEntity = this.entityOfInterest(localServerUserId, entity, null);

        if (eventEntity != null)
        {
            log.debug("Publishing refresh Entity event from: " + sourceName);

            eventPublisher.publishEntityEvent(eventEntity, OpenMetadataEventType.REFRESH_ELEMENT_EVENT);
        }
    }


    /**
     * A new relationship has been created.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param relationship                   details of the new relationship
     */
    @Override
    public void processNewRelationshipEvent(String       sourceName,
                                            String       originatorMetadataCollectionId,
                                            String       originatorServerName,
                                            String       originatorServerType,
                                            String       originatorOrganizationName,
                                            Relationship relationship)
    {
        log.debug("Receiving new relationship event from: " + sourceName);

        this.processRelationshipEvent(OpenMetadataEventType.NEW_ELEMENT_CREATED, relationship, null);
    }


    /**
     * An existing relationship has been updated.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param oldRelationship                original details of the relationship.
     * @param newRelationship                details of the new version of the relationship.
     */
    @Override
    public void processUpdatedRelationshipEvent(String       sourceName,
                                                String       originatorMetadataCollectionId,
                                                String       originatorServerName,
                                                String       originatorServerType,
                                                String       originatorOrganizationName,
                                                Relationship oldRelationship,
                                                Relationship newRelationship)
    {
        log.debug("Receiving updated relationship event from: " + sourceName);

        this.processRelationshipEvent(OpenMetadataEventType.ELEMENT_UPDATED, newRelationship, oldRelationship);
    }


    /**
     * An existing relationship has been deleted.  This is a soft delete. This means it is still in the repository,
     * but it is no longer returned on queries.
     * <p>
     * Details of the TypeDef are included with the relationship's unique id (guid) to ensure the right
     * relationship is deleted in the remote repositories.
     * </p>
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param relationship                   deleted relationship
     */
    @Override
    public void processDeletedRelationshipEvent(String       sourceName,
                                                String       originatorMetadataCollectionId,
                                                String       originatorServerName,
                                                String       originatorServerType,
                                                String       originatorOrganizationName,
                                                Relationship relationship)
    {
        log.debug("Receiving deleted relationship event from: " + sourceName);

        this.processRelationshipEvent(OpenMetadataEventType.ELEMENT_DELETED, relationship, null);
    }


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
    @Override
    public void processDeletePurgedRelationshipEvent(String       sourceName,
                                                     String       originatorMetadataCollectionId,
                                                     String       originatorServerName,
                                                     String       originatorServerType,
                                                     String       originatorOrganizationName,
                                                     Relationship relationship)
    {
        log.debug("Receiving delete-purge relationship event from: " + sourceName);

        this.processRelationshipEvent(OpenMetadataEventType.ELEMENT_DELETED, relationship, null);
    }


    /**
     * The guid of an existing relationship has changed.  This is used if two different
     * relationships are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param originalRelationshipGUID       the existing identifier for the relationship.
     * @param relationship                   new values for this relationship, including the new guid.
     */
    @Override
    public void processReIdentifiedRelationshipEvent(String       sourceName,
                                                     String       originatorMetadataCollectionId,
                                                     String       originatorServerName,
                                                     String       originatorServerType,
                                                     String       originatorOrganizationName,
                                                     String       originalRelationshipGUID,
                                                     Relationship relationship)
    {
        log.debug("Receiving reidentified relationship event from: " + sourceName);

        this.processRelationshipEvent(OpenMetadataEventType.ELEMENT_GUID_CHANGED, relationship, null);
    }


    /**
     * An existing relationship has had its type changed.  Typically, this action is taken to move a relationship's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param originalTypeDefSummary         original details of this relationship's TypeDef.
     * @param relationship                   new values for this relationship, including the new type information.
     */
    @Override
    public void processReTypedRelationshipEvent(String         sourceName,
                                                String         originatorMetadataCollectionId,
                                                String         originatorServerName,
                                                String         originatorServerType,
                                                String         originatorOrganizationName,
                                                TypeDefSummary originalTypeDefSummary,
                                                Relationship   relationship)
    {
        log.debug("Receiving retyped relationship event from: " + sourceName);

        this.processRelationshipEvent(OpenMetadataEventType.ELEMENT_TYPE_CHANGED, relationship, null);
    }


    /**
     * An existing relationship has changed home repository.  This action is taken for example, if a repository
     * becomes permanently unavailable, or if the user community updating this relationship move to working
     * from a different repository in the open metadata repository cohort.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param originalHomeMetadataCollection unique identifier for the original home repository.
     * @param relationship                   new values for this relationship, including the new home information.
     */
    @Override
    public void processReHomedRelationshipEvent(String       sourceName,
                                                String       originatorMetadataCollectionId,
                                                String       originatorServerName,
                                                String       originatorServerType,
                                                String       originatorOrganizationName,
                                                String       originalHomeMetadataCollection,
                                                Relationship relationship)
    {
        log.debug("Receiving rehomed relationship event from: " + sourceName);

        this.processRelationshipEvent(OpenMetadataEventType.ELEMENT_HOME_CHANGED, relationship, null);
    }



    /**
     * The local repository is refreshing the information about a relationship for the other
     * repositories in the cohort.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param relationship                   relationship details
     */
    @SuppressWarnings(value = "unused")
    @Override
    public void processRefreshRelationshipEvent(String       sourceName,
                                                String       originatorMetadataCollectionId,
                                                String       originatorServerName,
                                                String       originatorServerType,
                                                String       originatorOrganizationName,
                                                Relationship relationship)
    {
        log.debug("Processing refresh relationship event from: " + sourceName);

        this.processRelationshipEvent(OpenMetadataEventType.ELEMENT_HOME_CHANGED, relationship, null);
    }


    /**
     * Test to see if the entity is going to be sent out as an event - it must be a referenceable - and if an asset or anchored to an asset
     * then it must be in one of the supported zones.
     *
     * @param userId callers userId
     * @param entity entity to test
     * @param entityProxy entity proxy when entity is not available
     * @return entity detail if it is to be sent.
     */
    private EntityDetail entityOfInterest(String       userId,
                                          EntityDetail entity,
                                          EntityProxy  entityProxy)
    {
        final String methodName = "entityOfInterest";
        final String guidParameterName = "entity.getGUID()";

        try
        {
            EntityDetail fullEntity    = null;
            Date         effectiveTime = new Date();

            if (entity != null)
            {
                fullEntity = entity;

                metadataElementHandler.validateEntityAndAnchorForRead(userId,
                                                                      OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                      fullEntity,
                                                                      guidParameterName,
                                                                      false,
                                                                      false,
                                                                      true,
                                                                      false,
                                                                      effectiveTime,
                                                                      methodName);
            }
            else if (entityProxy != null)
            {
                fullEntity = metadataElementHandler.getEntityFromRepository(userId,
                                                                            entityProxy.getGUID(),
                                                                            guidParameterName,
                                                                            entityProxy.getType().getTypeDefName(),
                                                                            null,
                                                                            null,
                                                                            false,
                                                                            false,
                                                                            null,
                                                                            effectiveTime,
                                                                            methodName);
            }

            return fullEntity;
        }
        catch (Exception error)
        {
            if (entity != null)
            {
                log.debug("Element " + entity.getGUID() + " not visible");
            }
            else
            {
                log.debug("Element " + entityProxy.getGUID() + " not visible");
            }
        }

        return null;
    }


    /**
     * A relationship event needs processing.
     *
     * @param eventType                      type of change to the relationship
     * @param relationship                   details of the changed relationship
     * @param previousRelationship           details of the relationship before it changed
     */
    private void processRelationshipEvent(OpenMetadataEventType eventType,
                                          Relationship          relationship,
                                          Relationship          previousRelationship)
    {
        EntityDetail end1 = this.entityOfInterest(localServerUserId, null, relationship.getEntityOneProxy());
        EntityDetail end2 = this.entityOfInterest(localServerUserId, null, relationship.getEntityTwoProxy());

        if ((end1 != null) && (end2 != null))
        {
            log.debug("Publishing " + eventType.getEventTypeName() + " relationship event");

            eventPublisher.publishRelationshipEvent(eventType, relationship, previousRelationship, end1, end2);
        }
    }
}
