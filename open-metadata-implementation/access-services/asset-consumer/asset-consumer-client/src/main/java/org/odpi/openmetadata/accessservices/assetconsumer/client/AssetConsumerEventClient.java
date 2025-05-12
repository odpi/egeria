/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.client;

import org.odpi.openmetadata.accessservices.assetconsumer.api.AssetConsumerEventInterface;
import org.odpi.openmetadata.accessservices.assetconsumer.api.AssetConsumerEventListener;
import org.odpi.openmetadata.accessservices.assetconsumer.connectors.outtopic.AssetConsumerOutTopicClientConnector;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.AssetConsumerErrorCode;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client.OCFRESTClient;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest.ConnectionResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;


/**
 * AssetConsumerEventClient provides the implementation to manage the interaction with the server to
 * set up a listener to support the receipt of inbound events from the Asset Consumer OMAS Out Topic.
 */
public class AssetConsumerEventClient implements AssetConsumerEventInterface
{
    private static final String  serviceName = "Asset Consumer OMAS";

    private final String                 serverName;               /* Initialized in constructor */
    private final String                 serverPlatformURLRoot;    /* Initialized in constructor */
    private final OCFRESTClient          restClient;               /* Initialized in constructor */
    private final AuditLog               auditLog;                 /* Initialized in constructor */
    private final String                 callerId;                 /* Initialized in constructor */

    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    private AssetConsumerOutTopicClientConnector configurationEventTopicConnector = null;



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
    public AssetConsumerEventClient(String                 serverName,
                                    String                 serverPlatformURLRoot,
                                    String                 serverUserId,
                                    String                 serverPassword,
                                    int                    maxPageSize,
                                    AuditLog               auditLog,
                                    String                 callerId) throws InvalidParameterException
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
     * the Asset Consumer OMAS.
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
    public void registerListener(String                     userId,
                                 AssetConsumerEventListener listener) throws InvalidParameterException,
                                                                            ConnectionCheckedException,
                                                                            ConnectorCheckedException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String methodName = "registerListener";
        final String nameParameter = "listener";
        final String callerIdParameter = "callerId";

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/topics/out-topic-connection/{2}";

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
                throw new ConnectorCheckedException(AssetConsumerErrorCode.NULL_CONNECTOR_RETURNED.getMessageDefinition(topicConnection.getQualifiedName(),
                                                                                                                       serviceName,
                                                                                                                       serverName,
                                                                                                                       serverPlatformURLRoot),
                                                    this.getClass().getName(),
                                                    methodName);
            }

            if (connector instanceof AssetConsumerOutTopicClientConnector)
            {
                configurationEventTopicConnector = (AssetConsumerOutTopicClientConnector)connector;
                configurationEventTopicConnector.start();
            }
            else
            {
                throw new ConnectorCheckedException(AssetConsumerErrorCode.WRONG_TYPE_OF_CONNECTOR.getMessageDefinition(topicConnection.getQualifiedName(),
                                                                                                                  serviceName,
                                                                                                                  serverName,
                                                                                                                  serverPlatformURLRoot,
                                                                                                                  AssetConsumerOutTopicClientConnector.class.getName()),
                                                    this.getClass().getName(),
                                                    methodName);
            }
        }

        configurationEventTopicConnector.registerListener(userId, listener);
    }
}
