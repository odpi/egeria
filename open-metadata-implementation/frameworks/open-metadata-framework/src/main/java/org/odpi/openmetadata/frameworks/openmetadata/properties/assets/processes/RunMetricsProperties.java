/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RunMetricsProperties describes the RunMetrics classification.  It is attached to a process and records statistics
 * about the runs of the process: how many times it has run, when, for how long and how much data it handled.
 * These are maintained as the runs are observed, for example from OpenLineage events, and support questions such as
 * how often a process runs and the volume of data it processes.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RunMetricsProperties extends ClassificationBeanProperties
{
    private Long                runCount = null;
    private Long                failedRunCount = null;
    private Date                firstRunStartTime = null;
    private String              lastRunId = null;
    private Date                lastRunStartTime = null;
    private Date                lastRunEndTime = null;
    private String              lastRunStatus = null;
    private Long                lastRunDuration = null;
    private Long                totalRunDuration = null;
    private Long                lastRunRowsRead = null;
    private Long                lastRunRowsWritten = null;
    private Long                lastRunBytesRead = null;
    private Long                lastRunBytesWritten = null;
    private Long                totalRowsRead = null;
    private Long                totalRowsWritten = null;
    private Long                totalBytesRead = null;
    private Long                totalBytesWritten = null;
    private Map<String, String> additionalProperties = null;


    /**
     * Default constructor
     */
    public RunMetricsProperties()
    {
        super();
        super.typeName = OpenMetadataType.RUN_METRICS_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public RunMetricsProperties(RunMetricsProperties template)
    {
        super(template);

        if (template != null)
        {
            runCount = template.getRunCount();
            failedRunCount = template.getFailedRunCount();
            firstRunStartTime = template.getFirstRunStartTime();
            lastRunId = template.getLastRunId();
            lastRunStartTime = template.getLastRunStartTime();
            lastRunEndTime = template.getLastRunEndTime();
            lastRunStatus = template.getLastRunStatus();
            lastRunDuration = template.getLastRunDuration();
            totalRunDuration = template.getTotalRunDuration();
            lastRunRowsRead = template.getLastRunRowsRead();
            lastRunRowsWritten = template.getLastRunRowsWritten();
            lastRunBytesRead = template.getLastRunBytesRead();
            lastRunBytesWritten = template.getLastRunBytesWritten();
            totalRowsRead = template.getTotalRowsRead();
            totalRowsWritten = template.getTotalRowsWritten();
            totalBytesRead = template.getTotalBytesRead();
            totalBytesWritten = template.getTotalBytesWritten();
            additionalProperties = template.getAdditionalProperties();
        }
    }


    /**
     * Return the number of runs of the process that have been recorded.
     *
     * @return long
     */
    public Long getRunCount()
    {
        return runCount;
    }


    /**
     * Set up the number of runs of the process that have been recorded.
     *
     * @param runCount long
     */
    public void setRunCount(Long runCount)
    {
        this.runCount = runCount;
    }


    /**
     * Return the number of recorded runs of the process that failed.
     *
     * @return long
     */
    public Long getFailedRunCount()
    {
        return failedRunCount;
    }


    /**
     * Set up the number of recorded runs of the process that failed.
     *
     * @param failedRunCount long
     */
    public void setFailedRunCount(Long failedRunCount)
    {
        this.failedRunCount = failedRunCount;
    }


    /**
     * Return the start time of the earliest recorded run of the process.
     *
     * @return date
     */
    public Date getFirstRunStartTime()
    {
        return firstRunStartTime;
    }


    /**
     * Set up the start time of the earliest recorded run of the process.
     *
     * @param firstRunStartTime date
     */
    public void setFirstRunStartTime(Date firstRunStartTime)
    {
        this.firstRunStartTime = firstRunStartTime;
    }


    /**
     * Return the identifier of the most recent run of the process.
     *
     * @return string
     */
    public String getLastRunId()
    {
        return lastRunId;
    }


    /**
     * Set up the identifier of the most recent run of the process.
     *
     * @param lastRunId string
     */
    public void setLastRunId(String lastRunId)
    {
        this.lastRunId = lastRunId;
    }


    /**
     * Return the start time of the most recent run of the process.
     *
     * @return date
     */
    public Date getLastRunStartTime()
    {
        return lastRunStartTime;
    }


    /**
     * Set up the start time of the most recent run of the process.
     *
     * @param lastRunStartTime date
     */
    public void setLastRunStartTime(Date lastRunStartTime)
    {
        this.lastRunStartTime = lastRunStartTime;
    }


    /**
     * Return the end time of the most recent run of the process.
     *
     * @return date
     */
    public Date getLastRunEndTime()
    {
        return lastRunEndTime;
    }


    /**
     * Set up the end time of the most recent run of the process.
     *
     * @param lastRunEndTime date
     */
    public void setLastRunEndTime(Date lastRunEndTime)
    {
        this.lastRunEndTime = lastRunEndTime;
    }


    /**
     * Return the status reported for the most recent run of the process, for example START, RUNNING, COMPLETE, FAIL or ABORT.
     *
     * @return string
     */
    public String getLastRunStatus()
    {
        return lastRunStatus;
    }


    /**
     * Set up the status reported for the most recent run of the process, for example START, RUNNING, COMPLETE, FAIL or ABORT.
     *
     * @param lastRunStatus string
     */
    public void setLastRunStatus(String lastRunStatus)
    {
        this.lastRunStatus = lastRunStatus;
    }


    /**
     * Return the duration of the most recent completed run of the process in milliseconds.
     *
     * @return long
     */
    public Long getLastRunDuration()
    {
        return lastRunDuration;
    }


    /**
     * Set up the duration of the most recent completed run of the process in milliseconds.
     *
     * @param lastRunDuration long
     */
    public void setLastRunDuration(Long lastRunDuration)
    {
        this.lastRunDuration = lastRunDuration;
    }


    /**
     * Return the sum of the durations of the recorded completed runs of the process in milliseconds.
     *
     * @return long
     */
    public Long getTotalRunDuration()
    {
        return totalRunDuration;
    }


    /**
     * Set up the sum of the durations of the recorded completed runs of the process in milliseconds.
     *
     * @param totalRunDuration long
     */
    public void setTotalRunDuration(Long totalRunDuration)
    {
        this.totalRunDuration = totalRunDuration;
    }


    /**
     * Return the number of rows (records) read by the most recent run of the process.
     *
     * @return long
     */
    public Long getLastRunRowsRead()
    {
        return lastRunRowsRead;
    }


    /**
     * Set up the number of rows (records) read by the most recent run of the process.
     *
     * @param lastRunRowsRead long
     */
    public void setLastRunRowsRead(Long lastRunRowsRead)
    {
        this.lastRunRowsRead = lastRunRowsRead;
    }


    /**
     * Return the number of rows (records) written by the most recent run of the process.
     *
     * @return long
     */
    public Long getLastRunRowsWritten()
    {
        return lastRunRowsWritten;
    }


    /**
     * Set up the number of rows (records) written by the most recent run of the process.
     *
     * @param lastRunRowsWritten long
     */
    public void setLastRunRowsWritten(Long lastRunRowsWritten)
    {
        this.lastRunRowsWritten = lastRunRowsWritten;
    }


    /**
     * Return the number of bytes read by the most recent run of the process.
     *
     * @return long
     */
    public Long getLastRunBytesRead()
    {
        return lastRunBytesRead;
    }


    /**
     * Set up the number of bytes read by the most recent run of the process.
     *
     * @param lastRunBytesRead long
     */
    public void setLastRunBytesRead(Long lastRunBytesRead)
    {
        this.lastRunBytesRead = lastRunBytesRead;
    }


    /**
     * Return the number of bytes written by the most recent run of the process.
     *
     * @return long
     */
    public Long getLastRunBytesWritten()
    {
        return lastRunBytesWritten;
    }


    /**
     * Set up the number of bytes written by the most recent run of the process.
     *
     * @param lastRunBytesWritten long
     */
    public void setLastRunBytesWritten(Long lastRunBytesWritten)
    {
        this.lastRunBytesWritten = lastRunBytesWritten;
    }


    /**
     * Return the sum of the rows (records) read by the recorded runs of the process.
     *
     * @return long
     */
    public Long getTotalRowsRead()
    {
        return totalRowsRead;
    }


    /**
     * Set up the sum of the rows (records) read by the recorded runs of the process.
     *
     * @param totalRowsRead long
     */
    public void setTotalRowsRead(Long totalRowsRead)
    {
        this.totalRowsRead = totalRowsRead;
    }


    /**
     * Return the sum of the rows (records) written by the recorded runs of the process.
     *
     * @return long
     */
    public Long getTotalRowsWritten()
    {
        return totalRowsWritten;
    }


    /**
     * Set up the sum of the rows (records) written by the recorded runs of the process.
     *
     * @param totalRowsWritten long
     */
    public void setTotalRowsWritten(Long totalRowsWritten)
    {
        this.totalRowsWritten = totalRowsWritten;
    }


    /**
     * Return the sum of the bytes read by the recorded runs of the process.
     *
     * @return long
     */
    public Long getTotalBytesRead()
    {
        return totalBytesRead;
    }


    /**
     * Set up the sum of the bytes read by the recorded runs of the process.
     *
     * @param totalBytesRead long
     */
    public void setTotalBytesRead(Long totalBytesRead)
    {
        this.totalBytesRead = totalBytesRead;
    }


    /**
     * Return the sum of the bytes written by the recorded runs of the process.
     *
     * @return long
     */
    public Long getTotalBytesWritten()
    {
        return totalBytesWritten;
    }


    /**
     * Set up the sum of the bytes written by the recorded runs of the process.
     *
     * @param totalBytesWritten long
     */
    public void setTotalBytesWritten(Long totalBytesWritten)
    {
        this.totalBytesWritten = totalBytesWritten;
    }


    /**
     * Return the additional properties describing the runs that are not covered by the standard properties.
     *
     * @return map
     */
    public Map<String, String> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up the additional properties describing the runs that are not covered by the standard properties.
     *
     * @param additionalProperties map
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RunMetricsProperties{" +
                "runCount=" + runCount +
                ", failedRunCount=" + failedRunCount +
                ", firstRunStartTime=" + firstRunStartTime +
                ", lastRunId=" + lastRunId +
                ", lastRunStartTime=" + lastRunStartTime +
                ", lastRunEndTime=" + lastRunEndTime +
                ", lastRunStatus=" + lastRunStatus +
                ", lastRunDuration=" + lastRunDuration +
                ", totalRunDuration=" + totalRunDuration +
                ", lastRunRowsRead=" + lastRunRowsRead +
                ", lastRunRowsWritten=" + lastRunRowsWritten +
                ", lastRunBytesRead=" + lastRunBytesRead +
                ", lastRunBytesWritten=" + lastRunBytesWritten +
                ", totalRowsRead=" + totalRowsRead +
                ", totalRowsWritten=" + totalRowsWritten +
                ", totalBytesRead=" + totalBytesRead +
                ", totalBytesWritten=" + totalBytesWritten +
                ", additionalProperties=" + additionalProperties +
                "} " + super.toString();
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        RunMetricsProperties that = (RunMetricsProperties) objectToCompare;
        return Objects.equals(runCount, that.runCount) &&
               Objects.equals(failedRunCount, that.failedRunCount) &&
               Objects.equals(firstRunStartTime, that.firstRunStartTime) &&
               Objects.equals(lastRunId, that.lastRunId) &&
               Objects.equals(lastRunStartTime, that.lastRunStartTime) &&
               Objects.equals(lastRunEndTime, that.lastRunEndTime) &&
               Objects.equals(lastRunStatus, that.lastRunStatus) &&
               Objects.equals(lastRunDuration, that.lastRunDuration) &&
               Objects.equals(totalRunDuration, that.totalRunDuration) &&
               Objects.equals(lastRunRowsRead, that.lastRunRowsRead) &&
               Objects.equals(lastRunRowsWritten, that.lastRunRowsWritten) &&
               Objects.equals(lastRunBytesRead, that.lastRunBytesRead) &&
               Objects.equals(lastRunBytesWritten, that.lastRunBytesWritten) &&
               Objects.equals(totalRowsRead, that.totalRowsRead) &&
               Objects.equals(totalRowsWritten, that.totalRowsWritten) &&
               Objects.equals(totalBytesRead, that.totalBytesRead) &&
               Objects.equals(totalBytesWritten, that.totalBytesWritten) &&
               Objects.equals(additionalProperties, that.additionalProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), runCount, failedRunCount, firstRunStartTime, lastRunId, lastRunStartTime, lastRunEndTime, lastRunStatus, lastRunDuration, totalRunDuration, lastRunRowsRead, lastRunRowsWritten, lastRunBytesRead, lastRunBytesWritten, totalRowsRead, totalRowsWritten, totalBytesRead, totalBytesWritten, additionalProperties);
    }
}
