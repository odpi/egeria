/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.bvt;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.odpi.openmetadata.adminservices.client.MetadataAccessStoreConfigurationClient;
import org.odpi.openmetadata.platformchassis.springboot.OMAGServerPlatform;
import org.odpi.openmetadata.platformservices.client.PlatformServicesClient;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * OMAGPlatformExtension starts a single OMAG Server Platform in-process for the whole BVT run (no Kafka,
 * no PostgreSQL - an in-memory repository and console audit log are used instead), configures and starts
 * a metadata access store server on it, and shuts everything down once when the test run finishes.
 * <br>
 * It follows the JUnit 5 "singleton resource" pattern: the first test class that is extended with this
 * class pays the (one-off) startup cost in its {@code @BeforeAll}; every other extended class reuses the
 * same running platform and server.  Registering the extension against the root context's store ties its
 * {@link #close()} call to the end of the whole test run rather than to any one test class.
 */
public class OMAGPlatformExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource
{
    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(OMAGPlatformExtension.class);
    private static final String                      STORE_KEY = OMAGPlatformExtension.class.getName();

    /**
     * Name of the metadata access store server that is configured and started for the BVT suite.
     */
    public static final String SERVER_NAME = "bvtMetadataAccessStore";

    /**
     * UserId used for all admin, platform and metadata calls made by the BVT suite.
     */
    public static final String USER_ID = "bvtuser";

    private static volatile boolean                 started = false;
    private static ConfigurableApplicationContext    platformContext;
    private static String                            platformURLRoot;


    /**
     * Return the root URL of the running platform, for example "http://localhost:54321".
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
                    started = true;

                    context.getRoot().getStore(NAMESPACE).put(STORE_KEY, this);
                }
            }
        }
    }


    /**
     * Start the OMAG Server Platform's Spring Boot application in-process, listening on a free local port.
     * The platform uses a local file-based configuration document store under this module's build directory
     * and does not attempt to auto-start any servers of its own.
     */
    private void startPlatform()
    {
        Map<String, Object> properties = new HashMap<>();
        properties.put("server.port", "0");
        properties.put("platform.configstore.provider", "org.odpi.openmetadata.adapters.adminservices.configurationstore.file.FileBasedServerConfigStoreProvider");
        properties.put("platform.configstore.endpoint", "build/bvt-data/servers/{0}/config/{0}.config");
        properties.put("startup.server.list", "");
        properties.put("startup.user", "system");
        properties.put("authn.header.name.list", "");
        properties.put("cors.allowed-origins", "*");
        // No user directory is configured for this hermetic, in-process platform, so switch off Spring
        // Boot's default security auto-configuration rather than fall back to a generated basic-auth password.
        properties.put("spring.autoconfigure.exclude",
                        "org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration,"
                                + "org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration,"
                                + "org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration");
        properties.put("app.description", "open-metadata-bvt");
        properties.put("app.title", "open-metadata-bvt");
        properties.put("scan.packages", "org.odpi.openmetadata.*");
        properties.put("springdoc.api-docs.enabled", "false");
        properties.put("management.health.cassandra.enabled", "false");
        properties.put("management.health.redis.enabled", "false");
        properties.put("management.health.ldap.enabled", "false");
        properties.put("logging.level.root", "WARN");
        properties.put("logging.level.org.odpi.openmetadata", "WARN");
        // The connector-context-client tests deliberately look up deleted elements to check they are
        // gone - keep the resulting (expected) server-side FFDC logging out of the build output.
        properties.put("logging.level.org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler", "OFF");

        SpringApplicationBuilder builder = new SpringApplicationBuilder(OMAGServerPlatform.class);

        builder.web(WebApplicationType.SERVLET);
        builder.properties(properties);

        platformContext = builder.run();

        int port = ((ServletWebServerApplicationContext) platformContext).getWebServer().getPort();

        platformURLRoot = "http://localhost:" + port;
    }


    /**
     * Configure a metadata access store server (in-memory local repository, console audit log, all access
     * services enabled with no event topics) and start it, checking along the way that the platform is
     * responding and that the server actually came up.
     *
     * @throws Exception any problem configuring or starting the server is fatal to the whole BVT run
     */
    private void configureAndStartServer() throws Exception
    {
        PlatformServicesClient platformServicesClient = new PlatformServicesClient("BVT Platform",
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

        configurationClient.setServerUserId(USER_ID);
        // setBasicServerProperties insists on a fully specified secrets store connection (provider, location
        // and collection all non-null) even though this BVT server never actually looks up a secret from it.
        configurationClient.setBasicServerProperties("Egeria BVT",
                                                       "Server used by the open-metadata-bvt build verification test suite",
                                                       USER_ID,
                                                       "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider",
                                                       "build/bvt-data/secrets.omsecrets",
                                                       "bvt",
                                                       platformURLRoot,
                                                       100);
        configurationClient.setInMemLocalRepository();
        configurationClient.addConsoleAuditLogDestination(new ArrayList<>());
        configurationClient.configureAllAccessServicesNoTopics(new HashMap<>());

        platformServicesClient.activateWithStoredConfig(SERVER_NAME);

        if (! platformServicesClient.isServerKnown(SERVER_NAME))
        {
            throw new IllegalStateException("Server " + SERVER_NAME + " did not start on platform " + platformURLRoot);
        }
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
                new PlatformServicesClient("BVT Platform", platformURLRoot, null, null, null, USER_ID, null)
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
