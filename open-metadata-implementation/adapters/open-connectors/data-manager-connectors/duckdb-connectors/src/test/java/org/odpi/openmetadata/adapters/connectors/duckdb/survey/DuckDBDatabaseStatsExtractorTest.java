/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.duckdb.survey;

import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


/**
 * Verify that DuckDBDatabaseStatsExtractor surveys only the schemas that the includeSchemaNames and
 * excludeSchemaNames properties allow, and never DuckDB's own catalog schemas; that its counts add up; and
 * that a failed query leaves a gap in the results rather than ending the survey.
 */
public class DuckDBDatabaseStatsExtractorTest
{
    /**
     * With neither list set, every schema is surveyed - only the caller's lists leave a schema out.
     */
    @Test public void testUnfilteredSurveyCoversEverySchema()
    {
        DuckDBDatabaseStatsExtractor extractor = new DuckDBDatabaseStatsExtractor(List.of("db"), null);

        assertTrue(extractor.schemaShouldBeSurveyed("main"));
        assertTrue(extractor.schemaShouldBeSurveyed("sales"));
        assertTrue(extractor.schemaShouldBeSurveyed("information_schema"));
        assertTrue(extractor.schemaShouldBeSurveyed("pg_catalog"));
        assertFalse(extractor.schemaShouldBeSurveyed(null));
    }


    /**
     * The exclude list removes the schemas it names and nothing else.  Names are matched exactly, so a
     * schema whose name differs only in case is not excluded.
     */
    @Test public void testExcludedSchemasAreSkipped()
    {
        DuckDBDatabaseStatsExtractor extractor = new DuckDBDatabaseStatsExtractor(List.of("db"),
                                                                                  List.of("sales"),
                                                                                  null,
                                                                                  null,
                                                                                  null,
                                                                                  null);

        assertFalse(extractor.schemaShouldBeSurveyed("sales"));
        assertTrue(extractor.schemaShouldBeSurveyed("SALES"));
        assertTrue(extractor.schemaShouldBeSurveyed("main"));
    }


    /**
     * The include list names the only schemas surveyed, and wins over the exclude list.
     */
    @Test public void testIncludedSchemasTakePrecedence()
    {
        DuckDBDatabaseStatsExtractor extractor = new DuckDBDatabaseStatsExtractor(List.of("db"),
                                                                                  List.of("sales"),
                                                                                  List.of("sales", "information_schema"),
                                                                                  null,
                                                                                  null,
                                                                                  null);

        assertTrue(extractor.schemaShouldBeSurveyed("sales"));
        assertFalse(extractor.schemaShouldBeSurveyed("main"));
        assertTrue(extractor.schemaShouldBeSurveyed("information_schema"));
    }


    /**
     * The column pass checks whether a table is known before recording its columns.  That check must not
     * create an entry for the schema it asks about, or a schema that is not being surveyed (and which
     * therefore has no tables recorded) would still turn up, empty, in the results and the schema count.
     */
    @Test public void testTableLookupDoesNotCreateSchema()
    {
        DuckDBDatabaseStatsExtractor.DatabaseDetails databaseDetails = new DuckDBDatabaseStatsExtractor.DatabaseDetails("db");

        assertFalse(databaseDetails.hasTable("excluded_schema", "some_table"));
        assertTrue(databaseDetails.getSchemaNames().isEmpty());

        databaseDetails.getSchemaDetails("main").getTableDetails("orders");

        assertTrue(databaseDetails.hasTable("main", "orders"));
        assertFalse(databaseDetails.hasTable("main", "customers"));
        assertEquals(databaseDetails.getSchemaNames(), List.of("main"));
    }


    /**
     * DuckDB's catalog functions list every database the connection can see, including the databases
     * ATTACH-ed to it.  Only the surveyed database's schemas and tables are surveyed: a table in an attached
     * database must not turn up in the surveyed database's schema of the same name.
     */
    @Test public void testAttachedDatabasesAreNotSurveyed() throws Exception
    {
        try (Connection connection = DriverManager.getConnection("jdbc:duckdb:");
             Statement  statement  = connection.createStatement())
        {
            statement.execute("ATTACH ':memory:' AS attached_db");
            statement.execute("CREATE SCHEMA sales");
            statement.execute("CREATE TABLE sales.orders (id INTEGER)");
            statement.execute("CREATE SCHEMA attached_db.sales");
            statement.execute("CREATE TABLE attached_db.sales.returns (id INTEGER, reason VARCHAR)");
            statement.execute("CREATE TABLE attached_db.main.stray (id INTEGER)");
            statement.execute("CREATE SCHEMA attached_db.only_attached");

            DuckDBDatabaseStatsExtractor extractor = new DuckDBDatabaseStatsExtractor(List.of("db"), null);

            extractor.getSchemaStatistics("db", connection);

            DuckDBDatabaseStatsExtractor.DatabaseDetails databaseDetails = extractor.getDatabaseDetails("db");

            assertTrue(databaseDetails.hasTable("sales", "orders"));
            assertFalse(databaseDetails.hasTable("sales", "returns"));
            assertFalse(databaseDetails.hasTable("main", "stray"));
            assertFalse(databaseDetails.getSchemaNames().contains("only_attached"));
            assertEquals(databaseDetails.getSchemaDetails("sales").getColumnCount(), 1L);
        }
    }


    /**
     * The database and schema counts are the totals over every schema, table, view and column recorded.
     */
    @Test public void testDatabaseCounts()
    {
        DuckDBDatabaseStatsExtractor.DatabaseDetails databaseDetails = new DuckDBDatabaseStatsExtractor.DatabaseDetails("db");

        DuckDBDatabaseStatsExtractor.SchemaDetails schema1 = databaseDetails.getSchemaDetails("s1");
        schema1.getTableDetails("t1").setTableMeasurements("Table", false, 2, "create table t1");
        schema1.getTableDetails("t1").getColumnDetails("c1");
        schema1.getTableDetails("t1").getColumnDetails("c2");
        schema1.getTableDetails("t2").setTableMeasurements("Table", false, 1, "create table t2");
        schema1.getTableDetails("t2").getColumnDetails("c1");
        schema1.getTableDetails("v1").setTableMeasurements("View", false, 1, "create view v1");
        schema1.getTableDetails("v1").getColumnDetails("c1");

        DuckDBDatabaseStatsExtractor.SchemaDetails schema2 = databaseDetails.getSchemaDetails("s2");
        schema2.getTableDetails("t3").setTableMeasurements("Table", true, 1, "create table t3");
        schema2.getTableDetails("t3").getColumnDetails("c1");
        schema2.getTableDetails("v2").setTableMeasurements("View", false, 1, "create view v2");
        schema2.getTableDetails("v2").getColumnDetails("c1");

        databaseDetails.setUpCounts();

        assertEquals(databaseDetails.getDatabaseMeasurements().getSchemaCount(), 2L);
        assertEquals(databaseDetails.getDatabaseMeasurements().getTableCount(), 3L);
        assertEquals(databaseDetails.getDatabaseMeasurements().getViewCount(), 2L);
        assertEquals(databaseDetails.getDatabaseMeasurements().getColumnCount(), 6L);
        assertEquals(schema1.getSchemaMeasurement().getColumnCount(), 4L);
        assertEquals(schema1.getSchemaMeasurement().getViewCount(), 1L);
    }


    /**
     * A table whose columns could not be read keeps the column count that DuckDB's catalog reported for it,
     * rather than being counted as having none.
     */
    @Test public void testColumnCountWithoutColumnDetails()
    {
        DuckDBDatabaseStatsExtractor.TableDetails tableDetails = new DuckDBDatabaseStatsExtractor.TableDetails("db.main", "t1");

        tableDetails.setTableMeasurements("Table", false, 3, "create table t1");

        assertEquals(tableDetails.getColumnCount(), 3L);

        tableDetails.getColumnDetails("c1");

        assertEquals(tableDetails.getColumnCount(), 1L);
    }


    /**
     * A query that fails is skipped: the other queries for the schema still contribute their results.  The
     * views query is made to fail here; the tables and their columns must still be surveyed.
     */
    @Test public void testFailedQueryIsIsolated() throws Exception
    {
        try (Connection connection = DriverManager.getConnection("jdbc:duckdb:");
             Statement  statement  = connection.createStatement())
        {
            statement.execute("CREATE SCHEMA sales");
            statement.execute("CREATE TABLE sales.orders (id INTEGER, amount DECIMAL(10,2))");
            statement.execute("CREATE VIEW sales.big_orders AS SELECT * FROM sales.orders WHERE amount > 100");
            statement.execute("CREATE TABLE main.\"MixedCase\" (\"Id\" INTEGER)");

            Connection failingConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                                                                               new Class<?>[]{Connection.class},
                                                                               (proxy, method, args) ->
            {
                if (("prepareStatement".equals(method.getName())) && (args[0].toString().contains("duckdb_views()")))
                {
                    throw new SQLException("Simulated failure of the views query");
                }

                try
                {
                    return method.invoke(connection, args);
                }
                catch (InvocationTargetException error)
                {
                    throw error.getCause();
                }
            });

            DuckDBDatabaseStatsExtractor extractor = new DuckDBDatabaseStatsExtractor(List.of("db"), null);

            extractor.getDatabaseStatistics(failingConnection);
            extractor.getSchemaStatistics("db", failingConnection);

            DuckDBDatabaseStatsExtractor.DatabaseDetails databaseDetails = extractor.getDatabaseDetails("db");

            assertTrue(databaseDetails.hasTable("sales", "orders"));
            assertFalse(databaseDetails.hasTable("sales", "big_orders"));
            assertTrue(databaseDetails.hasTable("main", "MixedCase"));
            assertEquals(databaseDetails.getSchemaDetails("sales").getColumnCount(), 2L);
            assertEquals(databaseDetails.getDatabaseMeasurements().getSchemaCount(), 2L);
            assertEquals(databaseDetails.getDatabaseMeasurements().getTableCount(), 2L);
            assertEquals(databaseDetails.getDatabaseMeasurements().getViewCount(), 0L);
        }
    }
}
