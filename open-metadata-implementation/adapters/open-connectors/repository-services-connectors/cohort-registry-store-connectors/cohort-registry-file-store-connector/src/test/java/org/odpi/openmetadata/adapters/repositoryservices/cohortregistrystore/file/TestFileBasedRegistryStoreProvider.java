/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.cohortregistrystore.file;

import org.odpi.openmetadata.frameworks.connectors.properties.ConnectorTypeDetails;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Ensures FileBasedRegistryStoreProvider correctly initializes its superclass.
 */
public class TestFileBasedRegistryStoreProvider
{
    @Test public void testProviderInitialization()
    {
        FileBasedRegistryStoreProvider  provider = new FileBasedRegistryStoreProvider();

        assertTrue(provider.getConnectorClassName().equals(FileBasedRegistryStoreConnector.class.getName()));

        ConnectorTypeDetails connectorTypeDetails = provider.getConnectorTypeProperties();

        assertTrue(connectorTypeDetails != null);

        assertTrue(connectorTypeDetails.getConnectorProviderClassName().equals(FileBasedRegistryStoreProvider.class.getName()));
    }
}
