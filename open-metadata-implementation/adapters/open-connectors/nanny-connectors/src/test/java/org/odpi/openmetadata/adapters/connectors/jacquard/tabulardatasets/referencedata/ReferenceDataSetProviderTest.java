/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.referencedata;

import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.controls.TabularDataSetConfigurationProperty;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;


/**
 * The product for a reference data set is defined when Jacquard finds the set, and again each time its
 * connector is created from the product's connection.  The connector only has the connection's configuration
 * properties to go on, so they must carry everything the definition is built from.  The canonical name was
 * left out, so the connector presented a table called null and deliveries to subscribers failed.
 */
public class ReferenceDataSetProviderTest
{
    /**
     * The configuration properties carry the values the connector rebuilds the product definition from.
     */
    @Test
    public void testConfigurationPropertiesRebuildDefinition()
    {
        ProductDefinition productDefinition = new ReferenceDataSetProvider().getProductDefinition("test-guid",
                                                                                                  "testIdentifier",
                                                                                                  "Test Identifier Reference Data Set",
                                                                                                  "Test description");

        assertEquals(productDefinition.getDataSpecTableName(), "Test Identifier Reference Data Set");

        Map<String, Object> configurationProperties = productDefinition.getConfigurationProperties();

        assertNotNull(configurationProperties);
        assertEquals(configurationProperties.get(TabularDataSetConfigurationProperty.STARTING_ELEMENT_GUID.name), "test-guid");
        assertEquals(configurationProperties.get(TabularDataSetConfigurationProperty.IDENTIFIER_PROPERTY_VALUE.name), "testIdentifier");
        assertEquals(configurationProperties.get(TabularDataSetConfigurationProperty.CANONICAL_NAME.name), "Test Identifier Reference Data Set");
        assertEquals(configurationProperties.get(TabularDataSetConfigurationProperty.PRODUCT_DESCRIPTION.name), "Test description");

        /*
         * The same definition comes back from the configuration properties alone.
         */
        ProductDefinition rebuiltDefinition = new ReferenceDataSetProvider().getProductDefinition((String) configurationProperties.get(TabularDataSetConfigurationProperty.STARTING_ELEMENT_GUID.name),
                                                                                                  (String) configurationProperties.get(TabularDataSetConfigurationProperty.IDENTIFIER_PROPERTY_VALUE.name),
                                                                                                  (String) configurationProperties.get(TabularDataSetConfigurationProperty.CANONICAL_NAME.name),
                                                                                                  (String) configurationProperties.get(TabularDataSetConfigurationProperty.PRODUCT_DESCRIPTION.name));

        assertEquals(rebuiltDefinition.getDataSpecTableName(), productDefinition.getDataSpecTableName());
        assertEquals(rebuiltDefinition.getQualifiedName(), productDefinition.getQualifiedName());
    }


    /**
     * A reference data set without a description has no description property, rather than a null one.
     */
    @Test
    public void testNullDescriptionIsLeftOut()
    {
        ProductDefinition productDefinition = new ReferenceDataSetProvider().getProductDefinition("test-guid",
                                                                                                  "testIdentifier",
                                                                                                  "Test Identifier Reference Data Set",
                                                                                                  null);

        assertFalse(productDefinition.getConfigurationProperties().containsKey(TabularDataSetConfigurationProperty.PRODUCT_DESCRIPTION.name));
    }
}
