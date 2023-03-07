/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securityofficer.server.listener;

import org.odpi.openmetadata.accessservices.securityofficer.api.events.GovernedAssetEvent;
import org.odpi.openmetadata.accessservices.securityofficer.api.events.SecurityOfficerEvent;
import org.odpi.openmetadata.accessservices.securityofficer.api.events.SecurityOfficerEventType;
import org.odpi.openmetadata.accessservices.securityofficer.api.ffdc.SecurityOfficerAuditCode;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.GovernedAsset;
import org.odpi.openmetadata.accessservices.securityofficer.server.handler.GovernedAssetHandler;
import org.odpi.openmetadata.accessservices.securityofficer.server.publisher.SecurityOfficerPublisher;
import org.odpi.openmetadata.accessservices.securityofficer.server.services.SecurityOfficerInstanceHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListenerBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.events.OMRSEventOriginator;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SecurityOfficerOMRSTopicListener extends OMRSTopicListenerBase {

    private static final Logger                         log             = LoggerFactory.getLogger(SecurityOfficerOMRSTopicListener.class);
    private static       SecurityOfficerInstanceHandler instanceHandler = new SecurityOfficerInstanceHandler();

    private OMRSRepositoryHelper     repositoryHelper;
    private OMRSRepositoryValidator  repositoryValidator;
    private String                   componentName;
    private String                   serverName;
    private String                   serverUserId;
    private List<String>             supportedZones;
    private SecurityOfficerPublisher publisher;

    public SecurityOfficerOMRSTopicListener(SecurityOfficerPublisher securityOfficerPublisher,
                                            OMRSRepositoryHelper repositoryHelper,
                                            OMRSRepositoryValidator repositoryValidator,
                                            String componentName,
                                            String serverName,
                                            String serverUserId,
                                            List<String> supportedZones,
                                            AuditLog auditLog) {
        super(componentName, auditLog);
        this.repositoryHelper    = repositoryHelper;
        this.repositoryValidator = repositoryValidator;
        this.componentName       = componentName;
        this.serverName          = serverName;
        this.serverUserId        = serverUserId;
        this.supportedZones      = supportedZones;
        this.publisher           = securityOfficerPublisher;
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
                    // processClassifiedEntityEvent(instanceEvent);
                    break;
                case RECLASSIFIED_ENTITY_EVENT:
                    // this.processReclassifiedEntityEvent(instanceEvent);
                    break;
                case DELETED_ENTITY_EVENT:
                    // processDeletePurgedEntityEvent(instanceEvent);
                    break;
                case DECLASSIFIED_ENTITY_EVENT:
                    // this.processDeclassifiedEntityEvent(instanceEvent);
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
            GovernedAssetHandler governedAssetHandler = instanceHandler.getGovernedAssetHandler(serverUserId, serverName, methodName);
            if (!governedAssetHandler.isSchemaElement(entity.getType()) || !governedAssetHandler.containsGovernedClassification(entity)) {
                logNoProcessEvent(eventTypeName, entity);
                return;
            }

            SecurityOfficerEvent governanceEngineEvent = getGovernanceEngineEvent(entity, SecurityOfficerEventType.NEW_CLASSIFIED_ASSET);
            publisher.publishEvent(governanceEngineEvent);
        } catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e) {
            logExceptionToAudit(methodName, instanceEvent, e);
        }
    }

    public void processReclassifiedEntityEvent(OMRSInstanceEvent instanceEvent) {
        String methodName = "processReclassifiedEntityEvent";
        EntityDetail entity = instanceEvent.getEntity();
        String eventTypeName = instanceEvent.getInstanceEventType().getName();
        logEvent(eventTypeName, entity);

        try {
            GovernedAssetHandler governedAssetHandler = instanceHandler.getGovernedAssetHandler(serverUserId, serverName, methodName);
            if (governedAssetHandler != null && !governedAssetHandler.isSchemaElement(entity.getType())) {
                logNoProcessEvent(eventTypeName, entity);
                return;
            }

            SecurityOfficerEvent governanceEngineEvent = getGovernanceEngineEvent(entity, SecurityOfficerEventType.RE_CLASSIFIED_ASSET);
            publisher.publishEvent(governanceEngineEvent);

        } catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e) {
            logExceptionToAudit(methodName, instanceEvent, e);
        }

    }


    public void processDeletePurgedEntityEvent(OMRSInstanceEvent instanceEvent) {
        String methodName = "processDeletePurgedEntityEvent";
        EntityDetail entity = instanceEvent.getEntity();
        String eventTypeName = instanceEvent.getInstanceEventType().getName();
        logEvent(eventTypeName, entity);

        try {
            GovernedAssetHandler governedAssetHandler = instanceHandler.getGovernedAssetHandler(serverUserId, serverName, methodName);
            if (governedAssetHandler != null &&
                    (!governedAssetHandler.isSchemaElement(entity.getType()) || !governedAssetHandler.containsGovernedClassification(entity))) {
                logNoProcessEvent(eventTypeName, entity);
                return;
            }

            GovernedAssetEvent governanceEngineEvent = getGovernanceEngineEvent(entity, SecurityOfficerEventType.DELETED_ASSET);
            publisher.publishEvent(governanceEngineEvent);
        } catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e) {
            logExceptionToAudit(methodName, instanceEvent, e);
        }

    }

    public void processDeclassifiedEntityEvent(OMRSInstanceEvent instanceEvent) {
        String methodName = "processDeclassifiedEntityEvent";
        EntityDetail entity = instanceEvent.getEntity();
        String eventTypeName = instanceEvent.getInstanceEventType().getName();
        logEvent(eventTypeName, entity);

        try {
            GovernedAssetHandler governedAssetHandler = instanceHandler.getGovernedAssetHandler(serverUserId, serverName, methodName);

            if (governedAssetHandler != null && !governedAssetHandler.isSchemaElement(entity.getType())) {
                logNoProcessEvent(eventTypeName, entity);
                return;
            }

            GovernedAssetEvent governanceEngineEvent = getGovernanceEngineEvent(entity, SecurityOfficerEventType.DE_CLASSIFIED_ASSET);
            publisher.publishEvent(governanceEngineEvent);
        } catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e) {
            logExceptionToAudit(methodName, instanceEvent, e);
        }

    }

    private GovernedAssetEvent getGovernanceEngineEvent(EntityDetail entityDetail,
                                                        SecurityOfficerEventType governanceEngineEventType) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "getGovernanceEngineEvent";
        GovernedAssetHandler governedAssetHandler = instanceHandler.getGovernedAssetHandler(serverUserId, serverName, methodName);

        if (governedAssetHandler == null) {
            return null;
        }

        GovernedAssetEvent governanceEvent = new GovernedAssetEvent();
        governanceEvent.setEventType(governanceEngineEventType);
        GovernedAsset governedAsset = governedAssetHandler.convertGovernedAsset(serverUserId, entityDetail);
        governanceEvent.setGovernedAsset(governedAsset);

        return governanceEvent;
    }

    private void logEvent(String eventType, EntityDetail entityDetail) {
        log.debug("{} received event {} for entity GUID = {} - type = {}",
                  serverUserId, eventType, entityDetail.getGUID(), entityDetail.getType().getTypeDefName());
    }

    private void logNoProcessEvent(String eventTypeName, EntityDetail entityDetail) {
        log.debug("Event received {} for entity GUID = {} - type = {} is not processed by {}",
                  eventTypeName, entityDetail.getGUID(), entityDetail.getType().getTypeDefName(), serverUserId);
    }

    private void logExceptionToAudit(String methodName, OMRSInstanceEvent instanceEvent, Exception error) {
        auditLog.logException(methodName,
                              SecurityOfficerAuditCode.EVENT_PROCESSING_ERROR.getMessageDefinition(instanceEvent.getInstanceEventType().getName()),
                              "instanceEvent {" + instanceEvent.toString() + "}",
                              error);
    }
}
