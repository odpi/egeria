/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.InvalidSearchFilterException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
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

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private final AuthService authenticationService;

    Logger log = LoggerFactory.getLogger(this.getClass());

    protected LoginFilter(String urlMapping, AuthenticationManager authenticationManager, AuthService authenticationService) {
        super(new AntPathRequestMatcher(urlMapping));
        setAuthenticationManager(authenticationManager);
        this.authenticationService = authenticationService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException,IOException {

        String username = request.getParameter(USERNAME);
        String password = request.getParameter(PASSWORD);
        Authentication authentication =  getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken( username, password));

        if(authentication.getAuthorities().isEmpty()){
            log.warn("NO roles for user: {}", request.getParameter(USERNAME));
            response.sendError(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase());
        }
        return authentication;
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        log.info("Unsuccessful Authentication");
        if(failed instanceof BadCredentialsException || failed.getCause() instanceof InvalidSearchFilterException) {
            log.warn("Bad credentials UNSUCCESSFUL AUTHENTICATION for user: {}", request.getParameter(USERNAME));
            response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        } else {
            log.warn("ERROR AUTHENTICATION for user: {}", request.getParameter(USERNAME), failed);
            response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication)  {
        log.info("Successful Authentication");
        authenticationService.addAuthentication(request, response, authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }
}
