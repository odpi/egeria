/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.bitolfvt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.odpi.openmetadata.adminservices.client.IntegrationDaemonConfigurationClient;
import org.odpi.openmetadata.adminservices.client.MetadataAccessStoreConfigurationClient;
import org.odpi.openmetadata.adminservices.client.OMAGServerConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationGroupConfig;
import org.odpi.openmetadata.contentpacks.core.IntegrationGroupDefinition;
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
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OMAGPlatformExtension stands up the deployment the Bitol connectors are designed to run in, in one JVM: an
 * OMAG Server Platform and, on it, two servers.
 * <ul>
 *     <li><b>{@value #METADATA_STORE_NAME}</b> - a metadata access store with a PostgreSQL local repository and the
 *     open metadata types, the Core content pack and the Bitol content pack loaded at start-up.  Its access
 *     services publish to an Apache Kafka broker.</li>
 *     <li><b>{@value #INTEGRATION_DAEMON_NAME}</b> - an integration daemon running the content pack's
 *     {@code BitolIntegrationGroup}, which is where the six Bitol connectors live.</li>
 * </ul>
 * The repository schema is dropped before the metadata access store starts, so every run begins with an empty
 * repository even though PostgreSQL persists between runs.  The access services' out topics run over Apache
 * Kafka by default - they are how the publisher connector hears about the products and agreements the
 * cataloguers create; run with {@code -PrunBitolFvtInMemory} to carry them over the in-memory topic connector
 * instead, which needs no broker.
 * <br>
 * It follows the JUnit 5 "singleton resource" pattern: the first test class extended with this class pays the
 * one-off start-up cost; every other extended class reuses the same running platform, and it is shut down once
 * when the whole run finishes.
 */
public class OMAGPlatformExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource
{
    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(OMAGPlatformExtension.class);
    private static final String                     STORE_KEY = OMAGPlatformExtension.class.getName();

    /**
     * The user this suite configures and drives every server as.
     */
    public static final String USER_ID = "bitolfvtuser";

    /**
     * The metadata access store.
     */
    public static final String METADATA_STORE_NAME = "bitolFvtMetadataStore";

    /**
     * The integration daemon running the Bitol content pack's integration group.
     */
    public static final String INTEGRATION_DAEMON_NAME = "bitolFvtIntegrationDaemon";

    /**
     * A fixed local metadata collection id for the metadata access store's repository, so that the identity of the
     * repository does not change from run to run even though its schema is recreated each time.
     */
    private static final String METADATA_COLLECTION_ID = "3b7e1d52-9f0a-4c6e-8a21-6269746f6c66";

    /**
     * The archives loaded at start-up, in dependency order.  The Core content pack supplies the reference data
     * (file types, deployed implementation types) and the connector types that the Bitol pack builds on; the Files
     * content pack supplies the YAML and JSON file templates that the document files are catalogued from.
     */
    private static final List<String> ARCHIVE_FILES = List.of("OpenMetadataTypes.omarchive",
                                                              "CoreContentPack.omarchive",
                                                              "FilesContentPack.omarchive",
                                                              "BitolContentPack.omarchive");

    /**
     * The integration group this suite's integration daemon runs, named from the content pack's own
     * definition so that a rename there is a compile failure here rather than an empty daemon.
     */
    static final IntegrationGroupDefinition INTEGRATION_GROUP = IntegrationGroupDefinition.BITOL;

    private static volatile boolean               started = false;
    private static volatile Exception             startupFailure;
    private static ConfigurableApplicationContext platformContext;
    private static String                         platformURLRoot;


    /**
     * Return the root URL of the running platform.
     *
     * @return URL root
     */
    public static String getPlatformURLRoot()
    {
        return platformURLRoot;
    }


    /**
     * Return a setting from the platform's own configuration, or the supplied default when it is absent.
     *
     * @param propertyName name of the property
     * @param defaultValue value to use when the property is not set
     * @return configured value
     */
    public static String getProperty(String propertyName,
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
     * Return a numeric setting from the platform's own configuration.
     *
     * @param propertyName name of the property
     * @param defaultValue value to use when the property is absent or unreadable
     * @return configured value
     */
    public static long getLongProperty(String propertyName,
                                       long   defaultValue)
    {
        String value = getProperty(propertyName, null);

        if (value != null)
        {
            try
            {
                return Long.parseLong(value);
            }
            catch (NumberFormatException error)
            {
                System.out.println("bitol-fvt: " + propertyName + " is not a number ('" + value + "') - using " + defaultValue);
            }
        }

        return defaultValue;
    }


    /**
     * Which event bus is this run using?  "kafka" (the default) publishes the access services' out topics to the
     * Apache Kafka broker named in application.properties; "inmemory" uses the in-memory topic connector, which
     * carries them between the two servers inside this JVM; "none" configures the access services without out
     * topics.
     * <br>
     * An event bus is needed because the publisher connector listens to the Open Metadata Store's out topic
     * for changes to digital products and agreements, and a listener registration against a server with no out
     * topic fails the connector's start.  The in-memory connector keeps the suite hermetic while still
     * exercising that event path.
     *
     * @return event bus mode
     */
    static String getEventBusMode()
    {
        return getProperty("bitol.fvt.event.bus", "kafka").toLowerCase();
    }


    /**
     * Return the Apache Kafka broker named in application.properties (the kafkaEndpoint placeholder), which is both
     * the event bus for the access services and the broker the Kafka receiver test publishes to.
     *
     * @return host:port
     * @throws Exception the placeholder variables could not be read
     */
    public static String getKafkaEndpoint() throws Exception
    {
        JsonNode placeholders = new ObjectMapper().readTree(getProperty("platform.placeholder.variables", "{}"));

        return placeholders.path("kafkaEndpoint").asText("localhost:9092");
    }


    /**
     * Is this run using an event bus?
     *
     * @return true unless the mode is "none"
     */
    static boolean isEventBusConfigured()
    {
        return ! "none".equals(getEventBusMode());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void beforeAll(ExtensionContext context) throws Exception
    {
        synchronized (OMAGPlatformExtension.class)
        {
            if (startupFailure != null)
            {
                throw new IllegalStateException("The bitol-fvt environment failed to start: " + startupFailure.getMessage(), startupFailure);
            }

            if (started)
            {
                return;
            }

            context.getRoot().getStore(NAMESPACE).put(STORE_KEY, this);

            try
            {
                /*
                 * The connectors' default directories are relative to the working directory.  Start each run
                 * from an empty store and loading bay so that the assertions describe this run only.
                 */
                BitolFvtTestSupport.deleteRecursively(BitolFvtTestSupport.STORE_DIRECTORY);
                BitolFvtTestSupport.deleteRecursively(BitolFvtTestSupport.LOADING_BAY_DIRECTORY);

                if (! BitolFvtTestSupport.LOADING_BAY_DIRECTORY.mkdirs())
                {
                    throw new IllegalStateException("Could not create " + BitolFvtTestSupport.LOADING_BAY_DIRECTORY.getAbsolutePath());
                }

                startPlatform();

                PlatformServicesClient platformServicesClient = getPlatformServicesClient();

                dropRepositorySchema();

                configureMetadataStore();
                startServer(platformServicesClient, METADATA_STORE_NAME);

                configureIntegrationDaemon();
                startServer(platformServicesClient, INTEGRATION_DAEMON_NAME);

                waitForIntegrationGroup();

                started = true;
            }
            catch (Exception error)
            {
                startupFailure = error;
                throw error;
            }
        }
    }


    /**
     * Start the OMAG Server Platform's Spring Boot application in-process on a free port.
     */
    private void startPlatform()
    {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(OMAGServerPlatform.class);

        builder.properties(Map.of("server.port", Integer.toString(allocateFreePort())));
        builder.web(WebApplicationType.SERVLET);

        platformContext = builder.run();

        int port = ((ServletWebServerApplicationContext) platformContext).getWebServer().getPort();

        platformURLRoot = "http://localhost:" + port;
    }


    /**
     * Ask the operating system for a free TCP port.
     *
     * @return port number
     */
    private static int allocateFreePort()
    {
        try (ServerSocket socket = new ServerSocket(0))
        {
            return socket.getLocalPort();
        }
        catch (Exception error)
        {
            throw new IllegalStateException("Could not allocate a free port for the bitol-fvt platform", error);
        }
    }


    /**
     * Return a platform services client pointed at the running platform, checking that it responds.
     *
     * @return client
     * @throws Exception the platform is not answering
     */
    private PlatformServicesClient getPlatformServicesClient() throws Exception
    {
        PlatformServicesClient platformServicesClient = new PlatformServicesClient("bitol-fvt Platform", platformURLRoot, null, null, null, USER_ID, null);

        String origin = platformServicesClient.getPlatformOrigin();

        if ((origin == null) || (origin.isBlank()))
        {
            throw new IllegalStateException("OMAG Server Platform at " + platformURLRoot + " did not return an origin response");
        }

        return platformServicesClient;
    }


    /**
     * Return the PostgreSQL schema that holds this suite's repository.
     *
     * @return schema name
     */
    private static String getRepositorySchemaName()
    {
        return "repository_" + METADATA_STORE_NAME;
    }


    /**
     * Drop this suite's repository schema so the metadata access store starts empty.  PostgreSQL persists between
     * runs, so without this the cataloguers would find the previous run's agreements and products and the tests
     * would describe a mixture of two runs.  The credentials come from the same secrets store the server itself is
     * configured with, so there is only one place to change them.
     *
     * @throws Exception the schema could not be dropped - the run would not be trustworthy, so it does not go ahead
     */
    private void dropRepositorySchema() throws Exception
    {
        JsonNode placeholders = new ObjectMapper().readTree(getProperty("platform.placeholder.variables", "{}"));

        String databaseURL      = placeholders.path("repositoryDatabaseURL").asText(null);
        String secretsStore     = placeholders.path("egeriaServersSecretsStore").asText(null);
        String secretCollection = placeholders.path("repositorySecretCollectionName").asText(null);

        if ((databaseURL == null) || (secretsStore == null) || (secretCollection == null))
        {
            throw new IllegalStateException("The platform placeholder variables in application.properties must carry repositoryDatabaseURL,"
                                                    + " egeriaServersSecretsStore and repositorySecretCollectionName.");
        }

        JsonNode secrets = new ObjectMapper(new YAMLFactory()).readTree(new File(secretsStore))
                                   .path("secretsCollections").path(secretCollection).path("secrets");

        String userId   = secrets.path("userId").asText(null);
        String password = secrets.path("clearPassword").asText(null);

        try (Connection connection = DriverManager.getConnection(databaseURL, userId, password);
             Statement  statement  = connection.createStatement())
        {
            statement.execute("drop schema if exists " + getRepositorySchemaName() + " cascade");
        }

        System.out.println("bitol-fvt: dropped schema " + getRepositorySchemaName() + " - the repository starts empty");
    }


    /**
     * Configure the metadata access store: a PostgreSQL local repository in this suite's own schema, all access
     * services, and the three archives loaded at start-up.
     *
     * @throws Exception any problem configuring the server is fatal to the whole run
     */
    private void configureMetadataStore() throws Exception
    {
        MetadataAccessStoreConfigurationClient configurationClient = new MetadataAccessStoreConfigurationClient(METADATA_STORE_NAME, platformURLRoot, null, null, null, USER_ID, null);

        configurationClient.clearOMAGServerConfig();

        configurationClient.setServerUserId(USER_ID);
        configurationClient.setBasicServerProperties("Egeria bitol-fvt",
                                                     "Metadata access store holding the metadata that the bitol-fvt suite creates and reads.",
                                                     USER_ID,
                                                     "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider",
                                                     "build/bitol-fvt-data/secrets.omsecrets",
                                                     "bitol-fvt",
                                                     platformURLRoot,
                                                     BitolFvtTestSupport.MAX_PAGE_SIZE);

        Map<String, Object> storageProperties = new HashMap<>();

        storageProperties.put("databaseURL", "~{repositoryDatabaseURL}~?currentSchema=" + getRepositorySchemaName());
        storageProperties.put("databaseSchema", getRepositorySchemaName());
        storageProperties.put("secretsStore", "~{egeriaServersSecretsStore}~");
        storageProperties.put("secretsCollectionName", "~{repositorySecretCollectionName}~");

        configurationClient.setPostgreSQLLocalRepository(storageProperties);

        addAuditLogDestinations(configurationClient);

        if (isEventBusConfigured())
        {
            setEventBus(configurationClient);
            configurationClient.configureAllAccessServices(new HashMap<>());
        }
        else
        {
            configurationClient.configureAllAccessServicesNoTopics(new HashMap<>());
        }

        configurationClient.setLocalMetadataCollectionId(METADATA_COLLECTION_ID);

        for (String archiveFileName : ARCHIVE_FILES)
        {
            File archiveFile = new File(new File(BitolFvtTestSupport.findRepositoryRoot(), "content-packs"), archiveFileName);

            if (! archiveFile.isFile())
            {
                throw new IllegalStateException("Open metadata archive " + archiveFile.getPath() + " not found - has the content pack writer been run?  See content-packs/README.md.");
            }

            configurationClient.addStartUpOpenMetadataArchiveFile(archiveFile.getAbsolutePath());
        }
    }


    /**
     * Configure the integration daemon with the Bitol content pack's integration group.  Which connectors are
     * in the group, what they connect to and how often they refresh all come from the content pack.
     *
     * @throws Exception any problem configuring the server is fatal to the whole run
     */
    private void configureIntegrationDaemon() throws Exception
    {
        IntegrationDaemonConfigurationClient configurationClient = new IntegrationDaemonConfigurationClient(INTEGRATION_DAEMON_NAME, platformURLRoot, null, null, null, USER_ID, null);

        configurationClient.clearOMAGServerConfig();

        configurationClient.setServerUserId(USER_ID);
        configurationClient.setBasicServerProperties("Egeria bitol-fvt",
                                                     "Integration daemon running the Bitol content pack's integration group.",
                                                     USER_ID,
                                                     "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider",
                                                     "build/bitol-fvt-data/secrets.omsecrets",
                                                     "bitol-fvt",
                                                     platformURLRoot,
                                                     BitolFvtTestSupport.MAX_PAGE_SIZE);

        addAuditLogDestinations(configurationClient);

        IntegrationGroupConfig integrationGroupConfig = new IntegrationGroupConfig();

        integrationGroupConfig.setIntegrationGroupQualifiedName(INTEGRATION_GROUP.getQualifiedName());
        integrationGroupConfig.setOMAGServerName(METADATA_STORE_NAME);
        integrationGroupConfig.setOMAGServerPlatformRootURL(platformURLRoot);

        configurationClient.configureIntegrationGroup(integrationGroupConfig);
    }


    /**
     * Send each server's audit log to the console and, through the SLF4J destination, to one shared file that
     * can be read while a run is in progress (see logback-test.xml).
     *
     * @param configurationClient client for the server being configured
     * @throws Exception problem talking to the admin services
     */
    private void addAuditLogDestinations(OMAGServerConfigurationClient configurationClient) throws Exception
    {
        configurationClient.addConsoleAuditLogDestination(new ArrayList<>());
        configurationClient.addSLF4JAuditLogDestination(new ArrayList<>());
    }


    /**
     * Declare the event bus that carries the access services' out topics - Apache Kafka or the in-memory topic
     * connector, depending on the mode.
     *
     * @param configurationClient client for the server being configured
     * @throws Exception problem talking to the admin services
     */
    private void setEventBus(OMAGServerConfigurationClient configurationClient) throws Exception
    {
        if ("kafka".equals(getEventBusMode()))
        {
            Map<String, Object> eventBusProperties = new HashMap<>();
            Map<String, Object> bootstrapServers   = new HashMap<>();

            bootstrapServers.put("bootstrap.servers", "~{kafkaEndpoint}~");

            eventBusProperties.put("producer", bootstrapServers);
            eventBusProperties.put("consumer", bootstrapServers);

            configurationClient.setEventBus("org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider",
                                            "egeria.omag.bitol-fvt",
                                            eventBusProperties);
        }
        else
        {
            configurationClient.setEventBus("org.odpi.openmetadata.adapters.eventbus.topic.inmemory.InMemoryOpenMetadataTopicProvider",
                                            "egeria.omag.bitol-fvt",
                                            new HashMap<>());
        }
    }


    /**
     * Start one of the servers, checking that it actually came up.
     *
     * @param platformServicesClient client for the platform
     * @param serverName server to start
     * @throws Exception the server did not start
     */
    private void startServer(PlatformServicesClient platformServicesClient,
                             String                 serverName) throws Exception
    {
        platformServicesClient.activateWithStoredConfig(serverName);

        if (! platformServicesClient.isServerKnown(serverName))
        {
            throw new IllegalStateException("Server " + serverName + " did not start on platform " + platformURLRoot);
        }

        System.out.println("bitol-fvt: started " + serverName);
    }


    /**
     * Wait until the integration group has retrieved its definition from the metadata access store, reached
     * {@code RUNNING}, and started the connectors registered with it.  A connector that reports a failing
     * exception is reported now, with its message, rather than being left for a test to trip over later.
     *
     * @throws Exception the integration group did not start
     */
    private void waitForIntegrationGroup() throws Exception
    {
        IntegrationDaemon integrationDaemon = getIntegrationDaemonClient();

        long timeoutMilliseconds = getLongProperty("bitol.fvt.governance.server.timeout.seconds", 180) * 1000;
        long pollMilliseconds    = getLongProperty("bitol.fvt.governance.server.poll.seconds", 2) * 1000;
        long giveUpTime          = System.currentTimeMillis() + timeoutMilliseconds;

        String lastKnownStatus = "none reported";

        while (System.currentTimeMillis() < giveUpTime)
        {
            IntegrationGroupSummary summary = null;

            try
            {
                summary = integrationDaemon.getIntegrationGroupSummary(INTEGRATION_GROUP.getQualifiedName());
            }
            catch (Exception notReadyYet)
            {
                lastKnownStatus = "not registered with the daemon yet (" + notReadyYet.getMessage() + ")";
            }

            if (summary != null)
            {
                lastKnownStatus = String.valueOf(summary.getIntegrationGroupStatus());

                List<IntegrationConnectorReport> connectorReports = summary.getIntegrationConnectorReports();

                if ((summary.getIntegrationGroupStatus() == IntegrationGroupStatus.RUNNING) && (connectorReports != null) && (! connectorReports.isEmpty()))
                {
                    for (IntegrationConnectorReport connectorReport : connectorReports)
                    {
                        if (connectorReport.getFailingExceptionMessage() != null)
                        {
                            throw new IllegalStateException("Integration connector " + connectorReport.getConnectorName() + " in group "
                                                                    + INTEGRATION_GROUP.getQualifiedName() + " failed to start: "
                                                                    + connectorReport.getFailingExceptionMessage()
                                                                    + ".  If this is a class loading problem, the connector's module is"
                                                                    + " missing from bitol-fvt's test runtime classpath.");
                        }
                    }

                    System.out.println("bitol-fvt: integration group " + INTEGRATION_GROUP.getQualifiedName() + " running with " + connectorReports.size() + " connector(s)");
                    return;
                }
            }

            Thread.sleep(pollMilliseconds);
        }

        throw new IllegalStateException("Integration group " + INTEGRATION_GROUP.getQualifiedName() + " on " + INTEGRATION_DAEMON_NAME
                                                + " did not reach RUNNING with connectors within " + (timeoutMilliseconds / 1000)
                                                + " seconds (last known status: " + lastKnownStatus + ").  Check that the Bitol content pack loaded into " + METADATA_STORE_NAME + ".");
    }


    /**
     * Return a client for the integration daemon.
     *
     * @return client
     * @throws Exception problem creating the client
     */
    public static IntegrationDaemon getIntegrationDaemonClient() throws Exception
    {
        return new IntegrationDaemon(INTEGRATION_DAEMON_NAME, platformURLRoot, null, null, null, USER_ID, null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void close()
    {
        if (platformContext != null)
        {
            try
            {
                PlatformServicesClient platformServicesClient = new PlatformServicesClient("bitol-fvt Platform", platformURLRoot, null, null, null, USER_ID, null);

                for (String serverName : new String[]{INTEGRATION_DAEMON_NAME, METADATA_STORE_NAME})
                {
                    platformServicesClient.shutdownServer(serverName);
                }
            }
            catch (Exception ignoredShutdownFailure)
            {
                // Best-effort - the JVM is about to exit either way.
            }

            try
            {
                platformContext.close();
            }
            catch (Exception error)
            {
                System.out.println("Problem shutting down the bitol-fvt platform: " + error.getMessage());
            }
        }
    }
}
