/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.secretsstore.envar;

import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;


/**
 * EnvVarSecretsStoreConnector retrieves secrets from environment variables.  Each secret is named for its environment variable.
 */
public class EnvVarSecretsStoreConnector extends SecretsStoreConnector
{
    /**
     * Request that the subclass refreshes its secrets.
     */
    @Override
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
