/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.opensurvey.measurements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RelationalDataManagerMeasurement captures statistics about a single database/catalog.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelationalDataManagerMeasurement
{
    private String resourceName          = null;
    private long   schemaCount           = 0L;
    private long   tableCount            = 0L;
    private long   viewCount             = 0L;
    private long   columnCount           = 0L;
    private long   materializedViewCount = 0L;
    private long   size                  = 0L;
    private long   rowsFetched           = 0L;
    private long   rowsInserted          = 0L;
    private long   rowsUpdated           = 0L;
    private long   rowsDeleted           = 0L;
    private double sessionTime           = 0D;
    private double activeTime            = 0D;
    private Date   statsReset            = null;


    /**
     * Default Constructor
     */
    public RelationalDataManagerMeasurement()
    {
    }


    public String getResourceName()
    {
        return resourceName;
    }

    public void setResourceName(String resourceName)
    {
        this.resourceName = resourceName;
    }

    public long getSchemaCount()
    {
        return schemaCount;
    }

    public void setSchemaCount(long schemaCount)
    {
        this.schemaCount = schemaCount;
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


    public long getSize()
    {
        return size;
    }

    public void setSize(long size)
    {
        this.size = size;
    }

    public long getRowsFetched()
    {
        return rowsFetched;
    }

    public void setRowsFetched(long rowsFetched)
    {
        this.rowsFetched = rowsFetched;
    }

    public long getRowsInserted()
    {
        return rowsInserted;
    }

    public void setRowsInserted(long rowsInserted)
    {
        this.rowsInserted = rowsInserted;
    }

    public long getRowsUpdated()
    {
        return rowsUpdated;
    }

    public void setRowsUpdated(long rowsUpdated)
    {
        this.rowsUpdated = rowsUpdated;
    }

    public long getRowsDeleted()
    {
        return rowsDeleted;
    }

    public void setRowsDeleted(long rowsDeleted)
    {
        this.rowsDeleted = rowsDeleted;
    }

    public double getSessionTime()
    {
        return sessionTime;
    }

    public void setSessionTime(double sessionTime)
    {
        this.sessionTime = sessionTime;
    }

    public double getActiveTime()
    {
        return activeTime;
    }

    public void setActiveTime(double activeTime)
    {
        this.activeTime = activeTime;
    }

    public Date getStatsReset()
    {
        return statsReset;
    }

    public void setStatsReset(Date statsReset)
    {
        this.statsReset = statsReset;
    }
}
