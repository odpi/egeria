/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.notificationmanager.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.GovernanceDefinitionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.MonitoredResourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.NotificationSubscriberProperties;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;



/**
 * The NotificationManagerRESTServices provides the server-side implementation of the Notification Manager Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class NotificationManagerRESTServices extends TokenController
{
    private static final NotificationManagerInstanceHandler instanceHandler = new NotificationManagerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(NotificationManagerRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public NotificationManagerRESTServices()
    {
    }




    /**
     * Attach a monitored resource to a notification type.
     *
     * @param serverName         name of called server
     * @param notificationTypeGUID    unique identifier of the parent subject area.
     * @param elementGUID    unique identifier of the nested subject area.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkMonitoredResource(String                  serverName,
                                              String                  notificationTypeGUID,
                                              String                  elementGUID,
                                              NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkMonitoredResource";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof MonitoredResourceProperties monitoredResourceProperties)
                {
                    handler.linkMonitoredResource(userId,
                                                  notificationTypeGUID,
                                                  elementGUID,
                                                  requestBody,
                                                  monitoredResourceProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkMonitoredResource(userId,
                                                  notificationTypeGUID,
                                                  elementGUID,
                                                  requestBody,
                                                  null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(MonitoredResourceProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkMonitoredResource(userId,
                                              notificationTypeGUID,
                                              elementGUID,
                                              null,
                                              null);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Detach a monitored resource from a notification type.
     *
     * @param serverName         name of called server
     * @param notificationTypeGUID    unique identifier of the parent subject area.
     * @param elementGUID    unique identifier of the nested subject area.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachMonitoredResource(String                        serverName,
                                                String                        notificationTypeGUID,
                                                String                        elementGUID,
                                                DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachMonitoredResource";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

            handler.detachMonitoredResource(userId, notificationTypeGUID, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }




    /**
     * Attach subscriber to a notification type.
     *
     * @param serverName         name of called server
     * @param notificationTypeGUID    unique identifier of the parent subject area.
     * @param elementGUID    unique identifier of the nested subject area.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkNotificationSubscriber(String                  serverName,
                                                   String                  notificationTypeGUID,
                                                   String                  elementGUID,
                                                   NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkNotificationSubscriber";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof NotificationSubscriberProperties notificationSubscriberProperties)
                {
                    handler.linkNotificationSubscriber(userId,
                                                       notificationTypeGUID,
                                                       elementGUID,
                                                       requestBody,
                                                       notificationSubscriberProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkNotificationSubscriber(userId,
                                                       notificationTypeGUID,
                                                       elementGUID,
                                                       requestBody,
                                                       null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(NotificationSubscriberProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkNotificationSubscriber(userId,
                                                   notificationTypeGUID,
                                                   elementGUID,
                                                   null,
                                                   null);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Detach a subscriber from a notification type.
     *
     * @param serverName         name of called server
     * @param notificationTypeGUID    unique identifier of the parent subject area.
     * @param elementGUID    unique identifier of the nested subject area.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachNotificationSubscriber(String                        serverName,
                                                     String                        notificationTypeGUID,
                                                     String                        elementGUID,
                                                     DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachNotificationSubscriber";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

            handler.detachNotificationSubscriber(userId, notificationTypeGUID, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


}
