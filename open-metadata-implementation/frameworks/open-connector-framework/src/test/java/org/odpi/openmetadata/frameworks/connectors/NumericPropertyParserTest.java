/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors;

import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFErrorCode;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFRuntimeException;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * Numeric configuration properties and request parameters accept any representation of a whole number in range,
 * and report anything else with the property's name rather than a bare NumberFormatException.
 */
public class NumericPropertyParserTest
{
    private static final String PROPERTY_NAME = "maxRetries";


    @Test
    public void testIntRepresentations()
    {
        assertEquals(NumericPropertyParser.getIntConfigurationProperty(PROPERTY_NAME, 30), 30);
        assertEquals(NumericPropertyParser.getIntConfigurationProperty(PROPERTY_NAME, 30L), 30);
        assertEquals(NumericPropertyParser.getIntConfigurationProperty(PROPERTY_NAME, 30.0), 30);
        assertEquals(NumericPropertyParser.getIntConfigurationProperty(PROPERTY_NAME, new BigDecimal("30")), 30);
        assertEquals(NumericPropertyParser.getIntConfigurationProperty(PROPERTY_NAME, "30"), 30);
        assertEquals(NumericPropertyParser.getIntConfigurationProperty(PROPERTY_NAME, " 30 "), 30);
        assertEquals(NumericPropertyParser.getIntConfigurationProperty(PROPERTY_NAME, "-5"), -5);
        assertEquals(NumericPropertyParser.getIntRequestParameter(PROPERTY_NAME, "30.0"), 30);
    }


    @Test
    public void testLongRepresentations()
    {
        assertEquals(NumericPropertyParser.getLongConfigurationProperty(PROPERTY_NAME, 1.0E10), 10000000000L);
        assertEquals(NumericPropertyParser.getLongConfigurationProperty(PROPERTY_NAME, "10000000000"), 10000000000L);
        assertEquals(NumericPropertyParser.getLongRequestParameter(PROPERTY_NAME, " 7 "), 7L);
    }


    @Test
    public void testInvalidValues()
    {
        assertInvalid(() -> NumericPropertyParser.getIntConfigurationProperty(PROPERTY_NAME, "abc"), "abc");
        assertInvalid(() -> NumericPropertyParser.getIntConfigurationProperty(PROPERTY_NAME, 2.5), "2.5");
        assertInvalid(() -> NumericPropertyParser.getIntConfigurationProperty(PROPERTY_NAME, Double.NaN), "NaN");
        assertInvalid(() -> NumericPropertyParser.getIntConfigurationProperty(PROPERTY_NAME, 10000000000L), "10000000000");
        assertInvalid(() -> NumericPropertyParser.getIntRequestParameter(PROPERTY_NAME, ""), "");
        assertInvalid(() -> NumericPropertyParser.getLongRequestParameter(PROPERTY_NAME, "1 000"), "1 000");
    }


    @Test
    public void testConnectorBaseUsesParser()
    {
        TestConnector connector = new TestConnector();

        assertEquals(connector.getIntConfigurationProperty(PROPERTY_NAME, Map.of(PROPERTY_NAME, 30.0)), 30);
        assertEquals(connector.getLongConfigurationProperty(PROPERTY_NAME, Map.of(PROPERTY_NAME, " 30")), 30L);
        assertEquals(connector.getIntConfigurationProperty(PROPERTY_NAME, Map.of()), 0);
        assertInvalid(() -> connector.getIntConfigurationProperty(PROPERTY_NAME, Map.of(PROPERTY_NAME, "thirty")), "thirty");
    }


    private static void assertInvalid(Runnable parse, String value)
    {
        try
        {
            parse.run();
            fail("No exception for " + value);
        }
        catch (OMFRuntimeException error)
        {
            assertEquals(error.getReportedErrorMessageId(), OCFErrorCode.INVALID_NUMERIC_PROPERTY.getMessageDefinition().getMessageId());
            assertTrue(error.getMessage().contains(PROPERTY_NAME), error.getMessage());
            assertTrue(error.getMessage().contains(value), error.getMessage());
        }
    }


    /**
     * Exposes the protected helpers.
     */
    private static class TestConnector extends ConnectorBase
    {
    }
}
