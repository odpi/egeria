/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datastore.csvfile;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularDataCollection;


/**
 * CSVTabularDataSetCollectionConnector works with collections of structured CSV files to write/retrieve simple tables of data.
 * These files are all in the same directory (folder). The name of the folder is supplied through the configuration
 * properties.  The name of each file is derived from the supplied table name.  The initial value may be supplied as a configuration
 * property.  However, the caller typically uses the setTableName() method to step from file to file.
 */
public class CSVTabularDataSetCollectionConnector extends CSVTabularDataSetConnector implements TabularDataCollection
{
    /**
     * Set up the canonical table name for this data source.  Each word in the name should be capitalized, with spaces
     * between the words to allow translation between different naming conventions.
     *
     * @param tableName  string
     * @param tableDescription optional description
     * @throws ConnectorCheckedException there is a problem accessing the data
     */
    @Override
    public void setTableName(String tableName,
                             String tableDescription) throws ConnectorCheckedException
    {
        super.tableName = tableName;
        super.tableDescription = tableDescription;

        super.setFileStoreName(tableName);
    }
}