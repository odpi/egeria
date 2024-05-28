/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.surveyaction.measurements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RelationalDatabaseMeasurement captures statistics about a single database.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelationalDatabaseMeasurement
{
    private String databaseName = null;
    private long   size         = 0L;
    private long   rowsFetched  = 0L;
    private long   rowsInserted = 0L;
    private long   rowsUpdated  = 0L;
    private long   rowsDeleted  = 0L;
    private double sessionTime  = 0D;
    private double activeTime   = 0D;
    private Date   statsReset   = null;


    /**
     * Default Constructor
     */
    public RelationalDatabaseMeasurement()
    {
    }


    public String getDatabaseName()
    {
        return databaseName;
    }

    public void setDatabaseName(String databaseName)
    {
        this.databaseName = databaseName;
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
