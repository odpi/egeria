/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.server.handlers;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;


/**
 * ErrorHandler provides common validation routines for the other handler classes
 */
public class ErrorHandler
{
     static String className = ErrorHandler.class.getName(); 


    /**
     * Throw an exception if the supplied userId is null
     *
     * @param userId - user name to validate
     * @param methodName - name of the method making the call.
     * @throws InvalidParameterException - the userId is null
     */
    public static  void validateUserId(String userId,
                                String methodName) throws InvalidParameterException
    {
        if (userId == null)
        {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.NULL_USER_ID;
            String                 errorMessage = errorCode.getErrorMessageId()
                                                + errorCode.getFormattedErrorMessage(methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                className,
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction());
        }
    }


    /**
     * Throw an exception if the supplied unique identifier is null
     *
     * @param guid - unique identifier to validate
     * @param parameterName - name of the parameter that passed the guid.
     * @param methodName - name of the method making the call.
     * @throws InvalidParameterException - the guid is null
     */
    public static  void validateGUID(String guid,
                              String parameterName,
                              String methodName) throws InvalidParameterException
    {
        if (guid == null)
        {
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.NULL_GUID;
            String                 errorMessage = errorCode.getErrorMessageId()
                                                + errorCode.getFormattedErrorMessage(parameterName, methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                className,
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction());
        }
    }


    /**
     * Throw an exception if the supplied enum is null
     *
     * @param enumValue - enum value to validate
     * @param parameterName - name of the parameter that passed the enum.
     * @param methodName - name of the method making the call.
     * @throws InvalidParameterException - the enum is null
     */
    public static  void validateEnum(Object enumValue,
                              String parameterName,
                              String methodName) throws InvalidParameterException
    {
        if (enumValue == null)
        {
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.NULL_ENUM;
            String                 errorMessage = errorCode.getErrorMessageId()
                                                + errorCode.getFormattedErrorMessage(parameterName, methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                className,
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction());
        }
    }


    /**
     * Throw an exception if the supplied name is null
     *
     * @param name - unique name to validate
     * @param parameterName - name of the parameter that passed the name.
     * @param methodName - name of the method making the call.
     * @throws InvalidParameterException - the guid is null
     */
    public static  void validateName(String name,
                              String parameterName,
                              String methodName) throws InvalidParameterException
    {
        if (name == null)
        {
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.NULL_NAME;
            String                 errorMessage = errorCode.getErrorMessageId()
                                                + errorCode.getFormattedErrorMessage(parameterName, methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                className,
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction());
        }
    }


    /**
     * Throw an exception if the supplied text field is null
     *
     * @param text - unique name to validate
     * @param parameterName - name of the parameter that passed the name.
     * @param methodName - name of the method making the call.
     * @throws InvalidParameterException - the guid is null
     */
    public static  void validateText(String text,
                              String parameterName,
                              String methodName) throws InvalidParameterException
    {
        if (text == null)
        {
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.NULL_TEXT;
            String                 errorMessage = errorCode.getErrorMessageId()
                                                + errorCode.getFormattedErrorMessage(parameterName, methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                className,
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
     * @param repositoryConnector - connector used to reference repository.
     * @return metadata collection that provides access to the properties in the property server
     * @throws MetadataServerUncontactableException - exception thrown if the repository connector
     */
    public static OMRSMetadataCollection validateRepositoryConnector(String   methodName,OMRSRepositoryConnector repositoryConnector ) throws MetadataServerUncontactableException
    {
        if (repositoryConnector == null)
        {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.OMRS_NOT_INITIALIZED;
            String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new MetadataServerUncontactableException(errorCode.getHTTPErrorCode(),
                                              className,
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());

        }

        if (! repositoryConnector.isActive())
        {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.OMRS_NOT_AVAILABLE;
            String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new MetadataServerUncontactableException(errorCode.getHTTPErrorCode(),
                                              className,
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
            // consider a unique error code.

            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.OMRS_NOT_AVAILABLE;
            String                 errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new MetadataServerUncontactableException(errorCode.getHTTPErrorCode(),
                                              className,
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }


    /**
     * Throw an exception if the supplied userId is not authorized to perform a request
     *
     * @param userId - user name to validate
     * @param methodName - name of the method making the call.
     * @param serverName - name of this server
     * @param serviceName - name of this access service
     * @throws UserNotAuthorizedException - the userId is unauthorised for the request
     */
    public static  void handleUnauthorizedUser(String userId,
                                        String methodName,
                                        String serverName,
                                        String serviceName) throws UserNotAuthorizedException
    {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.USER_NOT_AUTHORIZED;
        String errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(userId,
                                                     methodName,
                                                     serviceName,
                                                     serverName);

        throw new UserNotAuthorizedException(errorCode.getHTTPErrorCode(),
                                             className,
                                             methodName,
                                             errorMessage,
                                             errorCode.getSystemAction(),
                                             errorCode.getUserAction(),
                                             userId);

    }


    /**
     * Throw an exception if the respository could not be contacted
     *
     * @param error - caught exception
     * @param methodName - name of the method making the call.
     * @param serverName - name of this server
     * @param serviceName - name of this access service
     * @throws MetadataServerUncontactableException - unexpected exception from property server
     */
    public static  void handleRepositoryError(Throwable  error,
                                       String     methodName,
                                       String     serverName,
                                       String     serviceName) throws MetadataServerUncontactableException
    {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.METADATA_SERVER_UNCONTACTABLE_ERROR;
        String                 errorMessage = errorCode.getErrorMessageId()
                                            + errorCode.getFormattedErrorMessage(error.getMessage(),
                                                                                 methodName,
                                                                                 serviceName,
                                                                                 serverName);

        throw new MetadataServerUncontactableException(errorCode.getHTTPErrorCode(),
                                          className,
                                          methodName,
                                          errorMessage,
                                          errorCode.getSystemAction(),
                                          errorCode.getUserAction());
    }


    /**
     * Throw an exception if the asset is not known
     *
     * @param error - caught exception
     * @param assetGUID - unique identifier for the requested asset
     * @param methodName - name of the method making the call
     * @param serverName - name of this server
     * @param serviceName - name of this access service
     * @throws UnrecognizedGUIDException - unexpected exception from property server
     * @throws InvalidParameterException - if a parameter is not valid or missing.
     */
    public static  void handleUnknownAsset(Throwable  error,
                                    String     assetGUID,
                                    String     methodName,
                                    String     serverName,
                                    String     serviceName) throws InvalidParameterException, UnrecognizedGUIDException {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GUID_DOES_NOT_EXIST;
        String                 errorMessage = errorCode.getErrorMessageId()
                                            + errorCode.getFormattedErrorMessage(assetGUID,
                                                                                 methodName,
                                                                                 serviceName,
                                                                                 serverName,
                                                                                 error.getMessage());

        throw new UnrecognizedGUIDException(errorCode.getHTTPErrorCode(),
                                            className,
                                            methodName,
                                            errorMessage,
                                            errorCode.getSystemAction(),
                                            errorCode.getUserAction(),
                                            assetGUID);

    }

    /**
     * convert OMRS exception to OMAS Exception
     * @param e - exception to handle
     * @param methodName - name of the method making the call
     * @param serverName - name of this server
     * @param serviceName - name of this access service
     * @throws InvalidParameterException if any of the parameters are invalid.
     */
    public static void handleInvalidParameterException(org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e, String methodName, String serverName, String serviceName) throws InvalidParameterException {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.INVALID_PARAMETER;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName);

        throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }

    /**
     * Invalid type name
     * convert OMRS exception to OMAS Exception
     * @param typeName - type of asset
     * @param methodName - name of the method making the call
     * @param serverName - name of this server
     * @param serviceName - name of this access service
     * @throws InvalidParameterException if any of the parameters are invalid.
     */
    public static void handleTypeDefNotKnownException(String typeName, String methodName, String serverName, String serviceName) throws InvalidParameterException {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.TYPEDEF_NOT_KNOWN;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName);
        // specifying a typedef name that is not correct is a parameter error.
        throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }

    /**
     * convert OMRS exception to OMAS Exception
     * @param e - exception to handle
     * @param methodName - name of the method making the call
     * @param serverName - name of this server
     * @param serviceName - name of this access service
     * @throws InvalidParameterException if any of the parameters are invalid.
     */
    public static void handlePropertyErrorException(org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException e, String methodName, String serverName, String serviceName) throws InvalidParameterException {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.TYPEDEF_NOT_KNOWN;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName);
        // specifying a typedef name that is not correct is a parameter error.
        throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }

    /**
     *
     * @param e - exception to handle
     * @param methodName - name of the method making the call
     * @param serverName - name of this server
     * @param serviceName - name of this access service
     * @throws ClassificationException - unable to use classification.
     */
    public static void handleClassificationErrorException(org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException e, String methodName, String serverName, String serviceName) throws ClassificationException {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.CLASSIFICATION_ERROR;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName);

        throw new ClassificationException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }

    /**
     * status is not known
     * @param e - exception to handle
     * @param methodName - name of the method making the call
     * @param serverName - name of this server
     * @param serviceName - name of this access service
     * @throws StatusNotSupportedException - the status is not supported.
     */
    public static void handleStatusNotSupportedException(org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException e, String methodName, String serverName, String serviceName) throws StatusNotSupportedException {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.STATUS_NOT_SUPPORTED_ERROR;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName);

        throw new StatusNotSupportedException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }

    /**
     * Entity not known
     * @param guid - the GUID of the entity.
     * @param methodName - name of the method making the call
     * @param serverName - name of this server
     * @param serviceName - name of this access service
     * @throws UnrecognizedGUIDException if the GUID invalid.
     */
    public static void handleEntityNotKnownError(String guid, String methodName, String serverName, String serviceName) throws UnrecognizedGUIDException {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.ENTITY_NOT_KNOWN_ERROR;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName,guid);

        throw new UnrecognizedGUIDException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction(),
                guid);
    }

    /**
     * convert OMRS exception to OMAS Exception
     * @param e - exception to handle
     * @param methodName - name of the method making the call
     * @param serverName - name of this server
     * @param serviceName - name of this access service
     * @throws MetadataServerUncontactableException - unable to contact the server.
     */
    public static void handleEntityProxyOnlyException(org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException e, String methodName, String serverName, String serviceName) throws MetadataServerUncontactableException {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.METADATA_SERVER_UNCONTACTABLE_ERROR;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(e.getMessage(),
                methodName,
                serviceName,
                serverName);

        throw new MetadataServerUncontactableException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }

    /**
     * convert OMRS exception to OMAS Exception
     * @param e - exception to handle
     * @param methodName - name of the method making the call
     * @param serverName - name of this server
     * @param serviceName - name of this access service
     * @throws InvalidParameterException if any of the parameters are invalid.
     */
    public static void handleTypeErrorException(org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException e , String methodName, String serverName, String serviceName) throws InvalidParameterException {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.TYPEDEF_ERROR;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName);

        throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }

    /**
     * convert OMRS exception to OMAS Exception
     * @param e - exception to handle
     * @param methodName - name of the method making the call
     * @param serverName - name of this server
     * @param serviceName - name of this access service
     * @throws FunctionNotSupportedException - the function is not supported.
     */
    public static void handleFunctionNotSupportedException(org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException e, String methodName, String serverName, String serviceName) throws FunctionNotSupportedException {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.FUNCTION_NOT_SUPPORTED;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName);

        throw new FunctionNotSupportedException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }

    /**
     * convert OMRS exception to OMAS Exception
     * @param e - exception to handle
     * @param methodName - name of the method making the call
     * @param serverName - name of this server
     * @param serviceName - name of this access service
     * @throws InvalidParameterException if any of the parameters are invalid.
     */
    public static void handlePagingErrorException(org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException e, String methodName, String serverName, String serviceName) throws InvalidParameterException {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.PAGING_ERROR;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName);

        throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }

    /**
     * convert OMRS exception to OMAS Exception
     * @param e - exception to handle
     * @param methodName - name of the method making the call
     * @param serverName - name of this server
     * @param serviceName - name of this access service
     * @param guid - the GUID of the entity.
     * @throws GUIDNotPurgedException - The entity could not be purged
     */
    public static void handleEntityNotDeletedException(org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotDeletedException e, String methodName, String serverName, String serviceName,String guid) throws GUIDNotPurgedException {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GUID_NOT_PURGED_ERROR;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName);

        throw new GUIDNotPurgedException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction(),
                guid);
    }

    /**
     * relationship not known
     * @param guid - the GUID of the entity.
     * @param methodName - name of the method making the call
     * @param serverName - name of this server
     * @param serviceName - name of this access service
     * @throws UnrecognizedGUIDException - The entity could not be resolved
     */
    public static void handleRelationshipNotKnownException(String guid, String methodName, String serverName, String serviceName)  throws UnrecognizedGUIDException {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GUID_DOES_NOT_EXIST;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName);

        UnrecognizedGUIDException uge =  new UnrecognizedGUIDException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction(),guid);
        throw uge;
    }

    /**
     * relationship not deleted
     * @param e - exception originating this error.
     * @param methodName - name of the method making the call
     * @param serverName - name of this server
     * @param serviceName - name of this access service
     * @param guid - the GUID of the entity.
     * @throws GUIDNotPurgedException - The entity could not be purged
     */
    public static void handleRelationshipNotDeletedException(org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotDeletedException e, String methodName, String serverName, String serviceName,String guid) throws  GUIDNotPurgedException {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GUID_NOT_PURGED_ERROR;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName);

        throw new GUIDNotPurgedException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction(),
                guid);
    }

    /**
     * Entity not deleted
     * @param guid - the GUID of the entity.
     * @param methodName - name of the method making the call
     * @param serverName - name of this server
     * @param serviceName - name of this access service
     * @throws GUIDNotPurgedException - The entity could not be purged
     */

    public static void handleEntityNotDeletedException(String guid, String methodName, String serverName, String serviceName) throws GUIDNotPurgedException {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GUID_NOT_PURGED_ERROR;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName);

        throw new GUIDNotPurgedException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction(),
                guid);

    }
}
