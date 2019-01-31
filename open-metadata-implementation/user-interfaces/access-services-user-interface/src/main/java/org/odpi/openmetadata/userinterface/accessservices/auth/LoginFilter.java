/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.InetOrgPerson;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    private TokenAuthService tokenAuthenticationService;

    protected LoginFilter(String urlMapping, AuthenticationManager authenticationManager, TokenAuthService tokenAuthenticationService) {
        super(new AntPathRequestMatcher(urlMapping));
        setAuthenticationManager(authenticationManager);
        this.tokenAuthenticationService = tokenAuthenticationService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        return getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken(
                        request.getParameter("username"), request.getParameter("password")));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) throws IOException, ServletException {

        TokenUser token;
        if (authentication.getPrincipal() instanceof TokenUser) {
            token = (TokenUser) authentication.getPrincipal();
        }
        else {
            token = new TokenUser((InetOrgPerson)authentication.getPrincipal());
        }
        tokenAuthenticationService.addAuthentication(response, token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.getSession().setAttribute("user",token.getUser());
    }
}
