/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;

import org.odpi.openmetadata.adminservices.configuration.properties.*;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.rest.URLRequestBody;
import org.odpi.openmetadata.adminservices.rest.VoidResponse;
import org.odpi.openmetadata.repositoryservices.admin.OMRSConfigurationFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OMAGConformanceSuiteConfigServices configures the Open Metadata Conformance Suite workbenches
 * in an OMAG Server.
 */
public class OMAGConformanceSuiteConfigServices
{
    static private final String serviceName    = "Open Metadata Conformance Suite";
    static private final int    maxPageSize    = 50;

    private static final Logger log = LoggerFactory.getLogger(OMAGConformanceSuiteConfigServices.class);


    private OMAGServerAdminStoreServices configStore  = new OMAGServerAdminStoreServices();
    private OMAGServerErrorHandler       errorHandler = new OMAGServerErrorHandler();


    /**
     * Request that the repository conformance suite workbench is activated in this server to test the
     * support of the repository services running in the server named tutRepositoryServerName.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param tutRepositoryServerName name of the server that the repository workbench should test.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter.
     * OMAGConfigurationErrorException unexpected exception.
     */
    public VoidResponse enableRepositoryConformanceSuiteWorkbench(String userId,
                                                                  String serverName,
                                                                  String tutRepositoryServerName)
    {
        final String methodName = "enableRepositoryConformanceSuiteWorkbench";

        log.debug("Calling method: " + methodName);

        VoidResponse response = this.enableAllConformanceSuiteWorkbenches(userId,
                                                                          serverName,
                                                                          tutRepositoryServerName,
                                                                          null);

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Request that the platform conformance suite workbench is activated in this server to test the
     * support of the platform services running in the platform at tutPlatformRootURL.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param requestBody url of the OMAG platform to test.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter.
     * OMAGConfigurationErrorException unexpected exception.
     */
    public VoidResponse enablePlatformConformanceSuiteWorkbench(String         userId,
                                                                String         serverName,
                                                                URLRequestBody requestBody)
    {
        final String methodName = "enablePlatformConformanceSuiteWorkbench";

        log.debug("Calling method: " + methodName);

        VoidResponse response = this.enableAllConformanceSuiteWorkbenches(userId,
                                                                          serverName,
                                                                          null,
                                                                          requestBody.getUrlRoot());

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Request that the conformance suite services are activated in this server.  If tutRepositoryServerName
     * is set then the repository workbench is run.  If tutPlatformRootURL is set then the platform
     * workbench is run.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param tutRepositoryServerName name of the server that the repository workbench should use.
     * @param tutPlatformRootURL url of the OMAG platform to test.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter.
     * OMAGConfigurationErrorException unexpected exception.
     */
    private VoidResponse enableAllConformanceSuiteWorkbenches(String userId,
                                                              String serverName,
                                                              String tutRepositoryServerName,
                                                              String tutPlatformRootURL)
    {
        final String methodName = "enableAllConformanceSuiteWorkbenches";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);

            ConformanceSuiteConfig conformanceSuiteConfig = serverConfig.getConformanceSuiteConfig();

            List<String> configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            configAuditTrail.add(new Date().toString() + " " + userId + " begins adding configuration for " + serviceName + ".");

            serverConfig.setAuditTrail(configAuditTrail);

            configStore.saveServerConfig(serverName, methodName, serverConfig);

            if (conformanceSuiteConfig == null)
            {
                conformanceSuiteConfig = new ConformanceSuiteConfig();
            }

            if ((conformanceSuiteConfig.getPlatformWorkbenchConfig() == null) &&
                (conformanceSuiteConfig.getRepositoryWorkbenchConfig() == null))
            {
                OMAGServerAdminServices adminAPI = new OMAGServerAdminServices();

                adminAPI.setMaxPageSize(userId, serverName, maxPageSize);
                adminAPI.setServerType(userId, serverName, serviceName);
                adminAPI.setInMemLocalRepository(userId, serverName);

                serverConfig = configStore.getServerConfig(serverName, methodName);

                RepositoryServicesConfig repositoryServicesConfig = serverConfig.getRepositoryServicesConfig();
                OMRSConfigurationFactory configurationFactory     = new OMRSConfigurationFactory();
                EnterpriseAccessConfig
                        enterpriseAccessConfig = configurationFactory.getDefaultEnterpriseAccessConfig(serverConfig.getLocalServerName(),
                                                                                                       serverConfig.getLocalServerId());

                repositoryServicesConfig.setEnterpriseAccessConfig(enterpriseAccessConfig);

                serverConfig.setRepositoryServicesConfig(repositoryServicesConfig);

                configStore.saveServerConfig(serverName, methodName, serverConfig);
            }

            serverConfig = configStore.getServerConfig(serverName, methodName);
            configAuditTrail = serverConfig.getAuditTrail();

            if (tutRepositoryServerName != null)
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " enable repository workbench to test " + tutRepositoryServerName + ".");

                RepositoryConformanceWorkbenchConfig
                        repositoryWorkbenchConfig = new RepositoryConformanceWorkbenchConfig();

                repositoryWorkbenchConfig.setTutRepositoryServerName(tutRepositoryServerName);
                conformanceSuiteConfig.setRepositoryWorkbenchConfig(repositoryWorkbenchConfig);
            }

            if (tutPlatformRootURL != null)
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " enable platform workbench to test " + tutPlatformRootURL + ".");

                PlatformConformanceWorkbenchConfig platformWorkbenchConfig = new PlatformConformanceWorkbenchConfig();
                platformWorkbenchConfig.setTutPlatformURLRoot(tutPlatformRootURL);

                conformanceSuiteConfig.setPlatformWorkbenchConfig(platformWorkbenchConfig);
            }

            serverConfig.setConformanceSuiteConfig(conformanceSuiteConfig);

            configAuditTrail.add(new Date().toString() + " " + userId + " finished adding configuration for " + serviceName + ".");

            serverConfig.setAuditTrail(configAuditTrail);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (OMAGInvalidParameterException error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable   error)
        {
            errorHandler.captureRuntimeException(serverName, methodName, response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Request that the repository conformance suite tests are deactivated in this server.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter.
     * OMAGConfigurationErrorException unexpected exception.
     */
    public VoidResponse disableRepositoryConformanceSuiteServices(String userId, String serverName)
    {
        final String methodName = "disableRepositoryConformanceSuiteServices";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);

            ConformanceSuiteConfig conformanceSuiteConfig = serverConfig.getConformanceSuiteConfig();

            if (conformanceSuiteConfig != null)
            {
                RepositoryConformanceWorkbenchConfig repositoryConformanceWorkbenchConfig = conformanceSuiteConfig.getRepositoryWorkbenchConfig();

                if (repositoryConformanceWorkbenchConfig != null)
                {
                    List<String> configAuditTrail = serverConfig.getAuditTrail();

                    if (configAuditTrail == null)
                    {
                        configAuditTrail = new ArrayList<>();
                    }

                    configAuditTrail.add(new Date().toString() + " " + userId + " removed repository workbench configuration for " + serviceName + ".");

                    serverConfig.setAuditTrail(configAuditTrail);

                    conformanceSuiteConfig.setRepositoryWorkbenchConfig(null);
                    serverConfig.setConformanceSuiteConfig(conformanceSuiteConfig);

                    configStore.saveServerConfig(serverName, methodName, serverConfig);
                }
            }
        }
        catch (OMAGInvalidParameterException error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable   error)
        {
            errorHandler.captureRuntimeException(serverName, methodName, response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Request that the repository conformance suite tests are deactivated in this server.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter.
     * OMAGConfigurationErrorException unexpected exception.
     */
    public VoidResponse disablePlatformConformanceSuiteServices(String userId, String serverName)
    {
        final String methodName = "disablePlatformConformanceSuiteServices";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);

            ConformanceSuiteConfig conformanceSuiteConfig = serverConfig.getConformanceSuiteConfig();

            if (conformanceSuiteConfig != null)
            {
                PlatformConformanceWorkbenchConfig platformConformanceWorkbenchConfig = conformanceSuiteConfig.getPlatformWorkbenchConfig();

                if (platformConformanceWorkbenchConfig != null)
                {
                    List<String> configAuditTrail = serverConfig.getAuditTrail();

                    if (configAuditTrail == null)
                    {
                        configAuditTrail = new ArrayList<>();
                    }

                    configAuditTrail.add(new Date().toString() + " " + userId + " removed platform workbench configuration for " + serviceName + ".");

                    serverConfig.setAuditTrail(configAuditTrail);

                    conformanceSuiteConfig.setPlatformWorkbenchConfig(null);
                    serverConfig.setConformanceSuiteConfig(conformanceSuiteConfig);

                    configStore.saveServerConfig(serverName, methodName, serverConfig);
                }
            }
        }
        catch (OMAGInvalidParameterException error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable   error)
        {
            errorHandler.captureRuntimeException(serverName, methodName, response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Request that all of the conformance suite tests are deactivated in this server.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter.
     * OMAGConfigurationErrorException unexpected exception.
     */
    public VoidResponse disableAllConformanceSuiteWorkbenches(String userId, String serverName)
    {
        final String methodName = "disableAllConformanceSuiteWorkbenches";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);

            List<String> configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for " + serviceName + ".");

            serverConfig.setAuditTrail(configAuditTrail);

            serverConfig.setConformanceSuiteConfig(null);
            serverConfig.setRepositoryServicesConfig(null);
            serverConfig.setLocalServerType(null);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (OMAGInvalidParameterException error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable   error)
        {
            errorHandler.captureRuntimeException(serverName, methodName, response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }
}
