/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.listener;

import org.odpi.openmetadata.accessservices.assetlineage.outtopic.AssetLineagePublisher;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.events.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AssetLineageOMRSTopicListener implements OMRSTopicListener {
    private static final Logger log = LoggerFactory.getLogger(AssetLineageOMRSTopicListener.class);

    private AssetLineagePublisher publisher;


    /**
     * The constructor is given the connection to the out topic for Asset Lineage OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param assetConsumerOutTopic connection to the out topic
     * @param repositoryHelper      provides methods for working with metadata instances
     * @param repositoryValidator   provides validation of metadata instance
     * @param componentName         name of component
     */
    public AssetLineageOMRSTopicListener(Connection assetConsumerOutTopic,
                                         OMRSRepositoryConnector repositoryConnector,
                                         OMRSRepositoryHelper repositoryHelper,
                                         OMRSRepositoryValidator repositoryValidator,
                                         String componentName) {
        publisher = new AssetLineagePublisher(assetConsumerOutTopic,
                repositoryConnector,
                repositoryHelper,
                repositoryValidator,
                componentName);
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
/*        log.debug("Processing instance event: " + instanceEvent);

        if (instanceEvent == null)
        {
            log.debug("Null instance event - ignoring event");
        }
        else
        {
            OMRSInstanceEventType instanceEventType       = instanceEvent.getInstanceEventType();
            OMRSEventOriginator   instanceEventOriginator = instanceEvent.getEventOriginator();

            if ((instanceEventType != null) && (instanceEventOriginator != null))
            {
                switch (instanceEventType)
                {
                    case NEW_ENTITY_EVENT:
                        publisher.processNewEntity(instanceEvent.getEntity());
                        break;

                    case UPDATED_ENTITY_EVENT:
                        publisher.processUpdatedEntity(instanceEvent.getOriginalEntity(),
                                                       instanceEvent.getEntity());
                        break;

                    case CLASSIFIED_ENTITY_EVENT:
                        publisher.processUpdatedEntity(instanceEvent.getEntity());
                        break;

                    case RECLASSIFIED_ENTITY_EVENT:
                        publisher.processUpdatedEntity(instanceEvent.getEntity());
                        break;

                    case DECLASSIFIED_ENTITY_EVENT:
                        publisher.processUpdatedEntity(instanceEvent.getEntity());
                        break;

                    case DELETED_ENTITY_EVENT:
                        publisher.processDeletedEntity(instanceEvent.getEntity());
                        break;

                    case PURGED_ENTITY_EVENT:
                        if (log.isDebugEnabled())
                        {
                            log.debug("Ignoring entity purge events");
                        }
                        break;

                    case UNDONE_ENTITY_EVENT:
                        publisher.processUpdatedEntity(instanceEvent.getEntity());
                        break;

                    case RESTORED_ENTITY_EVENT:
                        publisher.processRestoredEntity(instanceEvent.getEntity());
                        break;

                    case REFRESH_ENTITY_REQUEST:
                    case REFRESHED_ENTITY_EVENT:
                    case RE_HOMED_ENTITY_EVENT:
                    case RETYPED_ENTITY_EVENT:
                    case RE_IDENTIFIED_ENTITY_EVENT:
                        if (log.isDebugEnabled())
                        {
                            log.debug("Ignoring entity repository maintenance events");
                        }
                        break;

                    case NEW_RELATIONSHIP_EVENT:
                        publisher.processNewRelationship(instanceEvent.getRelationship());
                        break;

                    case UPDATED_RELATIONSHIP_EVENT:
                        publisher.processUpdatedRelationship(instanceEvent.getOriginalRelationship(),
                                                             instanceEvent.getRelationship());
                        break;

                    case UNDONE_RELATIONSHIP_EVENT:
                        publisher.processUpdatedRelationship(instanceEvent.getRelationship());
                        break;

                    case DELETED_RELATIONSHIP_EVENT:
                        publisher.processDeletedRelationship(instanceEvent.getRelationship());

                        break;

                    case PURGED_RELATIONSHIP_EVENT:
                        if (log.isDebugEnabled())
                        {
                            log.debug("Ignoring relationship purge events");
                        }
                        break;

                    case RESTORED_RELATIONSHIP_EVENT:
                        publisher.processRestoredRelationship(instanceEvent.getRelationship());

                        break;

                    case REFRESH_RELATIONSHIP_REQUEST:
                    case REFRESHED_RELATIONSHIP_EVENT:
                    case RE_IDENTIFIED_RELATIONSHIP_EVENT:
                    case RE_HOMED_RELATIONSHIP_EVENT:
                    case RETYPED_RELATIONSHIP_EVENT:

                        if (log.isDebugEnabled())
                        {
                            log.debug("Ignoring relationship repository maintenance events");
                        }
                        break;

                    case INSTANCE_ERROR_EVENT:

                        if (log.isDebugEnabled())
                        {
                            log.debug("Ignoring instance error events");
                        }
                        break;
                }
            }
            else
            {
                log.debug("Ignored instance event - null type");
            }
        }*/
    }
}
