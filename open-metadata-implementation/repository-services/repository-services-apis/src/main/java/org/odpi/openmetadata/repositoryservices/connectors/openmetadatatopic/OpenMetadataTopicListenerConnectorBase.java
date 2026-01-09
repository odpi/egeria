/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.VirtualConnectorExtension;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;

import java.util.List;

/**
 * OpenMetadataTopicListenerConnectorBase is a base class for a connector that is going to embed the OpenMetadataTopicConnector
 * and register a listener with it.  It manages the instances of the OpenMetadataTopicConnector as they are passed as a
 * list of connectors from the ConnectorBroker and registers itself with the OpenMetadataTopicConnector.
 *
 * Subclasses just need to manage their list of listeners and override the processEvent() method.  When it is called, the subclass
 */
public abstract class OpenMetadataTopicListenerConnectorBase extends OpenMetadataTopicConsumerBase implements OpenMetadataTopicListener,
                                                                                                              VirtualConnectorExtension
{

    private static final ObjectReader OBJECT_READER = new ObjectMapper().reader();

    /**
     * Set up the list of connectors that this virtual connector will use to support its interface.
     * The connectors are initialized waiting to start.  When start() is called on the
     * virtual connector, it needs to pass start() to each of the embedded connectors. Similarly for
     * disconnect().
     *
     * @param embeddedConnectors  list of connectors
     */
    public void initializeEmbeddedConnectors(List<Connector> embeddedConnectors)
    {
        super.initializeEmbeddedConnectors(embeddedConnectors);

        if (eventBusConnectors != null)
        {
            for (OpenMetadataTopicConnector eventBusConnector : eventBusConnectors)
            {
                if (eventBusConnector != null)
                {
                    /*
                     * Register this connector as a listener of the event bus connector.
                     */
                    eventBusConnector.registerListener(this);
                }
            }
        }
    }


    /**
     * Method to pass an event received on topic.
     *
     * @param event inbound event
     */
    public abstract void processEvent(String event);


    /**
     * Parse the event and return it to a Java bean.
     *
     * @param event event as a String
     * @param eventClass class of the Java bean.
     * @param <T> the name of the class
     * @return Java bean
     * @throws Exception something went wrong in the parsing process.
     */
    protected <T> T getEventBean(String    event,
                                 Class<T>  eventClass) throws Exception
    {
        /*
         * Parse the string (JSON) event into a bean.
         */

        return OBJECT_READER.readValue(event, eventClass);
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     */
    public  void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();

        /*
         * Each of the event bus connectors need to be disconnected, so they stop receiving inbound events.
         */
        for (OpenMetadataTopicConnector eventBusConnector : eventBusConnectors)
        {
            eventBusConnector.disconnect();
        }
    }
}
