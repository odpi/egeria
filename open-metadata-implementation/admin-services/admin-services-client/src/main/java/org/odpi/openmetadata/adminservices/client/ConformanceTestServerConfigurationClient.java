/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.client;

import org.odpi.openmetadata.adminservices.configuration.properties.RepositoryConformanceWorkbenchConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.rest.URLRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

/**
 * ConformanceTestServerConfigurationClient provides the configuration services needed by
 * The Conformance Test Server.  This configuration focuses on setting up test workbenches.
 */
public class ConformanceTestServerConfigurationClient extends CohortMemberConfigurationClient
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
    public ConformanceTestServerConfigurationClient(String   serverName,
                                                    String   serverPlatformRootURL,
                                                    String   secretStoreProvider,
                                                    String   secretStoreLocation,
                                                    String   secretStoreCollection,
                                                    AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformRootURL, secretStoreProvider, secretStoreLocation, secretStoreCollection, auditLog);
    }


    /**
     * Request that the conformance suite services are activated in this server to test the
     * support of the repository services running in the server named tutRepositoryServerName.
     *
     * @param repositoryConformanceWorkbenchConfig configuration for the repository conformance workbench.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */

    public void enableRepositoryConformanceSuiteWorkbench(RepositoryConformanceWorkbenchConfig repositoryConformanceWorkbenchConfig) throws UserNotAuthorizedException,
                                                                                                                                            InvalidParameterException,
                                                                                                                                            OMAGConfigurationErrorException
    {
        final String methodName    = "enableRepositoryConformanceSuiteWorkbench";
        final String parameterName = "repositoryConformanceWorkbenchConfig";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/conformance-suite-workbenches/repository-workbench" +
                "/repositories";

        invalidParameterHandler.validateObject(repositoryConformanceWorkbenchConfig, parameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        repositoryConformanceWorkbenchConfig,
                                        serverName);
    }


    /**
     * Request that the conformance suite services are activated in this server to test the
     * support of the platform services running in the platform at tutPlatformRootURL.
     *
     * @param platformURL url of the OMAG platform to test.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void enablePlatformConformanceSuiteWorkbench(String platformURL) throws UserNotAuthorizedException,
                                                                                   InvalidParameterException,
                                                                                   OMAGConfigurationErrorException
    {
        final String methodName    = "enablePlatformConformanceSuiteWorkbench";
        final String parameterName = "platformURL";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/conformance-suite-workbenches/platform-workbench/platforms";

        invalidParameterHandler.validateName(platformURL, parameterName, methodName);

        URLRequestBody requestBody = new URLRequestBody();

        requestBody.setUrlRoot(platformURL);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        serverName);
    }


    /**
     * Request that the repository conformance suite tests are deactivated in this server.
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void disableRepositoryConformanceSuiteServices() throws UserNotAuthorizedException,
                                                                   InvalidParameterException,
                                                                   OMAGConfigurationErrorException
    {
        final String methodName  = "disableRepositoryConformanceSuiteServices";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/conformance-suite-workbenches/repository-workbench";

        restClient.callVoidDeleteRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        serverName);
    }


    /**
     * Request that the repository conformance suite tests are deactivated in this server.
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void disablePlatformConformanceSuiteServices() throws UserNotAuthorizedException,
                                                                 InvalidParameterException,
                                                                 OMAGConfigurationErrorException
    {
        final String methodName  = "disablePlatformConformanceSuiteServices";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/conformance-suite-workbenches/platform-workbench";

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          serverName);
    }


    /**
     * Request that the conformance suite services are deactivated in this server.
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void disableAllConformanceSuiteWorkbenches() throws UserNotAuthorizedException,
                                                               InvalidParameterException,
                                                               OMAGConfigurationErrorException
    {
        final String methodName  = "disableAllConformanceSuiteWorkbenches";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/conformance-suite-workbenches";

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          serverName);
    }
}
