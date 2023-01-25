/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.http;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HttpRequestHeadersFilter is a Java Servlet Filter that receives a list of http header names which are then
 * saved from the request in HttpHeadersThreadLocal
 */
public class HttpRequestHeadersFilter implements Filter {
    private List<String> headerNames;

    /**
     * @param headerNames list of http header names
     */
    public HttpRequestHeadersFilter(List<String> headerNames) {
        this.headerNames = headerNames;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;

        Map<String, String> threadLocalHeaders = new HashMap<>();

        for (String headerName : headerNames) {
            String headerValue = req.getHeader(headerName);

            if (headerValue != null && !headerValue.isEmpty()) {
                threadLocalHeaders.put(headerName, headerValue);
            }
        }

        HttpHeadersThreadLocal.getHeadersThreadLocal().set(threadLocalHeaders);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
