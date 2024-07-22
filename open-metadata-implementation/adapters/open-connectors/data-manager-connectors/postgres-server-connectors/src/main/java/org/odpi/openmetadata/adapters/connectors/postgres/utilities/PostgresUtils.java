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
