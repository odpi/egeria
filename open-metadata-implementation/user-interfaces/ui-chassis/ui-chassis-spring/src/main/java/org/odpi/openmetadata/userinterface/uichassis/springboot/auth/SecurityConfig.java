/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.userdetails.InetOrgPersonContextMapper;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public abstract class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthService authService;

    public SecurityConfig() {
        super(true);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .exceptionHandling().and()
            .anonymous().and()
            .authorizeRequests()
                .antMatchers("/logout").permitAll()
                .antMatchers("/api/public/**").permitAll()
                .antMatchers("/api/public/css/**").permitAll()
                .antMatchers("/api/public/js/**").permitAll()
            .antMatchers("/api/**").authenticated()
            .antMatchers("/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                .logoutSuccessHandler(logoutSuccessHandler())
                .logoutSuccessUrl("/login?logoutSuccessful")
                .and()
            .addFilterBefore(new AuthFilter(authService), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new LoggingRequestFilter("/api/auth/login"), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(
                    new LoginFilter("/api/auth/login",
                            authenticationManager(),
                            authService,
                            getAuthenticationExceptionHandler()),UsernamePasswordAuthenticationFilter.class)
        ;
    }

    public LogoutSuccessHandler logoutSuccessHandler() {
        return new TokenLogoutSuccessHandler();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public InetOrgPersonContextMapper userContextMapper() {
        return new InetOrgPersonContextMapper();
    }

    protected abstract AuthenticationExceptionHandler getAuthenticationExceptionHandler();
}
