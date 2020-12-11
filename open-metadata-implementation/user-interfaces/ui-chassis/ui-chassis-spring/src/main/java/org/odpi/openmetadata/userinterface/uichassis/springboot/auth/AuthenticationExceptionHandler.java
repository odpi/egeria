/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.auth;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

/**
 * Handles AuthenticationException for different instances of WebSecurityConfigurerAdapter used for different
 * authentication mechanism used
 */
public interface AuthenticationExceptionHandler {

    /**
     *
     * @param e the AuthenticationException thrown by authentication attempt
     * @return whether or not is an bad credentials related exception
     */
     boolean isBadCredentials(AuthenticationException e);
}
