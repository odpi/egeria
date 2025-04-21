/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.datadesigner.server;


import org.odpi.openmetadata.accessservices.designmodel.client.DataDesignManager;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworkservices.gaf.rest.AnyTimeRequestBody;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * The DataDesignerRESTServices provides the server-side implementation of the Data Designer Open Metadata
 * View Service (OMVS).  This interface provides access to data fields, data structures and data classes.
 */
public class DataDesignerRESTServices extends TokenController
{
    private static final DataDesignerInstanceHandler instanceHandler = new DataDesignerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(DataDesignerRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public DataDesignerRESTServices()
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
    public GUIDResponse createDataStructure(String                      serverName,
                                            NewDataStructureRequestBody requestBody)
    {
        final String methodName = "createDataStructure";

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
                DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

                response.setGUID(handler.createDataStructure(userId,
                                                             requestBody.getExternalSourceGUID(),
                                                             requestBody.getExternalSourceName(),
                                                             requestBody.getAnchorGUID(),
                                                             requestBody.getIsOwnAnchor(),
                                                             requestBody.getAnchorScopeGUID(),
                                                             requestBody.getProperties(),
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
    public GUIDResponse createDataStructureFromTemplate(String              serverName,
                                                        TemplateRequestBody requestBody)
    {
        final String methodName = "createDataStructureFromTemplate";

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
                DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

                response.setGUID(handler.createDataStructureFromTemplate(userId,
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
     * @param dataStructureGUID unique identifier of the data structure (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateDataStructure(String                         serverName,
                                            String                         dataStructureGUID,
                                            boolean                        replaceAllProperties,
                                            UpdateDataStructureRequestBody requestBody)
    {
        final String methodName = "updateDataStructure";

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
                DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

                handler.updateDataStructure(userId,
                                            requestBody.getExternalSourceGUID(),
                                            requestBody.getExternalSourceName(),
                                            dataStructureGUID,
                                            replaceAllProperties,
                                            requestBody.getProperties(),
                                            requestBody.getForLineage(),
                                            requestBody.getForDuplicateProcessing(),
                                            requestBody.getEffectiveTime());
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
     * @param parentDataStructureGUID    unique identifier of the parent data structure.
     * @param nestedDataFieldGUID    unique identifier of the nested data field.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkMemberDataField(String                                serverName,
                                            String                                parentDataStructureGUID,
                                            String                                nestedDataFieldGUID,
                                            MemberDataFieldRequestBody requestBody)
    {
        final String methodName = "linkMemberDataField";

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
                DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

                handler.linkMemberDataField(userId,
                                            requestBody.getExternalSourceGUID(),
                                            requestBody.getExternalSourceName(),
                                            parentDataStructureGUID,
                                            nestedDataFieldGUID,
                                            requestBody.getProperties(),
                                            requestBody.getForLineage(),
                                            requestBody.getForDuplicateProcessing(),
                                            requestBody.getEffectiveTime());
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
     * Detach a data field from a data structure.
     *
     * @param serverName         name of called server
     * @param parentDataStructureGUID    unique identifier of the parent data structure.
     * @param nestedDataFieldGUID    unique identifier of the nested data field.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachMemberDataField(String                    serverName,
                                              String                    parentDataStructureGUID,
                                              String                    nestedDataFieldGUID,
                                              MetadataSourceRequestBody requestBody)
    {
        final String methodName = "detachMemberDataField";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.detachMemberDataField(userId,
                                              requestBody.getExternalSourceGUID(),
                                              requestBody.getExternalSourceName(),
                                              parentDataStructureGUID,
                                              nestedDataFieldGUID,
                                              requestBody.getForLineage(),
                                              requestBody.getForDuplicateProcessing(),
                                              requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachMemberDataField(userId,
                                              null,
                                              null,
                                              parentDataStructureGUID,
                                              nestedDataFieldGUID,
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
     * @param dataStructureGUID  unique identifier of the element to delete
     * @param cascadedDelete can data structures be deleted if data fields are attached?
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteDataStructure(String                    serverName,
                                            String                    dataStructureGUID,
                                            boolean                   cascadedDelete,
                                            MetadataSourceRequestBody requestBody)
    {
        final String methodName = "deleteDataStructure";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.deleteDataStructure(userId,
                                            requestBody.getExternalSourceGUID(),
                                            requestBody.getExternalSourceName(),
                                            dataStructureGUID,
                                            cascadedDelete,
                                            requestBody.getForLineage(),
                                            requestBody.getForDuplicateProcessing(),
                                            requestBody.getEffectiveTime());
            }
            else
            {
                handler.deleteDataStructure(userId,
                                            null,
                                            null,
                                            dataStructureGUID,
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
    public DataStructuresResponse getDataStructuresByName(String            serverName,
                                                          int               startFrom,
                                                          int               pageSize,
                                                          FilterRequestBody requestBody)
    {
        final String methodName = "getDataStructuresByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        DataStructuresResponse response = new DataStructuresResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getDataStructuresByName(userId,
                                                                     requestBody.getFilter(),
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
    public DataStructuresResponse findDataStructures(String            serverName,
                                                     boolean           startsWith,
                                                     boolean           endsWith,
                                                     boolean           ignoreCase,
                                                     int               startFrom,
                                                     int               pageSize,
                                                     FilterRequestBody requestBody)
    {
        final String methodName = "findDataStructures";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        DataStructuresResponse response = new DataStructuresResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findDataStructures(userId,
                                                                instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
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
                response.setElements(handler.findDataStructures(userId,
                                                                instanceHandler.getSearchString(null, startsWith, endsWith, ignoreCase),
                                                                null,
                                                                null,
                                                                SequencingOrder.CREATION_DATE_RECENT,
                                                                null,
                                                                startFrom,
                                                                pageSize,
                                                                true,
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
     * @param dataStructureGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataStructureResponse getDataStructureByGUID(String             serverName,
                                                        String             dataStructureGUID,
                                                        AnyTimeRequestBody requestBody)
    {
        final String methodName = "getDataStructureByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        DataStructureResponse response = new DataStructureResponse();
        AuditLog                      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getDataStructureByGUID(userId,
                                                                   dataStructureGUID,
                                                                   requestBody.getAsOfTime(),
                                                                   requestBody.getForLineage(),
                                                                   requestBody.getForDuplicateProcessing(),
                                                                   requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElement(handler.getDataStructureByGUID(userId,
                                                                   dataStructureGUID,
                                                                   null,
                                                                   true,
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
     * Create a data field.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the data field.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createDataField(String                               serverName,
                                        NewDataFieldRequestBody requestBody)
    {
        final String methodName = "createDataField";

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
                DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

                response.setGUID(handler.createDataField(userId,
                                                         requestBody.getExternalSourceGUID(),
                                                         requestBody.getExternalSourceName(),
                                                         requestBody.getAnchorGUID(),
                                                         requestBody.getIsOwnAnchor(),
                                                         requestBody.getAnchorScopeGUID(),
                                                         requestBody.getProperties(),
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
     * Create a new metadata element to represent a data field using an existing metadata element as a template.
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
    public GUIDResponse createDataFieldFromTemplate(String              serverName,
                                                    TemplateRequestBody requestBody)
    {
        final String methodName = "createDataFieldFromTemplate";

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
                DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

                response.setGUID(handler.createDataFieldFromTemplate(userId,
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
     * Update the properties of a data field.
     *
     * @param serverName         name of called server.
     * @param dataFieldGUID unique identifier of the data field (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse   updateDataField(String                                  serverName,
                                          String                                  dataFieldGUID,
                                          boolean                                 replaceAllProperties,
                                          UpdateDataFieldRequestBody requestBody)
    {
        final String methodName = "updateDataField";

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
                DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

                handler.updateDataField(userId,
                                        requestBody.getExternalSourceGUID(),
                                        requestBody.getExternalSourceName(),
                                        dataFieldGUID,
                                        replaceAllProperties,
                                        requestBody.getProperties(),
                                        requestBody.getForLineage(),
                                        requestBody.getForDuplicateProcessing(),
                                        requestBody.getEffectiveTime());
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
     * Connect two data field as parent and child.
     *
     * @param serverName         name of called server
     * @param parentDataFieldGUID  unique identifier of the parent data field
     * @param nestedDataFieldGUID      unique identifier of the child data field
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkNestedDataFields(String                     serverName,
                                             String                     parentDataFieldGUID,
                                             String                     nestedDataFieldGUID,
                                             MemberDataFieldRequestBody requestBody)
    {
        final String methodName = "linkNestedDataFields";

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
                DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

                handler.linkNestedDataFields(userId,
                                             requestBody.getExternalSourceGUID(),
                                             requestBody.getExternalSourceName(),
                                             parentDataFieldGUID,
                                             nestedDataFieldGUID,
                                             requestBody.getProperties(),
                                             requestBody.getForLineage(),
                                             requestBody.getForDuplicateProcessing(),
                                             requestBody.getEffectiveTime());
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
     * Detach two data fields from one another.
     *
     * @param serverName         name of called server
     * @param parentDataFieldGUID  unique identifier of the first data field
     * @param nestedDataFieldGUID      unique identifier of the second data field
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachNestedDataFields(String                    serverName,
                                               String                    parentDataFieldGUID,
                                               String                    nestedDataFieldGUID,
                                               MetadataSourceRequestBody requestBody)
    {
        final String methodName = "detachNestedDataClass";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.detachNestedDataFields(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               parentDataFieldGUID,
                                               nestedDataFieldGUID,
                                               requestBody.getForLineage(),
                                               requestBody.getForDuplicateProcessing(),
                                               requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachNestedDataFields(userId,
                                               null,
                                               null,
                                               parentDataFieldGUID,
                                               nestedDataFieldGUID,
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
     * Delete a data field.
     *
     * @param serverName         name of called server
     * @param dataFieldGUID  unique identifier of the element to delete
     * @param cascadedDelete can data fields be deleted if other data fields are attached?
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteDataField(String                    serverName,
                                        String                    dataFieldGUID,
                                        boolean                   cascadedDelete,
                                        MetadataSourceRequestBody requestBody)
    {
        final String methodName = "deleteDataField";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.deleteDataField(userId,
                                        requestBody.getExternalSourceGUID(),
                                        requestBody.getExternalSourceName(),
                                        dataFieldGUID,
                                        cascadedDelete,
                                        requestBody.getForLineage(),
                                        requestBody.getForDuplicateProcessing(),
                                        requestBody.getEffectiveTime());
            }
            else
            {
                handler.deleteDataField(userId,
                                        null,
                                        null,
                                        dataFieldGUID,
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
     * Retrieve the list of data field metadata elements that contain the search string.
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
    public DataFieldsResponse getDataFieldsByName(String            serverName,
                                                  int               startFrom,
                                                  int               pageSize,
                                                  FilterRequestBody requestBody)
    {
        final String methodName = "getDataFieldsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        DataFieldsResponse response = new DataFieldsResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getDataFieldsByName(userId,
                                                                 requestBody.getFilter(),
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
     * Retrieve the list of data field metadata elements that contain the search string.
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
    public DataFieldsResponse findDataFields(String            serverName,
                                             boolean           startsWith,
                                             boolean           endsWith,
                                             boolean           ignoreCase,
                                             int               startFrom,
                                             int               pageSize,
                                             FilterRequestBody requestBody)
    {
        final String methodName = "findDataFields";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        DataFieldsResponse response = new DataFieldsResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findDataFields(userId,
                                                            instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
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
                response.setElements(handler.findDataFields(userId,
                                                            instanceHandler.getSearchString(null, startsWith, endsWith, ignoreCase),
                                                            null,
                                                            null,
                                                            SequencingOrder.CREATION_DATE_RECENT,
                                                            null,
                                                            startFrom,
                                                            pageSize,
                                                            true,
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
     * Retrieve the list of data field metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param dataFieldGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataFieldResponse getDataFieldByGUID(String             serverName,
                                                String             dataFieldGUID,
                                                AnyTimeRequestBody requestBody)
    {
        final String methodName = "getDataFieldByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        DataFieldResponse response = new DataFieldResponse();
        AuditLog                      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getDataFieldByGUID(userId,
                                                               dataFieldGUID,
                                                               requestBody.getAsOfTime(),
                                                               requestBody.getForLineage(),
                                                               requestBody.getForDuplicateProcessing(),
                                                               requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElement(handler.getDataFieldByGUID(userId,
                                                               dataFieldGUID,
                                                               null,
                                                               true,
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
     * Create a data class.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the data class.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createDataClass(String                               serverName,
                                        NewDataClassRequestBody requestBody)
    {
        final String methodName = "createDataClass";

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
                DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

                response.setGUID(handler.createDataClass(userId,
                                                         requestBody.getExternalSourceGUID(),
                                                         requestBody.getExternalSourceName(),
                                                         requestBody.getAnchorGUID(),
                                                         requestBody.getIsOwnAnchor(),
                                                         requestBody.getAnchorScopeGUID(),
                                                         requestBody.getProperties(),
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
     * Create a new metadata element to represent a data class using an existing metadata element as a template.
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
    public GUIDResponse createDataClassFromTemplate(String              serverName,
                                                    TemplateRequestBody requestBody)
    {
        final String methodName = "createDataClassFromTemplate";

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
                DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

                response.setGUID(handler.createDataClassFromTemplate(userId,
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
     * Update the properties of a data class.
     *
     * @param serverName         name of called server.
     * @param dataClassGUID unique identifier of the data class (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse   updateDataClass(String                                  serverName,
                                          String                                  dataClassGUID,
                                          boolean                                 replaceAllProperties,
                                          UpdateDataClassRequestBody requestBody)
    {
        final String methodName = "updateDataClass";

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
                DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

                handler.updateDataClass(userId,
                                        requestBody.getExternalSourceGUID(),
                                        requestBody.getExternalSourceName(),
                                        dataClassGUID,
                                        replaceAllProperties,
                                        requestBody.getProperties(),
                                        requestBody.getForLineage(),
                                        requestBody.getForDuplicateProcessing(),
                                        requestBody.getEffectiveTime());
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
     * Connect two data classes to show that one is used by the other when it is validating (typically a complex data item).
     *
     * @param serverName         name of called server
     * @param parentDataClassGUID  unique identifier of the first data class
     * @param childDataClassGUID      unique identifier of the second data class
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkNestedDataClass(String                                serverName,
                                            String                                parentDataClassGUID,
                                            String                                childDataClassGUID,
                                            MetadataSourceRequestBody requestBody)
    {
        final String methodName = "linkNestedDataClass";

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
                DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

                handler.linkNestedDataClass(userId,
                                            requestBody.getExternalSourceGUID(),
                                            requestBody.getExternalSourceName(),
                                            parentDataClassGUID,
                                            childDataClassGUID,
                                            requestBody.getForLineage(),
                                            requestBody.getForDuplicateProcessing(),
                                            requestBody.getEffectiveTime());
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
     * Detach two nested data classes from one another.
     *
     * @param serverName         name of called server
     * @param parentDataClassGUID  unique identifier of the first data class
     * @param childDataClassGUID      unique identifier of the second data class
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachNestedDataClass(String                    serverName,
                                              String                    parentDataClassGUID,
                                              String                    childDataClassGUID,
                                              MetadataSourceRequestBody requestBody)
    {
        final String methodName = "detachNestedDataClass";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.detachNestedDataClass(userId,
                                              requestBody.getExternalSourceGUID(),
                                              requestBody.getExternalSourceName(),
                                              parentDataClassGUID,
                                              childDataClassGUID,
                                              requestBody.getForLineage(),
                                              requestBody.getForDuplicateProcessing(),
                                              requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachNestedDataClass(userId,
                                              null,
                                              null,
                                              parentDataClassGUID,
                                              childDataClassGUID,
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
     * Connect two data classes to show that one provides a more specialist evaluation.
     *
     * @param serverName         name of called server
     * @param parentDataClassGUID  unique identifier of the first data class
     * @param childDataClassGUID      unique identifier of the second data class
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkSpecialistDataClass(String                    serverName,
                                                String                    parentDataClassGUID,
                                                String                    childDataClassGUID,
                                                MetadataSourceRequestBody requestBody)
    {
        final String methodName = "linkSpecialistDataClass";

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
                DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

                handler.linkSpecialistDataClass(userId,
                                                requestBody.getExternalSourceGUID(),
                                                requestBody.getExternalSourceName(),
                                                parentDataClassGUID,
                                                childDataClassGUID,
                                                requestBody.getForLineage(),
                                                requestBody.getForDuplicateProcessing(),
                                                requestBody.getEffectiveTime());
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
     * Detach two data classes from one another.
     *
     * @param serverName         name of called server
     * @param parentDataClassGUID  unique identifier of the first data class
     * @param childDataClassGUID      unique identifier of the second data class
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachSpecialistDataClass(String                    serverName,
                                                  String                    parentDataClassGUID,
                                                  String                    childDataClassGUID,
                                                  MetadataSourceRequestBody requestBody)
    {
        final String methodName = "detachSpecialistDataClass";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.detachSpecialistDataClass(userId,
                                                  requestBody.getExternalSourceGUID(),
                                                  requestBody.getExternalSourceName(),
                                                  parentDataClassGUID,
                                                  childDataClassGUID,
                                                  requestBody.getForLineage(),
                                                  requestBody.getForDuplicateProcessing(),
                                                  requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachSpecialistDataClass(userId,
                                                  null,
                                                  null,
                                                  parentDataClassGUID,
                                                  childDataClassGUID,
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
     * Delete a data class.
     *
     * @param serverName         name of called server
     * @param dataClassGUID  unique identifier of the element to delete
     * @param cascadedDelete can data classes be deleted if linked data classes are attached?
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteDataClass(String                    serverName,
                                        String                    dataClassGUID,
                                        boolean                   cascadedDelete,
                                        MetadataSourceRequestBody requestBody)
    {
        final String methodName = "deleteDataClass";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.deleteDataClass(userId,
                                        requestBody.getExternalSourceGUID(),
                                        requestBody.getExternalSourceName(),
                                        dataClassGUID,
                                        cascadedDelete,
                                        requestBody.getForLineage(),
                                        requestBody.getForDuplicateProcessing(),
                                        requestBody.getEffectiveTime());
            }
            else
            {
                handler.deleteDataClass(userId,
                                        null,
                                        null,
                                        dataClassGUID,
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
     * Retrieve the list of data class metadata elements that contain the search string.
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
    public DataClassesResponse getDataClassesByName(String            serverName,
                                                    int               startFrom,
                                                    int               pageSize,
                                                    FilterRequestBody requestBody)
    {
        final String methodName = "getDataClassesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        DataClassesResponse response = new DataClassesResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getDataClassesByName(userId,
                                                                  requestBody.getFilter(),
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
     * Retrieve the list of data class metadata elements that contain the search string.
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
    public DataClassesResponse findDataClasses(String            serverName,
                                               boolean           startsWith,
                                               boolean           endsWith,
                                               boolean           ignoreCase,
                                               int               startFrom,
                                               int               pageSize,
                                               FilterRequestBody requestBody)
    {
        final String methodName = "findDataClasses";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        DataClassesResponse response = new DataClassesResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findDataClasses(userId,
                                                             instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
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
                response.setElements(handler.findDataClasses(userId,
                                                             instanceHandler.getSearchString(null, startsWith, endsWith, ignoreCase),
                                                             null,
                                                             null,
                                                             SequencingOrder.CREATION_DATE_RECENT,
                                                             null,
                                                             startFrom,
                                                             pageSize,
                                                             true,
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
     * Retrieve the list of data class metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param dataClassGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataClassResponse getDataClassByGUID(String             serverName,
                                                String             dataClassGUID,
                                                AnyTimeRequestBody requestBody)
    {
        final String methodName = "getDataClassByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        DataClassResponse response = new DataClassResponse();
        AuditLog                      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getDataClassByGUID(userId,
                                                               dataClassGUID,
                                                               requestBody.getAsOfTime(),
                                                               requestBody.getForLineage(),
                                                               requestBody.getForDuplicateProcessing(),
                                                               requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElement(handler.getDataClassByGUID(userId,
                                                               dataClassGUID,
                                                               null,
                                                               true,
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
     * Connect an element that is part of a data design to a data class to show that the data class should be used
     * as the specification for the data values when interpreting the data definition.
     *
     * @param serverName         name of called server
     * @param dataDefinitionGUID     unique identifier of the data design element (eg data field) that uses the data class
     * @param dataClassGUID          unique identifier of the data class
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkDataClassDefinition(String                    serverName,
                                                String                    dataDefinitionGUID,
                                                String                    dataClassGUID,
                                                MetadataSourceRequestBody requestBody)
    {
        final String methodName = "linkDataClassDefinition";

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
                DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

                handler.linkDataClassDefinition(userId,
                                                requestBody.getExternalSourceGUID(),
                                                requestBody.getExternalSourceName(),
                                                dataDefinitionGUID,
                                                dataClassGUID,
                                                requestBody.getForLineage(),
                                                requestBody.getForDuplicateProcessing(),
                                                requestBody.getEffectiveTime());
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
     * Detach a data definition from a data class.
     *
     * @param serverName         name of called server
     * @param dataDefinitionGUID     unique identifier of the data design element (eg data field) that uses the data class
     * @param dataClassGUID          unique identifier of the data class
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachDataClassDefinition(String                    serverName,
                                                  String                    dataDefinitionGUID,
                                                  String                    dataClassGUID,
                                                  MetadataSourceRequestBody requestBody)
    {
        final String methodName = "detachDataClassDefinition";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.detachDataClassDefinition(userId,
                                                  requestBody.getExternalSourceGUID(),
                                                  requestBody.getExternalSourceName(),
                                                  dataDefinitionGUID,
                                                  dataClassGUID,
                                                  requestBody.getForLineage(),
                                                  requestBody.getForDuplicateProcessing(),
                                                  requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachDataClassDefinition(userId,
                                                  null,
                                                  null,
                                                  dataDefinitionGUID,
                                                  dataClassGUID,
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
     * Connect an element that is part of a data design to a glossary term to show that the term should be used
     * as the semantic definition for the data values when interpreting the data definition.
     *
     * @param serverName         name of called server
     * @param dataDefinitionGUID     unique identifier of the data design element (eg data field) that uses the data class
     * @param glossaryTermGUID       unique identifier of the glossary term
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkSemanticDefinition(String                    serverName,
                                               String                    dataDefinitionGUID,
                                               String                    glossaryTermGUID,
                                               MetadataSourceRequestBody requestBody)
    {
        final String methodName = "linkSemanticDefinition";

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
                DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

                handler.linkSemanticDefinition(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               dataDefinitionGUID,
                                               glossaryTermGUID,
                                               requestBody.getForLineage(),
                                               requestBody.getForDuplicateProcessing(),
                                               requestBody.getEffectiveTime());
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
     * Detach a data definition from a glossary term.
     *
     * @param serverName         name of called server
     * @param dataDefinitionGUID     unique identifier of the data design element (eg data field) that uses the data class
     * @param glossaryTermGUID       unique identifier of the glossary term
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachSemanticDefinition(String                    serverName,
                                                 String                    dataDefinitionGUID,
                                                 String                    glossaryTermGUID,
                                                 MetadataSourceRequestBody requestBody)
    {
        final String methodName = "detachSemanticDefinition";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.detachSemanticDefinition(userId,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 dataDefinitionGUID,
                                                 glossaryTermGUID,
                                                 requestBody.getForLineage(),
                                                 requestBody.getForDuplicateProcessing(),
                                                 requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachSemanticDefinition(userId,
                                                 null,
                                                 null,
                                                 dataDefinitionGUID,
                                                 glossaryTermGUID,
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
     * Connect a certification type to a data structure to guide the survey action service (that checks the data
     * quality of a data resource as part of certifying it with the supplied certification type) to the definition
     * of the data structure to use as a specification of how the data should be both structured and (if
     * data classes are attached to the associated data fields using the DataClassDefinition relationship)
     * contain the valid values.
     *
     * @param serverName         name of called server
     * @param certificationTypeGUID  unique identifier of the certification type
     * @param dataStructureGUID      unique identifier of the data structure
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkCertificationTypeToDataStructure(String                    serverName,
                                                             String                    certificationTypeGUID,
                                                             String                    dataStructureGUID,
                                                             MetadataSourceRequestBody requestBody)
    {
        final String methodName = "linkCertificationTypeToDataStructure";

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
                DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

                handler.linkCertificationTypeToDataStructure(userId,
                                                             requestBody.getExternalSourceGUID(),
                                                             requestBody.getExternalSourceName(),
                                                             certificationTypeGUID,
                                                             dataStructureGUID,
                                                             requestBody.getForLineage(),
                                                             requestBody.getForDuplicateProcessing(),
                                                             requestBody.getEffectiveTime());
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
     * Detach a data structure from a certification type.
     *
     * @param serverName         name of called server
     * @param certificationTypeGUID  unique identifier of the certification type
     * @param dataStructureGUID      unique identifier of the data structure
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachCertificationTypeToDataStructure(String                    serverName,
                                                               String                    certificationTypeGUID,
                                                               String                    dataStructureGUID,
                                                               MetadataSourceRequestBody requestBody)
    {
        final String methodName = "detachCertificationTypeToDataStructure";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataDesignManager handler = instanceHandler.getDataDesignManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.detachCertificationTypeToDataStructure(userId,
                                                               requestBody.getExternalSourceGUID(),
                                                               requestBody.getExternalSourceName(),
                                                               certificationTypeGUID,
                                                               dataStructureGUID,
                                                               requestBody.getForLineage(),
                                                               requestBody.getForDuplicateProcessing(),
                                                               requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachCertificationTypeToDataStructure(userId,
                                                               null,
                                                               null,
                                                               certificationTypeGUID,
                                                               dataStructureGUID,
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
}
