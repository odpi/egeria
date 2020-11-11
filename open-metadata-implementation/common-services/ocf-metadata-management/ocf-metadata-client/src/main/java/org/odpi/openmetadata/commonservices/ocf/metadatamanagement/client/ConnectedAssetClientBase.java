/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.ffdc.OMAGOCFErrorCode;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.AssetResponse;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.ConnectionResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

/**
 * ConnectedAssetClientBase provides a base calls for clients that support an OCF interface.
 * In particular, it manages the retrieval of connections for assets, and the creation of connectors.
 */
public class ConnectedAssetClientBase
{
    protected String   serverName;               /* Initialized in constructor */
    protected String   serverPlatformRootURL;    /* Initialized in constructor */
    protected AuditLog auditLog;                 /* Initialized in constructor */

    protected InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    protected static NullRequestBody         nullRequestBody         = new NullRequestBody();


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog destination for log messages
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ConnectedAssetClientBase(String   serverName,
                                    String   serverPlatformRootURL,
                                    AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformRootURL, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformRootURL = serverPlatformRootURL;
        this.auditLog = auditLog;
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param maxPageSize maximum page size for this process
     * @param auditLog destination for log messages
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ConnectedAssetClientBase(String   serverName,
                                    String   serverPlatformRootURL,
                                    int      maxPageSize,
                                    AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformRootURL, serverName, methodName);
        invalidParameterHandler.setMaxPagingSize(maxPageSize);

        this.serverName = serverName;
        this.serverPlatformRootURL = serverPlatformRootURL;
        this.auditLog = auditLog;
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ConnectedAssetClientBase(String serverName,
                                    String serverPlatformRootURL) throws InvalidParameterException
    {
        this(serverName, serverPlatformRootURL, null);
    }


    /**
     * Return the basic properties of a asset.
     *
     * @param restClient client that calls REST APIs
     * @param serviceName name of the calling service
     * @param userId calling user
     * @param guid unique identifier of asset
     * @param methodName calling method
     *
     * @return Asset bean
     *
     * @throws InvalidParameterException the name is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    protected Asset getAssetSummary(OCFRESTClient  restClient,
                                    String         serviceName,
                                    String         userId,
                                    String         guid,
                                    String         methodName) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        final String   urlTemplate = "/servers/{0}/open-metadata/common-services/{1}/connected-asset/users/{2}/assets/{3}";

        AssetResponse restResult = restClient.callAssetGetRESTCall(methodName,
                                                                   serverPlatformRootURL + urlTemplate,
                                                                   serverName,
                                                                   serviceName,
                                                                   userId,
                                                                   guid);

        return restResult.getAsset();
    }


    /**
     * Returns a comprehensive collection of properties about the requested asset.
     *
     * @param serviceName name of the calling service
     * @param userId         userId of user making request.
     * @param assetGUID      unique identifier for asset.
     *
     * @return a comprehensive collection of properties about the asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving the asset properties from the property servers).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    protected AssetUniverse getAssetProperties(String serviceName,
                                               String userId,
                                               String assetGUID) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String   methodName = "getAssetProperties";
        final String   guidParameter = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, guidParameter, methodName);

        try
        {
            /*
             * Make use of the ConnectedAsset OMAS Service which provides the metadata services for the
             * Open Connector Framework (OCF).
             */
            return new ConnectedAssetUniverse(serviceName, serverName, serverPlatformRootURL, userId, assetGUID);
        }
        catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException error)
        {
            throw error;
        }
        catch (Throwable error)
        {
            throw new PropertyServerException(OMAGOCFErrorCode.NO_ASSET_PROPERTIES.getMessageDefinition(assetGUID,
                                                                                                        error.getClass().getName(),
                                                                                                        error.getMessage()),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Use the Open Connector Framework (OCF) to create a connector using the supplied connection.
     *
     * @param restClient client that calls REST APIs
     * @param serviceName calling service
     * @param userId calling user
     * @param requestedConnection  connection describing the required connector.
     * @param methodName  name of the calling method.
     *
     * @return a new connector.
     *
     * @throws ConnectionCheckedException  there are issues with the values in the connection
     * @throws ConnectorCheckedException the connector had an operational issue accessing the asset.
     */
    protected Connector getConnectorForConnection(OCFRESTClient   restClient,
                                                  String          serviceName,
                                                  String          userId,
                                                  Connection      requestedConnection,
                                                  String          methodName) throws ConnectionCheckedException,
                                                                                     ConnectorCheckedException
    {
        ConnectorBroker connectorBroker = new ConnectorBroker();

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
            throw new ConnectorCheckedException(OMAGOCFErrorCode.NULL_CONNECTOR_RETURNED.getMessageDefinition(requestedConnection.getQualifiedName(),
                                                                                                              serviceName,
                                                                                                              serverName,
                                                                                                              serverPlatformRootURL),
                                                this.getClass().getName(),
                                                methodName);
        }

        try
        {
            String  assetGUID = this.getAssetForConnection(restClient, serviceName, userId, requestedConnection.getGUID());

            /*
             * If the connector is successfully created, set up the Connected Asset Properties for the connector.
             * The properties should be retrieved from the open metadata repositories, so use an OMAS implementation
             * of the ConnectedAssetProperties object.
             */
            ConnectedAssetProperties connectedAssetProperties = new ConnectedAssetProperties(serviceName,
                                                                                             serverName,
                                                                                             userId,
                                                                                             serverPlatformRootURL,
                                                                                             newConnector.getConnectorInstanceId(),
                                                                                             newConnector.getConnection(),
                                                                                             assetGUID);

            /*
             * Pass the new connected asset properties to the connector
             */
            newConnector.initializeConnectedAssetProperties(connectedAssetProperties);
        }
        catch (Throwable  error)
        {
            /*
             * Ignore error - connectedAssetProperties is left at null.
             */
        }

        /*
         * At this stage, the asset properties are not retrieved from the server.  This does not happen until the caller
         * issues a connector.getConnectedAssetProperties.  This causes the connectedAssetProperties.refresh() call
         * to be made, which contacts the OMAS server and retrieves the asset properties.
         *
         * Delaying the population of the connected asset properties ensures the latest values are returned to the
         * caller (consider a long running connection).  Alternatively, these properties may not ever be used by the
         * caller so retrieving the properties at this point would be unnecessary.
         */

        return newConnector;
    }


    /**
     * Returns the connection corresponding to the supplied connection GUID.
     *
     * @param restClient client that calls REST APIs
     * @param serviceName name of the calling service
     * @param userId userId of user making request.
     * @param guid   the unique id for the connection within the metadata repository.
     *
     * @return connection instance.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    protected Connection getConnectionByGUID(OCFRESTClient  restClient,
                                             String         serviceName,
                                             String         userId,
                                             String         guid) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String   methodName  = "getConnectionByGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/common-services/{1}/connected-asset/users/{2}/connections/{3}";

        ConnectionResponse   restResult = restClient.callConnectionGetRESTCall(methodName,
                                                                               serverPlatformRootURL + urlTemplate,
                                                                               serverName,
                                                                               serviceName,
                                                                               userId,
                                                                               guid);

         return restResult.getConnection();
    }


    /**
     * Returns the connection object corresponding to the supplied connection name.
     *
     * @param restClient client that calls REST APIs
     * @param serviceName name of the calling service
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
                                             String         serviceName,
                                             String         userId,
                                             String         name) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String   methodName = "getConnectionByName";
        final String   urlTemplate = "/servers/{0}/open-metadata/common-services/{1}/connected-asset/users/{2}/connections/by-name/{3}";

        ConnectionResponse restResult = restClient.callConnectionGetRESTCall(methodName,
                                                                             serverPlatformRootURL + urlTemplate,
                                                                             serverName,
                                                                             serviceName,
                                                                             userId,
                                                                             name);

        return restResult.getConnection();
    }

    /**
     * Returns the connection corresponding to the supplied asset GUID.
     *
     * @param restClient client that calls REST APIs
     * @param serviceName name of the calling service
     * @param userId       userId of user making request.
     * @param assetGUID   the unique id for the asset within the metadata repository.
     *
     * @return Connector   connector instance.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    protected Connection getConnectionForAsset(OCFRESTClient  restClient,
                                               String         serviceName,
                                               String         userId,
                                               String         assetGUID) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String   methodName = "getConnectionForAsset";
        final String   urlTemplate = "/servers/{0}/open-metadata/common-services/{1}/connected-asset/users/{2}/assets/{3}/connection";

        ConnectionResponse restResult = restClient.callConnectionGetRESTCall(methodName,
                                                                             serverPlatformRootURL + urlTemplate,
                                                                             serverName,
                                                                             serviceName,
                                                                             userId,
                                                                             assetGUID);

        return restResult.getConnection();
    }


    /**
     * Returns the unique identifier for the asset connected to the requested connection.
     *
     * @param restClient initialized client for calling REST APIs.
     * @param serviceName name of the calling service.
     * @param userId the userId of the requesting user.
     * @param connectionGUID  unique identifier for the connection.
     *
     * @return unique identifier of asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    protected String  getAssetForConnection(OCFRESTClient  restClient,
                                            String         serviceName,
                                            String         userId,
                                            String         connectionGUID) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String   methodName = "getAssetForConnection";
        final String   urlTemplate = "/servers/{0}/open-metadata/common-services/{1}/connected-asset/users/{2}/assets/by-connection/{3}";

        GUIDResponse restResult = restClient.callGUIDGetRESTCall(methodName,
                                                                 serverPlatformRootURL + urlTemplate,
                                                                 serverName,
                                                                 serviceName,
                                                                 userId,
                                                                 connectionGUID);

        return restResult.getGUID();
    }
}
