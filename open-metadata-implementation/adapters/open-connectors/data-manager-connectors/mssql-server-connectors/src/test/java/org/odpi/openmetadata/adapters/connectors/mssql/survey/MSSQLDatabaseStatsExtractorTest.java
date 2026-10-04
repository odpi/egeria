/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.mssql.survey;

import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


/**
 * Verify that MSSQLDatabaseStatsExtractor surveys only the schemas that the includeSchemaNames and
 * excludeSchemaNames properties allow, and never Microsoft SQL Server's own catalog schemas.
 */
public class MSSQLDatabaseStatsExtractorTest
{
    /**
     * With neither list set, every user schema is surveyed - but never Microsoft SQL Server's own catalog schemas.
     */
    @Test public void testUnfilteredSurveyCoversUserSchemasOnly()
    {
        MSSQLDatabaseStatsExtractor extractor = new MSSQLDatabaseStatsExtractor(List.of("db"), null);

        assertTrue(extractor.schemaShouldBeSurveyed("dbo"));
        assertTrue(extractor.schemaShouldBeSurveyed("sales"));
        assertFalse(extractor.schemaShouldBeSurveyed("sys"));
        assertFalse(extractor.schemaShouldBeSurveyed("INFORMATION_SCHEMA"));
        assertFalse(extractor.schemaShouldBeSurveyed(null));
    }


    /**
     * The exclude list removes the schemas it names and nothing else.  Names are matched exactly, so a
     * schema whose name differs only in case is not excluded.
     */
    @Test public void testExcludedSchemasAreSkipped()
    {
        MSSQLDatabaseStatsExtractor extractor = new MSSQLDatabaseStatsExtractor(List.of("db"),
                                                                                List.of("sales"),
                                                                                null,
                                                                                null);

        assertFalse(extractor.schemaShouldBeSurveyed("sales"));
        assertTrue(extractor.schemaShouldBeSurveyed("SALES"));
        assertTrue(extractor.schemaShouldBeSurveyed("dbo"));
    }


    /**
     * The include list names the only schemas surveyed, and wins over the exclude list - but it cannot bring the
     * system schemas back in.
     */
    @Test public void testIncludedSchemasTakePrecedence()
    {
        MSSQLDatabaseStatsExtractor extractor = new MSSQLDatabaseStatsExtractor(List.of("db"),
                                                                                List.of("sales"),
                                                                                List.of("sales", "sys"),
                                                                                null);

        assertTrue(extractor.schemaShouldBeSurveyed("sales"));
        assertFalse(extractor.schemaShouldBeSurveyed("dbo"));
        assertFalse(extractor.schemaShouldBeSurveyed("sys"));
    }
}
