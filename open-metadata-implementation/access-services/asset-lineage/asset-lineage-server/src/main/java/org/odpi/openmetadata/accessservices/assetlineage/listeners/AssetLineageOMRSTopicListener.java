/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.listeners;


import org.odpi.openmetadata.accessservices.assetlineage.handlers.ContextHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.GlossaryHandler;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.AssetContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.GlossaryTerm;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.RelationshipEvent;
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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.events.*;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.RELATIONAL_COLUMN;
import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.RELATIONAL_TABLE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.SEMANTIC_ASSIGNMENT;

public class AssetLineageOMRSTopicListener implements OMRSTopicListener {


    private static final String assetTypeName = "Asset";
    private static AssetLineageInstanceHandler instanceHandler = new AssetLineageInstanceHandler();
    private static final Logger log = LoggerFactory.getLogger(AssetLineageOMRSTopicListener.class);

    private OMRSRepositoryValidator repositoryValidator;
    private String componentName;
    private List<String> supportedZones;
    private AssetLineagePublisher publisher;
    private String serverName;
    private String serverUserName;


    /**
     * The constructor is given the connection to the out topic for Asset Lineage OMAS
     * along with classes for testing and manipulating instances.
     *  @param assetLineageOutTopic connection to the out topic
     * @param repositoryValidator  provides validation of metadata instance
     * @param componentName        name of component
     * @param supportedZones       list of zones covered by this instance of the access service.
     * @param auditLog             log for errors and information messages
     * @param serverUserName
     * @param serverName
     */
    public AssetLineageOMRSTopicListener(Connection assetLineageOutTopic,
                                         OMRSRepositoryValidator repositoryValidator,
                                         String componentName,
                                         List<String> supportedZones,
                                         OMRSAuditLog auditLog,
                                         String serverUserName,
                                         String serverName) throws OMAGConfigurationErrorException {

        this.repositoryValidator = repositoryValidator;
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

        log.debug("Processing instance event", instanceEvent);

        if (instanceEvent == null) {
            log.debug("Null instance event - ignoring event");
        } else {
            OMRSInstanceEventType instanceEventType = instanceEvent.getInstanceEventType();
            OMRSEventOriginator instanceEventOriginator = instanceEvent.getEventOriginator();

            if (instanceEventOriginator != null) {
                switch (instanceEventType) {
                    case NEW_ENTITY_EVENT:
                        break;

                    case UPDATED_ENTITY_EVENT:
                        processUpdatedEntityEvent(instanceEvent.getOriginalEntity(), instanceEvent.getEntity());
                        break;

                    case DELETED_ENTITY_EVENT:
                        processDeletedEntity(instanceEvent.getEntity());
                        break;

                    case NEW_RELATIONSHIP_EVENT:
                        processRelationship(instanceEvent.getRelationship());
                        break;

                    case UPDATED_RELATIONSHIP_EVENT:
                        processRelationship(instanceEvent.getRelationship());
                        break;
                    case DELETED_RELATIONSHIP_EVENT:
                        processDeletedRelationship(instanceEvent.getRelationship());
                    default:
                        break;
                }
            }

        }
    }


    /**
     * @param relationship - details of the new relationship
     */
    public void processRelationship(Relationship relationship) {

        //It should handle only semantic assignments for relational columns and relational tables
        if (!(relationship.getType().getTypeDefName().equals(SEMANTIC_ASSIGNMENT) &&
                (relationship.getEntityOneProxy().getType().getTypeDefName().equals(RELATIONAL_COLUMN)) ||
                relationship.getEntityOneProxy().getType().getTypeDefName().equals(RELATIONAL_TABLE))) {
            log.info("Event is ignored as the relationship is not a semantic assignment for a column or table");

        } else {
            log.info("Processing semantic assignment relationship event");
                processSemanticAssignment(relationship);
        }
    }


    private void processSemanticAssignment(Relationship relationship){
        RelationshipEvent relationshipEvent = new RelationshipEvent();

        String technicalGuid = relationship.getEntityOneProxy().getGUID();

        try {
            ContextHandler contextHandler = instanceHandler.getContextHandler(serverUserName, serverName);
            AssetContext assetContext = contextHandler.getAssetContext(serverName, serverUserName, technicalGuid);

            GlossaryHandler glossaryHandler = instanceHandler.getGlossaryHandler(serverUserName, serverName);
            GlossaryTerm glossaryTerm = glossaryHandler.getGlossaryTerm(relationship.getEntityTwoProxy(), serverUserName);

            relationshipEvent.setGlossaryTerm(glossaryTerm);
            relationshipEvent.setTypeDefName(relationship.getType().getTypeDefName());
            relationshipEvent.setAssetContext(assetContext);
            relationshipEvent.setOmrsInstanceEventType(OMRSInstanceEventType.NEW_RELATIONSHIP_EVENT);

            publisher.publishRelationshipEvent(relationshipEvent);
        }
        catch (Exception e){  //TODO This catches 20 different possible errors right now.
            log.error(e.getMessage());
        }
    }

    private void processDeletedRelationship(Relationship relationship) {
    }
    private void processUpdatedEntityEvent(EntityDetail originalEntity, EntityDetail entity) {
    }
    private void processDeletedEntity(EntityDetail entity) {
    }

}
