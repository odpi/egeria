/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.runtimemanager.server;


import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.OMAGServerPlatformConnector;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.EngineHostConnector;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.IntegrationDaemonConnector;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.MetadataAccessServerConnector;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.OMAGServerConnectorBase;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.client.ConnectedAssetClient;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.AssetHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.MetadataRepositoryCohortHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.MetadataRepositoryCohortProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.MetadataCohortPeerProperties;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.rest.ConnectorConfigPropertiesRequestBody;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.serveroperations.rest.SuccessMessageResponse;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.odpi.openmetadata.viewservices.runtimemanager.rest.PlatformReportResponse;
import org.odpi.openmetadata.viewservices.runtimemanager.rest.ServerReportResponse;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * The RuntimeManagerRESTServices provides the server-side implementation of the Runtime Manager Open Metadata
 * View Service (OMVS).
 */
public class RuntimeManagerRESTServices extends TokenController
{
    private static final RuntimeManagerInstanceHandler instanceHandler = new RuntimeManagerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(RuntimeManagerRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public RuntimeManagerRESTServices()
    {
    }


    /**
     * Returns the list of platforms with a particular qualifiedName or name.
     *
     * @param serverName         name of called server
     * @param requestBody qualified name or display name of the platform
     *
     * @return a list of projects
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getPlatformsByName(String            serverName,
                                                               FilterRequestBody requestBody)
    {
        final String methodName = "getPlatformsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getSoftwarePlatformHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getAssetsByName(userId, requestBody.getFilter(), requestBody));
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
     * Returns the list of platforms with a particular deployed implementation type.
     *
     * @param serverName         name of called server
     * @param getTemplates boolean indicating whether templates or non-template platforms should be returned.
     * @param requestBody qualified name or display name of the platform
     *
     * @return a list of projects
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getPlatformsByDeployedImplType(String            serverName,
                                                                           boolean           getTemplates,
                                                                           FilterRequestBody requestBody)
    {
        final String methodName = "getPlatformsByDeployedImplType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getSoftwarePlatformHandler(userId, serverName, methodName);

            List<OpenMetadataRootElement> platforms;
            if (requestBody != null)
            {
                platforms = handler.getAssetsByDeployedImplementationType(userId, requestBody.getFilter(), requestBody);
            }
            else
            {
                platforms = handler.getAssetsByDeployedImplementationType(userId, null, null);
            }

            if (platforms != null)
            {
                List<OpenMetadataRootElement> filteredPlatforms = new ArrayList<>();

                for (OpenMetadataRootElement platformElement : platforms)
                {
                    if (platformElement != null)
                    {
                        if (this.isTemplate(platformElement.getElementHeader()) && (getTemplates))
                        {
                            filteredPlatforms.add(platformElement);
                        }

                        if (!this.isTemplate(platformElement.getElementHeader()) && (!getTemplates))
                        {
                            filteredPlatforms.add(platformElement);
                        }
                    }
                }

                if (! filteredPlatforms.isEmpty())
                {
                    response.setElements(filteredPlatforms);
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
     * Determine whether the element is a template.
     *
     * @param elementHeader element header
     * @return boolean flag
     */
    private boolean isTemplate(ElementHeader elementHeader)
    {
        return elementHeader.getTemplate() != null;
    }


    /**
     * Returns details about the platform's catalog entry (asset).
     *
     * @param serverName  name of called server
     * @param platformGUID unique identifier of the platform
     * @param requestBody effective time
     * @return a list of platforms
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementResponse getPlatformByGUID(String             serverName,
                                                             String             platformGUID,
                                                             GetRequestBody requestBody)
    {
        final String methodName = "getPlatformByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getSoftwarePlatformHandler(userId, serverName, methodName);

            response.setElement(handler.getAssetByGUID(userId, platformGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Returns details about the running platform.
     *
     * @param serverName  name of called server
     * @param platformGUID unique identifier of the platform
     * @return a list of platforms
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public PlatformReportResponse getPlatformReport(String serverName,
                                                    String platformGUID)
    {
        final String methodName = "getPlatformReport";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        PlatformReportResponse response = new PlatformReportResponse();
        AuditLog               auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectedAssetClient connectedAssetClient = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);
            AssetHandler         platformHandler      = instanceHandler.getSoftwarePlatformHandler(userId, serverName, methodName);

            OpenMetadataRootElement asset = platformHandler.getAssetByGUID(userId, platformGUID, null);

            Connector     connector = connectedAssetClient.getConnectorForAsset(userId, platformGUID, auditLog);

            if (connector instanceof OMAGServerPlatformConnector omagServerPlatformConnector)
            {
                if ((asset != null) && (asset.getProperties() instanceof AssetProperties assetProperties))
                {
                    omagServerPlatformConnector.setPlatformName(assetProperties.getResourceName());
                }

                omagServerPlatformConnector.setClientUserId(userId);
                omagServerPlatformConnector.start();
                response.setElement(omagServerPlatformConnector.getPlatformReport());
                omagServerPlatformConnector.disconnect();
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
     * Returns the list of platforms with a particular qualifiedName or name.
     *
     * @param serverName         name of called server

     * @param requestBody qualified name or display name of the platform
     *
     * @return a list of projects
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getServersByName(String            serverName,
                                                             FilterRequestBody requestBody)
    {
        final String methodName = "getServersByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getSoftwareServerHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getAssetsByName(userId, requestBody.getFilter(), requestBody));
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
     * Returns the list of servers with a particular deployed implementation type.
     *
     * @param serverName         name of called server
     * @param getTemplates boolean indicating whether templates or non-template platforms should be returned.
     * @param requestBody qualified name or display name of the platform
     *
     * @return a list of projects
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getServersByDeployedImplType(String            serverName,
                                                                         boolean           getTemplates,
                                                                         FilterRequestBody requestBody)
    {
        final String methodName = "getServersByDeployedImplType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getSoftwareServerHandler(userId, serverName, methodName);

            List<OpenMetadataRootElement> servers = handler.getAssetsByDeployedImplementationType(userId, requestBody.getFilter(), requestBody);

            if (servers != null)
            {
                List<OpenMetadataRootElement> filteredServers = new ArrayList<>();

                for (OpenMetadataRootElement serverElement : servers)
                {
                    if (serverElement != null)
                    {
                        if (this.isTemplate(serverElement.getElementHeader()) && (getTemplates))
                        {
                            filteredServers.add(serverElement);
                        }

                        if (!this.isTemplate(serverElement.getElementHeader()) && (!getTemplates))
                        {
                            filteredServers.add(serverElement);
                        }
                    }
                }

                if (! filteredServers.isEmpty())
                {
                    response.setElements(filteredServers);
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
     * Returns details about the server's catalog entry (asset).
     *
     * @param serverName  name of called server
     * @param serverGUID unique identifier of the platform
     * @param requestBody effective time
     * @return a list of platforms
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */

    public OpenMetadataRootElementResponse getServerByGUID(String         serverName,
                                                           String         serverGUID,
                                                           GetRequestBody requestBody)
    {
        final String methodName = "getServerByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getSoftwareServerHandler(userId, serverName, methodName);

            response.setElement(handler.getAssetByGUID(userId, serverGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Returns details about the running server.
     *
     * @param serverName  name of called server
     * @param serverGUID unique identifier of the server
     * @return a list of platforms
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public ServerReportResponse getServerReport(String serverName,
                                                String serverGUID)
    {
        final String methodName = "getServerReport";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ServerReportResponse response = new ServerReportResponse();
        AuditLog             auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectedAssetClient handler = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);

            Connector connector = handler.getConnectorForAsset(userId, serverGUID, auditLog);

            if (connector instanceof OMAGServerConnectorBase omagServerConnector)
            {
                omagServerConnector.setClientUserId(userId);
                omagServerConnector.start();
                response.setElement(omagServerConnector.getServerReport());
                omagServerConnector.disconnect();
            }
            else
            {
                restExceptionHandler.handleInvalidCallToServer(OMAGServerConnectorBase.class.getName(),
                                                               methodName,
                                                               serverGUID,
                                                               connector.getClass().getName());
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /*
     * =============================================================
     * Initialization and shutdown
     */

    /**
     * Activate the open metadata and governance services using the stored configuration information.
     *
     * @param serverName  local server name
     * @param serverGUID unique identifier of the server to call
     * @return success message response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the server name is invalid or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration.
     */
    public SuccessMessageResponse activateWithStoredConfig(String serverName,
                                                           String serverGUID)
    {
        final String methodName = "activateWithStoredConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SuccessMessageResponse response = new SuccessMessageResponse();
        AuditLog              auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectedAssetClient handler = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);

            Connector connector = handler.getConnectorForAsset(userId, serverGUID, auditLog);

            if (connector instanceof OMAGServerConnectorBase omagServerConnector)
            {
                omagServerConnector.setClientUserId(userId);
                omagServerConnector.start();
                response.setSuccessMessage(omagServerConnector.activateServer());
                omagServerConnector.disconnect();
            }
            else
            {
                restExceptionHandler.handleInvalidCallToServer(OMAGServerConnectorBase.class.getName(),
                                                               methodName,
                                                               serverGUID,
                                                               connector.getClass().getName());
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
     * Temporarily deactivate any open metadata and governance services for the requested server.
     *
     * @param serverName  local server name
     * @param serverGUID unique identifier of the server to call
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    public VoidResponse shutdownServer(String serverName,
                                       String serverGUID)
    {
        final String methodName = "shutdownServer";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectedAssetClient handler = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);

            Connector connector = handler.getConnectorForAsset(userId, serverGUID, auditLog);

            if (connector instanceof OMAGServerConnectorBase omagServerConnector)
            {
                omagServerConnector.setClientUserId(userId);
                omagServerConnector.start();
                omagServerConnector.shutdownServer();
                omagServerConnector.disconnect();
            }
            else
            {
                restExceptionHandler.handleInvalidCallToServer(OMAGServerConnectorBase.class.getName(),
                                                               methodName,
                                                               serverGUID,
                                                               connector.getClass().getName());
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
     * Terminate any running open metadata and governance services, remove the server from any open metadata cohorts.
     *
     * @param serverName  local server name
     * @param serverGUID unique identifier of the server to call
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    public VoidResponse shutdownAndUnregisterServer(String serverName,
                                                    String serverGUID)
    {
        final String methodName = "shutdownAndUnregisterServer";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectedAssetClient handler = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);

            Connector connector = handler.getConnectorForAsset(userId, serverGUID, auditLog);

            if (connector instanceof OMAGServerConnectorBase omagServerConnector)
            {
                omagServerConnector.setClientUserId(userId);
                omagServerConnector.start();
                omagServerConnector.shutdownAndUnregisterServer();
                omagServerConnector.disconnect();
            }
            else
            {
                restExceptionHandler.handleInvalidCallToServer(OMAGServerConnectorBase.class.getName(),
                                                               methodName,
                                                               serverGUID,
                                                               connector.getClass().getName());
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /*
     * =============================================================
     * Open Metadata Repository Cohorts
     */

    /**
     * Create an open metadata repository cohort.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the cohort.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createMetadataRepositoryCohort(String                serverName,
                                                       NewElementRequestBody requestBody)
    {
        final String methodName = "createMetadataRepositoryCohort";

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
                MetadataRepositoryCohortHandler handler = instanceHandler.getMetadataRepositoryCohortHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof MetadataRepositoryCohortProperties cohortProperties)
                {
                    response.setGUID(handler.createMetadataRepositoryCohort(userId,
                                                                            requestBody,
                                                                            requestBody.getInitialClassifications(),
                                                                            cohortProperties,
                                                                            requestBody.getParentRelationshipProperties()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(MetadataRepositoryCohortProperties.class.getName(), methodName);
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
     * Create a new metadata element to represent an open metadata repository cohort using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createMetadataRepositoryCohortFromTemplate(String              serverName,
                                                                   TemplateRequestBody requestBody)
    {
        final String methodName = "createMetadataRepositoryCohortFromTemplate";

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
                MetadataRepositoryCohortHandler handler = instanceHandler.getMetadataRepositoryCohortHandler(userId, serverName, methodName);

                response.setGUID(handler.createMetadataRepositoryCohortFromTemplate(userId,
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
     * Update the properties of an open metadata repository cohort.
     *
     * @param serverName         name of called server.
     * @param metadataRepositoryCohortGUID unique identifier of the cohort (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateMetadataRepositoryCohort(String                   serverName,
                                                       String                   metadataRepositoryCohortGUID,
                                                       UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateMetadataRepositoryCohort";

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
                MetadataRepositoryCohortHandler handler = instanceHandler.getMetadataRepositoryCohortHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof MetadataRepositoryCohortProperties cohortProperties)
                {
                    handler.updateMetadataRepositoryCohort(userId,
                                                           metadataRepositoryCohortGUID,
                                                           requestBody,
                                                           cohortProperties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(MetadataRepositoryCohortProperties.class.getName(), methodName);
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
     * Delete an open metadata repository cohort.
     *
     * @param serverName         name of called server
     * @param metadataRepositoryCohortGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteMetadataRepositoryCohort(String                   serverName,
                                                       String                   metadataRepositoryCohortGUID,
                                                       DeleteElementRequestBody requestBody)
    {
        final String methodName = "deleteMetadataRepositoryCohort";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            MetadataRepositoryCohortHandler handler = instanceHandler.getMetadataRepositoryCohortHandler(userId, serverName, methodName);

            handler.deleteMetadataRepositoryCohort(userId, metadataRepositoryCohortGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of cohort metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getMetadataRepositoryCohortsByName(String            serverName,
                                                                               FilterRequestBody requestBody)
    {
        final String methodName = "getMetadataRepositoryCohortsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            MetadataRepositoryCohortHandler handler = instanceHandler.getMetadataRepositoryCohortHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getMetadataRepositoryCohortsByName(userId, requestBody.getFilter(), requestBody));
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
     * Retrieve the list of cohort metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param metadataRepositoryCohortGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getMetadataRepositoryCohortByGUID(String         serverName,
                                                                             String         metadataRepositoryCohortGUID,
                                                                             GetRequestBody requestBody)
    {
        final String methodName = "getMetadataRepositoryCohortByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            MetadataRepositoryCohortHandler handler = instanceHandler.getMetadataRepositoryCohortHandler(userId, serverName, methodName);

            response.setElement(handler.getMetadataRepositoryCohortByGUID(userId, metadataRepositoryCohortGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of cohort metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findMetadataRepositoryCohorts(String                  serverName,
                                                                          SearchStringRequestBody requestBody)
    {
        final String methodName = "findMetadataRepositoryCohorts";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            MetadataRepositoryCohortHandler handler = instanceHandler.getMetadataRepositoryCohortHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findMetadataRepositoryCohorts(userId, requestBody.getSearchString(), requestBody));
            }
            else
            {
                response.setElements(handler.findMetadataRepositoryCohorts(userId, null, null));
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
     * Link cohort to one of its members.
     *
     * @param serverName         name of called server
     * @param cohortGUID    unique identifier of the cohort.
     * @param cohortMemberGUID    unique identifier of the cohort member.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkCohortMember(String                     serverName,
                                         String                     cohortGUID,
                                         String                     cohortMemberGUID,
                                         NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkCohortMember";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            MetadataRepositoryCohortHandler handler = instanceHandler.getMetadataRepositoryCohortHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof MetadataCohortPeerProperties metadataCohortPeerProperties)
                {
                    handler.linkCohortToMember(userId,
                                               cohortGUID,
                                               cohortMemberGUID,
                                               requestBody,
                                               metadataCohortPeerProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkCohortToMember(userId,
                                               cohortGUID,
                                               cohortMemberGUID,
                                               requestBody,
                                               null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(MetadataCohortPeerProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkCohortToMember(userId,
                                           cohortGUID,
                                           cohortMemberGUID,
                                           null,
                                           null);
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
     * Detach a member form a cohort.
     *
     * @param serverName         name of called server
     * @param cohortGUID    unique identifier of the parent subject area.
     * @param cohortMemberGUID    unique identifier of the nested subject area.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachCohortMember(String                        serverName,
                                           String                        cohortGUID,
                                           String                        cohortMemberGUID,
                                           DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachCohortMember";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            MetadataRepositoryCohortHandler handler = instanceHandler.getMetadataRepositoryCohortHandler(userId, serverName, methodName);

            handler.detachCohortFromMember(userId, cohortGUID, cohortMemberGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /*
     * =============================================================
     * Open Metadata Archives
     */


    /**
     * Add a new open metadata archive to running repository.
     *
     * @param serverName  local server name.
     * @param serverGUID unique identifier of the server to call
     * @param fileName name of the open metadata archive file.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or fileName parameter.
     */
    public VoidResponse addOpenMetadataArchiveFile(String serverName,
                                                   String serverGUID,
                                                   String fileName)
    {
        final String methodName = "addOpenMetadataArchiveFile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectedAssetClient handler = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);

            Connector connector = handler.getConnectorForAsset(userId, serverGUID, auditLog);

            if (connector instanceof MetadataAccessServerConnector omagServerConnector)
            {
                omagServerConnector.setClientUserId(userId);
                omagServerConnector.start();
                omagServerConnector.addOpenMetadataArchiveFile(fileName);
                omagServerConnector.disconnect();
            }
            else
            {
                restExceptionHandler.handleInvalidCallToServer(MetadataAccessServerConnector.class.getName(),
                                                               methodName,
                                                               serverGUID,
                                                               connector.getClass().getName());
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
     * Add a new open metadata archive to running repository.
     *
     * @param serverName  local server name.
     * @param serverGUID unique identifier of the server to call
     * @param openMetadataArchive contents of the open metadata archive file.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or openMetadataArchive parameter.
     */
    public VoidResponse addOpenMetadataArchiveContent(String              serverName,
                                                      String              serverGUID,
                                                      OpenMetadataArchive openMetadataArchive)
    {
        final String methodName = "addOpenMetadataArchive";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectedAssetClient handler = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);

            Connector connector = handler.getConnectorForAsset(userId, serverGUID, auditLog);

            if (connector instanceof MetadataAccessServerConnector omagServerConnector)
            {
                omagServerConnector.setClientUserId(userId);
                omagServerConnector.start();
                omagServerConnector.addOpenMetadataArchiveContent(openMetadataArchive);
                omagServerConnector.disconnect();
            }
            else
            {
                restExceptionHandler.handleInvalidCallToServer(MetadataAccessServerConnector.class.getName(),
                                                               methodName,
                                                               serverGUID,
                                                               connector.getClass().getName());
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /*
     * =============================================================
     * Engine Host
     */


    /**
     * Request that the governance engine refresh its configuration by calling the metadata server.
     * This request is useful if the metadata server has an outage, particularly while the
     * governance server is initializing.  This request just ensures that the latest configuration
     * is in use.
     *
     * @param serverName name of the governance server
     * @param serverGUID unique identifier of the server to call
     * @param governanceEngineName unique name of the governance engine
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  GovernanceEngineException there was a problem detected by the governance engine.
     */
    public  VoidResponse refreshConfig(String serverName,
                                       String serverGUID,
                                       String governanceEngineName)
    {
        final String methodName = "refreshConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectedAssetClient handler = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);

            Connector connector = handler.getConnectorForAsset(userId, serverGUID, auditLog);

            if (connector instanceof EngineHostConnector omagServerConnector)
            {
                omagServerConnector.setClientUserId(userId);
                omagServerConnector.start();
                omagServerConnector.refreshEngineConfig(governanceEngineName);
                omagServerConnector.disconnect();
            }
            else
            {
                restExceptionHandler.handleInvalidCallToServer(OMAGServerConnectorBase.class.getName(),
                                                               methodName,
                                                               serverGUID,
                                                               connector.getClass().getName());
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
     * Request that all governance engines refresh their configuration by calling the metadata server.
     * This request is useful if the metadata server has an outage, particularly while the
     * governance server is initializing.  This request just ensures that the latest configuration
     * is in use.
     *
     * @param serverName name of the governance server
     * @param serverGUID unique identifier of the server to call
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  GovernanceEngineException there was a problem detected by the governance engine.
     */
    public  VoidResponse refreshConfig(String serverName,
                                       String serverGUID)
    {
        final String methodName = "refreshConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectedAssetClient handler = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);

            Connector connector = handler.getConnectorForAsset(userId, serverGUID, auditLog);

            if (connector instanceof EngineHostConnector omagServerConnector)
            {
                omagServerConnector.setClientUserId(userId);
                omagServerConnector.start();
                omagServerConnector.refreshEngineConfig();
                omagServerConnector.disconnect();
            }
            else
            {
                restExceptionHandler.handleInvalidCallToServer(OMAGServerConnectorBase.class.getName(),
                                                               methodName,
                                                               serverGUID,
                                                               connector.getClass().getName());
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /*
     * =============================================================
     * Integration Daemon
     */


    /**
     * Retrieve the configuration properties of the named integration connector running in the integration daemon.
     *
     * @param serverName integration daemon server name
     * @param serverGUID unique identifier of the server to call
     * @param connectorName name of a specific connector
     *
     * @return properties map or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration service.
     */
    public PropertiesResponse getConfigurationProperties(String serverName,
                                                         String serverGUID,
                                                         String connectorName)
    {
        final String methodName = "getConfigurationProperties";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        PropertiesResponse response = new PropertiesResponse();
        AuditLog           auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectedAssetClient handler = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);

            Connector connector = handler.getConnectorForAsset(userId, serverGUID, auditLog);

            if (connector instanceof IntegrationDaemonConnector omagServerConnector)
            {
                omagServerConnector.setClientUserId(userId);
                omagServerConnector.start();
                response.setProperties(omagServerConnector.getConfigurationProperties(connectorName));
                omagServerConnector.disconnect();
            }
            else
            {
                restExceptionHandler.handleInvalidCallToServer(OMAGServerConnectorBase.class.getName(),
                                                               methodName,
                                                               serverGUID,
                                                               connector.getClass().getName());
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
     * Update the configuration properties of the integration connectors, or specific integration connector if a
     * connector name is supplied.  This update is in memory and will not persist over a server restart.
     *
     * @param serverName integration daemon server name
     * @param serverGUID unique identifier of the server to call
     * @param requestBody name of a specific connector or null for all connectors and the properties to change
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration service.
     */
    public  VoidResponse updateConfigurationProperties(String                               serverName,
                                                       String                               serverGUID,
                                                       ConnectorConfigPropertiesRequestBody requestBody)
    {
        final String methodName = "updateConfigurationProperties";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectedAssetClient handler = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);

            Connector connector = handler.getConnectorForAsset(userId, serverGUID, auditLog);

            if (connector instanceof IntegrationDaemonConnector omagServerConnector)
            {
                omagServerConnector.setClientUserId(userId);
                omagServerConnector.start();
                omagServerConnector.updateConfigurationProperties(requestBody.getConnectorName(),
                                                                  requestBody.getMergeUpdate(),
                                                                  requestBody.getConfigurationProperties());
                omagServerConnector.disconnect();
            }
            else
            {
                restExceptionHandler.handleInvalidCallToServer(OMAGServerConnectorBase.class.getName(),
                                                               methodName,
                                                               serverGUID,
                                                               connector.getClass().getName());
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
     * Update the endpoint network address for a specific integration connector.
     * This update is in memory and will not persist over a server restart.
     *
     * @param serverName integration daemon server name
     * @param serverGUID unique identifier of the server to call
     * @param connectorName name of a specific connector
     * @param requestBody new endpoint address
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration service.
     */
    public  VoidResponse updateEndpointNetworkAddress(String            serverName,
                                                      String            serverGUID,
                                                      String            connectorName,
                                                      StringRequestBody requestBody)
    {
        final String methodName = "updateEndpointNetworkAddress";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectedAssetClient handler = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);

            Connector connector = handler.getConnectorForAsset(userId, serverGUID, auditLog);

            if (connector instanceof IntegrationDaemonConnector omagServerConnector)
            {
                omagServerConnector.setClientUserId(userId);
                omagServerConnector.start();

                if ((requestBody == null) || requestBody.getString() == null)
                {
                    restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
                }
                else
                {
                    omagServerConnector.updateEndpointNetworkAddress(connectorName, requestBody.getString());
                }
                omagServerConnector.disconnect();
            }
            else
            {
                restExceptionHandler.handleInvalidCallToServer(OMAGServerConnectorBase.class.getName(),
                                                               methodName,
                                                               serverGUID,
                                                               connector.getClass().getName());
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
     * Update the connection for a specific integration connector.
     * This update is in memory and will not persist over a server restart.
     *
     * @param serverName integration daemon server name
     * @param serverGUID unique identifier of the server to call
     * @param connectorName name of a specific connector
     * @param requestBody new connection object
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration service.
     */
    public  VoidResponse updateConnectorConnection(String     serverName,
                                                   String     serverGUID,
                                                   String     connectorName,
                                                   Connection requestBody)
    {
        final String methodName = "updateConnectorConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectedAssetClient handler = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);

            Connector connector = handler.getConnectorForAsset(userId, serverGUID, auditLog);

            if (connector instanceof IntegrationDaemonConnector omagServerConnector)
            {
                omagServerConnector.setClientUserId(userId);
                omagServerConnector.start();
                omagServerConnector.updateConnectorConnection(connectorName, requestBody);
                omagServerConnector.disconnect();
            }
            else
            {
                restExceptionHandler.handleInvalidCallToServer(OMAGServerConnectorBase.class.getName(),
                                                               methodName,
                                                               serverGUID,
                                                               connector.getClass().getName());
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
     * Issue a refresh() request on all connectors running in the integration daemon, or a specific connector if the connector name is specified.
     *
     * @param serverName integration daemon server name
     * @param serverGUID unique identifier of the server to call
     * @param requestBody optional name of the connector to target - if no connector name is specified, all
     *                      connectors managed by this integration service are refreshed.
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration daemon.
     */
    public VoidResponse refreshConnectors(String          serverName,
                                          String          serverGUID,
                                          NameRequestBody requestBody)
    {
        final String methodName = "refreshConnectors";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectedAssetClient handler = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);

            Connector connector = handler.getConnectorForAsset(userId, serverGUID, auditLog);

            if (connector instanceof IntegrationDaemonConnector omagServerConnector)
            {
                omagServerConnector.setClientUserId(userId);
                omagServerConnector.start();

                if ((requestBody == null) || (requestBody.getName() == null))
                {
                    omagServerConnector.refreshConnectors();
                }
                else
                {
                    omagServerConnector.refreshConnector(requestBody.getName());
                }

                omagServerConnector.disconnect();
            }
            else
            {
                restExceptionHandler.handleInvalidCallToServer(OMAGServerConnectorBase.class.getName(),
                                                               methodName,
                                                               serverGUID,
                                                               connector.getClass().getName());
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
     * Restart all connectors running in the integration daemon, or restart a specific connector if the connector name is specified.
     *
     * @param serverName integration daemon server name
     * @param serverGUID unique identifier of the server to call
     * @param requestBody optional name of the connector to target - if no connector name is specified, all
     *                      connectors managed by this integration service are refreshed.
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration daemon.
     */
    public VoidResponse restartConnectors(String          serverName,
                                          String          serverGUID,
                                          NameRequestBody requestBody)
    {
        final String methodName = "restartConnectors";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectedAssetClient handler = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);

            Connector connector = handler.getConnectorForAsset(userId, serverGUID, auditLog);

            if (connector instanceof IntegrationDaemonConnector omagServerConnector)
            {
                omagServerConnector.setClientUserId(userId);
                omagServerConnector.start();

                if ((requestBody == null) || (requestBody.getName() == null))
                {
                    omagServerConnector.restartConnectors();
                }
                else
                {
                    omagServerConnector.restartConnector(requestBody.getName());
                }

                omagServerConnector.disconnect();
            }
            else
            {
                restExceptionHandler.handleInvalidCallToServer(OMAGServerConnectorBase.class.getName(),
                                                               methodName,
                                                               serverGUID,
                                                               connector.getClass().getName());
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
     * Request that the integration group refresh its configuration by calling the metadata access server.
     * Changes to the connector configuration will result in the affected connectors being restarted.
     * This request is useful if the metadata access server has an outage, particularly while the
     * integration daemon is initializing.  This request just ensures that the latest configuration
     * is in use.
     *
     * @param serverName name of the governance server
     * @param serverGUID unique identifier of the server to call
     * @param integrationGroupName unique name of the integration group
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  IntegrationGroupException there was a problem detected by the integration group.
     */
    public  VoidResponse refreshIntegrationGroupConfig(String serverName,
                                                       String serverGUID,
                                                       String integrationGroupName)
    {
        final String methodName = "refreshIntegrationGroupConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectedAssetClient handler = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);

            Connector connector = handler.getConnectorForAsset(userId, serverGUID, auditLog);

            if (connector instanceof IntegrationDaemonConnector omagServerConnector)
            {
                omagServerConnector.setClientUserId(userId);
                omagServerConnector.start();

                omagServerConnector.refreshIntegrationGroupConfig(integrationGroupName);

                omagServerConnector.disconnect();
            }
            else
            {
                restExceptionHandler.handleInvalidCallToServer(OMAGServerConnectorBase.class.getName(),
                                                               methodName,
                                                               serverGUID,
                                                               connector.getClass().getName());
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
     * Pass an open lineage event to the integration service.  It will pass it on to the integration connectors that have registered a
     * listener for open lineage events.
     *
     * @param serverName integration daemon server name
     * @param serverGUID unique identifier of the server to call
     * @param event open lineage event to publish.
     */
    public VoidResponse publishOpenLineageEvent(String serverName,
                                                String serverGUID,
                                                String event)
    {
        final String methodName = "publishOpenLineageEvent";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectedAssetClient handler = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);

            Connector connector = handler.getConnectorForAsset(userId, serverGUID, auditLog);

            if (connector instanceof IntegrationDaemonConnector omagServerConnector)
            {
                omagServerConnector.setClientUserId(userId);
                omagServerConnector.start();

                omagServerConnector.publishOpenLineageEvent(event);

                omagServerConnector.disconnect();
            }
            else
            {
                restExceptionHandler.handleInvalidCallToServer(OMAGServerConnectorBase.class.getName(),
                                                               methodName,
                                                               serverGUID,
                                                               connector.getClass().getName());
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /*
     * =============================================================
     * Metadata Access Store
     */


    /**
     * A new server needs to register the metadataCollectionId for its metadata repository with the other servers in the
     * open metadata repository.  It only needs to do this once and uses a timestamp to record that the registration
     * event has been sent.
     * If the server has already registered in the past, it sends a reregistration request.
     *
     * @return boolean to indicate that the request has been issued.  If false it is likely that the cohort name is not known
     * @param serverName server to query
     * @param serverGUID unique identifier of the server to call
     * @param cohortName name of cohort
     * @param requestBody null request body
     */
    @SuppressWarnings(value = "unused")
    public BooleanResponse connectToCohort(String          serverName,
                                           String          serverGUID,
                                           String          cohortName,
                                           NullRequestBody requestBody)
    {
        final String methodName = "connectToCohort";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectedAssetClient handler = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);

            Connector connector = handler.getConnectorForAsset(userId, serverGUID, auditLog);

            if (connector instanceof OMAGServerConnectorBase omagServerConnector)
            {
                omagServerConnector.setClientUserId(userId);
                omagServerConnector.start();

                response.setFlag(omagServerConnector.connectToCohort(userId, cohortName));

                omagServerConnector.disconnect();
            }
            else
            {
                restExceptionHandler.handleInvalidCallToServer(OMAGServerConnectorBase.class.getName(),
                                                               methodName,
                                                               serverGUID,
                                                               connector.getClass().getName());
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
     * Disconnect communications from a specific cohort.
     *
     * @param serverName server to query
     * @param serverGUID unique identifier of the server to call
     * @param cohortName name of cohort
     * @param requestBody null request body
     * @return boolean to indicate that the request has been issued.  If false it is likely that the cohort name is not known
     */
    @SuppressWarnings(value = "unused")
    public BooleanResponse disconnectFromCohort(String          serverName,
                                                String          serverGUID,
                                                String          cohortName,
                                                NullRequestBody requestBody)
    {
        final String methodName = "disconnectFromCohort";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectedAssetClient handler = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);

            Connector connector = handler.getConnectorForAsset(userId, serverGUID, auditLog);

            if (connector instanceof OMAGServerConnectorBase omagServerConnector)
            {
                omagServerConnector.setClientUserId(userId);
                omagServerConnector.start();

                response.setFlag(omagServerConnector.disconnectFromCohort(userId, cohortName));

                omagServerConnector.disconnect();
            }
            else
            {
                restExceptionHandler.handleInvalidCallToServer(OMAGServerConnectorBase.class.getName(),
                                                               methodName,
                                                               serverGUID,
                                                               connector.getClass().getName());
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
     * Unregister from a specific cohort and disconnect from cohort communications.
     *
     * @param serverName server to query
     * @param serverGUID unique identifier of the server to call
     * @param cohortName name of cohort
     * @param requestBody null request body
     * @return boolean to indicate that the request has been issued.  If false it is likely that the cohort name is not known
     */
    @SuppressWarnings(value = "unused")
    public BooleanResponse unregisterFromCohort(String          serverName,
                                                String          serverGUID,
                                                String          cohortName,
                                                NullRequestBody requestBody)
    {
        final String methodName = "unregisterFromCohort";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectedAssetClient handler = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);

            Connector connector = handler.getConnectorForAsset(userId, serverGUID, auditLog);

            if (connector instanceof OMAGServerConnectorBase omagServerConnector)
            {
                omagServerConnector.setClientUserId(userId);
                omagServerConnector.start();

                response.setFlag(omagServerConnector.unregisterFromCohort(userId, cohortName));

                omagServerConnector.disconnect();
            }
            else
            {
                restExceptionHandler.handleInvalidCallToServer(OMAGServerConnectorBase.class.getName(),
                                                               methodName,
                                                               serverGUID,
                                                               connector.getClass().getName());
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
