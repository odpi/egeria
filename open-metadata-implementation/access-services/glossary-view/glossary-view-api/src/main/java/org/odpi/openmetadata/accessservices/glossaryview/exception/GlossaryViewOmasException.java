/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.exception;

import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSCheckedExceptionBase;

/**
 * Generic exception used when unknown exceptions are coming from the omrs layer. Made to be used only by {@code OMRSClient}
 */
public class GlossaryViewOmasException extends OMRSCheckedExceptionBase {

    public GlossaryViewOmasException(int  httpCode, String className, String  actionDescription, String errorMessage,
                                     String systemAction, String userAction) {
        super(httpCode, className, actionDescription, errorMessage, null, null,
                systemAction, userAction, null, null);
    }

    public GlossaryViewOmasException(OMRSCheckedExceptionBase omrsCheckedExceptionBase){
        this(omrsCheckedExceptionBase.getReportedHTTPCode(), omrsCheckedExceptionBase.getReportingClassName(),
                omrsCheckedExceptionBase.getReportingActionDescription(), omrsCheckedExceptionBase.getReportedErrorMessage(),
                omrsCheckedExceptionBase.getReportedSystemAction(), omrsCheckedExceptionBase.getReportedUserAction());
    }

}
