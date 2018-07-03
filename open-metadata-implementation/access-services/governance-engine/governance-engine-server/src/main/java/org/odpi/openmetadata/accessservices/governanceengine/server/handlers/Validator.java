/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.server.handlers;

import org.apache.log4j.Logger;
import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.errorcode.GovernanceEngineErrorCode;
import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.InvalidParameterException;

import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * ErrorHandler provides common validation routines for the other handler classes
 */
public class Validator {
    private OMRSRepositoryConnector repositoryConnector;

    private static final Logger log = Logger.getLogger(Validator.class);



    /**
     * Typical constructor providing access to the repository connector for this access service.
     *
     * @param repositoryConnector - connector object
     */
    public Validator(OMRSRepositoryConnector repositoryConnector) {
        this.repositoryConnector = repositoryConnector;
    }


    /**
     * Throw an exception if the supplied userId is null
     *
     * @param userId     - user name to validate
     * @param methodName - name of the method making the call.
     * @throws InvalidParameterException - the userId is null
     */
    public void validateUserId(String userId,
                               String methodName) throws InvalidParameterException {
        if (userId == null) {
            GovernanceEngineErrorCode errorCode = GovernanceEngineErrorCode.EMPTY_USER_ID;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }


    /**
     * Throw an exception if the supplied unique identifier is null
     *
     * @param guid          - unique identifier to validate
     * @param parameterName - name of the parameter that passed the guid.
     * @param methodName    - name of the method making the call.
     * @throws InvalidParameterException - the guid is null
     */
    public void validateGUID(String guid,
                             String parameterName,
                             String methodName) throws InvalidParameterException {
        if (guid == null) {
            GovernanceEngineErrorCode errorCode = GovernanceEngineErrorCode.NULL_GUID;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(parameterName, methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }


    /**
     * Throw an exception if the supplied unique identifier is null
     *
     * @param rootClassification          - unique identifier to validate
     * @param parameterName - name of the parameter that passed the guid.
     * @param methodName    - name of the method making the call.
     * @throws InvalidParameterException - the guid is null
     */
    public void validateRootClassification(List<String> rootClassification,
                                           String parameterName,
                                           String methodName) throws InvalidParameterException {

        //NULL is valid so no further checks for now
        return;
    }

    /**
     * Throw an exception if the supplied unique identifier is null
     *
     * @param rootAssetType          - unique identifier to validate
     * @param parameterName - name of the parameter that passed the guid.
     * @param methodName    - name of the method making the call.
     * @throws InvalidParameterException - the guid is null
     */
    public void validateRootAssetType(List<String> rootAssetType,
                                           String parameterName,
                                           String methodName) throws InvalidParameterException {
        // NULL is valid, so no further checks for now
        return;
    }


    /**
     * Throw an exception if the supplied text field is null
     *
     * @param text          - unique name to validate
     * @param parameterName - name of the parameter that passed the name.
     * @param methodName    - name of the method making the call.
     * @throws InvalidParameterException - the guid is null
     */
    public void validateText(String text,
                             String parameterName,
                             String methodName) throws InvalidParameterException {
        if (text == null) {
            GovernanceEngineErrorCode errorCode = GovernanceEngineErrorCode.NULL_TEXT;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(parameterName, methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }


    /**
     * Check that there is a repository connector.
     *
     * @param methodName - name of the method being called
     * @return metadata collection that provides access to the properties in the property handlers
     * @throws PropertyServerException - exception thrown if the repository connector
     */
    public OMRSMetadataCollection validateRepositoryConnector(String methodName) throws PropertyServerException {
        if (this.repositoryConnector == null) {
            GovernanceEngineErrorCode errorCode = GovernanceEngineErrorCode.OMRS_NOT_INITIALIZED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());

        }

        if (!this.repositoryConnector.isActive()) {
            GovernanceEngineErrorCode errorCode = GovernanceEngineErrorCode.OMRS_NOT_AVAILABLE;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        try {
            return repositoryConnector.getMetadataCollection();
        } catch (Throwable error) {
            GovernanceEngineErrorCode errorCode = GovernanceEngineErrorCode.NO_METADATA_COLLECTION;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

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
     * @param userId      - user name to validate
     * @param methodName  - name of the method making the call.
     * @param serverName  - name of this handlers
     * @param serviceName - name of this access service
     * @throws UserNotAuthorizedException - the userId is unauthorised for the request
     */
    public void handleUnauthorizedUser(String userId,
                                       String methodName,
                                       String serverName,
                                       String serviceName) throws UserNotAuthorizedException {
        GovernanceEngineErrorCode errorCode = GovernanceEngineErrorCode.USER_NOT_AUTHORIZED;
        String errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(userId,
                methodName,
                serviceName,
                serverName);

        throw new UserNotAuthorizedException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());

    }


    /**
     * Throw an exception if the supplied userId is not authorized to perform a request
     *
     * @param error       - caught exception
     * @param methodName  - name of the method making the call.
     * @param serverName  - name of this handlers
     * @param serviceName - name of this access service
     * @throws PropertyServerException - unexpected exception from property handlers
     */
    public void handleRepositoryError(Throwable error,
                                      String methodName,
                                      String serverName,
                                      String serviceName) throws PropertyServerException {
        GovernanceEngineErrorCode errorCode = GovernanceEngineErrorCode.PROPERTY_SERVER_ERROR;
        String errorMessage = errorCode.getErrorMessageId()
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
