/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.controls;


import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;

import java.util.ArrayList;
import java.util.List;

/**
 * PlaceholderProperty provides some standard definitions for placeholder properties used to pass properties
 * to services that use templates.
 */
public enum UnityCatalogPlaceholderProperty
{
    /**
     * The unique name of a Unity Catalog (UC) server
     */
    SERVER_QUALIFIED_NAME ("ucServerQualifiedName", "The unique name of a Unity Catalog (UC) server.", DataType.STRING.getName(), "Unity Catalog Server:Unity Catalog 1"),

    /**
     * The name of the catalog being described.
     */
    CATALOG_NAME ("ucCatalogName", "The name of a catalog within Unity Catalog (UC).", DataType.STRING.getName(), "my_catalog"),

    /**
     * The name of a schema within a catalog in Unity Catalog (UC).
     */
    SCHEMA_NAME ("ucSchemaName", "The name of a schema within a catalog in Unity Catalog (UC).  This value is combined with the name of the catalog to get the full name.", DataType.STRING.getName(), "my_schema"),

    /**
     * The identifier of the owner of this element
     */
    OWNER ("ucOwner", "The identifier of the owner of this element", DataType.STRING.getName(), null),

    /**
     * GUID for the associated Hive Metadata Store.
     */
    METASTORE_ID("ucMetaStoreId", "GUID for the associated Hive Metadata Store.", DataType.STRING.getName(), null),

    /**
     * Type of element to secure.
     */
    SECURABLE_TYPE("ucSecurableType", "Type of element to secure.", DataType.STRING.getName(), null),

    /**
     * Kind of security got this element.
     */
    SECURABLE_KIND("ucSecurableKind", "Kind of security got this element.", DataType.STRING.getName(), null),

    /**
     * Can this be updated?
     */
    BROWSE_ONLY("ucBrowseOnly", "Can this be updated?", DataType.BOOLEAN.getName(), null),

    /**
     * Isolation mode, such as OPEN.
     */
    ISOLATION_MODE("ucIsolationMode", "Isolation mode, such as OPEN.", DataType.STRING.getName(), null),

    /**
     * Access mode.
     */
    ACCESSIBLE_IN_CURRENT_WORKSPACE("ucAccessibleInCurrentWorkspace", "Access mode.", DataType.BOOLEAN.getName(), null),

    /**
     * The location where the data associated with this element in Unity Catalog is stored.
     */
    STORAGE_LOCATION ("ucStorageLocation", "The location where the data associated with this element in Unity Catalog is stored.", DataType.STRING.getName(), "a/b/c"),

    /**
     * The name of a table within a schema and catalog in Unity Catalog (UC).
     */
    TABLE_NAME ("ucTableName", "The name of a table within a schema and catalog in Unity Catalog (UC).  This value is combined with the name of the schema and catalog to get the full name.", DataType.STRING.getName(), "my_table"),

    /**
     * The type a table: MANAGED or EXTERNAL.
     */
    TABLE_TYPE ("ucTableType", "The type of a table: MANAGED or EXTERNAL.", DataType.STRING.getName(), "MANAGED"),

    /**
     * The format of the data source: DELTA, CSV, JSON, AVRO, PARQUET, ORC, TEXT.
     */
    DATA_SOURCE_FORMAT ("ucDataSourceFormat", "The format of the data source: DELTA, CSV, JSON, AVRO, PARQUET, ORC, TEXT.", DataType.STRING.getName(), "CSV"),

    /**
     * The name of a volume within a schema and catalog in Unity Catalog (UC).
     */
    VOLUME_NAME ("ucVolumeName", "The name of a volume within a schema and catalog in Unity Catalog (UC).  This value is combined with the name of the schema and catalog to get the full name.", DataType.STRING.getName(), "my_volume"),

    /**
     * The type of volume: MANAGED or EXTERNAL.
     */
    VOLUME_TYPE ("ucVolumeType", "The type of a volume: MANAGED or EXTERNAL.", DataType.STRING.getName(), "MANAGED"),

    /**
     * The name of a function within a schema and catalog in Unity Catalog (UC).
     */
    FUNCTION_NAME ("ucFunctionName", "The name of a function within a schema and catalog in Unity Catalog (UC).  This value is combined with the name of the schema and catalog to get the full name.", DataType.STRING.getName(), "my_function"),

    /**
     * The name of a function within a schema and catalog in Unity Catalog (UC).
     */
    MODEL_NAME ("ucModelName", "The name of a deployed model within a schema and catalog in Unity Catalog (UC).  This value is combined with the name of the schema and catalog to get the full name.", DataType.STRING.getName(), "my_model"),

    /**
     * The number of a version of a deployed model within a schema and catalog in Unity Catalog (UC).
     */
    MODEL_VERSION ("ucModelVersion", "The number of a version of a deployed model within a schema and catalog in Unity Catalog (UC).", DataType.LONG.getName(), "1"),

    /**
     * The status of a version of a deployed model within a schema and catalog in Unity Catalog (UC).
     */
    MODEL_VERSION_STATUS ("ucModelVersionStatus", "The status of a version of a deployed model within a schema and catalog in Unity Catalog (UC).", DataType.STRING.getName(), "READY"),

    /**
     * The source of a version of a deployed model within a schema and catalog in Unity Catalog (UC).
     */
    MODEL_VERSION_SOURCE ("ucModelVersionSource", "The source of a version of a deployed model within a schema and catalog in Unity Catalog (UC).", DataType.STRING.getName(), null),

    /**
     * The run identifier associated with a version of a deployed model within a schema and catalog in Unity Catalog (UC).
     */
    MODEL_VERSION_RUN_ID ("ucModelVersionRunId", "The run identifier associated with a version of a deployed model within a schema and catalog in Unity Catalog (UC).", DataType.STRING.getName(), null),


    ;

    public final String           name;
    public final String           description;
    public final String           dataType;
    public final String           example;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the placeholder property
     * @param description description of the placeholder property
     * @param dataType type of value of the placeholder property
     * @param example example of the placeholder property
     */
    UnityCatalogPlaceholderProperty(String name,
                                    String description,
                                    String dataType,
                                    String example)
    {
        this.name        = name;
        this.description = description;
        this.dataType    = dataType;
        this.example     = example;
    }


    /**
     * Return the name of the placeholder property.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the placeholder to use when building templates.
     *
     * @return placeholder property
     */
    public String getPlaceholder()
    {
        return "~{" + name + "}~";
    }


    /**
     * Return the description of the placeholder property.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the data type for the placeholder property.
     *
     * @return data type name
     */
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Return an example of the placeholder property to help users understand how to set it up.
     *
     * @return example
     */
    public String getExample()
    {
        return example;
    }


    /**
     * Retrieve all the defined placeholder properties
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getServerPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(PlaceholderProperty.HOST_URL.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.PORT_NUMBER.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.SERVER_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.DESCRIPTION.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholderType());

        return placeholderPropertyTypes;
    }


    /**
     * Retrieve all the defined placeholder properties for a server using secrets.
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getSecureServerPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(PlaceholderProperty.HOST_URL.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.PORT_NUMBER.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.SERVER_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.DESCRIPTION.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.SECRETS_STORE.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.SECRETS_COLLECTION_NAME.getPlaceholderType());

        return placeholderPropertyTypes;
    }

    /**
     * Retrieve all the defined placeholder properties
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getCatalogPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(UnityCatalogPlaceholderProperty.SERVER_QUALIFIED_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.SERVER_NETWORK_ADDRESS.getPlaceholderType());
        placeholderPropertyTypes.add(CATALOG_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.DESCRIPTION.getPlaceholderType());

        return placeholderPropertyTypes;
    }


    /**
     * Retrieve all the defined placeholder properties
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getSchemaPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(PlaceholderProperty.SERVER_NETWORK_ADDRESS.getPlaceholderType());
        placeholderPropertyTypes.add(CATALOG_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(SCHEMA_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.DESCRIPTION.getPlaceholderType());

        return placeholderPropertyTypes;
    }



    /**
     * Retrieve all the defined placeholder properties
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getTablePlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(PlaceholderProperty.SERVER_NETWORK_ADDRESS.getPlaceholderType());
        placeholderPropertyTypes.add(CATALOG_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(SCHEMA_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(TABLE_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.DESCRIPTION.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholderType());
        placeholderPropertyTypes.add(UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getPlaceholderType());
        placeholderPropertyTypes.add(UnityCatalogPlaceholderProperty.TABLE_TYPE.getPlaceholderType());
        placeholderPropertyTypes.add(UnityCatalogPlaceholderProperty.DATA_SOURCE_FORMAT.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.ROOT_SCHEMA_TYPE_QUALIFIED_NAME.getPlaceholderType());

        return placeholderPropertyTypes;
    }


    /**
     * Retrieve all the defined placeholder properties
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getVolumePlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(PlaceholderProperty.SERVER_NETWORK_ADDRESS.getPlaceholderType());
        placeholderPropertyTypes.add(CATALOG_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(SCHEMA_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(VOLUME_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.DESCRIPTION.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholderType());
        placeholderPropertyTypes.add(UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getPlaceholderType());
        placeholderPropertyTypes.add(UnityCatalogPlaceholderProperty.VOLUME_TYPE.getPlaceholderType());

        return placeholderPropertyTypes;
    }


    /**
     * Retrieve all the defined placeholder properties
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getFunctionPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(PlaceholderProperty.SERVER_NETWORK_ADDRESS.getPlaceholderType());
        placeholderPropertyTypes.add(CATALOG_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(SCHEMA_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(FUNCTION_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.DESCRIPTION.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.ROOT_SCHEMA_TYPE_QUALIFIED_NAME.getPlaceholderType());

        return placeholderPropertyTypes;
    }



    /**
     * Retrieve all the defined placeholder properties
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getRegisteredModelPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(PlaceholderProperty.SERVER_NETWORK_ADDRESS.getPlaceholderType());
        placeholderPropertyTypes.add(CATALOG_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(SCHEMA_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(MODEL_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.DESCRIPTION.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.ROOT_SCHEMA_TYPE_QUALIFIED_NAME.getPlaceholderType());

        return placeholderPropertyTypes;
    }



    /**
     * Retrieve all the defined placeholder properties
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getModelVersionPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(PlaceholderProperty.SERVER_NETWORK_ADDRESS.getPlaceholderType());
        placeholderPropertyTypes.add(CATALOG_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(SCHEMA_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(MODEL_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(MODEL_VERSION.getPlaceholderType());
        placeholderPropertyTypes.add(MODEL_VERSION_RUN_ID.getPlaceholderType());
        placeholderPropertyTypes.add(MODEL_VERSION_SOURCE.getPlaceholderType());
        placeholderPropertyTypes.add(MODEL_VERSION_STATUS.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.DESCRIPTION.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.ROOT_SCHEMA_TYPE_QUALIFIED_NAME.getPlaceholderType());

        return placeholderPropertyTypes;
    }



    /**
     * Return a summary of this enum to use in a service provider.
     *
     * @return placeholder property type
     */
    public PlaceholderPropertyType getPlaceholderType()
    {
        PlaceholderPropertyType placeholderPropertyType = new PlaceholderPropertyType();

        placeholderPropertyType.setName(name);
        placeholderPropertyType.setDescription(description);
        placeholderPropertyType.setDataType(dataType);
        placeholderPropertyType.setExample(example);
        placeholderPropertyType.setRequired(true);

        return placeholderPropertyType;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "PlaceholderProperty{ name=" + name + "}";
    }
}
