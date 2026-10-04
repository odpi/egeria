/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.db2luw.survey;

import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


/**
 * Verify that DB2LUWDatabaseStatsExtractor correctly converts Db2's raw SYSSTAT.COLUMNS.COLCARD catalog
 * value into an actual number-of-distinct-values estimate, per Db2's documented semantics (COLCARD is
 * -1 if statistics have not been gathered for the column - see
 * https://www.ibm.com/docs/en/db2/11.5?topic=views-syscatcolumns and
 * https://www1.columbia.edu/sec/acis/db2/db2d0/db2d0101.htm), and that it surveys only the schemas that the
 * includeSchemaNames and excludeSchemaNames properties allow.
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


    /**
     * With neither list set, every user schema is surveyed - but never Db2's own schemas.
     */
    @Test public void testUnfilteredSurveyCoversUserSchemasOnly()
    {
        DB2LUWDatabaseStatsExtractor extractor = new DB2LUWDatabaseStatsExtractor(List.of("db"), null);

        assertTrue(extractor.schemaShouldBeSurveyed("DB2INST1"));
        assertTrue(extractor.schemaShouldBeSurveyed("SALES"));
        assertFalse(extractor.schemaShouldBeSurveyed("SYSCAT"));
        assertFalse(extractor.schemaShouldBeSurveyed("SYSIBM"));
        assertFalse(extractor.schemaShouldBeSurveyed("NULLID"));
        assertFalse(extractor.schemaShouldBeSurveyed("SQLJ"));
        assertFalse(extractor.schemaShouldBeSurveyed("DB2GSE"));
        assertFalse(extractor.schemaShouldBeSurveyed(null));
    }


    /**
     * The exclude list removes the schemas it names and nothing else.  Names are matched exactly, so a
     * schema whose name differs only in case is not excluded.
     */
    @Test public void testExcludedSchemasAreSkipped()
    {
        DB2LUWDatabaseStatsExtractor extractor = new DB2LUWDatabaseStatsExtractor(List.of("db"),
                                                                                  List.of("SALES"),
                                                                                  null,
                                                                                  null);

        assertFalse(extractor.schemaShouldBeSurveyed("SALES"));
        assertTrue(extractor.schemaShouldBeSurveyed("sales"));
        assertTrue(extractor.schemaShouldBeSurveyed("DB2INST1"));
    }


    /**
     * The include list names the only schemas surveyed, and wins over the exclude list - but it cannot bring the
     * system schemas back in.
     */
    @Test public void testIncludedSchemasTakePrecedence()
    {
        DB2LUWDatabaseStatsExtractor extractor = new DB2LUWDatabaseStatsExtractor(List.of("db"),
                                                                                  List.of("SALES"),
                                                                                  List.of("SALES", "SYSCAT"),
                                                                                  null);

        assertTrue(extractor.schemaShouldBeSurveyed("SALES"));
        assertFalse(extractor.schemaShouldBeSurveyed("DB2INST1"));
        assertFalse(extractor.schemaShouldBeSurveyed("SYSCAT"));
    }
}
