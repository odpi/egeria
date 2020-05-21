/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.handlers;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.responses.OMASExceptionToResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;


/**
 * ErrorHandler provides common validation routines for the other handler classes
 */
public class ErrorHandler {
    static String className = ErrorHandler.class.getName();

    /**
     * return an exception response if the supplied guid is not authorized to perform a request
     *
     * @param userId      user name to validate
     * @param methodName  name of the method making the call.
     * @param serverName  name of this server name of this server
     * @param serviceName name of this access service
     * @return UserNotAuthorizedException response the userId is unauthorised for the request
     */
    public static SubjectAreaOMASAPIResponse handleUnauthorizedUser(String userId,
                                                                    String methodName,
                                                                    String serverName,
                                                                    String serviceName) {
        ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.USER_NOT_AUTHORIZED.getMessageDefinition();

        UserNotAuthorizedException oe = new UserNotAuthorizedException(
                messageDefinition,
                className,
                methodName,
                userId);
        return OMASExceptionToResponse.convertUserNotAuthorizedException(oe);

    }


    /**
     * return an exception response if the respository could not be contacted
     *
     * @param error       caught exception
     * @param methodName  name of the method making the call.
     * @param serverName  name of this server name of this server
     * @param serviceName name of this access service
     * @return MetadataServerUncontactableException   the metadata server cannot be reached
     */
    public static SubjectAreaOMASAPIResponse handleRepositoryError(Throwable error,
                                                                   String methodName,
                                                                   String serverName,
                                                                   String serviceName) {
        ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.METADATA_SERVER_UNCONTACTABLE_ERROR.getMessageDefinition();

        MetadataServerUncontactableException oe = new MetadataServerUncontactableException(
                messageDefinition,
                className,
                methodName);
        return OMASExceptionToResponse.convertMetadataServerUncontactableException(oe);
    }


    /**
     * return an exception response if the asset is not known
     *
     * @param error       caught exception
     * @param assetGUID   unique identifier for the requested asset
     * @param methodName  name of the method making the call
     * @param serverName  name of this server name of this server
     * @param serviceName name of this access service
     * @return UnrecognizedGUIDException response unexpected exception from property server
     */
    public static SubjectAreaOMASAPIResponse handleUnknownAsset(Throwable error,
                                                                String assetGUID,
                                                                String methodName,
                                                                String serverName,
                                                                String serviceName) {
        ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.GUID_DOES_NOT_EXIST.getMessageDefinition();


        UnrecognizedGUIDException oe = new UnrecognizedGUIDException(
                messageDefinition,
                className,
                methodName,
                assetGUID);
        return OMASExceptionToResponse.convertUnrecognizedGUIDException(oe);

    }

    /**
     * Throw an Invalid Parameter exception
     *
     * @param e           exception to handle
     * @param methodName  name of the method making the call.
     * @param serverName  name of this server
     * @param serviceName name of this access service
     * @return Invalid parameter response
     */
    public static SubjectAreaOMASAPIResponse handleInvalidParameterException(org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e, String methodName, String serverName, String serviceName) {
        ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.INVALID_PARAMETER.getMessageDefinition();

        InvalidParameterException oe = new InvalidParameterException(
                messageDefinition,
                className,
                methodName,
                "",
                null);
        oe.setRelatedProperties(e.getRelatedProperties());
        return OMASExceptionToResponse.convertInvalidParameterException(oe);
    }

    /**
     * Convert the supplied OMRS exception to a Subject Area invalid type name exception and throw it.
     *
     * @param typeName    the name of the unknown type
     * @param methodName  name of the method making the call.
     * @param serverName  name of this server
     * @param serviceName access service name
     * @return InvalidParameterException response a parameter is not valid or missing.
     */
    public static SubjectAreaOMASAPIResponse handleTypeDefNotKnownException(String typeName, String methodName, String serverName, String serviceName) {
        ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.TYPEDEF_NOT_KNOWN.getMessageDefinition();
        // specifying a typedef name that is not correct is a parameter error.

        InvalidParameterException oe = new InvalidParameterException(
                messageDefinition,
                className,
                methodName,
                "",
                null);

        return OMASExceptionToResponse.convertInvalidParameterException(oe);
    }

    /**
     * Convert the supplied OMRS exception to a Subject Area property error exception and throw it.
     *
     * @param e           exception to handle
     * @param methodName  name of the method making the call.
     * @param serverName  name of this server
     * @param serviceName name of this access service
     * @return InvalidParameterException response a parameter is not valid or missing.
     */
    public static SubjectAreaOMASAPIResponse handlePropertyErrorException(org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException e, String methodName, String serverName, String serviceName) {
        ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.INVALID_PARAMETER.getMessageDefinition();

        // property error
        InvalidParameterException oe = new InvalidParameterException(
                messageDefinition,
                className,
                methodName,
                "",
                null);
        oe.setRelatedProperties(e.getRelatedProperties());
        return OMASExceptionToResponse.convertInvalidParameterException(oe);
    }

    /**
     * Convert the supplied OMRS exception to a Subject Area classification error exception and throw it.
     *
     * @param e           exception to handle
     * @param methodName  name of the method making the call.
     * @param serverName  name of this server
     * @param serviceName name of this access service
     * @return ClassificationException error occurred during a classification
     */
    public static SubjectAreaOMASAPIResponse handleClassificationErrorException(org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException e, String methodName, String serverName, String serviceName) {
        ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.CLASSIFICATION_ERROR.getMessageDefinition();
        ClassificationException oe = new ClassificationException(
                messageDefinition,
                className,
                methodName);
        return OMASExceptionToResponse.convertClassificationException(oe);

    }

    /**
     * Convert the supplied OMRS exception to a Subject Area status not known exception and throw it.
     *
     * @param e           exception to handle
     * @param methodName  name of the method making the call.
     * @param serverName  name of this server
     * @param serviceName name of this access service
     * @return StatusNotSupportedException response status not supported response
     */
    public static SubjectAreaOMASAPIResponse handleStatusNotSupportedException(org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException e, String methodName, String serverName, String serviceName) {
        ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.STATUS_NOT_SUPPORTED_ERROR.getMessageDefinition();

        StatusNotSupportedException oe = new StatusNotSupportedException(
                messageDefinition,
                className,
                methodName);
        return OMASExceptionToResponse.convertStatusNotsupportedException(oe);
    }

    /**
     * Convert the supplied OMRS exception to a Subject Area Entity Not known exception and throw it.
     *
     * @param guid        supplied guid
     * @param methodName  name of the method making the call.
     * @param serverName  name of this server
     * @param serviceName name of this access service
     * @return UnrecognizedGUIDException unrecognized GUID response
     */
    public static SubjectAreaOMASAPIResponse handleEntityNotKnownError(String guid, String methodName, String serverName, String serviceName) {
        ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.ENTITY_NOT_KNOWN_ERROR.getMessageDefinition();

        UnrecognizedGUIDException oe = new UnrecognizedGUIDException(
                messageDefinition,
                className,
                methodName,
                guid);
        return OMASExceptionToResponse.convertUnrecognizedGUIDException(oe);
    }

    /**
     * Convert the supplied OMRS exception to a Subject Area Entity Proxy Only exception and throw it.
     *
     * @param e           exception to handle
     * @param methodName  name of the method making the call.
     * @param serverName  name of this server
     * @param serviceName name of this access service
     * @return MetadataServerUncontactableException   the metadata server cannot be reached response
     */
    public static SubjectAreaOMASAPIResponse handleEntityProxyOnlyException(org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException e, String methodName, String serverName, String serviceName) {
        ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.METADATA_SERVER_UNCONTACTABLE_ERROR.getMessageDefinition();

        MetadataServerUncontactableException oe = new MetadataServerUncontactableException(
                messageDefinition,
                className,
                methodName);
        return OMASExceptionToResponse.convertMetadataServerUncontactableException(oe);

    }

    /**
     * Convert the supplied OMRS exception to a Subject Area type error exception and throw it.
     *
     * @param e           exception to handle
     * @param methodName  name of the method making the call.
     * @param serverName  name of this server
     * @param serviceName name of this access service
     * @return InvalidParameterException a parameter is not valid or missing response
     */
    public static SubjectAreaOMASAPIResponse handleTypeErrorException(org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException e, String methodName, String serverName, String serviceName) {
        ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.TYPEDEF_ERROR.getMessageDefinition();

        InvalidParameterException oe = new InvalidParameterException(
                messageDefinition,
                className,
                methodName,
                "",
                null);
        return OMASExceptionToResponse.convertInvalidParameterException(oe);
    }

    /**
     * Convert the supplied OMRS exception to a Subject Area Function Not Supported exception and throw it.
     *
     * @param e           exception to handle
     * @param methodName  name of the method making the call.
     * @param serverName  name of this server
     * @param serviceName name of this access service
     * @return FunctionNotSupportedException response Function not supported
     */
    public static SubjectAreaOMASAPIResponse handleFunctionNotSupportedException(org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException e, String methodName, String serverName, String serviceName) {
        ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.FUNCTION_NOT_SUPPORTED.getMessageDefinition();

        FunctionNotSupportedException oe = new FunctionNotSupportedException(
                messageDefinition,
                className,
                methodName);
        return OMASExceptionToResponse.convertFunctionNotSupportedException(oe);
    }

    /**
     * Convert the supplied OMRS exception to a Subject Area paging error exception and throw it.
     *
     * @param e           exception to handle
     * @param methodName  name of the method making the call.
     * @param serverName  name of this server
     * @param serviceName name of this access service
     * @return InvalidParameterException response. a parameter is not valid or missing
     */
    public static SubjectAreaOMASAPIResponse handlePagingErrorException(org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException e, String methodName, String serverName, String serviceName) {
        ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.PAGING_ERROR.getMessageDefinition();


        InvalidParameterException oe = new InvalidParameterException(
                messageDefinition,
                className,
                methodName,
                "",
                null);
        return OMASExceptionToResponse.convertInvalidParameterException(oe);
    }

    /**
     * Convert the supplied OMRS exception to a Subject Area relationship not known exception and throw it.
     *
     * @param guid        supplied guid
     * @param methodName  name of the method making the call.
     * @param serverName  name of this server
     * @param serviceName name of this access service
     * @return UnrecognizedGUIDException response - GUID not recognized
     */
    public static SubjectAreaOMASAPIResponse handleRelationshipNotKnownException(String guid, String methodName, String serverName, String serviceName) {
        ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.GUID_DOES_NOT_EXIST.getMessageDefinition();

        UnrecognizedGUIDException oe = new UnrecognizedGUIDException(
                messageDefinition,
                className,
                methodName,
                guid);
        return OMASExceptionToResponse.convertUnrecognizedGUIDException(oe);

    }

    /**
     * Convert the supplied OMRS exception to a Subject Area Entity not known exception and throw it.
     *
     * @param guid        supplied guid
     * @param methodName  name of the method making the call.
     * @param serverName  name of this server
     * @param serviceName name of this access service
     * @return UnrecognizedGUIDException response - GUID not recognized
     */
    public static SubjectAreaOMASAPIResponse handleEntityNotKnownException(String guid, String methodName, String serverName, String serviceName) {
        ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.GUID_DOES_NOT_EXIST.getMessageDefinition();

        UnrecognizedGUIDException oe = new UnrecognizedGUIDException(
                messageDefinition,
                className,
                methodName,
                guid);
        return OMASExceptionToResponse.convertUnrecognizedGUIDException(oe);
    }

    /**
     * Convert the supplied OMRS exception to a Subject Area entity not deleted exception and throw it.
     *
     * @param guid        supplied guid
     * @param methodName  name of the method making the call.
     * @param serverName  name of this server
     * @param serviceName name of this access service
     * @return EntityNotDeletedException Relationship not purged
     */
    public static SubjectAreaOMASAPIResponse handleEntityNotDeletedException(String guid,
                                                                             String methodName,
                                                                             String serverName,
                                                                             String serviceName
                                                                            ) {
        ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.GUID_NOT_DELETED_ERROR.getMessageDefinition();

        EntityNotDeletedException oe = new EntityNotDeletedException(
                messageDefinition,
                className,
                methodName,
                guid);
        return OMASExceptionToResponse.convertEntityNotDeletedException(oe);
    }

    /**
     * Convert the supplied OMRS exception to a Subject Area relationship not deleted exception and throw it.
     *
     * @param guid        supplied guid
     * @param methodName  name of the method making the call.
     * @param serverName  name of this server
     * @param serviceName name of this access service
     * @return EntityNotDeletedException response Relationship not purged
     */
    public static SubjectAreaOMASAPIResponse handleRelationshipNotDeletedException(String guid,
                                                                                   String methodName,
                                                                                   String serverName,
                                                                                   String serviceName
                                                                                  ) {
        ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.GUID_NOT_DELETED_ERROR.getMessageDefinition();

        RelationshipNotDeletedException oe = new RelationshipNotDeletedException(
                messageDefinition,
                className,
                methodName,
                guid);
        return OMASExceptionToResponse.convertRelationshipNotDeletedException(oe);
    }

    /**
     * Convert the supplied OMRS exception to a Subject Area metadata server not reachable exception and throw it.
     *
     * @param e           exception to handle
     * @param methodName  name of the method making the call.
     * @param serverName  name of this server
     * @param serviceName name of this access service
     * @return MetadataServerUncontactableException response -the metadata server cannot be reached
     */
    public SubjectAreaOMASAPIResponse handleMetadataServerUnContactable(MetadataServerUncontactableException e, String methodName, String serverName, String serviceName) {
        ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.METADATA_SERVER_UNCONTACTABLE_ERROR.getMessageDefinition();

        MetadataServerUncontactableException oe = new MetadataServerUncontactableException(
                messageDefinition,
                className,
                methodName
        );
        return OMASExceptionToResponse.convertMetadataServerUncontactableException(oe);
    }

    public SubjectAreaOMASAPIResponse handleEntityNotPurgedException(String obsoleteGuid, String methodName, String serverName, String serviceName) {
        ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.GUID_NOT_PURGED_ERROR.getMessageDefinition();

        EntityNotPurgedException oe = new EntityNotPurgedException(
                messageDefinition,
                className,
                methodName,
                obsoleteGuid
        );
        return OMASExceptionToResponse.convertEntityNotPurgedException(oe);
    }

    public SubjectAreaOMASAPIResponse handleRelationshipNotPurgedException(String obsoleteGuid, String methodName, String serverName, String serviceName) {
        ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.GUID_NOT_PURGED_ERROR.getMessageDefinition();

        RelationshipNotPurgedException oe = new RelationshipNotPurgedException(
                messageDefinition,
                className,
                methodName,
                obsoleteGuid
        );
        return OMASExceptionToResponse.convertRelationshipNotPurgedException(oe);
    }
}
