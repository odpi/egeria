/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.classifier;

import org.odpi.openmetadata.adminservices.configuration.properties.*;
import org.odpi.openmetadata.adminservices.configuration.registration.GovernanceServicesDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.ServerTypeClassification;
import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;

import java.util.List;

/**
 * ServerTypeClassifier is responsible for reviewing the configuration document to determine what type of
 * server is being requested in the configuration document.
 */
public class ServerTypeClassifier
{
    private static final String ACCESS_SERVICES_NAME      = "Open Metadata Access Services (OMAS)";
    private static final String INTEGRATION_SERVICES_NAME = "Open Metadata Integration Services (OMIS)";
    private static final String VIEW_SERVICES_NAME        = "Open Metadata View Services (OMVS)";

    private OMAGServerConfig  configurationDocument;
    private String            serverName;

    /**
     * Constructor
     *
     * @param serverName name of the server
     * @param configurationDocument document to analyze
     */
    public ServerTypeClassifier(String           serverName,
                                OMAGServerConfig configurationDocument)
    {
        this.configurationDocument = configurationDocument;
        this.serverName = serverName;
    }


    /**
     * Check that the config document will result in a valid type of server and return the type.
     *
     * @return server type classification or exception if anything wrong
     * @throws OMAGInvalidParameterException null config doc
     * @throws OMAGConfigurationErrorException incompatible config
     */
    public ServerTypeClassification getServerType() throws OMAGInvalidParameterException,
                                                           OMAGConfigurationErrorException
    {
        final String methodName = "getServerType";

        ServerTypeClassification serverTypeClassification = null;

        this.validateConfigurationDocumentNotNull(serverName, configurationDocument, methodName);

        RepositoryServicesConfig        repositoryServicesConfig        = configurationDocument.getRepositoryServicesConfig();
        List<AccessServiceConfig>       accessServiceConfigList         = configurationDocument.getAccessServicesConfig();
        List<IntegrationServiceConfig>  integrationServiceConfigList    = configurationDocument.getIntegrationServicesConfig();
        List<ViewServiceConfig>         viewServiceConfigList           = configurationDocument.getViewServicesConfig();
        ConformanceSuiteConfig          conformanceSuiteConfig          = configurationDocument.getConformanceSuiteConfig();
        EngineHostServicesConfig        engineHostServicesConfig        = configurationDocument.getEngineHostServicesConfig();
        OpenLineageServerConfig         openLineageServerConfig         = configurationDocument.getOpenLineageServerConfig();
        DataEngineProxyConfig           dataEngineProxyConfig           = configurationDocument.getDataEngineProxyConfig();

        if ((repositoryServicesConfig == null) &&
                (accessServiceConfigList == null) &&
                (engineHostServicesConfig == null) &&
                (integrationServiceConfigList == null) &&
                (viewServiceConfigList == null) &&
                (conformanceSuiteConfig == null) &&
                (openLineageServerConfig == null) &&
                (dataEngineProxyConfig == null))
        {
            throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.EMPTY_CONFIGURATION.getMessageDefinition(serverName),
                                                      this.getClass().getName(),
                                                      methodName);
        }


        /*
         * All servers need the repository services
         */
        if (repositoryServicesConfig == null)
        {
            /*
             * To get here, then another service is configured but not the repository services.
             */
            throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.NULL_REPOSITORY_CONFIG.getMessageDefinition(serverName),
                                                      this.getClass().getName(),
                                                      methodName);
        }

        /*
         * The access service list is only allowed in a metadata server or metadata access point.
         */
        if (accessServiceConfigList != null)
        {
            LocalRepositoryMode localRepositoryMode = this.detectLocalRepository(repositoryServicesConfig);
            if (localRepositoryMode == LocalRepositoryMode.METADATA_CACHE)
            {
                serverTypeClassification = ServerTypeClassification.METADATA_SERVER;
            }
            else if (localRepositoryMode == LocalRepositoryMode.OPEN_METADATA_NATIVE)
            {
                serverTypeClassification = ServerTypeClassification.METADATA_SERVER;
            }
            else if (localRepositoryMode == LocalRepositoryMode.PLUGIN_REPOSITORY)
            {
                serverTypeClassification = ServerTypeClassification.METADATA_SERVER;
            }
            else if (localRepositoryMode == LocalRepositoryMode.REPOSITORY_PROXY)
            {
                serverTypeClassification = ServerTypeClassification.REPOSITORY_PROXY;
            }
            else
            {
                serverTypeClassification = ServerTypeClassification.METADATA_ACCESS_POINT;
            }

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                GovernanceServicesDescription.CONFORMANCE_SUITE_SERVICES.getServiceName(),
                                                conformanceSuiteConfig);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                GovernanceServicesDescription.DATA_ENGINE_PROXY_SERVICES.getServiceName(),
                                                dataEngineProxyConfig);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                GovernanceServicesDescription.OPEN_LINEAGE_SERVICES.getServiceName(),
                                                openLineageServerConfig);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                GovernanceServicesDescription.ENGINE_HOST_SERVICES.getServiceName(),
                                                engineHostServicesConfig);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                INTEGRATION_SERVICES_NAME,
                                                integrationServiceConfigList);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                VIEW_SERVICES_NAME,
                                                viewServiceConfigList);
        }

        if (conformanceSuiteConfig != null)
        {
            serverTypeClassification = ServerTypeClassification.CONFORMANCE_SERVER;

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                ACCESS_SERVICES_NAME,
                                                accessServiceConfigList);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                GovernanceServicesDescription.ENGINE_HOST_SERVICES.getServiceName(),
                                                engineHostServicesConfig);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                INTEGRATION_SERVICES_NAME,
                                                integrationServiceConfigList);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                VIEW_SERVICES_NAME,
                                                viewServiceConfigList);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                GovernanceServicesDescription.DATA_ENGINE_PROXY_SERVICES.getServiceName(),
                                                dataEngineProxyConfig);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                GovernanceServicesDescription.OPEN_LINEAGE_SERVICES.getServiceName(),
                                                openLineageServerConfig);
        }

        if (dataEngineProxyConfig != null)
        {
            serverTypeClassification = ServerTypeClassification.DATA_ENGINE_PROXY;

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                ACCESS_SERVICES_NAME,
                                                accessServiceConfigList);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                GovernanceServicesDescription.ENGINE_HOST_SERVICES.getServiceName(),
                                                engineHostServicesConfig);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                INTEGRATION_SERVICES_NAME,
                                                integrationServiceConfigList);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                VIEW_SERVICES_NAME,
                                                viewServiceConfigList);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                GovernanceServicesDescription.CONFORMANCE_SUITE_SERVICES.getServiceName(),
                                                conformanceSuiteConfig);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                GovernanceServicesDescription.OPEN_LINEAGE_SERVICES.getServiceName(),
                                                openLineageServerConfig);
        }

        if (engineHostServicesConfig != null)
        {
            serverTypeClassification = ServerTypeClassification.ENGINE_HOST;

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                ACCESS_SERVICES_NAME,
                                                accessServiceConfigList);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                INTEGRATION_SERVICES_NAME,
                                                integrationServiceConfigList);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                VIEW_SERVICES_NAME,
                                                integrationServiceConfigList);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                GovernanceServicesDescription.CONFORMANCE_SUITE_SERVICES.getServiceName(),
                                                conformanceSuiteConfig);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                GovernanceServicesDescription.DATA_ENGINE_PROXY_SERVICES.getServiceName(),
                                                dataEngineProxyConfig);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                GovernanceServicesDescription.OPEN_LINEAGE_SERVICES.getServiceName(),
                                                openLineageServerConfig);
        }

        if (integrationServiceConfigList != null)
        {
            serverTypeClassification = ServerTypeClassification.INTEGRATION_DAEMON;

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                ACCESS_SERVICES_NAME,
                                                accessServiceConfigList);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                GovernanceServicesDescription.ENGINE_HOST_SERVICES.getServiceName(),
                                                engineHostServicesConfig);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                VIEW_SERVICES_NAME,
                                                viewServiceConfigList);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                GovernanceServicesDescription.CONFORMANCE_SUITE_SERVICES.getServiceName(),
                                                conformanceSuiteConfig);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                GovernanceServicesDescription.DATA_ENGINE_PROXY_SERVICES.getServiceName(),
                                                dataEngineProxyConfig);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                GovernanceServicesDescription.OPEN_LINEAGE_SERVICES.getServiceName(),
                                                openLineageServerConfig);
        }

        if (openLineageServerConfig != null)
        {
            serverTypeClassification = ServerTypeClassification.OPEN_LINEAGE_SERVER;

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                ACCESS_SERVICES_NAME,
                                                accessServiceConfigList);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                GovernanceServicesDescription.ENGINE_HOST_SERVICES.getServiceName(),
                                                engineHostServicesConfig);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                INTEGRATION_SERVICES_NAME,
                                                integrationServiceConfigList);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                VIEW_SERVICES_NAME,
                                                viewServiceConfigList);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                GovernanceServicesDescription.CONFORMANCE_SUITE_SERVICES.getServiceName(),
                                                conformanceSuiteConfig);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                GovernanceServicesDescription.DATA_ENGINE_PROXY_SERVICES.getServiceName(),
                                                dataEngineProxyConfig);
        }

        if (viewServiceConfigList != null)
        {
            serverTypeClassification = ServerTypeClassification.VIEW_SERVER;

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                ACCESS_SERVICES_NAME,
                                                accessServiceConfigList);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                GovernanceServicesDescription.ENGINE_HOST_SERVICES.getServiceName(),
                                                engineHostServicesConfig);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                INTEGRATION_SERVICES_NAME,
                                                integrationServiceConfigList);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                GovernanceServicesDescription.CONFORMANCE_SUITE_SERVICES.getServiceName(),
                                                conformanceSuiteConfig);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                GovernanceServicesDescription.DATA_ENGINE_PROXY_SERVICES.getServiceName(),
                                                dataEngineProxyConfig);

            this.validateSubsystemNotConfigured(serverName,
                                                serverTypeClassification.getServerTypeName(),
                                                GovernanceServicesDescription.OPEN_LINEAGE_SERVICES.getServiceName(),
                                                openLineageServerConfig);
        }

        if (serverTypeClassification == null)
        {
            /*
             * Last attempt to classify the server - it could be a metadata server or a repository proxy that are not running the access services.
             */
            LocalRepositoryMode   localRepositoryMode = this.detectLocalRepository(repositoryServicesConfig);

            if (localRepositoryMode == LocalRepositoryMode.METADATA_CACHE)
            {
                serverTypeClassification = ServerTypeClassification.METADATA_SERVER;
            }
            else if (localRepositoryMode == LocalRepositoryMode.OPEN_METADATA_NATIVE)
            {
                serverTypeClassification = ServerTypeClassification.METADATA_SERVER;
            }
            else if (localRepositoryMode == LocalRepositoryMode.PLUGIN_REPOSITORY)
            {
                serverTypeClassification = ServerTypeClassification.METADATA_SERVER;
            }
            else if (localRepositoryMode == LocalRepositoryMode.REPOSITORY_PROXY)
            {
                serverTypeClassification = ServerTypeClassification.REPOSITORY_PROXY;
            }
        }

        this.validateServerClassificationNotNull(serverName, serverTypeClassification, methodName);
        return serverTypeClassification;
    }


    /**
     * Checks that a configuration document has been supplied at server start up.
     *
     * @param serverName requested server
     * @param configurationDocument supplied document
     * @param methodName calling method
     * @throws OMAGInvalidParameterException resulting exception if config document is null.
     */
    @SuppressWarnings(value = "deprecation")
    private void validateConfigurationDocumentNotNull(String            serverName,
                                                      OMAGServerConfig  configurationDocument,
                                                      String            methodName) throws  OMAGInvalidParameterException
    {
        if (configurationDocument == null)
        {
            throw new OMAGInvalidParameterException(OMAGAdminErrorCode.NULL_SERVER_CONFIG.getMessageDefinition(serverName),
                                                    this.getClass().getName(),
                                                    methodName);
        }

        if (configurationDocument.getDataPlatformServicesConfig() != null)
        {
            throw new OMAGInvalidParameterException(OMAGAdminErrorCode.OLD_CONFIGURATION.getMessageDefinition(serverName,
                                                                                                              configurationDocument.getDataPlatformServicesConfig().toString()),
                                                    this.getClass().getName(),
                                                    methodName);
        }

        if (configurationDocument.getDiscoveryEngineServicesConfig() != null)
        {
            throw new OMAGInvalidParameterException(OMAGAdminErrorCode.OLD_CONFIGURATION.getMessageDefinition(serverName,
                                                                                                              configurationDocument.getDiscoveryEngineServicesConfig().toString()),
                                                    this.getClass().getName(),
                                                    methodName);
        }

        if (configurationDocument.getSecurityOfficerConfig() != null)
        {
            throw new OMAGInvalidParameterException(OMAGAdminErrorCode.OLD_CONFIGURATION.getMessageDefinition(serverName,
                                                                                                              configurationDocument.getSecurityOfficerConfig().toString()),
                                                    this.getClass().getName(),
                                                    methodName);
        }

        if (configurationDocument.getSecuritySyncConfig() != null)
        {
            throw new OMAGInvalidParameterException(OMAGAdminErrorCode.OLD_CONFIGURATION.getMessageDefinition(serverName,
                                                                                                              configurationDocument.getSecuritySyncConfig().toString()),
                                                    this.getClass().getName(),
                                                    methodName);
        }

        if (configurationDocument.getStewardshipEngineServicesConfig() != null)
        {
            throw new OMAGInvalidParameterException(OMAGAdminErrorCode.OLD_CONFIGURATION.getMessageDefinition(serverName,
                                                                                                              configurationDocument.getStewardshipEngineServicesConfig().toString()),
                                                    this.getClass().getName(),
                                                    methodName);
        }

        if (configurationDocument.getVirtualizationConfig() != null)
        {
            throw new OMAGInvalidParameterException(OMAGAdminErrorCode.OLD_CONFIGURATION.getMessageDefinition(serverName,
                                                                                                              configurationDocument.getVirtualizationConfig().toString()),
                                                    this.getClass().getName(),
                                                    methodName);
        }
    }


    /**
     * Checks that a classification has been derived for a configuration document.
     * This should never be called.  If it is occurring then there has probably been a new type
     * of server configuration added to Egeria without a corresponding update to the
     * server classifier.
     *
     * @param serverName requested server
     * @param serverTypeClassification classification value derived from the analysis of the
     *                                 configuration document
     * @param methodName calling method
     * @throws OMAGInvalidParameterException resulting exception if config document is null.
     */
    private void validateServerClassificationNotNull(String                    serverName,
                                                     ServerTypeClassification  serverTypeClassification,
                                                     String                    methodName) throws OMAGInvalidParameterException
    {
        if (serverTypeClassification == null)
        {
            throw new OMAGInvalidParameterException(OMAGAdminErrorCode.UNCLASSIFIABLE_SERVER.getMessageDefinition(serverName),
                                                    this.getClass().getName(),
                                                    methodName);
        }
    }


    /**
     * Determine if the server is to have a local repository or not.
     *
     * @param repositoryServicesConfig repository services config section of configuration document
     * @return The local repository mode.
     */
    private LocalRepositoryMode detectLocalRepository(RepositoryServicesConfig  repositoryServicesConfig)
    {
        LocalRepositoryConfig localRepositoryConfig = repositoryServicesConfig.getLocalRepositoryConfig();

        if (localRepositoryConfig == null)
        {
            return LocalRepositoryMode.NO_REPOSITORY;
        }

        LocalRepositoryMode localRepositoryMode = localRepositoryConfig.getLocalRepositoryMode();

        if (localRepositoryMode == null)
        {
            if (localRepositoryConfig.getLocalRepositoryLocalConnection() != null)
            {
                return LocalRepositoryMode.REPOSITORY_PROXY;
            }

            return LocalRepositoryMode.NO_REPOSITORY;
        }

        return localRepositoryMode;
    }


    /**
     * Check that the server configuration does not include a subsystem that it should not have.
     *
     * @param serverName name of the server that the configuration belongs to
     * @param serverTypeName name of the identified server type.
     * @param subsystemName name of subsystem being tested.
     * @param subsystemConfig setting of the configuration for the subsystem being tested.
     * @throws OMAGConfigurationErrorException inconsistent definition of subsystems for an OMAG server.
     */
    private void validateSubsystemNotConfigured(String serverName,
                                                String serverTypeName,
                                                String subsystemName,
                                                Object subsystemConfig) throws OMAGConfigurationErrorException
    {
        final String methodName = "validateSubsystemNotConfigured";

        if (subsystemConfig != null)
        {
           throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.INCOMPATIBLE_SUBSYSTEMS.getMessageDefinition(serverName,
                                                                                                                      serverTypeName,
                                                                                                                      subsystemName),
                                                      this.getClass().getName(),
                                                      methodName);
        }
    }
}
