/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.mssql.utilities;


/**
 * MSSQLUtils provides simple functions to work with Microsoft SQL Server names and connection strings
 */
public class MSSQLUtils
{
    private static final String DATABASE_NAME_PROPERTY = ";databaseName=";


    /**
     * Convert the connection string for the database server into a connection string for the database.
     * Microsoft SQL Server JDBC URLs identify the target database through a "databaseName" connection
     * property (eg "jdbc:sqlserver://host:port;databaseName=myDatabase") rather than a URL path segment,
     * so any existing databaseName property on the server URL is replaced rather than split off.
     *
     * @param databaseServerURL connection string used to connect to the Microsoft SQL Server
     * @param databaseName name of the database
     * @return connection string
     */
    public static String getDatabaseURL(String databaseServerURL,
                                        String databaseName)
    {
        String baseURL = databaseServerURL;

        int propertyIndex = baseURL.toLowerCase().indexOf(DATABASE_NAME_PROPERTY.toLowerCase());

        if (propertyIndex != -1)
        {
            baseURL = baseURL.substring(0, propertyIndex);
        }

        return baseURL + DATABASE_NAME_PROPERTY + databaseName;
    }
}
