/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.client;

import org.odpi.openmetadata.accessservices.glossaryview.exception.GlossaryViewOmasException;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;

public class GlossaryViewClientExceptionHandler extends RESTExceptionHandler {

    /**
     * Throw an InvalidParameterException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called
     * @param restResult  response from the rest call
     *
     * @throws GlossaryViewOmasException encoded exception from the server
     */
    public void detectAndThrowGlossaryViewOmasException(String methodName, GlossaryViewEntityDetailResponse restResult) throws GlossaryViewOmasException {
        final String exceptionClassName = GlossaryViewOmasException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new GlossaryViewOmasException(restResult.getRelatedHTTPCode(), this.getClass().getName(), restResult.getActionDescription(),
                    restResult.getExceptionErrorMessage(), restResult.getExceptionSystemAction(), restResult.getExceptionUserAction() );
        }
    }

}
