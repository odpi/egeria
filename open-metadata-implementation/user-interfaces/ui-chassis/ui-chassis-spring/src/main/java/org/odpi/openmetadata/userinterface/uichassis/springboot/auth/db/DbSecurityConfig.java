/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.auth.db;

import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.AuthenticationExceptionHandler;
import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.ldap.InvalidSearchFilterException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@Configuration("securityConfig")
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty(value = "authentication.source", havingValue = "db")
public class DbSecurityConfig extends SecurityConfig {

    @Autowired
    @Qualifier("dbUserDetailsService")
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected AuthenticationExceptionHandler getAuthenticationExceptionHandler() {
        return new AuthenticationExceptionHandler() {
            @Override
            public boolean isBadCredentials(AuthenticationException e) {
                return e instanceof BadCredentialsException;
            }
        };
    }
}
