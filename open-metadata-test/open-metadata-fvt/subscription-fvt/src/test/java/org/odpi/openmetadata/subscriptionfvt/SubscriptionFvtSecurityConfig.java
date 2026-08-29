/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.subscriptionfvt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * This suite's platform has no user directory configured, so there is nothing to authenticate against.
 * This configuration keeps Spring Security's filter chain in place - the admin, platform and view service
 * REST controllers all require an {@code Authentication} in the security context on every call, even an
 * anonymous one - while permitting every request through unchallenged.
 * <br>
 * The anonymous principal is renamed from Spring's default "anonymousUser" to this suite's own user.  That
 * is not cosmetic: a view service takes the caller's userId from the security context rather than from the
 * request body, so everything the suite does through the Automated Curation API is attributed to whoever
 * this names.  Naming it after the suite means the metadata created by a governance action carries the same
 * {@code createdBy} as the metadata the suite creates directly, and a repository left behind after a run can
 * be read without having to know that "anonymousUser" meant subscription-fvt.
 */
@Configuration
@EnableWebSecurity
public class SubscriptionFvtSecurityConfig
{
    @Bean
    public SecurityFilterChain postgresFvtSecurityFilterChain(HttpSecurity http) throws Exception
    {
        http.csrf(csrf -> csrf.disable())
            .anonymous(anonymous -> anonymous.principal(OMAGPlatformExtension.USER_ID))
            .authorizeHttpRequests(requests -> requests.anyRequest().permitAll());

        return http.build();
    }
}
