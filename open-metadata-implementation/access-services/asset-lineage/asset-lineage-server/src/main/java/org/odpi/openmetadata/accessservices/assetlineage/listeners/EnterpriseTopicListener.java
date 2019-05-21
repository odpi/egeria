/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.listeners;


import org.odpi.openmetadata.accessservices.assetlineage.outtopic.AssetLineagePublisher;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.events.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class EnterpriseTopicListener implements OMRSTopicListener {


    private static final Logger log = LoggerFactory.getLogger(EnterpriseTopicListener.class);
    private static final String SOURCENAME ="EnterpriseOMRSTopic";
    private OMRSInstanceEventProcessor instanceEventProcessor;
    private OMRSAuditLog auditLog;
    private AssetLineagePublisher assetLineagePublisher;


    /**
     * The constructor is given the connection to the out topic for Asset Lineage OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param assetLineageOutTopic  connection to the out topic
     * @param repositoryHelper  provides methods for working with metadata instances
     * @param repositoryValidator  provides validation of metadata instance
     * @param componentName  name of component
     * @param supportedZones list of zones covered by this instance of the access service.
     * @param auditLog log for errors and information messages
     */
    public EnterpriseTopicListener(Connection assetLineageOutTopic,
                                   OMRSRepositoryHelper repositoryHelper,
                                   OMRSRepositoryValidator repositoryValidator,
                                   String                  componentName,
                                   List<String> supportedZones,
                                   OMRSAuditLog            auditLog) throws OMAGConfigurationErrorException {

        this.auditLog = auditLog;
        assetLineagePublisher = new AssetLineagePublisher(repositoryHelper, assetLineageOutTopic, auditLog);
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
                        break;

                    case UPDATED_ENTITY_EVENT:
                        instanceEventProcessor.processUpdatedEntityEvent(
                                SOURCENAME,
                                instanceEventOriginator.getMetadataCollectionId(),
                                instanceEventOriginator.getServerName(),
                                instanceEventOriginator.getServerType(),
                                instanceEventOriginator.getOrganizationName(),
                                instanceEvent.getOriginalEntity(),
                                instanceEvent.getEntity());
                        break;

                    case DELETED_ENTITY_EVENT:
                        instanceEventProcessor.processUpdatedEntityEvent(
                                SOURCENAME,
                                instanceEventOriginator.getMetadataCollectionId(),
                                instanceEventOriginator.getServerName(),
                                instanceEventOriginator.getServerType(),
                                instanceEventOriginator.getOrganizationName(),
                                instanceEvent.getOriginalEntity(),
                                instanceEvent.getEntity());
                        break;

                    case NEW_RELATIONSHIP_EVENT:
                        instanceEventProcessor.processNewRelationshipEvent(
                                SOURCENAME,
                                instanceEventOriginator.getMetadataCollectionId(),
                                instanceEventOriginator.getServerName(),
                                instanceEventOriginator.getServerType(),
                                instanceEventOriginator.getOrganizationName(),
                                instanceEvent.getRelationship());

                        break;

                    case UPDATED_RELATIONSHIP_EVENT:
                        instanceEventProcessor.sendInstanceEvent(SOURCENAME, instanceEvent);
                        break;

                     default:
                         break;
                }
            }

        }
    }
}
