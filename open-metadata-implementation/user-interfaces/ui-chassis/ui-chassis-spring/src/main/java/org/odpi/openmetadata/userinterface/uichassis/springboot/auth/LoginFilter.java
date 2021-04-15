/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.auth;

import org.odpi.openmetadata.userinterface.uichassis.springboot.service.ComponentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private final AuthService authenticationService;
    private final AuthenticationExceptionHandler authenticationExceptionHandler;
    private final Set<String> appRoles;


    Logger log = LoggerFactory.getLogger(this.getClass());

    private LoginFilter(LoginFilterBuilder builder){
        super(new AntPathRequestMatcher(builder.urlMapping));
        setAuthenticationManager(builder.authenticationManager);
        this.authenticationService = builder.authService;
        this.authenticationExceptionHandler = builder.authenticationExceptionHandler;
        this.appRoles = builder.appRoles;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        String username = request.getParameter(USERNAME);
        String password = request.getParameter(PASSWORD);
        Authentication authentication =  getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken( username, password));
        return authentication;
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        log.info("UNSUCCESSFUL Authentication");
        if( authenticationExceptionHandler.isBadCredentials(failed) ) {
            log.warn("Bad credentials for user: {}", request.getParameter(USERNAME));
            response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        } else {
            log.warn("ERROR AUTHENTICATION for user: {}", request.getParameter(USERNAME), failed);
            response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) throws IOException {
        log.info("SUCCESSFUL Authentication for user {}", request.getParameter(USERNAME));
        authenticationService.addAuthentication(request, response, authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        if( !checkRoles(authentication) || authentication.getAuthorities().isEmpty() ){
            log.warn("NO roles for user: {}", request.getParameter(USERNAME));
            response.sendError(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase());
        }
    }

    private boolean checkRoles(Authentication authentication){
       return  authentication.getAuthorities().stream()
                .map(r -> r.getAuthority())
                .anyMatch(appRoles::contains);
    }

    public static class LoginFilterBuilder{
        private String urlMapping;
        private AuthenticationManager authenticationManager;
        private AuthService authService;
        private AuthenticationExceptionHandler authenticationExceptionHandler;
        private Set<String> appRoles;

        public LoginFilterBuilder url(String urlMapping){
            this.urlMapping = urlMapping;
            return this;
        }

        public LoginFilterBuilder authManager(AuthenticationManager authenticationManager){
            this.authenticationManager = authenticationManager;
            return this;
        }

        public LoginFilterBuilder authService(AuthService authService){
            this.authService = authService;
            return this;
        }

        public LoginFilterBuilder exceptionHandler(AuthenticationExceptionHandler authenticationExceptionHandler){
            this.authenticationExceptionHandler = authenticationExceptionHandler;
            return this;
        }

        public LoginFilterBuilder appRoles(Set<String> appRoles){
            this.appRoles = appRoles;
            return this;
        }

        public LoginFilter build(){
            return new LoginFilter(this);
        }
    }
}
