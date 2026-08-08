/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.db2luw.tabulardatasource;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.db2luw.DB2LUWColumn;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.db2luw.DB2LUWForeignKey;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.db2luw.DB2LUWTable;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularColumnDescription;

import java.util.ArrayList;
import java.util.List;

/**
 * Convert the tabular data source description into something understandable by Db2 for Linux, UNIX and Windows
 * so the schema for the table can be set up in the database.
 */
public class DB2LUWTabularTable implements DB2LUWTable
{
    private final String                         tableName;
    private final String                         tableDescription;
    private final List<TabularColumnDescription> columnDescriptions;


    /**
     * Construct the Db2 for Linux, UNIX and Windows table information from the tabular data information.
     *
     * @param tableName name of the table
     * @param tableDescription description of the table
     * @param columnDescriptions list of columns
     */
    public DB2LUWTabularTable(String                         tableName,
                              String                         tableDescription,
                              List<TabularColumnDescription> columnDescriptions)
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
    public List<DB2LUWColumn> getPrimaryKeys()
    {
        if (columnDescriptions != null)
        {
            List<DB2LUWColumn> db2luwColumns = new ArrayList<>();

            for (TabularColumnDescription tabularColumnDescription : columnDescriptions)
            {
                if ((tabularColumnDescription != null) && (tabularColumnDescription.isIdentifier()))
                {
                    db2luwColumns.add(new DB2LUWTabularColumn(tabularColumnDescription));
                }
            }

            if (! db2luwColumns.isEmpty())
            {
                return db2luwColumns;
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
    public List<DB2LUWColumn> getDataColumns()
    {
        if (columnDescriptions != null)
        {
            List<DB2LUWColumn> db2luwColumns = new ArrayList<>();

            for (TabularColumnDescription tabularColumnDescription : columnDescriptions)
            {
                if ((tabularColumnDescription != null) && (! tabularColumnDescription.isIdentifier()))
                {
                    db2luwColumns.add(new DB2LUWTabularColumn(tabularColumnDescription));
                }
            }

            if (! db2luwColumns.isEmpty())
            {
                return db2luwColumns;
            }
        }

        return null;
    }


    /**
     * Return the columns that are added as an extension using ALTER TABLE.
     *
     * @return list of columns
     */
    @Override
    public List<DB2LUWColumn> getNewColumns()
    {
        return null;
    }


    /**
     * Return the list of foreign keys for this table.
     *
     * @return list
     */
    @Override
    public List<DB2LUWForeignKey> getForeignKeys()
    {
        return null;
    }
}
