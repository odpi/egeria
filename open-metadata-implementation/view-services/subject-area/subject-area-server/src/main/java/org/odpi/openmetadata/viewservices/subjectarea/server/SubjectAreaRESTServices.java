/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.subjectarea.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SubjectAreaHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.SubjectAreaDefinitionProperties;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;


/**
 * The SubjectAreaRESTServices provides the server-side implementation of the Subject Area Open Metadata
 * View Service (OMVS).  This interface provides access to data fields, data structures and data classes.
 */
public class SubjectAreaRESTServices extends TokenController
{
    private static final SubjectAreaInstanceHandler instanceHandler = new SubjectAreaInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(SubjectAreaRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public SubjectAreaRESTServices()
    {
    }


    /**
     * Create a data structure.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the data structure.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createSubjectArea(String                serverName,
                                          NewElementRequestBody requestBody)
    {
        final String methodName = "createSubjectArea";

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
                SubjectAreaHandler handler = instanceHandler.getSubjectAreaManager(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof SubjectAreaDefinitionProperties subjectAreaDefinitionProperties)
                {
                    response.setGUID(handler.createSubjectArea(userId,
                                                               requestBody,
                                                               requestBody.getInitialClassifications(),
                                                               subjectAreaDefinitionProperties,
                                                               requestBody.getParentRelationshipProperties()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SubjectAreaDefinitionProperties.class.getName(), methodName);
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
     * Create a new metadata element to represent a data structure using an existing metadata element as a template.
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
    public GUIDResponse createSubjectAreaFromTemplate(String              serverName,
                                                      TemplateRequestBody requestBody)
    {
        final String methodName = "createSubjectAreaFromTemplate";

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
                SubjectAreaHandler handler = instanceHandler.getSubjectAreaManager(userId, serverName, methodName);

                response.setGUID(handler.createSubjectAreaFromTemplate(userId,
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
     * Update the properties of a data structure.
     *
     * @param serverName         name of called server.
     * @param subjectAreaGUID unique identifier of the data structure (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateSubjectArea(String                   serverName,
                                          String                   subjectAreaGUID,
                                          UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateSubjectArea";

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
                SubjectAreaHandler handler = instanceHandler.getSubjectAreaManager(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof SubjectAreaDefinitionProperties subjectAreaDefinitionProperties)
                {
                    handler.updateSubjectArea(userId, subjectAreaGUID, requestBody, subjectAreaDefinitionProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.updateSubjectArea(userId, subjectAreaGUID, requestBody, null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SubjectAreaDefinitionProperties.class.getName(), methodName);
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
     * Attach a data field to a data structure.
     *
     * @param serverName         name of called server
     * @param subjectAreaGUID    unique identifier of the parent subject area.
     * @param nestedSubjectAreaGUID    unique identifier of the nested subject area.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkSubjectAreas(String                  serverName,
                                         String                  subjectAreaGUID,
                                         String                  nestedSubjectAreaGUID,
                                         NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkSubjectAreas";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaHandler handler = instanceHandler.getSubjectAreaManager(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.linkSubjectAreas(userId,
                                         subjectAreaGUID,
                                         nestedSubjectAreaGUID,
                                         requestBody,
                                         requestBody.getProperties());
            }
            else
            {
                handler.linkSubjectAreas(userId,
                                         subjectAreaGUID,
                                         nestedSubjectAreaGUID,
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
     * Detach a data field from a data structure.
     *
     * @param serverName         name of called server
     * @param subjectAreaGUID    unique identifier of the parent data structure.
     * @param dataFieldGUID    unique identifier of the nested data field.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachSubjectAreas(String                   serverName,
                                           String                   subjectAreaGUID,
                                           String                   dataFieldGUID,
                                           DeleteRequestBody requestBody)
    {
        final String methodName = "detachSubjectAreas";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SubjectAreaHandler handler = instanceHandler.getSubjectAreaManager(userId, serverName, methodName);

            handler.detachSubjectAreas(userId, subjectAreaGUID, dataFieldGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Delete a data structure.
     *
     * @param serverName         name of called server
     * @param subjectAreaGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteSubjectArea(String                   serverName,
                                          String                   subjectAreaGUID,
                                          DeleteRequestBody requestBody)
    {
        final String methodName = "deleteSubjectArea";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SubjectAreaHandler handler = instanceHandler.getSubjectAreaManager(userId, serverName, methodName);

            handler.deleteSubjectArea(userId, subjectAreaGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of data structure metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getSubjectAreasByName(String            serverName,
                                                                  FilterRequestBody requestBody)
    {
        final String methodName = "getSubjectAreasByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SubjectAreaHandler handler = instanceHandler.getSubjectAreaManager(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getSubjectAreasByName(userId, requestBody.getFilter(), requestBody));
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
     * Retrieve the list of data structure metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param subjectAreaGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getSubjectAreaByGUID(String             serverName,
                                                                String             subjectAreaGUID,
                                                                GetRequestBody requestBody)
    {
        final String methodName = "getSubjectAreaByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SubjectAreaHandler handler = instanceHandler.getSubjectAreaManager(userId, serverName, methodName);

            response.setElement(handler.getSubjectAreaByGUID(userId, subjectAreaGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of data structure metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findSubjectAreas(String                  serverName,
                                                             SearchStringRequestBody requestBody)
    {
        final String methodName = "findSubjectAreas";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SubjectAreaHandler handler = instanceHandler.getSubjectAreaManager(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findSubjectAreas(userId,
                                                              requestBody.getSearchString(),
                                                              requestBody));
            }
            else
            {
                response.setElements(handler.findSubjectAreas(userId, null, null));
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
