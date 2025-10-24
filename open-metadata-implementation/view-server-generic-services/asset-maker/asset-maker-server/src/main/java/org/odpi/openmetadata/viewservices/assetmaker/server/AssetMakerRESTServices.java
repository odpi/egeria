/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetmaker.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.AssetHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworkservices.omf.rest.OpenMetadataRelationshipResponse;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;


/**
 * The AssetMakerRESTServices provides the server-side implementation of the Asset Maker Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class AssetMakerRESTServices extends TokenController
{
    private static final AssetMakerInstanceHandler instanceHandler = new AssetMakerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(AssetMakerRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public AssetMakerRESTServices()
    {
    }


    /**
     * Create an asset.
     *
     * @param serverName                 name of called server.
     * @param urlMarker  view service URL marker
     * @param requestBody             properties for the asset.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createAsset(String                serverName,
                                    String                urlMarker,
                                    NewElementRequestBody requestBody)
    {
        final String methodName = "createAsset";

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
                AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof AssetProperties assetProperties)
                {
                    response.setGUID(handler.createAsset(userId,
                                                         requestBody,
                                                         requestBody.getInitialClassifications(),
                                                         assetProperties,
                                                         requestBody.getParentRelationshipProperties()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(AssetProperties.class.getName(), methodName);
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
     * Create a new metadata element to represent an asset using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param urlMarker  view service URL marker
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createAssetFromTemplate(String              serverName,
                                                String              urlMarker,
                                                TemplateRequestBody requestBody)
    {
        final String methodName = "createAssetFromTemplate";

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
                AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

                response.setGUID(handler.createAssetFromTemplate(userId,
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
     * Update the properties of an asset.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param assetGUID unique identifier of the asset (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateAsset(String                   serverName,
                                    String                   urlMarker,
                                    String                   assetGUID,
                                    UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateAsset";

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
                AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof AssetProperties assetProperties)
                {
                    handler.updateAsset(userId,
                                        assetGUID,
                                        requestBody,
                                        assetProperties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(AssetProperties.class.getName(), methodName);
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
     * Delete an asset.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param assetGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteAsset(String                   serverName,
                                    String                   urlMarker,
                                    String                   assetGUID,
                                    DeleteElementRequestBody requestBody)
    {
        final String methodName = "deleteAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.deleteAsset(userId, assetGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of asset metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getAssetsByName(String            serverName,
                                                            String            urlMarker,
                                                            FilterRequestBody requestBody)
    {
        final String methodName = "getAssetsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getAssetsByName(userId,
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

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of asset metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param assetGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getAssetByGUID(String             serverName,
                                                          String             urlMarker,
                                                          String             assetGUID,
                                                          GetRequestBody requestBody)
    {
        final String methodName = "getAssetByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            response.setElement(handler.getAssetByGUID(userId, assetGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of asset metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findAssets(String            serverName,
                                                       String            urlMarker,
                                                       SearchStringRequestBody requestBody)
    {
        final String methodName = "findAssets";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findAssets(userId, requestBody.getSearchString(), requestBody));
            }
            else
            {
                response.setElements(handler.findAssets(userId, null, null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /* =====================================================================================================================
     * A catalog target links an element (typically an asset) to an integration connector for processing.
     */

    /**
     * Add a catalog target to an integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param integrationConnectorGUID unique identifier of the integration service.
     * @param metadataElementGUID unique identifier of the metadata element that is a catalog target.
     * @param requestBody properties for the relationship.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the catalog target definition.
     */
    public GUIDResponse addCatalogTarget(String                  serverName,
                                         String                  urlMarker,
                                         String                  integrationConnectorGUID,
                                         String                  metadataElementGUID,
                                         NewRelationshipRequestBody requestBody)
    {
        final String methodName = "addCatalogTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog                    auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof  CatalogTargetProperties catalogTargetProperties)
                {
                    response.setGUID(handler.addCatalogTarget(userId,
                                                              integrationConnectorGUID,
                                                              metadataElementGUID,
                                                              requestBody,
                                                              catalogTargetProperties));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CatalogTargetProperties.class.getName(), methodName);
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
     * Update a catalog target for an integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param relationshipGUID unique identifier of the relationship.
     * @param requestBody properties for the relationship.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the catalog target definition.
     */
    public VoidResponse updateCatalogTarget(String                        serverName,
                                            String                        urlMarker,
                                            String                        relationshipGUID,
                                            UpdateRelationshipRequestBody requestBody)
    {
        final String methodName = "updateCatalogTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog                    auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof CatalogTargetProperties catalogTargetProperties)
                {
                    handler.updateCatalogTarget(userId, relationshipGUID, requestBody, catalogTargetProperties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CatalogTargetProperties.class.getName(), methodName);
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
     * Retrieve a specific catalog target associated with an integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param relationshipGUID unique identifier of the relationship.
     * @param requestBody describes ho the results should be returned
     *
     * @return details of the governance service and the asset types it is registered for or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    public OpenMetadataRelationshipResponse getCatalogTarget(String serverName,
                                                             String urlMarker,
                                                             String relationshipGUID,
                                                             GetRequestBody requestBody)
    {
        final String methodName = "getCatalogTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRelationshipResponse response = new OpenMetadataRelationshipResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            response.setElement(handler.getCatalogTarget(userId, relationshipGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the details of the metadata elements identified as catalog targets with an integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param requestBody describes how results are to be returned
     *
     * @return list of unique identifiers or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    public OpenMetadataRootElementsResponse getCatalogTargets(String             serverName,
                                                              String             urlMarker,
                                                              String             integrationConnectorGUID,
                                                              ResultsRequestBody requestBody)
    {
        final String methodName = "getCatalogTargets";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            response.setElements(handler.getCatalogTargets(userId, integrationConnectorGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Unregister a catalog target from the integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param relationshipGUID unique identifier of the integration connector.
     * @param requestBody null request body.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    public VoidResponse removeCatalogTarget(String                        serverName,
                                            String                        urlMarker,
                                            String                        relationshipGUID,
                                            DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "removeCatalogTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog                    auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.removeCatalogTarget(userId, relationshipGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Unregister a catalog target from the integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param elementGUID unique identifier of the target element.
     * @param requestBody null request body.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    public VoidResponse removeCatalogTarget(String                        serverName,
                                            String                        urlMarker,
                                            String                        integrationConnectorGUID,
                                            String                        elementGUID,
                                            DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "removeCatalogTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog                    auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.removeCatalogTarget(userId, integrationConnectorGUID, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



}
