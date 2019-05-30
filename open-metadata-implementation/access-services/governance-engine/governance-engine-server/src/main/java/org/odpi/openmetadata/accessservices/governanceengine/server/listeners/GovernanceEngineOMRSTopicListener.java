/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server.listeners;

import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListener;
import org.odpi.openmetadata.repositoryservices.events.OMRSEventOriginator;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventProcessor;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventType;
import org.odpi.openmetadata.repositoryservices.events.OMRSRegistryEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSTypeDefEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GovernanceEngineOMRSTopicListener implements OMRSTopicListener {

    private static final Logger log = LoggerFactory.getLogger(GovernanceEngineOMRSTopicListener.class);
    private OMRSInstanceEventProcessor instanceEventProcessor;

    public GovernanceEngineOMRSTopicListener(OMRSInstanceEventProcessor instanceEventProcessor) {
        this.instanceEventProcessor = instanceEventProcessor;
    }


    /**
     * Registry events are ignored by this OMAS
     *
     * @param omrsRegistryEvent event
     */
    public void processRegistryEvent(OMRSRegistryEvent omrsRegistryEvent) {
    }


    /**
     * TypeDef events are ignored by this OMAS
     *
     * @param event inbound event
     */
    public void processTypeDefEvent(OMRSTypeDefEvent event) {
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

            String enterpriseOMRSTopic = "EnterpriseOMRSTopic";
            switch (instanceEventType) {
                case CLASSIFIED_ENTITY_EVENT:
                    instanceEventProcessor.processClassifiedEntityEvent(enterpriseOMRSTopic,
                            instanceEventOriginator.getMetadataCollectionId(),
                            instanceEventOriginator.getServerName(),
                            instanceEventOriginator.getServerType(),
                            instanceEventOriginator.getOrganizationName(),
                            instanceEvent.getEntity());
                    break;
                case RECLASSIFIED_ENTITY_EVENT:
                    instanceEventProcessor.processReclassifiedEntityEvent(enterpriseOMRSTopic,
                            instanceEventOriginator.getMetadataCollectionId(),
                            instanceEventOriginator.getServerName(),
                            instanceEventOriginator.getServerType(),
                            instanceEventOriginator.getOrganizationName(),
                            instanceEvent.getEntity());
                    break;
                case DELETED_ENTITY_EVENT:
                    instanceEventProcessor.processDeletePurgedEntityEvent(enterpriseOMRSTopic,
                            instanceEventOriginator.getMetadataCollectionId(),
                            instanceEventOriginator.getServerName(),
                            instanceEventOriginator.getServerType(),
                            instanceEventOriginator.getOrganizationName(),
                            instanceEvent.getEntity());
                    break;
                case DECLASSIFIED_ENTITY_EVENT:
                    instanceEventProcessor.processDeclassifiedEntityEvent(enterpriseOMRSTopic,
                            instanceEventOriginator.getMetadataCollectionId(),
                            instanceEventOriginator.getServerName(),
                            instanceEventOriginator.getServerType(),
                            instanceEventOriginator.getOrganizationName(),
                            instanceEvent.getEntity());
                    break;
                default:
                    log.debug("Unknown instance event error code, ignoring event");
                    break;
            }
        }
    }
}
