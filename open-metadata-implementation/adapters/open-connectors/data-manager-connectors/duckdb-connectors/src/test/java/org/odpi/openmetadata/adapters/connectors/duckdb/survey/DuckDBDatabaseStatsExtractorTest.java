/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.duckdb.survey;

import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


/**
 * Verify that DuckDBDatabaseStatsExtractor surveys only the schemas that the includeSchemaNames and
 * excludeSchemaNames properties allow, and never DuckDB's own catalog schemas.
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
}
