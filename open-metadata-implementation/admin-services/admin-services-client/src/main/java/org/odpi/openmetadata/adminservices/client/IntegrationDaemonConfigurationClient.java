/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.client;

import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationGroupConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.rest.IntegrationGroupsResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * IntegrationDaemonConfigurationClient provides the configuration services for integration daemons.
 * This involves creating a list of integration services and/or integration groups.
 */
public class IntegrationDaemonConfigurationClient extends GovernanceServerConfigurationClient
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
    public IntegrationDaemonConfigurationClient(String   serverName,
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
     * Work with the integration group configuration
     */


    /**
     * Return the configuration for the integration groups in this server.
     *
     * @return list of integration group configuration
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public List<IntegrationGroupConfig> getIntegrationGroupsConfiguration() throws UserNotAuthorizedException,
                                                                                   InvalidParameterException,
                                                                                   OMAGConfigurationErrorException
    {
        final String methodName  = "getIntegrationGroupsConfiguration";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/integration-groups/configuration";

        IntegrationGroupsResponse restResult = restClient.callIntegrationGroupsGetRESTCall(methodName,
                                                                                             serverPlatformRootURL + urlTemplate,
                                                                                             serverName);
        return restResult.getGroups();
    }

    /*
     * =============================================================
     * Configure server making maximum use of defaults
     */


    /**
     * Add configuration for a single integration group to the server's config document.
     *
     * @param groupConfig  all values to configure an integration group
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void configureIntegrationGroup(IntegrationGroupConfig groupConfig) throws UserNotAuthorizedException,
                                                                                     InvalidParameterException,
                                                                                     OMAGConfigurationErrorException
    {
        final String methodName    = "configureIntegrationGroup";
        final String parameterName = "groupConfig";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/integration-groups/configuration";

        invalidParameterHandler.validateObject(groupConfig, parameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        groupConfig,
                                        serverName);
    }



    /**
     * Set up the configuration for all the open metadata integration groups.  This overrides the current values.
     *
     * @param integrationGroupConfigs  list of configuration properties for each integration group.
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setIntegrationGroupsConfig(List<IntegrationGroupConfig> integrationGroupConfigs) throws UserNotAuthorizedException,
                                                                                                              InvalidParameterException,
                                                                                                              OMAGConfigurationErrorException
    {
        final String methodName    = "setIntegrationGroupsConfig";
        final String configName    = "integrationGroupConfigs";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/integration-groups/configuration/all";

        invalidParameterHandler.validateObject(integrationGroupConfigs, configName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        integrationGroupConfigs,
                                        serverName);
    }


    /**
     * Disable the integration groups.  This removes all configuration for the integration groups from the integration daemon.
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearAllIntegrationGroups() throws UserNotAuthorizedException,
                                                   InvalidParameterException,
                                                   OMAGConfigurationErrorException
    {
        final String methodName  = "clearAllIntegrationGroups";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/integration-groups";

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          serverName);
    }


    /**
     * Remove an integration group.  This removes all configuration for the integration group.
     *
     * @param groupQualifiedName integration group name used in URL
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearIntegrationGroup(String groupQualifiedName) throws UserNotAuthorizedException,
                                                                        InvalidParameterException,
                                                                        OMAGConfigurationErrorException
    {
        final String methodName    = "clearIntegrationGroup";
        final String parameterName = "groupQualifiedName";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/integration-groups/{1}";

        invalidParameterHandler.validateName(groupQualifiedName, parameterName, methodName);

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          serverName,
                                          groupQualifiedName);
    }
}
