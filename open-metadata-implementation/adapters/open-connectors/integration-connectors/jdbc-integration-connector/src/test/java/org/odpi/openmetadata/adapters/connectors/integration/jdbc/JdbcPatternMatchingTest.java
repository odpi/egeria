/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc;

import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.JdbcMetadata;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.model.JdbcColumn;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.model.JdbcTable;
import org.testng.annotations.Test;

import java.lang.reflect.Proxy;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * JdbcPatternMatchingTest pins down how the JDBC cataloguer picks out the tables and columns that belong where it
 * is cataloguing.
 * <br><br>
 * DatabaseMetaData.getTables and getColumns take <b>patterns</b>, not names.  A null schema matches every schema,
 * and "_" and "%" in a name are wildcards.  The cataloguer's database-level pass, which is meant to catalogue only
 * the tables that sit directly under the database, asked for the tables of a null schema - and so catalogued every
 * table in every schema a second time, under the database, with all of its columns.  The tests run against a fake
 * DatabaseMetaData that applies the patterns the way a driver does.
 */
public class JdbcPatternMatchingTest
{
    private static final String[] TABLE_TYPES = {"TABLE"};

    /*
     * schema, table, column - a database with two schemas that share a table name, and a table whose name differs
     * from another only where the other has "_".
     */
    private static final String[][] COLUMNS = {
            {"sales",   "orders",     "order_id"},
            {"sales",   "orders",     "customer_id"},
            {"sales",   "order_line", "line_id"},
            {"sales",   "orderXline", "x_id"},
            {"finance", "orders",     "ledger_id"},
            {null,      "settings",   "setting_name"}};


    /**
     * The database-level pass picks out only the table that has no schema, even though JDBC returns every table.
     */
    @Test
    public void onlyTablesWithNoSchemaSitUnderTheDatabase() throws Exception
    {
        List<JdbcTable> fromJdbc = jdbcMetadata().getTables(null, null, null, TABLE_TYPES);

        assertEquals(fromJdbc.size(), 5, "the fake driver should behave like a real one and return every table for a null schema");

        List<JdbcTable> underDatabase = RelationalDatabaseCataloguer.tablesInSchema(fromJdbc, null);

        assertEquals(underDatabase.size(), 1, "only the table with no schema belongs directly under the database");
        assertEquals(underDatabase.get(0).getTableName(), "settings");
    }


    /**
     * A table whose schema JDBC reports as an empty string also has no schema.
     */
    @Test
    public void anEmptySchemaCountsAsNoSchema() throws Exception
    {
        List<JdbcTable> tables = jdbcMetadata(new String[][] {{"", "settings", "setting_name"}}).getTables(null, null, null, TABLE_TYPES);

        assertEquals(RelationalDatabaseCataloguer.tablesInSchema(tables, null).size(), 1);
        assertEquals(RelationalDatabaseCataloguer.tablesInSchema(tables, "sales").size(), 0);
    }


    /**
     * A schema's pass picks out only that schema's tables, even when the schema name contains a wildcard.
     */
    @Test
    public void aSchemaGetsOnlyItsOwnTables() throws Exception
    {
        List<JdbcTable> fromJdbc = jdbcMetadata().getTables(null, "sale_", null, TABLE_TYPES);

        assertTrue(fromJdbc.size() > 0, "the fake driver treats '_' as a wildcard, so sale_ matches sales");
        assertEquals(RelationalDatabaseCataloguer.tablesInSchema(fromJdbc, "sale_").size(), 0,
                     "no schema is actually named sale_");

        List<JdbcTable> sales = RelationalDatabaseCataloguer.tablesInSchema(jdbcMetadata().getTables(null, "sales", null, TABLE_TYPES), "sales");

        assertEquals(sales.size(), 3);

        for (JdbcTable table : sales)
        {
            assertEquals(table.getTableSchem(), "sales");
        }
    }


    /**
     * A table's columns are only its own, not those of tables whose names match it as a pattern, or of the
     * table of the same name in another schema.
     */
    @Test
    public void aTableGetsOnlyItsOwnColumns() throws Exception
    {
        List<JdbcColumn> fromJdbc = jdbcMetadata().getColumns(null, "sales", "order_line", null);

        assertEquals(fromJdbc.size(), 2, "the fake driver matches order_line to orderXline too");

        List<JdbcColumn> columns = RelationalDatabaseCataloguer.columnsOfTable(fromJdbc, "sales", "order_line");

        assertEquals(columns.size(), 1);
        assertEquals(columns.get(0).getColumnName(), "line_id");

        List<JdbcColumn> anySchema = jdbcMetadata().getColumns(null, null, "orders", null);

        assertEquals(anySchema.size(), 3, "a null schema returns the columns of orders in both schemas");
        assertEquals(RelationalDatabaseCataloguer.columnsOfTable(anySchema, "finance", "orders").size(), 1);
        assertEquals(RelationalDatabaseCataloguer.columnsOfTable(anySchema, null, "orders").size(), 0,
                     "no table called orders sits directly under the database");
    }


    /**
     * Build JDBC metadata over the standard fake database.
     *
     * @return metadata
     */
    private JdbcMetadata jdbcMetadata()
    {
        return jdbcMetadata(COLUMNS);
    }


    /**
     * Build JDBC metadata over a fake DatabaseMetaData that applies schema and table patterns as a driver does.
     *
     * @param columns schema, table and column of each column in the database
     * @return metadata
     */
    private JdbcMetadata jdbcMetadata(String[][] columns)
    {
        DatabaseMetaData databaseMetaData = (DatabaseMetaData) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class<?>[] {DatabaseMetaData.class},
                (proxy, method, args) ->
                {
                    List<Map<String, String>> rows    = new ArrayList<>();
                    List<String>              tables  = new ArrayList<>();

                    switch (method.getName())
                    {
                        case "getTables" ->
                        {
                            for (String[] column : columns)
                            {
                                String key = column[0] + "." + column[1];

                                if (matches((String) args[1], column[0]) && matches((String) args[2], column[1]) && ! tables.contains(key))
                                {
                                    tables.add(key);
                                    rows.add(row("TABLE_SCHEM", column[0], "TABLE_NAME", column[1], "TABLE_TYPE", "TABLE"));
                                }
                            }
                        }
                        case "getColumns" ->
                        {
                            for (String[] column : columns)
                            {
                                if (matches((String) args[1], column[0]) && matches((String) args[2], column[1]))
                                {
                                    rows.add(row("TABLE_SCHEM", column[0], "TABLE_NAME", column[1], "COLUMN_NAME", column[2]));
                                }
                            }
                        }
                        default -> throw new UnsupportedOperationException(method.getName());
                    }

                    return resultSet(rows);
                });

        return new JdbcMetadata(databaseMetaData);
    }


    /**
     * Apply a JDBC search pattern: null matches anything, "%" matches any characters and "_" any one character.
     *
     * @param pattern pattern from the caller
     * @param value value to test (null for "no schema")
     * @return true if it matches
     */
    private static boolean matches(String pattern, String value)
    {
        if (pattern == null)
        {
            return true;
        }

        if (value == null)
        {
            return false;
        }

        return value.matches(pattern.replace("%", ".*").replace("_", "."));
    }


    /**
     * Build one result row.
     *
     * @param keysAndValues alternating column names and values
     * @return row
     */
    private static Map<String, String> row(String... keysAndValues)
    {
        Map<String, String> row = new java.util.HashMap<>();

        for (int i = 0; i + 1 < keysAndValues.length; i = i + 2)
        {
            row.put(keysAndValues[i], keysAndValues[i + 1]);
        }

        return row;
    }


    /**
     * Wrap rows as a forward-only ResultSet.  Unknown string columns are null and numeric columns are zero.
     *
     * @param rows rows to return
     * @return result set
     */
    private static ResultSet resultSet(List<Map<String, String>> rows)
    {
        Iterator<Map<String, String>> iterator = rows.iterator();
        List<Map<String, String>>     current  = new ArrayList<>(List.of(Map.of()));

        return (ResultSet) Proxy.newProxyInstance(
                JdbcPatternMatchingTest.class.getClassLoader(),
                new Class<?>[] {ResultSet.class},
                (proxy, method, args) -> switch (method.getName())
                {
                    case "next" ->
                    {
                        boolean hasNext = iterator.hasNext();
                        current.set(0, hasNext ? iterator.next() : Map.of());
                        yield hasNext;
                    }
                    case "getString" -> current.get(0).get((String) args[0]);
                    case "getInt" -> 0;
                    case "getShort" -> (short) 0;
                    case "getBoolean" -> false;
                    case "isClosed" -> false;
                    case "close" -> null;
                    default -> throw new UnsupportedOperationException(method.getName());
                });
    }
}
