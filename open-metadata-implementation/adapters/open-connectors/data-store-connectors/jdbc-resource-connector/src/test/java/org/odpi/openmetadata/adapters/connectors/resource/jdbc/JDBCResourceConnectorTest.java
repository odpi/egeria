/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.resource.jdbc;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.controls.JDBCConfigurationProperty;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
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


    /**
     * Build a connector whose connection carries the supplied configuration properties.
     *
     * @param configurationProperties properties to place on the connection - may be null
     * @return initialized connector, ready for setConfigurationProperty()
     * @throws ConnectorCheckedException problem initializing the connector
     */
    private JDBCResourceConnector getConnectorWithProperties(Map<String, Object> configurationProperties) throws ConnectorCheckedException
    {
        Connection connectionBean = new Connection();

        connectionBean.setConfigurationProperties(configurationProperties);

        JDBCResourceConnector connector = new JDBCResourceConnector();

        connector.initialize("test-connector-instance", connectionBean);

        return connector;
    }


    /**
     * A value passed down by a hosting connector must replace the one already on the embedded connection, since the
     * connection's value is a template default and the host's configuration is where a deployment is tuned.
     */
    @Test
    public void testHostValueOverridesTheConnectionValue() throws ConnectorCheckedException
    {
        Map<String, Object> configurationProperties = new HashMap<>();
        configurationProperties.put(JDBCConfigurationProperty.JDBC_MAXIMUM_POOL_SIZE.getName(), "3");

        JDBCResourceConnector connector = getConnectorWithProperties(configurationProperties);

        connector.setConfigurationProperty(JDBCConfigurationProperty.JDBC_MAXIMUM_POOL_SIZE.getName(), "17");

        assertEquals(connector.getConnection().getConfigurationProperties().get(JDBCConfigurationProperty.JDBC_MAXIMUM_POOL_SIZE.getName()),
                     "17");
    }


    /**
     * A host that does not set the property passes null, which must leave the connection's own value alone.
     */
    @Test
    public void testNullFromHostLeavesTheConnectionValueAlone() throws ConnectorCheckedException
    {
        Map<String, Object> configurationProperties = new HashMap<>();
        configurationProperties.put(JDBCConfigurationProperty.JDBC_MAXIMUM_POOL_SIZE.getName(), "3");

        JDBCResourceConnector connector = getConnectorWithProperties(configurationProperties);

        connector.setConfigurationProperty(JDBCConfigurationProperty.JDBC_MAXIMUM_POOL_SIZE.getName(), null);

        assertEquals(connector.getConnection().getConfigurationProperties().get(JDBCConfigurationProperty.JDBC_MAXIMUM_POOL_SIZE.getName()),
                     "3");
    }


    /**
     * The property must still arrive when the connection carries no configuration properties at all.
     */
    @Test
    public void testHostValueAppliesWhenConnectionHasNoProperties() throws ConnectorCheckedException
    {
        JDBCResourceConnector connector = getConnectorWithProperties(null);

        connector.setConfigurationProperty(JDBCConfigurationProperty.JDBC_MAXIMUM_POOL_SIZE.getName(), "17");

        assertEquals(connector.getConnection().getConfigurationProperties().get(JDBCConfigurationProperty.JDBC_MAXIMUM_POOL_SIZE.getName()),
                     "17");
    }


    /**
     * Passing a value down must not disturb the other properties on the connection.
     */
    @Test
    public void testOtherConnectionPropertiesAreUntouched() throws ConnectorCheckedException
    {
        Map<String, Object> configurationProperties = new HashMap<>();
        configurationProperties.put(JDBCConfigurationProperty.DATABASE_NAME.getName(), "MyDatabase");
        configurationProperties.put(JDBCConfigurationProperty.JDBC_MAXIMUM_POOL_SIZE.getName(), "3");

        JDBCResourceConnector connector = getConnectorWithProperties(configurationProperties);

        connector.setConfigurationProperty(JDBCConfigurationProperty.JDBC_MAXIMUM_POOL_SIZE.getName(), "17");

        assertEquals(connector.getConnection().getConfigurationProperties().get(JDBCConfigurationProperty.DATABASE_NAME.getName()),
                     "MyDatabase");
        assertEquals(connector.getConnection().getConfigurationProperties().size(), 2);
    }
}
