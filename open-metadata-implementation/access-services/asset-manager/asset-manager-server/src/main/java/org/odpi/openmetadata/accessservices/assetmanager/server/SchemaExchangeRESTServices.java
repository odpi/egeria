/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.server;

import org.odpi.openmetadata.accessservices.assetmanager.handlers.SchemaExchangeHandler;
import org.odpi.openmetadata.accessservices.assetmanager.rest.*;
import org.odpi.openmetadata.accessservices.assetmanager.rest.CalculatedValueClassificationRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.EffectiveTimeQueryRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NameRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.SchemaAttributeElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.SchemaAttributeElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.SchemaAttributeRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.SchemaTypeElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.SchemaTypeElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.SchemaTypeRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.SearchStringRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.TemplateRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.UpdateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

import org.slf4j.LoggerFactory;


/**
 * SchemaExchangeRESTServices is the server-side implementation of the Asset Manager OMAS's
 * support for schemas.  It matches the SchemaExchangeClient.
 */
public class SchemaExchangeRESTServices
{
    private static final AssetManagerInstanceHandler instanceHandler = new AssetManagerInstanceHandler();
    private static final RESTCallLogger              restCallLogger  = new RESTCallLogger(LoggerFactory.getLogger(SchemaExchangeRESTServices.class),
                                                                                          instanceHandler.getServiceName());

    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public SchemaExchangeRESTServices()
    {
    }



    /* =====================================================================================================================
     * A schemaType describes the structure of a data asset, process or port
     */

    /**
     * Create a new metadata element to represent a schema type.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the asset manager can update this schema element
     * @param anchorGUID unique identifier of the intended anchor of the schema type
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties about the schema type to store
     *
     * @return unique identifier of the new schema type or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createSchemaType(String                serverName,
                                         String                userId,
                                         boolean               assetManagerIsHome,
                                         String                anchorGUID,
                                         boolean               forLineage,
                                         boolean               forDuplicateProcessing,
                                         SchemaTypeRequestBody requestBody)
    {
        final String methodName = "createSchemaType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaExchangeHandler handler = instanceHandler.getSchemaExchangeHandler(userId, serverName, methodName);

                response.setGUID(handler.createSchemaType(userId,
                                                          requestBody.getMetadataCorrelationProperties(),
                                                          assetManagerIsHome,
                                                          anchorGUID,
                                                          requestBody.getElementProperties(),
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          requestBody.getEffectiveTime(),
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
     * Create a new metadata element to represent a schema type using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the asset manager can update this schema element
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
                                                     boolean             assetManagerIsHome,
                                                     TemplateRequestBody requestBody)
    {
        final String methodName = "createSchemaTypeFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaExchangeHandler handler = instanceHandler.getSchemaExchangeHandler(userId, serverName, methodName);

                response.setGUID(handler.createSchemaTypeFromTemplate(userId,
                                                                      requestBody.getMetadataCorrelationProperties(),
                                                                      assetManagerIsHome,
                                                                      templateGUID,
                                                                      requestBody.getElementProperties(),
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
     * Update the metadata element representing a schema type.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
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
                                         boolean               forLineage,
                                         boolean               forDuplicateProcessing,
                                         SchemaTypeRequestBody requestBody)
    {
        final String methodName = "updateSchemaType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaExchangeHandler handler = instanceHandler.getSchemaExchangeHandler(userId, serverName, methodName);

                handler.updateSchemaType(userId,
                                         requestBody.getMetadataCorrelationProperties(),
                                         schemaTypeGUID,
                                         isMergeUpdate,
                                         forLineage,
                                         forDuplicateProcessing,
                                         requestBody.getElementProperties(),
                                         requestBody.getEffectiveTime(),
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
     * Connect a schema type to a data asset, process or port.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the schema type to connect
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is to be connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is to be connected to
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupSchemaTypeParent(String                        serverName,
                                              String                        userId,
                                              String                        parentElementGUID,
                                              String                        parentElementTypeName,
                                              String                        schemaTypeGUID,
                                              boolean                       assetManagerIsHome,
                                              boolean                       forLineage,
                                              boolean                       forDuplicateProcessing,
                                              RelationshipRequestBody       requestBody)
    {
        final String methodName = "setupSchemaTypeParent";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaExchangeHandler handler = instanceHandler.getSchemaExchangeHandler(userId, serverName, methodName);

                if (requestBody.getProperties() != null)
                {
                    handler.setupSchemaTypeParent(userId,
                                                  requestBody.getExternalSourceGUID(),
                                                  requestBody.getExternalSourceName(),
                                                  assetManagerIsHome,
                                                  schemaTypeGUID,
                                                  parentElementGUID,
                                                  parentElementTypeName,
                                                  requestBody.getProperties().getEffectiveFrom(),
                                                  requestBody.getProperties().getEffectiveTo(),
                                                  forLineage,
                                                  forDuplicateProcessing,
                                                  requestBody.getEffectiveTime(),
                                                  methodName);
                }
                else
                {
                    handler.setupSchemaTypeParent(userId,
                                                  requestBody.getExternalSourceGUID(),
                                                  requestBody.getExternalSourceName(),
                                                  assetManagerIsHome,
                                                  schemaTypeGUID,
                                                  parentElementGUID,
                                                  parentElementTypeName,
                                                  null,
                                                  null,
                                                  forLineage,
                                                  forDuplicateProcessing,
                                                  requestBody.getEffectiveTime(),
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
     * Remove the relationship between a schema type and its parent data asset, process or port.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the schema type to connect
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is to be connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is to be connected to
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearSchemaTypeParent(String                        serverName,
                                              String                        userId,
                                              String                        parentElementGUID,
                                              String                        parentElementTypeName,
                                              String                        schemaTypeGUID,
                                              boolean                       forLineage,
                                              boolean                       forDuplicateProcessing,
                                              EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "clearSchemaTypeParent";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaExchangeHandler handler = instanceHandler.getSchemaExchangeHandler(userId, serverName, methodName);

                handler.clearSchemaTypeParent(userId,
                                              requestBody.getAssetManagerGUID(),
                                              requestBody.getAssetManagerName(),
                                              schemaTypeGUID,
                                              parentElementGUID,
                                              parentElementTypeName,
                                              forLineage,
                                              forDuplicateProcessing,
                                              requestBody.getEffectiveTime(),
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
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param endOneGUID unique identifier of the schema element at end one of the relationship
     * @param endTwoGUID unique identifier of the schema element at end two of the relationship
     * @param relationshipTypeName type of the relationship to create
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody relationship properties
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
                                                       boolean                 assetManagerIsHome,
                                                       boolean                 forLineage,
                                                       boolean                 forDuplicateProcessing,
                                                       RelationshipRequestBody requestBody)
    {
        final String methodName = "setupSchemaElementRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaExchangeHandler handler = instanceHandler.getSchemaExchangeHandler(userId, serverName, methodName);

                if (requestBody.getProperties() != null)
                {
                    handler.setupSchemaElementRelationship(userId,
                                                           requestBody.getExternalSourceGUID(),
                                                           requestBody.getExternalSourceName(),
                                                           assetManagerIsHome,
                                                           endOneGUID,
                                                           endTwoGUID,
                                                           relationshipTypeName,
                                                           requestBody.getProperties(),
                                                           requestBody.getProperties().getEffectiveFrom(),
                                                           requestBody.getProperties().getEffectiveTo(),
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           requestBody.getEffectiveTime(),
                                                           methodName);
                }
                else
                {
                    handler.setupSchemaElementRelationship(userId,
                                                           requestBody.getExternalSourceGUID(),
                                                           requestBody.getExternalSourceName(),
                                                           assetManagerIsHome,
                                                           endOneGUID,
                                                           endTwoGUID,
                                                           relationshipTypeName,
                                                           null,
                                                           null,
                                                           null,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           requestBody.getEffectiveTime(),
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
     * Remove a relationship between two schema elements.  The name of the desired relationship is passed on the API.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param endOneGUID unique identifier of the schema element at end one of the relationship
     * @param endTwoGUID unique identifier of the schema element at end two of the relationship
     * @param relationshipTypeName type of the relationship to delete
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearSchemaElementRelationship(String                        serverName,
                                                       String                        userId,
                                                       String                        endOneGUID,
                                                       String                        relationshipTypeName,
                                                       String                        endTwoGUID,
                                                       boolean                       forLineage,
                                                       boolean                       forDuplicateProcessing,
                                                       EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "clearSchemaElementRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaExchangeHandler handler = instanceHandler.getSchemaExchangeHandler(userId, serverName, methodName);

                handler.clearSchemaElementRelationship(userId,
                                                       requestBody.getAssetManagerGUID(),
                                                       requestBody.getAssetManagerName(),
                                                       endTwoGUID,
                                                       endOneGUID,
                                                       relationshipTypeName,
                                                       forLineage,
                                                       forDuplicateProcessing,
                                                       requestBody.getEffectiveTime(),
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
     * Remove the metadata element representing a schema type.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier/name of software server capability representing the caller and external identifier of element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeSchemaType(String            serverName,
                                         String            userId,
                                         String            schemaTypeGUID,
                                         boolean           forLineage,
                                         boolean           forDuplicateProcessing,
                                         UpdateRequestBody requestBody)
    {
        final String methodName = "removeSchemaType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaExchangeHandler handler = instanceHandler.getSchemaExchangeHandler(userId, serverName, methodName);

                handler.removeSchemaType(userId, requestBody.getMetadataCorrelationProperties(), schemaTypeGUID, forLineage, forDuplicateProcessing, requestBody.getEffectiveTime(), methodName);
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
     * Retrieve the list of schema type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody string to find in the properties plus external identifiers
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeElementsResponse findSchemaType(String                  serverName,
                                                     String                  userId,
                                                     int                     startFrom,
                                                     int                     pageSize,
                                                     boolean                 forLineage,
                                                     boolean                 forDuplicateProcessing,
                                                     SearchStringRequestBody requestBody)
    {
        final String methodName = "findSchemaType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SchemaTypeElementsResponse response = new SchemaTypeElementsResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaExchangeHandler handler = instanceHandler.getSchemaExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.findSchemaType(userId,
                                                               requestBody.getAssetManagerGUID(),
                                                               requestBody.getAssetManagerName(),
                                                               requestBody.getSearchString(),
                                                               startFrom,
                                                               pageSize,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               requestBody.getEffectiveTime(),
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
     * Return the schema type associated with a specific open metadata element (data asset, process or port).
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is to be connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is to be connected to
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return metadata element describing the schema type associated with the requested parent element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeElementResponse getSchemaTypeForElement(String                        serverName,
                                                             String                        userId,
                                                             String                        parentElementGUID,
                                                             String                        parentElementTypeName,
                                                             boolean                       forLineage,
                                                             boolean                       forDuplicateProcessing,
                                                             EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getSchemaTypeForElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SchemaTypeElementResponse response = new SchemaTypeElementResponse();
        AuditLog                  auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaExchangeHandler handler = instanceHandler.getSchemaExchangeHandler(userId, serverName, methodName);

                response.setElement(handler.getSchemaTypeForElement(userId,
                                                                    requestBody.getAssetManagerGUID(),
                                                                    requestBody.getAssetManagerName(),
                                                                    parentElementGUID,
                                                                    parentElementTypeName,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    requestBody.getEffectiveTime(),
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
     * Retrieve the list of schema type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody name to search for plus identifiers
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeElementsResponse   getSchemaTypeByName(String          serverName,
                                                            String          userId,
                                                            int             startFrom,
                                                            int             pageSize,
                                                            boolean         forLineage,
                                                            boolean         forDuplicateProcessing,
                                                            NameRequestBody requestBody)
    {
        final String methodName = "getSchemaTypeByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SchemaTypeElementsResponse response = new SchemaTypeElementsResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaExchangeHandler handler = instanceHandler.getSchemaExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.getSchemaTypeByName(userId,
                                                                    requestBody.getAssetManagerGUID(),
                                                                    requestBody.getAssetManagerName(),
                                                                    requestBody.getName(),
                                                                    startFrom,
                                                                    pageSize,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    requestBody.getEffectiveTime(),
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
     * Retrieve the schema type metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeElementResponse getSchemaTypeByGUID(String                        serverName,
                                                         String                        userId,
                                                         String                        schemaTypeGUID,
                                                         boolean                       forLineage,
                                                         boolean                       forDuplicateProcessing,
                                                         EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getSchemaTypeByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SchemaTypeElementResponse response = new SchemaTypeElementResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaExchangeHandler handler = instanceHandler.getSchemaExchangeHandler(userId, serverName, methodName);

                response.setElement(handler.getSchemaTypeByGUID(userId,
                                                                requestBody.getAssetManagerGUID(),
                                                                requestBody.getAssetManagerName(),
                                                                schemaTypeGUID,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                requestBody.getEffectiveTime(),
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
     * Retrieve the header of the metadata element connected to a schema type.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return header for parent element (data asset, process, port) or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ElementHeaderResponse getSchemaTypeParent(String                        serverName,
                                                     String                        userId,
                                                     String                        schemaTypeGUID,
                                                     boolean                       forLineage,
                                                     boolean                       forDuplicateProcessing,
                                                     EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getSchemaTypeParent";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ElementHeaderResponse response = new ElementHeaderResponse();
        AuditLog              auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaExchangeHandler handler = instanceHandler.getSchemaExchangeHandler(userId, serverName, methodName);

                response.setElement(handler.getSchemaTypeParent(userId,
                                                                requestBody.getAssetManagerGUID(),
                                                                requestBody.getAssetManagerName(),
                                                                schemaTypeGUID,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                requestBody.getEffectiveTime(),
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


    /* ===============================================================================
     * A schemaType typically contains many schema attributes, linked with relationships.
     */

    /**
     * Create a new metadata element to represent a schema attribute.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the asset manager can update this schema attribute
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the schema attribute is connected to
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
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
                                              boolean                    assetManagerIsHome,
                                              boolean                    forLineage,
                                              boolean                    forDuplicateProcessing,
                                              SchemaAttributeRequestBody requestBody)
    {
        final String methodName = "createSchemaAttribute";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaExchangeHandler handler = instanceHandler.getSchemaExchangeHandler(userId, serverName, methodName);

                response.setGUID(handler.createSchemaAttribute(userId,
                                                               requestBody.getMetadataCorrelationProperties(),
                                                               assetManagerIsHome,
                                                               schemaElementGUID,
                                                               requestBody.getElementProperties(),
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               requestBody.getEffectiveTime(),
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
     * Create a new metadata element to represent a schema attribute using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the schema attribute is connected to
     * @param templateGUID unique identifier of the metadata element to copy
     * @param assetManagerIsHome ensure that only the asset manager can update this schema attribute
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
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
                                                          boolean             assetManagerIsHome,
                                                          boolean             forLineage,
                                                          boolean             forDuplicateProcessing,
                                                          TemplateRequestBody requestBody)
    {
        final String methodName = "createSchemaAttributeFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaExchangeHandler handler = instanceHandler.getSchemaExchangeHandler(userId, serverName, methodName);

                response.setGUID(handler.createSchemaAttributeFromTemplate(userId,
                                                                           requestBody.getMetadataCorrelationProperties(),
                                                                           assetManagerIsHome,
                                                                           schemaElementGUID,
                                                                           templateGUID,
                                                                           requestBody.getElementProperties(),
                                                                           forLineage,
                                                                           forDuplicateProcessing,
                                                                           requestBody.getEffectiveTime(),
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
     * Update the properties of the metadata element representing a schema attribute.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaAttributeGUID unique identifier of the schema attribute to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
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
                                              boolean                    forLineage,
                                              boolean                    forDuplicateProcessing,
                                              SchemaAttributeRequestBody requestBody)
    {
        final String methodName  = "updateSchemaAttribute";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaExchangeHandler handler = instanceHandler.getSchemaExchangeHandler(userId, serverName, methodName);

                handler.updateSchemaAttribute(userId,
                                              requestBody.getMetadataCorrelationProperties(),
                                              schemaAttributeGUID,
                                              isMergeUpdate,
                                              requestBody.getElementProperties(),
                                              forLineage,
                                              forDuplicateProcessing,
                                              requestBody.getEffectiveTime(),
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
     * Classify the schema type (or attribute if type is embedded) to indicate that it is a calculated value.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaElementGUID unique identifier of the metadata element to update
     * @param assetManagerIsHome ensure that only the asset manager can update this classification
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier/name of software server capability representing the caller and external identifier of element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setSchemaElementAsCalculatedValue(String                                   serverName,
                                                          String                                   userId,
                                                          String                                   schemaElementGUID,
                                                          boolean                                  assetManagerIsHome,
                                                          boolean                                  forLineage,
                                                          boolean                                  forDuplicateProcessing,
                                                          CalculatedValueClassificationRequestBody requestBody)
    {
        final String methodName = "setSchemaElementAsCalculatedValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if ((requestBody != null) && (requestBody.getMetadataCorrelationProperties() != null))
            {
                SchemaExchangeHandler handler = instanceHandler.getSchemaExchangeHandler(userId, serverName, methodName);

                handler.setSchemaElementAsCalculatedValue(userId,
                                                          requestBody.getMetadataCorrelationProperties().getExternalScopeGUID(),
                                                          requestBody.getMetadataCorrelationProperties().getExternalScopeName(),
                                                          assetManagerIsHome,
                                                          schemaElementGUID,
                                                          requestBody.getFormula(),
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          requestBody.getEffectiveTime(),
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
     * Remove the calculated value designation from the schema element.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaElementGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier/name of software server capability representing the caller and external identifier of element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearSchemaElementAsCalculatedValue(String            serverName,
                                                            String            userId,
                                                            String            schemaElementGUID,
                                                            boolean           forLineage,
                                                            boolean           forDuplicateProcessing,
                                                            UpdateRequestBody requestBody)
    {
        final String methodName = "clearSchemaElementAsCalculatedValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if ((requestBody != null) && (requestBody.getMetadataCorrelationProperties() != null))
            {
                SchemaExchangeHandler handler = instanceHandler.getSchemaExchangeHandler(userId, serverName, methodName);

                handler.clearSchemaElementAsCalculatedValue(userId,
                                                            requestBody.getMetadataCorrelationProperties().getExternalScopeGUID(),
                                                            requestBody.getMetadataCorrelationProperties().getExternalScopeName(),
                                                            schemaElementGUID,
                                                            forLineage,
                                                            forDuplicateProcessing,
                                                            requestBody.getEffectiveTime(),
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
     * Classify the column schema attribute to indicate that it describes a primary key.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the asset manager can update this classification
     * @param schemaAttributeGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody details of the primary key plus external identifiers
     *
     * @return null or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupColumnAsPrimaryKey(String                              serverName,
                                                String                              userId,
                                                String                              schemaAttributeGUID,
                                                boolean                             assetManagerIsHome,
                                                boolean                             forLineage,
                                                boolean                             forDuplicateProcessing,
                                                PrimaryKeyClassificationRequestBody requestBody)
    {
        final String methodName = "setupColumnAsPrimaryKey";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaExchangeHandler handler = instanceHandler.getSchemaExchangeHandler(userId, serverName, methodName);

                handler.setupColumnAsPrimaryKey(userId,
                                                requestBody.getExternalSourceGUID(),
                                                requestBody.getExternalSourceName(),
                                                assetManagerIsHome,
                                                schemaAttributeGUID,
                                                requestBody.getPrimaryKeyProperties().getName(),
                                                requestBody.getPrimaryKeyProperties().getKeyPattern(),
                                                forLineage,
                                                forDuplicateProcessing,
                                                requestBody.getEffectiveTime(),
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
     * Remove the primary key designation from the schema attribute.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaAttributeGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier/name of software server capability representing the caller and external identifier of element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearColumnAsPrimaryKey(String            serverName,
                                                String            userId,
                                                String            schemaAttributeGUID,
                                                boolean           forLineage,
                                                boolean           forDuplicateProcessing,
                                                UpdateRequestBody requestBody)
    {
        final String methodName = "clearColumnAsPrimaryKey";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if ((requestBody != null) && (requestBody.getMetadataCorrelationProperties() != null))
            {
                SchemaExchangeHandler handler = instanceHandler.getSchemaExchangeHandler(userId, serverName, methodName);

                handler.clearColumnAsPrimaryKey(userId,
                                                requestBody.getMetadataCorrelationProperties().getExternalScopeGUID(),
                                                requestBody.getMetadataCorrelationProperties().getExternalScopeName(),
                                                schemaAttributeGUID,
                                                forLineage,
                                                forDuplicateProcessing,
                                                requestBody.getEffectiveTime(),
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
     * Link two schema attributes together to show a foreign key relationship.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param primaryKeyGUID unique identifier of the derived schema element
     * @param foreignKeyGUID unique identifier of the query target schema element
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the foreign key relationship
     *
     * @return  void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupForeignKeyRelationship(String                serverName,
                                                    String                userId,
                                                    String                primaryKeyGUID,
                                                    String                foreignKeyGUID,
                                                    boolean               assetManagerIsHome,
                                                    boolean               forLineage,
                                                    boolean               forDuplicateProcessing,
                                                    ForeignKeyRequestBody requestBody)
    {
        final String methodName = "setupForeignKeyRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaExchangeHandler handler = instanceHandler.getSchemaExchangeHandler(userId, serverName, methodName);

                handler.setupForeignKeyRelationship(userId,
                                                    requestBody.getAssetManagerGUID(),
                                                    requestBody.getAssetManagerName(),
                                                    assetManagerIsHome,
                                                    primaryKeyGUID,
                                                    foreignKeyGUID,
                                                    requestBody.getProperties(),
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    requestBody.getEffectiveTime(),
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
     * Update the relationship properties for the query target.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param primaryKeyGUID unique identifier of the derived schema element
     * @param foreignKeyGUID unique identifier of the query target schema element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the foreign key relationship plus external identifiers
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateForeignKeyRelationship(String                serverName,
                                                     String                userId,
                                                     String                primaryKeyGUID,
                                                     String                foreignKeyGUID,
                                                     boolean               forLineage,
                                                     boolean               forDuplicateProcessing,
                                                     ForeignKeyRequestBody requestBody)
    {
        final String methodName = "updateForeignKeyRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaExchangeHandler handler = instanceHandler.getSchemaExchangeHandler(userId, serverName, methodName);

                handler.updateForeignKeyRelationship(userId,
                                                     requestBody.getAssetManagerGUID(),
                                                     requestBody.getAssetManagerName(),
                                                     primaryKeyGUID,
                                                     foreignKeyGUID,
                                                     requestBody.getProperties(),
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     requestBody.getEffectiveTime(),
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
     * Remove the foreign key relationship between two schema elements.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param primaryKeyGUID unique identifier of the derived schema element
     * @param foreignKeyGUID unique identifier of the query target schema element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearForeignKeyRelationship(String                        serverName,
                                                    String                        userId,
                                                    String                        primaryKeyGUID,
                                                    String                        foreignKeyGUID,
                                                    boolean                       forLineage,
                                                    boolean                       forDuplicateProcessing,
                                                    EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "clearForeignKeyRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaExchangeHandler handler = instanceHandler.getSchemaExchangeHandler(userId, serverName, methodName);

                handler.clearForeignKeyRelationship(userId,
                                                    requestBody.getAssetManagerGUID(),
                                                    requestBody.getAssetManagerName(),
                                                    primaryKeyGUID,
                                                    foreignKeyGUID,
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    requestBody.getEffectiveTime(),
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
     * Remove the metadata element representing a schema attribute.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaAttributeGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier/name of software server capability representing the caller and external identifier of element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeSchemaAttribute(String            serverName,
                                              String            userId,
                                              String            schemaAttributeGUID,
                                              boolean           forLineage,
                                              boolean           forDuplicateProcessing,
                                              UpdateRequestBody requestBody)
    {
        final String methodName = "removeSchemaAttribute";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaExchangeHandler handler = instanceHandler.getSchemaExchangeHandler(userId, serverName, methodName);

                handler.removeSchemaAttribute(userId,
                                              requestBody.getMetadataCorrelationProperties(),
                                              schemaAttributeGUID,
                                              forLineage,
                                              forDuplicateProcessing,
                                              requestBody.getEffectiveTime(),
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
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody string to find in the properties plus external identifiers
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaAttributeElementsResponse findSchemaAttributes(String                  serverName,
                                                                String                  userId,
                                                                int                     startFrom,
                                                                int                     pageSize,
                                                                boolean                 forLineage,
                                                                boolean                 forDuplicateProcessing,
                                                                SearchStringRequestBody requestBody)
    {
        final String methodName = "findSchemaAttributes";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SchemaAttributeElementsResponse response = new SchemaAttributeElementsResponse();
        AuditLog                        auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaExchangeHandler handler = instanceHandler.getSchemaExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.findSchemaAttributes(userId,
                                                                     requestBody.getAssetManagerGUID(),
                                                                     requestBody.getAssetManagerName(),
                                                                     requestBody.getSearchString(),
                                                                     startFrom,
                                                                     pageSize,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     requestBody.getEffectiveTime(),
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
     * Retrieve the list of schema attributes associated with a schemaType.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the schemaType of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return list of associated metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaAttributeElementsResponse getNestedAttributes(String                        serverName,
                                                               String                        userId,
                                                               String                        schemaTypeGUID,
                                                               int                           startFrom,
                                                               int                           pageSize,
                                                               boolean                       forLineage,
                                                               boolean                       forDuplicateProcessing,
                                                               EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getNestedAttributes";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SchemaAttributeElementsResponse response = new SchemaAttributeElementsResponse();
        AuditLog                        auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaExchangeHandler handler = instanceHandler.getSchemaExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.getNestedAttributes(userId,
                                                                    requestBody.getAssetManagerGUID(),
                                                                    requestBody.getAssetManagerName(),
                                                                    schemaTypeGUID,
                                                                    startFrom,
                                                                    pageSize,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    requestBody.getEffectiveTime(),
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
     * Retrieve the list of schema attribute metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaAttributeElementsResponse getSchemaAttributesByName(String          serverName,
                                                                     String          userId,
                                                                     int             startFrom,
                                                                     int             pageSize,
                                                                     boolean         forLineage,
                                                                     boolean         forDuplicateProcessing,
                                                                     NameRequestBody requestBody)
    {
        final String methodName = "getSchemaAttributesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SchemaAttributeElementsResponse response = new SchemaAttributeElementsResponse();
        AuditLog                        auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaExchangeHandler handler = instanceHandler.getSchemaExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.getSchemaAttributesByName(userId,
                                                                          requestBody.getAssetManagerGUID(),
                                                                          requestBody.getAssetManagerName(),
                                                                          requestBody.getName(),
                                                                          startFrom,
                                                                          pageSize,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          requestBody.getEffectiveTime(),
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
     * Retrieve the schema attribute metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaAttributeGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return matching metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaAttributeElementResponse getSchemaAttributeByGUID(String                        serverName,
                                                                   String                        userId,
                                                                   String                        schemaAttributeGUID,
                                                                   boolean                       forLineage,
                                                                   boolean                       forDuplicateProcessing,
                                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getSchemaAttributeByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SchemaAttributeElementResponse response = new SchemaAttributeElementResponse();
        AuditLog                       auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaExchangeHandler handler = instanceHandler.getSchemaExchangeHandler(userId, serverName, methodName);

                response.setElement(handler.getSchemaAttributeByGUID(userId,
                                                                     requestBody.getAssetManagerGUID(),
                                                                     requestBody.getAssetManagerName(),
                                                                     schemaAttributeGUID,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     requestBody.getEffectiveTime(),
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
}
