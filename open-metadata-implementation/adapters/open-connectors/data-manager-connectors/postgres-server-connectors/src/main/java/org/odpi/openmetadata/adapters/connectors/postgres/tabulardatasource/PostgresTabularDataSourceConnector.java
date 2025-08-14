/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.tabulardatasource;

import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.postgres.ffdc.PostgresAuditCode;
import org.odpi.openmetadata.adapters.connectors.postgres.ffdc.PostgresErrorCode;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.TabularColumnDescription;
import org.odpi.openmetadata.frameworks.connectors.TabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;


/**
 * PostgresTabularDataSourceConnector works with structured files to retrieve simple tables of data.
 */
public class PostgresTabularDataSourceConnector extends ConnectorBase implements TabularDataSource
{
    /*
     * Variables used for logging and debug.
     */
    private static final Logger log = LoggerFactory.getLogger(PostgresTabularDataSourceConnector.class);

    private String tableName = null;
    private JDBCResourceConnector jdbcResourceConnector = null;
    private java.sql.Connection   databaseConnection = null;


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws UserNotAuthorizedException, ConnectorCheckedException
    {
        super.start();

        final String methodName = "start";

        tableName = super.getStringConfigurationProperty(PostgresConfigurationProperty.TABLE_NAME.getName(),
                                                         connectionBean.getConfigurationProperties(),
                                                         "data");

        try
        {
            jdbcResourceConnector = this.getDatabaseConnection();
            databaseConnection = jdbcResourceConnector.getDataSource().getConnection();
        }
        catch (Exception exception)
        {
            auditLog.logException(methodName,
                                  PostgresAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
                                                                                              exception.getClass().getName(),
                                                                                              methodName,
                                                                                              exception.getMessage()),
                                  exception);

            throw new ConnectorCheckedException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
                                                                                                            exception.getClass().getName(),
                                                                                                            methodName,
                                                                                                            exception.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                exception);
        }
    }


    /**
     * Retrieve the embedded JDBC Connector.
     *
     * @return connector or exception
     * @throws ConnectorCheckedException no working JDBC connector
     */
    private JDBCResourceConnector getDatabaseConnection() throws ConnectorCheckedException
    {
        final String methodName = "getDatabaseConnection";

        if ((embeddedConnectors != null) && (!embeddedConnectors.isEmpty()))
        {
            for (Connector embeddedConnector : embeddedConnectors)
            {
                if (embeddedConnector instanceof JDBCResourceConnector jdbcResourceConnector)
                {
                    try
                    {
                        if (! jdbcResourceConnector.isActive())
                        {
                            jdbcResourceConnector.start();
                        }

                        return jdbcResourceConnector;
                    }
                    catch (Exception exception)
                    {
                        auditLog.logException(methodName,
                                              PostgresAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
                                                                                                          exception.getClass().getName(),
                                                                                                          methodName,
                                                                                                          exception.getMessage()),
                                              exception);

                        throw new ConnectorCheckedException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
                                                                                                                        exception.getClass().getName(),
                                                                                                                        methodName,
                                                                                                                        exception.getMessage()),
                                                            this.getClass().getName(),
                                                            methodName,
                                                            exception);
                    }
                }
            }
        }

        throw new ConnectorCheckedException(PostgresErrorCode.NO_DATABASE_CONNECTION.getMessageDefinition(connectionBean.getDisplayName()),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Return the number of records in the file.  This is achieved by scanning the file and counting the records -
     * not recommended for very large files.
     *
     * @return count
     * @throws ConnectorCheckedException problem accessing the data
     */
    @Override
    public long getRecordCount() throws ConnectorCheckedException
    {
        final String  methodName = "getRecordCount";

        try
        {
            return jdbcResourceConnector.getRowCount(databaseConnection, tableName);
        }
        catch (Exception exception)
        {
            auditLog.logException(methodName,
                                  PostgresAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
                                                                                              exception.getClass().getName(),
                                                                                              methodName,
                                                                                              exception.getMessage()),
                                  exception);

            throw new ConnectorCheckedException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
                                                                                                            exception.getClass().getName(),
                                                                                                            methodName,
                                                                                                            exception.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                exception);
        }
    }


    /**
     * Return the list of column names associated with this data source.
     *
     * @return a list of column descriptions or null if not available.
     * @throws ConnectorCheckedException there is a problem accessing the data
     */
    @Override
    public List<TabularColumnDescription> getColumnDescriptions() throws ConnectorCheckedException
    {
        final String methodName = "getColumnDescriptions";

        try
        {

        }
        catch (Exception exception)
        {
            auditLog.logException(methodName,
                                  PostgresAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
                                                                                              exception.getClass().getName(),
                                                                                              methodName,
                                                                                              exception.getMessage()),
                                  exception);

            throw new ConnectorCheckedException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
                                                                                                            exception.getClass().getName(),
                                                                                                            methodName,
                                                                                                            exception.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                exception);
        }

        return null;
    }


    /**
     * Return the requested data record.  The first record is record 0.  If the first line of the file is the column
     * names then record 0 is the line following the column names.
     *
     * @param dataRecordNumber long
     * @return List of strings, each string is the value from the column.
     * @throws ConnectorCheckedException there is a problem accessing the data
     */
    public List<String> readRecord(long dataRecordNumber) throws ConnectorCheckedException
    {
        final String  methodName = "readRecord";

        try
        {

        }
        catch (Exception exception)
        {
            auditLog.logException(methodName,
                                  PostgresAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
                                                                                              exception.getClass().getName(),
                                                                                              methodName,
                                                                                              exception.getMessage()),
                                  exception);

            throw new ConnectorCheckedException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
                                                                                                            exception.getClass().getName(),
                                                                                                            methodName,
                                                                                                            exception.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                exception);
        }

        return null;
    }


    /**
     * Set up the columns associated with this tabular data source.  These are stored in the first record of the file.
     * The rest of the file is cleared.
     *
     * @param columnDescriptions a list of column descriptions
     * @throws ConnectorCheckedException there is a problem accessing the data
     */
    @Override
    public void setColumnDescriptions(List<TabularColumnDescription> columnDescriptions) throws ConnectorCheckedException
    {
        final String methodName = "setColumnDescriptions";

        try
        {

            List<Map<String, Object>> allData = jdbcResourceConnector.getUnmappedRows(databaseConnection, tableName);

        }
        catch (Exception exception)
        {
            auditLog.logException(methodName,
                                  PostgresAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
                                                                                              exception.getClass().getName(),
                                                                                              methodName,
                                                                                              exception.getMessage()),
                                  exception);

            throw new ConnectorCheckedException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
                                                                                                            exception.getClass().getName(),
                                                                                                            methodName,
                                                                                                            exception.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                exception);
        }
    }


    /**
     * Write the requested data record.  The first data record is record 0.
     * This process reads the entire file, inserts the record in the right place and writes it out again.
     *
     * @param requestedRowNumber  long
     * @param dataValues Map of column descriptions to strings, each string is the value for the column.
     * @throws ConnectorCheckedException there is a problem accessing the data.
     */
    @Override
    public void writeRecord(long requestedRowNumber, List<String> dataValues) throws ConnectorCheckedException
    {
        final String methodName = "writeRecord";

        try
        {

        }
        catch (Exception exception)
        {
            auditLog.logException(methodName,
                                  PostgresAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
                                                                                              exception.getClass().getName(),
                                                                                              methodName,
                                                                                              exception.getMessage()),
                                  exception);

            throw new ConnectorCheckedException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
                                                                                                            exception.getClass().getName(),
                                                                                                            methodName,
                                                                                                            exception.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                exception);
        }
    }


    /**
     * Write the requested data record to the end of the data source.
     *
     * @param dataValues Map of column descriptions to strings, each string is the value for the column.
     * @throws ConnectorCheckedException there is a problem accessing the data.
     */
    @Override
    public void appendRecord(List<String> dataValues) throws ConnectorCheckedException
    {
        final String methodName = "appendRecord";

        try
        {
            // todo add column value map
            jdbcResourceConnector.insertRowIntoTable(databaseConnection,
                                                     tableName,
                                                     null);
        }
        catch (Exception exception)
        {
            auditLog.logException(methodName,
                                  PostgresAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
                                                                                              exception.getClass().getName(),
                                                                                              methodName,
                                                                                              exception.getMessage()),
                                  exception);

            throw new ConnectorCheckedException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
                                                                                                            exception.getClass().getName(),
                                                                                                            methodName,
                                                                                                            exception.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                exception);
        }
    }



    /**
     * Close the file
     */
    public void disconnect()
    {
        try
        {
            super.disconnect();
        }
        catch (Exception  exec)
        {
            log.debug("Ignoring unexpected exception " + exec.getClass().getSimpleName() + " with message " + exec.getMessage());
        }

        log.debug("Closing Structured File Store");
    }
}