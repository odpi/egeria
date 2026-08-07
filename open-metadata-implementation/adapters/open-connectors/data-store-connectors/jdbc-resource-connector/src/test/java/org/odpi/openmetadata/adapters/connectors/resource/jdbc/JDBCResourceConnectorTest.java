/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.resource.jdbc;

import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Verify how the additionalConnectionProperties configuration property is copied into the Properties object
 * passed to DriverManager.getConnection().
 */
public class JDBCResourceConnectorTest
{
    @Test
    public void testValidMapIsCopied()
    {
        Map<String, Object> additionalConnectionProperties = new HashMap<>();
        additionalConnectionProperties.put("remarksReporting", "true");
        additionalConnectionProperties.put("oracle.jdbc.timezoneAsRegion", "false");

        Properties target = new Properties();

        JDBCConnectionPropertiesUtil.addAdditionalConnectionProperties(additionalConnectionProperties, target);

        assertEquals(target.getProperty("remarksReporting"), "true");
        assertEquals(target.getProperty("oracle.jdbc.timezoneAsRegion"), "false");
        assertEquals(target.size(), 2);
    }


    @Test
    public void testNonMapValueIsIgnored()
    {
        Properties target = new Properties();

        JDBCConnectionPropertiesUtil.addAdditionalConnectionProperties("not-a-map", target);
        JDBCConnectionPropertiesUtil.addAdditionalConnectionProperties(null, target);

        assertTrue(target.isEmpty());
    }


    @Test
    public void testEmptyMapLeavesTargetUnchanged()
    {
        Properties target = new Properties();

        JDBCConnectionPropertiesUtil.addAdditionalConnectionProperties(new HashMap<>(), target);

        assertTrue(target.isEmpty());
    }


    @Test
    public void testNullKeysAndValuesAreSkipped()
    {
        Map<String, Object> additionalConnectionProperties = new HashMap<>();
        additionalConnectionProperties.put("validKey", "validValue");
        additionalConnectionProperties.put(null, "valueWithNullKey");
        additionalConnectionProperties.put("keyWithNullValue", null);

        Properties target = new Properties();

        JDBCConnectionPropertiesUtil.addAdditionalConnectionProperties(additionalConnectionProperties, target);

        assertEquals(target.size(), 1);
        assertEquals(target.getProperty("validKey"), "validValue");
    }


    @Test
    public void testNonStringMapValuesAreConvertedToString()
    {
        Map<Object, Object> additionalConnectionProperties = new HashMap<>();
        additionalConnectionProperties.put("loginTimeout", 30);
        additionalConnectionProperties.put(42, "numericKey");

        Properties target = new Properties();

        JDBCConnectionPropertiesUtil.addAdditionalConnectionProperties(additionalConnectionProperties, target);

        assertEquals(target.getProperty("loginTimeout"), "30");
        assertEquals(target.getProperty("42"), "numericKey");
    }


    @Test
    public void testExistingTargetPropertiesArePreserved()
    {
        Map<String, Object> additionalConnectionProperties = new HashMap<>();
        additionalConnectionProperties.put("remarksReporting", "true");

        Properties target = new Properties();
        target.setProperty("user", "scott");

        JDBCConnectionPropertiesUtil.addAdditionalConnectionProperties(additionalConnectionProperties, target);

        assertEquals(target.getProperty("user"), "scott");
        assertEquals(target.getProperty("remarksReporting"), "true");
        assertEquals(target.size(), 2);
    }
}
