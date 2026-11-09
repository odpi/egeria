/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.requestid.ActiveRequests;
import org.odpi.openmetadata.frameworks.auditlog.requestid.RequestId;
import org.odpi.openmetadata.frameworks.auditlog.requestid.RequestIdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RESTCallLogger provides standard logging for REST API method invocations.  It logs
 * the start and end of the call and manages a timer so that the elapsed time of a call is logged.
 */
public class RESTCallLogger
{
    private static final RequestId requestId = new RequestId();

    private final static Logger myLog = LoggerFactory.getLogger(RESTCallLogger.class);

    private final Logger log;
    private final String serviceName;

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

        if (myLog.isDebugEnabled())
        {
            myLog.debug("New RESTCallLogger registered for service: {}, use logging.level.{}=DEBUG", serviceName, log.getName());
        }
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
        requestId.setRequestId(null);

        ActiveRequests.beginRequest(requestId.getRequestId());

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
     * Log the start of an inbound REST Call.
     *
     * @param serverName destination server
     * @param userId     calling user
     * @param methodName called method
     * @return stop watch measuring the call execution length
     */
    public RESTCallToken logRESTCall(String           serverName,
                                     String           userId,
                                     String           methodName,
                                     RequestIdService requestIdService)
    {
        if (requestIdService != null)
        {
            requestId.setRequestId(requestIdService.getRequestId());
        }
        else
        {
            requestId.setRequestId(null);
        }

        ActiveRequests.beginRequest(requestId.getRequestId());

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
     * Log the start of an inbound REST Call.
     *
     * @param serverName destination server
     * @param methodName called method
     * @return stop watch measuring the call execution length
     */
    public RESTCallToken logRESTCall(String serverName,
                                     String methodName)
    {
        return this.logRESTCall(serverName,  methodName, (String) null);
    }


    /**
     * Log the start of an inbound REST Call.
     *
     * @param serverName destination server
     * @param methodName called method
     * @return stop watch measuring the call execution length
     */
    public RESTCallToken logRESTCall(String           serverName,
                                     String           methodName,
                                     RequestIdService requestIdService)
    {
        if (requestIdService != null)
        {
            requestId.setRequestId(requestIdService.getRequestId());
        }
        else
        {
            requestId.clearRequestId();
        }

        ActiveRequests.beginRequest(requestId.getRequestId());

        if (log.isDebugEnabled())
        {
            RESTCallToken token = new RESTCallToken(serviceName, serverName, methodName);

            log.debug(token.getRESTCallStartText());

            return token;
        }
        else
        {
            return null;
        }
    }


    /**
     * Allow the userId to be added retrospectively
     *
     * @param token REST call token
     * @param userId calling user
     */
    public void setUserId(RESTCallToken  token,
                          String         userId)
    {
        if (log.isDebugEnabled())
        {
            if (token != null)
            {
                token.setUserId(userId);
            }
        }
    }


    /**
     * Allow an external caller to set up the request Id.
     *
     * @param requestIdValue REST call supplied value
     */
    public void setRequestId(String requestIdValue)
    {
        requestId.setRequestId(requestIdValue);
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
                log.debug(serviceName + "::" + " returned with response " + response);
            }
        }

        ActiveRequests.endRequest();
        requestId.clearRequestId();
    }


    /**
     * Log the return of an inbound REST Call.
     *
     * @param token REST call token
     * @param response result of call
     */
    public void logRESTCallReturn(RESTCallToken    token,
                                  RequestIdService response)
    {
        if (response == null)
        {
            if (log.isDebugEnabled())
            {
                if (token != null)
                {
                    log.debug(token.getRESTCallReturnText(null));
                }
                else
                {
                    log.debug(serviceName + "::" + " returned with no response");
                }
            }
        }
        else
        {
            if (log.isDebugEnabled())
            {
                if (token != null)
                {
                    log.debug(token.getRESTCallReturnText(response.toString()));
                }
                else
                {
                    log.debug(serviceName + "::" + " returned with response " + response);
                }
            }

            response.setRequestId(requestId.getRequestId());
        }

        ActiveRequests.endRequest();
        requestId.clearRequestId();
    }
}
