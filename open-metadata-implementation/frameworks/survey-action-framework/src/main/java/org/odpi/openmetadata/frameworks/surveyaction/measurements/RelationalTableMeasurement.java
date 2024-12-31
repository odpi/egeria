/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.surveyaction.measurements;

import java.util.Objects;

public class RelationalTableMeasurement
{
    private String tableName          = null;
    private String tableQualifiedName = null;
    private long   tableSize          = 0L;
    private String tableType          = null;
    private long   columnCount        = 0L;


    public RelationalTableMeasurement()
    {
    }


    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getTableQualifiedName()
    {
        return tableQualifiedName;
    }

    public void setTableQualifiedName(String tableQualifiedName)
    {
        this.tableQualifiedName = tableQualifiedName;
    }

    public long getTableSize()
    {
        return tableSize;
    }

    public void setTableSize(long tableSize)
    {
        this.tableSize = tableSize;
    }

    public String getTableType()
    {
        return tableType;
    }

    public void setTableType(String tableType)
    {
        this.tableType = tableType;
    }

    public long getColumnCount()
    {
        return columnCount;
    }

    public void setColumnCount(long columnCount)
    {
        this.columnCount = columnCount;
    }


    @Override
    public String toString()
    {
        return "RelationalTableMeasurement{" +
                "tableName='" + tableName + '\'' +
                ", tableQualifiedName='" + tableQualifiedName + '\'' +
                ", tableSize=" + tableSize +
                ", tableType='" + tableType + '\'' +
                ", columnCount=" + columnCount +
                '}';
    }


    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        RelationalTableMeasurement that = (RelationalTableMeasurement) objectToCompare;
        return tableSize == that.tableSize && columnCount == that.columnCount && Objects.equals(tableName, that.tableName) && Objects.equals(tableQualifiedName, that.tableQualifiedName) && Objects.equals(tableType, that.tableType);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(tableName, tableQualifiedName, tableSize, tableType, columnCount);
    }
}
