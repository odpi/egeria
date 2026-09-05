/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.resource.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.controls.JDBCConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ffdc.JDBCAuditCode;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ffdc.JDBCErrorCode;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.JDBCDataValue;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * JDBCResourceConnector provides a resource connector to work with JDBC Databases.  A JDBC {@link Connection} carries
 * a single database transaction and must not be used by two threads at once, so the connector dispenses connections
 * from a pool via a data source object.  The data source is a separate object rather than this connector itself
 * because {@link DataSource#getConnection()} clashes with {@link ConnectorBase#getConnection()} - two methods with
 * the same signature cannot differ only by return type.
 * <br><br>
 * The pool is a {@link HikariDataSource}.  Callers therefore obtain a connection for the duration of one unit of work
 * and <b>must close it</b> when that unit of work ends - ideally with try-with-resources:
 * <pre>
 *     try (Connection jdbcConnection = jdbcResourceConnector.getDataSource().getConnection())
 *     {
 *         ... statements ...
 *
 *         jdbcConnection.commit();   // only if the unit of work made changes
 *     }
 * </pre>
 * Closing returns the connection to the pool; it does not close the network connection.  Because the connector runs
 * with auto-commit disabled, the pool rolls back any transaction still open when a connection is returned, so a
 * caller that forgets to commit loses its changes rather than leaving the connection idle-in-transaction.
 * <br><br>
 * A caller that never closes will exhaust the pool.  Set the jdbcConnectionLeakThreshold configuration property to
 * have the pool log a stack trace naming any caller that holds a connection for too long.
 */
public class JDBCResourceConnector extends ConnectorBase implements AuditLoggingComponent
{
    private String                          jdbcDatabaseName   = null;
    private String                          jdbcDatabaseURL    = null;
    private HikariDataSource                jdbcDataSource     = null;
    private final Properties                jdbcConnectionProperties = new Properties();


    private static final Logger log = LoggerFactory.getLogger(JDBCResourceConnector.class);


    /**
     * Receive an audit log object that can be used to record audit log messages.  The caller has initialized it
     * with the correct component description and log destinations.
     *
     * @param auditLog audit log object
     */
    @Override
    public void setAuditLog(AuditLog auditLog)
    {
        this.auditLog = auditLog;
    }


    /**
     * Return the component description that is used by this connector in the audit log.
     *
     * @return id, name, description, wiki page URL.
     */
    @Override
    public ComponentDescription getConnectorComponentDescription()
    {
        if ((this.auditLog != null) && (this.auditLog.getReport() != null))
        {
            return auditLog.getReport().getReportingComponent();
        }

        return null;
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        /*
         * Retrieve the connection string
         */
        Endpoint endpoint = connectionBean.getEndpoint();

        if (endpoint != null)
        {
            jdbcDatabaseURL = endpoint.getNetworkAddress();
        }

        if (jdbcDatabaseURL == null)
        {
            throw new ConnectorCheckedException(JDBCErrorCode.NULL_URL.getMessageDefinition(connectionBean.getDisplayName()),
                                                this.getClass().getName(),
                                                methodName);
        }

        Map<String, Object> configurationProperties = connectionBean.getConfigurationProperties();

        if (configurationProperties != null)
        {
            if (configurationProperties.get(JDBCConfigurationProperty.DATABASE_NAME.getName()) != null)
            {
                jdbcDatabaseName = configurationProperties.get(JDBCConfigurationProperty.DATABASE_NAME.getName()).toString();
            }
        }

        /*
         * Default the database name to the connection URL if an alternative name is not supplied.
         */
        if (jdbcDatabaseName == null)
        {
            jdbcDatabaseName = jdbcDatabaseURL;
        }

        if (configurationProperties != null)
        {
            if (configurationProperties.get(JDBCConfigurationProperty.JDBC_CONNECTION_TIMEOUT.getName()) != null)
            {
                Object connectionTimeoutOption = configurationProperties.get(JDBCConfigurationProperty.JDBC_CONNECTION_TIMEOUT.getName());

                if (connectionTimeoutOption != null)
                {
                    int connectionTimeout = Integer.parseInt(connectionTimeoutOption.toString());

                    /*
                     * Note that this is a class level property and will affect all connectors running in this class loader.
                     */
                    DriverManager.setLoginTimeout(connectionTimeout);
                }
            }

            Object driverManagerClassName = configurationProperties.get(JDBCConfigurationProperty.JDBC_DRIVER_MANAGER_CLASS_NAME.getName());

            if (driverManagerClassName != null)
            {
                try
                {
                    Class.forName(driverManagerClassName.toString());
                }
                catch (ClassNotFoundException error)
                {
                    throw new ConnectorCheckedException(JDBCErrorCode.BAD_DRIVER_MANAGER_CLASS.getMessageDefinition(jdbcDatabaseName,
                                                                                                                    driverManagerClassName.toString(),
                                                                                                                    connectionBean.getDisplayName(),
                                                                                                                    error.getMessage()),
                                                        this.getClass().getName(),
                                                        methodName);
                }
            }

            JDBCConnectionPropertiesUtil.addAdditionalConnectionProperties(configurationProperties.get(JDBCConfigurationProperty.ADDITIONAL_CONNECTION_PROPERTIES.getName()),
                                                                           jdbcConnectionProperties);
        }

        jdbcDataSource = this.createConnectionPool(configurationProperties);
    }


    /**
     * Supply a configuration property value on behalf of this connector.  This allows a connector that embeds a
     * JDBCResourceConnector to pass a matching property from its own configuration straight through, so that a
     * setting such as {@link JDBCConfigurationProperty#JDBC_MAXIMUM_POOL_SIZE} can be tuned where the hosting
     * connector is configured rather than only on the embedded connection.
     * <br><br>
     * This must be called before {@link #start()}, since that is when the pool is built from these properties.
     * <br><br>
     * A value passed down by the host replaces any value already on this connector's own connection.  The value on
     * the connection is typically supplied by a template or content pack and acts as the default; the host's
     * configuration is where a deployment can be tuned, so it has to be able to override that default.  Passing
     * null changes nothing, so a host that does not set the property leaves the connection's own value in place.
     *
     * @param propertyName name of the configuration property
     * @param propertyValue value to use - ignored if null
     */
    public void setConfigurationProperty(String propertyName,
                                         Object propertyValue)
    {
        if ((propertyName == null) || (propertyValue == null))
        {
            return;
        }

        Map<String, Object> configurationProperties = connectionBean.getConfigurationProperties();

        if (configurationProperties == null)
        {
            configurationProperties = new HashMap<>();
        }
        else
        {
            configurationProperties = new HashMap<>(configurationProperties);
        }

        configurationProperties.put(propertyName, propertyValue);

        connectionBean.setConfigurationProperties(configurationProperties);
    }


    /**
     * Build the connection pool for this database.
     *
     * @param configurationProperties the connector's configuration properties - may be null
     * @return configured pool
     */
    private HikariDataSource createConnectionPool(Map<String, Object> configurationProperties)
    {
        HikariConfig poolConfig = new HikariConfig();

        poolConfig.setPoolName(jdbcDatabaseName);
        poolConfig.setJdbcUrl(jdbcDatabaseURL);

        if ((connectionBean.getUserId() != null) && (connectionBean.getClearPassword() != null))
        {
            poolConfig.setUsername(connectionBean.getUserId());
            poolConfig.setPassword(connectionBean.getClearPassword());
        }

        /*
         * Turn on socket-level keepalive before the caller's own properties are applied, so that an explicit
         * setting in additionalConnectionProperties always wins.
         *
         * Without this, a connection whose peer disappears silently - a dropped network, a firewall idle timeout -
         * leaves the pool holding a socket that will never be read from again.  HikariCP calls this out as the
         * cause of a pool that drains to zero and does not recover.  The property name is driver specific, so it is
         * only set for drivers whose spelling of it is known; for anything else, keepalive has to come from the
         * operating system's own TCP settings.
         */
        this.addKeepAliveProperty(poolConfig.getDataSourceProperties());

        /*
         * Any additional driver properties are passed straight through on every connection the pool opens.
         */
        poolConfig.getDataSourceProperties().putAll(jdbcConnectionProperties);

        /*
         * Egeria manages its transactions explicitly, so auto-commit stays off.  This is also what makes the pool
         * roll back an unfinished transaction when a connection is returned.
         */
        poolConfig.setAutoCommit(false);

        poolConfig.setMaximumPoolSize((int) this.getLongConfigurationProperty(configurationProperties,
                                                                             JDBCConfigurationProperty.JDBC_MAXIMUM_POOL_SIZE.getName(),
                                                                             10L));
        poolConfig.setMinimumIdle((int) this.getLongConfigurationProperty(configurationProperties,
                                                                         JDBCConfigurationProperty.JDBC_MINIMUM_IDLE.getName(),
                                                                         1L));
        poolConfig.setConnectionTimeout(this.getLongConfigurationProperty(configurationProperties,
                                                                         JDBCConfigurationProperty.JDBC_CONNECTION_WAIT_TIMEOUT.getName(),
                                                                         30000L));
        poolConfig.setMaxLifetime(this.getLongConfigurationProperty(configurationProperties,
                                                                    JDBCConfigurationProperty.JDBC_MAXIMUM_CONNECTION_LIFETIME.getName(),
                                                                    1800000L));

        /*
         * This is the pool's own liveness probe on idle connections, which is a different mechanism from the
         * socket-level keepalive set above: it retires a connection the database has quietly dropped, rather than
         * keeping the socket alive in the first place.  Both are wanted.  Zero disables it.
         */
        long keepAlive = this.getLongConfigurationProperty(configurationProperties,
                                                          JDBCConfigurationProperty.JDBC_CONNECTION_KEEPALIVE.getName(),
                                                          120000L);
        if (keepAlive > 0)
        {
            poolConfig.setKeepaliveTime(keepAlive);
        }

        long leakThreshold = this.getLongConfigurationProperty(configurationProperties,
                                                              JDBCConfigurationProperty.JDBC_CONNECTION_LEAK_THRESHOLD.getName(),
                                                              0L);
        if (leakThreshold > 0)
        {
            poolConfig.setLeakDetectionThreshold(leakThreshold);
        }

        return new HikariDataSource(poolConfig);
    }


    /**
     * Switch on the JDBC driver's socket-level TCP keepalive, where the property name for this driver is known.
     * <br><br>
     * The property is driver specific and an unrecognised property can be rejected outright by some drivers, so
     * nothing is set for a database whose spelling of it is not known here.  Those deployments need keepalive
     * configured at the operating system level instead, or the property supplied through the
     * additionalConnectionProperties configuration property.
     *
     * @param dataSourceProperties driver properties being assembled for the pool
     */
    private void addKeepAliveProperty(Properties dataSourceProperties)
    {
        final String methodName = "addKeepAliveProperty";

        if (jdbcDatabaseURL == null)
        {
            return;
        }

        String keepAlivePropertyName = null;

        if (jdbcDatabaseURL.startsWith("jdbc:postgresql:"))
        {
            keepAlivePropertyName = "tcpKeepAlive";
        }
        else if (jdbcDatabaseURL.startsWith("jdbc:oracle:"))
        {
            keepAlivePropertyName = "oracle.net.keepAlive";
        }

        if (keepAlivePropertyName != null)
        {
            dataSourceProperties.setProperty(keepAlivePropertyName, "true");

            logRecord(methodName,
                      JDBCAuditCode.CONNECTION_KEEPALIVE_ENABLED.getMessageDefinition(jdbcDatabaseName,
                                                                                      keepAlivePropertyName));
        }
    }


    /**
     * Retrieve a numeric configuration property, falling back to the supplied default if it is absent or unreadable.
     *
     * @param configurationProperties the connector's configuration properties - may be null
     * @param propertyName name of the property to retrieve
     * @param defaultValue value to use when the property is not usable
     * @return configured value or the default
     */
    private long getLongConfigurationProperty(Map<String, Object> configurationProperties,
                                              String              propertyName,
                                              long                defaultValue)
    {
        final String methodName = "getLongConfigurationProperty";

        if (configurationProperties != null)
        {
            Object propertyValue = configurationProperties.get(propertyName);

            if (propertyValue != null)
            {
                try
                {
                    return Long.parseLong(propertyValue.toString());
                }
                catch (NumberFormatException notANumber)
                {
                    logExceptionRecord(methodName,
                                       JDBCAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(jdbcDatabaseName,
                                                                                               notANumber.getClass().getName(),
                                                                                               methodName,
                                                                                               notANumber.getMessage()),
                                       notANumber);
                }
            }
        }

        return defaultValue;
    }


    /**
     * Return the name of the database.
     *
     * @return string name
     */
    public String getDatabaseName()
    {
        return jdbcDatabaseName;
    }


    /**
     * Return the DataSource implementation for this database.
     * This supports creating the connection to the database.
     * Notice that DataSources are created per thread.
     *
     * @return DataSource
     */
    public DataSource getDataSource()
    {
        return jdbcDataSource;
    }


    /**
     * Issue the supplied DDL statements.
     *
     * @param jdbcConnection connection to use
     * @param ddlStatements statements to execute
     * @throws PropertyServerException problem communicating with the database
     */
    public void addDatabaseDefinitions(java.sql.Connection jdbcConnection,
                                       List<String>        ddlStatements) throws PropertyServerException
    {
        if ((ddlStatements != null) && (!ddlStatements.isEmpty()))
        {
            for (String ddlStatement : ddlStatements)
            {
                if (ddlStatement != null)
                {
                    this.issueSQLCommand(jdbcConnection, ddlStatement);
                }
            }
        }
    }


    /**
     * Issue a SQL command that expects no results.
     *
     * @param jdbcConnection connection to use
     * @param sqlCommand command to issue
     * @throws PropertyServerException something went wrong
     */
    public void issueSQLCommand(java.sql.Connection jdbcConnection,
                                String              sqlCommand) throws PropertyServerException
    {
        final String methodName = "issueSQLCommand";

        try (PreparedStatement preparedStatement = jdbcConnection.prepareStatement(sqlCommand))
        {
            log.debug(sqlCommand);

            preparedStatement.execute();
        }
        catch (SQLException sqlException)
        {
            this.rollbackAfterException(jdbcConnection, sqlException);
            throw new PropertyServerException(JDBCErrorCode.UNEXPECTED_SQL_EXCEPTION.getMessageDefinition(jdbcDatabaseName,
                                                                                                          sqlCommand,
                                                                                                          methodName,
                                                                                                          sqlException.getMessage()),
                                              this.getClass().getName(),
                                              methodName,
                                              sqlException);
        }
    }


    /**
     * Issue a rollback on the connection when a SQL error occurs.
     *
     * @param jdbcConnection connection with the error
     * @param sqlException original exception
     */
    private void rollbackAfterException(java.sql.Connection jdbcConnection,
                                        SQLException        sqlException)
    {
        final String methodName = "rollbackAfterException";

        try
        {
            jdbcConnection.rollback();

            super.logRecord(methodName,
                            JDBCAuditCode.ROllBACK_AFTER_EXCEPTION.getMessageDefinition(jdbcDatabaseName,
                                                                                        sqlException.getClass().getName(),
                                                                                        sqlException.getMessage()));
        }
        catch (SQLException rollbackFailed)
        {
            super.logExceptionRecord(methodName,
                                     JDBCAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(jdbcDatabaseName,
                                                                                             rollbackFailed.getClass().getName(),
                                                                                             methodName,
                                                                                             rollbackFailed.getMessage()),
                                     rollbackFailed);
        }
    }


    /**
     * Retrieve the row with the requested identifier and with the latest timestamp.
     *
     * @param jdbcConnection connection to use
     * @param tableName name of the table to query
     * @param identifierColumnName name of the column with the identifier in it
     * @param identifierColumnValue value of the identifier to match on
     * @param timestampColumnName name of the column with the timestamp
     * @param columnNameTypeMap map of resulting column names and values to include in the results
     * @return Map of column names to data values that represent the requested row
     * @throws PropertyServerException there was a problem calling the database
     */
    public Map<String, JDBCDataValue> getLatestRow(java.sql.Connection  jdbcConnection,
                                                   String               tableName,
                                                   String               identifierColumnName,
                                                   String               identifierColumnValue,
                                                   String               timestampColumnName,
                                                   Map<String, Integer> columnNameTypeMap) throws PropertyServerException
    {
        final String methodName = "getLatestRow";

        String sqlCommand = "SELECT * FROM " +
                tableName +
                " WHERE " + identifierColumnName + " = ? AND " +
                timestampColumnName +
                " = (SELECT MAX(" + timestampColumnName + ") FROM " + tableName + " WHERE " + identifierColumnName + " = ?)";

        try
        {
            log.debug(sqlCommand);

            PreparedStatement preparedStatement = jdbcConnection.prepareStatement(sqlCommand);

            preparedStatement.setString(1, identifierColumnValue);
            preparedStatement.setString(2, identifierColumnValue);

            ResultSet resultSet = preparedStatement.executeQuery();

            Map<String, JDBCDataValue> results = this.getRowFromResultSet(resultSet, columnNameTypeMap);

            resultSet.close();
            preparedStatement.close();

            return results;
        }
        catch (SQLException sqlException)
        {
            this.rollbackAfterException(jdbcConnection, sqlException);
            throw new PropertyServerException(JDBCErrorCode.UNEXPECTED_SQL_EXCEPTION.getMessageDefinition(jdbcDatabaseName,
                                                                                                          sqlCommand,
                                                                                                          methodName,
                                                                                                          sqlException.getMessage()),
                                              this.getClass().getName(),
                                              methodName,
                                              sqlException);
        }
    }


    /**
     * Retrieve the row that matches the where clause.
     *
     * @param jdbcConnection connection to use
     * @param tableName name of the table to query
     * @param whereClause condition describing how to match the desired columns
     * @param columnNameTypeMap map of resulting column names and values to include in the results
     * @return row consisting of column names to data values that represent the requested row
     * @throws PropertyServerException there was a problem calling the database
     */
    public Map<String, JDBCDataValue> getMatchingRow(java.sql.Connection  jdbcConnection,
                                                     String               tableName,
                                                     String               whereClause,
                                                     Map<String, Integer> columnNameTypeMap) throws PropertyServerException
    {
        final String methodName = "getMatchingRow";

        String sqlCommand = "SELECT * FROM " + tableName + " WHERE " + whereClause;

        try
        {
            log.debug(sqlCommand);

            PreparedStatement preparedStatement = jdbcConnection.prepareStatement(sqlCommand);

            ResultSet resultSet = preparedStatement.executeQuery();

            Map<String, JDBCDataValue> results = this.getRowFromResultSet(resultSet, columnNameTypeMap);

            resultSet.close();
            preparedStatement.close();

            return results;
        }
        catch (SQLException sqlException)
        {
            this.rollbackAfterException(jdbcConnection, sqlException);
            throw new PropertyServerException(JDBCErrorCode.UNEXPECTED_SQL_EXCEPTION.getMessageDefinition(jdbcDatabaseName,
                                                                                                          sqlCommand,
                                                                                                          methodName,
                                                                                                          sqlException.getMessage()),
                                              this.getClass().getName(),
                                              methodName,
                                              sqlException);
        }
    }


    /**
     * Retrieve the row with the requested identifier and with the latest timestamp.
     *
     * @param jdbcConnection connection to use
     * @param tableName name of the table to query
     * @param whereClause condition describing how to match the desired columns
     * @param columnNameTypeMap map of resulting column names and values to include in the results
     * @return list of rows consisting of column names to data values that represent the requested row
     * @throws PropertyServerException there was a problem calling the database
     */
    public List<Map<String, JDBCDataValue>> getMatchingRows(java.sql.Connection  jdbcConnection,
                                                            String               tableName,
                                                            String               whereClause,
                                                            Map<String, Integer> columnNameTypeMap) throws PropertyServerException
    {
        final String methodName = "getMatchingRows";

        String sqlCommand = "SELECT * FROM " + tableName + " WHERE " + whereClause;

        try
        {
            log.debug(sqlCommand);

            PreparedStatement preparedStatement = jdbcConnection.prepareStatement(sqlCommand);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Map<String, JDBCDataValue>> results = this.getRowsFromResultSet(resultSet, columnNameTypeMap);

            resultSet.close();
            preparedStatement.close();

            return results;
        }
        catch (SQLException sqlException)
        {
            this.rollbackAfterException(jdbcConnection, sqlException);
            throw new PropertyServerException(JDBCErrorCode.UNEXPECTED_SQL_EXCEPTION.getMessageDefinition(jdbcDatabaseName,
                                                                                                          sqlCommand,
                                                                                                          methodName,
                                                                                                          sqlException.getMessage()),
                                              this.getClass().getName(),
                                              methodName,
                                              sqlException);
        }
    }


    /**
     * Retrieve the row with the requested identifier and with the latest timestamp.
     *
     * @param jdbcConnection connection to use
     * @param sqlCommand condition describing how to match the desired columns
     * @param columnNameTypeMap map of resulting column names and values to include in the results
     * @return list of rows consisting of column names to data values that represent the requested row
     * @throws PropertyServerException there was a problem calling the database
     */
    public List<Map<String, JDBCDataValue>> getMatchingRows(java.sql.Connection  jdbcConnection,
                                                            String               sqlCommand,
                                                            Map<String, Integer> columnNameTypeMap) throws PropertyServerException
    {
        final String methodName = "getMatchingRows";

        try
        {
            log.debug(sqlCommand);

            PreparedStatement preparedStatement = jdbcConnection.prepareStatement(sqlCommand);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Map<String, JDBCDataValue>> results = this.getRowsFromResultSet(resultSet, columnNameTypeMap);

            resultSet.close();
            preparedStatement.close();

            return results;
        }
        catch (SQLException sqlException)
        {
            this.rollbackAfterException(jdbcConnection, sqlException);
            throw new PropertyServerException(JDBCErrorCode.UNEXPECTED_SQL_EXCEPTION.getMessageDefinition(jdbcDatabaseName,
                                                                                                          sqlCommand,
                                                                                                          methodName,
                                                                                                          sqlException.getMessage()),
                                              this.getClass().getName(),
                                              methodName,
                                              sqlException);
        }
    }


    /**
     * Retrieve the number of rows in the table.
     *
     * @param jdbcConnection connection to use
     * @param tableName name of the table to query
     * @return number of rows in the named table
     * @throws PropertyServerException there was a problem calling the database
     */
    public int getRowCount(java.sql.Connection  jdbcConnection,
                           String               tableName) throws PropertyServerException
    {
        final String methodName = "getRowCount";

        String sqlCommand = "SELECT COUNT(*) FROM " + tableName;
        log.debug(sqlCommand);

        int rowCount = 0;

        try
        {

            PreparedStatement preparedStatement = jdbcConnection.prepareStatement(sqlCommand);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                rowCount = resultSet.getInt(1); // Get the count from the first column
            }

            resultSet.close();
            preparedStatement.close();
        }
        catch (SQLException sqlException)
        {
            this.rollbackAfterException(jdbcConnection, sqlException);
            throw new PropertyServerException(JDBCErrorCode.UNEXPECTED_SQL_EXCEPTION.getMessageDefinition(jdbcDatabaseName,
                                                                                                          sqlCommand,
                                                                                                          methodName,
                                                                                                          sqlException.getMessage()),
                                              this.getClass().getName(),
                                              methodName,
                                              sqlException);
        }

        return rowCount;
    }


    /**
     * Retrieve the number of rows that would be returned by the supplied SQL query (which should be a
     * "SELECT COUNT(...) FROM ... WHERE ..." style command).  This allows a caller to reuse a WHERE clause built
     * for a row-fetching query, without fetching and materializing every matching row.
     *
     * @param jdbcConnection connection to use
     * @param sqlCommand the full "SELECT COUNT(...)" SQL command to execute
     * @return number of rows matching the supplied SQL command
     * @throws PropertyServerException there was a problem calling the database
     */
    public long countMatchingRows(java.sql.Connection  jdbcConnection,
                                  String               sqlCommand) throws PropertyServerException
    {
        final String methodName = "countMatchingRows";

        log.debug(sqlCommand);

        long rowCount = 0L;

        try
        {
            PreparedStatement preparedStatement = jdbcConnection.prepareStatement(sqlCommand);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                rowCount = resultSet.getLong(1); // Get the count from the first column
            }

            resultSet.close();
            preparedStatement.close();
        }
        catch (SQLException sqlException)
        {
            this.rollbackAfterException(jdbcConnection, sqlException);
            throw new PropertyServerException(JDBCErrorCode.UNEXPECTED_SQL_EXCEPTION.getMessageDefinition(jdbcDatabaseName,
                                                                                                          sqlCommand,
                                                                                                          methodName,
                                                                                                          sqlException.getMessage()),
                                              this.getClass().getName(),
                                              methodName,
                                              sqlException);
        }

        return rowCount;
    }


    /**
     * Retrieve the row with the requested identifier and with the latest timestamp.
     *
     * @param jdbcConnection connection to use
     * @param tableName name of the table to query
     * @param columnNameTypeMap map of resulting column names and values to include in the results
     * @return list of rows consisting of column names to data values that represent the requested row
     * @throws PropertyServerException there was a problem calling the database
     */
    public List<Map<String, JDBCDataValue>> getRows(java.sql.Connection  jdbcConnection,
                                                    String               tableName,
                                                    Map<String, Integer> columnNameTypeMap) throws PropertyServerException
    {
        final String methodName = "getRows";

        String sqlCommand = "SELECT * FROM " + tableName;

        try
        {
            log.debug(sqlCommand);

            PreparedStatement preparedStatement = jdbcConnection.prepareStatement(sqlCommand);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Map<String, JDBCDataValue>> results = this.getRowsFromResultSet(resultSet, columnNameTypeMap);

            resultSet.close();
            preparedStatement.close();

            return results;
        }
        catch (SQLException sqlException)
        {
            this.rollbackAfterException(jdbcConnection, sqlException);
            throw new PropertyServerException(JDBCErrorCode.UNEXPECTED_SQL_EXCEPTION.getMessageDefinition(jdbcDatabaseName,
                                                                                                          sqlCommand,
                                                                                                          methodName,
                                                                                                          sqlException.getMessage()),
                                              this.getClass().getName(),
                                              methodName,
                                              sqlException);
        }
    }



    /**
     * Retrieve the row with the requested identifier and with the latest timestamp.
     *
     * @param jdbcConnection connection to use
     * @param tableName name of the table to query
     * @return list of rows consisting of column names to data values that represent the requested row
     * @throws PropertyServerException there was a problem calling the database
     */
    public List<Map<String, Object>> getUnmappedRows(java.sql.Connection  jdbcConnection,
                                                     String               tableName) throws PropertyServerException
    {
        final String methodName = "getUnmappedRows";

        String sqlCommand = "SELECT * FROM " + tableName;

        List<Map<String, Object>> rows = new ArrayList<>();

        try
        {
            log.debug(sqlCommand);

            PreparedStatement preparedStatement = jdbcConnection.prepareStatement(sqlCommand);

            ResultSet resultSet = preparedStatement.executeQuery();

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();

            /*
             * The query should have returned 0 or more rows
             */
            while (resultSet.next())
            {
                Map<String, Object> row = new HashMap<>();

                for (int i = 0; i < columnCount; i++)
                {
                    row.put(resultSetMetaData.getColumnName(i), resultSet.getObject(i));
                }

                rows.add(row);
            }


            resultSet.close();
            preparedStatement.close();
        }
        catch (SQLException sqlException)
        {
            this.rollbackAfterException(jdbcConnection, sqlException);
            throw new PropertyServerException(JDBCErrorCode.UNEXPECTED_SQL_EXCEPTION.getMessageDefinition(jdbcDatabaseName,
                                                                                                          sqlCommand,
                                                                                                          methodName,
                                                                                                          sqlException.getMessage()),
                                              this.getClass().getName(),
                                              methodName,
                                              sqlException);
        }

        if (! rows.isEmpty())
        {
            return rows;
        }

        return null;
    }


    /**
     * Return whether a table exists or not.
     *
     * @param jdbcConnection connection to use
     * @param tableName name of the table to query
     * @return boolean
     * @throws PropertyServerException there was a problem calling the database
     */
    public boolean doesTableExist(java.sql.Connection  jdbcConnection,
                                  String               tableName) throws PropertyServerException
    {
        final String methodName = "doesTableExist";

        boolean exists;

        try
        {
            DatabaseMetaData metaData = jdbcConnection.getMetaData();
            try (ResultSet resultSet = metaData.getTables(null,
                                                          null,
                                                          tableName,
                                                          new String[]{"TABLE"}))
            {
                exists = resultSet.next();
            }
        }
        catch (SQLException sqlException)
        {
            this.rollbackAfterException(jdbcConnection, sqlException);
            throw new PropertyServerException(JDBCErrorCode.UNEXPECTED_SQL_EXCEPTION.getMessageDefinition(jdbcDatabaseName,
                                                                                                          "metaData.getTables",
                                                                                                          methodName,
                                                                                                          sqlException.getMessage()),
                                              this.getClass().getName(),
                                              methodName,
                                              sqlException);
        }

        return exists;
    }


    /**
     * Return the single row returned from a query.
     *
     * @param resultSet results from the database
     * @param columnNameTypeMap expected structure of the results.
     * @return results map
     * @throws SQLException problem unpacking the results
     */
    private Map<String, JDBCDataValue> getRowFromResultSet(ResultSet            resultSet,
                                                           Map<String, Integer> columnNameTypeMap) throws SQLException
    {
        Map<String, JDBCDataValue> row = null;

        /*
         * The query should have returned 0 or one rows
         */
        if (resultSet.next())
        {
            row = new HashMap<>();

            for (String columnName : columnNameTypeMap.keySet())
            {
                JDBCDataValue dataValue = null;
                int sqlType = columnNameTypeMap.get(columnName);
                switch (sqlType)
                {
                    case Types.VARCHAR   -> dataValue = new JDBCDataValue(resultSet.getString(columnName), sqlType);
                    case Types.ARRAY     -> dataValue = new JDBCDataValue(resultSet.getArray(columnName), sqlType);
                    case Types.BOOLEAN   -> dataValue = new JDBCDataValue(resultSet.getBoolean(columnName), sqlType);
                    case Types.DATE      -> dataValue = new JDBCDataValue(resultSet.getDate(columnName), sqlType);
                    case Types.INTEGER,
                            Types.NUMERIC -> dataValue = new JDBCDataValue(resultSet.getInt(columnName), sqlType);
                    case Types.TIMESTAMP -> dataValue = new JDBCDataValue(resultSet.getTimestamp(columnName), sqlType);
                    case Types.BIGINT    -> dataValue = new JDBCDataValue(resultSet.getBigDecimal(columnName), sqlType);
                }

                if ((dataValue != null) && (dataValue.getDataValue() != null))
                {
                    row.put(columnName, dataValue);
                }
            }
        }

        return row;
    }


    /**
     * Return the single row returned from a query.
     *
     * @param resultSet results from the database
     * @param columnNameTypeMap expected structure of the results.
     * @return results map
     * @throws SQLException problem unpacking the results
     */
    private List<Map<String, JDBCDataValue>> getRowsFromResultSet(ResultSet            resultSet,
                                                                  Map<String, Integer> columnNameTypeMap) throws SQLException
    {
        List<Map<String, JDBCDataValue>> rows = new ArrayList<>();

        /*
         * The query should have returned 0 or more rows
         */
        while (resultSet.next())
        {
            Map<String, JDBCDataValue> row = new HashMap<>();

            for (String columnName : columnNameTypeMap.keySet())
            {
                JDBCDataValue dataValue = null;
                int sqlType = columnNameTypeMap.get(columnName);
                switch (sqlType)
                {
                    case Types.VARCHAR   -> dataValue = new JDBCDataValue(resultSet.getString(columnName), sqlType);
                    case Types.ARRAY     -> dataValue = new JDBCDataValue(resultSet.getArray(columnName), sqlType);
                    case Types.BOOLEAN   -> dataValue = new JDBCDataValue(resultSet.getBoolean(columnName), sqlType);
                    case Types.DATE      -> dataValue = new JDBCDataValue(resultSet.getDate(columnName), sqlType);
                    case Types.INTEGER,
                            Types.NUMERIC -> dataValue = new JDBCDataValue(resultSet.getInt(columnName), sqlType);
                    case Types.TIMESTAMP -> dataValue = new JDBCDataValue(resultSet.getTimestamp(columnName), sqlType);
                    case Types.BIGINT    -> dataValue = new JDBCDataValue(resultSet.getBigDecimal(columnName), sqlType);
                }

                if ((dataValue != null) && (dataValue.getDataValue() != null))
                {
                    row.put(columnName, dataValue);
                }
            }

            rows.add(row);
        }

        if (! rows.isEmpty())
        {
            return rows;
        }

        return null;
    }


    /**
     * Prepare an INSERT SQL statement with all the columns for the new row filled out.
     *
     * @param jdbcConnection connection to use
     * @param tableName name of the table where the row is to be added
     * @param columnNameValueMap column names, values and types
     * @throws PropertyServerException problem executing the command
     */
    public void insertRowIntoTable(java.sql.Connection        jdbcConnection,
                                   String                     tableName,
                                   Map<String, JDBCDataValue> columnNameValueMap) throws PropertyServerException
    {
        final String methodName = "insertRowIntoTable";

        String sqlCommand = "INSERT INTO " + tableName + this.getInsertColumnList(columnNameValueMap) + " ON CONFLICT DO NOTHING";

        try
        {
            log.debug(sqlCommand);

            PreparedStatement preparedStatement = jdbcConnection.prepareStatement(sqlCommand);

            int parameterIndex = 1;
            for (String columnName : columnNameValueMap.keySet())
            {
                JDBCDataValue jdbcDataValue = columnNameValueMap.get(columnName);

                if (jdbcDataValue.getScaleOrLength() == 0)
                {
                    preparedStatement.setObject(parameterIndex,
                                                jdbcDataValue.getDataValue(),
                                                jdbcDataValue.getTargetSQLType());
                }
                else
                {
                    preparedStatement.setObject(parameterIndex,
                                                jdbcDataValue.getDataValue(),
                                                jdbcDataValue.getTargetSQLType(),
                                                jdbcDataValue.getScaleOrLength());
                }

                parameterIndex++;
            }

            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 1)
            {
                logRecord(methodName,
                          JDBCAuditCode.UNEXPECTED_ROW_COUNT_FROM_DATABASE.getMessageDefinition(jdbcDatabaseName,
                                                                                                Integer.toString(rowsInserted),
                                                                                                sqlCommand));
            }

            preparedStatement.close();
        }
        catch (SQLException sqlException)
        {
            this.rollbackAfterException(jdbcConnection, sqlException);
            throw new PropertyServerException(JDBCErrorCode.UNEXPECTED_SQL_EXCEPTION.getMessageDefinition(jdbcDatabaseName,
                                                                                                          sqlCommand,
                                                                                                          methodName,
                                                                                                          sqlException.getMessage()),
                                              this.getClass().getName(),
                                              methodName,
                                              sqlException);
        }
    }


    /**
     * Prepare an INSERT SQL statement with all the columns for each of the new rows filled out.
     *
     * @param jdbcConnection connection to use
     * @param tableName name of the table where the row is to be added
     * @param rows list of column names, values and types
     * @throws PropertyServerException problem executing the command
     */
    public void insertRowsIntoTable(java.sql.Connection              jdbcConnection,
                                    String                           tableName,
                                    List<Map<String, JDBCDataValue>> rows) throws PropertyServerException
    {
        if (rows != null)
        {
            for (Map<String, JDBCDataValue> row : rows)
            {
                insertRowIntoTable(jdbcConnection, tableName, row);
            }
        }
    }


    /**
     * Return the part of the SQL INSERT command that includes the column names
     *
     * @param columnNameValueMap column names, values and types
     * @return part of the SQL INSERT statement
     */
    private String  getInsertColumnList(Map<String, JDBCDataValue> columnNameValueMap)
    {
        return " (" + getColumnNames(columnNameValueMap) + ") values (" + getPlaceholders(columnNameValueMap.size()) + ")";
    }


    /**
     * Return a comma separated list of column names
     *
     * @param columnNameValueMap column names, values and types
     * @return list
     */
    private String getColumnNames(Map<String, JDBCDataValue> columnNameValueMap)
    {
        StringBuilder sqlFragment = new StringBuilder();
        boolean firstColumn = true;

        for (String columnName : columnNameValueMap.keySet())
        {
            if (! firstColumn)
            {
                sqlFragment.append(", ");
            }
            else
            {
                firstColumn = false;
            }

            sqlFragment.append(columnName);
        }

        return sqlFragment.toString();
    }


    /**
     * Return the list of comma separated question marks that are the placeholders for the SQL INSERT command.
     *
     * @param numberOfColumns number of columns in the table
     * @return list of common separated question marks
     */
    private String getPlaceholders(int numberOfColumns)
    {
        StringBuilder sqlFragment = new StringBuilder();

        for (int i=0; i<numberOfColumns; i++)
        {
            sqlFragment.append("?");

            if ((i + 1) < numberOfColumns)
            {
                sqlFragment.append(",");
            }
        }

        return sqlFragment.toString();
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     */
    @Override
    public void disconnect() throws ConnectorCheckedException
    {
        /*
         * This disconnects any embedded connections such as secrets connectors.
         */
        super.disconnectConnectors(this.embeddedConnectors);

        /*
         * This ensures the connections for each requested data source are closed before the connector quits.
         */
        this.disconnectKnownDataSources();

        /*
         * Now the superclass
         */
        super.disconnect();
    }


    /**
     * This shuts down the connection pool, closing every connection it holds, before the connector quits.
     */
    private synchronized void disconnectKnownDataSources()
    {
        final String methodName = "disconnectKnownDataSources";

        if (jdbcDataSource != null)
        {
            logRecord(methodName,
                      JDBCAuditCode.CONNECTOR_STOPPING.getMessageDefinition(jdbcDatabaseName,
                                                                            Integer.toString(jdbcDataSource.getHikariPoolMXBean() == null ? 0 : jdbcDataSource.getHikariPoolMXBean().getTotalConnections())));

            try
            {
                jdbcDataSource.close();
            }
            catch (Exception error)
            {
                /*
                 * Ignore error - in shutdown and the pool may already be closed.
                 */
            }
        }
    }


}
