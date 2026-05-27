/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.privacyofficer.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.CollectionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.dataprocessing.DataProcessingTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.dataprocessing.PermittedProcessingProperties;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;



/**
 * The PrivacyOfficerRESTServices provides the server-side implementation of the Privacy Officer Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class PrivacyOfficerRESTServices extends TokenController
{
    private static final PrivacyOfficerInstanceHandler instanceHandler = new PrivacyOfficerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(PrivacyOfficerRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public PrivacyOfficerRESTServices()
    {
    }


    /**
     * Link a data processing purpose to a data processing description.
     *
     * @param serverName         name of called server
     * @param dataProcessingPurposeGUID          unique identifier of the data processing purpose
     * @param dataProcessingDescriptionGUID  unique identifier of the data processing description
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkPermittedProcessing(String                     serverName,
                                                String                     dataProcessingPurposeGUID,
                                                String                     dataProcessingDescriptionGUID,
                                                NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkPermittedProcessing";

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
                if (requestBody.getProperties() instanceof PermittedProcessingProperties properties)
                {
                    handler.linkPermittedProcessing(userId,
                                                    dataProcessingPurposeGUID,
                                                    dataProcessingDescriptionGUID,
                                                    requestBody,
                                                    properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkPermittedProcessing(userId,
                                                    dataProcessingPurposeGUID,
                                                    dataProcessingDescriptionGUID,
                                                    requestBody,
                                                    null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(PermittedProcessingProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkPermittedProcessing(userId,
                                                dataProcessingPurposeGUID,
                                                dataProcessingDescriptionGUID,
                                                null,
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
     * Detach a data processing purpose from a data processing description.
     *
     * @param serverName         name of called server
     * @param dataProcessingPurposeGUID          unique identifier of the data processing purpose
     * @param dataProcessingDescriptionGUID  unique identifier of the data processing description
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachPermittedProcessing(String                        serverName,
                                                  String                        dataProcessingPurposeGUID,
                                                  String                        dataProcessingDescriptionGUID,
                                                  DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachPermittedProcessing";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

            handler.detachPermittedProcessing(userId, dataProcessingPurposeGUID, dataProcessingDescriptionGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Link a data processing action to a target element.
     *
     * @param serverName         name of called server
     * @param dataProcessingActionGUID          unique identifier of the data processing action
     * @param targetGUID  unique identifier of the target element
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkDataProcessingTarget(String                     serverName,
                                                 String                     dataProcessingActionGUID,
                                                 String                     targetGUID,
                                                 NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkDataProcessingTarget";

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
                if (requestBody.getProperties() instanceof DataProcessingTargetProperties properties)
                {
                    handler.linkDataProcessingTarget(userId,
                                                     dataProcessingActionGUID,
                                                     targetGUID,
                                                     requestBody,
                                                     properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkDataProcessingTarget(userId,
                                                     dataProcessingActionGUID,
                                                     targetGUID,
                                                     requestBody,
                                                     null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DataProcessingTargetProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkDataProcessingTarget(userId,
                                                 dataProcessingActionGUID,
                                                 targetGUID,
                                                 null,
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
     * Detach a data processing action from a target element.
     *
     * @param serverName         name of called server
     * @param dataProcessingActionGUID          unique identifier of the data processing action
     * @param targetGUID  unique identifier of the target element
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachDataProcessingTarget(String                        serverName,
                                                   String                        dataProcessingActionGUID,
                                                   String                        targetGUID,
                                                   DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachDataProcessingTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

            handler.detachDataProcessingTarget(userId, dataProcessingActionGUID, targetGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }



}
