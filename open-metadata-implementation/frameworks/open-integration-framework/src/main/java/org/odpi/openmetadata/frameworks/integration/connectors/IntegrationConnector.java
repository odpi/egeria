/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.connectors;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

/**
 * IntegrationConnector is the abstract interface for a connector that is managed by the integration services.
 * If the connector throws an exception from any of these methods, it is flagged with a failed status
 * and will not be called again until it is restarted.
 * The setConnectorName and setIntegrationContext methods are called after initialize() and before start().
 */
public interface IntegrationConnector
{
    /**
     * Set up the connector name for logging (called just before start()).
     *
     * @param connectorName connector name from the configuration
     */
    void setConnectorName(String connectorName);


    /**
     * Set up the standard integration context for the connector.
     *
     * @param integrationContext integration context.
     */
    void setContext(IntegrationContext integrationContext);


    /**
     * Indicates that the connector is completely configured and can begin processing.
     * This call can be used to register with non-blocking services.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector has been disconnected
     */
    void start() throws ConnectorCheckedException, UserNotAuthorizedException;


    /**
     * This method is for blocking calls to wait for new metadata.  It is called from its own thread if
     * the connector is configured to have its own thread.  It is recommended that the engage() method
     * returns when each blocking call completes.  The integration daemon will pause a second and then
     * call engage() again.  This pattern enables the calling thread to detect the shutdown of the integration
     * daemon.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     */
    void engage() throws ConnectorCheckedException;


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     * @throws UserNotAuthorizedException the connector has been disconnected
     */
    void refresh() throws ConnectorCheckedException, UserNotAuthorizedException;


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     */
    void disconnect() throws ConnectorCheckedException;
}
