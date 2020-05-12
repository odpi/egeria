/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.handlers;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.responses.OMASExceptionToResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;


/**
 * ErrorHandler provides common validation routines for the other handler classes
 */
public class ErrorHandler
{
     static String className = ErrorHandler.class.getName();

    /**
     * return an exception response if the supplied userId is not authorized to perform a request
     *
     * @param userId user name to validate
     * @param methodName name of the method making the call.
     * @param serverName name of this server name of this server
     * @param serviceName name of this access service
     * @return UserNotAuthorizedException response the userId is unauthorised for the request
     */
    public static  SubjectAreaOMASAPIResponse handleUnauthorizedUser(String userId,
                                        String methodName,
                                        String serverName,
                                        String serviceName)
    {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.USER_NOT_AUTHORIZED;
        String errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(userId,
                                                     methodName,
                                                     serviceName,
                                                     serverName);

        UserNotAuthorizedException oe =new UserNotAuthorizedException(errorCode.getHTTPErrorCode(),
                                             className,
                                             methodName,
                                             errorMessage,
                                             errorCode.getSystemAction(),
                                             errorCode.getUserAction(),
                                             userId);
        return OMASExceptionToResponse.convertUserNotAuthorizedException(oe);

    }


    /**
     * return an exception response if the respository could not be contacted
     *
     * @param error caught exception
     * @param methodName name of the method making the call.
     * @param serverName name of this server name of this server
     * @param serviceName name of this access service
     * @return MetadataServerUncontactableException   the metadata server cannot be reached
     */
    public static SubjectAreaOMASAPIResponse handleRepositoryError(Throwable  error,
                                       String     methodName,
                                       String     serverName,
                                       String     serviceName)
    {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.METADATA_SERVER_UNCONTACTABLE_ERROR;
        String                 errorMessage = errorCode.getErrorMessageId()
                                            + errorCode.getFormattedErrorMessage(error.getMessage(),
                                                                                 methodName,
                                                                                 serviceName,
                                                                                 serverName);

        MetadataServerUncontactableException oe =  new MetadataServerUncontactableException(errorCode.getHTTPErrorCode(),
                                          className,
                                          methodName,
                                          errorMessage,
                                          errorCode.getSystemAction(),
                                          errorCode.getUserAction());
        return OMASExceptionToResponse.convertMetadataServerUncontactableException(oe);
    }


    /**
     * return an exception response if the asset is not known
     *
     * @param error caught exception
     * @param assetGUID unique identifier for the requested asset
     * @param methodName name of the method making the call
     * @param serverName name of this server name of this server
     * @param serviceName name of this access service
     * @return UnrecognizedGUIDException response unexpected exception from property server
     */
    public static SubjectAreaOMASAPIResponse handleUnknownAsset(Throwable  error,
                                    String     assetGUID,
                                    String     methodName,
                                    String     serverName,
                                    String     serviceName) {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GUID_DOES_NOT_EXIST;
        String                 errorMessage = errorCode.getErrorMessageId()
                                            + errorCode.getFormattedErrorMessage(assetGUID,
                                                                                 methodName,
                                                                                 serviceName,
                                                                                 serverName,
                                                                                 error.getMessage());

        UnrecognizedGUIDException oe = new UnrecognizedGUIDException(errorCode.getHTTPErrorCode(),
                                            className,
                                            methodName,
                                            errorMessage,
                                            errorCode.getSystemAction(),
                                            errorCode.getUserAction(),
                                            assetGUID);
        return OMASExceptionToResponse.convertUnrecognizedGUIDException(oe);

    }

    /**
     * Throw an Invalid Parameter exception
     * @param e exception to handle
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName name of this access service
     * @return Invalid parameter response
     */
    public static SubjectAreaOMASAPIResponse  handleInvalidParameterException(org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e, String methodName, String serverName, String serviceName)  {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.INVALID_PARAMETER;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName);

        InvalidParameterException oe = new InvalidParameterException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
        oe.setRelatedProperties(e.getRelatedProperties());
        return OMASExceptionToResponse.convertInvalidParameterException(oe);
    }

    /**
     * Convert the supplied OMRS exception to a Subject Area invalid type name exception and throw it.
     * @param typeName the name of the unknown type
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName access service name
     * @return  InvalidParameterException response a parameter is not valid or missing.
     */
    public static SubjectAreaOMASAPIResponse  handleTypeDefNotKnownException(String typeName, String methodName, String serverName, String serviceName)  {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.TYPEDEF_NOT_KNOWN;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName,typeName);
        // specifying a typedef name that is not correct is a parameter error.

        InvalidParameterException oe = new InvalidParameterException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
        return OMASExceptionToResponse.convertInvalidParameterException(oe);
    }

    /**
     * Convert the supplied OMRS exception to a Subject Area property error exception and throw it.
     * @param e exception to handle
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName name of this access service
     * @return  InvalidParameterException response a parameter is not valid or missing.
     */
    public static SubjectAreaOMASAPIResponse  handlePropertyErrorException(org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException e, String methodName, String serverName, String serviceName)  {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.INVALID_PARAMETER;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName);
        // specifying a typedef name that is not correct is a parameter error.
        InvalidParameterException oe = new InvalidParameterException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
        oe.setRelatedProperties(e.getRelatedProperties());
        return OMASExceptionToResponse.convertInvalidParameterException(oe);
    }

    /**
     * Convert the supplied OMRS exception to a Subject Area classification error exception and throw it.
     * @param e exception to handle
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName name of this access service
     * @return ClassificationException error occurred during a classification
     */
    public static SubjectAreaOMASAPIResponse  handleClassificationErrorException(org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException e, String methodName, String serverName, String serviceName)  {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.CLASSIFICATION_ERROR;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName);

        ClassificationException oe = new ClassificationException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
        return OMASExceptionToResponse.convertClassificationException(oe);

    }

    /**
     * Convert the supplied OMRS exception to a Subject Area status not known exception and throw it.
     * @param e exception to handle
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName name of this access service
     * @return StatusNotSupportedException response status not supported response
     */
    public static SubjectAreaOMASAPIResponse  handleStatusNotSupportedException(org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException e, String methodName, String serverName, String serviceName)  {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.STATUS_NOT_SUPPORTED_ERROR;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName);

        StatusNotSupportedException oe= new StatusNotSupportedException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
        return OMASExceptionToResponse.convertStatusNotsupportedException(oe);
    }

    /**
     * Convert the supplied OMRS exception to a Subject Area Entity Not known exception and throw it.
     * @param guid supplied guid
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName name of this access service
     * @return UnrecognizedGUIDException unrecognized GUID response
     */
    public static SubjectAreaOMASAPIResponse  handleEntityNotKnownError(String guid, String methodName, String serverName, String serviceName)  {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.ENTITY_NOT_KNOWN_ERROR;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName,guid);

        UnrecognizedGUIDException oe =new UnrecognizedGUIDException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction(),
                guid);
        return OMASExceptionToResponse.convertUnrecognizedGUIDException(oe);
    }

    /**
     * Convert the supplied OMRS exception to a Subject Area Entity Proxy Only exception and throw it.
     * @param e exception to handle
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName name of this access service
     * @return MetadataServerUncontactableException   the metadata server cannot be reached response
     */
    public static SubjectAreaOMASAPIResponse  handleEntityProxyOnlyException(org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException e, String methodName, String serverName, String serviceName)  {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.METADATA_SERVER_UNCONTACTABLE_ERROR;
        String errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(e.getMessage(),
                e.getReportedErrorMessage(),
                methodName,
                serviceName,
                serverName);

        MetadataServerUncontactableException oe = new MetadataServerUncontactableException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
        return OMASExceptionToResponse.convertMetadataServerUncontactableException(oe);

    }

    /**
     * Convert the supplied OMRS exception to a Subject Area type error exception and throw it.
     * @param e exception to handle
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName name of this access service
     * @return InvalidParameterException a parameter is not valid or missing response
     */
    public static SubjectAreaOMASAPIResponse  handleTypeErrorException(org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException e , String methodName, String serverName, String serviceName)  {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.TYPEDEF_ERROR;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName);

        InvalidParameterException oe = new InvalidParameterException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
        return OMASExceptionToResponse.convertInvalidParameterException(oe);
    }

    /**
     * Convert the supplied OMRS exception to a Subject Area Function Not Supported exception and throw it.
     * @param e exception to handle
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName name of this access service
     * @return FunctionNotSupportedException response Function not supported
     */
    public static SubjectAreaOMASAPIResponse  handleFunctionNotSupportedException(org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException e, String methodName, String serverName, String serviceName)  {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.FUNCTION_NOT_SUPPORTED;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName);

        FunctionNotSupportedException oe = new FunctionNotSupportedException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
        return OMASExceptionToResponse.convertFunctionNotSupportedException(oe);
    }

    /**
     * Convert the supplied OMRS exception to a Subject Area paging error exception and throw it.
     * @param e exception to handle
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName name of this access service
     * @return InvalidParameterException response. a parameter is not valid or missing
     */
    public static SubjectAreaOMASAPIResponse  handlePagingErrorException(org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException e, String methodName, String serverName, String serviceName)  {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.PAGING_ERROR;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName);

        InvalidParameterException oe = new InvalidParameterException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
        return OMASExceptionToResponse.convertInvalidParameterException(oe);
    }

    /**
     * Convert the supplied OMRS exception to a Subject Area Entity Not Deleted exception and throw it.
     * @param e exception to handle
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName name of this access service
     * @param guid supplied guid
     * @return GUIDNotPurgedException response - Entity not deleted
     */
//    public static SubjectAreaOMASAPIResponse  handleEntityNotDeletedException(org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotDeletedException e, String methodName, String serverName, String serviceName,String guid)  {
//        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GUID_NOT_PURGED_ERROR;
//        String                 errorMessage = errorCode.getErrorMessageId()
//                + errorCode.getFormattedErrorMessage(methodName,guid);
//
//        GUIDNotDeletedException oe = new GUIDNotDeletedException((errorCode.getHTTPErrorCode(),
//                className,
//                methodName,
//                errorMessage,
//                errorCode.getSystemAction(),
//                errorCode.getUserAction(),
//                guid);
//        return OMASExceptionToResponse.convertGUIDNotDeletedException(oe);
//    }
    /**
     * Convert the supplied OMRS exception to a Subject Area relationship not known exception and throw it.
     * @param guid supplied guid
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName name of this access service
     * @return UnrecognizedGUIDException response - GUID not recognized
     */
    public static SubjectAreaOMASAPIResponse  handleRelationshipNotKnownException(String guid, String methodName, String serverName, String serviceName)  {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GUID_DOES_NOT_EXIST;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(guid,methodName);

        UnrecognizedGUIDException oe =  new UnrecognizedGUIDException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction(),guid);
        return OMASExceptionToResponse.convertUnrecognizedGUIDException(oe);

    }

    /**
     * Convert the supplied OMRS exception to a Subject Area Entity not known exception and throw it.
     * @param guid supplied guid
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName name of this access service
     * @return UnrecognizedGUIDException response - GUID not recognized
     */
    public static SubjectAreaOMASAPIResponse  handleEntityNotKnownException(String guid, String methodName, String serverName, String serviceName)   {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GUID_DOES_NOT_EXIST;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(guid,methodName);

        UnrecognizedGUIDException oe =  new UnrecognizedGUIDException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction(),guid);
        return OMASExceptionToResponse.convertUnrecognizedGUIDException(oe);
    }

    /**
     * Convert the supplied OMRS exception to a Subject Area entity not deleted exception and throw it.
     * @param guid supplied guid
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName name of this access service
     * @return GUIDNotDeletedException Relationship not purged
     */
    public static SubjectAreaOMASAPIResponse  handleEntityNotDeletedException( String guid,
                                                             String methodName,
                                                             String serverName,
                                                             String serviceName
                                                            )  {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GUID_NOT_DELETED_ERROR;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName);

        GUIDNotDeletedException oe=new GUIDNotDeletedException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction(),
                guid);
        return OMASExceptionToResponse.convertGUIDNotDeletedException(oe);
    }

    /**
     * Convert the supplied OMRS exception to a Subject Area relationship not deleted exception and throw it.
     * @param guid supplied guid
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName name of this access service
     * @return GUIDNotDeletedException response Relationship not purged
     */
    public static SubjectAreaOMASAPIResponse  handleRelationshipNotDeletedException( String guid,
                                                              String methodName,
                                                              String serverName,
                                                              String serviceName
    )  {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GUID_NOT_DELETED_ERROR;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName);

        GUIDNotDeletedException oe = new GUIDNotDeletedException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction(),
                guid);
        return OMASExceptionToResponse.convertGUIDNotDeletedException(oe);
    }

    /**
     * Convert the supplied OMRS exception to a Subject Area metadata server not reachable exception and throw it.
     * @param e exception to handle
     * @param methodName name of the method making the call.
     * @param serverName name of this server
     * @param serviceName name of this access service
     * @return MetadataServerUncontactableException response -the metadata server cannot be reached
     */
    public static SubjectAreaOMASAPIResponse handleMetadataServerUnContactable(MetadataServerUncontactableException e, String methodName, String serverName, String serviceName)
    {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.METADATA_SERVER_UNCONTACTABLE_ERROR;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName);

        MetadataServerUncontactableException oe=new MetadataServerUncontactableException(errorCode.getHTTPErrorCode(),
                className,
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
        return OMASExceptionToResponse.convertMetadataServerUncontactableException(oe);
    }

    public static SubjectAreaOMASAPIResponse handleEntityNotPurgedException(String obsoleteGuid, String restAPIName, String serverName, String serviceName)
    {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GUID_NOT_PURGED_ERROR;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(restAPIName);

        GUIDNotPurgedException oe = new GUIDNotPurgedException(errorCode.getHTTPErrorCode(),
                className,
                restAPIName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction(),
                obsoleteGuid);
        return OMASExceptionToResponse.convertGUIDNotPurgedException(oe);
    }

    public static SubjectAreaOMASAPIResponse handleRelationshipNotPurgedException(String  obsoleteGuid, String restAPIName, String serverName, String serviceName)
    {
        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GUID_NOT_PURGED_ERROR;
        String                 errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(restAPIName);


        GUIDNotPurgedException oe = new GUIDNotPurgedException(errorCode.getHTTPErrorCode(),
                className,
                restAPIName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction(),
                obsoleteGuid);
        return OMASExceptionToResponse.convertGUIDNotPurgedException(oe);
    }
}
