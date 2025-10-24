/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.controls;

import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ConfigurationPropertyType;

import java.util.ArrayList;
import java.util.List;

/**
 * UnityCatalogConfigurationProperty provides definitions for the configuration properties used with the UC connectors.
 */
public enum UnityCatalogConfigurationProperty
{
    /**
     * Unique identifier of the integration connector that is able to catalog the contents of a UC catalog.
     */
    FRIENDSHIP_GUID ("UnityCatalogFriendshipGUID",
                     "Unique identifier of the integration connector that is able to catalog the contents of a Unity Catalog (UC) catalog.",
                     "string",
                     "48886e79-a822-45a5-ab37-b5cefade9d8a",
                     false),

    /**
     * Provide a comma-separated list of catalog names that should be catalogued.  Other catalogs will be ignored.
     */
    INCLUDE_CATALOG_NAMES ("includeCatalogNames",
                          "Provide a comma-separated list of catalog names that should be catalogued.  Other catalogs will be ignored.",
                          "array<string>",
                          "catalog1,catalog2",
                          false),

    /**
     * Provide a comma-separated list of catalog names that should not be catalogued.  Only catalog not in this list will be catalogued.
     */
    EXCLUDE_CATALOG_NAMES ("excludeCatalogNames",
                          "Provide a comma-separated list of catalog names that should not be catalogued.  Only catalogs not in this list will be catalogued.",
                          "array<string>",
                          "catalog1,catalog2",
                          false),

    /**
     * Provide a comma-separated list of schema names that should be catalogued.  Other schemas will be ignored.
     */
    INCLUDE_SCHEMA_NAMES ("includeSchemaNames",
                          "Provide a comma-separated list of schema names that should be catalogued.  Other schemas will be ignored.",
                          "array<string>",
                          "schema1,schema2",
                          false),

    /**
     * Provide a comma-separated list of schema names that should not be catalogued.  Only schemas not in this list will be catalogued.
     */
    EXCLUDE_SCHEMA_NAMES ("excludeSchemaNames",
                          "Provide a comma-separated list of schema names that should not be catalogued.  Only schemas not in this list will be catalogued.",
                          "array<string>",
                          "schema1,schema2",
                          false),

    /**
     * Provide a comma-separated list of table names that should be catalogued.  Other tables will be ignored.
     */
    INCLUDE_TABLE_NAMES ("includeTableNames",
                          "Provide a comma-separated list of table names that should be catalogued.  Other tables will be ignored.",
                          "array<string>",
                          "table1,table2",
                          false),

    /**
     * Provide a comma-separated list of table names that should not be catalogued.  Only tables not in this list will be catalogued.
     */
    EXCLUDE_TABLE_NAMES ("excludeTableNames",
                          "Provide a comma-separated list of table names that should not be catalogued.  Only tables not in this list will be catalogued.",
                          "array<string>",
                          "table1,table2",
                          false),

    /**
     * Provide a comma-separated list of function names that should be catalogued.  Other functions will be ignored.
     */
    INCLUDE_FUNCTION_NAMES ("includeFunctionNames",
                          "Provide a comma-separated list of function names that should be catalogued.  Other functions will be ignored.",
                          "array<string>",
                          "function1,function22",
                          false),

    /**
     * Provide a comma-separated list of function names that should not be catalogued.  Only functions not in this list will be catalogued.
     */
    EXCLUDE_FUNCTION_NAMES ("excludeFunctionNames",
                          "Provide a comma-separated list of function names that should not be catalogued.  Only functions not in this list will be catalogued.",
                          "array<string>",
                          "function1,function2",
                          false),

    /**
     * Provide a comma-separated list of volume names that should be catalogued.  Other volumes will be ignored.
     */
    INCLUDE_VOLUME_NAMES ("includeVolumeNames",
                          "Provide a comma-separated list of volume names that should be catalogued.  Other volumes will be ignored.",
                          "array<string>",
                          "volume1,volume2",
                          false),

    /**
     * Provide a comma-separated list of volume names that should not be catalogued.  Only volumes not in this list will be catalogued.
     */
    EXCLUDE_VOLUME_NAMES ("excludeVolumeNames",
                          "Provide a comma-separated list of volume names that should not be catalogued.  Only volumes not in this list will be catalogued.",
                          "array<string>",
                          "volume1,volume2",
                          false),

    /**
     * Provide a comma-separated list of model names that should be catalogued.  Other models will be ignored.
     */
    INCLUDE_MODEL_NAMES ("includeModelNames",
                          "Provide a comma-separated list of model names that should be catalogued.  Other models will be ignored.",
                          "array<string>",
                          "model1,model2",
                          false),

    /**
     * Provide a comma-separated list of model names that should not be catalogued.  Only models not in this list will be catalogued.
     */
    EXCLUDE_MODEL_NAMES ("excludeModelNames",
                          "Provide a comma-separated list of model names that should not be catalogued.  Only models not in this list will be catalogued.",
                          "array<string>",
                          "model1,model2",
                          false),

    /**
     * The name of the catalog being described.
     */
    CATALOG_NAME(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(),
                 UnityCatalogPlaceholderProperty.CATALOG_NAME.getDescription(),
                 UnityCatalogPlaceholderProperty.CATALOG_NAME.getDataType(),
                 UnityCatalogPlaceholderProperty.CATALOG_NAME.getExample(),
                 false),

    /**
     * The name of a schema within a catalog in Unity Catalog (UC).
     */
    SCHEMA_NAME(UnityCatalogPlaceholderProperty.SCHEMA_NAME.getName(),
                UnityCatalogPlaceholderProperty.SCHEMA_NAME.getDescription(),
                UnityCatalogPlaceholderProperty.SCHEMA_NAME.getDataType(),
                UnityCatalogPlaceholderProperty.SCHEMA_NAME.getExample(),
                false),
    ;

    public final String           name;
    public final String           description;
    public final String           dataType;
    public final String           example;
    public final boolean          isPlaceholder;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the request parameter
     * @param description description of the request parameter
     * @param dataType type of value of the request parameter
     * @param example example of the request parameter
     * @param isPlaceholder is this also used as a placeholder property?
     */
    UnityCatalogConfigurationProperty(String  name,
                                      String  description,
                                      String  dataType,
                                      String  example,
                                      boolean isPlaceholder)
    {
        this.name          = name;
        this.description   = description;
        this.dataType      = dataType;
        this.example       = example;
        this.isPlaceholder = isPlaceholder;
    }


    /**
     * Return the name of the request parameter.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the description of the configuration property.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the data type for the configuration property.
     *
     * @return data type name
     */
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Return an example of the configuration property to help users understand how to set it up.
     *
     * @return example
     */
    public String getExample()
    {
        return example;
    }


    /**
     * Return whether this value is also used as a placeholder property.
     *
     * @return boolean
     */
    public boolean isPlaceholder()
    {
        return isPlaceholder;
    }


    /**
     * Get recognizedConfigurationProperties for the UC Server Integration connector.
     *
     * @return list of property names
     */
    public static List<String> getUnityCatalogServerRecognizedConfigurationProperties()
    {
        List<String> recognizedConfigurationProperties = new ArrayList<>();

        recognizedConfigurationProperties.add(UnityCatalogConfigurationProperty.FRIENDSHIP_GUID.getName());
        recognizedConfigurationProperties.add(UnityCatalogConfigurationProperty.INCLUDE_CATALOG_NAMES.getName());
        recognizedConfigurationProperties.add(UnityCatalogConfigurationProperty.EXCLUDE_CATALOG_NAMES.getName());

        return recognizedConfigurationProperties;
    }


    /**
     * Retrieve the defined configuration properties for the UC Server integration connector.
     *
     * @return list of configuration property types
     */
    public static List<ConfigurationPropertyType> getUnityCatalogServerConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        configurationPropertyTypes.add(UnityCatalogConfigurationProperty.FRIENDSHIP_GUID.getConfigurationPropertyType());
        configurationPropertyTypes.add(UnityCatalogConfigurationProperty.INCLUDE_CATALOG_NAMES.getConfigurationPropertyType());
        configurationPropertyTypes.add(UnityCatalogConfigurationProperty.EXCLUDE_CATALOG_NAMES.getConfigurationPropertyType());

        return configurationPropertyTypes;
    }


    /**
     * Get recognizedConfigurationProperties for the UC Inside Catalog Integration connector.
     *
     * @return list of property names
     */
    public static List<String> getUnityCatalogInsideCatalogRecognizedConfigurationProperties()
    {
        List<String> recognizedConfigurationProperties = new ArrayList<>();

        recognizedConfigurationProperties.add(UnityCatalogConfigurationProperty.INCLUDE_SCHEMA_NAMES.getName());
        recognizedConfigurationProperties.add(UnityCatalogConfigurationProperty.EXCLUDE_SCHEMA_NAMES.getName());
        recognizedConfigurationProperties.add(UnityCatalogConfigurationProperty.INCLUDE_TABLE_NAMES.getName());
        recognizedConfigurationProperties.add(UnityCatalogConfigurationProperty.EXCLUDE_TABLE_NAMES.getName());
        recognizedConfigurationProperties.add(UnityCatalogConfigurationProperty.INCLUDE_FUNCTION_NAMES.getName());
        recognizedConfigurationProperties.add(UnityCatalogConfigurationProperty.EXCLUDE_FUNCTION_NAMES.getName());
        recognizedConfigurationProperties.add(UnityCatalogConfigurationProperty.INCLUDE_VOLUME_NAMES.getName());
        recognizedConfigurationProperties.add(UnityCatalogConfigurationProperty.EXCLUDE_VOLUME_NAMES.getName());

        return recognizedConfigurationProperties;
    }


    /**
     * Retrieve the defined configuration properties for the UC Inside Catalog integration connector.
     *
     * @return list of configuration property types
     */
    public static List<ConfigurationPropertyType> getUnityCatalogInsideCatalogConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        configurationPropertyTypes.add(UnityCatalogConfigurationProperty.INCLUDE_SCHEMA_NAMES.getConfigurationPropertyType());
        configurationPropertyTypes.add(UnityCatalogConfigurationProperty.EXCLUDE_SCHEMA_NAMES.getConfigurationPropertyType());
        configurationPropertyTypes.add(UnityCatalogConfigurationProperty.INCLUDE_TABLE_NAMES.getConfigurationPropertyType());
        configurationPropertyTypes.add(UnityCatalogConfigurationProperty.EXCLUDE_TABLE_NAMES.getConfigurationPropertyType());
        configurationPropertyTypes.add(UnityCatalogConfigurationProperty.INCLUDE_FUNCTION_NAMES.getConfigurationPropertyType());
        configurationPropertyTypes.add(UnityCatalogConfigurationProperty.EXCLUDE_FUNCTION_NAMES.getConfigurationPropertyType());
        configurationPropertyTypes.add(UnityCatalogConfigurationProperty.INCLUDE_VOLUME_NAMES.getConfigurationPropertyType());
        configurationPropertyTypes.add(UnityCatalogConfigurationProperty.EXCLUDE_VOLUME_NAMES.getConfigurationPropertyType());

        return configurationPropertyTypes;
    }



    /**
     * Get recognizedConfigurationProperties for the UC Catalog Survey Service.
     *
     * @return list of property names
     */
    public static List<String> getUnityCatalogInsideCatalogSurveyRecognizedConfigurationProperties()
    {
        List<String> recognizedConfigurationProperties = new ArrayList<>();

        recognizedConfigurationProperties.add(UnityCatalogConfigurationProperty.CATALOG_NAME.getName());

        return recognizedConfigurationProperties;
    }


    /**
     * Retrieve the defined configuration properties for the UC Server Survey Service.
     *
     * @return list of configuration property types
     */
    public static List<ConfigurationPropertyType> getUnityCatalogInsideCatalogSurveyConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        configurationPropertyTypes.add(UnityCatalogConfigurationProperty.CATALOG_NAME.getConfigurationPropertyType());

        return configurationPropertyTypes;
    }




    /**
     * Get recognizedConfigurationProperties for the UC Catalog Survey Service.
     *
     * @return list of property names
     */
    public static List<String> getUnityCatalogInsideSchemaSurveyRecognizedConfigurationProperties()
    {
        List<String> recognizedConfigurationProperties = new ArrayList<>();

        recognizedConfigurationProperties.add(UnityCatalogConfigurationProperty.CATALOG_NAME.getName());
        recognizedConfigurationProperties.add(UnityCatalogConfigurationProperty.SCHEMA_NAME.getName());

        return recognizedConfigurationProperties;
    }


    /**
     * Retrieve the defined configuration properties for the UC Server Survey Service.
     *
     * @return list of configuration property types
     */
    public static List<ConfigurationPropertyType> getUnityCatalogInsideSchemaSurveyConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        configurationPropertyTypes.add(UnityCatalogConfigurationProperty.CATALOG_NAME.getConfigurationPropertyType());
        configurationPropertyTypes.add(UnityCatalogConfigurationProperty.SCHEMA_NAME.getConfigurationPropertyType());

        return configurationPropertyTypes;
    }


    /**
     * Retrieve all the defined configuration properties
     *
     * @return list of configuration property types
     */
    public static List<ConfigurationPropertyType> getConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        for (UnityCatalogConfigurationProperty configurationProperty : UnityCatalogConfigurationProperty.values())
        {
            configurationPropertyTypes.add(configurationProperty.getConfigurationPropertyType());
        }

        return configurationPropertyTypes;
    }


    /**
     * Return a summary of this enum to use in a connector provider.
     *
     * @return request parameter type
     */
    public ConfigurationPropertyType getConfigurationPropertyType()
    {
        ConfigurationPropertyType configurationPropertyType = new ConfigurationPropertyType();

        configurationPropertyType.setName(name);
        configurationPropertyType.setDescription(description);
        configurationPropertyType.setDataType(dataType);
        configurationPropertyType.setExample(example);
        configurationPropertyType.setRequired(isPlaceholder);

        return configurationPropertyType;
    }

    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "ConfigurationProperty{ name=" + name + "}";
    }
}
