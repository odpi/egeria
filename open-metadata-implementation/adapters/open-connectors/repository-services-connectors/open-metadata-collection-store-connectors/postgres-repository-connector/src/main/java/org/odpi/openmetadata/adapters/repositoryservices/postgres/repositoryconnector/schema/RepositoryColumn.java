/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.schema;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLColumn;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.ColumnType;

/**
 * Describes the different types of columns found in the repository database schema
 */
public enum RepositoryColumn implements PostgreSQLColumn
{
    LOCAL_METADATA_COLLECTION_GUID("local_metadata_collection_guid", ColumnType.STRING, "Unique identifier of the local repository's metadata collection.", true),
    SERVER_NAME("server_name", ColumnType.STRING, "Unique name of the server that is hosting this repository.", true),
    SCHEMA_VERSION("schema_version", ColumnType.STRING, "Version of this database schema to manage schema migration.", true),

    TYPE_GUID("type_guid", ColumnType.STRING, "Unique identifier of an open metadata type.", true),
    TYPE_NAME("type_name", ColumnType.STRING, "Fully qualified name of an open metadata type.", true),
    ATTRIBUTE_TYPE_GUID("attribute_type_guid", ColumnType.STRING, "Unique identifier of an open metadata type.", false),
    ATTRIBUTE_TYPE_NAME("attribute_type_name", ColumnType.STRING, "Unique name of an open metadata type.", false),


    METADATA_COLLECTION_GUID("metadata_collection_guid", ColumnType.STRING, "Unique identifier of a metadata collection.", true),
    METADATA_COLLECTION_NAME("metadata_collection_name", ColumnType.STRING, "Unique name of a metadata collection.", false),
    INSTANCE_PROVENANCE_TYPE("instance_provenance_type", ColumnType.STRING, "Category of metadata collection.", true),
    REPLICATED_BY("replicated_by", ColumnType.STRING, "Metadata collection guid responsible for propagating updates about this instance.", false),


    MAPPING_PROPERTIES("mapping_properties", ColumnType.STRING, "Mapping properties for an entity instance header.", false),


    CREATED_BY("created_by", ColumnType.STRING, "UserId that created this instance.", true),
    CREATE_TIME("create_time", ColumnType.DATE, "Time when this instance was created.", true),


    CURRENT_STATUS("current_status", ColumnType.STRING, "Status of this instance. Values from the Instance Status enum.", true),
    STATUS_ON_DELETE("status_on_delete", ColumnType.STRING, "Status of this instance when it was deleted. Values from the Instance Status enum.", false),


    INSTANCE_GUID("instance_guid", ColumnType.STRING, "Unique identifier of an instance.", true),
    END_1_GUID("end_1_guid", ColumnType.STRING, "Unique identifier of the entity at end 1 of the relationship.", true),
    END_2_GUID("end_2_guid", ColumnType.STRING, "Unique identifier of the entity at end 2 of the relationship.", true),
    CLASSIFICATION_NAME("classification_name", ColumnType.STRING,  "Unique name of classification (same as classification's type name).", true),
    IS_PROXY("is_proxy", ColumnType.BOOLEAN,  "Is this just an entity proxy (meaning that we only have the unique properties).", true),


    REIDENTIFIED_FROM_GUID("reidentified_from_guid", ColumnType.STRING, "Unique identifier of an instance before its GUID was changed.", false),
    INSTANCE_URL("instance_url", ColumnType.STRING, "URL to retrieve this instance.", false),
    INSTANCE_LICENCE("instance_license", ColumnType.STRING, "License for this instance.  Typically used by content packs.", false),


    ATTRIBUTE_NAME("attribute_name", ColumnType.STRING, "Name of an open metadata attribute.", true),
    PROPERTY_NAME("property_name", ColumnType.STRING, "Qualified name of a property value, eg qualifiedName, zoneMembership.0 or additionalProperties.scalarValue.", true),
    PROPERTY_VALUE("property_value", ColumnType.STRING, "Value for property", false),
    PROPERTY_CATEGORY("property_category", ColumnType.STRING, "Category for attribute.  Comes from the InstancePropertyCategory enum unless it is a primitive, in which case it comes from the PrimitivePropertyCategory enum.", true),
    IS_UNIQUE_ATTRIBUTE("is_unique_attribute", ColumnType.BOOLEAN, "Is this attribute a unique identifier?.", true),


    UPDATED_BY("updated_by", ColumnType.STRING, "UserId that updated this property version.", false),
    UPDATE_TIME("update_time", ColumnType.DATE, "Time when this property version was created.", false),
    MAINTAINED_BY("maintained_by", ColumnType.STRING, "List of users that have contributed to this instance.", false),

    VERSION("version", ColumnType.LONG, "Monotonically increasing number for the instance version.", true),
    VERSION_START_TIME("version_start_time", ColumnType.DATE, "Time when this version became active.  This value is derived from the create_time/update_time.", true),
    VERSION_END_TIME("version_end_time", ColumnType.DATE, "Time when this version stops being the latest version.  This value is derived from the create_time/update_time.  This is null while the version is the latest version.  It is updated to the update_time-1 of the next version.", false),

    EFFECTIVE_FROM_TIME("effective_from_time", ColumnType.DATE, "Time when this property group begins to be effective.", false),
    EFFECTIVE_TO_TIME("effective_to_time", ColumnType.DATE, "Time when this property group stops being effective.", false),


    ;

    private final String     columnName;
    private final ColumnType columnType;
    private final String     columnDescription;
    private final boolean      isNotNull;


    RepositoryColumn(String     columnName,
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
