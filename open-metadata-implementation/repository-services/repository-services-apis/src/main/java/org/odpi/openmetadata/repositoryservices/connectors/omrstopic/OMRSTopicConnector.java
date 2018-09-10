/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.connectors.omrstopic;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.VirtualConnectorExtension;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.odpi.openmetadata.repositoryservices.events.*;
import org.odpi.openmetadata.repositoryservices.events.beans.OMRSEventBean;
import org.odpi.openmetadata.repositoryservices.events.beans.v1.OMRSEventV1;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSLogicErrorException;

import java.util.ArrayList;
import java.util.List;


/**
 * OMRSTopicConnector provides the support for the registration of OMRSTopicListeners and the distribution of
 * OMRS events - both inbound and outbound.
 * <p>
 *     The OMRSTopicConnector is a virtual connector.  It uses one or more event bus connectors to
 *     interact with real event buses.   These connectors are passed to it during initialization.
 *     During its operation, it acts as a go-between the event buses processing JSON payloads and
 *     internal open metadata components that expect to receive OMRS Events.
 * </p>
 * <p>
 *     OMRSTopicConnector implements 3 interfaces:
 * </p>
 * <ul>
 *     <li>
 *         OMRSTopic provides the methods for OMRSTopicListeners to register with this connector.
 *     </li>
 *     <li>
 *         VirtualConnectorExtension declares this connector a virtual connector and provides the
 *         method for receiving the event bus connectors (embedded connectors).
 *     </li>
 *     <li>
 *         OpenMetadataTopicListener enables this object to register with the real event bus
 *         connectors that implement OpenMetadataTopic.
 *     </li>
 * </ul>
 */
public class OMRSTopicConnector extends ConnectorBase implements OMRSTopic,
                                                                 VirtualConnectorExtension,
                                                                 OpenMetadataTopicListener
{
    private static final Logger       log      = LoggerFactory.getLogger(OMRSTopicConnector.class);
    private static final OMRSAuditLog auditLog = new OMRSAuditLog(OMRSAuditingComponent.OMRS_TOPIC_CONNECTOR);

    private static final String connectorName    = "OMRSTopicListener";

    private List<OMRSTopicListener>          internalTopicListeners = new ArrayList<>();
    private List<OpenMetadataTopicConnector> eventBusConnectors     = new ArrayList<>();

    private String                    connectionName       = connectorName;
    private OMRSEventProtocolVersion  eventProtocolVersion = OMRSEventProtocolVersion.V1;

    /**
     * Default constructor
     */
    public OMRSTopicConnector()
    {
        super();
    }


    /**
     * Registers itself as a listener of any OpenMetadataTopicConnectors that are passed as
     * embedded connectors.
     *
     * @param embeddedConnectors  list of connectors
     */
    public void initializeEmbeddedConnectors(List<Connector> embeddedConnectors)
    {
        /*
         * Save the name of the connection for error messages.
         */
        if (super.connectionProperties != null)
        {
            connectionName = super.connectionProperties.getConnectionName();
        }

        log.debug("Initializing OMRSTopicConnector: " + connectionName);

        /*
         * Step through the embedded connectors, selecting only the OpenMetadataTopicConnectors
         * to use.
         */
        if (embeddedConnectors != null)
        {
            log.debug("OMRSTopicConnector: " + connectionName + " supplied with " + embeddedConnectors.size() + " embedded connectors");

            for (Connector  embeddedConnector : embeddedConnectors)
            {
                if (embeddedConnector != null)
                {
                    if (embeddedConnector instanceof OpenMetadataTopicConnector)
                    {
                        /*
                         * Successfully found an event bus connector of the right type.
                         */
                        OpenMetadataTopicConnector realTopicConnector = (OpenMetadataTopicConnector)embeddedConnector;

                        String   topicName = realTopicConnector.registerListener(this);
                        this.eventBusConnectors.add(realTopicConnector);

                        OMRSAuditCode auditCode = OMRSAuditCode.OMRS_TOPIC_LISTENER_REGISTERED;
                        auditLog.logRecord(connectorName,
                                           auditCode.getLogMessageId(),
                                           auditCode.getSeverity(),
                                           auditCode.getFormattedLogMessage(connectionName, topicName),
                                           this.getConnection().toString(),
                                           auditCode.getSystemAction(),
                                           auditCode.getUserAction());
                    }
                }
            }
        }

        /*
         * OMRSTopicConnector needs at least one event bus connector to operate successfully.
         */
        if (this.eventBusConnectors.isEmpty())
        {
            OMRSAuditCode auditCode = OMRSAuditCode.OMRS_TOPIC_LISTENER_DEAF;
            auditLog.logRecord(connectorName,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(connectionName),
                               this.getConnection().toString(),
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
        }
    }


    /**
     * Setup the version of the protocol to use for events.
     *
     * @param eventProtocolVersion version enum
     */
    public void setEventProtocolLevel(OMRSEventProtocolVersion eventProtocolVersion)
    {
        if (eventProtocolVersion != null)
        {
            this.eventProtocolVersion = eventProtocolVersion;
        }
    }

    /**
     * Register an OMRSTopicListener object.  This object will be supplied with all of the OMRS events
     * received on the topic.
     *
     * @param topicListener object implementing the OMRSTopicListener interface
     */
    public void registerListener(OMRSTopicListener  topicListener)
    {
        if (topicListener != null)
        {
            internalTopicListeners.add(topicListener);
        }
        else
        {
            final String            methodName = "registerListener";

            OMRSErrorCode errorCode = OMRSErrorCode.NULL_OPEN_METADATA_TOPIC_LISTENER;
            String        errorMessage = errorCode.getErrorMessageId()
                                       + errorCode.getFormattedErrorMessage(connectionName);

            throw new OMRSLogicErrorException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     * OMRSTopicConnector needs to pass on the start() to its embedded connectors.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public void start() throws ConnectorCheckedException
    {
        super.start();

        if (this.eventBusConnectors.isEmpty())
        {
            final String            methodName = "start";

            OMRSErrorCode errorCode = OMRSErrorCode.NO_EVENT_BUS_CONNECTORS;
            String        errorMessage = errorCode.getErrorMessageId()
                                       + errorCode.getFormattedErrorMessage(connectionName);

            throw new ConnectorCheckedException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction());
        }
        else
        {
            for (OpenMetadataTopicConnector eventBusConnector : eventBusConnectors)
            {
                eventBusConnector.start();
            }

            OMRSAuditCode auditCode = OMRSAuditCode.OMRS_TOPIC_LISTENER_STARTED;
            auditLog.logRecord(connectorName,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(connectionName),
                               this.getConnection().toString(),
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
        }
    }


    /**
     * Send the registry event to the OMRS Topic connector and manage errors
     *
     * @param registryEvent  properties of the event to send
     * @throws ConnectorCheckedException the connector is not able to communicate with the event bus
     */
    public void sendRegistryEvent(OMRSRegistryEvent registryEvent) throws ConnectorCheckedException
    {
        if (eventProtocolVersion == OMRSEventProtocolVersion.V1)
        {
            this.sendEvent(registryEvent.getOMRSEventV1());
        }
        else
        {
            log.debug("Unsupported Protocol: " + eventProtocolVersion);

            OMRSErrorCode errorCode = OMRSErrorCode.OMRS_UNSUPPORTED_EVENT_PROTOCOL;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(connectionName,
                                                         eventProtocolVersion.toString());

            throw new ConnectorCheckedException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                connectorName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction());
        }
    }


    /**
     * Send the TypeDef event to the OMRS Topic connector (providing TypeDef Events are enabled).
     *
     * @param typeDefEvent  properties of the event to send
     * @throws ConnectorCheckedException the connector is not able to communicate with the event bus
     */
    public void sendTypeDefEvent(OMRSTypeDefEvent typeDefEvent) throws ConnectorCheckedException
    {
        if (eventProtocolVersion == OMRSEventProtocolVersion.V1)
        {
            this.sendEvent(typeDefEvent.getOMRSEventV1());
        }
        else
        {
            log.debug("Unsupported Protocol: " + eventProtocolVersion);

            OMRSErrorCode errorCode = OMRSErrorCode.OMRS_UNSUPPORTED_EVENT_PROTOCOL;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(connectionName,
                                                         eventProtocolVersion.toString());

            throw new ConnectorCheckedException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                connectorName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction());
        }
    }


    /**
     * Set the instance event to the OMRS Topic connector if the instance
     * event is of the permitted type.
     *
     * @param instanceEvent  properties of the event to send
     * @throws ConnectorCheckedException the connector is not able to communicate with the event bus
     */
    public void sendInstanceEvent(OMRSInstanceEvent instanceEvent) throws ConnectorCheckedException
    {
        if (eventProtocolVersion == OMRSEventProtocolVersion.V1)
        {
            this.sendEvent(instanceEvent.getOMRSEventV1());
        }
        else
        {
            log.debug("Unsupported Protocol: " + eventProtocolVersion);

            OMRSErrorCode errorCode = OMRSErrorCode.OMRS_UNSUPPORTED_EVENT_PROTOCOL;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(connectionName,
                                                         eventProtocolVersion.toString());

            throw new ConnectorCheckedException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                connectorName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction());
        }
    }


    /**
     * Sends the supplied event outbound to the OMRSTopicListeners using the event bus connectors.
     *
     * @param event OMRSEvent object containing the event properties.
     * @throws ConnectorCheckedException the connector is not able to communicate with the event bus
     */
    private void sendEvent(OMRSEventV1 event) throws ConnectorCheckedException
    {
        if (event != null)
        {
            try
            {
                ObjectMapper objectMapper = new ObjectMapper();

                for (OpenMetadataTopicConnector eventBusConnector : eventBusConnectors)
                {
                    if (eventBusConnector != null)
                    {
                        eventBusConnector.sendEvent(objectMapper.writeValueAsString(event));
                    }
                }
            }
            catch (ConnectorCheckedException exc)
            {
                log.debug("Unable to send event: " + exc.getMessage());

                throw exc;
            }
            catch (Throwable exc)
            {
                log.debug("Unexpected error sending event: " + exc.getMessage());

                OMRSErrorCode errorCode = OMRSErrorCode.OMRS_TOPIC_SEND_EVENT_FAILED;
                String        errorMessage = errorCode.getErrorMessageId()
                                           + errorCode.getFormattedErrorMessage(connectionName,
                                                                                event.toString(),
                                                                                exc.getMessage());

                throw new ConnectorCheckedException(errorCode.getHTTPErrorCode(),
                                                    this.getClass().getName(),
                                                    connectorName,
                                                    errorMessage,
                                                    errorCode.getSystemAction(),
                                                    errorCode.getUserAction(),
                                                    exc);
            }
        }
        else
        {
            log.debug("Unable to send null events");

            OMRSErrorCode errorCode = OMRSErrorCode.OMRS_TOPIC_SEND_NULL_EVENT;
            String        errorMessage = errorCode.getErrorMessageId()
                                       + errorCode.getFormattedErrorMessage(connectionName);

            throw new OMRSLogicErrorException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              connectorName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }


    /**
     * Receives events from the real topic, parses them into event objects and passes them on to
     * the OMRSTopicListeners registered with this connector.
     *
     * @param event inbound event
     */
    public void processEvent(String event)
    {
        if (event != null)
        {
            OMRSEventBean   eventBean = null;

            /*
             * Parse the string (JSON) event into a bean.
             */
            try
            {
                ObjectMapper objectMapper = new ObjectMapper();

                eventBean = objectMapper.readValue(event, OMRSEventBean.class);
            }
            catch (Throwable   exception)
            {
                OMRSAuditCode auditCode = OMRSAuditCode.EVENT_PARSING_ERROR;

                auditLog.logException(connectorName,
                                      auditCode.getLogMessageId(),
                                      auditCode.getSeverity(),
                                      auditCode.getFormattedLogMessage(event, exception.toString()),
                                      null,
                                      auditCode.getSystemAction(),
                                      auditCode.getUserAction(),
                                      exception);
            }


            /*
             * If the event bean is successfully created then pass it on to the registered listeners.
             */
            if ((eventBean != null) && (eventBean instanceof OMRSEventV1))
            {
                for (OMRSTopicListener  topicListener : internalTopicListeners)
                {
                    try
                    {
                        this.processOMRSEvent((OMRSEventV1)eventBean,
                                              topicListener);
                    }
                    catch (Throwable  error)
                    {
                        log.debug("Unable to pass event to one of the topic listeners");

                        OMRSAuditCode auditCode = OMRSAuditCode.EVENT_PROCESSING_ERROR;

                        auditLog.logException(connectorName,
                                              auditCode.getLogMessageId(),
                                              auditCode.getSeverity(),
                                              auditCode.getFormattedLogMessage(event,
                                                                               error.toString(),
                                                                               topicListener.toString()),
                                              null,
                                              auditCode.getSystemAction(),
                                              auditCode.getUserAction(),
                                              error);
                    }
                }
            }
        }
        else
        {
            log.debug("Unable to process null events");

            OMRSAuditCode auditCode = OMRSAuditCode.NULL_EVENT_TO_PROCESS;

            auditLog.logRecord(connectorName,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(connectionName),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
        }
    }


    /**
     * Process the OMRS Event bean.  The processing is careful of nulls and ignores an event
     * that is incorrectly formatted.  The assumption is that the unformatted part of the message
     * is an extension from a newer version of the protocol and can be ignored.
     *
     * @param event Version 1 of the OMRSEvent that defines the category and payload of the incoming event.
     * @param topicListener listener that will receive the event.
     */
    private void processOMRSEvent(OMRSEventV1        event,
                                  OMRSTopicListener  topicListener)
    {
        String   actionDescription = "Process Incoming Event";

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
                    topicListener.processRegistryEvent(new OMRSRegistryEvent(event));
                    break;

                case TYPEDEF:
                    topicListener.processTypeDefEvent(new OMRSTypeDefEvent(event));
                    break;

                case INSTANCE:
                    topicListener.processInstanceEvent(new OMRSInstanceEvent(event));
                    break;

                default:
                    /*
                     * Nothing to do since this server does not understand the message type.  This situation
                     * will occur if the local server is back level from another server in the cohort
                     * and the more advanced server supports new types of messages,
                     */
                    OMRSAuditCode auditCode = OMRSAuditCode.PROCESS_UNKNOWN_EVENT;

                    auditLog.logRecord(actionDescription,
                                       auditCode.getLogMessageId(),
                                       auditCode.getSeverity(),
                                       auditCode.getFormattedLogMessage(),
                                       "event {" + event.toString() + "}",
                                       auditCode.getSystemAction(),
                                       auditCode.getUserAction());

                    log.debug("Unknown event received :|");
            }
        }
        else
        {
            /*
             * A null event was passed, this probably should not happen so log audit record.
             */
            OMRSAuditCode auditCode = OMRSAuditCode.NULL_OMRS_EVENT_RECEIVED;

            auditLog.logRecord(actionDescription,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());

            log.debug("Null OMRS Event received :(");
        }
    }

    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public  void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();

        for (OpenMetadataTopicConnector eventBusConnector : eventBusConnectors)
        {
            eventBusConnector.disconnect();
        }

        OMRSAuditCode auditCode = OMRSAuditCode.OMRS_TOPIC_LISTENER_DISCONNECTED;
        auditLog.logRecord(connectorName,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(connectionName),
                           this.getConnection().toString(),
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());
    }
}
