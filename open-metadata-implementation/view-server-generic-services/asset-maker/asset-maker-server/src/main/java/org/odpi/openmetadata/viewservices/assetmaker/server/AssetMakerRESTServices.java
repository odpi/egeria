/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetmaker.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.AssetHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SoftwareCapabilityHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.AssignmentScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetContentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.CapabilityAssetUseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.DeployedOnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ActionTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.SupportedGovernanceServiceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.ReportDependencyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.ReportOriginatorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.ReportSubjectProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.SoftwareCapabilityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.SupportedSoftwareCapabilityProperties;
import org.odpi.openmetadata.frameworkservices.omf.rest.OpenMetadataRelationshipResponse;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.apis.APIEndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.ImpactedResourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.FolderHierarchyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.LinkedFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.NestedFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.ProcessHierarchyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.SampleDataProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.ArchiveContentsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.LinkedMediaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.ProcessPortProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.topics.AssociatedLogProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.AuditLogProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.LineageLogProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.MeteringLogProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.SecurityLogProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.ExceptionBacklogProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.LogAnalysisProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.apis.ListenerInterfaceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.apis.PublisherInterfaceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.apis.RequestResponseInterfaceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataAssetEncodingProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.PortDelegationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.RegisteredIntegrationConnectorProperties;


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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createAsset(String                serverName,
                                    String                urlMarker,
                                    NewElementRequestBody requestBody)
    {
        final String methodName = "createAsset";

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public GUIDResponse createAssetFromTemplate(String              serverName,
                                                String              urlMarker,
                                                TemplateRequestBody requestBody)
    {
        final String methodName = "createAssetFromTemplate";

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
                AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

                response.setGUID(handler.createAssetFromTemplate(userId,
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
     * Update the properties of an asset.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param assetGUID unique identifier of the asset (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return boolean or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public BooleanResponse updateAsset(String                   serverName,
                                       String                   urlMarker,
                                       String                   assetGUID,
                                       UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateAsset";

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteAsset(String                   serverName,
                                    String                   urlMarker,
                                    String                   assetGUID,
                                    DeleteElementRequestBody requestBody)
    {
        final String methodName = "deleteAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getAssetsByName(String            serverName,
                                                            String            urlMarker,
                                                            FilterRequestBody requestBody)
    {
        final String methodName = "getAssetsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                                                                    requestBody.getActivityStatusList(),
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getAssetByGUID(String             serverName,
                                                          String             urlMarker,
                                                          String             assetGUID,
                                                          GetRequestBody requestBody)
    {
        final String methodName = "getAssetByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findAssets(String                  serverName,
                                                       String                  urlMarker,
                                                       SearchStringRequestBody requestBody)
    {
        final String methodName = "findAssets";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                                                           requestBody.getActivityStatusList(),
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

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /* =====================================================================================================================
     * Working with infrastructure
     */


    /**
     * Retrieve the infrastructure assets that match the search string and optional status.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return a list of assets
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse findInfrastructure(String                       serverName,
                                                               String                       urlMarker,
                                                               DeploymentStatusSearchString requestBody)
    {
        final String methodName = "findInfrastructure";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                response.setElements(handler.findInfrastructure(userId, requestBody.getSearchString(), requestBody.getDeploymentStatusList(), requestBody));
            }
            else
            {
                response.setElements(handler.findInfrastructure(userId, null, null, null));
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
     * Retrieve the infrastructure assets that match the category name and status.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return a list of assets
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getInfrastructureByCategory(String                            serverName,
                                                                        String                            urlMarker,
                                                                        DeploymentStatusFilterRequestBody requestBody)
    {
        final String methodName = "getInfrastructureByCategory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                response.setElements(handler.getInfrastructureByCategory(userId, requestBody.getFilter(), requestBody.getDeploymentStatusList(), requestBody));
            }
            else
            {
                response.setElements(handler.getInfrastructureByCategory(userId, null, null, null));
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
     * Reports
     */


    /**
     * Create a relationship that identifies the originator of a report.
     *
     * @param serverName name of the server to route the request to
     * @param originatorGUID       unique identifier of the originator
     * @param reportGUID           unique identifier of the report
     * @param urlMarker  view service URL marker
     * @param requestBody optional effective time
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkReportOriginator(String                     serverName,
                                             String                     urlMarker,
                                             String                     originatorGUID,
                                             String                     reportGUID,
                                             NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkReportOriginator";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                handler.linkReportOriginator(userId, originatorGUID, reportGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof ReportOriginatorProperties properties)
            {
                handler.linkReportOriginator(userId, originatorGUID, reportGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkReportOriginator(userId, originatorGUID, reportGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(ReportOriginatorProperties.class.getName(), methodName);
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
     * Remove a ReportOriginator relationship.
     *
     * @param serverName name of the server to route the request to
     * @param originatorGUID       unique identifier of the originator
     * @param reportGUID           unique identifier of the report
     * @param urlMarker  view service URL marker
     * @param requestBody optional effective time
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse unlinkReportOriginator(String                        serverName,
                                               String                        urlMarker,
                                               String                        originatorGUID,
                                               String                        reportGUID,
                                               DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "unlinkReportOriginator";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.unlinkReportOriginator(userId, originatorGUID, reportGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Create a relationship that identifies the prior publishing of a report.
     *
     * @param serverName name of the server to route the request to
     * @param priorReportGUID       unique identifier of the earlier report
     * @param reportGUID           unique identifier of the new report
     * @param urlMarker  view service URL marker
     * @param requestBody optional effective time
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkReportDependency(String                     serverName,
                                             String                     urlMarker,
                                             String                     priorReportGUID,
                                             String                     reportGUID,
                                             NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkReportDependency";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                handler.linkReportDependency(userId, priorReportGUID, reportGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof ReportDependencyProperties properties)
            {
                handler.linkReportDependency(userId, priorReportGUID, reportGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkReportDependency(userId, priorReportGUID, reportGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(ReportDependencyProperties.class.getName(), methodName);
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
     * Remove a ReportDependency relationship.
     *
     * @param serverName name of the server to route the request to
     * @param priorReportGUID       unique identifier of the prior report
     * @param reportGUID           unique identifier of the new report
     * @param urlMarker  view service URL marker
     * @param requestBody optional effective time
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse unlinkReportDependency(String                        serverName,
                                               String                        urlMarker,
                                               String                        priorReportGUID,
                                               String                        reportGUID,
                                               DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "unlinkReportDependency";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.unlinkReportDependency(userId, priorReportGUID, reportGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }



    /**
     * Create a relationship that identifies the subject of a report.
     *
     * @param serverName name of the server to route the request to
     * @param subjectGUID       unique identifier of the subject
     * @param reportGUID           unique identifier of the report
     * @param urlMarker  view service URL marker
     * @param requestBody optional effective time
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkReportSubject(String                     serverName,
                                          String                     urlMarker,
                                          String                     subjectGUID,
                                          String                     reportGUID,
                                          NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkReportSubject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                handler.linkReportSubject(userId, subjectGUID, reportGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof ReportSubjectProperties properties)
            {
                handler.linkReportSubject(userId, subjectGUID, reportGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkReportSubject(userId, subjectGUID, reportGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(ReportSubjectProperties.class.getName(), methodName);
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
     * Remove a ReportSubject relationship.
     *
     * @param serverName name of the server to route the request to
     * @param subjectGUID       unique identifier of the subject
     * @param reportGUID           unique identifier of the report
     * @param urlMarker  view service URL marker
     * @param requestBody optional effective time
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse unlinkReportSubject(String                        serverName,
                                            String                        urlMarker,
                                            String                        subjectGUID,
                                            String                        reportGUID,
                                            DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "unlinkReportSubject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.unlinkReportSubject(userId, subjectGUID, reportGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /* =====================================================================================================================
     * Working with infrastructure
     */


    /*
     * IT Assets and Software capabilities
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
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse deployITAsset(String                     serverName,
                                      String                     urlMarker,
                                      String                     assetGUID,
                                      String                     destinationGUID,
                                      NewRelationshipRequestBody requestBody)
    {
        final String methodName = "deployITAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);

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
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse unDeployITAsset(String                        serverName,
                                        String                        urlMarker,
                                        String                        assetGUID,
                                        String                        destinationGUID,
                                        DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "unDeployITAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);

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
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkSoftwareCapability(String                     serverName,
                                               String                     urlMarker,
                                               String                     assetGUID,
                                               String                     capabilityGUID,
                                               NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkSoftwareCapability";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);

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
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachSoftwareCapability(String                        serverName,
                                                 String                        urlMarker,
                                                 String                        assetGUID,
                                                 String                        capabilityGUID,
                                                 DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachSoftwareCapability";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /* =====================================================================================================================
     * Working with data assets
     */



    /**
     * Retrieve the data assets that match the search string and optional status.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return a list of assets
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse findDataAssets(String                    serverName,
                                                           String                    urlMarker,
                                                           ContentStatusSearchString requestBody)
    {
        final String methodName = "findDataAssets";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                response.setElements(handler.findDataAssets(userId, requestBody.getSearchString(), requestBody.getContentStatusList(), requestBody));
            }
            else
            {
                response.setElements(handler.findDataAssets(userId, null, null, null));
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
     * Retrieve the data assets that match the category name and status.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return a list of assets
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getDataAssetsByCategory(String                         serverName,
                                                                    String                         urlMarker,
                                                                    ContentStatusFilterRequestBody requestBody)
    {
        final String methodName = "getDataAssetsByCategory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                response.setElements(handler.getDataAssetsByCategory(userId, requestBody.getFilter(), requestBody.getContentStatusList(), requestBody));
            }
            else
            {
                response.setElements(handler.getDataAssetsByCategory(userId, null, null, null));
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
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkDataSetContent(String                     serverName,
                                           String                     urlMarker,
                                           String                     dataSetGUID,
                                           String                     dataContentAssetGUID,
                                           NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkDataSetContent";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);

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
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachDataSetContent(String                        serverName,
                                             String                        urlMarker,
                                             String                        dataSetGUID,
                                             String                        dataContentAssetGUID,
                                             DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachDataSetContent";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);

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

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                                                      requestBody.getInitialClassifications(),
                                                      requestBody.getProperties(),
                                                      requestBody.getOriginatorGUID(),
                                                      requestBody.getActionSponsorGUID(),
                                                      requestBody.getAssignToActorGUID(),
                                                      requestBody,
                                                      requestBody.getNewActionTargets()));
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

        restCallLogger.logRESTCallReturn(token, response);

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
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException problem storing the catalog target definition.
     */
    public GUIDResponse addActionTarget(String                     serverName,
                                        String                     urlMarker,
                                        String                     actionGUID,
                                        String                     metadataElementGUID,
                                        NewRelationshipRequestBody requestBody)
    {
        final String methodName = "addActionTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);

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
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    public OpenMetadataRelationshipResponse getActionTarget(String         serverName,
                                                            String         urlMarker,
                                                            String         relationshipGUID,
                                                            GetRequestBody requestBody)
    {
        final String methodName = "getActionTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    public OpenMetadataRootElementsResponse getActionTargets(String                    serverName,
                                                             String                    urlMarker,
                                                             String                    integrationConnectorGUID,
                                                             ActivityStatusRequestBody requestBody)
    {
        final String methodName = "getActionTargets";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                response.setElements(handler.getActionTargets(userId, integrationConnectorGUID, requestBody.getActivityStatusList(), requestBody));
            }
            else
            {
                response.setElements(handler.getActionTargets(userId, integrationConnectorGUID, null, null));
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

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                                                                       requestBody.getActivityStatusList(),
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

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Assign an action to a new actor.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param actionGUID unique identifier of the to do
     * @param actorGUID  actor to assign the action to
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public VoidResponse assignAction(String                     serverName,
                                     String                     urlMarker,
                                     String                     actionGUID,
                                     String                     actorGUID,
                                     NewRelationshipRequestBody requestBody)
    {
        final String methodName = "assignAction";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                handler.assignAction(userId, actionGUID, actorGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof AssignmentScopeProperties assignmentScopeProperties)
            {
                handler.assignAction(userId, actionGUID, actorGUID, requestBody, assignmentScopeProperties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.assignAction(userId, actionGUID, actorGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(AssignmentScopeProperties.class.getName(), methodName);
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
     * Assign an action to a new actor.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param actionGUID unique identifier of the to do
     * @param actorGUID  actor to assign the action to
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public VoidResponse reassignAction(String                     serverName,
                                       String                     urlMarker,
                                       String                     actionGUID,
                                       String                     actorGUID,
                                       NewRelationshipRequestBody requestBody)
    {
        final String methodName = "reassignAction";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                handler.reassignAction(userId, actionGUID, actorGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof AssignmentScopeProperties assignmentScopeProperties)
            {
                handler.reassignAction(userId, actionGUID, actorGUID, requestBody, assignmentScopeProperties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.reassignAction(userId, actionGUID, actorGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(AssignmentScopeProperties.class.getName(), methodName);
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
     * Remove an action from an actor.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param actionGUID unique identifier of the to do
     * @param actorGUID  actor to assign the action to
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public VoidResponse unassignAction(String                        serverName,
                                       String                        urlMarker,
                                       String                        actionGUID,
                                       String                        actorGUID,
                                       DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "unassignAction";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.unassignAction(userId, actionGUID, actorGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Retrieve the actions that are chained off a sponsoring element.
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

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                                                                  requestBody.getActivityStatusList(),
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

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }



    /**
     * Retrieve the actions that are chained off a requester's element.
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
    public OpenMetadataRootElementsResponse getActionsFromRequester(String                    serverName,
                                                                    String                    urlMarker,
                                                                    String                    elementGUID,
                                                                    ActivityStatusRequestBody requestBody)
    {
        final String methodName = "getActionsFromRequester";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                response.setElements(handler.getActionsFromRequester(userId,
                                                                     elementGUID,
                                                                     requestBody.getActivityStatusList(),
                                                                     requestBody));
            }
            else
            {
                response.setElements(handler.getActionsFromRequester(userId,
                                                                     elementGUID,
                                                                     null,
                                                                     requestBody));
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

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                                                                requestBody.getActivityStatusList(),
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

        restCallLogger.logRESTCallReturn(token, response);

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
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException problem storing the catalog target definition.
     */
    public GUIDResponse addCatalogTarget(String                     serverName,
                                         String                     urlMarker,
                                         String                     integrationConnectorGUID,
                                         String                     metadataElementGUID,
                                         NewRelationshipRequestBody requestBody)
    {
        final String methodName = "addCatalogTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException problem storing the catalog target definition.
     */
    public VoidResponse updateCatalogTarget(String                        serverName,
                                            String                        urlMarker,
                                            String                        relationshipGUID,
                                            UpdateRelationshipRequestBody requestBody)
    {
        final String methodName = "updateCatalogTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    public OpenMetadataRelationshipResponse getCatalogTarget(String         serverName,
                                                             String         urlMarker,
                                                             String         relationshipGUID,
                                                             GetRequestBody requestBody)
    {
        final String methodName = "getCatalogTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    public OpenMetadataRootElementsResponse getCatalogTargets(String             serverName,
                                                              String             urlMarker,
                                                              String             integrationConnectorGUID,
                                                              ResultsRequestBody requestBody)
    {
        final String methodName = "getCatalogTargets";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    public VoidResponse removeCatalogTarget(String                        serverName,
                                            String                        urlMarker,
                                            String                        relationshipGUID,
                                            DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "removeCatalogTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    public VoidResponse removeCatalogTarget(String                        serverName,
                                            String                        urlMarker,
                                            String                        integrationConnectorGUID,
                                            String                        elementGUID,
                                            DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "removeCatalogTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }




    /* =====================================================================================================================
     * Working with software capabilities
     */


    /**
     * Create a software capability.
     *
     * @param serverName  name of called server.
     * @param urlMarker   view service URL marker
     * @param requestBody properties for the software capability.
     * @return unique identifier of the newly created element
     */
    public GUIDResponse createSoftwareCapability(String serverName,
                                                 String urlMarker,
                                                 NewElementRequestBody requestBody)
    {
        final String methodName = "createSoftwareCapability";

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
                SoftwareCapabilityHandler handler = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof SoftwareCapabilityProperties softwareCapabilityProperties)
                {
                    response.setGUID(handler.createSoftwareCapability(userId,
                                                                      requestBody,
                                                                      requestBody.getInitialClassifications(),
                                                                      softwareCapabilityProperties,
                                                                      requestBody.getParentRelationshipProperties()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SoftwareCapabilityProperties.class.getName(), methodName);
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
     * Create a new metadata element to represent a software capability using an existing metadata element as a template.
     *
     * @param serverName  name of called server
     * @param urlMarker   view service URL marker
     * @param requestBody properties that override the template
     * @return unique identifier of the new metadata element
     */
    public GUIDResponse createSoftwareCapabilityFromTemplate(String serverName,
                                                             String urlMarker,
                                                             TemplateRequestBody requestBody)
    {
        final String methodName = "createSoftwareCapabilityFromTemplate";

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
                SoftwareCapabilityHandler handler = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, urlMarker, methodName);

                response.setGUID(handler.createSoftwareCapabilityFromTemplate(userId,
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
     * Update the properties of a software capability.
     *
     * @param serverName             name of called server
     * @param urlMarker              view service URL marker
     * @param softwareCapabilityGUID unique identifier of the software capability
     * @param requestBody            properties for the updated element
     * @return boolean response
     */
    public BooleanResponse updateSoftwareCapability(String serverName,
                                                    String urlMarker,
                                                    String softwareCapabilityGUID,
                                                    UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateSoftwareCapability";

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
                SoftwareCapabilityHandler handler = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof SoftwareCapabilityProperties softwareCapabilityProperties)
                {
                    response.setFlag(handler.updateSoftwareCapability(userId,
                                                                      softwareCapabilityGUID,
                                                                      requestBody,
                                                                      softwareCapabilityProperties));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SoftwareCapabilityProperties.class.getName(), methodName);
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
     * Retrieve a specific software capability.
     *
     * @param serverName             name of called server
     * @param urlMarker              view service URL marker
     * @param softwareCapabilityGUID unique identifier of the required element
     * @param requestBody            options to control the query
     * @return retrieved software capability
     */
    public OpenMetadataRootElementResponse getSoftwareCapabilityByGUID(String serverName,
                                                                       String urlMarker,
                                                                       String softwareCapabilityGUID,
                                                                       GetRequestBody requestBody)
    {
        final String methodName = "getSoftwareCapabilityByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler handler = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, urlMarker, methodName);

            response.setElement(handler.getSoftwareCapabilityByGUID(userId, softwareCapabilityGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve the list of software capability metadata elements that contain the search string.
     *
     * @param serverName  name of called server
     * @param urlMarker   view service URL marker
     * @param requestBody string to find in the properties
     * @return list of matching metadata elements
     */
    public OpenMetadataRootElementsResponse findSoftwareCapabilities(String serverName,
                                                                     String urlMarker,
                                                                     SearchStringRequestBody requestBody)
    {
        final String methodName = "findSoftwareCapabilities";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler handler = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findSoftwareCapabilities(userId, requestBody.getSearchString(), requestBody));
            }
            else
            {
                response.setElements(handler.findSoftwareCapabilities(userId, null, null));
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
     * Retrieve the list of software capabilities with a particular name.
     *
     * @param serverName  name of called server
     * @param urlMarker   view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements
     */
    public OpenMetadataRootElementsResponse getSoftwareCapabilitiesByName(String            serverName,
                                                                          String            urlMarker,
                                                                          FilterRequestBody requestBody)
    {
        final String methodName = "getSoftwareCapabilitiesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler handler = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getSoftwareCapabilitiesByName(userId,
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
     * Retrieve the list of software capabilities with a particular deployed implementation type.
     *
     * @param serverName  name of called server
     * @param urlMarker   view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements
     */
    public OpenMetadataRootElementsResponse getSoftwareCapabilitiesByDeployedImplementationType(String            serverName,
                                                                                                String            urlMarker,
                                                                                                FilterRequestBody requestBody)
    {
        final String methodName = "getSoftwareCapabilitiesByDeployedImplementationType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler handler = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getSoftwareCapabilitiesByDeployedImplementationType(userId,
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
     * Retrieve the list of software capabilities attached to a specific infrastructure element.
     *
     * @param serverName         name of called server
     * @param urlMarker          view service URL marker
     * @param infrastructureGUID unique identifier of the infrastructure element
     * @param requestBody        options to control the query
     *
     * @return list of matching metadata elements
     */
    public OpenMetadataRootElementsResponse getSoftwareCapabilitiesForInfrastructure(String             serverName,
                                                                                     String             urlMarker,
                                                                                     String             infrastructureGUID,
                                                                                     ResultsRequestBody requestBody)
    {
        final String methodName = "getSoftwareCapabilitiesForInfrastructure";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler handler = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, urlMarker, methodName);

            response.setElements(handler.getSoftwareCapabilitiesForInfrastructure(userId, infrastructureGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Create a relationship that represents the use of an asset by a software capability.
     *
     * @param serverName             name of the server to route the request to
     * @param urlMarker              view service URL marker
     * @param softwareCapabilityGUID unique identifier of the software capability
     * @param assetGUID              unique identifier of the asset
     * @param requestBody            optional effective time and relationship properties
     *
     * @return void response
     */
    public VoidResponse addCapabilityAssetUse(String                     serverName,
                                              String                     urlMarker,
                                              String                     softwareCapabilityGUID,
                                              String                     assetGUID,
                                              NewRelationshipRequestBody requestBody)
    {
        final String methodName = "addCapabilityAssetUse";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler handler = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                handler.addCapabilityAssetUse(userId, softwareCapabilityGUID, assetGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof CapabilityAssetUseProperties capabilityAssetUseProperties)
            {
                handler.addCapabilityAssetUse(userId, softwareCapabilityGUID, assetGUID, requestBody, capabilityAssetUseProperties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.addCapabilityAssetUse(userId, softwareCapabilityGUID, assetGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(CapabilityAssetUseProperties.class.getName(), methodName);
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
     * Remove the relationship that represents the use of an asset by a software capability.
     *
     * @param serverName             name of the server to route the request to
     * @param urlMarker              view service URL marker
     * @param softwareCapabilityGUID unique identifier of the software capability
     * @param assetGUID              unique identifier of the asset
     * @param requestBody            optional effective time
     *
     * @return void response
     */
    public VoidResponse removeCapabilityAssetUse(String                        serverName,
                                                 String                        urlMarker,
                                                 String                        softwareCapabilityGUID,
                                                 String                        assetGUID,
                                                 DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "removeCapabilityAssetUse";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler handler = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, urlMarker, methodName);

            handler.removeCapabilityAssetUse(userId, softwareCapabilityGUID, assetGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve the software capabilities using a particular asset.
     *
     * @param serverName  name of called server
     * @param urlMarker   view service URL marker
     * @param assetGUID   unique identifier of the asset
     * @param requestBody options to control the query
     *
     * @return list of matching metadata elements
     */
    public OpenMetadataRootElementsResponse getCapabilityUse(String             serverName,
                                                             String             urlMarker,
                                                             String             assetGUID,
                                                             ResultsRequestBody requestBody)
    {
        final String methodName = "getCapabilityUse";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler handler = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, urlMarker, methodName);

            response.setElements(handler.getCapabilityUse(userId, assetGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Create a relationship that registers a governance service with a governance engine.  This is a multi-link
     * relationship so the unique identifier of the new relationship is returned.
     *
     * @param serverName            name of the server to route the request to
     * @param urlMarker             view service URL marker
     * @param governanceEngineGUID  unique identifier of the governance engine
     * @param governanceServiceGUID unique identifier of the governance service
     * @param requestBody           properties for the relationship
     *
     * @return unique identifier of the new relationship or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException problem storing the relationship.
     */
    public GUIDResponse linkSupportedGovernanceService(String                     serverName,
                                                       String                     urlMarker,
                                                       String                     governanceEngineGUID,
                                                       String                     governanceServiceGUID,
                                                       NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkSupportedGovernanceService";

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
                SoftwareCapabilityHandler handler = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof SupportedGovernanceServiceProperties supportedGovernanceServiceProperties)
                {
                    response.setGUID(handler.linkSupportedGovernanceService(userId,
                                                                            governanceEngineGUID,
                                                                            governanceServiceGUID,
                                                                            requestBody,
                                                                            supportedGovernanceServiceProperties));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setGUID(handler.linkSupportedGovernanceService(userId,
                                                                            governanceEngineGUID,
                                                                            governanceServiceGUID,
                                                                            requestBody,
                                                                            null));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SupportedGovernanceServiceProperties.class.getName(), methodName);
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
     * Update the properties of a SupportedGovernanceService relationship.
     *
     * @param serverName                     name of the server to route the request to
     * @param urlMarker                      view service URL marker
     * @param supportedGovernanceServiceGUID unique identifier of the relationship
     * @param requestBody                    new properties for the relationship
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException problem storing the relationship.
     */
    public VoidResponse updateSupportedGovernanceService(String                        serverName,
                                                         String                        urlMarker,
                                                         String                        supportedGovernanceServiceGUID,
                                                         UpdateRelationshipRequestBody requestBody)
    {
        final String methodName = "updateSupportedGovernanceService";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SoftwareCapabilityHandler handler = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof SupportedGovernanceServiceProperties supportedGovernanceServiceProperties)
                {
                    handler.updateSupportedGovernanceService(userId,
                                                             supportedGovernanceServiceGUID,
                                                             requestBody,
                                                             supportedGovernanceServiceProperties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SupportedGovernanceServiceProperties.class.getName(), methodName);
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
     * Remove a SupportedGovernanceService relationship.
     *
     * @param serverName                     name of the server to route the request to
     * @param urlMarker                      view service URL marker
     * @param supportedGovernanceServiceGUID unique identifier of the relationship
     * @param requestBody                    external source information
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException problem removing the relationship.
     */
    public VoidResponse detachSupportedGovernanceService(String                        serverName,
                                                         String                        urlMarker,
                                                         String                        supportedGovernanceServiceGUID,
                                                         DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachSupportedGovernanceService";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler handler = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, urlMarker, methodName);

            handler.detachSupportedGovernanceService(userId, supportedGovernanceServiceGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve the governance engines connected to a particular governance service.
     *
     * @param serverName            name of called server
     * @param urlMarker             view service URL marker
     * @param governanceServiceGUID unique identifier of the governance service
     * @param requestBody           options to control the query
     *
     * @return list of matching metadata elements
     */
    public OpenMetadataRootElementsResponse getGovernanceEngines(String             serverName,
                                                                 String             urlMarker,
                                                                 String             governanceServiceGUID,
                                                                 ResultsRequestBody requestBody)
    {
        final String methodName = "getGovernanceEngines";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler handler = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, urlMarker, methodName);

            response.setElements(handler.getGovernanceEngines(userId, governanceServiceGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve the integration groups connected to a particular integration connector.
     *
     * @param serverName               name of called server
     * @param urlMarker                view service URL marker
     * @param integrationConnectorGUID unique identifier of the integration connector
     * @param requestBody              options to control the query
     *
     * @return list of matching metadata elements
     */
    public OpenMetadataRootElementsResponse getIntegrationGroups(String             serverName,
                                                                 String             urlMarker,
                                                                 String             integrationConnectorGUID,
                                                                 ResultsRequestBody requestBody)
    {
        final String methodName = "getIntegrationGroups";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler handler = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, urlMarker, methodName);

            response.setElements(handler.getIntegrationGroups(userId, integrationConnectorGUID, requestBody));
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
     * Asset structure relationships
     */

    /**
     * Attach a deployed API to the endpoint where it is called.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param deployedAPIGUID unique identifier of the deployed API
     * @param endpointGUID unique identifier of the endpoint
     * @param requestBody properties for the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkAPIEndpoint(String                     serverName,
                                        String                     urlMarker,
                                        String                     deployedAPIGUID,
                                        String                     endpointGUID,
                                        NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkAPIEndpoint";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                handler.linkAPIEndpoint(userId, deployedAPIGUID, endpointGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof APIEndpointProperties properties)
            {
                handler.linkAPIEndpoint(userId, deployedAPIGUID, endpointGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkAPIEndpoint(userId, deployedAPIGUID, endpointGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(APIEndpointProperties.class.getName(), methodName);
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
     * Detach a deployed API from the endpoint where it is called.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param deployedAPIGUID unique identifier of the deployed API
     * @param endpointGUID unique identifier of the endpoint
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachAPIEndpoint(String                        serverName,
                                          String                        urlMarker,
                                          String                        deployedAPIGUID,
                                          String                        endpointGUID,
                                          DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachAPIEndpoint";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.detachAPIEndpoint(userId, deployedAPIGUID, endpointGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Attach a child process to its parent process.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param parentProcessGUID unique identifier of the parent process
     * @param childProcessGUID unique identifier of the child process
     * @param requestBody properties for the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkProcessHierarchy(String                     serverName,
                                             String                     urlMarker,
                                             String                     parentProcessGUID,
                                             String                     childProcessGUID,
                                             NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkProcessHierarchy";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                handler.linkProcessHierarchy(userId, parentProcessGUID, childProcessGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof ProcessHierarchyProperties properties)
            {
                handler.linkProcessHierarchy(userId, parentProcessGUID, childProcessGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkProcessHierarchy(userId, parentProcessGUID, childProcessGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(ProcessHierarchyProperties.class.getName(), methodName);
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
     * Detach a child process from its parent process.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param parentProcessGUID unique identifier of the parent process
     * @param childProcessGUID unique identifier of the child process
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachProcessHierarchy(String                        serverName,
                                               String                        urlMarker,
                                               String                        parentProcessGUID,
                                               String                        childProcessGUID,
                                               DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachProcessHierarchy";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.detachProcessHierarchy(userId, parentProcessGUID, childProcessGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Attach a data file to the file folder that it is stored in.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param folderGUID unique identifier of the file folder
     * @param fileGUID unique identifier of the data file that is stored in the folder
     * @param requestBody properties for the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkNestedFiles(String                     serverName,
                                        String                     urlMarker,
                                        String                     folderGUID,
                                        String                     fileGUID,
                                        NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkNestedFiles";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                handler.linkNestedFiles(userId, folderGUID, fileGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof NestedFileProperties properties)
            {
                handler.linkNestedFiles(userId, folderGUID, fileGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkNestedFiles(userId, folderGUID, fileGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(NestedFileProperties.class.getName(), methodName);
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
     * Detach a data file from the file folder that it is stored in.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param folderGUID unique identifier of the file folder
     * @param fileGUID unique identifier of the data file that is stored in the folder
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachNestedFile(String                        serverName,
                                         String                        urlMarker,
                                         String                        folderGUID,
                                         String                        fileGUID,
                                         DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachNestedFile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.detachNestedFile(userId, folderGUID, fileGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Attach a data file to a file folder that links to it without storing it.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param folderGUID unique identifier of the file folder
     * @param fileGUID unique identifier of the data file that is linked to the folder
     * @param requestBody properties for the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkLinkedFiles(String                     serverName,
                                        String                     urlMarker,
                                        String                     folderGUID,
                                        String                     fileGUID,
                                        NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkLinkedFiles";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                handler.linkLinkedFiles(userId, folderGUID, fileGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof LinkedFileProperties properties)
            {
                handler.linkLinkedFiles(userId, folderGUID, fileGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkLinkedFiles(userId, folderGUID, fileGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(LinkedFileProperties.class.getName(), methodName);
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
     * Detach a data file from a file folder that links to it without storing it.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param folderGUID unique identifier of the file folder
     * @param fileGUID unique identifier of the data file that is linked to the folder
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachLinkedFile(String                        serverName,
                                         String                        urlMarker,
                                         String                        folderGUID,
                                         String                        fileGUID,
                                         DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachLinkedFile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.detachLinkedFile(userId, folderGUID, fileGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Attach a child file folder to its parent file folder.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param parentFolderGUID unique identifier of the parent file folder
     * @param childFolderGUID unique identifier of the child file folder
     * @param requestBody properties for the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkFolderHierarchy(String                     serverName,
                                            String                     urlMarker,
                                            String                     parentFolderGUID,
                                            String                     childFolderGUID,
                                            NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkFolderHierarchy";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                handler.linkFolderHierarchy(userId, parentFolderGUID, childFolderGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof FolderHierarchyProperties properties)
            {
                handler.linkFolderHierarchy(userId, parentFolderGUID, childFolderGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkFolderHierarchy(userId, parentFolderGUID, childFolderGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(FolderHierarchyProperties.class.getName(), methodName);
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
     * Detach a child file folder from its parent file folder.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param parentFolderGUID unique identifier of the parent file folder
     * @param childFolderGUID unique identifier of the child file folder
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachFolderHierarchy(String                        serverName,
                                              String                        urlMarker,
                                              String                        parentFolderGUID,
                                              String                        childFolderGUID,
                                              DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachFolderHierarchy";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.detachFolderHierarchy(userId, parentFolderGUID, childFolderGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Attach a resource that is impacted by an incident report to that incident report.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param resourceGUID unique identifier of the impacted resource
     * @param incidentReportGUID unique identifier of the incident report
     * @param requestBody properties for the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkImpactedResource(String                     serverName,
                                             String                     urlMarker,
                                             String                     resourceGUID,
                                             String                     incidentReportGUID,
                                             NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkImpactedResource";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                handler.linkImpactedResource(userId, resourceGUID, incidentReportGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof ImpactedResourceProperties properties)
            {
                handler.linkImpactedResource(userId, resourceGUID, incidentReportGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkImpactedResource(userId, resourceGUID, incidentReportGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(ImpactedResourceProperties.class.getName(), methodName);
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
     * Detach a resource from an incident report that no longer impacts it.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param resourceGUID unique identifier of the impacted resource
     * @param incidentReportGUID unique identifier of the incident report
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachImpactedResource(String                        serverName,
                                               String                        urlMarker,
                                               String                        resourceGUID,
                                               String                        incidentReportGUID,
                                               DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachImpactedResource";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.detachImpactedResource(userId, resourceGUID, incidentReportGUID, requestBody);
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
     * Further asset relationships
     */

    /**
     * Attach an asset that holds a log to the element that the log is about.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that the log is about
     * @param logAssetGUID unique identifier of the asset that holds the log
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkAssociatedLog(String                     serverName,
                                          String                     urlMarker,
                                          String                     elementGUID,
                                          String                     logAssetGUID,
                                          NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkAssociatedLog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                handler.linkAssociatedLog(userId, elementGUID, logAssetGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof AssociatedLogProperties properties)
            {
                handler.linkAssociatedLog(userId, elementGUID, logAssetGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkAssociatedLog(userId, elementGUID, logAssetGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(AssociatedLogProperties.class.getName(), methodName);
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
     * Detach an asset that holds a log from the element that the log was about.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that the log is about
     * @param logAssetGUID unique identifier of the asset that holds the log
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachAssociatedLog(String                        serverName,
                                            String                        urlMarker,
                                            String                        elementGUID,
                                            String                        logAssetGUID,
                                            DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachAssociatedLog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.detachAssociatedLog(userId, elementGUID, logAssetGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Attach an asset holding sample data to the element that the sample was taken from.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that the sample was taken from
     * @param sampleDataGUID unique identifier of the asset holding the sample data
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkSampleData(String                     serverName,
                                       String                     urlMarker,
                                       String                     elementGUID,
                                       String                     sampleDataGUID,
                                       NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkSampleData";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                handler.linkSampleData(userId, elementGUID, sampleDataGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof SampleDataProperties properties)
            {
                handler.linkSampleData(userId, elementGUID, sampleDataGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkSampleData(userId, elementGUID, sampleDataGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(SampleDataProperties.class.getName(), methodName);
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
     * Detach an asset holding sample data from the element that the sample was taken from.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that the sample was taken from
     * @param sampleDataGUID unique identifier of the asset holding the sample data
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachSampleData(String                        serverName,
                                         String                        urlMarker,
                                         String                        elementGUID,
                                         String                        sampleDataGUID,
                                         DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachSampleData";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.detachSampleData(userId, elementGUID, sampleDataGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Attach a port to the process that owns it.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param processGUID unique identifier of the process that owns the port
     * @param portGUID unique identifier of the port
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkProcessPort(String                     serverName,
                                        String                     urlMarker,
                                        String                     processGUID,
                                        String                     portGUID,
                                        NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkProcessPort";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                handler.linkProcessPort(userId, processGUID, portGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof ProcessPortProperties properties)
            {
                handler.linkProcessPort(userId, processGUID, portGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkProcessPort(userId, processGUID, portGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(ProcessPortProperties.class.getName(), methodName);
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
     * Detach a port from the process that owned it.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param processGUID unique identifier of the process that owns the port
     * @param portGUID unique identifier of the port
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachProcessPort(String                        serverName,
                                          String                        urlMarker,
                                          String                        processGUID,
                                          String                        portGUID,
                                          DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachProcessPort";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.detachProcessPort(userId, processGUID, portGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Attach an archive file to the collection that describes its contents.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param archiveFileGUID unique identifier of the archive file
     * @param collectionGUID unique identifier of the collection describing the archive's contents
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkArchiveContents(String                     serverName,
                                            String                     urlMarker,
                                            String                     archiveFileGUID,
                                            String                     collectionGUID,
                                            NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkArchiveContents";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                handler.linkArchiveContents(userId, archiveFileGUID, collectionGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof ArchiveContentsProperties properties)
            {
                handler.linkArchiveContents(userId, archiveFileGUID, collectionGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkArchiveContents(userId, archiveFileGUID, collectionGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(ArchiveContentsProperties.class.getName(), methodName);
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
     * Detach an archive file from the collection that described its contents.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param archiveFileGUID unique identifier of the archive file
     * @param collectionGUID unique identifier of the collection describing the archive's contents
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachArchiveContents(String                        serverName,
                                              String                        urlMarker,
                                              String                        archiveFileGUID,
                                              String                        collectionGUID,
                                              DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachArchiveContents";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.detachArchiveContents(userId, archiveFileGUID, collectionGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Attach a media file to another media file that is related to it.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param mediaFileGUID unique identifier of the media file
     * @param linkedMediaFileGUID unique identifier of the related media file
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkLinkedMedia(String                     serverName,
                                        String                     urlMarker,
                                        String                     mediaFileGUID,
                                        String                     linkedMediaFileGUID,
                                        NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkLinkedMedia";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                handler.linkLinkedMedia(userId, mediaFileGUID, linkedMediaFileGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof LinkedMediaProperties properties)
            {
                handler.linkLinkedMedia(userId, mediaFileGUID, linkedMediaFileGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkLinkedMedia(userId, mediaFileGUID, linkedMediaFileGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(LinkedMediaProperties.class.getName(), methodName);
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
     * Detach a media file from another media file that was related to it.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param mediaFileGUID unique identifier of the media file
     * @param linkedMediaFileGUID unique identifier of the related media file
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachLinkedMedia(String                        serverName,
                                          String                        urlMarker,
                                          String                        mediaFileGUID,
                                          String                        linkedMediaFileGUID,
                                          DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachLinkedMedia";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.detachLinkedMedia(userId, mediaFileGUID, linkedMediaFileGUID, requestBody);
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
     * Asset log classifications
     */

    /**
     * Classify an asset to say that it holds an audit log.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param assetGUID unique identifier of the asset
     * @param requestBody properties for the classification
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setAssetAsAuditLog(String                       serverName,
                                           String                       urlMarker,
                                           String                       assetGUID,
                                           NewClassificationRequestBody requestBody)
    {
        final String methodName = "setAssetAsAuditLog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                handler.setAssetAsAuditLog(userId, assetGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof AuditLogProperties properties)
            {
                handler.setAssetAsAuditLog(userId, assetGUID, properties, requestBody);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.setAssetAsAuditLog(userId, assetGUID, null, requestBody);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(AuditLogProperties.class.getName(), methodName);
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
     * Remove the audit log designation from an asset.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param assetGUID unique identifier of the asset
     * @param requestBody options for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse clearAssetAsAuditLog(String                          serverName,
                                             String                          urlMarker,
                                             String                          assetGUID,
                                             DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearAssetAsAuditLog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.clearAssetAsAuditLog(userId, assetGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Classify an asset to say that it holds a lineage log.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param assetGUID unique identifier of the asset
     * @param requestBody properties for the classification
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setAssetAsLineageLog(String                       serverName,
                                             String                       urlMarker,
                                             String                       assetGUID,
                                             NewClassificationRequestBody requestBody)
    {
        final String methodName = "setAssetAsLineageLog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                handler.setAssetAsLineageLog(userId, assetGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof LineageLogProperties properties)
            {
                handler.setAssetAsLineageLog(userId, assetGUID, properties, requestBody);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.setAssetAsLineageLog(userId, assetGUID, null, requestBody);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(LineageLogProperties.class.getName(), methodName);
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
     * Remove the lineage log designation from an asset.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param assetGUID unique identifier of the asset
     * @param requestBody options for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse clearAssetAsLineageLog(String                          serverName,
                                               String                          urlMarker,
                                               String                          assetGUID,
                                               DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearAssetAsLineageLog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.clearAssetAsLineageLog(userId, assetGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Classify an asset to say that it holds a metering log.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param assetGUID unique identifier of the asset
     * @param requestBody properties for the classification
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setAssetAsMeteringLog(String                       serverName,
                                              String                       urlMarker,
                                              String                       assetGUID,
                                              NewClassificationRequestBody requestBody)
    {
        final String methodName = "setAssetAsMeteringLog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                handler.setAssetAsMeteringLog(userId, assetGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof MeteringLogProperties properties)
            {
                handler.setAssetAsMeteringLog(userId, assetGUID, properties, requestBody);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.setAssetAsMeteringLog(userId, assetGUID, null, requestBody);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(MeteringLogProperties.class.getName(), methodName);
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
     * Remove the metering log designation from an asset.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param assetGUID unique identifier of the asset
     * @param requestBody options for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse clearAssetAsMeteringLog(String                          serverName,
                                                String                          urlMarker,
                                                String                          assetGUID,
                                                DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearAssetAsMeteringLog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.clearAssetAsMeteringLog(userId, assetGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Classify an asset to say that it holds a security log.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param assetGUID unique identifier of the asset
     * @param requestBody properties for the classification
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setAssetAsSecurityLog(String                       serverName,
                                              String                       urlMarker,
                                              String                       assetGUID,
                                              NewClassificationRequestBody requestBody)
    {
        final String methodName = "setAssetAsSecurityLog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                handler.setAssetAsSecurityLog(userId, assetGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof SecurityLogProperties properties)
            {
                handler.setAssetAsSecurityLog(userId, assetGUID, properties, requestBody);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.setAssetAsSecurityLog(userId, assetGUID, null, requestBody);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(SecurityLogProperties.class.getName(), methodName);
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
     * Remove the security log designation from an asset.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param assetGUID unique identifier of the asset
     * @param requestBody options for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse clearAssetAsSecurityLog(String                          serverName,
                                                String                          urlMarker,
                                                String                          assetGUID,
                                                DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearAssetAsSecurityLog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.clearAssetAsSecurityLog(userId, assetGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Classify an asset to say that it holds a backlog of exceptions that need to be resolved.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param assetGUID unique identifier of the asset
     * @param requestBody properties for the classification
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setAssetAsExceptionBacklog(String                       serverName,
                                                   String                       urlMarker,
                                                   String                       assetGUID,
                                                   NewClassificationRequestBody requestBody)
    {
        final String methodName = "setAssetAsExceptionBacklog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                handler.setAssetAsExceptionBacklog(userId, assetGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof ExceptionBacklogProperties properties)
            {
                handler.setAssetAsExceptionBacklog(userId, assetGUID, properties, requestBody);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.setAssetAsExceptionBacklog(userId, assetGUID, null, requestBody);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(ExceptionBacklogProperties.class.getName(), methodName);
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
     * Remove the exception backlog designation from an asset.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param assetGUID unique identifier of the asset
     * @param requestBody options for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse clearAssetAsExceptionBacklog(String                          serverName,
                                                     String                          urlMarker,
                                                     String                          assetGUID,
                                                     DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearAssetAsExceptionBacklog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.clearAssetAsExceptionBacklog(userId, assetGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Classify an asset to say that it holds the results of analysing a log.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param assetGUID unique identifier of the asset
     * @param requestBody properties for the classification
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setAssetAsLogAnalysis(String                       serverName,
                                              String                       urlMarker,
                                              String                       assetGUID,
                                              NewClassificationRequestBody requestBody)
    {
        final String methodName = "setAssetAsLogAnalysis";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                handler.setAssetAsLogAnalysis(userId, assetGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof LogAnalysisProperties properties)
            {
                handler.setAssetAsLogAnalysis(userId, assetGUID, properties, requestBody);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.setAssetAsLogAnalysis(userId, assetGUID, null, requestBody);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(LogAnalysisProperties.class.getName(), methodName);
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
     * Remove the log analysis designation from an asset.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param assetGUID unique identifier of the asset
     * @param requestBody options for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse clearAssetAsLogAnalysis(String                          serverName,
                                                String                          urlMarker,
                                                String                          assetGUID,
                                                DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearAssetAsLogAnalysis";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.clearAssetAsLogAnalysis(userId, assetGUID, requestBody);
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
     * Deployed API classifications
     */

    /**
     * Classify a deployed API to say that it provides a listener interface.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param deployedAPIGUID unique identifier of the deployed API
     * @param requestBody properties for the classification
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setAPIAsListenerInterface(String                       serverName,
                                                  String                       urlMarker,
                                                  String                       deployedAPIGUID,
                                                  NewClassificationRequestBody requestBody)
    {
        final String methodName = "setAPIAsListenerInterface";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                handler.setAPIAsListenerInterface(userId, deployedAPIGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof ListenerInterfaceProperties properties)
            {
                handler.setAPIAsListenerInterface(userId, deployedAPIGUID, properties, requestBody);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.setAPIAsListenerInterface(userId, deployedAPIGUID, null, requestBody);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(ListenerInterfaceProperties.class.getName(), methodName);
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
     * Remove the listener interface designation from a deployed API.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param deployedAPIGUID unique identifier of the deployed API
     * @param requestBody options for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse clearAPIAsListenerInterface(String                          serverName,
                                                    String                          urlMarker,
                                                    String                          deployedAPIGUID,
                                                    DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearAPIAsListenerInterface";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.clearAPIAsListenerInterface(userId, deployedAPIGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Classify a deployed API to say that it provides a publisher interface.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param deployedAPIGUID unique identifier of the deployed API
     * @param requestBody properties for the classification
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setAPIAsPublisherInterface(String                       serverName,
                                                   String                       urlMarker,
                                                   String                       deployedAPIGUID,
                                                   NewClassificationRequestBody requestBody)
    {
        final String methodName = "setAPIAsPublisherInterface";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                handler.setAPIAsPublisherInterface(userId, deployedAPIGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof PublisherInterfaceProperties properties)
            {
                handler.setAPIAsPublisherInterface(userId, deployedAPIGUID, properties, requestBody);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.setAPIAsPublisherInterface(userId, deployedAPIGUID, null, requestBody);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(PublisherInterfaceProperties.class.getName(), methodName);
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
     * Remove the publisher interface designation from a deployed API.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param deployedAPIGUID unique identifier of the deployed API
     * @param requestBody options for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse clearAPIAsPublisherInterface(String                          serverName,
                                                     String                          urlMarker,
                                                     String                          deployedAPIGUID,
                                                     DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearAPIAsPublisherInterface";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.clearAPIAsPublisherInterface(userId, deployedAPIGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Classify a deployed API to say that it provides a request-response interface.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param deployedAPIGUID unique identifier of the deployed API
     * @param requestBody properties for the classification
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setAPIAsRequestResponseInterface(String                       serverName,
                                                         String                       urlMarker,
                                                         String                       deployedAPIGUID,
                                                         NewClassificationRequestBody requestBody)
    {
        final String methodName = "setAPIAsRequestResponseInterface";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                handler.setAPIAsRequestResponseInterface(userId, deployedAPIGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof RequestResponseInterfaceProperties properties)
            {
                handler.setAPIAsRequestResponseInterface(userId, deployedAPIGUID, properties, requestBody);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.setAPIAsRequestResponseInterface(userId, deployedAPIGUID, null, requestBody);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(RequestResponseInterfaceProperties.class.getName(), methodName);
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
     * Remove the request-response interface designation from a deployed API.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param deployedAPIGUID unique identifier of the deployed API
     * @param requestBody options for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse clearAPIAsRequestResponseInterface(String                          serverName,
                                                           String                          urlMarker,
                                                           String                          deployedAPIGUID,
                                                           DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearAPIAsRequestResponseInterface";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.clearAPIAsRequestResponseInterface(userId, deployedAPIGUID, requestBody);
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
     * Data asset encoding classification
     */

    /**
     * Classify a data asset to describe how its data is encoded.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param dataAssetGUID unique identifier of the data asset
     * @param requestBody properties for the classification
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setDataAssetEncoding(String                       serverName,
                                             String                       urlMarker,
                                             String                       dataAssetGUID,
                                             NewClassificationRequestBody requestBody)
    {
        final String methodName = "setDataAssetEncoding";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                handler.setDataAssetEncoding(userId, dataAssetGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof DataAssetEncodingProperties properties)
            {
                handler.setDataAssetEncoding(userId, dataAssetGUID, properties, requestBody);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.setDataAssetEncoding(userId, dataAssetGUID, null, requestBody);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(DataAssetEncodingProperties.class.getName(), methodName);
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
     * Remove the encoding description from a data asset.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param dataAssetGUID unique identifier of the data asset
     * @param requestBody options for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse clearDataAssetEncoding(String                          serverName,
                                               String                          urlMarker,
                                               String                          dataAssetGUID,
                                               DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearDataAssetEncoding";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.clearDataAssetEncoding(userId, dataAssetGUID, requestBody);
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
     * Port delegation relationship
     */

    /**
     * Attach a port to the port that it delegates to.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param delegatingFromPortGUID unique identifier of the port that delegates
     * @param delegatingToPortGUID unique identifier of the port that is delegated to
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkPortDelegation(String                     serverName,
                                           String                     urlMarker,
                                           String                     delegatingFromPortGUID,
                                           String                     delegatingToPortGUID,
                                           NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkPortDelegation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                handler.linkPortDelegation(userId, delegatingFromPortGUID, delegatingToPortGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof PortDelegationProperties properties)
            {
                handler.linkPortDelegation(userId, delegatingFromPortGUID, delegatingToPortGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkPortDelegation(userId, delegatingFromPortGUID, delegatingToPortGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(PortDelegationProperties.class.getName(), methodName);
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
     * Detach a port from the port that it delegated to.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param delegatingFromPortGUID unique identifier of the port that delegates
     * @param delegatingToPortGUID unique identifier of the port that is delegated to
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachPortDelegation(String                        serverName,
                                             String                        urlMarker,
                                             String                        delegatingFromPortGUID,
                                             String                        delegatingToPortGUID,
                                             DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachPortDelegation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, urlMarker, methodName);

            handler.detachPortDelegation(userId, delegatingFromPortGUID, delegatingToPortGUID, requestBody);
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
     * Registered integration connector relationship
     */

    /**
     * Register an integration connector with the integration group that runs it.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param integrationGroupGUID unique identifier of the integration group
     * @param integrationConnectorGUID unique identifier of the integration connector
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkRegisteredIntegrationConnector(String                     serverName,
                                                           String                     urlMarker,
                                                           String                     integrationGroupGUID,
                                                           String                     integrationConnectorGUID,
                                                           NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkRegisteredIntegrationConnector";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler handler = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                handler.linkRegisteredIntegrationConnector(userId, integrationGroupGUID, integrationConnectorGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof RegisteredIntegrationConnectorProperties properties)
            {
                handler.linkRegisteredIntegrationConnector(userId, integrationGroupGUID, integrationConnectorGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkRegisteredIntegrationConnector(userId, integrationGroupGUID, integrationConnectorGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(RegisteredIntegrationConnectorProperties.class.getName(), methodName);
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
     * Remove an integration connector from the integration group that ran it.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param integrationGroupGUID unique identifier of the integration group
     * @param integrationConnectorGUID unique identifier of the integration connector
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachRegisteredIntegrationConnector(String                        serverName,
                                                             String                        urlMarker,
                                                             String                        integrationGroupGUID,
                                                             String                        integrationConnectorGUID,
                                                             DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachRegisteredIntegrationConnector";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler handler = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, urlMarker, methodName);

            handler.detachRegisteredIntegrationConnector(userId, integrationGroupGUID, integrationConnectorGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }
}
