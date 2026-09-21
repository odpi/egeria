/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformcatalogfvt;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.odpi.openmetadata.adminservices.client.IntegrationDaemonConfigurationClient;
import org.odpi.openmetadata.adminservices.client.MetadataAccessStoreConfigurationClient;
import org.odpi.openmetadata.adminservices.client.OMAGServerConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationGroupConfig;
import org.odpi.openmetadata.contentpacks.core.IntegrationConnectorDefinition;
import org.odpi.openmetadata.contentpacks.core.IntegrationGroupDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.client.IntegrationDaemon;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationConnectorReport;
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
 * OMAGPlatformExtension starts a single OMAG Server Platform in-process for the whole platform-catalog-fvt
 * run.  That platform is both the deployment the connector runs in and the resource the connector
 * catalogs - which is exactly how the OMAG Server Platform Cataloguer is normally deployed, since it
 * self-registers the local platform as a catalog target when it starts.
 * <ul>
 *     <li><b>{@value #METADATA_STORE_NAME}</b> - a metadata access store with a PostgreSQL local repository
 *     and its access services publishing to a real Apache Kafka broker.  Kafka is not optional here: the
 *     integration daemon learns about its integration group from the out topics.  It loads the open metadata
 *     types, the core content pack and the Egeria content pack, which is where the cataloguer's own
 *     definitions live.</li>
 *     <li><b>{@value #INTEGRATION_DAEMON_NAME}</b> - an integration daemon running the Egeria integration
 *     group, which is where the OMAG Server Platform Cataloguer runs.</li>
 * </ul>
 * It follows the JUnit 5 "singleton resource" pattern: the first test class that is extended with this class
 * pays the one-off startup cost in its {@code @BeforeAll}; every other extended class reuses the same running
 * platform and servers.
 * <br>
 * Everything the suite reaches out to - the PostgreSQL server, the Kafka broker, the platform's own name and
 * organization - is configured from {@code src/test/resources/application.properties}.
 */
public class OMAGPlatformExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource
{
    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(OMAGPlatformExtension.class);
    private static final String                     STORE_KEY = OMAGPlatformExtension.class.getName();

    /**
     * Name of the metadata access store that the cataloguer writes to - and, being a server on the platform
     * under test, one of the servers it catalogs.
     */
    public static final String METADATA_STORE_NAME = "platformCatalogFvtMetadataStore";

    /**
     * Name of the integration daemon that runs the OMAG Server Platform Cataloguer - and, likewise, one of
     * the servers it catalogs.
     */
    public static final String INTEGRATION_DAEMON_NAME = "platformCatalogFvtIntegrationDaemon";

    /**
     * The description given to the integration daemon's configuration document.  It is a constant because
     * {@code setBasicServerProperties} replaces the whole set, so a test that changes one of them has to
     * send the others back unchanged.
     */
    public static final String INTEGRATION_DAEMON_DESCRIPTION = "Integration daemon running the OMAG Server Platform Cataloguer.";

    /**
     * The open metadata repository cohort this suite's metadata access store belongs to.
     * <br>
     * For most of the suite it is a cohort of one, which costs a little at start-up and changes nothing.
     * It is configured from the beginning so that {@link PlatformCatalogCohortFVT} can bring a second
     * platform's metadata access store into it without having to reconfigure and restart this one, which
     * every other test class is using.
     */
    public static final String COHORT_NAME = "platformCatalogFvtCohort";

    /**
     * The servers this suite puts on the platform.  Every one of them should end up catalogued and linked to
     * the platform element.
     */
    public static final List<String> SUITE_SERVER_NAMES = List.of(METADATA_STORE_NAME, INTEGRATION_DAEMON_NAME);

    /**
     * UserId used for all admin, platform and metadata calls made by the platform-catalog-fvt suite.
     */
    public static final String USER_ID = "platformcatalogfvtuser";

    /**
     * The organization name that each server's configuration document carries when the suite starts.  It is
     * deliberately left unset (null) rather than given a value: the cataloguer folds a server's organization
     * name into that server's qualified name, resource name and display name only when there is one, and
     * PlatformCatalogOrganizationNameFVT needs to start from the "no organization name" shape in order to
     * set one afterwards.
     */
    public static final String INITIAL_SERVER_ORGANIZATION_NAME = null;

    /**
     * The integration group the cataloguer belongs to, and its name within that group.  Both come from the
     * content pack definitions rather than being spelled out here, so that renaming either in the content
     * pack cannot leave this suite quietly looking for something that no longer exists.
     */
    public static final String INTEGRATION_GROUP_QUALIFIED_NAME = IntegrationGroupDefinition.EGERIA.getQualifiedName();

    /**
     * The name the cataloguer is registered under in that group - this is what the tests pass to
     * {@code refreshConnector}.
     */
    public static final String CATALOGUER_CONNECTOR_NAME = IntegrationConnectorDefinition.OMAG_SERVER_PLATFORM_CATALOGUER.getConnectorName();

    /**
     * Fixed local metadata collection id for the store's repository.  This is deliberately stable (rather
     * than auto-generated afresh on every run) because the underlying PostgreSQL schema persists across runs
     * - keeping the collection id constant means metadata created by an earlier run is still recognised as
     * belonging to "this" repository on a later run.
     */
    private static final String METADATA_COLLECTION_ID = "8c1f5a72-4d0e-4a9b-9c3f-2b6e7d8a4f10";

    /**
     * The open metadata archives loaded at start-up, in dependency order.  The Egeria content pack is the
     * one that carries the cataloguer's connector definition, its integration group and the software server
     * templates it creates servers from; it is built on top of the core content pack, so both are loaded.
     */
    private static final List<String> CONTENT_PACK_FILES = List.of("OpenMetadataTypes.omarchive",
                                                                   "CoreContentPack.omarchive",
                                                                   "EgeriaContentPack.omarchive");

    private static volatile boolean               started = false;
    private static ConfigurableApplicationContext platformContext;
    private static String                         platformURLRoot;


    /**
     * Return the root URL of the running platform.  The port is allocated at run time rather than
     * fixed, so it differs from one run to the next - which is also why the tests never hard-code a server's
     * qualified name: the cataloguer builds it out of this URL.
     *
     * @return URL root
     */
    public static String getPlatformURLRoot()
    {
        return platformURLRoot;
    }


    /**
     * Return the platform's display name, as set by {@code platform.name} in this suite's
     * application.properties.  The cataloguer reads this out of the platform's public properties and builds
     * the platform element's qualified name, resource name and display name from it.
     *
     * @return platform name
     */
    public static String getPlatformName()
    {
        return getStringProperty("platform.name", "platform-catalog-fvt OMAG Server Platform");
    }


    /**
     * Return the platform's organization name, as set by {@code platform.organization.name} in this suite's
     * application.properties.  This is the PLATFORM's organization, which is a different value from the
     * organization name in each server's configuration document.
     *
     * @return organization name
     */
    public static String getPlatformOrganizationName()
    {
        return getStringProperty("platform.organization.name", "Egeria platform-catalog-fvt");
    }


    /**
     * Return a client for the integration daemon, used to drive the cataloguer's refresh from the tests
     * rather than waiting out its configured refresh interval (an hour).
     *
     * @return integration daemon client
     * @throws Exception problem creating the client
     */
    public static IntegrationDaemon getIntegrationDaemonClient() throws Exception
    {
        return new IntegrationDaemon(INTEGRATION_DAEMON_NAME, platformURLRoot, null, null, null, USER_ID, null);
    }


    /**
     * Return a platform services client for this suite's platform.  The tests use it to compare what the
     * cataloguer recorded against what the platform actually reports.
     *
     * @return client
     * @throws Exception problem creating the client
     */
    public static PlatformServicesClient getPlatformServicesClient() throws Exception
    {
        return new PlatformServicesClient("platform-catalog-fvt Platform", platformURLRoot, null, null, null, USER_ID, null);
    }


    /**
     * Return a configuration client for one of this suite's servers, so that a test can change the stored
     * configuration document the cataloguer reads.
     *
     * @param serverName server to configure
     * @return client
     * @throws Exception problem creating the client
     */
    public static OMAGServerConfigurationClient getServerConfigurationClient(String serverName) throws Exception
    {
        return new OMAGServerConfigurationClient(serverName, platformURLRoot, null, null, null, USER_ID, null);
    }


    /**
     * Ask the integration daemon to refresh the cataloguer now, and wait for the call to return.  The
     * refresh is synchronous, so when this returns the cataloguer has worked through every catalog target it
     * has: the platform, its servers and their configuration.
     *
     * @throws Exception the refresh failed
     */
    /**
     * Restart the integration daemon, and wait until the cataloguer is running in it again.  This is how a
     * test makes the connector's {@code start()} run a second time.
     *
     * @throws Exception the daemon did not come back
     */
    public static void restartIntegrationDaemon() throws Exception
    {
        PlatformServicesClient platformServicesClient = getPlatformServicesClient();

        platformServicesClient.shutdownServer(INTEGRATION_DAEMON_NAME);
        platformServicesClient.activateWithStoredConfig(INTEGRATION_DAEMON_NAME);

        waitForIntegrationGroup();
    }


    public static void refreshCataloguer() throws Exception
    {
        getIntegrationDaemonClient().refreshConnector(CATALOGUER_CONNECTOR_NAME);
    }


    /**
     * Return a string setting from the suite's application.properties, falling back to a default.
     *
     * @param propertyName name of the property
     * @param defaultValue value to use if it is not set
     * @return the value
     */
    static String getStringProperty(String propertyName,
                                    String defaultValue)
    {
        if (platformContext != null)
        {
            String value = platformContext.getEnvironment().getProperty(propertyName);

            if ((value != null) && (! value.isBlank()))
            {
                return value.trim();
            }
        }

        return defaultValue;
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
        String value = getStringProperty(propertyName, null);

        if (value != null)
        {
            return Long.parseLong(value);
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

                    /*
                     * The repository is emptied before the metadata access store opens it, not afterwards.
                     * The cataloguer registers the local platform as a catalog target in its start() method
                     * and refreshes straight away, so anything a previous run left would be picked up and
                     * worked on while it was being removed.
                     */
                    dropRepositorySchema();

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
         * the egeriaEndpoint placeholder, which becomes each server's own localServerURL - resolves to the
         * port actually in use.
         */
        SpringApplicationBuilder builder = new SpringApplicationBuilder(OMAGServerPlatform.class);

        builder.properties(Map.of("server.port", Integer.toString(allocateFreePort())));

        builder.web(WebApplicationType.SERVLET);

        platformContext = builder.run();

        int port = ((ServletWebServerApplicationContext) platformContext).getWebServer().getPort();

        platformURLRoot = "http://localhost:" + port;

        System.out.println("platform-catalog-fvt: platform under test is at " + platformURLRoot);
    }


    /**
     * Configure and start the metadata access store that the cataloguer writes to.
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
        setBasicServerProperties(configurationClient,
                                 INITIAL_SERVER_ORGANIZATION_NAME,
                                 "Metadata access store that platform-catalog-fvt's cataloguer writes to.");

        Map<String, Object> storageProperties = new HashMap<>();

        storageProperties.put("databaseURL", "~{repositoryDatabaseURL}~?currentSchema=repository_" + METADATA_STORE_NAME);
        storageProperties.put("databaseSchema", "repository_" + METADATA_STORE_NAME);
        storageProperties.put("secretsStore", "~{egeriaServersSecretsStore}~");
        storageProperties.put("secretsCollectionName", "~{repositorySecretCollectionName}~");

        configurationClient.setPostgreSQLLocalRepository(storageProperties);
        configurationClient.addConsoleAuditLogDestination(new ArrayList<>());

        setEventBus(configurationClient);

        configurationClient.addCohortRegistration(COHORT_NAME, null);

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
     * Configure and start the integration daemon that runs the cataloguer.
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
        setBasicServerProperties(configurationClient,
                                 INITIAL_SERVER_ORGANIZATION_NAME,
                                 INTEGRATION_DAEMON_DESCRIPTION);

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
     * Set the basic properties of one of this suite's servers.  They are set in one place because the
     * organization name is one of them, and {@code setBasicServerProperties} replaces the lot: a test that
     * changes the organization name has to send every other value again, unchanged, or it will quietly clear
     * them.  {@link PlatformCatalogOrganizationNameFVT} calls straight back into here to do exactly that.
     *
     * @param configurationClient client for the server being configured
     * @param organizationName organization name to store in the configuration document - may be null
     * @param serverDescription description for the server
     * @throws Exception the properties could not be set
     */
    static void setBasicServerProperties(OMAGServerConfigurationClient configurationClient,
                                         String                        organizationName,
                                         String                        serverDescription) throws Exception
    {
        setBasicServerProperties(configurationClient, organizationName, serverDescription, platformURLRoot);
    }


    /**
     * Set the basic properties of one of this suite's servers, including the address the server records for
     * the platform it is running on.
     * <br>
     * That address is the server's {@code localServerURL}, and it is what reaches a connector as
     * {@code getLocalServerURL()}.  Varying it is how {@link PlatformCatalogLocalServerURLFVT} puts the
     * suite into the shape of a deployment where the integration daemon and the metadata access store are
     * on different platforms, without needing a second platform to do it.
     *
     * @param configurationClient client for the server being configured
     * @param organizationName organization name to store in the configuration document - may be null
     * @param serverDescription description for the server
     * @param serverURLRoot the address of the platform this server records as its own
     * @throws Exception the properties could not be set
     */
    static void setBasicServerProperties(OMAGServerConfigurationClient configurationClient,
                                         String                        organizationName,
                                         String                        serverDescription,
                                         String                        serverURLRoot) throws Exception
    {
        configurationClient.setBasicServerProperties(organizationName,
                                                     serverDescription,
                                                     USER_ID,
                                                     "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider",
                                                     "build/platform-catalog-fvt-data/secrets.omsecrets",
                                                     "platform-catalog-fvt",
                                                     serverURLRoot,
                                                     PlatformCatalogFvtTestSupport.MAX_PAGE_SIZE);
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
                                        "egeria.omag.platform-catalog-fvt",
                                        eventBusProperties);
    }


    /**
     * Empty the repository this suite's metadata access store runs on, by dropping and recreating its
     * PostgreSQL schema.
     * <br>
     * The other FVT suites clear up after themselves through the metadata APIs, because what they assert on
     * is a fixture they laid down and can name.  Nothing here is: every element this suite looks at is
     * created by the connector, from a platform whose port is allocated afresh on each run, so last run's
     * platform and servers carry names this run cannot predict.  They also actively break the next run - the
     * connector names the platform's User Token Manager from a fixed placeholder rather than from the
     * platform, so the one left behind takes the qualified name that the next run's {@code start()} needs and
     * the connector fails before it catalogs anything.
     * <br>
     * Dropping the schema is also honest about what this suite is: a run of it is meaningful only against an
     * ecosystem that has never been catalogued before.
     *
     * @throws Exception the schema could not be dropped, which would make the run's results meaningless
     */
    private void dropRepositorySchema() throws Exception
    {
        String schemaName = getStringProperty("platform.catalog.fvt.repository.schema", "repository_" + METADATA_STORE_NAME);
        String databaseURL = getStringProperty("platform.catalog.fvt.repository.url", "jdbc:postgresql://localhost:5442/egeria");
        String databaseUser = getStringProperty("platform.catalog.fvt.repository.user", "egeria_user");
        String databasePassword = getStringProperty("platform.catalog.fvt.repository.password", "user4egeria");

        try (java.sql.Connection connection = java.sql.DriverManager.getConnection(databaseURL, databaseUser, databasePassword);
             java.sql.Statement statement = connection.createStatement())
        {
            statement.execute("DROP SCHEMA IF EXISTS " + schemaName + " CASCADE");
            statement.execute("CREATE SCHEMA " + schemaName);
        }

        System.out.println("platform-catalog-fvt: repository schema " + schemaName + " recreated empty");
    }


    /**
     * Wait for the integration daemon to bring the Egeria integration group up.  A connector that failed to
     * start reports its own exception message, which is repeated here: the usual causes are the
     * egeria-system-connectors module missing from this suite's test runtime classpath (not a compile
     * failure, but it leaves every test with nothing to assert against) and the module's own
     * {@code secrets/egeria-servers.omsecrets} being absent or unreadable.
     *
     * @throws Exception the integration group did not start
     */
    static void waitForIntegrationGroup() throws Exception
    {
        IntegrationDaemon integrationDaemon = getIntegrationDaemonClient();

        long timeoutMilliseconds = getLongProperty("platform.catalog.fvt.integration.daemon.timeout.seconds", 180) * 1000;
        long pollMilliseconds    = getLongProperty("platform.catalog.fvt.integration.daemon.poll.seconds", 2) * 1000;
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
                        if ((CATALOGUER_CONNECTOR_NAME.equals(connectorReport.getConnectorName()))
                                    && (connectorReport.getFailingExceptionMessage() != null))
                        {
                            throw new IllegalStateException("Integration connector " + CATALOGUER_CONNECTOR_NAME + " in group "
                                                                    + INTEGRATION_GROUP_QUALIFIED_NAME + " failed to start: "
                                                                    + connectorReport.getFailingExceptionMessage()
                                                                    + ".  If this is a class loading problem, the"
                                                                    + " egeria-system-connectors module is missing from"
                                                                    + " platform-catalog-fvt's test runtime classpath.");
                        }
                    }

                    System.out.println("platform-catalog-fvt: integration group " + INTEGRATION_GROUP_QUALIFIED_NAME
                                               + " running with " + connectorReports.size() + " connector(s)");
                    return;
                }
            }

            Thread.sleep(pollMilliseconds);
        }

        throw new IllegalStateException("Integration group " + INTEGRATION_GROUP_QUALIFIED_NAME + " on " + INTEGRATION_DAEMON_NAME
                                                + " did not reach RUNNING with connectors within " + (timeoutMilliseconds / 1000)
                                                + " seconds (last known status: " + lastKnownStatus + ").  Check that the Egeria"
                                                + " content pack loaded into " + METADATA_STORE_NAME + ".");
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
                    getPlatformServicesClient().shutdownServer(serverName);
                }
                catch (Exception ignoredShutdownFailure)
                {
                    // Best-effort - the JVM is about to exit either way.
                }
            }

            platformContext.close();
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
     * placeholder, which becomes each server's own localServerURL, and that is resolved before Tomcat
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
