/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.subjectarea.responses.InvalidParameterExceptionResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.PropertyServerExceptionResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.UserNotAuthorizedExceptionResponse;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;

public class ExceptionMapper {
    /**
     * Return the equivalent Subject Area exception response if the passed OCF checked exception is an invalid parameter, property server or user not authorized error.
     * If the passed exception is not one of the 3 we we expecting then return null.
     * @param ocfCheckedExceptionBase ocf checked exception
     * @return subject area exception response
     */
    public static SubjectAreaOMASAPIResponse getResponseFromOCFCheckedExceptionBase(OCFCheckedExceptionBase ocfCheckedExceptionBase) {
        SubjectAreaOMASAPIResponse response =null;
        ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.valueOf( ocfCheckedExceptionBase.getReportedErrorMessageId()).getMessageDefinition();
        if (ocfCheckedExceptionBase instanceof org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException) {
            InvalidParameterException e = new InvalidParameterException(
                    messageDefinition,
                    ocfCheckedExceptionBase.getReportingClassName(),
                    ocfCheckedExceptionBase.getReportingActionDescription(),
                    "",
                    null);
            response = new InvalidParameterExceptionResponse(e);
        } else if (ocfCheckedExceptionBase instanceof org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException) {
            org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException e  = new UserNotAuthorizedException(
                    messageDefinition,
                    ocfCheckedExceptionBase.getReportingClassName(),
                    ocfCheckedExceptionBase.getReportingActionDescription(),
                    ((org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException) ocfCheckedExceptionBase).getUserId());
            response = new UserNotAuthorizedExceptionResponse(e);
        } else if (ocfCheckedExceptionBase instanceof org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException) {
            PropertyServerException e = new PropertyServerException(
                    messageDefinition,
                    ocfCheckedExceptionBase.getReportingClassName(),
                    ocfCheckedExceptionBase.getReportingActionDescription());
            response = new PropertyServerExceptionResponse(e);
        }
        return response;
    }
}
