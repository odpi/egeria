/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors;


/**
 * The SecureConnectorExtension is an optional interface for a virtual connector.  A virtual connector is
 * a connector that uses embedded connectors to access its data resources.  The secure connector interface requests
 * that the connector is informed of any secrets store connectors that are found in the embedded connectors.
 */
public interface SecureConnectorExtension
{
    /**
     * Set up information about an embedded connector that this connector can use to support secure access to its resources.
     *
     * @param displayName name of the secrets store
     * @param secretsStoreConnector an embedded secrets store connector
     */
    void initializeSecretsStoreConnector(String                displayName,
                                         SecretsStoreConnector secretsStoreConnector);
}
