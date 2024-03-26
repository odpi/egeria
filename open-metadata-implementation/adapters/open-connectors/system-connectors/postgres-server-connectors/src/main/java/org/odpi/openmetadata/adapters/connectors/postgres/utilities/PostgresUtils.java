/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.utilities;


import java.util.List;

/**
 * PostgresUtils provides simple functions to work with Postgres names and connection strings
 */
public class PostgresUtils
{
    /**
     * Determine whether a particular database should be included.
     *
     * @param databaseName name of the database
     * @param excludedDatabases configuration property to exclude databases
     * @param includedDatabases configuration property to restrict included databases
     * @return flag indicating whether to work with the database
     */
    public static boolean databaseShouldBeCatalogued(String       databaseName,
                                                     List<String> excludedDatabases,
                                                     List<String> includedDatabases)
    {
        return true;
    }


    /**
     * Convert the connection string for the database server into a connection string for the database.
     *
     * @param databaseServerURL connection string used to connect to the postgres database
     * @param databaseName name of the database
     * @return connection string
     */
    public static String getDatabaseURL(String databaseServerURL,
                                        String databaseName)
    {
        String[] splitURL = databaseServerURL.split("postgres");

        if (splitURL.length == 1)
        {
            return databaseServerURL + "/" + databaseName;
        }
        else
        {
            return splitURL[0] + "/" + databaseName;
        }
    }
}
