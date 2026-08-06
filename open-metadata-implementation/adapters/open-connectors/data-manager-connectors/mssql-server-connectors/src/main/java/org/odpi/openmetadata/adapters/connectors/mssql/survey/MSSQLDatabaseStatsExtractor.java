/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.mssql.survey;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.ResourceMeasureAnnotationProperties;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceConnector;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyDatabaseAnnotationType;
import org.odpi.openmetadata.frameworks.opensurvey.measurements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.AnnotationProperties;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * MSSQLDatabaseStatsExtractor gathers the same measurements as the PostgreSQL equivalent
 * (org.odpi.openmetadata.adapters.connectors.postgres.survey.PostgresDatabaseStatsExtractor) but sources
 * them from Microsoft SQL Server's system catalog views and DMVs (sys.databases, sys.tables, sys.views,
 * sys.columns, sys.dm_db_partition_stats, sys.dm_db_index_operational_stats, sys.dm_db_stats_histogram)
 * instead of pg_catalog.  All of these sources are metadata/statistics only - no user data is read.
 */
public class MSSQLDatabaseStatsExtractor
{
    private final List<String>                 validDatabases;
    private final SurveyActionServiceConnector surveyActionServiceConnector;
    private final Map<String, DatabaseDetails> databaseResults = new HashMap<>();


    /**
     * Constructor sets up the list of databases to process and the connection to the database.
     *
     * @param validDatabases               list of database names
     * @param surveyActionServiceConnector calling connector
     */
    public MSSQLDatabaseStatsExtractor(List<String>                validDatabases,
                                       SurveyActionServiceConnector surveyActionServiceConnector)
    {
        this.validDatabases               = validDatabases;
        this.surveyActionServiceConnector = surveyActionServiceConnector;
    }


    /**
     * Retrieve statistics about each requested database.
     * This works whether connected to the master database or a user database, since sys.databases and
     * sys.master_files are server-wide catalog views visible from any connection.
     *
     * @param serverJDBCConnection connection to the SQL Server instance
     * @throws SQLException            problem accessing the database
     * @throws PropertyServerException problem create JSON properties
     */
    void getDatabaseStatistics(java.sql.Connection serverJDBCConnection) throws SQLException, PropertyServerException
    {
        final String databasesSQLCommand = "SELECT name FROM sys.databases;";

        final String databaseActivitySQLCommand =
                "SELECT d.name AS dbname, " +
                        "       SUM(ius.user_seeks + ius.user_scans + ius.user_lookups) AS rowsfetched, " +
                        "       SUM(ius.user_updates) AS rowsupdated " +
                        "FROM sys.dm_db_index_usage_stats ius " +
                        "JOIN sys.databases d ON d.database_id = ius.database_id " +
                        "GROUP BY d.name;";

        try
        {
            /*
             * Identify the requested databases that actually exist on this server.
             */
            PreparedStatement preparedStatement = serverJDBCConnection.prepareStatement(databasesSQLCommand);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                String databaseName = resultSet.getString("name");

                if (validDatabases.contains(databaseName))
                {
                    RelationalDataManagerMeasurement databaseMeasurement = new RelationalDataManagerMeasurement();

                    databaseMeasurement.setResourceName(databaseName);

                    databaseResults.put(databaseName, new DatabaseDetails(databaseMeasurement));
                }
            }

            resultSet.close();
            preparedStatement.close();

            /*
             * Add the read/write activity counters.  Microsoft SQL Server does not expose a database-wide split of
             * inserted/updated/deleted row counts the way PostgreSQL's pg_stat_database does (that granularity is
             * only available per-table, via sys.dm_db_index_operational_stats, applied in getSchemaStatistics
             * below), so rowsInserted/rowsDeleted are left at their default of zero at the database level.
             */
            preparedStatement = serverJDBCConnection.prepareStatement(databaseActivitySQLCommand);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                DatabaseDetails currentDatabase = databaseResults.get(resultSet.getString("dbname"));

                if (currentDatabase != null)
                {
                    currentDatabase.setActivity(resultSet.getLong("rowsfetched"), resultSet.getLong("rowsupdated"));
                }
            }

            resultSet.close();
            preparedStatement.close();

            /*
             * Extract the total size of each database from sys.master_files (a server-wide catalog view).
             */
            for (String databaseName : validDatabases)
            {
                DatabaseDetails currentDatabase = databaseResults.get(databaseName);

                if (currentDatabase != null)
                {
                    final String databaseSizeSQLCommand =
                            "SELECT SUM(CAST(size AS BIGINT)) * 8 * 1024 AS databasesize " +
                                    "FROM sys.master_files WHERE database_id = DB_ID(N'" + databaseName + "');";

                    preparedStatement = serverJDBCConnection.prepareStatement(databaseSizeSQLCommand);

                    resultSet = preparedStatement.executeQuery();

                    if (resultSet.next())
                    {
                        currentDatabase.setSize(resultSet.getLong("databasesize"));
                    }

                    resultSet.close();
                    preparedStatement.close();

                    databaseResults.put(databaseName, currentDatabase);
                }
            }
        }
        catch (SQLException sqlException)
        {
            try
            {
                serverJDBCConnection.rollback();
            }
            catch (Exception error)
            {
                // ignore
            }

            throw sqlException;
        }
    }


    /**
     * Extract detailed information about the schemas, tables and columns in a database.
     * This only returns information from the connected database - so the connection needs to be pointing at the
     * named user database rather than master.
     *
     * @param databaseName          name of database that is connected to
     * @param databaseSQLConnection connection to the named database
     * @throws SQLException            problem accessing the database
     */
    void getSchemaStatistics(String databaseName,
                             java.sql.Connection databaseSQLConnection) throws SQLException
    {
        /*
         * One row per column, giving its declared width/type/nullability plus a best-effort distinct-value estimate
         * and single most-common-value/frequency, both sourced from Microsoft SQL Server's own auto-collected
         * column statistics (sys.dm_db_stats_histogram) - an already-collected estimate, just like PostgreSQL's
         * pg_stats (populated by ANALYZE), so no user data is scanned to build this annotation.
         */
        final String columnStatsSQLCommand =
                "SELECT schemaname, tablename, columnname, avgwidth, typename, isnullable, mostcommonvalue, mostcommonfreq, ndistinct " +
                        "FROM ( " +
                        "    SELECT s.name AS schemaname, " +
                        "           t.name AS tablename, " +
                        "           c.name AS columnname, " +
                        "           c.max_length AS avgwidth, " +
                        "           ty.name AS typename, " +
                        "           c.is_nullable AS isnullable, " +
                        "           CONVERT(nvarchar(4000), h.range_high_key) AS mostcommonvalue, " +
                        "           h.equal_rows AS mostcommonfreq, " +
                        "           SUM(h.distinct_range_rows + 1) OVER (PARTITION BY c.object_id, c.column_id) AS ndistinct, " +
                        "           ROW_NUMBER() OVER (PARTITION BY c.object_id, c.column_id ORDER BY h.equal_rows DESC) AS rn " +
                        "    FROM sys.columns c " +
                        "    JOIN sys.types ty ON c.user_type_id = ty.user_type_id " +
                        "    JOIN sys.tables t ON t.object_id = c.object_id " +
                        "    JOIN sys.schemas s ON s.schema_id = t.schema_id " +
                        "    LEFT OUTER JOIN sys.stats_columns sc ON sc.object_id = c.object_id AND sc.column_id = c.column_id AND sc.stats_column_id = 1 " +
                        "    LEFT OUTER JOIN sys.stats st ON st.object_id = sc.object_id AND st.stats_id = sc.stats_id " +
                        "    OUTER APPLY sys.dm_db_stats_histogram(st.object_id, st.stats_id) h " +
                        "    WHERE s.name NOT IN (N'sys', N'INFORMATION_SCHEMA') " +
                        ") ranked " +
                        "WHERE rn = 1;";

        /*
         * One row per table, with insert/update/delete counts sourced from sys.dm_db_index_operational_stats -
         * the direct Microsoft SQL Server analog of PostgreSQL's pg_stat_user_tables.n_tup_ins/n_tup_upd/n_tup_del.
         */
        final String tablesSQLCommand =
                "SELECT s.name AS schemaname, " +
                        "       t.name AS tablename, " +
                        "       COALESCE(USER_NAME(t.principal_id), SCHEMA_NAME(t.schema_id)) AS tableowner, " +
                        "       CASE WHEN EXISTS (SELECT 1 FROM sys.indexes i WHERE i.object_id = t.object_id AND i.index_id > 0) THEN 1 ELSE 0 END AS hasindexes, " +
                        "       CASE WHEN EXISTS (SELECT 1 FROM sys.triggers tr WHERE tr.parent_id = t.object_id) THEN 1 ELSE 0 END AS hastriggers, " +
                        "       CASE WHEN EXISTS (SELECT 1 FROM sys.security_predicates sp WHERE sp.target_object_id = t.object_id) THEN 1 ELSE 0 END AS hasrowsecurity, " +
                        "       ISNULL(ops.n_tup_ins, 0) AS n_tup_ins, " +
                        "       ISNULL(ops.n_tup_upd, 0) AS n_tup_upd, " +
                        "       ISNULL(ops.n_tup_del, 0) AS n_tup_del " +
                        "FROM sys.tables t " +
                        "JOIN sys.schemas s ON t.schema_id = s.schema_id " +
                        "OUTER APPLY ( " +
                        "    SELECT SUM(leaf_insert_count) AS n_tup_ins, SUM(leaf_update_count) AS n_tup_upd, SUM(leaf_delete_count) AS n_tup_del " +
                        "    FROM sys.dm_db_index_operational_stats(DB_ID(), t.object_id, NULL, NULL) " +
                        ") ops " +
                        "WHERE s.name NOT IN (N'sys', N'INFORMATION_SCHEMA');";

        /*
         * Plain views only - views with a unique clustered index (indexed views) are Microsoft SQL Server's closest
         * equivalent to PostgreSQL materialized views and are reported separately below.
         */
        final String viewsSQLCommand =
                "SELECT s.name AS schemaname, v.name AS viewname, " +
                        "       COALESCE(USER_NAME(v.principal_id), SCHEMA_NAME(v.schema_id)) AS viewowner, " +
                        "       m.definition AS definition " +
                        "FROM sys.views v " +
                        "JOIN sys.schemas s ON v.schema_id = s.schema_id " +
                        "LEFT OUTER JOIN sys.sql_modules m ON m.object_id = v.object_id " +
                        "WHERE s.name NOT IN (N'sys', N'INFORMATION_SCHEMA') " +
                        "  AND NOT EXISTS (SELECT 1 FROM sys.indexes i WHERE i.object_id = v.object_id AND i.index_id = 1);";

        /*
         * Indexed views - always populated and indexed by definition.
         */
        final String indexedViewsSQLCommand =
                "SELECT s.name AS schemaname, v.name AS viewname, " +
                        "       COALESCE(USER_NAME(v.principal_id), SCHEMA_NAME(v.schema_id)) AS viewowner, " +
                        "       m.definition AS definition " +
                        "FROM sys.views v " +
                        "JOIN sys.schemas s ON v.schema_id = s.schema_id " +
                        "LEFT OUTER JOIN sys.sql_modules m ON m.object_id = v.object_id " +
                        "WHERE s.name NOT IN (N'sys', N'INFORMATION_SCHEMA') " +
                        "  AND EXISTS (SELECT 1 FROM sys.indexes i WHERE i.object_id = v.object_id AND i.index_id = 1);";

        try
        {
            DatabaseDetails databaseDetails = databaseResults.get(databaseName);

            if (databaseDetails != null)
            {
                /*
                 * Extract key stats about each column in each database.  This builds up the basic structure of
                 * schemas, tables and columns and enables the calculation of the total number of schemas, tables
                 * and columns to be determined.  The declared column width is also used directly as the column's
                 * size measurement - unlike PostgreSQL's pg_column_size(), Microsoft SQL Server has no way to
                 * estimate the actual stored size of a column without scanning data, so the declared max_length is
                 * used as a metadata-only approximation.
                 */
                PreparedStatement preparedStatement = databaseSQLConnection.prepareStatement(columnStatsSQLCommand);

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next())
                {
                    String schemaName = resultSet.getString("schemaname");

                    if ((schemaName != null) && (!schemaName.equals("sys")) && (!schemaName.equals("INFORMATION_SCHEMA")))
                    {
                        String  tableName              = resultSet.getString("tablename");
                        String  columnName             = resultSet.getString("columnname");
                        int     averageColumnWidth     = resultSet.getInt("avgwidth");
                        long    numberOfDistinctValues = resultSet.getLong("ndistinct");
                        String  mostCommonValue        = resultSet.getString("mostcommonvalue");
                        String  mostCommonFrequency     = resultSet.getString("mostcommonfreq");
                        String  columnTypeName         = resultSet.getString("typename");
                        boolean columnNullable         = resultSet.getBoolean("isnullable");

                        SchemaDetails schemaDetails = databaseDetails.getSchemaDetails(schemaName);
                        TableDetails  tableDetails  = schemaDetails.getTableDetails(tableName);
                        ColumnDetails columnDetails = tableDetails.getColumnDetails(columnName);

                        columnDetails.setColumnMeasurement(averageColumnWidth,
                                                           numberOfDistinctValues,
                                                           mostCommonValue,
                                                           mostCommonFrequency,
                                                           columnTypeName,
                                                           !columnNullable);
                    }
                }

                resultSet.close();
                preparedStatement.close();

                /*
                 * Extract core information about the tables
                 */
                preparedStatement = databaseSQLConnection.prepareStatement(tablesSQLCommand);
                resultSet         = preparedStatement.executeQuery();

                while (resultSet.next())
                {
                    String schemaName = resultSet.getString("schemaname");

                    if ((schemaName != null) && (!schemaName.equals("sys")) && (!schemaName.equals("INFORMATION_SCHEMA")))
                    {
                        String  tableName            = resultSet.getString("tablename");
                        String  tableOwner           = resultSet.getString("tableowner");
                        boolean hasIndexes           = resultSet.getBoolean("hasindexes");
                        boolean hasTriggers          = resultSet.getBoolean("hastriggers");
                        boolean hasRowSecurity       = resultSet.getBoolean("hasrowsecurity");
                        long    numberOfRowsInserted = resultSet.getLong("n_tup_ins");
                        long    numberOfRowsUpdated  = resultSet.getLong("n_tup_upd");
                        long    numberOfRowsDeleted  = resultSet.getLong("n_tup_del");

                        SchemaDetails schemaDetails = databaseDetails.getSchemaDetails(schemaName);
                        TableDetails  tableDetails  = schemaDetails.getTableDetails(tableName);

                        /*
                         * Microsoft SQL Server has no equivalent of PostgreSQL's CREATE RULE feature, so hasRules is
                         * always reported as false.
                         */
                        tableDetails.setTableMeasurements(tableOwner,
                                                          hasIndexes,
                                                          false,
                                                          hasTriggers,
                                                          hasRowSecurity,
                                                          numberOfRowsInserted,
                                                          numberOfRowsUpdated,
                                                          numberOfRowsDeleted);
                    }
                }

                resultSet.close();
                preparedStatement.close();


                /*
                 * Extract stats about the views
                 */
                preparedStatement = databaseSQLConnection.prepareStatement(viewsSQLCommand);
                resultSet         = preparedStatement.executeQuery();

                while (resultSet.next())
                {
                    String schemaName = resultSet.getString("schemaname");

                    if ((schemaName != null) && (!schemaName.equals("sys")) && (!schemaName.equals("INFORMATION_SCHEMA")))
                    {
                        String viewName   = resultSet.getString("viewname");
                        String viewOwner  = resultSet.getString("viewowner");
                        String definition = resultSet.getString("definition");

                        SchemaDetails schemaDetails = databaseDetails.getSchemaDetails(schemaName);
                        TableDetails  tableDetails  = schemaDetails.getTableDetails(viewName);

                        tableDetails.setViewDetails(viewOwner, definition);
                    }
                }

                resultSet.close();
                preparedStatement.close();

                /*
                 * Extract stats about the indexed views (Microsoft SQL Server's closest equivalent of a
                 * materialized view).
                 */
                preparedStatement = databaseSQLConnection.prepareStatement(indexedViewsSQLCommand);
                resultSet         = preparedStatement.executeQuery();

                while (resultSet.next())
                {
                    String schemaName = resultSet.getString("schemaname");

                    if ((schemaName != null) && (!schemaName.equals("sys")) && (!schemaName.equals("INFORMATION_SCHEMA")))
                    {
                        String viewName   = resultSet.getString("viewname");
                        String viewOwner  = resultSet.getString("viewowner");
                        String definition = resultSet.getString("definition");

                        SchemaDetails schemaDetails = databaseDetails.getSchemaDetails(schemaName);
                        TableDetails  tableDetails  = schemaDetails.getTableDetails(viewName);

                        tableDetails.setMaterializedViewDetails(viewOwner,
                                                                true,
                                                                true,
                                                                definition);
                    }
                }

                resultSet.close();
                preparedStatement.close();

                /*
                 * At this point we have details of each schema, table, view and column.
                 */
                List<String> schemaNames = databaseDetails.getSchemaNames();

                if (schemaNames != null)
                {
                    /*
                     * Extract the total size of each table and roll it up into the owning schema.
                     */
                    for (String schemaName : schemaNames)
                    {
                        SchemaDetails currentSchema = databaseDetails.getSchemaDetails(schemaName);

                        if (currentSchema != null)
                        {
                            List<String> tableNames = currentSchema.getTableNames();

                            if (tableNames != null)
                            {
                                long schemaSize = 0L;

                                for (String tableName : tableNames)
                                {
                                    TableDetails currentTable = currentSchema.getTableDetails(tableName);

                                    if (currentTable != null)
                                    {
                                        this.getTableSize(databaseSQLConnection, currentTable);
                                        schemaSize = schemaSize + currentTable.getTableMeasurements().getTableSize();
                                    }
                                }

                                currentSchema.schemaMeasurement.setTotalTableSize(schemaSize);
                            }
                        }
                    }

                    /*
                     * Count up instances of schema, tables and columns in database
                     */
                    databaseDetails.setUpCounts();
                }
            }
        }
        catch (SQLException sqlException)
        {
            /*
             * This rollback helps to clean up any issues in the connection.
             */
            try
            {
                databaseSQLConnection.rollback();
            }
            catch (Exception error)
            {
                // ignore
            }

            throw sqlException;
        }
    }


    /**
     * Convert the information retrieve from the database server into annotations recognized by the
     * survey action framework.
     *
     * @return list of annotations
     * @throws PropertyServerException problem formatting JSON string
     */
    List<AnnotationProperties> getAnnotations() throws PropertyServerException
    {
        /*
         * All information has been assembled so create the annotations.
         */
        List<AnnotationProperties> annotations = new ArrayList<>();

        for (String databaseName : databaseResults.keySet())
        {
            DatabaseDetails databaseDetails = databaseResults.get(databaseName);

            if (databaseDetails != null)
            {
                List<String> schemaNames = databaseDetails.getSchemaNames();

                ResourceMeasureAnnotationProperties databaseAnnotation = new ResourceMeasureAnnotationProperties();

                databaseAnnotation.setQualifiedName("Annotation::" + SurveyDatabaseAnnotationType.DATABASE_MEASUREMENTS.getName() + "::" + databaseName + "::" + UUID.randomUUID());
                databaseAnnotation.setAnnotationType(SurveyDatabaseAnnotationType.DATABASE_MEASUREMENTS.getName());
                databaseAnnotation.setSummary(SurveyDatabaseAnnotationType.DATABASE_MEASUREMENTS.getSummary());
                databaseAnnotation.setExplanation(SurveyDatabaseAnnotationType.DATABASE_MEASUREMENTS.getExplanation());
                databaseAnnotation.setAnalysisStep(SurveyDatabaseAnnotationType.DATABASE_MEASUREMENTS.getAnalysisStep());

                databaseAnnotation.setJsonProperties(surveyActionServiceConnector.getJSONProperties(databaseDetails.getDatabaseMeasurements()));

                databaseAnnotation.setResourceProperties(databaseDetails.getDatabaseResourceProperties());

                annotations.add(databaseAnnotation);

                for (String schemaName : schemaNames)
                {
                    SchemaDetails currentSchema = databaseDetails.getSchemaDetails(schemaName);

                    if (currentSchema != null)
                    {
                        ResourceMeasureAnnotationProperties schemaAnnotation = new ResourceMeasureAnnotationProperties();

                        schemaAnnotation.setQualifiedName("Annotation::" + SurveyDatabaseAnnotationType.SCHEMA_MEASUREMENTS.getName() + "::" + currentSchema.getQualifiedSchemaName() + "::" + UUID.randomUUID());
                        schemaAnnotation.setAnnotationType(SurveyDatabaseAnnotationType.SCHEMA_MEASUREMENTS.getName());
                        schemaAnnotation.setSummary(SurveyDatabaseAnnotationType.SCHEMA_MEASUREMENTS.getSummary());
                        schemaAnnotation.setExplanation(SurveyDatabaseAnnotationType.SCHEMA_MEASUREMENTS.getExplanation());
                        schemaAnnotation.setAnalysisStep(SurveyDatabaseAnnotationType.SCHEMA_MEASUREMENTS.getAnalysisStep());

                        schemaAnnotation.setJsonProperties(surveyActionServiceConnector.getJSONProperties(currentSchema.getSchemaMeasurement()));

                        schemaAnnotation.setResourceProperties(currentSchema.getSchemaResourceProperties());

                        annotations.add(schemaAnnotation);


                        List<String> tableNames = currentSchema.getTableNames();

                        if (tableNames != null)
                        {
                            for (String tableName : tableNames)
                            {
                                TableDetails currentTable = currentSchema.getTableDetails(tableName);

                                if (currentTable != null)
                                {
                                    ResourceMeasureAnnotationProperties tableAnnotation = new ResourceMeasureAnnotationProperties();

                                    tableAnnotation.setQualifiedName("Annotation::" + SurveyDatabaseAnnotationType.TABLE_MEASUREMENTS.getName() + "::" + currentTable.getQualifiedTableName() + "::" + UUID.randomUUID());
                                    tableAnnotation.setAnnotationType(SurveyDatabaseAnnotationType.TABLE_MEASUREMENTS.getName());
                                    tableAnnotation.setSummary(SurveyDatabaseAnnotationType.TABLE_MEASUREMENTS.getSummary());
                                    tableAnnotation.setExplanation(SurveyDatabaseAnnotationType.TABLE_MEASUREMENTS.getExplanation());
                                    tableAnnotation.setAnalysisStep(SurveyDatabaseAnnotationType.TABLE_MEASUREMENTS.getAnalysisStep());

                                    tableAnnotation.setJsonProperties(surveyActionServiceConnector.getJSONProperties(currentTable.getTableMeasurements()));

                                    tableAnnotation.setResourceProperties(currentTable.getTableResourceProperties());

                                    annotations.add(tableAnnotation);

                                    List<String> columnNames = currentTable.getColumnNames();

                                    if (columnNames != null)
                                    {
                                        for (String columnName : columnNames)
                                        {
                                            ColumnDetails currentColumn = currentTable.getColumnDetails(columnName);

                                            ResourceMeasureAnnotationProperties columnAnnotation = new ResourceMeasureAnnotationProperties();

                                            columnAnnotation.setQualifiedName("Annotation::" + SurveyDatabaseAnnotationType.COLUMN_MEASUREMENTS.getName() + "::" + currentColumn.getQualifiedColumnName() + "::" + UUID.randomUUID());
                                            columnAnnotation.setAnnotationType(SurveyDatabaseAnnotationType.COLUMN_MEASUREMENTS.getName());
                                            columnAnnotation.setSummary(SurveyDatabaseAnnotationType.COLUMN_MEASUREMENTS.getSummary());
                                            columnAnnotation.setExplanation(SurveyDatabaseAnnotationType.COLUMN_MEASUREMENTS.getExplanation());
                                            columnAnnotation.setAnalysisStep(SurveyDatabaseAnnotationType.COLUMN_MEASUREMENTS.getAnalysisStep());

                                            columnAnnotation.setJsonProperties(surveyActionServiceConnector.getJSONProperties(currentColumn.getColumnMeasurements()));

                                            columnAnnotation.setResourceProperties(currentColumn.getColumnResourceProperties());

                                            annotations.add(columnAnnotation);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (!annotations.isEmpty())
        {
            return annotations;
        }


        /*
         * Empty or uncontactable database
         */
        return null;
    }


    /**
     * Retrieve information about a table size using sys.dm_db_partition_stats - the Microsoft SQL Server
     * equivalent of PostgreSQL's pg_table_size() function.  index_id IN (0,1) covers heap tables (0) and tables
     * with a clustered index (1), which together represent the table's own data pages.
     *
     * @param databaseSQLConnection connection to the appropriate database
     * @param tableDetails collected information about a table
     * @throws SQLException errors accessing the database
     */
    void getTableSize(java.sql.Connection databaseSQLConnection,
                      TableDetails        tableDetails) throws SQLException
    {
        final String tableSizeSQLCommand =
                "SELECT SUM(ps.used_page_count) * 8 * 1024 AS tablesize " +
                        "FROM sys.dm_db_partition_stats ps " +
                        "WHERE ps.object_id = OBJECT_ID(N'" + tableDetails.getQualifiedTableName() + "') AND ps.index_id IN (0,1);";

        if (! tableDetails.columns.isEmpty())
        {
            try
            {
                PreparedStatement preparedStatement = databaseSQLConnection.prepareStatement(tableSizeSQLCommand);
                ResultSet         resultSet         = preparedStatement.executeQuery();

                if (resultSet.next())
                {
                    tableDetails.setTableSize(resultSet.getLong("tablesize"));
                }

                resultSet.close();
                preparedStatement.close();
            }
            catch (SQLException sqlException)
            {
                try
                {
                    databaseSQLConnection.rollback();
                }
                catch (Exception error)
                {
                    // ignore
                }

                throw sqlException;
            }
        }
    }


    /**
     * Manages consolidated information about a database
     */
    static class DatabaseDetails
    {
        private final RelationalDataManagerMeasurement databaseMeasurements;
        private final Map<String, SchemaDetails> schemas = new HashMap<>();


        public DatabaseDetails(RelationalDataManagerMeasurement databaseMeasurements)
        {
            this.databaseMeasurements = databaseMeasurements;
        }


        /**
         * Add the size of the database.
         *
         * @param size number of bytes
         */
        void setSize(long size)
        {
            databaseMeasurements.setSize(size);
        }


        /**
         * Add the read/write activity counters for the database.
         *
         * @param rowsFetched approximate number of rows read (seeks + scans + lookups)
         * @param rowsUpdated approximate number of rows changed (inserts + updates + deletes combined)
         */
        void setActivity(long rowsFetched, long rowsUpdated)
        {
            databaseMeasurements.setRowsFetched(rowsFetched);
            databaseMeasurements.setRowsUpdated(rowsUpdated);
        }


        /**
         * Return the counts for the database structure.
         */
        void setUpCounts()
        {
            if (! schemas.isEmpty())
            {
                databaseMeasurements.setSchemaCount(schemas.size());

                long tableCount = 0L;
                long viewCount = 0L;
                long materializedViewCount = 0L;
                long columnCount = 0L;

                for (String schemaName : schemas.keySet())
                {
                    SchemaDetails schemaDetails = schemas.get(schemaName);

                    if (schemaDetails != null)
                    {
                        tableCount = tableCount + schemaDetails.getTableCount();
                        viewCount = viewCount + schemaDetails.getViewCount();
                        materializedViewCount = materializedViewCount + schemaDetails.getMaterializedViewCount();
                        columnCount = columnCount + schemaDetails.getColumnCount();
                    }
                }

                databaseMeasurements.setTableCount(tableCount);
                databaseMeasurements.setViewCount(viewCount);
                databaseMeasurements.setMaterializedViewCount(materializedViewCount);
                databaseMeasurements.setColumnCount(columnCount);
            }
        }


        /**
         * Return a map of properties about the database.
         *
         * @return map
         */
        Map<String, String> getDatabaseResourceProperties()
        {
            Map<String, String> resourceProperties = new HashMap<>();

            resourceProperties.put(RelationalDatabaseMetric.DATABASE_NAME.getDisplayName(), databaseMeasurements.getResourceName());
            resourceProperties.put(RelationalDatabaseMetric.DATA_SIZE.getDisplayName(), Long.toString(databaseMeasurements.getSize()));
            resourceProperties.put(RelationalDatabaseMetric.SCHEMA_COUNT.getDisplayName(), Long.toString(databaseMeasurements.getSchemaCount()));
            resourceProperties.put(RelationalDatabaseMetric.TABLE_COUNT.getDisplayName(), Long.toString(databaseMeasurements.getTableCount()));
            resourceProperties.put(RelationalDatabaseMetric.COLUMN_COUNT.getDisplayName(), Long.toString(databaseMeasurements.getColumnCount()));
            resourceProperties.put(RelationalDatabaseMetric.ROWS_FETCHED.getDisplayName(), Long.toString(databaseMeasurements.getRowsFetched()));
            resourceProperties.put(RelationalDatabaseMetric.ROWS_INSERTED.getDisplayName(), Long.toString(databaseMeasurements.getRowsInserted()));
            resourceProperties.put(RelationalDatabaseMetric.ROWS_UPDATED.getDisplayName(), Long.toString(databaseMeasurements.getRowsUpdated()));
            resourceProperties.put(RelationalDatabaseMetric.ROWS_DELETED.getDisplayName(), Long.toString(databaseMeasurements.getRowsDeleted()));

            return resourceProperties;
        }


        /**
         * Return the set of database measurements collected.
         *
         * @return measurements object
         */
        public RelationalDataManagerMeasurement getDatabaseMeasurements()
        {
            return databaseMeasurements;
        }


        /**
         * Retrieve the schema details for the named schema.
         *
         * @param schemaName name of schema
         * @return corresponding schema details object
         */
        SchemaDetails getSchemaDetails(String schemaName)
        {
            SchemaDetails schemaDetails = schemas.get(schemaName);

            if (schemaDetails == null)
            {
                schemaDetails = new SchemaDetails(databaseMeasurements.getResourceName(), schemaName);

                schemas.put(schemaName, schemaDetails);
            }

            return schemaDetails;
        }


        /**
         * Return the list of schema names.
         *
         * @return list
         */
        List<String> getSchemaNames()
        {
            return new ArrayList<>(schemas.keySet());
        }
    }


    /**
     * Manages consolidated information about a schema
     */
    static class SchemaDetails
    {
        private final RelationalSchemaMeasurement schemaMeasurement = new RelationalSchemaMeasurement();
        private final Map<String, TableDetails>   tables            = new HashMap<>();


        /**
         * Create a schema details object
         *
         * @param databaseName name of database
         * @param schemaName   name of schema
         */
        SchemaDetails(String databaseName,
                      String schemaName)
        {
            this.schemaMeasurement.setQualifiedSchemaName(databaseName + "." + schemaName);
            this.schemaMeasurement.setSchemaName(schemaName);
        }


        /**
         * Return the number of tables.
         *
         * @return table count
         */
        long getTableCount()
        {
            long tableCount = 0L;

            if (! tables.isEmpty())
            {
                for (TableDetails tableDetails : tables.values())
                {
                    if ((tableDetails != null) && ("Table".equals(tableDetails.getTableMeasurements().getTableType())))
                    {
                        tableCount ++;
                    }
                }
            }

            schemaMeasurement.setTableCount(tableCount);
            return schemaMeasurement.getTableCount();
        }


        /**
         * Return the number of views.
         *
         * @return view count
         */
        long getViewCount()
        {
            long viewCount = 0L;

            if (! tables.isEmpty())
            {
                for (TableDetails tableDetails : tables.values())
                {
                    if ((tableDetails != null) && ("View".equals(tableDetails.getTableMeasurements().getTableType())))
                    {
                        viewCount ++;
                    }
                }
            }

            schemaMeasurement.setViewCount(viewCount);

            return schemaMeasurement.getViewCount();
        }


        /**
         * Return the number of indexed views (this schema's materialized-view analog).
         *
         * @return view count
         */
        long getMaterializedViewCount()
        {
            long viewCount = 0L;

            if (! tables.isEmpty())
            {
                for (TableDetails tableDetails : tables.values())
                {
                    if ((tableDetails != null) && ("MaterializedView".equals(tableDetails.getTableMeasurements().getTableType())))
                    {
                        viewCount ++;
                    }
                }
            }

            schemaMeasurement.setMaterializedViewCount(viewCount);

            return schemaMeasurement.getMaterializedViewCount();
        }


        /**
         * Return the number of columns summed from all of the tables.
         *
         * @return column count (from all tables)
         */
        long getColumnCount()
        {
            long columnCount = 0L;

            if (! tables.isEmpty())
            {
                for (String tableName : tables.keySet())
                {
                    TableDetails tableDetails = tables.get(tableName);
                    if (tableDetails != null)
                    {
                        columnCount = columnCount + tableDetails.getColumnCount();
                    }
                }
            }

            schemaMeasurement.setColumnCount(columnCount);
            return schemaMeasurement.getColumnCount();
        }


        /**
         * Retrieve the qualified schema name
         *
         * @return string
         */
        public String getQualifiedSchemaName()
        {
            return schemaMeasurement.getQualifiedSchemaName();
        }


        /**
         * Retrieve the table details for the named table.
         *
         * @param tableName name of table
         * @return corresponding table details object
         */
        TableDetails getTableDetails(String tableName)
        {
            TableDetails tableDetails = tables.get(tableName);

            if (tableDetails == null)
            {
                tableDetails = new TableDetails(this.getQualifiedSchemaName(), tableName);

                tables.put(tableName, tableDetails);
            }

            return tableDetails;
        }


        /**
         * Return the list of table names for this schema.
         *
         * @return list
         */
        List<String> getTableNames()
        {
            return new ArrayList<>(tables.keySet());
        }


        /**
         * Return a map of properties about the database.
         *
         * @return map
         */
        Map<String, String> getSchemaResourceProperties()
        {
            Map<String, String> resourceProperties = new HashMap<>();

            resourceProperties.put(RelationalSchemaMetric.QUALIFIED_SCHEMA_NAME.getDisplayName(), schemaMeasurement.getQualifiedSchemaName());
            resourceProperties.put(RelationalSchemaMetric.SCHEMA_NAME.getDisplayName(), schemaMeasurement.getSchemaName());
            resourceProperties.put(RelationalSchemaMetric.TOTAL_TABLE_SIZE.getDisplayName(), Long.toString(schemaMeasurement.getTotalTableSize()));
            resourceProperties.put(RelationalSchemaMetric.TABLE_COUNT.getDisplayName(), Long.toString(schemaMeasurement.getTableCount()));
            resourceProperties.put(RelationalSchemaMetric.VIEW_COUNT.getDisplayName(), Long.toString(schemaMeasurement.getViewCount()));
            resourceProperties.put(RelationalSchemaMetric.MAT_VIEW_COUNT.getDisplayName(), Long.toString(schemaMeasurement.getMaterializedViewCount()));
            resourceProperties.put(RelationalSchemaMetric.COLUMN_COUNT.getDisplayName(), Long.toString(schemaMeasurement.getColumnCount()));

            return resourceProperties;
        }


        /**
         * Return the set of schema measurements collected.
         *
         * @return measurements object
         */
        public RelationalSchemaMeasurement getSchemaMeasurement()
        {
            return schemaMeasurement;
        }
    }


    /**
     * Manages consolidated information about a table.
     */
    static class TableDetails
    {
        private final RelationalTableMeasurement tableMeasurement = new RelationalTableMeasurement();
        private final Map<String, ColumnDetails> columns = new HashMap<>();

        TableDetails(String qualifiedSchemaName,
                     String tableName)
        {
            this.tableMeasurement.setQualifiedTableName(qualifiedSchemaName + "." + tableName);
            this.tableMeasurement.setTableName(tableName);
        }


        /**
         * Set up key values describing a table.
         *
         * @param tableOwner user that owns the table
         * @param hasIndexes does the table have indexes
         * @param hasRules does the table have rules attached (always false - Microsoft SQL Server has no rules)
         * @param hasTriggers does the table have triggers attached
         * @param hasRowSecurity is row-level security set on
         * @param numberOfRowsInserted how many rows have been inserted
         * @param numberOfRowsUpdated how many rows have been updated
         * @param numberOfRowsDeleted how many rows have been deleted
         */
        void setTableMeasurements(String  tableOwner,
                                  boolean hasIndexes,
                                  boolean hasRules,
                                  boolean hasTriggers,
                                  boolean hasRowSecurity,
                                  long    numberOfRowsInserted,
                                  long    numberOfRowsUpdated,
                                  long    numberOfRowsDeleted)
        {
            this.tableMeasurement.setTableOwner(tableOwner);
            this.tableMeasurement.setHasIndexes(hasIndexes);
            this.tableMeasurement.setHasRules(hasRules);
            this.tableMeasurement.setHasTriggers(hasTriggers);
            this.tableMeasurement.setHasRowSecurity(hasRowSecurity);
            this.tableMeasurement.setNumberOfRowsInserted(numberOfRowsInserted);
            this.tableMeasurement.setNumberOfRowsUpdated(numberOfRowsUpdated);
            this.tableMeasurement.setNumberOfRowsDeleted(numberOfRowsDeleted);
        }


        /**
         * Set up the number of bytes in the table.
         *
         * @param tableSize long
         */
        void setTableSize(long tableSize)
        {
            this.tableMeasurement.setTableSize(tableSize);
        }

        /**
         * Return the number of columns in this table.
         *
         * @return long
         */
        long getColumnCount()
        {
            tableMeasurement.setColumnCount(columns.size());

            return tableMeasurement.getColumnCount();
        }


        /**
         * Set up information about a view.
         *
         * @param viewOwner owner
         * @param definition query that retrieves the data for this view
         */
        void setViewDetails(String viewOwner,
                            String definition)
        {
            tableMeasurement.setTableOwner(viewOwner);
            tableMeasurement.setQueryDefinition(definition);
            tableMeasurement.setTableType("View");
        }


        void setMaterializedViewDetails(String  viewOwner,
                                        boolean hasIndexes,
                                        boolean isPopulated,
                                        String  definition)
        {
            tableMeasurement.setTableOwner(viewOwner);
            tableMeasurement.setQueryDefinition(definition);
            tableMeasurement.setHasIndexes(hasIndexes);
            tableMeasurement.setIsPopulated(isPopulated);
            tableMeasurement.setTableType("MaterializedView");
        }

        /**
         * Retrieve the qualified table name
         *
         * @return string
         */
        public String getQualifiedTableName()
        {
            return tableMeasurement.getQualifiedTableName();
        }


        /**
         * Return the list of column names for this table.
         *
         * @return list
         */
        List<String> getColumnNames()
        {
            return new ArrayList<>(columns.keySet());
        }


        /**
         * Retrieve the column details for the named column.
         *
         * @param columnName name of column
         * @return corresponding column details object
         */
        ColumnDetails getColumnDetails(String columnName)
        {
            ColumnDetails columnDetails = columns.get(columnName);

            if (columnDetails == null)
            {
                columnDetails = new ColumnDetails(this.getQualifiedTableName(), columnName);

                columns.put(columnName, columnDetails);
            }

            return columnDetails;
        }


        /**
         * Return a map of properties about the database table.
         *
         * @return map
         */
        Map<String, String> getTableResourceProperties()
        {
            Map<String, String> resourceProperties = new HashMap<>();

            resourceProperties.put(RelationalTableMetric.TABLE_QNAME.getDisplayName(), tableMeasurement.getQualifiedTableName());
            resourceProperties.put(RelationalTableMetric.TABLE_NAME.getDisplayName(), tableMeasurement.getTableName());
            resourceProperties.put(RelationalTableMetric.TABLE_OWNER.getDisplayName(), tableMeasurement.getTableOwner());
            resourceProperties.put(RelationalTableMetric.TABLE_TYPE.getDisplayName(), tableMeasurement.getTableType());
            resourceProperties.put(RelationalTableMetric.TABLE_SIZE.getDisplayName(), Long.toString(tableMeasurement.getTableSize()));
            resourceProperties.put(RelationalTableMetric.COLUMN_COUNT.getDisplayName(), Long.toString(tableMeasurement.getColumnCount()));
            resourceProperties.put(RelationalTableMetric.NUMBER_OF_ROWS_INSERTED.getDisplayName(), Long.toString(tableMeasurement.getNumberOfRowsInserted()));
            resourceProperties.put(RelationalTableMetric.NUMBER_OF_ROWS_UPDATED.getDisplayName(), Long.toString(tableMeasurement.getNumberOfRowsUpdated()));
            resourceProperties.put(RelationalTableMetric.NUMBER_OF_ROWS_DELETED.getDisplayName(), Long.toString(tableMeasurement.getNumberOfRowsDeleted()));
            resourceProperties.put(RelationalTableMetric.IS_POPULATED.getDisplayName(), Boolean.toString(tableMeasurement.getIsPopulated()));
            resourceProperties.put(RelationalTableMetric.HAS_INDEXES.getDisplayName(), Boolean.toString(tableMeasurement.getHasIndexes()));
            resourceProperties.put(RelationalTableMetric.HAS_RULES.getDisplayName(), Boolean.toString(tableMeasurement.getHasRules()));
            resourceProperties.put(RelationalTableMetric.HAS_TRIGGERS.getDisplayName(), Boolean.toString(tableMeasurement.getHasTriggers()));
            resourceProperties.put(RelationalTableMetric.HAS_ROW_SECURITY.getDisplayName(), Boolean.toString(tableMeasurement.getHasRowSecurity()));
            resourceProperties.put(RelationalTableMetric.QUERY_DEFINITION.getDisplayName(), tableMeasurement.getQueryDefinition());

            return resourceProperties;
        }


        /**
         * Return the set of database table measurements collected.
         *
         * @return measurements object
         */
        public RelationalTableMeasurement getTableMeasurements()
        {
            return tableMeasurement;
        }
    }


    /**
     * Manages consolidated information about a column
     */
    static class ColumnDetails
    {
        private final RelationalColumnMeasurement columnMeasurement = new RelationalColumnMeasurement();

        /**
         * Construct the column details.
         *
         * @param qualifiedTableName name of the owning table
         * @param columnName column name
         */
        ColumnDetails(String qualifiedTableName,
                      String columnName)
        {
            this.columnMeasurement.setQualifiedColumnName(qualifiedTableName + "." + columnName);
            this.columnMeasurement.setColumnName(columnName);
        }

        /**
         * Retrieve the qualified table name
         *
         * @return string
         */
        public String getQualifiedColumnName()
        {
            return columnMeasurement.getQualifiedColumnName();
        }


        /**
         * Set the measurements gathered for this column.  The declared column width (max_length) is used directly
         * as the column size, since Microsoft SQL Server has no metadata-only way to estimate the actual stored
         * size of a column's values without scanning data.
         *
         * @param averageColumnWidth declared width in bytes
         * @param numberOfDistinctValues estimate from the column's auto-collected statistics
         * @param mostCommonValue single most common value found in the column's histogram, if any
         * @param mostCommonFrequency estimated number of rows matching the most common value
         * @param columnTypeName declared SQL type name
         * @param columnNotNull true if the column does not allow nulls
         */
        void setColumnMeasurement(int     averageColumnWidth,
                                  long    numberOfDistinctValues,
                                  String  mostCommonValue,
                                  String  mostCommonFrequency,
                                  String  columnTypeName,
                                  boolean columnNotNull)
        {
            this.columnMeasurement.setAverageColumnWidth(averageColumnWidth);
            this.columnMeasurement.setColumnSize(averageColumnWidth);
            this.columnMeasurement.setNumberOfDistinctValues(numberOfDistinctValues);

            if (mostCommonValue != null)
            {
                this.columnMeasurement.setMostCommonValues(mostCommonValue);
            }

            if (mostCommonFrequency != null)
            {
                this.columnMeasurement.setMostCommonValuesFrequency(mostCommonFrequency);
            }

            this.columnMeasurement.setColumnDataType(columnTypeName);
            this.columnMeasurement.setColumnNotNull(columnNotNull);
        }


        /**
         * Return a map of properties about the database table.
         *
         * @return map
         */
        Map<String, String> getColumnResourceProperties()
        {
            Map<String, String> resourceProperties = new HashMap<>();

            resourceProperties.put(RelationalColumnMetric.COLUMN_QNAME.getDisplayName(), columnMeasurement.getQualifiedColumnName());
            resourceProperties.put(RelationalColumnMetric.COLUMN_NAME.getDisplayName(), columnMeasurement.getColumnName());
            resourceProperties.put(RelationalColumnMetric.COLUMN_SIZE.getDisplayName(), Long.toString(columnMeasurement.getColumnSize()));
            resourceProperties.put(RelationalColumnMetric.COLUMN_TYPE.getDisplayName(), columnMeasurement.getColumnDataType());
            resourceProperties.put(RelationalColumnMetric.COLUMN_NOT_NULL.getDisplayName(), Boolean.toString(columnMeasurement.getColumnNotNull()));
            resourceProperties.put(RelationalColumnMetric.AVERAGE_WIDTH.getDisplayName(), Integer.toString(columnMeasurement.getAverageColumnWidth()));
            resourceProperties.put(RelationalColumnMetric.NUMBER_OF_DISTINCT_VALUES.getDisplayName(), Long.toString(columnMeasurement.getNumberOfDistinctValues()));
            resourceProperties.put(RelationalColumnMetric.MOST_COMMON_VALUES.getDisplayName(), columnMeasurement.getMostCommonValues());
            resourceProperties.put(RelationalColumnMetric.MOST_COMMON_VALUES_FREQUENCY.getDisplayName(), columnMeasurement.getMostCommonValuesFrequency());

            return resourceProperties;
        }


        /**
         * Return the set of database column measurements collected.
         *
         * @return measurements object
         */
        public RelationalColumnMeasurement getColumnMeasurements()
        {
            return columnMeasurement;
        }
    }
}
