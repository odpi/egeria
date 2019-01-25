/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.server;

import org.odpi.openmetadata.accessservices.connectedasset.ffdc.ConnectedAssetErrorCode;
import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.UnrecognizedGUIDException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

/**
 * ErrorHandler provides common validation routines for the other handler classes
 */
class ErrorHandler
{
    private OMRSRepositoryConnector repositoryConnector;


    /**
     * Typical constructor providing access to the repository connector for this access service.
     *
     * @param repositoryConnector - connector object
     */
    ErrorHandler(OMRSRepositoryConnector   repositoryConnector)
    {
        this.repositoryConnector = repositoryConnector;
    }


    /**
     * Throw an exception if the supplied userId is null
     *
     * @param userId  user name to validate
     * @param methodName  name of the method making the call.
     *
     * @throws InvalidParameterException the userId is null
     */
    void validateUserId(String userId,
                        String methodName) throws InvalidParameterException
    {
        if (userId == null)
        {
            ConnectedAssetErrorCode errorCode = ConnectedAssetErrorCode.NULL_USER_ID;
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
     * Throw an exception if the supplied unique identifier is null
     *
     * @param guid  unique identifier to validate
     * @param parameterName  name of the parameter that passed the guid.
     * @param methodName  name of the method making the call.
     *
     * @throws InvalidParameterException the guid is null
     */
    void validateGUID(String guid,
                      String parameterName,
                      String methodName) throws InvalidParameterException
    {
        if (guid == null)
        {
            ConnectedAssetErrorCode errorCode    = ConnectedAssetErrorCode.NULL_GUID;
            String                 errorMessage = errorCode.getErrorMessageId()
                                                + errorCode.getFormattedErrorMessage(parameterName, methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                parameterName);
        }
    }


    /**
     * Throw an exception if the supplied enum is null
     *
     * @param enumValue  enum value to validate
     * @param parameterName  name of the parameter that passed the enum.
     * @param methodName  name of the method making the call.
     *
     * @throws InvalidParameterException the enum is null
     */
    void validateEnum(Object enumValue,
                      String parameterName,
                      String methodName) throws InvalidParameterException
    {
        if (enumValue == null)
        {
            ConnectedAssetErrorCode errorCode    = ConnectedAssetErrorCode.NULL_ENUM;
            String                 errorMessage = errorCode.getErrorMessageId()
                                                + errorCode.getFormattedErrorMessage(parameterName, methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                parameterName);
        }
    }


    /**
     * Throw an exception if the supplied name is null
     *
     * @param name  unique name to validate
     * @param parameterName  name of the parameter that passed the name.
     * @param methodName  name of the method making the call.
     *
     * @throws InvalidParameterException the name is null
     */
    void validateName(String name,
                      String parameterName,
                      String methodName) throws InvalidParameterException
    {
        if (name == null)
        {
            ConnectedAssetErrorCode errorCode    = ConnectedAssetErrorCode.NULL_NAME;
            String                 errorMessage = errorCode.getErrorMessageId()
                                                + errorCode.getFormattedErrorMessage(parameterName, methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                parameterName);
        }
    }


    /**
     * Throw an exception if the supplied paging values don't make sense.
     *
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param methodName  name of the method making the call.
     *
     * @throws InvalidParameterException the guid is null
     */
    void validatePaging(int    startFrom,
                        int    pageSize,
                        String methodName) throws InvalidParameterException
    {
        final  String   startFromParameterName = "startFrom";
        final  String   pageSizeParameterName = "pageSize";

        if (startFrom < 0)
        {
            ConnectedAssetErrorCode errorCode    = ConnectedAssetErrorCode.NEGATIVE_START_FROM;
            String                 errorMessage = errorCode.getErrorMessageId()
                                                + errorCode.getFormattedErrorMessage(startFromParameterName, methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                startFromParameterName);
        }


        if (pageSize < 1)
        {
            ConnectedAssetErrorCode errorCode    = ConnectedAssetErrorCode.EMPTY_PAGE_SIZE;
            String                 errorMessage = errorCode.getErrorMessageId()
                                                + errorCode.getFormattedErrorMessage(pageSizeParameterName, methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                pageSizeParameterName);
        }
    }


    /**
     * Check that there is a repository connector.
     *
     * @param methodName  name of the method being called
     * @return metadata collection that provides access to the properties in the property server
     *
     * @throws PropertyServerException exception thrown if the repository connector
     */
    OMRSMetadataCollection validateRepositoryConnector(String   methodName) throws PropertyServerException
    {
        if (this.repositoryConnector == null)
        {
            ConnectedAssetErrorCode errorCode = ConnectedAssetErrorCode.OMRS_NOT_INITIALIZED;
            String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());

        }

        if (! this.repositoryConnector.isActive())
        {
            ConnectedAssetErrorCode errorCode = ConnectedAssetErrorCode.OMRS_NOT_AVAILABLE;
            String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }

        try
        {
            return repositoryConnector.getMetadataCollection();
        }
        catch (Throwable error)
        {
            ConnectedAssetErrorCode errorCode = ConnectedAssetErrorCode.NO_METADATA_COLLECTION;
            String                 errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }


    /**
     * Throw an exception if the supplied userId is not authorized to perform a request
     *
     * @param userId  user name to validate
     * @param methodName  name of the method making the call.
     * @param serverName  name of this server
     * @param serviceName  name of this access service
     *
     * @throws UserNotAuthorizedException the userId is unauthorised for the request
     */
    void handleUnauthorizedUser(String userId,
                                String methodName,
                                String serverName,
                                String serviceName) throws UserNotAuthorizedException
    {
        ConnectedAssetErrorCode errorCode = ConnectedAssetErrorCode.USER_NOT_AUTHORIZED;
        String                 errorMessage = errorCode.getErrorMessageId()
                                            + errorCode.getFormattedErrorMessage(userId,
                                                                                 methodName,
                                                                                 serviceName,
                                                                                 serverName);

        throw new UserNotAuthorizedException(errorCode.getHTTPErrorCode(),
                                             this.getClass().getName(),
                                             methodName,
                                             errorMessage,
                                             errorCode.getSystemAction(),
                                             errorCode.getUserAction(),
                                             userId);

    }


    /**
     * Throw an exception if the supplied userId is not authorized to perform a request
     *
     * @param error  caught exception
     * @param methodName  name of the method making the call.
     * @param serverName  name of this server
     * @param serviceName  name of this access service
     *
     * @throws PropertyServerException unexpected exception from property server
     */
    void handleRepositoryError(Throwable  error,
                               String     methodName,
                               String     serverName,
                               String     serviceName) throws PropertyServerException
    {
        ConnectedAssetErrorCode errorCode = ConnectedAssetErrorCode.PROPERTY_SERVER_ERROR;
        String                 errorMessage = errorCode.getErrorMessageId()
                                            + errorCode.getFormattedErrorMessage(error.getMessage(),
                                                                                 methodName,
                                                                                 serviceName,
                                                                                 serverName);

        throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          errorCode.getSystemAction(),
                                          errorCode.getUserAction());

    }


    /**
     * Throw an exception if there is a problem with the asset guid
     *
     * @param error  caught exception
     * @param assetGUID  unique identifier for the requested asset
     * @param methodName  name of the method making the call
     * @param serverName name of this server
     * @param serviceName  name of this access service
     *
     * @throws InvalidParameterException unexpected exception from property server
     */
    void handleUnknownAsset(Throwable  error,
                            String     assetGUID,
                            String     methodName,
                            String     serverName,
                            String     serviceName) throws InvalidParameterException
    {
        ConnectedAssetErrorCode errorCode = ConnectedAssetErrorCode.UNKNOWN_ASSET;
        String                 errorMessage = errorCode.getErrorMessageId()
                                            + errorCode.getFormattedErrorMessage(assetGUID,
                                                                                 methodName,
                                                                                 serviceName,
                                                                                 serverName,
                                                                                 error.getMessage());

        throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                            this.getClass().getName(),
                                            methodName,
                                            errorMessage,
                                            errorCode.getSystemAction(),
                                            errorCode.getUserAction(),
                                            "assetGUID");

    }


    /**
     * Throw an exception if the supplied guid is not recognized
     *
     * @param userId  user name to validate
     * @param methodName  name of the method making the call.
     * @param serverName  name of this server
     * @param expectedType  name of object to return
     * @throws UnrecognizedGUIDException the guid is not recognized
     */
    void handleUnrecognizedGUIDException(String userId,
                                         String methodName,
                                         String serverName,
                                         String expectedType,
                                         String guid) throws UnrecognizedGUIDException
    {
        ConnectedAssetErrorCode errorCode = ConnectedAssetErrorCode.INSTANCE_NOT_FOUND_BY_GUID;
        String                 errorMessage = errorCode.getErrorMessageId()
                                            + errorCode.getFormattedErrorMessage(methodName,
                                                                                 expectedType,
                                                                                 guid,
                                                                                 userId,
                                                                                 serverName);

        throw new UnrecognizedGUIDException(errorCode.getHTTPErrorCode(),
                                            this.getClass().getName(),
                                            methodName,
                                            errorMessage,
                                            errorCode.getSystemAction(),
                                            errorCode.getUserAction(),
                                            expectedType,
                                            guid);

    }
}
