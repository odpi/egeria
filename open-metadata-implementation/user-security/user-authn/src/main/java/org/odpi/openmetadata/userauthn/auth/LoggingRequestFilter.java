/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userauthn.auth;

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


/**
 * LoggingRequestFilter is setting up additional diagnostics using the Mapped Diagnostic Context (MDC) service.
 * It inherits from Filter which is an object that performs filtering tasks on either the request to a resource
 * (a servlet or static content), or on the response from a resource, or both.
 */
public class LoggingRequestFilter implements Filter
{
    private String path;


    /**
     * Constructor.
     *
     * @param path path to provide filtering on
     */
    public LoggingRequestFilter(String path) {
        this.path = path;
    }


    /**
     * Called by the web container to indicate to a filter that it is being placed into service.
     * The servlet container calls the init method exactly once after instantiating the filter.
     * The init method must complete successfully before the filter is asked to do any filtering work.
     *
     * @param filterConfig The configuration information associated with the filter instance being initialised
     *
     * @throws ServletException problem with servlet
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {

    }


    /**
     * Request to provide filtering. The doFilter method of the Filter interface is called by the container each time
     * a request/response pair is passed through the chain due to a client request for a resource at the end of the chain.
     * The FilterChain passed in to this method allows the Filter to pass on the request and response to the next entity in the chain.
     *
     * @param servletRequest  The request to process
     * @param servletResponse The response associated with the request
     * @param filterChain    Provides access to the next filter in the chain for this filter to pass the request and response
     *                     to for further processing
     *
     * @throws IOException  if an I/O error occurs during this filter's processing of the request
     * @throws ServletException if the processing fails for any other reason
     */
    @Override
    public void doFilter(ServletRequest  servletRequest,
                         ServletResponse servletResponse,
                         FilterChain     filterChain) throws IOException, ServletException
    {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String             requestURI = httpRequest.getRequestURI();

        if (! path.equals(requestURI))
        {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        MDC.put("remoteAddress", httpRequest.getRemoteAddr());
        MDC.put("remoteHost", httpRequest.getRemoteHost());
        MDC.put("userId", httpRequest.getParameter("userId"));
        MDC.put("sessionId", httpRequest.getSession().getId());

        Collections.list(httpRequest.getHeaderNames()).forEach(name -> MDC.put(name, httpRequest.getHeader(name)));
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        MDC.clear();
    }
}
