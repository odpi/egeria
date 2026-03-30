/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.secretsstore.yaml;

import org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.ffdc.YAMLAuditCode;
import org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.ffdc.YAMLErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.users.*;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.io.IOException;
import java.util.HashMap;

/**
 * YAMLSecretsStoreConnector retrieves secrets from a YAML File
 */
public class YAMLSecretsFileConnector extends YAMLSecretsStoreConnector
{
    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     */
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        super.secretsCollectionName = null;

        checkSecretsStillValid();
    }


    /**
     * Retrieve the refresh time from the secrets store.
     *
     * @return how long the secrets can be cached - 0 means indefinitely
     */
    public SecretsStore getSecretsStore()
    {
        return secretsStore;
    }


    /**
     * Dynamically set the secrets collection name for the secrets store.
     *
     * @param secretsCollectionName name of the secrets collection
     */
    public void setSecretsCollectionName(String secretsCollectionName)
    {
        this.secretsCollectionName = secretsCollectionName;
    }



    /**
     * Save the requested secrets collection in the secrets store.
     *
     * @param collectionName           collectionName for the save
     * @param newSecretsCollection associated collection details
     * @throws ConnectorCheckedException a problem with the connector
     */
    public void saveSecretsCollection(String            collectionName,
                                      SecretsCollection newSecretsCollection) throws ConnectorCheckedException
    {
        final String methodName = "saveSecretsCollection";

        if (secretsStoreFile != null)
        {
            if (! secretsStoreFile.exists())
            {
                logRecord(methodName, YAMLAuditCode.NEW_SECRETS_STORE.getMessageDefinition(secretsStoreFile.getPath()));

                try
                {
                    secretsStoreFile.createNewFile();
                    secretsStore = new SecretsStore();
                    secretsStore.setSecretsCollections(new HashMap<>());
                }
                catch (IOException ioException)
                {
                    throw new ConnectorCheckedException(YAMLErrorCode.UNEXPECTED_IO_EXCEPTION.getMessageDefinition(secretsStoreFile.getPath(),
                                                                                                                   ioException.getMessage()),
                                                        this.getClass().getName(),
                                                        methodName,
                                                        ioException);
                }
            }
            else
            {
                super.checkSecretsStillValid();

            }

            logRecord(methodName, YAMLAuditCode.ADDING_CLIENT_SIDE_SECRET.getMessageDefinition(collectionName, secretsStoreFile.getPath()));
            secretsStore.getSecretsCollections().put(collectionName, new SecretsCollection(newSecretsCollection));
            saveSecrets();
        }
    }


    /**
     * Delete the requested secrets collection from the secrets store.
     *
     * @param collectionName name for the lookup
     * @throws ConnectorCheckedException a problem with the connector
     */
    public void deleteSecretsCollection(String collectionName) throws ConnectorCheckedException
    {
        final String methodName = "deleteSecretsCollection";

        if (secretsStore != null)
        {
            super.checkSecretsStillValid();

            logRecord(methodName, YAMLAuditCode.REMOVING_CLIENT_SIDE_SECRET.getMessageDefinition(collectionName, secretsStoreFile.getPath()));

            secretsStore.getSecretsCollections().remove(collectionName);

            saveSecrets();
        }
    }

}
