/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class AuthFilter extends GenericFilterBean {

    private static final Logger LOG = LoggerFactory.getLogger(AuthFilter.class);

    private final AuthService authService;

    public AuthFilter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            Authentication authentication = authService.getAuthentication((HttpServletRequest) request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (ExpiredJwtException e){
            LOG.error("TOKEN EXPIRED", e.getMessage());
            SecurityContextHolder.getContext().setAuthentication(null);
        }catch (JwtException e){
            LOG.debug("Token error", e.getMessage());
            SecurityContextHolder.getContext().setAuthentication(null);
        }catch (Exception e){
            LOG.error("Authentication exception", e);
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        filterChain.doFilter(request, response);
        SecurityContextHolder.getContext().setAuthentication(null);

    }
}
