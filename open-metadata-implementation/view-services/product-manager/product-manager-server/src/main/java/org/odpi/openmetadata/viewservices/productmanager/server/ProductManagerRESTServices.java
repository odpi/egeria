/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.productmanager.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.CollectionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.AssignmentScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductDependencyProperties;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;



/**
 * The ProductManagerRESTServices provides the server-side implementation of the Product Manager Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
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
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkDigitalProductDependency(String                  serverName,
                                                     String                  consumerDigitalProductGUID,
                                                     String                  consumedDigitalProductGUID,
                                                     NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkDigitalProductDependency";

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
                if (requestBody.getProperties() instanceof DigitalProductDependencyProperties properties)
                {
                    handler.linkDigitalProductDependency(userId,
                                                         consumerDigitalProductGUID,
                                                         consumedDigitalProductGUID,
                                                         requestBody,
                                                         properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkDigitalProductDependency(userId,
                                                         consumerDigitalProductGUID,
                                                         consumedDigitalProductGUID,
                                                         requestBody,
                                                         null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DigitalProductDependencyProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkDigitalProductDependency(userId,
                                                     consumerDigitalProductGUID,
                                                     consumedDigitalProductGUID,
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
     */
    public VoidResponse detachDigitalProductDependency(String                   serverName,
                                                       String                   consumerDigitalProductGUID,
                                                       String                   consumedDigitalProductGUID,
                                                       DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachDigitalProductDependency";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());
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

        restCallLogger.logRESTCallReturn(token, response.toString());
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

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


}
