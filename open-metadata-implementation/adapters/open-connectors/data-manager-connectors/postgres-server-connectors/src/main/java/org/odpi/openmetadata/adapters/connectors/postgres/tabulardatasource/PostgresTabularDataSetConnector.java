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
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularColumnDescription;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.WritableTabularDataSource;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * PostgresTabularDataSetConnector presents one PostgreSQL table as a simple table of data that can be both read and
 * written.  As a source, it describes the table's columns from the database's own catalog and reads its records in
 * a stable order; as a destination, it creates the table from the column descriptions it is given and writes
 * records into it.  Column names cross the boundary in canonical form - capitalised words with spaces between them -
 * and are held in the database in snake case, which is the naming convention PostgreSQL uses.
 */
public class PostgresTabularDataSetConnector extends ConnectorBase implements ReadableTabularDataSource,
                                                                             WritableTabularDataSource
{
    /*
     * Variables used for logging and debug.
     */
    private static final Logger log = LoggerFactory.getLogger(PostgresTabularDataSetConnector.class);

    protected String tableName         = null; // stored in snake case

    /**
     * The columns of the table, as this database names them.  Set from the column descriptions supplied to
     * {@link #setColumnDescriptions} when this connector is the destination of a copy, and read from the
     * database's catalog when it is the source - see {@link #getDatabaseColumnDescriptions()}.  Kept so that a
     * write can name the columns it is setting and identify the record it is replacing, and so that every
     * operation puts the records in the same order.
     */
    private List<TabularColumnDescription> databaseColumnDescriptions = null;
    protected String tableDescription  = null;
    private   String schemaName        = "tabular_data";
    private   String schemaDescription = null;

    private JDBCResourceConnector jdbcResourceConnector = null;

    /**
     * The number of records that {@link #readRecord} fetches in one query.  A copy reads a table one record
     * at a time from record 0 upwards, and fetching each record on its own means one query per record whose
     * offset the database has to step through - so the work of a copy grows with the square of the table's
     * size.  Fetching them a window at a time divides that work by the size of the window.
     */
    private static final int READ_AHEAD_RECORDS = 1000;

    /**
     * The window of records most recently fetched by {@link #readRecord}, starting at record
     * {@link #readAheadStart}; null when there is none.  It is dropped whenever the table might have changed
     * through this connector, and whenever the records are counted, which is how every copy begins - so a copy
     * never starts from records that an earlier copy fetched.
     */
    private List<List<String>> readAheadRecords = null;
    private long               readAheadStart   = 0;

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
     * <br>
     * A record that is already there with the same values is left alone.  A refresh writes every record of
     * the source each time it runs, and most of them have not changed since the last one; updating them anyway
     * gives each one a new row version to write, index and vacuum, for no change to the data.  The values are
     * compared as text, which is the form they arrive in and which every column type can be cast to.
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

        List<String>  columnNames     = new ArrayList<>();
        List<String>  literals        = new ArrayList<>();
        List<String>  updateClauses   = new ArrayList<>();
        List<String>  existingValues  = new ArrayList<>();
        List<String>  suppliedValues  = new ArrayList<>();

        for (int columnNumber = 0; columnNumber < databaseColumnDescriptions.size(); columnNumber ++)
        {
            String columnName = databaseColumnDescriptions.get(columnNumber).columnName();

            columnNames.add(columnName);
            literals.add(getSQLLiteral(dataValues.get(columnNumber)));

            if (! identifierColumns.contains(columnName))
            {
                updateClauses.add(columnName + " = excluded." + columnName);
                existingValues.add("existing." + columnName + "::text");
                suppliedValues.add("excluded." + columnName + "::text");
            }
        }

        String statement = "INSERT INTO " + tableName + " AS existing (" + String.join(", ", columnNames) + ") VALUES ("
                                   + String.join(", ", literals) + ") ON CONFLICT (" + String.join(", ", identifierColumns) + ")";

        if (updateClauses.isEmpty())
        {
            /*
             * Every column identifies the record, so there is nothing to update: the record either is already
             * there, exactly as supplied, or it is added.
             */
            return statement + " DO NOTHING;";
        }

        return statement + " DO UPDATE SET " + String.join(", ", updateClauses)
                       + " WHERE (" + String.join(", ", existingValues) + ") IS DISTINCT FROM ("
                       + String.join(", ", suppliedValues) + ");";
    }


    /**
     * Forget the records read ahead by {@link #readRecord}, so that the next read fetches them afresh.
     */
    private synchronized void discardReadAhead()
    {
        readAheadRecords = null;
        readAheadStart   = 0;
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
    private static String getSQLLiteral(String dataValue)
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
     * Return the number of records in the table.
     *
     * @return count
     * @throws ConnectorCheckedException problem accessing the data
     */
    @Override
    public long getRecordCount() throws ConnectorCheckedException
    {
        final String  methodName = "getRecordCount";

        /*
         * Counting the records is how a copy begins, so the records it goes on to read are fetched afresh.
         */
        this.discardReadAhead();

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
     * Return the name of the table in canonical form - capitalised words with spaces between them - so that a
     * destination can translate it into its own naming convention.  The table is held in the database in snake case.
     *
     * @return table name
     * @throws ConnectorCheckedException data access problem
     */
    @Override
    public String getTableName() throws ConnectorCheckedException
    {
        return super.fromSnakeToCanonicalCase(tableName);
    }


    /**
     * Return the description for this data source.
     *
     * @return string
     * @throws ConnectorCheckedException data access problem
     */
    @Override
    public String getTableDescription() throws ConnectorCheckedException
    {
        return tableDescription;
    }


    /**
     * Return the list of columns in this table, named in canonical form so that a destination can translate
     * them into its own naming convention.  The column order is the table's own, which is also the order that
     * the values of a record are returned in by {@link #readRecord}.
     *
     * @return a list of column descriptions or null if not available.
     * @throws ConnectorCheckedException data access problem
     */
    @Override
    public List<TabularColumnDescription> getColumnDescriptions() throws ConnectorCheckedException
    {
        List<TabularColumnDescription> databaseColumns = this.getDatabaseColumnDescriptions();

        if (databaseColumns == null)
        {
            return null;
        }

        List<TabularColumnDescription> columnDescriptions = new ArrayList<>();

        for (TabularColumnDescription databaseColumn : databaseColumns)
        {
            columnDescriptions.add(new TabularColumnDescription(super.fromSnakeToCanonicalCase(databaseColumn.columnName()),
                                                                databaseColumn.columnDataType(),
                                                                databaseColumn.description(),
                                                                databaseColumn.isNullable(),
                                                                databaseColumn.isIdentifier()));
        }

        return columnDescriptions;
    }


    /**
     * Locate the named column.  The name may be given in canonical form or as the database names it.
     * A negative number means the column is not present.
     *
     * @param columnName name of the column
     * @return column number, starting at 0
     * @throws ConnectorCheckedException problem extracting the column descriptions
     */
    @Override
    public int getColumnNumber(String columnName) throws ConnectorCheckedException
    {
        List<TabularColumnDescription> databaseColumns = this.getDatabaseColumnDescriptions();

        if ((columnName != null) && (databaseColumns != null))
        {
            String databaseColumnName = super.fromCanonicalToSnakeCase(columnName);

            for (int columnNumber = 0; columnNumber < databaseColumns.size(); columnNumber++)
            {
                String candidateName = databaseColumns.get(columnNumber).columnName();

                if ((columnName.equals(candidateName)) || (databaseColumnName.equals(candidateName)))
                {
                    return columnNumber;
                }
            }
        }

        return -1;
    }


    /**
     * Return the requested data record.  The first record is record 0.
     * <br>
     * A table has no row numbers of its own, so the record at a position is the one at that offset when the
     * rows are put in the same repeatable order that {@link #deleteRecord} uses - by the columns that identify a
     * record where the table has them, and by physical position where it does not.  The values are returned in
     * the table's column order, as text: PostgreSQL renders each value in its own type's text form, which is
     * what a destination casts back on the way in.
     * <br>
     * The records are fetched a window of {@link #READ_AHEAD_RECORDS} at a time, starting at the one requested,
     * and the records that follow it are returned from that window - see {@link #READ_AHEAD_RECORDS} for why.
     * A record outside the window starts a new one.
     *
     * @param rowNumber long
     * @return List of strings, each string is the value from the column; a null value stays null.
     * @throws ConnectorCheckedException a problem occurred accessing the data, or there is no such record.
     */
    @Override
    public synchronized List<String> readRecord(long rowNumber) throws ConnectorCheckedException
    {
        final String methodName = "readRecord";

        try
        {
            propertyHelper.validateMandatoryName(tableName, PostgresConfigurationProperty.TABLE_NAME.name, methodName);

            if ((readAheadRecords == null) || (rowNumber < readAheadStart) || (rowNumber >= readAheadStart + readAheadRecords.size()))
            {
                /*
                 * A read that carries on from the end of a full window - which is every read of a copy - starts
                 * the next window after the last record of this one.
                 */
                List<String> previousRecord = null;

                if ((readAheadRecords != null) && (readAheadRecords.size() == READ_AHEAD_RECORDS)
                            && (rowNumber == readAheadStart + readAheadRecords.size()))
                {
                    previousRecord = readAheadRecords.get(readAheadRecords.size() - 1);
                }

                readAheadRecords = this.fetchRecords(rowNumber, previousRecord);
                readAheadStart   = rowNumber;
            }

            int position = (int)(rowNumber - readAheadStart);

            if (position >= readAheadRecords.size())
            {
                throw new ConnectorCheckedException(PostgresErrorCode.NO_SUCH_RECORD.getMessageDefinition(Long.toString(rowNumber),
                                                                                                          tableName,
                                                                                                          connectionBean.getDisplayName()),
                                                    this.getClass().getName(),
                                                    methodName);
            }

            return new ArrayList<>(readAheadRecords.get(position));
        }
        catch (ConnectorCheckedException error)
        {
            throw error;
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
     * Return the columns for a query that reads records: the described columns, in their described order.
     * <br>
     * When this connector is the destination of a copy, its column descriptions are the ones supplied to
     * {@link #setColumnDescriptions}, and the table may hold more columns than that - a subscriber's staging
     * table adds its own processing columns, for example.  Reading every column with {@code SELECT *} returned
     * records that were longer than the columns described, so a destination record never equalled the source
     * record it came from.  Every delivery then rewrote every record, and reported all of them as changed, even
     * though the upserts changed nothing.  Reading only the described columns keeps a record the same shape as
     * {@link #getColumnDescriptions} and {@link #writeRecord}.
     *
     * @param columnDescriptions columns as the database names them - may be null or empty
     * @return comma-separated column names, or {@code *} when no columns are described
     */
    static String getSelectList(List<TabularColumnDescription> columnDescriptions)
    {
        if ((columnDescriptions == null) || (columnDescriptions.isEmpty()))
        {
            return "*";
        }

        List<String> columnNames = new ArrayList<>(columnDescriptions.size());

        for (TabularColumnDescription columnDescription : columnDescriptions)
        {
            if ((columnDescription == null) || (columnDescription.columnName() == null))
            {
                return "*";
            }

            columnNames.add(columnDescription.columnName());
        }

        return String.join(", ", columnNames);
    }


    /**
     * Return the condition that selects the records after the supplied one, in the order that gives each record
     * its position - for a table ordered by the columns that identify a record.
     * <br>
     * Fetching a window with {@code OFFSET n} makes the database step through the n records in front of it, so
     * reading a whole table that way is still quadratic in its size, just with a smaller constant.  Starting the
     * window after the last record already read lets the database go straight to it through the primary key's
     * index.  The comparison uses the same columns, in the same order, as the {@code ORDER BY} - and the
     * database's own collation for both - so the window starts exactly where the last one ended.
     *
     * @param columnDescriptions the columns read, as the database names them
     * @param previousRecord the last record read, in the same column order
     * @return SQL condition, or null when the records are not ordered by identifying columns, or the record does
     * not hold a value for each of them
     */
    static String getKeysetCondition(List<TabularColumnDescription> columnDescriptions,
                                     List<String>                   previousRecord)
    {
        if ((previousRecord == null) || ("*".equals(getSelectList(columnDescriptions)))
                    || (previousRecord.size() != columnDescriptions.size()))
        {
            return null;
        }

        List<String> columnNames = new ArrayList<>();
        List<String> literals    = new ArrayList<>();

        for (int columnNumber = 0; columnNumber < columnDescriptions.size(); columnNumber++)
        {
            if (columnDescriptions.get(columnNumber).isIdentifier())
            {
                String dataValue = previousRecord.get(columnNumber);

                if (dataValue == null)
                {
                    return null;
                }

                columnNames.add(columnDescriptions.get(columnNumber).columnName());
                literals.add(getSQLLiteral(dataValue));
            }
        }

        if (columnNames.isEmpty())
        {
            return null;
        }

        if (columnNames.size() == 1)
        {
            return columnNames.get(0) + " > " + literals.get(0);
        }

        return "(" + String.join(", ", columnNames) + ") > (" + String.join(", ", literals) + ")";
    }


    /**
     * Fetch the window of records that starts at the requested record, in the order that gives each record its
     * position.  The window is shorter than {@link #READ_AHEAD_RECORDS} at the end of the table, and empty when
     * the requested record is beyond it.
     * <br>
     * When the window follows on from one already read, it is fetched as the records after that window's last
     * record - see {@link #getKeysetCondition}.  Otherwise, and for a table without identifying columns, it is
     * fetched by position.
     *
     * @param firstRowNumber position of the first record in the window
     * @param previousRecord the record just before the window, or null if it has not been read
     * @return records, each as its values in column order
     * @throws Exception problem accessing the database
     */
    private List<List<String>> fetchRecords(long         firstRowNumber,
                                            List<String> previousRecord) throws Exception
    {
        /*
         * The column descriptions are loaded first so that the record order is known, and so that only the
         * described columns are read.
         */
        List<TabularColumnDescription> columnDescriptions = this.getDatabaseColumnDescriptions();

        String keysetCondition = getKeysetCondition(columnDescriptions, previousRecord);
        String sqlQuery;

        if (keysetCondition == null)
        {
            sqlQuery = "SELECT " + getSelectList(columnDescriptions) + " FROM " + tableName
                               + " ORDER BY " + this.getRecordOrder()
                               + " OFFSET " + firstRowNumber + " LIMIT " + READ_AHEAD_RECORDS;
        }
        else
        {
            sqlQuery = "SELECT " + getSelectList(columnDescriptions) + " FROM " + tableName
                               + " WHERE " + keysetCondition
                               + " ORDER BY " + this.getRecordOrder()
                               + " LIMIT " + READ_AHEAD_RECORDS;
        }

        List<List<String>> records = new ArrayList<>();

        try (java.sql.Connection databaseConnection = jdbcResourceConnector.getDataSource().getConnection();
             Statement           statement          = databaseConnection.createStatement();
             ResultSet           resultSet          = statement.executeQuery(sqlQuery))
        {
            int columnCount = resultSet.getMetaData().getColumnCount();

            while (resultSet.next())
            {
                List<String> dataValues = new ArrayList<>(columnCount);

                for (int columnNumber = 1; columnNumber <= columnCount; columnNumber++)
                {
                    dataValues.add(resultSet.getString(columnNumber));
                }

                records.add(dataValues);
            }
        }

        return records;
    }


    /**
     * Return the columns of the table as the database names them, reading them from the database's catalog the
     * first time they are needed.  When this connector is the destination of a copy the columns are already
     * known - they were supplied to {@link #setColumnDescriptions} - and the catalog is not consulted.
     * <br>
     * The columns come from the result set of an empty query against the table rather than from the catalog's
     * tables, so that the table name resolves exactly as it does for every read and write on this connector -
     * through the connection's own schema search path.  The primary key, which is what marks a column as
     * identifying a record, does come from the catalog, looked up in the schema the query resolved to.
     *
     * @return column descriptions, or null if the table does not exist
     * @throws ConnectorCheckedException problem accessing the database
     */
    private synchronized List<TabularColumnDescription> getDatabaseColumnDescriptions() throws ConnectorCheckedException
    {
        final String methodName = "getDatabaseColumnDescriptions";

        if (databaseColumnDescriptions != null)
        {
            return databaseColumnDescriptions;
        }

        try
        {
            propertyHelper.validateMandatoryName(tableName, PostgresConfigurationProperty.TABLE_NAME.name, methodName);

            try (java.sql.Connection databaseConnection = jdbcResourceConnector.getDataSource().getConnection())
            {
                if (! jdbcResourceConnector.doesTableExist(databaseConnection, tableName))
                {
                    return null;
                }

                List<TabularColumnDescription> columnDescriptions = new ArrayList<>();

                try (Statement statement = databaseConnection.createStatement();
                     ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName + " WHERE false"))
                {
                    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                    List<String>      primaryKeyColumns = this.getPrimaryKeyColumns(databaseConnection, resultSetMetaData);

                    for (int columnNumber = 1; columnNumber <= resultSetMetaData.getColumnCount(); columnNumber++)
                    {
                        String columnName = resultSetMetaData.getColumnName(columnNumber);

                        columnDescriptions.add(new TabularColumnDescription(columnName,
                                                                            getDataType(resultSetMetaData.getColumnType(columnNumber), resultSetMetaData.getColumnTypeName(columnNumber)),
                                                                            null,
                                                                            resultSetMetaData.isNullable(columnNumber) != ResultSetMetaData.columnNoNulls,
                                                                            primaryKeyColumns.contains(columnName)));
                    }
                }

                databaseColumnDescriptions = columnDescriptions;

                return databaseColumnDescriptions;
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
     * Return the names of the columns that make up the table's primary key.  The table is identified by the
     * schema and name that the query resolved it to; where the driver does not report the schema, the
     * connection's current schema is used, since that is where an unqualified table name resolves.
     *
     * @param databaseConnection open connection
     * @param resultSetMetaData metadata of a query against the table
     * @return column names, empty if the table has no primary key
     * @throws java.sql.SQLException problem accessing the catalog
     */
    private List<String> getPrimaryKeyColumns(java.sql.Connection databaseConnection,
                                              ResultSetMetaData   resultSetMetaData) throws java.sql.SQLException
    {
        List<String> primaryKeyColumns = new ArrayList<>();

        if (resultSetMetaData.getColumnCount() == 0)
        {
            return primaryKeyColumns;
        }

        String schemaName = resultSetMetaData.getSchemaName(1);
        String tableName  = resultSetMetaData.getTableName(1);

        if ((schemaName == null) || (schemaName.isEmpty()))
        {
            schemaName = databaseConnection.getSchema();
        }

        if ((tableName == null) || (tableName.isEmpty()))
        {
            tableName = this.tableName;
        }

        DatabaseMetaData databaseMetaData = databaseConnection.getMetaData();

        try (ResultSet primaryKeys = databaseMetaData.getPrimaryKeys(null, schemaName, tableName))
        {
            while (primaryKeys.next())
            {
                primaryKeyColumns.add(primaryKeys.getString("COLUMN_NAME"));
            }
        }

        return primaryKeyColumns;
    }


    /**
     * Bring a different table into focus.  The columns of the previous table are forgotten so that they are read
     * afresh - or supplied afresh through {@link #setColumnDescriptions} - for the new one.
     *
     * @param tableName name of the table in snake case
     * @param tableDescription description of the table (may be null)
     */
    protected synchronized void focusOnTable(String tableName,
                                             String tableDescription)
    {
        this.tableName                  = tableName;
        this.tableDescription           = tableDescription;
        this.databaseColumnDescriptions = null;
        this.readAheadRecords           = null;
        this.readAheadStart             = 0;
    }


    /**
     * Return the names of the tables in the connection's current schema - the schema that every unqualified
     * table name used by this connector resolves to - in snake case, sorted by name.  Views are not included
     * since they cannot be written to.
     *
     * @return list of table names (may be empty)
     * @throws ConnectorCheckedException problem accessing the database
     */
    protected List<String> getSchemaTableNames() throws ConnectorCheckedException
    {
        final String methodName = "getSchemaTableNames";

        try (java.sql.Connection databaseConnection = jdbcResourceConnector.getDataSource().getConnection())
        {
            String currentSchemaName = databaseConnection.getSchema();

            if ((currentSchemaName == null) || (currentSchemaName.isEmpty()))
            {
                currentSchemaName = schemaName;
            }

            List<String> tableNames = new ArrayList<>();

            DatabaseMetaData databaseMetaData = databaseConnection.getMetaData();
            String           schemaPattern    = jdbcResourceConnector.escapeSearchPattern(currentSchemaName,
                                                                                         databaseMetaData.getSearchStringEscape());

            try (ResultSet tables = databaseMetaData.getTables(null, schemaPattern, "%", new String[]{"TABLE"}))
            {
                while (tables.next())
                {
                    tableNames.add(tables.getString("TABLE_NAME"));
                }
            }

            Collections.sort(tableNames);

            return tableNames;
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
     * Return the data type that describes a column of the given SQL type.  The mapping is the inverse of the one
     * {@link PostgresTabularColumn} uses to create the table, widened to the other types a table that this
     * connector did not create may have; anything else is presented as text, which is how the values are read.
     * <br>
     * The three kinds of time are kept apart, so that a copy creates the same kind of column it was read from: a
     * {@code date} is a {@link DataType#LOCAL_DATE}, a {@code timestamp} without a time zone a
     * {@link DataType#LOCAL_DATE_TIME}, and a {@code timestamptz} - a point in time - a {@link DataType#DATE}.
     * The PostgreSQL driver reports both kinds of timestamp as {@code Types.TIMESTAMP}, so they are told apart by
     * the database's own name for the type.
     *
     * @param sqlType type from java.sql.Types
     * @param sqlTypeName the database's name for the type - may be null
     * @return data type
     */
    static DataType getDataType(int    sqlType,
                                String sqlTypeName)
    {
        return switch (sqlType)
        {
            case Types.INTEGER, Types.SMALLINT, Types.TINYINT -> DataType.INT;
            case Types.BIGINT -> DataType.LONG;
            case Types.DATE -> DataType.LOCAL_DATE;
            case Types.TIMESTAMP_WITH_TIMEZONE -> DataType.DATE;
            case Types.TIMESTAMP -> isTimeZoneTypeName(sqlTypeName) ? DataType.DATE : DataType.LOCAL_DATE_TIME;
            case Types.BOOLEAN, Types.BIT -> DataType.BOOLEAN;
            case Types.REAL, Types.FLOAT -> DataType.FLOAT;
            case Types.DOUBLE -> DataType.DOUBLE;
            case Types.NUMERIC, Types.DECIMAL -> DataType.BIGDECIMAL;
            default -> DataType.STRING;
        };
    }


    /**
     * Return whether the database's name for a timestamp type is one with a time zone.
     *
     * @param sqlTypeName type name, such as {@code timestamptz} - may be null
     * @return true for a timestamp with a time zone
     */
    private static boolean isTimeZoneTypeName(String sqlTypeName)
    {
        if (sqlTypeName == null)
        {
            return false;
        }

        String typeName = sqlTypeName.toLowerCase();

        return typeName.equals("timestamptz") || typeName.contains("with time zone");
    }


    /**
     * Set up the columns associated with this tabular data source, creating the schema and table to hold them
     * if they do not already exist.
     *
     * @param columnDescriptions a list of column descriptions
     * @throws ConnectorCheckedException data access problem
     */
    @Override
    public void setColumnDescriptions(List<TabularColumnDescription> columnDescriptions) throws ConnectorCheckedException
    {
        final String methodName = "setColumnDescriptions";

        this.discardReadAhead();

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

        this.discardReadAhead();

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

            stringBuilder.append(getSQLLiteral(dataValue));
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

        this.discardReadAhead();

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

        this.discardReadAhead();

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
     * The number of statements sent to the database in one JDBC batch by {@link #writeRecords} and
     * {@link #appendRecords}.
     */
    private static final int WRITE_BATCH_SIZE = 1000;


    /**
     * Write several records in one transaction.  Each is written as {@link #writeRecord} would write it - the
     * row numbers are not used - but the statements go to the database in batches on one connection, and are
     * committed once.  Writing them one at a time took a connection and a commit for each record.
     *
     * @param dataRecords map of row number to the values of the record to write there
     * @throws ConnectorCheckedException a problem occurred accessing the data.
     */
    @Override
    public void writeRecords(Map<Long, List<String>> dataRecords) throws ConnectorCheckedException
    {
        if (dataRecords != null)
        {
            this.issueSQLWriteStatements(new ArrayList<>(dataRecords.values()), "writeRecords");
        }
    }


    /**
     * Write several records to the table in one transaction - see {@link #writeRecords}.
     *
     * @param dataRecords values of each record to append
     * @throws ConnectorCheckedException a problem occurred accessing the data.
     */
    @Override
    public void appendRecords(List<List<String>> dataRecords) throws ConnectorCheckedException
    {
        this.issueSQLWriteStatements(dataRecords, "appendRecords");
    }


    /**
     * Issue the statement that writes each record, in batches on one connection, committing once at the end.
     * Nothing is written if any of them fails.
     *
     * @param dataRecords values of each record to write
     * @param methodName calling method
     * @throws ConnectorCheckedException a problem occurred accessing the data.
     */
    private void issueSQLWriteStatements(List<List<String>> dataRecords,
                                         String             methodName) throws ConnectorCheckedException
    {
        this.discardReadAhead();

        if ((dataRecords == null) || (dataRecords.isEmpty()))
        {
            return;
        }

        try
        {
            try (java.sql.Connection databaseConnection = jdbcResourceConnector.getDataSource().getConnection())
            {
                try (Statement statement = databaseConnection.createStatement())
                {
                    int batchedStatements = 0;

                    for (List<String> dataValues : dataRecords)
                    {
                        statement.addBatch(buildSQLWriteStatement(dataValues));
                        batchedStatements++;

                        if (batchedStatements == WRITE_BATCH_SIZE)
                        {
                            statement.executeBatch();
                            batchedStatements = 0;
                        }
                    }

                    if (batchedStatements > 0)
                    {
                        statement.executeBatch();
                    }

                    databaseConnection.commit();
                }
                catch (Exception exception)
                {
                    databaseConnection.rollback();
                    throw exception;
                }
            }
        }
        catch (Exception exception)
        {
            throw this.getUnexpectedException(exception, methodName);
        }
    }


    /**
     * Remove several records in one statement.  Each row number is the record's position, in the order that
     * {@link #readRecord} gives them, before any of them is removed; the records are numbered by that order
     * and the ones at the requested positions removed together.  Removing them one at a time took a query for
     * each, stepping through the table to the record's offset.
     *
     * @param rowNumbers positions of the records to remove
     * @throws ConnectorCheckedException a problem occurred accessing the data.
     */
    @Override
    public void deleteRecords(List<Long> rowNumbers) throws ConnectorCheckedException
    {
        final String methodName = "deleteRecords";

        this.discardReadAhead();

        if ((rowNumbers == null) || (rowNumbers.isEmpty()))
        {
            return;
        }

        String sqlCommand = "DELETE FROM " + tableName + " WHERE ctid IN (SELECT ctid FROM (SELECT ctid, row_number() OVER (ORDER BY "
                                    + this.getRecordOrder() + ") - 1 AS record_position FROM " + tableName
                                    + ") AS numbered_records WHERE record_position = ANY (?))";

        try
        {
            try (java.sql.Connection databaseConnection = jdbcResourceConnector.getDataSource().getConnection())
            {
                try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(sqlCommand))
                {
                    preparedStatement.setArray(1, databaseConnection.createArrayOf("bigint", rowNumbers.toArray(new Long[0])));
                    preparedStatement.executeUpdate();
                    databaseConnection.commit();
                }
                catch (Exception exception)
                {
                    databaseConnection.rollback();
                    throw exception;
                }
            }
        }
        catch (Exception exception)
        {
            throw this.getUnexpectedException(exception, methodName);
        }
    }


    /**
     * Log an unexpected exception and return the exception to throw for it.
     *
     * @param exception what went wrong
     * @param methodName calling method
     * @return exception to throw
     */
    private ConnectorCheckedException getUnexpectedException(Exception exception,
                                                             String    methodName)
    {
        super.logExceptionRecord(methodName,
                                 PostgresAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
                                                                                             exception.getClass().getName(),
                                                                                             methodName,
                                                                                             exception.getMessage()),
                                 exception);

        return new ConnectorCheckedException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
                                                                                                         exception.getClass().getName(),
                                                                                                         methodName,
                                                                                                         exception.getMessage()),
                                             this.getClass().getName(),
                                             methodName,
                                             exception);
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
    @Override
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