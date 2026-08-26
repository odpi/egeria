/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.postgres.survey;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


/**
 * Verify that PostgresDatabaseStatsExtractor correctly converts PostgreSQL's raw pg_stats.n_distinct
 * catalog value into an actual number-of-distinct-values estimate, per PostgreSQL's documented semantics
 * (see https://www.postgresql.org/docs/current/view-pg-stats.html).
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
}
