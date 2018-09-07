/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram.server;

import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.GovernanceProgramErrorCode;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.UnrecognizedGUIDException;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GovernanceProgramErrorHandler provides common validation routines for the other handler classes.
 */
public class GovernanceProgramErrorHandler
{
    private static final Logger log = LoggerFactory.getLogger(GovernanceProgramErrorHandler.class);

    private OMRSRepositoryConnector repositoryConnector;


    /**
     * Typical constructor providing access to the repository connector for this access service.
     *
     * @param repositoryConnector - connector object
     */
    GovernanceProgramErrorHandler(OMRSRepositoryConnector   repositoryConnector)
    {
        this.repositoryConnector = repositoryConnector;
    }


    /**
     * Throw an exception if the supplied userId is null
     *
     * @param userId  user name to validate
     * @param methodName  name of the method making the call.
     * @throws UserNotAuthorizedException the userId is null
     */
    void validateUserId(String userId,
                        String methodName) throws UserNotAuthorizedException
    {
        if (userId == null)
        {
            GovernanceProgramErrorCode errorCode = GovernanceProgramErrorCode.NULL_USER_ID;
            String                     errorMessage = errorCode.getErrorMessageId()
                                                    + errorCode.getFormattedErrorMessage(methodName);

            throw new UserNotAuthorizedException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                null);
        }

        log.debug("Non null userId: " + userId);
    }


    /**
     * Throw an exception if the supplied unique identifier is null
     *
     * @param guid  unique identifier to validate
     * @param parameterName  name of the parameter that passed the guid.
     * @param expectedTypeName  type of the entity for the guid.
     * @param methodName  name of the method making the call.
     * @throws UnrecognizedGUIDException the guid is null
     */
    void validateGUID(String guid,
                      String parameterName,
                      String expectedTypeName,
                      String methodName) throws UnrecognizedGUIDException
    {
        if (guid == null)
        {
            GovernanceProgramErrorCode errorCode    = GovernanceProgramErrorCode.NULL_GUID;
            String                     errorMessage = errorCode.getErrorMessageId()
                                                    + errorCode.getFormattedErrorMessage(parameterName, methodName);

            throw new UnrecognizedGUIDException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                expectedTypeName,
                                                null);
        }

        log.debug("Non null guid: " + guid);
    }


    /**
     * Throw an exception if the supplied enum is null
     *
     * @param enumValue  enum value to validate
     * @param parameterName  name of the parameter that passed the enum.
     * @param methodName  name of the method making the call.
     * @throws InvalidParameterException the enum is null
     */
    void validateEnum(Object enumValue,
                      String parameterName,
                      String methodName) throws InvalidParameterException
    {
        if (enumValue == null)
        {
            GovernanceProgramErrorCode errorCode    = GovernanceProgramErrorCode.NULL_ENUM;
            String                     errorMessage = errorCode.getErrorMessageId()
                                                    + errorCode.getFormattedErrorMessage(parameterName, methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                parameterName);
        }

        log.debug("Non null enum: " + enumValue.toString());
    }


    /**
     * Throw an exception if the supplied name is null
     *
     * @param name  unique name to validate
     * @param parameterName  name of the parameter that passed the name.
     * @param methodName  name of the method making the call.
     * @throws InvalidParameterException the guid is null
     */
    void validateName(String name,
                      String parameterName,
                      String methodName) throws InvalidParameterException
    {
        if (name == null)
        {
            GovernanceProgramErrorCode errorCode    = GovernanceProgramErrorCode.NULL_NAME;
            String                     errorMessage = errorCode.getErrorMessageId()
                                                    + errorCode.getFormattedErrorMessage(parameterName, methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                parameterName);
        }

        log.debug("Non null " + parameterName + ": " + name);
    }


    /**
     * Check that there is a repository connector.
     *
     * @param methodName  name of the method being called
     * @return metadata collection that provides access to the properties in the property server
     * @throws PropertyServerException exception thrown if the repository connector
     */
    OMRSMetadataCollection validateRepositoryConnector(String   methodName) throws PropertyServerException
    {
        if (this.repositoryConnector == null)
        {
            GovernanceProgramErrorCode errorCode = GovernanceProgramErrorCode.OMRS_NOT_INITIALIZED;
            String                     errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());

        }

        if (! this.repositoryConnector.isActive())
        {
            GovernanceProgramErrorCode errorCode = GovernanceProgramErrorCode.OMRS_NOT_AVAILABLE;
            String                     errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }

        log.debug("Active repository connector: " + repositoryConnector.getMetadataCollectionId());

        try
        {
            return repositoryConnector.getMetadataCollection();
        }
        catch (Throwable error)
        {
            GovernanceProgramErrorCode errorCode = GovernanceProgramErrorCode.NO_METADATA_COLLECTION;
            String                     errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
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
        GovernanceProgramErrorCode errorCode = GovernanceProgramErrorCode.INSTANCE_NOT_FOUND_BY_GUID;
        String                     errorMessage = errorCode.getErrorMessageId()
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


    /**
     * Throw an exception if the supplied guid returned an entity of the wrong type
     *
     * @param guid  unique identifier of entity
     * @param methodName  name of the method making the call.
     * @param actualType  type of retrieved entity
     * @param expectedType  type the entity should be
     * @throws UnrecognizedGUIDException the guid is for the wrong type of object
     */
    void handleWrongTypeForGUIDException(String guid,
                                         String methodName,
                                         String actualType,
                                         String expectedType) throws UnrecognizedGUIDException
    {
        GovernanceProgramErrorCode errorCode = GovernanceProgramErrorCode.INSTANCE_WRONG_TYPE_FOR_GUID;
        String                     errorMessage = errorCode.getErrorMessageId()
                                                + errorCode.getFormattedErrorMessage(methodName,
                                                                                     guid,
                                                                                     actualType,
                                                                                     expectedType);

        throw new UnrecognizedGUIDException(errorCode.getHTTPErrorCode(),
                                            this.getClass().getName(),
                                            methodName,
                                            errorMessage,
                                            errorCode.getSystemAction(),
                                            errorCode.getUserAction(),
                                            expectedType,
                                            guid);

    }


    /**
     * Throw an exception if the supplied userId is not authorized to perform a request
     *
     * @param userId  user name to validate
     * @param methodName  name of the method making the call.
     * @param serverName  name of this server
     * @param serviceName  name of this access service
     * @throws UserNotAuthorizedException the userId is unauthorised for the request
     */
    void handleUnauthorizedUser(String userId,
                                String methodName,
                                String serverName,
                                String serviceName) throws UserNotAuthorizedException
    {
        GovernanceProgramErrorCode errorCode = GovernanceProgramErrorCode.USER_NOT_AUTHORIZED;
        String                     errorMessage = errorCode.getErrorMessageId()
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
     * @throws PropertyServerException unexpected exception from property server
     */
    void handleRepositoryError(Throwable  error,
                               String     methodName,
                               String     serverName,
                               String     serviceName) throws PropertyServerException
    {
        GovernanceProgramErrorCode errorCode = GovernanceProgramErrorCode.PROPERTY_SERVER_ERROR;
        String                     errorMessage = errorCode.getErrorMessageId()
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
}
