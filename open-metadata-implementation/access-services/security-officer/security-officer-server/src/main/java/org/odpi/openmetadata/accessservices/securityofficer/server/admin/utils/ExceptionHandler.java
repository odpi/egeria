/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */

package org.odpi.openmetadata.accessservices.securityofficer.server.admin.utils;

import org.odpi.openmetadata.accessservices.securityofficer.api.ffdc.exceptions.SecurityOfficerException;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.rest.SecurityOfficerOMASAPIResponse;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSCheckedExceptionBase;

public class ExceptionHandler {

    /**
     * Set the exception information into the response.
     *
     * @param response           - REST Response
     * @param error              returned response.
     * @param exceptionClassName - class name of the exception to recreate
     */
    public void captureCheckedException(SecurityOfficerOMASAPIResponse response,
                                        SecurityOfficerException error,
                                        String exceptionClassName) {
        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(exceptionClassName);
        response.setExceptionErrorMessage(error.getErrorMessage());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
    }

    public void captureOMRSException(SecurityOfficerOMASAPIResponse response,
                                     OMRSCheckedExceptionBase error) {
        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(error.getClass().getName());
        response.setExceptionErrorMessage(error.getErrorMessage());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
    }

}