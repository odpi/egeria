/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.bitolfvt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * This suite's platform has no user directory configured, so there is nothing to authenticate against.  This
 * configuration keeps Spring Security's filter chain in place - the admin, platform and governance server REST
 * controllers all require an {@code Authentication} in the security context on every call, even an anonymous
 * one - while permitting every request through unchallenged.  The anonymous principal is named after this
 * suite's user so that everything the suite does is attributed to it.
 */
@Configuration
@EnableWebSecurity
public class BitolFvtSecurityConfig
{
    @Bean
    public SecurityFilterChain bitolFvtSecurityFilterChain(HttpSecurity http) throws Exception
    {
        http.csrf(csrf -> csrf.disable())
            .anonymous(anonymous -> anonymous.principal(OMAGPlatformExtension.USER_ID))
            .authorizeHttpRequests(requests -> requests.anyRequest().permitAll());

        return http.build();
    }
}
