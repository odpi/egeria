/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.VirtualConnectorExtension;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;

import java.util.ArrayList;
import java.util.List;

/**
 * OpenMetadataTopicConnectorBase is a base class to topic connectors that only send
 * events on the embedded event bus connector
 */
public class OpenMetadataTopicConsumerBase extends ConnectorBase implements VirtualConnectorExtension,
                                                                            AuditLoggingComponent
{
    protected List<OpenMetadataTopicConnector> eventBusConnectors = new ArrayList<>();
    protected String                           connectionName     = "<Unknown>";
    protected AuditLog                         auditLog           = null;

    /**
     * Set up the list of connectors that this virtual connector will use to support its interface.
     * The connectors are initialized waiting to start.  When start() is called on the
     * virtual connector, it needs to pass start() to each of the embedded connectors. Similarly for
     * disconnect().
     *
     * @param embeddedConnectors  list of connectors
     */
    @Override
    public void initializeEmbeddedConnectors(List<Connector> embeddedConnectors)
    {
        if (embeddedConnectors != null)
        {
            for (Connector embeddedConnector : embeddedConnectors)
            {
                if (embeddedConnector != null)
                {
                    if (embeddedConnector instanceof OpenMetadataTopicConnector)
                    {
                        /*
                         * Successfully found an event bus connector of the right type.
                         */
                        OpenMetadataTopicConnector eventBusConnector = (OpenMetadataTopicConnector)embeddedConnector;

                        /*
                         * Save the event bus connector so it can be started and disconnected when appropriate.
                         */
                        eventBusConnectors.add(eventBusConnector);
                    }
                }
            }
        }
    }


    /**
     * Receive an audit log object that can be used to record audit log messages.  The caller has initialized it
     * with the correct component description and log destinations.
     *
     * @param auditLog audit log object
     */
    @Override
    public void setAuditLog(AuditLog auditLog)
    {
        this.auditLog = auditLog;
    }


    /**
     * Validates that there is at least one event bus connector receiving events.
     *
     * @param methodName calling method
     * @throws ConnectorCheckedException no event bus connectors available.
     */
    protected void validateEventBusConnectors(String methodName) throws ConnectorCheckedException
    {
        if (eventBusConnectors.isEmpty())
        {
            throw new ConnectorCheckedException(OMRSErrorCode.NO_EVENT_BUS_CONNECTORS.getMessageDefinition(connectionName),
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
        if (super.connectionProperties != null)
        {
            connectionName = super.connectionProperties.getConnectionName();
        }

        /*
         * If there are no event bus connectors then no inbound events will be received.
         */
        this.validateEventBusConnectors(methodName);

        /*
         * Each of the event bus connectors need to be started.  They will then begin receiving inbound events.
         */
        for (OpenMetadataTopicConnector eventBusConnector : eventBusConnectors)
        {
            eventBusConnector.setAuditLog(auditLog);
            eventBusConnector.start();
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

        /*
         * Each of the event bus connectors need to be disconnected so they stop receiving inbound events.
         */
        for (OpenMetadataTopicConnector eventBusConnector : eventBusConnectors)
        {
            eventBusConnector.start();
        }
    }
}
