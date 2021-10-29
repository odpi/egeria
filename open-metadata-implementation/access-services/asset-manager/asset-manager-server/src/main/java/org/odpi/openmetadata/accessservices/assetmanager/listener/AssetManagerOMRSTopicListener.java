/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.listener;

import org.odpi.openmetadata.accessservices.assetmanager.events.AssetManagerEventType;
import org.odpi.openmetadata.accessservices.assetmanager.handlers.DataAssetExchangeHandler;
import org.odpi.openmetadata.accessservices.assetmanager.outtopic.AssetManagerOutTopicPublisher;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListenerBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * AssetManagerOMRSTopicListener received details of each OMRS event from the cohorts that the local server
 * is connected to.  It passes NEW_ENTITY_EVENTs to the publisher.
 */
public class AssetManagerOMRSTopicListener extends OMRSTopicListenerBase
{
    private static final Logger log = LoggerFactory.getLogger(AssetManagerOMRSTopicListener.class);

    private AssetManagerOutTopicPublisher eventPublisher;
    private DataAssetExchangeHandler      dataAssetExchangeHandler;
    private String                        localServerUserId;


    /**
     * Initialize the topic listener.
     *
     * @param serviceName this is the full name of the service - used for error logging in base class
     * @param localServerUserId userId used by this server for metadata governance
     * @param eventPublisher this is the out topic publisher.
     * @param auditLog logging destination
     */
    public AssetManagerOMRSTopicListener(String                        serviceName,
                                         String                        localServerUserId,
                                         AssetManagerOutTopicPublisher eventPublisher,
                                         DataAssetExchangeHandler      dataAssetExchangeHandler,
                                         AuditLog                      auditLog)
    {
        super(serviceName, auditLog);

        this.dataAssetExchangeHandler = dataAssetExchangeHandler;
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
    public void processNewEntityEvent(String       sourceName,
                                      String       originatorMetadataCollectionId,
                                      String       originatorServerName,
                                      String       originatorServerType,
                                      String       originatorOrganizationName,
                                      EntityDetail entity)
    {
        log.debug("Received new Entity event from: " + sourceName);

        if (dataAssetExchangeHandler.entityOfInterest(localServerUserId, entity))
        {
            log.debug("Publishing new Entity event from: " + sourceName);

            eventPublisher.publishEntityEvent(entity, AssetManagerEventType.NEW_ELEMENT_CREATED);
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
    public void processUpdatedEntityEvent(String       sourceName,
                                          String       originatorMetadataCollectionId,
                                          String       originatorServerName,
                                          String       originatorServerType,
                                          String       originatorOrganizationName,
                                          EntityDetail oldEntity,
                                          EntityDetail newEntity)
    {
        log.debug("Received updated Entity event from: " + sourceName);

        if (dataAssetExchangeHandler.entityOfInterest(localServerUserId, newEntity))
        {
            log.debug("Publishing updated Entity event from: " + sourceName);

            eventPublisher.publishEntityEvent(newEntity, AssetManagerEventType.ELEMENT_UPDATED);
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
     * @param entity  details of the entity with the new classification added. No guarantee this is all of the classifications.
     * @param classification new classification
     */
    public void processClassifiedEntityEvent(String         sourceName,
                                             String         originatorMetadataCollectionId,
                                             String         originatorServerName,
                                             String         originatorServerType,
                                             String         originatorOrganizationName,
                                             EntityDetail   entity,
                                             Classification classification)
    {
        log.debug("Received classified Entity event from: " + sourceName);

        if (dataAssetExchangeHandler.entityOfInterest(localServerUserId, entity))
        {
            log.debug("Publishing classified Entity event from: " + sourceName);

            if (classification != null)
            {
                eventPublisher.publishEntityEvent(entity, AssetManagerEventType.ELEMENT_CLASSIFIED, classification.getName());
            }
            else
            {
                eventPublisher.publishEntityEvent(entity, AssetManagerEventType.ELEMENT_CLASSIFIED);
            }
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
    public void processClassifiedEntityEvent(String       sourceName,
                                             String       originatorMetadataCollectionId,
                                             String       originatorServerName,
                                             String       originatorServerType,
                                             String       originatorOrganizationName,
                                             EntityDetail entity)
    {
        this.processClassifiedEntityEvent(sourceName,
                                          originatorMetadataCollectionId,
                                          originatorServerName,
                                          originatorServerType,
                                          originatorOrganizationName,
                                          entity,
                                          null);
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
     * @param entity  details of the entity after the classification has been removed. No guarantee this is all of the classifications.
     * @param originalClassification classification that was removed
     */
    public void processDeclassifiedEntityEvent(String         sourceName,
                                               String         originatorMetadataCollectionId,
                                               String         originatorServerName,
                                               String         originatorServerType,
                                               String         originatorOrganizationName,
                                               EntityDetail   entity,
                                               Classification originalClassification)
    {
        log.debug("Receiving declassified Entity event from: " + sourceName);

        if (dataAssetExchangeHandler.entityOfInterest(localServerUserId, entity))
        {
            log.debug("Publishing declassified Entity event from: " + sourceName);

            if (originalClassification != null)
            {
                eventPublisher.publishEntityEvent(entity, AssetManagerEventType.ELEMENT_DECLASSIFIED, originalClassification.getName());
            }
            else
            {
                eventPublisher.publishEntityEvent(entity, AssetManagerEventType.ELEMENT_DECLASSIFIED);
            }
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
    public void processDeclassifiedEntityEvent(String       sourceName,
                                               String       originatorMetadataCollectionId,
                                               String       originatorServerName,
                                               String       originatorServerType,
                                               String       originatorOrganizationName,
                                               EntityDetail entity)
    {
        this.processDeclassifiedEntityEvent(sourceName,
                                            originatorMetadataCollectionId,
                                            originatorServerName,
                                            originatorServerType,
                                            originatorOrganizationName,
                                            entity,
                                            null);
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
     * @param entity  details of the entity after the classification has been changed. No guarantee this is all of the classifications.
     * @param originalClassification classification that was removed
     * @param classification new classification
     */
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

        if (dataAssetExchangeHandler.entityOfInterest(localServerUserId, entity))
        {
            log.debug("Publishing reclassified Entity event from: " + sourceName);

            if (originalClassification != null)
            {
                eventPublisher.publishEntityEvent(entity, AssetManagerEventType.ELEMENT_RECLASSIFIED, originalClassification.getName());
            }
            else
            {
                eventPublisher.publishEntityEvent(entity, AssetManagerEventType.ELEMENT_RECLASSIFIED);
            }
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
    public void processReclassifiedEntityEvent(String       sourceName,
                                               String       originatorMetadataCollectionId,
                                               String       originatorServerName,
                                               String       originatorServerType,
                                               String       originatorOrganizationName,
                                               EntityDetail entity)
    {
        this.processReclassifiedEntityEvent(sourceName,
                                            originatorMetadataCollectionId,
                                            originatorServerName,
                                            originatorServerType,
                                            originatorOrganizationName,
                                            entity,
                                            null,
                                            null);
    }


    /**
     * An existing entity has been deleted.  This is a soft delete. This means it is still in the repository
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
    public void processDeletedEntityEvent(String       sourceName,
                                          String       originatorMetadataCollectionId,
                                          String       originatorServerName,
                                          String       originatorServerType,
                                          String       originatorOrganizationName,
                                          EntityDetail entity)
    {
        log.debug("Receiving deleted Entity event from: " + sourceName);

        if (dataAssetExchangeHandler.entityOfInterest(localServerUserId, entity))
        {
            log.debug("Publishing deleted Entity event from: " + sourceName);

            eventPublisher.publishEntityEvent(entity, AssetManagerEventType.ELEMENT_DELETED);
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
    public void processDeletePurgedEntityEvent(String       sourceName,
                                               String       originatorMetadataCollectionId,
                                               String       originatorServerName,
                                               String       originatorServerType,
                                               String       originatorOrganizationName,
                                               EntityDetail entity)
    {
        log.debug("Processing delete-purge entity event from: " + sourceName);

        if (dataAssetExchangeHandler.entityOfInterest(localServerUserId, entity))
        {
            log.debug("Publishing delete-purge entity event from: " + sourceName);

            eventPublisher.publishEntityEvent(entity, AssetManagerEventType.ELEMENT_DELETED);
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
    public void processRestoredEntityEvent(String       sourceName,
                                           String       originatorMetadataCollectionId,
                                           String       originatorServerName,
                                           String       originatorServerType,
                                           String       originatorOrganizationName,
                                           EntityDetail entity)
    {
        log.debug("Receiving restored Entity event from: " + sourceName);

        if (dataAssetExchangeHandler.entityOfInterest(localServerUserId, entity))
        {
            log.debug("Publishing restored Entity event from: " + sourceName);

            eventPublisher.publishEntityEvent(entity, AssetManagerEventType.ELEMENT_RESTORED);
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
    public void processReIdentifiedEntityEvent(String       sourceName,
                                               String       originatorMetadataCollectionId,
                                               String       originatorServerName,
                                               String       originatorServerType,
                                               String       originatorOrganizationName,
                                               String       originalEntityGUID,
                                               EntityDetail entity)
    {
        log.debug("Processing re-identified Entity event from: " + sourceName);

        if (dataAssetExchangeHandler.entityOfInterest(localServerUserId, entity))
        {
            log.debug("Publishing re-identified Entity event from: " + sourceName);

            eventPublisher.publishEntityEvent(entity, AssetManagerEventType.ELEMENT_GUID_CHANGED);
        }
    }


    /**
     * An existing entity has had its type changed.  Typically this action is taken to move an entity's
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
    public void processReTypedEntityEvent(String         sourceName,
                                          String         originatorMetadataCollectionId,
                                          String         originatorServerName,
                                          String         originatorServerType,
                                          String         originatorOrganizationName,
                                          TypeDefSummary originalTypeDefSummary,
                                          EntityDetail   entity)
    {
        log.debug("Processing re-typed Entity event from: " + sourceName);

        if (dataAssetExchangeHandler.entityOfInterest(localServerUserId, entity))
        {
            log.debug("Publishing re-typed Entity event from: " + sourceName);

            eventPublisher.publishEntityEvent(entity, AssetManagerEventType.ELEMENT_TYPE_CHANGED);
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
    public void processRefreshEntityEvent(String       sourceName,
                                          String       originatorMetadataCollectionId,
                                          String       originatorServerName,
                                          String       originatorServerType,
                                          String       originatorOrganizationName,
                                          EntityDetail entity)
    {
        log.debug("Receiving refresh Entity event from: " + sourceName);

        if (dataAssetExchangeHandler.entityOfInterest(localServerUserId, entity))
        {
            log.debug("Publishing refresh Entity event from: " + sourceName);

            eventPublisher.publishEntityEvent(entity, AssetManagerEventType.REFRESH_ELEMENT_EVENT);
        }
    }


}
