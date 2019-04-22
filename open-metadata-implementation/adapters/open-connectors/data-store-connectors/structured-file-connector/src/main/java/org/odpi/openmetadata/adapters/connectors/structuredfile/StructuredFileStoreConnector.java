/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.structuredfile;

import org.odpi.openmetadata.adapters.connectors.structuredfile.ffdc.StructuredFileConnectorErrorCode;
import org.odpi.openmetadata.adapters.connectors.structuredfile.ffdc.exception.FileReadException;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.properties.AdditionalProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * StructuredFileStoreConnector works with structured files to retrieve simple tables of data.
 */
public class StructuredFileStoreConnector extends ConnectorBase implements StructuredFileStore
{
    /*
     * Variables used in writing to the file.
     */
    private String         fileStoreName     = null;
    private List<String>   columnNames       = null;
    private char           delimiterChar     = ',';
    private char           quoteChar         = '"';

    /*
     * Variables used for logging and debug.
     */
    private static final Logger log = LoggerFactory.getLogger(StructuredFileStoreConnector.class);


    /**
     * Initialize the connector.
     *
     * @param connectorInstanceId - unique id for the connector instance - useful for messages etc
     * @param connectionProperties - POJO for the configuration used to create the connector.
     */
    @Override
    public void initialize(String connectorInstanceId, ConnectionProperties connectionProperties)
    {
        super.initialize(connectorInstanceId, connectionProperties);

        Map<String, Object> configurationProperties = connectionProperties.getConfigurationProperties();
        EndpointProperties  endpoint                = connectionProperties.getEndpoint();

        if (configurationProperties != null)
        {
            Object  columnNamesProperty = configurationProperties.get(StructuredFileStoreProvider.columnNamesProperty);
            Object  delimiterCharProperty = configurationProperties.get(StructuredFileStoreProvider.delimiterCharacterProperty);
            Object  quoteCharProperty = configurationProperties.get(StructuredFileStoreProvider.quoteCharacterProperty);

            if (columnNamesProperty != null)
            {
                if (columnNamesProperty instanceof List)
                {
                    columnNames = new ArrayList<>();
                    for (Object   columnNameProperty : (List)columnNamesProperty)
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
            fileStoreName = endpoint.getAddress();
        }

        else
        {
            log.error("Null endpoint");
        }

    }


    /**
     * Return the name of the file to read.
     *
     * @return file name
     * @throws FileReadException - the file name is null, the file does not exist, or is a directory or
     *                             is not readable.
     */
    public String   getFileName() throws FileReadException
    {
        final String  methodName = "getFileName";

        validateFileStore(fileStoreName, methodName);

        return fileStoreName;
    }


    /**
     * Return the last update data for the file.
     *
     * @return Date object
     * @throws FileReadException - the file name is null, the file does not exist, or is a directory or
     *                             is not readable.
     */
    public Date getLastUpdateDate() throws FileReadException
    {
        final String  methodName = "getLastUpdateDate";

        File fileStore = validateFileStore(fileStoreName, methodName);

        return new Date(fileStore.lastModified());
    }


    /**
     * Return the number of records in the file.  This is achieved by scanning the file and counting the records -
     * not recommended for very large files.
     *
     * @return count
     * @throws FileReadException unable to find, open or scan the file.
     */
    public long     getRecordCount() throws FileReadException
    {
        final String  methodName = "getRecordCount";

        long    rowCount = 0;

        File fileStore = validateFileStore(fileStoreName, methodName);

        try
        {
            Scanner scanner  = new Scanner(fileStore);
            while (scanner.hasNext())
            {
                scanner.nextLine();
                rowCount ++;
            }
            scanner.close();

            if ((rowCount > 0) && (columnNames == null))
            {
                rowCount = rowCount - 1;
            }
        }
        catch (IOException  error)
        {
            StructuredFileConnectorErrorCode errorCode    = StructuredFileConnectorErrorCode.UNEXPECTED_IO_EXCEPTION;
            String                           errorMessage = errorCode.getErrorMessageId()
                                                          + errorCode.getFormattedErrorMessage(fileStoreName,
                                                                                               error.getMessage());

            throw new FileReadException(errorCode.getHTTPErrorCode(),
                                        this.getClass().getName(),
                                        methodName,
                                        errorMessage,
                                        errorCode.getSystemAction(),
                                        errorCode.getUserAction(),
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
     * @throws FileReadException unable to retrieve the column names
     */
    public List<String>      getColumnNames() throws FileReadException
    {
        final String  methodName = "getColumnNames";

        validateFileStore(fileStoreName, methodName);

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
     * @throws FileReadException unable to find, open or read the file, or the file does not include the requested record.
     */
    public List<String>      readRecord(int  dataRecordNumber) throws FileReadException
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
     * @throws FileReadException unable to find, open or read the file, or the file does not include the requested record.
     */
    private List<String>      readRow(int     recordLocation,
                                      String  methodName) throws FileReadException
    {

        File fileStore = validateFileStore(fileStoreName, methodName);

        try
        {
            Scanner scanner        = new Scanner(fileStore);

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
            scanner.close();

            StructuredFileConnectorErrorCode errorCode    = StructuredFileConnectorErrorCode.FILE_TOO_SHORT;
            String                           errorMessage = errorCode.getErrorMessageId()
                                                          + errorCode.getFormattedErrorMessage(fileStoreName,
                                                                                               Integer.toString(recordLocation));

            throw new FileReadException(errorCode.getHTTPErrorCode(),
                                        this.getClass().getName(),
                                        methodName,
                                        errorMessage,
                                        errorCode.getSystemAction(),
                                        errorCode.getUserAction(),
                                        fileStoreName);

        }
        catch (IOException  error)
        {
            StructuredFileConnectorErrorCode errorCode    = StructuredFileConnectorErrorCode.UNEXPECTED_IO_EXCEPTION;
            String                           errorMessage = errorCode.getErrorMessageId()
                                                          + errorCode.getFormattedErrorMessage(fileStoreName,
                                                                                               error.getMessage());

            throw new FileReadException(errorCode.getHTTPErrorCode(),
                                        this.getClass().getName(),
                                        methodName,
                                        errorMessage,
                                        errorCode.getSystemAction(),
                                        errorCode.getUserAction(),
                                        error,
                                        fileStoreName);
        }
    }


    /**
     * Step through the record, character by character, extracting each column and enduring that escaped double quotes
     * and other tricks found in CSV files are handled.
     *
     * @param fileRecord a single record from the CSV file store
     * @return an array of column values extracted from the record
     */
    private  List<String> parseRecord(String fileRecord)
    {
        if ((fileRecord == null) || (fileRecord.isEmpty()))
        {
            return null;
        }

        List<String> result = new ArrayList<>();
        StringBuffer currentValue = new StringBuffer();

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
            } else {
                if (character == quoteChar) {

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

                    currentValue = new StringBuffer();
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
     * Close the config file
     */
    public void disconnect()
    {
        try
        {
            super.disconnect();
        }
        catch (Throwable  exec)
        {
            log.debug("Ignoring unexpected exception " + exec.getClass().getSimpleName() + " with message " + exec.getMessage());
        }

        log.debug("Closing Structured File Store");
    }


    /**
     * Validates that the file of the supplied file name can be open and read.
     *
     * @param fileStoreName name of the file
     * @return File object
     * @throws FileReadException null file name or file is actually a directory, or file not found or file not readable.
     */
    private File  validateFileStore(String   fileStoreName,
                                    String   methodName) throws FileReadException
    {
        if (fileStoreName == null)
        {
            StructuredFileConnectorErrorCode errorCode    = StructuredFileConnectorErrorCode.FILE_NOT_SPECIFIED;
            String                           errorMessage = errorCode.getErrorMessageId()
                                                          + errorCode.getFormattedErrorMessage(super.connectionBean.getQualifiedName());

            throw new FileReadException(errorCode.getHTTPErrorCode(),
                                        this.getClass().getName(),
                                        methodName,
                                        errorMessage,
                                        errorCode.getSystemAction(),
                                        errorCode.getUserAction(),
                                        null);
        }

        File  fileStore = new File(fileStoreName);

        if (! fileStore.exists())
        {
            StructuredFileConnectorErrorCode errorCode    = StructuredFileConnectorErrorCode.FILE_NOT_FOUND;
            String                           errorMessage = errorCode.getErrorMessageId()
                                                          + errorCode.getFormattedErrorMessage(fileStoreName,
                                                                                               super.connectionBean.getQualifiedName());

            throw new FileReadException(errorCode.getHTTPErrorCode(),
                                        this.getClass().getName(),
                                        methodName,
                                        errorMessage,
                                        errorCode.getSystemAction(),
                                        errorCode.getUserAction(),
                                        fileStoreName);
        }

        if (fileStore.isDirectory())
        {
            StructuredFileConnectorErrorCode errorCode    = StructuredFileConnectorErrorCode.DIRECTORY_SPECIFIED;
            String                           errorMessage = errorCode.getErrorMessageId()
                                                          + errorCode.getFormattedErrorMessage(fileStoreName,
                                                                                               super.connectionBean.getQualifiedName());

            throw new FileReadException(errorCode.getHTTPErrorCode(),
                                        this.getClass().getName(),
                                        methodName,
                                        errorMessage,
                                        errorCode.getSystemAction(),
                                        errorCode.getUserAction(),
                                        fileStoreName);
        }

        if (! fileStore.canRead())
        {
            StructuredFileConnectorErrorCode errorCode    = StructuredFileConnectorErrorCode.FILE_NOT_READABLE;
            String                           errorMessage = errorCode.getErrorMessageId()
                                                          + errorCode.getFormattedErrorMessage(fileStoreName,
                                                                                               super.connectionBean.getQualifiedName());

            throw new FileReadException(errorCode.getHTTPErrorCode(),
                                        this.getClass().getName(),
                                        methodName,
                                        errorMessage,
                                        errorCode.getSystemAction(),
                                        errorCode.getUserAction(),
                                        fileStoreName);
        }

        return fileStore;
    }
}
