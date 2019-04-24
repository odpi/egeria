/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime;


import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.InformationViewUncheckedExceptionBase;

public class ReportSubmitException extends InformationViewUncheckedExceptionBase {

    private String reportName;

    public ReportSubmitException(int httpCode,
                                 String className,
                                 String errorMessage,
                                 String systemAction,
                                 String userAction,
                                 Throwable throwable,
                                 String reportName) {
        super(httpCode, className, errorMessage, systemAction, userAction, throwable);
        this.reportName = reportName;
    }
}
