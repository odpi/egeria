/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.samples.configure;

import org.odpi.openmetadata.adminservices.client.MetadataAccessStoreConfigurationClient;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.http.HttpHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * ConfigMetadataServerSample illustrates how to use the MetadataAccessStoreConfigurationClient
 * to configure a Metadata Server.
 */
public class ConfigMetadataServerSample
{
    /*
     * These are the values used to configure the metadata server
     */
    private static final String defaultAdminPlatformURLRoot = "https://localhost:9443";
    private static final String eventBusURLRoot             = "localhost:9092";
    private static final String metadataServerName          = "cocoMDS10";
    private static final String metadataServerPlatform      = "https://localhost:9444";
    private static final String metadataServerUserId        = "cocoMDS1npa";
    private static final String metadataCollectionName      = "Data Lake Catalog";
    private static final String organizationName            = "Coco Pharmaceuticals";
    private static final String cohortName                  = "cocoCohort";
    private static final String topicRoot                   = "egeria";

    private static final int   maxPageSize = 100;

    private static final String securityConnectorProvider = "org.odpi.openmetadata.metadatasecurity.accessconnector.OpenMetadataAccessSecurityProvider";

    private static final String[] supportedZones = { "quarantine", "clinical-trials", "research", "data-lake", "trash-can" };
    private static final String[] defaultZones   = { "quarantine" };

    private final MetadataAccessStoreConfigurationClient configurationClient;

    private final String secretsStoreProvider;
    private final String secretsStoreLocation;
    private final String secretsStoreCollection;


    /**
     * Create a configuration client that does not have any security in the HTTP header.
     *
     * @param adminPlatformURLRoot root URL where the platform is registered
     * @param secretsStoreProvider secrets store connector for bearer token
     * @param secretsStoreLocation secrets store location for bearer token
     * @param secretsStoreCollection secrets store collection for bearer token
     * @throws InvalidParameterException one of the parameters is invalid
     */
    private ConfigMetadataServerSample(String adminPlatformURLRoot,
                                       String secretsStoreProvider,
                                       String secretsStoreLocation,
                                       String secretsStoreCollection) throws InvalidParameterException
    {
        System.out.println("=================================");
        System.out.println("Configure Metadata Server Sample ");
        System.out.println("=================================");
        System.out.println("Running against admin platform: " + adminPlatformURLRoot);
        System.out.println("Using secrets store collection: " + secretsStoreCollection);
        System.out.println("(No connection security)");
        System.out.println();
        System.out.println("Configuring server: " + metadataServerName);

        this.secretsStoreProvider   = secretsStoreProvider;
        this.secretsStoreLocation   = secretsStoreLocation;
        this.secretsStoreCollection = secretsStoreCollection;

        this.configurationClient = new MetadataAccessStoreConfigurationClient(metadataServerName, adminPlatformURLRoot, secretsStoreProvider, secretsStoreLocation, secretsStoreCollection, null);
    }


    /**
     * Run the configuration - no exceptions are expected.
     *
     * @throws UserNotAuthorizedException admin userId is not authorized to the platform
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws OMAGConfigurationErrorException problem with the configuration server
     */
    private void run() throws UserNotAuthorizedException,
                              InvalidParameterException,
                              OMAGConfigurationErrorException
    {
        configurationClient.setBasicServerProperties(organizationName,
                                                     null,
                                                     metadataServerUserId,
                                                     secretsStoreProvider,
                                                     secretsStoreLocation,
                                                     secretsStoreCollection,
                                                     metadataServerPlatform,
                                                     maxPageSize);

        Connection     connection    = new Connection();
        ConnectorType  connectorType = new ConnectorType();

        connectorType.setConnectorProviderClassName(securityConnectorProvider);
        connection.setConnectorType(connectorType);

        configurationClient.setServerSecurityConnection(connection);

        Map<String, Object> configurationProperties = new HashMap<>();
        Map<String, Object> kafkaProperties = new HashMap<>();

        kafkaProperties.put("bootstrap.servers", eventBusURLRoot);

        configurationProperties.put("producer", kafkaProperties);
        configurationProperties.put("consumer", kafkaProperties);

        configurationClient.setEventBus(null, topicRoot, configurationProperties);

        configurationClient.setInMemLocalRepository();
        configurationClient.setLocalMetadataCollectionName(metadataCollectionName);

        configurationClient.addCohortRegistration(cohortName, null);

        Map<String, Object>  accessServiceOptions = new HashMap<>();

        accessServiceOptions.put("SupportedZones", supportedZones);

        configurationClient.configureAccessService("asset-catalog", accessServiceOptions);
        configurationClient.configureAccessService("asset-consumer", accessServiceOptions);

        accessServiceOptions.put("DefaultZones", defaultZones);

        configurationClient.configureAccessService("asset-owner", accessServiceOptions);
        configurationClient.configureAccessService("glossary-view");
        configurationClient.configureAccessService("discovery-engine", accessServiceOptions);
        configurationClient.configureAccessService("stewardship-action", accessServiceOptions);
        configurationClient.configureAccessService("data-engine", accessServiceOptions);
        configurationClient.configureAccessService("data-manager", accessServiceOptions);

        accessServiceOptions = new HashMap<>();
        accessServiceOptions.put("KarmaPointPlateau", 500);

        configurationClient.configureAccessService("community-profile", accessServiceOptions);

        System.out.println();
        System.out.println("Configuration complete");
        System.out.println("  Local Metadata collection id: " + configurationClient.getLocalMetadataCollectionId());
        System.out.println("  Server classification:        " + configurationClient.getServerTypeClassification().getServerTypeName());
        System.out.println("  Cohort topic:                 " + configurationClient.getCohortTopicName(cohortName));
        System.out.println("  Access Service topics:        " + configurationClient.getAllAccessServiceTopicNames());
        System.out.println("  Full configuration:           " + configurationClient.getOMAGServerConfig().toString());
    }



    /**
     * Main program that controls the operation of the sample.  The parameters are passed space separated.
     * The parameters are used to override the sample's default values.
     *
     * @param args 1. URL root for the admin platform, 2. admin secret collection 3. secret store location 4. secret store provider
     */
    public static void main(String[] args)
    {
        String secretsStoreProvider = "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider";
        String secretsStoreLocation = "loading-bay/secrets/default.omsecrets";
        String secretsStoreCollection = "garygeeke";

        String  serverURLRoot = defaultAdminPlatformURLRoot;

        if (args.length > 0)
        {
            serverURLRoot = args[0];
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

        HttpHelper.noStrictSSL();

        try
        {
            ConfigMetadataServerSample sample = new ConfigMetadataServerSample(serverURLRoot, secretsStoreProvider, secretsStoreLocation, secretsStoreCollection);

            sample.run();
        }
        catch (Exception  error)
        {
            System.out.println("Exception: " + error.getClass().getName() + " with message " + error.getMessage());
            System.exit(-1);
        }
    }
}
