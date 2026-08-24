/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.clientfvt;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.odpi.openmetadata.adminservices.client.MetadataAccessStoreConfigurationClient;
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
 * OMAGPlatformExtension starts a single OMAG Server Platform in-process for the whole client-fvt run,
 * configures and starts a metadata access store server on it backed by the PostgreSQL repository connector
 * and a real Apache Kafka event bus, loads the open
 * metadata type archive from the repo's top-level {@code content-packs} directory, and shuts everything
 * down once when the test run finishes.
 * <br>
 * It follows the JUnit 5 "singleton resource" pattern: the first test class that is extended with this
 * class pays the one-off startup cost in its {@code @BeforeAll}; every other extended class reuses the same
 * running platform and server.  Registering the extension against the root context's store ties its
 * {@link #close()} call to the end of the whole test run rather than to any one test class.
 * <br>
 * Unlike open-metadata-bvt's platform, this one is entirely configured from
 * {@code src/test/resources/application.properties} (port, and which PostgreSQL server/database/schema
 * backs the repository) - see that file for the knobs available.
 */
public class OMAGPlatformExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource
{
    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(OMAGPlatformExtension.class);
    private static final String                      STORE_KEY = OMAGPlatformExtension.class.getName();

    /**
     * Name of the metadata access store server that is configured and started for the client-fvt suite.
     */
    public static final String SERVER_NAME = "clientFvtMetadataStore";

    /**
     * UserId used for all admin, platform and metadata calls made by the client-fvt suite.
     */
    public static final String USER_ID = "clientfvtuser";

    /**
     * Fixed local metadata collection id for the server's repository.  This is deliberately stable
     * (rather than left to be auto-generated afresh on every run) because the underlying PostgreSQL
     * schema persists across test runs - keeping the collection id constant means metadata created by
     * an earlier run is still recognised as belonging to "this" repository on a later run.
     */
    private static final String METADATA_COLLECTION_ID = "9e0d1f2a-3b4c-4d5e-8f60-636c69656e7466";

    /**
     * The open metadata archives to load at server startup.
     * <br>
     * The type archive plus the core content pack.  The clients under test are exercised against elements
     * this suite creates itself, so the wider content packs would add load time without adding coverage -
     * but the core pack carries the valid metadata values and reference data that several clients
     * (ValidMetadataValuesClient, SpecificationPropertyClient) read, so it is worth its startup cost.
     * <br>
     * The path is resolved at runtime (see {@link #findContentPacksDirectory()}) rather than assumed to be
     * a fixed number of "../" above the current working directory - Gradle's test worker process does not
     * always use this module's project directory as its working directory.
     */
    private static final List<String> ARCHIVE_FILES = List.of("OpenMetadataTypes.omarchive",
                                                              "CoreContentPack.omarchive");

    private static volatile boolean                 started = false;
    private static ConfigurableApplicationContext    platformContext;
    private static String                            platformURLRoot;


    /**
     * Return the root URL of the running platform, for example "http://localhost:9446".
     *
     * @return URL root
     */
    public static String getPlatformURLRoot()
    {
        return platformURLRoot;
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
                    configureAndStartServer();
                    ClientFvtTestSupport.cleanUpLeftoverTestElements();
                    started = true;

                    context.getRoot().getStore(NAMESPACE).put(STORE_KEY, this);
                }
            }
        }
    }


    /**
     * Start the OMAG Server Platform's Spring Boot application in-process.  All of the Spring Boot
     * configuration (port, placeholder variables that control which PostgreSQL server is used, logging,
     * security) comes from this module's classpath {@code application.properties} - see that file for
     * details - rather than being set programmatically here, so that the environment this suite targets
     * can be changed without touching Java code.
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
     * Configure a metadata access store server (PostgreSQL local repository, console audit log, all
     * access services enabled with no event topics, the full content-packs archive set) and start it,
     * checking along the way that the platform is responding and that the server actually came up.
     *
     * @throws Exception any problem configuring or starting the server is fatal to the whole client-fvt run
     */
    private void configureAndStartServer() throws Exception
    {
        PlatformServicesClient platformServicesClient = new PlatformServicesClient("client-fvt Platform",
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

        MetadataAccessStoreConfigurationClient configurationClient = new MetadataAccessStoreConfigurationClient(SERVER_NAME,
                                                                                                                  platformURLRoot,
                                                                                                                  null,
                                                                                                                  null,
                                                                                                                  null,
                                                                                                                  USER_ID,
                                                                                                                  null);

        // A config document from an earlier run may still be on disk (for example if this suite was run
        // before with a different startup archive list) - clear it first so the config built below is
        // the only thing that ends up in it, rather than being appended to leftover state.
        configurationClient.clearOMAGServerConfig();

        configurationClient.setServerUserId(USER_ID);
        // setBasicServerProperties insists on a fully specified secrets store connection (provider, location
        // and collection all non-null) even though this suite's server never actually looks up a secret
        // through this particular connection (the real secrets lookup, for the PostgreSQL credentials, is
        // configured separately below via setPostgreSQLLocalRepository).
        configurationClient.setBasicServerProperties("Egeria client-fvt",
                                                       "Server used by the client-fvt functional verification test suite",
                                                       USER_ID,
                                                       "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider",
                                                       "build/client-fvt-data/secrets.omsecrets",
                                                       "client-fvt",
                                                       platformURLRoot,
                                                       100);

        Map<String, Object> storageProperties = new HashMap<>();

        storageProperties.put("databaseURL", "~{repositoryDatabaseURL}~?currentSchema=repository_" + SERVER_NAME);
        storageProperties.put("databaseSchema", "repository_" + SERVER_NAME);
        storageProperties.put("secretsStore", "~{egeriaServersSecretsStore}~");
        storageProperties.put("secretsCollectionName", "~{repositorySecretCollectionName}~");

        configurationClient.setPostgreSQLLocalRepository(storageProperties);
        configurationClient.addConsoleAuditLogDestination(new ArrayList<>());

        /*
         * This suite differs from query-fvt and type-fvt here: it declares an event bus and configures the
         * access services *with* their event topics, so the OMASs publish out-topic events to Apache Kafka
         * as they would on a real deployment.  Exercising the clients with the event infrastructure switched
         * on is the point - a client call that behaves correctly against a silent server but trips over
         * event publication is exactly the kind of fault this suite exists to surface.
         *
         * The broker address comes from the "kafkaEndpoint" placeholder variable in application.properties.
         * The topic root is unique to this suite so that its events cannot be confused with, or consumed by,
         * anything else using the same broker.
         */
        Map<String, Object> eventBusProperties = new HashMap<>();
        Map<String, Object> bootstrapServers   = new HashMap<>();

        bootstrapServers.put("bootstrap.servers", "~{kafkaEndpoint}~");

        eventBusProperties.put("producer", bootstrapServers);
        eventBusProperties.put("consumer", bootstrapServers);

        configurationClient.setEventBus("org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider",
                                          "egeria.omag.client-fvt",
                                          eventBusProperties);

        configurationClient.configureAllAccessServices(new HashMap<>());

        /*
         * The metadata collection id is set through the administration services call that exists for it:
         *
         *     POST {platform}/open-metadata/admin-services/servers/{server}/local-repository/metadata-collection-id
         *
         * This used to be done by editing the whole config document, because the client method sent the id as
         * a JSON-quoted string and the server - which binds it as an opaque @RequestBody String - stored it
         * with the quotation marks still attached.  The REST client connector now sends a string request body
         * as text/plain, so the client method records the id as given.
         */
        configurationClient.setLocalMetadataCollectionId(METADATA_COLLECTION_ID);

        for (String archiveFileName : ARCHIVE_FILES)
        {
            configurationClient.addStartUpOpenMetadataArchiveFile(new File(findContentPacksDirectory(), archiveFileName).getAbsolutePath());
        }

        platformServicesClient.activateWithStoredConfig(SERVER_NAME);

        if (! platformServicesClient.isServerKnown(SERVER_NAME))
        {
            throw new IllegalStateException("Server " + SERVER_NAME + " did not start on platform " + platformURLRoot);
        }
    }


    /**
     * Locate the repo's shared top-level {@code content-packs} directory by walking up from the current
     * working directory until a directory containing a "content-packs" subdirectory is found.  A fixed
     * relative path (a hardcoded number of "../") is not reliable here - Gradle's test worker process
     * does not always use this module's project directory as its working directory (for example, when an
     * existing worker process is reused across tasks/modules), so the number of levels needed to reach
     * the repo root cannot be assumed.
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
            try
            {
                new PlatformServicesClient("client-fvt Platform", platformURLRoot, null, null, null, USER_ID, null)
                        .shutdownServer(SERVER_NAME);
            }
            catch (Exception ignoredShutdownFailure)
            {
                // Best-effort - the JVM is about to exit either way.
            }

            platformContext.close();
        }
    }
}
