/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.services;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedExceptionBase;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.subjectarea.responses.InvalidParameterExceptionResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.UnexpectedExceptionResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.UserNotAuthorizedExceptionResponse;
import org.odpi.openmetadata.accessservices.subjectarea.utils.DetectUtils;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.viewservices.glossaryauthor.auditlog.GlossaryAuthorViewAuditCode;
import org.odpi.openmetadata.viewservices.glossaryauthor.initialization.GlossaryAuthorViewInstanceHandler;
import org.slf4j.LoggerFactory;

/**
 * An abstract base class providing common methods for glossary authoring services.
 */
abstract public class BaseGlossaryAuthorView {
    protected static GlossaryAuthorViewInstanceHandler instanceHandler     = new GlossaryAuthorViewInstanceHandler();
    protected static RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(GlossaryAuthorViewGlossaryRESTServices.class),
            instanceHandler.getServiceName());

    /**
     * Get the appropriate response from the supplied Exception
     * @param error - supplied exception
     * @param auditLog - auditlog (may be null if unable to initialize)
     * @param methodName - calling method's name
     * @return response corresponding to the error.
     */
    protected SubjectAreaOMASAPIResponse getResponseForError(Throwable error, AuditLog auditLog, String methodName) {
        SubjectAreaOMASAPIResponse response = null;
        if (error instanceof OCFCheckedExceptionBase) {
            response = getResponseFromOCFCheckedExceptionBase(( OCFCheckedExceptionBase)error);
        } else if (error instanceof SubjectAreaCheckedExceptionBase) {
            response = DetectUtils.getResponseFromException((SubjectAreaCheckedExceptionBase)error);
        } else  {
            response = new UnexpectedExceptionResponse(methodName, error.getMessage());
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                        GlossaryAuthorViewAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(error.getClass().getName(), methodName, error.getMessage()),
                        error);
            }
        }
        return response;
    }

    /**
     * Return the equivalent Subject Area exception response if the passed OCF checked exception is an invalid parameter, property server or user not authorized error.
     * If the passed exception is not one of the 3 we we expecting then return null.
     * @param ocfCheckedExceptionBase ocf checked exception
     * @return subject area exception response
     */
    private SubjectAreaOMASAPIResponse getResponseFromOCFCheckedExceptionBase(OCFCheckedExceptionBase ocfCheckedExceptionBase) {
        SubjectAreaOMASAPIResponse response =null;
        if (ocfCheckedExceptionBase instanceof org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException) {
            InvalidParameterException e = new InvalidParameterException(
                    ocfCheckedExceptionBase.getReportedHTTPCode(),
                    ocfCheckedExceptionBase.getReportingClassName(),
                    ocfCheckedExceptionBase.getReportingActionDescription(),
                    ocfCheckedExceptionBase.getErrorMessage(),
                    ocfCheckedExceptionBase.getReportedSystemAction(),
                    ocfCheckedExceptionBase.getReportedUserAction());
            response = new InvalidParameterExceptionResponse(e);
        } else if (ocfCheckedExceptionBase instanceof org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException) {
            UserNotAuthorizedException e  = new UserNotAuthorizedException(
                    ocfCheckedExceptionBase.getReportedHTTPCode(),
                    ocfCheckedExceptionBase.getReportingClassName(),
                    ocfCheckedExceptionBase.getReportingActionDescription(),
                    ocfCheckedExceptionBase.getErrorMessage(),
                    ocfCheckedExceptionBase.getReportedSystemAction(),
                    ocfCheckedExceptionBase.getReportedUserAction(),
                    ((org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException) ocfCheckedExceptionBase).getUserId());
            response = new UserNotAuthorizedExceptionResponse(e);
        } else if (ocfCheckedExceptionBase instanceof org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException) {
            InvalidParameterException e = new InvalidParameterException(
                    ocfCheckedExceptionBase.getReportedHTTPCode(),
                    ocfCheckedExceptionBase.getReportingClassName(),
                    ocfCheckedExceptionBase.getReportingActionDescription(),
                    ocfCheckedExceptionBase.getErrorMessage(),
                    ocfCheckedExceptionBase.getReportedSystemAction(),
                    ocfCheckedExceptionBase.getReportedUserAction());
            response = new InvalidParameterExceptionResponse(e);
        }
        return response;
    }

}
