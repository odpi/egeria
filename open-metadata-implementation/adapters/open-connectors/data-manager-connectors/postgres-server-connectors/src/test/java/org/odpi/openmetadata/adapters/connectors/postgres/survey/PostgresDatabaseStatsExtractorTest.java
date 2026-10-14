/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.postgres.survey;

import org.odpi.openmetadata.frameworks.opensurvey.measurements.RelationalDataManagerMeasurement;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


/**
 * Verify that PostgresDatabaseStatsExtractor correctly converts PostgreSQL's raw pg_stats.n_distinct
 * catalog value into an actual number-of-distinct-values estimate, per PostgreSQL's documented semantics
 * (see https://www.postgresql.org/docs/current/view-pg-stats.html), and that it surveys only the schemas
 * that the includeSchemaNames and excludeSchemaNames properties allow.
 */
public class PostgresDatabaseStatsExtractorTest
{
    /**
     * A non-negative n_distinct is already an absolute count and should pass through unchanged
     * (allowing for rounding of the underlying real/float4 value).
     */
    @Test public void testNonNegativeNDistinctIsUsedDirectly()
    {
        assertEquals(PostgresDatabaseStatsExtractor.calculateNumberOfDistinctValues(42.0, 1_000_000L), 42L);
        assertEquals(PostgresDatabaseStatsExtractor.calculateNumberOfDistinctValues(0.0, 1_000_000L), 0L);
    }


    /**
     * A negative n_distinct is the negative of the distinct-to-row ratio, and must be scaled by the
     * table's row count to recover an actual distinct-value estimate - not used as a count directly.
     */
    @Test public void testNegativeNDistinctIsScaledByRowCount()
    {
        // A primary-key-like column: every value distinct (ratio -1.0) in a 1,000-row table.
        assertEquals(PostgresDatabaseStatsExtractor.calculateNumberOfDistinctValues(-1.0, 1000L), 1000L);

        // A column where 25% of values are distinct (ratio -0.25) in a 2,000-row table.
        assertEquals(PostgresDatabaseStatsExtractor.calculateNumberOfDistinctValues(-0.25, 2000L), 500L);
    }


    /**
     * Before this fix, a fractional negative ratio read via ResultSet.getLong() would truncate to zero -
     * silently hiding the fact that PostgreSQL was reporting a ratio at all. Confirm the fix does not
     * collapse a genuinely near-unique, low-row-count column down to zero.
     */
    @Test public void testSmallFractionalNegativeNDistinctDoesNotTruncateToZero()
    {
        assertEquals(PostgresDatabaseStatsExtractor.calculateNumberOfDistinctValues(-0.95, 100L), 95L);
    }


    /**
     * A table with no tracked row-count statistics yet (n_live_tup is 0, e.g. an unanalyzed or empty
     * table) must not produce a negative distinct-value estimate.
     */
    @Test public void testZeroRowCountWithNegativeRatioYieldsZero()
    {
        assertEquals(PostgresDatabaseStatsExtractor.calculateNumberOfDistinctValues(-1.0, 0L), 0L);
    }


    /**
     * With neither list set, every user schema is surveyed - but never PostgreSQL's own catalog schemas.
     */
    @Test public void testUnfilteredSurveyCoversUserSchemasOnly()
    {
        PostgresDatabaseStatsExtractor extractor = new PostgresDatabaseStatsExtractor(List.of("db"), null);

        assertTrue(extractor.schemaShouldBeSurveyed("public"));
        assertTrue(extractor.schemaShouldBeSurveyed("repository_qs_metadata_store"));
        assertFalse(extractor.schemaShouldBeSurveyed("pg_catalog"));
        assertFalse(extractor.schemaShouldBeSurveyed("information_schema"));
        assertFalse(extractor.schemaShouldBeSurveyed(null));
    }


    /**
     * The exclude list removes the schemas it names and nothing else.
     */
    @Test public void testExcludedSchemasAreSkipped()
    {
        PostgresDatabaseStatsExtractor extractor = new PostgresDatabaseStatsExtractor(List.of("db"),
                                                                                      List.of("repository_qs_metadata_store"),
                                                                                      null,
                                                                                      null,
                                                                                      null,
                                                                                      null);

        assertFalse(extractor.schemaShouldBeSurveyed("repository_qs_metadata_store"));
        assertTrue(extractor.schemaShouldBeSurveyed("public"));
    }


    /**
     * The include list names the only schemas surveyed, and wins over the exclude list - but it cannot bring
     * the catalog schemas back in.
     */
    @Test public void testIncludedSchemasTakePrecedence()
    {
        PostgresDatabaseStatsExtractor extractor = new PostgresDatabaseStatsExtractor(List.of("db"),
                                                                                      List.of("postgres_fvt_schema"),
                                                                                      List.of("postgres_fvt_schema", "pg_catalog"),
                                                                                      null,
                                                                                      null,
                                                                                      null);

        assertTrue(extractor.schemaShouldBeSurveyed("postgres_fvt_schema"));
        assertFalse(extractor.schemaShouldBeSurveyed("public"));
        assertFalse(extractor.schemaShouldBeSurveyed("pg_catalog"));
    }


    /**
     * A column's size is estimated from its average width and the table's row count, since PostgreSQL has no
     * catalog value for it.  Missing statistics give zero rather than a negative number.
     */
    @Test public void testColumnSizeEstimate()
    {
        assertEquals(PostgresDatabaseStatsExtractor.estimateColumnSize(8, 1000L), 8000L);
        assertEquals(PostgresDatabaseStatsExtractor.estimateColumnSize(0, 1000L), 0L);
        assertEquals(PostgresDatabaseStatsExtractor.estimateColumnSize(8, 0L), 0L);
        assertEquals(PostgresDatabaseStatsExtractor.estimateColumnSize(8, -1L), 0L);

        // Wide columns in large tables must not overflow an int.
        assertEquals(PostgresDatabaseStatsExtractor.estimateColumnSize(1000, 10_000_000L), 10_000_000_000L);
    }


    /**
     * The database counts are the sums over all schemas, with tables, views and materialized views each
     * counted separately.
     */
    @Test public void testDatabaseCounts()
    {
        RelationalDataManagerMeasurement measurement = new RelationalDataManagerMeasurement();
        measurement.setResourceName("db");

        PostgresDatabaseStatsExtractor.DatabaseDetails databaseDetails = new PostgresDatabaseStatsExtractor.DatabaseDetails(measurement);

        PostgresDatabaseStatsExtractor.SchemaDetails schema1 = databaseDetails.getSchemaDetails("s1");
        schema1.getTableDetails("t1").getColumnDetails("c1");
        schema1.getTableDetails("t1").getColumnDetails("c2");
        schema1.getTableDetails("t2").getColumnDetails("c1");
        schema1.getTableDetails("v1").setViewDetails("owner", "select 1");

        PostgresDatabaseStatsExtractor.SchemaDetails schema2 = databaseDetails.getSchemaDetails("s2");
        schema2.getTableDetails("t3").getColumnDetails("c1");
        schema2.getTableDetails("m1").setMaterializedViewDetails("owner", false, true, "select 1");
        schema2.getTableDetails("m1").getColumnDetails("c1");

        databaseDetails.setUpCounts();

        assertEquals(measurement.getSchemaCount(), 2L);
        assertEquals(measurement.getTableCount(), 3L);
        assertEquals(measurement.getViewCount(), 1L);
        assertEquals(measurement.getMaterializedViewCount(), 1L);
        assertEquals(measurement.getColumnCount(), 5L);
        assertEquals(schema1.getSchemaMeasurement().getColumnCount(), 3L);
    }
}
