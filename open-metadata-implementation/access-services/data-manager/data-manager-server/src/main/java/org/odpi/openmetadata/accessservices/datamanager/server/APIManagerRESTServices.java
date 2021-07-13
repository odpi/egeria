/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.server;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.APIElement;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.APIOperationElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.APIParameterListElement;
import org.odpi.openmetadata.accessservices.datamanager.properties.APIParameterListType;
import org.odpi.openmetadata.accessservices.datamanager.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.APIOperationHandler;
import org.odpi.openmetadata.commonservices.generichandlers.APIParameterListHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * APIManagerRESTServices is the server-side implementation of the Data Manager OMAS's
 * support for apis.  It matches the APIManagerClient.
 */
public class APIManagerRESTServices
{
    private static DataManagerInstanceHandler instanceHandler = new DataManagerInstanceHandler();
    private static RESTCallLogger             restCallLogger  = new RESTCallLogger(LoggerFactory.getLogger(APIManagerRESTServices.class),
                                                                                   instanceHandler.getServiceName());

    private RESTExceptionHandler     restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public APIManagerRESTServices()
    {
    }

    /**
     * Create a new metadata element to represent a api.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiManagerIsHome should the API be marked as owned by the event broker so others can not update?
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createAPI(String           serverName,
                                  String           userId,
                                  boolean          apiManagerIsHome,
                                  APIRequestBody requestBody)
    {
        final String methodName                   = "createAPI";
        final String apiManagerGUIDParameterName = "apiManagerGUID";
        final String apiGUIDParameterName       = "apiGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<APIElement> handler = instanceHandler.getAPIHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String typeName = OpenMetadataAPIMapper.DEPLOYED_API_TYPE_NAME;

                if (requestBody.getTypeName() != null)
                {
                    typeName = requestBody.getTypeName();
                }

                String apiGUID;

                if (apiManagerIsHome)
                {
                    apiGUID = handler.createAssetInRepository(userId,
                                                              requestBody.getExternalSourceGUID(),
                                                              requestBody.getExternalSourceName(),
                                                              requestBody.getQualifiedName(),
                                                              requestBody.getDisplayName(),
                                                              requestBody.getDescription(),
                                                              requestBody.getAdditionalProperties(),
                                                              typeName,
                                                              requestBody.getExtendedProperties(),
                                                              InstanceStatus.ACTIVE,
                                                              methodName);

                    if ((apiGUID != null) && (requestBody.getExternalSourceGUID() != null))
                    {
                        handler.linkElementToElement(userId,
                                                     requestBody.getExternalSourceGUID(),
                                                     requestBody.getExternalSourceName(),
                                                     requestBody.getExternalSourceGUID(),
                                                     apiManagerGUIDParameterName,
                                                     OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                     apiGUID,
                                                     apiGUIDParameterName,
                                                     OpenMetadataAPIMapper.DEPLOYED_API_TYPE_NAME,
                                                     OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                     OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                                     null,
                                                     methodName);
                    }
                }
                else
                {
                    apiGUID = handler.createAssetInRepository(userId,
                                                              null,
                                                              null,
                                                              requestBody.getQualifiedName(),
                                                              requestBody.getDisplayName(),
                                                              requestBody.getDescription(),
                                                              requestBody.getAdditionalProperties(),
                                                              typeName,
                                                              requestBody.getExtendedProperties(),
                                                              InstanceStatus.ACTIVE,
                                                              methodName);

                    if ((apiGUID != null) && (requestBody.getExternalSourceGUID() != null))
                    {
                        handler.linkElementToElement(userId,
                                                     null,
                                                     null,
                                                     requestBody.getExternalSourceGUID(),
                                                     apiManagerGUIDParameterName,
                                                     OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                     apiGUID,
                                                     apiGUIDParameterName,
                                                     OpenMetadataAPIMapper.DEPLOYED_API_TYPE_NAME,
                                                     OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                     OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                                     null,
                                                     methodName);
                    }
                }

                if (apiGUID != null)
                {
                    if (requestBody.getVendorProperties() != null)
                    {
                        handler.setVendorProperties(userId,
                                                    apiGUID,
                                                    requestBody.getVendorProperties(),
                                                    methodName);
                    }
                }

                response.setGUID(apiGUID);
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
     * Create a new metadata element to represent a API using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param apiManagerIsHome should the API be marked as owned by the event broker so others can not update?
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createAPIFromTemplate(String              serverName,
                                              String              userId,
                                              String              templateGUID,
                                              boolean             apiManagerIsHome,
                                              TemplateRequestBody requestBody)
    {
        final String methodName                   = "createAPIFromTemplate";
        final String apiManagerGUIDParameterName = "apiManagerGUID";
        final String apiGUIDParameterName       = "apiGUID";
        final String templateGUIDParameterName    = "templateGUID";
        final String qualifiedNameParameterName   = "qualifiedName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<APIElement> handler = instanceHandler.getAPIHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String apiGUID;

                if (apiManagerIsHome)
                {
                    apiGUID = handler.addAssetFromTemplate(userId,
                                                           requestBody.getExternalSourceGUID(),
                                                           requestBody.getExternalSourceName(),
                                                           templateGUID,
                                                           templateGUIDParameterName,
                                                           OpenMetadataAPIMapper.DEPLOYED_API_TYPE_GUID,
                                                           OpenMetadataAPIMapper.DEPLOYED_API_TYPE_NAME,
                                                           requestBody.getQualifiedName(),
                                                           qualifiedNameParameterName,
                                                           requestBody.getDisplayName(),
                                                           requestBody.getDescription(),
                                                           requestBody.getNetworkAddress(),
                                                           methodName);

                    if ((apiGUID != null) && (requestBody.getExternalSourceGUID() != null))
                    {
                        handler.linkElementToElement(userId,
                                                     requestBody.getExternalSourceGUID(),
                                                     requestBody.getExternalSourceName(),
                                                     requestBody.getExternalSourceGUID(),
                                                     apiManagerGUIDParameterName,
                                                     OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                     apiGUID,
                                                     apiGUIDParameterName,
                                                     OpenMetadataAPIMapper.DEPLOYED_API_TYPE_NAME,
                                                     OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                     OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                                     null,
                                                     methodName);
                    }
                }
                else
                {
                    apiGUID = handler.addAssetFromTemplate(userId,
                                                           null,
                                                           null,
                                                           templateGUID,
                                                           templateGUIDParameterName,
                                                           OpenMetadataAPIMapper.DEPLOYED_API_TYPE_GUID,
                                                           OpenMetadataAPIMapper.DEPLOYED_API_TYPE_NAME,
                                                           requestBody.getQualifiedName(),
                                                           qualifiedNameParameterName,
                                                           requestBody.getDisplayName(),
                                                           requestBody.getDescription(),
                                                           requestBody.getNetworkAddress(),
                                                           methodName);

                    if ((apiGUID != null) && (requestBody.getExternalSourceGUID() != null))
                    {
                        handler.linkElementToElement(userId,
                                                     null,
                                                     null,
                                                     requestBody.getExternalSourceGUID(),
                                                     apiManagerGUIDParameterName,
                                                     OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                     apiGUID,
                                                     apiGUIDParameterName,
                                                     OpenMetadataAPIMapper.DEPLOYED_API_TYPE_NAME,
                                                     OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                     OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                                     null,
                                                     methodName);
                    }
                }

                response.setGUID(apiGUID);
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
     * Update the metadata element representing a api.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with the existing properties of overlay them?
     * @param requestBody new properties for this element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateAPI(String           serverName,
                                  String           userId,
                                  String           apiGUID,
                                  boolean          isMergeUpdate,
                                  APIRequestBody requestBody)
    {
        final String methodName = "updateAPI";
        final String apiGUIDParameterName = "apiGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<APIElement> handler = instanceHandler.getAPIHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String typeName = OpenMetadataAPIMapper.DEPLOYED_API_TYPE_NAME;

                if (requestBody.getTypeName() != null)
                {
                    typeName = requestBody.getTypeName();
                }

                handler.updateAsset(userId,
                                    requestBody.getExternalSourceGUID(),
                                    requestBody.getExternalSourceName(),
                                    apiGUID,
                                    apiGUIDParameterName,
                                    requestBody.getQualifiedName(),
                                    requestBody.getDisplayName(),
                                    requestBody.getDescription(),
                                    requestBody.getAdditionalProperties(),
                                    typeName,
                                    requestBody.getExtendedProperties(),
                                    isMergeUpdate,
                                    methodName);

                if (requestBody.getVendorProperties() == null)
                {
                    if (!isMergeUpdate)
                    {
                        // todo delete vendor properties
                    }
                }
                else
                {
                    // todo update vendor properties
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
     * Update the zones for the API asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Manager OMAS).
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiGUID unique identifier of the metadata element to publish
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse publishAPI(String          serverName,
                                   String          userId,
                                   String          apiGUID,
                                   NullRequestBody nullRequestBody)
    {
        final String methodName = "publishAPI";
        final String apiGUIDParameterName = "apiGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<APIElement> handler = instanceHandler.getAPIHandler(userId, serverName, methodName);

            handler.publishAsset(userId, apiGUID, apiGUIDParameterName, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the zones for the API asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Manager OMAS.  This is the setting when the API is first created).
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiGUID unique identifier of the metadata element to withdraw
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse withdrawAPI(String          serverName,
                                    String          userId,
                                    String          apiGUID,
                                    NullRequestBody nullRequestBody)
    {
        final String methodName = "withdrawAPI";
        final String apiGUIDParameterName = "apiGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<APIElement> handler = instanceHandler.getAPIHandler(userId, serverName, methodName);

            handler.withdrawAsset(userId, apiGUID, apiGUIDParameterName, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the metadata element representing a api.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeAPI(String                    serverName,
                                  String                    userId,
                                  String                    apiGUID,
                                  String                    qualifiedName,
                                  MetadataSourceRequestBody requestBody)
    {
        final String methodName = "removeAPI";
        final String apiGUIDParameterName = "apiGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<APIElement> handler = instanceHandler.getAPIHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.deleteBeanInRepository(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               apiGUID,
                                               apiGUIDParameterName,
                                               OpenMetadataAPIMapper.DEPLOYED_API_TYPE_GUID,
                                               OpenMetadataAPIMapper.DEPLOYED_API_TYPE_NAME,
                                               OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                               qualifiedName,
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
     * Retrieve the list of API metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public APIsResponse findAPIs(String serverName,
                                 String userId,
                                 String searchString,
                                 int    startFrom,
                                 int    pageSize)
    {
        final String methodName = "findAPIs";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        APIsResponse response = new APIsResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<APIElement> handler = instanceHandler.getAPIHandler(userId, serverName, methodName);

            List<APIElement> apiAssets = handler.findAssets(userId,
                                                            OpenMetadataAPIMapper.DEPLOYED_API_TYPE_GUID,
                                                            OpenMetadataAPIMapper.DEPLOYED_API_TYPE_NAME,
                                                            searchString,
                                                            searchStringParameterName,
                                                            startFrom,
                                                            pageSize,
                                                            methodName);

            response.setElementList(apiAssets);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of API metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public APIsResponse   getAPIsByName(String serverName,
                                        String userId,
                                        String name,
                                        int    startFrom,
                                        int    pageSize)
    {
        final String methodName = "getAPIsByName";
        final String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        APIsResponse response = new APIsResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<APIElement> handler = instanceHandler.getAPIHandler(userId, serverName, methodName);

            List<APIElement> apiAssets = handler.getAssetsByName(userId,
                                                                 OpenMetadataAPIMapper.DEPLOYED_API_TYPE_GUID,
                                                                 OpenMetadataAPIMapper.DEPLOYED_API_TYPE_NAME,
                                                                 name,
                                                                 nameParameterName,
                                                                 startFrom,
                                                                 pageSize,
                                                                 methodName);

            response.setElementList(apiAssets);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of apis created by this caller.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the event broker
     * @param apiManagerName unique name of software server capability representing the event broker
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public APIsResponse   getAPIsForAPIManager(String serverName,
                                               String userId,
                                               String apiManagerGUID,
                                               String apiManagerName,
                                               int    startFrom,
                                               int    pageSize)
    {
        final String methodName = "getAPIsForAPIManager";
        final String apiManagerGUIDParameterName = "apiManagerGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        APIsResponse response = new APIsResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<APIElement> handler = instanceHandler.getAPIHandler(userId, serverName, methodName);

            List<APIElement> apiAssets = handler.getAttachedElements(userId,
                                                                     apiManagerGUID,
                                                                     apiManagerGUIDParameterName,
                                                                     OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                                     OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                                     OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                                                     OpenMetadataAPIMapper.DEPLOYED_API_TYPE_NAME,
                                                                     startFrom,
                                                                     pageSize,
                                                                     methodName);

            response.setElementList(apiAssets);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the API metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public APIResponse getAPIByGUID(String serverName,
                                    String userId,
                                    String guid)
    {
        final String methodName = "getAPIByGUID";
        final String guidParameterName = "apiGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        APIResponse response = new APIResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<APIElement> handler = instanceHandler.getAPIHandler(userId, serverName, methodName);

            APIElement apiAsset = handler.getBeanFromRepository(userId,
                                                                guid,
                                                                guidParameterName,
                                                                OpenMetadataAPIMapper.DEPLOYED_API_TYPE_NAME,
                                                                methodName);

            response.setElement(apiAsset);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /* ============================================================================
     * A api may host one or more API operations depending on its capability
     */

    /**
     * Create a new metadata element to represent a API operation.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiManagerIsHome should the API operation be marked as owned by the event broker so others can not update?
     * @param apiGUID unique identifier of the topic where the schema is located
     * @param requestBody properties about the API operation
     *
     * @return unique identifier of the new API operation or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createAPIOperation(String                  serverName,
                                           String                  userId,
                                           String                  apiGUID,
                                           boolean                 apiManagerIsHome,
                                           APIOperationRequestBody requestBody)
    {
        final String methodName = "createAPIOperation";
        final String apiGUIDParameterName = "apiGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            APIOperationHandler<APIOperationElement> handler = instanceHandler.getAPIOperationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String apiOperationGUID;

                if (apiManagerIsHome)
                {
                    apiOperationGUID = handler.createAPIOperation(userId,
                                                                  requestBody.getExternalSourceGUID(),
                                                                  requestBody.getExternalSourceName(),
                                                                  apiGUID,
                                                                  apiGUIDParameterName,
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
                                                                  methodName);
                }
                else
                {
                    apiOperationGUID = handler.createAPIOperation(userId,
                                                                  null,
                                                                  null,
                                                                  apiGUID,
                                                                  apiGUIDParameterName,
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
                                                                  methodName);
                }

                if (requestBody.getVendorProperties() != null)
                {
                    handler.setVendorProperties(userId,
                                                apiGUID,
                                                requestBody.getVendorProperties(),
                                                methodName);
                }

                response.setGUID(apiOperationGUID);
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
     * Create a new metadata element to represent a API operation using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param apiGUID unique identifier of the topic where the schema is located
     * @param apiManagerIsHome should the API operation be marked as owned by the event broker so others can not update?
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new API operation or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createAPIOperationFromTemplate(String              serverName,
                                                       String              userId,
                                                       String              templateGUID,
                                                       String              apiGUID,
                                                       boolean             apiManagerIsHome,
                                                       TemplateRequestBody requestBody)
    {
        final String methodName = "createAPIOperationFromTemplate";
        final String apiGUIDParameterName = "apiGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            APIOperationHandler<APIOperationElement> handler = instanceHandler.getAPIOperationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (apiManagerIsHome)
                {
                    response.setGUID(handler.createAPIOperationFromTemplate(userId,
                                                                            requestBody.getExternalSourceGUID(),
                                                                            requestBody.getExternalSourceName(),
                                                                            apiGUID,
                                                                            apiGUIDParameterName,
                                                                            templateGUID,
                                                                            requestBody.getQualifiedName(),
                                                                            requestBody.getDisplayName(),
                                                                            requestBody.getDescription(),
                                                                            methodName));
                }
                else
                {
                    response.setGUID(handler.createAPIOperationFromTemplate(userId,
                                                                            null,
                                                                            null,
                                                                            apiGUID,
                                                                            apiGUIDParameterName,
                                                                            templateGUID,
                                                                            requestBody.getQualifiedName(),
                                                                            requestBody.getDisplayName(),
                                                                            requestBody.getDescription(),
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
     * Update the metadata element representing a API operation.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiOperationGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateAPIOperation(String                  serverName,
                                           String                  userId,
                                           String                  apiOperationGUID,
                                           boolean                 isMergeUpdate,
                                           APIOperationRequestBody requestBody)
    {
        final String methodName = "updateAPIOperation";
        final String apiOperationGUIDParameterName = "apiOperationGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            APIOperationHandler<APIOperationElement> handler = instanceHandler.getAPIOperationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.updateAPIOperation(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           apiOperationGUID,
                                           apiOperationGUIDParameterName,
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
                                           isMergeUpdate,
                                           methodName);

                if (requestBody.getVendorProperties() == null)
                {
                    if (!isMergeUpdate)
                    {
                        // todo delete vendor properties
                    }
                }
                else
                {
                    // todo update vendor properties
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
     * Remove the metadata element representing an API operation.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiOperationGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeAPIOperation(String                    serverName,
                                           String                    userId,
                                           String                    apiOperationGUID,
                                           String                    qualifiedName,
                                           MetadataSourceRequestBody requestBody)
    {
        final String methodName = "removeAPIOperation";
        final String apiOperationGUIDParameterName = "apiOperationGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            APIOperationHandler<APIOperationElement> handler = instanceHandler.getAPIOperationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeAPIOperation(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           apiOperationGUID,
                                           apiOperationGUIDParameterName,
                                           qualifiedName,
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
     * Retrieve the list of API operation metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public APIOperationsResponse findAPIOperations(String serverName,
                                                   String userId,
                                                   String searchString,
                                                   int    startFrom,
                                                   int    pageSize)
    {
        final String methodName = "findAPIOperations";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        APIOperationsResponse response = new APIOperationsResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            APIOperationHandler<APIOperationElement> handler = instanceHandler.getAPIOperationHandler(userId, serverName, methodName);

            List<APIOperationElement> elements = handler.findAPIOperations(userId,
                                                                           searchString,
                                                                           searchStringParameterName,
                                                                           startFrom,
                                                                           pageSize,
                                                                           methodName);

            response.setElementList(elements);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the list of operation associated with an API.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiGUID unique identifier of the topic to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the schemas associated with the requested topic or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public APIOperationsResponse getAPIOperationsForAPI(String serverName,
                                                        String userId,
                                                        String apiGUID,
                                                        int    startFrom,
                                                        int    pageSize)
    {
        final String methodName = "getAPIOperationsForTopic";
        final String apiGUIDParameterName = "apiGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        APIOperationsResponse response = new APIOperationsResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            APIOperationHandler<APIOperationElement> handler = instanceHandler.getAPIOperationHandler(userId, serverName, methodName);

            List<APIOperationElement> elements = handler.getAPIOperationsForAPI(userId,
                                                                                apiGUID,
                                                                                apiGUIDParameterName,
                                                                                startFrom,
                                                                                pageSize,
                                                                                methodName);

            response.setElementList(elements);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of API operation metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public APIOperationsResponse getAPIOperationsByName(String serverName,
                                                        String userId,
                                                        String name,
                                                        int    startFrom,
                                                        int    pageSize)
    {
        final String methodName = "getAPIOperationsByName";
        final String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        APIOperationsResponse response = new APIOperationsResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            APIOperationHandler<APIOperationElement> handler = instanceHandler.getAPIOperationHandler(userId, serverName, methodName);

            List<APIOperationElement> elements = handler.getAPIOperationsByName(userId, name, nameParameterName, startFrom, pageSize, methodName);

            response.setElementList(elements);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the API operation metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public APIOperationResponse getAPIOperationByGUID(String serverName,
                                                      String userId,
                                                      String guid)
    {
        final String methodName = "getAPIOperationByGUID";
        final String apiOperationGUIDParameterName = "apiOperationGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        APIOperationResponse response = new APIOperationResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            APIOperationHandler<APIOperationElement> handler = instanceHandler.getAPIOperationHandler(userId, serverName, methodName);

            APIOperationElement element = handler.getAPIOperationByGUID(userId, guid, apiOperationGUIDParameterName, methodName);

            response.setElement(element);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }

    /*
     * An API Operation may support a header, a request and a response parameter list depending on its capability
     */

    /**
     * Create a new metadata element to represent a API parameter list.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiOperationGUID unique identifier of the topic where the schema is located
     * @param parameterListType is this a header, request or response
     * @param apiManagerIsHome should the API parameter list be marked as owned by the event broker so others can not update?
     * @param requestBody properties about the API parameter list
     *
     * @return unique identifier of the new API parameter list or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createAPIParameterList(String                      serverName,
                                               String                      userId,
                                               String                      apiOperationGUID,
                                               APIParameterListType        parameterListType,
                                               boolean                     apiManagerIsHome,
                                               APIParameterListRequestBody requestBody)
    {
        final String methodName = "createAPIParameterList";
        final String apiOperationGUIDParameterName = "apiOperationGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            APIParameterListHandler<APIParameterListElement> handler = instanceHandler.getAPIParameterListHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String apiParameterListGUID;

                if (apiManagerIsHome)
                {
                    apiParameterListGUID = handler.createAPIParameterList(userId,
                                                                          requestBody.getExternalSourceGUID(),
                                                                          requestBody.getExternalSourceName(),
                                                                          apiOperationGUID,
                                                                          apiOperationGUIDParameterName,
                                                                          requestBody.getQualifiedName(),
                                                                          requestBody.getDisplayName(),
                                                                          requestBody.getDescription(),
                                                                          requestBody.getVersionNumber(),
                                                                          requestBody.getIsDeprecated(),
                                                                          requestBody.getAuthor(),
                                                                          requestBody.getUsage(),
                                                                          requestBody.getEncodingStandard(),
                                                                          requestBody.getNamespace(),
                                                                          requestBody.getRequired(),
                                                                          requestBody.getAdditionalProperties(),
                                                                          requestBody.getTypeName(),
                                                                          requestBody.getExtendedProperties(),
                                                                          this.getRelationshipType(parameterListType),
                                                                          methodName);
                }
                else
                {
                    apiParameterListGUID = handler.createAPIParameterList(userId,
                                                                          null,
                                                                          null,
                                                                          apiOperationGUID,
                                                                          apiOperationGUIDParameterName,
                                                                          requestBody.getQualifiedName(),
                                                                          requestBody.getDisplayName(),
                                                                          requestBody.getDescription(),
                                                                          requestBody.getVersionNumber(),
                                                                          requestBody.getIsDeprecated(),
                                                                          requestBody.getAuthor(),
                                                                          requestBody.getUsage(),
                                                                          requestBody.getEncodingStandard(),
                                                                          requestBody.getNamespace(),
                                                                          requestBody.getRequired(),
                                                                          requestBody.getAdditionalProperties(),
                                                                          requestBody.getTypeName(),
                                                                          requestBody.getExtendedProperties(),
                                                                          this.getRelationshipType(parameterListType),
                                                                          methodName);
                }

                if (requestBody.getVendorProperties() != null)
                {
                    handler.setVendorProperties(userId,
                                                apiOperationGUID,
                                                requestBody.getVendorProperties(),
                                                methodName);
                }

                response.setGUID(apiParameterListGUID);
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
     * Convert the parameter type into a relationship name.
     *
     * @param parameterListType type enum
     * @return relationship name
     */
    private String getRelationshipType(APIParameterListType parameterListType)
    {
        String relationshipTypeName = OpenMetadataAPIMapper.API_HEADER_RELATIONSHIP_TYPE_NAME;

        if (APIParameterListType.REQUEST.equals(parameterListType))
        {
            relationshipTypeName = OpenMetadataAPIMapper.API_REQUEST_RELATIONSHIP_TYPE_NAME;
        }
        else if (APIParameterListType.RESPONSE.equals(parameterListType))
        {
            relationshipTypeName = OpenMetadataAPIMapper.API_RESPONSE_RELATIONSHIP_TYPE_NAME;
        }

        return relationshipTypeName;
    }


    /**
     * Create a new metadata element to represent a API parameter list using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param apiOperationGUID unique identifier of the topic where the schema is located
     * @param parameterListType is this a header, request or response
     * @param apiManagerIsHome should the API parameter list be marked as owned by the event broker so others can not update?
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new API parameter list or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createAPIParameterListFromTemplate(String               serverName,
                                                           String               userId,
                                                           String               templateGUID,
                                                           String               apiOperationGUID,
                                                           APIParameterListType parameterListType,
                                                           boolean              apiManagerIsHome,
                                                           TemplateRequestBody  requestBody)
    {
        final String methodName = "createAPIParameterListFromTemplate";
        final String apiOperationGUIDParameterName = "apiOperationGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            APIParameterListHandler<APIParameterListElement> handler = instanceHandler.getAPIParameterListHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (apiManagerIsHome)
                {
                    response.setGUID(handler.createAPIParameterListFromTemplate(userId,
                                                                                requestBody.getExternalSourceGUID(),
                                                                                requestBody.getExternalSourceName(),
                                                                                apiOperationGUID,
                                                                                apiOperationGUIDParameterName,
                                                                                templateGUID,
                                                                                requestBody.getQualifiedName(),
                                                                                requestBody.getDisplayName(),
                                                                                requestBody.getDescription(),
                                                                                this.getRelationshipType(parameterListType),
                                                                                methodName));
                }
                else
                {
                    response.setGUID(handler.createAPIParameterListFromTemplate(userId,
                                                                                null,
                                                                                null,
                                                                                apiOperationGUID,
                                                                                apiOperationGUIDParameterName,
                                                                                templateGUID,
                                                                                requestBody.getQualifiedName(),
                                                                                requestBody.getDisplayName(),
                                                                                requestBody.getDescription(),
                                                                                this.getRelationshipType(parameterListType),
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
     * Update the metadata element representing a API parameter list.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiParameterListGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateAPIParameterList(String               serverName,
                                               String               userId,
                                               String               apiParameterListGUID,
                                               boolean              isMergeUpdate,
                                               APIParameterListRequestBody requestBody)
    {
        final String methodName = "updateAPIParameterList";
        final String apiParameterListGUIDParameterName = "apiParameterListGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            APIParameterListHandler<APIParameterListElement> handler = instanceHandler.getAPIParameterListHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.updateAPIParameterList(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               apiParameterListGUID,
                                               apiParameterListGUIDParameterName,
                                               requestBody.getQualifiedName(),
                                               requestBody.getDisplayName(),
                                               requestBody.getDescription(),
                                               requestBody.getVersionNumber(),
                                               requestBody.getIsDeprecated(),
                                               requestBody.getAuthor(),
                                               requestBody.getUsage(),
                                               requestBody.getEncodingStandard(),
                                               requestBody.getNamespace(),
                                               requestBody.getRequired(),
                                               requestBody.getAdditionalProperties(),
                                               requestBody.getTypeName(),
                                               requestBody.getExtendedProperties(),
                                               isMergeUpdate,
                                               methodName);

                if (requestBody.getVendorProperties() == null)
                {
                    if (!isMergeUpdate)
                    {
                        // todo delete vendor properties
                    }
                }
                else
                {
                    // todo update vendor properties
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
     * Remove the metadata element representing a API parameter list.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiParameterListGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeAPIParameterList(String                    serverName,
                                               String                    userId,
                                               String                    apiParameterListGUID,
                                               String                    qualifiedName,
                                               MetadataSourceRequestBody requestBody)
    {
        final String methodName = "removeAPIParameterList";
        final String apiParameterListGUIDParameterName = "apiParameterListGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            APIParameterListHandler<APIParameterListElement> handler = instanceHandler.getAPIParameterListHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeAPIParameterList(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               apiParameterListGUID,
                                               apiParameterListGUIDParameterName,
                                               qualifiedName,
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
     * Retrieve the list of API parameter list metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public APIParameterListsResponse findAPIParameterLists(String serverName,
                                                           String userId,
                                                           String searchString,
                                                           int    startFrom,
                                                           int    pageSize)
    {
        final String methodName = "findAPIParameterLists";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        APIParameterListsResponse response = new APIParameterListsResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            APIParameterListHandler<APIParameterListElement> handler = instanceHandler.getAPIParameterListHandler(userId, serverName, methodName);

            List<APIParameterListElement> topicSchemaAssets = handler.findAPIParameterLists(userId,
                                                                                            searchString,
                                                                                            searchStringParameterName,
                                                                                            startFrom,
                                                                                            pageSize,
                                                                                            methodName);

            response.setElementList(topicSchemaAssets);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the list of schemas associated with a topic.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiOperationGUID unique identifier of the topic to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the schemas associated with the requested topic or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public APIParameterListsResponse getAPIParameterListsForOperation(String serverName,
                                                                      String userId,
                                                                      String apiOperationGUID,
                                                                      int    startFrom,
                                                                      int    pageSize)
    {
        final String methodName = "getAPIParameterListsForTopic";
        final String apiOperationGUIDParameterName = "apiOperationGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        APIParameterListsResponse response = new APIParameterListsResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            APIParameterListHandler<APIParameterListElement> handler = instanceHandler.getAPIParameterListHandler(userId, serverName, methodName);

            List<APIParameterListElement> topicSchemaAssets = handler.getAPIParameterListsForOperation(userId,
                                                                                                       apiOperationGUID,
                                                                                                       apiOperationGUIDParameterName,
                                                                                                       startFrom,
                                                                                                       pageSize,
                                                                                                       methodName);

            response.setElementList(topicSchemaAssets);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of API parameter list metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public APIParameterListsResponse getAPIParameterListsByName(String serverName,
                                                                String userId,
                                                                String name,
                                                                int    startFrom,
                                                                int    pageSize)
    {
        final String methodName = "getAPIParameterListsByName";
        final String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        APIParameterListsResponse response = new APIParameterListsResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            APIParameterListHandler<APIParameterListElement> handler = instanceHandler.getAPIParameterListHandler(userId, serverName, methodName);

            List<APIParameterListElement> topicSchemaAssets = handler.getAPIParameterListsByName(userId, name, nameParameterName, startFrom, pageSize, methodName);

            response.setElementList(topicSchemaAssets);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the API parameter list metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public APIParameterListResponse getAPIParameterListByGUID(String serverName,
                                                              String userId,
                                                              String guid)
    {
        final String methodName = "getAPIParameterListByGUID";
        final String apiParameterListGUIDParameterName = "apiParameterListGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        APIParameterListResponse response = new APIParameterListResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            APIParameterListHandler<APIParameterListElement> handler = instanceHandler.getAPIParameterListHandler(userId, serverName, methodName);

            APIParameterListElement topicSchemaAsset = handler.getAPIParameterListByGUID(userId, guid, apiParameterListGUIDParameterName, methodName);

            response.setElement(topicSchemaAsset);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
