/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.templatesfvt;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.adminservices.client.MetadataAccessStoreConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.properties.LocalRepositoryConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.RepositoryServicesConfig;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
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
 * OMAGPlatformExtension starts a single OMAG Server Platform in-process for the whole templates-fvt run
 * (no Kafka - a PostgreSQL repository and console audit log are used instead), configures and starts a
 * metadata access store server on it backed by the PostgreSQL repository connector, loads the full set
 * of open metadata archives under the repo's top-level {@code content-packs} directory (in dependency
 * order), and shuts everything down once when the test run finishes.
 * <br>
 * It follows the JUnit 5 "singleton resource" pattern: the first test class that is extended with this
 * class pays the (one-off, potentially slow - archive loading against a real PostgreSQL repository can
 * take a while) startup cost in its {@code @BeforeAll}; every other extended class reuses the same
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
     * Name of the metadata access store server that is configured and started for the templates-fvt suite.
     */
    public static final String SERVER_NAME = "templatesFvtMetadataStore";

    /**
     * UserId used for all admin, platform and metadata calls made by the templates-fvt suite.
     */
    public static final String USER_ID = "templatesfvtuser";

    /**
     * Fixed local metadata collection id for the server's repository.  This is deliberately stable
     * (rather than left to be auto-generated afresh on every run) because the underlying PostgreSQL
     * schema persists across test runs - keeping the collection id constant means metadata created by
     * an earlier run is still recognised as belonging to "this" repository on a later run.
     */
    private static final String METADATA_COLLECTION_ID = "3f4e5d6c-7b8a-4901-a2b3-74656d706c74";

    /**
     * The open metadata archives to load at server startup, in dependency order (each archive's
     * "dependsOnArchives" list is descriptive metadata only - nothing in the platform resolves it
     * automatically, so this list has to already be in a valid load order; see the templates-fvt README for
     * how this order was derived). Paths are relative to this module's project directory and point at
     * the repo's shared top-level content-packs directory, so this suite always exercises the same
     * archive content as the rest of the project rather than a private copy.
     * <br>
     * The two purely self-contained "combo" archives (CocoComboArchive, SimpleCatalog) are deliberately
     * left out: their content is a strict superset merge of the individual archives already listed here,
     * so loading them too would only add load time, not additional coverage.
     * <br>
     * The directory is located at runtime (see {@link #findContentPacksDirectory()}) rather than assumed
     * to be a fixed number of "../" above the current working directory - Gradle's test worker process
     * does not always use this module's project directory as its working directory (for example, when an
     * existing worker is reused across tasks), so a hardcoded relative depth is not reliable here.
     */
    private static final List<String> ARCHIVE_FILES = List.of(
            "OpenMetadataTypes.omarchive",
            "CoreContentPack.omarchive",
            "EgeriaContentPack.omarchive",
            "FilesContentPack.omarchive",
            "OpenLineageContentPack.omarchive",
            "ApacheAtlasContentPack.omarchive",
            "ApacheKafkaContentPack.omarchive",
            "PostgresContentPack.omarchive",
            "OpenMetadataDigitalProductsContentPack.omarchive",
            "OrganizationInsightContentPack.omarchive",
            "MSSQLContentPack.omarchive",
            "OracleContentPack.omarchive",
            "DB2LUWContentPack.omarchive",
            "DuckDBContentPack.omarchive",
            "APIsContentPack.omarchive",
            "UnityCatalogContentPack.omarchive",
            "CocoTypesArchive.omarchive",
            "CocoOrganizationArchive.omarchive",
            "CocoGovernanceProgramArchive.omarchive",
            "CocoClinicalTrialsTemplatesArchive.omarchive",
            "CocoSustainabilityArchive.omarchive",
            "CocoGovernanceEngineDefinitionsArchive.omarchive",
            "CocoBusinessSystemsArchive.omarchive",
            "SimpleEventCatalog.omarchive",
            "SimpleAPICatalog.omarchive",
            "SimpleDataCatalog.omarchive",
            "SimpleGovernanceCatalog.omarchive");

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
                    TemplatesFvtTestSupport.cleanUpLeftoverTestElements();
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
     * @throws Exception any problem configuring or starting the server is fatal to the whole templates-fvt run
     */
    private void configureAndStartServer() throws Exception
    {
        PlatformServicesClient platformServicesClient = new PlatformServicesClient("templates-fvt Platform",
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
        configurationClient.setBasicServerProperties("Egeria templates-fvt",
                                                       "Server used by the templates-fvt functional verification test suite",
                                                       USER_ID,
                                                       "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider",
                                                       "build/templates-fvt-data/secrets.omsecrets",
                                                       "templates-fvt",
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

        // Neither setLocalMetadataCollectionId(String), addStartUpOpenMetadataArchiveFile(String), nor
        // addStartUpOpenMetadataArchiveList(List<Connection>) is used here directly - all three end up broken by
        // the same underlying cause. admin-services-client always issues its REST calls through the JDK REST
        // client connector (RESTClientFactory hardcodes JDKRESTClientConnectorProvider), whose plain
        // `new ObjectMapper()` serializes whatever request body object it is given via
        // `writeValueAsString(Object requestBody)`. Because that call site only ever sees the request body as a
        // type-erased `Object`:
        //  - a bare String body (the metadata collection id, or an archive file name) gets JSON-quoted (correct
        //    per JSON), but the server's matching `@RequestBody String` endpoint is bound by Spring's
        //    StringHttpMessageConverter (opaque text) ahead of Jackson, so the quotes are never stripped back
        //    out - they end up baked into the persisted value. For the metadata collection id this was only
        //    caught after the fact: the corrupted, quote-wrapped id was silently accepted and became every
        //    archive-loaded entity's `replicatedBy` value, and countMetadataElements(...) - whose SQL matches
        //    entities against the connector's own (unquoted) in-memory collection id - then saw none of them,
        //    even though the entities were genuinely all there;
        //  - a bare List<Connection> body loses Jackson's polymorphic "class" type-id markers on every element
        //    (confirmed empirically: Jackson only emits @JsonTypeInfo markers for collection elements when it
        //    can see the collection's static generic element type via reflection - e.g. a bean property/field
        //    declared as List<Connection> - and a type-erased root Object value gives it no such static type to
        //    reflect on), so the server fails to deserialize it ("missing type id property 'class'").
        // Fetching the config document, setting the metadata collection id and archive connections list as
        // fields on its nested RepositoryServicesConfig bean, and pushing the whole document back with
        // setOMAGServerConfig(...) avoids both problems: those values are then nested inside a properly-typed
        // bean graph, which Jackson serializes correctly via normal reflection-based bean property introspection.
        ConnectorConfigurationFactory connectorConfigurationFactory = new ConnectorConfigurationFactory();
        List<Connection>              archiveConnections            = new ArrayList<>();

        for (String archiveFileName : ARCHIVE_FILES)
        {
            String archiveFilePath = new File(findContentPacksDirectory(), archiveFileName).getAbsolutePath();

            archiveConnections.add(connectorConfigurationFactory.getOpenMetadataArchiveFileConnection(archiveFilePath));
        }

        OMAGServerConfig serverConfig = configurationClient.getOMAGServerConfig();

        RepositoryServicesConfig repositoryServicesConfig = serverConfig.getRepositoryServicesConfig();
        LocalRepositoryConfig    localRepositoryConfig     = repositoryServicesConfig.getLocalRepositoryConfig();

        localRepositoryConfig.setMetadataCollectionId(METADATA_COLLECTION_ID);
        repositoryServicesConfig.setLocalRepositoryConfig(localRepositoryConfig);
        repositoryServicesConfig.setOpenMetadataArchiveConnections(archiveConnections);
        serverConfig.setRepositoryServicesConfig(repositoryServicesConfig);

        configurationClient.setOMAGServerConfig(serverConfig);

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
                new PlatformServicesClient("templates-fvt Platform", platformURLRoot, null, null, null, USER_ID, null)
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
