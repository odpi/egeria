/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;


import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;

import java.util.regex.Pattern;

/**
 * OMVSServiceInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the ViewServiceAdmin class.
 */
public class OMVSServiceInstanceHandler extends AuditableServerServiceInstanceHandler
{
    private final RESTExceptionHandler exceptionHandler = new RESTExceptionHandler();


    /**
     * Constructor
     *
     * @param serviceName a descriptive name for the OMVS
     */
    public OMVSServiceInstanceHandler(String   serviceName)
    {
        super(serviceName);
    }

    /**
     * Retrieve the exception handler that can package up common exceptions and pack them into
     * a REST Response.
     *
     * @return exception handler object
     */
    public RESTExceptionHandler getExceptionHandler() { return exceptionHandler; }




    /**
     * Set the provided search string to be interpreted as either case-insensitive or case-sensitive.
     *
     * @param searchString the string to set as case-insensitive
     * @param insensitive if true, set the string to be case-insensitive, otherwise leave as case-sensitive
     * @return string ensuring the provided searchString is case-(in)sensitive
     */
    private String setInsensitive(String searchString, boolean insensitive)
    {
        return insensitive ? "(?i)" + searchString : searchString;
    }


    /**
     * Retrieve an escaped version of the provided string that can be passed to methods that expect regular expressions,
     * to search for the string with a "starts with" semantic. The passed string will NOT be treated as a regular expression;
     * if you intend to use both a "starts with" semantic and a regular expression within the string, simply construct your
     * own regular expression directly (not with this helper method).
     *
     * @param searchString the string to escape to avoid being interpreted as a regular expression, but also wrap to obtain a "starts with" semantic
     * @param insensitive set to true to have a case-insensitive "starts with" regular expression
     * @return string that is interpreted literally, wrapped for a "starts with" semantic
     */
    public String getStartsWithRegex(String searchString, boolean insensitive)
    {
        return searchString == null ? null : setInsensitive(getExactMatchRegex(searchString, false) + ".*", insensitive);
    }


    /**
     * Retrieve an escaped version of the provided string that can be passed to methods that expect regular expressions,
     * to search for the string with an "ends with" semantic. The passed string will NOT be treated as a regular expression;
     * if you intend to use both an "ends with" semantic and a regular expression within the string, simply construct your
     * own regular expression directly (not with this helper method).
     *
     * @param searchString the string to escape to avoid being interpreted as a regular expression, but also wrap to obtain an "ends with" semantic
     * @param insensitive set to true to have a case-insensitive "ends with" regular expression
     * @return string that is interpreted literally, wrapped for an "ends with" semantic
     */
    public String getEndsWithRegex(String searchString, boolean insensitive)
    {
        return searchString == null ? null : setInsensitive(".*" + getExactMatchRegex(searchString, false), insensitive);
    }



    /**
     * Retrieve an escaped version of the provided string that can be passed to methods that expect regular expressions,
     * without being interpreted as a regular expression (i.e. the returned string will be interpreted as a literal --
     * used to find an exact match of the string, irrespective of whether it contains characters that may have special
     * meanings to regular expressions).
     *
     * @param searchString the string to escape to avoid being interpreted as a regular expression
     * @param insensitive set to true to have a case-insensitive exact match regular expression
     * @return string that is interpreted literally rather than as a regular expression
     */
    String getExactMatchRegex(String searchString, boolean insensitive)
    {
        return searchString == null ? null : setInsensitive(Pattern.quote(searchString), insensitive);
    }


    /**
     * Construct a regular expression from the string supplied by the caller.  If their string includes regular expression characters then
     * they will be ignored.
     *
     * @param requestedSearch the supplied string
     * @param startsWith set to true if the requested string is at the front of the search
     * @param endsWith set to true if the requested string is at the end of the search
     * @param ignoreCase set to true to have a case-insensitive search
     * @return string that is interpreted literally rather than as a regular expression
     */
    public String getSearchString(String requestedSearch, boolean startsWith, boolean endsWith, boolean ignoreCase)
    {
        if ((requestedSearch == null) || ("".equals(requestedSearch.trim())))
        {
            // ignore the flags for an empty search criteria string - assume we want everything
            requestedSearch = ".*";
        }
        else
        {
            // lose any leading and trailing blanks
            requestedSearch = requestedSearch.trim();

            if (startsWith && endsWith)
            {
                requestedSearch = this.getExactMatchRegex(requestedSearch, ignoreCase);
            }
            else if (startsWith)
            {
                requestedSearch = this.getStartsWithRegex(requestedSearch, ignoreCase);
            }
            else if (endsWith)
            {
                requestedSearch = this.getEndsWithRegex(requestedSearch, ignoreCase);
            }
            else
            {
                requestedSearch = this.getStartsWithRegex(this.getEndsWithRegex(requestedSearch, false), ignoreCase);
            }
        }

        return requestedSearch;
    }

}
