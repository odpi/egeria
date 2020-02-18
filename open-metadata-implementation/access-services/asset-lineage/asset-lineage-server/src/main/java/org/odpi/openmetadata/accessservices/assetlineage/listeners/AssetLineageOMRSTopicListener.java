/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.listeners;

import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventType;
import org.odpi.openmetadata.accessservices.assetlineage.event.LineageEvent;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.exception.AssetLineageException;
import org.odpi.openmetadata.accessservices.assetlineage.outtopic.AssetLineagePublisher;
import org.odpi.openmetadata.accessservices.assetlineage.util.Converter;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.events.OMRSEventOriginator;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventType;
import org.odpi.openmetadata.repositoryservices.events.OMRSRegistryEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSTypeDefEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.*;

/**
 * AssetLineageOMRSTopicListener received details of each OMRS event from the cohorts that the local server
 * is connected to.  It passes Lineage Entity events to the publisher.
 */
public class AssetLineageOMRSTopicListener implements OMRSTopicListener {

    private static final Logger log = LoggerFactory.getLogger(AssetLineageOMRSTopicListener.class);

    private AssetLineagePublisher publisher;

    private Converter converter = new Converter();

    /**
     * The constructor is given the connection to the out topic for Asset Lineage OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param serverName        name of this server instance
     * @param serverUserName    name of the user of the server instance
     * @param repositoryHelper  helper object for building and querying TypeDefs and metadata instances
     * @param outTopicConnector The connector used for the Asset Lineage OMAS Out Topic
     */
    public AssetLineageOMRSTopicListener(String serverName, String serverUserName, OMRSRepositoryHelper repositoryHelper, OpenMetadataTopicConnector outTopicConnector)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        this.publisher = new AssetLineagePublisher(serverName, serverUserName, repositoryHelper, outTopicConnector);
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

        if (!immutableValidLineageEventEntities.contains(entityDetail.getType().getTypeDefName()))
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
        log.debug("Asset Lineage OMAS start processing events for the following entity {}: ", entityDetail.getGUID());

        if (entityDetail.getType().getTypeDefName().equals(PROCESS) && entityDetail.getStatus().getName().equals(VALUE_FOR_ACTIVE))
            processNewEntity(entityDetail);
        else
            processUpdatedEntity(entityDetail);
    }

    private void processUpdatedEntity(EntityDetail entityDetail) {
        log.debug("Asset Lineage OMAS start processing events for updating the following entity: {} ", entityDetail.getGUID());
        LineageEvent event = new LineageEvent();
        event.setLineageEntity(converter.createLineageEntity(entityDetail));
        event.setAssetLineageEventType(AssetLineageEventType.UPDATE_ENTITY_EVENT);
        publisher.publishEvent(event);
    }

    private void processNewEntity(EntityDetail entityDetail) {
        log.debug("Asset Lineage OMAS start processing new entity events for the following entity {}: ", entityDetail.getGUID());

        if (entityDetail.getType().getTypeDefName().equals(PROCESS))
            publisher.publishProcessContext(entityDetail);
        else {
            try {
                publisher.publishAssetContext(entityDetail);
            } catch (InvalidParameterException e) {
                log.error("An invalid parameter exception occurred while processing a NewEntity event", e);
                throw new AssetLineageException(e.getReportedHTTPCode(),
                        e.getReportingClassName(),
                        e.getReportingActionDescription(),
                        e.getErrorMessage(),
                        e.getReportedSystemAction(),
                        e.getReportedUserAction());
            }
        }
    }

    private void processDeleteEntity(EntityDetail entityDetail) {
        log.debug("Asset Lineage OMAS is processing a DeleteEntity event which contains the following entity {}: ", entityDetail.getGUID());
        LineageEvent event = new LineageEvent();
        event.setLineageEntity(converter.createLineageEntity(entityDetail));
        event.setAssetLineageEventType(AssetLineageEventType.DELETE_ENTITY_EVENT);
        publisher.publishEvent(event);
    }

    private void processClassifiedEntityEvent(EntityDetail entityDetail) {
        log.debug("Asset Lineage OMAS is processing a Classified Entity event which contains the following entity {}: ", entityDetail.getGUID());
        publisher.publishClassificationContext(entityDetail);
    }

    private void processReclassifiedEntityEvent(EntityDetail entityDetail) {
        log.debug("Asset Lineage OMAS is processing a ReClassified Entity event which contains the following entity {}: ", entityDetail.getGUID());
    }

    private void processDeclassifiedEntityEvent(EntityDetail entityDetail) {
        log.debug("Asset Lineage OMAS is processing a DeClassified Entity event which contains the following entity {}: ", entityDetail.getGUID());
    }

    private void processNewRelationship(EntityDetail entityDetail) {
        log.debug("Asset Lineage OMAS is processing a NewRelationship event which contains the following entity {}: ", entityDetail.getGUID());
    }

    private void processUpdatedRelationshipEvent(EntityDetail entityDetail) {
        log.debug("Asset Lineage OMAS is processing an UpdatedRelationship event which contains the following entity {}: ", entityDetail.getGUID());
    }

    private void processDeletedRelationshipEvent(EntityDetail entityDetail) {
        log.debug("Asset Lineage OMAS is processing a DeletedRelationship event which contains the following entity {}: ", entityDetail.getGUID());
    }

}
