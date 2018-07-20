/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;

public class OMASExceptionToResponse {

    public static SubjectAreaOMASAPIResponse convertInvalidParameterException(InvalidParameterException e) {
        InvalidParameterExceptionResponse response = new InvalidParameterExceptionResponse(e);
        return response;

    }
    public static SubjectAreaOMASAPIResponse convertUserNotAuthorizedException(UserNotAuthorizedException e) {
        UserNotAuthorizedExceptionResponse response = new UserNotAuthorizedExceptionResponse(e);
        return response;
    }

    public static SubjectAreaOMASAPIResponse convertPropertyServerException(PropertyServerException e) {
        PropertyServerExceptionResponse response =new PropertyServerExceptionResponse(e);
        return response;
    }

    public static SubjectAreaOMASAPIResponse convertUnrecognizedGUIDException(UnrecognizedGUIDException e) {
        UnrecognizedGUIDExceptionResponse response = new  UnrecognizedGUIDExceptionResponse(e);
        return response;
    }


    public static SubjectAreaOMASAPIResponse convertFunctionNotSupportedException(FunctionNotSupportedException e) {
        FunctionNotSupportedExceptionResponse response = new   FunctionNotSupportedExceptionResponse(e);
        return response;
    }


    public static SubjectAreaOMASAPIResponse convertStatusNotsupportedException(StatusNotSupportedException e) {
        StatusNotsupportedExceptionResponse response= new StatusNotsupportedExceptionResponse(e);
        return response;
    }

    public static SubjectAreaOMASAPIResponse convertClassificationException(ClassificationException e) {
        ClassificationExceptionResponse response = new ClassificationExceptionResponse(e);
        return response;
    }

    public static SubjectAreaOMASAPIResponse convertGUIDNotPurgedException(GUIDNotPurgedException e) {
        GUIDNotPurgedExceptionResponse response = new GUIDNotPurgedExceptionResponse(e);
        return response;
    }

    public static SubjectAreaOMASAPIResponse convertEntityNotDeletedException(EntityNotDeletedException e) {
        EntityNotDeletedExceptionResponse response = new EntityNotDeletedExceptionResponse(e);
        return response;
    }
}
