/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformcatalogfvt;

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
 * <br>
 * It matters more here than in the other FVT suites: the cataloguer reaches the platform over the same REST
 * APIs the tests use, unauthenticated, and everything it reads about the platform - the platform report,
 * the list of known servers, each server's stored configuration - comes back through this filter chain.
 */
@Configuration
@EnableWebSecurity
public class PlatformCatalogFvtSecurityConfig
{
    @Bean
    public SecurityFilterChain platformCatalogFvtSecurityFilterChain(HttpSecurity http) throws Exception
    {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(requests -> requests.anyRequest().permitAll());

        return http.build();
    }
}
