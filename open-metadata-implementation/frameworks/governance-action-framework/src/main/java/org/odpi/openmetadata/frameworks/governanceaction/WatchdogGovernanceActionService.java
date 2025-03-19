/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;

import java.util.Map;

/**
 * The watchdog governance action service is responsible for monitoring changes to the metadata elements and then initiating
 * further governance activity.  It can be written as a log running service that is detecting multiple events over time, or
 * it may just be looking for a specific change before completing.
 * To create your own watchdog governance action service, create a new class that extends this class
 * along with another class that extends the GovernanceActionServiceProviderBase class to act as its connector provider.
 * Add a start() method that begins by calling super.start() and then registers a listener to wait for the desired event(s).
 * THe listener is called each time an event occurs.  It can use the methods on the governance context to:
 *
 * <ul>
 *     <li>Create a governance action to drive a specific governance action service.</li>
 *     <li>Initiate a governance action process.</li>
 *     <li>Create an incident report that will be processed by the incident report governance processes.</li>
 * </ul>
 *
 * When the watchdog governance action service is complete, it should call
 * governanceContext.recordCompletionStatus() and then return. The recordCompletionStatus() call will include guards that are used to determine
 * the next step if any.
 */
public abstract class WatchdogGovernanceActionService extends GovernanceActionServiceConnector
{
    protected WatchdogGovernanceContext governanceContext = null;


    /**
     * Set up details of the governance action request and access to the metadata store.
     * This method is called before start and should not be null
     *
     * @param governanceContext specialist context for this type of governance action.
     */
    public void setGovernanceContext(GovernanceActionContext governanceContext)
    {
        this.governanceContext = governanceContext;
    }


    /**
     * Retrieve the property value from the values passed to this governance action service.
     *
     * @param propertyName name of the property
     * @param defaultValue default value
     * @return property value
     */
    protected String getProperty(String propertyName, String defaultValue)
    {
        Map<String, String> requestParameters       = governanceContext.getRequestParameters();
        Map<String, Object> configurationProperties = connectionDetails.getConfigurationProperties();

        String propertyValue = defaultValue;

        if ((requestParameters != null) && (requestParameters.get(propertyName) != null))
        {
            propertyValue = requestParameters.get(propertyName);
        }
        else
        {
            if ((configurationProperties != null) && (configurationProperties.get(propertyName) != null))
            {
                propertyValue = configurationProperties.get(propertyName).toString();
            }
        }

        return propertyValue;
    }


    /**
     * Indicates that the governance action service is completely configured and can begin processing.
     * This is where the watchdog governance action service registers its listener.  The watchdog listener
     * will then be called for each event.  It can use the context to initiate other governance activity or call
     * governanceContext.recordCompletionStatus() to end.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() in your version.
     *
     * @throws ConnectorCheckedException there is a problem within the governance action service.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();
        super.validateContext(governanceContext);
    }


    /**
     * Disconnect is called either because this governance action service called governanceContext.recordCompletionStatus()
     * or the administer requested this governance action service stop running or the hosting server is shutting down.
     * If disconnect completes before the governance action service records
     * its completion status then the governance action service is restarted either at the administrator's request or the next time the server starts.
     * If you do not want this governance action service restarted, be sure to record the completion status in disconnect().
     * The disconnect() method is a standard method from the Open Connector Framework (OCF).  If you need to override this method
     * be sure to call super.disconnect() in your version.
     *
     * @throws ConnectorCheckedException there is a problem within the governance action service.
     */
    @Override
    public  void disconnect() throws ConnectorCheckedException
    {
        if ((isActive()) && (governanceContext != null))
        {
            governanceContext.disconnectListener();
        }
        super.disconnect();
    }
}
