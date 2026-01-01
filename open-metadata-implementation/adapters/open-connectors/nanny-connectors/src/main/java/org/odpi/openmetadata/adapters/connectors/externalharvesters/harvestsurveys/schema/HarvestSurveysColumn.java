/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.externalharvesters.harvestsurveys.schema;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLColumn;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.ColumnType;
import org.odpi.openmetadata.frameworks.opensurvey.measurements.*;

/**
 * Describes the different types of columns found in the audit log database schema
 */
public enum HarvestSurveysColumn implements PostgreSQLColumn
{
    SYNC_TIME                    ("sync_time", ColumnType.DATE, "Time that a record is made.", true),
    DISPLAY_NAME                 ("display_name", ColumnType.STRING, "Display name of the associated element", false),
    ASSET_GUID                   ("asset_guid", ColumnType.STRING, "Unique identifier of the asset", true),
    ASSET_TYPE_NAME              ("asset_type_name", ColumnType.STRING, "Open Metadata Type for asset.", true),
    QUALIFIED_NAME               ("qualified_name", ColumnType.STRING, "Unique name for associated element.", true),
    DEPLOYED_IMPLEMENTATION_TYPE ("deployed_implementation_type", ColumnType.STRING, "Technology type for element.", false),
    CREATION_TIME                ("creation_time", ColumnType.DATE, "Creation time for the element.", true),
    METADATA_COLLECTION_ID       ("metadata_collection_id", ColumnType.STRING, "Home metadata collection for the subject element.", false),
    DESCRIPTION                  ("description", ColumnType.STRING, "Description of the element.", false),
    SURVEY_REPORT_GUID           ("sr_guid", ColumnType.STRING, "Unique identifier for a survey report.", true),
    ENGINE_ACTION_GUID           ("engine_action_guid", ColumnType.STRING, "Unique identifier of the engine that run the survey.", true),
    REQUEST_TYPE                 ("request_type", ColumnType.STRING, "The request type ", false),
    GOVERNANCE_ENGINE_NAME       ("governance_engine_name", ColumnType.STRING, "Name of the engine that ran the survey.", false),
    ENGINE_HOST_USER_ID          ("engine_host_user_id", ColumnType.STRING, "User that ran the survey", false),
    INITIATOR                    ("initiator", ColumnType.STRING, "The user id of the engine action.", false),
    END_TIMESTAMP                ("end_timestamp", ColumnType.DATE, "The end time of the survey.", false),
    START_TIMESTAMP              ("start_timestamp", ColumnType.DATE, "The start time of the survey.", true),
    SUBJECT_GUID                 ("subject_guid", ColumnType.STRING, "Unique identifier of the subject of the survey.", false),
    SUBJECT_TYPE                 ("subject_type", ColumnType.STRING, "Open metadata type of the survey subject.", false),
    OPEN_METADATA_TYPE           ("open_metadata_type", ColumnType.STRING, "Open metadata type of this row element.", true),
    ANNOTATION_GUID              ("annotation_guid", ColumnType.STRING, "The unique identifier of the annotation.", true),
    ANNOTATION_TYPE              ("annotation_type", ColumnType.STRING, "The unique identifier of the annotation.", false),
    SUMMARY                      ("summary", ColumnType.STRING, "A summary of the annotation.", false),
    EXPLANATION                  ("explanation", ColumnType.STRING, "What does this annotation represent/contain.", false),
    ANALYSIS_STEP                ("analysis_step", ColumnType.STRING, "Which phase of the analysis produced the annotation.", false),
    CONFIDENCE_LEVEL             ("confidence_level", ColumnType.STRING, "How confident (0-100) that the figures in the annotation are accurate.", false),
    EXPRESSION                   ("expression", ColumnType.STRING, "Formula used to create the annotation (optional).", false),
    JSON_PROPERTIES              ("json_properties", ColumnType.STRING, "Optional JSON properties associated with the annotation.", false),
    MEASUREMENT_NAME             ("measurement_name", ColumnType.STRING, "The name of the measurement, aka annotation type.", true),
    MEASUREMENT_DISPLAY_NAME     ("measurement_display_value", ColumnType.STRING, "The measurement display description.", false),
    MEASUREMENT_VALUE            ("measurement_string_value", ColumnType.STRING, "The value of the measurement.", false),
    MEASUREMENT_NUMERIC_VALUE    ("measurement_numeric_value", ColumnType.INT, "The value of the measurement.", false),
    MEASUREMENT_CATEGORY         ("measurement_category", ColumnType.STRING, "The category of the measurement.", true),
    RESOURCE_PROPERTIES          ("resource_properties", ColumnType.STRING, "JSON encoded map of properties describing the resource.", true),
    RESOURCE_CREATION_TIME       ("resource_creation_time", ColumnType.DATE, "The creation time of the surveyed resource.", false),
    RESOURCE_SIZE                ("resource_size", ColumnType.INT, "The size of the surveyed resource.", false),
    ACTION_REQUEST_NAME          ("action_request_name", ColumnType.STRING, "The type of action requested.", false),
    ACTION_REQUEST_GUID          ("action_request_guid", ColumnType.STRING, "The unique identifier of the TargetForAction relationship.", true),
    ACTION_TARGET_GUID           ("action_target_guid", ColumnType.STRING, "The unique identifier of the action target element.", true),
    ACTION_TARGET_TYPE           ("action_target_type", ColumnType.STRING, "The open metadata type for the action target.", true),
    PURPOSE                      ("purpose", ColumnType.STRING, "Purpose of the survey.", false),
    FILE_NAME                    ("filename", ColumnType.STRING, FileMetric.FILE_NAME.getDescription(), true),
    FILE_EXTENSION               ("file_extension", ColumnType.STRING, FileMetric.FILE_EXTENSION.getDescription(), false),
    PATHNAME                     ("pathname", ColumnType.STRING, FileMetric.PATH_NAME.getDescription(), false),
    FILE_TYPE                    ("file_type", ColumnType.STRING, FileMetric.FILE_TYPE.getDescription(), false),
    ENCODING                     ("file_encoding", ColumnType.STRING, FileMetric.ENCODING.getDescription(), false),
    CAN_READ                     ("can_read", ColumnType.BOOLEAN, FileMetric.CAN_READ.getDescription(), false),
    CAN_WRITE                    ("can_write", ColumnType.BOOLEAN, FileMetric.CAN_WRITE.getDescription(), false),
    CAN_EXECUTE                  ("can_execute", ColumnType.BOOLEAN, FileMetric.CAN_EXECUTE.getDescription(), false),
    IS_SYM_LINK                  ("is_sym_link", ColumnType.BOOLEAN, FileMetric.IS_SYM_LINK.getDescription(), false),
    IS_HIDDEN                    ("is_hidden", ColumnType.BOOLEAN, FileMetric.IS_HIDDEN.getDescription(), false),
    FILE_CREATION_TIME           ("file_creation_time", ColumnType.DATE, FileMetric.CREATION_TIME.getDescription(), false),
    LAST_MODIFIED_TIME           ("last_modified_time", ColumnType.DATE, FileMetric.LAST_MODIFIED_TIME.getDescription(), false),
    LAST_ACCESSED_TIME           ("last_accessed_time", ColumnType.DATE, FileMetric.LAST_ACCESSED_TIME.getDescription(), false),
    FILE_SIZE                    ("file_size", ColumnType.INT, FileMetric.FILE_SIZE.getDescription(), false),
    RECORD_COUNT                 ("record_count", ColumnType.INT, FileMetric.RECORD_COUNT.getDescription(), false),
    DIRECTORY_NAME               ("directory_name", ColumnType.STRING, "Name of a file system directory.", true),
    FILE_COUNT                   ("file_count", ColumnType.INT, FileDirectoryMetric.FILE_COUNT.getDescription(), false),
    TOTAL_FILE_SIZE              ("total_file_size", ColumnType.LONG, FileDirectoryMetric.TOTAL_FILE_SIZE.getDescription(), false),
    SUB_DIRECTORY_COUNT          ("sub_directory_count", ColumnType.INT, FileDirectoryMetric.SUB_DIRECTORY_COUNT.getDescription(), false),
    READABLE_FILE_COUNT          ("readable_file_count", ColumnType.INT, FileDirectoryMetric.READABLE_FILE_COUNT.getDescription(), false),
    WRITEABLE_FILE_COUNT         ("writeable_file_count", ColumnType.INT, FileDirectoryMetric.WRITEABLE_FILE_COUNT.getDescription(), false),
    EXECUTABLE_FILE_COUNT        ("executable_file_count", ColumnType.INT, FileDirectoryMetric.EXECUTABLE_FILE_COUNT.getDescription(), false),
    SYM_LINK_FILE_COUNT          ("sym_link_file_count", ColumnType.INT, FileDirectoryMetric.SYM_LINK_COUNT.getDescription(), false),
    HIDDEN_FILE_COUNT            ("hidden_file_count", ColumnType.INT, FileDirectoryMetric.HIDDEN_FILE_COUNT.getDescription(), false),
    FILE_NAME_COUNT              ("file_name_count", ColumnType.INT, FileDirectoryMetric.FILE_NAME_COUNT.getDescription(), false),
    FILE_EXTENSION_COUNT         ("file_extension_count", ColumnType.INT, FileDirectoryMetric.FILE_EXTENSION_COUNT.getDescription(), false),
    FILE_TYPE_COUNT              ("file_type_count", ColumnType.INT, FileDirectoryMetric.FILE_TYPE_COUNT.getDescription(), false),
    ASSET_TYPE_COUNT             ("asset_type_count", ColumnType.INT, FileDirectoryMetric.ASSET_TYPE_COUNT.getDescription(), false),
    DEPLOYED_IMPLEMENTATION_TYPE_COUNT("deployed_implementation_type_count", ColumnType.INT, FileDirectoryMetric.DEPLOYED_IMPL_TYPE_COUNT.getDescription(), false),
    UNCLASSIFIED_FILE_COUNT     ("unclassified_file_count", ColumnType.INT, FileDirectoryMetric.UNCLASSIFIED_FILE_COUNT.getDescription(), false),
    INACCESSIBLE_FILE_COUNT     ("inaccessible_file_count", ColumnType.INT, FileDirectoryMetric.INACCESSIBLE_FILE_COUNT.getDescription(), false),
    LAST_FILE_CREATION_TIME     ("last_file_creation_time", ColumnType.DATE, FileDirectoryMetric.LAST_FILE_CREATION_TIME.getDescription(), false),
    LAST_FILE_MODIFICATION_TIME ("last_file_modification_time", ColumnType.DATE, FileDirectoryMetric.LAST_FILE_MODIFIED_TIME.getDescription(), false),
    LAST_FILE_ACCESSED_TIME     ("last_file_accessed_time", ColumnType.DATE, FileDirectoryMetric.LAST_FILE_ACCESSED_TIME.getDescription(), false),
    RESOURCE_NAME               ("resource_name", ColumnType.STRING, "Fully qualified name of the resource.", true),
    SCHEMA_COUNT                ("schema_count", ColumnType.INT, RelationalDatabaseMetric.SCHEMA_COUNT.getDescription(), false),
    TABLE_COUNT                 ("table_count", ColumnType.LONG, RelationalDatabaseMetric.TABLE_COUNT.getDescription(), false),
    VIEW_COUNT                  ("view_count", ColumnType.LONG, RelationalDatabaseMetric.VIEW_COUNT.getDescription(), false),
    MAT_VIEW_COUNT              ("materialized_view_count", ColumnType.LONG, RelationalDatabaseMetric.MAT_VIEW_COUNT.getDescription(), false),
    COLUMN_COUNT                ("column_count", ColumnType.LONG, RelationalDatabaseMetric.COLUMN_COUNT.getDescription(), false),
    DATA_SIZE                   ("data_size", ColumnType.LONG, RelationalDatabaseMetric.DATA_SIZE.getDescription(), false),
    DATA_TYPE                   ("data_type", ColumnType.STRING, RelationalColumnMetric.COLUMN_TYPE.getDescription(), false),
    TABLE_TYPE                  ("table_type", ColumnType.STRING, RelationalTableMetric.TABLE_TYPE.getDescription(), false),
    RESOURCE_OWNER              ("resource_owner", ColumnType.STRING, RelationalTableMetric.TABLE_OWNER.getDescription(), false),
    NOT_NULL                    ("not_null", ColumnType.BOOLEAN, RelationalColumnMetric.COLUMN_NOT_NULL.getDescription(), false),
    AVERAGE_WIDTH               ("data_width", ColumnType.INT, RelationalColumnMetric.AVERAGE_WIDTH.getDescription(), false),
    NUMBER_OF_DISTINCT_VALUES   ("distinct_value_count", ColumnType.LONG, RelationalColumnMetric.NUMBER_OF_DISTINCT_VALUES.getDescription(), false),
    MOST_COMMON_VALUES          ("most_common_values", ColumnType.STRING, RelationalColumnMetric.MOST_COMMON_VALUES.getDescription(), false),
    MOST_COMMON_VALUES_FREQUENCY("most_common_values_frequency", ColumnType.STRING, RelationalColumnMetric.MOST_COMMON_VALUES_FREQUENCY.getDescription(), false),
    ROWS_FETCHED                ("rows_fetched", ColumnType.LONG, RelationalDatabaseMetric.ROWS_FETCHED.getDescription(), false),
    ROWS_INSERTED               ("rows_inserted", ColumnType.LONG, RelationalDatabaseMetric.ROWS_INSERTED.getDescription(), false),
    ROWS_UPDATED                ("rows_updated", ColumnType.LONG, RelationalDatabaseMetric.ROWS_UPDATED.getDescription(), false),
    ROWS_DELETED                ("rows_deleted", ColumnType.LONG, RelationalDatabaseMetric.ROWS_DELETED.getDescription(), false),
    IS_POPULATED                ("is_populated", ColumnType.BOOLEAN, RelationalTableMetric.IS_POPULATED.getDescription(), false),
    HAS_INDEXES                 ("has_indexes", ColumnType.BOOLEAN, RelationalTableMetric.HAS_INDEXES.getDescription(), false),
    HAS_RULES                   ("has_rules", ColumnType.BOOLEAN, RelationalTableMetric.HAS_RULES.getDescription(), false),
    HAS_TRIGGERS                ("has_triggers", ColumnType.BOOLEAN, RelationalTableMetric.HAS_TRIGGERS.getDescription(), false),
    HAS_ROW_SECURITY            ("has_row_security", ColumnType.BOOLEAN, RelationalTableMetric.HAS_ROW_SECURITY.getDescription(), false),
    QUERY_DEFINITION            ("query_definition", ColumnType.STRING, RelationalTableMetric.QUERY_DEFINITION.getDescription(), false),
    SESSION_TIME                ("session_time", ColumnType.INT, RelationalDatabaseMetric.SESSION_TIME.getDescription(), false),
    ACTIVE_TIME                 ("active_time", ColumnType.INT, RelationalDatabaseMetric.ACTIVE_TIME.getDescription(), false),
    LAST_STATS_RESET            ("last_statistics_reset", ColumnType.DATE, RelationalDatabaseMetric.LAST_STATISTICS_RESET.getDescription(), false),

    ;

    private final String     columnName;
    private final ColumnType columnType;
    private final String     columnDescription;
    private final boolean      isNotNull;


    HarvestSurveysColumn(String     columnName,
                         ColumnType columnType,
                         String     columnDescription,
                         boolean    isNotNull)
    {
        this.columnName        = columnName;
        this.columnType        = columnType;
        this.columnDescription = columnDescription;
        this.isNotNull         = isNotNull;
    }


    /**
     * retrieve the name of the column.
     *
     * @return name
     */
    @Override
    public String getColumnName()
    {
        return columnName;
    }

    /**
     * retrieve the qualified name of the column.
     *
     * @param tableName name of table
     * @return name
     */
    public String getColumnName(String tableName)
    {
        return tableName + "." + columnName;
    }


    /**
     * Return the type of the column.
     *
     * @return ColumnType
     */
    @Override
    public ColumnType getColumnType()
    {
        return columnType;
    }


    /**
     * Return th optional description for the column.
     *
     * @return text
     */
    @Override
    public String getColumnDescription()
    {
        return columnDescription;
    }


    /**
     * Return whether the column is not null;
     *
     * @return boolean
     */
    @Override
    public boolean isNotNull()
    {
        return isNotNull;
    }
}
