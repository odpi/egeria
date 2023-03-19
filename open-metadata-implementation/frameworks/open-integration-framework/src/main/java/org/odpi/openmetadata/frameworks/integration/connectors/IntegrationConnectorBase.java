/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.connectors;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.VirtualConnectorExtension;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.integration.ffdc.OIFErrorCode;

import java.util.List;

/**
 * IntegrationConnectorBase is the base class for an integration connector.  It manages the storing of the audit log for the connector
 * and provides a default implementation for the abstract engage() method.  This method only needs to be overridden by connectors
 * that need to make blocking calls to wait for new metadata.
 */
public abstract class IntegrationConnectorBase extends ConnectorBase implements IntegrationConnector,
                                                                                AuditLoggingComponent,
                                                                                VirtualConnectorExtension
{
    protected AuditLog                 auditLog                    = null;
    protected String                   connectorName               = null;
    protected IntegrationContext       integrationContext          = null;
    protected List<Connector>          embeddedConnectors          = null;


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
        this.embeddedConnectors = embeddedConnectors;
    }


    /**
     * Set up the connector name for logging.
     *
     * @param connectorName connector name from the configuration
     */
    @Override
    public void setConnectorName(String connectorName)
    {
        this.connectorName = connectorName;
    }


    /**
     * Set up the standard integration context for the connector.
     *
     * @param integrationContext integration context.
     */
    public void setContext(IntegrationContext integrationContext)
    {
        this.integrationContext = integrationContext;
    }


    /**
     * This method is for blocking calls to wait for new metadata.  It is called from its own thread iff
     * the connector is configured to have its own thread.  It is recommended that the implementation
     * returns when each blocking call completes.  The integration daemon will pause a second and then
     * call engage() again.  This pattern enables the calling thread to detect the shutdown of the integration
     * daemon.
     *
     * This method should be overridden if the connector needs to issue calls that wait for new metadata.
     * If this specific implementation is called a message is logged in the audit log because there is probably
     * a mismatch between the configuration and the connector implementation.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public void engage() throws ConnectorCheckedException
    {
        final String actionDescription = "Calling default engage() method";

        throw new ConnectorCheckedException(OIFErrorCode.ENGAGE_IMPLEMENTATION_MISSING.getMessageDefinition(connectorName),
                                            this.getClass().getName(),
                                            actionDescription);

    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public  synchronized void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();

        if (this.embeddedConnectors != null)
        {
            for (Connector embeddedConnector : this.embeddedConnectors)
            {
                if (embeddedConnector != null)
                {
                    try
                    {
                        embeddedConnector.disconnect();
                    }
                    catch (Exception error)
                    {
                        // keep going
                    }
                }
            }
        }
    }
}
