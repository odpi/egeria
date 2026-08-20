/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.productmanager.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.CollectionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.AssignmentScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductDependencyProperties;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;



/**
 * The ProductManagerRESTServices provides the server-side implementation of the Product Manager Open Metadata
 * View Service (OMVS).  This interface provides access to digital products and digital product families.
 */
public class ProductManagerRESTServices extends TokenController
{
    private static final ProductManagerInstanceHandler instanceHandler = new ProductManagerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(ProductManagerRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public ProductManagerRESTServices()
    {
    }


    /**
     * Link two dependent products.
     *
     * @param serverName         name of called server
     * @param consumerDigitalProductGUID    unique identifier of the digital product that has the dependency.
     * @param consumedDigitalProductGUID    unique identifier of the digital product that it is using.
     * @param requestBody  description of the relationship.
     *
     * @return unique identifier of the new relationship or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse linkDigitalProductDependency(String                     serverName,
                                                     String                     consumerDigitalProductGUID,
                                                     String                     consumedDigitalProductGUID,
                                                     NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkDigitalProductDependency";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DigitalProductDependencyProperties properties)
                {
                    response.setGUID(handler.linkDigitalProductDependency(userId,
                                                         consumerDigitalProductGUID,
                                                         consumedDigitalProductGUID,
                                                         requestBody,
                                                         properties));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setGUID(handler.linkDigitalProductDependency(userId,
                                                         consumerDigitalProductGUID,
                                                         consumedDigitalProductGUID,
                                                         requestBody,
                                                         null));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DigitalProductDependencyProperties.class.getName(), methodName);
                }
            }
            else
            {
                response.setGUID(handler.linkDigitalProductDependency(userId,
                                                     consumerDigitalProductGUID,
                                                     consumedDigitalProductGUID,
                                                     null,
                                                     null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Update the properties of a digital product dependency relationship.
     *
     * @param serverName name of the server to route the request to
     * @param digitalProductDependencyRelationshipGUID unique identifier of the relationship
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse updateDigitalProductDependency(String                        serverName,
                                                       String                        digitalProductDependencyRelationshipGUID,
                                                       UpdateRelationshipRequestBody requestBody)
    {
        final String methodName = "updateDigitalProductDependency";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                if (requestBody.getProperties() instanceof DigitalProductDependencyProperties properties)
                {
                    handler.updateDigitalProductDependency(userId, digitalProductDependencyRelationshipGUID, requestBody, properties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DigitalProductDependencyProperties.class.getName(), methodName);
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Remove a digital product dependency relationship.
     *
     * @param serverName name of the server to route the request to
     * @param digitalProductDependencyRelationshipGUID unique identifier of the relationship
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachDigitalProductDependency(String                        serverName,
                                                       String                        digitalProductDependencyRelationshipGUID,
                                                       DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachDigitalProductDependency";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

            handler.detachDigitalProductDependency(userId, digitalProductDependencyRelationshipGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Attach a product manager to a digital product.
     *
     * @param serverName         name of called server
     * @param digitalProductGUID  unique identifier of the digital product
     * @param digitalProductManagerRoleGUID      unique identifier of the product manager role
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkProductManager(String                     serverName,
                                           String                     digitalProductGUID,
                                           String                     digitalProductManagerRoleGUID,
                                           NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkProductManager";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                if (requestBody.getProperties() instanceof AssignmentScopeProperties properties)
                {
                    handler.linkProductManager(userId,
                                               digitalProductGUID,
                                               digitalProductManagerRoleGUID,
                                               requestBody,
                                               properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkProductManager(userId,
                                               digitalProductGUID,
                                               digitalProductManagerRoleGUID,
                                               requestBody,
                                               null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(AssignmentScopeProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkProductManager(userId,
                                           digitalProductGUID,
                                           digitalProductManagerRoleGUID,
                                           requestBody,
                                           null);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Detach a product manager from a digital product.
     *
     * @param serverName         name of called server
     * @param digitalProductGUID  unique identifier of the digital product
     * @param digitalProductManagerRoleGUID      unique identifier of the product manager role
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachProductManager(String                        serverName,
                                             String                        digitalProductGUID,
                                             String                        digitalProductManagerRoleGUID,
                                             DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachProductManager";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

            handler.detachProductManager(userId,
                                         digitalProductGUID,
                                         digitalProductManagerRoleGUID,
                                         requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }

    /**
     * Unlink dependent products.
     *
     * @param serverName         name of called server
     * @param consumerDigitalProductGUID    unique identifier of the digital product that has the dependency.
     * @param consumedDigitalProductGUID    unique identifier of the digital product that it is using.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     *
     * This is a multi-link relationship, so this request removes every digital product dependency relationship
     * between the two elements.  Use the request that takes the relationship's own unique identifier to
     * remove just one of them.
     */
    public VoidResponse detachDigitalProductDependency(String                        serverName,
                                                       String                        consumerDigitalProductGUID,
                                                       String                        consumedDigitalProductGUID,
                                                       DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachDigitalProductDependency";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

            handler.detachDigitalProductDependency(userId,
                                                   consumerDigitalProductGUID,
                                                   consumedDigitalProductGUID,
                                                   requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }
}
