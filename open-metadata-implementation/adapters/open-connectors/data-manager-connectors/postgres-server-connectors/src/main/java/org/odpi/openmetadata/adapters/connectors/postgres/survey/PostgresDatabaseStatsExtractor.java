/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.survey;

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

public class PostgresDatabaseStatsExtractor
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
    public PostgresDatabaseStatsExtractor(List<String>                validDatabases,
                                          SurveyActionServiceConnector surveyActionServiceConnector)
    {
        this.validDatabases               = validDatabases;
        this.surveyActionServiceConnector = surveyActionServiceConnector;
    }


    /**
     * Retrieve statistics about each requested database.
     * This works if connected to either the postgres database or a user database.
     *
     * @param serverJDBCConnection connection to the postgres database
     * @throws SQLException            problem accessing the database
     * @throws PropertyServerException problem create JSON properties
     */
    void getDatabaseStatistics(java.sql.Connection serverJDBCConnection) throws SQLException, PropertyServerException
    {
        final String pg_stat_databaseSQLCommand = "SELECT datname, tup_fetched, tup_inserted, tup_updated, tup_deleted, session_time, active_time, stats_reset FROM pg_catalog.pg_stat_database;";

        try
        {
            /*
             * Extract key stats about each database
             */
            PreparedStatement preparedStatement = serverJDBCConnection.prepareStatement(pg_stat_databaseSQLCommand);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                String databaseName = resultSet.getString("datname");

                if (validDatabases.contains(databaseName))
                {
                    RelationalDataManagerMeasurement databaseMeasurement = new RelationalDataManagerMeasurement();

                    databaseMeasurement.setResourceName(databaseName);
                    databaseMeasurement.setRowsFetched(resultSet.getLong("tup_fetched"));
                    databaseMeasurement.setRowsInserted(resultSet.getLong("tup_inserted"));
                    databaseMeasurement.setRowsUpdated(resultSet.getLong("tup_updated"));
                    databaseMeasurement.setRowsDeleted(resultSet.getLong("tup_deleted"));
                    databaseMeasurement.setSessionTime(resultSet.getDouble("session_time"));
                    databaseMeasurement.setActiveTime(resultSet.getDouble("active_time"));
                    databaseMeasurement.setStatsReset(resultSet.getDate("stats_reset"));

                    databaseResults.put(databaseName, new DatabaseDetails(databaseMeasurement));
                }
            }

            resultSet.close();
            preparedStatement.close();

            /*
             * Extract the total size of each database
             */
            for (String databaseName : validDatabases)
            {
                DatabaseDetails currentDatabase = databaseResults.get(databaseName);

                if (currentDatabase != null)
                {
                    final String databaseSizeSQLCommand = "SELECT pg_database_size('" + databaseName + "');";

                    preparedStatement = serverJDBCConnection.prepareStatement(databaseSizeSQLCommand);

                    resultSet = preparedStatement.executeQuery();

                    if (resultSet.next())
                    {
                        currentDatabase.setSize(resultSet.getLong("pg_database_size"));
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
     * This only returns information from the connected database - so it needs to be connected
     * from a user database - rather than the postgres database.
     *
     * @param databaseName          name of database that is connected to
     * @param databaseSQLConnection connection to the named database
     * @throws SQLException            problem accessing the database
     */
    void getSchemaStatistics(String databaseName,
                             java.sql.Connection databaseSQLConnection) throws SQLException
    {
        final String pg_tablesSQLCommand      = "select pg_stat_user_tables.relid,pg_stat_user_tables.schemaname,pg_tables.tablename, pg_tables.tableowner, pg_tables.hasindexes, pg_tables.hasrules, pg_tables.hastriggers, pg_tables.rowsecurity, pg_stat_user_tables.n_tup_ins, pg_stat_user_tables.n_tup_upd, pg_stat_user_tables.n_tup_del from pg_catalog.pg_statio_user_tables left outer join pg_catalog.pg_stat_user_tables on pg_stat_user_tables.schemaname = pg_statio_user_tables.schemaname and pg_stat_user_tables.relname = pg_statio_user_tables.relname left outer join pg_catalog.pg_tables on pg_stat_user_tables.schemaname = pg_tables.schemaname and pg_stat_user_tables.relname = pg_tables.tablename where (pg_catalog.pg_statio_user_tables.schemaname != 'pg_catalog') and (pg_catalog.pg_statio_user_tables.schemaname != 'information_schema') ;";
        final String pg_columnStatsSQLCommand = "select pg_stats.schemaname, pg_stats.tablename, pg_statio_user_tables.relid, pg_stats.attname, pg_stats.avg_width, pg_stats.n_distinct, pg_stats.most_common_vals, pg_stats.most_common_freqs, pg_attribute.atttypid, pg_type.typname, pg_attribute.attnotnull from pg_catalog.pg_stats left outer join pg_catalog.pg_statio_user_tables on pg_stats.schemaname = pg_statio_user_tables.schemaname and pg_stats.tablename = pg_statio_user_tables.relname left outer join pg_catalog.pg_attribute on pg_statio_user_tables.relid = pg_attribute.attrelid and pg_stats.attname = pg_attribute.attname left outer join pg_type on pg_attribute.atttypid = pg_type.oid where (pg_stats.schemaname != 'pg_catalog') and (pg_stats.schemaname != 'information_schema') ;";
        final String pg_viewsSQLCommand       = "SELECT schemaname, viewname, viewowner, definition FROM pg_catalog.pg_views;";
        final String pg_matViewsSQLCommand    = "SELECT schemaname, matviewname, matviewowner, hasindexes, ispopulated, definition FROM pg_catalog.pg_matviews;";

        try
        {
            DatabaseDetails databaseDetails = databaseResults.get(databaseName);

            if (databaseDetails != null)
            {
                /*
                 * Extract key stats about each column in each database.  This builds up the basic structure of
                 * schemas, tables and columns and enables the calculation of the total number of schemas, tables
                 * and columns to be determined.
                 */
                PreparedStatement preparedStatement = databaseSQLConnection.prepareStatement(pg_columnStatsSQLCommand);

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next())
                {
                    String schemaName = resultSet.getString("schemaname");

                    if ((schemaName != null) && (!schemaName.equals("pg_catalog")) && (!schemaName.equals("information_schema")))
                    {
                        String  tableName              = resultSet.getString("tablename");
                        String  columnName             = resultSet.getString("attname");
                        int     averageColumnWidth     = resultSet.getInt("avg_width");
                        long    numberOfDistinctValues = resultSet.getLong("n_distinct");
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
                    }
                }

                resultSet.close();
                preparedStatement.close();

                /*
                 * Extract core information about the tables
                 */
                preparedStatement = databaseSQLConnection.prepareStatement(pg_tablesSQLCommand);
                resultSet         = preparedStatement.executeQuery();

                while (resultSet.next())
                {
                    String schemaName = resultSet.getString("schemaname");

                    if ((schemaName != null) && (!schemaName.equals("pg_catalog")) && (!schemaName.equals("information_schema")))
                    {
                        String  tableName            = resultSet.getString("tablename");
                        String  tableOwner           = resultSet.getString("tableowner");
                        boolean hasIndexes           = resultSet.getBoolean("hasindexes");
                        boolean hasRules             = resultSet.getBoolean("hasrules");
                        boolean hasTriggers          = resultSet.getBoolean("hastriggers");
                        boolean hasRowSecurity       = resultSet.getBoolean("rowsecurity");
                        long    numberOfRowsInserted = resultSet.getLong("n_tup_ins");
                        long    numberOfRowsUpdated  = resultSet.getLong("n_tup_upd");
                        long    numberOfRowsDeleted  = resultSet.getLong("n_tup_del");

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
                    }
                }

                resultSet.close();
                preparedStatement.close();


                /*
                 * Extract stats about the views
                 */
                preparedStatement = databaseSQLConnection.prepareStatement(pg_viewsSQLCommand);
                resultSet         = preparedStatement.executeQuery();

                while (resultSet.next())
                {
                    String schemaName = resultSet.getString("schemaname");

                    if ((schemaName != null) && (!schemaName.equals("pg_catalog")) && (!schemaName.equals("information_schema")))
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
                 * Extract stats about the views
                 */
                preparedStatement = databaseSQLConnection.prepareStatement(pg_matViewsSQLCommand);
                resultSet         = preparedStatement.executeQuery();

                while (resultSet.next())
                {
                    String schemaName = resultSet.getString("schemaname");

                    if ((schemaName != null) && (!schemaName.equals("pg_catalog")) && (!schemaName.equals("information_schema")))
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

                resultSet.close();
                preparedStatement.close();

                /*
                 * At this point we have details of each schema, table, view and column.
                 */
                List<String> schemaNames = databaseDetails.getSchemaNames();

                if (schemaNames != null)
                {
                    /*
                     * Extract the total size of each column, table and schema
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

                                        List<String> columnNames = currentTable.getColumnNames();

                                        if (columnNames != null)
                                        {
                                            for (String columnName : columnNames)
                                            {
                                                ColumnDetails currentColumn = currentTable.getColumnDetails(columnName);

                                                this.getColumnSize(databaseSQLConnection, currentColumn);
                                            }
                                        }
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
     * Retrieve information about a table size.
     *
     * @param databaseSQLConnection connection to the appropriate database
     * @param tableDetails collected information about a table
     * @throws SQLException errors accessing the database
     */
    void getTableSize(java.sql.Connection databaseSQLConnection,
                      TableDetails        tableDetails) throws SQLException
    {
        final String databaseSizeSQLCommand = "SELECT pg_table_size('" + tableDetails.getQualifiedTableName() + "');";

        if (! tableDetails.columns.isEmpty())
        {
            try
            {
                PreparedStatement preparedStatement = databaseSQLConnection.prepareStatement(databaseSizeSQLCommand);
                ResultSet         resultSet         = preparedStatement.executeQuery();

                if (resultSet.next())
                {
                    tableDetails.setTableSize(resultSet.getLong("pg_table_size"));
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
     * Retrieve information about a column size.
     *
     * @param databaseSQLConnection connection to the appropriate database
     * @param columnDetails collected information about a column
     * @throws SQLException errors accessing the database
     */
    void getColumnSize(java.sql.Connection databaseSQLConnection,
                       ColumnDetails       columnDetails) throws SQLException
    {
        final String databaseSizeSQLCommand = "SELECT pg_column_size('" + columnDetails.getQualifiedColumnName() + "');";

        try
        {
            PreparedStatement preparedStatement = databaseSQLConnection.prepareStatement(databaseSizeSQLCommand);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                columnDetails.setColumnSize(resultSet.getLong("pg_column_size"));
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
                databaseMeasurements.setViewCount(tableCount);
                databaseMeasurements.setMaterializedViewCount(tableCount);
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
                        columnCount = tableDetails.getColumnCount();
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
