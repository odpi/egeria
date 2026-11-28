/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.lineageinsight.openlineage;

import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageEgeriaGovernanceActionRunFacet;

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
     * The parts of a history that an analysis works from.  A log store holds a row for every run and for every
     * dataset each run read or wrote, so a service that asks only for the parts it uses keeps the history small.
     */
    public enum Content
    {
        /**
         * The runs of each job, with the volumes each run read and wrote.
         */
        RUNS,

        /**
         * The reads and writes of each dataset.
         */
        DATASET_ACTIVITY,

        /**
         * The data quality assertions evaluated against each dataset.
         */
        ASSERTIONS,

        /**
         * The tests reported by each job.
         */
        TESTS
    }


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
         * The egeria_governanceAction facet of the latest run of the job, if the job is a step of a governance
         * action process reported by the governance action publisher (null otherwise).
         */
        public OpenLineageEgeriaGovernanceActionRunFacet governanceAction = null;

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


    private final Map<String, Map<String, Key>> keys    = new HashMap<>();
    private final Map<String, String>           strings = new HashMap<>();

    private final Map<Key, JobHistory>     jobs     = new LinkedHashMap<>();
    private final Map<Key, DataSetHistory> datasets = new LinkedHashMap<>();
    private       int                      jobCount        = 0;
    private       int                      dataSetCount    = 0;
    private       int                      eventCount      = 0;
    private       int                      unreadableEvents = 0;
    private       Date                     earliestEvent   = null;
    private       Date                     latestEvent     = null;


    /**
     * Return the key for a job or dataset.  The same key is returned each time the same namespace and name are
     * passed, so the many records of a job or dataset share one key (and its strings) rather than each holding a
     * copy.
     *
     * @param namespace namespace
     * @param name name
     * @return shared key
     */
    public Key key(String namespace,
                   String name)
    {
        return keys.computeIfAbsent(namespace, newNamespace -> new HashMap<>())
                   .computeIfAbsent(name, newName -> new Key(namespace, name));
    }


    /**
     * Return the shared copy of a value that takes few distinct values (an event type or a status, for example),
     * so that each record does not hold its own copy.  It is not for values that are different in every record.
     *
     * @param value value or null
     * @return shared copy or null
     */
    public String shared(String value)
    {
        if (value == null)
        {
            return null;
        }

        String existing = strings.putIfAbsent(value, value);

        return (existing == null) ? value : existing;
    }


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
     * Record the number of events in the window, and the times of the first and last, when the history is built
     * from a digest rather than event by event.
     *
     * @param count number of events
     * @param earliest time of the first event (null if there were none)
     * @param latest time of the last event (null if there were none)
     */
    public void recordEventSummary(int  count,
                                   Date earliest,
                                   Date latest)
    {
        eventCount    = count;
        earliestEvent = earliest;
        latestEvent   = latest;
    }


    /**
     * Record the number of jobs in the window, when the history does not hold an entry for each of them.
     *
     * @param count number of jobs
     */
    public void recordJobCount(int count)
    {
        jobCount = count;
    }


    /**
     * Record the number of datasets in the window, when the history does not hold an entry for each of them.
     *
     * @param count number of datasets
     */
    public void recordDataSetCount(int count)
    {
        dataSetCount = count;
    }


    /**
     * Return the number of jobs in the window.  This is the number of jobs in the history unless a larger count
     * was recorded because the history holds only the jobs its reader was asked for.
     *
     * @return count
     */
    public int getJobCount()
    {
        return Math.max(jobCount, jobs.size());
    }


    /**
     * Return the number of datasets in the window.  This is the number of datasets in the history unless a larger
     * count was recorded because the history holds only the datasets its reader was asked for.
     *
     * @return count
     */
    public int getDataSetCount()
    {
        return Math.max(dataSetCount, datasets.size());
    }


    /**
     * Record that a stored event could not be read - for a file-based log store, an event file that is not an
     * OpenLineage run event in JSON.
     */
    public void countUnreadableEvent()
    {
        unreadableEvents++;
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
     * Return the number of stored events that could not be read.
     *
     * @return count
     */
    public int getUnreadableEvents()
    {
        return unreadableEvents;
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
