/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.handlers;

import org.odpi.openmetadata.accessservices.assetconsumer.AssetConsumerAssetInterface;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.AssetConsumerErrorCode;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions.*;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;

/**
 * AssetHandler provides the server-side logic for the AssetConsumerAssetInterface.
 */
public class AssetHandler implements AssetConsumerAssetInterface
{
    private static final String connectionToAssetRelationshipGUID       = "e777d660-8dbe-453e-8b83-903771f054c0";

    private String                  serviceName;
    private OMRSRepositoryConnector repositoryConnector;
    private OMRSRepositoryHelper    repositoryHelper = null;
    private String                  serverName       = null;
    private ErrorHandler            errorHandler     = null;


    /**
     * Construct the connection handler with a link to the property server's connector and this access service's
     * official name.
     *
     * @param serviceName  name of this service
     * @param repositoryConnector  connector to the property server.
     */
    public AssetHandler(String                  serviceName,
                        OMRSRepositoryConnector repositoryConnector)
    {
        this.serviceName = serviceName;
        this.repositoryConnector = repositoryConnector;
        if (repositoryConnector != null)
        {
            this.repositoryHelper = repositoryConnector.getRepositoryHelper();
            this.serverName = repositoryConnector.getServerName();
            errorHandler = new ErrorHandler(repositoryConnector);
        }
    }


    /**
     * Returns the unique identifier for the asset connected to the requested connection.
     *
     * @param userId the userId of the requesting user.
     * @param connectionGUID  unique identifier for the connection.
     *
     * @return unique identifier of asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server.
     * @throws NoConnectedAssetException there is no asset associated with this connection.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String  getAssetForConnection(String   userId,
                                         String   connectionGUID) throws InvalidParameterException,
                                                                         NoConnectedAssetException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final  String   methodName = "getAssetForConnection";
        final  String   guidParameter = "connectionGUID";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateGUID(connectionGUID, guidParameter, methodName);

        OMRSMetadataCollection metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        try
        {
            List<Relationship> relationships = metadataCollection.getRelationshipsForEntity(userId,
                                                                                            connectionGUID,
                                                                                            null,
                                                                                            0,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            0);
            Relationship      assetRelationship = null;

            for (Relationship  retrievedRelationship : relationships)
            {
                if (retrievedRelationship != null)
                {
                    InstanceType type = retrievedRelationship.getType();

                    if (type != null)
                    {
                        if (connectionToAssetRelationshipGUID.equals(type.getTypeDefGUID()))
                        {
                            if (assetRelationship == null)
                            {
                                assetRelationship = retrievedRelationship;
                            }
                            else
                            {
                                AssetConsumerErrorCode errorCode    = AssetConsumerErrorCode.MULTIPLE_ASSETS_FOUND;
                                String                 errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(connectionGUID);

                                throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                                                  this.getClass().getName(),
                                                                  methodName,
                                                                  errorMessage,
                                                                  errorCode.getSystemAction(),
                                                                  errorCode.getUserAction());
                            }
                        }
                    }
                }
            }

            if (assetRelationship == null)
            {
                AssetConsumerErrorCode errorCode = AssetConsumerErrorCode.ASSET_NOT_FOUND;
                String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(connectionGUID);

                throw new NoConnectedAssetException(errorCode.getHTTPErrorCode(),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    errorMessage,
                                                    errorCode.getSystemAction(),
                                                    errorCode.getUserAction(),
                                                    connectionGUID);
            }

            EntityProxy end2 = assetRelationship.getEntityTwoProxy();

            if (end2 != null)
            {
                return end2.getGUID();
            }
            else
            {
                AssetConsumerErrorCode errorCode = AssetConsumerErrorCode.NULL_END2_RETURNED;
                String                 errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(connectionToAssetRelationshipGUID,
                                                             assetRelationship.getGUID(),
                                                             assetRelationship.toString());

                throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                                  this.getClass().getName(),
                                                  methodName,
                                                  errorMessage,
                                                  errorCode.getSystemAction(),
                                                  errorCode.getUserAction());
            }
        }
        catch (NoConnectedAssetException | PropertyServerException error)
        {
            throw error;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException error)
        {
            AssetConsumerErrorCode errorCode = AssetConsumerErrorCode.CONNECTION_NOT_FOUND;
            String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(connectionGUID,
                                                                                                            serverName,
                                                                                                            error.getErrorMessage());

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                connectionGUID);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId,
                                                methodName,
                                                serverName,
                                                serviceName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error,
                                               methodName,
                                               serverName,
                                               serviceName);
        }

        return null;
    }


    /**
     * Returns the asset corresponding to the supplied connection name.
     *
     * @param userId           userId of user making request.
     * @param connectionName   this may be the qualifiedName or displayName of the connection.
     *
     * @return unique identifier of asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws AmbiguousConnectionNameException there is more than one connection defined for this name.
     * @throws NoConnectedAssetException there is no asset associated with this connection.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String  getAssetForConnectionName(String userId,
                                             String connectionName) throws InvalidParameterException,
                                                                           AmbiguousConnectionNameException,
                                                                           NoConnectedAssetException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        final  String   methodName = "getAssetForConnectionName";

        ConnectionHandler connectionHandler = new ConnectionHandler(serviceName, repositoryConnector);

        Connection  connection = connectionHandler.getConnectionByName(userId, connectionName);

        if (connection != null)
        {
            return this.getAssetForConnection(userId, connection.getGUID());
        }

        AssetConsumerErrorCode errorCode = AssetConsumerErrorCode.CONNECTION_NOT_FOUND;
        String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(connectionName,
                                                                                                        serverName,
                                                                                                        null);

        throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                            this.getClass().getName(),
                                            methodName,
                                            errorMessage,
                                            errorCode.getSystemAction(),
                                            errorCode.getUserAction(),
                                            connectionName);
    }


    /**
     * Returns a comprehensive collection of properties about the requested asset.
     *
     * @param userId         userId of user making request.
     * @param assetGUID      unique identifier for asset.
     *
     * @return a comprehensive collection of properties about the asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving the asset properties from the property servers).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public AssetUniverse getAssetProperties(String userId,
                                            String assetGUID) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        /*
         * This method is not used server side.  The client delegates it to Connected aAsset OMAS.
         */
        return null;
    }

}
