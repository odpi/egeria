/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.client;

import org.odpi.openmetadata.adminservices.configuration.properties.StewardshipEngineServicesConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerClientConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.rest.StewardshipEngineServicesConfigResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;

import java.util.List;

/**
 * StewardshipServerConfigurationClient provides configuration services for a stewardship server.  The stewardship server hosts
 * one or more stewardship engines as defined in the Open Stewardship Services (ODF).
 */
public class StewardshipServerConfigurationClient extends EngineHostConfigurationClient
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
    public StewardshipServerConfigurationClient(String adminUserId,
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
    public StewardshipServerConfigurationClient(String adminUserId,
                                                String serverName,
                                                String serverPlatformRootURL,
                                                String connectionUserId,
                                                String connectionPassword) throws OMAGInvalidParameterException
    {
        super(adminUserId, serverName, serverPlatformRootURL, connectionUserId, connectionPassword);
    }


    /**
     * Set up the name and platform URL root for the metadata server supporting this stewardship server.
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
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/stewardship-engine-services/client-config";

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
     * Set up the list of stewardship engines that are to run in the stewardship service.
     * The definition of these stewardship engines
     *
     * @param stewardshipEngines  stewardshipEngines for server.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setStewardshipEngines(List<String> stewardshipEngines) throws OMAGNotAuthorizedException,
                                                                              OMAGInvalidParameterException,
                                                                              OMAGConfigurationErrorException
    {
        final String methodName    = "setStewardshipEngines";
        final String parameterName = "stewardshipEngines";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/stewardship-engine-services/stewardship-engines";

        try
        {
            invalidParameterHandler.validateObject(stewardshipEngines, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        stewardshipEngines,
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
    public void setStewardshipEngineServicesConfig(StewardshipEngineServicesConfig servicesConfig) throws OMAGNotAuthorizedException,
                                                                                                          OMAGInvalidParameterException,
                                                                                                          OMAGConfigurationErrorException
    {
        final String methodName    = "setStewardshipEngineServicesConfig";
        final String parameterName = "servicesConfig";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/stewardship-engine-services";

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
     * Get the configuration for the stewardship engine services for this server.
     *
     * @return full configuration for the service.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public StewardshipEngineServicesConfig getStewardshipEngineServicesConfig() throws OMAGNotAuthorizedException,
                                                                                   OMAGInvalidParameterException,
                                                                                   OMAGConfigurationErrorException
    {
        final String methodName  = "getStewardshipEngineServicesConfig";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/stewardship-engine-services";

        StewardshipEngineServicesConfigResponse response = restClient.callStewardshipServerConfigGetRESTCall(methodName,
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
    public void clearStewardshipEngineServicesConfig() throws OMAGNotAuthorizedException,
                                                            OMAGInvalidParameterException,
                                                            OMAGConfigurationErrorException
    {
        final String methodName  = "clearStewardshipEngineServicesConfig";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/stewardship-engine-services";

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          adminUserId,
                                          serverName);
    }
}
