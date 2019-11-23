/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.listeners;


import org.odpi.openmetadata.accessservices.assetlineage.model.AssetContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.exception.AssetLineageException;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.ClassificationHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.AssetContextHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.GlossaryHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.ProcessContextHandler;
import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventType;
import org.odpi.openmetadata.accessservices.assetlineage.event.LineageEvent;
import org.odpi.openmetadata.accessservices.assetlineage.outtopic.AssetLineagePublisher;
import org.odpi.openmetadata.accessservices.assetlineage.server.AssetLineageInstanceHandler;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.events.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.*;

/**
 * AssetLineageOMRSTopicListener received details of each OMRS event from the cohorts that the local server
 * is connected to.  It passes Lineage Entity events to the publisher.
 */
public class AssetLineageOMRSTopicListener implements OMRSTopicListener {

    private static final Logger log = LoggerFactory.getLogger(AssetLineageOMRSTopicListener.class);
    private static AssetLineageInstanceHandler instanceHandler = new AssetLineageInstanceHandler();
    private OMRSRepositoryValidator repositoryValidator;
    private OMRSRepositoryHelper repositoryHelper;
    private String componentName;
    private List<String> supportedZones;
    private AssetLineagePublisher publisher;
    private String serverName;
    private String serverUserName;

    /**
     * The constructor is given the connection to the out topic for Asset Lineage OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param assetLineageOutTopic connection to the out topic
     * @param repositoryValidator  provides validation of metadata instance
     * @param repositoryHelper     helper object for building and querying TypeDefs and metadata instances
     * @param componentName        name of component
     * @param supportedZones       list of zones covered by this instance of the access service.
     * @param auditLog             log for errors and information messages
     * @param serverUserName       name of the user of the server instance
     * @param serverName           name of this server instance
     */
    public AssetLineageOMRSTopicListener(Connection assetLineageOutTopic,
                                         OMRSRepositoryValidator repositoryValidator,
                                         OMRSRepositoryHelper repositoryHelper,
                                         String componentName,
                                         List<String> supportedZones,
                                         OMRSAuditLog auditLog,
                                         String serverUserName,
                                         String serverName) throws OMAGConfigurationErrorException {

        this.repositoryValidator = repositoryValidator;
        this.repositoryHelper = repositoryHelper;
        this.componentName = componentName;
        this.supportedZones = supportedZones;
        this.serverName = serverName;
        this.serverUserName = serverUserName;
        publisher = new AssetLineagePublisher(assetLineageOutTopic, auditLog);
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

        final String serviceOperationName = "processInstanceEvent";

        log.debug("Processing instance event" + instanceEvent);

        if (instanceEvent == null) {
            log.debug("Null instance event - Asset LIneage OMAS is ignoring the event");
        } else {
            OMRSInstanceEventType instanceEventType = instanceEvent.getInstanceEventType();
            OMRSEventOriginator instanceEventOriginator = instanceEvent.getEventOriginator();

            if (instanceEventOriginator != null) {
                switch (instanceEventType) {
                    case NEW_ENTITY_EVENT:
                        processNewEntityEvent(instanceEvent.getEntity(), serviceOperationName);
                        break;
                    case UPDATED_ENTITY_EVENT:
                        processUpdatedEntityEvent(instanceEvent.getEntity(), serviceOperationName);
                        break;
                    case CLASSIFIED_ENTITY_EVENT:
                        processClassifiedEntityEvent(instanceEvent.getEntity(), serviceOperationName);
                        break;
                    case NEW_RELATIONSHIP_EVENT:
                        break;
//                  case DELETE_PURGED_RELATIONSHIP_EVENT:
//                      processDeletedPurgedRelationship(instanceEvent.getRelationship(), serviceOperationName);
                    default:
                        break;
                }
            }

        }
    }

    /**
     * Takes the entity from NEW ENTITY EVENT and process the event if the type is
     * of specific Types that Asset Lineage OMAS is interested in processing
     *
     * @param entityDetail         event to validate
     * @param serviceOperationName name of the calling operation
     */
    private void processNewEntityEvent(EntityDetail entityDetail, String serviceOperationName) {
        final String typeDefName = entityDetail.getType().getTypeDefName();

        if (!isValidEntityEvent(typeDefName)) {
            log.info(("Event is ignored as the entity is not relevant type for the Asset Lineage OMAS."));
        } else {
            processNewEntity(entityDetail, serviceOperationName);
        }
    }

    private void processUpdatedEntityEvent(EntityDetail entityDetail, String serviceOperationName) {

        if (entityDetail.getType().getTypeDefName().equals(PROCESS) && entityDetail.getStatus().getName().equals("Active")) {
            processNewEntity(entityDetail, serviceOperationName);

        }
    }

    private void processClassifiedEntityEvent(EntityDetail entityDetail, String serviceOperationName) {

        String methodName = "processClassifiedEntityEvent";
        final String typeDefName = entityDetail.getType().getTypeDefName();

        try {
            if (isValidEntityEvent(typeDefName)) {
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

    /**
     * Takes the entity  based on the type of the entity it delegates to the appropriate method
     *
     * @param entityDetail         entity to get context
     * @param serviceOperationName name of the calling operation
     */
    private void processNewEntity(EntityDetail entityDetail, String serviceOperationName) {
        final String methodName = "processNewEntity";

        try {
            //TODO: CHECK PROCESS
            if (entityDetail.getType().getTypeDefName().equals(PROCESS)) {
                getContextForProcess(entityDetail, serviceOperationName);
            } else {
                //TODO: CHECK ASSET CONTEXT
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
        AssetContext assetContext = newAssetContextHandler.getAssetContext(serverName, serverUserName, technicalGuid, entityDetail.getType().getTypeDefName());

        GlossaryHandler glossaryHandler = instanceHandler.getGlossaryHandler(serverUserName, serverName, serviceOperationName);
        Map<String, Set<GraphContext>> context = glossaryHandler.getGlossaryTerm(technicalGuid, serviceOperationName, entityDetail, assetContext);
        //getGlossaryContextForAsset(technicalGuid, serviceOperationName,entityDetail ,assetContext);

        LineageEvent event = new LineageEvent();
        if (context.size() != 0) {
            event.setAssetContext(context);
        } else {
            event.setAssetContext(assetContext.getNeighbors());
        }

        event.setAssetLineageEventType(AssetLineageEventType.TECHNICAL_ELEMENT_CONTEXT_EVENT);
        publisher.publishRelationshipEvent(event);
    }


    private boolean isValidEntityEvent(String typeDefName) {
        final List<String> types = Arrays.asList(GLOSSARY_TERM, TABULAR_SCHEMA_TYPE, TABULAR_COLUMN, RELATIONAL_COLUMN, RELATIONAL_TABLE, DATA_FILE);
        return types.contains(typeDefName);
    }


    /**
     * Only semantic assignments should be handled, and only when columns or tables are involved.
     *
     * @param relationship - details of the new relationship
     * @return
     */
    private boolean isValidRelationshipEvent(Relationship relationship) {
        String entityProxyOneType = relationship.getEntityOneProxy().getType().getTypeDefName();
        if (
                relationship.getType().getTypeDefName().equals(SEMANTIC_ASSIGNMENT)
                        &&
                        (
                                entityProxyOneType.equals(RELATIONAL_COLUMN) || entityProxyOneType.equals(RELATIONAL_TABLE)
                                        || entityProxyOneType.equals(TABULAR_COLUMN)
                        )
        )
            return true;

        final List<String> types = Arrays.asList(PROCESS_PORT, PORT_DELEGATION, PORT_SCHEMA, SCHEMA_TYPE, ATTRIBUTE_FOR_SCHEMA, LINEAGE_MAPPING);

        return types.contains(relationship.getType().getTypeDefName());
    }
}
