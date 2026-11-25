/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datastore.parquetfile;

import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStoreConnector;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.ffdc.exception.FileException;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.ffdc.exception.FileReadException;
import org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.ffdc.ParquetFileConnectorErrorCode;
import org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.footer.ParquetFooterReader;
import org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.footer.ParquetFormatException;
import org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.properties.ParquetFileMetadata;

import org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.data.ParquetDataReader;
import org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.properties.ParquetSchemaElement;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularColumnDescription;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * ParquetFileStoreConnector works with Apache Parquet files to retrieve the description of the data they hold,
 * and the data itself.
 * <br><br>
 * The description comes from the file's footer, which the connector decodes itself.  The data is read using
 * DuckDB and is only available if the DuckDB JDBC driver is on the class path.
 */
public class ParquetFileStoreConnector extends BasicFileStoreConnector implements ParquetFileStore
{
    private final ParquetFooterReader footerReader = new ParquetFooterReader();
    private final ParquetDataReader   dataReader   = new ParquetDataReader();

    /*
     * The footer is cached because callers typically ask for several values from it.  The length and last
     * modified time of the file are saved with it so that a change to the file is detected.
     */
    private ParquetFileMetadata cachedFileMetadata   = null;
    private String              cachedFileName       = null;
    private long                cachedFileLength     = 0L;
    private long                cachedFileUpdateTime = 0L;


    /**
     * Default constructor.
     */
    public ParquetFileStoreConnector()
    {
    }


    /**
     * Return the description of the file's contents from its footer.
     *
     * @return file metadata
     * @throws FileException a problem accessing the file
     * @throws FileReadException unable to read the file, or it is not a Parquet file that the connector can describe
     */
    @Override
    public synchronized ParquetFileMetadata getFileMetadata() throws FileException, FileReadException
    {
        final String methodName = "getFileMetadata";

        File file = super.getFile(methodName);

        if ((cachedFileMetadata != null) &&
            (fileStoreName.equals(cachedFileName)) &&
            (file.length() == cachedFileLength) &&
            (file.lastModified() == cachedFileUpdateTime))
        {
            return cachedFileMetadata;
        }

        long fileLength     = file.length();
        long fileUpdateTime = file.lastModified();

        try
        {
            cachedFileMetadata   = footerReader.readFooter(file);
            cachedFileName       = fileStoreName;
            cachedFileLength     = fileLength;
            cachedFileUpdateTime = fileUpdateTime;

            return cachedFileMetadata;
        }
        catch (ParquetFormatException error)
        {
            ParquetFileConnectorErrorCode errorCode = ParquetFileConnectorErrorCode.NOT_PARQUET_FILE;

            if (error.getReason() == ParquetFormatException.Reason.CORRUPT_FOOTER)
            {
                errorCode = ParquetFileConnectorErrorCode.INVALID_FOOTER;
            }
            else if (error.getReason() == ParquetFormatException.Reason.ENCRYPTED_FOOTER)
            {
                throw new FileReadException(ParquetFileConnectorErrorCode.ENCRYPTED_FOOTER.getMessageDefinition(fileStoreName),
                                            this.getClass().getName(),
                                            methodName,
                                            error,
                                            fileStoreName);
            }

            throw new FileReadException(errorCode.getMessageDefinition(fileStoreName, error.getMessage()),
                                        this.getClass().getName(),
                                        methodName,
                                        error,
                                        fileStoreName);
        }
        catch (IOException error)
        {
            throw new FileReadException(ParquetFileConnectorErrorCode.UNEXPECTED_IO_EXCEPTION.getMessageDefinition(fileStoreName,
                                                                                                                   error.getMessage()),
                                        this.getClass().getName(),
                                        methodName,
                                        error,
                                        fileStoreName);
        }
    }


    /**
     * Return the number of rows in the file.  This value comes from the footer, so the size of the file makes no
     * difference to how long it takes.
     *
     * @return count
     * @throws FileException a problem accessing the file
     * @throws FileReadException unable to read the file, or it is not a Parquet file that the connector can describe
     */
    @Override
    public long getRecordCount() throws FileException, FileReadException
    {
        return this.getFileMetadata().getRowCount();
    }


    /**
     * Return the list of top-level column names in the file.
     *
     * @return a list of column names
     * @throws FileException a problem accessing the file
     * @throws FileReadException unable to read the file, or it is not a Parquet file that the connector can describe
     */
    @Override
    public List<String> getColumnNames() throws FileException, FileReadException
    {
        return this.getFileMetadata().getColumnNames();
    }


    /**
     * Return whether the rows of data in the file can be read.  This is true when the DuckDB JDBC driver
     * is on the class path.
     *
     * @return boolean
     */
    @Override
    public boolean isDataAccessAvailable()
    {
        return ParquetDataReader.isAvailable();
    }


    /**
     * Return the requested row of data.  The first row is row 0.
     *
     * @param rowNumber long
     * @return list of strings, each string is the value from a top-level column.  A null value is returned as null.
     *         The value of a column that holds nested data is formatted as a string.
     * @throws FileException a problem accessing the file
     * @throws FileReadException unable to read the data, or the file does not include the requested row.
     */
    @Override
    public List<String> readRecord(long rowNumber) throws FileException, FileReadException
    {
        final String methodName = "readRecord";

        File file = super.getFile(methodName);

        this.validateDataAccess(methodName);

        List<String> row;

        try
        {
            row = dataReader.readRow(file.getAbsolutePath(), rowNumber);
        }
        catch (SQLException error)
        {
            throw this.getDataReadException(error, methodName);
        }

        if (row == null)
        {
            throw new FileReadException(ParquetFileConnectorErrorCode.FILE_TOO_SHORT.getMessageDefinition(fileStoreName,
                                                                                                          Long.toString(rowNumber)),
                                        this.getClass().getName(),
                                        methodName,
                                        fileStoreName);
        }

        return row;
    }


    /**
     * Return how many times each value occurs in a top-level column, with the most frequent value first.
     * Nulls are not included.  This reads all the values in the column.
     *
     * @param columnName the name of a top-level column
     * @param maxDistinctValues the largest number of different values to return
     * @return map of value to the number of times it occurs, or null if the column has more
     *         than the maximum number of different values
     * @throws FileException a problem accessing the file
     * @throws FileReadException unable to read the data
     */
    @Override
    public Map<String, Long> getValueCounts(String columnName,
                                            int    maxDistinctValues) throws FileException, FileReadException
    {
        final String methodName = "getValueCounts";

        File file = super.getFile(methodName);

        this.validateDataAccess(methodName);

        try
        {
            return dataReader.getValueCounts(file.getAbsolutePath(), columnName, maxDistinctValues);
        }
        catch (SQLException error)
        {
            throw this.getDataReadException(error, methodName);
        }
    }


    /**
     * Return the table name for this data source.  This is the name of the file without its file extension.
     *
     * @return string
     * @throws FileException a problem accessing the file
     */
    @Override
    public String getTableName() throws FileException
    {
        final String methodName = "getTableName";

        String name    = super.getFile(methodName).getName();
        int    lastDot = name.lastIndexOf('.');

        if (lastDot > 0)
        {
            return name.substring(0, lastDot);
        }

        return name;
    }


    /**
     * Return the description for this data source.
     *
     * @return string
     * @throws FileException a problem accessing the file
     * @throws FileReadException unable to read the file, or it is not a Parquet file that the connector can describe
     */
    @Override
    public String getTableDescription() throws FileException, FileReadException
    {
        ParquetFileMetadata fileMetadata = this.getFileMetadata();

        String description = "Apache Parquet file " + fileStoreName + " containing " + fileMetadata.getRowCount() + " rows";

        if (fileMetadata.getCreatedBy() != null)
        {
            description = description + ", written by " + fileMetadata.getCreatedBy();
        }

        return description;
    }


    /**
     * Return the description of each of the top-level columns in the file.
     *
     * @return a list of column descriptions
     * @throws FileException a problem accessing the file
     * @throws FileReadException unable to read the file, or it is not a Parquet file that the connector can describe
     */
    @Override
    public List<TabularColumnDescription> getColumnDescriptions() throws FileException, FileReadException
    {
        List<TabularColumnDescription> columnDescriptions = new ArrayList<>();

        for (ParquetSchemaElement column : this.getFileMetadata().getColumns())
        {
            String description = "Parquet type: " + column.getDataType();

            columnDescriptions.add(new TabularColumnDescription(column.getName(),
                                                                this.getColumnDataType(column),
                                                                description,
                                                                column.isNullable(),
                                                                false));
        }

        return columnDescriptions;
    }


    /**
     * Return the number of the column with the requested name.  The first column is column 0.
     *
     * @param columnName name of the column
     * @return column number, or -1 if there is no column with this name
     * @throws FileException a problem accessing the file
     * @throws FileReadException unable to read the file, or it is not a Parquet file that the connector can describe
     */
    @Override
    public int getColumnNumber(String columnName) throws FileException, FileReadException
    {
        return this.getColumnNames().indexOf(columnName);
    }


    /**
     * Return the open metadata data type that is the closest match to the type of a column.
     * The values of a column are always returned as strings, and this type describes how to interpret them.
     *
     * @param column column from the file's schema
     * @return data type
     */
    private DataType getColumnDataType(ParquetSchemaElement column)
    {
        String dataType = column.getDataType();

        if (dataType.startsWith("decimal"))
        {
            return DataType.BIGDECIMAL;
        }

        switch (dataType)
        {
            case "boolean"                    -> { return DataType.BOOLEAN; }
            case "byte"                       -> { return DataType.BYTE; }
            case "short", "unsigned byte"     -> { return DataType.SHORT; }
            case "int", "unsigned short"      -> { return DataType.INT; }
            case "long", "unsigned int"       -> { return DataType.LONG; }
            case "unsigned long"              -> { return DataType.BIGINTEGER; }
            case "float"                      -> { return DataType.FLOAT; }
            case "double"                     -> { return DataType.DOUBLE; }
            case "date"                       -> { return DataType.LOCAL_DATE; }
            case "timestamp"                  ->
            {
                if (Boolean.FALSE.equals(column.getAdjustedToUTC()))
                {
                    return DataType.LOCAL_DATE_TIME;
                }

                return DataType.DATE;
            }
            default                           -> { return DataType.STRING; }
        }
    }


    /**
     * Check that the data in the file can be read.
     *
     * @param methodName calling method
     * @throws FileReadException the DuckDB JDBC driver is not available
     */
    private void validateDataAccess(String methodName) throws FileReadException
    {
        if (! ParquetDataReader.isAvailable())
        {
            throw new FileReadException(ParquetFileConnectorErrorCode.DATA_ACCESS_NOT_AVAILABLE.getMessageDefinition(fileStoreName),
                                        this.getClass().getName(),
                                        methodName,
                                        fileStoreName);
        }
    }


    /**
     * Return the exception that reports that DuckDB was not able to read the file.
     *
     * @param error exception from DuckDB
     * @param methodName calling method
     * @return exception to throw
     */
    private FileReadException getDataReadException(SQLException error,
                                                   String       methodName)
    {
        return new FileReadException(ParquetFileConnectorErrorCode.DATA_READ_FAILED.getMessageDefinition(fileStoreName,
                                                                                                         error.getMessage()),
                                     this.getClass().getName(),
                                     methodName,
                                     error,
                                     fileStoreName);
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     */
    @Override
    public void disconnect()
    {
        dataReader.close();

        super.disconnect();
    }
}
