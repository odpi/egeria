/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openlineagefvt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageRunEvent;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SchemaAttributeClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SchemaTypeClient;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.ProcessProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.RunMetricsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.OwnershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Publishes run events for a small pipeline through the integration daemon's REST API and checks what the Open
 * Lineage Cataloguer made of them: the process, the data assets and their types, the lineage relationships, the
 * schema, the run metrics, the data scope, the data quality survey report, and the handling of a rename and a drop.
 * The tests build on each other's events, so they run in order.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OpenLineageCataloguerFVT
{
    private static final String PROCESS_QN        = OpenLineageFvtTestSupport.processQualifiedName(OpenLineageEventFactory.JOB_NAMESPACE, OpenLineageEventFactory.JOB_NAME);
    private static final String PARENT_PROCESS_QN = OpenLineageFvtTestSupport.processQualifiedName(OpenLineageEventFactory.JOB_NAMESPACE, OpenLineageEventFactory.PARENT_JOB_NAME);
    private static final String ORDERS_QN         = OpenLineageFvtTestSupport.assetQualifiedName(OpenMetadataType.TABULAR_DATA_SET.typeName, OpenLineageEventFactory.TABLE_NAMESPACE, OpenLineageEventFactory.ORDERS_TABLE);
    private static final String COUNTS_QN         = OpenLineageFvtTestSupport.assetQualifiedName(OpenMetadataType.TABULAR_DATA_SET.typeName, OpenLineageEventFactory.TABLE_NAMESPACE, OpenLineageEventFactory.COUNTS_TABLE);
    private static final String RENAMED_COUNTS_QN = OpenLineageFvtTestSupport.assetQualifiedName(OpenMetadataType.TABULAR_DATA_SET.typeName, OpenLineageEventFactory.TABLE_NAMESPACE, OpenLineageEventFactory.RENAMED_COUNTS_TABLE);
    private static final String LANDING_FILE_QN   = OpenLineageFvtTestSupport.assetQualifiedName(OpenMetadataType.DATA_FILE.typeName, OpenLineageEventFactory.FILE_NAMESPACE, OpenLineageEventFactory.LANDING_FILE);

    private static final UUID FIRST_RUN  = UUID.randomUUID();
    private static final UUID SECOND_RUN = UUID.randomUUID();
    private static final UUID FAILED_RUN = UUID.randomUUID();


    /**
     * Publish a START and a COMPLETE event for one run and check the structure the cataloguer builds.
     *
     * @throws Exception test failure
     */
    @Test
    @Order(1)
    @DisplayName("A completed run is catalogued as a process, its data assets and the lineage between them")
    void completedRunIsCatalogued() throws Exception
    {
        Instant now = Instant.now();

        OpenLineageRunEvent start = OpenLineageEventFactory.runEvent("START", now.minusSeconds(60), FIRST_RUN, OpenLineageEventFactory.JOB_NAME);
        OpenLineageEventFactory.withInputs(start, false);
        OpenLineageEventFactory.withOutput(start, OpenLineageEventFactory.COUNTS_TABLE, null);
        OpenLineageFvtTestSupport.publish(start);

        OpenLineageRunEvent complete = OpenLineageEventFactory.runEvent("COMPLETE", now, FIRST_RUN, OpenLineageEventFactory.JOB_NAME);
        OpenLineageEventFactory.withInputs(complete, true);
        OpenLineageEventFactory.withOutput(complete, OpenLineageEventFactory.COUNTS_TABLE, 31L);
        OpenLineageFvtTestSupport.publish(complete);

        /*
         * The process, with its facets mapped.
         */
        OpenMetadataRootElement process = OpenLineageFvtTestSupport.waitForAsset(PROCESS_QN, "the job's process to be catalogued");

        assertTrue(process.getProperties() instanceof ProcessProperties, "The job should be catalogued as a process but its properties are " + process.getProperties());

        ProcessProperties processProperties = (ProcessProperties) process.getProperties();

        assertEquals(OpenLineageEventFactory.JOB_DESCRIPTION, processProperties.getDescription(), "The documentation facet should become the process description");
        assertEquals(OpenLineageEventFactory.JOB_NAME, processProperties.getResourceName(), "The job name should be the process's resourceName so that other connectors can match it");
        assertEquals(OpenLineageEventFactory.JOB_NAMESPACE, processProperties.getNamespacePath(), "The job namespace should be the process's namespacePath");
        assertNotNull(processProperties.getFormula(), "The sql facet should become the process formula");
        assertEquals("SQL:postgres", processProperties.getFormulaType(), "The sql dialect should be recorded in the formula type");

        assertNotNull(process.getElementHeader().getOwnership(), "The ownership facet should become an Ownership classification on the process");
        assertTrue(process.getElementHeader().getOwnership().getClassificationProperties() instanceof OwnershipProperties, "Ownership classification properties should be an OwnershipProperties bean");
        OwnershipProperties ownership = (OwnershipProperties) process.getElementHeader().getOwnership().getClassificationProperties();
        assertNotNull(ownership.getAdditionalProperties(), "The OpenLineage owner name and type should be recorded in the Ownership classification's additional properties");
        assertEquals(OpenLineageEventFactory.JOB_OWNER, ownership.getAdditionalProperties().get("openLineageOwnerName"), "The OpenLineage owner name should be preserved");

        /*
         * The parent job, owning the job.
         */
        OpenMetadataRootElement parentProcess = OpenLineageFvtTestSupport.waitForAsset(PARENT_PROCESS_QN, "the parent job's process to be catalogued");

        assertTrue(OpenLineageFvtTestSupport.areLinked(parentProcess.getElementHeader().getGUID(), process.getElementHeader().getGUID(), OpenMetadataType.PROCESS_HIERARCHY_RELATIONSHIP.typeName),
                   "The parent run facet should link the parent job's process to the job's process with ProcessHierarchy");

        /*
         * The data assets, typed from their namespaces.
         */
        OpenMetadataRootElement orders  = OpenLineageFvtTestSupport.waitForAsset(ORDERS_QN, "the orders table to be catalogued");
        OpenMetadataRootElement landing = OpenLineageFvtTestSupport.waitForAsset(LANDING_FILE_QN, "the landing file to be catalogued");
        OpenMetadataRootElement counts  = OpenLineageFvtTestSupport.waitForAsset(COUNTS_QN, "the order counts table to be catalogued");

        assertEquals(OpenMetadataType.TABULAR_DATA_SET.typeName, orders.getElementHeader().getType().getTypeName(), "A postgres:// dataset should be a TabularDataSet");
        assertEquals(OpenMetadataType.DATA_FILE.typeName, landing.getElementHeader().getType().getTypeName(), "A file dataset should be a DataFile");
        assertEquals(OpenLineageEventFactory.ORDERS_DESCRIPTION, ((AssetProperties) orders.getProperties()).getDescription(), "The documentation facet should become the asset description");
        assertEquals(OpenLineageEventFactory.ORDERS_TABLE, ((AssetProperties) orders.getProperties()).getResourceName(), "The dataset name should be the asset's resourceName");
        assertEquals(OpenLineageEventFactory.TABLE_NAMESPACE, ((AssetProperties) orders.getProperties()).getNamespacePath(), "The dataset namespace should be the asset's namespacePath");

        /*
         * Lineage: inputs -> process -> output.
         */
        assertTrue(OpenLineageFvtTestSupport.areLinked(orders.getElementHeader().getGUID(), process.getElementHeader().getGUID(), OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName),
                   "The orders table should flow into the process");
        assertTrue(OpenLineageFvtTestSupport.areLinked(landing.getElementHeader().getGUID(), process.getElementHeader().getGUID(), OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName),
                   "The landing file should flow into the process");
        assertTrue(OpenLineageFvtTestSupport.areLinked(process.getElementHeader().getGUID(), counts.getElementHeader().getGUID(), OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName),
                   "The process should flow into the order counts table");

        /*
         * The schema facet becomes a tabular schema with a column per field.
         */
        ConnectorContextBase  context               = ConnectorContextFactory.newContext();
        SchemaTypeClient      schemaTypeClient      = context.getSchemaTypeClient();
        SchemaAttributeClient schemaAttributeClient = context.getSchemaAttributeClient();

        OpenMetadataRootElement schemaType = schemaTypeClient.getSchemaTypeForAsset(orders.getElementHeader().getGUID(), schemaTypeClient.getGetOptions());

        assertNotNull(schemaType, "The schema facet should create a schema type for the orders table");

        List<OpenMetadataRootElement> columns = schemaAttributeClient.getAttributesForSchemaType(schemaType.getElementHeader().getGUID(), schemaAttributeClient.getQueryOptions());

        assertNotNull(columns, "The schema type should have columns");
        List<String> columnNames = new ArrayList<>();
        for (OpenMetadataRootElement column : columns)
        {
            columnNames.add(((ReferenceableProperties) column.getProperties()).getDisplayName());
        }
        assertTrue(columnNames.containsAll(OpenLineageEventFactory.ORDERS_COLUMNS), "Expected columns " + OpenLineageEventFactory.ORDERS_COLUMNS + " but found " + columnNames);

        /*
         * Run metrics on the process.
         */
        OpenMetadataRootElement measuredProcess = OpenLineageFvtTestSupport.waitForAsset(PROCESS_QN, "the run metrics to record the completed run", element ->
                (element.getElementHeader().getRunMetrics() != null) &&
                (element.getElementHeader().getRunMetrics().getClassificationProperties() instanceof RunMetricsProperties metrics) &&
                ("COMPLETE".equals(metrics.getLastRunStatus())));

        RunMetricsProperties runMetrics = (RunMetricsProperties) measuredProcess.getElementHeader().getRunMetrics().getClassificationProperties();

        assertEquals(1, runMetrics.getRunCount(), "One run should have been counted");
        assertEquals(0, runMetrics.getFailedRunCount(), "No run should have failed");
        assertEquals(FIRST_RUN.toString(), runMetrics.getLastRunId(), "The last run id should be the run just completed");
        assertEquals(31, runMetrics.getLastRunRowsWritten(), "The output statistics should be recorded as rows written");
        assertEquals(31, runMetrics.getTotalRowsWritten(), "The total rows written should include the first run");
        assertNotNull(runMetrics.getLastRunStartTime(), "The START event should set the last run start time");
        assertNotNull(runMetrics.getLastRunEndTime(), "The COMPLETE event should set the last run end time");
        assertTrue(runMetrics.getLastRunDuration() > 0, "The run duration should be derived from the START and COMPLETE events");

        /*
         * Data scope on the output.
         */
        OpenMetadataRootElement scopedCounts = OpenLineageFvtTestSupport.waitForAsset(COUNTS_QN, "the data scope to record the write", element ->
                (element.getElementHeader().getDataScope() != null) &&
                (element.getElementHeader().getDataScope().getClassificationProperties() instanceof DataScopeProperties));

        DataScopeProperties dataScope = (DataScopeProperties) scopedCounts.getElementHeader().getDataScope().getClassificationProperties();

        assertNotNull(dataScope.getDataCollectionStartTime(), "The first write should set the data collection start time");
        assertNotNull(dataScope.getDataCollectionEndTime(), "The write should set the data collection end time");
        assertNotNull(dataScope.getAdditionalProperties(), "The write statistics should be recorded in the data scope");
        assertEquals("1", dataScope.getAdditionalProperties().get("writeCount"), "One write should have been counted");
        assertEquals("31", dataScope.getAdditionalProperties().get("lastRowCount"), "The last row count should come from the output statistics");

        /*
         * Data quality: a survey report with the assertions, attached to the orders table.
         */
        OpenLineageFvtTestSupport.waitFor("the data quality assertions to be recorded in a survey report on the orders table", () ->
        {
            for (RelatedMetadataElement report : OpenLineageFvtTestSupport.getRelatedElements(orders.getElementHeader().getGUID(), 1, OpenMetadataType.REPORT_SUBJECT_RELATIONSHIP.typeName))
            {
                if ((report.getElement() != null) && (OpenMetadataType.SURVEY_REPORT.typeName.equals(report.getElement().getType().getTypeName())))
                {
                    List<RelatedMetadataElement> annotations = OpenLineageFvtTestSupport.getRelatedElements(report.getElement().getElementGUID(), 1, OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName);

                    int qualityAnnotations = 0;

                    for (RelatedMetadataElement annotation : annotations)
                    {
                        if ((annotation.getElement() != null) && (OpenMetadataType.QUALITY_ANNOTATION.typeName.equals(annotation.getElement().getType().getTypeName())))
                        {
                            qualityAnnotations++;
                        }
                    }

                    if (qualityAnnotations >= 2)
                    {
                        return true;
                    }
                }
            }

            return false;
        });
    }


    /**
     * A second run accumulates into the run metrics.
     *
     * @throws Exception test failure
     */
    @Test
    @Order(2)
    @DisplayName("A second run accumulates in the process's run metrics")
    void secondRunAccumulatesRunMetrics() throws Exception
    {
        OpenLineageFvtTestSupport.waitForAsset(PROCESS_QN, "the first run to be catalogued", element -> element.getElementHeader().getRunMetrics() != null);

        Instant now = Instant.now();

        OpenLineageRunEvent start = OpenLineageEventFactory.runEvent("START", now.minusSeconds(30), SECOND_RUN, OpenLineageEventFactory.JOB_NAME);
        OpenLineageEventFactory.withInputs(start, false);
        OpenLineageFvtTestSupport.publish(start);

        OpenLineageRunEvent complete = OpenLineageEventFactory.runEvent("COMPLETE", now, SECOND_RUN, OpenLineageEventFactory.JOB_NAME);
        OpenLineageEventFactory.withInputs(complete, false);
        OpenLineageEventFactory.withOutput(complete, OpenLineageEventFactory.COUNTS_TABLE, 40L);
        OpenLineageFvtTestSupport.publish(complete);

        OpenMetadataRootElement process = OpenLineageFvtTestSupport.waitForAsset(PROCESS_QN, "the run metrics to record the second run", element ->
                (element.getElementHeader().getRunMetrics() != null) &&
                (element.getElementHeader().getRunMetrics().getClassificationProperties() instanceof RunMetricsProperties metrics) &&
                (SECOND_RUN.toString().equals(metrics.getLastRunId())) &&
                ("COMPLETE".equals(metrics.getLastRunStatus())));

        RunMetricsProperties runMetrics = (RunMetricsProperties) process.getElementHeader().getRunMetrics().getClassificationProperties();

        assertEquals(2, runMetrics.getRunCount(), "Two runs should have been counted");
        assertEquals(40, runMetrics.getLastRunRowsWritten(), "The last run's rows written should be from the second run");
        assertEquals(71, runMetrics.getTotalRowsWritten(), "The total rows written should be the sum of both runs");

        OpenMetadataRootElement counts = OpenLineageFvtTestSupport.waitForAsset(COUNTS_QN, "the data scope to record the second write", element ->
                (element.getElementHeader().getDataScope() != null) &&
                (element.getElementHeader().getDataScope().getClassificationProperties() instanceof DataScopeProperties scope) &&
                (scope.getAdditionalProperties() != null) &&
                ("2".equals(scope.getAdditionalProperties().get("writeCount"))));

        assertNotNull(counts, "The order counts table should record two writes");
    }


    /**
     * A failed run is counted as a failure.
     *
     * @throws Exception test failure
     */
    @Test
    @Order(3)
    @DisplayName("A failed run is counted in the process's run metrics")
    void failedRunIsCounted() throws Exception
    {
        OpenLineageFvtTestSupport.waitForAsset(PROCESS_QN, "the earlier runs to be catalogued", element -> element.getElementHeader().getRunMetrics() != null);

        Instant now = Instant.now();

        OpenLineageRunEvent start = OpenLineageEventFactory.runEvent("START", now.minusSeconds(10), FAILED_RUN, OpenLineageEventFactory.JOB_NAME);
        OpenLineageFvtTestSupport.publish(start);

        OpenLineageRunEvent fail = OpenLineageEventFactory.runEvent("FAIL", now, FAILED_RUN, OpenLineageEventFactory.JOB_NAME);
        OpenLineageFvtTestSupport.publish(fail);

        OpenMetadataRootElement process = OpenLineageFvtTestSupport.waitForAsset(PROCESS_QN, "the run metrics to record the failed run", element ->
                (element.getElementHeader().getRunMetrics() != null) &&
                (element.getElementHeader().getRunMetrics().getClassificationProperties() instanceof RunMetricsProperties metrics) &&
                ("FAIL".equals(metrics.getLastRunStatus())));

        RunMetricsProperties runMetrics = (RunMetricsProperties) process.getElementHeader().getRunMetrics().getClassificationProperties();

        assertEquals(3, runMetrics.getRunCount(), "Three runs should have been counted");
        assertEquals(1, runMetrics.getFailedRunCount(), "One run should have failed");
    }


    /**
     * A RENAME lifecycle change renames the asset in place.
     *
     * @throws Exception test failure
     */
    @Test
    @Order(4)
    @DisplayName("A RENAME lifecycle change renames the data asset in place")
    void renameUpdatesTheAsset() throws Exception
    {
        OpenMetadataRootElement counts = OpenLineageFvtTestSupport.waitForAsset(COUNTS_QN, "the order counts table to exist before the rename");
        String                  guid   = counts.getElementHeader().getGUID();

        UUID   runId = UUID.randomUUID();
        Instant now  = Instant.now();

        OpenLineageRunEvent complete = OpenLineageEventFactory.runEvent("COMPLETE", now, runId, OpenLineageEventFactory.JOB_NAME);
        OpenLineageEventFactory.withLifecycleChange(complete, OpenLineageEventFactory.RENAMED_COUNTS_TABLE, "RENAME", OpenLineageEventFactory.COUNTS_TABLE);
        OpenLineageFvtTestSupport.publish(complete);

        OpenMetadataRootElement renamed = OpenLineageFvtTestSupport.waitForAsset(RENAMED_COUNTS_QN, "the order counts table to be renamed");

        assertEquals(guid, renamed.getElementHeader().getGUID(), "The rename should update the existing asset rather than create a new one");
        assertEquals(OpenLineageEventFactory.RENAMED_COUNTS_TABLE, ((AssetProperties) renamed.getProperties()).getResourceName(), "The resourceName should carry the new name");
        assertNull(OpenLineageFvtTestSupport.getAsset(COUNTS_QN), "The old qualified name should no longer resolve after the rename");
    }


    /**
     * A DROP lifecycle change removes the asset.
     *
     * @throws Exception test failure
     */
    @Test
    @Order(5)
    @DisplayName("A DROP lifecycle change archives or deletes the data asset")
    void dropRemovesTheAsset() throws Exception
    {
        OpenLineageFvtTestSupport.waitForAsset(RENAMED_COUNTS_QN, "the renamed order counts table to exist before the drop");

        UUID    runId = UUID.randomUUID();
        Instant now   = Instant.now();

        OpenLineageRunEvent complete = OpenLineageEventFactory.runEvent("COMPLETE", now, runId, OpenLineageEventFactory.JOB_NAME);
        OpenLineageEventFactory.withLifecycleChange(complete, OpenLineageEventFactory.RENAMED_COUNTS_TABLE, "DROP", null);
        OpenLineageFvtTestSupport.publish(complete);

        OpenLineageFvtTestSupport.waitForAssetToGo(RENAMED_COUNTS_QN, "the dropped table's asset to be removed");
    }
}
