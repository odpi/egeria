/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serverfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.client.ConfigurationManagementClient;
import org.odpi.openmetadata.adminservices.client.MetadataAccessStoreConfigurationClient;
import org.odpi.openmetadata.adminservices.client.OMAGServerConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.BasicServerProperties;
import org.odpi.openmetadata.adminservices.rest.ServerTypeClassificationSummary;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStoreConfigurationProperty;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStorePurpose;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.EmbeddedConnection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.VirtualConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * AdminServicesConfigurationFVT covers the building of a configuration document: setting a property, reading
 * it back, changing it, clearing it, and confirming that what was stored is what comes back out.
 * <br><br>
 * Read-back is the whole method here.  Almost every call in this API returns a {@code VoidResponse}, so a
 * test that only checked "the call succeeded" would pass against a server that accepted the request and
 * stored nothing - and that is the failure mode this API actually has, because each setter writes into a
 * different corner of a large document.  So every test below sets a value and then goes and looks for it,
 * and where a property is reachable two ways - through its own getter and through the whole document - it is
 * checked both ways, because those are two separate pieces of server-side code.
 * <br><br>
 * Each test builds its own server and deletes it afterwards.  A configuration document is a single mutable
 * object per server name; sharing one between tests would make them order-dependent, and the first failure
 * would leave the rest reporting damage rather than their own result.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class AdminServicesConfigurationFVT
{
    /**
     * Set the basic properties of a server and read every one of them back.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void basicServerPropertiesAreStoredAndReturned() throws Exception
    {
        final String serverName = "serverFvtBasicProperties";

        try
        {
            OMAGServerConfigurationClient client = ServerFvtTestSupport.getServerConfigurationClient(serverName);

            client.clearOMAGServerConfig();

            client.setBasicServerProperties("server-fvt Organization",
                                            "A server built by basicServerPropertiesAreStoredAndReturned.",
                                            "serverFvtServerUser",
                                            ServerFvtTestSupport.SECRETS_STORE_PROVIDER,
                                            OMAGPlatformExtension.getUserDirectoryPath().toString(),
                                            OMAGPlatformExtension.CLIENT_TOKEN_COLLECTION,
                                            OMAGPlatformExtension.getPlatformURLRoot(),
                                            ServerFvtTestSupport.MAX_PAGE_SIZE);

            BasicServerProperties properties = client.getBasicServerProperties();

            assertNotNull(properties, "A server that has just been given its basic properties should return them");
            assertEquals("server-fvt Organization", properties.getOrganizationName(),
                         "The organization name should be the one that was set");
            assertEquals("A server built by basicServerPropertiesAreStoredAndReturned.", properties.getLocalServerDescription(),
                         "The description should be the one that was set");
            assertEquals("serverFvtServerUser", properties.getLocalServerUserId(),
                         "The server user id should be the one that was set");
            assertEquals(ServerFvtTestSupport.MAX_PAGE_SIZE, properties.getMaxPageSize(),
                         "The max page size should be the one that was set");

            /*
             * The same properties read from the whole document rather than from the basic-properties view.
             * These are different server-side paths onto the same stored config, and a property that appears
             * in one and not the other is a real defect - so they are checked separately rather than one
             * being taken as evidence for the other.
             */
            OMAGServerConfig config = client.getOMAGServerConfig();

            assertNotNull(config, "A configured server should return its configuration document");
            assertEquals(serverName, config.getLocalServerName(), "The document should name the server it belongs to");
            assertEquals("server-fvt Organization", config.getOrganizationName(),
                         "The document should carry the organization name that was set");
            assertEquals("serverFvtServerUser", config.getLocalServerUserId(),
                         "The document should carry the server user id that was set");
            assertEquals(ServerFvtTestSupport.MAX_PAGE_SIZE, config.getMaxPageSize(),
                         "The document should carry the max page size that was set");
        }
        finally
        {
            ServerFvtTestSupport.deleteServer(serverName);
        }
    }


    /**
     * Setting a property twice should replace it rather than accumulate.
     * <br><br>
     * Worth its own test because this API is used interactively and correcting a mistake is the normal way
     * to arrive at a working configuration.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aPropertyThatIsSetTwiceKeepsTheSecondValue() throws Exception
    {
        final String serverName = "serverFvtOverwrittenProperty";

        try
        {
            OMAGServerConfigurationClient client = ServerFvtTestSupport.getServerConfigurationClient(serverName);

            client.clearOMAGServerConfig();

            client.setOrganizationName("First Organization");
            assertEquals("First Organization", client.getOMAGServerConfig().getOrganizationName(),
                         "The organization name should be the one that was just set");

            client.setOrganizationName("Second Organization");
            assertEquals("Second Organization", client.getOMAGServerConfig().getOrganizationName(),
                         "Setting the organization name again should replace the first value");

            client.setMaxPageSize(250);
            assertEquals(250, client.getOMAGServerConfig().getMaxPageSize(),
                         "The max page size should be the one that was set");

            client.setMaxPageSize(750);
            assertEquals(750, client.getOMAGServerConfig().getMaxPageSize(),
                         "Setting the max page size again should replace the first value");
        }
        finally
        {
            ServerFvtTestSupport.deleteServer(serverName);
        }
    }


    /**
     * Clearing a server's configuration should leave nothing of the previous one behind.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void clearingAConfigurationRemovesWhatWasThere() throws Exception
    {
        final String serverName = "serverFvtClearedConfiguration";

        try
        {
            OMAGServerConfigurationClient client = ServerFvtTestSupport.getServerConfigurationClient(serverName);

            client.clearOMAGServerConfig();
            client.setOrganizationName("Organization That Should Not Survive");
            client.setServerUserId("serverFvtUserThatShouldNotSurvive");

            assertEquals("Organization That Should Not Survive", client.getOMAGServerConfig().getOrganizationName(),
                         "The organization name should have been stored before it is cleared");

            client.clearOMAGServerConfig();

            OMAGServerConfig config = client.getOMAGServerConfig();

            /*
             * A cleared configuration is a *default* document rather than an empty one or no document at
             * all.  That distinction is worth stating, because it is not what "clear" suggests: the server
             * name survives (it is in the URL), and so does a default local server user id of "OMAGServer".
             * What must not survive is anything the caller configured.
             */
            assertNotNull(config, "Clearing a configuration should leave a default document, not nothing");
            assertNull(config.getOrganizationName(),
                       "Clearing the configuration should have removed the organization name");
            assertNotEquals("serverFvtUserThatShouldNotSurvive", config.getLocalServerUserId(),
                            "Clearing the configuration should have removed the server user id that was set");
        }
        finally
        {
            ServerFvtTestSupport.deleteServer(serverName);
        }
    }


    /**
     * A server should report how it has been classified, which is derived from what has been configured into
     * it rather than set directly.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aServerReportsItsTypeClassification() throws Exception
    {
        final String serverName = "serverFvtTypeClassification";

        try
        {
            MetadataAccessStoreConfigurationClient client =
                    ServerFvtTestSupport.getMetadataAccessStoreConfigurationClient(serverName);

            client.clearOMAGServerConfig();
            client.setServerUserId(OMAGPlatformExtension.USER_ID);
            client.setMaxPageSize(ServerFvtTestSupport.MAX_PAGE_SIZE);
            client.setInMemLocalRepository();

            ServerTypeClassificationSummary summary = client.getServerTypeClassification();

            assertNotNull(summary, "A configured server should report a type classification");
            assertNotNull(summary.getServerTypeName(),
                          "A server with a local repository configured should be classified as some type of server");
        }
        finally
        {
            ServerFvtTestSupport.deleteServer(serverName);
        }
    }


    /**
     * A server always has at least one audit log destination, and adding one appends to that.
     * <br><br>
     * The extra destination here is by design and is worth stating, because at first sight it looks like a
     * caller's request being duplicated.  A server is not valid without repository services configuration, and
     * the default repository services configuration includes the default audit log - a particular
     * configuration of the console destination.  So the first destination a caller adds to a fresh server
     * arrives alongside that default rather than instead of it, and the server ends up with two.
     * <br><br>
     * What the test pins down is that this is the <em>only</em> extra: the caller's own destination is stored
     * as asked, one default accompanies it however many are added afterwards, and nothing else appears.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aServerAlwaysHasAnAuditLogDestinationAndAddingOneAppends() throws Exception
    {
        final String serverName = "serverFvtAuditLogDestinations";

        try
        {
            OMAGServerConfigurationClient client = ServerFvtTestSupport.getServerConfigurationClient(serverName);

            client.clearOMAGServerConfig();
            client.setServerUserId(OMAGPlatformExtension.USER_ID);

            client.addConsoleAuditLogDestination(new ArrayList<>());

            List<Connection> afterFirst = auditLogDestinations(client);

            assertEquals(2, afterFirst.size(),
                         "A fresh server gets the default audit log alongside the first destination added to it," +
                                 " so there should be two.  It reported " + describe(afterFirst));
            assertEquals(1, countDefaults(afterFirst),
                         "Exactly one of them should be the default audit log.  It reported " + describe(afterFirst));

            client.addSLF4JAuditLogDestination(new ArrayList<>());

            List<Connection> afterSecond = auditLogDestinations(client);

            assertEquals(3, afterSecond.size(),
                         "Adding a second destination should append to the list rather than replace it or bring" +
                                 " another default with it.  It reported " + describe(afterSecond));
            assertEquals(1, countDefaults(afterSecond),
                         "The default audit log should not be added again by a later destination.  It reported " +
                                 describe(afterSecond));

            client.clearAuditLogDestinations();

            List<Connection> afterClear = auditLogDestinations(client);

            assertTrue(afterClear.isEmpty(),
                       "Clearing the audit log destinations should remove all of them, but left " + describe(afterClear));
        }
        finally
        {
            ServerFvtTestSupport.deleteServer(serverName);
        }
    }


    /**
     * The default audit log destination should be removable, and addable again afterwards.
     * <br><br>
     * This is the other half of the rule that a server always has an audit log: the default is there so that
     * a server is never left without one, not because it is fixed.  An administrator who wants only their own
     * filtered console destination has to be able to take the default away, and to put it back when they
     * change their mind.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void theDefaultAuditLogDestinationCanBeRemovedAndAddedBack() throws Exception
    {
        final String serverName = "serverFvtDefaultAuditLog";

        try
        {
            OMAGServerConfigurationClient client = ServerFvtTestSupport.getServerConfigurationClient(serverName);

            client.clearOMAGServerConfig();
            client.setServerUserId(OMAGPlatformExtension.USER_ID);

            client.addSLF4JAuditLogDestination(new ArrayList<>());

            List<Connection> destinations = auditLogDestinations(client);

            assertEquals(1, countDefaults(destinations),
                         "The server should have picked up the default audit log.  It reported " + describe(destinations));

            String defaultName = null;

            for (Connection destination : destinations)
            {
                if (isDefault(destination))
                {
                    defaultName = destination.getQualifiedName();
                }
            }

            client.clearAuditLogDestination(defaultName);

            List<Connection> afterRemoval = auditLogDestinations(client);

            assertEquals(0, countDefaults(afterRemoval),
                         "The default audit log should have been removed, but the server reported " + describe(afterRemoval));
            assertEquals(1, afterRemoval.size(),
                         "Removing the default should leave the destination that was added, and only that." +
                                 "  It reported " + describe(afterRemoval));

            client.setDefaultAuditLog();

            List<Connection> afterRestore = auditLogDestinations(client);

            assertEquals(1, countDefaults(afterRestore),
                         "The default audit log should be addable again once it has been removed, but the server" +
                                 " reported " + describe(afterRestore));
        }
        finally
        {
            ServerFvtTestSupport.deleteServer(serverName);
        }
    }


    /**
     * An audit log destination should be removable by its display name as well as by its qualified name.
     * <br><br>
     * The administration service offers both: it looks for a qualified name first and falls back to matching
     * on the display name.  The fallback is the friendlier of the two - a display name is what an
     * administrator sees - so it is worth its own test rather than being assumed to work because the
     * qualified-name path does.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anAuditLogDestinationCanBeRemovedByDisplayName() throws Exception
    {
        final String serverName = "serverFvtAuditLogByDisplayName";

        try
        {
            OMAGServerConfigurationClient client = ServerFvtTestSupport.getServerConfigurationClient(serverName);

            client.clearOMAGServerConfig();
            client.setServerUserId(OMAGPlatformExtension.USER_ID);

            client.addSLF4JAuditLogDestination(new ArrayList<>());

            List<Connection> destinations = auditLogDestinations(client);

            assertEquals(2, destinations.size(),
                         "The server should hold the default audit log and the SLF4J destination.  It reported " +
                                 describe(destinations));

            String displayName = null;

            for (Connection destination : destinations)
            {
                if (! isDefault(destination))
                {
                    displayName = destination.getDisplayName();
                }
            }

            assertNotNull(displayName, "The added destination should have a display name to remove it by");

            client.clearAuditLogDestination(displayName);

            List<Connection> remaining = auditLogDestinations(client);

            assertEquals(1, remaining.size(),
                         "Removing one of two destinations by display name should leave exactly one." +
                                 "  It reported " + describe(remaining));
            assertEquals(1, countDefaults(remaining),
                         "The destination that should survive is the default, because the other one was removed." +
                                 "  It reported " + describe(remaining));
        }
        finally
        {
            ServerFvtTestSupport.deleteServer(serverName);
        }
    }


    /**
     * A whole configuration document should be storable in one call, and come back the same.
     * <br><br>
     * This is how a configuration is moved between platforms, so what matters is not just that the call is
     * accepted but that the document survives the round trip - it is serialized, sent, stored, re-read and
     * deserialized on the way through.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aWholeConfigurationDocumentSurvivesARoundTrip() throws Exception
    {
        final String sourceServer = "serverFvtRoundTripSource";
        final String targetServer = "serverFvtRoundTripTarget";

        try
        {
            MetadataAccessStoreConfigurationClient source =
                    ServerFvtTestSupport.getMetadataAccessStoreConfigurationClient(sourceServer);

            source.clearOMAGServerConfig();
            source.setServerUserId(OMAGPlatformExtension.USER_ID);
            source.setOrganizationName("server-fvt Round Trip");
            source.setMaxPageSize(321);
            source.setInMemLocalRepository();
            source.addConsoleAuditLogDestination(new ArrayList<>());

            OMAGServerConfig original = source.getOMAGServerConfig();

            assertNotNull(original, "The source server should have a configuration document to copy");

            /*
             * Renamed before it is stored, because a configuration document carries the name of the server
             * it belongs to and storing it under a different name is exactly what "deploy this config to
             * another server" does.
             */
            original.setLocalServerName(targetServer);

            OMAGServerConfigurationClient target = ServerFvtTestSupport.getServerConfigurationClient(targetServer);

            target.setOMAGServerConfig(original);

            OMAGServerConfig stored = target.getOMAGServerConfig();

            assertNotNull(stored, "The target server should have the configuration that was stored into it");
            assertEquals(targetServer, stored.getLocalServerName(),
                         "The stored document should name the server it was stored against");
            assertEquals("server-fvt Round Trip", stored.getOrganizationName(),
                         "The organization name should have survived the round trip");
            assertEquals(321, stored.getMaxPageSize(),
                         "The max page size should have survived the round trip");
            assertNotNull(stored.getRepositoryServicesConfig(),
                          "The repository services configuration should have survived the round trip");
            assertNotNull(stored.getRepositoryServicesConfig().getLocalRepositoryConfig(),
                          "The local repository configuration should have survived the round trip");
        }
        finally
        {
            ServerFvtTestSupport.deleteServer(sourceServer);
            ServerFvtTestSupport.deleteServer(targetServer);
        }
    }


    /**
     * The platform should list every configuration document it holds, and a server configured a moment ago
     * should be in that list.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void thePlatformListsTheConfigurationsItHolds() throws Exception
    {
        final String serverName = "serverFvtListedConfiguration";

        try
        {
            OMAGServerConfigurationClient client = ServerFvtTestSupport.getServerConfigurationClient(serverName);

            client.clearOMAGServerConfig();
            client.setServerUserId(OMAGPlatformExtension.USER_ID);
            client.setOrganizationName("server-fvt Listed");

            ConfigurationManagementClient configurationManagement = ServerFvtTestSupport.getConfigurationManagementClient();

            Set<OMAGServerConfig> allConfigurations = configurationManagement.getAllServerConfigurations();

            assertNotNull(allConfigurations, "The platform should list the configurations it holds");
            assertFalse(allConfigurations.isEmpty(),
                        "The platform holds at least this suite's own servers, so the list should not be empty");

            boolean found = false;

            for (OMAGServerConfig config : allConfigurations)
            {
                if (serverName.equals(config.getLocalServerName()))
                {
                    found = true;
                    break;
                }
            }

            assertTrue(found, "A server configured a moment ago should appear in the platform's list of configurations");

            /*
             * The same document, fetched by name rather than found in the list.  Two different endpoints,
             * and one can work while the other does not.
             */
            OMAGServerConfig byName = configurationManagement.getStoredOMAGServerConfig(serverName);

            assertNotNull(byName, "The platform should return a stored configuration by name");
            assertEquals("server-fvt Listed", byName.getOrganizationName(),
                         "The document fetched by name should be the one that was stored");
        }
        finally
        {
            ServerFvtTestSupport.deleteServer(serverName);
        }
    }


    /**
     * A server security connection should be stored, returned and cleared.
     * <br><br>
     * The connection is built the way the platform builds its own: a {@link VirtualConnection} whose embedded
     * connection - named for {@link SecretsStorePurpose#USER_DIRECTORY} - is the secrets store the connector
     * reads its user accounts from.  That embedded store is the part that matters, and an earlier version of
     * this test left it out.  A security connector with no user directory recognises nobody, so every
     * administration call that followed was refused; the connector was behaving correctly and the test was
     * wrong.  Supplying the directory this suite's administrator is in keeps them authorized, which is what
     * lets the round trip below be checked at all.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aServerSecurityConnectionIsStoredAndCleared() throws Exception
    {
        final String serverName = "serverFvtServerSecurity";

        try
        {
            OMAGServerConfigurationClient client = ServerFvtTestSupport.getServerConfigurationClient(serverName);

            client.clearOMAGServerConfig();
            client.setServerUserId(OMAGPlatformExtension.USER_ID);

            assertNull(client.getServerSecurityConnection(),
                       "A server that has had no security connection set should not report one");

            client.setServerSecurityConnection(
                    securityConnection("server-fvt server security connection",
                                       OMAGPlatformExtension.USER_DIRECTORY_COLLECTION));

            Connection stored = client.getServerSecurityConnection();

            assertNotNull(stored, "A server security connection that was just set should be returned");
            assertEquals("server-fvt server security connection", stored.getDisplayName(),
                         "The connection returned should be the one that was set");

            client.clearServerSecurityConnection();

            assertNull(client.getServerSecurityConnection(),
                       "Clearing the server security connection should remove it");
        }
        finally
        {
            ServerFvtTestSupport.deleteServer(serverName);
        }
    }


    /**
     * A server security connection should take effect on the very next call, including for the person who
     * set it.
     * <br><br>
     * This is the enforcement half of the test above, and it is worth having explicitly because the behaviour
     * looks alarming until it is stated as an intention.  Once a server security connection is stored,
     * {@code OMAGServerAdminStoreServices.getServerConfig} builds a verifier from it and calls
     * {@code validateUserAsServerAdmin} on every subsequent read and update of that server's configuration.
     * So an administrator who installs a connector whose user directory does not contain them has locked
     * themselves out of that server - not through a defect, but because the connector is answering honestly
     * about who it recognises, which is the whole reason for installing one.
     * <br><br>
     * The server here is deliberately not started: this is the <em>configuration</em> API enforcing the
     * server's security policy, which is the part that surprises.  Two things follow that are worth knowing
     * before using this call.  It takes effect immediately rather than at the next restart, so there is no
     * window in which to check the result and change your mind.  And because {@code
     * clearServerSecurityConnection} is itself an update of that configuration, it is refused too - so
     * recovery is a matter of correcting the user directory the connector points at, or removing the
     * configuration document, rather than of calling the API again.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aServerSecurityConnectionIsEnforcedFromTheNextCall() throws Exception
    {
        /*
         * A name of its own, and no attempt to remove the server afterwards: this test ends with a server
         * this suite's administrator can no longer configure, which is the point of it.  The configuration
         * document is left under the build directory, where a clean build discards it.
         */
        final String serverName = "serverFvtEnforcedServerSecurity";

        OMAGServerConfigurationClient client = ServerFvtTestSupport.getServerConfigurationClient(serverName);

        client.clearOMAGServerConfig();
        client.setServerUserId(OMAGPlatformExtension.USER_ID);

        /*
         * A user directory that holds the suite's *other* account and not the one this client is calling as.
         */
        client.setServerSecurityConnection(
                securityConnection("server-fvt server security connection excluding the caller",
                                   OMAGPlatformExtension.OTHER_USER_DIRECTORY_COLLECTION));

        Exception error = assertThrows(Exception.class,
                                       client::getServerSecurityConnection,
                                       "A server security connector that does not recognise the caller should refuse them");

        String message = String.valueOf(error.getMessage());

        assertTrue(message.contains(OMAGPlatformExtension.USER_ID),
                   "The refusal should name the user the connector did not recognise, because that is what tells" +
                           " an administrator which directory to correct.  It said: " + message);

        assertThrows(Exception.class,
                     client::clearServerSecurityConnection,
                     "Clearing the connection is an update of the same configuration, so it should be refused too" +
                             " - an administrator cannot undo this through the API");
    }


    /**
     * Build a server security connection of the shape the platform builds for itself: the security connector,
     * with the secrets store holding its user accounts as an embedded connection.
     *
     * @param displayName name for the connection, so a test can recognise what came back
     * @param secretsCollectionName collection within this suite's user directory that supplies the accounts
     * @return connection ready to be stored
     */
    private Connection securityConnection(String displayName,
                                          String secretsCollectionName)
    {
        Endpoint secretsEndpoint = new Endpoint();

        secretsEndpoint.setNetworkAddress(OMAGPlatformExtension.getUserDirectoryPath().toString());

        ConnectorType secretsConnectorType = new ConnectorType();

        secretsConnectorType.setConnectorProviderClassName(ServerFvtTestSupport.SECRETS_STORE_PROVIDER);

        Map<String, Object> secretsConfiguration = new HashMap<>();

        secretsConfiguration.put(SecretsStoreConfigurationProperty.SECRETS_COLLECTION_NAME.getName(), secretsCollectionName);

        Connection secretsConnection = new Connection();

        secretsConnection.setEndpoint(secretsEndpoint);
        secretsConnection.setConnectorType(secretsConnectorType);
        secretsConnection.setConfigurationProperties(secretsConfiguration);

        EmbeddedConnection embeddedConnection = new EmbeddedConnection();

        embeddedConnection.setDisplayName(SecretsStorePurpose.USER_DIRECTORY.getName());
        embeddedConnection.setEmbeddedConnection(secretsConnection);

        List<EmbeddedConnection> embeddedConnections = new ArrayList<>();

        embeddedConnections.add(embeddedConnection);

        ConnectorType connectorType = new ConnectorType();

        connectorType.setConnectorProviderClassName(
                "org.odpi.openmetadata.metadatasecurity.accessconnector.OpenMetadataAccessSecurityProvider");

        VirtualConnection connection = new VirtualConnection();

        connection.setDisplayName(displayName);
        connection.setConnectorType(connectorType);
        connection.setEmbeddedConnections(embeddedConnections);

        return connection;
    }


    /**
     * Return the audit log destinations a server currently has configured.
     *
     * @param client client for the server
     * @return the destinations, never null
     * @throws Exception problem reading the configuration
     */
    private List<Connection> auditLogDestinations(OMAGServerConfigurationClient client) throws Exception
    {
        OMAGServerConfig config = client.getOMAGServerConfig();

        if ((config == null) || (config.getRepositoryServicesConfig() == null)
                    || (config.getRepositoryServicesConfig().getAuditLogConnections() == null))
        {
            return new ArrayList<>();
        }

        return config.getRepositoryServicesConfig().getAuditLogConnections();
    }


    /**
     * Is this the default audit log destination - the one a server acquires by having repository services
     * configuration at all, rather than one a caller asked for?
     * <br><br>
     * Recognised by its qualified name, which the configuration factory builds as the destination name plus
     * the qualifier "- default".
     *
     * @param destination destination to examine
     * @return true when this is the default
     */
    private boolean isDefault(Connection destination)
    {
        return (destination.getQualifiedName() != null) && (destination.getQualifiedName().endsWith("- default"));
    }


    /**
     * Count how many of these destinations are the default audit log.
     *
     * @param destinations destinations to examine
     * @return number of defaults, which should never be more than one
     */
    private int countDefaults(List<Connection> destinations)
    {
        int count = 0;

        for (Connection destination : destinations)
        {
            if (isDefault(destination))
            {
                count++;
            }
        }

        return count;
    }


    /**
     * Describe a list of audit log destinations by name, for use in an assertion message.
     * <br><br>
     * The count alone is not enough to act on when one of these assertions fails: "expected 1 but was 2"
     * does not say whether the extra destination is a duplicate of the one that was asked for or something
     * else entirely, and those are different defects.
     *
     * @param destinations destinations to describe
     * @return their qualified names
     */
    private String describe(List<Connection> destinations)
    {
        List<String> names = new ArrayList<>();

        for (Connection destination : destinations)
        {
            names.add(destination.getQualifiedName());
        }

        return names.toString();
    }
}
