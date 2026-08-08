/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.db2luw.survey;

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
 * DB2LUWDatabaseStatsExtractor gathers the same measurements as the PostgreSQL equivalent
 * (org.odpi.openmetadata.adapters.connectors.postgres.survey.PostgresDatabaseStatsExtractor) but sources
 * them from Db2 for Linux, UNIX and Windows' system catalog views (SYSCAT.SCHEMATA, SYSCAT.TABLES,
 * SYSCAT.VIEWS, SYSCAT.COLUMNS, SYSCAT.COLDIST, SYSCAT.INDEXES, SYSCAT.TRIGGERS, SYSIBMADM.ADMINTABINFO,
 * SYSIBMADM.MON_TAB_STATS) instead of pg_catalog.  All of these sources are metadata/statistics only - no
 * user data is read.
 * <br><br>
 * Unlike Oracle (whose CDB root can see every pluggable database's size through a single cross-container
 * connection before looping per-PDB for schema detail), Db2 for Linux, UNIX and Windows has no catalog view
 * that spans multiple databases - every one of the views above is already scoped to whichever single
 * database the connection is attached to.  Consequently getDatabaseStatistics() here is called once per
 * database, on a connection to that specific database, rather than once up front on a shared connection.
 */
public class DB2LUWDatabaseStatsExtractor
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
    public DB2LUWDatabaseStatsExtractor(List<String>                validDatabases,
                                        SurveyActionServiceConnector surveyActionServiceConnector)
    {
        this.validDatabases               = validDatabases;
        this.surveyActionServiceConnector = surveyActionServiceConnector;
    }


    /**
     * Retrieve statistics about a single database.  Called on a connection to that specific database - Db2
     * for Linux, UNIX and Windows has no cross-database catalog view, so unlike Oracle's CDB root approach,
     * this cannot be gathered once for every requested database in a single call.
     * <br><br>
     * There is no direct catalog view giving total database size without the SYSPROC.GET_DBSIZE_INFO stored
     * procedure (which requires a CALL statement with OUT parameters rather than a plain query), so the
     * database size reported here is the sum of SYSIBMADM.ADMINTABINFO's per-table sizes - a metadata-only
     * approximation, consistent with how table size is calculated in getTableSize() below.
     *
     * @param databaseName name of the database this connection is attached to
     * @param databaseConnection connection to the named database
     * @throws SQLException            problem accessing the database
     * @throws PropertyServerException problem create JSON properties
     */
    void getDatabaseStatistics(String               databaseName,
                               java.sql.Connection   databaseConnection) throws SQLException, PropertyServerException
    {
        final String databaseSizeSQLCommand =
                "SELECT SUM(DATA_OBJECT_P_SIZE + INDEX_OBJECT_P_SIZE + LONG_OBJECT_P_SIZE + LOB_OBJECT_P_SIZE) AS databasesize " +
                        "FROM SYSIBMADM.ADMINTABINFO";

        try
        {
            RelationalDataManagerMeasurement databaseMeasurement = new RelationalDataManagerMeasurement();

            databaseMeasurement.setResourceName(databaseName);

            DatabaseDetails databaseDetails = new DatabaseDetails(databaseMeasurement);

            databaseResults.put(databaseName, databaseDetails);

            PreparedStatement preparedStatement = databaseConnection.prepareStatement(databaseSizeSQLCommand);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                /*
                 * ADMINTABINFO reports sizes in kilobytes.
                 */
                databaseDetails.setSize(resultSet.getLong("databasesize") * 1024L);
            }

            resultSet.close();
            preparedStatement.close();
        }
        catch (SQLException sqlException)
        {
            try
            {
                databaseConnection.rollback();
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
     *
     * @param databaseName          name of the database that is connected to
     * @param databaseSQLConnection connection to the named database
     * @throws SQLException            problem accessing the database
     */
    void getSchemaStatistics(String databaseName,
                             java.sql.Connection databaseSQLConnection) throws SQLException
    {
        /*
         * One row per column, giving its declared width/type/nullability plus a best-effort distinct-value
         * estimate and single most-common-value/frequency, both sourced from Db2's own auto-collected column
         * statistics (SYSSTAT.COLUMNS, SYSSTAT.COLDIST) - an already-collected estimate, just like PostgreSQL's
         * pg_stats (populated by ANALYZE / RUNSTATS), so no user data is scanned to build this annotation.
         * Unlike Oracle, Db2's COLDIST view already stores the single most-frequent value directly at SEQNO=1
         * for TYPE='F' (frequent-value) rows, so no window-function derivation is needed here.
         */
        final String columnStatsSQLCommand =
                "SELECT c.TABSCHEMA AS schemaname, " +
                        "       c.TABNAME AS tablename, " +
                        "       c.COLNAME AS columnname, " +
                        "       c.LENGTH AS avgwidth, " +
                        "       c.TYPENAME AS typename, " +
                        "       c.NULLS AS isnullable, " +
                        "       s.COLCARD AS ndistinct, " +
                        "       d.COLVALUE AS mostcommonvalue, " +
                        "       d.VALCOUNT AS mostcommonfreq " +
                        "FROM SYSCAT.COLUMNS c " +
                        "JOIN SYSCAT.SCHEMATA u ON u.SCHEMANAME = c.TABSCHEMA AND u.SCHEMANAME NOT LIKE 'SYS%' AND u.SCHEMANAME NOT IN ('NULLID', 'SQLJ', 'DB2GSE') " +
                        "LEFT OUTER JOIN SYSSTAT.COLUMNS s ON s.TABSCHEMA = c.TABSCHEMA AND s.TABNAME = c.TABNAME AND s.COLNAME = c.COLNAME " +
                        "LEFT OUTER JOIN SYSCAT.COLDIST d ON d.TABSCHEMA = c.TABSCHEMA AND d.TABNAME = c.TABNAME AND d.COLNAME = c.COLNAME AND d.TYPE = 'F' AND d.SEQNO = 1";

        /*
         * One row per table, with insert/update/delete counts sourced from SYSIBMADM.MON_TAB_STATS - the
         * direct Db2 analog of Oracle's all_tab_modifications / PostgreSQL's pg_stat_user_tables.n_tup_ins
         * etc.  hasRowSecurity is read directly from SYSCAT.TABLES.ROWSECURITY, which reflects whether Db2's
         * Row and Column Access Control (RCAC) is enabled on the table.
         */
        final String tablesSQLCommand =
                "SELECT t.TABSCHEMA AS schemaname, " +
                        "       t.TABNAME AS tablename, " +
                        "       t.OWNER AS tableowner, " +
                        "       CASE WHEN EXISTS (SELECT 1 FROM SYSCAT.INDEXES i WHERE i.TABSCHEMA = t.TABSCHEMA AND i.TABNAME = t.TABNAME) THEN 1 ELSE 0 END AS hasindexes, " +
                        "       CASE WHEN EXISTS (SELECT 1 FROM SYSCAT.TRIGGERS g WHERE g.TABSCHEMA = t.TABSCHEMA AND g.TABNAME = t.TABNAME) THEN 1 ELSE 0 END AS hastriggers, " +
                        "       CASE WHEN t.ROWSECURITY = 'Y' THEN 1 ELSE 0 END AS hasrowsecurity, " +
                        "       COALESCE(m.ROWS_INSERTED, 0) AS n_tup_ins, " +
                        "       COALESCE(m.ROWS_UPDATED, 0) AS n_tup_upd, " +
                        "       COALESCE(m.ROWS_DELETED, 0) AS n_tup_del " +
                        "FROM SYSCAT.TABLES t " +
                        "JOIN SYSCAT.SCHEMATA u ON u.SCHEMANAME = t.TABSCHEMA AND u.SCHEMANAME NOT LIKE 'SYS%' AND u.SCHEMANAME NOT IN ('NULLID', 'SQLJ', 'DB2GSE') " +
                        "LEFT OUTER JOIN SYSIBMADM.MON_TAB_STATS m ON m.TABSCHEMA = t.TABSCHEMA AND m.TABNAME = t.TABNAME " +
                        "WHERE t.TYPE = 'T'";

        /*
         * Plain views.
         */
        final String viewsSQLCommand =
                "SELECT v.VIEWSCHEMA AS schemaname, v.VIEWNAME AS viewname, v.DEFINER AS viewowner, v.TEXT AS definition " +
                        "FROM SYSCAT.VIEWS v " +
                        "JOIN SYSCAT.SCHEMATA u ON u.SCHEMANAME = v.VIEWSCHEMA AND u.SCHEMANAME NOT LIKE 'SYS%' AND u.SCHEMANAME NOT IN ('NULLID', 'SQLJ', 'DB2GSE') " +
                        "JOIN SYSCAT.TABLES t ON t.TABSCHEMA = v.VIEWSCHEMA AND t.TABNAME = v.VIEWNAME AND t.TYPE = 'V'";

        /*
         * Materialized Query Tables (MQTs) - Db2's direct equivalent of PostgreSQL materialized views.  An
         * MQT is registered in SYSCAT.TABLES with TYPE='S' (summary table) and also has a corresponding row
         * in SYSCAT.VIEWS carrying its defining query text.
         */
        final String materializedViewsSQLCommand =
                "SELECT t.TABSCHEMA AS schemaname, " +
                        "       t.TABNAME AS viewname, " +
                        "       t.OWNER AS viewowner, " +
                        "       v.TEXT AS definition, " +
                        "       CASE WHEN EXISTS (SELECT 1 FROM SYSCAT.INDEXES i WHERE i.TABSCHEMA = t.TABSCHEMA AND i.TABNAME = t.TABNAME) THEN 1 ELSE 0 END AS hasindexes, " +
                        "       CASE WHEN t.CARD >= 0 THEN 1 ELSE 0 END AS ispopulated " +
                        "FROM SYSCAT.TABLES t " +
                        "JOIN SYSCAT.SCHEMATA u ON u.SCHEMANAME = t.TABSCHEMA AND u.SCHEMANAME NOT LIKE 'SYS%' AND u.SCHEMANAME NOT IN ('NULLID', 'SQLJ', 'DB2GSE') " +
                        "LEFT OUTER JOIN SYSCAT.VIEWS v ON v.VIEWSCHEMA = t.TABSCHEMA AND v.VIEWNAME = t.TABNAME " +
                        "WHERE t.TYPE = 'S'";

        try
        {
            DatabaseDetails databaseDetails = databaseResults.get(databaseName);

            if (databaseDetails != null)
            {
                /*
                 * Extract key stats about each column in each database.  This builds up the basic structure of
                 * schemas, tables and columns and enables the calculation of the total number of schemas, tables
                 * and columns to be determined.  The declared width (LENGTH) is also used directly as the
                 * column's size measurement - like Microsoft SQL Server and Oracle, Db2 has no way to estimate
                 * the actual stored size of a column without scanning data beyond this auto-collected statistic,
                 * so LENGTH is used as a metadata-only approximation.
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
                         * Db2 for Linux, UNIX and Windows has no equivalent of PostgreSQL's CREATE RULE feature,
                         * so hasRules is always reported as false.
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
                 * Extract stats about the materialized query tables.
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
     * Retrieve information about a table size using SYSIBMADM.ADMINTABINFO - the Db2 for Linux, UNIX and
     * Windows equivalent of Microsoft SQL Server's sys.dm_db_partition_stats / PostgreSQL's pg_table_size()
     * function.  Data, index and LOB object sizes are summed to give the table's total footprint.
     *
     * @param databaseSQLConnection connection to the appropriate database
     * @param schemaOwner the raw (non-database-qualified) Db2 schema name
     * @param tableDetails collected information about a table
     * @throws SQLException errors accessing the database
     */
    void getTableSize(java.sql.Connection databaseSQLConnection,
                      String              schemaOwner,
                      TableDetails        tableDetails) throws SQLException
    {
        final String tableSizeSQLCommand =
                "SELECT (DATA_OBJECT_P_SIZE + INDEX_OBJECT_P_SIZE + LONG_OBJECT_P_SIZE + LOB_OBJECT_P_SIZE) AS tablesize " +
                        "FROM SYSIBMADM.ADMINTABINFO " +
                        "WHERE TABSCHEMA = ? AND TABNAME = ?";

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
                    /*
                     * ADMINTABINFO reports sizes in kilobytes.
                     */
                    tableDetails.setTableSize(resultSet.getLong("tablesize") * 1024L);
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
         * @param databaseName name of the database
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
         * @param databaseName name of the database
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
         * @param schemaOwner the raw (non-database-qualified) Db2 schema name
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
         * @param hasRules does the table have rules attached (always false - Db2 for Linux, UNIX and Windows has no rules)
         * @param hasTriggers does the table have triggers attached
         * @param hasRowSecurity is Row and Column Access Control (RCAC) row-level security set on
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
         * Retrieve the raw (non-database-qualified) table name, as needed for Db2 data dictionary lookups
         * such as SYSIBMADM.ADMINTABINFO that key on TABSCHEMA/TABNAME rather than a qualified name.
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
         * Set the measurements gathered for this column.  The declared width (LENGTH) is used directly as
         * the column size, since Db2 has no metadata-only way to estimate the actual stored size of a
         * column's values beyond this declared statistic.
         *
         * @param averageColumnWidth declared width in bytes
         * @param numberOfDistinctValues estimate from the column's auto-collected statistics (SYSSTAT.COLUMNS.COLCARD)
         * @param mostCommonValue single most common value found in the column's frequency distribution, if any
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
