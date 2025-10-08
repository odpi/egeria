/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.tabulardatasource;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLColumn;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLForeignKey;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLTable;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularColumnDescription;

import java.util.ArrayList;
import java.util.List;

/**
 * Convert the tabular data source description into something understandable by PostgreSQL so the schema
 * for the table can be set up in the database.
 */
public class PostgresTabularTable implements PostgreSQLTable
{
    private final String                         tableName;
    private final String                         tableDescription;
    private final List<TabularColumnDescription> columnDescriptions;


    /**
     * Construct the postgreSQL table information from the tabular data information.
     *
     * @param tableName name of the table
     * @param tableDescription description of the table
     * @param columnDescriptions list of columns
     */
    public PostgresTabularTable(String tableName, String tableDescription, List<TabularColumnDescription> columnDescriptions)
    {
        this.tableName          = tableName;
        this.tableDescription   = tableDescription;
        this.columnDescriptions = columnDescriptions;
    }


    /**
     * Return the name of the table.
     *
     * @return name
     */
    @Override
    public String getTableName()
    {
        return tableName;
    }


    /**
     * Return the name of the table.
     *
     * @param schemaName name of schema
     * @return name
     */
    @Override
    public String getTableName(String schemaName)
    {
        return schemaName + "." + tableName;
    }


    /**
     * Return the description of the table.
     *
     * @return text
     */
    @Override
    public String getTableDescription()
    {
        return tableDescription;
    }

    /**
     * Return the columns that are primary keys.
     *
     * @return list of columns
     */
    @Override
    public List<PostgreSQLColumn> getPrimaryKeys()
    {
        if (columnDescriptions != null)
        {
            List<PostgreSQLColumn> postgreSQLColumns = new ArrayList<>();

            for (TabularColumnDescription tabularColumnDescription : columnDescriptions)
            {
                if ((tabularColumnDescription != null) && (tabularColumnDescription.isIdentifier()))
                {
                    postgreSQLColumns.add(new PostgresTabularColumn(tabularColumnDescription));
                }
            }

            if (! postgreSQLColumns.isEmpty())
            {
                return postgreSQLColumns;
            }
        }

        return null;
    }


    /**
     * Return the columns that are not primary keys.
     *
     * @return list of columns
     */
    @Override
    public List<PostgreSQLColumn> getDataColumns()
    {
        if (columnDescriptions != null)
        {
            List<PostgreSQLColumn> postgreSQLColumns = new ArrayList<>();

            for (TabularColumnDescription tabularColumnDescription : columnDescriptions)
            {
                if ((tabularColumnDescription != null) && (! tabularColumnDescription.isIdentifier()))
                {
                    postgreSQLColumns.add(new PostgresTabularColumn(tabularColumnDescription));
                }
            }

            if (! postgreSQLColumns.isEmpty())
            {
                return postgreSQLColumns;
            }
        }

        return null;
    }


    /**
     * Return the list of foreign keys for this table.
     *
     * @return list
     */
    @Override
    public List<PostgreSQLForeignKey> getForeignKeys()
    {
        return null;
    }
}
