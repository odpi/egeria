/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.templatemanager.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.TemplateHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.SourcedFromProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.templates.CatalogTemplateProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.templates.TemplateProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.templates.TemplateSubstituteProperties;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;


/**
 * The TemplateManagerRESTServices provides the server-side implementation of the Template Manager Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class TemplateManagerRESTServices extends TokenController
{
    private static final TemplateManagerInstanceHandler instanceHandler = new TemplateManagerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(TemplateManagerRESTServices.class),
                                                                            instanceHandler.getServiceName());


    /**
     * Default constructor
     */
    public TemplateManagerRESTServices()
    {
    }

    /**
     * Classify/reclassify the element with the Template classification
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to classify
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public VoidResponse addTemplateClassification(String                       serverName,
                                                  String                       elementGUID,
                                                  NewClassificationRequestBody requestBody)
    {
        final String methodName = "addTemplateClassification";

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
                TemplateHandler handler = instanceHandler.getTemplateHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof TemplateProperties properties)
                {
                    handler.addTemplateClassification(userId, elementGUID, properties, requestBody);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.addTemplateClassification(userId, elementGUID, null, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(TemplateProperties.class.getName(), methodName);
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
     * Remove the Template classification from the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    public VoidResponse removeTemplateClassification(String                          serverName,
                                                     String                          elementGUID,
                                                     DeleteClassificationRequestBody requestBody)
    {
        final String   methodName = "removeTemplateClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            TemplateHandler handler = instanceHandler.getTemplateHandler(userId, serverName, methodName);

            handler.removeTemplateClassification(userId, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Create a relationship between two elements that show they represent the same "thing". If the relationship already exists,
     * the properties are updated.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to link
     * @param templateGUID identifier of the duplicate to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse linkSourcedFrom(String                     serverName,
                                        String                     elementGUID,
                                        String                     templateGUID,
                                        NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkSourcedFrom";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            TemplateHandler handler = instanceHandler.getTemplateHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof SourcedFromProperties sourcedFromProperties)
                {
                    handler.linkSourcedFrom(userId,
                                            elementGUID,
                                            templateGUID,
                                            requestBody,
                                            sourcedFromProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkSourcedFrom(userId,
                                            elementGUID,
                                            templateGUID,
                                            requestBody,
                                            null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SourcedFromProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkSourcedFrom(userId,
                                        elementGUID,
                                        templateGUID,
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
     * Remove the PeerDuplicateLink relationship between an element and its duplicate.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to update
     * @param templateGUID identifier of the duplicate to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse detachSourcedFrom(String                        serverName,
                                          String                        elementGUID,
                                          String                        templateGUID,
                                          DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachSourcedFrom";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            TemplateHandler handler = instanceHandler.getTemplateHandler(userId, serverName, methodName);

            handler.detachSourcedFrom(userId, elementGUID, templateGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Classify/reclassify the element with the TemplateSubstitute classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to classify
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public VoidResponse addTemplateSubstituteClassification(String                    serverName,
                                                            String                    elementGUID,
                                                            NewClassificationRequestBody requestBody)
    {
        final String methodName = "addTemplateSubstituteClassification";

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
                TemplateHandler handler = instanceHandler.getTemplateHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof TemplateSubstituteProperties properties)
                {
                    handler.addTemplateSubstituteClassification(userId, elementGUID, properties, requestBody);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.addTemplateSubstituteClassification(userId, elementGUID, null, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(TemplateSubstituteProperties.class.getName(), methodName);
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
     * Remove the TemplateSubstitute classification from the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    public VoidResponse removeTemplateSubstituteClassification(String                          serverName,
                                                               String                          elementGUID,
                                                               DeleteClassificationRequestBody requestBody)
    {
        final String   methodName = "removeTemplateSubstituteClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            TemplateHandler handler = instanceHandler.getTemplateHandler(userId, serverName, methodName);

            handler.removeTemplateSubstituteClassification(userId, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Create a CatalogTemplate relationship between an element and one of the source elements of its properties.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the element that was created with the values from a number of duplicate elements
     * @param templateGUID unique identifier of one of the source elements
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse linkCatalogTemplate(String                    serverName,
                                            String                     elementGUID,
                                            String                     templateGUID,
                                            NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkCatalogTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            TemplateHandler handler = instanceHandler.getTemplateHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof CatalogTemplateProperties catalogTemplateProperties)
                {
                    handler.linkCatalogTemplate(userId,
                                                elementGUID,
                                                templateGUID,
                                                requestBody,
                                                catalogTemplateProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkCatalogTemplate(userId,
                                                elementGUID,
                                                templateGUID,
                                                requestBody,
                                                null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CatalogTemplateProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkCatalogTemplate(userId,
                                            elementGUID,
                                            templateGUID,
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
     * Remove a CatalogTemplate relationship between an element and one of its source elements.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the element that was created with the values from a number of duplicate elements
     * @param templateGUID unique identifier of one of the source elements
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse detachCatalogTemplate(String                        serverName,
                                              String                        elementGUID,
                                              String                        templateGUID,
                                              DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachCatalogTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            TemplateHandler handler = instanceHandler.getTemplateHandler(userId, serverName, methodName);

            handler.detachCatalogTemplate(userId, elementGUID, templateGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

}
