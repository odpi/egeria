/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.duckdb.controls;

import org.odpi.openmetadata.frameworks.openmetadata.controls.FileSystemConfigurationProperty;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ConfigurationPropertyType;

import java.util.ArrayList;
import java.util.List;

/**
 * DuckDBConfigurationProperty provides definitions for the configuration properties used with the DuckDB connectors.
 * DuckDB is embedded, so the database itself typically needs neither a userId nor a password - DATABASE_USER_ID
 * and DATABASE_PASSWORD are retained here for consistency with the other database connector suites, and because
 * they may be needed to supply credentials for sources that are attached to the DuckDB database using its
 * federation ("ATTACH") capability.
 */
public enum DuckDBConfigurationProperty
{
    /**
     * The path to the DuckDB database file being catalogued - or the literal value ":memory:" for an in-memory session.
     */
    DATABASE_PATH (DuckDBPlaceholderProperty.DATABASE_PATH.getName(),
                  DuckDBPlaceholderProperty.DATABASE_PATH.getDescription(),
                  DuckDBPlaceholderProperty.DATABASE_PATH.getDataType(),
                  DuckDBPlaceholderProperty.DATABASE_PATH.getExample(),
                  true),

    /**
     * The name of the database being catalogued.
     */
    DATABASE_NAME (DuckDBPlaceholderProperty.DATABASE_NAME.getName(),
                   DuckDBPlaceholderProperty.DATABASE_NAME.getDescription(),
                   DuckDBPlaceholderProperty.DATABASE_NAME.getDataType(),
                   DuckDBPlaceholderProperty.DATABASE_NAME.getExample(),
                   true),

    /**
     * The userId to store in the userId attribute of the connection.  DuckDB itself does not need this, but it is
     * kept for consistency with the other database connector suites, and it may be used to authenticate to a
     * data source that has been ATTACH-ed to the DuckDB database.
     */
    DATABASE_USER_ID ("databaseUserId",
                      "The userId to store in the userId attribute of the connection.  DuckDB itself does not need this to open the database file, but it may be used to authenticate to a data source that has been ATTACH-ed to the DuckDB database.",
                      "string",
                      "myDatabase",
                      false),

    /**
     * The password to store in the clearPassword attribute of the connection.  DuckDB itself does not need this, but
     * it is kept for consistency with the other database connector suites, and it may be used to authenticate to a
     * data source that has been ATTACH-ed to the DuckDB database.
     */
    DATABASE_PASSWORD ("databasePassword",
                       "The password to store in the clearPassword attribute of the connection.  DuckDB itself does not need this to open the database file, but it may be used to authenticate to a data source that has been ATTACH-ed to the DuckDB database.",
                       "string",
                       "myDatabase",
                       false),

    /**
     * DuckDB's ATTACH statements needed to reconnect this database's federation relationships.  DuckDB does not
     * persist ATTACH-ed data sources in the database file between sessions - each new connection must re-issue the
     * ATTACH statements itself (see https://duckdb.org/docs/sql/statements/attach) - so, without this configuration
     * property, a survey or catalog connector opening its own fresh connection would never see any of the
     * federation relationships that were set up in a different session.  Each entry is a complete SQL statement,
     * for example "ATTACH 'host=myhost dbname=mydb user=myuser password=mypassword' AS mypg (TYPE POSTGRES,
     * READ_ONLY)" - including any INSTALL/LOAD statements needed for the relevant DuckDB extension.
     */
    ATTACH_STATEMENTS ("attachStatements",
                       "DuckDB's ATTACH (and any INSTALL/LOAD) statements to run when the connector opens its own connection to the database, needed because DuckDB does not persist ATTACH-ed data sources in the database file between sessions.",
                       "array<string>",
                       "INSTALL postgres,LOAD postgres,ATTACH 'host=myhost dbname=mydb user=myuser password=mypassword' AS mypg (TYPE POSTGRES, READ_ONLY)",
                       false),

    /**
     * Unique identifier of the integration connector that is able to catalog the contents of a JDBC database.
     */
    FRIENDSHIP_GUID ("DuckDBFriendshipGUID",
                     "Unique identifier of the integration connector that is able to catalog the contents of a DuckDB Database.",
                     "string",
                     "48886e79-a822-45a5-ab37-b5cefade9d8a",
                     false),

    ;

    public final String  name;
    public final String  description;
    public final String  dataType;
    public final String  example;
    public final boolean isPlaceholder;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the request parameter
     * @param description description of the request parameter
     * @param dataType type of value of the request parameter
     * @param example example of the request parameter
     * @param isPlaceholder is this also used as a placeholder property?
     */
    DuckDBConfigurationProperty(String  name,
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
     * Get recognizedConfigurationProperties for the DuckDB Database Integration connector.  This includes the
     * three generic file system properties (fileSystemName, localMountPoint, canonicalMountPoint) defined in
     * FileSystemConfigurationProperty, since the connector reuses the files-integration-connector's FileClassifier
     * to determine the canonical (masked) path of the DuckDB database file and of any files that it discovers
     * through DuckDB's federation capabilities.
     *
     * @return list of property names
     */
    public static List<String> getDuckDBDatabaseIntegrationConnectorNames()
    {
        List<String> recognizedConfigurationProperties = new ArrayList<>();

        recognizedConfigurationProperties.add(DuckDBConfigurationProperty.DATABASE_PATH.getName());
        recognizedConfigurationProperties.add(DuckDBConfigurationProperty.DATABASE_NAME.getName());
        recognizedConfigurationProperties.add(DuckDBConfigurationProperty.DATABASE_USER_ID.getName());
        recognizedConfigurationProperties.add(DuckDBConfigurationProperty.DATABASE_PASSWORD.getName());
        recognizedConfigurationProperties.add(DuckDBConfigurationProperty.ATTACH_STATEMENTS.getName());
        recognizedConfigurationProperties.add(DuckDBConfigurationProperty.FRIENDSHIP_GUID.getName());
        recognizedConfigurationProperties.add(FileSystemConfigurationProperty.FILE_SYSTEM_NAME.getName());
        recognizedConfigurationProperties.add(FileSystemConfigurationProperty.LOCAL_MOUNT_POINT.getName());
        recognizedConfigurationProperties.add(FileSystemConfigurationProperty.CANONICAL_MOUNT_POINT.getName());

        return recognizedConfigurationProperties;
    }


    /**
     * Retrieve the defined configuration properties for the DuckDB Database Integration connector.  This includes
     * the three generic file system properties defined in FileSystemConfigurationProperty - see
     * {@link #getDuckDBDatabaseIntegrationConnectorNames()} for more details on why these are needed.
     *
     * @return list of configuration property types
     */
    public static List<ConfigurationPropertyType> getDuckDBDatabaseConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        configurationPropertyTypes.add(DuckDBConfigurationProperty.DATABASE_PATH.getConfigurationPropertyType());
        configurationPropertyTypes.add(DuckDBConfigurationProperty.DATABASE_NAME.getConfigurationPropertyType());
        configurationPropertyTypes.add(DuckDBConfigurationProperty.DATABASE_USER_ID.getConfigurationPropertyType());
        configurationPropertyTypes.add(DuckDBConfigurationProperty.DATABASE_PASSWORD.getConfigurationPropertyType());
        configurationPropertyTypes.add(DuckDBConfigurationProperty.ATTACH_STATEMENTS.getConfigurationPropertyType());
        configurationPropertyTypes.add(DuckDBConfigurationProperty.FRIENDSHIP_GUID.getConfigurationPropertyType());
        configurationPropertyTypes.add(FileSystemConfigurationProperty.FILE_SYSTEM_NAME.getConfigurationPropertyType());
        configurationPropertyTypes.add(FileSystemConfigurationProperty.LOCAL_MOUNT_POINT.getConfigurationPropertyType());
        configurationPropertyTypes.add(FileSystemConfigurationProperty.CANONICAL_MOUNT_POINT.getConfigurationPropertyType());

        return configurationPropertyTypes;
    }


    /**
     * Retrieve all the defined configuration properties for this enum (not including the FileSystemConfigurationProperty
     * entries - see {@link #getDuckDBDatabaseConfigurationPropertyTypes()} for the full list used by the provider).
     *
     * @return list of configuration property types
     */
    public static List<ConfigurationPropertyType> getConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        for (DuckDBConfigurationProperty configurationProperty : DuckDBConfigurationProperty.values())
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
