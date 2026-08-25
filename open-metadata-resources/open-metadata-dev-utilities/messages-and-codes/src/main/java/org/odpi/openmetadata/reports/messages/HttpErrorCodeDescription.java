/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.reports.messages;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * HttpErrorCodeDescription supplies the meaning of the HTTP status codes that Egeria's exception messages use.
 * The status code allows an exception to be passed faithfully across a REST API call and rebuilt by the client.
 */
class HttpErrorCodeDescription
{
    private static final Map<Integer, String> HTTP_ERROR_CODES = new LinkedHashMap<>();

    static
    {
        HTTP_ERROR_CODES.put(400, "Bad Request - the caller has supplied invalid parameters");
        HTTP_ERROR_CODES.put(401, "Unauthorized - the caller is not authenticated");
        HTTP_ERROR_CODES.put(403, "Forbidden - the caller is not authorized to perform this request");
        HTTP_ERROR_CODES.put(404, "Not Found - the requested element does not exist");
        HTTP_ERROR_CODES.put(405, "Method Not Allowed - this operation is not supported for this element");
        HTTP_ERROR_CODES.put(409, "Conflict - the request clashes with the current state of the metadata");
        HTTP_ERROR_CODES.put(410, "Gone - the requested element has been deleted");
        HTTP_ERROR_CODES.put(422, "Unprocessable Content - the request is understood but cannot be carried out");
        HTTP_ERROR_CODES.put(500, "Internal Server Error - an unexpected error occurred inside Egeria");
        HTTP_ERROR_CODES.put(501, "Not Implemented - this function is not implemented by the called component");
        HTTP_ERROR_CODES.put(503, "Service Unavailable - the service needed to process the request is not running");
    }


    /**
     * Private constructor to prevent instantiation of this static class.
     */
    private HttpErrorCodeDescription()
    {
    }


    /**
     * Return the meaning of an HTTP status code.
     *
     * @param httpErrorCode status code from a message definition
     * @return description - an empty string if the status code is not one that Egeria normally uses
     */
    static String getDescription(int httpErrorCode)
    {
        return HTTP_ERROR_CODES.getOrDefault(httpErrorCode, "");
    }


    /**
     * Return all of the HTTP status codes and their meanings, so that they can be listed in the documentation.
     *
     * @return map of status code to description, in ascending order
     */
    static Map<Integer, String> getAllDescriptions()
    {
        return HTTP_ERROR_CODES;
    }
}
