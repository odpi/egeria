/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server;

import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.NoProfileForUserException;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;

import java.util.HashMap;
import java.util.Map;

/**
 * RESTExceptionHandler provides support for Community Profile OMAS specific exceptions
 */
public class RESTExceptionHandler extends org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler
{
    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    void captureNoProfileForUserException(FFDCResponseBase          response,
                                          NoProfileForUserException error)
    {
        String  userId = error.getUserId();

        if (userId != null)
        {
            Map<String, Object> exceptionProperties = new HashMap<>();

            exceptionProperties.put("userId", userId);
            captureCheckedException(response, error, error.getClass().getName(), exceptionProperties);
        }
        else
        {
            captureCheckedException(response, error, error.getClass().getName());
        }
    }

}
