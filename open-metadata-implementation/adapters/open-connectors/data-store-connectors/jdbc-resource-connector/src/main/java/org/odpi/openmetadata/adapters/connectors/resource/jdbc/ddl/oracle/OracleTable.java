/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.oracle;

import java.util.List;

/**
 * Defines the tables used in a database schema.
 */
public interface OracleTable
{

    /**
     * Return the name of the table.
     *
     * @return name
     */
    String getTableName();



    /**
     * Return the name of the table.
     *
     * @param schemaName name of schema
     * @return name
     */
    String getTableName(String schemaName);


    /**
     * Return the description of the table.
     *
     * @return text
     */
    String getTableDescription();


    /**
     * Return the columns that are primary keys.
     *
     * @return list of columns
     */
    List<OracleColumn> getPrimaryKeys();


    /**
     * Return the columns that are not primary keys.
     *
     * @return list of columns
     */
     List<OracleColumn> getDataColumns();


    /**
     * Return the columns that are added as an extension using ALTER TABLE.
     *
     * @return list of columns
     */
    List<OracleColumn> getNewColumns();


    /**
     * Return the list of foreign keys for this table.
     *
     * @return list
     */
     List<OracleForeignKey> getForeignKeys();
}
