/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server.listeners;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListener;
import org.odpi.openmetadata.repositoryservices.events.OMRSEventOriginator;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventProcessor;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventType;
import org.odpi.openmetadata.repositoryservices.events.OMRSRegistryEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSTypeDefEvent;
import org.odpi.openmetadata.repositoryservices.events.beans.v1.OMRSEventV1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GovernanceEngineOMRSTopicListener implements OMRSTopicListener {

    private static final Logger log = LoggerFactory.getLogger(GovernanceEngineOMRSTopicListener.class);

    private OMRSInstanceEventProcessor instanceEventProcessor;
    private OMRSAuditLog auditLog;

    public GovernanceEngineOMRSTopicListener(OMRSInstanceEventProcessor instanceEventProcessor, OMRSAuditLog auditLog) {
        this.instanceEventProcessor = instanceEventProcessor;
        this.auditLog = auditLog;
    }

    @Override
    public void processRegistryEvent(OMRSRegistryEvent omrsRegistryEvent) {
    }

    @Override
    public void processTypeDefEvent(OMRSTypeDefEvent event) {
    }

    public void processEvent(OMRSEventV1 event) {
        String actionDescription = "processEvent";

        if (event != null) {
            switch (event.getEventCategory()) {
                case INSTANCE:
                    this.processInstanceEvent(new OMRSInstanceEvent(event));
                    break;
                case REGISTRY:
                case TYPEDEF:
                default:
                    break;

            }
        } else {
            OMRSAuditCode auditCode = OMRSAuditCode.NULL_OMRS_EVENT_RECEIVED;

            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
        }
    }

    /**
     * @param instanceEvent - the event coming from enterprise topic
     */
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
                case NEW_RELATIONSHIP_EVENT:
                    instanceEventProcessor.processNewRelationshipEvent("EnterpriseOMRSTopic",
                            instanceEventOriginator.getMetadataCollectionId(),
                            instanceEventOriginator.getServerName(),
                            instanceEventOriginator.getServerType(),
                            instanceEventOriginator.getOrganizationName(),
                            instanceEvent.getRelationship());
                    break;
                case CLASSIFIED_ENTITY_EVENT:
                    instanceEventProcessor.processClassifiedEntityEvent("EnterpriseOMRSTopic",
                            instanceEventOriginator.getMetadataCollectionId(),
                            instanceEventOriginator.getServerName(),
                            instanceEventOriginator.getServerType(),
                            instanceEventOriginator.getOrganizationName(),
                            instanceEvent.getEntity());
                    break;
                case RECLASSIFIED_ENTITY_EVENT:
                    instanceEventProcessor.processReclassifiedEntityEvent("EnterpriseOMRSTopic",
                            instanceEventOriginator.getMetadataCollectionId(),
                            instanceEventOriginator.getServerName(),
                            instanceEventOriginator.getServerType(),
                            instanceEventOriginator.getOrganizationName(),
                            instanceEvent.getEntity());

                default:

            }
        }
    }
}
