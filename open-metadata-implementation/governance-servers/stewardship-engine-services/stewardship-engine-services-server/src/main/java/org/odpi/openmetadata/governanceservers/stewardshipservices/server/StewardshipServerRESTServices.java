/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.stewardshipservices.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;

import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.stewardshipengineservices.rest.StewardshipEngineStatusResponse;
import org.odpi.openmetadata.governanceservers.stewardshipservices.handlers.StewardshipEngineHandler;
import org.slf4j.LoggerFactory;


/**
 * StewardshipServerRESTServices provides the external service implementation for a stewardship engine.
 * Each method contains the stewardship server name and the stewardship engine identifier (guid).
 * The StewardshipServerRESTServices locates the correct stewardship engine instance within the correct
 * stewardship server instance and delegates the request.
 */
public class StewardshipServerRESTServices
{
    private static StewardshipServerInstanceHandler instanceHandler = new StewardshipServerInstanceHandler();

    private static RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(StewardshipServerRESTServices.class),
                                                                      instanceHandler.getServiceName());
    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();


    /**
     * Request that the stewardship engine refresh its configuration by calling the metadata server.
     * This request is useful if the metadata server has an outage, particularly while the
     * stewardship server is initializing.  This request just ensures that the latest configuration
     * is in use.
     *
     * @param serverName name of the stewardship server.
     * @param stewardshipEngineName unique name of the stewardship engine.
     * @param userId identifier of calling user
     *
     * @return void or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  StewardshipEngineException there was a problem detected by the stewardship engine.
     */
    public  VoidResponse refreshConfig(String                       serverName,
                                       String                       stewardshipEngineName,
                                       String                       userId)
    {
        final String        methodName = "refreshConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            StewardshipEngineHandler handler = instanceHandler.getStewardshipEngineHandler(userId,
                                                                                           serverName,
                                                                                           stewardshipEngineName,
                                                                                           methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.refreshConfig();
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return a summary of each of the stewardship engines' status.
     *
     * @param serverName stewardship server name
     * @param userId calling user
     * @return list of statuses - on for each assigned stewardship engines or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     */
    public StewardshipEngineStatusResponse getStewardshipEngineStatuses(String   serverName,
                                                                        String   userId)
    {
        final String methodName = "getStewardshipEngineStatuses";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        StewardshipEngineStatusResponse response = new StewardshipEngineStatusResponse();
        AuditLog                      auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setStewardshipEngineSummaries(instanceHandler.getStewardshipEngineStatuses(userId,
                                                                                       serverName,
                                                                                       methodName));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
