/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.client;

import org.odpi.openmetadata.adminservices.configuration.properties.DiscoveryEngineServicesConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerClientConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.rest.DiscoveryEngineServicesConfigResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;

import java.util.List;

/**
 * DiscoveryServerConfigurationClient provides configuration services for a discovery server.  The discovery server hosts
 * one or more discovery engines as defined in the Open Discovery Services (ODF).
 */
public class DiscoveryServerConfigurationClient extends EngineHostConfigurationClient
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param adminUserId           administrator's (end user's) userId to associate with calls.
     * @param serverName            name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @throws OMAGInvalidParameterException there is a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public DiscoveryServerConfigurationClient(String adminUserId,
                                              String serverName,
                                              String serverPlatformRootURL) throws OMAGInvalidParameterException
    {
        super(adminUserId, serverName, serverPlatformRootURL);
    }


    /**
     * Create a new client that passes a connection userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is passed as the admin userId.
     *
     * @param adminUserId           administrator's (end user's) userId to associate with calls.
     * @param serverName            name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @param connectionUserId      caller's system userId embedded in all HTTP requests
     * @param connectionPassword    caller's system password embedded in all HTTP requests
     * @throws OMAGInvalidParameterException there is a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public DiscoveryServerConfigurationClient(String adminUserId,
                                              String serverName,
                                              String serverPlatformRootURL,
                                              String connectionUserId,
                                              String connectionPassword) throws OMAGInvalidParameterException
    {
        super(adminUserId, serverName, serverPlatformRootURL, connectionUserId, connectionPassword);
    }


    /**
     * Set up the name and platform URL root for the metadata server supporting this discovery server.
     *
     * @param clientConfig  URL root and server name for the metadata server.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */

    public void setClientConfig(OMAGServerClientConfig clientConfig) throws OMAGNotAuthorizedException,
                                                                            OMAGInvalidParameterException,
                                                                            OMAGConfigurationErrorException
    {
        final String methodName  = "setClientConfig";
        final String configParameterName = "clientConfig";
        final String urlParameterName = "clientConfig.serverPlatformURLRoot";
        final String serverNameParameterName = "clientConfig.serverName";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/discovery-engine-services/client-config";

        try
        {
            invalidParameterHandler.validateObject(clientConfig, configParameterName, methodName);
            invalidParameterHandler.validateName(clientConfig.getOMAGServerPlatformRootURL(), urlParameterName, methodName);
            invalidParameterHandler.validateName(clientConfig.getOMAGServerName(), serverNameParameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        clientConfig,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Set up the list of discovery engines that are to run in the discovery service.
     * The definition of these discovery engines
     *
     * @param discoveryEngines  discoveryEngines for server.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setDiscoveryEngines(List<String> discoveryEngines) throws OMAGNotAuthorizedException,
                                                                          OMAGInvalidParameterException,
                                                                          OMAGConfigurationErrorException
    {
        final String methodName    = "setDiscoveryEngines";
        final String parameterName = "discoveryEngines";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/discovery-engine-services/discovery-engines";

        try
        {
            invalidParameterHandler.validateObject(discoveryEngines, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        discoveryEngines,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Add this service to the server configuration in one call.
     *
     * @param servicesConfig full configuration for the service.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setDiscoveryEngineServicesConfig(DiscoveryEngineServicesConfig servicesConfig) throws OMAGNotAuthorizedException,
                                                                                                      OMAGInvalidParameterException,
                                                                                                      OMAGConfigurationErrorException
    {
        final String methodName    = "setDiscoveryEngineServicesConfig";
        final String parameterName = "servicesConfig";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/discovery-engine-services";

        try
        {
            invalidParameterHandler.validateObject(servicesConfig, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        servicesConfig,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Get the configuration for the discovery engine services for this server.
     *
     * @return full configuration for the service.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public DiscoveryEngineServicesConfig getDiscoveryEngineServicesConfig() throws OMAGNotAuthorizedException,
                                                                                   OMAGInvalidParameterException,
                                                                                   OMAGConfigurationErrorException
    {
        final String methodName  = "getDiscoveryEngineServicesConfig";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/discovery-engine-services";

        DiscoveryEngineServicesConfigResponse response = restClient.callDiscoveryServerConfigGetRESTCall(methodName,
                                                                                                         serverPlatformRootURL + urlTemplate,
                                                                                                         adminUserId,
                                                                                                         serverName);
        if (response != null)
        {
            return response.getConfig();
        }

        return null;
    }


    /**
     * Remove this service from the server configuration.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearDiscoveryEngineServicesConfig() throws OMAGNotAuthorizedException,
                                                            OMAGInvalidParameterException,
                                                            OMAGConfigurationErrorException
    {
        final String methodName  = "clearDiscoveryEngineServicesConfig";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/discovery-engine-services";

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          adminUserId,
                                          serverName);
    }
}
