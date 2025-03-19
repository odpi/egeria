/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.datafolder;

import org.odpi.openmetadata.frameworks.connectors.properties.ConnectorTypeDetails;
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

        ConnectorTypeDetails connectorTypeDetails = provider.getConnectorTypeProperties();

        assertTrue(connectorTypeDetails != null);

        assertTrue(connectorTypeDetails.getConnectorProviderClassName().equals(DataFolderProvider.class.getName()));
    }
}
