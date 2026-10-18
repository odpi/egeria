/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.modules;

import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * The egeriaOwned attribute decides whether the connector treats an Apache Atlas entity as its own.  It used to be
 * compared with ==, which only matched a Boolean value, because Boolean.toString returns the interned literal.  A
 * value that arrived as a String was never recognised as true.
 */
public class AtlasBooleanPropertyTest
{
    private static final String PROPERTY_NAME = "egeriaOwned";


    @Test
    public void testBooleanTrue()
    {
        assertTrue(AtlasIntegrationModuleBase.getAtlasBooleanProperty(getAttributes(Boolean.TRUE), PROPERTY_NAME));
    }


    @Test
    public void testStringTrue()
    {
        assertTrue(AtlasIntegrationModuleBase.getAtlasBooleanProperty(getAttributes(new String("true")), PROPERTY_NAME));
    }


    @Test
    public void testFalseValues()
    {
        assertFalse(AtlasIntegrationModuleBase.getAtlasBooleanProperty(getAttributes(Boolean.FALSE), PROPERTY_NAME));
        assertFalse(AtlasIntegrationModuleBase.getAtlasBooleanProperty(getAttributes("false"), PROPERTY_NAME));
        assertFalse(AtlasIntegrationModuleBase.getAtlasBooleanProperty(new HashMap<>(), PROPERTY_NAME));
        assertFalse(AtlasIntegrationModuleBase.getAtlasBooleanProperty(null, PROPERTY_NAME));
    }


    private static Map<String, Object> getAttributes(Object value)
    {
        Map<String, Object> attributes = new HashMap<>();

        attributes.put(PROPERTY_NAME, value);

        return attributes;
    }
}
