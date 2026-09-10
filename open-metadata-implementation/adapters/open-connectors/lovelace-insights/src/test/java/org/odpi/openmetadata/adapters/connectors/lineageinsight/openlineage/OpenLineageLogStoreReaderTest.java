/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.lineageinsight.openlineage;

import org.testng.annotations.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the log store reader turns the files written by the file-based OpenLineage log store connector
 * into a run history: runs are assembled from their START and COMPLETE events, writes and reads are recorded once
 * per run from the completing event, assertions and tests are collected, and the time window is honoured.
 */
public class OpenLineageLogStoreReaderTest
{
    private static final String PRODUCER = "\"producer\":\"https://example.com/producer\",\"schemaURL\":\"https://openlineage.io/spec/2-0-2/OpenLineage.json#/$defs/RunEvent\"";


    private String event(String eventType,
                         String eventTime,
                         String runId,
                         String jobName,
                         String inputs,
                         String outputs,
                         String runFacets)
    {
        return "{\"eventType\":\"" + eventType + "\",\"eventTime\":\"" + eventTime + "\"," + PRODUCER +
               ",\"run\":{\"runId\":\"" + runId + "\"" + ((runFacets == null) ? "" : ",\"facets\":" + runFacets) + "}" +
               ",\"job\":{\"namespace\":\"airflow://prod\",\"name\":\"" + jobName + "\"}" +
               ",\"inputs\":[" + inputs + "],\"outputs\":[" + outputs + "]}";
    }


    private void write(File directory, String name, String content) throws Exception
    {
        File file = new File(directory, name);

        assertTrue(file.getParentFile().mkdirs() || file.getParentFile().isDirectory());

        Files.writeString(file.toPath(), content, StandardCharsets.UTF_8);
    }


    /**
     * Two runs of one job with statistics, assertions and a test; one run of another job; one unreadable file.
     *
     * @throws Exception problem with the temporary files
     */
    @Test
    public void testReadLogStore() throws Exception
    {
        File root = Files.createTempDirectory("openlineage-log-store").toFile();

        String orders = "{\"namespace\":\"postgres://db:5432\",\"name\":\"sales.public.orders\"," +
                        "\"inputFacets\":{\"inputStatistics\":{\"_producer\":\"p\",\"_schemaURL\":\"s\",\"rowCount\":500,\"size\":4096}," +
                        "\"dataQualityAssertions\":{\"_producer\":\"p\",\"_schemaURL\":\"s\",\"assertions\":[{\"assertion\":\"not_null\",\"column\":\"id\",\"success\":true},{\"assertion\":\"unique\",\"column\":\"id\",\"success\":false}]}}}";
        String ordersNoStats = "{\"namespace\":\"postgres://db:5432\",\"name\":\"sales.public.orders\"}";
        String counts = "{\"namespace\":\"postgres://db:5432\",\"name\":\"sales.public.order_counts\"," +
                        "\"facets\":{\"lifecycleStateChange\":{\"_producer\":\"p\",\"_schemaURL\":\"s\",\"lifecycleStateChange\":\"OVERWRITE\"}}," +
                        "\"outputFacets\":{\"outputStatistics\":{\"_producer\":\"p\",\"_schemaURL\":\"s\",\"rowCount\":10,\"size\":80}}}";
        String countsNoStats = "{\"namespace\":\"postgres://db:5432\",\"name\":\"sales.public.order_counts\"}";
        String tests = "{\"test\":{\"_producer\":\"p\",\"_schemaURL\":\"s\",\"tests\":[{\"name\":\"rows_present\",\"status\":\"pass\",\"type\":\"row_count\"}]}}";

        String run1 = "11111111-1111-1111-1111-111111111111";
        String run2 = "22222222-2222-2222-2222-222222222222";
        String run3 = "33333333-3333-3333-3333-333333333333";
        String old  = "44444444-4444-4444-4444-444444444444";

        write(root, "airflow:__prod/count_orders/" + run1 + "-a-START.openlineageevent",    event("START",    "2026-09-01T10:00:00Z", run1, "count_orders", ordersNoStats, countsNoStats, null));
        write(root, "airflow:__prod/count_orders/" + run1 + "-b-COMPLETE.openlineageevent", event("COMPLETE", "2026-09-01T10:05:00Z", run1, "count_orders", orders, counts, tests));
        write(root, "airflow:__prod/count_orders/" + run2 + "-a-START.openlineageevent",    event("START",    "2026-09-02T10:00:00Z", run2, "count_orders", ordersNoStats, countsNoStats, null));
        write(root, "airflow:__prod/count_orders/" + run2 + "-b-FAIL.openlineageevent",     event("FAIL",     "2026-09-02T10:02:00Z", run2, "count_orders", ordersNoStats, countsNoStats, null));
        write(root, "airflow:__prod/report/" + run3 + "-a-START.openlineageevent",          event("START",    "2026-09-02T11:00:00Z", run3, "report", countsNoStats, "", null));
        write(root, "airflow:__prod/count_orders/" + old + "-a-COMPLETE.openlineageevent",  event("COMPLETE", "2020-01-01T10:00:00Z", old,  "count_orders", orders, counts, null));
        write(root, "airflow:__prod/count_orders/junk.openlineageevent", "this is not json");
        write(root, "airflow:__prod/count_orders/notes.txt", "ignored");

        Date windowStart = OpenLineageLogStoreReader.parseTimestamp("2026-08-01T00:00:00Z");
        Date windowEnd   = OpenLineageLogStoreReader.parseTimestamp("2026-09-30T00:00:00Z");

        OpenLineageRunHistory history = new OpenLineageLogStoreReader().read(root, windowStart, windowEnd);

        assertEquals(history.getEventCount(), 5);
        assertEquals(history.getUnreadableFiles(), 1);
        assertEquals(history.getJobs().size(), 2);
        assertEquals(history.getDataSets().size(), 2);
        assertEquals(history.getEarliestEvent(), OpenLineageLogStoreReader.parseTimestamp("2026-09-01T10:00:00Z"));
        assertEquals(history.getLatestEvent(), OpenLineageLogStoreReader.parseTimestamp("2026-09-02T11:00:00Z"));

        OpenLineageRunHistory.JobHistory countOrders = history.getJobs().get(new OpenLineageRunHistory.Key("airflow://prod", "count_orders"));

        assertNotNull(countOrders);
        assertEquals(countOrders.runs.size(), 2, "old run outside the window must be excluded");

        OpenLineageRunHistory.RunRecord first = countOrders.runs.get(run1);

        assertEquals(first.status, "COMPLETE");
        assertEquals(first.startTime, OpenLineageLogStoreReader.parseTimestamp("2026-09-01T10:00:00Z"));
        assertEquals(first.endTime, OpenLineageLogStoreReader.parseTimestamp("2026-09-01T10:05:00Z"));
        assertEquals(first.duration(), Long.valueOf(5 * 60 * 1000));
        assertEquals(OpenLineageRunHistory.RunRecord.total(first.inputVolumes, 0), 500);
        assertEquals(OpenLineageRunHistory.RunRecord.total(first.inputVolumes, 1), 4096);
        assertEquals(OpenLineageRunHistory.RunRecord.total(first.outputVolumes, 0), 10);

        OpenLineageRunHistory.RunRecord second = countOrders.runs.get(run2);

        assertEquals(second.status, "FAIL");
        assertEquals(second.duration(), Long.valueOf(2 * 60 * 1000));

        assertEquals(countOrders.tests.size(), 1);
        assertEquals(countOrders.tests.get(0).name(), "rows_present");
        assertEquals(countOrders.tests.get(0).status(), "pass");

        OpenLineageRunHistory.JobHistory report = history.getJobs().get(new OpenLineageRunHistory.Key("airflow://prod", "report"));

        assertNotNull(report);
        assertEquals(report.runs.get(run3).status, "START");
        assertNull(report.runs.get(run3).endTime);

        OpenLineageRunHistory.DataSetHistory ordersHistory = history.getDataSets().get(new OpenLineageRunHistory.Key("postgres://db:5432", "sales.public.orders"));

        assertNotNull(ordersHistory);
        assertEquals(ordersHistory.reads.size(), 2, "one read per run");
        assertEquals(ordersHistory.writes.size(), 0);
        assertEquals(ordersHistory.assertions.size(), 2);
        assertEquals(ordersHistory.assertions.get(0).dimension(), "not_null.id");
        assertTrue(ordersHistory.assertions.get(0).success());
        assertTrue(! ordersHistory.assertions.get(1).success());

        OpenLineageRunHistory.DataSetHistory countsHistory = history.getDataSets().get(new OpenLineageRunHistory.Key("postgres://db:5432", "sales.public.order_counts"));

        assertNotNull(countsHistory);
        assertEquals(countsHistory.writes.size(), 2, "one write per run, from the completing event");
        assertEquals(countsHistory.reads.size(), 1);

        OpenLineageRunHistory.WriteRecord completedWrite = countsHistory.writes.stream().filter(write -> run1.equals(write.runId())).findFirst().orElse(null);

        assertNotNull(completedWrite);
        assertEquals(completedWrite.eventType(), "COMPLETE");
        assertEquals(completedWrite.rowCount(), Long.valueOf(10));
        assertEquals(completedWrite.lifecycleStateChange(), "OVERWRITE");

        /*
         * No window: the old run is included.
         */
        history = new OpenLineageLogStoreReader().read(root, null, null);

        assertEquals(history.getEventCount(), 6);
        assertEquals(history.getJobs().get(new OpenLineageRunHistory.Key("airflow://prod", "count_orders")).runs.size(), 3);
    }


    /**
     * Timestamps in the forms produced by the OpenLineage integrations all parse.
     */
    @Test
    public void testParseTimestamp()
    {
        assertNotNull(OpenLineageLogStoreReader.parseTimestamp("2026-09-08T14:00:00.123Z"));
        assertNotNull(OpenLineageLogStoreReader.parseTimestamp("2026-09-08T14:00:00+01:00"));
        assertNotNull(OpenLineageLogStoreReader.parseTimestamp("2026-09-08T14:00:00"));
        assertNull(OpenLineageLogStoreReader.parseTimestamp("not a date"));
        assertNull(OpenLineageLogStoreReader.parseTimestamp(null));
    }
}
