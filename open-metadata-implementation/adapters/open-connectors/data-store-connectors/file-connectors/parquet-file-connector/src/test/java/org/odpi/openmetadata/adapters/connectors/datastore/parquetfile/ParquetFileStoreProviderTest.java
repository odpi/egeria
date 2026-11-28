/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.parquetfile;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Ensures ParquetFileStoreProvider correctly initializes its superclass.
 */
public class ParquetFileStoreProviderTest
{
    @Test public void testProviderInitialization()
    {
        ParquetFileStoreProvider provider = new ParquetFileStoreProvider();

        assertEquals(ParquetFileStoreConnector.class.getName(), provider.getConnectorClassName());

        ConnectorType connectorTypeDetails = provider.getConnectorType();

        assertNotNull(connectorTypeDetails);

        assertEquals(ParquetFileStoreProvider.class.getName(), connectorTypeDetails.getConnectorProviderClassName());
    }
}
