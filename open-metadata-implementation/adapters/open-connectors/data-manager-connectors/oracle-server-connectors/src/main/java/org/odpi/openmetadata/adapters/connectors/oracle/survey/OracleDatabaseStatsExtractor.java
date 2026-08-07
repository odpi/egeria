/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.oracle.survey;

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
 * OracleDatabaseStatsExtractor gathers the same measurements as the PostgreSQL equivalent
 * (org.odpi.openmetadata.adapters.connectors.postgres.survey.PostgresDatabaseStatsExtractor) but sources
 * them from Oracle Database's data dictionary views (v$pdbs, cdb_data_files, all_tables, all_views,
 * all_mviews, all_tab_columns, all_tab_col_statistics, all_tab_histograms, all_tab_modifications,
 * dba_segments) instead of pg_catalog.  All of these sources are metadata/statistics only - no user data
 * is read.  The database-level connection is to the CDB root (for the cross-PDB size lookup); the
 * schema-level connection is to a specific pluggable database (PDB), which is the point at which the
 * ALL_* data dictionary views become scoped to that one container.
 */
public class OracleDatabaseStatsExtractor
{
    private final List<String>                 validDatabases;
    private final SurveyActionServiceConnector surveyActionServiceConnector;
    private final Map<String, DatabaseDetails> databaseResults = new HashMap<>();


    /**
     * Constructor sets up the list of pluggable databases to process and the connection to the database.
     *
     * @param validDatabases               list of pluggable database (PDB) names
     * @param surveyActionServiceConnector calling connector
     */
    public OracleDatabaseStatsExtractor(List<String>                validDatabases,
                                        SurveyActionServiceConnector surveyActionServiceConnector)
    {
        this.validDatabases               = validDatabases;
        this.surveyActionServiceConnector = surveyActionServiceConnector;
    }


    /**
     * Retrieve statistics about each requested pluggable database (PDB).
     * This is expected to be called on a connection to the CDB root, since v$pdbs and cdb_data_files are
     * cross-container catalog views only visible from there.
     *
     * @param cdbRootConnection connection to the CDB root of the Oracle Database Server
     * @throws SQLException            problem accessing the database
     * @throws PropertyServerException problem create JSON properties
     */
    void getDatabaseStatistics(java.sql.Connection cdbRootConnection) throws SQLException, PropertyServerException
    {
        final String databasesSQLCommand = "SELECT name FROM v$pdbs WHERE name != 'PDB$SEED';";

        /*
         * cdb_data_files is a cross-container catalog view listing the datafiles of every PDB in the CDB,
         * joined here via con_id back to the PDB name - the direct equivalent of Microsoft SQL Server's
         * sys.master_files / PostgreSQL's pg_database_size().
         */
        final String databaseSizeSQLCommand =
                "SELECT p.name AS pdbname, SUM(f.bytes) AS databasesize " +
                        "FROM v$pdbs p " +
                        "JOIN cdb_data_files f ON f.con_id = p.con_id " +
                        "GROUP BY p.name;";

        try
        {
            /*
             * Identify the requested PDBs that actually exist on this server.
             */
            PreparedStatement preparedStatement = cdbRootConnection.prepareStatement(databasesSQLCommand);

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
             * Oracle has no cross-PDB equivalent of Microsoft SQL Server's sys.dm_db_index_usage_stats /
             * PostgreSQL's pg_stat_database read/write counters that can be queried from the CDB root without
             * the Diagnostics Pack (AWR), so rowsFetched/rowsUpdated are left at their default of zero at the
             * database level - in the same way that the Microsoft SQL Server connector leaves
             * rowsInserted/rowsDeleted at zero at this level.
             */

            preparedStatement = cdbRootConnection.prepareStatement(databaseSizeSQLCommand);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                DatabaseDetails currentDatabase = databaseResults.get(resultSet.getString("pdbname"));

                if (currentDatabase != null)
                {
                    currentDatabase.setSize(resultSet.getLong("databasesize"));
                }
            }

            resultSet.close();
            preparedStatement.close();
        }
        catch (SQLException sqlException)
        {
            try
            {
                cdbRootConnection.rollback();
            }
            catch (Exception error)
            {
                // ignore
            }

            throw sqlException;
        }
    }


    /**
     * Extract detailed information about the schemas, tables and columns in a pluggable database (PDB).
     * This only returns information from the connected PDB - so the connection needs to be pointing directly
     * at the named PDB's service (rather than the CDB root), which is what makes the ALL_* data dictionary
     * views resolve to just that one container.
     *
     * @param databaseName          name of the pluggable database that is connected to
     * @param databaseSQLConnection connection to the named pluggable database
     * @throws SQLException            problem accessing the database
     */
    void getSchemaStatistics(String databaseName,
                             java.sql.Connection databaseSQLConnection) throws SQLException
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
                "SELECT schemaname, tablename, columnname, avgwidth, typename, isnullable, ndistinct, mostcommonvalue, mostcommonfreq " +
                        "FROM ( " +
                        "    SELECT schemaname, tablename, columnname, avgwidth, typename, isnullable, ndistinct, mostcommonvalue, mostcommonfreq, " +
                        /*
                         * Oracle does not allow a window function expression to be referenced directly inside the
                         * ORDER BY of another window function (ORA-30483), so the LAG()-derived mostcommonfreq is
                         * computed as a named column in the inner subquery first, and only that plain column name
                         * is referenced by ROW_NUMBER()'s ORDER BY here.
                         */
                        "           ROW_NUMBER() OVER (PARTITION BY schemaname, tablename, columnname ORDER BY mostcommonfreq DESC NULLS LAST) AS rn " +
                        "    FROM ( " +
                        "        SELECT c.owner AS schemaname, " +
                        "               c.table_name AS tablename, " +
                        "               c.column_name AS columnname, " +
                        "               c.avg_col_len AS avgwidth, " +
                        "               c.data_type AS typename, " +
                        "               c.nullable AS isnullable, " +
                        "               s.num_distinct AS ndistinct, " +
                        "               h.endpoint_actual_value AS mostcommonvalue, " +
                        "               h.endpoint_number - LAG(h.endpoint_number, 1, 0) OVER (PARTITION BY h.owner, h.table_name, h.column_name ORDER BY h.endpoint_number) AS mostcommonfreq " +
                        "        FROM all_tab_columns c " +
                        "        JOIN all_users u ON u.username = c.owner AND u.oracle_maintained = 'N' " +
                        "        LEFT OUTER JOIN all_tab_col_statistics s ON s.owner = c.owner AND s.table_name = c.table_name AND s.column_name = c.column_name " +
                        "        LEFT OUTER JOIN all_tab_histograms h ON h.owner = c.owner AND h.table_name = c.table_name AND h.column_name = c.column_name AND h.endpoint_actual_value IS NOT NULL " +
                        "    ) base " +
                        ") ranked " +
                        "WHERE rn = 1";

        /*
         * One row per table, with insert/update/delete counts sourced from all_tab_modifications - the direct
         * Oracle analog of Microsoft SQL Server's sys.dm_db_index_operational_stats / PostgreSQL's
         * pg_stat_user_tables.n_tup_ins/n_tup_upd/n_tup_del.  hasRowSecurity is derived from all_policies,
         * which lists Oracle's Virtual Private Database (VPD) row-level security policies.
         */
        final String tablesSQLCommand =
                "SELECT t.owner AS schemaname, " +
                        "       t.table_name AS tablename, " +
                        "       t.owner AS tableowner, " +
                        "       CASE WHEN EXISTS (SELECT 1 FROM all_indexes i WHERE i.table_owner = t.owner AND i.table_name = t.table_name) THEN 1 ELSE 0 END AS hasindexes, " +
                        "       CASE WHEN EXISTS (SELECT 1 FROM all_triggers tr WHERE tr.table_owner = t.owner AND tr.table_name = t.table_name) THEN 1 ELSE 0 END AS hastriggers, " +
                        "       CASE WHEN EXISTS (SELECT 1 FROM all_policies p WHERE p.object_owner = t.owner AND p.object_name = t.table_name) THEN 1 ELSE 0 END AS hasrowsecurity, " +
                        "       NVL(m.inserts, 0) AS n_tup_ins, " +
                        "       NVL(m.updates, 0) AS n_tup_upd, " +
                        "       NVL(m.deletes, 0) AS n_tup_del " +
                        "FROM all_tables t " +
                        "JOIN all_users u ON u.username = t.owner AND u.oracle_maintained = 'N' " +
                        "LEFT OUTER JOIN all_tab_modifications m ON m.table_owner = t.owner AND m.table_name = t.table_name";

        /*
         * Plain views.
         */
        final String viewsSQLCommand =
                "SELECT v.owner AS schemaname, v.view_name AS viewname, v.owner AS viewowner, v.text AS definition " +
                        "FROM all_views v " +
                        "JOIN all_users u ON u.username = v.owner AND u.oracle_maintained = 'N'";

        /*
         * Materialized views - Oracle's direct equivalent of PostgreSQL materialized views.
         */
        final String materializedViewsSQLCommand =
                "SELECT m.owner AS schemaname, " +
                        "       m.mview_name AS viewname, " +
                        "       m.owner AS viewowner, " +
                        "       m.query AS definition, " +
                        "       CASE WHEN EXISTS (SELECT 1 FROM all_indexes i WHERE i.table_owner = m.owner AND i.table_name = m.mview_name) THEN 1 ELSE 0 END AS hasindexes, " +
                        "       CASE WHEN m.last_refresh_date IS NOT NULL THEN 1 ELSE 0 END AS ispopulated " +
                        "FROM all_mviews m " +
                        "JOIN all_users u ON u.username = m.owner AND u.oracle_maintained = 'N'";

        try
        {
            DatabaseDetails databaseDetails = databaseResults.get(databaseName);

            if (databaseDetails != null)
            {
                /*
                 * Extract key stats about each column in each database.  This builds up the basic structure of
                 * schemas, tables and columns and enables the calculation of the total number of schemas, tables
                 * and columns to be determined.  The declared average column width (avg_col_len) is also used
                 * directly as the column's size measurement - like Microsoft SQL Server, Oracle has no way to
                 * estimate the actual stored size of a column without scanning data beyond this auto-collected
                 * statistic, so avg_col_len is used as a metadata-only approximation.
                 */
                PreparedStatement preparedStatement = databaseSQLConnection.prepareStatement(columnStatsSQLCommand);

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next())
                {
                    String schemaName = resultSet.getString("schemaname");

                    if (schemaName != null)
                    {
                        String  tableName              = resultSet.getString("tablename");
                        String  columnName             = resultSet.getString("columnname");
                        int     averageColumnWidth     = resultSet.getInt("avgwidth");
                        long    numberOfDistinctValues = resultSet.getLong("ndistinct");
                        String  mostCommonValue        = resultSet.getString("mostcommonvalue");
                        String  mostCommonFrequency     = resultSet.getString("mostcommonfreq");
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

                    if (schemaName != null)
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

                    if (schemaName != null)
                    {
                        String viewName   = resultSet.getString("viewname");
                        String viewOwner  = resultSet.getString("viewowner");
                        String definition = resultSet.getString("definition");

                        SchemaDetails schemaDetails = databaseDetails.getSchemaDetails(databaseName, schemaName);
                        TableDetails  tableDetails  = schemaDetails.getTableDetails(schemaName, viewName);

                        tableDetails.setViewDetails(viewOwner, definition);
                    }
                }

                resultSet.close();
                preparedStatement.close();

                /*
                 * Extract stats about the materialized views.
                 */
                preparedStatement = databaseSQLConnection.prepareStatement(materializedViewsSQLCommand);
                resultSet         = preparedStatement.executeQuery();

                while (resultSet.next())
                {
                    String schemaName = resultSet.getString("schemaname");

                    if (schemaName != null)
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
                        SchemaDetails currentSchema = databaseDetails.getSchemaDetails(databaseName, schemaName);

                        if (currentSchema != null)
                        {
                            List<String> tableNames = currentSchema.getTableNames();

                            if (tableNames != null)
                            {
                                long schemaSize = 0L;

                                for (String tableName : tableNames)
                                {
                                    TableDetails currentTable = currentSchema.getTableDetails(schemaName, tableName);

                                    if (currentTable != null)
                                    {
                                        this.getTableSize(databaseSQLConnection, schemaName, currentTable);
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

                        annotations.add(schemaAnnotation);


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
     * Retrieve information about a table size using dba_segments - the Oracle equivalent of Microsoft
     * SQL Server's sys.dm_db_partition_stats / PostgreSQL's pg_table_size() function.  Table and table
     * partition segments are summed to cover both partitioned and non-partitioned tables.
     * <br><br>
     * Unlike ALL_TABLES/ALL_TAB_COLUMNS etc., there is no ALL_SEGMENTS view in Oracle - only USER_SEGMENTS
     * (the connected user's own segments) and DBA_SEGMENTS (every segment, requiring SELECT_CATALOG_ROLE or
     * DBA privilege).  Since this connector already needs schema-spanning visibility for other catalog
     * views such as ALL_TAB_MODIFICATIONS and ALL_POLICIES, DBA_SEGMENTS is used here rather than narrowing
     * to just the connecting user's own segments.
     *
     * @param databaseSQLConnection connection to the appropriate database
     * @param schemaOwner the raw (non-database-qualified) Oracle schema/owner name
     * @param tableDetails collected information about a table
     * @throws SQLException errors accessing the database
     */
    void getTableSize(java.sql.Connection databaseSQLConnection,
                      String              schemaOwner,
                      TableDetails        tableDetails) throws SQLException
    {
        final String tableSizeSQLCommand =
                "SELECT SUM(bytes) AS tablesize " +
                        "FROM dba_segments " +
                        "WHERE owner = ? AND segment_name = ? AND segment_type IN ('TABLE','TABLE PARTITION')";

        if (! tableDetails.columns.isEmpty())
        {
            try
            {
                PreparedStatement preparedStatement = databaseSQLConnection.prepareStatement(tableSizeSQLCommand);

                preparedStatement.setString(1, schemaOwner);
                preparedStatement.setString(2, tableDetails.getTableName());

                ResultSet resultSet = preparedStatement.executeQuery();

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
         * Retrieve the raw (non-database-qualified) table name, as needed for Oracle data dictionary lookups
         * such as dba_segments that key on owner/segment_name rather than a qualified name.
         *
         * @return string
         */
        public String getTableName()
        {
            return tableMeasurement.getTableName();
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
