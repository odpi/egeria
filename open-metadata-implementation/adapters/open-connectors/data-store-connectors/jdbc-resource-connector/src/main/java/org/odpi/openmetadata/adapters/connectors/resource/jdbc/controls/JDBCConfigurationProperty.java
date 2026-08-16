/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.resource.jdbc.controls;


import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ConfigurationPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;

import java.util.ArrayList;
import java.util.List;

/**
 * JDBCConfigurationProperty describes the configuration properties supported by the JDBC Resource connector.
 */
public enum JDBCConfigurationProperty
{
    /**
     * Connection string for the database where the schema is located. This should set the current schema to your chosen schema.
     */
    DATABASE_URL("databaseURL", "Connection string for the database where the schema is located. This should set the current schema to your chosen schema.", DataType.STRING.getDisplayName(), "~{postgreSQLDatabaseURL}~"),

    /**
     * Provides a name to use in messages about the database.  If it is not set then the connection URL string is used.
     */
    DATABASE_NAME("databaseName", "Provides a name to use in messages about the database.  If it is not set then the connection URL string is used.", DataType.STRING.getDisplayName(), null),

    /**
     * The name of the database schema used to store the data.  This should match the schema set up in the database URL.
     */
    DATABASE_SCHEMA ("databaseSchema", "The name of the database schema used to store the data.  This should match the schema set up in the database URL.", DataType.STRING.getDisplayName(), null),

    /**
     * The file name of the secrets store containing the log on credentials to access the database.
     */
    SECRETS_STORE ("secretsStore", "The file name of the secrets store containing the log on credentials to access the database.", DataType.STRING.getDisplayName(), "~{secretsStore}~"),

    /**
     * The name of the secrets collection within the secrets store to use for login credentials.
     */
    SECRETS_COLLECTION_NAME ("secretsCollectionName", "The name of the secrets collection within the secrets store to use for login credentials.", DataType.STRING.getDisplayName(), "~{postgreSQLServerCollectionName}~"),


    /**
     * An optional configuration property that causes the named class to be loaded and registered as a driver.
     * This property only needs to be defined if the connector is experiencing exceptions related to a missing DriverManager class for
     * the database URL.
     */
    JDBC_DRIVER_MANAGER_CLASS_NAME ("jdbcDriverManagerClassName", "An optional configuration property that causes the named class to be loaded and registered as a driver. This property only needs to be defined if the connector is experiencing exceptions related to a missing DriverManager class for the database URL.", DataType.STRING.getDisplayName(), null),

    /**
     * Sets the maximum time in seconds that this data source will wait while attempting to connect to a database.
     * The default value is 0 which means use the system default timeout, if any; otherwise it means no timeout.
     */
    JDBC_CONNECTION_TIMEOUT("jdbcConnectionTimeout", "Sets the maximum time in seconds that this data source will wait while attempting to connect to a database. The default value is 0 which means use the system default timeout, if any; otherwise it means no timeout.", DataType.INT.getDisplayName(), "10"),

    /**
     * Additional properties passed straight through to the JDBC driver on every connection. For example, Oracle's
     * driver needs remarksReporting=true to return table/column REMARKS (comments) via DatabaseMetaData - without
     * it they are silently omitted.
     */
    ADDITIONAL_CONNECTION_PROPERTIES("additionalConnectionProperties", "Additional properties passed straight through to the JDBC driver on every connection. For example, Oracle's driver needs remarksReporting=true to return table/column REMARKS (comments) via DatabaseMetaData - without it they are silently omitted.", DataType.MAP_STRING_STRING.getDisplayName(), "{\"remarksReporting\": \"true\"}"),

    /**
     * The maximum number of database connections this connector will hold open at once.  Note that a separate pool is
     * created for each connector instance - and one connector instance is created per catalog target - so this value
     * is multiplied by the number of catalog targets when sizing the database server.
     */
    JDBC_MAXIMUM_POOL_SIZE("jdbcMaximumPoolSize", "The maximum number of database connections this connector will hold open at once. Note that a separate pool is created for each connector instance - and one connector instance is created per catalog target - so this value is multiplied by the number of catalog targets when sizing the database server.", DataType.INT.getDisplayName(), "5"),

    /**
     * The minimum number of idle connections the pool keeps ready.  Setting this equal to jdbcMaximumPoolSize gives a
     * fixed-size pool, which is the recommended configuration for steady workloads.
     */
    JDBC_MINIMUM_IDLE("jdbcMinimumIdle", "The minimum number of idle connections the pool keeps ready. Setting this equal to jdbcMaximumPoolSize gives a fixed-size pool, which is the recommended configuration for steady workloads.", DataType.INT.getDisplayName(), "1"),

    /**
     * The number of milliseconds a caller waits for a free connection from the pool before failing.  This is distinct
     * from jdbcConnectionTimeout, which limits how long the driver waits when opening a new network connection.
     */
    JDBC_CONNECTION_WAIT_TIMEOUT("jdbcConnectionWaitTimeout", "The number of milliseconds a caller waits for a free connection from the pool before failing. This is distinct from jdbcConnectionTimeout, which limits how long the driver waits when opening a new network connection.", DataType.LONG.getDisplayName(), "30000"),

    /**
     * The maximum number of milliseconds a connection may live before the pool retires and replaces it.  This must be
     * set comfortably below any idle or lifetime limit imposed by the database server or intervening infrastructure,
     * otherwise the pool will hand out connections that have already been closed at the far end.
     */
    JDBC_MAXIMUM_CONNECTION_LIFETIME("jdbcMaximumConnectionLifetime", "The maximum number of milliseconds a connection may live before the pool retires and replaces it. This must be set comfortably below any idle or lifetime limit imposed by the database server or intervening infrastructure, otherwise the pool will hand out connections that have already been closed at the far end.", DataType.LONG.getDisplayName(), "1800000"),

    /**
     * How often, in milliseconds, the pool probes an idle connection to check it is still usable, retiring it if it
     * is not.  Zero disables the probe.  This must be smaller than jdbcMaximumConnectionLifetime.  Note that this is
     * the pool's own liveness check and is separate from socket level TCP keepalive, which the connector switches on
     * automatically for the drivers whose property name for it is known.
     */
    JDBC_CONNECTION_KEEPALIVE("jdbcConnectionKeepAlive", "How often, in milliseconds, the pool probes an idle connection to check it is still usable, retiring it if it is not. Zero disables the probe. This must be smaller than jdbcMaximumConnectionLifetime. Note that this is the pool's own liveness check and is separate from socket level TCP keepalive, which the connector switches on automatically for the drivers whose property name for it is known.", DataType.LONG.getDisplayName(), "120000"),

    /**
     * How long, in milliseconds, a connection may be held by a caller before the connector logs a stack trace of
     * whoever took it out.  Zero disables the check.  This is a diagnostic aid for finding code that fails to close
     * the connections it obtains; it does not itself reclaim the connection.
     */
    JDBC_CONNECTION_LEAK_THRESHOLD("jdbcConnectionLeakThreshold", "How long, in milliseconds, a connection may be held by a caller before the connector logs a stack trace of whoever took it out. Zero disables the check. This is a diagnostic aid for finding code that fails to close the connections it obtains; it does not itself reclaim the connection.", DataType.LONG.getDisplayName(), "0"),


    ;

    public final String           name;
    public final String           description;
    public final String           dataType;
    public final String           example;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the request parameter
     * @param description description of the request parameter
     * @param dataType type of value of the request parameter
     * @param example example of the request parameter
     */
    JDBCConfigurationProperty(String name,
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
     * Return the name of the request parameter.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the description of the request parameter.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the data type for the request parameter.
     *
     * @return data type name
     */
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Return an example of the request parameter to help users understand how to set it up.
     *
     * @return example
     */
    public String getExample()
    {
        return example;
    }


    /**
     * Get recognizedConfigurationProperties for the connector.
     *
     * @return list of property names
     */
    public static List<String> getRecognizedConfigurationProperties()
    {
        List<String> recognizedConfigurationProperties = new ArrayList<>();

        for (JDBCConfigurationProperty configurationProperty : JDBCConfigurationProperty.values())
        {
            recognizedConfigurationProperties.add(configurationProperty.getName());
        }
        return recognizedConfigurationProperties;
    }


    /**
     * Retrieve all the defined configuration properties
     *
     * @return list
     */
    public static List<ConfigurationPropertyType> getConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        for (JDBCConfigurationProperty configurationProperty : JDBCConfigurationProperty.values())
        {
            configurationPropertyTypes.add(configurationProperty.getConfigurationPropertyType());
        }

        return configurationPropertyTypes;
    }



    /**
     * Return a summary of this enum to use in a service provider.
     *
     * @return request parameter type
     */
    public ConfigurationPropertyType getConfigurationPropertyType()
    {
        ConfigurationPropertyType requestParameterType = new ConfigurationPropertyType();

        requestParameterType.setName(name);
        requestParameterType.setDescription(description);
        requestParameterType.setDataType(dataType);
        requestParameterType.setExample(example);

        return requestParameterType;
    }

    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "RequestParameter{ name=" + name + "}";
    }
}
