/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.listeners;


import org.odpi.openmetadata.accessservices.assetlineage.ffdc.exception.AssetLineageException;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.ContextHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.GlossaryHandler;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.*;
import org.odpi.openmetadata.accessservices.assetlineage.outtopic.AssetLineagePublisher;
import org.odpi.openmetadata.accessservices.assetlineage.server.AssetLineageInstanceHandler;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.events.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.*;

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
            log.debug("Null instance event - ignoring event");
        } else {
            OMRSInstanceEventType instanceEventType = instanceEvent.getInstanceEventType();
            OMRSEventOriginator instanceEventOriginator = instanceEvent.getEventOriginator();

            if (instanceEventOriginator != null) {
                switch (instanceEventType) {
                    case NEW_ENTITY_EVENT:
                        processNewEntityEvent(instanceEvent.getEntity());
                        break;
                    case NEW_RELATIONSHIP_EVENT:
                        processRelationship(instanceEvent.getRelationship(), serviceOperationName);
                        break;
//                  case DELETE_PURGED_RELATIONSHIP_EVENT:
//                        processDeletedPurgedRelationship(instanceEvent.getRelationship(), serviceOperationName);
                    default:
                        break;
                }
            }

        }
    }

    /**
     * Takes the entity from NEW ENTITY EVENT and process the event if the type is
     * of specific Types
     *
     * @param entityDetail event to validate
     */
    public void processNewEntityEvent(EntityDetail entityDetail) {

        if (!isValidEntityEvent(entityDetail.getType().getTypeDefName())) {

            log.info(("Event is ignored as the entity is not relevant type for the Asset Lineage OMAS."));
        } else {
            processNewEntity(entityDetail);
        }
    }


    private void processNewEntity(EntityDetail entityDetail) {
        final String methodName = "processNewEntity";
        AssetLineageEntityEvent newEntityEvent = new AssetLineageEntityEvent();

        Map<String, InstancePropertyValue> instancePropertiesMap = entityDetail.getProperties().getInstanceProperties();
        Map<String, Object> properties = new HashMap<>();

        for (Map.Entry<String, InstancePropertyValue> entry : instancePropertiesMap.entrySet()) {

            String key = entry.getKey();
            InstancePropertyValue value = entry.getValue();

            if (value.getInstancePropertyCategory().getName().equalsIgnoreCase("PRIMITIVE")) {
                if (value.getTypeName().equals("int")) {
                    properties.put(key, repositoryHelper.getIntProperty(ASSET_LINEAGE_OMAS, key, entityDetail.getProperties(), methodName)).toString();
                }

                properties.put(key, repositoryHelper.getStringProperty(ASSET_LINEAGE_OMAS, key, entityDetail.getProperties(), methodName));
            }


            if (value.getInstancePropertyCategory().getName().equalsIgnoreCase("ENUM")) {
                EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                properties.put(key, enumPropertyValue.getSymbolicName());
            }

            if (value.getInstancePropertyCategory().getName().equalsIgnoreCase("ARRAY")) {

                properties.put(key, repositoryHelper.getStringArrayProperty(ASSET_LINEAGE_OMAS, key,
                        entityDetail.getProperties(), methodName));

            }

        }

        newEntityEvent.setGUID(entityDetail.getGUID());
        newEntityEvent.setTypeDefName(entityDetail.getType().getTypeDefName());
        newEntityEvent.setCreatedBy(entityDetail.getCreatedBy());
        newEntityEvent.setCreateTime(entityDetail.getCreateTime());
        newEntityEvent.setUpdatedBy(entityDetail.getUpdatedBy());
        newEntityEvent.setUpdateTime(entityDetail.getUpdateTime());
        newEntityEvent.setVersion(entityDetail.getVersion());
        newEntityEvent.setProperties(properties);
        newEntityEvent.setOmrsInstanceEventType(OMRSInstanceEventType.NEW_ENTITY_EVENT);

        publisher.publishRelationshipEvent(newEntityEvent);
    }

    /**
     * @param relationship         - details of the new relationship
     * @param serviceOperationName name of the calling operation
     */
    public void processRelationship(Relationship relationship,
                                    String serviceOperationName) {

        if (!isValidRelationshipEvent(relationship))
            log.info("Event is ignored as the relationship is not a semantic assignment for a column or table");
        else {
            log.info("Processing semantic assignment relationship event");

            final List<String> types = Arrays.asList(PROCESS_PORT, PORT_DELEGATION, PORT_SCHEMA, SCHEMA_TYPE, SCHEMA_ATTRIBUTE_TYPE, ATTRIBUTE_FOR_SCHEMA, LINEAGE_MAPPING);

            if (types.contains(relationship.getType().getTypeDefName())) {
                processRelationships(relationship);

            } else {
                processSemanticAssignment(relationship, serviceOperationName);

            }
        }

    }

    private void processRelationships(Relationship relationship) {

        RelationshipEvent relationshipEvent = new RelationshipEvent();
        Map<String, AssetLineageEntityEvent> proxies = new HashMap<>();

        EntityProxy proxyOne = relationship.getEntityOneProxy();
        EntityProxy proxyTwo = relationship.getEntityTwoProxy();

        proxies.put("EntityProxyOne", proxyToNewEntity(proxyOne));
        proxies.put("EntityProxyTwo", proxyToNewEntity(proxyTwo));

        relationshipEvent.setProxies(proxies);
        relationshipEvent.setTypeDefName(relationship.getType().getTypeDefName());
        relationshipEvent.setGUID(relationship.getGUID());
        relationshipEvent.setCreatedBy(relationship.getCreatedBy());
        relationshipEvent.setCreateTime(relationship.getCreateTime());
        relationshipEvent.setUpdatedBy(relationship.getUpdatedBy());
        relationshipEvent.setUpdateTime(relationship.getUpdateTime());
        relationshipEvent.setVersion(relationship.getVersion());
        relationshipEvent.setOmrsInstanceEventType(OMRSInstanceEventType.NEW_RELATIONSHIP_EVENT);

        publisher.publishRelationshipEvent(relationshipEvent);

    }

    private boolean isValidEntityEvent(String typeDefName) {
        final List<String> types = Arrays.asList(PROCESS, PORT_ALIAS, PORT_IMPLEMENTATION, SCHEMA_ATTRIBUTE_TYPE, TABULAR_SCHEMA_TYPE,
                SCHEMA_ATTRIBUTE, TABULAR_COLUMN_TYPE);

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

        final List<String> types = Arrays.asList(PROCESS_PORT, PORT_DELEGATION, PORT_SCHEMA, SCHEMA_TYPE, SCHEMA_ATTRIBUTE_TYPE, ATTRIBUTE_FOR_SCHEMA, LINEAGE_MAPPING);

        return types.contains(relationship.getType().getTypeDefName());
    }


    /**
     * Takes the entities of the the relationship and builds the context for entityOneProxy
     * and publishes an event.
     *
     * @param relationship - details of the new relationship
     */
    private void processSemanticAssignment(Relationship relationship,
                                           String serviceOperationName) {

        RelationshipEvent relationshipEvent = new RelationshipEvent();

        String technicalGuid = relationship.getEntityOneProxy().getGUID();
        ContextHandler contextHandler = null;
        GlossaryHandler glossaryHandler = null;

        try {
            contextHandler = instanceHandler.getContextHandler(serverUserName, serverName, serviceOperationName);
            glossaryHandler = instanceHandler.getGlossaryHandler(serverUserName, serverName, serviceOperationName);
        } catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e) {
            log.error("Retrieving handler for the access service failed. Exception message is: {}", e.getMessage());
            throw new AssetLineageException(e.getReportedHTTPCode(), e.getReportingClassName(), e.getReportingActionDescription(), e.getErrorMessage(), e.getReportedSystemAction(), e.getReportedUserAction());
        }

        ConvertedAssetContext assetContext = contextHandler.getAssetContext(serverName, serverUserName, technicalGuid);
        GlossaryTerm glossaryTerm = glossaryHandler.getGlossaryTerm(relationship.getEntityTwoProxy(), serverUserName);

        relationshipEvent.setGlossaryTerm(glossaryTerm);
        relationshipEvent.setTypeDefName(relationship.getType().getTypeDefName());
        relationshipEvent.setGUID(relationship.getType().getTypeDefGUID());
        relationshipEvent.setAssetContext(assetContext);
        relationshipEvent.setOmrsInstanceEventType(OMRSInstanceEventType.NEW_RELATIONSHIP_EVENT);

        publisher.publishRelationshipEvent(relationshipEvent);
    }

    private void processDeletedPurgedRelationship(Relationship relationship, String serviceOperationName) {

        if (!isValidRelationshipEvent(relationship))
            log.info("Event is ignored as the relationship is not a semantic assignment for a column or table");
        else {
            log.info("Processing semantic assignment deletion relationship event");
            processSemanticAssignmentDeletion(relationship, serviceOperationName);

        }
    }

    private void processUpdatedEntityEvent(EntityDetail originalEntity, EntityDetail entity) {
    }

    private void processDeletedEntity(EntityDetail entity) {
    }


    /**
     * Takes the entities of the the relationship, extracts the relevant types
     * and publishes an event for deletion.
     *
     * @param relationship - details of the new relationship
     * @return
     */
    private void processSemanticAssignmentDeletion(Relationship relationship, String serviceOperationName) {
        DeletePurgedRelationshipEvent deletionEvent = new DeletePurgedRelationshipEvent();

        GlossaryHandler glossaryHandler = null;
        try {
            glossaryHandler = instanceHandler.getGlossaryHandler(serverUserName, serverName, serviceOperationName);
        } catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e) {
            log.error("Retrieving glossaryHandler for the access setvice failed. Exception message is {}", e.getMessage());
            throw new AssetLineageException(e.getReportedHTTPCode(), e.getReportingClassName(), e.getReportingActionDescription(), e.getErrorMessage(), e.getReportedSystemAction(), e.getReportedUserAction());

        }

        GlossaryTerm glossaryTerm = glossaryHandler.getGlossaryTerm(relationship.getEntityTwoProxy(), serverUserName);

        deletionEvent.setGlossaryTerm(glossaryTerm);
        deletionEvent.setEntityGuid(relationship.getEntityOneProxy().getGUID());
        deletionEvent.setEntityTypeDef(relationship.getEntityOneProxy().getType().getTypeDefName());
        deletionEvent.setOmrsInstanceEventType(OMRSInstanceEventType.DELETE_PURGED_RELATIONSHIP_EVENT);

        publisher.publishRelationshipEvent(deletionEvent);
    }

    private AssetLineageEntityEvent proxyToNewEntity(EntityProxy proxy) {

        final String methodName = "proxyToNewEntity";
        AssetLineageEntityEvent assetLineageEntityEvent = new AssetLineageEntityEvent();
        Map<String, Object> properties = new HashMap<>();

        properties.put("qualifiedName", repositoryHelper.getStringProperty(ASSET_LINEAGE_OMAS, "qualifiedName",
                proxy.getUniqueProperties(), methodName));

        assetLineageEntityEvent.setProperties(properties);
        assetLineageEntityEvent.setGUID(proxy.getGUID());
        assetLineageEntityEvent.setTypeDefName(proxy.getType().getTypeDefName());
        assetLineageEntityEvent.setCreateTime(proxy.getCreateTime());
        assetLineageEntityEvent.setCreatedBy(proxy.getCreatedBy());

        assetLineageEntityEvent.setVersion(proxy.getVersion());

        return assetLineageEntityEvent;
    }
}
