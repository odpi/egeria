/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.tabulardatasource;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLColumn;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.ColumnType;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularColumnDescription;

public class PostgresTabularColumn implements PostgreSQLColumn
{
    private final TabularColumnDescription tabularColumnDescription;

    public PostgresTabularColumn(TabularColumnDescription tabularColumnDescription)
    {
        this.tabularColumnDescription = tabularColumnDescription;
    }


    /**
     * Retrieve the name of the column.
     *
     * @return name
     */
    @Override
    public String getColumnName()
    {
        return tabularColumnDescription.columnName();
    }


    /**
     * Return the type of the column.
     *
     * @return ColumnType
     */
    @Override
    public ColumnType getColumnType()
    {
        switch (tabularColumnDescription.columnDataType())
        {
            case LONG ->
            {
                return ColumnType.LONG;
            }
            case INT ->
            {
                return ColumnType.INT;
            }
            case DATE ->
            {
                return ColumnType.DATE;
            }
            default ->
            {
                return ColumnType.STRING;
            }
        }
    }


    /**
     * Return th optional description for the column.
     *
     * @return text
     */
    @Override
    public String getColumnDescription()
    {
        return tabularColumnDescription.description();
    }

    /**
     * Return whether the column is not null;
     *
     * @return boolean
     */
    @Override
    public boolean isNotNull()
    {
        return ! tabularColumnDescription.isNullable();
    }
}
