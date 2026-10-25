/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.mssql.survey;

import org.odpi.openmetadata.adapters.connectors.mssql.ffdc.MSSQLAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingBase;
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
 * MSSQLDatabaseStatsExtractor gathers the same measurements as the PostgreSQL equivalent
 * (org.odpi.openmetadata.adapters.connectors.postgres.survey.PostgresDatabaseStatsExtractor) but sources
 * them from Microsoft SQL Server's system catalog views and DMVs (sys.databases, sys.tables, sys.views,
 * sys.columns, sys.dm_db_partition_stats, sys.dm_db_index_operational_stats, sys.dm_db_stats_histogram)
 * instead of pg_catalog.  All of these sources are metadata/statistics only - no user data is read.
 * <br><br>
 * The connections passed to it are expected to be in auto-commit mode, so that every query is its own short
 * transaction.  Each schema is surveyed with its own set of queries, and a query that fails is logged and
 * skipped, so one inaccessible schema, missing permission or dropped table leaves a gap in the results rather
 * than ending the survey.
 */
public class MSSQLDatabaseStatsExtractor extends AuditLoggingBase
{
    private final List<String>                 validDatabases;
    private final List<String>                 excludedSchemas;
    private final List<String>                 includedSchemas;
    private final SurveyActionServiceConnector surveyActionServiceConnector;
    private final String                       surveyActionServiceName;
    private final Map<String, DatabaseDetails> databaseResults = new HashMap<>();


    /**
     * Constructor sets up the list of databases to process and the connection to the database.  Every schema
     * in those databases is surveyed.
     *
     * @param validDatabases               list of database names
     * @param surveyActionServiceConnector calling connector
     */
    public MSSQLDatabaseStatsExtractor(List<String>                validDatabases,
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
    public MSSQLDatabaseStatsExtractor(List<String>                validDatabases,
                                       List<String>                excludedSchemas,
                                       List<String>                includedSchemas,
                                       SurveyActionServiceConnector surveyActionServiceConnector,
                                       AuditLog                    auditLog,
                                       String                      surveyActionServiceName)
    {
        super(auditLog);

        this.validDatabases               = validDatabases;
        this.excludedSchemas              = excludedSchemas;
        this.includedSchemas              = includedSchemas;
        this.surveyActionServiceConnector = surveyActionServiceConnector;
        this.surveyActionServiceName      = surveyActionServiceName;
    }


    /**
     * Determine whether a schema should be surveyed.  Microsoft SQL Server's own catalog schemas (sys and
     * INFORMATION_SCHEMA) never are.  Otherwise the include list, if there is one, decides; failing that, the
     * exclude list does.  This follows SurveyContext.elementShouldBeSurveyed, which applies the same rule to
     * database names.
     *
     * @param schemaName name of the schema
     * @return flag indicating whether to gather statistics for the schema
     */
    boolean schemaShouldBeSurveyed(String schemaName)
    {
        if ((schemaName == null) || (schemaName.equals("sys")) || (schemaName.equals("INFORMATION_SCHEMA")))
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
     * Retrieve statistics about each requested database.
     * This works whether connected to the master database or a user database, since sys.databases and
     * sys.master_files are server-wide catalog views visible from any connection.  Every requested database
     * gets an entry in the results, even if its statistics cannot be read, so that its schemas can still be
     * surveyed.
     *
     * @param serverJDBCConnection connection to the SQL Server instance
     */
    void getDatabaseStatistics(java.sql.Connection serverJDBCConnection)
    {
        final String databaseActivitySQLCommand =
                "SELECT d.name AS dbname, " +
                        "       SUM(ius.user_seeks + ius.user_scans + ius.user_lookups) AS rowsfetched, " +
                        "       SUM(ius.user_updates) AS rowsupdated " +
                        "FROM sys.dm_db_index_usage_stats ius " +
                        "JOIN sys.databases d ON d.database_id = ius.database_id " +
                        "GROUP BY d.name;";

        /*
         * The size of every database in one query, matched by database_id rather than by name so that names
         * needing quoting are not a problem.
         */
        final String databaseSizeSQLCommand =
                "SELECT d.name AS dbname, SUM(CAST(mf.size AS BIGINT)) * 8 * 1024 AS databasesize " +
                        "FROM sys.master_files mf " +
                        "JOIN sys.databases d ON d.database_id = mf.database_id " +
                        "GROUP BY d.name;";

        for (String databaseName : validDatabases)
        {
            RelationalDataManagerMeasurement databaseMeasurement = new RelationalDataManagerMeasurement();

            databaseMeasurement.setResourceName(databaseName);
            databaseResults.put(databaseName, new DatabaseDetails(databaseMeasurement));
        }

        /*
         * Add the read/write activity counters.  Microsoft SQL Server does not expose a database-wide split of
         * inserted/updated/deleted row counts the way PostgreSQL's pg_stat_database does (that granularity is
         * only available per-table, via sys.dm_db_index_operational_stats, applied in getSchemaStatistics
         * below), so rowsInserted/rowsDeleted are left at their default of zero at the database level.
         * This DMV needs the VIEW SERVER STATE permission.
         */
        try (PreparedStatement preparedStatement = serverJDBCConnection.prepareStatement(databaseActivitySQLCommand);
             ResultSet         resultSet         = preparedStatement.executeQuery())
        {
            while (resultSet.next())
            {
                DatabaseDetails currentDatabase = databaseResults.get(resultSet.getString("dbname"));

                if (currentDatabase != null)
                {
                    currentDatabase.setActivity(resultSet.getLong("rowsfetched"), resultSet.getLong("rowsupdated"));
                }
            }
        }
        catch (SQLException sqlException)
        {
            logQueryFailure("database activity statistics", "the database server", sqlException);
        }

        /*
         * Extract the total size of each database from sys.master_files (a server-wide catalog view).
         */
        try (PreparedStatement preparedStatement = serverJDBCConnection.prepareStatement(databaseSizeSQLCommand);
             ResultSet         resultSet         = preparedStatement.executeQuery())
        {
            while (resultSet.next())
            {
                DatabaseDetails currentDatabase = databaseResults.get(resultSet.getString("dbname"));

                if (currentDatabase != null)
                {
                    currentDatabase.setSize(resultSet.getLong("databasesize"));
                }
            }
        }
        catch (SQLException sqlException)
        {
            logQueryFailure("database sizes", "the database server", sqlException);
        }
    }


    /**
     * Extract detailed information about the schemas, tables and columns in a database.
     * This only returns information from the connected database - so the connection needs to be pointing at the
     * named user database rather than master.
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
        /*
         * Only schemas that own tables or views are of interest.  This skips the fixed database-role schemas
         * (db_owner, db_datareader, ...) and guest, which exist in every database but are almost always empty.
         */
        final String schemasSQLCommand =
                "SELECT s.name AS schemaname " +
                        "FROM sys.schemas s " +
                        "WHERE s.name NOT IN (N'sys', N'INFORMATION_SCHEMA') " +
                        "  AND EXISTS (SELECT 1 FROM sys.objects o WHERE o.schema_id = s.schema_id AND o.type IN ('U', 'V')) " +
                        "ORDER BY s.name;";

        DatabaseDetails databaseDetails = databaseResults.get(databaseName);

        if (databaseDetails != null)
        {
            List<String> schemaNames = new ArrayList<>();

            try (PreparedStatement preparedStatement = databaseSQLConnection.prepareStatement(schemasSQLCommand);
                 ResultSet         resultSet         = preparedStatement.executeQuery())
            {
                while (resultSet.next())
                {
                    String schemaName = resultSet.getString("schemaname");

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
        /*
         * One row per column, giving its declared width/type/nullability plus a best-effort distinct-value estimate
         * and single most-common-value/frequency, both sourced from Microsoft SQL Server's own auto-collected
         * column statistics (sys.dm_db_stats_histogram) - an already-collected estimate, just like PostgreSQL's
         * pg_stats (populated by ANALYZE), so no user data is scanned to build this annotation.  When several
         * statistics objects lead with the same column (an index and an auto-created statistic, for example),
         * only the first is used; otherwise the distinct-value estimate would count the same values once per
         * statistics object.
         */
        final String columnStatsSQLCommand =
                "SELECT tablename, columnname, avgwidth, typename, isnullable, mostcommonvalue, mostcommonfreq, ndistinct " +
                        "FROM ( " +
                        "    SELECT t.name AS tablename, " +
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
                        "    OUTER APPLY (SELECT TOP 1 sc.stats_id FROM sys.stats_columns sc " +
                        "                 WHERE sc.object_id = c.object_id AND sc.column_id = c.column_id AND sc.stats_column_id = 1 " +
                        "                 ORDER BY sc.stats_id) st " +
                        "    OUTER APPLY sys.dm_db_stats_histogram(c.object_id, st.stats_id) h " +
                        "    WHERE s.name = ? " +
                        ") ranked " +
                        "WHERE rn = 1;";

        /*
         * One row per table from the catalog views alone, so it only needs the permission to see the tables.
         */
        final String tablesSQLCommand =
                "SELECT t.name AS tablename, " +
                        "       COALESCE(USER_NAME(t.principal_id), USER_NAME(s.principal_id)) AS tableowner, " +
                        "       CASE WHEN EXISTS (SELECT 1 FROM sys.indexes i WHERE i.object_id = t.object_id AND i.index_id > 0) THEN 1 ELSE 0 END AS hasindexes, " +
                        "       CASE WHEN EXISTS (SELECT 1 FROM sys.triggers tr WHERE tr.parent_id = t.object_id) THEN 1 ELSE 0 END AS hastriggers, " +
                        "       CASE WHEN EXISTS (SELECT 1 FROM sys.security_predicates sp WHERE sp.target_object_id = t.object_id) THEN 1 ELSE 0 END AS hasrowsecurity " +
                        "FROM sys.tables t " +
                        "JOIN sys.schemas s ON t.schema_id = s.schema_id " +
                        "WHERE s.name = ?;";

        /*
         * One row per table with its insert/update/delete counts, sourced from sys.dm_db_index_operational_stats -
         * the direct Microsoft SQL Server analog of PostgreSQL's pg_stat_user_tables.n_tup_ins/n_tup_upd/n_tup_del -
         * and its size from sys.dm_db_partition_stats - the equivalent of PostgreSQL's pg_table_size().
         * index_id IN (0,1) covers heap tables (0) and tables with a clustered index (1), which together
         * represent the table's own data pages.  Both are located by object_id rather than by name, so names
         * needing quoting are not a problem.  These DMVs need the VIEW DATABASE STATE permission, which is why
         * they are kept apart from the catalog query above.
         */
        final String tableActivitySQLCommand =
                "SELECT t.name AS tablename, " +
                        "       ISNULL(ops.n_tup_ins, 0) AS n_tup_ins, " +
                        "       ISNULL(ops.n_tup_upd, 0) AS n_tup_upd, " +
                        "       ISNULL(ops.n_tup_del, 0) AS n_tup_del, " +
                        "       ISNULL(ps.tablesize, 0) AS tablesize " +
                        "FROM sys.tables t " +
                        "JOIN sys.schemas s ON t.schema_id = s.schema_id " +
                        "OUTER APPLY ( " +
                        "    SELECT SUM(leaf_insert_count) AS n_tup_ins, SUM(leaf_update_count) AS n_tup_upd, SUM(leaf_delete_count) AS n_tup_del " +
                        "    FROM sys.dm_db_index_operational_stats(DB_ID(), t.object_id, NULL, NULL) " +
                        ") ops " +
                        "OUTER APPLY ( " +
                        "    SELECT SUM(CAST(p.used_page_count AS BIGINT)) * 8 * 1024 AS tablesize " +
                        "    FROM sys.dm_db_partition_stats p " +
                        "    WHERE p.object_id = t.object_id AND p.index_id IN (0, 1) " +
                        ") ps " +
                        "WHERE s.name = ?;";

        /*
         * Plain views only - views with a unique clustered index (indexed views) are Microsoft SQL Server's closest
         * equivalent to PostgreSQL materialized views and are reported separately below.
         */
        final String viewsSQLCommand =
                "SELECT v.name AS viewname, " +
                        "       COALESCE(USER_NAME(v.principal_id), USER_NAME(s.principal_id)) AS viewowner, " +
                        "       m.definition AS definition " +
                        "FROM sys.views v " +
                        "JOIN sys.schemas s ON v.schema_id = s.schema_id " +
                        "LEFT OUTER JOIN sys.sql_modules m ON m.object_id = v.object_id " +
                        "WHERE s.name = ? " +
                        "  AND NOT EXISTS (SELECT 1 FROM sys.indexes i WHERE i.object_id = v.object_id AND i.index_id = 1);";

        /*
         * Indexed views - always populated and indexed by definition.
         */
        final String indexedViewsSQLCommand =
                "SELECT v.name AS viewname, " +
                        "       COALESCE(USER_NAME(v.principal_id), USER_NAME(s.principal_id)) AS viewowner, " +
                        "       m.definition AS definition " +
                        "FROM sys.views v " +
                        "JOIN sys.schemas s ON v.schema_id = s.schema_id " +
                        "LEFT OUTER JOIN sys.sql_modules m ON m.object_id = v.object_id " +
                        "WHERE s.name = ? " +
                        "  AND EXISTS (SELECT 1 FROM sys.indexes i WHERE i.object_id = v.object_id AND i.index_id = 1);";

        final String qualifiedSchemaName = databaseDetails.getDatabaseMeasurements().getResourceName() + "." + schemaName;

        /*
         * Extract key stats about each column.  This builds up the basic structure of tables and columns and
         * enables the calculation of the total number of tables and columns to be determined.  The declared
         * column width is also used directly as the column's size measurement - unlike PostgreSQL,
         * Microsoft SQL Server has no way to estimate the actual stored size of a column without scanning data,
         * so the declared max_length is used as a metadata-only approximation.
         */
        try (PreparedStatement preparedStatement = databaseSQLConnection.prepareStatement(columnStatsSQLCommand))
        {
            preparedStatement.setString(1, schemaName);

            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                while (resultSet.next())
                {
                    String  tableName              = resultSet.getString("tablename");
                    String  columnName             = resultSet.getString("columnname");
                    int     averageColumnWidth     = resultSet.getInt("avgwidth");
                    long    numberOfDistinctValues = resultSet.getLong("ndistinct");
                    String  mostCommonValue        = resultSet.getString("mostcommonvalue");
                    String  mostCommonFrequency    = resultSet.getString("mostcommonfreq");
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
        }
        catch (SQLException sqlException)
        {
            logQueryFailure("column statistics", "schema " + qualifiedSchemaName, sqlException);
        }

        /*
         * Extract core information about the tables
         */
        try (PreparedStatement preparedStatement = databaseSQLConnection.prepareStatement(tablesSQLCommand))
        {
            preparedStatement.setString(1, schemaName);

            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                while (resultSet.next())
                {
                    String  tableName      = resultSet.getString("tablename");
                    String  tableOwner     = resultSet.getString("tableowner");
                    boolean hasIndexes     = resultSet.getBoolean("hasindexes");
                    boolean hasTriggers    = resultSet.getBoolean("hastriggers");
                    boolean hasRowSecurity = resultSet.getBoolean("hasrowsecurity");

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
                                                      hasRowSecurity);
                }
            }
        }
        catch (SQLException sqlException)
        {
            logQueryFailure("table details", "schema " + qualifiedSchemaName, sqlException);
        }

        /*
         * Extract the activity counters and size of each table
         */
        try (PreparedStatement preparedStatement = databaseSQLConnection.prepareStatement(tableActivitySQLCommand))
        {
            preparedStatement.setString(1, schemaName);

            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                while (resultSet.next())
                {
                    String tableName            = resultSet.getString("tablename");
                    long   numberOfRowsInserted = resultSet.getLong("n_tup_ins");
                    long   numberOfRowsUpdated  = resultSet.getLong("n_tup_upd");
                    long   numberOfRowsDeleted  = resultSet.getLong("n_tup_del");
                    long   tableSize            = resultSet.getLong("tablesize");

                    SchemaDetails schemaDetails = databaseDetails.getSchemaDetails(schemaName);
                    TableDetails  tableDetails  = schemaDetails.getTableDetails(tableName);

                    tableDetails.setTableActivity(numberOfRowsInserted,
                                                  numberOfRowsUpdated,
                                                  numberOfRowsDeleted);
                    tableDetails.setTableSize(tableSize);
                }
            }
        }
        catch (SQLException sqlException)
        {
            logQueryFailure("table activity statistics and sizes", "schema " + qualifiedSchemaName, sqlException);
        }

        /*
         * Extract details of the views
         */
        try (PreparedStatement preparedStatement = databaseSQLConnection.prepareStatement(viewsSQLCommand))
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
         * Extract details of the indexed views (Microsoft SQL Server's closest equivalent of a
         * materialized view).
         */
        try (PreparedStatement preparedStatement = databaseSQLConnection.prepareStatement(indexedViewsSQLCommand))
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

                    tableDetails.setMaterializedViewDetails(viewOwner,
                                                            true,
                                                            true,
                                                            definition);
                }
            }
        }
        catch (SQLException sqlException)
        {
            logQueryFailure("indexed view definitions", "schema " + qualifiedSchemaName, sqlException);
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

            currentSchema.schemaMeasurement.setTotalTableSize(schemaSize);
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

        logRecord(methodName,
                  MSSQLAuditCode.SURVEY_QUERY_FAILED.getMessageDefinition(surveyActionServiceName,
                                                                          informationSought,
                                                                          elementName,
                                                                          sqlException.getClass().getName(),
                                                                          sqlException.getMessage()));
    }


    /**
     * Return the results gathered so far for a database.
     *
     * @param databaseName name of the database
     * @return results, or null if the database is not being surveyed (or its annotations have been produced)
     */
    DatabaseDetails getDatabaseDetails(String databaseName)
    {
        return databaseResults.get(databaseName);
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


        /**
         * Create a database details object.
         *
         * @param databaseMeasurements measurements for the database, with its name set
         */
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
         */
        void setTableMeasurements(String  tableOwner,
                                  boolean hasIndexes,
                                  boolean hasRules,
                                  boolean hasTriggers,
                                  boolean hasRowSecurity)
        {
            this.tableMeasurement.setTableOwner(tableOwner);
            this.tableMeasurement.setHasIndexes(hasIndexes);
            this.tableMeasurement.setHasRules(hasRules);
            this.tableMeasurement.setHasTriggers(hasTriggers);
            this.tableMeasurement.setHasRowSecurity(hasRowSecurity);
        }


        /**
         * Set up the activity counters for a table.  These come from a different source to the rest of the
         * table measurements, so they are set separately.
         *
         * @param numberOfRowsInserted how many rows have been inserted
         * @param numberOfRowsUpdated how many rows have been updated
         * @param numberOfRowsDeleted how many rows have been deleted
         */
        void setTableActivity(long numberOfRowsInserted,
                              long numberOfRowsUpdated,
                              long numberOfRowsDeleted)
        {
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


        /**
         * Set up information about an indexed view (Microsoft SQL Server's closest equivalent of a materialized
         * view).
         *
         * @param viewOwner owner
         * @param hasIndexes does the view have indexes
         * @param isPopulated is the view populated
         * @param definition query that retrieves the data for this view
         */
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
