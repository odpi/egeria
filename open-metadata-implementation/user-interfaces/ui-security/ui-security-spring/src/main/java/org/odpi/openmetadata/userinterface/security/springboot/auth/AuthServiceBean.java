/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.userinterface.security.springboot.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class is a Spring bean that returns either a session or a token spring authentication service,
 * depending on the authentication.mode property
 */
@Configuration
public class AuthServiceBean{
    /**
     * get the Spring authentication service
     * @param authenticationMode configured authentication mode
     * @return appropriate session or token authService
     */
    @Bean
    public AuthService getAuthService(@Value("${authentication.mode}") String authenticationMode)  {
        if(null == authenticationMode || authenticationMode.isEmpty() || "token".equals(authenticationMode)){
            return new TokenAuthService();
        }
        return new SessionAuthService();
    }
}
