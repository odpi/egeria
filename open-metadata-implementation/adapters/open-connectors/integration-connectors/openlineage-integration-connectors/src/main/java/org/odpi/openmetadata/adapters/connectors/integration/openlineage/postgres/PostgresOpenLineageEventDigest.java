/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage.postgres;

import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageDataQualityAssertionsInputDataSetFacetAssertions;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageDataQualityMetricsInputDataSetFacet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageEgeriaGovernanceActionRunFacet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageInputDataSet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageInputStatisticsInputDataSetFacet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageOutputDataSet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageOutputStatisticsOutputDataSetFacet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageRunEvent;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageTestRunFacetTestExecution;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * PostgresOpenLineageEventDigest extracts the rows that an OpenLineage run event contributes to the digest tables
 * of a PostgreSQL OpenLineage log store (see {@link PostgresOpenLineageLogStoreSchema}).  It holds no database
 * code, so the extraction can be tested on its own.
 */
public class PostgresOpenLineageEventDigest
{
    /**
     * Contribution of the event to its run.  The times that the event does not set are null.
     *
     * @param status status the event reports, or null if it does not affect the status
     * @param startTime time of a START event
     * @param endTime time of a COMPLETE, FAIL or ABORT event
     */
    public record RunRow(String status,
                         Date   startTime,
                         Date   endTime)
    {
    }


    /**
     * A dataset read (I) or written (O) by the run.
     *
     * @param direction I or O
     * @param namespace dataset namespace (empty if the event had none)
     * @param name dataset name
     * @param rowCount rows read or written
     * @param size bytes read or written
     * @param fileCount files written
     * @param lifecycleStateChange lifecycle state change of an output
     * @param subsetType type of the output condition of an output subset
     * @param datasetVersion version of an output
     */
    public record DatasetRow(String direction,
                             String namespace,
                             String name,
                             Long   rowCount,
                             Long   size,
                             Long   fileCount,
                             String lifecycleStateChange,
                             String subsetType,
                             String datasetVersion)
    {
    }


    /**
     * A data quality assertion reported on an input dataset.
     *
     * @param datasetNamespace namespace of the dataset (empty if the event had none)
     * @param datasetName name of the dataset
     * @param assertion type of assertion
     * @param column column the assertion is about
     * @param success whether it passed
     * @param severity severity
     * @param name name of the assertion
     */
    public record AssertionRow(String  datasetNamespace,
                               String  datasetName,
                               String  assertion,
                               String  column,
                               boolean success,
                               String  severity,
                               String  name)
    {
    }


    /**
     * A test reported in the run's test facet.
     *
     * @param name test name
     * @param status test status
     * @param type test type
     * @param severity test severity
     */
    public record TestRow(String name,
                          String status,
                          String type,
                          String severity)
    {
    }


    private final Date                                      eventTime;
    private final String                                    eventType;
    private final String                                    runId;
    private final String                                    jobNamespace;
    private final String                                    jobName;
    private final String                                    producer;
    private final OpenLineageEgeriaGovernanceActionRunFacet governanceAction;
    private final RunRow                                    run;
    private final List<DatasetRow>                          datasets   = new ArrayList<>();
    private final List<AssertionRow>                        assertions = new ArrayList<>();
    private final List<TestRow>                             tests      = new ArrayList<>();


    /**
     * Return the digest of an event, or null if the event can not be stored because it has no job name, run
     * identifier or readable event time.
     *
     * @param event event formatted using the Egeria beans
     * @return digest or null
     */
    public static PostgresOpenLineageEventDigest digest(OpenLineageRunEvent event)
    {
        if ((event == null) || (event.getJob() == null) || (event.getJob().getName() == null) ||
                (event.getRun() == null) || (event.getRun().getRunId() == null))
        {
            return null;
        }

        Date eventTime = parseTimestamp(event.getEventTime());

        if (eventTime == null)
        {
            return null;
        }

        return new PostgresOpenLineageEventDigest(event, eventTime);
    }


    /**
     * Extract the rows.
     *
     * @param event event with a job name and run identifier
     * @param eventTime parsed event time
     */
    private PostgresOpenLineageEventDigest(OpenLineageRunEvent event,
                                           Date                eventTime)
    {
        this.eventTime    = eventTime;
        this.eventType    = (event.getEventType() == null) ? "" : event.getEventType();
        this.runId        = event.getRun().getRunId().toString();
        this.jobNamespace = PostgresOpenLineageLogStoreSchema.toStoredNamespace(event.getJob().getNamespace());
        this.jobName      = event.getJob().getName();
        this.producer     = (event.getProducer() == null) ? null : event.getProducer().toString();

        if (event.getRun().getFacets() != null)
        {
            this.governanceAction = event.getRun().getFacets().getEgeriaGovernanceAction();

            if ((event.getRun().getFacets().getTest() != null) && (event.getRun().getFacets().getTest().getTests() != null))
            {
                for (OpenLineageTestRunFacetTestExecution test : event.getRun().getFacets().getTest().getTests())
                {
                    if (test != null)
                    {
                        tests.add(new TestRow(test.getName(), test.getStatus(), test.getType(), test.getSeverity()));
                    }
                }
            }
        }
        else
        {
            this.governanceAction = null;
        }

        switch (eventType)
        {
            case "START"                     -> this.run = new RunRow(eventType, eventTime, null);
            case "COMPLETE", "FAIL", "ABORT" -> this.run = new RunRow(eventType, null, eventTime);
            case "RUNNING"                   -> this.run = new RunRow(eventType, null, null);
            default                          -> this.run = new RunRow(null, null, null);
        }

        if (event.getInputs() != null)
        {
            for (OpenLineageInputDataSet input : event.getInputs())
            {
                if ((input != null) && (input.getName() != null))
                {
                    addInput(input);
                }
            }
        }

        if (event.getOutputs() != null)
        {
            for (OpenLineageOutputDataSet output : event.getOutputs())
            {
                if ((output != null) && (output.getName() != null))
                {
                    addOutput(output);
                }
            }
        }
    }


    /**
     * Extract an input dataset and its assertions.  The input statistics facet is preferred; the data quality
     * metrics facet is used when it is absent.
     *
     * @param input input dataset
     */
    private void addInput(OpenLineageInputDataSet input)
    {
        String namespace = PostgresOpenLineageLogStoreSchema.toStoredNamespace(input.getNamespace());
        Long   rowCount  = null;
        Long   size      = null;

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
                        assertions.add(new AssertionRow(namespace,
                                                        input.getName(),
                                                        assertion.getAssertion(),
                                                        assertion.getColumn(),
                                                        assertion.isSuccess(),
                                                        assertion.getSeverity(),
                                                        assertion.getName()));
                    }
                }
            }
        }

        datasets.add(new DatasetRow(PostgresOpenLineageLogStoreSchema.INPUT_DIRECTION, namespace, input.getName(), rowCount, size, null, null, null, null));
    }


    /**
     * Extract an output dataset.
     *
     * @param output output dataset
     */
    private void addOutput(OpenLineageOutputDataSet output)
    {
        Long   rowCount             = null;
        Long   size                 = null;
        Long   fileCount            = null;
        String lifecycleStateChange = null;
        String subsetType           = null;
        String datasetVersion       = null;

        if (output.getOutputFacets() != null)
        {
            OpenLineageOutputStatisticsOutputDataSetFacet outputStatistics = output.getOutputFacets().getOutputStatistics();

            if (outputStatistics != null)
            {
                rowCount  = outputStatistics.getRowCount();
                size      = outputStatistics.getSize();
                fileCount = outputStatistics.getFileCount();
            }

            if ((output.getOutputFacets().getSubset() != null) && (output.getOutputFacets().getSubset().getOutputCondition() != null))
            {
                Object type = output.getOutputFacets().getSubset().getOutputCondition().get("type");

                subsetType = (type == null) ? "unknown" : type.toString();
            }
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

        datasets.add(new DatasetRow(PostgresOpenLineageLogStoreSchema.OUTPUT_DIRECTION,
                                    PostgresOpenLineageLogStoreSchema.toStoredNamespace(output.getNamespace()),
                                    output.getName(),
                                    rowCount,
                                    size,
                                    fileCount,
                                    lifecycleStateChange,
                                    subsetType,
                                    datasetVersion));
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


    /**
     * Return the time of the event.
     *
     * @return date
     */
    public Date getEventTime()
    {
        return eventTime;
    }


    /**
     * Return the event type (empty if the event had none).
     *
     * @return START, RUNNING, COMPLETE, FAIL, ABORT, OTHER or empty
     */
    public String getEventType()
    {
        return eventType;
    }


    /**
     * Return the run identifier.
     *
     * @return string
     */
    public String getRunId()
    {
        return runId;
    }


    /**
     * Return the job namespace (empty if the event had none).
     *
     * @return string
     */
    public String getJobNamespace()
    {
        return jobNamespace;
    }


    /**
     * Return the job name.
     *
     * @return string
     */
    public String getJobName()
    {
        return jobName;
    }


    /**
     * Return the producer of the event.
     *
     * @return URI as a string, or null
     */
    public String getProducer()
    {
        return producer;
    }


    /**
     * Return the egeria governance action run facet, if the event has one.
     *
     * @return facet or null
     */
    public OpenLineageEgeriaGovernanceActionRunFacet getGovernanceAction()
    {
        return governanceAction;
    }


    /**
     * Return the contribution of the event to its run.
     *
     * @return run row
     */
    public RunRow getRun()
    {
        return run;
    }


    /**
     * Return the datasets read and written.
     *
     * @return list
     */
    public List<DatasetRow> getDatasets()
    {
        return datasets;
    }


    /**
     * Return the data quality assertions.
     *
     * @return list
     */
    public List<AssertionRow> getAssertions()
    {
        return assertions;
    }


    /**
     * Return the tests.
     *
     * @return list
     */
    public List<TestRow> getTests()
    {
        return tests;
    }
}
