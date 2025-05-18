/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.client;

import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineHostServicesConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerClientConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.rest.EngineServiceConfigResponse;
import org.odpi.openmetadata.adminservices.rest.EngineServiceRequestBody;
import org.odpi.openmetadata.adminservices.rest.EngineHostServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGServicesResponse;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;

import java.util.List;
import java.util.Map;

/**
 * EngineHostConfigurationClient provides the configuration services for Engine Host OMAG Servers.
 * This involves creating a list of engine services.
 */
public class EngineHostConfigurationClient extends GovernanceServerConfigurationClient
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
    public EngineHostConfigurationClient(String adminUserId,
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
    public EngineHostConfigurationClient(String adminUserId,
                                         String serverName,
                                         String serverPlatformRootURL,
                                         String connectionUserId,
                                         String connectionPassword) throws OMAGInvalidParameterException
    {
        super(adminUserId, serverName, serverPlatformRootURL, connectionUserId, connectionPassword);
    }


    /*
     * =============================================================
     * Learn about all possible engine services
     */

    /**
     * Return the list of engine services for this server.
     *
     * @return list of engine service descriptions
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public List<RegisteredOMAGService> getRegisteredEngineServices() throws OMAGNotAuthorizedException,
                                                                            OMAGInvalidParameterException,
                                                                            OMAGConfigurationErrorException
    {
        final String methodName  = "getRegisteredEngineServices";
        final String urlTemplate = "/open-metadata/platform-services/users/{0}/server-platform/registered-services/engine-services";

        RegisteredOMAGServicesResponse restResult = restClient.callRegisteredOMAGServicesGetRESTCall(methodName,
                                                                                                     serverPlatformRootURL + urlTemplate,
                                                                                                     adminUserId);
        return restResult.getServices();
    }


    /*
     * =============================================================
     * Retrieve the current state of the engine service configuration
     */


    /**
     * Return the list of engine services for this server.
     *
     * @return list of engine service descriptions
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public List<RegisteredOMAGService> getConfiguredEngineServices() throws OMAGNotAuthorizedException,
                                                                            OMAGInvalidParameterException,
                                                                            OMAGConfigurationErrorException
    {
        final String methodName  = "getConfiguredEngineServices";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/engine-services";

        RegisteredOMAGServicesResponse restResult = restClient.callRegisteredOMAGServicesGetRESTCall(methodName,
                                                                                                     serverPlatformRootURL + urlTemplate,
                                                                                                     adminUserId,
                                                                                                     serverName);
        return restResult.getServices();
    }


    /**
     * Return the configuration for the complete engine host services in this server.
     *
     * @return response containing the engine host services configuration
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public EngineHostServicesConfig getEngineHostServicesConfiguration() throws OMAGNotAuthorizedException,
                                                                                OMAGInvalidParameterException,
                                                                                OMAGConfigurationErrorException
    {
        final String methodName  = "getEngineHostServicesConfiguration";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/engine-host-services/configuration";

        EngineHostServicesResponse restResult = restClient.callEngineHostServicesGetRESTCall(methodName,
                                                                                             serverPlatformRootURL + urlTemplate,
                                                                                             adminUserId,
                                                                                             serverName);
        return restResult.getServices();
    }


    /**
     * Return the configuration for the named engine service for this server.
     *
     * @param serviceURLMarker engine service name used in URL
     * @return response containing the engine services configuration
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public EngineServiceConfig getEngineServiceConfiguration(String serviceURLMarker) throws OMAGNotAuthorizedException,
                                                                                             OMAGInvalidParameterException,
                                                                                             OMAGConfigurationErrorException
    {
        final String methodName  = "getEngineServiceConfiguration";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/engine-services/{2}/configuration";

        EngineServiceConfigResponse restResult = restClient.callEngineServiceConfigGetRESTCall(methodName,
                                                                                               serverPlatformRootURL + urlTemplate,
                                                                                               adminUserId,
                                                                                               serverName,
                                                                                               serviceURLMarker);
        return restResult.getConfig();
    }


    /*
     * =============================================================
     * Configure server making maximum use of defaults
     */

    /**
     * Set up the name and platform URL root for the metadata server running the Governance Engine OMAS that provides
     * the governance engine definitions used by the engine services.
     *
     * @param clientConfig  URL root and server name for the metadata server.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */

    public void setEngineDefinitionsClientConfig(OMAGServerClientConfig clientConfig) throws OMAGNotAuthorizedException,
                                                                                             OMAGInvalidParameterException,
                                                                                             OMAGConfigurationErrorException
    {
        final String methodName  = "setEngineDefinitionsClientConfig";
        final String configParameterName = "clientConfig";
        final String urlParameterName = "clientConfig.serverPlatformURLRoot";
        final String serverNameParameterName = "clientConfig.serverName";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/engine-definitions/client-config";

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
     * Enable a single engine service.
     *
     * @param partnerOMASServerURLRoot URL root of the OMAG Server Platform where the access service used by this view service is running
     * @param partnerOMASServerName name of metadata access server where the access service used by this view service is running
     * @param serviceURLMarker string indicating which engine service it is configuring
     * @param engineServiceOptions property name/value pairs used to configure the engine service
     * @param engines list of qualified names of the engines and optional user information
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void configureEngineService(String              partnerOMASServerURLRoot,
                                       String              partnerOMASServerName,
                                       String              serviceURLMarker,
                                       Map<String, Object> engineServiceOptions,
                                       List<EngineConfig>  engines) throws OMAGNotAuthorizedException,
                                                                           OMAGInvalidParameterException,
                                                                           OMAGConfigurationErrorException
    {
        final String methodName    = "configureEngineService";
        final String parameterName = "serviceURLMarker";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/engine-services/{2}";

        try
        {
            invalidParameterHandler.validateName(serviceURLMarker, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        EngineServiceRequestBody requestBody = new EngineServiceRequestBody();

        requestBody.setOMAGServerPlatformRootURL(partnerOMASServerURLRoot);
        requestBody.setOMAGServerName(partnerOMASServerName);
        requestBody.setEngineServiceOptions(engineServiceOptions);
        requestBody.setEngines(engines);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        engineServiceOptions,
                                        adminUserId,
                                        serverName,
                                        serviceURLMarker);
    }

    /**
     * Disable a single engine service.
     *
     * @param serviceURLMarker string indicating which engine service it is disabling
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void disableEngineService(String serviceURLMarker) throws OMAGNotAuthorizedException,
                                                                     OMAGInvalidParameterException,
                                                                     OMAGConfigurationErrorException
    {
        final String methodName    = "disableEngineService";
        final String parameterName = "serviceURLMarker";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/engine-services/{2}";

        try
        {
            invalidParameterHandler.validateName(serviceURLMarker, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidDeleteRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        adminUserId,
                                        serverName,
                                        serviceURLMarker);
    }


    /**
     * Add configuration for a single engine service to the server's config document.
     *
     * @param serviceConfig  all values to configure an engine service
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void configureEngineService(EngineServiceConfig serviceConfig) throws OMAGNotAuthorizedException,
                                                                                 OMAGInvalidParameterException,
                                                                                 OMAGConfigurationErrorException
    {
        final String methodName    = "configureEngineService";
        final String configName    = "serviceConfig";
        final String parameterName = "serviceURLMarker";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/engine-services/configuration";

        try
        {
            invalidParameterHandler.validateObject(serviceConfig, configName, methodName);
            invalidParameterHandler.validateName(serviceConfig.getEngineServiceURLMarker(), parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        serviceConfig,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Set up the configuration for all the open metadata engine services (OMESs).  This overrides
     * the current values.
     *
     * @param engineServicesConfig  list of configuration properties for each engine service.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setEngineServicesConfig(List<EngineServiceConfig> engineServicesConfig) throws OMAGNotAuthorizedException,
                                                                                               OMAGInvalidParameterException,
                                                                                               OMAGConfigurationErrorException
    {
        final String methodName    = "setEngineHostServicesConfig";
        final String configName    = "engineServicesConfig";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/engine-services/configuration/all";

        try
        {
            invalidParameterHandler.validateObject(engineServicesConfig, configName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        engineServicesConfig,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Set up the configuration for the Engine Host Services in an Engine Host OMAG Server in a single call.  This overrides the current values.
     *
     * @param engineHostServicesConfig  governance engine definition client config and list of configuration properties for each engine service.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setEngineHostServicesConfig(EngineHostServicesConfig engineHostServicesConfig) throws OMAGNotAuthorizedException,
                                                                                                      OMAGInvalidParameterException,
                                                                                                      OMAGConfigurationErrorException
    {
        final String methodName    = "setEngineHostServicesConfig";
        final String configName    = "engineHostServicesConfig";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/engine-host-services";

        try
        {
            invalidParameterHandler.validateObject(engineHostServicesConfig, configName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        engineHostServicesConfig,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Clear the configuration for the engine host services.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearEngineHostServices() throws OMAGNotAuthorizedException,
                                                 OMAGInvalidParameterException,
                                                 OMAGConfigurationErrorException
    {
        final String methodName  = "clearEngineHostServices";
        final String urlTemplate = "open-metadata/admin-services/users/{0}/servers/{1}/engine-host-services";

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          adminUserId,
                                          serverName);
    }


    /**
     * Clear the configuration for the metadata access server that provides the governance engine definitions through the
     * Governance Engine OMAS.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearEngineDefinitionsClientConfig() throws OMAGNotAuthorizedException,
                                                            OMAGInvalidParameterException,
                                                            OMAGConfigurationErrorException
    {
        final String methodName  = "clearEngineDefinitionsClientConfig";
        final String urlTemplate = "open-metadata/admin-services/users/{0}/servers/{1}/engine-definitions/client-config";

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          adminUserId,
                                          serverName);
    }


    /**
     * Disable the engine services.  This removes all configuration for the engine services in the engine host server.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearAllEngineServices() throws OMAGNotAuthorizedException,
                                                OMAGInvalidParameterException,
                                                OMAGConfigurationErrorException
    {
        final String methodName  = "clearAllEngineServices";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/engine-services";

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          adminUserId,
                                          serverName);
    }


    /**
     * Disable the engine services.  This removes all configuration for the engine services from the engine host server.
     *
     * @param serviceURLMarker engine service name used in URL
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearEngineService(String serviceURLMarker) throws OMAGNotAuthorizedException,
                                                                   OMAGInvalidParameterException,
                                                                   OMAGConfigurationErrorException
    {
        final String methodName    = "clearEngineService";
        final String parameterName = "serviceURLMarker";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/engine-services/{2}";

        try
        {
            invalidParameterHandler.validateName(serviceURLMarker, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          adminUserId,
                                          serverName);
    }
}
