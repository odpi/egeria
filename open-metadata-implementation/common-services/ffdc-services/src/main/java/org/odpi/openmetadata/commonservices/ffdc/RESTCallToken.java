/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc;

import org.apache.commons.lang3.time.StopWatch;

/**
 * RESTCallToken provides a cache of information about a single REST API call invocation.
 * It is used for logging and diagnosis.
 */
public class RESTCallToken
{
    static volatile private long nextCallId = 0;
    static final String PLATFORM_NAME = "<*>";

    static private synchronized long getNextCallId() { return nextCallId++; }


    private final StopWatch watch;
    private final long      callId;
    private final String    serviceName;
    private final String    serverName;
    private       String    userId;
    private final String    methodName;

    /**
     * Set up the values that will be used in the logging process.
     *
     * @param serviceName name of service
     * @param serverName name of server (or null if it is a platform request)
     * @param userId calling user
     * @param methodName calling method
     */
    RESTCallToken(String serviceName, String serverName, String userId, String methodName)
    {
        this.serviceName = serviceName;
        this.userId      = userId;
        this.methodName  = methodName;

        this.watch = StopWatch.createStarted();
        this.callId = getNextCallId();

        if (serverName == null)
        {
            this.serverName = PLATFORM_NAME;
        }
        else
        {
            this.serverName = serverName;
        }
    }


    /**
     * Set up the values that will be used in the logging process.
     *
     * @param serviceName name of service
     * @param serverName name of server (or null if it is a platform request)
     * @param methodName calling method
     */
    RESTCallToken(String serviceName, String serverName, String methodName)
    {
        this.serviceName = serviceName;
        this.userId      = null;
        this.methodName  = methodName;

        this.watch = StopWatch.createStarted();
        this.callId = getNextCallId();

        if (serverName == null)
        {
            this.serverName = PLATFORM_NAME;
        }
        else
        {
            this.serverName = serverName;
        }
    }


    /**
     * Allow the userId to be added retrospectively
     *
     * @param userId calling user
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }


    /**
     * Build the start text.
     *
     * @return string
     */
    String getRESTCallStartText()
    {
        return callId + "::" + serviceName + "::" + serverName + "::" + methodName + " call invoked by " + userId;
    }


    /**
     * Build the return text with the response
     *
     * @param response what is the response to build into the return text
     * @return string
     */
    String getRESTCallReturnText(String response)
    {
        return callId + "::" + serviceName + "::" + serverName + "::" + methodName + " call invoked by " + userId + " returned with response " + response + "; Duration: " + watch.getTime()/1000 + "seconds";
    }
}
