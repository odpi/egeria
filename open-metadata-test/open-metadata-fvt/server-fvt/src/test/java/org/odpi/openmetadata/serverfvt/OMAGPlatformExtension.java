/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serverfvt;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.odpi.openmetadata.adminservices.client.EngineHostConfigurationClient;
import org.odpi.openmetadata.adminservices.client.IntegrationDaemonConfigurationClient;
import org.odpi.openmetadata.adminservices.client.MetadataAccessStoreConfigurationClient;
import org.odpi.openmetadata.adminservices.client.OMAGServerConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationGroupConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerClientConfig;
import org.odpi.openmetadata.platformchassis.springboot.OMAGServerPlatform;
import org.odpi.openmetadata.platformservices.client.PlatformServicesClient;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OMAGPlatformExtension stands up the platform, and the servers on it, that the server-fvt suite administers
 * and operates - one platform in one JVM, started once for the whole run and shut down when it finishes.
 * <br><br>
 * <b>Nothing outside this JVM is required.</b>  The repository is in-memory, the event bus is the in-memory
 * topic connector, no content packs are loaded, and no PostgreSQL server or Kafka broker is involved.  That
 * is a deliberate choice rather than a simplification: the subject of this suite is how servers are
 * configured, started, inspected and stopped, not what is stored in them, so none of that machinery would
 * be doing anything except making the suite too expensive to run on every change.
 * <br><br>
 * <b>The platform runs with its real authentication chain switched on</b>, and this is the one decision
 * here worth arguing with.  Every sibling suite except auth-fvt excludes {@code user-authn} and installs a
 * permit-all filter chain, which is simpler and cannot fail for reasons unrelated to what is being tested.
 * This suite does not, for two reasons:
 * <ul>
 *     <li>{@code user-security} is one of the services under test, and most of it - {@code /api/about},
 *     {@code /api/public/app/info}, the token endpoints - does not exist at all on a platform that excludes
 *     the module.  A suite that excluded it would report those clients as broken when they are merely
 *     absent.</li>
 *     <li>Every client under test can obtain a bearer token for itself from a secrets store, and that path
 *     - {@code SpringRESTClientConnector.refreshAuthorizationToken} calling the YAML secrets store, which
 *     POSTs to {@code /api/token} - is precisely the sort of thing this suite exists to cover: it is used
 *     in every real deployment and by no other test.  Against a permit-all platform it would be exercised
 *     but never actually tested, because a request that carried no token at all would succeed too.</li>
 * </ul>
 * So the port is allocated <em>before</em> the platform starts, the user directory is written with a token
 * API pointing at that port, and {@link ServerFvtTestSupport} builds every client against it.  A failure to
 * obtain a token therefore fails the whole run at start-up with a message that says so, rather than
 * appearing as an unexplained 401 in each test.
 * <br><br>
 * Three servers are configured and started here, and they are the ones the tests <em>read</em>.  Tests that
 * create servers of their own - most of the admin-services coverage does - use their own names and clean up
 * after themselves; see {@link ServerFvtTestSupport#deleteServer}.
 * <ul>
 *     <li><b>{@value #METADATA_STORE_NAME}</b> - a metadata access store with an in-memory repository and
 *     its access services configured without out topics.  This is the running server that server-operations
 *     and repository-services are tested against.</li>
 *     <li><b>{@value #ENGINE_HOST_NAME}</b> - an engine host.</li>
 *     <li><b>{@value #INTEGRATION_DAEMON_NAME}</b> - an integration daemon.</li>
 * </ul>
 * Neither governance server is given any content to run: no content pack is loaded, so the engine host's
 * governance engine and the integration daemon's integration group never find their definitions and never
 * reach {@code RUNNING}.  That is intentional and is <em>not</em> a degraded setup for these tests.  What
 * this suite checks of those two servers is their control surface - can their status be retrieved, is an
 * unknown engine reported as unknown rather than as a server error, is a refresh-configuration request
 * accepted - and all of that is answerable by a started server with nothing to do.  Whether an engine
 * actually runs a governance service is subscription-fvt's subject, and it needs the content packs, Kafka
 * and PostgreSQL that this suite is built to do without.
 */
public class OMAGPlatformExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource
{
    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(OMAGPlatformExtension.class);
    private static final String                     STORE_KEY = OMAGPlatformExtension.class.getName();

    /**
     * The user this suite configures and drives every server as.  It is a real account in the user directory
     * written by {@link #writeUserDirectory}, because this platform really does authenticate.
     */
    public static final String USER_ID = "serverfvtuser";

    /**
     * The password for {@link #USER_ID}.  A test suite's user directory is not a secret.
     */
    public static final String PASSWORD = "serverfvtsecret";

    /**
     * A second account, used by the tests that need to show that a request is attributed to the caller
     * rather than to whoever configured the platform.
     */
    public static final String OTHER_USER_ID = "serverfvtotheruser";

    /**
     * The password for {@link #OTHER_USER_ID}.
     */
    public static final String OTHER_PASSWORD = "serverfvtothersecret";

    /**
     * The metadata access store: in-memory repository, access services without out topics.  This is the
     * running server that server-operations and repository-services are tested against.
     */
    public static final String METADATA_STORE_NAME = "serverFvtMetadataStore";

    /**
     * The engine host.  Started, but with no engine definitions to find - see the class comment.
     */
    public static final String ENGINE_HOST_NAME = "serverFvtEngineHost";

    /**
     * The integration daemon.  Started, but with no group definition to find - see the class comment.
     */
    public static final String INTEGRATION_DAEMON_NAME = "serverFvtIntegrationDaemon";

    /**
     * The governance engine the engine host is configured with.  Nothing defines it - no content pack is
     * loaded - so it is a name the engine host will look for and not find, which is what the engine host
     * tests are about.
     */
    public static final String GOVERNANCE_ENGINE_NAME = "serverFvtGovernanceEngine";

    /**
     * The integration group the integration daemon is configured with.  Undefined, for the same reason.
     */
    public static final String INTEGRATION_GROUP_NAME = "serverFvtIntegrationGroup";

    /**
     * Name of the secrets collection holding the user accounts the platform authenticates against.
     */
    static final String USER_DIRECTORY_COLLECTION = "serverFvtUserDirectory";

    /**
     * Name of a second user directory collection that holds {@link #OTHER_USER_ID} and <em>not</em>
     * {@link #USER_ID}.
     * <br><br>
     * It exists so that a server security connection can be pointed at a directory this suite's own
     * administrator is not in, which is how {@code AdminServicesConfigurationFVT} shows that a server
     * security connection is enforced rather than merely stored.
     */
    static final String OTHER_USER_DIRECTORY_COLLECTION = "serverFvtOtherUserDirectory";

    /**
     * Name of the secrets collection the clients obtain their bearer token from.  It holds a token API
     * rather than a password, so that building a client exercises the token exchange.
     */
    static final String CLIENT_TOKEN_COLLECTION = "serverFvtClientToken";

    /**
     * Name of the secrets collection that authenticates as {@link #OTHER_USER_ID}.
     */
    static final String OTHER_CLIENT_TOKEN_COLLECTION = "serverFvtOtherClientToken";

    /**
     * Where this suite's working files go.  Under the build directory so that a clean build discards them:
     * the configuration documents the tests write, and the user directory, are both rewritten on every run.
     */
    static final String DATA_DIRECTORY = "build/server-fvt-data";

    private static final String USER_DIRECTORY_FILE = DATA_DIRECTORY + "/server-fvt-user-directory.omsecrets";

    private static volatile boolean               started         = false;
    private static          Exception             startupFailure  = null;
    private static ConfigurableApplicationContext platformContext;
    private static String                         platformURLRoot;
    private static Path                           userDirectoryPath;


    /**
     * Return the base URL of the running platform, including its allocated port.
     *
     * @return url root
     */
    public static String getPlatformURLRoot()
    {
        return platformURLRoot;
    }


    /**
     * Return the path of the user directory this platform authenticates against, so that a test can check
     * what the platform persisted rather than only what it accepted over HTTP.
     *
     * @return path to the user directory YAML
     */
    public static Path getUserDirectoryPath()
    {
        return userDirectoryPath;
    }


    /**
     * Build the environment once for the whole run.
     *
     * @param context junit context
     * @throws Exception problem building the environment
     */
    @Override
    public void beforeAll(ExtensionContext context) throws Exception
    {
        synchronized (OMAGPlatformExtension.class)
        {
            /*
             * A start-up that has already failed is reported again rather than retried.  Retrying would
             * start a second platform on a port the first one is still holding, and every test class after
             * the first would then fail with a port-in-use error - burying the message that says what
             * actually went wrong.
             */
            if (startupFailure != null)
            {
                throw new IllegalStateException("The server-fvt environment failed to start: " + startupFailure.getMessage(),
                                                startupFailure);
            }

            if (started)
            {
                return;
            }

            /*
             * Registered before the environment is built rather than after, so that the platform is shut
             * down at the end of the run even when the build below fails part-way.
             */
            context.getRoot().getStore(NAMESPACE).put(STORE_KEY, this);

            try
            {
                /*
                 * The port is allocated before anything else because the user directory has to name it: the
                 * clients' token API posts to this platform's own /api/token, and that URL cannot be written
                 * until the port is known.
                 */
                int port = allocateFreePort();

                platformURLRoot   = "http://localhost:" + port;
                userDirectoryPath = writeUserDirectory(platformURLRoot);

                startPlatform(port);
                confirmPlatformIsAnswering();

                PlatformServicesClient platformServicesClient = ServerFvtTestSupport.getPlatformServicesClient();

                configureMetadataStore();
                configureEngineHost();
                configureIntegrationDaemon();

                startServer(platformServicesClient, METADATA_STORE_NAME);
                startServer(platformServicesClient, ENGINE_HOST_NAME);
                startServer(platformServicesClient, INTEGRATION_DAEMON_NAME);

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
     * Find a free port for the platform to listen on.
     * <br><br>
     * A fixed port would mean a second checkout of Egeria running this same suite fails with a
     * PortInUseException, and that failure looks like a broken test rather than a clash.
     *
     * @return a port that was free a moment ago
     */
    private int allocateFreePort()
    {
        try (ServerSocket socket = new ServerSocket(0))
        {
            return socket.getLocalPort();
        }
        catch (IOException error)
        {
            throw new IllegalStateException("Could not allocate a port for the server-fvt platform", error);
        }
    }


    /**
     * Write the user directory that the platform authenticates against and the clients obtain their tokens
     * from.
     * <br><br>
     * It is generated rather than shipped as a test resource, because the token API in it has to name the
     * port allocated above.  Generating it fresh also means a run always starts from clear-text passwords:
     * the platform rewrites this file in place when a password is changed, so a file carried over from an
     * earlier run would start in a different state.
     *
     * @param urlRoot the platform's own URL, which the token API posts to
     * @return path of the file written
     * @throws IOException the file could not be written
     */
    private Path writeUserDirectory(String urlRoot) throws IOException
    {
        Path target = Paths.get(USER_DIRECTORY_FILE).toAbsolutePath();

        Files.createDirectories(target.getParent());

        String content =
                "# SPDX-License-Identifier: Apache-2.0\n" +
                "# Copyright Contributors to the Egeria project.\n" +
                "#\n" +
                "# Generated by server-fvt's OMAGPlatformExtension - do not edit.  The token API below names the\n" +
                "# port this run's platform was allocated, so this file is only valid for the run that wrote it.\n" +
                "#\n" +
                "secretsCollections:\n" +
                "  " + USER_DIRECTORY_COLLECTION + ":\n" +
                "    refreshTimeInterval: 0\n" +
                "    users:\n" +
                "      " + USER_ID + ":\n" +
                "        userAccountStatus: AVAILABLE\n" +
                "        userAccountType: EMPLOYEE\n" +
                "        userName: Server FVT Administrator\n" +
                "        secrets:\n" +
                "          clearPassword: " + PASSWORD + "\n" +
                "      " + OTHER_USER_ID + ":\n" +
                "        userAccountStatus: AVAILABLE\n" +
                "        userAccountType: EMPLOYEE\n" +
                "        userName: Server FVT Second User\n" +
                "        secrets:\n" +
                "          clearPassword: " + OTHER_PASSWORD + "\n" +
                "  " + OTHER_USER_DIRECTORY_COLLECTION + ":\n" +
                "    refreshTimeInterval: 0\n" +
                "    users:\n" +
                "      " + OTHER_USER_ID + ":\n" +
                "        userAccountStatus: AVAILABLE\n" +
                "        userAccountType: EMPLOYEE\n" +
                "        userName: Server FVT Second User\n" +
                "        secrets:\n" +
                "          clearPassword: " + OTHER_PASSWORD + "\n" +
                "  " + CLIENT_TOKEN_COLLECTION + ":\n" +
                "    refreshTimeInterval: 0\n" +
                "    tokenAPI:\n" +
                "      httpRequestType: POST\n" +
                "      url: " + urlRoot + "/api/token\n" +
                "      contentType: application/json\n" +
                "      requestBody:\n" +
                "        userId: " + USER_ID + "\n" +
                "        password: " + PASSWORD + "\n" +
                "  " + OTHER_CLIENT_TOKEN_COLLECTION + ":\n" +
                "    refreshTimeInterval: 0\n" +
                "    tokenAPI:\n" +
                "      httpRequestType: POST\n" +
                "      url: " + urlRoot + "/api/token\n" +
                "      contentType: application/json\n" +
                "      requestBody:\n" +
                "        userId: " + OTHER_USER_ID + "\n" +
                "        password: " + OTHER_PASSWORD + "\n";

        Files.writeString(target, content);

        return target;
    }


    /**
     * Start the platform in-process, with authentication switched on.
     * <br><br>
     * The properties are set here rather than in an {@code application.properties} because two of them - the
     * port and the user directory location - are only known at runtime.
     *
     * @param port the port allocated above
     */
    private void startPlatform(int port)
    {
        Map<String, Object> properties = new HashMap<>();

        properties.put("server.port", Integer.toString(port));
        properties.put("platform.name", "server-fvt OMAG Server Platform");
        properties.put("platform.description", "Hermetic, in-process platform started by the server-fvt functional verification test suite.");
        properties.put("platform.organization.name", "Egeria server-fvt");
        properties.put("platform.configstore.provider", "org.odpi.openmetadata.adapters.adminservices.configurationstore.file.FileBasedServerConfigStoreProvider");
        properties.put("platform.configstore.endpoint", DATA_DIRECTORY + "/servers/{0}/config/{0}.config");

        /*
         * Nothing is auto-started: every server this suite uses is configured and started explicitly, so
         * that a test can tell "the server did not start" from "the server was never asked to".
         */
        properties.put("startup.server.list", "");
        properties.put("startup.user", USER_ID);
        properties.put("cors.allowed-origins", "*");

        /*
         * The properties that switch the real authentication chain on.  PlatformUserDetailsService and
         * PlatformSecurityConfig are both conditional on authentication.source being "platform"; without
         * them SecurityConfig has no AuthenticationProvider and the platform does not start at all.
         */
        properties.put("authentication.source", "platform");
        properties.put("platform.security.provider", "org.odpi.openmetadata.metadatasecurity.accessconnector.OpenMetadataAccessSecurityProvider");
        properties.put("platform.security.name", "server-fvt Platform");
        properties.put("platform.security.secrets.provider", "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider");
        properties.put("platform.security.secrets.location", userDirectoryPath.toString());
        properties.put("platform.security.secrets.collection", USER_DIRECTORY_COLLECTION);

        properties.put("authn.header.name.list", "");
        properties.put("app.description", "server-fvt - server administration and operation functional verification tests");
        properties.put("app.title", "Egeria server-fvt");
        properties.put("scan.packages", "org.odpi.openmetadata.*");

        /*
         * The API docs are left on.  Their endpoints are in SecurityConfig's permitAll list, so checking
         * that they stay reachable is part of the user-security coverage rather than a documentation check.
         */
        properties.put("springdoc.api-docs.enabled", "true");
        properties.put("springdoc.api-docs.path", "/v3/api-docs");

        properties.put("management.health.cassandra.enabled", "false");
        properties.put("management.health.redis.enabled", "false");
        properties.put("management.health.ldap.enabled", "false");

        properties.put("logging.level.root", "WARN");
        properties.put("logging.level.org.springframework", "ERROR");
        properties.put("logging.level.org.odpi.openmetadata", "WARN");
        properties.put("logging.level.org.odpi.openmetadata.platformchassis.springboot", "INFO");

        /*
         * Rejected calls are the expected outcome of a large part of this suite - most of the validation
         * coverage is a call that is supposed to fail - and each one logs an error server-side.  Keep that
         * expected noise out of the build output; a test that wants to see the message reads it from the
         * exception it caught, not from the console.
         */
        properties.put("logging.level.org.odpi.openmetadata.userauthn", "OFF");
        properties.put("logging.level.org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler", "OFF");

        SpringApplicationBuilder builder = new SpringApplicationBuilder(OMAGServerPlatform.class);

        builder.web(WebApplicationType.SERVLET);
        builder.properties(properties);

        platformContext = builder.run();
    }


    /**
     * Check that the platform this suite just started is the one answering on the port it was given, before
     * any test runs.
     * <br><br>
     * The origin endpoint is used because {@code SecurityConfig} lists it as {@code permitAll}: it answers
     * whether or not a token was obtained, so a failure here means the platform is not there rather than
     * that authentication is misconfigured.  Authentication is checked separately, and immediately
     * afterwards, by the first client this suite builds.
     *
     * @throws Exception the platform is not answering as itself
     */
    private void confirmPlatformIsAnswering() throws Exception
    {
        java.net.http.HttpRequest request =
                java.net.http.HttpRequest.newBuilder()
                                         .uri(java.net.URI.create(platformURLRoot + "/open-metadata/platform-services/server-platform/origin"))
                                         .GET()
                                         .build();

        java.net.http.HttpResponse<String> response =
                java.net.http.HttpClient.newHttpClient()
                                        .send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200)
        {
            throw new IllegalStateException(
                    "The platform started on " + platformURLRoot + " but its origin endpoint answered " +
                            response.statusCode() + " rather than 200, so something other than this suite's " +
                            "platform is responding on that port.  Body: " + response.body());
        }
    }


    /**
     * Configure the metadata access store this suite operates on: an in-memory repository and the access
     * services without out topics.
     * <br><br>
     * The access services are configured because server-operations and platform-services are partly about
     * <em>which services a running server is running</em>, and a server with none would make those
     * assertions vacuous.  They are configured without out topics because nothing subscribes to them here.
     *
     * @throws Exception any problem configuring the server is fatal to the whole run
     */
    private void configureMetadataStore() throws Exception
    {
        MetadataAccessStoreConfigurationClient configurationClient =
                ServerFvtTestSupport.getMetadataAccessStoreConfigurationClient(METADATA_STORE_NAME);

        configurationClient.clearOMAGServerConfig();

        setBasicProperties(configurationClient, "Metadata access store that the server-fvt suite operates on.");

        configurationClient.setInMemLocalRepository();
        configurationClient.setLocalMetadataCollectionName(METADATA_STORE_NAME + " repository");

        addAuditLogDestinations(configurationClient);

        configurationClient.configureAllAccessServicesNoTopics(new HashMap<>());
    }


    /**
     * Configure the engine host.  See the class comment for why its engine is deliberately one that nothing
     * defines.
     *
     * @throws Exception any problem configuring the server is fatal to the whole run
     */
    private void configureEngineHost() throws Exception
    {
        EngineHostConfigurationClient configurationClient =
                ServerFvtTestSupport.getEngineHostConfigurationClient(ENGINE_HOST_NAME);

        configurationClient.clearOMAGServerConfig();

        setBasicProperties(configurationClient, "Engine host whose control surface the server-fvt suite tests.");

        addAuditLogDestinations(configurationClient);

        EngineConfig engineConfig = new EngineConfig();

        engineConfig.setEngineQualifiedName(GOVERNANCE_ENGINE_NAME);
        engineConfig.setEngineUserId(USER_ID);
        engineConfig.setOMAGServerName(METADATA_STORE_NAME);
        engineConfig.setOMAGServerPlatformRootURL(platformURLRoot);

        /*
         * The engine's own secrets store, which is a different setting from the server's: an engine host
         * builds the client it reads its engine definitions with from the EngineConfig, not from the
         * server properties.  Leaving it unset on a platform that authenticates gives an engine host that
         * starts cleanly and then logs a 401 on every configuration refresh.
         */
        setClientSecrets(engineConfig);

        List<EngineConfig> engineConfigs = new ArrayList<>();

        engineConfigs.add(engineConfig);

        configurationClient.setEngineHostServicesConfig(engineConfigs);
    }


    /**
     * Configure the integration daemon.  Its integration group is undefined for the same reason the engine
     * host's engine is - see the class comment.
     *
     * @throws Exception any problem configuring the server is fatal to the whole run
     */
    private void configureIntegrationDaemon() throws Exception
    {
        IntegrationDaemonConfigurationClient configurationClient =
                ServerFvtTestSupport.getIntegrationDaemonConfigurationClient(INTEGRATION_DAEMON_NAME);

        configurationClient.clearOMAGServerConfig();

        setBasicProperties(configurationClient, "Integration daemon whose control surface the server-fvt suite tests.");

        addAuditLogDestinations(configurationClient);

        IntegrationGroupConfig integrationGroupConfig = new IntegrationGroupConfig();

        integrationGroupConfig.setIntegrationGroupQualifiedName(INTEGRATION_GROUP_NAME);
        integrationGroupConfig.setOMAGServerName(METADATA_STORE_NAME);
        integrationGroupConfig.setOMAGServerPlatformRootURL(platformURLRoot);

        /*
         * As with the engine host, the group carries its own secrets store - see configureEngineHost.
         */
        setClientSecrets(integrationGroupConfig);

        configurationClient.configureIntegrationGroup(integrationGroupConfig);
    }


    /**
     * Point one of the governance servers' partner-server clients at this suite's user directory, so that
     * they authenticate when they call the metadata access store.
     *
     * @param clientConfig engine or integration group configuration to complete
     */
    private void setClientSecrets(OMAGServerClientConfig clientConfig)
    {
        clientConfig.setSecretsStoreProvider(ServerFvtTestSupport.SECRETS_STORE_PROVIDER);
        clientConfig.setSecretsStoreLocation(userDirectoryPath.toString());
        clientConfig.setSecretsStoreCollection(CLIENT_TOKEN_COLLECTION);
    }


    /**
     * Apply the properties every server this suite starts needs.
     *
     * @param configurationClient client for the server being configured
     * @param description what the server is for
     * @throws Exception problem talking to the admin services
     */
    private void setBasicProperties(OMAGServerConfigurationClient configurationClient,
                                    String                        description) throws Exception
    {
        configurationClient.setServerUserId(USER_ID);
        configurationClient.setBasicServerProperties("Egeria server-fvt",
                                                     description,
                                                     USER_ID,
                                                     "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider",
                                                     userDirectoryPath.toString(),
                                                     CLIENT_TOKEN_COLLECTION,
                                                     platformURLRoot,
                                                     ServerFvtTestSupport.MAX_PAGE_SIZE);
    }


    /**
     * Send each server's audit log to the console and, through the SLF4J destination, to the build's log
     * file.  The audit log is also a subject here rather than only a diagnostic: the repository-services
     * coverage reads it back through the audit log client.
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

        if (! platformServicesClient.getActiveServers().contains(serverName))
        {
            throw new IllegalStateException("Server " + serverName + " was activated on platform " + platformURLRoot +
                                                    " but does not appear in its list of active servers");
        }

        System.out.println("server-fvt: started " + serverName);
    }


    /**
     * Shut the platform down at the end of the whole run.
     */
    @Override
    public void close()
    {
        if (platformContext != null)
        {
            platformContext.close();
            platformContext = null;
            started = false;
        }
    }
}
