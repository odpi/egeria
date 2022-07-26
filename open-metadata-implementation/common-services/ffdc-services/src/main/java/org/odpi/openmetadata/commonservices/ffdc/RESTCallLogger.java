/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc;

import org.slf4j.Logger;

/**
 * RESTCallLogger provides standard logging for REST API method invocations.  It logs
 * the start and end of the call and manages a timer so that the elapsed time of a call is logged.
 */
public class RESTCallLogger
{
    private  Logger  log;
    private  String  serviceName;

    /**
     * Create a REST Call logger for a REST Service.
     *
     * @param log debug logger
     * @param serviceName service name
     */
    public RESTCallLogger(Logger log, String serviceName)
    {
        this.log         = log;
        this.serviceName = serviceName;
    }


    /**
     * Log the start of an inbound REST Call.
     *
     * @param serverName destination server
     * @param userId calling user
     * @param methodName called method
     * @return stop watch measuring the call execution length
     */
    public RESTCallToken logRESTCall(String serverName,
                                     String userId,
                                     String methodName)
    {
        if (log.isDebugEnabled())
        {
            RESTCallToken token = new RESTCallToken(serviceName, serverName, userId, methodName);

            log.debug(token.getRESTCallStartText());

            return token;
        }
        else
        {
            return null;
        }
    }


    /**
     * Log the return of an inbound REST Call.
     *
     * @param token REST call token
     * @param response result of call
     */
    public void logRESTCallReturn(RESTCallToken  token,
                                  String         response)
    {
        if (log.isDebugEnabled())
        {
            if (token != null)
            {
                log.debug(token.getRESTCallReturnText(response));
            }
            else
            {
                log.debug(serviceName + ":" + " returned with response " + response);
            }
        }
    }
}
