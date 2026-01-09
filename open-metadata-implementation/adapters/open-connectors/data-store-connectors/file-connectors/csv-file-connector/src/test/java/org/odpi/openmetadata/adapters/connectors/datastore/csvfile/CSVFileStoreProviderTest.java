/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.csvfile;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Ensures CSVFileStoreProvider correctly initializes its superclass.
 */
public class CSVFileStoreProviderTest
{
    @Test public void testProviderInitialization()
    {
        CSVFileStoreProvider provider = new CSVFileStoreProvider();

        assertEquals(CSVFileStoreConnector.class.getName(), provider.getConnectorClassName());

        ConnectorType connectorTypeDetails = provider.getConnectorType();

        assertNotNull(connectorTypeDetails);

        assertEquals(CSVFileStoreProvider.class.getName(), connectorTypeDetails.getConnectorProviderClassName());
    }
}
