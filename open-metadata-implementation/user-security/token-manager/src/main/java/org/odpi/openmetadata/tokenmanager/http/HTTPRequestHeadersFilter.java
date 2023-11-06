/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.tokenmanager.http;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HTTPRequestHeadersFilter is a Java Servlet Filter that receives a list of http header names which are then
 * saved from the request in HTTPHeadersThreadLocal.
 */
public class HTTPRequestHeadersFilter implements Filter
{
    private final List<String> headerNames;
    private final boolean      saveAllHeaders;

    /**
     * Constructor
     *
     * @param headerNames list of http header names
     */
    public HTTPRequestHeadersFilter(List<String> headerNames)
    {
        if (headerNames == null)
        {
            this.headerNames = new ArrayList<>();
            this.saveAllHeaders = false;
        }
        else
        {
            this.headerNames = headerNames;
            this.saveAllHeaders = headerNames.contains("*");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        Filter.super.init(filterConfig);
    }


    /**
     * This call is run during the processing of an HTTP request.  It copies the authorization headers
     * into ThreadLocal storage.
     *
     * @param servletRequest the <code>ServletRequest</code> object contains the client's request
     * @param servletResponse the <code>ServletResponse</code> object contains the filter's response
     * @param filterChain the <code>FilterChain</code> for invoking the next filter or the resource
     * @throws IOException problem accessing the http request
     * @throws ServletException problem with the servlet
     */
    @Override
    public void doFilter(ServletRequest  servletRequest,
                         ServletResponse servletResponse,
                         FilterChain     filterChain) throws IOException,
                                                             ServletException
    {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        Map<String, String> threadLocalHeaders = new HashMap<>();

        if (saveAllHeaders)
        {
            Enumeration<String> allHeaders = httpServletRequest.getHeaderNames();

            if (allHeaders != null)
            {
                while (allHeaders.hasMoreElements())
                {
                    String headerName = allHeaders.nextElement();
                    String headerValue = httpServletRequest.getHeader(headerName);

                    if (headerValue != null && ! headerValue.isEmpty())
                    {
                        threadLocalHeaders.put(headerName, headerValue);
                    }
                }
            }
        }
        else
        {
            for (String headerName : headerNames)
            {
                String headerValue = httpServletRequest.getHeader(headerName);

                if (headerValue != null && ! headerValue.isEmpty())
                {
                    threadLocalHeaders.put(headerName, headerValue);
                }
            }
        }

        HTTPHeadersThreadLocal.getHeadersThreadLocal().set(threadLocalHeaders);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
