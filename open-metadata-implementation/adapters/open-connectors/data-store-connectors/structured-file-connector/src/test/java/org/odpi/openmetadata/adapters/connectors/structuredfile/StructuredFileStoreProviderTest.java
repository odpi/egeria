/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.structuredfile;

import org.odpi.openmetadata.frameworks.connectors.properties.ConnectorTypeProperties;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Ensures StructuredFileStoreProvider correctly initializes its superclass.
 */
public class StructuredFileStoreProviderTest
{
    @Test public void testProviderInitialization()
    {
        StructuredFileStoreProvider  provider = new StructuredFileStoreProvider();

        assertTrue(provider.getConnectorClassName().equals(StructuredFileStoreConnector.class.getName()));

        ConnectorTypeProperties connectorTypeProperties = provider.getConnectorTypeProperties();

        assertTrue(connectorTypeProperties != null);

        assertTrue(connectorTypeProperties.getConnectorProviderClassName().equals(StructuredFileStoreProvider.class.getName()));
    }
}
