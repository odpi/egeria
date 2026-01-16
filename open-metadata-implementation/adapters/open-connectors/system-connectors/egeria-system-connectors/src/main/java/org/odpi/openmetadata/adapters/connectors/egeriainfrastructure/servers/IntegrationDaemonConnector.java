/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers;

import io.openlineage.client.OpenLineage;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.List;
import java.util.Map;

/**
 * IntegrationDaemonConnector provides access to an Integration Daemon server.
 */
public class IntegrationDaemonConnector extends OMAGServerConnectorBase
{
    public IntegrationDaemonConnector()
    {
        super("Integration Daemon Connector");
    }


    /*
     * ========================================================================================
     * Integration Daemon specific services
     */


    /**
     * Retrieve the configuration properties of the named connector.
     *
     * @param connectorName name of a specific connector or null for all connectors
     *
     * @return property map
     *
     * @throws InvalidParameterException the connector name is not recognized
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration daemon
     */
    public Map<String, Object> getConfigurationProperties(String connectorName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        return extractor.getConfigurationProperties(connectorName);
    }


    /**
     * Update the configuration properties of the connectors, or specific connector if a connector name is supplied.
     *
     * @param connectorName name of a specific connector or null for all connectors
     * @param isMergeUpdate should the properties be merged into the existing properties or replace them
     * @param configurationProperties new configuration properties
     * @throws InvalidParameterException the connector name is not recognized
     */
    public void updateConfigurationProperties(String              connectorName,
                                              boolean             isMergeUpdate,
                                              Map<String, Object> configurationProperties) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        extractor.updateConfigurationProperties(connectorName, isMergeUpdate, configurationProperties);
    }


    /**
     * Update the configuration properties of the connectors, or specific connector if a connector name is supplied.
     *
     * @param connectorName name of a specific connector or null for all connectors
     * @param networkAddress new address
     * @throws InvalidParameterException the connector name is not recognized
     */
    public void updateEndpointNetworkAddress(String              connectorName,
                                             String              networkAddress) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        extractor.updateEndpointNetworkAddress(connectorName, networkAddress);
    }


    /**
     * Update the configuration properties of the connectors, or specific connector if a connector name is supplied.
     *
     * @param connectorName name of a specific connector or null for all connectors
     * @param connection new address
     * @throws InvalidParameterException the connector name is not recognized
     */
    public void updateConnectorConnection(String     connectorName,
                                          Connection connection) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        extractor.updateConnectorConnection(connectorName, connection);
    }


    /**
     * Issue a refresh() request on a specific connector
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration daemon
     */
    public void refreshConnector(String connectorName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        extractor.refreshConnector(connectorName);
    }



    /**
     * Issue a refresh() request on a connector running in the integration daemon.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration daemon
     */
    public void refreshConnectors() throws InvalidParameterException,
                                           UserNotAuthorizedException,
                                           PropertyServerException
    {
        extractor.refreshConnectors();
    }



    /**
     * Issue a restart() request on a specific connector
     *
     * @param connectorName connector
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration daemon
     */
    public void restartConnector(String connectorName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        extractor.restartConnector(connectorName);
    }



    /**
     * Issue a restart() request on a connector running in the integration daemon.
     **
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration daemon
     */
    public void restartConnectors() throws InvalidParameterException,
                                           UserNotAuthorizedException,
                                           PropertyServerException
    {
        extractor.restartConnectors();
    }



    /**
     * Request that the integration group refresh its configuration by calling the metadata server.
     * This request is useful if the metadata server has an outage, particularly while the
     * integration daemon is initializing.  This request just ensures that the latest configuration
     * is in use.
     *
     * @param integrationGroupName qualifiedName of the integration group to target
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the integration group.
     */
    public  void refreshIntegrationGroupConfig(String integrationGroupName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        extractor.refreshIntegrationGroupConfig(integrationGroupName);
    }


    /**
     * Pass an open lineage event to the integration service.  It will pass it on to the integration connectors that have registered a
     * listener for open lineage events.
     *
     * @param event open lineage event to publish.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException the caller is not authorized to call the service
     * @throws PropertyServerException a problem processing the request
     */
    public void publishOpenLineageEvent(String event) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        extractor.publishOpenLineageEvent(event);
    }


    /**
     * Pass an open lineage event to the integration service.  It will pass it on to the integration connectors that have registered a
     * listener for open lineage events.
     *
     * @param event open lineage event to publish.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException the caller is not authorized to call the service
     * @throws PropertyServerException a problem processing the request
     */
    public void publishOpenLineageEvent(OpenLineage.RunEvent event) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        extractor.publishOpenLineageEvent(event);
    }
}
