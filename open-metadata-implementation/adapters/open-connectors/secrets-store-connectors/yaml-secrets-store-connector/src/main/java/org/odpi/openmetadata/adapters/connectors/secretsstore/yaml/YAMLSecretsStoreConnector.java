/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.secretsstore.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.ffdc.YAMLAuditCode;
import org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.secretsstore.SecretsCollection;
import org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.secretsstore.SecretsStore;
import org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.secretsstore.TokenAPI;
import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStoreCollectionProperty;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;

import java.io.File;

/**
 * YAMLSecretsStoreConnector retrieves secrets from environment variables.  Each secret is named for its environment variable.
 */
public class YAMLSecretsStoreConnector extends SecretsStoreConnector
{
    private static final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

    private File         secretsStoreFile = null;
    private SecretsStore secretsStore     = null;

    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        secretsStoreFile = new File(connectionProperties.getEndpoint().getAddress());
        refreshSecrets();
    }


    /**
     * Retrieve the refresh time from the secrets store.
     *
     * @return how long the secrets can be cached - 0 means indefinitely
     * @throws ConnectorCheckedException there is a problem with the connector
     */
    public long   getRefreshTimeInterval() throws ConnectorCheckedException
    {
        super.checkSecretsStillValid();

        if (secretsStore != null)
        {
            SecretsCollection secretsCollection = secretsStore.getSecretsCollections().get(secretsCollectionName);

            if (secretsCollection != null)
            {
                return secretsCollection.getRefreshTimeInterval();
            }
        }

        return 0L;
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
        super.checkSecretsStillValid();

        if (secretsStore != null)
        {
            SecretsCollection secretsCollection = secretsStore.getSecretsCollections().get(secretsCollectionName);

            if (secretsCollection != null)
            {
                /*
                 * Return the secret if found in the collection
                 */
                if (secretsCollection.getSecrets().get(secretName) != null)
                {
                    return secretsCollection.getSecrets().get(secretName);
                }

                /*
                 * A token is requested - is there a token API to use?
                 */
                if (SecretsStoreCollectionProperty.TOKEN.getName().equals(secretName))
                {
                    if (secretsCollection.getTokenAPI() != null)
                    {
                        /*
                         * It is possible to request the token through the API
                         */
                        String token = this.getToken(secretsCollection.getTokenAPI());

                        if (token != null)
                        {
                            /*
                             * Save the token for next call.  It will be removed when the secrets store refreshes.
                             */
                            secretsCollection.getSecrets().put(SecretsStoreCollectionProperty.TOKEN.getName(), token);
                        }
                    }
                }
            }
        }

        /*
         * Unable to resolve secret
         */
        return null;
    }



    /**
     * Request a new token from the token API.
     *
     * @param tokenAPI request specification
     * @return new token or null
     */
    private String getToken(TokenAPI tokenAPI)
    {
        return "token value";
    }

    /**
     * Request that the subclass refreshes its secrets.
     */
    protected void refreshSecrets()
    {
        final String methodName = "refreshSecrets";

        try
        {
            secretsStore = objectMapper.readValue(secretsStoreFile, SecretsStore.class);
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    YAMLAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                            methodName,
                                                                                            error.getMessage()));
            }
        }
    }
}
