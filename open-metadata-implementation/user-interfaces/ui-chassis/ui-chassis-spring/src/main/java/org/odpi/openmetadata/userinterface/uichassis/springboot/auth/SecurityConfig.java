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

public class SecurityConfig extends WebSecurityConfigurerAdapter {

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
                .antMatchers("/").permitAll()
                .antMatchers("/error/**").permitAll()
                .antMatchers("/service-worker.js").permitAll()
                .antMatchers("/manifest.json").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/node_modules/**").permitAll()
                .antMatchers("/src/**").permitAll()
                .antMatchers("/h2/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/themes/**").permitAll()
                .antMatchers("/locales/**").permitAll()
                .antMatchers("/properties/**").permitAll()
                .antMatchers("/open-metadata/ui-admin-services/**").permitAll()
                .antMatchers("/csrf").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new AuthFilter(authService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new LoginFilter("/auth/login", authenticationManager(), authService),
                        UsernamePasswordAuthenticationFilter.class)
                ;
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
}
