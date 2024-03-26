/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.governanceservers.lineagewarehouse.util;

import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.ffdc.LineageWarehouseException;


/**
 * Extend the standard REST exception handler to support the LineageWarehouseException.
 */
public class LineageWarehouseExceptionHandler extends RESTExceptionHandler
{

    public void captureOpenLineageException(FFDCResponseBase response,
                                            LineageWarehouseException e)
    {
        response.setRelatedHTTPCode(e.getReportedHTTPCode());
        response.setExceptionClassName(e.getClass().getName());
        response.setExceptionErrorMessage(e.getReportedErrorMessage());
        response.setExceptionSystemAction(e.getReportedSystemAction());
        response.setExceptionUserAction(e.getReportedUserAction());
    }

    /**
     * Throw an LineageWarehouseException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws LineageWarehouseException encoded exception from the server
     */
    public void detectAndThrowLineageWarehouseException(String           methodName,
                                                        FFDCResponseBase restResult) throws LineageWarehouseException
    {
        final String exceptionClassName = LineageWarehouseException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new LineageWarehouseException(restResult.getRelatedHTTPCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                restResult.getExceptionErrorMessage(),
                                                restResult.getExceptionSystemAction(),
                                                restResult.getExceptionUserAction());
        }
    }
}
