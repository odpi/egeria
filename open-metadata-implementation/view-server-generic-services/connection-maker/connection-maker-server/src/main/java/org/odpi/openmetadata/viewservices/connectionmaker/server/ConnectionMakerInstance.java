/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.connectionmaker.server;

import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ViewServiceClientMap;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ConnectionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ConnectorTypeHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.EndpointHandler;


import java.util.List;

/**
 * ConnectionMakerInstance caches references to the objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class ConnectionMakerInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.CONNECTION_MAKER;

    /*
     * These maps cache clients for specific view services/access services.
     */
    private final ViewServiceClientMap<ConnectionHandler>    connectionHandlerMap;
    private final ViewServiceClientMap<ConnectorTypeHandler> connectorTypeHandlerMap;
    private final ViewServiceClientMap<EndpointHandler>      endpointHandlerMap;



    /**
     * Set up the Connection Maker OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param localServerUserPassword  password to use as part of an HTTP authentication mechanism.
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @param activeViewServices list of view services active in this server
     */
    public ConnectionMakerInstance(String                  serverName,
                                   AuditLog                auditLog,
                                   String                  localServerUserId,
                                   String                  localServerUserPassword,
                                   int                     maxPageSize,
                                   String                  remoteServerName,
                                   String                  remoteServerURL,
                                   List<ViewServiceConfig> activeViewServices)
    {
        super(serverName,
              myDescription.getViewServiceFullName(),
              auditLog,
              localServerUserId,
              localServerUserPassword,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        this.connectionHandlerMap = new ViewServiceClientMap<>(ConnectionHandler.class,
                                                               serverName,
                                                               localServerUserId,
                                                               localServerUserPassword,
                                                               auditLog,
                                                               activeViewServices,
                                                               myDescription.getViewServiceFullName(),
                                                               maxPageSize);

        this.connectorTypeHandlerMap = new ViewServiceClientMap<>(ConnectorTypeHandler.class,
                                                                  serverName,
                                                                  localServerUserId,
                                                                  localServerUserPassword,
                                                                  auditLog,
                                                                  activeViewServices,
                                                                  myDescription.getViewServiceFullName(),
                                                                  maxPageSize);

        this.endpointHandlerMap = new ViewServiceClientMap<>(EndpointHandler.class,
                                                             serverName,
                                                             localServerUserId,
                                                             localServerUserPassword,
                                                             auditLog,
                                                             activeViewServices,
                                                             myDescription.getViewServiceFullName(),
                                                             maxPageSize);

    }


    /**
     * Return the client.  This client is from the Open Metadata Store services and is for maintaining
     * connection artifacts.
     *
     * @return client
     * @throws InvalidParameterException bad client initialization
     * @throws PropertyServerException bad client handler class
     */
    public ConnectionHandler getConnectionHandler(String viewServiceURLMarker,
                                                  String methodName) throws InvalidParameterException,
                                                                            PropertyServerException
    {
        return connectionHandlerMap.getClient(viewServiceURLMarker, methodName);
    }


    /**
     * Return the client.  This client is from the Open Metadata Store services and is for maintaining
     * connectorType artifacts.
     *
     * @return client
     * @throws InvalidParameterException bad client initialization
     * @throws PropertyServerException bad client handler class
     */
    public ConnectorTypeHandler getConnectorTypeHandler(String viewServiceURLMarker,
                                                String methodName) throws InvalidParameterException,
                                                                          PropertyServerException
    {
        return connectorTypeHandlerMap.getClient(viewServiceURLMarker, methodName);
    }


    /**
     * Return the client.  This client is from the Open Metadata Store services and is for maintaining
     * governance definition artifacts.
     *
     * @return client
     * @throws InvalidParameterException bad client initialization
     * @throws PropertyServerException bad client handler class
     */
    public EndpointHandler getEndpointHandler(String viewServiceURLMarker,
                                              String methodName) throws InvalidParameterException,
                                                                        PropertyServerException
    {
        return endpointHandlerMap.getClient(viewServiceURLMarker, methodName);
    }
}
