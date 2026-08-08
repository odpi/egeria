/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.duckdb.survey;

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
 * DuckDBDatabaseStatsExtractor gathers statistics about the schemas, tables and columns of a DuckDB database
 * using DuckDB's own catalog metadata table functions (duckdb_schemas(), duckdb_tables() and duckdb_columns())
 * and converts them into the same vendor-neutral measurement beans used by the other database connector suites
 * (RelationalDataManagerMeasurement, RelationalSchemaMeasurement, RelationalTableMeasurement and
 * RelationalColumnMeasurement), tagged with the reused SurveyDatabaseAnnotationType constants.
 * <br>
 * DuckDB, being embedded, does not expose the same workload statistics as a client/server database (there is no
 * equivalent of Postgres's pg_stat_database/pg_table_size/pg_column_size).  Where DuckDB does not provide an
 * equivalent measurement, the corresponding field is simply left at its default value.
 */
public class DuckDBDatabaseStatsExtractor
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
    public DuckDBDatabaseStatsExtractor(List<String>                  validDatabases,
                                        SurveyActionServiceConnector surveyActionServiceConnector)
    {
        this.validDatabases               = validDatabases;
        this.surveyActionServiceConnector = surveyActionServiceConnector;
    }


    /**
     * Retrieve statistics about each requested database using DuckDB's pragma_database_size() table function.
     *
     * @param databaseJDBCConnection connection to the DuckDB database
     * @throws SQLException            problem accessing the database
     */
    void getDatabaseStatistics(java.sql.Connection databaseJDBCConnection) throws SQLException
    {
        for (String databaseName : validDatabases)
        {
            databaseResults.put(databaseName, new DatabaseDetails(databaseName));
        }

        final String databaseSizeSQLCommand = "SELECT * FROM pragma_database_size();";

        try
        {
            PreparedStatement preparedStatement = databaseJDBCConnection.prepareStatement(databaseSizeSQLCommand);
            ResultSet         resultSet         = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                String databaseName = resultSet.getString("database_name");

                DatabaseDetails currentDatabase = databaseResults.get(databaseName);

                if (currentDatabase != null)
                {
                    long totalBlocks = resultSet.getLong("total_blocks");
                    long blockSize   = resultSet.getLong("block_size");

                    currentDatabase.setSize(totalBlocks * blockSize);
                }
            }

            resultSet.close();
            preparedStatement.close();
        }
        catch (SQLException sqlException)
        {
            /*
             * pragma_database_size() may not be available on every DuckDB version, or for every attached
             * database.  This is not fatal to the survey - the size measurement is simply left unset.
             */
            try
            {
                databaseJDBCConnection.rollback();
            }
            catch (Exception error)
            {
                // ignore
            }
        }
    }


    /**
     * Extract detailed information about the schemas, tables and columns in a database using DuckDB's own
     * catalog metadata table functions.
     *
     * @param databaseName          name of database that is connected to
     * @param databaseSQLConnection connection to the named database
     * @throws SQLException            problem accessing the database
     */
    void getSchemaStatistics(String              databaseName,
                             java.sql.Connection databaseSQLConnection) throws SQLException
    {
        final String duckdbSchemasSQLCommand = "SELECT * FROM duckdb_schemas() WHERE NOT internal;";
        final String duckdbTablesSQLCommand  = "SELECT * FROM duckdb_tables();";
        final String duckdbViewsSQLCommand   = "SELECT * FROM duckdb_views() WHERE NOT internal;";
        final String duckdbColumnsSQLCommand = "SELECT * FROM duckdb_columns();";

        try
        {
            DatabaseDetails databaseDetails = databaseResults.get(databaseName);

            if (databaseDetails == null)
            {
                databaseDetails = new DatabaseDetails(databaseName);
                databaseResults.put(databaseName, databaseDetails);
            }

            /*
             * Ensure every non-internal schema is represented, even if it has no tables or views.
             */
            PreparedStatement preparedStatement = databaseSQLConnection.prepareStatement(duckdbSchemasSQLCommand);
            ResultSet         resultSet         = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                String schemaName = resultSet.getString("schema_name");

                if (schemaName != null)
                {
                    databaseDetails.getSchemaDetails(schemaName);
                }
            }

            resultSet.close();
            preparedStatement.close();

            /*
             * Extract core information about the tables.
             */
            preparedStatement = databaseSQLConnection.prepareStatement(duckdbTablesSQLCommand);
            resultSet         = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                String schemaName = resultSet.getString("schema_name");
                String tableName  = resultSet.getString("table_name");

                if ((schemaName != null) && (tableName != null))
                {
                    SchemaDetails schemaDetails = databaseDetails.getSchemaDetails(schemaName);
                    TableDetails  tableDetails  = schemaDetails.getTableDetails(tableName);

                    boolean hasIndexes  = resultSet.getLong("index_count") > 0;
                    long    columnCount = resultSet.getLong("column_count");
                    String  sql         = resultSet.getString("sql");

                    tableDetails.setTableMeasurements("Table", hasIndexes, columnCount, sql);
                }
            }

            resultSet.close();
            preparedStatement.close();

            /*
             * Extract core information about the views.
             */
            preparedStatement = databaseSQLConnection.prepareStatement(duckdbViewsSQLCommand);
            resultSet         = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                String schemaName = resultSet.getString("schema_name");
                String viewName   = resultSet.getString("view_name");

                if ((schemaName != null) && (viewName != null))
                {
                    SchemaDetails schemaDetails = databaseDetails.getSchemaDetails(schemaName);
                    TableDetails  tableDetails  = schemaDetails.getTableDetails(viewName);

                    long   columnCount = resultSet.getLong("column_count");
                    String sql         = resultSet.getString("sql");

                    tableDetails.setTableMeasurements("View", false, columnCount, sql);
                }
            }

            resultSet.close();
            preparedStatement.close();

            /*
             * Extract key stats about each column in each table.
             */
            preparedStatement = databaseSQLConnection.prepareStatement(duckdbColumnsSQLCommand);
            resultSet         = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                String schemaName = resultSet.getString("schema_name");
                String tableName  = resultSet.getString("table_name");

                if ((schemaName != null) && (tableName != null) && (databaseDetails.getSchemaDetails(schemaName).hasTable(tableName)))
                {
                    String  columnName    = resultSet.getString("column_name");
                    String  columnType    = resultSet.getString("data_type");
                    boolean columnIsNull  = "YES".equalsIgnoreCase(resultSet.getString("is_nullable"));

                    SchemaDetails schemaDetails = databaseDetails.getSchemaDetails(schemaName);
                    TableDetails  tableDetails  = schemaDetails.getTableDetails(tableName);
                    ColumnDetails columnDetails = tableDetails.getColumnDetails(columnName);

                    columnDetails.setColumnMeasurement(columnType, !columnIsNull);
                }
            }

            resultSet.close();
            preparedStatement.close();

            /*
             * Count up instances of schema, tables and columns in database.
             */
            databaseDetails.setUpCounts();
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
     * Convert the information retrieve from the database into annotations recognized by the survey action framework.
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
     * Manages consolidated information about a database.
     */
    static class DatabaseDetails
    {
        private final RelationalDataManagerMeasurement databaseMeasurements;
        private final Map<String, SchemaDetails>        schemas = new HashMap<>();


        DatabaseDetails(String databaseName)
        {
            this.databaseMeasurements = new RelationalDataManagerMeasurement();
            this.databaseMeasurements.setResourceName(databaseName);
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

                long tableCount  = 0L;
                long viewCount   = 0L;
                long columnCount = 0L;

                for (SchemaDetails schemaDetails : schemas.values())
                {
                    if (schemaDetails != null)
                    {
                        tableCount  = tableCount + schemaDetails.getTableCount();
                        viewCount   = viewCount + schemaDetails.getViewCount();
                        columnCount = columnCount + schemaDetails.getColumnCount();
                    }
                }

                databaseMeasurements.setTableCount(tableCount);
                databaseMeasurements.setViewCount(viewCount);
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
     * Manages consolidated information about a schema.
     */
    static class SchemaDetails
    {
        private final RelationalSchemaMeasurement schemaMeasurement = new RelationalSchemaMeasurement();
        private final Map<String, TableDetails>   tables            = new HashMap<>();


        /**
         * Create a schema details object.
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
         * Return whether a table/view with this name has already been recorded for this schema.
         *
         * @param tableName name of table
         * @return boolean
         */
        boolean hasTable(String tableName)
        {
            return tables.containsKey(tableName);
        }


        /**
         * Return the number of tables.
         *
         * @return table count
         */
        long getTableCount()
        {
            long tableCount = 0L;

            for (TableDetails tableDetails : tables.values())
            {
                if ((tableDetails != null) && ("Table".equals(tableDetails.getTableMeasurements().getTableType())))
                {
                    tableCount++;
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

            for (TableDetails tableDetails : tables.values())
            {
                if ((tableDetails != null) && ("View".equals(tableDetails.getTableMeasurements().getTableType())))
                {
                    viewCount++;
                }
            }

            schemaMeasurement.setViewCount(viewCount);

            return schemaMeasurement.getViewCount();
        }


        /**
         * Return the number of columns summed from all of the tables.
         *
         * @return column count (from all tables)
         */
        long getColumnCount()
        {
            long columnCount = 0L;

            for (TableDetails tableDetails : tables.values())
            {
                if (tableDetails != null)
                {
                    columnCount = columnCount + tableDetails.getColumnCount();
                }
            }

            schemaMeasurement.setColumnCount(columnCount);
            return schemaMeasurement.getColumnCount();
        }


        /**
         * Retrieve the qualified schema name.
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
         * Return a map of properties about the schema.
         *
         * @return map
         */
        Map<String, String> getSchemaResourceProperties()
        {
            Map<String, String> resourceProperties = new HashMap<>();

            resourceProperties.put(RelationalSchemaMetric.QUALIFIED_SCHEMA_NAME.getDisplayName(), schemaMeasurement.getQualifiedSchemaName());
            resourceProperties.put(RelationalSchemaMetric.SCHEMA_NAME.getDisplayName(), schemaMeasurement.getSchemaName());
            resourceProperties.put(RelationalSchemaMetric.TABLE_COUNT.getDisplayName(), Long.toString(schemaMeasurement.getTableCount()));
            resourceProperties.put(RelationalSchemaMetric.VIEW_COUNT.getDisplayName(), Long.toString(schemaMeasurement.getViewCount()));
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
     * Manages consolidated information about a table or view.
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
         * Set up key values describing a table or view.
         *
         * @param tableType "Table" or "View"
         * @param hasIndexes does the table have indexes
         * @param columnCount number of columns reported by DuckDB's catalog
         * @param queryDefinition the SQL used to define the table/view (as reported by DuckDB)
         */
        void setTableMeasurements(String  tableType,
                                  boolean hasIndexes,
                                  long    columnCount,
                                  String  queryDefinition)
        {
            this.tableMeasurement.setTableType(tableType);
            this.tableMeasurement.setHasIndexes(hasIndexes);
            this.tableMeasurement.setColumnCount(columnCount);
            this.tableMeasurement.setQueryDefinition(queryDefinition);
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
         * Retrieve the qualified table name.
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
         * Return a map of properties about the table.
         *
         * @return map
         */
        Map<String, String> getTableResourceProperties()
        {
            Map<String, String> resourceProperties = new HashMap<>();

            resourceProperties.put(RelationalTableMetric.TABLE_QNAME.getDisplayName(), tableMeasurement.getQualifiedTableName());
            resourceProperties.put(RelationalTableMetric.TABLE_NAME.getDisplayName(), tableMeasurement.getTableName());
            resourceProperties.put(RelationalTableMetric.TABLE_TYPE.getDisplayName(), tableMeasurement.getTableType());
            resourceProperties.put(RelationalTableMetric.COLUMN_COUNT.getDisplayName(), Long.toString(tableMeasurement.getColumnCount()));
            resourceProperties.put(RelationalTableMetric.HAS_INDEXES.getDisplayName(), Boolean.toString(tableMeasurement.getHasIndexes()));
            resourceProperties.put(RelationalTableMetric.QUERY_DEFINITION.getDisplayName(), tableMeasurement.getQueryDefinition());

            return resourceProperties;
        }


        /**
         * Return the set of table measurements collected.
         *
         * @return measurements object
         */
        public RelationalTableMeasurement getTableMeasurements()
        {
            return tableMeasurement;
        }
    }


    /**
     * Manages consolidated information about a column.
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
         * Retrieve the qualified column name.
         *
         * @return string
         */
        public String getQualifiedColumnName()
        {
            return columnMeasurement.getQualifiedColumnName();
        }


        /**
         * Record the type and nullability of the column, as reported by DuckDB's catalog.
         *
         * @param columnTypeName DuckDB logical type name
         * @param columnNotNull  true if the column is declared NOT NULL
         */
        void setColumnMeasurement(String  columnTypeName,
                                  boolean columnNotNull)
        {
            this.columnMeasurement.setColumnDataType(columnTypeName);
            this.columnMeasurement.setColumnNotNull(columnNotNull);
        }


        /**
         * Return a map of properties about the column.
         *
         * @return map
         */
        Map<String, String> getColumnResourceProperties()
        {
            Map<String, String> resourceProperties = new HashMap<>();

            resourceProperties.put(RelationalColumnMetric.COLUMN_QNAME.getDisplayName(), columnMeasurement.getQualifiedColumnName());
            resourceProperties.put(RelationalColumnMetric.COLUMN_NAME.getDisplayName(), columnMeasurement.getColumnName());
            resourceProperties.put(RelationalColumnMetric.COLUMN_TYPE.getDisplayName(), columnMeasurement.getColumnDataType());
            resourceProperties.put(RelationalColumnMetric.COLUMN_NOT_NULL.getDisplayName(), Boolean.toString(columnMeasurement.getColumnNotNull()));

            return resourceProperties;
        }


        /**
         * Return the set of column measurements collected.
         *
         * @return measurements object
         */
        public RelationalColumnMeasurement getColumnMeasurements()
        {
            return columnMeasurement;
        }
    }
}
