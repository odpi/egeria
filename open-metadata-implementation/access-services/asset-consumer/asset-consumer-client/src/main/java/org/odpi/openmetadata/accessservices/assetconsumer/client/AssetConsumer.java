/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.client;

import org.odpi.openmetadata.accessservices.assetconsumer.AssetConsumerInterface;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.AssetConsumerErrorCode;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.*;
import org.odpi.openmetadata.accessservices.assetconsumer.rest.*;
import org.odpi.openmetadata.accessservices.connectedasset.client.ConnectedAsset;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.CommentType;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectedAssetProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.StarRating;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * The Asset Consumer Open Metadata Access Service (OMAS) is used by applications and tools as a factory for Open
 * Connector Framework (OCF) connectors.  The configuration for the connectors is managed as open metadata in
 * a Connection definition.  The caller to the Asset Consumer OMAS passes either the name, GUID or URL for the
 * connection to the appropriate method to retrieve a connector.  The Asset Consumer OMAS retrieves the connection
 * from the metadata repository and creates an appropriate connector as described the connection and
 * returns it to the caller.
 *
 * Each connection has a unique guid and a name.  An application can request a connector instance
 * from the OCF's Connector Broker using the guid, name or URL of a connection, or by passing a fully
 * populated connection object.  If the connection guid, name or URL is used, AssetConsumer OMAS
 * looks up the connection properties in the metadata repository before calling the OCF ConnectorBroker to create the
 * connector.
 *
 * The Asset Consumer OMAS supports access to the asset properties either through the connector, or by a direct
 * call to Asset Consumer API.  It also provides access to the note log contents and comment conversations
 *
 * In addition it is possible to maintain feedback for the asset through the Asset Consumer OMAS.
 * This is in terms of tags, star ratings, likes and comments. There is also the ability to add audit log records
 * related to the use of the asset through the Asset Consumer OMAS and finally manage a list of favourite assets.
 *
 * The AssetConsumer OMAS interface is divided into three parts:
 * <ul>
 *     <li>Client-side only services that work with the OCF to create requested connectors.</li>
 *     <li>OMAS Server calls to retrieve connection information on behalf of the OCF.</li>
 *     <li>OMAS Server calls to add feedback to the asset.</li>
 * </ul>
 */
public class AssetConsumer implements AssetConsumerInterface
{
    private String           serverName;     /* Initialized in constructor */
    private String           omasServerURL;  /* Initialized in constructor */
    private NullRequestBody  nullRequestBody = new NullRequestBody();


    /**
     * Create a new AssetConsumer client.
     *
     * @param serverName name of the server to connect to
     * @param newServerURL the network address of the server running the OMAS REST servers
     */
    public AssetConsumer(String     serverName,
                         String     newServerURL)
    {
        this.serverName = serverName;
        this.omasServerURL = newServerURL;
    }


    /**
     * Use the Open Connector Framework (OCF) to create a connector using the supplied connection.
     *
     * @param requestedConnection  connection describing the required connector.
     * @param methodName  name of the calling method.
     *
     * @return a new connector.
     *
     * @throws ConnectionCheckedException  there are issues with the values in the connection
     * @throws ConnectorCheckedException the connector had an operational issue accessing the asset.
     */
    private Connector  getConnectorForConnection(String          userId,
                                                 Connection      requestedConnection,
                                                 String          methodName) throws ConnectionCheckedException,
                                                                                    ConnectorCheckedException
    {
        ConnectorBroker  connectorBroker = new ConnectorBroker();

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
            AssetConsumerErrorCode errorCode    = AssetConsumerErrorCode.NULL_CONNECTOR_RETURNED;
            String                 errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();

            throw new AssetConsumerRuntimeException(errorCode.getHTTPErrorCode(),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    errorMessage,
                                                    errorCode.getSystemAction(),
                                                    errorCode.getUserAction(),
                                                    null);
        }

        try
        {
            String  assetGUID = this.getAssetForConnection(userId, requestedConnection.getGUID());

            /*
             * If the connector is successfully created, set up the Connected Asset Properties for the connector.
             * The properties should be retrieved from the open metadata repositories, so use an OMAS implementation
             * of the ConnectedAssetProperties object.
             */
            ConnectedAssetProperties connectedAssetProperties
                    = new org.odpi.openmetadata.accessservices.connectedasset.client.ConnectedAssetProperties(serverName,
                                                                                                              userId,
                                                                                                              omasServerURL,
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
     * Returns the connection object corresponding to the supplied connection name.
     *
     * @param userId  String - userId of user making request.
     * @param name  this may be the qualifiedName or displayName of the connection.
     *
     * @return Connection retrieved from property server.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UnrecognizedConnectionNameException there is no connection defined for this name.
     * @throws AmbiguousConnectionNameException there is more than one connection defined for this name.
     * @throws PropertyServerException there is a problem retrieving information from the property (metadata) server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private Connection getConnectionByName(String   userId,
                                           String   name) throws InvalidParameterException,
                                                                 UnrecognizedConnectionNameException,
                                                                 AmbiguousConnectionNameException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        final String   methodName = "getConnectionByName";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/connection/by-name/{2}";

        validateOMASServerURL(methodName);

        ConnectionResponse restResult = callConnectionGetRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  serverName,
                                                                  userId,
                                                                  name);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowUnrecognizedConnectionNameException(methodName, restResult);
        this.detectAndThrowAmbiguousConnectionNameException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getConnection();
    }


    /**
     * Returns the connector corresponding to the supplied connection name.
     *
     * @param userId           userId of user making request.
     * @param connectionName   this may be the qualifiedName or displayName of the connection.
     *
     * @return Connector   connector instance.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UnrecognizedConnectionNameException there is no connection defined for this name.
     * @throws AmbiguousConnectionNameException there is more than one connection defined for this name.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public Connector getConnectorByName(String   userId,
                                        String   connectionName) throws InvalidParameterException,
                                                                        UnrecognizedConnectionNameException,
                                                                        AmbiguousConnectionNameException,
                                                                        ConnectionCheckedException,
                                                                        ConnectorCheckedException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String   methodName = "getConnectorByName";
        final  String  nameParameter = "connectionName";


        validateOMASServerURL(methodName);
        validateUserId(userId, methodName);
        validateName(connectionName, nameParameter, methodName);

        return this.getConnectorForConnection(userId,
                                              this.getConnectionByName(userId, connectionName),
                                              methodName);
    }



    /**
     * Returns the connection corresponding to the supplied connection GUID.
     *
     * @param userId userId of user making request.
     * @param guid   the unique id for the connection within the metadata repository.
     *
     * @return connection instance.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UnrecognizedConnectionGUIDException the supplied GUID is not recognized by the property server
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private Connection getConnectionByGUID(String     userId,
                                           String     guid) throws InvalidParameterException,
                                                                   UnrecognizedConnectionGUIDException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String   methodName  = "getConnectionByGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/connection/{2}";

        validateOMASServerURL(methodName);

        ConnectionResponse   restResult = callConnectionGetRESTCall(methodName,
                                                                    omasServerURL + urlTemplate,
                                                                    serverName,
                                                                    userId,
                                                                    guid);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowUnrecognizedConnectionGUIDException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getConnection();
    }



    /**
     * Returns the connector corresponding to the supplied connection GUID.
     *
     * @param userId           userId of user making request.
     * @param connectionGUID   the unique id for the connection within the metadata repository.
     *
     * @return Connector   connector instance.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UnrecognizedConnectionGUIDException the supplied GUID is not recognized by the property server.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public Connector getConnectorByGUID(String     userId,
                                        String     connectionGUID) throws InvalidParameterException,
                                                                          UnrecognizedConnectionGUIDException,
                                                                          ConnectionCheckedException,
                                                                          ConnectorCheckedException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final  String  methodName = "getConnectorByGUID";
        final  String  guidParameter = "connectionGUID";

        validateOMASServerURL(methodName);
        validateUserId(userId, methodName);
        validateGUID(connectionGUID, guidParameter, methodName);

        return this.getConnectorForConnection(userId,
                                              this.getConnectionByGUID(userId, connectionGUID),
                                              methodName);
    }


    /**
     * Returns the connector corresponding to the supplied connection.
     *
     * @param userId       userId of user making request.
     * @param connection   the connection object that contains the properties needed to create the connection.
     *
     * @return Connector   connector instance
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     */
    public Connector  getConnectorByConnection(String        userId,
                                               Connection    connection) throws InvalidParameterException,
                                                                                ConnectionCheckedException,
                                                                                ConnectorCheckedException,
                                                                                PropertyServerException
    {
        final  String  methodName = "getConnectorByConnection";

        validateOMASServerURL(methodName);
        validateUserId(userId, methodName);

        return this.getConnectorForConnection(userId, connection, methodName);
    }


    /**
     * Return the profile for this user.
     *
     * @param userId userId of the user making the request.
     *
     * @return profile object
     *
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws NoProfileForUserException the user does not have a profile.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public MyProfile getMyProfile(String userId) throws InvalidParameterException,
                                                        NoProfileForUserException,
                                                        PropertyServerException,
                                                        UserNotAuthorizedException
    {
        final String   methodName = "getMyProfile";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/my-profile";


        validateOMASServerURL(methodName);
        validateUserId(userId, methodName);

        MyProfileResponse restResult = callMyProfileGetRESTCall(methodName,
                                                                omasServerURL + urlTemplate,
                                                                userId);

        detectAndThrowInvalidParameterException(methodName, restResult);
        detectAndThrowNoProfileForUserException(methodName, restResult);
        detectAndThrowUserNotAuthorizedException(methodName, restResult);
        detectAndThrowPropertyServerException(methodName, restResult);

        return null;
    }


    /**
     * Create or update the profile for the requesting user.
     *
     * @param userId the name of the calling user.
     * @param employeeNumber personnel/serial/unique employee number of the individual.
     * @param fullName full name of the person.
     * @param knownName known name or nickname of the individual.
     * @param jobTitle job title of the individual.
     * @param jobRoleDescription job description of the individual.
     * @param additionalProperties  additional properties about the individual.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateMyProfile(String              userId,
                                String              employeeNumber,
                                String              fullName,
                                String              knownName,
                                String              jobTitle,
                                String              jobRoleDescription,
                                Map<String, Object> additionalProperties) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        final String   methodName = "updateMyProfile";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/my-profile";

        final String   employeeNumberParameterName = "employeeNumber";
        final String   knownNameParameterName = "knownName";

        validateOMASServerURL(methodName);
        validateUserId(userId, methodName);
        validateName(employeeNumber, employeeNumberParameterName, methodName);
        validateName(knownName, knownNameParameterName, methodName);

        MyProfileRequestBody  requestBody = new MyProfileRequestBody();
        requestBody.setEmployeeNumber(employeeNumber);
        requestBody.setFullName(fullName);
        requestBody.setKnownName(knownName);
        requestBody.setJobTitle(jobTitle);
        requestBody.setJobRoleDescription(jobRoleDescription);
        requestBody.setAdditionalProperties(additionalProperties);


        VoidResponse restResult = callVoidPostRESTCall(methodName,
                                                       omasServerURL + urlTemplate,
                                                       requestBody,
                                                       serverName,
                                                       userId);

        detectAndThrowInvalidParameterException(methodName, restResult);
        detectAndThrowUserNotAuthorizedException(methodName, restResult);
        detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Returns the list of asset collections that this user has created or linked to their profile.
     *
     * @param userId     userId of user making request
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     *
     * @return a list of asset collections
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws NoProfileForUserException  the user does not have a profile so can not have any asset collections.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<AssetCollection>    getMyAssetCollections(String    userId,
                                                          int       startFrom,
                                                          int       pageSize) throws InvalidParameterException,
                                                                                     NoProfileForUserException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        return null;
    }


    /**
     * Return the properties of a specific asset collection,
     *
     * @param userId              userId of user making request.
     * @param assetCollectionGUID unique identifier of the required connection.
     *
     * @return asset collection properties
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws NoProfileForUserException  the user does not have a profile so can not have any asset collections.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public AssetCollection     getAssetCollection(String    userId,
                                                  String    assetCollectionGUID) throws InvalidParameterException,
                                                                                        NoProfileForUserException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        return null;
    }

    /**
     * Create a new asset collection.
     *
     * @param userId                 userId of user making request.
     * @param qualifiedName          unique name of the asset collection.
     * @param displayName            short displayable name for the asset collection.
     * @param description            description of the asset collection.
     * @param collectionUse          description of how the asset consumer intends to use the collection.
     * @param assetCollectionOrder   description of how the assets in the collection should be organized (null for no organization).
     * @param additionalProperties   additional arbitrary properties.
     *
     * @return the newly created AssetCollection
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws NoProfileForUserException  the user does not have a profile so can not have any asset collections.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public AssetCollection     createAssetCollection(String                 userId,
                                                     String                 qualifiedName,
                                                     String                 displayName,
                                                     String                 description,
                                                     String                 collectionUse,
                                                     AssetCollectionOrder   assetCollectionOrder,
                                                     Map<String, Object>    additionalProperties) throws InvalidParameterException,
                                                                                                         NoProfileForUserException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        return null;
    }


    /**
     * Connect an existing asset collection to this user's profile.
     *
     * @param userId               userId of user making request.
     * @param assetCollectionGUID  unique identifier of the asset collection.
     * @return details of the identified asset collection
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws NoProfileForUserException  the user does not have a profile so can not have any asset collections.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public AssetCollection     attachAssetCollection(String    userId,
                                                     String    assetCollectionGUID) throws InvalidParameterException,
                                                                                           NoProfileForUserException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        return null;
    }


    /**
     * Delete an asset collection (the assets are not affected).
     *
     * @param userId               userId of user making request.
     * @param assetCollectionGUID  unique identifier of the asset collection.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws NoProfileForUserException  the user does not have a profile so can not have any asset collections.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   removeAssetCollection(String    userId,
                                        String    assetCollectionGUID) throws InvalidParameterException,
                                                                              NoProfileForUserException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {

    }


    /**
     * Return a list of assets that the specified user has added to their favorites list.
     *
     * @param userId     userId of user making request.
     * @param assetCollectionGUID  unique identifier of the asset collection to use.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of asset details
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws NoProfileForUserException  the user does not have a profile so can not have any asset collections.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<AssetCollectionMember> getMyAssets(String    userId,
                                                   String    assetCollectionGUID,
                                                   int       startFrom,
                                                   int       pageSize) throws InvalidParameterException,
                                                                              NoProfileForUserException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        return null;
    }


    /**
     * Add an asset to the identified user's list of favorite assets.
     *
     * @param userId     userId of user making request.
     * @param assetCollectionGUID  unique identifier of the asset collection to use.
     * @param assetGUID  unique identifier of the asset.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws NoProfileForUserException  the user does not have a profile so can not have any asset collections.
     * @throws NotAnAssetException the guid is not recognized as an identifier for an asset.
     * @throws PropertyServerException there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void  addToMyAssets(String   userId,
                               String   assetCollectionGUID,
                               String   assetGUID) throws InvalidParameterException,
                                                          NoProfileForUserException,
                                                          NotAnAssetException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException
    {

    }


    /**
     * Remove an asset from identified user's list of favorite assets.
     *
     * @param userId     userId of user making request.
     * @param assetCollectionGUID  unique identifier of the asset collection to use.
     * @param assetGUID  unique identifier of the asset.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws NoProfileForUserException  the user does not have a profile so can not have any asset collections.
     * @throws PropertyServerException there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void  removeFromMyAssets(String   userId,
                                    String   assetCollectionGUID,
                                    String   assetGUID) throws InvalidParameterException,
                                                               NoProfileForUserException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {

    }


    /**
     * Returns the unique identifier for the asset connected to the connection.
     *
     * @param userId the userId of the requesting user.
     * @param connectionGUID  uniqueId for the connection.
     *
     * @return unique identifier of asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving the connected asset properties from the property server.
     * @throws UnrecognizedConnectionGUIDException the supplied GUID is not recognized by the property server.
     * @throws NoConnectedAssetException there is no asset associated with this connection.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String  getAssetForConnection(String   userId,
                                         String   connectionGUID) throws InvalidParameterException,
                                                                         UnrecognizedConnectionGUIDException,
                                                                         NoConnectedAssetException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String   methodName = "getAssetForConnection";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/by-connection/{2}";

        validateOMASServerURL(methodName);

        GUIDResponse restResult = callGUIDGetRESTCall(methodName,
                                                      omasServerURL + urlTemplate,
                                                      serverName,
                                                      userId,
                                                      connectionGUID);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowUnrecognizedConnectionGUIDException(methodName, restResult);
        this.detectAndThrowNoConnectedAssetException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getGUID();
    }


    /**
     * Return a list of assets that the specified user has added to their favorites list.
     *
     * @param userId     userId of user making request.
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of asset details
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<Asset> getMyAssets(String    userId,
                                   int       startFrom,
                                   int       pageSize) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        final String  methodName  = "getMyAssets";
        final String  urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/my-assets?startFrom{2}&pageSize={3}";

        validateOMASServerURL(methodName);
        validateUserId(userId, methodName);

        AssetListResponse   restResult = callAssetListGetRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  serverName,
                                                                  userId,
                                                                  startFrom,
                                                                  pageSize);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getAssets();
    }


    /**
     * Add an asset to the identified user's list of favorite assets.
     *
     * @param userId     userId of user making request.
     * @param assetGUID  unique identifier of the asset.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void  addToMyAssets(String   userId,
                               String   assetGUID) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException
    {
        final String  methodName = "addToMyAssets";
        final String  guidParameter = "assetGUID";
        final String  urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/my-assets/{2}";

        validateOMASServerURL(methodName);
        validateUserId(userId, methodName);
        validateGUID(assetGUID, guidParameter, methodName);

        VoidResponse   restResult = callVoidPostRESTCall(methodName,
                                                         omasServerURL + urlTemplate,
                                                         nullRequestBody,
                                                         serverName,
                                                         userId,
                                                         assetGUID);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Remove an asset from identified user's list of favorite assets.
     *
     * @param userId     userId of user making request.
     * @param assetGUID  unique identifier of the asset.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void  removeFromMyAssets(String   userId,
                                    String   assetGUID) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        final String  methodName = "removeFromMyAssets";
        final String  guidParameter = "assetGUID";
        final String  urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/my-assets/{2}/delete";

        validateOMASServerURL(methodName);
        validateUserId(userId, methodName);
        validateGUID(assetGUID, guidParameter, methodName);

        VoidResponse   restResult = callVoidPostRESTCall(methodName,
                                                         omasServerURL + urlTemplate,
                                                         nullRequestBody,
                                                         serverName,
                                                         userId,
                                                         assetGUID);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Returns a comprehensive collection of properties about the requested asset.
     *
     * @param userId         userId of user making request.
     * @param assetGUID      unique id for asset.
     *
     * @return a comprehensive collection of properties about the asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException There is a problem retrieving the asset properties from the property servers).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public AssetUniverse getAssetProperties(String   userId,
                                            String   assetGUID) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        final String   methodName = "getAssetProperties";
        final String   guidParameter = "assetGUID";

        validateOMASServerURL(methodName);
        validateUserId(userId, methodName);
        validateGUID(assetGUID, guidParameter, methodName);

        try
        {
            /*
             * Make use of the ConnectedAsset OMAS Service which provides the metadata services for the
             * Open Connector Framework (OCF).
             */
            return new ConnectedAsset(serverName, omasServerURL, userId, assetGUID);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException error)
        {
            throw new UserNotAuthorizedException(error.getReportedHTTPCode(),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 error.getErrorMessage(),
                                                 error.getReportedSystemAction(),
                                                 error.getReportedUserAction(),
                                                 userId);
        }
        catch (Throwable error)
        {
            AssetConsumerErrorCode errorCode = AssetConsumerErrorCode.NO_CONNECTED_ASSET;
            String                 errorMessage = errorCode.getErrorMessageId()
                                                + errorCode.getFormattedErrorMessage(methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }


    /**
     * Creates an Audit log record for the asset.  This log record is stored in the Asset's Audit Log.
     *
     * @param userId               userId of user making request.
     * @param assetGUID            unique id for the asset.
     * @param connectorInstanceId  (optional) id of connector in use (if any).
     * @param connectionName       (optional) name of the connection (extracted from the connector).
     * @param connectorType        (optional) type of connector in use (if any).
     * @param contextId            (optional) function name, or processId of the activity that the caller is performing.
     * @param message              log record content.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException There is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void  addLogMessageToAsset(String      userId,
                                      String      assetGUID,
                                      String      connectorInstanceId,
                                      String      connectionName,
                                      String      connectorType,
                                      String      contextId,
                                      String      message) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        final String   methodName = "addLogMessageToAsset";
        final String   guidParameter = "assetGUID";

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/{2}/log-records";

        validateOMASServerURL(methodName);
        validateUserId(userId, methodName);
        validateGUID(assetGUID, guidParameter, methodName);

        LogRecordRequestBody  requestBody = new LogRecordRequestBody();
        requestBody.setConnectorInstanceId(connectorInstanceId);
        requestBody.setConnectionName(connectionName);
        requestBody.setConnectorType(connectorType);
        requestBody.setContextId(contextId);
        requestBody.setMessage(message);

        VoidResponse restResult = callVoidPostRESTCall(methodName,
                                                       omasServerURL + urlTemplate,
                                                       requestBody,
                                                       serverName,
                                                       userId,
                                                       assetGUID);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowPropertyServerException(methodName, restResult);
    }



    /**
     * Adds a new public tag to the asset's properties.
     *
     * @param userId          userId of user making request.
     * @param assetGUID       unique id for the asset.
     * @param tagName         name of the tag.
     * @param tagDescription  (optional) description of the tag.  Setting a description, particularly in a public tag
     *                        makes the tag more valuable to other users and can act as an embryonic glossary term.
     *
     * @return GUID for new tag.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String addTagToAsset(String userId,
                                String assetGUID,
                                String tagName,
                                String tagDescription) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        final String   methodName  = "addTagToAsset";
        final String   guidParameter = "assetGUID";
        final String   nameParameter = "tagName";

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/{2}/tags";

        validateOMASServerURL(methodName);
        validateUserId(userId, methodName);
        validateGUID(assetGUID, guidParameter, methodName);
        validateName(tagName, nameParameter, methodName);

        TagRequestBody  requestBody = new TagRequestBody();
        requestBody.setTagName(tagName);
        requestBody.setTagDescription(tagDescription);

        GUIDResponse restResult = callGUIDPostRESTCall(methodName,
                                                       omasServerURL + urlTemplate,
                                                       requestBody,
                                                       serverName,
                                                       userId,
                                                       assetGUID);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getGUID();
    }


    /**
     * Adds a new private tag to the asset's properties.
     *
     * @param userId              userId of user making request.
     * @param assetGUID           unique id for the asset.
     * @param tagName             name of the tag.
     * @param tagDescription      (optional) description of the tag.
     *
     * @return GUID for new tag.
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String addPrivateTagToAsset(String userId,
                                       String assetGUID,
                                       String tagName,
                                       String tagDescription) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String   methodName  = "addPrivateTagToAsset";
        final String   guidParameter = "assetGUID";
        final String   nameParameter = "tagName";

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/{2}/tags/private";

        validateOMASServerURL(methodName);
        validateUserId(userId, methodName);
        validateGUID(assetGUID, guidParameter, methodName);
        validateName(tagName, nameParameter, methodName);

        TagRequestBody  requestBody = new TagRequestBody();
        requestBody.setTagName(tagName);
        requestBody.setTagDescription(tagDescription);

        GUIDResponse restResult = callGUIDPostRESTCall(methodName,
                                                       omasServerURL + urlTemplate,
                                                       requestBody,
                                                       serverName,
                                                       userId,
                                                       assetGUID);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getGUID();
    }


    /**
     * Adds a rating to the asset.
     *
     * @param userId      userId of user making request.
     * @param assetGUID   unique id for the asset.
     * @param starRating  StarRating enumeration for none, one to five stars.
     * @param review      user review of asset.
     *
     * @return guid of new rating object.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String addRatingToAsset(String     userId,
                                   String     assetGUID,
                                   StarRating starRating,
                                   String     review) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        final String   methodName  = "addRatingToAsset";
        final String   guidParameter = "assetGUID";

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/{2}/ratings";

        validateOMASServerURL(methodName);
        validateUserId(userId, methodName);
        validateGUID(assetGUID, guidParameter, methodName);

        RatingRequestBody  requestBody = new RatingRequestBody();
        requestBody.setStarRating(starRating);
        requestBody.setReview(review);

        GUIDResponse restResult = callGUIDPostRESTCall(methodName,
                                                       omasServerURL + urlTemplate,
                                                       requestBody,
                                                       serverName,
                                                       userId,
                                                       assetGUID);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getGUID();
    }


    /**
     * Adds a "Like" to the asset.
     *
     * @param userId      userId of user making request.
     * @param assetGUID   unique id for the asset
     *
     * @return guid of new like object.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String addLikeToAsset(String       userId,
                                 String       assetGUID) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        final String   methodName  = "addRatingToAsset";
        final String   guidParameter = "assetGUID";

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/{2}/likes";

        validateOMASServerURL(methodName);
        validateUserId(userId, methodName);
        validateGUID(assetGUID, guidParameter, methodName);

        GUIDResponse restResult = callGUIDPostRESTCall(methodName,
                                                       omasServerURL + urlTemplate,
                                                       nullRequestBody,
                                                       serverName,
                                                       userId,
                                                       assetGUID);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getGUID();
    }


    /**
     * Adds a comment to the asset.
     *
     * @param userId        userId of user making request.
     * @param assetGUID     unique id for the asset.
     * @param commentType   type of comment enum.
     * @param commentText   the text of the comment.
     *
     * @return guid of new comment.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String addCommentToAsset(String      userId,
                                    String      assetGUID,
                                    CommentType commentType,
                                    String      commentText) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        final String   methodName  = "addCommentToAsset";
        final String   guidParameter = "assetGUID";

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/{2}/comments";

        validateOMASServerURL(methodName);
        validateUserId(userId, methodName);
        validateGUID(assetGUID, guidParameter, methodName);

        CommentRequestBody  requestBody = new CommentRequestBody();
        requestBody.setCommentType(commentType);
        requestBody.setCommentText(commentText);

        GUIDResponse restResult = callGUIDPostRESTCall(methodName,
                                                       omasServerURL + urlTemplate,
                                                       requestBody,
                                                       serverName,
                                                       userId,
                                                       assetGUID);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getGUID();
    }


    /**
     * Adds a comment to the asset.
     *
     * @param userId        userId of user making request.
     * @param commentGUID   unique id for an existing comment.  Used to add a reply to a comment.
     * @param commentType   type of comment enum.
     * @param commentText   the text of the comment.
     *
     * @return guid of new comment.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String addCommentReply(String      userId,
                                  String      commentGUID,
                                  CommentType commentType,
                                  String      commentText) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        final String   methodName  = "addCommentReply";
        final String   commentGUIDParameter = "commentGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/comments/{2}/replies";

        validateOMASServerURL(methodName);
        validateUserId(userId, methodName);
        validateGUID(commentGUID, commentGUIDParameter, methodName);

        CommentRequestBody  requestBody = new CommentRequestBody();
        requestBody.setCommentType(commentType);
        requestBody.setCommentText(commentText);

        GUIDResponse restResult = callGUIDPostRESTCall(methodName,
                                                       omasServerURL + urlTemplate,
                                                       requestBody,
                                                       serverName,
                                                       userId,
                                                       commentGUID);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getGUID();
    }


    /**
     * Removes a tag from the asset that was added by this user.
     *
     * @param userId    userId of user making request.
     * @param tagGUID   unique id for the tag.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   removeTag(String     userId,
                            String     tagGUID) throws InvalidParameterException,
                                                       PropertyServerException,
                                                       UserNotAuthorizedException
    {
        final String   methodName = "removeTag";
        final String   guidParameter = "tagGUID";

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/tags/{2}/delete";

        validateOMASServerURL(methodName);
        validateUserId(userId, methodName);
        validateGUID(tagGUID, guidParameter, methodName);

        VoidResponse restResult = callVoidPostRESTCall(methodName,
                                                       omasServerURL + urlTemplate,
                                                       nullRequestBody,
                                                       serverName,
                                                       userId,
                                                       tagGUID);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Removes a private tag from the asset that was added by this user.
     *
     * @param userId    userId of user making request.
     * @param tagGUID   unique id for the tag.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   removePrivateTag(String     userId,
                                   String     tagGUID) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        final String   methodName = "removePrivateTag";
        final String   guidParameter = "tagGUID";

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/tags/private/{2}/delete";

        validateOMASServerURL(methodName);
        validateUserId(userId, methodName);
        validateGUID(tagGUID, guidParameter, methodName);

        VoidResponse restResult = callVoidPostRESTCall(methodName,
                                                       omasServerURL + urlTemplate,
                                                       nullRequestBody,
                                                       serverName,
                                                       userId,
                                                       tagGUID);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Removes of a star rating that was added to the asset by this user.
     *
     * @param userId      userId of user making request.
     * @param ratingGUID  unique id for the rating object.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   removeRating(String     userId,
                               String     ratingGUID) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        final String   methodName = "removeRating";
        final String   guidParameter = "ratingGUID";

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/ratings/{2}/delete";

        validateOMASServerURL(methodName);
        validateUserId(userId, methodName);
        validateGUID(ratingGUID, guidParameter, methodName);

        VoidResponse restResult = callVoidPostRESTCall(methodName,
                                                       omasServerURL + urlTemplate,
                                                       nullRequestBody,
                                                       serverName,
                                                       userId,
                                                       ratingGUID);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Removes a "Like" added to the asset by this user.
     *
     * @param userId   userId of user making request.
     * @param likeGUID unique id for the like object.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   removeLike(String     userId,
                             String     likeGUID) throws InvalidParameterException,
                                                         PropertyServerException,
                                                         UserNotAuthorizedException
    {
        final String   methodName = "removeLike";
        final String   guidParameter = "likeGUID";

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/likes/{2}/delete";

        validateOMASServerURL(methodName);
        validateUserId(userId, methodName);
        validateGUID(likeGUID, guidParameter, methodName);

        VoidResponse restResult = callVoidPostRESTCall(methodName,
                                                       omasServerURL + urlTemplate,
                                                       nullRequestBody,
                                                       serverName,
                                                       userId,
                                                       likeGUID);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Removes a comment added to the asset by this user.
     *
     * @param userId       userId of user making request.
     * @param commentGUID  unique id for the comment object.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public void   removeComment(String     userId,
                                String     commentGUID) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        final  String  methodName = "removeComment";
        final  String  guidParameter = "commentGUID";

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/comments/{2}/delete";

        validateOMASServerURL(methodName);
        validateUserId(userId, methodName);
        validateGUID(commentGUID, guidParameter, methodName);

        VoidResponse restResult = callVoidPostRESTCall(methodName,
                                                       omasServerURL + urlTemplate,
                                                       nullRequestBody,
                                                       serverName,
                                                       userId,
                                                       commentGUID);

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Throw an exception if a server URL has not been supplied on the constructor.
     *
     * @param methodName  name of the method making the call.
     *
     * @throws PropertyServerException the server URL is not set
     */
    private void validateOMASServerURL(String methodName) throws PropertyServerException
    {
        if (omasServerURL == null)
        {
            /*
             * It is not possible to retrieve a connection without knowledge of where the OMAS Server is located.
             */
            AssetConsumerErrorCode errorCode    = AssetConsumerErrorCode.SERVER_URL_NOT_SPECIFIED;
            String                 errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }


    /**
     * Throw an exception if the supplied userId is null
     *
     * @param userId      user name to validate
     * @param methodName  name of the method making the call.
     *
     * @throws InvalidParameterException the userId is null
     */
    private void validateUserId(String userId,
                                String methodName) throws InvalidParameterException
    {
        if (userId == null)
        {
            AssetConsumerErrorCode errorCode    = AssetConsumerErrorCode.NULL_USER_ID;
            String                 errorMessage = errorCode.getErrorMessageId()
                                                + errorCode.getFormattedErrorMessage(methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                "userId");
        }
    }


    /**
     * Throw an exception if the supplied userId is null
     *
     * @param guid           unique identifier to validate
     * @param guidParameter  name of the parameter that passed the guid.
     * @param methodName     name of the method making the call.
     *
     * @throws InvalidParameterException the guid is null
     */
    private void validateGUID(String guid,
                              String guidParameter,
                              String methodName) throws InvalidParameterException
    {
        if (guid == null)
        {
            AssetConsumerErrorCode errorCode    = AssetConsumerErrorCode.NULL_GUID;
            String                 errorMessage = errorCode.getErrorMessageId()
                                                + errorCode.getFormattedErrorMessage(guidParameter,
                                                                                     methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                guidParameter);
        }
    }


    /**
     * Throw an exception if the supplied userId is null
     *
     * @param name           unique name to validate
     * @param nameParameter  name of the parameter that passed the name.
     * @param methodName     name of the method making the call.
     *
     * @throws InvalidParameterException the guid is null
     */
    private void validateName(String name,
                              String nameParameter,
                              String methodName) throws InvalidParameterException
    {
        if (name == null)
        {
            AssetConsumerErrorCode errorCode    = AssetConsumerErrorCode.NULL_NAME;
            String                 errorMessage = errorCode.getErrorMessageId()
                                                + errorCode.getFormattedErrorMessage(nameParameter,
                                                                                     methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                nameParameter);
        }
    }


    /**
     * Issue a GET REST call that returns a Connection object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return ConnectionResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private ConnectionResponse callConnectionGetRESTCall(String    methodName,
                                                         String    urlTemplate,
                                                         Object... params) throws PropertyServerException
    {
        return (ConnectionResponse)this.callGetRESTCall(methodName, GUIDResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns a MyProfile object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return MyProfileResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private MyProfileResponse callMyProfileGetRESTCall(String    methodName,
                                                       String    urlTemplate,
                                                       Object... params) throws PropertyServerException
    {
        return (MyProfileResponse)this.callGetRESTCall(methodName, MyProfileResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns a Connection object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return GUIDResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private GUIDResponse callGUIDGetRESTCall(String    methodName,
                                             String    urlTemplate,
                                             Object... params) throws PropertyServerException
    {
        return (GUIDResponse)this.callGetRESTCall(methodName, GUIDResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns a AssetListResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return AssetListResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private AssetListResponse callAssetListGetRESTCall(String    methodName,
                                                       String    urlTemplate,
                                                       Object... params) throws PropertyServerException
    {
        return (AssetListResponse)this.callGetRESTCall(methodName, AssetListResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns a response object.
     *
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private Object callGetRESTCall(String    methodName,
                                   Class     returnClass,
                                   String    urlTemplate,
                                   Object... params) throws PropertyServerException
    {
        try
        {
            RestTemplate restTemplate = new RestTemplate();

            return restTemplate.getForObject(urlTemplate, returnClass, params);
        }
        catch (Throwable error)
        {
            AssetConsumerErrorCode errorCode = AssetConsumerErrorCode.CLIENT_SIDE_REST_API_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                                                                                                     omasServerURL,
                                                                                                     error.getMessage());

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction(),
                                              error);
        }
    }


    /**
     * Issue a POST REST call that returns a guid object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return GUIDResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private GUIDResponse callGUIDPostRESTCall(String    methodName,
                                              String    urlTemplate,
                                              Object    requestBody,
                                              Object... params) throws PropertyServerException
    {
        return (GUIDResponse)this.callPostRESTCall(methodName,
                                                   GUIDResponse.class,
                                                   urlTemplate,
                                                   requestBody,
                                                   params);
    }


    /**
     * Issue a POST REST call that returns a VoidResponse object.  This is typically a create
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return VoidResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private VoidResponse callVoidPostRESTCall(String    methodName,
                                              String    urlTemplate,
                                              Object    requestBody,
                                              Object... params) throws PropertyServerException
    {
        return (VoidResponse)this.callPostRESTCall(methodName,
                                                   VoidResponse.class,
                                                   urlTemplate,
                                                   requestBody,
                                                   params);
    }


    /**
     * Issue a POST REST call that returns a VoidResponse object.  This is typically a create
     *
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return Object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private Object callPostRESTCall(String    methodName,
                                    Class     returnClass,
                                    String    urlTemplate,
                                    Object    requestBody,
                                    Object... params) throws PropertyServerException
    {
        try
        {
            RestTemplate  restTemplate = new RestTemplate();

            return restTemplate.postForObject(urlTemplate, requestBody, returnClass, params);
        }
        catch (Throwable error)
        {
            AssetConsumerErrorCode errorCode = AssetConsumerErrorCode.CLIENT_SIDE_REST_API_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                                                                                                     omasServerURL,
                                                                                                     error.getMessage());

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction(),
                                              error);
        }
    }


    /**
     * Throw an AmbiguousConnectionNameException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called
     * @param restResult  response from the rest call.  This generated in the remote server.
     * @throws AmbiguousConnectionNameException encoded exception from the server
     */
    private void detectAndThrowAmbiguousConnectionNameException(String                       methodName,
                                                                AssetConsumerOMASAPIResponse restResult) throws AmbiguousConnectionNameException
    {
        final String   exceptionClassName = AmbiguousConnectionNameException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            String connectionName = null;

            Map<String, Object>   exceptionProperties = restResult. getExceptionProperties();

            if (exceptionProperties != null)
            {
                Object  nameObject = exceptionProperties.get("connectionName");

                if (nameObject != null)
                {
                    connectionName = (String)nameObject;
                }
            }

            throw new AmbiguousConnectionNameException(restResult.getRelatedHTTPCode(),
                                                       this.getClass().getName(),
                                                       methodName,
                                                       restResult.getExceptionErrorMessage(),
                                                       restResult.getExceptionSystemAction(),
                                                       restResult.getExceptionUserAction(),
                                                       connectionName);
        }
    }


    /**
     * Throw an InvalidParameterException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws InvalidParameterException encoded exception from the server
     */
    private void detectAndThrowInvalidParameterException(String                       methodName,
                                                         AssetConsumerOMASAPIResponse restResult) throws InvalidParameterException
    {
        final String   exceptionClassName = InvalidParameterException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            String paramName = null;

            Map<String, Object>   exceptionProperties = restResult. getExceptionProperties();

            if (exceptionProperties != null)
            {
                Object  nameObject = exceptionProperties.get("parameterName");

                if (nameObject != null)
                {
                    paramName = (String)nameObject;
                }
            }
            throw new InvalidParameterException(restResult.getRelatedHTTPCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                restResult.getExceptionErrorMessage(),
                                                restResult.getExceptionSystemAction(),
                                                restResult.getExceptionUserAction(),
                                                paramName);
        }
    }


    /**
     * Throw an PropertyServerException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws PropertyServerException encoded exception from the server
     */
    private void detectAndThrowPropertyServerException(String                       methodName,
                                                       AssetConsumerOMASAPIResponse restResult) throws PropertyServerException
    {
        final String   exceptionClassName = PropertyServerException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new PropertyServerException(restResult.getRelatedHTTPCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              restResult.getExceptionErrorMessage(),
                                              restResult.getExceptionSystemAction(),
                                              restResult.getExceptionUserAction());
        }
    }


    /**
     * Throw an UnrecognizedConnectionGUIDException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws UnrecognizedConnectionGUIDException encoded exception from the server
     */
    private void detectAndThrowUnrecognizedConnectionGUIDException(String                       methodName,
                                                                   AssetConsumerOMASAPIResponse restResult) throws UnrecognizedConnectionGUIDException
    {
        final String   exceptionClassName = UnrecognizedConnectionGUIDException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            String connectionGUID = null;

            Map<String, Object>   exceptionProperties = restResult. getExceptionProperties();

            if (exceptionProperties != null)
            {
                Object  guidObject = exceptionProperties.get("connectionGUID");

                if (guidObject != null)
                {
                    connectionGUID = (String)guidObject;
                }
            }
            throw new UnrecognizedConnectionGUIDException(restResult.getRelatedHTTPCode(),
                                                          this.getClass().getName(),
                                                          methodName,
                                                          restResult.getExceptionErrorMessage(),
                                                          restResult.getExceptionSystemAction(),
                                                          restResult.getExceptionUserAction(),
                                                          connectionGUID);
        }
    }


    /**
     * Throw an NoConnectedAssetException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws NoConnectedAssetException encoded exception from the server
     */
    private void detectAndThrowNoConnectedAssetException(String                       methodName,
                                                         AssetConsumerOMASAPIResponse restResult) throws NoConnectedAssetException
    {
        final String   exceptionClassName = UnrecognizedConnectionGUIDException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            String connectionGUID = null;

            Map<String, Object>   exceptionProperties = restResult. getExceptionProperties();

            if (exceptionProperties != null)
            {
                Object  guidObject = exceptionProperties.get("connectionGUID");

                if (guidObject != null)
                {
                    connectionGUID = (String)guidObject;
                }
            }
            throw new NoConnectedAssetException(restResult.getRelatedHTTPCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                restResult.getExceptionErrorMessage(),
                                                restResult.getExceptionSystemAction(),
                                                restResult.getExceptionUserAction(),
                                                connectionGUID);
        }
    }


    /**
     * Throw an UnrecognizedConnectionNameException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws UnrecognizedConnectionNameException encoded exception from the server
     */
    private void detectAndThrowUnrecognizedConnectionNameException(String                       methodName,
                                                                   AssetConsumerOMASAPIResponse restResult) throws UnrecognizedConnectionNameException
    {
        final String   exceptionClassName = UnrecognizedConnectionNameException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            String connectionName = null;

            Map<String, Object>   exceptionProperties = restResult. getExceptionProperties();

            if (exceptionProperties != null)
            {
                Object  nameObject = exceptionProperties.get("connectionName");

                if (nameObject != null)
                {
                    connectionName = (String)nameObject;
                }
            }

            throw new UnrecognizedConnectionNameException(restResult.getRelatedHTTPCode(),
                                                          this.getClass().getName(),
                                                          methodName,
                                                          restResult.getExceptionErrorMessage(),
                                                          restResult.getExceptionSystemAction(),
                                                          restResult.getExceptionUserAction(),
                                                          connectionName);
        }
    }


    /**
     * Throw an NoProfileForUserException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called.
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws NoProfileForUserException encoded exception from the server
     */
    private void detectAndThrowNoProfileForUserException(String                       methodName,
                                                         AssetConsumerOMASAPIResponse restResult) throws NoProfileForUserException
    {
        final String   exceptionClassName = UserNotAuthorizedException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            String userId = null;

            Map<String, Object>   exceptionProperties = restResult. getExceptionProperties();

            if (exceptionProperties != null)
            {
                Object  userIdObject = exceptionProperties.get("userId");

                if (userIdObject != null)
                {
                    userId = (String)userIdObject;
                }
            }

            throw new NoProfileForUserException(restResult.getRelatedHTTPCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                restResult.getExceptionErrorMessage(),
                                                restResult.getExceptionSystemAction(),
                                                restResult.getExceptionUserAction(),
                                                userId);
        }
    }


    /**
     * Throw an UserNotAuthorizedException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called.
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws UserNotAuthorizedException encoded exception from the server
     */
    private void detectAndThrowUserNotAuthorizedException(String                       methodName,
                                                          AssetConsumerOMASAPIResponse restResult) throws UserNotAuthorizedException
    {
        final String   exceptionClassName = UserNotAuthorizedException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            String userId = null;

            Map<String, Object>   exceptionProperties = restResult. getExceptionProperties();

            if (exceptionProperties != null)
            {
                Object  userIdObject = exceptionProperties.get("userId");

                if (userIdObject != null)
                {
                    userId = (String)userIdObject;
                }
            }

            throw new UserNotAuthorizedException(restResult.getRelatedHTTPCode(),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 restResult.getExceptionErrorMessage(),
                                                 restResult.getExceptionSystemAction(),
                                                 restResult.getExceptionUserAction(),
                                                 userId);
        }
    }
}
