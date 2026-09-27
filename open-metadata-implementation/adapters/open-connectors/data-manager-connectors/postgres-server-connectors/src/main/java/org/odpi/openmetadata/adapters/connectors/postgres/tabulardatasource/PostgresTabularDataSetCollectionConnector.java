/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.tabulardatasource;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataCollection;

import java.util.ArrayList;
import java.util.List;


/**
 * PostgresTabularDataSetCollectionConnector presents the tables of one PostgreSQL schema as a collection of simple
 * tables of data.  One table is in focus at a time - it is chosen with {@link #setTableName} - and is read and
 * written exactly as {@link PostgresTabularDataSetConnector} reads and writes its single table.  As a source, the
 * collection lists its tables with {@link #getTableNames} so that each can be brought into focus and read in turn;
 * as a destination, a table is created in the schema the first time data is written to it.
 */
public class PostgresTabularDataSetCollectionConnector extends PostgresTabularDataSetConnector implements ReadableTabularDataCollection
{
    /**
     * Set up the table name for the tabular data set to focus on.  This is in canonical word format where each word in the name
     * should be capitalized, with spaces between the words.
     * This format allows easy translation between different naming conventions.
     *
     * @param tableName name of the table
     * @param tableDescription optional description for the table - useful if the connector needs to set up a
     *                         definition of the table.
     */
    @Override
    public void setTableName(String tableName,
                             String tableDescription)
    {
        super.focusOnTable(super.fromCanonicalToSnakeCase(tableName), tableDescription);
    }


    /**
     * Return the names of the tables in the schema, in canonical form so that they can be passed back to
     * {@link #setTableName} and translated by a destination into its own naming convention.
     *
     * @return list of table names (may be empty)
     * @throws ConnectorCheckedException problem accessing the database
     */
    @Override
    public List<String> getTableNames() throws ConnectorCheckedException
    {
        List<String> tableNames = new ArrayList<>();

        for (String tableName : super.getSchemaTableNames())
        {
            tableNames.add(super.fromSnakeToCanonicalCase(tableName));
        }

        return tableNames;
    }
}
