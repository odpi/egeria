/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.informationview.connectors;


import org.odpi.openmetadata.accessservices.informationview.events.InformationViewEvent;
import org.odpi.openmetadata.accessservices.informationview.listeners.InformationViewListener;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class InformationViewTopicConnector extends ConnectorBase implements InformationViewTopic {

    private List<InformationViewListener> topicListeners = new ArrayList<>();

    private final Logger log = LoggerFactory.getLogger(InformationViewTopicConnector.class);
    /**
     * Simple constructor
     */
    public InformationViewTopicConnector() {
    }

    /**
     * Pass an event that has been received on the topic to each of the registered listeners.
     *
     * @param event - InformationViewEvent to distribute
     */
    protected void distributeEvent(InformationViewEvent event) {
        for (InformationViewListener topicListener : topicListeners) {
            topicListener.processEvent(event);
        }
    }


    /**
     * Register a listener object.  This object will be supplied with all of the events received on the topic.
     *
     * @param topicListener - object implementing the InformationViewInTopicListener interface
     */
    public void registerListener(InformationViewListener topicListener) {
        if (topicListener != null) {
            topicListeners.add(topicListener);
        }
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException - there is a problem disconnecting the connector.
     */
    public void disconnect() throws ConnectorCheckedException {

    }

}
