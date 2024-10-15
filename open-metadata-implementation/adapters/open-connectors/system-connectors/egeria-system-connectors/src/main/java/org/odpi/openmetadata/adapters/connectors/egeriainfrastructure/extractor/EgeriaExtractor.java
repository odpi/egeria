/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.extractor;

import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.properties.*;
import org.odpi.openmetadata.adminservices.classifier.ServerTypeClassifier;
import org.odpi.openmetadata.adminservices.client.ConfigurationManagementClient;
import org.odpi.openmetadata.adminservices.client.OMAGServerPlatformConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.properties.CohortConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.ServerTypeClassification;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.EmbeddedConnection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.VirtualConnection;
import org.odpi.openmetadata.governanceservers.enginehostservices.client.EngineHostClient;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.client.IntegrationDaemon;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationDaemonStatus;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationGroupSummary;
import org.odpi.openmetadata.integrationservices.lineage.client.LineageIntegrator;
import org.odpi.openmetadata.platformservices.client.PlatformServicesClient;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogReport;
import org.odpi.openmetadata.repositoryservices.clients.AuditLogServicesClient;
import org.odpi.openmetadata.repositoryservices.clients.MetadataHighwayServicesClient;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.cohortregistrystore.properties.MemberRegistration;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.properties.CohortConnectionStatus;
import org.odpi.openmetadata.repositoryservices.properties.CohortDescription;
import org.odpi.openmetadata.repositoryservices.rest.properties.BooleanResponse;
import org.odpi.openmetadata.repositoryservices.rest.properties.CohortMembershipListResponse;
import org.odpi.openmetadata.serveroperations.client.ServerOperationsClient;
import org.odpi.openmetadata.serveroperations.properties.ServerServicesStatus;
import org.odpi.openmetadata.serveroperations.properties.ServerStatus;

import java.util.*;

/**
 * EgeriaExtractor extracts information from the Egeria runtimes.  It covers both the OMAG Server Platform and the
 * OMAG Servers.
 */
public class EgeriaExtractor
{
    private final String platformURLRoot;
    private final String platformName;
    private final String serverOfInterest;
    private final String clientUserId;

    private final PlatformServicesClient                platformServicesClient;
    private final OMAGServerPlatformConfigurationClient platformConfigurationClient;
    private final ConfigurationManagementClient         configurationManagementClient;
    private final IntegrationDaemon                     integrationDaemonClient;
    private final LineageIntegrator                     openLineageClient;
    private final EngineHostClient                      engineHostClient;
    private final AuditLogServicesClient                auditLogServicesClient;
    private final MetadataHighwayServicesClient         metadataHighwayServicesClient;
    private final ServerOperationsClient                serverOperationsClient;


    /**
     * Constructor
     *
     * @param platformURLRoot platform URL root
     * @param platformName name of the platform
     * @param serverOfInterest optional server name
     * @param clientUserId userId to use for calls to the
     * @throws InvalidParameterException invalid parameter
     * @throws OMAGInvalidParameterException invalid parameter
     */
    public EgeriaExtractor(String platformURLRoot,
                           String platformName,
                           String serverOfInterest,
                           String clientUserId) throws InvalidParameterException,
                                                       OMAGInvalidParameterException,
                                                       org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException
    {
        this.platformURLRoot  = platformURLRoot;
        this.platformName     = platformName;
        this.serverOfInterest = serverOfInterest;
        this.clientUserId     = clientUserId;

        platformServicesClient        = new PlatformServicesClient(platformName, platformURLRoot);
        platformConfigurationClient   = new OMAGServerPlatformConfigurationClient(clientUserId, platformURLRoot);
        configurationManagementClient = new ConfigurationManagementClient(clientUserId, platformURLRoot);
        serverOperationsClient        = new ServerOperationsClient(clientUserId, platformURLRoot);

        if (serverOfInterest != null)
        {
            integrationDaemonClient = new IntegrationDaemon(serverOfInterest, platformURLRoot);
            openLineageClient = new LineageIntegrator(serverOfInterest, platformURLRoot);
            engineHostClient = new EngineHostClient(platformURLRoot, serverOfInterest);
            metadataHighwayServicesClient = new MetadataHighwayServicesClient(serverOfInterest, platformURLRoot);
            auditLogServicesClient = new AuditLogServicesClient(serverOfInterest, platformURLRoot);
        }
        else
        {
            integrationDaemonClient = null;
            openLineageClient = null;
            engineHostClient = null;
            metadataHighwayServicesClient = null;
            auditLogServicesClient = null;
        }
    }


    /**
     * Return the latest status information for the platform.
     *
     * @return platform report
     * @throws Exception a variety of exceptions from the different clients
     */
    public OMAGServerPlatformProperties getPlatformReport() throws Exception
    {
        OMAGServerPlatformProperties platformReport = new OMAGServerPlatformProperties();

        platformReport.setPlatformName(platformName);
        platformReport.setPlatformURLRoot(platformURLRoot);
        platformReport.setPlatformOrigin(platformServicesClient.getPlatformOrigin(clientUserId));
        platformReport.setPlatformStartTime(platformServicesClient.getPlatformStartTime(clientUserId));
        platformReport.setPlatformSecurityConnection(this.getConnectorProperties("Platform Security Connector",
                                                                                 platformServicesClient.getPlatformSecurityConnection(clientUserId)));
        platformReport.setConfigurationStoreConnection(this.getConnectorProperties("Configuration Store Connector",
                                                                                   platformConfigurationClient.getConfigurationStoreConnection()));
        platformReport.setRegisteredOMAGServices(platformServicesClient.getAllServices(clientUserId));

        /*
         * Collect server details.
         */
        Map<String, OMAGServerProperties> serverDetailsMap = new HashMap<>();

        if (serverOfInterest != null)
        {
            serverDetailsMap.put(serverOfInterest, extractServerReport(serverOfInterest));
        }
        else
        {
            List<String> knownServers = platformServicesClient.getKnownServers(clientUserId);

            if (knownServers != null)
            {
                for (String serverName : knownServers)
                {
                    if (serverName != null)
                    {
                        serverDetailsMap.put(serverName, extractServerReport(serverName));
                    }
                }
            }

        }

        if (! serverDetailsMap.isEmpty())
        {
            platformReport.setOMAGServers(new ArrayList<>(serverDetailsMap.values()));
        }
        return platformReport;
    }


    /**
     * Return the connector type for the requested connector provider after validating that the
     * connector provider is available on the OMAGServerPlatform's class path.  This method is for tools that are configuring
     * connectors into an Egeria server.  It does not validate that the connector will load and initialize.
     *
     * @param connectorProviderClassName name of the connector provider class
     * @return ConnectorType bean or exceptions that occur when trying to create the connector
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */

    public ConnectorType getConnectorType(String connectorProviderClassName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        return platformServicesClient.getConnectorType(clientUserId, connectorProviderClassName);
    }


    /**
     * Retrieve a list of the access services registered on the platform
     *
     * @return List of access services
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<RegisteredOMAGService> getAccessServices() throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        return platformServicesClient.getAccessServices(clientUserId);
    }


    /**
     * Retrieve a list of the engine services registered on the platform
     *
     * @return List of engine services
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<RegisteredOMAGService> getEngineServices() throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        return platformServicesClient.getEngineServices(clientUserId);
    }


    /**
     * Retrieve a list of the view services registered on the platform
     *
     * @return List of view services
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<RegisteredOMAGService> getViewServices() throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return platformServicesClient.getViewServices(clientUserId);
    }


    /**
     * Retrieve a list of the integration services registered on the platform
     *
     * @return List of integration services
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<RegisteredOMAGService> getIntegrationServices() throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        return platformServicesClient.getIntegrationServices(clientUserId);
    }


    /**
     * Retrieve a list of the services known on the platform
     *
     * @return List of common services
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<RegisteredOMAGService> getAllServices() throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        return platformServicesClient.getAllServices(clientUserId);
    }


    /*
     * ========================================================================================
     * Activate and deactivate the open metadata and governance capabilities in the OMAG Server
     */

    /**
     * Activate the Open Metadata and Governance (OMAG) server using the configuration document stored for this server.
     *
     * @param serverName server to start
     * @return success message
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the server.
     */
    public String activateServer(String serverName) throws UserNotAuthorizedException,
                                                                     InvalidParameterException,
                                                                     PropertyServerException
    {
        return platformServicesClient.activateWithStoredConfig(clientUserId, serverName);
    }



    /**
     * Activate the Open Metadata and Governance (OMAG) server using the configuration document stored for this server.
     *
     * @return success message
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the server.
     */
    public String activateServer() throws UserNotAuthorizedException,
                                          InvalidParameterException,
                                          PropertyServerException
    {
        return platformServicesClient.activateWithStoredConfig(clientUserId, serverOfInterest);
    }


    /**
     * Temporarily deactivate any open metadata and governance services.
     *
     * @param serverName server to start
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void shutdownServer(String serverName) throws UserNotAuthorizedException,
                                                         InvalidParameterException,
                                                         PropertyServerException
    {
        platformServicesClient.shutdownServer(clientUserId, serverName);
    }


    /**
     * Temporarily deactivate any open metadata and governance services.
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void shutdownServer() throws UserNotAuthorizedException,
                                                         InvalidParameterException,
                                                         PropertyServerException
    {
        platformServicesClient.shutdownServer(clientUserId, serverOfInterest);
    }


    /**
     * Temporarily shutdown all running servers.
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void shutdownAllServers() throws UserNotAuthorizedException,
                                            InvalidParameterException,
                                            PropertyServerException
    {
        platformServicesClient.shutdownAllServers(clientUserId);
    }


    /**
     * Permanently deactivate any open metadata and governance services and unregister from
     * any cohorts.
     *
     * @param serverName server to start
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */

    public void shutdownAndUnregisterServer(String serverName) throws UserNotAuthorizedException,
                                                                      InvalidParameterException,
                                                                      PropertyServerException
    {
        platformServicesClient.shutdownAndUnregisterServer(clientUserId, serverName);
    }


    /**
     * Permanently deactivate any open metadata and governance services and unregister from
     * any cohorts.
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */

    public void shutdownAndUnregisterServer() throws UserNotAuthorizedException,
                                                     InvalidParameterException,
                                                     PropertyServerException
    {
        platformServicesClient.shutdownAndUnregisterServer(clientUserId, serverOfInterest);
    }


    /**
     * Shutdown any active servers and unregister them from
     * any cohorts.
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void shutdownAndUnregisterAllServers() throws UserNotAuthorizedException,
                                                         InvalidParameterException,
                                                         PropertyServerException
    {
        platformServicesClient.shutdownAndUnregisterAllServers(clientUserId);
    }



    /**
     * A new server needs to register the metadataCollectionId for its metadata repository with the other servers in the
     * open metadata repository.  It only needs to do this once and uses a timestamp to record that the registration
     * event has been sent.
     * If the server has already registered in the past, it sends a reregistration request.
     *
     * @param userId calling user
     * @param cohortName name of cohort
     *
     * @return boolean to indicate that the request has been issued.  If false it is likely that the cohort name is not known
     *
     * @throws InvalidParameterException one of the supplied parameters caused a problem
     * @throws PropertyServerException there is a problem communicating with the remote server.
     * @throws UserNotAuthorizedException the user is not authorized to perform the operation requested
     */
    public boolean connectToCohort(String userId,
                                   String cohortName) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        assert (metadataHighwayServicesClient != null);

        try
        {
            return metadataHighwayServicesClient.connectToCohort(userId, cohortName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException error)
        {
            throw new InvalidParameterException(error, cohortName);
        }
        catch (RepositoryErrorException error)
        {
            throw new PropertyServerException(error);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            throw new UserNotAuthorizedException(error, userId);
        }
    }



    /**
     * Disconnect communications from a specific cohort.
     *
     * @param userId calling user
     * @param cohortName name of cohort
     * @return boolean flag to indicate success.
     * @throws InvalidParameterException one of the supplied parameters caused a problem
     * @throws PropertyServerException there is a problem communicating with the remote server.
     * @throws UserNotAuthorizedException the user is not authorized to perform the operation requested
     */
    public boolean disconnectFromCohort(String userId,
                                        String cohortName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        assert (metadataHighwayServicesClient != null);

        try
        {
            return metadataHighwayServicesClient.disconnectFromCohort(userId, cohortName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException error)
        {
            throw new InvalidParameterException(error, cohortName);
        }
        catch (RepositoryErrorException error)
        {
            throw new PropertyServerException(error);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            throw new UserNotAuthorizedException(error, userId);
        }
    }


    /**
     * Unregister from a specific cohort and disconnect from cohort communications.
     *
     * @param userId calling user
     * @param cohortName name of cohort
     * @return boolean flag to indicate success.
     * @throws InvalidParameterException one of the supplied parameters caused a problem
     * @throws PropertyServerException there is a problem communicating with the remote server.
     * @throws UserNotAuthorizedException the user is not authorized to perform the operation requested
     */
    public boolean unregisterFromCohort(String userId,
                                        String cohortName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        assert (metadataHighwayServicesClient != null);

        try
        {
            return metadataHighwayServicesClient.unregisterFromCohort(userId, cohortName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException error)
        {
            throw new InvalidParameterException(error, cohortName);
        }
        catch (RepositoryErrorException error)
        {
            throw new PropertyServerException(error);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            throw new UserNotAuthorizedException(error, userId);
        }
    }


    /*
     * ========================================================================================
     * Find out about a specific OMAG Server's configuration
     */

    /**
     * Return all the OMAG Server configurations that are stored on this platform.
     *
     * @return the OMAG Server configurations that are stored on this platform
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public Set<OMAGServerConfig> getAllServerConfigurations() throws OMAGNotAuthorizedException,
                                                                     OMAGConfigurationErrorException,
                                                                     OMAGInvalidParameterException
    {
        return configurationManagementClient.getAllServerConfigurations();
    }



    /**
     * Push the configuration for the server to another OMAG Server Platform.
     *
     * @param serverName                 local server name
     * @param destinationPlatformURLRoot location of the platform where the config is to be deployed to
     * @throws OMAGNotAuthorizedException      the supplied userId is not authorized to issue this command.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     * @throws OMAGInvalidParameterException   invalid serverName or destinationPlatform parameter.
     */
    public void deployOMAGServerConfig(String serverName,
                                       String destinationPlatformURLRoot) throws OMAGNotAuthorizedException,
                                                                                 OMAGConfigurationErrorException,
                                                                                 OMAGInvalidParameterException
    {
        configurationManagementClient.deployOMAGServerConfig(serverName, destinationPlatformURLRoot);
    }


    /**
     * Return the complete set of configuration properties in use by the server.
     *
     * @param serverName local server name
     * @return OMAGServerConfig properties
     * @throws OMAGNotAuthorizedException      the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException   invalid serverName parameter.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    public OMAGServerConfig getOMAGServerConfig(String serverName) throws OMAGNotAuthorizedException,
                                                                          OMAGInvalidParameterException,
                                                                          OMAGConfigurationErrorException
    {
        return configurationManagementClient.getOMAGServerConfig(serverName);
    }



    /**
     * Return the complete set of configuration properties in use by the server.
     *
     * @return OMAGServerConfig properties
     * @throws OMAGNotAuthorizedException      the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException   invalid serverName parameter.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    public OMAGServerConfig getOMAGServerConfig() throws OMAGNotAuthorizedException,
                                                         OMAGInvalidParameterException,
                                                         OMAGConfigurationErrorException
    {
        return configurationManagementClient.getOMAGServerConfig(serverOfInterest);
    }


    /*
     * =============================================================
     * Operational status and control
     */

    /**
     * Retrieve the server status
     *
     * @return The server status
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ServerStatus getServerStatus() throws InvalidParameterException,
                                                 UserNotAuthorizedException,
                                                 PropertyServerException
    {
        if (serverOfInterest != null)
        {
            return serverOperationsClient.getServerStatus(clientUserId, serverOfInterest);
        }

        return null;
    }


    /**
     * Return the configuration used for the current active instance of the server.  Null is returned if
     * the server instance is not running.
     *
     * @return configuration properties used to initialize the server or null if not running
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public OMAGServerConfig getActiveConfiguration() throws UserNotAuthorizedException,
                                                            InvalidParameterException,
                                                            PropertyServerException
    {
        if (serverOfInterest != null)
        {
            return serverOperationsClient.getActiveConfiguration(clientUserId, serverOfInterest);
        }

        return null;
    }



    /**
     * Return the status of a running server (use platform services to find out if the server is running).
     *
     * @return status of the server
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public ServerServicesStatus getActiveServerStatus() throws UserNotAuthorizedException,
                                                               InvalidParameterException,
                                                               PropertyServerException
    {
        if (serverOfInterest != null)
        {
            return serverOperationsClient.getActiveServerStatus(clientUserId, serverOfInterest);
        }

        return null;
    }


    /**
     * Retrieve a list of the active services on a server
     *
     * @return List of service names
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<String> getActiveServices() throws InvalidParameterException,
                                                   UserNotAuthorizedException,
                                                   PropertyServerException
    {
        if (serverOfInterest != null)
        {
            return serverOperationsClient.getActiveServices(clientUserId, serverOfInterest);
        }

        return null;
    }




    /**
     * Add a new open metadata archive to running repository.
     *
     * @param fileName name of the open metadata archive file.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void addOpenMetadataArchiveFile(String fileName) throws UserNotAuthorizedException,
                                                                   InvalidParameterException,
                                                                   PropertyServerException
    {
        if (serverOfInterest != null)
        {
            serverOperationsClient.addOpenMetadataArchiveFile(clientUserId, serverOfInterest, fileName);
        }
    }


    /**
     * Add a new open metadata archive to running repository.
     *
     * @param connection connection for the open metadata archive.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void addOpenMetadataArchive(Connection connection) throws UserNotAuthorizedException,
                                                                     InvalidParameterException,
                                                                     PropertyServerException
    {
        if (serverOfInterest != null)
        {
            serverOperationsClient.addOpenMetadataArchive(clientUserId, serverOfInterest, connection);
        }
    }


    /**
     * Add a new open metadata archive to running repository.
     *
     * @param openMetadataArchive openMetadataArchive for the open metadata archive.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void addOpenMetadataArchiveContent(OpenMetadataArchive openMetadataArchive) throws UserNotAuthorizedException,
                                                                                              InvalidParameterException,
                                                                                              PropertyServerException
    {
        if (serverOfInterest != null)
        {
            serverOperationsClient.addOpenMetadataArchiveContent(clientUserId, serverOfInterest, openMetadataArchive);
        }
    }



    /**
     * Return the latest status information about a particular service.
     *
     * @return server report
     * @throws Exception a variety of exceptions from the different clients
     */
    public OMAGServerProperties getServerReport() throws Exception
    {
        if (serverOfInterest != null)
        {
            return extractServerReport(serverOfInterest);
        }

        return null;
    }


    /**
     * Internal function for extracting details about a server.
     *
     * @param serverName ane of the server
     * @return server report
     * @throws Exception a variety of exceptions from the different clients
     */
    private OMAGServerProperties extractServerReport(String serverName) throws Exception
    {
        OMAGServerConfig savedConfiguration = configurationManagementClient.getOMAGServerConfig(serverName);

        if (savedConfiguration != null)
        {
            OMAGServerProperties serverProperties;
            ServerTypeClassifier serverTypeClassifier = new ServerTypeClassifier(serverName, savedConfiguration);

            ServerTypeClassification serverTypeClassification = serverTypeClassifier.getServerType();

            if (serverTypeClassification == ServerTypeClassification.INTEGRATION_DAEMON)
            {
                IntegrationDaemon integrationDaemonClient = new IntegrationDaemon(serverName, platformURLRoot);

                OMAGIntegrationDaemonProperties integrationDaemon = new OMAGIntegrationDaemonProperties();

                IntegrationDaemonStatus integrationDaemonStatus = integrationDaemonClient.getIntegrationDaemonStatus(clientUserId);

                if (integrationDaemonStatus != null)
                {
                    if (integrationDaemonStatus.getIntegrationGroupSummaries() != null)
                    {
                        List<OMAGIntegrationGroupProperties> integrationGroups = new ArrayList<>();

                        for (IntegrationGroupSummary integrationGroupSummary : integrationDaemonStatus.getIntegrationGroupSummaries())
                        {
                            if (integrationGroupSummary != null)
                            {
                                OMAGIntegrationGroupProperties integrationGroupProperties = new OMAGIntegrationGroupProperties();

                                integrationGroupProperties.setIntegrationGroupName(integrationGroupSummary.getIntegrationGroupName());
                                integrationGroupProperties.setIntegrationGroupGUID(integrationGroupSummary.getIntegrationGroupGUID());
                                integrationGroupProperties.setIntegrationGroupDescription(integrationGroupSummary.getIntegrationGroupDescription());
                                integrationGroupProperties.setIntegrationGroupStatus(integrationGroupSummary.getIntegrationGroupStatus());

                                integrationGroups.add(integrationGroupProperties);
                            }
                        }

                        integrationDaemon.setIntegrationGroups(integrationGroups);
                    }

                    integrationDaemon.setIntegrationConnectorReports(integrationDaemonStatus.getIntegrationConnectorReports());
                }

                serverProperties = integrationDaemon;
            }
            else if (serverTypeClassification == ServerTypeClassification.ENGINE_HOST)
            {
                EngineHostClient engineHostClient = new EngineHostClient(platformURLRoot, serverName);

                OMAGEngineHostProperties engineHost = new OMAGEngineHostProperties();

                engineHost.setGovernanceEngineSummaries(engineHostClient.getGovernanceEngineSummaries(clientUserId));

                serverProperties = engineHost;
            }
            else if (serverTypeClassification == ServerTypeClassification.METADATA_ACCESS_STORE)
            {
                OMAGMetadataStoreProperties metadataStoreProperties = new OMAGMetadataStoreProperties();

                if ((savedConfiguration.getRepositoryServicesConfig() != null) &&
                    (savedConfiguration.getRepositoryServicesConfig().getLocalRepositoryConfig() != null))
                {
                    metadataStoreProperties.setRepositoryConnector(this.getConnectorProperties("Local Repository",
                                                                                               savedConfiguration.getRepositoryServicesConfig().getLocalRepositoryConfig().getLocalRepositoryLocalConnection()));
                }

                serverProperties = metadataStoreProperties;
            }
            else
            {
                serverProperties = new OMAGServerProperties();
            }

            this.fillInStandardServerProperties(serverName,
                                                serverTypeClassification,
                                                savedConfiguration,
                                                serverProperties);

            return serverProperties;
        }

        return null;
    }


    /**
     * Internal function to fill out the common details of a server.
     *
     * @param serverName name of the server
     * @param serverTypeClassification the server's classification
     * @param configuration the server's configuration
     * @param currentDetails the current values extracted for the server
     * @throws Exception a variety of exceptions from the different clients
     */
    private void fillInStandardServerProperties(String                   serverName,
                                                ServerTypeClassification serverTypeClassification,
                                                OMAGServerConfig         configuration,
                                                OMAGServerProperties     currentDetails) throws Exception
    {
        currentDetails.setServerName(serverName);
        currentDetails.setServerType(serverTypeClassification.getServerTypeName());

        if (configuration != null)
        {
            currentDetails.setMaxPageSize(configuration.getMaxPageSize());
            currentDetails.setDescription(configuration.getLocalServerDescription());
            currentDetails.setOrganizationName(configuration.getOrganizationName());
            currentDetails.setSecurityConnection(configuration.getServerSecurityConnection());
            currentDetails.setServerId(configuration.getLocalServerId());

            if (configuration.getRepositoryServicesConfig() != null)
            {
                currentDetails.setCohorts(this.getCohortConfigDetails(serverName, configuration.getRepositoryServicesConfig().getCohortConfigList()));
            }
        }

        ServerStatus platformServerStatus = platformServicesClient.getServerStatus(clientUserId, serverName);

        if (platformServerStatus != null)
        {
            currentDetails.setLastStartTime(platformServerStatus.getServerStartTime());
            currentDetails.setLastShutdownTime(platformServerStatus.getServerEndTime());
            currentDetails.setServerHistory(platformServerStatus.getServerHistory());
        }

        try
        {
            ServerServicesStatus activeServerStatus = platformServicesClient.getActiveServerStatus(clientUserId, serverName);

            if (activeServerStatus != null)
            {
                currentDetails.setServerActiveStatus(activeServerStatus.getServerActiveStatus());
                currentDetails.setServerType(activeServerStatus.getServerType());
                currentDetails.setServices(activeServerStatus.getServices());
            }
        }
        catch (Exception serverNotRunningException)
        {
            // nothing to do - simply that the server is not running
        }
    }


    /**
     * Return the cohort details for the named cohort.
     *
     * @param cohortConfigs details of the cohort from the configuration
     *
     * @return corresponding details
     */
    private List<OMAGCohortProperties> getCohortConfigDetails(String             serverName,
                                                              List<CohortConfig> cohortConfigs) throws org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException,
                                                                                                       org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException
    {
        if (cohortConfigs != null)
        {
            try
            {
                MetadataHighwayServicesClient metadataHighwayServicesClient = new MetadataHighwayServicesClient(serverName, platformURLRoot);

                List<CohortDescription> cohortDescriptions = metadataHighwayServicesClient.getCohortDescriptions(clientUserId);

                Map<String, CohortConnectionStatus> cohortConnectionStatusMap = new HashMap<>();

                if (cohortDescriptions != null)
                {
                    for (CohortDescription cohortDescription : cohortDescriptions)
                    {
                        if (cohortDescription != null)
                        {
                            cohortConnectionStatusMap.put(cohortDescription.getCohortName(),
                                                          cohortDescription.getConnectionStatus());
                        }
                    }
                }

                List<OMAGCohortProperties> cohortPropertiesList = new ArrayList<>();

                for (CohortConfig cohortConfig : cohortConfigs)
                {
                    if (cohortConfig != null)
                    {
                        OMAGCohortProperties cohortDetails = new OMAGCohortProperties();

                        cohortDetails.setCohortName(cohortConfig.getCohortName());

                        List<OMAGConnectorProperties> cohortConnectors = new ArrayList<>();

                        if (cohortConfig.getCohortRegistryConnection() != null)
                        {
                            cohortConnectors.add(getConnectorProperties(cohortDetails.getCohortName() + " Cohort Registry Connector",
                                                                        cohortConfig.getCohortRegistryConnection()));
                        }

                        if (cohortConfig.getCohortOMRSTopicConnection() != null)
                        {
                            cohortConnectors.add(getConnectorProperties(cohortDetails.getCohortName() + " Cohort Topic",
                                                                        cohortConfig.getCohortOMRSTopicConnection()));
                        }

                        if (cohortConfig.getCohortOMRSRegistrationTopicConnection() != null)
                        {
                            cohortConnectors.add(getConnectorProperties(cohortDetails.getCohortName() + " Cohort Registration Topic",
                                                                        cohortConfig.getCohortOMRSRegistrationTopicConnection()));
                        }

                        if (cohortConfig.getCohortOMRSTypesTopicConnection() != null)
                        {
                            cohortConnectors.add(getConnectorProperties(cohortDetails.getCohortName() + " Cohort Types Topic",
                                                                        cohortConfig.getCohortOMRSTypesTopicConnection()));
                        }

                        if (cohortConfig.getCohortOMRSInstancesTopicConnection() != null)
                        {
                            cohortConnectors.add(getConnectorProperties(cohortDetails.getCohortName() + " Cohort Instances Topic",
                                                                        cohortConfig.getCohortOMRSInstancesTopicConnection()));
                        }

                        cohortDetails.setConnectors(cohortConnectors);
                        cohortDetails.setConnectionStatus(cohortConnectionStatusMap.get(cohortConfig.getCohortName()));
                        cohortDetails.setLocalRegistration(metadataHighwayServicesClient.getLocalRegistration(clientUserId, cohortConfig.getCohortName()));
                        cohortDetails.setRemoteRegistrations(metadataHighwayServicesClient.getRemoteRegistrations(clientUserId, cohortConfig.getCohortName()));

                        cohortPropertiesList.add(cohortDetails);
                    }
                }

                return cohortPropertiesList;
            }
            catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException serverNotRunningException)
            {
                // nothing to do - simply that the server is not running
            }
        }

        return null;
    }


    /**
     * Extract interesting details about a connector from its connector object.
     *
     * @param connectorName name of the connector (from its context)
     * @param connection connector used to initialize the connector
     * @return connector properties for the report
     */
    private OMAGConnectorProperties getConnectorProperties(String     connectorName,
                                                           Connection connection)
    {
        OMAGConnectorProperties connectorProperties = new OMAGConnectorProperties();

        connectorProperties.setConnectorName(connectorName);
        if (connection != null)
        {
            connectorProperties.setConnectorType(connection.getConnectorType());
            connectorProperties.setConfigurationProperties(connection.getConfigurationProperties());

            if (connectorName == null)
            {
                if (connection.getDisplayName() != null)
                {
                    connectorProperties.setConnectorName(connection.getDisplayName());
                }
                else
                {
                    connectorProperties.setConnectorName(connection.getQualifiedName());
                }
            }

            connectorProperties.setConnectorUserId(connection.getUserId());

            if (connection.getEndpoint() != null)
            {
                connectorProperties.setNetworkAddress(connection.getEndpoint().getAddress());
            }

            if (connection instanceof VirtualConnection virtualConnection)
            {
                if (virtualConnection.getEmbeddedConnections() != null)
                {
                    List<OMAGConnectorProperties> nestedConnectors = new ArrayList<>();

                    for (EmbeddedConnection embeddedConnection : virtualConnection.getEmbeddedConnections())
                    {
                        if (embeddedConnection != null)
                        {
                            if (embeddedConnection.getEmbeddedConnection() != null)
                            {
                                nestedConnectors.add(this.getConnectorProperties(embeddedConnection.getDisplayName(),
                                                                                 embeddedConnection.getEmbeddedConnection()));
                            }
                        }
                    }

                    connectorProperties.setNestedConnectors(nestedConnectors);
                }
            }
        }

        return connectorProperties;
    }



    /*
     * ========================================================================================
     * Integration Daemon specific services
     */


    /**
     * Retrieve the configuration properties of the named connector.
     *
     * @param connectorName name of a specific connector or null for all connectors
     *
     * @return property map
     *
     * @throws InvalidParameterException the connector name is not recognized
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration daemon
     */
    public Map<String, Object> getConfigurationProperties(String connectorName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        assert integrationDaemonClient != null;
        return integrationDaemonClient.getConfigurationProperties(clientUserId,
                                                                  connectorName);
    }


    /**
     * Update the configuration properties of the connectors, or specific connector if a connector name is supplied.
     *
     * @param connectorName name of a specific connector or null for all connectors
     * @param isMergeUpdate should the properties be merged into the existing properties or replace them
     * @param configurationProperties new configuration properties
     * @throws InvalidParameterException the connector name is not recognized
     */
    public void updateConfigurationProperties(String              connectorName,
                                              boolean             isMergeUpdate,
                                              Map<String, Object> configurationProperties) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        assert integrationDaemonClient != null;
        integrationDaemonClient.updateConfigurationProperties(clientUserId, connectorName, isMergeUpdate, configurationProperties);
    }


    /**
     * Update the configuration properties of the connectors, or specific connector if a connector name is supplied.
     *
     * @param connectorName name of a specific connector or null for all connectors
     * @param networkAddress new address
     * @throws InvalidParameterException the connector name is not recognized
     */
    public void updateEndpointNetworkAddress(String              connectorName,
                                             String              networkAddress) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        assert integrationDaemonClient != null;
        integrationDaemonClient.updateEndpointNetworkAddress(clientUserId, connectorName, networkAddress);

    }

    /**
     * Update the configuration properties of the connectors, or specific connector if a connector name is supplied.
     *
     * @param connectorName name of a specific connector or null for all connectors
     * @param connection new address
     * @throws InvalidParameterException the connector name is not recognized
     */
    public void updateConnectorConnection(String     connectorName,
                                          Connection connection) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        assert integrationDaemonClient != null;
        integrationDaemonClient.updateConnectorConnection(clientUserId, connectorName, connection);
    }


    /**
     * Issue a refresh() request on a specific connector
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration daemon
     */
    public void refreshConnector(String connectorName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        assert integrationDaemonClient != null;
        integrationDaemonClient.refreshConnector(clientUserId, connectorName);
    }



    /**
     * Issue a refresh() request on a connector running in the integration daemon.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration daemon
     */
    public void refreshConnectors() throws InvalidParameterException,
                                           UserNotAuthorizedException,
                                           PropertyServerException
    {
        assert integrationDaemonClient != null;
        integrationDaemonClient.refreshConnectors(clientUserId);
    }



    /**
     * Issue a restart() request on a specific connector
     *
     * @param connectorName connector
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration daemon
     */
    public void restartConnector(String connectorName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        assert integrationDaemonClient != null;
        integrationDaemonClient.restartConnector(clientUserId, connectorName);
    }



    /**
     * Issue a restart() request on a connector running in the integration daemon.
     **
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration daemon
     */
    public void restartConnectors() throws InvalidParameterException,
                                           UserNotAuthorizedException,
                                           PropertyServerException
    {
        assert integrationDaemonClient != null;
        integrationDaemonClient.restartConnectors(clientUserId);
    }


    /**
     * Request that the integration group refresh its configuration by calling the metadata server.
     * This request is useful if the metadata server has an outage, particularly while the
     * integration daemon is initializing.  This request just ensures that the latest configuration
     * is in use.
     *
     * @param integrationGroupName qualifiedName of the integration group to target
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the integration group.
     */
    public  void refreshIntegrationGroupConfig(String integrationGroupName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        assert integrationDaemonClient != null;
        integrationDaemonClient.refreshConfig(clientUserId, integrationGroupName);
    }


    /**
     * Pass an open lineage event to the integration service.  It will pass it on to the integration connectors that have registered a
     * listener for open lineage events.
     *
     * @param event open lineage event to publish.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException the caller is not authorized to call the service
     * @throws PropertyServerException there is a problem processing the request
     */
    public void publishOpenLineageEvent(String event) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        assert openLineageClient != null;

        openLineageClient.publishOpenLineageEvent(clientUserId, event);
    }


    /*
     * ========================================================================================
     * Engine Host specific services
     */


    /**
     * Request that the governance engine refresh its configuration by calling the metadata server.
     * This request is useful if the metadata server has an outage, particularly while the
     * governance server is initializing.  This request just ensures that the latest configuration
     * is in use.
     *
     * @param governanceEngineName qualifiedName of the governance engine to target
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the governance engine.
     */
    public  void refreshEngineConfig(String governanceEngineName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        assert engineHostClient != null;
        engineHostClient.refreshConfig(clientUserId, governanceEngineName);
    }

    /*
     * ========================================================================================
     * Metadata Access Server specific services
     */

    /**
     * A new server needs to register the metadataCollectionId for its metadata repository with the other servers in the
     * open metadata repository.  It only needs to do this once and uses a timestamp to record that the registration
     * event has been sent.
     * If the server has already registered in the past, it sends a reregistration request.
     *
     * @param cohortName name of cohort
     *
     * @return boolean to indicate that the request has been issued.  If false it is likely that the cohort name is not known
     *
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException one of the supplied parameters caused a problem
     * @throws RepositoryErrorException there is a problem communicating with the remote server.
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException the user is not authorized to perform the operation requested
     */
    public boolean connectToCohort(String cohortName) throws org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException,
                                                             RepositoryErrorException,
                                                             org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException
    {
        assert metadataHighwayServicesClient != null;
        return metadataHighwayServicesClient.connectToCohort(clientUserId, cohortName);
    }



    /**
     * Disconnect communications from a specific cohort.
     *
     * @param cohortName name of cohort
     * @return boolean flag to indicate success.
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException one of the supplied parameters caused a problem
     * @throws RepositoryErrorException there is a problem communicating with the remote server.
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException the user is not authorized to perform the operation requested
     */
    public boolean disconnectFromCohort(String cohortName) throws org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException,
                                                                  RepositoryErrorException,
                                                                  org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException
    {
        assert metadataHighwayServicesClient != null;
        return metadataHighwayServicesClient.disconnectFromCohort(clientUserId, cohortName);
    }


    /**
     * Unregister from a specific cohort and disconnect from cohort communications.
     *
     * @param cohortName name of cohort
     * @return boolean flag to indicate success.
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException one of the supplied parameters caused a problem
     * @throws RepositoryErrorException there is a problem communicating with the remote server.
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException the user is not authorized to perform the operation requested
     */
    public boolean unregisterFromCohort(String cohortName) throws org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException,
                                                                  RepositoryErrorException,
                                                                  org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException
    {
        assert metadataHighwayServicesClient != null;
        return metadataHighwayServicesClient.unregisterFromCohort(clientUserId, cohortName);
    }
}
