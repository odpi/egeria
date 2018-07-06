/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic;

import org.apache.log4j.Logger;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.AdditionalProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSLogicErrorException;

import java.util.ArrayList;
import java.util.List;


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
public abstract class OpenMetadataTopicConnector extends ConnectorBase implements OpenMetadataTopic, Runnable
{
    private static final Logger       log      = Logger.getLogger(OpenMetadataTopicConnector.class);
    private static final OMRSAuditLog auditLog = new OMRSAuditLog(OMRSAuditingComponent.OPEN_METADATA_TOPIC_CONNECTOR);

    private static final String       defaultThreadName = "OpenMetadataTopicListener";
    private static final String       defaultTopicName  = "OpenMetadataTopic";

    private volatile boolean keepRunning = false;

    private List<OpenMetadataTopicListener> topicListeners     = new ArrayList<>();
    private String                          listenerThreadName = defaultThreadName;
    private String                          topicName          = defaultTopicName;
    private int                             sleepTime          = 100;


    /**
     * Simple constructor
     */
    public OpenMetadataTopicConnector()
    {
        super();
    }


    /**
     * This is the method called by the listener thread when it starts.
     */
    public void run()
    {
        OMRSAuditCode auditCode = OMRSAuditCode.OPEN_METADATA_TOPIC_LISTENER_START;
        auditLog.logRecord(listenerThreadName,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(topicName),
                           this.getConnection().toString(),
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());

        while (keepRunning)
        {
            try
            {
                try
                {
                    List<String> receivedEvents = this.checkForEvents();

                    if ((receivedEvents != null) && (!receivedEvents.isEmpty()))
                    {
                        for (String event : receivedEvents)
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

        auditCode = OMRSAuditCode.OPEN_METADATA_TOPIC_LISTENER_SHUTDOWN;
        auditLog.logRecord(listenerThreadName,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(topicName),
                           this.getConnection().toString(),
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());
    }


    /**
     * Pass an event that has been received on the topic to each of the registered listeners.
     *
     * @param event OMRSEvent to distribute
     */
    private void distributeEvent(String event)
    {
        for (OpenMetadataTopicListener  topicListener : topicListeners)
        {
            try
            {
                topicListener.processEvent(event);
            }
            catch (Throwable  error)
            {
                final String   actionDescription = "distributeEvent";

                OMRSAuditCode auditCode = OMRSAuditCode.EVENT_PROCESSING_ERROR;
                auditLog.logRecord(actionDescription,
                                   auditCode.getLogMessageId(),
                                   auditCode.getSeverity(),
                                   auditCode.getFormattedLogMessage(event, error.toString()),
                                   null,
                                   auditCode.getSystemAction(),
                                   auditCode.getUserAction());
            }
        }
    }


    /**
     * Look to see if there is one of more new events to process.
     *
     * @return a list of received events or null
     */
    protected abstract List<String> checkForEvents();


    /**
     * Register a listener object.  This object will be supplied with all of the events received on the topic.
     *
     * @param topicListener object implementing the OMRSTopicListener interface
     * @return topic name
     */
    public String registerListener(OpenMetadataTopicListener  topicListener)
    {
        if (topicListener != null)
        {
            topicListeners.add(topicListener);
            return topicName;
        }

        final String            methodName = "registerListener";

        OMRSErrorCode errorCode = OMRSErrorCode.NULL_OPEN_METADATA_TOPIC_LISTENER;
        String        errorMessage = errorCode.getErrorMessageId()
                                   + errorCode.getFormattedErrorMessage(listenerThreadName, topicName);

        throw new OMRSLogicErrorException(errorCode.getHTTPErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          errorCode.getSystemAction(),
                                          errorCode.getUserAction());
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
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

            AdditionalProperties  additionalProperties = super.connectionProperties.getAdditionalProperties();

            if (additionalProperties != null)
            {
                Object   sleepTime = additionalProperties.getProperty("sleepTime");

                if ((sleepTime != null) && (sleepTime instanceof Integer))
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
    public  void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();

        keepRunning = false;
    }
}
