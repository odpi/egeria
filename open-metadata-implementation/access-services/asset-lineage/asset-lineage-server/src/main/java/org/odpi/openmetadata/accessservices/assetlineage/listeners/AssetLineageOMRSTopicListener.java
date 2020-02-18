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
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
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
import static org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventType.*;

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
    private ProcessContextHandler processContextHandler;
    private ClassificationHandler classificationHandler;
    private AssetContextHandler assetContextHandler;

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
                                         String serverName) throws OMAGConfigurationErrorException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        String methodName = "AssetLineageOMRSTopicListener";
        this.serverName = serverName;
        this.serverUserName = serverUserName;
        this.publisher = new AssetLineagePublisher(assetLineageOutTopic, auditLog);
        this.validator = new Validator(repositoryHelper);
        this.processContextHandler = instanceHandler.getProcessHandler(serverUserName, serverName, methodName);
        this.classificationHandler = instanceHandler.getClassificationHandler(serverUserName, serverName, methodName);
        this.assetContextHandler = instanceHandler.getAssetContextHandler(serverUserName, serverName, methodName);

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
            log.debug("Null instance event - Asset Lineage OMAS is ignoring the event");
            return;
        }

        OMRSInstanceEventType instanceEventType = instanceEvent.getInstanceEventType();
        OMRSEventOriginator instanceEventOriginator = instanceEvent.getEventOriginator();

        if (instanceEventOriginator == null)
            return;

        EntityDetail entityDetail = instanceEvent.getEntity();

        if (!validator.isValidLineageEntityEvent(entityDetail.getType().getTypeDefName()))
            return;

        switch (instanceEventType) {
            case NEW_ENTITY_EVENT:
                processNewEntity(entityDetail);
                break;
            case UPDATED_ENTITY_EVENT:
                processUpdatedEntityEvent(entityDetail);
                break;
            case DELETED_ENTITY_EVENT:
                processDeleteEntity(entityDetail);
                break;
            case CLASSIFIED_ENTITY_EVENT:
                processClassifiedEntityEvent(entityDetail);
                break;
            case RECLASSIFIED_ENTITY_EVENT:
                processReclassifiedEntityEvent(entityDetail);
                break;
            case DECLASSIFIED_ENTITY_EVENT:
                processDeclassifiedEntityEvent(entityDetail);
                break;
//            case NEW_RELATIONSHIP_EVENT:
//                processNewRelationship(entityDetail);
//                break;
//            case UPDATED_RELATIONSHIP_EVENT:
//                processUpdatedRelationshipEvent(entityDetail);
//                break;
//            case DELETED_RELATIONSHIP_EVENT:
//                processDeletedRelationshipEvent(entityDetail);
//                break;
        }
    }


    private void processUpdatedEntityEvent(EntityDetail entityDetail) {
        String serviceOperationName = "processUpdatedEntityEvent";
        log.debug("Asset Lineage OMAS start processing events for the following entity {}: ", entityDetail.getGUID());

        if (entityDetail.getType().getTypeDefName().equals(PROCESS) && entityDetail.getStatus().getName().equals("Active")) {
            processNewEntity(entityDetail);
        } else {
            processUpdatedEntity(entityDetail, serviceOperationName + UPDATED_ENTITY_EVENT.getName());
        }

    }

    private void processNewEntity(EntityDetail entityDetail) {
        String serviceOperationName = "processNewEntity";
        final String methodName = "processNewEntity";

        log.debug("Asset Lineage OMAS start processing new entity events for the following entity {}: ", entityDetail.getGUID());

        try {
            if (entityDetail.getType().getTypeDefName().equals(PROCESS)) {
                getContextForProcess(entityDetail);
            } else {
                getAssetContext(entityDetail, serviceOperationName);
            }
        } catch (OCFCheckedExceptionBase e) {
            log.error("Retrieving handler for the access service failed at {}, Exception message is: {}", methodName, e.getMessage());

            throw new AssetLineageException(e.getReportedHTTPCode(),
                    e.getReportingClassName(),
                    e.getReportingActionDescription(),
                    e.getErrorMessage(),
                    e.getReportedSystemAction(),
                    e.getReportedUserAction());
        }
    }


    private void processNewRelationship(EntityDetail entityDetail) {
        final String methodName = "processNewRelationship";
        log.debug("Asset Lineage OMAS start processing events with method {} for the following entity {}: ", methodName, entityDetail.getGUID());

    }

    private void processUpdatedRelationshipEvent(EntityDetail entityDetail) {

        final String methodName = "processUpdatedRelationshipEvent";

        log.debug("Asset Lineage OMAS start processing events with method {} for the following entity {}: ", methodName, entityDetail.getGUID());

    }

    private void processDeletedRelationshipEvent(EntityDetail entityDetail) {

        final String methodName = "processDeletedRelationshipEvent";

        log.debug("Asset Lineage OMAS start processing events with method {} for the following entity {}: ", methodName, entityDetail.getGUID());

    }


    private void processDeleteEntity(EntityDetail entityDetail) {

        log.debug("Asset Lineage OMAS start processing events for deleting the following entity: {} ", entityDetail.getGUID());

        LineageEvent event = new LineageEvent();
        event.setLineageEntity(converter.createLineageEntity(entityDetail));
        event.setAssetLineageEventType(AssetLineageEventType.DELETE_ENTITY_EVENT);

        publisher.publishRelationshipEvent(event);
    }

    private void processClassifiedEntityEvent(EntityDetail entityDetail) {
        log.debug("Asset Lineage OMAS is processing a Classified Entity event which contains the following entity {}: ", entityDetail.getGUID());
        publishClassificationContext(entityDetail);
    }


    private void processReclassifiedEntityEvent(EntityDetail entityDetail) {
        String methodName = "processReclassifiedEntityEvent";
        log.debug("Asset Lineage OMAS start processing events with method {} for the following entity {}: ", methodName, entityDetail.getGUID());
    }

    private void processDeclassifiedEntityEvent(EntityDetail entityDetail) {
        String methodName = "processDeclassifiedEntityEvent";
        log.debug("Asset Lineage OMAS start processing events with method {} for the following entity {}: ", methodName, entityDetail.getGUID());
    }


    private void publishClassificationContext(EntityDetail entityDetail) {
        Map<String, Set<GraphContext>> classificationContext = this.classificationHandler.getAssetContextByClassification(
                serverName,
                serverUserName,
                entityDetail);

        if (classificationContext.isEmpty())
            return;

        LineageEvent event = new LineageEvent();
        event.setAssetContext(classificationContext);
        event.setAssetLineageEventType(AssetLineageEventType.CLASSIFICATION_CONTEXT_EVENT);
        publisher.publishRelationshipEvent(event);
    }


    /**
     * Takes the context for a Process and publishes the event to the Cohort
     *
     * @param entityDetail entity to get context
     */
    private void getContextForProcess(EntityDetail entityDetail) {
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

        AssetContext assetContext = this.assetContextHandler.getAssetContext(serverUserName, technicalGuid, entityDetail.getType().getTypeDefName());

        GlossaryHandler glossaryHandler = instanceHandler.getGlossaryHandler(serverUserName, serverName, serviceOperationName);
        Map<String, Set<GraphContext>> context = glossaryHandler.getGlossaryTerm(technicalGuid, serviceOperationName, entityDetail, assetContext, validator);

        LineageEvent event = new LineageEvent();
        if (context.size() != 0)
            event.setAssetContext(context);
        else
            event.setAssetContext(assetContext.getNeighbors());

        event.setAssetLineageEventType(AssetLineageEventType.TECHNICAL_ELEMENT_CONTEXT_EVENT);
        publisher.publishRelationshipEvent(event);
    }

    private void processUpdatedEntity(EntityDetail entityDetail, String serviceOperationName) {

        log.debug("Asset Lineage OMAS start processing events for updating the following entity: {} ", entityDetail.getGUID());

        LineageEvent event = new LineageEvent();
        event.setLineageEntity(converter.createLineageEntity(entityDetail));
        event.setAssetLineageEventType(AssetLineageEventType.UPDATE_ENTITY_EVENT);

        publisher.publishRelationshipEvent(event);
    }
}
