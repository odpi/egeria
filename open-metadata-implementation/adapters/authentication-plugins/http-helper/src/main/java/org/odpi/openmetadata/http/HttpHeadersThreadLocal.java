/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.http;

import java.util.Map;

/**
 * HttpHeadersThreadLocal keeps ThreadLocal HTTP headers to be available for request thread.
 */
public class HttpHeadersThreadLocal {

    /**
     * HEADERS_THREAD_LOCAL is a ThreadLocal that holds a map of headers and their value
     */
    private static ThreadLocal<Map<String, String>> HEADERS_THREAD_LOCAL = new ThreadLocal<>();

    public static ThreadLocal<Map<String, String>> getHeadersThreadLocal() {
        return HEADERS_THREAD_LOCAL;
    }
}
