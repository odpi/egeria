/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.samples.assetdeploy;


import org.odpi.openmetadata.accessservices.itinfrastructure.client.EndpointManagerClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.PlatformManagerClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.ServerManagerClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.EndpointElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.SoftwareServerPlatformElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.EndpointProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.SoftwareServerPlatformProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.SoftwareServerProperties;
import org.odpi.openmetadata.adminservices.configuration.registration.ServerTypeClassification;
import org.odpi.openmetadata.http.HttpHelper;
import org.odpi.openmetadata.platformservices.client.PlatformServicesClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;


/**
 * AssetDeploy illustrates the use of the IT Infrastructure OMAS APIs to catalog different types of infrastructure.
 */
public class AssetDeploy
{
    private final String platformURLRoot;
    private final String clientUserId;
    private final String serverName;


    /**
     * Set up the parameters for the utility.
     *
     * @param platformURLRoot location of server
     * @param clientUserId userId to access the server
     */
    private AssetDeploy(String platformURLRoot,
                        String clientUserId,
                        String serverName)
    {
        this.platformURLRoot = platformURLRoot;
        this.clientUserId    = clientUserId;
        this.serverName      = serverName;
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
            PlatformServicesClient platformServicesClient = new PlatformServicesClient("MyPlatform", platformURLRoot);

            /*
             * This is the first call to the platform and determines the version of the software.
             * If the platform is not running, or the remote service is not an OMAG Server Platform,
             * the utility fails at this point.
             */
            return platformServicesClient.getPlatformOrigin(clientUserId);
        }
        catch (Exception error)
        {
            System.out.println("\n\nThere was an " + error.getClass().getName() + " exception when calling the platform.  Error message is: " + error.getMessage());
            System.out.println("Ensure the platform URl is correct and the platform is running");
        }

        return null;
    }


    /**
     * Create a platform with an endpoint.
     *
     * @param platformName string name
     * @param platformType type of platform
     * @param platformNetworkAddress URL to call the platform
     * @return platform GUID
     */
    private String  deployPlatform(String platformName,
                                   String platformType,
                                   String platformNetworkAddress)
    {
        try
        {
            System.out.println("Creating " + platformType + " platform " + platformName + " with endpoint " + platformNetworkAddress);

            PlatformManagerClient platformManagerClient = new PlatformManagerClient(serverName, platformURLRoot);

            System.out.println("Adding platform to " + serverName + " ...");

            SoftwareServerPlatformProperties platformProperties = new SoftwareServerPlatformProperties();

            platformProperties.setQualifiedName(platformType + ":" + platformName + ":" + platformNetworkAddress);
            platformProperties.setDisplayName(platformType + " " + platformName + " at " + platformNetworkAddress);

            String platformGUID = platformManagerClient.createSoftwareServerPlatform(clientUserId, null, null, false, platformProperties);

            if (platformGUID != null)
            {
                EndpointManagerClient endpointManagerClient = new EndpointManagerClient(serverName, platformURLRoot);

                EndpointProperties endpointProperties = new EndpointProperties();

                endpointProperties.setQualifiedName("Endpoint:" + platformNetworkAddress);
                endpointProperties.setAddress(platformNetworkAddress);

                String endpointGUID = endpointManagerClient.createEndpoint(clientUserId, null, null, platformGUID, endpointProperties);

                System.out.println("  New platform " + platformGUID + " with endpoint " + endpointGUID);

                List<EndpointElement> endpoints = endpointManagerClient.getEndpointsForInfrastructure(clientUserId, platformGUID, 0, 0);

                if (endpoints != null)
                {
                    for (EndpointElement endpoint : endpoints)
                    {
                        System.out.println("     Attached endpoint " + endpoint.getElementHeader().getGUID());
                    }
                }

                platformManagerClient.publishSoftwareServerPlatform(clientUserId, platformGUID);

                SoftwareServerPlatformElement retrievedPlatform = platformManagerClient.getSoftwareServerPlatformByGUID(clientUserId, platformGUID);

                if (retrievedPlatform.getElementHeader().getClassifications() != null)
                {
                    System.out.println("     New classifications " + retrievedPlatform.getElementHeader().getClassifications());
                }

                return platformGUID;
            }
        }
        catch (Exception error)
        {
            System.out.println("There was an " + error.getClass().getName() + " exception when calling the platform.  Error message is: " + error.getMessage());
            error.printStackTrace();
        }

        return null;
    }



    /**
     * Create a server deployed on a platform with an endpoint.
     *
     * @param softwarePlatformURL string URL for platform
     * @param softwareServerName string name
     * @param softwareServerType type of server
     * @return server GUID
     */
    private String  deployServer(String softwarePlatformURL,
                                 String softwareServerName,
                                 String softwareServerType)
    {
        try
        {
            System.out.println("Creating " + softwareServerType + " server " + softwareServerName + " deployed on platform " + softwarePlatformURL);

            ServerManagerClient serverManagerClient = new ServerManagerClient(serverName, platformURLRoot);

            System.out.println("Adding platform to " + softwareServerName + " ...");

            SoftwareServerProperties serverProperties = new SoftwareServerProperties();

            serverProperties.setQualifiedName(softwareServerType + ":" + softwareServerName);
            serverProperties.setDisplayName(softwareServerType + " " + softwareServerName);

            String serverGUID = serverManagerClient.createSoftwareServer(clientUserId, null, null, false, serverProperties);

            if (serverGUID != null)
            {
                PlatformManagerClient platformManagerClient = new PlatformManagerClient(serverName, platformURLRoot);

                List<SoftwareServerPlatformElement> platforms = platformManagerClient.findSoftwareServerPlatforms(clientUserId, softwarePlatformURL, null, 0, 0);

                if (platforms != null)
                {
                    for (SoftwareServerPlatformElement platform : platforms)
                    {
                        System.out.println("     Attaching to platform " + platform.getProperties().getDisplayName());

                        platformManagerClient.deployITAsset(clientUserId, null, null, false, serverGUID, platform.getElementHeader().getGUID(), null);
                    }
                }

                return serverGUID;
            }
        }
        catch (Exception error)
        {
            System.out.println("There was an " + error.getClass().getName() + " exception when calling the platform.  Error message is: " + error.getMessage());
            error.printStackTrace();
        }

        return null;
    }


    /**
     * Create a platform with an endpoint.
     *
     * @param platformName string name
     * @param platformType type of platform
     * @param platformNetworkAddress URL to call the platform
     */
    private void  quickTestPlatform(String platformName,
                                    String platformType,
                                    String platformNetworkAddress)
    {
        try
        {
            String platformGUID = deployPlatform(platformName, platformType, platformNetworkAddress);

            if (platformGUID != null)
            {
                String serverGUID = deployServer(platformNetworkAddress, "TestServer", ServerTypeClassification.METADATA_SERVER.getServerTypeName());
            }
        }
        catch (Exception error)
        {
            System.out.println("There was an " + error.getClass().getName() + " exception when testing the platform.  Error message is: " + error.getMessage());
            error.printStackTrace();
        }
    }


    /**
     * Run the requested command.
     *
     * @param mode command
     * @param url url of new IT infrastructure
     */
    private void runCommand(String mode,
                            String url,
                            String name)
    {
        if ("deploy-omag-platform".equals(mode))
        {
            this.deployPlatform(name, "OMAG Server Platform", url);
        }
        else if ("deploy-event-broker".equals(mode))
        {
            this.deployPlatform(name, "Apache Kafka Server", url);
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
        String clientUserId = "erinoverview";
        String serverName = "simple-metadata-store";
        String mode = interactiveMode;

        if (args.length > 0)
        {
            platformURLRoot = args[0];
        }

        if (args.length > 1)
        {
            clientUserId = args[1];
        }

        if (args.length > 2)
        {
            serverName = args[2];
        }

        if (args.length > 3)
        {
            mode = args[3];
        }

        System.out.println("===============================");
        System.out.println("Asset Deploy Utility:    " + new Date().toString());
        System.out.println("===============================");
        System.out.print("Running against platform: " + platformURLRoot);

        AssetDeploy utility = new AssetDeploy(platformURLRoot, clientUserId, serverName);

        HttpHelper.noStrictSSLIfConfigured();

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

        System.out.println("Focused on server: " + serverName);
        System.out.println("Using userId: " + clientUserId);
        System.out.println();

        try
        {
            if (interactiveMode.equals(mode))
            {
                while (! endInteractiveMode.equals(mode))
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    System.out.println("Enter a command {deploy-omag-platform, deploy-event-broker} along with its URL and Name. Press enter to execute request.");

                    String   command = br.readLine();
                    String[] commandWords = command.split(" ");

                    if (commandWords.length > 3)
                    {
                        mode = commandWords[0];
                        String url  = commandWords[1];

                        String name = null;

                        for (int i=2; i<commandWords.length; i++)
                        {
                            if (name == null)
                            {
                                name = "";
                            }
                            else
                            {
                                name = name + " ";
                            }
                            name = name + commandWords[i];
                        }

                        utility.runCommand(mode, url, name);
                    }

                    System.out.println();
                }
            }
            else if (args.length > 6)
            {
                String url  = args[5];

                String name = "";

                for (int i=6; i<args.length; i++)
                {
                    name = name + args[i];
                }

                utility.runCommand(mode, url, name);
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
