/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datastore.csvfile;

import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStoreConnector;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.ffdc.exception.FileException;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.ffdc.exception.FileReadException;
import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.ffdc.CSVFileConnectorErrorCode;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionDetails;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


/**
 * CSVFileStoreConnector works with structured files to retrieve simple tables of data.
 */
public class CSVFileStoreConnector extends BasicFileStoreConnector implements CSVFileStore
{
    /*
     * Variables used in reading the file.
     */
    private List<String>   columnNames       = null;
    private char           delimiterChar     = ',';
    private char           quoteChar         = '"';

    /*
     * Variables used for logging and debug.
     */
    private static final Logger log = LoggerFactory.getLogger(CSVFileStoreConnector.class);


    /**
     * Initialize the connector.
     *
     * @param connectorInstanceId - unique id for the connector instance - useful for messages etc
     * @param connectionDetails - POJO for the configuration used to create the connector.
     */
    @Override
    public void initialize(String connectorInstanceId, ConnectionDetails connectionDetails)
    {
        super.initialize(connectorInstanceId, connectionDetails);

        Map<String, Object> configurationProperties = connectionDetails.getConfigurationProperties();
        EndpointDetails     endpoint                = connectionDetails.getEndpoint();

        if (configurationProperties != null)
        {
            Object  columnNamesProperty   = configurationProperties.get(CSVFileStoreProvider.columnNamesProperty);
            Object  delimiterCharProperty = configurationProperties.get(CSVFileStoreProvider.delimiterCharacterProperty);
            Object  quoteCharProperty     = configurationProperties.get(CSVFileStoreProvider.quoteCharacterProperty);

            if (columnNamesProperty != null)
            {
                if (columnNamesProperty instanceof List<?> columnNamesPropertyList)
                {
                    columnNames = new ArrayList<>();
                    for (Object   columnNameProperty : columnNamesPropertyList)
                    {
                        if (columnNameProperty != null)
                        {
                            columnNames.add(columnNameProperty.toString());
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
        }
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

            if ((rowCount > 0) && (columnNames == null))
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
     * Return the list of column names associated with this structured file.
     * This may be embedded in the first line of the file or encoded in the
     * connection object used to create a connector instance.
     *
     * @return a list of column names
     * @throws FileException problem accessing the file
     * @throws FileReadException unable to retrieve the column names
     */
    @Override
    public List<String>      getColumnNames() throws FileException,
                                                     FileReadException
    {
        final String  methodName = "getColumnNames";

        super.getFile(methodName);

        if (columnNames != null)
        {
            return columnNames;
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
    public List<String>      readRecord(long  dataRecordNumber) throws FileException, FileReadException
    {
        final String  methodName = "readRecord";

        if (columnNames == null)
        {
            return readRow(dataRecordNumber + 1, methodName);
        }
        else
        {
            return readRow(dataRecordNumber, methodName);
        }
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
    private List<String>      readRow(long    recordLocation,
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
