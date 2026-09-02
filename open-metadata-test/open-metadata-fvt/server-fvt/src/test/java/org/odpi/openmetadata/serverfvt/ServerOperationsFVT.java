/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serverfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.serveroperations.client.ServerOperationsClient;
import org.odpi.openmetadata.serveroperations.properties.ServerServicesStatus;
import org.odpi.openmetadata.serveroperations.properties.ServerStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ServerOperationsFVT covers the {@code server-operations} service, whose subject is a <em>running</em>
 * server: what it is running, what configuration it is running with, and adding an open metadata archive to
 * it while it runs.
 * <br><br>
 * Every method on {@link ServerOperationsClient} is called here, and that is the point of this class rather
 * than a by-product.  Six of the client's seven operations have a counterpart on
 * {@code PlatformServicesClient} - five of them under the same name, and {@code getActiveServices} under the
 * name {@code getActiveServicesForServer} - each addressing a different URL for the same answer.  The
 * duplicates are where the defects are: a question with two clients has two chances to drift from its
 * endpoint, and no compiler to notice when one of them does.  {@link #serverStatusIsAvailableThroughTheServerOperationsClient}
 * and {@link #anArchiveConnectionCanBeAddedToARunningServer} are both failing at the time of writing, and
 * each has the detail against it.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class ServerOperationsFVT
{
    /**
     * A running server should report which services it is running.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aRunningServerReportsItsActiveServices() throws Exception
    {
        ServerOperationsClient client = ServerFvtTestSupport.getServerOperationsClient();

        List<String> services = client.getActiveServices(OMAGPlatformExtension.METADATA_STORE_NAME);

        assertNotNull(services, "A running metadata access store should report the services it is running");
        assertFalse(services.isEmpty(),
                    "A metadata access store configured with all of the access services should be running at least one service");
    }


    /**
     * A running server should report the configuration it is actually running with, which is not necessarily
     * the configuration document on disk.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aRunningServerReportsTheConfigurationItIsRunningWith() throws Exception
    {
        ServerOperationsClient client = ServerFvtTestSupport.getServerOperationsClient();

        OMAGServerConfig config = client.getActiveConfiguration(OMAGPlatformExtension.METADATA_STORE_NAME);

        assertNotNull(config, "A running server should report its active configuration");
        assertEquals(OMAGPlatformExtension.METADATA_STORE_NAME, config.getLocalServerName(),
                     "The active configuration should be the one belonging to the server that was asked");
        assertEquals(ServerFvtTestSupport.MAX_PAGE_SIZE, config.getMaxPageSize(),
                     "The active configuration should carry the max page size the server was configured with");
    }


    /**
     * A running server should report its own status and the status of each of its services.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aRunningServerReportsTheStatusOfItsServices() throws Exception
    {
        ServerOperationsClient client = ServerFvtTestSupport.getServerOperationsClient();

        ServerServicesStatus status = client.getActiveServerStatus(OMAGPlatformExtension.METADATA_STORE_NAME);

        assertNotNull(status, "A running server should report its status");
        assertEquals(OMAGPlatformExtension.METADATA_STORE_NAME, status.getServerName(),
                     "The status should be the one belonging to the server that was asked");
        assertNotNull(status.getServerActiveStatus(), "A running server should report an active status");
        assertNotNull(status.getServices(), "A running server should report the status of its services");
        assertFalse(status.getServices().isEmpty(),
                    "A metadata access store should report the status of at least one service");
    }


    /**
     * The server operations client should be able to report a server's status.
     * <br><br>
     * This failed when it was first written, and the account is kept because the defect is an easy one to
     * reintroduce.  {@code ServerOperationsClient.getServerStatus} called
     * {@code GET /open-metadata/server-operations/servers/{serverName}/status}, and no such endpoint exists:
     * {@code OMAGServerResource}, the only controller mapped under {@code /open-metadata/server-operations},
     * publishes {@code /servers/{serverName}/instance/status} and {@code /servers/{serverName}/services} but
     * nothing at {@code /status}.  A server's status - when it started, when it stopped, and its history of
     * previous instances - is held by the <em>platform</em>, so it is the platform services that answer for
     * it, and the client now addresses them.
     * <br><br>
     * It compiled, it was public API, and it 404d on the first call.  Nothing else in the repository called
     * it, which is why it survived.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void serverStatusIsAvailableThroughTheServerOperationsClient() throws Exception
    {
        ServerOperationsClient client = ServerFvtTestSupport.getServerOperationsClient();

        ServerStatus status = client.getServerStatus(OMAGPlatformExtension.METADATA_STORE_NAME);

        assertNotNull(status, "The server operations client should report a server's status");
        assertEquals(OMAGPlatformExtension.METADATA_STORE_NAME, status.getServerName(),
                     "The status should name the server that was asked about");
        assertTrue(status.getIsActive(), "A server this suite started should be reported as active");
        assertNotNull(status.getServerStartTime(), "A running server should report when it started");
    }


    /**
     * The two clients that both offer {@code getServerStatus} should give the same answer.
     * <br><br>
     * This is the assertion that says the duplication above is a duplication rather than two different
     * questions.  It failed for the same reason the previous test did, and now that both clients reach the
     * same endpoint it is what says they still agree.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void theTwoClientsShouldAgreeAboutAServerStatus() throws Exception
    {
        ServerStatus fromPlatformServices =
                ServerFvtTestSupport.getPlatformServicesClient().getServerStatus(OMAGPlatformExtension.METADATA_STORE_NAME);

        assertNotNull(fromPlatformServices, "The platform services client should report a server's status");
        assertTrue(fromPlatformServices.getIsActive(), "A server this suite started should be reported as active");

        ServerStatus fromServerOperations =
                ServerFvtTestSupport.getServerOperationsClient().getServerStatus(OMAGPlatformExtension.METADATA_STORE_NAME);

        assertEquals(fromPlatformServices.getServerName(), fromServerOperations.getServerName(),
                     "Both clients should name the same server");
        assertEquals(fromPlatformServices.getIsActive(), fromServerOperations.getIsActive(),
                     "Both clients should agree about whether the server is running");
    }


    /**
     * Adding an archive by connection to a running server should be accepted.
     * <br><br>
     * This failed when it was first written, and it failed before a request was ever sent.
     * {@code ServerOperationsClient.addOpenMetadataArchive} builds the URL template
     * {@code .../servers/{0}/instance/open-metadata-archives/connection?delegatingUserId={1}} - two
     * variables - and called {@code callVoidPostRESTCall(methodName, urlTemplate, connection, serverName)},
     * supplying only one.  URI expansion then threw
     * {@code IllegalArgumentException: Not enough variable values available to expand '1'}.
     * <br><br>
     * The two sibling methods on the same client, {@code addOpenMetadataArchiveFile} and
     * {@code addOpenMetadataArchiveContent}, both passed {@code delegatingUserId} and were correct.  This one
     * omitted it - a plain slip rather than a design difference, and one that no amount of using the REST API
     * directly would ever have surfaced, because it was entirely client-side.
     * <br><br>
     * The connection used here does not have to resolve to a real archive: the failure under test happens
     * while the URL is being built, long before the platform sees the request.  The assertion is therefore
     * that the call reaches the server and is answered - which, given a connection that cannot be
     * instantiated, means an ordinary failure response rather than success.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anArchiveConnectionCanBeAddedToARunningServer() throws Exception
    {
        ServerOperationsClient client = ServerFvtTestSupport.getServerOperationsClient();

        Connection    connection    = new Connection();
        ConnectorType connectorType = new ConnectorType();
        Endpoint      endpoint      = new Endpoint();

        connectorType.setConnectorProviderClassName(
                "org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.file.FileBasedOpenMetadataArchiveStoreProvider");
        endpoint.setNetworkAddress("build/server-fvt-data/no-such-archive.json");

        connection.setDisplayName("server-fvt archive that does not exist");
        connection.setConnectorType(connectorType);
        connection.setEndpoint(endpoint);

        Exception error = assertThrows(Exception.class,
                                       () -> client.addOpenMetadataArchive(OMAGPlatformExtension.METADATA_STORE_NAME, connection),
                                       "Loading an archive from a file that does not exist should be reported as a failure");

        /*
         * Deliberately asserting on *which* failure, and doing it by looking inside the message rather than
         * at the exception type.  The REST client connector catches everything the call throws - including
         * the IllegalArgumentException raised while expanding the URI template - and re-reports it as a
         * client-side REST error naming the original class.  So "the client never sent anything" and "the
         * platform refused the archive" arrive as the same Java type, and only the message tells them apart.
         */
        String message = String.valueOf(error.getMessage());

        assertFalse(message.contains("IllegalArgumentException"),
                    "The client should send the request rather than failing to build its URL.  It reported: " + message);
        assertFalse(message.contains("Not enough variable values"),
                    "The client's URL template and its arguments should match.  It reported: " + message);
    }


    /**
     * Adding an archive file to a running server should reach the server.
     * <br><br>
     * The file named does not exist, so the platform is expected to refuse it - the assertion is about where
     * the refusal comes from.  This is the correctly-written sibling of the method above, and it is here so
     * that the pair can be compared: if this one starts failing in the same way, the cause is the REST layer
     * rather than that one client method.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void addingAnArchiveFileThatDoesNotExistIsRefusedByTheServer() throws Exception
    {
        ServerOperationsClient client = ServerFvtTestSupport.getServerOperationsClient();

        Exception error = assertThrows(Exception.class,
                                       () -> client.addOpenMetadataArchiveFile(OMAGPlatformExtension.METADATA_STORE_NAME,
                                                                               "build/server-fvt-data/no-such-archive.json"),
                                       "Loading an archive from a file that does not exist should be reported as a failure");

        assertFalse(error instanceof IllegalArgumentException,
                    "The client should send the request rather than failing to build its URL.  It threw: " + error);
    }


    /**
     * Asking about a server that is not running should say so, rather than reporting an internal error or
     * quietly returning nothing.
     * <br><br>
     * This is the most common mistake an operator makes at this API - a typo in a server name, or a server
     * that has not been started yet - so what comes back is worth pinning down.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void askingAboutAServerThatIsNotRunningIsReportedClearly() throws Exception
    {
        ServerOperationsClient client = ServerFvtTestSupport.getServerOperationsClient();

        final String unknownServer = "serverFvtServerThatWasNeverStarted";

        Exception error = assertThrows(Exception.class,
                                       () -> client.getActiveServices(unknownServer),
                                       "Asking a server that is not running for its services should fail");

        assertNotNull(error.getMessage(), "A failure at an administration API should carry a message");
        assertTrue(error.getMessage().contains(unknownServer),
                   "The message should name the server that was asked about, so that the caller can see the typo." +
                           "  It said: " + error.getMessage());
    }


    /**
     * A null server name should be refused by the client, before a request is sent.
     * <br><br>
     * Client-side validation matters more here than it looks.  A null server name in the URL path does not
     * produce a tidy 400 - it produces a request to a URL with an empty path segment, which is a different
     * endpoint or none at all - so a client that passes it on turns a caller's mistake into a puzzle.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aNullServerNameIsRefusedByTheClient() throws Exception
    {
        ServerOperationsClient client = ServerFvtTestSupport.getServerOperationsClient();

        Exception error = assertThrows(Exception.class,
                                       () -> client.getActiveServices(null),
                                       "A null server name should be refused");

        assertNotNull(error.getMessage(), "Refusing a null server name should say what was wrong");
        assertTrue(error.getMessage().toLowerCase().contains("servername"),
                   "The message should name the parameter that was wrong.  It said: " + error.getMessage());
    }
}
