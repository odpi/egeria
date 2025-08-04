/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.cohortregistrystore.file;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Ensures FileBasedRegistryStoreProvider correctly initializes its superclass.
 */
public class TestFileBasedRegistryStoreProvider
{
    @Test public void testProviderInitialization()
    {
        FileBasedRegistryStoreProvider  provider = new FileBasedRegistryStoreProvider();

        assertEquals(FileBasedRegistryStoreConnector.class.getName(), provider.getConnectorClassName());

        ConnectorType connectorTypeDetails = provider.getConnectorType();

        assertNotNull(connectorTypeDetails);

        assertEquals(FileBasedRegistryStoreProvider.class.getName(), connectorTypeDetails.getConnectorProviderClassName());
    }
}
