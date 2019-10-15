/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */
package org.odpi.openmetadata.accessservices.securityofficer.server.admin.listener;

import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListener;
import org.odpi.openmetadata.repositoryservices.events.OMRSEventOriginator;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventProcessor;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventType;
import org.odpi.openmetadata.repositoryservices.events.OMRSRegistryEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSTypeDefEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityOfficerOMRSTopicListener implements OMRSTopicListener {
    private static final Logger log = LoggerFactory.getLogger(SecurityOfficerOMRSTopicListener.class);
    private OMRSInstanceEventProcessor instanceEventProcessor;

    public SecurityOfficerOMRSTopicListener(String serviceName, OMRSInstanceEventProcessor instanceEventProcessor) {
        this.instanceEventProcessor = instanceEventProcessor;
    }


    @Override
    public void processRegistryEvent(OMRSRegistryEvent event) {

    }

    @Override
    public void processTypeDefEvent(OMRSTypeDefEvent event) {

    }

    @Override
    public void processInstanceEvent(OMRSInstanceEvent instanceEvent) {
        if (instanceEventProcessor == null) {
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
                case NEW_RELATIONSHIP_EVENT:
                    instanceEventProcessor.processNewRelationshipEvent(enterpriseOMRSTopic,
                            instanceEventOriginator.getMetadataCollectionId(),
                            instanceEventOriginator.getServerName(),
                            instanceEventOriginator.getServerType(),
                            instanceEventOriginator.getOrganizationName(),
                            instanceEvent.getRelationship());
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