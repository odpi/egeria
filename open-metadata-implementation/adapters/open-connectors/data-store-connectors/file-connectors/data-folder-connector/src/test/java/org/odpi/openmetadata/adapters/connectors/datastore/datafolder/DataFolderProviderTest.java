/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.datafolder;

import org.odpi.openmetadata.frameworks.connectors.properties.ConnectorTypeProperties;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Ensures DataFolderProvider correctly initializes its superclass.
 */
public class DataFolderProviderTest
{
    @Test public void testProviderInitialization()
    {
        DataFolderProvider provider = new DataFolderProvider();

        assertTrue(provider.getConnectorClassName().equals(DataFolderConnector.class.getName()));

        ConnectorTypeProperties connectorTypeProperties = provider.getConnectorTypeProperties();

        assertTrue(connectorTypeProperties != null);

        assertTrue(connectorTypeProperties.getConnectorProviderClassName().equals(DataFolderProvider.class.getName()));
    }
}
