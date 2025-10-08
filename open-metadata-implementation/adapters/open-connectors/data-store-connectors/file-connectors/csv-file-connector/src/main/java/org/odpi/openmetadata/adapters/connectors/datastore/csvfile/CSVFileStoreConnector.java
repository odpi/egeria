/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datastore.csvfile;

import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStoreConnector;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.ffdc.exception.FileException;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.ffdc.exception.FileReadException;
import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.ffdc.CSVFileConnectorErrorCode;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularColumnDescription;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.WritableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


/**
 * CSVFileStoreConnector works with structured files to retrieve simple tables of data.
 */
public class CSVFileStoreConnector extends BasicFileStoreConnector implements CSVFileStore,
                                                                              ReadableTabularDataSource,
                                                                              WritableTabularDataSource
{
    /*
     * Variables used in reading the file.
     */
    private List<String> suppliedColumnNames = null;
    private char         delimiterChar       = ',';
    private char         quoteChar         = '"';
    private String       tableName          = "Tabular File";
    private String       tableDescription   = null;

    /*
     * Variables used for logging and debug.
     */
    private static final Logger log = LoggerFactory.getLogger(CSVFileStoreConnector.class);


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

        Map<String, Object> configurationProperties = connectionBean.getConfigurationProperties();
        Endpoint            endpoint                = connectionBean.getEndpoint();

        if (configurationProperties != null)
        {
            Object  columnNamesProperty   = configurationProperties.get(CSVFileStoreProvider.columnNamesProperty);
            Object  delimiterCharProperty = configurationProperties.get(CSVFileStoreProvider.delimiterCharacterProperty);
            Object  quoteCharProperty     = configurationProperties.get(CSVFileStoreProvider.quoteCharacterProperty);

            if (columnNamesProperty != null)
            {
                if (columnNamesProperty instanceof List<?> columnNamesPropertyList)
                {
                    suppliedColumnNames = new ArrayList<>();
                    for (Object   columnNameProperty : columnNamesPropertyList)
                    {
                        if (columnNameProperty != null)
                        {
                            suppliedColumnNames.add(columnNameProperty.toString());
                        }
                    }
                }
            }

            if (delimiterCharProperty instanceof Character)
            {
                delimiterChar = (char)delimiterCharProperty;
            }

            if (quoteCharProperty instanceof Character)
            {
                quoteChar = (char)quoteCharProperty;
            }
        }

        if (endpoint != null)
        {
            super.fileStoreName = endpoint.getAddress();
        }
        else
        {
            log.error("Null endpoint");
            super.throwMissingEndpointAddress(this.getClass().getName(), methodName);
        }
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
    public long     getRecordCount() throws FileException, FileReadException
    {
        final String  methodName = "getRecordCount";

        long    rowCount = 0;

        File fileStore = getFile(methodName);

        try (Scanner scanner  = new Scanner(fileStore))
        {
            while (scanner.hasNext())
            {
                scanner.nextLine();
                rowCount ++;
            }

            if ((rowCount > 0) && (suppliedColumnNames == null))
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
     * @throws ConnectorCheckedException there is a problem accessing the data
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
     * @throws ConnectorCheckedException there is a problem accessing the data
     */
    @Override
    public String getTableDescription() throws ConnectorCheckedException
    {
        return tableDescription;
    }


    /**
     * Set up the canonical table name for this data source.  Each word in the name should be capitalized, with spaces
     * between the words to allow translation between different naming conventions.
     *
     * @param tableName  string
     * @param tableDescription optional description
     */
    @Override
    public void setTableName(String tableName,
                             String tableDescription)
    {
        this.tableName = tableName;
        this.tableDescription = tableDescription;
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
        List<String> columnNames = this.getColumnNames();

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
     * Return the list of column names associated with this structured file.
     * This may be embedded in the first line of the file or encoded in the
     * connection object used to create a connector instance.
     *
     * @return a list of column names
     * @throws FileException problem accessing the file
     * @throws FileReadException unable to retrieve the column names
     */
    @Override
    public List<String> getColumnNames() throws FileException, FileReadException
    {
        final String  methodName = "getColumnNames";

        super.getFile(methodName);

        if (suppliedColumnNames != null)
        {
            return suppliedColumnNames;
        }
        else
        {
            return readRow(0, methodName);
        }
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
        final String  methodName = "readRecord";

        if (suppliedColumnNames == null)
        {
            return readRow(dataRecordNumber + 1, methodName);
        }
        else
        {
            return readRow(dataRecordNumber, methodName);
        }
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

        if (suppliedColumnNames != null)
        {
            /*
             * Column Names are fixed because they are defined in the configuration properties
             */
            throw new FileException(CSVFileConnectorErrorCode.FIXED_COLUMN_NAMES.getMessageDefinition(),
                                    this.getClass().getName(),
                                    methodName,
                                    fileStoreName);
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

        File fileStore = getFile(methodName);

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
     * Write the requested data record to the end of the data source.
     *
     * @param dataValues Map of column descriptions to strings, each string is the value for the column.
     * @throws ConnectorCheckedException there is a problem accessing the data.
     */
    @Override
    public void appendRecord(List<String> dataValues) throws ConnectorCheckedException
    {
        final String methodName = "appendRecord";

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
     * @throws ConnectorCheckedException there is a problem accessing the data.
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
                stringBuilder.append(delimiterChar);
            }
            stringBuilder.append(dataValue);
        }

        stringBuilder.append("\n");

        return stringBuilder;
    }


    /**
     * Return the requested row in the file.  The first record is record 0.
     *
     * @param recordLocation long
     * @param methodName name of calling method
     * @return List of strings, each string is the value from the column.
     * @throws FileException problem accessing the file
     * @throws FileReadException unable to find, open or read the file, or the file does not include the requested record.
     */
    private List<String> readRow(long    recordLocation,
                                 String  methodName) throws FileException, FileReadException
    {
        File fileStore = super.getFile(methodName);

        try (Scanner scanner = new Scanner(fileStore))
        {

            int rowCounter = 0;
            while (scanner.hasNext())
            {
                if (rowCounter == recordLocation)
                {
                    return parseRecord(scanner.nextLine());
                }
                else
                {
                    scanner.nextLine();
                }
                rowCounter ++;
            }

            throw new FileReadException(CSVFileConnectorErrorCode.FILE_TOO_SHORT.getMessageDefinition(fileStoreName,
                                                                                                      Long.toString(recordLocation)),
                                        this.getClass().getName(),
                                        methodName,
                                        fileStoreName);

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
     * Step through the record, character by character, extracting each column and enduring that escaped double quotes
     * and other tricks found in CSV files are handled.
     *
     * @param fileRecord a single record from the CSV file store
     * @return a list of column values extracted from the record
     */
    private  List<String> parseRecord(String fileRecord)
    {
        if ((fileRecord == null) || (fileRecord.isEmpty()))
        {
            return null;
        }

        List<String>  result       = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();

        boolean inQuotes = false;
        boolean startCollectingCharacters = false;
        boolean doubleQuotesInColumn = false;

        char[] characters = fileRecord.toCharArray();

        for (char character : characters)
        {
            if (inQuotes)
            {
                startCollectingCharacters = true;
                if (character == quoteChar)
                {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                }
                else
                {
                    if (character == '\"')
                    {
                        if (!doubleQuotesInColumn)
                        {
                            currentValue.append(character);
                            doubleQuotesInColumn = true;
                        }
                    }
                    else
                    {
                        currentValue.append(character);
                    }

                }
            }
            else
            {
                if (character == quoteChar)
                {

                    inQuotes = true;

                    if (characters[0] != '"' && quoteChar == '\"')
                    {
                        currentValue.append('"');
                    }

                    if (startCollectingCharacters)
                    {
                        currentValue.append('"');
                    }
                }
                else if (character == delimiterChar)
                {
                    result.add(currentValue.toString());

                    currentValue = new StringBuilder();
                    startCollectingCharacters = false;

                }
                else if (character == '\n')
                {
                    break;
                }
                else if (character != '\r')
                {
                    currentValue.append(character);
                }
            }

        }

        result.add(currentValue.toString());

        return result;
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