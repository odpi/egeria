/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.lineageinsight.openlineage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * OpenLineageRunHistory is the in-memory model of a window of OpenLineage events read from a log store.
 * It groups the events by job (into runs) and by dataset (into writes, reads and data quality results) so that
 * the analysis services can work with a job's run series or a dataset's write series directly.
 */
public class OpenLineageRunHistory
{
    /**
     * Identity of a job or dataset in OpenLineage.
     *
     * @param namespace namespace
     * @param name name
     */
    public record Key(String namespace,
                      String name)
    {
        /**
         * Return a readable form.
         *
         * @return namespace/name
         */
        public String display()
        {
            return namespace + "/" + name;
        }
    }


    /**
     * The events seen for a single run of a job.
     */
    public static class RunRecord
    {
        public final String              runId;
        public final Key                 job;
        public       Date                startTime      = null;
        public       Date                endTime        = null;
        public       Date                firstEventTime = null;
        public       Date                lastEventTime  = null;
        public       String              status         = null;
        public final Map<Key, long[]>    inputVolumes   = new HashMap<>();   // rows, bytes read per input dataset
        public final Map<Key, long[]>    outputVolumes  = new HashMap<>();   // rows, bytes written per output dataset

        /**
         * Constructor.
         *
         * @param runId run identifier
         * @param job job identity
         */
        public RunRecord(String runId,
                         Key    job)
        {
            this.runId = runId;
            this.job   = job;
        }

        /**
         * Return the best available start time (the START event, or the first event seen).
         *
         * @return date or null
         */
        public Date effectiveStartTime()
        {
            return (startTime != null) ? startTime : firstEventTime;
        }

        /**
         * Return the duration in milliseconds if the run has both a start and an end.
         *
         * @return milliseconds or null
         */
        public Long duration()
        {
            if ((effectiveStartTime() != null) && (endTime != null))
            {
                return endTime.getTime() - effectiveStartTime().getTime();
            }

            return null;
        }

        /**
         * Sum one of the volume figures across the datasets.
         *
         * @param volumes map of dataset to [rows, bytes]
         * @param index 0 for rows, 1 for bytes
         * @return total
         */
        public static long total(Map<Key, long[]> volumes,
                                 int              index)
        {
            long total = 0;

            for (long[] volume : volumes.values())
            {
                total += volume[index];
            }

            return total;
        }
    }


    /**
     * A test reported by the test run facet.
     *
     * @param time event time
     * @param runId run identifier
     * @param name test name
     * @param status pass, fail or skip
     * @param type test type
     * @param severity severity
     */
    public record TestRecord(Date   time,
                             String runId,
                             String name,
                             String status,
                             String type,
                             String severity)
    {
    }


    /**
     * The runs seen for a job.
     */
    public static class JobHistory
    {
        public final Key                    job;
        public final Map<String, RunRecord> runs  = new LinkedHashMap<>();
        public final List<TestRecord>       tests = new ArrayList<>();

        /**
         * Constructor.
         *
         * @param job job identity
         */
        public JobHistory(Key job)
        {
            this.job = job;
        }
    }


    /**
     * A write to a dataset by a run.
     *
     * @param time time of the event reporting the write
     * @param runId run identifier
     * @param job writing job
     * @param eventType event type
     * @param rowCount rows written (null if not reported)
     * @param size bytes written (null if not reported)
     * @param fileCount files written (null if not reported)
     * @param lifecycleStateChange lifecycle state change reported (null if none)
     * @param subsetType type of subset written (null if none)
     * @param datasetVersion dataset version reported (null if none)
     */
    public record WriteRecord(Date   time,
                              String runId,
                              Key    job,
                              String eventType,
                              Long   rowCount,
                              Long   size,
                              Long   fileCount,
                              String lifecycleStateChange,
                              String subsetType,
                              String datasetVersion)
    {
    }


    /**
     * A read of a dataset by a run.
     *
     * @param time time of the event reporting the read
     * @param runId run identifier
     * @param job reading job
     * @param rowCount rows read (null if not reported)
     * @param size bytes read (null if not reported)
     */
    public record ReadRecord(Date   time,
                             String runId,
                             Key    job,
                             Long   rowCount,
                             Long   size)
    {
    }


    /**
     * A data quality assertion evaluated against a dataset.
     *
     * @param time event time
     * @param runId run identifier
     * @param job job that evaluated the assertion
     * @param assertion assertion type
     * @param column column (null for dataset level)
     * @param success whether it passed
     * @param severity severity
     * @param name assertion name
     */
    public record AssertionRecord(Date    time,
                                  String  runId,
                                  Key     job,
                                  String  assertion,
                                  String  column,
                                  boolean success,
                                  String  severity,
                                  String  name)
    {
        /**
         * Return the quality dimension this assertion contributes to.
         *
         * @return assertion type, qualified by column when present
         */
        public String dimension()
        {
            String base = (assertion != null) ? assertion : ((name != null) ? name : "unknown");

            return (column != null) ? base + "." + column : base;
        }
    }


    /**
     * The writes, reads and assertions seen for a dataset.
     */
    public static class DataSetHistory
    {
        public final Key                   dataset;
        public final List<WriteRecord>     writes     = new ArrayList<>();
        public final List<ReadRecord>      reads      = new ArrayList<>();
        public final List<AssertionRecord> assertions = new ArrayList<>();

        /**
         * Constructor.
         *
         * @param dataset dataset identity
         */
        public DataSetHistory(Key dataset)
        {
            this.dataset = dataset;
        }
    }


    private final Map<Key, JobHistory>     jobs     = new LinkedHashMap<>();
    private final Map<Key, DataSetHistory> datasets = new LinkedHashMap<>();
    private       int                      eventCount      = 0;
    private       int                      unreadableFiles = 0;
    private       Date                     earliestEvent   = null;
    private       Date                     latestEvent     = null;


    /**
     * Return the history of a job, creating it if this is the first event for the job.
     *
     * @param job job identity
     * @return job history
     */
    public JobHistory getJob(Key job)
    {
        return jobs.computeIfAbsent(job, JobHistory::new);
    }


    /**
     * Return the history of a dataset, creating it if this is the first event for the dataset.
     *
     * @param dataset dataset identity
     * @return dataset history
     */
    public DataSetHistory getDataSet(Key dataset)
    {
        return datasets.computeIfAbsent(dataset, DataSetHistory::new);
    }


    /**
     * Return the jobs seen.
     *
     * @return map
     */
    public Map<Key, JobHistory> getJobs()
    {
        return jobs;
    }


    /**
     * Return the datasets seen.
     *
     * @return map
     */
    public Map<Key, DataSetHistory> getDataSets()
    {
        return datasets;
    }


    /**
     * Record that an event has been added.
     *
     * @param eventTime time of the event
     */
    public void countEvent(Date eventTime)
    {
        eventCount++;

        if (eventTime != null)
        {
            if ((earliestEvent == null) || (eventTime.before(earliestEvent)))
            {
                earliestEvent = eventTime;
            }
            if ((latestEvent == null) || (eventTime.after(latestEvent)))
            {
                latestEvent = eventTime;
            }
        }
    }


    /**
     * Record that a file could not be read.
     */
    public void countUnreadableFile()
    {
        unreadableFiles++;
    }


    /**
     * Return the number of events in the history.
     *
     * @return count
     */
    public int getEventCount()
    {
        return eventCount;
    }


    /**
     * Return the number of files that could not be read.
     *
     * @return count
     */
    public int getUnreadableFiles()
    {
        return unreadableFiles;
    }


    /**
     * Return the time of the earliest event in the history.
     *
     * @return date or null
     */
    public Date getEarliestEvent()
    {
        return earliestEvent;
    }


    /**
     * Return the time of the latest event in the history.
     *
     * @return date or null
     */
    public Date getLatestEvent()
    {
        return latestEvent;
    }
}
