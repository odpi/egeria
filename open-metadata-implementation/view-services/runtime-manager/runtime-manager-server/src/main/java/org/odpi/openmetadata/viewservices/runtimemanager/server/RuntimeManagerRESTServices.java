/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.runtimemanager.server;


import org.odpi.openmetadata.accessservices.itinfrastructure.client.ConnectedAssetClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.PlatformManagerClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.ServerManagerClient;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.SoftwareServerElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.SoftwareServerPlatformElement;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.OMAGServerPlatformConnector;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.MetadataAccessServerConnector;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.OMAGServerConnectorBase;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.serveroperations.rest.SuccessMessageResponse;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.odpi.openmetadata.viewservices.runtimemanager.rest.EffectiveTimeQueryRequestBody;
import org.odpi.openmetadata.viewservices.runtimemanager.rest.PlatformReportResponse;
import org.odpi.openmetadata.viewservices.runtimemanager.rest.ServerReportResponse;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
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
     * @param startFrom          index of the list to start from (0 for start)
     * @param pageSize           maximum number of elements to return
     * @param requestBody qualified name or display name of the platform
     *
     * @return a list of projects
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public SoftwareServerPlatformsResponse getPlatformsByName(String            serverName,
                                                              int               startFrom,
                                                              int               pageSize,
                                                              FilterRequestBody requestBody)
    {
        final String methodName = "getPlatformsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SoftwareServerPlatformsResponse response = new SoftwareServerPlatformsResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            PlatformManagerClient handler = instanceHandler.getPlatformManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getSoftwareServerPlatformsByName(userId, requestBody.getFilter(), requestBody.getEffectiveTime(), startFrom, pageSize));
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
     * Returns the list of platforms with a particular deployed implementation type.
     *
     * @param serverName         name of called server
     * @param startFrom          index of the list to start from (0 for start)
     * @param pageSize           maximum number of elements to return
     * @param getTemplates boolean indicating whether templates or non-template platforms should be returned.
     * @param requestBody qualified name or display name of the platform
     *
     * @return a list of projects
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public SoftwareServerPlatformsResponse getPlatformsByDeployedImplType(String            serverName,
                                                                          int               startFrom,
                                                                          int               pageSize,
                                                                          boolean           getTemplates,
                                                                          FilterRequestBody requestBody)
    {
        final String methodName = "getPlatformsByDeployedImplType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SoftwareServerPlatformsResponse response = new SoftwareServerPlatformsResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            PlatformManagerClient handler = instanceHandler.getPlatformManagerClient(userId, serverName, methodName);

            List<SoftwareServerPlatformElement> platforms;
            if (requestBody != null)
            {
                platforms = handler.getSoftwareServerPlatformsByDeployedImplType(userId, requestBody.getFilter(), requestBody.getEffectiveTime(), startFrom, pageSize);
            }
            else
            {
                platforms = handler.getSoftwareServerPlatformsByDeployedImplType(userId, null, new Date(), startFrom, pageSize);
            }

            if (platforms != null)
            {
                List<SoftwareServerPlatformElement> filteredPlatforms = new ArrayList<>();

                for (SoftwareServerPlatformElement platformElement : platforms)
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
        if (elementHeader.getClassifications() != null)
        {
            for (ElementClassification classification : elementHeader.getClassifications())
            {
                if (classification != null)
                {
                    if (classification.getClassificationName().equals(OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName))
                    {
                        return true;
                    }
                }
            }
        }

        return false;
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
    @SuppressWarnings(value = "unused")
    public SoftwareServerPlatformResponse getPlatformByGUID(String                        serverName,
                                                            String                        platformGUID,
                                                            EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getPlatformByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SoftwareServerPlatformResponse response = new SoftwareServerPlatformResponse();
        AuditLog                       auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            PlatformManagerClient handler = instanceHandler.getPlatformManagerClient(userId, serverName, methodName);

            response.setElement(handler.getSoftwareServerPlatformByGUID(userId, platformGUID));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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

            ConnectedAssetClient handler = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);

            Connector connector = handler.getConnectorForAsset(userId, platformGUID);

            if (connector instanceof OMAGServerPlatformConnector omagServerPlatformConnector)
            {
                omagServerPlatformConnector.start();
                response.setElement(omagServerPlatformConnector.getPlatformReport());
                omagServerPlatformConnector.disconnect();
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
     * Returns the list of platforms with a particular qualifiedName or name.
     *
     * @param serverName         name of called server
     * @param startFrom          index of the list to start from (0 for start)
     * @param pageSize           maximum number of elements to return
     * @param requestBody qualified name or display name of the platform
     *
     * @return a list of projects
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public SoftwareServersResponse getServersByName(String            serverName,
                                                    int               startFrom,
                                                    int               pageSize,
                                                    FilterRequestBody requestBody)
    {
        final String methodName = "getServersByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SoftwareServersResponse response = new SoftwareServersResponse();
        AuditLog                auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ServerManagerClient handler = instanceHandler.getServerManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getSoftwareServersByName(userId, requestBody.getFilter(), requestBody.getEffectiveTime(), startFrom, pageSize));
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
     * Returns the list of servers with a particular deployed implementation type.
     *
     * @param serverName         name of called server
     * @param startFrom          index of the list to start from (0 for start)
     * @param pageSize           maximum number of elements to return
     * @param getTemplates boolean indicating whether templates or non-template platforms should be returned.
     * @param requestBody qualified name or display name of the platform
     *
     * @return a list of projects
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public SoftwareServersResponse getServersByDeployedImplType(String            serverName,
                                                                int               startFrom,
                                                                int               pageSize,
                                                                boolean           getTemplates,
                                                                FilterRequestBody requestBody)
    {
        final String methodName = "getServersByDeployedImplType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SoftwareServersResponse response = new SoftwareServersResponse();
        AuditLog                auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ServerManagerClient handler = instanceHandler.getServerManagerClient(userId, serverName, methodName);

            List<SoftwareServerElement> servers;
            if (requestBody != null)
            {
                servers = handler.getSoftwareServersByDeployedImplType(userId, requestBody.getFilter(), requestBody.getEffectiveTime(), startFrom, pageSize);
            }
            else
            {
                servers = handler.getSoftwareServersByDeployedImplType(userId, null, new Date(), startFrom, pageSize);
            }

            if (servers != null)
            {
                List<SoftwareServerElement> filteredServers = new ArrayList<>();

                for (SoftwareServerElement serverElement : servers)
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
    @SuppressWarnings(value = "unused")

    public SoftwareServerResponse getServerByGUID(String                        serverName,
                                                  String                        serverGUID,
                                                  EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getServerByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SoftwareServerResponse response = new SoftwareServerResponse();
        AuditLog               auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ServerManagerClient handler = instanceHandler.getServerManagerClient(userId, serverName, methodName);

            response.setElement(handler.getSoftwareServerByGUID(userId, serverGUID));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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

            Connector connector = handler.getConnectorForAsset(userId, serverGUID);

            if (connector instanceof OMAGServerConnectorBase omagServerConnector)
            {
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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

            Connector connector = handler.getConnectorForAsset(userId, serverGUID);

            if (connector instanceof OMAGServerConnectorBase omagServerConnector)
            {
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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

            Connector connector = handler.getConnectorForAsset(userId, serverGUID);

            if (connector instanceof OMAGServerConnectorBase omagServerConnector)
            {
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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

            Connector connector = handler.getConnectorForAsset(userId, serverGUID);

            if (connector instanceof OMAGServerConnectorBase omagServerConnector)
            {
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /*
     * =============================================================
     * Services on running instances
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

            Connector connector = handler.getConnectorForAsset(userId, serverGUID);

            if (connector instanceof MetadataAccessServerConnector omagServerConnector)
            {
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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

            Connector connector = handler.getConnectorForAsset(userId, serverGUID);

            if (connector instanceof MetadataAccessServerConnector omagServerConnector)
            {
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
