/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.security.springboot.auth;

import org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.commonservices.multitenant.OMAGServerPlatformInstanceMap;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    private AuthService authenticationService;

    protected LoginFilter(String urlMapping, AuthenticationManager authenticationManager, AuthService authenticationService) {
        super(new AntPathRequestMatcher(urlMapping));
        setAuthenticationManager(authenticationManager);
        this.authenticationService = authenticationService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        String queryString = request.getQueryString();
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        if (queryString == null) {
            // TODO error message properly
            throw new InternalAuthenticationServiceException("ServerName required in the query string, but there was no query string");

        } else {
            int indexOfServerName = queryString.indexOf('=');
            if (indexOfServerName == -1) {
                // TODO error message properly
                throw new InternalAuthenticationServiceException("ServerName not specified in the query string");
            } else {
                String serverName = queryString.substring(indexOfServerName+1);

                OMAGServerPlatformInstanceMap platformInstanceMap = new OMAGServerPlatformInstanceMap();
                try {
                    if (platformInstanceMap.isServerActive(userName, serverName)) {
                        // stash the server name in the response, so subsequent rest calls can use it
                        response.setHeader("egeria-ui-servername", serverName);

                    } else {
                        // TODO error message properly
                        throw new InternalAuthenticationServiceException("ServerName " + serverName + " is not active");
                    }

                } catch (UserNotAuthorizedException e) {
                    // TODO error message properly
                    throw new InsufficientAuthenticationException("User " + userName + " not authorized", e);
                }
            }
        }

        return getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken(userName, password));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication)  {
        authenticationService.addAuthentication(request, response, authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }
}
