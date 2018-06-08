/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.MockConnectorProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.AssertJUnit.assertTrue;


/**
 * Test Connection validates the setting up of different connection properties.
 */
public class TestConnection
{
    /**
     * Validates the setting up of various properties in a connection.
     */
    @Test public void testConnectionProperties()
    {
        ElementType connectorEType = new ElementType("TypeId",
                                                     "ConnectorType",
                                                     1,
                                                     "TypeDescription",
                                                     "URL",
                                                     ElementOrigin.CONFIGURATION,
                                                     "HomeId");
        ElementType          connectionEType = new ElementType("TypeId",
                                                               "Connection",
                                                               1,
                                                               "TypeDescription",
                                                               "URL",
                                                               ElementOrigin.CONFIGURATION,
                                                               "HomeId");
        Map<String, Object> addProperties = new HashMap<>();
        Map<String, Object> secProperties = new HashMap<>();
        addProperties.put("propertyOne", "Value1");
        addProperties.put("propertyTwo", "Value2");
        secProperties.put("SecPropertyOne", "Value1");

        AdditionalProperties additionalProperties = new AdditionalProperties(addProperties);
        AdditionalProperties securedProperties = new AdditionalProperties(secProperties);
        Meanings             meanings = new MockMeaningsIterator(null, 6, 10);
        ConnectorType        connectorType = new ConnectorType(connectorEType,
                                                               "ConTypeGUID",
                                                               "odpi.github.io/egeria",
                                                               null,
                                                               "Test.ConnectorType",
                                                               null,
                                                               null,
                                                               "TestCT",
                                                               null,
                                                               MockConnectorProvider.class.getName());
        Endpoint             endpoint = new Endpoint(null,
                                                     null,
                                                     null,
                                                     null,
                                                     null,
                                                     null,
                                                     null,
                                                     null,
                                                     null,
                                                     null,
                                                     null,
                                                     null);

        Connection testConnection = new Connection(connectionEType,
                                                   "guid",
                                                   "url",
                                                   null,
                                                   "qualifiedName",
                                                   additionalProperties,
                                                   meanings,
                                                   "displayName",
                                                   "description",
                                                   connectorType,
                                                   endpoint,
                                                   securedProperties);

        assertTrue(testConnection.toString() != null);
    }
}
