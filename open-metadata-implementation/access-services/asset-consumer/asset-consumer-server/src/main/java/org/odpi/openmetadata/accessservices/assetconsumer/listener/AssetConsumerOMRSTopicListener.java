/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetconsumer.listener;

import org.apache.log4j.Logger;
import org.odpi.openmetadata.accessservices.assetconsumer.outtopic.AssetConsumerPublisher;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.events.OMRSEventOriginator;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventType;
import org.odpi.openmetadata.repositoryservices.events.v1.OMRSEventV1;


public class AssetConsumerOMRSTopicListener implements OMRSTopicListener
{
    private static final Logger log = Logger.getLogger(AssetConsumerOMRSTopicListener.class);

    private AssetConsumerPublisher   publisher;


    /**
     * The constructor is given the connection to the out topic for Asset Consumer OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param assetConsumerOutTopic - connection to the out topic
     * @param repositoryHelper - provides methods for working with metadata instances
     * @param repositoryValidator - provides validation of metadata instance
     * @param componentName - name of component
     */
    public AssetConsumerOMRSTopicListener(Connection              assetConsumerOutTopic,
                                          OMRSRepositoryHelper    repositoryHelper,
                                          OMRSRepositoryValidator repositoryValidator,
                                          String                  componentName)
    {
        publisher = new AssetConsumerPublisher(assetConsumerOutTopic,
                                               repositoryHelper,
                                               repositoryValidator,
                                               componentName);
    }


    /**
     * Method to pass an event received on topic.
     *
     * @param event - inbound event
     */
    public void processEvent(OMRSEventV1 event)
    {
        /*
         * The event should not be null but worth checking.
         */
        if (event != null)
        {
            /*
             * Determine the category of event to process.
             */
            switch (event.getEventCategory())
            {
                case REGISTRY:
                    if (log.isDebugEnabled())
                    {
                        log.debug("Ignoring registry event: " + event.toString());
                    }
                    break;

                case TYPEDEF:
                    if (log.isDebugEnabled())
                    {
                        log.debug("Ignoring type event: " + event.toString());
                    }
                    break;

                case INSTANCE:
                    this.processInstanceEvent(new OMRSInstanceEvent(event));
                    break;

                default:
                    if (log.isDebugEnabled())
                    {
                        log.debug("Unknown event received :|");
                    }
            }
        }
        else
        {
            if (log.isDebugEnabled())
            {
                log.debug("Null OMRS Event received :(");
            }
        }
    }


    /**
     * Unpack and deliver an instance event to the InstanceEventProcessor
     *
     * @param instanceEvent - event to unpack
     */
    private void processInstanceEvent(OMRSInstanceEvent  instanceEvent)
    {
        if (log.isDebugEnabled())
        {
            log.debug("Processing instance event: " + instanceEvent);
        }

        if (instanceEvent == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Null instance event - ignoring event");
            }
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
                if (log.isDebugEnabled())
                {
                    log.debug("Ignored instance event - null type");
                }
            }
        }
    }
}
