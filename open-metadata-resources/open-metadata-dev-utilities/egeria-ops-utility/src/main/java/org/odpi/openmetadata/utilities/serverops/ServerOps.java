/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.utilities.serverops;

import org.odpi.openmetadata.adminservices.client.ConfigurationManagementClient;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.ServerTypeClassification;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.client.IntegrationDaemon;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationConnectorReport;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationGroupSummary;
import org.odpi.openmetadata.http.HttpHelper;
import org.odpi.openmetadata.platformservices.client.PlatformServicesClient;
import org.odpi.openmetadata.serveroperations.properties.ServerServicesStatus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * ServerOps provides a utility for starting and stopping servers on an OMAG Server Platform.
 */
public class ServerOps
{
    private final String platformURLRoot;
    private final String secretsStoreProvider;
    private final String secretsStoreLocation;
    private final String secretsStoreCollection;


    /**
     * Set up the parameters for the sample.
     *
     * @param platformURLRoot location of server
     * @param secretsStoreProvider secrets store connector for bearer token
     * @param secretsStoreLocation secrets store location for bearer token
     * @param secretsStoreCollection secrets store collection for bearer token
     */
    private ServerOps(String platformURLRoot,
                      String secretsStoreProvider,
                      String secretsStoreLocation,
                      String secretsStoreCollection)
    {
        this.platformURLRoot = platformURLRoot;
        this.secretsStoreProvider   = secretsStoreProvider;
        this.secretsStoreLocation   = secretsStoreLocation;
        this.secretsStoreCollection = secretsStoreCollection;
    }



    /**
     * Retrieve the version of the platform.  This fails if the platform is not running or the endpoint is populated by a service that is not an
     * OMAG Server Platform.
     *
     * @return platform version or null
     */
    private String getPlatformOrigin()
    {
        try
        {
            /*
             * This client is from the platform services module and queries the runtime state of the platform and the servers that are running on it.
             */
            PlatformServicesClient platformServicesClient = new PlatformServicesClient("MyPlatform", platformURLRoot, secretsStoreProvider, secretsStoreLocation, secretsStoreCollection, null, null);

            /*
             * This is the first call to the platform and determines the version of the software.
             * If the platform is not running, or the remote service is not an OMAG Server Platform,
             * the utility fails at this point.
             */
            return platformServicesClient.getPlatformOrigin();
        }
        catch (Exception error)
        {
            System.out.println("\n\nThere was an " + error.getClass().getName() + " exception when calling the platform.  Error message is: " + error.getMessage());
            System.out.println("Ensure the platform URl is correct and the platform is running");
        }

        return null;
    }



    /**
     * Start the named server on the platform.  This will fail if the platform is not running,
     * or if the user is not authorized to issue operations requests to the platform or if the
     * server is not configured.
     *
     * @param serverName string name
     */
    private void startServer(String serverName)
    {
        try
        {
            PlatformServicesClient client = new PlatformServicesClient(serverName, platformURLRoot, secretsStoreProvider, secretsStoreLocation, secretsStoreCollection, null, null);

            System.out.println("Starting " + serverName + " ...");
            System.out.println(client.activateWithStoredConfig(serverName));
        }
        catch (Exception error)
        {
            System.out.println("There was an " + error.getClass().getName() + " exception when calling the platform.  Error message is: " + error.getMessage());
        }
    }


    /**
     * Start the list of named servers.
     *
     * @param serverNames list of names
     */
    private void startServers(List<String> serverNames)
    {
        if (serverNames != null)
        {
            for (String serverName : serverNames)
            {
                if (serverName != null)
                {
                    this.startServer(serverName);
                }
            }
        }
    }


    /**
     * Stop the requested server.    This will fail if the server or the platform is not running,
     * or if the user is not authorized to issue operations requests to the platform.
     *
     * @param serverName string name
     */
    private void stopServer(String serverName)
    {
        try
        {
            PlatformServicesClient client = new PlatformServicesClient(serverName, platformURLRoot, secretsStoreProvider, secretsStoreLocation, secretsStoreCollection, null, null);

            System.out.println("Stopping " + serverName + " ...");

            client.shutdownServer(serverName);

            System.out.println(serverName + " stopped.");
        }
        catch (Exception error)
        {
            System.out.println("There was an " + error.getClass().getName() + " exception when calling the platform.  Error message is: " + error.getMessage());
        }
    }


    /**
     * Stop the list of named servers.
     *
     * @param serverNames list of names
     */
    private void stopServers(List<String> serverNames)
    {
        if (serverNames != null)
        {
            for (String serverName : serverNames)
            {
                if (serverName != null)
                {
                    this.stopServer(serverName);
                }
            }
        }
    }


    /**
     * Query the status of an integration daemon.
     *
     * @param serverName name of the server
     */
    private void queryIntegrationDaemon(String serverName)
    {
        try
        {
            IntegrationDaemon client = new IntegrationDaemon(serverName, platformURLRoot, secretsStoreProvider, secretsStoreLocation, secretsStoreCollection, null, null);

            List<IntegrationGroupSummary> serviceSummaries = client.getIntegrationGroupSummaries();

            if (serviceSummaries != null)
            {
                System.out.println("  integration services: ");

                for (IntegrationGroupSummary serviceSummary : serviceSummaries)
                {
                    System.out.println("    integration group: " + serviceSummary.getIntegrationGroupName());

                    if (serviceSummary.getIntegrationConnectorReports() != null)
                    {
                        System.out.println("      connectors: " + serviceSummary.getIntegrationGroupName());

                        for (IntegrationConnectorReport connectorReport : serviceSummary.getIntegrationConnectorReports())
                        {
                            System.out.println("        connector: " + connectorReport.getConnectorName() + "(" + connectorReport.getConnectorInstanceId() + ")");
                            System.out.println("          status: " + connectorReport.getConnectorStatus());
                            System.out.println("          lastRefreshTime: " + connectorReport.getLastRefreshTime());
                            System.out.println("          lastStatusChange: " + connectorReport.getLastStatusChange());

                            if (connectorReport.getStatistics() != null)
                            {
                                System.out.println("          statistics: ");

                                for (String statisticName : connectorReport.getStatistics().keySet())
                                {
                                    System.out.println("            " + statisticName + ": " + connectorReport.getStatistics().get(statisticName));
                                }
                            }
                        }
                    }
                }
            }
            else
            {
                System.out.println(serverName + "  not running.");
            }
        }
        catch (Exception error)
        {
            System.out.println("There was an " + error.getClass().getName() + " exception when calling the platform.  Error message is: " + error.getMessage());
        }
    }

    /**
     * Query the status of a server.
     *
     * @param serverName name of the server
     */
    private void queryServer(String serverName)
    {
        try
        {
            PlatformServicesClient client = new PlatformServicesClient(serverName, platformURLRoot, secretsStoreProvider, secretsStoreLocation, secretsStoreCollection, null, null);

            System.out.println("Status of " + serverName + " ...");

            ServerServicesStatus serverStatus = client.getActiveServerStatus(serverName);

            if (serverStatus != null)
            {
                System.out.println("  serverType: " + serverStatus.getServerType());
                System.out.println("  status: " + serverStatus.getServerActiveStatus());

                if (ServerTypeClassification.INTEGRATION_DAEMON.getServerTypeName().equals(serverStatus.getServerType()))
                {
                    queryIntegrationDaemon(serverName);
                }
            }
            else
            {
                System.out.println(serverName + "  not running.");
            }
        }
        catch (Exception error)
        {
            System.out.println("There was an " + error.getClass().getName() + " exception when calling the platform.  Error message is: " + error.getMessage());
        }
    }


    /**
     * Query the list of named servers.
     *
     * @param serverNames list of names
     */
    private void queryServers(List<String> serverNames)
    {
        if (serverNames != null)
        {
            for (String serverName : serverNames)
            {
                if (serverName != null)
                {
                    this.queryServer(serverName);
                }
            }
        }
    }


    /**
     * Run the requested command.
     *
     * @param mode command
     * @param serverArray list of server names
     */
    private void runCommand(String   mode,
                            String[] serverArray)
    {
        List<String> serverList = null;

        if (serverArray != null)
        {
            serverList = Arrays.asList(serverArray);
        }

        if ("start".equals(mode))
        {
            this.startServers(serverList);
        }
        else if ("stop".equals(mode))
        {
            this.stopServers(serverList);
        }
        else if ("query".equals(mode))
        {
            this.queryServers(serverList);
        }
    }


    /**
     * Main program that controls the operation of the utility.  The parameters are passed space separated.
     * The parameters are used to override the utility's default values. If mode is set to "interactive"
     * the caller is prompted for a command and one to many server names.
     *
     * @param args 1. service platform URL root, 2. client userId, 3. mode 4. server name 5. server name ...
     */
    public static void main(String[] args)
    {
        final String interactiveMode = "interactive";
        final String endInteractiveMode = "exit";

        String platformURLRoot = "https://localhost:9443";
        String secretsStoreProvider = "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider";
        String secretsStoreLocation = "loading-bay/secrets/default.omsecrets";
        String secretsStoreCollection = "garygeeke";
        String mode = interactiveMode;

        if (args.length > 0)
        {
            platformURLRoot = args[0];
        }

        if (args.length > 1)
        {
            secretsStoreCollection = args[1];
        }

        if (args.length > 2)
        {
            secretsStoreLocation = args[2];
        }

        if (args.length > 3)
        {
            secretsStoreProvider = args[3];
        }

        System.out.println("===============================");
        System.out.println("OMAG Server Operations Utility:    " + new Date());
        System.out.println("===============================");
        System.out.print("Running against platform: " + platformURLRoot);

        ServerOps utility = new ServerOps(platformURLRoot, secretsStoreProvider, secretsStoreLocation, secretsStoreCollection);

        HttpHelper.noStrictSSL();

        String platformOrigin = utility.getPlatformOrigin();

        if (platformOrigin != null)
        {
            System.out.print(" - " + platformOrigin);
        }
        else
        {
            System.out.println();
            System.exit(-1);
        }

        System.out.println("Using secrets collection: " + secretsStoreCollection);
        System.out.println();


        try
        {
            ConfigurationManagementClient configurationManagementClient = new ConfigurationManagementClient(platformURLRoot, secretsStoreProvider, secretsStoreLocation, secretsStoreCollection, null, null);

            if (interactiveMode.equals(mode))
            {
                while (! endInteractiveMode.equals(mode))
                {
                    Set<OMAGServerConfig> configuredServers = configurationManagementClient.getAllServerConfigurations();
                    List<String>          configuredServerNames = new ArrayList<>();

                    if (configuredServers != null)
                    {
                        for (OMAGServerConfig serverConfig : configuredServers)
                        {
                            if (serverConfig != null)
                            {
                                configuredServerNames.add(serverConfig.getLocalServerName());
                            }
                        }
                    }

                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    System.out.println("Available servers: " + configuredServerNames);
                    System.out.println("Enter a command {start, query, stop, exit} along with one or more space separate server names. Press enter to execute request.");

                    String   command = br.readLine();
                    String[] commandWords = command.split(" ");

                    if (commandWords.length > 0)
                    {
                        mode = commandWords[0];

                        if (commandWords.length > 1)
                        {
                            utility.runCommand(mode,  Arrays.copyOfRange(commandWords, 1, commandWords.length));
                        }
                    }

                    System.out.println();
                }
            }
            else
            {
               utility.runCommand(mode, Arrays.copyOfRange(args, 3, args.length));
            }
        }
        catch (Exception  error)
        {
            System.out.println("Exception: " + error.getClass().getName() + " with message " + error.getMessage());
            System.exit(-1);
        }

        System.exit(0);
    }
}
