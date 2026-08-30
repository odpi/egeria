/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.authfvt;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.odpi.openmetadata.platformchassis.springboot.OMAGServerPlatform;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

/**
 * OMAGPlatformExtension starts a single OMAG Server Platform in-process for the whole auth-fvt run, with
 * the platform's real authentication chain switched on, and shuts it down once the run finishes.
 * <br><br>
 * This is the one difference that matters between this suite and every other test suite in the
 * repository.  The others exclude {@code user-authn} and install a permit-all filter chain so they can
 * run without a user directory; here {@code user-authn} is on the classpath, {@code authentication.source}
 * is set to {@code platform}, and a user directory is configured - so requests go through the same
 * {@code SecurityFilterChain}, {@code AuthenticationManager} and JWT decoder that a real deployment uses.
 * <br><br>
 * The user directory is a <em>copy</em>.  Changing a password rewrites the directory file in place, so
 * running against the file in {@code src/test/resources} would modify the repository's own source tree
 * and make a second run start from a different state than the first.  The copy is taken fresh on every
 * run, which is also what makes the suite repeatable.
 */
public class OMAGPlatformExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource
{
    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(OMAGPlatformExtension.class);
    private static final String                     STORE_KEY = OMAGPlatformExtension.class.getName();

    /**
     * Name of the secrets collection inside the user directory file.
     */
    static final String SECRETS_COLLECTION = "authFvtUserDirectory";

    /**
     * Resource holding the user directory this suite logs in against.
     */
    private static final String SECRETS_RESOURCE = "/auth-fvt-user-directory.omsecrets";

    /**
     * Where the working copy of the user directory is placed.  Under the build directory so that a clean
     * build discards whatever the tests did to it.
     */
    private static final String SECRETS_WORKING_COPY = "build/auth-fvt-data/auth-fvt-user-directory.omsecrets";

    private static volatile boolean               started = false;
    private static ConfigurableApplicationContext platformContext;
    private static String                         platformURLRoot;
    private static Path                           userDirectoryPath;


    /**
     * Return the base URL of the running platform, including its randomly allocated port.
     *
     * @return url root
     */
    static String getPlatformURLRoot()
    {
        return platformURLRoot;
    }


    /**
     * Return the path of the working copy of the user directory, so that a test can inspect what the
     * platform actually persisted.
     *
     * @return path to the user directory YAML
     */
    static Path getUserDirectoryPath()
    {
        return userDirectoryPath;
    }


    /**
     * Start the platform once for the whole run.
     *
     * @param context junit context
     * @throws Exception problem starting the platform
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
                    userDirectoryPath = copyUserDirectory();
                    startPlatform();
                    started = true;
                    context.getRoot().getStore(NAMESPACE).put(STORE_KEY, this);
                }
            }
        }
    }


    /**
     * Take a fresh copy of the user directory into the build directory.  See the class comment for why
     * the tests must not run against the source file.
     *
     * @return path of the copy
     * @throws Exception the resource is missing or cannot be written
     */
    private Path copyUserDirectory() throws Exception
    {
        Path target = Paths.get(SECRETS_WORKING_COPY).toAbsolutePath();

        Files.createDirectories(target.getParent());

        try (InputStream source = OMAGPlatformExtension.class.getResourceAsStream(SECRETS_RESOURCE))
        {
            if (source == null)
            {
                throw new IllegalStateException("Missing test resource " + SECRETS_RESOURCE);
            }

            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        }

        return target;
    }


    /**
     * Start the platform on a randomly allocated port with authentication switched on.
     */
    private void startPlatform()
    {
        Map<String, Object> properties = new HashMap<>();

        properties.put("server.port", "0");
        properties.put("platform.configstore.provider", "org.odpi.openmetadata.adapters.adminservices.configurationstore.file.FileBasedServerConfigStoreProvider");
        properties.put("platform.configstore.endpoint", "build/auth-fvt-data/servers/{0}/config/{0}.config");
        properties.put("startup.server.list", "");
        properties.put("startup.user", "system");
        properties.put("cors.allowed-origins", "*");

        /*
         * The three properties that make this suite what it is: the platform reads its user accounts from
         * our own copy of the user directory, and PlatformUserDetailsService/PlatformSecurityConfig are
         * both conditional on authentication.source being "platform".
         */
        properties.put("authentication.source", "platform");
        properties.put("platform.security.provider", "org.odpi.openmetadata.metadatasecurity.accessconnector.OpenMetadataAccessSecurityProvider");
        properties.put("platform.security.name", "auth-fvt Platform");
        properties.put("platform.security.secrets.provider", "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider");
        properties.put("platform.security.secrets.location", userDirectoryPath.toString());
        properties.put("platform.security.secrets.collection", SECRETS_COLLECTION);

        /*
         * Deliberately NOT excluding Spring Security's auto-configuration here - unlike the BVT and the
         * other FVT suites, this one wants the real filter chain.
         */
        properties.put("authn.header.name.list", "");
        properties.put("app.description", "auth-fvt");
        properties.put("app.title", "auth-fvt");
        properties.put("scan.packages", "org.odpi.openmetadata.*");
        properties.put("springdoc.api-docs.enabled", "false");
        properties.put("management.health.cassandra.enabled", "false");
        properties.put("management.health.redis.enabled", "false");
        properties.put("management.health.ldap.enabled", "false");
        properties.put("logging.level.root", "WARN");
        properties.put("logging.level.org.springframework", "ERROR");
        properties.put("logging.level.org.odpi.openmetadata", "WARN");
        properties.put("logging.level.org.odpi.openmetadata.platformchassis.springboot", "INFO");

        /*
         * Rejected logons are the expected outcome of several tests, and each one logs an error
         * server-side.  Keep that (expected) noise out of the build output.
         */
        properties.put("logging.level.org.odpi.openmetadata.userauthn", "OFF");
        properties.put("logging.level.org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler", "OFF");

        SpringApplicationBuilder builder = new SpringApplicationBuilder(OMAGServerPlatform.class);

        builder.web(WebApplicationType.SERVLET);
        builder.properties(properties);

        platformContext = builder.run();

        int port = ((ServletWebServerApplicationContext) platformContext).getWebServer().getPort();

        platformURLRoot = "http://localhost:" + port;
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
