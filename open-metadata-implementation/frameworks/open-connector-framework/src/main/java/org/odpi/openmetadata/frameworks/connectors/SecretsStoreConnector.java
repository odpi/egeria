/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors;

/**
 * SecretsStoreConnector is the interface for a connector that is able to retrieve secrets (passwords, certificates, ...) from a secure location.
 * The secrets store connector is embedded in a connector that needs one or more secrets to perform its tasks.  Both connectors are initialised
 * together by the ConnectorBroker.  The secrets store connector is called by the surrounding connector to extract the needed secrets.
 *
 * When the ConnectorBroker detects that there is a secrets store connector embedded in another connector, it attempts to retrieve the standard
 * secrets for the corresponding connection object (if they are null):
 *
 * <ul>
 *     <li>userId</li>
 *     <li>clearPassword</li>
 *     <li>encryptedPassword</li>
 * </ul>
 *
 * This means that even if the outer connector is written to expect these secrets in its connection object, they do not need to be stored
 * in the connection object (ie in the configuration document or in the metadata store) but will be placed in the right fields by the
 * ConnectorBroker.
 *
 * If the name(s) of the secret(s) needed by the connector must be configured, they can be stored in the secureProperties.
 */
public abstract class SecretsStoreConnector extends ConnectorBase
{
    /**
     * Retrieve a secret from the secrets store.
     *
     * @param secretName name of the secret.
     * @return secret
     */
    abstract public String getSecret(String secretName);
}
