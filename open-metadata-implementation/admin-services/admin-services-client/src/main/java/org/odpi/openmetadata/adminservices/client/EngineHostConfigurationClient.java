/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.client;

import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.rest.EngineHostServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGServicesResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * EngineHostConfigurationClient provides the configuration services for Engine Host OMAG Servers.
 * This involves creating a list of engine services.
 */
public class EngineHostConfigurationClient extends GovernanceServerConfigurationClient
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @param secretStoreProvider class name of the secrets store
     * @param secretStoreLocation location (networkAddress) of the secrets store
     * @param secretStoreCollection name of the collection of secrets to use to connect to the remote server
     * @param auditLog destination for log messages.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public EngineHostConfigurationClient(String   serverName,
                                         String   serverPlatformRootURL,
                                         String   secretStoreProvider,
                                         String   secretStoreLocation,
                                         String   secretStoreCollection,
                                         AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformRootURL, secretStoreProvider, secretStoreLocation, secretStoreCollection, auditLog);
    }

    /*
     * =============================================================
     * Learn about all possible engine services
     */

    /**
     * Return the list of engine services for this server.
     *
     * @return list of engine service descriptions
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public List<RegisteredOMAGService> getRegisteredEngineServices() throws UserNotAuthorizedException,
                                                                            InvalidParameterException,
                                                                            OMAGConfigurationErrorException
    {
        final String methodName  = "getRegisteredEngineServices";
        final String urlTemplate = "/open-metadata/platform-services/server-platform/registered-services/engine-services";

        RegisteredOMAGServicesResponse restResult = restClient.callRegisteredOMAGServicesGetRESTCall(methodName,
                                                                                                     serverPlatformRootURL + urlTemplate);
        return restResult.getServices();
    }


    /*
     * =============================================================
     * Retrieve the current state of the engine service configuration
     */


    /**
     * Return the configuration for the complete engine host services in this server.
     *
     * @return response containing the engine host services configuration
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public List<EngineConfig> getEngineHostServicesConfiguration() throws UserNotAuthorizedException,
                                                                                InvalidParameterException,
                                                                                OMAGConfigurationErrorException
    {
        final String methodName  = "getEngineHostServicesConfiguration";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/engine-host-services/configuration";

        EngineHostServicesResponse restResult = restClient.callEngineHostServicesGetRESTCall(methodName,
                                                                                             serverPlatformRootURL + urlTemplate,
                                                                                             serverName);
        return restResult.getGovernanceEngines();
    }


    /*
     * =============================================================
     * Configure server making maximum use of defaults
     */


    /**
     * Set up the configuration for the Engine Host Services in an Engine Host OMAG Server in a single call.  This overrides the current values.
     *
     * @param engineHostServicesConfig  governance engine definition client config and list of configuration properties for each engine service.
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setEngineHostServicesConfig(List<EngineConfig> engineHostServicesConfig) throws UserNotAuthorizedException,
                                                                                                InvalidParameterException,
                                                                                                OMAGConfigurationErrorException
    {
        final String methodName    = "setEngineHostServicesConfig";
        final String configName    = "engineHostServicesConfig";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/engine-host-services";

        invalidParameterHandler.validateObject(engineHostServicesConfig, configName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        engineHostServicesConfig,
                                        serverName);
    }


    /**
     * Clear the configuration for the engine host services.
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearEngineHostServices() throws UserNotAuthorizedException,
                                                 InvalidParameterException,
                                                 OMAGConfigurationErrorException
    {
        final String methodName  = "clearEngineHostServices";
        final String urlTemplate = "open-metadata/admin-services/servers/{0}/engine-host-services";

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          serverName);
    }
}
