/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.samples;

import org.apache.log4j.varia.NullAppender;
import org.odpi.openmetadata.adminservices.client.MetadataServerConfigurationClient;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

import java.util.HashMap;
import java.util.Map;

/**
 * ConfigMetadataServerSample illustrates how to use the MetadataServerConfigurationClient to configure a Metadata Server.
 */
public class ConfigMetadataServerSample
{
    /*
     * These are the values used to configure the metadata server
     */
    static final String defaultAdminPlatformURLRoot = "http://localhost:8080";
    static final String defaultAdminUserId          = "garygeeke";

    static final String eventBusURLRoot = "localhost:9092";

    static final String metadataServerName          = "cocoMDS1";
    static final String metadataServerPlatform      = "http://localhost:8081";
    static final String metadataServerUserId        = "cocoMDS1npa";
    static final String metadataServerPassword      = "cocoMDS1passw0rd";
    static final String metadataCollectionName      = "Data Lake Catalog";
    static final String organizationName            = "Coco Pharmaceuticals";
    static final String cohortName                  = "cocoCohort";

    static final int   maxPageSize = 100;

    static final String securityConnectorProvider = "org.odpi.openmetadata.metadatasecurity.samples.CocoPharmaServerSecurityProvider";

    static final String[] supportedZones = { "quarantine", "clinical-trials", "research", "data-lake", "trash-can" };
    static final String[] defaultZones   = { "quarantine" };

    private MetadataServerConfigurationClient configurationClient = null;

    private ConfigMetadataServerSample(String adminPlatformURLRoot,
                                       String adminUserId) throws OMAGInvalidParameterException
    {
        System.out.println("=================================");
        System.out.println("Configure Metadata Server Sample ");
        System.out.println("=================================");
        System.out.println("Running against admin platform: " + adminPlatformURLRoot);
        System.out.println("Using admin userId: " + adminUserId);
        System.out.println("(No connection security)");
        System.out.println();
        System.out.println("Configuring server: " + metadataServerName);

        configurationClient = new MetadataServerConfigurationClient(adminUserId, metadataServerName, adminPlatformURLRoot);
    }

    private ConfigMetadataServerSample(String adminPlatformURLRoot,
                                       String adminUserId,
                                       String connectionUserId,
                                       String connectionPassword)
    {
        System.out.println("=================================");
        System.out.println("Configure Metadata Server Sample ");
        System.out.println("=================================");
        System.out.println("Running against admin platform: " + adminPlatformURLRoot);
        System.out.println("Using admin userId: " + adminUserId);
        System.out.println("Using connection userId: " + connectionUserId);
        System.out.println("Using connection password: " + connectionPassword);
        System.out.println();
        System.out.println("Configuring server: " + metadataServerName);

    }


    void run() throws OMAGNotAuthorizedException,
                      OMAGInvalidParameterException,
                      OMAGConfigurationErrorException
    {
        configurationClient.setServerURLRoot(metadataServerPlatform);
        configurationClient.setMaxPageSize(maxPageSize);
        configurationClient.setServerType(null);
        configurationClient.setOrganizationName(organizationName);
        configurationClient.setServerUserId(metadataServerUserId);
        configurationClient.setServerPassword(metadataServerPassword);

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

        configurationClient.setEventBus(null, null, configurationProperties);

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
        configurationClient.configureAccessService("data-platform", accessServiceOptions);
        configurationClient.configureAccessService("information-view", accessServiceOptions);

        accessServiceOptions = new HashMap<>();
        accessServiceOptions.put("KarmaPointPlateau", 500);

        configurationClient.configureAccessService("community-profile", accessServiceOptions);

        System.out.println();
        System.out.println("Configuration complete");
        System.out.println("  Local Metadata collection id: " + configurationClient.getLocalMetadataCollectionId());
        System.out.println("  Server Type: " + configurationClient.getServerClassification().toString());
        System.out.println("  Full configuration: " + configurationClient.getOMAGServerConfig().toString());
    }



    /**
     * Main program that controls the operation of the sample.  The parameters are passed space separated.
     * The parameters are used to override the sample's default values.
     *
     * @param args 1. URL root for the admin platform, 2. admin userId  3. connection userId 4. connection password
     */
    public static void main(String[] args)
    {
        org.apache.log4j.BasicConfigurator.configure(new NullAppender());

        String  serverURLRoot = defaultAdminPlatformURLRoot;
        String  adminUserId   = defaultAdminUserId;

        if (args.length > 0)
        {
            serverURLRoot = args[0];
        }

        if (args.length > 1)
        {
            adminUserId = args[1];
        }

        if (args.length > 3)
        {
            try
            {
                ConfigMetadataServerSample sample = new ConfigMetadataServerSample(serverURLRoot, adminUserId, args[2], args[3]);

                sample.run();
            }
            catch (Throwable  error)
            {
                System.out.println("Exception: " + error.getClass().getName() + " with message " + error.getMessage());
                System.exit(-1);
            }
        }
        else
        {
            try
            {
                ConfigMetadataServerSample sample = new ConfigMetadataServerSample(serverURLRoot, adminUserId);

                sample.run();
            }
            catch (Throwable  error)
            {
                System.out.println("Exception: " + error.getClass().getName() + " with message " + error.getMessage());
                System.exit(-1);
            }
        }
    }
}
