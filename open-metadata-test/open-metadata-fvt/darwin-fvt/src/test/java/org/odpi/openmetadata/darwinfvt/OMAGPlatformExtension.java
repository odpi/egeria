/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.darwinfvt;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.odpi.openmetadata.adminservices.client.IntegrationDaemonConfigurationClient;
import org.odpi.openmetadata.adminservices.client.MetadataAccessStoreConfigurationClient;
import org.odpi.openmetadata.adminservices.client.OMAGServerConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationGroupConfig;
import org.odpi.openmetadata.contentpacks.core.IntegrationConnectorDefinition;
import org.odpi.openmetadata.contentpacks.core.IntegrationGroupDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementOriginCategory;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
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
 * OMAGPlatformExtension starts a single OMAG Server Platform in-process for the whole darwin-fvt run and
 * stands up the deployment that Darwin runs in:
 * <ul>
 *     <li><b>{@value #METADATA_STORE_NAME}</b> - a metadata access store with a PostgreSQL local repository
 *     and its access services publishing to a real Apache Kafka broker.  Kafka is not optional here: the
 *     integration daemon learns about its configuration from the out topics.  It loads the open metadata
 *     types, the core content pack (which is where Darwin's own definitions live) and this suite's fixture
 *     archive.</li>
 *     <li><b>{@value #INTEGRATION_DAEMON_NAME}</b> - an integration daemon running the core content pack's
 *     Darwin integration group, which is where the Darwin Product Dependency Manager runs.</li>
 * </ul>
 * It follows the JUnit 5 "singleton resource" pattern: the first test class that is extended with this class
 * pays the one-off startup cost in its {@code @BeforeAll}; every other extended class reuses the same running
 * platform and servers.
 * <br>
 * Everything the suite reaches out to - the PostgreSQL server, the Kafka broker, the port it listens on - is
 * configured from {@code src/test/resources/application.properties}.
 */
public class OMAGPlatformExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource
{
    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(OMAGPlatformExtension.class);
    private static final String                     STORE_KEY = OMAGPlatformExtension.class.getName();

    /**
     * Name of the metadata access store server that holds the fixture.
     */
    public static final String METADATA_STORE_NAME = "darwinFvtMetadataStore";

    /**
     * Name of the integration daemon that runs the Darwin Product Dependency Manager.
     */
    public static final String INTEGRATION_DAEMON_NAME = "darwinFvtIntegrationDaemon";

    /**
     * UserId used for all admin, platform and metadata calls made by the darwin-fvt suite.
     */
    public static final String USER_ID = "darwinfvtuser";

    /**
     * The integration group that Darwin belongs to, and the connector's name within it.  Both come from the
     * content pack definitions rather than being spelled out here, so that renaming either in the content
     * pack cannot leave this suite quietly looking for something that no longer exists.
     */
    public static final String INTEGRATION_GROUP_QUALIFIED_NAME = IntegrationGroupDefinition.DARWIN.getQualifiedName();

    /**
     * The name Darwin is registered under in that group - this is what the tests pass to
     * {@code refreshConnector}.
     */
    public static final String DARWIN_CONNECTOR_NAME = IntegrationConnectorDefinition.DARWIN_PRODUCT_DEPENDENCY_MANAGER.getConnectorName();

    /**
     * Fixed local metadata collection id for the store's repository.  This is deliberately stable (rather
     * than auto-generated afresh on every run) because the underlying PostgreSQL schema persists across runs
     * - keeping the collection id constant means metadata created by an earlier run is still recognised as
     * belonging to "this" repository on a later run.
     */
    private static final String METADATA_COLLECTION_ID = "5a7b9c2d-3e4f-4a1b-8c6d-7e8f9a0b1c2d";

    /**
     * The open metadata archives loaded at start-up, in dependency order.  The core content pack is the one
     * that carries Darwin's connector definition and integration group; the fixture archive is generated by
     * this suite (see {@link DarwinArchiveWriter}) and loaded once the server is up.
     */
    private static final List<String> CONTENT_PACK_FILES = List.of("OpenMetadataTypes.omarchive",
                                                                    "CoreContentPack.omarchive");

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
     * Return a client for the integration daemon, used to drive Darwin's refresh from the tests rather than
     * waiting out its configured refresh interval.
     *
     * @return integration daemon client
     * @throws Exception problem creating the client
     */
    public static IntegrationDaemon getIntegrationDaemonClient() throws Exception
    {
        return new IntegrationDaemon(INTEGRATION_DAEMON_NAME, platformURLRoot, null, null, null, USER_ID, null);
    }


    /**
     * Ask the integration daemon to refresh Darwin now, and wait for the call to return.  The refresh is
     * synchronous, so when this returns the connector has worked through all three levels and the exceptions.
     *
     * @throws Exception the refresh failed
     */
    public static void refreshDarwin() throws Exception
    {
        getIntegrationDaemonClient().refreshConnector(DARWIN_CONNECTOR_NAME);
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
        if (platformContext != null)
        {
            String value = platformContext.getEnvironment().getProperty(propertyName);

            if ((value != null) && (! value.isBlank()))
            {
                return Long.parseLong(value.trim());
            }
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
     * Configure and start the metadata access store that holds the fixture.
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
        configurationClient.setBasicServerProperties("Egeria darwin-fvt",
                                                      "Metadata access store holding the darwin-fvt fixture.",
                                                      USER_ID,
                                                      "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider",
                                                      "build/darwin-fvt-data/secrets.omsecrets",
                                                      "darwin-fvt",
                                                      platformURLRoot,
                                                      DarwinFvtTestSupport.MAX_PAGE_SIZE);

        Map<String, Object> storageProperties = new HashMap<>();

        storageProperties.put("databaseURL", "~{repositoryDatabaseURL}~?currentSchema=repository_" + METADATA_STORE_NAME);
        storageProperties.put("databaseSchema", "repository_" + METADATA_STORE_NAME);
        storageProperties.put("secretsStore", "~{egeriaServersSecretsStore}~");
        storageProperties.put("secretsCollectionName", "~{repositorySecretCollectionName}~");

        configurationClient.setPostgreSQLLocalRepository(storageProperties);
        configurationClient.addConsoleAuditLogDestination(new ArrayList<>());

        setEventBus(configurationClient);

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

        /*
         * The fixture is laid down after the server is up rather than as a startup archive, because anything
         * a previous run left behind has to go first.  The repository behind this suite persists between
         * runs, and the previous run's Darwin will have created data flows and dependencies between the
         * fixture's elements and filled in supply chains on its relationships - none of which an archive
         * reload undoes, and some of which the tests remove.  Assertions about a pristine fixture would then
         * be reporting the state of every run that came before.
         */
        this.purgeLeftoverFixture();

        platformServicesClient.addOpenMetadataArchiveFile(METADATA_STORE_NAME, buildFixtureArchive().getAbsolutePath());

        System.out.println("darwin-fvt: fixture archive loaded into " + METADATA_STORE_NAME);
    }


    /**
     * Remove everything this suite's fixture has left in the repository, so that the archive loaded next
     * recreates it exactly as the tests expect.
     * <br>
     * Purging an element takes its relationships with it - the archive's own and the ones Darwin created
     * between the fixture's elements - so they do not need removing separately.  Darwin's exception type is
     * left where it is: it is not part of the fixture, and Darwin reuses it by qualified name.
     *
     * @throws Exception the leftovers could not be removed, which would make the run's results meaningless
     */
    private void purgeLeftoverFixture() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        int purged = 0;

        for (String fixtureGUID : DarwinFvtTestSupport.FIXTURE_ELEMENT_GUIDS)
        {
            OpenMetadataElement leftover = this.getElementIfPresent(openMetadataStore, fixtureGUID);

            if ((leftover != null) && (this.purgeElement(openMetadataStore, leftover)))
            {
                purged++;
            }
        }

        System.out.println("darwin-fvt: purged " + purged + " leftover fixture element(s) from a previous run");
    }


    /**
     * Retrieve an element if it is there, including one only visible to lineage.
     *
     * @param openMetadataStore store to read through
     * @param elementGUID element to look for
     * @return the element, or null if it is not there
     */
    private OpenMetadataElement getElementIfPresent(OpenMetadataStore openMetadataStore,
                                                    String            elementGUID)
    {
        try
        {
            GetOptions getOptions = new GetOptions();

            getOptions.setForLineage(true);

            return openMetadataStore.getMetadataElementByGUID(elementGUID, getOptions);
        }
        catch (Exception notThere)
        {
            /*
             * Reported rather than discarded: an element that cannot be retrieved is indistinguishable from
             * one that is not there, and silently treating the first as the second leaves the fixture in
             * place while claiming it was cleared.
             */
            System.err.println("darwin-fvt: could not retrieve fixture element " + elementGUID + " - "
                                       + notThere.getClass().getSimpleName() + ": " + notThere.getMessage());

            return null;
        }
    }


    /**
     * Permanently remove one element.  A soft delete would leave it retrievable by a status-filtered query
     * and the archive would not lay it down again, so the fixture has to be purged outright.
     *
     * @param openMetadataStore store to delete through
     * @param element element to remove
     * @return true if it went
     */
    private boolean purgeElement(OpenMetadataStore   openMetadataStore,
                                 OpenMetadataElement element)
    {
        DeleteOptions deleteOptions = new DeleteOptions();

        deleteOptions.setDeleteMethod(DeleteMethod.PURGE);
        deleteOptions.setForLineage(true);
        deleteOptions.setCascadedDelete(true);

        /*
         * The fixture is owned by the content pack that supplied it, so it can only be removed on behalf of
         * the repository replicating it - the same rule Darwin follows when it updates one.
         */
        if ((element.getOrigin() != null) && (element.getOrigin().getOriginCategory() != ElementOriginCategory.LOCAL_COHORT))
        {
            deleteOptions.setExternalSourceGUID(element.getOrigin().getHomeMetadataCollectionId());
            deleteOptions.setExternalSourceName(element.getOrigin().getHomeMetadataCollectionName());
        }

        try
        {
            /*
             * PURGE only succeeds on an instance that is already soft-deleted - the OMRS lifecycle the
             * repository connector enforces - so the element is soft-deleted first.  That step is
             * best-effort: it fails harmlessly when a previous run left the element deleted but not purged.
             */
            DeleteOptions softDeleteOptions = new DeleteOptions(deleteOptions);

            softDeleteOptions.setDeleteMethod(DeleteMethod.SOFT_DELETE);

            try
            {
                openMetadataStore.deleteMetadataElementInStore(element.getElementGUID(), softDeleteOptions);
            }
            catch (Exception alreadyDeleted)
            {
                // ordinary - the element may already be soft-deleted
            }

            openMetadataStore.deleteMetadataElementInStore(element.getElementGUID(), deleteOptions);

            return true;
        }
        catch (Exception error)
        {
            System.err.println("darwin-fvt: could not purge leftover element " + element.getElementGUID()
                                       + " - " + error.getClass().getSimpleName() + ": " + error.getMessage());

            return false;
        }
    }


    /**
     * Configure and start the integration daemon that runs Darwin.
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
        configurationClient.setBasicServerProperties("Egeria darwin-fvt",
                                                      "Integration daemon running the Darwin Product Dependency Manager.",
                                                      USER_ID,
                                                      "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider",
                                                      "build/darwin-fvt-data/secrets.omsecrets",
                                                      "darwin-fvt",
                                                      platformURLRoot,
                                                      DarwinFvtTestSupport.MAX_PAGE_SIZE);

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
                                          "egeria.omag.darwin-fvt",
                                          eventBusProperties);
    }


    /**
     * Build this suite's fixture archive into the module's build directory.
     *
     * @return the archive file
     * @throws Exception the archive could not be built
     */
    private File buildFixtureArchive() throws Exception
    {
        File archiveFile = new File("build/darwin-fvt-data/archives/DarwinFvtArchive.omarchive").getAbsoluteFile();

        new DarwinArchiveWriter().writeArchive(archiveFile);

        if (! archiveFile.exists())
        {
            throw new IllegalStateException("The fixture archive was not written to " + archiveFile.getAbsolutePath());
        }

        System.out.println("darwin-fvt: fixture archive written to " + archiveFile.getAbsolutePath());

        return archiveFile;
    }


    /**
     * Wait for the integration daemon to bring the Darwin integration group up.  A connector that failed to
     * start reports its own exception message, which is repeated here: the usual cause is the connector's
     * module missing from this suite's test runtime classpath, which is not a compile failure but leaves
     * every test with nothing to assert against.
     *
     * @throws Exception the integration group did not start
     */
    private void waitForIntegrationGroup() throws Exception
    {
        IntegrationDaemon integrationDaemon = getIntegrationDaemonClient();

        long timeoutMilliseconds = getLongProperty("darwin.fvt.integration.daemon.timeout.seconds", 180) * 1000;
        long pollMilliseconds    = getLongProperty("darwin.fvt.integration.daemon.poll.seconds", 2) * 1000;
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
                        if (connectorReport.getFailingExceptionMessage() != null)
                        {
                            throw new IllegalStateException("Integration connector " + connectorReport.getConnectorName() + " in group "
                                                                    + INTEGRATION_GROUP_QUALIFIED_NAME + " failed to start: "
                                                                    + connectorReport.getFailingExceptionMessage()
                                                                    + ".  If this is a class loading problem, the nanny-connectors module is"
                                                                    + " missing from darwin-fvt's test runtime classpath.");
                        }
                    }

                    System.out.println("darwin-fvt: integration group " + INTEGRATION_GROUP_QUALIFIED_NAME + " running with "
                                               + connectorReports.size() + " connector(s)");
                    return;
                }
            }

            Thread.sleep(pollMilliseconds);
        }

        throw new IllegalStateException("Integration group " + INTEGRATION_GROUP_QUALIFIED_NAME + " on " + INTEGRATION_DAEMON_NAME
                                                + " did not reach RUNNING with connectors within " + (timeoutMilliseconds / 1000)
                                                + " seconds (last known status: " + lastKnownStatus + ").  Check that the core content"
                                                + " pack loaded into " + METADATA_STORE_NAME + ".");
    }


    /**
     * Return a platform services client for this suite's platform.
     *
     * @return client
     * @throws Exception problem creating the client
     */
    private static PlatformServicesClient getPlatformServicesClient() throws Exception
    {
        return new PlatformServicesClient("darwin-fvt Platform", platformURLRoot, null, null, null, USER_ID, null);
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
