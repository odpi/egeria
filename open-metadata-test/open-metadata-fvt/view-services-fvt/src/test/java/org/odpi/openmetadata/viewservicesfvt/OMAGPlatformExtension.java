/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.odpi.openmetadata.adminservices.client.MetadataAccessStoreConfigurationClient;
import org.odpi.openmetadata.adminservices.client.ViewServerConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerClientConfig;
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
import java.util.UUID;

/**
 * OMAGPlatformExtension starts one OMAG Server Platform in-process for the whole view-services-fvt run and
 * configures two servers on it:
 * <ul>
 *     <li><b>{@value #METADATA_STORE_NAME}</b> - a metadata access store with either the in-memory or the
 *     PostgreSQL repository, loaded with the open metadata types and the Core Content Pack.  The content pack is
 *     there because several view services (Automated Curation, Template Manager, Valid Metadata, Reference
 *     Data) describe what it defines.</li>
 *     <li><b>{@value #VIEW_SERVER_NAME}</b> - a view server running every registered view service against
 *     the metadata access store.</li>
 * </ul>
 * It follows the JUnit 5 "singleton resource" pattern: the first test class pays the startup cost and every
 * other class reuses the running servers.  {@link #close()} runs once at the end of the whole run, and writes
 * the endpoint coverage report (see {@link EndpointCoverage}) before shutting down.
 */
public class OMAGPlatformExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource
{
    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(OMAGPlatformExtension.class);
    private static final String                      STORE_KEY = OMAGPlatformExtension.class.getName();

    /**
     * Name of the metadata access store server.
     */
    public static final String METADATA_STORE_NAME = "viewServicesFvtMetadataStore";

    /**
     * Name of the view server running every view service.
     */
    public static final String VIEW_SERVER_NAME = "viewServicesFvtViewServer";

    /**
     * UserId for the admin calls, and the user every view service request runs as - the view services take
     * the caller from the security context, which {@link ViewServicesFvtSecurityConfig} sets to this.
     */
    public static final String USER_ID = "viewservicesfvtuser";

    /**
     * A value unique to this run.  Test data carries it in its qualified names so that a run against a
     * PostgreSQL repository holding a previous run's elements neither collides with them nor finds them.
     */
    public static final String RUN_ID = UUID.randomUUID().toString().substring(0, 8);

    private static final String REPOSITORY_PROPERTY  = "view.services.fvt.repository";
    private static final String IN_MEMORY_REPOSITORY = "inmemory";

    private static final String METADATA_COLLECTION_ID = "3f6b2a1c-7d4e-4f58-9a0b-766965777376";

    private static final List<String> ARCHIVE_FILES = List.of("OpenMetadataTypes.omarchive", "CoreContentPack.omarchive");

    private static final int MAX_PAGE_SIZE = 1000;

    private static volatile boolean              started = false;
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
     * Return the platform's Spring context - the coverage report reads the registered request mappings from it.
     *
     * @return application context
     */
    static ConfigurableApplicationContext getPlatformContext()
    {
        return platformContext;
    }


    /**
     * Is this run using the in-memory repository rather than PostgreSQL?
     *
     * @return true if the in-memory repository was selected
     */
    static boolean usingInMemoryRepository()
    {
        return ! "postgres".equals(System.getProperty(REPOSITORY_PROPERTY, IN_MEMORY_REPOSITORY));
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
                    configureAndStartServers();
                    started = true;

                    context.getRoot().getStore(NAMESPACE).put(STORE_KEY, this);
                }
            }
        }
    }


    /**
     * Start the platform's Spring Boot application in-process on a free port.
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
     * Configure and start the metadata access store and then the view server.
     *
     * @throws Exception any problem is fatal to the whole run
     */
    private void configureAndStartServers() throws Exception
    {
        PlatformServicesClient platformServicesClient = new PlatformServicesClient("view-services-fvt Platform",
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

        configureMetadataStore();
        platformServicesClient.activateWithStoredConfig(METADATA_STORE_NAME);

        if (! platformServicesClient.isServerKnown(METADATA_STORE_NAME))
        {
            throw new IllegalStateException("Server " + METADATA_STORE_NAME + " did not start on platform " + platformURLRoot);
        }

        configureViewServer();
        platformServicesClient.activateWithStoredConfig(VIEW_SERVER_NAME);

        if (! platformServicesClient.isServerKnown(VIEW_SERVER_NAME))
        {
            throw new IllegalStateException("Server " + VIEW_SERVER_NAME + " did not start on platform " + platformURLRoot);
        }
    }


    /**
     * Configure the metadata access store: local repository, console audit log, all access services with no
     * event topics, the types and the Core Content Pack.
     *
     * @throws Exception problem with the admin services
     */
    private void configureMetadataStore() throws Exception
    {
        MetadataAccessStoreConfigurationClient configurationClient = new MetadataAccessStoreConfigurationClient(METADATA_STORE_NAME,
                                                                                                                  platformURLRoot,
                                                                                                                  null,
                                                                                                                  null,
                                                                                                                  null,
                                                                                                                  USER_ID,
                                                                                                                  null);

        configurationClient.clearOMAGServerConfig();

        configurationClient.setServerUserId(USER_ID);
        configurationClient.setBasicServerProperties("Egeria view-services-fvt",
                                                     "Metadata access store used by the view-services-fvt suite.",
                                                     USER_ID,
                                                     "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider",
                                                     "build/view-services-fvt-data/secrets.omsecrets",
                                                     "view-services-fvt",
                                                     platformURLRoot,
                                                     MAX_PAGE_SIZE);

        if (usingInMemoryRepository())
        {
            configurationClient.setInMemLocalRepository();
        }
        else
        {
            Map<String, Object> storageProperties = new HashMap<>();

            storageProperties.put("databaseURL", "~{repositoryDatabaseURL}~?currentSchema=repository_" + METADATA_STORE_NAME);
            storageProperties.put("databaseSchema", "repository_" + METADATA_STORE_NAME);
            storageProperties.put("secretsStore", "~{egeriaServersSecretsStore}~");
            storageProperties.put("secretsCollectionName", "~{repositorySecretCollectionName}~");

            configurationClient.setPostgreSQLLocalRepository(storageProperties);
        }

        configurationClient.addConsoleAuditLogDestination(new ArrayList<>());
        configurationClient.configureAllAccessServicesNoTopics(new HashMap<>());
        configurationClient.setLocalMetadataCollectionId(METADATA_COLLECTION_ID);

        for (String archiveFileName : ARCHIVE_FILES)
        {
            configurationClient.addStartUpOpenMetadataArchiveFile(new File(findContentPacksDirectory(), archiveFileName).getAbsolutePath());
        }
    }


    /**
     * Configure the view server with every registered view service, all calling the metadata access store.
     *
     * @throws Exception problem with the admin services
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
        configurationClient.setBasicServerProperties("Egeria view-services-fvt",
                                                     "View server running every view service for the view-services-fvt suite.",
                                                     USER_ID,
                                                     "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider",
                                                     "build/view-services-fvt-data/secrets.omsecrets",
                                                     "view-services-fvt",
                                                     platformURLRoot,
                                                     MAX_PAGE_SIZE);

        configurationClient.addConsoleAuditLogDestination(new ArrayList<>());

        OMAGServerClientConfig metadataServerDetails = new OMAGServerClientConfig();

        metadataServerDetails.setOMAGServerName(METADATA_STORE_NAME);
        metadataServerDetails.setOMAGServerPlatformRootURL(platformURLRoot);

        configurationClient.configureAllViewService(metadataServerDetails, new HashMap<>());
    }


    /**
     * Locate the repository's top-level content-packs directory by walking up from the working directory.
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
                                                System.getProperty("user.dir"));
    }


    /**
     * Write the coverage report, then shut the servers and the platform down.
     */
    @Override
    public void close()
    {
        if (platformContext != null)
        {
            String coverageFailure = EndpointCoverage.writeReport(platformContext);

            try
            {
                PlatformServicesClient platformServicesClient = new PlatformServicesClient("view-services-fvt Platform",
                                                                                           platformURLRoot,
                                                                                           null,
                                                                                           null,
                                                                                           null,
                                                                                           USER_ID,
                                                                                           null);

                platformServicesClient.shutdownServer(VIEW_SERVER_NAME);
                platformServicesClient.shutdownServer(METADATA_STORE_NAME);
            }
            catch (Exception ignoredShutdownFailure)
            {
                // Best-effort - the JVM is about to exit either way.
            }

            platformContext.close();

            if (coverageFailure != null)
            {
                throw new AssertionError(coverageFailure);
            }
        }
    }


    /**
     * Find a port that is free right now.  It must be known before the context starts because the
     * egeriaEndpoint placeholder in application.properties uses it.
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
