/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.babbage;


import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;


/**
 * The Babbage Analytical Engine Service is an integration service that ensures that the governance action types
 * attached as catalog targets are regularly invoked to gather and record insight, or, if they are supposed to
 * be long-running, they are still.
 */
public class BabbageAnalyticalEngineService extends IntegrationConnectorBase
{
    volatile boolean completed = false;


    /**
     * Indicates that the watchdog action service is completely configured and can begin processing.
     * This is a standard method from the Open Connector Framework (OCF), so
     * be sure to call super.start() at the start of your overriding version.
     *
     * @throws ConnectorCheckedException a problem within the watchdog action service.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

    }

    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     *
     * @throws ConnectorCheckedException  a problem with the connector.  It is not able to refresh the metadata.
     * @throws UserNotAuthorizedException the connector has been disconnected
     */
    @Override
    public void refresh() throws ConnectorCheckedException, UserNotAuthorizedException
    {

    }


    /**
     * Disconnect is called either because this governance action service called governanceContext.recordCompletionStatus()
     * or the administrator requested this watchdog action service stop running, or the hosting server is shutting down.
     * If disconnect completes before this watchdog action service records
     * its completion status, then the watchdog action service is restarted either at the administrator's request or the next time the server starts.
     * If you do not want this governance action service restarted, be sure to record the completion status in the engine action.
     * <p>
     * The disconnect() method is a standard method from the Open Connector Framework (OCF).  If you need to override this method,
     * be sure to call super.disconnect() in your version.
     * </p>
     *
     * @throws ConnectorCheckedException a problem within the watchdog action service.
     */
    @Override
    public  void disconnect() throws ConnectorCheckedException
    {
        completed = true;

        super.disconnect();
    }
}
