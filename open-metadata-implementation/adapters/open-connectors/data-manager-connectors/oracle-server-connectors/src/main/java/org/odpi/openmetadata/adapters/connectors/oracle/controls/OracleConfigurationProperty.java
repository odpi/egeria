/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.oracle.controls;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.controls.JDBCConfigurationProperty;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ConfigurationPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * OracleConfigurationProperty provides definitions for the configuration properties used with the oracle connectors.
 */
public enum OracleConfigurationProperty
{
    /**
     * The host IP address or domain name of the database server.
     */
    HOST_IDENTIFIER (PlaceholderProperty.HOST_IDENTIFIER.getName(),
                     PlaceholderProperty.HOST_IDENTIFIER.getDescription(),
                     PlaceholderProperty.HOST_IDENTIFIER.getDataType(),
                     PlaceholderProperty.HOST_IDENTIFIER.getExample(),
                     true),

    /**
     * The number of the port to use to connect to the database server.
     */
    PORT_NUMBER (PlaceholderProperty.PORT_NUMBER.getName(),
                 PlaceholderProperty.PORT_NUMBER.getDescription(),
                 PlaceholderProperty.PORT_NUMBER.getDataType(),
                 PlaceholderProperty.PORT_NUMBER.getExample(),
                 true),

    /**
     * The name of the database server being catalogued.
     */
    SERVER_NAME (PlaceholderProperty.SERVER_NAME.getName(),
                 PlaceholderProperty.SERVER_NAME.getDescription(),
                 PlaceholderProperty.SERVER_NAME.getDataType(),
                 PlaceholderProperty.SERVER_NAME.getExample(),
                 true),

    /**
     * The name of the pluggable database (PDB)/service being catalogued.
     */
    DATABASE_NAME (OraclePlaceholderProperty.DATABASE_NAME.getName(),
                   OraclePlaceholderProperty.DATABASE_NAME.getDescription(),
                   OraclePlaceholderProperty.DATABASE_NAME.getDataType(),
                   OraclePlaceholderProperty.DATABASE_NAME.getExample(),
                   true),

    /**
     * The userId to store in the userId attribute of the connection.
     */
    DATABASE_USER_ID (OraclePlaceholderProperty.DATABASE_USER_ID.getName(),
                      OraclePlaceholderProperty.DATABASE_USER_ID.getDescription(),
                      OraclePlaceholderProperty.DATABASE_USER_ID.getDataType(),
                      OraclePlaceholderProperty.DATABASE_USER_ID.getExample(),
                      true),

    /**
     * The password to store in the clearPassword attribute of the connection.
     */
    DATABASE_PASSWORD (OraclePlaceholderProperty.DATABASE_PASSWORD.getName(),
                       OraclePlaceholderProperty.DATABASE_PASSWORD.getDescription(),
                       OraclePlaceholderProperty.DATABASE_PASSWORD.getDataType(),
                       OraclePlaceholderProperty.DATABASE_PASSWORD.getExample(),
                       true),

    /**
     * The name of the database schema (Oracle user) being catalogued.
     */
    SCHEMA_NAME (PlaceholderProperty.SCHEMA_NAME.getName(),
                 PlaceholderProperty.SCHEMA_NAME.getDescription(),
                 PlaceholderProperty.SCHEMA_NAME.getDataType(),
                 PlaceholderProperty.SCHEMA_NAME.getExample(),
                 true),

    /**
     * The description of the database schema being catalogued.
     */
    SCHEMA_DESCRIPTION(PlaceholderProperty.DESCRIPTION.getName(),
                       PlaceholderProperty.DESCRIPTION.getDescription(),
                       PlaceholderProperty.DESCRIPTION.getDataType(),
                       PlaceholderProperty.DESCRIPTION.getExample(),
                       true),

    /**
     * The name of the database table being catalogued.
     */
    TABLE_NAME (PlaceholderProperty.TABLE_NAME.getName(),
                PlaceholderProperty.TABLE_NAME.getDescription(),
                PlaceholderProperty.TABLE_NAME.getDataType(),
                PlaceholderProperty.TABLE_NAME.getExample(),
                true),


    /**
     * The description of the database table being catalogued.
     */
    TABLE_DESCRIPTION (PlaceholderProperty.TABLE_DESCRIPTION.getName(),
                       PlaceholderProperty.TABLE_DESCRIPTION.getDescription(),
                       PlaceholderProperty.TABLE_DESCRIPTION.getDataType(),
                       PlaceholderProperty.TABLE_DESCRIPTION.getExample(),
                       true),


    /**
     * Provides a list of pluggable database (PDB) names that should not be catalogued.
     */
    EXCLUDE_DATABASE_LIST ("excludeDatabaseList",
                        "Provides a list of pluggable database (PDB) names that should not be catalogued.",
                        "array<string>",
                        "pdbBoring,pdbNotUsed",
                        false),


    /**
     * Provides a list of pluggable databases (PDBs) that should be catalogued.  If not set, or null, or *, all PDBs except the
     * excluded databases are catalogued.
     */
    INCLUDE_DATABASE_LIST ("includeDatabaseList",
                        "Provides a list of pluggable databases (PDBs) that should be catalogued.  " +
                                "If not set, or null, or *, all PDBs except the excluded databases are catalogued.",
                        "array<string>",
                        "*",
                        false),

    /**
     * Unique identifier of the integration connector that is able to catalog the contents of an Oracle pluggable database.
     */
    FRIENDSHIP_GUID ("OracleFriendshipGUID",
                     "Unique identifier of the integration connector that is able to catalog the contents of an Oracle pluggable database.",
                     "string",
                     "48886e79-a822-45a5-ab37-b5cefade9d8a",
                     false),

    /**
     * Additional properties passed straight through to the Oracle JDBC driver - for example, remarksReporting=true is
     * needed to retrieve table/column comments via DatabaseMetaData, and oracle.jdbc.timezoneAsRegion=false avoids
     * timezone conversion errors against certain PDB configurations.
     */
    ADDITIONAL_CONNECTION_PROPERTIES (JDBCConfigurationProperty.ADDITIONAL_CONNECTION_PROPERTIES.getName(),
                                      JDBCConfigurationProperty.ADDITIONAL_CONNECTION_PROPERTIES.getDescription(),
                                      JDBCConfigurationProperty.ADDITIONAL_CONNECTION_PROPERTIES.getDataType(),
                                      JDBCConfigurationProperty.ADDITIONAL_CONNECTION_PROPERTIES.getExample(),
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
    OracleConfigurationProperty(String  name,
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
     * Get recognizedConfigurationProperties for the Oracle Database Server Integration connector.
     *
     * @return list of property names
     */
    public static List<String> getOracleServerIntegrationConnectorNames()
    {
        List<String> recognizedConfigurationProperties = new ArrayList<>();

        recognizedConfigurationProperties.add(OracleConfigurationProperty.EXCLUDE_DATABASE_LIST.getName());
        recognizedConfigurationProperties.add(OracleConfigurationProperty.INCLUDE_DATABASE_LIST.getName());
        recognizedConfigurationProperties.add(OracleConfigurationProperty.HOST_IDENTIFIER.getName());
        recognizedConfigurationProperties.add(OracleConfigurationProperty.PORT_NUMBER.getName());
        recognizedConfigurationProperties.add(OracleConfigurationProperty.SERVER_NAME.getName());
        recognizedConfigurationProperties.add(OracleConfigurationProperty.DATABASE_USER_ID.getName());
        recognizedConfigurationProperties.add(OracleConfigurationProperty.DATABASE_PASSWORD.getName());
        recognizedConfigurationProperties.add(OracleConfigurationProperty.FRIENDSHIP_GUID.getName());
        recognizedConfigurationProperties.add(OracleConfigurationProperty.ADDITIONAL_CONNECTION_PROPERTIES.getName());

        return recognizedConfigurationProperties;
    }


    /**
     * Retrieve the defined configuration properties for the Oracle Database Server integration connector.
     *
     * @return list of configuration property types
     */
    public static List<ConfigurationPropertyType> getOracleServerConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        configurationPropertyTypes.add(OracleConfigurationProperty.EXCLUDE_DATABASE_LIST.getConfigurationPropertyType());
        configurationPropertyTypes.add(OracleConfigurationProperty.INCLUDE_DATABASE_LIST.getConfigurationPropertyType());
        configurationPropertyTypes.add(OracleConfigurationProperty.HOST_IDENTIFIER.getConfigurationPropertyType());
        configurationPropertyTypes.add(OracleConfigurationProperty.PORT_NUMBER.getConfigurationPropertyType());
        configurationPropertyTypes.add(OracleConfigurationProperty.SERVER_NAME.getConfigurationPropertyType());
        configurationPropertyTypes.add(OracleConfigurationProperty.DATABASE_USER_ID.getConfigurationPropertyType());
        configurationPropertyTypes.add(OracleConfigurationProperty.DATABASE_PASSWORD.getConfigurationPropertyType());
        configurationPropertyTypes.add(OracleConfigurationProperty.FRIENDSHIP_GUID.getConfigurationPropertyType());
        configurationPropertyTypes.add(OracleConfigurationProperty.ADDITIONAL_CONNECTION_PROPERTIES.getConfigurationPropertyType());

        return configurationPropertyTypes;
    }



    /**
     * Get recognizedConfigurationProperties for the Oracle tabular data source resource connector.
     *
     * @return list of property names
     */
    public static List<String> getOracleTabularDataSourceConfigPropertyNames()
    {
        List<String> recognizedConfigurationProperties = new ArrayList<>();

        recognizedConfigurationProperties.add(OracleConfigurationProperty.SCHEMA_NAME.getName());
        recognizedConfigurationProperties.add(OracleConfigurationProperty.SCHEMA_DESCRIPTION.getName());
        recognizedConfigurationProperties.add(OracleConfigurationProperty.TABLE_NAME.getName());
        recognizedConfigurationProperties.add(OracleConfigurationProperty.TABLE_DESCRIPTION.getName());
        recognizedConfigurationProperties.add(OracleConfigurationProperty.ADDITIONAL_CONNECTION_PROPERTIES.getName());

        return recognizedConfigurationProperties;
    }


    /**
     * Retrieve the defined configuration properties for the Oracle tabular data source connector
     *
     * @return list of configuration property types
     */
    public static List<ConfigurationPropertyType> getOracleTabularDataSourceConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        configurationPropertyTypes.add(OracleConfigurationProperty.SCHEMA_NAME.getConfigurationPropertyType());
        configurationPropertyTypes.add(OracleConfigurationProperty.SCHEMA_DESCRIPTION.getConfigurationPropertyType());
        configurationPropertyTypes.add(OracleConfigurationProperty.TABLE_NAME.getConfigurationPropertyType());
        configurationPropertyTypes.add(OracleConfigurationProperty.TABLE_DESCRIPTION.getConfigurationPropertyType());
        configurationPropertyTypes.add(OracleConfigurationProperty.ADDITIONAL_CONNECTION_PROPERTIES.getConfigurationPropertyType());

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

        for (OracleConfigurationProperty configurationProperty : OracleConfigurationProperty.values())
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
