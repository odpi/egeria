/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.db2luw.survey;

import org.odpi.openmetadata.adapters.connectors.db2luw.ffdc.DB2LUWAuditCode;
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
 * DB2LUWDatabaseStatsExtractor gathers the same measurements as the PostgreSQL equivalent
 * (org.odpi.openmetadata.adapters.connectors.postgres.survey.PostgresDatabaseStatsExtractor) but sources
 * them from Db2 for Linux, UNIX and Windows' system catalog views (SYSCAT.SCHEMATA, SYSCAT.TABLES,
 * SYSCAT.VIEWS, SYSCAT.COLUMNS, SYSCAT.COLDIST, SYSCAT.INDEXES, SYSCAT.TRIGGERS, SYSSTAT.COLUMNS), the
 * SYSIBMADM.ADMINTABINFO administrative view and the SYSPROC.ADMIN_GET_TAB_INFO and MON_GET_TABLE table
 * functions instead of pg_catalog.  All of these sources are metadata/statistics only - no user data is read.
 * <br><br>
 * Unlike Oracle (whose CDB root can see every pluggable database's size through a single cross-container
 * connection before looping per-PDB for schema detail), Db2 for Linux, UNIX and Windows has no catalog view
 * that spans multiple databases - every one of the views above is already scoped to whichever single
 * database the connection is attached to.  Consequently getDatabaseStatistics() here is called once per
 * database, on a connection to that specific database, rather than once up front on a shared connection.
 * <br><br>
 * The connections passed to it are expected to be in auto-commit mode, so that every query is its own short
 * transaction.  Each schema is surveyed with its own set of queries, and a query that fails is logged and
 * skipped, so one inaccessible schema, a missing privilege for the administrative views and monitoring
 * functions (a common situation, since they need more than the CONNECT privilege), or a dropped table leaves
 * a gap in the results rather than ending the survey.
 */
public class DB2LUWDatabaseStatsExtractor
{
    private final List<String>                 validDatabases;
    private final List<String>                 excludedSchemas;
    private final List<String>                 includedSchemas;
    private final SurveyActionServiceConnector surveyActionServiceConnector;
    private final AuditLog                     auditLog;
    private final String                       surveyActionServiceName;
    private final Map<String, DatabaseDetails> databaseResults = new HashMap<>();


    /**
     * Convert Db2's raw SYSSTAT.COLUMNS.COLCARD catalog value into an actual number-of-distinct-values
     * estimate.  Per Db2's own documentation, COLCARD is a non-negative distinct-value count once RUNSTATS
     * has been run on the table, but is exactly -1 if statistics have not been gathered for the column -
     * a documented "unknown" sentinel, not a real (negative) count.  Reading that raw catalog value
     * directly into a "number of distinct values" measurement, as this method's caller previously did,
     * reports a nonsensical -1 for any un-RUNSTATS column - the same class of bug already fixed for
     * PostgreSQL's equivalent pg_stats.n_distinct sentinel (see PostgresDatabaseStatsExtractor).
     *
     * @param rawColumnCardinality the raw value of SYSSTAT.COLUMNS.COLCARD for the column
     * @return the distinct-value count, or 0 if statistics have not been gathered for the column
     */
    static long calculateNumberOfDistinctValues(long rawColumnCardinality)
    {
        if (rawColumnCardinality < 0)
        {
            return 0L;
        }

        return rawColumnCardinality;
    }


    /**
     * Constructor sets up the list of databases to process and the connection to the database.  Every schema
     * in those databases is surveyed.
     *
     * @param validDatabases               list of database names
     * @param surveyActionServiceConnector calling connector
     */
    public DB2LUWDatabaseStatsExtractor(List<String>                validDatabases,
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
    public DB2LUWDatabaseStatsExtractor(List<String>                validDatabases,
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
     * Determine whether a schema should be surveyed.  Db2's own schemas (SYS*, NULLID, SQLJ and DB2GSE - the
     * same set that the catalog queries filter out) never are.  Otherwise the include list, if there is one,
     * decides; failing that, the exclude list does.  This follows SurveyContext.elementShouldBeSurveyed, which
     * applies the same rule to database names.
     *
     * @param schemaName name of the schema
     * @return flag indicating whether to gather statistics for the schema
     */
    boolean schemaShouldBeSurveyed(String schemaName)
    {
        if ((schemaName == null) || (schemaName.startsWith("SYS")) ||
            (schemaName.equals("NULLID")) || (schemaName.equals("SQLJ")) || (schemaName.equals("DB2GSE")))
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
     * Retrieve statistics about a single database.  Called on a connection to that specific database - Db2
     * for Linux, UNIX and Windows has no cross-database catalog view, so unlike Oracle's CDB root approach,
     * this cannot be gathered once for every requested database in a single call.
     * <br><br>
     * The database gets an entry in the results before any query is issued, so that if its statistics
     * cannot be read, its schemas are still surveyed.
     * <br><br>
     * There is no direct catalog view giving total database size without the SYSPROC.GET_DBSIZE_INFO stored
     * procedure (which requires a CALL statement with OUT parameters rather than a plain query), so the
     * database size reported here is the sum of SYSIBMADM.ADMINTABINFO's per-table sizes - a metadata-only
     * approximation, consistent with how table size is calculated in getSchemaStatistics() below.
     *
     * @param databaseName name of the database this connection is attached to
     * @param databaseConnection connection to the named database
     */
    void getDatabaseStatistics(String              databaseName,
                               java.sql.Connection databaseConnection)
    {
        final String databaseSizeSQLCommand =
                "SELECT SUM(DATA_OBJECT_P_SIZE + INDEX_OBJECT_P_SIZE + LONG_OBJECT_P_SIZE + LOB_OBJECT_P_SIZE) AS databasesize " +
                        "FROM SYSIBMADM.ADMINTABINFO";

        RelationalDataManagerMeasurement databaseMeasurement = new RelationalDataManagerMeasurement();

        databaseMeasurement.setResourceName(databaseName);

        DatabaseDetails databaseDetails = new DatabaseDetails(databaseMeasurement);

        databaseResults.put(databaseName, databaseDetails);

        /*
         * SYSIBMADM.ADMINTABINFO needs more than the CONNECT privilege, so this query commonly fails.
         */
        try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(databaseSizeSQLCommand);
             ResultSet         resultSet         = preparedStatement.executeQuery())
        {
            if (resultSet.next())
            {
                /*
                 * ADMINTABINFO reports sizes in kilobytes.
                 */
                databaseDetails.setSize(resultSet.getLong("databasesize") * 1024L);
            }
        }
        catch (SQLException sqlException)
        {
            logQueryFailure("database size", "database " + databaseName, sqlException);
        }
    }


    /**
     * Extract detailed information about the schemas, tables and columns in a database.
     * <br><br>
     * Each schema is surveyed with its own queries, so a failure is confined to the schema (or the
     * part of the schema) where it occurred.
     *
     * @param databaseName          name of the database that is connected to
     * @param databaseSQLConnection connection to the named database
     * @throws SQLException the schemas in the database could not be listed
     */
    void getSchemaStatistics(String              databaseName,
                             java.sql.Connection databaseSQLConnection) throws SQLException
    {
        final String schemataSQLCommand =
                "SELECT SCHEMANAME FROM SYSCAT.SCHEMATA " +
                        "WHERE SCHEMANAME NOT LIKE 'SYS%' AND SCHEMANAME NOT IN ('NULLID', 'SQLJ', 'DB2GSE') " +
                        "ORDER BY SCHEMANAME";

        DatabaseDetails databaseDetails = databaseResults.get(databaseName);

        if (databaseDetails != null)
        {
            List<String> schemaNames = new ArrayList<>();

            try (PreparedStatement preparedStatement = databaseSQLConnection.prepareStatement(schemataSQLCommand);
                 ResultSet         resultSet         = preparedStatement.executeQuery())
            {
                while (resultSet.next())
                {
                    String schemaName = resultSet.getString("SCHEMANAME");

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

                this.getSchemaStatistics(databaseName, databaseDetails, schemaName, databaseSQLConnection);
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
     * @param databaseName          name of the database that is connected to
     * @param databaseDetails       results for the database that is connected to
     * @param schemaName            name of the schema to survey
     * @param databaseSQLConnection connection to the database
     */
    private void getSchemaStatistics(String              databaseName,
                                     DatabaseDetails     databaseDetails,
                                     String              schemaName,
                                     java.sql.Connection databaseSQLConnection)
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
                "SELECT c.TABNAME AS tablename, " +
                        "       c.COLNAME AS columnname, " +
                        "       c.LENGTH AS avgwidth, " +
                        "       c.TYPENAME AS typename, " +
                        "       c.NULLS AS isnullable, " +
                        "       s.COLCARD AS ndistinct, " +
                        "       d.COLVALUE AS mostcommonvalue, " +
                        "       d.VALCOUNT AS mostcommonfreq " +
                        "FROM SYSCAT.COLUMNS c " +
                        "LEFT OUTER JOIN SYSSTAT.COLUMNS s ON s.TABSCHEMA = c.TABSCHEMA AND s.TABNAME = c.TABNAME AND s.COLNAME = c.COLNAME " +
                        "LEFT OUTER JOIN SYSCAT.COLDIST d ON d.TABSCHEMA = c.TABSCHEMA AND d.TABNAME = c.TABNAME AND d.COLNAME = c.COLNAME AND d.TYPE = 'F' AND d.SEQNO = 1 " +
                        "WHERE c.TABSCHEMA = ?";

        /*
         * One row per table.  hasRowSecurity is read directly from SYSCAT.TABLES.ROWSECURITY, which reflects
         * whether Db2's Row and Column Access Control (RCAC) is enabled on the table.  This query only uses the
         * catalog views, which every user that can connect is able to read, so the tables are recorded even if
         * the monitoring and size queries below fail for lack of privileges.
         */
        final String tablesSQLCommand =
                "SELECT t.TABNAME AS tablename, " +
                        "       t.OWNER AS tableowner, " +
                        "       CASE WHEN EXISTS (SELECT 1 FROM SYSCAT.INDEXES i WHERE i.TABSCHEMA = t.TABSCHEMA AND i.TABNAME = t.TABNAME) THEN 1 ELSE 0 END AS hasindexes, " +
                        "       CASE WHEN EXISTS (SELECT 1 FROM SYSCAT.TRIGGERS g WHERE g.TABSCHEMA = t.TABSCHEMA AND g.TABNAME = t.TABNAME) THEN 1 ELSE 0 END AS hastriggers, " +
                        "       CASE WHEN t.ROWSECURITY = 'Y' THEN 1 ELSE 0 END AS hasrowsecurity " +
                        "FROM SYSCAT.TABLES t " +
                        "WHERE t.TYPE = 'T' AND t.TABSCHEMA = ?";

        /*
         * Insert/update/delete counts for each table, from the MON_GET_TABLE monitoring table function - the
         * direct Db2 analog of Oracle's all_tab_modifications / PostgreSQL's pg_stat_user_tables.n_tup_ins
         * etc.  MON_GET_TABLE returns a row per member (and per data partition), so the counts are summed for
         * each table.  The -2 asks for the counts from all members.
         */
        final String tableActivitySQLCommand =
                "SELECT m.TABNAME AS tablename, " +
                        "       SUM(m.ROWS_INSERTED) AS n_tup_ins, " +
                        "       SUM(m.ROWS_UPDATED) AS n_tup_upd, " +
                        "       SUM(m.ROWS_DELETED) AS n_tup_del " +
                        "FROM TABLE(MON_GET_TABLE(CAST(? AS VARCHAR(128)), CAST(NULL AS VARCHAR(128)), -2)) AS m " +
                        "GROUP BY m.TABNAME";

        /*
         * The size of each table in the schema, from the SYSPROC.ADMIN_GET_TAB_INFO table function that
         * underlies SYSIBMADM.ADMINTABINFO - the Db2 for Linux, UNIX and Windows equivalent of Microsoft SQL
         * Server's sys.dm_db_partition_stats / PostgreSQL's pg_table_size() function.  Data, index and LOB
         * object sizes are summed to give the table's total footprint.  Passing the schema name to the table
         * function, rather than filtering the administrative view, means that only this schema's tables are
         * examined.  A partitioned table returns a row per partition, so the sizes are summed for each table.
         */
        final String tableSizeSQLCommand =
                "SELECT a.TABNAME AS tablename, " +
                        "       SUM(a.DATA_OBJECT_P_SIZE + a.INDEX_OBJECT_P_SIZE + a.LONG_OBJECT_P_SIZE + a.LOB_OBJECT_P_SIZE) AS tablesize " +
                        "FROM TABLE(SYSPROC.ADMIN_GET_TAB_INFO(CAST(? AS VARCHAR(128)), CAST(NULL AS VARCHAR(128)))) AS a " +
                        "GROUP BY a.TABNAME";

        /*
         * Plain views.
         */
        final String viewsSQLCommand =
                "SELECT v.VIEWNAME AS viewname, v.DEFINER AS viewowner, v.TEXT AS definition " +
                        "FROM SYSCAT.VIEWS v " +
                        "JOIN SYSCAT.TABLES t ON t.TABSCHEMA = v.VIEWSCHEMA AND t.TABNAME = v.VIEWNAME AND t.TYPE = 'V' " +
                        "WHERE v.VIEWSCHEMA = ?";

        /*
         * Materialized Query Tables (MQTs) - Db2's direct equivalent of PostgreSQL materialized views.  An
         * MQT is registered in SYSCAT.TABLES with TYPE='S' (summary table) and also has a corresponding row
         * in SYSCAT.VIEWS carrying its defining query text.
         */
        final String materializedViewsSQLCommand =
                "SELECT t.TABNAME AS viewname, " +
                        "       t.OWNER AS viewowner, " +
                        "       v.TEXT AS definition, " +
                        "       CASE WHEN EXISTS (SELECT 1 FROM SYSCAT.INDEXES i WHERE i.TABSCHEMA = t.TABSCHEMA AND i.TABNAME = t.TABNAME) THEN 1 ELSE 0 END AS hasindexes, " +
                        "       CASE WHEN t.CARD >= 0 THEN 1 ELSE 0 END AS ispopulated " +
                        "FROM SYSCAT.TABLES t " +
                        "LEFT OUTER JOIN SYSCAT.VIEWS v ON v.VIEWSCHEMA = t.TABSCHEMA AND v.VIEWNAME = t.TABNAME " +
                        "WHERE t.TYPE = 'S' AND t.TABSCHEMA = ?";

        final String qualifiedSchemaName = databaseName + "." + schemaName;

        /*
         * Extract key stats about each column.  This builds up the basic structure of tables and columns and
         * enables the calculation of the total number of tables and columns to be determined.  The declared
         * width (LENGTH) is also used directly as the column's size measurement - like Microsoft SQL Server and
         * Oracle, Db2 has no way to estimate the actual stored size of a column without scanning data beyond
         * this auto-collected statistic, so LENGTH is used as a metadata-only approximation.
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
                    long    numberOfDistinctValues = calculateNumberOfDistinctValues(resultSet.getLong("ndistinct"));
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
                    String  tableName      = resultSet.getString("tablename");
                    String  tableOwner     = resultSet.getString("tableowner");
                    boolean hasIndexes     = resultSet.getBoolean("hasindexes");
                    boolean hasTriggers    = resultSet.getBoolean("hastriggers");
                    boolean hasRowSecurity = resultSet.getBoolean("hasrowsecurity");

                    SchemaDetails schemaDetails = databaseDetails.getSchemaDetails(databaseName, schemaName);
                    TableDetails  tableDetails  = schemaDetails.getTableDetails(schemaName, tableName);

                    /*
                     * Db2 for Linux, UNIX and Windows has no equivalent of PostgreSQL's CREATE RULE feature,
                     * so hasRules is always reported as false.  The row counts are added from the monitoring
                     * query below.
                     */
                    tableDetails.setTableMeasurements(tableOwner,
                                                      hasIndexes,
                                                      false,
                                                      hasTriggers,
                                                      hasRowSecurity,
                                                      0L,
                                                      0L,
                                                      0L);
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
         * Extract details of the materialized query tables.
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
            logQueryFailure("materialized query table definitions", "schema " + qualifiedSchemaName, sqlException);
        }

        SchemaDetails currentSchema = databaseDetails.schemas.get(schemaName);

        if (currentSchema != null)
        {
            /*
             * Add the insert/update/delete counts to the tables already found.  MON_GET_TABLE needs a
             * monitoring privilege, so this query commonly fails; the tables are still reported, without the
             * counts.  Rows for tables that the queries above did not find are ignored.
             */
            try (PreparedStatement preparedStatement = databaseSQLConnection.prepareStatement(tableActivitySQLCommand))
            {
                preparedStatement.setString(1, schemaName);

                try (ResultSet resultSet = preparedStatement.executeQuery())
                {
                    while (resultSet.next())
                    {
                        TableDetails currentTable = currentSchema.tables.get(resultSet.getString("tablename"));

                        if (currentTable != null)
                        {
                            currentTable.setRowCounts(resultSet.getLong("n_tup_ins"),
                                                      resultSet.getLong("n_tup_upd"),
                                                      resultSet.getLong("n_tup_del"));
                        }
                    }
                }
            }
            catch (SQLException sqlException)
            {
                logQueryFailure("table activity statistics", "schema " + qualifiedSchemaName, sqlException);
            }

            /*
             * Add the sizes to the tables already found.  ADMIN_GET_TAB_INFO needs more than the CONNECT
             * privilege, so this query commonly fails; the tables are still reported, without their sizes.
             */
            try (PreparedStatement preparedStatement = databaseSQLConnection.prepareStatement(tableSizeSQLCommand))
            {
                preparedStatement.setString(1, schemaName);

                try (ResultSet resultSet = preparedStatement.executeQuery())
                {
                    while (resultSet.next())
                    {
                        TableDetails currentTable = currentSchema.tables.get(resultSet.getString("tablename"));

                        if (currentTable != null)
                        {
                            /*
                             * ADMIN_GET_TAB_INFO reports sizes in kilobytes.
                             */
                            currentTable.setTableSize(resultSet.getLong("tablesize") * 1024L);
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

        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                DB2LUWAuditCode.SURVEY_QUERY_FAILED.getMessageDefinition(surveyActionServiceName,
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
         * Set up the counts of the rows changed in the table.  These come from a monitoring query that is
         * separate from the catalog query that supplies the other table measurements, so that the table is still
         * reported if the userId is not permitted to read the monitoring data.
         *
         * @param numberOfRowsInserted how many rows have been inserted
         * @param numberOfRowsUpdated how many rows have been updated
         * @param numberOfRowsDeleted how many rows have been deleted
         */
        void setRowCounts(long numberOfRowsInserted,
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
