/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;

public class OMASExceptionToResponse {

    public static SubjectAreaOMASAPIResponse convertInvalidParameterException(InvalidParameterException e) {
        return new InvalidParameterExceptionResponse(e);
    }
    public static SubjectAreaOMASAPIResponse convertUserNotAuthorizedException(UserNotAuthorizedException e) {
       return  new UserNotAuthorizedExceptionResponse(e);

    }

    public static SubjectAreaOMASAPIResponse convertUnrecognizedGUIDException(UnrecognizedGUIDException e) {
       return new UnrecognizedGUIDExceptionResponse(e);
    }

    public static SubjectAreaOMASAPIResponse convertFunctionNotSupportedException(FunctionNotSupportedException e) {
       return new  FunctionNotSupportedExceptionResponse(e);
    }

    public static SubjectAreaOMASAPIResponse convertStatusNotsupportedException(StatusNotSupportedException e) {
       return new StatusNotsupportedExceptionResponse(e);
    }

    public static SubjectAreaOMASAPIResponse convertClassificationException(ClassificationException e) {
       return new ClassificationExceptionResponse(e);
    }

    public static SubjectAreaOMASAPIResponse convertEntityNotPurgedException(EntityNotPurgedException e) {
       return new EntityNotPurgedExceptionResponse(e);
    }

    public static SubjectAreaOMASAPIResponse convertEntityNotDeletedException(EntityNotDeletedException e)
    {
       return new EntityNotDeletedExceptionResponse(e);
    }
    public static SubjectAreaOMASAPIResponse convertRelationshipNotPurgedException(RelationshipNotPurgedException e) {
        return new RelationshipNotPurgedExceptionResponse(e);
    }

    public static SubjectAreaOMASAPIResponse convertRelationshipNotDeletedException(RelationshipNotDeletedException e)
    {
        return new RelationshipNotDeletedExceptionResponse(e);
    }


    public static SubjectAreaOMASAPIResponse convertMetadataServerUncontactableException(MetadataServerUncontactableException e) {
       return new MetadataServerUncontactableExceptionResponse(e);
    }


}