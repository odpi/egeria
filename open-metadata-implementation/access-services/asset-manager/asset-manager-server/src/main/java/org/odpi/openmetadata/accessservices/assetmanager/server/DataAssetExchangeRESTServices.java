/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.server;

import org.odpi.openmetadata.accessservices.assetmanager.handlers.DataAssetExchangeHandler;

import org.odpi.openmetadata.accessservices.assetmanager.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.slf4j.LoggerFactory;



/**
 * DataAssetExchangeRESTServices is the server-side implementation of the Asset Manager OMAS's
 * support for relational databases.  It matches the DataAssetExchangeClient.
 */
public class DataAssetExchangeRESTServices
{
    private static final AssetManagerInstanceHandler instanceHandler = new AssetManagerInstanceHandler();
    private static final RESTCallLogger              restCallLogger  = new RESTCallLogger(LoggerFactory.getLogger(DataAssetExchangeRESTServices.class),
                                                                                          instanceHandler.getServiceName());

    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public DataAssetExchangeRESTServices()
    {
    }


    /* ======================================================================================
     * The Asset entity is the top level element to describe an implemented data asset such as a data store or data set.
     */

    /**
     * Create a new metadata element to represent the root of an asset.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDataAsset(String               serverName,
                                        String               userId,
                                        boolean              assetManagerIsHome,
                                        DataAssetRequestBody requestBody)
    {
        final String methodName = "createDataAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                DataAssetExchangeHandler handler = instanceHandler.getDataAssetExchangeHandler(userId, serverName, methodName);

                response.setGUID(handler.createDataAsset(userId,
                                                         requestBody.getMetadataCorrelationProperties(),
                                                         assetManagerIsHome,
                                                         requestBody.getElementProperties(),
                                                         false,
                                                         false,
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
     * Create a new metadata element to represent an asset using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new asset.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template and correlate to external identifiers
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDataAssetFromTemplate(String              serverName,
                                                    String              userId,
                                                    boolean             assetManagerIsHome,
                                                    String              templateGUID,
                                                    TemplateRequestBody requestBody)
    {
        final String methodName = "createDataAssetFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                DataAssetExchangeHandler handler = instanceHandler.getDataAssetExchangeHandler(userId, serverName, methodName);

                response.setGUID(handler.createDataAssetFromTemplate(userId,
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
     * Update the metadata element representing an asset.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody new properties for this element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateDataAsset(String               serverName,
                                        String               userId,
                                        String               assetGUID,
                                        boolean              isMergeUpdate,
                                        boolean              forLineage,
                                        boolean              forDuplicateProcessing,
                                        DataAssetRequestBody requestBody)
    {
        final String methodName = "updateDataAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                DataAssetExchangeHandler handler = instanceHandler.getDataAssetExchangeHandler(userId, serverName, methodName);

                handler.updateDataAsset(userId,
                                        requestBody.getMetadataCorrelationProperties(),
                                        assetGUID,
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
     * Update the zones for the asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Asset Manager OMAS).
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to publish
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody correlation properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse publishDataAsset(String                             serverName,
                                         String                             userId,
                                         String                             assetGUID,
                                         boolean               forLineage,
                                         boolean               forDuplicateProcessing,
                                         EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "publishDataAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataAssetExchangeHandler handler = instanceHandler.getDataAssetExchangeHandler(userId, serverName, methodName);

            handler.publishDataAsset(userId,
                                     assetGUID,
                                     forLineage,
                                     forDuplicateProcessing,
                                     requestBody.getEffectiveTime(),
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
     * Update the zones for the asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Asset Manager OMAS.  This is the setting when the database is first created).
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to withdraw
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody correlation properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse withdrawDataAsset(String                        serverName,
                                          String                        userId,
                                          String                        assetGUID,
                                          boolean                       forLineage,
                                          boolean                       forDuplicateProcessing,
                                          EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "withdrawDataAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataAssetExchangeHandler handler = instanceHandler.getDataAssetExchangeHandler(userId, serverName, methodName);

            handler.withdrawDataAsset(userId,
                                      assetGUID,
                                      forLineage,
                                      forDuplicateProcessing,
                                      requestBody.getEffectiveTime(),
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
     * Remove the metadata element representing an asset.  This will delete the asset and all anchored
     * elements such as schema and comments.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody correlation properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeDataAsset(String            serverName,
                                        String            userId,
                                        String            assetGUID,
                                        boolean           forLineage,
                                        boolean           forDuplicateProcessing,
                                        UpdateRequestBody requestBody)
    {
        final String methodName = "removeDataAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataAssetExchangeHandler handler = instanceHandler.getDataAssetExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeDataAsset(userId,
                                        requestBody.getMetadataCorrelationProperties(),
                                        assetGUID,
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
     * Classify the asset to indicate that it can be used as reference data.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody correlation properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse setDataAssetAsReferenceData(String            serverName,
                                                    String            userId,
                                                    String            assetGUID,
                                                    boolean           forLineage,
                                                    boolean           forDuplicateProcessing,
                                                    UpdateRequestBody requestBody)
    {
        final String methodName = "setDataAssetAsReferenceData";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataAssetExchangeHandler handler = instanceHandler.getDataAssetExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.setDataAssetAsReferenceData(userId,
                                                    assetGUID,
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
     * Remove the reference data designation from the asset.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody correlation properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse clearDataAssetAsReferenceData(String            serverName,
                                                      String            userId,
                                                      String            assetGUID,
                                                      boolean           forLineage,
                                                      boolean           forDuplicateProcessing,
                                                      UpdateRequestBody requestBody)
    {
        final String methodName = "clearDataAssetAsReferenceData";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataAssetExchangeHandler handler = instanceHandler.getDataAssetExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearDataAssetAsReferenceData(userId,
                                                      assetGUID,
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
     * Link two asset together.
     * Use information from the relationship type definition to ensure the fromAssetGUID and toAssetGUID are the right way around.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param relationshipTypeName type name of relationship to create
     * @param fromAssetGUID unique identifier of the asset at end 1 of the relationship
     * @param toAssetGUID unique identifier of the asset at end 2 of the relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier for this relationship
     *
     * @return unique identifier of the relationship or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse setupRelatedDataAsset(String                  serverName,
                                              String                  userId,
                                              boolean                 assetManagerIsHome,
                                              String                  relationshipTypeName,
                                              String                  fromAssetGUID,
                                              String                  toAssetGUID,
                                              boolean                 forLineage,
                                              boolean                 forDuplicateProcessing,
                                              RelationshipRequestBody requestBody)
    {
        final String methodName  = "setupRelatedDataAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                DataAssetExchangeHandler handler = instanceHandler.getDataAssetExchangeHandler(userId, serverName, methodName);

                response.setGUID(handler.setupRelatedDataAsset(userId,
                                                               requestBody.getAssetManagerGUID(),
                                                               requestBody.getAssetManagerName(),
                                                               assetManagerIsHome,
                                                               relationshipTypeName,
                                                               fromAssetGUID,
                                                               toAssetGUID,
                                                               requestBody.getProperties(),
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
     * Retrieve the relationship between two elements.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param relationshipTypeName type name of relationship to create
     * @param fromAssetGUID unique identifier of the asset at end 1 of the relationship
     * @param toAssetGUID unique identifier of the asset at end 2 of the relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier and properties of the relationship or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelationshipElementResponse getAssetRelationship(String                        serverName,
                                                            String                        userId,
                                                            String                        relationshipTypeName,
                                                            String                        fromAssetGUID,
                                                            String                        toAssetGUID,
                                                            boolean                       forLineage,
                                                            boolean                       forDuplicateProcessing,
                                                            EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getAssetRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelationshipElementResponse response = new RelationshipElementResponse();
        AuditLog                    auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataAssetExchangeHandler handler = instanceHandler.getDataAssetExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getAssetRelationship(userId,
                                                                 requestBody.getAssetManagerGUID(),
                                                                 requestBody.getAssetManagerName(),
                                                                 relationshipTypeName,
                                                                 fromAssetGUID,
                                                                 toAssetGUID,
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
     * Update relationship between two elements.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param relationshipTypeName type name of relationship to update
     * @param relationshipGUID unique identifier of the relationship
     * @param isMergeUpdate should the new properties be merged with the existing properties, or replace them entirely
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody description and/or purpose of the relationship
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateAssetRelationship(String                  serverName,
                                                String                  userId,
                                                String                  relationshipTypeName,
                                                String                  relationshipGUID,
                                                boolean                 isMergeUpdate,
                                                boolean                 forLineage,
                                                boolean                 forDuplicateProcessing,
                                                RelationshipRequestBody requestBody)
    {
        final String methodName = "updateAssetRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                DataAssetExchangeHandler handler = instanceHandler.getDataAssetExchangeHandler(userId, serverName, methodName);

                handler.updateAssetRelationship(userId,
                                                requestBody.getAssetManagerGUID(),
                                                requestBody.getAssetManagerName(),
                                                relationshipTypeName,
                                                relationshipGUID,
                                                isMergeUpdate,
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
     * Remove the relationship between two elements.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param relationshipTypeName type name of relationship to delete
     * @param relationshipGUID unique identifier of the relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody external source ids
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearAssetRelationship(String                        serverName,
                                               String                        userId,
                                               String                        relationshipTypeName,
                                               String                        relationshipGUID,
                                               boolean                       forLineage,
                                               boolean                       forDuplicateProcessing,
                                               EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "clearAssetRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                DataAssetExchangeHandler handler = instanceHandler.getDataAssetExchangeHandler(userId, serverName, methodName);

                handler.clearAssetRelationship(userId,
                                               requestBody.getAssetManagerGUID(),
                                               requestBody.getAssetManagerName(),
                                               relationshipTypeName,
                                               relationshipGUID,
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
     * Retrieve the requested relationships linked from a specific element at end 2.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param relationshipTypeName type name of relationship to delete
     * @param fromAssetGUID unique identifier of the asset at end 1 of the relationship
     * @param startFrom start position for results
     * @param pageSize     maximum number of results
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return unique identifier and properties of the relationships or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelationshipElementsResponse getRelatedAssetsAtEnd2(String                        serverName,
                                                               String                        userId,
                                                               String                        relationshipTypeName,
                                                               String                        fromAssetGUID,
                                                               int                           startFrom,
                                                               int                           pageSize,
                                                               boolean                       forLineage,
                                                               boolean                       forDuplicateProcessing,
                                                               EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName  = "getRelatedAssetsAtEnd2";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelationshipElementsResponse response = new RelationshipElementsResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                DataAssetExchangeHandler handler = instanceHandler.getDataAssetExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.getRelatedAssetsAtEnd2(userId,
                                                                       requestBody.getAssetManagerGUID(),
                                                                       requestBody.getAssetManagerName(),
                                                                       relationshipTypeName,
                                                                       fromAssetGUID,
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
     * Retrieve the relationships linked from a specific element at end 2 of the relationship.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param relationshipTypeName type name of relationship to delete
     * @param toAssetGUID unique identifier of the asset at end 2 of the relationship
     * @param startFrom start position for results
     * @param pageSize     maximum number of results
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return unique identifier and properties of the relationships or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelationshipElementsResponse getRelatedAssetsAtEnd1(String                        serverName,
                                                               String                        userId,
                                                               String                        relationshipTypeName,
                                                               String                        toAssetGUID,
                                                               int                           startFrom,
                                                               int                           pageSize,
                                                               boolean                       forLineage,
                                                               boolean                       forDuplicateProcessing,
                                                               EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getRelatedAssetsAtEnd1";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelationshipElementsResponse response = new RelationshipElementsResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                DataAssetExchangeHandler handler = instanceHandler.getDataAssetExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.getRelatedAssetsAtEnd1(userId,
                                                                       requestBody.getAssetManagerGUID(),
                                                                       requestBody.getAssetManagerName(),
                                                                       relationshipTypeName,
                                                                       toAssetGUID,
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
     * Retrieve the list of asset metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody search parameter and correlation properties
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataAssetElementsResponse findDataAssets(String                  serverName,
                                                    String                  userId,
                                                    int                     startFrom,
                                                    int                     pageSize,
                                                    boolean                 forLineage,
                                                    boolean                 forDuplicateProcessing,
                                                    SearchStringRequestBody requestBody)
    {
        final String methodName = "findDataAssets";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DataAssetElementsResponse response = new DataAssetElementsResponse();
        AuditLog                  auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                DataAssetExchangeHandler handler = instanceHandler.getDataAssetExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.findDataAssets(userId,
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
     * Step through the assets visible to this caller.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody search parameter and correlation properties
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataAssetElementsResponse scanDataAssets(String                        serverName,
                                                    String                        userId,
                                                    int                           startFrom,
                                                    int                           pageSize,
                                                    boolean                       forLineage,
                                                    boolean                       forDuplicateProcessing,
                                                    EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "scanDataAssets";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DataAssetElementsResponse response = new DataAssetElementsResponse();
        AuditLog                  auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                DataAssetExchangeHandler handler = instanceHandler.getDataAssetExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.scanDataAssets(userId,
                                                               requestBody.getAssetManagerGUID(),
                                                               requestBody.getAssetManagerName(),
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
     * Retrieve the list of asset metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody search parameter and correlation properties
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataAssetElementsResponse getDataAssetsByName(String          serverName,
                                                         String          userId,
                                                         int             startFrom,
                                                         int             pageSize,
                                                         boolean         forLineage,
                                                         boolean         forDuplicateProcessing,
                                                         NameRequestBody requestBody)
    {
        final String methodName = "getDataAssetsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DataAssetElementsResponse response = new DataAssetElementsResponse();
        AuditLog                  auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataAssetExchangeHandler handler = instanceHandler.getDataAssetExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getDataAssetsByName(userId,
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
     * Retrieve the list of assets created on behalf of the named asset manager.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody search parameters and correlation properties
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataAssetElementsResponse getDataAssetsForAssetManager(String                        serverName,
                                                                  String                        userId,
                                                                  int                           startFrom,
                                                                  int                           pageSize,
                                                                  boolean                       forLineage,
                                                                  boolean                       forDuplicateProcessing,
                                                                  EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getDataAssetsForAssetManager";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DataAssetElementsResponse response = new DataAssetElementsResponse();
        AuditLog                  auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                DataAssetExchangeHandler handler = instanceHandler.getDataAssetExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.getDataAssetsForAssetManager(userId,
                                                                             requestBody.getAssetManagerGUID(),
                                                                             requestBody.getAssetManagerName(),
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
     * Retrieve the asset metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody correlation properties
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataAssetElementResponse getDataAssetByGUID(String                        serverName,
                                                       String                        userId,
                                                       String                        assetGUID,
                                                       boolean                       forLineage,
                                                       boolean                       forDuplicateProcessing,
                                                       EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getDataAssetByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DataAssetElementResponse response = new DataAssetElementResponse();
        AuditLog                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataAssetExchangeHandler handler = instanceHandler.getDataAssetExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getDataAssetByGUID(userId,
                                                               requestBody.getAssetManagerGUID(),
                                                               requestBody.getAssetManagerName(),
                                                               assetGUID,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               requestBody.getEffectiveTime(),
                                                               methodName));
            }
            else
            {
                response.setElement(handler.getDataAssetByGUID(userId,
                                                               null,
                                                               null,
                                                               assetGUID,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               null,
                                                               methodName));
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
