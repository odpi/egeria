/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.typefvt;

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
 * OMAGPlatformExtension starts a single OMAG Server Platform in-process for the whole type-fvt run
 * (no Kafka - a PostgreSQL repository and console audit log are used instead), configures and starts a
 * metadata access store server on it backed by the PostgreSQL repository connector, loads the open
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
     * Name of the metadata access store server that is configured and started for the type-fvt suite.
     */
    public static final String SERVER_NAME = "typeFvtMetadataStore";

    /**
     * UserId used for all admin, platform and metadata calls made by the type-fvt suite.
     */
    public static final String USER_ID = "typefvtuser";

    /**
     * Fixed local metadata collection id for the server's repository.  This is deliberately stable
     * (rather than left to be auto-generated afresh on every run) because the underlying PostgreSQL
     * schema persists across test runs - keeping the collection id constant means metadata created by
     * an earlier run is still recognised as belonging to "this" repository on a later run.
     */
    private static final String METADATA_COLLECTION_ID = "7c8b9d0e-1f2a-4b3c-8d4e-747970656676";

    /**
     * The open metadata archives to load at server startup.
     * <br>
     * Only the type archive is loaded.  The subject under test here is the type definitions themselves -
     * every instance this suite works with, it creates - so the instance content carried by the other
     * content packs would add a substantial load time without adding any type coverage.  (query-fvt, whose
     * tests query pre-existing content, does load the full set.)
     * <br>
     * The path is resolved at runtime (see {@link #findContentPacksDirectory()}) rather than assumed to be
     * a fixed number of "../" above the current working directory - Gradle's test worker process does not
     * always use this module's project directory as its working directory.
     */
    private static final List<String> ARCHIVE_FILES = List.of("OpenMetadataTypes.omarchive");

    private static volatile boolean                 started = false;
    private static ConfigurableApplicationContext    platformContext;
    private static String                            platformURLRoot;


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
                    TypeFvtTestSupport.cleanUpLeftoverTestElements();
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
     * Configure a metadata access store server (PostgreSQL local repository, console audit log, all
     * access services enabled with no event topics, the full content-packs archive set) and start it,
     * checking along the way that the platform is responding and that the server actually came up.
     *
     * @throws Exception any problem configuring or starting the server is fatal to the whole type-fvt run
     */
    private void configureAndStartServer() throws Exception
    {
        PlatformServicesClient platformServicesClient = new PlatformServicesClient("type-fvt Platform",
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
        configurationClient.setBasicServerProperties("Egeria type-fvt",
                                                       "Server used by the type-fvt functional verification test suite",
                                                       USER_ID,
                                                       "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider",
                                                       "build/type-fvt-data/secrets.omsecrets",
                                                       "type-fvt",
                                                       platformURLRoot,
                                                       100);

        Map<String, Object> storageProperties = new HashMap<>();

        storageProperties.put("databaseURL", "~{repositoryDatabaseURL}~?currentSchema=repository_" + SERVER_NAME);
        storageProperties.put("databaseSchema", "repository_" + SERVER_NAME);
        storageProperties.put("secretsStore", "~{egeriaServersSecretsStore}~");
        storageProperties.put("secretsCollectionName", "~{repositorySecretCollectionName}~");

        configurationClient.setPostgreSQLLocalRepository(storageProperties);
        configurationClient.addConsoleAuditLogDestination(new ArrayList<>());
        configurationClient.configureAllAccessServicesNoTopics(new HashMap<>());

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
                new PlatformServicesClient("type-fvt Platform", platformURLRoot, null, null, null, USER_ID, null)
                        .shutdownServer(SERVER_NAME);
            }
            catch (Exception ignoredShutdownFailure)
            {
                // Best-effort - the JVM is about to exit either way.
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
