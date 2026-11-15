/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.secretsstore.yaml;

import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStoreConfigurationProperty;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccount;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * A user account says which governance zones an element moves to when the user publishes it (publishZones) or
 * withdraws it (defaultZones), in the account's otherProperties.  The security connector treats a property that
 * is missing differently from one that is present with no zones: the first refuses the request, the second
 * removes all of the element's zones.  So the secrets store must be able to hold "present, with no zones" - as
 * an empty list or as a property with no value - and must not lose it when the file is rewritten.
 */
public class UserZoneSettingsTest
{
    private static final String COLLECTION = "userDirectory";

    private static final String SECRETS_FILE =
            "secretsCollections:\n" +
            "  " + COLLECTION + ":\n" +
            "    refreshTimeInterval: 10\n" +
            "    users:\n" +
            "      listuser:\n" +
            "        userAccountStatus: AVAILABLE\n" +
            "        userAccountType: EMPLOYEE\n" +
            "        userName: Zones Listed\n" +
            "        otherProperties:\n" +
            "          defaultZones:\n" +
            "            - quarantine\n" +
            "          publishZones:\n" +
            "            - trusted\n" +
            "      emptyuser:\n" +
            "        userAccountStatus: AVAILABLE\n" +
            "        userAccountType: EMPLOYEE\n" +
            "        userName: Zones Empty\n" +
            "        otherProperties:\n" +
            "          defaultZones: []\n" +
            "          publishZones: []\n" +
            "      nulluser:\n" +
            "        userAccountStatus: AVAILABLE\n" +
            "        userAccountType: EMPLOYEE\n" +
            "        userName: Zones Null\n" +
            "        otherProperties:\n" +
            "          defaultZones:\n" +
            "          publishZones: null\n" +
            "      plainuser:\n" +
            "        userAccountStatus: AVAILABLE\n" +
            "        userAccountType: EMPLOYEE\n" +
            "        userName: No Zone Settings\n";


    private static YAMLSecretsStoreConnector getConnector(Path file) throws Exception
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


    private static void assertZoneSettings(YAMLSecretsStoreConnector connector) throws Exception
    {
        UserAccount listUser = connector.getUser("listuser");

        assertNotNull(listUser);
        assertEquals(listUser.getOtherProperties().get("defaultZones"), List.of("quarantine"));
        assertEquals(listUser.getOtherProperties().get("publishZones"), List.of("trusted"));

        UserAccount emptyUser = connector.getUser("emptyuser");

        assertNotNull(emptyUser);
        assertTrue(emptyUser.getOtherProperties().containsKey("defaultZones"), "An empty defaultZones list should still be defined");
        assertTrue(emptyUser.getOtherProperties().containsKey("publishZones"), "An empty publishZones list should still be defined");
        assertEquals(emptyUser.getOtherProperties().get("defaultZones"), List.of());
        assertEquals(emptyUser.getOtherProperties().get("publishZones"), List.of());

        UserAccount nullUser = connector.getUser("nulluser");

        assertNotNull(nullUser);
        assertTrue(nullUser.getOtherProperties().containsKey("defaultZones"), "A defaultZones property with no value should still be defined");
        assertTrue(nullUser.getOtherProperties().containsKey("publishZones"), "A null publishZones property should still be defined");
        assertNull(nullUser.getOtherProperties().get("defaultZones"));
        assertNull(nullUser.getOtherProperties().get("publishZones"));

        UserAccount plainUser = connector.getUser("plainuser");

        assertNotNull(plainUser);
        assertTrue((plainUser.getOtherProperties() == null) || (! plainUser.getOtherProperties().containsKey("publishZones")),
                   "An account with no zone settings should not acquire any");
    }


    /**
     * Each way of writing a zone setting is read from the file as written.
     */
    @Test
    public void testZoneSettingsAreRead() throws Exception
    {
        Path file = Files.createTempFile("user-zone-settings", ".omsecrets");

        try
        {
            Files.writeString(file, SECRETS_FILE);

            assertZoneSettings(getConnector(file));
        }
        finally
        {
            Files.deleteIfExists(file);
        }
    }


    /**
     * Saving any user rewrites the whole file.  The empty and null settings of the other users must survive
     * that, or an unrelated change to the directory would silently turn "remove all zones" into "refuse".
     */
    @Test
    public void testZoneSettingsSurviveARewriteOfTheFile() throws Exception
    {
        Path file = Files.createTempFile("user-zone-settings", ".omsecrets");

        try
        {
            Files.writeString(file, SECRETS_FILE);

            YAMLSecretsStoreConnector connector = getConnector(file);

            UserAccount plainUser = connector.getUser("plainuser");

            plainUser.setUserName("Renamed");
            connector.saveUser("plainuser", plainUser);

            assertFalse(Files.readString(file).contains("No Zone Settings"), "The file should have been rewritten");

            /*
             * A second connector reads only what reached the file.
             */
            assertZoneSettings(getConnector(file));
        }
        finally
        {
            Files.deleteIfExists(file);
        }
    }


    /**
     * A user saved through the connector with an empty list, or with a null, keeps it.
     */
    @Test
    public void testZoneSettingsCanBeSaved() throws Exception
    {
        Path file = Files.createTempFile("user-zone-settings", ".omsecrets");

        try
        {
            Files.writeString(file, SECRETS_FILE);

            YAMLSecretsStoreConnector connector = getConnector(file);

            UserAccount plainUser = connector.getUser("plainuser");

            Map<String, Object> otherProperties = new java.util.HashMap<>();

            otherProperties.put("publishZones", new java.util.ArrayList<String>());
            otherProperties.put("defaultZones", null);

            plainUser.setOtherProperties(otherProperties);
            connector.saveUser("plainuser", plainUser);

            UserAccount saved = getConnector(file).getUser("plainuser");

            assertNotNull(saved);
            assertTrue(saved.getOtherProperties().containsKey("publishZones"), "A saved empty publishZones list should still be defined");
            assertEquals(saved.getOtherProperties().get("publishZones"), List.of());
            assertTrue(saved.getOtherProperties().containsKey("defaultZones"), "A saved null defaultZones should still be defined");
            assertNull(saved.getOtherProperties().get("defaultZones"));
        }
        finally
        {
            Files.deleteIfExists(file);
        }
    }
}
