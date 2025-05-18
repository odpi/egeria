/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.client;

import org.odpi.openmetadata.accessservices.itinfrastructure.api.ITInfrastructureEventInterface;
import org.odpi.openmetadata.accessservices.itinfrastructure.api.ITInfrastructureEventListener;
import org.odpi.openmetadata.accessservices.itinfrastructure.connectors.outtopic.ITInfrastructureOutTopicClientConnector;
import org.odpi.openmetadata.accessservices.itinfrastructure.ffdc.ITInfrastructureErrorCode;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client.OCFRESTClient;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest.ConnectionResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;


/**
 * ITInfrastructureEventClient provides the implementation to manage the interaction with the server to
 * set up a listener to support the receipt of inbound events from the IT Infrastructure OMAS Out Topic.
 */
public class ITInfrastructureEventClient implements ITInfrastructureEventInterface
{
    private static final String  serviceName = "IT Infrastructure OMAS";

    private String                 serverName;               /* Initialized in constructor */
    private String                 serverPlatformURLRoot;    /* Initialized in constructor */
    private OCFRESTClient          restClient;               /* Initialized in constructor */
    private AuditLog               auditLog;                 /* Initialized in constructor */
    private String                 callerId;                 /* Initialized in constructor */

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    private ITInfrastructureOutTopicClientConnector configurationEventTopicConnector = null;



    /**
     * Create a new client that is to be used within an OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param serverUserId this server's userId
     * @param serverPassword this server's userId
     * @param maxPageSize pre-initialized parameter limit
     * @param auditLog logging destination
     * @param callerId unique identifier of the caller
     * @throws InvalidParameterException there is a problem with the information about the remote OMAS
     */
    public ITInfrastructureEventClient(String   serverName,
                                       String   serverPlatformURLRoot,
                                       String   serverUserId,
                                       String   serverPassword,
                                       int      maxPageSize,
                                       AuditLog auditLog,
                                       String   callerId) throws InvalidParameterException
    {
        final String methodName = "Constructor (with REST Client)";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName            = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        if (serverPassword == null)
        {
            this.restClient = new OCFRESTClient(serverName, serverPlatformURLRoot);
        }
        else
        {
            this.restClient = new OCFRESTClient(serverName, serverPlatformURLRoot, serverUserId, serverPassword, auditLog);
        }

        this.auditLog              = auditLog;
        this.callerId              = callerId;
    }



    /**
     * Create a new client that is to be used within an OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient pre-initialized REST client
     * @param maxPageSize pre-initialized parameter limit
     * @param auditLog logging destination
     * @param callerId unique identifier of the caller
     * @throws InvalidParameterException there is a problem with the information about the remote OMAS
     */
    public ITInfrastructureEventClient(String        serverName,
                                       String        serverPlatformURLRoot,
                                       OCFRESTClient restClient,
                                       int           maxPageSize,
                                       AuditLog      auditLog,
                                       String        callerId) throws InvalidParameterException
    {
        final String methodName = "Constructor (with REST Client)";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName            = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient            = restClient;
        this.auditLog              = auditLog;
        this.callerId              = callerId;
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
     * the IT Infrastructure OMAS.
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
    @Override
    public void registerListener(String                    userId,
                                 ITInfrastructureEventListener listener) throws InvalidParameterException,
                                                                                ConnectionCheckedException,
                                                                                ConnectorCheckedException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName = "registerListener";
        final String nameParameter = "listener";
        final String callerIdParameter = "callerId";

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/it-infrastructure/users/{1}/topics/out-topic-connection/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(listener, nameParameter, methodName);
        invalidParameterHandler.validateName(callerId, callerIdParameter, methodName);

        if (configurationEventTopicConnector == null)
        {
            /*
             * The connector is only created if/when a listener is registered to prevent unnecessary load on the
             * event bus.
             */
            ConnectionResponse restResult = restClient.callOCFConnectionGetRESTCall(methodName,
                                                                                    urlTemplate,
                                                                                    serverName,
                                                                                    userId,
                                                                                    callerId);

            Connection      topicConnection = restResult.getConnection();
            ConnectorBroker connectorBroker = new ConnectorBroker(auditLog);
            Connector       connector       = connectorBroker.getConnector(topicConnection);

            if (connector == null)
            {
                throw new ConnectorCheckedException(ITInfrastructureErrorCode.NULL_CONNECTOR_RETURNED.getMessageDefinition(topicConnection.getQualifiedName(),
                                                                                                                           serviceName,
                                                                                                                           serverName,
                                                                                                                           serverPlatformURLRoot),
                                                    this.getClass().getName(),
                                                    methodName);
            }

            if (connector instanceof ITInfrastructureOutTopicClientConnector)
            {
                configurationEventTopicConnector = (ITInfrastructureOutTopicClientConnector)connector;
                configurationEventTopicConnector.start();
            }
            else
            {
                throw new ConnectorCheckedException(ITInfrastructureErrorCode.WRONG_TYPE_OF_CONNECTOR.getMessageDefinition(topicConnection.getQualifiedName(),
                                                                                                                  serviceName,
                                                                                                                  serverName,
                                                                                                                  serverPlatformURLRoot,
                                                                                                                  ITInfrastructureOutTopicClientConnector.class.getName()),
                                                    this.getClass().getName(),
                                                    methodName);
            }
        }

        configurationEventTopicConnector.registerListener(userId, listener);
    }
}
