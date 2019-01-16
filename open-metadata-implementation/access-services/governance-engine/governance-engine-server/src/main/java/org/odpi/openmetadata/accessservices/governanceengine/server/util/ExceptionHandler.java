/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceengine.server.util;

import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.ClassificationNotFoundException;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.GovernanceEngineCheckedExceptionBase;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.GuidNotFoundException;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.MetadataServerException;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.TypeNotFoundException;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernanceEngineOMASAPIResponse;

public class ExceptionHandler {

    /**
     * Set the exception information into the response.
     *
     * @param response           - REST Response
     * @param error              returned response.
     * @param exceptionClassName - class name of the exception to recreate
     */
    public void captureCheckedException(GovernanceEngineOMASAPIResponse response,
                                         GovernanceEngineCheckedExceptionBase error,
                                         String exceptionClassName) {
        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(exceptionClassName);
        response.setExceptionErrorMessage(error.getErrorMessage());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
    }

    /**
     * Set the exception information into the response.
     *
     * @param response - REST Response
     * @param error    returned response.
     */
    public void captureInvalidParameterException(GovernanceEngineOMASAPIResponse response,
                                                  InvalidParameterException error) {
        captureCheckedException(response, error, error.getClass().getName());
    }

    /**
     * Set the exception information into the response.
     *
     * @param response - REST Response
     * @param error    returned response.
     */
    public void captureMetadataServerException(GovernanceEngineOMASAPIResponse response,
                                                MetadataServerException error) {
        captureCheckedException(response, error, error.getClass().getName());
    }

    /**
     * Set the exception information into the response.
     *
     * @param response - REST Response
     * @param error    returned response.
     */
    public void captureClassificationNotFoundException(GovernanceEngineOMASAPIResponse response,
                                                        ClassificationNotFoundException error) {
        captureCheckedException(response, error, error.getClass().getName());
    }

    /**
     * Set the exception information into the response.
     *
     * @param response - REST Response
     * @param error    returned response.
     */
    public void captureUserNotAuthorizedException(GovernanceEngineOMASAPIResponse response,
                                                   UserNotAuthorizedException error) {
        captureCheckedException(response, error, error.getClass().getName());
    }

    /**
     * Set the exception information into the response.
     *
     * @param response - REST Response
     * @param error    returned response.
     */
    public void captureTypeNotFoundException(GovernanceEngineOMASAPIResponse response,
                                              TypeNotFoundException error) {
        captureCheckedException(response, error, error.getClass().getName());
    }

    /**
     * Set the exception information into the response.
     *
     * @param response - REST Response
     * @param error    returned response.
     */
    public void captureGuidNotFoundException(GovernanceEngineOMASAPIResponse response,
                                              GuidNotFoundException error) {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response - REST Response
     * @param error    returned response.
     */
    public void capturePropertyServerException(GovernanceEngineOMASAPIResponse response,
                                                PropertyServerException error) {
        captureCheckedException(response, error, error.getClass().getName());
    }

}
