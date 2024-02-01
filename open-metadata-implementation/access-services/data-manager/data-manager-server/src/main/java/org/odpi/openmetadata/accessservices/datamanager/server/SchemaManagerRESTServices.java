/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.server;


import org.odpi.openmetadata.accessservices.datamanager.converters.ElementStubConverter;
import org.odpi.openmetadata.accessservices.datamanager.ffdc.DataManagerErrorCode;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.SchemaAttributeElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.SchemaTypeElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.ValidValueSetElement;
import org.odpi.openmetadata.accessservices.datamanager.properties.DataItemSortOrder;
import org.odpi.openmetadata.accessservices.datamanager.properties.SchemaAttributeProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.SchemaTypeProperties;
import org.odpi.openmetadata.accessservices.datamanager.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaAttributeHandler;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaTypeHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ValidValuesHandler;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaAttributeBuilder;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;


/**
 * SchemaManagerRESTServices is the server-side implementation of the Data Manager OMAS's
 * support for relational topics.  It matches the SchemaManagerClient.
 */
public class SchemaManagerRESTServices
{
    private static final DataManagerInstanceHandler instanceHandler = new DataManagerInstanceHandler();
    private static final RESTCallLogger             restCallLogger  = new RESTCallLogger(LoggerFactory.getLogger(SchemaManagerRESTServices.class),
                                                                                   instanceHandler.getServiceName());

    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();
    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    /**
     * Default constructor
     */
    public SchemaManagerRESTServices()
    {
    }


    /* =====================================================================================================================
     * A schemaType is used to describe complex structures found in the schema of a data asset
     */

    /**
     * Create a new metadata element to represent a primitive schema type such as a string, integer or character.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody properties about the schema type to store
     *
     * @return unique identifier of the new schema type or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createPrimitiveSchemaType(String                         serverName,
                                                  String                         userId,
                                                  PrimitiveSchemaTypeRequestBody requestBody)
    {
        final String methodName = "createPrimitiveSchemaType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaTypeHandler<SchemaTypeElement> handler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String schemaTypeGUID = handler.addPrimitiveSchemaType(userId,
                                                                       requestBody.getExternalSourceGUID(),
                                                                       requestBody.getExternalSourceName(),
                                                                       requestBody.getQualifiedName(),
                                                                       requestBody.getDisplayName(),
                                                                       requestBody.getDescription(),
                                                                       requestBody.getVersionNumber(),
                                                                       requestBody.getIsDeprecated(),
                                                                       requestBody.getAuthor(),
                                                                       requestBody.getUsage(),
                                                                       requestBody.getEncodingStandard(),
                                                                       requestBody.getNamespace(),
                                                                       requestBody.getDataType(),
                                                                       requestBody.getDefaultValue(),
                                                                       requestBody.getAdditionalProperties(),
                                                                       requestBody.getTypeName(),
                                                                       requestBody.getExtendedProperties(),
                                                                       null,
                                                                       null,
                                                                       false,
                                                                       false,
                                                                       new Date(),
                                                                       methodName);

                handler.setVendorProperties(userId,
                                            schemaTypeGUID,
                                            requestBody.getVendorProperties(),
                                            false,
                                            false,
                                            new Date(),
                                            methodName);

                response.setGUID(schemaTypeGUID);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a new metadata element to represent a schema type that has a fixed value.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody properties about the schema type to store
     *
     * @return unique identifier of the new schema type or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createLiteralSchemaType(String                       serverName,
                                                String                       userId,
                                                LiteralSchemaTypeRequestBody requestBody)
    {
        final String methodName = "createLiteralSchemaType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaTypeHandler<SchemaTypeElement> handler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String schemaTypeGUID = handler.addLiteralSchemaType(userId,
                                                                     requestBody.getExternalSourceGUID(),
                                                                     requestBody.getExternalSourceName(),
                                                                     requestBody.getQualifiedName(),
                                                                     requestBody.getDisplayName(),
                                                                     requestBody.getDescription(),
                                                                     requestBody.getVersionNumber(),
                                                                     requestBody.getIsDeprecated(),
                                                                     requestBody.getAuthor(),
                                                                     requestBody.getUsage(),
                                                                     requestBody.getEncodingStandard(),
                                                                     requestBody.getNamespace(),
                                                                     requestBody.getDataType(),
                                                                     requestBody.getFixedValue(),
                                                                     requestBody.getAdditionalProperties(),
                                                                     requestBody.getTypeName(),
                                                                     requestBody.getExtendedProperties(),
                                                                     null,
                                                                     null,
                                                                     false,
                                                                     false,
                                                                     new Date(),
                                                                     methodName);

                handler.setVendorProperties(userId,
                                            schemaTypeGUID,
                                            requestBody.getVendorProperties(),
                                            false,
                                            false,
                                            new Date(),
                                            methodName);

                response.setGUID(schemaTypeGUID);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a new metadata element to represent a schema type that has a fixed set of values that are described by a valid value set.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param validValuesSetGUID unique identifier of the valid values set to used
     * @param requestBody properties about the schema type to store
     *
     * @return unique identifier of the new schema type or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createEnumSchemaType(String                    serverName,
                                             String                    userId,
                                             String                    validValuesSetGUID,
                                             EnumSchemaTypeRequestBody requestBody)
    {
        final String methodName = "createEnumSchemaType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaTypeHandler<SchemaTypeElement> handler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String schemaTypeGUID = handler.addEnumSchemaType(userId,
                                                                  requestBody.getExternalSourceGUID(),
                                                                  requestBody.getExternalSourceName(),
                                                                  requestBody.getQualifiedName(),
                                                                  requestBody.getDisplayName(),
                                                                  requestBody.getDescription(),
                                                                  requestBody.getVersionNumber(),
                                                                  requestBody.getIsDeprecated(),
                                                                  requestBody.getAuthor(),
                                                                  requestBody.getUsage(),
                                                                  requestBody.getEncodingStandard(),
                                                                  requestBody.getNamespace(),
                                                                  requestBody.getDataType(),
                                                                  requestBody.getDefaultValue(),
                                                                  validValuesSetGUID,
                                                                  requestBody.getAdditionalProperties(),
                                                                  requestBody.getTypeName(),
                                                                  requestBody.getExtendedProperties(),
                                                                  null,
                                                                  null,
                                                                  false,
                                                                  false,
                                                                  new Date(),
                                                                  methodName);

                handler.setVendorProperties(userId,
                                            schemaTypeGUID,
                                            requestBody.getVendorProperties(),
                                            false,
                                            false,
                                            new Date(),
                                            methodName);

                response.setGUID(schemaTypeGUID);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of valid value set metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ValidValueSetsResponse getValidValueSetByName(String          serverName,
                                                         String          userId,
                                                         NameRequestBody requestBody,
                                                         int             startFrom,
                                                         int             pageSize)
    {
        final String methodName        = "getValidValueSetByName";
        final String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ValidValueSetsResponse response = new ValidValueSetsResponse();
        AuditLog               auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ValidValuesHandler<ValidValueSetElement> handler = instanceHandler.getValidValuesSetHandler(userId, serverName, methodName);

                response.setElementList(handler.getValidValueByName(userId,
                                                                    requestBody.getName(),
                                                                    nameParameterName,
                                                                    startFrom,
                                                                    pageSize,
                                                                    false,
                                                                    false,
                                                                    instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                                    new Date(),
                                                                    methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of valid value set metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ValidValueSetsResponse findValidValueSet(String                  serverName,
                                                    String                  userId,
                                                    SearchStringRequestBody requestBody,
                                                    int                     startFrom,
                                                    int                     pageSize)
    {
        final String methodName                = "findValidValueSet";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ValidValueSetsResponse response = new ValidValueSetsResponse();
        AuditLog               auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ValidValuesHandler<ValidValueSetElement> handler = instanceHandler.getValidValuesSetHandler(userId, serverName, methodName);

                response.setElementList(handler.findValidValues(userId,
                                                                requestBody.getSearchString(),
                                                                searchStringParameterName,
                                                                startFrom,
                                                                pageSize,
                                                                false,
                                                                false,
                                                                instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                                new Date(),
                                                                methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a new metadata element to represent a schema type.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody properties about the schema type to store
     *
     * @return unique identifier of the new schema type or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createStructSchemaType(String                      serverName,
                                               String                      userId,
                                               StructSchemaTypeRequestBody requestBody)
    {
        final String methodName = "createStructSchemaType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaTypeHandler<SchemaTypeElement> handler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String schemaTypeGUID = handler.addStructSchemaType(userId,
                                                                    requestBody.getExternalSourceGUID(),
                                                                    requestBody.getExternalSourceName(),
                                                                    requestBody.getQualifiedName(),
                                                                    requestBody.getDisplayName(),
                                                                    requestBody.getDescription(),
                                                                    requestBody.getVersionNumber(),
                                                                    requestBody.getIsDeprecated(),
                                                                    requestBody.getAuthor(),
                                                                    requestBody.getUsage(),
                                                                    requestBody.getEncodingStandard(),
                                                                    requestBody.getNamespace(),
                                                                    requestBody.getAdditionalProperties(),
                                                                    requestBody.getTypeName(),
                                                                    requestBody.getExtendedProperties(),
                                                                    null,
                                                                    null,
                                                                    false,
                                                                    false,
                                                                    new Date(),
                                                                    methodName);

                handler.setVendorProperties(userId,
                                            schemaTypeGUID,
                                            requestBody.getVendorProperties(),
                                            false,
                                            false,
                                            new Date(),
                                            methodName);

                response.setGUID(schemaTypeGUID);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a new metadata element to represent a list of possible schema types that can be used for the attached schema attribute.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody properties about the schema type to store
     *
     * @return unique identifier of the new schema type or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createSchemaTypeChoice(String                      serverName,
                                               String                      userId,
                                               SchemaTypeChoiceRequestBody requestBody)
    {
        final String methodName = "createSchemaTypeChoice";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaTypeHandler<SchemaTypeElement> handler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String schemaTypeGUID = handler.addSchemaTypeChoice(userId,
                                                                    requestBody.getExternalSourceGUID(),
                                                                    requestBody.getExternalSourceName(),
                                                                    requestBody.getQualifiedName(),
                                                                    requestBody.getDisplayName(),
                                                                    requestBody.getDescription(),
                                                                    requestBody.getVersionNumber(),
                                                                    requestBody.getIsDeprecated(),
                                                                    requestBody.getAuthor(),
                                                                    requestBody.getUsage(),
                                                                    requestBody.getEncodingStandard(),
                                                                    requestBody.getNamespace(),
                                                                    requestBody.getAdditionalProperties(),
                                                                    requestBody.getTypeName(),
                                                                    requestBody.getExtendedProperties(),
                                                                    null,
                                                                    null,
                                                                    false,
                                                                    false,
                                                                    new Date(),
                                                                    methodName);

                handler.setVendorProperties(userId,
                                            schemaTypeGUID,
                                            requestBody.getVendorProperties(),
                                            false,
                                            false,
                                            new Date(),
                                            methodName);

                response.setGUID(schemaTypeGUID);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a new metadata element to represent a schema type.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param mapFromSchemaTypeGUID unique identifier of the domain of the map
     * @param mapToSchemaTypeGUID unique identifier of the range of the map
     * @param requestBody properties about the schema type to store
     *
     * @return unique identifier of the new schema type or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createMapSchemaType(String                   serverName,
                                            String                   userId,
                                            String                   mapFromSchemaTypeGUID,
                                            String                   mapToSchemaTypeGUID,
                                            MapSchemaTypeRequestBody requestBody)
    {
        final String methodName = "createMapSchemaType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaTypeHandler<SchemaTypeElement> handler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String schemaTypeGUID = handler.addMapSchemaType(userId,
                                                                 requestBody.getExternalSourceGUID(),
                                                                 requestBody.getExternalSourceName(),
                                                                 requestBody.getQualifiedName(),
                                                                 requestBody.getDisplayName(),
                                                                 requestBody.getDescription(),
                                                                 requestBody.getVersionNumber(),
                                                                 requestBody.getIsDeprecated(),
                                                                 requestBody.getAuthor(),
                                                                 requestBody.getUsage(),
                                                                 requestBody.getEncodingStandard(),
                                                                 requestBody.getNamespace(),
                                                                 mapFromSchemaTypeGUID,
                                                                 mapToSchemaTypeGUID,
                                                                 requestBody.getAdditionalProperties(),
                                                                 requestBody.getTypeName(),
                                                                 requestBody.getExtendedProperties(),
                                                                 null,
                                                                 null,
                                                                 false,
                                                                 false,
                                                                 new Date(),
                                                                 methodName);

                handler.setVendorProperties(userId,
                                            schemaTypeGUID,
                                            requestBody.getVendorProperties(),
                                            false,
                                            false,
                                            new Date(),
                                            methodName);

                response.setGUID(schemaTypeGUID);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a new metadata element to represent a schema type using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new schema type or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createSchemaTypeFromTemplate(String              serverName,
                                                     String              userId,
                                                     String              templateGUID,
                                                     TemplateRequestBody requestBody)
    {
        final String methodName = "createSchemaTypeFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaTypeHandler<SchemaTypeElement> handler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String schemaTypeGUID = handler.createSchemaTypeFromTemplate(userId,
                                                                             requestBody.getExternalSourceGUID(),
                                                                             requestBody.getExternalSourceName(),
                                                                             templateGUID,
                                                                             requestBody.getQualifiedName(),
                                                                             requestBody.getDisplayName(),
                                                                             requestBody.getDescription(),
                                                                             methodName);

                response.setGUID(schemaTypeGUID);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the metadata element representing a schema type.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateSchemaType(String                serverName,
                                         String                userId,
                                         String                schemaTypeGUID,
                                         boolean               isMergeUpdate,
                                         SchemaTypeRequestBody requestBody)
    {
        final String methodName = "updateSchemaType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaTypeHandler<SchemaTypeElement> handler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.updateSchemaType(userId,
                                         requestBody.getExternalSourceGUID(),
                                         requestBody.getExternalSourceName(),
                                         schemaTypeGUID,
                                         requestBody.getQualifiedName(),
                                         requestBody.getDisplayName(),
                                         requestBody.getDescription(),
                                         requestBody.getVersionNumber(),
                                         requestBody.getIsDeprecated(),
                                         requestBody.getAuthor(),
                                         requestBody.getUsage(),
                                         requestBody.getEncodingStandard(),
                                         requestBody.getNamespace(),
                                         requestBody.getAdditionalProperties(),
                                         requestBody.getTypeName(),
                                         requestBody.getExtendedProperties(),
                                         null,
                                         null,
                                         isMergeUpdate,
                                         false,
                                         false,
                                         new Date(),
                                         methodName);

                if ((!isMergeUpdate) || (requestBody.getVendorProperties() != null))
                {
                    handler.setVendorProperties(userId,
                                                schemaTypeGUID,
                                                requestBody.getVendorProperties(),
                                                false,
                                                false,
                                                new Date(),
                                                methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the metadata element representing a schema type.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the metadata element to remove
     * @param requestBody optional identifiers for the external source
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeSchemaType(String                    serverName,
                                         String                    userId,
                                         String                    schemaTypeGUID,
                                         MetadataSourceRequestBody requestBody)
    {
        final String methodName = "removeSchemaType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaTypeHandler<SchemaTypeElement> handler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeSchemaType(userId,
                                         requestBody.getExternalSourceGUID(),
                                         requestBody.getExternalSourceName(),
                                         schemaTypeGUID,
                                         false,
                                         false,
                                         new Date(),
                                         methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a relationship between two schema elements.  The name of the desired relationship, and any properties (including effectivity dates)
     * are passed on the API.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param endOneGUID unique identifier of the schema element at end one of the relationship
     * @param endTwoGUID unique identifier of the schema element at end two of the relationship
     * @param relationshipTypeName type of the relationship to delete
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupSchemaElementRelationship(String                  serverName,
                                                       String                  userId,
                                                       String                  endOneGUID,
                                                       String                  relationshipTypeName,
                                                       String                  endTwoGUID,
                                                       RelationshipRequestBody requestBody)
    {
        final String methodName                    = "setupSchemaElementRelationship";
        final String endOneParameterName           = "endOneGUID";
        final String endTwoParameterName           = "endTwoGUID";
        final String relationshipTypeParameterName = "relationshipTypeName";
        final String propertiesParameterName       = "properties";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaTypeHandler<SchemaTypeElement> handler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

            invalidParameterHandler.validateName(relationshipTypeName, relationshipTypeParameterName, methodName);

            String relationshipTypeGUID = invalidParameterHandler.validateTypeName(relationshipTypeName,
                                                                                   null,
                                                                                   instanceHandler.getServiceName(),
                                                                                   methodName,
                                                                                   instanceHandler.getRepositoryHelper(userId, serverName, methodName));

            if (requestBody != null)
            {

                if (requestBody.getProperties() != null)
                {
                    InstanceProperties instanceProperties = null;

                    if (! requestBody.getProperties().getExtendedProperties().isEmpty())
                    {
                        try
                        {
                            instanceProperties = instanceHandler.getRepositoryHelper(userId, serverName, methodName).addPropertyMapToInstance(
                                    instanceHandler.getServiceName(),
                                    null,
                                    requestBody.getProperties().getExtendedProperties(),
                                    methodName);
                        }
                        catch (Exception badPropertyException)
                        {
                            throw new InvalidParameterException(DataManagerErrorCode.BAD_PARAMETER.getMessageDefinition(relationshipTypeName,
                                                                                                                        badPropertyException.getClass().getName(),
                                                                                                                        badPropertyException.getMessage()),
                                                                this.getClass().getName(),
                                                                methodName,
                                                                badPropertyException,
                                                                propertiesParameterName);
                        }
                    }

                    handler.linkElementToElement(userId,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 endOneGUID,
                                                 endOneParameterName,
                                                 OpenMetadataType.SCHEMA_ELEMENT_TYPE_NAME,
                                                 endTwoGUID,
                                                 endTwoParameterName,
                                                 OpenMetadataType.SCHEMA_ELEMENT_TYPE_NAME,
                                                 false,
                                                 false,
                                                 relationshipTypeGUID,
                                                 relationshipTypeName,
                                                 instanceProperties,
                                                 requestBody.getProperties().getEffectiveFrom(),
                                                 requestBody.getProperties().getEffectiveTo(),
                                                 new Date(),
                                                 methodName);
                }
                else
                {
                    handler.linkElementToElement(userId,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 endOneGUID,
                                                 endOneParameterName,
                                                 OpenMetadataType.SCHEMA_ELEMENT_TYPE_NAME,
                                                 endTwoGUID,
                                                 endTwoParameterName,
                                                 OpenMetadataType.SCHEMA_ELEMENT_TYPE_NAME,
                                                 false,
                                                 false,
                                                 relationshipTypeGUID,
                                                 relationshipTypeName,
                                                 (InstanceProperties)null,
                                                 null,
                                                 null,
                                                 new Date(),
                                                 methodName);
                }
            }
            else
            {
                handler.linkElementToElement(userId,
                                             null,
                                             null,
                                             endOneGUID,
                                             endOneParameterName,
                                             OpenMetadataType.SCHEMA_ELEMENT_TYPE_NAME,
                                             endTwoGUID,
                                             endTwoParameterName,
                                             OpenMetadataType.SCHEMA_ELEMENT_TYPE_NAME,
                                             false,
                                             false,
                                             relationshipTypeGUID,
                                             relationshipTypeName,
                                             (InstanceProperties)null,
                                             null,
                                             null,
                                             new Date(),
                                             methodName);            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove a relationship between two schema elements.  The name of the desired relationship is passed on the API.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param endOneGUID unique identifier of the schema element at end one of the relationship
     * @param endTwoGUID unique identifier of the schema element at end two of the relationship
     * @param relationshipTypeName type of the relationship to delete
     * @param requestBody optional identifiers for the external source
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearSchemaElementRelationship(String                    serverName,
                                                       String                    userId,
                                                       String                    endOneGUID,
                                                       String                    relationshipTypeName,
                                                       String                    endTwoGUID,
                                                       MetadataSourceRequestBody requestBody)
    {
        final String methodName                    = "clearSchemaElementRelationship";
        final String endOneParameterName           = "endOneGUID";
        final String endTwoParameterName           = "endTwoGUID";
        final String relationshipTypeParameterName = "relationshipTypeName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaTypeHandler<SchemaTypeElement> handler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

            invalidParameterHandler.validateName(relationshipTypeName, relationshipTypeParameterName, methodName);

            String relationshipTypeGUID = invalidParameterHandler.validateTypeName(relationshipTypeName,
                                                                                   null,
                                                                                   instanceHandler.getServiceName(),
                                                                                   methodName,
                                                                                   instanceHandler.getRepositoryHelper(userId, serverName, methodName));
            if (requestBody != null)
            {
                handler.unlinkElementFromElement(userId,
                                                 false,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 endOneGUID,
                                                 endOneParameterName,
                                                 OpenMetadataType.SCHEMA_ELEMENT_TYPE_NAME,
                                                 endTwoGUID,
                                                 endTwoParameterName,
                                                 OpenMetadataType.SCHEMA_ELEMENT_TYPE_GUID,
                                                 OpenMetadataType.SCHEMA_ELEMENT_TYPE_NAME,
                                                 false,
                                                 false,
                                                 relationshipTypeGUID,
                                                 relationshipTypeName,
                                                 new Date(),
                                                 methodName);
            }
            else
            {
                handler.unlinkElementFromElement(userId,
                                                 false,
                                                 null,
                                                 null,
                                                 endOneGUID,
                                                 endOneParameterName,
                                                 OpenMetadataType.SCHEMA_ELEMENT_TYPE_NAME,
                                                 endTwoGUID,
                                                 endTwoParameterName,
                                                 OpenMetadataType.SCHEMA_ELEMENT_TYPE_GUID,
                                                 OpenMetadataType.SCHEMA_ELEMENT_TYPE_NAME,
                                                 false,
                                                 false,
                                                 relationshipTypeGUID,
                                                 relationshipTypeName,
                                                 new Date(),
                                                 methodName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of schema type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param typeName optional type name for the schema type - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypesResponse findSchemaType(String                  serverName,
                                              String                  userId,
                                              String                  typeName,
                                              SearchStringRequestBody requestBody,
                                              int                     startFrom,
                                              int                     pageSize)
    {
        final String methodName                = "findSchemaType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SchemaTypesResponse response = new SchemaTypesResponse();
        AuditLog            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaTypeHandler<SchemaTypeElement> handler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

                List<SchemaTypeElement> results = handler.findSchemaTypes(userId,
                                                                          typeName,
                                                                          requestBody.getSearchString(),
                                                                          startFrom,
                                                                          pageSize,
                                                                          false,
                                                                          false,
                                                                          new Date(),
                                                                          methodName);

                response.setElementList(setUpVendorProperties(userId, results, handler, methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the schema type associated with a specific open metadata element (data asset, process or port).
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is connected to
     *
     * @return metadata element describing the schema type associated with the requested parent element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeResponse getSchemaTypeForElement(String serverName,
                                                      String userId,
                                                      String parentElementGUID,
                                                      String parentElementTypeName)
    {
        final String methodName                     = "findSchemaType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SchemaTypeResponse response = new SchemaTypeResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaTypeHandler<SchemaTypeElement> handler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

            SchemaTypeElement result = handler.getSchemaTypeForElement(userId,
                                                                       parentElementGUID,
                                                                       parentElementTypeName,
                                                                       false,
                                                                       false,
                                                                       new Date(),
                                                                       methodName);

            response.setElement(setUpVendorProperties(userId, result, handler, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of schema type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody name to search for
     * @param typeName optional type name for the schema type - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypesResponse getSchemaTypeByName(String          serverName,
                                                   String          userId,
                                                   String          typeName,
                                                   NameRequestBody requestBody,
                                                   int             startFrom,
                                                   int             pageSize)
    {
        final String methodName = "getSchemaTypeByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SchemaTypesResponse response = new SchemaTypesResponse();
        AuditLog            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaTypeHandler<SchemaTypeElement> handler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

                List<SchemaTypeElement> results = handler.getSchemaTypeByName(userId,
                                                                              typeName,
                                                                              requestBody.getName(),
                                                                              startFrom,
                                                                              pageSize,
                                                                              false,
                                                                              false,
                                                                              new Date(),
                                                                              methodName);

                response.setElementList(setUpVendorProperties(userId, results, handler, methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the schema type metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeResponse getSchemaTypeByGUID(String serverName,
                                                  String userId,
                                                  String schemaTypeGUID)
    {
        final String methodName = "getSchemaTypeByGUID";
        final String guidParameterName = "schemaTypeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SchemaTypeResponse response = new SchemaTypeResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaTypeHandler<SchemaTypeElement> handler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

            SchemaTypeElement result = handler.getSchemaType(userId,
                                                             schemaTypeGUID,
                                                             guidParameterName,
                                                             false,
                                                             false,
                                                             new Date(),
                                                             methodName);

            response.setElement(setUpVendorProperties(userId, result, handler, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the header of the metadata element connected to a schema type.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the requested metadata element
     *
     * @return header for parent element (data asset, process, port) plus qualified name or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ElementStubResponse getSchemaTypeParent(String serverName,
                                                   String userId,
                                                   String schemaTypeGUID)
    {
        final String methodName = "getSchemaTypeByGUID";
        final String guidParameterName = "schemaTypeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ElementStubResponse response = new ElementStubResponse();
        AuditLog            auditLog = null;

        try
        {
            ElementStub parent = null;

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaTypeHandler<SchemaTypeElement> handler   = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);
            ElementStubConverter<ElementStub>    converter = instanceHandler.getElementStubConverter(userId, serverName, methodName);

            List<Relationship> relationships = handler.getAttachmentLinks(userId,
                                                                          schemaTypeGUID,
                                                                          guidParameterName,
                                                                          OpenMetadataType.SCHEMA_TYPE_TYPE_NAME,
                                                                          OpenMetadataType.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                                                                          OpenMetadataType.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                                                                          null,
                                                                          OpenMetadataType.ASSET.typeName,
                                                                          1,
                                                                          false,
                                                                          false,
                                                                          0,
                                                                          2,
                                                                          new Date(),
                                                                          methodName);
            if (relationships != null)
            {
                parent = converter.getNewBean(ElementStub.class, relationships.get(0), true, methodName);
            }
            else
            {
                relationships = handler.getAttachmentLinks(userId,
                                                           schemaTypeGUID,
                                                           guidParameterName,
                                                           OpenMetadataType.SCHEMA_TYPE_TYPE_NAME,
                                                           OpenMetadataType.PORT_SCHEMA_RELATIONSHIP_TYPE_GUID,
                                                           OpenMetadataType.PORT_SCHEMA_RELATIONSHIP_TYPE_NAME,
                                                           null,
                                                           OpenMetadataType.PORT_TYPE_NAME,
                                                           1,
                                                           false,
                                                           false,
                                                           0,
                                                           2,
                                                           new Date(),
                                                           methodName);

                if (relationships != null)
                {
                    parent = converter.getNewBean(ElementStub.class, relationships.get(0), true, methodName);
                }
            }

            response.setElement(parent);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }


        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /* ===============================================================================
     * A schemaType typically contains many schema attributes, linked with relationships.
     */

    /**
     * Create a new metadata element to represent a schema attribute.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the schema attribute is nested underneath
     * @param requestBody properties for the schema attribute
     *
     * @return unique identifier of the new metadata element for the schema attribute or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createSchemaAttribute(String                     serverName,
                                              String                     userId,
                                              String                     schemaElementGUID,
                                              SchemaAttributeRequestBody requestBody)
    {
        final String methodName                     = "createSchemaAttribute";
        final String schemaElementGUIDParameterName = "schemaElementGUID";
        final String qualifiedNameParameterName     = "qualifiedName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler<SchemaAttributeElement,
                                   SchemaTypeElement> handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                int sortOrder = DataItemSortOrder.UNKNOWN.getOpenTypeOrdinal();

                if (requestBody.getSortOrder() != null)
                {
                    sortOrder = requestBody.getSortOrder().getOpenTypeOrdinal();
                }

                String schemaAttributeGUID = handler.createNestedSchemaAttribute(userId,
                                                                                 requestBody.getExternalSourceGUID(),
                                                                                 requestBody.getExternalSourceName(),
                                                                                 schemaElementGUID,
                                                                                 schemaElementGUIDParameterName,
                                                                                 requestBody.getQualifiedName(),
                                                                                 qualifiedNameParameterName,
                                                                                 requestBody.getDisplayName(),
                                                                                 requestBody.getDescription(),
                                                                                 requestBody.getExternalTypeGUID(),
                                                                                 requestBody.getDataType(),
                                                                                 requestBody.getDefaultValue(),
                                                                                 requestBody.getFixedValue(),
                                                                                 requestBody.getValidValuesSetGUID(),
                                                                                 null,
                                                                                 requestBody.getIsDeprecated(),
                                                                                 requestBody.getElementPosition(),
                                                                                 requestBody.getMinCardinality(),
                                                                                 requestBody.getMaxCardinality(),
                                                                                 requestBody.getAllowsDuplicateValues(),
                                                                                 requestBody.getOrderedValues(),
                                                                                 requestBody.getDefaultValueOverride(),
                                                                                 sortOrder,
                                                                                 requestBody.getMinimumLength(),
                                                                                 requestBody.getLength(),
                                                                                 requestBody.getPrecision(),
                                                                                 requestBody.getIsNullable(),
                                                                                 requestBody.getNativeJavaClass(),
                                                                                 requestBody.getAliases(),
                                                                                 requestBody.getAdditionalProperties(),
                                                                                 requestBody.getTypeName(),
                                                                                 requestBody.getExtendedProperties(),
                                                                                 null,
                                                                                 null,
                                                                                 false,
                                                                                 false,
                                                                                 new Date(),
                                                                                 methodName);

                if ((schemaAttributeGUID != null) && (requestBody.getVendorProperties() != null))
                {
                    handler.setVendorProperties(userId,
                                                schemaAttributeGUID,
                                                requestBody.getVendorProperties(),
                                                false,
                                                false,
                                                new Date(),
                                                methodName);
                }

                response.setGUID(schemaAttributeGUID);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a new metadata element to represent a schema attribute using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the schema attribute is connected to
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element for the schema attribute or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createSchemaAttributeFromTemplate(String              serverName,
                                                          String              userId,
                                                          String              schemaElementGUID,
                                                          String              templateGUID,
                                                          TemplateRequestBody requestBody)
    {
        final String methodName                     = "createSchemaAttributeFromTemplate";
        final String schemaElementGUIDParameterName = "schemaElementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler<SchemaAttributeElement,
                                          SchemaTypeElement> handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String schemaTypeGUID = handler.createSchemaAttributeFromTemplate(userId,
                                                                                  requestBody.getExternalSourceGUID(),
                                                                                  requestBody.getExternalSourceName(),
                                                                                  schemaElementGUID,
                                                                                  schemaElementGUIDParameterName,
                                                                                  templateGUID,
                                                                                  requestBody.getQualifiedName(),
                                                                                  requestBody.getDisplayName(),
                                                                                  requestBody.getDescription(),
                                                                                  false,
                                                                                  false,
                                                                                  new Date(),
                                                                                  methodName);

                response.setGUID(schemaTypeGUID);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Connect a schema type to a schema attribute.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param relationshipTypeName name of relationship to create
     * @param schemaAttributeGUID unique identifier of the schema attribute
     * @param schemaTypeGUID unique identifier of the schema type to connect
     * @param requestBody unique identifier of software server capability representing the caller
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupSchemaType(String                    serverName,
                                        String                    userId,
                                        String                    relationshipTypeName,
                                        String                    schemaAttributeGUID,
                                        String                    schemaTypeGUID,
                                        MetadataSourceRequestBody requestBody)
    {
        final String methodName                        = "setupSchemaType";
        final String schemaAttributeGUIDParameterName  = "schemaAttributeGUID";
        final String schemaTypeGUIDParameterName       = "schemaTypeGUID";
        final String relationshipTypeNameParameterName = "relationshipTypeName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler<SchemaAttributeElement,
                                          SchemaTypeElement> handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (relationshipTypeName != null)
                {
                    TypeDef relationshipTypeDef = handler.getRepositoryHelper().getTypeDefByName(instanceHandler.getServiceName(), relationshipTypeName);

                    if (relationshipTypeDef != null)
                    {
                        handler.linkElementToElement(userId,
                                                     requestBody.getExternalSourceGUID(),
                                                     requestBody.getExternalSourceName(),
                                                     schemaAttributeGUID,
                                                     schemaAttributeGUIDParameterName,
                                                     OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                     schemaTypeGUID,
                                                     schemaTypeGUIDParameterName,
                                                     OpenMetadataType.SCHEMA_TYPE_TYPE_NAME,
                                                     false,
                                                     false,
                                                     relationshipTypeDef.getGUID(),
                                                     relationshipTypeDef.getName(),
                                                     (InstanceProperties) null,
                                                     null,
                                                     null,
                                                     new Date(),
                                                     methodName);
                    }
                }
                else
                {
                    restExceptionHandler.handleMissingValue(relationshipTypeNameParameterName, methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the linked schema types from a schema attribute.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param schemaAttributeGUID unique identifier of the schema attribute
     * @param requestBody unique identifier of software server capability representing the caller
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearSchemaTypes(String                    serverName,
                                         String                    userId,
                                         String                    schemaAttributeGUID,
                                         MetadataSourceRequestBody requestBody)
    {
        final String methodName                       = "clearSchemaTypes";
        final String schemaAttributeGUIDParameterName = "schemaAttributeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler<SchemaAttributeElement,
                                          SchemaTypeElement> handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeSchemaTypes(userId,
                                          requestBody.getExternalSourceGUID(),
                                          requestBody.getExternalSourceName(),
                                          schemaAttributeGUID,
                                          schemaAttributeGUIDParameterName,
                                          false,
                                          false,
                                          new Date(),
                                          methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the properties of the metadata element representing a schema attribute.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param schemaAttributeGUID unique identifier of the schema attribute to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param requestBody new properties for the schema attribute
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateSchemaAttribute(String                     serverName,
                                              String                     userId,
                                              String                     schemaAttributeGUID,
                                              boolean                    isMergeUpdate,
                                              SchemaAttributeRequestBody requestBody)
    {
        final String methodName                  = "updateSchemaAttribute";
        final String elementGUIDParameterName    = "schemaAttributeGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler<SchemaAttributeElement,
                                          SchemaTypeElement> handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                int sortOrder = DataItemSortOrder.UNKNOWN.getOpenTypeOrdinal();

                if (requestBody.getSortOrder() != null)
                {
                    sortOrder = requestBody.getSortOrder().getOpenTypeOrdinal();
                }

                handler.updateSchemaAttribute(userId,
                                              requestBody.getExternalSourceGUID(),
                                              requestBody.getExternalSourceName(),
                                              schemaAttributeGUID,
                                              elementGUIDParameterName,
                                              requestBody.getQualifiedName(),
                                              qualifiedNameParameterName,
                                              requestBody.getDisplayName(),
                                              requestBody.getDescription(),
                                              requestBody.getExternalTypeGUID(),
                                              requestBody.getDataType(),
                                              requestBody.getDefaultValue(),
                                              requestBody.getFixedValue(),
                                              requestBody.getValidValuesSetGUID(),
                                              null,
                                              requestBody.getIsDeprecated(),
                                              requestBody.getElementPosition(),
                                              requestBody.getMinCardinality(),
                                              requestBody.getMaxCardinality(),
                                              requestBody.getAllowsDuplicateValues(),
                                              requestBody.getOrderedValues(),
                                              requestBody.getDefaultValueOverride(),
                                              sortOrder,
                                              requestBody.getMinimumLength(),
                                              requestBody.getLength(),
                                              requestBody.getPrecision(),
                                              requestBody.getIsNullable(),
                                              requestBody.getNativeJavaClass(),
                                              requestBody.getAliases(),
                                              requestBody.getAdditionalProperties(),
                                              requestBody.getTypeName(),
                                              requestBody.getExtendedProperties(),
                                              null,
                                              null,
                                              isMergeUpdate,
                                              false,
                                              false,
                                              new Date(),
                                              methodName);

                if ((!isMergeUpdate) || (requestBody.getVendorProperties() != null))
                {
                    handler.setVendorProperties(userId,
                                                schemaAttributeGUID,
                                                requestBody.getVendorProperties(),
                                                false,
                                                false,
                                                new Date(),
                                                methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the metadata element representing a schema attribute.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody unique identifier of software server capability representing the caller
     * @param schemaAttributeGUID unique identifier of the metadata element to remove
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeSchemaAttribute(String                    serverName,
                                              String                    userId,
                                              String                    schemaAttributeGUID,
                                              MetadataSourceRequestBody requestBody)
    {
        final String methodName                  = "removeSchemaAttribute";
        final String elementGUIDParameterName    = "schemaAttributeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler<SchemaAttributeElement,
                                          SchemaTypeElement> handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.deleteBeanInRepository(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               schemaAttributeGUID,
                                               elementGUIDParameterName,
                                               OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_GUID,
                                               OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                               null,
                                               null,
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of schema attribute metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param typeName optional type name for the schema attribute - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaAttributesResponse findSchemaAttributes(String                  serverName,
                                                         String                  userId,
                                                         String                  typeName,
                                                         SearchStringRequestBody requestBody,
                                                         int                     startFrom,
                                                         int                     pageSize)
    {
        final String methodName                = "findSchemaAttributes";
        final String searchStringParameterName = "searchString";


        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SchemaAttributesResponse response = new SchemaAttributesResponse();
        AuditLog                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler<SchemaAttributeElement,
                                          SchemaTypeElement> handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, methodName);

            TypeDef typeDef = handler.getTypeDefByName(typeName, OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME);

            if (typeDef != null)
            {
                List<SchemaAttributeElement> results = handler.findSchemaAttributes(userId,
                                                                                    requestBody.getSearchString(),
                                                                                    searchStringParameterName,
                                                                                    typeDef.getGUID(),
                                                                                    typeDef.getName(),
                                                                                    null,
                                                                                    null,
                                                                                    startFrom,
                                                                                    pageSize,
                                                                                    false,
                                                                                    false,
                                                                                    new Date(),
                                                                                    methodName);

                response.setElementList(setUpVendorProperties(userId, results, handler, methodName));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of schema attributes associated with a StructSchemaType or nested underneath a schema attribute.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param parentSchemaElementGUID unique identifier of the schema element of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of associated metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaAttributesResponse getNestedAttributes(String serverName,
                                                        String userId,
                                                        String parentSchemaElementGUID,
                                                        int    startFrom,
                                                        int    pageSize)
    {
        final String methodName                  = "getNestedAttributes";
        final String elementGUIDParameterName    = "schemaAttributeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SchemaAttributesResponse response = new SchemaAttributesResponse();
        AuditLog                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler<SchemaAttributeElement,
                                          SchemaTypeElement> handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, methodName);

            List<SchemaAttributeElement> results = handler.getAttachedSchemaAttributes(userId,
                                                                                       parentSchemaElementGUID,
                                                                                       elementGUIDParameterName,
                                                                                       OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                                       startFrom,
                                                                                       pageSize,
                                                                                       false,
                                                                                       false,
                                                                                       new Date(),
                                                                                       methodName);

            response.setElementList(setUpVendorProperties(userId, results, handler, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of schema attribute metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody name to search for
     * @param typeName optional type name for the schema attribute - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaAttributesResponse getSchemaAttributesByName(String          serverName,
                                                              String          userId,
                                                              String          typeName,
                                                              NameRequestBody requestBody,
                                                              int             startFrom,
                                                              int             pageSize)
    {
        final String methodName        = "getSchemaAttributesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SchemaAttributesResponse response = new SchemaAttributesResponse();
        AuditLog                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaAttributeHandler<SchemaAttributeElement,
                                              SchemaTypeElement> handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, methodName);

                TypeDef typeDef = handler.getTypeDefByName(typeName, OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME);

                if (typeDef != null)
                {
                    List<SchemaAttributeElement> results = handler.getSchemaAttributesByName(userId,
                                                                                             typeDef.getGUID(),
                                                                                             typeDef.getName(),
                                                                                             requestBody.getName(),
                                                                                             null,
                                                                                             null,
                                                                                             startFrom,
                                                                                             pageSize,
                                                                                             false,
                                                                                             false,
                                                                                             new Date(),
                                                                                             methodName);

                    response.setElementList(setUpVendorProperties(userId, results, handler, methodName));
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the schema attribute metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param schemaAttributeGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaAttributeResponse getSchemaAttributeByGUID(String serverName,
                                                            String userId,
                                                            String schemaAttributeGUID)
    {
        final String methodName = "getSchemaAttributeByGUID";
        final String guidParameterName = "schemaAttributeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SchemaAttributeResponse response = new SchemaAttributeResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler<SchemaAttributeElement,
                                          SchemaTypeElement> handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, methodName);

            SchemaAttributeElement result = handler.getSchemaAttribute(userId,
                                                                       schemaAttributeGUID,
                                                                       guidParameterName,
                                                                       OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                       null,
                                                                       null,
                                                                       false,
                                                                       false,
                                                                       new Date(),
                                                                       methodName);

            response.setElement(setUpVendorProperties(userId, result, handler, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /* =====================================================================================================================
     * Working with derived values
     */


    /**
     * Classify the schema element to indicate that it describes a calculated value.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param schemaElementGUID unique identifier of the metadata element to update
     * @param requestBody requestBody for calculating the value - this may contain placeholders that are identified by the
     *                queryIds used in the queryTarget relationships
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupCalculatedValue(String             serverName,
                                             String             userId,
                                             String             schemaElementGUID,
                                             FormulaRequestBody requestBody)
    {
        final String methodName                     = "setupCalculatedValue";
        final String schemaElementGUIDParameterName = "schemaElementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler<SchemaAttributeElement,
                                          SchemaTypeElement> handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, methodName);

            SchemaAttributeBuilder builder = new SchemaAttributeBuilder(handler.getRepositoryHelper(),
                                                                        instanceHandler.getServiceName(),
                                                                        serverName);

            handler.setClassificationInRepository(userId,
                                                  requestBody.getExternalSourceGUID(),
                                                  requestBody.getExternalSourceName(),
                                                  schemaElementGUID,
                                                  schemaElementGUIDParameterName,
                                                  OpenMetadataType.SCHEMA_ELEMENT_TYPE_NAME,
                                                  OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION_TYPE_GUID,
                                                  OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME,
                                                  builder.getCalculatedValueProperties(requestBody.getFormula(), methodName),
                                                  false,
                                                  false,
                                                  false,
                                                  new Date(),
                                                  methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the calculated value designation from the schema element.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param schemaElementGUID unique identifier of the metadata element to update
     * @param requestBody unique identifier of software server capability representing the caller
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearCalculatedValue(String                    serverName,
                                             String                    userId,
                                             String                    schemaElementGUID,
                                             MetadataSourceRequestBody requestBody)
    {
        final String methodName                     = "clearCalculatedValue";
        final String schemaElementGUIDParameterName = "schemaElementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler<SchemaAttributeElement,
                                          SchemaTypeElement> handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, methodName);

            handler.removeClassificationFromRepository(userId,
                                                       requestBody.getExternalSourceGUID(),
                                                       requestBody.getExternalSourceName(),
                                                       schemaElementGUID,
                                                       schemaElementGUIDParameterName,
                                                       OpenMetadataType.SCHEMA_ELEMENT_TYPE_NAME,
                                                       OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION_TYPE_GUID,
                                                       OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME,
                                                       false,
                                                       false,
                                                       new Date(),
                                                       methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Link two schema elements together to show a query target relationship.  The query target provides
     * data values to calculate a derived value.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param derivedElementGUID unique identifier of the derived schema element
     * @param queryTargetGUID unique identifier of the query target schema element
     * @param requestBody properties for the query target relationship
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupQueryTargetRelationship(String                                  serverName,
                                                     String                                  userId,
                                                     String                                  derivedElementGUID,
                                                     String                                  queryTargetGUID,
                                                     DerivedSchemaTypeQueryTargetRequestBody requestBody)

    {
        final String methodName                     = "setupQueryTargetRelationship";
        final String schemaElementGUIDParameterName = "derivedElementGUID";
        final String queryTargetGUIDParameterName   = "queryTargetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler<SchemaAttributeElement,
                                          SchemaTypeElement> handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, methodName);

            handler.setupQueryTargetRelationship(userId,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 derivedElementGUID,
                                                 schemaElementGUIDParameterName,
                                                 OpenMetadataType.SCHEMA_ELEMENT_TYPE_NAME,
                                                 requestBody.getQueryId(),
                                                 requestBody.getQuery(),
                                                 queryTargetGUID,
                                                 queryTargetGUIDParameterName,
                                                 null,
                                                 null,
                                                 false,
                                                 false,
                                                 new Date(),
                                                 methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the relationship properties for the query target.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param derivedElementGUID unique identifier of the derived schema element
     * @param queryTargetGUID unique identifier of the query target schema element
     * @param requestBody properties for the query target relationship
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateQueryTargetRelationship(String                                  serverName,
                                                      String                                  userId,
                                                      String                                  derivedElementGUID,
                                                      String                                  queryTargetGUID,
                                                      DerivedSchemaTypeQueryTargetRequestBody requestBody)
    {
        final String methodName                     = "updateQueryTargetRelationship";
        final String schemaElementGUIDParameterName = "derivedElementGUID";
        final String queryTargetGUIDParameterName   = "queryTargetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler<SchemaAttributeElement,
                                          SchemaTypeElement> handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, methodName);

            handler.updateQueryTargetRelationship(userId,
                                                  requestBody.getExternalSourceGUID(),
                                                  requestBody.getExternalSourceName(),
                                                  derivedElementGUID,
                                                  schemaElementGUIDParameterName,
                                                  OpenMetadataType.SCHEMA_ELEMENT_TYPE_NAME,
                                                  requestBody.getQueryId(),
                                                  requestBody.getQuery(),
                                                  queryTargetGUID,
                                                  queryTargetGUIDParameterName,
                                                  null,
                                                  null,
                                                  true,
                                                  false,
                                                  false,
                                                  new Date(),
                                                  methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the query target relationship between two schema elements.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody unique identifier of software server capability representing the caller
     * @param derivedElementGUID unique identifier of the derived schema element
     * @param queryTargetGUID unique identifier of the query target schema element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearQueryTargetRelationship(String                    serverName,
                                                     String                    userId,
                                                     String                    derivedElementGUID,
                                                     String                    queryTargetGUID,
                                                     MetadataSourceRequestBody requestBody)
    {
        final String methodName                     = "clearQueryTargetRelationship";
        final String schemaElementGUIDParameterName = "derivedElementGUID";
        final String queryTargetGUIDParameterName   = "queryTargetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler<SchemaAttributeElement,
                                          SchemaTypeElement> handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, methodName);

            handler.clearQueryTargetRelationship(userId,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 derivedElementGUID,
                                                 schemaElementGUIDParameterName,
                                                 OpenMetadataType.SCHEMA_ELEMENT_TYPE_NAME,
                                                 queryTargetGUID,
                                                 queryTargetGUIDParameterName,
                                                 methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }




    /**
     * Set up the vendor properties in the retrieved elements.
     *
     * @param userId calling user
     * @param retrievedResults results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @return updated results
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private List<SchemaTypeElement> setUpVendorProperties(String                               userId,
                                                          List<SchemaTypeElement>              retrievedResults,
                                                          SchemaTypeHandler<SchemaTypeElement> handler,
                                                          String                               methodName) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException
    {
        if (retrievedResults != null)
        {
            for (SchemaTypeElement element : retrievedResults)
            {
                if (element != null)
                {
                    setUpVendorProperties(userId, element, handler, methodName);
                }
            }
        }

        return retrievedResults;
    }


    /**
     * Set up the vendor properties in the retrieved element.
     *
     * @param userId calling user
     * @param element results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @return updated results
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private SchemaTypeElement setUpVendorProperties(String                               userId,
                                                    SchemaTypeElement                    element,
                                                    SchemaTypeHandler<SchemaTypeElement> handler,
                                                    String                               methodName) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        final String elementGUIDParameterName = "element.getElementHeader().getGUID()";

        if (element != null)
        {
            SchemaTypeProperties properties = element.getSchemaTypeProperties();

            properties.setVendorProperties(handler.getVendorProperties(userId,
                                                                       element.getElementHeader().getGUID(),
                                                                       elementGUIDParameterName,
                                                                       false,
                                                                       false,
                                                                       new Date(),
                                                                       methodName));
        }

        return element;
    }


    /**
     * Set up the vendor properties in the retrieved elements.
     *
     * @param userId calling user
     * @param retrievedResults results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @return updated results
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private List<SchemaAttributeElement> setUpVendorProperties(String                                                            userId,
                                                               List<SchemaAttributeElement>                                      retrievedResults,
                                                               SchemaAttributeHandler<SchemaAttributeElement, SchemaTypeElement> handler,
                                                               String                                                            methodName) throws InvalidParameterException,
                                                                                                                                                    UserNotAuthorizedException,
                                                                                                                                                    PropertyServerException
    {
        if (retrievedResults != null)
        {
            for (SchemaAttributeElement element : retrievedResults)
            {
                if (element != null)
                {
                    setUpVendorProperties(userId, element, handler, methodName);
                }
            }
        }

        return retrievedResults;
    }


    /**
     * Set up the vendor properties in the retrieved element.
     *
     * @param userId calling user
     * @param element results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @return updated results
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private SchemaAttributeElement setUpVendorProperties(String                                                            userId,
                                                         SchemaAttributeElement                                            element,
                                                         SchemaAttributeHandler<SchemaAttributeElement, SchemaTypeElement> handler,
                                                         String                                                            methodName) throws InvalidParameterException,
                                                                                                                                              UserNotAuthorizedException,
                                                                                                                                              PropertyServerException
    {
        final String elementGUIDParameterName = "element.getElementHeader().getGUID()";

        if (element != null)
        {
            SchemaAttributeProperties properties = element.getProperties();

            properties.setVendorProperties(handler.getVendorProperties(userId,
                                                                       element.getElementHeader().getGUID(),
                                                                       elementGUIDParameterName,
                                                                       false,
                                                                       false,
                                                                       new Date(),
                                                                       methodName));
        }

        return element;
    }
}
