/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.mssql.tabulardatasource;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.mssql.MSSQLColumn;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.mssql.MSSQLForeignKey;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.mssql.MSSQLTable;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularColumnDescription;

import java.util.ArrayList;
import java.util.List;

/**
 * Convert the tabular data source description into something understandable by Microsoft SQL Server so the schema
 * for the table can be set up in the database.
 */
public class MSSQLTabularTable implements MSSQLTable
{
    private final String                         tableName;
    private final String                         tableDescription;
    private final List<TabularColumnDescription> columnDescriptions;


    /**
     * Construct the Microsoft SQL Server table information from the tabular data information.
     *
     * @param tableName name of the table
     * @param tableDescription description of the table
     * @param columnDescriptions list of columns
     */
    public MSSQLTabularTable(String                         tableName,
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
    public List<MSSQLColumn> getPrimaryKeys()
    {
        if (columnDescriptions != null)
        {
            List<MSSQLColumn> mssqlColumns = new ArrayList<>();

            for (TabularColumnDescription tabularColumnDescription : columnDescriptions)
            {
                if ((tabularColumnDescription != null) && (tabularColumnDescription.isIdentifier()))
                {
                    mssqlColumns.add(new MSSQLTabularColumn(tabularColumnDescription));
                }
            }

            if (! mssqlColumns.isEmpty())
            {
                return mssqlColumns;
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
    public List<MSSQLColumn> getDataColumns()
    {
        if (columnDescriptions != null)
        {
            List<MSSQLColumn> mssqlColumns = new ArrayList<>();

            for (TabularColumnDescription tabularColumnDescription : columnDescriptions)
            {
                if ((tabularColumnDescription != null) && (! tabularColumnDescription.isIdentifier()))
                {
                    mssqlColumns.add(new MSSQLTabularColumn(tabularColumnDescription));
                }
            }

            if (! mssqlColumns.isEmpty())
            {
                return mssqlColumns;
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
    public List<MSSQLColumn> getNewColumns()
    {
        return null;
    }


    /**
     * Return the list of foreign keys for this table.
     *
     * @return list
     */
    @Override
    public List<MSSQLForeignKey> getForeignKeys()
    {
        return null;
    }
}
