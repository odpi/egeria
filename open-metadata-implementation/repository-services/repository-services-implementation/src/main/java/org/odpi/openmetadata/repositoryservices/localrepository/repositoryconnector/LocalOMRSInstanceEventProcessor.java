/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.events.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSLogicErrorException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryeventmapper.OMRSRepositoryEventProcessor;
import org.odpi.openmetadata.repositoryservices.eventmanagement.*;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;



/**
 * LocalOMRSInstanceEventProcessor processes incoming metadata instance events that describe changes to the
 * entities and relationships in other repositories in the connected cohorts.
 * It uses the save exchange rule to decide which events to process and which to ignore.
 * Events that are to be processed are converted into reference copies of their respective entities and
 * relationships and stored in the local repository.
 */
public class LocalOMRSInstanceEventProcessor implements OMRSInstanceEventProcessor, OMRSInstanceRetrievalEventProcessor
{
    private String                          localMetadataCollectionId;
    private String                          localServerName;
    private OMRSRepositoryConnector         realLocalConnector;
    private OMRSRepositoryHelper            repositoryHelper;
    private OMRSRepositoryValidator         repositoryValidator;
    private OMRSRepositoryEventExchangeRule saveExchangeRule;
    private String                          realRepositoryName               = "Local Repository";
    private OMRSMetadataCollection          realMetadataCollection           = null;
    private OMRSRepositoryEventProcessor    outboundRepositoryEventProcessor = null;



    /*
     * The audit log provides a verifiable record of the open metadata archives that have been loaded into
     * the open metadata repository.  The Logger is for standard debug.
     */
    private static final OMRSAuditLog auditLog = new OMRSAuditLog(OMRSAuditingComponent.INSTANCE_EVENT_PROCESSOR);
    private static final Logger       log      = LoggerFactory.getLogger(LocalOMRSInstanceEventProcessor.class);


    /**
     * Constructor saves all of the information necessary to process incoming instance events.  It is intolerant
     * of nulls in any of its parameters and will throw a logic error exception is it finds any.
     *
     * @param localMetadataCollectionId local metadata collection identifier
     * @param localServerName           name of the local server for logging
     * @param realLocalConnector        connector to the real local repository
     * @param repositoryHelper          helper class for building instances
     * @param repositoryValidator       helper class for validating instances
     * @param saveExchangeRule          rule that determines which events to process.
     */
    LocalOMRSInstanceEventProcessor(String                          localMetadataCollectionId,
                                    String                          localServerName,
                                    OMRSRepositoryConnector         realLocalConnector,
                                    OMRSRepositoryHelper            repositoryHelper,
                                    OMRSRepositoryValidator         repositoryValidator,
                                    OMRSRepositoryEventExchangeRule saveExchangeRule,
                                    OMRSRepositoryEventProcessor    outboundRepositoryEventProcessor)
    {
        final String methodName = "LocalOMRSInstanceEventProcessor constructor";

        this.localMetadataCollectionId = localMetadataCollectionId;
        this.localServerName = localServerName;
        this.realLocalConnector = realLocalConnector;
        this.repositoryHelper = repositoryHelper;
        this.repositoryValidator = repositoryValidator;
        this.saveExchangeRule = saveExchangeRule;
        this.outboundRepositoryEventProcessor = outboundRepositoryEventProcessor;

        if (realLocalConnector != null)
        {
            try
            {
                this.realMetadataCollection = realLocalConnector.getMetadataCollection();
            }
            catch (Throwable  error)
            {
                /*
                 * Nothing to do, error will be logged in verifyEventProcessor
                 */
                this.realMetadataCollection = null;
            }
        }

        this.verifyEventProcessor(methodName);

        ConnectionProperties connection = this.realLocalConnector.getConnection();
        if (connection != null)
        {
            this.realRepositoryName = connection.getConnectionName();
        }
    }


    /*
     * ====================================
     * OMRSInstanceEventProcessor
     */


    /**
     * Unpack and process the incoming event
     *
     * @param cohortName source of the event
     * @param instanceEvent the event to process
     */
    public void   sendInstanceEvent(String            cohortName,
                                    OMRSInstanceEvent instanceEvent)
    {
        OMRSInstanceEventType instanceEventType       = instanceEvent.getInstanceEventType();
        OMRSEventOriginator   instanceEventOriginator = instanceEvent.getEventOriginator();

        if ((instanceEventType != null) && (instanceEventOriginator != null))
        {
            switch (instanceEventType)
            {
                case NEW_ENTITY_EVENT:
                    this.processNewEntityEvent(cohortName,
                                               instanceEventOriginator.getMetadataCollectionId(),
                                               instanceEventOriginator.getServerName(),
                                               instanceEventOriginator.getServerType(),
                                               instanceEventOriginator.getOrganizationName(),
                                               instanceEvent.getEntity());
                    break;

                case UPDATED_ENTITY_EVENT:
                    this.processUpdatedEntityEvent(cohortName,
                                                   instanceEventOriginator.getMetadataCollectionId(),
                                                   instanceEventOriginator.getServerName(),
                                                   instanceEventOriginator.getServerType(),
                                                   instanceEventOriginator.getOrganizationName(),
                                                   instanceEvent.getOriginalEntity(),
                                                   instanceEvent.getEntity());
                    break;

                case CLASSIFIED_ENTITY_EVENT:
                    this.processClassifiedEntityEvent(cohortName,
                                                      instanceEventOriginator.getMetadataCollectionId(),
                                                      instanceEventOriginator.getServerName(),
                                                      instanceEventOriginator.getServerType(),
                                                      instanceEventOriginator.getOrganizationName(),
                                                      instanceEvent.getEntity());
                    break;

                case RECLASSIFIED_ENTITY_EVENT:
                    this.processReclassifiedEntityEvent(cohortName,
                                                        instanceEventOriginator.getMetadataCollectionId(),
                                                        instanceEventOriginator.getServerName(),
                                                        instanceEventOriginator.getServerType(),
                                                        instanceEventOriginator.getOrganizationName(),
                                                        instanceEvent.getEntity());
                    break;

                case DECLASSIFIED_ENTITY_EVENT:
                    this.processDeclassifiedEntityEvent(cohortName,
                                                        instanceEventOriginator.getMetadataCollectionId(),
                                                        instanceEventOriginator.getServerName(),
                                                        instanceEventOriginator.getServerType(),
                                                        instanceEventOriginator.getOrganizationName(),
                                                        instanceEvent.getEntity());
                    break;

                case DELETED_ENTITY_EVENT:
                    this.processDeletedEntityEvent(cohortName,
                                                   instanceEventOriginator.getMetadataCollectionId(),
                                                   instanceEventOriginator.getServerName(),
                                                   instanceEventOriginator.getServerType(),
                                                   instanceEventOriginator.getOrganizationName(),
                                                   instanceEvent.getEntity());
                    break;

                case PURGED_ENTITY_EVENT:
                    this.processPurgedEntityEvent(cohortName,
                                                  instanceEventOriginator.getMetadataCollectionId(),
                                                  instanceEventOriginator.getServerName(),
                                                  instanceEventOriginator.getServerType(),
                                                  instanceEventOriginator.getOrganizationName(),
                                                  instanceEvent.getTypeDefGUID(),
                                                  instanceEvent.getTypeDefName(),
                                                  instanceEvent.getInstanceGUID());
                    break;

                case UNDONE_ENTITY_EVENT:
                    this.processUndoneEntityEvent(cohortName,
                                                  instanceEventOriginator.getMetadataCollectionId(),
                                                  instanceEventOriginator.getServerName(),
                                                  instanceEventOriginator.getServerType(),
                                                  instanceEventOriginator.getOrganizationName(),
                                                  instanceEvent.getEntity());
                    break;

                case RESTORED_ENTITY_EVENT:
                    this.processRestoredEntityEvent(cohortName,
                                                    instanceEventOriginator.getMetadataCollectionId(),
                                                    instanceEventOriginator.getServerName(),
                                                    instanceEventOriginator.getServerType(),
                                                    instanceEventOriginator.getOrganizationName(),
                                                    instanceEvent.getEntity());
                    break;

                case REFRESH_ENTITY_REQUEST:
                    this.processRefreshEntityRequested(cohortName,
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
                    this.processRefreshEntityEvent(cohortName,
                                                   instanceEventOriginator.getMetadataCollectionId(),
                                                   instanceEventOriginator.getServerName(),
                                                   instanceEventOriginator.getServerType(),
                                                   instanceEventOriginator.getOrganizationName(),
                                                   instanceEvent.getEntity());
                    break;

                case RE_HOMED_ENTITY_EVENT:
                    this.processReHomedEntityEvent(cohortName,
                                                   instanceEventOriginator.getMetadataCollectionId(),
                                                   instanceEventOriginator.getServerName(),
                                                   instanceEventOriginator.getServerType(),
                                                   instanceEventOriginator.getOrganizationName(),
                                                   instanceEvent.getOriginalHomeMetadataCollectionId(),
                                                   instanceEvent.getEntity());
                    break;

                case RETYPED_ENTITY_EVENT:
                    this.processReTypedEntityEvent(cohortName,
                                                   instanceEventOriginator.getMetadataCollectionId(),
                                                   instanceEventOriginator.getServerName(),
                                                   instanceEventOriginator.getServerType(),
                                                   instanceEventOriginator.getOrganizationName(),
                                                   instanceEvent.getOriginalTypeDefSummary(),
                                                   instanceEvent.getEntity());
                    break;

                case RE_IDENTIFIED_ENTITY_EVENT:
                    this.processReIdentifiedEntityEvent(cohortName,
                                                        instanceEventOriginator.getMetadataCollectionId(),
                                                        instanceEventOriginator.getServerName(),
                                                        instanceEventOriginator.getServerType(),
                                                        instanceEventOriginator.getOrganizationName(),
                                                        instanceEvent.getOriginalInstanceGUID(),
                                                        instanceEvent.getEntity());
                    break;

                case NEW_RELATIONSHIP_EVENT:
                    this.processNewRelationshipEvent(cohortName,
                                                     instanceEventOriginator.getMetadataCollectionId(),
                                                     instanceEventOriginator.getServerName(),
                                                     instanceEventOriginator.getServerType(),
                                                     instanceEventOriginator.getOrganizationName(),
                                                     instanceEvent.getRelationship());
                    break;

                case UPDATED_RELATIONSHIP_EVENT:
                    this.processUpdatedRelationshipEvent(cohortName,
                                                         instanceEventOriginator.getMetadataCollectionId(),
                                                         instanceEventOriginator.getServerName(),
                                                         instanceEventOriginator.getServerType(),
                                                         instanceEventOriginator.getOrganizationName(),
                                                         instanceEvent.getOriginalRelationship(),
                                                         instanceEvent.getRelationship());
                    break;

                case UNDONE_RELATIONSHIP_EVENT:
                    this.processUndoneRelationshipEvent(cohortName,
                                                        instanceEventOriginator.getMetadataCollectionId(),
                                                        instanceEventOriginator.getServerName(),
                                                        instanceEventOriginator.getServerType(),
                                                        instanceEventOriginator.getOrganizationName(),
                                                        instanceEvent.getRelationship());
                    break;

                case DELETED_RELATIONSHIP_EVENT:
                    this.processDeletedRelationshipEvent(cohortName,
                                                         instanceEventOriginator.getMetadataCollectionId(),
                                                         instanceEventOriginator.getServerName(),
                                                         instanceEventOriginator.getServerType(),
                                                         instanceEventOriginator.getOrganizationName(),
                                                         instanceEvent.getRelationship());
                    break;

                case PURGED_RELATIONSHIP_EVENT:
                    this.processPurgedEntityEvent(cohortName,
                                                  instanceEventOriginator.getMetadataCollectionId(),
                                                  instanceEventOriginator.getServerName(),
                                                  instanceEventOriginator.getServerType(),
                                                  instanceEventOriginator.getOrganizationName(),
                                                  instanceEvent.getTypeDefGUID(),
                                                  instanceEvent.getTypeDefName(),
                                                  instanceEvent.getInstanceGUID());
                    break;

                case RESTORED_RELATIONSHIP_EVENT:
                    this.processRestoredRelationshipEvent(cohortName,
                                                          instanceEventOriginator.getMetadataCollectionId(),
                                                          instanceEventOriginator.getServerName(),
                                                          instanceEventOriginator.getServerType(),
                                                          instanceEventOriginator.getOrganizationName(),
                                                          instanceEvent.getRelationship());
                    break;

                case REFRESH_RELATIONSHIP_REQUEST:
                    this.processRefreshRelationshipRequest(cohortName,
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
                    this.processRefreshRelationshipEvent(cohortName,
                                                         instanceEventOriginator.getMetadataCollectionId(),
                                                         instanceEventOriginator.getServerName(),
                                                         instanceEventOriginator.getServerType(),
                                                         instanceEventOriginator.getOrganizationName(),
                                                         instanceEvent.getRelationship());
                    break;

                case RE_IDENTIFIED_RELATIONSHIP_EVENT:
                    this.processReIdentifiedRelationshipEvent(cohortName,
                                                              instanceEventOriginator.getMetadataCollectionId(),
                                                              instanceEventOriginator.getServerName(),
                                                              instanceEventOriginator.getServerType(),
                                                              instanceEventOriginator.getOrganizationName(),
                                                              instanceEvent.getOriginalInstanceGUID(),
                                                              instanceEvent.getRelationship());
                    break;

                case RE_HOMED_RELATIONSHIP_EVENT:
                    this.processReHomedRelationshipEvent(cohortName,
                                                         instanceEventOriginator.getMetadataCollectionId(),
                                                         instanceEventOriginator.getServerName(),
                                                         instanceEventOriginator.getServerType(),
                                                         instanceEventOriginator.getOrganizationName(),
                                                         instanceEvent.getOriginalHomeMetadataCollectionId(),
                                                         instanceEvent.getRelationship());
                    break;

                case RETYPED_RELATIONSHIP_EVENT:
                    this.processReTypedRelationshipEvent(cohortName,
                                                         instanceEventOriginator.getMetadataCollectionId(),
                                                         instanceEventOriginator.getServerName(),
                                                         instanceEventOriginator.getServerType(),
                                                         instanceEventOriginator.getOrganizationName(),
                                                         instanceEvent.getOriginalTypeDefSummary(),
                                                         instanceEvent.getRelationship());
                    break;
                case BATCH_INSTANCES_EVENT:
                	this.processInstanceBatchEvent(cohortName,
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
                        switch(errorCode)
                        {
                            case CONFLICTING_INSTANCES:
                                this.processConflictingInstancesEvent(cohortName,
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
                                this.processConflictingTypeEvent(cohortName,
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
        final String methodName = "processNewEntityEvent";
        final String entityParameterName = "entity";

        updateReferenceEntity(sourceName,
                              methodName,
                              entityParameterName,
                              originatorMetadataCollectionId,
                              originatorServerName,
                              entity);
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
        final String methodName = "processUpdatedEntityEvent";
        final String entityParameterName = "entity";

        updateReferenceEntity(sourceName,
                              methodName,
                              entityParameterName,
                              originatorMetadataCollectionId,
                              originatorServerName,
                              newEntity);
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
        final String methodName = "processUndoneEntityEvent";
        final String entityParameterName = "entity";

        updateReferenceEntity(sourceName,
                              methodName,
                              entityParameterName,
                              originatorMetadataCollectionId,
                              originatorServerName,
                              entity);
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
    public void processClassifiedEntityEvent(String sourceName,
                                             String originatorMetadataCollectionId,
                                             String originatorServerName,
                                             String originatorServerType,
                                             String originatorOrganizationName,
                                             EntityDetail entity)
    {
        final String methodName = "processClassifiedEntityEvent";
        final String entityParameterName = "entity";

        updateReferenceEntity(sourceName,
                              methodName,
                              entityParameterName,
                              originatorMetadataCollectionId,
                              originatorServerName,
                              entity);
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
    public void processDeclassifiedEntityEvent(String sourceName,
                                               String originatorMetadataCollectionId,
                                               String originatorServerName,
                                               String originatorServerType,
                                               String originatorOrganizationName,
                                               EntityDetail entity)
    {
        final String methodName = "processDeclassifiedEntityEvent";
        final String entityParameterName = "entity";

        updateReferenceEntity(sourceName,
                              methodName,
                              entityParameterName,
                              originatorMetadataCollectionId,
                              originatorServerName,
                              entity);
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
    public void processReclassifiedEntityEvent(String       sourceName,
                                               String       originatorMetadataCollectionId,
                                               String       originatorServerName,
                                               String       originatorServerType,
                                               String       originatorOrganizationName,
                                               EntityDetail entity)
    {
        final String methodName = "processReclassifiedEntityEvent";
        final String entityParameterName = "entity";

        updateReferenceEntity(sourceName,
                              methodName,
                              entityParameterName,
                              originatorMetadataCollectionId,
                              originatorServerName,
                              entity);
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
        final String methodName = "processDeletedEntityEvent";
        final String entityParameterName = "entity";

        updateReferenceEntity(sourceName,
                              methodName,
                              entityParameterName,
                              originatorMetadataCollectionId,
                              originatorServerName,
                              entity);
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
    public void processPurgedEntityEvent(String sourceName,
                                         String originatorMetadataCollectionId,
                                         String originatorServerName,
                                         String originatorServerType,
                                         String originatorOrganizationName,
                                         String typeDefGUID,
                                         String typeDefName,
                                         String instanceGUID)
    {
        final String methodName = "processPurgedEntityEvent";

        purgeReferenceInstance(sourceName,
                               methodName,
                               originatorMetadataCollectionId,
                               originatorServerName,
                               typeDefGUID,
                               typeDefName,
                               instanceGUID);
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
        final String methodName = "processRestoredEntityEvent";
        final String entityParameterName = "entity";

        updateReferenceEntity(sourceName,
                              methodName,
                              entityParameterName,
                              originatorMetadataCollectionId,
                              originatorServerName,
                              entity);
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
        final String methodName = "processReIdentifiedEntityEvent";
        final String entityParameterName = "entity";

        updateReferenceEntity(sourceName,
                              methodName,
                              entityParameterName,
                              originatorMetadataCollectionId,
                              originatorServerName,
                              entity);
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
        final String methodName = "processReTypedEntityEvent";
        final String entityParameterName = "entity";

        updateReferenceEntity(sourceName,
                              methodName,
                              entityParameterName,
                              originatorMetadataCollectionId,
                              originatorServerName,
                              entity);
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
        final String methodName = "processReHomedEntityEvent";
        final String entityParameterName = "entity";

        updateReferenceEntity(sourceName,
                              methodName,
                              entityParameterName,
                              originatorMetadataCollectionId,
                              originatorServerName,
                              entity);
    }


    /**
     * The local repository is requesting that an entity from another repository's metadata collection is
     * refreshed so the local repository can create a reference copy.
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
        final String  methodName = "processRefreshEntityRequested";

        try
        {
            verifyEventProcessor(methodName);

            realMetadataCollection.refreshEntityReferenceCopy(sourceName,
                                                              instanceGUID,
                                                              typeDefGUID,
                                                              typeDefName,
                                                              originatorMetadataCollectionId);

        }
        catch (Throwable error)
        {
            OMRSAuditCode auditCode = OMRSAuditCode.UNEXPECTED_EXCEPTION_FROM_EVENT;
            auditLog.logRecord(methodName,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(methodName,
                                                                originatorServerName,
                                                                originatorMetadataCollectionId,
                                                                error.getMessage()),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
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
    public void processRefreshEntityEvent(String       sourceName,
                                          String       originatorMetadataCollectionId,
                                          String       originatorServerName,
                                          String       originatorServerType,
                                          String       originatorOrganizationName,
                                          EntityDetail entity)
    {
        final String methodName = "processReHomedEntityEvent";
        final String entityParameterName = "entity";

        updateReferenceEntity(sourceName,
                              methodName,
                              entityParameterName,
                              originatorMetadataCollectionId,
                              originatorServerName,
                              entity);
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
        final String methodName = "processNewRelationshipEvent";
        final String entityParameterName = "relationship";

        updateReferenceRelationship(sourceName,
                                    methodName,
                                    entityParameterName,
                                    originatorMetadataCollectionId,
                                    originatorServerName,
                                    relationship);
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
        final String methodName = "processUpdatedRelationshipEvent";
        final String entityParameterName = "relationship";

        updateReferenceRelationship(sourceName,
                                    methodName,
                                    entityParameterName,
                                    originatorMetadataCollectionId,
                                    originatorServerName,
                                    newRelationship);
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
        final String methodName = "processUndoneRelationshipEvent";
        final String entityParameterName = "relationship";

        updateReferenceRelationship(sourceName,
                                    methodName,
                                    entityParameterName,
                                    originatorMetadataCollectionId,
                                    originatorServerName,
                                    relationship);
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
    public void processDeletedRelationshipEvent(String       sourceName,
                                                String       originatorMetadataCollectionId,
                                                String       originatorServerName,
                                                String       originatorServerType,
                                                String       originatorOrganizationName,
                                                Relationship relationship)
    {
        final String methodName = "processDeletedRelationshipEvent";
        final String entityParameterName = "relationship";

        updateReferenceRelationship(sourceName,
                                    methodName,
                                    entityParameterName,
                                    originatorMetadataCollectionId,
                                    originatorServerName,
                                    relationship);
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
    public void processPurgedRelationshipEvent(String sourceName,
                                               String originatorMetadataCollectionId,
                                               String originatorServerName,
                                               String originatorServerType,
                                               String originatorOrganizationName,
                                               String typeDefGUID,
                                               String typeDefName,
                                               String instanceGUID)
    {
        final String methodName = "processPurgedRelationshipEvent";

        purgeReferenceInstance(sourceName,
                               methodName,
                               originatorMetadataCollectionId,
                               originatorServerName,
                               typeDefGUID,
                               typeDefName,
                               instanceGUID);
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
        final String methodName = "processRestoredRelationshipEvent";
        final String entityParameterName = "relationship";

        updateReferenceRelationship(sourceName,
                                    methodName,
                                    entityParameterName,
                                    originatorMetadataCollectionId,
                                    originatorServerName,
                                    relationship);
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
        final String methodName = "processReIdentifiedRelationshipEvent";
        final String entityParameterName = "relationship";

        updateReferenceRelationship(sourceName,
                                    methodName,
                                    entityParameterName,
                                    originatorMetadataCollectionId,
                                    originatorServerName,
                                    relationship);
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
    public void processReTypedRelationshipEvent(String         sourceName,
                                                String         originatorMetadataCollectionId,
                                                String         originatorServerName,
                                                String         originatorServerType,
                                                String         originatorOrganizationName,
                                                TypeDefSummary originalTypeDefSummary,
                                                Relationship   relationship)
    {
        final String methodName = "processReTypedRelationshipEvent";
        final String entityParameterName = "relationship";

        updateReferenceRelationship(sourceName,
                                    methodName,
                                    entityParameterName,
                                    originatorMetadataCollectionId,
                                    originatorServerName,
                                    relationship);
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
        final String methodName = "processReHomedRelationshipEvent";
        final String entityParameterName = "relationship";

        updateReferenceRelationship(sourceName,
                                    methodName,
                                    entityParameterName,
                                    originatorMetadataCollectionId,
                                    originatorServerName,
                                    relationship);
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
        final String    methodName = "processRefreshRelationshipRequest";

        try
        {
            verifyEventProcessor(methodName);

            realMetadataCollection.refreshRelationshipReferenceCopy(sourceName,
                                                                    instanceGUID,
                                                                    typeDefGUID,
                                                                    typeDefName,
                                                                    originatorMetadataCollectionId);

        }
        catch (Throwable error)
        {
            OMRSAuditCode auditCode = OMRSAuditCode.UNEXPECTED_EXCEPTION_FROM_EVENT;
            auditLog.logRecord(methodName,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(methodName,
                                                                originatorServerName,
                                                                originatorMetadataCollectionId,
                                                                error.getMessage()),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
        }
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
    public void processRefreshRelationshipEvent(String       sourceName,
                                                String       originatorMetadataCollectionId,
                                                String       originatorServerName,
                                                String       originatorServerType,
                                                String       originatorOrganizationName,
                                                Relationship relationship)
    {
        final String methodName = "processRefreshRelationshipEvent";
        final String relationshipParameterName = "relationship";

        updateReferenceRelationship(sourceName,
                                    methodName,
                                    relationshipParameterName,
                                    originatorMetadataCollectionId,
                                    originatorServerName,
                                    relationship);
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
    public void processInstanceBatchEvent(String         sourceName,
                                          String         originatorMetadataCollectionId,
                                          String         originatorServerName,
                                          String         originatorServerType,
                                          String         originatorOrganizationName,
                                          InstanceGraph  instances)
    {
        final String methodName = "processInstanceBatchEvent";

        try
        {
            verifyEventProcessor(methodName);

            realMetadataCollection.saveInstanceReferenceCopies(sourceName, instances);
        }
        catch (Throwable error)
        {
            OMRSAuditCode auditCode = OMRSAuditCode.UNEXPECTED_EXCEPTION_FROM_EVENT;
            auditLog.logRecord(methodName,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(methodName,
                                                                originatorServerName,
                                                                originatorMetadataCollectionId,
                                                                error.getMessage()),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
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

    }


    /*
     * =======================
     * OMRSInstanceRetrievalEventProcessor
     */


    /**
     * Pass an entity that has been retrieved from a remote open metadata repository so it can be validated and
     * (if the rules permit) cached in the local repository.
     *
     * @param sourceName name of the source of this event.
     * @param metadataCollectionId unique identifier for the metadata from the remote repository
     * @param processedEntityGUID the retrieved entity's GUID.
     * @param processedEntityType the retrieved entity's Type.
     */
    public void refreshRetrievedEntity(String        sourceName,
                                       String        metadataCollectionId,
                                       String        processedEntityGUID,
                                       InstanceType  processedEntityType)
    {
        try
        {
            if (realMetadataCollection.isEntityKnown(sourceName, processedEntityGUID) == null)
            {
                if (processedEntityType != null)
                {
                    /*
                     * It would be possible to save the relationship directly into the repository,
                     * but it is possible that some of the properties have been suppressed for the
                     * requesting user Id.  In which case saving it now would result in other users
                     * seeing a restricted view of the relationship.
                     */
                    realMetadataCollection.refreshEntityReferenceCopy(localServerName,
                                                                      processedEntityGUID,
                                                                      processedEntityType.getTypeDefGUID(),
                                                                      processedEntityType.getTypeDefName(),
                                                                      metadataCollectionId);
                }
            }
        }
        catch (Throwable   error)
        {
            final String methodName = "processRetrievedEntity";

            OMRSAuditCode auditCode = OMRSAuditCode.UNEXPECTED_EXCEPTION_FROM_EVENT;
            auditLog.logRecord(methodName,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(methodName,
                                                                sourceName,
                                                                metadataCollectionId,
                                                                error.getMessage()),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
        }
    }


    /**
     * Pass an entity that has been retrieved from a remote open metadata repository so it can be validated and
     * (if the rules permit) cached in the local repository.
     *
     * @param sourceName name of the source of this event.
     * @param metadataCollectionId unique identifier for the metadata from the remote repository
     * @param processedEntity  the retrieved entity.
     */
    public void processRetrievedEntitySummary(String        sourceName,
                                              String        metadataCollectionId,
                                              EntitySummary processedEntity)
    {
        /*
         * Discover whether the instance should be learned.
         */
        if (verifyEventToLearn(sourceName, processedEntity))
        {
            refreshRetrievedEntity(sourceName,
                                   metadataCollectionId,
                                   processedEntity.getGUID(),
                                   processedEntity.getType());
        }
    }


    /**
     * Pass an entity that has been retrieved from a remote open metadata repository so it can be validated and
     * (if the rules permit) cached in the local repository.
     *
     * @param sourceName name of the source of this event.
     * @param metadataCollectionId unique identifier for the metadata from the remote repository
     * @param processedEntity the retrieved entity.
     */
    public void processRetrievedEntityDetail(String       sourceName,
                                             String       metadataCollectionId,
                                             EntityDetail processedEntity)
    {
        /*
         * Discover whether the instance should be learned.
         */
        if (verifyEventToLearn(sourceName, processedEntity))
        {
            refreshRetrievedEntity(sourceName,
                                   metadataCollectionId,
                                   processedEntity.getGUID(),
                                   processedEntity.getType());
        }
    }


    /**
     * Pass a relationship that has been retrieved from a remote open metadata repository so it can be validated and
     * (if the rules permit) cached in the local repository.
     *
     * @param sourceName name of the source of this event.
     * @param metadataCollectionId unique identifier for the metadata from the remote repository
     * @param processedRelationship         the retrieved relationship
     */
    public void processRetrievedRelationship(String       sourceName,
                                             String       metadataCollectionId,
                                             Relationship processedRelationship)
    {
        /*
         * Discover whether the instance should be learned.
         */
        if (verifyEventToLearn(sourceName, processedRelationship))
        {
            try
            {
                if (realMetadataCollection.isRelationshipKnown(sourceName, processedRelationship.getGUID()) == null)
                {
                    InstanceType type = processedRelationship.getType();

                    if (type != null)
                    {
                        /*
                         * It would be possible to save the relationship directly into the repository,
                         * but it is possible that some of the properties have been suppressed for the
                         * requesting user Id.  In which case saving it now would result in other users
                         * seeing a restricted view of the relationship.
                         */
                        realMetadataCollection.refreshRelationshipReferenceCopy(localServerName,
                                                                                processedRelationship.getGUID(),
                                                                                type.getTypeDefGUID(),
                                                                                type.getTypeDefName(),
                                                                                metadataCollectionId);
                    }
                }
            }
            catch (Throwable   error)
            {
                final String methodName = "processRetrievedRelationship";

                OMRSAuditCode auditCode = OMRSAuditCode.UNEXPECTED_EXCEPTION_FROM_EVENT;
                auditLog.logRecord(methodName,
                                   auditCode.getLogMessageId(),
                                   auditCode.getSeverity(),
                                   auditCode.getFormattedLogMessage(methodName,
                                                                    sourceName,
                                                                    metadataCollectionId,
                                                                    error.getMessage()),
                                   null,
                                   auditCode.getSystemAction(),
                                   auditCode.getUserAction());
            }
        }
    }


    /*
     * ==============================
     * Private methods
     * ==============================
     */


    /**
     * Update the reference entity in the local repository.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param methodName                     name of the event method
     * @param entityParameterName            name of the parameter that passed the entity.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param entity                         details of the new entity
     */
    private void updateReferenceEntity(String       sourceName,
                                       String       methodName,
                                       String       entityParameterName,
                                       String       originatorMetadataCollectionId,
                                       String       originatorServerName,
                                       EntityDetail entity)
    {
        try
        {
            verifyEventProcessor(methodName);
            repositoryValidator.validateReferenceInstanceHeader(realRepositoryName,
                                                                localMetadataCollectionId,
                                                                entityParameterName,
                                                                entity,
                                                                methodName);

            if (verifyEventToSave(sourceName, entity))
            {
                realMetadataCollection.saveEntityReferenceCopy(sourceName, entity);
            }
        }
        catch (Throwable error)
        {
            OMRSAuditCode auditCode = OMRSAuditCode.UNEXPECTED_EXCEPTION_FROM_EVENT;
            auditLog.logRecord(methodName,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(methodName,
                                                                originatorServerName,
                                                                originatorMetadataCollectionId,
                                                                error.getMessage()),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
        }
    }


    /**
     * Update the reference relationship in the local repository.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param methodName                     name of the event method
     * @param relationshipParameterName            name of the parameter that passed the relationship.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param relationship                   details of the relationship
     */
    private void updateReferenceRelationship(String       sourceName,
                                             String       methodName,
                                             String       relationshipParameterName,
                                             String       originatorMetadataCollectionId,
                                             String       originatorServerName,
                                             Relationship relationship)
    {
        try
        {
            verifyEventProcessor(methodName);
            repositoryValidator.validateReferenceInstanceHeader(realRepositoryName,
                                                                localMetadataCollectionId,
                                                                relationshipParameterName,
                                                                relationship,
                                                                methodName);

            if (verifyEventToSave(sourceName, relationship))
            {
                realMetadataCollection.saveRelationshipReferenceCopy(sourceName, relationship);
            }
        }
        catch (Throwable error)
        {
            OMRSAuditCode auditCode = OMRSAuditCode.UNEXPECTED_EXCEPTION_FROM_EVENT;
            auditLog.logRecord(methodName,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(methodName,
                                                                originatorServerName,
                                                                originatorMetadataCollectionId,
                                                                error.getMessage()),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
        }
    }


    /**
     * Purge a reference copy of an instance from the repository.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param methodName                     name of the purge method
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param typeDefGUID                    unique identifier for this entity's TypeDef
     * @param typeDefName                    name of this entity's TypeDef
     * @param instanceGUID                   unique identifier for the entity
     */
    private void purgeReferenceInstance(String sourceName,
                                        String methodName,
                                        String originatorMetadataCollectionId,
                                        String originatorServerName,
                                        String typeDefGUID,
                                        String typeDefName,
                                        String instanceGUID)
    {
        try
        {
            verifyEventProcessor(methodName);

            realMetadataCollection.purgeEntityReferenceCopy(sourceName,
                                                            instanceGUID,
                                                            typeDefGUID,
                                                            typeDefName,
                                                            originatorMetadataCollectionId);

        }
        catch (Throwable error)
        {
            OMRSAuditCode auditCode = OMRSAuditCode.UNEXPECTED_EXCEPTION_FROM_EVENT;
            auditLog.logRecord(methodName,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(methodName,
                                                                originatorServerName,
                                                                originatorMetadataCollectionId,
                                                                error.getMessage()),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
        }
    }


    /**
     * Validate that this event processor is correctly initialized.
     *
     * @param methodName name of the method being called
     */
    private void verifyEventProcessor(String    methodName)
    {
        if (localMetadataCollectionId == null)
        {
            OMRSErrorCode errorCode    = OMRSErrorCode.NULL_LOCAL_METADATA_COLLECTION;
            String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();

            throw new OMRSLogicErrorException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }

        if (realLocalConnector == null)
        {
            OMRSErrorCode errorCode    = OMRSErrorCode.NO_LOCAL_REPOSITORY;
            String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();

            throw new OMRSLogicErrorException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }

        if (realMetadataCollection == null)
        {
            OMRSErrorCode errorCode    = OMRSErrorCode.NULL_LOCAL_METADATA_COLLECTION;
            String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();

            throw new OMRSLogicErrorException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }

        if (repositoryHelper ==null)
        {
            OMRSErrorCode errorCode    = OMRSErrorCode.NULL_REPOSITORY_HELPER;
            String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();

            throw new OMRSLogicErrorException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }

        if (repositoryValidator == null)
        {
            OMRSErrorCode errorCode    = OMRSErrorCode.NULL_REPOSITORY_VALIDATOR;
            String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();

            throw new OMRSLogicErrorException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }

        if (saveExchangeRule == null)
        {
            OMRSErrorCode errorCode    = OMRSErrorCode.NULL_EXCHANGE_RULE;
            String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new OMRSLogicErrorException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }

        if (realMetadataCollection == null)
        {
            OMRSErrorCode errorCode = OMRSErrorCode.NULL_METADATA_COLLECTION;
            String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(realRepositoryName);

            throw new OMRSLogicErrorException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }


    /**
     * Determine if the event should be processed.
     *
     * @param source identifier of the source of the event.
     * @param instance metadata instance in the event.
     * @return boolean flag indicating whether the event should be sent to the real repository or not.
     */
    private boolean verifyEventToSave(String             source,
                                      InstanceHeader     instance)
    {
        InstanceType   instanceType = instance.getType();

        return ((saveExchangeRule.processInstanceEvent(instance)) &&
                (repositoryValidator.isActiveType(source,
                                                  instanceType.getTypeDefGUID(),
                                                  instanceType.getTypeDefName())));
    }

    /**
     * Determine if the event should be processed.
     *
     * @param source identifier of the source of the event.
     * @param instance metadata instance in the event.
     * @return boolean flag indicating whether the event should be sent to the real repository or not.
     */
    private boolean verifyEventToLearn(String             source,
                                       InstanceHeader     instance)
    {
        InstanceType   instanceType = instance.getType();

        return ((saveExchangeRule.learnInstanceEvent(instance)) &&
                (repositoryValidator.isActiveType(source,
                                                  instanceType.getTypeDefGUID(),
                                                  instanceType.getTypeDefName())));
    }
}
