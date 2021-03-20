/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.eventmanagement;

import java.util.ArrayList;
import java.util.List;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.events.*;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.InternalOMRSEventProcessingContext;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryeventmapper.OMRSRepositoryEventProcessor;
import org.odpi.openmetadata.repositoryservices.events.future.OMRSFuture;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSLogicErrorException;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OMRSRepositoryEventManager is responsible for managing the distribution of TypeDef and instance events.
 * There is one OMRSRepositoryEventManager for each cohort that the local server is registered with and one for
 * the local repository.
 * <p>
 * Since OMRSRepositoryEventManager sits at the crossroads of the flow of events between the cohorts,
 * the local repository and the enterprise access components, it performs detailed error checking of the
 * event contents to help assure the integrity of the open metadata ecosystem.
 */
public class OMRSRepositoryEventManager extends OMRSRepositoryEventBuilder
{
    private boolean                                   isActive               = false;
    private List<OMRSTypeDefEvent>                    typeDefEventBuffer     = new ArrayList<>();
    private List<BufferedInstanceEvent>               instanceEventBuffer    = new ArrayList<>();
    private List<OMRSTypeDefEventProcessorInterface>  typeDefEventConsumers  = new ArrayList<>();
    private List<OMRSInstanceEventProcessorInterface> instanceEventConsumers = new ArrayList<>();
    private OMRSRepositoryContentValidator            repositoryValidator;   /* set in constructor */
    private OMRSRepositoryEventExchangeRule           exchangeRule;          /* set in constructor */

    /*
     * The audit log provides a verifiable record of the open metadata archives that have been loaded into
     * the open metadata repository.  The Logger is for standard debug.
     */
    private AuditLog auditLog;

    private static final Logger log = LoggerFactory.getLogger(OMRSRepositoryEventManager.class);


    /**
     * Constructor to initialize a repository event manager
     *
     * @param eventManagerName    this is the name of the event manager to use for logging.
     * @param exchangeRule        this is the rule that determines which events are processed.
     * @param repositoryValidator validator class for checking open metadata repository objects and parameters.
     * @param auditLog audit log for this component.
     */
    public OMRSRepositoryEventManager(String                          eventManagerName,
                                      OMRSRepositoryEventExchangeRule exchangeRule,
                                      OMRSRepositoryContentValidator  repositoryValidator,
                                      AuditLog                        auditLog)
    {
        super(eventManagerName);

        this.auditLog = auditLog;

        final String actionDescription = "Initialize OMRS Event Manager";
        final String methodName        = "OMRSRepositoryEventManager";

        auditLog.logMessage(actionDescription,
                            OMRSAuditCode.INITIALIZING_EVENT_MANAGER.getMessageDefinition(eventManagerName));

        /*
         * If the exchangeRule is null, throw exception
         */
        if (exchangeRule == null)
        {
            throw new OMRSLogicErrorException(OMRSErrorCode.NULL_EXCHANGE_RULE.getMessageDefinition(methodName),
                                              this.getClass().getName(),
                                              methodName);
        }

        this.exchangeRule = exchangeRule;

        /*
         * If the repository validator is null, throw an exception
         */
        if (repositoryValidator == null)
        {
            throw new OMRSLogicErrorException(OMRSErrorCode.NULL_REPOSITORY_VALIDATOR.getMessageDefinition(),
                                              this.getClass().getName(),
                                              methodName);
        }

        this.repositoryValidator = repositoryValidator;

        log.debug("New Event Manager");
    }


    /**
     * Adds a new consumer to the list of consumers that the OMRSRepositoryEventManager will notify of
     * any TypeDef events it receives.
     *
     * @param typeDefEventConsumer the new consumer of TypeDef events from other members of the cohort
     */
    public void registerTypeDefProcessor(OMRSTypeDefEventProcessor typeDefEventConsumer)
    {
        final String actionDescription = "Register TypeDef Event Processor";

        auditLog.logMessage(actionDescription,
                            OMRSAuditCode.REGISTERING_EVENT_PROCESSOR.getMessageDefinition(typeDefEventConsumer.getEventProcessorName(),
                                                                                           super.getEventProcessorName()));

        typeDefEventConsumers.add(typeDefEventConsumer);
    }


    /**
     * Adds a new consumer to the list of consumers that the OMRSRepositoryEventManager will notify of
     * any instance events it receives.
     *
     * @param instanceEventConsumer the new consumer of instance events from other members of the cohort
     */
    public void registerInstanceProcessor(OMRSInstanceEventProcessor instanceEventConsumer)
    {
        final String actionDescription = "Register Instance Event Processor";

        auditLog.logMessage(actionDescription,
                            OMRSAuditCode.REGISTERING_EVENT_PROCESSOR.getMessageDefinition(instanceEventConsumer.getEventProcessorName(),
                                                                                           super.getEventProcessorName()));

        instanceEventConsumers.add(instanceEventConsumer);
    }


    /**
     * Adds a new consumer to the list of consumers that the OMRSRepositoryEventManager will notify of
     * any instance events it receives.
     *
     * @param repositoryEventProcessor the new consumer of instance events from other members of the cohort
     */
    public void registerRepositoryEventProcessor(OMRSRepositoryEventProcessor repositoryEventProcessor)
    {
        final String actionDescription = "Register Instance Event Processor";

        auditLog.logMessage(actionDescription,
                            OMRSAuditCode.REGISTERING_EVENT_PROCESSOR.getMessageDefinition(repositoryEventProcessor.getEventProcessorName(),
                                                                                           super.getEventProcessorName()));

        instanceEventConsumers.add(repositoryEventProcessor);
        typeDefEventConsumers.add(repositoryEventProcessor);
    }


    /**
     * Indicate that all of the event processors are registered and it is ready to
     * process events.
     */
    public void start()
    {
        final String actionDescription = "Start OMRS Event Manager";

        auditLog.logMessage(actionDescription,
                            OMRSAuditCode.STARTING_EVENT_MANAGER.getMessageDefinition(super.eventProcessorName,
                                                                                      Integer.toString(typeDefEventConsumers.size()),
                                                                                      Integer.toString(instanceEventConsumers.size())));


        /*
         * Updating this flag will allow new events to flow directly.
         */
        this.isActive = true;


        /*
         * This sends out the buffered events.   There is a possibility that
         * new events may be interlaced, or go ahead of the buffered events.
         * However the receiver should be able to handle this since event buses
         * do not necessarily guarantee delivery order is the same as the sending
         * order.
         */
        this.drainEventBuffers();
    }


    /**
     * Send out all of the buffered events, beginning with the TypeDef events and
     * then the instance events.  Typically the TypeDef events should cover all of the
     * TypeDefs that were added during start up.  These events help to ensure there is
     * consistency in the types used in the cohort.
     * <p>
     * Any instance events typically come from the repository event mapper (for the
     * outbound event manager) or from the cohort (for inbound events)
     */
    private void drainEventBuffers()
    {
        final String actionDescription = "Drain Event Buffers";

        if (!typeDefEventBuffer.isEmpty())
        {
            auditLog.logMessage(actionDescription,
                                OMRSAuditCode.DRAINING_TYPEDEF_EVENT_BUFFER.getMessageDefinition(super.eventProcessorName,
                                                                                                 Integer.toString(typeDefEventBuffer.size())));

            for (OMRSTypeDefEvent event : typeDefEventBuffer)
            {
                if (event != null)
                {
                    this.distributeTypeDefEvent(event);
                }
            }
            typeDefEventBuffer.clear();
        }


        if (!instanceEventBuffer.isEmpty())
        {
            auditLog.logMessage(actionDescription,
                                OMRSAuditCode.DRAINING_INSTANCE_EVENT_BUFFER.getMessageDefinition(super.eventProcessorName,
                                                                                                  Integer.toString(instanceEventBuffer.size())));

            for (BufferedInstanceEvent event : instanceEventBuffer)
            {
                if (event != null)
                {
                    //Clear the async event processing context to ensure that it will only have
                    //results from processing this event
                    
                    InternalOMRSEventProcessingContext.clear();
                    InternalOMRSEventProcessingContext.getInstance().setCurrentMessageId(event.getMessageId());
                    this.distributeInstanceEvent(event.getEvent());
                    //Now that the buffered event has been distributed, we need to update the Future
                    //that the OpenMetadataTopicConnector is monitoring the reflect the state of
                    //any asynchronous event processing that is taking place for this event.
                    
                    //That future is recorded in the BufferedInstanceEvent.
                    
                    //Get OMRSFuture for overall asynchronous processing result for the event
                    OMRSFuture future = InternalOMRSEventProcessingContext.getInstance().getOverallAsyncProcessingResult();
                    
                    //Update the future stored in the BufferedInstanceEvent to delegate its processing
                    //status check to that Future.
                    event.getFuture().setDelegate(future);
                }
            }
            instanceEventBuffer.clear();
        }
    }


    /**
     * Providing the rule allows, send the typeDef event to all registered
     * consumers.
     *
     * @param event formatted event to send
     */
    private void distributeTypeDefEvent(OMRSTypeDefEvent event)
    {
        if (exchangeRule.processTypeDefEvents())
        {
            for (OMRSTypeDefEventProcessorInterface consumer : typeDefEventConsumers)
            {
                consumer.sendTypeDefEvent(super.eventProcessorName, event);
            }
        }
    }


    /**
     * Providing the rule allows, send the instance event to all registered
     * consumers.
     *
     * @param event formatted event to send
     */
    private void distributeInstanceEvent(OMRSInstanceEvent event)
    {
    	boolean validEvent = false;
    	
    	if (event.getInstanceEventType() == OMRSInstanceEventType.BATCH_INSTANCES_EVENT) 
    	{
    	    /*
    		 * A batch instance event is valid and should be processed if all
    	     * references and entities in the contained graph are valid to be processed
    		 */
    		InstanceGraph eventGraph = event.getInstanceBatch();
    		List<EntityDetail> eventEntities = eventGraph.getEntities();
    		List<Relationship> eventRelationships = eventGraph.getRelationships();
    		
    		List<EntityDetail> validEntities = new ArrayList<>();
    		List<Relationship> validRelationships = new ArrayList<>();
    		
    		for (EntityDetail entity: eventEntities)
    		{
    			if (exchangeRule.processInstanceEvent(entity))
    			{
    				validEntities.add(entity);
    			}
    		}
    		
    		
    		for (Relationship relationship: eventRelationships)
    		{
    			if (exchangeRule.processInstanceEvent(relationship))
    			{
    				validRelationships.add(relationship);
    			}
    		}
    		
    		if (validEntities.size() > 0 || validRelationships.size() > 0)
    		{
    		    /*
    			 * Can't just update the instance graph on the event, so we'll
    			 * construct a new event with the updated instances and adjust...
    			 */
    			InstanceGraph validInstanceGraph = new InstanceGraph(validEntities, validRelationships);
    	        OMRSInstanceEvent validInstanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.BATCH_INSTANCES_EVENT,
                                                                             validInstanceGraph);
    	        validInstanceEvent.setEventOriginator(event.getEventOriginator());
    	        event = validInstanceEvent;
    	        validEvent = true;
    		}
    		
    	}
    	else
        {
    		validEvent = exchangeRule.processInstanceEvent(event.getTypeDefGUID(),
                                                           event.getTypeDefName());
        }
    	
    	if (validEvent)
    	{
            for (OMRSInstanceEventProcessorInterface consumer : instanceEventConsumers)
            {
                consumer.sendInstanceEvent(super.eventProcessorName, event);
            }

    	}
    }


    /**
     * Send the TypeDef event to the OMRS Topic connector (providing TypeDef Events are enabled).
     *
     * @param sourceName name of the caller
     * @param typeDefEvent properties of the event to send
     */
    public void sendTypeDefEvent(String           sourceName,
                                 OMRSTypeDefEvent typeDefEvent)
    {
        if (isActive)
        {
            this.distributeTypeDefEvent(typeDefEvent);
        }
        else
        {
            this.typeDefEventBuffer.add(typeDefEvent);
        }
    }


    /**
     * Process the instance event directly.
     *
     * @param sourceName name of the caller
     * @param instanceEvent properties of the event to send
     */
    public void sendInstanceEvent(String            sourceName,
                                  OMRSInstanceEvent instanceEvent)
    {
        if (isActive)
        {
            this.distributeInstanceEvent(instanceEvent);
        }
        else
        {
            //If distributeInstanceEvent() is not being called now and we are just adding
            //the event to the buffer, we still need a way for the OpenMetadataTopicConnector
            //to monitor the state of event processing.  This makes it so that we do not
            //treat the event is being fully processed until it is actually distributed
            //to the instance event consumers.
            //
            //To this, we leverage the asynchronous message processing mechanism and create
            //an OMRSFuture that can be used to monitor the processing state of the event.
            
            //The OMRSFuture is created in the BufferedInstanceEvent.  It is a special type
            //of future ("DelegatingFuture") that initially always returns false when
            //we check to see if the processing has finished.  Once we do start processing
            //the event, that future is updated to delegate to a future that tracks the 
            //state of any asynchronous processing being done for the event.
            
            //Here, we just add the future for the BufferedInstanceEvent to the 
            //OMRSAsyncEventProcessingContext so that the event will not be
            //treated as consumed quite yet.
            
            BufferedInstanceEvent event = new BufferedInstanceEvent(instanceEvent,
                                                                    InternalOMRSEventProcessingContext.getInstance().getCurrentMessageId());
            instanceEventBuffer.add(event);
            InternalOMRSEventProcessingContext context = InternalOMRSEventProcessingContext.getInstance();
            context.addAsyncProcessingResult(event.getFuture());
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
        if (repositoryValidator.validEntity(sourceName, entity))
        {
            super.processNewEntityEvent(sourceName,
                                        originatorMetadataCollectionId,
                                        originatorServerName,
                                        originatorServerType,
                                        originatorOrganizationName,
                                        entity);
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
        if (repositoryValidator.validEntity(sourceName, newEntity))
        {
            super.processUpdatedEntityEvent(sourceName,
                                            originatorMetadataCollectionId,
                                            originatorServerName,
                                            originatorServerType,
                                            originatorOrganizationName,
                                            oldEntity,
                                            newEntity);
        }
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
        if (repositoryValidator.validEntity(sourceName, entity))
        {
            super.processUndoneEntityEvent(sourceName,
                                           originatorMetadataCollectionId,
                                           originatorServerName,
                                           originatorServerType,
                                           originatorOrganizationName,
                                           entity);
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
     * @param classification                 new classification
     */
    public void processClassifiedEntityEvent(String         sourceName,
                                             String         originatorMetadataCollectionId,
                                             String         originatorServerName,
                                             String         originatorServerType,
                                             String         originatorOrganizationName,
                                             EntityDetail   entity,
                                             Classification classification)
    {
        if (repositoryValidator.validEntity(sourceName, entity))
        {
            super.processClassifiedEntityEvent(sourceName,
                                               originatorMetadataCollectionId,
                                               originatorServerName,
                                               originatorServerType,
                                               originatorOrganizationName,
                                               entity,
                                               classification);
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
     * @param originalClassification         classification that was removed
     */
    public void processDeclassifiedEntityEvent(String         sourceName,
                                               String         originatorMetadataCollectionId,
                                               String         originatorServerName,
                                               String         originatorServerType,
                                               String         originatorOrganizationName,
                                               EntityDetail   entity,
                                               Classification originalClassification)
    {
        if (repositoryValidator.validEntity(sourceName, entity))
        {
            super.processDeclassifiedEntityEvent(sourceName,
                                                 originatorMetadataCollectionId,
                                                 originatorServerName,
                                                 originatorServerType,
                                                 originatorOrganizationName,
                                                 entity,
                                                 originalClassification);
        }
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
     * @param originalClassification         classification that was changed
     * @param classification                 new classification
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
        if (repositoryValidator.validEntity(sourceName, entity))
        {
            super.processReclassifiedEntityEvent(sourceName,
                                                 originatorMetadataCollectionId,
                                                 originatorServerName,
                                                 originatorServerType,
                                                 originatorOrganizationName,
                                                 entity,
                                                 originalClassification,
                                                 classification);
        }
    }


    /**
     * An existing entity has been deleted.  This is a soft delete. This means it is still in the repository
     * but it is no longer returned on queries.
     *
     * All relationships to the entity are also soft-deleted and will no longer be usable.  These deleted relationships
     * will be notified through separate events.
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
        if (repositoryValidator.validEntity(sourceName, entity))
        {
            super.processDeletedEntityEvent(sourceName,
                                            originatorMetadataCollectionId,
                                            originatorServerName,
                                            originatorServerType,
                                            originatorOrganizationName,
                                            entity);
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
        if (repositoryValidator.validEntity(sourceName, entity))
        {
            super.processRestoredEntityEvent(sourceName,
                                             originatorMetadataCollectionId,
                                             originatorServerName,
                                             originatorServerType,
                                             originatorOrganizationName,
                                             entity);
        }
    }


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
    public void processPurgedEntityEvent(String       sourceName,
                                         String       originatorMetadataCollectionId,
                                         String       originatorServerName,
                                         String       originatorServerType,
                                         String       originatorOrganizationName,
                                         EntityDetail entity)
    {
        if (repositoryValidator.validEntity(sourceName, entity))
        {
            super.processPurgedEntityEvent(sourceName,
                                           originatorMetadataCollectionId,
                                           originatorServerName,
                                           originatorServerType,
                                           originatorOrganizationName,
                                           entity);
        }
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
        if (repositoryValidator.validInstanceId(sourceName,
                                                typeDefGUID,
                                                typeDefName,
                                                TypeDefCategory.ENTITY_DEF,
                                                instanceGUID))
        {
            super.processPurgedEntityEvent(sourceName,
                                           originatorMetadataCollectionId,
                                           originatorServerName,
                                           originatorServerType,
                                           originatorOrganizationName,
                                           typeDefGUID,
                                           typeDefName,
                                           instanceGUID);
        }
    }


    /**
     * An existing entity has been deleted.  This is a soft delete. This means it is still in the repository
     * but it is no longer returned on queries.
     *
     * All relationships to the entity are also deleted and purged and will no longer be usable.  These deleted relationships
     * will be notified through separate events.
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
    public void processDeletePurgedEntityEvent(String       sourceName,
                                               String       originatorMetadataCollectionId,
                                               String       originatorServerName,
                                               String       originatorServerType,
                                               String       originatorOrganizationName,
                                               EntityDetail entity)
    {
        if (repositoryValidator.validEntity(sourceName, entity))
        {
            super.processDeletePurgedEntityEvent(sourceName,
                                                 originatorMetadataCollectionId,
                                                 originatorServerName,
                                                 originatorServerType,
                                                 originatorOrganizationName,
                                                 entity);
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
        if (repositoryValidator.validEntity(sourceName, entity))
        {
            super.processReIdentifiedEntityEvent(sourceName,
                                                 originatorMetadataCollectionId,
                                                 originatorServerName,
                                                 originatorServerType,
                                                 originatorOrganizationName,
                                                 originalEntityGUID,
                                                 entity);
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
     * @param originalTypeDefSummary         details of this entity's original TypeDef.
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
        if (repositoryValidator.validEntity(sourceName, entity))
        {
            super.processReTypedEntityEvent(sourceName,
                                            originatorMetadataCollectionId,
                                            originatorServerName,
                                            originatorServerType,
                                            originatorOrganizationName,
                                            originalTypeDefSummary,
                                            entity);
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
     * @param originalHomeMetadataCollectionId unique identifier for the original home metadata collection/repository.
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
        if (repositoryValidator.validEntity(sourceName, entity))
        {
            super.processReHomedEntityEvent(sourceName,
                                            originatorMetadataCollectionId,
                                            originatorServerName,
                                            originatorServerType,
                                            originatorOrganizationName,
                                            originalHomeMetadataCollectionId,
                                            entity);
        }
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
        if (repositoryValidator.validInstanceId(sourceName,
                                                typeDefGUID,
                                                typeDefName,
                                                TypeDefCategory.ENTITY_DEF,
                                                instanceGUID))
        {
            super.processRefreshEntityRequested(sourceName,
                                                originatorMetadataCollectionId,
                                                originatorServerName,
                                                originatorServerType,
                                                originatorOrganizationName,
                                                typeDefGUID,
                                                typeDefName,
                                                instanceGUID,
                                                homeMetadataCollectionId);
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
        if (repositoryValidator.validEntity(sourceName, entity))
        {
            super.processRefreshEntityEvent(sourceName,
                                            originatorMetadataCollectionId,
                                            originatorServerName,
                                            originatorServerType,
                                            originatorOrganizationName,
                                            entity);
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
     * @param relationship                   details of the new relationship.
     */
    public void processNewRelationshipEvent(String       sourceName,
                                            String       originatorMetadataCollectionId,
                                            String       originatorServerName,
                                            String       originatorServerType,
                                            String       originatorOrganizationName,
                                            Relationship relationship)
    {
        if (repositoryValidator.validRelationship(sourceName, relationship))
        {
            super.processNewRelationshipEvent(sourceName,
                                              originatorMetadataCollectionId,
                                              originatorServerName,
                                              originatorServerType,
                                              originatorOrganizationName,
                                              relationship);
        }
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
        if (repositoryValidator.validRelationship(sourceName, newRelationship))
        {
            super.processUpdatedRelationshipEvent(sourceName,
                                                  originatorMetadataCollectionId,
                                                  originatorServerName,
                                                  originatorServerType,
                                                  originatorOrganizationName,
                                                  oldRelationship,
                                                  newRelationship);
        }
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
        if (repositoryValidator.validRelationship(sourceName, relationship))
        {
            super.processUndoneRelationshipEvent(sourceName,
                                                 originatorMetadataCollectionId,
                                                 originatorServerName,
                                                 originatorServerType,
                                                 originatorOrganizationName,
                                                 relationship);
        }
    }


    /**
     * An existing relationship has been deleted.  This is a soft delete. This means it is still in the repository
     * but it is no longer returned on queries.
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
        if (repositoryValidator.validRelationship(sourceName, relationship))
        {
            super.processDeletedRelationshipEvent(sourceName,
                                                  originatorMetadataCollectionId,
                                                  originatorServerName,
                                                  originatorServerType,
                                                  originatorOrganizationName,
                                                  relationship);
        }
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
        if (repositoryValidator.validRelationship(sourceName, relationship))
        {
            super.processRestoredRelationshipEvent(sourceName,
                                                   originatorMetadataCollectionId,
                                                   originatorServerName,
                                                   originatorServerType,
                                                   originatorOrganizationName,
                                                   relationship);
        }
    }


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
        if (repositoryValidator.validRelationship(sourceName, relationship))
        {
            super.processPurgedRelationshipEvent(sourceName,
                                                 originatorMetadataCollectionId,
                                                 originatorServerName,
                                                 originatorServerType,
                                                 originatorOrganizationName,
                                                 relationship);
        }
    }


    /**
     * A deleted relationship has been permanently removed from the repository.  This request can not be undone.
     *
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
        if (repositoryValidator.validInstanceId(sourceName,
                                                typeDefGUID,
                                                typeDefName,
                                                TypeDefCategory.RELATIONSHIP_DEF,
                                                instanceGUID))
        {
            super.processPurgedRelationshipEvent(sourceName,
                                                 originatorMetadataCollectionId,
                                                 originatorServerName,
                                                 originatorServerType,
                                                 originatorOrganizationName,
                                                 typeDefGUID,
                                                 typeDefName,
                                                 instanceGUID);
        }
    }


    /**
     * An existing relationship has been deleted.  This is a soft delete. This means it is still in the repository
     * but it is no longer returned on queries.
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
    public void processDeletePurgedRelationshipEvent(String       sourceName,
                                                     String       originatorMetadataCollectionId,
                                                     String       originatorServerName,
                                                     String       originatorServerType,
                                                     String       originatorOrganizationName,
                                                     Relationship relationship)
    {
        if (repositoryValidator.validRelationship(sourceName, relationship))
        {
            super.processDeletePurgedRelationshipEvent(sourceName,
                                                       originatorMetadataCollectionId,
                                                       originatorServerName,
                                                       originatorServerType,
                                                       originatorOrganizationName,
                                                       relationship);
        }
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
        if (repositoryValidator.validRelationship(sourceName, relationship))
        {
            super.processReIdentifiedRelationshipEvent(sourceName,
                                                       originatorMetadataCollectionId,
                                                       originatorServerName,
                                                       originatorServerType,
                                                       originatorOrganizationName,
                                                       originalRelationshipGUID,
                                                       relationship);
        }
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
     * @param originalTypeDefSummary         original details for this relationship's TypeDef.
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
        if (repositoryValidator.validRelationship(sourceName, relationship))
        {
            super.processReTypedRelationshipEvent(sourceName,
                                                  originatorMetadataCollectionId,
                                                  originatorServerName,
                                                  originatorServerType,
                                                  originatorOrganizationName,
                                                  originalTypeDefSummary,
                                                  relationship);
        }
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
    public void processReHomedRelationshipEvent(String       sourceName,
                                                String       originatorMetadataCollectionId,
                                                String       originatorServerName,
                                                String       originatorServerType,
                                                String       originatorOrganizationName,
                                                String       originalHomeMetadataCollection,
                                                Relationship relationship)
    {
        if (repositoryValidator.validRelationship(sourceName, relationship))
        {
            super.processReHomedRelationshipEvent(sourceName,
                                                  originatorMetadataCollectionId,
                                                  originatorServerName,
                                                  originatorServerType,
                                                  originatorOrganizationName,
                                                  originalHomeMetadataCollection,
                                                  relationship);
        }
    }


    /**
     * A repository has requested the home repository of a relationship send details of the relationship so
     * its local metadata collection can create a reference copy of the instance.
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
        if (repositoryValidator.validInstanceId(sourceName,
                                                typeDefGUID,
                                                typeDefName,
                                                TypeDefCategory.RELATIONSHIP_DEF,
                                                instanceGUID))
        {
            super.processRefreshRelationshipRequest(sourceName,
                                                    originatorMetadataCollectionId,
                                                    originatorServerName,
                                                    originatorServerType,
                                                    originatorOrganizationName,
                                                    typeDefGUID,
                                                    typeDefName,
                                                    instanceGUID,
                                                    homeMetadataCollectionId);
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
        if (repositoryValidator.validRelationship(sourceName, relationship))
        {
            super.processRefreshRelationshipEvent(sourceName,
                                                  originatorMetadataCollectionId,
                                                  originatorServerName,
                                                  originatorServerType,
                                                  originatorOrganizationName,
                                                  relationship);
        }
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
        if (instances != null)
        {
            List<EntityDetail>  validatedEntities = new ArrayList<>();
            List<Relationship>  validatedRelationships = new ArrayList<>();

            if (instances.getEntities() != null)
            {
                for (EntityDetail entity : instances.getEntities())
                {
                    if (repositoryValidator.validEntity(sourceName, entity))
                    {
                        validatedEntities.add(entity);
                    }
                }
            }

            if (instances.getRelationships() != null)
            {
                for (Relationship relationship : instances.getRelationships())
                {
                    if (repositoryValidator.validRelationship(sourceName, relationship))
                    {
                        validatedRelationships.add(relationship);
                    }
                }
            }

            if (!(validatedEntities.isEmpty() && validatedRelationships.isEmpty()))
            {
                InstanceGraph       validatedInstances = new InstanceGraph();

                validatedInstances.setEntities(validatedEntities);
                validatedInstances.setRelationships(validatedRelationships);

                super.processInstanceBatchEvent(sourceName,
                                                originatorMetadataCollectionId,
                                                originatorServerName,
                                                originatorServerType,
                                                originatorOrganizationName,
                                                validatedInstances);
            }
        }
    }


    /**
     * A remote repository has detected two metadata instances with the same identifier (guid).  One of these instances
     * has its home in the repository and the other is located in a metadata collection owned by another
     * repository in the cohort.  This is a serious error because it could lead to corruption of the metadata collection.
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
     * @param errorMessage                   description of the error
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
        if ((repositoryValidator.validTypeDefSummary(sourceName, targetTypeDefSummary)) &&
                (repositoryValidator.validTypeDefSummary(sourceName, otherTypeDefSummary)))
        {
            if ((exchangeRule.processInstanceEvent(targetTypeDefSummary)) ||
                    (exchangeRule.processInstanceEvent(otherTypeDefSummary)))
            {
                super.processConflictingInstancesEvent(sourceName,
                                                       originatorMetadataCollectionId,
                                                       originatorServerName,
                                                       originatorServerType,
                                                       originatorOrganizationName,
                                                       targetMetadataCollectionId,
                                                       targetTypeDefSummary,
                                                       targetInstanceGUID,
                                                       otherMetadataCollectionId,
                                                       otherOrigin,
                                                       otherTypeDefSummary,
                                                       otherInstanceGUID,
                                                       errorMessage);
            }
        }
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
     * @param otherTypeDefSummary            details of the other's TypeDef
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
        if (exchangeRule.processInstanceEvent(targetTypeDefSummary))
        {
            super.processConflictingTypeEvent(sourceName,
                                              originatorMetadataCollectionId,
                                              originatorServerName,
                                              originatorServerType,
                                              originatorOrganizationName,
                                              targetMetadataCollectionId,
                                              targetTypeDefSummary,
                                              targetInstanceGUID,
                                              otherTypeDefSummary,
                                              errorMessage);
        }
    }
}
