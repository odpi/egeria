/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.connectionmaker.server;

import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.ConnectionHandler;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.ConnectorTypeHandler;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.EndpointHandler;
import org.odpi.openmetadata.viewservices.connectionmaker.ffdc.ConnectionMakerErrorCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final Map<String, ConnectionHandler> connectionHandlerMap = new HashMap<>();
    private final Map<String, ConnectorTypeHandler>    connectorTypeHandlerMap    = new HashMap<>();
    private final Map<String, EndpointHandler> endpointHandlerMap = new HashMap<>();

    private final List<ViewServiceConfig> activeViewServices;



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

        this.activeViewServices = activeViewServices;
    }


    /**
     * Return the client.  This client is from the Open Metadata Store services and is for maintaining
     * connection artifacts.
     *
     * @return client
     */
    public ConnectionHandler getConnectionHandler(String viewServiceURLMarker,
                                                      String methodName) throws InvalidParameterException
    {
        ConnectionHandler connectionHandler = null;

        if (viewServiceURLMarker != null)
        {
            connectionHandler = connectionHandlerMap.get(viewServiceURLMarker);

            if (connectionHandler == null)
            {
                for (ViewServiceConfig viewServiceConfig : activeViewServices)
                {
                    if (viewServiceConfig.getViewServiceURLMarker().equals(viewServiceURLMarker))
                    {
                        String viewServicePartnerService = viewServiceConfig.getViewServicePartnerService();

                        if (viewServicePartnerService != null)
                        {
                            for (AccessServiceDescription accessServiceDescription : AccessServiceDescription.values())
                            {
                                if (accessServiceDescription.getAccessServiceFullName().equals(viewServicePartnerService))
                                {
                                    if (localServerUserPassword != null)
                                    {
                                        connectionHandler = new ConnectionHandler(serverName,
                                                                                      viewServiceConfig.getOMAGServerName(),
                                                                                      viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                                                      auditLog,
                                                                                      accessServiceDescription.getAccessServiceURLMarker(),
                                                                                      ViewServiceDescription.CONNECTION_MAKER.getViewServiceFullName(),
                                                                                      maxPageSize);
                                    }
                                    else
                                    {
                                        connectionHandler = new ConnectionHandler(serverName,
                                                                                      viewServiceConfig.getOMAGServerName(),
                                                                                      viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                                                      localServerUserId,
                                                                                      localServerUserPassword,
                                                                                      auditLog,
                                                                                      accessServiceDescription.getAccessServiceURLMarker(),
                                                                                      ViewServiceDescription.CONNECTION_MAKER.getViewServiceFullName(),
                                                                                      maxPageSize);
                                    }

                                    connectionHandlerMap.put(viewServiceURLMarker, connectionHandler);
                                }
                            }
                        }
                    }
                }
            }
        }


        if (connectionHandler == null)
        {
            throw new InvalidParameterException(ConnectionMakerErrorCode.INVALID_URL_MARKER.getMessageDefinition(viewServiceURLMarker),
                                                this.getClass().getName(),
                                                methodName,
                                                "viewServiceURLMarker");
        }

        return connectionHandler;
    }




    /**
     * Return the client.  This client is from the Open Metadata Store services and is for maintaining
     * connectorType artifacts.
     *
     * @return client
     */
    public ConnectorTypeHandler getConnectorTypeHandler(String viewServiceURLMarker,
                                                String methodName) throws InvalidParameterException
    {
        ConnectorTypeHandler connectorTypeHandler = null;

        if (viewServiceURLMarker != null)
        {
            connectorTypeHandler = connectorTypeHandlerMap.get(viewServiceURLMarker);

            if (connectorTypeHandler == null)
            {
                for (ViewServiceConfig viewServiceConfig : activeViewServices)
                {
                    if (viewServiceConfig.getViewServiceURLMarker().equals(viewServiceURLMarker))
                    {
                        String viewServicePartnerService = viewServiceConfig.getViewServicePartnerService();

                        if (viewServicePartnerService != null)
                        {
                            for (AccessServiceDescription accessServiceDescription : AccessServiceDescription.values())
                            {
                                if (accessServiceDescription.getAccessServiceFullName().equals(viewServicePartnerService))
                                {
                                    if (localServerUserPassword != null)
                                    {
                                        connectorTypeHandler = new ConnectorTypeHandler(serverName,
                                                                                viewServiceConfig.getOMAGServerName(),
                                                                                viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                                                auditLog,
                                                                                accessServiceDescription.getAccessServiceURLMarker(),
                                                                                ViewServiceDescription.CONNECTION_MAKER.getViewServiceFullName(),
                                                                                maxPageSize,
                                                                                false);
                                    }
                                    else
                                    {
                                        connectorTypeHandler = new ConnectorTypeHandler(serverName,
                                                                                viewServiceConfig.getOMAGServerName(),
                                                                                viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                                                localServerUserId,
                                                                                localServerUserPassword,
                                                                                auditLog,
                                                                                accessServiceDescription.getAccessServiceURLMarker(),
                                                                                ViewServiceDescription.CONNECTION_MAKER.getViewServiceFullName(),
                                                                                maxPageSize,
                                                                                false);
                                    }

                                    connectorTypeHandlerMap.put(viewServiceURLMarker, connectorTypeHandler);
                                }
                            }
                        }
                    }
                }
            }
        }


        if (connectorTypeHandler == null)
        {
            throw new InvalidParameterException(ConnectionMakerErrorCode.INVALID_URL_MARKER.getMessageDefinition(viewServiceURLMarker),
                                                this.getClass().getName(),
                                                methodName,
                                                "viewServiceURLMarker");
        }

        return connectorTypeHandler;
    }




    /**
     * Return the client.  This client is from the Open Metadata Store services and is for maintaining
     * governance definition artifacts.
     *
     * @return client
     */
    public EndpointHandler getEndpointHandler(String viewServiceURLMarker,
                                                      String methodName) throws InvalidParameterException
    {
        EndpointHandler endpointHandler = null;

        if (viewServiceURLMarker != null)
        {
            endpointHandler = endpointHandlerMap.get(viewServiceURLMarker);

            if (endpointHandler == null)
            {
                for (ViewServiceConfig viewServiceConfig : activeViewServices)
                {
                    if (viewServiceConfig.getViewServiceURLMarker().equals(viewServiceURLMarker))
                    {
                        String viewServicePartnerService = viewServiceConfig.getViewServicePartnerService();

                        if (viewServicePartnerService != null)
                        {
                            for (AccessServiceDescription accessServiceDescription : AccessServiceDescription.values())
                            {
                                if (accessServiceDescription.getAccessServiceFullName().equals(viewServicePartnerService))
                                {
                                    if (localServerUserPassword != null)
                                    {
                                        endpointHandler = new EndpointHandler(serverName,
                                                                                      viewServiceConfig.getOMAGServerName(),
                                                                                      viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                                                      auditLog,
                                                                                      accessServiceDescription.getAccessServiceURLMarker(),
                                                                                      ViewServiceDescription.CONNECTION_MAKER.getViewServiceFullName(),
                                                                                      maxPageSize);
                                    }
                                    else
                                    {
                                        endpointHandler = new EndpointHandler(serverName,
                                                                                      viewServiceConfig.getOMAGServerName(),
                                                                                      viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                                                      localServerUserId,
                                                                                      localServerUserPassword,
                                                                                      auditLog,
                                                                                      accessServiceDescription.getAccessServiceURLMarker(),
                                                                                      ViewServiceDescription.CONNECTION_MAKER.getViewServiceFullName(),
                                                                                      maxPageSize);
                                    }

                                    endpointHandlerMap.put(viewServiceURLMarker, endpointHandler);
                                }
                            }
                        }
                    }
                }
            }
        }


        if (endpointHandler == null)
        {
            throw new InvalidParameterException(ConnectionMakerErrorCode.INVALID_URL_MARKER.getMessageDefinition(viewServiceURLMarker),
                                                this.getClass().getName(),
                                                methodName,
                                                "viewServiceURLMarker");
        }

        return endpointHandler;
    }
}
