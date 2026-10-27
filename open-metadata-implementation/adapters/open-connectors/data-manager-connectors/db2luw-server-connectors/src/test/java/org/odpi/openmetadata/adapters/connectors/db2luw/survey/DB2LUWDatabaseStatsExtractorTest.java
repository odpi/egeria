/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.db2luw.survey;

import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.ResourceMeasureAnnotationProperties;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceConnector;
import org.odpi.openmetadata.frameworks.opensurvey.DatabaseElementAnnotation;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyDatabaseAnnotationType;
import org.odpi.openmetadata.frameworks.opensurvey.measurements.RelationalDataManagerMeasurement;
import org.odpi.openmetadata.frameworks.opensurvey.measurements.RelationalTableMetric;
import org.testng.annotations.Test;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;


/**
 * Verify that DB2LUWDatabaseStatsExtractor correctly converts Db2's raw SYSSTAT.COLUMNS.COLCARD catalog
 * value into an actual number-of-distinct-values estimate, per Db2's documented semantics (COLCARD is
 * -1 if statistics have not been gathered for the column - see
 * https://www.ibm.com/docs/en/db2/11.5?topic=views-syscatcolumns and
 * https://www1.columbia.edu/sec/acis/db2/db2d0/db2d0101.htm), and that it surveys only the schemas that the
 * includeSchemaNames and excludeSchemaNames properties allow.  It also checks the structure counts and that
 * a failing query (typically a missing privilege for the administrative views and monitoring functions) is
 * confined to the information that query retrieves rather than ending the survey.
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
                                                                                  null,
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
                                                                                  null,
                                                                                  null,
                                                                                  null);

        assertTrue(extractor.schemaShouldBeSurveyed("SALES"));
        assertFalse(extractor.schemaShouldBeSurveyed("DB2INST1"));
        assertFalse(extractor.schemaShouldBeSurveyed("SYSCAT"));
    }


    /**
     * The database counts are the sums of the schema counts, and each kind of table is counted separately.
     */
    @Test public void testDatabaseCounts()
    {
        RelationalDataManagerMeasurement measurement = new RelationalDataManagerMeasurement();
        measurement.setResourceName("db");

        DB2LUWDatabaseStatsExtractor.DatabaseDetails databaseDetails = new DB2LUWDatabaseStatsExtractor.DatabaseDetails(measurement);

        DB2LUWDatabaseStatsExtractor.SchemaDetails schema1 = databaseDetails.getSchemaDetails("db", "S1");
        schema1.getTableDetails("S1", "T1").getColumnDetails("C1");
        schema1.getTableDetails("S1", "T1").getColumnDetails("C2");
        schema1.getTableDetails("S1", "T2").getColumnDetails("C1");
        schema1.getTableDetails("S1", "V1").setViewDetails("owner", "select 1");

        DB2LUWDatabaseStatsExtractor.SchemaDetails schema2 = databaseDetails.getSchemaDetails("db", "S2");
        schema2.getTableDetails("S2", "T3").getColumnDetails("C1");
        schema2.getTableDetails("S2", "M1").setMaterializedViewDetails("owner", false, true, "select 1");
        schema2.getTableDetails("S2", "M1").getColumnDetails("C1");

        databaseDetails.setUpCounts();

        assertEquals(measurement.getSchemaCount(), 2L);
        assertEquals(measurement.getTableCount(), 3L);
        assertEquals(measurement.getViewCount(), 1L);
        assertEquals(measurement.getMaterializedViewCount(), 1L);
        assertEquals(measurement.getColumnCount(), 5L);
        assertEquals(schema1.getSchemaMeasurement().getColumnCount(), 3L);
    }


    /**
     * The administrative views and monitoring functions need more than the CONNECT privilege, so their
     * queries commonly fail.  When they do, and when one schema's catalog query fails too, the survey still
     * reports everything the other queries returned - and every per-schema query is bound to its schema.
     *
     * @throws Exception unexpected failure
     */
    @Test public void testFailingQueriesAreIsolated() throws Exception
    {
        Map<String, List<Map<String, Object>>> salesResults = new HashMap<>();
        salesResults.put("FROM SYSCAT.COLUMNS",  List.of(row("tablename", "T1", "columnname", "C1", "avgwidth", 4, "typename", "INTEGER", "isnullable", "N", "ndistinct", -1L),
                                                         row("tablename", "V1", "columnname", "C1", "avgwidth", 4, "typename", "INTEGER", "isnullable", "Y", "ndistinct", 7L)));
        salesResults.put("t.TYPE = 'T'",         List.of(row("tablename", "T1", "tableowner", "DB2INST1", "hasindexes", 1, "hastriggers", 0, "hasrowsecurity", 0)));
        salesResults.put("FROM SYSCAT.VIEWS v",  List.of(row("viewname", "V1", "viewowner", "DB2INST1", "definition", "select c1 from t1")));
        salesResults.put("ADMIN_GET_TAB_INFO",   List.of(row("tablename", "T1", "tablesize", 2L),
                                                         row("tablename", "GHOST", "tablesize", 9L)));

        Map<String, List<Map<String, Object>>> hrResults = new HashMap<>();
        hrResults.put("t.TYPE = 'T'",            List.of(row("tablename", "T9", "tableowner", "DB2INST1", "hasindexes", 0, "hastriggers", 0, "hasrowsecurity", 1)));

        Map<String, Map<String, List<Map<String, Object>>>> schemaResults = new HashMap<>();
        schemaResults.put("SALES", salesResults);
        schemaResults.put("HR", hrResults);

        Connection connection = fakeConnection((sql, schemaName) ->
        {
            if (sql.contains("FROM SYSCAT.SCHEMATA"))
            {
                return List.of(row("SCHEMANAME", "HR"), row("SCHEMANAME", "SALES"));
            }

            if ((sql.contains("SYSIBMADM.ADMINTABINFO")) || (sql.contains("MON_GET_TABLE")))
            {
                throw new SQLException("SQL0551N  The statement failed because the authorization ID does not have the required privilege.");
            }

            if (schemaName == null)
            {
                throw new SQLException("Per-schema query issued without a bound schema name: " + sql);
            }

            if ((schemaName.equals("HR")) && (sql.contains("FROM SYSCAT.COLUMNS")))
            {
                throw new SQLException("SQL0551N  No access to the columns of schema HR.");
            }

            for (String fragment : schemaResults.get(schemaName).keySet())
            {
                if (sql.contains(fragment))
                {
                    return schemaResults.get(schemaName).get(fragment);
                }
            }

            return new ArrayList<>();
        });

        /*
         * The extractor stops between schemas if the survey has been stopped, so the calling survey must
         * report that it is active.
         */
        SurveyActionServiceConnector surveyActionService = new SurveyActionServiceConnector()
        {
            @Override
            public synchronized boolean isActive()
            {
                return true;
            }
        };

        DB2LUWDatabaseStatsExtractor extractor = new DB2LUWDatabaseStatsExtractor(List.of("DB"),
                                                                                  null,
                                                                                  null,
                                                                                  surveyActionService,
                                                                                  null,
                                                                                  "TestSurvey");

        extractor.getDatabaseStatistics("DB", connection);
        extractor.getSchemaStatistics("DB", connection);

        Map<String, Map<String, String>> resourceProperties = new HashMap<>();
        List<DatabaseElementAnnotation> annotations        = extractor.getAnnotations("DB");

        assertNotNull(annotations);

        for (DatabaseElementAnnotation annotation : annotations)
        {
            if (SurveyDatabaseAnnotationType.TABLE_MEASUREMENTS.getName().equals(annotation.annotationProperties().getAnnotationType()))
            {
                Map<String, String> properties = ((ResourceMeasureAnnotationProperties)annotation.annotationProperties()).getResourceProperties();

                /*
                 * The names identify the catalogued table that the annotation is linked to.
                 */
                assertEquals(properties.get(RelationalTableMetric.TABLE_QNAME.getDisplayName()),
                             annotation.databaseName() + "." + annotation.schemaName() + "." + annotation.tableName());
                assertNull(annotation.columnName());

                resourceProperties.put(properties.get(RelationalTableMetric.TABLE_QNAME.getDisplayName()), properties);
            }
        }

        /*
         * The table sizes came back, the row counts did not, and the size of a table that no other query found
         * did not create a table.
         */
        assertEquals(resourceProperties.keySet().size(), 3);
        assertEquals(resourceProperties.get("DB.SALES.T1").get(RelationalTableMetric.TABLE_SIZE.getDisplayName()), "2048");
        assertEquals(resourceProperties.get("DB.SALES.T1").get(RelationalTableMetric.NUMBER_OF_ROWS_INSERTED.getDisplayName()), "0");
        assertEquals(resourceProperties.get("DB.SALES.V1").get(RelationalTableMetric.TABLE_TYPE.getDisplayName()), "View");

        /*
         * The failed column query for HR did not stop its tables being reported.
         */
        assertEquals(resourceProperties.get("DB.HR.T9").get(RelationalTableMetric.HAS_ROW_SECURITY.getDisplayName()), "true");

        /*
         * The results for the database are released once its annotations are produced.
         */
        assertNull(extractor.getAnnotations("DB"));
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

        DB2LUWDatabaseStatsExtractor extractor = new DB2LUWDatabaseStatsExtractor(List.of("DB"), surveyActionService);

        /*
         * The database size query needs more than the CONNECT privilege, so it commonly fails.
         */
        extractor.getDatabaseStatistics("DB", fakeConnection((sql, schemaName) ->
        {
            throw new SQLException("SQL0551N  The statement failed because the authorization ID does not have the required privilege.");
        }));

        List<DatabaseElementAnnotation> annotations = extractor.getDatabaseAnnotations();

        assertNotNull(annotations);
        assertEquals(annotations.size(), 1);

        for (DatabaseElementAnnotation annotation : annotations)
        {
            assertEquals(annotation.annotationProperties().getAnnotationType(), SurveyDatabaseAnnotationType.DATABASE_MEASUREMENTS.getName());
            assertNotNull(annotation.databaseName());
            assertNull(annotation.schemaName());
        }

        assertNull(extractor.getDatabaseAnnotations());
        assertNull(extractor.getAnnotations("DB"));
    }


    /**
     * Build a result set row from alternating column names and values.
     *
     * @param namesAndValues column name, value, column name, value ...
     * @return row
     */
    private static Map<String, Object> row(Object... namesAndValues)
    {
        Map<String, Object> row = new HashMap<>();

        for (int i = 0; i + 1 < namesAndValues.length; i = i + 2)
        {
            row.put(namesAndValues[i].toString().toLowerCase(), namesAndValues[i + 1]);
        }

        return row;
    }


    /**
     * Supplies the rows for a query, or fails it.
     */
    private interface QueryHandler
    {
        /**
         * Return the rows for a query.
         *
         * @param sql the query
         * @param schemaName the value bound to its first parameter, or null if none was bound
         * @return rows
         * @throws SQLException simulated failure
         */
        List<Map<String, Object>> getRows(String sql, String schemaName) throws SQLException;
    }


    /**
     * Create a JDBC connection that answers queries from the handler.  Only the methods that the extractor
     * uses are supported.
     *
     * @param handler supplies the results
     * @return connection
     */
    private static Connection fakeConnection(QueryHandler handler)
    {
        return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                                                   new Class<?>[]{Connection.class},
                                                   (connectionProxy, connectionMethod, connectionArgs) ->
        {
            if (connectionMethod.getName().equals("prepareStatement"))
            {
                String   sql         = (String)connectionArgs[0];
                String[] boundSchema = new String[1];

                return Proxy.newProxyInstance(PreparedStatement.class.getClassLoader(),
                                              new Class<?>[]{PreparedStatement.class},
                                              (statementProxy, statementMethod, statementArgs) ->
                {
                    if (statementMethod.getName().equals("setString"))
                    {
                        boundSchema[0] = (String)statementArgs[1];
                    }
                    else if (statementMethod.getName().equals("executeQuery"))
                    {
                        return fakeResultSet(handler.getRows(sql, boundSchema[0]));
                    }

                    return null;
                });
            }

            return null;
        });
    }


    /**
     * Create a JDBC result set over the supplied rows.  Column names are matched without regard to case, as
     * JDBC does.
     *
     * @param rows rows to return
     * @return result set
     */
    private static ResultSet fakeResultSet(List<Map<String, Object>> rows)
    {
        Iterator<Map<String, Object>> iterator   = rows.iterator();
        List<Map<String, Object>>     currentRow = new ArrayList<>();

        return (ResultSet) Proxy.newProxyInstance(ResultSet.class.getClassLoader(),
                                                  new Class<?>[]{ResultSet.class},
                                                  (proxy, method, args) ->
        {
            switch (method.getName())
            {
                case "next":
                    currentRow.clear();
                    if (iterator.hasNext())
                    {
                        currentRow.add(iterator.next());
                        return true;
                    }
                    return false;

                case "getString":
                    Object stringValue = currentRow.get(0).get(args[0].toString().toLowerCase());
                    return (stringValue == null) ? null : stringValue.toString();

                case "getLong":
                    Object longValue = currentRow.get(0).get(args[0].toString().toLowerCase());
                    return (longValue == null) ? 0L : ((Number)longValue).longValue();

                case "getInt":
                    Object intValue = currentRow.get(0).get(args[0].toString().toLowerCase());
                    return (intValue == null) ? 0 : ((Number)intValue).intValue();

                case "getBoolean":
                    Object booleanValue = currentRow.get(0).get(args[0].toString().toLowerCase());
                    return (booleanValue != null) && (((Number)booleanValue).intValue() != 0);

                default:
                    return null;
            }
        });
    }
}
