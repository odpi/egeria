/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.schemamaker.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SchemaAttributeHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SchemaTypeHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaTypeProperties;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.AttributeForSchemaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.DerivedSchemaTypeQueryTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.ForeignKeyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.GraphEdgeLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.LinkedExternalSchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.MapFromElementTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.MapToElementTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.NestedSchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalDBSchemaProperties;


/**
 * The SchemaMakerRESTServices provides the server-side implementation of the Schema Maker Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class SchemaMakerRESTServices extends TokenController
{
    private static final SchemaMakerInstanceHandler instanceHandler = new SchemaMakerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(SchemaMakerRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public SchemaMakerRESTServices()
    {
    }


    /**
     * Create a schema type.
     *
     * @param serverName                 name of called server.
     * @param urlMarker  view service URL marker
     * @param requestBody             properties for the schema type.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createSchemaType(String                serverName,
                                         String                urlMarker,
                                         NewElementRequestBody requestBody)
    {
        final String methodName = "createSchemaType";

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
                SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof SchemaTypeProperties schemaTypeProperties)
                {
                    response.setGUID(handler.createSchemaType(userId,
                                                              requestBody,
                                                              requestBody.getInitialClassifications(),
                                                              schemaTypeProperties,
                                                              requestBody.getParentRelationshipProperties()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SchemaTypeProperties.class.getName(), methodName);
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
     * Create a new metadata element to represent a schema type using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param urlMarker  view service URL marker
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public GUIDResponse createSchemaTypeFromTemplate(String              serverName,
                                                     String              urlMarker,
                                                     TemplateRequestBody requestBody)
    {
        final String methodName = "createSchemaTypeFromTemplate";

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
                SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, urlMarker, methodName);

                response.setGUID(handler.createSchemaTypeFromTemplate(userId,
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
     * Update the properties of a schema type.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param schemaTypeGUID unique identifier of the schema type (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return boolean or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public BooleanResponse updateSchemaType(String                   serverName,
                                            String                   urlMarker,
                                            String                   schemaTypeGUID,
                                            UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateSchemaType";

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
                SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof SchemaTypeProperties schemaTypeProperties)
                {
                    response.setFlag(handler.updateSchemaType(userId,
                                                              schemaTypeGUID,
                                                              requestBody,
                                                              schemaTypeProperties));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SchemaTypeProperties.class.getName(), methodName);
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
     * Delete a schema type.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param schemaTypeGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteSchemaType(String                   serverName,
                                         String                   urlMarker,
                                         String                   schemaTypeGUID,
                                         DeleteElementRequestBody requestBody)
    {
        final String methodName = "deleteSchemaType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, urlMarker, methodName);

            handler.deleteSchemaType(userId, schemaTypeGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve the list of schema type metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getSchemaTypesByName(String            serverName,
                                                                 String            urlMarker,
                                                                 FilterRequestBody requestBody)
    {
        final String methodName = "getSchemaTypesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getSchemaTypesByName(userId,
                                                                  requestBody.getFilter(),
                                                                  requestBody));
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
     * Retrieve the list of schema type metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param schemaTypeGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getSchemaTypeByGUID(String             serverName,
                                                               String             urlMarker,
                                                               String             schemaTypeGUID,
                                                               GetRequestBody requestBody)
    {
        final String methodName = "getSchemaTypeByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, urlMarker, methodName);

            response.setElement(handler.getSchemaTypeByGUID(userId, schemaTypeGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve the list of schema type metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findSchemaTypes(String            serverName,
                                               String            urlMarker,
                                               SearchStringRequestBody requestBody)
    {
        final String methodName = "findSchemaTypes";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findSchemaTypes(userId, requestBody.getSearchString(), requestBody));
            }
            else
            {
                response.setElements(handler.findSchemaTypes(userId, null, null));
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
     * Create a schemaAttribute.
     *
     * @param serverName                 name of called server.
     * @param urlMarker  view service URL marker
     * @param requestBody             properties for the schema attribute.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createSchemaAttribute(String                serverName,
                                              String                urlMarker,
                                              NewElementRequestBody requestBody)
    {
        final String methodName = "createSchemaAttribute";

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
                SchemaAttributeHandler handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof SchemaAttributeProperties schemaAttributeProperties)
                {
                    response.setGUID(handler.createSchemaAttribute(userId,
                                                                   requestBody,
                                                                   requestBody.getInitialClassifications(),
                                                                   schemaAttributeProperties,
                                                                   requestBody.getParentRelationshipProperties()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SchemaAttributeProperties.class.getName(), methodName);
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
     * Create a new metadata element to represent a schemaAttribute using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param urlMarker  view service URL marker
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public GUIDResponse createSchemaAttributeFromTemplate(String              serverName,
                                                          String              urlMarker,
                                                          TemplateRequestBody requestBody)
    {
        final String methodName = "createSchemaAttributeFromTemplate";

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
                SchemaAttributeHandler handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, urlMarker, methodName);

                response.setGUID(handler.createSchemaAttributeFromTemplate(userId,
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
     * Update the properties of a schema attribute.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param schemaAttributeGUID unique identifier of the schema attribute (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public BooleanResponse updateSchemaAttribute(String                   serverName,
                                                 String                   urlMarker,
                                                 String                   schemaAttributeGUID,
                                                 UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateSchemaAttribute";

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
                SchemaAttributeHandler handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof SchemaAttributeProperties schemaAttributeProperties)
                {
                    response.setFlag(handler.updateSchemaAttribute(userId,
                                                                   schemaAttributeGUID,
                                                                   requestBody,
                                                                   schemaAttributeProperties));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SchemaAttributeProperties.class.getName(), methodName);
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
     * Delete a schema attribute.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param schemaAttributeGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteSchemaAttribute(String                   serverName,
                                              String                   urlMarker,
                                              String                   schemaAttributeGUID,
                                              DeleteElementRequestBody requestBody)
    {
        final String methodName = "deleteSchemaAttribute";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, urlMarker, methodName);

            handler.deleteSchemaAttribute(userId, schemaAttributeGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve the list of schema attribute metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getSchemaAttributesByName(String            serverName,
                                                                      String            urlMarker,
                                                                      FilterRequestBody requestBody)
    {
        final String methodName = "getSchemaAttributesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getSchemaAttributesByName(userId,
                                                                       requestBody.getFilter(),
                                                                       requestBody));
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
     * Retrieve the list of schema attribute metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param schemaAttributeGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getSchemaAttributeByGUID(String             serverName,
                                                                    String             urlMarker,
                                                                    String             schemaAttributeGUID,
                                                                    GetRequestBody requestBody)
    {
        final String methodName = "getSchemaAttributeByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, urlMarker, methodName);

            response.setElement(handler.getSchemaAttributeByGUID(userId, schemaAttributeGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve the list of schema attribute metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findSchemaAttributes(String            serverName,
                                                                 String            urlMarker,
                                                                 SearchStringRequestBody requestBody)
    {
        final String methodName = "findSchemaAttributes";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findSchemaAttributes(userId,
                                                                  requestBody.getSearchString(),
                                                                  requestBody));
            }
            else
            {
                response.setElements(handler.findSchemaAttributes(userId, null, null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }



    /*
     * =====================================================================================================================
     * Schema element relationships
     */

    /**
     * Attach a nested schema attribute to its parent schema attribute.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param schemaAttributeGUID unique identifier of the parent schema attribute
     * @param nestedSchemaAttributeGUID unique identifier of the nested schema attribute
     * @param requestBody properties for the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkNestedSchemaAttribute(String                     serverName,
                                                  String                     urlMarker,
                                                  String                     schemaAttributeGUID,
                                                  String                     nestedSchemaAttributeGUID,
                                                  NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkNestedSchemaAttribute";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                handler.linkNestedSchemaAttribute(userId, schemaAttributeGUID, nestedSchemaAttributeGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof NestedSchemaAttributeProperties properties)
            {
                handler.linkNestedSchemaAttribute(userId, schemaAttributeGUID, nestedSchemaAttributeGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkNestedSchemaAttribute(userId, schemaAttributeGUID, nestedSchemaAttributeGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(NestedSchemaAttributeProperties.class.getName(), methodName);
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
     * Detach a nested schema attribute from its parent schema attribute.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param schemaAttributeGUID unique identifier of the parent schema attribute
     * @param nestedSchemaAttributeGUID unique identifier of the nested schema attribute
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachNestedSchemaAttribute(String                        serverName,
                                                    String                        urlMarker,
                                                    String                        schemaAttributeGUID,
                                                    String                        nestedSchemaAttributeGUID,
                                                    DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachNestedSchemaAttribute";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, urlMarker, methodName);

            handler.detachNestedSchemaAttribute(userId, schemaAttributeGUID, nestedSchemaAttributeGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Attach a schema attribute to the schema type that it belongs to.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param schemaTypeGUID unique identifier of the schema type
     * @param nestedSchemaAttributeGUID unique identifier of the schema attribute
     * @param requestBody properties for the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkAttributeForSchema(String                     serverName,
                                               String                     urlMarker,
                                               String                     schemaTypeGUID,
                                               String                     nestedSchemaAttributeGUID,
                                               NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkAttributeForSchema";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                handler.linkAttributeForSchema(userId, schemaTypeGUID, nestedSchemaAttributeGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof AttributeForSchemaProperties properties)
            {
                handler.linkAttributeForSchema(userId, schemaTypeGUID, nestedSchemaAttributeGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkAttributeForSchema(userId, schemaTypeGUID, nestedSchemaAttributeGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(AttributeForSchemaProperties.class.getName(), methodName);
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
     * Detach a schema attribute from the schema type that it belongs to.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param schemaTypeGUID unique identifier of the schema type
     * @param nestedSchemaAttributeGUID unique identifier of the schema attribute
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachAttributeForSchema(String                        serverName,
                                                 String                        urlMarker,
                                                 String                        schemaTypeGUID,
                                                 String                        nestedSchemaAttributeGUID,
                                                 DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachAttributeForSchema";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, urlMarker, methodName);

            handler.detachAttributeForSchema(userId, schemaTypeGUID, nestedSchemaAttributeGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Attach a foreign key column to the primary key column that it refers to.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param primaryKeyColumnGUID unique identifier of the primary key column
     * @param foreignKeyColumnGUID unique identifier of the foreign key column
     * @param requestBody properties for the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkForeignKey(String                     serverName,
                                       String                     urlMarker,
                                       String                     primaryKeyColumnGUID,
                                       String                     foreignKeyColumnGUID,
                                       NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkForeignKey";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                handler.linkForeignKey(userId, primaryKeyColumnGUID, foreignKeyColumnGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof ForeignKeyProperties properties)
            {
                handler.linkForeignKey(userId, primaryKeyColumnGUID, foreignKeyColumnGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkForeignKey(userId, primaryKeyColumnGUID, foreignKeyColumnGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(ForeignKeyProperties.class.getName(), methodName);
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
     * Detach a foreign key column from the primary key column that it refers to.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param primaryKeyColumnGUID unique identifier of the primary key column
     * @param foreignKeyColumnGUID unique identifier of the foreign key column
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachForeignKey(String                        serverName,
                                         String                        urlMarker,
                                         String                        primaryKeyColumnGUID,
                                         String                        foreignKeyColumnGUID,
                                         DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachForeignKey";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, urlMarker, methodName);

            handler.detachForeignKey(userId, primaryKeyColumnGUID, foreignKeyColumnGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Attach an external schema type to the schema element that uses it.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param schemaElementGUID unique identifier of the schema element
     * @param schemaTypeGUID unique identifier of the external schema type
     * @param requestBody properties for the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkExternalSchemaType(String                     serverName,
                                               String                     urlMarker,
                                               String                     schemaElementGUID,
                                               String                     schemaTypeGUID,
                                               NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkExternalSchemaType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                handler.linkExternalSchemaType(userId, schemaElementGUID, schemaTypeGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof LinkedExternalSchemaTypeProperties properties)
            {
                handler.linkExternalSchemaType(userId, schemaElementGUID, schemaTypeGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkExternalSchemaType(userId, schemaElementGUID, schemaTypeGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(LinkedExternalSchemaTypeProperties.class.getName(), methodName);
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
     * Detach an external schema type from the schema element that uses it.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param schemaElementGUID unique identifier of the schema element
     * @param schemaTypeGUID unique identifier of the external schema type
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachExternalSchemaType(String                        serverName,
                                                 String                        urlMarker,
                                                 String                        schemaElementGUID,
                                                 String                        schemaTypeGUID,
                                                 DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachExternalSchemaType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, urlMarker, methodName);

            handler.detachExternalSchemaType(userId, schemaElementGUID, schemaTypeGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Attach the schema type that describes the domain (from) element of a map.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param schemaElementGUID unique identifier of the map schema element
     * @param schemaTypeGUID unique identifier of the schema type describing the domain of the map
     * @param requestBody properties for the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkMapFromSchemaType(String                     serverName,
                                              String                     urlMarker,
                                              String                     schemaElementGUID,
                                              String                     schemaTypeGUID,
                                              NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkMapFromSchemaType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                handler.linkMapFromSchemaType(userId, schemaElementGUID, schemaTypeGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof MapFromElementTypeProperties properties)
            {
                handler.linkMapFromSchemaType(userId, schemaElementGUID, schemaTypeGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkMapFromSchemaType(userId, schemaElementGUID, schemaTypeGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(MapFromElementTypeProperties.class.getName(), methodName);
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
     * Detach the schema type that describes the domain (from) element of a map.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param schemaElementGUID unique identifier of the map schema element
     * @param schemaTypeGUID unique identifier of the schema type describing the domain of the map
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachMapFromSchemaType(String                        serverName,
                                                String                        urlMarker,
                                                String                        schemaElementGUID,
                                                String                        schemaTypeGUID,
                                                DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachMapFromSchemaType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, urlMarker, methodName);

            handler.detachMapFromSchemaType(userId, schemaElementGUID, schemaTypeGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Attach the schema type that describes the range (to) element of a map.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param schemaElementGUID unique identifier of the map schema element
     * @param schemaTypeGUID unique identifier of the schema type describing the range of the map
     * @param requestBody properties for the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkMapToSchemaType(String                     serverName,
                                            String                     urlMarker,
                                            String                     schemaElementGUID,
                                            String                     schemaTypeGUID,
                                            NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkMapToSchemaType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                handler.linkMapToSchemaType(userId, schemaElementGUID, schemaTypeGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof MapToElementTypeProperties properties)
            {
                handler.linkMapToSchemaType(userId, schemaElementGUID, schemaTypeGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkMapToSchemaType(userId, schemaElementGUID, schemaTypeGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(MapToElementTypeProperties.class.getName(), methodName);
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
     * Detach the schema type that describes the range (to) element of a map.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param schemaElementGUID unique identifier of the map schema element
     * @param schemaTypeGUID unique identifier of the schema type describing the range of the map
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachMapToSchemaType(String                        serverName,
                                              String                        urlMarker,
                                              String                        schemaElementGUID,
                                              String                        schemaTypeGUID,
                                              DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachMapToSchemaType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, urlMarker, methodName);

            handler.detachMapToSchemaType(userId, schemaElementGUID, schemaTypeGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Attach a graph edge to one of the graph vertices that it connects.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param graphEdgeGUID unique identifier of the graph edge
     * @param graphVertexGUID unique identifier of the graph vertex
     * @param requestBody properties for the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkGraphEdge(String                     serverName,
                                      String                     urlMarker,
                                      String                     graphEdgeGUID,
                                      String                     graphVertexGUID,
                                      NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkGraphEdge";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                handler.linkGraphEdge(userId, graphEdgeGUID, graphVertexGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof GraphEdgeLinkProperties properties)
            {
                handler.linkGraphEdge(userId, graphEdgeGUID, graphVertexGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkGraphEdge(userId, graphEdgeGUID, graphVertexGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(GraphEdgeLinkProperties.class.getName(), methodName);
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
     * Detach a graph edge from one of the graph vertices that it connects.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param graphEdgeGUID unique identifier of the graph edge
     * @param graphVertexGUID unique identifier of the graph vertex
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachGraphEdge(String                        serverName,
                                        String                        urlMarker,
                                        String                        graphEdgeGUID,
                                        String                        graphVertexGUID,
                                        DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachGraphEdge";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, urlMarker, methodName);

            handler.detachGraphEdge(userId, graphEdgeGUID, graphVertexGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Attach a query target to the derived schema element that queries it.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param schemaElementGUID unique identifier of the derived schema element
     * @param queryTargetSchemaElementGUID unique identifier of the schema element supplying the query target
     * @param requestBody properties for the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkQueryTarget(String                     serverName,
                                        String                     urlMarker,
                                        String                     schemaElementGUID,
                                        String                     queryTargetSchemaElementGUID,
                                        NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkQueryTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                handler.linkQueryTarget(userId, schemaElementGUID, queryTargetSchemaElementGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof DerivedSchemaTypeQueryTargetProperties properties)
            {
                handler.linkQueryTarget(userId, schemaElementGUID, queryTargetSchemaElementGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkQueryTarget(userId, schemaElementGUID, queryTargetSchemaElementGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(DerivedSchemaTypeQueryTargetProperties.class.getName(), methodName);
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
     * Detach a query target from the derived schema element that queries it.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param schemaElementGUID unique identifier of the derived schema element
     * @param queryTargetSchemaElementGUID unique identifier of the schema element supplying the query target
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachQueryTarget(String                        serverName,
                                          String                        urlMarker,
                                          String                        schemaElementGUID,
                                          String                        queryTargetSchemaElementGUID,
                                          DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachQueryTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, urlMarker, methodName);

            handler.detachQueryTarget(userId, schemaElementGUID, queryTargetSchemaElementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Attach a schema type to the element that it describes.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that is described by the schema type
     * @param schemaTypeGUID unique identifier of the schema type
     * @param requestBody properties for the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkSchema(String                     serverName,
                                   String                     urlMarker,
                                   String                     elementGUID,
                                   String                     schemaTypeGUID,
                                   NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkSchema";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                handler.linkSchema(userId, elementGUID, schemaTypeGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof SchemaProperties properties)
            {
                handler.linkSchema(userId, elementGUID, schemaTypeGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkSchema(userId, elementGUID, schemaTypeGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(SchemaProperties.class.getName(), methodName);
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
     * Detach a schema type from the element that it describes.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that is described by the schema type
     * @param schemaTypeGUID unique identifier of the schema type
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachSchema(String                        serverName,
                                     String                        urlMarker,
                                     String                        elementGUID,
                                     String                        schemaTypeGUID,
                                     DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachSchema";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, urlMarker, methodName);

            handler.detachSchema(userId, elementGUID, schemaTypeGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /*
     * =====================================================================================================================
     * Relational database schema relationships
     */

    /**
     * Attach a relational database schema type to the list that contains it.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param databaseSchemaTypeListGUID unique identifier of the relational database schema type list
     * @param relationalDBSchemaTypeGUID unique identifier of the relational database schema type
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkRelationalDBSchema(String                     serverName,
                                               String                     urlMarker,
                                               String                     databaseSchemaTypeListGUID,
                                               String                     relationalDBSchemaTypeGUID,
                                               NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkRelationalDBSchema";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                handler.linkRelationalDBSchema(userId, databaseSchemaTypeListGUID, relationalDBSchemaTypeGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof RelationalDBSchemaProperties properties)
            {
                handler.linkRelationalDBSchema(userId, databaseSchemaTypeListGUID, relationalDBSchemaTypeGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkRelationalDBSchema(userId, databaseSchemaTypeListGUID, relationalDBSchemaTypeGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(RelationalDBSchemaProperties.class.getName(), methodName);
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
     * Detach a relational database schema type from the list that contained it.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param databaseSchemaTypeListGUID unique identifier of the relational database schema type list
     * @param relationalDBSchemaTypeGUID unique identifier of the relational database schema type
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachRelationalDBSchema(String                        serverName,
                                                 String                        urlMarker,
                                                 String                        databaseSchemaTypeListGUID,
                                                 String                        relationalDBSchemaTypeGUID,
                                                 DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachRelationalDBSchema";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, urlMarker, methodName);

            handler.detachRelationalDBSchema(userId, databaseSchemaTypeListGUID, relationalDBSchemaTypeGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }
}
