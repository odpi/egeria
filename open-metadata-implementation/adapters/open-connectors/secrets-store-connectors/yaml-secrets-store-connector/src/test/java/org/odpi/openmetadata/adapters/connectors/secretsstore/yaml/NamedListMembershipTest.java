/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.secretsstore.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStoreConfigurationProperty;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.connectors.properties.users.NamedList;
import org.odpi.openmetadata.frameworks.connectors.properties.users.SecretsStore;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccount;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * Saving a user adds them to the named lists of their security roles and groups. The membership of a user belongs
 * in the userMembers of a list - listMembers names the nested lists - and it has to be added to a list the
 * connector can still grow, so that a role keeps every user it is given rather than just the first one.
 */
public class NamedListMembershipTest
{
    private static final String COLLECTION = "userDirectory";
    private static final String ROLE       = "data-steward";
    private static final String NEW_GROUP  = "engineering";


    /**
     * A secrets file with a role that already has a member, so a saved user is added to an existing named list.
     */
    private static String secretsFile()
    {
        return "secretsCollections:\n" +
               "  " + COLLECTION + ":\n" +
               "    refreshTimeInterval: 60\n" +
               "    namedLists:\n" +
               "      " + ROLE + ":\n" +
               "        listTypeName: " + OpenMetadataType.SECURITY_ROLE.typeName + "\n" +
               "        userMembers:\n" +
               "          - alice\n" +
               "    users:\n" +
               "      alice:\n" +
               "        userAccountStatus: AVAILABLE\n" +
               "        userAccountType: EMPLOYEE\n" +
               "        userName: Alice Anderson\n";
    }


    /**
     * Set up a started connector over the supplied secrets file.
     *
     * @param file secrets file
     * @return started connector
     * @throws Exception connector setup problem
     */
    private static YAMLSecretsStoreConnector startedConnector(Path file) throws Exception
    {
        Endpoint endpoint = new Endpoint();
        endpoint.setNetworkAddress(file.toString());

        Connection connection = new Connection();
        connection.setEndpoint(endpoint);
        connection.setConfigurationProperties(Map.of(SecretsStoreConfigurationProperty.SECRETS_COLLECTION_NAME.getName(), COLLECTION));

        YAMLSecretsStoreConnector connector = new YAMLSecretsStoreConnector();

        connector.initialize("test", connection);
        connector.start();

        return connector;
    }


    /**
     * A user account with the supplied roles and groups.
     *
     * @param userName name of the user
     * @param securityRoles roles the user has
     * @param securityGroups groups the user is a member of
     * @return user account
     */
    private static UserAccount userAccount(String       userName,
                                           List<String> securityRoles,
                                           List<String> securityGroups)
    {
        UserAccount userAccount = new UserAccount();

        userAccount.setUserName(userName);
        userAccount.setSecurityRoles(securityRoles);
        userAccount.setSecurityGroups(securityGroups);

        return userAccount;
    }


    /**
     * Read the named lists of the secrets collection back from the saved secrets file.
     *
     * @param file secrets file
     * @return the named lists of the secrets collection
     * @throws Exception problem reading the file
     */
    private static Map<String, NamedList> savedNamedLists(Path file) throws Exception
    {
        SecretsStore secretsStore = new ObjectMapper(new YAMLFactory()).readValue(file.toFile(), SecretsStore.class);

        return secretsStore.getSecretsCollections().get(COLLECTION).getNamedLists();
    }


    /**
     * A new member of a role is recorded as a user member, in an existing role and in one the connector creates,
     * and the nested lists of those roles are left alone.
     *
     * @throws Exception connector problem
     */
    @Test
    public void testNewMemberIsStoredAsAUserMember() throws Exception
    {
        Path file = Files.createTempFile("named-list-membership", ".omsecrets");

        try
        {
            Files.writeString(file, secretsFile());

            YAMLSecretsStoreConnector connector = startedConnector(file);

            connector.saveUser("bob", userAccount("Bob Brown", List.of(ROLE), List.of(NEW_GROUP)));

            NamedList role  = savedNamedLists(file).get(ROLE);
            NamedList group = savedNamedLists(file).get(NEW_GROUP);

            assertNull(role.getListMembers(), "a user id was stored in the nested lists of " + ROLE);
            assertTrue(role.getUserMembers().contains("bob"), "bob is not a user member of " + ROLE);

            assertNull(group.getListMembers(), "a user id was stored in the nested lists of " + NEW_GROUP);
            assertTrue(group.getUserMembers().contains("bob"), "bob is not a user member of " + NEW_GROUP);
        }
        finally
        {
            Files.deleteIfExists(file);
        }
    }


    /**
     * Adding a second user to a role that the connector has already extended needs the member list of the role to
     * stay growable.
     *
     * @throws Exception connector problem
     */
    @Test
    public void testEveryMemberOfARoleIsKept() throws Exception
    {
        Path file = Files.createTempFile("named-list-members", ".omsecrets");

        try
        {
            Files.writeString(file, secretsFile());

            YAMLSecretsStoreConnector connector = startedConnector(file);

            connector.saveUser("bob", userAccount("Bob Brown", List.of(ROLE), null));
            connector.saveUser("carol", userAccount("Carol Clark", List.of(ROLE), null));

            List<String> members = savedNamedLists(file).get(ROLE).getUserMembers();

            assertTrue(members.contains("alice"), "the original member of " + ROLE + " was lost");
            assertTrue(members.contains("bob"), "bob is not a user member of " + ROLE);
            assertTrue(members.contains("carol"), "carol is not a user member of " + ROLE);
        }
        finally
        {
            Files.deleteIfExists(file);
        }
    }
}
