/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */
package org.odpi.openmetadata.accessservices.securityofficer.server.admin.listener;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
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
    private String enterpriseOMRSTopic = "EnterpriseOMRSTopic";
    private OMRSInstanceEventProcessor instanceEventProcessor;

    public SecurityOfficerOMRSTopicListener(OMRSInstanceEventProcessor instanceEventProcessor, OMRSAuditLog auditLog) {
        this.instanceEventProcessor = instanceEventProcessor;
    }

    @Override
    public void processRegistryEvent(OMRSRegistryEvent omrsRegistryEvent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processTypeDefEvent(OMRSTypeDefEvent event) {
        throw new UnsupportedOperationException();
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

            if (instanceEventType == OMRSInstanceEventType.NEW_RELATIONSHIP_EVENT) {
                instanceEventProcessor.processNewRelationshipEvent(enterpriseOMRSTopic,
                        instanceEventOriginator.getMetadataCollectionId(),
                        instanceEventOriginator.getServerName(),
                        instanceEventOriginator.getServerType(),
                        instanceEventOriginator.getOrganizationName(),
                        instanceEvent.getRelationship());
            }
        }
    }
}