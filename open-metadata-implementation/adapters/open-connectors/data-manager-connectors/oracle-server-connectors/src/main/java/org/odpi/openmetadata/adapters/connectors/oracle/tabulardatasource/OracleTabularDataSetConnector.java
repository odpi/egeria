/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.oracle.tabulardatasource;

import org.odpi.openmetadata.adapters.connectors.oracle.controls.OracleConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.oracle.ffdc.OracleAuditCode;
import org.odpi.openmetadata.adapters.connectors.oracle.ffdc.OracleErrorCode;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.oracle.OracleSchemaDDL;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.oracle.OracleTable;
import org.odpi.openmetadata.frameworks.connectors.*;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularColumnDescription;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.WritableTabularDataSource;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;


/**
 * OracleTabularDataSourceConnector works with structured files to retrieve simple tables of data.
 */
public class OracleTabularDataSetConnector extends ConnectorBase implements WritableTabularDataSource
{
    /*
     * Variables used for logging and debug.
     */
    private static final Logger log = LoggerFactory.getLogger(OracleTabularDataSetConnector.class);

    protected String tableName         = null; // stored in snake case
    protected String tableDescription  = null;
    private   String schemaName        = "tabular_data";
    private   String schemaDescription = null;

    private JDBCResourceConnector jdbcResourceConnector = null;
    private java.sql.Connection   databaseConnection    = null;

    protected final PropertyHelper propertyHelper = new PropertyHelper();


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws UserNotAuthorizedException, ConnectorCheckedException
    {
        super.start();

        final String methodName = "start";

        tableName = super.getStringConfigurationProperty(OracleConfigurationProperty.TABLE_NAME.getName(),
                                                          connectionBean.getConfigurationProperties(),
                                                          "data");

        tableDescription = super.getStringConfigurationProperty(OracleConfigurationProperty.TABLE_DESCRIPTION.getName(),
                                                                 connectionBean.getConfigurationProperties(),
                                                                 null);

        schemaName = super.getStringConfigurationProperty(OracleConfigurationProperty.SCHEMA_NAME.getName(),
                                                           connectionBean.getConfigurationProperties(),
                                                           schemaName);

        schemaDescription = super.getStringConfigurationProperty(OracleConfigurationProperty.SCHEMA_DESCRIPTION.getName(),
                                                                  connectionBean.getConfigurationProperties(),
                                                                  schemaDescription);

        try
        {
            jdbcResourceConnector = this.getDatabaseConnection();
            databaseConnection = jdbcResourceConnector.getDataSource().getConnection();
        }
        catch (Exception exception)
        {
            auditLog.logException(methodName,
                                  OracleAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
                                                                                          exception.getClass().getName(),
                                                                                          methodName,
                                                                                          exception.getMessage()),
                                  exception);

            throw new ConnectorCheckedException(OracleErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
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
                if (embeddedConnector instanceof JDBCResourceConnector jdbcConnector)
                {
                    try
                    {
                        if (! jdbcConnector.isActive())
                        {
                            jdbcConnector.start();
                        }

                        return jdbcConnector;
                    }
                    catch (Exception exception)
                    {
                        auditLog.logException(methodName,
                                              OracleAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
                                                                                                      exception.getClass().getName(),
                                                                                                      methodName,
                                                                                                      exception.getMessage()),
                                              exception);

                        throw new ConnectorCheckedException(OracleErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
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

        throw new ConnectorCheckedException(OracleErrorCode.NO_DATABASE_CONNECTION.getMessageDefinition(connectionBean.getDisplayName()),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Return the table name qualified with its schema.  Oracle has no equivalent of PostgreSQL's
     * "currentSchema=" JDBC URL parameter, so (unlike the PostgreSQL tabular data set connector) table references
     * in SQL statements issued by this connector are always schema-qualified explicitly, rather than relying on
     * the connection's default schema.
     *
     * @return schema-qualified table name
     */
    private String getQualifiedTableName()
    {
        return schemaName + "." + tableName;
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
            propertyHelper.validateMandatoryName(tableName, OracleConfigurationProperty.TABLE_NAME.name, methodName);

            return jdbcResourceConnector.getRowCount(databaseConnection, getQualifiedTableName());
        }
        catch (Exception exception)
        {
            auditLog.logException(methodName,
                                  OracleAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
                                                                                          exception.getClass().getName(),
                                                                                          methodName,
                                                                                          exception.getMessage()),
                                  exception);

            throw new ConnectorCheckedException(OracleErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
                                                                                                            exception.getClass().getName(),
                                                                                                            methodName,
                                                                                                            exception.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                exception);
        }
    }



    /**
     * Set up the columns associated with this tabular data source.  These are stored in the first record of the file.
     * The rest of the file is cleared.
     *
     * @param columnDescriptions a list of column descriptions
     * @throws ConnectorCheckedException data access problem
     */
    @Override
    public void setColumnDescriptions(List<TabularColumnDescription> columnDescriptions) throws ConnectorCheckedException
    {
        final String methodName = "setColumnDescriptions";

        try
        {
            if (columnDescriptions != null)
            {
                OracleTable oracleTable = new OracleTabularTable(tableName,
                                                                  tableDescription,
                                                                  columnDescriptions);

                OracleSchemaDDL oracleSchemaDDL = new OracleSchemaDDL(schemaName,
                                                                       schemaDescription,
                                                                       Collections.singletonList(oracleTable));

                jdbcResourceConnector.addDatabaseDefinitions(databaseConnection, oracleSchemaDDL.getDDLStatements());
            }
        }
        catch (Exception exception)
        {
            auditLog.logException(methodName,
                                  OracleAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
                                                                                          exception.getClass().getName(),
                                                                                          methodName,
                                                                                          exception.getMessage()),
                                  exception);

            throw new ConnectorCheckedException(OracleErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
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
     * @throws ConnectorCheckedException a problem occurred accessing the data.
     */
    @Override
    public void writeRecord(long requestedRowNumber, List<String> dataValues) throws ConnectorCheckedException
    {
        final String methodName = "writeRecord";

        try
        {
            jdbcResourceConnector.issueSQLCommand(databaseConnection, buildSQLInsertIntoStatement(dataValues));
        }
        catch (Exception exception)
        {
            auditLog.logException(methodName,
                                  OracleAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
                                                                                          exception.getClass().getName(),
                                                                                          methodName,
                                                                                          exception.getMessage()),
                                  exception);

            throw new ConnectorCheckedException(OracleErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
                                                                                                            exception.getClass().getName(),
                                                                                                            methodName,
                                                                                                            exception.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                exception);
        }
    }


    /**
     * Convert a list of values into an insert SQL statement.  Unlike PostgreSQL and Microsoft SQL Server, a
     * trailing semicolon on a plain SQL statement makes Oracle JDBC reject it with ORA-00911 (invalid character)
     * - a semicolon is only valid there as the terminator of a PL/SQL block - so none is added here.
     *
     * @param dataValues values
     * @return SQL statement as a string
     */
    private String buildSQLInsertIntoStatement(List<String> dataValues)
    {
        StringBuilder stringBuilder = new StringBuilder("INSERT INTO ");
        stringBuilder.append(getQualifiedTableName());
        stringBuilder.append(" VALUES (");

        boolean firstRecord = true;
        for (String dataValue : dataValues)
        {
            if (! firstRecord)
            {
                stringBuilder.append(", ");
            }
            else
            {
                firstRecord = false;
            }

            stringBuilder.append(dataValue);
        }

        stringBuilder.append(")");

        return stringBuilder.toString();
    }


    /**
     * Write the requested data record to the end of the data source.
     *
     * @param dataValues Map of column descriptions to strings, each string is the value for the column.
     * @throws ConnectorCheckedException a problem occurred accessing the data.
     */
    @Override
    public void appendRecord(List<String> dataValues) throws ConnectorCheckedException
    {
        final String methodName = "appendRecord";

        try
        {
            jdbcResourceConnector.issueSQLCommand(databaseConnection, buildSQLInsertIntoStatement(dataValues));
        }
        catch (Exception exception)
        {
            auditLog.logException(methodName,
                                  OracleAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
                                                                                          exception.getClass().getName(),
                                                                                          methodName,
                                                                                          exception.getMessage()),
                                  exception);

            throw new ConnectorCheckedException(OracleErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
                                                                                                            exception.getClass().getName(),
                                                                                                            methodName,
                                                                                                            exception.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                exception);
        }
    }


    /**
     * Remove the requested data record.  The first data record is record 0.
     *
     * @param rowNumber long
     * @throws ConnectorCheckedException a problem occurred accessing the data.
     */
    @Override
    public void deleteRecord(long rowNumber) throws ConnectorCheckedException
    {
        // todo
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

        log.debug("Closing Database Connection");
    }
}
