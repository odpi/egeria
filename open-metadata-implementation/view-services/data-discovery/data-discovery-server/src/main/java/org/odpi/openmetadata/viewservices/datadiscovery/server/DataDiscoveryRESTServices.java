/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.datadiscovery.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.AnnotationHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.*;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;


/**
 * The DataDiscoveryRESTServices provides the server-side implementation of the Data Discovery Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class DataDiscoveryRESTServices extends TokenController
{
    private static final DataDiscoveryInstanceHandler instanceHandler = new DataDiscoveryInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(DataDiscoveryRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public DataDiscoveryRESTServices()
    {
    }



    /**
     * Create an annotation.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the annotation.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createAnnotation(String                serverName,
                                         NewElementRequestBody requestBody)
    {
        final String methodName = "createAnnotation";

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
                AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof AnnotationProperties annotationProperties)
                {
                    response.setGUID(handler.createAnnotation(userId,
                                                              requestBody,
                                                              requestBody.getInitialClassifications(),
                                                              annotationProperties,
                                                              requestBody.getParentRelationshipProperties()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(AnnotationProperties.class.getName(), methodName);
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
     * Create a new metadata element to represent an annotation using an existing metadata element as a template.
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
    public GUIDResponse createAnnotationFromTemplate(String              serverName,
                                                     TemplateRequestBody requestBody)
    {
        final String methodName = "createAnnotationFromTemplate";

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
                AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

                response.setGUID(handler.createAnnotationFromTemplate(userId,
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
     * Update the properties of an annotation.
     *
     * @param serverName         name of called server.
     * @param annotationGUID unique identifier of the annotation (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return boolean or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public BooleanResponse updateAnnotation(String                   serverName,
                                            String                   annotationGUID,
                                            UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateAnnotation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        BooleanResponse response = new BooleanResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof AnnotationProperties annotationProperties)
                {
                    response.setFlag(handler.updateAnnotation(userId,
                                                              annotationGUID,
                                                              requestBody,
                                                              annotationProperties));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(AnnotationProperties.class.getName(), methodName);
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
     * Create a relationship that links a new annotation to its survey report.  This relationship is typically
     * established during the createAnnotation as the parent relationship.  It is included for completeness.
     *
     * @param serverName         name of called server
     * @param surveyReportGUID       unique identifier of the report
     * @param newAnnotationGUID           unique identifier of the  annotation
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse attachAnnotationToReport(String                     serverName,
                                                 String                     surveyReportGUID,
                                                 String                     newAnnotationGUID,
                                                 NewRelationshipRequestBody requestBody)
    {
        final String methodName = "attachAnnotationToReport";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ReportedAnnotationProperties reportedAnnotationProperties)
                {
                    handler.attachAnnotationToReport(userId,
                                                     surveyReportGUID,
                                                     newAnnotationGUID,
                                                     requestBody,
                                                     reportedAnnotationProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.attachAnnotationToReport(userId,
                                                     surveyReportGUID,
                                                     newAnnotationGUID,
                                                     requestBody,
                                                     null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ReportedAnnotationProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.attachAnnotationToReport(userId,
                                                 surveyReportGUID,
                                                 newAnnotationGUID,
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
     * Detach an annotation from its report (ReportedAnnotation relationship).
     *
     * @param serverName         name of called server
     * @param surveyReportGUID       unique identifier of the report
     * @param newAnnotationGUID           unique identifier of the  annotation
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachAnnotationFromReport(String                        serverName,
                                                   String                        surveyReportGUID,
                                                   String                        newAnnotationGUID,
                                                   DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachAnnotationFromReport";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

            handler.detachAnnotationFromReport(userId, surveyReportGUID, newAnnotationGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Attach an annotation to the element that it is describing (via AssociatedAnnotation relationship).
     *
     * @param serverName         name of called server
     * @param elementGUID          unique identifier of the described element
     * @param annotationGUID          unique identifier of the annotation
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkAnnotationToDescribedElement(String                     serverName,
                                                         String                     elementGUID,
                                                         String                     annotationGUID,
                                                         NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkAnnotationToDescribedElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof AssociatedAnnotationProperties associatedAnnotationProperties)
                {
                    handler.linkAnnotationToDescribedElement(userId,
                                                             elementGUID,
                                                             annotationGUID,
                                                             requestBody,
                                                             associatedAnnotationProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkAnnotationToDescribedElement(userId,
                                                             elementGUID,
                                                             annotationGUID,
                                                             requestBody,
                                                             null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(AssociatedAnnotationProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkAnnotationToDescribedElement(userId,
                                                         elementGUID,
                                                         annotationGUID,
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
     * Detach an annotation from the element that it is describing (via AssociatedAnnotation relationship).
     *
     * @param serverName         name of called server
     * @param elementGUID          unique identifier of the described element
     * @param annotationGUID          unique identifier of the annotation
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachAnnotationFromDescribedElement(String                        serverName,
                                                             String                        elementGUID,
                                                             String                        annotationGUID,
                                                             DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachAnnotationFromDescribedElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

            handler.detachAnnotationFromDescribedElement(userId, elementGUID, annotationGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Attach an annotation to the equivalent annotation from the previous run of the survey.
     *
     * @param serverName         name of called server
     * @param previousAnnotationGUID          unique identifier of the annotation from the previous run of the survey
     * @param newAnnotationGUID            unique identifier of the annotation from this run of the survey
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkAnnotationToItsPredecessor(String                     serverName,
                                                       String                     previousAnnotationGUID,
                                                       String                     newAnnotationGUID,
                                                       NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkAnnotationToItsPredecessor";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof AnnotationExtensionProperties annotationExtensionProperties)
                {
                    handler.linkAnnotationToItsPredecessor(userId,
                                                           previousAnnotationGUID,
                                                           newAnnotationGUID,
                                                           requestBody,
                                                           annotationExtensionProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkAnnotationToItsPredecessor(userId,
                                                           previousAnnotationGUID,
                                                           newAnnotationGUID,
                                                           requestBody,
                                                           null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(AnnotationExtensionProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkAnnotationToItsPredecessor(userId,
                                                       previousAnnotationGUID,
                                                       newAnnotationGUID,
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
     * Detach an annotation from an annotation from the previous run of the survey.
     *
     * @param serverName         name of called server
     * @param previousAnnotationGUID          unique identifier of the annotation from the previous run of the survey
     * @param newAnnotationGUID            unique identifier of the annotation from this run of the survey
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachAnnotationFromItsPredecessor(String                        serverName,
                                                           String                        previousAnnotationGUID,
                                                           String                        newAnnotationGUID,
                                                           DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachAnnotationFromItsPredecessor";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

            handler.detachAnnotationFromItsPredecessor(userId, previousAnnotationGUID, newAnnotationGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Attach a schema analysis annotation to a matching schema type.
     *
     * @param serverName         name of called server
     * @param annotationGUID       unique identifier of the annotation
     * @param schemaTypeGUID            unique identifier of the schema type
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkDiscoveredSchemaType(String                     serverName,
                                                 String                     annotationGUID,
                                                 String                     schemaTypeGUID,
                                                 NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkDiscoveredSchemaType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DiscoveredSchemaTypeProperties discoveredSchemaTypeProperties)
                {
                    handler.linkDiscoveredSchemaType(userId,
                                                     annotationGUID,
                                                     schemaTypeGUID,
                                                     requestBody,
                                                     discoveredSchemaTypeProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkDiscoveredSchemaType(userId,
                                                     annotationGUID,
                                                     schemaTypeGUID,
                                                     requestBody,
                                                     null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DiscoveredSchemaTypeProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkDiscoveredSchemaType(userId,
                                                 annotationGUID,
                                                 schemaTypeGUID,
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
     * Detach a schema analysis annotation from a matching schema type.
     *
     * @param serverName         name of called server
     * @param annotationGUID       unique identifier of the annotation
     * @param schemaTypeGUID            unique identifier of the schema type
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachDiscoveredSchemaType(String                        serverName,
                                                   String                        annotationGUID,
                                                   String                        schemaTypeGUID,
                                                   DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachDiscoveredSchemaType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

            handler.detachDiscoveredSchemaType(userId, annotationGUID, schemaTypeGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Attach a resource profile log annotation to an asset where the profile data is stored.
     *
     * @param serverName         name of called server
     * @param annotationGUID               unique identifier of the annotation
     * @param assetGUID         unique identifier of the associated asset
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkResourceProfileData(String                     serverName,
                                                String                     annotationGUID,
                                                String                      assetGUID,
                                                NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkResourceProfileData";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ResourceProfileDataProperties resourceProfileDataProperties)
                {
                    handler.linkResourceProfileData(userId,
                                                    annotationGUID,
                                                    assetGUID,
                                                    requestBody,
                                                    resourceProfileDataProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkResourceProfileData(userId,
                                                    annotationGUID,
                                                    assetGUID,
                                                    requestBody,
                                                    null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ResourceProfileDataProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkResourceProfileData(userId,
                                                annotationGUID,
                                                assetGUID,
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
     * Detach a resource profile log annotation from an asset where the profile data is stored.
     *
     * @param serverName         name of called server
     * @param annotationGUID               unique identifier of the annotation
     * @param assetGUID         unique identifier of the associated asset
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachResourceProfileData(String                        serverName,
                                                  String                        annotationGUID,
                                                  String                        assetGUID,
                                                  DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachResourceProfileData";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

            handler.detachResourceProfileData(userId, annotationGUID, assetGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Attach a data class annotation to a data class.
     *
     * @param serverName         name of called server
     * @param annotationGUID               unique identifier of the annotation
     * @param dataClassGUID         unique identifier of the associated data class
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkDataClassMatch(String                     serverName,
                                           String                     annotationGUID,
                                           String                     dataClassGUID,
                                           NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkDataClassMatch";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DataClassMatchProperties dataClassMatchProperties)
                {
                    handler.linkDataClassMatch(userId,
                                               annotationGUID,
                                               dataClassGUID,
                                               requestBody,
                                               dataClassMatchProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkDataClassMatch(userId,
                                               annotationGUID,
                                               dataClassGUID,
                                               requestBody,
                                               null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DataClassMatchProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkDataClassMatch(userId,
                                           annotationGUID,
                                           dataClassGUID,
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
     * Detach a data class annotation from a data class.
     *
     * @param serverName         name of called server
     * @param annotationGUID     unique identifier of the annotation
     * @param dataClassGUID     unique identifier of the associated data class
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachDataClassMatch(String                        serverName,
                                             String                        annotationGUID,
                                             String                        dataClassGUID,
                                             DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachDataClassMatch";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

            handler.detachDataClassMatch(userId, annotationGUID, dataClassGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Attach a request for action annotation to the element that needs attention.
     *
     * @param serverName         name of called server
     * @param annotationGUID               unique identifier of the annotation
     * @param elementGUID         unique identifier of the associated element
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkRequestForActionTarget(String                     serverName,
                                                   String                     annotationGUID,
                                                   String                     elementGUID,
                                                   NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkRequestForActionTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof RequestForActionTargetProperties requestForActionTargetProperties)
                {
                    handler.linkRequestForActionTarget(userId,
                                                       elementGUID,
                                                       annotationGUID,
                                                       requestBody,
                                                       requestForActionTargetProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkRequestForActionTarget(userId,
                                                       elementGUID,
                                                       annotationGUID,
                                                       requestBody,
                                                       null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(RequestForActionTargetProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkRequestForActionTarget(userId,
                                                   elementGUID,
                                                   annotationGUID,
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
     * Detach a request for action annotation from its intended target element.
     *
     * @param serverName         name of called server
     * @param annotationGUID               unique identifier of the annotation
     * @param elementGUID         unique identifier of the associated element
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachRequestForActionTarget(String                        serverName,
                                                     String                        annotationGUID,
                                                     String                        elementGUID,
                                                     DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachRequestForActionTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

            handler.detachRequestForActionTarget(userId, elementGUID, annotationGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Delete an annotation.
     *
     * @param serverName         name of called server
     * @param annotationGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteAnnotation(String                   serverName,
                                         String                   annotationGUID,
                                         DeleteElementRequestBody requestBody)
    {
        final String methodName = "deleteAnnotation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

            handler.deleteAnnotation(userId, annotationGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of annotation metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getAnnotationsByName(String            serverName,
                                                                 FilterRequestBody requestBody)
    {
        final String methodName = "getAnnotationsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getAnnotationsByName(userId, requestBody.getFilter(), requestBody));
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
     * Returns the list of annotations associated with a particular analysis step.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getAnnotationsByAnalysisStep(String            serverName,
                                                                         FilterRequestBody requestBody)
    {
        final String methodName = "getAnnotationsByAnalysisStep";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getAnnotationsByAnalysisStep(userId, requestBody.getFilter(), requestBody));
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
     * Returns the list of annotations with a particular annotation type property.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getAnnotationsByAnnotationType(String            serverName,
                                                                           FilterRequestBody requestBody)
    {
        final String methodName = "getAnnotationsByAnnotationType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getAnnotationsByAnnotationType(userId, requestBody.getFilter(), requestBody));
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
     * Returns the list of annotations that describe the supplied element (AssociatedAnnotation relationship).
     *
     * @param serverName name of the service to route the request to
     * @param elementGUID              unique identifier of the starting element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getAnnotationsForElement(String             serverName,
                                                                     String             elementGUID,
                                                                     ResultsRequestBody requestBody)
    {
        final String methodName = "getAnnotationsForElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getAnnotationsForElement(userId, elementGUID, requestBody));
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
     * Returns the annotations created under the supplied survey report.
     *
     * @param serverName name of the service to route the request to
     * @param surveyReportGUID              unique identifier of the starting element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getNewAnnotations(String             serverName,
                                                              String             surveyReportGUID,
                                                              ResultsRequestBody requestBody)
    {
        final String methodName = "getNewAnnotations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getNewAnnotations(userId, surveyReportGUID, requestBody));
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
     * Returns the list of annotations that extend the supplied annotation (AnnotationExtension relationship).
     *
     * @param serverName name of the service to route the request to
     * @param annotationGUID              unique identifier of the starting element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getAnnotationExtensions(String             serverName,
                                                                    String             annotationGUID,
                                                                    ResultsRequestBody requestBody)
    {
        final String methodName = "getAnnotationExtensions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getAnnotationExtensions(userId, annotationGUID, requestBody));
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
     * Returns the list of annotations that are extended by the supplied annotation (AnnotationExtension relationship).
     *
     * @param serverName name of the service to route the request to
     * @param annotationGUID              unique identifier of the starting element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getPreviousAnnotations(String             serverName,
                                                                   String             annotationGUID,
                                                                   ResultsRequestBody requestBody)
    {
        final String methodName = "getPreviousAnnotations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getPreviousAnnotations(userId, annotationGUID, requestBody));
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
     * Retrieve the list of annotation metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param annotationGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getAnnotationByGUID(String             serverName,
                                                               String             annotationGUID,
                                                               GetRequestBody requestBody)
    {
        final String methodName = "getAnnotationByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

            response.setElement(handler.getAnnotationByGUID(userId, annotationGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of annotation metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findAnnotations(String                  serverName,
                                                            SearchStringRequestBody requestBody)
    {
        final String methodName = "findAnnotations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findAnnotations(userId, requestBody.getSearchString(), requestBody));
            }
            else
            {
                response.setElements(handler.findAnnotations(userId, null, null));
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
