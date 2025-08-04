/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.datafolder;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Ensures DataFolderProvider correctly initializes its superclass.
 */
public class DataFolderProviderTest
{
    @Test public void testProviderInitialization()
    {
        DataFolderProvider provider = new DataFolderProvider();

        assertEquals(DataFolderConnector.class.getName(), provider.getConnectorClassName());

        ConnectorType connectorTypeDetails = provider.getConnectorType();

        assertNotNull(connectorTypeDetails);

        assertEquals(DataFolderProvider.class.getName(), connectorTypeDetails.getConnectorProviderClassName());
    }
}
