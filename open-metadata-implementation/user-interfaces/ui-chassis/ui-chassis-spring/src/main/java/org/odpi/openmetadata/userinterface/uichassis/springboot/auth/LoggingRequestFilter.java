/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.auth;

import org.slf4j.MDC;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;

public class LoggingRequestFilter implements Filter {
    private String path;

    public LoggingRequestFilter(String path) {
        this.path = path;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpRequest.getRequestURI();
        if (!path.equals(requestURI)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        MDC.put("remoteAddress", httpRequest.getRemoteAddr());
        MDC.put("remoteHost", httpRequest.getRemoteHost());
        MDC.put("username", httpRequest.getParameter("username"));
        MDC.put("sessionId", httpRequest.getSession().getId());

        Collections.list(httpRequest.getHeaderNames()).forEach(name -> MDC.put(name, httpRequest.getHeader(name)));
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        MDC.clear();
    }
}
