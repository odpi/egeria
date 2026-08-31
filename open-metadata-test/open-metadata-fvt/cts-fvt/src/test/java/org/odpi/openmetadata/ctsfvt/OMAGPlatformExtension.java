/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.ctsfvt;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.odpi.openmetadata.adminservices.client.ConformanceTestServerConfigurationClient;
import org.odpi.openmetadata.adminservices.client.MetadataAccessStoreConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.properties.RepositoryConformanceWorkbenchConfig;
import org.odpi.openmetadata.platformchassis.springboot.OMAGServerPlatform;
import org.odpi.openmetadata.platformservices.client.PlatformServicesClient;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OMAGPlatformExtension stands up everything the Open Metadata Conformance Suite needs, in one JVM:
 * an OMAG Server Platform, and on it the two servers that a conformance run is made of.
 * <ul>
 *     <li><b>The technology under test</b> - a metadata access store whose local repository is the one
 *     being certified, either the PostgreSQL or the in-memory repository connector.  Which one is decided
 *     by the Gradle property that started the run - see {@link RepositoryKind}.</li>
 *     <li><b>The conformance test server</b> - running the repository workbench, pointed at the
 *     technology under test by name.</li>
 * </ul>
 * The two are joined by a real cohort on a real Apache Kafka broker, and that is the part worth being
 * explicit about: the workbench never calls the technology under test directly.  It waits for it to
 * register in the cohort, picks it up through the enterprise connector manager, and drives it from there.
 * So a conformance run exercises cohort registration and the OMRS event exchange just as much as it
 * exercises the repository - if the cohort does not form, the workbench simply waits, and reports nothing.
 * <br>
 * It follows the JUnit 5 "singleton resource" pattern: the first test class extended with this class pays
 * the one-off startup cost in its {@code @BeforeAll}; every other extended class reuses the same running
 * platform, and it is shut down once when the whole run finishes.
 */
public class OMAGPlatformExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource
{
    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(OMAGPlatformExtension.class);
    private static final String                     STORE_KEY = OMAGPlatformExtension.class.getName();

    /**
     * The user this harness configures and drives both servers as.
     */
    public static final String USER_ID = "garygeeke";

    /**
     * Which local repository the technology under test runs.  Everything that has to differ between the
     * two - server names, cohort name, metadata collection ids - comes from here, so that a run of one
     * kind never meets anything left behind by a run of the other.
     */
    static final RepositoryKind REPOSITORY_KIND = RepositoryKind.getConfiguredKind();

    /**
     * The technology under test: a metadata access store whose local repository is the one being tested.
     */
    public static final String TUT_SERVER_NAME = REPOSITORY_KIND.getTutServerName();

    /**
     * The conformance test server, which runs the repository workbench against the technology under test.
     */
    public static final String CTS_SERVER_NAME = REPOSITORY_KIND.getCtsServerName();

    /**
     * The cohort both servers join.  The workbench discovers the technology under test through this
     * cohort rather than being given an address for it, so both servers must name the same one.
     */
    public static final String COHORT_NAME = REPOSITORY_KIND.getCohortName();

    /**
     * A fixed metadata collection id for the technology under test, so that a repeated run reports
     * against the same repository identity rather than a freshly generated one.
     */
    private static final String TUT_METADATA_COLLECTION_ID = REPOSITORY_KIND.getTutMetadataCollectionId();

    /**
     * A fixed metadata collection id for the conformance test server too.
     * <br>
     * This one is not cosmetic.  The conformance test server's local repository is in-memory and is given a
     * freshly generated collection id every time it starts, but its cohort registry store is a file that
     * outlives the run - and it is kept under {@code data/servers}, outside {@code build}, so even a clean
     * build does not remove it.  On the second run the server therefore tries to rejoin the cohort it is
     * already registered with under a different identity, and the repository services refuse to start it:
     * "the local metadata collection id has been changed ... since this server registered with the cohort".
     * Pinning the id means the server rejoins as itself every time.
     */
    private static final String CTS_METADATA_COLLECTION_ID = REPOSITORY_KIND.getCtsMetadataCollectionId();

    /**
     * The open metadata type definitions.  The technology under test needs the types loaded before the
     * workbench can ask it to create instances of them - but nothing beyond the types is wanted here.
     * The workbench builds its own test instances, and loading the content packs on top would add a large
     * amount of unrelated metadata to work around without testing anything more.
     */
    private static final String TYPES_ARCHIVE_FILE = "OpenMetadataTypes.omarchive";

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
     * Return a numeric setting from the platform's own configuration, so that values such as how long this
     * harness is prepared to wait for the workbench live in application.properties alongside everything
     * else it is configured with, rather than being compiled in.
     *
     * @param propertyName name of the property
     * @param defaultValue value to use when the property is absent or unreadable
     * @return configured value
     */
    public static long getLongProperty(String propertyName, long defaultValue)
    {
        if (platformContext != null)
        {
            String value = platformContext.getEnvironment().getProperty(propertyName);

            if ((value != null) && (! value.isBlank()))
            {
                try
                {
                    return Long.parseLong(value.trim());
                }
                catch (NumberFormatException error)
                {
                    System.out.println("cts-fvt: " + propertyName + " is not a number ('" + value
                                               + "') - using " + defaultValue);
                }
            }
        }

        return defaultValue;
    }


    /**
     * Remove the cohort registry stores of both servers, so that they join the cohort afresh.
     * <br>
     * The registry files live under {@code data/servers}, outside {@code build}, and outlive the run - which
     * is what lets a server rejoin the cohort it already belongs to.  That is the right behaviour for a real
     * deployment and the wrong one here, because a server that is already registered has no reason to
     * exchange its type definitions again, and the workbench has test cases that exist only to observe that
     * exchange.  A run against a registry left over from a previous run was measured recording 44 type
     * definition events where a fresh one recorded 4092, and the event-driven test cases either never fired
     * or failed waiting for what never arrived.
     * <br>
     * Removing the registries costs nothing else: the metadata collection ids are pinned, so each server
     * rejoins as itself rather than as a stranger.
     */
    private static void clearDownCohortRegistries()
    {
        if (! Boolean.parseBoolean(getProperty("cts.fvt.repository.clear.down", "true")))
        {
            return;
        }

        for (String serverName : new String[]{ TUT_SERVER_NAME, CTS_SERVER_NAME })
        {
            File cohortDirectory = new File("data/servers/" + serverName + "/cohorts");

            File[] registryFiles = cohortDirectory.listFiles();

            if (registryFiles == null)
            {
                continue;
            }

            for (File registryFile : registryFiles)
            {
                if (registryFile.isFile())
                {
                    if (registryFile.delete())
                    {
                        System.out.println("cts-fvt: removed cohort registry " + registryFile.getPath()
                                                   + " - the servers join the cohort afresh");
                    }
                    else
                    {
                        System.out.println("cts-fvt: could not remove cohort registry " + registryFile.getPath()
                                                   + " - the servers will rejoin an existing cohort registration, and the"
                                                   + " test cases that observe the type definition exchange may not fire");
                    }
                }
            }
        }
    }


    /**
     * Drop the PostgreSQL repository's schema, so that the repository under test starts empty.
     * <br>
     * The workbench is written for a repository that starts empty - it creates the instances it needs and
     * expects searches to see its own data.  The in-memory repository satisfies that by construction; a
     * PostgreSQL one does not, because its schema outlives the run.  What accumulates is not small: a
     * measured schema held 20322 entities and 255502 attribute values left over from previous runs, and
     * every unbounded search in the workbench pays for them, which showed up as a run taking 2h36m where an
     * equivalent one had taken 33 minutes.
     * <br>
     * This is done before the run rather than after it deliberately.  Clearing up afterwards leaves the
     * debris behind whenever a run is killed or crashes - which is exactly when it is most likely to be left
     * in a state the next run should not inherit.  Clearing beforehand makes the guarantee unconditional.
     * Set cts.fvt.repository.clear.down to false to keep a repository for inspection after a run.
     */
    private static void clearDownRepositorySchema()
    {
        String schemaName = "repository_" + TUT_SERVER_NAME;

        if (! Boolean.parseBoolean(getProperty("cts.fvt.repository.clear.down", "true")))
        {
            System.out.println("cts-fvt: leaving schema " + schemaName + " in place - cts.fvt.repository.clear.down is false."
                                       + "  The workbench expects an empty repository, so a run against a schema holding"
                                       + " previous results may report failures that say more about the leftovers than the repository.");
            return;
        }

        try
        {
            JsonNode placeholders = new ObjectMapper().readTree(getProperty("platform.placeholder.variables", "{}"));

            String databaseURL     = placeholders.path("repositoryDatabaseURL").asText(null);
            String secretsStore    = placeholders.path("egeriaServersSecretsStore").asText(null);
            String secretCollection = placeholders.path("repositorySecretCollectionName").asText(null);

            if ((databaseURL == null) || (secretsStore == null) || (secretCollection == null))
            {
                System.out.println("cts-fvt: cannot clear schema " + schemaName
                                           + " - the platform placeholder variables do not carry the database URL and secrets store location");
                return;
            }

            /*
             * The credentials come from the same secrets store the server itself is configured with, rather
             * than being repeated here, so there is only one place to change them.
             */
            JsonNode secrets = new ObjectMapper(new YAMLFactory()).readTree(new File(secretsStore))
                                       .path("secretsCollections").path(secretCollection).path("secrets");

            String userId   = secrets.path("userId").asText(null);
            String password = secrets.path("clearPassword").asText(null);

            try (Connection connection = DriverManager.getConnection(databaseURL, userId, password);
                 Statement statement = connection.createStatement())
            {
                statement.execute("drop schema if exists " + schemaName + " cascade");
            }

            System.out.println("cts-fvt: dropped schema " + schemaName + " - the repository under test starts empty");
        }
        catch (Exception error)
        {
            /*
             * Reported rather than thrown: the run can still go ahead against whatever is there, and the
             * conformance result is what matters.  The message says what was not done so that a slow run or
             * an unexpected search result is not a mystery.
             */
            System.out.println("cts-fvt: could not clear schema " + schemaName + " (" + error.getClass().getSimpleName()
                                       + ": " + error.getMessage() + ") - the run continues, but the repository under test"
                                       + " is not empty and both its timings and its results may reflect that");
        }
    }


    /**
     * Return a setting from the platform's own configuration, or the supplied default when it is absent.
     *
     * @param propertyName name of the property
     * @param defaultValue value to use when the property is not set
     * @return configured value
     */
    private static String getProperty(String propertyName, String defaultValue)
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
     * Return a comma-separated setting from the platform's own configuration as a list, empty when the
     * property is absent or blank.
     *
     * @param propertyName name of the property
     * @return configured values
     */
    private static List<String> getListProperty(String propertyName)
    {
        List<String> values = new ArrayList<>();

        if (platformContext != null)
        {
            String value = platformContext.getEnvironment().getProperty(propertyName);

            if ((value != null) && (! value.isBlank()))
            {
                for (String element : value.split(","))
                {
                    if (! element.isBlank())
                    {
                        values.add(element.trim());
                    }
                }
            }
        }

        return values;
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
                     * The conformance test server is configured and started first, so that its workbench is
                     * already listening on the cohort when the technology under test registers.  Started the
                     * other way round, the workbench relies on catching up with a registration that has
                     * already happened, which is a slower and less certain path to the same place.
                     */
                    configureConformanceTestServer();
                    configureTechnologyUnderTest();

                    PlatformServicesClient platformServicesClient = getPlatformServicesClient();

                    startServer(platformServicesClient, CTS_SERVER_NAME);
                    startServer(platformServicesClient, TUT_SERVER_NAME);

                    started = true;

                    context.getRoot().getStore(NAMESPACE).put(STORE_KEY, this);
                }
            }
        }
    }


    /**
     * Start the OMAG Server Platform's Spring Boot application in-process.  All of the Spring Boot
     * configuration (port, placeholder variables that control which PostgreSQL server and which Kafka
     * broker are used, logging, security) comes from this module's classpath {@code application.properties}
     * rather than being set programmatically here, so that the environment this harness targets can be
     * changed without touching Java code.
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
     * Return a platform services client pointed at the running platform, checking as it does so that the
     * platform is actually responding.
     *
     * @return client
     * @throws Exception the platform is not answering
     */
    private PlatformServicesClient getPlatformServicesClient() throws Exception
    {
        PlatformServicesClient platformServicesClient = new PlatformServicesClient("cts-fvt Platform",
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
     * Configure the conformance test server: a cohort member with no access services of its own, running
     * the repository workbench against the technology under test.
     * <br>
     * Enabling the workbench does more than record the workbench's own configuration.  It also sets the
     * server type, gives the server an in-memory local repository, and builds its enterprise access
     * configuration - the enterprise connector manager is how the workbench reaches the technology under
     * test once the cohort has formed - so there is deliberately no local repository set up here.
     *
     * @throws Exception any problem configuring the server is fatal to the whole run
     */
    private void configureConformanceTestServer() throws Exception
    {
        ConformanceTestServerConfigurationClient configurationClient =
                new ConformanceTestServerConfigurationClient(CTS_SERVER_NAME,
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
        configurationClient.setBasicServerProperties("Egeria cts-fvt conformance test server",
                                                     "Runs the repository workbench against " + TUT_SERVER_NAME + ".",
                                                     USER_ID,
                                                     "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider",
                                                     "build/cts-fvt-data/secrets.omsecrets",
                                                     "cts-fvt",
                                                     platformURLRoot,
                                                     100);

        addAuditLogDestinations(configurationClient);

        /*
         * The event bus has to be declared before the cohort is added: the cohort's registration and
         * instance topic connections are built from it at the point the cohort registration is made, so a
         * cohort added first would be configured with no way to talk to anything.
         */
        setEventBus(configurationClient);

        configurationClient.addCohortRegistration(COHORT_NAME, null);

        RepositoryConformanceWorkbenchConfig workbenchConfig = new RepositoryConformanceWorkbenchConfig();

        workbenchConfig.setTutRepositoryServerName(TUT_SERVER_NAME);
        workbenchConfig.setMaxSearchResults(50);

        /*
         * The workbench works through every entity type in the model unless it is told otherwise, which is
         * thorough and very slow - a measured run was still going after six hours.  So this harness ships
         * with cts.fvt.workbench.entity.types set to a small set of types, making an ordinary run a quick
         * check of a change; widening it, or emptying it to cover the whole model, is a deliberate act by
         * whoever wants the fuller answer.  See the notes beside the setting in application.properties.
         */
        List<String> testEntityTypes = getListProperty("cts.fvt.workbench.entity.types");

        if (! testEntityTypes.isEmpty())
        {
            System.out.println("cts-fvt: workbench scoped to entity types " + testEntityTypes
                                       + " - set cts.fvt.workbench.entity.types to widen or empty it to test every type");

            workbenchConfig.setTestEntityTypes(testEntityTypes);
        }
        else
        {
            System.out.println("cts-fvt: workbench testing every entity type in the model - this takes many hours");
        }

        /*
         * The relationship and classification types can be narrowed independently of the entity types.  They
         * are otherwise derived: a relationship is tested when both of its ends are among the entity types in
         * the run, and a classification when any of its valid entity types is - and because the entity types
         * bring their supertypes with them, that derived set is much larger than the named entity types
         * suggest.  Naming the relationship and classification types wanted is what keeps a scoped run short.
         */
        List<String> testRelationshipTypes = getListProperty("cts.fvt.workbench.relationship.types");

        if (! testRelationshipTypes.isEmpty())
        {
            System.out.println("cts-fvt: workbench scoped to relationship types " + testRelationshipTypes);

            workbenchConfig.setTestRelationshipTypes(testRelationshipTypes);
        }

        /*
         * How long a test case waits for an event to propagate before deciding it is not coming.  The default
         * matches the conformance suite's own, and is left alone deliberately: raising it on a machine where
         * propagation is genuinely slow tells "not yet" apart from "never", but raising it by default would
         * turn a visible timing sensitivity into an invisible one.
         */
        workbenchConfig.setEventPollCount((int) getLongProperty("cts.fvt.workbench.event.poll.count", 300));
        workbenchConfig.setEventPollPeriod((int) getLongProperty("cts.fvt.workbench.event.poll.period.ms", 100));

        System.out.println("cts-fvt: event propagation waits are "
                                   + workbenchConfig.getEventPollCount() + " polls of "
                                   + workbenchConfig.getEventPollPeriod() + "ms ("
                                   + ((workbenchConfig.getEventPollCount() * workbenchConfig.getEventPollPeriod()) / 1000)
                                   + "s)");

        List<String> testClassificationTypes = getListProperty("cts.fvt.workbench.classification.types");

        if (! testClassificationTypes.isEmpty())
        {
            System.out.println("cts-fvt: workbench scoped to classification types " + testClassificationTypes);

            workbenchConfig.setTestClassificationTypes(testClassificationTypes);
        }

        configurationClient.enableRepositoryConformanceSuiteWorkbench(workbenchConfig);

        /*
         * Enabling the workbench is what gives this server its in-memory local repository, so the metadata
         * collection id can only be pinned afterwards.
         */
        configurationClient.setLocalMetadataCollectionId(CTS_METADATA_COLLECTION_ID);
    }


    /**
     * Configure the technology under test: a metadata access store whose local repository is the one
     * being certified, in the same cohort as the conformance test server.
     * <br>
     * No access services are configured.  The repository workbench drives the repository services
     * directly through the enterprise connector, so access services would add start-up time and moving
     * parts without adding anything the workbench looks at.
     *
     * @throws Exception any problem configuring the server is fatal to the whole run
     */
    private void configureTechnologyUnderTest() throws Exception
    {
        MetadataAccessStoreConfigurationClient configurationClient =
                new MetadataAccessStoreConfigurationClient(TUT_SERVER_NAME,
                                                           platformURLRoot,
                                                           null,
                                                           null,
                                                           null,
                                                           USER_ID,
                                                           null);

        configurationClient.clearOMAGServerConfig();

        configurationClient.setServerUserId(USER_ID);
        configurationClient.setBasicServerProperties("Egeria cts-fvt technology under test",
                                                     REPOSITORY_KIND.getTutServerDescription(),
                                                     USER_ID,
                                                     "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider",
                                                     "build/cts-fvt-data/secrets.omsecrets",
                                                     "cts-fvt",
                                                     platformURLRoot,
                                                     100);

        /*
         * The only difference between the two kinds of run.  The in-memory repository takes no
         * configuration at all: it needs no database, and it starts empty every time, so the schema and
         * credentials the PostgreSQL repository has to be told about have no counterpart here.
         */
        clearDownCohortRegistries();

        if (REPOSITORY_KIND == RepositoryKind.POSTGRES)
        {
            clearDownRepositorySchema();

            Map<String, Object> storageProperties = new HashMap<>();

            storageProperties.put("databaseURL", "~{repositoryDatabaseURL}~?currentSchema=repository_" + TUT_SERVER_NAME);
            storageProperties.put("databaseSchema", "repository_" + TUT_SERVER_NAME);
            storageProperties.put("secretsStore", "~{egeriaServersSecretsStore}~");
            storageProperties.put("secretsCollectionName", "~{repositorySecretCollectionName}~");

            configurationClient.setPostgreSQLLocalRepository(storageProperties);
        }
        else
        {
            configurationClient.setInMemLocalRepository();
        }

        addAuditLogDestinations(configurationClient);

        setEventBus(configurationClient);

        configurationClient.addCohortRegistration(COHORT_NAME, null);

        File typesArchive = new File(findContentPacksDirectory(), TYPES_ARCHIVE_FILE);

        if (! typesArchive.isFile())
        {
            throw new IllegalStateException("Open metadata types archive " + typesArchive.getPath() + " not found");
        }

        configurationClient.addStartUpOpenMetadataArchiveFile(typesArchive.getAbsolutePath());
        configurationClient.setLocalMetadataCollectionId(TUT_METADATA_COLLECTION_ID);
    }


    /**
     * Send each server's audit log to the console and to a file of its own.
     * <br>
     * The file destination is what makes a run diagnosable.  A conformance run is mostly waiting - the
     * workbench does not start until the technology under test registers in the cohort - and when it waits
     * forever the reason is always in the audit log: whether the cohort connected, whether the remote
     * member's registration arrived, and whether a connector could be built for it.  Gradle buffers the
     * console output of a test JVM until the task ends, so on a run that hangs the console destination
     * alone tells you nothing until it is too late to be useful.
     * <br>
     * The SLF4J destination is used rather than the file destination because the file destination writes
     * one file per audit record - hundreds during start-up alone, and far more over a full workbench run.
     * logback-test.xml routes it to a single file.
     *
     * @param configurationClient client for the server being configured
     * @throws Exception problem talking to the admin services
     */
    private void addAuditLogDestinations(org.odpi.openmetadata.adminservices.client.OMAGServerConfigurationClient configurationClient) throws Exception
    {
        configurationClient.addConsoleAuditLogDestination(new ArrayList<>());
        configurationClient.addSLF4JAuditLogDestination(new ArrayList<>());
    }


    /**
     * Declare the Apache Kafka event bus that carries this harness's cohort.  The topic root is unique to
     * this harness so that its cohort traffic cannot be confused with, or consumed by, anything else using
     * the same broker.
     *
     * @param configurationClient client for the server being configured
     * @throws Exception problem talking to the admin services
     */
    private void setEventBus(org.odpi.openmetadata.adminservices.client.OMAGServerConfigurationClient configurationClient) throws Exception
    {
        Map<String, Object> eventBusProperties = new HashMap<>();
        Map<String, Object> bootstrapServers   = new HashMap<>();

        bootstrapServers.put("bootstrap.servers", "~{kafkaEndpoint}~");

        eventBusProperties.put("producer", bootstrapServers);
        eventBusProperties.put("consumer", bootstrapServers);

        configurationClient.setEventBus("org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider",
                                        "egeria.omag.cts-fvt",
                                        eventBusProperties);
    }


    /**
     * Start one of the two servers, checking that it actually came up.
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

        throw new IllegalStateException("Could not locate the repo's content-packs directory by walking up from " +
                                                System.getProperty("user.dir") +
                                                " - is this harness being run from outside the egeria repository checkout?");
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
                platformContext.close();
            }
            catch (Exception error)
            {
                System.out.println("Problem shutting down the cts-fvt platform: " + error.getMessage());
            }
            finally
            {
                platformContext = null;
                started         = false;
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

}
