/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.db2luw;

import java.util.List;

/**
 * Defines the tables used in a database schema.
 */
public interface DB2LUWTable
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
    List<DB2LUWColumn> getPrimaryKeys();


    /**
     * Return the columns that are not primary keys.
     *
     * @return list of columns
     */
     List<DB2LUWColumn> getDataColumns();


    /**
     * Return the columns that are added as an extension using ALTER TABLE.
     *
     * @return list of columns
     */
    List<DB2LUWColumn> getNewColumns();


    /**
     * Return the list of foreign keys for this table.
     *
     * @return list
     */
     List<DB2LUWForeignKey> getForeignKeys();
}
