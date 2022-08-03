/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOG = LoggerFactory.getLogger(HttpRequestHeadersFilter.class);
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
        LOG.debug(
                "Reading request headers from : {}.",
                req.getRequestURI());

        Map<String, String> threadLocalHeaders = new HashMap<>();

        for (String headerName : headerNames) {
            String headerValue = req.getHeader(headerName);
            LOG.debug("Got header {} with value {}.", headerName, headerValue);
            threadLocalHeaders.put(headerName, headerValue);
        }

        HttpHeadersThreadLocal.getHeadersThreadLocal().set(threadLocalHeaders);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
