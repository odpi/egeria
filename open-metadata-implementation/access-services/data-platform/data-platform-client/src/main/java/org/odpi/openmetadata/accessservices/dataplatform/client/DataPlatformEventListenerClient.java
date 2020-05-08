/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.client;

import org.odpi.openmetadata.accessservices.dataplatform.api.DataPlatformEventInterface;
import org.odpi.openmetadata.accessservices.dataplatform.api.DataPlatformEventListener;
import org.odpi.openmetadata.accessservices.dataplatform.connectors.outtopic.DataPlatformOutTopicClientConnector;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client.OCFRESTClient;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.ffdc.OMAGOCFErrorCode;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.ConnectionResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;



public class DataPlatformEventListenerClient  implements DataPlatformEventInterface
{
    private static final String  serviceName = "Data Platform OMAS";

    private String        serverName;               /* Initialized in constructor */
    private String        serverPlatformURLRoot;    /* Initialized in constructor */
    private OCFRESTClient restClient;               /* Initialized in constructor */

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    private DataPlatformOutTopicClientConnector configurationEventTopicConnector = null;
    private AuditLog                            auditLog                         = null;


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public DataPlatformEventListenerClient(String     serverName,
                                           String     serverPlatformURLRoot) throws InvalidParameterException
    {
        final String methodName = "Constructor (no security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient = new OCFRESTClient(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public DataPlatformEventListenerClient(String     serverName,
                                           String     serverPlatformURLRoot,
                                           String     userId,
                                           String     password) throws InvalidParameterException
    {
        final String methodName = "Constructor (with security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient = new OCFRESTClient(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param restClient pre-initialized REST client
     * @param maxPageSize pre-initialized parameter limit
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem with the information about the remote OMAS
     */
    public DataPlatformEventListenerClient(String        serverName,
                                           String        serverPlatformURLRoot,
                                           OCFRESTClient restClient,
                                           int           maxPageSize,
                                           AuditLog      auditLog) throws InvalidParameterException
    {
        final String methodName = "Constructor (with security)";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient = restClient;
        this.auditLog = auditLog;
    }



    /**
     * Return the name of the server where configuration is supposed to be stored.
     *
     * @return server name
     */
    public String getConfigurationServerName()
    {
        return serverName;
    }


    /**
     * Register a listener object that will be passed each of the events published by
     * the Data Platform OMAS.
     *
     * @param userId calling user
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void registerListener(String                    userId,
                                 DataPlatformEventListener listener) throws InvalidParameterException,
                                                                            ConnectionCheckedException,
                                                                            ConnectorCheckedException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String methodName = "registerListener";
        final String nameParameter = "listener";

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/data-platform/users/{1}/topics/out-topic-connection";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(listener, nameParameter, methodName);

        if (configurationEventTopicConnector == null)
        {
            /*
             * The connector is only created if/when a listener is registered to prevent unnecessary load on the
             * event bus.
             */
            ConnectionResponse restResult = restClient.callConnectionGetRESTCall(methodName,
                                                                                 serverPlatformURLRoot + urlTemplate,
                                                                                 serverName,
                                                                                 userId);

            Connection      topicConnection = restResult.getConnection();
            ConnectorBroker connectorBroker = new ConnectorBroker();
            Connector       connector       = connectorBroker.getConnector(topicConnection);

            if (connector == null)
            {
                throw new ConnectorCheckedException(OMAGOCFErrorCode.NULL_CONNECTOR_RETURNED.getMessageDefinition(topicConnection.getQualifiedName(),
                                                                                                                  serviceName,
                                                                                                                  serverName,
                                                                                                                  serverPlatformURLRoot),
                                                    this.getClass().getName(),
                                                    methodName);
            }

            if (connector instanceof DataPlatformOutTopicClientConnector)
            {
                configurationEventTopicConnector = (DataPlatformOutTopicClientConnector)connector;
                configurationEventTopicConnector.setAuditLog(auditLog);
                configurationEventTopicConnector.start();
            }
            else
            {
                throw new ConnectorCheckedException(OMAGOCFErrorCode.WRONG_TYPE_OF_CONNECTOR.getMessageDefinition(topicConnection.getQualifiedName(),
                                                                                                                  serviceName,
                                                                                                                  serverName,
                                                                                                                  serverPlatformURLRoot,
                                                                                                                  DataPlatformOutTopicClientConnector.class.getName()),
                                                    this.getClass().getName(),
                                                    methodName);
            }
        }

        configurationEventTopicConnector.registerListener(userId, listener);
    }
}
