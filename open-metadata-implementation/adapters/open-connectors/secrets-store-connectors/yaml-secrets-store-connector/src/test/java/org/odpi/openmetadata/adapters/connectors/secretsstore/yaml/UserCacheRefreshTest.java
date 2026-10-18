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
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * A user that has been looked up is cached.  Refreshing the secrets file must replace that cached copy, otherwise
 * a change to an existing user (a new password, a change of status) only takes effect after a platform restart.
 */
public class UserCacheRefreshTest
{
    private static final String COLLECTION = "userDirectory";

    private static String secretsFile(String password)
    {
        return "secretsCollections:\n" +
               "  " + COLLECTION + ":\n" +
               "    refreshTimeInterval: 10\n" +
               "    users:\n" +
               "      testuser:\n" +
               "        userAccountStatus: AVAILABLE\n" +
               "        userAccountType: EMPLOYEE\n" +
               "        userName: Test User\n" +
               "        secrets:\n" +
               "          clearPassword: " + password + "\n";
    }


    @Test
    public void testChangedUserIsVisibleAfterRefresh() throws Exception
    {
        Path file = Files.createTempFile("user-cache-refresh", ".omsecrets");

        try
        {
            Files.writeString(file, secretsFile("first"));

            Endpoint endpoint = new Endpoint();
            endpoint.setNetworkAddress(file.toString());

            Connection connection = new Connection();
            connection.setEndpoint(endpoint);
            connection.setConfigurationProperties(Map.of(SecretsStoreConfigurationProperty.SECRETS_COLLECTION_NAME.getName(), COLLECTION));

            YAMLSecretsStoreConnector connector = new YAMLSecretsStoreConnector();

            connector.initialize("test", connection);
            connector.start();

            UserAccount before = connector.getUser("testuser");

            assertNotNull(before);
            assertEquals(before.getSecrets().get("clearPassword"), "first");

            Files.writeString(file, secretsFile("second"));
            connector.refreshSecrets();

            UserAccount after = connector.getUser("testuser");

            assertNotNull(after);
            assertEquals(after.getSecrets().get("clearPassword"), "second");
        }
        finally
        {
            Files.deleteIfExists(file);
        }
    }
}
