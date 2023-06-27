/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.omrstopic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.VirtualConnectorExtension;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.odpi.openmetadata.repositoryservices.events.OMRSEventProtocolVersion;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSRegistryEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSTypeDefEvent;
import org.odpi.openmetadata.repositoryservices.events.beans.OMRSEventBean;
import org.odpi.openmetadata.repositoryservices.events.beans.v1.OMRSEventV1;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSLogicErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;


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
                                                                 OpenMetadataTopicListener,
                                                                 AuditLoggingComponent
{
    private static final Logger       log      = LoggerFactory.getLogger(OMRSTopicConnector.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final ObjectReader OBJECT_READER = OBJECT_MAPPER.reader();
    private static final ObjectWriter OBJECT_WRITER = OBJECT_MAPPER.writer();

    private static final String unknownTopicName = "<Unknown>";

    private List<Connector> embeddedConnectors = null;

    private final List<OMRSTopicListener>          internalTopicListeners = new ArrayList<>();
    private final List<OpenMetadataTopicConnector> eventBusConnectors     = new ArrayList<>();

    private String                    connectionName       = OMRSAuditingComponent.OMRS_TOPIC_CONNECTOR.getComponentName();
    private String                    topicName = unknownTopicName;
    private OMRSEventProtocolVersion  eventProtocolVersion = OMRSEventProtocolVersion.V1;

    protected AuditLog auditLog = null;


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
    @Override
    public void initializeEmbeddedConnectors(List<Connector> embeddedConnectors)
    {
        this.embeddedConnectors = embeddedConnectors;
    }


    /**
     * Receive an audit log object that can be used to record audit log messages.  The caller has initialized it
     * with the correct component description and log destinations.
     *
     * @param auditLog audit log object
     */
    @Override
    public void setAuditLog(AuditLog   auditLog)
    {
        this.auditLog = auditLog;
    }


    /**
     * Return the component description that is used by this connector in the audit log.
     *
     * @return id, name, description, wiki page URL.
     */
    @Override
    public ComponentDescription getConnectorComponentDescription()
    {
        if ((this.auditLog != null) && (this.auditLog.getReport() != null))
        {
            return auditLog.getReport().getReportingComponent();
        }

        return null;
    }


    /**
     * Set up the version of the protocol to use for events.
     *
     * @param eventProtocolVersion version enum
     */
    @Override
    public void setEventProtocolLevel(OMRSEventProtocolVersion eventProtocolVersion)
    {
        if (eventProtocolVersion != null)
        {
            this.eventProtocolVersion = eventProtocolVersion;
        }
    }


    /**
     * Register an OMRSTopicListener object.  This object will be supplied with all the OMRS events
     * received on the topic.
     *
     * @param topicListener object implementing the OMRSTopicListener interface
     */
    @Deprecated
    @Override
    public void registerListener(OMRSTopicListener  topicListener)
    {
        if (topicListener != null)
        {
            internalTopicListeners.add(new OMRSTopicListenerWrapper(topicListener, auditLog));
        }
        else
        {
            final String            methodName = "registerListener";

            throw new OMRSLogicErrorException(OMRSErrorCode.NULL_OPEN_METADATA_TOPIC_LISTENER.getMessageDefinition(connectionName),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Register a listener object.  This object will be supplied with all the events
     * received on the topic.
     *
     * @param topicListener object implementing the OMRSTopicListener interface
     * @param serviceName name of the service that the listener is from
     */
    @Override
    public void registerListener(OMRSTopicListener topicListener,
                                 String            serviceName)
    {
        if (topicListener != null)
        {
            internalTopicListeners.add(new OMRSTopicListenerWrapper(topicListener,
                                                                    serviceName,
                                                                    auditLog.createNewAuditLog(OMRSAuditingComponent.ENTERPRISE_TOPIC_LISTENER)));
        }
        else
        {
            final String            methodName = "registerListener";

            throw new OMRSLogicErrorException(OMRSErrorCode.NULL_OPEN_METADATA_TOPIC_LISTENER.getMessageDefinition(connectionName),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Register a listener object.  This object will be supplied with all the events
     * received on the topic.
     *
     * @param topicListener object implementing the OMRSTopicRepositoryEventListener interface
     * @param serviceName name of the service that the listener is from
     */
    @Override
    public void registerListener(OMRSTopicRepositoryEventListener topicListener,
                                 String                           serviceName)
    {
        if (topicListener != null)
        {
            internalTopicListeners.add(new OMRSTopicListenerWrapper(topicListener,
                                                                    serviceName,
                                                                    auditLog.createNewAuditLog(OMRSAuditingComponent.ENTERPRISE_TOPIC_LISTENER)));
        }
        else
        {
            final String            methodName = "registerListener";

            throw new OMRSLogicErrorException(OMRSErrorCode.NULL_OPEN_METADATA_TOPIC_LISTENER.getMessageDefinition(connectionName),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     * OMRSTopicConnector needs to pass on the start() to its embedded connectors.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        final String methodName = "start";

        /*
         * Save the name of the connection for error messages.
         */
        connectionName = OMRSAuditingComponent.OMRS_TOPIC_CONNECTOR.getComponentName();

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

                        if (auditLog != null)
                        {
                            realTopicConnector.setAuditLog(auditLog.createNewAuditLog(OMRSAuditingComponent.OPEN_METADATA_TOPIC_CONNECTOR));
                        }

                        topicName = realTopicConnector.registerListener(this);

                        this.eventBusConnectors.add(realTopicConnector);

                        if (auditLog != null)
                        {
                            auditLog.logMessage(methodName,
                                                OMRSAuditCode.OMRS_TOPIC_LISTENER_REGISTERED.getMessageDefinition(topicName),
                                                this.getConnection().toString());
                        }
                    }
                }
            }
        }

        /*
         * OMRSTopicConnector needs at least one event bus connector to operate successfully.
         */
        if (this.eventBusConnectors.isEmpty())
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    OMRSAuditCode.OMRS_TOPIC_LISTENER_DEAF.getMessageDefinition(),
                                    this.getConnection().toString());
            }

            throw new ConnectorCheckedException(OMRSErrorCode.NO_EVENT_BUS_CONNECTORS.getMessageDefinition(connectionName),
                                                this.getClass().getName(),
                                                methodName);
        }
        else
        {
            for (OpenMetadataTopicConnector eventBusConnector : eventBusConnectors)
            {
                eventBusConnector.start();
            }

            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    OMRSAuditCode.OMRS_TOPIC_LISTENER_STARTED.getMessageDefinition(topicName),
                                    this.getConnection().toString());
            }
        }
    }


    /**
     * Log that this connector does not support the requested event protocol.
     * This protocol level is requested in the configuration.
     *
     * @param methodName calling method
     * @throws ConnectorCheckedException unsupported event protocol
     */
    private void handleUnsupportedEventVersion(String methodName) throws ConnectorCheckedException
    {
        log.debug("Unsupported Protocol: " + eventProtocolVersion);

        throw new ConnectorCheckedException(OMRSErrorCode.OMRS_UNSUPPORTED_EVENT_PROTOCOL.getMessageDefinition(connectionName,
                                                                                                               eventProtocolVersion.toString()),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Send the registry event to the OMRS Topic connector and manage errors
     *
     * @param registryEvent properties of the event to send
     * @return a future that contains the result of send event
     * @throws ConnectorCheckedException the connector is not able to communicate with the event bus
     */
    @Override
    public CompletableFuture<Boolean> sendRegistryEvent(OMRSRegistryEvent registryEvent) throws ConnectorCheckedException
    {
        final String methodName = "sendRegistryEvent";

        if (eventProtocolVersion == OMRSEventProtocolVersion.V1)
        {
            return this.sendEvent(registryEvent.getOMRSEventV1(), true);
        }
        else
        {
            this.handleUnsupportedEventVersion(methodName);
        }
        return CompletableFuture.completedFuture(false);
    }


    /**
     * Send the TypeDef event to the OMRS Topic connector (providing TypeDef Events are enabled).
     *
     * @param typeDefEvent properties of the event to send
     * @return a future that contains the result of sendEvent
     * @throws ConnectorCheckedException the connector is not able to communicate with the event bus
     */
    @Override
    public CompletableFuture<Boolean> sendTypeDefEvent(OMRSTypeDefEvent typeDefEvent) throws ConnectorCheckedException
    {
        final String methodName = "sendTypeDefEvent";

        if (eventProtocolVersion == OMRSEventProtocolVersion.V1)
        {
            return this.sendEvent(typeDefEvent.getOMRSEventV1(), false);
        }
        else
        {
            this.handleUnsupportedEventVersion(methodName);
        }
        return CompletableFuture.completedFuture(false);
    }


    /**
     * Set the instance event to the OMRS Topic connector if the instance
     * event is of the permitted type.
     *
     * @param instanceEvent  properties of the event to send
     * @throws ConnectorCheckedException the connector is not able to communicate with the event bus
     */
    @Override
    public void sendInstanceEvent(OMRSInstanceEvent instanceEvent) throws ConnectorCheckedException
    {
        final String methodName = "sendInstanceEvent";

        if (eventProtocolVersion == OMRSEventProtocolVersion.V1)
        {
            this.sendEvent(instanceEvent.getOMRSEventV1(), true);
        }
        else
        {
            this.handleUnsupportedEventVersion(methodName);
        }
    }


    /**
     * Sends the supplied event outbound to the OMRSTopicListeners using the event bus connectors.
     *
     * @param event    OMRSEvent object containing the event properties
     * @param logEvent should an audit log message be created?
     */
    private CompletableFuture<Boolean> sendEvent(OMRSEventV1 event,
                                                 boolean     logEvent)
    {
        final String methodName = "sendEvent";
        if (event != null)
        {
            return CompletableFuture.supplyAsync(() -> sendEventTask(event, logEvent));
        }
        else
        {
            log.debug("Unable to send null events");

            throw new OMRSLogicErrorException(OMRSErrorCode.OMRS_TOPIC_SEND_NULL_EVENT.getMessageDefinition(connectionName),
                    this.getClass().getName(),
                    methodName);
        }
    }

    private boolean sendEventTask(OMRSEventV1 event,
                                  boolean logEvent)
    {
        final String methodName = "sendEventTask";
        try
        {
            String eventString = OBJECT_WRITER.writeValueAsString(event);

            if ((auditLog != null) && logEvent)
            {
                auditLog.logMessage(methodName,
                        OMRSAuditCode.OUTBOUND_TOPIC_EVENT.getMessageDefinition(event.getEventCategory().getName(),
                                topicName),
                        eventString);
            }

            for (OpenMetadataTopicConnector eventBusConnector : eventBusConnectors)
            {
                if (eventBusConnector != null)
                {
                    eventBusConnector.sendEvent(eventString);
                }
            }
        }
        catch (ConnectorCheckedException exc)
        {
            log.debug("Unable to send event: " + exc.getMessage());
            throw new CompletionException(exc);
        }
        catch (Exception exc)
        {
            log.debug("Unexpected error sending event: " + exc.getMessage());
            throw new CompletionException(exc);
        }
        return true;
    }


    /**
     * Receives events from the real topic, parses them into event objects and passes them on to
     * the OMRSTopicListeners registered with this connector.
     *
     * @param event inbound event
     */
    @Override
    public void processEvent(String event)
    {
        final String actionDescription = "Process an OMRS Event";
        final String methodName = "processEvent";

        if (event != null)
        {
            OMRSEventBean   eventBean = null;

            /*
             * Parse the string (JSON) event into a bean.
             */
            try
            {
                eventBean = OBJECT_READER.readValue(event, OMRSEventBean.class);
            }
            catch (Exception   exception)
            {
                if (auditLog != null)
                {
                    auditLog.logException(actionDescription,
                                          OMRSAuditCode.EVENT_PARSING_ERROR.getMessageDefinition(event, exception.toString()),
                                          exception);
                }
            }


            /*
             * If the event bean is successfully created then pass it on to the registered listeners.
             */
            if (eventBean instanceof OMRSEventV1)
            {
                OMRSEventBean finalEventBean = eventBean;
                internalTopicListeners.parallelStream().forEach((topicListener) ->
                {
                    try
                    {
                        this.processOMRSEvent((OMRSEventV1) finalEventBean, topicListener);
                    }
                    catch (Exception  error)
                    {
                        log.debug("Unable to pass event to one of the topic listeners");

                        if (auditLog != null)
                        {
                            auditLog.logException(methodName,
                                                  OMRSAuditCode.EVENT_PROCESSING_ERROR.getMessageDefinition(event,
                                                                                                            error.toString(),
                                                                                                            topicListener.toString()),
                                                  event,
                                                  error);
                        }
                    }
                });
            }
        }
        else
        {
            log.debug("Unable to process null events");

            if (auditLog != null)
            {
                auditLog.logMessage(actionDescription, OMRSAuditCode.NULL_EVENT_TO_PROCESS.getMessageDefinition(connectionName));
            }
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
                    if (auditLog != null)
                    {
                        auditLog.logMessage(actionDescription,
                                            OMRSAuditCode.PROCESS_UNKNOWN_EVENT.getMessageDefinition(),
                                           "event {" + event + "}");
                    }

                    log.debug("Unknown event received :|");
            }
        }
        else
        {
            /*
             * A null event was passed, this probably should not happen so log audit record.
             */
            if (auditLog != null)
            {
                auditLog.logMessage(actionDescription, OMRSAuditCode.NULL_OMRS_EVENT_RECEIVED.getMessageDefinition(topicName));
            }

            log.debug("Null OMRS Event received :(");
        }
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

        final String actionDescription = "Disconnect OMRS Topic Connector";

        for (OpenMetadataTopicConnector eventBusConnector : eventBusConnectors)
        {
            eventBusConnector.disconnect();
        }

        if ((auditLog != null) && (! unknownTopicName.equals(topicName)))
        {
            auditLog.logMessage(actionDescription,
                                OMRSAuditCode.OMRS_TOPIC_LISTENER_DISCONNECTED.getMessageDefinition(topicName),
                                this.getConnection().toString());
        }
    }
}
