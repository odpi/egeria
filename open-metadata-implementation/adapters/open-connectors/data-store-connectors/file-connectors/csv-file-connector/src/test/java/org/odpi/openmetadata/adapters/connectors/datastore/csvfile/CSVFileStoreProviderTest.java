/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.csvfile;

import org.odpi.openmetadata.frameworks.connectors.properties.ConnectorTypeProperties;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Ensures CSVFileStoreProvider correctly initializes its superclass.
 */
public class CSVFileStoreProviderTest
{
    @Test public void testProviderInitialization()
    {
        CSVFileStoreProvider provider = new CSVFileStoreProvider();

        assertTrue(provider.getConnectorClassName().equals(CSVFileStoreConnector.class.getName()));

        ConnectorTypeProperties connectorTypeProperties = provider.getConnectorTypeProperties();

        assertTrue(connectorTypeProperties != null);

        assertTrue(connectorTypeProperties.getConnectorProviderClassName().equals(CSVFileStoreProvider.class.getName()));
    }
}
