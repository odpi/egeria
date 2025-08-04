/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.datadesigner.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.DataClassHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.DataFieldHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.DataStructureHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.*;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;


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
    public GUIDResponse createDataStructure(String                serverName,
                                            NewElementRequestBody requestBody)
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
                DataStructureHandler handler = instanceHandler.getDataStructureHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof DataStructureProperties dataStructureProperties)
                {
                    response.setGUID(handler.createDataStructure(userId,
                                                                 requestBody,
                                                                 requestBody.getInitialClassifications(),
                                                                 dataStructureProperties,
                                                                 requestBody.getParentRelationshipProperties()));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setGUID(handler.createDataStructure(userId,
                                                                 requestBody,
                                                                 requestBody.getInitialClassifications(),
                                                                 null,
                                                                 requestBody.getParentRelationshipProperties()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DataStructureProperties.class.getName(), methodName);
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
                DataStructureHandler handler = instanceHandler.getDataStructureHandler(userId, serverName, methodName);

                response.setGUID(handler.createDataStructureFromTemplate(userId,
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
     * @param dataStructureGUID unique identifier of the data structure (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateDataStructure(String                   serverName,
                                            String                   dataStructureGUID,
                                            UpdateElementRequestBody requestBody)
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
                DataStructureHandler handler = instanceHandler.getDataStructureHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof DataStructureProperties dataStructureProperties)
                {
                    handler.updateDataStructure(userId,
                                                dataStructureGUID,
                                                requestBody,
                                                dataStructureProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.updateDataStructure(userId,
                                                dataStructureGUID,
                                                requestBody,
                                                null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DataStructureProperties.class.getName(), methodName);
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
     * @param dataStructureGUID    unique identifier of the parent data structure.
     * @param dataFieldGUID    unique identifier of the nested data field.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkMemberDataField(String                  serverName,
                                            String                  dataStructureGUID,
                                            String                  dataFieldGUID,
                                            NewRelationshipRequestBody requestBody)
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
            DataStructureHandler handler = instanceHandler.getDataStructureHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof MemberDataFieldProperties memberDataFieldProperties)
                {
                    handler.linkMemberDataField(userId,
                                                dataStructureGUID,
                                                dataFieldGUID,
                                                requestBody,
                                                memberDataFieldProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkMemberDataField(userId,
                                                dataStructureGUID,
                                                dataFieldGUID,
                                                requestBody,
                                                null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(MemberDataFieldProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkMemberDataField(userId,
                                            dataStructureGUID,
                                            dataFieldGUID,
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
     * Detach a data field from a data structure.
     *
     * @param serverName         name of called server
     * @param dataStructureGUID    unique identifier of the parent data structure.
     * @param dataFieldGUID    unique identifier of the nested data field.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachMemberDataField(String                   serverName,
                                              String                   dataStructureGUID,
                                              String                   dataFieldGUID,
                                              DeleteRequestBody requestBody)
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

            DataStructureHandler handler = instanceHandler.getDataStructureHandler(userId, serverName, methodName);

            handler.detachMemberDataField(userId, dataStructureGUID, dataFieldGUID, requestBody);
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
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteDataStructure(String                   serverName,
                                            String                   dataStructureGUID,
                                            DeleteRequestBody requestBody)
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

            DataStructureHandler handler = instanceHandler.getDataStructureHandler(userId, serverName, methodName);

            handler.deleteDataStructure(userId, dataStructureGUID, requestBody);
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
    public DataStructuresResponse getDataStructuresByName(String            serverName,
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

            DataStructureHandler handler = instanceHandler.getDataStructureHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getDataStructuresByName(userId, requestBody.getFilter(), requestBody));
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
                                                        GetRequestBody requestBody)
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

            DataStructureHandler handler = instanceHandler.getDataStructureHandler(userId, serverName, methodName);

            response.setElement(handler.getDataStructureByGUID(userId, dataStructureGUID, requestBody));
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
    public DataStructuresResponse findDataStructures(String            serverName,
                                                     SearchStringRequestBody requestBody)
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

            DataStructureHandler handler = instanceHandler.getDataStructureHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findDataStructures(userId,
                                                                requestBody.getSearchString(),
                                                                requestBody));
            }
            else
            {
                response.setElements(handler.findDataStructures(userId,
                                                                null,
                                                                null));
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
    public GUIDResponse createDataField(String                serverName,
                                        NewElementRequestBody requestBody)
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
                DataFieldHandler handler = instanceHandler.getDataFieldHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof DataFieldProperties dataFieldProperties)
                {
                    response.setGUID(handler.createDataField(userId,
                                                             requestBody,
                                                             requestBody.getInitialClassifications(),
                                                             dataFieldProperties,
                                                             requestBody.getParentRelationshipProperties()));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setGUID(handler.createDataField(userId,
                                                             requestBody,
                                                             requestBody.getInitialClassifications(),
                                                             null,
                                                             requestBody.getParentRelationshipProperties()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DataFieldProperties.class.getName(), methodName);
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
                DataFieldHandler handler = instanceHandler.getDataFieldHandler(userId, serverName, methodName);

                response.setGUID(handler.createDataFieldFromTemplate(userId,
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
     * Update the properties of a data field.
     *
     * @param serverName         name of called server.
     * @param dataFieldGUID unique identifier of the data field (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse   updateDataField(String                   serverName,
                                          String                   dataFieldGUID,
                                          UpdateElementRequestBody requestBody)
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
                DataFieldHandler handler = instanceHandler.getDataFieldHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof DataFieldProperties dataFieldProperties)
                {
                    handler.updateDataField(userId,
                                            dataFieldGUID,
                                            requestBody,
                                            dataFieldProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.updateDataField(userId,
                                            dataFieldGUID,
                                            requestBody,
                                            null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DataFieldProperties.class.getName(), methodName);
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
    public VoidResponse linkNestedDataFields(String                  serverName,
                                             String                  parentDataFieldGUID,
                                             String                  nestedDataFieldGUID,
                                             NewRelationshipRequestBody requestBody)
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
            DataFieldHandler handler = instanceHandler.getDataFieldHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof MemberDataFieldProperties memberDataFieldProperties)
                {
                    handler.linkNestedDataFields(userId,
                                                 parentDataFieldGUID,
                                                 nestedDataFieldGUID,
                                                 requestBody,
                                                 memberDataFieldProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkNestedDataFields(userId,
                                                 parentDataFieldGUID,
                                                 nestedDataFieldGUID,
                                                 requestBody,
                                                 null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(MemberDataFieldProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkNestedDataFields(userId,
                                             parentDataFieldGUID,
                                             nestedDataFieldGUID,
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
    public VoidResponse detachNestedDataFields(String                   serverName,
                                               String                   parentDataFieldGUID,
                                               String                   nestedDataFieldGUID,
                                               DeleteRequestBody requestBody)
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

            DataFieldHandler handler = instanceHandler.getDataFieldHandler(userId, serverName, methodName);

            handler.detachNestedDataFields(userId, parentDataFieldGUID, nestedDataFieldGUID, requestBody);
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
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteDataField(String                    serverName,
                                        String                    dataFieldGUID,
                                        DeleteRequestBody requestBody)
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

            DataFieldHandler handler = instanceHandler.getDataFieldHandler(userId, serverName, methodName);

            handler.deleteDataField(userId, dataFieldGUID, requestBody);
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
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataFieldsResponse getDataFieldsByName(String            serverName,
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

            DataFieldHandler handler = instanceHandler.getDataFieldHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getDataFieldsByName(userId, requestBody.getFilter(), requestBody));
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
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataFieldsResponse findDataFields(String                  serverName,
                                             SearchStringRequestBody requestBody)
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

            DataFieldHandler handler = instanceHandler.getDataFieldHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findDataFields(userId,
                                                            requestBody.getSearchString(),
                                                            requestBody));
            }
            else
            {
                response.setElements(handler.findDataFields(userId, null, null));
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
                                                GetRequestBody requestBody)
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

            DataFieldHandler handler = instanceHandler.getDataFieldHandler(userId, serverName, methodName);

            response.setElement(handler.getDataFieldByGUID(userId, dataFieldGUID, requestBody));
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
    public GUIDResponse createDataClass(String                serverName,
                                        NewElementRequestBody requestBody)
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
                DataClassHandler handler = instanceHandler.getDataClassHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof DataClassProperties dataClassProperties)
                {
                    response.setGUID(handler.createDataClass(userId,
                                                             requestBody,
                                                             requestBody.getInitialClassifications(),
                                                             dataClassProperties,
                                                             requestBody.getParentRelationshipProperties()));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setGUID(handler.createDataClass(userId,
                                                             requestBody,
                                                             requestBody.getInitialClassifications(),
                                                             null,
                                                             requestBody.getParentRelationshipProperties()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DataClassProperties.class.getName(), methodName);
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
                DataClassHandler handler = instanceHandler.getDataClassHandler(userId, serverName, methodName);

                response.setGUID(handler.createDataClassFromTemplate(userId,
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
     * Update the properties of a data class.
     *
     * @param serverName         name of called server.
     * @param dataClassGUID unique identifier of the data class (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse   updateDataClass(String                   serverName,
                                          String                   dataClassGUID,
                                          UpdateElementRequestBody requestBody)
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
                DataClassHandler handler = instanceHandler.getDataClassHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof DataClassProperties dataClassProperties)
                {
                    handler.updateDataClass(userId,
                                            dataClassGUID,
                                            requestBody,
                                            dataClassProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.updateDataClass(userId,
                                            dataClassGUID,
                                            requestBody,
                                            null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DataClassProperties.class.getName(), methodName);
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
    public VoidResponse linkNestedDataClass(String                  serverName,
                                            String                  parentDataClassGUID,
                                            String                  childDataClassGUID,
                                            NewRelationshipRequestBody requestBody)
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
            DataClassHandler handler = instanceHandler.getDataClassHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DataClassCompositionProperties dataClassCompositionProperties)
                {
                    handler.linkNestedDataClass(userId,
                                                parentDataClassGUID,
                                                childDataClassGUID,
                                                requestBody,
                                                dataClassCompositionProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkNestedDataClass(userId,
                                                parentDataClassGUID,
                                                childDataClassGUID,
                                                requestBody,
                                                null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DataClassCompositionProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkNestedDataClass(userId,
                                            parentDataClassGUID,
                                            childDataClassGUID,
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
    public VoidResponse detachNestedDataClass(String                   serverName,
                                              String                   parentDataClassGUID,
                                              String                   childDataClassGUID,
                                              DeleteRequestBody requestBody)
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

            DataClassHandler handler = instanceHandler.getDataClassHandler(userId, serverName, methodName);

            handler.detachNestedDataClass(userId, parentDataClassGUID, childDataClassGUID, requestBody);
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
    public VoidResponse linkSpecializedDataClass(String                  serverName,
                                                 String                  parentDataClassGUID,
                                                 String                  childDataClassGUID,
                                                 NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkSpecializedDataClass";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            DataClassHandler handler = instanceHandler.getDataClassHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DataClassHierarchyProperties dataClassHierarchyProperties)
                {
                    handler.linkSpecializedDataClass(userId,
                                                     parentDataClassGUID,
                                                     childDataClassGUID,
                                                     requestBody,
                                                     dataClassHierarchyProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkSpecializedDataClass(userId,
                                                     parentDataClassGUID,
                                                     childDataClassGUID,
                                                     requestBody,
                                                     null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DataClassHierarchyProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkSpecializedDataClass(userId,
                                                 parentDataClassGUID,
                                                 childDataClassGUID,
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
    public VoidResponse detachSpecializedDataClass(String                   serverName,
                                                   String                   parentDataClassGUID,
                                                   String                   childDataClassGUID,
                                                   DeleteRequestBody requestBody)
    {
        final String methodName = "detachSpecializedDataClass";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataClassHandler handler = instanceHandler.getDataClassHandler(userId, serverName, methodName);

            handler.detachSpecializedDataClass(userId, parentDataClassGUID, childDataClassGUID, requestBody);
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
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteDataClass(String                   serverName,
                                        String                   dataClassGUID,
                                        DeleteRequestBody requestBody)
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

            DataClassHandler handler = instanceHandler.getDataClassHandler(userId, serverName, methodName);

            handler.deleteDataClass(userId, dataClassGUID, requestBody);
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
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getDataClassesByName(String            serverName,
                                                                 FilterRequestBody requestBody)
    {
        final String methodName = "getDataClassesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataClassHandler handler = instanceHandler.getDataClassHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getDataClassesByName(userId, requestBody.getFilter(), requestBody));
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
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findDataClasses(String                  serverName,
                                                            SearchStringRequestBody requestBody)
    {
        final String methodName = "findDataClasses";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataClassHandler handler = instanceHandler.getDataClassHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findDataClasses(userId,
                                                             requestBody.getSearchString(),
                                                             requestBody));
            }
            else
            {
                response.setElements(handler.findDataClasses(userId,
                                                             null,
                                                             null));
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
    public OpenMetadataRootElementResponse getDataClassByGUID(String             serverName,
                                                              String             dataClassGUID,
                                                              GetRequestBody requestBody)
    {
        final String methodName = "getDataClassByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataClassHandler handler = instanceHandler.getDataClassHandler(userId, serverName, methodName);

            response.setElement(handler.getDataClassByGUID(userId, dataClassGUID, requestBody));
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
    public VoidResponse linkDataClassDefinition(String                  serverName,
                                                String                  dataDefinitionGUID,
                                                String                  dataClassGUID,
                                                NewRelationshipRequestBody requestBody)
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

            DataStructureHandler handler = instanceHandler.getDataStructureHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DataClassDefinitionProperties dataClassDefinitionProperties)
                {
                    handler.linkDataClassDefinition(userId,
                                                    dataDefinitionGUID,
                                                    dataClassGUID,
                                                    requestBody,
                                                    dataClassDefinitionProperties);
                }
                else if (requestBody.getProperties() != null)
                {
                    handler.linkDataClassDefinition(userId,
                                                    dataDefinitionGUID,
                                                    dataClassGUID,
                                                    requestBody,
                                                    null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DataClassDefinitionProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkDataClassDefinition(userId,
                                                dataDefinitionGUID,
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
    public VoidResponse detachDataClassDefinition(String                   serverName,
                                                  String                   dataDefinitionGUID,
                                                  String                   dataClassGUID,
                                                  DeleteRequestBody requestBody)
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

            DataStructureHandler handler = instanceHandler.getDataStructureHandler(userId, serverName, methodName);

            handler.detachDataClassDefinition(userId, dataDefinitionGUID, dataClassGUID, requestBody);
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
    public VoidResponse linkSemanticDefinition(String                  serverName,
                                               String                  dataDefinitionGUID,
                                               String                  glossaryTermGUID,
                                               NewRelationshipRequestBody requestBody)
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
            DataStructureHandler handler = instanceHandler.getDataStructureHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof SemanticDefinitionProperties semanticDefinitionProperties)
                {
                    handler.linkSemanticDefinition(userId,
                                                   dataDefinitionGUID,
                                                   glossaryTermGUID,
                                                   requestBody,
                                                   semanticDefinitionProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkSemanticDefinition(userId,
                                                   dataDefinitionGUID,
                                                   glossaryTermGUID,
                                                   requestBody,
                                                   null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SemanticDefinitionProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkSemanticDefinition(userId,
                                               dataDefinitionGUID,
                                               glossaryTermGUID,
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
    public VoidResponse detachSemanticDefinition(String                   serverName,
                                                 String                   dataDefinitionGUID,
                                                 String                   glossaryTermGUID,
                                                 DeleteRequestBody requestBody)
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

            DataStructureHandler handler = instanceHandler.getDataStructureHandler(userId, serverName, methodName);

            handler.detachSemanticDefinition(userId, dataDefinitionGUID, glossaryTermGUID, requestBody);
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
    public VoidResponse linkCertificationTypeToDataStructure(String                  serverName,
                                                             String                  certificationTypeGUID,
                                                             String                  dataStructureGUID,
                                                             NewRelationshipRequestBody requestBody)
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
            DataStructureHandler handler = instanceHandler.getDataStructureHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DataStructureDefinitionProperties dataStructureDefinitionProperties)
                {
                    handler.linkCertificationTypeToDataStructure(userId,
                                                                 certificationTypeGUID,
                                                                 dataStructureGUID,
                                                                 requestBody,
                                                                 dataStructureDefinitionProperties);
                }
                else if (requestBody.getProperties() != null)
                {
                    handler.linkCertificationTypeToDataStructure(userId,
                                                                 certificationTypeGUID,
                                                                 dataStructureGUID,
                                                                 requestBody,
                                                                 null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DataStructureDefinitionProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkCertificationTypeToDataStructure(userId,
                                                             certificationTypeGUID,
                                                             dataStructureGUID,
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
    public VoidResponse detachCertificationTypeToDataStructure(String                   serverName,
                                                               String                   certificationTypeGUID,
                                                               String                   dataStructureGUID,
                                                               DeleteRequestBody requestBody)
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

            DataStructureHandler handler = instanceHandler.getDataStructureHandler(userId, serverName, methodName);

            handler.detachCertificationTypeToDataStructure(userId, certificationTypeGUID, dataStructureGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
