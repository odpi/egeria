/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.oracle;

import java.util.List;

/**
 * Defines the tables used in a database schema.
 */
public interface OracleView
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
    List<OracleTable> getTables();


    /**
     * Return the columns that are not primary keys.
     *
     * @return list of columns
     */
     List<OracleColumn> getDataColumns();
}
