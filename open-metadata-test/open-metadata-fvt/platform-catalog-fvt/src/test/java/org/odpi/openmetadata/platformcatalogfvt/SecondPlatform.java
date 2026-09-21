/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformcatalogfvt;

import org.odpi.openmetadata.adminservices.client.IntegrationDaemonConfigurationClient;
import org.odpi.openmetadata.adminservices.client.MetadataAccessStoreConfigurationClient;
import org.odpi.openmetadata.adminservices.client.OMAGServerConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationGroupConfig;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.client.IntegrationDaemon;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationConnectorReport;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationGroupStatus;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationGroupSummary;
import org.odpi.openmetadata.platformservices.client.PlatformServicesClient;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SecondPlatform starts, configures and stops a second OMAG Server Platform in a JVM of its own, so that
 * {@link PlatformCatalogCohortFVT} has two genuinely separate platforms to work with.
 * <br>
 * A second platform cannot share the first's JVM: the platform holds its live servers in static registries,
 * so a second Spring context would find the first's servers rather than have any of its own.  This class
 * therefore launches {@code OMAGServerPlatform} as a subprocess, with the same classpath the test JVM has
 * (handed over by Gradle as a system property) and its own
 * {@code src/test/resources/platform-b.properties}.
 * <br>
 * What it stands up mirrors the first platform: a metadata access store with its own PostgreSQL repository,
 * and an integration daemon running the same Egeria integration group - which means a second instance of
 * the OMAG Server Platform Cataloguer, running against its own metadata access store.  The two stores join
 * one open metadata repository cohort, so each one's queries are federated across both repositories.  That
 * is the deployment the test is about.
 */
class SecondPlatform
{
    /**
     * Name of the metadata access store on the second platform.  It is deliberately a different name from
     * the first platform's: two servers of the same name in one cohort is a different problem from the one
     * this suite is about, and the repository would refuse the registration.
     */
    static final String METADATA_STORE_NAME = "platformCatalogFvtMetadataStoreB";

    /**
     * Name of the integration daemon on the second platform.
     */
    static final String INTEGRATION_DAEMON_NAME = "platformCatalogFvtIntegrationDaemonB";

    /**
     * Fixed local metadata collection id for the second store's repository, for the same reason the first
     * store has one.
     */
    private static final String METADATA_COLLECTION_ID = "3d9e7b41-6c2a-4f85-b0d7-1e5a8c3f9b62";

    private static final String REPOSITORY_SCHEMA = "repository_platformCatalogFvtMetadataStoreB";

    private static final List<String> CONTENT_PACK_FILES = List.of("OpenMetadataTypes.omarchive",
                                                                    "CoreContentPack.omarchive",
                                                                    "EgeriaContentPack.omarchive");

    private Process process         = null;
    private String  platformURLRoot = null;
    private File    logFile         = null;


    /**
     * Return the file the second platform's output - including its audit log - is written to.  It is the
     * only place the second connector's behaviour can be observed from the test JVM.
     *
     * @return log file
     */
    File getLogFile()
    {
        return logFile;
    }


    /**
     * Return the root URL of the second platform.
     *
     * @return URL root
     */
    String getPlatformURLRoot()
    {
        return platformURLRoot;
    }


    /**
     * Start the second platform and bring up its metadata access store and integration daemon.
     *
     * @throws Exception the platform or one of its servers did not start
     */
    void start() throws Exception
    {
        this.dropRepositorySchema();
        this.startPlatform();
        this.configureAndStartMetadataStore();
        this.configureAndStartIntegrationDaemon();
    }


    /**
     * Empty the second store's repository, for the same reason the first one is emptied: a run of this
     * suite is only meaningful against an ecosystem that has never been catalogued before.
     *
     * @throws Exception the schema could not be dropped
     */
    private void dropRepositorySchema() throws Exception
    {
        String databaseURL      = OMAGPlatformExtension.getStringProperty("platform.catalog.fvt.repository.url", "jdbc:postgresql://localhost:5442/egeria");
        String databaseUser     = OMAGPlatformExtension.getStringProperty("platform.catalog.fvt.repository.user", "egeria_user");
        String databasePassword = OMAGPlatformExtension.getStringProperty("platform.catalog.fvt.repository.password", "user4egeria");

        try (java.sql.Connection connection = java.sql.DriverManager.getConnection(databaseURL, databaseUser, databasePassword);
             java.sql.Statement statement = connection.createStatement())
        {
            statement.execute("DROP SCHEMA IF EXISTS " + REPOSITORY_SCHEMA + " CASCADE");
            statement.execute("CREATE SCHEMA " + REPOSITORY_SCHEMA);
        }
    }


    /**
     * Launch the platform as a subprocess and wait until it answers.
     *
     * @throws Exception the platform did not start
     */
    private void startPlatform() throws Exception
    {
        String classpath = System.getProperty("platform.catalog.fvt.classpath");

        if ((classpath == null) || (classpath.isBlank()))
        {
            throw new IllegalStateException("The test runtime classpath was not passed to this JVM."
                                                    + "  PlatformCatalogCohortFVT needs it to start a second platform,"
                                                    + " and it is set by the 'platform.catalog.fvt.classpath' system"
                                                    + " property in this module's build.gradle - so this suite is"
                                                    + " probably being run some way other than through Gradle.");
        }

        int port = allocateFreePort();

        platformURLRoot = "http://localhost:" + port;

        File logDirectory = new File("build/platform-catalog-fvt-data/platform-b");

        if (! logDirectory.isDirectory() && ! logDirectory.mkdirs())
        {
            throw new IllegalStateException("Could not create " + logDirectory.getAbsolutePath());
        }

        logFile = new File(logDirectory, "platform-b.log");

        ProcessBuilder processBuilder = new ProcessBuilder("java",
                                                            "-cp",
                                                            classpath,
                                                            "org.odpi.openmetadata.platformchassis.springboot.OMAGServerPlatform",
                                                            "--spring.config.location=classpath:/platform-b.properties",
                                                            "--server.port=" + port);

        processBuilder.redirectErrorStream(true);
        processBuilder.redirectOutput(logFile);

        process = processBuilder.start();

        System.out.println("platform-catalog-fvt: second platform starting at " + platformURLRoot
                                   + " (output in " + logFile.getPath() + ")");

        this.waitForPlatform(logFile);
    }


    /**
     * Wait until the second platform answers a request.
     *
     * @param logFile where the platform's output went, named in the failure message
     * @throws Exception the platform did not come up
     */
    private void waitForPlatform(File logFile) throws Exception
    {
        long timeoutMilliseconds = OMAGPlatformExtension.getLongProperty("platform.catalog.fvt.integration.daemon.timeout.seconds", 180) * 1000;
        long pollMilliseconds    = OMAGPlatformExtension.getLongProperty("platform.catalog.fvt.integration.daemon.poll.seconds", 2) * 1000;
        long giveUpTime          = System.currentTimeMillis() + timeoutMilliseconds;

        String lastKnownProblem = "no request attempted yet";

        while (System.currentTimeMillis() < giveUpTime)
        {
            if (! process.isAlive())
            {
                throw new IllegalStateException("The second platform's JVM exited with code " + process.exitValue()
                                                        + " before it started listening.  Its output is in "
                                                        + logFile.getPath() + ".");
            }

            try
            {
                String origin = this.getPlatformServicesClient().getPlatformOrigin();

                if ((origin != null) && (! origin.isBlank()))
                {
                    System.out.println("platform-catalog-fvt: second platform is up at " + platformURLRoot);
                    return;
                }
            }
            catch (Exception notReadyYet)
            {
                lastKnownProblem = notReadyYet.getClass().getSimpleName() + ": " + notReadyYet.getMessage();
            }

            Thread.sleep(pollMilliseconds);
        }

        throw new IllegalStateException("The second platform at " + platformURLRoot + " did not start within "
                                                + (timeoutMilliseconds / 1000) + " seconds (last problem: "
                                                + lastKnownProblem + ").  Its output is in " + logFile.getPath() + ".");
    }


    /**
     * Configure and start the second platform's metadata access store, and put it in the same cohort as the
     * first platform's.
     *
     * @throws Exception the server did not start
     */
    private void configureAndStartMetadataStore() throws Exception
    {
        MetadataAccessStoreConfigurationClient configurationClient = new MetadataAccessStoreConfigurationClient(METADATA_STORE_NAME,
                                                                                                               platformURLRoot,
                                                                                                               null,
                                                                                                               null,
                                                                                                               null,
                                                                                                               OMAGPlatformExtension.USER_ID,
                                                                                                               null);

        configurationClient.clearOMAGServerConfig();

        configurationClient.setServerUserId(OMAGPlatformExtension.USER_ID);
        this.setBasicServerProperties(configurationClient, "Metadata access store on the second platform.");

        Map<String, Object> storageProperties = new HashMap<>();

        storageProperties.put("databaseURL", "~{repositoryDatabaseURL}~?currentSchema=" + REPOSITORY_SCHEMA);
        storageProperties.put("databaseSchema", REPOSITORY_SCHEMA);
        storageProperties.put("secretsStore", "~{egeriaServersSecretsStore}~");
        storageProperties.put("secretsCollectionName", "~{repositorySecretCollectionName}~");

        configurationClient.setPostgreSQLLocalRepository(storageProperties);
        configurationClient.addConsoleAuditLogDestination(new ArrayList<>());

        this.setEventBus(configurationClient);

        configurationClient.configureAllAccessServices(new HashMap<>());
        configurationClient.setLocalMetadataCollectionId(METADATA_COLLECTION_ID);

        /*
         * The cohort is what makes this test what it is: with both stores in it, each one's queries are
         * federated across both repositories, and each connector can see what the other has catalogued.
         */
        configurationClient.addCohortRegistration(OMAGPlatformExtension.COHORT_NAME, null);

        for (String archiveFileName : CONTENT_PACK_FILES)
        {
            configurationClient.addStartUpOpenMetadataArchiveFile(new File(findContentPacksDirectory(), archiveFileName).getAbsolutePath());
        }

        PlatformServicesClient platformServicesClient = this.getPlatformServicesClient();

        platformServicesClient.activateWithStoredConfig(METADATA_STORE_NAME);

        if (! platformServicesClient.isServerKnown(METADATA_STORE_NAME))
        {
            throw new IllegalStateException("Server " + METADATA_STORE_NAME + " did not start on platform " + platformURLRoot);
        }
    }


    /**
     * Configure and start the second platform's integration daemon, running the same Egeria integration
     * group as the first - and so a second instance of the cataloguer.
     *
     * @throws Exception the server did not start
     */
    private void configureAndStartIntegrationDaemon() throws Exception
    {
        IntegrationDaemonConfigurationClient configurationClient = new IntegrationDaemonConfigurationClient(INTEGRATION_DAEMON_NAME,
                                                                                                           platformURLRoot,
                                                                                                           null,
                                                                                                           null,
                                                                                                           null,
                                                                                                           OMAGPlatformExtension.USER_ID,
                                                                                                           null);

        configurationClient.clearOMAGServerConfig();

        configurationClient.setServerUserId(OMAGPlatformExtension.USER_ID);
        this.setBasicServerProperties(configurationClient, "Integration daemon on the second platform.");

        configurationClient.addConsoleAuditLogDestination(new ArrayList<>());

        this.setEventBus(configurationClient);

        IntegrationGroupConfig integrationGroupConfig = new IntegrationGroupConfig();

        integrationGroupConfig.setIntegrationGroupQualifiedName(OMAGPlatformExtension.INTEGRATION_GROUP_QUALIFIED_NAME);
        integrationGroupConfig.setOMAGServerName(METADATA_STORE_NAME);
        integrationGroupConfig.setOMAGServerPlatformRootURL(platformURLRoot);

        configurationClient.configureIntegrationGroup(integrationGroupConfig);

        PlatformServicesClient platformServicesClient = this.getPlatformServicesClient();

        platformServicesClient.activateWithStoredConfig(INTEGRATION_DAEMON_NAME);

        if (! platformServicesClient.isServerKnown(INTEGRATION_DAEMON_NAME))
        {
            throw new IllegalStateException("Server " + INTEGRATION_DAEMON_NAME + " did not start on platform " + platformURLRoot);
        }
    }


    /**
     * Set the basic properties of one of the second platform's servers.  Note the organization name: it is
     * the same as the first platform's servers use, for the same reason the platform names match.
     *
     * @param configurationClient client for the server being configured
     * @param serverDescription description for the server
     * @throws Exception the properties could not be set
     */
    private void setBasicServerProperties(OMAGServerConfigurationClient configurationClient,
                                          String                        serverDescription) throws Exception
    {
        configurationClient.setBasicServerProperties(OMAGPlatformExtension.INITIAL_SERVER_ORGANIZATION_NAME,
                                                     serverDescription,
                                                     OMAGPlatformExtension.USER_ID,
                                                     "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider",
                                                     "build/platform-catalog-fvt-data/platform-b/secrets.omsecrets",
                                                     "platform-catalog-fvt",
                                                     platformURLRoot,
                                                     PlatformCatalogFvtTestSupport.MAX_PAGE_SIZE);
    }


    /**
     * Point a server's event bus at the same Kafka broker and topic root the first platform uses.  The
     * topic root has to match: it is how the two stores find each other's cohort topics.
     *
     * @param configurationClient client for the server being configured
     * @throws Exception the event bus could not be configured
     */
    private void setEventBus(OMAGServerConfigurationClient configurationClient) throws Exception
    {
        Map<String, Object> eventBusProperties = new HashMap<>();
        Map<String, Object> bootstrapServers   = new HashMap<>();

        bootstrapServers.put("bootstrap.servers", "~{kafkaEndpoint}~");

        eventBusProperties.put("producer", bootstrapServers);
        eventBusProperties.put("consumer", bootstrapServers);

        configurationClient.setEventBus("org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider",
                                        "egeria.omag.platform-catalog-fvt",
                                        eventBusProperties);
    }


    /**
     * Wait for the second platform's integration daemon to bring the Egeria integration group up.
     *
     * @throws Exception the integration group did not start
     */
    void waitForIntegrationGroup() throws Exception
    {
        IntegrationDaemon integrationDaemon = this.getIntegrationDaemonClient();

        long timeoutMilliseconds = OMAGPlatformExtension.getLongProperty("platform.catalog.fvt.integration.daemon.timeout.seconds", 180) * 1000;
        long pollMilliseconds    = OMAGPlatformExtension.getLongProperty("platform.catalog.fvt.integration.daemon.poll.seconds", 2) * 1000;
        long giveUpTime          = System.currentTimeMillis() + timeoutMilliseconds;

        String lastKnownStatus = "none reported";

        while (System.currentTimeMillis() < giveUpTime)
        {
            IntegrationGroupSummary summary = null;

            try
            {
                summary = integrationDaemon.getIntegrationGroupSummary(OMAGPlatformExtension.INTEGRATION_GROUP_QUALIFIED_NAME);
            }
            catch (Exception notReadyYet)
            {
                lastKnownStatus = "not registered with the daemon yet (" + notReadyYet.getMessage() + ")";
            }

            if (summary != null)
            {
                lastKnownStatus = String.valueOf(summary.getIntegrationGroupStatus());

                List<IntegrationConnectorReport> connectorReports = summary.getIntegrationConnectorReports();

                if ((summary.getIntegrationGroupStatus() == IntegrationGroupStatus.RUNNING)
                            && (connectorReports != null) && (! connectorReports.isEmpty()))
                {
                    return;
                }
            }

            Thread.sleep(pollMilliseconds);
        }

        throw new IllegalStateException("Integration group " + OMAGPlatformExtension.INTEGRATION_GROUP_QUALIFIED_NAME
                                                + " on the second platform did not reach RUNNING within "
                                                + (timeoutMilliseconds / 1000) + " seconds (last known status: "
                                                + lastKnownStatus + ").");
    }


    /**
     * Ask the second platform's integration daemon to refresh its cataloguer.
     *
     * @throws Exception the refresh failed
     */
    void refreshCataloguer() throws Exception
    {
        this.getIntegrationDaemonClient().refreshConnector(OMAGPlatformExtension.CATALOGUER_CONNECTOR_NAME);
    }


    /**
     * Return a platform services client for the second platform.
     *
     * @return client
     * @throws Exception problem creating the client
     */
    PlatformServicesClient getPlatformServicesClient() throws Exception
    {
        return new PlatformServicesClient("platform-catalog-fvt Platform B", platformURLRoot, null, null, null, OMAGPlatformExtension.USER_ID, null);
    }


    /**
     * Return an integration daemon client for the second platform.
     *
     * @return client
     * @throws Exception problem creating the client
     */
    private IntegrationDaemon getIntegrationDaemonClient() throws Exception
    {
        return new IntegrationDaemon(INTEGRATION_DAEMON_NAME, platformURLRoot, null, null, null, OMAGPlatformExtension.USER_ID, null);
    }


    /**
     * Shut the second platform down.  Best-effort: the JVM is destroyed either way.
     */
    void stop()
    {
        if (process != null)
        {
            for (String serverName : new String[]{INTEGRATION_DAEMON_NAME, METADATA_STORE_NAME})
            {
                try
                {
                    this.getPlatformServicesClient().shutdownServer(serverName);
                }
                catch (Exception ignoredShutdownFailure)
                {
                    // Best-effort - the process is about to be destroyed anyway.
                }
            }

            process.destroy();

            try
            {
                if (! process.waitFor(30, java.util.concurrent.TimeUnit.SECONDS))
                {
                    process.destroyForcibly();
                }
            }
            catch (InterruptedException interrupted)
            {
                process.destroyForcibly();
                Thread.currentThread().interrupt();
            }
        }
    }


    /**
     * Locate the repo's shared top-level {@code content-packs} directory by walking up from the current
     * working directory.
     *
     * @return the content-packs directory
     */
    private static File findContentPacksDirectory()
    {
        File candidate = new File(System.getProperty("user.dir")).getAbsoluteFile();

        for (int levelsUp = 0; levelsUp < 10; levelsUp++)
        {
            File contentPacksDirectory = new File(candidate, "content-packs");

            if (contentPacksDirectory.isDirectory())
            {
                return contentPacksDirectory;
            }

            File parent = candidate.getParentFile();

            if (parent == null)
            {
                break;
            }

            candidate = parent;
        }

        throw new IllegalStateException("Could not locate the repo's content-packs directory by walking up from "
                                                + System.getProperty("user.dir"));
    }


    /**
     * Find a port that is free right now.
     *
     * @return a currently free TCP port
     */
    private static int allocateFreePort()
    {
        try (java.net.ServerSocket socket = new java.net.ServerSocket(0))
        {
            socket.setReuseAddress(true);
            return socket.getLocalPort();
        }
        catch (java.io.IOException error)
        {
            throw new IllegalStateException("Could not allocate a free port for the second test platform", error);
        }
    }
}
