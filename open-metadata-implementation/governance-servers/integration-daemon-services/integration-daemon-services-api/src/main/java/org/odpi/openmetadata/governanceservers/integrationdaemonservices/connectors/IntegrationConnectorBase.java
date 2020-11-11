/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.connectors;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc.IntegrationDaemonServicesAuditCode;

/**
 * IntegrationConnectorBase is the base class for an integration connector.  It manages the storing of the audit log for the connector
 * and provides a default implementation for the engage() method.  This method only needs to be overridden by connectors
 * that need to make blocking calls to wait for new metadata.
 */
public abstract class IntegrationConnectorBase extends ConnectorBase implements IntegrationConnector,
                                                                                AuditLoggingComponent
{
    protected AuditLog auditLog = null;
    protected String   connectorName = null;

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
     * Set up the connector name for logging.
     *
     * @param connectorName connector name from the configuration
     */
    public void setConnectorName(String connectorName)
    {
        this.connectorName = connectorName;
    }


    /**
     * This method is for blocking calls to wait for new metadata.  It is called from its own thread iff
     * the connector is configured to have its own thread.  It is recommended that the engage() method
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

        auditLog.logMessage(actionDescription,
                            IntegrationDaemonServicesAuditCode.ENGAGE_IMPLEMENTATION_MISSING.getMessageDefinition(connectorName));
    }
}
