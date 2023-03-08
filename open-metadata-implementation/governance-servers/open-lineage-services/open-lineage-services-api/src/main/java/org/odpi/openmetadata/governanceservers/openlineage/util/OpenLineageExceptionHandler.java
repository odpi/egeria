/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.governanceservers.openlineage.util;

import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;

public class OpenLineageExceptionHandler extends RESTExceptionHandler {

    public void captureOpenLineageException(FFDCResponseBase response, OpenLineageException e) {
        response.setRelatedHTTPCode(e.getReportedHTTPCode());
        response.setExceptionClassName(e.getClass().getName());
        response.setExceptionErrorMessage(e.getReportedErrorMessage());
        response.setExceptionSystemAction(e.getReportedSystemAction());
        response.setExceptionUserAction(e.getReportedUserAction());
    }

    /**
     * Throw an detectAndThrowOpenLineageException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws OpenLineageException encoded exception from the server
     */
    public void detectAndThrowOpenLineageException(String methodName,
                                                   FFDCResponseBase restResult) throws OpenLineageException {
        final String exceptionClassName = OpenLineageException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName()))) {
            throw new OpenLineageException(restResult.getRelatedHTTPCode(),
                    this.getClass().getName(),
                    methodName,
                    restResult.getExceptionErrorMessage(),
                    restResult.getExceptionSystemAction(),
                    restResult.getExceptionUserAction());
        }
    }


}
