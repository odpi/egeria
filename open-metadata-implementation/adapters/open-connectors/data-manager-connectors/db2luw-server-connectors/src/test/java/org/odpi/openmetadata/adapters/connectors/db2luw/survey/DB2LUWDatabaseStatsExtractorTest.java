/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.db2luw.survey;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


/**
 * Verify that DB2LUWDatabaseStatsExtractor correctly converts Db2's raw SYSSTAT.COLUMNS.COLCARD catalog
 * value into an actual number-of-distinct-values estimate, per Db2's documented semantics (COLCARD is
 * -1 if statistics have not been gathered for the column - see
 * https://www.ibm.com/docs/en/db2/11.5?topic=views-syscatcolumns and
 * https://www1.columbia.edu/sec/acis/db2/db2d0/db2d0101.htm).
 */
public class DB2LUWDatabaseStatsExtractorTest
{
    /**
     * A non-negative COLCARD is already a real distinct-value count and must pass through unchanged.
     */
    @Test public void testNonNegativeColumnCardinalityIsUsedDirectly()
    {
        assertEquals(DB2LUWDatabaseStatsExtractor.calculateNumberOfDistinctValues(42L), 42L);
        assertEquals(DB2LUWDatabaseStatsExtractor.calculateNumberOfDistinctValues(0L), 0L);
    }


    /**
     * Before this fix, COLCARD = -1 (Db2's documented "statistics not gathered" sentinel - not a real,
     * negative count) flowed straight into the survey's "Number of Distinct Values" measurement as a
     * literal -1. A column that has never had RUNSTATS run on it is a routine, common state, not a rare
     * edge case, so this fired constantly. Confirm the sentinel is now recognised and reported as
     * "no data" (0) rather than a nonsensical negative count.
     */
    @Test public void testUngatheredStatisticsSentinelDoesNotProduceNegativeCount()
    {
        assertEquals(DB2LUWDatabaseStatsExtractor.calculateNumberOfDistinctValues(-1L), 0L);
    }
}
