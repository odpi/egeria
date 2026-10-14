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
     * so any existing databaseName property on the server URL is replaced.  The other connection properties
     * (such as encrypt or trustServerCertificate) are kept, wherever they appear in the URL.
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
            /*
             * Remove just the databaseName property: the text up to the next property separator.
             */
            int propertyEnd = baseURL.indexOf(';', propertyIndex + 1);

            if (propertyEnd == -1)
            {
                baseURL = baseURL.substring(0, propertyIndex);
            }
            else
            {
                baseURL = baseURL.substring(0, propertyIndex) + baseURL.substring(propertyEnd);
            }
        }

        if (baseURL.endsWith(";"))
        {
            baseURL = baseURL.substring(0, baseURL.length() - 1);
        }

        return baseURL + DATABASE_NAME_PROPERTY + getPropertyValue(databaseName);
    }


    /**
     * Return a value that can be placed in a Microsoft SQL Server JDBC connection property.  A value containing
     * a property separator (;), a brace or surrounding spaces must be enclosed in braces, with any closing
     * brace doubled.
     *
     * @param value raw value
     * @return value ready for the connection string
     */
    private static String getPropertyValue(String value)
    {
        if ((value.contains(";")) || (value.contains("{")) || (value.contains("}")) || (! value.equals(value.trim())))
        {
            return "{" + value.replace("}", "}}") + "}";
        }

        return value;
    }
}
