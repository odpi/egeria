/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.lineageinsight.openlineage;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageDataQualityAssertionsInputDataSetFacetAssertions;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageDataQualityMetricsInputDataSetFacet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageInputDataSet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageInputStatisticsInputDataSetFacet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageOutputDataSet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageOutputStatisticsOutputDataSetFacet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageRunEvent;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageTestRunFacetTestExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

/**
 * OpenLineageLogStoreReader walks the directory tree written by the file-based OpenLineage log store integration
 * connector ({directory}/{namespace}/{job}/{runId}-{timestamp}-{eventType}.openlineageevent), parses each run event
 * and accumulates it into an OpenLineageRunHistory.  Events outside the requested time window are skipped.
 */
public class OpenLineageLogStoreReader
{
    private static final Logger       log           = LoggerFactory.getLogger(OpenLineageLogStoreReader.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String       EVENT_SUFFIX  = ".openlineageevent";


    /**
     * Read every event file below a directory into a history.
     *
     * @param logStoreDirectory root of the log store
     * @param windowStart earliest event time to include (null for no limit)
     * @param windowEnd latest event time to include (null for no limit)
     * @return history
     */
    public OpenLineageRunHistory read(File logStoreDirectory,
                                      Date windowStart,
                                      Date windowEnd)
    {
        OpenLineageRunHistory history = new OpenLineageRunHistory();

        readDirectory(logStoreDirectory, windowStart, windowEnd, history);

        return history;
    }


    /**
     * Recursively read a directory.
     *
     * @param directory directory
     * @param windowStart earliest event time to include (null for no limit)
     * @param windowEnd latest event time to include (null for no limit)
     * @param history history to add to
     */
    private void readDirectory(File                  directory,
                               Date                  windowStart,
                               Date                  windowEnd,
                               OpenLineageRunHistory history)
    {
        File[] files = directory.listFiles();

        if (files == null)
        {
            return;
        }

        for (File file : files)
        {
            if (file.isDirectory())
            {
                readDirectory(file, windowStart, windowEnd, history);
            }
            else if (file.getName().endsWith(EVENT_SUFFIX))
            {
                try
                {
                    String              json  = Files.readString(file.toPath(), StandardCharsets.UTF_8);
                    OpenLineageRunEvent event = OBJECT_MAPPER.readValue(json, OpenLineageRunEvent.class);

                    addEvent(event, windowStart, windowEnd, history);
                }
                catch (Exception error)
                {
                    log.debug("Unable to read OpenLineage event file {}: {}", file.getAbsolutePath(), error.getMessage());
                    history.countUnreadableFile();
                }
            }
        }
    }


    /**
     * Add a run event to the history.
     *
     * @param event event
     * @param windowStart earliest event time to include (null for no limit)
     * @param windowEnd latest event time to include (null for no limit)
     * @param history history to add to
     */
    public void addEvent(OpenLineageRunEvent   event,
                         Date                  windowStart,
                         Date                  windowEnd,
                         OpenLineageRunHistory history)
    {
        if ((event == null) || (event.getJob() == null) || (event.getJob().getName() == null) || (event.getRun() == null) || (event.getRun().getRunId() == null))
        {
            return;
        }

        Date eventTime = parseTimestamp(event.getEventTime());

        if (eventTime == null)
        {
            return;
        }

        if (((windowStart != null) && (eventTime.before(windowStart))) || ((windowEnd != null) && (eventTime.after(windowEnd))))
        {
            return;
        }

        history.countEvent(eventTime);

        OpenLineageRunHistory.Key        jobKey     = new OpenLineageRunHistory.Key(event.getJob().getNamespace(), event.getJob().getName());
        OpenLineageRunHistory.JobHistory jobHistory = history.getJob(jobKey);
        String                           runId      = event.getRun().getRunId().toString();
        String                           eventType  = event.getEventType();

        OpenLineageRunHistory.RunRecord run = jobHistory.runs.computeIfAbsent(runId, id -> new OpenLineageRunHistory.RunRecord(id, jobKey));

        if ((run.firstEventTime == null) || (eventTime.before(run.firstEventTime)))
        {
            run.firstEventTime = eventTime;
        }
        if ((run.lastEventTime == null) || (eventTime.after(run.lastEventTime)))
        {
            run.lastEventTime = eventTime;
        }

        if ("START".equals(eventType))
        {
            if ((run.startTime == null) || (eventTime.before(run.startTime)))
            {
                run.startTime = eventTime;
            }
            if (run.status == null)
            {
                run.status = eventType;
            }
        }
        else if (("COMPLETE".equals(eventType)) || ("FAIL".equals(eventType)) || ("ABORT".equals(eventType)))
        {
            if ((run.endTime == null) || (eventTime.after(run.endTime)))
            {
                run.endTime = eventTime;
                run.status  = eventType;
            }
        }
        else if (("RUNNING".equals(eventType)) && ((run.status == null) || ("START".equals(run.status))))
        {
            run.status = eventType;
        }

        /*
         * Inputs
         */
        if (event.getInputs() != null)
        {
            for (OpenLineageInputDataSet input : event.getInputs())
            {
                if ((input == null) || (input.getName() == null))
                {
                    continue;
                }

                OpenLineageRunHistory.Key            datasetKey     = new OpenLineageRunHistory.Key(input.getNamespace(), input.getName());
                OpenLineageRunHistory.DataSetHistory datasetHistory = history.getDataSet(datasetKey);

                Long rowCount = null;
                Long size     = null;

                if (input.getInputFacets() != null)
                {
                    OpenLineageInputStatisticsInputDataSetFacet    inputStatistics = input.getInputFacets().getInputStatistics();
                    OpenLineageDataQualityMetricsInputDataSetFacet inputMetrics    = input.getInputFacets().getDataQualityMetrics();

                    if (inputStatistics != null)
                    {
                        rowCount = inputStatistics.getRowCount();
                        size     = inputStatistics.getSize();
                    }
                    else if (inputMetrics != null)
                    {
                        rowCount = inputMetrics.getRowCount();
                        size     = inputMetrics.getBytes();
                    }

                    if ((input.getInputFacets().getDataQualityAssertions() != null) && (input.getInputFacets().getDataQualityAssertions().getAssertions() != null))
                    {
                        for (OpenLineageDataQualityAssertionsInputDataSetFacetAssertions assertion : input.getInputFacets().getDataQualityAssertions().getAssertions())
                        {
                            if (assertion != null)
                            {
                                datasetHistory.assertions.add(new OpenLineageRunHistory.AssertionRecord(eventTime,
                                                                                                        runId,
                                                                                                        jobKey,
                                                                                                        assertion.getAssertion(),
                                                                                                        assertion.getColumn(),
                                                                                                        assertion.isSuccess(),
                                                                                                        assertion.getSeverity(),
                                                                                                        assertion.getName()));
                            }
                        }
                    }
                }

                /*
                 * A read is recorded once per run per dataset.  It is registered on first sight (so that a run that
                 * has only started is still counted) and replaced when a later event - the completing event, or a
                 * RUNNING event carrying statistics - reports the read.
                 */
                boolean statisticsPresent = (rowCount != null) || (size != null);

                if ((statisticsPresent) || (! run.inputVolumes.containsKey(datasetKey)))
                {
                    run.inputVolumes.put(datasetKey, new long[]{(rowCount == null) ? 0 : rowCount, (size == null) ? 0 : size});
                }

                recordOnce(datasetHistory.reads,
                           read -> runId.equals(read.runId()),
                           (statisticsPresent) || (! "START".equals(eventType)),
                           new OpenLineageRunHistory.ReadRecord(eventTime, runId, jobKey, rowCount, size));
            }
        }

        /*
         * Outputs
         */
        if (event.getOutputs() != null)
        {
            for (OpenLineageOutputDataSet output : event.getOutputs())
            {
                if ((output == null) || (output.getName() == null))
                {
                    continue;
                }

                OpenLineageRunHistory.Key            datasetKey     = new OpenLineageRunHistory.Key(output.getNamespace(), output.getName());
                OpenLineageRunHistory.DataSetHistory datasetHistory = history.getDataSet(datasetKey);

                Long   rowCount             = null;
                Long   size                 = null;
                Long   fileCount            = null;
                String lifecycleStateChange = null;
                String subsetType           = null;
                String datasetVersion       = null;

                if ((output.getOutputFacets() != null) && (output.getOutputFacets().getOutputStatistics() != null))
                {
                    OpenLineageOutputStatisticsOutputDataSetFacet outputStatistics = output.getOutputFacets().getOutputStatistics();

                    rowCount  = outputStatistics.getRowCount();
                    size      = outputStatistics.getSize();
                    fileCount = outputStatistics.getFileCount();
                }

                if ((output.getOutputFacets() != null) && (output.getOutputFacets().getSubset() != null) && (output.getOutputFacets().getSubset().getOutputCondition() != null))
                {
                    Object type = output.getOutputFacets().getSubset().getOutputCondition().get("type");

                    subsetType = (type == null) ? "unknown" : type.toString();
                }

                if (output.getFacets() != null)
                {
                    if (output.getFacets().getLifecycleStateChange() != null)
                    {
                        lifecycleStateChange = output.getFacets().getLifecycleStateChange().getLifecycleStateChange();
                    }
                    if (output.getFacets().getVersion() != null)
                    {
                        datasetVersion = output.getFacets().getVersion().getDatasetVersion();
                    }
                }

                boolean statisticsPresent = (rowCount != null) || (size != null);

                if ((statisticsPresent) || (! run.outputVolumes.containsKey(datasetKey)))
                {
                    run.outputVolumes.put(datasetKey, new long[]{(rowCount == null) ? 0 : rowCount, (size == null) ? 0 : size});
                }

                /*
                 * A write is recorded once per run per dataset, on the same basis as a read.
                 */
                recordOnce(datasetHistory.writes,
                           write -> runId.equals(write.runId()),
                           (statisticsPresent) || (! "START".equals(eventType)),
                           new OpenLineageRunHistory.WriteRecord(eventTime, runId, jobKey, eventType, rowCount, size, fileCount, lifecycleStateChange, subsetType, datasetVersion));
            }
        }

        /*
         * Tests
         */
        if ((event.getRun().getFacets() != null) && (event.getRun().getFacets().getTest() != null) && (event.getRun().getFacets().getTest().getTests() != null))
        {
            for (OpenLineageTestRunFacetTestExecution test : event.getRun().getFacets().getTest().getTests())
            {
                if (test != null)
                {
                    jobHistory.tests.add(new OpenLineageRunHistory.TestRecord(eventTime, runId, test.getName(), test.getStatus(), test.getType(), test.getSeverity()));
                }
            }
        }
    }


    /**
     * Keep one record per run in a list: add the record if the run has none, or replace the run's record when the
     * new one is authoritative (from a completing event or one carrying statistics).
     *
     * @param records list
     * @param sameRun test for a record of the same run
     * @param authoritative whether the new record should replace an existing one
     * @param record new record
     * @param <T> record type
     */
    private <T> void recordOnce(List<T>      records,
                                Predicate<T> sameRun,
                                boolean      authoritative,
                                T            record)
    {
        boolean existing = records.stream().anyMatch(sameRun);

        if (! existing)
        {
            records.add(record);
        }
        else if (authoritative)
        {
            records.removeIf(sameRun);
            records.add(record);
        }
    }


    /**
     * Parse an ISO-8601 timestamp from an OpenLineage event.  Timestamps without a zone are treated as UTC.
     *
     * @param timestamp string timestamp
     * @return date or null if the timestamp is missing or unparsable
     */
    public static Date parseTimestamp(String timestamp)
    {
        if ((timestamp == null) || (timestamp.isBlank()))
        {
            return null;
        }

        try
        {
            return Date.from(OffsetDateTime.parse(timestamp).toInstant());
        }
        catch (Exception offsetError)
        {
            try
            {
                return Date.from(Instant.parse(timestamp));
            }
            catch (Exception instantError)
            {
                try
                {
                    return Date.from(LocalDateTime.parse(timestamp).toInstant(ZoneOffset.UTC));
                }
                catch (Exception localError)
                {
                    return null;
                }
            }
        }
    }
}
