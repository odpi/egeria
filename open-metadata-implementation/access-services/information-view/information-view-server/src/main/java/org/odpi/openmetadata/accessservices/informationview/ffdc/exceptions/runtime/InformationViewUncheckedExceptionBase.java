/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime;

public class InformationViewUncheckedExceptionBase extends RuntimeException{

    private String reportingClassName;
    private String reportedErrorMessage;
    private String reportedSystemAction;
    private String reportedUserAction;
    private Throwable reportedCaughtException ;

    public InformationViewUncheckedExceptionBase(
            String className,
            String errorMessage,
            String systemAction,
            String userAction,
            Throwable caughtError) {
        super(errorMessage, caughtError);
        this.reportingClassName = className;
        this.reportedErrorMessage = errorMessage;
        this.reportedSystemAction = systemAction;
        this.reportedUserAction = userAction;
        this.reportedCaughtException = caughtError;
    }



}
