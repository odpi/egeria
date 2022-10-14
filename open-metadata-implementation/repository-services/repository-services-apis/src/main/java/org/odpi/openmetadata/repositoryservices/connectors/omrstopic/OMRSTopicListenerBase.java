/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.omrstopic;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.events.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * OMRSTopicListenerBase provides a base class for a topic listener, so it only needs to
 * override the methods for the events it cares about.
 */
public class OMRSTopicListenerBase implements OMRSTopicListener
{
    private static final Logger log = LoggerFactory.getLogger(OMRSTopicListenerBase.class);

    private final String   NULL_EVENT = "<null>";

    protected String   serviceName;
    protected AuditLog auditLog = null;


    /**
     * Name of the service that this is listening on behalf of.
     * 
     * @param serviceName name of service
     */
    public OMRSTopicListenerBase(String serviceName)
    {
        this.serviceName = serviceName;
    }


    /**
     * Name of the service that this is listening on behalf of.
     *
     * @param serviceName name of service
     * @param auditLog logging destination
     */
    public OMRSTopicListenerBase(String       serviceName,
                                 AuditLog     auditLog)
    {
        this.serviceName = serviceName;
        this.auditLog    = auditLog;
    }


    /**
     * Log an audit log message to record an unexpected exception.  We should never see this message.
     * It indicates a logic error in the service that threw the exception.
     *
     * @param event string version of the event
     * @param error exception
     * @param actionDescription calling activity
     */
    private void logUnexpectedException(String     event,
                                        Throwable  error,
                                        String     actionDescription)
    {
        if (auditLog != null)
        {
            auditLog.logException(actionDescription,
                                  OMRSAuditCode.UNEXPECTED_EXCEPTION_FROM_SERVICE_LISTENER.getMessageDefinition(serviceName,
                                                                                                                error.getClass().getName(),
                                                                                                                error.getMessage()),
                                  event,
                                  error);
        }
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
     * @param instance                       details of the instance from the event
     * @param actionDescription              description of overall processing
     * @return typeName
     */
    protected String getInstanceTypeName(String            sourceName,
                                         String            originatorMetadataCollectionId,
                                         String            originatorServerName,
                                         String            originatorServerType,
                                         String            originatorOrganizationName,
                                         InstanceHeader    instance,
                                         String            actionDescription)
    {
        String instanceTypeName = null;

        if (instance != null)
        {
            InstanceType instanceType = instance.getType();

            if (instanceType != null)
            {
                instanceTypeName = instanceType.getTypeDefName();
            }
        }

        if (instanceTypeName == null)
        {
            final String   unknownEventInsert = "<Unknown Value>";

            String         eventSourceName           = unknownEventInsert;
            String         eventMetadataCollectionId = unknownEventInsert;
            String         eventServerName           = unknownEventInsert;
            String         eventServerType           = unknownEventInsert;
            String         eventOrgName              = unknownEventInsert;
            String         eventInstanceString       = unknownEventInsert;

            if (instance != null)
            {
                eventInstanceString = instance.toString();
            }

            if (sourceName != null)
            {
                eventSourceName = sourceName;
            }

            if (originatorMetadataCollectionId != null)
            {
                eventMetadataCollectionId = originatorMetadataCollectionId;
            }

            if (originatorServerName != null)
            {
                eventServerName = originatorServerName;
            }

            if (originatorServerType != null)
            {
                eventServerType = originatorServerType;
            }

            if (originatorOrganizationName != null)
            {
                eventOrgName = originatorOrganizationName;
            }

            auditLog.logMessage(actionDescription,
                                OMRSAuditCode.BAD_EVENT_INSTANCE.getMessageDefinition(eventSourceName,
                                                                                      eventServerName,
                                                                                      eventServerType,
                                                                                      eventOrgName,
                                                                                      eventMetadataCollectionId,
                                                                                      eventInstanceString));
        }

        return instanceTypeName;
    }


    /**
     * Method to pass a Registry event received on topic.
     *
     * @param registryEvent inbound event
     */
    public void processRegistryEvent(OMRSRegistryEvent registryEvent)
    {
        final String actionDescription = "processRegistryEvent";

        try
        {
            log.debug("Processing registry event: " + registryEvent);

            if (registryEvent == null)
            {
                log.debug("Null registry event; ignoring event");
            }
            else /* process registry event */
            {
                OMRSRegistryEventType registryEventType       = registryEvent.getRegistryEventType();
                OMRSEventOriginator   registryEventOriginator = registryEvent.getEventOriginator();

                if ((registryEventType != null) && (registryEventOriginator != null))
                {
                    switch (registryEventType)
                    {
                        case REGISTRATION_EVENT:
                            this.processRegistrationEvent(serviceName,
                                                          registryEventOriginator.getMetadataCollectionId(),
                                                          registryEvent.getMetadataCollectionName(),
                                                          registryEventOriginator.getServerName(),
                                                          registryEventOriginator.getServerType(),
                                                          registryEventOriginator.getOrganizationName(),
                                                          registryEvent.getRegistrationTimestamp(),
                                                          registryEvent.getRemoteConnection());
                            break;

                        case RE_REGISTRATION_EVENT:
                            this.processReRegistrationEvent(serviceName,
                                                            registryEventOriginator.getMetadataCollectionId(),
                                                            registryEvent.getMetadataCollectionName(),
                                                            registryEventOriginator.getServerName(),
                                                            registryEventOriginator.getServerType(),
                                                            registryEventOriginator.getOrganizationName(),
                                                            registryEvent.getRegistrationTimestamp(),
                                                            registryEvent.getRemoteConnection());
                            break;

                        case REFRESH_REGISTRATION_REQUEST:
                            this.processRegistrationRefreshRequest(serviceName,
                                                                   registryEventOriginator.getServerName(),
                                                                   registryEventOriginator.getServerType(),
                                                                   registryEventOriginator.getOrganizationName());
                            break;

                        case UN_REGISTRATION_EVENT:
                            this.processUnRegistrationEvent(serviceName,
                                                            registryEventOriginator.getMetadataCollectionId(),
                                                            registryEvent.getMetadataCollectionName(),
                                                            registryEventOriginator.getServerName(),
                                                            registryEventOriginator.getServerType(),
                                                            registryEventOriginator.getOrganizationName());
                            break;

                        case REGISTRATION_ERROR_EVENT:
                            OMRSRegistryEventErrorCode errorCode = registryEvent.getErrorCode();

                            if (errorCode != null)
                            {
                                switch (errorCode)
                                {
                                    case BAD_REMOTE_CONNECTION:
                                        this.processBadConnectionEvent(serviceName,
                                                                       registryEventOriginator.getMetadataCollectionId(),
                                                                       registryEvent.getMetadataCollectionName(),
                                                                       registryEventOriginator.getServerName(),
                                                                       registryEventOriginator.getServerType(),
                                                                       registryEventOriginator.getOrganizationName(),
                                                                       registryEvent.getTargetMetadataCollectionId(),
                                                                       registryEvent.getTargetRemoteConnection(),
                                                                       registryEvent.getErrorMessage());
                                        break;

                                    case CONFLICTING_COLLECTION_ID:
                                        this.processConflictingCollectionIdEvent(serviceName,
                                                                                 registryEventOriginator.getMetadataCollectionId(),
                                                                                 registryEvent.getMetadataCollectionName(),
                                                                                 registryEventOriginator.getServerName(),
                                                                                 registryEventOriginator.getServerType(),
                                                                                 registryEventOriginator.getOrganizationName(),
                                                                                 registryEvent.getTargetMetadataCollectionId(),
                                                                                 registryEvent.getErrorMessage());
                                        break;

                                    default:
                                        log.debug("Unknown registry event error code; ignoring event");
                                        break;
                                }
                            }
                            else
                            {
                                log.debug("Null registry event error code, ignoring event");
                            }
                            break;

                        default:
                            /*
                             * New type of registry event that this server does not understand ignore it
                             */
                            log.debug("Unknown registry event: " + registryEvent);
                            break;
                    }
                }
                else
                {
                    log.debug("Ignored registry event: " + registryEvent);
                }
            }
        }
        catch (Exception error)
        {
            String eventString = NULL_EVENT;

            if (registryEvent != null)
            {
                eventString = registryEvent.toString();
            }

            this.logUnexpectedException(eventString, error, actionDescription);
        }
    }


    /**
     * Method to pass a TypeDef event received on topic.
     *
     * @param typeDefEvent inbound event
     */
    public void processTypeDefEvent(OMRSTypeDefEvent typeDefEvent)
    {
        final String actionDescription = "processTypeDefEvent";

        try
        {
            log.debug("Processing typeDef event: " + typeDefEvent);

            OMRSTypeDefEventType typeDefEventType       = typeDefEvent.getTypeDefEventType();
            OMRSEventOriginator  typeDefEventOriginator = typeDefEvent.getEventOriginator();

            if ((typeDefEventType != null) && (typeDefEventOriginator != null))
            {
                switch (typeDefEventType)
                {
                    case NEW_TYPEDEF_EVENT:
                        this.processNewTypeDefEvent(serviceName,
                                                    typeDefEventOriginator.getMetadataCollectionId(),
                                                    typeDefEventOriginator.getServerName(),
                                                    typeDefEventOriginator.getServerType(),
                                                    typeDefEventOriginator.getOrganizationName(),
                                                    typeDefEvent.getTypeDef());
                        break;

                    case NEW_ATTRIBUTE_TYPEDEF_EVENT:
                        this.processNewAttributeTypeDefEvent(serviceName,
                                                             typeDefEventOriginator.getMetadataCollectionId(),
                                                             typeDefEventOriginator.getServerName(),
                                                             typeDefEventOriginator.getServerType(),
                                                             typeDefEventOriginator.getOrganizationName(),
                                                             typeDefEvent.getAttributeTypeDef());
                        break;

                    case UPDATED_TYPEDEF_EVENT:
                        this.processUpdatedTypeDefEvent(serviceName,
                                                        typeDefEventOriginator.getMetadataCollectionId(),
                                                        typeDefEventOriginator.getServerName(),
                                                        typeDefEventOriginator.getServerType(),
                                                        typeDefEventOriginator.getOrganizationName(),
                                                        typeDefEvent.getTypeDefPatch());
                        break;

                    case DELETED_TYPEDEF_EVENT:
                        this.processDeletedTypeDefEvent(serviceName,
                                                        typeDefEventOriginator.getMetadataCollectionId(),
                                                        typeDefEventOriginator.getServerName(),
                                                        typeDefEventOriginator.getServerType(),
                                                        typeDefEventOriginator.getOrganizationName(),
                                                        typeDefEvent.getTypeDefGUID(),
                                                        typeDefEvent.getTypeDefName());
                        break;

                    case DELETED_ATTRIBUTE_TYPEDEF_EVENT:
                        this.processDeletedAttributeTypeDefEvent(serviceName,
                                                                 typeDefEventOriginator.getMetadataCollectionId(),
                                                                 typeDefEventOriginator.getServerName(),
                                                                 typeDefEventOriginator.getServerType(),
                                                                 typeDefEventOriginator.getOrganizationName(),
                                                                 typeDefEvent.getTypeDefGUID(),
                                                                 typeDefEvent.getTypeDefName());
                        break;

                    case RE_IDENTIFIED_TYPEDEF_EVENT:
                        this.processReIdentifiedTypeDefEvent(serviceName,
                                                             typeDefEventOriginator.getMetadataCollectionId(),
                                                             typeDefEventOriginator.getServerName(),
                                                             typeDefEventOriginator.getServerType(),
                                                             typeDefEventOriginator.getOrganizationName(),
                                                             typeDefEvent.getOriginalTypeDefSummary(),
                                                             typeDefEvent.getTypeDef());
                        break;

                    case RE_IDENTIFIED_ATTRIBUTE_TYPEDEF_EVENT:
                        this.processReIdentifiedAttributeTypeDefEvent(serviceName,
                                                                      typeDefEventOriginator.getMetadataCollectionId(),
                                                                      typeDefEventOriginator.getServerName(),
                                                                      typeDefEventOriginator.getServerType(),
                                                                      typeDefEventOriginator.getOrganizationName(),
                                                                      typeDefEvent.getOriginalAttributeTypeDef(),
                                                                      typeDefEvent.getAttributeTypeDef());

                        break;

                    case TYPEDEF_ERROR_EVENT:
                        OMRSTypeDefEventErrorCode errorCode = typeDefEvent.getErrorCode();

                        if (errorCode != null)
                        {
                            switch (errorCode)
                            {
                                case CONFLICTING_TYPEDEFS:
                                    this.processTypeDefConflictEvent(serviceName,
                                                                     typeDefEventOriginator.getMetadataCollectionId(),
                                                                     typeDefEventOriginator.getServerName(),
                                                                     typeDefEventOriginator.getServerType(),
                                                                     typeDefEventOriginator.getOrganizationName(),
                                                                     typeDefEvent.getOriginalTypeDefSummary(),
                                                                     typeDefEvent.getOtherMetadataCollectionId(),
                                                                     typeDefEvent.getOtherTypeDefSummary(),
                                                                     typeDefEvent.getErrorMessage());
                                    break;

                                case CONFLICTING_ATTRIBUTE_TYPEDEFS:
                                    this.processAttributeTypeDefConflictEvent(serviceName,
                                                                              typeDefEventOriginator.getMetadataCollectionId(),
                                                                              typeDefEventOriginator.getServerName(),
                                                                              typeDefEventOriginator.getServerType(),
                                                                              typeDefEventOriginator.getOrganizationName(),
                                                                              typeDefEvent.getOriginalAttributeTypeDef(),
                                                                              typeDefEvent.getOtherMetadataCollectionId(),
                                                                              typeDefEvent.getOtherAttributeTypeDef(),
                                                                              typeDefEvent.getErrorMessage());
                                    break;

                                case TYPEDEF_PATCH_MISMATCH:
                                    this.processTypeDefPatchMismatchEvent(serviceName,
                                                                          typeDefEventOriginator.getMetadataCollectionId(),
                                                                          typeDefEventOriginator.getServerName(),
                                                                          typeDefEventOriginator.getServerType(),
                                                                          typeDefEventOriginator.getOrganizationName(),
                                                                          typeDefEvent.getTargetMetadataCollectionId(),
                                                                          typeDefEvent.getTargetTypeDefSummary(),
                                                                          typeDefEvent.getOtherTypeDef(),
                                                                          typeDefEvent.getErrorMessage());
                                    break;

                                default:
                                    log.debug("Unknown TypeDef event error code; ignoring event");
                                    break;
                            }
                        }
                        else
                        {
                            log.debug("Ignored TypeDef event; null error code");
                        }
                        break;

                    default:
                        log.debug("Ignored TypeDef event; unknown type");
                        break;
                }
            }
        }
        catch (Exception error)
        {
            String eventString = NULL_EVENT;

            if (typeDefEvent != null)
            {
                eventString = typeDefEvent.toString();
            }

            this.logUnexpectedException(eventString, error, actionDescription);
        }
    }


    /**
     * Method to pass an Instance event received on topic.
     *
     * @param instanceEvent inbound event
     */
    public void processInstanceEvent(OMRSInstanceEvent instanceEvent)
    {
        final String actionDescription = "processInstanceEvent";

        try
        {
            OMRSInstanceEventType instanceEventType       = instanceEvent.getInstanceEventType();
            OMRSEventOriginator   instanceEventOriginator = instanceEvent.getEventOriginator();

            log.debug("Processing instance event: " + instanceEvent);


            if ((instanceEventType != null) && (instanceEventOriginator != null))
            {
                switch (instanceEventType)
                {
                    case NEW_ENTITY_EVENT:
                        this.processNewEntityEvent(serviceName,
                                                   instanceEventOriginator.getMetadataCollectionId(),
                                                   instanceEventOriginator.getServerName(),
                                                   instanceEventOriginator.getServerType(),
                                                   instanceEventOriginator.getOrganizationName(),
                                                   instanceEvent.getEntity());
                        break;

                    case UPDATED_ENTITY_EVENT:
                        this.processUpdatedEntityEvent(serviceName,
                                                       instanceEventOriginator.getMetadataCollectionId(),
                                                       instanceEventOriginator.getServerName(),
                                                       instanceEventOriginator.getServerType(),
                                                       instanceEventOriginator.getOrganizationName(),
                                                       instanceEvent.getOriginalEntity(),
                                                       instanceEvent.getEntity());
                        break;

                    case CLASSIFIED_ENTITY_EVENT:
                        if (instanceEvent.getEntity() != null)
                        {
                            this.processClassifiedEntityEvent(serviceName,
                                                              instanceEventOriginator.getMetadataCollectionId(),
                                                              instanceEventOriginator.getServerName(),
                                                              instanceEventOriginator.getServerType(),
                                                              instanceEventOriginator.getOrganizationName(),
                                                              instanceEvent.getEntity(),
                                                              instanceEvent.getClassification());
                        }
                        else if (instanceEvent.getEntityProxy() != null)
                        {
                            this.processClassifiedEntityEvent(serviceName,
                                                              instanceEventOriginator.getMetadataCollectionId(),
                                                              instanceEventOriginator.getServerName(),
                                                              instanceEventOriginator.getServerType(),
                                                              instanceEventOriginator.getOrganizationName(),
                                                              instanceEvent.getEntityProxy(),
                                                              instanceEvent.getClassification());
                        }
                        break;

                    case RECLASSIFIED_ENTITY_EVENT:
                        if (instanceEvent.getEntity() != null)
                        {
                            this.processReclassifiedEntityEvent(serviceName,
                                                                instanceEventOriginator.getMetadataCollectionId(),
                                                                instanceEventOriginator.getServerName(),
                                                                instanceEventOriginator.getServerType(),
                                                                instanceEventOriginator.getOrganizationName(),
                                                                instanceEvent.getEntity(),
                                                                instanceEvent.getOriginalClassification(),
                                                                instanceEvent.getClassification());
                        }
                        else if (instanceEvent.getEntityProxy() != null)
                        {
                            this.processReclassifiedEntityEvent(serviceName,
                                                                instanceEventOriginator.getMetadataCollectionId(),
                                                                instanceEventOriginator.getServerName(),
                                                                instanceEventOriginator.getServerType(),
                                                                instanceEventOriginator.getOrganizationName(),
                                                                instanceEvent.getEntityProxy(),
                                                                instanceEvent.getOriginalClassification(),
                                                                instanceEvent.getClassification());
                        }
                        break;

                    case DECLASSIFIED_ENTITY_EVENT:
                        if (instanceEvent.getEntity() != null)
                        {
                            this.processDeclassifiedEntityEvent(serviceName,
                                                                instanceEventOriginator.getMetadataCollectionId(),
                                                                instanceEventOriginator.getServerName(),
                                                                instanceEventOriginator.getServerType(),
                                                                instanceEventOriginator.getOrganizationName(),
                                                                instanceEvent.getEntity(),
                                                                instanceEvent.getOriginalClassification());
                        }
                        else if (instanceEvent.getEntityProxy() != null)
                        {
                            this.processDeclassifiedEntityEvent(serviceName,
                                                                instanceEventOriginator.getMetadataCollectionId(),
                                                                instanceEventOriginator.getServerName(),
                                                                instanceEventOriginator.getServerType(),
                                                                instanceEventOriginator.getOrganizationName(),
                                                                instanceEvent.getEntityProxy(),
                                                                instanceEvent.getOriginalClassification());
                        }
                        break;

                    case DELETED_ENTITY_EVENT:
                        this.processDeletedEntityEvent(serviceName,
                                                       instanceEventOriginator.getMetadataCollectionId(),
                                                       instanceEventOriginator.getServerName(),
                                                       instanceEventOriginator.getServerType(),
                                                       instanceEventOriginator.getOrganizationName(),
                                                       instanceEvent.getEntity());
                        break;

                    case PURGED_ENTITY_EVENT:
                        this.processPurgedEntityEvent(serviceName,
                                                      instanceEventOriginator.getMetadataCollectionId(),
                                                      instanceEventOriginator.getServerName(),
                                                      instanceEventOriginator.getServerType(),
                                                      instanceEventOriginator.getOrganizationName(),
                                                      instanceEvent.getTypeDefGUID(),
                                                      instanceEvent.getTypeDefName(),
                                                      instanceEvent.getInstanceGUID());
                        break;

                    case DELETE_PURGED_ENTITY_EVENT:
                        this.processDeletePurgedEntityEvent(serviceName,
                                                            instanceEventOriginator.getMetadataCollectionId(),
                                                            instanceEventOriginator.getServerName(),
                                                            instanceEventOriginator.getServerType(),
                                                            instanceEventOriginator.getOrganizationName(),
                                                            instanceEvent.getEntity());
                        break;

                    case UNDONE_ENTITY_EVENT:
                        this.processUndoneEntityEvent(serviceName,
                                                      instanceEventOriginator.getMetadataCollectionId(),
                                                      instanceEventOriginator.getServerName(),
                                                      instanceEventOriginator.getServerType(),
                                                      instanceEventOriginator.getOrganizationName(),
                                                      instanceEvent.getEntity());
                        break;

                    case RESTORED_ENTITY_EVENT:
                        this.processRestoredEntityEvent(serviceName,
                                                        instanceEventOriginator.getMetadataCollectionId(),
                                                        instanceEventOriginator.getServerName(),
                                                        instanceEventOriginator.getServerType(),
                                                        instanceEventOriginator.getOrganizationName(),
                                                        instanceEvent.getEntity());
                        break;

                    case REFRESH_ENTITY_REQUEST:
                        this.processRefreshEntityRequested(serviceName,
                                                           instanceEventOriginator.getMetadataCollectionId(),
                                                           instanceEventOriginator.getServerName(),
                                                           instanceEventOriginator.getServerType(),
                                                           instanceEventOriginator.getOrganizationName(),
                                                           instanceEvent.getTypeDefGUID(),
                                                           instanceEvent.getTypeDefName(),
                                                           instanceEvent.getInstanceGUID(),
                                                           instanceEvent.getHomeMetadataCollectionId());
                        break;

                    case REFRESHED_ENTITY_EVENT:
                        this.processRefreshEntityEvent(serviceName,
                                                       instanceEventOriginator.getMetadataCollectionId(),
                                                       instanceEventOriginator.getServerName(),
                                                       instanceEventOriginator.getServerType(),
                                                       instanceEventOriginator.getOrganizationName(),
                                                       instanceEvent.getEntity());
                        break;

                    case RE_HOMED_ENTITY_EVENT:
                        this.processReHomedEntityEvent(serviceName,
                                                       instanceEventOriginator.getMetadataCollectionId(),
                                                       instanceEventOriginator.getServerName(),
                                                       instanceEventOriginator.getServerType(),
                                                       instanceEventOriginator.getOrganizationName(),
                                                       instanceEvent.getOriginalHomeMetadataCollectionId(),
                                                       instanceEvent.getEntity());
                        break;

                    case RETYPED_ENTITY_EVENT:
                        this.processReTypedEntityEvent(serviceName,
                                                       instanceEventOriginator.getMetadataCollectionId(),
                                                       instanceEventOriginator.getServerName(),
                                                       instanceEventOriginator.getServerType(),
                                                       instanceEventOriginator.getOrganizationName(),
                                                       instanceEvent.getOriginalTypeDefSummary(),
                                                       instanceEvent.getEntity());
                        break;

                    case RE_IDENTIFIED_ENTITY_EVENT:
                        this.processReIdentifiedEntityEvent(serviceName,
                                                            instanceEventOriginator.getMetadataCollectionId(),
                                                            instanceEventOriginator.getServerName(),
                                                            instanceEventOriginator.getServerType(),
                                                            instanceEventOriginator.getOrganizationName(),
                                                            instanceEvent.getOriginalInstanceGUID(),
                                                            instanceEvent.getEntity());
                        break;

                    case NEW_RELATIONSHIP_EVENT:
                        this.processNewRelationshipEvent(serviceName,
                                                         instanceEventOriginator.getMetadataCollectionId(),
                                                         instanceEventOriginator.getServerName(),
                                                         instanceEventOriginator.getServerType(),
                                                         instanceEventOriginator.getOrganizationName(),
                                                         instanceEvent.getRelationship());
                        break;

                    case UPDATED_RELATIONSHIP_EVENT:
                        this.processUpdatedRelationshipEvent(serviceName,
                                                             instanceEventOriginator.getMetadataCollectionId(),
                                                             instanceEventOriginator.getServerName(),
                                                             instanceEventOriginator.getServerType(),
                                                             instanceEventOriginator.getOrganizationName(),
                                                             instanceEvent.getOriginalRelationship(),
                                                             instanceEvent.getRelationship());
                        break;

                    case UNDONE_RELATIONSHIP_EVENT:
                        this.processUndoneRelationshipEvent(serviceName,
                                                            instanceEventOriginator.getMetadataCollectionId(),
                                                            instanceEventOriginator.getServerName(),
                                                            instanceEventOriginator.getServerType(),
                                                            instanceEventOriginator.getOrganizationName(),
                                                            instanceEvent.getRelationship());
                        break;

                    case DELETED_RELATIONSHIP_EVENT:
                        this.processDeletedRelationshipEvent(serviceName,
                                                             instanceEventOriginator.getMetadataCollectionId(),
                                                             instanceEventOriginator.getServerName(),
                                                             instanceEventOriginator.getServerType(),
                                                             instanceEventOriginator.getOrganizationName(),
                                                             instanceEvent.getRelationship());
                        break;

                    case PURGED_RELATIONSHIP_EVENT:
                        this.processPurgedRelationshipEvent(serviceName,
                                                            instanceEventOriginator.getMetadataCollectionId(),
                                                            instanceEventOriginator.getServerName(),
                                                            instanceEventOriginator.getServerType(),
                                                            instanceEventOriginator.getOrganizationName(),
                                                            instanceEvent.getTypeDefGUID(),
                                                            instanceEvent.getTypeDefName(),
                                                            instanceEvent.getInstanceGUID());
                        break;

                    case DELETE_PURGED_RELATIONSHIP_EVENT:
                        this.processDeletePurgedRelationshipEvent(serviceName,
                                                                  instanceEventOriginator.getMetadataCollectionId(),
                                                                  instanceEventOriginator.getServerName(),
                                                                  instanceEventOriginator.getServerType(),
                                                                  instanceEventOriginator.getOrganizationName(),
                                                                  instanceEvent.getRelationship());
                        break;

                    case RESTORED_RELATIONSHIP_EVENT:
                        this.processRestoredRelationshipEvent(serviceName,
                                                              instanceEventOriginator.getMetadataCollectionId(),
                                                              instanceEventOriginator.getServerName(),
                                                              instanceEventOriginator.getServerType(),
                                                              instanceEventOriginator.getOrganizationName(),
                                                              instanceEvent.getRelationship());
                        break;

                    case REFRESH_RELATIONSHIP_REQUEST:
                        this.processRefreshRelationshipRequest(serviceName,
                                                               instanceEventOriginator.getMetadataCollectionId(),
                                                               instanceEventOriginator.getServerName(),
                                                               instanceEventOriginator.getServerType(),
                                                               instanceEventOriginator.getOrganizationName(),
                                                               instanceEvent.getTypeDefGUID(),
                                                               instanceEvent.getTypeDefName(),
                                                               instanceEvent.getInstanceGUID(),
                                                               instanceEvent.getHomeMetadataCollectionId());
                        break;

                    case REFRESHED_RELATIONSHIP_EVENT:
                        this.processRefreshRelationshipEvent(serviceName,
                                                             instanceEventOriginator.getMetadataCollectionId(),
                                                             instanceEventOriginator.getServerName(),
                                                             instanceEventOriginator.getServerType(),
                                                             instanceEventOriginator.getOrganizationName(),
                                                             instanceEvent.getRelationship());
                        break;

                    case RE_IDENTIFIED_RELATIONSHIP_EVENT:
                        this.processReIdentifiedRelationshipEvent(serviceName,
                                                                  instanceEventOriginator.getMetadataCollectionId(),
                                                                  instanceEventOriginator.getServerName(),
                                                                  instanceEventOriginator.getServerType(),
                                                                  instanceEventOriginator.getOrganizationName(),
                                                                  instanceEvent.getOriginalInstanceGUID(),
                                                                  instanceEvent.getRelationship());
                        break;

                    case RE_HOMED_RELATIONSHIP_EVENT:
                        this.processReHomedRelationshipEvent(serviceName,
                                                             instanceEventOriginator.getMetadataCollectionId(),
                                                             instanceEventOriginator.getServerName(),
                                                             instanceEventOriginator.getServerType(),
                                                             instanceEventOriginator.getOrganizationName(),
                                                             instanceEvent.getOriginalHomeMetadataCollectionId(),
                                                             instanceEvent.getRelationship());
                        break;

                    case RETYPED_RELATIONSHIP_EVENT:
                        this.processReTypedRelationshipEvent(serviceName,
                                                             instanceEventOriginator.getMetadataCollectionId(),
                                                             instanceEventOriginator.getServerName(),
                                                             instanceEventOriginator.getServerType(),
                                                             instanceEventOriginator.getOrganizationName(),
                                                             instanceEvent.getOriginalTypeDefSummary(),
                                                             instanceEvent.getRelationship());
                        break;
                    case BATCH_INSTANCES_EVENT:
                        this.processInstanceBatchEvent(serviceName,
                                                       instanceEventOriginator.getMetadataCollectionId(),
                                                       instanceEventOriginator.getServerName(),
                                                       instanceEventOriginator.getServerType(),
                                                       instanceEventOriginator.getOrganizationName(),
                                                       instanceEvent.getInstanceBatch());
                        break;
                    case INSTANCE_ERROR_EVENT:
                        OMRSInstanceEventErrorCode errorCode = instanceEvent.getErrorCode();

                        if (errorCode != null)
                        {
                            switch (errorCode)
                            {
                                case CONFLICTING_INSTANCES:
                                    this.processConflictingInstancesEvent(serviceName,
                                                                          instanceEventOriginator.getMetadataCollectionId(),
                                                                          instanceEventOriginator.getServerName(),
                                                                          instanceEventOriginator.getServerType(),
                                                                          instanceEventOriginator.getOrganizationName(),
                                                                          instanceEvent.getTargetMetadataCollectionId(),
                                                                          instanceEvent.getTargetTypeDefSummary(),
                                                                          instanceEvent.getTargetInstanceGUID(),
                                                                          instanceEvent.getOtherMetadataCollectionId(),
                                                                          instanceEvent.getOtherOrigin(),
                                                                          instanceEvent.getOtherTypeDefSummary(),
                                                                          instanceEvent.getOtherInstanceGUID(),
                                                                          instanceEvent.getErrorMessage());
                                    break;

                                case CONFLICTING_TYPE:
                                    this.processConflictingTypeEvent(serviceName,
                                                                     instanceEventOriginator.getMetadataCollectionId(),
                                                                     instanceEventOriginator.getServerName(),
                                                                     instanceEventOriginator.getServerType(),
                                                                     instanceEventOriginator.getOrganizationName(),
                                                                     instanceEvent.getTargetMetadataCollectionId(),
                                                                     instanceEvent.getTargetTypeDefSummary(),
                                                                     instanceEvent.getTargetInstanceGUID(),
                                                                     instanceEvent.getOtherTypeDefSummary(),
                                                                     instanceEvent.getErrorMessage());
                                    break;

                                default:
                                    log.debug("Unknown instance event error code, ignoring event");
                                    break;
                            }
                        }
                        else
                        {
                            log.debug("Ignored Instance event, null error code");
                        }
                        break;

                    default:
                        log.debug("Ignored Instance event, unknown type");
                        break;
                }
            }
            else
            {
                log.debug("Ignored instance event, null type");
            }
        }
        catch (Exception error)
        {
            String eventString = NULL_EVENT;

            if (instanceEvent != null)
            {
                eventString = instanceEvent.toString();
            }

            this.logUnexpectedException(eventString, error, actionDescription);
        }
    }


    /*
     * Registry events
     */

    /**
     * Introduces a new server/repository to the metadata repository cohort.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param metadataCollectionId  unique identifier of metadata collection of originator.
     * @param metadataCollectionName  display name of metadata collection of originator.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param registrationTimestamp  the time that the server/repository issued the registration request.
     * @param remoteConnection  the Connection properties for the connector used to call the registering server.
     */
    @SuppressWarnings(value = "unused")
    protected void processRegistrationEvent(String     sourceName,
                                            String     metadataCollectionId,
                                            String     metadataCollectionName,
                                            String     originatorServerName,
                                            String     originatorServerType,
                                            String     originatorOrganizationName,
                                            Date       registrationTimestamp,
                                            Connection remoteConnection)
    {
        log.debug("Processing registration event from: " + sourceName);
    }


    /**
     * Requests that the other servers in the cohort send re-registration events.
     *
     * @param sourceName                 name of the source of the event.  It may be the cohort name for incoming events or the
     *                                   local repository, or event mapper name.
     * @param originatorServerName       name of the server that the event came from.
     * @param originatorServerType       type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     */
    @SuppressWarnings(value = "unused")
    protected void processRegistrationRefreshRequest(String sourceName,
                                                     String originatorServerName,
                                                     String originatorServerType,
                                                     String originatorOrganizationName)
    {
        log.debug("Processing registration refresh event from: " + sourceName);
    }


    /**
     * Refreshes the other servers in the cohort with the originating server's registration.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param metadataCollectionId  unique identifier of metadata collection of originator.
     * @param metadataCollectionName  display name of metadata collection of originator.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param registrationTimestamp  the time that the server/repository first registered with the cohort.
     * @param remoteConnection  the Connection properties for the connector used to call the registering server.
     */
    @SuppressWarnings(value = "unused")
    protected void processReRegistrationEvent(String     sourceName,
                                              String     metadataCollectionId,
                                              String     metadataCollectionName,
                                              String     originatorServerName,
                                              String     originatorServerType,
                                              String     originatorOrganizationName,
                                              Date       registrationTimestamp,
                                              Connection remoteConnection)
    {
        log.debug("Processing re-registration event from: " + sourceName);
    }


    /**
     * A server/repository is being removed from the metadata repository cohort.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param metadataCollectionId  unique identifier of metadata collection of originator.
     * @param metadataCollectionName  display name of metadata collection of originator.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     */
    @SuppressWarnings(value = "unused")
    protected void processUnRegistrationEvent(String sourceName,
                                              String metadataCollectionId,
                                              String metadataCollectionName,
                                              String originatorServerName,
                                              String originatorServerType,
                                              String originatorOrganizationName)
    {
        log.debug("Processing unregistration event from: " + sourceName);
    }


    /**
     * There is more than one member of the open metadata repository cohort that is using the same metadata
     * collection id.  This means that their metadata instances can be updated in more than one server and there
     * is a potential for data integrity issues.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier of metadata collection of originator.
     * @param originatorMetadataCollectionName  display name of metadata collection of originator.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param conflictingMetadataCollectionId  unique identifier for the metadata collection that is registering with the cohort.
     * @param errorMessage  details of the conflict
     */
    @SuppressWarnings(value = "unused")
    protected void processConflictingCollectionIdEvent(String sourceName,
                                                       String originatorMetadataCollectionId,
                                                       String originatorMetadataCollectionName,
                                                       String originatorServerName,
                                                       String originatorServerType,
                                                       String originatorOrganizationName,
                                                       String conflictingMetadataCollectionId,
                                                       String errorMessage)
    {
        log.debug("Processing conflicting collection id event from: " + sourceName);
    }


    /**
     * A connection to one of the members of the open metadata repository cohort is not usable by one of the members.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier of metadata collection of originator.
     * @param originatorMetadataCollectionName  display name of metadata collection of originator.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param targetMetadataCollectionId  id for the repository with the bad remote connection.
     * @param remoteConnection  the Connection properties for the connector used to call the registering server.
     * @param errorMessage  details of the error that occurs when the connection is used.
     */
    @SuppressWarnings(value = "unused")
    protected void processBadConnectionEvent(String     sourceName,
                                             String     originatorMetadataCollectionId,
                                             String     originatorMetadataCollectionName,
                                             String     originatorServerName,
                                             String     originatorServerType,
                                             String     originatorOrganizationName,
                                             String     targetMetadataCollectionId,
                                             Connection remoteConnection,
                                             String     errorMessage)
    {
        log.debug("Processing bad connection event from: " + sourceName);
    }


    /*
     * TypeDef events
     */


    /**
     * A new TypeDef has been defined.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param typeDef details of the new TypeDef
     */
    @SuppressWarnings(value = "unused")
    public void processNewTypeDefEvent(String       sourceName,
                                       String       originatorMetadataCollectionId,
                                       String       originatorServerName,
                                       String       originatorServerType,
                                       String       originatorOrganizationName,
                                       TypeDef typeDef)
    {
        log.debug("Processing new TypeDef event from: " + sourceName);
    }


    /**
     * A new AttributeTypeDef has been defined in an open metadata repository.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param attributeTypeDef details of the new AttributeTypeDef.
     */
    @SuppressWarnings(value = "unused")
    public void processNewAttributeTypeDefEvent(String           sourceName,
                                                String           originatorMetadataCollectionId,
                                                String           originatorServerName,
                                                String           originatorServerType,
                                                String           originatorOrganizationName,
                                                AttributeTypeDef attributeTypeDef)
    {
        log.debug("Processing new AttributeTypeDef event from: " + sourceName);
    }


    /**
     * An existing TypeDef has been updated.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param typeDefPatch details of the new version of the TypeDef
     */
    @SuppressWarnings(value = "unused")
    public void processUpdatedTypeDefEvent(String       sourceName,
                                           String       originatorMetadataCollectionId,
                                           String       originatorServerName,
                                           String       originatorServerType,
                                           String       originatorOrganizationName,
                                           TypeDefPatch typeDefPatch)
    {
        log.debug("Processing updated TypeDef event from: " + sourceName);

    }


    /**
     * An existing TypeDef has been deleted.  Both the name and the GUID are provided to ensure the right TypeDef is
     * deleted in remote repositories.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param typeDefGUID unique identifier of the TypeDef
     * @param typeDefName unique name of the TypeDef
     */
    @SuppressWarnings(value = "unused")
    public void processDeletedTypeDefEvent(String       sourceName,
                                           String       originatorMetadataCollectionId,
                                           String       originatorServerName,
                                           String       originatorServerType,
                                           String       originatorOrganizationName,
                                           String       typeDefGUID,
                                           String       typeDefName)
    {
        log.debug("Processing deleted TypeDef event from: " + sourceName);
    }


    /**
     * An existing AttributeTypeDef has been deleted in an open metadata repository.  Both the name and the
     * GUID are provided to ensure the right AttributeTypeDef is deleted in other cohort member repositories.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param attributeTypeDefGUID unique identifier of the AttributeTypeDef
     * @param attributeTypeDefName unique name of the AttributeTypeDef
     */
    @SuppressWarnings(value = "unused")
    public void processDeletedAttributeTypeDefEvent(String      sourceName,
                                                    String      originatorMetadataCollectionId,
                                                    String      originatorServerName,
                                                    String      originatorServerType,
                                                    String      originatorOrganizationName,
                                                    String      attributeTypeDefGUID,
                                                    String      attributeTypeDefName)
    {
        log.debug("Processing deleted AttributeTypeDef event from: " + sourceName);
    }


    /**
     * The guid or name of an existing TypeDef has been changed to a new value.  This is used if two different
     * Typedefs are discovered to have either the same guid or, most likely, the same name.  This type of conflict
     * is rare but typically occurs when a new repository joins the cohort.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param originalTypeDefSummary the details for the original TypeDef.
     * @param typeDef updated TypeDef with new identifiers
     */
    @SuppressWarnings(value = "unused")
    public void processReIdentifiedTypeDefEvent(String         sourceName,
                                                String         originatorMetadataCollectionId,
                                                String         originatorServerName,
                                                String         originatorServerType,
                                                String         originatorOrganizationName,
                                                TypeDefSummary originalTypeDefSummary,
                                                TypeDef        typeDef)
    {
        log.debug("Processing re-identified TypeDef event from: " + sourceName);
    }


    /**
     * Process an event that changes either the name or guid of an AttributeTypeDef.
     * It is resolving a Conflicting AttributeTypeDef Error.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param originalAttributeTypeDef description of original AttributeTypeDef
     * @param attributeTypeDef updated AttributeTypeDef with new identifiers inside.
     */
    @SuppressWarnings(value = "unused")
    public void processReIdentifiedAttributeTypeDefEvent(String           sourceName,
                                                         String           originatorMetadataCollectionId,
                                                         String           originatorServerName,
                                                         String           originatorServerType,
                                                         String           originatorOrganizationName,
                                                         AttributeTypeDef originalAttributeTypeDef,
                                                         AttributeTypeDef attributeTypeDef)
    {
        log.debug("Processing re-identified AttributeTypeDef event from: " + sourceName);
    }


    /**
     * Process a detected conflict in type definitions (TypeDefs) used in the cohort.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param originatorTypeDefSummary details of the TypeDef in the event originator
     * @param otherMetadataCollectionId the metadataCollection using the conflicting TypeDef
     * @param conflictingTypeDefSummary the details of the TypeDef in the other metadata collection
     * @param errorMessage details of the error that occurs when the connection is used.
     */
    @SuppressWarnings(value = "unused")
    public void processTypeDefConflictEvent(String         sourceName,
                                            String         originatorMetadataCollectionId,
                                            String         originatorServerName,
                                            String         originatorServerType,
                                            String         originatorOrganizationName,
                                            TypeDefSummary originatorTypeDefSummary,
                                            String         otherMetadataCollectionId,
                                            TypeDefSummary conflictingTypeDefSummary,
                                            String         errorMessage)

    {
        log.debug("Processing TypeDef conflict event from: " + sourceName);
    }


    /**
     * Process a detected conflict in the attribute type definitions (AttributeTypeDefs) used in the cohort.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param originatorAttributeTypeDef description of the AttributeTypeDef in the event originator.
     * @param otherMetadataCollectionId the metadataCollection using the conflicting AttributeTypeDef.
     * @param conflictingAttributeTypeDef description of the AttributeTypeDef in the other metadata collection.
     * @param errorMessage details of the error that occurs when the connection is used.
     */
    @SuppressWarnings(value = "unused")
    public void processAttributeTypeDefConflictEvent(String           sourceName,
                                                     String           originatorMetadataCollectionId,
                                                     String           originatorServerName,
                                                     String           originatorServerType,
                                                     String           originatorOrganizationName,
                                                     AttributeTypeDef originatorAttributeTypeDef,
                                                     String           otherMetadataCollectionId,
                                                     AttributeTypeDef conflictingAttributeTypeDef,
                                                     String           errorMessage)
    {
        log.debug("Processing AttributeTypeDef conflict event from: " + sourceName);
    }


    /**
     * A TypeDef from another member in the cohort is at a different version than the local repository.  This may
     * create some inconsistencies in the different copies of instances of this type in different members of the
     * cohort.  The recommended action is to update all TypeDefs to the latest version.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param targetMetadataCollectionId identifier of the metadata collection that is reporting a TypeDef at a
     *                                   different level to the local repository.
     * @param targetTypeDefSummary details of the TypeDef being patched
     * @param otherTypeDef details of the TypeDef in the local repository.
     * @param errorMessage descriptive message
     */
    @SuppressWarnings(value = "unused")
    public void processTypeDefPatchMismatchEvent(String         sourceName,
                                                 String         originatorMetadataCollectionId,
                                                 String         originatorServerName,
                                                 String         originatorServerType,
                                                 String         originatorOrganizationName,
                                                 String         targetMetadataCollectionId,
                                                 TypeDefSummary targetTypeDefSummary,
                                                 TypeDef        otherTypeDef,
                                                 String         errorMessage)
    {
        log.debug("Processing TypeDefPatch mismatch event from: " + sourceName);
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
    public void processNewEntityEvent(String       sourceName,
                                      String       originatorMetadataCollectionId,
                                      String       originatorServerName,
                                      String       originatorServerType,
                                      String       originatorOrganizationName,
                                      EntityDetail entity)
    {
        log.debug("Processing new Entity event from: " + sourceName);
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
        log.debug("Processing updated Entity event from: " + sourceName);
    }


    /**
     * An update to an entity has been undone.
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
    public void processUndoneEntityEvent(String       sourceName,
                                         String       originatorMetadataCollectionId,
                                         String       originatorServerName,
                                         String       originatorServerType,
                                         String       originatorOrganizationName,
                                         EntityDetail entity)
    {
        log.debug("Processing undone Entity event from: " + sourceName);
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
    public void processClassifiedEntityEvent(String         sourceName,
                                             String         originatorMetadataCollectionId,
                                             String         originatorServerName,
                                             String         originatorServerType,
                                             String         originatorOrganizationName,
                                             EntityDetail   entity,
                                             Classification classification)
    {
        log.debug("Processing classified Entity event from: " + sourceName);

        /*
         * Supports subclasses still overriding the deprecated version of this event processing method
         */
        this.processClassifiedEntityEvent(sourceName,
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
     * @param entity  details of the entity with the new classification added. No guarantee this is all the classifications.
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
        log.debug("Processing classified EntityProxy event from: " + sourceName);
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
    @Deprecated
    @SuppressWarnings(value = "unused")
    public void processClassifiedEntityEvent(String       sourceName,
                                             String       originatorMetadataCollectionId,
                                             String       originatorServerName,
                                             String       originatorServerType,
                                             String       originatorOrganizationName,
                                             EntityDetail entity)
    {
        log.debug("Processing deprecated classified Entity event from: " + sourceName);
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
    public void processDeclassifiedEntityEvent(String         sourceName,
                                               String         originatorMetadataCollectionId,
                                               String         originatorServerName,
                                               String         originatorServerType,
                                               String         originatorOrganizationName,
                                               EntityDetail   entity,
                                               Classification originalClassification)
    {
        log.debug("Processing declassified Entity event from: " + sourceName);

        /*
         * Supports subclasses still overriding the deprecated version of this event processing method
         */
        this.processDeclassifiedEntityEvent(sourceName,
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
     * @param entity  details of the entity after the classification has been removed. No guarantee this is all the classifications.
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
        log.debug("Processing declassified EntityProxy event from: " + sourceName);
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
    @Deprecated
    @SuppressWarnings(value = "unused")
    public void processDeclassifiedEntityEvent(String       sourceName,
                                               String       originatorMetadataCollectionId,
                                               String       originatorServerName,
                                               String       originatorServerType,
                                               String       originatorOrganizationName,
                                               EntityDetail entity)
    {
        log.debug("Processing deprecated declassified Entity event from: " + sourceName);
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
    public void processReclassifiedEntityEvent(String         sourceName,
                                               String         originatorMetadataCollectionId,
                                               String         originatorServerName,
                                               String         originatorServerType,
                                               String         originatorOrganizationName,
                                               EntityDetail   entity,
                                               Classification originalClassification,
                                               Classification classification)
    {
        log.debug("Processing reclassified Entity event from: " + sourceName);

        /*
         * Supports subclasses still overriding the deprecated version of this event processing method
         */
        this.processReclassifiedEntityEvent(sourceName,
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
     * @param entity  details of the entity after the classification has been changed. No guarantee this is all the classifications.
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
        log.debug("Processing reclassified EntityProxy event from: " + sourceName);
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
    @Deprecated
    @SuppressWarnings(value = "unused")
    public void processReclassifiedEntityEvent(String       sourceName,
                                               String       originatorMetadataCollectionId,
                                               String       originatorServerName,
                                               String       originatorServerType,
                                               String       originatorOrganizationName,
                                               EntityDetail entity)
    {
        log.debug("Processing deprecated reclassified Entity event from: " + sourceName);
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
    public void processDeletedEntityEvent(String       sourceName,
                                          String       originatorMetadataCollectionId,
                                          String       originatorServerName,
                                          String       originatorServerType,
                                          String       originatorOrganizationName,
                                          EntityDetail entity)
    {
        log.debug("Processing deleted Entity event from: " + sourceName);
    }


    /**
     * A deleted entity has been permanently removed from the repository.  This request can not be undone.
     * <p>
     * Details of the TypeDef are included with the entity's unique id (guid) to ensure the right entity is purged in
     * the remote repositories.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param typeDefGUID                    unique identifier for this entity's TypeDef
     * @param typeDefName                    name of this entity's TypeDef
     * @param instanceGUID                   unique identifier for the entity
     */
    @SuppressWarnings(value="unused")
    public void processPurgedEntityEvent(String sourceName,
                                         String originatorMetadataCollectionId,
                                         String originatorServerName,
                                         String originatorServerType,
                                         String originatorOrganizationName,
                                         String typeDefGUID,
                                         String typeDefName,
                                         String instanceGUID)
    {
        log.debug("Processing purged Entity event from: " + sourceName);
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
        log.debug("Processing restored Entity event from: " + sourceName);
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
    public void processReTypedEntityEvent(String         sourceName,
                                          String         originatorMetadataCollectionId,
                                          String         originatorServerName,
                                          String         originatorServerType,
                                          String         originatorOrganizationName,
                                          TypeDefSummary originalTypeDefSummary,
                                          EntityDetail   entity)
    {
        log.debug("Processing re-typed Entity event from: " + sourceName);
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
    public void processReHomedEntityEvent(String       sourceName,
                                          String       originatorMetadataCollectionId,
                                          String       originatorServerName,
                                          String       originatorServerType,
                                          String       originatorOrganizationName,
                                          String       originalHomeMetadataCollectionId,
                                          EntityDetail entity)
    {
        log.debug("Processing re-homed Entity event from: " + sourceName);
    }


    /**
     * The remote repository is requesting that an entity from this repository's metadata collection is
     * refreshed so the remote repository can create a reference copy.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param typeDefGUID                    unique identifier for this entity's TypeDef
     * @param typeDefName                    name of this entity's TypeDef
     * @param instanceGUID                   unique identifier for the entity
     * @param homeMetadataCollectionId       metadata collection id for the home of this instance.
     */
    @SuppressWarnings(value = "unused")
    public void processRefreshEntityRequested(String sourceName,
                                              String originatorMetadataCollectionId,
                                              String originatorServerName,
                                              String originatorServerType,
                                              String originatorOrganizationName,
                                              String typeDefGUID,
                                              String typeDefName,
                                              String instanceGUID,
                                              String homeMetadataCollectionId)
    {
        log.debug("Processing refresh Entity request event from: " + sourceName);
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
        log.debug("Processing refresh Entity event from: " + sourceName);
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
    public void processNewRelationshipEvent(String       sourceName,
                                            String       originatorMetadataCollectionId,
                                            String       originatorServerName,
                                            String       originatorServerType,
                                            String       originatorOrganizationName,
                                            Relationship relationship)
    {
        log.debug("Processing new relationship event from: " + sourceName);
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
    public void processUpdatedRelationshipEvent(String       sourceName,
                                                String       originatorMetadataCollectionId,
                                                String       originatorServerName,
                                                String       originatorServerType,
                                                String       originatorOrganizationName,
                                                Relationship oldRelationship,
                                                Relationship newRelationship)
    {
        log.debug("Processing updated relationship event from: " + sourceName);
    }


    /**
     * An update to a relationship has been undone.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param relationship                   details of the version of the relationship that has been restored.
     */
    public void processUndoneRelationshipEvent(String       sourceName,
                                               String       originatorMetadataCollectionId,
                                               String       originatorServerName,
                                               String       originatorServerType,
                                               String       originatorOrganizationName,
                                               Relationship relationship)
    {
        log.debug("Processing undone relationship event from: " + sourceName);
    }


    /**
     * An existing relationship has been deleted.  This is a soft delete. This means it is still in the repository,
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
    public void processDeletedRelationshipEvent(String       sourceName,
                                                String       originatorMetadataCollectionId,
                                                String       originatorServerName,
                                                String       originatorServerType,
                                                String       originatorOrganizationName,
                                                Relationship relationship)
    {
        log.debug("Processing deleted relationship event from: " + sourceName);
    }


    /**
     * A deleted relationship has been permanently removed from the repository.  This request can not be undone.
     * <p>
     * Details of the TypeDef are included with the relationship's unique id (guid) to ensure the right
     * relationship is purged in the remote repositories.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param typeDefGUID                    unique identifier for this relationship's TypeDef.
     * @param typeDefName                    name of this relationship's TypeDef.
     * @param instanceGUID                   unique identifier for the relationship.
     */
    @SuppressWarnings(value = "unused")
    public void processPurgedRelationshipEvent(String sourceName,
                                               String originatorMetadataCollectionId,
                                               String originatorServerName,
                                               String originatorServerType,
                                               String originatorOrganizationName,
                                               String typeDefGUID,
                                               String typeDefName,
                                               String instanceGUID)
    {
        log.debug("Processing purged relationship event from: " + sourceName);
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
    public void processDeletePurgedRelationshipEvent(String       sourceName,
                                                     String       originatorMetadataCollectionId,
                                                     String       originatorServerName,
                                                     String       originatorServerType,
                                                     String       originatorOrganizationName,
                                                     Relationship relationship)
    {
        log.debug("Processing delete-purge relationship event from: " + sourceName);
    }


    /**
     * A deleted relationship has been restored to the state it was before it was deleted.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param relationship                   details of the version of the relationship that has been restored.
     */
    public void processRestoredRelationshipEvent(String       sourceName,
                                                 String       originatorMetadataCollectionId,
                                                 String       originatorServerName,
                                                 String       originatorServerType,
                                                 String       originatorOrganizationName,
                                                 Relationship relationship)
    {
        log.debug("Processing restored relationship event from: " + sourceName);
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
    public void processReIdentifiedRelationshipEvent(String       sourceName,
                                                     String       originatorMetadataCollectionId,
                                                     String       originatorServerName,
                                                     String       originatorServerType,
                                                     String       originatorOrganizationName,
                                                     String       originalRelationshipGUID,
                                                     Relationship relationship)
    {
        log.debug("Processing re-identified relationship event from: " + sourceName);
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
    public void processReTypedRelationshipEvent(String         sourceName,
                                                String         originatorMetadataCollectionId,
                                                String         originatorServerName,
                                                String         originatorServerType,
                                                String         originatorOrganizationName,
                                                TypeDefSummary originalTypeDefSummary,
                                                Relationship   relationship)
    {
        log.debug("Processing re-typed relationship event from: " + sourceName);
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
    public void processReHomedRelationshipEvent(String       sourceName,
                                                String       originatorMetadataCollectionId,
                                                String       originatorServerName,
                                                String       originatorServerType,
                                                String       originatorOrganizationName,
                                                String       originalHomeMetadataCollection,
                                                Relationship relationship)
    {
        log.debug("Processing re-homed relationship event from: " + sourceName);
    }


    /**
     * A repository has requested the home repository of a relationship send details of the relationship so
     * the local repository can create a reference copy of the instance.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param typeDefGUID                    unique identifier for this instance's TypeDef
     * @param typeDefName                    name of this relationship's TypeDef
     * @param instanceGUID                   unique identifier for the instance
     * @param homeMetadataCollectionId       metadata collection id for the home of this instance.
     */
    @SuppressWarnings(value = "unused")
    public void processRefreshRelationshipRequest(String sourceName,
                                                  String originatorMetadataCollectionId,
                                                  String originatorServerName,
                                                  String originatorServerType,
                                                  String originatorOrganizationName,
                                                  String typeDefGUID,
                                                  String typeDefName,
                                                  String instanceGUID,
                                                  String homeMetadataCollectionId)
    {
        log.debug("Processing refresh relationship request event from: " + sourceName);
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
    public void processRefreshRelationshipEvent(String       sourceName,
                                                String       originatorMetadataCollectionId,
                                                String       originatorServerName,
                                                String       originatorServerType,
                                                String       originatorOrganizationName,
                                                Relationship relationship)
    {
        log.debug("Processing refresh relationship event from: " + sourceName);
    }


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
    public void processInstanceBatchEvent(String        sourceName,
                                          String        originatorMetadataCollectionId,
                                          String        originatorServerName,
                                          String        originatorServerType,
                                          String        originatorOrganizationName,
                                          InstanceGraph instances)
    {
        log.debug("Processing instance batch event from: " + sourceName);

        if (instances != null)
        {
            List<EntityDetail> entities      = instances.getEntities();
            List<Relationship> relationships = instances.getRelationships();

            if (entities != null)
            {
                for (EntityDetail entity : entities)
                {
                    if (entity != null)
                    {
                        this.processNewEntityEvent(sourceName,
                                                   originatorMetadataCollectionId,
                                                   originatorServerName,
                                                   originatorServerType,
                                                   originatorOrganizationName,
                                                   entity);
                    }
                }
            }

            if (relationships != null)
            {
                for (Relationship relationship : relationships)
                {
                    if (relationship != null)
                    {
                        this.processNewRelationshipEvent(sourceName,
                                                         originatorMetadataCollectionId,
                                                         originatorServerName,
                                                         originatorServerType,
                                                         originatorOrganizationName,
                                                         relationship);
                    }
                }
            }
        }
    }


    /**
     * An open metadata repository has detected two metadata instances with the same identifier (guid).
     * This is a serious error because it could lead to corruption of the metadata collections within the cohort.
     * When this occurs, all repositories in the cohort delete their reference copies of the metadata instances and
     * at least one of the instances has its GUID changed in its respective home repository.  The updated instance(s)
     * are redistributed around the cohort.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId metadata collection id of the repository reporting the conflicting instance
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param targetMetadataCollectionId     metadata collection id of other repository with the conflicting instance
     * @param targetTypeDefSummary           details of the target instance's TypeDef
     * @param targetInstanceGUID             unique identifier for the source instance
     * @param otherOrigin                    origin of the other (older) metadata instance
     * @param otherMetadataCollectionId      metadata collection of the other (older) metadata instance
     * @param otherTypeDefSummary            details of the other (older) instance's TypeDef
     * @param otherInstanceGUID              unique identifier for the other (older) instance
     * @param errorMessage                   description of the error.
     */
    @SuppressWarnings(value = "unused")
    public void processConflictingInstancesEvent(String                 sourceName,
                                                 String                 originatorMetadataCollectionId,
                                                 String                 originatorServerName,
                                                 String                 originatorServerType,
                                                 String                 originatorOrganizationName,
                                                 String                 targetMetadataCollectionId,
                                                 TypeDefSummary         targetTypeDefSummary,
                                                 String                 targetInstanceGUID,
                                                 String                 otherMetadataCollectionId,
                                                 InstanceProvenanceType otherOrigin,
                                                 TypeDefSummary         otherTypeDefSummary,
                                                 String                 otherInstanceGUID,
                                                 String                 errorMessage)
    {
        log.debug("Processing conflicting instances event from: " + sourceName);
    }


    /**
     * An open metadata repository has detected an inconsistency in the version of the type used in an updated metadata
     * instance compared to its stored version.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId metadata collection id of the repository reporting the conflicting instance
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param targetMetadataCollectionId     metadata collection id of other repository with the conflicting instance
     * @param targetTypeDefSummary           details of the target instance's TypeDef
     * @param targetInstanceGUID             unique identifier for the source instance
     * @param otherTypeDefSummary            details of the local copy of the instance's TypeDef
     * @param errorMessage                   description of the error.
     */
    @SuppressWarnings(value = "unused")
    public void processConflictingTypeEvent(String         sourceName,
                                            String         originatorMetadataCollectionId,
                                            String         originatorServerName,
                                            String         originatorServerType,
                                            String         originatorOrganizationName,
                                            String         targetMetadataCollectionId,
                                            TypeDefSummary targetTypeDefSummary,
                                            String         targetInstanceGUID,
                                            TypeDefSummary otherTypeDefSummary,
                                            String         errorMessage)
    {
        log.debug("Processing conflicting instance type event from: " + sourceName);
    }
}
