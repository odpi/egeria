/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.secretsstore.envar;

import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccount;

import java.util.Map;

/**
 * EnvVarSecretsStoreConnector retrieves secrets from environment variables.  Each secret is named for its environment variable.
 */
public class EnvVarSecretsStoreConnector extends SecretsStoreConnector
{
    /**
     * Request that the subclass refreshes its secrets.
     */
    protected void refreshSecrets()
    {
        // nothing to do
    }


    /**
     * Retrieve a secret from the secrets store.
     *
     * @param secretName name of the secret.
     * @return secret
     */
    @Override
    public String getSecret(String secretName)
    {
        return System.getenv(secretsCollectionName + "_" + secretName);
    }


    /**
     * Retrieve a secret from the secrets store.
     *
     * @param secretsCollectionName name of collection
     * @param secretName name of the secret.
     * @return secret
     */
    @Override
    public String getSecret(String secretsCollectionName,
                            String secretName)

    {
        return System.getenv(this.secretsCollectionName + "_" + secretName);
    }


    /**
     * Retrieve any user definitions stored in the secrets collection.
     *
     * @return map of userIds to user details
     */
    public Map<String, UserAccount> getUsers()
    {
        /*
         * Not supported.
         */
        return null;
    }


    /**
     * Retrieve the requested user definitions stored in the secrets collection.
     *
     * @param userId userId for the lookup
     * @return associated user details or null
     */
    public UserAccount getUser(String userId)
    {
        /*
         * Not supported.
         */
        return null;
    }


    /**
     * Retrieve the refresh time from the secrets store.
     *
     * @return how long the secrets can be cached - 0 means indefinitely
     */
    @Override
    public long   getRefreshTimeInterval()
    {
        return 0L;
    }
}
