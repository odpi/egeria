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
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetContentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.DeployedOnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ActionTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.SupportedSoftwareCapabilityProperties;
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
     * @return boolean or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public BooleanResponse updateAsset(String                   serverName,
                                       String                   urlMarker,
                                       String                   assetGUID,
                                       UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        BooleanResponse response = new BooleanResponse();
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
                    response.setFlag(handler.updateAsset(userId,
                                                         assetGUID,
                                                         requestBody,
                                                         assetProperties));
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
     * Retrieve the processes that match the category name and status.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param requestBody     status of the to do (null means current active)
     *
     * @return list of to do beans or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public OpenMetadataRootElementsResponse getProcessesByCategory(String                          serverName,
                                                                   String                          urlMarker,
                                                                   ActivityStatusFilterRequestBody requestBody)
    {
        final String methodName = "getProcessesByCategory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getProcessesByCategory(userId,
                                                                    requestBody.getFilter(),
                                                                    requestBody.getActivityStatus(),
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



    /**
     * Retrieve the processes that match the search string.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param requestBody     status of the to do (null means current active)
     *
     * @return list of to do beans or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public OpenMetadataRootElementsResponse findProcesses(String                     serverName,
                                                          String                     urlMarker,
                                                          ActivityStatusSearchString requestBody)
    {
        final String methodName = "findProcesses";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findProcesses(userId,
                                                           requestBody.getSearchString(),
                                                           requestBody.getActivityStatus(),
                                                           requestBody));
            }
            else
            {
                response.setElements(handler.findProcesses(userId,
                                                           null,
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


    /* =====================================================================================================================
     * Working with infrastructure
     */


    /**
     * Create a relationship that represents the deployment of an IT infrastructure asset to a specific deployment destination (another asset).
     *
     * @param serverName name of the server to route the request to
     * @param assetGUID       unique identifier of the asset
     * @param destinationGUID           unique identifier of the destination asset
     * @param urlMarker  view service URL marker
     * @param requestBody optional effective time
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse deployITAsset(String                     serverName,
                                      String                     urlMarker,
                                      String                     assetGUID,
                                      String                     destinationGUID,
                                      NewRelationshipRequestBody requestBody)
    {
        final String methodName = "deployITAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                handler.deployITAsset(userId, assetGUID, destinationGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof DeployedOnProperties deployedOnProperties)
            {
                handler.deployITAsset(userId, assetGUID, destinationGUID, requestBody, deployedOnProperties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.deployITAsset(userId, assetGUID, destinationGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(DeployedOnProperties.class.getName(), methodName);
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
     * Remove a DeployedOn relationship.
     *
     * @param serverName name of the server to route the request to
     * @param assetGUID       unique identifier of the asset
     * @param destinationGUID           unique identifier of the destination asset
     * @param urlMarker  view service URL marker
     * @param requestBody optional effective time
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse unDeployITAsset(String                        serverName,
                                        String                        urlMarker,
                                        String                        assetGUID,
                                        String                        destinationGUID,
                                        DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "unDeployITAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.unDeployITAsset(userId, assetGUID, destinationGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Create a relationship that links a software capability to an infrastructure asset like a software server.
     *
     * @param serverName name of the server to route the request to
     * @param assetGUID          unique identifier of the data set
     * @param capabilityGUID          unique identifier of the data asset supplying the data
     * @param urlMarker  view service URL marker
     * @param requestBody optional effective time
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse linkSoftwareCapability(String                     serverName,
                                               String                     urlMarker,
                                               String                     assetGUID,
                                               String                     capabilityGUID,
                                               NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkSoftwareCapability";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                handler.linkSoftwareCapability(userId, assetGUID, capabilityGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof SupportedSoftwareCapabilityProperties supportedSoftwareCapabilityProperties)
            {
                handler.linkSoftwareCapability(userId, assetGUID, capabilityGUID, requestBody, supportedSoftwareCapabilityProperties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkSoftwareCapability(userId, assetGUID, capabilityGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(SupportedSoftwareCapabilityProperties.class.getName(), methodName);
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
     *  Remove a relationship that links a software capability to an infrastructure asset like a software server.
     *
     * @param serverName name of the server to route the request to
     * @param assetGUID          unique identifier of the data set
     * @param capabilityGUID  unique identifier of the data asset supplying the data
     * @param urlMarker  view service URL marker
     * @param requestBody optional effective time
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse detachSoftwareCapability(String                        serverName,
                                                 String                        urlMarker,
                                                 String                        assetGUID,
                                                 String                        capabilityGUID,
                                                 DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachSoftwareCapability";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.detachSoftwareCapability(userId, assetGUID, capabilityGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /* =====================================================================================================================
     * Working with data assets
     */


    /**
     * Attach a data set to another asset (typically a data store) that is supplying the data.
     *
     * @param serverName name of the server to route the request to
     * @param dataSetGUID          unique identifier of the data set
     * @param dataContentAssetGUID          unique identifier of the data asset supplying the data
     * @param urlMarker  view service URL marker
     * @param requestBody optional effective time
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse linkDataSetContent(String                     serverName,
                                           String                     urlMarker,
                                           String                     dataSetGUID,
                                           String                     dataContentAssetGUID,
                                           NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkDataSetContent";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                handler.linkDataSetContent(userId, dataSetGUID, dataContentAssetGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof DataSetContentProperties dataSetContentProperties)
            {
                handler.linkDataSetContent(userId, dataSetGUID, dataContentAssetGUID, requestBody, dataSetContentProperties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkDataSetContent(userId, dataSetGUID, dataContentAssetGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(DataSetContentProperties.class.getName(), methodName);
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
     * Detach a data set from another asset that was supplying the data and is no more.
     *
     * @param serverName name of the server to route the request to
     * @param dataSetGUID          unique identifier of the data set
     * @param dataContentAssetGUID  unique identifier of the data asset supplying the data
     * @param urlMarker  view service URL marker
     * @param requestBody optional effective time
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse detachDataSetContent(String                        serverName,
                                             String                        urlMarker,
                                             String                        dataSetGUID,
                                             String                        dataContentAssetGUID,
                                             DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachDataSetContent";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.detachDataSetContent(userId, dataSetGUID, dataContentAssetGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }

    /* =====================================================================================================================
     * Actions are special types of processes
     */

    /**
     * Create a new action and link it to the supplied role and targets (if applicable).
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param requestBody properties of the to do action
     *
     * @return unique identifier of the to do or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public GUIDResponse createAction(String            serverName,
                                     String            urlMarker,
                                     ActionRequestBody requestBody)
    {
        final String methodName = "createAction";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setGUID(handler.createAction(userId,
                                                      requestBody.getOriginatorGUID(),
                                                      requestBody.getActionSponsorGUID(),
                                                      requestBody.getAssignToActorGUID(),
                                                      requestBody,
                                                      requestBody.getInitialClassifications(),
                                                      requestBody.getNewActionTargets(),
                                                      requestBody.getProperties()));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, SearchStringRequestBody.class.getName());
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
     * Add an element to an action's workload.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param actionGUID unique identifier of the integration service.
     * @param metadataElementGUID unique identifier of the metadata element that is a catalog target.
     * @param requestBody properties for the relationship.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the catalog target definition.
     */
    public GUIDResponse addActionTarget(String                     serverName,
                                        String                     urlMarker,
                                        String                     actionGUID,
                                        String                     metadataElementGUID,
                                        NewRelationshipRequestBody requestBody)
    {
        final String methodName = "addActionTarget";

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

                if (requestBody.getProperties() instanceof  ActionTargetProperties actionTargetProperties)
                {
                    response.setGUID(handler.addActionTarget(userId,
                                                              actionGUID,
                                                              metadataElementGUID,
                                                              requestBody,
                                                              actionTargetProperties));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ActionTargetProperties.class.getName(), methodName);
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
     * Update the properties associated with an Action Target.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param actionTargetGUID               unique identifier of the action target relationship
     * @param requestBody properties to change
     *
     * @return void or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public VoidResponse updateActionTargetProperties(String                        serverName,
                                                     String                        urlMarker,
                                                     String                        actionTargetGUID,
                                                     UpdateRelationshipRequestBody requestBody)
    {
        final String methodName = "updateActionTargetProperties";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ActionTargetProperties actionTargetProperties)
                {
                    handler.updateActionTargetProperties(userId, actionTargetGUID, requestBody, actionTargetProperties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ActionTargetProperties.class.getName(), methodName);
                }
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
     * Retrieve a specific action target associated with an action.
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
    public OpenMetadataRelationshipResponse getActionTarget(String         serverName,
                                                            String         urlMarker,
                                                            String         relationshipGUID,
                                                            GetRequestBody requestBody)
    {
        final String methodName = "getActionTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRelationshipResponse response = new OpenMetadataRelationshipResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            response.setElement(handler.getActionTarget(userId, relationshipGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the details of the metadata elements identified as action targets with an action.
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
    public OpenMetadataRootElementsResponse getActionTargets(String             serverName,
                                                             String             urlMarker,
                                                             String             integrationConnectorGUID,
                                                             ResultsRequestBody requestBody)
    {
        final String methodName = "getActionTargets";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            response.setElements(handler.getActionTargets(userId, integrationConnectorGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Retrieve the actions that are chained off of an action target element.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element to start with
     * @param requestBody     status of the to do (null means current active)
     *
     * @return list of to do beans or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public OpenMetadataRootElementsResponse getActionsForActionTarget(String                    serverName,
                                                                      String                    urlMarker,
                                                                      String                    elementGUID,
                                                                      ActivityStatusRequestBody requestBody)
    {
        final String methodName = "getActionsForActionTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getActionsForActionTarget(userId,
                                                                       elementGUID,
                                                                       requestBody.getActivityStatus(),
                                                                       requestBody));
            }
            else
            {
                response.setElements(handler.getActionsForActionTarget(userId,
                                                                       elementGUID,
                                                                       null,
                                                                       requestBody));
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
     * Retrieve the actions that are chained off of a sponsoring element.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element to start with
     * @param requestBody     status of the to do (null means current active)
     *
     * @return list of to do beans or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public OpenMetadataRootElementsResponse getActionsForSponsor(String                    serverName,
                                                                 String                    urlMarker,
                                                                 String                    elementGUID,
                                                                 ActivityStatusRequestBody requestBody)
    {
        final String methodName = "getActionsForSponsor";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getActionsForSponsor(userId,
                                                                  elementGUID,
                                                                  requestBody.getActivityStatus(),
                                                                  requestBody));
            }
            else
            {
                response.setElements(handler.getActionsForSponsor(userId,
                                                                  elementGUID,
                                                                  null,
                                                                  requestBody));
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
     * Retrieve the "Actions" for a particular actor.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param actorGUID unique identifier of the role
     * @param requestBody     status of the to do (null means current active)
     *
     * @return list of to do beans or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public OpenMetadataRootElementsResponse getAssignedActions(String                    serverName,
                                                               String                    urlMarker,
                                                               String                    actorGUID,
                                                               ActivityStatusRequestBody requestBody)
    {
        final String methodName = "getAssignedActions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getAssignedActions(userId,
                                                                actorGUID,
                                                                requestBody.getActivityStatus(),
                                                                requestBody));
            }
            else
            {
                response.setElements(handler.getAssignedActions(userId,
                                                                actorGUID,
                                                                null,
                                                                requestBody));
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
