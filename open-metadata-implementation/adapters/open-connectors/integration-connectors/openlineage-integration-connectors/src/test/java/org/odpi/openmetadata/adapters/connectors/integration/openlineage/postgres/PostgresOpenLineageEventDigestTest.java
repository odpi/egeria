/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage.postgres;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageRunEvent;
import org.testng.annotations.Test;

import java.util.Date;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * Validate that an OpenLineage run event is turned into the rows of the digest tables of the PostgreSQL
 * OpenLineage log store, using the same rules as the Lovelace file-based reader.
 */
public class PostgresOpenLineageEventDigestTest
{
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String       RUN_ID        = "11111111-1111-1111-1111-111111111111";


    private OpenLineageRunEvent event(String eventType,
                                      String jobNamespace,
                                      String inputs,
                                      String outputs,
                                      String runFacets) throws Exception
    {
        String json = "{\"eventType\":\"" + eventType + "\",\"eventTime\":\"2026-09-01T10:05:00Z\"," +
                "\"producer\":\"https://example.com/producer\",\"schemaURL\":\"https://openlineage.io/spec/2-0-2/OpenLineage.json#/$defs/RunEvent\"" +
                ",\"run\":{\"runId\":\"" + RUN_ID + "\"" + ((runFacets == null) ? "" : ",\"facets\":" + runFacets) + "}" +
                ",\"job\":{" + ((jobNamespace == null) ? "" : "\"namespace\":\"" + jobNamespace + "\",") + "\"name\":\"count_orders\"}" +
                ",\"inputs\":[" + inputs + "],\"outputs\":[" + outputs + "]}";

        return OBJECT_MAPPER.readValue(json, OpenLineageRunEvent.class);
    }


    /**
     * A completing event with input statistics, assertions, an output with statistics, lifecycle and version, a
     * test and the egeria governance action facet.
     *
     * @throws Exception problem parsing the event
     */
    @Test
    public void testCompleteEvent() throws Exception
    {
        String orders = "{\"namespace\":\"postgres://db:5432\",\"name\":\"sales.public.orders\"," +
                "\"inputFacets\":{\"inputStatistics\":{\"_producer\":\"p\",\"_schemaURL\":\"s\",\"rowCount\":500,\"size\":4096}," +
                "\"dataQualityAssertions\":{\"_producer\":\"p\",\"_schemaURL\":\"s\",\"assertions\":[{\"assertion\":\"not_null\",\"column\":\"id\",\"success\":true},{\"assertion\":\"unique\",\"column\":\"id\",\"success\":false}]}}}";
        String counts = "{\"namespace\":\"postgres://db:5432\",\"name\":\"sales.public.order_counts\"," +
                "\"facets\":{\"lifecycleStateChange\":{\"_producer\":\"p\",\"_schemaURL\":\"s\",\"lifecycleStateChange\":\"OVERWRITE\"}," +
                "\"version\":{\"_producer\":\"p\",\"_schemaURL\":\"s\",\"datasetVersion\":\"7\"}}," +
                "\"outputFacets\":{\"outputStatistics\":{\"_producer\":\"p\",\"_schemaURL\":\"s\",\"rowCount\":10,\"size\":80,\"fileCount\":2}}}";
        String facets = "{\"test\":{\"_producer\":\"p\",\"_schemaURL\":\"s\",\"tests\":[{\"name\":\"rows_present\",\"status\":\"pass\",\"type\":\"row_count\",\"severity\":\"error\"}]}," +
                "\"egeria_governanceAction\":{\"_producer\":\"p\",\"_schemaURL\":\"s\",\"engineActionGUID\":\"ea-guid\",\"governanceEngineName\":\"AssetSurvey\"}}";

        PostgresOpenLineageEventDigest digest = PostgresOpenLineageEventDigest.digest(event("COMPLETE", "airflow://prod", orders, counts, facets));

        assertNotNull(digest);
        assertEquals(digest.getEventType(), "COMPLETE");
        assertEquals(digest.getRunId(), RUN_ID);
        assertEquals(digest.getJobNamespace(), "airflow://prod");
        assertEquals(digest.getJobName(), "count_orders");
        assertEquals(digest.getProducer(), "https://example.com/producer");
        assertEquals(digest.getEventTime(), PostgresOpenLineageEventDigest.parseTimestamp("2026-09-01T10:05:00Z"));

        assertEquals(digest.getRun().status(), "COMPLETE");
        assertNull(digest.getRun().startTime());
        assertEquals(digest.getRun().endTime(), digest.getEventTime());

        assertNotNull(digest.getGovernanceAction());
        assertEquals(digest.getGovernanceAction().getEngineActionGUID(), "ea-guid");

        assertEquals(digest.getDatasets().size(), 2);

        PostgresOpenLineageEventDigest.DatasetRow input = digest.getDatasets().get(0);

        assertEquals(input.direction(), PostgresOpenLineageLogStoreSchema.INPUT_DIRECTION);
        assertEquals(input.name(), "sales.public.orders");
        assertEquals(input.rowCount(), Long.valueOf(500));
        assertEquals(input.size(), Long.valueOf(4096));

        PostgresOpenLineageEventDigest.DatasetRow output = digest.getDatasets().get(1);

        assertEquals(output.direction(), PostgresOpenLineageLogStoreSchema.OUTPUT_DIRECTION);
        assertEquals(output.rowCount(), Long.valueOf(10));
        assertEquals(output.size(), Long.valueOf(80));
        assertEquals(output.fileCount(), Long.valueOf(2));
        assertEquals(output.lifecycleStateChange(), "OVERWRITE");
        assertEquals(output.datasetVersion(), "7");

        assertEquals(digest.getAssertions().size(), 2);
        assertEquals(digest.getAssertions().get(0).assertion(), "not_null");
        assertEquals(digest.getAssertions().get(0).column(), "id");
        assertTrue(digest.getAssertions().get(0).success());
        assertFalse(digest.getAssertions().get(1).success());
        assertEquals(digest.getAssertions().get(1).datasetName(), "sales.public.orders");

        assertEquals(digest.getTests().size(), 1);
        assertEquals(digest.getTests().get(0).name(), "rows_present");
        assertEquals(digest.getTests().get(0).severity(), "error");
    }


    /**
     * Each event type contributes the right status and times to its run.
     *
     * @throws Exception problem parsing the event
     */
    @Test
    public void testRunContributionByEventType() throws Exception
    {
        PostgresOpenLineageEventDigest start = PostgresOpenLineageEventDigest.digest(event("START", "ns", "", "", null));

        assertNotNull(start);
        assertEquals(start.getRun().status(), "START");
        assertEquals(start.getRun().startTime(), start.getEventTime());
        assertNull(start.getRun().endTime());

        PostgresOpenLineageEventDigest running = PostgresOpenLineageEventDigest.digest(event("RUNNING", "ns", "", "", null));

        assertNotNull(running);
        assertEquals(running.getRun().status(), "RUNNING");
        assertNull(running.getRun().startTime());
        assertNull(running.getRun().endTime());

        PostgresOpenLineageEventDigest fail = PostgresOpenLineageEventDigest.digest(event("FAIL", "ns", "", "", null));

        assertNotNull(fail);
        assertEquals(fail.getRun().status(), "FAIL");
        assertEquals(fail.getRun().endTime(), fail.getEventTime());

        PostgresOpenLineageEventDigest other = PostgresOpenLineageEventDigest.digest(event("OTHER", "ns", "", "", null));

        assertNotNull(other);
        assertNull(other.getRun().status());
        assertNull(other.getRun().startTime());
        assertNull(other.getRun().endTime());
    }


    /**
     * Missing namespaces are stored as empty strings; datasets without names are skipped; the data quality
     * metrics facet stands in for missing input statistics; a subset output records its condition type.
     *
     * @throws Exception problem parsing the event
     */
    @Test
    public void testOptionalValues() throws Exception
    {
        String metricsInput = "{\"name\":\"orders.csv\",\"inputFacets\":{\"dataQualityMetrics\":{\"_producer\":\"p\",\"_schemaURL\":\"s\",\"rowCount\":42,\"bytes\":1024}}}";
        String unnamed      = "{\"namespace\":\"file://x\"}";
        String subset       = "{\"namespace\":\"file://x\",\"name\":\"extract.csv\",\"outputFacets\":{\"subset\":{\"_producer\":\"p\",\"_schemaURL\":\"s\",\"outputCondition\":{\"type\":\"partition\"}}}}";

        PostgresOpenLineageEventDigest digest = PostgresOpenLineageEventDigest.digest(event("START", null, metricsInput + "," + unnamed, subset, null));

        assertNotNull(digest);
        assertEquals(digest.getJobNamespace(), "");
        assertEquals(digest.getDatasets().size(), 2);
        assertEquals(digest.getDatasets().get(0).namespace(), "");
        assertEquals(digest.getDatasets().get(0).rowCount(), Long.valueOf(42));
        assertEquals(digest.getDatasets().get(0).size(), Long.valueOf(1024));
        assertEquals(digest.getDatasets().get(1).subsetType(), "partition");
        assertNull(digest.getDatasets().get(1).rowCount());
        assertTrue(digest.getAssertions().isEmpty());
        assertTrue(digest.getTests().isEmpty());
        assertNull(digest.getGovernanceAction());
    }


    /**
     * Events that can not be stored produce no digest.
     *
     * @throws Exception problem parsing the event
     */
    @Test
    public void testUnstorableEvents() throws Exception
    {
        assertNull(PostgresOpenLineageEventDigest.digest(null));

        OpenLineageRunEvent noTime = event("START", "ns", "", "", null);
        noTime.setEventTime("not a time");
        assertNull(PostgresOpenLineageEventDigest.digest(noTime));

        OpenLineageRunEvent noRun = event("START", "ns", "", "", null);
        noRun.setRun(null);
        assertNull(PostgresOpenLineageEventDigest.digest(noRun));

        OpenLineageRunEvent noJobName = event("START", "ns", "", "", null);
        noJobName.getJob().setName(null);
        assertNull(PostgresOpenLineageEventDigest.digest(noJobName));
    }


    /**
     * Timestamps with an offset, in UTC, and without a zone are all accepted.
     */
    @Test
    public void testParseTimestamp()
    {
        Date expected = new Date(1788300000000L);

        assertEquals(PostgresOpenLineageEventDigest.parseTimestamp("2026-09-01T22:00:00Z"), expected);
        assertEquals(PostgresOpenLineageEventDigest.parseTimestamp("2026-09-01T23:00:00+01:00"), expected);
        assertEquals(PostgresOpenLineageEventDigest.parseTimestamp("2026-09-01T22:00:00"), expected);
        assertNull(PostgresOpenLineageEventDigest.parseTimestamp(" "));
        assertNull(PostgresOpenLineageEventDigest.parseTimestamp(null));
    }
}
