/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.listener;

import org.odpi.openmetadata.accessservices.datamanager.converters.DataManagerOMASConverter;
import org.odpi.openmetadata.accessservices.datamanager.events.DataManagerOutboundEventType;
import org.odpi.openmetadata.accessservices.datamanager.ffdc.DataManagerAuditCode;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.ElementStub;
import org.odpi.openmetadata.accessservices.datamanager.outtopic.DataManagerOutTopicPublisher;
import org.odpi.openmetadata.accessservices.datamanager.server.DataManagerServicesInstance;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListenerBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * DataManagerOMRSTopicListener received details of each OMRS event from the cohorts that the local server
 * is connected to.  It passes NEW_ENTITY_EVENTs to the publisher.
 */
public class DataManagerOMRSTopicListener extends OMRSTopicListenerBase
{
    private static final Logger log = LoggerFactory.getLogger(DataManagerOMRSTopicListener.class);

    private List<String>                               supportedZones;
    private DataManagerOutTopicPublisher               publisher;
    private OMRSRepositoryHelper                       repositoryHelper;
    private String                                     serverUserId;
    private DataManagerOMASConverter<ElementStub>      converter;
    private OpenMetadataAPIGenericHandler<ElementStub> genericHandler;


    /**
     * Initialize the topic listener.
     *
     * @param supportedZones list of zones for the visible assets
     * @param serviceName this is the full name of the service - used for error logging in base class
     * @param publisher this is the out topic publisher.
     * @param repositoryHelper repository helper
     * @param auditLog logging destination
     */
    public DataManagerOMRSTopicListener(List<String>                 supportedZones,
                                        DataManagerOutTopicPublisher publisher,
                                        String                       serverUserId,
                                        AuditLog                     auditLog,
                                        OMRSRepositoryHelper         repositoryHelper,
                                        String                       serviceName,
                                        String                       serverName,
                                        DataManagerServicesInstance  instance)
    {
        super(serviceName, auditLog);

        this.repositoryHelper = repositoryHelper;
        this.supportedZones = supportedZones;

        this.publisher = publisher;
        this.serverUserId = serverUserId;

        this.converter = new DataManagerOMASConverter<>(repositoryHelper, serviceName, serverName);
        this.genericHandler = instance.getGenericHandler();
    }


    /**
     * An entity has been changed.
     *
     * @param eventType                      type of change to the entity
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param entity                         details of the new entity
     * @param classificationName             potential classification change
     * @param methodName                     calling method
     */
    private void processEntityEvent(DataManagerOutboundEventType eventType,
                                    String                       sourceName,
                                    String                       originatorMetadataCollectionId,
                                    String                       originatorServerName,
                                    String                       originatorServerType,
                                    String                       originatorOrganizationName,
                                    EntityDetail                 entity,
                                    String                       classificationName,
                                    String                       methodName)
    {
        final String entityGUIDParameterName = "entity.getGUID()";

        String instanceTypeName = this.getInstanceTypeName(sourceName,
                                                           originatorMetadataCollectionId,
                                                           originatorServerName,
                                                           originatorServerType,
                                                           originatorOrganizationName,
                                                           entity,
                                                           methodName);

        if (instanceTypeName != null)
        {
            try
            {
                ElementStub elementStub = converter.getElementStub(ElementStub.class, entity, methodName);

                if ((repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataAPIMapper.DATA_SET_TYPE_NAME)) ||
                    (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataAPIMapper.DATA_STORE_TYPE_NAME)) ||
                    (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataAPIMapper.SCHEMA_ELEMENT_TYPE_NAME)))
                {
                    genericHandler.validateAnchorEntity(serverUserId,
                                                        entity.getGUID(),
                                                        instanceTypeName,
                                                        entity,
                                                        entityGUIDParameterName,
                                                        false,
                                                        supportedZones,
                                                        methodName);

                    /*
                     * The event will publish if the entity is visible through this interface.
                     */
                    publisher.sendEntityEvent(eventType, entity.getGUID(), instanceTypeName, classificationName, elementStub);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      DataManagerAuditCode.OUTBOUND_EVENT_EXCEPTION.getMessageDefinition(entity.getGUID(),
                                                                                                         instanceTypeName,
                                                                                                         error.getClass().getName(),
                                                                                                         error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * A relationship has changed.
     *
     * @param eventType                      type of change to the relationship
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param relationship                   details of the changed relationship
     * @param methodName                     calling method
     */
    private void processRelationshipEvent(DataManagerOutboundEventType eventType,
                                          String                       sourceName,
                                          String                       originatorMetadataCollectionId,
                                          String                       originatorServerName,
                                          String                       originatorServerType,
                                          String                       originatorOrganizationName,
                                          Relationship                 relationship,
                                          String                       methodName)
    {
        final String entityProxyOneGUIDParameterName = "relationship.getEntityProxyOne().getGUID()";
        final String entityProxyTwoGUIDParameterName = "relationship.getEntityProxyTwo().getGUID()";

        String instanceTypeName = this.getInstanceTypeName(sourceName,
                                                           originatorMetadataCollectionId,
                                                           originatorServerName,
                                                           originatorServerType,
                                                           originatorOrganizationName,
                                                           relationship,
                                                           methodName);

        if (instanceTypeName != null)
        {
            try
            {
                ElementStub relationshipElementStub = converter.getElementStub(ElementStub.class, relationship, methodName);
                ElementStub endOneElementStub = converter.getElementStub(ElementStub.class, relationship.getEntityOneProxy(), methodName);
                ElementStub endTwoElementStub = converter.getElementStub(ElementStub.class, relationship.getEntityTwoProxy(), methodName);

                if ((repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataAPIMapper.DATA_CONTENT_FOR_DATA_SET_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataAPIMapper.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataAPIMapper.SCHEMA_QUERY_TARGET_RELATIONSHIP_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataAPIMapper.LINKED_EXTERNAL_SCHEMA_TYPE_RELATIONSHIP_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataAPIMapper.MAP_FROM_RELATIONSHIP_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataAPIMapper.MAP_TO_RELATIONSHIP_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataAPIMapper.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_NAME)))
                {
                    genericHandler.validateAnchorEntity(serverUserId,
                                                        relationship.getEntityOneProxy().getGUID(),
                                                        entityProxyOneGUIDParameterName,
                                                        instanceTypeName,
                                                        false,
                                                        supportedZones,
                                                        methodName);

                    genericHandler.validateAnchorEntity(serverUserId,
                                                        relationship.getEntityTwoProxy().getGUID(),
                                                        entityProxyTwoGUIDParameterName,
                                                        instanceTypeName,
                                                        false,
                                                        supportedZones,
                                                        methodName);

                    /*
                     * The event will publish if both entities are visible to the publisher
                     */
                    publisher.sendRelationshipEvent(eventType, relationship.getGUID(), instanceTypeName, relationshipElementStub, endOneElementStub, endTwoElementStub);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      DataManagerAuditCode.OUTBOUND_EVENT_EXCEPTION.getMessageDefinition(relationship.getGUID(),
                                                                                                              instanceTypeName,
                                                                                                              error.getClass().getName(),
                                                                                                              error.getMessage()),
                                      error);
            }
        }
    }


    /*
     * Instance events
     */



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
        final String methodName = "processNewEntityEvent";

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.NEW_ELEMENT_EVENT;

        log.debug("Processing new Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                null,
                                methodName);
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
        final String methodName = "processUpdatedEntityEvent";

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.UPDATED_ELEMENT_EVENT;

        log.debug("Processing updated Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                newEntity,
                                null,
                                methodName);
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
    @Override
    public void processClassifiedEntityEvent(String         sourceName,
                                             String         originatorMetadataCollectionId,
                                             String         originatorServerName,
                                             String         originatorServerType,
                                             String         originatorOrganizationName,
                                             EntityDetail   entity,
                                             Classification classification)
    {
        final String methodName = "processClassifiedEntityEvent";

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.CLASSIFIED_ELEMENT_EVENT;

        log.debug("Processing classified Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                classification.getName(),
                                methodName);
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
    @Override
    public void processDeclassifiedEntityEvent(String         sourceName,
                                               String         originatorMetadataCollectionId,
                                               String         originatorServerName,
                                               String         originatorServerType,
                                               String         originatorOrganizationName,
                                               EntityDetail   entity,
                                               Classification originalClassification)
    {
        final String methodName = "processDeclassifiedEntityEvent";

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.DECLASSIFIED_ELEMENT_EVENT;

        log.debug("Processing declassified Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                originalClassification.getName(),
                                methodName);
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
        final String methodName = "processReclassifiedEntityEvent";

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.RECLASSIFIED_ELEMENT_EVENT;

        log.debug("Processing reclassified Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                originalClassification.getName(),
                                methodName);
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
    @Override
    public void processDeletedEntityEvent(String       sourceName,
                                          String       originatorMetadataCollectionId,
                                          String       originatorServerName,
                                          String       originatorServerType,
                                          String       originatorOrganizationName,
                                          EntityDetail entity)
    {
        final String methodName = "processDeletedEntityEvent";

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.DELETED_ELEMENT_EVENT;

        log.debug("Processing deleted Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                null,
                                methodName);
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
        final String methodName = "processDeletePurgedEntityEvent";

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.DELETED_ELEMENT_EVENT;

        log.debug("Processing delete-purge entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                null,
                                methodName);
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
        final String methodName = "processReIdentifiedEntityEvent";

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.UPDATED_ELEMENT_EVENT;

        log.debug("Processing re-identified Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                null,
                                methodName);
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
    @Override
    public void processReTypedEntityEvent(String         sourceName,
                                          String         originatorMetadataCollectionId,
                                          String         originatorServerName,
                                          String         originatorServerType,
                                          String         originatorOrganizationName,
                                          TypeDefSummary originalTypeDefSummary,
                                          EntityDetail   entity)
    {
        final String methodName = "processReTypedEntityEvent";

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.UPDATED_ELEMENT_EVENT;

        log.debug("Processing re-typed Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                null,
                                methodName);
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
        final String methodName = "processReHomedEntityEvent";

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.UPDATED_ELEMENT_EVENT;

        log.debug("Processing re-homed Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                null,
                                methodName);
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
        final String methodName = "processNewRelationshipEvent";

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.NEW_ELEMENT_EVENT;

        log.debug("Processing new relationship event from: " + sourceName);

        this.processRelationshipEvent(eventType,
                                      sourceName,
                                      originatorMetadataCollectionId,
                                      originatorServerName,
                                      originatorServerType,
                                      originatorOrganizationName,
                                      relationship,
                                      methodName);
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
        final String methodName = "processUpdatedRelationshipEvent";

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.UPDATED_ELEMENT_EVENT;

        log.debug("Processing updated relationship event from: " + sourceName);

        this.processRelationshipEvent(eventType,
                                      sourceName,
                                      originatorMetadataCollectionId,
                                      originatorServerName,
                                      originatorServerType,
                                      originatorOrganizationName,
                                      newRelationship,
                                      methodName);
    }


    /**
     * An existing relationship has been deleted.  This is a soft delete. This means it is still in the repository
     * but it is no longer returned on queries.
     * <p>
     * Details of the TypeDef are included with the relationship's unique id (guid) to ensure the right
     * relationship is deleted in the remote repositories.
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
        final String methodName = "processDeletedRelationshipEvent";

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.DELETED_ELEMENT_EVENT;

        log.debug("Processing deleted relationship event from: " + sourceName);

        this.processRelationshipEvent(eventType,
                                      sourceName,
                                      originatorMetadataCollectionId,
                                      originatorServerName,
                                      originatorServerType,
                                      originatorOrganizationName,
                                      relationship,
                                      methodName);
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
        final String methodName = "processUpdatedRelationshipEvent";

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.DELETED_ELEMENT_EVENT;

        log.debug("Processing delete-purge relationship event from: " + sourceName);

        this.processRelationshipEvent(eventType,
                                      sourceName,
                                      originatorMetadataCollectionId,
                                      originatorServerName,
                                      originatorServerType,
                                      originatorOrganizationName,
                                      relationship,
                                      methodName);
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
        final String methodName = "processReIdentifiedRelationshipEvent";

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.UPDATED_ELEMENT_EVENT;

        log.debug("Processing re-identified relationship event from: " + sourceName);

        this.processRelationshipEvent(eventType,
                                      sourceName,
                                      originatorMetadataCollectionId,
                                      originatorServerName,
                                      originatorServerType,
                                      originatorOrganizationName,
                                      relationship,
                                      methodName);
    }


    /**
     * An existing relationship has had its type changed.  Typically this action is taken to move a relationship's
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
        final String methodName = "processReTypedRelationshipEvent";

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.UPDATED_ELEMENT_EVENT;

        log.debug("Processing re-typed relationship event from: " + sourceName);

        this.processRelationshipEvent(eventType,
                                      sourceName,
                                      originatorMetadataCollectionId,
                                      originatorServerName,
                                      originatorServerType,
                                      originatorOrganizationName,
                                      relationship,
                                      methodName);
    }


    /**
     * An existing relationship has changed home repository.  This action is taken for example, if a repository
     * becomes permanently unavailable, or if the user community updating this relationship move to working
     * from a different repository in the open metadata repository cluster.
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
        final String methodName = "processReHomedRelationshipEvent";

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.UPDATED_ELEMENT_EVENT;

        log.debug("Processing re-homed relationship event from: " + sourceName);

        this.processRelationshipEvent(eventType,
                                      sourceName,
                                      originatorMetadataCollectionId,
                                      originatorServerName,
                                      originatorServerType,
                                      originatorOrganizationName,
                                      relationship,
                                      methodName);
    }
}
