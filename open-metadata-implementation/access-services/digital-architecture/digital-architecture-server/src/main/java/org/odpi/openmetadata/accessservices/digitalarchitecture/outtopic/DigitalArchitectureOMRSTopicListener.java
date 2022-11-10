/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.outtopic;

import org.odpi.openmetadata.accessservices.digitalarchitecture.events.DigitalArchitectureEventType;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.commonservices.generichandlers.ReferenceableHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListenerBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * DigitalArchitectureOMRSTopicListener received details of each OMRS event from the cohorts that the local server
 * is connected to.  It passes NEW_ENTITY_EVENTs to the publisher.
 */
public class DigitalArchitectureOMRSTopicListener extends OMRSTopicListenerBase
{
    private static final Logger log = LoggerFactory.getLogger(DigitalArchitectureOMRSTopicListener.class);

    private final DigitalArchitectureOutTopicPublisher eventPublisher;
    private final ReferenceableHandler<ElementHeader>  referenceableHandler;
    private final String                               localServerUserId;
    private final List<String>                         supportedZones;


    /**
     * Initialize the topic listener.
     *
     * @param serviceName this is the full name of the service - used for error logging in base class
     * @param localServerUserId userId used by this server for metadata governance
     * @param eventPublisher this is the out topic publisher
     * @param referenceableHandler handler for retrieving asset information
     * @param supportedZones list of zones that the access service is allowed to serve instances from.
     * @param auditLog logging destination
     */
    public DigitalArchitectureOMRSTopicListener(String                               serviceName,
                                                String                               localServerUserId,
                                                DigitalArchitectureOutTopicPublisher eventPublisher,
                                                ReferenceableHandler<ElementHeader>  referenceableHandler,
                                                List<String>                         supportedZones,
                                                AuditLog                             auditLog)
    {
        super(serviceName, auditLog);

        this.referenceableHandler = referenceableHandler;
        this.supportedZones = supportedZones;
        this.localServerUserId = localServerUserId;
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

            eventPublisher.publishEntityEvent(eventEntity, DigitalArchitectureEventType.NEW_ELEMENT_CREATED);
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

            eventPublisher.publishEntityEvent(DigitalArchitectureEventType.ELEMENT_UPDATED,
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
                                             EntityProxy entityProxy,
                                             Classification classification)
    {
        log.debug("Processing classified EntityProxy event from: " + sourceName);

        EntityDetail eventEntity = this.entityOfInterest(localServerUserId, null, entityProxy);

        if (eventEntity != null)
        {
            log.debug("Publishing declassified Entity event from: " + sourceName);

            eventPublisher.publishEntityEvent(DigitalArchitectureEventType.ELEMENT_DECLASSIFIED,
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

            eventPublisher.publishEntityEvent(DigitalArchitectureEventType.ELEMENT_CLASSIFIED,
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

            eventPublisher.publishEntityEvent(DigitalArchitectureEventType.ELEMENT_CLASSIFIED,
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

            eventPublisher.publishEntityEvent(DigitalArchitectureEventType.ELEMENT_DECLASSIFIED,
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

            eventPublisher.publishEntityEvent(DigitalArchitectureEventType.ELEMENT_DECLASSIFIED,
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

            eventPublisher.publishEntityEvent(DigitalArchitectureEventType.ELEMENT_DECLASSIFIED,
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

            eventPublisher.publishEntityEvent(DigitalArchitectureEventType.ELEMENT_RECLASSIFIED,
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

            eventPublisher.publishEntityEvent(DigitalArchitectureEventType.ELEMENT_RECLASSIFIED,
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

            eventPublisher.publishEntityEvent(DigitalArchitectureEventType.ELEMENT_RECLASSIFIED,
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
     * <p>
     * Details of the TypeDef are included with the entity's unique id (guid) to ensure the right entity is deleted in
     * the remote repositories.
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

            eventPublisher.publishEntityEvent(eventEntity, DigitalArchitectureEventType.ELEMENT_DELETED);
        }
    }



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

            eventPublisher.publishEntityEvent(eventEntity, DigitalArchitectureEventType.ELEMENT_DELETED);
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

            eventPublisher.publishEntityEvent(eventEntity, DigitalArchitectureEventType.ELEMENT_RESTORED);
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

            eventPublisher.publishEntityEvent(eventEntity, DigitalArchitectureEventType.ELEMENT_GUID_CHANGED);
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

            eventPublisher.publishEntityEvent(eventEntity, DigitalArchitectureEventType.ELEMENT_TYPE_CHANGED);
        }
    }


    /**
     * An existing entity has changed home repository.  This action is taken for example, if a repository
     * becomes permanently unavailable, or if the user community updating this entity move to working
     * from a different repository in the open metadata repository cluster.
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
            log.debug("Publishing re-homes Entity event from: " + sourceName);

            eventPublisher.publishEntityEvent(eventEntity, DigitalArchitectureEventType.ELEMENT_HOME_CHANGED);
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

            eventPublisher.publishEntityEvent(eventEntity, DigitalArchitectureEventType.REFRESH_ELEMENT_EVENT);
        }
    }


    /**
     * Test to see if the entity is going to be sent out as an event - it must be a referenceable - and if an asset or anchored to an asset
     * then it must be in one of the supported zones.
     *
     * @param userId callers userId
     * @param entity entity to test
     * @param entityProxy entity proxy when entity is not available
     * @return entity detail if it is sent.
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
                /*
                 * Test the type first before doing any real work
                 */
                if (this.isTypeOfInterest(entity))
                {
                    fullEntity = entity;

                    referenceableHandler.validateAnchorEntity(userId,
                                                              fullEntity.getGUID(),
                                                              OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                                              fullEntity,
                                                              guidParameterName,
                                                              false,
                                                              true,
                                                              false,
                                                              supportedZones,
                                                              effectiveTime,
                                                              methodName);
                }
            }
            else if (entityProxy != null)
            {
                /*
                 * Test the type first before doing any real work
                 */
                if (this.isTypeOfInterest(entityProxy))
                {
                    fullEntity = referenceableHandler.getEntityFromRepository(userId,
                                                                              entityProxy.getGUID(),
                                                                              guidParameterName,
                                                                              entityProxy.getType().getTypeDefName(),
                                                                              null,
                                                                              null,
                                                                              false,
                                                                              false,
                                                                              supportedZones,
                                                                              effectiveTime,
                                                                              methodName);
                }
            }

            if (fullEntity != null)
            {
                return fullEntity;
            }
        }
        catch (Exception error)
        {
            // element not visible
        }


        return null;
    }


    /**
     * Digital Architecture OMAS only publishes events of type Asset that
     *
     * @param entityHeader entity element
     * @return flag to say whether to publish the event.
     */
    private boolean isTypeOfInterest(InstanceHeader entityHeader)
    {
        final String interestingTypeName = "Referenceable";

        if (entityHeader != null)
        {
            List<String> typeNames = new ArrayList<>();

            typeNames.add(entityHeader.getType().getTypeDefName());

            if (entityHeader.getType().getTypeDefSuperTypes() != null)
            {
                for (TypeDefLink superType : entityHeader.getType().getTypeDefSuperTypes())
                {
                    if (superType != null)
                    {
                        typeNames.add(superType.getName());
                    }
                }
            }

            return typeNames.contains(interestingTypeName);
        }

        return false;
    }
}
