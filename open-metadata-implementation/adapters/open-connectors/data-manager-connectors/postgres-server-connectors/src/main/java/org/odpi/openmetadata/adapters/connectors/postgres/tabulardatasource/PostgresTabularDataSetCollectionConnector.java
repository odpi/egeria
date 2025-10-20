/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.tabulardatasource;

import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularDataCollection;


/**
 * PostgresTabularDataSourceConnector works with structured files to retrieve simple tables of data.
 */
public class PostgresTabularDataSetCollectionConnector extends PostgresTabularDataSetConnector implements TabularDataCollection
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
        super.tableName = super.fromCanonicalToSnakeCase(tableName);
        super.tableDescription = tableDescription;
    }
}