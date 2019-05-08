/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.exception;

import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSRuntimeException;

/**
 * Stores checked OMRS exceptions info until it is sent to the client. Made to be used only by {@code OMRSClient} in lambdas
 *
 */
public class OMRSRuntimeExceptionWrapper extends OMRSRuntimeException {

    public OMRSRuntimeExceptionWrapper(int httpCode, String className, String  actionDescription, String errorMessage,
                                       String systemAction, String userAction) {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
    }

    public OMRSRuntimeExceptionWrapper(OMRSCheckedExceptionBase omrsCheckedExceptionBase){
        this(omrsCheckedExceptionBase.getReportedHTTPCode(), omrsCheckedExceptionBase.getReportingClassName(),
                omrsCheckedExceptionBase.getReportingActionDescription(), omrsCheckedExceptionBase.getErrorMessage(),
                omrsCheckedExceptionBase.getReportedSystemAction(), omrsCheckedExceptionBase.getReportedUserAction());
    }

}
