/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.accessservices.connectedasset.outtopic.ConnectedAssetPublisher;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.events.*;


public class ConnectedAssetOMRSTopicListener implements OMRSTopicListener
{
    private static final Logger log = LoggerFactory.getLogger(ConnectedAssetOMRSTopicListener.class);

    private ConnectedAssetPublisher publisher;


    /**
     * The constructor is given the connection to the out topic for Asset Consumer OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param assetConsumerOutTopic connection to the out topic
     * @param repositoryHelper provides methods for working with metadata instances
     * @param repositoryValidator provides validation of metadata instance
     * @param componentName name of component
     */
    public ConnectedAssetOMRSTopicListener(Connection              assetConsumerOutTopic,
                                           OMRSRepositoryHelper    repositoryHelper,
                                           OMRSRepositoryValidator repositoryValidator,
                                           String                  componentName)
    {
        publisher = new ConnectedAssetPublisher(assetConsumerOutTopic,
                                                repositoryHelper,
                                                repositoryValidator,
                                                componentName);
    }


    /**
     * Method to pass a Registry event received on topic.
     *
     * @param event inbound event
     */
    public void processRegistryEvent(OMRSRegistryEvent event)
    {
        log.debug("Ignoring registry event: " + event.toString());
    }


    /**
     * Method to pass a Registry event received on topic.
     *
     * @param event inbound event
     */
    public void processTypeDefEvent(OMRSTypeDefEvent event)
    {
        log.debug("Ignoring type event: " + event.toString());
    }


    /**
     * Unpack and deliver an instance event to the InstanceEventProcessor
     *
     * @param event inbound
     */
    public void processInstanceEvent(OMRSInstanceEvent  event)
    {
        log.debug("Ignoring type event: " + event.toString());
    }
}
