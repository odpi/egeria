/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc;

import org.apache.commons.lang3.time.StopWatch;

/**
 * RESTCallToken provides a cache of information about a single REST API call invocation.
 * It is used for logging and diagnosis.
 */
public class RESTCallToken
{
    static private long nextCallId = 0;

    static private synchronized long getNextCallId() { return nextCallId++; }

    private StopWatch watch;
    private long      callId;
    private String    serviceName;
    private String    serverName;
    private String    userId;
    private String    methodName;

    RESTCallToken(String serviceName, String serverName, String userId, String methodName)
    {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.userId     = userId;
        this.methodName = methodName;

        this.watch = StopWatch.createStarted();
        this.callId = getNextCallId();
    }

    String getRESTCallStartText()
    {
        return callId + ":" + serviceName + ":" + serverName + ":" + methodName + " call invoked by " + userId;
    }

    String getRESTCallReturnText(String response)
    {
        return callId + ":" + serviceName + ":" + serverName + ":" + methodName + " call invoked by " + userId + " returned with response " + response + "; Duration: " + watch.getTime()/1000 + "seconds";
    }
}
