/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.listener;

import org.odpi.openmetadata.commonservices.generichandlers.DataManagerOMASConverter;
import org.odpi.openmetadata.accessservices.datamanager.events.DataManagerOutboundEventType;
import org.odpi.openmetadata.accessservices.datamanager.ffdc.DataManagerAuditCode;
import org.odpi.openmetadata.accessservices.datamanager.outtopic.DataManagerOutTopicPublisher;
import org.odpi.openmetadata.accessservices.datamanager.server.DataManagerServicesInstance;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericHandler;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementStub;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListenerBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;


/**
 * DataManagerOMRSTopicListener received details of each OMRS event from the cohorts that the local server
 * is connected to.  It passes NEW_ENTITY_EVENTs to the publisher.
 */
public class DataManagerOMRSTopicListener extends OMRSTopicListenerBase
{
    private static final Logger log = LoggerFactory.getLogger(DataManagerOMRSTopicListener.class);

    private final List<String>                               supportedZones;
    private final DataManagerOutTopicPublisher               publisher;
    private final OMRSRepositoryHelper                       repositoryHelper;
    private final String                                     serverUserId;
    private final DataManagerOMASConverter<ElementStub>      converter;
    private final OpenMetadataAPIGenericHandler<ElementStub> genericHandler;


    /**
     * Initialize the topic listener.
     *
     * @param supportedZones list of zones for the visible assets
     * @param publisher this is the out topic publisher.
     * @param serverUserId this servers user id for accessing the repositories
     * @param auditLog logging destination
     * @param serviceName this is the full name of the service - used for error logging in base class
     * @param repositoryHelper repository helper
     * @param serverName this server
     * @param instance server instance
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
                                    EntityProxy                  entityProxy,
                                    String                       classificationName,
                                    String                       methodName)
    {
        final String entityGUIDParameterName = "entity.getGUID()";

        String instanceTypeName = null;

        if (entity != null)
        {
            instanceTypeName = this.getInstanceTypeName(sourceName,
                                                        originatorMetadataCollectionId,
                                                        originatorServerName,
                                                        originatorServerType,
                                                        originatorOrganizationName,
                                                        entity,
                                                        methodName);
        }
        else if (entityProxy != null)
        {
            instanceTypeName = this.getInstanceTypeName(sourceName,
                                                        originatorMetadataCollectionId,
                                                        originatorServerName,
                                                        originatorServerType,
                                                        originatorOrganizationName,
                                                        entityProxy,
                                                        methodName);
        }

        if (instanceTypeName != null)
        {
            try
            {
                Date effectiveTime = new Date();

                ElementStub elementStub;

                if (entity != null)
                {
                    elementStub = converter.getElementStub(ElementStub.class, entity, methodName);
                }
                else
                {
                    elementStub = converter.getElementStub(ElementStub.class, entityProxy, methodName);
                }

                if (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.REFERENCEABLE.typeName))
                {
                    if (entity != null)
                    {
                        genericHandler.validateAnchorEntity(serverUserId,
                                                            entity.getGUID(),
                                                            instanceTypeName,
                                                            entity,
                                                            entityGUIDParameterName,
                                                            false,
                                                            false,
                                                            false,
                                                            false,
                                                            supportedZones,
                                                            effectiveTime,
                                                            methodName);

                    }
                    else /* proxy */
                    {
                        genericHandler.validateAnchorEntity(serverUserId,
                                                            entityProxy.getGUID(),
                                                            entityGUIDParameterName,
                                                            instanceTypeName,
                                                            false,
                                                            false,
                                                            false,
                                                            false,
                                                            supportedZones,
                                                            effectiveTime,
                                                            methodName);
                    }

                    /*
                     * The event will publish if the entity is visible through this interface.
                     */
                    publisher.sendEntityEvent(eventType, elementStub.getGUID(), instanceTypeName, classificationName, elementStub);
                }
            }
            catch (UserNotAuthorizedException | InvalidParameterException error)
            {
                if (entity != null)
                {
                    auditLog.logMessage(methodName,
                                          DataManagerAuditCode.OUTBOUND_EVENT_EXCEPTION.getMessageDefinition(eventType.getEventTypeName(),
                                                                                                             entity.getGUID(),
                                                                                                             instanceTypeName,
                                                                                                             error.getClass().getName(),
                                                                                                             error.getMessage()));
                }
                else /* proxy */
                {
                    auditLog.logMessage(methodName,
                                          DataManagerAuditCode.OUTBOUND_EVENT_EXCEPTION.getMessageDefinition(eventType.getEventTypeName(),
                                                                                                             entityProxy.getGUID(),
                                                                                                             instanceTypeName,
                                                                                                             error.getClass().getName(),
                                                                                                             error.getMessage()));
                }
            }
            catch (Exception error)
            {
                if (entity != null)
                {
                    auditLog.logException(methodName,
                                          DataManagerAuditCode.OUTBOUND_EVENT_EXCEPTION.getMessageDefinition(eventType.getEventTypeName(),
                                                                                                             entity.getGUID(),
                                                                                                             instanceTypeName,
                                                                                                             error.getClass().getName(),
                                                                                                             error.getMessage()),
                                          error);
                }
                else /* proxy */
                {
                    auditLog.logException(methodName,
                                          DataManagerAuditCode.OUTBOUND_EVENT_EXCEPTION.getMessageDefinition(eventType.getEventTypeName(),
                                                                                                             entityProxy.getGUID(),
                                                                                                             instanceTypeName,
                                                                                                             error.getClass().getName(),
                                                                                                             error.getMessage()),
                                          error);
                }
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
    @SuppressWarnings(value="deprecation")
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

        Date effectiveTime = new Date();

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

                if ((repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.DATA_CONTENT_FOR_DATA_SET_RELATIONSHIP.typeName)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.ASSET_TO_SCHEMA_TYPE_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.SERVER_ENDPOINT_RELATIONSHIP.typeName)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.SEMANTIC_ASSIGNMENT.typeName)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.API_ENDPOINT_RELATIONSHIP.typeName)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeName)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.ASSET_TO_CONNECTION_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.CONNECTION_ENDPOINT_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.CONNECTION_CONNECTOR_TYPE_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.SCHEMA_QUERY_TARGET_RELATIONSHIP_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.LINKED_EXTERNAL_SCHEMA_TYPE_RELATIONSHIP_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.MAP_FROM_RELATIONSHIP_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.MAP_TO_RELATIONSHIP_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.API_HEADER_RELATIONSHIP_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.API_REQUEST_RELATIONSHIP_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.API_RESPONSE_RELATIONSHIP_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.API_OPERATIONS_RELATIONSHIP_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.GRAPH_EDGE_LINK_RELATIONSHIP.typeName)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.FOREIGN_KEY_RELATIONSHIP_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.NESTED_FILE_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.LINKED_FILE_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.FOLDER_HIERARCHY_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.LINKED_MEDIA_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.TOPIC_SUBSCRIBERS_TYPE_NAME)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.ASSOCIATED_LOG_RELATIONSHIP.typeName)) ||
                            (repositoryHelper.isTypeOf(sourceName, instanceTypeName, OpenMetadataType.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_NAME)))
                {
                    genericHandler.validateAnchorEntity(serverUserId,
                                                        relationship.getEntityOneProxy().getGUID(),
                                                        entityProxyOneGUIDParameterName,
                                                        OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                        false,
                                                        false,
                                                        false,
                                                        false,
                                                        supportedZones,
                                                        effectiveTime,
                                                        methodName);

                    genericHandler.validateAnchorEntity(serverUserId,
                                                        relationship.getEntityTwoProxy().getGUID(),
                                                        entityProxyTwoGUIDParameterName,
                                                        OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                        false,
                                                        false,
                                                        false,
                                                        false,
                                                        supportedZones,
                                                        effectiveTime,
                                                        methodName);

                    /*
                     * The event will publish if both entities are visible to the publisher
                     */
                    publisher.sendRelationshipEvent(eventType, relationship.getGUID(), instanceTypeName, relationshipElementStub, endOneElementStub, endTwoElementStub);
                }
            }
            catch (UserNotAuthorizedException | InvalidParameterException error)
            {
                /*
                 * InvalidParameterException is returned if an entity is not visible due to the zones.
                 * UserNotAuthorizedException means that the server's userId is not allowed to access the entity.
                 */
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

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.NEW_ELEMENT_CREATED;

        log.debug("Processing new Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                null,
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

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.ELEMENT_UPDATED;

        log.debug("Processing updated Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                newEntity,
                                null,
                                null,
                                methodName);
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
        final String methodName = "processClassifiedEntityEvent(proxy)";

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.ELEMENT_CLASSIFIED;

        log.debug("Processing classified Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                null,
                                entityProxy,
                                classification.getName(),
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
        final String methodName = "processClassifiedEntityEvent";

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.ELEMENT_CLASSIFIED;

        log.debug("Processing classified Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                null,
                                classification.getName(),
                                methodName);
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
        final String methodName = "processClassifiedEntityEvent";

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.ELEMENT_CLASSIFIED;

        log.debug("Processing classified Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                null,
                                null,
                                methodName);
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
        final String methodName = "processDeclassifiedEntityEvent(proxy)";

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.ELEMENT_DECLASSIFIED;

        log.debug("Processing declassified Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                null,
                                entityProxy,
                                null,
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
        final String methodName = "processDeclassifiedEntityEvent";

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.ELEMENT_DECLASSIFIED;

        log.debug("Processing declassified Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                null,
                                originalClassification.getName(),
                                methodName);
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
        final String methodName = "processDeclassifiedEntityEvent";

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.ELEMENT_DECLASSIFIED;

        log.debug("Processing declassified Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                null,
                                null,
                                methodName);
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
        final String methodName = "processReclassifiedEntityEvent(proxy)";

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.ELEMENT_RECLASSIFIED;

        log.debug("Processing reclassified Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                null,
                                entityProxy,
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
        final String methodName = "processReclassifiedEntityEvent";

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.ELEMENT_RECLASSIFIED;

        log.debug("Processing reclassified Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                null,
                                originalClassification.getName(),
                                methodName);
    }


    /**
     * An existing classification has been changed on an entity.
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
        final String methodName = "processReclassifiedEntityEvent";

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.ELEMENT_RECLASSIFIED;

        log.debug("Processing reclassified Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                null,
                                null,
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

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.ELEMENT_DELETED;

        log.debug("Processing deleted Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                null,
                                null,
                                methodName);
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
        final String methodName = "processDeletePurgedEntityEvent";

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.ELEMENT_DELETED;

        log.debug("Processing delete-purge entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                null,
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

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.ELEMENT_GUID_CHANGED;

        log.debug("Processing re-identified Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                null,
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

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.ELEMENT_TYPE_CHANGED;

        log.debug("Processing re-typed Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                null,
                                null,
                                methodName);
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
        final String methodName = "processReHomedEntityEvent";

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.ELEMENT_HOME_CHANGED;

        log.debug("Processing re-homed Entity event from: " + sourceName);

        this.processEntityEvent(eventType,
                                sourceName,
                                originatorMetadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                entity,
                                null,
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

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.NEW_ELEMENT_CREATED;

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

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.ELEMENT_UPDATED;

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

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.ELEMENT_DELETED;

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

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.ELEMENT_DELETED;

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

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.ELEMENT_GUID_CHANGED;

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

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.ELEMENT_TYPE_CHANGED;

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
        final String methodName = "processReHomedRelationshipEvent";

        final DataManagerOutboundEventType eventType = DataManagerOutboundEventType.ELEMENT_GUID_CHANGED;

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
