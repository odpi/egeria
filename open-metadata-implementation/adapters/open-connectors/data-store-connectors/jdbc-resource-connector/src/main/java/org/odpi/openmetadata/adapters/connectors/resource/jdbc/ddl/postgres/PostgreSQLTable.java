/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres;

import java.util.List;

/**
 * Defines the tables used in a database schema.
 */
public interface PostgreSQLTable
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
    List<PostgreSQLColumn> getPrimaryKeys();


    /**
     * Return the columns that are not primary keys.
     *
     * @return list of columns
     */
     List<PostgreSQLColumn> getDataColumns();


    /**
     * Return the list of foreign keys for this table.
     *
     * @return list
     */
     List<PostgreSQLForeignKey> getForeignKeys();
}
