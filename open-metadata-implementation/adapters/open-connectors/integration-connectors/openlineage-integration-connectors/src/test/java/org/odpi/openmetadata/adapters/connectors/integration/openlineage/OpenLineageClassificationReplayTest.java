/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage;

import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageDataSetFacets;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageLifecycleStateChangeDataSetFacet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageOutputDataSet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageOutputDataSetOutputFacets;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageOutputStatisticsOutputDataSetFacet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageRun;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageRunEvent;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.RunMetricsProperties;
import org.testng.annotations.Test;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * The cataloguer queues the run events received for an element and replays them onto the stored RunMetrics and
 * DataScope classifications at refresh.  These tests check that replaying a batch of events gives the same
 * counts and timings as applying them one at a time.
 */
public class OpenLineageClassificationReplayTest
{
    private final OpenLineageCataloguerIntegrationConnector connector = new OpenLineageCataloguerIntegrationConnector();


    /**
     * Default constructor
     */
    public OpenLineageClassificationReplayTest()
    {
    }


    /**
     * Build a run event.
     *
     * @param runId run identifier
     * @param eventType START, RUNNING, COMPLETE, FAIL, ABORT
     * @param eventTime ISO-8601 time
     * @param outputs datasets written
     * @return event
     */
    private OpenLineageRunEvent event(UUID                           runId,
                                      String                         eventType,
                                      String                         eventTime,
                                      List<OpenLineageOutputDataSet> outputs)
    {
        OpenLineageRunEvent event = new OpenLineageRunEvent();
        OpenLineageRun      run   = new OpenLineageRun();

        run.setRunId(runId);
        event.setRun(run);
        event.setEventType(eventType);
        event.setEventTime(eventTime);
        event.setOutputs(outputs);

        return event;
    }


    /**
     * Build an output dataset with output statistics and, optionally, a lifecycle state change.
     *
     * @param rowCount rows written
     * @param lifecycleStateChange CREATE, OVERWRITE, TRUNCATE ... or null
     * @return dataset
     */
    private OpenLineageOutputDataSet output(long   rowCount,
                                            String lifecycleStateChange)
    {
        OpenLineageOutputDataSet                      output       = new OpenLineageOutputDataSet();
        OpenLineageOutputDataSetOutputFacets          outputFacets = new OpenLineageOutputDataSetOutputFacets();
        OpenLineageOutputStatisticsOutputDataSetFacet statistics   = new OpenLineageOutputStatisticsOutputDataSetFacet();

        statistics.setRowCount(rowCount);
        outputFacets.setOutputStatistics(statistics);
        output.setOutputFacets(outputFacets);

        if (lifecycleStateChange != null)
        {
            OpenLineageDataSetFacets                    facets    = new OpenLineageDataSetFacets();
            OpenLineageLifecycleStateChangeDataSetFacet lifecycle = new OpenLineageLifecycleStateChangeDataSetFacet();

            lifecycle.setLifecycleStateChange(lifecycleStateChange);
            facets.setLifecycleStateChange(lifecycle);
            output.setFacets(facets);
        }

        output.setName("table");
        output.setNamespace("postgres://host:5432");

        return output;
    }


    /**
     * Two runs replayed in one refresh: each run is counted once however many events it sent, the failure is
     * counted, and the durations add up.
     */
    @Test public void testRunEventsReplayed()
    {
        UUID run1 = UUID.randomUUID();
        UUID run2 = UUID.randomUUID();

        OpenLineageOutputDataSet written = output(100, null);

        List<OpenLineageRunEvent> queued = List.of(event(run1, "START", "2026-11-10T10:00:00Z", null),
                                                   event(run1, "RUNNING", "2026-11-10T10:00:30Z", null),
                                                   event(run1, "COMPLETE", "2026-11-10T10:01:00Z", List.of(written)),
                                                   event(run2, "START", "2026-11-10T11:00:00Z", null),
                                                   event(run2, "FAIL", "2026-11-10T11:00:10Z", null));

        RunMetricsProperties runMetrics = new RunMetricsProperties();

        for (OpenLineageRunEvent event : queued)
        {
            connector.applyRunEvent(runMetrics, event);
        }

        assertEquals(runMetrics.getRunCount().longValue(), 2L);
        assertEquals(runMetrics.getFailedRunCount().longValue(), 1L);
        assertEquals(runMetrics.getLastRunId(), run2.toString());
        assertEquals(runMetrics.getLastRunStatus(), "FAIL");
        assertEquals(runMetrics.getFirstRunStartTime(), Date.from(Instant.parse("2026-11-10T10:00:00Z")));
        assertEquals(runMetrics.getLastRunStartTime(), Date.from(Instant.parse("2026-11-10T11:00:00Z")));
        assertEquals(runMetrics.getLastRunDuration().longValue(), 10_000L);
        assertEquals(runMetrics.getTotalRunDuration().longValue(), 70_000L);
        assertEquals(runMetrics.getTotalRowsWritten().longValue(), 100L);
    }


    /**
     * Replaying onto the properties already stored continues the counts rather than restarting them.
     */
    @Test public void testRunEventsContinueStoredCounts()
    {
        RunMetricsProperties runMetrics = new RunMetricsProperties();

        runMetrics.setRunCount(5L);
        runMetrics.setFailedRunCount(2L);
        runMetrics.setTotalRunDuration(1_000L);
        runMetrics.setLastRunId(UUID.randomUUID().toString());

        UUID run = UUID.randomUUID();

        connector.applyRunEvent(runMetrics, event(run, "START", "2026-11-10T10:00:00Z", null));
        connector.applyRunEvent(runMetrics, event(run, "COMPLETE", "2026-11-10T10:00:02Z", null));

        assertEquals(runMetrics.getRunCount().longValue(), 6L);
        assertEquals(runMetrics.getFailedRunCount().longValue(), 2L);
        assertEquals(runMetrics.getTotalRunDuration().longValue(), 3_000L);
    }


    /**
     * Writes replayed in one refresh: the window spans them, an OVERWRITE restarts it, only completed runs count
     * as writes, and the latest statistics win.
     */
    @Test public void testDataScopeWritesReplayed()
    {
        DataScopeProperties dataScope = new DataScopeProperties();

        UUID run1 = UUID.randomUUID();
        UUID run2 = UUID.randomUUID();
        UUID run3 = UUID.randomUUID();

        OpenLineageOutputDataSet firstWrite     = output(10, null);
        OpenLineageOutputDataSet streamingWrite = output(20, null);
        OpenLineageOutputDataSet overwrite      = output(30, "OVERWRITE");

        connector.applyDataScopeWrite(dataScope, event(run1, "COMPLETE", "2026-11-10T10:00:00Z", List.of(firstWrite)), firstWrite);
        connector.applyDataScopeWrite(dataScope, event(run2, "RUNNING", "2026-11-10T11:00:00Z", List.of(streamingWrite)), streamingWrite);

        assertEquals(dataScope.getDataCollectionStartTime(), Date.from(Instant.parse("2026-11-10T10:00:00Z")));
        assertEquals(dataScope.getDataCollectionEndTime(), Date.from(Instant.parse("2026-11-10T11:00:00Z")));
        assertEquals(dataScope.getAdditionalProperties().get("writeCount"), "1", "a RUNNING event is not a completed write");
        assertEquals(dataScope.getAdditionalProperties().get("lastRowCount"), "20");

        connector.applyDataScopeWrite(dataScope, event(run3, "COMPLETE", "2026-11-10T12:00:00Z", List.of(overwrite)), overwrite);

        assertEquals(dataScope.getDataCollectionStartTime(), Date.from(Instant.parse("2026-11-10T12:00:00Z")), "an overwrite restarts the window");
        assertEquals(dataScope.getDataCollectionEndTime(), Date.from(Instant.parse("2026-11-10T12:00:00Z")));
        assertEquals(dataScope.getAdditionalProperties().get("writeCount"), "2");
        assertEquals(dataScope.getAdditionalProperties().get("lastWrittenByRunId"), run3.toString());
        assertEquals(dataScope.getAdditionalProperties().get("lastRowCount"), "30");
    }


    /**
     * Reads replayed in one refresh add to the stored read count, and leave the write window alone.
     */
    @Test public void testDataScopeReadsReplayed()
    {
        DataScopeProperties dataScope = new DataScopeProperties();

        dataScope.setAdditionalProperties(new java.util.HashMap<>(java.util.Map.of("readCount", "4")));

        UUID run1 = UUID.randomUUID();
        UUID run2 = UUID.randomUUID();

        connector.applyDataScopeRead(dataScope, event(run1, "COMPLETE", "2026-11-10T10:00:00Z", null));
        connector.applyDataScopeRead(dataScope, event(run2, "COMPLETE", "2026-11-10T10:05:00Z", null));

        assertEquals(dataScope.getAdditionalProperties().get("readCount"), "6");
        assertEquals(dataScope.getAdditionalProperties().get("lastReadByRunId"), run2.toString());
        assertNull(dataScope.getDataCollectionStartTime());
        assertNull(dataScope.getDataCollectionEndTime());
    }
}
