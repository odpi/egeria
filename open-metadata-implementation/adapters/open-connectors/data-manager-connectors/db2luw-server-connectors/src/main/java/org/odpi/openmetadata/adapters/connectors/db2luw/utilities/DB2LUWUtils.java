/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.db2luw.utilities;


/**
 * DB2LUWUtils provides simple functions to work with Db2 for Linux, UNIX and Windows database names and
 * connection strings.
 */
public class DB2LUWUtils
{
    /**
     * Convert the connection string for one database on a Db2 for Linux, UNIX and Windows server into a
     * connection string for a different database on the same server.  Db2's JDBC URLs identify the target
     * database through the final path segment (eg "jdbc:db2://host:port/databaseName"), so the existing
     * database name on the URL is replaced with the requested database's name rather than appended as a
     * connection property.
     *
     * @param serverURL connection string used to connect to one database on the Db2 for Linux, UNIX and Windows Server
     * @param databaseName name of the target database
     * @return connection string
     */
    public static String getDatabaseURL(String serverURL,
                                        String databaseName)
    {
        int lastSlashIndex = serverURL.lastIndexOf('/');

        String baseURL = serverURL;

        if (lastSlashIndex != -1)
        {
            baseURL = serverURL.substring(0, lastSlashIndex);
        }

        return baseURL + "/" + databaseName;
    }
}
