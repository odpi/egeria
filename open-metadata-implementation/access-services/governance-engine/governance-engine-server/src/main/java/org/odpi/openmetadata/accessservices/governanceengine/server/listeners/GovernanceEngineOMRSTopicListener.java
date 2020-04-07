/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server.listeners;

import org.odpi.openmetadata.accessservices.governanceengine.api.events.GovernanceEngineEvent;
import org.odpi.openmetadata.accessservices.governanceengine.api.events.GovernanceEngineEventType;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.errorcode.GovernanceEngineAuditCode;
import org.odpi.openmetadata.accessservices.governanceengine.api.model.GovernedAsset;
import org.odpi.openmetadata.accessservices.governanceengine.server.admin.GovernanceEngineInstanceHandler;
import org.odpi.openmetadata.accessservices.governanceengine.server.handlers.GovernedAssetHandler;
import org.odpi.openmetadata.accessservices.governanceengine.server.publisher.GovernanceEnginePublisher;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListenerBase;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.events.OMRSEventOriginator;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GovernanceEngineOMRSTopicListener extends OMRSTopicListenerBase {

    private static final Logger log = LoggerFactory.getLogger(GovernanceEngineOMRSTopicListener.class);
    private static GovernanceEngineInstanceHandler instanceHandler = new GovernanceEngineInstanceHandler();

    private OMRSRepositoryHelper repositoryHelper;
    private OMRSRepositoryValidator repositoryValidator;
    private String componentName;
    private String serverName;
    private List<String> supportedZones;
    private GovernanceEnginePublisher publisher;

    public GovernanceEngineOMRSTopicListener(OpenMetadataTopicConnector openMetadataTopicConnector,
                                             OMRSRepositoryHelper repositoryHelper,
                                             OMRSRepositoryValidator repositoryValidator,
                                             String componentName,
                                             String serverName,
                                             List<String> supportedZones,
                                             AuditLog auditLog) {
        super(componentName, auditLog);
        this.repositoryHelper = repositoryHelper;
        this.repositoryValidator = repositoryValidator;
        this.componentName = componentName;
        this.serverName = serverName;
        this.supportedZones = supportedZones;
        publisher = new GovernanceEnginePublisher(openMetadataTopicConnector, auditLog);
    }

    /**
     * Unpack and deliver an instance event to the InstanceEventProcessor
     *
     * @param instanceEvent event to unpack
     */
    @Override
    public void processInstanceEvent(OMRSInstanceEvent instanceEvent) {

        if (instanceEvent == null) {
            log.debug("Null instance event - ignoring event");
        } else {
            OMRSInstanceEventType instanceEventType = instanceEvent.getInstanceEventType();
            OMRSEventOriginator instanceEventOriginator = instanceEvent.getEventOriginator();

            if ((instanceEventType == null) || (instanceEventOriginator == null)) {
                return;
            }

            switch (instanceEventType) {
                case CLASSIFIED_ENTITY_EVENT:
                    processClassifiedEntityEvent(instanceEvent);
                    break;
                case RECLASSIFIED_ENTITY_EVENT:
                    this.processReclassifiedEntityEvent(instanceEvent);
                    break;
                case DELETED_ENTITY_EVENT:
                    processDeletePurgedEntityEvent(instanceEvent);
                    break;
                case DECLASSIFIED_ENTITY_EVENT:
                    this.processDeclassifiedEntityEvent(instanceEvent);
                    break;
                default:
                    log.debug("Unknown instance event error code, ignoring event");
                    break;
            }
        }
    }

    public void processClassifiedEntityEvent(OMRSInstanceEvent instanceEvent) {
        String methodName = "processClassifiedEntityEvent";
        EntityDetail entity = instanceEvent.getEntity();
        String eventTypeName = instanceEvent.getInstanceEventType().getName();
        logEvent(eventTypeName, entity);

        try {
            GovernedAssetHandler governedAssetHandler = instanceHandler.getGovernedAssetHandler(componentName, serverName, methodName);
            if (!governedAssetHandler.isSchemaElement(entity.getType()) || !governedAssetHandler.containsGovernedClassification(entity)) {
                logNoProcessEvent(eventTypeName, entity);
                return;
            }

            GovernanceEngineEvent governanceEngineEvent = getGovernanceEngineEvent(entity, GovernanceEngineEventType.NEW_CLASSIFIED_ASSET);
            publisher.publishEvent(governanceEngineEvent);
        } catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e) {
            logExceptionToAudit(instanceEvent, e);
        }
    }

    public void processReclassifiedEntityEvent(OMRSInstanceEvent instanceEvent) {
        String methodName = "processReclassifiedEntityEvent";
        EntityDetail entity = instanceEvent.getEntity();
        String eventTypeName = instanceEvent.getInstanceEventType().getName();
        logEvent(eventTypeName, entity);

        try {
            GovernedAssetHandler governedAssetHandler = instanceHandler.getGovernedAssetHandler(componentName, serverName, methodName);
            if (governedAssetHandler != null && !governedAssetHandler.isSchemaElement(entity.getType())) {
                logNoProcessEvent(eventTypeName, entity);
                return;
            }

            GovernanceEngineEvent governanceEngineEvent = getGovernanceEngineEvent(entity, GovernanceEngineEventType.RE_CLASSIFIED_ASSET);
            publisher.publishEvent(governanceEngineEvent);

        } catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e) {
            logExceptionToAudit(instanceEvent, e);
        }

    }


    public void processDeletePurgedEntityEvent(OMRSInstanceEvent instanceEvent) {
        String methodName = "processDeletePurgedEntityEvent";
        EntityDetail entity = instanceEvent.getEntity();
        String eventTypeName = instanceEvent.getInstanceEventType().getName();
        logEvent(eventTypeName, entity);

        try {
            GovernedAssetHandler governedAssetHandler = instanceHandler.getGovernedAssetHandler(componentName, serverName, methodName);
            if (governedAssetHandler != null &&
                    (!governedAssetHandler.isSchemaElement(entity.getType()) || !governedAssetHandler.containsGovernedClassification(entity))) {
                logNoProcessEvent(eventTypeName, entity);
                return;
            }

            GovernanceEngineEvent governanceEngineEvent = getGovernanceEngineEvent(entity, GovernanceEngineEventType.DELETED_ASSET);
            publisher.publishEvent(governanceEngineEvent);
        } catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e) {
            logExceptionToAudit(instanceEvent, e);
        }

    }

    public void processDeclassifiedEntityEvent(OMRSInstanceEvent instanceEvent) {
        String methodName = "processDeclassifiedEntityEvent";
        EntityDetail entity = instanceEvent.getEntity();
        String eventTypeName = instanceEvent.getInstanceEventType().getName();
        logEvent(eventTypeName, entity);

        try {
            GovernedAssetHandler governedAssetHandler = instanceHandler.getGovernedAssetHandler(componentName, serverName, methodName);

            if (governedAssetHandler != null && !governedAssetHandler.isSchemaElement(entity.getType())) {
                logNoProcessEvent(eventTypeName, entity);
                return;
            }

            GovernanceEngineEvent governanceEngineEvent = getGovernanceEngineEvent(entity, GovernanceEngineEventType.DE_CLASSIFIED_ASSET);
            publisher.publishEvent(governanceEngineEvent);
        } catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e) {
            logExceptionToAudit(instanceEvent, e);
        }

    }

    private GovernanceEngineEvent getGovernanceEngineEvent(EntityDetail entityDetail,
                                                           GovernanceEngineEventType governanceEngineEventType) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "getGovernanceEngineEvent";
        GovernedAssetHandler governedAssetHandler = instanceHandler.getGovernedAssetHandler(componentName, serverName, methodName);

        if (governedAssetHandler == null) {
            return null;
        }

        GovernanceEngineEvent governanceEvent = new GovernanceEngineEvent();
        governanceEvent.setEventType(governanceEngineEventType);
        GovernedAsset governedAsset = governedAssetHandler.convertGovernedAsset(componentName, entityDetail);
        governanceEvent.setGovernedAsset(governedAsset);

        return governanceEvent;
    }

    private void logEvent(String eventType, EntityDetail entityDetail) {
        log.debug("{} received event {} for entity GUID = {} - type = {}",
                componentName, eventType, entityDetail.getGUID(), entityDetail.getType().getTypeDefName());
    }

    private void logNoProcessEvent(String eventTypeName, EntityDetail entityDetail) {
        log.debug("Event received {} for entity GUID = {} - type = {} is not processed by {}",
                eventTypeName, entityDetail.getGUID(), entityDetail.getType().getTypeDefName(), componentName);
    }

    private void logExceptionToAudit(OMRSInstanceEvent instanceEvent, Throwable e) {
        auditLog.logMessage("Governance Engine OMAS is processing an OMRSTopic event.",
                GovernanceEngineAuditCode.EVENT_PROCESSING_ERROR.getMessageDefinition());
    }
}
