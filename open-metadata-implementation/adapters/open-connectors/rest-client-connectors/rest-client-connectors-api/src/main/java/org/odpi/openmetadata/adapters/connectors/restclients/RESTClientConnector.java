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
                                                                           SecureConnectorExtension,
                                                                           AuditLoggingComponent
{

    /**
     * Name of the connection configuration property that sets how long a REST call may wait for its response,
     * in seconds.  Zero means no limit.
     */
    public static final String REQUEST_TIMEOUT_PROPERTY = "requestTimeoutSeconds";

    /**
     * Name of the JVM system property that sets the same limit for every REST client connector that does not
     * set it in its connection.
     */
    public static final String REQUEST_TIMEOUT_SYSTEM_PROPERTY = "egeria.rest.client.request.timeout.seconds";

    /**
     * How long a REST call waits for its response unless configured otherwise: long enough for the slowest
     * legitimate calls, such as loading a large archive, but finite.  Without a limit a call to a server that
     * never answers - one that has deadlocked, for example - waits for ever, and so does whatever made it.
     */
    public static final long DEFAULT_REQUEST_TIMEOUT_SECONDS = 30L * 60L;


    /**
     * Default constructor
     */
    public RESTClientConnector()
    {
        super();
    }


    /**
     * Return how long a REST call may wait for its response, in seconds: the requestTimeoutSeconds
     * configuration property of the connection if it is set, otherwise the
     * egeria.rest.client.request.timeout.seconds system property, otherwise {@link #DEFAULT_REQUEST_TIMEOUT_SECONDS}.
     * Zero (or a negative or unreadable value) means no limit.
     *
     * @return number of seconds, or 0 for no limit
     */
    public long getRequestTimeoutSeconds()
    {
        Object configuredValue = null;

        if ((connectionBean != null) && (connectionBean.getConfigurationProperties() != null))
        {
            configuredValue = connectionBean.getConfigurationProperties().get(REQUEST_TIMEOUT_PROPERTY);
        }

        if (configuredValue == null)
        {
            configuredValue = System.getProperty(REQUEST_TIMEOUT_SYSTEM_PROPERTY);
        }

        if (configuredValue == null)
        {
            return DEFAULT_REQUEST_TIMEOUT_SECONDS;
        }

        try
        {
            return Math.max(0L, Long.parseLong(configuredValue.toString().trim()));
        }
        catch (NumberFormatException notANumber)
        {
            return 0L;
        }
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
}
