/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime;

public class IllegalUpdateException extends InformationViewExceptionBase {

    private static final long    serialVersionUID = 1L;

    public IllegalUpdateException(int httpErrorCode, String reportingClassName, String reportedErrorMessage,
                                  String reportedSystemAction, String reportedUserAction,
                                  Throwable reportedCaughtException) {
        super(httpErrorCode, reportingClassName, reportedErrorMessage, reportedSystemAction, reportedUserAction, reportedCaughtException);
    }
}
