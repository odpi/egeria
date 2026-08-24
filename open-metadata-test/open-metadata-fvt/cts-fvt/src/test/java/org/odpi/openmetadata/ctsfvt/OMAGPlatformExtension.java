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

import java.io.File;
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
     * Return the root URL of the running platform, for example "http://localhost:9450".
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
         * By default the workbench works through every entity type in the model, which is thorough and very
         * slow - a measured run was still going after six hours.  Naming entity types in
         * cts.fvt.workbench.entity.types scopes the run to those types instead, which is what makes this
         * harness usable for checking a specific change rather than certifying the repository outright.
         */
        List<String> testEntityTypes = getListProperty("cts.fvt.workbench.entity.types");

        if (! testEntityTypes.isEmpty())
        {
            System.out.println("cts-fvt: workbench scoped to entity types " + testEntityTypes);

            workbenchConfig.setTestEntityTypes(testEntityTypes);
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
        if (REPOSITORY_KIND == RepositoryKind.POSTGRES)
        {
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
}
