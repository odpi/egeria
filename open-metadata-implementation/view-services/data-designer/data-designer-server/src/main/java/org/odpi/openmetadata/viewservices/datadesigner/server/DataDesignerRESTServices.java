/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.datadesigner.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.DataValueSpecificationHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.DataFieldHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.DataStructureHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.*;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;


/**
 * The DataDesignerRESTServices provides the server-side implementation of the Data Designer Open Metadata
 * View Service (OMVS).  This interface provides access to data fields, data structures and data value specifications.
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createDataStructure(String                serverName,
                                            NewElementRequestBody requestBody)
    {
        final String methodName = "createDataStructure";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDataStructureFromTemplate(String              serverName,
                                                        TemplateRequestBody requestBody)
    {
        final String methodName = "createDataStructureFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                                                                         requestBody.getReplacementClassifications(),
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Update the properties of a data structure.
     *
     * @param serverName         name of called server.
     * @param dataStructureGUID unique identifier of the data structure (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return boolean or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public BooleanResponse updateDataStructure(String                   serverName,
                                               String                   dataStructureGUID,
                                               UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateDataStructure";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

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
                    response.setFlag(handler.updateDataStructure(userId,
                                                                 dataStructureGUID,
                                                                 requestBody,
                                                                 dataStructureProperties));
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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkMemberDataField(String                     serverName,
                                            String                     dataStructureGUID,
                                            String                     dataFieldGUID,
                                            NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkMemberDataField";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachMemberDataField(String                        serverName,
                                              String                        dataStructureGUID,
                                              String                        dataFieldGUID,
                                              DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachMemberDataField";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteDataStructure(String                   serverName,
                                            String                   dataStructureGUID,
                                            DeleteElementRequestBody requestBody)
    {
        final String methodName = "deleteDataStructure";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getDataStructuresByName(String            serverName,
                                                                    FilterRequestBody requestBody)
    {
        final String methodName = "getDataStructuresByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getDataStructureByGUID(String         serverName,
                                                                  String         dataStructureGUID,
                                                                  GetRequestBody requestBody)
    {
        final String methodName = "getDataStructureByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findDataStructures(String                  serverName,
                                                               SearchStringRequestBody requestBody)
    {
        final String methodName = "findDataStructures";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createDataField(String                serverName,
                                        NewElementRequestBody requestBody)
    {
        final String methodName = "createDataField";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDataFieldFromTemplate(String              serverName,
                                                    TemplateRequestBody requestBody)
    {
        final String methodName = "createDataFieldFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                                                                     requestBody.getReplacementClassifications(),
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Update the properties of a data field.
     *
     * @param serverName         name of called server.
     * @param dataFieldGUID unique identifier of the data field (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return boolean or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public BooleanResponse updateDataField(String                   serverName,
                                           String                   dataFieldGUID,
                                           UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateDataField";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

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
                    response.setFlag(handler.updateDataField(userId,
                                                             dataFieldGUID,
                                                             requestBody,
                                                             dataFieldProperties));
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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkNestedDataFields(String                     serverName,
                                             String                     parentDataFieldGUID,
                                             String                     nestedDataFieldGUID,
                                             NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkNestedDataFields";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                if (requestBody.getProperties() instanceof NestedDataFieldProperties nestedDataFieldProperties)
                {
                    handler.linkNestedDataFields(userId,
                                                 parentDataFieldGUID,
                                                 nestedDataFieldGUID,
                                                 requestBody,
                                                 nestedDataFieldProperties);
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
                    restExceptionHandler.handleInvalidPropertiesObject(NestedDataFieldProperties.class.getName(), methodName);
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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachNestedDataFields(String                        serverName,
                                               String                        parentDataFieldGUID,
                                               String                        nestedDataFieldGUID,
                                               DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachNestedDataValueSpecification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteDataField(String                   serverName,
                                        String                   dataFieldGUID,
                                        DeleteElementRequestBody requestBody)
    {
        final String methodName = "deleteDataField";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getDataFieldsByName(String            serverName,
                                                                FilterRequestBody requestBody)
    {
        final String methodName = "getDataFieldsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findDataFields(String                  serverName,
                                                           SearchStringRequestBody requestBody)
    {
        final String methodName = "findDataFields";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getDataFieldByGUID(String         serverName,
                                                              String         dataFieldGUID,
                                                              GetRequestBody requestBody)
    {
        final String methodName = "getDataFieldByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }



    /**
     * Create a data value specification.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the data value specification.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createDataValueSpecification(String                serverName,
                                        NewElementRequestBody requestBody)
    {
        final String methodName = "createDataValueSpecification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                DataValueSpecificationHandler handler = instanceHandler.getDataValueSpecificationHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof DataValueSpecificationProperties dataValueSpecificationProperties)
                {
                    response.setGUID(handler.createDataValueSpecification(userId,
                                                                          requestBody,
                                                                          requestBody.getInitialClassifications(),
                                                                          dataValueSpecificationProperties,
                                                                          requestBody.getParentRelationshipProperties()));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setGUID(handler.createDataValueSpecification(userId,
                                                                          requestBody,
                                                                          requestBody.getInitialClassifications(),
                                                                          null,
                                                                          requestBody.getParentRelationshipProperties()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DataValueSpecificationProperties.class.getName(), methodName);
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
     * Create a new metadata element to represent a data value specification using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDataValueSpecificationFromTemplate(String              serverName,
                                                    TemplateRequestBody requestBody)
    {
        final String methodName = "createDataValueSpecificationFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                DataValueSpecificationHandler handler = instanceHandler.getDataValueSpecificationHandler(userId, serverName, methodName);

                response.setGUID(handler.createDataValueSpecificationFromTemplate(userId,
                                                                                  requestBody,
                                                                                  requestBody.getTemplateGUID(),
                                                                                  requestBody.getReplacementProperties(),
                                                                                  requestBody.getReplacementClassifications(),
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Update the properties of a data value specification.
     *
     * @param serverName         name of called server.
     * @param dataValueSpecificationGUID unique identifier of the data value specification (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return boolean or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public BooleanResponse updateDataValueSpecification(String                   serverName,
                                           String                   dataValueSpecificationGUID,
                                           UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateDataValueSpecification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        BooleanResponse response = new BooleanResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                DataValueSpecificationHandler handler = instanceHandler.getDataValueSpecificationHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof DataValueSpecificationProperties dataValueSpecificationProperties)
                {
                    response.setFlag(handler.updateDataValueSpecification(userId,
                                                             dataValueSpecificationGUID,
                                                             requestBody,
                                                             dataValueSpecificationProperties));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DataValueSpecificationProperties.class.getName(), methodName);
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
     * Connect two data classes to show that one is used by the other when it is validating (typically a complex data item).
     *
     * @param serverName         name of called server
     * @param parentDataClassGUID  unique identifier of the first data class
     * @param childDataClassGUID      unique identifier of the second data class
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkNestedDataClass(String                  serverName,
                                            String                  parentDataClassGUID,
                                            String                  childDataClassGUID,
                                            NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkNestedDataClass";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            DataValueSpecificationHandler handler = instanceHandler.getDataValueSpecificationHandler(userId, serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Detach two nested data value specifications from one another.
     *
     * @param serverName         name of called server
     * @param parentDataClassGUID  unique identifier of the first data class
     * @param childDataClassGUID      unique identifier of the second data class
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachNestedDataClass(String                        serverName,
                                              String                        parentDataClassGUID,
                                              String                        childDataClassGUID,
                                              DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachNestedDataClass";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataValueSpecificationHandler handler = instanceHandler.getDataValueSpecificationHandler(userId, serverName, methodName);

            handler.detachNestedDataClass(userId, parentDataClassGUID, childDataClassGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Connect two data value specifications to show that one provides a more specialist evaluation.
     *
     * @param serverName         name of called server
     * @param parentDataValueSpecificationGUID  unique identifier of the first data value specification
     * @param childDataValueSpecificationGUID      unique identifier of the second data value specification
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkSpecializedDataValueSpecification(String                     serverName,
                                                              String                     parentDataValueSpecificationGUID,
                                                              String                     childDataValueSpecificationGUID,
                                                              NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkSpecializedDataValueSpecification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            DataValueSpecificationHandler handler = instanceHandler.getDataValueSpecificationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DataValueHierarchyProperties dataValueHierarchyProperties)
                {
                    handler.linkSpecializedDataValueSpecification(userId,
                                                     parentDataValueSpecificationGUID,
                                                     childDataValueSpecificationGUID,
                                                     requestBody,
                                                     dataValueHierarchyProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkSpecializedDataValueSpecification(userId,
                                                     parentDataValueSpecificationGUID,
                                                     childDataValueSpecificationGUID,
                                                     requestBody,
                                                     null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DataValueHierarchyProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkSpecializedDataValueSpecification(userId,
                                                 parentDataValueSpecificationGUID,
                                                 childDataValueSpecificationGUID,
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
     * Detach two data value specifications from one another.
     *
     * @param serverName         name of called server
     * @param parentDataValueSpecificationGUID  unique identifier of the first data value specification
     * @param childDataValueSpecificationGUID      unique identifier of the second data value specification
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachSpecializedDataValueSpecification(String                        serverName,
                                                                String                        parentDataValueSpecificationGUID,
                                                                String                        childDataValueSpecificationGUID,
                                                                DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachSpecializedDataValueSpecification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataValueSpecificationHandler handler = instanceHandler.getDataValueSpecificationHandler(userId, serverName, methodName);

            handler.detachSpecializedDataValueSpecification(userId, parentDataValueSpecificationGUID, childDataValueSpecificationGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }



    /**
     * Connect an element to a data value specification that describes the data associated with this element.
     *
     * @param serverName         name of called server
     * @param elementGUID  unique identifier of the first data value specification
     * @param dataValueSpecificationGUID      unique identifier of the second data value specification
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse assignDataValueSpecification(String                     serverName,
                                                     String                     elementGUID,
                                                     String                     dataValueSpecificationGUID,
                                                     NewRelationshipRequestBody requestBody)
    {
        final String methodName = "assignDataValueSpecification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            DataValueSpecificationHandler handler = instanceHandler.getDataValueSpecificationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DataValueAssignmentProperties dataValueAssignmentProperties)
                {
                    handler.linkDataValueAssignment(userId,
                                                    elementGUID,
                                                    dataValueSpecificationGUID,
                                                    requestBody,
                                                    dataValueAssignmentProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkSpecializedDataValueSpecification(userId,
                                                                  elementGUID,
                                                                  dataValueSpecificationGUID,
                                                                  requestBody,
                                                                  null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DataValueHierarchyProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkSpecializedDataValueSpecification(userId,
                                                              elementGUID,
                                                              dataValueSpecificationGUID,
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
     * Detach an element from one of its assigned data value specifications.
     *
     * @param serverName         name of called server
     * @param elementGUID  unique identifier of the element
     * @param dataValueSpecificationGUID      unique identifier of the  data value specification
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachDataValueSpecificationAssignment(String                        serverName,
                                                                String                        elementGUID,
                                                                String                        dataValueSpecificationGUID,
                                                                DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachDataValueSpecificationAssignment";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataValueSpecificationHandler handler = instanceHandler.getDataValueSpecificationHandler(userId, serverName, methodName);

            handler.detachDataValueAssignment(userId, elementGUID, dataValueSpecificationGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Delete a data value specification.
     *
     * @param serverName         name of called server
     * @param dataValueSpecificationGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteDataValueSpecification(String                   serverName,
                                        String                   dataValueSpecificationGUID,
                                        DeleteElementRequestBody requestBody)
    {
        final String methodName = "deleteDataValueSpecification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataValueSpecificationHandler handler = instanceHandler.getDataValueSpecificationHandler(userId, serverName, methodName);

            handler.deleteDataValueSpecification(userId, dataValueSpecificationGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve the list of data value specification metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getDataValueSpecificationsByName(String            serverName,
                                                                 FilterRequestBody requestBody)
    {
        final String methodName = "getDataValueSpecificationsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataValueSpecificationHandler handler = instanceHandler.getDataValueSpecificationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getDataValueSpecificationsByName(userId, requestBody.getFilter(), requestBody));
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
     * Retrieve the list of data value specification metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findDataValueSpecifications(String                  serverName,
                                                            SearchStringRequestBody requestBody)
    {
        final String methodName = "findDataValueSpecifications";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataValueSpecificationHandler handler = instanceHandler.getDataValueSpecificationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findDataValueSpecifications(userId,
                                                             requestBody.getSearchString(),
                                                             requestBody));
            }
            else
            {
                response.setElements(handler.findDataValueSpecifications(userId,
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
     * Retrieve the list of data value specification metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param dataValueSpecificationGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getDataValueSpecificationByGUID(String         serverName,
                                                              String         dataValueSpecificationGUID,
                                                              GetRequestBody requestBody)
    {
        final String methodName = "getDataValueSpecificationByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataValueSpecificationHandler handler = instanceHandler.getDataValueSpecificationHandler(userId, serverName, methodName);

            response.setElement(handler.getDataValueSpecificationByGUID(userId, dataValueSpecificationGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Connect an element that is part of a data design to a data value specification to show that the data value specification should be used
     * as the specification for the data values when interpreting the data definition.
     *
     * @param serverName         name of called server
     * @param dataDefinitionGUID     unique identifier of the data design element (eg data field) that uses the data value specification
     * @param dataValueSpecificationGUID          unique identifier of the data value specification
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkDataValueSpecificationDefinition(String                     serverName,
                                                             String                     dataDefinitionGUID,
                                                             String                     dataValueSpecificationGUID,
                                                             NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkDataValueSpecificationDefinition";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataValueSpecificationHandler handler = instanceHandler.getDataValueSpecificationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DataValueDefinitionProperties dataValueDefinitionProperties)
                {
                    handler.linkDataValueDefinition(userId,
                                                    dataDefinitionGUID,
                                                    dataValueSpecificationGUID,
                                                    requestBody,
                                                    dataValueDefinitionProperties);
                }
                else if (requestBody.getProperties() != null)
                {
                    handler.linkDataValueDefinition(userId,
                                                    dataDefinitionGUID,
                                                    dataValueSpecificationGUID,
                                                    requestBody,
                                                    null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DataValueDefinitionProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkDataValueDefinition(userId,
                                                dataDefinitionGUID,
                                                dataValueSpecificationGUID,
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
     * Detach a data definition from a data value specification.
     *
     * @param serverName         name of called server
     * @param dataDefinitionGUID     unique identifier of the data design element (eg data field) that uses the data value specification
     * @param dataValueSpecificationGUID          unique identifier of the data value specification
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachDataValueSpecificationDefinition(String                        serverName,
                                                  String                        dataDefinitionGUID,
                                                  String                        dataValueSpecificationGUID,
                                                  DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachDataValueSpecificationDefinition";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataValueSpecificationHandler handler = instanceHandler.getDataValueSpecificationHandler(userId, serverName, methodName);

            handler.detachDataValueDefinition(userId, dataDefinitionGUID, dataValueSpecificationGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Connect an element that is part of a data design to a glossary term to show that the term should be used
     * as the semantic definition for the data values when interpreting the data definition.
     *
     * @param serverName         name of called server
     * @param dataDefinitionGUID     unique identifier of the data design element (eg data field) that uses the data value specification
     * @param glossaryTermGUID       unique identifier of the glossary term
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkSemanticDefinition(String                     serverName,
                                               String                     dataDefinitionGUID,
                                               String                     glossaryTermGUID,
                                               NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkSemanticDefinition";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Detach a data definition from a glossary term.
     *
     * @param serverName         name of called server
     * @param dataDefinitionGUID     unique identifier of the data design element (eg data field) that uses the data value specification
     * @param glossaryTermGUID       unique identifier of the glossary term
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachSemanticDefinition(String                        serverName,
                                                 String                        dataDefinitionGUID,
                                                 String                        glossaryTermGUID,
                                                 DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachSemanticDefinition";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Connect a certification type to a data structure to guide the survey action service (that checks the data
     * quality of a data resource as part of certifying it with the supplied certification type) to the definition
     * of the data structure to use as a specification of how the data should be both structured and (if
     * data value specifications are attached to the associated data fields using the DataValueSpecificationDefinition relationship)
     * contain the valid values.
     *
     * @param serverName         name of called server
     * @param certificationTypeGUID  unique identifier of the certification type
     * @param dataStructureGUID      unique identifier of the data structure
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkCertificationTypeToDataStructure(String                     serverName,
                                                             String                     certificationTypeGUID,
                                                             String                     dataStructureGUID,
                                                             NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkCertificationTypeToDataStructure";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachCertificationTypeToDataStructure(String                        serverName,
                                                               String                        certificationTypeGUID,
                                                               String                        dataStructureGUID,
                                                               DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachCertificationTypeToDataStructure";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }
}
