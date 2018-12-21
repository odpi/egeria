/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
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
     * @param userId user name to validate
     * @param methodName name of the method making the call.
     * @throws InvalidParameterException the userId is null
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
     * @param guid supplied guid  unique identifier to validate
     * @param parameterName name of the parameter that passed the guid.
     * @param methodName name of the method making the call.
     * @throws InvalidParameterException the guid is null
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
     * @param enumValue enum value to validate
     * @param parameterName name of the parameter that passed the enum.
     * @param methodName name of the method making the call.
     * @throws InvalidParameterException the enum is null
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
     * @param name unique name to validate
     * @param parameterName name of the parameter that passed the name.
     * @param methodName name of the method making the call.
     * @throws InvalidParameterException the guid is null
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
     * @param text unique name to validate
     * @param parameterName name of the parameter that passed the name.
     * @param methodName name of the method making the call.
     * @throws InvalidParameterException the guid is null
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
     * @param methodName name of the method being called
     * @param repositoryConnector  connector to the repository
     * @return metadata collection that provides access to the properties in the property server
     * @throws MetadataServerUncontactableException exception thrown if the metadata server cannot be reached
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
     * @param userId user name to validate
     * @param methodName name of the method making the call.
     * @param serverName name of this server name of this server
     * @param serviceName name of this access service
     * @throws UserNotAuthorizedException the userId is unauthorised for the request
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
     * @param error caught exception
     * @param methodName name of the method making the call.
     * @param serverName name of this server name of this server
     * @param serviceName name of this access service
     * @throws MetadataServerUncontactableException   the metadata server cannot be reached
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
     * @param error caught exception
     * @param assetGUID unique identifier for the requested asset
     * @param methodName name of the method making the call
     * @param serverName name of this server name of this server
     * @param serviceName name of this access service
     * @throws UnrecognizedGUIDException unexpected exception from property server
     */
    public static  void handleUnknownAsset(Throwable  error,
                                    String     assetGUID,
                                    String     methodName,
                                    String     serverName,
                                    String     serviceName) throws UnrecognizedGUIDException {
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
     * Throw an Invalid Parameter exception
     * @param e exception to handle
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName name of this access service
     * @throws InvalidParameterException a parameter is not valid or missing.
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
     * Convert the supplied OMRS exception to a Subject Area invalid type name exception and throw it.
     * @param typeName the name of the unknown type
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName access service name 
     * @throws InvalidParameterException a parameter is not valid or missing.
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
     * Convert the supplied OMRS exception to a Subject Area property error exception and throw it.
     * @param e exception to handle
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName name of this access service
     * @throws InvalidParameterException a parameter is not valid or missing.
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
     * Convert the supplied OMRS exception to a Subject Area classification error exception and throw it.
     * @param e exception to handle
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName name of this access service
     * @throws ClassificationException error occured during a classification
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
     * Convert the supplied OMRS exception to a Subject Area status not known exception and throw it.
     * @param e exception to handle
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName name of this access service
     * @throws StatusNotSupportedException staus not supported
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
     * Convert the supplied OMRS exception to a Subject Area Entity Not known exception and throw it.
     * @param guid supplied guid 
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName name of this access service
     * @throws UnrecognizedGUIDException unrecognized GUID
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
     * Convert the supplied OMRS exception to a Subject Area Entity Proxy Only exception and throw it.
     * @param e exception to handle 
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName name of this access service
     * @throws MetadataServerUncontactableException   the metadata server cannot be reached
     */
    public static void handleEntityProxyOnlyException(org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException e, String methodName, String serverName, String serviceName) throws MetadataServerUncontactableException {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.METADATA_SERVER_UNCONTACTABLE_ERROR;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(e.getMessage(),
                e.getErrorMessage(),
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
     * Convert the supplied OMRS exception to a Subject Area type error exception and throw it.
     * @param e exception to handle
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName name of this access service
     * @throws InvalidParameterException a parameter is not valid or missing.
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
     * Convert the supplied OMRS exception to a Subject Area Function Not Supported exception and throw it.
     * @param e exception to handle
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName name of this access service
     * @throws FunctionNotSupportedException Function not supported
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
     * Convert the supplied OMRS exception to a Subject Area paging error exception and throw it.
     * @param e exception to handle
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName name of this access service
     * @throws InvalidParameterException a parameter is not valid or missing.
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
     * Convert the supplied OMRS exception to a Subject Area Entity Not Deleted exception and throw it.
     * @param e exception to handle
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName name of this access service
     * @param guid supplied guid 
     * @throws GUIDNotPurgedException Entity not deleted
     */
    public static void handleEntityNotDeletedException(org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotDeletedException e, String methodName, String serverName, String serviceName,String guid) throws GUIDNotPurgedException {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GUID_NOT_PURGED_ERROR;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName,guid);

        throw new GUIDNotPurgedException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction(),
                guid);
    }
    /**
     * Convert the supplied OMRS exception to a Subject Area relationship not known exception and throw it.
     * @param guid supplied guid
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName name of this access service
     * @throws UnrecognizedGUIDException GUID not recognized
     */
    public static void handleRelationshipNotKnownException(String guid, String methodName, String serverName, String serviceName)  throws UnrecognizedGUIDException {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GUID_DOES_NOT_EXIST;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(guid,methodName);

        UnrecognizedGUIDException uge =  new UnrecognizedGUIDException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction(),guid);
        throw uge;
    }

    /**
     * Convert the supplied OMRS exception to a Subject Area Entity not known exception and throw it.
     * @param guid supplied guid 
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName name of this access service
     * @throws UnrecognizedGUIDException GUID not recognized
     */
    public static void handleEntityNotKnownException(String guid, String methodName, String serverName, String serviceName)  throws UnrecognizedGUIDException {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GUID_DOES_NOT_EXIST;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(guid,methodName);

        UnrecognizedGUIDException uge =  new UnrecognizedGUIDException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction(),guid);
        throw uge;
    }

    /**
     * Convert the supplied OMRS exception to a Subject Area entity not deleted exception and throw it.
     * @param guid supplied guid
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName name of this access service
     * @throws GUIDNotDeletedException Relationship not purged
     */
    public static void handleEntityNotDeletedException( String guid,
                                                             String methodName,
                                                             String serverName,
                                                             String serviceName
                                                            ) throws  GUIDNotDeletedException {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GUID_NOT_DELETED_ERROR;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName);

        throw new GUIDNotDeletedException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction(),
                guid);
    }

    /**
     * Convert the supplied OMRS exception to a Subject Area relationship not deleted exception and throw it.
     * @param guid supplied guid
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName name of this access service
     * @throws GUIDNotDeletedException Relationship not purged
     */
    public static void handleRelationshipNotDeletedException( String guid,
                                                              String methodName,
                                                              String serverName,
                                                              String serviceName
    ) throws  GUIDNotDeletedException {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GUID_NOT_DELETED_ERROR;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName);

        throw new GUIDNotDeletedException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction(),
                guid);
    }

    /**
     * Convert the supplied OMRS exception to a Subject Area metadata server not reachable exception and throw it.
     * @param e exception to handle
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName name of this access service
     * @throws MetadataServerUncontactableException the metadata server cannot be reached
     */
    public void handleMetadataServerUnContactable(MetadataServerUncontactableException e, String methodName, String serverName, String serviceName) throws MetadataServerUncontactableException
    {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.METADATA_SERVER_UNCONTACTABLE_ERROR;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName);

        throw new MetadataServerUncontactableException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }

    public void handleEntityNotPurgedException(String obsoleteGuid, String restAPIName, String serverName, String serviceName) throws GUIDNotPurgedException
    {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GUID_NOT_PURGED_ERROR;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(restAPIName);

        throw new GUIDNotPurgedException(errorCode.getHTTPErrorCode(),
                className,
                restAPIName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction(),
                obsoleteGuid);
    }

    public void handleRelationshipNotPurgedException(String  obsoleteGuid, String restAPIName, String serverName, String serviceName) throws GUIDNotPurgedException
    {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GUID_NOT_PURGED_ERROR;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(restAPIName);

        throw new GUIDNotPurgedException(errorCode.getHTTPErrorCode(),
                className,
                restAPIName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction(),
                obsoleteGuid);
    }
}
