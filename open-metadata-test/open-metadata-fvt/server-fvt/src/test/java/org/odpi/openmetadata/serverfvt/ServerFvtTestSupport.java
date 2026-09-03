/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serverfvt;

import org.odpi.openmetadata.adminservices.client.ConfigurationManagementClient;
import org.odpi.openmetadata.adminservices.client.EngineHostConfigurationClient;
import org.odpi.openmetadata.adminservices.client.IntegrationDaemonConfigurationClient;
import org.odpi.openmetadata.adminservices.client.MetadataAccessStoreConfigurationClient;
import org.odpi.openmetadata.adminservices.client.OMAGServerConfigurationClient;
import org.odpi.openmetadata.adminservices.client.ViewServerConfigurationClient;
import org.odpi.openmetadata.governanceservers.enginehostservices.client.EngineHostClient;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.client.IntegrationDaemon;
import org.odpi.openmetadata.platformservices.client.PlatformServicesClient;
import org.odpi.openmetadata.repositoryservices.clients.AuditLogServicesClient;
import org.odpi.openmetadata.repositoryservices.clients.MetadataHighwayServicesClient;
import org.odpi.openmetadata.serveroperations.client.ServerOperationsClient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Shared helpers for the server-fvt suite: one factory method per client under test, and the small amount of
 * raw HTTP the suite needs.
 * <br><br>
 * Every client is built the same way - against this run's platform, authenticating with a bearer token
 * obtained from the generated user directory.  Building them here rather than in each test keeps that
 * arrangement in one place, and means a test reads as what it is asking the service rather than as how it
 * connected to it.
 * <br><br>
 * The secrets store is named on every client rather than the simpler "no authentication" constructor being
 * used, because the token exchange is itself part of what this suite covers - see
 * {@link OMAGPlatformExtension} for why the platform runs with authentication switched on at all.
 */
public final class ServerFvtTestSupport
{
    /**
     * The connector that reads this suite's generated user directory.
     */
    static final String SECRETS_STORE_PROVIDER = "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider";

    /**
     * Maximum page size configured on the servers this suite starts.
     * <br><br>
     * It is deliberately a round, small-ish number rather than the platform default, because it is also an
     * assertion target: {@code setMaxPageSize} is one of the configuration calls under test, and a value
     * that differs from the default is one that can be shown to have been stored.
     */
    public static final int MAX_PAGE_SIZE = 500;

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
                                                            .connectTimeout(Duration.ofSeconds(20))
                                                            .build();

    private ServerFvtTestSupport()
    {
        // no instances
    }


    /**
     * Return the location of the user directory the clients authenticate against.
     *
     * @return absolute path
     */
    private static String secretsStoreLocation()
    {
        return OMAGPlatformExtension.getUserDirectoryPath().toString();
    }


    /**
     * Return a platform services client, authenticating as this suite's user.
     *
     * @return client
     * @throws Exception the client could not be created - which, because creating one obtains a token,
     *                   includes the platform refusing this suite's credentials
     */
    public static PlatformServicesClient getPlatformServicesClient() throws Exception
    {
        return new PlatformServicesClient("server-fvt Platform",
                                          OMAGPlatformExtension.getPlatformURLRoot(),
                                          SECRETS_STORE_PROVIDER,
                                          secretsStoreLocation(),
                                          OMAGPlatformExtension.CLIENT_TOKEN_COLLECTION,
                                          OMAGPlatformExtension.USER_ID,
                                          null);
    }


    /**
     * Return a server operations client, authenticating as this suite's user.
     *
     * @return client
     * @throws Exception the client could not be created
     */
    public static ServerOperationsClient getServerOperationsClient() throws Exception
    {
        return new ServerOperationsClient("server-fvt Platform",
                                          OMAGPlatformExtension.getPlatformURLRoot(),
                                          SECRETS_STORE_PROVIDER,
                                          secretsStoreLocation(),
                                          OMAGPlatformExtension.CLIENT_TOKEN_COLLECTION,
                                          OMAGPlatformExtension.USER_ID,
                                          null);
    }


    /**
     * Return a configuration management client - the one that works across all of the platform's stored
     * configuration documents rather than one server's.
     *
     * @return client
     * @throws Exception the client could not be created
     */
    public static ConfigurationManagementClient getConfigurationManagementClient() throws Exception
    {
        return new ConfigurationManagementClient(OMAGPlatformExtension.getPlatformURLRoot(),
                                                 SECRETS_STORE_PROVIDER,
                                                 secretsStoreLocation(),
                                                 OMAGPlatformExtension.CLIENT_TOKEN_COLLECTION,
                                                 OMAGPlatformExtension.USER_ID,
                                                 null);
    }


    /**
     * Return a generic server configuration client for a named server.
     *
     * @param serverName server whose configuration document is being built
     * @return client
     * @throws Exception the client could not be created
     */
    public static OMAGServerConfigurationClient getServerConfigurationClient(String serverName) throws Exception
    {
        return new OMAGServerConfigurationClient(serverName,
                                                 OMAGPlatformExtension.getPlatformURLRoot(),
                                                 SECRETS_STORE_PROVIDER,
                                                 secretsStoreLocation(),
                                                 OMAGPlatformExtension.CLIENT_TOKEN_COLLECTION,
                                                 OMAGPlatformExtension.USER_ID,
                                                 null);
    }


    /**
     * Return a metadata access store configuration client for a named server.
     *
     * @param serverName server whose configuration document is being built
     * @return client
     * @throws Exception the client could not be created
     */
    public static MetadataAccessStoreConfigurationClient getMetadataAccessStoreConfigurationClient(String serverName) throws Exception
    {
        return new MetadataAccessStoreConfigurationClient(serverName,
                                                          OMAGPlatformExtension.getPlatformURLRoot(),
                                                          SECRETS_STORE_PROVIDER,
                                                          secretsStoreLocation(),
                                                          OMAGPlatformExtension.CLIENT_TOKEN_COLLECTION,
                                                          OMAGPlatformExtension.USER_ID,
                                                          null);
    }


    /**
     * Return a view server configuration client for a named server.
     *
     * @param serverName server whose configuration document is being built
     * @return client
     * @throws Exception the client could not be created
     */
    public static ViewServerConfigurationClient getViewServerConfigurationClient(String serverName) throws Exception
    {
        return new ViewServerConfigurationClient(serverName,
                                                 OMAGPlatformExtension.getPlatformURLRoot(),
                                                 SECRETS_STORE_PROVIDER,
                                                 secretsStoreLocation(),
                                                 OMAGPlatformExtension.CLIENT_TOKEN_COLLECTION,
                                                 OMAGPlatformExtension.USER_ID,
                                                 null);
    }


    /**
     * Return an engine host configuration client for a named server.
     *
     * @param serverName server whose configuration document is being built
     * @return client
     * @throws Exception the client could not be created
     */
    public static EngineHostConfigurationClient getEngineHostConfigurationClient(String serverName) throws Exception
    {
        return new EngineHostConfigurationClient(serverName,
                                                 OMAGPlatformExtension.getPlatformURLRoot(),
                                                 SECRETS_STORE_PROVIDER,
                                                 secretsStoreLocation(),
                                                 OMAGPlatformExtension.CLIENT_TOKEN_COLLECTION,
                                                 OMAGPlatformExtension.USER_ID,
                                                 null);
    }


    /**
     * Return an integration daemon configuration client for a named server.
     *
     * @param serverName server whose configuration document is being built
     * @return client
     * @throws Exception the client could not be created
     */
    public static IntegrationDaemonConfigurationClient getIntegrationDaemonConfigurationClient(String serverName) throws Exception
    {
        return new IntegrationDaemonConfigurationClient(serverName,
                                                        OMAGPlatformExtension.getPlatformURLRoot(),
                                                        SECRETS_STORE_PROVIDER,
                                                        secretsStoreLocation(),
                                                        OMAGPlatformExtension.CLIENT_TOKEN_COLLECTION,
                                                        OMAGPlatformExtension.USER_ID,
                                                        null);
    }


    /**
     * Return an engine host client for the running engine host.
     *
     * @return client
     * @throws Exception the client could not be created
     */
    public static EngineHostClient getEngineHostClient() throws Exception
    {
        return new EngineHostClient(OMAGPlatformExtension.ENGINE_HOST_NAME,
                                    OMAGPlatformExtension.getPlatformURLRoot(),
                                    SECRETS_STORE_PROVIDER,
                                    secretsStoreLocation(),
                                    OMAGPlatformExtension.CLIENT_TOKEN_COLLECTION,
                                    OMAGPlatformExtension.USER_ID,
                                    null);
    }


    /**
     * Return an integration daemon client for the running integration daemon.
     *
     * @return client
     * @throws Exception the client could not be created
     */
    public static IntegrationDaemon getIntegrationDaemonClient() throws Exception
    {
        return new IntegrationDaemon(OMAGPlatformExtension.INTEGRATION_DAEMON_NAME,
                                     OMAGPlatformExtension.getPlatformURLRoot(),
                                     SECRETS_STORE_PROVIDER,
                                     secretsStoreLocation(),
                                     OMAGPlatformExtension.CLIENT_TOKEN_COLLECTION,
                                     OMAGPlatformExtension.USER_ID,
                                     null);
    }


    /**
     * Return the URL root the repository services clients expect.
     * <br><br>
     * This is the plain platform URL, the same as every other client here takes - and it is worth a method
     * of its own only because the clients' own javadoc says otherwise.  Both {@code AuditLogServicesClient}
     * and {@code MetadataHighwayServicesClient} document {@code restURLRoot} as "of the form serverURLroot +
     * "/servers/" + serverName", but their code prepends {@code /servers/{0}/open-metadata/repository-services}
     * to it themselves.  Following the javadoc produces a doubled path segment and a 404 on every call.
     * <br><br>
     * That stale comment is a finding in its own right, and it is recorded here rather than as a test
     * because it is not something an assertion can catch: the tests below simply pass the URL the code
     * requires.
     *
     * @param serverName server being addressed
     * @return the platform url root
     */
    public static String getRepositoryServicesURLRoot(String serverName)
    {
        return OMAGPlatformExtension.getPlatformURLRoot();
    }


    /**
     * Return an audit log services client for a named server.
     *
     * @param serverName server being addressed
     * @return client
     * @throws Exception the client could not be created
     */
    public static AuditLogServicesClient getAuditLogServicesClient(String serverName) throws Exception
    {
        return new AuditLogServicesClient(serverName,
                                          getRepositoryServicesURLRoot(serverName),
                                          SECRETS_STORE_PROVIDER,
                                          secretsStoreLocation(),
                                          OMAGPlatformExtension.CLIENT_TOKEN_COLLECTION,
                                          OMAGPlatformExtension.USER_ID,
                                          null);
    }


    /**
     * Return a metadata highway services client for a named server.
     *
     * @param serverName server being addressed
     * @return client
     * @throws Exception the client could not be created
     */
    public static MetadataHighwayServicesClient getMetadataHighwayServicesClient(String serverName) throws Exception
    {
        return new MetadataHighwayServicesClient(serverName,
                                                 getRepositoryServicesURLRoot(serverName),
                                                 SECRETS_STORE_PROVIDER,
                                                 secretsStoreLocation(),
                                                 OMAGPlatformExtension.CLIENT_TOKEN_COLLECTION,
                                                 OMAGPlatformExtension.USER_ID,
                                                 null);
    }


    /**
     * Remove a server this suite created: shut it down if it is running, and delete its configuration
     * document.
     * <br><br>
     * Written to be safe to call on a server that was never started, or never configured, so that a test can
     * clean up in a finally block without first working out how far it got.
     *
     * @param serverName server to remove
     */
    public static void deleteServer(String serverName)
    {
        try
        {
            getPlatformServicesClient().shutdownAndUnregisterServer(serverName);
        }
        catch (Exception error)
        {
            /*
             * Deliberately swallowed.  This runs in clean-up, where the interesting failure is the one the
             * test itself reported; a server that was never started, or whose configuration was never
             * written, is the ordinary case here rather than a problem.
             */
            System.out.println("server-fvt: could not remove " + serverName + " (" + error.getClass().getSimpleName() +
                                       ": " + error.getMessage() + ")");
        }
    }


    /**
     * Issue a GET straight at the platform, presenting no credentials at all.
     * <br><br>
     * The clients cannot express this - building one obtains a token - so the tests that check which
     * endpoints are open without authentication use the JDK's own HTTP client instead.
     *
     * @param path path below the platform url root
     * @return the platform's response
     * @throws Exception the request could not be sent
     */
    public static HttpResponse<String> getWithoutCredentials(String path) throws Exception
    {
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(URI.create(OMAGPlatformExtension.getPlatformURLRoot() + path))
                                         .GET()
                                         .build();

        return HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }


    /**
     * Issue a POST straight at the platform, presenting no credentials.
     *
     * @param path path below the platform url root
     * @param jsonBody request body
     * @return the platform's response
     * @throws Exception the request could not be sent
     */
    public static HttpResponse<String> postWithoutCredentials(String path,
                                                              String jsonBody) throws Exception
    {
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(URI.create(OMAGPlatformExtension.getPlatformURLRoot() + path))
                                         .header("Content-Type", "application/json")
                                         .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                                         .build();

        return HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
