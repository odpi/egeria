/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server;


import org.odpi.openmetadata.accessservices.communityprofile.ffdc.CommunityProfileErrorCode;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

/**
 * ErrorHandler provides common validation routines for the other handler classes
 */
class ErrorHandler
{
    private final String  defaultGUIDParameterName = "guid";

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
            CommunityProfileErrorCode errorCode = CommunityProfileErrorCode.NULL_USER_ID;
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
            CommunityProfileErrorCode errorCode    = CommunityProfileErrorCode.NULL_GUID;
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
     * Throw an exception if the supplied unique identifier is null
     *
     * @param guid  unique identifier to validate
     * @param parameterName  name of the parameter that passed the guid.
     * @param expectedTypeName  type of the entity for the guid.
     * @param methodName  name of the method making the call.
     * @throws InvalidParameterException the guid is null
     */
    void validateGUID(String guid,
                      String parameterName,
                      String expectedTypeName,
                      String methodName) throws InvalidParameterException
    {
        if (guid == null)
        {
            CommunityProfileErrorCode errorCode    = CommunityProfileErrorCode.NULL_GUID;
            String                    errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(expectedTypeName, parameterName, methodName);

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
            CommunityProfileErrorCode errorCode    = CommunityProfileErrorCode.NULL_ENUM;
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
            CommunityProfileErrorCode errorCode    = CommunityProfileErrorCode.NULL_NAME;
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
     * Throw an exception if the supplied text field is null
     *
     * @param text  unique name to validate
     * @param parameterName  name of the parameter that passed the name.
     * @param methodName  name of the method making the call.
     *
     * @throws InvalidParameterException the text is null
     */
    void validateText(String text,
                      String parameterName,
                      String methodName) throws InvalidParameterException
    {
        if (text == null)
        {
            CommunityProfileErrorCode errorCode    = CommunityProfileErrorCode.NULL_TEXT;
            String                 errorMessage = errorCode.getErrorMessageId()
                                                + errorCode.getFormattedErrorMessage(parameterName, methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                "text");
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
            CommunityProfileErrorCode errorCode    = CommunityProfileErrorCode.NEGATIVE_START_FROM;
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
            CommunityProfileErrorCode errorCode    = CommunityProfileErrorCode.NEGATIVE_PAGE_SIZE;
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
     * Throw an exception if the supplied guid returned an entity of the wrong type
     *
     * @param guid  unique identifier of entity
     * @param methodName  name of the method making the call.
     * @param actualType  type of retrieved entity
     * @param expectedType  type the entity should be
     * @throws InvalidParameterException the guid is for the wrong type of object
     */
    void handleWrongTypeForGUIDException(String guid,
                                         String methodName,
                                         String actualType,
                                         String expectedType) throws InvalidParameterException
    {
        CommunityProfileErrorCode errorCode = CommunityProfileErrorCode.INSTANCE_WRONG_TYPE_FOR_GUID;
        String                    errorMessage = errorCode.getErrorMessageId()
                                               + errorCode.getFormattedErrorMessage(methodName,
                                                                                    guid,
                                                                                    actualType,
                                                                                    expectedType);

        throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                            this.getClass().getName(),
                                            methodName,
                                            errorMessage,
                                            errorCode.getSystemAction(),
                                            errorCode.getUserAction(),
                                            defaultGUIDParameterName);

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
            CommunityProfileErrorCode errorCode = CommunityProfileErrorCode.OMRS_NOT_INITIALIZED;
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
            CommunityProfileErrorCode errorCode = CommunityProfileErrorCode.OMRS_NOT_AVAILABLE;
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
            CommunityProfileErrorCode errorCode = CommunityProfileErrorCode.NO_METADATA_COLLECTION;
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
        CommunityProfileErrorCode errorCode = CommunityProfileErrorCode.USER_NOT_AUTHORIZED;
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
        CommunityProfileErrorCode errorCode = CommunityProfileErrorCode.PROPERTY_SERVER_ERROR;
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
     * Throw an exception if the supplied guid is not recognized
     *
     * @param userId  user name to validate
     * @param methodName  name of the method making the call.
     * @param serverName  name of this server
     * @param expectedType  name of object to return
     * @throws InvalidParameterException the guid is not recognized
     */
    void handleUnrecognizedGUIDException(String userId,
                                         String methodName,
                                         String serverName,
                                         String expectedType,
                                         String guid) throws InvalidParameterException
    {
        CommunityProfileErrorCode errorCode = CommunityProfileErrorCode.INSTANCE_NOT_FOUND_BY_GUID;
        String                 errorMessage = errorCode.getErrorMessageId()
                                            + errorCode.getFormattedErrorMessage(methodName,
                                                                                 expectedType,
                                                                                 guid,
                                                                                 userId,
                                                                                 serverName);

        throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                            this.getClass().getName(),
                                            methodName,
                                            errorMessage,
                                            errorCode.getSystemAction(),
                                            errorCode.getUserAction(),
                                            defaultGUIDParameterName);

    }
}
