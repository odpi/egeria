/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.digitalbusiness.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.CollectionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.BusinessCapabilityDependencyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.BusinessSignificantProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.BusinessCapabilityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalSupportProperties;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;



/**
 * The DigitalBusinessRESTServices provides the server-side implementation of the Digital Business Open Metadata
 * View Service (OMVS).  This interface provides access to business capabilities.  These are collections so
 * Collection Manager OMVS provides the definition of the business capability element and this service handles the
 * specialized relationships and classification.
 */
public class DigitalBusinessRESTServices extends TokenController
{
    private static final DigitalBusinessInstanceHandler instanceHandler = new DigitalBusinessInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(DigitalBusinessRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public DigitalBusinessRESTServices()
    {
    }


    /**
     * Link dependent business capabilities.
     *
     * @param serverName         name of called server
     * @param businessCapabilityGUID          unique identifier of the business capability that is dependent on another
     * @param supportingBusinessCapabilityGUID  unique identifier of the business capability that is supporting
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkBusinessCapabilityDependency(String                  serverName,
                                                         String                  businessCapabilityGUID,
                                                         String                  supportingBusinessCapabilityGUID,
                                                         NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkBusinessCapabilityDependency";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof BusinessCapabilityDependencyProperties properties)
                {
                    handler.linkBusinessCapabilityDependency(userId,
                                                             businessCapabilityGUID,
                                                             supportingBusinessCapabilityGUID,
                                                             requestBody,
                                                             properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkBusinessCapabilityDependency(userId,
                                                             businessCapabilityGUID,
                                                             supportingBusinessCapabilityGUID,
                                                             requestBody,
                                                             null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(BusinessCapabilityDependencyProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkBusinessCapabilityDependency(userId,
                                                         businessCapabilityGUID,
                                                         supportingBusinessCapabilityGUID,
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
     * Detach dependent business capabilities.
     *
     * @param serverName         name of called server
     * @param businessCapabilityGUID          unique identifier of the business capability that is dependent on another
     * @param supportingBusinessCapabilityGUID  unique identifier of the business capability that is supporting
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachBusinessCapabilityDependency(String                        serverName,
                                                           String                        businessCapabilityGUID,
                                                           String                        supportingBusinessCapabilityGUID,
                                                           DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachBusinessCapabilityDependency";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

            handler.detachBusinessCapabilityDependency(userId, businessCapabilityGUID, supportingBusinessCapabilityGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Attach a business capability to an element that provides digital support.
     *
     * @param serverName         name of called server
     * @param businessCapabilityGUID          unique identifier of the business capability
     * @param elementGUID  unique identifier of the element
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkDigitalSupport(String                     serverName,
                                           String                     businessCapabilityGUID,
                                           String                     elementGUID,
                                           NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkDigitalSupport";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DigitalSupportProperties properties)
                {
                    handler.linkDigitalSupport(userId,
                                               businessCapabilityGUID,
                                               elementGUID,
                                               requestBody,
                                               properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkDigitalSupport(userId,
                                               businessCapabilityGUID,
                                               elementGUID,
                                               requestBody,
                                               null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DigitalSupportProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkDigitalSupport(userId,
                                           businessCapabilityGUID,
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
     * Detach a business capability from an element that provides digital support.
     *
     * @param serverName         name of called server
     * @param businessCapabilityGUID          unique identifier of the business capability
     * @param elementGUID  unique identifier of the element
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachDigitalSupport(String                        serverName,
                                             String                        businessCapabilityGUID,
                                             String                        elementGUID,
                                             DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachDigitalSupport";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

            handler.detachDigitalSupport(userId, businessCapabilityGUID, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Classify an element to indicate that it is significant to a particular business capability.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to update
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse setBusinessSignificant(String                       serverName,
                                               String                       elementGUID,
                                               NewClassificationRequestBody requestBody)
    {
        final String methodName = "setBusinessSignificant";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof BusinessSignificantProperties properties)
                {
                    CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

                    handler.setBusinessSignificant(userId, elementGUID, properties, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(BusinessCapabilityProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
     * Remove the business significant classification from the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to update
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse clearBusinessSignificance(String                          serverName,
                                                  String                          elementGUID,
                                                  DeleteClassificationRequestBody requestBody)
    {
        final String   methodName = "clearBusinessSignificance";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

            handler.clearBusinessSignificance(userId, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
