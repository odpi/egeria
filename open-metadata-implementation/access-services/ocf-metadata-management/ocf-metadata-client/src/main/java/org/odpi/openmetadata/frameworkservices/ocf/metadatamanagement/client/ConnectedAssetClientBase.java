/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.OCFConnectionResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.connectors.client.ConnectedAssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.ffdc.OCFServicesErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;


/**
 * ConnectedAssetClient provides a base calls for clients that support an OCF interface.
 * In particular, it manages the retrieval of connections for assets, and the creation of connectors.
 */
public class ConnectedAssetClientBase extends ConnectedAssetClient
{

    protected InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    private final OCFRESTClient ocfRESTClient;



    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OCF REST services
     * @param maxPageSize maximum page size for this process
     * @param auditLog destination for log messages
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ConnectedAssetClientBase(String   serverName,
                                    String   serverPlatformURLRoot,
                                    String   localServerSecretsStoreProvider,
                                    String   localServerSecretsStoreLocation,
                                    String   localServerSecretsStoreCollection,
                                    int      maxPageSize,
                                    AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, maxPageSize, auditLog);

        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.setMaxPagingSize(maxPageSize);

        this.ocfRESTClient = new OCFRESTClient(serverName, serverPlatformURLRoot, localServerSecretsStoreProvider, localServerSecretsStoreLocation, localServerSecretsStoreCollection, auditLog);
    }


    /**
     * Use the Open Connector Framework (OCF) to create a connector using the supplied connection.
     * The connector is initialized by not started.
     *
     * @param requestedConnection  connection describing the required connector
     * @param methodName  name of the calling method.
     *
     * @return a new connector.
     *
     * @throws ConnectionCheckedException  there are issues with the values in the connection
     * @throws ConnectorCheckedException the connector had an operational issue accessing the asset.
     * @throws UserNotAuthorizedException connector disconnected
     */
    protected Connector getConnectorForConnection(Connection      requestedConnection,
                                                  String          methodName) throws ConnectionCheckedException,
                                                                                     ConnectorCheckedException,
                                                                                     UserNotAuthorizedException
    {
        ConnectorBroker connectorBroker = new ConnectorBroker(auditLog);

        /*
         * Pass the connection to the ConnectorBroker to create the connector instance.
         * Again, exceptions from this process are returned directly to the caller.
         */
        Connector newConnector = connectorBroker.getConnector(requestedConnection);

        /*
         * If no exception is thrown by getConnector, we should have a connector instance.
         */
        if (newConnector == null)
        {
            /*
             * This is probably some sort of logic error since the connector should have been returned.
             * Whatever the cause, the process can not proceed without a connector.
             */
            throw new ConnectorCheckedException(OCFServicesErrorCode.NULL_CONNECTOR_RETURNED.getMessageDefinition(requestedConnection.getQualifiedName(),
                                                                                                                  serverName,
                                                                                                                  serverPlatformURLRoot),
                                                this.getClass().getName(),
                                                methodName);
        }

        return newConnector;
    }


    /**
     * Returns the connection corresponding to the supplied connection GUID.
     *
     * @param restClient client that calls REST APIs
     * @param userId userId of user making request.
     * @param guid   the unique id for the connection within the metadata repository.
     *
     * @return connection instance.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    protected Connection getConnectionByGUID(OCFRESTClient  restClient,
                                             String         userId,
                                             String         guid) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String   methodName  = "getConnectionByGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/connected-asset/users/{1}/connections/{2}";

        OCFConnectionResponse restResult = restClient.callOCFConnectionGetRESTCall(methodName,
                                                                                   serverPlatformURLRoot + urlTemplate,
                                                                                   serverName,
                                                                                   userId,
                                                                                   guid);

         return restResult.getConnection();
    }


    /**
     * Returns the connection object corresponding to the supplied connection name.
     *
     * @param restClient client that calls REST APIs
     * @param userId  String - userId of user making request.
     * @param name  this is the qualifiedName of the connection.
     *
     * @return Connection retrieved from property server.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property (metadata) server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    protected Connection getConnectionByName(OCFRESTClient  restClient,
                                             String         userId,
                                             String         name) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String   methodName = "getConnectionByName";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/connected-asset/users/{1}/connections/by-name/{2}";

        OCFConnectionResponse restResult = restClient.callOCFConnectionGetRESTCall(methodName,
                                                                                   serverPlatformURLRoot + urlTemplate,
                                                                                   serverName,
                                                                                   userId,
                                                                                   name);

        return restResult.getConnection();
    }


    /**
     * Returns the connection corresponding to the supplied asset GUID.
     *
     * @param restClient client that calls REST APIs
     * @param userId       userId of user making request.
     * @param assetGUID   the unique id for the asset within the metadata repository.
     *
     * @return connection
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public Connection getConnectionForAsset(OCFRESTClient  restClient,
                                            String         userId,
                                            String         assetGUID) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String   methodName = "getConnectionForAsset";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/connected-asset/users/{1}/assets/{2}/connection";

        OCFConnectionResponse restResult = restClient.callOCFConnectionGetRESTCall(methodName,
                                                                                   serverPlatformURLRoot + urlTemplate,
                                                                                   serverName,
                                                                                   userId,
                                                                                   assetGUID);

        return restResult.getConnection();
    }


    /**
     * Returns the unique identifier corresponding to the supplied connection.
     *
     * @param userId calling user
     * @param connection   the connection object that contains the properties needed to create the connection.
     *
     * @return guid
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     */
    @Override
    public String saveConnection(String     userId,
                                 Connection connection) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException,
                                                               ConnectionCheckedException,
                                                               ConnectorCheckedException
    {
        final String methodName = "saveConnection";
        final String connectionParameterName = "connection";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/{1}/connected-asset/users/{2}/connections";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(connection, connectionParameterName, methodName);

        GUIDResponse restResult = ocfRESTClient.callGUIDPostRESTCall(methodName,
                                                                    serverPlatformURLRoot + urlTemplate,
                                                                    connection,
                                                                    serverName,
                                                                    userId);

        return restResult.getGUID();
    }


    /**
     * Returns the unique identifier corresponding to the supplied connection.
     *
     * @param userId calling user
     * @param assetGUID the unique identifier of an asset to attach the connection to
     * @param connection   the connection object that contains the properties needed to create the connection.
     *
     * @return guid
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     */
    @Override
    public String saveConnection(String     userId,
                                 String     assetGUID,
                                 Connection connection) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException,
                                                               ConnectionCheckedException,
                                                               ConnectorCheckedException
    {
        final String methodName = "saveConnection";
        final String connectionParameterName = "connection";
        final String assetParameterName = "assetGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/{1}/connected-asset/users/{2}/connections?assetGUID={3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetParameterName, methodName);
        invalidParameterHandler.validateObject(connection, connectionParameterName, methodName);

        GUIDResponse restResult = ocfRESTClient.callGUIDPostRESTCall(methodName,
                                                                     serverPlatformURLRoot + urlTemplate,
                                                                     connection,
                                                                     serverName,
                                                                     userId,
                                                                     assetGUID);

        return restResult.getGUID();
    }


    /*
     * ===============================================
     * ConnectorFactoryInterface
     * ===============================================
     */


    /**
     * Returns the connector corresponding to the supplied connection name.
     *
     * @param userId           userId of user making request.
     * @param connectionName   this may be the qualifiedName or displayName of the connection.
     *
     * @return   connector instance - or null if there is no connection
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public Connector getConnectorByName(String userId,
                                        String connectionName) throws InvalidParameterException,
                                                                      ConnectionCheckedException,
                                                                      ConnectorCheckedException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        final String methodName = "getConnectorByName";
        final String nameParameter = "connectionName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(connectionName, nameParameter, methodName);

        Connection connection = this.getConnectionByName(ocfRESTClient, userId, connectionName);

        if (connection != null)
        {
            return this.getConnectorForConnection(connection, methodName);
        }

        return null;
    }


    /**
     * Returns the connector corresponding to the supplied asset GUID.
     *
     * @param userId       userId of user making request.
     * @param assetGUID   the unique id for the asset within the metadata repository.
     *
     * @return    connector instance - or null if there is no connection
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public Connector getConnectorForAsset(String   userId,
                                          String   assetGUID) throws InvalidParameterException,
                                                                     ConnectionCheckedException,
                                                                     ConnectorCheckedException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        return this.getConnectorForAsset(userId, assetGUID, null);
    }


    /**
     * Returns the connector corresponding to the supplied asset GUID.
     *
     * @param userId       userId of user making request.
     * @param assetGUID   the unique id for the asset within the metadata repository.
     * @param auditLog    optional logging destination
     *
     * @return    connector instance - or null if there is no connection
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public Connector getConnectorForAsset(String   userId,
                                          String   assetGUID,
                                          AuditLog auditLog) throws InvalidParameterException,
                                                                    ConnectionCheckedException,
                                                                    ConnectorCheckedException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        final  String  methodName = "getConnectorForAsset";
        final  String  guidParameter = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, guidParameter, methodName);

        Connection connection = this.getConnectionForAsset(ocfRESTClient, userId, assetGUID);

        if (connection != null)
        {
            Connector connector = this.getConnectorForConnection(connection, methodName);

            if ((auditLog != null) && (connector instanceof AuditLoggingComponent auditLoggingComponent))
            {
                /*
                 * This set up the connector to log messages in the integration connector's audit log.
                 */
                auditLoggingComponent.setAuditLog(auditLog);
            }

            return connector;
        }

        return null;
    }


    /**
     * Returns the connector corresponding to the supplied connection GUID.
     *
     * @param userId           userId of user making request.
     * @param connectionGUID   the unique id for the connection within the metadata repository.
     *
     * @return  connector instance - or null if there is no connection
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public Connector getConnectorByGUID(String userId,
                                        String connectionGUID) throws InvalidParameterException,
                                                                      ConnectionCheckedException,
                                                                      ConnectorCheckedException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        final  String  methodName = "getConnectorByGUID";
        final  String  guidParameter = "connectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, guidParameter, methodName);

        Connection connection = this.getConnectionByGUID(ocfRESTClient, userId, connectionGUID);

        if (connection != null)
        {
            return this.getConnectorForConnection(connection, methodName);
        }

        return null;
    }


    /**
     * Returns the connector corresponding to the supplied connection.
     *
     * @param userId       userId of user making request.
     * @param connection   the connection object that contains the properties needed to create the connection.
     *
     * @return  connector instance
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws UserNotAuthorizedException connector disconnected
     */
    @Override
    public Connector  getConnectorByConnection(String     userId,
                                               Connection connection) throws InvalidParameterException,
                                                                             ConnectionCheckedException,
                                                                             ConnectorCheckedException,
                                                                             UserNotAuthorizedException
    {
        final  String  methodName = "getConnectorByConnection";

        invalidParameterHandler.validateUserId(userId, methodName);

        return this.getConnectorForConnection(connection, methodName);
    }


}
