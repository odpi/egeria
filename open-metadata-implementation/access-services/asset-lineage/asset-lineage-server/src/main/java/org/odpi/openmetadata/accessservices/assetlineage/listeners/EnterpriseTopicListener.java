/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.listeners;


import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListener;
import org.odpi.openmetadata.repositoryservices.events.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnterpriseTopicListener implements OMRSTopicListener {


    private static final Logger log = LoggerFactory.getLogger(EnterpriseTopicListener.class);
    private OMRSInstanceEventProcessor instanceEventProcessor;
    private OMRSAuditLog auditLog;

    /**
     *
     * @param instanceEventProcessor
     * @param auditLog
     */
    public EnterpriseTopicListener(OMRSInstanceEventProcessor instanceEventProcessor, OMRSAuditLog auditLog) {

        this.instanceEventProcessor = instanceEventProcessor;
        this.auditLog = auditLog;
    }

    @Override
    public void processRegistryEvent(OMRSRegistryEvent event) {

    }

    @Override
    public void processTypeDefEvent(OMRSTypeDefEvent event) {

    }

    /**
     * @param instanceEvent - the event coming from enterprise topic
     */
    public void processInstanceEvent(OMRSInstanceEvent instanceEvent) {

        log.debug("Processing instance event", instanceEvent);

        if (instanceEvent == null) {
            log.debug("Null instance event - ignoring event");
        } else {
            OMRSInstanceEventType instanceEventType = instanceEvent.getInstanceEventType();
            OMRSEventOriginator instanceEventOriginator = instanceEvent.getEventOriginator();

            if ((instanceEventType != null) && (instanceEventOriginator != null)) {
                switch (instanceEventType) {
                    case NEW_ENTITY_EVENT:
                        //Do nothing. New entities can only be introduced via New_Relationship_event
                        break;

                    case UPDATED_ENTITY_EVENT:
                        instanceEventProcessor.processUpdatedEntityEvent(
                                "EnterpriseOMRSTopic",
                                instanceEventOriginator.getMetadataCollectionId(),
                                instanceEventOriginator.getServerName(),
                                instanceEventOriginator.getServerType(),
                                instanceEventOriginator.getOrganizationName(),
                                instanceEvent.getOriginalEntity(),
                                instanceEvent.getEntity());
                        break;

                    case NEW_RELATIONSHIP_EVENT:
                        instanceEventProcessor.sendInstanceEvent("EnterpriseOMRSTopic", instanceEvent);

                        break;

                    case UPDATED_RELATIONSHIP_EVENT:

                        instanceEventProcessor.sendInstanceEvent("EnterpriseOMRSTopic", instanceEvent);
                        break;
                }
            }

        }
    }
}
