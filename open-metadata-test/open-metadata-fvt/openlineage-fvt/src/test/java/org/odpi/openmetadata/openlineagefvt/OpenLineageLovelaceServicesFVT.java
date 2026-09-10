/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openlineagefvt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.GovernanceActionTypeDefinition;
import org.odpi.openmetadata.contentpacks.core.GovernanceEngineDefinition;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageRunEvent;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.RunMetricsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.io.File;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Drives the three Open Lineage Lovelace services against the file-based log store that the file publisher fills
 * as events arrive.  The test publishes three runs of its own job an hour apart, waits for the cataloguer and the
 * file publisher to process them, starts each service as an engine action on the Egeria Governance Engine with
 * the log store directory as a request parameter, and checks what each wrote back: the run profile in the
 * process's RunMetrics, the write pattern in the output's DataScope, and the data quality summary report on the
 * input.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class OpenLineageLovelaceServicesFVT
{
    private static final String JOB_NAME       = "orders_etl.load_customers";
    private static final String INPUT_TABLE    = "sales.public.customer_events";
    private static final String OUTPUT_TABLE   = "sales.public.customers";
    private static final int    RUN_COUNT      = 3;
    private static final long   RUN_INTERVAL   = 60 * 60;   // seconds
    private static final long   RUN_DURATION   = 5 * 60;    // seconds

    private static final File LOG_STORE = new File("logs/openlineage");

    private static final String SUMMARY_ANNOTATION_TYPE = "OpenLineage Data Quality Summary";


    /**
     * Publish the runs, run the services, check their output.
     *
     * @throws Exception test failure
     */
    @Test
    @DisplayName("The Lovelace services profile the runs, refine the data scope and summarise the data quality from the log store")
    void lovelaceServicesAnalyseTheLogStore() throws Exception
    {
        String processQN = OpenLineageFvtTestSupport.processQualifiedName(OpenLineageEventFactory.JOB_NAMESPACE, JOB_NAME);
        String inputQN   = OpenLineageFvtTestSupport.assetQualifiedName(OpenMetadataType.TABULAR_DATA_SET.typeName, OpenLineageEventFactory.TABLE_NAMESPACE, INPUT_TABLE);
        String outputQN  = OpenLineageFvtTestSupport.assetQualifiedName(OpenMetadataType.TABULAR_DATA_SET.typeName, OpenLineageEventFactory.TABLE_NAMESPACE, OUTPUT_TABLE);

        /*
         * Three completed runs, an hour apart, the last one finishing a few minutes ago.
         */
        Instant now       = Instant.now();
        UUID    lastRunId = null;

        for (int run = 0; run < RUN_COUNT; run++)
        {
            UUID    runId     = UUID.randomUUID();
            Instant startTime = now.minusSeconds((RUN_COUNT - run) * RUN_INTERVAL);
            Instant endTime   = startTime.plusSeconds(RUN_DURATION);

            OpenLineageRunEvent start = OpenLineageEventFactory.runEvent("START", startTime, runId, JOB_NAME);
            addInput(start, false);
            addOutput(start, null);
            OpenLineageFvtTestSupport.publish(start);

            OpenLineageRunEvent complete = OpenLineageEventFactory.runEvent("COMPLETE", endTime, runId, JOB_NAME);
            addInput(complete, true);
            addOutput(complete, 100L + run);
            OpenLineageFvtTestSupport.publish(complete);

            lastRunId = runId;
        }

        /*
         * Wait for the cataloguer (so the elements the services update exist) and for the file publisher (so the
         * log store holds the events the services analyse).
         */
        OpenLineageFvtTestSupport.waitForAsset(processQN, "the runs to be catalogued", element ->
                (element.getElementHeader().getRunMetrics() != null) &&
                (element.getElementHeader().getRunMetrics().getClassificationProperties() instanceof RunMetricsProperties metrics) &&
                (metrics.getRunCount() >= RUN_COUNT) &&
                ("COMPLETE".equals(metrics.getLastRunStatus())));
        OpenLineageFvtTestSupport.waitForAsset(inputQN, "the input table to be catalogued");
        OpenLineageFvtTestSupport.waitForAsset(outputQN, "the output table to be catalogued");

        final String lastRun = lastRunId.toString();

        OpenLineageFvtTestSupport.waitFor("the last run's COMPLETE event to be written to the log store", () -> findEventFile(LOG_STORE, lastRun, "COMPLETE") != null);

        /*
         * Run the three services.
         */
        Map<String, String> requestParameters = new HashMap<>();

        requestParameters.put("logStoreDirectory", LOG_STORE.getAbsolutePath());
        requestParameters.put("analysisWindowDays", "30");

        runService(GovernanceActionTypeDefinition.PROFILE_OPEN_LINEAGE_RUNS, requestParameters);
        runService(GovernanceActionTypeDefinition.REFINE_OPEN_LINEAGE_DATA_SCOPE, requestParameters);
        runService(GovernanceActionTypeDefinition.SUMMARISE_OPEN_LINEAGE_DATA_QUALITY, requestParameters);

        /*
         * Run profile.
         */
        OpenMetadataRootElement process = OpenLineageFvtTestSupport.getAsset(processQN);

        assertNotNull(process.getElementHeader().getRunMetrics(), "The process should still carry RunMetrics after the run profiler");
        RunMetricsProperties runMetrics = (RunMetricsProperties) process.getElementHeader().getRunMetrics().getClassificationProperties();
        Map<String, String>  profile    = runMetrics.getAdditionalProperties();

        assertNotNull(profile, "The run profiler should add its profile to the RunMetrics additional properties");
        assertEquals(Integer.toString(RUN_COUNT), profile.get("runsInWindow"), "Every run should be in the analysis window: " + profile);
        assertEquals(Integer.toString(RUN_COUNT), profile.get("completedRunsInWindow"), "Every run completed: " + profile);
        assertEquals("0", profile.get("failedRunsInWindow"), "No run failed: " + profile);
        assertEquals("HOURLY", profile.get("inferredSchedule"), "Runs an hour apart should be inferred as HOURLY: " + profile);
        assertEquals("REGULAR", profile.get("runIntervalRegularity"), "Evenly spaced runs are REGULAR: " + profile);
        assertNotNull(profile.get("meanRunDuration"), "The run durations should be profiled: " + profile);
        assertNotNull(profile.get("meanRowsWrittenPerRun"), "The output statistics should be profiled: " + profile);
        assertNotNull(profile.get("analysisService"), "The profile should record which service produced it: " + profile);
        assertEquals(RUN_COUNT, runMetrics.getRunCount(), "The run profiler must not disturb the counts the cataloguer keeps");

        /*
         * Data scope.
         */
        OpenMetadataRootElement output = OpenLineageFvtTestSupport.getAsset(outputQN);

        assertNotNull(output.getElementHeader().getDataScope(), "The output should carry DataScope after the data scope service");
        DataScopeProperties dataScope = (DataScopeProperties) output.getElementHeader().getDataScope().getClassificationProperties();
        Map<String, String> scope     = dataScope.getAdditionalProperties();

        assertNotNull(scope, "The data scope service should record the write pattern in the additional properties");
        assertEquals("APPENDED", scope.get("writePattern"), "Writes with no lifecycle or subset facet are APPENDED: " + scope);
        assertEquals(Integer.toString(RUN_COUNT), scope.get("writesInWindow"), "Every run wrote the output: " + scope);
        assertEquals("1", scope.get("writersInWindow"), "One job writes the output: " + scope);
        assertNotNull(dataScope.getDataCollectionStartTime(), "The collection start should be set from the writes");
        assertNotNull(dataScope.getDataCollectionEndTime(), "The collection end should be set from the writes");
        assertTrue(dataScope.getDataCollectionStartTime().before(dataScope.getDataCollectionEndTime()), "Three writes an hour apart span a window");

        /*
         * Data quality summary: a survey report on the input holding summary annotations.
         */
        OpenMetadataRootElement input          = OpenLineageFvtTestSupport.getAsset(inputQN);
        PropertyHelper          propertyHelper = new PropertyHelper();
        int                     summaries      = 0;

        for (RelatedMetadataElement report : OpenLineageFvtTestSupport.getRelatedElements(input.getElementHeader().getGUID(), 1, OpenMetadataType.REPORT_SUBJECT_RELATIONSHIP.typeName))
        {
            if ((report.getElement() == null) || (! OpenMetadataType.SURVEY_REPORT.typeName.equals(report.getElement().getType().getTypeName())))
            {
                continue;
            }

            for (RelatedMetadataElement annotation : OpenLineageFvtTestSupport.getRelatedElements(report.getElement().getElementGUID(), 1, OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName))
            {
                if ((annotation.getElement() != null) && (OpenMetadataType.QUALITY_ANNOTATION.typeName.equals(annotation.getElement().getType().getTypeName())))
                {
                    String annotationType = propertyHelper.getStringProperty("openlineage-fvt", OpenMetadataProperty.ANNOTATION_TYPE.name, annotation.getElement().getElementProperties(), "lovelaceServicesAnalyseTheLogStore");

                    if (SUMMARY_ANNOTATION_TYPE.equals(annotationType))
                    {
                        summaries++;
                    }
                }
            }
        }

        /*
         * Two assertions (not_null.id passes, unique.id fails) plus the overall summary.
         */
        assertEquals(3, summaries, "The data quality summary should produce one annotation per dimension plus an overall one on the input table");
    }


    /**
     * Start a Lovelace service and wait for it to complete.
     *
     * @param governanceActionType governance action type definition
     * @param requestParameters request parameters
     * @throws Exception the service failed or did not complete
     */
    private void runService(GovernanceActionTypeDefinition governanceActionType,
                            Map<String, String>            requestParameters) throws Exception
    {
        EngineActionWaiter waiter = new EngineActionWaiter();

        String qualifiedName = GovernanceEngineDefinition.EGERIA_GOVERNANCE_ENGINE.getName() + "::" + governanceActionType.getGovernanceRequestType();

        String engineActionGUID = waiter.getOpenGovernanceClient().initiateGovernanceActionType(OMAGPlatformExtension.USER_ID,
                                                                                                qualifiedName,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                requestParameters,
                                                                                                "openlineage-fvt",
                                                                                                null,
                                                                                                null);

        waiter.waitForCompletion(engineActionGUID, "the " + governanceActionType.getGovernanceRequestType() + " service");
    }


    /**
     * Add this test's input dataset to an event.
     *
     * @param event event
     * @param withAssertions include the data quality assertions
     */
    private void addInput(OpenLineageRunEvent event,
                          boolean             withAssertions)
    {
        OpenLineageEventFactory.withInputs(event, withAssertions);

        /*
         * Rename the orders input to this test's own table so that the two suites' datasets stay apart.
         */
        event.getInputs().get(0).setName(INPUT_TABLE);
        event.getInputs().remove(1);
    }


    /**
     * Add this test's output dataset to an event.
     *
     * @param event event
     * @param rowCount rows written (null for none)
     */
    private void addOutput(OpenLineageRunEvent event,
                           Long                rowCount)
    {
        OpenLineageEventFactory.withOutput(event, OUTPUT_TABLE, rowCount);
    }


    /**
     * Find an event file for a run and event type below a directory.
     *
     * @param directory directory
     * @param runId run id
     * @param eventType event type
     * @return file or null
     */
    private static File findEventFile(File   directory,
                                      String runId,
                                      String eventType)
    {
        File[] files = directory.listFiles();

        if (files == null)
        {
            return null;
        }

        for (File file : files)
        {
            if (file.isDirectory())
            {
                File found = findEventFile(file, runId, eventType);

                if (found != null)
                {
                    return found;
                }
            }
            else if ((file.getName().contains(runId)) && (file.getName().endsWith("-" + eventType + ".openlineageevent")))
            {
                return file;
            }
        }

        return null;
    }
}
