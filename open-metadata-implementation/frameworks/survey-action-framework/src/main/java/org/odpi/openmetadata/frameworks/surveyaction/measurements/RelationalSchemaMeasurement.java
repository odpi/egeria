/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.surveyaction.measurements;


import java.util.Objects;

/**
 * Captures the measurement counts for a relational schema.
 */
public class RelationalSchemaMeasurement
{
    private String schemaName = null;
    private long   totalTableSize = 0L;
    private long   tableCount     = 0L;
    private long   viewCount      = 0L;
    private long   columnCount    = 0L;
    private long   materializedViewCount = 0L;

    public RelationalSchemaMeasurement()
    {
    }

    public String getSchemaName()
    {
        return schemaName;
    }

    public void setSchemaName(String schemaName)
    {
        this.schemaName = schemaName;
    }

    public long getTotalTableSize()
    {
        return totalTableSize;
    }

    public void setTotalTableSize(long totalTableSize)
    {
        this.totalTableSize = totalTableSize;
    }

    public long getTableCount()
    {
        return tableCount;
    }

    public void setTableCount(long tableCount)
    {
        this.tableCount = tableCount;
    }

    public long getViewCount()
    {
        return viewCount;
    }

    public void setViewCount(long viewCount)
    {
        this.viewCount = viewCount;
    }

    public long getColumnCount()
    {
        return columnCount;
    }

    public void setColumnCount(long columnCount)
    {
        this.columnCount = columnCount;
    }

    public long getMaterializedViewCount()
    {
        return materializedViewCount;
    }

    public void setMaterializedViewCount(long materializedViewCount)
    {
        this.materializedViewCount = materializedViewCount;
    }

    @Override
    public String toString()
    {
        return "RelationalSchemaMeasurement{" +
                "schemaName='" + schemaName + '\'' +
                ", totalTableSize=" + totalTableSize +
                ", tableCount=" + tableCount +
                ", viewCount=" + viewCount +
                ", columnCount=" + columnCount +
                ", materializedViewCount=" + materializedViewCount +
                '}';
    }


    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        RelationalSchemaMeasurement that = (RelationalSchemaMeasurement) objectToCompare;
        return totalTableSize == that.totalTableSize && tableCount == that.tableCount && viewCount == that.viewCount && columnCount == that.columnCount && materializedViewCount == that.materializedViewCount && Objects.equals(schemaName, that.schemaName);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(schemaName, totalTableSize, tableCount, viewCount, columnCount, materializedViewCount);
    }
}
