/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datastore.csvfile;

import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.ffdc.exception.FileException;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.ffdc.exception.FileReadException;
import org.odpi.openmetadata.frameworks.openmetadata.controls.CSVFileConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.ffdc.CSVFileConnectorAuditCode;
import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.ffdc.CSVFileConnectorErrorCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularColumnDescription;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.WritableTabularDataSource;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * CSVFileStoreConnector works with structured files to retrieve simple tables of data.
 */
public class CSVTabularDataSetConnector extends ConnectorBase implements ReadableTabularDataSource,
                                                                         WritableTabularDataSource
{
    /*
     * Variables used in managing the tabular data.
     */
    private   String       directoryPathName  = ".";
    protected String       tableName          = "Tabular File";
    protected String       tableDescription   = null;

    protected CSVFileStoreConnector fileStoreConnector = null;

    /*
     * Variables used for logging and debug.
     */
    private static final Logger log = LoggerFactory.getLogger(CSVTabularDataSetConnector.class);


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

        directoryPathName = super.getStringConfigurationProperty(CSVFileConfigurationProperty.DIRECTORY_PATH_NAME.getName(),
                                                                 connectionBean.getConfigurationProperties(),
                                                                 directoryPathName);

        tableName = super.getStringConfigurationProperty(CSVFileConfigurationProperty.TABLE_NAME.getName(),
                                                         connectionBean.getConfigurationProperties(),
                                                         tableName);

        this.setFileStoreName(tableName);

        tableDescription = super.getStringConfigurationProperty(CSVFileConfigurationProperty.TABLE_DESCRIPTION.getName(),
                                                                connectionBean.getConfigurationProperties(),
                                                                tableDescription);

        fileStoreConnector = this.getFileStoreConnector();
    }


    /**
     * Retrieve the embedded CSV File Store Connector.
     *
     * @return connector or exception
     * @throws ConnectorCheckedException no working JDBC connector
     */
    private CSVFileStoreConnector getFileStoreConnector() throws ConnectorCheckedException
    {
        final String methodName = "getFileStoreConnector";

        if ((embeddedConnectors != null) && (!embeddedConnectors.isEmpty()))
        {
            for (Connector embeddedConnector : embeddedConnectors)
            {
                if (embeddedConnector instanceof CSVFileStoreConnector csvFileStoreConnector)
                {
                    try
                    {
                        if (! csvFileStoreConnector.isActive())
                        {
                            csvFileStoreConnector.start();
                        }

                        return csvFileStoreConnector;
                    }
                    catch (Exception exception)
                    {
                        auditLog.logException(methodName,
                                              CSVFileConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
                                                                                                                  exception.getClass().getName(),
                                                                                                                  methodName,
                                                                                                                  exception.getMessage()),
                                              exception);

                        throw new ConnectorCheckedException(CSVFileConnectorErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
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

        throw new ConnectorCheckedException(CSVFileConnectorErrorCode.NO_EMBEDDED_FILE_STORE.getMessageDefinition(connectionBean.getDisplayName()),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Set up the file name to use for this tabular data set.
     *
     * @param tableName name of table
     * @throws FileException problem using the name as a file name.
     */
    protected void setFileStoreName(String tableName) throws FileException
    {
        fileStoreConnector.setFileStoreName(directoryPathName + "/" + tableName + ".csv");
    }


    /**
     * Locate the named column. A negative number means the column is not present.
     *
     * @return column
     * @throws ConnectorCheckedException problem extracting the column descriptions
     */
    @Override
    public int getColumnNumber(String columnName) throws ConnectorCheckedException
    {
        int columnNumber = -1; // means not present

        List<TabularColumnDescription> columnDescriptions = this.getColumnDescriptions();
        if (columnDescriptions != null)
        {
            int columnCount = 0;
            for (TabularColumnDescription tabularColumnDescription : columnDescriptions)
            {
                if ((tabularColumnDescription != null) && (columnName.equals(tabularColumnDescription.columnName())))
                {
                    columnNumber = columnCount;
                    break;
                }

                columnCount ++;
            }
        }

        return columnNumber;
    }


    /**
     * Return the number of records in the file.  This is achieved by scanning the file and counting the records -
     * not recommended for very large files.
     *
     * @return count
     * @throws FileException problem accessing the file
     * @throws FileReadException unable to find, open or scan the file.
     */
    @Override
    public long getRecordCount() throws FileException, FileReadException
    {
        final String  methodName = "getRecordCount";

        String fileStoreName = fileStoreConnector.getFileName();

        long    rowCount = 0;

        File fileStore = fileStoreConnector.getFile();

        try (Scanner scanner  = new Scanner(fileStore))
        {
            while (scanner.hasNext())
            {
                scanner.nextLine();
                rowCount ++;
            }

            if ((rowCount > 0) && (fileStoreConnector.getSuppliedColumnNames() == null))
            {
                rowCount = rowCount - 1;
            }
        }
        catch (IOException  error)
        {
            throw new FileReadException(CSVFileConnectorErrorCode.UNEXPECTED_IO_EXCEPTION.getMessageDefinition(fileStoreName,
                                                                                                               error.getMessage()),
                                        this.getClass().getName(),
                                        methodName,
                                        error,
                                        fileStoreName);
        }

        return rowCount;
    }


    /**
     * Return the canonical table name for this data source.  Each word in the name should be capitalized, with spaces
     * between the words to allow translation between different naming conventions.
     *
     * @return string
     * @throws ConnectorCheckedException data access problem
     */
    @Override
    public String getTableName() throws ConnectorCheckedException
    {
        return tableName;
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
     * Return the list of column names associated with this data source.
     *
     * @return a list of column descriptions or null if not available.
     * @throws ConnectorCheckedException data access problem
     */
    @Override
    public List<TabularColumnDescription> getColumnDescriptions() throws ConnectorCheckedException
    {
        List<String> columnNames = fileStoreConnector.getColumnNames();

        if (columnNames != null)
        {
            List<TabularColumnDescription> columnDescriptions = new ArrayList<>();

            for (String columnName : columnNames)
            {
                TabularColumnDescription columnDescription = new TabularColumnDescription(columnName, DataType.STRING, null, false, false);

                columnDescriptions.add(columnDescription);
            }

            return columnDescriptions;
        }

        return null;
    }


    /**
     * Return the requested data record.  The first record is record 0.  If the first line of the file is the column
     * names then record 0 is the line following the column names.
     *
     * @param dataRecordNumber long
     * @return List of strings, each string is the value from the column.
     * @throws FileException problem accessing the file
     * @throws FileReadException unable to find, open or read the file, or the file does not include the requested record.
     */
    @Override
    public List<String> readRecord(long  dataRecordNumber) throws FileException, FileReadException
    {
        return fileStoreConnector.readRecord(dataRecordNumber);
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

        File   fileStore = fileStoreConnector.getFile();
        String fileStoreName = fileStoreConnector.getFileName();


        StringBuilder stringBuilder = new StringBuilder();

        try (Scanner scanner = new Scanner(fileStore))
        {
            long rowCount = 0L;

            while (scanner.hasNext())
            {
                String readRow = scanner.nextLine();

                if (requestedRowNumber == rowCount)
                {
                    readRow = this.assembleRowData(dataValues).toString();
                }

                stringBuilder.append(readRow);
                rowCount ++;
            }

            if (rowCount < requestedRowNumber)
            {
                stringBuilder.append(this.assembleRowData(dataValues));
            }

            try (FileWriter writer = new FileWriter(fileStoreName, false))
            {
                writer.append(stringBuilder);
            }
            catch (IOException error)
            {
                throw new FileReadException(CSVFileConnectorErrorCode.UNEXPECTED_IO_EXCEPTION.getMessageDefinition(fileStoreName,
                                                                                                                   error.getMessage()),
                                            this.getClass().getName(),
                                            methodName,
                                            error,
                                            fileStoreName);
            }
        }
        catch (IOException  error)
        {
            throw new FileReadException(CSVFileConnectorErrorCode.UNEXPECTED_IO_EXCEPTION.getMessageDefinition(fileStoreName,
                                                                                                               error.getMessage()),
                                        this.getClass().getName(),
                                        methodName,
                                        error,
                                        fileStoreName);
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

        if (fileStoreConnector.getSuppliedColumnNames() != null)
        {
            /*
             * Column Names are fixed because they are defined in the configuration properties
             */
            throw new FileException(CSVFileConnectorErrorCode.FIXED_COLUMN_NAMES.getMessageDefinition(),
                                    this.getClass().getName(),
                                    methodName,
                                    fileStoreConnector.getFileName());
        }
        else
        {
            /*
             * The column names are stored in the first line of the CSV file
             */
            List<String> columnNames = new ArrayList<>();

            for (TabularColumnDescription columnDescription : columnDescriptions)
            {
                columnNames.add(columnDescription.columnName());
            }

            writeRecord(0, columnNames);
        }
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
        String fileStoreName = fileStoreConnector.getFileName();


        try (FileWriter writer = new FileWriter(fileStoreName, true))
        {
            writer.append(this.assembleRowData(dataValues));
        }
        catch (IOException error)
        {
            throw new FileReadException(CSVFileConnectorErrorCode.UNEXPECTED_IO_EXCEPTION.getMessageDefinition(fileStoreName,
                                                                                                               error.getMessage()),
                                        this.getClass().getName(),
                                        methodName,
                                        error,
                                        fileStoreName);
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
     * Assemble the data values into a legal CSV row.
     *
     * @param dataValues list of data values in the correct order
     * @return populated string buffer
     * todo handle quotes
     */
    private StringBuilder assembleRowData(List<String> dataValues)
    {
        StringBuilder stringBuilder = new StringBuilder();
        boolean       firstRecord   = true;

        for (String dataValue : dataValues)
        {
            if (firstRecord)
            {
                firstRecord = false;
            }
            else
            {
                stringBuilder.append(fileStoreConnector.getDelimiterChar());
            }
            stringBuilder.append(dataValue);
        }

        stringBuilder.append("\n");

        return stringBuilder;
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