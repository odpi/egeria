/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.resource.jdbc;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ffdc.JDBCAuditCode;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ffdc.JDBCErrorCode;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.JDBCDataValue;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.VirtualConnectorExtension;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JDBCResourceConnector provides basic implementation of {@link DataSource} interface in order to get a {@link Connection} to
 * target database. This is done via a static inner class, since {@link DataSource#getConnection()} clashes with
 * {@link ConnectorBase#getConnection()}.
 * <br><br>
 * The DataSource can be used directly.  There are also selected methods to issue common SQL statements to the database.
 */
public class JDBCResourceConnector extends ConnectorBase implements AuditLoggingComponent,
                                                                    VirtualConnectorExtension
{
    private AuditLog                        auditLog           = null;
    private String                          jdbcDatabaseName   = null;
    private String                          jdbcDatabaseURL    = null;
    private List<Connector>                 embeddedConnectors = null;

    private final List<JDBCConnectorAsDataSource> knownDataSources   = new ArrayList<>();


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
     * Set up the list of connectors that this virtual connector will use to support its interface.
     * The connectors are initialized waiting to start.  When start() is called on the
     * virtual connector, it needs to pass start() to each of the embedded connectors. Similarly for
     * disconnect().
     *
     * @param embeddedConnectors  list of connectors
     */
    @Override
    public void initializeEmbeddedConnectors(List<Connector> embeddedConnectors)
    {
        this.embeddedConnectors = embeddedConnectors;
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public synchronized void start() throws ConnectorCheckedException
    {
        super.start();

        final String methodName = "start";

        /*
         * Retrieve the connection string
         */
        EndpointProperties endpoint = connectionProperties.getEndpoint();

        if (endpoint != null)
        {
            jdbcDatabaseURL = endpoint.getAddress();
        }

        if (jdbcDatabaseURL == null)
        {
            throw new ConnectorCheckedException(JDBCErrorCode.NULL_URL.getMessageDefinition(connectionProperties.getConnectionName()),
                                                this.getClass().getName(),
                                                methodName);
        }

        Map<String, Object> configurationProperties = connectionProperties.getConfigurationProperties();

        if (configurationProperties != null)
        {
            if (configurationProperties.get(JDBCResourceConnectorProvider.JDBC_DATABASE_NAME) != null)
            {
                jdbcDatabaseName = configurationProperties.get(JDBCResourceConnectorProvider.JDBC_DATABASE_NAME).toString();
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
            if (configurationProperties.get(JDBCResourceConnectorProvider.JDBC_CONNECTION_TIMEOUT) != null)
            {
                Object connectionTimeoutOption = configurationProperties.get(JDBCResourceConnectorProvider.JDBC_CONNECTION_TIMEOUT);

                if (connectionTimeoutOption != null)
                {
                    int connectionTimeout = Integer.parseInt(connectionTimeoutOption.toString());

                    /*
                     * Note that this is a class level property and will affect all connectors running in this class loader.
                     */
                    DriverManager.setLoginTimeout(connectionTimeout);
                }
            }

            Object driverManagerClassName = configurationProperties.get(JDBCResourceConnectorProvider.JDBC_DRIVER_MANAGER_CLASS_NAME);

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
                                                                                                                    connectionProperties.getConnectionName(),
                                                                                                                    error.getMessage()),
                                                        this.getClass().getName(),
                                                        methodName);
                }
            }
        }
    }


    /**
     * Return the DataSource implementation for this database.  This supports creating the connection to the database.
     *
     * @return DataSource
     */
    public DataSource getDataSource()
    {
        JDBCConnectorAsDataSource dataSource = new JDBCConnectorAsDataSource(jdbcDatabaseName, auditLog);

        this.knownDataSources.add(dataSource);

        return dataSource;
    }


    /**
     * Retrieve the row with the requested identifier and with the latest timestamp.
     *
     * @param jdbcConnection connection to the database
     * @param tableName name of the table to query
     * @param identifierColumnName name of the column with the identifier in it
     * @param identifierColumnValue value of the identifier to match on
     * @param timestampColumnName name of the column with the timestamp
     * @param columnNameTypeMap map of resulting column names and values to include in the results
     * @return Map of column names to data values that represent the requested row
     * @throws SQLException there was a problem calling the database
     */
    public Map<String, JDBCDataValue> getLatestRow(Connection           jdbcConnection,
                                                   String               tableName,
                                                   String               identifierColumnName,
                                                   String               identifierColumnValue,
                                                   String               timestampColumnName,
                                                   Map<String, Integer> columnNameTypeMap) throws SQLException
    {
        String sqlCommand = "SELECT * FROM " +
                                    tableName +
                                    " WHERE " + identifierColumnName + " = ? AND " +
                                    timestampColumnName +
                                    " = (SELECT MAX(" + timestampColumnName + ") FROM " + tableName + " WHERE " + identifierColumnName + " = ?)";

        PreparedStatement preparedStatement = jdbcConnection.prepareStatement(sqlCommand);

        preparedStatement.setString(1, identifierColumnValue);
        preparedStatement.setString(2, identifierColumnValue);

        ResultSet resultSet = preparedStatement.executeQuery();
        Map<String, JDBCDataValue> results = null;

        /*
         * The query should have returned 0 or one rows
         */
        if (resultSet.next())
        {
            results = new HashMap<>();

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
                    case Types.INTEGER   -> dataValue = new JDBCDataValue(resultSet.getInt(columnName), sqlType);
                    case Types.TIMESTAMP -> dataValue = new JDBCDataValue(resultSet.getTimestamp(columnName), sqlType);
                }

                if ((dataValue != null) && (dataValue.getDataValue() != null))
                {
                    results.put(columnName, dataValue);
                }
            }
        }

        resultSet.close();
        preparedStatement.close();

        return results;
    }


    /**
     * Prepare an INSERT SQL statement with all the columns for the new row filled out.
     *
     * @param jdbcConnection connection to send the request
     * @param tableName name of the table where the row is to be added
     * @param columnNameValueMap column names, values and types
     * @throws SQLException problem executing the command
     */
    public void insertRowIntoTable(Connection                 jdbcConnection,
                                   String                     tableName,
                                   Map<String, JDBCDataValue> columnNameValueMap) throws SQLException
    {
        final String methodName = "insertRowIntoTable";

        String sqlCommand = "INSERT INTO " + tableName + this.getInsertColumnList(columnNameValueMap) + " ON CONFLICT DO NOTHING";

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

        if ((rowsInserted > 1) && (auditLog != null))
        {
            auditLog.logMessage(methodName,
                                JDBCAuditCode.UNEXPECTED_ROW_COUNT_FROM_DATABASE.getMessageDefinition(jdbcDatabaseName,
                                                                                                      Integer.toString(rowsInserted),
                                                                                                      sqlCommand));
        }

        preparedStatement.close();
    }


    /**
     * Return the part of the SQL INSERT command that includes the column names
     *
     * @param columnNameValueMap column names, values and types
     * @return part of the SQL INSERT statement
     */
    private String getInsertColumnList(Map<String, JDBCDataValue> columnNameValueMap)
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
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public  synchronized void disconnect() throws ConnectorCheckedException
    {
        /*
         * This disconnects any embedded connections such as secrets connectors.
         */
        super.disconnectConnectors(this.embeddedConnectors);

        /*
         * This ensures the connections for each requested data source are closed before the connector quits.
         */
        for (JDBCConnectorAsDataSource dataSource : this.knownDataSources)
        {
            try
            {
                dataSource.disconnect();
            }
            catch (Exception error)
            {
                /*
                 * Ignore error - in shutdown and the caller may have closed the connection already.
                 */
            }
        }

        super.disconnect();
    }


    /**
     * JDBCConnectorAsDataSource provides the inner class for DataSource.
     */
    private class JDBCConnectorAsDataSource implements DataSource
    {
        private final String   databaseName;
        private final AuditLog auditLog;

        private final List<Connection> knownConnections = new ArrayList<>();


        /**
         * Construct the data source wrapper.
         *
         * @param databaseName database name to use in messages
         * @param auditLog logging destination
         */
        JDBCConnectorAsDataSource(String   databaseName,
                                  AuditLog auditLog)
        {
            this.databaseName = databaseName;
            this.auditLog = auditLog;
        }


        /**
         * Attempts to establish a connection with the data source that this DataSource object represents.
         *
         * @return a connection to the data source
         * @throws SQLException if a database access error occurs
         */
        @Override
        public Connection getConnection() throws SQLException
        {
            final String methodName = "dataSource.getConnection";

            try
            {
                Connection jdbcConnection;

                if ((connectionProperties.getUserId() == null) || (connectionProperties.getClearPassword() == null))
                {
                    jdbcConnection = DriverManager.getConnection(connectionProperties.getEndpoint().getAddress());
                }
                else
                {
                    jdbcConnection = DriverManager.getConnection(connectionProperties.getEndpoint().getAddress(),
                                                                 connectionBean.getUserId(),
                                                                 connectionBean.getClearPassword());
                }

                if (jdbcConnection != null)
                {
                    this.knownConnections.add(jdbcConnection);
                }

                if (auditLog != null)
                {
                    auditLog.logMessage(methodName, JDBCAuditCode.CONNECTOR_CONNECTED_TO_DATABASE.getMessageDefinition(databaseName));
                }

                return jdbcConnection;
            }
            catch (SQLException error)
            {
                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          JDBCAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(databaseName,
                                                                                                  error.getClass().getName(),
                                                                                                  methodName,
                                                                                                  error.getMessage()),
                                          error);
                }

                throw error;
            }
        }


        /**
         * Attempts to establish a connection with the data source that this DataSource object represents.
         *
         * @param username the username for connecting to the database that overrides the configured userId
         * @param password the password for connecting to the database that overrides the configured clearPassword
         * @return a connection to the data source
         * @throws SQLException if a database access error occurs
         */
        @Override
        public Connection getConnection(String username, String password) throws SQLException
        {
            final String methodName = "dataSource.getConnection(supplied security)";

            try
            {
                Connection jdbcConnection = DriverManager.getConnection(connectionBean.getEndpoint().getAddress(), username, password);

                if (auditLog != null)
                {
                    auditLog.logMessage(methodName, JDBCAuditCode.CONNECTOR_CONNECTED_TO_DATABASE.getMessageDefinition(databaseName));
                }

                return jdbcConnection;
            }
            catch (SQLException error)
            {
                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          JDBCAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(databaseName,
                                                                                                  error.getClass().getName(),
                                                                                                  methodName,
                                                                                                  error.getMessage()),
                                          error);
                }

                throw error;
            }
        }


        /**
         * Retrieves the log writer for this DataSource object.
         * The log writer is a character output stream to which all logging and tracing messages for this data source will be printed.
         * This includes messages printed by the methods of this object, messages printed by methods of other objects manufactured by this object,
         * and so on. Messages printed to a data source specific log writer are not printed to the log writer associated with the
         * java.sql.DriverManager class. When a DataSource object is created, the log writer is initially null; in other words,
         * the default is for logging to be disabled.
         *
         * @return log writer
         * @throws SQLException if a database access error occurs
         */
        @Override
        public PrintWriter getLogWriter() throws SQLException
        {
            return DriverManager.getLogWriter();
        }


        /**
         * Sets the log writer for this DataSource object to the given java.io.PrintWriter object.
         * The log writer is a character output stream to which all logging and tracing messages for this data source will be printed.
         * This includes messages printed by the methods of this object, messages printed by methods of other objects manufactured by this object,
         * and so on. Messages printed to a data source - specific log writer are not printed to the log writer associated with the
         * java.sql.DriverManager class. When a DataSource object is created the log writer is initially null; in other words,
         * the default is for logging to be disabled
         *
         * @param out the new log writer; to disable logging, set to null
         * @throws SQLException if a database access error occurs
         */
        @Override
        public void setLogWriter(PrintWriter out) throws SQLException
        {
            DriverManager.setLogWriter(out);
        }


        /**
         * Sets the maximum time in seconds that this data source will wait while attempting to connect to a database.
         * A value of zero specifies that the timeout is the default system timeout if there is one; otherwise, it specifies that there is no timeout.
         * When a DataSource object is created, the login timeout is initially zero.
         *
         * @param seconds the data source login time limit
         * @throws SQLException if a database access error occurs
         */
        @Override
        public void setLoginTimeout(int seconds) throws SQLException
        {
            DriverManager.setLoginTimeout(seconds);
        }


        /**
         * Gets the maximum time in seconds that this data source can wait while attempting to connect to a database.
         * A value of zero means that the timeout is the default system timeout if there is one; otherwise, it means that there is no timeout.
         * When a DataSource object is created, the login timeout is initially zero.
         *
         * @return seconds
         * @throws SQLException if a database access error occurs
         */
        @Override
        public int getLoginTimeout() throws SQLException
        {
            return DriverManager.getLoginTimeout();
        }


        /**
         * Return the parent Logger of all the Loggers used by this data source. This should be the Logger farthest from the root Logger
         * that is still an ancestor of all the Loggers used by this data source. Configuring this Logger will affect all the
         * log messages generated by the data source. In the worst case, this may be the root Logger.
         *
         * @return the parent Logger for this data source
         * @throws SQLFeatureNotSupportedException – if the data source does not use java.util.logging
         */
        @Override
        public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException
        {
            throw new SQLFeatureNotSupportedException();
        }


        /**
         * Returns an object that implements the given interface to allow access to non-standard methods, or standard methods not exposed by
         * the proxy. If the receiver implements the interface then the result is the receiver or a proxy for the receiver.
         * If the receiver is a wrapper and the wrapped object implements the interface then the result is the wrapped object or a proxy for the
         * wrapped object. Otherwise, return the result of calling unwrap recursively on the wrapped object or a proxy for that result.
         * If the receiver is not a wrapper and does not implement the interface, then an SQLException is thrown.
         *
         * @param requestedInterface A Class defining an interface that the result must implement.
         * @return an object that implements the interface. Maybe a proxy for the actual implementing object.
         * @param <T> class
         * @throws SQLException If no object found that implements the interface
         */
        @Override
        public <T> T unwrap(Class<T> requestedInterface) throws SQLException
        {
            return null;
        }


        /**
         * Returns true if this either implements the interface argument or is directly or indirectly a wrapper for an object that does.
         * Returns false otherwise. If this implements the interface then return true, else if this is a wrapper then return the result of
         * recursively calling isWrapperFor on the wrapped object. If this does not implement the interface and is not a wrapper,
         * return false. This method should be implemented as a low-cost operation compared to unwrap so that callers can use this method to avoid
         * expensive unwrap calls that may fail. If this method returns true then calling unwrap with the same argument should succeed.
         *
         * @param requestedInterface a Class defining an interface.
         * @return true if this implements the interface or directly or indirectly wraps an object that does
         * @throws SQLException if an error occurs while determining whether this is a wrapper for an object with the given interface.
         */
        @Override
        public boolean isWrapperFor(Class<?> requestedInterface) throws SQLException
        {
            return false;
        }


        /**
         * Free up any connections held since the data source is no longer needed.
         */
        public  void disconnect()
        {
            final String methodName = "disconnect";

            if (auditLog != null)
            {
                String numberOfConnections = "zero";

                if (! knownConnections.isEmpty())
                {
                    numberOfConnections = Integer.toString(knownConnections.size());
                }

                auditLog.logMessage(methodName, JDBCAuditCode.CONNECTOR_STOPPING.getMessageDefinition(jdbcDatabaseName, numberOfConnections));
            }

            for (Connection connection : this.knownConnections)
            {
                try
                {
                    if (! connection.isClosed())
                    {
                        connection.close();
                    }
                }
                catch (Exception error)
                {
                    // Ignore error - in shutdown and the connection may be in error already.
                }
            }
        }
    }
}
