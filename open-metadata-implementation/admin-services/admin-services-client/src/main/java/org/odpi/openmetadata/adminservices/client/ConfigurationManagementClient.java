/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.client;

import org.odpi.openmetadata.adminservices.client.rest.AdminServicesRESTClient;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.rest.OMAGServerConfigResponse;
import org.odpi.openmetadata.adminservices.rest.OMAGServerConfigsResponse;
import org.odpi.openmetadata.adminservices.rest.URLRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.Map;
import java.util.Set;


/**
 * ConfigurationManagementClient is able to issue calls to an OMAG Server Platform that manages configuration
 * for OMAG Servers.  This includes storing and retrieving configuration and deploying it between platforms.
 */
public class ConfigurationManagementClient
{
    private final String serverPlatformRootURL;    /* Initialized in constructor */
    private final String delegatingUserId;         /* Initialized in the constructor */

    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private final AdminServicesRESTClient restClient;               /* Initialized in constructor */

    private static final String NULL_SERVER_NAME = "<*>";


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @param secretStoreProvider class name of the secrets store
     * @param secretStoreLocation location (networkAddress) of the secrets store
     * @param secretStoreCollection name of the collection of secrets to use to connect to the remote server
     * @param delegatingUserId external userId making request
     * @param auditLog destination for log messages.
     * @throws InvalidParameterException a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public ConfigurationManagementClient(String   serverPlatformRootURL,
                                         String   secretStoreProvider,
                                         String   secretStoreLocation,
                                         String   secretStoreCollection,
                                         String   delegatingUserId,
                                         AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        try
        {
            invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformRootURL, NULL_SERVER_NAME, methodName);

            this.serverPlatformRootURL = serverPlatformRootURL;
            this.delegatingUserId = delegatingUserId;

            this.restClient = new AdminServicesRESTClient(NULL_SERVER_NAME, serverPlatformRootURL, secretStoreProvider, secretStoreLocation, secretStoreCollection, auditLog);
        }
        catch (InvalidParameterException error)
        {
            throw new InvalidParameterException(error.getReportedErrorMessage(), error);
        }
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @param secretsStoreConnectorMap connectors to secrets stores
     * @param delegatingUserId external userId making request
     * @param auditLog destination for log messages.
     * @throws InvalidParameterException a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public ConfigurationManagementClient(String                             serverPlatformRootURL,
                                         Map<String, SecretsStoreConnector> secretsStoreConnectorMap,
                                         String                             delegatingUserId,
                                         AuditLog                           auditLog) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        try
        {
            invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformRootURL, NULL_SERVER_NAME, methodName);

            this.serverPlatformRootURL = serverPlatformRootURL;
            this.delegatingUserId = delegatingUserId;

            this.restClient = new AdminServicesRESTClient(NULL_SERVER_NAME, serverPlatformRootURL, secretsStoreConnectorMap, auditLog);
        }
        catch (InvalidParameterException error)
        {
            throw new InvalidParameterException(error.getReportedErrorMessage(), error);
        }
    }


    /**
     * Return all the OMAG Server configurations that are stored on this platform.
     *
     * @return the OMAG Server configurations that are stored on this platform
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public Set<OMAGServerConfig> getAllServerConfigurations() throws UserNotAuthorizedException,
                                                                     OMAGConfigurationErrorException,
                                                                     InvalidParameterException
    {
        final String methodName  = "getAllServerConfigurations";
        final String urlTemplate = "/open-metadata/admin-services/configurations?delegatingUserId={1}";

        OMAGServerConfigsResponse restResult = restClient.callGetAllServerConfigurationsRESTCall(methodName,
                                                                                                 serverPlatformRootURL + urlTemplate,
                                                                                                 delegatingUserId);

        return restResult.getOMAGServerConfigs();
    }



    /**
     * Set up the configuration properties for an OMAG Server in a single command.
     *
     * @param serverConfig configuration for the server
     * @throws UserNotAuthorizedException      the supplied userId is not authorized to issue this command.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     * @throws InvalidParameterException   invalid serverName or destinationPlatform parameter.
     */
    public void setOMAGServerConfig(OMAGServerConfig serverConfig) throws UserNotAuthorizedException,
                                                                          OMAGConfigurationErrorException,
                                                                          InvalidParameterException
    {
        final String methodName  = "setOMAGServerConfig";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/configuration?delegatingUserId={1}";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        serverConfig,
                                        serverConfig.getLocalServerName(),
                                        delegatingUserId);
    }


    /**
     * Push the configuration for the server to another OMAG Server Platform.
     *
     * @param serverName                 local server name
     * @param destinationPlatformURLRoot location of the platform where the config is to be deployed to
     * @throws UserNotAuthorizedException      the supplied userId is not authorized to issue this command.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     * @throws InvalidParameterException   invalid serverName or destinationPlatform parameter.
     */
    public void deployOMAGServerConfig(String serverName,
                                       String destinationPlatformURLRoot) throws UserNotAuthorizedException,
                                                                                 OMAGConfigurationErrorException,
                                                                                 InvalidParameterException
    {
        final String methodName  = "deployOMAGServerConfig";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/configuration/deploy?delegatingUserId={1}";

        URLRequestBody requestBody = new URLRequestBody();

        requestBody.setUrlRoot(destinationPlatformURLRoot);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        serverName,
                                        delegatingUserId);
    }


    /**
     * Return the complete set of configuration properties in use by the server from the configuration store.
     *
     * @param serverName local server name
     * @return OMAGServerConfig properties
     * @throws UserNotAuthorizedException      the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException   invalid serverName parameter.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    public OMAGServerConfig getStoredOMAGServerConfig(String serverName) throws UserNotAuthorizedException,
                                                                                InvalidParameterException,
                                                                                OMAGConfigurationErrorException
    {
        final String methodName  = "getStoredOMAGServerConfig";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/configuration?delegatingUserId={1}";

        OMAGServerConfigResponse restResult = restClient.callOMAGServerConfigGetRESTCall(methodName,
                                                                                         serverPlatformRootURL + urlTemplate,
                                                                                         serverName,
                                                                                         delegatingUserId);

        return restResult.getOMAGServerConfig();
    }


    /**
     * Return the complete set of configuration properties in use by the server that have the placeholders resolved.
     *
     * @param serverName local server name
     * @return OMAGServerConfig properties
     * @throws UserNotAuthorizedException      the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException   invalid serverName parameter.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    public OMAGServerConfig getResolvedOMAGServerConfig(String serverName) throws UserNotAuthorizedException,
                                                                                  InvalidParameterException,
                                                                                  OMAGConfigurationErrorException
    {
        final String methodName  = "getResolvedOMAGServerConfig";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/configuration/resolved?delegatingUserId={1}";

        OMAGServerConfigResponse restResult = restClient.callOMAGServerConfigGetRESTCall(methodName,
                                                                                         serverPlatformRootURL + urlTemplate,
                                                                                         serverName,
                                                                                         delegatingUserId);

        return restResult.getOMAGServerConfig();
    }
}
