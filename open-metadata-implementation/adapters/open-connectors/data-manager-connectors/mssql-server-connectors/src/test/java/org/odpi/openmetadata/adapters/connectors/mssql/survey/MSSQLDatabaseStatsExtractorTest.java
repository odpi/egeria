/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.mssql.survey;

import org.odpi.openmetadata.frameworks.opensurvey.DatabaseElementAnnotation;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyDatabaseAnnotationType;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceConnector;
import org.odpi.openmetadata.frameworks.opensurvey.measurements.RelationalDataManagerMeasurement;
import org.testng.annotations.Test;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;


/**
 * Verify that MSSQLDatabaseStatsExtractor surveys only the schemas that the includeSchemaNames and
 * excludeSchemaNames properties allow, and never Microsoft SQL Server's own catalog schemas; that its counts
 * are correct; and that a failing query leaves a gap in the results rather than ending the survey.
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
                                                                                null,
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
                                                                                null,
                                                                                null,
                                                                                null);

        assertTrue(extractor.schemaShouldBeSurveyed("sales"));
        assertFalse(extractor.schemaShouldBeSurveyed("dbo"));
        assertFalse(extractor.schemaShouldBeSurveyed("sys"));
    }


    /**
     * The database counts are the sums over all schemas, with tables, views and indexed views each
     * counted separately.
     */
    @Test public void testDatabaseCounts()
    {
        RelationalDataManagerMeasurement measurement = new RelationalDataManagerMeasurement();
        measurement.setResourceName("db");

        MSSQLDatabaseStatsExtractor.DatabaseDetails databaseDetails = new MSSQLDatabaseStatsExtractor.DatabaseDetails(measurement);

        MSSQLDatabaseStatsExtractor.SchemaDetails schema1 = databaseDetails.getSchemaDetails("s1");
        schema1.getTableDetails("t1").getColumnDetails("c1");
        schema1.getTableDetails("t1").getColumnDetails("c2");
        schema1.getTableDetails("t2").getColumnDetails("c1");
        schema1.getTableDetails("v1").setViewDetails("owner", "select 1");

        MSSQLDatabaseStatsExtractor.SchemaDetails schema2 = databaseDetails.getSchemaDetails("s2");
        schema2.getTableDetails("t3").getColumnDetails("c1");
        schema2.getTableDetails("m1").setMaterializedViewDetails("owner", true, true, "select 1");
        schema2.getTableDetails("m1").getColumnDetails("c1");

        databaseDetails.setUpCounts();

        assertEquals(measurement.getSchemaCount(), 2L);
        assertEquals(measurement.getTableCount(), 3L);
        assertEquals(measurement.getViewCount(), 1L);
        assertEquals(measurement.getMaterializedViewCount(), 1L);
        assertEquals(measurement.getColumnCount(), 5L);
        assertEquals(schema1.getSchemaMeasurement().getColumnCount(), 3L);
    }


    /**
     * When every server-level statistics query fails, the database is still in the results, so it is still
     * surveyed and reported.
     */
    @Test public void testDatabaseKeptWhenServerStatisticsFail()
    {
        MSSQLDatabaseStatsExtractor extractor = new MSSQLDatabaseStatsExtractor(List.of("db"), null);

        extractor.getDatabaseStatistics(fakeConnection(Map.of()));

        MSSQLDatabaseStatsExtractor.DatabaseDetails databaseDetails = extractor.getDatabaseDetails("db");

        assertNotNull(databaseDetails);
        assertEquals(databaseDetails.getDatabaseMeasurements().getResourceName(), "db");
    }


    /**
     * A failing query within a schema is skipped: the queries that work still contribute their results and
     * the schema name is passed as a bound parameter rather than concatenated into the SQL.
     *
     * @throws SQLException the schemas could not be listed (not expected)
     */
    @Test public void testFailingSchemaQueryIsIsolated() throws SQLException
    {
        MSSQLDatabaseStatsExtractor extractor = new MSSQLDatabaseStatsExtractor(List.of("db"), null);

        extractor.getDatabaseStatistics(fakeConnection(Map.of()));

        List<String> boundSchemaNames = new ArrayList<>();

        /*
         * Only the schema listing and the plain views query work; the column, table, table activity and
         * indexed view queries all fail.
         */
        Connection connection = fakeConnection(Map.of("FROM sys.schemas s", List.of(Map.of("schemaname", "MixedCase Sales"))),
                                               Map.of("AND NOT EXISTS",     List.of(Map.of("viewname",   "v1",
                                                                                           "viewowner",  "dbo",
                                                                                           "definition", "select 1"))),
                                               boundSchemaNames);

        extractor.getSchemaStatistics("db", connection);

        MSSQLDatabaseStatsExtractor.DatabaseDetails databaseDetails = extractor.getDatabaseDetails("db");

        assertNotNull(databaseDetails);
        assertEquals(databaseDetails.getDatabaseMeasurements().getSchemaCount(), 1L);
        assertEquals(databaseDetails.getDatabaseMeasurements().getViewCount(), 1L);
        assertEquals(databaseDetails.getDatabaseMeasurements().getTableCount(), 0L);
        assertEquals(databaseDetails.getSchemaDetails("MixedCase Sales").getTableDetails("v1").getTableMeasurements().getTableOwner(), "dbo");
        assertEquals(boundSchemaNames.size(), 5);
        for (String boundSchemaName : boundSchemaNames)
        {
            assertEquals(boundSchemaName, "MixedCase Sales");
        }
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

        MSSQLDatabaseStatsExtractor extractor = new MSSQLDatabaseStatsExtractor(List.of("db1", "db2"), surveyActionService);

        extractor.getDatabaseStatistics(fakeConnection(Map.of()));

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
        assertNull(extractor.getAnnotations("db1"));
    }


    /**
     * Return a JDBC connection where every query fails except those matching the supplied rows.
     *
     * @param schemaRows rows returned by the schema listing query, keyed by a fragment of its SQL
     * @return fake connection
     */
    private static Connection fakeConnection(Map<String, List<Map<String, String>>> schemaRows)
    {
        return fakeConnection(schemaRows, Map.of(), new ArrayList<>());
    }


    /**
     * Return a JDBC connection where every query fails except those whose SQL contains one of the supplied
     * fragments, which return the matching rows.
     *
     * @param firstRows rows for queries matching these fragments
     * @param moreRows rows for queries matching these fragments
     * @param boundValues receives the values bound to the statements' parameters
     * @return fake connection
     */
    private static Connection fakeConnection(Map<String, List<Map<String, String>>> firstRows,
                                             Map<String, List<Map<String, String>>> moreRows,
                                             List<String>                           boundValues)
    {
        return (Connection) Proxy.newProxyInstance(MSSQLDatabaseStatsExtractorTest.class.getClassLoader(),
                                                   new Class<?>[]{Connection.class},
                                                   (proxy, method, args) ->
        {
            if ("prepareStatement".equals(method.getName()))
            {
                String sql = (String)args[0];

                List<Map<String, String>> rows = null;

                for (Map.Entry<String, List<Map<String, String>>> entry : firstRows.entrySet())
                {
                    if (sql.contains(entry.getKey()))
                    {
                        rows = entry.getValue();
                    }
                }

                for (Map.Entry<String, List<Map<String, String>>> entry : moreRows.entrySet())
                {
                    if (sql.contains(entry.getKey()))
                    {
                        rows = entry.getValue();
                    }
                }

                return fakeStatement(rows, boundValues);
            }

            return null;
        });
    }


    /**
     * Return a prepared statement that returns the supplied rows, or fails when executed if there are none.
     *
     * @param rows rows to return (null means fail)
     * @param boundValues receives the values bound to the statement's parameters
     * @return fake statement
     */
    private static PreparedStatement fakeStatement(List<Map<String, String>> rows,
                                                   List<String>              boundValues)
    {
        return (PreparedStatement) Proxy.newProxyInstance(MSSQLDatabaseStatsExtractorTest.class.getClassLoader(),
                                                          new Class<?>[]{PreparedStatement.class},
                                                          (proxy, method, args) ->
        {
            if ("setString".equals(method.getName()))
            {
                boundValues.add((String)args[1]);
            }
            else if ("executeQuery".equals(method.getName()))
            {
                if (rows == null)
                {
                    throw new SQLException("permission denied");
                }

                return fakeResultSet(rows.iterator());
            }

            return null;
        });
    }


    /**
     * Return a result set over the supplied rows.  Missing columns read as null, zero or false.
     *
     * @param rows rows to return
     * @return fake result set
     */
    private static ResultSet fakeResultSet(Iterator<Map<String, String>> rows)
    {
        final List<Map<String, String>> currentRow = new ArrayList<>();

        return (ResultSet) Proxy.newProxyInstance(MSSQLDatabaseStatsExtractorTest.class.getClassLoader(),
                                                  new Class<?>[]{ResultSet.class},
                                                  (proxy, method, args) ->
        {
            switch (method.getName())
            {
                case "next":
                    currentRow.clear();
                    if (rows.hasNext())
                    {
                        currentRow.add(rows.next());
                        return true;
                    }
                    return false;
                case "getString":
                    return currentRow.get(0).get((String)args[0]);
                case "getInt":
                    return 0;
                case "getLong":
                    return 0L;
                case "getBoolean":
                    return false;
                default:
                    return null;
            }
        });
    }
}
