/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.server;

import org.odpi.openmetadata.accessservices.assetmanager.handlers.ExternalReferenceExchangeHandler;
import org.odpi.openmetadata.accessservices.assetmanager.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.slf4j.LoggerFactory;


/**
 * ExternalReferenceExchangeRESTServices is the server-side implementation of the Asset Manager OMAS's
 * support for glossaries.  It matches the ExternalReferenceExchangeClient.
 */
public class ExternalReferenceExchangeRESTServices
{
    private static final AssetManagerInstanceHandler instanceHandler = new AssetManagerInstanceHandler();
    private static final RESTCallLogger              restCallLogger  = new RESTCallLogger(LoggerFactory.getLogger(ExternalReferenceExchangeRESTServices.class),
                                                                                    instanceHandler.getServiceName());

    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public ExternalReferenceExchangeRESTServices()
    {
    }


    /**
     * Create a definition of an external reference.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param requestBody properties for a external reference
     *
     * @return unique identifier of the external reference or
     *  InvalidParameterException qualifiedName or userId is null; qualifiedName is not unique
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public GUIDResponse createExternalReference(String                       serverName,
                                                String                       userId,
                                                boolean                      assetManagerIsHome,
                                                ExternalReferenceRequestBody requestBody)
    {
        final String methodName = "createExternalReference";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ExternalReferenceExchangeHandler handler = instanceHandler.getExternalReferenceExchangeHandler(userId, serverName, methodName);

                response.setGUID(handler.createExternalReference(userId,
                                                                 assetManagerIsHome,
                                                                 requestBody.getMetadataCorrelationProperties(),
                                                                 requestBody.getElementProperties(),
                                                                 requestBody.getAnchorGUID(),
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
     * Update the definition of an external reference.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param externalReferenceGUID unique identifier of external reference
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to change
     *
     * @return void or
     *  InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse updateExternalReference(String                       serverName,
                                                String                       userId,
                                                String                       externalReferenceGUID,
                                                boolean                      isMergeUpdate,
                                                boolean                      forLineage,
                                                boolean                      forDuplicateProcessing,
                                                ExternalReferenceRequestBody requestBody)
    {
        final String methodName = "updateExternalReference";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ExternalReferenceExchangeHandler handler = instanceHandler.getExternalReferenceExchangeHandler(userId, serverName, methodName);

                handler.updateExternalReference(userId,
                                                requestBody.getMetadataCorrelationProperties(),
                                                externalReferenceGUID,
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
     * Remove the definition of an external reference.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param externalReferenceGUID unique identifier of external reference
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of the external reference in the external asset manager
     *
     * @return void or
     *  InvalidParameterException guid or userId is null; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse deleteExternalReference(String            serverName,
                                                String            userId,
                                                String            externalReferenceGUID,
                                                boolean           forLineage,
                                                boolean           forDuplicateProcessing,
                                                UpdateRequestBody requestBody)
    {
        final String methodName                  = "deleteExternalReference";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ExternalReferenceExchangeHandler handler = instanceHandler.getExternalReferenceExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeExternalReference(userId,
                                                requestBody.getMetadataCorrelationProperties(),
                                                externalReferenceGUID,
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
     * Link an external reference to an object.
     *
     * @param serverName name of the server to route the request to
     * @param userId the name of the calling user.
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param attachedToGUID object linked to external references.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody description for the reference from the perspective of the object that the reference is being attached to.
     * @param externalReferenceGUID unique identifier (guid) of the external reference details.
     *
     * @return Unique identifier for new relationship or
     *  InvalidParameterException problem with the GUID or the external references are not correctly specified, or are null.
     *  PropertyServerException the server is not available.
     *  UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GUIDResponse linkExternalReferenceToElement(String                           serverName,
                                                       String                           userId,
                                                       boolean                          assetManagerIsHome,
                                                       String                           attachedToGUID,
                                                       String                           externalReferenceGUID,
                                                       boolean                          forLineage,
                                                       boolean                          forDuplicateProcessing,
                                                       ExternalReferenceLinkRequestBody requestBody)
    {
        final String methodName                          = "linkExternalReferenceToElement";
        final String attachedToGUIDParameterName         = "attachedToGUID";
        final String externalReferenceGUIDParameterName  = "externalReferenceGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ExternalReferenceExchangeHandler handler = instanceHandler.getExternalReferenceExchangeHandler(userId, serverName, methodName);

                if (assetManagerIsHome)
                {
                    response.setGUID(handler.linkExternalReferenceToElement(userId,
                                                                            requestBody.getAssetManagerGUID(),
                                                                            requestBody.getAssetManagerName(),
                                                                            attachedToGUID,
                                                                            attachedToGUIDParameterName,
                                                                            externalReferenceGUID,
                                                                            externalReferenceGUIDParameterName,
                                                                            requestBody.getElementProperties(),
                                                                            forLineage,
                                                                            forDuplicateProcessing,
                                                                            requestBody.getEffectiveTime(),
                                                                            methodName));
                }
                else
                {
                    response.setGUID(handler.linkExternalReferenceToElement(userId,
                                                                            null,
                                                                            null,
                                                                            attachedToGUID,
                                                                            attachedToGUIDParameterName,
                                                                            externalReferenceGUID,
                                                                            externalReferenceGUIDParameterName,
                                                                            requestBody.getElementProperties(),
                                                                            forLineage,
                                                                            forDuplicateProcessing,
                                                                            requestBody.getEffectiveTime(),
                                                                            methodName));
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
     * Update the link between an external reference to an object.
     *
     * @param serverName name of the server to route the request to
     * @param userId the name of the calling user.
     * @param externalReferenceLinkGUID unique identifier (guid) of the external reference details.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody description for the reference from the perspective of the object that the reference is being attached to.
     *
     * @return void or
     *  InvalidParameterException problem with the GUID or the external references are not correctly specified, or are null.
     *  PropertyServerException the server is not available.
     *  UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public VoidResponse updateExternalReferenceToElementLink(String                           serverName,
                                                             String                           userId,
                                                             String                           externalReferenceLinkGUID,
                                                             boolean                          forLineage,
                                                             boolean                          forDuplicateProcessing,
                                                             ExternalReferenceLinkRequestBody requestBody)
    {
        final String methodName                          = "linkExternalReferenceToElement";
        final String externalReferenceGUIDParameterName  = "externalReferenceLinkGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ExternalReferenceExchangeHandler handler = instanceHandler.getExternalReferenceExchangeHandler(userId, serverName, methodName);

                handler.updateExternalReferenceToElementLink(userId,
                                                             requestBody.getAssetManagerGUID(),
                                                             requestBody.getAssetManagerName(),
                                                             externalReferenceLinkGUID,
                                                             externalReferenceGUIDParameterName,
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
     * Remove the link between an external reference and an element.  If the element is its anchor, the external reference is removed.
     *
     * @param serverName name of the server to route the request to
     * @param userId the name of the calling user.
     * @param externalReferenceLinkGUID identifier of the external reference relationship.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier of software server capability representing the caller
     *
     * @return void or
     *  InvalidParameterException problem with the GUID or the external references are not correctly specified, or are null.
     *  PropertyServerException the server is not available.
     *  UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public VoidResponse unlinkExternalReferenceFromElement(String                        serverName,
                                                           String                        userId,
                                                           String                        externalReferenceLinkGUID,
                                                           boolean                       forLineage,
                                                           boolean                       forDuplicateProcessing,
                                                           EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName                          = "linkExternalReferenceToElement";
        final String externalReferenceGUIDParameterName  = "externalReferenceLinkGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ExternalReferenceExchangeHandler handler = instanceHandler.getExternalReferenceExchangeHandler(userId, serverName, methodName);

                handler.unlinkExternalReferenceFromElement(userId,
                                                           requestBody.getAssetManagerGUID(),
                                                           requestBody.getAssetManagerName(),
                                                           externalReferenceLinkGUID,
                                                           externalReferenceGUIDParameterName,
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
     * Retrieve the list of external references sorted in open metadata.
     *
     * @param serverName name of the server to route the request to
     * @param userId the name of the calling user.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody the time that the retrieved elements must be effective for
     *
     * @return links to addition information or
     *  InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     *  PropertyServerException the server is not available.
     *  UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public ExternalReferenceElementsResponse getExternalReferences(String                        serverName,
                                                                   String                        userId,
                                                                   int                           startFrom,
                                                                   int                           pageSize,
                                                                   boolean                       forLineage,
                                                                   boolean                       forDuplicateProcessing,
                                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getExternalReferences";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ExternalReferenceElementsResponse response = new ExternalReferenceElementsResponse();
        AuditLog                          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ExternalReferenceExchangeHandler handler = instanceHandler.getExternalReferenceExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.getExternalReferences(userId,
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
     * Retrieve the list of external references for this requestBody.
     *
     * @param serverName name of the server to route the request to
     * @param userId the name of the calling user.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique reference id assigned by the resource owner (supports wildcards). This is the qualified name of the entity
     *
     * @return links to addition information or
     *  InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     *  PropertyServerException the server is not available.
     *  UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public ExternalReferenceElementsResponse getExternalReferencesById(String          serverName,
                                                                       String          userId,
                                                                       int             startFrom,
                                                                       int             pageSize,
                                                                       boolean         forLineage,
                                                                       boolean         forDuplicateProcessing,
                                                                       NameRequestBody requestBody)
    {
        final String methodName        = "getExternalReferencesById";
        final String nameParameterName = "resourceId";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ExternalReferenceElementsResponse response = new ExternalReferenceElementsResponse();
        AuditLog                          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ExternalReferenceExchangeHandler handler = instanceHandler.getExternalReferenceExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.getExternalReferencesById(userId,
                                                                          requestBody.getAssetManagerGUID(),
                                                                          requestBody.getAssetManagerName(),
                                                                          requestBody.getName(),
                                                                          nameParameterName,
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
     * Retrieve the list of external references for this URL.
     *
     * @param serverName name of the server to route the request to
     * @param userId the name of the calling user.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody URL of the external resource.
     *
     * @return links to addition information or
     *  InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     *  PropertyServerException the server is not available.
     *  UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public ExternalReferenceElementsResponse getExternalReferencesByURL(String          serverName,
                                                                        String          userId,
                                                                        int             startFrom,
                                                                        int             pageSize,
                                                                        boolean         forLineage,
                                                                        boolean         forDuplicateProcessing,
                                                                        NameRequestBody requestBody)
    {
        final String methodName        = "getExternalReferencesByURL";
        final String nameParameterName = "url";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ExternalReferenceElementsResponse response = new ExternalReferenceElementsResponse();
        AuditLog                          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ExternalReferenceExchangeHandler handler = instanceHandler.getExternalReferenceExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.getExternalReferencesByURL(userId,
                                                                           requestBody.getAssetManagerGUID(),
                                                                           requestBody.getAssetManagerName(),
                                                                           requestBody.getName(),
                                                                           nameParameterName,
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
     * Retrieve the list of external references for this name.
     *
     * @param serverName name of the server to route the request to
     * @param userId the name of the calling user.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody name of the external resource.
     *
     * @return links to addition information or
     *  InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     *  PropertyServerException the server is not available.
     *  UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public ExternalReferenceElementsResponse getExternalReferencesByName(String          serverName,
                                                                         String          userId,
                                                                         int             startFrom,
                                                                         int             pageSize,
                                                                         boolean         forLineage,
                                                                         boolean         forDuplicateProcessing,
                                                                         NameRequestBody requestBody)
    {
        final String methodName        = "getExternalReferencesByName";
        final String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ExternalReferenceElementsResponse response = new ExternalReferenceElementsResponse();
        AuditLog                          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ExternalReferenceExchangeHandler handler = instanceHandler.getExternalReferenceExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.getExternalReferencesByName(userId,
                                                                            requestBody.getAssetManagerGUID(),
                                                                            requestBody.getAssetManagerName(),
                                                                            requestBody.getName(),
                                                                            nameParameterName,
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
     * Retrieve the list of external reference created on behalf of the named asset manager.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody the time that the retrieved elements must be effective for
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ExternalReferenceElementsResponse getExternalReferencesForAssetManager(String                        serverName,
                                                                                  String                        userId,
                                                                                  int                           startFrom,
                                                                                  int                           pageSize,
                                                                                  boolean                       forLineage,
                                                                                  boolean                       forDuplicateProcessing,
                                                                                  EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getExternalReferencesForAssetManager";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ExternalReferenceElementsResponse response = new ExternalReferenceElementsResponse();
        AuditLog                          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ExternalReferenceExchangeHandler handler = instanceHandler.getExternalReferenceExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.getExternalReferencesForAssetManager(userId,
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
     * Find the external references that contain the search string - which may contain wildcards.
     *
     * @param serverName name of the server to route the request to
     * @param userId the name of the calling user.
     * @param requestBody regular expression (RegEx) to search for
     * @param startFrom  index of the list to start from (0 for start)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param pageSize   maximum number of elements to return.
     *
     * @return links to addition information or
     *  InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     *  PropertyServerException the server is not available.
     *  UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public ExternalReferenceElementsResponse findExternalReferences(String                  serverName,
                                                                    String                  userId,
                                                                    int                     startFrom,
                                                                    int                     pageSize,
                                                                    boolean                 forLineage,
                                                                    boolean                 forDuplicateProcessing,
                                                                    SearchStringRequestBody requestBody)
    {
        final String methodName                = "findExternalReferences";
        final String searchStringParameterName = "requestBody";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ExternalReferenceElementsResponse response = new ExternalReferenceElementsResponse();
        AuditLog                          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ExternalReferenceExchangeHandler handler = instanceHandler.getExternalReferenceExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.findExternalReferences(userId,
                                                                       requestBody.getAssetManagerGUID(),
                                                                       requestBody.getAssetManagerName(),
                                                                       requestBody.getSearchString(),
                                                                       searchStringParameterName,
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
     * Retrieve the list of external references attached to the supplied object.
     *
     * @param serverName name of the server to route the request to
     * @param userId the name of the calling user.
     * @param attachedToGUID object linked to external reference.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody the time that the retrieved elements must be effective for
     *
     * @return links to addition information or
     *  InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     *  PropertyServerException the server is not available.
     *  UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public ExternalReferenceLinkElementsResponse retrieveAttachedExternalReferences(String                        serverName,
                                                                                    String                        userId,
                                                                                    String                        attachedToGUID,
                                                                                    int                           startFrom,
                                                                                    int                           pageSize,
                                                                                    boolean                       forLineage,
                                                                                    boolean                       forDuplicateProcessing,
                                                                                    EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName        = "retrieveAttachedExternalReferences";
        final String guidParameterName = "attachedToGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ExternalReferenceLinkElementsResponse response = new ExternalReferenceLinkElementsResponse();
        AuditLog                              auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ExternalReferenceExchangeHandler handler = instanceHandler.getExternalReferenceExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.retrieveAttachedExternalReferences(userId,
                                                                                   requestBody.getAssetManagerGUID(),
                                                                                   requestBody.getAssetManagerName(),
                                                                                   attachedToGUID,
                                                                                   guidParameterName,
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
     * Return information about a specific external reference.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param externalReferenceGUID unique identifier for the external reference
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody the time that the retrieved elements must be effective for
     *
     * @return properties of the external reference or
     *  InvalidParameterException externalReferenceGUID or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public ExternalReferenceElementResponse getExternalReferenceByGUID(String                        serverName,
                                                                       String                        userId,
                                                                       String                        externalReferenceGUID,
                                                                       boolean                       forLineage,
                                                                       boolean                       forDuplicateProcessing,
                                                                       EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getExternalReferenceByGUID";
        final String guidParameterName = "externalReferenceGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ExternalReferenceElementResponse response = new ExternalReferenceElementResponse();
        AuditLog                         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ExternalReferenceExchangeHandler handler = instanceHandler.getExternalReferenceExchangeHandler(userId, serverName, methodName);

                response.setElement(handler.getExternalReferenceByGUID(userId,
                                                                       requestBody.getAssetManagerGUID(),
                                                                       requestBody.getAssetManagerName(),
                                                                       externalReferenceGUID,
                                                                       guidParameterName,
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
