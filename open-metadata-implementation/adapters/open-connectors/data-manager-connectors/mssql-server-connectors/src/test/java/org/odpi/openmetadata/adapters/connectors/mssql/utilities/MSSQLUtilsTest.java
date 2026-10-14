/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.mssql.utilities;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


/**
 * Verify that MSSQLUtils builds a database-specific JDBC URL from the server's URL without losing the
 * server URL's other connection properties.
 */
public class MSSQLUtilsTest
{
    /**
     * A server URL with no databaseName property has one added.
     */
    @Test public void testDatabaseNameIsAdded()
    {
        assertEquals(MSSQLUtils.getDatabaseURL("jdbc:sqlserver://localhost:1433", "sales"),
                     "jdbc:sqlserver://localhost:1433;databaseName=sales");
        assertEquals(MSSQLUtils.getDatabaseURL("jdbc:sqlserver://localhost:1433;", "sales"),
                     "jdbc:sqlserver://localhost:1433;databaseName=sales");
    }


    /**
     * An existing databaseName property is replaced, and the properties after it are kept.
     */
    @Test public void testDatabaseNameIsReplacedAndOtherPropertiesKept()
    {
        assertEquals(MSSQLUtils.getDatabaseURL("jdbc:sqlserver://localhost:1433;databaseName=master", "sales"),
                     "jdbc:sqlserver://localhost:1433;databaseName=sales");
        assertEquals(MSSQLUtils.getDatabaseURL("jdbc:sqlserver://localhost:1433;DatabaseName=master;trustServerCertificate=true", "sales"),
                     "jdbc:sqlserver://localhost:1433;trustServerCertificate=true;databaseName=sales");
        assertEquals(MSSQLUtils.getDatabaseURL("jdbc:sqlserver://localhost:1433;encrypt=true;databaseName=master;trustServerCertificate=true", "sales"),
                     "jdbc:sqlserver://localhost:1433;encrypt=true;trustServerCertificate=true;databaseName=sales");
    }


    /**
     * A database name containing characters that are special in a connection string is enclosed in braces.
     */
    @Test public void testSpecialCharactersAreEscaped()
    {
        assertEquals(MSSQLUtils.getDatabaseURL("jdbc:sqlserver://localhost:1433", "My Sales"),
                     "jdbc:sqlserver://localhost:1433;databaseName=My Sales");
        assertEquals(MSSQLUtils.getDatabaseURL("jdbc:sqlserver://localhost:1433", "a;b"),
                     "jdbc:sqlserver://localhost:1433;databaseName={a;b}");
        assertEquals(MSSQLUtils.getDatabaseURL("jdbc:sqlserver://localhost:1433", "a}b"),
                     "jdbc:sqlserver://localhost:1433;databaseName={a}}b}");
    }
}
