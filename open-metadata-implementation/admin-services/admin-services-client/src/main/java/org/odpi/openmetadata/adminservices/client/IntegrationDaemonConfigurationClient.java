/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.client;

import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationConnectorConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationServiceConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.rest.IntegrationServiceConfigResponse;
import org.odpi.openmetadata.adminservices.rest.IntegrationServiceRequestBody;
import org.odpi.openmetadata.adminservices.rest.IntegrationServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGServicesResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;

import java.util.List;
import java.util.Map;

/**
 * IntegrationDaemonConfigurationClient provides the configuration services for integration daemons.
 * This involves creating a list of integration services.
 */
public class IntegrationDaemonConfigurationClient extends GovernanceServerConfigurationClient
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
    public IntegrationDaemonConfigurationClient(String adminUserId,
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
    public IntegrationDaemonConfigurationClient(String adminUserId,
                                                String serverName,
                                                String serverPlatformRootURL,
                                                String connectionUserId,
                                                String connectionPassword) throws OMAGInvalidParameterException
    {
        super(adminUserId, serverName, serverPlatformRootURL, connectionUserId, connectionPassword);
    }


    /*
     * =============================================================
     * Learn about all possible integration services
     */

    /**
     * Return the list of integration services for this server.
     *
     * @return list of integration service descriptions
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public List<RegisteredOMAGService> getRegisteredIntegrationServices() throws OMAGNotAuthorizedException,
                                                                                 OMAGInvalidParameterException,
                                                                                 OMAGConfigurationErrorException
    {
        final String methodName  = "getRegisteredIntegrationServices";
        final String urlTemplate = "/open-metadata/platform-services/users/{0}/server-platform/registered-services/integration-services";

        RegisteredOMAGServicesResponse restResult = restClient.callRegisteredOMAGServicesGetRESTCall(methodName,
                                                                                                     serverPlatformRootURL + urlTemplate,
                                                                                                     adminUserId);
        return restResult.getServices();
    }


    /*
     * =============================================================
     * Retrieve the current state of the integration service configuration
     */


    /**
     * Return the list of integration services for this server.
     *
     * @return list of integration service descriptions
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public List<RegisteredOMAGService> getConfiguredIntegrationServices() throws OMAGNotAuthorizedException,
                                                                                 OMAGInvalidParameterException,
                                                                                 OMAGConfigurationErrorException
    {
        final String methodName  = "getConfiguredIntegrationServices";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/integration-services";

        RegisteredOMAGServicesResponse restResult = restClient.callRegisteredOMAGServicesGetRESTCall(methodName,
                                                                                                     serverPlatformRootURL + urlTemplate,
                                                                                                     adminUserId,
                                                                                                     serverName);
        return restResult.getServices();
    }


    /**
     * Return the configuration for the integration services in this server.
     *
     * @return list of integration service configuration
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public List<IntegrationServiceConfig> getIntegrationServicesConfiguration() throws OMAGNotAuthorizedException,
                                                                                       OMAGInvalidParameterException,
                                                                                       OMAGConfigurationErrorException
    {
        final String methodName  = "getIntegrationServicesConfiguration";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/integration-services/configuration";

        IntegrationServicesResponse restResult = restClient.callIntegrationServicesGetRESTCall(methodName,
                                                                                               serverPlatformRootURL + urlTemplate,
                                                                                               adminUserId,
                                                                                               serverName);
        return restResult.getServices();
    }


    /**
     * Return the configuration for the named integration service for this server.
     *
     * @param serviceURLMarker integration service name used in URL
     * @return response containing the integration services configuration
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public IntegrationServiceConfig getIntegrationServiceConfiguration(String serviceURLMarker) throws OMAGNotAuthorizedException,
                                                                                                       OMAGInvalidParameterException,
                                                                                                       OMAGConfigurationErrorException
    {
        final String methodName  = "getIntegrationServiceConfiguration";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/integration-services/{2}/configuration";

        IntegrationServiceConfigResponse restResult = restClient.callIntegrationServiceConfigGetRESTCall(methodName,
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
     * Enable a single integration service.
     *
     * @param partnerOMASServerURLRoot URL root of the OMAG Server Platform where the access service used by this integration service is running
     * @param partnerOMASServerName name of server where the access service used by this integration service is running
     * @param serviceURLMarker string indicating which integration service it is configuring
     * @param integrationServiceOptions property name/value pairs used to configure the integration service
     * @param integrationConnectorConfigs Connection properties
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void configureIntegrationService(String                           partnerOMASServerURLRoot,
                                            String                           partnerOMASServerName,
                                            String                           serviceURLMarker,
                                            Map<String, Object>              integrationServiceOptions,
                                            List<IntegrationConnectorConfig> integrationConnectorConfigs) throws OMAGNotAuthorizedException,
                                                                                                                 OMAGInvalidParameterException,
                                                                                                                 OMAGConfigurationErrorException
    {
        final String methodName    = "configureIntegrationService";
        final String parameterName = "serviceURLMarker";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/integration-services/{2}";

        try
        {
            invalidParameterHandler.validateName(serviceURLMarker, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        IntegrationServiceRequestBody requestBody = new IntegrationServiceRequestBody();

        requestBody.setOMAGServerPlatformRootURL(partnerOMASServerURLRoot);
        requestBody.setOMAGServerName(partnerOMASServerName);
        requestBody.setIntegrationServiceOptions(integrationServiceOptions);
        requestBody.setIntegrationConnectorConfigs(integrationConnectorConfigs);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        adminUserId,
                                        serverName,
                                        serviceURLMarker);
    }

    /**
     * Disable a single integration service.
     *
     * @param serviceURLMarker string indicating which integration service it is configuring
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void disableIntegrationService(String serviceURLMarker) throws OMAGNotAuthorizedException,
                                                                          OMAGInvalidParameterException,
                                                                          OMAGConfigurationErrorException
    {
        final String methodName    = "disableIntegrationService";
        final String parameterName = "serviceURLMarker";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/integration-services/{2}";

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
     * Add configuration for a single integration service to the server's config document.
     *
     * @param serviceConfig  all values to configure an integration service
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void configureIntegrationService(IntegrationServiceConfig serviceConfig) throws OMAGNotAuthorizedException,
                                                                                           OMAGInvalidParameterException,
                                                                                           OMAGConfigurationErrorException
    {
        final String methodName    = "configureIntegrationService";
        final String configName    = "serviceConfig";
        final String parameterName = "serviceURLMarker";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/integration-services/configuration";

        try
        {
            invalidParameterHandler.validateObject(serviceConfig, configName, methodName);
            invalidParameterHandler.validateName(serviceConfig.getIntegrationServiceURLMarker(), parameterName, methodName);
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
     * Set up the configuration for all the open metadata integration services (OMISs).  This overrides
     * the current values.
     *
     * @param integrationServicesConfig  list of configuration properties for each integration service.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setIntegrationServicesConfig(List<IntegrationServiceConfig> integrationServicesConfig) throws OMAGNotAuthorizedException,
                                                                                                              OMAGInvalidParameterException,
                                                                                                              OMAGConfigurationErrorException
    {
        final String methodName    = "setIntegrationServicesConfig";
        final String configName    = "integrationServicesConfig";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/integration-services/configuration/all";

        try
        {
            invalidParameterHandler.validateObject(integrationServicesConfig, configName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        integrationServicesConfig,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Disable the integration services.  This removes all configuration for the integration daemon.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearAllIntegrationServices() throws OMAGNotAuthorizedException,
                                                     OMAGInvalidParameterException,
                                                     OMAGConfigurationErrorException
    {
        final String methodName  = "clearAllIntegrationServices";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/integration-services";

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          adminUserId,
                                          serverName);
    }


    /**
     * Disable the integration services.  This removes all configuration for the integration daemon.
     *
     * @param serviceURLMarker integration service name used in URL
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearIntegrationService(String serviceURLMarker) throws OMAGNotAuthorizedException,
                                                                        OMAGInvalidParameterException,
                                                                        OMAGConfigurationErrorException
    {
        final String methodName    = "clearIntegrationService";
        final String parameterName = "serviceURLMarker";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/integration-services/{2}";

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
