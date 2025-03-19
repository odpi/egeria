/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.surveyaction.measurements;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Captures the measurement counts for a relational schema.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelationalSchemaMeasurement
{
    private String qualifiedSchemaName = null;
    private String schemaName = null;
    private long   totalTableSize = 0L;
    private long   tableCount     = 0L;
    private long   viewCount      = 0L;
    private long   columnCount    = 0L;
    private long   materializedViewCount = 0L;

    public RelationalSchemaMeasurement()
    {
    }

    public String getQualifiedSchemaName()
    {
        return qualifiedSchemaName;
    }

    public void setQualifiedSchemaName(String qualifiedSchemaName)
    {
        this.qualifiedSchemaName = qualifiedSchemaName;
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

    /**
     * Return the number of tables discovered.
     *
     * @return long
     */
    public long getTableCount()
    {
        return tableCount;
    }


    /**
     * Set up the number of tables discovered.
     *
     * @param tableCount long
     */
    public void setTableCount(long tableCount)
    {
        this.tableCount = tableCount;
    }


    /**
     * Return the number of views discovered.
     *
     * @return long
     */
    public long getViewCount()
    {
        return viewCount;
    }


    /**
     * Set up the number of views discovered.
     *
     * @param viewCount long
     */
    public void setViewCount(long viewCount)
    {
        this.viewCount = viewCount;
    }


    /**
     * Return the number of materialized views discovered.
     *
     * @return long
     */
    public long getMaterializedViewCount()
    {
        return materializedViewCount;
    }


    /**
     * Set up the number of materialized views discovered.
     *
     * @param materializedViewCount long
     */
    public void setMaterializedViewCount(long materializedViewCount)
    {
        this.materializedViewCount = materializedViewCount;
    }


    /**
     * Return the  number of columns discovered.
     *
     * @return long
     */
    public long getColumnCount()
    {
        return columnCount;
    }


    /**
     *  Set up the number of columns discovered.
     *
     * @param columnCount long
     */
    public void setColumnCount(long columnCount)
    {
        this.columnCount = columnCount;
    }



    @Override
    public String toString()
    {
        return "RelationalSchemaMeasurement{" +
                "qualifiedSchemaName='" + qualifiedSchemaName + '\'' +
                ", schemaName='" + schemaName + '\'' +
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
        return totalTableSize == that.totalTableSize && tableCount == that.tableCount && viewCount == that.viewCount && columnCount == that.columnCount && materializedViewCount == that.materializedViewCount && Objects.equals(qualifiedSchemaName, that.qualifiedSchemaName) && Objects.equals(schemaName, that.schemaName);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(qualifiedSchemaName, schemaName, totalTableSize, tableCount, viewCount, columnCount, materializedViewCount);
    }
}
