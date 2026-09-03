/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serverfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.client.MetadataAccessStoreConfigurationClient;
import org.odpi.openmetadata.adminservices.client.OMAGServerConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * AdminServicesValidationFVT asks what the administration API does with input it should not accept, and
 * whether what it says back is any use.
 * <br><br>
 * This is a separate class from {@link AdminServicesConfigurationFVT} because it is asking a different
 * question.  That class asks whether a correct request works; this one asks what happens when the request is
 * wrong - which, for an API that people drive by hand, is most of the time. A configuration API that accepts
 * nonsense silently produces a server that fails to start hours later with an error about something else
 * entirely, and one that rejects it with "an error occurred" leaves the caller guessing.
 * <br><br>
 * So the assertions here are mostly about the <em>message</em>, not just about the failure.  Two things are
 * asked of every rejection: that it happens at all, and that it names the parameter that was wrong.  The
 * second matters as much as the first - there are around forty setters on this API, most of them taking a
 * single string, and a message that does not say which one was rejected is not much better than silence.
 * <br><br>
 * It also holds the three admin-services client methods that address URLs that no longer exist.  They are
 * validation tests in the sense that matters here: each one is a request the caller cannot get right,
 * because there is nothing at the other end.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class AdminServicesValidationFVT
{
    /**
     * Setting a server's type should be accepted.
     * <br><br>
     * This failed when it was first written, and it is the one defect in this class that was <em>not</em> in
     * the client.  {@code OMAGServerConfigurationClient.setServerType} posts to
     * {@code /open-metadata/admin-services/servers/{serverName}/server-type} in exactly the way its
     * neighbour {@code setOrganizationName} does, and the administration service behind it - {@code
     * OMAGServerAdminServices.setServerType} - was there all along.  What was missing was the Spring mapping:
     * {@code ConfigPropertiesResource} published the neighbouring {@code /organization-name},
     * {@code /server-description} and {@code /server-user-id} setters, and a <em>GET</em> at
     * {@code /server-type-classification}, but nothing at {@code /server-type} - even though its own class
     * comment says it supports configuring the server type.  The mapping has been added.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aServerTypeCanBeSet() throws Exception
    {
        final String serverName = "serverFvtServerType";

        try
        {
            OMAGServerConfigurationClient client = ServerFvtTestSupport.getServerConfigurationClient(serverName);

            client.clearOMAGServerConfig();
            client.setServerUserId(OMAGPlatformExtension.USER_ID);

            client.setServerType("Metadata Access Store");

            OMAGServerConfig config = client.getOMAGServerConfig();

            assertNotNull(config, "A configured server should return its configuration document");
            assertNotNull(config.getLocalServerType(),
                          "A server that has just been given a type should report one");
        }
        finally
        {
            ServerFvtTestSupport.deleteServer(serverName);
        }
    }


    /**
     * A database audit log destination should be configurable.
     * <br><br>
     * This failed when it was first written.  {@code addJDBCAuditLogDestination} posted to
     * {@code .../audit-log-destinations/jdbc}, and {@code ConfigRepositoryServicesResource} publishes
     * {@code default}, {@code console}, {@code slf4j}, {@code files}, {@code postgres},
     * {@code event-topic} and {@code connection} - but not {@code jdbc}.  The JDBC destination had become
     * the PostgreSQL one and the client had not followed it.
     * <br><br>
     * The method is now deprecated and delegates to {@code addPostgreSQLAuditLogDestination}, which is new
     * and takes the storage properties that destination actually needs rather than a bare connection string.
     * Both are exercised here, because the deprecated one is what existing callers will still be using.
     * <br><br>
     * The same method carried a second, smaller defect, fixed at the same time: its {@code methodName} was
     * {@code "addFileAuditLogDestination"}, copied from the method below it, so every error it reported named
     * the wrong operation - which is how a 404 on {@code /jdbc} would come to be read as a problem with the
     * file destination.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aJDBCAuditLogDestinationCanBeConfigured() throws Exception
    {
        final String serverName = "serverFvtJDBCAuditLog";

        try
        {
            OMAGServerConfigurationClient client = ServerFvtTestSupport.getServerConfigurationClient(serverName);

            client.clearOMAGServerConfig();
            client.setServerUserId(OMAGPlatformExtension.USER_ID);

            Map<String, Object> storageProperties = new HashMap<>();

            storageProperties.put("databaseURL", "jdbc:postgresql://localhost:5432/egeria");
            storageProperties.put("databaseSchema", "audit_log_server_fvt");

            client.addPostgreSQLAuditLogDestination(storageProperties);

            assertNotNull(client.getOMAGServerConfig().getRepositoryServicesConfig(),
                          "A server with an audit log destination should have repository services configured");

            /*
             * The deprecated JDBC method has to keep working for callers that have not moved yet, so it is
             * called here too - it delegates to the PostgreSQL destination.  Neither call connects to a
             * database: configuring a destination stores a connection, it does not open one.
             */
            client.addJDBCAuditLogDestination(new ArrayList<>(), "jdbc:postgresql://localhost:5432/egeria");

            assertNotNull(client.getOMAGServerConfig().getRepositoryServicesConfig().getAuditLogConnections(),
                          "The audit log destinations should have been stored");
        }
        finally
        {
            ServerFvtTestSupport.deleteServer(serverName);
        }
    }


    /**
     * The configuration a running server is using should be reachable through the admin services client.
     * <br><br>
     * This failed when it was first written.
     * {@code OMAGServerConfigurationClient.getOMAGServerInstanceConfig} got
     * {@code /open-metadata/admin-services/servers/{serverName}/instance/configuration}, and nothing is
     * mapped there.  The endpoint exists twice elsewhere - at
     * {@code /open-metadata/platform-services/server-platform/servers/{serverName}/instance/configuration}
     * and at {@code /open-metadata/server-operations/servers/{serverName}/instance/configuration} - and the
     * clients for both of those reach it correctly, which is what
     * {@link ServerOperationsFVT#aRunningServerReportsTheConfigurationItIsRunningWith} shows.  The client now
     * addresses the server-operations one: the administration services own the configuration document on
     * disk, and the configuration a <em>running</em> instance is using belongs to the server operations.
     * <br><br>
     * The pattern across all three of this class's URL defects was the same: the endpoint was relocated to
     * another service and one client was left behind.  Nothing in the build notices, because the client and
     * the controller share only a string literal - which is the argument for this suite existing.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void theInstanceConfigurationIsReachableThroughTheAdminClient() throws Exception
    {
        OMAGServerConfigurationClient client =
                ServerFvtTestSupport.getServerConfigurationClient(OMAGPlatformExtension.METADATA_STORE_NAME);

        OMAGServerConfig config = client.getOMAGServerInstanceConfig();

        assertNotNull(config, "A running server should report the configuration it is running with");
    }


    /**
     * A null server name should be refused when the client is built, rather than producing a request to a
     * URL with a hole in it.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aNullServerNameIsRefused() throws Exception
    {
        Exception error = assertThrows(Exception.class,
                                       () -> ServerFvtTestSupport.getServerConfigurationClient(null),
                                       "A client for a null server name should be refused");

        assertNotNull(error.getMessage(), "Refusing a null server name should say what was wrong");
    }


    /**
     * A blank server name should be refused too.
     * <br><br>
     * Separate from the null case because they arrive by different routes - a null is usually a bug in the
     * caller, a blank string is usually a person.  This failed when it was first written: only the null case
     * was checked, so a blank name went into the URL path and left an empty segment, sending the request to a
     * different endpoint or to none and reporting whatever it happened to hit.
     * {@code InvalidParameterHandler.validateOMAGServerPlatformURL} now treats blank as not specified, for
     * the platform URL as well as the server name.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aBlankServerNameIsRefused() throws Exception
    {
        Exception error = assertThrows(Exception.class,
                                       () -> ServerFvtTestSupport.getServerConfigurationClient("   "),
                                       "A client for a blank server name should be refused");

        assertNotNull(error.getMessage(), "Refusing a blank server name should say what was wrong");
    }


    /**
     * A negative max page size should be refused, and the message should say which value was wrong.
     * <br><br>
     * Max page size is the one numeric property on this API, and an invalid one is not discovered until the
     * server is started and a query is run against it - so refusing it here is the difference between a
     * clear message now and a puzzling one much later.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aNegativeMaxPageSizeIsRefused() throws Exception
    {
        final String serverName = "serverFvtNegativePageSize";

        try
        {
            OMAGServerConfigurationClient client = ServerFvtTestSupport.getServerConfigurationClient(serverName);

            client.clearOMAGServerConfig();

            Exception error = assertThrows(Exception.class,
                                           () -> client.setMaxPageSize(-1),
                                           "A negative max page size should be refused");

            String message = String.valueOf(error.getMessage());

            assertTrue(message.toLowerCase().contains("page"),
                       "The message should name the property that was rejected.  It said: " + message);
        }
        finally
        {
            ServerFvtTestSupport.deleteServer(serverName);
        }
    }


    /**
     * A null connection should be refused where a connection is required.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aNullConnectionIsRefused() throws Exception
    {
        final String serverName = "serverFvtNullConnection";

        try
        {
            MetadataAccessStoreConfigurationClient client =
                    ServerFvtTestSupport.getMetadataAccessStoreConfigurationClient(serverName);

            client.clearOMAGServerConfig();

            Exception error = assertThrows(Exception.class,
                                           () -> client.setPluginRepositoryConnection((Connection) null),
                                           "A null repository connection should be refused");

            String message = String.valueOf(error.getMessage());

            assertTrue(message.toLowerCase().contains("connection"),
                       "The message should name the parameter that was rejected.  It said: " + message);
        }
        finally
        {
            ServerFvtTestSupport.deleteServer(serverName);
        }
    }


    /**
     * A connector provider class that is not on the platform's classpath should be refused, and the message
     * should name the class.
     * <br><br>
     * This is the single most common configuration mistake there is - a typo in a fully qualified class name
     * - and it is the one where a vague message costs the most, because the caller cannot tell a typo from a
     * missing jar without being told which name was tried.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anUnknownConnectorProviderIsRefusedByName() throws Exception
    {
        final String serverName          = "serverFvtUnknownProvider";
        final String unknownProviderName = "org.odpi.openmetadata.serverfvt.NoSuchConnectorProvider";

        try
        {
            MetadataAccessStoreConfigurationClient client =
                    ServerFvtTestSupport.getMetadataAccessStoreConfigurationClient(serverName);

            client.clearOMAGServerConfig();
            client.setServerUserId(OMAGPlatformExtension.USER_ID);

            Exception error = assertThrows(Exception.class,
                                           () -> client.setPluginRepositoryConnection(unknownProviderName, null),
                                           "A connector provider class that does not exist should be refused");

            String message = String.valueOf(error.getMessage());

            assertTrue(message.contains(unknownProviderName),
                       "The message should name the class that could not be found, because a typo is" +
                               " indistinguishable from a missing jar without it.  It said: " + message);
        }
        finally
        {
            ServerFvtTestSupport.deleteServer(serverName);
        }
    }


    /**
     * Asking for the configuration of a server that has never been configured should say so clearly.
     * <br><br>
     * The expected answer is not necessarily a failure - an unconfigured server reasonably has a default
     * document rather than none - but whichever it is, it should be unambiguous and should name the server.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void askingForAConfigurationThatWasNeverCreatedIsAnsweredClearly() throws Exception
    {
        final String serverName = "serverFvtNeverConfigured";

        try
        {
            OMAGServerConfigurationClient client = ServerFvtTestSupport.getServerConfigurationClient(serverName);

            OMAGServerConfig config = client.getOMAGServerConfig();

            /*
             * Deliberately not asserting that this fails.  A default document is a defensible answer and is
             * what the platform gives; what would not be defensible is a document belonging to some other
             * server, or one that claims to be configured when it is not.
             */
            if (config != null)
            {
                assertNotNull(config.getLocalServerName(),
                              "A configuration document should always name the server it belongs to");
                assertTrue(serverName.equals(config.getLocalServerName()),
                           "The document returned should belong to the server that was asked about, but named " +
                                   config.getLocalServerName());
                assertNull(config.getRepositoryServicesConfig(),
                           "A server that was never configured should not report repository services configuration");
            }
        }
        finally
        {
            ServerFvtTestSupport.deleteServer(serverName);
        }
    }


    /**
     * Deploying a configuration to a platform URL that is not a platform should be refused with a message
     * that names the URL.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void deployingToSomethingThatIsNotAPlatformIsRefused() throws Exception
    {
        final String serverName     = "serverFvtBadDeployTarget";
        final String badDestination = "http://localhost:1/not-a-platform";

        try
        {
            OMAGServerConfigurationClient client = ServerFvtTestSupport.getServerConfigurationClient(serverName);

            /*
             * Given a complete, valid configuration rather than a bare one, so that the failure under test
             * can only be the unreachable destination.  An incomplete document would be refused too, and
             * for a different reason.
             */
            client.clearOMAGServerConfig();
            client.setBasicServerProperties("Egeria server-fvt",
                                            "A server used to check what a failed deployment reports.",
                                            OMAGPlatformExtension.USER_ID,
                                            ServerFvtTestSupport.SECRETS_STORE_PROVIDER,
                                            OMAGPlatformExtension.getUserDirectoryPath().toString(),
                                            OMAGPlatformExtension.CLIENT_TOKEN_COLLECTION,
                                            OMAGPlatformExtension.getPlatformURLRoot(),
                                            ServerFvtTestSupport.MAX_PAGE_SIZE);

            Exception error = assertThrows(Exception.class,
                                           () -> client.deployOMAGServerConfig(badDestination),
                                           "Deploying to a URL that is not a platform should be refused");

            String message = String.valueOf(error.getMessage());

            assertFalse(message.isBlank(), "A failed deployment should say something");
            assertTrue(message.contains(badDestination),
                       "The message should name the destination that could not be reached.  It said: " + message);
        }
        finally
        {
            ServerFvtTestSupport.deleteServer(serverName);
        }
    }
}
