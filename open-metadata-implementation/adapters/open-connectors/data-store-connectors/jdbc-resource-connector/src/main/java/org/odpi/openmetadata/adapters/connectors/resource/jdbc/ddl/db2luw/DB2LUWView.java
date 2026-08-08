/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.db2luw;

import java.util.List;

/**
 * Defines the tables used in a database schema.
 */
public interface DB2LUWView
{

    /**
     * Return the name of the table.
     *
     * @return name
     */
    String getViewName();



    /**
     * Return the name of the table.
     *
     * @param schemaName name of schema
     * @return name
     */
    String getViewName(String schemaName);


    /**
     * Return the description of the table.
     *
     * @return text
     */
    String getViewDescription();


    /**
     * Return the columns that are primary keys.
     *
     * @return list of columns
     */
    List<DB2LUWTable> getTables();


    /**
     * Return the columns that are not primary keys.
     *
     * @return list of columns
     */
     List<DB2LUWColumn> getDataColumns();
}
