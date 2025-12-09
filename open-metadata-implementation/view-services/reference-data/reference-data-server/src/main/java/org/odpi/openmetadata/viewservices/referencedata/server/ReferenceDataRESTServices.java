/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.referencedata.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ValidValueDefinitionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * The ReferenceDataRESTServices provides the server-side implementation of the Reference Data Open Metadata
 * View Service (OMVS).
 */
public class ReferenceDataRESTServices extends TokenController
{
    private static final ReferenceDataInstanceHandler instanceHandler = new ReferenceDataInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(ReferenceDataRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public ReferenceDataRESTServices()
    {
    }


    /**
     * Create a validValueDefinition.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the validValueDefinition.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createValidValueDefinition(String                serverName,
                                                   NewElementRequestBody requestBody)
    {
        final String methodName = "createValidValueDefinition";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ValidValueDefinitionHandler handler = instanceHandler.getValidValueDefinitionHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof ValidValueDefinitionProperties validValueDefinitionProperties)
                {
                    response.setGUID(handler.createValidValueDefinition(userId,
                                                                        requestBody,
                                                                        requestBody.getInitialClassifications(),
                                                                        validValueDefinitionProperties,
                                                                        requestBody.getParentRelationshipProperties()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ValidValueDefinitionProperties.class.getName(), methodName);
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
     * Create a new metadata element to represent a validValueDefinition using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createValidValueDefinitionFromTemplate(String              serverName,
                                                               TemplateRequestBody requestBody)
    {
        final String methodName = "createValidValueDefinitionFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ValidValueDefinitionHandler handler = instanceHandler.getValidValueDefinitionHandler(userId, serverName, methodName);

                response.setGUID(handler.createValidValueDefinitionFromTemplate(userId,
                                                                                requestBody,
                                                                                requestBody.getTemplateGUID(),
                                                                                requestBody.getReplacementProperties(),
                                                                                requestBody.getPlaceholderPropertyValues(),
                                                                                requestBody.getParentRelationshipProperties()));
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
     * Update the properties of a validValueDefinition.
     *
     * @param serverName         name of called server.
     * @param validValueDefinitionGUID unique identifier of the validValueDefinition (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return boolean or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public BooleanResponse updateValidValueDefinition(String                   serverName,
                                                      String                   validValueDefinitionGUID,
                                                      UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateValidValueDefinition";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ValidValueDefinitionHandler handler = instanceHandler.getValidValueDefinitionHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof ValidValueDefinitionProperties validValueDefinitionProperties)
                {
                    response.setFlag(handler.updateValidValueDefinition(userId,
                                                                        validValueDefinitionGUID,
                                                                        requestBody,
                                                                        validValueDefinitionProperties));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ValidValueDefinitionProperties.class.getName(), methodName);
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
     * Attach a valid value to an implementation - probably a referenceable.
     *
     * @param serverName         name of called server
     * @param validValueDefinitionGUID            unique identifier of the validValueDefinition
     * @param elementGUID       unique identifier of the asset
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkValidValueImplementation(String                     serverName,
                                                     String                     validValueDefinitionGUID,
                                                     String                     elementGUID,
                                                     NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkValidValueImplementation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ValidValueDefinitionHandler handler = instanceHandler.getValidValueDefinitionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ValidValuesImplementationProperties properties)
                {
                    handler.linkValidValueImplementation(userId,
                                                         validValueDefinitionGUID,
                                                         elementGUID,
                                                         requestBody,
                                                         properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkValidValueImplementation(userId,
                                                         validValueDefinitionGUID,
                                                         elementGUID,
                                                         requestBody,
                                                         null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ValidValuesImplementationProperties.class.getName(), methodName);
                }
            }
            else
            {
                MetadataSourceOptions metadataSourceOptions = new MetadataSourceOptions();
                metadataSourceOptions.setEffectiveTime(new Date());

                handler.linkValidValueImplementation(userId,
                                                     validValueDefinitionGUID,
                                                     elementGUID,
                                                     metadataSourceOptions,
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
     * Detach a valid value from an implementation - probably a referenceable.
     *
     * @param serverName         name of called server
     * @param validValueDefinitionGUID            unique identifier of the validValueDefinition
     * @param elementGUID       unique identifier of the element
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachValidValueImplementation(String                        serverName,
                                                       String                        validValueDefinitionGUID,
                                                       String                        elementGUID,
                                                       DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachValidValueImplementation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValueDefinitionHandler handler = instanceHandler.getValidValueDefinitionHandler(userId, serverName, methodName);

            handler.detachValidValueImplementation(userId, validValueDefinitionGUID, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Attach a valid value to a consumer - probably a schema element or data set.
     *
     * @param serverName         name of called server
     * @param elementGUID       unique identifier of the asset
     * @param validValueDefinitionGUID            unique identifier of the validValueDefinition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkValidValuesAssignment(String                     serverName,
                                                  String                     elementGUID,
                                                  String                     validValueDefinitionGUID,
                                                  NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkValidValuesAssignment";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ValidValueDefinitionHandler handler = instanceHandler.getValidValueDefinitionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ValidValuesAssignmentProperties validValuesAssignmentProperties)
                {
                    handler.linkValidValuesAssignment(userId,
                                                      elementGUID,
                                                      validValueDefinitionGUID,
                                                      requestBody,
                                                      validValuesAssignmentProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkValidValuesAssignment(userId,
                                                      elementGUID,
                                                      validValueDefinitionGUID,
                                                      requestBody,
                                                      null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ValidValuesAssignmentProperties.class.getName(), methodName);
                }
            }
            else
            {
                MetadataSourceOptions metadataSourceOptions = new MetadataSourceOptions();
                metadataSourceOptions.setEffectiveTime(new Date());

                handler.linkValidValuesAssignment(userId,
                                                  elementGUID,
                                                  validValueDefinitionGUID,
                                                  metadataSourceOptions,
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
     * Detach a valid value from a consumer - probably a schema element or data set.
     *
     * @param serverName         name of called server
     * @param elementGUID       unique identifier of the element
     * @param validValueDefinitionGUID            unique identifier of the validValueDefinition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachValidValuesAssignment(String                        serverName,
                                                    String                        elementGUID,
                                                    String                        validValueDefinitionGUID,
                                                    DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachValidValuesAssignment";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValueDefinitionHandler handler = instanceHandler.getValidValueDefinitionHandler(userId, serverName, methodName);

            handler.detachValidValuesAssignment(userId, elementGUID, validValueDefinitionGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Attach a valid value to a consumer - probably a schema element or data set.
     *
     * @param serverName         name of called server
     * @param elementGUID       unique identifier of the asset
     * @param validValueDefinitionGUID            unique identifier of the validValueDefinition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkReferenceValueAssignment(String                     serverName,
                                                     String                     elementGUID,
                                                     String                     validValueDefinitionGUID,
                                                     NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkValidValuesAssignment";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ValidValueDefinitionHandler handler = instanceHandler.getValidValueDefinitionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ValidValuesAssignmentProperties validValuesAssignmentProperties)
                {
                    handler.linkValidValuesAssignment(userId,
                                                      elementGUID,
                                                      validValueDefinitionGUID,
                                                      requestBody,
                                                      validValuesAssignmentProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkValidValuesAssignment(userId,
                                                      elementGUID,
                                                      validValueDefinitionGUID,
                                                      requestBody,
                                                      null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ValidValuesAssignmentProperties.class.getName(), methodName);
                }
            }
            else
            {
                MetadataSourceOptions metadataSourceOptions = new MetadataSourceOptions();
                metadataSourceOptions.setEffectiveTime(new Date());

                handler.linkValidValuesAssignment(userId,
                                                  elementGUID,
                                                  validValueDefinitionGUID,
                                                  metadataSourceOptions,
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
     * Detach a valid value from a consumer - probably a schema element or data set.
     *
     * @param serverName         name of called server
     * @param elementGUID       unique identifier of the element
     * @param validValueDefinitionGUID            unique identifier of the validValueDefinition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachReferenceValueAssignment(String                        serverName,
                                                       String                        elementGUID,
                                                       String                        validValueDefinitionGUID,
                                                       DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachReferenceValueAssignment";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValueDefinitionHandler handler = instanceHandler.getValidValueDefinitionHandler(userId, serverName, methodName);

            handler.detachReferenceValueAssignment(userId, elementGUID, validValueDefinitionGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Attach a valid value to one of its peers.
     *
     * @param serverName         name of called server
     * @param validValueDefinitionOneGUID          unique identifier of the first validValueDefinition
     * @param validValueDefinitionTwoGUID          unique identifier of the second validValueDefinition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkAssociatedValidValues(String                     serverName,
                                                  String                     validValueDefinitionOneGUID,
                                                  String                     validValueDefinitionTwoGUID,
                                                  NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkAssociatedValidValues";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ValidValueDefinitionHandler handler = instanceHandler.getValidValueDefinitionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ValidValueAssociationProperties properties)
                {
                    handler.linkAssociatedValidValues(userId,
                                                      validValueDefinitionOneGUID,
                                                      validValueDefinitionTwoGUID,
                                                      requestBody,
                                                      properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkAssociatedValidValues(userId,
                                                          validValueDefinitionOneGUID,
                                                          validValueDefinitionTwoGUID,
                                                          requestBody,
                                                          null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ValidValueAssociationProperties.class.getName(), methodName);
                }
            }
            else
            {
                MetadataSourceOptions metadataSourceOptions = new MetadataSourceOptions();
                metadataSourceOptions.setEffectiveTime(new Date());

                handler.linkAssociatedValidValues(userId,
                                                  validValueDefinitionOneGUID,
                                                  validValueDefinitionTwoGUID,
                                                  metadataSourceOptions,
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
     * Detach a valid value from one of its peers.
     *
     * @param serverName         name of called server
     * @param validValueDefinitionOneGUID          unique identifier of the first validValueDefinition
     * @param validValueDefinitionTwoGUID          unique identifier of the second validValueDefinition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachAssociatedValidValues(String                        serverName,
                                                    String                        validValueDefinitionOneGUID,
                                                    String                        validValueDefinitionTwoGUID,
                                                    DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachAssociatedValidValues";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValueDefinitionHandler handler = instanceHandler.getValidValueDefinitionHandler(userId, serverName, methodName);

            handler.detachAssociatedValidValues(userId, validValueDefinitionOneGUID, validValueDefinitionTwoGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Attach a valid value to one of its peers.
     *
     * @param serverName         name of called server
     * @param validValueDefinitionOneGUID          unique identifier of the first validValueDefinition
     * @param validValueDefinitionTwoGUID          unique identifier of the second validValueDefinition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkConsistentValidValues(String                     serverName,
                                                  String                     validValueDefinitionOneGUID,
                                                  String                     validValueDefinitionTwoGUID,
                                                  NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkConsistentValidValues";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ValidValueDefinitionHandler handler = instanceHandler.getValidValueDefinitionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ConsistentValidValuesProperties properties)
                {
                    handler.linkConsistentValidValues(userId,
                                                      validValueDefinitionOneGUID,
                                                      validValueDefinitionTwoGUID,
                                                      requestBody,
                                                      properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkConsistentValidValues(userId,
                                                      validValueDefinitionOneGUID,
                                                      validValueDefinitionTwoGUID,
                                                      requestBody,
                                                      null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ConsistentValidValuesProperties.class.getName(), methodName);
                }
            }
            else
            {
                MetadataSourceOptions metadataSourceOptions = new MetadataSourceOptions();
                metadataSourceOptions.setEffectiveTime(new Date());

                handler.linkConsistentValidValues(userId,
                                                  validValueDefinitionOneGUID,
                                                  validValueDefinitionTwoGUID,
                                                  metadataSourceOptions,
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
     * Detach a valid value from one of its peers.
     *
     * @param serverName         name of called server
     * @param validValueDefinitionOneGUID          unique identifier of the first validValueDefinition
     * @param validValueDefinitionTwoGUID          unique identifier of the second validValueDefinition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachConsistentValidValues(String                        serverName,
                                                    String                        validValueDefinitionOneGUID,
                                                    String                        validValueDefinitionTwoGUID,
                                                    DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachConsistentValidValues";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValueDefinitionHandler handler = instanceHandler.getValidValueDefinitionHandler(userId, serverName, methodName);

            handler.detachConsistentValidValues(userId, validValueDefinitionOneGUID, validValueDefinitionTwoGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Attach a valid value to one of its peers.
     *
     * @param serverName         name of called server
     * @param validValueDefinitionOneGUID          unique identifier of the first validValueDefinition
     * @param validValueDefinitionTwoGUID          unique identifier of the second validValueDefinition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkMappedValidValues(String                     serverName,
                                              String                     validValueDefinitionOneGUID,
                                              String                     validValueDefinitionTwoGUID,
                                              NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkMappedValidValues";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ValidValueDefinitionHandler handler = instanceHandler.getValidValueDefinitionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ValidValuesMappingProperties properties)
                {
                    handler.linkMappedValidValues(userId,
                                                  validValueDefinitionOneGUID,
                                                  validValueDefinitionTwoGUID,
                                                  requestBody,
                                                  properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkMappedValidValues(userId,
                                                  validValueDefinitionOneGUID,
                                                  validValueDefinitionTwoGUID,
                                                  requestBody,
                                                  null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ValidValuesMappingProperties.class.getName(), methodName);
                }
            }
            else
            {
                MetadataSourceOptions metadataSourceOptions = new MetadataSourceOptions();
                metadataSourceOptions.setEffectiveTime(new Date());

                handler.linkMappedValidValues(userId,
                                              validValueDefinitionOneGUID,
                                              validValueDefinitionTwoGUID,
                                              metadataSourceOptions,
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
     * Detach a valid value from one of its peers.
     *
     * @param serverName         name of called server
     * @param validValueDefinitionOneGUID          unique identifier of the first validValueDefinition
     * @param validValueDefinitionTwoGUID          unique identifier of the second validValueDefinition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachMappedValidValues(String                        serverName,
                                                String                        validValueDefinitionOneGUID,
                                                String                        validValueDefinitionTwoGUID,
                                                DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachMappedValidValues";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValueDefinitionHandler handler = instanceHandler.getValidValueDefinitionHandler(userId, serverName, methodName);

            handler.detachMappedValidValues(userId, validValueDefinitionOneGUID, validValueDefinitionTwoGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Attach a valid value to a valid value set.
     *
     * @param serverName         name of called server
     * @param validValueDefinitionGUID          unique identifier of the super validValueDefinition
     * @param nestedValidValueDefinitionGUID            unique identifier of the nested validValueDefinition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkValidValueMember(String                     serverName,
                                             String                     validValueDefinitionGUID,
                                             String                     nestedValidValueDefinitionGUID,
                                             NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkValidValueMember";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ValidValueDefinitionHandler handler = instanceHandler.getValidValueDefinitionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ValidValueMemberProperties properties)
                {
                    handler.linkValidValueMember(userId,
                                                 validValueDefinitionGUID,
                                                 nestedValidValueDefinitionGUID,
                                                 requestBody,
                                                 properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkValidValueMember(userId,
                                                 validValueDefinitionGUID,
                                                 nestedValidValueDefinitionGUID,
                                                 requestBody,
                                                 null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ValidValueMemberProperties.class.getName(), methodName);
                }
            }
            else
            {
                MetadataSourceOptions metadataSourceOptions = new MetadataSourceOptions();
                metadataSourceOptions.setEffectiveTime(new Date());

                handler.linkValidValueMember(userId,
                                             validValueDefinitionGUID,
                                             nestedValidValueDefinitionGUID,
                                             metadataSourceOptions,
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
     * Detach a valid value from a valid value set.
     *
     * @param serverName         name of called server
     * @param validValueDefinitionGUID          unique identifier of the super validValueDefinition
     * @param nestedValidValueDefinitionGUID            unique identifier of the nested validValueDefinition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachValidValueMember(String                        serverName,
                                               String                        validValueDefinitionGUID,
                                               String                        nestedValidValueDefinitionGUID,
                                               DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachNestedValidValueDefinition";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValueDefinitionHandler handler = instanceHandler.getValidValueDefinitionHandler(userId, serverName, methodName);

            handler.detachValidValueMember(userId, validValueDefinitionGUID, nestedValidValueDefinitionGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Delete a validValueDefinition.
     *
     * @param serverName         name of called server
     * @param validValueDefinitionGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteValidValueDefinition(String                   serverName,
                                                   String                   validValueDefinitionGUID,
                                                   DeleteElementRequestBody requestBody)
    {
        final String methodName = "deleteValidValueDefinition";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValueDefinitionHandler handler = instanceHandler.getValidValueDefinitionHandler(userId, serverName, methodName);

            handler.deleteValidValueDefinition(userId, validValueDefinitionGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of validValueDefinition metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getValidValueDefinitionsByName(String            serverName,
                                                                           FilterRequestBody requestBody)
    {
        final String methodName = "getValidValueDefinitionsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValueDefinitionHandler handler = instanceHandler.getValidValueDefinitionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getValidValueDefinitionsByName(userId, requestBody.getFilter(), requestBody));
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
     * Retrieve the list of validValueDefinition metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param validValueDefinitionGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getValidValueDefinitionByGUID(String             serverName,
                                                                         String             validValueDefinitionGUID,
                                                                         GetRequestBody requestBody)
    {
        final String methodName = "getValidValueDefinitionByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValueDefinitionHandler handler = instanceHandler.getValidValueDefinitionHandler(userId, serverName, methodName);

            response.setElement(handler.getValidValueDefinitionByGUID(userId, validValueDefinitionGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of validValueDefinition metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findValidValueDefinitions(String                  serverName,
                                                                      SearchStringRequestBody requestBody)
    {
        final String methodName = "findValidValueDefinitions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ValidValueDefinitionHandler handler = instanceHandler.getValidValueDefinitionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findValidValueDefinitions(userId, requestBody.getSearchString(), requestBody));
            }
            else
            {
                response.setElements(handler.findValidValueDefinitions(userId, null, null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

}
