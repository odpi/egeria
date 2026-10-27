/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.oracle.survey;

import org.odpi.openmetadata.frameworks.opensurvey.DatabaseElementAnnotation;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyDatabaseAnnotationType;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceConnector;
import org.odpi.openmetadata.frameworks.opensurvey.measurements.RelationalDataManagerMeasurement;
import org.testng.annotations.Test;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertThrows;
import static org.testng.Assert.assertTrue;


/**
 * Verify that OracleDatabaseStatsExtractor surveys only the schemas that the includeSchemaNames and
 * excludeSchemaNames properties allow, and never a schema with no name, that it counts the elements of a
 * database correctly, and that a failing query does not lose the database from the results.
 * Oracle-maintained schemas are left out by the catalog queries themselves, so they are not tested here.
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
                                                                                  null,
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
                                                                                  null,
                                                                                  null,
                                                                                  null);

        assertTrue(extractor.schemaShouldBeSurveyed("SALES"));
        assertFalse(extractor.schemaShouldBeSurveyed("HR"));
    }


    /**
     * The database counts are the sums over all schemas, with tables, views and materialized views each
     * counted separately.
     */
    @Test public void testDatabaseCounts()
    {
        RelationalDataManagerMeasurement measurement = new RelationalDataManagerMeasurement();
        measurement.setResourceName("PDB1");

        OracleDatabaseStatsExtractor.DatabaseDetails databaseDetails = new OracleDatabaseStatsExtractor.DatabaseDetails(measurement);

        OracleDatabaseStatsExtractor.SchemaDetails schema1 = databaseDetails.getSchemaDetails("PDB1", "HR");
        schema1.getTableDetails("HR", "T1").getColumnDetails("C1");
        schema1.getTableDetails("HR", "T1").getColumnDetails("C2");
        schema1.getTableDetails("HR", "T2").getColumnDetails("C1");
        schema1.getTableDetails("HR", "V1").setViewDetails("HR", "select 1 from dual");

        OracleDatabaseStatsExtractor.SchemaDetails schema2 = databaseDetails.getSchemaDetails("PDB1", "SALES");
        schema2.getTableDetails("SALES", "T3").getColumnDetails("C1");
        schema2.getTableDetails("SALES", "M1").setMaterializedViewDetails("SALES", false, true, "select 1 from dual");
        schema2.getTableDetails("SALES", "M1").getColumnDetails("C1");

        databaseDetails.setUpCounts();

        assertEquals(measurement.getSchemaCount(), 2L);
        assertEquals(measurement.getTableCount(), 3L);
        assertEquals(measurement.getViewCount(), 1L);
        assertEquals(measurement.getMaterializedViewCount(), 1L);
        assertEquals(measurement.getColumnCount(), 5L);
        assertEquals(schema1.getSchemaMeasurement().getColumnCount(), 3L);
        assertEquals(schema1.getQualifiedSchemaName(), "PDB1.HR");
    }


    /**
     * A failure of the server-level statistics query is logged rather than thrown, and every requested
     * database still has an entry in the results, so its schemas are still surveyed.  The schema survey only
     * reaches the database (and so fails on this connection) for a database that has an entry; if the
     * schemas cannot be listed, the caller is told, so that it can report the database as not surveyed.
     */
    @Test public void testFailedDatabaseStatisticsKeepDatabases() throws Exception
    {
        OracleDatabaseStatsExtractor extractor = new OracleDatabaseStatsExtractor(List.of("PDB1", "PDB2"), null);

        extractor.getDatabaseStatistics(getFailingConnection());

        assertThrows(SQLException.class, () -> extractor.getSchemaStatistics("PDB1", getFailingConnection()));
        assertThrows(SQLException.class, () -> extractor.getSchemaStatistics("PDB2", getFailingConnection()));

        /*
         * A database that was not requested has no entry, so nothing is queried.
         */
        extractor.getSchemaStatistics("PDB3", getFailingConnection());
    }


    /**
     * A survey that stops after gathering the database statistics stores one database-level annotation for
     * each database, and the results are released so that they cannot be stored a second time.
     *
     * @throws Exception unexpected failure
     */
    @Test public void testDatabaseAnnotationsWhenSurveyStopsAfterDatabaseStatistics() throws Exception
    {
        SurveyActionServiceConnector surveyActionService = new SurveyActionServiceConnector()
        {
            @Override
            public synchronized boolean isActive()
            {
                return true;
            }
        };

        OracleDatabaseStatsExtractor extractor = new OracleDatabaseStatsExtractor(List.of("PDB1", "PDB2"), surveyActionService);

        extractor.getDatabaseStatistics(getFailingConnection());

        List<DatabaseElementAnnotation> annotations = extractor.getDatabaseAnnotations();

        assertNotNull(annotations);
        assertEquals(annotations.size(), 2);

        for (DatabaseElementAnnotation annotation : annotations)
        {
            assertEquals(annotation.annotationProperties().getAnnotationType(), SurveyDatabaseAnnotationType.DATABASE_MEASUREMENTS.getName());
            assertNotNull(annotation.databaseName());
            assertNull(annotation.schemaName());
        }

        assertNull(extractor.getDatabaseAnnotations());
        assertNull(extractor.getAnnotations("PDB1"));
    }


    /**
     * Return a JDBC connection whose every statement fails, as it would if the userId lacked the privileges
     * to read the data dictionary.
     *
     * @return connection proxy
     */
    private Connection getFailingConnection()
    {
        return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                                                   new Class<?>[]{Connection.class},
                                                   (proxy, method, arguments) ->
                                                   {
                                                       throw new SQLException("ORA-00942: table or view does not exist");
                                                   });
    }
}
