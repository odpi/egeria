/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.secretsstore.yaml;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.connectors.properties.users.SecretsCollection;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccount;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

/**
 * The YAML secrets file connector replaces and deletes whole secrets collections. The user accounts read from a
 * collection are cached by its base class, so those caches have to be replaced with the update - otherwise an
 * account, or a whole collection of them, keeps serving requests after the change that removed it.
 */
public class SecretsCollectionCacheTest
{
    private static final String COLLECTION = "userDirectory";
    private static final String OTHER      = "serviceAccounts";


    /**
     * A secrets file with alice's password in the collection under test, plus a second collection so that deleting
     * the first leaves a valid file behind.
     *
     * @param password clear password stored for alice
     * @return contents of the secrets file
     */
    private static String secretsFile(String password)
    {
        return "secretsCollections:\n" +
               "  " + COLLECTION + ":\n" +
               "    refreshTimeInterval: 60\n" +
               "    users:\n" +
               "      alice:\n" +
               "        userAccountStatus: AVAILABLE\n" +
               "        userAccountType: EMPLOYEE\n" +
               "        userName: Alice Anderson\n" +
               "        secrets:\n" +
               "          clearPassword: " + password + "\n" +
               "  " + OTHER + ":\n" +
               "    refreshTimeInterval: 60\n";
    }


    /**
     * Set up a started file connector over the supplied secrets file.
     *
     * @param file secrets file
     * @return started connector
     * @throws Exception connector setup problem
     */
    private static YAMLSecretsFileConnector startedConnector(Path file) throws Exception
    {
        Endpoint endpoint = new Endpoint();
        endpoint.setNetworkAddress(file.toString());

        Connection connection = new Connection();
        connection.setEndpoint(endpoint);

        YAMLSecretsFileConnector connector = new YAMLSecretsFileConnector();

        connector.initialize("test", connection);
        connector.start();
        connector.setSecretsCollectionName(COLLECTION);

        return connector;
    }


    /**
     * A collection holding alice with the supplied password.
     *
     * @param password clear password stored for alice
     * @return secrets collection
     */
    private static SecretsCollection collectionWithUser(String password)
    {
        UserAccount alice = new UserAccount();

        alice.setUserName("Alice Anderson");
        alice.setSecrets(Map.of("clearPassword", password));

        Map<String, UserAccount> users = new HashMap<>();

        users.put("alice", alice);

        SecretsCollection secretsCollection = new SecretsCollection();

        secretsCollection.setUsers(users);

        return secretsCollection;
    }


    /**
     * Replacing a collection changes the accounts that the connector serves for its users.
     *
     * @throws Exception connector problem
     */
    @Test
    public void testReplacedCollectionIsNotServedFromTheCache() throws Exception
    {
        Path file = Files.createTempFile("collection-save", ".omsecrets");

        try
        {
            Files.writeString(file, secretsFile("first"));

            YAMLSecretsFileConnector connector = startedConnector(file);

            UserAccount before = connector.getUser("alice");

            assertNotNull(before);
            assertEquals(before.getSecrets().get("clearPassword"), "first");

            connector.saveSecretsCollection(COLLECTION, collectionWithUser("second"));

            UserAccount after = connector.getUser("alice");

            assertNotNull(after);
            assertEquals(after.getSecrets().get("clearPassword"), "second",
                         "getUser() returned the account cached before saveSecretsCollection replaced " + COLLECTION);
        }
        finally
        {
            Files.deleteIfExists(file);
        }
    }


    /**
     * Deleting a collection takes its users with it.
     *
     * @throws Exception connector problem
     */
    @Test
    public void testDeletedCollectionTakesItsUsersWithIt() throws Exception
    {
        Path file = Files.createTempFile("collection-delete", ".omsecrets");

        try
        {
            Files.writeString(file, secretsFile("first"));

            YAMLSecretsFileConnector connector = startedConnector(file);

            assertNotNull(connector.getUser("alice"));

            connector.deleteSecretsCollection(COLLECTION);

            assertNull(connector.getUser("alice"),
                       "alice's collection was deleted but getUser() still returns her cached account");
        }
        finally
        {
            Files.deleteIfExists(file);
        }
    }
}
