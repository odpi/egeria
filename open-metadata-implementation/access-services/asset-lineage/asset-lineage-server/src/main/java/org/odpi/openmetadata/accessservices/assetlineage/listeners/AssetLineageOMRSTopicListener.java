/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.listeners;


import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventType;
import org.odpi.openmetadata.accessservices.assetlineage.event.LineageEvent;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.exception.AssetLineageException;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.AssetContextHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.ClassificationHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.GlossaryHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.ProcessContextHandler;
import org.odpi.openmetadata.accessservices.assetlineage.model.AssetContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.outtopic.AssetLineagePublisher;
import org.odpi.openmetadata.accessservices.assetlineage.server.AssetLineageInstanceHandler;
import org.odpi.openmetadata.accessservices.assetlineage.util.Converter;
import org.odpi.openmetadata.accessservices.assetlineage.util.Validator;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.events.OMRSEventOriginator;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventType;
import org.odpi.openmetadata.repositoryservices.events.OMRSRegistryEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSTypeDefEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.PROCESS;
import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.SCHEMA_ELEMENT;
import static org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventType.DELETED_ENTITY_EVENT;
import static org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventType.NEW_ENTITY_EVENT;
import static org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventType.UPDATED_ENTITY_EVENT;

/**
 * AssetLineageOMRSTopicListener received details of each OMRS event from the cohorts that the local server
 * is connected to.  It passes Lineage Entity events to the publisher.
 */
public class AssetLineageOMRSTopicListener implements OMRSTopicListener {

    private static final Logger log = LoggerFactory.getLogger(AssetLineageOMRSTopicListener.class);
    private static AssetLineageInstanceHandler instanceHandler = new AssetLineageInstanceHandler();
    private AssetLineagePublisher publisher;
    private String serverName;
    private String serverUserName;
    private Converter converter = new Converter();
    private Validator validator;

    /**
     * The constructor is given the connection to the out topic for Asset Lineage OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param assetLineageOutTopic connection to the out topic
     * @param repositoryHelper     helper object for building and querying TypeDefs and metadata instances
     * @param auditLog             log for errors and information messages
     * @param serverUserName       name of the user of the server instance
     * @param serverName           name of this server instance
     */
    public AssetLineageOMRSTopicListener(Connection assetLineageOutTopic,
                                         OMRSRepositoryHelper repositoryHelper,
                                         OMRSAuditLog auditLog,
                                         String serverUserName,
                                         String serverName) throws OMAGConfigurationErrorException {

        this.serverName = serverName;
        this.serverUserName = serverUserName;
        publisher = new AssetLineagePublisher(assetLineageOutTopic, auditLog);
        validator = new Validator(repositoryHelper);
    }

    /**
     * Method to pass a Registry event received on topic.
     *
     * @param event inbound event
     */
    public void processRegistryEvent(OMRSRegistryEvent event) {
        log.debug("Ignoring registry event: " + event.toString());
    }


    /**
     * Method to pass a Registry event received on topic.
     *
     * @param event inbound event
     */
    public void processTypeDefEvent(OMRSTypeDefEvent event) {
        log.debug("Ignoring type event: " + event.toString());
    }


    /**
     * Unpack and deliver an instance event to the InstanceEventProcessor
     *
     * @param instanceEvent event to unpack
     */
    public void processInstanceEvent(OMRSInstanceEvent instanceEvent) {

        final String serviceOperationName = "processOMRSInstanceEvent: ";

        log.debug("Processing instance event" + instanceEvent);

        if (instanceEvent == null) {
            log.debug("Null instance event - Asset LIneage OMAS is ignoring the event");
        } else {
            OMRSInstanceEventType instanceEventType = instanceEvent.getInstanceEventType();
            OMRSEventOriginator instanceEventOriginator = instanceEvent.getEventOriginator();

            if (instanceEventOriginator != null) {
                switch (instanceEventType) {
                    case NEW_ENTITY_EVENT:
                        validateEventType(instanceEvent.getEntity(),serviceOperationName + NEW_ENTITY_EVENT.getName());
                        break;
                    case UPDATED_ENTITY_EVENT:
                        processUpdatedEntityEvent(instanceEvent.getEntity(),
                                serviceOperationName + UPDATED_ENTITY_EVENT.getName());
                        break;
                    case DELETED_ENTITY_EVENT:
                        processDeleteEntity(instanceEvent.getEntity(),
                                serviceOperationName + DELETED_ENTITY_EVENT.getName());
                        break;
////                    case NEW_RELATIONSHIP_EVENT:
////                        processNewRelationship(instanceEvent.getEntity(),
////                                serviceOperationName + NEW_RELATIONSHIP_EVENT.getName());
////                        break;
////                    case UPDATED_RELATIONSHIP_EVENT:
////                        processUpdatedRelationshipEvent(instanceEvent.getEntity(),
////                                serviceOperationName + UPDATED_RELATIONSHIP_EVENT.getName());
////                        break;
////                    case DELETED_RELATIONSHIP_EVENT:
////                        processDeletedRelationshipEvent(instanceEvent.getEntity(),
////                                serviceOperationName + DELETED_RELATIONSHIP_EVENT.getName());
////                        break;
////                    case CLASSIFIED_ENTITY_EVENT:
////                        processClassifiedEntityEvent(instanceEvent.getEntity(),
////                                serviceOperationName + CLASSIFIED_ENTITY_EVENT.getName());
////                        break;
////                    case RECLASSIFIED_ENTITY_EVENT:
////                        processReclassifiedEntityEvent(instanceEvent.getEntity(),
////                                serviceOperationName + RECLASSIFIED_ENTITY_EVENT.getName());
////                        break;
////                    case DECLASSIFIED_ENTITY_EVENT:
////                        processDeclassifiedEntityEvent(instanceEvent.getEntity(),
////                                serviceOperationName + DECLASSIFIED_ENTITY_EVENT.getName());
////                        break;
                }
            }

        }
    }

    private void validateEventType(EntityDetail entityDetail, String serviceOperationName){

        if (validator.isValidLineageEntityEvent(entityDetail.getType().getTypeDefName())) {
            processNewEntity(entityDetail,
                    serviceOperationName + NEW_ENTITY_EVENT.getName(),SCHEMA_ELEMENT);
        }
    }

    private void processUpdatedEntityEvent(EntityDetail entityDetail, String serviceOperationName) {

        log.debug("Asset Lineage OMAS start processing events for the following entity {}: ", entityDetail.getGUID());

        if (entityDetail.getType().getTypeDefName().equals(PROCESS) && entityDetail.getStatus().getName().equals("Active")) {
            processNewEntity(entityDetail, serviceOperationName + NEW_ENTITY_EVENT.getName(),PROCESS);
        }
        else{
            processUpdatedEntity(entityDetail,serviceOperationName + UPDATED_ENTITY_EVENT.getName());
        }

    }

    private void processNewEntity(EntityDetail entityDetail, String serviceOperationName,String supertype) {

        final String methodName = "processNewEntity";

        log.debug("Asset Lineage OMAS start processing new entity events for the following entity {}: ", entityDetail.getGUID());

        try {
            if (entityDetail.getType().getTypeDefName().equals(PROCESS)) {
                getContextForProcess(entityDetail, serviceOperationName);
            } else {
                getAssetContext(entityDetail, serviceOperationName);
            }
        } catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e) {
            log.error("Retrieving handler for the access service failed at {}, Exception message is: {}", methodName, e.getMessage());

            throw new AssetLineageException(e.getReportedHTTPCode(),
                    e.getReportingClassName(),
                    e.getReportingActionDescription(),
                    e.getErrorMessage(),
                    e.getReportedSystemAction(),
                    e.getReportedUserAction());
        }
    }


    private void processNewRelationship(EntityDetail entityDetail, String serviceOperationName) {

        final String methodName = "processNewRelationship";

        log.debug("Asset Lineage OMAS start processing events with method {} for the following entity {}: ", methodName, entityDetail.getGUID());

    }

    private void processUpdatedRelationshipEvent(EntityDetail entityDetail, String serviceOperationName) {

        final String methodName = "processUpdatedRelationshipEvent";

        log.debug("Asset Lineage OMAS start processing events with method {} for the following entity {}: ", methodName, entityDetail.getGUID());

    }

    private void processDeletedRelationshipEvent(EntityDetail entityDetail, String serviceOperationName) {

        final String methodName = "processDeletedRelationshipEvent";

        log.debug("Asset Lineage OMAS start processing events with method {} for the following entity {}: ", methodName, entityDetail.getGUID());

    }


    private void processDeleteEntity(EntityDetail entityDetail, String serviceOperationName) {

        log.debug("Asset Lineage OMAS start processing events for deleting the following entity: {} ", entityDetail.getGUID());

        LineageEvent event = new LineageEvent();
        event.setLineageEntity(converter.createLineageEntity(entityDetail));
        event.setAssetLineageEventType(AssetLineageEventType.DELETE_ENTITY_EVENT);

        publisher.publishRelationshipEvent(event);
    }

    private void processClassifiedEntityEvent(EntityDetail entityDetail, String serviceOperationName) {

        String methodName = "processClassifiedEntityEvent";

        log.debug("Asset Lineage OMAS start processing events with method {} for the following entity {}: ", methodName, entityDetail.getGUID());

        final String typeDefName = entityDetail.getType().getTypeDefName();

        try {

            if (validator.isValidLineageEntityEvent(typeDefName)) {
                getClassificationContext(entityDetail, serviceOperationName);

            } else {
                log.info("Event is ignored as the process is not a ready yet");
            }

        } catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e) {
            log.error("Exception in processing the classified entities for the access service failed at {}, " +
                    "Exception message is: {}", methodName, e.getMessage());

            throw new AssetLineageException(e.getReportedHTTPCode(),
                    e.getReportingClassName(),
                    e.getReportingActionDescription(),
                    e.getErrorMessage(),
                    e.getReportedSystemAction(),
                    e.getReportedUserAction());
        }
    }



    private void processReclassifiedEntityEvent(EntityDetail entityDetail, String serviceOperationName) {

        String methodName = "processReclassifiedEntityEvent";

        log.debug("Asset Lineage OMAS start processing events with method {} for the following entity {}: ", methodName, entityDetail.getGUID());


    }
    private void processDeclassifiedEntityEvent(EntityDetail entityDetail, String serviceOperationName) {

        String methodName = "processDeclassifiedEntityEvent";

        log.debug("Asset Lineage OMAS start processing events with method {} for the following entity {}: ", methodName, entityDetail.getGUID());
    }



    private void getClassificationContext(EntityDetail entityDetail, String serviceOperationName) throws
            InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        ClassificationHandler classificationHandler = instanceHandler.getClassificationHandler(serverUserName, serverName, serviceOperationName);
        Map<String, Set<GraphContext>> classificationContext = classificationHandler.getAssetContextByClassification(
                serverName,
                serverUserName,
                entityDetail);

        if (!classificationContext.isEmpty()){
            LineageEvent event = new LineageEvent();
            event.setAssetContext(classificationContext);
            event.setAssetLineageEventType(AssetLineageEventType.CLASSIFICATION_CONTEXT_EVENT);
            publisher.publishRelationshipEvent(event);
        }
    }


    /**
     * Takes the context for a Process and publishes the event to the Cohort
     *
     * @param entityDetail         entity to get context
     * @param serviceOperationName name of the calling operation
     */
    private void getContextForProcess(EntityDetail entityDetail, String serviceOperationName) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {

        ProcessContextHandler processContextHandler = instanceHandler.getProcessHandler(serverUserName, serverName, serviceOperationName);
        Map<String, Set<GraphContext>> processContext = processContextHandler.getProcessContext(serverUserName, entityDetail.getGUID());
        LineageEvent event = new LineageEvent();
        event.setAssetContext(processContext);
        event.setAssetLineageEventType(AssetLineageEventType.PROCESS_CONTEXT_EVENT);
        publisher.publishRelationshipEvent(event);
    }

    private void getAssetContext(EntityDetail entityDetail, String serviceOperationName) throws InvalidParameterException,
                                                                                                                 PropertyServerException,
                                                                                                                 UserNotAuthorizedException {
        String technicalGuid = entityDetail.getGUID();

        AssetContextHandler newAssetContextHandler = instanceHandler.getContextHandler(serverUserName, serverName, serviceOperationName);
        AssetContext assetContext = newAssetContextHandler.getAssetContext(serverUserName, technicalGuid, entityDetail.getType().getTypeDefName());

        GlossaryHandler glossaryHandler = instanceHandler.getGlossaryHandler(serverUserName, serverName, serviceOperationName);
        Map<String, Set<GraphContext>> context = glossaryHandler.getGlossaryTerm(technicalGuid, serviceOperationName, entityDetail, assetContext,validator);

        LineageEvent event = new LineageEvent();
        if (context.size() != 0) {
            event.setAssetContext(context);
        } else {
            event.setAssetContext(assetContext.getNeighbors());
        }
        
        event.setAssetLineageEventType(AssetLineageEventType.TECHNICAL_ELEMENT_CONTEXT_EVENT);
        publisher.publishRelationshipEvent(event);
    }

    private void processUpdatedEntity(EntityDetail entityDetail, String serviceOperationName){

        log.debug("Asset Lineage OMAS start processing events for updating the following entity: {} ", entityDetail.getGUID());

        LineageEvent event = new LineageEvent();
        event.setLineageEntity(converter.createLineageEntity(entityDetail));
        event.setAssetLineageEventType(AssetLineageEventType.UPDATE_ENTITY_EVENT);

        publisher.publishRelationshipEvent(event);
    }
}
