/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * Parse realistic OpenLineage events (in the JSON form produced by the OpenLineage integrations) into the beans
 * and validate that every standard facet is mapped to its typed bean, that custom facets and unknown properties
 * are retained, and that the event can be written back out without loss.
 */
public class OpenLineageEventParsingTest
{
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    private String readResource(String name) throws Exception
    {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(name))
        {
            assertNotNull(inputStream, "Missing test resource " + name);

            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }


    /**
     * Full run event with every standard facet plus a custom facet.
     *
     * @throws Exception problem reading the resource or parsing the event
     */
    @Test
    public void testRunEventAllFacets() throws Exception
    {
        String json = readResource("openlineage/run-event-all-facets.json");

        OpenLineageRunEvent event = OBJECT_MAPPER.readValue(json, OpenLineageRunEvent.class);

        assertEquals(event.getEventType(), "COMPLETE");
        assertEquals(event.getEventTime(), "2026-09-08T14:00:00.123Z");
        assertEquals(event.getSchemaURL(), URI.create("https://openlineage.io/spec/2-0-2/OpenLineage.json#/$defs/RunEvent"));

        /*
         * Run facets
         */
        OpenLineageRun run = event.getRun();
        assertEquals(run.getRunId(), UUID.fromString("d46e465b-d358-4d32-83d4-df660ff614dd"));
        OpenLineageRunFacets runFacets = run.getFacets();

        assertEquals(runFacets.getParent().getRun().getRunId(), UUID.fromString("f99310b4-3c3c-1a1a-2b2b-c1c1c1c1c1c1"));
        assertEquals(runFacets.getParent().getJob().getName(), "orders_etl");
        assertNotNull(runFacets.getParent().getJob().getFacets().get("documentation"));
        assertEquals(runFacets.getParent().getRoot().getRun().getRunId(), UUID.fromString("a1a1a1a1-b2b2-c3c3-d4d4-e5e5e5e5e5e5"));
        assertEquals(runFacets.getParent().getRoot().getJob().getNamespace(), "airflow://prod");
        assertEquals(runFacets.getParent().get_schemaURL(), URI.create("https://openlineage.io/spec/facets/1-2-0/ParentRunFacet.json#/$defs/ParentRunFacet"));

        assertEquals(runFacets.getNominalTime().getNominalStartTime(), "2026-09-08T13:00:00Z");
        assertEquals(runFacets.getNominalTime().getNominalEndTime(), "2026-09-08T14:00:00Z");

        assertEquals(runFacets.getEnvironmentVariables().getEnvironmentVariables().size(), 2);
        assertEquals(runFacets.getEnvironmentVariables().getEnvironmentVariables().get(1).getValue(), "eu-west-1");

        assertEquals(runFacets.getErrorMessage().getMessage(), "Boom");
        assertEquals(runFacets.getErrorMessage().getProgrammingLanguage(), "python");
        assertEquals(runFacets.getErrorMessage().getStackTrace(), "Traceback...");

        assertEquals(runFacets.getExecutionParameters().getParameters().get(0).getKey(), "batch_date");
        assertEquals(runFacets.getExecutionParameters().getParameters().get(0).getValue(), "2026-09-08");

        assertEquals(runFacets.getExternalQuery().getExternalQueryId(), "01b7c2d3-aaaa-bbbb-cccc-000000000001");
        assertEquals(runFacets.getExternalQuery().getSource(), "snowflake");

        assertEquals(runFacets.getExtractionError().getTotalTasks(), Long.valueOf(3));
        assertEquals(runFacets.getExtractionError().getFailedTasks(), Long.valueOf(1));
        assertEquals(runFacets.getExtractionError().getErrors().get(0).getTaskNumber(), Long.valueOf(2));

        OpenLineageJobDependenciesRunFacetDependency upstream = runFacets.getJobDependencies().getUpstream().get(0);
        assertEquals(upstream.getJob().getName(), "orders_etl.extract");
        assertEquals(upstream.getRun().getRunId(), UUID.fromString("11111111-2222-3333-4444-555555555555"));
        assertEquals(upstream.getDependencyType(), "TRIGGER");
        assertEquals(upstream.getSequenceTriggerRule(), "FINISH_TO_START");
        assertEquals(upstream.getStatusTriggerRule(), "ALL_SUCCESS");
        assertEquals(runFacets.getJobDependencies().getDownstream().get(0).getJob().getName(), "orders_etl.report");
        assertEquals(runFacets.getJobDependencies().getTriggerRule(), "all_success");

        assertEquals(runFacets.getProcessingEngine().getName(), "Airflow");
        assertEquals(runFacets.getProcessingEngine().getVersion(), "2.10.1");
        assertEquals(runFacets.getProcessingEngine().getOpenlineageAdapterVersion(), "1.53.0");

        assertEquals(runFacets.getTags().getTags().get(0).getKey(), "environment");
        assertEquals(runFacets.getTags().getTags().get(0).getSource(), "AIRFLOW");

        OpenLineageTestRunFacetTestExecution test = runFacets.getTest().getTests().get(0);
        assertEquals(test.getName(), "not_null_orders_id");
        assertEquals(test.getStatus(), "pass");
        assertEquals(test.getSeverity(), "error");
        assertEquals(test.getType(), "not_null");
        assertEquals(test.getContentType(), "text/x-sql");
        assertEquals(test.getParams().get("column"), "id");

        /* registered custom run facets are typed */
        assertEquals(runFacets.getGcpComposerRun().getDagRunId(), "manual__2026-09-08T13:00:00+00:00");
        assertEquals(runFacets.getGcpDataproc().getAppId(), "application_1");
        assertEquals(runFacets.getGcpDataproc().getProjectId(), "my-project");
        assertEquals(runFacets.getGcpDataproc().getJobType(), "job");

        /* custom (unknown) run facet is retained as a generic run facet with its properties */
        OpenLineageRunFacet airflowFacet = runFacets.getAdditionalProperties().get("airflow");
        assertNotNull(airflowFacet);
        assertEquals(airflowFacet.get_producer(), URI.create("https://example.com/producer"));
        assertTrue(airflowFacet.getAdditionalProperties().get("dag") instanceof Map);
        assertEquals(((Map<?, ?>) airflowFacet.getAdditionalProperties().get("dag")).get("dag_id"), "orders_etl");

        /*
         * Job facets
         */
        OpenLineageJob job = event.getJob();
        assertEquals(job.getNamespace(), "airflow://prod");
        assertEquals(job.getName(), "orders_etl.count_orders");
        OpenLineageJobFacets jobFacets = job.getFacets();

        assertEquals(jobFacets.getDocumentation().getDescription(), "Counts the orders");
        assertEquals(jobFacets.getDocumentation().getContentType(), "text/markdown");
        assertEquals(jobFacets.getSql().getQuery(), "INSERT INTO order_counts SELECT count(*) FROM orders");
        assertEquals(jobFacets.getSql().getDialect(), "postgres");
        assertEquals(jobFacets.getSourceCodeLocation().getType(), "git");
        assertEquals(jobFacets.getSourceCodeLocation().getUrl(), URI.create("https://github.com/example/pipelines/blob/abc123/dags/orders_etl.py"));
        assertEquals(jobFacets.getSourceCodeLocation().getRepoUrl(), "https://github.com/example/pipelines");
        assertEquals(jobFacets.getSourceCodeLocation().getPath(), "dags/orders_etl.py");
        assertEquals(jobFacets.getSourceCodeLocation().getVersion(), "abc123");
        assertEquals(jobFacets.getSourceCodeLocation().getTag(), "v1.2.3");
        assertEquals(jobFacets.getSourceCodeLocation().getBranch(), "main");
        assertEquals(jobFacets.getSourceCodeLocation().getPullRequestNumber(), "42");
        assertEquals(jobFacets.getSourceCode().getLanguage(), "python");
        assertEquals(jobFacets.getSourceCode().getSourceCode(), "def count(): pass");
        assertEquals(jobFacets.getJobType().getProcessingType(), "BATCH");
        assertEquals(jobFacets.getJobType().getIntegration(), "AIRFLOW");
        assertEquals(jobFacets.getJobType().getJobType(), "TASK");
        assertEquals(jobFacets.getJobType().getEmissionPattern().getEventTrigger(), "TASK");
        assertEquals(jobFacets.getJobType().getEmissionPattern().getWindowDuration(), Long.valueOf(60));
        assertEquals(jobFacets.getOwnership().getOwners().get(0).getName(), "team:data-platform");
        assertEquals(jobFacets.getOwnership().getOwners().get(0).getType(), "MAINTAINER");
        assertEquals(jobFacets.getTags().getTags().get(0).getValue(), "sales");

        assertEquals(jobFacets.getGcpComposerJob().getDagId(), "orders_etl");
        assertEquals(jobFacets.getGcpComposerJob().getTaskId(), "count_orders");
        assertEquals(jobFacets.getGcpComposerJob().getComposerVersion(), "3.0.1");
        assertEquals(jobFacets.getGcpLineage().getDisplayName(), "Count orders");
        assertEquals(jobFacets.getGcpLineage().getOrigin().getSourceType(), "COMPOSER");

        List<OpenLineageLineageEntry> entries = jobFacets.getLineage().getEntries();
        assertEquals(entries.size(), 2);
        assertEquals(entries.get(0).getType(), "DATASET");
        assertEquals(entries.get(0).getName(), "sales.public.order_counts");
        assertEquals(entries.get(0).getInputs().get(0).getTransformations().get(0).getSubtype(), "AGGREGATION");
        assertEquals(entries.get(0).getInputs().get(0).getTransformations().get(0).getMasking(), Boolean.FALSE);
        assertEquals(entries.get(0).getFields().get("order_count").getInputs().get(0).getField(), "id");
        assertEquals(entries.get(1).getType(), "JOB");
        assertEquals(entries.get(1).getRunId(), UUID.fromString("d46e465b-d358-4d32-83d4-df660ff614dd"));
        assertEquals(entries.get(1).getInputs().get(0).getType(), "JOB");
        assertEquals(entries.get(1).getInputs().get(0).getName(), "orders_etl.extract");

        /*
         * Input dataset
         */
        assertEquals(event.getInputs().size(), 1);
        OpenLineageInputDataSet input = event.getInputs().get(0);
        assertEquals(input.getNamespace(), "postgres://db.example.com:5432");
        assertEquals(input.getName(), "sales.public.orders");
        OpenLineageDataSetFacets inputFacets = input.getFacets();

        assertEquals(inputFacets.getDocumentation().getDescription(), "Orders table");
        assertEquals(inputFacets.getDocumentation().getContentType(), "text/plain");
        assertEquals(inputFacets.getDataSource().getName(), "postgres://db.example.com:5432");
        assertEquals(inputFacets.getDataSource().getUri(), URI.create("postgres://db.example.com:5432/sales"));

        List<OpenLineageSchemaDataSetFacetField> fields = inputFacets.getSchema().getFields();
        assertEquals(fields.size(), 2);
        assertEquals(fields.get(0).getName(), "id");
        assertEquals(fields.get(0).getOrdinalPosition(), Long.valueOf(1));
        assertEquals(fields.get(1).getType(), "STRUCT");
        assertEquals(fields.get(1).getFields().size(), 2);
        assertEquals(fields.get(1).getFields().get(1).getName(), "email");
        assertEquals(fields.get(1).getFields().get(1).getOrdinalPosition(), Long.valueOf(2));

        assertEquals(inputFacets.getCatalog().getFramework(), "iceberg");
        assertEquals(inputFacets.getCatalog().getType(), "rest");
        assertEquals(inputFacets.getCatalog().getName(), "lakehouse");
        assertEquals(inputFacets.getCatalog().getMetadataUri(), "https://catalog.example.com");
        assertEquals(inputFacets.getCatalog().getWarehouseUri(), "s3://warehouse");
        assertEquals(inputFacets.getCatalog().getSource(), "spark");
        assertEquals(inputFacets.getCatalog().getCatalogProperties().get("region"), "eu-west-1");
        assertEquals(inputFacets.getDatasetType().getDatasetType(), "TABLE");
        assertEquals(inputFacets.getDatasetType().getSubType(), "EXTERNAL");
        assertEquals(inputFacets.getVersion().getDatasetVersion(), "7");
        assertEquals(inputFacets.getOwnership().getOwners().get(0).getName(), "user:jdoe");
        assertEquals(inputFacets.getStorage().getStorageLayer(), "iceberg");
        assertEquals(inputFacets.getStorage().getFileFormat(), "parquet");
        assertEquals(inputFacets.getSymlinks().getIdentifiers().get(0).getNamespace(), "s3://warehouse");
        assertEquals(inputFacets.getSymlinks().getIdentifiers().get(0).getType(), "LOCATION");
        assertEquals(inputFacets.getTags().getTags().get(0).getKey(), "pii");
        assertEquals(inputFacets.getTags().getTags().get(0).getField(), "customer.email");
        assertEquals(inputFacets.getHierarchy().getHierarchy().size(), 3);
        assertEquals(inputFacets.getHierarchy().getHierarchy().get(2).getType(), "TABLE");
        assertEquals(inputFacets.getHierarchy().getHierarchy().get(2).getName(), "orders");
        assertEquals(inputFacets.getDataQualityMetrics().getRowCount(), Long.valueOf(1000000));
        assertEquals(inputFacets.getDataQualityMetrics().getBytes(), Long.valueOf(123456789));
        assertEquals(inputFacets.getDataQualityMetrics().getFileCount(), Long.valueOf(12));
        assertEquals(inputFacets.getDataQualityMetrics().getLastUpdated(), "2026-09-08T12:00:00Z");
        assertEquals(inputFacets.getDataQualityMetrics().getColumnMetrics().get("id").getDistinctCount(), Long.valueOf(1000000));
        assertNull(inputFacets.getColumnLineage());

        /* custom dataset facet retained */
        OpenLineageDataSetFacet customFacet = inputFacets.getAdditionalProperties().get("customDatasetFacet");
        assertNotNull(customFacet);
        assertTrue(customFacet.getAdditionalProperties().get("anything") instanceof List);

        OpenLineageInputDataSetInputFacets inputInputFacets = input.getInputFacets();

        List<OpenLineageDataQualityAssertionsInputDataSetFacetAssertions> assertions = inputInputFacets.getDataQualityAssertions().getAssertions();
        assertEquals(assertions.size(), 2);
        assertEquals(assertions.get(0).getAssertion(), "not_null");
        assertTrue(assertions.get(0).isSuccess());
        assertEquals(assertions.get(0).getColumn(), "id");
        assertEquals(assertions.get(0).getSeverity(), "error");
        assertEquals(assertions.get(0).getName(), "id_not_null");
        assertEquals(assertions.get(0).getDescription(), "id must be set");
        assertEquals(assertions.get(0).getExpected(), "0");
        assertEquals(assertions.get(0).getActual(), "0");
        assertEquals(assertions.get(0).getContent(), "select count(*) from orders where id is null");
        assertEquals(assertions.get(0).getContentType(), "text/x-sql");
        assertEquals(assertions.get(0).getParams().get("threshold"), 0);
        assertEquals(assertions.get(1).getAssertion(), "row_count_gt");
        assertTrue(! assertions.get(1).isSuccess());

        OpenLineageDataQualityMetricsInputDataSetFacet inputMetrics = inputInputFacets.getDataQualityMetrics();
        assertEquals(inputMetrics.getRowCount(), Long.valueOf(500));
        assertEquals(inputMetrics.getBytes(), Long.valueOf(65536));
        assertEquals(inputMetrics.getFileCount(), Long.valueOf(1));
        assertEquals(inputMetrics.getLastUpdated(), "2026-09-08T12:00:00Z");
        OpenLineageDataQualityMetricsColumnMetrics idMetrics = inputMetrics.getColumnMetrics().get("id");
        assertEquals(idMetrics.getNullCount(), Long.valueOf(0));
        assertEquals(idMetrics.getDistinctCount(), Long.valueOf(500));
        assertEquals(idMetrics.getSum(), 125250.0);
        assertEquals(idMetrics.getCount(), 500.0);
        assertEquals(idMetrics.getMin(), 1.0);
        assertEquals(idMetrics.getMax(), 500.0);
        assertEquals(idMetrics.getQuantiles().get("0.5"), 250.5);
        assertEquals(idMetrics.getQuantiles().get("0.9"), 450.0);

        assertEquals(inputInputFacets.getInputStatistics().getRowCount(), Long.valueOf(500));
        assertEquals(inputInputFacets.getInputStatistics().getSize(), Long.valueOf(65536));
        assertEquals(inputInputFacets.getInputStatistics().getFileCount(), Long.valueOf(1));
        assertEquals(inputInputFacets.getSubset().getInputCondition().get("type"), "partition");

        OpenLineageIcebergScanReportInputDataSetFacet scanReport = inputInputFacets.getIcebergScanReport();
        assertEquals(scanReport.getSnapshotId(), Long.valueOf(7791));
        assertEquals(scanReport.getFilterDescription(), "status = 'OPEN'");
        assertEquals(scanReport.getSchemaId(), Long.valueOf(2));
        assertEquals(scanReport.getProjectedFieldNames(), List.of("id", "status"));
        assertEquals(scanReport.getScanMetrics().getTotalPlanningDuration(), 12.5);
        assertEquals(scanReport.getScanMetrics().getSkippedDataFiles(), 9.0);
        assertEquals(scanReport.getMetadata().get("engine"), "spark");

        /*
         * Output dataset
         */
        assertEquals(event.getOutputs().size(), 1);
        OpenLineageOutputDataSet output = event.getOutputs().get(0);
        assertEquals(output.getName(), "sales.public.order_counts");
        OpenLineageDataSetFacets outputFacets = output.getFacets();

        assertEquals(outputFacets.getSchema().getFields().get(0).getName(), "order_count");
        assertNull(outputFacets.getSchema().getFields().get(0).getOrdinalPosition());

        OpenLineageColumnLineageDataSetFacetField columnLineage = outputFacets.getColumnLineage().getFields().get("order_count");
        assertEquals(columnLineage.getInputFields().get(0).getName(), "sales.public.orders");
        assertEquals(columnLineage.getInputFields().get(0).getField(), "id");
        assertEquals(columnLineage.getInputFields().get(0).getTransformations().get(0).getType(), "INDIRECT");
        assertEquals(columnLineage.getInputFields().get(0).getTransformations().get(0).getDescription(), "count(id)");
        assertEquals(columnLineage.getTransformationDescription(), "count");
        assertEquals(columnLineage.getTransformationType(), "IDENTITY");
        assertEquals(outputFacets.getColumnLineage().getDataset().get(0).getField(), "status");
        assertEquals(outputFacets.getColumnLineage().getDataset().get(0).getTransformations().get(0).getSubtype(), "FILTER");

        assertEquals(outputFacets.getLifecycleStateChange().getLifecycleStateChange(), "RENAME");
        assertEquals(outputFacets.getLifecycleStateChange().getPreviousIdentifier().getName(), "sales.public.order_count");
        assertEquals(outputFacets.getLineage().getInputs().get(0).getName(), "sales.public.orders");
        assertEquals(outputFacets.getLineage().getFields().get("order_count").getInputs().get(0).getField(), "id");

        OpenLineageOutputDataSetOutputFacets outputOutputFacets = output.getOutputFacets();
        assertEquals(outputOutputFacets.getOutputStatistics().getRowCount(), Long.valueOf(1));
        assertEquals(outputOutputFacets.getOutputStatistics().getSize(), Long.valueOf(8));
        assertEquals(outputOutputFacets.getOutputStatistics().getFileCount(), Long.valueOf(1));
        assertEquals(outputOutputFacets.getSubset().getOutputCondition().get("type"), "location");

        OpenLineageIcebergCommitReportOutputDataSetFacet commitReport = outputOutputFacets.getIcebergCommitReport();
        assertEquals(commitReport.getSnapshotId(), Long.valueOf(7792));
        assertEquals(commitReport.getSequenceNumber(), Long.valueOf(15));
        assertEquals(commitReport.getOperation(), "append");
        assertEquals(commitReport.getCommitMetrics().getTotalDuration(), 340.0);
        assertEquals(commitReport.getCommitMetrics().getTotalRecords(), 1500.0);
        assertNull(commitReport.getCommitMetrics().getRemovedRecords());

        /*
         * Round trip: what we write must parse back to an equal bean, and must be structurally identical
         * to the original JSON (nothing dropped, nothing invented).
         */
        String   written   = OBJECT_MAPPER.writeValueAsString(event);
        OpenLineageRunEvent roundTrip = OBJECT_MAPPER.readValue(written, OpenLineageRunEvent.class);

        assertEquals(roundTrip, event);

        JsonNode originalTree = OBJECT_MAPPER.readTree(json);
        JsonNode writtenTree  = OBJECT_MAPPER.readTree(written);

        assertTrue(writtenTree.equals(originalTree), "JSON changed on round trip:\n" + written);
    }


    /**
     * The minimal run event required by the spec - no facets at all - parses and round-trips.
     *
     * @throws Exception parsing problem
     */
    @Test
    public void testMinimalRunEvent() throws Exception
    {
        String json = "{\"eventTime\":\"2026-09-08T14:00:00Z\",\"eventType\":\"START\",\"producer\":\"https://example.com/producer\"," +
                      "\"schemaURL\":\"https://openlineage.io/spec/2-0-2/OpenLineage.json#/$defs/RunEvent\"," +
                      "\"run\":{\"runId\":\"d46e465b-d358-4d32-83d4-df660ff614dd\"},\"job\":{\"namespace\":\"ns\",\"name\":\"job\"}}";

        OpenLineageRunEvent event = OBJECT_MAPPER.readValue(json, OpenLineageRunEvent.class);

        assertEquals(event.getEventType(), "START");
        assertNull(event.getRun().getFacets());
        assertNull(event.getJob().getFacets());
        assertNull(event.getInputs());
        assertNull(event.getOutputs());

        assertTrue(OBJECT_MAPPER.readTree(OBJECT_MAPPER.writeValueAsString(event)).equals(OBJECT_MAPPER.readTree(json)));
    }


    /**
     * A job event (spec 2-0-x) parses into its own bean.
     *
     * @throws Exception problem reading the resource or parsing the event
     */
    @Test
    public void testJobEvent() throws Exception
    {
        String json = readResource("openlineage/job-event.json");

        OpenLineageJobEvent event = OBJECT_MAPPER.readValue(json, OpenLineageJobEvent.class);

        assertEquals(event.getSchemaURL(), URI.create("https://openlineage.io/spec/2-0-2/OpenLineage.json#/$defs/JobEvent"));
        assertEquals(event.getJob().getName(), "orders_etl.count_orders");
        assertEquals(event.getJob().getFacets().getDocumentation().getDescription(), "Counts the orders");
        assertEquals(event.getInputs().get(0).getName(), "sales.public.orders");
        assertEquals(event.getOutputs().get(0).getName(), "sales.public.order_counts");

        assertTrue(OBJECT_MAPPER.readTree(OBJECT_MAPPER.writeValueAsString(event)).equals(OBJECT_MAPPER.readTree(json)));
    }


    /**
     * A dataset event (spec 2-0-x) parses into its own bean.
     *
     * @throws Exception problem reading the resource or parsing the event
     */
    @Test
    public void testDataSetEvent() throws Exception
    {
        String json = readResource("openlineage/dataset-event.json");

        OpenLineageDataSetEvent event = OBJECT_MAPPER.readValue(json, OpenLineageDataSetEvent.class);

        assertEquals(event.getSchemaURL(), URI.create("https://openlineage.io/spec/2-0-2/OpenLineage.json#/$defs/DatasetEvent"));
        assertEquals(event.getDataset().getNamespace(), "postgres://db.example.com:5432");
        assertEquals(event.getDataset().getFacets().getSchema().getFields().get(0).getName(), "id");
        assertEquals(event.getDataset().getFacets().getVersion().getDatasetVersion(), "8");

        assertTrue(OBJECT_MAPPER.readTree(OBJECT_MAPPER.writeValueAsString(event)).equals(OBJECT_MAPPER.readTree(json)));
    }


    /**
     * Facets built programmatically (as the governance action open lineage connector does) serialize with the
     * spec's property names, including the ones that are not valid Java identifiers.
     *
     * @throws Exception parsing problem
     */
    @Test
    public void testSpecPropertyNames() throws Exception
    {
        OpenLineageRunFacets runFacets = new OpenLineageRunFacets();
        OpenLineageProcessingEngineRunFacet processingEngine = new OpenLineageProcessingEngineRunFacet();
        processingEngine.setName("Spark");
        runFacets.setProcessingEngine(processingEngine);

        OpenLineageJobDependenciesRunFacet jobDependencies = new OpenLineageJobDependenciesRunFacet();
        jobDependencies.setTriggerRule("all_done");
        runFacets.setJobDependencies(jobDependencies);

        String json = OBJECT_MAPPER.writeValueAsString(runFacets);

        assertTrue(json.contains("\"processing_engine\""), json);
        assertTrue(json.contains("\"trigger_rule\""), json);
        assertTrue(json.contains("\"_schemaURL\":\"https://openlineage.io/spec/facets/1-1-1/ProcessingEngineRunFacet.json#/$defs/ProcessingEngineRunFacet\""), json);
        assertTrue(! json.contains("processingEngine"), json);

        OpenLineageSchemaDataSetFacetField field = new OpenLineageSchemaDataSetFacetField();
        field.setOrdinalPosition(3L);

        assertTrue(OBJECT_MAPPER.writeValueAsString(field).contains("\"ordinal_position\":3"));
    }
}
