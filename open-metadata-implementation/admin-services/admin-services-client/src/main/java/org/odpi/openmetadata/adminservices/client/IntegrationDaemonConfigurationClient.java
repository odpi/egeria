/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.client;

import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationGroupConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.rest.IntegrationGroupsResponse;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;

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
     * Work with the integration group configuration
     */


    /**
     * Return the configuration for the integration groups in this server.
     *
     * @return list of integration group configuration
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public List<IntegrationGroupConfig> getIntegrationGroupsConfiguration() throws OMAGNotAuthorizedException,
                                                                                   OMAGInvalidParameterException,
                                                                                   OMAGConfigurationErrorException
    {
        final String methodName  = "getIntegrationGroupsConfiguration";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/integration-groups/configuration";

        IntegrationGroupsResponse restResult = restClient.callIntegrationGroupsGetRESTCall(methodName,
                                                                                             serverPlatformRootURL + urlTemplate,
                                                                                             adminUserId,
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
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void configureIntegrationGroup(IntegrationGroupConfig groupConfig) throws OMAGNotAuthorizedException,
                                                                                     OMAGInvalidParameterException,
                                                                                     OMAGConfigurationErrorException
    {
        final String methodName    = "configureIntegrationGroup";
        final String parameterName = "groupConfig";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/integration-groups/configuration";

        try
        {
            invalidParameterHandler.validateObject(groupConfig, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        groupConfig,
                                        adminUserId,
                                        serverName);
    }



    /**
     * Set up the configuration for all the open metadata integration groups.  This overrides the current values.
     *
     * @param integrationGroupConfigs  list of configuration properties for each integration group.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setIntegrationGroupsConfig(List<IntegrationGroupConfig> integrationGroupConfigs) throws OMAGNotAuthorizedException,
                                                                                                              OMAGInvalidParameterException,
                                                                                                              OMAGConfigurationErrorException
    {
        final String methodName    = "setIntegrationGroupsConfig";
        final String configName    = "integrationGroupConfigs";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/integration-groups/configuration/all";

        try
        {
            invalidParameterHandler.validateObject(integrationGroupConfigs, configName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        integrationGroupConfigs,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Disable the integration groups.  This removes all configuration for the integration groups from the integration daemon.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearAllIntegrationGroups() throws OMAGNotAuthorizedException,
                                                   OMAGInvalidParameterException,
                                                   OMAGConfigurationErrorException
    {
        final String methodName  = "clearAllIntegrationGroups";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/integration-groups";

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          adminUserId,
                                          serverName);
    }


    /**
     * Remove an integration group.  This removes all configuration for the integration group.
     *
     * @param groupQualifiedName integration group name used in URL
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearIntegrationGroup(String groupQualifiedName) throws OMAGNotAuthorizedException,
                                                                        OMAGInvalidParameterException,
                                                                        OMAGConfigurationErrorException
    {
        final String methodName    = "clearIntegrationGroup";
        final String parameterName = "groupQualifiedName";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/integration-groups/{2}";

        try
        {
            invalidParameterHandler.validateName(groupQualifiedName, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          adminUserId,
                                          serverName,
                                          groupQualifiedName);
    }
}
