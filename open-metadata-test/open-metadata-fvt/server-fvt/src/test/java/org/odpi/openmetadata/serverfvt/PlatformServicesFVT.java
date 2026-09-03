/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serverfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.client.MetadataAccessStoreConfigurationClient;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;
import org.odpi.openmetadata.platformservices.client.PlatformServicesClient;
import org.odpi.openmetadata.serveroperations.properties.ServerStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * PlatformServicesFVT covers what the platform can say about itself and about the servers on it: where it
 * came from, what services are registered in it, which servers it knows about, which of those are running,
 * and the starting and stopping of them.
 * <br><br>
 * The server lifecycle tests here are the ones worth reading.  Starting and stopping a server is the single
 * most-used operation in this whole API surface, and it is also the one whose <em>intermediate</em> states
 * nobody checks: a server that has been configured but not started, one that has been started, and one that
 * has been shut down but not unregistered are three different states, and this API is where an operator goes
 * to tell them apart.  So each transition below is followed by asking the platform what it now thinks,
 * rather than by trusting that a call which did not throw did what it said.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class PlatformServicesFVT
{
    /**
     * The platform should report its origin and when it started.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void thePlatformReportsItsOriginAndStartTime() throws Exception
    {
        PlatformServicesClient client = ServerFvtTestSupport.getPlatformServicesClient();

        String origin = client.getPlatformOrigin();

        assertNotNull(origin, "The platform should report an origin");
        assertFalse(origin.isBlank(), "The platform's origin should not be blank");

        Date startTime = client.getPlatformStartTime();

        assertNotNull(startTime, "The platform should report when it started");
        assertTrue(startTime.before(new Date()) || startTime.equals(new Date()),
                   "The platform cannot have started in the future, but reported " + startTime);
    }


    /**
     * The three servers the extension started should all be reported as known and active.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void startedServersAreKnownAndActive() throws Exception
    {
        PlatformServicesClient client = ServerFvtTestSupport.getPlatformServicesClient();

        List<String> knownServers  = client.getKnownServers();
        List<String> activeServers = client.getActiveServers();

        assertNotNull(knownServers, "The platform should report the servers it knows about");
        assertNotNull(activeServers, "The platform should report the servers that are running");

        for (String serverName : List.of(OMAGPlatformExtension.METADATA_STORE_NAME,
                                         OMAGPlatformExtension.ENGINE_HOST_NAME,
                                         OMAGPlatformExtension.INTEGRATION_DAEMON_NAME))
        {
            assertTrue(knownServers.contains(serverName),
                       "The platform should know about " + serverName + ", but reported " + knownServers);
            assertTrue(activeServers.contains(serverName),
                       "The platform should report " + serverName + " as active, but reported " + activeServers);
            assertTrue(client.isServerKnown(serverName),
                       "isServerKnown should agree with getKnownServers about " + serverName);
        }
    }


    /**
     * The platform should list the services registered in it, and every service in a category should also
     * appear in the list of all services.
     * <br><br>
     * That second part is the assertion with teeth, and it failed when it was first written: the four engine
     * services - Governance Action OMES, Survey Action OMES, Watchdog Action OMES and Repository Governance
     * OMES - were reported by {@code getEngineServices()} and absent from {@code getAllServices()}.
     * {@code OMAGServerPlatformInstanceMap.getAllRegisteredServices} aggregated the common, access, view and
     * governance categories and simply omitted the engine one.
     * <br><br>
     * There are six of these endpoints, one per category plus one for everything, and this is exactly the way
     * they drift: a category is added or grows, and the "all" list is not updated alongside it.  It matters
     * because "all" is what a tool building a picture of the platform reads - the Runtime Manager API among
     * them - so a service missing from it does not exist as far as that tool is concerned, even though the
     * platform is running it and the category endpoint lists it.
     * <br><br>
     * The test compares by name across every category rather than by count, so it stays honest if a sixth
     * category is added later: a new category that the aggregation forgets will fail this the same way.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void thePlatformListsItsRegisteredServices() throws Exception
    {
        PlatformServicesClient client = ServerFvtTestSupport.getPlatformServicesClient();

        assertFalse(client.getAccessServices().isEmpty(), "The platform should have access services registered");
        assertFalse(client.getViewServices().isEmpty(), "The platform should have view services registered");
        assertFalse(client.getEngineServices().isEmpty(), "The platform should have engine services registered");
        assertFalse(client.getCommonServices().isEmpty(), "The platform should have common services registered");
        assertNotNull(client.getGovernanceServices(), "The platform should report its governance services");

        /*
         * Compared by name rather than by count.  A count that does not add up says only that something is
         * wrong; the names say which services the "all" list is missing, and that is the difference between
         * a finding somebody can act on and one they have to reproduce first.
         */
        List<String> allServiceNames = new ArrayList<>();

        for (RegisteredOMAGService service : client.getAllServices())
        {
            allServiceNames.add(service.getServiceName());
        }

        List<String> missing = new ArrayList<>();

        for (List<RegisteredOMAGService> category : List.of(client.getAccessServices(),
                                                            client.getViewServices(),
                                                            client.getEngineServices(),
                                                            client.getCommonServices(),
                                                            client.getGovernanceServices()))
        {
            for (RegisteredOMAGService service : category)
            {
                if (! allServiceNames.contains(service.getServiceName()))
                {
                    missing.add(service.getServiceName());
                }
            }
        }

        assertTrue(missing.isEmpty(),
                   "Every service reported in a category should also appear in the list of all registered" +
                           " services, but these did not: " + missing);
    }


    /**
     * The platform should describe a connector provider that is on its classpath, and refuse one that is
     * not.
     * <br><br>
     * This is what a tool configuring a connector calls to check a class name before writing it into a
     * configuration document, so the refusal has to name the class it could not find - otherwise the caller
     * cannot tell a typo from a jar that is not deployed.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void thePlatformDescribesAConnectorTypeAndRefusesAnUnknownOne() throws Exception
    {
        PlatformServicesClient client = ServerFvtTestSupport.getPlatformServicesClient();

        ConnectorType connectorType = client.getConnectorType(
                "org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector.InMemoryOMRSRepositoryConnectorProvider");

        assertNotNull(connectorType, "The platform should describe a connector provider on its own classpath");
        assertNotNull(connectorType.getConnectorProviderClassName(),
                      "A connector type should name the provider class it describes");

        final String unknownProvider = "org.odpi.openmetadata.serverfvt.NoSuchConnectorProvider";

        Exception error = assertThrows(Exception.class,
                                       () -> client.getConnectorType(unknownProvider),
                                       "The platform should refuse a connector provider that is not on its classpath");

        String message = String.valueOf(error.getMessage());

        assertTrue(message.contains(unknownProvider),
                   "The message should name the class that could not be found.  It said: " + message);
    }


    /**
     * A server should be startable, visible as running, stoppable, and then still known but not running.
     * <br><br>
     * The three states are checked separately on purpose.  "Known" and "active" are different questions -
     * the first is about a configuration document, the second about a running instance - and a platform that
     * conflated them would still pass a test that only started a server and asked whether it was there.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aServerCanBeStartedAndStoppedAndItsStateFollowed() throws Exception
    {
        final String serverName = "serverFvtLifecycle";

        try
        {
            MetadataAccessStoreConfigurationClient configurationClient =
                    ServerFvtTestSupport.getMetadataAccessStoreConfigurationClient(serverName);

            configurationClient.clearOMAGServerConfig();
            configurationClient.setServerUserId(OMAGPlatformExtension.USER_ID);
            configurationClient.setMaxPageSize(ServerFvtTestSupport.MAX_PAGE_SIZE);
            configurationClient.setInMemLocalRepository();
            configurationClient.addConsoleAuditLogDestination(new java.util.ArrayList<>());

            PlatformServicesClient client = ServerFvtTestSupport.getPlatformServicesClient();

            /*
             * Configured but never started: the platform should know the name and should not claim it is
             * running.
             */
            assertFalse(client.getActiveServers().contains(serverName),
                        "A server that has been configured but not started should not be reported as active");

            String startMessage = client.activateWithStoredConfig(serverName);

            assertNotNull(startMessage, "Starting a server should report what happened");
            assertTrue(client.getActiveServers().contains(serverName),
                       "A server that has just been started should be reported as active, but the platform reported " +
                               client.getActiveServers());
            assertTrue(client.isServerKnown(serverName), "A running server should be known to the platform");

            ServerStatus status = client.getServerStatus(serverName);

            assertNotNull(status, "A running server should have a status");
            assertTrue(status.getIsActive(), "A server that has just been started should report itself as active");
            assertNotNull(status.getServerStartTime(), "A running server should report when it started");

            client.shutdownServer(serverName);

            assertFalse(client.getActiveServers().contains(serverName),
                        "A server that has been shut down should not be reported as active, but the platform reported " +
                                client.getActiveServers());
            assertTrue(client.isServerKnown(serverName),
                       "A server that has been shut down but not unregistered should still be known to the platform");

            ServerStatus afterShutdown = client.getServerStatus(serverName);

            assertNotNull(afterShutdown, "A server that has been shut down should still have a status");
            assertFalse(afterShutdown.getIsActive(),
                        "A server that has been shut down should not report itself as active");
            assertNotNull(afterShutdown.getServerEndTime(),
                          "A server that has been shut down should report when it stopped");
        }
        finally
        {
            ServerFvtTestSupport.deleteServer(serverName);
        }
    }


    /**
     * Starting a server that has no configuration document should be refused, and the message should name
     * the server.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void startingAServerWithNoConfigurationIsRefused() throws Exception
    {
        PlatformServicesClient client = ServerFvtTestSupport.getPlatformServicesClient();

        final String unconfiguredServer = "serverFvtServerWithNoConfiguration";

        Exception error = assertThrows(Exception.class,
                                       () -> client.activateWithStoredConfig(unconfiguredServer),
                                       "Starting a server that was never configured should be refused");

        String message = String.valueOf(error.getMessage());

        assertFalse(message.isBlank(), "A refused server start should say something");
        assertTrue(message.contains(unconfiguredServer),
                   "The message should name the server that could not be started.  It said: " + message);
    }


    /**
     * Shutting down a server that is not running should be reported clearly rather than silently accepted.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void shuttingDownAServerThatIsNotRunningIsReportedClearly() throws Exception
    {
        PlatformServicesClient client = ServerFvtTestSupport.getPlatformServicesClient();

        final String notRunning = "serverFvtServerThatIsNotRunningAtAll";

        Exception error = assertThrows(Exception.class,
                                       () -> client.shutdownServer(notRunning),
                                       "Shutting down a server that is not running should be reported as a failure");

        assertTrue(String.valueOf(error.getMessage()).contains(notRunning),
                   "The message should name the server that was asked about.  It said: " + error.getMessage());
    }


    /**
     * A server that is not running should be reported as not running rather than as an error.
     * <br><br>
     * The distinction matters because this is how an operator checks whether something they configured has
     * come up: a failure here and "isActive false" mean quite different things.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aServerThatIsNotKnownIsReportedAsNotKnown() throws Exception
    {
        PlatformServicesClient client = ServerFvtTestSupport.getPlatformServicesClient();

        assertFalse(client.isServerKnown("serverFvtServerThatDoesNotExistAnywhere"),
                    "A server that has never been configured should not be reported as known");
    }


    /**
     * A null server name should be refused by the client before a request is sent.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aNullServerNameIsRefusedByTheClient() throws Exception
    {
        PlatformServicesClient client = ServerFvtTestSupport.getPlatformServicesClient();

        Exception error = assertThrows(Exception.class,
                                       () -> client.activateWithStoredConfig(null),
                                       "A null server name should be refused");

        String message = String.valueOf(error.getMessage());

        assertTrue(message.toLowerCase().contains("servername") || message.toLowerCase().contains("server name"),
                   "The message should name the parameter that was wrong.  It said: " + message);
    }


    /**
     * The list of services a running server is running should be reported, and should be a subset of the
     * services the platform has registered.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aRunningServerReportsTheServicesItIsRunning() throws Exception
    {
        PlatformServicesClient client = ServerFvtTestSupport.getPlatformServicesClient();

        List<String> services = client.getActiveServicesForServer(OMAGPlatformExtension.METADATA_STORE_NAME);

        assertNotNull(services, "A running server should report the services it is running");
        assertFalse(services.isEmpty(),
                    "A metadata access store configured with all of the access services should be running at least one");
    }


    /**
     * The platform services and server operations clients should agree about which services a server is
     * running.
     * <br><br>
     * Both answer the same question through different URLs, so this is the assertion that catches one of them
     * being changed and the other not - and it failed when it was first written: platform services included
     * {@code Server Operations} and server operations did not include itself.
     * <br><br>
     * The two build their answers from different places.  Platform services reads the platform's own instance
     * map, which every service registers with; server operations reads the status map that its activation
     * code fills in as it starts each subsystem - and server operations is the component doing the starting,
     * so it was the one running service that never recorded itself.  It now does, at the point the instance
     * is created, because that is when it begins answering for the server.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void bothClientsAgreeAboutARunningServersServices() throws Exception
    {
        List<String> fromPlatformServices =
                ServerFvtTestSupport.getPlatformServicesClient()
                                    .getActiveServicesForServer(OMAGPlatformExtension.METADATA_STORE_NAME);

        List<String> fromServerOperations =
                ServerFvtTestSupport.getServerOperationsClient()
                                    .getActiveServices(OMAGPlatformExtension.METADATA_STORE_NAME);

        List<String> onlyInPlatformServices = new ArrayList<>(fromPlatformServices);
        List<String> onlyInServerOperations  = new ArrayList<>(fromServerOperations);

        onlyInPlatformServices.removeAll(fromServerOperations);
        onlyInServerOperations.removeAll(fromPlatformServices);

        assertTrue(onlyInPlatformServices.isEmpty() && onlyInServerOperations.isEmpty(),
                   "The two clients answer the same question about the same running server through different" +
                           " URLs, so they should agree.  Only platform services reported " + onlyInPlatformServices +
                           "; only server operations reported " + onlyInServerOperations + ".");
    }
}
