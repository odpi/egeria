/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime;


public class DataViewCreationException extends InformationViewExceptionBase {

    private static final long    serialVersionUID = 1L;

    private String dataViewId;



    public DataViewCreationException(int httpCode,
                                   String className,
                                   String errorMessage,
                                   String systemAction,
                                   String userAction,
                                   String dataViewId) {
        super(httpCode, className, errorMessage, systemAction, userAction, null);
        this.dataViewId = dataViewId;
    }

    public DataViewCreationException(int httpCode, String className, String errorMessage, String systemAction, String userAction, Throwable throwable, String dataViewId) {
        super(httpCode, className, errorMessage, systemAction, userAction, throwable);
        this.dataViewId = dataViewId;
    }

    public String getDataViewId() {
        return dataViewId;
    }

    public void setDataViewId(String dataViewId) {
        this.dataViewId = dataViewId;
    }
}
