/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.util;

import org.odpi.openmetadata.accessservices.dataengine.exception.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.exception.DataEngineException;
import org.odpi.openmetadata.accessservices.dataengine.exception.NewInstanceException;
import org.odpi.openmetadata.accessservices.dataengine.exception.PropertyServerException;
import org.odpi.openmetadata.accessservices.dataengine.exception.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataEngineOMASAPIResponse;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSCheckedExceptionBase;

public class DataEngineErrorHandler {

    /**
     * Throws a NewInstanceException
     *
     * @param errorCode  error code for the exception thrown
     * @param methodName name of the method where the exception occurs
     * @throws NewInstanceException a problem occurred during initialization
     */
    public void handleNewInstanceException(DataEngineErrorCode errorCode, String methodName) throws
                                                                                             NewInstanceException {
        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

        throw new NewInstanceException(errorCode.getHttpErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Throws a PropertyServerException
     *
     * @param errorCode  error code for the exception thrown
     * @param methodName name of the method where the exception occurs
     * @throws PropertyServerException no available instance for the requested server
     */
    public void handlePropertyServerException(DataEngineErrorCode errorCode, String methodName) throws
                                                                                                PropertyServerException {
        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

        throw new PropertyServerException(errorCode.getHttpErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Throw an exception if the supplied userId is null
     *
     * @param userId     user name to validate
     * @param methodName name of the method making the call.
     * @throws UserNotAuthorizedException the userId is null
     */
    public void validateUserId(String userId, String methodName) throws UserNotAuthorizedException {
        if (userId == null) {
            DataEngineErrorCode errorCode = DataEngineErrorCode.NULL_USER_ID;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new UserNotAuthorizedException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }

    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param e        returned response
     */
    public void captureOMRSCheckedExceptionBase(DataEngineOMASAPIResponse response, OMRSCheckedExceptionBase e) {
        captureException(response, e.getReportedHTTPCode(), e.getClass().getName(), e.getErrorMessage(),
                e.getReportedSystemAction(), e.getReportedUserAction());
    }

    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param e        returned response
     */
    public void captureDataEngineException(DataEngineOMASAPIResponse response, DataEngineException e) {
        captureException(response, e.getReportedHTTPCode(), e.getClass().getName(), e.getReportedErrorMessage(),
                e.getReportedSystemAction(), e.getReportedUserAction());
    }

    private void captureException(DataEngineOMASAPIResponse response, int reportedHTTPCode, String name,
                                  String errorMessage, String reportedSystemAction, String reportedUserAction) {
        response.setRelatedHTTPCode(reportedHTTPCode);
        response.setExceptionClassName(name);
        response.setExceptionErrorMessage(errorMessage);
        response.setExceptionSystemAction(reportedSystemAction);
        response.setExceptionUserAction(reportedUserAction);
    }
}
