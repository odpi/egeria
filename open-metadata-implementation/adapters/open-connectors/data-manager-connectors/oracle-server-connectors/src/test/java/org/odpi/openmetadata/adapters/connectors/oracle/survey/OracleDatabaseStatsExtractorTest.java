/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.oracle.survey;

import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


/**
 * Verify that OracleDatabaseStatsExtractor surveys only the schemas that the includeSchemaNames and
 * excludeSchemaNames properties allow, and never a schema with no name.  Oracle-maintained schemas are left
 * out by the catalog queries themselves, so they are not tested here.
 */
public class OracleDatabaseStatsExtractorTest
{
    /**
     * With neither list set, every user schema is surveyed - but never one with no name.
     */
    @Test public void testUnfilteredSurveyCoversUserSchemasOnly()
    {
        OracleDatabaseStatsExtractor extractor = new OracleDatabaseStatsExtractor(List.of("db"), null);

        assertTrue(extractor.schemaShouldBeSurveyed("HR"));
        assertTrue(extractor.schemaShouldBeSurveyed("SALES"));
        assertFalse(extractor.schemaShouldBeSurveyed(null));
    }


    /**
     * The exclude list removes the schemas it names and nothing else.  Names are matched exactly, so a
     * schema whose name differs only in case is not excluded.
     */
    @Test public void testExcludedSchemasAreSkipped()
    {
        OracleDatabaseStatsExtractor extractor = new OracleDatabaseStatsExtractor(List.of("db"),
                                                                                  List.of("SALES"),
                                                                                  null,
                                                                                  null);

        assertFalse(extractor.schemaShouldBeSurveyed("SALES"));
        assertTrue(extractor.schemaShouldBeSurveyed("sales"));
        assertTrue(extractor.schemaShouldBeSurveyed("HR"));
    }


    /**
     * The include list names the only schemas surveyed, and wins over the exclude list.
     */
    @Test public void testIncludedSchemasTakePrecedence()
    {
        OracleDatabaseStatsExtractor extractor = new OracleDatabaseStatsExtractor(List.of("db"),
                                                                                  List.of("SALES"),
                                                                                  List.of("SALES"),
                                                                                  null);

        assertTrue(extractor.schemaShouldBeSurveyed("SALES"));
        assertFalse(extractor.schemaShouldBeSurveyed("HR"));
    }
}
