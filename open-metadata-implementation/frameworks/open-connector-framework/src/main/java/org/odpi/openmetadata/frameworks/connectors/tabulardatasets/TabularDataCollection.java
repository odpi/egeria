/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors.tabulardatasets;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;

import java.util.List;

/**
 * TabularDataCollection defines the specific connector api for managing a collection of tabular data sets.
 * The setTableName method allows the connector to switch its attention from data set to data set.
 */
public interface TabularDataCollection
{
    /**
     * Set up the table name for the tabular data set to focus on.  This is in canonical word format where each word in the name
     * should be capitalized, with spaces between the words.
     * This format allows easy translation between different naming conventions.
     *
     * @param tableName name of the table
     * @param tableDescription optional description for the table - useful if the connector needs to set up a
     *                         definition of the table.
     * @throws ConnectorCheckedException there is a problem accessing the data
     */
    void setTableName(String tableName,
                      String tableDescription) throws ConnectorCheckedException;

}
