/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.auth;

import org.odpi.openmetadata.userinterface.uichassis.springboot.service.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.userdetails.InetOrgPersonContextMapper;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

public abstract class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthService authService;

    @Autowired(required = false)
    TokenClient tokenClient;

    @Autowired
    private ComponentService componentService;

    @Value("${cors.allowed-origins}")
    List<String> allowedOrigins;


    public SecurityConfig() {
        super(true);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors().and()
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
                .logoutSuccessHandler(logoutSuccessHandler())
                .and()
            .addFilterBefore(new AuthFilter(authService), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new LoggingRequestFilter("/api/auth/login"), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(
                    new LoginFilter.LoginFilterBuilder()
                            .url("/api/auth/login")
                            .authManager(authenticationManager())
                            .exceptionHandler(getAuthenticationExceptionHandler())
                            .authService(authService)
                            .appRoles(componentService.getAppRoles())
                            .build(),
                            UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @ConditionalOnProperty(value = "cors.allowed-origins")
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        if( allowedOrigins!=null && allowedOrigins.size() > 0) {
            configuration.setAllowedOrigins(allowedOrigins);
            configuration.setAllowedMethods(Arrays.asList("GET","POST"));
            configuration.addExposedHeader("x-auth-token");
            configuration.setAllowedHeaders(Arrays.asList("content-type","x-auth-token"));
            source.registerCorsConfiguration("/**", configuration);
        }
        return source;
    }

    public LogoutSuccessHandler logoutSuccessHandler() {
        return new TokenLogoutSuccessHandler(tokenClient);
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
