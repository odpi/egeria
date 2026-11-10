/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.pasteurfvt;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.odpi.openmetadata.adminservices.client.IntegrationDaemonConfigurationClient;
import org.odpi.openmetadata.adminservices.client.MetadataAccessStoreConfigurationClient;
import org.odpi.openmetadata.adminservices.client.OMAGServerConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationGroupConfig;
import org.odpi.openmetadata.contentpacks.core.IntegrationConnectorDefinition;
import org.odpi.openmetadata.contentpacks.core.IntegrationGroupDefinition;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.client.IntegrationDaemon;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationConnectorReport;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationConnectorStatus;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationGroupStatus;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationGroupSummary;
import org.odpi.openmetadata.platformchassis.springboot.OMAGServerPlatform;
import org.odpi.openmetadata.platformservices.client.PlatformServicesClient;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OMAGPlatformExtension starts a single OMAG Server Platform in-process for the whole pasteur-fvt run and
 * stands up the deployment that Pasteur runs in:
 * <ul>
 *     <li><b>{@value #METADATA_STORE_NAME}</b> - a metadata access store with a PostgreSQL local repository
 *     and its access services publishing to a real Apache Kafka broker.  Kafka is not optional here: the
 *     integration daemon learns about its configuration from the out topics.  It loads the open metadata
 *     types and the core content pack, which is where Pasteur's own definitions live.  The actions that
 *     Pasteur cleans up are created by the tests themselves.</li>
 *     <li><b>{@value #INTEGRATION_DAEMON_NAME}</b> - an integration daemon running the core content pack's
 *     Pasteur integration group, which is where the Pasteur Action History Cleanup runs.</li>
 * </ul>
 * It follows the JUnit 5 "singleton resource" pattern: the first test class that is extended with this class
 * pays the one-off startup cost in its {@code @BeforeAll}; every other extended class reuses the same running
 * platform and servers.
 * <br>
 * Everything the suite reaches out to - the PostgreSQL server, the Kafka broker, the port it listens on - is
 * configured from {@code src/test/resources/application.properties}.
 */
public class OMAGPlatformExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource
{
    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(OMAGPlatformExtension.class);
    private static final String                     STORE_KEY = OMAGPlatformExtension.class.getName();

    /**
     * Name of the metadata access store server that holds the actions under test.
     */
    public static final String METADATA_STORE_NAME = "pasteurFvtMetadataStore";

    /**
     * Name of the integration daemon that runs the Pasteur Action History Cleanup.
     */
    public static final String INTEGRATION_DAEMON_NAME = "pasteurFvtIntegrationDaemon";

    /**
     * UserId used for all admin, platform and metadata calls made by the pasteur-fvt suite.
     */
    public static final String USER_ID = "pasteurfvtuser";

    /**
     * The integration group that Pasteur belongs to, and the connector's name within it.  Both come from the
     * content pack definitions rather than being spelled out here, so that renaming either in the content
     * pack cannot leave this suite quietly looking for something that no longer exists.
     */
    public static final String INTEGRATION_GROUP_QUALIFIED_NAME = IntegrationGroupDefinition.PASTEUR.getQualifiedName();

    /**
     * The name Pasteur is registered under in that group - this is what the tests pass to
     * {@code refreshConnector}.
     */
    public static final String PASTEUR_CONNECTOR_NAME = IntegrationConnectorDefinition.PASTEUR_ACTION_HISTORY_CLEANUP.getConnectorName();

    /**
     * Fixed local metadata collection id for the store's repository.  This is deliberately stable (rather
     * than auto-generated afresh on every run) because the underlying PostgreSQL schema persists across runs
     * - keeping the collection id constant means metadata created by an earlier run is still recognised as
     * belonging to "this" repository on a later run.
     */
    private static final String METADATA_COLLECTION_ID = "7c1e4b9a-2d63-4f80-a5e7-3b9d0c6f8a21";

    /**
     * The open metadata archives loaded at start-up, in dependency order.  The core content pack is the one
     * that carries Pasteur's connector definition, integration group and default configuration.
     */
    private static final List<String> CONTENT_PACK_FILES = List.of("OpenMetadataTypes.omarchive",
                                                                    "CoreContentPack.omarchive");

    private static volatile boolean               started = false;
    private static ConfigurableApplicationContext platformContext;
    private static String                         platformURLRoot;


    /**
     * Return the root URL of the running platform.  The port is allocated at run time rather than
     * fixed, so it differs from one run to the next.
     *
     * @return URL root
     */
    public static String getPlatformURLRoot()
    {
        return platformURLRoot;
    }


    /**
     * Return a client for the integration daemon, used to drive Pasteur's refresh from the tests rather than
     * waiting out its configured refresh interval.
     *
     * @return integration daemon client
     * @throws Exception problem creating the client
     */
    public static IntegrationDaemon getIntegrationDaemonClient() throws Exception
    {
        return new IntegrationDaemon(INTEGRATION_DAEMON_NAME, platformURLRoot, null, null, null, USER_ID, null);
    }


    /**
     * Ask the integration daemon to refresh Pasteur now, and wait for the refresh to finish.  The daemon's refresh
     * call is synchronous, but only when the connector is idle: if a refresh is already in progress - such as the
     * first refresh, which the daemon starts itself as soon as the connector is running - the call returns at once
     * without refreshing.  So this waits for Pasteur to be idle, having completed its first refresh, before asking,
     * and again afterwards in case the daemon's own refresh started in between.  When this returns, the connector
     * has worked through every type of action in its configuration.
     *
     * @throws Exception the refresh failed, or Pasteur did not become idle in time
     */
    public static void refreshPasteur() throws Exception
    {
        IntegrationDaemon integrationDaemon = getIntegrationDaemonClient();

        waitUntilPasteurIsIdle(integrationDaemon, 0);

        integrationDaemon.refreshConnector(PASTEUR_CONNECTOR_NAME);

        waitUntilPasteurIsIdle(integrationDaemon, 0);
    }


    /**
     * Merge new values into Pasteur's configuration properties and wait for it to finish the refresh it runs
     * with them.  Updating the configuration properties restarts the connector, and the daemon refreshes it as
     * soon as it is running again - so this waits for a refresh that started after the update, rather than
     * asking for one, which could be refused while the connector is starting.
     *
     * @param configurationProperties property names and values to merge
     * @throws Exception the update failed, or Pasteur did not finish a refresh in time
     */
    public static void updatePasteurConfiguration(Map<String, Object> configurationProperties) throws Exception
    {
        IntegrationDaemon integrationDaemon = getIntegrationDaemonClient();

        long updateTime = System.currentTimeMillis();

        integrationDaemon.updateConfigurationProperties(PASTEUR_CONNECTOR_NAME, true, configurationProperties);

        waitUntilPasteurIsIdle(integrationDaemon, updateTime);
    }


    /**
     * Wait until Pasteur has completed a refresh since the given time and is not refreshing now.
     *
     * @param integrationDaemon client for the integration daemon
     * @param refreshedSince the refresh must be later than this time in milliseconds - 0 accepts any refresh
     * @throws Exception Pasteur failed, or did not become idle within the integration daemon timeout
     */
    private static void waitUntilPasteurIsIdle(IntegrationDaemon integrationDaemon,
                                               long              refreshedSince) throws Exception
    {
        long timeoutMilliseconds = getLongProperty("pasteur.fvt.integration.daemon.timeout.seconds", 180) * 1000;
        long pollMilliseconds    = 250;
        long giveUpTime          = System.currentTimeMillis() + timeoutMilliseconds;

        String lastKnownStatus = "none reported";

        while (System.currentTimeMillis() < giveUpTime)
        {
            IntegrationGroupSummary summary = integrationDaemon.getIntegrationGroupSummary(INTEGRATION_GROUP_QUALIFIED_NAME);

            if ((summary != null) && (summary.getIntegrationConnectorReports() != null))
            {
                for (IntegrationConnectorReport connectorReport : summary.getIntegrationConnectorReports())
                {
                    if (PASTEUR_CONNECTOR_NAME.equals(connectorReport.getConnectorName()))
                    {
                        if (connectorReport.getFailingExceptionMessage() != null)
                        {
                            throw new IllegalStateException("Pasteur failed: " + connectorReport.getFailingExceptionMessage());
                        }

                        lastKnownStatus = connectorReport.getConnectorStatus() + ", last refresh " + connectorReport.getLastRefreshTime();

                        if ((connectorReport.getConnectorStatus() != IntegrationConnectorStatus.REFRESHING) &&
                                (connectorReport.getLastRefreshTime() != null) &&
                                (connectorReport.getLastRefreshTime().getTime() > refreshedSince))
                        {
                            return;
                        }
                    }
                }
            }

            Thread.sleep(pollMilliseconds);
        }

        throw new IllegalStateException("Pasteur did not finish refreshing within " + (timeoutMilliseconds / 1000)
                                                + " seconds (last known status: " + lastKnownStatus + ")");
    }


    /**
     * Return a numeric setting from the suite's application.properties, falling back to a default.
     *
     * @param propertyName name of the property
     * @param defaultValue value to use if it is not set
     * @return the value
     */
    static long getLongProperty(String propertyName,
                                long   defaultValue)
    {
        if (platformContext != null)
        {
            String value = platformContext.getEnvironment().getProperty(propertyName);

            if ((value != null) && (! value.isBlank()))
            {
                return Long.parseLong(value.trim());
            }
        }

        return defaultValue;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void beforeAll(ExtensionContext context) throws Exception
    {
        if (! started)
        {
            synchronized (OMAGPlatformExtension.class)
            {
                if (! started)
                {
                    startPlatform();
                    configureAndStartMetadataStore();
                    configureAndStartIntegrationDaemon();
                    waitForIntegrationGroup();

                    started = true;

                    context.getRoot().getStore(NAMESPACE).put(STORE_KEY, this);
                }
            }
        }
    }


    /**
     * Start the OMAG Server Platform's Spring Boot application in-process.  All of the Spring Boot
     * configuration comes from this module's classpath {@code application.properties}.
     */
    private void startPlatform()
    {
        /*
         * Allocate a free port before the platform starts, rather than binding a fixed one from
         * application.properties.  A fixed port means a second checkout of Egeria running this same suite
         * fails with PortInUseException, and the failure looks like a broken test rather than a clash.
         * The port is passed in as a property so that ${server.port} in application.properties - notably
         * the egeriaEndpoint placeholder, which becomes the server's own localServerURL - resolves to the
         * port actually in use.
         */
        SpringApplicationBuilder builder = new SpringApplicationBuilder(OMAGServerPlatform.class);

        builder.properties(java.util.Map.of("server.port", Integer.toString(allocateFreePort())));

        builder.web(WebApplicationType.SERVLET);

        platformContext = builder.run();

        int port = ((ServletWebServerApplicationContext) platformContext).getWebServer().getPort();

        platformURLRoot = "http://localhost:" + port;
    }


    /**
     * Configure and start the metadata access store that holds the actions under test.
     *
     * @throws Exception any problem configuring or starting the server is fatal to the whole run
     */
    private void configureAndStartMetadataStore() throws Exception
    {
        PlatformServicesClient platformServicesClient = getPlatformServicesClient();

        String origin = platformServicesClient.getPlatformOrigin();

        if ((origin == null) || (origin.isBlank()))
        {
            throw new IllegalStateException("OMAG Server Platform at " + platformURLRoot + " did not return an origin response");
        }

        MetadataAccessStoreConfigurationClient configurationClient = new MetadataAccessStoreConfigurationClient(METADATA_STORE_NAME,
                                                                                                                platformURLRoot,
                                                                                                                null,
                                                                                                                null,
                                                                                                                null,
                                                                                                                USER_ID,
                                                                                                                null);

        configurationClient.clearOMAGServerConfig();

        configurationClient.setServerUserId(USER_ID);
        configurationClient.setBasicServerProperties("Egeria pasteur-fvt",
                                                      "Metadata access store holding the actions that pasteur-fvt cleans up.",
                                                      USER_ID,
                                                      "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider",
                                                      "build/pasteur-fvt-data/secrets.omsecrets",
                                                      "pasteur-fvt",
                                                      platformURLRoot,
                                                      ConnectorContextFactory.MAX_PAGE_SIZE);

        Map<String, Object> storageProperties = new HashMap<>();

        storageProperties.put("databaseURL", "~{repositoryDatabaseURL}~?currentSchema=repository_" + METADATA_STORE_NAME);
        storageProperties.put("databaseSchema", "repository_" + METADATA_STORE_NAME);
        storageProperties.put("secretsStore", "~{egeriaServersSecretsStore}~");
        storageProperties.put("secretsCollectionName", "~{repositorySecretCollectionName}~");

        configurationClient.setPostgreSQLLocalRepository(storageProperties);
        configurationClient.addConsoleAuditLogDestination(new ArrayList<>());

        setEventBus(configurationClient);

        /*
         * The access services are configured WITH their out topics.  That is the point of running Kafka
         * here: the integration daemon learns about its integration group from them.
         */
        configurationClient.configureAllAccessServices(new HashMap<>());

        configurationClient.setLocalMetadataCollectionId(METADATA_COLLECTION_ID);

        for (String archiveFileName : CONTENT_PACK_FILES)
        {
            configurationClient.addStartUpOpenMetadataArchiveFile(new File(findContentPacksDirectory(), archiveFileName).getAbsolutePath());
        }

        platformServicesClient.activateWithStoredConfig(METADATA_STORE_NAME);

        if (! platformServicesClient.isServerKnown(METADATA_STORE_NAME))
        {
            throw new IllegalStateException("Server " + METADATA_STORE_NAME + " did not start on platform " + platformURLRoot);
        }
    }


    /**
     * Configure and start the integration daemon that runs Pasteur.
     *
     * @throws Exception any problem configuring or starting the server is fatal to the whole run
     */
    private void configureAndStartIntegrationDaemon() throws Exception
    {
        IntegrationDaemonConfigurationClient configurationClient = new IntegrationDaemonConfigurationClient(INTEGRATION_DAEMON_NAME,
                                                                                                            platformURLRoot,
                                                                                                            null,
                                                                                                            null,
                                                                                                            null,
                                                                                                            USER_ID,
                                                                                                            null);

        configurationClient.clearOMAGServerConfig();

        configurationClient.setServerUserId(USER_ID);
        configurationClient.setBasicServerProperties("Egeria pasteur-fvt",
                                                      "Integration daemon running the Pasteur Action History Cleanup.",
                                                      USER_ID,
                                                      "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider",
                                                      "build/pasteur-fvt-data/secrets.omsecrets",
                                                      "pasteur-fvt",
                                                      platformURLRoot,
                                                      ConnectorContextFactory.MAX_PAGE_SIZE);

        configurationClient.addConsoleAuditLogDestination(new ArrayList<>());

        setEventBus(configurationClient);

        IntegrationGroupConfig integrationGroupConfig = new IntegrationGroupConfig();

        integrationGroupConfig.setIntegrationGroupQualifiedName(INTEGRATION_GROUP_QUALIFIED_NAME);
        integrationGroupConfig.setOMAGServerName(METADATA_STORE_NAME);
        integrationGroupConfig.setOMAGServerPlatformRootURL(platformURLRoot);

        configurationClient.configureIntegrationGroup(integrationGroupConfig);

        PlatformServicesClient platformServicesClient = getPlatformServicesClient();

        platformServicesClient.activateWithStoredConfig(INTEGRATION_DAEMON_NAME);

        if (! platformServicesClient.isServerKnown(INTEGRATION_DAEMON_NAME))
        {
            throw new IllegalStateException("Server " + INTEGRATION_DAEMON_NAME + " did not start on platform " + platformURLRoot);
        }
    }


    /**
     * Point a server's event bus at the Kafka broker named in application.properties.  The topic root is
     * unique to this suite so that its events cannot be confused with, or consumed by, anything else using
     * the same broker.
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
                                          "egeria.omag.pasteur-fvt",
                                          eventBusProperties);
    }


    /**
     * Wait for the integration daemon to bring the Pasteur integration group up.  A connector that failed to
     * start reports its own exception message, which is repeated here: the usual cause is the connector's
     * module missing from this suite's test runtime classpath, which is not a compile failure but leaves
     * every test with nothing to assert against.
     *
     * @throws Exception the integration group did not start
     */
    private void waitForIntegrationGroup() throws Exception
    {
        IntegrationDaemon integrationDaemon = getIntegrationDaemonClient();

        long timeoutMilliseconds = getLongProperty("pasteur.fvt.integration.daemon.timeout.seconds", 180) * 1000;
        long pollMilliseconds    = getLongProperty("pasteur.fvt.integration.daemon.poll.seconds", 2) * 1000;
        long giveUpTime          = System.currentTimeMillis() + timeoutMilliseconds;

        String lastKnownStatus = "none reported";

        while (System.currentTimeMillis() < giveUpTime)
        {
            IntegrationGroupSummary summary = null;

            try
            {
                summary = integrationDaemon.getIntegrationGroupSummary(INTEGRATION_GROUP_QUALIFIED_NAME);
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
                    for (IntegrationConnectorReport connectorReport : connectorReports)
                    {
                        if (connectorReport.getFailingExceptionMessage() != null)
                        {
                            throw new IllegalStateException("Integration connector " + connectorReport.getConnectorName() + " in group "
                                                                    + INTEGRATION_GROUP_QUALIFIED_NAME + " failed to start: "
                                                                    + connectorReport.getFailingExceptionMessage()
                                                                    + ".  If this is a class loading problem, the nanny-connectors module is"
                                                                    + " missing from pasteur-fvt's test runtime classpath.");
                        }
                    }

                    System.out.println("pasteur-fvt: integration group " + INTEGRATION_GROUP_QUALIFIED_NAME + " running with "
                                               + connectorReports.size() + " connector(s)");
                    return;
                }
            }

            Thread.sleep(pollMilliseconds);
        }

        throw new IllegalStateException("Integration group " + INTEGRATION_GROUP_QUALIFIED_NAME + " on " + INTEGRATION_DAEMON_NAME
                                                + " did not reach RUNNING with connectors within " + (timeoutMilliseconds / 1000)
                                                + " seconds (last known status: " + lastKnownStatus + ").  Check that the core content"
                                                + " pack loaded into " + METADATA_STORE_NAME + ".");
    }


    /**
     * Return a platform services client for this suite's platform.
     *
     * @return client
     * @throws Exception problem creating the client
     */
    private static PlatformServicesClient getPlatformServicesClient() throws Exception
    {
        return new PlatformServicesClient("pasteur-fvt Platform", platformURLRoot, null, null, null, USER_ID, null);
    }


    /**
     * Locate the repo's shared top-level {@code content-packs} directory by walking up from the current
     * working directory.  A fixed relative path is not reliable: Gradle's test worker process does not
     * always use this module's project directory as its working directory.
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

        throw new IllegalStateException("Could not locate the repo's content-packs directory by walking up from " +
                                                System.getProperty("user.dir") +
                                                " - is this suite being run from somewhere outside the egeria repository checkout?");
    }


    /*
     * Shutting down is bounded.  A server whose shutdown never returns - a deadlock inside it, for example - used
     * to hang the whole Gradle run in close(), because the REST call has no timeout.  Each step now runs on a
     * daemon thread with a limit, and close() fails with a message saying what hung if any step overran, so the
     * problem is reported as a failure rather than as a run that never finishes.
     */
    private static final long SHUTDOWN_TIMEOUT_SECONDS = Long.getLong("fvt.shutdown.timeout.seconds", 120L);

    private static volatile String shutdownTimeoutFailure = null;


    /**
     * A shutdown step that may throw.
     */
    @FunctionalInterface
    private interface ShutdownStep
    {
        void run() throws Exception;
    }


    /**
     * Run one shutdown step, waiting at most {@link #SHUTDOWN_TIMEOUT_SECONDS} for it.  An overrun is recorded so
     * that close() can report it once the remaining steps have had their chance to run.
     *
     * @param description what is being shut down, for the failure message
     * @param step the shutdown step
     * @throws Exception the step failed or overran
     */
    private static void runWithShutdownTimeout(String       description,
                                               ShutdownStep step) throws Exception
    {
        java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newSingleThreadExecutor(runnable ->
        {
            Thread thread = new Thread(runnable, "fvt-shutdown: " + description);
            thread.setDaemon(true);
            return thread;
        });

        try
        {
            executor.submit(() -> { step.run(); return null; }).get(SHUTDOWN_TIMEOUT_SECONDS, java.util.concurrent.TimeUnit.SECONDS);
        }
        catch (java.util.concurrent.TimeoutException overrun)
        {
            String message = "Shutting down " + description + " did not finish within " + SHUTDOWN_TIMEOUT_SECONDS
                    + " seconds.  Run jstack against the Gradle test worker while it is stuck: a server that never"
                    + " finishes shutting down is usually deadlocked, and jstack reports the threads involved.";

            System.out.println(message);

            if (shutdownTimeoutFailure == null)
            {
                shutdownTimeoutFailure = message;
            }

            throw new IllegalStateException(message, overrun);
        }
        finally
        {
            executor.shutdownNow();
        }
    }


    /**
     * Close the platform, waiting at most {@link #SHUTDOWN_TIMEOUT_SECONDS}, and then fail if any shutdown step
     * overran.  This is the last step of close(), so the failure is raised once everything has been tried.
     *
     * @param platform the platform's application context
     */
    private static void closePlatformWithTimeout(AutoCloseable platform)
    {
        try
        {
            runWithShutdownTimeout("the platform", platform::close);
        }
        catch (Exception closeFailure)
        {
            if (shutdownTimeoutFailure == null)
            {
                System.out.println("Problem closing the platform: " + closeFailure.getMessage());
            }
        }

        String failure = shutdownTimeoutFailure;

        if (failure != null)
        {
            shutdownTimeoutFailure = null;
            throw new AssertionError(failure);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void close()
    {
        if (platformContext != null)
        {
            for (String serverName : new String[]{INTEGRATION_DAEMON_NAME, METADATA_STORE_NAME})
            {
                try
                {
                    runWithShutdownTimeout("server " + serverName, () -> getPlatformServicesClient().shutdownServer(serverName));
                }
                catch (Exception ignoredShutdownFailure)
                {
                    // Best-effort - the JVM is about to exit either way.
                }
            }

            closePlatformWithTimeout(platformContext);
        }
    }

    /**
     * Find a port that is free right now, so that concurrent test runs - in another checkout, or another
     * suite - do not collide on a hard-coded one.
     * <br><br>
     * The socket is closed before the port is handed to Spring, so there is a small window in which
     * something else could take it.  Binding with {@code server.port=0} and letting Tomcat choose would
     * close that window, but the port has to be known before the context starts: this suite's
     * {@code application.properties} interpolates {@code ${server.port}} into the egeriaEndpoint
     * placeholder, which becomes the server's own localServerURL, and that is resolved before Tomcat
     * binds.  Knowing the number up front is worth the small race.
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
            throw new IllegalStateException("Could not allocate a free port for the test platform", error);
        }
    }

}
