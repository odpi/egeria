/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.restclients;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.*;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;

import java.util.List;

/**
 * RESTClientConnector provides the base class for REST Client connectors.
 */
public abstract class RESTClientConnector extends ConnectorBase implements RESTClientCalls,
                                                                           VirtualConnectorExtension,
                                                                           SecureConnectorExtension,
                                                                           AuditLoggingComponent
{
    protected List<Connector>           embeddedConnectors = null;
    protected AuditLog                  auditLog = null;

    /**
     * Default constructor
     */
    public RESTClientConnector()
    {
        super();
    }

    /**
     * Set up the list of connectors that this virtual connector will use to support its interface.
     * The connectors are initialized waiting to start.  When start() is called on the
     * virtual connector, it needs to pass start() to each of the embedded connectors.
     * Similar processing is needed for the disconnect() method.
     *
     * @param embeddedConnectors list of connectors
     */
    @Override
    public void initializeEmbeddedConnectors(List<Connector> embeddedConnectors)
    {
        this.embeddedConnectors = embeddedConnectors;
    }


    /**
     * Return the component description that is used by this connector in the audit log.
     *
     * @return id, name, description, wiki page URL.
     */
    public ComponentDescription getConnectorComponentDescription()
    {
        if ((this.auditLog != null) && (this.auditLog.getReport() != null))
        {
            return auditLog.getReport().getReportingComponent();
        }

        return null;
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
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public  void disconnect() throws ConnectorCheckedException
    {
        /*
         * This disconnects any embedded connections such as secrets connectors.
         */
        super.disconnectConnectors(this.embeddedConnectors);

        super.disconnect();
    }
}
