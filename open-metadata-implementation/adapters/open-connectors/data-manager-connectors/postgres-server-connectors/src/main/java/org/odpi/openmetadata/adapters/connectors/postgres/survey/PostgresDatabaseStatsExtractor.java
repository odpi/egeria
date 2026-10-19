/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.survey;

import org.odpi.openmetadata.adapters.connectors.postgres.ffdc.PostgresAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.ResourceMeasureAnnotationProperties;
import org.odpi.openmetadata.frameworks.opensurvey.DatabaseElementAnnotation;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceConnector;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyDatabaseAnnotationType;
import org.odpi.openmetadata.frameworks.opensurvey.measurements.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * PostgresDatabaseStatsExtractor gathers the statistics that PostgreSQL keeps about its databases, schemas,
 * tables and columns, and turns them into survey annotations.
 * <br><br>
 * The connections passed to it are expected to be in auto-commit mode, so that every query is its own short
 * transaction.  Each schema is surveyed with its own set of queries, and a query that fails is logged and
 * skipped, so one inaccessible schema or dropped table leaves a gap in the results rather than ending the
 * survey.
 */
public class PostgresDatabaseStatsExtractor
{
    private final List<String>                 validDatabases;
    private final List<String>                 excludedSchemas;
    private final List<String>                 includedSchemas;
    private final SurveyActionServiceConnector surveyActionServiceConnector;
    private final AuditLog                     auditLog;
    private final String                       surveyActionServiceName;
    private final Map<String, DatabaseDetails> databaseResults = new HashMap<>();


    /**
     * Constructor sets up the list of databases to process and the connection to the database.  Every schema
     * in those databases is surveyed.
     *
     * @param validDatabases               list of database names
     * @param surveyActionServiceConnector calling connector
     */
    public PostgresDatabaseStatsExtractor(List<String>                validDatabases,
                                          SurveyActionServiceConnector surveyActionServiceConnector)
    {
        this(validDatabases, null, null, surveyActionServiceConnector, null, null);
    }


    /**
     * Constructor sets up the list of databases to process, the schemas within them to survey, and the
     * connection to the database.
     *
     * @param validDatabases               list of database names
     * @param excludedSchemas              schemas to leave out (null means none are excluded)
     * @param includedSchemas              the only schemas to survey (null means all that are not excluded);
     *                                     takes precedence over excludedSchemas
     * @param surveyActionServiceConnector calling connector
     * @param auditLog                     logging destination for queries that fail (may be null)
     * @param surveyActionServiceName      name of the calling survey action service, for the audit log
     */
    public PostgresDatabaseStatsExtractor(List<String>                validDatabases,
                                          List<String>                excludedSchemas,
                                          List<String>                includedSchemas,
                                          SurveyActionServiceConnector surveyActionServiceConnector,
                                          AuditLog                    auditLog,
                                          String                      surveyActionServiceName)
    {
        this.validDatabases               = validDatabases;
        this.excludedSchemas              = excludedSchemas;
        this.includedSchemas              = includedSchemas;
        this.surveyActionServiceConnector = surveyActionServiceConnector;
        this.auditLog                     = auditLog;
        this.surveyActionServiceName      = surveyActionServiceName;
    }


    /**
     * Determine whether a schema should be surveyed.  PostgreSQL's own catalog schemas never are.  Otherwise
     * the include list, if there is one, decides; failing that, the exclude list does.  This follows
     * SurveyContext.elementShouldBeSurveyed, which applies the same rule to database names.
     *
     * @param schemaName name of the schema
     * @return flag indicating whether to gather statistics for the schema
     */
    boolean schemaShouldBeSurveyed(String schemaName)
    {
        if ((schemaName == null) || (schemaName.equals("pg_catalog")) || (schemaName.equals("information_schema")))
        {
            return false;
        }

        if (includedSchemas != null)
        {
            return includedSchemas.contains(schemaName);
        }
        else if (excludedSchemas != null)
        {
            return ! excludedSchemas.contains(schemaName);
        }

        return true;
    }


    /**
     * Convert PostgreSQL's raw pg_stats.n_distinct catalog value into an actual number-of-distinct-values
     * estimate.  Per PostgreSQL's own documentation, a non-negative n_distinct is the estimated number of
     * distinct values directly, but a NEGATIVE n_distinct is the negative of the ratio of distinct values
     * to rows in the table - used for columns (such as primary keys, UUIDs and timestamps) where the number
     * of distinct values scales with the size of the table.  Reading the raw catalog value directly as a
     * count produces a nonsensical negative (or, once truncated to a long, zero) result for exactly these
     * common, typically near-unique columns.
     *
     * @param rawDistinctStat  the raw value of pg_stats.n_distinct for the column
     * @param tableRowCount    the table's live row-count estimate (pg_stat_user_tables.n_live_tup)
     * @return estimated number of distinct values, never negative
     */
    static long calculateNumberOfDistinctValues(double rawDistinctStat,
                                                 long   tableRowCount)
    {
        if (rawDistinctStat < 0)
        {
            return Math.round(-rawDistinctStat * tableRowCount);
        }

        return Math.round(rawDistinctStat);
    }


    /**
     * Estimate the number of bytes stored in a column from its statistics.  PostgreSQL has no catalog value
     * for this, and measuring it exactly means reading every row of the table, so the average width that
     * ANALYZE recorded is multiplied by the table's live row count.
     *
     * @param averageColumnWidth pg_stats.avg_width for the column
     * @param tableRowCount      the table's live row-count estimate (pg_stat_user_tables.n_live_tup)
     * @return estimated column size in bytes, never negative
     */
    static long estimateColumnSize(int  averageColumnWidth,
                                   long tableRowCount)
    {
        if ((averageColumnWidth <= 0) || (tableRowCount <= 0))
        {
            return 0L;
        }

        return (long)averageColumnWidth * tableRowCount;
    }


    /**
     * Retrieve statistics about each requested database.
     * This works if connected to either the postgres database or a user database.  Every requested database
     * gets an entry in the results, even if its statistics cannot be read, so that its schemas can still be
     * surveyed.
     *
     * @param serverJDBCConnection connection to the postgres database
     */
    void getDatabaseStatistics(java.sql.Connection serverJDBCConnection)
    {
        final String pg_stat_databaseSQLCommand = "SELECT datname, tup_fetched, tup_inserted, tup_updated, tup_deleted, session_time, active_time, stats_reset FROM pg_catalog.pg_stat_database;";
        final String databaseSizeSQLCommand     = "SELECT pg_database_size(?::name) AS database_size;";

        for (String databaseName : validDatabases)
        {
            RelationalDataManagerMeasurement databaseMeasurement = new RelationalDataManagerMeasurement();

            databaseMeasurement.setResourceName(databaseName);
            databaseResults.put(databaseName, new DatabaseDetails(databaseMeasurement));
        }

        /*
         * Extract key stats about each database
         */
        try (PreparedStatement preparedStatement = serverJDBCConnection.prepareStatement(pg_stat_databaseSQLCommand);
             ResultSet         resultSet         = preparedStatement.executeQuery())
        {
            while (resultSet.next())
            {
                DatabaseDetails databaseDetails = databaseResults.get(resultSet.getString("datname"));

                if (databaseDetails != null)
                {
                    RelationalDataManagerMeasurement databaseMeasurement = databaseDetails.getDatabaseMeasurements();

                    databaseMeasurement.setRowsFetched(resultSet.getLong("tup_fetched"));
                    databaseMeasurement.setRowsInserted(resultSet.getLong("tup_inserted"));
                    databaseMeasurement.setRowsUpdated(resultSet.getLong("tup_updated"));
                    databaseMeasurement.setRowsDeleted(resultSet.getLong("tup_deleted"));
                    databaseMeasurement.setSessionTime(resultSet.getDouble("session_time"));
                    databaseMeasurement.setActiveTime(resultSet.getDouble("active_time"));
                    databaseMeasurement.setStatsReset(resultSet.getDate("stats_reset"));
                }
            }
        }
        catch (SQLException sqlException)
        {
            logQueryFailure("database activity statistics", "the database server", sqlException);
        }

        /*
         * Extract the total size of each database.  This needs the CONNECT privilege on the database, so
         * each one is queried separately.
         */
        for (String databaseName : validDatabases)
        {
            try (PreparedStatement preparedStatement = serverJDBCConnection.prepareStatement(databaseSizeSQLCommand))
            {
                preparedStatement.setString(1, databaseName);

                try (ResultSet resultSet = preparedStatement.executeQuery())
                {
                    if (resultSet.next())
                    {
                        databaseResults.get(databaseName).setSize(resultSet.getLong("database_size"));
                    }
                }
            }
            catch (SQLException sqlException)
            {
                logQueryFailure("database size", "database " + databaseName, sqlException);
            }
        }
    }


    /**
     * Extract detailed information about the schemas, tables and columns in a database.
     * This only returns information from the connected database - so it needs to be connected
     * from a user database - rather than the postgres database.
     * <br><br>
     * Each schema is surveyed with its own queries, so a failure is confined to the schema (or the
     * part of the schema) where it occurred.
     *
     * @param databaseName          name of database that is connected to
     * @param databaseSQLConnection connection to the named database
     * @throws SQLException the schemas in the database could not be listed
     */
    void getSchemaStatistics(String              databaseName,
                             java.sql.Connection databaseSQLConnection) throws SQLException
    {
        final String pg_namespaceSQLCommand = "SELECT nspname FROM pg_catalog.pg_namespace WHERE nspname NOT LIKE 'pg\\_%' AND nspname <> 'information_schema' ORDER BY nspname;";

        DatabaseDetails databaseDetails = databaseResults.get(databaseName);

        if (databaseDetails != null)
        {
            List<String> schemaNames = new ArrayList<>();

            try (PreparedStatement preparedStatement = databaseSQLConnection.prepareStatement(pg_namespaceSQLCommand);
                 ResultSet         resultSet         = preparedStatement.executeQuery())
            {
                while (resultSet.next())
                {
                    String schemaName = resultSet.getString("nspname");

                    if (schemaShouldBeSurveyed(schemaName))
                    {
                        schemaNames.add(schemaName);
                    }
                }
            }

            for (String schemaName : schemaNames)
            {
                if ((surveyActionServiceConnector != null) && (! surveyActionServiceConnector.isActive()))
                {
                    return;
                }

                this.getSchemaStatistics(databaseDetails, schemaName, databaseSQLConnection);
            }

            /*
             * Count up instances of schema, tables and columns in database
             */
            databaseDetails.setUpCounts();
        }
    }


    /**
     * Extract detailed information about the tables and columns in a single schema.  Each query is
     * independent: if one fails, it is logged and the others still contribute their results.
     *
     * @param databaseDetails       results for the database that is connected to
     * @param schemaName            name of the schema to survey
     * @param databaseSQLConnection connection to the database
     */
    private void getSchemaStatistics(DatabaseDetails     databaseDetails,
                                     String              schemaName,
                                     java.sql.Connection databaseSQLConnection)
    {
        final String pg_columnStatsSQLCommand = "select pg_stats.tablename, pg_stats.attname, pg_stats.avg_width, pg_stats.n_distinct, pg_stat_user_tables.n_live_tup, pg_stats.most_common_vals, pg_stats.most_common_freqs, pg_type.typname, pg_attribute.attnotnull from pg_catalog.pg_stats left outer join pg_catalog.pg_stat_user_tables on pg_stats.schemaname = pg_stat_user_tables.schemaname and pg_stats.tablename = pg_stat_user_tables.relname left outer join pg_catalog.pg_attribute on pg_stat_user_tables.relid = pg_attribute.attrelid and pg_stats.attname = pg_attribute.attname left outer join pg_catalog.pg_type on pg_attribute.atttypid = pg_type.oid where pg_stats.schemaname = ?;";
        final String pg_tablesSQLCommand      = "select pg_statio_user_tables.relname, pg_tables.tableowner, pg_tables.hasindexes, pg_tables.hasrules, pg_tables.hastriggers, pg_tables.rowsecurity, pg_stat_user_tables.n_tup_ins, pg_stat_user_tables.n_tup_upd, pg_stat_user_tables.n_tup_del, pg_catalog.pg_table_size(pg_statio_user_tables.relid) as table_size from pg_catalog.pg_statio_user_tables left outer join pg_catalog.pg_stat_user_tables on pg_stat_user_tables.relid = pg_statio_user_tables.relid left outer join pg_catalog.pg_tables on pg_statio_user_tables.schemaname = pg_tables.schemaname and pg_statio_user_tables.relname = pg_tables.tablename where pg_statio_user_tables.schemaname = ?;";
        final String pg_viewsSQLCommand       = "SELECT viewname, viewowner, definition FROM pg_catalog.pg_views WHERE schemaname = ?;";
        final String pg_matViewsSQLCommand    = "SELECT matviewname, matviewowner, hasindexes, ispopulated, definition FROM pg_catalog.pg_matviews WHERE schemaname = ?;";

        final String qualifiedSchemaName = databaseDetails.getDatabaseMeasurements().getResourceName() + "." + schemaName;

        /*
         * Extract key stats about each column.  This builds up the basic structure of tables and columns and
         * enables the calculation of the total number of tables and columns to be determined.
         */
        try (PreparedStatement preparedStatement = databaseSQLConnection.prepareStatement(pg_columnStatsSQLCommand))
        {
            preparedStatement.setString(1, schemaName);

            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                while (resultSet.next())
                {
                    String  tableName              = resultSet.getString("tablename");
                    String  columnName             = resultSet.getString("attname");
                    int     averageColumnWidth     = resultSet.getInt("avg_width");
                    long    tableRowCount          = resultSet.getLong("n_live_tup");
                    long    numberOfDistinctValues = calculateNumberOfDistinctValues(resultSet.getDouble("n_distinct"),
                                                                                      tableRowCount);
                    Array   mostCommonValues       = resultSet.getArray("most_common_vals");
                    Array   mostCommonFrequencies  = resultSet.getArray("most_common_freqs");
                    String  columnTypeName         = resultSet.getString("typname");
                    boolean columnNotNull          = resultSet.getBoolean("attnotnull");

                    SchemaDetails schemaDetails = databaseDetails.getSchemaDetails(schemaName);
                    TableDetails  tableDetails  = schemaDetails.getTableDetails(tableName);
                    ColumnDetails columnDetails = tableDetails.getColumnDetails(columnName);

                    columnDetails.setColumnMeasurement(averageColumnWidth,
                                                       numberOfDistinctValues,
                                                       mostCommonValues,
                                                       mostCommonFrequencies,
                                                       columnTypeName,
                                                       columnNotNull);
                    columnDetails.setColumnSize(estimateColumnSize(averageColumnWidth, tableRowCount));
                }
            }
        }
        catch (SQLException sqlException)
        {
            logQueryFailure("column statistics", "schema " + qualifiedSchemaName, sqlException);
        }

        /*
         * Extract core information about the tables, including their size.  The size is located from the
         * table's OID rather than its name, so it does not depend on how the name is quoted or on the
         * schema's search privileges.
         */
        try (PreparedStatement preparedStatement = databaseSQLConnection.prepareStatement(pg_tablesSQLCommand))
        {
            preparedStatement.setString(1, schemaName);

            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                while (resultSet.next())
                {
                    String  tableName            = resultSet.getString("relname");
                    String  tableOwner           = resultSet.getString("tableowner");
                    boolean hasIndexes           = resultSet.getBoolean("hasindexes");
                    boolean hasRules             = resultSet.getBoolean("hasrules");
                    boolean hasTriggers          = resultSet.getBoolean("hastriggers");
                    boolean hasRowSecurity       = resultSet.getBoolean("rowsecurity");
                    long    numberOfRowsInserted = resultSet.getLong("n_tup_ins");
                    long    numberOfRowsUpdated  = resultSet.getLong("n_tup_upd");
                    long    numberOfRowsDeleted  = resultSet.getLong("n_tup_del");
                    long    tableSize            = resultSet.getLong("table_size");

                    SchemaDetails schemaDetails = databaseDetails.getSchemaDetails(schemaName);
                    TableDetails  tableDetails  = schemaDetails.getTableDetails(tableName);

                    tableDetails.setTableMeasurements(tableOwner,
                                                      hasIndexes,
                                                      hasRules,
                                                      hasTriggers,
                                                      hasRowSecurity,
                                                      numberOfRowsInserted,
                                                      numberOfRowsUpdated,
                                                      numberOfRowsDeleted);
                    tableDetails.setTableSize(tableSize);
                }
            }
        }
        catch (SQLException sqlException)
        {
            logQueryFailure("table details", "schema " + qualifiedSchemaName, sqlException);
        }

        /*
         * Extract details of the views
         */
        try (PreparedStatement preparedStatement = databaseSQLConnection.prepareStatement(pg_viewsSQLCommand))
        {
            preparedStatement.setString(1, schemaName);

            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                while (resultSet.next())
                {
                    String viewName   = resultSet.getString("viewname");
                    String viewOwner  = resultSet.getString("viewowner");
                    String definition = resultSet.getString("definition");

                    SchemaDetails schemaDetails = databaseDetails.getSchemaDetails(schemaName);
                    TableDetails  tableDetails  = schemaDetails.getTableDetails(viewName);

                    tableDetails.setViewDetails(viewOwner, definition);
                }
            }
        }
        catch (SQLException sqlException)
        {
            logQueryFailure("view definitions", "schema " + qualifiedSchemaName, sqlException);
        }

        /*
         * Extract details of the materialized views
         */
        try (PreparedStatement preparedStatement = databaseSQLConnection.prepareStatement(pg_matViewsSQLCommand))
        {
            preparedStatement.setString(1, schemaName);

            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                while (resultSet.next())
                {
                    String  viewName    = resultSet.getString("matviewname");
                    String  viewOwner   = resultSet.getString("matviewowner");
                    boolean hasIndexes  = resultSet.getBoolean("hasindexes");
                    boolean isPopulated = resultSet.getBoolean("ispopulated");
                    String  definition  = resultSet.getString("definition");

                    SchemaDetails schemaDetails = databaseDetails.getSchemaDetails(schemaName);
                    TableDetails  tableDetails  = schemaDetails.getTableDetails(viewName);

                    tableDetails.setMaterializedViewDetails(viewOwner,
                                                            hasIndexes,
                                                            isPopulated,
                                                            definition);
                }
            }
        }
        catch (SQLException sqlException)
        {
            logQueryFailure("materialized view definitions", "schema " + qualifiedSchemaName, sqlException);
        }

        /*
         * The total size of the schema is the sum of its tables' sizes.
         */
        SchemaDetails currentSchema = databaseDetails.schemas.get(schemaName);

        if (currentSchema != null)
        {
            long schemaSize = 0L;

            for (TableDetails currentTable : currentSchema.tables.values())
            {
                schemaSize = schemaSize + currentTable.getTableMeasurements().getTableSize();
            }

            currentSchema.setSize(schemaSize);
        }
    }


    /**
     * Record that a query failed.  The survey carries on without its results.
     *
     * @param informationSought description of the information the query was retrieving
     * @param elementName       description of the element the query was about
     * @param sqlException      the failure
     */
    private void logQueryFailure(String       informationSought,
                                 String       elementName,
                                 SQLException sqlException)
    {
        final String methodName = "logQueryFailure";

        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                PostgresAuditCode.SURVEY_QUERY_FAILED.getMessageDefinition(surveyActionServiceName,
                                                                                           informationSought,
                                                                                           elementName,
                                                                                           sqlException.getClass().getName(),
                                                                                           sqlException.getMessage()));
        }
    }


    /**
     * Return the database-level annotations for every database whose results have not yet been released, and
     * release them.  This is used when the survey stops after the database statistics have been gathered,
     * before the schemas are surveyed - so the annotations have no schema, table or column counts.
     *
     * @return list of annotations, or null if there are no results
     * @throws PropertyServerException problem formatting JSON string
     */
    List<DatabaseElementAnnotation> getDatabaseAnnotations() throws PropertyServerException
    {
        List<DatabaseElementAnnotation> annotations = new ArrayList<>();

        for (String databaseName : new ArrayList<>(databaseResults.keySet()))
        {
            DatabaseDetails databaseDetails = databaseResults.remove(databaseName);

            if (databaseDetails != null)
            {
                annotations.add(this.getDatabaseAnnotation(databaseName, databaseDetails));
            }
        }

        if (! annotations.isEmpty())
        {
            return annotations;
        }

        return null;
    }


    /**
     * Build the annotation that holds the measurements for a database.
     *
     * @param databaseName name of the database
     * @param databaseDetails results for the database
     * @return annotation
     * @throws PropertyServerException problem formatting JSON string
     */
    private DatabaseElementAnnotation getDatabaseAnnotation(String          databaseName,
                                                       DatabaseDetails databaseDetails) throws PropertyServerException
    {
        ResourceMeasureAnnotationProperties databaseAnnotation = new ResourceMeasureAnnotationProperties();

        databaseAnnotation.setQualifiedName("Annotation::" + SurveyDatabaseAnnotationType.DATABASE_MEASUREMENTS.getName() + "::" + databaseName + "::" + UUID.randomUUID());
        databaseAnnotation.setAnnotationType(SurveyDatabaseAnnotationType.DATABASE_MEASUREMENTS.getName());
        databaseAnnotation.setSummary(SurveyDatabaseAnnotationType.DATABASE_MEASUREMENTS.getSummary());
        databaseAnnotation.setExplanation(SurveyDatabaseAnnotationType.DATABASE_MEASUREMENTS.getExplanation());
        databaseAnnotation.setAnalysisStep(SurveyDatabaseAnnotationType.DATABASE_MEASUREMENTS.getAnalysisStep());

        databaseAnnotation.setJsonProperties(surveyActionServiceConnector.getJSONProperties(databaseDetails.getDatabaseMeasurements()));

        databaseAnnotation.setResourceProperties(databaseDetails.getDatabaseResourceProperties());

        return new DatabaseElementAnnotation(databaseAnnotation, databaseName, null, null, null);
    }


    /**
     * Convert the information retrieved from the database server about a database into annotations
     * recognized by the survey action framework.  The results for the database are released once the
     * annotations are created, so that a server survey does not hold the details of every database in memory
     * at once.
     *
     * @param databaseName name of the database
     * @return list of annotations, or null if there are no results for the database
     * @throws PropertyServerException problem formatting JSON string
     */
    List<DatabaseElementAnnotation> getAnnotations(String databaseName) throws PropertyServerException
    {
        List<DatabaseElementAnnotation> annotations = new ArrayList<>();

        DatabaseDetails databaseDetails = databaseResults.remove(databaseName);

        if (databaseDetails != null)
        {
            List<String> schemaNames = databaseDetails.getSchemaNames();

            annotations.add(this.getDatabaseAnnotation(databaseName, databaseDetails));

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

                    annotations.add(new DatabaseElementAnnotation(schemaAnnotation, databaseName, schemaName, null, null));


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

                                annotations.add(new DatabaseElementAnnotation(tableAnnotation, databaseName, schemaName, tableName, null));

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

                                        annotations.add(new DatabaseElementAnnotation(columnAnnotation, databaseName, schemaName, tableName, columnName));
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
            resourceProperties.put(RelationalDatabaseMetric.SESSION_TIME.getDisplayName(), Double.toString(databaseMeasurements.getSessionTime()));
            resourceProperties.put(RelationalDatabaseMetric.ACTIVE_TIME.getDisplayName(), Double.toString(databaseMeasurements.getActiveTime()));
            if (databaseMeasurements.getStatsReset() != null)
            {
                resourceProperties.put(RelationalDatabaseMetric.LAST_STATISTICS_RESET.getDisplayName(), databaseMeasurements.getStatsReset().toString());
            }

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
         * Add the size of the schema.
         *
         * @param size number of bytes
         */
        void setSize(long size)
        {
            schemaMeasurement.setTotalTableSize(size);
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
         * Return the number of tables.
         *
         * @return table count
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
         * Return the number of columns summed from all of the columns.
         *
         * @return column count (from all columns)
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
         * @param hasRules does the table have rules attached
         * @param hasTriggers does the table have triggers attached
         * @param hasRowSecurity is row security set on
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
         * Capture how much data is stored in this column.
         *
         * @param columnSize long (bytes)
         */
        public void setColumnSize(long columnSize)
        {
            this.columnMeasurement.setColumnSize(columnSize);
        }


        void setColumnMeasurement(int     averageColumnWidth,
                                  long    numberOfDistinctValues,
                                  Array   mostCommonValues,
                                  Array   mostCommonFrequencies,
                                  String  columnTypeName,
                                  boolean columnNotNull)
        {
            this.columnMeasurement.setAverageColumnWidth(averageColumnWidth);
            this.columnMeasurement.setNumberOfDistinctValues(numberOfDistinctValues);
            if (mostCommonValues != null)
            {
                this.columnMeasurement.setMostCommonValues(mostCommonValues.toString());
            }

            if (mostCommonFrequencies != null)
            {
                this.columnMeasurement.setMostCommonValuesFrequency(mostCommonFrequencies.toString());
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
