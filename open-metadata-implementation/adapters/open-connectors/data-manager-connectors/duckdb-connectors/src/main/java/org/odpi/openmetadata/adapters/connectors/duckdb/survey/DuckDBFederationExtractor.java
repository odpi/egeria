/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.duckdb.survey;

import org.odpi.openmetadata.adapters.connectors.duckdb.ffdc.DuckDBAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.AnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.ResourceMeasureAnnotationProperties;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DuckDBFederationExtractor discovers the federation relationships that a DuckDB database makes use of:
 * <ul>
 *     <li>other databases that have been ATTACH-ed to it, discovered via DuckDB's duckdb_databases() table function</li>
 *     <li>views that scan external file/object-store resources (via functions like read_parquet(), read_csv(),
 *     read_csv_auto(), read_json(), iceberg_scan() or delta_scan()), discovered via DuckDB's duckdb_views() table
 *     function</li>
 * </ul>
 * Both queries are wrapped defensively - not every DuckDB database makes use of federation features, and older
 * DuckDB versions may not support every table function used here, so failures are logged and processing continues
 * rather than failing the whole survey/catalog pass.
 */
public class DuckDBFederationExtractor
{
    private static final Pattern[] SCAN_FUNCTION_PATTERNS = new Pattern[]{
            Pattern.compile("read_parquet\\s*\\(\\s*'([^']+)'", Pattern.CASE_INSENSITIVE),
            Pattern.compile("read_csv_auto\\s*\\(\\s*'([^']+)'", Pattern.CASE_INSENSITIVE),
            Pattern.compile("read_csv\\s*\\(\\s*'([^']+)'", Pattern.CASE_INSENSITIVE),
            Pattern.compile("read_json\\s*\\(\\s*'([^']+)'", Pattern.CASE_INSENSITIVE),
            Pattern.compile("iceberg_scan\\s*\\(\\s*'([^']+)'", Pattern.CASE_INSENSITIVE),
            Pattern.compile("delta_scan\\s*\\(\\s*'([^']+)'", Pattern.CASE_INSENSITIVE),
            };

    private final SurveyActionServiceConnector surveyActionServiceConnector;
    private final AuditLog                     auditLog;
    private final String                       connectorName;
    private final List<AnnotationProperties>   annotations = new ArrayList<>();


    /**
     * Constructor.
     *
     * @param surveyActionServiceConnector calling connector (used to format JSON properties)
     * @param auditLog                     logging destination
     * @param connectorName                name of the calling connector - used in audit log messages
     */
    public DuckDBFederationExtractor(SurveyActionServiceConnector surveyActionServiceConnector,
                                     AuditLog                     auditLog,
                                     String                       connectorName)
    {
        this.surveyActionServiceConnector = surveyActionServiceConnector;
        this.auditLog                     = auditLog;
        this.connectorName                = connectorName;
    }


    /**
     * Discover databases that have been ATTACH-ed to the DuckDB database.
     *
     * @param databaseJDBCConnection connection to the DuckDB database
     * @param primaryDatabaseName    name of the database being surveyed/catalogued - used only for audit log messages.
     *                               This is NOT used to identify (and exclude) the "self" row in duckdb_databases():
     *                               it is Egeria's configured display name for the asset, which is independent of
     *                               DuckDB's own internal database_name (always derived from the file's base name,
     *                               eg "egeria_test" for "egeria_test.duckdb") - the two only match by coincidence.
     *                               current_database() is queried instead to reliably identify the self row.
     */
    public void discoverAttachedSources(java.sql.Connection databaseJDBCConnection,
                                        String               primaryDatabaseName)
    {
        final String methodName        = "discoverAttachedSources";
        final String duckdbDatabasesSQL = "SELECT * FROM duckdb_databases() WHERE NOT internal;";

        try
        {
            String currentDatabaseName = getCurrentDatabaseName(databaseJDBCConnection);

            PreparedStatement preparedStatement = databaseJDBCConnection.prepareStatement(duckdbDatabasesSQL);
            ResultSet         resultSet         = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                String databaseName = resultSet.getString("database_name");

                if ((databaseName != null) && (!databaseName.equals(currentDatabaseName)))
                {
                    String  sourceType = getStringOrNull(resultSet, "type");
                    String  location   = redactPassword(getStringOrNull(resultSet, "path"));
                    boolean readOnly   = getBooleanOrFalse(resultSet, "readonly");

                    Map<String, String> resourceProperties = new HashMap<>();

                    resourceProperties.put("Alias", databaseName);
                    resourceProperties.put("Source Type", sourceType);
                    resourceProperties.put("Location", location);
                    resourceProperties.put("Read Only", Boolean.toString(readOnly));

                    ResourceMeasureAnnotationProperties annotation = new ResourceMeasureAnnotationProperties();

                    annotation.setQualifiedName("Annotation::" + SurveyDuckDBAnnotationType.ATTACHED_SOURCE.getName() + "::" + databaseName + "::" + UUID.randomUUID());
                    annotation.setAnnotationType(SurveyDuckDBAnnotationType.ATTACHED_SOURCE.getName());
                    annotation.setSummary(SurveyDuckDBAnnotationType.ATTACHED_SOURCE.getSummary());
                    annotation.setExplanation(SurveyDuckDBAnnotationType.ATTACHED_SOURCE.getExplanation());
                    annotation.setAnalysisStep(SurveyDuckDBAnnotationType.ATTACHED_SOURCE.getAnalysisStep());
                    annotation.setResourceProperties(resourceProperties);

                    annotations.add(annotation);

                    if (auditLog != null)
                    {
                        auditLog.logMessage(methodName,
                                            DuckDBAuditCode.ATTACHED_SOURCE_FOUND.getMessageDefinition(connectorName,
                                                                                                        primaryDatabaseName,
                                                                                                        databaseName,
                                                                                                        (sourceType == null ? "<unknown>" : sourceType)));
                    }
                }
            }

            resultSet.close();
            preparedStatement.close();
        }
        catch (Exception error)
        {
            /*
             * Not every DuckDB version, or database, supports/uses federation - this must never fail the whole
             * survey/catalog pass.
             */
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    DuckDBAuditCode.FEDERATION_QUERY_FAILED.getMessageDefinition(connectorName,
                                                                                                  "duckdb_databases()",
                                                                                                  primaryDatabaseName,
                                                                                                  error.getMessage()));
            }

            rollbackQuietly(databaseJDBCConnection);
        }
    }


    /**
     * Discover views that scan external file/object-store resources.
     *
     * @param databaseJDBCConnection connection to the DuckDB database
     * @param primaryDatabaseName    name of the database being surveyed/catalogued - used only for audit log messages
     */
    public void discoverExternalFileSources(java.sql.Connection databaseJDBCConnection,
                                            String               primaryDatabaseName)
    {
        final String methodName      = "discoverExternalFileSources";
        final String duckdbViewsSQL  = "SELECT * FROM duckdb_views();";

        try
        {
            PreparedStatement preparedStatement = databaseJDBCConnection.prepareStatement(duckdbViewsSQL);
            ResultSet         resultSet         = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                String viewName = resultSet.getString("view_name");
                String sql      = getStringOrNull(resultSet, "sql");

                if ((viewName != null) && (sql != null))
                {
                    for (Pattern pattern : SCAN_FUNCTION_PATTERNS)
                    {
                        Matcher matcher = pattern.matcher(sql);

                        while (matcher.find())
                        {
                            String location      = matcher.group(1);
                            String scanFunction  = getFunctionName(pattern);
                            String format        = getFormatFromFunctionName(scanFunction);

                            Map<String, String> resourceProperties = new HashMap<>();

                            resourceProperties.put("View Name", viewName);
                            resourceProperties.put("Scan Function", scanFunction);
                            resourceProperties.put("Location", location);
                            resourceProperties.put("Format", format);

                            ResourceMeasureAnnotationProperties annotation = new ResourceMeasureAnnotationProperties();

                            annotation.setQualifiedName("Annotation::" + SurveyDuckDBAnnotationType.EXTERNAL_FILE_SOURCE.getName() + "::" + viewName + "::" + UUID.randomUUID());
                            annotation.setAnnotationType(SurveyDuckDBAnnotationType.EXTERNAL_FILE_SOURCE.getName());
                            annotation.setSummary(SurveyDuckDBAnnotationType.EXTERNAL_FILE_SOURCE.getSummary());
                            annotation.setExplanation(SurveyDuckDBAnnotationType.EXTERNAL_FILE_SOURCE.getExplanation());
                            annotation.setAnalysisStep(SurveyDuckDBAnnotationType.EXTERNAL_FILE_SOURCE.getAnalysisStep());
                            annotation.setResourceProperties(resourceProperties);

                            annotations.add(annotation);

                            if (auditLog != null)
                            {
                                auditLog.logMessage(methodName,
                                                    DuckDBAuditCode.EXTERNAL_FILE_SOURCE_FOUND.getMessageDefinition(connectorName,
                                                                                                                    primaryDatabaseName,
                                                                                                                    viewName,
                                                                                                                    format,
                                                                                                                    location));
                            }
                        }
                    }
                }
            }

            resultSet.close();
            preparedStatement.close();
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    DuckDBAuditCode.FEDERATION_QUERY_FAILED.getMessageDefinition(connectorName,
                                                                                                  "duckdb_views()",
                                                                                                  primaryDatabaseName,
                                                                                                  error.getMessage()));
            }

            rollbackQuietly(databaseJDBCConnection);
        }
    }


    /**
     * Return the accumulated list of federation annotations discovered so far.
     *
     * @return list of annotations (never null, but may be empty)
     */
    public List<AnnotationProperties> getAnnotations()
    {
        return annotations;
    }


    /**
     * Simple, ordered holder for one column's name and DuckDB-reported data type, as read from duckdb_columns().
     *
     * @param columnName name of the column
     * @param dataType   DuckDB's data type name for the column (eg "VARCHAR", "BIGINT") - may be null
     */
    public record DuckDBColumnInfo(String columnName, String dataType) {}


    /**
     * Retrieve the ordered list of columns (name and DuckDB-reported data type) for a table or view known to
     * DuckDB's own catalog.  Used both for a DuckDB view's own columns (to model the "real" structure of the
     * external file/object-store resource it scans) and, via {@link #getColumnsForAttachedTable}, for tables
     * exposed through an ATTACH-ed network-backed data source.
     *
     * @param databaseJDBCConnection connection to the DuckDB database
     * @param tableName              name of the table/view to retrieve columns for
     * @return ordered list of columns - never null, may be empty
     */
    public List<DuckDBColumnInfo> getColumnsForTable(java.sql.Connection databaseJDBCConnection,
                                                     String               tableName)
    {
        final String methodName        = "getColumnsForTable";
        final String duckdbColumnsSQL = "SELECT column_name, data_type FROM duckdb_columns() WHERE table_name = ? ORDER BY column_index;";

        List<DuckDBColumnInfo> columns = new ArrayList<>();

        try (PreparedStatement preparedStatement = databaseJDBCConnection.prepareStatement(duckdbColumnsSQL))
        {
            preparedStatement.setString(1, tableName);

            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                while (resultSet.next())
                {
                    String columnName = getStringOrNull(resultSet, "column_name");
                    String dataType   = getStringOrNull(resultSet, "data_type");

                    if (columnName != null)
                    {
                        columns.add(new DuckDBColumnInfo(columnName, dataType));
                    }
                }
            }
        }
        catch (Exception error)
        {
            /*
             * Not every DuckDB version supports duckdb_columns(), and the table/view may simply not exist (eg it
             * has not been created yet) - this must never fail the whole survey/catalog pass.
             */
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    DuckDBAuditCode.FEDERATION_QUERY_FAILED.getMessageDefinition(connectorName,
                                                                                                  "duckdb_columns()",
                                                                                                  tableName,
                                                                                                  error.getMessage()));
            }

            rollbackQuietly(databaseJDBCConnection);
        }

        return columns;
    }


    /**
     * Retrieve the names of the tables exposed by an ATTACH-ed data source, as known to DuckDB's own catalog.
     *
     * @param databaseJDBCConnection connection to the DuckDB database (must already have re-issued the ATTACH
     *                               statement that established the alias, since DuckDB does not persist ATTACH-ed
     *                               data sources between sessions/connections)
     * @param databaseAlias          alias the data source was ATTACH-ed under
     * @return list of table names - never null, may be empty
     */
    public List<String> getTablesForAttachedDatabase(java.sql.Connection databaseJDBCConnection,
                                                      String               databaseAlias)
    {
        final String methodName     = "getTablesForAttachedDatabase";
        final String duckdbTablesSQL = "SELECT table_name FROM duckdb_tables() WHERE database_name = ?;";

        List<String> tableNames = new ArrayList<>();

        try (PreparedStatement preparedStatement = databaseJDBCConnection.prepareStatement(duckdbTablesSQL))
        {
            preparedStatement.setString(1, databaseAlias);

            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                while (resultSet.next())
                {
                    String tableName = getStringOrNull(resultSet, "table_name");

                    if (tableName != null)
                    {
                        tableNames.add(tableName);
                    }
                }
            }
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    DuckDBAuditCode.FEDERATION_QUERY_FAILED.getMessageDefinition(connectorName,
                                                                                                  "duckdb_tables()",
                                                                                                  databaseAlias,
                                                                                                  error.getMessage()));
            }

            rollbackQuietly(databaseJDBCConnection);
        }

        return tableNames;
    }


    /**
     * Retrieve the ordered list of columns (name and DuckDB-reported data type) for one table exposed by an
     * ATTACH-ed data source, filtering duckdb_columns() by both database_name and table_name so that a table name
     * that happens to be duplicated across several ATTACH-ed sources is not confused with the wrong one.
     *
     * @param databaseJDBCConnection connection to the DuckDB database
     * @param databaseAlias          alias the data source was ATTACH-ed under
     * @param tableName              name of the table to retrieve columns for
     * @return ordered list of columns - never null, may be empty
     */
    public List<DuckDBColumnInfo> getColumnsForAttachedTable(java.sql.Connection databaseJDBCConnection,
                                                              String               databaseAlias,
                                                              String               tableName)
    {
        final String methodName        = "getColumnsForAttachedTable";
        final String duckdbColumnsSQL = "SELECT column_name, data_type FROM duckdb_columns() WHERE database_name = ? AND table_name = ? ORDER BY column_index;";

        List<DuckDBColumnInfo> columns = new ArrayList<>();

        try (PreparedStatement preparedStatement = databaseJDBCConnection.prepareStatement(duckdbColumnsSQL))
        {
            preparedStatement.setString(1, databaseAlias);
            preparedStatement.setString(2, tableName);

            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                while (resultSet.next())
                {
                    String columnName = getStringOrNull(resultSet, "column_name");
                    String dataType   = getStringOrNull(resultSet, "data_type");

                    if (columnName != null)
                    {
                        columns.add(new DuckDBColumnInfo(columnName, dataType));
                    }
                }
            }
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    DuckDBAuditCode.FEDERATION_QUERY_FAILED.getMessageDefinition(connectorName,
                                                                                                  "duckdb_columns()",
                                                                                                  databaseAlias + "." + tableName,
                                                                                                  error.getMessage()));
            }

            rollbackQuietly(databaseJDBCConnection);
        }

        return columns;
    }


    /**
     * Return DuckDB's own name for the database this connection is currently attached to by default - the row in
     * duckdb_databases() that represents the database being surveyed/catalogued, not a federated source.  This is
     * independent of whatever display name Egeria has configured for the asset.
     *
     * @param databaseJDBCConnection connection to the DuckDB database
     * @return DuckDB's current_database() value, or null if it could not be determined
     */
    private String getCurrentDatabaseName(java.sql.Connection databaseJDBCConnection)
    {
        try (PreparedStatement preparedStatement = databaseJDBCConnection.prepareStatement("SELECT current_database();");
             ResultSet         resultSet         = preparedStatement.executeQuery())
        {
            if (resultSet.next())
            {
                return resultSet.getString(1);
            }
        }
        catch (Exception error)
        {
            // Fall through and return null - the caller will then simply not exclude any row.
        }

        return null;
    }


    /**
     * Extract the function name (eg "read_parquet") that a compiled scan-function pattern matches on.
     *
     * @param pattern compiled pattern
     * @return function name
     */
    private String getFunctionName(Pattern pattern)
    {
        String patternString = pattern.pattern();

        int functionNameEnd = patternString.indexOf("\\s*\\(");

        if (functionNameEnd > 0)
        {
            return patternString.substring(0, functionNameEnd);
        }

        return patternString;
    }


    /**
     * Infer a human-readable format name from a DuckDB scan function name.
     *
     * @param scanFunction function name, eg "read_parquet"
     * @return format name, eg "Parquet"
     */
    private String getFormatFromFunctionName(String scanFunction)
    {
        if (scanFunction == null)
        {
            return "<unknown>";
        }

        String normalized = scanFunction.toLowerCase();

        if (normalized.contains("parquet"))
        {
            return "Parquet";
        }
        else if (normalized.contains("csv"))
        {
            return "CSV";
        }
        else if (normalized.contains("json"))
        {
            return "JSON";
        }
        else if (normalized.contains("iceberg"))
        {
            return "Iceberg";
        }
        else if (normalized.contains("delta"))
        {
            return "Delta";
        }

        return "<unknown>";
    }


    /**
     * Remove any "password=..." substring from a connection string/path before it is stored as an annotation
     * property, so that credentials used in ATTACH statements are never persisted into the open metadata ecosystem.
     *
     * @param value raw value, may be null
     * @return redacted value
     */
    private String redactPassword(String value)
    {
        if (value == null)
        {
            return null;
        }

        return value.replaceAll("(?i)password\\s*=\\s*[^ '\"]+", "password=*****");
    }


    /**
     * Safely retrieve a string column value, tolerating columns that may not exist on every DuckDB version.
     *
     * @param resultSet  result set positioned on the current row
     * @param columnName name of the column to fetch
     * @return string value, or null
     */
    private String getStringOrNull(ResultSet resultSet, String columnName)
    {
        try
        {
            return resultSet.getString(columnName);
        }
        catch (SQLException notFound)
        {
            return null;
        }
    }


    /**
     * Safely retrieve a boolean column value, tolerating columns that may not exist on every DuckDB version.
     *
     * @param resultSet  result set positioned on the current row
     * @param columnName name of the column to fetch
     * @return boolean value, or false if the column is missing
     */
    private boolean getBooleanOrFalse(ResultSet resultSet, String columnName)
    {
        try
        {
            return resultSet.getBoolean(columnName);
        }
        catch (SQLException notFound)
        {
            return false;
        }
    }


    /**
     * Roll back the connection, ignoring any error - used to clean up after a failed federation query.
     *
     * @param databaseJDBCConnection connection to roll back
     */
    private void rollbackQuietly(java.sql.Connection databaseJDBCConnection)
    {
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
