/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.tokencontroller;

import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.tokencontroller.ffdc.TokenControllerErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * This class provides a method to access the userId from the servlet session. This class should be subclassed by the
 * View Service REST Service so that the userId can be obtained and then used on subsequent runtime calls.
 */
public class TokenController
{
    private static final Logger log = LoggerFactory.getLogger(RESTExceptionHandler.class);

    /**
     * Return userId extracted from the token in the HTTP header.
     *
     * @param serviceName name of the called service
     * @param methodName name of the called method
     * @return userName or null if there is not one
     * @throws UserNotAuthorizedException user not logged in
     */
    protected String getUser(String serviceName,
                             String methodName) throws UserNotAuthorizedException
    {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        if (userName == null)
        {
            log.error("No user for service " + serviceName + " and operation " + methodName);

            throw new UserNotAuthorizedException(TokenControllerErrorCode.NO_USER.getMessageDefinition(serviceName, methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 "<null>");
        }

        return userName;
    }
}
