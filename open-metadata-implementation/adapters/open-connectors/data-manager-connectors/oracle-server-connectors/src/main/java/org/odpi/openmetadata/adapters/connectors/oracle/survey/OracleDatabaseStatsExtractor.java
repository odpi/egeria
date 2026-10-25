/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.oracle.survey;

import org.odpi.openmetadata.adapters.connectors.oracle.ffdc.OracleAuditCode;
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
 * OracleDatabaseStatsExtractor gathers the same measurements as the PostgreSQL equivalent
 * (org.odpi.openmetadata.adapters.connectors.postgres.survey.PostgresDatabaseStatsExtractor) but sources
 * them from Oracle Database's data dictionary views (v$pdbs, cdb_data_files, all_users, all_tables, all_views,
 * all_mviews, all_tab_columns, all_tab_col_statistics, all_tab_histograms, all_tab_modifications,
 * dba_segments) instead of pg_catalog.  All of these sources are metadata/statistics only - no user data
 * is read.  The database-level connection is to the CDB root (for the cross-PDB size lookup); the
 * schema-level connection is to a specific pluggable database (PDB), which is the point at which the
 * ALL_* data dictionary views become scoped to that one container.
 * <br><br>
 * The connections passed to it are expected to be in auto-commit mode, so that every query is its own short
 * transaction.  Oracle has no aborted-transaction state like PostgreSQL, but a long-running transaction still
 * pins undo for its read-consistent snapshot, risking ORA-01555 (snapshot too old) on a busy server.  Each
 * schema is surveyed with its own set of queries, and a query that fails is logged and skipped, so one
 * inaccessible schema or dropped table leaves a gap in the results rather than ending the survey.
 * <br><br>
 * Oracle JDBC rejects a trailing ';' on a plain SQL statement, so none of the statements here have one.
 */
public class OracleDatabaseStatsExtractor extends AuditLoggingBase
{
    private final List<String>                 validDatabases;
    private final List<String>                 excludedSchemas;
    private final List<String>                 includedSchemas;
    private final SurveyActionServiceConnector surveyActionServiceConnector;
    private final String                       surveyActionServiceName;
    private final Map<String, DatabaseDetails> databaseResults = new HashMap<>();


    /**
     * Constructor sets up the list of pluggable databases to process and the connection to the database.
     * Every schema in those databases is surveyed.
     *
     * @param validDatabases               list of pluggable database (PDB) names
     * @param surveyActionServiceConnector calling connector
     */
    public OracleDatabaseStatsExtractor(List<String>                validDatabases,
                                        SurveyActionServiceConnector surveyActionServiceConnector)
    {
        this(validDatabases, null, null, surveyActionServiceConnector, null, null);
    }


    /**
     * Constructor sets up the list of pluggable databases to process, the schemas within them to survey,
     * and the connection to the database.
     *
     * @param validDatabases               list of pluggable database (PDB) names
     * @param excludedSchemas              schemas to leave out (null means none are excluded)
     * @param includedSchemas              the only schemas to survey (null means all that are not excluded);
     *                                     takes precedence over excludedSchemas
     * @param surveyActionServiceConnector calling connector
     * @param auditLog                     logging destination for queries that fail (may be null)
     * @param surveyActionServiceName      name of the calling survey action service, for the audit log
     */
    public OracleDatabaseStatsExtractor(List<String>                validDatabases,
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
     * Determine whether a schema should be surveyed.  A schema with no name never is.  Oracle-maintained
     * schemas (SYS, SYSTEM and the like) are not tested here because the schema-list query already leaves
     * them out by selecting from all_users on oracle_maintained = 'N'.  Otherwise the include list, if there
     * is one, decides; failing that, the exclude list does.  This follows SurveyContext.elementShouldBeSurveyed,
     * which applies the same rule to database names.
     *
     * @param schemaName name of the schema
     * @return flag indicating whether to gather statistics for the schema
     */
    boolean schemaShouldBeSurveyed(String schemaName)
    {
        if (schemaName == null)
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
     * Retrieve statistics about each requested pluggable database (PDB).
     * This is expected to be called on a connection to the CDB root, since v$pdbs and cdb_data_files are
     * cross-container catalog views that only show every PDB from there (from inside a PDB they show just
     * that PDB, which is what the single-database survey needs).  Every requested database gets an entry in
     * the results, even if its statistics cannot be read, so that its schemas can still be surveyed.
     *
     * @param cdbRootConnection connection to the CDB root of the Oracle Database Server
     */
    void getDatabaseStatistics(java.sql.Connection cdbRootConnection)
    {
        /*
         * cdb_data_files is a cross-container catalog view listing the datafiles of every PDB in the CDB,
         * joined here via con_id back to the PDB name - the direct equivalent of Microsoft SQL Server's
         * sys.master_files / PostgreSQL's pg_database_size().
         */
        final String databaseSizeSQLCommand =
                "SELECT p.name AS pdbname, SUM(f.bytes) AS databasesize " +
                        "FROM v$pdbs p " +
                        "JOIN cdb_data_files f ON f.con_id = p.con_id " +
                        "GROUP BY p.name";

        for (String databaseName : validDatabases)
        {
            RelationalDataManagerMeasurement databaseMeasurement = new RelationalDataManagerMeasurement();

            databaseMeasurement.setResourceName(databaseName);
            databaseResults.put(databaseName, new DatabaseDetails(databaseMeasurement));
        }

        /*
         * Oracle has no cross-PDB equivalent of Microsoft SQL Server's sys.dm_db_index_usage_stats /
         * PostgreSQL's pg_stat_database read/write counters that can be queried from the CDB root without
         * the Diagnostics Pack (AWR), so rowsFetched/rowsUpdated are left at their default of zero at the
         * database level - in the same way that the Microsoft SQL Server connector leaves
         * rowsInserted/rowsDeleted at zero at this level.
         *
         * Extract the total size of each database.
         */
        try (PreparedStatement preparedStatement = cdbRootConnection.prepareStatement(databaseSizeSQLCommand);
             ResultSet         resultSet         = preparedStatement.executeQuery())
        {
            while (resultSet.next())
            {
                DatabaseDetails currentDatabase = databaseResults.get(resultSet.getString("pdbname"));

                if (currentDatabase != null)
                {
                    currentDatabase.setSize(resultSet.getLong("databasesize"));
                }
            }
        }
        catch (SQLException sqlException)
        {
            logQueryFailure("pluggable database sizes", "the database server", sqlException);
        }
    }


    /**
     * Extract detailed information about the schemas, tables and columns in a pluggable database (PDB).
     * This only returns information from the connected PDB - so the connection needs to be pointing directly
     * at the named PDB's service (rather than the CDB root), which is what makes the ALL_* data dictionary
     * views resolve to just that one container.
     * <br><br>
     * Each schema is surveyed with its own queries, so a failure is confined to the schema (or the
     * part of the schema) where it occurred.
     *
     * @param databaseName          name of the pluggable database that is connected to
     * @param databaseSQLConnection connection to the named pluggable database
     * @throws SQLException the schemas in the database could not be listed
     */
    void getSchemaStatistics(String              databaseName,
                             java.sql.Connection databaseSQLConnection) throws SQLException
    {
        /*
         * Oracle-maintained schemas (SYS, SYSTEM, XDB and the like) are left out.
         */
        final String schemasSQLCommand = "SELECT username FROM all_users WHERE oracle_maintained = 'N' ORDER BY username";

        DatabaseDetails databaseDetails = databaseResults.get(databaseName);

        if (databaseDetails != null)
        {
            List<String> schemaNames = new ArrayList<>();

            try (PreparedStatement preparedStatement = databaseSQLConnection.prepareStatement(schemasSQLCommand);
                 ResultSet         resultSet         = preparedStatement.executeQuery())
            {
                while (resultSet.next())
                {
                    String schemaName = resultSet.getString("username");

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

                this.getSchemaStatistics(databaseDetails, databaseName, schemaName, databaseSQLConnection);
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
     * @param databaseDetails       results for the pluggable database that is connected to
     * @param databaseName          name of the pluggable database that is connected to
     * @param schemaName            name of the schema (owner) to survey
     * @param databaseSQLConnection connection to the pluggable database
     */
    private void getSchemaStatistics(DatabaseDetails     databaseDetails,
                                     String              databaseName,
                                     String              schemaName,
                                     java.sql.Connection databaseSQLConnection)
    {
        /*
         * One row per column, giving its declared width/type/nullability plus a best-effort distinct-value estimate
         * and single most-common-value/frequency, both sourced from Oracle's own auto-collected column statistics
         * (all_tab_col_statistics, all_tab_histograms) - an already-collected estimate, just like PostgreSQL's
         * pg_stats (populated by ANALYZE), so no user data is scanned to build this annotation.  The frequency
         * of the most common value is derived from a FREQUENCY histogram's cumulative endpoint_number, the
         * standard technique for reading Oracle frequency histograms: each bucket's own frequency is the
         * difference between its endpoint_number and the previous bucket's endpoint_number.
         */
        final String columnStatsSQLCommand =
                "SELECT tablename, columnname, avgwidth, typename, isnullable, ndistinct, mostcommonvalue, mostcommonfreq " +
                        "FROM ( " +
                        "    SELECT tablename, columnname, avgwidth, typename, isnullable, ndistinct, mostcommonvalue, mostcommonfreq, " +
                        /*
                         * Oracle does not allow a window function expression to be referenced directly inside the
                         * ORDER BY of another window function (ORA-30483), so the LAG()-derived mostcommonfreq is
                         * computed as a named column in the inner subquery first, and only that plain column name
                         * is referenced by ROW_NUMBER()'s ORDER BY here.
                         */
                        "           ROW_NUMBER() OVER (PARTITION BY tablename, columnname ORDER BY mostcommonfreq DESC NULLS LAST) AS rn " +
                        "    FROM ( " +
                        "        SELECT c.table_name AS tablename, " +
                        "               c.column_name AS columnname, " +
                        "               c.avg_col_len AS avgwidth, " +
                        "               c.data_type AS typename, " +
                        "               c.nullable AS isnullable, " +
                        "               s.num_distinct AS ndistinct, " +
                        "               h.endpoint_actual_value AS mostcommonvalue, " +
                        "               h.endpoint_number - LAG(h.endpoint_number, 1, 0) OVER (PARTITION BY h.owner, h.table_name, h.column_name ORDER BY h.endpoint_number) AS mostcommonfreq " +
                        "        FROM all_tab_columns c " +
                        "        LEFT OUTER JOIN all_tab_col_statistics s ON s.owner = c.owner AND s.table_name = c.table_name AND s.column_name = c.column_name " +
                        "        LEFT OUTER JOIN all_tab_histograms h ON h.owner = c.owner AND h.table_name = c.table_name AND h.column_name = c.column_name AND h.endpoint_actual_value IS NOT NULL " +
                        "        WHERE c.owner = ? " +
                        "    ) base " +
                        ") ranked " +
                        "WHERE rn = 1";

        /*
         * One row per table, with insert/update/delete counts sourced from all_tab_modifications - the direct
         * Oracle analog of Microsoft SQL Server's sys.dm_db_index_operational_stats / PostgreSQL's
         * pg_stat_user_tables.n_tup_ins/n_tup_upd/n_tup_del.  hasRowSecurity is derived from all_policies,
         * which lists Oracle's Virtual Private Database (VPD) row-level security policies.
         * all_tab_modifications has a row per partition as well as one for the whole table (partition_name
         * null), so only the whole-table row is joined to avoid repeating the table.
         */
        final String tablesSQLCommand =
                "SELECT t.table_name AS tablename, " +
                        "       t.owner AS tableowner, " +
                        "       CASE WHEN EXISTS (SELECT 1 FROM all_indexes i WHERE i.table_owner = t.owner AND i.table_name = t.table_name) THEN 1 ELSE 0 END AS hasindexes, " +
                        "       CASE WHEN EXISTS (SELECT 1 FROM all_triggers tr WHERE tr.table_owner = t.owner AND tr.table_name = t.table_name) THEN 1 ELSE 0 END AS hastriggers, " +
                        "       CASE WHEN EXISTS (SELECT 1 FROM all_policies p WHERE p.object_owner = t.owner AND p.object_name = t.table_name) THEN 1 ELSE 0 END AS hasrowsecurity, " +
                        "       NVL(m.inserts, 0) AS n_tup_ins, " +
                        "       NVL(m.updates, 0) AS n_tup_upd, " +
                        "       NVL(m.deletes, 0) AS n_tup_del " +
                        "FROM all_tables t " +
                        "LEFT OUTER JOIN all_tab_modifications m ON m.table_owner = t.owner AND m.table_name = t.table_name AND m.partition_name IS NULL " +
                        "WHERE t.owner = ?";

        /*
         * Plain views.
         */
        final String viewsSQLCommand =
                "SELECT v.view_name AS viewname, v.owner AS viewowner, v.text AS definition " +
                        "FROM all_views v " +
                        "WHERE v.owner = ?";

        /*
         * Materialized views - Oracle's direct equivalent of PostgreSQL materialized views.
         */
        final String materializedViewsSQLCommand =
                "SELECT m.mview_name AS viewname, " +
                        "       m.owner AS viewowner, " +
                        "       m.query AS definition, " +
                        "       CASE WHEN EXISTS (SELECT 1 FROM all_indexes i WHERE i.table_owner = m.owner AND i.table_name = m.mview_name) THEN 1 ELSE 0 END AS hasindexes, " +
                        "       CASE WHEN m.last_refresh_date IS NOT NULL THEN 1 ELSE 0 END AS ispopulated " +
                        "FROM all_mviews m " +
                        "WHERE m.owner = ?";

        /*
         * The size of every table in the schema, using dba_segments - the Oracle equivalent of Microsoft
         * SQL Server's sys.dm_db_partition_stats / PostgreSQL's pg_table_size() function.  Table, partition
         * and subpartition segments are summed to cover both partitioned and non-partitioned tables.
         * <br><br>
         * Unlike ALL_TABLES/ALL_TAB_COLUMNS etc., there is no ALL_SEGMENTS view in Oracle - only USER_SEGMENTS
         * (the connected user's own segments) and DBA_SEGMENTS (every segment, requiring SELECT_CATALOG_ROLE or
         * DBA privilege).  Since this connector already needs schema-spanning visibility for other catalog
         * views such as ALL_TAB_MODIFICATIONS and ALL_POLICIES, DBA_SEGMENTS is used here rather than narrowing
         * to just the connecting user's own segments.  It is a separate query, rather than a join on the tables
         * query, so that a userId without access to DBA_SEGMENTS still gets the rest of the table details.
         */
        final String tableSizesSQLCommand =
                "SELECT segment_name AS tablename, SUM(bytes) AS tablesize " +
                        "FROM dba_segments " +
                        "WHERE owner = ? AND segment_type IN ('TABLE','TABLE PARTITION','TABLE SUBPARTITION') " +
                        "GROUP BY segment_name";

        final String qualifiedSchemaName = databaseName + "." + schemaName;

        /*
         * Extract key stats about each column.  This builds up the basic structure of tables and columns and
         * enables the calculation of the total number of tables and columns to be determined.  The declared
         * average column width (avg_col_len) is also used directly as the column's size measurement - like
         * Microsoft SQL Server, Oracle has no way to estimate the actual stored size of a column without
         * scanning data beyond this auto-collected statistic, so avg_col_len is used as a metadata-only
         * approximation.
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
                    boolean columnNullable         = "Y".equals(resultSet.getString("isnullable"));

                    SchemaDetails schemaDetails = databaseDetails.getSchemaDetails(databaseName, schemaName);
                    TableDetails  tableDetails  = schemaDetails.getTableDetails(schemaName, tableName);
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
                    String  tableName            = resultSet.getString("tablename");
                    String  tableOwner           = resultSet.getString("tableowner");
                    boolean hasIndexes           = resultSet.getBoolean("hasindexes");
                    boolean hasTriggers          = resultSet.getBoolean("hastriggers");
                    boolean hasRowSecurity       = resultSet.getBoolean("hasrowsecurity");
                    long    numberOfRowsInserted = resultSet.getLong("n_tup_ins");
                    long    numberOfRowsUpdated  = resultSet.getLong("n_tup_upd");
                    long    numberOfRowsDeleted  = resultSet.getLong("n_tup_del");

                    SchemaDetails schemaDetails = databaseDetails.getSchemaDetails(databaseName, schemaName);
                    TableDetails  tableDetails  = schemaDetails.getTableDetails(schemaName, tableName);

                    /*
                     * Oracle has no equivalent of PostgreSQL's CREATE RULE feature, so hasRules is
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
        }
        catch (SQLException sqlException)
        {
            logQueryFailure("table details", "schema " + qualifiedSchemaName, sqlException);
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

                    SchemaDetails schemaDetails = databaseDetails.getSchemaDetails(databaseName, schemaName);
                    TableDetails  tableDetails  = schemaDetails.getTableDetails(schemaName, viewName);

                    tableDetails.setViewDetails(viewOwner, definition);
                }
            }
        }
        catch (SQLException sqlException)
        {
            logQueryFailure("view definitions", "schema " + qualifiedSchemaName, sqlException);
        }

        /*
         * Extract details of the materialized views.  Oracle also lists a materialized view's container
         * table in all_tables under the same name, so this reclassifies that entry rather than adding a new one.
         */
        try (PreparedStatement preparedStatement = databaseSQLConnection.prepareStatement(materializedViewsSQLCommand))
        {
            preparedStatement.setString(1, schemaName);

            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                while (resultSet.next())
                {
                    String  viewName    = resultSet.getString("viewname");
                    String  viewOwner   = resultSet.getString("viewowner");
                    String  definition  = resultSet.getString("definition");
                    boolean hasIndexes  = resultSet.getBoolean("hasindexes");
                    boolean isPopulated = resultSet.getBoolean("ispopulated");

                    SchemaDetails schemaDetails = databaseDetails.getSchemaDetails(databaseName, schemaName);
                    TableDetails  tableDetails  = schemaDetails.getTableDetails(schemaName, viewName);

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
         * The schema's details only exist if one of the queries above found something in it.  An empty
         * schema has nothing to size.
         */
        SchemaDetails currentSchema = databaseDetails.schemas.get(schemaName);

        if (currentSchema != null)
        {
            /*
             * Extract the size of every table in the schema with one query.  The segment names are the
             * dictionary's own table names, so they match the names already collected without any quoting.
             * Segments that do not belong to a table already found (for example, a table created since the
             * queries above ran) are ignored rather than added as new tables.
             */
            try (PreparedStatement preparedStatement = databaseSQLConnection.prepareStatement(tableSizesSQLCommand))
            {
                preparedStatement.setString(1, schemaName);

                try (ResultSet resultSet = preparedStatement.executeQuery())
                {
                    while (resultSet.next())
                    {
                        TableDetails currentTable = currentSchema.tables.get(resultSet.getString("tablename"));

                        if (currentTable != null)
                        {
                            currentTable.setTableSize(resultSet.getLong("tablesize"));
                        }
                    }
                }
            }
            catch (SQLException sqlException)
            {
                logQueryFailure("table sizes", "schema " + qualifiedSchemaName, sqlException);
            }

            /*
             * The total size of the schema is the sum of its tables' sizes.
             */
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
                  OracleAuditCode.SURVEY_QUERY_FAILED.getMessageDefinition(surveyActionServiceName,
                                                                           informationSought,
                                                                           elementName,
                                                                           sqlException.getClass().getName(),
                                                                           sqlException.getMessage()));
    }


    /**
     * Return the database-level annotations for every pluggable database whose results have not yet been released, and
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
     * Build the annotation that holds the measurements for a pluggable database.
     *
     * @param databaseName name of the pluggable database
     * @param databaseDetails results for the pluggable database
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
     * Convert the information retrieved from the database server about a pluggable database into annotations
     * recognized by the survey action framework.  The results for the database are released once the
     * annotations are created, so that a server survey does not hold the details of every database in memory
     * at once.
     *
     * @param databaseName name of the pluggable database
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
                SchemaDetails currentSchema = databaseDetails.getSchemaDetails(databaseName, schemaName);

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
                            TableDetails currentTable = currentSchema.getTableDetails(schemaName, tableName);

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
         * @param databaseName name of the pluggable database
         * @param schemaName name of schema
         * @return corresponding schema details object
         */
        SchemaDetails getSchemaDetails(String databaseName, String schemaName)
        {
            SchemaDetails schemaDetails = schemas.get(schemaName);

            if (schemaDetails == null)
            {
                schemaDetails = new SchemaDetails(databaseName, schemaName);

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
         * @param databaseName name of the pluggable database
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
         * Return the number of materialized views.
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
         * @param schemaOwner the raw (non-database-qualified) Oracle schema/owner name
         * @param tableName name of table
         * @return corresponding table details object
         */
        TableDetails getTableDetails(String schemaOwner, String tableName)
        {
            TableDetails tableDetails = tables.get(tableName);

            if (tableDetails == null)
            {
                tableDetails = new TableDetails(this.getQualifiedSchemaName(), schemaOwner, tableName);

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
                     String schemaOwner,
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
         * @param hasRules does the table have rules attached (always false - Oracle has no rules)
         * @param hasTriggers does the table have triggers attached
         * @param hasRowSecurity is Virtual Private Database (VPD) row-level security set on
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
         * Set the measurements gathered for this column.  The declared average column width (avg_col_len) is
         * used directly as the column size, since Oracle has no metadata-only way to estimate the actual
         * stored size of a column's values beyond this auto-collected statistic.
         *
         * @param averageColumnWidth declared average width in bytes
         * @param numberOfDistinctValues estimate from the column's auto-collected statistics
         * @param mostCommonValue single most common value found in the column's frequency histogram, if any
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
