/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.client;

import org.odpi.openmetadata.adminservices.configuration.properties.RepositoryConformanceWorkbenchConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.rest.URLRequestBody;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;

/**
 * ConformanceTestServerConfigurationClient provides the configuration services needed by
 * The Conformance Test Server.  This configuration focuses on setting up test workbenches.
 */
public class ConformanceTestServerConfigurationClient extends CohortMemberConfigurationClient
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
    public ConformanceTestServerConfigurationClient(String adminUserId,
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
    public ConformanceTestServerConfigurationClient(String adminUserId,
                                                    String serverName,
                                                    String serverPlatformRootURL,
                                                    String connectionUserId,
                                                    String connectionPassword) throws OMAGInvalidParameterException
    {
        super(adminUserId, serverName, serverPlatformRootURL, connectionUserId, connectionPassword);
    }


    /**
     * Request that the conformance suite services are activated in this server to test the
     * support of the repository services running in the server named tutRepositoryServerName.
     *
     * @param repositoryConformanceWorkbenchConfig configuration for the repository conformance workbench.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */

    public void enableRepositoryConformanceSuiteWorkbench(RepositoryConformanceWorkbenchConfig repositoryConformanceWorkbenchConfig) throws OMAGNotAuthorizedException,
                                                                                                                                            OMAGInvalidParameterException,
                                                                                                                                            OMAGConfigurationErrorException
    {
        final String methodName    = "enableRepositoryConformanceSuiteWorkbench";
        final String parameterName = "repositoryConformanceWorkbenchConfig";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/conformance-suite-workbenches/repository-workbench" +
                "/repositories";

        try
        {
            invalidParameterHandler.validateObject(repositoryConformanceWorkbenchConfig, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        repositoryConformanceWorkbenchConfig,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Request that the conformance suite services are activated in this server to test the
     * support of the platform services running in the platform at tutPlatformRootURL.
     *
     * @param platformURL url of the OMAG platform to test.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void enablePlatformConformanceSuiteWorkbench(String platformURL) throws OMAGNotAuthorizedException,
                                                                                   OMAGInvalidParameterException,
                                                                                   OMAGConfigurationErrorException
    {
        final String methodName    = "enablePlatformConformanceSuiteWorkbench";
        final String parameterName = "platformURL";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/conformance-suite-workbenches/platform-workbench/platforms";

        try
        {
            invalidParameterHandler.validateName(platformURL, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        URLRequestBody requestBody = new URLRequestBody();

        requestBody.setUrlRoot(platformURL);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Request that the repository conformance suite tests are deactivated in this server.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void disableRepositoryConformanceSuiteServices() throws OMAGNotAuthorizedException,
                                                                   OMAGInvalidParameterException,
                                                                   OMAGConfigurationErrorException
    {
        final String methodName  = "disableRepositoryConformanceSuiteServices";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/conformance-suite-workbenches/repository-workbench";

        restClient.callVoidDeleteRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Request that the repository conformance suite tests are deactivated in this server.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void disablePlatformConformanceSuiteServices() throws OMAGNotAuthorizedException,
                                                                 OMAGInvalidParameterException,
                                                                 OMAGConfigurationErrorException
    {
        final String methodName  = "disablePlatformConformanceSuiteServices";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/conformance-suite-workbenches/platform-workbench";

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          adminUserId,
                                          serverName);
    }


    /**
     * Request that the conformance suite services are deactivated in this server.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void disableAllConformanceSuiteWorkbenches() throws OMAGNotAuthorizedException,
                                                               OMAGInvalidParameterException,
                                                               OMAGConfigurationErrorException
    {
        final String methodName  = "disableAllConformanceSuiteWorkbenches";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/conformance-suite-workbenches";

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          adminUserId,
                                          serverName);
    }
}
