/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions;

public class DataViewCreationException extends InformationViewExceptionBase{


    private String dataViewId;



    public DataViewCreationException(int httpCode,
                                   String className,
                                   String actionDescription,
                                   String errorMessage,
                                   String systemAction,
                                   String userAction,
                                   String dataViewId) {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
        this.dataViewId = dataViewId;
    }

    public DataViewCreationException(int httpCode, String className, String actionDescription, String errorMessage, String systemAction, String userAction, Throwable throwable, String dataViewId) {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, throwable);
        this.dataViewId = dataViewId;
    }
}
