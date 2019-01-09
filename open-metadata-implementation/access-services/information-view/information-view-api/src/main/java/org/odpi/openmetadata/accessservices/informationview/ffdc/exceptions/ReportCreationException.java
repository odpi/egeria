/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions;


public class ReportCreationException extends InformationViewExceptionBase{

    private   String  reportName;

    public ReportCreationException(int httpCode,
                                   String className,
                                   String actionDescription,
                                   String errorMessage,
                                   String systemAction,
                                   String userAction,
                                   String reportName) {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
        this.reportName = reportName;
    }

    public ReportCreationException(int httpCode, String className, String actionDescription, String errorMessage, String systemAction, String userAction, Throwable throwable, String reportName) {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, throwable);
        this.reportName = reportName;
    }
}
