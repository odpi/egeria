/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.tabulardatasource;

import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.postgres.ffdc.PostgresAuditCode;
import org.odpi.openmetadata.adapters.connectors.postgres.ffdc.PostgresErrorCode;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLSchemaDDL;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLTable;
import org.odpi.openmetadata.frameworks.connectors.*;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularColumnDescription;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.WritableTabularDataSource;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;


/**
 * PostgresTabularDataSourceConnector works with structured files to retrieve simple tables of data.
 */
public class PostgresTabularDataSetConnector extends ConnectorBase implements WritableTabularDataSource
{
    /*
     * Variables used for logging and debug.
     */
    private static final Logger log = LoggerFactory.getLogger(PostgresTabularDataSetConnector.class);

    protected String tableName         = null; // stored in snake case

    /**
     * The columns of the destination table, as this database names them.  Kept so that a write can name the
     * columns it is setting and identify the record it is replacing - see {@link #writeRecord}.
     */
    private List<TabularColumnDescription> databaseColumnDescriptions = null;
    protected String tableDescription  = null;
    private   String schemaName        = "tabular_data";
    private   String schemaDescription = null;

    private JDBCResourceConnector jdbcResourceConnector = null;

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

        tableName = super.getStringConfigurationProperty(PostgresConfigurationProperty.TABLE_NAME.getName(),
                                                         connectionBean.getConfigurationProperties(),
                                                         "data");

        tableDescription = super.getStringConfigurationProperty(PostgresConfigurationProperty.TABLE_DESCRIPTION.getName(),
                                                                connectionBean.getConfigurationProperties(),
                                                                null);

        schemaName = super.getStringConfigurationProperty(PostgresConfigurationProperty.SCHEMA_NAME.getName(),
                                                          connectionBean.getConfigurationProperties(),
                                                          schemaName);

        schemaDescription = super.getStringConfigurationProperty(PostgresConfigurationProperty.SCHEMA_DESCRIPTION.getName(),
                                                                 connectionBean.getConfigurationProperties(),
                                                                 schemaDescription);

        try
        {
            jdbcResourceConnector = this.getDatabaseConnection();
        }
        catch (Exception exception)
        {
            super.logExceptionRecord(methodName,
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
     * Build the statement that writes a record, replacing the one already there rather than adding a second
     * copy of it.
     * <br>
     * "Write" means the record should end up in the table exactly once, whether or not it was there before -
     * this is how a copy refreshes a destination it has delivered to previously.  Issuing a plain insert makes
     * every re-delivery add duplicates instead of updating, so a destination grows by the size of the source
     * each time.
     * <br>
     * The record's identity is the table's primary key, built from the columns marked as identifying the
     * record.  Where a table has no such columns there is nothing to match an existing record on, so the
     * record can only be added.
     *
     * @param dataValues values of the record, in column order
     * @return SQL statement
     */
    private String buildSQLWriteStatement(List<String> dataValues)
    {
        List<String> identifierColumns = this.getIdentifierColumnNames();

        if ((identifierColumns.isEmpty()) || (databaseColumnDescriptions == null)
                    || (databaseColumnDescriptions.size() != dataValues.size()))
        {
            return this.buildSQLInsertIntoStatement(dataValues);
        }

        List<String>  columnNames   = new ArrayList<>();
        List<String>  literals      = new ArrayList<>();
        List<String>  updateClauses = new ArrayList<>();

        for (int columnNumber = 0; columnNumber < databaseColumnDescriptions.size(); columnNumber ++)
        {
            String columnName = databaseColumnDescriptions.get(columnNumber).columnName();

            columnNames.add(columnName);
            literals.add(this.getSQLLiteral(dataValues.get(columnNumber)));

            if (! identifierColumns.contains(columnName))
            {
                updateClauses.add(columnName + " = excluded." + columnName);
            }
        }

        String statement = "INSERT INTO " + tableName + " (" + String.join(", ", columnNames) + ") VALUES ("
                                   + String.join(", ", literals) + ") ON CONFLICT (" + String.join(", ", identifierColumns) + ")";

        if (updateClauses.isEmpty())
        {
            /*
             * Every column identifies the record, so there is nothing to update: the record either is already
             * there, exactly as supplied, or it is added.
             */
            return statement + " DO NOTHING;";
        }

        return statement + " DO UPDATE SET " + String.join(", ", updateClauses) + ";";
    }


    /**
     * Return one data value as a SQL literal.
     * <br>
     * A record's values are text, whatever the column they are going into: they arrive from whichever data
     * source is being copied, and PostgreSQL casts a quoted literal to the column's type on the way in.
     * Written into the statement unquoted, a value is read as SQL rather than as data - so a description
     * reading "the name of a valid value" becomes a syntax error at "of", and a value containing an
     * apostrophe would end the literal early and let the rest of it be executed.
     *
     * @param dataValue value to write, may be null
     * @return literal for the statement
     */
    private String getSQLLiteral(String dataValue)
    {
        if (dataValue == null)
        {
            return "null";
        }

        /*
         * A literal apostrophe is doubled - the only escaping a standard-conforming SQL string literal needs.
         */
        return "'" + dataValue.replace("'", "''") + "'";
    }


    /**
     * Return the supplied column descriptions with their names in the form this database uses.
     * <br>
     * A column description carries the column's name in canonical form - capitalised words with spaces
     * between them, so that it can be translated to whatever naming convention the store at either end
     * happens to use.  PostgreSQL's is snake case, which is what this connector already stores its table name
     * in.  Used unconverted, a name like "Property Name" produces {@code Property Name text} in the generated
     * DDL, and the database reports a syntax error at the type rather than at the name that caused it.
     * <br>
     * Only the names are converted.  Records are written positionally, so the column order is what has to
     * agree between the two ends, not the names.
     *
     * @param columnDescriptions columns as described by whoever supplied them
     * @return the same columns, named as this database needs them
     */
    private List<TabularColumnDescription> getDatabaseColumnDescriptions(List<TabularColumnDescription> columnDescriptions)
    {
        List<TabularColumnDescription> databaseColumnDescriptions = new ArrayList<>();

        for (TabularColumnDescription columnDescription : columnDescriptions)
        {
            if (columnDescription != null)
            {
                databaseColumnDescriptions.add(new TabularColumnDescription(super.fromCanonicalToSnakeCase(columnDescription.columnName()),
                                                                            columnDescription.columnDataType(),
                                                                            columnDescription.description(),
                                                                            columnDescription.isNullable(),
                                                                            columnDescription.isIdentifier()));
            }
        }

        return databaseColumnDescriptions;
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
                            /*
                             * The audit log is passed on before the connector is started, because starting it is
                             * where it first has something to report.  Without it the embedded connector has no
                             * audit log at all, and its own error handling then fails with a null pointer while
                             * trying to log the error it was reporting - which is how a failure to reach the
                             * database arrives as a NullPointerException naming the audit log.
                             */
                            jdbcConnector.setAuditLog(auditLog);
                            jdbcConnector.start();
                        }

                        return jdbcConnector;
                    }
                    catch (Exception exception)
                    {
                        super.logExceptionRecord(methodName,
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
            propertyHelper.validateMandatoryName(tableName, PostgresConfigurationProperty.TABLE_NAME.name, methodName);

            try (java.sql.Connection databaseConnection = jdbcResourceConnector.getDataSource().getConnection())
            {
                return jdbcResourceConnector.getRowCount(databaseConnection, tableName);
            }
        }
        catch (Exception exception)
        {
            super.logExceptionRecord(methodName,
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
                databaseColumnDescriptions = this.getDatabaseColumnDescriptions(columnDescriptions);

                PostgreSQLTable postgreSQLTable = new PostgresTabularTable(tableName,
                                                                           tableDescription,
                                                                           databaseColumnDescriptions);

                PostgreSQLSchemaDDL postgreSQLSchemaDDL = new PostgreSQLSchemaDDL(schemaName,
                                                                                  schemaDescription,
                                                                                  Collections.singletonList(postgreSQLTable));

                try (java.sql.Connection databaseConnection = jdbcResourceConnector.getDataSource().getConnection())
                {
                    jdbcResourceConnector.addDatabaseDefinitions(databaseConnection, postgreSQLSchemaDDL.getDDLStatements());
                    databaseConnection.commit();
                }
            }
        }
        catch (Exception exception)
        {
            super.logExceptionRecord(methodName,
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
     * <br><br>
     * A relational table has no row order to insert into, so the row number is not used; the record is
     * written the same way as {@link #appendRecord}: as an upsert keyed on the identifier columns when the
     * column descriptions have been set and mark some, and as a plain insert otherwise.  A plain insert was
     * used here before, and it meant a data set delivered a second time to the same table - which is what a
     * refresh subscription does - failed on every row already there.
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
            try (java.sql.Connection databaseConnection = jdbcResourceConnector.getDataSource().getConnection())
            {
                jdbcResourceConnector.issueSQLCommand(databaseConnection, buildSQLWriteStatement(dataValues));
                databaseConnection.commit();
            }
        }
        catch (Exception exception)
        {
            super.logExceptionRecord(methodName,
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
     * Convert a list of values into an insert SQL statement.
     *
     * @param dataValues values
     * @return SQL statement as a string
     */
    private String buildSQLInsertIntoStatement(List<String> dataValues)
    {
        StringBuilder stringBuilder = new StringBuilder("INSERT INTO ");
        stringBuilder.append(tableName);
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

            stringBuilder.append(this.getSQLLiteral(dataValue));
        }

        stringBuilder.append(");");

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
            try (java.sql.Connection databaseConnection = jdbcResourceConnector.getDataSource().getConnection())
            {
                jdbcResourceConnector.issueSQLCommand(databaseConnection, buildSQLWriteStatement(dataValues));
                databaseConnection.commit();
            }
        }
        catch (Exception exception)
        {
            super.logExceptionRecord(methodName,
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
     * Remove the requested data record.  The first data record is record 0.
     * <br>
     * A table has no row numbers of its own, so the record at a position is the one at that offset when the
     * rows are put in a repeatable order - by the columns that identify a record where the table has them,
     * and by physical position where it does not.  That is the same order every other operation on this
     * connector sees, which is what makes "the record at position n" mean the same thing to all of them.
     *
     * @param rowNumber long
     * @throws ConnectorCheckedException a problem occurred accessing the data.
     */
    @Override
    public void deleteRecord(long rowNumber) throws ConnectorCheckedException
    {
        final String methodName = "deleteRecord";

        try
        {
            try (java.sql.Connection databaseConnection = jdbcResourceConnector.getDataSource().getConnection())
            {
                jdbcResourceConnector.issueSQLCommand(databaseConnection,
                                                      "DELETE FROM " + tableName + " WHERE ctid IN (SELECT ctid FROM " + tableName
                                                              + " ORDER BY " + this.getRecordOrder() + " OFFSET " + rowNumber + " LIMIT 1);");
                databaseConnection.commit();
            }
        }
        catch (Exception exception)
        {
            super.logExceptionRecord(methodName,
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
     * Return the order that gives a table's records a stable position.  The columns that identify a record
     * are used where the table has them, because they are what the table's primary key is built from; where it
     * has none there is nothing to order by but physical position.
     *
     * @return SQL order by list
     */
    private String getRecordOrder()
    {
        List<String> identifierColumns = this.getIdentifierColumnNames();

        if (identifierColumns.isEmpty())
        {
            return "ctid";
        }

        return String.join(", ", identifierColumns);
    }


    /**
     * Return the names of the columns that identify a record - the table's primary key.
     *
     * @return column names, empty if the columns are not known or none of them identifies a record
     */
    private List<String> getIdentifierColumnNames()
    {
        List<String> identifierColumns = new ArrayList<>();

        if (databaseColumnDescriptions != null)
        {
            for (TabularColumnDescription columnDescription : databaseColumnDescriptions)
            {
                if ((columnDescription != null) && (columnDescription.isIdentifier()))
                {
                    identifierColumns.add(columnDescription.columnName());
                }
            }
        }

        return identifierColumns;
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