/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * This suite's platform has no user directory, so there is nothing to authenticate against.  The filter
 * chain stays in place - the REST controllers require an {@code Authentication} on every call - and permits
 * every request.
 * <br>
 * The anonymous principal is renamed to the suite's user.  A view service takes the caller from the security
 * context, so this is the user every request runs as - and, for My Profile, whose profile "my" profile is.
 */
@Configuration
@EnableWebSecurity
public class ViewServicesFvtSecurityConfig
{
    @Bean
    public SecurityFilterChain viewServicesFvtSecurityFilterChain(HttpSecurity http) throws Exception
    {
        http.csrf(csrf -> csrf.disable())
            .anonymous(anonymous -> anonymous.principal(OMAGPlatformExtension.USER_ID))
            .authorizeHttpRequests(requests -> requests.anyRequest().permitAll());

        return http.build();
    }
}
