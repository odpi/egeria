/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openlineagefvt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.odpi.openmetadata.adminservices.client.EngineHostConfigurationClient;
import org.odpi.openmetadata.adminservices.client.IntegrationDaemonConfigurationClient;
import org.odpi.openmetadata.adminservices.client.MetadataAccessStoreConfigurationClient;
import org.odpi.openmetadata.adminservices.client.OMAGServerConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationGroupConfig;
import org.odpi.openmetadata.contentpacks.core.GovernanceEngineDefinition;
import org.odpi.openmetadata.contentpacks.core.IntegrationGroupDefinition;
import org.odpi.openmetadata.governanceservers.enginehostservices.client.EngineHostClient;
import org.odpi.openmetadata.governanceservers.enginehostservices.properties.GovernanceEngineStatus;
import org.odpi.openmetadata.governanceservers.enginehostservices.properties.GovernanceEngineSummary;
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
 * OMAGPlatformExtension starts the openlineage-fvt environment once per test JVM: an OMAG Server Platform running
 * in-process, a metadata access store with a PostgreSQL repository and an Apache Kafka event bus, and an integration
 * daemon running the Open Lineage content pack's integration group.  It is registered on each test class with
 * {@code @ExtendWith(OMAGPlatformExtension.class)}; the first class to start triggers the start-up, later classes
 * find it already running, and the platform is shut down when the JUnit root context closes.
 * <ul>
 *     <li><b>{@value #METADATA_STORE_NAME}</b> - holds the elements the connectors create from the events.</li>
 *     <li><b>{@value #INTEGRATION_DAEMON_NAME}</b> - runs the Open Lineage cataloguer, Kafka listener, file
 *     publisher, API publisher and governance action publisher.</li>
 *     <li><b>{@value #ENGINE_HOST_NAME}</b> - runs the Egeria Governance Engine, which hosts the Open Lineage
 *     Lovelace services that analyse the file-based log store.</li>
 * </ul>
 * An activated integration daemon is not yet a ready one: it fetches its integration group's definition from
 * the metadata store and starts the connectors afterwards, so start-up waits until the group reports running
 * with its connectors before any test is allowed to publish an event.
 */
public class OMAGPlatformExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource
{
    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(OMAGPlatformExtension.class);
    private static final String                     STORE_KEY = OMAGPlatformExtension.class.getName();

    /**
     * The user this suite configures and drives every server as.  {@link SubscriptionFvtSecurityConfig} also names
     * the platform's anonymous principal after it, so the calls this suite makes into the view server arrive
     * as this user too.
     */
    public static final String USER_ID = "openlineagefvtuser";

    /**
     * The metadata access store: PostgreSQL local repository, all access services with Kafka out topics, and
     * the open metadata types plus the Core and Open Lineage content packs loaded at start-up.
     */
    public static final String METADATA_STORE_NAME = "openlineageFvtMetadataStore";

    /**
     * The integration daemon running the Open Lineage content pack's integration group.
     */
    public static final String INTEGRATION_DAEMON_NAME = "openlineageFvtIntegrationDaemon";

    /**
     * Name of the engine host running the Egeria Governance Engine, which hosts the Open Lineage Lovelace services.
     */
    public static final String ENGINE_HOST_NAME = "openlineageFvtEngineHost";

    /**
     * A fixed local metadata collection id for the metadata access store's repository.  This is deliberately
     * stable rather than generated afresh on every run, because the underlying PostgreSQL schema persists
     * across runs: keeping the id constant means metadata created by an earlier run is still recognised as
     * belonging to "this" repository on a later one.
     */
    private static final String METADATA_COLLECTION_ID = "9c4b2e71-5f3a-4d8e-b2a6-6f70656e6c696e";

    /**
     * The archives loaded at start-up, in dependency order.
     * <br>
     * Each entry earns its place.  The types have to be there before any instance can be created.  The Core
     * content pack supplies the generic governance services the product processes call, and the integration
     * daemon machinery the Open Lineage pack plugs into.  The Open Lineage pack supplies the
     * templates this suite creates its subscription destinations from - a table as a tabular data set, and a
     * schema as a tabular data set collection.  The products pack supplies the catalogue itself: Jacquard,
     * the Jacquard integration group, the Baudot subscription manager, and the create- and
     * cancel-subscription governance services.
     * <br>
     * Nothing else is loaded.  The other content packs would add load time and a large amount of unrelated
     * metadata for the suite's searches to work around, and none of it is reachable from the processes under
     * test.
     */
    private static final List<String> ARCHIVE_FILES = List.of("OpenMetadataTypes.omarchive",
                                                              "CoreContentPack.omarchive",
                                                              "OpenLineageContentPack.omarchive");

    /**
     * The integration group the daemon runs.  It is named from the content pack's definition so that a renamed
     * group is a compile failure here rather than a timeout waiting for it to start.
     */
    static final IntegrationGroupDefinition INTEGRATION_GROUP = IntegrationGroupDefinition.OPEN_LINEAGE;

    /**
     * The governance engines the engine host runs.  The Open Lineage Lovelace services are registered with the
     * Egeria Governance Engine.
     */
    static final List<GovernanceEngineDefinition> GOVERNANCE_ENGINES = List.of(GovernanceEngineDefinition.EGERIA_GOVERNANCE_ENGINE);

    private static volatile boolean               started = false;
    private static volatile Exception             startupFailure;
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
     * Return a setting from the platform's own configuration, or the supplied default when it is absent, so
     * that everything this suite is pointed at lives in application.properties rather than being compiled in.
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
                System.out.println("openlineage-fvt: " + propertyName + " is not a number ('" + value + "') - using " + defaultValue);
            }
        }

        return defaultValue;
    }


    /**
     * Return a boolean setting from the platform's own configuration.
     *
     * @param propertyName name of the property
     * @param defaultValue value to use when the property is not set
     * @return configured value
     */
    public static boolean getBooleanProperty(String  propertyName,
                                             boolean defaultValue)
    {
        String value = getProperty(propertyName, null);

        return (value == null) ? defaultValue : Boolean.parseBoolean(value);
    }


    /**
     * Is this run using an event bus?
     * <br>
     * Normally it is: the access services publish to Apache Kafka, the integration daemon is told about each new
     * engine action as it is requested, and work starts promptly.  Started with
     * {@code -PrunSubscriptionFvtNoKafka}, the access services are configured without out topics and there is no
     * broker in the picture at all - nothing is published, and the governance servers are left with only what
     * they poll for.
     * <br>
     * That second mode is worth running because an event bus is not a requirement for an integration daemon, and this
     * is what says so.  Every test in this suite is expected to pass either way; the difference is how quickly
     * an engine action is picked up, not whether it is.
     *
     * @return true when the event bus is configured
     */
    static boolean isEventBusConfigured()
    {
        return ! "none".equalsIgnoreCase(getProperty("openlineage.fvt.event.bus", "kafka"));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void beforeAll(ExtensionContext context) throws Exception
    {
        synchronized (OMAGPlatformExtension.class)
        {
            /*
             * A start-up that has already failed is reported again rather than retried.  Retrying would start
             * a second platform on a port the first one is still holding, and every test class after the first
             * would then fail with a port-in-use error - burying the one message that says what actually
             * went wrong.
             */
            if (startupFailure != null)
            {
                throw new IllegalStateException("The openlineage-fvt environment failed to start: " + startupFailure.getMessage(),
                                                startupFailure);
            }

            if (started)
            {
                return;
            }

            /*
             * Registered before the environment is built rather than after, so that the platform is shut down
             * at the end of the run even when the build below fails part-way.
             */
            context.getRoot().getStore(NAMESPACE).put(STORE_KEY, this);

            try
            {
                stageSecretsStore();

                startPlatform();

                PlatformServicesClient platformServicesClient = getPlatformServicesClient();

                /*
                 * The metadata access store is configured and started first: it is where the integration daemon
                 * reads its configuration from, so it has nothing to run until the store is up and the content
                 * packs have been loaded into it.
                 */
                configureMetadataStore();
                startServer(platformServicesClient, METADATA_STORE_NAME);

                /*
                 * Clear away everything a previous run left behind before the integration daemon starts, so that
                 * the connectors work from an empty slate and the tests only see what this run's events created.
                 */
                OpenLineageFvtTestSupport.cleanUpLeftoverTestElements();
                OpenLineageFvtTestSupport.cleanUpLogStore();

                configureEngineHost();
                configureIntegrationDaemon();

                startServer(platformServicesClient, ENGINE_HOST_NAME);
                startServer(platformServicesClient, INTEGRATION_DAEMON_NAME);

                /*
                 * An activated governance server is not yet a ready one - see the class comment.
                 */
                waitForGovernanceEngines();
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
     * Put a secrets store where the connectors expect to find it, with its token exchanges removed.
     * <br>
     * Two things have to be dealt with.  The content pack's connector definitions and catalog templates name
     * their secrets store as {@code secrets/egeria-servers.omsecrets} - a path relative to the working
     * directory of whatever is running them, which here is this module's directory rather than a server's.
     * And the shipped store obtains most of its identities by POSTing to {@code https://localhost:7443/api/token},
     * a platform that does not exist in a hermetic test run.
     * <br>
     * Both matter for the same reason.  A connector that cannot read its secrets never learns the user
     * identity it should call open metadata as, so it builds its client with a null user and fails on its
     * first query - which arrives as a complaint about a null userId rather than about a missing secret.
     * <br>
     * So the file is copied to where the definitions look for it, and every collection that authenticates by
     * token is rewritten to supply the same user identity directly.  This platform has no user directory and
     * no authentication, so a bearer token would have nothing to prove; the identity is the part that is
     * actually needed.  The definitions themselves are left alone - the path and the collection names are what
     * the content pack ships, and a suite that rewrote those would be testing something other than what is
     * shipped.
     *
     * @throws Exception the secrets store could not be staged, which is fatal - every connector needs it
     */
    private void stageSecretsStore() throws Exception
    {
        File sharedSecrets = new File(OpenLineageFvtTestSupport.getSecretsStoreLocation());

        if (! sharedSecrets.isFile())
        {
            throw new IllegalStateException("The shared secrets store " + sharedSecrets.getAbsolutePath()
                                                    + " is missing.  Point openlineage.fvt.server.secrets.store at it.");
        }

        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

        @SuppressWarnings("unchecked")
        Map<String, Object> secretsStore = yamlMapper.readValue(sharedSecrets, Map.class);

        Object collections = secretsStore.get("secretsCollections");

        if (collections instanceof Map<?, ?> collectionMap)
        {
            for (Object collection : collectionMap.values())
            {
                if (collection instanceof Map<?, ?> collectionProperties)
                {
                    replaceTokenExchangeWithUserId(collectionProperties);
                }
            }
        }

        File stagedSecrets = new File("secrets", sharedSecrets.getName());

        if (stagedSecrets.getParentFile().mkdirs())
        {
            System.out.println("openlineage-fvt: created " + stagedSecrets.getParentFile().getAbsolutePath()
                                       + " for the connectors' secrets store");
        }

        yamlMapper.writeValue(stagedSecrets, secretsStore);
    }


    /**
     * Turn one collection's token exchange into the user identity it would have authenticated as.
     *
     * @param collectionProperties one secrets collection, modified in place
     */
    @SuppressWarnings("unchecked")
    private void replaceTokenExchangeWithUserId(Map<?, ?> collectionProperties)
    {
        Object tokenAPI = collectionProperties.get("tokenAPI");

        if (! (tokenAPI instanceof Map<?, ?> tokenProperties))
        {
            return;
        }

        Object requestBody = tokenProperties.get("requestBody");

        if (requestBody instanceof Map<?, ?> requestProperties)
        {
            Object userId = requestProperties.get("userId");

            if (userId != null)
            {
                Map<String, Object> secrets = new HashMap<>();

                secrets.put("userId", userId);

                ((Map<String, Object>) collectionProperties).put("secrets", secrets);
            }
        }

        ((Map<String, Object>) collectionProperties).remove("tokenAPI");
    }


    /**
     * Start the OMAG Server Platform's Spring Boot application in-process.  All of the Spring Boot
     * configuration (port, the placeholder variables that decide which PostgreSQL server and which Kafka
     * broker are used, logging, security) comes from this module's classpath {@code application.properties}
     * rather than being set programmatically here, so that the environment this suite targets can be changed
     * without touching Java code.
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

        builder.properties(java.util.Map.of("server.port", Integer.toString(choosePort())));

        builder.web(WebApplicationType.SERVLET);

        platformContext = builder.run();

        int port = ((ServletWebServerApplicationContext) platformContext).getWebServer().getPort();

        platformURLRoot = "http://localhost:" + port;
    }


    /**
     * Return a platform services client pointed at the running platform, checking as it does so that the
     * platform is actually responding.
     *
     * @return client
     * @throws Exception the platform is not answering
     */
    private PlatformServicesClient getPlatformServicesClient() throws Exception
    {
        PlatformServicesClient platformServicesClient = new PlatformServicesClient("openlineage-fvt Platform",
                                                                                   platformURLRoot,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   USER_ID,
                                                                                   null);

        String origin = platformServicesClient.getPlatformOrigin();

        if ((origin == null) || (origin.isBlank()))
        {
            throw new IllegalStateException("OMAG Server Platform at " + platformURLRoot + " did not return an origin response");
        }

        return platformServicesClient;
    }


    /**
     * Configure the metadata access store: a PostgreSQL local repository, all access services <em>with</em>
     * their Kafka out topics, and the three archives loaded at start-up.
     * <br>
     * The out topics are the reason this suite needs Kafka.  The sibling suites configure their access
     * services with {@code configureAllAccessServicesNoTopics} because nothing listens to them; here the
     * integration daemon does, and without the topics it would start, read its
     * configuration once, and then never hear about another engine action again.
     *
     * @throws Exception any problem configuring the server is fatal to the whole run
     */
    private void configureMetadataStore() throws Exception
    {
        MetadataAccessStoreConfigurationClient configurationClient =
                new MetadataAccessStoreConfigurationClient(METADATA_STORE_NAME,
                                                          platformURLRoot,
                                                          null,
                                                          null,
                                                          null,
                                                          USER_ID,
                                                          null);

        /*
         * A config document from an earlier run may still be on disk - clear it first so the config built
         * below is the only thing in it, rather than being appended to leftover state.
         */
        configurationClient.clearOMAGServerConfig();

        configurationClient.setServerUserId(USER_ID);
        configurationClient.setBasicServerProperties("Egeria openlineage-fvt",
                                                     "Metadata access store holding the metadata that the Open Lineage connectors create from the events the openlineage-fvt suite publishes.",
                                                     USER_ID,
                                                     "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider",
                                                     "build/openlineage-fvt-data/secrets.omsecrets",
                                                     "openlineage-fvt",
                                                     platformURLRoot,
                                                     OpenLineageFvtTestSupport.MAX_PAGE_SIZE);

        Map<String, Object> storageProperties = new HashMap<>();

        storageProperties.put("databaseURL", "~{repositoryDatabaseURL}~?currentSchema=repository_" + METADATA_STORE_NAME);
        storageProperties.put("databaseSchema", "repository_" + METADATA_STORE_NAME);
        storageProperties.put("secretsStore", "~{egeriaServersSecretsStore}~");
        storageProperties.put("secretsCollectionName", "~{repositorySecretCollectionName}~");

        configurationClient.setPostgreSQLLocalRepository(storageProperties);

        addAuditLogDestinations(configurationClient);

        if (isEventBusConfigured())
        {
            /*
             * The event bus has to be declared before the access services are configured: each service's out
             * topic connection is built from it at the point the service is registered, so services configured
             * first would be configured with no way to publish anything.
             */
            setEventBus(configurationClient);

            configurationClient.configureAllAccessServices(new HashMap<>());
        }
        else
        {
            /*
             * No event bus, and therefore no out topics: nothing is published and the governance servers hear
             * nothing.  See isEventBusConfigured for what this run is for.
             */
            configurationClient.configureAllAccessServicesNoTopics(new HashMap<>());

            System.out.println("openlineage-fvt: running with no event bus - the access services are configured without out topics,"
                                       + " so the integration daemon is working from what it polls for"
                                       + " rather than from what they are told");
        }

        configurationClient.setLocalMetadataCollectionId(METADATA_COLLECTION_ID);

        for (String archiveFileName : ARCHIVE_FILES)
        {
            File archiveFile = new File(findContentPacksDirectory(), archiveFileName);

            if (! archiveFile.isFile())
            {
                throw new IllegalStateException("Open metadata archive " + archiveFile.getPath() + " not found - has the content pack" +
                                                        " writer been run?  See content-packs/README.md.");
            }

            configurationClient.addStartUpOpenMetadataArchiveFile(archiveFile.getAbsolutePath());
        }
    }


    /**
     * Configure the engine host to run the governance engines.  Only the engines' names are configured: the
     * engine host fetches each engine's definition - and the services it runs - from the metadata store.
     *
     * @throws Exception configuration failed
     */
    private void configureEngineHost() throws Exception
    {
        EngineHostConfigurationClient configurationClient = new EngineHostConfigurationClient(ENGINE_HOST_NAME,
                                                                                              platformURLRoot,
                                                                                              null,
                                                                                              null,
                                                                                              null,
                                                                                              USER_ID,
                                                                                              null);

        configurationClient.clearOMAGServerConfig();
        configurationClient.setServerUserId(USER_ID);
        configurationClient.setBasicServerProperties("Egeria openlineage-fvt",
                                                     "Engine host running the Egeria Governance Engine, which hosts the Open Lineage Lovelace services.",
                                                     USER_ID,
                                                     "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider",
                                                     "build/openlineage-fvt-data/secrets.omsecrets",
                                                     "openlineage-fvt",
                                                     platformURLRoot,
                                                     OpenLineageFvtTestSupport.MAX_PAGE_SIZE);

        addAuditLogDestinations(configurationClient);

        List<EngineConfig> engineConfigs = new ArrayList<>();

        for (GovernanceEngineDefinition governanceEngine : GOVERNANCE_ENGINES)
        {
            EngineConfig engineConfig = new EngineConfig();

            engineConfig.setEngineQualifiedName(governanceEngine.getName());
            engineConfig.setEngineUserId(USER_ID);
            engineConfig.setOMAGServerName(METADATA_STORE_NAME);
            engineConfig.setOMAGServerPlatformRootURL(platformURLRoot);
            engineConfigs.add(engineConfig);
        }

        configurationClient.setEngineHostServicesConfig(engineConfigs);
    }


    /**
     * Configure the integration daemon with the Open Lineage content pack's integration group.
     * <br>
     * Only the group's qualified name is configured here.  Which connectors are in
     * the group, what they connect to and how often they refresh are all recorded in the content pack and
     * read from the metadata access store at start-up - so this configuration would be unchanged if another
     * connector were added to the group tomorrow.
     *
     * @throws Exception any problem configuring the server is fatal to the whole run
     */
    private void configureIntegrationDaemon() throws Exception
    {
        IntegrationDaemonConfigurationClient configurationClient =
                new IntegrationDaemonConfigurationClient(INTEGRATION_DAEMON_NAME,
                                                        platformURLRoot,
                                                        null,
                                                        null,
                                                        null,
                                                        USER_ID,
                                                        null);

        configurationClient.clearOMAGServerConfig();

        configurationClient.setServerUserId(USER_ID);
        configurationClient.setBasicServerProperties("Egeria openlineage-fvt",
                                                     "Integration daemon running the Open Lineage content pack's integration group.",
                                                     USER_ID,
                                                     "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider",
                                                     "build/openlineage-fvt-data/secrets.omsecrets",
                                                     "openlineage-fvt",
                                                     platformURLRoot,
                                                     OpenLineageFvtTestSupport.MAX_PAGE_SIZE);

        addAuditLogDestinations(configurationClient);

        IntegrationGroupConfig integrationGroupConfig = new IntegrationGroupConfig();

        integrationGroupConfig.setIntegrationGroupQualifiedName(INTEGRATION_GROUP.getQualifiedName());
        integrationGroupConfig.setOMAGServerName(METADATA_STORE_NAME);
        integrationGroupConfig.setOMAGServerPlatformRootURL(platformURLRoot);

        configurationClient.configureIntegrationGroup(integrationGroupConfig);
    }


    /**
     * Send each server's audit log to the console and, through the SLF4J destination, to one shared file.
     * <br>
     * The file destination is what makes a run diagnosable.  Almost nothing this suite tests happens in the
     * test's own thread: the test asks the view server to start a governance action and then waits, and when
     * the wait expires the reason is in the integration daemon's audit log - whether the
     * engine claimed the action, whether a connector could be built for it, and what the connector said.
     * Gradle buffers a test JVM's console output until the task ends, so on a run that is stuck the console
     * destination alone tells you nothing until it is too late to be useful.
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
     * Declare the Apache Kafka event bus that carries the access services' out topics.  The topic root is
     * unique to this suite so that its traffic cannot be confused with, or consumed by, anything else using
     * the same broker.
     *
     * @param configurationClient client for the server being configured
     * @throws Exception problem talking to the admin services
     */
    private void setEventBus(OMAGServerConfigurationClient configurationClient) throws Exception
    {
        Map<String, Object> eventBusProperties = new HashMap<>();
        Map<String, Object> bootstrapServers   = new HashMap<>();

        bootstrapServers.put("bootstrap.servers", "~{kafkaEndpoint}~");

        eventBusProperties.put("producer", bootstrapServers);
        eventBusProperties.put("consumer", bootstrapServers);

        configurationClient.setEventBus("org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider",
                                        "egeria.omag.openlineage-fvt",
                                        eventBusProperties);
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

        System.out.println("openlineage-fvt: started " + serverName);
    }


    /**
     * Wait for every configured governance engine to report RUNNING.
     *
     * @throws Exception the engines did not start in time
     */
    private void waitForGovernanceEngines() throws Exception
    {
        EngineHostClient engineHostClient = getEngineHostClient();

        long timeoutMilliseconds = getLongProperty("openlineage.fvt.governance.server.timeout.seconds", 180) * 1000;
        long pollMilliseconds    = getLongProperty("openlineage.fvt.governance.server.poll.seconds", 2) * 1000;
        long giveUpTime          = System.currentTimeMillis() + timeoutMilliseconds;

        String lastKnownStatuses = "none reported";

        while (System.currentTimeMillis() < giveUpTime)
        {
            List<GovernanceEngineSummary> summaries = engineHostClient.getGovernanceEngineSummaries();

            if (summaries != null)
            {
                StringBuilder statuses     = new StringBuilder();
                int           runningCount = 0;

                for (GovernanceEngineSummary summary : summaries)
                {
                    statuses.append(" ").append(summary.getGovernanceEngineName()).append("=").append(summary.getGovernanceEngineStatus());

                    if (summary.getGovernanceEngineStatus() == GovernanceEngineStatus.RUNNING)
                    {
                        runningCount++;
                    }
                }

                lastKnownStatuses = statuses.toString().trim();

                if (runningCount == GOVERNANCE_ENGINES.size())
                {
                    System.out.println("openlineage-fvt: governance engines running -" + statuses);
                    return;
                }
            }

            Thread.sleep(pollMilliseconds);
        }

        throw new IllegalStateException("The governance engines on " + ENGINE_HOST_NAME + " did not reach RUNNING within "
                                                + (timeoutMilliseconds / 1000) + " seconds (last known: " + lastKnownStatuses
                                                + ").  An engine that stays CONFIGURING has not found its definition in "
                                                + METADATA_STORE_NAME + " - check that the Core content pack loaded.");
    }


    /**
     * Wait until the integration group has retrieved its definition from the metadata access store, reached
     * {@code RUNNING}, and started the connectors registered with it.
     * <br>
     * Both halves matter.  A group that reaches RUNNING with no connector reports has found the group but
     * none of its members; a connector that reports {@code CONFIG_FAILED} has been found but could not be
     * instantiated, which in this suite almost always means the connector's implementation is not on the test
     * runtime classpath.  Either way the run cannot do what it is here to do, so both are reported now with
     * the connector's own failing exception message rather than being left for a test to trip over later.
     *
     * @throws Exception the integration group did not start
     */
    private void waitForIntegrationGroup() throws Exception
    {
        IntegrationDaemon integrationDaemon = getIntegrationDaemonClient();

        long timeoutMilliseconds = getLongProperty("openlineage.fvt.governance.server.timeout.seconds", 180) * 1000;
        long pollMilliseconds    = getLongProperty("openlineage.fvt.governance.server.poll.seconds", 2) * 1000;
        long giveUpTime          = System.currentTimeMillis() + timeoutMilliseconds;

        String lastKnownStatus = "none reported";

        while (System.currentTimeMillis() < giveUpTime)
        {
            IntegrationGroupSummary summary = null;

            try
            {
                /*
                 * The group is looked up by its QUALIFIED name, not its display name: that is what the
                 * integration daemon was configured with, and so what it knows the group by.
                 *
                 * A group the daemon has not registered yet is reported as an error rather than as an empty
                 * summary, so the exception is caught and treated as "not yet".  The daemon populates its
                 * groups from the metadata access store after start-up returns, so an early poll landing
                 * before that is ordinary, not a failure.
                 */
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

                if ((summary.getIntegrationGroupStatus() == IntegrationGroupStatus.RUNNING)
                            && (connectorReports != null) && (! connectorReports.isEmpty()))
                {
                    for (IntegrationConnectorReport connectorReport : connectorReports)
                    {
                        if (connectorReport.getFailingExceptionMessage() != null)
                        {
                            throw new IllegalStateException("Integration connector " + connectorReport.getConnectorName() + " in group "
                                                                    + INTEGRATION_GROUP.getQualifiedName() + " failed to start: "
                                                                    + connectorReport.getFailingExceptionMessage()
                                                                    + ".  If this is a class loading problem, the connector's module is"
                                                                    + " missing from openlineage-fvt's test runtime classpath.");
                        }
                    }

                    System.out.println("openlineage-fvt: integration group " + INTEGRATION_GROUP.getQualifiedName() + " running with "
                                               + connectorReports.size() + " connector(s)");
                    return;
                }
            }

            Thread.sleep(pollMilliseconds);
        }

        throw new IllegalStateException("Integration group " + INTEGRATION_GROUP.getQualifiedName() + " on " + INTEGRATION_DAEMON_NAME
                                                + " did not reach RUNNING with connectors within " + (timeoutMilliseconds / 1000)
                                                + " seconds (last known status: " + lastKnownStatus + ").  Check that the Open Lineage"
                                                + " content pack loaded into " + METADATA_STORE_NAME + ".");
    }


    /**
     * Return a client for the engine host.
     *
     * @return client
     * @throws Exception client could not be created
     */
    public static EngineHostClient getEngineHostClient() throws Exception
    {
        return new EngineHostClient(ENGINE_HOST_NAME, platformURLRoot, null, null, null, USER_ID, null);
    }


    /**
     * Return a client for the integration daemon, used both at start-up and by the tests that drive a
     * connector refresh rather than waiting for the connector's own refresh interval.
     *
     * @return client
     * @throws Exception problem creating the client
     */
    public static IntegrationDaemon getIntegrationDaemonClient() throws Exception
    {
        return new IntegrationDaemon(INTEGRATION_DAEMON_NAME, platformURLRoot, null, null, null, USER_ID, null);
    }


    /**
     * Read one secret out of the YAML secrets store that the servers themselves are configured with, so that
     * the credentials this suite uses to reach the PostgreSQL server under test are defined in one place
     * rather than repeated in Java.
     *
     * @param secretName name of the secret within the collection, for example "userId"
     * @return secret value, or null if the store, the collection or the secret is missing
     */
    static String getServerUnderTestSecret(String secretName)
    {
        String secretsStoreLocation = getProperty("openlineage.fvt.server.secrets.store", null);
        String secretsCollection    = getProperty("openlineage.fvt.server.secrets.collection", null);

        if ((secretsStoreLocation == null) || (secretsCollection == null))
        {
            return null;
        }

        try
        {
            JsonNode secrets = new ObjectMapper(new YAMLFactory()).readTree(new File(secretsStoreLocation))
                                       .path("secretsCollections").path(secretsCollection).path("secrets");

            return secrets.path(secretName).asText(null);
        }
        catch (Exception error)
        {
            System.out.println("openlineage-fvt: could not read '" + secretName + "' from secrets collection " + secretsCollection
                                       + " in " + secretsStoreLocation + " (" + error.getClass().getSimpleName() + ": "
                                       + error.getMessage() + ")");
            return null;
        }
    }


    /**
     * Locate the repo's shared top-level {@code content-packs} directory by walking up from the current
     * working directory until a directory containing a "content-packs" subdirectory is found.  A fixed
     * relative path is not reliable here - Gradle's test worker process does not always use this module's
     * project directory as its working directory - so the number of levels needed cannot be assumed.
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
                                                + System.getProperty("user.dir")
                                                + " - is this suite being run from outside the egeria repository checkout?");
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
                PlatformServicesClient platformServicesClient = new PlatformServicesClient("openlineage-fvt Platform",
                                                                                           platformURLRoot,
                                                                                           null,
                                                                                           null,
                                                                                           null,
                                                                                           USER_ID,
                                                                                           null);

                /*
                 * Shut the governance servers down before the metadata access store they depend on, so that
                 * neither is left calling a server that has gone.
                 */
                for (String serverName : new String[]{INTEGRATION_DAEMON_NAME, ENGINE_HOST_NAME, METADATA_STORE_NAME})
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
                System.out.println("Problem shutting down the openlineage-fvt platform: " + error.getMessage());
            }
            finally
            {
                platformContext = null;
                started         = false;
                startupFailure  = null;
            }
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


    /**
     * The file in which the port used by the last run is remembered.
     */
    private static final java.io.File LAST_PORT_FILE = new java.io.File("build/openlineage-fvt-data/platform.port");


    /**
     * Choose the platform's port: the one the previous run used if it is still free, otherwise a fresh one.
     * <br>
     * The repository persists between runs and the catalogue in it names the platform by URL - every product's
     * connection carries an endpoint with the platform's address, and the subscription manager and provisioner
     * dial it.  A fresh port on every run made every one of those endpoints stale on every run: Jacquard
     * repaired the ones it knew at start-up, the rest were repaired only when the harvest reached them a few
     * minutes in, and anything provisioned before then dialled a platform that no longer existed.  None of that
     * happens to a deployment, whose platform keeps its address.  Reusing the port makes the suite behave the
     * same way, while a second checkout on the same machine still gets a port of its own when the remembered
     * one is taken.
     *
     * @return port number
     */
    private static int choosePort()
    {
        Integer lastPort = readLastPort();

        if (lastPort != null)
        {
            try (java.net.ServerSocket socket = new java.net.ServerSocket(lastPort))
            {
                socket.setReuseAddress(true);
                return lastPort;
            }
            catch (java.io.IOException notFree)
            {
                System.out.println("openlineage-fvt: the port used by the last run (" + lastPort + ") is in use - allocating another");
            }
        }

        int port = allocateFreePort();

        rememberPort(port);

        return port;
    }


    /**
     * Read the port used by the previous run, if it was recorded.
     *
     * @return port number or null
     */
    private static Integer readLastPort()
    {
        try
        {
            if (LAST_PORT_FILE.isFile())
            {
                return Integer.parseInt(java.nio.file.Files.readString(LAST_PORT_FILE.toPath()).trim());
            }
        }
        catch (Exception ignored)
        {
            // An unreadable or malformed file is treated as no record: a fresh port is allocated and recorded.
        }

        return null;
    }


    /**
     * Record the port this run is using, for the next run to prefer.
     *
     * @param port port number
     */
    private static void rememberPort(int port)
    {
        try
        {
            java.nio.file.Files.createDirectories(LAST_PORT_FILE.toPath().getParent());
            java.nio.file.Files.writeString(LAST_PORT_FILE.toPath(), Integer.toString(port));
        }
        catch (Exception error)
        {
            System.out.println("openlineage-fvt: could not record the platform port for the next run - " + error.getMessage());
        }
    }

}
