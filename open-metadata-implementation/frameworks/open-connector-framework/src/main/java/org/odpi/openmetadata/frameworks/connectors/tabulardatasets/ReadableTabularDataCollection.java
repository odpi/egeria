/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors.tabulardatasets;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;

import java.util.List;

/**
 * ReadableTabularDataCollection is the connector API for reading a collection of tabular data sets - a schema
 * of tables, a folder of files, a product family - through a single connector.  It is the read-side counterpart
 * of {@link TabularDataCollection}, and works the same way: the connector focuses on one table at a time, and
 * the {@link ReadableTabularDataSource} methods describe and read the table in focus.
 * <br><br>
 * What the read side adds is the ability to say which tables there are.  A caller that copies a whole collection
 * asks for the table names, focuses on each in turn with {@link #setTableName(String, String)}, and reads it.
 * The names are in canonical word format - each word capitalized, with spaces between the words - so they can be
 * translated into the naming convention of whatever the data is copied into.
 */
public interface ReadableTabularDataCollection extends TabularDataCollection,
                                                       ReadableTabularDataSource
{
    /**
     * Return the names of the tabular data sets in this collection.  Each name can be passed to
     * {@link #setTableName(String, String)} to bring that data set into focus.
     *
     * @return list of table names in canonical word format; empty if the collection has no members
     * @throws ConnectorCheckedException data access problem
     */
    List<String> getTableNames() throws ConnectorCheckedException;
}
