/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.InternalOMRSEventProcessingContext;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSLogicErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OpenMetadataTopicConnector provides the support for the registration of listeners and the distribution of
 * incoming events to the registered listeners.  An implementation of the OpenMetadataTopicConnector needs to
 * extend this class to include the interaction with the eventing/messaging layer.
 * <ul>
 *     <li>
 *         For inbound events it should call the protected distributeEvents() method.
 *     </li>
 *     <li>
 *         For outbound events, callers will invoke the sendEvent() method.
 *     </li>
 *     <li>
 *         When the server no longer needs the topic, it will call disconnect().
 *     </li>
 * </ul>
 */
public abstract class OpenMetadataTopicConnector extends ConnectorBase implements OpenMetadataTopic,
                                                                                  Runnable,
                                                                                  AuditLoggingComponent
{
    private static final Logger       log      = LoggerFactory.getLogger(OpenMetadataTopicConnector.class);

    private static final String       defaultThreadName = "OpenMetadataTopicListener";
    private static final String       defaultTopicName  = "OpenMetadataTopic";

    private volatile boolean keepRunning = false;

    private List<OpenMetadataTopicListener> topicListeners     = new ArrayList<>();
    private String                          listenerThreadName = defaultThreadName;
    private String                          topicName          = defaultTopicName;
    private int                             sleepTime          = 100;

    protected AuditLog auditLog = null;

    /**
     * Simple constructor
     */
    public OpenMetadataTopicConnector()
    {
        super();
    }


    /**
     * Receive an audit log object that can be used to record audit log messages.  The caller has initialized it
     * with the correct component description and log destinations.
     *
     * @param auditLog audit log object
     */
    public void setAuditLog(AuditLog   auditLog)
    {
        this.auditLog = auditLog;
    }

    /**
     * This is the method called by the listener thread when it starts.
     */
    public void run()
    {
        auditLog.logMessage(listenerThreadName,
                            OMRSAuditCode.OPEN_METADATA_TOPIC_LISTENER_START.getMessageDefinition(topicName),
                            this.getConnection().toString());

        while (keepRunning)
        {
            try
            {
                try
                {
                    List<IncomingEvent> receivedEvents = checkForIncomingEvents();

                    if ((receivedEvents != null) && (!receivedEvents.isEmpty()))
                    {
                        for (IncomingEvent event : receivedEvents)
                        {
                            if (event != null)
                            {
                                this.distributeEvent(event);
                            }
                        }
                    }
                }
                catch (Throwable   error)
                {
                    log.error("Bad exception from checkForEvents", error);
                }

                Thread.sleep(sleepTime);
            }
            catch (InterruptedException   wakeUp)
            {
                log.info("Wake up for more events");
            }
        }

        auditLog.logMessage(listenerThreadName,
                            OMRSAuditCode.OPEN_METADATA_TOPIC_LISTENER_SHUTDOWN.getMessageDefinition(topicName),
                           this.getConnection().toString());
    }


    /**
     * Pass an event that has been received on the topic to each of the registered listeners.
     *
     * @param event OMRSEvent to distribute
     */
    private void distributeEvent(IncomingEvent event)
    {
        //Initially clear the async event processing context to ensure that it will only
        //have results from processing this event
        InternalOMRSEventProcessingContext.clear();
        InternalOMRSEventProcessingContext.getInstance().setCurrentMessageId(event.getMessageId());
        for (OpenMetadataTopicListener  topicListener : topicListeners)
        {
            try
            {
                topicListener.processEvent(event.getJson());
            }
            catch (Throwable  error)
            {
                final String   actionDescription = "distributeEvent";

                auditLog.logException(actionDescription,
                                      OMRSAuditCode.EVENT_PROCESSING_ERROR.getMessageDefinition(event.getJson(), error.toString()),
                                      event.getJson(),
                                      error);
            }
        }
        
        //Change the state once all listeners have at least seen the event
        //The listeners may be processing the event asynchronously.  In that case,
        //they will add Futures to the event to allow us to know when the processing
        //is truly complete.
        event.setState(IncomingEventState.DISTRIBUTED_TO_ALL_TOPIC_LISTENERS);
        
        //record any asynchronous processing being done by consumers
        InternalOMRSEventProcessingContext context = InternalOMRSEventProcessingContext.getInstance();
        event.addAsyncProcessingResult(context.getOverallAsyncProcessingResult());
    }


    /**
     * Look to see if there is one of more new events to process.
     *
     * @return a list of received events or null
     */
    protected List<IncomingEvent> checkForIncomingEvents()
    {
        List<IncomingEvent> result = new ArrayList<>();
        for (String event : checkForEvents())
        {
            result.add(new IncomingEvent(event, String.valueOf(event.hashCode())));
        }
        return result;
    }
    
    /**
     * Checks for events.  Only used if checkForIncomingEvents() has not
     * been overridden.
     * 
     * @deprecated Use checkForIncomingEvents() instead.
     * @return list of events
     */
    @Deprecated
    protected List<String> checkForEvents()
    {
        return Collections.emptyList();
    }


    /**
     * Register a listener object.  This object will be supplied with all of the events received on the topic.
     *
     * @param topicListener object implementing the OMRSTopicListener interface
     * @return topic name
     */
    @Override
    public String registerListener(OpenMetadataTopicListener  topicListener)
    {
        if (topicListener != null)
        {
            topicListeners.add(topicListener);

            if (super.connectionProperties != null)
            {
                EndpointProperties endpoint = super.connectionProperties.getEndpoint();

                if (endpoint != null)
                {
                    topicName = endpoint.getAddress();
                }
            }

            return topicName;
        }

        final String methodName = "registerListener";

        throw new OMRSLogicErrorException(OMRSErrorCode.NULL_OPEN_METADATA_TOPIC_LISTENER.getMessageDefinition(listenerThreadName, topicName),
                                          this.getClass().getName(),
                                          methodName);
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        keepRunning = true;

        if (super.connectionProperties != null)
        {
            EndpointProperties endpoint = super.connectionProperties.getEndpoint();

            if (endpoint != null)
            {
                topicName = endpoint.getAddress();
                listenerThreadName = defaultThreadName + ": " + topicName;
            }

            Map<String, Object> configurationProperties = super.connectionProperties.getConfigurationProperties();

            if (configurationProperties != null)
            {
                Object   sleepTime = configurationProperties.get("sleepTime");

                if (sleepTime instanceof Integer)
                {
                    this.sleepTime = (Integer)sleepTime;
                }
            }
        }

        Thread listenerThread = new Thread(this, listenerThreadName);
        listenerThread.start();
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public  void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();

        keepRunning = false;
    }
}
