/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.secretsstore.yaml;

import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.users.SecretsCollection;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

/**
 * Saving and deleting whole secrets collections is part of SecretsStoreConnector, so a caller no longer needs to
 * know which connector class can do it.  Only a connector that says it supports the updates may be asked to make
 * them: the others reject the request with OCF-CONNECTOR-400-012 and change nothing.
 */
public class SecretsCollectionUpdateTest
{
    /**
     * The YAML secrets file connector supports updates.
     */
    @Test
    public void testYAMLSecretsFileConnectorSupportsUpdates()
    {
        SecretsStoreConnector connector = new YAMLSecretsFileConnector();

        assertTrue(connector.isSecretsCollectionUpdateSupported());
    }


    /**
     * The read-only YAML secrets store connector inherits the base class's refusal.
     */
    @Test
    public void testReadOnlyConnectorRejectsUpdates()
    {
        SecretsStoreConnector connector = new YAMLSecretsStoreConnector();

        assertFalse(connector.isSecretsCollectionUpdateSupported());

        ConnectorCheckedException saveError = expectThrows(ConnectorCheckedException.class,
                                                           () -> connector.saveSecretsCollection("testCollection", new SecretsCollection()));

        assertTrue(saveError.getMessage().contains("OCF-CONNECTOR-400-012"), saveError.getMessage());
        assertTrue(saveError.getMessage().contains("saveSecretsCollection"), saveError.getMessage());
        assertTrue(saveError.getMessage().contains("testCollection"), saveError.getMessage());

        ConnectorCheckedException deleteError = expectThrows(ConnectorCheckedException.class,
                                                             () -> connector.deleteSecretsCollection("testCollection"));

        assertTrue(deleteError.getMessage().contains("OCF-CONNECTOR-400-012"), deleteError.getMessage());
        assertTrue(deleteError.getMessage().contains("deleteSecretsCollection"), deleteError.getMessage());
    }
}
