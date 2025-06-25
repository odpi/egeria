/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.subjectarea.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.SubjectAreaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateFilter;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.SubjectAreaHandler;
import org.odpi.openmetadata.frameworkservices.omf.rest.AnyTimeRequestBody;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;

import java.util.Date;


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

                if (requestBody.getProperties() instanceof SubjectAreaProperties subjectAreaProperties)
                {
                    response.setGUID(handler.createSubjectArea(userId,
                                                               requestBody.getExternalSourceGUID(),
                                                               requestBody.getExternalSourceName(),
                                                               requestBody.getAnchorGUID(),
                                                               requestBody.getIsOwnAnchor(),
                                                               requestBody.getAnchorScopeGUID(),
                                                               subjectAreaProperties,
                                                               requestBody.getParentGUID(),
                                                               requestBody.getParentRelationshipTypeName(),
                                                               requestBody.getParentRelationshipProperties(),
                                                               requestBody.getParentAtEnd1(),
                                                               requestBody.getForLineage(),
                                                               requestBody.getForDuplicateProcessing(),
                                                               requestBody.getEffectiveTime()));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setGUID(handler.createSubjectArea(userId,
                                                               requestBody.getExternalSourceGUID(),
                                                               requestBody.getExternalSourceName(),
                                                               requestBody.getAnchorGUID(),
                                                               requestBody.getIsOwnAnchor(),
                                                               requestBody.getAnchorScopeGUID(),
                                                               null,
                                                               requestBody.getParentGUID(),
                                                               requestBody.getParentRelationshipTypeName(),
                                                               requestBody.getParentRelationshipProperties(),
                                                               requestBody.getParentAtEnd1(),
                                                               requestBody.getForLineage(),
                                                               requestBody.getForDuplicateProcessing(),
                                                               requestBody.getEffectiveTime()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SubjectAreaProperties.class.getName(), methodName);
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
                                                                       requestBody.getExternalSourceGUID(),
                                                                       requestBody.getExternalSourceName(),
                                                                       requestBody.getAnchorGUID(),
                                                                       requestBody.getIsOwnAnchor(),
                                                                       requestBody.getAnchorScopeGUID(),
                                                                       null,
                                                                       null,
                                                                       requestBody.getTemplateGUID(),
                                                                       requestBody.getReplacementProperties(),
                                                                       requestBody.getPlaceholderPropertyValues(),
                                                                       requestBody.getParentGUID(),
                                                                       requestBody.getParentRelationshipTypeName(),
                                                                       requestBody.getParentRelationshipProperties(),
                                                                       requestBody.getParentAtEnd1(),
                                                                       requestBody.getForLineage(),
                                                                       requestBody.getForDuplicateProcessing(),
                                                                       requestBody.getEffectiveTime()));
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
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateSubjectArea(String                   serverName,
                                          String                   subjectAreaGUID,
                                          boolean                  replaceAllProperties,
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

                if (requestBody.getProperties() instanceof SubjectAreaProperties subjectAreaProperties)
                {
                    handler.updateSubjectArea(userId,
                                              requestBody.getExternalSourceGUID(),
                                              requestBody.getExternalSourceName(),
                                              subjectAreaGUID,
                                              replaceAllProperties,
                                              subjectAreaProperties,
                                              requestBody.getForLineage(),
                                              requestBody.getForDuplicateProcessing(),
                                              requestBody.getEffectiveTime());
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.updateSubjectArea(userId,
                                              requestBody.getExternalSourceGUID(),
                                              requestBody.getExternalSourceName(),
                                              subjectAreaGUID,
                                              replaceAllProperties,
                                              null,
                                              requestBody.getForLineage(),
                                              requestBody.getForDuplicateProcessing(),
                                              requestBody.getEffectiveTime());
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SubjectAreaProperties.class.getName(), methodName);
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
                                         RelationshipRequestBody requestBody)
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
                                         requestBody.getExternalSourceGUID(),
                                         requestBody.getExternalSourceName(),
                                         subjectAreaGUID,
                                         nestedSubjectAreaGUID,
                                         requestBody.getProperties(),
                                         requestBody.getForLineage(),
                                         requestBody.getForDuplicateProcessing(),
                                         requestBody.getEffectiveTime());
            }
            else
            {
                handler.linkSubjectAreas(userId,
                                         null,
                                         null,
                                         subjectAreaGUID,
                                         nestedSubjectAreaGUID,
                                         null,
                                         false,
                                         false,
                                         new Date());
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
    public VoidResponse detachSubjectAreas(String                    serverName,
                                           String                    subjectAreaGUID,
                                           String                    dataFieldGUID,
                                           MetadataSourceRequestBody requestBody)
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

            if (requestBody != null)
            {
                handler.detachSubjectAreas(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           subjectAreaGUID,
                                           dataFieldGUID,
                                           requestBody.getForLineage(),
                                           requestBody.getForDuplicateProcessing(),
                                           requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachSubjectAreas(userId,
                                           null,
                                           null,
                                           subjectAreaGUID,
                                           dataFieldGUID,
                                           false,
                                           false,
                                           new Date());
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
     * Delete a data structure.
     *
     * @param serverName         name of called server
     * @param subjectAreaGUID  unique identifier of the element to delete
     * @param cascadedDelete can data structures be deleted if data fields are attached?
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteSubjectArea(String                    serverName,
                                          String                    subjectAreaGUID,
                                          boolean                   cascadedDelete,
                                          MetadataSourceRequestBody requestBody)
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

            if (requestBody != null)
            {
                handler.deleteSubjectArea(userId,
                                          requestBody.getExternalSourceGUID(),
                                          requestBody.getExternalSourceName(),
                                          subjectAreaGUID,
                                          cascadedDelete,
                                          requestBody.getForLineage(),
                                          requestBody.getForDuplicateProcessing(),
                                          requestBody.getEffectiveTime());
            }
            else
            {
                handler.deleteSubjectArea(userId,
                                          null,
                                          null,
                                          subjectAreaGUID,
                                          cascadedDelete,
                                          false,
                                          false,
                                          new Date());
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
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SubjectAreasResponse getSubjectAreasByName(String            serverName,
                                                      int               startFrom,
                                                      int               pageSize,
                                                      FilterRequestBody requestBody)
    {
        final String methodName = "getSubjectAreasByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SubjectAreasResponse response = new SubjectAreasResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SubjectAreaHandler handler = instanceHandler.getSubjectAreaManager(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getSubjectAreasByName(userId,
                                                                   requestBody.getFilter(),
                                                                   requestBody.getTemplateFilter(),
                                                                   requestBody.getLimitResultsByStatus(),
                                                                   requestBody.getAsOfTime(),
                                                                   requestBody.getSequencingOrder(),
                                                                   requestBody.getSequencingProperty(),
                                                                   startFrom,
                                                                   pageSize,
                                                                   requestBody.getForLineage(),
                                                                   requestBody.getForDuplicateProcessing(),
                                                                   requestBody.getEffectiveTime()));
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
    public SubjectAreaResponse getSubjectAreaByGUID(String             serverName,
                                                    String             subjectAreaGUID,
                                                    AnyTimeRequestBody requestBody)
    {
        final String methodName = "getSubjectAreaByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SubjectAreaResponse response = new SubjectAreaResponse();
        AuditLog                      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SubjectAreaHandler handler = instanceHandler.getSubjectAreaManager(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getSubjectAreaByGUID(userId,
                                                                 subjectAreaGUID,
                                                                 requestBody.getAsOfTime(),
                                                                 requestBody.getForLineage(),
                                                                 requestBody.getForDuplicateProcessing(),
                                                                 requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElement(handler.getSubjectAreaByGUID(userId,
                                                                 subjectAreaGUID,
                                                                 null,
                                                                 false,
                                                                 false,
                                                                 new Date()));
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
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SubjectAreasResponse findSubjectAreas(String            serverName,
                                                 boolean           startsWith,
                                                 boolean           endsWith,
                                                 boolean           ignoreCase,
                                                 int               startFrom,
                                                 int               pageSize,
                                                 FilterRequestBody requestBody)
    {
        final String methodName = "findSubjectAreas";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SubjectAreasResponse response = new SubjectAreasResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SubjectAreaHandler handler = instanceHandler.getSubjectAreaManager(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findSubjectAreas(userId,
                                                              instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
                                                              requestBody.getTemplateFilter(),
                                                              requestBody.getLimitResultsByStatus(),
                                                              requestBody.getAsOfTime(),
                                                              requestBody.getSequencingOrder(),
                                                              requestBody.getSequencingProperty(),
                                                              startFrom,
                                                              pageSize,
                                                              requestBody.getForLineage(),
                                                              requestBody.getForDuplicateProcessing(),
                                                              requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElements(handler.findSubjectAreas(userId,
                                                              instanceHandler.getSearchString(null, startsWith, endsWith, ignoreCase),
                                                              TemplateFilter.ALL,
                                                              null,
                                                              null,
                                                              SequencingOrder.CREATION_DATE_RECENT,
                                                              null,
                                                              startFrom,
                                                              pageSize,
                                                              false,
                                                              false,
                                                              new Date()));
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
