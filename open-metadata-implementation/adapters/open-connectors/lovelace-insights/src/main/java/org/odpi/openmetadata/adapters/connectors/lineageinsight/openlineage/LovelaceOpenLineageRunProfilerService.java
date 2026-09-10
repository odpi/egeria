/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.lineageinsight.openlineage;

import org.odpi.openmetadata.adapters.connectors.lovelaceinsight.ffdc.LovelaceInsightAuditCode;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ClassificationExplorerClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.RunMetricsProperties;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * LovelaceOpenLineageRunProfilerService analyses the series of runs of each job in an OpenLineage log store and
 * records the run profile - how often the job runs, how regularly, how long it takes, how often it fails and how
 * much data it handles - in the RunMetrics classification of the job's process.  The OpenLineage cataloguer keeps
 * the counts and latest values in RunMetrics as events arrive; this service adds the derived values that need the
 * run series, in the classification's additional properties.
 */
public class LovelaceOpenLineageRunProfilerService extends LovelaceOpenLineageAnalysisServiceBase
{
    /**
     * Default constructor
     */
    public LovelaceOpenLineageRunProfilerService()
    {
    }


    /**
     * Describe what the service produces.
     *
     * @return description
     */
    @Override
    protected String getAnalysisDescription()
    {
        return "process run profiles";
    }


    /**
     * Profile the runs of each job.
     *
     * @param history events in the analysis window
     * @return number of processes updated
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    @Override
    protected int analyse(OpenLineageRunHistory history) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        final String methodName = "analyse";

        ClassificationExplorerClient classificationClient = governanceContext.getClassificationExplorerClient();

        int    updated    = 0;
        double windowDays = getWindowDays(history);

        for (OpenLineageRunHistory.JobHistory jobHistory : history.getJobs().values())
        {
            if (jobHistory.runs.isEmpty())
            {
                continue;
            }

            OpenMetadataRootElement process = findProcess(jobHistory.job);

            if (process == null)
            {
                continue;
            }

            /*
             * Start from the classification the cataloguer maintains, if there is one.
             */
            boolean              classified = false;
            RunMetricsProperties runMetrics = null;

            if ((process.getElementHeader() != null) && (process.getElementHeader().getRunMetrics() != null))
            {
                classified = true;

                if (process.getElementHeader().getRunMetrics().getClassificationProperties() instanceof RunMetricsProperties existing)
                {
                    runMetrics = new RunMetricsProperties(existing);
                }
            }

            if (runMetrics == null)
            {
                runMetrics = new RunMetricsProperties();
            }

            Map<String, String> additionalProperties = new HashMap<>();

            if (runMetrics.getAdditionalProperties() != null)
            {
                additionalProperties.putAll(runMetrics.getAdditionalProperties());
            }

            /*
             * Order the runs by start time.
             */
            List<OpenLineageRunHistory.RunRecord> runs = new ArrayList<>();

            for (OpenLineageRunHistory.RunRecord run : jobHistory.runs.values())
            {
                if (run.effectiveStartTime() != null)
                {
                    runs.add(run);
                }
            }

            runs.sort(Comparator.comparing(OpenLineageRunHistory.RunRecord::effectiveStartTime));

            /*
             * Counts by outcome.
             */
            int completed = 0;
            int failed    = 0;
            int aborted   = 0;
            int running   = 0;

            for (OpenLineageRunHistory.RunRecord run : runs)
            {
                if ("COMPLETE".equals(run.status))
                {
                    completed++;
                }
                else if ("FAIL".equals(run.status))
                {
                    failed++;
                }
                else if ("ABORT".equals(run.status))
                {
                    aborted++;
                }
                else
                {
                    running++;
                }
            }

            /*
             * Intervals between starts.
             */
            List<Long> intervals = new ArrayList<>();

            for (int i = 1; i < runs.size(); i++)
            {
                intervals.add(runs.get(i).effectiveStartTime().getTime() - runs.get(i - 1).effectiveStartTime().getTime());
            }

            /*
             * Durations of finished runs.
             */
            List<Long> durations = new ArrayList<>();

            for (OpenLineageRunHistory.RunRecord run : runs)
            {
                Long duration = run.duration();

                if ((duration != null) && (duration >= 0))
                {
                    durations.add(duration);
                }
            }

            /*
             * Volumes.
             */
            long rowsRead     = 0;
            long rowsWritten  = 0;
            long bytesRead    = 0;
            long bytesWritten = 0;
            int  runsWithVolumes = 0;

            Set<OpenLineageRunHistory.Key> inputs  = new HashSet<>();
            Set<OpenLineageRunHistory.Key> outputs = new HashSet<>();

            for (OpenLineageRunHistory.RunRecord run : runs)
            {
                inputs.addAll(run.inputVolumes.keySet());
                outputs.addAll(run.outputVolumes.keySet());

                long runRowsRead     = OpenLineageRunHistory.RunRecord.total(run.inputVolumes, 0);
                long runRowsWritten  = OpenLineageRunHistory.RunRecord.total(run.outputVolumes, 0);
                long runBytesRead    = OpenLineageRunHistory.RunRecord.total(run.inputVolumes, 1);
                long runBytesWritten = OpenLineageRunHistory.RunRecord.total(run.outputVolumes, 1);

                if ((runRowsRead > 0) || (runRowsWritten > 0) || (runBytesRead > 0) || (runBytesWritten > 0))
                {
                    runsWithVolumes++;
                }

                rowsRead     += runRowsRead;
                rowsWritten  += runRowsWritten;
                bytesRead    += runBytesRead;
                bytesWritten += runBytesWritten;
            }

            /*
             * Record the profile.
             */
            addAnalysisProperties(additionalProperties);

            put(additionalProperties, "runsInWindow", runs.size());
            put(additionalProperties, "completedRunsInWindow", completed);
            put(additionalProperties, "failedRunsInWindow", failed);
            put(additionalProperties, "abortedRunsInWindow", aborted);
            put(additionalProperties, "unfinishedRunsInWindow", running);
            put(additionalProperties, "failureRate", (runs.isEmpty()) ? 0.0 : ((double) failed / runs.size()));
            put(additionalProperties, "runsPerDay", runs.size() / windowDays);
            put(additionalProperties, "distinctInputs", inputs.size());
            put(additionalProperties, "distinctOutputs", outputs.size());

            if (! intervals.isEmpty())
            {
                Statistics intervalStatistics = new Statistics(intervals);

                put(additionalProperties, "meanRunInterval", intervalStatistics.mean);
                put(additionalProperties, "medianRunInterval", intervalStatistics.median);
                put(additionalProperties, "minRunInterval", intervalStatistics.min);
                put(additionalProperties, "maxRunInterval", intervalStatistics.max);
                additionalProperties.put("typicalRunInterval", formatDuration(intervalStatistics.median));
                additionalProperties.put("runIntervalRegularity", intervalStatistics.regularity());
                additionalProperties.put("inferredSchedule", inferSchedule(intervalStatistics));
            }
            else
            {
                additionalProperties.remove("meanRunInterval");
                additionalProperties.remove("medianRunInterval");
                additionalProperties.remove("minRunInterval");
                additionalProperties.remove("maxRunInterval");
                additionalProperties.remove("typicalRunInterval");
                additionalProperties.put("runIntervalRegularity", "SINGLE_RUN");
                additionalProperties.put("inferredSchedule", "UNKNOWN");
            }

            if (! durations.isEmpty())
            {
                Statistics durationStatistics = new Statistics(durations);

                put(additionalProperties, "meanRunDuration", durationStatistics.mean);
                put(additionalProperties, "medianRunDuration", durationStatistics.median);
                put(additionalProperties, "maxRunDuration", durationStatistics.max);
                put(additionalProperties, "p95RunDuration", durationStatistics.percentile(95));
                additionalProperties.put("typicalRunDuration", formatDuration(durationStatistics.median));
            }

            if (runsWithVolumes > 0)
            {
                put(additionalProperties, "meanRowsReadPerRun", (double) rowsRead / runsWithVolumes);
                put(additionalProperties, "meanRowsWrittenPerRun", (double) rowsWritten / runsWithVolumes);
                put(additionalProperties, "meanBytesReadPerRun", (double) bytesRead / runsWithVolumes);
                put(additionalProperties, "meanBytesWrittenPerRun", (double) bytesWritten / runsWithVolumes);
                put(additionalProperties, "rowsWrittenInWindow", rowsWritten);
                put(additionalProperties, "rowsReadInWindow", rowsRead);
                put(additionalProperties, "bytesWrittenInWindow", bytesWritten);
                put(additionalProperties, "bytesReadInWindow", bytesRead);
            }

            runMetrics.setAdditionalProperties(additionalProperties);

            /*
             * If the cataloguer has not classified the process (for example the events were only ever written to
             * the log store), fill in the counts and latest values from the history.
             */
            if (! classified)
            {
                OpenLineageRunHistory.RunRecord first = runs.get(0);
                OpenLineageRunHistory.RunRecord last  = runs.get(runs.size() - 1);

                runMetrics.setRunCount(runs.size());
                runMetrics.setFailedRunCount(failed);
                runMetrics.setFirstRunStartTime(first.effectiveStartTime());
                runMetrics.setLastRunId(last.runId);
                runMetrics.setLastRunStartTime(last.effectiveStartTime());
                runMetrics.setLastRunEndTime(last.endTime);
                runMetrics.setLastRunStatus(last.status);

                if (last.duration() != null)
                {
                    runMetrics.setLastRunDuration(last.duration());
                }

                long totalDuration = 0;

                for (Long duration : durations)
                {
                    totalDuration += duration;
                }

                runMetrics.setTotalRunDuration(totalDuration);
                runMetrics.setLastRunRowsRead(OpenLineageRunHistory.RunRecord.total(last.inputVolumes, 0));
                runMetrics.setLastRunRowsWritten(OpenLineageRunHistory.RunRecord.total(last.outputVolumes, 0));
                runMetrics.setLastRunBytesRead(OpenLineageRunHistory.RunRecord.total(last.inputVolumes, 1));
                runMetrics.setLastRunBytesWritten(OpenLineageRunHistory.RunRecord.total(last.outputVolumes, 1));
                runMetrics.setTotalRowsRead(rowsRead);
                runMetrics.setTotalRowsWritten(rowsWritten);
                runMetrics.setTotalBytesRead(bytesRead);
                runMetrics.setTotalBytesWritten(bytesWritten);

                classificationClient.addRunMetricsClassification(process.getElementHeader().getGUID(), runMetrics, classificationClient.getMetadataSourceOptions());
            }
            else
            {
                classificationClient.updateRunMetricsClassification(process.getElementHeader().getGUID(), runMetrics, classificationClient.getUpdateOptions(false));
            }

            logRecord(methodName, LovelaceInsightAuditCode.OPEN_LINEAGE_PROCESS_PROFILED.getMessageDefinition(governanceServiceName,
                                                                                                             jobHistory.job.display(),
                                                                                                             process.getElementHeader().getGUID(),
                                                                                                             Integer.toString(runs.size()),
                                                                                                             additionalProperties.get("inferredSchedule")));
            updated++;
        }

        return updated;
    }


    /**
     * Infer a schedule label from the intervals between runs.
     *
     * @param intervals interval statistics
     * @return label
     */
    private String inferSchedule(Statistics intervals)
    {
        if (! "REGULAR".equals(intervals.regularity()))
        {
            return "IRREGULAR";
        }

        long   median  = intervals.median;
        long   minute  = 60L * 1000L;
        long   hour    = 60L * minute;
        long   day     = 24L * hour;
        long[] periods = {5 * minute, 10 * minute, 15 * minute, 30 * minute, hour, 2 * hour, 4 * hour, 6 * hour, 12 * hour, day, 7 * day, 30 * day};
        String[] labels = {"EVERY_5_MINUTES", "EVERY_10_MINUTES", "EVERY_15_MINUTES", "EVERY_30_MINUTES", "HOURLY", "EVERY_2_HOURS", "EVERY_4_HOURS", "EVERY_6_HOURS", "EVERY_12_HOURS", "DAILY", "WEEKLY", "MONTHLY"};

        for (int i = 0; i < periods.length; i++)
        {
            if (Math.abs(median - periods[i]) <= periods[i] * 0.15)
            {
                return labels[i];
            }
        }

        return "REGULAR (" + formatDuration(median) + ")";
    }


    /**
     * Simple descriptive statistics over a list of longs.
     */
    private static class Statistics
    {
        final long   min;
        final long   max;
        final long   median;
        final double mean;
        final double standardDeviation;
        final List<Long> sorted;

        /**
         * Constructor.
         *
         * @param values values (not empty)
         */
        Statistics(List<Long> values)
        {
            sorted = new ArrayList<>(values);
            sorted.sort(Comparator.naturalOrder());

            min    = sorted.get(0);
            max    = sorted.get(sorted.size() - 1);
            median = sorted.get(sorted.size() / 2);

            double total = 0;

            for (long value : sorted)
            {
                total += value;
            }

            mean = total / sorted.size();

            double squares = 0;

            for (long value : sorted)
            {
                squares += (value - mean) * (value - mean);
            }

            standardDeviation = Math.sqrt(squares / sorted.size());
        }

        /**
         * Return a percentile.
         *
         * @param percentile 0-100
         * @return value
         */
        long percentile(int percentile)
        {
            int index = (int) Math.ceil(percentile / 100.0 * sorted.size()) - 1;

            return sorted.get(Math.max(0, Math.min(index, sorted.size() - 1)));
        }

        /**
         * Classify the regularity of the values by their coefficient of variation.
         *
         * @return REGULAR, VARIABLE or IRREGULAR
         */
        String regularity()
        {
            if (sorted.size() < 2)
            {
                return "SINGLE_RUN";
            }

            double coefficientOfVariation = (mean == 0) ? 0 : standardDeviation / mean;

            if (coefficientOfVariation <= 0.25)
            {
                return "REGULAR";
            }
            else if (coefficientOfVariation <= 0.75)
            {
                return "VARIABLE";
            }

            return "IRREGULAR";
        }
    }
}
