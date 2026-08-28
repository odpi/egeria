/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.postgresfvt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.odpi.openmetadata.adminservices.client.EngineHostConfigurationClient;
import org.odpi.openmetadata.adminservices.client.IntegrationDaemonConfigurationClient;
import org.odpi.openmetadata.adminservices.client.MetadataAccessStoreConfigurationClient;
import org.odpi.openmetadata.adminservices.client.OMAGServerConfigurationClient;
import org.odpi.openmetadata.adminservices.client.ViewServerConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationGroupConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
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
 * OMAGPlatformExtension stands up the whole deployment that the PostgreSQL connectors are designed to run
 * in, in one JVM: an OMAG Server Platform and, on it, the four servers a curator would need in order to
 * catalogue and survey a PostgreSQL server.
 * <ul>
 *     <li><b>{@value #METADATA_STORE_NAME}</b> - a metadata access store with a PostgreSQL local repository,
 *     the open metadata types and the Core and PostgreSQL content packs loaded, and its access services
 *     publishing to a real Apache Kafka broker.</li>
 *     <li><b>{@value #ENGINE_HOST_NAME}</b> - an engine host running the two governance engines the
 *     PostgreSQL content pack defines: {@code PostgreSQLSurvey} and {@code PostgreSQLGovernance}.</li>
 *     <li><b>{@value #INTEGRATION_DAEMON_NAME}</b> - an integration daemon running the content pack's
 *     {@code PostgreSQLIntegrationGroup}, which is where the PostgreSQL Server Cataloguer lives.</li>
 *     <li><b>{@value #VIEW_SERVER_NAME}</b> - a view server running the Automated Curation OMVS, the API a
 *     curator uses to ask for any of the above to happen.</li>
 * </ul>
 * The Kafka broker is not optional and it is worth being explicit about why, because nothing in the suite
 * mentions Kafka directly.  Neither governance server is told what to run: each one asks the metadata access
 * store for its configuration at start-up and then <em>listens</em> for changes and for new engine actions on
 * the access services' out topics.  With no broker, all four servers still start and the repository still
 * answers queries - but an engine action sits at {@code WAITING} for ever because no engine host ever hears
 * about it, and that is the failure this suite is most likely to be diagnosing.
 * <br>
 * The servers are configured and started in dependency order: the metadata access store first, because it is
 * where the other three read their configuration from, and the view server last, because it is only useful
 * once there is something behind it to drive.  After the governance servers are started this class waits for
 * their engines and integration groups to reach {@code RUNNING} rather than assuming that an activated server
 * is a ready one - both are populated from the metadata access store <em>after</em> activation returns, and a
 * suite that started testing before they were ready would report a content pack problem for what is really a
 * timing one.
 * <br>
 * It follows the JUnit 5 "singleton resource" pattern: the first test class extended with this class pays the
 * one-off startup cost in its {@code @BeforeAll}; every other extended class reuses the same running
 * platform, and it is shut down once when the whole run finishes.
 */
public class OMAGPlatformExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource
{
    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(OMAGPlatformExtension.class);
    private static final String                     STORE_KEY = OMAGPlatformExtension.class.getName();

    /**
     * The user this suite configures and drives every server as.  {@link PostgresFvtSecurityConfig} also names
     * the platform's anonymous principal after it, so the calls this suite makes into the view server arrive
     * as this user too.
     */
    public static final String USER_ID = "postgresfvtuser";

    /**
     * The metadata access store: PostgreSQL local repository, all access services with Kafka out topics, and
     * the open metadata types plus the Core and PostgreSQL content packs loaded at start-up.
     */
    public static final String METADATA_STORE_NAME = "postgresFvtMetadataStore";

    /**
     * The engine host running the PostgreSQL content pack's survey and governance engines.
     */
    public static final String ENGINE_HOST_NAME = "postgresFvtEngineHost";

    /**
     * The integration daemon running the PostgreSQL content pack's integration group.
     */
    public static final String INTEGRATION_DAEMON_NAME = "postgresFvtIntegrationDaemon";

    /**
     * The view server running the Automated Curation OMVS.
     */
    public static final String VIEW_SERVER_NAME = "postgresFvtViewServer";

    /**
     * A fixed local metadata collection id for the metadata access store's repository.  This is deliberately
     * stable rather than generated afresh on every run, because the underlying PostgreSQL schema persists
     * across runs: keeping the id constant means metadata created by an earlier run is still recognised as
     * belonging to "this" repository on a later one.
     */
    private static final String METADATA_COLLECTION_ID = "1c2f4d3e-6a58-4b90-9c17-706f73677265";

    /**
     * The archives loaded at start-up, in dependency order.
     * <br>
     * This is the shortest list that makes the PostgreSQL content pack work, and each entry earns its place.
     * The types have to be there before any instance can be created.  The Core content pack supplies the
     * generic governance services that the PostgreSQL processes call - create an asset from a template,
     * delete it again, add it to an integration connector as a catalog target - and the JDBC Database
     * Cataloguer that the PostgreSQL Server Cataloguer hands each database on to.  The PostgreSQL content
     * pack itself supplies everything this suite is here to test.
     * <br>
     * Nothing else is loaded.  The other content packs would add load time and a large amount of unrelated
     * metadata for the suite's searches to work around, and none of it is reachable from the processes under
     * test.
     */
    private static final List<String> ARCHIVE_FILES = List.of("OpenMetadataTypes.omarchive",
                                                              "CoreContentPack.omarchive",
                                                              "PostgresContentPack.omarchive");

    /**
     * The integration group this suite's integration daemon runs, and the engines its engine host runs.  They
     * are named from the content pack's own definitions rather than as string literals, so that a rename in
     * the content pack is a compile failure here rather than a server that starts with nothing in it.
     * <br>
     * The Stewardship engine is in the list even though it belongs to the Core content pack rather than the
     * PostgreSQL one, and it is not optional.  The PostgreSQL "create and survey" process ends with a third
     * step that writes the survey report out as a markdown document, and that step is addressed to the
     * Stewardship engine.  Without it the first two steps would run, the survey report would be produced, and
     * then the process would sit at its last step for ever - which reads exactly like a broken survey.
     */
    static final IntegrationGroupDefinition INTEGRATION_GROUP = IntegrationGroupDefinition.POSTGRES;

    static final List<GovernanceEngineDefinition> GOVERNANCE_ENGINES = List.of(GovernanceEngineDefinition.POSTGRES_SURVEY_ENGINE,
                                                                               GovernanceEngineDefinition.POSTGRES_GOVERNANCE_ENGINE,
                                                                               GovernanceEngineDefinition.STEWARDSHIP_ENGINE);

    private static volatile boolean               started = false;
    private static volatile Exception             startupFailure;
    private static ConfigurableApplicationContext platformContext;
    private static String                         platformURLRoot;


    /**
     * Return the root URL of the running platform, for example "http://localhost:9451".
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
                System.out.println("postgres-fvt: " + propertyName + " is not a number ('" + value + "') - using " + defaultValue);
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
     * Normally it is: the access services publish to Apache Kafka, the engine host is told about each new
     * engine action as it is requested, and work starts promptly.  Started with
     * {@code -PrunPostgresFvtNoKafka}, the access services are configured without out topics and there is no
     * broker in the picture at all - nothing is published, and the governance servers are left with only what
     * they poll for.
     * <br>
     * That second mode is worth running because an event bus is not a requirement for an engine host, and this
     * is what says so.  Every test in this suite is expected to pass either way; the difference is how quickly
     * an engine action is picked up, not whether it is.
     *
     * @return true when the event bus is configured
     */
    static boolean isEventBusConfigured()
    {
        return ! "none".equalsIgnoreCase(getProperty("postgres.fvt.event.bus", "kafka"));
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
             * would then report "port 9451 already in use" - burying the one message that says what actually
             * went wrong.
             */
            if (startupFailure != null)
            {
                throw new IllegalStateException("The postgres-fvt environment failed to start: " + startupFailure.getMessage(),
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
                startPlatform();

                PlatformServicesClient platformServicesClient = getPlatformServicesClient();

                /*
                 * The metadata access store is configured and started first: it is where the other three
                 * servers read their configuration from, so none of them has anything to run until it is up
                 * and the content packs have been loaded into it.
                 */
                configureMetadataStore();
                startServer(platformServicesClient, METADATA_STORE_NAME);

                /*
                 * Clear away everything a previous run left behind BEFORE the governance servers start, and the
                 * order here is not cosmetic.
                 *
                 * An engine host sweeps the repository for unfinished engine actions when its engines load their
                 * configuration, and it does that as it starts.  So a run whose metadata store still holds the
                 * previous run's abandoned actions hands them to this run's engine host, which dutifully carries
                 * them out - creating a previous run's assets moments after start-up, and racing the clean-up
                 * that is deleting those same elements.  Both produce failures that describe the previous run
                 * rather than this one.  Cleaning first means the engine host starts against an empty slate.
                 */
                PostgresFvtTestSupport.cleanUpLeftoverTestElements();

                /*
                 * Prepare the PostgreSQL server under test.  This needs only the metadata store, so it happens
                 * here rather than after the governance servers are up.
                 */
                PostgresFvtTestSupport.prepareServerUnderTest();

                configureEngineHost();
                configureIntegrationDaemon();
                configureViewServer();

                startServer(platformServicesClient, ENGINE_HOST_NAME);
                startServer(platformServicesClient, INTEGRATION_DAEMON_NAME);
                startServer(platformServicesClient, VIEW_SERVER_NAME);

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
     * Start the OMAG Server Platform's Spring Boot application in-process.  All of the Spring Boot
     * configuration (port, the placeholder variables that decide which PostgreSQL server and which Kafka
     * broker are used, logging, security) comes from this module's classpath {@code application.properties}
     * rather than being set programmatically here, so that the environment this suite targets can be changed
     * without touching Java code.
     */
    private void startPlatform()
    {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(OMAGServerPlatform.class);

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
        PlatformServicesClient platformServicesClient = new PlatformServicesClient("postgres-fvt Platform",
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
     * engine host and the integration daemon do, and without the topics they would start, read their
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
        configurationClient.setBasicServerProperties("Egeria postgres-fvt",
                                                     "Metadata access store holding the metadata that the postgres-fvt suite creates and reads.",
                                                     USER_ID,
                                                     "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider",
                                                     "build/postgres-fvt-data/secrets.omsecrets",
                                                     "postgres-fvt",
                                                     platformURLRoot,
                                                     PostgresFvtTestSupport.MAX_PAGE_SIZE);

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

            System.out.println("postgres-fvt: running with no event bus - the access services are configured without out topics,"
                                       + " so the engine host and the integration daemon are working from what they poll for"
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
     * Configure the engine host with the two governance engines the PostgreSQL content pack defines.
     * <br>
     * Each engine is named here, but nothing about what it can do is: the request types it supports, the
     * governance services behind them and the connections used to instantiate those services all come from
     * the content pack, which the engine host reads out of the metadata access store after it starts.  That
     * is the point of the arrangement, and it is why {@link #waitForGovernanceEngines()} checks that the
     * engines really did populate rather than trusting that a started server is a working one.
     *
     * @throws Exception any problem configuring the server is fatal to the whole run
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
        configurationClient.setBasicServerProperties("Egeria postgres-fvt",
                                                     "Engine host running the PostgreSQL content pack's survey and governance engines.",
                                                     USER_ID,
                                                     "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider",
                                                     "build/postgres-fvt-data/secrets.omsecrets",
                                                     "postgres-fvt",
                                                     platformURLRoot,
                                                     PostgresFvtTestSupport.MAX_PAGE_SIZE);

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

        /*
         * Configured in one request, with the whole list as the request body.
         *
         * That is worth doing here rather than adding the engines one at a time, because this call is also
         * where a defect in the REST client connectors used to show: a Java List carries no element type at
         * runtime, so each EngineConfig was serialized without the "class" discriminator the receiving
         * endpoint requires, and the request came back as "Could not resolve subtype of EngineConfig: missing
         * type id property 'class'".  Every admin call that sends a bare list was affected the same way.  The
         * connectors now serialize a collection against the type of its contents, and this suite fails at
         * start-up if that stops being true.
         */
        configurationClient.setEngineHostServicesConfig(engineConfigs);
    }


    /**
     * Configure the integration daemon with the PostgreSQL content pack's integration group.
     * <br>
     * As with the engine host, only the group's qualified name is configured here.  Which connectors are in
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
        configurationClient.setBasicServerProperties("Egeria postgres-fvt",
                                                     "Integration daemon running the PostgreSQL content pack's integration group.",
                                                     USER_ID,
                                                     "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider",
                                                     "build/postgres-fvt-data/secrets.omsecrets",
                                                     "postgres-fvt",
                                                     platformURLRoot,
                                                     PostgresFvtTestSupport.MAX_PAGE_SIZE);

        addAuditLogDestinations(configurationClient);

        IntegrationGroupConfig integrationGroupConfig = new IntegrationGroupConfig();

        integrationGroupConfig.setIntegrationGroupQualifiedName(INTEGRATION_GROUP.getQualifiedName());
        integrationGroupConfig.setOMAGServerName(METADATA_STORE_NAME);
        integrationGroupConfig.setOMAGServerPlatformRootURL(platformURLRoot);

        configurationClient.configureIntegrationGroup(integrationGroupConfig);
    }


    /**
     * Configure the view server with the Automated Curation OMVS - the API this suite uses to ask for
     * governance action processes to be run, and the one a curator would use for the same thing.
     *
     * @throws Exception any problem configuring the server is fatal to the whole run
     */
    private void configureViewServer() throws Exception
    {
        ViewServerConfigurationClient configurationClient = new ViewServerConfigurationClient(VIEW_SERVER_NAME,
                                                                                              platformURLRoot,
                                                                                              null,
                                                                                              null,
                                                                                              null,
                                                                                              USER_ID,
                                                                                              null);

        configurationClient.clearOMAGServerConfig();

        configurationClient.setServerUserId(USER_ID);
        configurationClient.setBasicServerProperties("Egeria postgres-fvt",
                                                     "View server running the Automated Curation OMVS for the postgres-fvt suite.",
                                                     USER_ID,
                                                     "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider",
                                                     "build/postgres-fvt-data/secrets.omsecrets",
                                                     "postgres-fvt",
                                                     platformURLRoot,
                                                     PostgresFvtTestSupport.MAX_PAGE_SIZE);

        addAuditLogDestinations(configurationClient);

        ViewServiceConfig viewServiceConfig = new ViewServiceConfig();

        viewServiceConfig.setOMAGServerName(METADATA_STORE_NAME);
        viewServiceConfig.setOMAGServerPlatformRootURL(platformURLRoot);

        configurationClient.configureViewService(ViewServiceDescription.AUTOMATED_CURATION.getViewServiceURLMarker(),
                                                 viewServiceConfig);
    }


    /**
     * Send each server's audit log to the console and, through the SLF4J destination, to one shared file.
     * <br>
     * The file destination is what makes a run diagnosable.  Almost nothing this suite tests happens in the
     * test's own thread: the test asks the view server to start a governance action and then waits, and when
     * the wait expires the reason is in the engine host's or the integration daemon's audit log - whether the
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
                                        "egeria.omag.postgres-fvt",
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

        System.out.println("postgres-fvt: started " + serverName);
    }


    /**
     * Wait until every configured governance engine has retrieved its definition from the metadata access
     * store and reached {@code RUNNING}.
     * <br>
     * An engine that never gets there has not failed a test - it has made the rest of the run meaningless,
     * because every governance action would sit unclaimed - so this is a start-up failure with the engines'
     * last known statuses in the message rather than something for an individual test to discover.
     *
     * @throws Exception the engines did not start
     */
    private void waitForGovernanceEngines() throws Exception
    {
        EngineHostClient engineHostClient = getEngineHostClient();

        long timeoutMilliseconds = getLongProperty("postgres.fvt.governance.server.timeout.seconds", 180) * 1000;
        long pollMilliseconds    = getLongProperty("postgres.fvt.governance.server.poll.seconds", 2) * 1000;
        long giveUpTime          = System.currentTimeMillis() + timeoutMilliseconds;

        String lastKnownStatuses = "none reported";

        while (System.currentTimeMillis() < giveUpTime)
        {
            List<GovernanceEngineSummary> summaries = engineHostClient.getGovernanceEngineSummaries();

            if (summaries != null)
            {
                StringBuilder statuses    = new StringBuilder();
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
                    System.out.println("postgres-fvt: governance engines running -" + statuses);
                    return;
                }
            }

            Thread.sleep(pollMilliseconds);
        }

        throw new IllegalStateException("The governance engines on " + ENGINE_HOST_NAME + " did not reach RUNNING within "
                                                + (timeoutMilliseconds / 1000) + " seconds (last known: " + lastKnownStatuses
                                                + ").  An engine that stays CONFIGURING has not found its definition in "
                                                + METADATA_STORE_NAME + " - check that the PostgreSQL content pack loaded.");
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

        long timeoutMilliseconds = getLongProperty("postgres.fvt.governance.server.timeout.seconds", 180) * 1000;
        long pollMilliseconds    = getLongProperty("postgres.fvt.governance.server.poll.seconds", 2) * 1000;
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
                                                                    + " missing from postgres-fvt's test runtime classpath.");
                        }
                    }

                    System.out.println("postgres-fvt: integration group " + INTEGRATION_GROUP.getQualifiedName() + " running with "
                                               + connectorReports.size() + " connector(s)");
                    return;
                }
            }

            Thread.sleep(pollMilliseconds);
        }

        throw new IllegalStateException("Integration group " + INTEGRATION_GROUP.getQualifiedName() + " on " + INTEGRATION_DAEMON_NAME
                                                + " did not reach RUNNING with connectors within " + (timeoutMilliseconds / 1000)
                                                + " seconds (last known status: " + lastKnownStatus + ").  Check that the PostgreSQL"
                                                + " content pack loaded into " + METADATA_STORE_NAME + ".");
    }


    /**
     * Return a client for the engine host, used both at start-up and by the tests that check what the engines
     * ended up supporting.
     *
     * @return client
     * @throws Exception problem creating the client
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
        String secretsStoreLocation = getProperty("postgres.fvt.server.secrets.store", null);
        String secretsCollection    = getProperty("postgres.fvt.server.secrets.collection", null);

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
            System.out.println("postgres-fvt: could not read '" + secretName + "' from secrets collection " + secretsCollection
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
                PlatformServicesClient platformServicesClient = new PlatformServicesClient("postgres-fvt Platform",
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
                for (String serverName : new String[]{VIEW_SERVER_NAME, INTEGRATION_DAEMON_NAME, ENGINE_HOST_NAME, METADATA_STORE_NAME})
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
                System.out.println("Problem shutting down the postgres-fvt platform: " + error.getMessage());
            }
            finally
            {
                platformContext = null;
                started         = false;
                startupFailure  = null;
            }
        }
    }
}
