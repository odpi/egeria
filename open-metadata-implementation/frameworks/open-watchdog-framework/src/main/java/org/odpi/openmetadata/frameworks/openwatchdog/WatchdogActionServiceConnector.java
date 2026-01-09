/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openwatchdog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.ffdc.GovernanceServiceException;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openwatchdog.ffdc.OWFErrorCode;

import java.util.List;


/**
 * WatchdogActionServiceConnector describes a specific type of connector that is responsible for monitoring for
 * a specific notification type.  Information about the notification type to support is passed in the watchdog context.
 */
public abstract class WatchdogActionServiceConnector extends ConnectorBase implements WatchdogActionService,
                                                                                      AuditLoggingComponent
{
    protected final PropertyHelper propertyHelper = new PropertyHelper();

    protected Connector connector = null;

    protected static ObjectMapper objectMapper = new ObjectMapper();

    protected String          watchdogActionServiceName = "<Unknown>";
    protected WatchdogContext watchdogContext           = null;
    protected AuditLog        auditLog                  = null;
    protected List<Connector> embeddedConnectors        = null;


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
     * Convert the supplied properties object to a JSON String.
     *
     * @param properties properties object
     * @return properties as a JSON String
     * @throws PropertyServerException parsing error
     */
    public String getJSONProperties(Object properties) throws PropertyServerException
    {
        final String methodName = "getJSONProperties";

        try
        {
            return objectMapper.writeValueAsString(properties);
        }
        catch (JsonProcessingException parsingError)
        {
            throw new PropertyServerException(OWFErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(watchdogActionServiceName,
                                                                                                     parsingError.getClass().getName(),
                                                                                                     methodName,
                                                                                                     parsingError.getMessage()),
                                              this.getClass().getName(),
                                              methodName,
                                              parsingError);
        }
    }


    /**
     * Set up details of the notification type to monitor.
     *
     * @param watchdogContext information about the asset to analyze and the results of analysis of
     *                      other watchdog action service request.  Partial results from other watchdog action
     *                      services run as part of the same watchdog action service request may also be
     *                      stored in the newAnnotations list.
     */
    public void setWatchdogContext(WatchdogContext watchdogContext)
    {
        this.watchdogContext = watchdogContext;
    }


    /**
     * Set up the watchdog action service name.  This is used in error messages.
     *
     * @param watchdogActionServiceName name of the watchdog action service
     */
    public void setWatchdogActionServiceName(String watchdogActionServiceName)
    {
        this.watchdogActionServiceName = watchdogActionServiceName;
    }


    /**
     * Indicates that the watchdog action service is completely configured and can begin processing.
     * This is where the function of the watchdog action service is implemented.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() in your version.
     *
     * @throws ConnectorCheckedException there is a problem within the watchdog action service.
     * @throws UserNotAuthorizedException the service was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        if (watchdogContext == null)
        {
            throw new ConnectorCheckedException(OWFErrorCode.NULL_WATCHDOG_CONTEXT.getMessageDefinition(watchdogActionServiceName),
                                                this.getClass().getName(),
                                                methodName);
        }
    }


    /**
     * Provide a common exception for unexpected errors.
     *
     * @param methodName calling method
     * @param error caught exception
     * @throws ConnectorCheckedException wrapped exception
     */
    protected void handleUnexpectedException(String      methodName,
                                             Exception   error) throws ConnectorCheckedException
    {
        throw new ConnectorCheckedException(OWFErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(watchdogActionServiceName,
                                                                                                   error.getClass().getName(),
                                                                                                   methodName,
                                                                                                   error.getMessage()),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * This method is called each time a requested event is received from the open metadata repositories.
     * It is called for events received after this listener is registered until the watchdog governance
     * service sets its status in the context as ACTIONED, INVALID, IGNORED or FAILED or it is stopped by an administrator shutting down
     * the hosting server or this service explicitly.
     *
     * @param event event containing details of a change to an open metadata element.
     *
     * @throws GovernanceServiceException reports that the event can not be processed (this is logged but
     *                                    no other action is taken).  The listener will continue to be
     *                                    called until the watchdog governance action service declares it is complete
     *                                    or administrator action shuts down the service.
     */
    public abstract void processEvent(OpenMetadataOutTopicEvent event) throws GovernanceServiceException;


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     */
    @Override
    public  synchronized void disconnect() throws ConnectorCheckedException
    {
        if (connector != null)
        {
            connector.disconnect();
        }

        if (watchdogContext != null)
        {
            watchdogContext.disconnect();
        }

        super.disconnect();
    }
}
