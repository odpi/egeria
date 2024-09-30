/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.tokenmanager.http;

import java.util.Map;

/**
 * HTTPHeadersThreadLocal keeps ThreadLocal HTTP headers to be available for request thread.
 */
public class HTTPHeadersThreadLocal
{

    /**
     * HEADERS_THREAD_LOCAL is a ThreadLocal that holds a map of headers and their value
     */
    private static final ThreadLocal<Map<String, String>> HEADERS_THREAD_LOCAL = new ThreadLocal<>();


    /**
     * Return the header values stored in thread local storage.
     *
     * @return thread local map
     */
    public static ThreadLocal<Map<String, String>> getHeadersThreadLocal() {
        return HEADERS_THREAD_LOCAL;
    }
}
