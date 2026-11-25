/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.parquetfile;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataCollection;

import java.util.List;


/**
 * ParquetTabularDataSetCollectionConnector reads a collection of tabular data sets, each stored in an
 * Apache Parquet file.  These files are all in the same directory (folder). The name of the directory is supplied
 * through the configuration properties.  The name of each file is derived from the table name
 * (directoryPathName/tableName.parquet).  The caller uses getTableNames() to discover the tables in the
 * directory and setTableName() to step from file to file.
 */
public class ParquetTabularDataSetCollectionConnector extends ParquetTabularDataSetConnector implements ReadableTabularDataCollection
{
    /**
     * Return the names of the tabular data sets in this collection.  There is one for each Parquet file in
     * the directory, and its name is the name of the file without the file extension.
     *
     * @return sorted list of table names; empty if the collection has no members
     */
    @Override
    public List<String> getTableNames()
    {
        return super.getTableNamesInDirectory();
    }


    /**
     * Set up the table name for the tabular data set to focus on.
     *
     * @param tableName name of the table
     * @param tableDescription optional description - if null, a description is derived from the file
     * @throws ConnectorCheckedException problem using the name as a file name
     */
    @Override
    public void setTableName(String tableName,
                             String tableDescription) throws ConnectorCheckedException
    {
        super.tableName        = tableName;
        super.tableDescription = tableDescription;

        super.setFileStoreName(tableName);
    }
}
