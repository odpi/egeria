/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime;

public class DeleteRelationshipException extends InformationViewExceptionBase{

    private static final long    serialVersionUID = 1L;

    public DeleteRelationshipException(String className, String errorMessage, String systemAction, String userAction, Throwable caughtError) {
        super(className, errorMessage, systemAction, userAction, caughtError);
    }

    public DeleteRelationshipException(int httpErrorCode, String reportingClassName, String reportedErrorMessage,
                                       String reportedSystemAction, String reportedUserAction,
                                       Throwable reportedCaughtException) {
        super(httpErrorCode, reportingClassName, reportedErrorMessage, reportedSystemAction, reportedUserAction,
                reportedCaughtException);
    }
}
