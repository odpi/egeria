/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.parquetfile;

import org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.ffdc.ParquetFileConnectorErrorCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularColumnDescription;
import org.odpi.openmetadata.frameworks.openmetadata.controls.TabularFileConfigurationProperty;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * ParquetTabularDataSetConnector provides read access to an Apache Parquet file as a tabular data set.
 * The file is located from the directoryPathName and tableName configuration properties
 * (directoryPathName/tableName.parquet) and the work of reading it is delegated to an embedded
 * ParquetFileStoreConnector.  Parquet files are immutable once written, so this connector is read-only.
 */
public class ParquetTabularDataSetConnector extends ConnectorBase implements ReadableTabularDataSource
{
    private static final String FILE_EXTENSION = ".parquet";

    private   String directoryPathName = ".";
    protected String tableName         = "Tabular File";
    protected String tableDescription  = null;

    protected ParquetFileStoreConnector fileStoreConnector = null;


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

        fileStoreConnector = this.getFileStoreConnector();

        directoryPathName = super.getStringConfigurationProperty(TabularFileConfigurationProperty.DIRECTORY_PATH_NAME.getName(),
                                                                 connectionBean.getConfigurationProperties(),
                                                                 directoryPathName);

        tableName = super.getStringConfigurationProperty(TabularFileConfigurationProperty.TABLE_NAME.getName(),
                                                         connectionBean.getConfigurationProperties(),
                                                         tableName);

        tableDescription = super.getStringConfigurationProperty(TabularFileConfigurationProperty.TABLE_DESCRIPTION.getName(),
                                                                connectionBean.getConfigurationProperties(),
                                                                tableDescription);

        this.setFileStoreName(tableName);
    }


    /**
     * Set up the file name to use for this tabular data set.
     *
     * @param tableName name of table
     * @throws ConnectorCheckedException problem using the name as a file name.
     */
    protected void setFileStoreName(String tableName) throws ConnectorCheckedException
    {
        fileStoreConnector.setFileStoreName(directoryPathName + "/" + tableName + FILE_EXTENSION);
    }


    /**
     * Return the names of the tables that have a Parquet file in the directory.  Each is the name of a file
     * without its file extension.
     *
     * @return sorted list of table names - empty if there are no Parquet files, or the directory does not exist
     */
    protected List<String> getTableNamesInDirectory()
    {
        List<String> tableNames = new ArrayList<>();

        File[] files = new File(directoryPathName).listFiles();

        if (files != null)
        {
            for (File file : files)
            {
                String fileName = file.getName();

                if ((file.isFile()) && (fileName.length() > FILE_EXTENSION.length()) && (fileName.endsWith(FILE_EXTENSION)))
                {
                    tableNames.add(fileName.substring(0, fileName.length() - FILE_EXTENSION.length()));
                }
            }
        }

        Collections.sort(tableNames);

        return tableNames;
    }


    /**
     * Retrieve the embedded Parquet File Store Connector.
     *
     * @return connector
     * @throws ConnectorCheckedException there is no embedded Parquet file store connector, or it failed to start
     * @throws UserNotAuthorizedException the embedded connector was disconnected before/during start
     */
    private ParquetFileStoreConnector getFileStoreConnector() throws ConnectorCheckedException,
                                                                     UserNotAuthorizedException
    {
        final String methodName = "getFileStoreConnector";

        if (embeddedConnectors != null)
        {
            for (Connector embeddedConnector : embeddedConnectors)
            {
                if (embeddedConnector instanceof ParquetFileStoreConnector parquetFileStoreConnector)
                {
                    if (! parquetFileStoreConnector.isActive())
                    {
                        parquetFileStoreConnector.start();
                    }

                    return parquetFileStoreConnector;
                }
            }
        }

        throw new ConnectorCheckedException(ParquetFileConnectorErrorCode.NO_EMBEDDED_FILE_STORE.getMessageDefinition(connectionBean.getDisplayName()),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Return whether the rows of data can be read.  This needs the DuckDB JDBC driver to be on the class path.
     * The description of the table (its name, columns and number of records) is available without it.
     *
     * @return boolean
     */
    public boolean isDataAccessAvailable()
    {
        return fileStoreConnector.isDataAccessAvailable();
    }


    /**
     * Return the number of records in the table.  This comes from the footer of the file.
     *
     * @return count
     * @throws ConnectorCheckedException problem reading the file
     */
    @Override
    public long getRecordCount() throws ConnectorCheckedException
    {
        return fileStoreConnector.getRecordCount();
    }


    /**
     * Return the table name for this data source.
     *
     * @return string
     */
    @Override
    public String getTableName()
    {
        return tableName;
    }


    /**
     * Return the description for this data source.  It is the description supplied in the configuration
     * properties, or a description derived from the file if none was configured.
     *
     * @return string
     * @throws ConnectorCheckedException problem reading the file
     */
    @Override
    public String getTableDescription() throws ConnectorCheckedException
    {
        if (tableDescription != null)
        {
            return tableDescription;
        }

        return fileStoreConnector.getTableDescription();
    }


    /**
     * Return the list of column descriptions associated with this data source.  There is one for each
     * top-level column in the file.
     *
     * @return a list of column descriptions
     * @throws ConnectorCheckedException problem reading the file
     */
    @Override
    public List<TabularColumnDescription> getColumnDescriptions() throws ConnectorCheckedException
    {
        return fileStoreConnector.getColumnDescriptions();
    }


    /**
     * Locate the named column. A negative number means the column is not present.
     *
     * @param columnName name of the column to return
     * @return column number (starting at 0)
     * @throws ConnectorCheckedException problem reading the file
     */
    @Override
    public int getColumnNumber(String columnName) throws ConnectorCheckedException
    {
        return fileStoreConnector.getColumnNumber(columnName);
    }


    /**
     * Return the requested data record.  The first record is record 0.
     *
     * @param rowNumber long
     * @return list of values (as strings) where each string is the value from a column.  The order is the same as the columns
     * @throws ConnectorCheckedException the row is not in the file, or the data can not be read
     */
    @Override
    public List<String> readRecord(long rowNumber) throws ConnectorCheckedException
    {
        return fileStoreConnector.readRecord(rowNumber);
    }
}
