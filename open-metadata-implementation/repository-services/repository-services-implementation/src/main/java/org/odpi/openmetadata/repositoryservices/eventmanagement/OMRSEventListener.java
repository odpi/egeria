/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.eventmanagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.events.*;
import org.odpi.openmetadata.repositoryservices.events.beans.v1.OMRSEventV1;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListener;

/**
 * <p>
 * The OMRSEventListener manages inbound events from the metadata repository cohort.  There are
 * two main groups of events: registration events that are handled by the OMRSCohortRegistry and metadata
 * events that describe changes to TypeDefs and metadata instances.  The metadata events are handled by the
 * local connector.
 * </p>
 * <p>
 * The role of the OMRSEventListener is to decide which events to process.  This is controlled by the
 * synchronization rule passed on the constructor.
 * </p>
 */
public class OMRSEventListener implements OMRSTopicListener
{
    private String cohortName;
    private String localMetadataCollectionId;

    /*
     * There is an event processor for each category of event.  The OMRSEventListener passes appropriate events to these
     * objects depending on the settings of its configuration.
     */
    private OMRSRegistryEventProcessor registryEventProcessor;
    private OMRSTypeDefEventProcessor  typeDefEventProcessor;
    private OMRSInstanceEventProcessor instanceEventProcessor;

    /*
     * The audit log is used for recording events, decisions, errors and exceptions
     */
    private  OMRSAuditLog  auditLog = new OMRSAuditLog(OMRSAuditingComponent.EVENT_LISTENER);


    private static final Logger log = LoggerFactory.getLogger(OMRSEventListener.class);


    /**
     * Default Constructor saves configuration parameters.
     *
     * @param cohortName name of the cohort that this event listener belongs to
     * @param localMetadataCollectionId unique identifier for the local metadata collection
     * @param registryEventProcessor processor for registry events
     * @param typeDefEventProcessor processor for TypeDef synchronization events
     * @param instanceEventProcessor processor for metadata instance replication
     */
    public OMRSEventListener(String                                cohortName,
                             String                                localMetadataCollectionId,
                             OMRSRegistryEventProcessor            registryEventProcessor,
                             OMRSTypeDefEventProcessor             typeDefEventProcessor,
                             OMRSInstanceEventProcessor            instanceEventProcessor)
    {
        this.cohortName = cohortName;
        this.localMetadataCollectionId = localMetadataCollectionId;
        this.registryEventProcessor = registryEventProcessor;
        this.typeDefEventProcessor  = typeDefEventProcessor;
        this.instanceEventProcessor = instanceEventProcessor;
    }


    /**
     * The event contains a registry event.  It needs to be further unpacked and passed to the appropriate
     * registry event processor (OMRSCohortRegistry).
     *
     * @param registryEvent event to unpack
     */
    public void processRegistryEvent(OMRSRegistryEvent   registryEvent)
    {
        log.debug("Processing registry event: " + registryEvent);

        if (registryEvent == null)
        {
            log.debug("Null registry event; ignoring event");
        }
        else if ((localMetadataCollectionId != null) &&
                 (localMetadataCollectionId.equals(registryEvent.getEventOriginator().getMetadataCollectionId())))
        {
            log.debug("Ignoring event that this server originated");
        }
        else if (registryEventProcessor == null)
        {
            log.debug("No registry event processor; ignoring event ");
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
                        registryEventProcessor.processRegistrationEvent(cohortName,
                                                                        registryEventOriginator.getMetadataCollectionId(),
                                                                        registryEventOriginator.getServerName(),
                                                                        registryEventOriginator.getServerType(),
                                                                        registryEventOriginator.getOrganizationName(),
                                                                        registryEvent.getRegistrationTimestamp(),
                                                                        registryEvent.getRemoteConnection());
                        break;

                    case RE_REGISTRATION_EVENT:
                        registryEventProcessor.processReRegistrationEvent(cohortName,
                                                                          registryEventOriginator.getMetadataCollectionId(),
                                                                          registryEventOriginator.getServerName(),
                                                                          registryEventOriginator.getServerType(),
                                                                          registryEventOriginator.getOrganizationName(),
                                                                          registryEvent.getRegistrationTimestamp(),
                                                                          registryEvent.getRemoteConnection());
                        break;

                    case REFRESH_REGISTRATION_REQUEST:
                        registryEventProcessor.processRegistrationRefreshRequest(cohortName,
                                                                                 registryEventOriginator.getServerName(),
                                                                                 registryEventOriginator.getServerType(),
                                                                                 registryEventOriginator.getOrganizationName());
                        break;

                    case UN_REGISTRATION_EVENT:
                        registryEventProcessor.processUnRegistrationEvent(cohortName,
                                                                          registryEventOriginator.getMetadataCollectionId(),
                                                                          registryEventOriginator.getServerName(),
                                                                          registryEventOriginator.getServerType(),
                                                                          registryEventOriginator.getOrganizationName());
                        break;

                    case REGISTRATION_ERROR_EVENT:
                        OMRSRegistryEventErrorCode errorCode = registryEvent.getErrorCode();

                        if (errorCode != null)
                        {
                            switch(errorCode)
                            {
                                case BAD_REMOTE_CONNECTION:
                                    registryEventProcessor.processBadConnectionEvent(cohortName,
                                                                                     registryEventOriginator.getMetadataCollectionId(),
                                                                                     registryEventOriginator.getServerName(),
                                                                                     registryEventOriginator.getServerType(),
                                                                                     registryEventOriginator.getOrganizationName(),
                                                                                     registryEvent.getTargetMetadataCollectionId(),
                                                                                     registryEvent.getTargetRemoteConnection(),
                                                                                     registryEvent.getErrorMessage());
                                    break;

                                case CONFLICTING_COLLECTION_ID:
                                    registryEventProcessor.processConflictingCollectionIdEvent(cohortName,
                                                                                               registryEventOriginator.getMetadataCollectionId(),
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
                        log.debug("Unknown registry event: " + registryEvent.toString());
                        break;
                }
            }
            else
            {
                log.debug("Ignored registry event: " + registryEvent.toString());
            }
        }
    }


    /**
     * Unpack and deliver a TypeDef event to the TypeDefEventProcessor
     *
     * @param typeDefEvent event to unpack
     */
    public void processTypeDefEvent(OMRSTypeDefEvent   typeDefEvent)
    {
        if (typeDefEvent == null)
        {
            log.debug("Null TypeDef event; ignoring event");
        }
        else if ((localMetadataCollectionId != null) &&
                (localMetadataCollectionId.equals(typeDefEvent.getEventOriginator().getMetadataCollectionId())))
        {
            log.debug("Ignoring event that this server originated");
        }
        else if (typeDefEventProcessor == null)
        {
            log.debug("No TypeDef event processor; ignoring event");
        }
        else
        {
            OMRSTypeDefEventType typeDefEventType       = typeDefEvent.getTypeDefEventType();
            OMRSEventOriginator  typeDefEventOriginator = typeDefEvent.getEventOriginator();

            if ((typeDefEventType != null) && (typeDefEventOriginator != null))
            {
                switch (typeDefEventType)
                {
                    case NEW_TYPEDEF_EVENT:
                        typeDefEventProcessor.processNewTypeDefEvent(cohortName,
                                                                     typeDefEventOriginator.getMetadataCollectionId(),
                                                                     typeDefEventOriginator.getServerName(),
                                                                     typeDefEventOriginator.getServerType(),
                                                                     typeDefEventOriginator.getOrganizationName(),
                                                                     typeDefEvent.getTypeDef());
                        break;

                    case NEW_ATTRIBUTE_TYPEDEF_EVENT:
                        typeDefEventProcessor.processNewAttributeTypeDefEvent(cohortName,
                                                                              typeDefEventOriginator.getMetadataCollectionId(),
                                                                              typeDefEventOriginator.getServerName(),
                                                                              typeDefEventOriginator.getServerType(),
                                                                              typeDefEventOriginator.getOrganizationName(),
                                                                              typeDefEvent.getAttributeTypeDef());
                        break;

                    case UPDATED_TYPEDEF_EVENT:
                        typeDefEventProcessor.processUpdatedTypeDefEvent(cohortName,
                                                                         typeDefEventOriginator.getMetadataCollectionId(),
                                                                         typeDefEventOriginator.getServerName(),
                                                                         typeDefEventOriginator.getServerType(),
                                                                         typeDefEventOriginator.getOrganizationName(),
                                                                         typeDefEvent.getTypeDefPatch());
                        break;

                    case DELETED_TYPEDEF_EVENT:
                        typeDefEventProcessor.processDeletedTypeDefEvent(cohortName,
                                                                         typeDefEventOriginator.getMetadataCollectionId(),
                                                                         typeDefEventOriginator.getServerName(),
                                                                         typeDefEventOriginator.getServerType(),
                                                                         typeDefEventOriginator.getOrganizationName(),
                                                                         typeDefEvent.getTypeDefGUID(),
                                                                         typeDefEvent.getTypeDefName());
                        break;

                    case DELETED_ATTRIBUTE_TYPEDEF_EVENT:
                        typeDefEventProcessor.processDeletedAttributeTypeDefEvent(cohortName,
                                                                                  typeDefEventOriginator.getMetadataCollectionId(),
                                                                                  typeDefEventOriginator.getServerName(),
                                                                                  typeDefEventOriginator.getServerType(),
                                                                                  typeDefEventOriginator.getOrganizationName(),
                                                                                  typeDefEvent.getTypeDefGUID(),
                                                                                  typeDefEvent.getTypeDefName());
                        break;

                    case RE_IDENTIFIED_TYPEDEF_EVENT:
                        typeDefEventProcessor.processReIdentifiedTypeDefEvent(cohortName,
                                                                              typeDefEventOriginator.getMetadataCollectionId(),
                                                                              typeDefEventOriginator.getServerName(),
                                                                              typeDefEventOriginator.getServerType(),
                                                                              typeDefEventOriginator.getOrganizationName(),
                                                                              typeDefEvent.getOriginalTypeDefSummary(),
                                                                              typeDefEvent.getTypeDef());
                        break;

                    case RE_IDENTIFIED_ATTRIBUTE_TYPEDEF_EVENT:
                        typeDefEventProcessor.processReIdentifiedAttributeTypeDefEvent(cohortName,
                                                                                       typeDefEventOriginator.getMetadataCollectionId(),
                                                                                       typeDefEventOriginator.getServerName(),
                                                                                       typeDefEventOriginator.getServerType(),
                                                                                       typeDefEventOriginator.getOrganizationName(),
                                                                                       typeDefEvent.getOriginalAttributeTypeDef(),
                                                                                       typeDefEvent.getAttributeTypeDef());

                    case TYPEDEF_ERROR_EVENT:
                        OMRSTypeDefEventErrorCode errorCode = typeDefEvent.getErrorCode();

                        if (errorCode != null)
                        {
                            switch(errorCode)
                            {
                                case CONFLICTING_TYPEDEFS:
                                    typeDefEventProcessor.processTypeDefConflictEvent(cohortName,
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
                                    typeDefEventProcessor.processAttributeTypeDefConflictEvent(cohortName,
                                                                                               typeDefEventOriginator.getMetadataCollectionId(),
                                                                                               typeDefEventOriginator.getServerName(),
                                                                                               typeDefEventOriginator.getServerType(),
                                                                                               typeDefEventOriginator.getOrganizationName(),
                                                                                               typeDefEvent.getOriginalAttributeTypeDef(),
                                                                                               typeDefEvent.getOtherMetadataCollectionId(),
                                                                                               typeDefEvent.getOtherAttributeTypeDef(),
                                                                                               typeDefEvent.getErrorMessage());

                                case TYPEDEF_PATCH_MISMATCH:
                                    typeDefEventProcessor.processTypeDefPatchMismatchEvent(cohortName,
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
    }


    /**
     * Unpack and deliver an instance event to the InstanceEventProcessor
     *
     * @param instanceEvent event to unpack
     */
    public void processInstanceEvent(OMRSInstanceEvent  instanceEvent)
    {
        log.debug("Processing instance event: " + instanceEvent);

        if (instanceEvent == null)
        {
            log.debug("Null instance event ignoring event");
        }
        else if ((localMetadataCollectionId != null) &&
                (localMetadataCollectionId.equals(instanceEvent.getEventOriginator().getMetadataCollectionId())))
        {
            log.debug("Ignoring event that this server originated");
        }
        else
        {
            OMRSInstanceEventType instanceEventType       = instanceEvent.getInstanceEventType();
            OMRSEventOriginator   instanceEventOriginator = instanceEvent.getEventOriginator();

            if ((instanceEventType != null) && (instanceEventOriginator != null))
            {
                switch (instanceEventType)
                {
                    case NEW_ENTITY_EVENT:
                        instanceEventProcessor.processNewEntityEvent(cohortName,
                                                                     instanceEventOriginator.getMetadataCollectionId(),
                                                                     instanceEventOriginator.getServerName(),
                                                                     instanceEventOriginator.getServerType(),
                                                                     instanceEventOriginator.getOrganizationName(),
                                                                     instanceEvent.getEntity());
                        break;

                    case UPDATED_ENTITY_EVENT:
                        instanceEventProcessor.processUpdatedEntityEvent(cohortName,
                                                                         instanceEventOriginator.getMetadataCollectionId(),
                                                                         instanceEventOriginator.getServerName(),
                                                                         instanceEventOriginator.getServerType(),
                                                                         instanceEventOriginator.getOrganizationName(),
                                                                         instanceEvent.getOriginalEntity(),
                                                                         instanceEvent.getEntity());
                        break;

                    case CLASSIFIED_ENTITY_EVENT:
                        instanceEventProcessor.processClassifiedEntityEvent(cohortName,
                                                                            instanceEventOriginator.getMetadataCollectionId(),
                                                                            instanceEventOriginator.getServerName(),
                                                                            instanceEventOriginator.getServerType(),
                                                                            instanceEventOriginator.getOrganizationName(),
                                                                            instanceEvent.getEntity());
                        break;

                    case RECLASSIFIED_ENTITY_EVENT:
                        instanceEventProcessor.processReclassifiedEntityEvent(cohortName,
                                                                              instanceEventOriginator.getMetadataCollectionId(),
                                                                              instanceEventOriginator.getServerName(),
                                                                              instanceEventOriginator.getServerType(),
                                                                              instanceEventOriginator.getOrganizationName(),
                                                                              instanceEvent.getEntity());
                        break;

                    case DECLASSIFIED_ENTITY_EVENT:
                        instanceEventProcessor.processDeclassifiedEntityEvent(cohortName,
                                                                              instanceEventOriginator.getMetadataCollectionId(),
                                                                              instanceEventOriginator.getServerName(),
                                                                              instanceEventOriginator.getServerType(),
                                                                              instanceEventOriginator.getOrganizationName(),
                                                                              instanceEvent.getEntity());
                        break;

                    case DELETED_ENTITY_EVENT:
                        instanceEventProcessor.processDeletedEntityEvent(cohortName,
                                                                         instanceEventOriginator.getMetadataCollectionId(),
                                                                         instanceEventOriginator.getServerName(),
                                                                         instanceEventOriginator.getServerType(),
                                                                         instanceEventOriginator.getOrganizationName(),
                                                                         instanceEvent.getEntity());
                        break;

                    case PURGED_ENTITY_EVENT:
                        instanceEventProcessor.processPurgedEntityEvent(cohortName,
                                                                        instanceEventOriginator.getMetadataCollectionId(),
                                                                        instanceEventOriginator.getServerName(),
                                                                        instanceEventOriginator.getServerType(),
                                                                        instanceEventOriginator.getOrganizationName(),
                                                                        instanceEvent.getTypeDefGUID(),
                                                                        instanceEvent.getTypeDefName(),
                                                                        instanceEvent.getInstanceGUID());
                        break;

                    case UNDONE_ENTITY_EVENT:
                        instanceEventProcessor.processUndoneEntityEvent(cohortName,
                                                                        instanceEventOriginator.getMetadataCollectionId(),
                                                                        instanceEventOriginator.getServerName(),
                                                                        instanceEventOriginator.getServerType(),
                                                                        instanceEventOriginator.getOrganizationName(),
                                                                        instanceEvent.getEntity());
                        break;

                    case RESTORED_ENTITY_EVENT:
                        instanceEventProcessor.processRestoredEntityEvent(cohortName,
                                                                          instanceEventOriginator.getMetadataCollectionId(),
                                                                          instanceEventOriginator.getServerName(),
                                                                          instanceEventOriginator.getServerType(),
                                                                          instanceEventOriginator.getOrganizationName(),
                                                                          instanceEvent.getEntity());
                        break;

                    case REFRESH_ENTITY_REQUEST:
                        instanceEventProcessor.processRefreshEntityRequested(cohortName,
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
                        instanceEventProcessor.processRefreshEntityEvent(cohortName,
                                                                         instanceEventOriginator.getMetadataCollectionId(),
                                                                         instanceEventOriginator.getServerName(),
                                                                         instanceEventOriginator.getServerType(),
                                                                         instanceEventOriginator.getOrganizationName(),
                                                                         instanceEvent.getEntity());
                        break;

                    case RE_HOMED_ENTITY_EVENT:
                        instanceEventProcessor.processReHomedEntityEvent(cohortName,
                                                                         instanceEventOriginator.getMetadataCollectionId(),
                                                                         instanceEventOriginator.getServerName(),
                                                                         instanceEventOriginator.getServerType(),
                                                                         instanceEventOriginator.getOrganizationName(),
                                                                         instanceEvent.getOriginalHomeMetadataCollectionId(),
                                                                         instanceEvent.getEntity());
                        break;

                    case RETYPED_ENTITY_EVENT:
                        instanceEventProcessor.processReTypedEntityEvent(cohortName,
                                                                         instanceEventOriginator.getMetadataCollectionId(),
                                                                         instanceEventOriginator.getServerName(),
                                                                         instanceEventOriginator.getServerType(),
                                                                         instanceEventOriginator.getOrganizationName(),
                                                                         instanceEvent.getOriginalTypeDefSummary(),
                                                                         instanceEvent.getEntity());
                        break;

                    case RE_IDENTIFIED_ENTITY_EVENT:
                        instanceEventProcessor.processReIdentifiedEntityEvent(cohortName,
                                                                              instanceEventOriginator.getMetadataCollectionId(),
                                                                              instanceEventOriginator.getServerName(),
                                                                              instanceEventOriginator.getServerType(),
                                                                              instanceEventOriginator.getOrganizationName(),
                                                                              instanceEvent.getOriginalInstanceGUID(),
                                                                              instanceEvent.getEntity());
                        break;

                    case NEW_RELATIONSHIP_EVENT:
                        instanceEventProcessor.processNewRelationshipEvent(cohortName,
                                                                           instanceEventOriginator.getMetadataCollectionId(),
                                                                           instanceEventOriginator.getServerName(),
                                                                           instanceEventOriginator.getServerType(),
                                                                           instanceEventOriginator.getOrganizationName(),
                                                                           instanceEvent.getRelationship());
                        break;

                    case UPDATED_RELATIONSHIP_EVENT:
                        instanceEventProcessor.processUpdatedRelationshipEvent(cohortName,
                                                                               instanceEventOriginator.getMetadataCollectionId(),
                                                                               instanceEventOriginator.getServerName(),
                                                                               instanceEventOriginator.getServerType(),
                                                                               instanceEventOriginator.getOrganizationName(),
                                                                               instanceEvent.getOriginalRelationship(),
                                                                               instanceEvent.getRelationship());
                        break;

                    case UNDONE_RELATIONSHIP_EVENT:
                        instanceEventProcessor.processUndoneRelationshipEvent(cohortName,
                                                                              instanceEventOriginator.getMetadataCollectionId(),
                                                                              instanceEventOriginator.getServerName(),
                                                                              instanceEventOriginator.getServerType(),
                                                                              instanceEventOriginator.getOrganizationName(),
                                                                              instanceEvent.getRelationship());
                        break;

                    case DELETED_RELATIONSHIP_EVENT:
                        instanceEventProcessor.processDeletedRelationshipEvent(cohortName,
                                                                               instanceEventOriginator.getMetadataCollectionId(),
                                                                               instanceEventOriginator.getServerName(),
                                                                               instanceEventOriginator.getServerType(),
                                                                               instanceEventOriginator.getOrganizationName(),
                                                                               instanceEvent.getRelationship());
                        break;

                    case PURGED_RELATIONSHIP_EVENT:
                        instanceEventProcessor.processPurgedEntityEvent(cohortName,
                                                                        instanceEventOriginator.getMetadataCollectionId(),
                                                                        instanceEventOriginator.getServerName(),
                                                                        instanceEventOriginator.getServerType(),
                                                                        instanceEventOriginator.getOrganizationName(),
                                                                        instanceEvent.getTypeDefGUID(),
                                                                        instanceEvent.getTypeDefName(),
                                                                        instanceEvent.getInstanceGUID());
                        break;

                    case RESTORED_RELATIONSHIP_EVENT:
                        instanceEventProcessor.processRestoredRelationshipEvent(cohortName,
                                                                                instanceEventOriginator.getMetadataCollectionId(),
                                                                                instanceEventOriginator.getServerName(),
                                                                                instanceEventOriginator.getServerType(),
                                                                                instanceEventOriginator.getOrganizationName(),
                                                                                instanceEvent.getRelationship());
                        break;

                    case REFRESH_RELATIONSHIP_REQUEST:
                        instanceEventProcessor.processRefreshRelationshipRequest(cohortName,
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
                        instanceEventProcessor.processRefreshRelationshipEvent(cohortName,
                                                                               instanceEventOriginator.getMetadataCollectionId(),
                                                                               instanceEventOriginator.getServerName(),
                                                                               instanceEventOriginator.getServerType(),
                                                                               instanceEventOriginator.getOrganizationName(),
                                                                               instanceEvent.getRelationship());
                        break;

                    case RE_IDENTIFIED_RELATIONSHIP_EVENT:
                        instanceEventProcessor.processReIdentifiedRelationshipEvent(cohortName,
                                                                                    instanceEventOriginator.getMetadataCollectionId(),
                                                                                    instanceEventOriginator.getServerName(),
                                                                                    instanceEventOriginator.getServerType(),
                                                                                    instanceEventOriginator.getOrganizationName(),
                                                                                    instanceEvent.getOriginalInstanceGUID(),
                                                                                    instanceEvent.getRelationship());
                        break;

                    case RE_HOMED_RELATIONSHIP_EVENT:
                        instanceEventProcessor.processReHomedRelationshipEvent(cohortName,
                                                                               instanceEventOriginator.getMetadataCollectionId(),
                                                                               instanceEventOriginator.getServerName(),
                                                                               instanceEventOriginator.getServerType(),
                                                                               instanceEventOriginator.getOrganizationName(),
                                                                               instanceEvent.getOriginalHomeMetadataCollectionId(),
                                                                               instanceEvent.getRelationship());
                        break;

                    case RETYPED_RELATIONSHIP_EVENT:
                        instanceEventProcessor.processReTypedRelationshipEvent(cohortName,
                                                                               instanceEventOriginator.getMetadataCollectionId(),
                                                                               instanceEventOriginator.getServerName(),
                                                                               instanceEventOriginator.getServerType(),
                                                                               instanceEventOriginator.getOrganizationName(),
                                                                               instanceEvent.getOriginalTypeDefSummary(),
                                                                               instanceEvent.getRelationship());
                        break;

                    case INSTANCE_ERROR_EVENT:
                        OMRSInstanceEventErrorCode errorCode = instanceEvent.getErrorCode();

                        if (errorCode != null)
                        {
                            switch(errorCode)
                            {
                                case CONFLICTING_INSTANCES:
                                    instanceEventProcessor.processConflictingInstancesEvent(cohortName,
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
                                    instanceEventProcessor.processConflictingTypeEvent(cohortName,
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
    }
}



