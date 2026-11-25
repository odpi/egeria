/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ParquetDataReader retrieves the values stored in a Parquet file.
 * <br><br>
 * Reading the values in a Parquet file means supporting every version of the data page layout, every
 * encoding and every compression algorithm.  Rather than implement these, or add the Apache Parquet libraries
 * (and the Apache Hadoop libraries they depend on), this class passes the work to DuckDB, which has a complete
 * implementation of the Parquet format built in to its JDBC driver.
 * <br><br>
 * DuckDB is only called through the standard java.sql interfaces.  This means there is no compile time
 * dependency on DuckDB, and the rest of the Parquet connector works without it.  The DuckDB JDBC
 * driver needs to be on the class path for this class to work.  It is included in the OMAG Server Platform.
 * <br><br>
 * The DuckDB database is a private, in-memory, database that is only used to run queries against the file.
 * Nothing is stored in it.
 */
public class ParquetDataReader implements AutoCloseable
{
    private static final String DUCKDB_URL          = "jdbc:duckdb:";
    private static final String DUCKDB_DRIVER_CLASS = "org.duckdb.DuckDBDriver";

    /*
     * DuckDB adds this column, on request, to say where each row is located in the file.  It is used to retrieve
     * specific rows and to return rows in the order that they are stored.
     */
    private static final String ROW_NUMBER_COLUMN   = "file_row_number";

    /*
     * The number of rows that are retrieved together.  Callers typically step through the rows in order
     * and so the rows around the one requested are retrieved at the same time and saved.
     */
    private static final int    ROWS_PER_PAGE       = 1000;

    private Connection         connection       = null;
    private String             cachedFileName   = null;
    private long               cachedFirstRow   = -1L;
    private List<List<String>> cachedRows       = null;


    /**
     * Default constructor.
     */
    public ParquetDataReader()
    {
    }


    /**
     * Return whether the DuckDB JDBC driver is available, and so it is possible to read values.
     *
     * @return boolean
     */
    public static boolean isAvailable()
    {
        try
        {
            DriverManager.getDriver(DUCKDB_URL);
            return true;
        }
        catch (SQLException notRegistered)
        {
            /*
             * The driver registers itself when its class is loaded.  This normally happens automatically.
             * However, it does not if the driver was added to the class path by a class loader that the
             * DriverManager does not search.
             */
            try
            {
                Class.forName(DUCKDB_DRIVER_CLASS);
                DriverManager.getDriver(DUCKDB_URL);
                return true;
            }
            catch (ClassNotFoundException | SQLException | LinkageError notAvailable)
            {
                return false;
            }
        }
    }


    /**
     * Return a row from the file.
     *
     * @param fileName location of the file
     * @param rowNumber the position of the row in the file - the first row is 0
     * @return the value from each top-level column, as a string, or null if there is no such row.  A null value is
     *         returned as null and a nested value uses DuckDB's string format for lists, maps and structures.
     * @throws SQLException DuckDB is not available, or is not able to read the file
     */
    public synchronized List<String> readRow(String fileName,
                                             long   rowNumber) throws SQLException
    {
        if (rowNumber < 0)
        {
            return null;
        }

        if ((! fileName.equals(cachedFileName)) ||
            (rowNumber < cachedFirstRow) ||
            (rowNumber >= cachedFirstRow + cachedRows.size()))
        {
            /*
             * The filter on the row number is passed down to the reader of the file so that only the row groups
             * that contain these rows are read.
             */
            final String query = "SELECT COLUMNS(* EXCLUDE (" + ROW_NUMBER_COLUMN + "))::VARCHAR " +
                                 "FROM read_parquet(?, " + ROW_NUMBER_COLUMN + " = true) " +
                                 "WHERE " + ROW_NUMBER_COLUMN + " >= ? AND " + ROW_NUMBER_COLUMN + " < ? " +
                                 "ORDER BY " + ROW_NUMBER_COLUMN;

            long               firstRow = rowNumber - (rowNumber % ROWS_PER_PAGE);
            List<List<String>> rows     = new ArrayList<>();

            try (PreparedStatement statement = this.getConnection().prepareStatement(query))
            {
                statement.setString(1, fileName);
                statement.setLong(2, firstRow);
                statement.setLong(3, firstRow + ROWS_PER_PAGE);

                try (ResultSet resultSet = statement.executeQuery())
                {
                    int columnCount = resultSet.getMetaData().getColumnCount();

                    while (resultSet.next())
                    {
                        List<String> row = new ArrayList<>(columnCount);

                        for (int i = 1; i <= columnCount; i++)
                        {
                            row.add(resultSet.getString(i));
                        }

                        rows.add(row);
                    }
                }
            }

            cachedFileName = fileName;
            cachedFirstRow = firstRow;
            cachedRows     = rows;
        }

        int rowIndex = (int) (rowNumber - cachedFirstRow);

        if (rowIndex >= cachedRows.size())
        {
            return null;
        }

        return cachedRows.get(rowIndex);
    }


    /**
     * Return how many times each value occurs in a top-level column, with the most frequent value first.
     * Nulls are not included.
     *
     * @param fileName location of the file
     * @param columnName the name of a top-level column
     * @param maxDistinctValues the largest number of different values to return
     * @return map of value to the number of times it occurs, or null if the column has more
     *         than the maximum number of different values
     * @throws SQLException DuckDB is not available, or is not able to read the file
     */
    public synchronized Map<String, Long> getValueCounts(String fileName,
                                                         String columnName,
                                                         int    maxDistinctValues) throws SQLException
    {
        /*
         * A column name cannot be passed as a parameter, and so it is included in the query as a quoted
         * identifier.  Doubling any quotes in the name stops it from being read as anything else.
         */
        final String column = "\"" + columnName.replace("\"", "\"\"") + "\"";
        final String query  = "SELECT " + column + "::VARCHAR AS column_value, count(*) AS value_count " +
                              "FROM read_parquet(?) " +
                              "WHERE " + column + " IS NOT NULL " +
                              "GROUP BY column_value " +
                              "ORDER BY value_count DESC, column_value " +
                              "LIMIT ?";

        Map<String, Long> valueCounts = new LinkedHashMap<>();

        try (PreparedStatement statement = this.getConnection().prepareStatement(query))
        {
            statement.setString(1, fileName);
            statement.setInt(2, maxDistinctValues + 1);

            try (ResultSet resultSet = statement.executeQuery())
            {
                while (resultSet.next())
                {
                    valueCounts.put(resultSet.getString(1), resultSet.getLong(2));
                }
            }
        }

        if (valueCounts.size() > maxDistinctValues)
        {
            return null;
        }

        return valueCounts;
    }


    /**
     * Return the connection to the private DuckDB database, creating it if necessary.
     *
     * @return connection
     * @throws SQLException DuckDB is not available
     */
    private Connection getConnection() throws SQLException
    {
        if ((connection == null) || (connection.isClosed()))
        {
            if (! isAvailable())
            {
                throw new SQLException("The DuckDB JDBC driver (" + DUCKDB_DRIVER_CLASS + ") is not on the class path");
            }

            connection = DriverManager.getConnection(DUCKDB_URL);

            /*
             * Support for Parquet is built in to DuckDB.  These settings stop it from downloading, or loading,
             * any of its optional extensions, and then stop the settings from being changed.
             */
            try (Statement statement = connection.createStatement())
            {
                statement.execute("SET autoinstall_known_extensions = false");
                statement.execute("SET autoload_known_extensions = false");
                statement.execute("SET lock_configuration = true");
            }
        }

        return connection;
    }


    /**
     * Release the DuckDB database.
     */
    @Override
    public synchronized void close()
    {
        cachedFileName = null;
        cachedRows     = null;
        cachedFirstRow = -1L;

        if (connection != null)
        {
            try
            {
                connection.close();
            }
            catch (SQLException error)
            {
                /*
                 * Nothing more can be done to release the database.
                 */
            }

            connection = null;
        }
    }
}
