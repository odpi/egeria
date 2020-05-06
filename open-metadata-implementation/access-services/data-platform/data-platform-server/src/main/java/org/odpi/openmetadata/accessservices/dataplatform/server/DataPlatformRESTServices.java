/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.ConnectionResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import org.slf4j.LoggerFactory;


/**
 * The DiscoveryConfigurationServices provides the server-side implementation of the configuration services
 * from the Open Discovery Framework (ODF).  These services configure discovery engines and discovery services.
 */
public class DataPlatformRESTServices
{
    private static DataPlatformInstanceHandler instanceHandler = new DataPlatformInstanceHandler();

    private static RESTCallLogger       restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(DataPlatformRESTServices.class),
                                                                                  instanceHandler.getServiceName());
    private    RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public DataPlatformRESTServices()
    {
    }


    /**
     * Return the connection object for the Data Platform OMAS's out topic.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     *
     * @return connection object for the out topic or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the discovery engine definition.
     */
    public ConnectionResponse getOutTopicConnection(String serverName,
                                                    String userId)
    {
        final String        methodName = "getOutTopicConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectionResponse response = new ConnectionResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setConnection(instanceHandler.getOutTopicConnection(userId, serverName, methodName));
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
