/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private AuthService authenticationService;

    protected LoginFilter(String urlMapping, AuthenticationManager authenticationManager, AuthService authenticationService) {
        super(new AntPathRequestMatcher(urlMapping));
        setAuthenticationManager(authenticationManager);
        this.authenticationService = authenticationService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        return getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken(
                        request.getParameter("username"), request.getParameter("password")));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
        if(failed instanceof BadCredentialsException){
            log.warn("Bad credentials UNSUCCESSFUL AUTHENTICATION for user: {}", request.getParameter("username"));
        }else{
            log.warn("UNSUCCESSFUL AUTHENTICATION for user: {}", request.getParameter("username"), failed);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication)  {
        authenticationService.addAuthentication(request, response, authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }
}
