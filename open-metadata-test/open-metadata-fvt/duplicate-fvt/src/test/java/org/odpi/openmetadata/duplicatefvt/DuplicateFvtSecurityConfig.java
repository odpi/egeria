/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.duplicatefvt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * This suite's platform has no user directory configured, so there is nothing to authenticate against.
 * This configuration keeps Spring Security's filter chain in place (the admin/platform REST controllers
 * need an {@code Authentication} in the security context on every call, even an anonymous one) while
 * permitting every request through unchallenged.
 */
@Configuration
@EnableWebSecurity
public class DuplicateFvtSecurityConfig
{
    @Bean
    public SecurityFilterChain duplicateFvtSecurityFilterChain(HttpSecurity http) throws Exception
    {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(requests -> requests.anyRequest().permitAll());

        return http.build();
    }
}
